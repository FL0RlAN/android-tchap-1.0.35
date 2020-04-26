package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "it", "Lio/realm/Realm;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: RealmCryptoStore.kt */
final class RealmCryptoStore$setRoomsListBlacklistUnverifiedDevices$1 extends Lambda implements Function1<Realm, Unit> {
    final /* synthetic */ List $roomIds;

    RealmCryptoStore$setRoomsListBlacklistUnverifiedDevices$1(List list) {
        this.$roomIds = list;
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Realm) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(Realm realm) {
        Intrinsics.checkParameterIsNotNull(realm, "it");
        RealmQuery where = realm.where(CryptoRoomEntity.class);
        String str = "this.where(T::class.java)";
        Intrinsics.checkExpressionValueIsNotNull(where, str);
        RealmResults<CryptoRoomEntity> findAll = where.findAll();
        String str2 = "it.where<CryptoRoomEntit…               .findAll()";
        Intrinsics.checkExpressionValueIsNotNull(findAll, str2);
        for (CryptoRoomEntity blacklistUnverifiedDevices : findAll) {
            blacklistUnverifiedDevices.setBlacklistUnverifiedDevices(false);
        }
        RealmQuery where2 = realm.where(CryptoRoomEntity.class);
        Intrinsics.checkExpressionValueIsNotNull(where2, str);
        Collection collection = this.$roomIds;
        if (collection != null) {
            Object[] array = collection.toArray(new String[0]);
            if (array != null) {
                RealmResults<CryptoRoomEntity> findAll2 = where2.in(CryptoRoomEntityFields.ROOM_ID, (String[]) array).findAll();
                Intrinsics.checkExpressionValueIsNotNull(findAll2, str2);
                for (CryptoRoomEntity blacklistUnverifiedDevices2 : findAll2) {
                    blacklistUnverifiedDevices2.setBlacklistUnverifiedDevices(true);
                }
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        throw new TypeCastException("null cannot be cast to non-null type java.util.Collection<T>");
    }
}
