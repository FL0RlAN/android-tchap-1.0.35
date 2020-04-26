package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmInboundGroupSessionEntity;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "it", "Lio/realm/Realm;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: RealmCryptoStore.kt */
final class RealmCryptoStore$resetBackupMarkers$1 extends Lambda implements Function1<Realm, Unit> {
    public static final RealmCryptoStore$resetBackupMarkers$1 INSTANCE = new RealmCryptoStore$resetBackupMarkers$1();

    RealmCryptoStore$resetBackupMarkers$1() {
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Realm) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(Realm realm) {
        Intrinsics.checkParameterIsNotNull(realm, "it");
        RealmQuery where = realm.where(OlmInboundGroupSessionEntity.class);
        Intrinsics.checkExpressionValueIsNotNull(where, "this.where(T::class.java)");
        RealmResults findAll = where.findAll();
        Intrinsics.checkExpressionValueIsNotNull(findAll, "it.where<OlmInboundGroup…               .findAll()");
        Iterable<OlmInboundGroupSessionEntity> iterable = findAll;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (OlmInboundGroupSessionEntity backedUp : iterable) {
            backedUp.setBackedUp(false);
            arrayList.add(Unit.INSTANCE);
        }
        List list = (List) arrayList;
    }
}
