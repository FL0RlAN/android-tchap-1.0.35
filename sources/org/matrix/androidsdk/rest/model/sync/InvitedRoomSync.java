package org.matrix.androidsdk.rest.model.sync;

import com.google.gson.annotations.SerializedName;

public class InvitedRoomSync {
    @SerializedName("invite_state")
    public RoomInviteState inviteState;
}
