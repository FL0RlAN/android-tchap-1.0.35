package org.matrix.androidsdk.rest.model.sync;

import com.google.gson.annotations.SerializedName;

public class RoomSync {
    public RoomSyncAccountData accountData;
    public RoomSyncEphemeral ephemeral;
    @SerializedName("summary")
    public RoomSyncSummary roomSyncSummary;
    public RoomSyncState state;
    public RoomSyncTimeline timeline;
    public RoomSyncUnreadNotifications unreadNotifications;
}
