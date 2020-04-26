package io.realm;

import org.matrix.androidsdk.crypto.cryptostore.db.model.DeviceInfoEntity;

public interface org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxyInterface {
    int realmGet$deviceTrackingStatus();

    RealmList<DeviceInfoEntity> realmGet$devices();

    String realmGet$userId();

    void realmSet$deviceTrackingStatus(int i);

    void realmSet$devices(RealmList<DeviceInfoEntity> realmList);

    void realmSet$userId(String str);
}
