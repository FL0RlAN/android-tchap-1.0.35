package org.matrix.androidsdk.data.timeline;

import javax.annotation.Nullable;
import org.apache.commons.cli.HelpFormatter;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.sync.InvitedRoomSync;

class TimelineInvitedRoomSyncHandler {
    private final InvitedRoomSync mInvitedRoomSync;
    private final TimelineLiveEventHandler mLiveEventHandler;
    private final Room mRoom;

    TimelineInvitedRoomSyncHandler(Room room, TimelineLiveEventHandler timelineLiveEventHandler, @Nullable InvitedRoomSync invitedRoomSync) {
        this.mRoom = room;
        this.mLiveEventHandler = timelineLiveEventHandler;
        this.mInvitedRoomSync = invitedRoomSync;
    }

    public void handle() {
        InvitedRoomSync invitedRoomSync = this.mInvitedRoomSync;
        if (invitedRoomSync != null && invitedRoomSync.inviteState != null && this.mInvitedRoomSync.inviteState.events != null) {
            String roomId = this.mRoom.getRoomId();
            for (Event event : this.mInvitedRoomSync.inviteState.events) {
                if (event.eventId == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(roomId);
                    String str = HelpFormatter.DEFAULT_OPT_PREFIX;
                    sb.append(str);
                    sb.append(System.currentTimeMillis());
                    sb.append(str);
                    sb.append(event.hashCode());
                    event.eventId = sb.toString();
                }
                event.roomId = roomId;
                this.mLiveEventHandler.handleLiveEvent(event, false, true);
            }
            this.mRoom.setReadyState(true);
        }
    }
}
