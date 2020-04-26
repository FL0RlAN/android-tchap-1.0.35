package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.Sort;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmSessionEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmSessionEntityFields;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "Lorg/matrix/androidsdk/crypto/cryptostore/db/model/OlmSessionEntity;", "it", "Lio/realm/Realm;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: RealmCryptoStore.kt */
final class RealmCryptoStore$getLastUsedSessionId$1 extends Lambda implements Function1<Realm, OlmSessionEntity> {
    final /* synthetic */ String $deviceKey;

    RealmCryptoStore$getLastUsedSessionId$1(String str) {
        this.$deviceKey = str;
        super(1);
    }

    public final OlmSessionEntity invoke(Realm realm) {
        Intrinsics.checkParameterIsNotNull(realm, "it");
        RealmQuery where = realm.where(OlmSessionEntity.class);
        Intrinsics.checkExpressionValueIsNotNull(where, "this.where(T::class.java)");
        return (OlmSessionEntity) where.equalTo(OlmSessionEntityFields.DEVICE_KEY, this.$deviceKey).sort(OlmSessionEntityFields.LAST_RECEIVED_MESSAGE_TS, Sort.DESCENDING).findFirst();
    }
}
