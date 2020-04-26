package io.realm;

import android.content.Context;
import android.os.Looper;
import io.reactivex.Flowable;
import io.realm.Realm.Transaction;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.CheckedRow;
import io.realm.internal.ColumnInfo;
import io.realm.internal.InvalidRow;
import io.realm.internal.ObjectServerFacade;
import io.realm.internal.OsObjectStore;
import io.realm.internal.OsRealmConfig.Builder;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.OsSharedRealm;
import io.realm.internal.OsSharedRealm.InitializationCallback;
import io.realm.internal.OsSharedRealm.MigrationCallback;
import io.realm.internal.OsSharedRealm.SchemaChangedCallback;
import io.realm.internal.Row;
import io.realm.internal.Table;
import io.realm.internal.UncheckedRow;
import io.realm.internal.Util;
import io.realm.internal.async.RealmThreadPoolExecutor;
import io.realm.log.RealmLog;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;

abstract class BaseRealm implements Closeable {
    static final String CLOSED_REALM_MESSAGE = "This Realm instance has already been closed, making it unusable.";
    static final String DELETE_NOT_SUPPORTED_UNDER_PARTIAL_SYNC = "This API is not supported by partially synchronized Realms. Either unsubscribe using 'Realm.unsubscribeAsync()' or delete the objects using a query and 'RealmResults.deleteAllFromRealm()'";
    private static final String INCORRECT_THREAD_CLOSE_MESSAGE = "Realm access from incorrect thread. Realm instance can only be closed on the thread it was created.";
    static final String INCORRECT_THREAD_MESSAGE = "Realm access from incorrect thread. Realm objects can only be accessed on the thread they were created.";
    static final String LISTENER_NOT_ALLOWED_MESSAGE = "Listeners cannot be used on current thread.";
    private static final String NOT_IN_TRANSACTION_MESSAGE = "Changing Realm data can only be done from inside a transaction.";
    static volatile Context applicationContext;
    static final RealmThreadPoolExecutor asyncTaskExecutor = RealmThreadPoolExecutor.newDefaultExecutor();
    public static final ThreadLocalRealmObjectContext objectContext = new ThreadLocalRealmObjectContext();
    protected final RealmConfiguration configuration;
    private RealmCache realmCache;
    private SchemaChangedCallback schemaChangedCallback;
    public OsSharedRealm sharedRealm;
    private boolean shouldCloseSharedRealm;
    final long threadId;

    public static abstract class InstanceCallback<T extends BaseRealm> {
        public abstract void onSuccess(T t);

        public void onError(Throwable th) {
            throw new RealmException("Exception happens when initializing Realm in the background thread.", th);
        }
    }

    public static final class RealmObjectContext {
        private boolean acceptDefaultValue;
        private ColumnInfo columnInfo;
        private List<String> excludeFields;
        private BaseRealm realm;
        private Row row;

        public void set(BaseRealm baseRealm, Row row2, ColumnInfo columnInfo2, boolean z, List<String> list) {
            this.realm = baseRealm;
            this.row = row2;
            this.columnInfo = columnInfo2;
            this.acceptDefaultValue = z;
            this.excludeFields = list;
        }

        /* access modifiers changed from: 0000 */
        public BaseRealm getRealm() {
            return this.realm;
        }

        public Row getRow() {
            return this.row;
        }

        public ColumnInfo getColumnInfo() {
            return this.columnInfo;
        }

        public boolean getAcceptDefaultValue() {
            return this.acceptDefaultValue;
        }

        public List<String> getExcludeFields() {
            return this.excludeFields;
        }

        public void clear() {
            this.realm = null;
            this.row = null;
            this.columnInfo = null;
            this.acceptDefaultValue = false;
            this.excludeFields = null;
        }
    }

    static final class ThreadLocalRealmObjectContext extends ThreadLocal<RealmObjectContext> {
        ThreadLocalRealmObjectContext() {
        }

        /* access modifiers changed from: protected */
        public RealmObjectContext initialValue() {
            return new RealmObjectContext();
        }
    }

    public abstract Flowable asFlowable();

    public abstract RealmSchema getSchema();

    public abstract boolean isEmpty();

    BaseRealm(RealmCache realmCache2, @Nullable OsSchemaInfo osSchemaInfo) {
        this(realmCache2.getConfiguration(), osSchemaInfo);
        this.realmCache = realmCache2;
    }

    BaseRealm(RealmConfiguration realmConfiguration, @Nullable OsSchemaInfo osSchemaInfo) {
        this.schemaChangedCallback = new SchemaChangedCallback() {
            public void onSchemaChanged() {
                RealmSchema schema = BaseRealm.this.getSchema();
                if (schema != null) {
                    schema.refresh();
                }
            }
        };
        this.threadId = Thread.currentThread().getId();
        this.configuration = realmConfiguration;
        AnonymousClass2 r0 = null;
        this.realmCache = null;
        MigrationCallback createMigrationCallback = (osSchemaInfo == null || realmConfiguration.getMigration() == null) ? null : createMigrationCallback(realmConfiguration.getMigration());
        final Transaction initialDataTransaction = realmConfiguration.getInitialDataTransaction();
        if (initialDataTransaction != null) {
            r0 = new InitializationCallback() {
                public void onInit(OsSharedRealm osSharedRealm) {
                    initialDataTransaction.execute(Realm.createInstance(osSharedRealm));
                }
            };
        }
        this.sharedRealm = OsSharedRealm.getInstance(new Builder(realmConfiguration).fifoFallbackDir(new File(applicationContext.getFilesDir(), ".realm.temp")).autoUpdateNotification(true).migrationCallback(createMigrationCallback).schemaInfo(osSchemaInfo).initializationCallback(r0));
        this.shouldCloseSharedRealm = true;
        this.sharedRealm.registerSchemaChangedCallback(this.schemaChangedCallback);
    }

    BaseRealm(OsSharedRealm osSharedRealm) {
        this.schemaChangedCallback = new SchemaChangedCallback() {
            public void onSchemaChanged() {
                RealmSchema schema = BaseRealm.this.getSchema();
                if (schema != null) {
                    schema.refresh();
                }
            }
        };
        this.threadId = Thread.currentThread().getId();
        this.configuration = osSharedRealm.getConfiguration();
        this.realmCache = null;
        this.sharedRealm = osSharedRealm;
        this.shouldCloseSharedRealm = false;
    }

    public void setAutoRefresh(boolean z) {
        checkIfValid();
        this.sharedRealm.setAutoRefresh(z);
    }

    public boolean isAutoRefresh() {
        return this.sharedRealm.isAutoRefresh();
    }

    public void refresh() {
        checkIfValid();
        if (!isInTransaction()) {
            this.sharedRealm.refresh();
            return;
        }
        throw new IllegalStateException("Cannot refresh a Realm instance inside a transaction.");
    }

    public boolean isInTransaction() {
        checkIfValid();
        return this.sharedRealm.isInTransaction();
    }

    /* access modifiers changed from: protected */
    public <T extends BaseRealm> void addListener(RealmChangeListener<T> realmChangeListener) {
        if (realmChangeListener != null) {
            checkIfValid();
            this.sharedRealm.capabilities.checkCanDeliverNotification(LISTENER_NOT_ALLOWED_MESSAGE);
            this.sharedRealm.realmNotifier.addChangeListener(this, realmChangeListener);
            return;
        }
        throw new IllegalArgumentException("Listener should not be null");
    }

    /* access modifiers changed from: protected */
    public <T extends BaseRealm> void removeListener(RealmChangeListener<T> realmChangeListener) {
        if (realmChangeListener != null) {
            if (isClosed()) {
                RealmLog.warn("Calling removeChangeListener on a closed Realm %s, make sure to close all listeners before closing the Realm.", this.configuration.getPath());
            }
            this.sharedRealm.realmNotifier.removeChangeListener(this, realmChangeListener);
            return;
        }
        throw new IllegalArgumentException("Listener should not be null");
    }

    /* access modifiers changed from: protected */
    public void removeAllListeners() {
        if (isClosed()) {
            RealmLog.warn("Calling removeChangeListener on a closed Realm %s, make sure to close all listeners before closing the Realm.", this.configuration.getPath());
        }
        this.sharedRealm.realmNotifier.removeChangeListeners(this);
    }

    public void writeCopyTo(File file) {
        if (file != null) {
            checkIfValid();
            this.sharedRealm.writeCopy(file, null);
            return;
        }
        throw new IllegalArgumentException("The destination argument cannot be null");
    }

    public void writeEncryptedCopyTo(File file, byte[] bArr) {
        if (file != null) {
            checkIfValid();
            this.sharedRealm.writeCopy(file, bArr);
            return;
        }
        throw new IllegalArgumentException("The destination argument cannot be null");
    }

    public boolean waitForChange() {
        checkIfValid();
        if (isInTransaction()) {
            throw new IllegalStateException("Cannot wait for changes inside of a transaction.");
        } else if (Looper.myLooper() == null) {
            boolean waitForChange = this.sharedRealm.waitForChange();
            if (waitForChange) {
                this.sharedRealm.refresh();
            }
            return waitForChange;
        } else {
            throw new IllegalStateException("Cannot wait for changes inside a Looper thread. Use RealmChangeListeners instead.");
        }
    }

    public void stopWaitForChange() {
        RealmCache realmCache2 = this.realmCache;
        if (realmCache2 != null) {
            realmCache2.invokeWithLock(new Callback0() {
                public void onCall() {
                    if (BaseRealm.this.sharedRealm == null || BaseRealm.this.sharedRealm.isClosed()) {
                        throw new IllegalStateException(BaseRealm.CLOSED_REALM_MESSAGE);
                    }
                    BaseRealm.this.sharedRealm.stopWaitForChange();
                }
            });
            return;
        }
        throw new IllegalStateException(CLOSED_REALM_MESSAGE);
    }

    public void beginTransaction() {
        checkIfValid();
        this.sharedRealm.beginTransaction();
    }

    public void commitTransaction() {
        checkIfValid();
        this.sharedRealm.commitTransaction();
    }

    public void cancelTransaction() {
        checkIfValid();
        this.sharedRealm.cancelTransaction();
    }

    /* access modifiers changed from: protected */
    public void checkIfValid() {
        OsSharedRealm osSharedRealm = this.sharedRealm;
        if (osSharedRealm == null || osSharedRealm.isClosed()) {
            throw new IllegalStateException(CLOSED_REALM_MESSAGE);
        } else if (this.threadId != Thread.currentThread().getId()) {
            throw new IllegalStateException(INCORRECT_THREAD_MESSAGE);
        }
    }

    /* access modifiers changed from: protected */
    public void checkIfInTransaction() {
        if (!this.sharedRealm.isInTransaction()) {
            throw new IllegalStateException(NOT_IN_TRANSACTION_MESSAGE);
        }
    }

    /* access modifiers changed from: protected */
    public void checkIfPartialRealm() {
        if (!(this.configuration.isSyncConfiguration() ? ObjectServerFacade.getSyncFacadeIfPossible().isPartialRealm(this.configuration) : false)) {
            throw new IllegalStateException("This method is only available on partially synchronized Realms.");
        }
    }

    /* access modifiers changed from: protected */
    public void checkIfValidAndInTransaction() {
        if (!isInTransaction()) {
            throw new IllegalStateException(NOT_IN_TRANSACTION_MESSAGE);
        }
    }

    /* access modifiers changed from: 0000 */
    public void checkNotInSync() {
        if (this.configuration.isSyncConfiguration()) {
            throw new IllegalArgumentException("You cannot perform changes to a schema. Please update app and restart.");
        }
    }

    public String getPath() {
        return this.configuration.getPath();
    }

    public RealmConfiguration getConfiguration() {
        return this.configuration;
    }

    public long getVersion() {
        return OsObjectStore.getSchemaVersion(this.sharedRealm);
    }

    public void close() {
        if (this.threadId == Thread.currentThread().getId()) {
            RealmCache realmCache2 = this.realmCache;
            if (realmCache2 != null) {
                realmCache2.release(this);
            } else {
                doClose();
            }
        } else {
            throw new IllegalStateException(INCORRECT_THREAD_CLOSE_MESSAGE);
        }
    }

    /* access modifiers changed from: 0000 */
    public void doClose() {
        this.realmCache = null;
        OsSharedRealm osSharedRealm = this.sharedRealm;
        if (osSharedRealm != null && this.shouldCloseSharedRealm) {
            osSharedRealm.close();
            this.sharedRealm = null;
        }
    }

    public boolean isClosed() {
        if (this.threadId == Thread.currentThread().getId()) {
            OsSharedRealm osSharedRealm = this.sharedRealm;
            return osSharedRealm == null || osSharedRealm.isClosed();
        }
        throw new IllegalStateException(INCORRECT_THREAD_MESSAGE);
    }

    /* access modifiers changed from: 0000 */
    public <E extends RealmModel> E get(@Nullable Class<E> cls, @Nullable String str, UncheckedRow uncheckedRow) {
        if (str != null) {
            return new DynamicRealmObject(this, CheckedRow.getFromRow(uncheckedRow));
        }
        return this.configuration.getSchemaMediator().newInstance(cls, this, uncheckedRow, getSchema().getColumnInfo(cls), false, Collections.emptyList());
    }

    /* access modifiers changed from: 0000 */
    public <E extends RealmModel> E get(Class<E> cls, long j, boolean z, List<String> list) {
        return this.configuration.getSchemaMediator().newInstance(cls, this, getSchema().getTable(cls).getUncheckedRow(j), getSchema().getColumnInfo(cls), z, list);
    }

    /* access modifiers changed from: 0000 */
    public <E extends RealmModel> E get(@Nullable Class<E> cls, @Nullable String str, long j) {
        boolean z = str != null;
        Table table = z ? getSchema().getTable(str) : getSchema().getTable(cls);
        if (z) {
            return new DynamicRealmObject(this, j != -1 ? table.getCheckedRow(j) : InvalidRow.INSTANCE);
        }
        return this.configuration.getSchemaMediator().newInstance(cls, this, j != -1 ? table.getUncheckedRow(j) : InvalidRow.INSTANCE, getSchema().getColumnInfo(cls), false, Collections.emptyList());
    }

    public void deleteAll() {
        checkIfValid();
        if (!this.sharedRealm.isPartial()) {
            boolean isPartial = this.sharedRealm.isPartial();
            for (RealmObjectSchema className : getSchema().getAll()) {
                getSchema().getTable(className.getClassName()).clear(isPartial);
            }
            return;
        }
        throw new IllegalStateException(DELETE_NOT_SUPPORTED_UNDER_PARTIAL_SYNC);
    }

    static boolean deleteRealm(final RealmConfiguration realmConfiguration) {
        final AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        if (OsObjectStore.callWithLock(realmConfiguration, new Runnable() {
            public void run() {
                atomicBoolean.set(Util.deleteRealm(RealmConfiguration.this.getPath(), RealmConfiguration.this.getRealmDirectory(), RealmConfiguration.this.getRealmFileName()));
            }
        })) {
            return atomicBoolean.get();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("It's not allowed to delete the file associated with an open Realm. Remember to close() all the instances of the Realm before deleting its file: ");
        sb.append(realmConfiguration.getPath());
        throw new IllegalStateException(sb.toString());
    }

    static boolean compactRealm(RealmConfiguration realmConfiguration) {
        OsSharedRealm instance = OsSharedRealm.getInstance(realmConfiguration);
        Boolean valueOf = Boolean.valueOf(instance.compact());
        instance.close();
        return valueOf.booleanValue();
    }

    protected static void migrateRealm(final RealmConfiguration realmConfiguration, @Nullable final RealmMigration realmMigration) throws FileNotFoundException {
        if (realmConfiguration == null) {
            throw new IllegalArgumentException("RealmConfiguration must be provided");
        } else if (realmConfiguration.isSyncConfiguration()) {
            throw new IllegalArgumentException("Manual migrations are not supported for synced Realms");
        } else if (realmMigration == null && realmConfiguration.getMigration() == null) {
            throw new RealmMigrationNeededException(realmConfiguration.getPath(), "RealmMigration must be provided.");
        } else {
            final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            RealmCache.invokeWithGlobalRefCount(realmConfiguration, new Callback() {
                public void onResult(int i) {
                    if (i != 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Cannot migrate a Realm file that is already open: ");
                        sb.append(RealmConfiguration.this.getPath());
                        throw new IllegalStateException(sb.toString());
                    } else if (!new File(RealmConfiguration.this.getPath()).exists()) {
                        atomicBoolean.set(true);
                    } else {
                        OsSchemaInfo osSchemaInfo = new OsSchemaInfo(RealmConfiguration.this.getSchemaMediator().getExpectedObjectSchemaInfoMap().values());
                        MigrationCallback migrationCallback = null;
                        RealmMigration realmMigration = realmMigration;
                        if (realmMigration == null) {
                            realmMigration = RealmConfiguration.this.getMigration();
                        }
                        if (realmMigration != null) {
                            migrationCallback = BaseRealm.createMigrationCallback(realmMigration);
                        }
                        OsSharedRealm instance = OsSharedRealm.getInstance(new Builder(RealmConfiguration.this).autoUpdateNotification(false).schemaInfo(osSchemaInfo).migrationCallback(migrationCallback));
                        if (instance != null) {
                            instance.close();
                        }
                    }
                }
            });
            if (atomicBoolean.get()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot migrate a Realm file which doesn't exist: ");
                sb.append(realmConfiguration.getPath());
                throw new FileNotFoundException(sb.toString());
            }
        }
    }

    /* access modifiers changed from: private */
    public static MigrationCallback createMigrationCallback(final RealmMigration realmMigration) {
        return new MigrationCallback() {
            public void onMigrationNeeded(OsSharedRealm osSharedRealm, long j, long j2) {
                RealmMigration.this.migrate(DynamicRealm.createInstance(osSharedRealm), j, j2);
            }
        };
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        if (this.shouldCloseSharedRealm) {
            OsSharedRealm osSharedRealm = this.sharedRealm;
            if (osSharedRealm != null && !osSharedRealm.isClosed()) {
                RealmLog.warn("Remember to call close() on all Realm instances. Realm %s is being finalized without being closed, this can lead to running out of native memory.", this.configuration.getPath());
                RealmCache realmCache2 = this.realmCache;
                if (realmCache2 != null) {
                    realmCache2.leak();
                }
            }
        }
        super.finalize();
    }

    /* access modifiers changed from: 0000 */
    public OsSharedRealm getSharedRealm() {
        return this.sharedRealm;
    }
}
