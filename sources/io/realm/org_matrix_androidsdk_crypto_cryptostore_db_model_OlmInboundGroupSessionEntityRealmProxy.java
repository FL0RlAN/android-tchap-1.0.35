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
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmInboundGroupSessionEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmInboundGroupSessionEntityFields;

public class org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy extends OlmInboundGroupSessionEntity implements RealmObjectProxy, org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface {
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();
    private OlmInboundGroupSessionEntityColumnInfo columnInfo;
    private ProxyState<OlmInboundGroupSessionEntity> proxyState;

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "OlmInboundGroupSessionEntity";
    }

    static final class OlmInboundGroupSessionEntityColumnInfo extends ColumnInfo {
        long backedUpIndex;
        long maxColumnIndexValue;
        long olmInboundGroupSessionDataIndex;
        long primaryKeyIndex;
        long senderKeyIndex;
        long sessionIdIndex;

        OlmInboundGroupSessionEntityColumnInfo(OsSchemaInfo osSchemaInfo) {
            super(5);
            OsObjectSchemaInfo objectSchemaInfo = osSchemaInfo.getObjectSchemaInfo(ClassNameHelper.INTERNAL_CLASS_NAME);
            String str = "primaryKey";
            this.primaryKeyIndex = addColumnDetails(str, str, objectSchemaInfo);
            String str2 = "sessionId";
            this.sessionIdIndex = addColumnDetails(str2, str2, objectSchemaInfo);
            String str3 = OlmInboundGroupSessionEntityFields.SENDER_KEY;
            this.senderKeyIndex = addColumnDetails(str3, str3, objectSchemaInfo);
            String str4 = OlmInboundGroupSessionEntityFields.OLM_INBOUND_GROUP_SESSION_DATA;
            this.olmInboundGroupSessionDataIndex = addColumnDetails(str4, str4, objectSchemaInfo);
            String str5 = OlmInboundGroupSessionEntityFields.BACKED_UP;
            this.backedUpIndex = addColumnDetails(str5, str5, objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        OlmInboundGroupSessionEntityColumnInfo(ColumnInfo columnInfo, boolean z) {
            super(columnInfo, z);
            copy(columnInfo, this);
        }

        /* access modifiers changed from: protected */
        public final ColumnInfo copy(boolean z) {
            return new OlmInboundGroupSessionEntityColumnInfo(this, z);
        }

        /* access modifiers changed from: protected */
        public final void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2) {
            OlmInboundGroupSessionEntityColumnInfo olmInboundGroupSessionEntityColumnInfo = (OlmInboundGroupSessionEntityColumnInfo) columnInfo;
            OlmInboundGroupSessionEntityColumnInfo olmInboundGroupSessionEntityColumnInfo2 = (OlmInboundGroupSessionEntityColumnInfo) columnInfo2;
            olmInboundGroupSessionEntityColumnInfo2.primaryKeyIndex = olmInboundGroupSessionEntityColumnInfo.primaryKeyIndex;
            olmInboundGroupSessionEntityColumnInfo2.sessionIdIndex = olmInboundGroupSessionEntityColumnInfo.sessionIdIndex;
            olmInboundGroupSessionEntityColumnInfo2.senderKeyIndex = olmInboundGroupSessionEntityColumnInfo.senderKeyIndex;
            olmInboundGroupSessionEntityColumnInfo2.olmInboundGroupSessionDataIndex = olmInboundGroupSessionEntityColumnInfo.olmInboundGroupSessionDataIndex;
            olmInboundGroupSessionEntityColumnInfo2.backedUpIndex = olmInboundGroupSessionEntityColumnInfo.backedUpIndex;
            olmInboundGroupSessionEntityColumnInfo2.maxColumnIndexValue = olmInboundGroupSessionEntityColumnInfo.maxColumnIndexValue;
        }
    }

    public static String getSimpleClassName() {
        return ClassNameHelper.INTERNAL_CLASS_NAME;
    }

    org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy() {
        this.proxyState.setConstructionFinished();
    }

    public void realm$injectObjectContext() {
        if (this.proxyState == null) {
            RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
            this.columnInfo = (OlmInboundGroupSessionEntityColumnInfo) realmObjectContext.getColumnInfo();
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

    public String realmGet$senderKey() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.senderKeyIndex);
    }

    public void realmSet$senderKey(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.senderKeyIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.senderKeyIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.senderKeyIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.senderKeyIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public String realmGet$olmInboundGroupSessionData() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.olmInboundGroupSessionDataIndex);
    }

    public void realmSet$olmInboundGroupSessionData(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.olmInboundGroupSessionDataIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.olmInboundGroupSessionDataIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.olmInboundGroupSessionDataIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.olmInboundGroupSessionDataIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public boolean realmGet$backedUp() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getBoolean(this.columnInfo.backedUpIndex);
    }

    public void realmSet$backedUp(boolean z) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            this.proxyState.getRow$realm().setBoolean(this.columnInfo.backedUpIndex, z);
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            row$realm.getTable().setBoolean(this.columnInfo.backedUpIndex, row$realm.getIndex(), z, true);
        }
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        Builder builder = new Builder(ClassNameHelper.INTERNAL_CLASS_NAME, 5, 0);
        Builder builder2 = builder;
        builder2.addPersistedProperty("primaryKey", RealmFieldType.STRING, true, true, false);
        builder2.addPersistedProperty("sessionId", RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty(OlmInboundGroupSessionEntityFields.SENDER_KEY, RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty(OlmInboundGroupSessionEntityFields.OLM_INBOUND_GROUP_SESSION_DATA, RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty(OlmInboundGroupSessionEntityFields.BACKED_UP, RealmFieldType.BOOLEAN, false, false, true);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static OlmInboundGroupSessionEntityColumnInfo createColumnInfo(OsSchemaInfo osSchemaInfo) {
        return new OlmInboundGroupSessionEntityColumnInfo(osSchemaInfo);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ea  */
    public static OlmInboundGroupSessionEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject jSONObject, boolean z) throws JSONException {
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy;
        String str;
        String str2;
        String str3;
        String str4;
        long j;
        List emptyList = Collections.emptyList();
        String str5 = "primaryKey";
        if (z) {
            Table table = realm.getTable(OlmInboundGroupSessionEntity.class);
            long j2 = ((OlmInboundGroupSessionEntityColumnInfo) realm.getSchema().getColumnInfo(OlmInboundGroupSessionEntity.class)).primaryKeyIndex;
            if (jSONObject.isNull(str5)) {
                j = table.findFirstNull(j2);
            } else {
                j = table.findFirstString(j2, jSONObject.getString(str5));
            }
            if (j != -1) {
                RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
                try {
                    realmObjectContext.set(realm, table.getUncheckedRow(j), realm.getSchema().getColumnInfo(OlmInboundGroupSessionEntity.class), false, Collections.emptyList());
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy();
                    if (org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy == null) {
                        if (!jSONObject.has(str5)) {
                            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'primaryKey'.");
                        } else if (jSONObject.isNull(str5)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy) realm.createObjectInternal(OlmInboundGroupSessionEntity.class, null, true, emptyList);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy) realm.createObjectInternal(OlmInboundGroupSessionEntity.class, jSONObject.getString(str5), true, emptyList);
                        }
                    }
                    org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy;
                    str = "sessionId";
                    if (jSONObject.has(str)) {
                        if (jSONObject.isNull(str)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$sessionId(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$sessionId(jSONObject.getString(str));
                        }
                    }
                    str2 = OlmInboundGroupSessionEntityFields.SENDER_KEY;
                    if (jSONObject.has(str2)) {
                        if (jSONObject.isNull(str2)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$senderKey(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$senderKey(jSONObject.getString(str2));
                        }
                    }
                    str3 = OlmInboundGroupSessionEntityFields.OLM_INBOUND_GROUP_SESSION_DATA;
                    if (jSONObject.has(str3)) {
                        if (jSONObject.isNull(str3)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$olmInboundGroupSessionData(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$olmInboundGroupSessionData(jSONObject.getString(str3));
                        }
                    }
                    str4 = OlmInboundGroupSessionEntityFields.BACKED_UP;
                    if (jSONObject.has(str4)) {
                        if (!jSONObject.isNull(str4)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$backedUp(jSONObject.getBoolean(str4));
                        } else {
                            throw new IllegalArgumentException("Trying to set non-nullable field 'backedUp' to null.");
                        }
                    }
                    return org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy;
                } finally {
                    realmObjectContext.clear();
                }
            }
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy = null;
        if (org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy == null) {
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface2 = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy;
        str = "sessionId";
        if (jSONObject.has(str)) {
        }
        str2 = OlmInboundGroupSessionEntityFields.SENDER_KEY;
        if (jSONObject.has(str2)) {
        }
        str3 = OlmInboundGroupSessionEntityFields.OLM_INBOUND_GROUP_SESSION_DATA;
        if (jSONObject.has(str3)) {
        }
        str4 = OlmInboundGroupSessionEntityFields.BACKED_UP;
        if (jSONObject.has(str4)) {
        }
        return org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy;
    }

    public static OlmInboundGroupSessionEntity createUsingJsonStream(Realm realm, JsonReader jsonReader) throws IOException {
        OlmInboundGroupSessionEntity olmInboundGroupSessionEntity = new OlmInboundGroupSessionEntity();
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface = olmInboundGroupSessionEntity;
        jsonReader.beginObject();
        boolean z = false;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if (nextName.equals("primaryKey")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$primaryKey(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$primaryKey(null);
                }
                z = true;
            } else if (nextName.equals("sessionId")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$sessionId(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$sessionId(null);
                }
            } else if (nextName.equals(OlmInboundGroupSessionEntityFields.SENDER_KEY)) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$senderKey(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$senderKey(null);
                }
            } else if (nextName.equals(OlmInboundGroupSessionEntityFields.OLM_INBOUND_GROUP_SESSION_DATA)) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$olmInboundGroupSessionData(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$olmInboundGroupSessionData(null);
                }
            } else if (!nextName.equals(OlmInboundGroupSessionEntityFields.BACKED_UP)) {
                jsonReader.skipValue();
            } else if (jsonReader.peek() != JsonToken.NULL) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$backedUp(jsonReader.nextBoolean());
            } else {
                jsonReader.skipValue();
                throw new IllegalArgumentException("Trying to set non-nullable field 'backedUp' to null.");
            }
        }
        jsonReader.endObject();
        if (z) {
            return (OlmInboundGroupSessionEntity) realm.copyToRealm(olmInboundGroupSessionEntity, new ImportFlag[0]);
        }
        throw new IllegalArgumentException("JSON object doesn't have the primary key field 'primaryKey'.");
    }

    private static org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy newProxyInstance(BaseRealm baseRealm, Row row) {
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        realmObjectContext.set(baseRealm, row, baseRealm.getSchema().getColumnInfo(OlmInboundGroupSessionEntity.class), false, Collections.emptyList());
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy();
        realmObjectContext.clear();
        return org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a1  */
    public static OlmInboundGroupSessionEntity copyOrUpdate(Realm realm, OlmInboundGroupSessionEntityColumnInfo olmInboundGroupSessionEntityColumnInfo, OlmInboundGroupSessionEntity olmInboundGroupSessionEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        boolean z2;
        long j;
        if (olmInboundGroupSessionEntity instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) olmInboundGroupSessionEntity;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null) {
                BaseRealm realm$realm = realmObjectProxy.realmGet$proxyState().getRealm$realm();
                if (realm$realm.threadId != realm.threadId) {
                    throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
                } else if (realm$realm.getPath().equals(realm.getPath())) {
                    return olmInboundGroupSessionEntity;
                }
            }
        }
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        RealmObjectProxy realmObjectProxy2 = (RealmObjectProxy) map.get(olmInboundGroupSessionEntity);
        if (realmObjectProxy2 != null) {
            return (OlmInboundGroupSessionEntity) realmObjectProxy2;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy = null;
        if (z) {
            Table table = realm.getTable(OlmInboundGroupSessionEntity.class);
            long j2 = olmInboundGroupSessionEntityColumnInfo.primaryKeyIndex;
            String realmGet$primaryKey = olmInboundGroupSessionEntity.realmGet$primaryKey();
            if (realmGet$primaryKey == null) {
                j = table.findFirstNull(j2);
            } else {
                j = table.findFirstString(j2, realmGet$primaryKey);
            }
            if (j == -1) {
                z2 = false;
                return !z2 ? update(realm, olmInboundGroupSessionEntityColumnInfo, org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy, olmInboundGroupSessionEntity, map, set) : copy(realm, olmInboundGroupSessionEntityColumnInfo, olmInboundGroupSessionEntity, z, map, set);
            }
            try {
                realmObjectContext.set(realm, table.getUncheckedRow(j), olmInboundGroupSessionEntityColumnInfo, false, Collections.emptyList());
                org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy();
                map.put(olmInboundGroupSessionEntity, org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy);
            } finally {
                realmObjectContext.clear();
            }
        }
        z2 = z;
        return !z2 ? update(realm, olmInboundGroupSessionEntityColumnInfo, org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy, olmInboundGroupSessionEntity, map, set) : copy(realm, olmInboundGroupSessionEntityColumnInfo, olmInboundGroupSessionEntity, z, map, set);
    }

    public static OlmInboundGroupSessionEntity copy(Realm realm, OlmInboundGroupSessionEntityColumnInfo olmInboundGroupSessionEntityColumnInfo, OlmInboundGroupSessionEntity olmInboundGroupSessionEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        RealmObjectProxy realmObjectProxy = (RealmObjectProxy) map.get(olmInboundGroupSessionEntity);
        if (realmObjectProxy != null) {
            return (OlmInboundGroupSessionEntity) realmObjectProxy;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface = olmInboundGroupSessionEntity;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(OlmInboundGroupSessionEntity.class), olmInboundGroupSessionEntityColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addString(olmInboundGroupSessionEntityColumnInfo.primaryKeyIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$primaryKey());
        osObjectBuilder.addString(olmInboundGroupSessionEntityColumnInfo.sessionIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$sessionId());
        osObjectBuilder.addString(olmInboundGroupSessionEntityColumnInfo.senderKeyIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$senderKey());
        osObjectBuilder.addString(olmInboundGroupSessionEntityColumnInfo.olmInboundGroupSessionDataIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$olmInboundGroupSessionData());
        osObjectBuilder.addBoolean(olmInboundGroupSessionEntityColumnInfo.backedUpIndex, Boolean.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$backedUp()));
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy newProxyInstance = newProxyInstance(realm, osObjectBuilder.createNewObject());
        map.put(olmInboundGroupSessionEntity, newProxyInstance);
        return newProxyInstance;
    }

    public static long insert(Realm realm, OlmInboundGroupSessionEntity olmInboundGroupSessionEntity, Map<RealmModel, Long> map) {
        long j;
        long j2;
        OlmInboundGroupSessionEntity olmInboundGroupSessionEntity2 = olmInboundGroupSessionEntity;
        if (olmInboundGroupSessionEntity2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) olmInboundGroupSessionEntity2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(OlmInboundGroupSessionEntity.class);
        long nativePtr = table.getNativePtr();
        OlmInboundGroupSessionEntityColumnInfo olmInboundGroupSessionEntityColumnInfo = (OlmInboundGroupSessionEntityColumnInfo) realm.getSchema().getColumnInfo(OlmInboundGroupSessionEntity.class);
        long j3 = olmInboundGroupSessionEntityColumnInfo.primaryKeyIndex;
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface = olmInboundGroupSessionEntity2;
        String realmGet$primaryKey = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$primaryKey();
        if (realmGet$primaryKey == null) {
            j = Table.nativeFindFirstNull(nativePtr, j3);
        } else {
            j = Table.nativeFindFirstString(nativePtr, j3, realmGet$primaryKey);
        }
        if (j == -1) {
            j2 = OsObject.createRowWithPrimaryKey(table, j3, realmGet$primaryKey);
        } else {
            Table.throwDuplicatePrimaryKeyException(realmGet$primaryKey);
            j2 = j;
        }
        map.put(olmInboundGroupSessionEntity2, Long.valueOf(j2));
        String realmGet$sessionId = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$sessionId();
        if (realmGet$sessionId != null) {
            Table.nativeSetString(nativePtr, olmInboundGroupSessionEntityColumnInfo.sessionIdIndex, j2, realmGet$sessionId, false);
        }
        String realmGet$senderKey = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$senderKey();
        if (realmGet$senderKey != null) {
            Table.nativeSetString(nativePtr, olmInboundGroupSessionEntityColumnInfo.senderKeyIndex, j2, realmGet$senderKey, false);
        }
        String realmGet$olmInboundGroupSessionData = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$olmInboundGroupSessionData();
        if (realmGet$olmInboundGroupSessionData != null) {
            Table.nativeSetString(nativePtr, olmInboundGroupSessionEntityColumnInfo.olmInboundGroupSessionDataIndex, j2, realmGet$olmInboundGroupSessionData, false);
        }
        Table.nativeSetBoolean(nativePtr, olmInboundGroupSessionEntityColumnInfo.backedUpIndex, j2, org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$backedUp(), false);
        return j2;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        long j2;
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(OlmInboundGroupSessionEntity.class);
        long nativePtr = table.getNativePtr();
        OlmInboundGroupSessionEntityColumnInfo olmInboundGroupSessionEntityColumnInfo = (OlmInboundGroupSessionEntityColumnInfo) realm.getSchema().getColumnInfo(OlmInboundGroupSessionEntity.class);
        long j3 = olmInboundGroupSessionEntityColumnInfo.primaryKeyIndex;
        while (it.hasNext()) {
            OlmInboundGroupSessionEntity olmInboundGroupSessionEntity = (OlmInboundGroupSessionEntity) it.next();
            if (!map2.containsKey(olmInboundGroupSessionEntity)) {
                if (olmInboundGroupSessionEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) olmInboundGroupSessionEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(olmInboundGroupSessionEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface = olmInboundGroupSessionEntity;
                String realmGet$primaryKey = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$primaryKey();
                if (realmGet$primaryKey == null) {
                    j = Table.nativeFindFirstNull(nativePtr, j3);
                } else {
                    j = Table.nativeFindFirstString(nativePtr, j3, realmGet$primaryKey);
                }
                if (j == -1) {
                    j2 = OsObject.createRowWithPrimaryKey(table, j3, realmGet$primaryKey);
                } else {
                    Table.throwDuplicatePrimaryKeyException(realmGet$primaryKey);
                    j2 = j;
                }
                map2.put(olmInboundGroupSessionEntity, Long.valueOf(j2));
                String realmGet$sessionId = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$sessionId();
                if (realmGet$sessionId != null) {
                    Table.nativeSetString(nativePtr, olmInboundGroupSessionEntityColumnInfo.sessionIdIndex, j2, realmGet$sessionId, false);
                }
                String realmGet$senderKey = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$senderKey();
                if (realmGet$senderKey != null) {
                    Table.nativeSetString(nativePtr, olmInboundGroupSessionEntityColumnInfo.senderKeyIndex, j2, realmGet$senderKey, false);
                }
                String realmGet$olmInboundGroupSessionData = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$olmInboundGroupSessionData();
                if (realmGet$olmInboundGroupSessionData != null) {
                    Table.nativeSetString(nativePtr, olmInboundGroupSessionEntityColumnInfo.olmInboundGroupSessionDataIndex, j2, realmGet$olmInboundGroupSessionData, false);
                }
                Table.nativeSetBoolean(nativePtr, olmInboundGroupSessionEntityColumnInfo.backedUpIndex, j2, org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$backedUp(), false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, OlmInboundGroupSessionEntity olmInboundGroupSessionEntity, Map<RealmModel, Long> map) {
        long j;
        OlmInboundGroupSessionEntity olmInboundGroupSessionEntity2 = olmInboundGroupSessionEntity;
        if (olmInboundGroupSessionEntity2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) olmInboundGroupSessionEntity2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(OlmInboundGroupSessionEntity.class);
        long nativePtr = table.getNativePtr();
        OlmInboundGroupSessionEntityColumnInfo olmInboundGroupSessionEntityColumnInfo = (OlmInboundGroupSessionEntityColumnInfo) realm.getSchema().getColumnInfo(OlmInboundGroupSessionEntity.class);
        long j2 = olmInboundGroupSessionEntityColumnInfo.primaryKeyIndex;
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface = olmInboundGroupSessionEntity2;
        String realmGet$primaryKey = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$primaryKey();
        if (realmGet$primaryKey == null) {
            j = Table.nativeFindFirstNull(nativePtr, j2);
        } else {
            j = Table.nativeFindFirstString(nativePtr, j2, realmGet$primaryKey);
        }
        long createRowWithPrimaryKey = j == -1 ? OsObject.createRowWithPrimaryKey(table, j2, realmGet$primaryKey) : j;
        map.put(olmInboundGroupSessionEntity2, Long.valueOf(createRowWithPrimaryKey));
        String realmGet$sessionId = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$sessionId();
        if (realmGet$sessionId != null) {
            Table.nativeSetString(nativePtr, olmInboundGroupSessionEntityColumnInfo.sessionIdIndex, createRowWithPrimaryKey, realmGet$sessionId, false);
        } else {
            Table.nativeSetNull(nativePtr, olmInboundGroupSessionEntityColumnInfo.sessionIdIndex, createRowWithPrimaryKey, false);
        }
        String realmGet$senderKey = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$senderKey();
        if (realmGet$senderKey != null) {
            Table.nativeSetString(nativePtr, olmInboundGroupSessionEntityColumnInfo.senderKeyIndex, createRowWithPrimaryKey, realmGet$senderKey, false);
        } else {
            Table.nativeSetNull(nativePtr, olmInboundGroupSessionEntityColumnInfo.senderKeyIndex, createRowWithPrimaryKey, false);
        }
        String realmGet$olmInboundGroupSessionData = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$olmInboundGroupSessionData();
        if (realmGet$olmInboundGroupSessionData != null) {
            Table.nativeSetString(nativePtr, olmInboundGroupSessionEntityColumnInfo.olmInboundGroupSessionDataIndex, createRowWithPrimaryKey, realmGet$olmInboundGroupSessionData, false);
        } else {
            Table.nativeSetNull(nativePtr, olmInboundGroupSessionEntityColumnInfo.olmInboundGroupSessionDataIndex, createRowWithPrimaryKey, false);
        }
        Table.nativeSetBoolean(nativePtr, olmInboundGroupSessionEntityColumnInfo.backedUpIndex, createRowWithPrimaryKey, org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$backedUp(), false);
        return createRowWithPrimaryKey;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(OlmInboundGroupSessionEntity.class);
        long nativePtr = table.getNativePtr();
        OlmInboundGroupSessionEntityColumnInfo olmInboundGroupSessionEntityColumnInfo = (OlmInboundGroupSessionEntityColumnInfo) realm.getSchema().getColumnInfo(OlmInboundGroupSessionEntity.class);
        long j2 = olmInboundGroupSessionEntityColumnInfo.primaryKeyIndex;
        while (it.hasNext()) {
            OlmInboundGroupSessionEntity olmInboundGroupSessionEntity = (OlmInboundGroupSessionEntity) it.next();
            if (!map2.containsKey(olmInboundGroupSessionEntity)) {
                if (olmInboundGroupSessionEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) olmInboundGroupSessionEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(olmInboundGroupSessionEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface = olmInboundGroupSessionEntity;
                String realmGet$primaryKey = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$primaryKey();
                if (realmGet$primaryKey == null) {
                    j = Table.nativeFindFirstNull(nativePtr, j2);
                } else {
                    j = Table.nativeFindFirstString(nativePtr, j2, realmGet$primaryKey);
                }
                long createRowWithPrimaryKey = j == -1 ? OsObject.createRowWithPrimaryKey(table, j2, realmGet$primaryKey) : j;
                map2.put(olmInboundGroupSessionEntity, Long.valueOf(createRowWithPrimaryKey));
                String realmGet$sessionId = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$sessionId();
                if (realmGet$sessionId != null) {
                    Table.nativeSetString(nativePtr, olmInboundGroupSessionEntityColumnInfo.sessionIdIndex, createRowWithPrimaryKey, realmGet$sessionId, false);
                } else {
                    Table.nativeSetNull(nativePtr, olmInboundGroupSessionEntityColumnInfo.sessionIdIndex, createRowWithPrimaryKey, false);
                }
                String realmGet$senderKey = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$senderKey();
                if (realmGet$senderKey != null) {
                    Table.nativeSetString(nativePtr, olmInboundGroupSessionEntityColumnInfo.senderKeyIndex, createRowWithPrimaryKey, realmGet$senderKey, false);
                } else {
                    Table.nativeSetNull(nativePtr, olmInboundGroupSessionEntityColumnInfo.senderKeyIndex, createRowWithPrimaryKey, false);
                }
                String realmGet$olmInboundGroupSessionData = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$olmInboundGroupSessionData();
                if (realmGet$olmInboundGroupSessionData != null) {
                    Table.nativeSetString(nativePtr, olmInboundGroupSessionEntityColumnInfo.olmInboundGroupSessionDataIndex, createRowWithPrimaryKey, realmGet$olmInboundGroupSessionData, false);
                } else {
                    Table.nativeSetNull(nativePtr, olmInboundGroupSessionEntityColumnInfo.olmInboundGroupSessionDataIndex, createRowWithPrimaryKey, false);
                }
                Table.nativeSetBoolean(nativePtr, olmInboundGroupSessionEntityColumnInfo.backedUpIndex, createRowWithPrimaryKey, org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmGet$backedUp(), false);
            }
        }
    }

    public static OlmInboundGroupSessionEntity createDetachedCopy(OlmInboundGroupSessionEntity olmInboundGroupSessionEntity, int i, int i2, Map<RealmModel, CacheData<RealmModel>> map) {
        OlmInboundGroupSessionEntity olmInboundGroupSessionEntity2;
        if (i > i2 || olmInboundGroupSessionEntity == null) {
            return null;
        }
        CacheData cacheData = (CacheData) map.get(olmInboundGroupSessionEntity);
        if (cacheData == null) {
            olmInboundGroupSessionEntity2 = new OlmInboundGroupSessionEntity();
            map.put(olmInboundGroupSessionEntity, new CacheData(i, olmInboundGroupSessionEntity2));
        } else if (i >= cacheData.minDepth) {
            return (OlmInboundGroupSessionEntity) cacheData.object;
        } else {
            OlmInboundGroupSessionEntity olmInboundGroupSessionEntity3 = (OlmInboundGroupSessionEntity) cacheData.object;
            cacheData.minDepth = i;
            olmInboundGroupSessionEntity2 = olmInboundGroupSessionEntity3;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface = olmInboundGroupSessionEntity2;
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface2 = olmInboundGroupSessionEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$primaryKey(org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface2.realmGet$primaryKey());
        org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$sessionId(org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface2.realmGet$sessionId());
        org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$senderKey(org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface2.realmGet$senderKey());
        org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$olmInboundGroupSessionData(org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface2.realmGet$olmInboundGroupSessionData());
        org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface.realmSet$backedUp(org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface2.realmGet$backedUp());
        return olmInboundGroupSessionEntity2;
    }

    static OlmInboundGroupSessionEntity update(Realm realm, OlmInboundGroupSessionEntityColumnInfo olmInboundGroupSessionEntityColumnInfo, OlmInboundGroupSessionEntity olmInboundGroupSessionEntity, OlmInboundGroupSessionEntity olmInboundGroupSessionEntity2, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface = olmInboundGroupSessionEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface2 = olmInboundGroupSessionEntity2;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(OlmInboundGroupSessionEntity.class), olmInboundGroupSessionEntityColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addString(olmInboundGroupSessionEntityColumnInfo.primaryKeyIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface2.realmGet$primaryKey());
        osObjectBuilder.addString(olmInboundGroupSessionEntityColumnInfo.sessionIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface2.realmGet$sessionId());
        osObjectBuilder.addString(olmInboundGroupSessionEntityColumnInfo.senderKeyIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface2.realmGet$senderKey());
        osObjectBuilder.addString(olmInboundGroupSessionEntityColumnInfo.olmInboundGroupSessionDataIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface2.realmGet$olmInboundGroupSessionData());
        osObjectBuilder.addBoolean(olmInboundGroupSessionEntityColumnInfo.backedUpIndex, Boolean.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxyinterface2.realmGet$backedUp()));
        osObjectBuilder.updateExistingObject();
        return olmInboundGroupSessionEntity;
    }

    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder sb = new StringBuilder("OlmInboundGroupSessionEntity = proxy[");
        sb.append("{primaryKey:");
        String str = "null";
        sb.append(realmGet$primaryKey() != null ? realmGet$primaryKey() : str);
        String str2 = "}";
        sb.append(str2);
        String str3 = ",";
        sb.append(str3);
        sb.append("{sessionId:");
        sb.append(realmGet$sessionId() != null ? realmGet$sessionId() : str);
        sb.append(str2);
        sb.append(str3);
        sb.append("{senderKey:");
        sb.append(realmGet$senderKey() != null ? realmGet$senderKey() : str);
        sb.append(str2);
        sb.append(str3);
        sb.append("{olmInboundGroupSessionData:");
        if (realmGet$olmInboundGroupSessionData() != null) {
            str = realmGet$olmInboundGroupSessionData();
        }
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        sb.append("{backedUp:");
        sb.append(realmGet$backedUp());
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
        org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxy) obj;
        String path = this.proxyState.getRealm$realm().getPath();
        String path2 = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy.proxyState.getRealm$realm().getPath();
        if (path == null ? path2 != null : !path.equals(path2)) {
            return false;
        }
        String name = this.proxyState.getRow$realm().getTable().getName();
        String name2 = org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy.proxyState.getRow$realm().getTable().getName();
        if (name == null ? name2 == null : name.equals(name2)) {
            return this.proxyState.getRow$realm().getIndex() == org_matrix_androidsdk_crypto_cryptostore_db_model_olminboundgroupsessionentityrealmproxy.proxyState.getRow$realm().getIndex();
        }
        return false;
    }
}
