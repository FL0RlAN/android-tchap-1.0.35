package im.vector.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import im.vector.Matrix;
import im.vector.activity.CommonActivityUtils;
import im.vector.push.fcm.FcmHelper;
import im.vector.util.PreferencesManager;
import im.vector.util.SystemUtilsKt;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.call.MXCall;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.listeners.IMXNetworkEventListener;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Pusher;
import org.matrix.androidsdk.rest.model.PushersResponse;

public final class PushManager {
    private static final String DEFAULT_PUSHER_APP_ID = "fr.gouv.tchap.a.android";
    private static final String DEFAULT_PUSHER_FILE_TAG = "mobile";
    private static final String DEFAULT_PUSHER_URL = "https://matrix.org/_matrix/push/v1/notify";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = PushManager.class.getSimpleName();
    private static final String PREFS_ALLOW_BACKGROUND_SYNC = "GcmRegistrationManager.PREFS_ALLOW_BACKGROUND_SYNC";
    private static final String PREFS_ALLOW_NOTIFICATIONS = "GcmRegistrationManager.PREFS_ALLOW_NOTIFICATIONS";
    private static final String PREFS_ALLOW_SENDING_CONTENT_TO_GCM = "GcmRegistrationManager.PREFS_ALLOW_SENDING_CONTENT_TO_GCM";
    private static final String PREFS_PUSH = "GcmRegistrationManager";
    private static final String PREFS_PUSHER_REGISTRATION_STATUS = "PREFS_PUSHER_REGISTRATION_STATUS";
    private static final String PREFS_PUSHER_REGISTRATION_TOKEN_KEY = "PREFS_PUSHER_REGISTRATION_TOKEN_KEY";
    private static final String PREFS_PUSHER_REGISTRATION_TOKEN_KEY_FCM = "PREFS_PUSHER_REGISTRATION_TOKEN_KEY_FCM";
    private static final String PREFS_SYNC_DELAY = "GcmRegistrationManager.PREFS_SYNC_DELAY";
    private static final String PREFS_SYNC_TIMEOUT = "GcmRegistrationManager.PREFS_SYNC_TIMEOUT";
    private static final String PREFS_TURN_SCREEN_ON = "GcmRegistrationManager.PREFS_TURN_SCREEN_ON";
    private final String mBasePusherDeviceName;
    /* access modifiers changed from: private */
    public final Context mContext;
    private String mPusherAppName = null;
    private String mPusherLang = null;
    public List<Pusher> mPushersList = new ArrayList();
    private final List<ApiCallback<Void>> mRegistrationCallbacks = new ArrayList();
    /* access modifiers changed from: private */
    public RegistrationState mRegistrationState;
    /* access modifiers changed from: private */
    public String mRegistrationToken;
    private final List<ApiCallback<Void>> mUnRegistrationCallbacks = new ArrayList();

    /* renamed from: im.vector.push.PushManager$12 reason: invalid class name */
    static /* synthetic */ class AnonymousClass12 {
        static final /* synthetic */ int[] $SwitchMap$im$vector$push$PushManager$NotificationPrivacy = new int[NotificationPrivacy.values().length];
        static final /* synthetic */ int[] $SwitchMap$im$vector$push$PushManager$RegistrationState = new int[RegistrationState.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|19|20|(3:21|22|24)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(19:0|1|2|3|(2:5|6)|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|24) */
        /* JADX WARNING: Can't wrap try/catch for region: R(20:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|16|17|18|19|20|21|22|24) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x003d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0047 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0051 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x005c */
        static {
            try {
                $SwitchMap$im$vector$push$PushManager$NotificationPrivacy[NotificationPrivacy.REDUCED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$im$vector$push$PushManager$NotificationPrivacy[NotificationPrivacy.LOW_DETAIL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$im$vector$push$PushManager$NotificationPrivacy[NotificationPrivacy.NORMAL.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $SwitchMap$im$vector$push$PushManager$RegistrationState[RegistrationState.UNREGISTRATED.ordinal()] = 1;
            $SwitchMap$im$vector$push$PushManager$RegistrationState[RegistrationState.SERVER_REGISTRATING.ordinal()] = 2;
            $SwitchMap$im$vector$push$PushManager$RegistrationState[RegistrationState.FCM_REGISTERED.ordinal()] = 3;
            $SwitchMap$im$vector$push$PushManager$RegistrationState[RegistrationState.SERVER_REGISTERED.ordinal()] = 4;
            try {
                $SwitchMap$im$vector$push$PushManager$RegistrationState[RegistrationState.SERVER_UNREGISTRATING.ordinal()] = 5;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public enum NotificationPrivacy {
        REDUCED,
        LOW_DETAIL,
        NORMAL
    }

    private enum RegistrationState {
        UNREGISTRATED,
        FCM_REGISTRATING,
        FCM_REGISTERED,
        SERVER_REGISTRATING,
        SERVER_REGISTERED,
        SERVER_UNREGISTRATING
    }

    public boolean useFcm() {
        return true;
    }

    public PushManager(Context context) {
        this.mContext = context.getApplicationContext();
        this.mBasePusherDeviceName = Build.MODEL.trim();
        try {
            this.mPusherAppName = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0).packageName;
            this.mPusherLang = this.mContext.getResources().getConfiguration().locale.getLanguage();
        } catch (Exception unused) {
            this.mPusherAppName = "VectorApp";
            this.mPusherLang = "en";
        }
        Matrix.getInstance(context).addNetworkEventListener(new IMXNetworkEventListener() {
            public void onNetworkConnectionUpdate(boolean z) {
                if (z && PushManager.this.useFcm()) {
                    if (PushManager.this.areDeviceNotificationsAllowed() && PushManager.this.mRegistrationState == RegistrationState.FCM_REGISTERED) {
                        PushManager.this.register(null);
                    } else if (!PushManager.this.areDeviceNotificationsAllowed() && PushManager.this.mRegistrationState == RegistrationState.SERVER_REGISTERED) {
                        PushManager.this.unregister(null);
                    }
                }
            }
        });
        this.mRegistrationState = getStoredRegistrationState();
        this.mRegistrationToken = getStoredRegistrationToken();
    }

    public void checkRegistrations() {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("checkRegistrations with state ");
        sb.append(this.mRegistrationState);
        Log.i(str, sb.toString());
        if (!useFcm()) {
            Log.i(LOG_TAG, "checkRegistrations : FCM is disabled");
            return;
        }
        if (getOldStoredRegistrationToken() != null) {
            Log.i(LOG_TAG, "checkRegistrations : remove the GCM registration token after switching to the FCM one");
            this.mRegistrationToken = getOldStoredRegistrationToken();
            unregister(new ApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    Log.i(PushManager.LOG_TAG, "resetFCMRegistration : remove the GCM registration token done");
                    PushManager.this.clearOldStoredRegistrationToken();
                    PushManager.this.mRegistrationToken = null;
                    PushManager.this.setAndStoreRegistrationState(RegistrationState.UNREGISTRATED);
                    PushManager.this.checkRegistrations();
                }

                public void onMatrixError(MatrixError matrixError) {
                    onSuccess((Void) null);
                }

                public void onNetworkError(Exception exc) {
                    onSuccess((Void) null);
                }

                public void onUnexpectedError(Exception exc) {
                    onSuccess((Void) null);
                }
            });
        } else if (this.mRegistrationState == RegistrationState.UNREGISTRATED) {
            Log.i(LOG_TAG, "checkPusherRegistration : try to register to FCM server");
            if (registerToFcm()) {
                register(null);
                Log.i(LOG_TAG, "checkRegistrations : reregistered");
                CommonActivityUtils.onPushUpdate(this.mContext);
            } else {
                Log.i(LOG_TAG, "checkRegistrations : onPusherRegistrationFailed");
            }
        } else if (this.mRegistrationState == RegistrationState.FCM_REGISTERED) {
            if (useFcm() && areDeviceNotificationsAllowed()) {
                register(null);
            }
        } else if (this.mRegistrationState == RegistrationState.SERVER_REGISTERED) {
            refreshPushersList(new ArrayList(Matrix.getInstance(this.mContext).getSessions()), null);
        }
    }

    /* access modifiers changed from: private */
    public String getFcmRegistrationToken() {
        String storedRegistrationToken = getStoredRegistrationToken();
        if (!TextUtils.isEmpty(storedRegistrationToken)) {
            return storedRegistrationToken;
        }
        Log.i(LOG_TAG, "## getFcmRegistrationToken() : undefined token -> getting a new one");
        return FcmHelper.getFcmToken(this.mContext);
    }

    private boolean registerToFcm() {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("registerToFcm with state ");
        sb.append(this.mRegistrationState);
        Log.i(str, sb.toString());
        boolean z = false;
        if (!useFcm()) {
            Log.i(LOG_TAG, "registerPusher : FCM is disabled");
            return false;
        } else if (this.mRegistrationState != RegistrationState.UNREGISTRATED) {
            return true;
        } else {
            String fcmRegistrationToken = getFcmRegistrationToken();
            setAndStoreRegistrationState(fcmRegistrationToken != null ? RegistrationState.FCM_REGISTERED : RegistrationState.UNREGISTRATED);
            setAndStoreRegistrationToken(fcmRegistrationToken);
            if (this.mRegistrationToken != null) {
                z = true;
            }
            return z;
        }
    }

    public void resetFCMRegistration() {
        resetFCMRegistration(null);
    }

    public void resetFCMRegistration(final String str) {
        Log.i(LOG_TAG, "resetFCMRegistration");
        if (this.mRegistrationState == RegistrationState.SERVER_REGISTERED) {
            Log.i(LOG_TAG, "resetFCMRegistration : unregister server before retrieving the new FCM token");
            unregister(new ApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    Log.i(PushManager.LOG_TAG, "resetFCMRegistration : un-registration is done --> start the registration process");
                    PushManager.this.resetFCMRegistration(str);
                }

                public void onNetworkError(Exception exc) {
                    Log.i(PushManager.LOG_TAG, "resetFCMRegistration : un-registration failed.");
                }

                public void onMatrixError(MatrixError matrixError) {
                    Log.i(PushManager.LOG_TAG, "resetFCMRegistration : un-registration failed.");
                }

                public void onUnexpectedError(Exception exc) {
                    Log.i(PushManager.LOG_TAG, "resetFCMRegistration : un-registration failed.");
                }
            });
            return;
        }
        final boolean isEmpty = TextUtils.isEmpty(str);
        Log.i(LOG_TAG, "resetFCMRegistration : Clear the FCM data");
        clearFcmData(new SimpleApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                if (!isEmpty) {
                    Log.i(PushManager.LOG_TAG, "resetFCMRegistration : make a full registration process.");
                    PushManager.this.register(null);
                    return;
                }
                Log.i(PushManager.LOG_TAG, "resetFCMRegistration : Ready to register.");
            }
        });
    }

    private static String computePushTag(MXSession mXSession) {
        StringBuilder sb = new StringBuilder();
        sb.append("mobile_");
        sb.append(Math.abs(mXSession.getMyUserId().hashCode()));
        String sb2 = sb.toString();
        if (sb2.length() <= 32) {
            return sb2;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(Math.abs(sb2.hashCode()));
        sb3.append("");
        return sb3.toString();
    }

    /* access modifiers changed from: private */
    public void manage500Error(final ApiCallback<Void> apiCallback) {
        Log.i(LOG_TAG, "got a 500 error -> reset the registration and try again");
        new Timer().schedule(new TimerTask() {
            public void run() {
                if (RegistrationState.SERVER_REGISTERED == PushManager.this.mRegistrationState) {
                    Log.i(PushManager.LOG_TAG, "500 error : unregister first");
                    PushManager.this.unregister(new ApiCallback<Void>() {
                        public void onSuccess(Void voidR) {
                            Log.i(PushManager.LOG_TAG, "500 error: unregister success");
                            PushManager.this.setAndStoreRegistrationToken(null);
                            PushManager.this.setAndStoreRegistrationState(RegistrationState.UNREGISTRATED);
                            PushManager.this.register(apiCallback);
                        }

                        public void onNetworkError(Exception exc) {
                            onError();
                        }

                        public void onMatrixError(MatrixError matrixError) {
                            onError();
                        }

                        public void onUnexpectedError(Exception exc) {
                            onError();
                        }

                        private void onError() {
                            Log.i(PushManager.LOG_TAG, "500 error : unregister error");
                            PushManager.this.setAndStoreRegistrationToken(null);
                            PushManager.this.setAndStoreRegistrationState(RegistrationState.UNREGISTRATED);
                            PushManager.this.register(apiCallback);
                        }
                    });
                    return;
                }
                Log.i(PushManager.LOG_TAG, "500 error : no FCM token");
                PushManager.this.setAndStoreRegistrationToken(null);
                PushManager.this.setAndStoreRegistrationState(RegistrationState.UNREGISTRATED);
                PushManager.this.register(apiCallback);
            }
        }, 30000);
    }

    public void onAppResume() {
        if (this.mRegistrationState == RegistrationState.SERVER_REGISTERED) {
            Log.i(LOG_TAG, "## onAppResume() : force the push registration");
            forceSessionsRegistration(null);
        }
    }

    /* access modifiers changed from: private */
    public void registerToThirdPartyServer(final MXSession mXSession, final boolean z, final ApiCallback<Void> apiCallback) {
        if (!areDeviceNotificationsAllowed() || !useFcm() || !mXSession.isAlive()) {
            if (!areDeviceNotificationsAllowed()) {
                Log.i(LOG_TAG, "registerPusher : the user disabled it.");
            } else if (!mXSession.isAlive()) {
                Log.i(LOG_TAG, "registerPusher : the session is not anymore alive");
            } else {
                Log.i(LOG_TAG, "registerPusher : FCM is disabled.");
            }
            try {
                apiCallback.onUnexpectedError(new Exception("FCM not allowed"));
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("registerToThirdPartyServer failed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
            setAndStoreRegistrationState(RegistrationState.FCM_REGISTERED);
            return;
        }
        String str2 = LOG_TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("registerToThirdPartyServer of ");
        sb2.append(mXSession.getMyUserId());
        Log.d(str2, sb2.toString());
        mXSession.getPushersRestClient().addHttpPusher(this.mRegistrationToken, "fr.gouv.tchap.a.android", computePushTag(mXSession), this.mPusherLang, this.mPusherAppName, this.mBasePusherDeviceName, DEFAULT_PUSHER_URL, z, isBackgroundSyncAllowed() || !isContentSendingAllowed(), new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                Log.i(PushManager.LOG_TAG, "registerToThirdPartyServer succeeded");
                try {
                    apiCallback.onSuccess(null);
                } catch (Exception e) {
                    String access$100 = PushManager.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onSessionRegistered failed ");
                    sb.append(e.getMessage());
                    Log.e(access$100, sb.toString(), e);
                }
            }

            public void onNetworkError(Exception exc) {
                String access$100 = PushManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("registerToThirdPartyServer onNetworkError ");
                sb.append(exc.getMessage());
                Log.e(access$100, sb.toString(), exc);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    public void run() {
                        if (PushManager.this.mRegistrationState == RegistrationState.SERVER_REGISTRATING) {
                            Log.e(PushManager.LOG_TAG, "registerToThirdPartyServer onNetworkError -> retry");
                            PushManager.this.registerToThirdPartyServer(mXSession, z, apiCallback);
                        }
                    }
                }, 30000);
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$100 = PushManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("registerToThirdPartyServer onMatrixError ");
                sb.append(matrixError.errcode);
                Log.e(access$100, sb.toString());
                PushManager.this.setAndStoreRegistrationState(RegistrationState.FCM_REGISTERED);
                if (MatrixError.UNKNOWN.equals(matrixError.errcode)) {
                    PushManager.this.manage500Error(apiCallback);
                } else {
                    apiCallback.onMatrixError(matrixError);
                }
            }

            public void onUnexpectedError(Exception exc) {
                String access$100 = PushManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("registerToThirdPartyServer onUnexpectedError ");
                sb.append(exc.getMessage());
                Log.e(access$100, sb.toString(), exc);
                PushManager.this.setAndStoreRegistrationState(RegistrationState.FCM_REGISTERED);
                apiCallback.onUnexpectedError(exc);
            }
        });
    }

    public void refreshPushersList(List<MXSession> list, final ApiCallback<Void> apiCallback) {
        if (list != null && !list.isEmpty()) {
            ((MXSession) list.get(0)).getPushersRestClient().getPushers(new SimpleApiCallback<PushersResponse>(apiCallback) {
                public void onSuccess(PushersResponse pushersResponse) {
                    Pusher pusher;
                    if (pushersResponse.pushers == null) {
                        PushManager.this.mPushersList = new ArrayList();
                    } else {
                        PushManager.this.mPushersList = new ArrayList(pushersResponse.pushers);
                        Iterator it = PushManager.this.mPushersList.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                pusher = null;
                                break;
                            }
                            pusher = (Pusher) it.next();
                            if (TextUtils.equals(pusher.pushkey, PushManager.this.getFcmRegistrationToken())) {
                                break;
                            }
                        }
                        if (pusher != null) {
                            PushManager.this.mPushersList.remove(pusher);
                            PushManager.this.mPushersList.add(0, pusher);
                        }
                    }
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onSuccess(null);
                    }
                }
            });
        }
    }

    public void forceSessionsRegistration(ApiCallback<Void> apiCallback) {
        if (this.mRegistrationState == RegistrationState.SERVER_REGISTERED || this.mRegistrationState == RegistrationState.FCM_REGISTERED) {
            setAndStoreRegistrationState(RegistrationState.FCM_REGISTERED);
            register(apiCallback);
        } else if (apiCallback != null) {
            try {
                apiCallback.onUnexpectedError(new IllegalAccessException("Invalid state"));
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("forceSessionsRegistration failed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
    }

    public void register(ApiCallback<Void> apiCallback) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("register with state ");
        sb.append(this.mRegistrationState);
        Log.i(str, sb.toString());
        int i = AnonymousClass12.$SwitchMap$im$vector$push$PushManager$RegistrationState[this.mRegistrationState.ordinal()];
        if (i == 1) {
            Log.i(LOG_TAG, "register unregistrated : try to register again");
            if (registerToFcm()) {
                Log.i(LOG_TAG, "FCM registration success: register on server side");
                register(apiCallback);
                return;
            }
            Log.i(LOG_TAG, "FCM registration failed");
            if (apiCallback != null) {
                apiCallback.onUnexpectedError(new Exception("FCM registration failed"));
            }
        } else if (i != 2) {
            if (i != 3) {
                if (i == 4) {
                    Log.e(LOG_TAG, "register : already registered");
                    PreferencesManager.setAutoStartOnBoot(this.mContext, false);
                    if (apiCallback != null) {
                        apiCallback.onSuccess(null);
                    }
                } else if (i == 5) {
                    String str2 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("register : invalid state ");
                    sb2.append(this.mRegistrationState);
                    Log.e(str2, sb2.toString());
                    if (apiCallback != null) {
                        apiCallback.onUnexpectedError(new Exception("invalid state"));
                    }
                }
            } else if (useFcm() && areDeviceNotificationsAllowed() && !TextUtils.isEmpty(this.mRegistrationToken)) {
                setAndStoreRegistrationState(RegistrationState.SERVER_REGISTRATING);
                if (apiCallback != null) {
                    synchronized (this.mRegistrationCallbacks) {
                        this.mRegistrationCallbacks.add(apiCallback);
                    }
                }
                registerToThirdPartyServerRecursive(new ArrayList(Matrix.getInstance(this.mContext).getSessions()), 0);
            } else if (apiCallback != null) {
                apiCallback.onUnexpectedError(new Exception("FCM is not allowed"));
            }
        } else if (apiCallback != null) {
            this.mRegistrationCallbacks.add(apiCallback);
        }
    }

    /* access modifiers changed from: private */
    public void registerToThirdPartyServerRecursive(final ArrayList<MXSession> arrayList, final int i) {
        boolean z = false;
        if (i >= arrayList.size()) {
            Log.i(LOG_TAG, "registerSessions : all the sessions are registered");
            setAndStoreRegistrationState(RegistrationState.SERVER_REGISTERED);
            PreferencesManager.setAutoStartOnBoot(this.mContext, false);
            dispatchRegisterSuccess();
            refreshPushersList(arrayList, null);
            if (!useFcm() || areDeviceNotificationsAllowed()) {
                CommonActivityUtils.onPushUpdate(this.mContext);
            } else {
                unregister(null);
            }
            return;
        }
        final MXSession mXSession = (MXSession) arrayList.get(i);
        if (i > 0) {
            z = true;
        }
        registerToThirdPartyServer(mXSession, z, new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                String access$100 = PushManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("registerSessions : session ");
                sb.append(mXSession.getMyUserId());
                sb.append(" is registered");
                Log.d(access$100, sb.toString());
                PushManager.this.registerToThirdPartyServerRecursive(arrayList, i + 1);
            }

            public void onNetworkError(Exception exc) {
                PushManager.this.setAndStoreRegistrationState(RegistrationState.FCM_REGISTERED);
                PushManager.this.dispatchRegisterNetworkError(exc);
            }

            public void onMatrixError(MatrixError matrixError) {
                PushManager.this.setAndStoreRegistrationState(RegistrationState.FCM_REGISTERED);
                PushManager.this.dispatchRegisterMatrixError(matrixError);
            }

            public void onUnexpectedError(Exception exc) {
                PushManager.this.setAndStoreRegistrationState(RegistrationState.FCM_REGISTERED);
                PushManager.this.dispatchRegisterUnexpectedError(exc);
            }
        });
    }

    public void unregister(ApiCallback<Void> apiCallback) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("unregister with state ");
        sb.append(this.mRegistrationState);
        Log.i(str, sb.toString());
        if (this.mRegistrationState == RegistrationState.SERVER_UNREGISTRATING) {
            if (apiCallback != null) {
                synchronized (this.mUnRegistrationCallbacks) {
                    this.mUnRegistrationCallbacks.add(apiCallback);
                }
            }
        } else if (this.mRegistrationState != RegistrationState.SERVER_REGISTERED) {
            String str2 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("unregisterSessions : invalid state ");
            sb2.append(this.mRegistrationState);
            Log.e(str2, sb2.toString());
            if (apiCallback != null) {
                apiCallback.onUnexpectedError(new Exception("Invalid state"));
            }
        } else {
            setAndStoreRegistrationState(RegistrationState.SERVER_UNREGISTRATING);
            if (apiCallback != null) {
                synchronized (this.mUnRegistrationCallbacks) {
                    this.mUnRegistrationCallbacks.add(apiCallback);
                }
            }
            unregisterRecursive(new ArrayList(Matrix.getInstance(this.mContext).getSessions()), 0);
        }
    }

    /* access modifiers changed from: private */
    public void unregisterRecursive(final ArrayList<MXSession> arrayList, final int i) {
        if (i >= arrayList.size()) {
            setAndStoreRegistrationState(RegistrationState.FCM_REGISTERED);
            if (!useFcm() || !areDeviceNotificationsAllowed() || !Matrix.hasValidSessions()) {
                CommonActivityUtils.onPushUpdate(this.mContext);
            } else {
                register(null);
            }
            dispatchUnregisterSuccess();
            return;
        }
        unregister((MXSession) arrayList.get(i), new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                PushManager.this.unregisterRecursive(arrayList, i + 1);
            }

            public void onMatrixError(MatrixError matrixError) {
                PushManager.this.setAndStoreRegistrationState(RegistrationState.SERVER_REGISTERED);
                PushManager.this.dispatchUnregisterMatrixError(matrixError);
            }

            public void onNetworkError(Exception exc) {
                PushManager.this.setAndStoreRegistrationState(RegistrationState.SERVER_REGISTERED);
                PushManager.this.dispatchUnregisterNetworkError(exc);
            }

            public void onUnexpectedError(Exception exc) {
                PushManager.this.setAndStoreRegistrationState(RegistrationState.SERVER_REGISTERED);
                PushManager.this.dispatchUnregisterUnexpectedError(exc);
            }
        });
    }

    public void unregister(MXSession mXSession, Pusher pusher, final ApiCallback<Void> apiCallback) {
        mXSession.getPushersRestClient().removeHttpPusher(pusher.pushkey, pusher.appId, pusher.profileTag, pusher.lang, pusher.appDisplayName, pusher.deviceDisplayName, (String) pusher.data.get("url"), new SimpleApiCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                PushManager pushManager = PushManager.this;
                pushManager.refreshPushersList(new ArrayList(Matrix.getInstance(pushManager.mContext).getSessions()), apiCallback);
            }

            public void onMatrixError(MatrixError matrixError) {
                if (matrixError.mStatus.intValue() == 404) {
                    onSuccess((Void) null);
                } else {
                    super.onMatrixError(matrixError);
                }
            }
        });
    }

    public void unregister(final MXSession mXSession, final ApiCallback<Void> apiCallback) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("unregister ");
        sb.append(mXSession.getMyUserId());
        Log.d(str, sb.toString());
        mXSession.getPushersRestClient().removeHttpPusher(this.mRegistrationToken, "fr.gouv.tchap.a.android", computePushTag(mXSession), this.mPusherLang, this.mPusherAppName, this.mBasePusherDeviceName, DEFAULT_PUSHER_URL, new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                Log.i(PushManager.LOG_TAG, "unregisterSession succeeded");
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    try {
                        apiCallback.onSuccess(null);
                    } catch (Exception e) {
                        String access$100 = PushManager.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("unregister : onSuccess() ");
                        sb.append(e.getMessage());
                        Log.e(access$100, sb.toString(), e);
                    }
                }
            }

            public void onNetworkError(Exception exc) {
                String access$100 = PushManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("unregisterSession onNetworkError ");
                sb.append(exc.getMessage());
                Log.e(access$100, sb.toString(), exc);
                if (mXSession.isAlive()) {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        try {
                            apiCallback.onNetworkError(exc);
                        } catch (Exception e) {
                            String access$1002 = PushManager.LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("unregister : onError() ");
                            sb2.append(e.getMessage());
                            Log.e(access$1002, sb2.toString(), e);
                        }
                    }
                }
            }

            public void onMatrixError(MatrixError matrixError) {
                if (matrixError.mStatus.intValue() == 404) {
                    onSuccess((Void) null);
                    return;
                }
                String access$100 = PushManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("unregisterSession onMatrixError ");
                sb.append(matrixError.errcode);
                Log.e(access$100, sb.toString());
                if (mXSession.isAlive()) {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        try {
                            apiCallback.onMatrixError(matrixError);
                        } catch (Exception e) {
                            String access$1002 = PushManager.LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("unregister : onMatrixError() ");
                            sb2.append(e.getMessage());
                            Log.e(access$1002, sb2.toString(), e);
                        }
                    }
                }
            }

            public void onUnexpectedError(Exception exc) {
                String access$100 = PushManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("unregisterSession onUnexpectedError ");
                sb.append(exc.getMessage());
                Log.e(access$100, sb.toString(), exc);
                if (mXSession.isAlive()) {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        try {
                            apiCallback.onUnexpectedError(exc);
                        } catch (Exception e) {
                            String access$1002 = PushManager.LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("unregister : onUnexpectedError() ");
                            sb2.append(e.getMessage());
                            Log.e(access$1002, sb2.toString(), e);
                        }
                    }
                }
            }
        });
    }

    public boolean hasRegistrationToken() {
        return this.mRegistrationToken != null;
    }

    public String getCurrentRegistrationToken() {
        return this.mRegistrationToken;
    }

    public boolean isFcmRegistered() {
        return this.mRegistrationState == RegistrationState.FCM_REGISTERED || this.mRegistrationState == RegistrationState.SERVER_REGISTRATING || this.mRegistrationState == RegistrationState.SERVER_REGISTERED;
    }

    public boolean isServerRegistered() {
        return this.mRegistrationState == RegistrationState.SERVER_REGISTERED;
    }

    public boolean isServerUnRegistered() {
        return this.mRegistrationState == RegistrationState.FCM_REGISTERED;
    }

    public void clearPreferences() {
        getPushSharedPreferences().edit().clear().apply();
    }

    public NotificationPrivacy getNotificationPrivacy() {
        NotificationPrivacy notificationPrivacy = NotificationPrivacy.LOW_DETAIL;
        boolean isContentSendingAllowed = isContentSendingAllowed();
        boolean isBackgroundSyncAllowed = isBackgroundSyncAllowed();
        if (!isContentSendingAllowed || isBackgroundSyncAllowed) {
            return (isContentSendingAllowed || !isBackgroundSyncAllowed) ? notificationPrivacy : NotificationPrivacy.NORMAL;
        }
        return NotificationPrivacy.REDUCED;
    }

    public void setNotificationPrivacy(NotificationPrivacy notificationPrivacy, ApiCallback<Void> apiCallback) {
        int i = AnonymousClass12.$SwitchMap$im$vector$push$PushManager$NotificationPrivacy[notificationPrivacy.ordinal()];
        if (i == 1 || i == 2) {
            setContentSendingAllowed(false);
            setBackgroundSyncAllowed(false);
        } else if (i == 3) {
            setContentSendingAllowed(false);
            setBackgroundSyncAllowed(true);
        }
        forceSessionsRegistration(apiCallback);
    }

    public boolean areDeviceNotificationsAllowed() {
        return getPushSharedPreferences().getBoolean(PREFS_ALLOW_NOTIFICATIONS, true);
    }

    public void setDeviceNotificationsAllowed(boolean z) {
        getPushSharedPreferences().edit().putBoolean(PREFS_ALLOW_NOTIFICATIONS, z).apply();
        if (!useFcm()) {
            CommonActivityUtils.onPushUpdate(this.mContext);
        }
    }

    public boolean isScreenTurnedOn() {
        return getPushSharedPreferences().getBoolean(PREFS_TURN_SCREEN_ON, false);
    }

    public void setScreenTurnedOn(boolean z) {
        getPushSharedPreferences().edit().putBoolean(PREFS_TURN_SCREEN_ON, z).apply();
    }

    public boolean isBackgroundSyncAllowed() {
        if (!hasRegistrationToken() || SystemUtilsKt.isIgnoringBatteryOptimizations(this.mContext)) {
            return getPushSharedPreferences().getBoolean(PREFS_ALLOW_BACKGROUND_SYNC, true);
        }
        return false;
    }

    public void setBackgroundSyncAllowed(boolean z) {
        getPushSharedPreferences().edit().putBoolean(PREFS_ALLOW_BACKGROUND_SYNC, z).apply();
        CommonActivityUtils.onPushUpdate(this.mContext);
    }

    public boolean canStartAppInBackground() {
        return isBackgroundSyncAllowed() || getStoredRegistrationToken() != null;
    }

    public boolean isContentSendingAllowed() {
        return getPushSharedPreferences().getBoolean(PREFS_ALLOW_SENDING_CONTENT_TO_GCM, true);
    }

    public void setContentSendingAllowed(boolean z) {
        getPushSharedPreferences().edit().putBoolean(PREFS_ALLOW_SENDING_CONTENT_TO_GCM, z).apply();
    }

    public int getBackgroundSyncTimeOut() {
        return getPushSharedPreferences().getInt(PREFS_SYNC_TIMEOUT, 6000);
    }

    public void setBackgroundSyncTimeOut(int i) {
        getPushSharedPreferences().edit().putInt(PREFS_SYNC_TIMEOUT, i).apply();
    }

    public int getBackgroundSyncDelay() {
        String str = this.mRegistrationToken;
        String str2 = PREFS_SYNC_DELAY;
        if (str == null && getStoredRegistrationToken() == null && !getPushSharedPreferences().contains(str2)) {
            return MXCall.CALL_TIMEOUT_MS;
        }
        int i = 0;
        MXSession defaultSession = Matrix.getInstance(this.mContext).getDefaultSession();
        if (defaultSession != null) {
            i = defaultSession.getSyncDelay();
        }
        return getPushSharedPreferences().getInt(str2, i);
    }

    public void setBackgroundSyncDelay(int i) {
        if (this.mRegistrationToken == null) {
            i = Math.max(i, 10000);
        }
        getPushSharedPreferences().edit().putInt(PREFS_SYNC_DELAY, i).apply();
    }

    private SharedPreferences getPushSharedPreferences() {
        return this.mContext.getSharedPreferences(PREFS_PUSH, 0);
    }

    private String getStoredRegistrationToken() {
        return getPushSharedPreferences().getString(PREFS_PUSHER_REGISTRATION_TOKEN_KEY_FCM, null);
    }

    private String getOldStoredRegistrationToken() {
        return getPushSharedPreferences().getString(PREFS_PUSHER_REGISTRATION_TOKEN_KEY, null);
    }

    /* access modifiers changed from: private */
    public void clearOldStoredRegistrationToken() {
        Log.i(LOG_TAG, "Remove old registration token");
        getPushSharedPreferences().edit().remove(PREFS_PUSHER_REGISTRATION_TOKEN_KEY).apply();
    }

    /* access modifiers changed from: private */
    public void setAndStoreRegistrationToken(String str) {
        Log.i(LOG_TAG, "Saving registration token");
        this.mRegistrationToken = str;
        getPushSharedPreferences().edit().putString(PREFS_PUSHER_REGISTRATION_TOKEN_KEY_FCM, str).apply();
    }

    private RegistrationState getStoredRegistrationState() {
        return RegistrationState.values()[getPushSharedPreferences().getInt(PREFS_PUSHER_REGISTRATION_STATUS, RegistrationState.UNREGISTRATED.ordinal())];
    }

    /* access modifiers changed from: private */
    public void setAndStoreRegistrationState(RegistrationState registrationState) {
        this.mRegistrationState = registrationState;
        if (RegistrationState.SERVER_REGISTRATING != registrationState && RegistrationState.SERVER_UNREGISTRATING != registrationState) {
            getPushSharedPreferences().edit().putInt(PREFS_PUSHER_REGISTRATION_STATUS, registrationState.ordinal()).apply();
        }
    }

    public void clearFcmData(ApiCallback<Void> apiCallback) {
        try {
            setAndStoreRegistrationToken(null);
            setAndStoreRegistrationState(RegistrationState.UNREGISTRATED);
            apiCallback.onSuccess(null);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## clearFcmData failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            apiCallback.onUnexpectedError(e);
        }
    }

    private void dispatchRegisterSuccess() {
        synchronized (this.mRegistrationCallbacks) {
            for (ApiCallback onSuccess : this.mRegistrationCallbacks) {
                try {
                    onSuccess.onSuccess(null);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "dispatchRegisterSuccess", e);
                }
            }
            this.mRegistrationCallbacks.clear();
        }
    }

    private void dispatchUnregisterSuccess() {
        synchronized (this.mUnRegistrationCallbacks) {
            for (ApiCallback onSuccess : this.mUnRegistrationCallbacks) {
                try {
                    onSuccess.onSuccess(null);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "dispatchUnregisterSuccess", e);
                }
            }
            this.mUnRegistrationCallbacks.clear();
        }
    }

    /* access modifiers changed from: private */
    public void dispatchRegisterNetworkError(Exception exc) {
        synchronized (this.mRegistrationCallbacks) {
            for (ApiCallback onNetworkError : this.mRegistrationCallbacks) {
                try {
                    onNetworkError.onNetworkError(exc);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "dispatchRegisterNetworkError", e);
                }
            }
            this.mRegistrationCallbacks.clear();
        }
    }

    /* access modifiers changed from: private */
    public void dispatchUnregisterNetworkError(Exception exc) {
        synchronized (this.mUnRegistrationCallbacks) {
            for (ApiCallback onNetworkError : this.mUnRegistrationCallbacks) {
                try {
                    onNetworkError.onNetworkError(exc);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "dispatchUnregisterNetworkError", e);
                }
            }
            this.mUnRegistrationCallbacks.clear();
        }
    }

    /* access modifiers changed from: private */
    public void dispatchRegisterMatrixError(MatrixError matrixError) {
        synchronized (this.mRegistrationCallbacks) {
            for (ApiCallback onMatrixError : this.mRegistrationCallbacks) {
                try {
                    onMatrixError.onMatrixError(matrixError);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "dispatchRegisterMatrixError", e);
                }
            }
            this.mRegistrationCallbacks.clear();
        }
    }

    /* access modifiers changed from: private */
    public void dispatchUnregisterMatrixError(MatrixError matrixError) {
        synchronized (this.mUnRegistrationCallbacks) {
            for (ApiCallback onMatrixError : this.mUnRegistrationCallbacks) {
                try {
                    onMatrixError.onMatrixError(matrixError);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "dispatchUnregisterMatrixError", e);
                }
            }
            this.mUnRegistrationCallbacks.clear();
        }
    }

    /* access modifiers changed from: private */
    public void dispatchRegisterUnexpectedError(Exception exc) {
        synchronized (this.mRegistrationCallbacks) {
            for (ApiCallback onUnexpectedError : this.mRegistrationCallbacks) {
                try {
                    onUnexpectedError.onUnexpectedError(exc);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "dispatchRegisterUnexpectedError", e);
                }
            }
            this.mRegistrationCallbacks.clear();
        }
    }

    /* access modifiers changed from: private */
    public void dispatchUnregisterUnexpectedError(Exception exc) {
        synchronized (this.mUnRegistrationCallbacks) {
            for (ApiCallback onUnexpectedError : this.mUnRegistrationCallbacks) {
                try {
                    onUnexpectedError.onUnexpectedError(exc);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "dispatchUnregisterUnexpectedError", e);
                }
            }
            this.mUnRegistrationCallbacks.clear();
        }
    }
}
