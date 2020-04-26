package io.realm;

import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.BaseRealm.RealmObjectContext;
import io.realm.exceptions.RealmException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsObject;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsObjectSchemaInfo.Builder;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmObjectProxy.CacheData;
import io.realm.internal.Row;
import io.realm.internal.Table;
import io.realm.internal.objectstore.OsObjectBuilder;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import org.matrix.androidsdk.crypto.cryptostore.db.model.KeysBackupDataEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.KeysBackupDataEntityFields;

public class org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy extends KeysBackupDataEntity implements RealmObjectProxy, org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface {
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();
    private KeysBackupDataEntityColumnInfo columnInfo;
    private ProxyState<KeysBackupDataEntity> proxyState;

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "KeysBackupDataEntity";
    }

    static final class KeysBackupDataEntityColumnInfo extends ColumnInfo {
        long backupLastServerHashIndex;
        long backupLastServerNumberOfKeysIndex;
        long maxColumnIndexValue;
        long primaryKeyIndex;

        KeysBackupDataEntityColumnInfo(OsSchemaInfo osSchemaInfo) {
            super(3);
            OsObjectSchemaInfo objectSchemaInfo = osSchemaInfo.getObjectSchemaInfo(ClassNameHelper.INTERNAL_CLASS_NAME);
            String str = "primaryKey";
            this.primaryKeyIndex = addColumnDetails(str, str, objectSchemaInfo);
            String str2 = KeysBackupDataEntityFields.BACKUP_LAST_SERVER_HASH;
            this.backupLastServerHashIndex = addColumnDetails(str2, str2, objectSchemaInfo);
            String str3 = KeysBackupDataEntityFields.BACKUP_LAST_SERVER_NUMBER_OF_KEYS;
            this.backupLastServerNumberOfKeysIndex = addColumnDetails(str3, str3, objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        KeysBackupDataEntityColumnInfo(ColumnInfo columnInfo, boolean z) {
            super(columnInfo, z);
            copy(columnInfo, this);
        }

        /* access modifiers changed from: protected */
        public final ColumnInfo copy(boolean z) {
            return new KeysBackupDataEntityColumnInfo(this, z);
        }

        /* access modifiers changed from: protected */
        public final void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2) {
            KeysBackupDataEntityColumnInfo keysBackupDataEntityColumnInfo = (KeysBackupDataEntityColumnInfo) columnInfo;
            KeysBackupDataEntityColumnInfo keysBackupDataEntityColumnInfo2 = (KeysBackupDataEntityColumnInfo) columnInfo2;
            keysBackupDataEntityColumnInfo2.primaryKeyIndex = keysBackupDataEntityColumnInfo.primaryKeyIndex;
            keysBackupDataEntityColumnInfo2.backupLastServerHashIndex = keysBackupDataEntityColumnInfo.backupLastServerHashIndex;
            keysBackupDataEntityColumnInfo2.backupLastServerNumberOfKeysIndex = keysBackupDataEntityColumnInfo.backupLastServerNumberOfKeysIndex;
            keysBackupDataEntityColumnInfo2.maxColumnIndexValue = keysBackupDataEntityColumnInfo.maxColumnIndexValue;
        }
    }

    public static String getSimpleClassName() {
        return ClassNameHelper.INTERNAL_CLASS_NAME;
    }

    org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy() {
        this.proxyState.setConstructionFinished();
    }

    public void realm$injectObjectContext() {
        if (this.proxyState == null) {
            RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
            this.columnInfo = (KeysBackupDataEntityColumnInfo) realmObjectContext.getColumnInfo();
            this.proxyState = new ProxyState<>(this);
            this.proxyState.setRealm$realm(realmObjectContext.getRealm());
            this.proxyState.setRow$realm(realmObjectContext.getRow());
            this.proxyState.setAcceptDefaultValue$realm(realmObjectContext.getAcceptDefaultValue());
            this.proxyState.setExcludeFields$realm(realmObjectContext.getExcludeFields());
        }
    }

    public int realmGet$primaryKey() {
        this.proxyState.getRealm$realm().checkIfValid();
        return (int) this.proxyState.getRow$realm().getLong(this.columnInfo.primaryKeyIndex);
    }

    public void realmSet$primaryKey(int i) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            throw new RealmException("Primary key field 'primaryKey' cannot be changed after object was created.");
        }
    }

    public String realmGet$backupLastServerHash() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.backupLastServerHashIndex);
    }

    public void realmSet$backupLastServerHash(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.backupLastServerHashIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.backupLastServerHashIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.backupLastServerHashIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.backupLastServerHashIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public Integer realmGet$backupLastServerNumberOfKeys() {
        this.proxyState.getRealm$realm().checkIfValid();
        if (this.proxyState.getRow$realm().isNull(this.columnInfo.backupLastServerNumberOfKeysIndex)) {
            return null;
        }
        return Integer.valueOf((int) this.proxyState.getRow$realm().getLong(this.columnInfo.backupLastServerNumberOfKeysIndex));
    }

    public void realmSet$backupLastServerNumberOfKeys(Integer num) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (num == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.backupLastServerNumberOfKeysIndex);
            } else {
                this.proxyState.getRow$realm().setLong(this.columnInfo.backupLastServerNumberOfKeysIndex, (long) num.intValue());
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (num == null) {
                row$realm.getTable().setNull(this.columnInfo.backupLastServerNumberOfKeysIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setLong(this.columnInfo.backupLastServerNumberOfKeysIndex, row$realm.getIndex(), (long) num.intValue(), true);
            }
        }
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        Builder builder = new Builder(ClassNameHelper.INTERNAL_CLASS_NAME, 3, 0);
        Builder builder2 = builder;
        builder2.addPersistedProperty("primaryKey", RealmFieldType.INTEGER, true, true, true);
        builder2.addPersistedProperty(KeysBackupDataEntityFields.BACKUP_LAST_SERVER_HASH, RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty(KeysBackupDataEntityFields.BACKUP_LAST_SERVER_NUMBER_OF_KEYS, RealmFieldType.INTEGER, false, false, false);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static KeysBackupDataEntityColumnInfo createColumnInfo(OsSchemaInfo osSchemaInfo) {
        return new KeysBackupDataEntityColumnInfo(osSchemaInfo);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0064  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00a0  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00b9  */
    public static KeysBackupDataEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject jSONObject, boolean z) throws JSONException {
        org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy;
        String str;
        String str2;
        List emptyList = Collections.emptyList();
        String str3 = "primaryKey";
        if (z) {
            Table table = realm.getTable(KeysBackupDataEntity.class);
            long findFirstLong = !jSONObject.isNull(str3) ? table.findFirstLong(((KeysBackupDataEntityColumnInfo) realm.getSchema().getColumnInfo(KeysBackupDataEntity.class)).primaryKeyIndex, jSONObject.getLong(str3)) : -1;
            if (findFirstLong != -1) {
                RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
                try {
                    realmObjectContext.set(realm, table.getUncheckedRow(findFirstLong), realm.getSchema().getColumnInfo(KeysBackupDataEntity.class), false, Collections.emptyList());
                    org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy();
                    if (org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy == null) {
                        if (!jSONObject.has(str3)) {
                            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'primaryKey'.");
                        } else if (jSONObject.isNull(str3)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy) realm.createObjectInternal(KeysBackupDataEntity.class, null, true, emptyList);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy) realm.createObjectInternal(KeysBackupDataEntity.class, Integer.valueOf(jSONObject.getInt(str3)), true, emptyList);
                        }
                    }
                    org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface = org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy;
                    str = KeysBackupDataEntityFields.BACKUP_LAST_SERVER_HASH;
                    if (jSONObject.has(str)) {
                        if (jSONObject.isNull(str)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmSet$backupLastServerHash(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmSet$backupLastServerHash(jSONObject.getString(str));
                        }
                    }
                    str2 = KeysBackupDataEntityFields.BACKUP_LAST_SERVER_NUMBER_OF_KEYS;
                    if (jSONObject.has(str2)) {
                        if (jSONObject.isNull(str2)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmSet$backupLastServerNumberOfKeys(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmSet$backupLastServerNumberOfKeys(Integer.valueOf(jSONObject.getInt(str2)));
                        }
                    }
                    return org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy;
                } finally {
                    realmObjectContext.clear();
                }
            }
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy = null;
        if (org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy == null) {
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface2 = org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy;
        str = KeysBackupDataEntityFields.BACKUP_LAST_SERVER_HASH;
        if (jSONObject.has(str)) {
        }
        str2 = KeysBackupDataEntityFields.BACKUP_LAST_SERVER_NUMBER_OF_KEYS;
        if (jSONObject.has(str2)) {
        }
        return org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy;
    }

    public static KeysBackupDataEntity createUsingJsonStream(Realm realm, JsonReader jsonReader) throws IOException {
        KeysBackupDataEntity keysBackupDataEntity = new KeysBackupDataEntity();
        org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface = keysBackupDataEntity;
        jsonReader.beginObject();
        boolean z = false;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if (nextName.equals("primaryKey")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmSet$primaryKey(jsonReader.nextInt());
                    z = true;
                } else {
                    jsonReader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'primaryKey' to null.");
                }
            } else if (nextName.equals(KeysBackupDataEntityFields.BACKUP_LAST_SERVER_HASH)) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmSet$backupLastServerHash(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmSet$backupLastServerHash(null);
                }
            } else if (!nextName.equals(KeysBackupDataEntityFields.BACKUP_LAST_SERVER_NUMBER_OF_KEYS)) {
                jsonReader.skipValue();
            } else if (jsonReader.peek() != JsonToken.NULL) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmSet$backupLastServerNumberOfKeys(Integer.valueOf(jsonReader.nextInt()));
            } else {
                jsonReader.skipValue();
                org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmSet$backupLastServerNumberOfKeys(null);
            }
        }
        jsonReader.endObject();
        if (z) {
            return (KeysBackupDataEntity) realm.copyToRealm(keysBackupDataEntity, new ImportFlag[0]);
        }
        throw new IllegalArgumentException("JSON object doesn't have the primary key field 'primaryKey'.");
    }

    private static org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy newProxyInstance(BaseRealm baseRealm, Row row) {
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        realmObjectContext.set(baseRealm, row, baseRealm.getSchema().getColumnInfo(KeysBackupDataEntity.class), false, Collections.emptyList());
        org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy();
        realmObjectContext.clear();
        return org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0091  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x009b  */
    public static KeysBackupDataEntity copyOrUpdate(Realm realm, KeysBackupDataEntityColumnInfo keysBackupDataEntityColumnInfo, KeysBackupDataEntity keysBackupDataEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        boolean z2;
        if (keysBackupDataEntity instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) keysBackupDataEntity;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null) {
                BaseRealm realm$realm = realmObjectProxy.realmGet$proxyState().getRealm$realm();
                if (realm$realm.threadId != realm.threadId) {
                    throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
                } else if (realm$realm.getPath().equals(realm.getPath())) {
                    return keysBackupDataEntity;
                }
            }
        }
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        RealmObjectProxy realmObjectProxy2 = (RealmObjectProxy) map.get(keysBackupDataEntity);
        if (realmObjectProxy2 != null) {
            return (KeysBackupDataEntity) realmObjectProxy2;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy = null;
        if (z) {
            Table table = realm.getTable(KeysBackupDataEntity.class);
            long findFirstLong = table.findFirstLong(keysBackupDataEntityColumnInfo.primaryKeyIndex, (long) keysBackupDataEntity.realmGet$primaryKey());
            if (findFirstLong == -1) {
                z2 = false;
                return !z2 ? update(realm, keysBackupDataEntityColumnInfo, org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy, keysBackupDataEntity, map, set) : copy(realm, keysBackupDataEntityColumnInfo, keysBackupDataEntity, z, map, set);
            }
            try {
                realmObjectContext.set(realm, table.getUncheckedRow(findFirstLong), keysBackupDataEntityColumnInfo, false, Collections.emptyList());
                org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy();
                map.put(keysBackupDataEntity, org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy);
            } finally {
                realmObjectContext.clear();
            }
        }
        z2 = z;
        return !z2 ? update(realm, keysBackupDataEntityColumnInfo, org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy, keysBackupDataEntity, map, set) : copy(realm, keysBackupDataEntityColumnInfo, keysBackupDataEntity, z, map, set);
    }

    public static KeysBackupDataEntity copy(Realm realm, KeysBackupDataEntityColumnInfo keysBackupDataEntityColumnInfo, KeysBackupDataEntity keysBackupDataEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        RealmObjectProxy realmObjectProxy = (RealmObjectProxy) map.get(keysBackupDataEntity);
        if (realmObjectProxy != null) {
            return (KeysBackupDataEntity) realmObjectProxy;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface = keysBackupDataEntity;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(KeysBackupDataEntity.class), keysBackupDataEntityColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addInteger(keysBackupDataEntityColumnInfo.primaryKeyIndex, Integer.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$primaryKey()));
        osObjectBuilder.addString(keysBackupDataEntityColumnInfo.backupLastServerHashIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$backupLastServerHash());
        osObjectBuilder.addInteger(keysBackupDataEntityColumnInfo.backupLastServerNumberOfKeysIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$backupLastServerNumberOfKeys());
        org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy newProxyInstance = newProxyInstance(realm, osObjectBuilder.createNewObject());
        map.put(keysBackupDataEntity, newProxyInstance);
        return newProxyInstance;
    }

    public static long insert(Realm realm, KeysBackupDataEntity keysBackupDataEntity, Map<RealmModel, Long> map) {
        long j;
        long j2;
        KeysBackupDataEntity keysBackupDataEntity2 = keysBackupDataEntity;
        if (keysBackupDataEntity2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) keysBackupDataEntity2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(KeysBackupDataEntity.class);
        long nativePtr = table.getNativePtr();
        KeysBackupDataEntityColumnInfo keysBackupDataEntityColumnInfo = (KeysBackupDataEntityColumnInfo) realm.getSchema().getColumnInfo(KeysBackupDataEntity.class);
        long j3 = keysBackupDataEntityColumnInfo.primaryKeyIndex;
        org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface = keysBackupDataEntity2;
        Integer valueOf = Integer.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$primaryKey());
        if (valueOf != null) {
            j = Table.nativeFindFirstInt(nativePtr, j3, (long) org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$primaryKey());
        } else {
            j = -1;
        }
        if (j == -1) {
            j2 = OsObject.createRowWithPrimaryKey(table, j3, Integer.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$primaryKey()));
        } else {
            Table.throwDuplicatePrimaryKeyException(valueOf);
            j2 = j;
        }
        map.put(keysBackupDataEntity2, Long.valueOf(j2));
        String realmGet$backupLastServerHash = org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$backupLastServerHash();
        if (realmGet$backupLastServerHash != null) {
            Table.nativeSetString(nativePtr, keysBackupDataEntityColumnInfo.backupLastServerHashIndex, j2, realmGet$backupLastServerHash, false);
        }
        Integer realmGet$backupLastServerNumberOfKeys = org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$backupLastServerNumberOfKeys();
        if (realmGet$backupLastServerNumberOfKeys != null) {
            Table.nativeSetLong(nativePtr, keysBackupDataEntityColumnInfo.backupLastServerNumberOfKeysIndex, j2, realmGet$backupLastServerNumberOfKeys.longValue(), false);
        }
        return j2;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        long j2;
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(KeysBackupDataEntity.class);
        long nativePtr = table.getNativePtr();
        KeysBackupDataEntityColumnInfo keysBackupDataEntityColumnInfo = (KeysBackupDataEntityColumnInfo) realm.getSchema().getColumnInfo(KeysBackupDataEntity.class);
        long j3 = keysBackupDataEntityColumnInfo.primaryKeyIndex;
        while (it.hasNext()) {
            KeysBackupDataEntity keysBackupDataEntity = (KeysBackupDataEntity) it.next();
            if (!map2.containsKey(keysBackupDataEntity)) {
                if (keysBackupDataEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) keysBackupDataEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(keysBackupDataEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface = keysBackupDataEntity;
                Integer valueOf = Integer.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$primaryKey());
                if (valueOf != null) {
                    j = Table.nativeFindFirstInt(nativePtr, j3, (long) org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$primaryKey());
                } else {
                    j = -1;
                }
                if (j == -1) {
                    j = OsObject.createRowWithPrimaryKey(table, j3, Integer.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$primaryKey()));
                } else {
                    Table.throwDuplicatePrimaryKeyException(valueOf);
                }
                long j4 = j;
                map2.put(keysBackupDataEntity, Long.valueOf(j4));
                String realmGet$backupLastServerHash = org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$backupLastServerHash();
                if (realmGet$backupLastServerHash != null) {
                    j2 = j3;
                    Table.nativeSetString(nativePtr, keysBackupDataEntityColumnInfo.backupLastServerHashIndex, j4, realmGet$backupLastServerHash, false);
                } else {
                    j2 = j3;
                }
                Integer realmGet$backupLastServerNumberOfKeys = org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$backupLastServerNumberOfKeys();
                if (realmGet$backupLastServerNumberOfKeys != null) {
                    Table.nativeSetLong(nativePtr, keysBackupDataEntityColumnInfo.backupLastServerNumberOfKeysIndex, j4, realmGet$backupLastServerNumberOfKeys.longValue(), false);
                }
                j3 = j2;
            }
        }
    }

    public static long insertOrUpdate(Realm realm, KeysBackupDataEntity keysBackupDataEntity, Map<RealmModel, Long> map) {
        KeysBackupDataEntity keysBackupDataEntity2 = keysBackupDataEntity;
        if (keysBackupDataEntity2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) keysBackupDataEntity2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(KeysBackupDataEntity.class);
        long nativePtr = table.getNativePtr();
        KeysBackupDataEntityColumnInfo keysBackupDataEntityColumnInfo = (KeysBackupDataEntityColumnInfo) realm.getSchema().getColumnInfo(KeysBackupDataEntity.class);
        long j = keysBackupDataEntityColumnInfo.primaryKeyIndex;
        org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface = keysBackupDataEntity2;
        long nativeFindFirstInt = Integer.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$primaryKey()) != null ? Table.nativeFindFirstInt(nativePtr, j, (long) org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$primaryKey()) : -1;
        long createRowWithPrimaryKey = nativeFindFirstInt == -1 ? OsObject.createRowWithPrimaryKey(table, j, Integer.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$primaryKey())) : nativeFindFirstInt;
        map.put(keysBackupDataEntity2, Long.valueOf(createRowWithPrimaryKey));
        String realmGet$backupLastServerHash = org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$backupLastServerHash();
        if (realmGet$backupLastServerHash != null) {
            Table.nativeSetString(nativePtr, keysBackupDataEntityColumnInfo.backupLastServerHashIndex, createRowWithPrimaryKey, realmGet$backupLastServerHash, false);
        } else {
            Table.nativeSetNull(nativePtr, keysBackupDataEntityColumnInfo.backupLastServerHashIndex, createRowWithPrimaryKey, false);
        }
        Integer realmGet$backupLastServerNumberOfKeys = org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$backupLastServerNumberOfKeys();
        if (realmGet$backupLastServerNumberOfKeys != null) {
            Table.nativeSetLong(nativePtr, keysBackupDataEntityColumnInfo.backupLastServerNumberOfKeysIndex, createRowWithPrimaryKey, realmGet$backupLastServerNumberOfKeys.longValue(), false);
        } else {
            Table.nativeSetNull(nativePtr, keysBackupDataEntityColumnInfo.backupLastServerNumberOfKeysIndex, createRowWithPrimaryKey, false);
        }
        return createRowWithPrimaryKey;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        long j2;
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(KeysBackupDataEntity.class);
        long nativePtr = table.getNativePtr();
        KeysBackupDataEntityColumnInfo keysBackupDataEntityColumnInfo = (KeysBackupDataEntityColumnInfo) realm.getSchema().getColumnInfo(KeysBackupDataEntity.class);
        long j3 = keysBackupDataEntityColumnInfo.primaryKeyIndex;
        while (it.hasNext()) {
            KeysBackupDataEntity keysBackupDataEntity = (KeysBackupDataEntity) it.next();
            if (!map2.containsKey(keysBackupDataEntity)) {
                if (keysBackupDataEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) keysBackupDataEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(keysBackupDataEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface = keysBackupDataEntity;
                if (Integer.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$primaryKey()) != null) {
                    j = Table.nativeFindFirstInt(nativePtr, j3, (long) org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$primaryKey());
                } else {
                    j = -1;
                }
                if (j == -1) {
                    j = OsObject.createRowWithPrimaryKey(table, j3, Integer.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$primaryKey()));
                }
                long j4 = j;
                map2.put(keysBackupDataEntity, Long.valueOf(j4));
                String realmGet$backupLastServerHash = org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$backupLastServerHash();
                if (realmGet$backupLastServerHash != null) {
                    j2 = j3;
                    Table.nativeSetString(nativePtr, keysBackupDataEntityColumnInfo.backupLastServerHashIndex, j4, realmGet$backupLastServerHash, false);
                } else {
                    j2 = j3;
                    Table.nativeSetNull(nativePtr, keysBackupDataEntityColumnInfo.backupLastServerHashIndex, j4, false);
                }
                Integer realmGet$backupLastServerNumberOfKeys = org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmGet$backupLastServerNumberOfKeys();
                if (realmGet$backupLastServerNumberOfKeys != null) {
                    Table.nativeSetLong(nativePtr, keysBackupDataEntityColumnInfo.backupLastServerNumberOfKeysIndex, j4, realmGet$backupLastServerNumberOfKeys.longValue(), false);
                } else {
                    Table.nativeSetNull(nativePtr, keysBackupDataEntityColumnInfo.backupLastServerNumberOfKeysIndex, j4, false);
                }
                j3 = j2;
            }
        }
    }

    public static KeysBackupDataEntity createDetachedCopy(KeysBackupDataEntity keysBackupDataEntity, int i, int i2, Map<RealmModel, CacheData<RealmModel>> map) {
        KeysBackupDataEntity keysBackupDataEntity2;
        if (i > i2 || keysBackupDataEntity == null) {
            return null;
        }
        CacheData cacheData = (CacheData) map.get(keysBackupDataEntity);
        if (cacheData == null) {
            keysBackupDataEntity2 = new KeysBackupDataEntity();
            map.put(keysBackupDataEntity, new CacheData(i, keysBackupDataEntity2));
        } else if (i >= cacheData.minDepth) {
            return (KeysBackupDataEntity) cacheData.object;
        } else {
            KeysBackupDataEntity keysBackupDataEntity3 = (KeysBackupDataEntity) cacheData.object;
            cacheData.minDepth = i;
            keysBackupDataEntity2 = keysBackupDataEntity3;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface = keysBackupDataEntity2;
        org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface2 = keysBackupDataEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmSet$primaryKey(org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface2.realmGet$primaryKey());
        org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmSet$backupLastServerHash(org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface2.realmGet$backupLastServerHash());
        org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface.realmSet$backupLastServerNumberOfKeys(org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface2.realmGet$backupLastServerNumberOfKeys());
        return keysBackupDataEntity2;
    }

    static KeysBackupDataEntity update(Realm realm, KeysBackupDataEntityColumnInfo keysBackupDataEntityColumnInfo, KeysBackupDataEntity keysBackupDataEntity, KeysBackupDataEntity keysBackupDataEntity2, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface = keysBackupDataEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface2 = keysBackupDataEntity2;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(KeysBackupDataEntity.class), keysBackupDataEntityColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addInteger(keysBackupDataEntityColumnInfo.primaryKeyIndex, Integer.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface2.realmGet$primaryKey()));
        osObjectBuilder.addString(keysBackupDataEntityColumnInfo.backupLastServerHashIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface2.realmGet$backupLastServerHash());
        osObjectBuilder.addInteger(keysBackupDataEntityColumnInfo.backupLastServerNumberOfKeysIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxyinterface2.realmGet$backupLastServerNumberOfKeys());
        osObjectBuilder.updateExistingObject();
        return keysBackupDataEntity;
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r4v2, types: [java.lang.Integer] */
    /* JADX WARNING: type inference failed for: r4v3 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder sb = new StringBuilder("KeysBackupDataEntity = proxy[");
        sb.append("{primaryKey:");
        sb.append(realmGet$primaryKey());
        String str = "}";
        sb.append(str);
        String str2 = ",";
        sb.append(str2);
        sb.append("{backupLastServerHash:");
        Object obj = "null";
        sb.append(realmGet$backupLastServerHash() != null ? realmGet$backupLastServerHash() : obj);
        sb.append(str);
        sb.append(str2);
        sb.append("{backupLastServerNumberOfKeys:");
        if (realmGet$backupLastServerNumberOfKeys() != null) {
            obj = realmGet$backupLastServerNumberOfKeys();
        }
        sb.append(obj);
        sb.append(str);
        sb.append("]");
        return sb.toString();
    }

    public ProxyState<?> realmGet$proxyState() {
        return this.proxyState;
    }

    public int hashCode() {
        String path = this.proxyState.getRealm$realm().getPath();
        String name = this.proxyState.getRow$realm().getTable().getName();
        long index = this.proxyState.getRow$realm().getIndex();
        int i = 0;
        int hashCode = (527 + (path != null ? path.hashCode() : 0)) * 31;
        if (name != null) {
            i = name.hashCode();
        }
        return ((hashCode + i) * 31) + ((int) ((index >>> 32) ^ index));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy) obj;
        String path = this.proxyState.getRealm$realm().getPath();
        String path2 = org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy.proxyState.getRealm$realm().getPath();
        if (path == null ? path2 != null : !path.equals(path2)) {
            return false;
        }
        String name = this.proxyState.getRow$realm().getTable().getName();
        String name2 = org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy.proxyState.getRow$realm().getTable().getName();
        if (name == null ? name2 == null : name.equals(name2)) {
            return this.proxyState.getRow$realm().getIndex() == org_matrix_androidsdk_crypto_cryptostore_db_model_keysbackupdataentityrealmproxy.proxyState.getRow$realm().getIndex();
        }
        return false;
    }
}
