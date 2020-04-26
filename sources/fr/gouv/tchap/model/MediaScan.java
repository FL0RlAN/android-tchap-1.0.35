package fr.gouv.tchap.model;

import fr.gouv.tchap.media.AntiVirusScanStatus;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.fr_gouv_tchap_model_MediaScanRealmProxyInterface;
import io.realm.internal.RealmObjectProxy;
import java.util.Date;

public class MediaScan extends RealmObject implements fr_gouv_tchap_model_MediaScanRealmProxyInterface {
    private Date antiVirusScanDate;
    private String antiVirusScanInfo;
    private String antiVirusScanStatus;
    @PrimaryKey
    private String url;

    public Date realmGet$antiVirusScanDate() {
        return this.antiVirusScanDate;
    }

    public String realmGet$antiVirusScanInfo() {
        return this.antiVirusScanInfo;
    }

    public String realmGet$antiVirusScanStatus() {
        return this.antiVirusScanStatus;
    }

    public String realmGet$url() {
        return this.url;
    }

    public void realmSet$antiVirusScanDate(Date date) {
        this.antiVirusScanDate = date;
    }

    public void realmSet$antiVirusScanInfo(String str) {
        this.antiVirusScanInfo = str;
    }

    public void realmSet$antiVirusScanStatus(String str) {
        this.antiVirusScanStatus = str;
    }

    public void realmSet$url(String str) {
        this.url = str;
    }

    public MediaScan() {
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
        realmSet$antiVirusScanStatus(AntiVirusScanStatus.UNKNOWN.toString());
        realmSet$antiVirusScanInfo(null);
        realmSet$antiVirusScanDate(null);
    }

    public String getUrl() {
        return realmGet$url();
    }

    public void setUrl(String str) {
        realmSet$url(str);
    }

    public AntiVirusScanStatus getAntiVirusScanStatus() {
        return AntiVirusScanStatus.valueOf(realmGet$antiVirusScanStatus());
    }

    public void setAntiVirusScanStatus(AntiVirusScanStatus antiVirusScanStatus2) {
        realmSet$antiVirusScanStatus(antiVirusScanStatus2.toString());
    }

    public String getAntiVirusScanInfo() {
        return realmGet$antiVirusScanInfo();
    }

    public void setAntiVirusScanInfo(String str) {
        realmSet$antiVirusScanInfo(str);
    }

    public Date getAntiVirusScanDate() {
        return realmGet$antiVirusScanDate();
    }

    public void setAntiVirusScanDate(Date date) {
        realmSet$antiVirusScanDate(date);
    }
}
