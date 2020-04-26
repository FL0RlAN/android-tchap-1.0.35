package org.piwik.sdk.dispatcher;

import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import kotlin.text.Typography;
import timber.log.Timber;

public class Event {
    private static final String LOGGER_TAG = "PIWIK:Event";
    private final String mQuery;
    private final long mTimestamp;

    public Event(Map<String, String> map) {
        this(urlEncodeUTF8(map));
    }

    public Event(String str) {
        this(System.currentTimeMillis(), str);
    }

    public Event(long j, String str) {
        this.mTimestamp = j;
        this.mQuery = str;
    }

    public long getTimeStamp() {
        return this.mTimestamp;
    }

    public String getEncodedQuery() {
        return this.mQuery;
    }

    public String toString() {
        return getEncodedQuery();
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Event event = (Event) obj;
        if (this.mTimestamp != event.mTimestamp || !this.mQuery.equals(event.mQuery)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        long j = this.mTimestamp;
        return (((int) (j ^ (j >>> 32))) * 31) + this.mQuery.hashCode();
    }

    private static String urlEncodeUTF8(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8").replaceAll("\\+", "%20");
        } catch (Exception e) {
            Timber.tag(LOGGER_TAG).e(e, "Cannot encode %s", str);
            return "";
        }
    }

    private static String urlEncodeUTF8(Map<String, String> map) {
        StringBuilder sb = new StringBuilder(100);
        sb.append('?');
        for (Entry entry : map.entrySet()) {
            sb.append(urlEncodeUTF8((String) entry.getKey()));
            sb.append('=');
            sb.append(urlEncodeUTF8((String) entry.getValue()));
            sb.append(Typography.amp);
        }
        return sb.substring(0, sb.length() - 1);
    }
}
