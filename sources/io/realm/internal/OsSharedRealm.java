package io.realm.internal;

import io.realm.RealmConfiguration;
import io.realm.internal.OsRealmConfig.Builder;
import io.realm.internal.OsResults.Iterator;
import io.realm.internal.android.AndroidCapabilities;
import io.realm.internal.android.AndroidRealmNotifier;
import java.io.Closeable;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.Nullable;

public final class OsSharedRealm implements Closeable, NativeObject {
    public static final byte FILE_EXCEPTION_INCOMPATIBLE_SYNC_FILE = 7;
    public static final byte FILE_EXCEPTION_KIND_ACCESS_ERROR = 0;
    public static final byte FILE_EXCEPTION_KIND_BAD_HISTORY = 1;
    public static final byte FILE_EXCEPTION_KIND_EXISTS = 3;
    public static final byte FILE_EXCEPTION_KIND_FORMAT_UPGRADE_REQUIRED = 6;
    public static final byte FILE_EXCEPTION_KIND_INCOMPATIBLE_LOCK_FILE = 5;
    public static final byte FILE_EXCEPTION_KIND_NOT_FOUND = 4;
    public static final byte FILE_EXCEPTION_KIND_PERMISSION_DENIED = 2;
    private static final long nativeFinalizerPtr = nativeGetFinalizerPtr();
    private static final List<OsSharedRealm> sharedRealmsUnderConstruction = new CopyOnWriteArrayList();
    private static volatile File temporaryDirectory;
    public final Capabilities capabilities;
    public final NativeContext context;
    final List<WeakReference<Iterator>> iterators = new ArrayList();
    private final long nativePtr;
    private final OsRealmConfig osRealmConfig;
    private final List<WeakReference<PendingRow>> pendingRows = new CopyOnWriteArrayList();
    public final RealmNotifier realmNotifier;
    private final OsSchemaInfo schemaInfo;
    private final List<OsSharedRealm> tempSharedRealmsForCallback = new ArrayList();

    public interface InitializationCallback {
        void onInit(OsSharedRealm osSharedRealm);
    }

    public interface MigrationCallback {
        void onMigrationNeeded(OsSharedRealm osSharedRealm, long j, long j2);
    }

    public interface SchemaChangedCallback {
        void onSchemaChanged();
    }

    public static class VersionID implements Comparable<VersionID> {
        public final long index;
        public final long version;

        VersionID(long j, long j2) {
            this.version = j;
            this.index = j2;
        }

        public int compareTo(VersionID versionID) {
            if (versionID != null) {
                long j = this.version;
                long j2 = versionID.version;
                if (j > j2) {
                    return 1;
                }
                return j < j2 ? -1 : 0;
            }
            throw new IllegalArgumentException("Version cannot be compared to a null value.");
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("VersionID{version=");
            sb.append(this.version);
            sb.append(", index=");
            sb.append(this.index);
            sb.append('}');
            return sb.toString();
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            VersionID versionID = (VersionID) obj;
            if (!(this.version == versionID.version && this.index == versionID.index)) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            int hashCode = super.hashCode() * 31;
            long j = this.version;
            int i = (hashCode + ((int) (j ^ (j >>> 32)))) * 31;
            long j2 = this.index;
            return i + ((int) (j2 ^ (j2 >>> 32)));
        }
    }

    private static native void nativeBeginTransaction(long j);

    private static native void nativeCancelTransaction(long j);

    private static native void nativeCloseSharedRealm(long j);

    private static native void nativeCommitTransaction(long j);

    private static native boolean nativeCompact(long j);

    private static native long nativeCreateTable(long j, String str);

    private static native long nativeCreateTableWithPrimaryKeyField(long j, String str, String str2, boolean z, boolean z2);

    private static native int nativeGetClassPrivileges(long j, String str);

    private static native long nativeGetFinalizerPtr();

    private static native int nativeGetObjectPrivileges(long j, long j2);

    private static native int nativeGetRealmPrivileges(long j);

    private static native long nativeGetSchemaInfo(long j);

    private static native long nativeGetSharedRealm(long j, RealmNotifier realmNotifier2);

    private static native long nativeGetTable(long j, String str);

    private static native String nativeGetTableName(long j, int i);

    private static native long[] nativeGetVersionID(long j);

    private static native boolean nativeHasTable(long j, String str);

    private static native void nativeInit(String str);

    private static native boolean nativeIsAutoRefresh(long j);

    private static native boolean nativeIsClosed(long j);

    private static native boolean nativeIsEmpty(long j);

    private static native boolean nativeIsInTransaction(long j);

    private static native boolean nativeIsPartial(long j);

    private static native void nativeRefresh(long j);

    private static native void nativeRegisterSchemaChangedCallback(long j, SchemaChangedCallback schemaChangedCallback);

    private static native void nativeRenameTable(long j, String str, String str2);

    private static native void nativeSetAutoRefresh(long j, boolean z);

    private static native long nativeSize(long j);

    private static native void nativeStopWaitForChange(long j);

    private static native boolean nativeWaitForChange(long j);

    private static native void nativeWriteCopy(long j, String str, @Nullable byte[] bArr);

    private OsSharedRealm(OsRealmConfig osRealmConfig2) {
        AndroidCapabilities androidCapabilities = new AndroidCapabilities();
        AndroidRealmNotifier androidRealmNotifier = new AndroidRealmNotifier(this, androidCapabilities);
        this.context = osRealmConfig2.getContext();
        sharedRealmsUnderConstruction.add(this);
        try {
            this.nativePtr = nativeGetSharedRealm(osRealmConfig2.getNativePtr(), androidRealmNotifier);
            this.tempSharedRealmsForCallback.clear();
            sharedRealmsUnderConstruction.remove(this);
            this.osRealmConfig = osRealmConfig2;
            this.schemaInfo = new OsSchemaInfo(nativeGetSchemaInfo(this.nativePtr), this);
            this.context.addReference(this);
            this.capabilities = androidCapabilities;
            this.realmNotifier = androidRealmNotifier;
            nativeSetAutoRefresh(this.nativePtr, androidCapabilities.canDeliverNotification());
        } catch (Throwable th) {
            this.tempSharedRealmsForCallback.clear();
            sharedRealmsUnderConstruction.remove(this);
            throw th;
        }
    }

    private OsSharedRealm(long j, OsRealmConfig osRealmConfig2) {
        this.nativePtr = j;
        this.osRealmConfig = osRealmConfig2;
        this.schemaInfo = new OsSchemaInfo(nativeGetSchemaInfo(this.nativePtr), this);
        this.context = osRealmConfig2.getContext();
        this.context.addReference(this);
        this.capabilities = new AndroidCapabilities();
        this.realmNotifier = null;
        boolean z = false;
        nativeSetAutoRefresh(this.nativePtr, false);
        java.util.Iterator it = sharedRealmsUnderConstruction.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            OsSharedRealm osSharedRealm = (OsSharedRealm) it.next();
            if (osSharedRealm.context == osRealmConfig2.getContext()) {
                z = true;
                osSharedRealm.tempSharedRealmsForCallback.add(this);
                break;
            }
        }
        if (!z) {
            throw new IllegalStateException("Cannot find the parent 'OsSharedRealm' which is under construction.");
        }
    }

    public static OsSharedRealm getInstance(RealmConfiguration realmConfiguration) {
        return getInstance(new Builder(realmConfiguration));
    }

    public static OsSharedRealm getInstance(Builder builder) {
        OsRealmConfig build = builder.build();
        ObjectServerFacade.getSyncFacadeIfPossible().wrapObjectStoreSessionIfRequired(build);
        return new OsSharedRealm(build);
    }

    public static void initialize(File file) {
        if (temporaryDirectory == null) {
            String absolutePath = file.getAbsolutePath();
            if (file.isDirectory() || file.mkdirs() || file.isDirectory()) {
                String str = "/";
                if (!absolutePath.endsWith(str)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(absolutePath);
                    sb.append(str);
                    absolutePath = sb.toString();
                }
                nativeInit(absolutePath);
                temporaryDirectory = file;
                return;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("failed to create temporary directory: ");
            sb2.append(absolutePath);
            throw new IOException(sb2.toString());
        }
    }

    public static File getTemporaryDirectory() {
        return temporaryDirectory;
    }

    public void beginTransaction() {
        detachIterators();
        executePendingRowQueries();
        nativeBeginTransaction(this.nativePtr);
    }

    public void commitTransaction() {
        nativeCommitTransaction(this.nativePtr);
    }

    public void cancelTransaction() {
        nativeCancelTransaction(this.nativePtr);
    }

    public boolean isInTransaction() {
        return nativeIsInTransaction(this.nativePtr);
    }

    public boolean hasTable(String str) {
        return nativeHasTable(this.nativePtr, str);
    }

    public Table getTable(String str) {
        return new Table(this, nativeGetTable(this.nativePtr, str));
    }

    public Table createTable(String str) {
        return new Table(this, nativeCreateTable(this.nativePtr, str));
    }

    public Table createTableWithPrimaryKey(String str, String str2, boolean z, boolean z2) {
        return new Table(this, nativeCreateTableWithPrimaryKeyField(this.nativePtr, str, str2, z, z2));
    }

    public void renameTable(String str, String str2) {
        nativeRenameTable(this.nativePtr, str, str2);
    }

    public String getTableName(int i) {
        return nativeGetTableName(this.nativePtr, i);
    }

    public long size() {
        return nativeSize(this.nativePtr);
    }

    public String getPath() {
        return this.osRealmConfig.getRealmConfiguration().getPath();
    }

    public boolean isEmpty() {
        return nativeIsEmpty(this.nativePtr);
    }

    public void refresh() {
        nativeRefresh(this.nativePtr);
    }

    public VersionID getVersionID() {
        long[] nativeGetVersionID = nativeGetVersionID(this.nativePtr);
        return new VersionID(nativeGetVersionID[0], nativeGetVersionID[1]);
    }

    public boolean isClosed() {
        return nativeIsClosed(this.nativePtr);
    }

    public void writeCopy(File file, @Nullable byte[] bArr) {
        if (!file.isFile() || !file.exists()) {
            nativeWriteCopy(this.nativePtr, file.getAbsolutePath(), bArr);
            return;
        }
        throw new IllegalArgumentException("The destination file must not exist");
    }

    public boolean waitForChange() {
        return nativeWaitForChange(this.nativePtr);
    }

    public void stopWaitForChange() {
        nativeStopWaitForChange(this.nativePtr);
    }

    public boolean compact() {
        return nativeCompact(this.nativePtr);
    }

    public void setAutoRefresh(boolean z) {
        this.capabilities.checkCanDeliverNotification(null);
        nativeSetAutoRefresh(this.nativePtr, z);
    }

    public boolean isAutoRefresh() {
        return nativeIsAutoRefresh(this.nativePtr);
    }

    public RealmConfiguration getConfiguration() {
        return this.osRealmConfig.getRealmConfiguration();
    }

    public void close() {
        RealmNotifier realmNotifier2 = this.realmNotifier;
        if (realmNotifier2 != null) {
            realmNotifier2.close();
        }
        synchronized (this.context) {
            nativeCloseSharedRealm(this.nativePtr);
        }
    }

    public long getNativePtr() {
        return this.nativePtr;
    }

    public long getNativeFinalizerPtr() {
        return nativeFinalizerPtr;
    }

    public OsSchemaInfo getSchemaInfo() {
        return this.schemaInfo;
    }

    public void registerSchemaChangedCallback(SchemaChangedCallback schemaChangedCallback) {
        nativeRegisterSchemaChangedCallback(this.nativePtr, schemaChangedCallback);
    }

    public boolean isPartial() {
        return nativeIsPartial(this.nativePtr);
    }

    public boolean isSyncRealm() {
        return this.osRealmConfig.getResolvedRealmURI() != null;
    }

    /* access modifiers changed from: 0000 */
    public void addIterator(Iterator iterator) {
        this.iterators.add(new WeakReference(iterator));
    }

    private void detachIterators() {
        for (WeakReference weakReference : this.iterators) {
            Iterator iterator = (Iterator) weakReference.get();
            if (iterator != null) {
                iterator.detach();
            }
        }
        this.iterators.clear();
    }

    /* access modifiers changed from: 0000 */
    public void invalidateIterators() {
        for (WeakReference weakReference : this.iterators) {
            Iterator iterator = (Iterator) weakReference.get();
            if (iterator != null) {
                iterator.invalidate();
            }
        }
        this.iterators.clear();
    }

    /* access modifiers changed from: 0000 */
    public void addPendingRow(PendingRow pendingRow) {
        this.pendingRows.add(new WeakReference(pendingRow));
    }

    /* access modifiers changed from: 0000 */
    public void removePendingRow(PendingRow pendingRow) {
        for (WeakReference weakReference : this.pendingRows) {
            PendingRow pendingRow2 = (PendingRow) weakReference.get();
            if (pendingRow2 == null || pendingRow2 == pendingRow) {
                this.pendingRows.remove(weakReference);
            }
        }
    }

    private void executePendingRowQueries() {
        for (WeakReference weakReference : this.pendingRows) {
            PendingRow pendingRow = (PendingRow) weakReference.get();
            if (pendingRow != null) {
                pendingRow.executeQuery();
            }
        }
        this.pendingRows.clear();
    }

    private static void runMigrationCallback(long j, OsRealmConfig osRealmConfig2, MigrationCallback migrationCallback, long j2) {
        migrationCallback.onMigrationNeeded(new OsSharedRealm(j, osRealmConfig2), j2, osRealmConfig2.getRealmConfiguration().getSchemaVersion());
    }

    private static void runInitializationCallback(long j, OsRealmConfig osRealmConfig2, InitializationCallback initializationCallback) {
        initializationCallback.onInit(new OsSharedRealm(j, osRealmConfig2));
    }
}
