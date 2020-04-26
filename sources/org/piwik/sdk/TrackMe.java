package org.piwik.sdk;

import java.util.HashMap;
import java.util.Map;

public class TrackMe {
    private static final int DEFAULT_QUERY_CAPACITY = 14;
    private final HashMap<String, String> mQueryParams = new HashMap<>(14);

    public TrackMe() {
    }

    public TrackMe(TrackMe trackMe) {
        this.mQueryParams.putAll(trackMe.mQueryParams);
    }

    public TrackMe putAll(TrackMe trackMe) {
        this.mQueryParams.putAll(trackMe.toMap());
        return this;
    }

    public synchronized TrackMe set(String str, String str2) {
        if (str2 == null) {
            this.mQueryParams.remove(str);
        } else if (str2.length() > 0) {
            this.mQueryParams.put(str, str2);
        }
        return this;
    }

    public synchronized String get(String str) {
        return (String) this.mQueryParams.get(str);
    }

    public synchronized TrackMe set(QueryParams queryParams, String str) {
        set(queryParams.toString(), str);
        return this;
    }

    public synchronized TrackMe set(QueryParams queryParams, int i) {
        set(queryParams, Integer.toString(i));
        return this;
    }

    public synchronized TrackMe set(QueryParams queryParams, float f) {
        set(queryParams, Float.toString(f));
        return this;
    }

    public synchronized TrackMe set(QueryParams queryParams, long j) {
        set(queryParams, Long.toString(j));
        return this;
    }

    public synchronized boolean has(QueryParams queryParams) {
        return this.mQueryParams.containsKey(queryParams.toString());
    }

    public synchronized TrackMe trySet(QueryParams queryParams, int i) {
        return trySet(queryParams, String.valueOf(i));
    }

    public synchronized TrackMe trySet(QueryParams queryParams, float f) {
        return trySet(queryParams, String.valueOf(f));
    }

    public synchronized TrackMe trySet(QueryParams queryParams, long j) {
        return trySet(queryParams, String.valueOf(j));
    }

    public synchronized TrackMe trySet(QueryParams queryParams, String str) {
        if (!has(queryParams)) {
            set(queryParams, str);
        }
        return this;
    }

    public synchronized Map<String, String> toMap() {
        return new HashMap(this.mQueryParams);
    }

    public synchronized String get(QueryParams queryParams) {
        return (String) this.mQueryParams.get(queryParams.toString());
    }

    public synchronized boolean isEmpty() {
        return this.mQueryParams.isEmpty();
    }
}
