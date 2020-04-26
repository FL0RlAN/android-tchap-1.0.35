package org.piwik.sdk;

import android.content.SharedPreferences;
import im.vector.adapters.AdapterUtils;
import im.vector.util.UrlUtilKt;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.apache.commons.cli.HelpFormatter;
import org.piwik.sdk.dispatcher.DispatchMode;
import org.piwik.sdk.dispatcher.Dispatcher;
import org.piwik.sdk.dispatcher.Packet;
import timber.log.Timber;

public class Tracker {
    private static final String DEFAULT_API_VERSION_VALUE = "1";
    private static final String DEFAULT_RECORD_VALUE = "1";
    private static final String DEFAULT_TRUE_VALUE = "1";
    private static final String DEFAULT_UNKNOWN_VALUE = "unknown";
    private static final String LOGGER_TAG = "PIWIK:Tracker";
    private static final Pattern PATTERN_VISITOR_ID = Pattern.compile("^[0-9a-f]{16}$");
    protected static final String PREF_KEY_DISPATCHER_MODE = "tracker.dispatcher.mode";
    protected static final String PREF_KEY_OFFLINE_CACHE_AGE = "tracker.cache.age";
    protected static final String PREF_KEY_OFFLINE_CACHE_SIZE = "tracker.cache.size";
    protected static final String PREF_KEY_TRACKER_FIRSTVISIT = "tracker.firstvisit";
    protected static final String PREF_KEY_TRACKER_OPTOUT = "tracker.optout";
    protected static final String PREF_KEY_TRACKER_PREVIOUSVISIT = "tracker.previousvisit";
    protected static final String PREF_KEY_TRACKER_USERID = "tracker.userid";
    protected static final String PREF_KEY_TRACKER_VISITCOUNT = "tracker.visitcount";
    private final URL mApiUrl;
    private String mApplicationDomain;
    private final TrackMe mDefaultTrackMe = new TrackMe();
    private final Dispatcher mDispatcher;
    private TrackMe mLastEvent;
    private final String mName;
    private boolean mOptOut = false;
    private final Piwik mPiwik;
    private SharedPreferences mPreferences;
    private final Random mRandomAntiCachingValue = new Random(new Date().getTime());
    private final Object mSessionLock = new Object();
    private CountDownLatch mSessionStartLatch = new CountDownLatch(0);
    private long mSessionStartTime;
    private long mSessionTimeout = 1800000;
    private final int mSiteId;

    public Tracker(Piwik piwik, TrackerConfig trackerConfig) {
        String str;
        this.mPiwik = piwik;
        this.mApiUrl = trackerConfig.getApiUrl();
        this.mSiteId = trackerConfig.getSiteId();
        this.mName = trackerConfig.getTrackerName();
        new LegacySettingsPorter(piwik).port(this);
        this.mOptOut = getPreferences().getBoolean(PREF_KEY_TRACKER_OPTOUT, false);
        this.mDispatcher = piwik.getDispatcherFactory().build(this);
        SharedPreferences preferences = getPreferences();
        String str2 = PREF_KEY_TRACKER_USERID;
        String string = preferences.getString(str2, null);
        if (string == null) {
            string = UUID.randomUUID().toString();
            getPreferences().edit().putString(str2, string).apply();
        }
        this.mDefaultTrackMe.set(QueryParams.USER_ID, string);
        this.mDefaultTrackMe.set(QueryParams.SESSION_START, "1");
        int[] resolution = this.mPiwik.getDeviceHelper().getResolution();
        if (resolution != null) {
            str = String.format("%sx%s", new Object[]{Integer.valueOf(resolution[0]), Integer.valueOf(resolution[1])});
        } else {
            str = "unknown";
        }
        this.mDefaultTrackMe.set(QueryParams.SCREEN_RESOLUTION, str);
        this.mDefaultTrackMe.set(QueryParams.USER_AGENT, this.mPiwik.getDeviceHelper().getUserAgent());
        this.mDefaultTrackMe.set(QueryParams.LANGUAGE, this.mPiwik.getDeviceHelper().getUserLanguage());
        this.mDefaultTrackMe.set(QueryParams.VISITOR_ID, makeRandomVisitorId());
        this.mDefaultTrackMe.set(QueryParams.URL_PATH, fixUrl(null, getApplicationBaseURL()));
    }

    public void setOptOut(boolean z) {
        this.mOptOut = z;
        getPreferences().edit().putBoolean(PREF_KEY_TRACKER_OPTOUT, z).apply();
        this.mDispatcher.clear();
    }

    public boolean isOptOut() {
        return this.mOptOut;
    }

    public String getName() {
        return this.mName;
    }

    public Piwik getPiwik() {
        return this.mPiwik;
    }

    public URL getAPIUrl() {
        return this.mApiUrl;
    }

    /* access modifiers changed from: protected */
    public int getSiteId() {
        return this.mSiteId;
    }

    public TrackMe getDefaultTrackMe() {
        return this.mDefaultTrackMe;
    }

    public void startNewSession() {
        synchronized (this.mSessionLock) {
            this.mSessionStartTime = 0;
        }
    }

    public void setSessionTimeout(int i) {
        synchronized (this.mSessionLock) {
            this.mSessionTimeout = (long) i;
        }
    }

    /* access modifiers changed from: protected */
    public boolean tryNewSession() {
        boolean z;
        synchronized (this.mSessionLock) {
            z = System.currentTimeMillis() - this.mSessionStartTime > this.mSessionTimeout;
            this.mSessionStartTime = System.currentTimeMillis();
        }
        return z;
    }

    public long getSessionTimeout() {
        return this.mSessionTimeout;
    }

    public int getDispatchTimeout() {
        return this.mDispatcher.getConnectionTimeOut();
    }

    public void setDispatchTimeout(int i) {
        this.mDispatcher.setConnectionTimeOut(i);
    }

    public void dispatch() {
        if (!this.mOptOut) {
            this.mDispatcher.forceDispatch();
        }
    }

    public Tracker setDispatchInterval(long j) {
        this.mDispatcher.setDispatchInterval(j);
        return this;
    }

    public Tracker setDispatchGzipped(boolean z) {
        this.mDispatcher.setDispatchGzipped(z);
        return this;
    }

    public long getDispatchInterval() {
        return this.mDispatcher.getDispatchInterval();
    }

    public void setOfflineCacheAge(long j) {
        getPreferences().edit().putLong(PREF_KEY_OFFLINE_CACHE_AGE, j).apply();
    }

    public long getOfflineCacheAge() {
        return getPreferences().getLong(PREF_KEY_OFFLINE_CACHE_AGE, AdapterUtils.MS_IN_DAY);
    }

    public void setOfflineCacheSize(long j) {
        getPreferences().edit().putLong(PREF_KEY_OFFLINE_CACHE_SIZE, j).apply();
    }

    public long getOfflineCacheSize() {
        return getPreferences().getLong(PREF_KEY_OFFLINE_CACHE_SIZE, 4194304);
    }

    public DispatchMode getDispatchMode() {
        DispatchMode fromString = DispatchMode.fromString(getPreferences().getString(PREF_KEY_DISPATCHER_MODE, null));
        if (fromString != null) {
            return fromString;
        }
        DispatchMode dispatchMode = DispatchMode.ALWAYS;
        setDispatchMode(dispatchMode);
        return dispatchMode;
    }

    public void setDispatchMode(DispatchMode dispatchMode) {
        getPreferences().edit().putString(PREF_KEY_DISPATCHER_MODE, dispatchMode.toString()).apply();
        this.mDispatcher.setDispatchMode(dispatchMode);
    }

    public Tracker setUserId(String str) {
        this.mDefaultTrackMe.set(QueryParams.USER_ID, str);
        getPreferences().edit().putString(PREF_KEY_TRACKER_USERID, str).apply();
        return this;
    }

    public String getUserId() {
        return this.mDefaultTrackMe.get(QueryParams.USER_ID);
    }

    public Tracker setVisitorId(String str) throws IllegalArgumentException {
        if (confirmVisitorIdFormat(str)) {
            this.mDefaultTrackMe.set(QueryParams.VISITOR_ID, str);
        }
        return this;
    }

    public String getVisitorId() {
        return this.mDefaultTrackMe.get(QueryParams.VISITOR_ID);
    }

    private boolean confirmVisitorIdFormat(String str) throws IllegalArgumentException {
        if (PATTERN_VISITOR_ID.matcher(str).matches()) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("VisitorId: ");
        sb.append(str);
        sb.append(" is not of valid format,  the format must match the regular expression: ");
        sb.append(PATTERN_VISITOR_ID.pattern());
        throw new IllegalArgumentException(sb.toString());
    }

    public Tracker setApplicationDomain(String str) {
        this.mApplicationDomain = str;
        this.mDefaultTrackMe.set(QueryParams.URL_PATH, fixUrl(null, getApplicationBaseURL()));
        return this;
    }

    /* access modifiers changed from: protected */
    public String getApplicationDomain() {
        String str = this.mApplicationDomain;
        return str != null ? str : this.mPiwik.getApplicationDomain();
    }

    private void injectInitialParams(TrackMe trackMe) {
        long j;
        long j2;
        long j3;
        TrackMe trackMe2 = trackMe;
        synchronized (getPreferences()) {
            j = getPreferences().getLong(PREF_KEY_TRACKER_VISITCOUNT, 0) + 1;
            getPreferences().edit().putLong(PREF_KEY_TRACKER_VISITCOUNT, j).apply();
        }
        synchronized (getPreferences()) {
            j2 = getPreferences().getLong(PREF_KEY_TRACKER_FIRSTVISIT, -1);
            if (j2 == -1) {
                j2 = System.currentTimeMillis() / 1000;
                getPreferences().edit().putLong(PREF_KEY_TRACKER_FIRSTVISIT, j2).apply();
            }
        }
        synchronized (getPreferences()) {
            j3 = getPreferences().getLong(PREF_KEY_TRACKER_PREVIOUSVISIT, -1);
            getPreferences().edit().putLong(PREF_KEY_TRACKER_PREVIOUSVISIT, System.currentTimeMillis() / 1000).apply();
        }
        this.mDefaultTrackMe.trySet(QueryParams.FIRST_VISIT_TIMESTAMP, j2);
        this.mDefaultTrackMe.trySet(QueryParams.TOTAL_NUMBER_OF_VISITS, j);
        if (j3 != -1) {
            this.mDefaultTrackMe.trySet(QueryParams.PREVIOUS_VISIT_TIMESTAMP, j3);
        }
        trackMe2.trySet(QueryParams.SESSION_START, this.mDefaultTrackMe.get(QueryParams.SESSION_START));
        trackMe2.trySet(QueryParams.SCREEN_RESOLUTION, this.mDefaultTrackMe.get(QueryParams.SCREEN_RESOLUTION));
        trackMe2.trySet(QueryParams.USER_AGENT, this.mDefaultTrackMe.get(QueryParams.USER_AGENT));
        trackMe2.trySet(QueryParams.LANGUAGE, this.mDefaultTrackMe.get(QueryParams.LANGUAGE));
        trackMe2.trySet(QueryParams.FIRST_VISIT_TIMESTAMP, this.mDefaultTrackMe.get(QueryParams.FIRST_VISIT_TIMESTAMP));
        trackMe2.trySet(QueryParams.TOTAL_NUMBER_OF_VISITS, this.mDefaultTrackMe.get(QueryParams.TOTAL_NUMBER_OF_VISITS));
        trackMe2.trySet(QueryParams.PREVIOUS_VISIT_TIMESTAMP, this.mDefaultTrackMe.get(QueryParams.PREVIOUS_VISIT_TIMESTAMP));
    }

    private void injectBaseParams(TrackMe trackMe) {
        String str;
        trackMe.trySet(QueryParams.SITE_ID, this.mSiteId);
        String str2 = "1";
        trackMe.trySet(QueryParams.RECORD, str2);
        trackMe.trySet(QueryParams.API_VERSION, str2);
        trackMe.trySet(QueryParams.RANDOM_NUMBER, this.mRandomAntiCachingValue.nextInt(100000));
        trackMe.trySet(QueryParams.DATETIME_OF_REQUEST, new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.US).format(new Date()));
        trackMe.trySet(QueryParams.SEND_IMAGE, "0");
        trackMe.trySet(QueryParams.VISITOR_ID, this.mDefaultTrackMe.get(QueryParams.VISITOR_ID));
        trackMe.trySet(QueryParams.USER_ID, this.mDefaultTrackMe.get(QueryParams.USER_ID));
        String str3 = trackMe.get(QueryParams.URL_PATH);
        if (str3 == null) {
            str = this.mDefaultTrackMe.get(QueryParams.URL_PATH);
        } else {
            str = fixUrl(str3, getApplicationBaseURL());
            this.mDefaultTrackMe.set(QueryParams.URL_PATH, str);
        }
        trackMe.set(QueryParams.URL_PATH, str);
    }

    private static String fixUrl(String str, String str2) {
        String str3 = "/";
        if (str == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(str3);
            str = sb.toString();
        }
        if (str.startsWith("http://") || str.startsWith(UrlUtilKt.HTTPS_SCHEME) || str.startsWith("ftp://")) {
            return str;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str2);
        if (str.startsWith(str3)) {
            str3 = "";
        }
        sb2.append(str3);
        sb2.append(str);
        return sb2.toString();
    }

    public Tracker track(TrackMe trackMe) {
        boolean tryNewSession;
        synchronized (this.mSessionLock) {
            tryNewSession = tryNewSession();
            if (tryNewSession) {
                this.mSessionStartLatch = new CountDownLatch(1);
            }
        }
        if (tryNewSession) {
            injectInitialParams(trackMe);
        } else {
            try {
                this.mSessionStartLatch.await((long) getDispatchTimeout(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Timber.tag("ContentValues").e(e, null, new Object[0]);
            }
        }
        injectBaseParams(trackMe);
        this.mLastEvent = trackMe;
        if (!this.mOptOut) {
            this.mDispatcher.submit(trackMe);
            Timber.tag(LOGGER_TAG).d("Event added to the queue: %s", trackMe);
        } else {
            Timber.tag(LOGGER_TAG).d("Event omitted due to opt out: %s", trackMe);
        }
        if (tryNewSession) {
            this.mSessionStartLatch.countDown();
        }
        return this;
    }

    public static String makeRandomVisitorId() {
        return UUID.randomUUID().toString().replaceAll(HelpFormatter.DEFAULT_OPT_PREFIX, "").substring(0, 16);
    }

    public SharedPreferences getPreferences() {
        if (this.mPreferences == null) {
            this.mPreferences = this.mPiwik.getTrackerPreferences(this);
        }
        return this.mPreferences;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Tracker tracker = (Tracker) obj;
        if (this.mSiteId == tracker.mSiteId && this.mApiUrl.equals(tracker.mApiUrl)) {
            return this.mName.equals(tracker.mName);
        }
        return false;
    }

    public int hashCode() {
        return (((this.mApiUrl.hashCode() * 31) + this.mSiteId) * 31) + this.mName.hashCode();
    }

    /* access modifiers changed from: protected */
    public String getApplicationBaseURL() {
        return String.format("http://%s", new Object[]{getApplicationDomain()});
    }

    public TrackMe getLastEventX() {
        return this.mLastEvent;
    }

    public void setDryRunTarget(List<Packet> list) {
        this.mDispatcher.setDryRunTarget(list);
    }

    public List<Packet> getDryRunTarget() {
        return this.mDispatcher.getDryRunTarget();
    }
}
