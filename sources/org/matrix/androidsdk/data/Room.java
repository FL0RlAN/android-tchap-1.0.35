package org.matrix.androidsdk.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Patterns;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.R;
import org.matrix.androidsdk.call.MXCallsManager;
import org.matrix.androidsdk.core.ImageUtils;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXPatterns;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.ApiFailureCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.MXCrypto;
import org.matrix.androidsdk.crypto.MXCryptoError;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;
import org.matrix.androidsdk.crypto.data.MXEncryptEventContentResult;
import org.matrix.androidsdk.crypto.interfaces.CryptoRoom;
import org.matrix.androidsdk.crypto.interfaces.CryptoRoomMember;
import org.matrix.androidsdk.data.RoomMediaMessage.EventCreationListener;
import org.matrix.androidsdk.data.room.RoomAvatarResolver;
import org.matrix.androidsdk.data.room.RoomDisplayNameResolver;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.data.timeline.EventTimeline;
import org.matrix.androidsdk.data.timeline.EventTimelineFactory;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.listeners.IMXEventListener;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.listeners.MXRoomEventListener;
import org.matrix.androidsdk.rest.client.RoomsRestClient;
import org.matrix.androidsdk.rest.client.UrlPostTask;
import org.matrix.androidsdk.rest.client.UrlPostTask.IPostTaskListener;
import org.matrix.androidsdk.rest.model.CreatedEvent;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.Event.SentState;
import org.matrix.androidsdk.rest.model.PowerLevels;
import org.matrix.androidsdk.rest.model.ReceiptData;
import org.matrix.androidsdk.rest.model.RoomDirectoryVisibility;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.TokensChunkEvents;
import org.matrix.androidsdk.rest.model.UserIdAndReason;
import org.matrix.androidsdk.rest.model.message.FileInfo;
import org.matrix.androidsdk.rest.model.message.FileMessage;
import org.matrix.androidsdk.rest.model.message.ImageInfo;
import org.matrix.androidsdk.rest.model.message.ImageMessage;
import org.matrix.androidsdk.rest.model.message.LocationMessage;
import org.matrix.androidsdk.rest.model.message.Message;
import org.matrix.androidsdk.rest.model.message.ThumbnailInfo;
import org.matrix.androidsdk.rest.model.message.VideoInfo;
import org.matrix.androidsdk.rest.model.message.VideoMessage;
import org.matrix.androidsdk.rest.model.sync.AccountDataElement;
import org.matrix.androidsdk.rest.model.sync.InvitedRoomSync;
import org.matrix.androidsdk.rest.model.sync.RoomResponse;
import org.matrix.androidsdk.rest.model.sync.RoomSync;

public class Room implements CryptoRoom {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = Room.class.getSimpleName();
    private RoomAccountData mAccountData = new RoomAccountData();
    private String mCallConferenceUserId;
    /* access modifiers changed from: private */
    public MXDataHandler mDataHandler;
    /* access modifiers changed from: private */
    public final MXEventListener mEncryptionListener = new MXEventListener() {
        public void onLiveEvent(Event event, RoomState roomState) {
            if (TextUtils.equals(event.getType(), "m.room.encryption") && Room.this.mRoomEncryptionCallback != null) {
                Room.this.mRoomEncryptionCallback.onSuccess(null);
                Room.this.mRoomEncryptionCallback = null;
            }
        }
    };
    private final Map<IMXEventListener, IMXEventListener> mEventListeners = new HashMap();
    /* access modifiers changed from: private */
    public boolean mIsLeaving = false;
    private boolean mIsLeft;
    private boolean mIsReady = false;
    private boolean mIsSyncing;
    /* access modifiers changed from: private */
    public final Map<String, Event> mMemberEventByEventId = new HashMap();
    /* access modifiers changed from: private */
    public String mMyUserId = null;
    private ApiCallback<Void> mOnInitialSyncCallback;
    private boolean mRefreshUnreadAfterSync = false;
    private final RoomAvatarResolver mRoomAvatarResolver;
    private final RoomDisplayNameResolver mRoomDisplayNameResolver;
    /* access modifiers changed from: private */
    public ApiCallback<Void> mRoomEncryptionCallback;
    private RoomMediaMessagesSender mRoomMediaMessagesSender;
    private IMXStore mStore;
    private EventTimeline mTimeline;
    private final List<String> mTypingUsers = new ArrayList();

    private class RoomInfoUpdateCallback<T> extends SimpleApiCallback<T> {
        private final ApiCallback<T> mCallback;

        public RoomInfoUpdateCallback(ApiCallback<T> apiCallback) {
            super((ApiFailureCallback) apiCallback);
            this.mCallback = apiCallback;
        }

        public void onSuccess(T t) {
            Room.this.getStore().storeLiveStateForRoom(Room.this.getRoomId());
            ApiCallback<T> apiCallback = this.mCallback;
            if (apiCallback != null) {
                apiCallback.onSuccess(t);
            }
        }
    }

    public Room(MXDataHandler mXDataHandler, IMXStore iMXStore, String str) {
        this.mDataHandler = mXDataHandler;
        this.mStore = iMXStore;
        this.mMyUserId = this.mDataHandler.getUserId();
        this.mTimeline = EventTimelineFactory.liveTimeline(this.mDataHandler, iMXStore, this, str);
        this.mRoomDisplayNameResolver = new RoomDisplayNameResolver(this);
        this.mRoomAvatarResolver = new RoomAvatarResolver(this);
    }

    public MXDataHandler getDataHandler() {
        return this.mDataHandler;
    }

    public IMXStore getStore() {
        if (this.mStore == null) {
            MXDataHandler mXDataHandler = this.mDataHandler;
            if (mXDataHandler != null) {
                this.mStore = mXDataHandler.getStore(getRoomId());
            }
            if (this.mStore == null) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getStore() : cannot retrieve the store of ");
                sb.append(getRoomId());
                Log.e(str, sb.toString());
            }
        }
        return this.mStore;
    }

    public boolean shouldEncryptForInvitedMembers() {
        return !TextUtils.equals(getState().history_visibility, RoomState.HISTORY_VISIBILITY_JOINED);
    }

    public boolean isConferenceUserRoom() {
        return getState().isConferenceUserRoom();
    }

    public void setIsConferenceUserRoom(boolean z) {
        getState().setIsConferenceUserRoom(z);
    }

    public boolean isOngoingConferenceCall() {
        RoomMember member = getState().getMember(MXCallsManager.getConferenceUserId(getRoomId()));
        return member != null && TextUtils.equals(member.membership, "join");
    }

    public void setIsLeft(boolean z) {
        this.mIsLeft = z;
        this.mTimeline.setIsHistorical(z);
    }

    public boolean isLeft() {
        return this.mIsLeft;
    }

    private void handleEphemeralEvents(List<Event> list) {
        Collection collection;
        for (Event event : list) {
            event.roomId = getRoomId();
            try {
                if (Event.EVENT_TYPE_RECEIPT.equals(event.getType())) {
                    if (event.roomId != null) {
                        List handleReceiptEvent = handleReceiptEvent(event);
                        if (handleReceiptEvent != null && !handleReceiptEvent.isEmpty()) {
                            this.mDataHandler.onReceiptEvent(event.roomId, handleReceiptEvent);
                        }
                    }
                } else if (Event.EVENT_TYPE_TYPING.equals(event.getType())) {
                    JsonObject contentAsJsonObject = event.getContentAsJsonObject();
                    if (contentAsJsonObject.has("user_ids")) {
                        synchronized (this.mTypingUsers) {
                            this.mTypingUsers.clear();
                            try {
                                collection = (List) JsonUtils.getBasicGson().fromJson(contentAsJsonObject.get("user_ids"), new TypeToken<List<String>>() {
                                }.getType());
                            } catch (Exception e) {
                                String str = LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## handleEphemeralEvents() : exception ");
                                sb.append(e.getMessage());
                                Log.e(str, sb.toString(), e);
                                collection = null;
                            }
                            if (collection != null) {
                                this.mTypingUsers.addAll(collection);
                            }
                        }
                    }
                    this.mDataHandler.onLiveEvent(event, getState());
                } else {
                    continue;
                }
            } catch (Exception e2) {
                String str2 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("ephemeral event failed ");
                sb2.append(e2.getMessage());
                Log.e(str2, sb2.toString(), e2);
            }
        }
    }

    public void handleJoinedRoomSync(RoomSync roomSync, boolean z) {
        if (this.mOnInitialSyncCallback != null) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("initial sync handleJoinedRoomSync ");
            sb.append(getRoomId());
            Log.d(str, sb.toString());
        } else {
            String str2 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("handleJoinedRoomSync ");
            sb2.append(getRoomId());
            Log.d(str2, sb2.toString());
        }
        this.mIsSyncing = true;
        synchronized (this) {
            this.mTimeline.handleJoinedRoomSync(roomSync, z);
            RoomSummary roomSummary = getRoomSummary();
            if (roomSummary != null) {
                roomSummary.setIsJoined();
            }
            if (!(roomSync.ephemeral == null || roomSync.ephemeral.events == null)) {
                handleEphemeralEvents(roomSync.ephemeral.events);
            }
            if (!(roomSync.accountData == null || roomSync.accountData.events == null || roomSync.accountData.events.size() <= 0)) {
                if (z) {
                    String str3 = LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("## handleJoinedRoomSync : received ");
                    sb3.append(roomSync.accountData.events.size());
                    sb3.append(" account data events");
                    Log.d(str3, sb3.toString());
                }
                handleRoomAccountDataEvents(roomSync.accountData.events);
            }
        }
        if (this.mOnInitialSyncCallback != null && isJoined()) {
            String str4 = LOG_TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("handleJoinedRoomSync ");
            sb4.append(getRoomId());
            sb4.append(" :  the initial sync is done");
            Log.d(str4, sb4.toString());
            final ApiCallback<Void> apiCallback = this.mOnInitialSyncCallback;
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    Room.this.markAllAsRead(null);
                    try {
                        apiCallback.onSuccess(null);
                    } catch (Exception e) {
                        String access$000 = Room.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("handleJoinedRoomSync : onSuccess failed");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            });
            this.mOnInitialSyncCallback = null;
        }
        this.mIsSyncing = false;
        if (this.mRefreshUnreadAfterSync) {
            if (!z) {
                refreshUnreadCounter();
            }
            this.mRefreshUnreadAfterSync = false;
        }
    }

    public void handleInvitedRoomSync(InvitedRoomSync invitedRoomSync) {
        this.mTimeline.handleInvitedRoomSync(invitedRoomSync);
        RoomSummary roomSummary = getRoomSummary();
        if (roomSummary != null) {
            roomSummary.setIsInvited();
        }
    }

    public void storeOutgoingEvent(Event event) {
        this.mTimeline.storeOutgoingEvent(event);
    }

    public void requestServerRoomHistory(String str, int i, final ApiCallback<TokensChunkEvents> apiCallback) {
        this.mDataHandler.getDataRetriever().requestServerRoomHistory(getRoomId(), str, i, this.mDataHandler.getPaginationFilter(), new SimpleApiCallback<TokensChunkEvents>(apiCallback) {
            public void onSuccess(TokensChunkEvents tokensChunkEvents) {
                apiCallback.onSuccess(tokensChunkEvents);
            }
        });
    }

    public void cancelRemoteHistoryRequest() {
        this.mDataHandler.getDataRetriever().cancelRemoteHistoryRequest(getRoomId());
    }

    public String getRoomId() {
        return getState().roomId;
    }

    public void setAccountData(RoomAccountData roomAccountData) {
        this.mAccountData = roomAccountData;
    }

    public RoomAccountData getAccountData() {
        return this.mAccountData;
    }

    public RoomState getState() {
        return this.mTimeline.getState();
    }

    public boolean isLeaving() {
        return this.mIsLeaving;
    }

    public void getMembersAsync(ApiCallback<List<RoomMember>> apiCallback) {
        getState().getMembersAsync(apiCallback);
    }

    public void getDisplayableMembersAsync(ApiCallback<List<RoomMember>> apiCallback) {
        getState().getDisplayableMembersAsync(apiCallback);
    }

    public EventTimeline getTimeline() {
        return this.mTimeline;
    }

    public void setTimeline(EventTimeline eventTimeline) {
        this.mTimeline = eventTimeline;
    }

    public void setReadyState(boolean z) {
        this.mIsReady = z;
    }

    public boolean isReady() {
        return this.mIsReady;
    }

    public void getActiveMembersAsync(final ApiCallback<List<RoomMember>> apiCallback) {
        getMembersAsync(new SimpleApiCallback<List<RoomMember>>(apiCallback) {
            public void onSuccess(List<RoomMember> list) {
                ArrayList arrayList = new ArrayList();
                String conferenceUserId = MXCallsManager.getConferenceUserId(Room.this.getRoomId());
                for (RoomMember roomMember : list) {
                    if (!TextUtils.equals(roomMember.getUserId(), conferenceUserId) && (TextUtils.equals(roomMember.membership, "join") || TextUtils.equals(roomMember.membership, "invite"))) {
                        arrayList.add(roomMember);
                    }
                }
                apiCallback.onSuccess(arrayList);
            }
        });
    }

    public void getActiveMembersAsyncCrypto(final ApiCallback<List<CryptoRoomMember>> apiCallback) {
        getActiveMembersAsync(new SimpleApiCallback<List<RoomMember>>(apiCallback) {
            public void onSuccess(List<RoomMember> list) {
                apiCallback.onSuccess(new ArrayList(list));
            }
        });
    }

    public void getJoinedMembersAsync(final ApiCallback<List<RoomMember>> apiCallback) {
        getMembersAsync(new SimpleApiCallback<List<RoomMember>>(apiCallback) {
            public void onSuccess(List<RoomMember> list) {
                ArrayList arrayList = new ArrayList();
                for (RoomMember roomMember : list) {
                    if (TextUtils.equals(roomMember.membership, "join")) {
                        arrayList.add(roomMember);
                    }
                }
                apiCallback.onSuccess(arrayList);
            }
        });
    }

    public void getJoinedMembersAsyncCrypto(final ApiCallback<List<CryptoRoomMember>> apiCallback) {
        getJoinedMembersAsync(new SimpleApiCallback<List<RoomMember>>(apiCallback) {
            public void onSuccess(List<RoomMember> list) {
                apiCallback.onSuccess(new ArrayList(list));
            }
        });
    }

    public RoomMember getMember(String str) {
        return getState().getMember(str);
    }

    public void getMemberEvent(String str, final ApiCallback<Event> apiCallback) {
        final Event event;
        RoomMember member = getMember(str);
        if (member == null || member.getOriginalEventId() == null) {
            event = null;
        } else {
            event = (Event) this.mMemberEventByEventId.get(member.getOriginalEventId());
            if (event == null) {
                this.mDataHandler.getDataRetriever().getRoomsRestClient().getEvent(getRoomId(), member.getOriginalEventId(), new SimpleApiCallback<Event>(apiCallback) {
                    public void onSuccess(Event event) {
                        if (event != null) {
                            Room.this.mMemberEventByEventId.put(event.eventId, event);
                        }
                        apiCallback.onSuccess(event);
                    }
                });
                return;
            }
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                apiCallback.onSuccess(event);
            }
        });
    }

    public String getRoomDisplayName(Context context) {
        return this.mRoomDisplayNameResolver.resolve(context);
    }

    public String getRoomDisplayName(Context context, String str) {
        return this.mRoomDisplayNameResolver.resolve(context, str);
    }

    public String getTopic() {
        return getState().topic;
    }

    public String getVisibility() {
        return getState().visibility;
    }

    public boolean isInvited() {
        if (getRoomSummary() == null) {
            return false;
        }
        return getRoomSummary().isInvited();
    }

    public boolean isJoined() {
        if (getRoomSummary() == null) {
            return false;
        }
        return getRoomSummary().isJoined();
    }

    public boolean isMember() {
        return isJoined() || isInvited();
    }

    public boolean isDirectChatInvitation() {
        if (isInvited()) {
            RoomMember member = getState().getMember(this.mMyUserId);
            if (!(member == null || member.isDirect == null)) {
                return member.isDirect.booleanValue();
            }
        }
        return false;
    }

    public void setOnInitialSyncCallback(ApiCallback<Void> apiCallback) {
        this.mOnInitialSyncCallback = apiCallback;
    }

    public void joinWithThirdPartySigned(final String str, String str2, final ApiCallback<Void> apiCallback) {
        if (str2 == null) {
            join(str, apiCallback);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append("&mxid=");
        sb.append(this.mMyUserId);
        String sb2 = sb.toString();
        UrlPostTask urlPostTask = new UrlPostTask();
        urlPostTask.setListener(new IPostTaskListener() {
            public void onSucceed(JsonObject jsonObject) {
                Object obj;
                try {
                    obj = (Map) JsonUtils.getBasicGson().fromJson((JsonElement) jsonObject, new TypeToken<Map<String, Object>>() {
                    }.getType());
                } catch (Exception e) {
                    String access$000 = Room.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("joinWithThirdPartySigned :  Gson().fromJson failed");
                    sb.append(e.getMessage());
                    Log.e(access$000, sb.toString(), e);
                    obj = null;
                }
                if (obj != null) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("third_party_signed", obj);
                    Room.this.join(str, null, hashMap, apiCallback);
                    return;
                }
                Room.this.join(apiCallback);
            }

            public void onError(String str) {
                String access$000 = Room.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("joinWithThirdPartySigned failed ");
                sb.append(str);
                Log.d(access$000, sb.toString());
                Room.this.join(apiCallback);
            }
        });
        try {
            urlPostTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{sb2});
        } catch (Exception e) {
            urlPostTask.cancel(true);
            String str3 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("joinWithThirdPartySigned : task.executeOnExecutor failed");
            sb3.append(e.getMessage());
            Log.e(str3, sb3.toString(), e);
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

    public void join(ApiCallback<Void> apiCallback) {
        join(null, null, null, apiCallback);
    }

    private void join(String str, ApiCallback<Void> apiCallback) {
        join(str, null, null, apiCallback);
    }

    public void join(String str, List<String> list, Map<String, Object> map, ApiCallback<Void> apiCallback) {
        String str2;
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Join the room ");
        sb.append(getRoomId());
        sb.append(" with alias ");
        sb.append(str);
        Log.d(str3, sb.toString());
        RoomsRestClient roomsRestClient = this.mDataHandler.getDataRetriever().getRoomsRestClient();
        if (str != null) {
            str2 = str;
        } else {
            str2 = getRoomId();
        }
        final ApiCallback<Void> apiCallback2 = apiCallback;
        final String str4 = str;
        final List<String> list2 = list;
        final Map<String, Object> map2 = map;
        AnonymousClass12 r2 = new SimpleApiCallback<RoomResponse>(apiCallback) {
            public void onSuccess(RoomResponse roomResponse) {
                try {
                    String str = "the room ";
                    if (!Room.this.isJoined()) {
                        String access$000 = Room.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(Room.this.getRoomId());
                        sb.append(" is joined but wait after initial sync");
                        Log.d(access$000, sb.toString());
                        Room.this.setOnInitialSyncCallback(apiCallback2);
                        return;
                    }
                    String access$0002 = Room.LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(Room.this.getRoomId());
                    sb2.append(" is joined : the initial sync has been done");
                    Log.d(access$0002, sb2.toString());
                    Room.this.markAllAsRead(null);
                    apiCallback2.onSuccess(null);
                } catch (Exception e) {
                    String access$0003 = Room.LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("join exception ");
                    sb3.append(e.getMessage());
                    Log.e(access$0003, sb3.toString(), e);
                }
            }

            public void onNetworkError(Exception exc) {
                String access$000 = Room.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("join onNetworkError ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                apiCallback2.onNetworkError(exc);
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$000 = Room.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("join onMatrixError ");
                sb.append(matrixError.getMessage());
                Log.e(access$000, sb.toString());
                if (MatrixError.UNKNOWN.equals(matrixError.errcode)) {
                    if (TextUtils.equals("No known servers", matrixError.error)) {
                        matrixError.error = Room.this.getStore().getContext().getString(R.string.room_error_join_failed_empty_room);
                    }
                }
                if (matrixError.mStatus.intValue() != 404 || TextUtils.isEmpty(str4)) {
                    apiCallback2.onMatrixError(matrixError);
                    return;
                }
                Log.e(Room.LOG_TAG, "Retry without the room alias");
                Room.this.join(null, list2, map2, apiCallback2);
            }

            public void onUnexpectedError(Exception exc) {
                String access$000 = Room.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("join onUnexpectedError ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                apiCallback2.onUnexpectedError(exc);
            }
        };
        roomsRestClient.joinRoom(str2, list, map, r2);
    }

    public void updateUserPowerLevels(String str, int i, ApiCallback<Void> apiCallback) {
        PowerLevels deepCopy = getState().getPowerLevels().deepCopy();
        deepCopy.setUserPowerLevel(str, i);
        this.mDataHandler.getDataRetriever().getRoomsRestClient().updatePowerLevels(getRoomId(), deepCopy, apiCallback);
    }

    public void updateName(final String str, ApiCallback<Void> apiCallback) {
        this.mDataHandler.getDataRetriever().getRoomsRestClient().updateRoomName(getRoomId(), str, new RoomInfoUpdateCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                Room.this.getState().name = str;
                super.onSuccess(voidR);
            }
        });
    }

    public void updateTopic(final String str, ApiCallback<Void> apiCallback) {
        this.mDataHandler.getDataRetriever().getRoomsRestClient().updateTopic(getRoomId(), str, new RoomInfoUpdateCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                Room.this.getState().topic = str;
                super.onSuccess(voidR);
            }
        });
    }

    public void updateCanonicalAlias(final String str, ApiCallback<Void> apiCallback) {
        this.mDataHandler.getDataRetriever().getRoomsRestClient().updateCanonicalAlias(getRoomId(), TextUtils.isEmpty(str) ? null : str, new RoomInfoUpdateCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                Room.this.getState().setCanonicalAlias(str);
                super.onSuccess(voidR);
            }
        });
    }

    public List<String> getAliases() {
        return getState().getAliases();
    }

    public void removeAlias(final String str, ApiCallback<Void> apiCallback) {
        ArrayList arrayList = new ArrayList(getAliases());
        if (TextUtils.isEmpty(str) || arrayList.indexOf(str) < 0) {
            if (apiCallback != null) {
                apiCallback.onSuccess(null);
            }
            return;
        }
        this.mDataHandler.getDataRetriever().getRoomsRestClient().removeRoomAlias(str, new RoomInfoUpdateCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                Room.this.getState().removeAlias(str);
                super.onSuccess(voidR);
            }
        });
    }

    public void addAlias(final String str, ApiCallback<Void> apiCallback) {
        ArrayList arrayList = new ArrayList(getAliases());
        if (TextUtils.isEmpty(str) || arrayList.indexOf(str) >= 0) {
            if (apiCallback != null) {
                apiCallback.onSuccess(null);
            }
            return;
        }
        this.mDataHandler.getDataRetriever().getRoomsRestClient().setRoomIdByAlias(getRoomId(), str, new RoomInfoUpdateCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                Room.this.getState().addAlias(str);
                super.onSuccess(voidR);
            }
        });
    }

    public void addRelatedGroup(String str, ApiCallback<Void> apiCallback) {
        ArrayList arrayList = new ArrayList(getState().getRelatedGroups());
        if (!arrayList.contains(str)) {
            arrayList.add(str);
        }
        updateRelatedGroups(arrayList, apiCallback);
    }

    public void removeRelatedGroup(String str, ApiCallback<Void> apiCallback) {
        ArrayList arrayList = new ArrayList(getState().getRelatedGroups());
        arrayList.remove(str);
        updateRelatedGroups(arrayList, apiCallback);
    }

    public void updateRelatedGroups(final List<String> list, final ApiCallback<Void> apiCallback) {
        HashMap hashMap = new HashMap();
        hashMap.put("groups", list);
        this.mDataHandler.getDataRetriever().getRoomsRestClient().sendStateEvent(getRoomId(), Event.EVENT_TYPE_STATE_RELATED_GROUPS, null, hashMap, new SimpleApiCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                Room.this.getState().groups = list;
                Room.this.getDataHandler().getStore().storeLiveStateForRoom(Room.this.getRoomId());
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onSuccess(null);
                }
            }
        });
    }

    public String getAvatarUrl() {
        return this.mRoomAvatarResolver.resolve();
    }

    public String getCallAvatarUrl() {
        if (getNumberOfMembers() != 2 || getState().getLoadedMembers().size() <= 1) {
            return getAvatarUrl();
        }
        RoomMember roomMember = (RoomMember) getState().getLoadedMembers().get(0);
        RoomMember roomMember2 = (RoomMember) getState().getLoadedMembers().get(1);
        if (TextUtils.equals(this.mMyUserId, roomMember.getUserId())) {
            return roomMember2.getAvatarUrl();
        }
        return roomMember.getAvatarUrl();
    }

    public void updateAvatarUrl(final String str, ApiCallback<Void> apiCallback) {
        this.mDataHandler.getDataRetriever().getRoomsRestClient().updateAvatarUrl(getRoomId(), str, new RoomInfoUpdateCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                Room.this.getState().url = str;
                super.onSuccess(voidR);
            }
        });
    }

    public void updateHistoryVisibility(final String str, ApiCallback<Void> apiCallback) {
        this.mDataHandler.getDataRetriever().getRoomsRestClient().updateHistoryVisibility(getRoomId(), str, new RoomInfoUpdateCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                Room.this.getState().history_visibility = str;
                super.onSuccess(voidR);
            }
        });
    }

    public void updateDirectoryVisibility(final String str, ApiCallback<Void> apiCallback) {
        this.mDataHandler.getDataRetriever().getRoomsRestClient().updateDirectoryVisibility(getRoomId(), str, new RoomInfoUpdateCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                Room.this.getState().visibility = str;
                super.onSuccess(voidR);
            }
        });
    }

    public void getDirectoryVisibility(String str, final ApiCallback<String> apiCallback) {
        RoomsRestClient roomsRestClient = this.mDataHandler.getDataRetriever().getRoomsRestClient();
        if (roomsRestClient != null) {
            roomsRestClient.getDirectoryVisibility(str, new SimpleApiCallback<RoomDirectoryVisibility>(apiCallback) {
                public void onSuccess(RoomDirectoryVisibility roomDirectoryVisibility) {
                    RoomState state = Room.this.getState();
                    if (state != null) {
                        state.visibility = roomDirectoryVisibility.visibility;
                    }
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onSuccess(roomDirectoryVisibility.visibility);
                    }
                }
            });
        }
    }

    public void updateJoinRules(final String str, ApiCallback<Void> apiCallback) {
        this.mDataHandler.getDataRetriever().getRoomsRestClient().updateJoinRules(getRoomId(), str, new RoomInfoUpdateCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                Room.this.getState().join_rule = str;
                super.onSuccess(voidR);
            }
        });
    }

    public void updateGuestAccess(final String str, ApiCallback<Void> apiCallback) {
        this.mDataHandler.getDataRetriever().getRoomsRestClient().updateGuestAccess(getRoomId(), str, new RoomInfoUpdateCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                Room.this.getState().guest_access = str;
                super.onSuccess(voidR);
            }
        });
    }

    private String getCallConferenceUserId() {
        if (this.mCallConferenceUserId == null) {
            this.mCallConferenceUserId = MXCallsManager.getConferenceUserId(getRoomId());
        }
        return this.mCallConferenceUserId;
    }

    public boolean handleReceiptData(ReceiptData receiptData) {
        if (TextUtils.equals(receiptData.userId, getCallConferenceUserId()) || getStore() == null) {
            return false;
        }
        boolean storeReceipt = getStore().storeReceipt(receiptData, getRoomId());
        if (storeReceipt && TextUtils.equals(this.mMyUserId, receiptData.userId)) {
            RoomSummary summary = getStore().getSummary(getRoomId());
            if (summary != null) {
                summary.setReadReceiptEventId(receiptData.eventId);
                getStore().flushSummary(summary);
            }
            refreshUnreadCounter();
        }
        return storeReceipt;
    }

    private List<String> handleReceiptEvent(Event event) {
        ArrayList arrayList = new ArrayList();
        try {
            Map map = (Map) JsonUtils.getGson(false).fromJson(event.getContent(), new TypeToken<Map<String, Map<String, Map<String, Map<String, Object>>>>>() {
            }.getType());
            if (map != null) {
                for (String str : map.keySet()) {
                    Map map2 = (Map) map.get(str);
                    for (String str2 : map2.keySet()) {
                        if (TextUtils.equals(str2, "m.read")) {
                            Map map3 = (Map) map2.get(str2);
                            for (String str3 : map3.keySet()) {
                                Map map4 = (Map) map3.get(str3);
                                for (String str4 : map4.keySet()) {
                                    if (TextUtils.equals("ts", str4) && handleReceiptData(new ReceiptData(str3, str, ((Double) map4.get(str4)).longValue()))) {
                                        arrayList.add(str3);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Log.w(LOG_TAG, "receiptsDict is null");
            }
        } catch (Exception e) {
            String str5 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("handleReceiptEvent : failed: ");
            sb.append(e.getMessage());
            Log.e(str5, sb.toString(), e);
        }
        return arrayList;
    }

    private void clearUnreadCounters(RoomSummary roomSummary) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## clearUnreadCounters ");
        sb.append(getRoomId());
        Log.d(str, sb.toString());
        getState().setHighlightCount(0);
        getState().setNotificationCount(0);
        if (getStore() != null) {
            getStore().storeLiveStateForRoom(getRoomId());
            if (roomSummary != null) {
                roomSummary.setUnreadEventsCount(0);
                roomSummary.setHighlightCount(0);
                roomSummary.setNotificationCount(0);
                getStore().flushSummary(roomSummary);
            }
            getStore().commit();
        }
    }

    public String getReadMarkerEventId() {
        if (getStore() == null) {
            return null;
        }
        RoomSummary summary = getStore().getSummary(getRoomId());
        if (summary == null) {
            return null;
        }
        return summary.getReadMarkerEventId() != null ? summary.getReadMarkerEventId() : summary.getReadReceiptEventId();
    }

    public boolean markAllAsRead(ApiCallback<Void> apiCallback) {
        return markAllAsRead(true, apiCallback);
    }

    /* access modifiers changed from: private */
    public boolean markAllAsRead(boolean z, ApiCallback<Void> apiCallback) {
        Event latestEvent = getStore() != null ? getStore().getLatestEvent(getRoomId()) : null;
        String str = z ? latestEvent != null ? latestEvent.eventId : null : getReadMarkerEventId();
        boolean sendReadMarkers = sendReadMarkers(str, null, apiCallback);
        if (!sendReadMarkers) {
            RoomSummary summary = getStore() != null ? getStore().getSummary(getRoomId()) : null;
            if (summary == null) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## sendReadReceipt() : no summary for ");
                sb.append(getRoomId());
                Log.e(str2, sb.toString());
            } else if (!(summary.getUnreadEventsCount() == 0 && summary.getHighlightCount() == 0 && summary.getNotificationCount() == 0)) {
                String str3 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## markAllAsRead() : the summary events counters should be cleared for ");
                sb2.append(getRoomId());
                Log.e(str3, sb2.toString());
                Event latestEvent2 = getStore().getLatestEvent(getRoomId());
                summary.setLatestReceivedEvent(latestEvent2);
                if (latestEvent2 != null) {
                    summary.setReadReceiptEventId(latestEvent2.eventId);
                } else {
                    summary.setReadReceiptEventId(null);
                }
                summary.setUnreadEventsCount(0);
                summary.setHighlightCount(0);
                summary.setNotificationCount(0);
                getStore().flushSummary(summary);
            }
            if (!(getState().getNotificationCount() == 0 && getState().getHighlightCount() == 0)) {
                String str4 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("## markAllAsRead() : the notification messages count for ");
                sb3.append(getRoomId());
                sb3.append(" should have been cleared");
                Log.e(str4, sb3.toString());
                getState().setNotificationCount(0);
                getState().setHighlightCount(0);
                if (getStore() != null) {
                    getStore().storeLiveStateForRoom(getRoomId());
                }
            }
        }
        return sendReadMarkers;
    }

    public void setReadMakerEventId(String str) {
        RoomSummary summary = getStore() != null ? getStore().getSummary(getRoomId()) : null;
        if (summary != null && !str.equals(summary.getReadMarkerEventId())) {
            sendReadMarkers(str, summary.getReadReceiptEventId(), null);
        }
    }

    public void sendReadReceipt() {
        markAllAsRead(false, null);
    }

    public boolean sendReadReceipt(Event event, ApiCallback<Void> apiCallback) {
        String str = event != null ? event.eventId : null;
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## sendReadReceipt() : eventId ");
        sb.append(str);
        sb.append(" in room ");
        sb.append(getRoomId());
        Log.d(str2, sb.toString());
        return sendReadMarkers(null, str, apiCallback);
    }

    public void forgetReadMarker(ApiCallback<Void> apiCallback) {
        String str = null;
        RoomSummary summary = getStore() != null ? getStore().getSummary(getRoomId()) : null;
        if (summary != null) {
            str = summary.getReadReceiptEventId();
        }
        if (summary != null) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## forgetReadMarker() : update the read marker to ");
            sb.append(str);
            sb.append(" in room ");
            sb.append(getRoomId());
            Log.d(str2, sb.toString());
            summary.setReadMarkerEventId(str);
            getStore().flushSummary(summary);
        }
        setReadMarkers(str, str, apiCallback);
    }

    public boolean sendReadMarkers(String str, String str2, ApiCallback<Void> apiCallback) {
        Event latestEvent = getStore() != null ? getStore().getLatestEvent(getRoomId()) : null;
        boolean z = false;
        if (latestEvent == null) {
            Log.e(LOG_TAG, "## sendReadMarkers(): no last event");
            return false;
        }
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## sendReadMarkers(): readMarkerEventId ");
        sb.append(str);
        sb.append(" readReceiptEventId ");
        sb.append(str2);
        String str4 = " in room ";
        sb.append(str4);
        sb.append(getRoomId());
        Log.d(str3, sb.toString());
        if (!TextUtils.isEmpty(str)) {
            if (!MXPatterns.isEventId(str)) {
                String str5 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## sendReadMarkers() : invalid event id ");
                sb2.append(str);
                Log.e(str5, sb2.toString());
                str = null;
            } else {
                RoomSummary summary = getStore().getSummary(getRoomId());
                if (summary != null && !TextUtils.equals(str, summary.getReadMarkerEventId())) {
                    Event event = getStore().getEvent(str, getRoomId());
                    Event event2 = getStore().getEvent(summary.getReadMarkerEventId(), getRoomId());
                    if (event == null || event2 == null || event.getOriginServerTs() > event2.getOriginServerTs()) {
                        String str6 = LOG_TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("## sendReadMarkers(): set new read marker event id ");
                        sb3.append(str);
                        sb3.append(str4);
                        sb3.append(getRoomId());
                        Log.d(str6, sb3.toString());
                        summary.setReadMarkerEventId(str);
                        getStore().flushSummary(summary);
                        z = true;
                    }
                }
            }
        }
        if (str2 == null) {
            str2 = latestEvent.eventId;
        }
        if (getStore() != null && !getStore().isEventRead(getRoomId(), getDataHandler().getUserId(), str2) && handleReceiptData(new ReceiptData(this.mMyUserId, str2, System.currentTimeMillis()))) {
            if (TextUtils.equals(latestEvent.eventId, str2)) {
                clearUnreadCounters(getStore().getSummary(getRoomId()));
            }
            z = true;
        }
        if (z) {
            setReadMarkers(str, str2, apiCallback);
        }
        return z;
    }

    private void setReadMarkers(String str, String str2, final ApiCallback<Void> apiCallback) {
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## setReadMarkers(): readMarkerEventId ");
        sb.append(str);
        sb.append(" readReceiptEventId ");
        sb.append(str);
        Log.d(str3, sb.toString());
        if (!MXPatterns.isEventId(str)) {
            str = null;
        }
        if (!MXPatterns.isEventId(str2)) {
            str2 = null;
        }
        if (!TextUtils.isEmpty(str) || !TextUtils.isEmpty(str2)) {
            this.mDataHandler.getDataRetriever().getRoomsRestClient().sendReadMarker(getRoomId(), str, str2, new SimpleApiCallback<Void>(apiCallback) {
                public void onSuccess(Void voidR) {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onSuccess(voidR);
                    }
                }
            });
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onSuccess(null);
                    }
                }
            });
        }
    }

    public boolean isEventRead(String str) {
        if (getStore() != null) {
            return getStore().isEventRead(getRoomId(), this.mMyUserId, str);
        }
        return false;
    }

    public int getNotificationCount() {
        return getState().getNotificationCount();
    }

    public int getHighlightCount() {
        return getState().getHighlightCount();
    }

    public void refreshUnreadCounter() {
        if (!this.mIsSyncing) {
            RoomSummary summary = getStore() != null ? getStore().getSummary(getRoomId()) : null;
            if (summary != null) {
                int unreadEventsCount = summary.getUnreadEventsCount();
                int eventsCountAfter = getStore().eventsCountAfter(getRoomId(), summary.getReadReceiptEventId());
                if (unreadEventsCount != eventsCountAfter) {
                    summary.setUnreadEventsCount(eventsCountAfter);
                    getStore().flushSummary(summary);
                    return;
                }
                return;
            }
            return;
        }
        this.mRefreshUnreadAfterSync = true;
    }

    public List<String> getTypingUsers() {
        ArrayList arrayList;
        synchronized (this.mTypingUsers) {
            arrayList = new ArrayList(this.mTypingUsers);
        }
        return arrayList;
    }

    public void sendTypingNotification(boolean z, int i, ApiCallback<Void> apiCallback) {
        if (isJoined()) {
            this.mDataHandler.getDataRetriever().getRoomsRestClient().sendTypingNotification(getRoomId(), this.mMyUserId, z, i, apiCallback);
        }
    }

    public static void fillLocationInfo(Context context, LocationMessage locationMessage, Uri uri, String str) {
        if (uri != null) {
            try {
                locationMessage.thumbnail_url = uri.toString();
                ThumbnailInfo thumbnailInfo = new ThumbnailInfo();
                File file = new File(uri.getPath());
                ExifInterface exifInterface = new ExifInterface(uri.getPath());
                String attribute = exifInterface.getAttribute("ImageWidth");
                String attribute2 = exifInterface.getAttribute("ImageLength");
                if (attribute != null) {
                    thumbnailInfo.w = Integer.valueOf(Integer.parseInt(attribute));
                }
                if (attribute2 != null) {
                    thumbnailInfo.h = Integer.valueOf(Integer.parseInt(attribute2));
                }
                thumbnailInfo.size = Long.valueOf(file.length());
                thumbnailInfo.mimetype = str;
                locationMessage.thumbnail_info = thumbnailInfo;
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("fillLocationInfo : failed");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
    }

    public static void fillVideoInfo(Context context, VideoMessage videoMessage, Uri uri, String str, Uri uri2, String str2) {
        try {
            VideoInfo videoInfo = new VideoInfo();
            File file = new File(uri.getPath());
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
            Bitmap frameAtTime = mediaMetadataRetriever.getFrameAtTime();
            videoInfo.h = Integer.valueOf(frameAtTime.getHeight());
            videoInfo.w = Integer.valueOf(frameAtTime.getWidth());
            videoInfo.mimetype = str;
            try {
                MediaPlayer create = MediaPlayer.create(context, uri);
                if (create != null) {
                    videoInfo.duration = Long.valueOf((long) create.getDuration());
                    create.release();
                }
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("fillVideoInfo : MediaPlayer.create failed");
                sb.append(e.getMessage());
                Log.e(str3, sb.toString(), e);
            }
            videoInfo.size = Long.valueOf(file.length());
            if (uri2 != null) {
                videoInfo.thumbnail_url = uri2.toString();
                ThumbnailInfo thumbnailInfo = new ThumbnailInfo();
                File file2 = new File(uri2.getPath());
                ExifInterface exifInterface = new ExifInterface(uri2.getPath());
                String attribute = exifInterface.getAttribute("ImageWidth");
                String attribute2 = exifInterface.getAttribute("ImageLength");
                if (attribute != null) {
                    thumbnailInfo.w = Integer.valueOf(Integer.parseInt(attribute));
                }
                if (attribute2 != null) {
                    thumbnailInfo.h = Integer.valueOf(Integer.parseInt(attribute2));
                }
                thumbnailInfo.size = Long.valueOf(file2.length());
                thumbnailInfo.mimetype = str2;
                videoInfo.thumbnail_info = thumbnailInfo;
            }
            videoMessage.info = videoInfo;
        } catch (Exception e2) {
            String str4 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("fillVideoInfo : failed");
            sb2.append(e2.getMessage());
            Log.e(str4, sb2.toString(), e2);
        }
    }

    public static void fillFileInfo(Context context, FileMessage fileMessage, Uri uri, String str) {
        try {
            FileInfo fileInfo = new FileInfo();
            File file = new File(uri.getPath());
            fileInfo.mimetype = str;
            fileInfo.size = Long.valueOf(file.length());
            fileMessage.info = fileInfo;
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("fillFileInfo : failed");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString(), e);
        }
    }

    public static ImageInfo getImageInfo(Context context, ImageInfo imageInfo, Uri uri, String str) {
        int i;
        String str2 = "fillImageInfo : failed";
        if (imageInfo == null) {
            imageInfo = new ImageInfo();
        }
        try {
            String path = uri.getPath();
            File file = new File(path);
            ExifInterface exifInterface = new ExifInterface(path);
            String attribute = exifInterface.getAttribute("ImageWidth");
            String attribute2 = exifInterface.getAttribute("ImageLength");
            imageInfo.orientation = Integer.valueOf(ImageUtils.getOrientationForBitmap(context, uri));
            int i2 = 0;
            if (attribute == null || attribute2 == null) {
                i = 0;
            } else {
                if (!(imageInfo.orientation.intValue() == 5 || imageInfo.orientation.intValue() == 6 || imageInfo.orientation.intValue() == 7)) {
                    if (imageInfo.orientation.intValue() != 8) {
                        i2 = Integer.parseInt(attribute);
                        i = Integer.parseInt(attribute2);
                    }
                }
                int parseInt = Integer.parseInt(attribute2);
                i = Integer.parseInt(attribute);
                i2 = parseInt;
            }
            if (i2 == 0 || i == 0) {
                try {
                    Options options = new Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(uri.getPath(), options);
                    if (options.outHeight > 0 && options.outWidth > 0) {
                        i2 = options.outWidth;
                        i = options.outHeight;
                    }
                } catch (Exception e) {
                    String str3 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append(str2);
                    sb.append(e.getMessage());
                    Log.e(str3, sb.toString(), e);
                } catch (OutOfMemoryError e2) {
                    Log.e(LOG_TAG, "fillImageInfo : oom", e2);
                }
            }
            if (!(i2 == 0 && i == 0)) {
                imageInfo.w = Integer.valueOf(i2);
                imageInfo.h = Integer.valueOf(i);
            }
            imageInfo.mimetype = str;
            imageInfo.size = Long.valueOf(file.length());
            return imageInfo;
        } catch (Exception e3) {
            String str4 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(e3.getMessage());
            Log.e(str4, sb2.toString(), e3);
            return null;
        }
    }

    public static void fillImageInfo(Context context, ImageMessage imageMessage, Uri uri, String str) {
        imageMessage.info = getImageInfo(context, imageMessage.info, uri, str);
    }

    public static void fillThumbnailInfo(Context context, ImageMessage imageMessage, Uri uri, String str) {
        ImageInfo imageInfo = getImageInfo(context, null, uri, str);
        if (imageInfo != null) {
            if (imageMessage.info == null) {
                imageMessage.info = new ImageInfo();
            }
            imageMessage.info.thumbnailInfo = new ThumbnailInfo();
            imageMessage.info.thumbnailInfo.w = imageInfo.w;
            imageMessage.info.thumbnailInfo.h = imageInfo.h;
            imageMessage.info.thumbnailInfo.size = imageInfo.size;
            imageMessage.info.thumbnailInfo.mimetype = imageInfo.mimetype;
        }
    }

    public boolean canPerformCall() {
        return getNumberOfMembers() > 1;
    }

    public void callees(final ApiCallback<List<RoomMember>> apiCallback) {
        getMembersAsync(new SimpleApiCallback<List<RoomMember>>(apiCallback) {
            public void onSuccess(List<RoomMember> list) {
                ArrayList arrayList = new ArrayList();
                for (RoomMember roomMember : list) {
                    if ("join".equals(roomMember.membership) && !Room.this.mMyUserId.equals(roomMember.getUserId())) {
                        arrayList.add(roomMember);
                    }
                }
                apiCallback.onSuccess(arrayList);
            }
        });
    }

    private void handleRoomAccountDataEvents(List<Event> list) {
        if (list != null && list.size() > 0) {
            for (Event event : list) {
                String type = event.getType();
                RoomSummary summary = getStore() != null ? getStore().getSummary(getRoomId()) : null;
                if (!type.equals(Event.EVENT_TYPE_READ_MARKER)) {
                    this.mAccountData.handleTagEvent(event);
                    if (Event.EVENT_TYPE_TAGS.equals(event.getType())) {
                        if (summary != null) {
                            summary.setRoomTags(this.mAccountData.getKeys());
                            getStore().flushSummary(summary);
                        }
                        this.mDataHandler.onRoomTagEvent(getRoomId());
                    } else {
                        if (Event.EVENT_TYPE_URL_PREVIEW.equals(event.getType())) {
                            JsonObject contentAsJsonObject = event.getContentAsJsonObject();
                            if (contentAsJsonObject != null) {
                                String str = AccountDataElement.ACCOUNT_DATA_KEY_URL_PREVIEW_DISABLE;
                                if (contentAsJsonObject.has(str)) {
                                    boolean asBoolean = contentAsJsonObject.get(str).getAsBoolean();
                                    Set roomsWithoutURLPreviews = this.mDataHandler.getStore().getRoomsWithoutURLPreviews();
                                    if (asBoolean) {
                                        roomsWithoutURLPreviews.add(getRoomId());
                                    } else {
                                        roomsWithoutURLPreviews.remove(getRoomId());
                                    }
                                    this.mDataHandler.getStore().setRoomsWithoutURLPreview(roomsWithoutURLPreviews);
                                }
                            }
                        }
                    }
                } else if (summary != null) {
                    Event event2 = JsonUtils.toEvent(event.getContent());
                    if (event2 != null && !TextUtils.equals(event2.eventId, summary.getReadMarkerEventId())) {
                        String str2 = LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## handleRoomAccountDataEvents() : update the read marker to ");
                        sb.append(event2.eventId);
                        sb.append(" in room ");
                        sb.append(getRoomId());
                        Log.d(str2, sb.toString());
                        if (TextUtils.isEmpty(event2.eventId)) {
                            String str3 = LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("## handleRoomAccountDataEvents() : null event id ");
                            sb2.append(event.getContent());
                            Log.e(str3, sb2.toString());
                        }
                        summary.setReadMarkerEventId(event2.eventId);
                        getStore().flushSummary(summary);
                        this.mDataHandler.onReadMarkerEvent(getRoomId());
                    }
                }
            }
            if (getStore() != null) {
                getStore().storeRoomAccountData(getRoomId(), this.mAccountData);
            }
        }
    }

    /* access modifiers changed from: private */
    public void addTag(String str, Double d, ApiCallback<Void> apiCallback) {
        if (str != null && d != null) {
            this.mDataHandler.getDataRetriever().getRoomsRestClient().addTag(this.mMyUserId, getRoomId(), str, d, apiCallback);
        } else if (apiCallback != null) {
            apiCallback.onSuccess(null);
        }
    }

    private void removeTag(String str, ApiCallback<Void> apiCallback) {
        if (str != null) {
            this.mDataHandler.getDataRetriever().getRoomsRestClient().removeTag(this.mMyUserId, getRoomId(), str, apiCallback);
        } else if (apiCallback != null) {
            apiCallback.onSuccess(null);
        }
    }

    public void replaceTag(String str, String str2, Double d, ApiCallback<Void> apiCallback) {
        if (str != null && str2 == null) {
            removeTag(str, apiCallback);
        } else if ((str != null || str2 == null) && !TextUtils.equals(str, str2)) {
            final String str3 = str2;
            final Double d2 = d;
            final ApiCallback<Void> apiCallback2 = apiCallback;
            AnonymousClass29 r1 = new SimpleApiCallback<Void>(apiCallback) {
                public void onSuccess(Void voidR) {
                    Room.this.addTag(str3, d2, apiCallback2);
                }
            };
            removeTag(str, r1);
        } else {
            addTag(str2, d, apiCallback);
        }
    }

    public boolean isURLPreviewAllowedByUser() {
        return !getDataHandler().getStore().getRoomsWithoutURLPreviews().contains(getRoomId());
    }

    public void setIsURLPreviewAllowedByUser(boolean z, ApiCallback<Void> apiCallback) {
        this.mDataHandler.getDataRetriever().getRoomsRestClient().updateURLPreviewStatus(this.mMyUserId, getRoomId(), z, apiCallback);
    }

    public void addEventListener(IMXEventListener iMXEventListener) {
        if (iMXEventListener == null) {
            Log.e(LOG_TAG, "addEventListener : eventListener is null");
        } else if (this.mDataHandler == null) {
            Log.e(LOG_TAG, "addEventListener : mDataHandler is null");
        } else {
            MXRoomEventListener mXRoomEventListener = new MXRoomEventListener(this, iMXEventListener);
            this.mEventListeners.put(iMXEventListener, mXRoomEventListener);
            MXDataHandler mXDataHandler = this.mDataHandler;
            if (mXDataHandler != null) {
                mXDataHandler.addListener(mXRoomEventListener);
            }
        }
    }

    public void removeEventListener(IMXEventListener iMXEventListener) {
        if (iMXEventListener != null) {
            MXDataHandler mXDataHandler = this.mDataHandler;
            if (mXDataHandler != null) {
                mXDataHandler.removeListener((IMXEventListener) this.mEventListeners.get(iMXEventListener));
                this.mEventListeners.remove(iMXEventListener);
            }
        }
    }

    public void sendEvent(final Event event, final ApiCallback<Void> apiCallback) {
        JsonElement jsonElement = null;
        if (!this.mIsReady || !isJoined()) {
            this.mDataHandler.updateEventState(event, SentState.WAITING_RETRY);
            try {
                apiCallback.onNetworkError(null);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("sendEvent exception ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
            return;
        }
        final String str2 = event.eventId;
        AnonymousClass30 r7 = new ApiCallback<CreatedEvent>() {
            public void onSuccess(CreatedEvent createdEvent) {
                if (Room.this.getStore() != null) {
                    Room.this.getStore().deleteEvent(event);
                }
                boolean equals = TextUtils.equals(Room.this.getReadMarkerEventId(), event.eventId);
                event.eventId = createdEvent.eventId;
                event.originServerTs = System.currentTimeMillis();
                Room.this.mDataHandler.updateEventState(event, SentState.SENT);
                if (Room.this.getStore() != null && !Room.this.getStore().doesEventExist(createdEvent.eventId, Room.this.getRoomId())) {
                    Room.this.getStore().storeLiveRoomEvent(event);
                }
                Room.this.markAllAsRead(equals, null);
                if (Room.this.getStore() != null) {
                    Room.this.getStore().commit();
                }
                Room.this.mDataHandler.onEventSent(event, str2);
                try {
                    apiCallback.onSuccess(null);
                } catch (Exception e) {
                    String access$000 = Room.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("sendEvent exception ");
                    sb.append(e.getMessage());
                    Log.e(access$000, sb.toString(), e);
                }
            }

            public void onNetworkError(Exception exc) {
                event.unsentException = exc;
                Room.this.mDataHandler.updateEventState(event, SentState.UNDELIVERED);
                try {
                    apiCallback.onNetworkError(exc);
                } catch (Exception e) {
                    String access$000 = Room.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("sendEvent exception ");
                    sb.append(e.getMessage());
                    Log.e(access$000, sb.toString(), e);
                }
            }

            public void onMatrixError(MatrixError matrixError) {
                event.unsentMatrixError = matrixError;
                Room.this.mDataHandler.updateEventState(event, SentState.UNDELIVERED);
                if (MatrixError.isConfigurationErrorCode(matrixError.errcode)) {
                    Room.this.mDataHandler.onConfigurationError(matrixError.errcode);
                    return;
                }
                try {
                    apiCallback.onMatrixError(matrixError);
                } catch (Exception e) {
                    String access$000 = Room.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("sendEvent exception ");
                    sb.append(e.getMessage());
                    Log.e(access$000, sb.toString(), e);
                }
            }

            public void onUnexpectedError(Exception exc) {
                event.unsentException = exc;
                Room.this.mDataHandler.updateEventState(event, SentState.UNDELIVERED);
                try {
                    apiCallback.onUnexpectedError(exc);
                } catch (Exception e) {
                    String access$000 = Room.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("sendEvent exception ");
                    sb.append(e.getMessage());
                    Log.e(access$000, sb.toString(), e);
                }
            }
        };
        if (!isEncrypted() || this.mDataHandler.getCrypto() == null) {
            this.mDataHandler.updateEventState(event, SentState.SENDING);
            if (Event.EVENT_TYPE_MESSAGE.equals(event.getType())) {
                this.mDataHandler.getDataRetriever().getRoomsRestClient().sendMessage(event.eventId, getRoomId(), JsonUtils.toMessage(event.getContent()), r7);
            } else {
                this.mDataHandler.getDataRetriever().getRoomsRestClient().sendEventToRoom(event.eventId, getRoomId(), event.getType(), event.getContentAsJsonObject(), r7);
            }
        } else {
            this.mDataHandler.updateEventState(event, SentState.ENCRYPTING);
            JsonObject contentAsJsonObject = event.getContentAsJsonObject();
            if (contentAsJsonObject != null) {
                String str3 = "m.relates_to";
                if (contentAsJsonObject.has(str3)) {
                    jsonElement = contentAsJsonObject.get(str3);
                    contentAsJsonObject.remove(str3);
                }
            }
            final JsonElement jsonElement2 = jsonElement;
            MXCrypto crypto = this.mDataHandler.getCrypto();
            String type = event.getType();
            final Event event2 = event;
            final AnonymousClass30 r6 = r7;
            final ApiCallback<Void> apiCallback2 = apiCallback;
            AnonymousClass31 r2 = new ApiCallback<MXEncryptEventContentResult>() {
                public void onSuccess(MXEncryptEventContentResult mXEncryptEventContentResult) {
                    event2.type = mXEncryptEventContentResult.mEventType;
                    JsonObject asJsonObject = mXEncryptEventContentResult.mEventContent.getAsJsonObject();
                    JsonElement jsonElement = jsonElement2;
                    if (jsonElement != null) {
                        asJsonObject.add("m.relates_to", jsonElement);
                    }
                    event2.updateContent(asJsonObject);
                    Room.this.mDataHandler.decryptEvent(event2, null);
                    Room.this.mDataHandler.updateEventState(event2, SentState.SENDING);
                    Room.this.mDataHandler.getDataRetriever().getRoomsRestClient().sendEventToRoom(event2.eventId, Room.this.getRoomId(), mXEncryptEventContentResult.mEventType, mXEncryptEventContentResult.mEventContent.getAsJsonObject(), r6);
                }

                public void onNetworkError(Exception exc) {
                    event2.unsentException = exc;
                    Room.this.mDataHandler.updateEventState(event2, SentState.UNDELIVERED);
                    ApiCallback apiCallback = apiCallback2;
                    if (apiCallback != null) {
                        apiCallback.onNetworkError(exc);
                    }
                }

                public void onMatrixError(MatrixError matrixError) {
                    if (!(matrixError instanceof MXCryptoError) || !TextUtils.equals(((MXCryptoError) matrixError).errcode, MXCryptoError.UNKNOWN_DEVICES_CODE)) {
                        event2.mSentState = SentState.UNDELIVERED;
                    } else {
                        event2.mSentState = SentState.FAILED_UNKNOWN_DEVICES;
                    }
                    event2.unsentMatrixError = matrixError;
                    Room.this.mDataHandler.onEventSentStateUpdated(event2);
                    ApiCallback apiCallback = apiCallback2;
                    if (apiCallback != null) {
                        apiCallback.onMatrixError(matrixError);
                    }
                }

                public void onUnexpectedError(Exception exc) {
                    event2.unsentException = exc;
                    Room.this.mDataHandler.updateEventState(event2, SentState.UNDELIVERED);
                    ApiCallback apiCallback = apiCallback2;
                    if (apiCallback != null) {
                        apiCallback.onUnexpectedError(exc);
                    }
                }
            };
            crypto.encryptEventContent(contentAsJsonObject, type, this, r2);
        }
    }

    public void cancelEventSending(Event event) {
        if (event != null) {
            if (SentState.UNSENT == event.mSentState || SentState.SENDING == event.mSentState || SentState.WAITING_RETRY == event.mSentState || SentState.ENCRYPTING == event.mSentState) {
                this.mDataHandler.updateEventState(event, SentState.UNDELIVERED);
            }
            List<String> mediaUrls = event.getMediaUrls();
            MXMediaCache mediaCache = this.mDataHandler.getMediaCache();
            for (String str : mediaUrls) {
                mediaCache.cancelUpload(str);
                mediaCache.cancelDownload(mediaCache.downloadIdFromUrl(str));
            }
        }
    }

    public void redact(final String str, final ApiCallback<Event> apiCallback) {
        this.mDataHandler.getDataRetriever().getRoomsRestClient().redactEvent(getRoomId(), str, new SimpleApiCallback<Event>(apiCallback) {
            public void onSuccess(Event event) {
                Event event2 = Room.this.getStore() != null ? Room.this.getStore().getEvent(str, Room.this.getRoomId()) : null;
                if (event2 != null && (event2.unsigned == null || event2.unsigned.redacted_because == null)) {
                    event2.prune(null);
                    Room.this.getStore().storeLiveRoomEvent(event2);
                    Room.this.getStore().commit();
                }
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onSuccess(event2);
                }
            }
        });
    }

    public void report(String str, int i, String str2, ApiCallback<Void> apiCallback) {
        this.mDataHandler.getDataRetriever().getRoomsRestClient().reportEvent(getRoomId(), str, i, str2, apiCallback);
    }

    public void invite(String str, ApiCallback<Void> apiCallback) {
        if (str != null) {
            invite(Collections.singletonList(str), apiCallback);
        }
    }

    public void inviteByEmail(String str, ApiCallback<Void> apiCallback) {
        if (str != null) {
            invite(Collections.singletonList(str), apiCallback);
        }
    }

    public void invite(List<String> list, ApiCallback<Void> apiCallback) {
        if (list != null) {
            invite(list.iterator(), apiCallback);
        }
    }

    /* access modifiers changed from: private */
    public void invite(final Iterator<String> it, final ApiCallback<Void> apiCallback) {
        if (!it.hasNext()) {
            apiCallback.onSuccess(null);
            return;
        }
        AnonymousClass33 r0 = new SimpleApiCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                Room.this.invite(it, apiCallback);
            }
        };
        String str = (String) it.next();
        if (Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
            this.mDataHandler.getDataRetriever().getRoomsRestClient().inviteByEmailToRoom(getRoomId(), str, r0);
        } else {
            this.mDataHandler.getDataRetriever().getRoomsRestClient().inviteUserToRoom(getRoomId(), str, r0);
        }
    }

    public void leave(final ApiCallback<Void> apiCallback) {
        this.mIsLeaving = true;
        this.mDataHandler.onRoomInternalUpdate(getRoomId());
        this.mDataHandler.getDataRetriever().getRoomsRestClient().leaveRoom(getRoomId(), new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                if (Room.this.mDataHandler.isAlive()) {
                    Room.this.mIsLeaving = false;
                    Room.this.mDataHandler.deleteRoom(Room.this.getRoomId());
                    if (Room.this.getStore() != null) {
                        Log.d(Room.LOG_TAG, "leave : commit");
                        Room.this.getStore().commit();
                    }
                    try {
                        apiCallback.onSuccess(voidR);
                    } catch (Exception e) {
                        String access$000 = Room.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("leave exception ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                    Room.this.mDataHandler.onLeaveRoom(Room.this.getRoomId());
                }
            }

            public void onNetworkError(Exception exc) {
                Room.this.mIsLeaving = false;
                try {
                    apiCallback.onNetworkError(exc);
                } catch (Exception e) {
                    String access$000 = Room.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("leave exception ");
                    sb.append(e.getMessage());
                    Log.e(access$000, sb.toString(), e);
                }
                Room.this.mDataHandler.onRoomInternalUpdate(Room.this.getRoomId());
            }

            public void onMatrixError(MatrixError matrixError) {
                if (matrixError.mStatus.intValue() == 404) {
                    onSuccess((Void) null);
                    return;
                }
                Room.this.mIsLeaving = false;
                try {
                    apiCallback.onMatrixError(matrixError);
                } catch (Exception e) {
                    String access$000 = Room.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("leave exception ");
                    sb.append(e.getMessage());
                    Log.e(access$000, sb.toString(), e);
                }
                Room.this.mDataHandler.onRoomInternalUpdate(Room.this.getRoomId());
            }

            public void onUnexpectedError(Exception exc) {
                Room.this.mIsLeaving = false;
                try {
                    apiCallback.onUnexpectedError(exc);
                } catch (Exception e) {
                    String access$000 = Room.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("leave exception ");
                    sb.append(e.getMessage());
                    Log.e(access$000, sb.toString(), e);
                }
                Room.this.mDataHandler.onRoomInternalUpdate(Room.this.getRoomId());
            }
        });
    }

    public void forget(final ApiCallback<Void> apiCallback) {
        this.mDataHandler.getDataRetriever().getRoomsRestClient().forgetRoom(getRoomId(), new SimpleApiCallback<Void>(apiCallback) {
            public void onSuccess(Void voidR) {
                if (Room.this.mDataHandler.isAlive()) {
                    IMXStore store = Room.this.mDataHandler.getStore(Room.this.getRoomId());
                    if (store != null) {
                        store.deleteRoom(Room.this.getRoomId());
                        store.commit();
                    }
                    try {
                        apiCallback.onSuccess(voidR);
                    } catch (Exception e) {
                        String access$000 = Room.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("forget exception ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void kick(String str, String str2, ApiCallback<Void> apiCallback) {
        UserIdAndReason userIdAndReason = new UserIdAndReason();
        userIdAndReason.userId = str;
        if (!TextUtils.isEmpty(str2)) {
            userIdAndReason.reason = str2;
        }
        this.mDataHandler.getDataRetriever().getRoomsRestClient().kickFromRoom(getRoomId(), userIdAndReason, apiCallback);
    }

    public void ban(String str, String str2, ApiCallback<Void> apiCallback) {
        UserIdAndReason userIdAndReason = new UserIdAndReason();
        userIdAndReason.userId = str;
        if (!TextUtils.isEmpty(str2)) {
            userIdAndReason.reason = str2;
        }
        this.mDataHandler.getDataRetriever().getRoomsRestClient().banFromRoom(getRoomId(), userIdAndReason, apiCallback);
    }

    public void unban(String str, ApiCallback<Void> apiCallback) {
        UserIdAndReason userIdAndReason = new UserIdAndReason();
        userIdAndReason.userId = str;
        this.mDataHandler.getDataRetriever().getRoomsRestClient().unbanFromRoom(getRoomId(), userIdAndReason, apiCallback);
    }

    public boolean isEncrypted() {
        return getState().isEncrypted();
    }

    public void enableEncryptionWithAlgorithm(String str, final ApiCallback<Void> apiCallback) {
        if (this.mDataHandler.getCrypto() != null && !TextUtils.isEmpty(str)) {
            HashMap hashMap = new HashMap();
            hashMap.put(CryptoRoomEntityFields.ALGORITHM, str);
            if (apiCallback != null) {
                this.mRoomEncryptionCallback = apiCallback;
                addEventListener(this.mEncryptionListener);
            }
            this.mDataHandler.getDataRetriever().getRoomsRestClient().sendStateEvent(getRoomId(), "m.room.encryption", null, hashMap, new ApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                }

                public void onNetworkError(Exception exc) {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onNetworkError(exc);
                        Room room = Room.this;
                        room.removeEventListener(room.mEncryptionListener);
                    }
                }

                public void onMatrixError(MatrixError matrixError) {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onMatrixError(matrixError);
                        Room room = Room.this;
                        room.removeEventListener(room.mEncryptionListener);
                    }
                }

                public void onUnexpectedError(Exception exc) {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onUnexpectedError(exc);
                        Room room = Room.this;
                        room.removeEventListener(room.mEncryptionListener);
                    }
                }
            });
        } else if (apiCallback == null) {
        } else {
            if (this.mDataHandler.getCrypto() == null) {
                String str2 = MXCryptoError.ENCRYPTING_NOT_ENABLED_REASON;
                apiCallback.onMatrixError(new MXCryptoError(MXCryptoError.ENCRYPTING_NOT_ENABLED_ERROR_CODE, str2, str2));
                return;
            }
            apiCallback.onMatrixError(new MXCryptoError(MXCryptoError.MISSING_FIELDS_ERROR_CODE, MXCryptoError.UNABLE_TO_ENCRYPT, MXCryptoError.MISSING_FIELDS_REASON));
        }
    }

    private void initRoomMediaMessagesSender() {
        if (this.mRoomMediaMessagesSender == null) {
            this.mRoomMediaMessagesSender = new RoomMediaMessagesSender(getStore().getContext(), this.mDataHandler, this);
        }
    }

    public void sendTextMessage(String str, String str2, String str3, EventCreationListener eventCreationListener) {
        sendTextMessage(str, str2, str3, null, Message.MSGTYPE_TEXT, eventCreationListener);
    }

    public void sendTextMessage(String str, String str2, String str3, Event event, EventCreationListener eventCreationListener) {
        sendTextMessage(str, str2, str3, event, Message.MSGTYPE_TEXT, eventCreationListener);
    }

    public void sendEmoteMessage(String str, String str2, String str3, EventCreationListener eventCreationListener) {
        sendTextMessage(str, str2, str3, null, Message.MSGTYPE_EMOTE, eventCreationListener);
    }

    private void sendTextMessage(String str, String str2, String str3, Event event, String str4, EventCreationListener eventCreationListener) {
        initRoomMediaMessagesSender();
        RoomMediaMessage roomMediaMessage = new RoomMediaMessage(str, str2, str3);
        roomMediaMessage.setMessageType(str4);
        roomMediaMessage.setEventCreationListener(eventCreationListener);
        if (canReplyTo(event)) {
            roomMediaMessage.setReplyToEvent(event);
        }
        this.mRoomMediaMessagesSender.send(roomMediaMessage);
    }

    public boolean canReplyTo(Event event) {
        char c;
        if (event != null) {
            if (Event.EVENT_TYPE_MESSAGE.equals(event.getType())) {
                String messageMsgType = JsonUtils.getMessageMsgType(event.getContentAsJsonObject());
                if (messageMsgType != null) {
                    switch (messageMsgType.hashCode()) {
                        case -1128764835:
                            if (messageMsgType.equals(Message.MSGTYPE_FILE)) {
                                c = 6;
                                break;
                            }
                        case -1128351218:
                            if (messageMsgType.equals(Message.MSGTYPE_TEXT)) {
                                c = 0;
                                break;
                            }
                        case -636239083:
                            if (messageMsgType.equals(Message.MSGTYPE_AUDIO)) {
                                c = 5;
                                break;
                            }
                        case -632772425:
                            if (messageMsgType.equals(Message.MSGTYPE_EMOTE)) {
                                c = 2;
                                break;
                            }
                        case -629092198:
                            if (messageMsgType.equals(Message.MSGTYPE_IMAGE)) {
                                c = 3;
                                break;
                            }
                        case -617202758:
                            if (messageMsgType.equals(Message.MSGTYPE_VIDEO)) {
                                c = 4;
                                break;
                            }
                        case 2118539129:
                            if (messageMsgType.equals(Message.MSGTYPE_NOTICE)) {
                                c = 1;
                                break;
                            }
                        default:
                            c = 65535;
                            break;
                    }
                    switch (c) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public void sendMediaMessage(RoomMediaMessage roomMediaMessage, int i, int i2, EventCreationListener eventCreationListener) {
        initRoomMediaMessagesSender();
        roomMediaMessage.setThumbnailSize(new Pair(Integer.valueOf(i), Integer.valueOf(i2)));
        roomMediaMessage.setEventCreationListener(eventCreationListener);
        this.mRoomMediaMessagesSender.send(roomMediaMessage);
    }

    public void sendStickerMessage(Event event, EventCreationListener eventCreationListener) {
        initRoomMediaMessagesSender();
        RoomMediaMessage roomMediaMessage = new RoomMediaMessage(event);
        roomMediaMessage.setMessageType(Event.EVENT_TYPE_STICKER);
        roomMediaMessage.setEventCreationListener(eventCreationListener);
        this.mRoomMediaMessagesSender.send(roomMediaMessage);
    }

    public List<Event> getUnsentEvents() {
        ArrayList arrayList = new ArrayList();
        if (getStore() != null) {
            List undeliveredEvents = getStore().getUndeliveredEvents(getRoomId());
            List unknownDeviceEvents = getStore().getUnknownDeviceEvents(getRoomId());
            if (undeliveredEvents != null) {
                arrayList.addAll(undeliveredEvents);
            }
            if (unknownDeviceEvents != null) {
                arrayList.addAll(unknownDeviceEvents);
            }
        }
        return arrayList;
    }

    public void deleteEvents(List<Event> list) {
        if (getStore() != null && list != null && list.size() > 0) {
            for (Event deleteEvent : list) {
                getStore().deleteEvent(deleteEvent);
            }
            Event latestEvent = getStore().getLatestEvent(getRoomId());
            if (latestEvent != null && RoomSummary.isSupportedEvent(latestEvent)) {
                RoomSummary summary = getStore().getSummary(getRoomId());
                if (summary != null) {
                    summary.setLatestReceivedEvent(latestEvent, getState());
                } else {
                    summary = new RoomSummary(null, latestEvent, getState(), this.mDataHandler.getUserId());
                }
                getStore().storeSummary(summary);
            }
            getStore().commit();
        }
    }

    public boolean isDirect() {
        return this.mDataHandler.getDirectChatRoomIdsList().contains(getRoomId());
    }

    public RoomSummary getRoomSummary() {
        if (getDataHandler() == null || getDataHandler().getStore() == null) {
            return null;
        }
        return getDataHandler().getStore().getSummary(getRoomId());
    }

    public int getNumberOfMembers() {
        if (getDataHandler().isLazyLoadingEnabled()) {
            return getNumberOfJoinedMembers() + getNumberOfInvitedMembers();
        }
        return getState().getLoadedMembers().size();
    }

    public int getNumberOfJoinedMembers() {
        if (!getDataHandler().isLazyLoadingEnabled()) {
            return getNumberOfLoadedJoinedMembers();
        }
        RoomSummary roomSummary = getRoomSummary();
        if (roomSummary != null) {
            return roomSummary.getNumberOfJoinedMembers();
        }
        return getNumberOfLoadedJoinedMembers();
    }

    private int getNumberOfLoadedJoinedMembers() {
        int i = 0;
        for (RoomMember roomMember : getState().getLoadedMembers()) {
            if ("join".equals(roomMember.membership)) {
                i++;
            }
        }
        return i;
    }

    public int getNumberOfInvitedMembers() {
        if (!getDataHandler().isLazyLoadingEnabled()) {
            return getNumberOfLoadedInvitedMembers();
        }
        RoomSummary roomSummary = getRoomSummary();
        if (roomSummary != null) {
            return roomSummary.getNumberOfInvitedMembers();
        }
        return getNumberOfLoadedInvitedMembers();
    }

    private int getNumberOfLoadedInvitedMembers() {
        int i = 0;
        for (RoomMember roomMember : getState().getLoadedMembers()) {
            if ("invite".equals(roomMember.membership)) {
                i++;
            }
        }
        return i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getRoomId());
        String str = " ";
        sb.append(str);
        sb.append(getRoomDisplayName(getStore().getContext()));
        sb.append(str);
        sb.append(super.toString());
        return sb.toString();
    }
}
