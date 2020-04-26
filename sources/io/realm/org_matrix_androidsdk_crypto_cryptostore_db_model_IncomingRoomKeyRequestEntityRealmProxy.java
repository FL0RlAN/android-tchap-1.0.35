package io.realm;

import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.BaseRealm.RealmObjectContext;
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
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import org.matrix.androidsdk.crypto.cryptostore.db.model.IncomingRoomKeyRequestEntity;

public class org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy extends IncomingRoomKeyRequestEntity implements RealmObjectProxy, org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface {
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();
    private IncomingRoomKeyRequestEntityColumnInfo columnInfo;
    private ProxyState<IncomingRoomKeyRequestEntity> proxyState;

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "IncomingRoomKeyRequestEntity";
    }

    static final class IncomingRoomKeyRequestEntityColumnInfo extends ColumnInfo {
        long deviceIdIndex;
        long maxColumnIndexValue;
        long requestBodyAlgorithmIndex;
        long requestBodyRoomIdIndex;
        long requestBodySenderKeyIndex;
        long requestBodySessionIdIndex;
        long requestIdIndex;
        long userIdIndex;

        IncomingRoomKeyRequestEntityColumnInfo(OsSchemaInfo osSchemaInfo) {
            super(7);
            OsObjectSchemaInfo objectSchemaInfo = osSchemaInfo.getObjectSchemaInfo(ClassNameHelper.INTERNAL_CLASS_NAME);
            String str = "requestId";
            this.requestIdIndex = addColumnDetails(str, str, objectSchemaInfo);
            String str2 = "userId";
            this.userIdIndex = addColumnDetails(str2, str2, objectSchemaInfo);
            String str3 = "deviceId";
            this.deviceIdIndex = addColumnDetails(str3, str3, objectSchemaInfo);
            String str4 = "requestBodyAlgorithm";
            this.requestBodyAlgorithmIndex = addColumnDetails(str4, str4, objectSchemaInfo);
            String str5 = "requestBodyRoomId";
            this.requestBodyRoomIdIndex = addColumnDetails(str5, str5, objectSchemaInfo);
            String str6 = "requestBodySenderKey";
            this.requestBodySenderKeyIndex = addColumnDetails(str6, str6, objectSchemaInfo);
            String str7 = "requestBodySessionId";
            this.requestBodySessionIdIndex = addColumnDetails(str7, str7, objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        IncomingRoomKeyRequestEntityColumnInfo(ColumnInfo columnInfo, boolean z) {
            super(columnInfo, z);
            copy(columnInfo, this);
        }

        /* access modifiers changed from: protected */
        public final ColumnInfo copy(boolean z) {
            return new IncomingRoomKeyRequestEntityColumnInfo(this, z);
        }

        /* access modifiers changed from: protected */
        public final void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2) {
            IncomingRoomKeyRequestEntityColumnInfo incomingRoomKeyRequestEntityColumnInfo = (IncomingRoomKeyRequestEntityColumnInfo) columnInfo;
            IncomingRoomKeyRequestEntityColumnInfo incomingRoomKeyRequestEntityColumnInfo2 = (IncomingRoomKeyRequestEntityColumnInfo) columnInfo2;
            incomingRoomKeyRequestEntityColumnInfo2.requestIdIndex = incomingRoomKeyRequestEntityColumnInfo.requestIdIndex;
            incomingRoomKeyRequestEntityColumnInfo2.userIdIndex = incomingRoomKeyRequestEntityColumnInfo.userIdIndex;
            incomingRoomKeyRequestEntityColumnInfo2.deviceIdIndex = incomingRoomKeyRequestEntityColumnInfo.deviceIdIndex;
            incomingRoomKeyRequestEntityColumnInfo2.requestBodyAlgorithmIndex = incomingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex;
            incomingRoomKeyRequestEntityColumnInfo2.requestBodyRoomIdIndex = incomingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex;
            incomingRoomKeyRequestEntityColumnInfo2.requestBodySenderKeyIndex = incomingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex;
            incomingRoomKeyRequestEntityColumnInfo2.requestBodySessionIdIndex = incomingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex;
            incomingRoomKeyRequestEntityColumnInfo2.maxColumnIndexValue = incomingRoomKeyRequestEntityColumnInfo.maxColumnIndexValue;
        }
    }

    public static String getSimpleClassName() {
        return ClassNameHelper.INTERNAL_CLASS_NAME;
    }

    org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy() {
        this.proxyState.setConstructionFinished();
    }

    public void realm$injectObjectContext() {
        if (this.proxyState == null) {
            RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
            this.columnInfo = (IncomingRoomKeyRequestEntityColumnInfo) realmObjectContext.getColumnInfo();
            this.proxyState = new ProxyState<>(this);
            this.proxyState.setRealm$realm(realmObjectContext.getRealm());
            this.proxyState.setRow$realm(realmObjectContext.getRow());
            this.proxyState.setAcceptDefaultValue$realm(realmObjectContext.getAcceptDefaultValue());
            this.proxyState.setExcludeFields$realm(realmObjectContext.getExcludeFields());
        }
    }

    public String realmGet$requestId() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.requestIdIndex);
    }

    public void realmSet$requestId(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.requestIdIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.requestIdIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.requestIdIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.requestIdIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public String realmGet$userId() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.userIdIndex);
    }

    public void realmSet$userId(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.userIdIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.userIdIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.userIdIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.userIdIndex, row$realm.getIndex(), str, true);
            }
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

    public String realmGet$requestBodyAlgorithm() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.requestBodyAlgorithmIndex);
    }

    public void realmSet$requestBodyAlgorithm(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.requestBodyAlgorithmIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.requestBodyAlgorithmIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.requestBodyAlgorithmIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.requestBodyAlgorithmIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public String realmGet$requestBodyRoomId() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.requestBodyRoomIdIndex);
    }

    public void realmSet$requestBodyRoomId(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.requestBodyRoomIdIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.requestBodyRoomIdIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.requestBodyRoomIdIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.requestBodyRoomIdIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public String realmGet$requestBodySenderKey() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.requestBodySenderKeyIndex);
    }

    public void realmSet$requestBodySenderKey(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.requestBodySenderKeyIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.requestBodySenderKeyIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.requestBodySenderKeyIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.requestBodySenderKeyIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public String realmGet$requestBodySessionId() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.requestBodySessionIdIndex);
    }

    public void realmSet$requestBodySessionId(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.requestBodySessionIdIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.requestBodySessionIdIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.requestBodySessionIdIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.requestBodySessionIdIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        Builder builder = new Builder(ClassNameHelper.INTERNAL_CLASS_NAME, 7, 0);
        Builder builder2 = builder;
        builder2.addPersistedProperty("requestId", RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty("userId", RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty("deviceId", RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty("requestBodyAlgorithm", RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty("requestBodyRoomId", RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty("requestBodySenderKey", RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty("requestBodySessionId", RealmFieldType.STRING, false, false, false);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static IncomingRoomKeyRequestEntityColumnInfo createColumnInfo(OsSchemaInfo osSchemaInfo) {
        return new IncomingRoomKeyRequestEntityColumnInfo(osSchemaInfo);
    }

    public static IncomingRoomKeyRequestEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject jSONObject, boolean z) throws JSONException {
        IncomingRoomKeyRequestEntity incomingRoomKeyRequestEntity = (IncomingRoomKeyRequestEntity) realm.createObjectInternal(IncomingRoomKeyRequestEntity.class, true, Collections.emptyList());
        org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface = incomingRoomKeyRequestEntity;
        String str = "requestId";
        if (jSONObject.has(str)) {
            if (jSONObject.isNull(str)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestId(null);
            } else {
                org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestId(jSONObject.getString(str));
            }
        }
        String str2 = "userId";
        if (jSONObject.has(str2)) {
            if (jSONObject.isNull(str2)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$userId(null);
            } else {
                org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$userId(jSONObject.getString(str2));
            }
        }
        String str3 = "deviceId";
        if (jSONObject.has(str3)) {
            if (jSONObject.isNull(str3)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$deviceId(null);
            } else {
                org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$deviceId(jSONObject.getString(str3));
            }
        }
        String str4 = "requestBodyAlgorithm";
        if (jSONObject.has(str4)) {
            if (jSONObject.isNull(str4)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyAlgorithm(null);
            } else {
                org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyAlgorithm(jSONObject.getString(str4));
            }
        }
        String str5 = "requestBodyRoomId";
        if (jSONObject.has(str5)) {
            if (jSONObject.isNull(str5)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyRoomId(null);
            } else {
                org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyRoomId(jSONObject.getString(str5));
            }
        }
        String str6 = "requestBodySenderKey";
        if (jSONObject.has(str6)) {
            if (jSONObject.isNull(str6)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySenderKey(null);
            } else {
                org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySenderKey(jSONObject.getString(str6));
            }
        }
        String str7 = "requestBodySessionId";
        if (jSONObject.has(str7)) {
            if (jSONObject.isNull(str7)) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySessionId(null);
            } else {
                org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySessionId(jSONObject.getString(str7));
            }
        }
        return incomingRoomKeyRequestEntity;
    }

    public static IncomingRoomKeyRequestEntity createUsingJsonStream(Realm realm, JsonReader jsonReader) throws IOException {
        IncomingRoomKeyRequestEntity incomingRoomKeyRequestEntity = new IncomingRoomKeyRequestEntity();
        org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface = incomingRoomKeyRequestEntity;
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if (nextName.equals("requestId")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestId(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestId(null);
                }
            } else if (nextName.equals("userId")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$userId(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$userId(null);
                }
            } else if (nextName.equals("deviceId")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$deviceId(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$deviceId(null);
                }
            } else if (nextName.equals("requestBodyAlgorithm")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyAlgorithm(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyAlgorithm(null);
                }
            } else if (nextName.equals("requestBodyRoomId")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyRoomId(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyRoomId(null);
                }
            } else if (nextName.equals("requestBodySenderKey")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySenderKey(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySenderKey(null);
                }
            } else if (!nextName.equals("requestBodySessionId")) {
                jsonReader.skipValue();
            } else if (jsonReader.peek() != JsonToken.NULL) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySessionId(jsonReader.nextString());
            } else {
                jsonReader.skipValue();
                org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySessionId(null);
            }
        }
        jsonReader.endObject();
        return (IncomingRoomKeyRequestEntity) realm.copyToRealm(incomingRoomKeyRequestEntity, new ImportFlag[0]);
    }

    private static org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy newProxyInstance(BaseRealm baseRealm, Row row) {
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        realmObjectContext.set(baseRealm, row, baseRealm.getSchema().getColumnInfo(IncomingRoomKeyRequestEntity.class), false, Collections.emptyList());
        org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy();
        realmObjectContext.clear();
        return org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxy;
    }

    public static IncomingRoomKeyRequestEntity copyOrUpdate(Realm realm, IncomingRoomKeyRequestEntityColumnInfo incomingRoomKeyRequestEntityColumnInfo, IncomingRoomKeyRequestEntity incomingRoomKeyRequestEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        if (incomingRoomKeyRequestEntity instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) incomingRoomKeyRequestEntity;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null) {
                BaseRealm realm$realm = realmObjectProxy.realmGet$proxyState().getRealm$realm();
                if (realm$realm.threadId != realm.threadId) {
                    throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
                } else if (realm$realm.getPath().equals(realm.getPath())) {
                    return incomingRoomKeyRequestEntity;
                }
            }
        }
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        RealmObjectProxy realmObjectProxy2 = (RealmObjectProxy) map.get(incomingRoomKeyRequestEntity);
        if (realmObjectProxy2 != null) {
            return (IncomingRoomKeyRequestEntity) realmObjectProxy2;
        }
        return copy(realm, incomingRoomKeyRequestEntityColumnInfo, incomingRoomKeyRequestEntity, z, map, set);
    }

    public static IncomingRoomKeyRequestEntity copy(Realm realm, IncomingRoomKeyRequestEntityColumnInfo incomingRoomKeyRequestEntityColumnInfo, IncomingRoomKeyRequestEntity incomingRoomKeyRequestEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        RealmObjectProxy realmObjectProxy = (RealmObjectProxy) map.get(incomingRoomKeyRequestEntity);
        if (realmObjectProxy != null) {
            return (IncomingRoomKeyRequestEntity) realmObjectProxy;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface = incomingRoomKeyRequestEntity;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(IncomingRoomKeyRequestEntity.class), incomingRoomKeyRequestEntityColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addString(incomingRoomKeyRequestEntityColumnInfo.requestIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestId());
        osObjectBuilder.addString(incomingRoomKeyRequestEntityColumnInfo.userIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$userId());
        osObjectBuilder.addString(incomingRoomKeyRequestEntityColumnInfo.deviceIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$deviceId());
        osObjectBuilder.addString(incomingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyAlgorithm());
        osObjectBuilder.addString(incomingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyRoomId());
        osObjectBuilder.addString(incomingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySenderKey());
        osObjectBuilder.addString(incomingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySessionId());
        org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy newProxyInstance = newProxyInstance(realm, osObjectBuilder.createNewObject());
        map.put(incomingRoomKeyRequestEntity, newProxyInstance);
        return newProxyInstance;
    }

    public static long insert(Realm realm, IncomingRoomKeyRequestEntity incomingRoomKeyRequestEntity, Map<RealmModel, Long> map) {
        if (incomingRoomKeyRequestEntity instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) incomingRoomKeyRequestEntity;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(IncomingRoomKeyRequestEntity.class);
        long nativePtr = table.getNativePtr();
        IncomingRoomKeyRequestEntityColumnInfo incomingRoomKeyRequestEntityColumnInfo = (IncomingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(IncomingRoomKeyRequestEntity.class);
        long createRow = OsObject.createRow(table);
        map.put(incomingRoomKeyRequestEntity, Long.valueOf(createRow));
        org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface = incomingRoomKeyRequestEntity;
        String realmGet$requestId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestId();
        if (realmGet$requestId != null) {
            Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestIdIndex, createRow, realmGet$requestId, false);
        }
        String realmGet$userId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$userId();
        if (realmGet$userId != null) {
            Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.userIdIndex, createRow, realmGet$userId, false);
        }
        String realmGet$deviceId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$deviceId();
        if (realmGet$deviceId != null) {
            Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.deviceIdIndex, createRow, realmGet$deviceId, false);
        }
        String realmGet$requestBodyAlgorithm = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyAlgorithm();
        if (realmGet$requestBodyAlgorithm != null) {
            Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex, createRow, realmGet$requestBodyAlgorithm, false);
        }
        String realmGet$requestBodyRoomId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyRoomId();
        if (realmGet$requestBodyRoomId != null) {
            Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex, createRow, realmGet$requestBodyRoomId, false);
        }
        String realmGet$requestBodySenderKey = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySenderKey();
        if (realmGet$requestBodySenderKey != null) {
            Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex, createRow, realmGet$requestBodySenderKey, false);
        }
        String realmGet$requestBodySessionId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySessionId();
        if (realmGet$requestBodySessionId != null) {
            Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex, createRow, realmGet$requestBodySessionId, false);
        }
        return createRow;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(IncomingRoomKeyRequestEntity.class);
        long nativePtr = table.getNativePtr();
        IncomingRoomKeyRequestEntityColumnInfo incomingRoomKeyRequestEntityColumnInfo = (IncomingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(IncomingRoomKeyRequestEntity.class);
        while (it.hasNext()) {
            IncomingRoomKeyRequestEntity incomingRoomKeyRequestEntity = (IncomingRoomKeyRequestEntity) it.next();
            if (!map2.containsKey(incomingRoomKeyRequestEntity)) {
                if (incomingRoomKeyRequestEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) incomingRoomKeyRequestEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(incomingRoomKeyRequestEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                long createRow = OsObject.createRow(table);
                map2.put(incomingRoomKeyRequestEntity, Long.valueOf(createRow));
                org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface = incomingRoomKeyRequestEntity;
                String realmGet$requestId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestId();
                if (realmGet$requestId != null) {
                    Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestIdIndex, createRow, realmGet$requestId, false);
                }
                String realmGet$userId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$userId();
                if (realmGet$userId != null) {
                    Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.userIdIndex, createRow, realmGet$userId, false);
                }
                String realmGet$deviceId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$deviceId();
                if (realmGet$deviceId != null) {
                    Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.deviceIdIndex, createRow, realmGet$deviceId, false);
                }
                String realmGet$requestBodyAlgorithm = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyAlgorithm();
                if (realmGet$requestBodyAlgorithm != null) {
                    Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex, createRow, realmGet$requestBodyAlgorithm, false);
                }
                String realmGet$requestBodyRoomId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyRoomId();
                if (realmGet$requestBodyRoomId != null) {
                    Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex, createRow, realmGet$requestBodyRoomId, false);
                }
                String realmGet$requestBodySenderKey = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySenderKey();
                if (realmGet$requestBodySenderKey != null) {
                    Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex, createRow, realmGet$requestBodySenderKey, false);
                }
                String realmGet$requestBodySessionId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySessionId();
                if (realmGet$requestBodySessionId != null) {
                    Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex, createRow, realmGet$requestBodySessionId, false);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, IncomingRoomKeyRequestEntity incomingRoomKeyRequestEntity, Map<RealmModel, Long> map) {
        if (incomingRoomKeyRequestEntity instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) incomingRoomKeyRequestEntity;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(IncomingRoomKeyRequestEntity.class);
        long nativePtr = table.getNativePtr();
        IncomingRoomKeyRequestEntityColumnInfo incomingRoomKeyRequestEntityColumnInfo = (IncomingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(IncomingRoomKeyRequestEntity.class);
        long createRow = OsObject.createRow(table);
        map.put(incomingRoomKeyRequestEntity, Long.valueOf(createRow));
        org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface = incomingRoomKeyRequestEntity;
        String realmGet$requestId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestId();
        if (realmGet$requestId != null) {
            Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestIdIndex, createRow, realmGet$requestId, false);
        } else {
            Table.nativeSetNull(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestIdIndex, createRow, false);
        }
        String realmGet$userId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$userId();
        if (realmGet$userId != null) {
            Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.userIdIndex, createRow, realmGet$userId, false);
        } else {
            Table.nativeSetNull(nativePtr, incomingRoomKeyRequestEntityColumnInfo.userIdIndex, createRow, false);
        }
        String realmGet$deviceId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$deviceId();
        if (realmGet$deviceId != null) {
            Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.deviceIdIndex, createRow, realmGet$deviceId, false);
        } else {
            Table.nativeSetNull(nativePtr, incomingRoomKeyRequestEntityColumnInfo.deviceIdIndex, createRow, false);
        }
        String realmGet$requestBodyAlgorithm = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyAlgorithm();
        if (realmGet$requestBodyAlgorithm != null) {
            Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex, createRow, realmGet$requestBodyAlgorithm, false);
        } else {
            Table.nativeSetNull(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex, createRow, false);
        }
        String realmGet$requestBodyRoomId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyRoomId();
        if (realmGet$requestBodyRoomId != null) {
            Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex, createRow, realmGet$requestBodyRoomId, false);
        } else {
            Table.nativeSetNull(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex, createRow, false);
        }
        String realmGet$requestBodySenderKey = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySenderKey();
        if (realmGet$requestBodySenderKey != null) {
            Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex, createRow, realmGet$requestBodySenderKey, false);
        } else {
            Table.nativeSetNull(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex, createRow, false);
        }
        String realmGet$requestBodySessionId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySessionId();
        if (realmGet$requestBodySessionId != null) {
            Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex, createRow, realmGet$requestBodySessionId, false);
        } else {
            Table.nativeSetNull(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex, createRow, false);
        }
        return createRow;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(IncomingRoomKeyRequestEntity.class);
        long nativePtr = table.getNativePtr();
        IncomingRoomKeyRequestEntityColumnInfo incomingRoomKeyRequestEntityColumnInfo = (IncomingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(IncomingRoomKeyRequestEntity.class);
        while (it.hasNext()) {
            IncomingRoomKeyRequestEntity incomingRoomKeyRequestEntity = (IncomingRoomKeyRequestEntity) it.next();
            if (!map2.containsKey(incomingRoomKeyRequestEntity)) {
                if (incomingRoomKeyRequestEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) incomingRoomKeyRequestEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(incomingRoomKeyRequestEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                long createRow = OsObject.createRow(table);
                map2.put(incomingRoomKeyRequestEntity, Long.valueOf(createRow));
                org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface = incomingRoomKeyRequestEntity;
                String realmGet$requestId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestId();
                if (realmGet$requestId != null) {
                    Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestIdIndex, createRow, realmGet$requestId, false);
                } else {
                    Table.nativeSetNull(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestIdIndex, createRow, false);
                }
                String realmGet$userId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$userId();
                if (realmGet$userId != null) {
                    Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.userIdIndex, createRow, realmGet$userId, false);
                } else {
                    Table.nativeSetNull(nativePtr, incomingRoomKeyRequestEntityColumnInfo.userIdIndex, createRow, false);
                }
                String realmGet$deviceId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$deviceId();
                if (realmGet$deviceId != null) {
                    Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.deviceIdIndex, createRow, realmGet$deviceId, false);
                } else {
                    Table.nativeSetNull(nativePtr, incomingRoomKeyRequestEntityColumnInfo.deviceIdIndex, createRow, false);
                }
                String realmGet$requestBodyAlgorithm = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyAlgorithm();
                if (realmGet$requestBodyAlgorithm != null) {
                    Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex, createRow, realmGet$requestBodyAlgorithm, false);
                } else {
                    Table.nativeSetNull(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex, createRow, false);
                }
                String realmGet$requestBodyRoomId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyRoomId();
                if (realmGet$requestBodyRoomId != null) {
                    Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex, createRow, realmGet$requestBodyRoomId, false);
                } else {
                    Table.nativeSetNull(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex, createRow, false);
                }
                String realmGet$requestBodySenderKey = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySenderKey();
                if (realmGet$requestBodySenderKey != null) {
                    Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex, createRow, realmGet$requestBodySenderKey, false);
                } else {
                    Table.nativeSetNull(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex, createRow, false);
                }
                String realmGet$requestBodySessionId = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySessionId();
                if (realmGet$requestBodySessionId != null) {
                    Table.nativeSetString(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex, createRow, realmGet$requestBodySessionId, false);
                } else {
                    Table.nativeSetNull(nativePtr, incomingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex, createRow, false);
                }
            }
        }
    }

    public static IncomingRoomKeyRequestEntity createDetachedCopy(IncomingRoomKeyRequestEntity incomingRoomKeyRequestEntity, int i, int i2, Map<RealmModel, CacheData<RealmModel>> map) {
        IncomingRoomKeyRequestEntity incomingRoomKeyRequestEntity2;
        if (i > i2 || incomingRoomKeyRequestEntity == null) {
            return null;
        }
        CacheData cacheData = (CacheData) map.get(incomingRoomKeyRequestEntity);
        if (cacheData == null) {
            incomingRoomKeyRequestEntity2 = new IncomingRoomKeyRequestEntity();
            map.put(incomingRoomKeyRequestEntity, new CacheData(i, incomingRoomKeyRequestEntity2));
        } else if (i >= cacheData.minDepth) {
            return (IncomingRoomKeyRequestEntity) cacheData.object;
        } else {
            IncomingRoomKeyRequestEntity incomingRoomKeyRequestEntity3 = (IncomingRoomKeyRequestEntity) cacheData.object;
            cacheData.minDepth = i;
            incomingRoomKeyRequestEntity2 = incomingRoomKeyRequestEntity3;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface = incomingRoomKeyRequestEntity2;
        org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface2 = incomingRoomKeyRequestEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestId(org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface2.realmGet$requestId());
        org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$userId(org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface2.realmGet$userId());
        org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$deviceId(org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface2.realmGet$deviceId());
        org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyAlgorithm(org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface2.realmGet$requestBodyAlgorithm());
        org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyRoomId(org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface2.realmGet$requestBodyRoomId());
        org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySenderKey(org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface2.realmGet$requestBodySenderKey());
        org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySessionId(org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxyinterface2.realmGet$requestBodySessionId());
        return incomingRoomKeyRequestEntity2;
    }

    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder sb = new StringBuilder("IncomingRoomKeyRequestEntity = proxy[");
        sb.append("{requestId:");
        String str = "null";
        sb.append(realmGet$requestId() != null ? realmGet$requestId() : str);
        String str2 = "}";
        sb.append(str2);
        String str3 = ",";
        sb.append(str3);
        sb.append("{userId:");
        sb.append(realmGet$userId() != null ? realmGet$userId() : str);
        sb.append(str2);
        sb.append(str3);
        sb.append("{deviceId:");
        sb.append(realmGet$deviceId() != null ? realmGet$deviceId() : str);
        sb.append(str2);
        sb.append(str3);
        sb.append("{requestBodyAlgorithm:");
        sb.append(realmGet$requestBodyAlgorithm() != null ? realmGet$requestBodyAlgorithm() : str);
        sb.append(str2);
        sb.append(str3);
        sb.append("{requestBodyRoomId:");
        sb.append(realmGet$requestBodyRoomId() != null ? realmGet$requestBodyRoomId() : str);
        sb.append(str2);
        sb.append(str3);
        sb.append("{requestBodySenderKey:");
        sb.append(realmGet$requestBodySenderKey() != null ? realmGet$requestBodySenderKey() : str);
        sb.append(str2);
        sb.append(str3);
        sb.append("{requestBodySessionId:");
        if (realmGet$requestBodySessionId() != null) {
            str = realmGet$requestBodySessionId();
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
        org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy) obj;
        String path = this.proxyState.getRealm$realm().getPath();
        String path2 = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxy.proxyState.getRealm$realm().getPath();
        if (path == null ? path2 != null : !path.equals(path2)) {
            return false;
        }
        String name = this.proxyState.getRow$realm().getTable().getName();
        String name2 = org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxy.proxyState.getRow$realm().getTable().getName();
        if (name == null ? name2 == null : name.equals(name2)) {
            return this.proxyState.getRow$realm().getIndex() == org_matrix_androidsdk_crypto_cryptostore_db_model_incomingroomkeyrequestentityrealmproxy.proxyState.getRow$realm().getIndex();
        }
        return false;
    }
}
