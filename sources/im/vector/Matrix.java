package im.vector;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.text.TextUtils;
import androidx.appcompat.app.AlertDialog;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.media.MediaScanManager;
import fr.gouv.tchap.sdk.rest.client.TchapValidityRestClient;
import fr.gouv.tchap.util.DinumUtilsKt;
import im.vector.UnrecognizedCertHandler.Callback;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.SplashActivity;
import im.vector.activity.VectorUniversalLinkActivity;
import im.vector.analytics.MetricsListenerProxy;
import im.vector.push.PushManager;
import im.vector.services.EventStreamService;
import im.vector.store.LoginStorage;
import im.vector.util.PreferencesManager;
import im.vector.widgets.WidgetsManager;
import io.realm.Realm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.cli.HelpFormatter;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.MXDataHandler.RequestNetworkErrorListener;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.MXSession.Builder;
import org.matrix.androidsdk.core.BingRulesManager;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.listeners.IMXNetworkEventListener;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.IncomingRoomKeyRequest;
import org.matrix.androidsdk.crypto.IncomingRoomKeyRequestCancellation;
import org.matrix.androidsdk.crypto.RoomKeysRequestListener;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.data.RoomSummary.RoomSummaryListener;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.data.store.MXFileStore;
import org.matrix.androidsdk.db.MXLatestChatMessageCache;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.EventContent;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.matrix.androidsdk.ssl.Fingerprint;
import org.matrix.androidsdk.ssl.UnrecognizedCertificateException;

public class Matrix {
    private static final boolean CONFIG_ENABLE_LOCAL_FILE_ENCRYPTION = false;
    public static final String EXPIRED_ACCOUNT = "ORG_MATRIX_EXPIRED_ACCOUNT";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = Matrix.class.getSimpleName();
    /* access modifiers changed from: private */
    public static Matrix instance = null;
    /* access modifiers changed from: private */
    public static final MXEventListener mLiveEventListener = new MXEventListener() {
        boolean mClearCacheRequired = false;
        private boolean mRefreshUnreadCounter = false;

        public void onIgnoredUsersListUpdate() {
            this.mClearCacheRequired = true;
        }

        public void onLiveEvent(Event event, RoomState roomState) {
            boolean z;
            boolean z2 = this.mRefreshUnreadCounter;
            if (!Event.EVENT_TYPE_MESSAGE.equals(event.getType())) {
                if (!Event.EVENT_TYPE_RECEIPT.equals(event.getType())) {
                    z = false;
                    this.mRefreshUnreadCounter = z2 | z;
                    WidgetsManager.getSharedInstance().onLiveEvent(Matrix.instance.getDefaultSession(), event);
                }
            }
            z = true;
            this.mRefreshUnreadCounter = z2 | z;
            WidgetsManager.getSharedInstance().onLiveEvent(Matrix.instance.getDefaultSession(), event);
        }

        public void onLiveEventsChunkProcessed(String str, String str2) {
            if (!(Matrix.instance == null || Matrix.instance.mMXSessions == null)) {
                if (this.mClearCacheRequired && !VectorApp.isAppInBackground()) {
                    this.mClearCacheRequired = false;
                    Matrix.instance.reloadSessions(VectorApp.getInstance());
                } else if (this.mRefreshUnreadCounter) {
                    PushManager pushManager = Matrix.instance.getPushManager();
                    if (pushManager != null && (!pushManager.useFcm() || !pushManager.hasRegistrationToken())) {
                        int i = 0;
                        for (MXSession mXSession : Matrix.instance.mMXSessions) {
                            if (mXSession.isAlive()) {
                                BingRulesManager bingRulesManager = mXSession.getDataHandler().getBingRulesManager();
                                for (Room room : mXSession.getDataHandler().getStore().getRooms()) {
                                    if (!room.isInvited()) {
                                        int notificationCount = room.getNotificationCount();
                                        if (bingRulesManager.isRoomMentionOnly(room.getRoomId())) {
                                            notificationCount = room.getHighlightCount();
                                        }
                                        if (notificationCount <= 0) {
                                        }
                                    }
                                    i++;
                                }
                            }
                        }
                        CommonActivityUtils.updateBadgeCount(Matrix.instance.mAppContext, i);
                    }
                }
                VectorApp.clearSyncingSessions();
            }
            this.mRefreshUnreadCounter = false;
            Log.d(Matrix.LOG_TAG, "onLiveEventsChunkProcessed ");
            EventStreamService.checkDisplayedNotifications();
        }
    };
    /* access modifiers changed from: private */
    public final Context mAppContext;
    /* access modifiers changed from: private */
    public AlertDialog mExpiredAccountDialog = null;
    public boolean mHasBeenDisconnected = false;
    /* access modifiers changed from: private */
    public final LoginStorage mLoginStorage;
    /* access modifiers changed from: private */
    public List<MXSession> mMXSessions;
    private final PushManager mPushManager;
    private List<IMXStore> mTmpStores;

    private Matrix(Context context) {
        instance = this;
        this.mAppContext = context.getApplicationContext();
        this.mLoginStorage = new LoginStorage(this.mAppContext);
        this.mMXSessions = new ArrayList();
        this.mTmpStores = new ArrayList();
        this.mPushManager = new PushManager(this.mAppContext);
    }

    public static synchronized Matrix getInstance(final Context context) {
        Matrix matrix;
        synchronized (Matrix.class) {
            if (instance == null && context != null) {
                instance = new Matrix(context);
                MatrixError.mConfigurationErrorCodes.add(EXPIRED_ACCOUNT);
                RoomSummary.setRoomSummaryListener(new RoomSummaryListener() {
                    public boolean isSupportedEvent(Event event) {
                        boolean isSupportedEventDefaultImplementation = RoomSummary.isSupportedEventDefaultImplementation(event);
                        if (!isSupportedEventDefaultImplementation || PreferencesManager.showJoinLeaveMessages(context)) {
                            return isSupportedEventDefaultImplementation;
                        }
                        if (!TextUtils.equals("m.room.member", event.getType())) {
                            return isSupportedEventDefaultImplementation;
                        }
                        String str = JsonUtils.toRoomMember(event.getContent()).membership;
                        if (!TextUtils.equals(str, "leave") && !TextUtils.equals(str, "join")) {
                            return isSupportedEventDefaultImplementation;
                        }
                        EventContent prevContent = event.getPrevContent();
                        if (prevContent != null) {
                            return TextUtils.equals(prevContent.membership, str);
                        }
                        return false;
                    }
                });
            }
            matrix = instance;
        }
        return matrix;
    }

    public LoginStorage getLoginStorage() {
        return this.mLoginStorage;
    }

    public static String getApplicationName() {
        return instance.mAppContext.getApplicationInfo().loadLabel(instance.mAppContext.getPackageManager()).toString();
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x00a9  */
    public String getVersion(boolean z, boolean z2) {
        String str;
        String string;
        String string2;
        String str2 = HelpFormatter.DEFAULT_OPT_PREFIX;
        String str3 = "";
        try {
            str = this.mAppContext.getPackageManager().getPackageInfo(this.mAppContext.getPackageName(), 0).versionName;
            str3 = BuildConfig.SHORT_FLAVOR_DESCRIPTION;
            try {
                if (!TextUtils.isEmpty(str3)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str3);
                    sb.append(str2);
                    str3 = sb.toString();
                }
            } catch (Exception e) {
                e = e;
                String str4 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## versionName() : failed ");
                sb2.append(e.getMessage());
                Log.e(str4, sb2.toString(), e);
                string = this.mAppContext.getString(R.string.git_revision);
                string2 = this.mAppContext.getString(R.string.build_number);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("b");
                sb3.append(string2);
                string = sb3.toString();
                z = false;
                String str5 = ")";
                String str6 = " (";
                if (z) {
                }
            }
        } catch (Exception e2) {
            e = e2;
            str = str3;
            String str42 = LOG_TAG;
            StringBuilder sb22 = new StringBuilder();
            sb22.append("## versionName() : failed ");
            sb22.append(e.getMessage());
            Log.e(str42, sb22.toString(), e);
            string = this.mAppContext.getString(R.string.git_revision);
            string2 = this.mAppContext.getString(R.string.build_number);
            StringBuilder sb32 = new StringBuilder();
            sb32.append("b");
            sb32.append(string2);
            string = sb32.toString();
            z = false;
            String str52 = ")";
            String str62 = " (";
            if (z) {
            }
        }
        string = this.mAppContext.getString(R.string.git_revision);
        string2 = this.mAppContext.getString(R.string.build_number);
        if (z2 && !TextUtils.equals(string2, "0")) {
            StringBuilder sb322 = new StringBuilder();
            sb322.append("b");
            sb322.append(string2);
            string = sb322.toString();
            z = false;
        }
        String str522 = ")";
        String str622 = " (";
        if (z) {
            String string3 = this.mAppContext.getString(R.string.git_revision_date);
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str);
            sb4.append(str622);
            sb4.append(str3);
            sb4.append(string);
            sb4.append(str2);
            sb4.append(string3);
            sb4.append(str522);
            return sb4.toString();
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append(str);
        sb5.append(str622);
        sb5.append(str3);
        sb5.append(string);
        sb5.append(str522);
        return sb5.toString();
    }

    public static List<MXSession> getMXSessions(Context context) {
        if (context != null) {
            Matrix matrix = instance;
            if (matrix != null) {
                return matrix.getSessions();
            }
        }
        return null;
    }

    public List<MXSession> getSessions() {
        ArrayList arrayList = new ArrayList();
        synchronized (LOG_TAG) {
            if (this.mMXSessions != null) {
                arrayList = new ArrayList(this.mMXSessions);
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:46:0x009d, code lost:
        return null;
     */
    public synchronized MXSession getDefaultSession() {
        List sessions = getSessions();
        if (sessions.size() > 0) {
            return (MXSession) sessions.get(0);
        }
        List<HomeServerConnectionConfig> credentialsList = this.mLoginStorage.getCredentialsList();
        if (credentialsList != null) {
            if (credentialsList.size() != 0) {
                boolean didAppCrash = VectorApp.getInstance().didAppCrash();
                HashSet hashSet = new HashSet();
                ArrayList arrayList = new ArrayList();
                for (HomeServerConnectionConfig homeServerConnectionConfig : credentialsList) {
                    if (homeServerConnectionConfig.getCredentials() != null && !TextUtils.isEmpty(homeServerConnectionConfig.getCredentials().userId) && !hashSet.contains(homeServerConnectionConfig.getCredentials().userId)) {
                        MXSession createSession = createSession(homeServerConnectionConfig);
                        if (didAppCrash) {
                            createSession.clear(VectorApp.getInstance());
                            createSession = createSession(homeServerConnectionConfig);
                        }
                        arrayList.add(createSession);
                        hashSet.add(homeServerConnectionConfig.getCredentials().userId);
                    }
                }
                synchronized (LOG_TAG) {
                    this.mMXSessions = arrayList;
                }
                if (arrayList.size() == 0) {
                    return null;
                }
                return (MXSession) arrayList.get(0);
            }
        }
    }

    public static MXSession getMXSession(Context context, String str) {
        return getInstance(context.getApplicationContext()).getSession(str);
    }

    public synchronized MXSession getSession(String str) {
        if (str != null) {
            synchronized (this) {
            }
            for (MXSession mXSession : getSessions()) {
                Credentials credentials = mXSession.getCredentials();
                if (credentials != null && credentials.userId.equals(str)) {
                    return mXSession;
                }
            }
        }
        return getDefaultSession();
    }

    public static void setSessionErrorListener(Activity activity) {
        if (instance != null && activity != null) {
            for (MXSession mXSession : getMXSessions(activity)) {
                if (mXSession.isAlive()) {
                    mXSession.setFailureCallback(new ErrorListener(mXSession, activity));
                }
            }
        }
    }

    public static void removeSessionErrorListener(Activity activity) {
        if (instance != null && activity != null) {
            for (MXSession mXSession : getMXSessions(activity)) {
                if (mXSession.isAlive()) {
                    mXSession.setFailureCallback(null);
                }
            }
        }
    }

    public MXMediaCache getMediaCache() {
        if (getSessions().size() > 0) {
            return ((MXSession) getSessions().get(0)).getMediaCache();
        }
        return null;
    }

    public MXLatestChatMessageCache getDefaultLatestChatMessageCache() {
        if (getSessions().size() > 0) {
            return ((MXSession) getSessions().get(0)).getLatestChatMessageCache();
        }
        return null;
    }

    public static boolean hasValidSessions() {
        boolean z;
        if (instance == null) {
            Log.e(LOG_TAG, "hasValidSessions : has no instance");
            return false;
        }
        synchronized (LOG_TAG) {
            z = instance.mMXSessions != null && instance.mMXSessions.size() > 0;
            if (!z) {
                Log.e(LOG_TAG, "hasValidSessions : has no session");
            } else {
                for (MXSession mXSession : instance.mMXSessions) {
                    z &= mXSession.isAlive() && mXSession.getDataHandler() != null;
                }
                if (!z) {
                    Log.e(LOG_TAG, "hasValidSessions : one sesssion has no valid data handler");
                }
            }
        }
        return z;
    }

    public void deactivateSession(Context context, final MXSession mXSession, String str, boolean z, final ApiCallback<Void> apiCallback) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## deactivateSession() ");
        sb.append(mXSession.getMyUserId());
        Log.d(str2, sb.toString());
        mXSession.deactivateAccount(context, str, z, new SimpleApiCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                Matrix.this.mLoginStorage.removeCredentials(mXSession.getHomeServerConfig());
                mXSession.getDataHandler().removeListener(Matrix.mLiveEventListener);
                VectorApp.removeSyncingSession(mXSession);
                synchronized (Matrix.LOG_TAG) {
                    Matrix.this.mMXSessions.remove(mXSession);
                }
                apiCallback.onSuccess(voidR);
            }
        });
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0080, code lost:
        return;
     */
    public synchronized void clearSession(Context context, final MXSession mXSession, boolean z, final ApiCallback<Void> apiCallback) {
        if (!mXSession.isAlive()) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## clearSession() ");
            sb.append(mXSession.getMyUserId());
            sb.append(" is already released");
            Log.e(str, sb.toString());
            return;
        }
        String str2 = LOG_TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("## clearSession() ");
        sb2.append(mXSession.getMyUserId());
        sb2.append(" clearCredentials ");
        sb2.append(z);
        Log.d(str2, sb2.toString());
        if (z) {
            this.mLoginStorage.removeCredentials(mXSession.getHomeServerConfig());
        }
        mXSession.getDataHandler().removeListener(mLiveEventListener);
        Realm defaultInstance = Realm.getDefaultInstance();
        new MediaScanManager(mXSession.getMediaScanRestClient(), defaultInstance).clearAntiVirusScanResults();
        defaultInstance.close();
        AnonymousClass4 r0 = new SimpleApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                VectorApp.removeSyncingSession(mXSession);
                synchronized (Matrix.LOG_TAG) {
                    Matrix.this.mMXSessions.remove(mXSession);
                }
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onSuccess(null);
                }
            }
        };
        if (z) {
            mXSession.logout(context, r0);
        } else {
            mXSession.clear(context, r0);
        }
    }

    public synchronized void clearSessions(Context context, boolean z, ApiCallback<Void> apiCallback) {
        ArrayList arrayList;
        synchronized (LOG_TAG) {
            arrayList = new ArrayList(this.mMXSessions);
        }
        clearSessions(context, arrayList.iterator(), z, apiCallback);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x000e, code lost:
        return;
     */
    public synchronized void clearSessions(Context context, Iterator<MXSession> it, boolean z, ApiCallback<Void> apiCallback) {
        if (it.hasNext()) {
            MXSession mXSession = (MXSession) it.next();
            final Context context2 = context;
            final Iterator<MXSession> it2 = it;
            final boolean z2 = z;
            final ApiCallback<Void> apiCallback2 = apiCallback;
            AnonymousClass5 r1 = new SimpleApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    Matrix.this.clearSessions(context2, it2, z2, apiCallback2);
                }
            };
            clearSession(context, mXSession, z, r1);
        } else if (apiCallback != null) {
            apiCallback.onSuccess(null);
        }
    }

    public synchronized void addSession(MXSession mXSession) {
        this.mLoginStorage.addCredentials(mXSession.getHomeServerConfig());
        synchronized (LOG_TAG) {
            this.mMXSessions.add(mXSession);
        }
    }

    public MXSession createSession(HomeServerConnectionConfig homeServerConnectionConfig) {
        return createSession(this.mAppContext, homeServerConnectionConfig);
    }

    private MXSession createSession(Context context, HomeServerConnectionConfig homeServerConnectionConfig) {
        MetricsListenerProxy metricsListenerProxy = new MetricsListenerProxy(VectorApp.getInstance().getAnalytics());
        Credentials credentials = homeServerConnectionConfig.getCredentials();
        MXFileStore mXFileStore = new MXFileStore(homeServerConnectionConfig, false, context);
        mXFileStore.setMetricsListener(metricsListenerProxy);
        MXDataHandler mXDataHandler = new MXDataHandler(mXFileStore, credentials);
        mXFileStore.setDataHandler(mXDataHandler);
        mXDataHandler.setLazyLoadingEnabled(PreferencesManager.useLazyLoading(context));
        final MXSession build = new Builder(homeServerConnectionConfig, mXDataHandler, context).withPushServerUrl(context.getString(R.string.push_server_url)).withMetricsListener(metricsListenerProxy).withFileEncryption(false).build();
        build.getContentManager().configureAntiVirusScanner(true);
        mXDataHandler.setMetricsListener(metricsListenerProxy);
        mXDataHandler.setRequestNetworkErrorListener(new RequestNetworkErrorListener() {
            public void onConfigurationError(String str) {
                String access$300 = Matrix.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## createSession() : onConfigurationError ");
                sb.append(str);
                Log.e(access$300, sb.toString());
                if (TextUtils.equals(str, MatrixError.UNKNOWN_TOKEN)) {
                    if (VectorApp.getCurrentActivity() != null) {
                        Log.e(Matrix.LOG_TAG, "## createSession() : onTokenCorrupted");
                        CommonActivityUtils.logout(VectorApp.getCurrentActivity());
                    }
                } else if (TextUtils.equals(str, Matrix.EXPIRED_ACCOUNT)) {
                    Matrix.instance.suspendTchapOnExpiredAccount(VectorApp.getInstance());
                }
            }

            public void onSSLCertificateError(UnrecognizedCertificateException unrecognizedCertificateException) {
                if (VectorApp.getCurrentActivity() != null) {
                    Fingerprint fingerprint = unrecognizedCertificateException.getFingerprint();
                    String access$300 = Matrix.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## createSession() : Found fingerprint: SHA-256: ");
                    sb.append(fingerprint.getBytesAsHexString());
                    Log.d(access$300, sb.toString());
                    UnrecognizedCertHandler.show(build.getHomeServerConfig(), fingerprint, true, new Callback() {
                        public void onIgnore() {
                        }

                        public void onAccept() {
                            Matrix.getInstance(VectorApp.getInstance().getApplicationContext()).getLoginStorage().replaceCredentials(build.getHomeServerConfig());
                        }

                        public void onReject() {
                            Log.d(Matrix.LOG_TAG, "Found fingerprint: reject fingerprint");
                            CommonActivityUtils.logout((Context) VectorApp.getCurrentActivity(), Arrays.asList(new MXSession[]{build}), true, null);
                        }
                    });
                }
            }
        });
        if (!TextUtils.isEmpty(credentials.deviceId)) {
            build.enableCryptoWhenStarting();
        }
        mXDataHandler.addListener(mLiveEventListener);
        mXDataHandler.addListener(VectorApp.getInstance().getDecryptionFailureTracker());
        build.setUseDataSaveMode(PreferencesManager.useDataSaveMode(context));
        mXDataHandler.addListener(new MXEventListener() {
            public void onInitialSyncComplete(String str) {
                if (build.getCrypto() != null) {
                    build.getCrypto().addRoomKeysRequestListener(new RoomKeysRequestListener() {
                        public void onRoomKeyRequest(IncomingRoomKeyRequest incomingRoomKeyRequest) {
                            KeyRequestHandler.getSharedInstance().handleKeyRequest(incomingRoomKeyRequest);
                        }

                        public void onRoomKeyRequestCancellation(IncomingRoomKeyRequestCancellation incomingRoomKeyRequestCancellation) {
                            KeyRequestHandler.getSharedInstance().handleKeyRequestCancellation(incomingRoomKeyRequestCancellation);
                        }
                    });
                }
            }

            public void onStoreReady() {
                DinumUtilsKt.clearSessionExpiredContents(build);
            }
        });
        return build;
    }

    public void reloadSessions(final Context context) {
        Log.e(LOG_TAG, "## reloadSessions");
        CommonActivityUtils.logout(context, getMXSessions(context), false, (ApiCallback<Void>) new SimpleApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                synchronized (Matrix.LOG_TAG) {
                    for (HomeServerConnectionConfig createSession : Matrix.this.mLoginStorage.getCredentialsList()) {
                        Matrix.this.mMXSessions.add(Matrix.this.createSession(createSession));
                    }
                }
                Matrix.getInstance(context).getPushManager().clearFcmData(new SimpleApiCallback<Void>() {
                    public void onSuccess(Void voidR) {
                        Matrix.this.launchSplashScreen(context);
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public void launchSplashScreen(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), SplashActivity.class);
        intent.setFlags(268468224);
        context.getApplicationContext().startActivity(intent);
        if (VectorApp.getCurrentActivity() != null) {
            VectorApp.getCurrentActivity().finish();
        }
    }

    /* access modifiers changed from: private */
    public void suspendTchapOnExpiredAccount(final Context context) {
        Log.i(LOG_TAG, "## suspendTchapOnExpiredAccount");
        MXSession defaultSession = getDefaultSession();
        Activity currentActivity = VectorApp.getCurrentActivity();
        if (defaultSession == null || !defaultSession.isAlive() || currentActivity == null || (currentActivity instanceof VectorUniversalLinkActivity)) {
            Log.w(LOG_TAG, "## suspendTchapOnExpiredAccount: ignored (app busy)");
            return;
        }
        AlertDialog alertDialog = this.mExpiredAccountDialog;
        if (!(alertDialog == null || alertDialog.getOwnerActivity() == currentActivity)) {
            Log.i(LOG_TAG, "## suspendTchapOnExpiredAccount: dismiss existing dialog");
            this.mExpiredAccountDialog.dismiss();
            this.mExpiredAccountDialog = null;
        }
        if (this.mExpiredAccountDialog == null) {
            CommonActivityUtils.logout(context, getMXSessions(context), false, (ApiCallback<Void>) new SimpleApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    synchronized (Matrix.LOG_TAG) {
                        for (HomeServerConnectionConfig createSession : Matrix.this.mLoginStorage.getCredentialsList()) {
                            Matrix.this.mMXSessions.add(Matrix.this.createSession(createSession));
                        }
                    }
                    Matrix.getInstance(context).getPushManager().clearFcmData(new SimpleApiCallback<Void>() {
                        public void onSuccess(Void voidR) {
                            Matrix.this.displayExpiredAccountDialog(context);
                        }
                    });
                }
            });
        } else {
            Log.w(LOG_TAG, "## suspendTchapOnExpiredAccount: ignored (already handled)");
        }
    }

    /* access modifiers changed from: private */
    public void displayExpiredAccountDialog(final Context context) {
        Activity currentActivity = VectorApp.getCurrentActivity();
        this.mExpiredAccountDialog = new AlertDialog.Builder(currentActivity).setMessage((int) R.string.tchap_expired_account_msg).setCancelable(false).setPositiveButton((int) R.string.tchap_expired_account_resume_button, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Matrix.this.launchSplashScreen(context);
            }
        }).setNeutralButton((int) R.string.tchap_request_renewal_email_button, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                new TchapValidityRestClient(Matrix.this.getDefaultSession().getHomeServerConfig()).requestRenewalEmail(new ApiCallback<Void>() {
                    public void onSuccess(Void voidR) {
                        Log.i(Matrix.LOG_TAG, "a renewal email has been requested");
                        Matrix.this.onRequestedRenewalEmail(context);
                    }

                    private void onError(String str) {
                        String access$300 = Matrix.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("request for a renewal email failed ");
                        sb.append(str);
                        Log.e(access$300, sb.toString());
                        Matrix.this.displayExpiredAccountDialog(context);
                    }

                    public void onNetworkError(Exception exc) {
                        onError(exc.getLocalizedMessage());
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        onError(matrixError.getLocalizedMessage());
                    }

                    public void onUnexpectedError(Exception exc) {
                        onError(exc.getLocalizedMessage());
                    }
                });
            }
        }).setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                Matrix.this.mExpiredAccountDialog = null;
            }
        }).show();
        this.mExpiredAccountDialog.setOwnerActivity(currentActivity);
    }

    /* access modifiers changed from: private */
    public void onRequestedRenewalEmail(final Context context) {
        Activity currentActivity = VectorApp.getCurrentActivity();
        this.mExpiredAccountDialog = new AlertDialog.Builder(currentActivity).setMessage((int) R.string.tchap_expired_account_on_new_sent_email_msg).setPositiveButton((int) R.string.tchap_expired_account_resume_button, (OnClickListener) null).setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                Matrix.this.mExpiredAccountDialog = null;
                Matrix.this.launchSplashScreen(context);
            }
        }).show();
        this.mExpiredAccountDialog.setOwnerActivity(currentActivity);
    }

    public void dismissExpiredAccountDialogIfAnyOnActivityDestroyed(Activity activity) {
        AlertDialog alertDialog = this.mExpiredAccountDialog;
        if (alertDialog != null && alertDialog.getOwnerActivity() == activity) {
            this.mExpiredAccountDialog.dismiss();
        }
    }

    public void onRenewAccountValidity() {
        AlertDialog alertDialog = this.mExpiredAccountDialog;
        if (alertDialog != null) {
            alertDialog.getButton(-1).performClick();
        }
    }

    public PushManager getPushManager() {
        return this.mPushManager;
    }

    public void refreshPushRules() {
        List<MXSession> sessions;
        synchronized (this) {
            sessions = getSessions();
        }
        for (MXSession mXSession : sessions) {
            if (mXSession.getDataHandler() != null) {
                mXSession.getDataHandler().refreshPushRules();
            }
        }
    }

    public void addNetworkEventListener(IMXNetworkEventListener iMXNetworkEventListener) {
        if (getDefaultSession() != null && iMXNetworkEventListener != null) {
            getDefaultSession().getNetworkConnectivityReceiver().addEventListener(iMXNetworkEventListener);
        }
    }

    public void removeNetworkEventListener(IMXNetworkEventListener iMXNetworkEventListener) {
        if (getDefaultSession() != null && iMXNetworkEventListener != null) {
            getDefaultSession().getNetworkConnectivityReceiver().removeEventListener(iMXNetworkEventListener);
        }
    }

    public boolean isConnected() {
        if (getDefaultSession() != null) {
            return getDefaultSession().getNetworkConnectivityReceiver().isConnected();
        }
        return true;
    }

    public int addTmpStore(IMXStore iMXStore) {
        if (iMXStore == null) {
            return -1;
        }
        int indexOf = this.mTmpStores.indexOf(iMXStore);
        if (indexOf < 0) {
            this.mTmpStores.add(iMXStore);
            indexOf = this.mTmpStores.indexOf(iMXStore);
        }
        return indexOf;
    }

    public IMXStore getTmpStore(int i) {
        if (i < 0 || i >= this.mTmpStores.size()) {
            return null;
        }
        return (IMXStore) this.mTmpStores.get(i);
    }

    public void clearTmpStoresList() {
        this.mTmpStores = new ArrayList();
    }
}
