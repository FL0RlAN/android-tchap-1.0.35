package org.piwik.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;
import java.util.Map;
import org.piwik.sdk.dispatcher.DispatcherFactory;
import org.piwik.sdk.tools.BuildInfo;
import org.piwik.sdk.tools.Checksum;
import org.piwik.sdk.tools.DeviceHelper;
import org.piwik.sdk.tools.PropertySource;
import timber.log.Timber;

public class Piwik {
    private static final String BASE_PREFERENCE_FILE = "org.piwik.sdk";
    public static final String LOGGER_PREFIX = "PIWIK:";
    private static final String LOGGER_TAG = "PIWIK";
    private static Piwik sInstance;
    private SharedPreferences mBasePreferences;
    private final Context mContext;
    private final Map<Tracker, SharedPreferences> mPreferenceMap = new HashMap();

    public static synchronized Piwik getInstance(Context context) {
        Piwik piwik;
        Class<Piwik> cls = Piwik.class;
        synchronized (cls) {
            if (sInstance == null) {
                synchronized (cls) {
                    if (sInstance == null) {
                        sInstance = new Piwik(context);
                    }
                }
            }
            piwik = sInstance;
        }
        return piwik;
    }

    private Piwik(Context context) {
        this.mContext = context.getApplicationContext();
        this.mBasePreferences = context.getSharedPreferences("org.piwik.sdk", 0);
    }

    public Context getContext() {
        return this.mContext;
    }

    public synchronized Tracker newTracker(TrackerConfig trackerConfig) {
        return new Tracker(this, trackerConfig);
    }

    public String getApplicationDomain() {
        return getContext().getPackageName();
    }

    public SharedPreferences getPiwikPreferences() {
        return this.mBasePreferences;
    }

    public SharedPreferences getTrackerPreferences(Tracker tracker) {
        SharedPreferences sharedPreferences;
        String str;
        synchronized (this.mPreferenceMap) {
            sharedPreferences = (SharedPreferences) this.mPreferenceMap.get(tracker);
            if (sharedPreferences == null) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("org.piwik.sdk_");
                    sb.append(Checksum.getMD5Checksum(tracker.getName()));
                    str = sb.toString();
                } catch (Exception e) {
                    Timber.tag(LOGGER_TAG).e(e, null, new Object[0]);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("org.piwik.sdk_");
                    sb2.append(tracker.getName());
                    str = sb2.toString();
                }
                sharedPreferences = getContext().getSharedPreferences(str, 0);
                this.mPreferenceMap.put(tracker, sharedPreferences);
            }
        }
        return sharedPreferences;
    }

    /* access modifiers changed from: protected */
    public DispatcherFactory getDispatcherFactory() {
        return new DispatcherFactory();
    }

    /* access modifiers changed from: 0000 */
    public DeviceHelper getDeviceHelper() {
        return new DeviceHelper(this.mContext, new PropertySource(), new BuildInfo());
    }
}
