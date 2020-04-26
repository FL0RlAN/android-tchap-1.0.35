package io.realm;

import org.matrix.androidsdk.crypto.cryptostore.db.model.UserEntity;

public interface org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface {
    String realmGet$deviceId();

    String realmGet$deviceInfoData();

    String realmGet$identityKey();

    String realmGet$primaryKey();

    RealmResults<UserEntity> realmGet$users();

    void realmSet$deviceId(String str);

    void realmSet$deviceInfoData(String str);

    void realmSet$identityKey(String str);

    void realmSet$primaryKey(String str);
}
