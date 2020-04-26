package org.matrix.androidsdk.rest.model.publicroom;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class PublicRoom implements Serializable {
    public List<String> aliases;
    @SerializedName("avatar_url")
    public String avatarUrl;
    @SerializedName("canonical_alias")
    public String canonicalAlias;
    @SerializedName("guest_can_join")
    public boolean guestCanJoin;
    public String name;
    @SerializedName("num_joined_members")
    public int numJoinedMembers;
    @SerializedName("room_id")
    public String roomId;
    public String topic;
    @SerializedName("world_readable")
    public boolean worldReadable;
}
