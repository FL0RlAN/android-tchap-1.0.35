package org.matrix.androidsdk.crypto.cryptostore.db.query;

import io.realm.Realm;
import io.realm.RealmQuery;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.crypto.cryptostore.db.model.DeviceInfoEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.DeviceInfoEntity.Companion;
import org.matrix.androidsdk.crypto.cryptostore.db.model.DeviceInfoEntityKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a$\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0000¨\u0006\b"}, d2 = {"getOrCreate", "Lorg/matrix/androidsdk/crypto/cryptostore/db/model/DeviceInfoEntity;", "Lorg/matrix/androidsdk/crypto/cryptostore/db/model/DeviceInfoEntity$Companion;", "realm", "Lio/realm/Realm;", "userId", "", "deviceId", "matrix-sdk-crypto_release"}, k = 2, mv = {1, 1, 13})
/* compiled from: DeviceInfoEntityQueries.kt */
public final class DeviceInfoEntityQueriesKt {
    public static final DeviceInfoEntity getOrCreate(Companion companion, Realm realm, String str, String str2) {
        Intrinsics.checkParameterIsNotNull(companion, "receiver$0");
        Intrinsics.checkParameterIsNotNull(realm, "realm");
        Intrinsics.checkParameterIsNotNull(str, "userId");
        Intrinsics.checkParameterIsNotNull(str2, "deviceId");
        RealmQuery where = realm.where(DeviceInfoEntity.class);
        Intrinsics.checkExpressionValueIsNotNull(where, "this.where(T::class.java)");
        DeviceInfoEntity deviceInfoEntity = (DeviceInfoEntity) where.equalTo("primaryKey", DeviceInfoEntityKt.createPrimaryKey(DeviceInfoEntity.Companion, str, str2)).findFirst();
        if (deviceInfoEntity != null) {
            return deviceInfoEntity;
        }
        DeviceInfoEntity deviceInfoEntity2 = (DeviceInfoEntity) realm.createObject(DeviceInfoEntity.class, DeviceInfoEntityKt.createPrimaryKey(DeviceInfoEntity.Companion, str, str2));
        deviceInfoEntity2.setDeviceId(str2);
        Intrinsics.checkExpressionValueIsNotNull(deviceInfoEntity2, "let {\n                re…          }\n            }");
        return deviceInfoEntity2;
    }
}
