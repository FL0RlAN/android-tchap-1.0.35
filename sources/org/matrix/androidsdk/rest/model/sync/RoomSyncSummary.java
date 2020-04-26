package org.matrix.androidsdk.rest.model.sync;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RoomSyncSummary {
    @SerializedName("m.heroes")
    public List<String> heroes;
    @SerializedName("m.invited_member_count")
    public Integer invitedMembersCount;
    @SerializedName("m.joined_member_count")
    public Integer joinedMembersCount;
}
