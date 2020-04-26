package org.matrix.androidsdk.rest.model.message;

import android.net.Uri;
import java.io.File;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.MXEncryptedAttachments.EncryptionResult;

public class MediaMessage extends Message {
    private static final String LOG_TAG = MediaMessage.class.getSimpleName();

    public String getMimeType() {
        return null;
    }

    public String getThumbnailUrl() {
        return null;
    }

    public String getUrl() {
        return null;
    }

    public void setThumbnailUrl(EncryptionResult encryptionResult, String str) {
    }

    public void setUrl(EncryptionResult encryptionResult, String str) {
    }

    public boolean isThumbnailLocalContent() {
        String thumbnailUrl = getThumbnailUrl();
        return thumbnailUrl != null && thumbnailUrl.startsWith(Message.FILE_SCHEME);
    }

    public boolean isLocalContent() {
        String url = getUrl();
        return url != null && url.startsWith(Message.FILE_SCHEME);
    }

    public void checkMediaUrls() {
        String thumbnailUrl = getThumbnailUrl();
        String str = "## checkMediaUrls() failed";
        String str2 = Message.FILE_SCHEME;
        if (thumbnailUrl != null && thumbnailUrl.startsWith(str2)) {
            try {
                if (!new File(Uri.parse(thumbnailUrl).getPath()).exists()) {
                    setThumbnailUrl(null, null);
                }
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(e.getMessage());
                Log.e(str3, sb.toString(), e);
            }
        }
        String url = getUrl();
        if (url != null && url.startsWith(str2)) {
            try {
                if (!new File(Uri.parse(url).getPath()).exists()) {
                    setUrl(null, null);
                }
            } catch (Exception e2) {
                String str4 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(e2.getMessage());
                Log.e(str4, sb2.toString(), e2);
            }
        }
    }
}
