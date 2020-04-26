package org.matrix.androidsdk.sync;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import androidx.core.app.NotificationCompat;
import java.lang.Thread.State;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiFailureCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.listeners.IMXNetworkEventListener;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.metrics.MetricsListener;
import org.matrix.androidsdk.network.NetworkConnectivityReceiver;
import org.matrix.androidsdk.rest.client.EventsRestClient;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.sync.RoomsSyncResponse;
import org.matrix.androidsdk.rest.model.sync.SyncResponse;

public class EventsThread extends Thread {
    private static final int DEFAULT_CLIENT_TIMEOUT_MS = 120000;
    private static final int DEFAULT_SERVER_TIMEOUT_MS = 30000;
    /* access modifiers changed from: private */
    public static final String LOG_TAG = EventsThread.class.getSimpleName();
    private static final int RETRY_WAIT_TIME_MS = 10000;
    /* access modifiers changed from: private */
    public static final Map<String, EventsThread> mSyncObjectByInstance = new HashMap();
    private final AlarmManager mAlarmManager;
    private final Context mContext;
    /* access modifiers changed from: private */
    public String mCurrentToken;
    /* access modifiers changed from: private */
    public int mDefaultServerTimeoutms = DEFAULT_SERVER_TIMEOUT_MS;
    private EventsRestClient mEventsRestClient;
    private ApiFailureCallback mFailureCallback;
    private String mFilterOrFilterId;
    /* access modifiers changed from: private */
    public boolean mIsCatchingUp = false;
    /* access modifiers changed from: private */
    public boolean mIsNetworkSuspended = false;
    private boolean mIsOnline = false;
    /* access modifiers changed from: private */
    public boolean mKilling = false;
    /* access modifiers changed from: private */
    public EventsThreadListener mListener;
    private MetricsListener mMetricsListener;
    private NetworkConnectivityReceiver mNetworkConnectivityReceiver;
    private final IMXNetworkEventListener mNetworkListener = new IMXNetworkEventListener() {
        public void onNetworkConnectionUpdate(boolean z) {
            String access$000 = EventsThread.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onNetworkConnectionUpdate : before ");
            sb.append(EventsThread.this.mbIsConnected);
            sb.append(" now ");
            sb.append(z);
            Log.d(access$000, sb.toString());
            synchronized (EventsThread.this.mSyncObject) {
                EventsThread.this.mbIsConnected = z;
            }
            if (z && !EventsThread.this.mKilling) {
                Log.d(EventsThread.LOG_TAG, "onNetworkConnectionUpdate : call onNetworkAvailable");
                EventsThread.this.onNetworkAvailable();
            }
        }
    };
    /* access modifiers changed from: private */
    public int mNextServerTimeoutms = DEFAULT_SERVER_TIMEOUT_MS;
    /* access modifiers changed from: private */
    public boolean mPaused = true;
    /* access modifiers changed from: private */
    public PendingIntent mPendingDelayedIntent;
    private PowerManager mPowerManager;
    /* access modifiers changed from: private */
    public int mRequestDelayMs = 0;
    /* access modifiers changed from: private */
    public final Object mSyncObject = new Object();
    /* access modifiers changed from: private */
    public boolean mbIsConnected = true;

    public static class SyncDelayReceiver extends BroadcastReceiver {
        public static final String EXTRA_INSTANCE_ID = "EXTRA_INSTANCE_ID";

        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra(EXTRA_INSTANCE_ID);
            if (stringExtra != null && EventsThread.mSyncObjectByInstance.containsKey(stringExtra)) {
                EventsThread eventsThread = (EventsThread) EventsThread.mSyncObjectByInstance.get(stringExtra);
                eventsThread.mPendingDelayedIntent = null;
                String access$000 = EventsThread.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("start a sync after ");
                sb.append(eventsThread.mRequestDelayMs);
                sb.append(" ms");
                Log.d(access$000, sb.toString());
                synchronized (eventsThread.mSyncObject) {
                    eventsThread.mSyncObject.notify();
                }
            }
        }
    }

    public EventsThread(Context context, EventsRestClient eventsRestClient, EventsThreadListener eventsThreadListener, String str) {
        super("Events thread");
        this.mContext = context;
        this.mEventsRestClient = eventsRestClient;
        this.mListener = eventsThreadListener;
        this.mCurrentToken = str;
        mSyncObjectByInstance.put(toString(), this);
        this.mAlarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        this.mPowerManager = (PowerManager) context.getSystemService("power");
    }

    public void setMetricsListener(MetricsListener metricsListener) {
        this.mMetricsListener = metricsListener;
    }

    public String getCurrentSyncToken() {
        return this.mCurrentToken;
    }

    public void setFilterOrFilterId(String str) {
        this.mFilterOrFilterId = str;
    }

    public void setServerLongPollTimeout(int i) {
        this.mDefaultServerTimeoutms = Math.max(i, DEFAULT_SERVER_TIMEOUT_MS);
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setServerLongPollTimeout : ");
        sb.append(this.mDefaultServerTimeoutms);
        Log.d(str, sb.toString());
    }

    public int getServerLongPollTimeout() {
        return this.mDefaultServerTimeoutms;
    }

    public void setSyncDelay(int i) {
        this.mRequestDelayMs = Math.max(0, i);
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## setSyncDelay() : ");
        sb.append(this.mRequestDelayMs);
        sb.append(" with state ");
        sb.append(getState());
        Log.d(str, sb.toString());
        if (State.WAITING != getState()) {
            return;
        }
        if (!this.mPaused || (this.mRequestDelayMs == 0 && this.mIsCatchingUp)) {
            if (!this.mPaused) {
                Log.d(LOG_TAG, "## setSyncDelay() : resume the application");
            }
            if (this.mRequestDelayMs == 0 && this.mIsCatchingUp) {
                Log.d(LOG_TAG, "## setSyncDelay() : cancel catchup");
                this.mIsCatchingUp = false;
            }
            synchronized (this.mSyncObject) {
                this.mSyncObject.notify();
            }
        }
    }

    public int getSyncDelay() {
        return this.mRequestDelayMs;
    }

    public void setNetworkConnectivityReceiver(NetworkConnectivityReceiver networkConnectivityReceiver) {
        this.mNetworkConnectivityReceiver = networkConnectivityReceiver;
    }

    public void setFailureCallback(ApiFailureCallback apiFailureCallback) {
        this.mFailureCallback = apiFailureCallback;
    }

    public void pause() {
        Log.d(LOG_TAG, "pause()");
        this.mPaused = true;
        this.mIsCatchingUp = false;
    }

    /* access modifiers changed from: private */
    public void onNetworkAvailable() {
        Log.d(LOG_TAG, "onNetWorkAvailable()");
        if (this.mIsNetworkSuspended) {
            this.mIsNetworkSuspended = false;
            if (this.mPaused) {
                Log.d(LOG_TAG, "the event thread is still suspended");
                return;
            }
            Log.d(LOG_TAG, "Resume the thread");
            this.mIsCatchingUp = false;
            synchronized (this.mSyncObject) {
                this.mSyncObject.notify();
            }
            return;
        }
        Log.d(LOG_TAG, "onNetWorkAvailable() : nothing to do");
    }

    public void unpause() {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## unpause() : thread state ");
        sb.append(getState());
        Log.d(str, sb.toString());
        if (State.WAITING == getState()) {
            Log.d(LOG_TAG, "## unpause() : the thread was paused so resume it.");
            this.mPaused = false;
            synchronized (this.mSyncObject) {
                this.mSyncObject.notify();
            }
        }
        this.mIsCatchingUp = false;
    }

    public void catchup() {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## catchup() : thread state ");
        sb.append(getState());
        Log.d(str, sb.toString());
        if (State.WAITING == getState()) {
            Log.d(LOG_TAG, "## catchup() : the thread was paused so wake it up");
            this.mPaused = false;
            synchronized (this.mSyncObject) {
                this.mSyncObject.notify();
            }
        }
        this.mIsCatchingUp = true;
    }

    public void kill() {
        Log.d(LOG_TAG, "killing ...");
        this.mKilling = true;
        if (this.mPaused) {
            Log.d(LOG_TAG, "killing : the thread was pause so wake it up");
            this.mPaused = false;
            synchronized (this.mSyncObject) {
                this.mSyncObject.notify();
            }
            Log.d(LOG_TAG, "Resume the thread to kill it.");
        }
    }

    public void cancelKill() {
        if (this.mKilling) {
            Log.d(LOG_TAG, "## cancelKill() : Cancel the pending kill");
            this.mKilling = false;
            return;
        }
        Log.d(LOG_TAG, "## cancelKill() : Nothing to d");
    }

    public void setIsOnline(boolean z) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setIsOnline to ");
        sb.append(z);
        Log.d(str, sb.toString());
        this.mIsOnline = z;
    }

    public boolean isOnline() {
        return this.mIsOnline;
    }

    public void run() {
        try {
            Looper.prepare();
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## run() : prepare failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
        startSync();
    }

    /* access modifiers changed from: private */
    public static boolean hasDevicesChanged(SyncResponse syncResponse) {
        return (syncResponse.deviceLists == null || syncResponse.deviceLists.changed == null || syncResponse.deviceLists.changed.size() <= 0) ? false : true;
    }

    private void resumeInitialSync() {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Resuming initial sync from ");
        sb.append(this.mCurrentToken);
        Log.d(str, sb.toString());
        SyncResponse syncResponse = new SyncResponse();
        syncResponse.nextBatch = this.mCurrentToken;
        this.mListener.onSyncResponse(syncResponse, null, true);
    }

    private void executeInitialSync() {
        Log.d(LOG_TAG, "Requesting initial sync...");
        long currentTimeMillis = System.currentTimeMillis();
        while (!isInitialSyncDone()) {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            this.mEventsRestClient.syncFromToken(null, 0, 120000, this.mIsOnline ? null : User.PRESENCE_OFFLINE, this.mFilterOrFilterId, new SimpleApiCallback<SyncResponse>(this.mFailureCallback) {
                public void onSuccess(SyncResponse syncResponse) {
                    Log.d(EventsThread.LOG_TAG, "Received initial sync response.");
                    boolean z = false;
                    EventsThread.this.mNextServerTimeoutms = EventsThread.hasDevicesChanged(syncResponse) ? 0 : EventsThread.this.mDefaultServerTimeoutms;
                    EventsThreadListener access$1100 = EventsThread.this.mListener;
                    if (EventsThread.this.mNextServerTimeoutms == 0) {
                        z = true;
                    }
                    access$1100.onSyncResponse(syncResponse, null, z);
                    EventsThread.this.mCurrentToken = syncResponse.nextBatch;
                    countDownLatch.countDown();
                }

                private void sleepAndUnblock() {
                    Log.i(EventsThread.LOG_TAG, "Waiting a bit before retrying");
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        public void run() {
                            countDownLatch.countDown();
                        }
                    }, 10000);
                }

                public void onNetworkError(Exception exc) {
                    if (EventsThread.this.isInitialSyncDone()) {
                        onSuccess((SyncResponse) null);
                        return;
                    }
                    String access$000 = EventsThread.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Sync V2 onNetworkError ");
                    sb.append(exc.getMessage());
                    Log.e(access$000, sb.toString(), exc);
                    super.onNetworkError(exc);
                    sleepAndUnblock();
                }

                public void onMatrixError(MatrixError matrixError) {
                    super.onMatrixError(matrixError);
                    if (MatrixError.isConfigurationErrorCode(matrixError.errcode)) {
                        EventsThread.this.mListener.onConfigurationError(matrixError.errcode);
                        return;
                    }
                    EventsThread.this.mListener.onSyncError(matrixError);
                    sleepAndUnblock();
                }

                public void onUnexpectedError(Exception exc) {
                    super.onUnexpectedError(exc);
                    String access$000 = EventsThread.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Sync V2 onUnexpectedError ");
                    sb.append(exc.getMessage());
                    Log.e(access$000, sb.toString(), exc);
                    sleepAndUnblock();
                }
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, "Interrupted whilst performing initial sync.", e);
            } catch (Exception e2) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("latch.await() failed ");
                sb.append(e2.getMessage());
                Log.e(str, sb.toString(), e2);
            }
        }
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        MetricsListener metricsListener = this.mMetricsListener;
        if (metricsListener != null) {
            metricsListener.onInitialSyncFinished(currentTimeMillis2);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:56:0x0122  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x01a0 A[SYNTHETIC] */
    private void startSync() {
        int i;
        final int i2;
        this.mPaused = false;
        if (isInitialSyncDone()) {
            resumeInitialSync();
            i = 0;
        } else {
            executeInitialSync();
            i = this.mNextServerTimeoutms;
        }
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Starting event stream from token ");
        sb.append(this.mCurrentToken);
        Log.d(str, sb.toString());
        NetworkConnectivityReceiver networkConnectivityReceiver = this.mNetworkConnectivityReceiver;
        if (networkConnectivityReceiver != null) {
            networkConnectivityReceiver.addEventListener(this.mNetworkListener);
            this.mbIsConnected = this.mNetworkConnectivityReceiver.isConnected();
            this.mIsNetworkSuspended = !this.mbIsConnected;
        }
        while (!this.mKilling) {
            if (!this.mPaused && !this.mIsNetworkSuspended && this.mRequestDelayMs != 0) {
                Log.d(LOG_TAG, "startSync : start a delay timer ");
                Intent intent = new Intent(this.mContext, SyncDelayReceiver.class);
                intent.putExtra(SyncDelayReceiver.EXTRA_INSTANCE_ID, toString());
                this.mPendingDelayedIntent = PendingIntent.getBroadcast(this.mContext, 0, intent, 134217728);
                long elapsedRealtime = SystemClock.elapsedRealtime() + ((long) this.mRequestDelayMs);
                if (VERSION.SDK_INT < 23 || !this.mPowerManager.isIgnoringBatteryOptimizations(this.mContext.getPackageName())) {
                    this.mAlarmManager.set(2, elapsedRealtime, this.mPendingDelayedIntent);
                } else {
                    this.mAlarmManager.setAndAllowWhileIdle(2, elapsedRealtime, this.mPendingDelayedIntent);
                }
            }
            String str2 = null;
            if (this.mPaused || this.mIsNetworkSuspended || this.mPendingDelayedIntent != null) {
                if (this.mPendingDelayedIntent != null) {
                    Log.d(LOG_TAG, "Event stream is paused because there is a timer delay.");
                } else if (this.mIsNetworkSuspended) {
                    Log.d(LOG_TAG, "Event stream is paused because there is no available network.");
                } else {
                    Log.d(LOG_TAG, "Event stream is paused. Waiting.");
                }
                try {
                    Log.d(LOG_TAG, "startSync : wait ...");
                    synchronized (this.mSyncObject) {
                        this.mSyncObject.wait();
                    }
                    if (this.mPendingDelayedIntent != null) {
                        Log.d(LOG_TAG, "startSync : cancel mSyncDelayTimer");
                        this.mAlarmManager.cancel(this.mPendingDelayedIntent);
                        this.mPendingDelayedIntent.cancel();
                        this.mPendingDelayedIntent = null;
                    }
                    Log.d(LOG_TAG, "Event stream woken from pause.");
                    i2 = 0;
                } catch (InterruptedException e) {
                    String str3 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Unexpected interruption while paused: ");
                    sb2.append(e.getMessage());
                    Log.e(str3, sb2.toString(), e);
                }
                if (this.mKilling) {
                    long currentTimeMillis = System.currentTimeMillis();
                    final CountDownLatch countDownLatch = new CountDownLatch(1);
                    String str4 = LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Get events from token ");
                    sb3.append(this.mCurrentToken);
                    sb3.append(" with filterOrFilterId ");
                    sb3.append(this.mFilterOrFilterId);
                    Log.d(str4, sb3.toString());
                    this.mNextServerTimeoutms = this.mDefaultServerTimeoutms;
                    EventsRestClient eventsRestClient = this.mEventsRestClient;
                    String str5 = this.mCurrentToken;
                    if (!this.mIsOnline) {
                        str2 = User.PRESENCE_OFFLINE;
                    }
                    eventsRestClient.syncFromToken(str5, i2, 120000, str2, this.mFilterOrFilterId, new SimpleApiCallback<SyncResponse>(this.mFailureCallback) {
                        public void onSuccess(SyncResponse syncResponse) {
                            int i;
                            if (!EventsThread.this.mKilling) {
                                boolean z = false;
                                if (i2 == 0 && EventsThread.hasDevicesChanged(syncResponse)) {
                                    if (EventsThread.this.mIsCatchingUp) {
                                        Log.d(EventsThread.LOG_TAG, "Some devices have changed but do not set mNextServerTimeoutms to 0 to avoid infinite loops");
                                    } else {
                                        String access$000 = EventsThread.LOG_TAG;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("mNextServerTimeoutms is set to 0 because of hasDevicesChanged ");
                                        sb.append(syncResponse.deviceLists.changed);
                                        Log.d(access$000, sb.toString());
                                        EventsThread.this.mNextServerTimeoutms = 0;
                                    }
                                }
                                if (EventsThread.this.mIsCatchingUp && EventsThread.this.mNextServerTimeoutms != 0) {
                                    if (syncResponse.rooms != null) {
                                        RoomsSyncResponse roomsSyncResponse = syncResponse.rooms;
                                        int size = roomsSyncResponse.join != null ? roomsSyncResponse.join.size() + 0 : 0;
                                        i = roomsSyncResponse.invite != null ? roomsSyncResponse.invite.size() + size : size;
                                    } else {
                                        i = 0;
                                    }
                                    EventsThread.this.mIsCatchingUp = false;
                                    EventsThread eventsThread = EventsThread.this;
                                    eventsThread.mPaused = eventsThread.mRequestDelayMs == 0;
                                    String access$0002 = EventsThread.LOG_TAG;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("Got ");
                                    sb2.append(i);
                                    sb2.append(" useful events while catching up : mPaused is set to ");
                                    sb2.append(EventsThread.this.mPaused);
                                    Log.d(access$0002, sb2.toString());
                                }
                                Log.d(EventsThread.LOG_TAG, "Got event response");
                                EventsThreadListener access$1100 = EventsThread.this.mListener;
                                String access$1200 = EventsThread.this.mCurrentToken;
                                if (EventsThread.this.mNextServerTimeoutms == 0) {
                                    z = true;
                                }
                                access$1100.onSyncResponse(syncResponse, access$1200, z);
                                EventsThread.this.mCurrentToken = syncResponse.nextBatch;
                                String access$0003 = EventsThread.LOG_TAG;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("mCurrentToken is now set to ");
                                sb3.append(EventsThread.this.mCurrentToken);
                                Log.d(access$0003, sb3.toString());
                            }
                            countDownLatch.countDown();
                        }

                        private void onError(String str) {
                            boolean access$100;
                            String access$000 = EventsThread.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("Got an error while polling events ");
                            sb.append(str);
                            Log.d(access$000, sb.toString());
                            synchronized (EventsThread.this.mSyncObject) {
                                access$100 = EventsThread.this.mbIsConnected;
                            }
                            if (access$100) {
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    public void run() {
                                        countDownLatch.countDown();
                                    }
                                }, 10000);
                                return;
                            }
                            EventsThread.this.mIsNetworkSuspended = true;
                            countDownLatch.countDown();
                        }

                        public void onNetworkError(Exception exc) {
                            onError(exc.getLocalizedMessage());
                        }

                        public void onMatrixError(MatrixError matrixError) {
                            if (MatrixError.isConfigurationErrorCode(matrixError.errcode)) {
                                EventsThread.this.mListener.onConfigurationError(matrixError.errcode);
                                return;
                            }
                            EventsThread.this.mListener.onSyncError(matrixError);
                            onError(matrixError.getLocalizedMessage());
                        }

                        public void onUnexpectedError(Exception exc) {
                            onError(exc.getLocalizedMessage());
                        }
                    });
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e2) {
                        Log.e(LOG_TAG, "Interrupted whilst polling message", e2);
                    } catch (Exception e3) {
                        String str6 = LOG_TAG;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("latch.await() failed ");
                        sb4.append(e3.getMessage());
                        Log.e(str6, sb4.toString(), e3);
                    }
                    long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                    MetricsListener metricsListener = this.mMetricsListener;
                    if (metricsListener != null) {
                        metricsListener.onIncrementalSyncFinished(currentTimeMillis2);
                    }
                }
                i = this.mNextServerTimeoutms;
            }
            i2 = i;
            if (this.mKilling) {
            }
            i = this.mNextServerTimeoutms;
        }
        NetworkConnectivityReceiver networkConnectivityReceiver2 = this.mNetworkConnectivityReceiver;
        if (networkConnectivityReceiver2 != null) {
            networkConnectivityReceiver2.removeEventListener(this.mNetworkListener);
        }
        Log.d(LOG_TAG, "Event stream terminating.");
    }

    /* access modifiers changed from: private */
    public boolean isInitialSyncDone() {
        return this.mCurrentToken != null;
    }
}
