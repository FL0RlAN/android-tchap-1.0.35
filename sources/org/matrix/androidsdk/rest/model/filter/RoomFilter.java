package org.matrix.androidsdk.rest.model.filter;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RoomFilter {
    @SerializedName("account_data")
    public RoomEventFilter accountData;
    public RoomEventFilter ephemeral;
    @SerializedName("include_leave")
    public Boolean includeLeave;
    @SerializedName("not_rooms")
    public List<String> notRooms;
    public List<String> rooms;
    public RoomEventFilter state;
    public RoomEventFilter timeline;

    public boolean hasData() {
        return (this.notRooms == null && this.rooms == null && this.ephemeral == null && this.includeLeave == null && this.state == null && this.timeline == null && this.accountData == null) ? false : true;
    }
}
