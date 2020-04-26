package io.realm;

import java.util.Date;

public interface fr_gouv_tchap_model_MediaScanRealmProxyInterface {
    Date realmGet$antiVirusScanDate();

    String realmGet$antiVirusScanInfo();

    String realmGet$antiVirusScanStatus();

    String realmGet$url();

    void realmSet$antiVirusScanDate(Date date);

    void realmSet$antiVirusScanInfo(String str);

    void realmSet$antiVirusScanStatus(String str);

    void realmSet$url(String str);
}
