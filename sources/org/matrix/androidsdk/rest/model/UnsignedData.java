package org.matrix.androidsdk.rest.model;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class UnsignedData implements Serializable {
    public Long age;
    @SerializedName("invite_room_state")
    public List<Event> inviteRoomState;
    public transient JsonElement prev_content;
    public RedactedBecause redacted_because;
    public String transaction_id;
}
