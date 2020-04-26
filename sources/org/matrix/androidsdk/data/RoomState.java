package org.matrix.androidsdk.data;

import android.text.TextUtils;
import com.google.gson.JsonObject;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.call.MXCallsManager;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.interfaces.CryptoRoomMember;
import org.matrix.androidsdk.crypto.interfaces.CryptoRoomState;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.PowerLevels;
import org.matrix.androidsdk.rest.model.RoomCreateContent;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.RoomPinnedEventsContent;
import org.matrix.androidsdk.rest.model.RoomTombstoneContent;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.pid.RoomThirdPartyInvite;

public class RoomState implements Externalizable, CryptoRoomState {
    public static final String GUEST_ACCESS_CAN_JOIN = "can_join";
    public static final String GUEST_ACCESS_FORBIDDEN = "forbidden";
    public static final String HISTORY_VISIBILITY_INVITED = "invited";
    public static final String HISTORY_VISIBILITY_JOINED = "joined";
    public static final String HISTORY_VISIBILITY_SHARED = "shared";
    public static final String HISTORY_VISIBILITY_WORLD_READABLE = "world_readable";
    public static final String JOIN_RULE_INVITE = "invite";
    public static final String JOIN_RULE_PUBLIC = "public";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = RoomState.class.getSimpleName();
    private static final long serialVersionUID = -6019932024524988201L;
    public String algorithm;
    public List<String> aliases;
    public String avatar_url;
    private String canonicalAlias;
    public List<String> groups;
    public String guest_access;
    public String history_visibility;
    public String join_rule;
    private Map<String, List<String>> mAliasesByDomain = new HashMap();
    /* access modifiers changed from: private */
    public boolean mAllMembersAreLoaded;
    /* access modifiers changed from: private */
    public transient Object mDataHandler = null;
    /* access modifiers changed from: private */
    public final List<ApiCallback<List<RoomMember>>> mGetAllMembersCallbacks = new ArrayList();
    private int mHighlightCount;
    private boolean mIsLive;
    private transient Map<String, String> mMemberDisplayNameByUserId = new HashMap();
    /* access modifiers changed from: private */
    public final Map<String, RoomMember> mMembers = new HashMap();
    private final Map<String, RoomMember> mMembersWithThirdPartyInviteTokenCache = new HashMap();
    private List<String> mMergedAliasesList;
    private int mNotificationCount;
    private Map<String, Event> mRoomAliases = new HashMap();
    private RoomCreateContent mRoomCreateContent;
    private RoomPinnedEventsContent mRoomPinnedEventsContent;
    private RoomTombstoneContent mRoomTombstoneContent;
    private Map<String, List<Event>> mStateEvents = new HashMap();
    private final Map<String, RoomThirdPartyInvite> mThirdPartyInvites = new HashMap();
    public String name;
    private PowerLevels powerLevels;
    public String roomId;
    private String token;
    public String topic;
    public String url;
    public String visibility;

    public String getGuestAccess() {
        String str = this.guest_access;
        return str != null ? str : GUEST_ACCESS_FORBIDDEN;
    }

    public String getHistoryVisibility() {
        String str = this.history_visibility;
        return str != null ? str : HISTORY_VISIBILITY_SHARED;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String str) {
        this.token = str;
    }

    public String getAvatarUrl() {
        String str = this.url;
        if (str != null) {
            return str;
        }
        return this.avatar_url;
    }

    public List<String> getRelatedGroups() {
        List<String> list = this.groups;
        return list == null ? new ArrayList() : list;
    }

    public List<RoomMember> getLoadedMembers() {
        ArrayList arrayList;
        synchronized (this) {
            arrayList = new ArrayList(this.mMembers.values());
        }
        return arrayList;
    }

    public List<CryptoRoomMember> getLoadedMembersCrypto() {
        return new ArrayList(getLoadedMembers());
    }

    public void getMembersAsync(ApiCallback<List<RoomMember>> apiCallback) {
        boolean z;
        ArrayList arrayList;
        if (areAllMembersLoaded()) {
            synchronized (this) {
                arrayList = new ArrayList(this.mMembers.values());
            }
            apiCallback.onSuccess(arrayList);
            return;
        }
        synchronized (this.mGetAllMembersCallbacks) {
            this.mGetAllMembersCallbacks.add(apiCallback);
            z = true;
            if (this.mGetAllMembersCallbacks.size() != 1) {
                z = false;
            }
        }
        if (z) {
            getDataHandler().getMembersAsync(this.roomId, new ApiCallback<List<RoomMember>>() {
                public void onSuccess(List<RoomMember> list) {
                    String access$000 = RoomState.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("getMembersAsync has returned ");
                    sb.append(list.size());
                    sb.append(" users.");
                    Log.d(access$000, sb.toString());
                    IMXStore store = ((MXDataHandler) RoomState.this.mDataHandler).getStore();
                    for (RoomMember roomMember : list) {
                        if (RoomState.this.getMember(roomMember.getUserId()) == null) {
                            RoomState.this.setMember(roomMember.getUserId(), roomMember);
                            if (store != null) {
                                store.updateUserWithRoomMemberEvent(roomMember);
                            }
                        }
                    }
                    synchronized (RoomState.this.mGetAllMembersCallbacks) {
                        for (ApiCallback onSuccess : RoomState.this.mGetAllMembersCallbacks) {
                            onSuccess.onSuccess(new ArrayList(RoomState.this.mMembers.values()));
                        }
                        RoomState.this.mGetAllMembersCallbacks.clear();
                    }
                    RoomState.this.mAllMembersAreLoaded = true;
                }

                public void onNetworkError(Exception exc) {
                    String access$000 = RoomState.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("getMembersAsync onNetworkError ");
                    sb.append(exc.getLocalizedMessage());
                    Log.e(access$000, sb.toString());
                    synchronized (RoomState.this.mGetAllMembersCallbacks) {
                        for (ApiCallback onNetworkError : RoomState.this.mGetAllMembersCallbacks) {
                            onNetworkError.onNetworkError(exc);
                        }
                        RoomState.this.mGetAllMembersCallbacks.clear();
                    }
                }

                public void onMatrixError(MatrixError matrixError) {
                    String access$000 = RoomState.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("getMembersAsync onMatrixError ");
                    sb.append(matrixError.getLocalizedMessage());
                    Log.e(access$000, sb.toString());
                    synchronized (RoomState.this.mGetAllMembersCallbacks) {
                        for (ApiCallback onMatrixError : RoomState.this.mGetAllMembersCallbacks) {
                            onMatrixError.onMatrixError(matrixError);
                        }
                        RoomState.this.mGetAllMembersCallbacks.clear();
                    }
                }

                public void onUnexpectedError(Exception exc) {
                    String access$000 = RoomState.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("getMembersAsync onUnexpectedError ");
                    sb.append(exc.getLocalizedMessage());
                    Log.e(access$000, sb.toString());
                    synchronized (RoomState.this.mGetAllMembersCallbacks) {
                        for (ApiCallback onUnexpectedError : RoomState.this.mGetAllMembersCallbacks) {
                            onUnexpectedError.onUnexpectedError(exc);
                        }
                        RoomState.this.mGetAllMembersCallbacks.clear();
                    }
                }
            });
        }
    }

    private boolean areAllMembersLoaded() {
        Object obj = this.mDataHandler;
        return obj != null && (!((MXDataHandler) obj).isLazyLoadingEnabled() || this.mAllMembersAreLoaded);
    }

    public void forceMembersRequest() {
        this.mAllMembersAreLoaded = false;
    }

    public List<Event> getStateEvents(Set<String> set) {
        ArrayList arrayList = new ArrayList();
        ArrayList<Event> arrayList2 = new ArrayList<>();
        for (List addAll : this.mStateEvents.values()) {
            arrayList2.addAll(addAll);
        }
        if (set == null || set.isEmpty()) {
            arrayList.addAll(arrayList2);
        } else {
            for (Event event : arrayList2) {
                if (event.getType() != null && set.contains(event.getType())) {
                    arrayList.add(event);
                }
            }
        }
        return arrayList;
    }

    public void getStateEvents(IMXStore iMXStore, final Set<String> set, final ApiCallback<List<Event>> apiCallback) {
        if (iMXStore != null) {
            final ArrayList arrayList = new ArrayList();
            for (List addAll : this.mStateEvents.values()) {
                arrayList.addAll(addAll);
            }
            iMXStore.getRoomStateEvents(this.roomId, new SimpleApiCallback<List<Event>>() {
                public void onSuccess(List<Event> list) {
                    arrayList.addAll(list);
                    ArrayList arrayList = new ArrayList();
                    Set set = set;
                    if (set == null || set.isEmpty()) {
                        arrayList.addAll(arrayList);
                    } else {
                        for (Event event : arrayList) {
                            if (event.getType() != null && set.contains(event.getType())) {
                                arrayList.add(event);
                            }
                        }
                    }
                    apiCallback.onSuccess(arrayList);
                }
            });
        }
    }

    public List<RoomMember> getDisplayableLoadedMembers() {
        List<RoomMember> loadedMembers = getLoadedMembers();
        RoomMember member = getMember(MXCallsManager.getConferenceUserId(this.roomId));
        if (member != null) {
            loadedMembers.remove(member);
        }
        return loadedMembers;
    }

    public void getDisplayableMembersAsync(final ApiCallback<List<RoomMember>> apiCallback) {
        getMembersAsync(new SimpleApiCallback<List<RoomMember>>(apiCallback) {
            public void onSuccess(List<RoomMember> list) {
                RoomState roomState = RoomState.this;
                RoomMember member = roomState.getMember(MXCallsManager.getConferenceUserId(roomState.roomId));
                if (member != null) {
                    ArrayList arrayList = new ArrayList(list);
                    arrayList.remove(member);
                    apiCallback.onSuccess(arrayList);
                    return;
                }
                apiCallback.onSuccess(list);
            }
        });
    }

    public boolean isConferenceUserRoom() {
        if (getDataHandler() != null && getDataHandler().getStore() != null && getDataHandler().getStore().getSummary(this.roomId) != null) {
            return getDataHandler().getStore().getSummary(this.roomId).isConferenceUserRoom();
        }
        Log.w(LOG_TAG, "## isConferenceUserRoom(): something is null");
        return false;
    }

    public void setIsConferenceUserRoom(boolean z) {
        if (getDataHandler() == null || getDataHandler().getStore() == null || getDataHandler().getStore().getSummary(this.roomId) == null) {
            Log.w(LOG_TAG, "## setIsConferenceUserRoom(): something is null");
        } else {
            getDataHandler().getStore().getSummary(this.roomId).setIsConferenceUserRoom(z);
        }
    }

    public void setMember(String str, RoomMember roomMember) {
        if (roomMember.getUserId() == null) {
            roomMember.setUserId(str);
        }
        synchronized (this) {
            if (this.mMemberDisplayNameByUserId != null) {
                this.mMemberDisplayNameByUserId.remove(str);
            }
            this.mMembers.put(str, roomMember);
        }
    }

    public RoomMember getMember(String str) {
        RoomMember roomMember;
        synchronized (this) {
            roomMember = (RoomMember) this.mMembers.get(str);
        }
        if (roomMember == null && TextUtils.equals(getDataHandler().getUserId(), str)) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## Null current user '");
            sb.append(str);
            Log.e(str2, sb.toString());
        }
        return roomMember;
    }

    public RoomMember getMemberByEventId(String str) {
        RoomMember roomMember;
        synchronized (this) {
            Iterator it = this.mMembers.values().iterator();
            while (true) {
                if (!it.hasNext()) {
                    roomMember = null;
                    break;
                }
                roomMember = (RoomMember) it.next();
                if (roomMember.getOriginalEventId().equals(str)) {
                    break;
                }
            }
        }
        return roomMember;
    }

    public void removeMember(String str) {
        synchronized (this) {
            this.mMembers.remove(str);
            if (this.mMemberDisplayNameByUserId != null) {
                this.mMemberDisplayNameByUserId.remove(str);
            }
        }
    }

    public RoomMember memberWithThirdPartyInviteToken(String str) {
        return (RoomMember) this.mMembersWithThirdPartyInviteTokenCache.get(str);
    }

    public RoomThirdPartyInvite thirdPartyInviteWithToken(String str) {
        return (RoomThirdPartyInvite) this.mThirdPartyInvites.get(str);
    }

    public Collection<RoomThirdPartyInvite> thirdPartyInvites() {
        return this.mThirdPartyInvites.values();
    }

    public PowerLevels getPowerLevels() {
        PowerLevels powerLevels2 = this.powerLevels;
        if (powerLevels2 != null) {
            return powerLevels2.deepCopy();
        }
        return null;
    }

    public void setPowerLevels(PowerLevels powerLevels2) {
        this.powerLevels = powerLevels2;
    }

    public void setDataHandler(MXDataHandler mXDataHandler) {
        this.mDataHandler = mXDataHandler;
    }

    public MXDataHandler getDataHandler() {
        return (MXDataHandler) this.mDataHandler;
    }

    public void setNotificationCount(int i) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## setNotificationCount() : ");
        sb.append(i);
        sb.append(" room id ");
        sb.append(this.roomId);
        Log.d(str, sb.toString());
        this.mNotificationCount = i;
    }

    public int getNotificationCount() {
        return this.mNotificationCount;
    }

    public void setHighlightCount(int i) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## setHighlightCount() : ");
        sb.append(i);
        sb.append(" room id ");
        sb.append(this.roomId);
        Log.d(str, sb.toString());
        this.mHighlightCount = i;
    }

    public int getHighlightCount() {
        return this.mHighlightCount;
    }

    public boolean canBackPaginate(boolean z, boolean z2) {
        boolean isEmpty = TextUtils.isEmpty(this.history_visibility);
        String str = HISTORY_VISIBILITY_SHARED;
        String str2 = isEmpty ? str : this.history_visibility;
        return z || str2.equals(HISTORY_VISIBILITY_WORLD_READABLE) || str2.equals(str) || (str2.equals(HISTORY_VISIBILITY_INVITED) && z2);
    }

    public RoomState deepCopy() {
        RoomState roomState = new RoomState();
        roomState.roomId = this.roomId;
        PowerLevels powerLevels2 = this.powerLevels;
        RoomTombstoneContent roomTombstoneContent = null;
        roomState.setPowerLevels(powerLevels2 == null ? null : powerLevels2.deepCopy());
        List<String> list = this.aliases;
        roomState.aliases = list == null ? null : new ArrayList<>(list);
        roomState.mAliasesByDomain = new HashMap(this.mAliasesByDomain);
        roomState.canonicalAlias = this.canonicalAlias;
        roomState.name = this.name;
        roomState.topic = this.topic;
        roomState.url = this.url;
        RoomCreateContent roomCreateContent = this.mRoomCreateContent;
        roomState.mRoomCreateContent = roomCreateContent != null ? roomCreateContent.deepCopy() : null;
        RoomPinnedEventsContent roomPinnedEventsContent = this.mRoomPinnedEventsContent;
        roomState.mRoomPinnedEventsContent = roomPinnedEventsContent != null ? roomPinnedEventsContent.deepCopy() : null;
        roomState.join_rule = this.join_rule;
        roomState.guest_access = this.guest_access;
        roomState.history_visibility = this.history_visibility;
        roomState.visibility = this.visibility;
        roomState.token = this.token;
        roomState.groups = this.groups;
        roomState.mDataHandler = this.mDataHandler;
        roomState.mIsLive = this.mIsLive;
        roomState.mAllMembersAreLoaded = this.mAllMembersAreLoaded;
        roomState.algorithm = this.algorithm;
        roomState.mRoomAliases = new HashMap(this.mRoomAliases);
        roomState.mStateEvents = new HashMap(this.mStateEvents);
        RoomTombstoneContent roomTombstoneContent2 = this.mRoomTombstoneContent;
        if (roomTombstoneContent2 != null) {
            roomTombstoneContent = roomTombstoneContent2.deepCopy();
        }
        roomState.mRoomTombstoneContent = roomTombstoneContent;
        synchronized (this) {
            for (Entry entry : this.mMembers.entrySet()) {
                roomState.setMember((String) entry.getKey(), ((RoomMember) entry.getValue()).deepCopy());
            }
            for (String str : this.mThirdPartyInvites.keySet()) {
                roomState.mThirdPartyInvites.put(str, ((RoomThirdPartyInvite) this.mThirdPartyInvites.get(str)).deepCopy());
            }
            for (String str2 : this.mMembersWithThirdPartyInviteTokenCache.keySet()) {
                roomState.mMembersWithThirdPartyInviteTokenCache.put(str2, ((RoomMember) this.mMembersWithThirdPartyInviteTokenCache.get(str2)).deepCopy());
            }
        }
        return roomState;
    }

    public String getCanonicalAlias() {
        return this.canonicalAlias;
    }

    public void setCanonicalAlias(String str) {
        this.canonicalAlias = str;
    }

    public List<String> getAliases() {
        if (this.mMergedAliasesList == null) {
            this.mMergedAliasesList = new ArrayList();
            for (String str : this.mAliasesByDomain.keySet()) {
                this.mMergedAliasesList.addAll((Collection) this.mAliasesByDomain.get(str));
            }
            List<String> list = this.aliases;
            if (list != null) {
                for (String str2 : list) {
                    if (this.mMergedAliasesList.indexOf(str2) < 0) {
                        this.mMergedAliasesList.add(str2);
                    }
                }
            }
        }
        return this.mMergedAliasesList;
    }

    public Map<String, List<String>> getAliasesByDomain() {
        return new HashMap(this.mAliasesByDomain);
    }

    public void removeAlias(String str) {
        if (getAliases().indexOf(str) >= 0) {
            List<String> list = this.aliases;
            if (list != null) {
                list.remove(str);
            }
            for (String str2 : this.mAliasesByDomain.keySet()) {
                ((List) this.mAliasesByDomain.get(str2)).remove(str);
            }
            this.mMergedAliasesList = null;
        }
    }

    public void addAlias(String str) {
        if (getAliases().indexOf(str) < 0) {
            this.mMergedAliasesList.add(str);
        }
    }

    public boolean isEncrypted() {
        return this.algorithm != null;
    }

    public boolean isVersioned() {
        return this.mRoomTombstoneContent != null;
    }

    public RoomTombstoneContent getRoomTombstoneContent() {
        return this.mRoomTombstoneContent;
    }

    public RoomCreateContent getRoomCreateContent() {
        return this.mRoomCreateContent;
    }

    public RoomPinnedEventsContent getRoomPinnedEventsContent() {
        return this.mRoomPinnedEventsContent;
    }

    public String encryptionAlgorithm() {
        if (TextUtils.isEmpty(this.algorithm)) {
            return null;
        }
        return this.algorithm;
    }

    public boolean applyState(Event event, boolean z, IMXStore iMXStore) {
        JsonObject jsonObject;
        String str = "leave";
        if (event.stateKey == null) {
            return false;
        }
        if (z) {
            jsonObject = event.getContentAsJsonObject();
        } else {
            jsonObject = event.getPrevContentAsJsonObject();
        }
        String type = event.getType();
        try {
            String str2 = "m.room.member";
            if (Event.EVENT_TYPE_STATE_ROOM_NAME.equals(type)) {
                this.name = JsonUtils.toStateEvent(jsonObject).name;
            } else if (Event.EVENT_TYPE_STATE_ROOM_TOPIC.equals(type)) {
                this.topic = JsonUtils.toStateEvent(jsonObject).topic;
            } else if (Event.EVENT_TYPE_STATE_ROOM_CREATE.equals(type)) {
                this.mRoomCreateContent = JsonUtils.toRoomCreateContent(jsonObject);
            } else if (Event.EVENT_TYPE_STATE_ROOM_JOIN_RULES.equals(type)) {
                this.join_rule = JsonUtils.toStateEvent(jsonObject).joinRule;
            } else if (Event.EVENT_TYPE_STATE_ROOM_GUEST_ACCESS.equals(type)) {
                this.guest_access = JsonUtils.toStateEvent(jsonObject).guestAccess;
            } else if (Event.EVENT_TYPE_STATE_ROOM_ALIASES.equals(type)) {
                if (!TextUtils.isEmpty(event.stateKey)) {
                    this.aliases = JsonUtils.toStateEvent(jsonObject).aliases;
                    if (this.aliases != null) {
                        this.mAliasesByDomain.put(event.stateKey, this.aliases);
                        this.mRoomAliases.put(event.stateKey, event);
                    } else {
                        this.mAliasesByDomain.put(event.stateKey, new ArrayList());
                    }
                }
            } else if ("m.room.encryption".equals(type)) {
                this.algorithm = JsonUtils.toStateEvent(jsonObject).algorithm;
                if (this.algorithm == null) {
                    this.algorithm = "";
                }
            } else if (Event.EVENT_TYPE_STATE_CANONICAL_ALIAS.equals(type)) {
                this.canonicalAlias = JsonUtils.toStateEvent(jsonObject).canonicalAlias;
            } else if (Event.EVENT_TYPE_STATE_HISTORY_VISIBILITY.equals(type)) {
                this.history_visibility = JsonUtils.toStateEvent(jsonObject).historyVisibility;
            } else if (Event.EVENT_TYPE_STATE_ROOM_AVATAR.equals(type)) {
                this.url = JsonUtils.toStateEvent(jsonObject).url;
            } else if (Event.EVENT_TYPE_STATE_RELATED_GROUPS.equals(type)) {
                this.groups = JsonUtils.toStateEvent(jsonObject).groups;
            } else if (str2.equals(type)) {
                RoomMember roomMember = JsonUtils.toRoomMember(jsonObject);
                String str3 = event.stateKey;
                if (str3 == null) {
                    String str4 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## applyState() : null stateKey in ");
                    sb.append(this.roomId);
                    Log.e(str4, sb.toString());
                } else if (roomMember != null) {
                    try {
                        roomMember.setUserId(str3);
                        roomMember.setOriginServerTs(event.getOriginServerTs());
                        roomMember.setOriginalEventId(event.eventId);
                        roomMember.mSender = event.getSender();
                        if (iMXStore != null) {
                            iMXStore.storeRoomStateEvent(this.roomId, event);
                        }
                        RoomMember member = getMember(str3);
                        if (roomMember.equals(member)) {
                            String str5 = LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("## applyState() : seems being a duplicated event for ");
                            sb2.append(str3);
                            sb2.append(" in room ");
                            sb2.append(this.roomId);
                            Log.e(str5, sb2.toString());
                            return false;
                        }
                        if (member != null && (TextUtils.equals(roomMember.membership, str) || TextUtils.equals(roomMember.membership, "ban"))) {
                            if (roomMember.getAvatarUrl() == null) {
                                roomMember.setAvatarUrl(member.getAvatarUrl());
                            }
                            if (roomMember.displayname == null) {
                                roomMember.displayname = member.displayname;
                            }
                            if (this.mMemberDisplayNameByUserId != null) {
                                this.mMemberDisplayNameByUserId.remove(str3);
                            }
                            if (!TextUtils.equals(event.getSender(), event.stateKey) && TextUtils.equals(member.membership, "join") && TextUtils.equals(roomMember.membership, str)) {
                                roomMember.membership = RoomMember.MEMBERSHIP_KICK;
                            }
                        }
                        if (iMXStore != null) {
                            iMXStore.updateUserWithRoomMemberEvent(roomMember);
                        }
                        if (!TextUtils.isEmpty(roomMember.getThirdPartyInviteToken())) {
                            this.mMembersWithThirdPartyInviteTokenCache.put(roomMember.getThirdPartyInviteToken(), roomMember);
                        }
                        setMember(str3, roomMember);
                    } catch (Exception e) {
                        String str6 = LOG_TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("## applyState() - EVENT_TYPE_STATE_ROOM_MEMBER failed ");
                        sb3.append(e.getMessage());
                        Log.e(str6, sb3.toString(), e);
                    }
                } else if (getMember(str3) == null) {
                    String str7 = LOG_TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("## applyState() : the user ");
                    sb4.append(str3);
                    sb4.append(" is not anymore a member of ");
                    sb4.append(this.roomId);
                    Log.e(str7, sb4.toString());
                    return false;
                } else {
                    removeMember(str3);
                }
            } else if (Event.EVENT_TYPE_STATE_ROOM_POWER_LEVELS.equals(type)) {
                this.powerLevels = JsonUtils.toPowerLevels(jsonObject);
            } else if (Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE.equals(event.getType())) {
                if (jsonObject != null) {
                    RoomThirdPartyInvite roomThirdPartyInvite = JsonUtils.toRoomThirdPartyInvite(jsonObject);
                    roomThirdPartyInvite.token = event.stateKey;
                    if (iMXStore != null) {
                        iMXStore.storeRoomStateEvent(this.roomId, event);
                    }
                    if (!TextUtils.isEmpty(roomThirdPartyInvite.token)) {
                        this.mThirdPartyInvites.put(roomThirdPartyInvite.token, roomThirdPartyInvite);
                    }
                }
            } else if (Event.EVENT_TYPE_STATE_ROOM_TOMBSTONE.equals(type)) {
                this.mRoomTombstoneContent = JsonUtils.toRoomTombstoneContent(jsonObject);
            } else if (Event.EVENT_TYPE_STATE_PINNED_EVENT.equals(type)) {
                this.mRoomPinnedEventsContent = JsonUtils.toRoomPinnedEventsContent(jsonObject);
            }
            if (!TextUtils.isEmpty(type) && !str2.equals(type)) {
                List list = (List) this.mStateEvents.get(type);
                if (list == null) {
                    list = new ArrayList();
                    this.mStateEvents.put(type, list);
                }
                list.add(event);
            }
        } catch (Exception e2) {
            String str8 = LOG_TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("applyState failed with error ");
            sb5.append(e2.getMessage());
            Log.e(str8, sb5.toString(), e2);
        }
        return true;
    }

    public boolean isPublic() {
        String str = this.visibility;
        if (str == null) {
            str = this.join_rule;
        }
        return TextUtils.equals(str, "public");
    }

    public String getMemberName(String str) {
        String str2;
        if (str == null) {
            return null;
        }
        synchronized (this) {
            if (this.mMemberDisplayNameByUserId == null) {
                this.mMemberDisplayNameByUserId = new HashMap();
            }
            str2 = (String) this.mMemberDisplayNameByUserId.get(str);
        }
        if (str2 != null) {
            return str2;
        }
        RoomMember member = getMember(str);
        if (member != null && !TextUtils.isEmpty(member.displayname)) {
            str2 = member.displayname;
            synchronized (this) {
                ArrayList arrayList = new ArrayList();
                for (RoomMember roomMember : this.mMembers.values()) {
                    if (str2.equals(roomMember.displayname)) {
                        arrayList.add(roomMember.getUserId());
                    }
                }
                if (arrayList.size() > 1) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str2);
                    sb.append(" (");
                    sb.append(str);
                    sb.append(")");
                    str2 = sb.toString();
                }
            }
        } else if (member != null && TextUtils.equals(member.membership, "invite")) {
            User user = ((MXDataHandler) this.mDataHandler).getUser(str);
            if (user != null) {
                str2 = user.displayname;
            }
        }
        if (str2 == null) {
            str2 = str;
        }
        this.mMemberDisplayNameByUserId.put(str, str2);
        return str2;
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        if (objectInput.readBoolean()) {
            this.roomId = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.powerLevels = (PowerLevels) objectInput.readObject();
        }
        if (objectInput.readBoolean()) {
            this.aliases = (List) objectInput.readObject();
        }
        for (Event event : (List) objectInput.readObject()) {
            this.mRoomAliases.put(event.stateKey, event);
        }
        this.mAliasesByDomain = (Map) objectInput.readObject();
        if (objectInput.readBoolean()) {
            this.mMergedAliasesList = (List) objectInput.readObject();
        }
        Map map = (Map) objectInput.readObject();
        if (map != null) {
            this.mStateEvents = new HashMap(map);
        }
        if (objectInput.readBoolean()) {
            this.canonicalAlias = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.name = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.topic = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.url = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.avatar_url = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.mRoomCreateContent = (RoomCreateContent) objectInput.readObject();
        }
        if (objectInput.readBoolean()) {
            this.mRoomPinnedEventsContent = (RoomPinnedEventsContent) objectInput.readObject();
        }
        if (objectInput.readBoolean()) {
            this.join_rule = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.guest_access = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.history_visibility = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.visibility = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.algorithm = objectInput.readUTF();
        }
        this.mNotificationCount = objectInput.readInt();
        this.mHighlightCount = objectInput.readInt();
        if (objectInput.readBoolean()) {
            this.token = objectInput.readUTF();
        }
        for (RoomMember roomMember : (List) objectInput.readObject()) {
            this.mMembers.put(roomMember.getUserId(), roomMember);
        }
        for (RoomThirdPartyInvite roomThirdPartyInvite : (List) objectInput.readObject()) {
            this.mThirdPartyInvites.put(roomThirdPartyInvite.token, roomThirdPartyInvite);
        }
        for (RoomMember roomMember2 : (List) objectInput.readObject()) {
            this.mMembersWithThirdPartyInviteTokenCache.put(roomMember2.getThirdPartyInviteToken(), roomMember2);
        }
        this.mIsLive = objectInput.readBoolean();
        this.mAllMembersAreLoaded = objectInput.readBoolean();
        if (objectInput.readBoolean()) {
            this.groups = (List) objectInput.readObject();
        }
        if (objectInput.readBoolean()) {
            this.mRoomTombstoneContent = (RoomTombstoneContent) objectInput.readObject();
        }
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        boolean z = true;
        objectOutput.writeBoolean(this.roomId != null);
        String str = this.roomId;
        if (str != null) {
            objectOutput.writeUTF(str);
        }
        objectOutput.writeBoolean(this.powerLevels != null);
        PowerLevels powerLevels2 = this.powerLevels;
        if (powerLevels2 != null) {
            objectOutput.writeObject(powerLevels2);
        }
        objectOutput.writeBoolean(this.aliases != null);
        List<String> list = this.aliases;
        if (list != null) {
            objectOutput.writeObject(list);
        }
        objectOutput.writeObject(new ArrayList(this.mRoomAliases.values()));
        objectOutput.writeObject(this.mAliasesByDomain);
        objectOutput.writeBoolean(this.mMergedAliasesList != null);
        List<String> list2 = this.mMergedAliasesList;
        if (list2 != null) {
            objectOutput.writeObject(list2);
        }
        objectOutput.writeObject(this.mStateEvents);
        objectOutput.writeBoolean(this.canonicalAlias != null);
        String str2 = this.canonicalAlias;
        if (str2 != null) {
            objectOutput.writeUTF(str2);
        }
        objectOutput.writeBoolean(this.name != null);
        String str3 = this.name;
        if (str3 != null) {
            objectOutput.writeUTF(str3);
        }
        objectOutput.writeBoolean(this.topic != null);
        String str4 = this.topic;
        if (str4 != null) {
            objectOutput.writeUTF(str4);
        }
        objectOutput.writeBoolean(this.url != null);
        String str5 = this.url;
        if (str5 != null) {
            objectOutput.writeUTF(str5);
        }
        objectOutput.writeBoolean(this.avatar_url != null);
        String str6 = this.avatar_url;
        if (str6 != null) {
            objectOutput.writeUTF(str6);
        }
        objectOutput.writeBoolean(this.mRoomCreateContent != null);
        RoomCreateContent roomCreateContent = this.mRoomCreateContent;
        if (roomCreateContent != null) {
            objectOutput.writeObject(roomCreateContent);
        }
        objectOutput.writeBoolean(this.mRoomPinnedEventsContent != null);
        RoomPinnedEventsContent roomPinnedEventsContent = this.mRoomPinnedEventsContent;
        if (roomPinnedEventsContent != null) {
            objectOutput.writeObject(roomPinnedEventsContent);
        }
        objectOutput.writeBoolean(this.join_rule != null);
        String str7 = this.join_rule;
        if (str7 != null) {
            objectOutput.writeUTF(str7);
        }
        objectOutput.writeBoolean(this.guest_access != null);
        String str8 = this.guest_access;
        if (str8 != null) {
            objectOutput.writeUTF(str8);
        }
        objectOutput.writeBoolean(this.history_visibility != null);
        String str9 = this.history_visibility;
        if (str9 != null) {
            objectOutput.writeUTF(str9);
        }
        objectOutput.writeBoolean(this.visibility != null);
        String str10 = this.visibility;
        if (str10 != null) {
            objectOutput.writeUTF(str10);
        }
        objectOutput.writeBoolean(this.algorithm != null);
        String str11 = this.algorithm;
        if (str11 != null) {
            objectOutput.writeUTF(str11);
        }
        objectOutput.writeInt(this.mNotificationCount);
        objectOutput.writeInt(this.mHighlightCount);
        objectOutput.writeBoolean(this.token != null);
        String str12 = this.token;
        if (str12 != null) {
            objectOutput.writeUTF(str12);
        }
        objectOutput.writeObject(new ArrayList(this.mMembers.values()));
        objectOutput.writeObject(new ArrayList(this.mThirdPartyInvites.values()));
        objectOutput.writeObject(new ArrayList(this.mMembersWithThirdPartyInviteTokenCache.values()));
        objectOutput.writeBoolean(this.mIsLive);
        objectOutput.writeBoolean(this.mAllMembersAreLoaded);
        objectOutput.writeBoolean(this.groups != null);
        List<String> list3 = this.groups;
        if (list3 != null) {
            objectOutput.writeObject(list3);
        }
        if (this.mRoomTombstoneContent == null) {
            z = false;
        }
        objectOutput.writeBoolean(z);
        RoomTombstoneContent roomTombstoneContent = this.mRoomTombstoneContent;
        if (roomTombstoneContent != null) {
            objectOutput.writeObject(roomTombstoneContent);
        }
    }
}
