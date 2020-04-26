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
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoMetadataEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoMetadataEntityFields;

public class org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy extends CryptoMetadataEntity implements RealmObjectProxy, org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface {
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();
    private CryptoMetadataEntityColumnInfo columnInfo;
    private ProxyState<CryptoMetadataEntity> proxyState;

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "CryptoMetadataEntity";
    }

    static final class CryptoMetadataEntityColumnInfo extends ColumnInfo {
        long backupVersionIndex;
        long deviceIdIndex;
        long deviceSyncTokenIndex;
        long globalBlacklistUnverifiedDevicesIndex;
        long maxColumnIndexValue;
        long olmAccountDataIndex;
        long userIdIndex;

        CryptoMetadataEntityColumnInfo(OsSchemaInfo osSchemaInfo) {
            super(6);
            OsObjectSchemaInfo objectSchemaInfo = osSchemaInfo.getObjectSchemaInfo(ClassNameHelper.INTERNAL_CLASS_NAME);
            String str = "userId";
            this.userIdIndex = addColumnDetails(str, str, objectSchemaInfo);
            String str2 = "deviceId";
            this.deviceIdIndex = addColumnDetails(str2, str2, objectSchemaInfo);
            String str3 = CryptoMetadataEntityFields.OLM_ACCOUNT_DATA;
            this.olmAccountDataIndex = addColumnDetails(str3, str3, objectSchemaInfo);
            String str4 = CryptoMetadataEntityFields.DEVICE_SYNC_TOKEN;
            this.deviceSyncTokenIndex = addColumnDetails(str4, str4, objectSchemaInfo);
            String str5 = CryptoMetadataEntityFields.GLOBAL_BLACKLIST_UNVERIFIED_DEVICES;
            this.globalBlacklistUnverifiedDevicesIndex = addColumnDetails(str5, str5, objectSchemaInfo);
            String str6 = CryptoMetadataEntityFields.BACKUP_VERSION;
            this.backupVersionIndex = addColumnDetails(str6, str6, objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        CryptoMetadataEntityColumnInfo(ColumnInfo columnInfo, boolean z) {
            super(columnInfo, z);
            copy(columnInfo, this);
        }

        /* access modifiers changed from: protected */
        public final ColumnInfo copy(boolean z) {
            return new CryptoMetadataEntityColumnInfo(this, z);
        }

        /* access modifiers changed from: protected */
        public final void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2) {
            CryptoMetadataEntityColumnInfo cryptoMetadataEntityColumnInfo = (CryptoMetadataEntityColumnInfo) columnInfo;
            CryptoMetadataEntityColumnInfo cryptoMetadataEntityColumnInfo2 = (CryptoMetadataEntityColumnInfo) columnInfo2;
            cryptoMetadataEntityColumnInfo2.userIdIndex = cryptoMetadataEntityColumnInfo.userIdIndex;
            cryptoMetadataEntityColumnInfo2.deviceIdIndex = cryptoMetadataEntityColumnInfo.deviceIdIndex;
            cryptoMetadataEntityColumnInfo2.olmAccountDataIndex = cryptoMetadataEntityColumnInfo.olmAccountDataIndex;
            cryptoMetadataEntityColumnInfo2.deviceSyncTokenIndex = cryptoMetadataEntityColumnInfo.deviceSyncTokenIndex;
            cryptoMetadataEntityColumnInfo2.globalBlacklistUnverifiedDevicesIndex = cryptoMetadataEntityColumnInfo.globalBlacklistUnverifiedDevicesIndex;
            cryptoMetadataEntityColumnInfo2.backupVersionIndex = cryptoMetadataEntityColumnInfo.backupVersionIndex;
            cryptoMetadataEntityColumnInfo2.maxColumnIndexValue = cryptoMetadataEntityColumnInfo.maxColumnIndexValue;
        }
    }

    public static String getSimpleClassName() {
        return ClassNameHelper.INTERNAL_CLASS_NAME;
    }

    org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy() {
        this.proxyState.setConstructionFinished();
    }

    public void realm$injectObjectContext() {
        if (this.proxyState == null) {
            RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
            this.columnInfo = (CryptoMetadataEntityColumnInfo) realmObjectContext.getColumnInfo();
            this.proxyState = new ProxyState<>(this);
            this.proxyState.setRealm$realm(realmObjectContext.getRealm());
            this.proxyState.setRow$realm(realmObjectContext.getRow());
            this.proxyState.setAcceptDefaultValue$realm(realmObjectContext.getAcceptDefaultValue());
            this.proxyState.setExcludeFields$realm(realmObjectContext.getExcludeFields());
        }
    }

    public String realmGet$userId() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.userIdIndex);
    }

    public void realmSet$userId(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            throw new RealmException("Primary key field 'userId' cannot be changed after object was created.");
        }
    }

    public String realmGet$deviceId() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.deviceIdIndex);
    }

    public void realmSet$deviceId(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.deviceIdIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.deviceIdIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.deviceIdIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.deviceIdIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public String realmGet$olmAccountData() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.olmAccountDataIndex);
    }

    public void realmSet$olmAccountData(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.olmAccountDataIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.olmAccountDataIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.olmAccountDataIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.olmAccountDataIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public String realmGet$deviceSyncToken() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.deviceSyncTokenIndex);
    }

    public void realmSet$deviceSyncToken(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.deviceSyncTokenIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.deviceSyncTokenIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.deviceSyncTokenIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.deviceSyncTokenIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public boolean realmGet$globalBlacklistUnverifiedDevices() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getBoolean(this.columnInfo.globalBlacklistUnverifiedDevicesIndex);
    }

    public void realmSet$globalBlacklistUnverifiedDevices(boolean z) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            this.proxyState.getRow$realm().setBoolean(this.columnInfo.globalBlacklistUnverifiedDevicesIndex, z);
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            row$realm.getTable().setBoolean(this.columnInfo.globalBlacklistUnverifiedDevicesIndex, row$realm.getIndex(), z, true);
        }
    }

    public String realmGet$backupVersion() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.backupVersionIndex);
    }

    public void realmSet$backupVersion(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.backupVersionIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.backupVersionIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.backupVersionIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.backupVersionIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        Builder builder = new Builder(ClassNameHelper.INTERNAL_CLASS_NAME, 6, 0);
        Builder builder2 = builder;
        builder2.addPersistedProperty("userId", RealmFieldType.STRING, true, true, false);
        builder2.addPersistedProperty("deviceId", RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty(CryptoMetadataEntityFields.OLM_ACCOUNT_DATA, RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty(CryptoMetadataEntityFields.DEVICE_SYNC_TOKEN, RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty(CryptoMetadataEntityFields.GLOBAL_BLACKLIST_UNVERIFIED_DEVICES, RealmFieldType.BOOLEAN, false, false, true);
        builder2.addPersistedProperty(CryptoMetadataEntityFields.BACKUP_VERSION, RealmFieldType.STRING, false, false, false);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static CryptoMetadataEntityColumnInfo createColumnInfo(OsSchemaInfo osSchemaInfo) {
        return new CryptoMetadataEntityColumnInfo(osSchemaInfo);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ea  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0108  */
    public static CryptoMetadataEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject jSONObject, boolean z) throws JSONException {
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        long j;
        List emptyList = Collections.emptyList();
        String str6 = "userId";
        if (z) {
            Table table = realm.getTable(CryptoMetadataEntity.class);
            long j2 = ((CryptoMetadataEntityColumnInfo) realm.getSchema().getColumnInfo(CryptoMetadataEntity.class)).userIdIndex;
            if (jSONObject.isNull(str6)) {
                j = table.findFirstNull(j2);
            } else {
                j = table.findFirstString(j2, jSONObject.getString(str6));
            }
            if (j != -1) {
                RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
                try {
                    realmObjectContext.set(realm, table.getUncheckedRow(j), realm.getSchema().getColumnInfo(CryptoMetadataEntity.class), false, Collections.emptyList());
                    org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy();
                    if (org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy == null) {
                        if (!jSONObject.has(str6)) {
                            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'userId'.");
                        } else if (jSONObject.isNull(str6)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy) realm.createObjectInternal(CryptoMetadataEntity.class, null, true, emptyList);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy) realm.createObjectInternal(CryptoMetadataEntity.class, jSONObject.getString(str6), true, emptyList);
                        }
                    }
                    org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy;
                    str = "deviceId";
                    if (jSONObject.has(str)) {
                        if (jSONObject.isNull(str)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$deviceId(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$deviceId(jSONObject.getString(str));
                        }
                    }
                    str2 = CryptoMetadataEntityFields.OLM_ACCOUNT_DATA;
                    if (jSONObject.has(str2)) {
                        if (jSONObject.isNull(str2)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$olmAccountData(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$olmAccountData(jSONObject.getString(str2));
                        }
                    }
                    str3 = CryptoMetadataEntityFields.DEVICE_SYNC_TOKEN;
                    if (jSONObject.has(str3)) {
                        if (jSONObject.isNull(str3)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$deviceSyncToken(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$deviceSyncToken(jSONObject.getString(str3));
                        }
                    }
                    str4 = CryptoMetadataEntityFields.GLOBAL_BLACKLIST_UNVERIFIED_DEVICES;
                    if (jSONObject.has(str4)) {
                        if (!jSONObject.isNull(str4)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$globalBlacklistUnverifiedDevices(jSONObject.getBoolean(str4));
                        } else {
                            throw new IllegalArgumentException("Trying to set non-nullable field 'globalBlacklistUnverifiedDevices' to null.");
                        }
                    }
                    str5 = CryptoMetadataEntityFields.BACKUP_VERSION;
                    if (jSONObject.has(str5)) {
                        if (jSONObject.isNull(str5)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$backupVersion(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$backupVersion(jSONObject.getString(str5));
                        }
                    }
                    return org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy;
                } finally {
                    realmObjectContext.clear();
                }
            }
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy = null;
        if (org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy == null) {
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface2 = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy;
        str = "deviceId";
        if (jSONObject.has(str)) {
        }
        str2 = CryptoMetadataEntityFields.OLM_ACCOUNT_DATA;
        if (jSONObject.has(str2)) {
        }
        str3 = CryptoMetadataEntityFields.DEVICE_SYNC_TOKEN;
        if (jSONObject.has(str3)) {
        }
        str4 = CryptoMetadataEntityFields.GLOBAL_BLACKLIST_UNVERIFIED_DEVICES;
        if (jSONObject.has(str4)) {
        }
        str5 = CryptoMetadataEntityFields.BACKUP_VERSION;
        if (jSONObject.has(str5)) {
        }
        return org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy;
    }

    public static CryptoMetadataEntity createUsingJsonStream(Realm realm, JsonReader jsonReader) throws IOException {
        CryptoMetadataEntity cryptoMetadataEntity = new CryptoMetadataEntity();
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface = cryptoMetadataEntity;
        jsonReader.beginObject();
        boolean z = false;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if (nextName.equals("userId")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$userId(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$userId(null);
                }
                z = true;
            } else if (nextName.equals("deviceId")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$deviceId(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$deviceId(null);
                }
            } else if (nextName.equals(CryptoMetadataEntityFields.OLM_ACCOUNT_DATA)) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$olmAccountData(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$olmAccountData(null);
                }
            } else if (nextName.equals(CryptoMetadataEntityFields.DEVICE_SYNC_TOKEN)) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$deviceSyncToken(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$deviceSyncToken(null);
                }
            } else if (nextName.equals(CryptoMetadataEntityFields.GLOBAL_BLACKLIST_UNVERIFIED_DEVICES)) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$globalBlacklistUnverifiedDevices(jsonReader.nextBoolean());
                } else {
                    jsonReader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'globalBlacklistUnverifiedDevices' to null.");
                }
            } else if (!nextName.equals(CryptoMetadataEntityFields.BACKUP_VERSION)) {
                jsonReader.skipValue();
            } else if (jsonReader.peek() != JsonToken.NULL) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$backupVersion(jsonReader.nextString());
            } else {
                jsonReader.skipValue();
                org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$backupVersion(null);
            }
        }
        jsonReader.endObject();
        if (z) {
            return (CryptoMetadataEntity) realm.copyToRealm(cryptoMetadataEntity, new ImportFlag[0]);
        }
        throw new IllegalArgumentException("JSON object doesn't have the primary key field 'userId'.");
    }

    private static org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy newProxyInstance(BaseRealm baseRealm, Row row) {
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        realmObjectContext.set(baseRealm, row, baseRealm.getSchema().getColumnInfo(CryptoMetadataEntity.class), false, Collections.emptyList());
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy();
        realmObjectContext.clear();
        return org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a1  */
    public static CryptoMetadataEntity copyOrUpdate(Realm realm, CryptoMetadataEntityColumnInfo cryptoMetadataEntityColumnInfo, CryptoMetadataEntity cryptoMetadataEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        boolean z2;
        long j;
        if (cryptoMetadataEntity instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) cryptoMetadataEntity;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null) {
                BaseRealm realm$realm = realmObjectProxy.realmGet$proxyState().getRealm$realm();
                if (realm$realm.threadId != realm.threadId) {
                    throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
                } else if (realm$realm.getPath().equals(realm.getPath())) {
                    return cryptoMetadataEntity;
                }
            }
        }
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        RealmObjectProxy realmObjectProxy2 = (RealmObjectProxy) map.get(cryptoMetadataEntity);
        if (realmObjectProxy2 != null) {
            return (CryptoMetadataEntity) realmObjectProxy2;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy = null;
        if (z) {
            Table table = realm.getTable(CryptoMetadataEntity.class);
            long j2 = cryptoMetadataEntityColumnInfo.userIdIndex;
            String realmGet$userId = cryptoMetadataEntity.realmGet$userId();
            if (realmGet$userId == null) {
                j = table.findFirstNull(j2);
            } else {
                j = table.findFirstString(j2, realmGet$userId);
            }
            if (j == -1) {
                z2 = false;
                return !z2 ? update(realm, cryptoMetadataEntityColumnInfo, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy, cryptoMetadataEntity, map, set) : copy(realm, cryptoMetadataEntityColumnInfo, cryptoMetadataEntity, z, map, set);
            }
            try {
                realmObjectContext.set(realm, table.getUncheckedRow(j), cryptoMetadataEntityColumnInfo, false, Collections.emptyList());
                org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy();
                map.put(cryptoMetadataEntity, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy);
            } finally {
                realmObjectContext.clear();
            }
        }
        z2 = z;
        return !z2 ? update(realm, cryptoMetadataEntityColumnInfo, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy, cryptoMetadataEntity, map, set) : copy(realm, cryptoMetadataEntityColumnInfo, cryptoMetadataEntity, z, map, set);
    }

    public static CryptoMetadataEntity copy(Realm realm, CryptoMetadataEntityColumnInfo cryptoMetadataEntityColumnInfo, CryptoMetadataEntity cryptoMetadataEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        RealmObjectProxy realmObjectProxy = (RealmObjectProxy) map.get(cryptoMetadataEntity);
        if (realmObjectProxy != null) {
            return (CryptoMetadataEntity) realmObjectProxy;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface = cryptoMetadataEntity;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(CryptoMetadataEntity.class), cryptoMetadataEntityColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addString(cryptoMetadataEntityColumnInfo.userIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$userId());
        osObjectBuilder.addString(cryptoMetadataEntityColumnInfo.deviceIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$deviceId());
        osObjectBuilder.addString(cryptoMetadataEntityColumnInfo.olmAccountDataIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$olmAccountData());
        osObjectBuilder.addString(cryptoMetadataEntityColumnInfo.deviceSyncTokenIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$deviceSyncToken());
        osObjectBuilder.addBoolean(cryptoMetadataEntityColumnInfo.globalBlacklistUnverifiedDevicesIndex, Boolean.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$globalBlacklistUnverifiedDevices()));
        osObjectBuilder.addString(cryptoMetadataEntityColumnInfo.backupVersionIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$backupVersion());
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy newProxyInstance = newProxyInstance(realm, osObjectBuilder.createNewObject());
        map.put(cryptoMetadataEntity, newProxyInstance);
        return newProxyInstance;
    }

    public static long insert(Realm realm, CryptoMetadataEntity cryptoMetadataEntity, Map<RealmModel, Long> map) {
        long j;
        long j2;
        CryptoMetadataEntity cryptoMetadataEntity2 = cryptoMetadataEntity;
        if (cryptoMetadataEntity2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) cryptoMetadataEntity2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(CryptoMetadataEntity.class);
        long nativePtr = table.getNativePtr();
        CryptoMetadataEntityColumnInfo cryptoMetadataEntityColumnInfo = (CryptoMetadataEntityColumnInfo) realm.getSchema().getColumnInfo(CryptoMetadataEntity.class);
        long j3 = cryptoMetadataEntityColumnInfo.userIdIndex;
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface = cryptoMetadataEntity2;
        String realmGet$userId = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$userId();
        if (realmGet$userId == null) {
            j = Table.nativeFindFirstNull(nativePtr, j3);
        } else {
            j = Table.nativeFindFirstString(nativePtr, j3, realmGet$userId);
        }
        if (j == -1) {
            j2 = OsObject.createRowWithPrimaryKey(table, j3, realmGet$userId);
        } else {
            Table.throwDuplicatePrimaryKeyException(realmGet$userId);
            j2 = j;
        }
        map.put(cryptoMetadataEntity2, Long.valueOf(j2));
        String realmGet$deviceId = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$deviceId();
        if (realmGet$deviceId != null) {
            Table.nativeSetString(nativePtr, cryptoMetadataEntityColumnInfo.deviceIdIndex, j2, realmGet$deviceId, false);
        }
        String realmGet$olmAccountData = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$olmAccountData();
        if (realmGet$olmAccountData != null) {
            Table.nativeSetString(nativePtr, cryptoMetadataEntityColumnInfo.olmAccountDataIndex, j2, realmGet$olmAccountData, false);
        }
        String realmGet$deviceSyncToken = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$deviceSyncToken();
        if (realmGet$deviceSyncToken != null) {
            Table.nativeSetString(nativePtr, cryptoMetadataEntityColumnInfo.deviceSyncTokenIndex, j2, realmGet$deviceSyncToken, false);
        }
        Table.nativeSetBoolean(nativePtr, cryptoMetadataEntityColumnInfo.globalBlacklistUnverifiedDevicesIndex, j2, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$globalBlacklistUnverifiedDevices(), false);
        String realmGet$backupVersion = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$backupVersion();
        if (realmGet$backupVersion != null) {
            Table.nativeSetString(nativePtr, cryptoMetadataEntityColumnInfo.backupVersionIndex, j2, realmGet$backupVersion, false);
        }
        return j2;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        long j2;
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(CryptoMetadataEntity.class);
        long nativePtr = table.getNativePtr();
        CryptoMetadataEntityColumnInfo cryptoMetadataEntityColumnInfo = (CryptoMetadataEntityColumnInfo) realm.getSchema().getColumnInfo(CryptoMetadataEntity.class);
        long j3 = cryptoMetadataEntityColumnInfo.userIdIndex;
        while (it.hasNext()) {
            CryptoMetadataEntity cryptoMetadataEntity = (CryptoMetadataEntity) it.next();
            if (!map2.containsKey(cryptoMetadataEntity)) {
                if (cryptoMetadataEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) cryptoMetadataEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(cryptoMetadataEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface = cryptoMetadataEntity;
                String realmGet$userId = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$userId();
                if (realmGet$userId == null) {
                    j = Table.nativeFindFirstNull(nativePtr, j3);
                } else {
                    j = Table.nativeFindFirstString(nativePtr, j3, realmGet$userId);
                }
                if (j == -1) {
                    j2 = OsObject.createRowWithPrimaryKey(table, j3, realmGet$userId);
                } else {
                    Table.throwDuplicatePrimaryKeyException(realmGet$userId);
                    j2 = j;
                }
                map2.put(cryptoMetadataEntity, Long.valueOf(j2));
                String realmGet$deviceId = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$deviceId();
                if (realmGet$deviceId != null) {
                    Table.nativeSetString(nativePtr, cryptoMetadataEntityColumnInfo.deviceIdIndex, j2, realmGet$deviceId, false);
                }
                String realmGet$olmAccountData = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$olmAccountData();
                if (realmGet$olmAccountData != null) {
                    Table.nativeSetString(nativePtr, cryptoMetadataEntityColumnInfo.olmAccountDataIndex, j2, realmGet$olmAccountData, false);
                }
                String realmGet$deviceSyncToken = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$deviceSyncToken();
                if (realmGet$deviceSyncToken != null) {
                    Table.nativeSetString(nativePtr, cryptoMetadataEntityColumnInfo.deviceSyncTokenIndex, j2, realmGet$deviceSyncToken, false);
                }
                Table.nativeSetBoolean(nativePtr, cryptoMetadataEntityColumnInfo.globalBlacklistUnverifiedDevicesIndex, j2, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$globalBlacklistUnverifiedDevices(), false);
                String realmGet$backupVersion = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$backupVersion();
                if (realmGet$backupVersion != null) {
                    Table.nativeSetString(nativePtr, cryptoMetadataEntityColumnInfo.backupVersionIndex, j2, realmGet$backupVersion, false);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, CryptoMetadataEntity cryptoMetadataEntity, Map<RealmModel, Long> map) {
        long j;
        CryptoMetadataEntity cryptoMetadataEntity2 = cryptoMetadataEntity;
        if (cryptoMetadataEntity2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) cryptoMetadataEntity2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(CryptoMetadataEntity.class);
        long nativePtr = table.getNativePtr();
        CryptoMetadataEntityColumnInfo cryptoMetadataEntityColumnInfo = (CryptoMetadataEntityColumnInfo) realm.getSchema().getColumnInfo(CryptoMetadataEntity.class);
        long j2 = cryptoMetadataEntityColumnInfo.userIdIndex;
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface = cryptoMetadataEntity2;
        String realmGet$userId = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$userId();
        if (realmGet$userId == null) {
            j = Table.nativeFindFirstNull(nativePtr, j2);
        } else {
            j = Table.nativeFindFirstString(nativePtr, j2, realmGet$userId);
        }
        long createRowWithPrimaryKey = j == -1 ? OsObject.createRowWithPrimaryKey(table, j2, realmGet$userId) : j;
        map.put(cryptoMetadataEntity2, Long.valueOf(createRowWithPrimaryKey));
        String realmGet$deviceId = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$deviceId();
        if (realmGet$deviceId != null) {
            Table.nativeSetString(nativePtr, cryptoMetadataEntityColumnInfo.deviceIdIndex, createRowWithPrimaryKey, realmGet$deviceId, false);
        } else {
            Table.nativeSetNull(nativePtr, cryptoMetadataEntityColumnInfo.deviceIdIndex, createRowWithPrimaryKey, false);
        }
        String realmGet$olmAccountData = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$olmAccountData();
        if (realmGet$olmAccountData != null) {
            Table.nativeSetString(nativePtr, cryptoMetadataEntityColumnInfo.olmAccountDataIndex, createRowWithPrimaryKey, realmGet$olmAccountData, false);
        } else {
            Table.nativeSetNull(nativePtr, cryptoMetadataEntityColumnInfo.olmAccountDataIndex, createRowWithPrimaryKey, false);
        }
        String realmGet$deviceSyncToken = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$deviceSyncToken();
        if (realmGet$deviceSyncToken != null) {
            Table.nativeSetString(nativePtr, cryptoMetadataEntityColumnInfo.deviceSyncTokenIndex, createRowWithPrimaryKey, realmGet$deviceSyncToken, false);
        } else {
            Table.nativeSetNull(nativePtr, cryptoMetadataEntityColumnInfo.deviceSyncTokenIndex, createRowWithPrimaryKey, false);
        }
        Table.nativeSetBoolean(nativePtr, cryptoMetadataEntityColumnInfo.globalBlacklistUnverifiedDevicesIndex, createRowWithPrimaryKey, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$globalBlacklistUnverifiedDevices(), false);
        String realmGet$backupVersion = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$backupVersion();
        if (realmGet$backupVersion != null) {
            Table.nativeSetString(nativePtr, cryptoMetadataEntityColumnInfo.backupVersionIndex, createRowWithPrimaryKey, realmGet$backupVersion, false);
        } else {
            Table.nativeSetNull(nativePtr, cryptoMetadataEntityColumnInfo.backupVersionIndex, createRowWithPrimaryKey, false);
        }
        return createRowWithPrimaryKey;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(CryptoMetadataEntity.class);
        long nativePtr = table.getNativePtr();
        CryptoMetadataEntityColumnInfo cryptoMetadataEntityColumnInfo = (CryptoMetadataEntityColumnInfo) realm.getSchema().getColumnInfo(CryptoMetadataEntity.class);
        long j2 = cryptoMetadataEntityColumnInfo.userIdIndex;
        while (it.hasNext()) {
            CryptoMetadataEntity cryptoMetadataEntity = (CryptoMetadataEntity) it.next();
            if (!map2.containsKey(cryptoMetadataEntity)) {
                if (cryptoMetadataEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) cryptoMetadataEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(cryptoMetadataEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface = cryptoMetadataEntity;
                String realmGet$userId = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$userId();
                if (realmGet$userId == null) {
                    j = Table.nativeFindFirstNull(nativePtr, j2);
                } else {
                    j = Table.nativeFindFirstString(nativePtr, j2, realmGet$userId);
                }
                long createRowWithPrimaryKey = j == -1 ? OsObject.createRowWithPrimaryKey(table, j2, realmGet$userId) : j;
                map2.put(cryptoMetadataEntity, Long.valueOf(createRowWithPrimaryKey));
                String realmGet$deviceId = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$deviceId();
                if (realmGet$deviceId != null) {
                    Table.nativeSetString(nativePtr, cryptoMetadataEntityColumnInfo.deviceIdIndex, createRowWithPrimaryKey, realmGet$deviceId, false);
                } else {
                    Table.nativeSetNull(nativePtr, cryptoMetadataEntityColumnInfo.deviceIdIndex, createRowWithPrimaryKey, false);
                }
                String realmGet$olmAccountData = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$olmAccountData();
                if (realmGet$olmAccountData != null) {
                    Table.nativeSetString(nativePtr, cryptoMetadataEntityColumnInfo.olmAccountDataIndex, createRowWithPrimaryKey, realmGet$olmAccountData, false);
                } else {
                    Table.nativeSetNull(nativePtr, cryptoMetadataEntityColumnInfo.olmAccountDataIndex, createRowWithPrimaryKey, false);
                }
                String realmGet$deviceSyncToken = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$deviceSyncToken();
                if (realmGet$deviceSyncToken != null) {
                    Table.nativeSetString(nativePtr, cryptoMetadataEntityColumnInfo.deviceSyncTokenIndex, createRowWithPrimaryKey, realmGet$deviceSyncToken, false);
                } else {
                    Table.nativeSetNull(nativePtr, cryptoMetadataEntityColumnInfo.deviceSyncTokenIndex, createRowWithPrimaryKey, false);
                }
                Table.nativeSetBoolean(nativePtr, cryptoMetadataEntityColumnInfo.globalBlacklistUnverifiedDevicesIndex, createRowWithPrimaryKey, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$globalBlacklistUnverifiedDevices(), false);
                String realmGet$backupVersion = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmGet$backupVersion();
                if (realmGet$backupVersion != null) {
                    Table.nativeSetString(nativePtr, cryptoMetadataEntityColumnInfo.backupVersionIndex, createRowWithPrimaryKey, realmGet$backupVersion, false);
                } else {
                    Table.nativeSetNull(nativePtr, cryptoMetadataEntityColumnInfo.backupVersionIndex, createRowWithPrimaryKey, false);
                }
            }
        }
    }

    public static CryptoMetadataEntity createDetachedCopy(CryptoMetadataEntity cryptoMetadataEntity, int i, int i2, Map<RealmModel, CacheData<RealmModel>> map) {
        CryptoMetadataEntity cryptoMetadataEntity2;
        if (i > i2 || cryptoMetadataEntity == null) {
            return null;
        }
        CacheData cacheData = (CacheData) map.get(cryptoMetadataEntity);
        if (cacheData == null) {
            cryptoMetadataEntity2 = new CryptoMetadataEntity();
            map.put(cryptoMetadataEntity, new CacheData(i, cryptoMetadataEntity2));
        } else if (i >= cacheData.minDepth) {
            return (CryptoMetadataEntity) cacheData.object;
        } else {
            CryptoMetadataEntity cryptoMetadataEntity3 = (CryptoMetadataEntity) cacheData.object;
            cacheData.minDepth = i;
            cryptoMetadataEntity2 = cryptoMetadataEntity3;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface = cryptoMetadataEntity2;
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface2 = cryptoMetadataEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$userId(org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface2.realmGet$userId());
        org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$deviceId(org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface2.realmGet$deviceId());
        org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$olmAccountData(org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface2.realmGet$olmAccountData());
        org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$deviceSyncToken(org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface2.realmGet$deviceSyncToken());
        org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$globalBlacklistUnverifiedDevices(org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface2.realmGet$globalBlacklistUnverifiedDevices());
        org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface.realmSet$backupVersion(org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface2.realmGet$backupVersion());
        return cryptoMetadataEntity2;
    }

    static CryptoMetadataEntity update(Realm realm, CryptoMetadataEntityColumnInfo cryptoMetadataEntityColumnInfo, CryptoMetadataEntity cryptoMetadataEntity, CryptoMetadataEntity cryptoMetadataEntity2, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface = cryptoMetadataEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface2 = cryptoMetadataEntity2;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(CryptoMetadataEntity.class), cryptoMetadataEntityColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addString(cryptoMetadataEntityColumnInfo.userIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface2.realmGet$userId());
        osObjectBuilder.addString(cryptoMetadataEntityColumnInfo.deviceIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface2.realmGet$deviceId());
        osObjectBuilder.addString(cryptoMetadataEntityColumnInfo.olmAccountDataIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface2.realmGet$olmAccountData());
        osObjectBuilder.addString(cryptoMetadataEntityColumnInfo.deviceSyncTokenIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface2.realmGet$deviceSyncToken());
        osObjectBuilder.addBoolean(cryptoMetadataEntityColumnInfo.globalBlacklistUnverifiedDevicesIndex, Boolean.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface2.realmGet$globalBlacklistUnverifiedDevices()));
        osObjectBuilder.addString(cryptoMetadataEntityColumnInfo.backupVersionIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxyinterface2.realmGet$backupVersion());
        osObjectBuilder.updateExistingObject();
        return cryptoMetadataEntity;
    }

    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder sb = new StringBuilder("CryptoMetadataEntity = proxy[");
        sb.append("{userId:");
        String str = "null";
        sb.append(realmGet$userId() != null ? realmGet$userId() : str);
        String str2 = "}";
        sb.append(str2);
        String str3 = ",";
        sb.append(str3);
        sb.append("{deviceId:");
        sb.append(realmGet$deviceId() != null ? realmGet$deviceId() : str);
        sb.append(str2);
        sb.append(str3);
        sb.append("{olmAccountData:");
        sb.append(realmGet$olmAccountData() != null ? realmGet$olmAccountData() : str);
        sb.append(str2);
        sb.append(str3);
        sb.append("{deviceSyncToken:");
        sb.append(realmGet$deviceSyncToken() != null ? realmGet$deviceSyncToken() : str);
        sb.append(str2);
        sb.append(str3);
        sb.append("{globalBlacklistUnverifiedDevices:");
        sb.append(realmGet$globalBlacklistUnverifiedDevices());
        sb.append(str2);
        sb.append(str3);
        sb.append("{backupVersion:");
        if (realmGet$backupVersion() != null) {
            str = realmGet$backupVersion();
        }
        sb.append(str);
        sb.append(str2);
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
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxy) obj;
        String path = this.proxyState.getRealm$realm().getPath();
        String path2 = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy.proxyState.getRealm$realm().getPath();
        if (path == null ? path2 != null : !path.equals(path2)) {
            return false;
        }
        String name = this.proxyState.getRow$realm().getTable().getName();
        String name2 = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy.proxyState.getRow$realm().getTable().getName();
        if (name == null ? name2 == null : name.equals(name2)) {
            return this.proxyState.getRow$realm().getIndex() == org_matrix_androidsdk_crypto_cryptostore_db_model_cryptometadataentityrealmproxy.proxyState.getRow$realm().getIndex();
        }
        return false;
    }
}
