package org.piwik.sdk;

import java.net.MalformedURLException;
import java.net.URL;

public class TrackerConfig {
    private final URL mApiUrl;
    private final int mSiteId;
    private final String mTrackerName;

    public static TrackerConfig createDefault(String str, int i) {
        return new TrackerConfig(str, i, "Default Tracker");
    }

    public TrackerConfig(String str, int i, String str2) {
        String str3 = "/";
        String str4 = "piwik.php";
        try {
            if (!str.endsWith(str4)) {
                if (!str.endsWith("piwik-proxy.php")) {
                    if (!str.endsWith(str3)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(str3);
                        str = sb.toString();
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(str4);
                    this.mApiUrl = new URL(sb2.toString());
                    this.mSiteId = i;
                    this.mTrackerName = str2;
                }
            }
            this.mApiUrl = new URL(str);
            this.mSiteId = i;
            this.mTrackerName = str2;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public URL getApiUrl() {
        return this.mApiUrl;
    }

    public int getSiteId() {
        return this.mSiteId;
    }

    public String getTrackerName() {
        return this.mTrackerName;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TrackerConfig trackerConfig = (TrackerConfig) obj;
        if (this.mSiteId != trackerConfig.mSiteId || !this.mApiUrl.equals(trackerConfig.mApiUrl) || !this.mTrackerName.equals(trackerConfig.mTrackerName)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((this.mApiUrl.hashCode() * 31) + this.mSiteId) * 31) + this.mTrackerName.hashCode();
    }
}
