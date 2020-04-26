package io.realm;

import android.os.SystemClock;
import io.realm.BaseRealm.InstanceCallback;
import io.realm.exceptions.RealmFileException;
import io.realm.exceptions.RealmFileException.Kind;
import io.realm.internal.ObjectServerFacade;
import io.realm.internal.OsObjectStore;
import io.realm.internal.OsSharedRealm;
import io.realm.internal.RealmNotifier;
import io.realm.internal.Table;
import io.realm.internal.Util;
import io.realm.internal.android.AndroidCapabilities;
import io.realm.internal.android.AndroidRealmNotifier;
import io.realm.internal.async.RealmAsyncTaskImpl;
import io.realm.log.RealmLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

final class RealmCache {
    private static final String ASYNC_CALLBACK_NULL_MSG = "The callback cannot be null.";
    private static final String ASYNC_NOT_ALLOWED_MSG = "Realm instances cannot be loaded asynchronously on a non-looper thread.";
    private static final String DIFFERENT_KEY_MESSAGE = "Wrong key used to decrypt Realm.";
    private static final String WRONG_REALM_CLASS_MESSAGE = "The type of Realm class must be Realm or DynamicRealm.";
    private static final List<WeakReference<RealmCache>> cachesList = new ArrayList();
    private static final Collection<RealmCache> leakedCaches = new ConcurrentLinkedQueue();
    private RealmConfiguration configuration;
    private final AtomicBoolean isLeaked = new AtomicBoolean(false);
    private final String realmPath;
    private final EnumMap<RealmCacheType, RefAndCount> refAndCountMap;

    interface Callback {
        void onResult(int i);
    }

    interface Callback0 {
        void onCall();
    }

    private static class CreateRealmRunnable<T extends BaseRealm> implements Runnable {
        /* access modifiers changed from: private */
        public final InstanceCallback<T> callback;
        /* access modifiers changed from: private */
        public final CountDownLatch canReleaseBackgroundInstanceLatch = new CountDownLatch(1);
        /* access modifiers changed from: private */
        public final RealmConfiguration configuration;
        /* access modifiers changed from: private */
        public Future future;
        private final RealmNotifier notifier;
        /* access modifiers changed from: private */
        public final Class<T> realmClass;

        CreateRealmRunnable(RealmNotifier realmNotifier, RealmConfiguration realmConfiguration, InstanceCallback<T> instanceCallback, Class<T> cls) {
            this.configuration = realmConfiguration;
            this.realmClass = cls;
            this.callback = instanceCallback;
            this.notifier = realmNotifier;
        }

        public void setFuture(Future future2) {
            this.future = future2;
        }

        public void run() {
            BaseRealm baseRealm = null;
            try {
                baseRealm = RealmCache.createRealmOrGetFromCache(this.configuration, this.realmClass);
                if (!this.notifier.post(new Runnable() {
                    public void run() {
                        if (CreateRealmRunnable.this.future == null || CreateRealmRunnable.this.future.isCancelled()) {
                            CreateRealmRunnable.this.canReleaseBackgroundInstanceLatch.countDown();
                            return;
                        }
                        BaseRealm baseRealm = null;
                        try {
                            BaseRealm createRealmOrGetFromCache = RealmCache.createRealmOrGetFromCache(CreateRealmRunnable.this.configuration, CreateRealmRunnable.this.realmClass);
                            CreateRealmRunnable.this.canReleaseBackgroundInstanceLatch.countDown();
                            BaseRealm baseRealm2 = createRealmOrGetFromCache;
                            th = null;
                            baseRealm = baseRealm2;
                        } catch (Throwable th) {
                            th = th;
                            CreateRealmRunnable.this.canReleaseBackgroundInstanceLatch.countDown();
                        }
                        if (baseRealm != null) {
                            CreateRealmRunnable.this.callback.onSuccess(baseRealm);
                        } else {
                            CreateRealmRunnable.this.callback.onError(th);
                        }
                    }
                })) {
                    this.canReleaseBackgroundInstanceLatch.countDown();
                }
                if (!this.canReleaseBackgroundInstanceLatch.await(2, TimeUnit.SECONDS)) {
                    RealmLog.warn("Timeout for creating Realm instance in foreground thread in `CreateRealmRunnable` ", new Object[0]);
                }
                if (baseRealm == null) {
                    return;
                }
            } catch (InterruptedException e) {
                RealmLog.warn(e, "`CreateRealmRunnable` has been interrupted.", new Object[0]);
                if (baseRealm == null) {
                    return;
                }
            } catch (Throwable th) {
                if (baseRealm != null) {
                    baseRealm.close();
                }
                throw th;
            }
            baseRealm.close();
        }
    }

    private enum RealmCacheType {
        TYPED_REALM,
        DYNAMIC_REALM;

        static RealmCacheType valueOf(Class<? extends BaseRealm> cls) {
            if (cls == Realm.class) {
                return TYPED_REALM;
            }
            if (cls == DynamicRealm.class) {
                return DYNAMIC_REALM;
            }
            throw new IllegalArgumentException(RealmCache.WRONG_REALM_CLASS_MESSAGE);
        }
    }

    private static class RefAndCount {
        /* access modifiers changed from: private */
        public int globalCount;
        /* access modifiers changed from: private */
        public final ThreadLocal<Integer> localCount;
        /* access modifiers changed from: private */
        public final ThreadLocal<BaseRealm> localRealm;

        private RefAndCount() {
            this.localRealm = new ThreadLocal<>();
            this.localCount = new ThreadLocal<>();
            this.globalCount = 0;
        }
    }

    private RealmCache(String str) {
        this.realmPath = str;
        this.refAndCountMap = new EnumMap<>(RealmCacheType.class);
        for (RealmCacheType put : RealmCacheType.values()) {
            this.refAndCountMap.put(put, new RefAndCount());
        }
    }

    private static RealmCache getCache(String str, boolean z) {
        RealmCache realmCache;
        synchronized (cachesList) {
            Iterator it = cachesList.iterator();
            realmCache = null;
            while (it.hasNext()) {
                RealmCache realmCache2 = (RealmCache) ((WeakReference) it.next()).get();
                if (realmCache2 == null) {
                    it.remove();
                } else if (realmCache2.realmPath.equals(str)) {
                    realmCache = realmCache2;
                }
            }
            if (realmCache == null && z) {
                realmCache = new RealmCache(str);
                cachesList.add(new WeakReference(realmCache));
            }
        }
        return realmCache;
    }

    static <T extends BaseRealm> RealmAsyncTask createRealmOrGetFromCacheAsync(RealmConfiguration realmConfiguration, InstanceCallback<T> instanceCallback, Class<T> cls) {
        return getCache(realmConfiguration.getPath(), true).doCreateRealmOrGetFromCacheAsync(realmConfiguration, instanceCallback, cls);
    }

    private synchronized <T extends BaseRealm> RealmAsyncTask doCreateRealmOrGetFromCacheAsync(RealmConfiguration realmConfiguration, InstanceCallback<T> instanceCallback, Class<T> cls) {
        Future submitTransaction;
        AndroidCapabilities androidCapabilities = new AndroidCapabilities();
        androidCapabilities.checkCanDeliverNotification(ASYNC_NOT_ALLOWED_MSG);
        if (instanceCallback != null) {
            CreateRealmRunnable createRealmRunnable = new CreateRealmRunnable(new AndroidRealmNotifier(null, androidCapabilities), realmConfiguration, instanceCallback, cls);
            submitTransaction = BaseRealm.asyncTaskExecutor.submitTransaction(createRealmRunnable);
            createRealmRunnable.setFuture(submitTransaction);
        } else {
            throw new IllegalArgumentException(ASYNC_CALLBACK_NULL_MSG);
        }
        return new RealmAsyncTaskImpl(submitTransaction, BaseRealm.asyncTaskExecutor);
    }

    static <E extends BaseRealm> E createRealmOrGetFromCache(RealmConfiguration realmConfiguration, Class<E> cls) {
        return getCache(realmConfiguration.getPath(), true).doCreateRealmOrGetFromCache(realmConfiguration, cls);
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0057  */
    private synchronized <E extends BaseRealm> E doCreateRealmOrGetFromCache(RealmConfiguration realmConfiguration, Class<E> cls) {
        RefAndCount refAndCount;
        Object obj;
        OsSharedRealm instance;
        refAndCount = (RefAndCount) this.refAndCountMap.get(RealmCacheType.valueOf(cls));
        boolean z = !realmConfiguration.realmExists();
        if (getTotalGlobalRefCount() == 0) {
            copyAssetFileIfNeeded(realmConfiguration);
            OsSharedRealm osSharedRealm = null;
            try {
                if (realmConfiguration.isSyncConfiguration()) {
                    if (z) {
                        instance = OsSharedRealm.getInstance(realmConfiguration);
                        ObjectServerFacade.getSyncFacadeIfPossible().downloadInitialRemoteChanges(realmConfiguration);
                        osSharedRealm = instance;
                    }
                } else if (!z) {
                    osSharedRealm = OsSharedRealm.getInstance(realmConfiguration);
                    Table.migratePrimaryKeyTableIfNeeded(osSharedRealm);
                }
                if (osSharedRealm != null) {
                    osSharedRealm.close();
                }
                this.configuration = realmConfiguration;
            } catch (Throwable th) {
                th = th;
                if (osSharedRealm != null) {
                }
                throw th;
            }
        } else {
            validateConfiguration(realmConfiguration);
        }
        if (refAndCount.localRealm.get() == null) {
            if (cls == Realm.class) {
                obj = Realm.createInstance(this);
                synchronizeInitialSubscriptionsIfNeeded((Realm) obj, z);
            } else if (cls == DynamicRealm.class) {
                obj = DynamicRealm.createInstance(this);
            } else {
                throw new IllegalArgumentException(WRONG_REALM_CLASS_MESSAGE);
            }
            refAndCount.localRealm.set(obj);
            refAndCount.localCount.set(Integer.valueOf(0));
            refAndCount.globalCount = refAndCount.globalCount + 1;
        }
        refAndCount.localCount.set(Integer.valueOf(((Integer) refAndCount.localCount.get()).intValue() + 1));
        return (BaseRealm) refAndCount.localRealm.get();
    }

    private static void synchronizeInitialSubscriptionsIfNeeded(Realm realm, boolean z) {
        if (z) {
            try {
                ObjectServerFacade.getSyncFacadeIfPossible().downloadInitialSubscriptions(realm);
            } catch (Throwable unused) {
                realm.close();
                deleteRealmFileOnDisk(realm.getConfiguration());
            }
        }
    }

    private static void deleteRealmFileOnDisk(RealmConfiguration realmConfiguration) {
        int i = 5;
        boolean z = false;
        while (i > 0 && !z) {
            try {
                z = BaseRealm.deleteRealm(realmConfiguration);
            } catch (IllegalStateException unused) {
                i--;
                StringBuilder sb = new StringBuilder();
                sb.append("Sync server still holds a reference to the Realm. It cannot be deleted. Retrying ");
                sb.append(i);
                sb.append(" more times");
                RealmLog.warn(sb.toString(), new Object[0]);
                if (i > 0) {
                    SystemClock.sleep(15);
                }
            }
        }
        if (!z) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to delete the underlying Realm file: ");
            sb2.append(realmConfiguration.getPath());
            RealmLog.error(sb2.toString(), new Object[0]);
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00a5, code lost:
        return;
     */
    public synchronized void release(BaseRealm baseRealm) {
        String path = baseRealm.getPath();
        RefAndCount refAndCount = (RefAndCount) this.refAndCountMap.get(RealmCacheType.valueOf(baseRealm.getClass()));
        Integer num = (Integer) refAndCount.localCount.get();
        if (num == null) {
            num = Integer.valueOf(0);
        }
        if (num.intValue() <= 0) {
            RealmLog.warn("%s has been closed already. refCount is %s", path, num);
            return;
        }
        Integer valueOf = Integer.valueOf(num.intValue() - 1);
        if (valueOf.intValue() == 0) {
            refAndCount.localCount.set(null);
            refAndCount.localRealm.set(null);
            refAndCount.globalCount = refAndCount.globalCount - 1;
            if (refAndCount.globalCount >= 0) {
                baseRealm.doClose();
                if (getTotalGlobalRefCount() == 0) {
                    this.configuration = null;
                    ObjectServerFacade.getFacade(baseRealm.getConfiguration().isSyncConfiguration()).realmClosed(baseRealm.getConfiguration());
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Global reference counter of Realm");
                sb.append(path);
                sb.append(" got corrupted.");
                throw new IllegalStateException(sb.toString());
            }
        } else {
            refAndCount.localCount.set(valueOf);
        }
    }

    private void validateConfiguration(RealmConfiguration realmConfiguration) {
        if (!this.configuration.equals(realmConfiguration)) {
            if (Arrays.equals(this.configuration.getEncryptionKey(), realmConfiguration.getEncryptionKey())) {
                RealmMigration migration = realmConfiguration.getMigration();
                RealmMigration migration2 = this.configuration.getMigration();
                if (migration2 == null || migration == null || !migration2.getClass().equals(migration.getClass()) || migration.equals(migration2)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Configurations cannot be different if used to open the same file. \nCached configuration: \n");
                    sb.append(this.configuration);
                    sb.append("\n\nNew configuration: \n");
                    sb.append(realmConfiguration);
                    throw new IllegalArgumentException(sb.toString());
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Configurations cannot be different if used to open the same file. The most likely cause is that equals() and hashCode() are not overridden in the migration class: ");
                sb2.append(realmConfiguration.getMigration().getClass().getCanonicalName());
                throw new IllegalArgumentException(sb2.toString());
            }
            throw new IllegalArgumentException(DIFFERENT_KEY_MESSAGE);
        }
    }

    static void invokeWithGlobalRefCount(RealmConfiguration realmConfiguration, Callback callback) {
        synchronized (cachesList) {
            RealmCache cache = getCache(realmConfiguration.getPath(), false);
            if (cache == null) {
                callback.onResult(0);
            } else {
                cache.doInvokeWithGlobalRefCount(callback);
            }
        }
    }

    private synchronized void doInvokeWithGlobalRefCount(Callback callback) {
        callback.onResult(getTotalGlobalRefCount());
    }

    /* access modifiers changed from: 0000 */
    public synchronized void invokeWithLock(Callback0 callback0) {
        callback0.onCall();
    }

    private static void copyAssetFileIfNeeded(final RealmConfiguration realmConfiguration) {
        final File file = realmConfiguration.hasAssetFile() ? new File(realmConfiguration.getRealmDirectory(), realmConfiguration.getRealmFileName()) : null;
        final String syncServerCertificateAssetName = ObjectServerFacade.getFacade(realmConfiguration.isSyncConfiguration()).getSyncServerCertificateAssetName(realmConfiguration);
        final boolean z = !Util.isEmptyString(syncServerCertificateAssetName);
        if (file != null || z) {
            OsObjectStore.callWithLock(realmConfiguration, new Runnable() {
                public void run() {
                    if (file != null) {
                        RealmCache.copyFileIfNeeded(realmConfiguration.getAssetFilePath(), file);
                    }
                    if (z) {
                        RealmCache.copyFileIfNeeded(syncServerCertificateAssetName, new File(ObjectServerFacade.getFacade(realmConfiguration.isSyncConfiguration()).getSyncServerCertificateFilePath(realmConfiguration)));
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0087 A[SYNTHETIC, Splitter:B:45:0x0087] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x008e A[SYNTHETIC, Splitter:B:49:0x008e] */
    public static void copyFileIfNeeded(String str, File file) {
        FileOutputStream fileOutputStream;
        InputStream inputStream;
        if (!file.exists()) {
            IOException e = null;
            try {
                inputStream = BaseRealm.applicationContext.getAssets().open(str);
                if (inputStream != null) {
                    try {
                        fileOutputStream = new FileOutputStream(file);
                        try {
                            byte[] bArr = new byte[4096];
                            while (true) {
                                int read = inputStream.read(bArr);
                                if (read <= -1) {
                                    break;
                                }
                                fileOutputStream.write(bArr, 0, read);
                            }
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e2) {
                                    e = e2;
                                }
                            }
                            try {
                                fileOutputStream.close();
                            } catch (IOException e3) {
                                if (e == null) {
                                    e = e3;
                                }
                            }
                            if (e != null) {
                                throw new RealmFileException(Kind.ACCESS_ERROR, (Throwable) e);
                            }
                        } catch (IOException e4) {
                            e = e4;
                            try {
                                Kind kind = Kind.ACCESS_ERROR;
                                StringBuilder sb = new StringBuilder();
                                sb.append("Could not resolve the path to the asset file: ");
                                sb.append(str);
                                throw new RealmFileException(kind, sb.toString(), e);
                            } catch (Throwable th) {
                                th = th;
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (IOException e5) {
                                    }
                                }
                                if (fileOutputStream != null) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (IOException unused) {
                                    }
                                }
                                throw th;
                            }
                        }
                    } catch (IOException e6) {
                        e = e6;
                        fileOutputStream = null;
                        Kind kind2 = Kind.ACCESS_ERROR;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Could not resolve the path to the asset file: ");
                        sb2.append(str);
                        throw new RealmFileException(kind2, sb2.toString(), e);
                    } catch (Throwable th2) {
                        th = th2;
                        fileOutputStream = null;
                        if (inputStream != null) {
                        }
                        if (fileOutputStream != null) {
                        }
                        throw th;
                    }
                } else {
                    Kind kind3 = Kind.ACCESS_ERROR;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Invalid input stream to the asset file: ");
                    sb3.append(str);
                    throw new RealmFileException(kind3, sb3.toString());
                }
            } catch (IOException e7) {
                e = e7;
                inputStream = null;
                fileOutputStream = null;
                Kind kind22 = Kind.ACCESS_ERROR;
                StringBuilder sb22 = new StringBuilder();
                sb22.append("Could not resolve the path to the asset file: ");
                sb22.append(str);
                throw new RealmFileException(kind22, sb22.toString(), e);
            } catch (Throwable th3) {
                th = th3;
                inputStream = null;
                fileOutputStream = null;
                if (inputStream != null) {
                }
                if (fileOutputStream != null) {
                }
                throw th;
            }
        }
    }

    static int getLocalThreadCount(RealmConfiguration realmConfiguration) {
        RealmCache cache = getCache(realmConfiguration.getPath(), false);
        if (cache == null) {
            return 0;
        }
        int i = 0;
        for (RefAndCount access$700 : cache.refAndCountMap.values()) {
            Integer num = (Integer) access$700.localCount.get();
            i += num != null ? num.intValue() : 0;
        }
        return i;
    }

    public RealmConfiguration getConfiguration() {
        return this.configuration;
    }

    private int getTotalGlobalRefCount() {
        int i = 0;
        for (RefAndCount access$800 : this.refAndCountMap.values()) {
            i += access$800.globalCount;
        }
        return i;
    }

    /* access modifiers changed from: 0000 */
    public void leak() {
        if (!this.isLeaked.getAndSet(true)) {
            leakedCaches.add(this);
        }
    }
}
