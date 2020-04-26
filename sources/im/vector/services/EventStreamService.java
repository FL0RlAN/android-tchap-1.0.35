package im.vector.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.Matrix;
import im.vector.VectorApp;
import im.vector.ViewedRoomTracker;
import im.vector.notifications.NotificationUtils;
import im.vector.notifications.NotifiedEvent;
import im.vector.notifications.RoomsNotifications;
import im.vector.push.PushManager;
import im.vector.receiver.DismissNotificationReceiver;
import im.vector.util.CallsManager;
import im.vector.util.RiotEventDisplay;
import im.vector.util.SystemUtilsKt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.BingRulesManager.onBingRulesUpdateListener;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.data.store.MXStoreListener;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

public class EventStreamService extends Service {
    public static final String EXTRA_AUTO_RESTART_ACTION = "EventStreamService.EXTRA_AUTO_RESTART_ACTION";
    public static final String EXTRA_MATRIX_IDS = "EventStreamService.EXTRA_MATRIX_IDS";
    public static final String EXTRA_STREAM_ACTION = "EventStreamService.EXTRA_STREAM_ACTION";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = EventStreamService.class.getSimpleName();
    /* access modifiers changed from: private */
    public static EventStreamService mActiveEventStreamService = null;
    private static final Set<String> mBackgroundNotificationEventIds = new HashSet();
    private static final List<CharSequence> mBackgroundNotificationStrings = new ArrayList();
    private static final BingRule mDefaultBingRule;
    /* access modifiers changed from: private */
    public static ForegroundNotificationState mForegroundNotificationState = ForegroundNotificationState.NONE;
    private static String mLastBackgroundNotificationRoomId = null;
    private static int mLastBackgroundNotificationUnreadCount = 0;
    private static HandlerThread mNotificationHandlerThread = null;
    private static Handler mNotificationsHandler = null;
    private final onBingRulesUpdateListener mBingRulesUpdatesListener = new onBingRulesUpdateListener() {
        public void onBingRulesUpdate() {
            EventStreamService.this.getNotificationsHandler().post(new Runnable() {
                public void run() {
                    Log.d(EventStreamService.LOG_TAG, "## on bing rules update");
                    EventStreamService.this.mNotifiedEventsByRoomId = null;
                    EventStreamService.this.refreshMessagesNotification();
                }
            });
        }
    };
    private String mCallIdInProgress = null;
    private final MXEventListener mEventsListener = new MXEventListener() {
        public void onBingEvent(Event event, RoomState roomState, BingRule bingRule) {
            if (event.isCallEvent()) {
                MXSession mXSession = Matrix.getMXSession(EventStreamService.this.getApplicationContext(), event.getMatrixId());
                if (mXSession == null || !mXSession.isVoipCallSupported()) {
                    Log.d(EventStreamService.LOG_TAG, "ignore call event : voip not allowed");
                    return;
                }
            }
            String access$000 = EventStreamService.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("prepareNotification : ");
            sb.append(event.eventId);
            sb.append(" in ");
            sb.append(roomState.roomId);
            Log.d(access$000, sb.toString());
            EventStreamService.this.prepareNotification(event, bingRule);
        }

        public void onLiveEventsChunkProcessed(String str, String str2) {
            EventStreamService.this.getNotificationsHandler().post(new Runnable() {
                public void run() {
                    EventStreamService.this.refreshMessagesNotification();
                    EventStreamService.this.mPendingNotifications.clear();
                }
            });
            if (StreamAction.CATCHUP == EventStreamService.this.mServiceState || StreamAction.PAUSE == EventStreamService.this.mServiceState) {
                boolean z = false;
                for (MXSession mXSession : EventStreamService.this.mSessions) {
                    z |= mXSession.mCallsManager.hasActiveCalls();
                }
                if (z) {
                    Log.d(EventStreamService.LOG_TAG, "onLiveEventsChunkProcessed : Catchup again because there are active calls");
                    EventStreamService.this.catchup(false);
                } else if (StreamAction.CATCHUP == EventStreamService.this.mServiceState) {
                    Log.d(EventStreamService.LOG_TAG, "onLiveEventsChunkProcessed : no Active call");
                    CallsManager.getSharedInstance().checkDeadCalls();
                    EventStreamService.this.setServiceState(StreamAction.PAUSE);
                }
            }
            if (EventStreamService.mForegroundNotificationState == ForegroundNotificationState.INITIAL_SYNCING) {
                Log.d(EventStreamService.LOG_TAG, "onLiveEventsChunkProcessed : end of init sync");
                EventStreamService.this.refreshForegroundNotification();
            }
        }
    };
    private String mIncomingCallId = null;
    private boolean mIsSelfDestroyed = false;
    private List<String> mMatrixIds;
    /* access modifiers changed from: private */
    public Map<String, List<NotifiedEvent>> mNotifiedEventsByRoomId = null;
    /* access modifiers changed from: private */
    public final LinkedHashMap<String, NotifiedEvent> mPendingNotifications = new LinkedHashMap<>();
    /* access modifiers changed from: private */
    public PushManager mPushManager;
    /* access modifiers changed from: private */
    public StreamAction mServiceState = StreamAction.IDLE;
    /* access modifiers changed from: private */
    public List<MXSession> mSessions;
    /* access modifiers changed from: private */
    public boolean mSuspendWhenStarted = false;

    /* renamed from: im.vector.services.EventStreamService$12 reason: invalid class name */
    static /* synthetic */ class AnonymousClass12 {
        static final /* synthetic */ int[] $SwitchMap$im$vector$services$EventStreamService$ForegroundNotificationState = new int[ForegroundNotificationState.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(22:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|(2:17|18)|19|21|22|23|24|25|26|27|28|29|30|(3:31|32|34)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|1|2|3|(2:5|6)|7|(2:9|10)|11|13|14|15|(2:17|18)|19|21|22|23|24|25|26|27|28|29|30|31|32|34) */
        /* JADX WARNING: Can't wrap try/catch for region: R(28:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|17|18|19|21|22|23|24|25|26|27|28|29|30|31|32|34) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0053 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x005d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0067 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0071 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x007b */
        static {
            try {
                $SwitchMap$im$vector$services$EventStreamService$ForegroundNotificationState[ForegroundNotificationState.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$im$vector$services$EventStreamService$ForegroundNotificationState[ForegroundNotificationState.INITIAL_SYNCING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$im$vector$services$EventStreamService$ForegroundNotificationState[ForegroundNotificationState.LISTENING_FOR_EVENTS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$im$vector$services$EventStreamService$ForegroundNotificationState[ForegroundNotificationState.INCOMING_CALL.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$im$vector$services$EventStreamService$ForegroundNotificationState[ForegroundNotificationState.CALL_IN_PROGRESS.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            $SwitchMap$im$vector$services$EventStreamService$StreamAction = new int[StreamAction.values().length];
            $SwitchMap$im$vector$services$EventStreamService$StreamAction[StreamAction.START.ordinal()] = 1;
            $SwitchMap$im$vector$services$EventStreamService$StreamAction[StreamAction.RESUME.ordinal()] = 2;
            $SwitchMap$im$vector$services$EventStreamService$StreamAction[StreamAction.STOP.ordinal()] = 3;
            $SwitchMap$im$vector$services$EventStreamService$StreamAction[StreamAction.PAUSE.ordinal()] = 4;
            $SwitchMap$im$vector$services$EventStreamService$StreamAction[StreamAction.CATCHUP.ordinal()] = 5;
            try {
                $SwitchMap$im$vector$services$EventStreamService$StreamAction[StreamAction.PUSH_STATUS_UPDATE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    private enum ForegroundNotificationState {
        NONE,
        INITIAL_SYNCING,
        LISTENING_FOR_EVENTS,
        INCOMING_CALL,
        CALL_IN_PROGRESS
    }

    public enum StreamAction {
        IDLE,
        STOP,
        START,
        PAUSE,
        RESUME,
        CATCHUP,
        PUSH_STATUS_UPDATE,
        AUTO_RESTART
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    static {
        Boolean valueOf = Boolean.valueOf(true);
        BingRule bingRule = new BingRule("ruleKind", "aPattern", valueOf, valueOf, false);
        mDefaultBingRule = bingRule;
    }

    public static EventStreamService getInstance() {
        return mActiveEventStreamService;
    }

    public void startAccounts(List<String> list) {
        for (String str : list) {
            if (this.mMatrixIds.indexOf(str) < 0) {
                MXSession session = Matrix.getInstance(getApplicationContext()).getSession(str);
                this.mSessions.add(session);
                this.mMatrixIds.add(str);
                monitorSession(session);
                session.startEventStream(null);
            }
        }
    }

    public void stopAccounts(List<String> list) {
        for (String str : list) {
            if (this.mMatrixIds.indexOf(str) >= 0) {
                MXSession session = Matrix.getInstance(getApplicationContext()).getSession(str);
                if (session != null) {
                    session.stopEventStream();
                    session.getDataHandler().removeListener(this.mEventsListener);
                    session.getDataHandler().getBingRulesManager().removeBingRulesUpdateListener(this.mBingRulesUpdatesListener);
                    CallsManager.getSharedInstance().removeSession(session);
                    this.mSessions.remove(session);
                    this.mMatrixIds.remove(str);
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x005d  */
    public int onStartCommand(Intent intent, int i, int i2) {
        boolean z;
        if (intent == null || intent.hasExtra(EXTRA_AUTO_RESTART_ACTION)) {
            if (StreamAction.AUTO_RESTART == this.mServiceState) {
                Log.e(LOG_TAG, "onStartCommand : auto restart in progress ignore current command");
                return 1;
            }
            if (intent == null) {
                Log.e(LOG_TAG, "onStartCommand : null intent -> restart the service");
            } else if (StreamAction.IDLE == this.mServiceState) {
                Log.e(LOG_TAG, "onStartCommand : automatically restart the service");
            } else if (StreamAction.STOP == this.mServiceState) {
                Log.e(LOG_TAG, "onStartCommand : automatically restart the service even if the service is stopped");
            } else {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onStartCommand : EXTRA_AUTO_RESTART_ACTION has been set but mServiceState = ");
                sb.append(this.mServiceState);
                Log.e(str, sb.toString());
                z = false;
                if (z) {
                    List sessions = Matrix.getInstance(getApplicationContext()).getSessions();
                    if (sessions == null || sessions.isEmpty()) {
                        Log.e(LOG_TAG, "onStartCommand : no session");
                        return 2;
                    } else if (VectorApp.getInstance() != null && VectorApp.getInstance().didAppCrash()) {
                        Log.e(LOG_TAG, "onStartCommand : no auto restart because the application crashed");
                        return 2;
                    } else if (!Matrix.getInstance(getApplicationContext()).getPushManager().canStartAppInBackground()) {
                        Log.e(LOG_TAG, "onStartCommand : no auto restart because the user disabled the background sync");
                        return 2;
                    } else {
                        this.mSessions = new ArrayList();
                        this.mSessions.addAll(Matrix.getInstance(getApplicationContext()).getSessions());
                        this.mMatrixIds = new ArrayList();
                        for (MXSession mXSession : this.mSessions) {
                            mXSession.getDataHandler().getStore().open();
                            this.mMatrixIds.add(mXSession.getMyUserId());
                        }
                        this.mSuspendWhenStarted = true;
                        start();
                        if (StreamAction.START == this.mServiceState) {
                            setServiceState(StreamAction.AUTO_RESTART);
                        }
                        return 1;
                    }
                }
            }
            z = true;
            if (z) {
            }
        }
        this.mSuspendWhenStarted = false;
        StreamAction streamAction = StreamAction.values()[intent.getIntExtra(EXTRA_STREAM_ACTION, StreamAction.IDLE.ordinal())];
        String str2 = LOG_TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onStartCommand with action : ");
        sb2.append(streamAction);
        Log.d(str2, sb2.toString());
        String str3 = EXTRA_MATRIX_IDS;
        if (intent.hasExtra(str3) && this.mMatrixIds == null) {
            this.mMatrixIds = new ArrayList(Arrays.asList(intent.getStringArrayExtra(str3)));
            this.mSessions = new ArrayList();
            for (String session : this.mMatrixIds) {
                this.mSessions.add(Matrix.getInstance(getApplicationContext()).getSession(session));
            }
            String str4 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("onStartCommand : update the matrix ids list to ");
            sb3.append(this.mMatrixIds);
            Log.d(str4, sb3.toString());
        }
        switch (streamAction) {
            case START:
            case RESUME:
                List<MXSession> list = this.mSessions;
                if (list != null && !list.isEmpty()) {
                    start();
                    break;
                } else {
                    String str5 = LOG_TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("onStartCommand : empty sessions list with action ");
                    sb4.append(streamAction);
                    Log.e(str5, sb4.toString());
                    return 2;
                }
                break;
            case STOP:
                Log.d(LOG_TAG, "## onStartCommand(): service stopped");
                this.mIsSelfDestroyed = true;
                stopSelf();
                break;
            case PAUSE:
                pause();
                break;
            case CATCHUP:
                catchup(true);
                break;
            case PUSH_STATUS_UPDATE:
                pushStatusUpdate();
                break;
        }
        return 1;
    }

    private void autoRestart() {
        int nextInt = new Random().nextInt(5000) + PathInterpolatorCompat.MAX_NUM_POINTS;
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## autoRestart() : restarts after ");
        sb.append(nextInt);
        sb.append(" ms");
        Log.d(str, sb.toString());
        mForegroundNotificationState = ForegroundNotificationState.NONE;
        Intent intent = new Intent(getApplicationContext(), getClass());
        intent.setPackage(getPackageName());
        String str2 = EXTRA_AUTO_RESTART_ACTION;
        intent.putExtra(str2, str2);
        ((AlarmManager) getApplicationContext().getSystemService(NotificationCompat.CATEGORY_ALARM)).set(3, SystemClock.elapsedRealtime() + ((long) nextInt), PendingIntent.getService(getApplicationContext(), 1, intent, 1073741824));
    }

    public void onTaskRemoved(Intent intent) {
        Log.d(LOG_TAG, "## onTaskRemoved");
        autoRestart();
        super.onTaskRemoved(intent);
    }

    public void onDestroy() {
        if (!this.mIsSelfDestroyed) {
            setServiceState(StreamAction.STOP);
            if (!SystemUtilsKt.isIgnoringBatteryOptimizations(getApplicationContext()) && VERSION.SDK_INT >= 26 && mForegroundNotificationState == ForegroundNotificationState.INITIAL_SYNCING && Matrix.getInstance(getApplicationContext()).getPushManager().hasRegistrationToken()) {
                setForegroundNotificationState(ForegroundNotificationState.NONE, null);
            }
            Log.d(LOG_TAG, "## onDestroy() : restart it");
            autoRestart();
        } else {
            Log.d(LOG_TAG, "## onDestroy() : do nothing");
            stop();
            super.onDestroy();
        }
        this.mIsSelfDestroyed = false;
    }

    /* access modifiers changed from: private */
    public void startEventStream(MXSession mXSession, IMXStore iMXStore) {
        if (mXSession.getCurrentSyncToken() != null) {
            mXSession.resumeEventStream();
        } else {
            mXSession.startEventStream(iMXStore.getEventStreamToken());
        }
    }

    private StreamAction getServiceState() {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getState ");
        sb.append(this.mServiceState);
        Log.d(str, sb.toString());
        return this.mServiceState;
    }

    /* access modifiers changed from: private */
    public void setServiceState(StreamAction streamAction) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setState from ");
        sb.append(this.mServiceState);
        sb.append(" to ");
        sb.append(streamAction);
        Log.d(str, sb.toString());
        this.mServiceState = streamAction;
    }

    public static boolean isStopped() {
        return getInstance() == null || getInstance().mServiceState == StreamAction.STOP;
    }

    private void monitorSession(final MXSession mXSession) {
        mXSession.getDataHandler().addListener(this.mEventsListener);
        mXSession.getDataHandler().getBingRulesManager().addBingRulesUpdateListener(this.mBingRulesUpdatesListener);
        CallsManager.getSharedInstance().addSession(mXSession);
        mXSession.getDataHandler().addListener(new MXEventListener() {
            public void onInitialSyncComplete(String str) {
                mXSession.getDataHandler().getStore().post(new Runnable() {
                    public void run() {
                        new Handler(EventStreamService.this.getMainLooper()).post(new Runnable() {
                            public void run() {
                                EventStreamService.this.refreshForegroundNotification();
                            }
                        });
                    }
                });
            }
        });
        final IMXStore store = mXSession.getDataHandler().getStore();
        if (store.isReady()) {
            startEventStream(mXSession, store);
            if (this.mSuspendWhenStarted) {
                PushManager pushManager = this.mPushManager;
                if (pushManager != null) {
                    mXSession.setSyncDelay(pushManager.getBackgroundSyncDelay());
                    mXSession.setSyncTimeout(this.mPushManager.getBackgroundSyncTimeOut());
                }
                catchup(false);
                return;
            }
            return;
        }
        store.addMXStoreListener(new MXStoreListener() {
            public void onStoreReady(String str) {
                EventStreamService.this.startEventStream(mXSession, store);
                if (EventStreamService.this.mSuspendWhenStarted) {
                    if (EventStreamService.this.mPushManager != null) {
                        mXSession.setSyncDelay(EventStreamService.this.mPushManager.getBackgroundSyncDelay());
                        mXSession.setSyncTimeout(EventStreamService.this.mPushManager.getBackgroundSyncTimeOut());
                    }
                    EventStreamService.this.catchup(false);
                }
            }

            public void onStoreCorrupted(String str, String str2) {
                if (store.getEventStreamToken() == null) {
                    EventStreamService.this.startEventStream(mXSession, store);
                } else {
                    Matrix.getInstance(EventStreamService.this.getApplicationContext()).reloadSessions(EventStreamService.this.getApplicationContext());
                }
            }

            public void onStoreOOM(final String str, final String str2) {
                new Handler(EventStreamService.this.getMainLooper()).post(new Runnable() {
                    public void run() {
                        Context applicationContext = EventStreamService.this.getApplicationContext();
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(" : ");
                        sb.append(str2);
                        Toast.makeText(applicationContext, sb.toString(), 1).show();
                        Matrix.getInstance(EventStreamService.this.getApplicationContext()).reloadSessions(EventStreamService.this.getApplicationContext());
                    }
                });
            }
        });
    }

    private void start() {
        this.mPushManager = Matrix.getInstance(getApplicationContext()).getPushManager();
        StreamAction serviceState = getServiceState();
        if (serviceState == StreamAction.START) {
            Log.e(LOG_TAG, "start : Already started.");
            for (MXSession refreshNetworkConnection : this.mSessions) {
                refreshNetworkConnection.refreshNetworkConnection();
            }
        } else if (serviceState == StreamAction.PAUSE || serviceState == StreamAction.CATCHUP) {
            Log.e(LOG_TAG, "start : Resuming active stream.");
            resume();
        } else if (this.mSessions == null) {
            Log.e(LOG_TAG, "start : No valid MXSession.");
        } else {
            Log.d(LOG_TAG, "## start : start the service");
            EventStreamService eventStreamService = mActiveEventStreamService;
            if (!(eventStreamService == null || this == eventStreamService)) {
                eventStreamService.stop();
            }
            mActiveEventStreamService = this;
            for (MXSession mXSession : this.mSessions) {
                if (mXSession == null || mXSession.getDataHandler() == null || mXSession.getDataHandler().getStore() == null) {
                    Log.e(LOG_TAG, "start : the session is not anymore valid.");
                    return;
                }
                monitorSession(mXSession);
            }
            refreshForegroundNotification();
            setServiceState(StreamAction.START);
        }
    }

    public void stopNow() {
        stop();
        this.mIsSelfDestroyed = true;
        stopSelf();
    }

    private void stop() {
        Log.d(LOG_TAG, "## stop(): the service is stopped");
        setForegroundNotificationState(ForegroundNotificationState.NONE, null);
        List<MXSession> list = this.mSessions;
        if (list != null) {
            for (MXSession mXSession : list) {
                if (mXSession != null && mXSession.isAlive()) {
                    mXSession.stopEventStream();
                    mXSession.getDataHandler().removeListener(this.mEventsListener);
                    mXSession.getDataHandler().getBingRulesManager().removeBingRulesUpdateListener(this.mBingRulesUpdatesListener);
                    CallsManager.getSharedInstance().removeSession(mXSession);
                }
            }
        }
        this.mMatrixIds = null;
        this.mSessions = null;
        setServiceState(StreamAction.STOP);
        mActiveEventStreamService = null;
    }

    private void pause() {
        StreamAction serviceState = getServiceState();
        if (StreamAction.START == serviceState || StreamAction.RESUME == serviceState) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onStartCommand pause from state ");
            sb.append(serviceState);
            Log.d(str, sb.toString());
            List<MXSession> list = this.mSessions;
            if (list != null) {
                for (MXSession pauseEventStream : list) {
                    pauseEventStream.pauseEventStream();
                }
                setServiceState(StreamAction.PAUSE);
                return;
            }
            return;
        }
        String str2 = LOG_TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onStartCommand invalid state pause ");
        sb2.append(serviceState);
        Log.e(str2, sb2.toString());
    }

    /* access modifiers changed from: private */
    public void catchup(boolean z) {
        StreamAction serviceState = getServiceState();
        boolean z2 = true;
        if (!z) {
            Log.d(LOG_TAG, "catchup  without checking state ");
        } else {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("catchup with state ");
            sb.append(serviceState);
            sb.append(" CurrentActivity ");
            sb.append(VectorApp.getCurrentActivity());
            Log.d(str, sb.toString());
            if (!(serviceState == StreamAction.CATCHUP || serviceState == StreamAction.PAUSE || (StreamAction.START == serviceState && VectorApp.getCurrentActivity() == null))) {
                z2 = false;
            }
        }
        if (z2) {
            List<MXSession> list = this.mSessions;
            if (list != null) {
                for (MXSession catchupEventStream : list) {
                    catchupEventStream.catchupEventStream();
                }
            } else {
                Log.e(LOG_TAG, "catchup no session");
            }
            setServiceState(StreamAction.CATCHUP);
            return;
        }
        Log.d(LOG_TAG, "No catchup is triggered because there is already a running event thread");
    }

    private void resume() {
        Log.d(LOG_TAG, "## resume : resume the service");
        List<MXSession> list = this.mSessions;
        if (list != null) {
            for (MXSession resumeEventStream : list) {
                resumeEventStream.resumeEventStream();
            }
        }
        setServiceState(StreamAction.START);
    }

    private void pushStatusUpdate() {
        Log.d(LOG_TAG, "## pushStatusUpdate");
        if (ForegroundNotificationState.NONE != mForegroundNotificationState) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## pushStatusUpdate : push status succeeds. So, stop foreground service (");
            sb.append(mForegroundNotificationState);
            sb.append(")");
            Log.d(str, sb.toString());
            if (ForegroundNotificationState.LISTENING_FOR_EVENTS == mForegroundNotificationState) {
                setForegroundNotificationState(ForegroundNotificationState.NONE, null);
            }
        }
        refreshForegroundNotification();
    }

    private boolean shouldDisplayListenForEventsNotification() {
        return (!this.mPushManager.useFcm() || (TextUtils.isEmpty(this.mPushManager.getCurrentRegistrationToken()) && !this.mPushManager.isServerRegistered())) && this.mPushManager.isBackgroundSyncAllowed() && this.mPushManager.areDeviceNotificationsAllowed();
    }

    public void refreshForegroundNotification() {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## refreshForegroundNotification from state ");
        sb.append(mForegroundNotificationState);
        Log.d(str, sb.toString());
        MXSession defaultSession = Matrix.getInstance(getApplicationContext()).getDefaultSession();
        if (defaultSession == null) {
            Log.e(LOG_TAG, "## updateServiceForegroundState(): no session");
        } else if (mForegroundNotificationState == ForegroundNotificationState.INCOMING_CALL || mForegroundNotificationState == ForegroundNotificationState.CALL_IN_PROGRESS) {
            Log.d(LOG_TAG, "## refreshForegroundNotification : does nothing as there is a pending call");
        } else if (this.mPushManager != null) {
            if (!defaultSession.getDataHandler().isInitialSyncComplete() || isStopped() || this.mServiceState == StreamAction.CATCHUP) {
                String str2 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## refreshForegroundNotification : put the service in foreground because of an initial sync ");
                sb2.append(mForegroundNotificationState);
                Log.d(str2, sb2.toString());
                setForegroundNotificationState(ForegroundNotificationState.INITIAL_SYNCING, null);
            } else if (shouldDisplayListenForEventsNotification()) {
                Log.d(LOG_TAG, "## refreshForegroundNotification : put the service in foreground because of FCM registration");
                setForegroundNotificationState(ForegroundNotificationState.LISTENING_FOR_EVENTS, null);
            } else {
                String str3 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("## refreshForegroundNotification : put the service in background from state ");
                sb3.append(mForegroundNotificationState);
                Log.d(str3, sb3.toString());
                setForegroundNotificationState(ForegroundNotificationState.NONE, null);
            }
        }
    }

    private void setForegroundNotificationState(ForegroundNotificationState foregroundNotificationState, Notification notification) {
        if (foregroundNotificationState != mForegroundNotificationState) {
            mForegroundNotificationState = foregroundNotificationState;
            int i = AnonymousClass12.$SwitchMap$im$vector$services$EventStreamService$ForegroundNotificationState[mForegroundNotificationState.ordinal()];
            if (i == 1) {
                NotificationUtils.INSTANCE.cancelNotificationForegroundService(this);
                if (getInstance() != null) {
                    getInstance().stopForeground(true);
                }
            } else if (i == 2) {
                notification = NotificationUtils.INSTANCE.buildForegroundServiceNotification(this, R.string.notification_sync_in_progress);
            } else if (i == 3) {
                notification = NotificationUtils.INSTANCE.buildForegroundServiceNotification(this, R.string.notification_listening_for_events);
            } else if ((i == 4 || i == 5) && notification == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("A notification object must be passed for state ");
                sb.append(foregroundNotificationState);
                throw new IllegalArgumentException(sb.toString());
            }
            if (!(notification == null || getInstance() == null)) {
                getInstance().startForeground(61, notification);
            }
        }
    }

    private void prepareCallNotification(Event event, BingRule bingRule) {
        if (!event.getType().equals(Event.EVENT_TYPE_CALL_INVITE)) {
            Log.d(LOG_TAG, "prepareCallNotification : don't bing - Call invite");
            return;
        }
        MXSession mXSession = Matrix.getMXSession(getApplicationContext(), event.getMatrixId());
        if (mXSession == null || !mXSession.isAlive()) {
            Log.d(LOG_TAG, "prepareCallNotification : don't bing - no session");
            return;
        }
        Room room = mXSession.getDataHandler().getRoom(event.roomId);
        if (room == null) {
            Log.d(LOG_TAG, "prepareCallNotification : don't bing - the room does not exist");
            return;
        }
        String str = null;
        try {
            str = event.getContentAsJsonObject().get("call_id").getAsString();
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("prepareNotification : getContentAsJsonObject ");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString(), e);
        }
        String str3 = str;
        if (!TextUtils.isEmpty(str3)) {
            displayIncomingCallNotification(mXSession, room, event, str3, bingRule);
        }
    }

    /* access modifiers changed from: private */
    public void prepareNotification(Event event, BingRule bingRule) {
        if (this.mPendingNotifications.containsKey(event.eventId)) {
            Log.d(LOG_TAG, "prepareNotification : don't bing - the event was already binged");
        } else if (!this.mPushManager.areDeviceNotificationsAllowed()) {
            Log.d(LOG_TAG, "prepareNotification : the push has been disable on this device");
        } else if (event.isCallEvent()) {
            prepareCallNotification(event, bingRule);
        } else {
            String str = event.roomId;
            if (VectorApp.isAppInBackground() || str == null || !event.roomId.equals(ViewedRoomTracker.getInstance().getViewedRoomId())) {
                if (!event.getContent().getAsJsonObject().has("body")) {
                    if (!"m.room.member".equals(event.getType()) && !event.isCallEvent()) {
                        Log.d(LOG_TAG, "onBingEvent : don't bing - no body and not a call event");
                        return;
                    }
                }
                MXSession mXSession = Matrix.getMXSession(getApplicationContext(), event.getMatrixId());
                if (mXSession == null || !mXSession.isAlive()) {
                    Log.d(LOG_TAG, "prepareNotification : don't bing - no session");
                } else if (mXSession.getDataHandler().getRoom(str) == null) {
                    Log.d(LOG_TAG, "prepareNotification : don't bing - the room does not exist");
                } else {
                    if (bingRule == null) {
                        bingRule = mDefaultBingRule;
                    }
                    BingRule bingRule2 = bingRule;
                    LinkedHashMap<String, NotifiedEvent> linkedHashMap = this.mPendingNotifications;
                    String str2 = event.eventId;
                    NotifiedEvent notifiedEvent = new NotifiedEvent(event.roomId, event.eventId, bingRule2, event.getOriginServerTs());
                    linkedHashMap.put(str2, notifiedEvent);
                }
            } else {
                Log.d(LOG_TAG, "prepareNotification : don't bing because it is the currently opened room");
            }
        }
    }

    public static void onMessagesNotificationDismiss(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onMessagesNotificationDismiss ");
        sb.append(str);
        Log.d(str2, sb.toString());
        EventStreamService eventStreamService = mActiveEventStreamService;
        if (eventStreamService != null) {
            eventStreamService.refreshMessagesNotification();
        }
    }

    public static void cancelNotificationsForRoomId(String str, String str2) {
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("cancelNotificationsForRoomId ");
        sb.append(str);
        sb.append(" - ");
        sb.append(str2);
        Log.d(str3, sb.toString());
        EventStreamService eventStreamService = mActiveEventStreamService;
        if (eventStreamService != null) {
            eventStreamService.cancelNotifications(str2);
        }
    }

    /* access modifiers changed from: private */
    public Handler getNotificationsHandler() {
        String str = "## getNotificationsHandler failed : ";
        if (mNotificationHandlerThread == null) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("NotificationsService_");
                sb.append(System.currentTimeMillis());
                mNotificationHandlerThread = new HandlerThread(sb.toString(), 1);
                mNotificationHandlerThread.start();
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(e.getMessage());
                Log.e(str2, sb2.toString(), e);
            }
        }
        if (mNotificationsHandler == null) {
            try {
                mNotificationsHandler = new Handler(mNotificationHandlerThread.getLooper());
            } catch (Exception e2) {
                String str3 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str);
                sb3.append(e2.getMessage());
                Log.e(str3, sb3.toString(), e2);
            }
        }
        Handler handler = mNotificationsHandler;
        return handler == null ? new Handler(getMainLooper()) : handler;
    }

    private void clearNotification() {
        NotificationUtils.INSTANCE.cancelAllNotifications(this);
        getNotificationsHandler().post(new Runnable() {
            public void run() {
                if (EventStreamService.this.mPendingNotifications != null) {
                    EventStreamService.this.mPendingNotifications.clear();
                }
                if (EventStreamService.this.mNotifiedEventsByRoomId != null) {
                    EventStreamService.this.mNotifiedEventsByRoomId.clear();
                }
                RoomsNotifications.deleteCachedRoomNotifications(VectorApp.getInstance());
            }
        });
    }

    public static void removeNotification() {
        EventStreamService eventStreamService = mActiveEventStreamService;
        if (eventStreamService != null) {
            eventStreamService.clearNotification();
        }
    }

    public static void checkDisplayedNotifications() {
        EventStreamService eventStreamService = mActiveEventStreamService;
        if (eventStreamService != null) {
            eventStreamService.getNotificationsHandler().post(new Runnable() {
                public void run() {
                    if (EventStreamService.mActiveEventStreamService != null) {
                        EventStreamService.mActiveEventStreamService.refreshMessagesNotification();
                    }
                }
            });
        }
    }

    private void cancelNotifications(final String str) {
        getNotificationsHandler().post(new Runnable() {
            public void run() {
                if (EventStreamService.this.mNotifiedEventsByRoomId == null) {
                    return;
                }
                if (str == null || EventStreamService.this.mNotifiedEventsByRoomId.containsKey(str)) {
                    EventStreamService.this.mNotifiedEventsByRoomId = null;
                    EventStreamService.this.refreshMessagesNotification();
                }
            }
        });
    }

    public static void onStaticNotifiedEvent(Context context, Event event, String str, String str2, int i) {
        String str3;
        String str4;
        if (event != null && !mBackgroundNotificationEventIds.contains(event.eventId)) {
            mBackgroundNotificationEventIds.add(event.eventId);
            if (TextUtils.isEmpty(str)) {
                str3 = "";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(": ");
                str3 = sb.toString();
            }
            if (event.contentJson == null) {
                if (event.roomId == null || TextUtils.isEmpty(str3)) {
                    mBackgroundNotificationStrings.clear();
                    mLastBackgroundNotificationUnreadCount = mBackgroundNotificationEventIds.size();
                } else {
                    String str5 = mLastBackgroundNotificationRoomId;
                    if (str5 == null || !str5.equals(event.roomId)) {
                        mLastBackgroundNotificationUnreadCount = 0;
                        mLastBackgroundNotificationRoomId = event.roomId;
                    } else {
                        mBackgroundNotificationStrings.remove(0);
                    }
                    mLastBackgroundNotificationUnreadCount++;
                }
                Resources resources = context.getResources();
                int i2 = mLastBackgroundNotificationUnreadCount;
                str4 = resources.getQuantityString(R.plurals.room_new_messages_notification, i2, new Object[]{Integer.valueOf(i2)});
            } else {
                if (TextUtils.isEmpty(str2)) {
                    str2 = event.sender;
                }
                if (!TextUtils.isEmpty(str2) && !str2.equalsIgnoreCase(str)) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str3);
                    sb2.append(str2);
                    sb2.append(" ");
                    str3 = sb2.toString();
                }
                if (event.isEncrypted()) {
                    str4 = context.getString(R.string.encrypted_message);
                } else {
                    RiotEventDisplay riotEventDisplay = new RiotEventDisplay(context);
                    riotEventDisplay.setPrependMessagesWithAuthor(false);
                    str4 = riotEventDisplay.getTextualDisplay(event, null).toString();
                }
            }
            if (!TextUtils.isEmpty(str4)) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append(str4);
                SpannableString spannableString = new SpannableString(sb3.toString());
                spannableString.setSpan(new StyleSpan(1), 0, str3.length(), 33);
                mBackgroundNotificationStrings.add(0, spannableString);
                List<CharSequence> list = mBackgroundNotificationStrings;
                BingRule bingRule = new BingRule(null, null, Boolean.valueOf(true), Boolean.valueOf(true), true);
                displayMessagesNotificationStatic(context, list, bingRule);
            }
        } else if (i == 0) {
            mBackgroundNotificationStrings.clear();
            mLastBackgroundNotificationUnreadCount = 0;
            mLastBackgroundNotificationRoomId = null;
            displayMessagesNotificationStatic(context, null, null);
        }
    }

    private static void displayMessagesNotificationStatic(Context context, List<CharSequence> list, BingRule bingRule) {
        if (!Matrix.getInstance(context).getPushManager().areDeviceNotificationsAllowed() || list == null || list.isEmpty()) {
            NotificationUtils.INSTANCE.cancelNotificationMessage(context);
            RoomsNotifications.deleteCachedRoomNotifications(VectorApp.getInstance());
            return;
        }
        Notification buildMessagesListNotification = NotificationUtils.INSTANCE.buildMessagesListNotification(context, list, bingRule);
        if (buildMessagesListNotification != null) {
            NotificationUtils.INSTANCE.showNotificationMessage(context, buildMessagesListNotification);
        } else {
            NotificationUtils.INSTANCE.cancelNotificationMessage(context);
        }
    }

    /* access modifiers changed from: private */
    public void displayMessagesNotification(final List<CharSequence> list, final BingRule bingRule) {
        new Handler(getMainLooper()).post(new Runnable() {
            public void run() {
                if (EventStreamService.this.mPushManager.areDeviceNotificationsAllowed()) {
                    List list = list;
                    if (list != null && !list.isEmpty()) {
                        Notification buildMessagesListNotification = NotificationUtils.INSTANCE.buildMessagesListNotification(EventStreamService.this.getApplicationContext(), list, bingRule);
                        if (buildMessagesListNotification != null) {
                            NotificationUtils.INSTANCE.showNotificationMessage(EventStreamService.this, buildMessagesListNotification);
                            return;
                        } else {
                            NotificationUtils.INSTANCE.cancelNotificationMessage(EventStreamService.this);
                            return;
                        }
                    }
                }
                NotificationUtils.INSTANCE.cancelNotificationMessage(EventStreamService.this);
                RoomsNotifications.deleteCachedRoomNotifications(VectorApp.getInstance());
            }
        });
    }

    /* access modifiers changed from: private */
    public void refreshMessagesNotification() {
        mBackgroundNotificationStrings.clear();
        final boolean z = false;
        mLastBackgroundNotificationUnreadCount = 0;
        mLastBackgroundNotificationRoomId = null;
        mBackgroundNotificationEventIds.clear();
        final NotifiedEvent eventToNotify = getEventToNotify();
        if (!this.mPushManager.areDeviceNotificationsAllowed()) {
            this.mNotifiedEventsByRoomId = null;
            new Handler(getMainLooper()).post(new Runnable() {
                public void run() {
                    EventStreamService.this.displayMessagesNotification(null, null);
                }
            });
        } else if (refreshNotifiedMessagesList()) {
            Map<String, List<NotifiedEvent>> map = this.mNotifiedEventsByRoomId;
            if (map == null || map.size() == 0) {
                new Handler(getMainLooper()).post(new Runnable() {
                    public void run() {
                        EventStreamService.this.displayMessagesNotification(null, null);
                    }
                });
            } else {
                if (eventToNotify == null) {
                    z = true;
                }
                if (z) {
                    IMXStore store = Matrix.getInstance(getBaseContext()).getDefaultSession().getDataHandler().getStore();
                    if (store == null) {
                        Log.e(LOG_TAG, "## refreshMessagesNotification() : null store");
                        return;
                    }
                    long j = 0;
                    for (String str : new ArrayList(this.mNotifiedEventsByRoomId.keySet())) {
                        List list = (List) this.mNotifiedEventsByRoomId.get(str);
                        NotifiedEvent notifiedEvent = (NotifiedEvent) list.get(list.size() - 1);
                        Event event = store.getEvent(notifiedEvent.mEventId, notifiedEvent.mRoomId);
                        if (event == null) {
                            String str2 = LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## refreshMessagesNotification() : the event ");
                            sb.append(notifiedEvent.mEventId);
                            sb.append(" in room ");
                            sb.append(notifiedEvent.mRoomId);
                            sb.append(" does not exist anymore");
                            Log.e(str2, sb.toString());
                            this.mNotifiedEventsByRoomId.remove(str);
                        } else if (event.getOriginServerTs() > j) {
                            j = event.getOriginServerTs();
                            eventToNotify = notifiedEvent;
                        }
                    }
                }
                final HashMap hashMap = new HashMap(this.mNotifiedEventsByRoomId);
                if (eventToNotify != null) {
                    DismissNotificationReceiver.setLatestNotifiedMessageTs(this, eventToNotify.mOriginServerTs);
                }
                new Handler(getMainLooper()).post(new Runnable() {
                    public void run() {
                        if (hashMap.size() > 0) {
                            Notification buildMessageNotification = NotificationUtils.INSTANCE.buildMessageNotification(EventStreamService.this.getApplicationContext(), (Map<String, ? extends List<? extends NotifiedEvent>>) new HashMap<String,Object>(hashMap), eventToNotify, z);
                            if (buildMessageNotification != null) {
                                NotificationUtils.INSTANCE.showNotificationMessage(EventStreamService.this, buildMessageNotification);
                            } else {
                                EventStreamService.this.displayMessagesNotification(null, null);
                            }
                        } else {
                            Log.e(EventStreamService.LOG_TAG, "## refreshMessagesNotification() : mNotifiedEventsByRoomId is empty");
                            EventStreamService.this.displayMessagesNotification(null, null);
                        }
                    }
                });
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0078 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x002f A[SYNTHETIC] */
    private NotifiedEvent getEventToNotify() {
        CharSequence charSequence;
        if (this.mPendingNotifications.size() > 0) {
            IMXStore store = Matrix.getInstance(getBaseContext()).getDefaultSession().getDataHandler().getStore();
            ArrayList<NotifiedEvent> arrayList = new ArrayList<>(this.mPendingNotifications.values());
            Collections.reverse(arrayList);
            for (NotifiedEvent notifiedEvent : arrayList) {
                Room room = store.getRoom(notifiedEvent.mRoomId);
                if (room != null && !room.isEventRead(notifiedEvent.mEventId)) {
                    Event event = store.getEvent(notifiedEvent.mEventId, notifiedEvent.mRoomId);
                    if (event != null) {
                        RiotEventDisplay riotEventDisplay = new RiotEventDisplay(getApplicationContext());
                        riotEventDisplay.setPrependMessagesWithAuthor(false);
                        CharSequence textualDisplay = riotEventDisplay.getTextualDisplay(event, room.getState());
                        if (textualDisplay != null) {
                            charSequence = textualDisplay.toString();
                            if (TextUtils.isEmpty(charSequence)) {
                                this.mPendingNotifications.clear();
                                this.mNotifiedEventsByRoomId = null;
                                return notifiedEvent;
                            }
                        }
                    }
                    charSequence = null;
                    if (TextUtils.isEmpty(charSequence)) {
                    }
                }
            }
            this.mPendingNotifications.clear();
        }
        return null;
    }

    private boolean refreshNotifiedMessagesList() {
        boolean z;
        MXSession defaultSession = Matrix.getInstance(getBaseContext()).getDefaultSession();
        if (defaultSession == null || !defaultSession.getDataHandler().getBingRulesManager().isReady()) {
            return false;
        }
        IMXStore store = defaultSession.getDataHandler().getStore();
        if (store == null || !store.areReceiptsReady()) {
            return false;
        }
        long notificationDismissTs = DismissNotificationReceiver.getNotificationDismissTs(this);
        Map<String, List<NotifiedEvent>> map = this.mNotifiedEventsByRoomId;
        if (map == null) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("##refreshNotifiedMessagesList() : min message TS ");
            sb.append(notificationDismissTs);
            Log.d(str, sb.toString());
            this.mNotifiedEventsByRoomId = new HashMap();
            for (Room room : store.getRooms()) {
                if (room.isInvited()) {
                    Collection<Event> roomMessages = store.getRoomMessages(room.getRoomId());
                    if (roomMessages != null) {
                        for (Event event : roomMessages) {
                            if (event.getOriginServerTs() >= notificationDismissTs) {
                                if ("m.room.member".equals(event.getType())) {
                                    try {
                                        if ("invite".equals(event.getContentAsJsonObject().getAsJsonPrimitive("membership").getAsString())) {
                                            BingRule fulfillRule = defaultSession.fulfillRule(event);
                                            if (fulfillRule != null && fulfillRule.isEnabled && fulfillRule.shouldNotify()) {
                                                ArrayList arrayList = new ArrayList();
                                                String str2 = event.roomId;
                                                String str3 = event.eventId;
                                                long originServerTs = event.getOriginServerTs();
                                                NotifiedEvent notifiedEvent = r11;
                                                NotifiedEvent notifiedEvent2 = new NotifiedEvent(str2, str3, fulfillRule, originServerTs);
                                                arrayList.add(notifiedEvent);
                                                this.mNotifiedEventsByRoomId.put(room.getRoomId(), arrayList);
                                            }
                                        }
                                    } catch (Exception e) {
                                        Log.e(LOG_TAG, "##refreshNotifiedMessagesList() : invitation parsing failed", e);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    try {
                        List<Event> unreadEvents = store.unreadEvents(room.getRoomId(), null);
                        if (unreadEvents != null && unreadEvents.size() > 0) {
                            ArrayList arrayList2 = new ArrayList();
                            for (Event event2 : unreadEvents) {
                                if (event2.getOriginServerTs() > notificationDismissTs) {
                                    BingRule fulfillRule2 = defaultSession.fulfillRule(event2);
                                    if (fulfillRule2 != null && fulfillRule2.isEnabled && fulfillRule2.shouldNotify()) {
                                        NotifiedEvent notifiedEvent3 = new NotifiedEvent(event2.roomId, event2.eventId, fulfillRule2, event2.getOriginServerTs());
                                        arrayList2.add(notifiedEvent3);
                                    }
                                } else {
                                    String str4 = LOG_TAG;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("##refreshNotifiedMessagesList() : ignore event ");
                                    sb2.append(event2.eventId);
                                    sb2.append(" in room ");
                                    sb2.append(event2.roomId);
                                    sb2.append(" because of the TS ");
                                    sb2.append(event2.originServerTs);
                                    Log.d(str4, sb2.toString());
                                }
                            }
                            if (arrayList2.size() > 0) {
                                this.mNotifiedEventsByRoomId.put(room.getRoomId(), arrayList2);
                            }
                        }
                    } catch (Exception e2) {
                        String str5 = LOG_TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("##refreshNotifiedMessagesList(): failed checking the unread ");
                        sb3.append(e2.getMessage());
                        Log.e(str5, sb3.toString(), e2);
                    }
                }
            }
            return true;
        }
        try {
            z = false;
            for (String str6 : new ArrayList(map.keySet())) {
                try {
                    Room room2 = store.getRoom(str6);
                    if (room2 == null) {
                        String str7 = LOG_TAG;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("## refreshNotifiedMessagesList() : the room ");
                        sb4.append(str6);
                        sb4.append(" does not exist anymore");
                        Log.d(str7, sb4.toString());
                        this.mNotifiedEventsByRoomId.remove(str6);
                    } else {
                        List list = (List) this.mNotifiedEventsByRoomId.get(str6);
                        NotifiedEvent notifiedEvent4 = (NotifiedEvent) list.get(0);
                        if (room2.isEventRead(notifiedEvent4.mEventId) || notifiedEvent4.mOriginServerTs < notificationDismissTs) {
                            NotifiedEvent notifiedEvent5 = (NotifiedEvent) list.get(list.size() - 1);
                            if (room2.isEventRead(notifiedEvent5.mEventId) || notifiedEvent5.mOriginServerTs <= notificationDismissTs) {
                                list.clear();
                            } else {
                                boolean z2 = z;
                                int i = 0;
                                while (i < list.size()) {
                                    try {
                                        NotifiedEvent notifiedEvent6 = (NotifiedEvent) list.get(i);
                                        if (!room2.isEventRead(notifiedEvent6.mEventId)) {
                                            if (notifiedEvent6.mOriginServerTs > notificationDismissTs) {
                                                i++;
                                            }
                                        }
                                        list.remove(i);
                                        z2 = true;
                                    } catch (Exception e3) {
                                        e = e3;
                                        z = z2;
                                        String str8 = LOG_TAG;
                                        StringBuilder sb5 = new StringBuilder();
                                        sb5.append("##refreshNotifiedMessagesList(): failed while building mNotifiedEventsByRoomId ");
                                        sb5.append(e.getMessage());
                                        Log.e(str8, sb5.toString(), e);
                                        return z;
                                    }
                                }
                                z = z2;
                            }
                            if (list.size() == 0) {
                                this.mNotifiedEventsByRoomId.remove(str6);
                            }
                        }
                    }
                    z = true;
                } catch (Exception e4) {
                    e = e4;
                    String str82 = LOG_TAG;
                    StringBuilder sb52 = new StringBuilder();
                    sb52.append("##refreshNotifiedMessagesList(): failed while building mNotifiedEventsByRoomId ");
                    sb52.append(e.getMessage());
                    Log.e(str82, sb52.toString(), e);
                    return z;
                }
            }
        } catch (Exception e5) {
            e = e5;
            z = false;
            String str822 = LOG_TAG;
            StringBuilder sb522 = new StringBuilder();
            sb522.append("##refreshNotifiedMessagesList(): failed while building mNotifiedEventsByRoomId ");
            sb522.append(e.getMessage());
            Log.e(str822, sb522.toString(), e);
            return z;
        }
        return z;
    }

    public void displayIncomingCallNotification(MXSession mXSession, Room room, Event event, String str, BingRule bingRule) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("displayIncomingCallNotification : ");
        sb.append(str);
        sb.append(" in ");
        sb.append(room.getRoomId());
        Log.d(str2, sb.toString());
        if (!TextUtils.isEmpty(this.mIncomingCallId)) {
            Log.d(LOG_TAG, "displayIncomingCallNotification : the incoming call in progress is already displayed");
        } else if (!TextUtils.isEmpty(this.mCallIdInProgress)) {
            Log.d(LOG_TAG, "displayIncomingCallNotification : a 'call in progress' notification is displayed");
        } else if (CallsManager.getSharedInstance().getActiveCall() == null) {
            Log.d(LOG_TAG, "displayIncomingCallNotification : display the dedicated notification");
            setForegroundNotificationState(ForegroundNotificationState.INCOMING_CALL, NotificationUtils.INSTANCE.buildIncomingCallNotification(this, RoomsNotifications.getRoomName(getApplicationContext(), mXSession, room, event), mXSession.getMyUserId(), str));
            this.mIncomingCallId = str;
            try {
                if (Matrix.getInstance(VectorApp.getInstance()).getPushManager().isScreenTurnedOn()) {
                    WakeLock newWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(268435466, "Tchap:MXEventListener");
                    newWakeLock.acquire(3000);
                    newWakeLock.release();
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "## turnScreenOn() failed", e);
            }
        } else {
            Log.d(LOG_TAG, "displayIncomingCallNotification : do not display the incoming call notification because there is a pending call");
        }
    }

    public void displayCallInProgressNotification(MXSession mXSession, Room room, String str) {
        if (str != null) {
            setForegroundNotificationState(ForegroundNotificationState.CALL_IN_PROGRESS, NotificationUtils.INSTANCE.buildPendingCallNotification(getApplicationContext(), DinsicUtils.getRoomDisplayName(this, room), room.getRoomId(), mXSession.getCredentials().userId, str));
            this.mCallIdInProgress = str;
        }
    }

    public void hideCallNotifications() {
        if (ForegroundNotificationState.CALL_IN_PROGRESS == mForegroundNotificationState || ForegroundNotificationState.INCOMING_CALL == mForegroundNotificationState) {
            if (ForegroundNotificationState.CALL_IN_PROGRESS == mForegroundNotificationState) {
                this.mCallIdInProgress = null;
            } else {
                this.mIncomingCallId = null;
            }
            setForegroundNotificationState(ForegroundNotificationState.NONE, null);
            refreshForegroundNotification();
        }
    }
}
