package org.matrix.androidsdk.data.room;

import android.text.TextUtils;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.rest.model.RoomMember;

public class RoomAvatarResolver {
    private static final String LOG_TAG = RoomAvatarResolver.class.getSimpleName();
    private final Room mRoom;

    public RoomAvatarResolver(Room room) {
        this.mRoom = room;
    }

    public String resolve() {
        String avatarUrl = this.mRoom.getState().getAvatarUrl();
        if (avatarUrl != null) {
            return avatarUrl;
        }
        if (this.mRoom.isInvited()) {
            if (this.mRoom.getState().getLoadedMembers().size() == 1) {
                return ((RoomMember) this.mRoom.getState().getLoadedMembers().get(0)).getAvatarUrl();
            }
            if (this.mRoom.getState().getLoadedMembers().size() <= 1) {
                return avatarUrl;
            }
            RoomMember roomMember = (RoomMember) this.mRoom.getState().getLoadedMembers().get(0);
            return TextUtils.equals(roomMember.getUserId(), this.mRoom.getDataHandler().getUserId()) ? ((RoomMember) this.mRoom.getState().getLoadedMembers().get(1)).getAvatarUrl() : roomMember.getAvatarUrl();
        } else if (this.mRoom.getNumberOfMembers() == 1 && !this.mRoom.getState().getLoadedMembers().isEmpty()) {
            return ((RoomMember) this.mRoom.getState().getLoadedMembers().get(0)).getAvatarUrl();
        } else {
            if (this.mRoom.getNumberOfMembers() != 2 || this.mRoom.getState().getLoadedMembers().size() <= 1) {
                return avatarUrl;
            }
            RoomMember roomMember2 = (RoomMember) this.mRoom.getState().getLoadedMembers().get(0);
            return TextUtils.equals(roomMember2.getUserId(), this.mRoom.getDataHandler().getUserId()) ? ((RoomMember) this.mRoom.getState().getLoadedMembers().get(1)).getAvatarUrl() : roomMember2.getAvatarUrl();
        }
    }
}
