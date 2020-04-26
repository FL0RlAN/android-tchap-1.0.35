package org.matrix.androidsdk.crypto.cryptostore.db;

import im.vector.activity.VectorUniversalLinkActivity;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema.Function;
import java.util.Map;
import kotlin.Metadata;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "dynamicObject", "Lio/realm/DynamicRealmObject;", "kotlin.jvm.PlatformType", "apply"}, k = 3, mv = {1, 1, 13})
/* compiled from: RealmCryptoStoreMigration.kt */
final class RealmCryptoStoreMigration$migrate$2 implements Function {
    public static final RealmCryptoStoreMigration$migrate$2 INSTANCE = new RealmCryptoStoreMigration$migrate$2();

    RealmCryptoStoreMigration$migrate$2() {
    }

    public final void apply(DynamicRealmObject dynamicRealmObject) {
        try {
            Map map = (Map) HelperKt.deserializeFromRealm(dynamicRealmObject.getString("requestBodyString"));
            if (map != null) {
                dynamicRealmObject.setString("requestBodyAlgorithm", (String) map.get(CryptoRoomEntityFields.ALGORITHM));
                dynamicRealmObject.setString("requestBodyRoomId", (String) map.get("room_id"));
                dynamicRealmObject.setString("requestBodySenderKey", (String) map.get("sender_key"));
                dynamicRealmObject.setString("requestBodySessionId", (String) map.get(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_SESSION_ID));
            }
        } catch (Exception e) {
            Log.d("RealmCryptoStoreMigration", "Error", e);
        }
    }
}
