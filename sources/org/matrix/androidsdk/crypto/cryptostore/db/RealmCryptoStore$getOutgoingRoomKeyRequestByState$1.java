package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.Realm;
import io.realm.RealmQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest.RequestState;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OutgoingRoomKeyRequestEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OutgoingRoomKeyRequestEntityFields;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "Lorg/matrix/androidsdk/crypto/cryptostore/db/model/OutgoingRoomKeyRequestEntity;", "it", "Lio/realm/Realm;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: RealmCryptoStore.kt */
final class RealmCryptoStore$getOutgoingRoomKeyRequestByState$1 extends Lambda implements Function1<Realm, OutgoingRoomKeyRequestEntity> {
    final /* synthetic */ Set $states;

    RealmCryptoStore$getOutgoingRoomKeyRequestByState$1(Set set) {
        this.$states = set;
        super(1);
    }

    public final OutgoingRoomKeyRequestEntity invoke(Realm realm) {
        Intrinsics.checkParameterIsNotNull(realm, "it");
        RealmQuery where = realm.where(OutgoingRoomKeyRequestEntity.class);
        Intrinsics.checkExpressionValueIsNotNull(where, "this.where(T::class.java)");
        Iterable<RequestState> iterable = this.$states;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (RequestState ordinal : iterable) {
            arrayList.add(Integer.valueOf(ordinal.ordinal()));
        }
        Object[] array = ((List) arrayList).toArray(new Integer[0]);
        if (array != null) {
            return (OutgoingRoomKeyRequestEntity) where.in(OutgoingRoomKeyRequestEntityFields.STATE, (Integer[]) array).findFirst();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
    }
}
