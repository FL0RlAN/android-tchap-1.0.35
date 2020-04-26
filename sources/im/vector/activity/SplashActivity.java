package im.vector.activity;

import android.content.Intent;
import android.os.Bundle;
import fr.gouv.tchap.a.R;
import im.vector.ErrorListener;
import im.vector.Matrix;
import im.vector.VectorApp;
import im.vector.analytics.TrackingEvent.LaunchScreen;
import im.vector.push.PushManager;
import im.vector.receiver.VectorUniversalLinkReceiver;
import im.vector.services.EventStreamService;
import im.vector.services.EventStreamService.StreamAction;
import im.vector.ui.themes.ActivityOtherThemes;
import im.vector.ui.themes.ActivityOtherThemes.NoActionBar;
import im.vector.util.PreferencesManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.listeners.IMXEventListener;
import org.matrix.androidsdk.listeners.MXEventListener;

public class SplashActivity extends MXCActionBarActivity {
    public static final String EXTRA_MATRIX_ID = "EXTRA_MATRIX_ID";
    public static final String EXTRA_ROOM_ID = "EXTRA_ROOM_ID";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = SplashActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public Map<MXSession, IMXEventListener> mDoneListeners = new HashMap();
    private final long mLaunchTime = System.currentTimeMillis();
    /* access modifiers changed from: private */
    public Map<MXSession, IMXEventListener> mListeners = new HashMap();

    public int getLayoutRes() {
        return R.layout.vector_activity_splash;
    }

    private boolean hasCorruptedStore() {
        boolean z = false;
        for (MXSession mXSession : Matrix.getMXSessions(this)) {
            if (mXSession.isAlive()) {
                z |= mXSession.getDataHandler().getStore().isCorrupted();
            }
        }
        return z;
    }

    /* access modifiers changed from: private */
    public void onFinish() {
        Log.e(LOG_TAG, "##onFinish() : start VectorHomeActivity");
        VectorApp.getInstance().getAnalytics().trackEvent(new LaunchScreen(System.currentTimeMillis() - this.mLaunchTime));
        if (!hasCorruptedStore()) {
            Intent intent = new Intent(this, VectorHomeActivity.class);
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                intent.putExtras(extras);
            }
            if (intent.hasExtra(VectorUniversalLinkReceiver.EXTRA_UNIVERSAL_LINK_URI)) {
                intent.putExtra(VectorHomeActivity.EXTRA_WAITING_VIEW_STATUS, true);
            }
            Intent intent2 = getIntent();
            String str = VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS;
            if (intent2.hasExtra(str)) {
                intent.putExtra(str, (Intent) getIntent().getParcelableExtra(str));
                getIntent().removeExtra(str);
            }
            String str2 = "EXTRA_ROOM_ID";
            if (getIntent().hasExtra(str2)) {
                String str3 = "EXTRA_MATRIX_ID";
                if (getIntent().hasExtra(str3)) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", getIntent().getStringExtra(str3));
                    hashMap.put(str2, getIntent().getStringExtra(str2));
                    intent.putExtra(VectorHomeActivity.EXTRA_JUMP_TO_ROOM_PARAMS, hashMap);
                }
            }
            startActivity(intent);
            finish();
            return;
        }
        Log.e(LOG_TAG, "##onFinish(): corrupted store");
        CommonActivityUtils.logout(this);
    }

    public ActivityOtherThemes getOtherThemes() {
        return NoActionBar.INSTANCE;
    }

    public void initUiAndData() {
        setRequestedOrientation(1);
        List sessions = Matrix.getInstance(getApplicationContext()).getSessions();
        if (sessions == null) {
            Log.e(LOG_TAG, "onCreate no Sessions");
            finish();
            return;
        }
        checkLazyLoadingStatus(sessions);
    }

    private void checkLazyLoadingStatus(final List<MXSession> list) {
        if (list.size() != 1) {
            startEventStreamService(list);
        }
        if (PreferencesManager.useLazyLoading(this)) {
            startEventStreamService(list);
        } else if (PreferencesManager.hasUserRefusedLazyLoading(this)) {
            startEventStreamService(list);
        } else {
            ((MXSession) list.get(0)).canEnableLazyLoading(new ApiCallback<Boolean>() {
                public void onNetworkError(Exception exc) {
                    SplashActivity.this.startEventStreamService(list);
                }

                public void onMatrixError(MatrixError matrixError) {
                    SplashActivity.this.startEventStreamService(list);
                }

                public void onUnexpectedError(Exception exc) {
                    SplashActivity.this.startEventStreamService(list);
                }

                public void onSuccess(Boolean bool) {
                    if (bool.booleanValue()) {
                        PreferencesManager.setUseLazyLoading(SplashActivity.this, true);
                        Matrix.getInstance(SplashActivity.this).reloadSessions(SplashActivity.this);
                        return;
                    }
                    SplashActivity.this.startEventStreamService(list);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void startEventStreamService(Collection<MXSession> collection) {
        ArrayList arrayList = new ArrayList();
        for (final MXSession mXSession : collection) {
            AnonymousClass2 r3 = new MXEventListener(mXSession) {
                final /* synthetic */ MXSession val$fSession;

                {
                    this.val$fSession = r2;
                }

                private void onReady() {
                    boolean containsKey;
                    synchronized (SplashActivity.LOG_TAG) {
                        containsKey = SplashActivity.this.mDoneListeners.containsKey(this.val$fSession);
                    }
                    if (!containsKey) {
                        synchronized (SplashActivity.LOG_TAG) {
                            String access$100 = SplashActivity.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("Session ");
                            sb.append(this.val$fSession.getCredentials().userId);
                            sb.append(" is initialized");
                            Log.e(access$100, sb.toString());
                            SplashActivity.this.mDoneListeners.put(this.val$fSession, SplashActivity.this.mListeners.get(this.val$fSession));
                            SplashActivity.this.mListeners.remove(this.val$fSession);
                            if (SplashActivity.this.mListeners.size() == 0) {
                                VectorApp.addSyncingSession(mXSession);
                                SplashActivity.this.onFinish();
                            }
                        }
                    }
                }

                public void onLiveEventsChunkProcessed(String str, String str2) {
                    onReady();
                }

                public void onInitialSyncComplete(String str) {
                    onReady();
                }
            };
            if (!mXSession.getDataHandler().isInitialSyncComplete()) {
                mXSession.getDataHandler().getStore().open();
                this.mListeners.put(mXSession, r3);
                mXSession.getDataHandler().addListener(r3);
                mXSession.setFailureCallback(new ErrorListener(mXSession, this));
                arrayList.add(mXSession.getCredentials().userId);
            }
        }
        boolean z = false;
        if (Matrix.getInstance(this).mHasBeenDisconnected) {
            arrayList = new ArrayList();
            for (MXSession credentials : collection) {
                arrayList.add(credentials.getCredentials().userId);
            }
            Matrix.getInstance(this).mHasBeenDisconnected = false;
        }
        if (EventStreamService.getInstance() == null) {
            Intent intent = new Intent(this, EventStreamService.class);
            intent.putExtra(EventStreamService.EXTRA_MATRIX_IDS, (String[]) arrayList.toArray(new String[arrayList.size()]));
            intent.putExtra(EventStreamService.EXTRA_STREAM_ACTION, StreamAction.START.ordinal());
            startService(intent);
        } else {
            EventStreamService.getInstance().startAccounts(arrayList);
        }
        PushManager pushManager = Matrix.getInstance(getApplicationContext()).getPushManager();
        if (!pushManager.isFcmRegistered()) {
            pushManager.checkRegistrations();
        } else {
            pushManager.forceSessionsRegistration(null);
        }
        synchronized (LOG_TAG) {
            if (this.mListeners.size() == 0) {
                z = true;
            }
        }
        if (z) {
            Log.e(LOG_TAG, "nothing to do");
            onFinish();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        for (MXSession mXSession : this.mDoneListeners.keySet()) {
            if (mXSession.isAlive()) {
                mXSession.getDataHandler().removeListener((IMXEventListener) this.mDoneListeners.get(mXSession));
                mXSession.setFailureCallback(null);
            }
        }
    }
}
