package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.Realm;
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

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "it", "Lio/realm/Realm;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: RealmCryptoStore.kt */
final class RealmCryptoStore$storeUserDevice$1 extends Lambda implements Function1<Realm, Unit> {
    final /* synthetic */ MXDeviceInfo $deviceInfo;
    final /* synthetic */ String $userId;

    RealmCryptoStore$storeUserDevice$1(String str, MXDeviceInfo mXDeviceInfo) {
        this.$userId = str;
        this.$deviceInfo = mXDeviceInfo;
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Realm) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(Realm realm) {
        Intrinsics.checkParameterIsNotNull(realm, "it");
        UserEntity orCreate = UserEntitiesQueriesKt.getOrCreate(UserEntity.Companion, realm, this.$userId);
        Companion companion = DeviceInfoEntity.Companion;
        String str = this.$userId;
        String str2 = this.$deviceInfo.deviceId;
        Intrinsics.checkExpressionValueIsNotNull(str2, "deviceInfo.deviceId");
        DeviceInfoEntity orCreate2 = DeviceInfoEntityQueriesKt.getOrCreate(companion, realm, str, str2);
        orCreate2.setDeviceId(this.$deviceInfo.deviceId);
        orCreate2.setIdentityKey(this.$deviceInfo.identityKey());
        orCreate2.putDeviceInfo(this.$deviceInfo);
        if (!orCreate.getDevices().contains(orCreate2)) {
            orCreate.getDevices().add(orCreate2);
        }
    }
}
