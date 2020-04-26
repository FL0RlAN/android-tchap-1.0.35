package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.Realm;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.crypto.cryptostore.db.model.UserEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.query.UserEntitiesQueriesKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "it", "Lio/realm/Realm;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: RealmCryptoStore.kt */
final class RealmCryptoStore$saveDeviceTrackingStatuses$1 extends Lambda implements Function1<Realm, Unit> {
    final /* synthetic */ Map $deviceTrackingStatuses;

    RealmCryptoStore$saveDeviceTrackingStatuses$1(Map map) {
        this.$deviceTrackingStatuses = map;
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Realm) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(Realm realm) {
        Intrinsics.checkParameterIsNotNull(realm, "it");
        Map map = this.$deviceTrackingStatuses;
        Collection arrayList = new ArrayList(map.size());
        for (Entry entry : map.entrySet()) {
            UserEntitiesQueriesKt.getOrCreate(UserEntity.Companion, realm, (String) entry.getKey()).setDeviceTrackingStatus(((Number) entry.getValue()).intValue());
            arrayList.add(Unit.INSTANCE);
        }
        List list = (List) arrayList;
    }
}
