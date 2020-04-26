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
import org.matrix.androidsdk.crypto.cryptostore.db.model.DeviceInfoEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.DeviceInfoEntityFields;
import org.matrix.androidsdk.crypto.cryptostore.db.model.UserEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.UserEntityFields.DEVICES;

public class org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy extends DeviceInfoEntity implements RealmObjectProxy, org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface {
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();
    private DeviceInfoEntityColumnInfo columnInfo;
    private ProxyState<DeviceInfoEntity> proxyState;
    private RealmResults<UserEntity> usersBacklinks;

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "DeviceInfoEntity";
    }

    static final class DeviceInfoEntityColumnInfo extends ColumnInfo {
        long deviceIdIndex;
        long deviceInfoDataIndex;
        long identityKeyIndex;
        long maxColumnIndexValue;
        long primaryKeyIndex;

        DeviceInfoEntityColumnInfo(OsSchemaInfo osSchemaInfo) {
            super(4);
            OsObjectSchemaInfo objectSchemaInfo = osSchemaInfo.getObjectSchemaInfo(ClassNameHelper.INTERNAL_CLASS_NAME);
            String str = "primaryKey";
            this.primaryKeyIndex = addColumnDetails(str, str, objectSchemaInfo);
            String str2 = "deviceId";
            this.deviceIdIndex = addColumnDetails(str2, str2, objectSchemaInfo);
            String str3 = DeviceInfoEntityFields.IDENTITY_KEY;
            this.identityKeyIndex = addColumnDetails(str3, str3, objectSchemaInfo);
            String str4 = DeviceInfoEntityFields.DEVICE_INFO_DATA;
            this.deviceInfoDataIndex = addColumnDetails(str4, str4, objectSchemaInfo);
            addBacklinkDetails(osSchemaInfo, DeviceInfoEntityFields.USERS, io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy.ClassNameHelper.INTERNAL_CLASS_NAME, DEVICES.$);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        DeviceInfoEntityColumnInfo(ColumnInfo columnInfo, boolean z) {
            super(columnInfo, z);
            copy(columnInfo, this);
        }

        /* access modifiers changed from: protected */
        public final ColumnInfo copy(boolean z) {
            return new DeviceInfoEntityColumnInfo(this, z);
        }

        /* access modifiers changed from: protected */
        public final void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2) {
            DeviceInfoEntityColumnInfo deviceInfoEntityColumnInfo = (DeviceInfoEntityColumnInfo) columnInfo;
            DeviceInfoEntityColumnInfo deviceInfoEntityColumnInfo2 = (DeviceInfoEntityColumnInfo) columnInfo2;
            deviceInfoEntityColumnInfo2.primaryKeyIndex = deviceInfoEntityColumnInfo.primaryKeyIndex;
            deviceInfoEntityColumnInfo2.deviceIdIndex = deviceInfoEntityColumnInfo.deviceIdIndex;
            deviceInfoEntityColumnInfo2.identityKeyIndex = deviceInfoEntityColumnInfo.identityKeyIndex;
            deviceInfoEntityColumnInfo2.deviceInfoDataIndex = deviceInfoEntityColumnInfo.deviceInfoDataIndex;
            deviceInfoEntityColumnInfo2.maxColumnIndexValue = deviceInfoEntityColumnInfo.maxColumnIndexValue;
        }
    }

    public static String getSimpleClassName() {
        return ClassNameHelper.INTERNAL_CLASS_NAME;
    }

    org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy() {
        this.proxyState.setConstructionFinished();
    }

    public void realm$injectObjectContext() {
        if (this.proxyState == null) {
            RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
            this.columnInfo = (DeviceInfoEntityColumnInfo) realmObjectContext.getColumnInfo();
            this.proxyState = new ProxyState<>(this);
            this.proxyState.setRealm$realm(realmObjectContext.getRealm());
            this.proxyState.setRow$realm(realmObjectContext.getRow());
            this.proxyState.setAcceptDefaultValue$realm(realmObjectContext.getAcceptDefaultValue());
            this.proxyState.setExcludeFields$realm(realmObjectContext.getExcludeFields());
        }
    }

    public String realmGet$primaryKey() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.primaryKeyIndex);
    }

    public void realmSet$primaryKey(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            throw new RealmException("Primary key field 'primaryKey' cannot be changed after object was created.");
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

    public String realmGet$identityKey() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.identityKeyIndex);
    }

    public void realmSet$identityKey(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.identityKeyIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.identityKeyIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.identityKeyIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.identityKeyIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public String realmGet$deviceInfoData() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.deviceInfoDataIndex);
    }

    public void realmSet$deviceInfoData(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.deviceInfoDataIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.deviceInfoDataIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.deviceInfoDataIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.deviceInfoDataIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public RealmResults<UserEntity> realmGet$users() {
        BaseRealm realm$realm = this.proxyState.getRealm$realm();
        realm$realm.checkIfValid();
        this.proxyState.getRow$realm().checkIfAttached();
        if (this.usersBacklinks == null) {
            this.usersBacklinks = RealmResults.createBacklinkResults(realm$realm, this.proxyState.getRow$realm(), UserEntity.class, DEVICES.$);
        }
        return this.usersBacklinks;
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        Builder builder = new Builder(ClassNameHelper.INTERNAL_CLASS_NAME, 4, 1);
        Builder builder2 = builder;
        builder2.addPersistedProperty("primaryKey", RealmFieldType.STRING, true, true, true);
        builder2.addPersistedProperty("deviceId", RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty(DeviceInfoEntityFields.IDENTITY_KEY, RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty(DeviceInfoEntityFields.DEVICE_INFO_DATA, RealmFieldType.STRING, false, false, false);
        builder.addComputedLinkProperty(DeviceInfoEntityFields.USERS, io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy.ClassNameHelper.INTERNAL_CLASS_NAME, DEVICES.$);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static DeviceInfoEntityColumnInfo createColumnInfo(OsSchemaInfo osSchemaInfo) {
        return new DeviceInfoEntityColumnInfo(osSchemaInfo);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0064  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00ce  */
    public static DeviceInfoEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject jSONObject, boolean z) throws JSONException {
        org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy;
        String str;
        String str2;
        String str3;
        List emptyList = Collections.emptyList();
        String str4 = "primaryKey";
        if (z) {
            Table table = realm.getTable(DeviceInfoEntity.class);
            long findFirstString = !jSONObject.isNull(str4) ? table.findFirstString(((DeviceInfoEntityColumnInfo) realm.getSchema().getColumnInfo(DeviceInfoEntity.class)).primaryKeyIndex, jSONObject.getString(str4)) : -1;
            if (findFirstString != -1) {
                RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
                try {
                    realmObjectContext.set(realm, table.getUncheckedRow(findFirstString), realm.getSchema().getColumnInfo(DeviceInfoEntity.class), false, Collections.emptyList());
                    org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy();
                    if (org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy == null) {
                        if (!jSONObject.has(str4)) {
                            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'primaryKey'.");
                        } else if (jSONObject.isNull(str4)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy) realm.createObjectInternal(DeviceInfoEntity.class, null, true, emptyList);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy) realm.createObjectInternal(DeviceInfoEntity.class, jSONObject.getString(str4), true, emptyList);
                        }
                    }
                    org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy;
                    str = "deviceId";
                    if (jSONObject.has(str)) {
                        if (jSONObject.isNull(str)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$deviceId(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$deviceId(jSONObject.getString(str));
                        }
                    }
                    str2 = DeviceInfoEntityFields.IDENTITY_KEY;
                    if (jSONObject.has(str2)) {
                        if (jSONObject.isNull(str2)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$identityKey(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$identityKey(jSONObject.getString(str2));
                        }
                    }
                    str3 = DeviceInfoEntityFields.DEVICE_INFO_DATA;
                    if (jSONObject.has(str3)) {
                        if (jSONObject.isNull(str3)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$deviceInfoData(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$deviceInfoData(jSONObject.getString(str3));
                        }
                    }
                    return org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy;
                } finally {
                    realmObjectContext.clear();
                }
            }
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy = null;
        if (org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy == null) {
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface2 = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy;
        str = "deviceId";
        if (jSONObject.has(str)) {
        }
        str2 = DeviceInfoEntityFields.IDENTITY_KEY;
        if (jSONObject.has(str2)) {
        }
        str3 = DeviceInfoEntityFields.DEVICE_INFO_DATA;
        if (jSONObject.has(str3)) {
        }
        return org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy;
    }

    public static DeviceInfoEntity createUsingJsonStream(Realm realm, JsonReader jsonReader) throws IOException {
        DeviceInfoEntity deviceInfoEntity = new DeviceInfoEntity();
        org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface = deviceInfoEntity;
        jsonReader.beginObject();
        boolean z = false;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if (nextName.equals("primaryKey")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$primaryKey(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$primaryKey(null);
                }
                z = true;
            } else if (nextName.equals("deviceId")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$deviceId(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$deviceId(null);
                }
            } else if (nextName.equals(DeviceInfoEntityFields.IDENTITY_KEY)) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$identityKey(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$identityKey(null);
                }
            } else if (!nextName.equals(DeviceInfoEntityFields.DEVICE_INFO_DATA)) {
                jsonReader.skipValue();
            } else if (jsonReader.peek() != JsonToken.NULL) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$deviceInfoData(jsonReader.nextString());
            } else {
                jsonReader.skipValue();
                org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$deviceInfoData(null);
            }
        }
        jsonReader.endObject();
        if (z) {
            return (DeviceInfoEntity) realm.copyToRealm(deviceInfoEntity, new ImportFlag[0]);
        }
        throw new IllegalArgumentException("JSON object doesn't have the primary key field 'primaryKey'.");
    }

    private static org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy newProxyInstance(BaseRealm baseRealm, Row row) {
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        realmObjectContext.set(baseRealm, row, baseRealm.getSchema().getColumnInfo(DeviceInfoEntity.class), false, Collections.emptyList());
        org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy();
        realmObjectContext.clear();
        return org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x009a  */
    public static DeviceInfoEntity copyOrUpdate(Realm realm, DeviceInfoEntityColumnInfo deviceInfoEntityColumnInfo, DeviceInfoEntity deviceInfoEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        boolean z2;
        if (deviceInfoEntity instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) deviceInfoEntity;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null) {
                BaseRealm realm$realm = realmObjectProxy.realmGet$proxyState().getRealm$realm();
                if (realm$realm.threadId != realm.threadId) {
                    throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
                } else if (realm$realm.getPath().equals(realm.getPath())) {
                    return deviceInfoEntity;
                }
            }
        }
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        RealmObjectProxy realmObjectProxy2 = (RealmObjectProxy) map.get(deviceInfoEntity);
        if (realmObjectProxy2 != null) {
            return (DeviceInfoEntity) realmObjectProxy2;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy = null;
        if (z) {
            Table table = realm.getTable(DeviceInfoEntity.class);
            long findFirstString = table.findFirstString(deviceInfoEntityColumnInfo.primaryKeyIndex, deviceInfoEntity.realmGet$primaryKey());
            if (findFirstString == -1) {
                z2 = false;
                return !z2 ? update(realm, deviceInfoEntityColumnInfo, org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy, deviceInfoEntity, map, set) : copy(realm, deviceInfoEntityColumnInfo, deviceInfoEntity, z, map, set);
            }
            try {
                realmObjectContext.set(realm, table.getUncheckedRow(findFirstString), deviceInfoEntityColumnInfo, false, Collections.emptyList());
                org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy();
                map.put(deviceInfoEntity, org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy);
            } finally {
                realmObjectContext.clear();
            }
        }
        z2 = z;
        return !z2 ? update(realm, deviceInfoEntityColumnInfo, org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy, deviceInfoEntity, map, set) : copy(realm, deviceInfoEntityColumnInfo, deviceInfoEntity, z, map, set);
    }

    public static DeviceInfoEntity copy(Realm realm, DeviceInfoEntityColumnInfo deviceInfoEntityColumnInfo, DeviceInfoEntity deviceInfoEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        RealmObjectProxy realmObjectProxy = (RealmObjectProxy) map.get(deviceInfoEntity);
        if (realmObjectProxy != null) {
            return (DeviceInfoEntity) realmObjectProxy;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface = deviceInfoEntity;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(DeviceInfoEntity.class), deviceInfoEntityColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addString(deviceInfoEntityColumnInfo.primaryKeyIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$primaryKey());
        osObjectBuilder.addString(deviceInfoEntityColumnInfo.deviceIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$deviceId());
        osObjectBuilder.addString(deviceInfoEntityColumnInfo.identityKeyIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$identityKey());
        osObjectBuilder.addString(deviceInfoEntityColumnInfo.deviceInfoDataIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$deviceInfoData());
        org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy newProxyInstance = newProxyInstance(realm, osObjectBuilder.createNewObject());
        map.put(deviceInfoEntity, newProxyInstance);
        return newProxyInstance;
    }

    public static long insert(Realm realm, DeviceInfoEntity deviceInfoEntity, Map<RealmModel, Long> map) {
        long j;
        DeviceInfoEntity deviceInfoEntity2 = deviceInfoEntity;
        if (deviceInfoEntity2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) deviceInfoEntity2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(DeviceInfoEntity.class);
        long nativePtr = table.getNativePtr();
        DeviceInfoEntityColumnInfo deviceInfoEntityColumnInfo = (DeviceInfoEntityColumnInfo) realm.getSchema().getColumnInfo(DeviceInfoEntity.class);
        long j2 = deviceInfoEntityColumnInfo.primaryKeyIndex;
        org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface = deviceInfoEntity2;
        String realmGet$primaryKey = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$primaryKey();
        long nativeFindFirstString = realmGet$primaryKey != null ? Table.nativeFindFirstString(nativePtr, j2, realmGet$primaryKey) : -1;
        if (nativeFindFirstString == -1) {
            j = OsObject.createRowWithPrimaryKey(table, j2, realmGet$primaryKey);
        } else {
            Table.throwDuplicatePrimaryKeyException(realmGet$primaryKey);
            j = nativeFindFirstString;
        }
        map.put(deviceInfoEntity2, Long.valueOf(j));
        String realmGet$deviceId = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$deviceId();
        if (realmGet$deviceId != null) {
            Table.nativeSetString(nativePtr, deviceInfoEntityColumnInfo.deviceIdIndex, j, realmGet$deviceId, false);
        }
        String realmGet$identityKey = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$identityKey();
        if (realmGet$identityKey != null) {
            Table.nativeSetString(nativePtr, deviceInfoEntityColumnInfo.identityKeyIndex, j, realmGet$identityKey, false);
        }
        String realmGet$deviceInfoData = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$deviceInfoData();
        if (realmGet$deviceInfoData != null) {
            Table.nativeSetString(nativePtr, deviceInfoEntityColumnInfo.deviceInfoDataIndex, j, realmGet$deviceInfoData, false);
        }
        return j;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(DeviceInfoEntity.class);
        long nativePtr = table.getNativePtr();
        DeviceInfoEntityColumnInfo deviceInfoEntityColumnInfo = (DeviceInfoEntityColumnInfo) realm.getSchema().getColumnInfo(DeviceInfoEntity.class);
        long j2 = deviceInfoEntityColumnInfo.primaryKeyIndex;
        while (it.hasNext()) {
            DeviceInfoEntity deviceInfoEntity = (DeviceInfoEntity) it.next();
            if (!map2.containsKey(deviceInfoEntity)) {
                if (deviceInfoEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) deviceInfoEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(deviceInfoEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface = deviceInfoEntity;
                String realmGet$primaryKey = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$primaryKey();
                long nativeFindFirstString = realmGet$primaryKey != null ? Table.nativeFindFirstString(nativePtr, j2, realmGet$primaryKey) : -1;
                if (nativeFindFirstString == -1) {
                    j = OsObject.createRowWithPrimaryKey(table, j2, realmGet$primaryKey);
                } else {
                    Table.throwDuplicatePrimaryKeyException(realmGet$primaryKey);
                    j = nativeFindFirstString;
                }
                map2.put(deviceInfoEntity, Long.valueOf(j));
                String realmGet$deviceId = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$deviceId();
                if (realmGet$deviceId != null) {
                    Table.nativeSetString(nativePtr, deviceInfoEntityColumnInfo.deviceIdIndex, j, realmGet$deviceId, false);
                }
                String realmGet$identityKey = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$identityKey();
                if (realmGet$identityKey != null) {
                    Table.nativeSetString(nativePtr, deviceInfoEntityColumnInfo.identityKeyIndex, j, realmGet$identityKey, false);
                }
                String realmGet$deviceInfoData = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$deviceInfoData();
                if (realmGet$deviceInfoData != null) {
                    Table.nativeSetString(nativePtr, deviceInfoEntityColumnInfo.deviceInfoDataIndex, j, realmGet$deviceInfoData, false);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, DeviceInfoEntity deviceInfoEntity, Map<RealmModel, Long> map) {
        DeviceInfoEntity deviceInfoEntity2 = deviceInfoEntity;
        if (deviceInfoEntity2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) deviceInfoEntity2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(DeviceInfoEntity.class);
        long nativePtr = table.getNativePtr();
        DeviceInfoEntityColumnInfo deviceInfoEntityColumnInfo = (DeviceInfoEntityColumnInfo) realm.getSchema().getColumnInfo(DeviceInfoEntity.class);
        long j = deviceInfoEntityColumnInfo.primaryKeyIndex;
        org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface = deviceInfoEntity2;
        String realmGet$primaryKey = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$primaryKey();
        long nativeFindFirstString = realmGet$primaryKey != null ? Table.nativeFindFirstString(nativePtr, j, realmGet$primaryKey) : -1;
        long createRowWithPrimaryKey = nativeFindFirstString == -1 ? OsObject.createRowWithPrimaryKey(table, j, realmGet$primaryKey) : nativeFindFirstString;
        map.put(deviceInfoEntity2, Long.valueOf(createRowWithPrimaryKey));
        String realmGet$deviceId = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$deviceId();
        if (realmGet$deviceId != null) {
            Table.nativeSetString(nativePtr, deviceInfoEntityColumnInfo.deviceIdIndex, createRowWithPrimaryKey, realmGet$deviceId, false);
        } else {
            Table.nativeSetNull(nativePtr, deviceInfoEntityColumnInfo.deviceIdIndex, createRowWithPrimaryKey, false);
        }
        String realmGet$identityKey = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$identityKey();
        if (realmGet$identityKey != null) {
            Table.nativeSetString(nativePtr, deviceInfoEntityColumnInfo.identityKeyIndex, createRowWithPrimaryKey, realmGet$identityKey, false);
        } else {
            Table.nativeSetNull(nativePtr, deviceInfoEntityColumnInfo.identityKeyIndex, createRowWithPrimaryKey, false);
        }
        String realmGet$deviceInfoData = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$deviceInfoData();
        if (realmGet$deviceInfoData != null) {
            Table.nativeSetString(nativePtr, deviceInfoEntityColumnInfo.deviceInfoDataIndex, createRowWithPrimaryKey, realmGet$deviceInfoData, false);
        } else {
            Table.nativeSetNull(nativePtr, deviceInfoEntityColumnInfo.deviceInfoDataIndex, createRowWithPrimaryKey, false);
        }
        return createRowWithPrimaryKey;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(DeviceInfoEntity.class);
        long nativePtr = table.getNativePtr();
        DeviceInfoEntityColumnInfo deviceInfoEntityColumnInfo = (DeviceInfoEntityColumnInfo) realm.getSchema().getColumnInfo(DeviceInfoEntity.class);
        long j = deviceInfoEntityColumnInfo.primaryKeyIndex;
        while (it.hasNext()) {
            DeviceInfoEntity deviceInfoEntity = (DeviceInfoEntity) it.next();
            if (!map2.containsKey(deviceInfoEntity)) {
                if (deviceInfoEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) deviceInfoEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(deviceInfoEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface = deviceInfoEntity;
                String realmGet$primaryKey = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$primaryKey();
                long nativeFindFirstString = realmGet$primaryKey != null ? Table.nativeFindFirstString(nativePtr, j, realmGet$primaryKey) : -1;
                long createRowWithPrimaryKey = nativeFindFirstString == -1 ? OsObject.createRowWithPrimaryKey(table, j, realmGet$primaryKey) : nativeFindFirstString;
                map2.put(deviceInfoEntity, Long.valueOf(createRowWithPrimaryKey));
                String realmGet$deviceId = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$deviceId();
                if (realmGet$deviceId != null) {
                    Table.nativeSetString(nativePtr, deviceInfoEntityColumnInfo.deviceIdIndex, createRowWithPrimaryKey, realmGet$deviceId, false);
                } else {
                    Table.nativeSetNull(nativePtr, deviceInfoEntityColumnInfo.deviceIdIndex, createRowWithPrimaryKey, false);
                }
                String realmGet$identityKey = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$identityKey();
                if (realmGet$identityKey != null) {
                    Table.nativeSetString(nativePtr, deviceInfoEntityColumnInfo.identityKeyIndex, createRowWithPrimaryKey, realmGet$identityKey, false);
                } else {
                    Table.nativeSetNull(nativePtr, deviceInfoEntityColumnInfo.identityKeyIndex, createRowWithPrimaryKey, false);
                }
                String realmGet$deviceInfoData = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmGet$deviceInfoData();
                if (realmGet$deviceInfoData != null) {
                    Table.nativeSetString(nativePtr, deviceInfoEntityColumnInfo.deviceInfoDataIndex, createRowWithPrimaryKey, realmGet$deviceInfoData, false);
                } else {
                    Table.nativeSetNull(nativePtr, deviceInfoEntityColumnInfo.deviceInfoDataIndex, createRowWithPrimaryKey, false);
                }
            }
        }
    }

    public static DeviceInfoEntity createDetachedCopy(DeviceInfoEntity deviceInfoEntity, int i, int i2, Map<RealmModel, CacheData<RealmModel>> map) {
        DeviceInfoEntity deviceInfoEntity2;
        if (i > i2 || deviceInfoEntity == null) {
            return null;
        }
        CacheData cacheData = (CacheData) map.get(deviceInfoEntity);
        if (cacheData == null) {
            deviceInfoEntity2 = new DeviceInfoEntity();
            map.put(deviceInfoEntity, new CacheData(i, deviceInfoEntity2));
        } else if (i >= cacheData.minDepth) {
            return (DeviceInfoEntity) cacheData.object;
        } else {
            DeviceInfoEntity deviceInfoEntity3 = (DeviceInfoEntity) cacheData.object;
            cacheData.minDepth = i;
            deviceInfoEntity2 = deviceInfoEntity3;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface = deviceInfoEntity2;
        org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface2 = deviceInfoEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$primaryKey(org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface2.realmGet$primaryKey());
        org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$deviceId(org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface2.realmGet$deviceId());
        org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$identityKey(org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface2.realmGet$identityKey());
        org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface.realmSet$deviceInfoData(org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface2.realmGet$deviceInfoData());
        return deviceInfoEntity2;
    }

    static DeviceInfoEntity update(Realm realm, DeviceInfoEntityColumnInfo deviceInfoEntityColumnInfo, DeviceInfoEntity deviceInfoEntity, DeviceInfoEntity deviceInfoEntity2, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface = deviceInfoEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface2 = deviceInfoEntity2;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(DeviceInfoEntity.class), deviceInfoEntityColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addString(deviceInfoEntityColumnInfo.primaryKeyIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface2.realmGet$primaryKey());
        osObjectBuilder.addString(deviceInfoEntityColumnInfo.deviceIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface2.realmGet$deviceId());
        osObjectBuilder.addString(deviceInfoEntityColumnInfo.identityKeyIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface2.realmGet$identityKey());
        osObjectBuilder.addString(deviceInfoEntityColumnInfo.deviceInfoDataIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxyinterface2.realmGet$deviceInfoData());
        osObjectBuilder.updateExistingObject();
        return deviceInfoEntity;
    }

    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder sb = new StringBuilder("DeviceInfoEntity = proxy[");
        sb.append("{primaryKey:");
        sb.append(realmGet$primaryKey());
        String str = "}";
        sb.append(str);
        String str2 = ",";
        sb.append(str2);
        sb.append("{deviceId:");
        String str3 = "null";
        sb.append(realmGet$deviceId() != null ? realmGet$deviceId() : str3);
        sb.append(str);
        sb.append(str2);
        sb.append("{identityKey:");
        sb.append(realmGet$identityKey() != null ? realmGet$identityKey() : str3);
        sb.append(str);
        sb.append(str2);
        sb.append("{deviceInfoData:");
        if (realmGet$deviceInfoData() != null) {
            str3 = realmGet$deviceInfoData();
        }
        sb.append(str3);
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
        org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy) obj;
        String path = this.proxyState.getRealm$realm().getPath();
        String path2 = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy.proxyState.getRealm$realm().getPath();
        if (path == null ? path2 != null : !path.equals(path2)) {
            return false;
        }
        String name = this.proxyState.getRow$realm().getTable().getName();
        String name2 = org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy.proxyState.getRow$realm().getTable().getName();
        if (name == null ? name2 == null : name.equals(name2)) {
            return this.proxyState.getRow$realm().getIndex() == org_matrix_androidsdk_crypto_cryptostore_db_model_deviceinfoentityrealmproxy.proxyState.getRow$realm().getIndex();
        }
        return false;
    }
}
