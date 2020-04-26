package org.matrix.androidsdk.crypto.cryptostore.db.query;

import io.realm.Realm;
import io.realm.RealmQuery;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntity.Companion;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\u001e\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0000\u001a\u001c\u0010\u0007\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0000¨\u0006\b"}, d2 = {"getById", "Lorg/matrix/androidsdk/crypto/cryptostore/db/model/CryptoRoomEntity;", "Lorg/matrix/androidsdk/crypto/cryptostore/db/model/CryptoRoomEntity$Companion;", "realm", "Lio/realm/Realm;", "roomId", "", "getOrCreate", "matrix-sdk-crypto_release"}, k = 2, mv = {1, 1, 13})
/* compiled from: CryptoRoomEntityQueries.kt */
public final class CryptoRoomEntityQueriesKt {
    public static final CryptoRoomEntity getOrCreate(Companion companion, Realm realm, String str) {
        Intrinsics.checkParameterIsNotNull(companion, "receiver$0");
        Intrinsics.checkParameterIsNotNull(realm, "realm");
        Intrinsics.checkParameterIsNotNull(str, CryptoRoomEntityFields.ROOM_ID);
        CryptoRoomEntity byId = getById(companion, realm, str);
        if (byId != null) {
            return byId;
        }
        CryptoRoomEntity cryptoRoomEntity = (CryptoRoomEntity) realm.createObject(CryptoRoomEntity.class, str);
        Intrinsics.checkExpressionValueIsNotNull(cryptoRoomEntity, "let {\n                re…va, roomId)\n            }");
        return cryptoRoomEntity;
    }

    public static final CryptoRoomEntity getById(Companion companion, Realm realm, String str) {
        Intrinsics.checkParameterIsNotNull(companion, "receiver$0");
        Intrinsics.checkParameterIsNotNull(realm, "realm");
        String str2 = CryptoRoomEntityFields.ROOM_ID;
        Intrinsics.checkParameterIsNotNull(str, str2);
        RealmQuery where = realm.where(CryptoRoomEntity.class);
        Intrinsics.checkExpressionValueIsNotNull(where, "this.where(T::class.java)");
        return (CryptoRoomEntity) where.equalTo(str2, str).findFirst();
    }
}
