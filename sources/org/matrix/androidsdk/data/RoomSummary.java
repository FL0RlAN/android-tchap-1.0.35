package org.matrix.androidsdk.data;

import android.text.TextUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.matrix.androidsdk.call.MXCallsManager;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.EventContent;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.message.Message;
import org.matrix.androidsdk.rest.model.sync.RoomSyncSummary;

public class RoomSummary implements Serializable {
    private static final String LOG_TAG = RoomSummary.class.getSimpleName();
    private static RoomSummaryListener mRoomSummaryListener = null;
    private static final List<String> sKnownUnsupportedType = Arrays.asList(new String[]{Event.EVENT_TYPE_TYPING, Event.EVENT_TYPE_STATE_ROOM_POWER_LEVELS, Event.EVENT_TYPE_STATE_ROOM_JOIN_RULES, Event.EVENT_TYPE_STATE_CANONICAL_ALIAS, Event.EVENT_TYPE_STATE_ROOM_ALIASES, Event.EVENT_TYPE_URL_PREVIEW, Event.EVENT_TYPE_STATE_RELATED_GROUPS, Event.EVENT_TYPE_STATE_ROOM_GUEST_ACCESS, Event.EVENT_TYPE_REDACTION});
    private static final List<String> sSupportedType = Arrays.asList(new String[]{Event.EVENT_TYPE_STATE_ROOM_TOPIC, "m.room.encrypted", "m.room.encryption", Event.EVENT_TYPE_STATE_ROOM_NAME, "m.room.member", Event.EVENT_TYPE_STATE_ROOM_CREATE, Event.EVENT_TYPE_STATE_HISTORY_VISIBILITY, Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE, Event.EVENT_TYPE_STICKER});
    private static final long serialVersionUID = -3683013938626566489L;
    private List<String> mHeroes = new ArrayList();
    public int mHighlightsCount;
    private int mInvitedMembersCountFromSyncRoomSummary;
    private String mInviterName = null;
    private String mInviterUserId = null;
    private Boolean mIsConferenceUserRoom = null;
    private int mJoinedMembersCountFromSyncRoomSummary;
    private Event mLatestReceivedEvent = null;
    private transient RoomState mLatestRoomState = null;
    public int mNotificationCount;
    private String mReadMarkerEventId;
    private String mReadReceiptEventId;
    private String mRoomId = null;
    private Set<String> mRoomTags;
    private String mTopic = null;
    public int mUnreadEventsCount;
    private String mUserId = null;
    private String mUserMembership;

    public interface RoomSummaryListener {
        boolean isSupportedEvent(Event event);
    }

    public RoomSummary() {
    }

    public RoomSummary(RoomSummary roomSummary, Event event, RoomState roomState, String str) {
        this.mUserId = str;
        if (roomState != null) {
            setRoomId(roomState.roomId);
        }
        if (getRoomId() == null && event != null) {
            setRoomId(event.roomId);
        }
        setLatestReceivedEvent(event, roomState);
        if (roomSummary == null) {
            if (event != null) {
                setReadMarkerEventId(event.eventId);
                setReadReceiptEventId(event.eventId);
            }
            if (roomState != null) {
                setHighlightCount(roomState.getHighlightCount());
                setNotificationCount(roomState.getHighlightCount());
            }
            setUnreadEventsCount(Math.max(getHighlightCount(), getNotificationCount()));
            return;
        }
        setReadMarkerEventId(roomSummary.getReadMarkerEventId());
        setReadReceiptEventId(roomSummary.getReadReceiptEventId());
        setUnreadEventsCount(roomSummary.getUnreadEventsCount());
        setHighlightCount(roomSummary.getHighlightCount());
        setNotificationCount(roomSummary.getNotificationCount());
        this.mHeroes.addAll(roomSummary.mHeroes);
        this.mJoinedMembersCountFromSyncRoomSummary = roomSummary.mJoinedMembersCountFromSyncRoomSummary;
        this.mInvitedMembersCountFromSyncRoomSummary = roomSummary.mInvitedMembersCountFromSyncRoomSummary;
        this.mUserMembership = roomSummary.mUserMembership;
    }

    public static void setRoomSummaryListener(RoomSummaryListener roomSummaryListener) {
        mRoomSummaryListener = roomSummaryListener;
    }

    public static boolean isSupportedEvent(Event event) {
        RoomSummaryListener roomSummaryListener = mRoomSummaryListener;
        if (roomSummaryListener != null) {
            return roomSummaryListener.isSupportedEvent(event);
        }
        return isSupportedEventDefaultImplementation(event);
    }

    public static boolean isSupportedEventDefaultImplementation(Event event) {
        String type = event.getType();
        boolean z = false;
        if (TextUtils.equals(Event.EVENT_TYPE_MESSAGE, type)) {
            try {
                String str = "";
                JsonElement jsonElement = event.getContentAsJsonObject().get("msgtype");
                if (jsonElement != null) {
                    str = jsonElement.getAsString();
                }
                if (TextUtils.equals(str, Message.MSGTYPE_TEXT) || TextUtils.equals(str, Message.MSGTYPE_EMOTE) || TextUtils.equals(str, Message.MSGTYPE_NOTICE) || TextUtils.equals(str, Message.MSGTYPE_IMAGE) || TextUtils.equals(str, Message.MSGTYPE_AUDIO) || TextUtils.equals(str, Message.MSGTYPE_VIDEO) || TextUtils.equals(str, Message.MSGTYPE_FILE)) {
                    z = true;
                }
                if (!z && !TextUtils.isEmpty(str)) {
                    String str2 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("isSupportedEvent : Unsupported msg type ");
                    sb.append(str);
                    Log.e(str2, sb.toString());
                }
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("isSupportedEvent failed ");
                sb2.append(e.getMessage());
                Log.e(str3, sb2.toString(), e);
            }
        } else if (TextUtils.equals("m.room.encrypted", type)) {
            z = event.hasContentFields();
        } else if (TextUtils.equals("m.room.member", type)) {
            JsonObject contentAsJsonObject = event.getContentAsJsonObject();
            if (contentAsJsonObject != null) {
                if (contentAsJsonObject.entrySet().isEmpty()) {
                    Log.d(LOG_TAG, "isSupportedEvent : room member with no content is not supported");
                } else {
                    EventContent prevContent = event.getPrevContent();
                    EventContent eventContent = event.getEventContent();
                    String str4 = null;
                    CharSequence charSequence = eventContent != null ? eventContent.membership : null;
                    if (prevContent != null) {
                        str4 = prevContent.membership;
                    }
                    z = !TextUtils.equals(charSequence, str4);
                    if (!z) {
                        Log.d(LOG_TAG, "isSupportedEvent : do not support avatar display name update");
                    }
                }
            }
        } else if (sSupportedType.contains(type) || (event.isCallEvent() && !TextUtils.isEmpty(type) && !Event.EVENT_TYPE_CALL_CANDIDATES.equals(type))) {
            z = true;
        }
        if (!z && !sKnownUnsupportedType.contains(type)) {
            String str5 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("isSupportedEvent :  Unsupported event type ");
            sb3.append(type);
            Log.e(str5, sb3.toString());
        }
        return z;
    }

    public String getUserId() {
        return this.mUserId;
    }

    public String getRoomId() {
        return this.mRoomId;
    }

    public String getRoomTopic() {
        return this.mTopic;
    }

    public Event getLatestReceivedEvent() {
        return this.mLatestReceivedEvent;
    }

    public RoomState getLatestRoomState() {
        return this.mLatestRoomState;
    }

    public boolean isInvited() {
        return "invite".equals(this.mUserMembership);
    }

    public void setIsInvited() {
        this.mUserMembership = "invite";
    }

    public void setIsJoined() {
        this.mUserMembership = "join";
    }

    public boolean isJoined() {
        return "join".equals(this.mUserMembership);
    }

    public String getInviterUserId() {
        return this.mInviterUserId;
    }

    public RoomSummary setTopic(String str) {
        this.mTopic = str;
        return this;
    }

    public RoomSummary setRoomId(String str) {
        this.mRoomId = str;
        return this;
    }

    public RoomSummary setLatestReceivedEvent(Event event, RoomState roomState) {
        setLatestReceivedEvent(event);
        setLatestRoomState(roomState);
        if (roomState != null) {
            setTopic(roomState.topic);
        }
        return this;
    }

    public RoomSummary setLatestReceivedEvent(Event event) {
        this.mLatestReceivedEvent = event;
        return this;
    }

    public RoomSummary setLatestRoomState(RoomState roomState) {
        this.mLatestRoomState = roomState;
        RoomState roomState2 = this.mLatestRoomState;
        boolean z = false;
        if (roomState2 != null) {
            RoomMember member = roomState2.getMember(this.mUserId);
            if (member != null) {
                if ("invite".equals(member.membership)) {
                    z = true;
                }
            }
        }
        if (z) {
            this.mInviterName = null;
            Event event = this.mLatestReceivedEvent;
            if (event != null) {
                String sender = event.getSender();
                this.mInviterUserId = sender;
                this.mInviterName = sender;
                RoomState roomState3 = this.mLatestRoomState;
                if (roomState3 != null) {
                    this.mInviterName = roomState3.getMemberName(this.mLatestReceivedEvent.getSender());
                }
            }
        } else {
            this.mInviterName = null;
            this.mInviterUserId = null;
        }
        return this;
    }

    public void setReadReceiptEventId(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## setReadReceiptEventId() : ");
        sb.append(str);
        sb.append(" roomId ");
        sb.append(getRoomId());
        Log.d(str2, sb.toString());
        this.mReadReceiptEventId = str;
    }

    public String getReadReceiptEventId() {
        return this.mReadReceiptEventId;
    }

    public void setReadMarkerEventId(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## setReadMarkerEventId() : ");
        sb.append(str);
        sb.append(" roomId ");
        sb.append(getRoomId());
        Log.d(str2, sb.toString());
        if (TextUtils.isEmpty(str)) {
            String str3 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## setReadMarkerEventId') : null mReadMarkerEventId, in ");
            sb2.append(getRoomId());
            Log.e(str3, sb2.toString());
        }
        this.mReadMarkerEventId = str;
    }

    public String getReadMarkerEventId() {
        if (TextUtils.isEmpty(this.mReadMarkerEventId)) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getReadMarkerEventId') : null mReadMarkerEventId, in ");
            sb.append(getRoomId());
            Log.e(str, sb.toString());
            this.mReadMarkerEventId = getReadReceiptEventId();
        }
        return this.mReadMarkerEventId;
    }

    public void setUnreadEventsCount(int i) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## setUnreadEventsCount() : ");
        sb.append(i);
        sb.append(" roomId ");
        sb.append(getRoomId());
        Log.d(str, sb.toString());
        this.mUnreadEventsCount = i;
    }

    public int getUnreadEventsCount() {
        return this.mUnreadEventsCount;
    }

    public void setNotificationCount(int i) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## setNotificationCount() : ");
        sb.append(i);
        sb.append(" roomId ");
        sb.append(getRoomId());
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
        sb.append(" roomId ");
        sb.append(getRoomId());
        Log.d(str, sb.toString());
        this.mHighlightsCount = i;
    }

    public int getHighlightCount() {
        return this.mHighlightsCount;
    }

    public Set<String> getRoomTags() {
        return this.mRoomTags;
    }

    public void setRoomTags(Set<String> set) {
        if (set != null) {
            this.mRoomTags = new HashSet(set);
        } else {
            this.mRoomTags = new HashSet();
        }
    }

    public boolean isConferenceUserRoom() {
        if (this.mIsConferenceUserRoom == null) {
            this.mIsConferenceUserRoom = Boolean.valueOf(false);
            List heroes = getHeroes();
            if (2 == heroes.size()) {
                Iterator it = heroes.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (MXCallsManager.isConferenceUserId((String) it.next())) {
                            this.mIsConferenceUserRoom = Boolean.valueOf(true);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return this.mIsConferenceUserRoom.booleanValue();
    }

    public void setIsConferenceUserRoom(boolean z) {
        this.mIsConferenceUserRoom = Boolean.valueOf(z);
    }

    public void setRoomSyncSummary(RoomSyncSummary roomSyncSummary) {
        if (roomSyncSummary.heroes != null) {
            this.mHeroes.clear();
            this.mHeroes.addAll(roomSyncSummary.heroes);
        }
        if (roomSyncSummary.joinedMembersCount != null) {
            this.mJoinedMembersCountFromSyncRoomSummary = roomSyncSummary.joinedMembersCount.intValue();
        }
        if (roomSyncSummary.invitedMembersCount != null) {
            this.mInvitedMembersCountFromSyncRoomSummary = roomSyncSummary.invitedMembersCount.intValue();
        }
    }

    public List<String> getHeroes() {
        return this.mHeroes;
    }

    public int getNumberOfJoinedMembers() {
        return this.mJoinedMembersCountFromSyncRoomSummary;
    }

    public int getNumberOfInvitedMembers() {
        return this.mInvitedMembersCountFromSyncRoomSummary;
    }
}
