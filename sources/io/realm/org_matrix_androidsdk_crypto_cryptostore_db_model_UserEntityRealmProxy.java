package io.realm;

import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.BaseRealm.RealmObjectContext;
import io.realm.exceptions.RealmException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsList;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.matrix.androidsdk.crypto.cryptostore.db.model.DeviceInfoEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.UserEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.UserEntityFields;
import org.matrix.androidsdk.crypto.cryptostore.db.model.UserEntityFields.DEVICES;

public class org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy extends UserEntity implements RealmObjectProxy, org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxyInterface {
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();
    private UserEntityColumnInfo columnInfo;
    private RealmList<DeviceInfoEntity> devicesRealmList;
    private ProxyState<UserEntity> proxyState;

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "UserEntity";
    }

    static final class UserEntityColumnInfo extends ColumnInfo {
        long deviceTrackingStatusIndex;
        long devicesIndex;
        long maxColumnIndexValue;
        long userIdIndex;

        UserEntityColumnInfo(OsSchemaInfo osSchemaInfo) {
            super(3);
            OsObjectSchemaInfo objectSchemaInfo = osSchemaInfo.getObjectSchemaInfo(ClassNameHelper.INTERNAL_CLASS_NAME);
            String str = "userId";
            this.userIdIndex = addColumnDetails(str, str, objectSchemaInfo);
            String str2 = DEVICES.$;
            this.devicesIndex = addColumnDetails(str2, str2, objectSchemaInfo);
            String str3 = UserEntityFields.DEVICE_TRACKING_STATUS;
            this.deviceTrackingStatusIndex = addColumnDetails(str3, str3, objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        UserEntityColumnInfo(ColumnInfo columnInfo, boolean z) {
            super(columnInfo, z);
            copy(columnInfo, this);
        }

        /* access modifiers changed from: protected */
        public final ColumnInfo copy(boolean z) {
            return new UserEntityColumnInfo(this, z);
        }

        /* access modifiers changed from: protected */
        public final void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2) {
            UserEntityColumnInfo userEntityColumnInfo = (UserEntityColumnInfo) columnInfo;
            UserEntityColumnInfo userEntityColumnInfo2 = (UserEntityColumnInfo) columnInfo2;
            userEntityColumnInfo2.userIdIndex = userEntityColumnInfo.userIdIndex;
            userEntityColumnInfo2.devicesIndex = userEntityColumnInfo.devicesIndex;
            userEntityColumnInfo2.deviceTrackingStatusIndex = userEntityColumnInfo.deviceTrackingStatusIndex;
            userEntityColumnInfo2.maxColumnIndexValue = userEntityColumnInfo.maxColumnIndexValue;
        }
    }

    public static String getSimpleClassName() {
        return ClassNameHelper.INTERNAL_CLASS_NAME;
    }

    org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy() {
        this.proxyState.setConstructionFinished();
    }

    public void realm$injectObjectContext() {
        if (this.proxyState == null) {
            RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
            this.columnInfo = (UserEntityColumnInfo) realmObjectContext.getColumnInfo();
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

    public RealmList<DeviceInfoEntity> realmGet$devices() {
        this.proxyState.getRealm$realm().checkIfValid();
        RealmList<DeviceInfoEntity> realmList = this.devicesRealmList;
        if (realmList != null) {
            return realmList;
        }
        this.devicesRealmList = new RealmList<>(DeviceInfoEntity.class, this.proxyState.getRow$realm().getModelList(this.columnInfo.devicesIndex), this.proxyState.getRealm$realm());
        return this.devicesRealmList;
    }

    public void realmSet$devices(RealmList<DeviceInfoEntity> realmList) {
        int i = 0;
        if (this.proxyState.isUnderConstruction()) {
            if (!this.proxyState.getAcceptDefaultValue$realm() || this.proxyState.getExcludeFields$realm().contains(DEVICES.$)) {
                return;
            }
            if (realmList != null && !realmList.isManaged()) {
                Realm realm = (Realm) this.proxyState.getRealm$realm();
                RealmList<DeviceInfoEntity> realmList2 = new RealmList<>();
                Iterator it = realmList.iterator();
                while (it.hasNext()) {
                    DeviceInfoEntity deviceInfoEntity = (DeviceInfoEntity) it.next();
                    if (deviceInfoEntity == null || RealmObject.isManaged(deviceInfoEntity)) {
                        realmList2.add(deviceInfoEntity);
                    } else {
                        realmList2.add(realm.copyToRealm(deviceInfoEntity, new ImportFlag[0]));
                    }
                }
                realmList = realmList2;
            }
        }
        this.proxyState.getRealm$realm().checkIfValid();
        OsList modelList = this.proxyState.getRow$realm().getModelList(this.columnInfo.devicesIndex);
        if (realmList == null || ((long) realmList.size()) != modelList.size()) {
            modelList.removeAll();
            if (realmList != null) {
                int size = realmList.size();
                while (i < size) {
                    DeviceInfoEntity deviceInfoEntity2 = (DeviceInfoEntity) realmList.get(i);
                    this.proxyState.checkValidObject(deviceInfoEntity2);
                    modelList.addRow(((RealmObjectProxy) deviceInfoEntity2).realmGet$proxyState().getRow$realm().getIndex());
                    i++;
                }
            }
        } else {
            int size2 = realmList.size();
            while (i < size2) {
                DeviceInfoEntity deviceInfoEntity3 = (DeviceInfoEntity) realmList.get(i);
                this.proxyState.checkValidObject(deviceInfoEntity3);
                modelList.setRow((long) i, ((RealmObjectProxy) deviceInfoEntity3).realmGet$proxyState().getRow$realm().getIndex());
                i++;
            }
        }
    }

    public int realmGet$deviceTrackingStatus() {
        this.proxyState.getRealm$realm().checkIfValid();
        return (int) this.proxyState.getRow$realm().getLong(this.columnInfo.deviceTrackingStatusIndex);
    }

    public void realmSet$deviceTrackingStatus(int i) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            this.proxyState.getRow$realm().setLong(this.columnInfo.deviceTrackingStatusIndex, (long) i);
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            row$realm.getTable().setLong(this.columnInfo.deviceTrackingStatusIndex, row$realm.getIndex(), (long) i, true);
        }
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        Builder builder = new Builder(ClassNameHelper.INTERNAL_CLASS_NAME, 3, 0);
        Builder builder2 = builder;
        builder2.addPersistedProperty("userId", RealmFieldType.STRING, true, true, false);
        builder.addPersistedLinkProperty(DEVICES.$, RealmFieldType.LIST, io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.ClassNameHelper.INTERNAL_CLASS_NAME);
        Builder builder3 = builder;
        builder3.addPersistedProperty(UserEntityFields.DEVICE_TRACKING_STATUS, RealmFieldType.INTEGER, false, false, true);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static UserEntityColumnInfo createColumnInfo(OsSchemaInfo osSchemaInfo) {
        return new UserEntityColumnInfo(osSchemaInfo);
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00ac  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00e2  */
    public static UserEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject jSONObject, boolean z) throws JSONException {
        org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy;
        String str;
        String str2;
        long j;
        Realm realm2 = realm;
        JSONObject jSONObject2 = jSONObject;
        boolean z2 = z;
        ArrayList arrayList = new ArrayList(1);
        String str3 = "userId";
        if (z2) {
            Table table = realm.getTable(UserEntity.class);
            long j2 = ((UserEntityColumnInfo) realm.getSchema().getColumnInfo(UserEntity.class)).userIdIndex;
            if (jSONObject.isNull(str3)) {
                j = table.findFirstNull(j2);
            } else {
                j = table.findFirstString(j2, jSONObject.getString(str3));
            }
            if (j != -1) {
                RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
                try {
                    realmObjectContext.set(realm, table.getUncheckedRow(j), realm.getSchema().getColumnInfo(UserEntity.class), false, Collections.emptyList());
                    org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy();
                    str = DEVICES.$;
                    if (org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy == null) {
                        if (jSONObject.has(str)) {
                            arrayList.add(str);
                        }
                        if (!jSONObject.has(str3)) {
                            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'userId'.");
                        } else if (jSONObject.isNull(str3)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy) realm.createObjectInternal(UserEntity.class, null, true, arrayList);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy) realm.createObjectInternal(UserEntity.class, jSONObject.getString(str3), true, arrayList);
                        }
                    }
                    org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface = org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy;
                    if (jSONObject.has(str)) {
                        if (jSONObject.isNull(str)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmSet$devices(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$devices().clear();
                            JSONArray jSONArray = jSONObject.getJSONArray(str);
                            for (int i = 0; i < jSONArray.length(); i++) {
                                org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$devices().add(org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.createOrUpdateUsingJsonObject(realm, jSONArray.getJSONObject(i), z2));
                            }
                        }
                    }
                    str2 = UserEntityFields.DEVICE_TRACKING_STATUS;
                    if (jSONObject.has(str2)) {
                        if (!jSONObject.isNull(str2)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmSet$deviceTrackingStatus(jSONObject.getInt(str2));
                        } else {
                            throw new IllegalArgumentException("Trying to set non-nullable field 'deviceTrackingStatus' to null.");
                        }
                    }
                    return org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy;
                } finally {
                    realmObjectContext.clear();
                }
            }
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy = null;
        str = DEVICES.$;
        if (org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy == null) {
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface2 = org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy;
        if (jSONObject.has(str)) {
        }
        str2 = UserEntityFields.DEVICE_TRACKING_STATUS;
        if (jSONObject.has(str2)) {
        }
        return org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy;
    }

    public static UserEntity createUsingJsonStream(Realm realm, JsonReader jsonReader) throws IOException {
        UserEntity userEntity = new UserEntity();
        org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface = userEntity;
        jsonReader.beginObject();
        boolean z = false;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if (nextName.equals("userId")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmSet$userId(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmSet$userId(null);
                }
                z = true;
            } else if (nextName.equals(DEVICES.$)) {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmSet$devices(null);
                } else {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmSet$devices(new RealmList());
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$devices().add(org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.createUsingJsonStream(realm, jsonReader));
                    }
                    jsonReader.endArray();
                }
            } else if (!nextName.equals(UserEntityFields.DEVICE_TRACKING_STATUS)) {
                jsonReader.skipValue();
            } else if (jsonReader.peek() != JsonToken.NULL) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmSet$deviceTrackingStatus(jsonReader.nextInt());
            } else {
                jsonReader.skipValue();
                throw new IllegalArgumentException("Trying to set non-nullable field 'deviceTrackingStatus' to null.");
            }
        }
        jsonReader.endObject();
        if (z) {
            return (UserEntity) realm.copyToRealm(userEntity, new ImportFlag[0]);
        }
        throw new IllegalArgumentException("JSON object doesn't have the primary key field 'userId'.");
    }

    private static org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy newProxyInstance(BaseRealm baseRealm, Row row) {
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        realmObjectContext.set(baseRealm, row, baseRealm.getSchema().getColumnInfo(UserEntity.class), false, Collections.emptyList());
        org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy();
        realmObjectContext.clear();
        return org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a1  */
    public static UserEntity copyOrUpdate(Realm realm, UserEntityColumnInfo userEntityColumnInfo, UserEntity userEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        boolean z2;
        long j;
        if (userEntity instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) userEntity;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null) {
                BaseRealm realm$realm = realmObjectProxy.realmGet$proxyState().getRealm$realm();
                if (realm$realm.threadId != realm.threadId) {
                    throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
                } else if (realm$realm.getPath().equals(realm.getPath())) {
                    return userEntity;
                }
            }
        }
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        RealmObjectProxy realmObjectProxy2 = (RealmObjectProxy) map.get(userEntity);
        if (realmObjectProxy2 != null) {
            return (UserEntity) realmObjectProxy2;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy = null;
        if (z) {
            Table table = realm.getTable(UserEntity.class);
            long j2 = userEntityColumnInfo.userIdIndex;
            String realmGet$userId = userEntity.realmGet$userId();
            if (realmGet$userId == null) {
                j = table.findFirstNull(j2);
            } else {
                j = table.findFirstString(j2, realmGet$userId);
            }
            if (j == -1) {
                z2 = false;
                return !z2 ? update(realm, userEntityColumnInfo, org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy, userEntity, map, set) : copy(realm, userEntityColumnInfo, userEntity, z, map, set);
            }
            try {
                realmObjectContext.set(realm, table.getUncheckedRow(j), userEntityColumnInfo, false, Collections.emptyList());
                org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy();
                map.put(userEntity, org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy);
            } finally {
                realmObjectContext.clear();
            }
        }
        z2 = z;
        return !z2 ? update(realm, userEntityColumnInfo, org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy, userEntity, map, set) : copy(realm, userEntityColumnInfo, userEntity, z, map, set);
    }

    public static UserEntity copy(Realm realm, UserEntityColumnInfo userEntityColumnInfo, UserEntity userEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        RealmObjectProxy realmObjectProxy = (RealmObjectProxy) map.get(userEntity);
        if (realmObjectProxy != null) {
            return (UserEntity) realmObjectProxy;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface = userEntity;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(UserEntity.class), userEntityColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addString(userEntityColumnInfo.userIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$userId());
        osObjectBuilder.addInteger(userEntityColumnInfo.deviceTrackingStatusIndex, Integer.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$deviceTrackingStatus()));
        org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy newProxyInstance = newProxyInstance(realm, osObjectBuilder.createNewObject());
        map.put(userEntity, newProxyInstance);
        RealmList realmGet$devices = org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$devices();
        if (realmGet$devices != null) {
            RealmList realmGet$devices2 = newProxyInstance.realmGet$devices();
            realmGet$devices2.clear();
            for (int i = 0; i < realmGet$devices.size(); i++) {
                DeviceInfoEntity deviceInfoEntity = (DeviceInfoEntity) realmGet$devices.get(i);
                DeviceInfoEntity deviceInfoEntity2 = (DeviceInfoEntity) map.get(deviceInfoEntity);
                if (deviceInfoEntity2 != null) {
                    realmGet$devices2.add(deviceInfoEntity2);
                } else {
                    realmGet$devices2.add(org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.copyOrUpdate(realm, (DeviceInfoEntityColumnInfo) realm.getSchema().getColumnInfo(DeviceInfoEntity.class), deviceInfoEntity, z, map, set));
                }
            }
        }
        return newProxyInstance;
    }

    public static long insert(Realm realm, UserEntity userEntity, Map<RealmModel, Long> map) {
        long j;
        long j2;
        if (userEntity instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) userEntity;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(UserEntity.class);
        long nativePtr = table.getNativePtr();
        UserEntityColumnInfo userEntityColumnInfo = (UserEntityColumnInfo) realm.getSchema().getColumnInfo(UserEntity.class);
        long j3 = userEntityColumnInfo.userIdIndex;
        org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface = userEntity;
        String realmGet$userId = org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$userId();
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
        map.put(userEntity, Long.valueOf(j2));
        RealmList realmGet$devices = org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$devices();
        if (realmGet$devices != null) {
            OsList osList = new OsList(table.getUncheckedRow(j2), userEntityColumnInfo.devicesIndex);
            Iterator it = realmGet$devices.iterator();
            while (it.hasNext()) {
                DeviceInfoEntity deviceInfoEntity = (DeviceInfoEntity) it.next();
                Long l = (Long) map.get(deviceInfoEntity);
                if (l == null) {
                    l = Long.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.insert(realm, deviceInfoEntity, map));
                }
                osList.addRow(l.longValue());
            }
        }
        Table.nativeSetLong(nativePtr, userEntityColumnInfo.deviceTrackingStatusIndex, j2, (long) org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$deviceTrackingStatus(), false);
        return j2;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        long j2;
        long j3;
        Realm realm2 = realm;
        Map<RealmModel, Long> map2 = map;
        Table table = realm2.getTable(UserEntity.class);
        long nativePtr = table.getNativePtr();
        UserEntityColumnInfo userEntityColumnInfo = (UserEntityColumnInfo) realm.getSchema().getColumnInfo(UserEntity.class);
        long j4 = userEntityColumnInfo.userIdIndex;
        while (it.hasNext()) {
            UserEntity userEntity = (UserEntity) it.next();
            if (!map2.containsKey(userEntity)) {
                if (userEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) userEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(userEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface = userEntity;
                String realmGet$userId = org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$userId();
                if (realmGet$userId == null) {
                    j = Table.nativeFindFirstNull(nativePtr, j4);
                } else {
                    j = Table.nativeFindFirstString(nativePtr, j4, realmGet$userId);
                }
                if (j == -1) {
                    j2 = OsObject.createRowWithPrimaryKey(table, j4, realmGet$userId);
                } else {
                    Table.throwDuplicatePrimaryKeyException(realmGet$userId);
                    j2 = j;
                }
                map2.put(userEntity, Long.valueOf(j2));
                RealmList realmGet$devices = org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$devices();
                if (realmGet$devices != null) {
                    j3 = j4;
                    OsList osList = new OsList(table.getUncheckedRow(j2), userEntityColumnInfo.devicesIndex);
                    Iterator it2 = realmGet$devices.iterator();
                    while (it2.hasNext()) {
                        DeviceInfoEntity deviceInfoEntity = (DeviceInfoEntity) it2.next();
                        Long l = (Long) map2.get(deviceInfoEntity);
                        if (l == null) {
                            l = Long.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.insert(realm2, deviceInfoEntity, map2));
                        }
                        osList.addRow(l.longValue());
                    }
                } else {
                    j3 = j4;
                }
                Table.nativeSetLong(nativePtr, userEntityColumnInfo.deviceTrackingStatusIndex, j2, (long) org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$deviceTrackingStatus(), false);
                j4 = j3;
            }
        }
    }

    public static long insertOrUpdate(Realm realm, UserEntity userEntity, Map<RealmModel, Long> map) {
        long j;
        long j2;
        Realm realm2 = realm;
        UserEntity userEntity2 = userEntity;
        Map<RealmModel, Long> map2 = map;
        if (userEntity2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) userEntity2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm2.getTable(UserEntity.class);
        long nativePtr = table.getNativePtr();
        UserEntityColumnInfo userEntityColumnInfo = (UserEntityColumnInfo) realm.getSchema().getColumnInfo(UserEntity.class);
        long j3 = userEntityColumnInfo.userIdIndex;
        org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface = userEntity2;
        String realmGet$userId = org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$userId();
        if (realmGet$userId == null) {
            j = Table.nativeFindFirstNull(nativePtr, j3);
        } else {
            j = Table.nativeFindFirstString(nativePtr, j3, realmGet$userId);
        }
        long createRowWithPrimaryKey = j == -1 ? OsObject.createRowWithPrimaryKey(table, j3, realmGet$userId) : j;
        map2.put(userEntity2, Long.valueOf(createRowWithPrimaryKey));
        OsList osList = new OsList(table.getUncheckedRow(createRowWithPrimaryKey), userEntityColumnInfo.devicesIndex);
        RealmList realmGet$devices = org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$devices();
        if (realmGet$devices == null || ((long) realmGet$devices.size()) != osList.size()) {
            j2 = createRowWithPrimaryKey;
            osList.removeAll();
            if (realmGet$devices != null) {
                Iterator it = realmGet$devices.iterator();
                while (it.hasNext()) {
                    DeviceInfoEntity deviceInfoEntity = (DeviceInfoEntity) it.next();
                    Long l = (Long) map2.get(deviceInfoEntity);
                    if (l == null) {
                        l = Long.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.insertOrUpdate(realm2, deviceInfoEntity, map2));
                    }
                    osList.addRow(l.longValue());
                }
            }
        } else {
            int size = realmGet$devices.size();
            int i = 0;
            while (i < size) {
                DeviceInfoEntity deviceInfoEntity2 = (DeviceInfoEntity) realmGet$devices.get(i);
                Long l2 = (Long) map2.get(deviceInfoEntity2);
                if (l2 == null) {
                    l2 = Long.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.insertOrUpdate(realm2, deviceInfoEntity2, map2));
                }
                long j4 = createRowWithPrimaryKey;
                osList.setRow((long) i, l2.longValue());
                i++;
                createRowWithPrimaryKey = j4;
            }
            j2 = createRowWithPrimaryKey;
        }
        Table.nativeSetLong(nativePtr, userEntityColumnInfo.deviceTrackingStatusIndex, j2, (long) org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$deviceTrackingStatus(), false);
        return j2;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        long j2;
        Realm realm2 = realm;
        Map<RealmModel, Long> map2 = map;
        Table table = realm2.getTable(UserEntity.class);
        long nativePtr = table.getNativePtr();
        UserEntityColumnInfo userEntityColumnInfo = (UserEntityColumnInfo) realm.getSchema().getColumnInfo(UserEntity.class);
        long j3 = userEntityColumnInfo.userIdIndex;
        while (it.hasNext()) {
            UserEntity userEntity = (UserEntity) it.next();
            if (!map2.containsKey(userEntity)) {
                if (userEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) userEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(userEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface = userEntity;
                String realmGet$userId = org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$userId();
                if (realmGet$userId == null) {
                    j = Table.nativeFindFirstNull(nativePtr, j3);
                } else {
                    j = Table.nativeFindFirstString(nativePtr, j3, realmGet$userId);
                }
                long createRowWithPrimaryKey = j == -1 ? OsObject.createRowWithPrimaryKey(table, j3, realmGet$userId) : j;
                map2.put(userEntity, Long.valueOf(createRowWithPrimaryKey));
                long j4 = j3;
                OsList osList = new OsList(table.getUncheckedRow(createRowWithPrimaryKey), userEntityColumnInfo.devicesIndex);
                RealmList realmGet$devices = org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$devices();
                if (realmGet$devices == null || ((long) realmGet$devices.size()) != osList.size()) {
                    j2 = createRowWithPrimaryKey;
                    osList.removeAll();
                    if (realmGet$devices != null) {
                        Iterator it2 = realmGet$devices.iterator();
                        while (it2.hasNext()) {
                            DeviceInfoEntity deviceInfoEntity = (DeviceInfoEntity) it2.next();
                            Long l = (Long) map2.get(deviceInfoEntity);
                            if (l == null) {
                                l = Long.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.insertOrUpdate(realm2, deviceInfoEntity, map2));
                            }
                            osList.addRow(l.longValue());
                        }
                    }
                } else {
                    int size = realmGet$devices.size();
                    int i = 0;
                    while (i < size) {
                        DeviceInfoEntity deviceInfoEntity2 = (DeviceInfoEntity) realmGet$devices.get(i);
                        Long l2 = (Long) map2.get(deviceInfoEntity2);
                        if (l2 == null) {
                            l2 = Long.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.insertOrUpdate(realm2, deviceInfoEntity2, map2));
                        }
                        long j5 = createRowWithPrimaryKey;
                        int i2 = size;
                        osList.setRow((long) i, l2.longValue());
                        i++;
                        size = i2;
                        createRowWithPrimaryKey = j5;
                    }
                    j2 = createRowWithPrimaryKey;
                }
                Table.nativeSetLong(nativePtr, userEntityColumnInfo.deviceTrackingStatusIndex, j2, (long) org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmGet$deviceTrackingStatus(), false);
                j3 = j4;
            }
        }
    }

    public static UserEntity createDetachedCopy(UserEntity userEntity, int i, int i2, Map<RealmModel, CacheData<RealmModel>> map) {
        UserEntity userEntity2;
        if (i > i2 || userEntity == null) {
            return null;
        }
        CacheData cacheData = (CacheData) map.get(userEntity);
        if (cacheData == null) {
            userEntity2 = new UserEntity();
            map.put(userEntity, new CacheData(i, userEntity2));
        } else if (i >= cacheData.minDepth) {
            return (UserEntity) cacheData.object;
        } else {
            UserEntity userEntity3 = (UserEntity) cacheData.object;
            cacheData.minDepth = i;
            userEntity2 = userEntity3;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface = userEntity2;
        org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface2 = userEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmSet$userId(org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface2.realmGet$userId());
        if (i == i2) {
            org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmSet$devices(null);
        } else {
            RealmList realmGet$devices = org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface2.realmGet$devices();
            RealmList realmList = new RealmList();
            org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmSet$devices(realmList);
            int i3 = i + 1;
            int size = realmGet$devices.size();
            for (int i4 = 0; i4 < size; i4++) {
                realmList.add(org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.createDetachedCopy((DeviceInfoEntity) realmGet$devices.get(i4), i3, i2, map));
            }
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface.realmSet$deviceTrackingStatus(org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface2.realmGet$deviceTrackingStatus());
        return userEntity2;
    }

    static UserEntity update(Realm realm, UserEntityColumnInfo userEntityColumnInfo, UserEntity userEntity, UserEntity userEntity2, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        UserEntityColumnInfo userEntityColumnInfo2 = userEntityColumnInfo;
        org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface = userEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface2 = userEntity2;
        Realm realm2 = realm;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(UserEntity.class), userEntityColumnInfo2.maxColumnIndexValue, set);
        osObjectBuilder.addString(userEntityColumnInfo2.userIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface2.realmGet$userId());
        RealmList realmGet$devices = org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface2.realmGet$devices();
        if (realmGet$devices != null) {
            RealmList realmList = new RealmList();
            for (int i = 0; i < realmGet$devices.size(); i++) {
                DeviceInfoEntity deviceInfoEntity = (DeviceInfoEntity) realmGet$devices.get(i);
                DeviceInfoEntity deviceInfoEntity2 = (DeviceInfoEntity) map.get(deviceInfoEntity);
                if (deviceInfoEntity2 != null) {
                    realmList.add(deviceInfoEntity2);
                } else {
                    realmList.add(org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxy.copyOrUpdate(realm, (DeviceInfoEntityColumnInfo) realm.getSchema().getColumnInfo(DeviceInfoEntity.class), deviceInfoEntity, true, map, set));
                }
            }
            osObjectBuilder.addObjectList(userEntityColumnInfo2.devicesIndex, realmList);
        } else {
            osObjectBuilder.addObjectList(userEntityColumnInfo2.devicesIndex, new RealmList());
        }
        osObjectBuilder.addInteger(userEntityColumnInfo2.deviceTrackingStatusIndex, Integer.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxyinterface2.realmGet$deviceTrackingStatus()));
        osObjectBuilder.updateExistingObject();
        return userEntity;
    }

    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder sb = new StringBuilder("UserEntity = proxy[");
        sb.append("{userId:");
        sb.append(realmGet$userId() != null ? realmGet$userId() : "null");
        String str = "}";
        sb.append(str);
        String str2 = ",";
        sb.append(str2);
        sb.append("{devices:");
        sb.append("RealmList<DeviceInfoEntity>[");
        sb.append(realmGet$devices().size());
        String str3 = "]";
        sb.append(str3);
        sb.append(str);
        sb.append(str2);
        sb.append("{deviceTrackingStatus:");
        sb.append(realmGet$deviceTrackingStatus());
        sb.append(str);
        sb.append(str3);
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
        org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxy) obj;
        String path = this.proxyState.getRealm$realm().getPath();
        String path2 = org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy.proxyState.getRealm$realm().getPath();
        if (path == null ? path2 != null : !path.equals(path2)) {
            return false;
        }
        String name = this.proxyState.getRow$realm().getTable().getName();
        String name2 = org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy.proxyState.getRow$realm().getTable().getName();
        if (name == null ? name2 == null : name.equals(name2)) {
            return this.proxyState.getRow$realm().getIndex() == org_matrix_androidsdk_crypto_cryptostore_db_model_userentityrealmproxy.proxyState.getRow$realm().getIndex();
        }
        return false;
    }
}
