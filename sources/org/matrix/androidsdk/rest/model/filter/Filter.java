package org.matrix.androidsdk.rest.model.filter;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Filter {
    public Integer limit;
    @SerializedName("not_rooms")
    public List<String> notRooms;
    @SerializedName("not_senders")
    public List<String> notSenders;
    @SerializedName("not_types")
    public List<String> notTypes;
    public List<String> rooms;
    public List<String> senders;
    public List<String> types;

    public boolean hasData() {
        return (this.limit == null && this.senders == null && this.notSenders == null && this.types == null && this.notTypes == null && this.rooms == null && this.notRooms == null) ? false : true;
    }
}
