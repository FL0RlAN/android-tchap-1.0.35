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
import org.matrix.androidsdk.crypto.cryptostore.db.model.OutgoingRoomKeyRequestEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OutgoingRoomKeyRequestEntityFields;

public class org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy extends OutgoingRoomKeyRequestEntity implements RealmObjectProxy, org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface {
    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();
    private OutgoingRoomKeyRequestEntityColumnInfo columnInfo;
    private ProxyState<OutgoingRoomKeyRequestEntity> proxyState;

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "OutgoingRoomKeyRequestEntity";
    }

    static final class OutgoingRoomKeyRequestEntityColumnInfo extends ColumnInfo {
        long cancellationTxnIdIndex;
        long maxColumnIndexValue;
        long recipientsDataIndex;
        long requestBodyAlgorithmIndex;
        long requestBodyRoomIdIndex;
        long requestBodySenderKeyIndex;
        long requestBodySessionIdIndex;
        long requestIdIndex;
        long stateIndex;

        OutgoingRoomKeyRequestEntityColumnInfo(OsSchemaInfo osSchemaInfo) {
            super(8);
            OsObjectSchemaInfo objectSchemaInfo = osSchemaInfo.getObjectSchemaInfo(ClassNameHelper.INTERNAL_CLASS_NAME);
            String str = "requestId";
            this.requestIdIndex = addColumnDetails(str, str, objectSchemaInfo);
            String str2 = OutgoingRoomKeyRequestEntityFields.CANCELLATION_TXN_ID;
            this.cancellationTxnIdIndex = addColumnDetails(str2, str2, objectSchemaInfo);
            String str3 = OutgoingRoomKeyRequestEntityFields.RECIPIENTS_DATA;
            this.recipientsDataIndex = addColumnDetails(str3, str3, objectSchemaInfo);
            String str4 = "requestBodyAlgorithm";
            this.requestBodyAlgorithmIndex = addColumnDetails(str4, str4, objectSchemaInfo);
            String str5 = "requestBodyRoomId";
            this.requestBodyRoomIdIndex = addColumnDetails(str5, str5, objectSchemaInfo);
            String str6 = "requestBodySenderKey";
            this.requestBodySenderKeyIndex = addColumnDetails(str6, str6, objectSchemaInfo);
            String str7 = "requestBodySessionId";
            this.requestBodySessionIdIndex = addColumnDetails(str7, str7, objectSchemaInfo);
            String str8 = OutgoingRoomKeyRequestEntityFields.STATE;
            this.stateIndex = addColumnDetails(str8, str8, objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        OutgoingRoomKeyRequestEntityColumnInfo(ColumnInfo columnInfo, boolean z) {
            super(columnInfo, z);
            copy(columnInfo, this);
        }

        /* access modifiers changed from: protected */
        public final ColumnInfo copy(boolean z) {
            return new OutgoingRoomKeyRequestEntityColumnInfo(this, z);
        }

        /* access modifiers changed from: protected */
        public final void copy(ColumnInfo columnInfo, ColumnInfo columnInfo2) {
            OutgoingRoomKeyRequestEntityColumnInfo outgoingRoomKeyRequestEntityColumnInfo = (OutgoingRoomKeyRequestEntityColumnInfo) columnInfo;
            OutgoingRoomKeyRequestEntityColumnInfo outgoingRoomKeyRequestEntityColumnInfo2 = (OutgoingRoomKeyRequestEntityColumnInfo) columnInfo2;
            outgoingRoomKeyRequestEntityColumnInfo2.requestIdIndex = outgoingRoomKeyRequestEntityColumnInfo.requestIdIndex;
            outgoingRoomKeyRequestEntityColumnInfo2.cancellationTxnIdIndex = outgoingRoomKeyRequestEntityColumnInfo.cancellationTxnIdIndex;
            outgoingRoomKeyRequestEntityColumnInfo2.recipientsDataIndex = outgoingRoomKeyRequestEntityColumnInfo.recipientsDataIndex;
            outgoingRoomKeyRequestEntityColumnInfo2.requestBodyAlgorithmIndex = outgoingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex;
            outgoingRoomKeyRequestEntityColumnInfo2.requestBodyRoomIdIndex = outgoingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex;
            outgoingRoomKeyRequestEntityColumnInfo2.requestBodySenderKeyIndex = outgoingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex;
            outgoingRoomKeyRequestEntityColumnInfo2.requestBodySessionIdIndex = outgoingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex;
            outgoingRoomKeyRequestEntityColumnInfo2.stateIndex = outgoingRoomKeyRequestEntityColumnInfo.stateIndex;
            outgoingRoomKeyRequestEntityColumnInfo2.maxColumnIndexValue = outgoingRoomKeyRequestEntityColumnInfo.maxColumnIndexValue;
        }
    }

    public static String getSimpleClassName() {
        return ClassNameHelper.INTERNAL_CLASS_NAME;
    }

    org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy() {
        this.proxyState.setConstructionFinished();
    }

    public void realm$injectObjectContext() {
        if (this.proxyState == null) {
            RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
            this.columnInfo = (OutgoingRoomKeyRequestEntityColumnInfo) realmObjectContext.getColumnInfo();
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
            throw new RealmException("Primary key field 'requestId' cannot be changed after object was created.");
        }
    }

    public String realmGet$cancellationTxnId() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.cancellationTxnIdIndex);
    }

    public void realmSet$cancellationTxnId(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.cancellationTxnIdIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.cancellationTxnIdIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.cancellationTxnIdIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.cancellationTxnIdIndex, row$realm.getIndex(), str, true);
            }
        }
    }

    public String realmGet$recipientsData() {
        this.proxyState.getRealm$realm().checkIfValid();
        return this.proxyState.getRow$realm().getString(this.columnInfo.recipientsDataIndex);
    }

    public void realmSet$recipientsData(String str) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            if (str == null) {
                this.proxyState.getRow$realm().setNull(this.columnInfo.recipientsDataIndex);
            } else {
                this.proxyState.getRow$realm().setString(this.columnInfo.recipientsDataIndex, str);
            }
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            if (str == null) {
                row$realm.getTable().setNull(this.columnInfo.recipientsDataIndex, row$realm.getIndex(), true);
            } else {
                row$realm.getTable().setString(this.columnInfo.recipientsDataIndex, row$realm.getIndex(), str, true);
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

    public int realmGet$state() {
        this.proxyState.getRealm$realm().checkIfValid();
        return (int) this.proxyState.getRow$realm().getLong(this.columnInfo.stateIndex);
    }

    public void realmSet$state(int i) {
        if (!this.proxyState.isUnderConstruction()) {
            this.proxyState.getRealm$realm().checkIfValid();
            this.proxyState.getRow$realm().setLong(this.columnInfo.stateIndex, (long) i);
        } else if (this.proxyState.getAcceptDefaultValue$realm()) {
            Row row$realm = this.proxyState.getRow$realm();
            row$realm.getTable().setLong(this.columnInfo.stateIndex, row$realm.getIndex(), (long) i, true);
        }
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        Builder builder = new Builder(ClassNameHelper.INTERNAL_CLASS_NAME, 8, 0);
        Builder builder2 = builder;
        builder2.addPersistedProperty("requestId", RealmFieldType.STRING, true, true, false);
        builder2.addPersistedProperty(OutgoingRoomKeyRequestEntityFields.CANCELLATION_TXN_ID, RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty(OutgoingRoomKeyRequestEntityFields.RECIPIENTS_DATA, RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty("requestBodyAlgorithm", RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty("requestBodyRoomId", RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty("requestBodySenderKey", RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty("requestBodySessionId", RealmFieldType.STRING, false, false, false);
        builder2.addPersistedProperty(OutgoingRoomKeyRequestEntityFields.STATE, RealmFieldType.INTEGER, false, false, true);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static OutgoingRoomKeyRequestEntityColumnInfo createColumnInfo(OsSchemaInfo osSchemaInfo) {
        return new OutgoingRoomKeyRequestEntityColumnInfo(osSchemaInfo);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ea  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0103  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0135  */
    public static OutgoingRoomKeyRequestEntity createOrUpdateUsingJsonObject(Realm realm, JSONObject jSONObject, boolean z) throws JSONException {
        org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        long j;
        List emptyList = Collections.emptyList();
        String str8 = "requestId";
        if (z) {
            Table table = realm.getTable(OutgoingRoomKeyRequestEntity.class);
            long j2 = ((OutgoingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(OutgoingRoomKeyRequestEntity.class)).requestIdIndex;
            if (jSONObject.isNull(str8)) {
                j = table.findFirstNull(j2);
            } else {
                j = table.findFirstString(j2, jSONObject.getString(str8));
            }
            if (j != -1) {
                RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
                try {
                    realmObjectContext.set(realm, table.getUncheckedRow(j), realm.getSchema().getColumnInfo(OutgoingRoomKeyRequestEntity.class), false, Collections.emptyList());
                    org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy();
                    if (org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy == null) {
                        if (!jSONObject.has(str8)) {
                            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'requestId'.");
                        } else if (jSONObject.isNull(str8)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy) realm.createObjectInternal(OutgoingRoomKeyRequestEntity.class, null, true, emptyList);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy) realm.createObjectInternal(OutgoingRoomKeyRequestEntity.class, jSONObject.getString(str8), true, emptyList);
                        }
                    }
                    org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy;
                    str = OutgoingRoomKeyRequestEntityFields.CANCELLATION_TXN_ID;
                    if (jSONObject.has(str)) {
                        if (jSONObject.isNull(str)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$cancellationTxnId(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$cancellationTxnId(jSONObject.getString(str));
                        }
                    }
                    str2 = OutgoingRoomKeyRequestEntityFields.RECIPIENTS_DATA;
                    if (jSONObject.has(str2)) {
                        if (jSONObject.isNull(str2)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$recipientsData(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$recipientsData(jSONObject.getString(str2));
                        }
                    }
                    str3 = "requestBodyAlgorithm";
                    if (jSONObject.has(str3)) {
                        if (jSONObject.isNull(str3)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyAlgorithm(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyAlgorithm(jSONObject.getString(str3));
                        }
                    }
                    str4 = "requestBodyRoomId";
                    if (jSONObject.has(str4)) {
                        if (jSONObject.isNull(str4)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyRoomId(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyRoomId(jSONObject.getString(str4));
                        }
                    }
                    str5 = "requestBodySenderKey";
                    if (jSONObject.has(str5)) {
                        if (jSONObject.isNull(str5)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySenderKey(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySenderKey(jSONObject.getString(str5));
                        }
                    }
                    str6 = "requestBodySessionId";
                    if (jSONObject.has(str6)) {
                        if (jSONObject.isNull(str6)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySessionId(null);
                        } else {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySessionId(jSONObject.getString(str6));
                        }
                    }
                    str7 = OutgoingRoomKeyRequestEntityFields.STATE;
                    if (jSONObject.has(str7)) {
                        if (!jSONObject.isNull(str7)) {
                            org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$state(jSONObject.getInt(str7));
                        } else {
                            throw new IllegalArgumentException("Trying to set non-nullable field 'state' to null.");
                        }
                    }
                    return org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy;
                } finally {
                    realmObjectContext.clear();
                }
            }
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy = null;
        if (org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy == null) {
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2 = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy;
        str = OutgoingRoomKeyRequestEntityFields.CANCELLATION_TXN_ID;
        if (jSONObject.has(str)) {
        }
        str2 = OutgoingRoomKeyRequestEntityFields.RECIPIENTS_DATA;
        if (jSONObject.has(str2)) {
        }
        str3 = "requestBodyAlgorithm";
        if (jSONObject.has(str3)) {
        }
        str4 = "requestBodyRoomId";
        if (jSONObject.has(str4)) {
        }
        str5 = "requestBodySenderKey";
        if (jSONObject.has(str5)) {
        }
        str6 = "requestBodySessionId";
        if (jSONObject.has(str6)) {
        }
        str7 = OutgoingRoomKeyRequestEntityFields.STATE;
        if (jSONObject.has(str7)) {
        }
        return org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy;
    }

    public static OutgoingRoomKeyRequestEntity createUsingJsonStream(Realm realm, JsonReader jsonReader) throws IOException {
        OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity = new OutgoingRoomKeyRequestEntity();
        org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface = outgoingRoomKeyRequestEntity;
        jsonReader.beginObject();
        boolean z = false;
        while (jsonReader.hasNext()) {
            String nextName = jsonReader.nextName();
            if (nextName.equals("requestId")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestId(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestId(null);
                }
                z = true;
            } else if (nextName.equals(OutgoingRoomKeyRequestEntityFields.CANCELLATION_TXN_ID)) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$cancellationTxnId(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$cancellationTxnId(null);
                }
            } else if (nextName.equals(OutgoingRoomKeyRequestEntityFields.RECIPIENTS_DATA)) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$recipientsData(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$recipientsData(null);
                }
            } else if (nextName.equals("requestBodyAlgorithm")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyAlgorithm(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyAlgorithm(null);
                }
            } else if (nextName.equals("requestBodyRoomId")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyRoomId(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyRoomId(null);
                }
            } else if (nextName.equals("requestBodySenderKey")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySenderKey(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySenderKey(null);
                }
            } else if (nextName.equals("requestBodySessionId")) {
                if (jsonReader.peek() != JsonToken.NULL) {
                    org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySessionId(jsonReader.nextString());
                } else {
                    jsonReader.skipValue();
                    org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySessionId(null);
                }
            } else if (!nextName.equals(OutgoingRoomKeyRequestEntityFields.STATE)) {
                jsonReader.skipValue();
            } else if (jsonReader.peek() != JsonToken.NULL) {
                org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$state(jsonReader.nextInt());
            } else {
                jsonReader.skipValue();
                throw new IllegalArgumentException("Trying to set non-nullable field 'state' to null.");
            }
        }
        jsonReader.endObject();
        if (z) {
            return (OutgoingRoomKeyRequestEntity) realm.copyToRealm(outgoingRoomKeyRequestEntity, new ImportFlag[0]);
        }
        throw new IllegalArgumentException("JSON object doesn't have the primary key field 'requestId'.");
    }

    private static org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy newProxyInstance(BaseRealm baseRealm, Row row) {
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        realmObjectContext.set(baseRealm, row, baseRealm.getSchema().getColumnInfo(OutgoingRoomKeyRequestEntity.class), false, Collections.emptyList());
        org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy();
        realmObjectContext.clear();
        return org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a1  */
    public static OutgoingRoomKeyRequestEntity copyOrUpdate(Realm realm, OutgoingRoomKeyRequestEntityColumnInfo outgoingRoomKeyRequestEntityColumnInfo, OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        boolean z2;
        long j;
        if (outgoingRoomKeyRequestEntity instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) outgoingRoomKeyRequestEntity;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null) {
                BaseRealm realm$realm = realmObjectProxy.realmGet$proxyState().getRealm$realm();
                if (realm$realm.threadId != realm.threadId) {
                    throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
                } else if (realm$realm.getPath().equals(realm.getPath())) {
                    return outgoingRoomKeyRequestEntity;
                }
            }
        }
        RealmObjectContext realmObjectContext = (RealmObjectContext) BaseRealm.objectContext.get();
        RealmObjectProxy realmObjectProxy2 = (RealmObjectProxy) map.get(outgoingRoomKeyRequestEntity);
        if (realmObjectProxy2 != null) {
            return (OutgoingRoomKeyRequestEntity) realmObjectProxy2;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy = null;
        if (z) {
            Table table = realm.getTable(OutgoingRoomKeyRequestEntity.class);
            long j2 = outgoingRoomKeyRequestEntityColumnInfo.requestIdIndex;
            String realmGet$requestId = outgoingRoomKeyRequestEntity.realmGet$requestId();
            if (realmGet$requestId == null) {
                j = table.findFirstNull(j2);
            } else {
                j = table.findFirstString(j2, realmGet$requestId);
            }
            if (j == -1) {
                z2 = false;
                return !z2 ? update(realm, outgoingRoomKeyRequestEntityColumnInfo, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy, outgoingRoomKeyRequestEntity, map, set) : copy(realm, outgoingRoomKeyRequestEntityColumnInfo, outgoingRoomKeyRequestEntity, z, map, set);
            }
            try {
                realmObjectContext.set(realm, table.getUncheckedRow(j), outgoingRoomKeyRequestEntityColumnInfo, false, Collections.emptyList());
                org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy = new org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy();
                map.put(outgoingRoomKeyRequestEntity, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy);
            } finally {
                realmObjectContext.clear();
            }
        }
        z2 = z;
        return !z2 ? update(realm, outgoingRoomKeyRequestEntityColumnInfo, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy, outgoingRoomKeyRequestEntity, map, set) : copy(realm, outgoingRoomKeyRequestEntityColumnInfo, outgoingRoomKeyRequestEntity, z, map, set);
    }

    public static OutgoingRoomKeyRequestEntity copy(Realm realm, OutgoingRoomKeyRequestEntityColumnInfo outgoingRoomKeyRequestEntityColumnInfo, OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity, boolean z, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        RealmObjectProxy realmObjectProxy = (RealmObjectProxy) map.get(outgoingRoomKeyRequestEntity);
        if (realmObjectProxy != null) {
            return (OutgoingRoomKeyRequestEntity) realmObjectProxy;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface = outgoingRoomKeyRequestEntity;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(OutgoingRoomKeyRequestEntity.class), outgoingRoomKeyRequestEntityColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addString(outgoingRoomKeyRequestEntityColumnInfo.requestIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestId());
        osObjectBuilder.addString(outgoingRoomKeyRequestEntityColumnInfo.cancellationTxnIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$cancellationTxnId());
        osObjectBuilder.addString(outgoingRoomKeyRequestEntityColumnInfo.recipientsDataIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$recipientsData());
        osObjectBuilder.addString(outgoingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyAlgorithm());
        osObjectBuilder.addString(outgoingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyRoomId());
        osObjectBuilder.addString(outgoingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySenderKey());
        osObjectBuilder.addString(outgoingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySessionId());
        osObjectBuilder.addInteger(outgoingRoomKeyRequestEntityColumnInfo.stateIndex, Integer.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$state()));
        org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy newProxyInstance = newProxyInstance(realm, osObjectBuilder.createNewObject());
        map.put(outgoingRoomKeyRequestEntity, newProxyInstance);
        return newProxyInstance;
    }

    public static long insert(Realm realm, OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity, Map<RealmModel, Long> map) {
        long j;
        long j2;
        OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity2 = outgoingRoomKeyRequestEntity;
        if (outgoingRoomKeyRequestEntity2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) outgoingRoomKeyRequestEntity2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(OutgoingRoomKeyRequestEntity.class);
        long nativePtr = table.getNativePtr();
        OutgoingRoomKeyRequestEntityColumnInfo outgoingRoomKeyRequestEntityColumnInfo = (OutgoingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(OutgoingRoomKeyRequestEntity.class);
        long j3 = outgoingRoomKeyRequestEntityColumnInfo.requestIdIndex;
        org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface = outgoingRoomKeyRequestEntity2;
        String realmGet$requestId = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestId();
        if (realmGet$requestId == null) {
            j = Table.nativeFindFirstNull(nativePtr, j3);
        } else {
            j = Table.nativeFindFirstString(nativePtr, j3, realmGet$requestId);
        }
        if (j == -1) {
            j2 = OsObject.createRowWithPrimaryKey(table, j3, realmGet$requestId);
        } else {
            Table.throwDuplicatePrimaryKeyException(realmGet$requestId);
            j2 = j;
        }
        map.put(outgoingRoomKeyRequestEntity2, Long.valueOf(j2));
        String realmGet$cancellationTxnId = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$cancellationTxnId();
        if (realmGet$cancellationTxnId != null) {
            Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.cancellationTxnIdIndex, j2, realmGet$cancellationTxnId, false);
        }
        String realmGet$recipientsData = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$recipientsData();
        if (realmGet$recipientsData != null) {
            Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.recipientsDataIndex, j2, realmGet$recipientsData, false);
        }
        String realmGet$requestBodyAlgorithm = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyAlgorithm();
        if (realmGet$requestBodyAlgorithm != null) {
            Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex, j2, realmGet$requestBodyAlgorithm, false);
        }
        String realmGet$requestBodyRoomId = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyRoomId();
        if (realmGet$requestBodyRoomId != null) {
            Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex, j2, realmGet$requestBodyRoomId, false);
        }
        String realmGet$requestBodySenderKey = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySenderKey();
        if (realmGet$requestBodySenderKey != null) {
            Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex, j2, realmGet$requestBodySenderKey, false);
        }
        String realmGet$requestBodySessionId = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySessionId();
        if (realmGet$requestBodySessionId != null) {
            Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex, j2, realmGet$requestBodySessionId, false);
        }
        Table.nativeSetLong(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.stateIndex, j2, (long) org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$state(), false);
        return j2;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        long j2;
        long j3;
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(OutgoingRoomKeyRequestEntity.class);
        long nativePtr = table.getNativePtr();
        OutgoingRoomKeyRequestEntityColumnInfo outgoingRoomKeyRequestEntityColumnInfo = (OutgoingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(OutgoingRoomKeyRequestEntity.class);
        long j4 = outgoingRoomKeyRequestEntityColumnInfo.requestIdIndex;
        while (it.hasNext()) {
            OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity = (OutgoingRoomKeyRequestEntity) it.next();
            if (!map2.containsKey(outgoingRoomKeyRequestEntity)) {
                if (outgoingRoomKeyRequestEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) outgoingRoomKeyRequestEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(outgoingRoomKeyRequestEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface = outgoingRoomKeyRequestEntity;
                String realmGet$requestId = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestId();
                if (realmGet$requestId == null) {
                    j = Table.nativeFindFirstNull(nativePtr, j4);
                } else {
                    j = Table.nativeFindFirstString(nativePtr, j4, realmGet$requestId);
                }
                if (j == -1) {
                    j2 = OsObject.createRowWithPrimaryKey(table, j4, realmGet$requestId);
                } else {
                    Table.throwDuplicatePrimaryKeyException(realmGet$requestId);
                    j2 = j;
                }
                map2.put(outgoingRoomKeyRequestEntity, Long.valueOf(j2));
                String realmGet$cancellationTxnId = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$cancellationTxnId();
                if (realmGet$cancellationTxnId != null) {
                    j3 = j4;
                    Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.cancellationTxnIdIndex, j2, realmGet$cancellationTxnId, false);
                } else {
                    j3 = j4;
                }
                String realmGet$recipientsData = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$recipientsData();
                if (realmGet$recipientsData != null) {
                    Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.recipientsDataIndex, j2, realmGet$recipientsData, false);
                }
                String realmGet$requestBodyAlgorithm = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyAlgorithm();
                if (realmGet$requestBodyAlgorithm != null) {
                    Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex, j2, realmGet$requestBodyAlgorithm, false);
                }
                String realmGet$requestBodyRoomId = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyRoomId();
                if (realmGet$requestBodyRoomId != null) {
                    Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex, j2, realmGet$requestBodyRoomId, false);
                }
                String realmGet$requestBodySenderKey = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySenderKey();
                if (realmGet$requestBodySenderKey != null) {
                    Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex, j2, realmGet$requestBodySenderKey, false);
                }
                String realmGet$requestBodySessionId = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySessionId();
                if (realmGet$requestBodySessionId != null) {
                    Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex, j2, realmGet$requestBodySessionId, false);
                }
                Table.nativeSetLong(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.stateIndex, j2, (long) org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$state(), false);
                j4 = j3;
            }
        }
    }

    public static long insertOrUpdate(Realm realm, OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity, Map<RealmModel, Long> map) {
        long j;
        OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity2 = outgoingRoomKeyRequestEntity;
        if (outgoingRoomKeyRequestEntity2 instanceof RealmObjectProxy) {
            RealmObjectProxy realmObjectProxy = (RealmObjectProxy) outgoingRoomKeyRequestEntity2;
            if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                return realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex();
            }
        }
        Table table = realm.getTable(OutgoingRoomKeyRequestEntity.class);
        long nativePtr = table.getNativePtr();
        OutgoingRoomKeyRequestEntityColumnInfo outgoingRoomKeyRequestEntityColumnInfo = (OutgoingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(OutgoingRoomKeyRequestEntity.class);
        long j2 = outgoingRoomKeyRequestEntityColumnInfo.requestIdIndex;
        org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface = outgoingRoomKeyRequestEntity2;
        String realmGet$requestId = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestId();
        if (realmGet$requestId == null) {
            j = Table.nativeFindFirstNull(nativePtr, j2);
        } else {
            j = Table.nativeFindFirstString(nativePtr, j2, realmGet$requestId);
        }
        long createRowWithPrimaryKey = j == -1 ? OsObject.createRowWithPrimaryKey(table, j2, realmGet$requestId) : j;
        map.put(outgoingRoomKeyRequestEntity2, Long.valueOf(createRowWithPrimaryKey));
        String realmGet$cancellationTxnId = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$cancellationTxnId();
        if (realmGet$cancellationTxnId != null) {
            Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.cancellationTxnIdIndex, createRowWithPrimaryKey, realmGet$cancellationTxnId, false);
        } else {
            Table.nativeSetNull(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.cancellationTxnIdIndex, createRowWithPrimaryKey, false);
        }
        String realmGet$recipientsData = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$recipientsData();
        if (realmGet$recipientsData != null) {
            Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.recipientsDataIndex, createRowWithPrimaryKey, realmGet$recipientsData, false);
        } else {
            Table.nativeSetNull(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.recipientsDataIndex, createRowWithPrimaryKey, false);
        }
        String realmGet$requestBodyAlgorithm = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyAlgorithm();
        if (realmGet$requestBodyAlgorithm != null) {
            Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex, createRowWithPrimaryKey, realmGet$requestBodyAlgorithm, false);
        } else {
            Table.nativeSetNull(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex, createRowWithPrimaryKey, false);
        }
        String realmGet$requestBodyRoomId = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyRoomId();
        if (realmGet$requestBodyRoomId != null) {
            Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex, createRowWithPrimaryKey, realmGet$requestBodyRoomId, false);
        } else {
            Table.nativeSetNull(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex, createRowWithPrimaryKey, false);
        }
        String realmGet$requestBodySenderKey = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySenderKey();
        if (realmGet$requestBodySenderKey != null) {
            Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex, createRowWithPrimaryKey, realmGet$requestBodySenderKey, false);
        } else {
            Table.nativeSetNull(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex, createRowWithPrimaryKey, false);
        }
        String realmGet$requestBodySessionId = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySessionId();
        if (realmGet$requestBodySessionId != null) {
            Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex, createRowWithPrimaryKey, realmGet$requestBodySessionId, false);
        } else {
            Table.nativeSetNull(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex, createRowWithPrimaryKey, false);
        }
        Table.nativeSetLong(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.stateIndex, createRowWithPrimaryKey, (long) org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$state(), false);
        return createRowWithPrimaryKey;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> it, Map<RealmModel, Long> map) {
        long j;
        long j2;
        Map<RealmModel, Long> map2 = map;
        Table table = realm.getTable(OutgoingRoomKeyRequestEntity.class);
        long nativePtr = table.getNativePtr();
        OutgoingRoomKeyRequestEntityColumnInfo outgoingRoomKeyRequestEntityColumnInfo = (OutgoingRoomKeyRequestEntityColumnInfo) realm.getSchema().getColumnInfo(OutgoingRoomKeyRequestEntity.class);
        long j3 = outgoingRoomKeyRequestEntityColumnInfo.requestIdIndex;
        while (it.hasNext()) {
            OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity = (OutgoingRoomKeyRequestEntity) it.next();
            if (!map2.containsKey(outgoingRoomKeyRequestEntity)) {
                if (outgoingRoomKeyRequestEntity instanceof RealmObjectProxy) {
                    RealmObjectProxy realmObjectProxy = (RealmObjectProxy) outgoingRoomKeyRequestEntity;
                    if (realmObjectProxy.realmGet$proxyState().getRealm$realm() != null && realmObjectProxy.realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                        map2.put(outgoingRoomKeyRequestEntity, Long.valueOf(realmObjectProxy.realmGet$proxyState().getRow$realm().getIndex()));
                    }
                }
                org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface = outgoingRoomKeyRequestEntity;
                String realmGet$requestId = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestId();
                if (realmGet$requestId == null) {
                    j = Table.nativeFindFirstNull(nativePtr, j3);
                } else {
                    j = Table.nativeFindFirstString(nativePtr, j3, realmGet$requestId);
                }
                long createRowWithPrimaryKey = j == -1 ? OsObject.createRowWithPrimaryKey(table, j3, realmGet$requestId) : j;
                map2.put(outgoingRoomKeyRequestEntity, Long.valueOf(createRowWithPrimaryKey));
                String realmGet$cancellationTxnId = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$cancellationTxnId();
                if (realmGet$cancellationTxnId != null) {
                    j2 = j3;
                    Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.cancellationTxnIdIndex, createRowWithPrimaryKey, realmGet$cancellationTxnId, false);
                } else {
                    j2 = j3;
                    Table.nativeSetNull(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.cancellationTxnIdIndex, createRowWithPrimaryKey, false);
                }
                String realmGet$recipientsData = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$recipientsData();
                if (realmGet$recipientsData != null) {
                    Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.recipientsDataIndex, createRowWithPrimaryKey, realmGet$recipientsData, false);
                } else {
                    Table.nativeSetNull(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.recipientsDataIndex, createRowWithPrimaryKey, false);
                }
                String realmGet$requestBodyAlgorithm = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyAlgorithm();
                if (realmGet$requestBodyAlgorithm != null) {
                    Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex, createRowWithPrimaryKey, realmGet$requestBodyAlgorithm, false);
                } else {
                    Table.nativeSetNull(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex, createRowWithPrimaryKey, false);
                }
                String realmGet$requestBodyRoomId = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodyRoomId();
                if (realmGet$requestBodyRoomId != null) {
                    Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex, createRowWithPrimaryKey, realmGet$requestBodyRoomId, false);
                } else {
                    Table.nativeSetNull(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex, createRowWithPrimaryKey, false);
                }
                String realmGet$requestBodySenderKey = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySenderKey();
                if (realmGet$requestBodySenderKey != null) {
                    Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex, createRowWithPrimaryKey, realmGet$requestBodySenderKey, false);
                } else {
                    Table.nativeSetNull(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex, createRowWithPrimaryKey, false);
                }
                String realmGet$requestBodySessionId = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$requestBodySessionId();
                if (realmGet$requestBodySessionId != null) {
                    Table.nativeSetString(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex, createRowWithPrimaryKey, realmGet$requestBodySessionId, false);
                } else {
                    Table.nativeSetNull(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex, createRowWithPrimaryKey, false);
                }
                Table.nativeSetLong(nativePtr, outgoingRoomKeyRequestEntityColumnInfo.stateIndex, createRowWithPrimaryKey, (long) org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmGet$state(), false);
                j3 = j2;
            }
        }
    }

    public static OutgoingRoomKeyRequestEntity createDetachedCopy(OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity, int i, int i2, Map<RealmModel, CacheData<RealmModel>> map) {
        OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity2;
        if (i > i2 || outgoingRoomKeyRequestEntity == null) {
            return null;
        }
        CacheData cacheData = (CacheData) map.get(outgoingRoomKeyRequestEntity);
        if (cacheData == null) {
            outgoingRoomKeyRequestEntity2 = new OutgoingRoomKeyRequestEntity();
            map.put(outgoingRoomKeyRequestEntity, new CacheData(i, outgoingRoomKeyRequestEntity2));
        } else if (i >= cacheData.minDepth) {
            return (OutgoingRoomKeyRequestEntity) cacheData.object;
        } else {
            OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity3 = (OutgoingRoomKeyRequestEntity) cacheData.object;
            cacheData.minDepth = i;
            outgoingRoomKeyRequestEntity2 = outgoingRoomKeyRequestEntity3;
        }
        org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface = outgoingRoomKeyRequestEntity2;
        org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2 = outgoingRoomKeyRequestEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestId(org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2.realmGet$requestId());
        org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$cancellationTxnId(org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2.realmGet$cancellationTxnId());
        org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$recipientsData(org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2.realmGet$recipientsData());
        org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyAlgorithm(org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2.realmGet$requestBodyAlgorithm());
        org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodyRoomId(org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2.realmGet$requestBodyRoomId());
        org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySenderKey(org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2.realmGet$requestBodySenderKey());
        org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$requestBodySessionId(org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2.realmGet$requestBodySessionId());
        org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface.realmSet$state(org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2.realmGet$state());
        return outgoingRoomKeyRequestEntity2;
    }

    static OutgoingRoomKeyRequestEntity update(Realm realm, OutgoingRoomKeyRequestEntityColumnInfo outgoingRoomKeyRequestEntityColumnInfo, OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity, OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity2, Map<RealmModel, RealmObjectProxy> map, Set<ImportFlag> set) {
        org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface = outgoingRoomKeyRequestEntity;
        org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxyInterface org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2 = outgoingRoomKeyRequestEntity2;
        OsObjectBuilder osObjectBuilder = new OsObjectBuilder(realm.getTable(OutgoingRoomKeyRequestEntity.class), outgoingRoomKeyRequestEntityColumnInfo.maxColumnIndexValue, set);
        osObjectBuilder.addString(outgoingRoomKeyRequestEntityColumnInfo.requestIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2.realmGet$requestId());
        osObjectBuilder.addString(outgoingRoomKeyRequestEntityColumnInfo.cancellationTxnIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2.realmGet$cancellationTxnId());
        osObjectBuilder.addString(outgoingRoomKeyRequestEntityColumnInfo.recipientsDataIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2.realmGet$recipientsData());
        osObjectBuilder.addString(outgoingRoomKeyRequestEntityColumnInfo.requestBodyAlgorithmIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2.realmGet$requestBodyAlgorithm());
        osObjectBuilder.addString(outgoingRoomKeyRequestEntityColumnInfo.requestBodyRoomIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2.realmGet$requestBodyRoomId());
        osObjectBuilder.addString(outgoingRoomKeyRequestEntityColumnInfo.requestBodySenderKeyIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2.realmGet$requestBodySenderKey());
        osObjectBuilder.addString(outgoingRoomKeyRequestEntityColumnInfo.requestBodySessionIdIndex, org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2.realmGet$requestBodySessionId());
        osObjectBuilder.addInteger(outgoingRoomKeyRequestEntityColumnInfo.stateIndex, Integer.valueOf(org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxyinterface2.realmGet$state()));
        osObjectBuilder.updateExistingObject();
        return outgoingRoomKeyRequestEntity;
    }

    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder sb = new StringBuilder("OutgoingRoomKeyRequestEntity = proxy[");
        sb.append("{requestId:");
        String str = "null";
        sb.append(realmGet$requestId() != null ? realmGet$requestId() : str);
        String str2 = "}";
        sb.append(str2);
        String str3 = ",";
        sb.append(str3);
        sb.append("{cancellationTxnId:");
        sb.append(realmGet$cancellationTxnId() != null ? realmGet$cancellationTxnId() : str);
        sb.append(str2);
        sb.append(str3);
        sb.append("{recipientsData:");
        sb.append(realmGet$recipientsData() != null ? realmGet$recipientsData() : str);
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
        sb.append(str3);
        sb.append("{state:");
        sb.append(realmGet$state());
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
        org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy = (org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy) obj;
        String path = this.proxyState.getRealm$realm().getPath();
        String path2 = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy.proxyState.getRealm$realm().getPath();
        if (path == null ? path2 != null : !path.equals(path2)) {
            return false;
        }
        String name = this.proxyState.getRow$realm().getTable().getName();
        String name2 = org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy.proxyState.getRow$realm().getTable().getName();
        if (name == null ? name2 == null : name.equals(name2)) {
            return this.proxyState.getRow$realm().getIndex() == org_matrix_androidsdk_crypto_cryptostore_db_model_outgoingroomkeyrequestentityrealmproxy.proxyState.getRow$realm().getIndex();
        }
        return false;
    }
}
