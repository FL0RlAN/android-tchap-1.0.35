package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.Realm;
import io.realm.RealmList;
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
import org.matrix.androidsdk.crypto.cryptostore.db.model.DeviceInfoEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.DeviceInfoEntity.Companion;
import org.matrix.androidsdk.crypto.cryptostore.db.model.UserEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.query.DeviceInfoEntityQueriesKt;
import org.matrix.androidsdk.crypto.cryptostore.db.query.UserEntitiesQueriesKt;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "realm", "Lio/realm/Realm;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: RealmCryptoStore.kt */
final class RealmCryptoStore$storeUserDevices$1 extends Lambda implements Function1<Realm, Unit> {
    final /* synthetic */ Map $devices;
    final /* synthetic */ String $userId;

    RealmCryptoStore$storeUserDevices$1(Map map, String str) {
        this.$devices = map;
        this.$userId = str;
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Realm) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(Realm realm) {
        Intrinsics.checkParameterIsNotNull(realm, "realm");
        if (this.$devices == null) {
            UserEntitiesQueriesKt.delete(UserEntity.Companion, realm, this.$userId);
            return;
        }
        UserEntity orCreate = UserEntitiesQueriesKt.getOrCreate(UserEntity.Companion, realm, this.$userId);
        orCreate.getDevices().deleteAllFromRealm();
        RealmList devices = orCreate.getDevices();
        Map map = this.$devices;
        Collection arrayList = new ArrayList(map.size());
        for (Entry entry : map.entrySet()) {
            Companion companion = DeviceInfoEntity.Companion;
            String str = this.$userId;
            String str2 = ((MXDeviceInfo) entry.getValue()).deviceId;
            Intrinsics.checkExpressionValueIsNotNull(str2, "it.value.deviceId");
            DeviceInfoEntity orCreate2 = DeviceInfoEntityQueriesKt.getOrCreate(companion, realm, str, str2);
            orCreate2.setDeviceId(((MXDeviceInfo) entry.getValue()).deviceId);
            orCreate2.setIdentityKey(((MXDeviceInfo) entry.getValue()).identityKey());
            orCreate2.putDeviceInfo((MXDeviceInfo) entry.getValue());
            arrayList.add(orCreate2);
        }
        devices.addAll((List) arrayList);
    }
}
