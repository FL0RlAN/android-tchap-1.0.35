package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy;
import io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy;
import io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxy.ClassNameHelper;
import io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.cryptostore.db.model.KeysBackupDataEntityFields;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmSessionEntityFields;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J \u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u0004H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/RealmCryptoStoreMigration;", "Lio/realm/RealmMigration;", "()V", "CRYPTO_STORE_SCHEMA_VERSION", "", "LOG_TAG", "", "migrate", "", "realm", "Lio/realm/DynamicRealm;", "oldVersion", "newVersion", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: RealmCryptoStoreMigration.kt */
public final class RealmCryptoStoreMigration implements RealmMigration {
    public static final long CRYPTO_STORE_SCHEMA_VERSION = 2;
    public static final RealmCryptoStoreMigration INSTANCE = new RealmCryptoStoreMigration();
    private static final String LOG_TAG = "RealmCryptoStoreMigration";

    private RealmCryptoStoreMigration() {
    }

    public void migrate(DynamicRealm dynamicRealm, long j, long j2) {
        Intrinsics.checkParameterIsNotNull(dynamicRealm, "realm");
        StringBuilder sb = new StringBuilder();
        sb.append("Migrating Realm Crypto from ");
        sb.append(j);
        sb.append(" to ");
        sb.append(j2);
        String sb2 = sb.toString();
        String str = LOG_TAG;
        Log.d(str, sb2);
        if (j <= 0) {
            Log.d(str, "Step 0 -> 1");
            Log.d(str, "Add field lastReceivedMessageTs (Long) and set the value to 0");
            RealmObjectSchema realmObjectSchema = dynamicRealm.getSchema().get(ClassNameHelper.INTERNAL_CLASS_NAME);
            if (realmObjectSchema != null) {
                RealmObjectSchema addField = realmObjectSchema.addField(OlmSessionEntityFields.LAST_RECEIVED_MESSAGE_TS, Long.TYPE, new FieldAttribute[0]);
                if (addField != null) {
                    addField.transform(RealmCryptoStoreMigration$migrate$1.INSTANCE);
                }
            }
        }
        if (j <= 1) {
            Log.d(str, "Step 1 -> 2");
            String str2 = "Update IncomingRoomKeyRequestEntity format: requestBodyString field is exploded into several fields";
            Log.d(str, str2);
            RealmObjectSchema realmObjectSchema2 = dynamicRealm.getSchema().get(org_matrix_androidsdk_crypto_cryptostore_db_model_IncomingRoomKeyRequestEntityRealmProxy.ClassNameHelper.INTERNAL_CLASS_NAME);
            String str3 = "requestBodyString";
            String str4 = "requestBodySessionId";
            String str5 = "requestBodySenderKey";
            String str6 = "requestBodyRoomId";
            String str7 = "requestBodyAlgorithm";
            if (realmObjectSchema2 != null) {
                RealmObjectSchema addField2 = realmObjectSchema2.addField(str7, String.class, new FieldAttribute[0]);
                if (addField2 != null) {
                    RealmObjectSchema addField3 = addField2.addField(str6, String.class, new FieldAttribute[0]);
                    if (addField3 != null) {
                        RealmObjectSchema addField4 = addField3.addField(str5, String.class, new FieldAttribute[0]);
                        if (addField4 != null) {
                            RealmObjectSchema addField5 = addField4.addField(str4, String.class, new FieldAttribute[0]);
                            if (addField5 != null) {
                                RealmObjectSchema transform = addField5.transform(RealmCryptoStoreMigration$migrate$2.INSTANCE);
                                if (transform != null) {
                                    transform.removeField(str3);
                                }
                            }
                        }
                    }
                }
            }
            Log.d(str, str2);
            RealmObjectSchema realmObjectSchema3 = dynamicRealm.getSchema().get(org_matrix_androidsdk_crypto_cryptostore_db_model_OutgoingRoomKeyRequestEntityRealmProxy.ClassNameHelper.INTERNAL_CLASS_NAME);
            if (realmObjectSchema3 != null) {
                RealmObjectSchema addField6 = realmObjectSchema3.addField(str7, String.class, new FieldAttribute[0]);
                if (addField6 != null) {
                    RealmObjectSchema addField7 = addField6.addField(str6, String.class, new FieldAttribute[0]);
                    if (addField7 != null) {
                        RealmObjectSchema addField8 = addField7.addField(str5, String.class, new FieldAttribute[0]);
                        if (addField8 != null) {
                            RealmObjectSchema addField9 = addField8.addField(str4, String.class, new FieldAttribute[0]);
                            if (addField9 != null) {
                                RealmObjectSchema transform2 = addField9.transform(RealmCryptoStoreMigration$migrate$3.INSTANCE);
                                if (transform2 != null) {
                                    transform2.removeField(str3);
                                }
                            }
                        }
                    }
                }
            }
            Log.d(str, "Create KeysBackupDataEntity");
            String str8 = "primaryKey";
            FieldAttribute[] fieldAttributeArr = new FieldAttribute[0];
            String str9 = KeysBackupDataEntityFields.BACKUP_LAST_SERVER_HASH;
            FieldAttribute[] fieldAttributeArr2 = new FieldAttribute[0];
            dynamicRealm.getSchema().create(org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxy.ClassNameHelper.INTERNAL_CLASS_NAME).addField(str8, Integer.class, new FieldAttribute[0]).addPrimaryKey(str8).setRequired(str8, true).addField(str9, String.class, fieldAttributeArr).addField(KeysBackupDataEntityFields.BACKUP_LAST_SERVER_NUMBER_OF_KEYS, Integer.class, fieldAttributeArr2);
        }
    }
}
