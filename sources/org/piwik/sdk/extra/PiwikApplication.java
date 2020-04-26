package org.piwik.sdk.extra;

import android.app.Application;
import android.os.Build.VERSION;
import org.piwik.sdk.Piwik;
import org.piwik.sdk.Tracker;
import org.piwik.sdk.TrackerConfig;

public abstract class PiwikApplication extends Application {
    private Tracker mPiwikTracker;

    public abstract TrackerConfig onCreateTrackerConfig();

    public Piwik getPiwik() {
        return Piwik.getInstance(this);
    }

    public synchronized Tracker getTracker() {
        if (this.mPiwikTracker == null) {
            this.mPiwikTracker = getPiwik().newTracker(onCreateTrackerConfig());
        }
        return this.mPiwikTracker;
    }

    public void onLowMemory() {
        if (VERSION.SDK_INT < 14) {
            Tracker tracker = this.mPiwikTracker;
            if (tracker != null) {
                tracker.dispatch();
            }
        }
        super.onLowMemory();
    }

    public void onTrimMemory(int i) {
        if (i == 20 || i == 80) {
            Tracker tracker = this.mPiwikTracker;
            if (tracker != null) {
                tracker.dispatch();
            }
        }
        super.onTrimMemory(i);
    }
}
