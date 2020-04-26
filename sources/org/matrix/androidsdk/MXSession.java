package org.matrix.androidsdk;

import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.format.Formatter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.cli.HelpFormatter;
import org.matrix.androidsdk.call.MXCallsManager;
import org.matrix.androidsdk.core.BingRulesManager;
import org.matrix.androidsdk.core.ContentManager;
import org.matrix.androidsdk.core.FileContentUtils;
import org.matrix.androidsdk.core.FilterUtil;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.UnsentEventsManager;
import org.matrix.androidsdk.core.VersionsUtil;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.ApiFailureCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.MXCrypto;
import org.matrix.androidsdk.crypto.MXCryptoConfig;
import org.matrix.androidsdk.crypto.cryptostore.IMXCryptoStore;
import org.matrix.androidsdk.crypto.cryptostore.MXFileCryptoStore;
import org.matrix.androidsdk.crypto.cryptostore.db.RealmCryptoStore;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.internal.MXCryptoImpl;
import org.matrix.androidsdk.crypto.model.rest.DeleteDeviceAuth;
import org.matrix.androidsdk.crypto.model.rest.DeleteDeviceParams;
import org.matrix.androidsdk.crypto.model.rest.DevicesListResponse;
import org.matrix.androidsdk.crypto.rest.CryptoRestClient;
import org.matrix.androidsdk.crypto.rest.RoomKeysRestClient;
import org.matrix.androidsdk.data.DataRetriever;
import org.matrix.androidsdk.data.MyUser;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.data.RoomTag;
import org.matrix.androidsdk.data.comparator.RoomComparatorWithTag;
import org.matrix.androidsdk.data.metrics.MetricsListener;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.data.store.MXStoreListener;
import org.matrix.androidsdk.db.MXLatestChatMessageCache;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.features.terms.TermsManager;
import org.matrix.androidsdk.groups.GroupsManager;
import org.matrix.androidsdk.network.NetworkConnectivityReceiver;
import org.matrix.androidsdk.rest.client.AccountDataRestClient;
import org.matrix.androidsdk.rest.client.CallRestClient;
import org.matrix.androidsdk.rest.client.EventsRestClient;
import org.matrix.androidsdk.rest.client.FilterRestClient;
import org.matrix.androidsdk.rest.client.GroupsRestClient;
import org.matrix.androidsdk.rest.client.LoginRestClient;
import org.matrix.androidsdk.rest.client.MediaScanRestClient;
import org.matrix.androidsdk.rest.client.PresenceRestClient;
import org.matrix.androidsdk.rest.client.ProfileRestClient;
import org.matrix.androidsdk.rest.client.PushRulesRestClient;
import org.matrix.androidsdk.rest.client.PushersRestClient;
import org.matrix.androidsdk.rest.client.RoomsRestClient;
import org.matrix.androidsdk.rest.client.ThirdPidRestClient;
import org.matrix.androidsdk.rest.model.CreateRoomParams;
import org.matrix.androidsdk.rest.model.CreateRoomResponse;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.ReceiptData;
import org.matrix.androidsdk.rest.model.RoomDirectoryVisibility;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.Versions;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;
import org.matrix.androidsdk.rest.model.filter.FilterBody;
import org.matrix.androidsdk.rest.model.filter.FilterResponse;
import org.matrix.androidsdk.rest.model.filter.RoomEventFilter;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.matrix.androidsdk.rest.model.login.LoginFlow;
import org.matrix.androidsdk.rest.model.login.RegistrationFlowResponse;
import org.matrix.androidsdk.rest.model.login.ThreePidCredentials;
import org.matrix.androidsdk.rest.model.login.TokenRefreshResponse;
import org.matrix.androidsdk.rest.model.message.MediaMessage;
import org.matrix.androidsdk.rest.model.search.SearchResponse;
import org.matrix.androidsdk.rest.model.search.SearchUsersResponse;
import org.matrix.androidsdk.rest.model.sync.AccountDataElement;
import org.matrix.androidsdk.rest.model.sync.RoomResponse;
import org.matrix.androidsdk.sync.DefaultEventsThreadListener;
import org.matrix.androidsdk.sync.EventsThread;
import org.matrix.androidsdk.sync.EventsThreadListener;
import org.matrix.olm.OlmManager;

public class MXSession implements CryptoSession {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = MXSession.class.getSimpleName();
    public static OlmManager mOlmManager = new OlmManager();
    private static MXCryptoConfig sCryptoConfig;
    private final AccountDataRestClient mAccountDataRestClient;
    private BingRulesManager mBingRulesManager;
    private final CallRestClient mCallRestClient;
    public MXCallsManager mCallsManager;
    private ContentManager mContentManager;
    private Context mContext;
    /* access modifiers changed from: private */
    public final Credentials mCredentials;
    /* access modifiers changed from: private */
    public MXCrypto mCrypto;
    /* access modifiers changed from: private */
    public IMXCryptoStore mCryptoStore;
    /* access modifiers changed from: private */
    public FilterBody mCurrentFilter;
    /* access modifiers changed from: private */
    public MXDataHandler mDataHandler;
    private DataRetriever mDataRetriever;
    /* access modifiers changed from: private */
    public boolean mEnableCryptoWhenStartingMXSession;
    private EventsRestClient mEventsRestClient;
    /* access modifiers changed from: private */
    public EventsThread mEventsThread;
    private ApiFailureCallback mFailureCallback;
    private final FilterRestClient mFilterRestClient;
    private GroupsManager mGroupsManager;
    private final GroupsRestClient mGroupsRestClient;
    private final HomeServerConnectionConfig mHsConfig;
    /* access modifiers changed from: private */
    public boolean mIsAliveSession;
    private boolean mIsBgCatchupPending;
    private boolean mIsOnline;
    private MXLatestChatMessageCache mLatestChatMessageCache;
    private final LoginRestClient mLoginRestClient;
    private MXMediaCache mMediaCache;
    private final MediaScanRestClient mMediaScanRestClient;
    private MetricsListener mMetricsListener;
    private NetworkConnectivityReceiver mNetworkConnectivityReceiver;
    private PresenceRestClient mPresenceRestClient;
    private ProfileRestClient mProfileRestClient;
    private final PushRulesRestClient mPushRulesRestClient;
    private PushersRestClient mPushersRestClient;
    private RoomsRestClient mRoomsRestClient;
    private int mSyncDelay;
    private int mSyncTimeout;
    private final ThirdPidRestClient mThirdPidRestClient;
    private UnsentEventsManager mUnsentEventsManager;
    private boolean mUseDataSaveMode;
    private TermsManager termsManager;

    public static class Builder {
        private final Context mContext;
        private final MXDataHandler mDataHandler;
        private boolean mEnableFileEncryption;
        private final HomeServerConnectionConfig mHsConfig;
        private MetricsListener mMetricsListener;
        private String mPushServerUrl;
        private boolean mUseLegacyCryptoStore;

        public Builder(HomeServerConnectionConfig homeServerConnectionConfig, MXDataHandler mXDataHandler, Context context) {
            this.mHsConfig = homeServerConnectionConfig;
            this.mDataHandler = mXDataHandler;
            this.mContext = context;
        }

        public Builder withFileEncryption(boolean z) {
            this.mEnableFileEncryption = z;
            return this;
        }

        public Builder withLegacyCryptoStore(boolean z) {
            this.mUseLegacyCryptoStore = z;
            return this;
        }

        public Builder withPushServerUrl(String str) {
            this.mPushServerUrl = str;
            return this;
        }

        public Builder withMetricsListener(MetricsListener metricsListener) {
            this.mMetricsListener = metricsListener;
            return this;
        }

        public MXSession build() {
            MXSession mXSession = new MXSession(this.mHsConfig, this.mDataHandler, this.mContext, this.mPushServerUrl, this.mEnableFileEncryption, this.mUseLegacyCryptoStore, this.mMetricsListener);
            return mXSession;
        }
    }

    private MXSession(HomeServerConnectionConfig homeServerConnectionConfig, String str) {
        this.mBingRulesManager = null;
        this.mIsAliveSession = true;
        this.mIsOnline = false;
        this.mSyncTimeout = 0;
        this.mSyncDelay = 0;
        this.mIsBgCatchupPending = false;
        this.mCurrentFilter = new FilterBody();
        this.mEnableCryptoWhenStartingMXSession = false;
        this.mCredentials = homeServerConnectionConfig.getCredentials();
        this.mHsConfig = homeServerConnectionConfig;
        this.mEventsRestClient = new EventsRestClient(homeServerConnectionConfig);
        this.mProfileRestClient = new ProfileRestClient(homeServerConnectionConfig);
        this.mPresenceRestClient = new PresenceRestClient(homeServerConnectionConfig);
        this.mRoomsRestClient = new RoomsRestClient(homeServerConnectionConfig);
        this.mPushRulesRestClient = new PushRulesRestClient(homeServerConnectionConfig);
        if (!TextUtils.isEmpty(str)) {
            try {
                this.mPushersRestClient = new PushersRestClient(new org.matrix.androidsdk.HomeServerConnectionConfig.Builder(homeServerConnectionConfig).withHomeServerUri(Uri.parse(str)).build());
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## withPushServerUrl() failed ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
        if (this.mPushersRestClient == null) {
            this.mPushersRestClient = new PushersRestClient(homeServerConnectionConfig);
        }
        this.mThirdPidRestClient = new ThirdPidRestClient(homeServerConnectionConfig);
        this.mCallRestClient = new CallRestClient(homeServerConnectionConfig);
        this.mAccountDataRestClient = new AccountDataRestClient(homeServerConnectionConfig);
        this.mLoginRestClient = new LoginRestClient(homeServerConnectionConfig);
        this.mGroupsRestClient = new GroupsRestClient(homeServerConnectionConfig);
        this.mMediaScanRestClient = new MediaScanRestClient(homeServerConnectionConfig);
        this.mFilterRestClient = new FilterRestClient(homeServerConnectionConfig);
    }

    private MXSession(HomeServerConnectionConfig homeServerConnectionConfig, MXDataHandler mXDataHandler, Context context, String str, boolean z, boolean z2, MetricsListener metricsListener) {
        this(homeServerConnectionConfig, str);
        this.mDataHandler = mXDataHandler;
        this.mContext = context;
        this.mMetricsListener = metricsListener;
        this.mCryptoStore = z2 ? new MXFileCryptoStore(z) : new RealmCryptoStore(z);
        this.mCryptoStore.initWithCredentials(this.mContext, this.mCredentials);
        this.mDataHandler.getStore().addMXStoreListener(new MXStoreListener() {
            public void onStoreReady(String str) {
                Log.d(MXSession.LOG_TAG, "## onStoreReady()");
                MXSession.this.getDataHandler().onStoreReady();
            }

            public void onStoreCorrupted(String str, String str2) {
                String access$000 = MXSession.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## onStoreCorrupted() : token ");
                sb.append(MXSession.this.getDataHandler().getStore().getEventStreamToken());
                Log.d(access$000, sb.toString());
                if (MXSession.this.getDataHandler().getStore().getEventStreamToken() == null) {
                    MXSession.this.getDataHandler().onStoreReady();
                }
            }

            public void postProcess(String str) {
                MXSession.this.getDataHandler().checkPermanentStorageData();
                if (MXSession.this.mCrypto != null) {
                    Log.e(MXSession.LOG_TAG, "## postProcess() : mCrypto is already created");
                } else if (MXSession.this.mCryptoStore.hasData() || MXSession.this.mEnableCryptoWhenStartingMXSession) {
                    String access$000 = MXSession.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## postProcess() : create the crypto instance for session ");
                    sb.append(this);
                    Log.d(access$000, sb.toString());
                    MXSession.this.checkCrypto();
                } else {
                    Log.e(MXSession.LOG_TAG, "## postProcess() : no crypto data");
                }
            }

            public void onReadReceiptsLoaded(String str) {
                List<ReceiptData> eventReceipts = MXSession.this.mDataHandler.getStore().getEventReceipts(str, null, false, false);
                ArrayList arrayList = new ArrayList();
                for (ReceiptData receiptData : eventReceipts) {
                    arrayList.add(receiptData.userId);
                }
                MXSession.this.mDataHandler.onReceiptEvent(str, arrayList);
            }
        });
        this.mDataRetriever = new DataRetriever();
        this.mDataRetriever.setRoomsRestClient(this.mRoomsRestClient);
        this.mDataHandler.setDataRetriever(this.mDataRetriever);
        this.mDataHandler.setProfileRestClient(this.mProfileRestClient);
        this.mDataHandler.setPresenceRestClient(this.mPresenceRestClient);
        this.mDataHandler.setThirdPidRestClient(this.mThirdPidRestClient);
        this.mDataHandler.setRoomsRestClient(this.mRoomsRestClient);
        this.mDataHandler.setEventsRestClient(this.mEventsRestClient);
        this.mDataHandler.setAccountDataRestClient(this.mAccountDataRestClient);
        this.mNetworkConnectivityReceiver = new NetworkConnectivityReceiver();
        this.mNetworkConnectivityReceiver.checkNetworkConnection(context);
        this.mDataHandler.setNetworkConnectivityReceiver(this.mNetworkConnectivityReceiver);
        this.mContext.registerReceiver(this.mNetworkConnectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        this.mBingRulesManager = new BingRulesManager(this, this.mNetworkConnectivityReceiver);
        this.mDataHandler.setPushRulesManager(this.mBingRulesManager);
        this.mUnsentEventsManager = new UnsentEventsManager(this.mNetworkConnectivityReceiver, this.mDataHandler);
        this.mContentManager = new ContentManager(homeServerConnectionConfig, this.mUnsentEventsManager);
        this.mCallsManager = new MXCallsManager(this, this.mContext);
        this.mDataHandler.setCallsManager(this.mCallsManager);
        this.mEventsRestClient.setUnsentEventsManager(this.mUnsentEventsManager);
        this.mProfileRestClient.setUnsentEventsManager(this.mUnsentEventsManager);
        this.mPresenceRestClient.setUnsentEventsManager(this.mUnsentEventsManager);
        this.mRoomsRestClient.setUnsentEventsManager(this.mUnsentEventsManager);
        this.mPushRulesRestClient.setUnsentEventsManager(this.mUnsentEventsManager);
        this.mThirdPidRestClient.setUnsentEventsManager(this.mUnsentEventsManager);
        this.mCallRestClient.setUnsentEventsManager(this.mUnsentEventsManager);
        this.mAccountDataRestClient.setUnsentEventsManager(this.mUnsentEventsManager);
        this.mLoginRestClient.setUnsentEventsManager(this.mUnsentEventsManager);
        this.mGroupsRestClient.setUnsentEventsManager(this.mUnsentEventsManager);
        this.mLatestChatMessageCache = new MXLatestChatMessageCache(this.mCredentials.userId);
        this.mMediaCache = new MXMediaCache(this.mContentManager, this.mNetworkConnectivityReceiver, this.mCredentials.userId, context);
        this.mDataHandler.setMediaCache(this.mMediaCache);
        this.mMediaScanRestClient.setMxStore(this.mDataHandler.getStore());
        this.mMediaCache.setMediaScanRestClient(this.mMediaScanRestClient);
        this.mGroupsManager = new GroupsManager(this.mDataHandler, this.mGroupsRestClient);
        this.mDataHandler.setGroupsManager(this.mGroupsManager);
        this.termsManager = new TermsManager(this);
    }

    private void checkIfAlive() {
        synchronized (this) {
            if (!this.mIsAliveSession) {
                Log.e(LOG_TAG, "Use of a released session", new Exception("Use of a released session"));
            }
        }
    }

    public static void initUserAgent(Context context, String str) {
        if (str == null) {
            str = "SDKApp";
        }
        RestClient.initUserAgent(context, BuildConfig.VERSION_NAME, str);
    }

    public String getVersion(boolean z) {
        checkIfAlive();
        String str = BuildConfig.VERSION_NAME;
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String string = this.mContext.getString(R.string.git_sdk_revision);
        String str2 = ")";
        String str3 = " (";
        if (z) {
            String string2 = this.mContext.getString(R.string.git_sdk_revision_date);
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(str3);
            sb.append(string);
            sb.append(HelpFormatter.DEFAULT_OPT_PREFIX);
            sb.append(string2);
            sb.append(str2);
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(str3);
        sb2.append(string);
        sb2.append(str2);
        return sb2.toString();
    }

    public String getCryptoVersion(Context context, boolean z) {
        OlmManager olmManager = mOlmManager;
        if (olmManager != null) {
            return z ? olmManager.getDetailedVersion(context) : olmManager.getVersion();
        }
        return "";
    }

    public MXDataHandler getDataHandler() {
        checkIfAlive();
        return this.mDataHandler;
    }

    public Credentials getCredentials() {
        checkIfAlive();
        return this.mCredentials;
    }

    public EventsRestClient getEventsApiClient() {
        checkIfAlive();
        return this.mEventsRestClient;
    }

    public ProfileRestClient getProfileApiClient() {
        checkIfAlive();
        return this.mProfileRestClient;
    }

    public PresenceRestClient getPresenceApiClient() {
        checkIfAlive();
        return this.mPresenceRestClient;
    }

    public FilterRestClient getFilterRestClient() {
        checkIfAlive();
        return this.mFilterRestClient;
    }

    public RoomKeysRestClient getRoomKeysRestClient() {
        checkIfAlive();
        return this.mCrypto.getKeysBackup().getRoomKeysRestClient();
    }

    public TermsManager getTermsManager() {
        checkIfAlive();
        return this.termsManager;
    }

    public void refreshUserPresence(final String str, final ApiCallback<Void> apiCallback) {
        this.mPresenceRestClient.getPresence(str, new SimpleApiCallback<User>(apiCallback) {
            public void onSuccess(User user) {
                User user2 = MXSession.this.mDataHandler.getStore().getUser(str);
                if (user2 != null) {
                    user2.presence = user.presence;
                    user2.currently_active = user.currently_active;
                    user2.lastActiveAgo = user.lastActiveAgo;
                    user = user2;
                }
                user.setLatestPresenceTs(System.currentTimeMillis());
                MXSession.this.mDataHandler.getStore().storeUser(user);
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onSuccess(null);
                }
            }
        });
    }

    public PushRulesRestClient getBingRulesApiClient() {
        checkIfAlive();
        return this.mPushRulesRestClient;
    }

    public ThirdPidRestClient getThirdPidRestClient() {
        checkIfAlive();
        return this.mThirdPidRestClient;
    }

    public CallRestClient getCallRestClient() {
        checkIfAlive();
        return this.mCallRestClient;
    }

    public PushersRestClient getPushersRestClient() {
        checkIfAlive();
        return this.mPushersRestClient;
    }

    public CryptoRestClient getCryptoRestClient() {
        checkIfAlive();
        return this.mCrypto.getCryptoRestClient();
    }

    public CryptoRestClient getCryptoRestClientForTest() {
        checkIfAlive();
        MXCrypto mXCrypto = this.mCrypto;
        if (mXCrypto != null) {
            return mXCrypto.getCryptoRestClient();
        }
        return new CryptoRestClient(this.mHsConfig);
    }

    public HomeServerConnectionConfig getHomeServerConfig() {
        checkIfAlive();
        return this.mHsConfig;
    }

    public RoomsRestClient getRoomsApiClient() {
        checkIfAlive();
        return this.mRoomsRestClient;
    }

    public MediaScanRestClient getMediaScanRestClient() {
        checkIfAlive();
        return this.mMediaScanRestClient;
    }

    public AccountDataRestClient getAccountDataRestClient() {
        checkIfAlive();
        return this.mAccountDataRestClient;
    }

    /* access modifiers changed from: protected */
    public void setEventsApiClient(EventsRestClient eventsRestClient) {
        checkIfAlive();
        this.mEventsRestClient = eventsRestClient;
    }

    /* access modifiers changed from: protected */
    public void setProfileApiClient(ProfileRestClient profileRestClient) {
        checkIfAlive();
        this.mProfileRestClient = profileRestClient;
    }

    /* access modifiers changed from: protected */
    public void setPresenceApiClient(PresenceRestClient presenceRestClient) {
        checkIfAlive();
        this.mPresenceRestClient = presenceRestClient;
    }

    /* access modifiers changed from: protected */
    public void setRoomsApiClient(RoomsRestClient roomsRestClient) {
        checkIfAlive();
        this.mRoomsRestClient = roomsRestClient;
    }

    public MXLatestChatMessageCache getLatestChatMessageCache() {
        checkIfAlive();
        return this.mLatestChatMessageCache;
    }

    public MXMediaCache getMediaCache() {
        checkIfAlive();
        return this.mMediaCache;
    }

    public static void getApplicationSizeCaches(final Context context, final ApiCallback<Long> apiCallback) {
        AnonymousClass3 r0 = new AsyncTask<Void, Void, Long>() {
            /* access modifiers changed from: protected */
            public Long doInBackground(Void... voidArr) {
                Context context = context;
                return Long.valueOf(FileContentUtils.getDirectorySize(context, context.getApplicationContext().getFilesDir().getParentFile(), 5));
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Long l) {
                String access$000 = MXSession.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getCacheSize() : ");
                sb.append(l);
                Log.d(access$000, sb.toString());
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onSuccess(l);
                }
            }
        };
        try {
            r0.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getApplicationSizeCaches() : failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            r0.cancel(true);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onUnexpectedError(e);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void clearApplicationCaches(Context context) {
        this.mDataHandler.clear();
        try {
            this.mContext.unregisterReceiver(this.mNetworkConnectivityReceiver);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## clearApplicationCaches() : unregisterReceiver failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
        this.mNetworkConnectivityReceiver.removeListeners();
        this.mUnsentEventsManager.clear();
        this.mLatestChatMessageCache.clearCache(context);
        this.mMediaCache.clear();
        MXCrypto mXCrypto = this.mCrypto;
        if (mXCrypto != null) {
            mXCrypto.close();
        }
    }

    public void clear(Context context) {
        clear(context, null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0015, code lost:
        if (r6 != null) goto L_0x001b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0017, code lost:
        clearApplicationCaches(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001b, code lost:
        r1 = new org.matrix.androidsdk.MXSession.AnonymousClass5(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r1.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR, new java.lang.Void[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0028, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0029, code lost:
        r0 = LOG_TAG;
        r2 = new java.lang.StringBuilder();
        r2.append("## clear() failed ");
        r2.append(r5.getMessage());
        org.matrix.androidsdk.core.Log.e(r0, r2.toString(), r5);
        r1.cancel(true);
        new android.os.Handler(android.os.Looper.getMainLooper()).post(new org.matrix.androidsdk.MXSession.AnonymousClass6(r4));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0012, code lost:
        stopEventStream();
     */
    public void clear(final Context context, final ApiCallback<Void> apiCallback) {
        synchronized (this) {
            if (!this.mIsAliveSession) {
                Log.e(LOG_TAG, "## clear() was already called");
                return;
            }
            this.mIsAliveSession = false;
        }
    }

    public void removeMediaBefore(Context context, long j) {
        final HashSet hashSet = new HashSet();
        IMXStore store = getDataHandler().getStore();
        Iterator it = store.getRooms().iterator();
        while (true) {
            String str = "## removeMediaBefore() : failed ";
            if (it.hasNext()) {
                Collection<Event> roomMessages = store.getRoomMessages(((Room) it.next()).getRoomId());
                if (roomMessages != null) {
                    for (Event event : roomMessages) {
                        Object obj = null;
                        try {
                            if (TextUtils.equals(Event.EVENT_TYPE_MESSAGE, event.getType())) {
                                obj = JsonUtils.toMessage(event.getContent());
                            } else if (TextUtils.equals(Event.EVENT_TYPE_STICKER, event.getType())) {
                                obj = JsonUtils.toStickerMessage(event.getContent());
                            }
                            if (obj != null && (obj instanceof MediaMessage)) {
                                MediaMessage mediaMessage = (MediaMessage) obj;
                                if (mediaMessage.isThumbnailLocalContent()) {
                                    hashSet.add(Uri.parse(mediaMessage.getThumbnailUrl()).getPath());
                                }
                                if (mediaMessage.isLocalContent()) {
                                    hashSet.add(Uri.parse(mediaMessage.getUrl()).getPath());
                                }
                            }
                        } catch (Exception e) {
                            String str2 = LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append(str);
                            sb.append(e.getMessage());
                            Log.e(str2, sb.toString(), e);
                        }
                    }
                }
            } else {
                final long j2 = j;
                final Context context2 = context;
                AnonymousClass7 r0 = new AsyncTask<Void, Void, Void>() {
                    /* access modifiers changed from: protected */
                    public Void doInBackground(Void... voidArr) {
                        long removeMediaBefore = MXSession.this.getMediaCache().removeMediaBefore(j2, hashSet);
                        File logDirectory = Log.getLogDirectory();
                        if (logDirectory != null) {
                            File[] listFiles = logDirectory.listFiles();
                            if (listFiles != null) {
                                for (File file : listFiles) {
                                    if (FileContentUtils.getLastAccessTime(file) < j2) {
                                        removeMediaBefore += file.length();
                                        file.delete();
                                    }
                                }
                            }
                        }
                        if (0 != removeMediaBefore) {
                            String access$000 = MXSession.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## removeMediaBefore() : save ");
                            sb.append(Formatter.formatFileSize(context2, removeMediaBefore));
                            Log.d(access$000, sb.toString());
                        } else {
                            Log.d(MXSession.LOG_TAG, "## removeMediaBefore() : useless");
                        }
                        return null;
                    }
                };
                try {
                    r0.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                    return;
                } catch (Exception e2) {
                    String str3 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(e2.getMessage());
                    Log.e(str3, sb2.toString(), e2);
                    r0.cancel(true);
                    return;
                }
            }
        }
    }

    public boolean isAlive() {
        boolean z;
        synchronized (this) {
            z = this.mIsAliveSession;
        }
        return z;
    }

    public ContentManager getContentManager() {
        checkIfAlive();
        return this.mContentManager;
    }

    public MyUser getMyUser() {
        checkIfAlive();
        return this.mDataHandler.getMyUser();
    }

    public String getMyUserId() {
        checkIfAlive();
        if (this.mDataHandler.getMyUser() != null) {
            return this.mDataHandler.getMyUser().user_id;
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00c7, code lost:
        return;
     */
    public void startEventStream(EventsThreadListener eventsThreadListener, NetworkConnectivityReceiver networkConnectivityReceiver, String str) {
        checkIfAlive();
        synchronized (LOG_TAG) {
            if (this.mEventsThread != null) {
                if (!this.mEventsThread.isAlive()) {
                    this.mEventsThread = null;
                    Log.e(LOG_TAG, "startEventStream() : create a new EventsThread");
                } else {
                    this.mEventsThread.cancelKill();
                    Log.e(LOG_TAG, "Ignoring startEventStream() : Thread already created.");
                    return;
                }
            }
            if (this.mDataHandler == null) {
                Log.e(LOG_TAG, "Error starting the event stream: No data handler is defined");
                return;
            }
            Log.d(LOG_TAG, "startEventStream : create the event stream");
            if (eventsThreadListener == null) {
                eventsThreadListener = new DefaultEventsThreadListener(this.mDataHandler);
            }
            this.mEventsThread = new EventsThread(this.mContext, this.mEventsRestClient, eventsThreadListener, str);
            setSyncFilter(this.mCurrentFilter);
            this.mEventsThread.setMetricsListener(this.mMetricsListener);
            this.mEventsThread.setNetworkConnectivityReceiver(networkConnectivityReceiver);
            this.mEventsThread.setIsOnline(this.mIsOnline);
            this.mEventsThread.setServerLongPollTimeout(this.mSyncTimeout);
            this.mEventsThread.setSyncDelay(this.mSyncDelay);
            if (this.mFailureCallback != null) {
                this.mEventsThread.setFailureCallback(this.mFailureCallback);
            }
            if (this.mCredentials.accessToken != null && !this.mEventsThread.isAlive()) {
                try {
                    this.mEventsThread.start();
                } catch (Exception e) {
                    String str2 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## startEventStream() :  mEventsThread.start failed ");
                    sb.append(e.getMessage());
                    Log.e(str2, sb.toString(), e);
                }
                if (this.mIsBgCatchupPending) {
                    Log.d(LOG_TAG, "startEventStream : start a catchup");
                    this.mIsBgCatchupPending = false;
                    this.mEventsThread.catchup();
                }
            }
        }
    }

    public void refreshToken() {
        checkIfAlive();
        this.mProfileRestClient.refreshTokens(getCredentials().refreshToken, new ApiCallback<TokenRefreshResponse>() {
            public void onSuccess(TokenRefreshResponse tokenRefreshResponse) {
                Log.d(MXSession.LOG_TAG, "refreshToken : succeeds.");
            }

            public void onNetworkError(Exception exc) {
                String access$000 = MXSession.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("refreshToken : onNetworkError ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$000 = MXSession.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("refreshToken : onMatrixError ");
                sb.append(matrixError.getMessage());
                Log.e(access$000, sb.toString());
            }

            public void onUnexpectedError(Exception exc) {
                String access$000 = MXSession.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("refreshToken : onMatrixError ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
            }
        });
    }

    public void setIsOnline(boolean z) {
        if (z != this.mIsOnline) {
            this.mIsOnline = z;
            EventsThread eventsThread = this.mEventsThread;
            if (eventsThread != null) {
                eventsThread.setIsOnline(z);
            }
        }
    }

    public boolean isOnline() {
        return this.mIsOnline;
    }

    public void setSyncTimeout(int i) {
        this.mSyncTimeout = i;
        EventsThread eventsThread = this.mEventsThread;
        if (eventsThread != null) {
            eventsThread.setServerLongPollTimeout(i);
        }
    }

    public int getSyncTimeout() {
        return this.mSyncTimeout;
    }

    public void setSyncDelay(int i) {
        this.mSyncDelay = i;
        EventsThread eventsThread = this.mEventsThread;
        if (eventsThread != null) {
            eventsThread.setSyncDelay(i);
        }
    }

    public int getSyncDelay() {
        return this.mSyncDelay;
    }

    public void setUseDataSaveMode(boolean z) {
        this.mUseDataSaveMode = z;
        if (this.mEventsThread != null) {
            setSyncFilter(this.mCurrentFilter);
        }
    }

    public synchronized void setSyncFilter(FilterBody filterBody) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setSyncFilter ## ");
        sb.append(filterBody);
        Log.d(str, sb.toString());
        this.mCurrentFilter = filterBody;
        FilterUtil.enableDataSaveMode(this.mCurrentFilter, this.mUseDataSaveMode);
        FilterUtil.enableLazyLoading(this.mCurrentFilter, this.mDataHandler.isLazyLoadingEnabled());
        convertFilterToFilterId();
    }

    public void setPaginationFilter(RoomEventFilter roomEventFilter) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setPaginationFilter ## ");
        sb.append(roomEventFilter);
        Log.d(str, sb.toString());
        this.mDataHandler.setPaginationFilter(roomEventFilter);
    }

    private void convertFilterToFilterId() {
        final String jSONString = this.mCurrentFilter.toJSONString();
        String str = (String) getDataHandler().getStore().getFilters().get(jSONString);
        if (TextUtils.isEmpty(str)) {
            this.mEventsThread.setFilterOrFilterId(jSONString);
            this.mFilterRestClient.uploadFilter(getMyUserId(), this.mCurrentFilter, new SimpleApiCallback<FilterResponse>() {
                public void onSuccess(FilterResponse filterResponse) {
                    MXSession.this.getDataHandler().getStore().addFilter(jSONString, filterResponse.filterId);
                    if (TextUtils.equals(jSONString, MXSession.this.mCurrentFilter.toJSONString())) {
                        MXSession.this.mEventsThread.setFilterOrFilterId(filterResponse.filterId);
                    }
                }
            });
            return;
        }
        this.mEventsThread.setFilterOrFilterId(str);
    }

    public void refreshNetworkConnection() {
        NetworkConnectivityReceiver networkConnectivityReceiver = this.mNetworkConnectivityReceiver;
        if (networkConnectivityReceiver != null) {
            networkConnectivityReceiver.checkNetworkConnection(this.mContext);
        }
    }

    public void startEventStream(String str) {
        checkIfAlive();
        startEventStream(null, this.mNetworkConnectivityReceiver, str);
    }

    public void stopEventStream() {
        MXCallsManager mXCallsManager = this.mCallsManager;
        if (mXCallsManager != null) {
            mXCallsManager.stopTurnServerRefresh();
        }
        if (this.mEventsThread != null) {
            Log.d(LOG_TAG, "stopEventStream");
            this.mEventsThread.kill();
            this.mEventsThread = null;
            return;
        }
        Log.e(LOG_TAG, "stopEventStream : mEventsThread is already null");
    }

    public void pauseEventStream() {
        checkIfAlive();
        MXCallsManager mXCallsManager = this.mCallsManager;
        if (mXCallsManager != null) {
            mXCallsManager.pauseTurnServerRefresh();
        }
        if (this.mEventsThread != null) {
            Log.d(LOG_TAG, "pauseEventStream");
            this.mEventsThread.pause();
        } else {
            Log.e(LOG_TAG, "pauseEventStream : mEventsThread is null");
        }
        if (getMediaCache() != null) {
            getMediaCache().clearTmpDecryptedMediaCache();
        }
        GroupsManager groupsManager = this.mGroupsManager;
        if (groupsManager != null) {
            groupsManager.onSessionPaused();
        }
    }

    public String getCurrentSyncToken() {
        EventsThread eventsThread = this.mEventsThread;
        if (eventsThread != null) {
            return eventsThread.getCurrentSyncToken();
        }
        return null;
    }

    public void resumeEventStream() {
        checkIfAlive();
        NetworkConnectivityReceiver networkConnectivityReceiver = this.mNetworkConnectivityReceiver;
        if (networkConnectivityReceiver != null) {
            networkConnectivityReceiver.checkNetworkConnection(this.mContext);
        }
        MXCallsManager mXCallsManager = this.mCallsManager;
        if (mXCallsManager != null) {
            mXCallsManager.unpauseTurnServerRefresh();
        }
        if (this.mEventsThread != null) {
            Log.d(LOG_TAG, "## resumeEventStream() : unpause");
            this.mEventsThread.unpause();
        } else {
            Log.e(LOG_TAG, "resumeEventStream : mEventsThread is null");
        }
        if (this.mIsBgCatchupPending) {
            this.mIsBgCatchupPending = false;
            Log.d(LOG_TAG, "## resumeEventStream() : cancel bg sync");
        }
        if (getMediaCache() != null) {
            getMediaCache().clearShareDecryptedMediaCache();
        }
        GroupsManager groupsManager = this.mGroupsManager;
        if (groupsManager != null) {
            groupsManager.onSessionResumed();
        }
    }

    public void catchupEventStream() {
        checkIfAlive();
        if (this.mEventsThread != null) {
            Log.d(LOG_TAG, "catchupEventStream");
            this.mEventsThread.catchup();
            return;
        }
        Log.e(LOG_TAG, "catchupEventStream : mEventsThread is null so catchup when the thread will be created");
        this.mIsBgCatchupPending = true;
    }

    public void setFailureCallback(ApiFailureCallback apiFailureCallback) {
        checkIfAlive();
        this.mFailureCallback = apiFailureCallback;
        EventsThread eventsThread = this.mEventsThread;
        if (eventsThread != null) {
            eventsThread.setFailureCallback(apiFailureCallback);
        }
    }

    public void createRoom(ApiCallback<String> apiCallback) {
        createRoom(null, null, null, apiCallback);
    }

    public void createRoom(String str, String str2, String str3, ApiCallback<String> apiCallback) {
        createRoom(str, str2, RoomDirectoryVisibility.DIRECTORY_VISIBILITY_PRIVATE, str3, null, apiCallback);
    }

    public void createRoom(String str, String str2, String str3, String str4, String str5, ApiCallback<String> apiCallback) {
        checkIfAlive();
        CreateRoomParams createRoomParams = new CreateRoomParams();
        if (TextUtils.isEmpty(str)) {
            str = null;
        }
        createRoomParams.name = str;
        if (TextUtils.isEmpty(str2)) {
            str2 = null;
        }
        createRoomParams.topic = str2;
        if (TextUtils.isEmpty(str3)) {
            str3 = null;
        }
        createRoomParams.visibility = str3;
        if (TextUtils.isEmpty(str4)) {
            str4 = null;
        }
        createRoomParams.roomAliasName = str4;
        createRoomParams.addCryptoAlgorithm(str5);
        createRoom(createRoomParams, apiCallback);
    }

    public void createEncryptedRoom(String str, ApiCallback<String> apiCallback) {
        CreateRoomParams createRoomParams = new CreateRoomParams();
        createRoomParams.addCryptoAlgorithm(str);
        createRoom(createRoomParams, apiCallback);
    }

    public boolean createDirectMessageRoom(String str, ApiCallback<String> apiCallback) {
        return createDirectMessageRoom(str, null, apiCallback);
    }

    public boolean createDirectMessageRoom(String str, String str2, ApiCallback<String> apiCallback) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        CreateRoomParams createRoomParams = new CreateRoomParams();
        createRoomParams.addCryptoAlgorithm(str2);
        createRoomParams.setDirectMessage();
        createRoomParams.addParticipantIds(this.mHsConfig, Arrays.asList(new String[]{str}));
        createRoom(createRoomParams, apiCallback);
        return true;
    }

    /* access modifiers changed from: private */
    public void finalizeDMRoomCreation(final String str, String str2, final ApiCallback<String> apiCallback) {
        toggleDirectChatRoom(str, str2, new SimpleApiCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                Room room = MXSession.this.getDataHandler().getRoom(str);
                if (room != null) {
                    room.markAllAsRead(null);
                }
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onSuccess(str);
                }
            }
        });
    }

    public void createRoom(final CreateRoomParams createRoomParams, final ApiCallback<String> apiCallback) {
        this.mRoomsRestClient.createRoom(createRoomParams, new SimpleApiCallback<CreateRoomResponse>(apiCallback) {
            public void onSuccess(CreateRoomResponse createRoomResponse) {
                final String str = createRoomResponse.roomId;
                final Room room = MXSession.this.mDataHandler.getRoom(str);
                if (!room.isJoined()) {
                    room.setOnInitialSyncCallback(new SimpleApiCallback<Void>(apiCallback) {
                        public void onSuccess(Void voidR) {
                            room.markAllAsRead(null);
                            if (createRoomParams.isDirect()) {
                                MXSession.this.finalizeDMRoomCreation(str, createRoomParams.getFirstInvitedUserId(), apiCallback);
                            } else {
                                apiCallback.onSuccess(str);
                            }
                        }
                    });
                    return;
                }
                room.markAllAsRead(null);
                if (createRoomParams.isDirect()) {
                    MXSession.this.finalizeDMRoomCreation(str, createRoomParams.getFirstInvitedUserId(), apiCallback);
                } else {
                    apiCallback.onSuccess(str);
                }
            }
        });
    }

    public void joinRoom(String str, ApiCallback<String> apiCallback) {
        joinRoom(str, null, apiCallback);
    }

    public void joinRoom(String str, List<String> list, final ApiCallback<String> apiCallback) {
        checkIfAlive();
        if (this.mDataHandler != null && str != null) {
            this.mDataRetriever.getRoomsRestClient().joinRoom(str, list, null, new SimpleApiCallback<RoomResponse>(apiCallback) {
                public void onSuccess(RoomResponse roomResponse) {
                    final String str = roomResponse.roomId;
                    Room room = MXSession.this.mDataHandler.getRoom(str);
                    if (!room.isJoined()) {
                        room.setOnInitialSyncCallback(new SimpleApiCallback<Void>(apiCallback) {
                            public void onSuccess(Void voidR) {
                                apiCallback.onSuccess(str);
                            }
                        });
                        return;
                    }
                    room.markAllAsRead(null);
                    apiCallback.onSuccess(str);
                }
            });
        }
    }

    public void markRoomsAsRead(Collection<Room> collection, final ApiCallback<Void> apiCallback) {
        if (collection == null || collection.size() == 0) {
            if (apiCallback != null) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        apiCallback.onSuccess(null);
                    }
                });
            }
            return;
        }
        markRoomsAsRead(collection.iterator(), apiCallback);
    }

    /* access modifiers changed from: private */
    public void markRoomsAsRead(final Iterator it, final ApiCallback<Void> apiCallback) {
        if (it.hasNext()) {
            Room room = (Room) it.next();
            boolean z = false;
            if (this.mNetworkConnectivityReceiver.isConnected()) {
                z = room.markAllAsRead(new SimpleApiCallback<Void>(apiCallback) {
                    public void onSuccess(Void voidR) {
                        MXSession.this.markRoomsAsRead(it, apiCallback);
                    }
                });
            } else {
                room.sendReadReceipt();
            }
            if (!z) {
                markRoomsAsRead(it, apiCallback);
            }
        } else if (apiCallback != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    apiCallback.onSuccess(null);
                }
            });
        }
    }

    public void lookup3Pid(String str, String str2, ApiCallback<String> apiCallback) {
        checkIfAlive();
        this.mThirdPidRestClient.lookup3Pid(str, str2, apiCallback);
    }

    public void lookup3Pids(List<String> list, List<String> list2, ApiCallback<List<String>> apiCallback) {
        checkIfAlive();
        this.mThirdPidRestClient.lookup3Pids(list, list2, apiCallback);
    }

    public void searchMessageText(String str, List<String> list, int i, int i2, String str2, ApiCallback<SearchResponse> apiCallback) {
        checkIfAlive();
        if (apiCallback != null) {
            this.mEventsRestClient.searchMessagesByText(str, list, i, i2, str2, apiCallback);
        }
    }

    public void searchMessagesByText(String str, List<String> list, String str2, ApiCallback<SearchResponse> apiCallback) {
        checkIfAlive();
        if (apiCallback != null) {
            this.mEventsRestClient.searchMessagesByText(str, list, 0, 0, str2, apiCallback);
        }
    }

    public void searchMessagesByText(String str, String str2, ApiCallback<SearchResponse> apiCallback) {
        checkIfAlive();
        if (apiCallback != null) {
            this.mEventsRestClient.searchMessagesByText(str, null, 0, 0, str2, apiCallback);
        }
    }

    public void cancelSearchMessagesByText() {
        checkIfAlive();
        this.mEventsRestClient.cancelSearchMessagesByText();
    }

    public void searchMediaByName(String str, List<String> list, String str2, ApiCallback<SearchResponse> apiCallback) {
        checkIfAlive();
        if (apiCallback != null) {
            this.mEventsRestClient.searchMediaByText(str, list, 0, 0, str2, apiCallback);
        }
    }

    public void cancelSearchMediaByText() {
        checkIfAlive();
        this.mEventsRestClient.cancelSearchMediaByText();
    }

    public void searchUsers(String str, Integer num, Set<String> set, ApiCallback<SearchUsersResponse> apiCallback) {
        checkIfAlive();
        if (apiCallback != null) {
            this.mEventsRestClient.searchUsers(str, num, set, apiCallback);
        }
    }

    public void cancelUsersSearch() {
        checkIfAlive();
        this.mEventsRestClient.cancelUsersSearch();
    }

    public BingRule fulfillRule(Event event) {
        checkIfAlive();
        return this.mBingRulesManager.fulfilledBingRule(event);
    }

    public boolean isVoipCallSupported() {
        MXCallsManager mXCallsManager = this.mCallsManager;
        if (mXCallsManager != null) {
            return mXCallsManager.isSupported();
        }
        return false;
    }

    public List<Room> roomsWithTag(String str) {
        ArrayList arrayList = new ArrayList();
        if (this.mDataHandler.getStore() == null) {
            return arrayList;
        }
        if (!TextUtils.equals(str, RoomTag.ROOM_TAG_NO_TAG)) {
            for (Room room : this.mDataHandler.getStore().getRooms()) {
                if (room.getAccountData().roomTag(str) != null) {
                    arrayList.add(room);
                }
            }
            if (arrayList.size() > 0) {
                Collections.sort(arrayList, new RoomComparatorWithTag(str));
            }
        } else {
            for (Room room2 : this.mDataHandler.getStore().getRooms()) {
                if (!room2.getAccountData().hasTags()) {
                    arrayList.add(room2);
                }
            }
        }
        return arrayList;
    }

    public List<String> roomIdsWithTag(String str) {
        List<Room> roomsWithTag = roomsWithTag(str);
        ArrayList arrayList = new ArrayList();
        for (Room roomId : roomsWithTag) {
            arrayList.add(roomId.getRoomId());
        }
        return arrayList;
    }

    public Double tagOrderToBeAtIndex(int i, int i2, String str) {
        Double valueOf = Double.valueOf(0.0d);
        Double valueOf2 = Double.valueOf(1.0d);
        List roomsWithTag = roomsWithTag(str);
        if (roomsWithTag.size() > 0) {
            if (i2 != Integer.MAX_VALUE && i2 < i) {
                i++;
            }
            if (i > 0) {
                RoomTag roomTag = ((Room) roomsWithTag.get((i < roomsWithTag.size() ? i : roomsWithTag.size()) - 1)).getAccountData().roomTag(str);
                if (roomTag.mOrder == null) {
                    Log.e(LOG_TAG, "computeTagOrderForRoom: Previous room in sublist has no ordering metadata. This should never happen.");
                } else {
                    valueOf = roomTag.mOrder;
                }
            }
            if (i <= roomsWithTag.size() - 1) {
                RoomTag roomTag2 = ((Room) roomsWithTag.get(i)).getAccountData().roomTag(str);
                if (roomTag2.mOrder == null) {
                    Log.e(LOG_TAG, "computeTagOrderForRoom: Next room in sublist has no ordering metadata. This should never happen.");
                } else {
                    valueOf2 = roomTag2.mOrder;
                }
            }
        }
        return Double.valueOf((valueOf.doubleValue() + valueOf2.doubleValue()) / 2.0d);
    }

    public void toggleDirectChatRoom(final String str, String str2, final ApiCallback<Void> apiCallback) {
        Room room = getDataHandler().getStore().getRoom(str);
        if (room != null) {
            if (getDataHandler().getDirectChatRoomIdsList().contains(str)) {
                removeDirectChatRoomFromAccountData(str, apiCallback);
            } else if (str2 == null) {
                searchOtherUserInRoomToCreateDirectChat(room, new SimpleApiCallback<String>(apiCallback) {
                    public void onSuccess(String str) {
                        MXSession.this.addDirectChatRoomToAccountData(str, str, apiCallback);
                    }
                });
            } else {
                addDirectChatRoomToAccountData(str, str2, apiCallback);
            }
        } else if (apiCallback != null) {
            apiCallback.onUnexpectedError(new Exception("Unknown room"));
        }
    }

    private void searchOtherUserInRoomToCreateDirectChat(Room room, final ApiCallback<String> apiCallback) {
        room.getActiveMembersAsync(new SimpleApiCallback<List<RoomMember>>(apiCallback) {
            public void onSuccess(List<RoomMember> list) {
                if (list.isEmpty()) {
                    apiCallback.onUnexpectedError(new Exception("Error"));
                    return;
                }
                RoomMember roomMember = null;
                int i = 1;
                if (list.size() > 1) {
                    Collections.sort(list, new Comparator<RoomMember>() {
                        public int compare(RoomMember roomMember, RoomMember roomMember2) {
                            if ("join".equals(roomMember2.membership)) {
                                if ("invite".equals(roomMember.membership)) {
                                    return 1;
                                }
                            }
                            if (roomMember2.membership.equals(roomMember.membership)) {
                                long originServerTs = roomMember.getOriginServerTs() - roomMember2.getOriginServerTs();
                                if (0 == originServerTs) {
                                    return 0;
                                }
                                if (originServerTs > 0) {
                                    return 1;
                                }
                            }
                            return -1;
                        }
                    });
                    String str = "join";
                    if (!TextUtils.equals(((RoomMember) list.get(0)).getUserId(), MXSession.this.getMyUserId())) {
                        if (str.equals(((RoomMember) list.get(0)).membership)) {
                            roomMember = (RoomMember) list.get(0);
                        }
                        i = 0;
                    } else if (str.equals(((RoomMember) list.get(1)).membership)) {
                        roomMember = (RoomMember) list.get(1);
                    }
                    if (roomMember == null) {
                        if ("invite".equals(((RoomMember) list.get(i)).membership)) {
                            roomMember = (RoomMember) list.get(i);
                        }
                    }
                }
                if (roomMember == null) {
                    roomMember = (RoomMember) list.get(0);
                }
                apiCallback.onSuccess(roomMember.getUserId());
            }
        });
    }

    /* access modifiers changed from: private */
    public void addDirectChatRoomToAccountData(String str, String str2, ApiCallback<Void> apiCallback) {
        HashMap hashMap;
        IMXStore store = getDataHandler().getStore();
        if (store.getDirectChatRoomsDict() != null) {
            hashMap = new HashMap(store.getDirectChatRoomsDict());
        } else {
            hashMap = new HashMap();
        }
        ArrayList arrayList = new ArrayList();
        if (hashMap.containsKey(str2)) {
            arrayList = new ArrayList((Collection) hashMap.get(str2));
        }
        arrayList.add(str);
        hashMap.put(str2, arrayList);
        getDataHandler().setDirectChatRoomsMap(hashMap, apiCallback);
    }

    private void removeDirectChatRoomFromAccountData(String str, ApiCallback<Void> apiCallback) {
        HashMap hashMap;
        IMXStore store = getDataHandler().getStore();
        if (store.getDirectChatRoomsDict() != null) {
            hashMap = new HashMap(store.getDirectChatRoomsDict());
        } else {
            hashMap = new HashMap();
        }
        if (store.getDirectChatRoomsDict() != null) {
            for (String str2 : new ArrayList(hashMap.keySet())) {
                List list = (List) hashMap.get(str2);
                if (list.contains(str)) {
                    list.remove(str);
                    if (list.isEmpty()) {
                        hashMap.remove(str2);
                    }
                }
            }
            getDataHandler().setDirectChatRoomsMap(hashMap, apiCallback);
            return;
        }
        Log.e(LOG_TAG, "## removeDirectChatRoomFromAccountData(): failed to remove a direct chat room (not seen as direct chat room)");
        if (apiCallback != null) {
            apiCallback.onUnexpectedError(new Exception("Error"));
        }
    }

    public void updatePassword(String str, String str2, ApiCallback<Void> apiCallback) {
        this.mProfileRestClient.updatePassword(getMyUserId(), str, str2, apiCallback);
    }

    public void resetPassword(String str, ThreePidCredentials threePidCredentials, ApiCallback<Void> apiCallback) {
        this.mProfileRestClient.resetPassword(str, threePidCredentials, apiCallback);
    }

    private void updateUsers(List<String> list, ApiCallback<Void> apiCallback) {
        HashMap hashMap = new HashMap();
        for (String put : list) {
            hashMap.put(put, new HashMap());
        }
        HashMap hashMap2 = new HashMap();
        hashMap2.put(AccountDataElement.ACCOUNT_DATA_KEY_IGNORED_USERS, hashMap);
        this.mAccountDataRestClient.setAccountData(getMyUserId(), AccountDataElement.ACCOUNT_DATA_TYPE_IGNORED_USER_LIST, hashMap2, apiCallback);
    }

    public boolean isUserIgnored(String str) {
        return str != null && getDataHandler().getIgnoredUserIds().indexOf(str) >= 0;
    }

    public void ignoreUsers(List<String> list, ApiCallback<Void> apiCallback) {
        List ignoredUserIds = getDataHandler().getIgnoredUserIds();
        ArrayList arrayList = new ArrayList(getDataHandler().getIgnoredUserIds());
        if (list != null && list.size() > 0) {
            for (String str : list) {
                if (arrayList.indexOf(str) < 0) {
                    arrayList.add(str);
                }
            }
            if (ignoredUserIds.size() != arrayList.size()) {
                updateUsers(arrayList, apiCallback);
            }
        }
    }

    public void unIgnoreUsers(List<String> list, ApiCallback<Void> apiCallback) {
        List ignoredUserIds = getDataHandler().getIgnoredUserIds();
        ArrayList arrayList = new ArrayList(getDataHandler().getIgnoredUserIds());
        if (list != null && list.size() > 0) {
            for (String remove : list) {
                arrayList.remove(remove);
            }
            if (ignoredUserIds.size() != arrayList.size()) {
                updateUsers(arrayList, apiCallback);
            }
        }
    }

    public NetworkConnectivityReceiver getNetworkConnectivityReceiver() {
        return this.mNetworkConnectivityReceiver;
    }

    public void canEnableLazyLoading(final ApiCallback<Boolean> apiCallback) {
        this.mLoginRestClient.getVersions(new SimpleApiCallback<Versions>(apiCallback) {
            public void onSuccess(Versions versions) {
                apiCallback.onSuccess(Boolean.valueOf(VersionsUtil.supportLazyLoadMembers(versions)));
            }
        });
    }

    public void logout(final Context context, final ApiCallback<Void> apiCallback) {
        synchronized (this) {
            if (!this.mIsAliveSession) {
                Log.e(LOG_TAG, "## logout() was already called");
                return;
            }
            this.mIsAliveSession = false;
            enableCrypto(false, null);
            this.mLoginRestClient.logout(new ApiCallback<Void>() {
                private void clearData() {
                    MXSession.this.mIsAliveSession = true;
                    MXSession.this.clear(context, new SimpleApiCallback<Void>() {
                        public void onSuccess(Void voidR) {
                            if (apiCallback != null) {
                                apiCallback.onSuccess(null);
                            }
                        }
                    });
                }

                public void onSuccess(Void voidR) {
                    Log.d(MXSession.LOG_TAG, "## logout() : succeed -> clearing the application data ");
                    clearData();
                }

                private void onError(String str) {
                    String access$000 = MXSession.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## logout() : failed ");
                    sb.append(str);
                    Log.e(access$000, sb.toString());
                    clearData();
                }

                public void onNetworkError(Exception exc) {
                    onError(exc.getMessage());
                }

                public void onMatrixError(MatrixError matrixError) {
                    onError(matrixError.getMessage());
                }

                public void onUnexpectedError(Exception exc) {
                    onError(exc.getMessage());
                }
            });
        }
    }

    public void deactivateAccount(final Context context, String str, boolean z, final ApiCallback<Void> apiCallback) {
        this.mProfileRestClient.deactivateAccount(getMyUserId(), str, z, new SimpleApiCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                Log.d(MXSession.LOG_TAG, "## deactivateAccount() : succeed -> clearing the application data ");
                MXSession.this.enableCrypto(false, null);
                MXSession.this.clear(context, new SimpleApiCallback<Void>(apiCallback) {
                    public void onSuccess(Void voidR) {
                        if (apiCallback != null) {
                            apiCallback.onSuccess(null);
                        }
                    }
                });
            }
        });
    }

    public void setURLPreviewStatus(final boolean z, final ApiCallback<Void> apiCallback) {
        HashMap hashMap = new HashMap();
        hashMap.put(AccountDataElement.ACCOUNT_DATA_KEY_URL_PREVIEW_DISABLE, Boolean.valueOf(!z));
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## setURLPreviewStatus() : status ");
        sb.append(z);
        Log.d(str, sb.toString());
        this.mAccountDataRestClient.setAccountData(getMyUserId(), AccountDataElement.ACCOUNT_DATA_TYPE_PREVIEW_URLS, hashMap, new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                Log.d(MXSession.LOG_TAG, "## setURLPreviewStatus() : succeeds");
                MXSession.this.getDataHandler().getStore().setURLPreviewEnabled(z);
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onSuccess(null);
                }
            }

            public void onNetworkError(Exception exc) {
                String access$000 = MXSession.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## setURLPreviewStatus() : failed ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                apiCallback.onNetworkError(exc);
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$000 = MXSession.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## setURLPreviewStatus() : failed ");
                sb.append(matrixError.getMessage());
                Log.e(access$000, sb.toString());
                apiCallback.onMatrixError(matrixError);
            }

            public void onUnexpectedError(Exception exc) {
                String access$000 = MXSession.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## setURLPreviewStatus() : failed ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                apiCallback.onUnexpectedError(exc);
            }
        });
    }

    public void addUserWidget(final Map<String, Object> map, final ApiCallback<Void> apiCallback) {
        Log.d(LOG_TAG, "## addUserWidget()");
        this.mAccountDataRestClient.setAccountData(getMyUserId(), "m.widgets", map, new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                Log.d(MXSession.LOG_TAG, "## addUserWidget() : succeeds");
                MXSession.this.getDataHandler().getStore().setUserWidgets(map);
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onSuccess(null);
                }
            }

            public void onNetworkError(Exception exc) {
                String access$000 = MXSession.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## addUserWidget() : failed ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                apiCallback.onNetworkError(exc);
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$000 = MXSession.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## addUserWidget() : failed ");
                sb.append(matrixError.getMessage());
                Log.e(access$000, sb.toString());
                apiCallback.onMatrixError(matrixError);
            }

            public void onUnexpectedError(Exception exc) {
                String access$000 = MXSession.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## addUserWidget() : failed ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                apiCallback.onUnexpectedError(exc);
            }
        });
    }

    public boolean isURLPreviewEnabled() {
        return getDataHandler().getStore().isURLPreviewEnabled();
    }

    public Map<String, Object> getUserWidgets() {
        return getDataHandler().getStore().getUserWidgets();
    }

    public MXCrypto getCrypto() {
        return this.mCrypto;
    }

    public MXCrypto requireCrypto() {
        return this.mCrypto;
    }

    public boolean isCryptoEnabled() {
        return this.mCrypto != null;
    }

    public void enableCryptoWhenStarting() {
        this.mEnableCryptoWhenStartingMXSession = true;
    }

    public static void setCryptoConfig(MXCryptoConfig mXCryptoConfig) {
        sCryptoConfig = mXCryptoConfig;
    }

    /* access modifiers changed from: private */
    public void decryptRoomSummaries() {
        if (getDataHandler().getStore() != null) {
            for (RoomSummary latestReceivedEvent : getDataHandler().getStore().getSummaries()) {
                this.mDataHandler.decryptEvent(latestReceivedEvent.getLatestReceivedEvent(), null);
            }
        }
    }

    public void checkCrypto() {
        if ((this.mCryptoStore.hasData() || this.mEnableCryptoWhenStartingMXSession) && this.mCrypto == null) {
            boolean z = false;
            try {
                this.mCryptoStore.open();
                z = true;
            } catch (UnsatisfiedLinkError e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## checkCrypto() failed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
            if (!z) {
                mOlmManager = new OlmManager();
                try {
                    this.mCryptoStore.open();
                    z = true;
                } catch (UnsatisfiedLinkError e2) {
                    String str2 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## checkCrypto() failed 2 ");
                    sb2.append(e2.getMessage());
                    Log.e(str2, sb2.toString(), e2);
                }
            }
            if (!z) {
                Log.e(LOG_TAG, "## checkCrypto() : cannot enable the crypto because of olm lib");
                return;
            }
            this.mCrypto = new MXCryptoImpl(this, this.mCryptoStore, sCryptoConfig, this.mHsConfig);
            this.mDataHandler.setCrypto(this.mCrypto);
            decryptRoomSummaries();
            Log.d(LOG_TAG, "## checkCrypto() : the crypto engine is ready");
        } else if (this.mDataHandler.getCrypto() != this.mCrypto) {
            Log.e(LOG_TAG, "## checkCrypto() : the data handler crypto was not initialized");
            this.mDataHandler.setCrypto(this.mCrypto);
        }
    }

    public void enableCrypto(boolean z, final ApiCallback<Void> apiCallback) {
        if (z != isCryptoEnabled()) {
            if (z) {
                Log.d(LOG_TAG, "Crypto is enabled");
                this.mCryptoStore.open();
                this.mCrypto = new MXCryptoImpl(this, this.mCryptoStore, sCryptoConfig, this.mHsConfig);
                this.mCrypto.start(true, new SimpleApiCallback<Void>(apiCallback) {
                    public void onSuccess(Void voidR) {
                        MXSession.this.decryptRoomSummaries();
                        ApiCallback apiCallback = apiCallback;
                        if (apiCallback != null) {
                            apiCallback.onSuccess(null);
                        }
                    }
                });
            } else if (this.mCrypto != null) {
                Log.d(LOG_TAG, "Crypto is disabled");
                this.mCrypto.close();
                this.mCryptoStore.deleteStore();
                this.mCrypto = null;
                this.mDataHandler.setCrypto(null);
                decryptRoomSummaries();
                if (apiCallback != null) {
                    apiCallback.onSuccess(null);
                }
            }
            this.mDataHandler.setCrypto(this.mCrypto);
        } else if (apiCallback != null) {
            apiCallback.onSuccess(null);
        }
    }

    public void getDevicesList(ApiCallback<DevicesListResponse> apiCallback) {
        this.mCrypto.getCryptoRestClient().getDevices(apiCallback);
    }

    public void setDeviceName(String str, String str2, ApiCallback<Void> apiCallback) {
        this.mCrypto.getCryptoRestClient().setDeviceName(str, str2, apiCallback);
    }

    public void deleteDevice(String str, String str2, ApiCallback<Void> apiCallback) {
        CryptoRestClient cryptoRestClient = this.mCrypto.getCryptoRestClient();
        DeleteDeviceParams deleteDeviceParams = new DeleteDeviceParams();
        final ApiCallback<Void> apiCallback2 = apiCallback;
        final String str3 = str2;
        final String str4 = str;
        AnonymousClass24 r2 = new SimpleApiCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                ApiCallback apiCallback = apiCallback2;
                if (apiCallback != null) {
                    apiCallback.onSuccess(null);
                }
            }

            /* JADX WARNING: Removed duplicated region for block: B:19:0x0076  */
            /* JADX WARNING: Removed duplicated region for block: B:24:0x008c  */
            /* JADX WARNING: Removed duplicated region for block: B:25:0x00d2  */
            public void onMatrixError(MatrixError matrixError) {
                RegistrationFlowResponse registrationFlowResponse;
                ArrayList arrayList;
                String access$000 = MXSession.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## deleteDevice() : onMatrixError ");
                sb.append(matrixError.getMessage());
                Log.d(access$000, sb.toString());
                if (matrixError.mStatus == null || matrixError.mStatus.intValue() != 401) {
                    String access$0002 = MXSession.LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## deleteDevice(): Received not expected status 401 =");
                    sb2.append(matrixError.mStatus);
                    Log.d(access$0002, sb2.toString());
                } else {
                    try {
                        registrationFlowResponse = JsonUtils.toRegistrationFlowResponse(matrixError.mErrorBodyAsString);
                    } catch (Exception e) {
                        Log.e(MXSession.LOG_TAG, "## deleteDevice(): Received status 401 - Exception - JsonUtils.toRegistrationFlowResponse()", e);
                    }
                    arrayList = new ArrayList();
                    if (!(registrationFlowResponse == null || registrationFlowResponse.flows == null || registrationFlowResponse.flows.isEmpty())) {
                        for (LoginFlow loginFlow : registrationFlowResponse.flows) {
                            if (loginFlow.stages != null) {
                                arrayList.addAll(loginFlow.stages);
                            }
                        }
                    }
                    if (arrayList.isEmpty()) {
                        DeleteDeviceParams deleteDeviceParams = new DeleteDeviceParams();
                        deleteDeviceParams.auth = new DeleteDeviceAuth();
                        deleteDeviceParams.auth.session = registrationFlowResponse.session;
                        deleteDeviceParams.auth.user = MXSession.this.mCredentials.userId;
                        deleteDeviceParams.auth.password = str3;
                        String access$0003 = MXSession.LOG_TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("## deleteDevice() : supported stages ");
                        sb3.append(arrayList);
                        Log.d(access$0003, sb3.toString());
                        MXSession.this.deleteDevice(str4, deleteDeviceParams, arrayList, apiCallback2);
                        return;
                    }
                    ApiCallback apiCallback = apiCallback2;
                    if (apiCallback != null) {
                        apiCallback.onMatrixError(matrixError);
                        return;
                    }
                    return;
                }
                registrationFlowResponse = null;
                arrayList = new ArrayList();
                for (LoginFlow loginFlow2 : registrationFlowResponse.flows) {
                }
                if (arrayList.isEmpty()) {
                }
            }
        };
        cryptoRestClient.deleteDevice(str, deleteDeviceParams, r2);
    }

    /* access modifiers changed from: private */
    public void deleteDevice(String str, DeleteDeviceParams deleteDeviceParams, List<String> list, ApiCallback<Void> apiCallback) {
        deleteDeviceParams.auth.type = (String) list.get(0);
        list.remove(0);
        CryptoRestClient cryptoRestClient = this.mCrypto.getCryptoRestClient();
        final ApiCallback<Void> apiCallback2 = apiCallback;
        final List<String> list2 = list;
        final String str2 = str;
        final DeleteDeviceParams deleteDeviceParams2 = deleteDeviceParams;
        AnonymousClass25 r1 = new SimpleApiCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                ApiCallback apiCallback = apiCallback2;
                if (apiCallback != null) {
                    apiCallback.onSuccess(null);
                }
            }

            public void onMatrixError(MatrixError matrixError) {
                if (((matrixError.mStatus != null && matrixError.mStatus.intValue() == 401) || TextUtils.equals(matrixError.errcode, MatrixError.FORBIDDEN) || TextUtils.equals(matrixError.errcode, MatrixError.UNKNOWN)) && !list2.isEmpty()) {
                    MXSession.this.deleteDevice(str2, deleteDeviceParams2, list2, apiCallback2);
                    return;
                }
                ApiCallback apiCallback = apiCallback2;
                if (apiCallback != null) {
                    apiCallback.onMatrixError(matrixError);
                }
            }
        };
        cryptoRestClient.deleteDevice(str, deleteDeviceParams, r1);
    }

    public void openIdToken(ApiCallback<Map<Object, Object>> apiCallback) {
        this.mAccountDataRestClient.openIdToken(getMyUserId(), apiCallback);
    }

    public GroupsManager getGroupsManager() {
        return this.mGroupsManager;
    }

    public String getDeviceId() {
        return getCredentials().deviceId;
    }

    public void setDeviceId(String str) {
        getCredentials().deviceId = str;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getMyUserId());
        sb.append(" ");
        sb.append(super.toString());
        return sb.toString();
    }
}
