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
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;

public class org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy extends CryptoRoomEntity implements RealmObjectProxy, org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface {
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();
    private CryptoRoomEntityColumnInfo columnInfo;
    private ProxyState<CryptoRoomEntity> proxyState;

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "CryptoRoomEntity";
    }

    static final class CryptoRoomEntityColumnInfo extends ColumnInfo {
        long algorithmIndex;
        long blacklistUnverifiedDevicesIndex;
        long maxColumnIndexValue;
        long roomIdIndex;

        CryptoRoomEntityColumnInfo(OsSchemaInfo osSchemaInfo) {
            super(3);
            OsObjectSchemaInfo objectSchemaInfo = osSchemaInfo.getObjectSchemaInfo(ClassNameHelper.INTERNAL_CLASS_NAME);
            String str = CryptoRoomEntityFields.ROOM_ID;
            this.roomIdIndex = addColumnDetails(str, str, objectSchemaInfo);
            String str2 = CryptoRoomEntityFields.ALGORITHM;
            this.algorithmIndex = addColumnDetails(str2, str2, objectSchemaInfo);
            String str3 = CryptoRoomEntityFields.BLACKLIST_UNVERIFIED_DEVICES;
            this.blacklistUnverifiedDevicesIndex = addColumnDetails(str3, str3, objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        CryptoRoomEntityColumnInfo(ColumnInfo columnInfo, boolean z) {
            super(columnInfo, z);
            copy(columnInfo, this);
        }

        /* access modifiers changed from: protected */
        public final ColumnInfo copy(boolean z) {
            return new CryptoRoomEntityColumnInfo(this, z);
        }

        /* access modifiers changed from: protected */
        public final void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2) {
            CryptoRoomEntityColumnInfo cryptoRoomEntityColumnInfo = (CryptoRoomEntityColumnInfo) columnInfo;
            CryptoRoomEntityColumnInfo cryptoRoomEntityColumnInfo2 = (CryptoRoomEntityColumnInfo) columnInfo2;
            cryptoRoomEntityColumnInfo2.roomIdIndex = cryptoRoomEntityColumnInfo.roomIdIndex;
            cryptoRoomEntityColumnInfo2.algorithmIndex = cryptoRoomEntityColumnInfo.algorithmIndex;
            cryptoRoomEntityColumnInfo2.blacklistUnverifiedDevicesIndex = cryptoRoomEntityColumnInfo.blacklistUnverifiedDevicesIndex;
            cryptoRoomEntityColumnInfo2.maxColumnIndexValue = cryptoRoomEntityColumnInfo.maxColumnIndexValue;
        }
    }

    public static String getSimpleClassName() {
        return ClassNameHelper.INTERNAL_CLASS_NAME;
    }

    org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy() {
        this.proxyState.setConstructionFinished();
    }

    public void realm$injectObjectContext() {
        if (this.proxyState == null) {
            RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
            this.columnInfo = (CryptoRoomEntityColumnInfo) realmObjectContext.getColumnInfo();
            this.proxyState = new ProxyState<>(this);
            this.proxyState.setRealm$realm(realmObjectContext.getRealm());
            this.proxyState.setRow$realm(realmObjectContext.getRow());
            this.proxyState.setAcceptDefaultValue$realm(realmObjectContext.getAcceptDefaultValue());
            this.proxyState.setExcludeFields$realm(realmObjectContext.getExcludeFields());
        }
    }

    public String realmGet$roomId() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.roomIdIndex);
    }

    public void realmSet$roomId(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            throw new RealmException("Primary key field 'roomId' cannot be changed after object was created.");
        }
    }

    public String realmGet$algorithm() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.algorithmIndex);
    }

    public void realmSet$algorithm(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.algorithmIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.algorithmIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.algorithmIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.algorithmIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public boolean realmGet$blacklistUnverifiedDevices() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getBoolean(this.columnInfo.blacklistUnverifiedDevicesIndex);
    }

    public void realmSet$blacklistUnverifiedDevices(boolean z) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            this.proxyState.getRow$realm().setBoolean(this.columnInfo.blacklistUnverifiedDevicesIndex, z);
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            row$realm.getTable().setBoolean(this.columnInfo.blacklistUnverifiedDevicesIndex, row$realm.getIndex(), z, true);
        }
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        Builder builder = new Builder(ClassNameHelper.INTERNAL_CLASS_NAME, 3, 0);
        Builder builder2 = builder;
        builder2.addPersistedProperty(CryptoRoomEntityFields.ROOM_ID, RealmFieldType.STRING, true, true, false);
        builder2.addPersistedProperty(CryptoRoomEntityFields.ALGORITHM, RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty(CryptoRoomEntityFields.BLACKLIST_UNVERIFIED_DEVICES, RealmFieldType.BOOLEAN, false, false, true);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static CryptoRoomEntityColumnInfo createColumnInfo(OsSchemaInfo osSchemaInfo) {
        return new CryptoRoomEntityColumnInfo(osSchemaInfo);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00b8  */
    public static CryptoRoomEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject jSONObject, boolean z) throws JSONException {
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy;
        String str;
        String str2;
        long j;
        List emptyList = Collections.emptyList();
        String str3 = CryptoRoomEntityFields.ROOM_ID;
        if (z) {
            Table table = realm.getTable(CryptoRoomEntity.class);
            long j2 = ((CryptoRoomEntityColumnInfo) realm.getSchema().getColumnInfo(CryptoRoomEntity.class)).roomIdIndex;
            if (jSONObject.isNull(str3)) {
                j = table.findFirstNull(j2);
            } else {
                j = table.findFirstString(j2, jSONObject.getString(str3));
            }
            if (j != -1) {
                RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
                try {
                    realmObjectContext.set(realm, table.getUncheckedRow(j), realm.getSchema().getColumnInfo(CryptoRoomEntity.class), false, Collections.emptyList());
                    org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy();
                    if (org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy == null) {
                        if (!jSONObject.has(str3)) {
                            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'roomId'.");
                        } else if (jSONObject.isNull(str3)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy) realm.createObjectInternal(CryptoRoomEntity.class, null, true, emptyList);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy) realm.createObjectInternal(CryptoRoomEntity.class, jSONObject.getString(str3), true, emptyList);
                        }
                    }
                    org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy;
                    str = CryptoRoomEntityFields.ALGORITHM;
                    if (jSONObject.has(str)) {
                        if (jSONObject.isNull(str)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmSet$algorithm(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmSet$algorithm(jSONObject.getString(str));
                        }
                    }
                    str2 = CryptoRoomEntityFields.BLACKLIST_UNVERIFIED_DEVICES;
                    if (jSONObject.has(str2)) {
                        if (!jSONObject.isNull(str2)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmSet$blacklistUnverifiedDevices(jSONObject.getBoolean(str2));
                        } else {
                            throw new IllegalArgumentException("Trying to set non-nullable field 'blacklistUnverifiedDevices' to null.");
                        }
                    }
                    return org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy;
                } finally {
                    realmObjectContext.clear();
                }
            }
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy = null;
        if (org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy == null) {
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface2 = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy;
        str = CryptoRoomEntityFields.ALGORITHM;
        if (jSONObject.has(str)) {
        }
        str2 = CryptoRoomEntityFields.BLACKLIST_UNVERIFIED_DEVICES;
        if (jSONObject.has(str2)) {
        }
        return org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy;
    }

    public static CryptoRoomEntity createUsingJsonStream(Realm realm, JsonReader jsonReader) throws IOException {
        CryptoRoomEntity cryptoRoomEntity = new CryptoRoomEntity();
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface = cryptoRoomEntity;
        jsonReader.beginObject();
        boolean z = false;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if (nextName.equals(CryptoRoomEntityFields.ROOM_ID)) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmSet$roomId(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmSet$roomId(null);
                }
                z = true;
            } else if (nextName.equals(CryptoRoomEntityFields.ALGORITHM)) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmSet$algorithm(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmSet$algorithm(null);
                }
            } else if (!nextName.equals(CryptoRoomEntityFields.BLACKLIST_UNVERIFIED_DEVICES)) {
                jsonReader.skipValue();
            } else if (jsonReader.peek() != JsonToken.NULL) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmSet$blacklistUnverifiedDevices(jsonReader.nextBoolean());
            } else {
                jsonReader.skipValue();
                throw new IllegalArgumentException("Trying to set non-nullable field 'blacklistUnverifiedDevices' to null.");
            }
        }
        jsonReader.endObject();
        if (z) {
            return (CryptoRoomEntity) realm.copyToRealm(cryptoRoomEntity, new ImportFlag[0]);
        }
        throw new IllegalArgumentException("JSON object doesn't have the primary key field 'roomId'.");
    }

    private static org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy newProxyInstance(BaseRealm baseRealm, Row row) {
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        realmObjectContext.set(baseRealm, row, baseRealm.getSchema().getColumnInfo(CryptoRoomEntity.class), false, Collections.emptyList());
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy();
        realmObjectContext.clear();
        return org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a1  */
    public static CryptoRoomEntity copyOrUpdate(Realm realm, CryptoRoomEntityColumnInfo cryptoRoomEntityColumnInfo, CryptoRoomEntity cryptoRoomEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        boolean z2;
        long j;
        if (cryptoRoomEntity instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) cryptoRoomEntity;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null) {
                BaseRealm realm$realm = realmObjectProxy.realmGet$proxyState().getRealm$realm();
                if (realm$realm.threadId != realm.threadId) {
                    throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
                } else if (realm$realm.getPath().equals(realm.getPath())) {
                    return cryptoRoomEntity;
                }
            }
        }
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        RealmObjectProxy realmObjectProxy2 = (RealmObjectProxy) map.get(cryptoRoomEntity);
        if (realmObjectProxy2 != null) {
            return (CryptoRoomEntity) realmObjectProxy2;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy = null;
        if (z) {
            Table table = realm.getTable(CryptoRoomEntity.class);
            long j2 = cryptoRoomEntityColumnInfo.roomIdIndex;
            String realmGet$roomId = cryptoRoomEntity.realmGet$roomId();
            if (realmGet$roomId == null) {
                j = table.findFirstNull(j2);
            } else {
                j = table.findFirstString(j2, realmGet$roomId);
            }
            if (j == -1) {
                z2 = false;
                return !z2 ? update(realm, cryptoRoomEntityColumnInfo, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy, cryptoRoomEntity, map, set) : copy(realm, cryptoRoomEntityColumnInfo, cryptoRoomEntity, z, map, set);
            }
            try {
                realmObjectContext.set(realm, table.getUncheckedRow(j), cryptoRoomEntityColumnInfo, false, Collections.emptyList());
                org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy();
                map.put(cryptoRoomEntity, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy);
            } finally {
                realmObjectContext.clear();
            }
        }
        z2 = z;
        return !z2 ? update(realm, cryptoRoomEntityColumnInfo, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy, cryptoRoomEntity, map, set) : copy(realm, cryptoRoomEntityColumnInfo, cryptoRoomEntity, z, map, set);
    }

    public static CryptoRoomEntity copy(Realm realm, CryptoRoomEntityColumnInfo cryptoRoomEntityColumnInfo, CryptoRoomEntity cryptoRoomEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        RealmObjectProxy realmObjectProxy = (RealmObjectProxy) map.get(cryptoRoomEntity);
        if (realmObjectProxy != null) {
            return (CryptoRoomEntity) realmObjectProxy;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface = cryptoRoomEntity;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(CryptoRoomEntity.class), cryptoRoomEntityColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addString(cryptoRoomEntityColumnInfo.roomIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmGet$roomId());
        osObjectBuilder.addString(cryptoRoomEntityColumnInfo.algorithmIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmGet$algorithm());
        osObjectBuilder.addBoolean(cryptoRoomEntityColumnInfo.blacklistUnverifiedDevicesIndex, Boolean.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmGet$blacklistUnverifiedDevices()));
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy newProxyInstance = newProxyInstance(realm, osObjectBuilder.createNewObject());
        map.put(cryptoRoomEntity, newProxyInstance);
        return newProxyInstance;
    }

    public static long insert(Realm realm, CryptoRoomEntity cryptoRoomEntity, Map<RealmModel, Long> map) {
        long j;
        long j2;
        CryptoRoomEntity cryptoRoomEntity2 = cryptoRoomEntity;
        if (cryptoRoomEntity2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) cryptoRoomEntity2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(CryptoRoomEntity.class);
        long nativePtr = table.getNativePtr();
        CryptoRoomEntityColumnInfo cryptoRoomEntityColumnInfo = (CryptoRoomEntityColumnInfo) realm.getSchema().getColumnInfo(CryptoRoomEntity.class);
        long j3 = cryptoRoomEntityColumnInfo.roomIdIndex;
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface = cryptoRoomEntity2;
        String realmGet$roomId = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmGet$roomId();
        if (realmGet$roomId == null) {
            j = Table.nativeFindFirstNull(nativePtr, j3);
        } else {
            j = Table.nativeFindFirstString(nativePtr, j3, realmGet$roomId);
        }
        if (j == -1) {
            j2 = OsObject.createRowWithPrimaryKey(table, j3, realmGet$roomId);
        } else {
            Table.throwDuplicatePrimaryKeyException(realmGet$roomId);
            j2 = j;
        }
        map.put(cryptoRoomEntity2, Long.valueOf(j2));
        String realmGet$algorithm = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmGet$algorithm();
        if (realmGet$algorithm != null) {
            Table.nativeSetString(nativePtr, cryptoRoomEntityColumnInfo.algorithmIndex, j2, realmGet$algorithm, false);
        }
        Table.nativeSetBoolean(nativePtr, cryptoRoomEntityColumnInfo.blacklistUnverifiedDevicesIndex, j2, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmGet$blacklistUnverifiedDevices(), false);
        return j2;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        long j2;
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(CryptoRoomEntity.class);
        long nativePtr = table.getNativePtr();
        CryptoRoomEntityColumnInfo cryptoRoomEntityColumnInfo = (CryptoRoomEntityColumnInfo) realm.getSchema().getColumnInfo(CryptoRoomEntity.class);
        long j3 = cryptoRoomEntityColumnInfo.roomIdIndex;
        while (it.hasNext()) {
            CryptoRoomEntity cryptoRoomEntity = (CryptoRoomEntity) it.next();
            if (!map2.containsKey(cryptoRoomEntity)) {
                if (cryptoRoomEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) cryptoRoomEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(cryptoRoomEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface = cryptoRoomEntity;
                String realmGet$roomId = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmGet$roomId();
                if (realmGet$roomId == null) {
                    j = Table.nativeFindFirstNull(nativePtr, j3);
                } else {
                    j = Table.nativeFindFirstString(nativePtr, j3, realmGet$roomId);
                }
                if (j == -1) {
                    j2 = OsObject.createRowWithPrimaryKey(table, j3, realmGet$roomId);
                } else {
                    Table.throwDuplicatePrimaryKeyException(realmGet$roomId);
                    j2 = j;
                }
                map2.put(cryptoRoomEntity, Long.valueOf(j2));
                String realmGet$algorithm = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmGet$algorithm();
                if (realmGet$algorithm != null) {
                    Table.nativeSetString(nativePtr, cryptoRoomEntityColumnInfo.algorithmIndex, j2, realmGet$algorithm, false);
                }
                Table.nativeSetBoolean(nativePtr, cryptoRoomEntityColumnInfo.blacklistUnverifiedDevicesIndex, j2, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmGet$blacklistUnverifiedDevices(), false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, CryptoRoomEntity cryptoRoomEntity, Map<RealmModel, Long> map) {
        long j;
        CryptoRoomEntity cryptoRoomEntity2 = cryptoRoomEntity;
        if (cryptoRoomEntity2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) cryptoRoomEntity2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(CryptoRoomEntity.class);
        long nativePtr = table.getNativePtr();
        CryptoRoomEntityColumnInfo cryptoRoomEntityColumnInfo = (CryptoRoomEntityColumnInfo) realm.getSchema().getColumnInfo(CryptoRoomEntity.class);
        long j2 = cryptoRoomEntityColumnInfo.roomIdIndex;
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface = cryptoRoomEntity2;
        String realmGet$roomId = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmGet$roomId();
        if (realmGet$roomId == null) {
            j = Table.nativeFindFirstNull(nativePtr, j2);
        } else {
            j = Table.nativeFindFirstString(nativePtr, j2, realmGet$roomId);
        }
        long createRowWithPrimaryKey = j == -1 ? OsObject.createRowWithPrimaryKey(table, j2, realmGet$roomId) : j;
        map.put(cryptoRoomEntity2, Long.valueOf(createRowWithPrimaryKey));
        String realmGet$algorithm = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmGet$algorithm();
        if (realmGet$algorithm != null) {
            Table.nativeSetString(nativePtr, cryptoRoomEntityColumnInfo.algorithmIndex, createRowWithPrimaryKey, realmGet$algorithm, false);
        } else {
            Table.nativeSetNull(nativePtr, cryptoRoomEntityColumnInfo.algorithmIndex, createRowWithPrimaryKey, false);
        }
        Table.nativeSetBoolean(nativePtr, cryptoRoomEntityColumnInfo.blacklistUnverifiedDevicesIndex, createRowWithPrimaryKey, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmGet$blacklistUnverifiedDevices(), false);
        return createRowWithPrimaryKey;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(CryptoRoomEntity.class);
        long nativePtr = table.getNativePtr();
        CryptoRoomEntityColumnInfo cryptoRoomEntityColumnInfo = (CryptoRoomEntityColumnInfo) realm.getSchema().getColumnInfo(CryptoRoomEntity.class);
        long j2 = cryptoRoomEntityColumnInfo.roomIdIndex;
        while (it.hasNext()) {
            CryptoRoomEntity cryptoRoomEntity = (CryptoRoomEntity) it.next();
            if (!map2.containsKey(cryptoRoomEntity)) {
                if (cryptoRoomEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) cryptoRoomEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(cryptoRoomEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface = cryptoRoomEntity;
                String realmGet$roomId = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmGet$roomId();
                if (realmGet$roomId == null) {
                    j = Table.nativeFindFirstNull(nativePtr, j2);
                } else {
                    j = Table.nativeFindFirstString(nativePtr, j2, realmGet$roomId);
                }
                long createRowWithPrimaryKey = j == -1 ? OsObject.createRowWithPrimaryKey(table, j2, realmGet$roomId) : j;
                map2.put(cryptoRoomEntity, Long.valueOf(createRowWithPrimaryKey));
                String realmGet$algorithm = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmGet$algorithm();
                if (realmGet$algorithm != null) {
                    Table.nativeSetString(nativePtr, cryptoRoomEntityColumnInfo.algorithmIndex, createRowWithPrimaryKey, realmGet$algorithm, false);
                } else {
                    Table.nativeSetNull(nativePtr, cryptoRoomEntityColumnInfo.algorithmIndex, createRowWithPrimaryKey, false);
                }
                Table.nativeSetBoolean(nativePtr, cryptoRoomEntityColumnInfo.blacklistUnverifiedDevicesIndex, createRowWithPrimaryKey, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmGet$blacklistUnverifiedDevices(), false);
            }
        }
    }

    public static CryptoRoomEntity createDetachedCopy(CryptoRoomEntity cryptoRoomEntity, int i, int i2, Map<RealmModel, CacheData<RealmModel>> map) {
        CryptoRoomEntity cryptoRoomEntity2;
        if (i > i2 || cryptoRoomEntity == null) {
            return null;
        }
        CacheData cacheData = (CacheData) map.get(cryptoRoomEntity);
        if (cacheData == null) {
            cryptoRoomEntity2 = new CryptoRoomEntity();
            map.put(cryptoRoomEntity, new CacheData(i, cryptoRoomEntity2));
        } else if (i >= cacheData.minDepth) {
            return (CryptoRoomEntity) cacheData.object;
        } else {
            CryptoRoomEntity cryptoRoomEntity3 = (CryptoRoomEntity) cacheData.object;
            cacheData.minDepth = i;
            cryptoRoomEntity2 = cryptoRoomEntity3;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface = cryptoRoomEntity2;
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface2 = cryptoRoomEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmSet$roomId(org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface2.realmGet$roomId());
        org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmSet$algorithm(org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface2.realmGet$algorithm());
        org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface.realmSet$blacklistUnverifiedDevices(org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface2.realmGet$blacklistUnverifiedDevices());
        return cryptoRoomEntity2;
    }

    static CryptoRoomEntity update(Realm realm, CryptoRoomEntityColumnInfo cryptoRoomEntityColumnInfo, CryptoRoomEntity cryptoRoomEntity, CryptoRoomEntity cryptoRoomEntity2, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface = cryptoRoomEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface2 = cryptoRoomEntity2;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(CryptoRoomEntity.class), cryptoRoomEntityColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addString(cryptoRoomEntityColumnInfo.roomIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface2.realmGet$roomId());
        osObjectBuilder.addString(cryptoRoomEntityColumnInfo.algorithmIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface2.realmGet$algorithm());
        osObjectBuilder.addBoolean(cryptoRoomEntityColumnInfo.blacklistUnverifiedDevicesIndex, Boolean.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxyinterface2.realmGet$blacklistUnverifiedDevices()));
        osObjectBuilder.updateExistingObject();
        return cryptoRoomEntity;
    }

    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder sb = new StringBuilder("CryptoRoomEntity = proxy[");
        sb.append("{roomId:");
        String str = "null";
        sb.append(realmGet$roomId() != null ? realmGet$roomId() : str);
        String str2 = "}";
        sb.append(str2);
        String str3 = ",";
        sb.append(str3);
        sb.append("{algorithm:");
        if (realmGet$algorithm() != null) {
            str = realmGet$algorithm();
        }
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        sb.append("{blacklistUnverifiedDevices:");
        sb.append(realmGet$blacklistUnverifiedDevices());
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
        org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxy) obj;
        String path = this.proxyState.getRealm$realm().getPath();
        String path2 = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy.proxyState.getRealm$realm().getPath();
        if (path == null ? path2 != null : !path.equals(path2)) {
            return false;
        }
        String name = this.proxyState.getRow$realm().getTable().getName();
        String name2 = org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy.proxyState.getRow$realm().getTable().getName();
        if (name == null ? name2 == null : name.equals(name2)) {
            return this.proxyState.getRow$realm().getIndex() == org_matrix_androidsdk_crypto_cryptostore_db_model_cryptoroomentityrealmproxy.proxyState.getRow$realm().getIndex();
        }
        return false;
    }
}
