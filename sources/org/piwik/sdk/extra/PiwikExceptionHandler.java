package org.piwik.sdk.extra;

import java.lang.Thread.UncaughtExceptionHandler;
import org.piwik.sdk.TrackMe;
import org.piwik.sdk.Tracker;
import timber.log.Timber;

public class PiwikExceptionHandler implements UncaughtExceptionHandler {
    private static final String LOGGER_TAG = "PIWIK:PiwikExceptionHandler";
    private final UncaughtExceptionHandler mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    private final TrackMe mTrackMe;
    private final Tracker mTracker;

    public PiwikExceptionHandler(Tracker tracker, TrackMe trackMe) {
        this.mTracker = tracker;
        this.mTrackMe = trackMe;
    }

    public Tracker getTracker() {
        return this.mTracker;
    }

    public UncaughtExceptionHandler getDefaultExceptionHandler() {
        return this.mDefaultExceptionHandler;
    }

    public void uncaughtException(Thread thread, Throwable th) {
        try {
            TrackHelper.track(this.mTrackMe).exception(th).description(th.getMessage()).fatal(true).with(getTracker());
            getTracker().dispatch();
            if (getDefaultExceptionHandler() == null || getDefaultExceptionHandler() == this) {
                return;
            }
        } catch (Exception e) {
            Timber.tag(LOGGER_TAG).e(e, "Couldn't track uncaught exception", new Object[0]);
            if (getDefaultExceptionHandler() == null || getDefaultExceptionHandler() == this) {
                return;
            }
        } catch (Throwable th2) {
            if (!(getDefaultExceptionHandler() == null || getDefaultExceptionHandler() == this)) {
                getDefaultExceptionHandler().uncaughtException(thread, th);
            }
            throw th2;
        }
        getDefaultExceptionHandler().uncaughtException(thread, th);
    }
}
