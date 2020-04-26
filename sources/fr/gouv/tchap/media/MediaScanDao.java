package fr.gouv.tchap.media;

import fr.gouv.tchap.model.MediaScan;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import java.util.Date;
import org.matrix.androidsdk.rest.model.MediaScanResult;

public class MediaScanDao {
    private static final String LOG_TAG = MediaScanDao.class.getSimpleName();
    private Realm mRealm;

    public MediaScanDao(Realm realm) {
        this.mRealm = realm;
    }

    /* access modifiers changed from: 0000 */
    public MediaScan getMediaScan(String str) {
        return getMediaScan(this.mRealm, str);
    }

    /* access modifiers changed from: 0000 */
    public void updateMediaAntiVirusScanStatus(final String str, final AntiVirusScanStatus antiVirusScanStatus) {
        this.mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                MediaScan access$000 = MediaScanDao.this.getMediaScan(realm, str);
                access$000.setAntiVirusScanStatus(antiVirusScanStatus);
                access$000.setAntiVirusScanDate(new Date());
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void updateMediaAntiVirusScanStatus(final String str, final MediaScanResult mediaScanResult) {
        this.mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                AntiVirusScanStatus antiVirusScanStatus;
                MediaScan access$000 = MediaScanDao.this.getMediaScan(realm, str);
                if (mediaScanResult.clean) {
                    antiVirusScanStatus = AntiVirusScanStatus.TRUSTED;
                } else {
                    antiVirusScanStatus = AntiVirusScanStatus.INFECTED;
                }
                access$000.setAntiVirusScanStatus(antiVirusScanStatus);
                access$000.setAntiVirusScanInfo(mediaScanResult.info);
                access$000.setAntiVirusScanDate(new Date());
            }
        });
    }

    /* access modifiers changed from: 0000 */
    public void clearAntiVirusScanResults() {
        this.mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                realm.delete(MediaScan.class);
            }
        });
    }

    /* access modifiers changed from: private */
    public MediaScan getMediaScan(Realm realm, String str) {
        MediaScan mediaScan = (MediaScan) realm.where(MediaScan.class).equalTo("url", str).findFirst();
        if (mediaScan != null) {
            return mediaScan;
        }
        if (realm.isInTransaction()) {
            return (MediaScan) realm.createObject(MediaScan.class, str);
        }
        realm.beginTransaction();
        MediaScan mediaScan2 = (MediaScan) realm.createObject(MediaScan.class, str);
        realm.commitTransaction();
        return mediaScan2;
    }
}
