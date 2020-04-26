package fr.gouv.tchap.media;

import fr.gouv.tchap.model.MediaScan;
import im.vector.util.SlidableMediaInfo;
import io.realm.Realm;
import java.util.Date;
import org.matrix.androidsdk.core.ContentManager;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;
import org.matrix.androidsdk.rest.client.MediaScanRestClient;
import org.matrix.androidsdk.rest.model.EncryptedMediaScanBody;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.Event.SentState;
import org.matrix.androidsdk.rest.model.MediaScanResult;

public class MediaScanManager {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = MediaScanManager.class.getSimpleName();
    private final int MEDIA_SCAN_MANAGER_RETRY_DELAY = 600000;
    /* access modifiers changed from: private */
    public MediaScanManagerListener mListener;
    MediaScanDao mMediaScanDao;
    private MediaScanRestClient mMediaScanRestClient;

    public interface MediaScanManagerListener {
        void onMediaScanChange(MediaScan mediaScan);
    }

    public MediaScanManager(MediaScanRestClient mediaScanRestClient, Realm realm) {
        this.mMediaScanRestClient = mediaScanRestClient;
        this.mMediaScanDao = new MediaScanDao(realm);
    }

    public void setListener(MediaScanManagerListener mediaScanManagerListener) {
        this.mListener = mediaScanManagerListener;
    }

    public MediaScan scanUnencryptedMedia(final String str) {
        if (!ContentManager.isValidMatrixContentUrl(str)) {
            return new MediaScan();
        }
        MediaScan mediaScan = this.mMediaScanDao.getMediaScan(str);
        if (isUpdateRequired(mediaScan)) {
            this.mMediaScanDao.updateMediaAntiVirusScanStatus(str, AntiVirusScanStatus.IN_PROGRESS);
            mediaScan = this.mMediaScanDao.getMediaScan(str);
            String substring = str.substring(6);
            int indexOf = substring.indexOf("/");
            if (indexOf < 0 || indexOf > substring.length() - 2) {
                Log.e(LOG_TAG, "## scanUnencryptedMedia failed: invalid url");
                return new MediaScan();
            }
            this.mMediaScanRestClient.scanUnencryptedFile(substring.substring(0, indexOf), substring.substring(indexOf + 1), new ApiCallback<MediaScanResult>() {
                public void onSuccess(MediaScanResult mediaScanResult) {
                    String access$000 = MediaScanManager.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## scanUnencryptedFile succeeded");
                    sb.append(mediaScanResult.info);
                    Log.d(access$000, sb.toString());
                    MediaScanManager.this.mMediaScanDao.updateMediaAntiVirusScanStatus(str, mediaScanResult);
                    if (MediaScanManager.this.mListener != null) {
                        MediaScanManager.this.mListener.onMediaScanChange(MediaScanManager.this.mMediaScanDao.getMediaScan(str));
                    }
                }

                private void onError(String str) {
                    String access$000 = MediaScanManager.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## scanUnencryptedFile failed ");
                    sb.append(str);
                    Log.e(access$000, sb.toString());
                    MediaScanManager.this.mMediaScanDao.updateMediaAntiVirusScanStatus(str, AntiVirusScanStatus.UNKNOWN);
                    if (MediaScanManager.this.mListener != null) {
                        MediaScanManager.this.mListener.onMediaScanChange(MediaScanManager.this.mMediaScanDao.getMediaScan(str));
                    }
                }

                public void onNetworkError(Exception exc) {
                    onError(exc.getLocalizedMessage());
                }

                public void onMatrixError(MatrixError matrixError) {
                    onError(matrixError.getLocalizedMessage());
                }

                public void onUnexpectedError(Exception exc) {
                    onError(exc.getLocalizedMessage());
                }
            });
        }
        return mediaScan;
    }

    public MediaScan scanEncryptedMedia(final EncryptedFileInfo encryptedFileInfo) {
        MediaScan mediaScan = this.mMediaScanDao.getMediaScan(encryptedFileInfo.url);
        if (!isUpdateRequired(mediaScan)) {
            return mediaScan;
        }
        this.mMediaScanDao.updateMediaAntiVirusScanStatus(encryptedFileInfo.url, AntiVirusScanStatus.IN_PROGRESS);
        MediaScan mediaScan2 = this.mMediaScanDao.getMediaScan(encryptedFileInfo.url);
        EncryptedMediaScanBody encryptedMediaScanBody = new EncryptedMediaScanBody();
        encryptedMediaScanBody.encryptedFileInfo = encryptedFileInfo;
        this.mMediaScanRestClient.scanEncryptedFile(encryptedMediaScanBody, new ApiCallback<MediaScanResult>() {
            public void onSuccess(MediaScanResult mediaScanResult) {
                String access$000 = MediaScanManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## scanEncryptedFile succeeded");
                sb.append(mediaScanResult.info);
                Log.d(access$000, sb.toString());
                MediaScanManager.this.mMediaScanDao.updateMediaAntiVirusScanStatus(encryptedFileInfo.url, mediaScanResult);
                if (MediaScanManager.this.mListener != null) {
                    MediaScanManager.this.mListener.onMediaScanChange(MediaScanManager.this.mMediaScanDao.getMediaScan(encryptedFileInfo.url));
                }
            }

            private void onError(String str) {
                String access$000 = MediaScanManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## scanEncryptedFile failed ");
                sb.append(str);
                Log.e(access$000, sb.toString());
                MediaScanManager.this.mMediaScanDao.updateMediaAntiVirusScanStatus(encryptedFileInfo.url, AntiVirusScanStatus.UNKNOWN);
                if (MediaScanManager.this.mListener != null) {
                    MediaScanManager.this.mListener.onMediaScanChange(MediaScanManager.this.mMediaScanDao.getMediaScan(encryptedFileInfo.url));
                }
            }

            public void onNetworkError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                onError(matrixError.getLocalizedMessage());
            }

            public void onUnexpectedError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }
        });
        return mediaScan2;
    }

    private boolean isUpdateRequired(MediaScan mediaScan) {
        if (AntiVirusScanStatus.UNKNOWN == mediaScan.getAntiVirusScanStatus()) {
            Date antiVirusScanDate = mediaScan.getAntiVirusScanDate();
            if (antiVirusScanDate == null) {
                return true;
            }
            long time = antiVirusScanDate.getTime();
            long currentTimeMillis = System.currentTimeMillis();
            if (time > currentTimeMillis || currentTimeMillis - time > 600000) {
                return true;
            }
        }
        return false;
    }

    public void clearAntiVirusScanResults() {
        this.mMediaScanDao.clearAntiVirusScanResults();
    }

    public boolean isUncheckedOrUntrustedMediaEvent(Event event) {
        if (event.isEncrypted()) {
            for (EncryptedFileInfo scanEncryptedMedia : event.getEncryptedFileInfos()) {
                if (scanEncryptedMedia(scanEncryptedMedia).getAntiVirusScanStatus() != AntiVirusScanStatus.TRUSTED) {
                    return true;
                }
            }
        } else if (event.mSentState != SentState.SENT) {
            return !event.getMediaUrls().isEmpty();
        } else {
            for (String scanUnencryptedMedia : event.getMediaUrls()) {
                if (scanUnencryptedMedia(scanUnencryptedMedia).getAntiVirusScanStatus() != AntiVirusScanStatus.TRUSTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isTrustedSlidableMediaInfo(SlidableMediaInfo slidableMediaInfo) {
        MediaScan mediaScan;
        MediaScan mediaScan2;
        if (slidableMediaInfo.mMediaUrl != null) {
            if (slidableMediaInfo.mEncryptedFileInfo != null) {
                mediaScan = scanEncryptedMedia(slidableMediaInfo.mEncryptedFileInfo);
            } else {
                mediaScan = scanUnencryptedMedia(slidableMediaInfo.mMediaUrl);
            }
            if (mediaScan.getAntiVirusScanStatus() == AntiVirusScanStatus.TRUSTED) {
                if (slidableMediaInfo.mThumbnailUrl == null) {
                    return true;
                }
                if (slidableMediaInfo.mEncryptedThumbnailFileInfo != null) {
                    mediaScan2 = scanEncryptedMedia(slidableMediaInfo.mEncryptedThumbnailFileInfo);
                } else {
                    mediaScan2 = scanUnencryptedMedia(slidableMediaInfo.mThumbnailUrl);
                }
                if (mediaScan2.getAntiVirusScanStatus() == AntiVirusScanStatus.TRUSTED) {
                    return true;
                }
            }
        }
        return false;
    }
}
