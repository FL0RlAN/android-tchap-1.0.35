package org.matrix.androidsdk.rest.model.message;

import android.net.Uri;
import java.io.File;
import org.matrix.androidsdk.core.Log;

public class LocationMessage extends Message {
    private static final String LOG_TAG = "LocationMessage";
    public String geo_uri;
    public ThumbnailInfo thumbnail_info;
    public String thumbnail_url;

    public LocationMessage() {
        this.msgtype = Message.MSGTYPE_LOCATION;
    }

    public LocationMessage deepCopy() {
        LocationMessage locationMessage = new LocationMessage();
        locationMessage.msgtype = this.msgtype;
        locationMessage.body = this.body;
        locationMessage.geo_uri = this.geo_uri;
        locationMessage.thumbnail_url = this.thumbnail_url;
        ThumbnailInfo thumbnailInfo = this.thumbnail_info;
        if (thumbnailInfo != null) {
            locationMessage.thumbnail_info = thumbnailInfo.deepCopy();
        }
        return locationMessage;
    }

    public boolean isLocalThumbnailContent() {
        String str = this.thumbnail_url;
        return str != null && str.startsWith(Message.FILE_SCHEME);
    }

    public void checkMediaUrls() {
        String str = this.thumbnail_url;
        if (str != null && str.startsWith(Message.FILE_SCHEME)) {
            try {
                if (!new File(Uri.parse(this.thumbnail_url).getPath()).exists()) {
                    this.thumbnail_url = null;
                }
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("## checkMediaUrls() failed ");
                sb.append(e.getMessage());
                Log.e(LOG_TAG, sb.toString(), e);
            }
        }
    }
}
