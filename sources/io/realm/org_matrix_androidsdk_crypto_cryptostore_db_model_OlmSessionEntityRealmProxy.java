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
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmSessionEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmSessionEntityFields;

public class org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy extends OlmSessionEntity implements RealmObjectProxy, org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxyInterface {
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();
    private OlmSessionEntityColumnInfo columnInfo;
    private ProxyState<OlmSessionEntity> proxyState;

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "OlmSessionEntity";
    }

    static final class OlmSessionEntityColumnInfo extends ColumnInfo {
        long deviceKeyIndex;
        long lastReceivedMessageTsIndex;
        long maxColumnIndexValue;
        long olmSessionDataIndex;
        long primaryKeyIndex;
        long sessionIdIndex;

        OlmSessionEntityColumnInfo(OsSchemaInfo osSchemaInfo) {
            super(5);
            OsObjectSchemaInfo objectSchemaInfo = osSchemaInfo.getObjectSchemaInfo(ClassNameHelper.INTERNAL_CLASS_NAME);
            String str = "primaryKey";
            this.primaryKeyIndex = addColumnDetails(str, str, objectSchemaInfo);
            String str2 = "sessionId";
            this.sessionIdIndex = addColumnDetails(str2, str2, objectSchemaInfo);
            String str3 = OlmSessionEntityFields.DEVICE_KEY;
            this.deviceKeyIndex = addColumnDetails(str3, str3, objectSchemaInfo);
            String str4 = OlmSessionEntityFields.OLM_SESSION_DATA;
            this.olmSessionDataIndex = addColumnDetails(str4, str4, objectSchemaInfo);
            String str5 = OlmSessionEntityFields.LAST_RECEIVED_MESSAGE_TS;
            this.lastReceivedMessageTsIndex = addColumnDetails(str5, str5, objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        OlmSessionEntityColumnInfo(ColumnInfo columnInfo, boolean z) {
            super(columnInfo, z);
            copy(columnInfo, this);
        }

        /* access modifiers changed from: protected */
        public final ColumnInfo copy(boolean z) {
            return new OlmSessionEntityColumnInfo(this, z);
        }

        /* access modifiers changed from: protected */
        public final void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2) {
            OlmSessionEntityColumnInfo olmSessionEntityColumnInfo = (OlmSessionEntityColumnInfo) columnInfo;
            OlmSessionEntityColumnInfo olmSessionEntityColumnInfo2 = (OlmSessionEntityColumnInfo) columnInfo2;
            olmSessionEntityColumnInfo2.primaryKeyIndex = olmSessionEntityColumnInfo.primaryKeyIndex;
            olmSessionEntityColumnInfo2.sessionIdIndex = olmSessionEntityColumnInfo.sessionIdIndex;
            olmSessionEntityColumnInfo2.deviceKeyIndex = olmSessionEntityColumnInfo.deviceKeyIndex;
            olmSessionEntityColumnInfo2.olmSessionDataIndex = olmSessionEntityColumnInfo.olmSessionDataIndex;
            olmSessionEntityColumnInfo2.lastReceivedMessageTsIndex = olmSessionEntityColumnInfo.lastReceivedMessageTsIndex;
            olmSessionEntityColumnInfo2.maxColumnIndexValue = olmSessionEntityColumnInfo.maxColumnIndexValue;
        }
    }

    public static String getSimpleClassName() {
        return ClassNameHelper.INTERNAL_CLASS_NAME;
    }

    org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy() {
        this.proxyState.setConstructionFinished();
    }

    public void realm$injectObjectContext() {
        if (this.proxyState == null) {
            RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
            this.columnInfo = (OlmSessionEntityColumnInfo) realmObjectContext.getColumnInfo();
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

    public String realmGet$sessionId() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.sessionIdIndex);
    }

    public void realmSet$sessionId(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.sessionIdIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.sessionIdIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.sessionIdIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.sessionIdIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public String realmGet$deviceKey() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.deviceKeyIndex);
    }

    public void realmSet$deviceKey(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.deviceKeyIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.deviceKeyIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.deviceKeyIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.deviceKeyIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public String realmGet$olmSessionData() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.olmSessionDataIndex);
    }

    public void realmSet$olmSessionData(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.olmSessionDataIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.olmSessionDataIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.olmSessionDataIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.olmSessionDataIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public long realmGet$lastReceivedMessageTs() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getLong(this.columnInfo.lastReceivedMessageTsIndex);
    }

    public void realmSet$lastReceivedMessageTs(long j) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            this.proxyState.getRow$realm().setLong(this.columnInfo.lastReceivedMessageTsIndex, j);
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            row$realm.getTable().setLong(this.columnInfo.lastReceivedMessageTsIndex, row$realm.getIndex(), j, true);
        }
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        Builder builder = new Builder(ClassNameHelper.INTERNAL_CLASS_NAME, 5, 0);
        Builder builder2 = builder;
        builder2.addPersistedProperty("primaryKey", RealmFieldType.STRING, true, true, true);
        builder2.addPersistedProperty("sessionId", RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty(OlmSessionEntityFields.DEVICE_KEY, RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty(OlmSessionEntityFields.OLM_SESSION_DATA, RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty(OlmSessionEntityFields.LAST_RECEIVED_MESSAGE_TS, RealmFieldType.INTEGER, false, false, true);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static OlmSessionEntityColumnInfo createColumnInfo(OsSchemaInfo osSchemaInfo) {
        return new OlmSessionEntityColumnInfo(osSchemaInfo);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0064  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00ce  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00e7  */
    public static OlmSessionEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject jSONObject, boolean z) throws JSONException {
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy;
        String str;
        String str2;
        String str3;
        String str4;
        List emptyList = Collections.emptyList();
        String str5 = "primaryKey";
        if (z) {
            Table table = realm.getTable(OlmSessionEntity.class);
            long findFirstString = !jSONObject.isNull(str5) ? table.findFirstString(((OlmSessionEntityColumnInfo) realm.getSchema().getColumnInfo(OlmSessionEntity.class)).primaryKeyIndex, jSONObject.getString(str5)) : -1;
            if (findFirstString != -1) {
                RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
                try {
                    realmObjectContext.set(realm, table.getUncheckedRow(findFirstString), realm.getSchema().getColumnInfo(OlmSessionEntity.class), false, Collections.emptyList());
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy();
                    if (org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy == null) {
                        if (!jSONObject.has(str5)) {
                            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'primaryKey'.");
                        } else if (jSONObject.isNull(str5)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy) realm.createObjectInternal(OlmSessionEntity.class, null, true, emptyList);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy) realm.createObjectInternal(OlmSessionEntity.class, jSONObject.getString(str5), true, emptyList);
                        }
                    }
                    org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy;
                    str = "sessionId";
                    if (jSONObject.has(str)) {
                        if (jSONObject.isNull(str)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$sessionId(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$sessionId(jSONObject.getString(str));
                        }
                    }
                    str2 = OlmSessionEntityFields.DEVICE_KEY;
                    if (jSONObject.has(str2)) {
                        if (jSONObject.isNull(str2)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$deviceKey(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$deviceKey(jSONObject.getString(str2));
                        }
                    }
                    str3 = OlmSessionEntityFields.OLM_SESSION_DATA;
                    if (jSONObject.has(str3)) {
                        if (jSONObject.isNull(str3)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$olmSessionData(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$olmSessionData(jSONObject.getString(str3));
                        }
                    }
                    str4 = OlmSessionEntityFields.LAST_RECEIVED_MESSAGE_TS;
                    if (jSONObject.has(str4)) {
                        if (!jSONObject.isNull(str4)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$lastReceivedMessageTs(jSONObject.getLong(str4));
                        } else {
                            throw new IllegalArgumentException("Trying to set non-nullable field 'lastReceivedMessageTs' to null.");
                        }
                    }
                    return org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy;
                } finally {
                    realmObjectContext.clear();
                }
            }
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy = null;
        if (org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy == null) {
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface2 = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy;
        str = "sessionId";
        if (jSONObject.has(str)) {
        }
        str2 = OlmSessionEntityFields.DEVICE_KEY;
        if (jSONObject.has(str2)) {
        }
        str3 = OlmSessionEntityFields.OLM_SESSION_DATA;
        if (jSONObject.has(str3)) {
        }
        str4 = OlmSessionEntityFields.LAST_RECEIVED_MESSAGE_TS;
        if (jSONObject.has(str4)) {
        }
        return org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy;
    }

    public static OlmSessionEntity createUsingJsonStream(Realm realm, JsonReader jsonReader) throws IOException {
        OlmSessionEntity olmSessionEntity = new OlmSessionEntity();
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface = olmSessionEntity;
        jsonReader.beginObject();
        boolean z = false;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if (nextName.equals("primaryKey")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$primaryKey(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$primaryKey(null);
                }
                z = true;
            } else if (nextName.equals("sessionId")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$sessionId(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$sessionId(null);
                }
            } else if (nextName.equals(OlmSessionEntityFields.DEVICE_KEY)) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$deviceKey(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$deviceKey(null);
                }
            } else if (nextName.equals(OlmSessionEntityFields.OLM_SESSION_DATA)) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$olmSessionData(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$olmSessionData(null);
                }
            } else if (!nextName.equals(OlmSessionEntityFields.LAST_RECEIVED_MESSAGE_TS)) {
                jsonReader.skipValue();
            } else if (jsonReader.peek() != JsonToken.NULL) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$lastReceivedMessageTs(jsonReader.nextLong());
            } else {
                jsonReader.skipValue();
                throw new IllegalArgumentException("Trying to set non-nullable field 'lastReceivedMessageTs' to null.");
            }
        }
        jsonReader.endObject();
        if (z) {
            return (OlmSessionEntity) realm.copyToRealm(olmSessionEntity, new ImportFlag[0]);
        }
        throw new IllegalArgumentException("JSON object doesn't have the primary key field 'primaryKey'.");
    }

    private static org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy newProxyInstance(BaseRealm baseRealm, Row row) {
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        realmObjectContext.set(baseRealm, row, baseRealm.getSchema().getColumnInfo(OlmSessionEntity.class), false, Collections.emptyList());
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy();
        realmObjectContext.clear();
        return org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x009a  */
    public static OlmSessionEntity copyOrUpdate(Realm realm, OlmSessionEntityColumnInfo olmSessionEntityColumnInfo, OlmSessionEntity olmSessionEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        boolean z2;
        if (olmSessionEntity instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) olmSessionEntity;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null) {
                BaseRealm realm$realm = realmObjectProxy.realmGet$proxyState().getRealm$realm();
                if (realm$realm.threadId != realm.threadId) {
                    throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
                } else if (realm$realm.getPath().equals(realm.getPath())) {
                    return olmSessionEntity;
                }
            }
        }
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        RealmObjectProxy realmObjectProxy2 = (RealmObjectProxy) map.get(olmSessionEntity);
        if (realmObjectProxy2 != null) {
            return (OlmSessionEntity) realmObjectProxy2;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy = null;
        if (z) {
            Table table = realm.getTable(OlmSessionEntity.class);
            long findFirstString = table.findFirstString(olmSessionEntityColumnInfo.primaryKeyIndex, olmSessionEntity.realmGet$primaryKey());
            if (findFirstString == -1) {
                z2 = false;
                return !z2 ? update(realm, olmSessionEntityColumnInfo, org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy, olmSessionEntity, map, set) : copy(realm, olmSessionEntityColumnInfo, olmSessionEntity, z, map, set);
            }
            try {
                realmObjectContext.set(realm, table.getUncheckedRow(findFirstString), olmSessionEntityColumnInfo, false, Collections.emptyList());
                org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy();
                map.put(olmSessionEntity, org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy);
            } finally {
                realmObjectContext.clear();
            }
        }
        z2 = z;
        return !z2 ? update(realm, olmSessionEntityColumnInfo, org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy, olmSessionEntity, map, set) : copy(realm, olmSessionEntityColumnInfo, olmSessionEntity, z, map, set);
    }

    public static OlmSessionEntity copy(Realm realm, OlmSessionEntityColumnInfo olmSessionEntityColumnInfo, OlmSessionEntity olmSessionEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        RealmObjectProxy realmObjectProxy = (RealmObjectProxy) map.get(olmSessionEntity);
        if (realmObjectProxy != null) {
            return (OlmSessionEntity) realmObjectProxy;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface = olmSessionEntity;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(OlmSessionEntity.class), olmSessionEntityColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addString(olmSessionEntityColumnInfo.primaryKeyIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$primaryKey());
        osObjectBuilder.addString(olmSessionEntityColumnInfo.sessionIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$sessionId());
        osObjectBuilder.addString(olmSessionEntityColumnInfo.deviceKeyIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$deviceKey());
        osObjectBuilder.addString(olmSessionEntityColumnInfo.olmSessionDataIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$olmSessionData());
        osObjectBuilder.addInteger(olmSessionEntityColumnInfo.lastReceivedMessageTsIndex, Long.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$lastReceivedMessageTs()));
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy newProxyInstance = newProxyInstance(realm, osObjectBuilder.createNewObject());
        map.put(olmSessionEntity, newProxyInstance);
        return newProxyInstance;
    }

    public static long insert(Realm realm, OlmSessionEntity olmSessionEntity, Map<RealmModel, Long> map) {
        long j;
        OlmSessionEntity olmSessionEntity2 = olmSessionEntity;
        if (olmSessionEntity2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) olmSessionEntity2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(OlmSessionEntity.class);
        long nativePtr = table.getNativePtr();
        OlmSessionEntityColumnInfo olmSessionEntityColumnInfo = (OlmSessionEntityColumnInfo) realm.getSchema().getColumnInfo(OlmSessionEntity.class);
        long j2 = olmSessionEntityColumnInfo.primaryKeyIndex;
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface = olmSessionEntity2;
        String realmGet$primaryKey = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$primaryKey();
        long nativeFindFirstString = realmGet$primaryKey != null ? Table.nativeFindFirstString(nativePtr, j2, realmGet$primaryKey) : -1;
        if (nativeFindFirstString == -1) {
            j = OsObject.createRowWithPrimaryKey(table, j2, realmGet$primaryKey);
        } else {
            Table.throwDuplicatePrimaryKeyException(realmGet$primaryKey);
            j = nativeFindFirstString;
        }
        map.put(olmSessionEntity2, Long.valueOf(j));
        String realmGet$sessionId = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$sessionId();
        if (realmGet$sessionId != null) {
            Table.nativeSetString(nativePtr, olmSessionEntityColumnInfo.sessionIdIndex, j, realmGet$sessionId, false);
        }
        String realmGet$deviceKey = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$deviceKey();
        if (realmGet$deviceKey != null) {
            Table.nativeSetString(nativePtr, olmSessionEntityColumnInfo.deviceKeyIndex, j, realmGet$deviceKey, false);
        }
        String realmGet$olmSessionData = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$olmSessionData();
        if (realmGet$olmSessionData != null) {
            Table.nativeSetString(nativePtr, olmSessionEntityColumnInfo.olmSessionDataIndex, j, realmGet$olmSessionData, false);
        }
        Table.nativeSetLong(nativePtr, olmSessionEntityColumnInfo.lastReceivedMessageTsIndex, j, org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$lastReceivedMessageTs(), false);
        return j;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        long j2;
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(OlmSessionEntity.class);
        long nativePtr = table.getNativePtr();
        OlmSessionEntityColumnInfo olmSessionEntityColumnInfo = (OlmSessionEntityColumnInfo) realm.getSchema().getColumnInfo(OlmSessionEntity.class);
        long j3 = olmSessionEntityColumnInfo.primaryKeyIndex;
        while (it.hasNext()) {
            OlmSessionEntity olmSessionEntity = (OlmSessionEntity) it.next();
            if (!map2.containsKey(olmSessionEntity)) {
                if (olmSessionEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) olmSessionEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(olmSessionEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface = olmSessionEntity;
                String realmGet$primaryKey = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$primaryKey();
                long nativeFindFirstString = realmGet$primaryKey != null ? Table.nativeFindFirstString(nativePtr, j3, realmGet$primaryKey) : -1;
                if (nativeFindFirstString == -1) {
                    j = OsObject.createRowWithPrimaryKey(table, j3, realmGet$primaryKey);
                } else {
                    Table.throwDuplicatePrimaryKeyException(realmGet$primaryKey);
                    j = nativeFindFirstString;
                }
                map2.put(olmSessionEntity, Long.valueOf(j));
                String realmGet$sessionId = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$sessionId();
                if (realmGet$sessionId != null) {
                    j2 = j3;
                    Table.nativeSetString(nativePtr, olmSessionEntityColumnInfo.sessionIdIndex, j, realmGet$sessionId, false);
                } else {
                    j2 = j3;
                }
                String realmGet$deviceKey = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$deviceKey();
                if (realmGet$deviceKey != null) {
                    Table.nativeSetString(nativePtr, olmSessionEntityColumnInfo.deviceKeyIndex, j, realmGet$deviceKey, false);
                }
                String realmGet$olmSessionData = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$olmSessionData();
                if (realmGet$olmSessionData != null) {
                    Table.nativeSetString(nativePtr, olmSessionEntityColumnInfo.olmSessionDataIndex, j, realmGet$olmSessionData, false);
                }
                Table.nativeSetLong(nativePtr, olmSessionEntityColumnInfo.lastReceivedMessageTsIndex, j, org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$lastReceivedMessageTs(), false);
                j3 = j2;
            }
        }
    }

    public static long insertOrUpdate(Realm realm, OlmSessionEntity olmSessionEntity, Map<RealmModel, Long> map) {
        OlmSessionEntity olmSessionEntity2 = olmSessionEntity;
        if (olmSessionEntity2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) olmSessionEntity2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(OlmSessionEntity.class);
        long nativePtr = table.getNativePtr();
        OlmSessionEntityColumnInfo olmSessionEntityColumnInfo = (OlmSessionEntityColumnInfo) realm.getSchema().getColumnInfo(OlmSessionEntity.class);
        long j = olmSessionEntityColumnInfo.primaryKeyIndex;
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface = olmSessionEntity2;
        String realmGet$primaryKey = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$primaryKey();
        long nativeFindFirstString = realmGet$primaryKey != null ? Table.nativeFindFirstString(nativePtr, j, realmGet$primaryKey) : -1;
        long createRowWithPrimaryKey = nativeFindFirstString == -1 ? OsObject.createRowWithPrimaryKey(table, j, realmGet$primaryKey) : nativeFindFirstString;
        map.put(olmSessionEntity2, Long.valueOf(createRowWithPrimaryKey));
        String realmGet$sessionId = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$sessionId();
        if (realmGet$sessionId != null) {
            Table.nativeSetString(nativePtr, olmSessionEntityColumnInfo.sessionIdIndex, createRowWithPrimaryKey, realmGet$sessionId, false);
        } else {
            Table.nativeSetNull(nativePtr, olmSessionEntityColumnInfo.sessionIdIndex, createRowWithPrimaryKey, false);
        }
        String realmGet$deviceKey = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$deviceKey();
        if (realmGet$deviceKey != null) {
            Table.nativeSetString(nativePtr, olmSessionEntityColumnInfo.deviceKeyIndex, createRowWithPrimaryKey, realmGet$deviceKey, false);
        } else {
            Table.nativeSetNull(nativePtr, olmSessionEntityColumnInfo.deviceKeyIndex, createRowWithPrimaryKey, false);
        }
        String realmGet$olmSessionData = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$olmSessionData();
        if (realmGet$olmSessionData != null) {
            Table.nativeSetString(nativePtr, olmSessionEntityColumnInfo.olmSessionDataIndex, createRowWithPrimaryKey, realmGet$olmSessionData, false);
        } else {
            Table.nativeSetNull(nativePtr, olmSessionEntityColumnInfo.olmSessionDataIndex, createRowWithPrimaryKey, false);
        }
        Table.nativeSetLong(nativePtr, olmSessionEntityColumnInfo.lastReceivedMessageTsIndex, createRowWithPrimaryKey, org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$lastReceivedMessageTs(), false);
        return createRowWithPrimaryKey;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(OlmSessionEntity.class);
        long nativePtr = table.getNativePtr();
        OlmSessionEntityColumnInfo olmSessionEntityColumnInfo = (OlmSessionEntityColumnInfo) realm.getSchema().getColumnInfo(OlmSessionEntity.class);
        long j2 = olmSessionEntityColumnInfo.primaryKeyIndex;
        while (it.hasNext()) {
            OlmSessionEntity olmSessionEntity = (OlmSessionEntity) it.next();
            if (!map2.containsKey(olmSessionEntity)) {
                if (olmSessionEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) olmSessionEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(olmSessionEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface = olmSessionEntity;
                String realmGet$primaryKey = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$primaryKey();
                long nativeFindFirstString = realmGet$primaryKey != null ? Table.nativeFindFirstString(nativePtr, j2, realmGet$primaryKey) : -1;
                long createRowWithPrimaryKey = nativeFindFirstString == -1 ? OsObject.createRowWithPrimaryKey(table, j2, realmGet$primaryKey) : nativeFindFirstString;
                map2.put(olmSessionEntity, Long.valueOf(createRowWithPrimaryKey));
                String realmGet$sessionId = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$sessionId();
                if (realmGet$sessionId != null) {
                    j = j2;
                    Table.nativeSetString(nativePtr, olmSessionEntityColumnInfo.sessionIdIndex, createRowWithPrimaryKey, realmGet$sessionId, false);
                } else {
                    j = j2;
                    Table.nativeSetNull(nativePtr, olmSessionEntityColumnInfo.sessionIdIndex, createRowWithPrimaryKey, false);
                }
                String realmGet$deviceKey = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$deviceKey();
                if (realmGet$deviceKey != null) {
                    Table.nativeSetString(nativePtr, olmSessionEntityColumnInfo.deviceKeyIndex, createRowWithPrimaryKey, realmGet$deviceKey, false);
                } else {
                    Table.nativeSetNull(nativePtr, olmSessionEntityColumnInfo.deviceKeyIndex, createRowWithPrimaryKey, false);
                }
                String realmGet$olmSessionData = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$olmSessionData();
                if (realmGet$olmSessionData != null) {
                    Table.nativeSetString(nativePtr, olmSessionEntityColumnInfo.olmSessionDataIndex, createRowWithPrimaryKey, realmGet$olmSessionData, false);
                } else {
                    Table.nativeSetNull(nativePtr, olmSessionEntityColumnInfo.olmSessionDataIndex, createRowWithPrimaryKey, false);
                }
                Table.nativeSetLong(nativePtr, olmSessionEntityColumnInfo.lastReceivedMessageTsIndex, createRowWithPrimaryKey, org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmGet$lastReceivedMessageTs(), false);
                j2 = j;
            }
        }
    }

    public static OlmSessionEntity createDetachedCopy(OlmSessionEntity olmSessionEntity, int i, int i2, Map<RealmModel, CacheData<RealmModel>> map) {
        OlmSessionEntity olmSessionEntity2;
        if (i > i2 || olmSessionEntity == null) {
            return null;
        }
        CacheData cacheData = (CacheData) map.get(olmSessionEntity);
        if (cacheData == null) {
            olmSessionEntity2 = new OlmSessionEntity();
            map.put(olmSessionEntity, new CacheData(i, olmSessionEntity2));
        } else if (i >= cacheData.minDepth) {
            return (OlmSessionEntity) cacheData.object;
        } else {
            OlmSessionEntity olmSessionEntity3 = (OlmSessionEntity) cacheData.object;
            cacheData.minDepth = i;
            olmSessionEntity2 = olmSessionEntity3;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface = olmSessionEntity2;
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface2 = olmSessionEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$primaryKey(org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface2.realmGet$primaryKey());
        org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$sessionId(org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface2.realmGet$sessionId());
        org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$deviceKey(org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface2.realmGet$deviceKey());
        org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$olmSessionData(org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface2.realmGet$olmSessionData());
        org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface.realmSet$lastReceivedMessageTs(org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface2.realmGet$lastReceivedMessageTs());
        return olmSessionEntity2;
    }

    static OlmSessionEntity update(Realm realm, OlmSessionEntityColumnInfo olmSessionEntityColumnInfo, OlmSessionEntity olmSessionEntity, OlmSessionEntity olmSessionEntity2, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface = olmSessionEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface2 = olmSessionEntity2;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(OlmSessionEntity.class), olmSessionEntityColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addString(olmSessionEntityColumnInfo.primaryKeyIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface2.realmGet$primaryKey());
        osObjectBuilder.addString(olmSessionEntityColumnInfo.sessionIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface2.realmGet$sessionId());
        osObjectBuilder.addString(olmSessionEntityColumnInfo.deviceKeyIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface2.realmGet$deviceKey());
        osObjectBuilder.addString(olmSessionEntityColumnInfo.olmSessionDataIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface2.realmGet$olmSessionData());
        osObjectBuilder.addInteger(olmSessionEntityColumnInfo.lastReceivedMessageTsIndex, Long.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxyinterface2.realmGet$lastReceivedMessageTs()));
        osObjectBuilder.updateExistingObject();
        return olmSessionEntity;
    }

    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder sb = new StringBuilder("OlmSessionEntity = proxy[");
        sb.append("{primaryKey:");
        sb.append(realmGet$primaryKey());
        String str = "}";
        sb.append(str);
        String str2 = ",";
        sb.append(str2);
        sb.append("{sessionId:");
        String str3 = "null";
        sb.append(realmGet$sessionId() != null ? realmGet$sessionId() : str3);
        sb.append(str);
        sb.append(str2);
        sb.append("{deviceKey:");
        sb.append(realmGet$deviceKey() != null ? realmGet$deviceKey() : str3);
        sb.append(str);
        sb.append(str2);
        sb.append("{olmSessionData:");
        if (realmGet$olmSessionData() != null) {
            str3 = realmGet$olmSessionData();
        }
        sb.append(str3);
        sb.append(str);
        sb.append(str2);
        sb.append("{lastReceivedMessageTs:");
        sb.append(realmGet$lastReceivedMessageTs());
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
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy) obj;
        String path = this.proxyState.getRealm$realm().getPath();
        String path2 = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy.proxyState.getRealm$realm().getPath();
        if (path == null ? path2 != null : !path.equals(path2)) {
            return false;
        }
        String name = this.proxyState.getRow$realm().getTable().getName();
        String name2 = org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy.proxyState.getRow$realm().getTable().getName();
        if (name == null ? name2 == null : name.equals(name2)) {
            return this.proxyState.getRow$realm().getIndex() == org_matrix_androidsdk_crypto_cryptostore_db_model_olmsessionentityrealmproxy.proxyState.getRow$realm().getIndex();
        }
        return false;
    }
}
