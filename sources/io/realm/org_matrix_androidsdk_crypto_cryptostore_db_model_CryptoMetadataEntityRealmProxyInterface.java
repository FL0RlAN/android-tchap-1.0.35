package io.realm;

public interface org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface {
    String realmGet$backupVersion();

    String realmGet$deviceId();

    String realmGet$deviceSyncToken();

    boolean realmGet$globalBlacklistUnverifiedDevices();

    String realmGet$olmAccountData();

    String realmGet$userId();

    void realmSet$backupVersion(String str);

    void realmSet$deviceId(String str);

    void realmSet$deviceSyncToken(String str);

    void realmSet$globalBlacklistUnverifiedDevices(boolean z);

    void realmSet$olmAccountData(String str);

    void realmSet$userId(String str);
}
