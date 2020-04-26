package org.piwik.sdk.dispatcher;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;

public class Packet {
    private final int mEventCount;
    private final JSONObject mPostData;
    private final URL mTargetURL;
    private final long mTimeStamp;

    public Packet(URL url) {
        this(url, null, 1);
    }

    public Packet(URL url, JSONObject jSONObject, int i) {
        this.mTargetURL = url;
        this.mPostData = jSONObject;
        this.mEventCount = i;
        this.mTimeStamp = System.currentTimeMillis();
    }

    /* access modifiers changed from: protected */
    public URL getTargetURL() {
        return this.mTargetURL;
    }

    /* access modifiers changed from: 0000 */
    public URLConnection openConnection() throws IOException {
        return this.mTargetURL.openConnection();
    }

    public JSONObject getPostData() {
        return this.mPostData;
    }

    public long getTimeStamp() {
        return this.mTimeStamp;
    }

    public int getEventCount() {
        return this.mEventCount;
    }
}
