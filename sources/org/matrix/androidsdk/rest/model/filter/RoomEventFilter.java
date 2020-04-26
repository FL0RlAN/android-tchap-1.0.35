package org.matrix.androidsdk.rest.model.filter;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.matrix.androidsdk.core.JsonUtils;

public class RoomEventFilter {
    @SerializedName("contains_url")
    public Boolean containsUrl;
    @SerializedName("lazy_load_members")
    public Boolean lazyLoadMembers;
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
        return (this.limit == null && this.notSenders == null && this.notTypes == null && this.senders == null && this.types == null && this.rooms == null && this.notRooms == null && this.containsUrl == null && this.lazyLoadMembers == null) ? false : true;
    }

    public String toJSONString() {
        return JsonUtils.getGson(false).toJson((Object) this);
    }
}
