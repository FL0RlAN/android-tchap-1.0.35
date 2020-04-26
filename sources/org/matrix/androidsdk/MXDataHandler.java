package org.matrix.androidsdk;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.matrix.androidsdk.call.MXCallsManager;
import org.matrix.androidsdk.core.BingRulesManager;
import org.matrix.androidsdk.core.DataHandlerInterface;
import org.matrix.androidsdk.core.FilterUtil;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXOsHandler;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.MXCrypto;
import org.matrix.androidsdk.crypto.MXCryptoError;
import org.matrix.androidsdk.crypto.MXDecryptionException;
import org.matrix.androidsdk.crypto.MXEventDecryptionResult;
import org.matrix.androidsdk.crypto.interfaces.CryptoDataHandler;
import org.matrix.androidsdk.crypto.interfaces.CryptoEvent;
import org.matrix.androidsdk.crypto.interfaces.CryptoEventListener;
import org.matrix.androidsdk.data.DataRetriever;
import org.matrix.androidsdk.data.MyUser;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.data.metrics.MetricsListener;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.data.store.MXMemoryStore;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.groups.GroupsManager;
import org.matrix.androidsdk.listeners.IMXEventListener;
import org.matrix.androidsdk.network.NetworkConnectivityReceiver;
import org.matrix.androidsdk.rest.client.AccountDataRestClient;
import org.matrix.androidsdk.rest.client.EventsRestClient;
import org.matrix.androidsdk.rest.client.PresenceRestClient;
import org.matrix.androidsdk.rest.client.ProfileRestClient;
import org.matrix.androidsdk.rest.client.RoomsRestClient;
import org.matrix.androidsdk.rest.client.ThirdPidRestClient;
import org.matrix.androidsdk.rest.model.ChunkEvents;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.Event.SentState;
import org.matrix.androidsdk.rest.model.ReceiptData;
import org.matrix.androidsdk.rest.model.RoomAliasDescription;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;
import org.matrix.androidsdk.rest.model.bingrules.PushRuleSet;
import org.matrix.androidsdk.rest.model.bingrules.PushRulesResponse;
import org.matrix.androidsdk.rest.model.filter.FilterBody;
import org.matrix.androidsdk.rest.model.filter.RoomEventFilter;
import org.matrix.androidsdk.rest.model.filter.RoomFilter;
import org.matrix.androidsdk.rest.model.group.InvitedGroupSync;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.matrix.androidsdk.rest.model.sync.AccountDataElement;
import org.matrix.androidsdk.rest.model.sync.InvitedRoomSync;
import org.matrix.androidsdk.rest.model.sync.RoomSync;
import org.matrix.androidsdk.rest.model.sync.SyncResponse;
import org.matrix.androidsdk.ssl.UnrecognizedCertificateException;

public class MXDataHandler implements CryptoDataHandler, DataHandlerInterface {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = MXDataHandler.class.getSimpleName();
    private AccountDataRestClient mAccountDataRestClient;
    /* access modifiers changed from: private */
    public boolean mAreLeftRoomsSynced;
    private BingRulesManager mBingRulesManager;
    private MXCallsManager mCallsManager;
    private final Credentials mCredentials;
    private MXCrypto mCrypto;
    private RoomEventFilter mCustomPaginationFilter;
    private DataRetriever mDataRetriever;
    private EventsRestClient mEventsRestClient;
    private GroupsManager mGroupsManager;
    private List<String> mIgnoredUserIdsList;
    private volatile String mInitialSyncToToken = null;
    private boolean mIsAlive = true;
    private boolean mIsLazyLoadingEnabled;
    /* access modifiers changed from: private */
    public boolean mIsRetrievingLeftRooms;
    private boolean mIsStartingCryptoWithInitialSync = false;
    /* access modifiers changed from: private */
    public final List<ApiCallback<Void>> mLeftRoomsRefreshCallbacks = new ArrayList();
    /* access modifiers changed from: private */
    public final MXMemoryStore mLeftRoomsStore;
    private List<String> mLocalDirectChatRoomIdsList = null;
    private MXMediaCache mMediaCache;
    private MetricsListener mMetricsListener;
    private MxEventDispatcher mMxEventDispatcher;
    private MyUser mMyUser;
    private NetworkConnectivityReceiver mNetworkConnectivityReceiver;
    private PresenceRestClient mPresenceRestClient;
    private ProfileRestClient mProfileRestClient;
    private RequestNetworkErrorListener mRequestNetworkErrorListener;
    private MatrixError mResourceLimitExceededError;
    private RoomsRestClient mRoomsRestClient;
    private final IMXStore mStore;
    private final MXOsHandler mSyncHandler;
    private HandlerThread mSyncHandlerThread;
    private ThirdPidRestClient mThirdPidRestClient;
    private final Set<String> mUpdatedRoomIdList = new HashSet();

    public interface RequestNetworkErrorListener {
        void onConfigurationError(String str);

        void onSSLCertificateError(UnrecognizedCertificateException unrecognizedCertificateException);
    }

    private class RoomIdsListRetroCompat {
        final String mParticipantUserId;
        final String mRoomId;

        public RoomIdsListRetroCompat(String str, String str2) {
            this.mParticipantUserId = str;
            this.mRoomId = str2;
        }
    }

    private void manageAcceptedTerms(AccountDataElement accountDataElement) {
    }

    public MXDataHandler(IMXStore iMXStore, Credentials credentials) {
        this.mStore = iMXStore;
        this.mCredentials = credentials;
        this.mMxEventDispatcher = new MxEventDispatcher();
        StringBuilder sb = new StringBuilder();
        sb.append("MXDataHandler");
        sb.append(this.mCredentials.userId);
        this.mSyncHandlerThread = new HandlerThread(sb.toString(), 1);
        this.mSyncHandlerThread.start();
        this.mSyncHandler = new MXOsHandler(this.mSyncHandlerThread.getLooper());
        this.mLeftRoomsStore = new MXMemoryStore(credentials, iMXStore.getContext());
    }

    public void setLazyLoadingEnabled(boolean z) {
        this.mIsLazyLoadingEnabled = z;
    }

    public boolean isLazyLoadingEnabled() {
        return this.mIsLazyLoadingEnabled;
    }

    public void setRequestNetworkErrorListener(RequestNetworkErrorListener requestNetworkErrorListener) {
        this.mRequestNetworkErrorListener = requestNetworkErrorListener;
    }

    public void setMetricsListener(MetricsListener metricsListener) {
        this.mMetricsListener = metricsListener;
    }

    public Credentials getCredentials() {
        return this.mCredentials;
    }

    public void setProfileRestClient(ProfileRestClient profileRestClient) {
        this.mProfileRestClient = profileRestClient;
    }

    public ProfileRestClient getProfileRestClient() {
        return this.mProfileRestClient;
    }

    public void setPresenceRestClient(PresenceRestClient presenceRestClient) {
        this.mPresenceRestClient = presenceRestClient;
    }

    public PresenceRestClient getPresenceRestClient() {
        return this.mPresenceRestClient;
    }

    public void setThirdPidRestClient(ThirdPidRestClient thirdPidRestClient) {
        this.mThirdPidRestClient = thirdPidRestClient;
    }

    public ThirdPidRestClient getThirdPidRestClient() {
        return this.mThirdPidRestClient;
    }

    public void setRoomsRestClient(RoomsRestClient roomsRestClient) {
        this.mRoomsRestClient = roomsRestClient;
    }

    public void setEventsRestClient(EventsRestClient eventsRestClient) {
        this.mEventsRestClient = eventsRestClient;
    }

    public void setAccountDataRestClient(AccountDataRestClient accountDataRestClient) {
        this.mAccountDataRestClient = accountDataRestClient;
    }

    public void setNetworkConnectivityReceiver(NetworkConnectivityReceiver networkConnectivityReceiver) {
        this.mNetworkConnectivityReceiver = networkConnectivityReceiver;
        if (getCrypto() != null) {
            getCrypto().setNetworkConnectivityReceiver(this.mNetworkConnectivityReceiver);
        }
    }

    public void setGroupsManager(GroupsManager groupsManager) {
        this.mGroupsManager = groupsManager;
    }

    public MXCrypto getCrypto() {
        return this.mCrypto;
    }

    public void setCrypto(MXCrypto mXCrypto) {
        this.mCrypto = mXCrypto;
    }

    public boolean isCryptoEnabled() {
        return this.mCrypto != null;
    }

    public List<String> getIgnoredUserIds() {
        if (this.mIgnoredUserIdsList == null) {
            this.mIgnoredUserIdsList = this.mStore.getIgnoredUserIdsList();
        }
        if (this.mIgnoredUserIdsList == null) {
            this.mIgnoredUserIdsList = new ArrayList();
        }
        return this.mIgnoredUserIdsList;
    }

    private void checkIfAlive() {
        synchronized (this) {
            if (!this.mIsAlive) {
                Log.e(LOG_TAG, "use of a released dataHandler", new Exception("use of a released dataHandler"));
            }
        }
    }

    public boolean isAlive() {
        boolean z;
        synchronized (this) {
            z = this.mIsAlive;
        }
        return z;
    }

    public void onConfigurationError(String str) {
        RequestNetworkErrorListener requestNetworkErrorListener = this.mRequestNetworkErrorListener;
        if (requestNetworkErrorListener != null) {
            requestNetworkErrorListener.onConfigurationError(str);
        }
    }

    public void onSSLCertificateError(UnrecognizedCertificateException unrecognizedCertificateException) {
        RequestNetworkErrorListener requestNetworkErrorListener = this.mRequestNetworkErrorListener;
        if (requestNetworkErrorListener != null) {
            requestNetworkErrorListener.onSSLCertificateError(unrecognizedCertificateException);
        }
    }

    public MatrixError getResourceLimitExceededError() {
        return this.mResourceLimitExceededError;
    }

    public MyUser getMyUser() {
        checkIfAlive();
        IMXStore store = getStore();
        if (this.mMyUser == null) {
            this.mMyUser = new MyUser(store.getUser(this.mCredentials.userId));
            this.mMyUser.setDataHandler(this);
            if (store.displayName() == null) {
                store.setAvatarURL(this.mMyUser.getAvatarUrl(), System.currentTimeMillis());
                store.setDisplayName(this.mMyUser.displayname, System.currentTimeMillis());
            } else {
                this.mMyUser.displayname = store.displayName();
                this.mMyUser.setAvatarUrl(store.avatarURL());
            }
            this.mMyUser.user_id = this.mCredentials.userId;
        } else if (store != null) {
            if (store.displayName() == null && this.mMyUser.displayname != null) {
                store.setAvatarURL(this.mMyUser.getAvatarUrl(), System.currentTimeMillis());
                store.setDisplayName(this.mMyUser.displayname, System.currentTimeMillis());
            } else if (!TextUtils.equals(this.mMyUser.displayname, store.displayName())) {
                this.mMyUser.displayname = store.displayName();
                this.mMyUser.setAvatarUrl(store.avatarURL());
            }
        }
        this.mMyUser.refreshUserInfos(null);
        return this.mMyUser;
    }

    public boolean isInitialSyncComplete() {
        checkIfAlive();
        return this.mInitialSyncToToken != null;
    }

    public DataRetriever getDataRetriever() {
        checkIfAlive();
        return this.mDataRetriever;
    }

    public void setDataRetriever(DataRetriever dataRetriever) {
        checkIfAlive();
        this.mDataRetriever = dataRetriever;
    }

    public void setPushRulesManager(BingRulesManager bingRulesManager) {
        if (isAlive()) {
            this.mBingRulesManager = bingRulesManager;
            this.mBingRulesManager.loadRules(new SimpleApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    MXDataHandler.this.onBingRulesUpdate();
                }
            });
        }
    }

    public void setCallsManager(MXCallsManager mXCallsManager) {
        checkIfAlive();
        this.mCallsManager = mXCallsManager;
    }

    public MXCallsManager getCallsManager() {
        checkIfAlive();
        return this.mCallsManager;
    }

    public void setMediaCache(MXMediaCache mXMediaCache) {
        checkIfAlive();
        this.mMediaCache = mXMediaCache;
    }

    public MXMediaCache getMediaCache() {
        checkIfAlive();
        return this.mMediaCache;
    }

    public PushRuleSet pushRules() {
        if (isAlive()) {
            BingRulesManager bingRulesManager = this.mBingRulesManager;
            if (bingRulesManager != null) {
                return bingRulesManager.pushRules();
            }
        }
        return null;
    }

    public void refreshPushRules() {
        if (isAlive()) {
            BingRulesManager bingRulesManager = this.mBingRulesManager;
            if (bingRulesManager != null) {
                bingRulesManager.loadRules(new SimpleApiCallback<Void>() {
                    public void onSuccess(Void voidR) {
                        MXDataHandler.this.onBingRulesUpdate();
                    }
                });
            }
        }
    }

    public BingRulesManager getBingRulesManager() {
        checkIfAlive();
        return this.mBingRulesManager;
    }

    public void setCryptoEventsListener(CryptoEventListener cryptoEventListener) {
        this.mMxEventDispatcher.setCryptoEventsListener(cryptoEventListener);
    }

    public void addListener(IMXEventListener iMXEventListener) {
        if (isAlive() && iMXEventListener != null) {
            synchronized (this.mMxEventDispatcher) {
                this.mMxEventDispatcher.addListener(iMXEventListener);
            }
            if (this.mInitialSyncToToken != null) {
                iMXEventListener.onInitialSyncComplete(this.mInitialSyncToToken);
            }
        }
    }

    public void removeListener(IMXEventListener iMXEventListener) {
        if (isAlive() && iMXEventListener != null) {
            synchronized (this.mMxEventDispatcher) {
                this.mMxEventDispatcher.removeListener(iMXEventListener);
            }
        }
    }

    public void clear() {
        synchronized (this.mMxEventDispatcher) {
            this.mIsAlive = false;
            this.mMxEventDispatcher.clearListeners();
        }
        this.mStore.close();
        this.mStore.clear();
        HandlerThread handlerThread = this.mSyncHandlerThread;
        if (handlerThread != null) {
            handlerThread.quit();
            this.mSyncHandlerThread = null;
        }
    }

    public String getUserId() {
        return isAlive() ? this.mCredentials.userId : "dummy";
    }

    /* access modifiers changed from: 0000 */
    public void checkPermanentStorageData() {
        if (!isAlive()) {
            Log.e(LOG_TAG, "checkPermanentStorageData : the session is not anymore active");
            return;
        }
        for (RoomSummary roomSummary : this.mStore.getSummaries()) {
            if (roomSummary.getLatestRoomState() != null) {
                roomSummary.getLatestRoomState().setDataHandler(this);
            }
        }
    }

    public IMXStore getStore() {
        if (isAlive()) {
            return this.mStore;
        }
        Log.e(LOG_TAG, "getStore : the session is not anymore active");
        return null;
    }

    public IMXStore getStore(String str) {
        if (!isAlive()) {
            Log.e(LOG_TAG, "getStore : the session is not anymore active");
            return null;
        } else if (str == null) {
            return this.mStore;
        } else {
            if (this.mLeftRoomsStore.getRoom(str) != null) {
                return this.mLeftRoomsStore;
            }
            return this.mStore;
        }
    }

    public RoomMember getMember(Collection<RoomMember> collection, String str) {
        if (isAlive()) {
            for (RoomMember roomMember : collection) {
                if (TextUtils.equals(str, roomMember.getUserId())) {
                    return roomMember;
                }
            }
        } else {
            Log.e(LOG_TAG, "getMember : the session is not anymore active");
        }
        return null;
    }

    public boolean doesRoomExist(String str) {
        return (str == null || this.mStore.getRoom(str) == null) ? false : true;
    }

    public Collection<Room> getLeftRooms() {
        return new ArrayList(this.mLeftRoomsStore.getRooms());
    }

    public Room getRoom(String str) {
        return getRoom(str, true);
    }

    public Room getRoom(String str, boolean z) {
        return getRoom(this.mStore, str, z);
    }

    public Room getRoom(String str, boolean z, boolean z2) {
        if (str == null) {
            return null;
        }
        Room room = this.mStore.getRoom(str);
        if (room == null && z) {
            room = this.mLeftRoomsStore.getRoom(str);
        }
        return (room != null || !z2) ? room : getRoom(this.mStore, str, z2);
    }

    public Room getRoom(IMXStore iMXStore, String str, boolean z) {
        Room room;
        if (!isAlive()) {
            Log.e(LOG_TAG, "getRoom : the session is not anymore active");
            return null;
        } else if (TextUtils.isEmpty(str)) {
            return null;
        } else {
            synchronized (this) {
                room = iMXStore.getRoom(str);
                if (room == null && z) {
                    String str2 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## getRoom() : create the room ");
                    sb.append(str);
                    Log.d(str2, sb.toString());
                    room = new Room(this, iMXStore, str);
                    iMXStore.storeRoom(room);
                } else if (room != null && room.getDataHandler() == null) {
                    String str3 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("getRoom ");
                    sb2.append(str);
                    sb2.append(" was not initialized");
                    Log.e(str3, sb2.toString());
                    iMXStore.storeRoom(room);
                }
            }
            return room;
        }
    }

    public Collection<RoomSummary> getSummaries(boolean z) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(getStore().getSummaries());
        if (z) {
            arrayList.addAll(this.mLeftRoomsStore.getSummaries());
        }
        return arrayList;
    }

    public void roomIdByAlias(String str, final ApiCallback<String> apiCallback) {
        Iterator it = getStore().getRooms().iterator();
        final String str2 = null;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Room room = (Room) it.next();
            if (!TextUtils.equals(room.getState().getCanonicalAlias(), str)) {
                Iterator it2 = room.getState().getAliases().iterator();
                while (true) {
                    if (it2.hasNext()) {
                        if (TextUtils.equals((String) it2.next(), str)) {
                            str2 = room.getRoomId();
                            continue;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (str2 != null) {
                    break;
                }
            } else {
                str2 = room.getRoomId();
                break;
            }
        }
        if (str2 != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    apiCallback.onSuccess(str2);
                }
            });
        } else {
            this.mRoomsRestClient.getRoomIdByAlias(str, new SimpleApiCallback<RoomAliasDescription>(apiCallback) {
                public void onSuccess(RoomAliasDescription roomAliasDescription) {
                    apiCallback.onSuccess(roomAliasDescription.room_id);
                }
            });
        }
    }

    public void getMembersAsync(final String str, final ApiCallback<List<RoomMember>> apiCallback) {
        String str2 = str;
        this.mRoomsRestClient.getRoomMembers(str2, null, null, "leave", new SimpleApiCallback<ChunkEvents>(apiCallback) {
            public void onSuccess(ChunkEvents chunkEvents) {
                Room room = MXDataHandler.this.getRoom(str);
                if (chunkEvents.chunk != null) {
                    for (Event applyState : chunkEvents.chunk) {
                        room.getState().applyState(applyState, true, MXDataHandler.this.getStore());
                    }
                }
                apiCallback.onSuccess(room.getState().getLoadedMembers());
            }
        });
    }

    public void deleteRoomEvent(Event event) {
        if (isAlive()) {
            Room room = getRoom(event.roomId);
            if (room != null) {
                this.mStore.deleteEvent(event);
                Event latestEvent = this.mStore.getLatestEvent(event.roomId);
                RoomState deepCopy = room.getState().deepCopy();
                RoomSummary summary = this.mStore.getSummary(event.roomId);
                if (summary == null) {
                    summary = new RoomSummary(null, latestEvent, deepCopy, this.mCredentials.userId);
                } else {
                    summary.setLatestReceivedEvent(latestEvent, deepCopy);
                }
                if (TextUtils.equals(summary.getReadReceiptEventId(), event.eventId)) {
                    summary.setReadReceiptEventId(latestEvent.eventId);
                }
                if (TextUtils.equals(summary.getReadMarkerEventId(), event.eventId)) {
                    summary.setReadMarkerEventId(latestEvent.eventId);
                }
                this.mStore.storeSummary(summary);
                return;
            }
            return;
        }
        Log.e(LOG_TAG, "deleteRoomEvent : the session is not anymore active");
    }

    public User getUser(String str) {
        if (!isAlive()) {
            Log.e(LOG_TAG, "getUser : the session is not anymore active");
            return null;
        }
        User user = this.mStore.getUser(str);
        if (user == null) {
            user = this.mLeftRoomsStore.getUser(str);
        }
        return user;
    }

    private void manageAccountData(List<AccountDataElement> list, boolean z) {
        try {
            for (AccountDataElement accountDataElement : list) {
                if (AccountDataElement.ACCOUNT_DATA_TYPE_IGNORED_USER_LIST.equals(accountDataElement.type)) {
                    manageIgnoredUsers(accountDataElement, z);
                } else if (AccountDataElement.ACCOUNT_DATA_TYPE_PUSH_RULES.equals(accountDataElement.type)) {
                    managePushRulesUpdate(accountDataElement);
                } else if (AccountDataElement.ACCOUNT_DATA_TYPE_DIRECT_MESSAGES.equals(accountDataElement.type)) {
                    manageDirectChatRooms(accountDataElement, z);
                } else if (AccountDataElement.ACCOUNT_DATA_TYPE_PREVIEW_URLS.equals(accountDataElement.type)) {
                    manageUrlPreview(accountDataElement);
                } else if ("m.widgets".equals(accountDataElement.type)) {
                    manageUserWidgets(accountDataElement);
                } else if (AccountDataElement.ACCOUNT_DATA_ACCEPTED_TERMS.equals(accountDataElement.type)) {
                    manageAcceptedTerms(accountDataElement);
                }
            }
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("manageAccountData failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    private void managePushRulesUpdate(AccountDataElement accountDataElement) {
        Gson gson = JsonUtils.getGson(false);
        getBingRulesManager().buildRules((PushRulesResponse) gson.fromJson(gson.toJsonTree(accountDataElement.content), PushRulesResponse.class));
        onBingRulesUpdate();
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0021  */
    private void manageIgnoredUsers(AccountDataElement accountDataElement, boolean z) {
        ArrayList arrayList;
        Map<String, Object> map = accountDataElement.content;
        String str = AccountDataElement.ACCOUNT_DATA_KEY_IGNORED_USERS;
        if (map.containsKey(str)) {
            Map map2 = (Map) accountDataElement.content.get(str);
            if (map2 != null) {
                arrayList = new ArrayList(map2.keySet());
                if (arrayList == null) {
                    List ignoredUserIds = getIgnoredUserIds();
                    if (arrayList.size() != 0 || ignoredUserIds.size() != 0) {
                        if (arrayList.size() != ignoredUserIds.size() || !arrayList.containsAll(ignoredUserIds)) {
                            this.mStore.setIgnoredUserIdsList(arrayList);
                            this.mIgnoredUserIdsList = arrayList;
                            if (!z) {
                                onIgnoredUsersListUpdate();
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
        }
        arrayList = null;
        if (arrayList == null) {
        }
    }

    private void manageDirectChatRooms(AccountDataElement accountDataElement, boolean z) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## manageDirectChatRooms() : update direct chats map");
        sb.append(accountDataElement.content);
        Log.d(str, sb.toString());
        Gson gson = JsonUtils.getGson(false);
        this.mStore.setDirectChatRoomsDict((Map) gson.fromJson(gson.toJsonTree(accountDataElement.content), new TypeToken<Map<String, List<String>>>() {
        }.getType()));
        this.mLocalDirectChatRoomIdsList = null;
        if (!z) {
            onDirectMessageChatRoomsListUpdate();
        }
    }

    private void manageUrlPreview(AccountDataElement accountDataElement) {
        Map<String, Object> map = accountDataElement.content;
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## manageUrlPreview() : ");
        sb.append(map);
        Log.d(str, sb.toString());
        String str2 = AccountDataElement.ACCOUNT_DATA_KEY_URL_PREVIEW_DISABLE;
        boolean z = true;
        if (map.containsKey(str2)) {
            z = true ^ ((Boolean) map.get(str2)).booleanValue();
        }
        this.mStore.setURLPreviewEnabled(z);
    }

    private void manageUserWidgets(AccountDataElement accountDataElement) {
        Map<String, Object> map = accountDataElement.content;
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## manageUserWidgets() : ");
        sb.append(map);
        Log.d(str, sb.toString());
        this.mStore.setUserWidgets(map);
    }

    private void handlePresenceEvent(Event event) {
        if (Event.EVENT_TYPE_PRESENCE.equals(event.getType())) {
            User user = JsonUtils.toUser(event.getContent());
            if (!TextUtils.isEmpty(event.getSender())) {
                user.user_id = event.getSender();
            }
            User user2 = this.mStore.getUser(user.user_id);
            if (user2 == null) {
                user.setDataHandler(this);
            } else {
                user2.currently_active = user.currently_active;
                user2.presence = user.presence;
                user2.lastActiveAgo = user.lastActiveAgo;
                user = user2;
            }
            user.setLatestPresenceTs(System.currentTimeMillis());
            if (this.mCredentials.userId.equals(user.user_id)) {
                getMyUser().displayname = user.displayname;
                getMyUser().avatar_url = user.getAvatarUrl();
                this.mStore.setAvatarURL(user.getAvatarUrl(), event.getOriginServerTs());
                this.mStore.setDisplayName(user.displayname, event.getOriginServerTs());
            }
            this.mStore.storeUser(user);
            onPresenceUpdate(event, user);
        }
    }

    public void onSyncResponse(final SyncResponse syncResponse, final String str, final boolean z) {
        this.mSyncHandler.post(new Runnable() {
            public void run() {
                MXDataHandler.this.manageResponse(syncResponse, str, z);
            }
        });
    }

    public void deleteRoom(String str) {
        Room room = getStore().getRoom(str);
        if (room != null) {
            if (this.mAreLeftRoomsSynced) {
                Room room2 = getRoom((IMXStore) this.mLeftRoomsStore, str, true);
                room2.setIsLeft(true);
                RoomSummary summary = getStore().getSummary(str);
                if (summary != null) {
                    this.mLeftRoomsStore.storeSummary(new RoomSummary(summary, summary.getLatestReceivedEvent(), summary.getLatestRoomState(), getUserId()));
                }
                ArrayList<ReceiptData> arrayList = new ArrayList<>();
                Collection<Event> roomMessages = getStore().getRoomMessages(str);
                if (roomMessages != null) {
                    for (Event event : roomMessages) {
                        arrayList.addAll(getStore().getEventReceipts(str, event.eventId, false, false));
                        this.mLeftRoomsStore.storeLiveRoomEvent(event);
                    }
                    for (ReceiptData storeReceipt : arrayList) {
                        this.mLeftRoomsStore.storeReceipt(storeReceipt, str);
                    }
                }
                room2.getTimeline().setState(room.getTimeline().getState());
            }
            getStore().deleteRoom(str);
        }
    }

    /* access modifiers changed from: private */
    public void manageResponse(SyncResponse syncResponse, String str, boolean z) {
        boolean z2;
        String str2;
        boolean z3;
        String str3;
        String str4;
        ArrayList arrayList;
        HashMap hashMap;
        SyncResponse syncResponse2 = syncResponse;
        String str5 = str;
        boolean z4 = z;
        if (!isAlive()) {
            Log.e(LOG_TAG, "manageResponse : ignored because the session has been closed");
            return;
        }
        boolean z5 = str5 == null;
        if (syncResponse2 != null) {
            Log.d(LOG_TAG, "onSyncComplete");
            if (!(syncResponse2.toDevice == null || syncResponse2.toDevice.events == null || syncResponse2.toDevice.events.size() <= 0)) {
                String str6 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("manageResponse : receives ");
                sb.append(syncResponse2.toDevice.events.size());
                sb.append(" toDevice events");
                Log.d(str6, sb.toString());
                for (Event handleToDeviceEvent : syncResponse2.toDevice.events) {
                    handleToDeviceEvent(handleToDeviceEvent);
                }
            }
            String str7 = "Received ";
            if (!(syncResponse2.accountData == null || syncResponse2.accountData.accountDataElements == null || syncResponse2.accountData.accountDataElements.isEmpty())) {
                String str8 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str7);
                sb2.append(syncResponse2.accountData.accountDataElements.size());
                sb2.append(" accountData events");
                Log.d(str8, sb2.toString());
                manageAccountData(syncResponse2.accountData.accountDataElements, z5);
                getStore().storeAccountData(syncResponse2.accountData);
                this.mMxEventDispatcher.dispatchOnAccountDataUpdate();
            }
            if (syncResponse2.rooms != null) {
                String str9 = "the room ";
                String str10 = " for room ";
                if (syncResponse2.rooms.join == null || syncResponse2.rooms.join.size() <= 0) {
                    z3 = true;
                } else {
                    String str11 = LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str7);
                    sb3.append(syncResponse2.rooms.join.size());
                    sb3.append(" joined rooms");
                    Log.d(str11, sb3.toString());
                    MetricsListener metricsListener = this.mMetricsListener;
                    if (metricsListener != null) {
                        metricsListener.onRoomsLoaded(syncResponse2.rooms.join.size());
                    }
                    for (String str12 : syncResponse2.rooms.join.keySet()) {
                        try {
                            if (this.mLeftRoomsStore.getRoom(str12) != null) {
                                String str13 = LOG_TAG;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append(str9);
                                sb4.append(str12);
                                sb4.append(" moves from left to the joined ones");
                                Log.d(str13, sb4.toString());
                                this.mLeftRoomsStore.deleteRoom(str12);
                            }
                            getRoom(str12).handleJoinedRoomSync((RoomSync) syncResponse2.rooms.join.get(str12), z5);
                        } catch (Exception e) {
                            String str14 = LOG_TAG;
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("## manageResponse() : handleJoinedRoomSync failed ");
                            sb5.append(e.getMessage());
                            sb5.append(str10);
                            sb5.append(str12);
                            Log.e(str14, sb5.toString(), e);
                        }
                    }
                    z3 = false;
                }
                if (syncResponse2.rooms.invite != null && syncResponse2.rooms.invite.size() > 0) {
                    String str15 = LOG_TAG;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(str7);
                    sb6.append(syncResponse2.rooms.invite.size());
                    sb6.append(" invited rooms");
                    Log.d(str15, sb6.toString());
                    boolean z6 = false;
                    Map map = null;
                    for (String str16 : syncResponse2.rooms.invite.keySet()) {
                        try {
                            String str17 = LOG_TAG;
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append("## manageResponse() : the user has been invited to ");
                            sb7.append(str16);
                            Log.d(str17, sb7.toString());
                            if (this.mLeftRoomsStore.getRoom(str16) != null) {
                                String str18 = LOG_TAG;
                                StringBuilder sb8 = new StringBuilder();
                                sb8.append(str9);
                                sb8.append(str16);
                                sb8.append(" moves from left to the invited ones");
                                Log.d(str18, sb8.toString());
                                this.mLeftRoomsStore.deleteRoom(str16);
                            }
                            Room room = getRoom(str16);
                            InvitedRoomSync invitedRoomSync = (InvitedRoomSync) syncResponse2.rooms.invite.get(str16);
                            room.handleInvitedRoomSync(invitedRoomSync);
                            if (room.isDirectChatInvitation()) {
                                Iterator it = invitedRoomSync.inviteState.events.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        str4 = null;
                                        break;
                                    }
                                    Event event = (Event) it.next();
                                    if (event.sender != null) {
                                        str4 = event.sender;
                                        break;
                                    }
                                }
                                if (str4 != null) {
                                    if (map == null) {
                                        if (getStore().getDirectChatRoomsDict() != null) {
                                            hashMap = new HashMap(getStore().getDirectChatRoomsDict());
                                        } else {
                                            hashMap = new HashMap();
                                        }
                                        map = hashMap;
                                    }
                                    if (map.containsKey(str4)) {
                                        arrayList = new ArrayList((Collection) map.get(str4));
                                    } else {
                                        arrayList = new ArrayList();
                                    }
                                    if (arrayList.indexOf(str16) < 0) {
                                        Log.d(LOG_TAG, "## manageResponse() : add this new invite in direct chats");
                                        arrayList.add(str16);
                                        map.put(str4, arrayList);
                                        z6 = true;
                                    }
                                }
                            }
                        } catch (Exception e2) {
                            String str19 = LOG_TAG;
                            StringBuilder sb9 = new StringBuilder();
                            sb9.append("## manageResponse() : handleInvitedRoomSync failed ");
                            sb9.append(e2.getMessage());
                            sb9.append(str10);
                            sb9.append(str16);
                            Log.e(str19, sb9.toString(), e2);
                        }
                    }
                    if (z6) {
                        this.mAccountDataRestClient.setAccountData(this.mCredentials.userId, AccountDataElement.ACCOUNT_DATA_TYPE_DIRECT_MESSAGES, map, new ApiCallback<Void>() {
                            public void onSuccess(Void voidR) {
                                Log.d(MXDataHandler.LOG_TAG, "## manageResponse() : succeeds");
                            }

                            public void onNetworkError(Exception exc) {
                                String access$100 = MXDataHandler.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## manageResponse() : update account data failed ");
                                sb.append(exc.getMessage());
                                Log.e(access$100, sb.toString(), exc);
                            }

                            public void onMatrixError(MatrixError matrixError) {
                                String access$100 = MXDataHandler.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## manageResponse() : update account data failed ");
                                sb.append(matrixError.getMessage());
                                Log.e(access$100, sb.toString());
                            }

                            public void onUnexpectedError(Exception exc) {
                                String access$100 = MXDataHandler.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## manageResponse() : update account data failed ");
                                sb.append(exc.getMessage());
                                Log.e(access$100, sb.toString(), exc);
                            }
                        });
                    }
                    z3 = false;
                }
                if (syncResponse2.rooms.leave == null || syncResponse2.rooms.leave.size() <= 0) {
                    z2 = z3;
                } else {
                    String str20 = LOG_TAG;
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append(str7);
                    sb10.append(syncResponse2.rooms.leave.size());
                    sb10.append(" left rooms");
                    Log.d(str20, sb10.toString());
                    for (String str21 : syncResponse2.rooms.leave.keySet()) {
                        Room room2 = getRoom(str21);
                        String str22 = "leave";
                        if (room2 != null) {
                            room2.handleJoinedRoomSync((RoomSync) syncResponse2.rooms.leave.get(str21), z5);
                            RoomMember member = room2.getMember(getUserId());
                            str3 = member != null ? member.membership : str22;
                            String str23 = LOG_TAG;
                            StringBuilder sb11 = new StringBuilder();
                            sb11.append("## manageResponse() : leave the room ");
                            sb11.append(str21);
                            Log.d(str23, sb11.toString());
                        } else {
                            str3 = str22;
                        }
                        if (TextUtils.equals(str3, RoomMember.MEMBERSHIP_KICK) || TextUtils.equals(str3, "ban")) {
                            onRoomKick(str21);
                        } else {
                            getStore().deleteRoom(str21);
                            onLeaveRoom(str21);
                        }
                        if (this.mAreLeftRoomsSynced && TextUtils.equals(str3, str22)) {
                            getRoom((IMXStore) this.mLeftRoomsStore, str21, true).handleJoinedRoomSync((RoomSync) syncResponse2.rooms.leave.get(str21), z5);
                        }
                    }
                    z2 = false;
                }
            } else {
                z2 = true;
            }
            if (syncResponse2.groups != null) {
                if (syncResponse2.groups.invite != null && !syncResponse2.groups.invite.isEmpty()) {
                    for (String str24 : syncResponse2.groups.invite.keySet()) {
                        InvitedGroupSync invitedGroupSync = (InvitedGroupSync) syncResponse2.groups.invite.get(str24);
                        this.mGroupsManager.onNewGroupInvitation(str24, invitedGroupSync.profile, invitedGroupSync.inviter, !z5);
                    }
                }
                if (syncResponse2.groups.join != null && !syncResponse2.groups.join.isEmpty()) {
                    for (String onJoinGroup : syncResponse2.groups.join.keySet()) {
                        this.mGroupsManager.onJoinGroup(onJoinGroup, !z5);
                    }
                }
                if (syncResponse2.groups.leave != null && !syncResponse2.groups.leave.isEmpty()) {
                    for (String onLeaveGroup : syncResponse2.groups.leave.keySet()) {
                        this.mGroupsManager.onLeaveGroup(onLeaveGroup, !z5);
                    }
                }
            }
            if (!(syncResponse2.presence == null || syncResponse2.presence.events == null)) {
                String str25 = LOG_TAG;
                StringBuilder sb12 = new StringBuilder();
                sb12.append(str7);
                sb12.append(syncResponse2.presence.events.size());
                sb12.append(" presence events");
                Log.d(str25, sb12.toString());
                for (Event handlePresenceEvent : syncResponse2.presence.events) {
                    handlePresenceEvent(handlePresenceEvent);
                }
            }
            MXCrypto mXCrypto = this.mCrypto;
            if (mXCrypto != null) {
                mXCrypto.onSyncCompleted(syncResponse2, str5, z4);
            }
            IMXStore store = getStore();
            if (!z2 && store != null) {
                store.setEventStreamToken(syncResponse2.nextBatch);
                store.commit();
            }
        } else {
            z2 = true;
        }
        if (z5) {
            if (!z4) {
                startCrypto(true);
            } else {
                this.mIsStartingCryptoWithInitialSync = !z2;
            }
            onInitialSyncComplete(syncResponse2 != null ? syncResponse2.nextBatch : null);
        } else {
            if (!z4) {
                startCrypto(this.mIsStartingCryptoWithInitialSync);
            }
            if (syncResponse2 != null) {
                try {
                    str2 = syncResponse2.nextBatch;
                } catch (Exception e3) {
                    String str26 = LOG_TAG;
                    StringBuilder sb13 = new StringBuilder();
                    sb13.append("onLiveEventsChunkProcessed failed ");
                    sb13.append(e3.getMessage());
                    Log.e(str26, sb13.toString(), e3);
                }
            } else {
                str2 = str5;
            }
            onLiveEventsChunkProcessed(str5, str2);
            try {
                this.mCallsManager.checkPendingIncomingCalls();
            } catch (Exception e4) {
                String str27 = LOG_TAG;
                StringBuilder sb14 = new StringBuilder();
                sb14.append("checkPendingIncomingCalls failed ");
                sb14.append(e4);
                sb14.append(" ");
                sb14.append(e4.getMessage());
                Log.e(str27, sb14.toString(), e4);
            }
        }
    }

    private void refreshUnreadCounters() {
        HashSet<String> hashSet;
        synchronized (this.mUpdatedRoomIdList) {
            hashSet = new HashSet<>(this.mUpdatedRoomIdList);
            this.mUpdatedRoomIdList.clear();
        }
        for (String room : hashSet) {
            Room room2 = this.mStore.getRoom(room);
            if (room2 != null) {
                room2.refreshUnreadCounter();
            }
        }
    }

    public boolean areLeftRoomsSynced() {
        return this.mAreLeftRoomsSynced;
    }

    public boolean isRetrievingLeftRooms() {
        return this.mIsRetrievingLeftRooms;
    }

    public void releaseLeftRooms() {
        if (this.mAreLeftRoomsSynced) {
            this.mLeftRoomsStore.clear();
            this.mAreLeftRoomsSynced = false;
        }
    }

    public void retrieveLeftRooms(ApiCallback<Void> apiCallback) {
        int size;
        if (!this.mAreLeftRoomsSynced) {
            synchronized (this.mLeftRoomsRefreshCallbacks) {
                if (apiCallback != null) {
                    this.mLeftRoomsRefreshCallbacks.add(apiCallback);
                }
                size = this.mLeftRoomsRefreshCallbacks.size();
            }
            if (1 == size) {
                this.mIsRetrievingLeftRooms = true;
                Log.d(LOG_TAG, "## refreshHistoricalRoomsList() : requesting");
                this.mEventsRestClient.syncFromToken(null, 0, 30000, null, getLeftRoomsFilter(), new ApiCallback<SyncResponse>() {
                    public void onSuccess(final SyncResponse syncResponse) {
                        Thread thread = new Thread(new Runnable() {
                            public void run() {
                                if (syncResponse.rooms.leave != null) {
                                    for (String str : syncResponse.rooms.leave.keySet()) {
                                        Room room = MXDataHandler.this.getRoom((IMXStore) MXDataHandler.this.mLeftRoomsStore, str, true);
                                        if (room != null) {
                                            room.setIsLeft(true);
                                            room.handleJoinedRoomSync((RoomSync) syncResponse.rooms.leave.get(str), true);
                                            RoomMember member = room.getState().getMember(MXDataHandler.this.getUserId());
                                            if (member == null || !TextUtils.equals(member.membership, "leave")) {
                                                MXDataHandler.this.mLeftRoomsStore.deleteRoom(str);
                                            }
                                        }
                                    }
                                    String access$100 = MXDataHandler.LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("## refreshHistoricalRoomsList() : ");
                                    sb.append(MXDataHandler.this.mLeftRoomsStore.getRooms().size());
                                    sb.append(" left rooms");
                                    Log.d(access$100, sb.toString());
                                }
                                MXDataHandler.this.mIsRetrievingLeftRooms = false;
                                MXDataHandler.this.mAreLeftRoomsSynced = true;
                                synchronized (MXDataHandler.this.mLeftRoomsRefreshCallbacks) {
                                    for (ApiCallback onSuccess : MXDataHandler.this.mLeftRoomsRefreshCallbacks) {
                                        onSuccess.onSuccess(null);
                                    }
                                    MXDataHandler.this.mLeftRoomsRefreshCallbacks.clear();
                                }
                            }
                        });
                        thread.setPriority(1);
                        thread.start();
                    }

                    public void onNetworkError(Exception exc) {
                        synchronized (MXDataHandler.this.mLeftRoomsRefreshCallbacks) {
                            String access$100 = MXDataHandler.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## refreshHistoricalRoomsList() : failed ");
                            sb.append(exc.getMessage());
                            Log.e(access$100, sb.toString(), exc);
                            for (ApiCallback onNetworkError : MXDataHandler.this.mLeftRoomsRefreshCallbacks) {
                                onNetworkError.onNetworkError(exc);
                            }
                            MXDataHandler.this.mLeftRoomsRefreshCallbacks.clear();
                        }
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        synchronized (MXDataHandler.this.mLeftRoomsRefreshCallbacks) {
                            String access$100 = MXDataHandler.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## refreshHistoricalRoomsList() : failed ");
                            sb.append(matrixError.getMessage());
                            Log.e(access$100, sb.toString());
                            for (ApiCallback onMatrixError : MXDataHandler.this.mLeftRoomsRefreshCallbacks) {
                                onMatrixError.onMatrixError(matrixError);
                            }
                            MXDataHandler.this.mLeftRoomsRefreshCallbacks.clear();
                        }
                    }

                    public void onUnexpectedError(Exception exc) {
                        synchronized (MXDataHandler.this.mLeftRoomsRefreshCallbacks) {
                            String access$100 = MXDataHandler.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## refreshHistoricalRoomsList() : failed ");
                            sb.append(exc.getMessage());
                            Log.e(access$100, sb.toString(), exc);
                            for (ApiCallback onUnexpectedError : MXDataHandler.this.mLeftRoomsRefreshCallbacks) {
                                onUnexpectedError.onUnexpectedError(exc);
                            }
                            MXDataHandler.this.mLeftRoomsRefreshCallbacks.clear();
                        }
                    }
                });
            }
        } else if (apiCallback != null) {
            apiCallback.onSuccess(null);
        }
    }

    private String getLeftRoomsFilter() {
        FilterBody filterBody = new FilterBody();
        filterBody.room = new RoomFilter();
        filterBody.room.timeline = new RoomEventFilter();
        filterBody.room.timeline.limit = Integer.valueOf(1);
        filterBody.room.includeLeave = Boolean.valueOf(true);
        FilterUtil.enableLazyLoading(filterBody, isLazyLoadingEnabled());
        return filterBody.toJSONString();
    }

    /* access modifiers changed from: 0000 */
    public void setPaginationFilter(RoomEventFilter roomEventFilter) {
        this.mCustomPaginationFilter = roomEventFilter;
    }

    public RoomEventFilter getPaginationFilter() {
        RoomEventFilter roomEventFilter = this.mCustomPaginationFilter;
        if (roomEventFilter == null) {
            return FilterUtil.createRoomEventFilter(isLazyLoadingEnabled());
        }
        FilterUtil.enableLazyLoading(roomEventFilter, isLazyLoadingEnabled());
        return this.mCustomPaginationFilter;
    }

    private void handleToDeviceEvent(Event event) {
        decryptEvent(event, null);
        if (!TextUtils.equals(event.getType(), Event.EVENT_TYPE_MESSAGE) || event.getContent() == null || !TextUtils.equals(JsonUtils.getMessageMsgType(event.getContent()), "m.bad.encrypted")) {
            onToDeviceEvent(event);
            return;
        }
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## handleToDeviceEvent() : Warning: Unable to decrypt to-device event : ");
        sb.append(event.getContent());
        Log.e(str, sb.toString());
    }

    public boolean decryptEvent(Event event, String str) {
        if (event != null && TextUtils.equals(event.getType(), "m.room.encrypted")) {
            MXEventDecryptionResult mXEventDecryptionResult = null;
            if (getCrypto() != null) {
                try {
                    mXEventDecryptionResult = getCrypto().decryptEvent(event, str);
                } catch (MXDecryptionException e) {
                    event.setCryptoError(e.getCryptoError());
                }
                if (mXEventDecryptionResult != null) {
                    event.setClearData(mXEventDecryptionResult);
                    return true;
                }
            } else {
                event.setCryptoError(new MXCryptoError(MXCryptoError.ENCRYPTING_NOT_ENABLED_ERROR_CODE, MXCryptoError.ENCRYPTING_NOT_ENABLED_REASON, null));
            }
        }
        return false;
    }

    public void resetReplayAttackCheckInTimeline(String str) {
        if (str != null) {
            MXCrypto mXCrypto = this.mCrypto;
            if (mXCrypto != null && mXCrypto.getOlmDevice() != null) {
                this.mCrypto.resetReplayAttackCheckInTimeline(str);
            }
        }
    }

    public void onStoreReady() {
        this.mMxEventDispatcher.dispatchOnStoreReady();
    }

    public void onAccountInfoUpdate(MyUser myUser) {
        this.mMxEventDispatcher.dispatchOnAccountInfoUpdate(myUser);
    }

    public void onPresenceUpdate(Event event, User user) {
        this.mMxEventDispatcher.dispatchOnPresenceUpdate(event, user);
    }

    private boolean ignoreEvent(String str) {
        if (!this.mIsRetrievingLeftRooms || TextUtils.isEmpty(str) || this.mLeftRoomsStore.getRoom(str) == null) {
            return false;
        }
        return true;
    }

    public void onLiveEvent(Event event, RoomState roomState) {
        if (!ignoreEvent(event.roomId)) {
            String type = event.getType();
            if (!TextUtils.equals(Event.EVENT_TYPE_TYPING, type) && !TextUtils.equals(Event.EVENT_TYPE_RECEIPT, type)) {
                synchronized (this.mUpdatedRoomIdList) {
                    this.mUpdatedRoomIdList.add(roomState.roomId);
                }
            }
            this.mMxEventDispatcher.dispatchOnLiveEvent(event, roomState);
        }
    }

    public void onLiveEventsChunkProcessed(String str, String str2) {
        this.mResourceLimitExceededError = null;
        refreshUnreadCounters();
        this.mMxEventDispatcher.dispatchOnLiveEventsChunkProcessed(str, str2);
    }

    public void onBingEvent(Event event, RoomState roomState, BingRule bingRule) {
        this.mMxEventDispatcher.dispatchOnBingEvent(event, roomState, bingRule, ignoreEvent(event.roomId));
    }

    public void updateEventState(Event event, SentState sentState) {
        if (event != null && event.mSentState != sentState) {
            event.mSentState = sentState;
            getStore().flushRoomEvents(event.roomId);
            onEventSentStateUpdated(event);
        }
    }

    public void onEventSentStateUpdated(Event event) {
        this.mMxEventDispatcher.dispatchOnEventSentStateUpdated(event, ignoreEvent(event.roomId));
    }

    public void onEventSent(Event event, String str) {
        this.mMxEventDispatcher.dispatchOnEventSent(event, str, ignoreEvent(event.roomId));
    }

    public void onBingRulesUpdate() {
        this.mMxEventDispatcher.dispatchOnBingRulesUpdate();
    }

    private void startCrypto(boolean z) {
        if (getCrypto() != null && !getCrypto().isStarted() && !getCrypto().isStarting()) {
            getCrypto().setNetworkConnectivityReceiver(this.mNetworkConnectivityReceiver);
            getCrypto().start(z, new ApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    MXDataHandler.this.onCryptoSyncComplete();
                }

                private void onError(String str) {
                    String access$100 = MXDataHandler.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## onInitialSyncComplete() : getCrypto().start fails ");
                    sb.append(str);
                    Log.e(access$100, sb.toString());
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

    public void onInitialSyncComplete(String str) {
        this.mInitialSyncToToken = str;
        refreshUnreadCounters();
        this.mMxEventDispatcher.dispatchOnInitialSyncComplete(str);
    }

    public void onSyncError(MatrixError matrixError) {
        if (MatrixError.RESOURCE_LIMIT_EXCEEDED.equals(matrixError.errcode)) {
            this.mResourceLimitExceededError = matrixError;
        }
        this.mMxEventDispatcher.dispatchOnSyncError(matrixError);
    }

    public void onCryptoSyncComplete() {
        this.mMxEventDispatcher.dispatchOnCryptoSyncComplete();
    }

    public void onNewRoom(String str) {
        this.mMxEventDispatcher.dispatchOnNewRoom(str, ignoreEvent(str));
    }

    public void onJoinRoom(String str) {
        this.mMxEventDispatcher.dispatchOnJoinRoom(str, ignoreEvent(str));
    }

    public void onRoomInternalUpdate(String str) {
        this.mMxEventDispatcher.dispatchOnRoomInternalUpdate(str, ignoreEvent(str));
    }

    public void onNotificationCountUpdate(String str) {
        this.mMxEventDispatcher.dispatchOnNotificationCountUpdate(str, ignoreEvent(str));
    }

    public void onLeaveRoom(String str) {
        this.mMxEventDispatcher.dispatchOnLeaveRoom(str, ignoreEvent(str));
    }

    public void onRoomKick(String str) {
        this.mMxEventDispatcher.dispatchOnRoomKick(str, ignoreEvent(str));
    }

    public void onReceiptEvent(String str, List<String> list) {
        synchronized (this.mUpdatedRoomIdList) {
            this.mUpdatedRoomIdList.add(str);
        }
        this.mMxEventDispatcher.dispatchOnReceiptEvent(str, list, ignoreEvent(str));
    }

    public void onRoomTagEvent(String str) {
        this.mMxEventDispatcher.dispatchOnRoomTagEvent(str, ignoreEvent(str));
    }

    public void onReadMarkerEvent(String str) {
        this.mMxEventDispatcher.dispatchOnReadMarkerEvent(str, ignoreEvent(str));
    }

    public void onRoomFlush(String str) {
        this.mMxEventDispatcher.dispatchOnRoomFlush(str, ignoreEvent(str));
    }

    public void onIgnoredUsersListUpdate() {
        this.mMxEventDispatcher.dispatchOnIgnoredUsersListUpdate();
    }

    public void onToDeviceEvent(Event event) {
        this.mMxEventDispatcher.dispatchOnToDeviceEvent(event, ignoreEvent(event.roomId));
    }

    public void onDirectMessageChatRoomsListUpdate() {
        this.mMxEventDispatcher.dispatchOnDirectMessageChatRoomsListUpdate();
    }

    public void onEventDecrypted(CryptoEvent cryptoEvent) {
        this.mMxEventDispatcher.dispatchOnEventDecrypted(cryptoEvent);
    }

    public void onNewGroupInvitation(String str) {
        this.mMxEventDispatcher.dispatchOnNewGroupInvitation(str);
    }

    public void onJoinGroup(String str) {
        this.mMxEventDispatcher.dispatchOnJoinGroup(str);
    }

    public void onLeaveGroup(String str) {
        this.mMxEventDispatcher.dispatchOnLeaveGroup(str);
    }

    public void onGroupProfileUpdate(String str) {
        this.mMxEventDispatcher.dispatchOnGroupProfileUpdate(str);
    }

    public void onGroupRoomsListUpdate(String str) {
        this.mMxEventDispatcher.dispatchOnGroupRoomsListUpdate(str);
    }

    public void onGroupUsersListUpdate(String str) {
        this.mMxEventDispatcher.dispatchOnGroupUsersListUpdate(str);
    }

    public void onGroupInvitedUsersListUpdate(String str) {
        this.mMxEventDispatcher.dispatchOnGroupInvitedUsersListUpdate(str);
    }

    public List<String> getDirectChatRoomIdsList() {
        List<String> list = this.mLocalDirectChatRoomIdsList;
        if (list != null) {
            return list;
        }
        IMXStore store = getStore();
        ArrayList arrayList = new ArrayList();
        if (store == null) {
            Log.e(LOG_TAG, "## getDirectChatRoomIdsList() : null store");
            return arrayList;
        }
        Collection<List> collection = null;
        if (store.getDirectChatRoomsDict() != null) {
            collection = store.getDirectChatRoomsDict().values();
        }
        if (collection != null) {
            for (List<String> it : collection) {
                for (String str : it) {
                    if (arrayList.indexOf(str) < 0) {
                        arrayList.add(str);
                    }
                }
            }
        }
        this.mLocalDirectChatRoomIdsList = arrayList;
        return arrayList;
    }

    public void setDirectChatRoomsMap(Map<String, List<String>> map, ApiCallback<Void> apiCallback) {
        Log.d(LOG_TAG, "## setDirectChatRoomsMap()");
        IMXStore store = getStore();
        if (store != null) {
            store.setDirectChatRoomsDict(map);
        } else {
            Log.e(LOG_TAG, "## setDirectChatRoomsMap() : null store");
        }
        this.mLocalDirectChatRoomIdsList = null;
        this.mAccountDataRestClient.setAccountData(getMyUser().user_id, AccountDataElement.ACCOUNT_DATA_TYPE_DIRECT_MESSAGES, map, apiCallback);
    }

    public List<String> getDirectChatRoomIdsList(String str) {
        ArrayList arrayList = new ArrayList();
        IMXStore store = getStore();
        if (store.getDirectChatRoomsDict() != null) {
            HashMap hashMap = new HashMap(store.getDirectChatRoomsDict());
            if (hashMap.containsKey(str)) {
                arrayList = new ArrayList();
                for (String str2 : (List) hashMap.get(str)) {
                    if (store.getRoom(str2) != null) {
                        arrayList.add(str2);
                    }
                }
            } else {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getDirectChatRoomIdsList(): UserId ");
                sb.append(str);
                sb.append(" has no entry in account_data");
                Log.w(str3, sb.toString());
            }
        } else {
            Log.w(LOG_TAG, "## getDirectChatRoomIdsList(): failure - getDirectChatRoomsDict()=null");
        }
        return arrayList;
    }

    public Context getContext() {
        return getStore().getContext();
    }
}
