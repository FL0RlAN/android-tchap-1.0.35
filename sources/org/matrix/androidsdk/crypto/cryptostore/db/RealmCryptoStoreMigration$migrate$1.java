package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema.Function;
import kotlin.Metadata;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmSessionEntityFields;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Lio/realm/DynamicRealmObject;", "kotlin.jvm.PlatformType", "apply"}, k = 3, mv = {1, 1, 13})
/* compiled from: RealmCryptoStoreMigration.kt */
final class RealmCryptoStoreMigration$migrate$1 implements Function {
    public static final RealmCryptoStoreMigration$migrate$1 INSTANCE = new RealmCryptoStoreMigration$migrate$1();

    RealmCryptoStoreMigration$migrate$1() {
    }

    public final void apply(DynamicRealmObject dynamicRealmObject) {
        dynamicRealmObject.setLong(OlmSessionEntityFields.LAST_RECEIVED_MESSAGE_TS, 0);
    }
}
