package org.piwik.sdk.extra;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import java.io.File;
import org.piwik.sdk.QueryParams;
import org.piwik.sdk.TrackMe;
import org.piwik.sdk.Tracker;
import org.piwik.sdk.tools.Checksum;
import timber.log.Timber;

public class DownloadTracker {
    private static final String INSTALL_SOURCE_GOOGLE_PLAY = "com.android.vending";
    protected static final String LOGGER_TAG = "PIWIK:DownloadTrackingHelper";
    private final Context mContext;
    private final boolean mInternalTracking;
    private final PackageManager mPackMan;
    private final PackageInfo mPkgInfo;
    private final SharedPreferences mPreferences;
    private final Object mTrackOnceLock;
    private final Tracker mTracker;
    private String mVersion;

    public interface Extra {

        public static class ApkChecksum implements Extra {
            private PackageInfo mPackageInfo;

            public boolean isIntensiveWork() {
                return true;
            }

            public ApkChecksum(Context context) {
                try {
                    this.mPackageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                } catch (Exception e) {
                    Timber.tag(DownloadTracker.LOGGER_TAG).e(e);
                    this.mPackageInfo = null;
                }
            }

            public ApkChecksum(PackageInfo packageInfo) {
                this.mPackageInfo = packageInfo;
            }

            public String buildExtraIdentifier() {
                PackageInfo packageInfo = this.mPackageInfo;
                if (!(packageInfo == null || packageInfo.applicationInfo == null || this.mPackageInfo.applicationInfo.sourceDir == null)) {
                    try {
                        return Checksum.getMD5Checksum(new File(this.mPackageInfo.applicationInfo.sourceDir));
                    } catch (Exception e) {
                        Timber.tag(DownloadTracker.LOGGER_TAG).e(e);
                    }
                }
                return null;
            }
        }

        public static abstract class Custom implements Extra {
        }

        public static class None implements Extra {
            public String buildExtraIdentifier() {
                return null;
            }

            public boolean isIntensiveWork() {
                return false;
            }
        }

        String buildExtraIdentifier();

        boolean isIntensiveWork();
    }

    public DownloadTracker(Tracker tracker) {
        this(tracker, getOurPackageInfo(tracker.getPiwik().getContext()));
    }

    private static PackageInfo getOurPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            Timber.tag(LOGGER_TAG).e(e);
            throw new RuntimeException(e);
        }
    }

    public DownloadTracker(Tracker tracker, PackageInfo packageInfo) {
        this.mTrackOnceLock = new Object();
        this.mTracker = tracker;
        this.mContext = tracker.getPiwik().getContext();
        this.mPreferences = tracker.getPreferences();
        this.mPackMan = tracker.getPiwik().getContext().getPackageManager();
        this.mPkgInfo = packageInfo;
        this.mInternalTracking = this.mPkgInfo.packageName.equals(this.mContext.getPackageName());
    }

    public void setVersion(String str) {
        this.mVersion = str;
    }

    public String getVersion() {
        String str = this.mVersion;
        if (str != null) {
            return str;
        }
        return Integer.toString(this.mPkgInfo.versionCode);
    }

    public void trackOnce(TrackMe trackMe, Extra extra) {
        StringBuilder sb = new StringBuilder();
        sb.append("downloaded:");
        sb.append(this.mPkgInfo.packageName);
        sb.append(":");
        sb.append(getVersion());
        String sb2 = sb.toString();
        synchronized (this.mTrackOnceLock) {
            if (!this.mPreferences.getBoolean(sb2, false)) {
                this.mPreferences.edit().putBoolean(sb2, true).apply();
                trackNewAppDownload(trackMe, extra);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x001c  */
    public void trackNewAppDownload(final TrackMe trackMe, final Extra extra) {
        final boolean z;
        Thread thread;
        if (this.mInternalTracking) {
            if ("com.android.vending".equals(this.mPackMan.getInstallerPackageName(this.mPkgInfo.packageName))) {
                z = true;
                if (z) {
                    Timber.tag(LOGGER_TAG).d("Google Play is install source, deferring tracking.", new Object[0]);
                }
                thread = new Thread(new Runnable() {
                    public void run() {
                        if (z) {
                            try {
                                Thread.sleep(3000);
                            } catch (Exception e) {
                                Timber.tag("ContentValues").e(e, null, new Object[0]);
                            }
                        }
                        DownloadTracker.this.trackNewAppDownloadInternal(trackMe, extra);
                    }
                });
                if (!z || extra.isIntensiveWork()) {
                    thread.start();
                } else {
                    thread.run();
                    return;
                }
            }
        }
        z = false;
        if (z) {
        }
        thread = new Thread(new Runnable() {
            public void run() {
                if (z) {
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        Timber.tag("ContentValues").e(e, null, new Object[0]);
                    }
                }
                DownloadTracker.this.trackNewAppDownloadInternal(trackMe, extra);
            }
        });
        if (!z) {
        }
        thread.start();
    }

    /* access modifiers changed from: private */
    public void trackNewAppDownloadInternal(TrackMe trackMe, Extra extra) {
        String str = LOGGER_TAG;
        Timber.tag(str).d("Tracking app download...", new Object[0]);
        StringBuilder sb = new StringBuilder();
        String str2 = "http://";
        sb.append(str2);
        sb.append(this.mPkgInfo.packageName);
        sb.append(":");
        sb.append(getVersion());
        String buildExtraIdentifier = extra.buildExtraIdentifier();
        if (buildExtraIdentifier != null) {
            sb.append("/");
            sb.append(buildExtraIdentifier);
        }
        String installerPackageName = this.mPackMan.getInstallerPackageName(this.mPkgInfo.packageName);
        if (installerPackageName != null && installerPackageName.length() > 200) {
            installerPackageName = installerPackageName.substring(0, 200);
        }
        if (installerPackageName != null && installerPackageName.equals("com.android.vending")) {
            String string = this.mTracker.getPiwik().getPiwikPreferences().getString("referrer.extras", null);
            if (string != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(installerPackageName);
                sb2.append("/?");
                sb2.append(string);
                installerPackageName = sb2.toString();
            }
        }
        if (installerPackageName != null) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str2);
            sb3.append(installerPackageName);
            installerPackageName = sb3.toString();
        }
        this.mTracker.track(trackMe.set(QueryParams.EVENT_CATEGORY, "Application").set(QueryParams.EVENT_ACTION, "downloaded").set(QueryParams.ACTION_NAME, "application/downloaded").set(QueryParams.URL_PATH, "/application/downloaded").set(QueryParams.DOWNLOAD, sb.toString()).set(QueryParams.REFERRER, installerPackageName));
        Timber.tag(str).d("... app download tracked.", new Object[0]);
    }
}
