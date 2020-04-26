package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.Realm;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.query.CryptoRoomEntityQueriesKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "Lorg/matrix/androidsdk/crypto/cryptostore/db/model/CryptoRoomEntity;", "it", "Lio/realm/Realm;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: RealmCryptoStore.kt */
final class RealmCryptoStore$getRoomAlgorithm$1 extends Lambda implements Function1<Realm, CryptoRoomEntity> {
    final /* synthetic */ String $roomId;

    RealmCryptoStore$getRoomAlgorithm$1(String str) {
        this.$roomId = str;
        super(1);
    }

    public final CryptoRoomEntity invoke(Realm realm) {
        Intrinsics.checkParameterIsNotNull(realm, "it");
        return CryptoRoomEntityQueriesKt.getById(CryptoRoomEntity.Companion, realm, this.$roomId);
    }
}
