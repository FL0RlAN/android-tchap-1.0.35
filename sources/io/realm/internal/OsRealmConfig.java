package io.realm.internal;

import io.realm.CompactOnLaunchCallback;
import io.realm.RealmConfiguration;
import io.realm.internal.OsSharedRealm.InitializationCallback;
import io.realm.internal.OsSharedRealm.MigrationCallback;
import io.realm.log.RealmLog;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

public class OsRealmConfig implements NativeObject {
    private static final byte SCHEMA_MODE_VALUE_ADDITIVE = 4;
    private static final byte SCHEMA_MODE_VALUE_AUTOMATIC = 0;
    private static final byte SCHEMA_MODE_VALUE_IMMUTABLE = 1;
    private static final byte SCHEMA_MODE_VALUE_MANUAL = 5;
    private static final byte SCHEMA_MODE_VALUE_READONLY = 2;
    private static final byte SCHEMA_MODE_VALUE_RESET_FILE = 3;
    private static final byte SYNCSESSION_STOP_POLICY_VALUE_AFTER_CHANGES_UPLOADED = 2;
    private static final byte SYNCSESSION_STOP_POLICY_VALUE_IMMEDIATELY = 0;
    private static final byte SYNCSESSION_STOP_POLICY_VALUE_LIVE_INDEFINETELY = 1;
    private static final long nativeFinalizerPtr = nativeGetFinalizerPtr();
    private final CompactOnLaunchCallback compactOnLaunchCallback;
    private final NativeContext context;
    private final InitializationCallback initializationCallback;
    private final MigrationCallback migrationCallback;
    private final long nativePtr;
    private final RealmConfiguration realmConfiguration;
    private final URI resolvedRealmURI;

    public static class Builder {
        private boolean autoUpdateNotification = false;
        private RealmConfiguration configuration;
        private String fifoFallbackDir = "";
        private InitializationCallback initializationCallback = null;
        private MigrationCallback migrationCallback = null;
        private OsSchemaInfo schemaInfo = null;

        public Builder(RealmConfiguration realmConfiguration) {
            this.configuration = realmConfiguration;
        }

        public Builder schemaInfo(@Nullable OsSchemaInfo osSchemaInfo) {
            this.schemaInfo = osSchemaInfo;
            return this;
        }

        public Builder migrationCallback(@Nullable MigrationCallback migrationCallback2) {
            this.migrationCallback = migrationCallback2;
            return this;
        }

        public Builder initializationCallback(@Nullable InitializationCallback initializationCallback2) {
            this.initializationCallback = initializationCallback2;
            return this;
        }

        public Builder autoUpdateNotification(boolean z) {
            this.autoUpdateNotification = z;
            return this;
        }

        /* access modifiers changed from: 0000 */
        public OsRealmConfig build() {
            OsRealmConfig osRealmConfig = new OsRealmConfig(this.configuration, this.fifoFallbackDir, this.autoUpdateNotification, this.schemaInfo, this.migrationCallback, this.initializationCallback);
            return osRealmConfig;
        }

        public Builder fifoFallbackDir(File file) {
            this.fifoFallbackDir = file.getAbsolutePath();
            return this;
        }
    }

    public enum Durability {
        FULL(0),
        MEM_ONLY(1);
        
        final int value;

        private Durability(int i) {
            this.value = i;
        }
    }

    public enum SchemaMode {
        SCHEMA_MODE_AUTOMATIC(0),
        SCHEMA_MODE_IMMUTABLE(1),
        SCHEMA_MODE_READONLY(2),
        SCHEMA_MODE_RESET_FILE(3),
        SCHEMA_MODE_ADDITIVE(4),
        SCHEMA_MODE_MANUAL(5);
        
        final byte value;

        private SchemaMode(byte b) {
            this.value = b;
        }

        public byte getNativeValue() {
            return this.value;
        }
    }

    public enum SyncSessionStopPolicy {
        IMMEDIATELY(0),
        LIVE_INDEFINITELY(1),
        AFTER_CHANGES_UPLOADED(2);
        
        final byte value;

        private SyncSessionStopPolicy(byte b) {
            this.value = b;
        }

        public byte getNativeValue() {
            return this.value;
        }
    }

    private static native long nativeCreate(String str, String str2, boolean z, boolean z2);

    private static native String nativeCreateAndSetSyncConfig(long j, String str, String str2, String str3, String str4, boolean z, byte b, String str5, String str6, String[] strArr);

    private static native void nativeEnableChangeNotification(long j, boolean z);

    private static native long nativeGetFinalizerPtr();

    private static native void nativeSetCompactOnLaunchCallback(long j, CompactOnLaunchCallback compactOnLaunchCallback2);

    private static native void nativeSetEncryptionKey(long j, byte[] bArr);

    private static native void nativeSetInMemory(long j, boolean z);

    private native void nativeSetInitializationCallback(long j, InitializationCallback initializationCallback2);

    private native void nativeSetSchemaConfig(long j, byte b, long j2, long j3, @Nullable MigrationCallback migrationCallback2);

    private static native void nativeSetSyncConfigSslSettings(long j, boolean z, String str);

    private OsRealmConfig(RealmConfiguration realmConfiguration2, String str, boolean z, @Nullable OsSchemaInfo osSchemaInfo, @Nullable MigrationCallback migrationCallback2, @Nullable InitializationCallback initializationCallback2) {
        long j;
        InitializationCallback initializationCallback3 = initializationCallback2;
        this.context = new NativeContext();
        this.realmConfiguration = realmConfiguration2;
        boolean z2 = true;
        this.nativePtr = nativeCreate(realmConfiguration2.getPath(), str, false, true);
        NativeContext.dummyContext.addReference(this);
        Object[] syncConfigurationOptions = ObjectServerFacade.getSyncFacadeIfPossible().getSyncConfigurationOptions(this.realmConfiguration);
        String str2 = (String) syncConfigurationOptions[0];
        String str3 = (String) syncConfigurationOptions[1];
        String str4 = (String) syncConfigurationOptions[2];
        String str5 = (String) syncConfigurationOptions[3];
        boolean equals = Boolean.TRUE.equals(syncConfigurationOptions[4]);
        String str6 = (String) syncConfigurationOptions[5];
        Byte b = (Byte) syncConfigurationOptions[6];
        boolean equals2 = Boolean.TRUE.equals(syncConfigurationOptions[7]);
        String str7 = (String) syncConfigurationOptions[8];
        String str8 = (String) syncConfigurationOptions[9];
        Map map = (Map) syncConfigurationOptions[10];
        String[] strArr = new String[(map != null ? map.size() * 2 : 0)];
        if (map != null) {
            int i = 0;
            for (Entry entry : map.entrySet()) {
                strArr[i] = (String) entry.getKey();
                strArr[i + 1] = (String) entry.getValue();
                i += 2;
            }
        }
        byte[] encryptionKey = realmConfiguration2.getEncryptionKey();
        if (encryptionKey != null) {
            nativeSetEncryptionKey(this.nativePtr, encryptionKey);
        }
        long j2 = this.nativePtr;
        if (realmConfiguration2.getDurability() != Durability.MEM_ONLY) {
            z2 = false;
        }
        nativeSetInMemory(j2, z2);
        nativeEnableChangeNotification(this.nativePtr, z);
        SchemaMode schemaMode = SchemaMode.SCHEMA_MODE_MANUAL;
        if (realmConfiguration2.isRecoveryConfiguration()) {
            schemaMode = SchemaMode.SCHEMA_MODE_IMMUTABLE;
        } else if (realmConfiguration2.isReadOnly()) {
            schemaMode = SchemaMode.SCHEMA_MODE_READONLY;
        } else if (str3 != null) {
            schemaMode = SchemaMode.SCHEMA_MODE_ADDITIVE;
        } else if (realmConfiguration2.shouldDeleteRealmIfMigrationNeeded()) {
            schemaMode = SchemaMode.SCHEMA_MODE_RESET_FILE;
        }
        long schemaVersion = realmConfiguration2.getSchemaVersion();
        if (osSchemaInfo == null) {
            j = 0;
        } else {
            j = osSchemaInfo.getNativePtr();
        }
        long j3 = j;
        this.migrationCallback = migrationCallback2;
        long j4 = j3;
        String[] strArr2 = strArr;
        nativeSetSchemaConfig(this.nativePtr, schemaMode.getNativeValue(), schemaVersion, j4, migrationCallback2);
        this.compactOnLaunchCallback = realmConfiguration2.getCompactOnLaunchCallback();
        CompactOnLaunchCallback compactOnLaunchCallback2 = this.compactOnLaunchCallback;
        if (compactOnLaunchCallback2 != null) {
            nativeSetCompactOnLaunchCallback(this.nativePtr, compactOnLaunchCallback2);
        }
        this.initializationCallback = initializationCallback3;
        if (initializationCallback3 != null) {
            nativeSetInitializationCallback(this.nativePtr, initializationCallback3);
        }
        URI uri = null;
        if (str3 != null) {
            boolean z3 = equals;
            String str9 = str6;
            try {
                uri = new URI(nativeCreateAndSetSyncConfig(this.nativePtr, str3, str4, str2, str5, equals2, b.byteValue(), str7, str8, strArr2));
            } catch (URISyntaxException e) {
                RealmLog.error(e, "Cannot create a URI from the Realm URL address", new Object[0]);
            }
            nativeSetSyncConfigSslSettings(this.nativePtr, z3, str9);
        }
        this.resolvedRealmURI = uri;
    }

    public long getNativePtr() {
        return this.nativePtr;
    }

    public long getNativeFinalizerPtr() {
        return nativeFinalizerPtr;
    }

    public RealmConfiguration getRealmConfiguration() {
        return this.realmConfiguration;
    }

    public URI getResolvedRealmURI() {
        return this.resolvedRealmURI;
    }

    /* access modifiers changed from: 0000 */
    public NativeContext getContext() {
        return this.context;
    }
}
