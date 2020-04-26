package io.realm;

import android.content.Context;
import io.realm.Realm.Transaction;
import io.realm.annotations.RealmModule;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmFileException;
import io.realm.exceptions.RealmFileException.Kind;
import io.realm.internal.OsRealmConfig.Durability;
import io.realm.internal.RealmCore;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Util;
import io.realm.internal.modules.CompositeMediator;
import io.realm.internal.modules.FilterableMediator;
import io.realm.rx.RealmObservableFactory;
import io.realm.rx.RxObservableFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.annotation.Nullable;

public class RealmConfiguration {
    /* access modifiers changed from: private */
    public static final Object DEFAULT_MODULE = Realm.getDefaultModule();
    protected static final RealmProxyMediator DEFAULT_MODULE_MEDIATOR;
    public static final String DEFAULT_REALM_NAME = "default.realm";
    public static final int KEY_LENGTH = 64;
    private static Boolean rxJavaAvailable;
    private final String assetFilePath;
    private final String canonicalPath;
    private final CompactOnLaunchCallback compactOnLaunch;
    private final boolean deleteRealmIfMigrationNeeded;
    private final Durability durability;
    private final Transaction initialDataTransaction;
    private final boolean isRecoveryConfiguration;
    private final byte[] key;
    private final RealmMigration migration;
    private final boolean readOnly;
    private final File realmDirectory;
    private final String realmFileName;
    private final RxObservableFactory rxObservableFactory;
    private final RealmProxyMediator schemaMediator;
    private final long schemaVersion;

    public static class Builder {
        private String assetFilePath;
        private CompactOnLaunchCallback compactOnLaunch;
        private HashSet<Class<? extends RealmModel>> debugSchema;
        private boolean deleteRealmIfMigrationNeeded;
        private File directory;
        private Durability durability;
        private String fileName;
        private Transaction initialDataTransaction;
        private byte[] key;
        private RealmMigration migration;
        private HashSet<Object> modules;
        private boolean readOnly;
        private RxObservableFactory rxFactory;
        private long schemaVersion;

        public Builder() {
            this(BaseRealm.applicationContext);
        }

        Builder(Context context) {
            this.modules = new HashSet<>();
            this.debugSchema = new HashSet<>();
            if (context != null) {
                RealmCore.loadLibrary(context);
                initializeBuilder(context);
                return;
            }
            throw new IllegalStateException("Call `Realm.init(Context)` before creating a RealmConfiguration");
        }

        private void initializeBuilder(Context context) {
            this.directory = context.getFilesDir();
            this.fileName = "default.realm";
            this.key = null;
            this.schemaVersion = 0;
            this.migration = null;
            this.deleteRealmIfMigrationNeeded = false;
            this.durability = Durability.FULL;
            this.readOnly = false;
            this.compactOnLaunch = null;
            if (RealmConfiguration.DEFAULT_MODULE != null) {
                this.modules.add(RealmConfiguration.DEFAULT_MODULE);
            }
        }

        public Builder name(String str) {
            if (str == null || str.isEmpty()) {
                throw new IllegalArgumentException("A non-empty filename must be provided");
            }
            this.fileName = str;
            return this;
        }

        public Builder directory(File file) {
            if (file != null) {
                String str = ".";
                if (file.isFile()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("'dir' is a file, not a directory: ");
                    sb.append(file.getAbsolutePath());
                    sb.append(str);
                    throw new IllegalArgumentException(sb.toString());
                } else if (!file.exists() && !file.mkdirs()) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Could not create the specified directory: ");
                    sb2.append(file.getAbsolutePath());
                    sb2.append(str);
                    throw new IllegalArgumentException(sb2.toString());
                } else if (file.canWrite()) {
                    this.directory = file;
                    return this;
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Realm directory is not writable: ");
                    sb3.append(file.getAbsolutePath());
                    sb3.append(str);
                    throw new IllegalArgumentException(sb3.toString());
                }
            } else {
                throw new IllegalArgumentException("Non-null 'dir' required.");
            }
        }

        public Builder encryptionKey(byte[] bArr) {
            if (bArr == null) {
                throw new IllegalArgumentException("A non-null key must be provided");
            } else if (bArr.length == 64) {
                this.key = Arrays.copyOf(bArr, bArr.length);
                return this;
            } else {
                throw new IllegalArgumentException(String.format(Locale.US, "The provided key must be %s bytes. Yours was: %s", new Object[]{Integer.valueOf(64), Integer.valueOf(bArr.length)}));
            }
        }

        public Builder schemaVersion(long j) {
            if (j >= 0) {
                this.schemaVersion = j;
                return this;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Realm schema version numbers must be 0 (zero) or higher. Yours was: ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString());
        }

        public Builder migration(RealmMigration realmMigration) {
            if (realmMigration != null) {
                this.migration = realmMigration;
                return this;
            }
            throw new IllegalArgumentException("A non-null migration must be provided");
        }

        public Builder deleteRealmIfMigrationNeeded() {
            String str = this.assetFilePath;
            if (str == null || str.length() == 0) {
                this.deleteRealmIfMigrationNeeded = true;
                return this;
            }
            throw new IllegalStateException("Realm cannot clear its schema when previously configured to use an asset file by calling assetFile().");
        }

        public Builder inMemory() {
            if (Util.isEmptyString(this.assetFilePath)) {
                this.durability = Durability.MEM_ONLY;
                return this;
            }
            throw new RealmException("Realm can not use in-memory configuration if asset file is present.");
        }

        public Builder modules(Object obj, Object... objArr) {
            this.modules.clear();
            addModule(obj);
            if (objArr != null) {
                for (Object addModule : objArr) {
                    addModule(addModule);
                }
            }
            return this;
        }

        public final Builder addModule(Object obj) {
            if (obj != null) {
                checkModule(obj);
                this.modules.add(obj);
            }
            return this;
        }

        public Builder rxFactory(RxObservableFactory rxObservableFactory) {
            this.rxFactory = rxObservableFactory;
            return this;
        }

        public Builder initialData(Transaction transaction) {
            this.initialDataTransaction = transaction;
            return this;
        }

        public Builder assetFile(String str) {
            if (Util.isEmptyString(str)) {
                throw new IllegalArgumentException("A non-empty asset file path must be provided");
            } else if (this.durability == Durability.MEM_ONLY) {
                throw new RealmException("Realm can not use in-memory configuration if asset file is present.");
            } else if (!this.deleteRealmIfMigrationNeeded) {
                this.assetFilePath = str;
                return this;
            } else {
                throw new IllegalStateException("Realm cannot use an asset file when previously configured to clear its schema in migration by calling deleteRealmIfMigrationNeeded().");
            }
        }

        public Builder readOnly() {
            this.readOnly = true;
            return this;
        }

        public Builder compactOnLaunch() {
            return compactOnLaunch(new DefaultCompactOnLaunchCallback());
        }

        public Builder compactOnLaunch(CompactOnLaunchCallback compactOnLaunchCallback) {
            if (compactOnLaunchCallback != null) {
                this.compactOnLaunch = compactOnLaunchCallback;
                return this;
            }
            throw new IllegalArgumentException("A non-null compactOnLaunch must be provided");
        }

        /* access modifiers changed from: 0000 */
        public final Builder schema(Class<? extends RealmModel> cls, Class<? extends RealmModel>... clsArr) {
            if (cls != null) {
                this.modules.clear();
                this.modules.add(RealmConfiguration.DEFAULT_MODULE_MEDIATOR);
                this.debugSchema.add(cls);
                if (clsArr != null) {
                    Collections.addAll(this.debugSchema, clsArr);
                }
                return this;
            }
            throw new IllegalArgumentException("A non-null class must be provided");
        }

        public RealmConfiguration build() {
            if (this.readOnly) {
                if (this.initialDataTransaction != null) {
                    throw new IllegalStateException("This Realm is marked as read-only. Read-only Realms cannot use initialData(Realm.Transaction).");
                } else if (this.assetFilePath == null) {
                    throw new IllegalStateException("Only Realms provided using 'assetFile(path)' can be marked read-only. No such Realm was provided.");
                } else if (this.deleteRealmIfMigrationNeeded) {
                    throw new IllegalStateException("'deleteRealmIfMigrationNeeded()' and read-only Realms cannot be combined");
                } else if (this.compactOnLaunch != null) {
                    throw new IllegalStateException("'compactOnLaunch()' and read-only Realms cannot be combined");
                }
            }
            if (this.rxFactory == null && RealmConfiguration.isRxJavaAvailable()) {
                this.rxFactory = new RealmObservableFactory();
            }
            File file = this.directory;
            File file2 = file;
            String str = this.fileName;
            RealmConfiguration realmConfiguration = r2;
            RealmConfiguration realmConfiguration2 = new RealmConfiguration(file2, str, RealmConfiguration.getCanonicalPath(new File(file, str)), this.assetFilePath, this.key, this.schemaVersion, this.migration, this.deleteRealmIfMigrationNeeded, this.durability, RealmConfiguration.createSchemaMediator(this.modules, this.debugSchema), this.rxFactory, this.initialDataTransaction, this.readOnly, this.compactOnLaunch, false);
            return realmConfiguration;
        }

        private void checkModule(Object obj) {
            if (!obj.getClass().isAnnotationPresent(RealmModule.class)) {
                StringBuilder sb = new StringBuilder();
                sb.append(obj.getClass().getCanonicalName());
                sb.append(" is not a RealmModule. Add @RealmModule to the class definition.");
                throw new IllegalArgumentException(sb.toString());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isSyncConfiguration() {
        return false;
    }

    static {
        Object obj = DEFAULT_MODULE;
        if (obj != null) {
            RealmProxyMediator moduleMediator = getModuleMediator(obj.getClass().getCanonicalName());
            if (moduleMediator.transformerApplied()) {
                DEFAULT_MODULE_MEDIATOR = moduleMediator;
                return;
            }
            throw new ExceptionInInitializerError("RealmTransformer doesn't seem to be applied. Please update the project configuration to use the Realm Gradle plugin. See https://realm.io/news/android-installation-change/");
        }
        DEFAULT_MODULE_MEDIATOR = null;
    }

    protected RealmConfiguration(@Nullable File file, @Nullable String str, String str2, @Nullable String str3, @Nullable byte[] bArr, long j, @Nullable RealmMigration realmMigration, boolean z, Durability durability2, RealmProxyMediator realmProxyMediator, @Nullable RxObservableFactory rxObservableFactory2, @Nullable Transaction transaction, boolean z2, @Nullable CompactOnLaunchCallback compactOnLaunchCallback, boolean z3) {
        this.realmDirectory = file;
        this.realmFileName = str;
        this.canonicalPath = str2;
        this.assetFilePath = str3;
        this.key = bArr;
        this.schemaVersion = j;
        this.migration = realmMigration;
        this.deleteRealmIfMigrationNeeded = z;
        this.durability = durability2;
        this.schemaMediator = realmProxyMediator;
        this.rxObservableFactory = rxObservableFactory2;
        this.initialDataTransaction = transaction;
        this.readOnly = z2;
        this.compactOnLaunch = compactOnLaunchCallback;
        this.isRecoveryConfiguration = z3;
    }

    public File getRealmDirectory() {
        return this.realmDirectory;
    }

    public String getRealmFileName() {
        return this.realmFileName;
    }

    public byte[] getEncryptionKey() {
        byte[] bArr = this.key;
        if (bArr == null) {
            return null;
        }
        return Arrays.copyOf(bArr, bArr.length);
    }

    public long getSchemaVersion() {
        return this.schemaVersion;
    }

    public RealmMigration getMigration() {
        return this.migration;
    }

    public boolean shouldDeleteRealmIfMigrationNeeded() {
        return this.deleteRealmIfMigrationNeeded;
    }

    public Durability getDurability() {
        return this.durability;
    }

    /* access modifiers changed from: protected */
    public RealmProxyMediator getSchemaMediator() {
        return this.schemaMediator;
    }

    /* access modifiers changed from: 0000 */
    public Transaction getInitialDataTransaction() {
        return this.initialDataTransaction;
    }

    /* access modifiers changed from: 0000 */
    public boolean hasAssetFile() {
        return !Util.isEmptyString(this.assetFilePath);
    }

    /* access modifiers changed from: 0000 */
    public String getAssetFilePath() {
        return this.assetFilePath;
    }

    public CompactOnLaunchCallback getCompactOnLaunchCallback() {
        return this.compactOnLaunch;
    }

    public Set<Class<? extends RealmModel>> getRealmObjectClasses() {
        return this.schemaMediator.getModelClasses();
    }

    public String getPath() {
        return this.canonicalPath;
    }

    /* access modifiers changed from: 0000 */
    public boolean realmExists() {
        return new File(this.canonicalPath).exists();
    }

    public RxObservableFactory getRxFactory() {
        RxObservableFactory rxObservableFactory2 = this.rxObservableFactory;
        if (rxObservableFactory2 != null) {
            return rxObservableFactory2;
        }
        throw new UnsupportedOperationException("RxJava seems to be missing from the classpath. Remember to add it as a compile dependency. See https://realm.io/docs/java/latest/#rxjava for more details.");
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public boolean isRecoveryConfiguration() {
        return this.isRecoveryConfiguration;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RealmConfiguration realmConfiguration = (RealmConfiguration) obj;
        if (this.schemaVersion != realmConfiguration.schemaVersion || this.deleteRealmIfMigrationNeeded != realmConfiguration.deleteRealmIfMigrationNeeded || this.readOnly != realmConfiguration.readOnly || this.isRecoveryConfiguration != realmConfiguration.isRecoveryConfiguration) {
            return false;
        }
        File file = this.realmDirectory;
        if (file == null ? realmConfiguration.realmDirectory != null : !file.equals(realmConfiguration.realmDirectory)) {
            return false;
        }
        String str = this.realmFileName;
        if (str == null ? realmConfiguration.realmFileName != null : !str.equals(realmConfiguration.realmFileName)) {
            return false;
        }
        if (!this.canonicalPath.equals(realmConfiguration.canonicalPath)) {
            return false;
        }
        String str2 = this.assetFilePath;
        if (str2 == null ? realmConfiguration.assetFilePath != null : !str2.equals(realmConfiguration.assetFilePath)) {
            return false;
        }
        if (!Arrays.equals(this.key, realmConfiguration.key)) {
            return false;
        }
        RealmMigration realmMigration = this.migration;
        if (realmMigration == null ? realmConfiguration.migration != null : !realmMigration.equals(realmConfiguration.migration)) {
            return false;
        }
        if (this.durability != realmConfiguration.durability || !this.schemaMediator.equals(realmConfiguration.schemaMediator)) {
            return false;
        }
        RxObservableFactory rxObservableFactory2 = this.rxObservableFactory;
        if (rxObservableFactory2 == null ? realmConfiguration.rxObservableFactory != null : !rxObservableFactory2.equals(realmConfiguration.rxObservableFactory)) {
            return false;
        }
        Transaction transaction = this.initialDataTransaction;
        if (transaction == null ? realmConfiguration.initialDataTransaction != null : !transaction.equals(realmConfiguration.initialDataTransaction)) {
            return false;
        }
        CompactOnLaunchCallback compactOnLaunchCallback = this.compactOnLaunch;
        CompactOnLaunchCallback compactOnLaunchCallback2 = realmConfiguration.compactOnLaunch;
        if (compactOnLaunchCallback != null) {
            z = compactOnLaunchCallback.equals(compactOnLaunchCallback2);
        } else if (compactOnLaunchCallback2 != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        File file = this.realmDirectory;
        int i = 0;
        int hashCode = (file != null ? file.hashCode() : 0) * 31;
        String str = this.realmFileName;
        int hashCode2 = (((hashCode + (str != null ? str.hashCode() : 0)) * 31) + this.canonicalPath.hashCode()) * 31;
        String str2 = this.assetFilePath;
        int hashCode3 = (((hashCode2 + (str2 != null ? str2.hashCode() : 0)) * 31) + Arrays.hashCode(this.key)) * 31;
        long j = this.schemaVersion;
        int i2 = (hashCode3 + ((int) (j ^ (j >>> 32)))) * 31;
        RealmMigration realmMigration = this.migration;
        int hashCode4 = (((((((i2 + (realmMigration != null ? realmMigration.hashCode() : 0)) * 31) + (this.deleteRealmIfMigrationNeeded ? 1 : 0)) * 31) + this.durability.hashCode()) * 31) + this.schemaMediator.hashCode()) * 31;
        RxObservableFactory rxObservableFactory2 = this.rxObservableFactory;
        int hashCode5 = (hashCode4 + (rxObservableFactory2 != null ? rxObservableFactory2.hashCode() : 0)) * 31;
        Transaction transaction = this.initialDataTransaction;
        int hashCode6 = (((hashCode5 + (transaction != null ? transaction.hashCode() : 0)) * 31) + (this.readOnly ? 1 : 0)) * 31;
        CompactOnLaunchCallback compactOnLaunchCallback = this.compactOnLaunch;
        if (compactOnLaunchCallback != null) {
            i = compactOnLaunchCallback.hashCode();
        }
        return ((hashCode6 + i) * 31) + (this.isRecoveryConfiguration ? 1 : 0);
    }

    protected static RealmProxyMediator createSchemaMediator(Set<Object> set, Set<Class<? extends RealmModel>> set2) {
        if (set2.size() > 0) {
            return new FilterableMediator(DEFAULT_MODULE_MEDIATOR, set2);
        }
        if (set.size() == 1) {
            return getModuleMediator(set.iterator().next().getClass().getCanonicalName());
        }
        RealmProxyMediator[] realmProxyMediatorArr = new RealmProxyMediator[set.size()];
        int i = 0;
        for (Object obj : set) {
            realmProxyMediatorArr[i] = getModuleMediator(obj.getClass().getCanonicalName());
            i++;
        }
        return new CompositeMediator(realmProxyMediatorArr);
    }

    private static RealmProxyMediator getModuleMediator(String str) {
        String str2 = "Could not create an instance of ";
        String[] split = str.split("\\.");
        String str3 = split[split.length - 1];
        String format = String.format(Locale.US, "io.realm.%s%s", new Object[]{str3, "Mediator"});
        try {
            Constructor constructor = Class.forName(format).getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            return (RealmProxyMediator) constructor.newInstance(new Object[0]);
        } catch (ClassNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Could not find ");
            sb.append(format);
            throw new RealmException(sb.toString(), e);
        } catch (InvocationTargetException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(format);
            throw new RealmException(sb2.toString(), e2);
        } catch (InstantiationException e3) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str2);
            sb3.append(format);
            throw new RealmException(sb3.toString(), e3);
        } catch (IllegalAccessException e4) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str2);
            sb4.append(format);
            throw new RealmException(sb4.toString(), e4);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("realmDirectory: ");
        File file = this.realmDirectory;
        sb.append(file != null ? file.toString() : "");
        String str = "\n";
        sb.append(str);
        sb.append("realmFileName : ");
        sb.append(this.realmFileName);
        sb.append(str);
        sb.append("canonicalPath: ");
        sb.append(this.canonicalPath);
        sb.append(str);
        sb.append("key: ");
        sb.append("[length: ");
        sb.append(this.key == null ? 0 : 64);
        sb.append("]");
        sb.append(str);
        sb.append("schemaVersion: ");
        sb.append(Long.toString(this.schemaVersion));
        sb.append(str);
        sb.append("migration: ");
        sb.append(this.migration);
        sb.append(str);
        sb.append("deleteRealmIfMigrationNeeded: ");
        sb.append(this.deleteRealmIfMigrationNeeded);
        sb.append(str);
        sb.append("durability: ");
        sb.append(this.durability);
        sb.append(str);
        sb.append("schemaMediator: ");
        sb.append(this.schemaMediator);
        sb.append(str);
        sb.append("readOnly: ");
        sb.append(this.readOnly);
        sb.append(str);
        sb.append("compactOnLaunch: ");
        sb.append(this.compactOnLaunch);
        return sb.toString();
    }

    static synchronized boolean isRxJavaAvailable() {
        boolean booleanValue;
        synchronized (RealmConfiguration.class) {
            if (rxJavaAvailable == null) {
                try {
                    Class.forName("io.reactivex.Flowable");
                    rxJavaAvailable = Boolean.valueOf(true);
                } catch (ClassNotFoundException unused) {
                    rxJavaAvailable = Boolean.valueOf(false);
                }
            }
            booleanValue = rxJavaAvailable.booleanValue();
        }
        return booleanValue;
    }

    protected static String getCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            Kind kind = Kind.ACCESS_ERROR;
            StringBuilder sb = new StringBuilder();
            sb.append("Could not resolve the canonical path to the Realm file: ");
            sb.append(file.getAbsolutePath());
            throw new RealmFileException(kind, sb.toString(), e);
        }
    }
}
