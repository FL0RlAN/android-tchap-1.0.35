package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.Realm;
import io.realm.RealmQuery;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.crypto.cryptostore.db.model.IncomingRoomKeyRequestEntity;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "Lorg/matrix/androidsdk/crypto/cryptostore/db/model/IncomingRoomKeyRequestEntity;", "it", "Lio/realm/Realm;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: RealmCryptoStore.kt */
final class RealmCryptoStore$getIncomingRoomKeyRequest$1 extends Lambda implements Function1<Realm, IncomingRoomKeyRequestEntity> {
    final /* synthetic */ String $deviceId;
    final /* synthetic */ String $requestId;
    final /* synthetic */ String $userId;

    RealmCryptoStore$getIncomingRoomKeyRequest$1(String str, String str2, String str3) {
        this.$userId = str;
        this.$deviceId = str2;
        this.$requestId = str3;
        super(1);
    }

    public final IncomingRoomKeyRequestEntity invoke(Realm realm) {
        Intrinsics.checkParameterIsNotNull(realm, "it");
        RealmQuery where = realm.where(IncomingRoomKeyRequestEntity.class);
        Intrinsics.checkExpressionValueIsNotNull(where, "this.where(T::class.java)");
        return (IncomingRoomKeyRequestEntity) where.equalTo("userId", this.$userId).equalTo("deviceId", this.$deviceId).equalTo("requestId", this.$requestId).findFirst();
    }
}
