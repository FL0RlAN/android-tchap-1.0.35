package org.matrix.androidsdk.listeners;

import android.text.TextUtils;
import java.util.List;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.User;

public class MXRoomEventListener extends MXEventListener {
    private static final String LOG_TAG = MXRoomEventListener.class.getSimpleName();
    private final IMXEventListener mEventListener;
    private final Room mRoom;
    private final String mRoomId;

    public MXRoomEventListener(Room room, IMXEventListener iMXEventListener) {
        this.mRoom = room;
        this.mRoomId = room.getRoomId();
        this.mEventListener = iMXEventListener;
    }

    public void onPresenceUpdate(Event event, User user) {
        if (this.mRoom.getMember(user.user_id) != null) {
            try {
                this.mEventListener.onPresenceUpdate(event, user);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onPresenceUpdate exception ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
    }

    public void onLiveEvent(Event event, RoomState roomState) {
        if (TextUtils.equals(this.mRoomId, event.roomId) && this.mRoom.isReady()) {
            try {
                this.mEventListener.onLiveEvent(event, roomState);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onLiveEvent exception ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
    }

    public void onLiveEventsChunkProcessed(String str, String str2) {
        try {
            this.mEventListener.onLiveEventsChunkProcessed(str, str2);
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onLiveEventsChunkProcessed exception ");
            sb.append(e.getMessage());
            Log.e(str3, sb.toString(), e);
        }
    }

    public void onEventSentStateUpdated(Event event) {
        if (TextUtils.equals(this.mRoomId, event.roomId)) {
            try {
                this.mEventListener.onEventSentStateUpdated(event);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onEventSentStateUpdated exception ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
    }

    public void onEventDecrypted(String str, String str2) {
        if (TextUtils.equals(this.mRoomId, str)) {
            try {
                this.mEventListener.onEventDecrypted(str, str2);
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onDecryptedEvent exception ");
                sb.append(e.getMessage());
                Log.e(str3, sb.toString(), e);
            }
        }
    }

    public void onEventSent(Event event, String str) {
        if (TextUtils.equals(this.mRoomId, event.roomId)) {
            try {
                this.mEventListener.onEventSent(event, str);
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onEventSent exception ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
    }

    public void onRoomInternalUpdate(String str) {
        if (TextUtils.equals(this.mRoomId, str)) {
            try {
                this.mEventListener.onRoomInternalUpdate(str);
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onRoomInternalUpdate exception ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
    }

    public void onNotificationCountUpdate(String str) {
        if (TextUtils.equals(this.mRoomId, str)) {
            try {
                this.mEventListener.onNotificationCountUpdate(str);
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onNotificationCountUpdate exception ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
    }

    public void onNewRoom(String str) {
        if (TextUtils.equals(this.mRoomId, str)) {
            try {
                this.mEventListener.onNewRoom(str);
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onNewRoom exception ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
    }

    public void onJoinRoom(String str) {
        if (TextUtils.equals(this.mRoomId, str)) {
            try {
                this.mEventListener.onJoinRoom(str);
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onJoinRoom exception ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
    }

    public void onReceiptEvent(String str, List<String> list) {
        if (TextUtils.equals(this.mRoomId, str)) {
            try {
                this.mEventListener.onReceiptEvent(str, list);
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onReceiptEvent exception ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
    }

    public void onRoomTagEvent(String str) {
        if (TextUtils.equals(this.mRoomId, str)) {
            try {
                this.mEventListener.onRoomTagEvent(str);
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onRoomTagEvent exception ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
    }

    public void onReadMarkerEvent(String str) {
        if (TextUtils.equals(this.mRoomId, str)) {
            try {
                this.mEventListener.onReadMarkerEvent(str);
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onReadMarkerEvent exception ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
    }

    public void onRoomFlush(String str) {
        if (TextUtils.equals(this.mRoomId, str)) {
            try {
                this.mEventListener.onRoomFlush(str);
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onRoomFlush exception ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
    }

    public void onLeaveRoom(String str) {
        if (TextUtils.equals(this.mRoomId, str)) {
            try {
                this.mEventListener.onLeaveRoom(str);
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onLeaveRoom exception ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
    }

    public void onRoomKick(String str) {
        if (TextUtils.equals(this.mRoomId, str)) {
            try {
                this.mEventListener.onRoomKick(str);
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onRoomKick exception ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
    }
}
