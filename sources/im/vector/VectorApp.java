package im.vector;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import fr.gouv.tchap.realm.VectorRealmMigration;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.JitsiCallActivity;
import im.vector.activity.VectorCallViewActivity;
import im.vector.activity.WidgetActivity;
import im.vector.adapters.AdapterUtils;
import im.vector.analytics.Analytics;
import im.vector.analytics.AppAnalytics;
import im.vector.analytics.e2e.DecryptionFailureTracker;
import im.vector.contacts.ContactsManager;
import im.vector.contacts.PIDsRetriever;
import im.vector.push.PushManager;
import im.vector.services.EventStreamService;
import im.vector.settings.FontScale;
import im.vector.settings.VectorLocale;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.CallsManager;
import im.vector.util.PermissionsToolsKt;
import im.vector.util.PhoneNumberUtils;
import im.vector.util.PreferencesManager;
import im.vector.util.RageShake;
import im.vector.util.VectorMarkdownParser;
import im.vector.util.VectorMarkdownParser.IVectorMarkdownParserListener;
import io.realm.Realm;
import io.realm.RealmConfiguration.Builder;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.MXCryptoConfig;

public class VectorApp extends MultiDexApplication {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorApp.class.getSimpleName();
    private static final long MAX_ACTIVITY_TRANSITION_TIME_MS = 4000;
    private static final String PREFS_CRASH_KEY = "PREFS_CRASH_KEY";
    private static String SDK_VERSION_STRING;
    private static String VECTOR_VERSION_STRING;
    private static VectorApp instance = null;
    /* access modifiers changed from: private */
    public static Activity mCurrentActivity = null;
    public static File mLogsDirectoryFile = null;
    private static Bitmap mSavedPickerImagePreview = null;
    private static final Set<MXSession> mSyncingSessions = new HashSet();
    /* access modifiers changed from: private */
    public Timer mActivityTransitionTimer;
    /* access modifiers changed from: private */
    public TimerTask mActivityTransitionTimerTask;
    private Analytics mAppAnalytics;
    /* access modifiers changed from: private */
    public CallsManager mCallsManager;
    /* access modifiers changed from: private */
    public final List<String> mCreatedActivities = new ArrayList();
    private DecryptionFailureTracker mDecryptionFailureTracker;
    /* access modifiers changed from: private */
    public boolean mIsCallingInBackground = false;
    /* access modifiers changed from: private */
    public boolean mIsInBackground = true;
    private final BroadcastReceiver mLanguageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (!TextUtils.equals(Locale.getDefault().toString(), VectorLocale.INSTANCE.getApplicationLocale().toString())) {
                String access$000 = VectorApp.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## onReceive() : the locale has been updated to ");
                sb.append(Locale.getDefault().toString());
                sb.append(", restore the expected value ");
                sb.append(VectorLocale.INSTANCE.getApplicationLocale().toString());
                Log.d(access$000, sb.toString());
                VectorApp.updateApplicationSettings(VectorLocale.INSTANCE.getApplicationLocale(), FontScale.INSTANCE.getFontScalePrefValue(), ThemeUtils.INSTANCE.getApplicationTheme(context));
                if (VectorApp.getCurrentActivity() != null) {
                    VectorApp.this.restartActivity(VectorApp.getCurrentActivity());
                }
            }
        }
    };
    private long mLastMediasCheck = 0;
    private VectorMarkdownParser mMarkdownParser;
    private final EventEmitter<Activity> mOnActivityDestroyedListener = new EventEmitter<>();
    private RageShake mRageShake;

    static {
        String str = "";
        VECTOR_VERSION_STRING = str;
        SDK_VERSION_STRING = str;
    }

    public static VectorApp getInstance() {
        return instance;
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    public void onCreate() {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(new Builder().schemaVersion(0).migration(new VectorRealmMigration()).build());
        MXSession.initUserAgent(this, BuildConfig.FLAVOR);
        instance = this;
        this.mCallsManager = new CallsManager(this);
        this.mAppAnalytics = new AppAnalytics(this, new Analytics[0]);
        this.mDecryptionFailureTracker = new DecryptionFailureTracker(this.mAppAnalytics);
        this.mActivityTransitionTimer = null;
        this.mActivityTransitionTimerTask = null;
        VECTOR_VERSION_STRING = Matrix.getInstance(this).getVersion(true, true);
        if (Matrix.getInstance(this).getDefaultSession() != null) {
            SDK_VERSION_STRING = Matrix.getInstance(this).getDefaultSession().getVersion(true);
        } else {
            SDK_VERSION_STRING = "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getCacheDir().getAbsolutePath());
        sb.append("/logs");
        mLogsDirectoryFile = new File(sb.toString());
        Log.setLogDirectory(mLogsDirectoryFile);
        Log.init("RiotLog");
        String str = "----------------------------------------------------------------";
        Log.d(LOG_TAG, str);
        Log.d(LOG_TAG, str);
        String str2 = LOG_TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" Application version: ");
        sb2.append(VECTOR_VERSION_STRING);
        Log.d(str2, sb2.toString());
        String str3 = LOG_TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(" SDK version: ");
        sb3.append(SDK_VERSION_STRING);
        Log.d(str3, sb3.toString());
        String str4 = LOG_TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(" Local time: ");
        sb4.append(new SimpleDateFormat("MM-dd HH:mm:ss.SSSZ", Locale.US).format(new Date()));
        Log.d(str4, sb4.toString());
        Log.d(LOG_TAG, str);
        Log.d(LOG_TAG, "----------------------------------------------------------------\n\n\n\n");
        this.mRageShake = new RageShake(this);
        MXCryptoConfig mXCryptoConfig = new MXCryptoConfig();
        mXCryptoConfig.mEnableEncryptionForInvitedMembers = true;
        MXSession.setCryptoConfig(mXCryptoConfig);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            final Map<String, String> mLocalesByActivity = new HashMap();

            public void onActivityCreated(Activity activity, Bundle bundle) {
                String access$000 = VectorApp.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onActivityCreated ");
                sb.append(activity);
                Log.d(access$000, sb.toString());
                VectorApp.this.mCreatedActivities.add(activity.toString());
                VectorApp.this.onNewScreen(activity);
            }

            public void onActivityStarted(Activity activity) {
                String access$000 = VectorApp.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onActivityStarted ");
                sb.append(activity);
                Log.d(access$000, sb.toString());
            }

            private String getActivityLocaleStatus(Activity activity) {
                StringBuilder sb = new StringBuilder();
                sb.append(VectorLocale.INSTANCE.getApplicationLocale().toString());
                String str = "_";
                sb.append(str);
                sb.append(FontScale.INSTANCE.getFontScalePrefValue());
                sb.append(str);
                sb.append(ThemeUtils.INSTANCE.getApplicationTheme(activity));
                return sb.toString();
            }

            public void onActivityResumed(Activity activity) {
                String access$000 = VectorApp.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onActivityResumed ");
                sb.append(activity);
                Log.d(access$000, sb.toString());
                VectorApp.this.setCurrentActivity(activity);
                String obj = activity.toString();
                if (this.mLocalesByActivity.containsKey(obj)) {
                    String str = (String) this.mLocalesByActivity.get(obj);
                    if (!TextUtils.equals(str, getActivityLocaleStatus(activity))) {
                        String access$0002 = VectorApp.LOG_TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("## onActivityResumed() : restart the activity ");
                        sb2.append(activity);
                        sb2.append(" because of the locale update from ");
                        sb2.append(str);
                        sb2.append(" to ");
                        sb2.append(getActivityLocaleStatus(activity));
                        Log.d(access$0002, sb2.toString());
                        VectorApp.this.restartActivity(activity);
                        return;
                    }
                }
                if (!TextUtils.equals(Locale.getDefault().toString(), VectorLocale.INSTANCE.getApplicationLocale().toString())) {
                    String access$0003 = VectorApp.LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("## onActivityResumed() : the locale has been updated to ");
                    sb3.append(Locale.getDefault().toString());
                    sb3.append(", restore the expected value ");
                    sb3.append(VectorLocale.INSTANCE.getApplicationLocale().toString());
                    Log.d(access$0003, sb3.toString());
                    VectorApp.updateApplicationSettings(VectorLocale.INSTANCE.getApplicationLocale(), FontScale.INSTANCE.getFontScalePrefValue(), ThemeUtils.INSTANCE.getApplicationTheme(activity));
                    VectorApp.this.restartActivity(activity);
                }
                PermissionsToolsKt.logPermissionStatuses(VectorApp.this);
            }

            public void onActivityPaused(Activity activity) {
                String access$000 = VectorApp.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onActivityPaused ");
                sb.append(activity);
                Log.d(access$000, sb.toString());
                this.mLocalesByActivity.put(activity.toString(), getActivityLocaleStatus(activity));
                VectorApp.this.setCurrentActivity(null);
                VectorApp.this.onAppPause();
            }

            public void onActivityStopped(Activity activity) {
                String access$000 = VectorApp.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onActivityStopped ");
                sb.append(activity);
                Log.d(access$000, sb.toString());
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                String access$000 = VectorApp.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onActivitySaveInstanceState ");
                sb.append(activity);
                Log.d(access$000, sb.toString());
            }

            public void onActivityDestroyed(Activity activity) {
                String access$000 = VectorApp.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onActivityDestroyed ");
                sb.append(activity);
                Log.d(access$000, sb.toString());
                Matrix.getInstance(VectorApp.this).dismissExpiredAccountDialogIfAnyOnActivityDestroyed(activity);
                VectorApp.this.mCreatedActivities.remove(activity.toString());
                this.mLocalesByActivity.remove(activity.toString());
                if (VectorApp.this.mCreatedActivities.size() > 1) {
                    String access$0002 = VectorApp.LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onActivityDestroyed : \n");
                    sb2.append(VectorApp.this.mCreatedActivities);
                    Log.d(access$0002, sb2.toString());
                }
            }
        });
        try {
            this.mMarkdownParser = new VectorMarkdownParser(this);
        } catch (Exception e) {
            String str5 = LOG_TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("cannot create the mMarkdownParser ");
            sb5.append(e.getMessage());
            Log.e(str5, sb5.toString(), e);
        }
        registerReceiver(this.mLanguageReceiver, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
        registerReceiver(this.mLanguageReceiver, new IntentFilter("android.intent.action.CONFIGURATION_CHANGED"));
        PreferencesManager.fixMigrationIssues(this);
        initApplicationLocale();
        visitSessionVariables();
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (!TextUtils.equals(Locale.getDefault().toString(), VectorLocale.INSTANCE.getApplicationLocale().toString())) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onConfigurationChanged() : the locale has been updated to ");
            sb.append(Locale.getDefault().toString());
            sb.append(", restore the expected value ");
            sb.append(VectorLocale.INSTANCE.getApplicationLocale().toString());
            Log.d(str, sb.toString());
            updateApplicationSettings(VectorLocale.INSTANCE.getApplicationLocale(), FontScale.INSTANCE.getFontScalePrefValue(), ThemeUtils.INSTANCE.getApplicationTheme(this));
        }
    }

    public static void markdownToHtml(final String str, final IVectorMarkdownParserListener iVectorMarkdownParserListener) {
        if (getInstance().mMarkdownParser != null) {
            getInstance().mMarkdownParser.markdownToHtml(str, iVectorMarkdownParserListener);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    iVectorMarkdownParserListener.onMarkdownParsed(str, null);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void suspendApp() {
        PushManager pushManager = Matrix.getInstance(this).getPushManager();
        if (!pushManager.isBackgroundSyncAllowed() || (pushManager.useFcm() && pushManager.hasRegistrationToken())) {
            Log.d(LOG_TAG, "suspendApp ; pause the event stream");
            CommonActivityUtils.pauseEventStream(this);
        } else {
            Log.d(LOG_TAG, "suspendApp ; the event stream is not paused because FCM is disabled.");
        }
        for (MXSession mXSession : Matrix.getInstance(this).getSessions()) {
            if (mXSession.isAlive()) {
                int i = 0;
                mXSession.setIsOnline(false);
                if (pushManager.isBackgroundSyncAllowed()) {
                    i = pushManager.getBackgroundSyncDelay();
                }
                mXSession.setSyncDelay(i);
                mXSession.setSyncTimeout(pushManager.getBackgroundSyncTimeOut());
                if (System.currentTimeMillis() - this.mLastMediasCheck < AdapterUtils.MS_IN_DAY) {
                    this.mLastMediasCheck = System.currentTimeMillis();
                    mXSession.removeMediaBefore(this, PreferencesManager.getMinMediasLastAccessTime(getApplicationContext()));
                }
                if (mXSession.getDataHandler().areLeftRoomsSynced()) {
                    mXSession.getDataHandler().releaseLeftRooms();
                }
            }
        }
        clearSyncingSessions();
        PIDsRetriever.getInstance().onAppBackgrounded();
        MyPresenceManager.advertiseAllUnavailable();
        this.mRageShake.stop();
        onAppPause();
    }

    private void startActivityTransitionTimer() {
        Log.d(LOG_TAG, "## startActivityTransitionTimer()");
        try {
            this.mActivityTransitionTimer = new Timer();
            this.mActivityTransitionTimerTask = new TimerTask() {
                public void run() {
                    try {
                        if (VectorApp.this.mActivityTransitionTimerTask != null) {
                            VectorApp.this.mActivityTransitionTimerTask.cancel();
                            VectorApp.this.mActivityTransitionTimerTask = null;
                        }
                        if (VectorApp.this.mActivityTransitionTimer != null) {
                            VectorApp.this.mActivityTransitionTimer.cancel();
                            VectorApp.this.mActivityTransitionTimer = null;
                        }
                    } catch (Exception e) {
                        String access$000 = VectorApp.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## startActivityTransitionTimer() failed ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                    if (VectorApp.mCurrentActivity != null) {
                        Log.e(VectorApp.LOG_TAG, "## startActivityTransitionTimer() : the timer expires but there is an active activity.");
                        return;
                    }
                    boolean z = true;
                    VectorApp.this.mIsInBackground = true;
                    VectorApp vectorApp = VectorApp.this;
                    if (vectorApp.mCallsManager.getActiveCall() == null) {
                        z = false;
                    }
                    vectorApp.mIsCallingInBackground = z;
                    if (!VectorApp.this.mIsCallingInBackground) {
                        Log.d(VectorApp.LOG_TAG, "Suspend the application because there was no resumed activity within 4 seconds");
                        CommonActivityUtils.displayMemoryInformation(null, " app suspended");
                        VectorApp.this.suspendApp();
                        return;
                    }
                    Log.d(VectorApp.LOG_TAG, "App not suspended due to call in progress");
                }
            };
            this.mActivityTransitionTimer.schedule(this.mActivityTransitionTimerTask, MAX_ACTIVITY_TRANSITION_TIME_MS);
        } catch (Throwable th) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## startActivityTransitionTimer() : failed to start the timer ");
            sb.append(th.getMessage());
            Log.e(str, sb.toString());
            Timer timer = this.mActivityTransitionTimer;
            if (timer != null) {
                timer.cancel();
                this.mActivityTransitionTimer = null;
            }
        }
    }

    private void stopActivityTransitionTimer() {
        Log.d(LOG_TAG, "## stopActivityTransitionTimer()");
        TimerTask timerTask = this.mActivityTransitionTimerTask;
        if (timerTask != null) {
            timerTask.cancel();
            this.mActivityTransitionTimerTask = null;
        }
        Timer timer = this.mActivityTransitionTimer;
        if (timer != null) {
            timer.cancel();
            this.mActivityTransitionTimer = null;
        }
        if (isAppInBackground() && !this.mIsCallingInBackground) {
            if (EventStreamService.isStopped()) {
                CommonActivityUtils.startEventStreamService(this);
            } else {
                CommonActivityUtils.resumeEventStream(this);
                PushManager pushManager = Matrix.getInstance(this).getPushManager();
                if (pushManager != null) {
                    pushManager.checkRegistrations();
                }
            }
            ContactsManager.getInstance().clearSnapshot();
            ContactsManager.getInstance().refreshLocalContactsSnapshot();
            for (MXSession mXSession : Matrix.getInstance(this).getSessions()) {
                mXSession.getMyUser().refreshUserInfos(null);
                mXSession.setIsOnline(true);
                mXSession.setSyncDelay(0);
                mXSession.setSyncTimeout(0);
                addSyncingSession(mXSession);
            }
            this.mCallsManager.checkDeadCalls();
            Matrix.getInstance(this).getPushManager().onAppResume();
        }
        MyPresenceManager.advertiseAllOnline();
        this.mRageShake.start();
        this.mIsCallingInBackground = false;
        this.mIsInBackground = false;
    }

    /* access modifiers changed from: private */
    public void setCurrentActivity(Activity activity) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## setCurrentActivity() : from ");
        sb.append(mCurrentActivity);
        sb.append(" to ");
        sb.append(activity);
        Log.d(str, sb.toString());
        if (isAppInBackground() && activity != null) {
            Matrix instance2 = Matrix.getInstance(activity.getApplicationContext());
            if (instance2 != null) {
                instance2.refreshPushRules();
            }
            Log.d(LOG_TAG, "The application is resumed");
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" app resumed with ");
            sb2.append(activity);
            CommonActivityUtils.displayMemoryInformation(activity, sb2.toString());
        }
        if (getInstance() == null) {
            Log.e(LOG_TAG, "The application is resumed but there is no active instance");
        } else if (activity == null) {
            getInstance().startActivityTransitionTimer();
        } else {
            getInstance().stopActivityTransitionTimer();
        }
        mCurrentActivity = activity;
        if (mCurrentActivity != null) {
            KeyRequestHandler.getSharedInstance().processNextRequest();
        }
    }

    public Analytics getAnalytics() {
        return this.mAppAnalytics;
    }

    public DecryptionFailureTracker getDecryptionFailureTracker() {
        return this.mDecryptionFailureTracker;
    }

    public static Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public static boolean isAppInBackground() {
        return mCurrentActivity == null && getInstance() != null && getInstance().mIsInBackground;
    }

    /* access modifiers changed from: private */
    public void restartActivity(Activity activity) {
        if (!(activity instanceof VectorCallViewActivity) && !(activity instanceof JitsiCallActivity) && !(activity instanceof WidgetActivity)) {
            activity.startActivity(activity.getIntent());
            activity.finish();
        }
    }

    public EventEmitter<Activity> getOnActivityDestroyedListener() {
        return this.mOnActivityDestroyedListener;
    }

    public static Bitmap getSavedPickerImagePreview() {
        return mSavedPickerImagePreview;
    }

    public static void setSavedCameraImagePreview(Bitmap bitmap) {
        if (bitmap != mSavedPickerImagePreview) {
            mSavedPickerImagePreview = bitmap;
        }
    }

    public static void addSyncingSession(MXSession mXSession) {
        synchronized (mSyncingSessions) {
            mSyncingSessions.add(mXSession);
        }
    }

    public static void removeSyncingSession(MXSession mXSession) {
        if (mXSession != null) {
            synchronized (mSyncingSessions) {
                mSyncingSessions.remove(mXSession);
            }
        }
    }

    public static void clearSyncingSessions() {
        synchronized (mSyncingSessions) {
            mSyncingSessions.clear();
        }
    }

    public static boolean isSessionSyncing(MXSession mXSession) {
        boolean contains;
        if (mXSession == null) {
            return false;
        }
        synchronized (mSyncingSessions) {
            contains = mSyncingSessions.contains(mXSession);
        }
        return contains;
    }

    public boolean didAppCrash() {
        return PreferenceManager.getDefaultSharedPreferences(getInstance()).getBoolean(PREFS_CRASH_KEY, false);
    }

    public void clearAppCrashStatus() {
        PreferenceManager.getDefaultSharedPreferences(getInstance()).edit().remove(PREFS_CRASH_KEY).apply();
    }

    private void initApplicationLocale() {
        VectorLocale.INSTANCE.init(this);
        Locale applicationLocale = VectorLocale.INSTANCE.getApplicationLocale();
        float fontScale = FontScale.INSTANCE.getFontScale();
        String applicationTheme = ThemeUtils.INSTANCE.getApplicationTheme(this);
        Locale.setDefault(applicationLocale);
        Configuration configuration = new Configuration(getResources().getConfiguration());
        configuration.locale = applicationLocale;
        configuration.fontScale = fontScale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        ThemeUtils.INSTANCE.setApplicationTheme(this, applicationTheme);
    }

    public static void updateApplicationLocale(Locale locale) {
        updateApplicationSettings(locale, FontScale.INSTANCE.getFontScalePrefValue(), ThemeUtils.INSTANCE.getApplicationTheme(getInstance()));
    }

    public static void updateApplicationTheme(String str) {
        ThemeUtils.INSTANCE.setApplicationTheme(getInstance(), str);
        updateApplicationSettings(VectorLocale.INSTANCE.getApplicationLocale(), FontScale.INSTANCE.getFontScalePrefValue(), ThemeUtils.INSTANCE.getApplicationTheme(getInstance()));
    }

    /* access modifiers changed from: private */
    public static void updateApplicationSettings(Locale locale, String str, String str2) {
        VectorApp instance2 = getInstance();
        VectorLocale.INSTANCE.saveApplicationLocale(instance2, locale);
        FontScale.INSTANCE.saveFontScale(str);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration(instance2.getResources().getConfiguration());
        configuration.locale = locale;
        configuration.fontScale = FontScale.INSTANCE.getFontScale();
        instance2.getResources().updateConfiguration(configuration, instance2.getResources().getDisplayMetrics());
        ThemeUtils.INSTANCE.setApplicationTheme(instance2, str2);
        PhoneNumberUtils.onLocaleUpdate();
    }

    public static Context getLocalisedContext(Context context) {
        try {
            Resources resources = context.getResources();
            Locale applicationLocale = VectorLocale.INSTANCE.getApplicationLocale();
            Configuration configuration = resources.getConfiguration();
            configuration.fontScale = FontScale.INSTANCE.getFontScale();
            if (VERSION.SDK_INT >= 24) {
                configuration.setLocale(applicationLocale);
                configuration.setLayoutDirection(applicationLocale);
                return context.createConfigurationContext(configuration);
            }
            configuration.locale = applicationLocale;
            if (VERSION.SDK_INT >= 17) {
                configuration.setLayoutDirection(applicationLocale);
            }
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            return context;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getLocalisedContext() failed : ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            return context;
        }
    }

    private void visitSessionVariables() {
        this.mAppAnalytics.visitVariable(1, "App Platform", "Android Platform");
        this.mAppAnalytics.visitVariable(2, "App Version", BuildConfig.VERSION_NAME);
        this.mAppAnalytics.visitVariable(4, "Chosen Language", VectorLocale.INSTANCE.getApplicationLocale().toString());
        MXSession defaultSession = Matrix.getInstance(this).getDefaultSession();
        if (defaultSession != null) {
            this.mAppAnalytics.visitVariable(7, "Homeserver URL", defaultSession.getHomeServerConfig().getHomeserverUri().toString());
            this.mAppAnalytics.visitVariable(8, "Identity Server URL", defaultSession.getHomeServerConfig().getIdentityServerUri().toString());
        }
    }

    /* access modifiers changed from: private */
    public void onNewScreen(Activity activity) {
        StringBuilder sb = new StringBuilder();
        sb.append("/android/");
        sb.append(Matrix.getApplicationName());
        String str = "/";
        sb.append(str);
        sb.append(BuildConfig.FLAVOR_DESCRIPTION);
        sb.append(str);
        sb.append(BuildConfig.VERSION_NAME);
        sb.append(str);
        sb.append(activity.getClass().getName().replace(".", str));
        this.mAppAnalytics.trackScreen(sb.toString(), null);
    }

    /* access modifiers changed from: private */
    public void onAppPause() {
        this.mDecryptionFailureTracker.dispatch();
        this.mAppAnalytics.forceDispatch();
    }
}
