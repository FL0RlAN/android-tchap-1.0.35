package org.matrix.androidsdk.rest.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class RoomCreateContent implements Serializable {
    public String creator;
    @SerializedName("m.federate")
    public Boolean isFederated;
    public Predecessor predecessor;
    @SerializedName("room_version")
    public String roomVersion;

    public static class Predecessor implements Serializable {
        @SerializedName("event_id")
        public String eventId;
        @SerializedName("room_id")
        public String roomId;

        public Predecessor deepCopy() {
            Predecessor predecessor = new Predecessor();
            predecessor.roomId = this.roomId;
            predecessor.eventId = this.eventId;
            return predecessor;
        }
    }

    public RoomCreateContent deepCopy() {
        RoomCreateContent roomCreateContent = new RoomCreateContent();
        roomCreateContent.creator = this.creator;
        roomCreateContent.roomVersion = this.roomVersion;
        roomCreateContent.isFederated = this.isFederated;
        Predecessor predecessor2 = this.predecessor;
        roomCreateContent.predecessor = predecessor2 != null ? predecessor2.deepCopy() : null;
        return roomCreateContent;
    }
}
