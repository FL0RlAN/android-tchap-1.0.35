package org.matrix.androidsdk.rest.model.filter;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import org.matrix.androidsdk.core.JsonUtils;

public class FilterBody {
    private static final String LOG_TAG = FilterBody.class.getSimpleName();
    @SerializedName("account_data")
    public Filter accountData;
    @SerializedName("event_fields")
    public List<String> eventFields;
    @SerializedName("event_format")
    public String eventFormat;
    public Filter presence;
    public RoomFilter room;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(LOG_TAG);
        sb.append(toJSONString());
        return sb.toString();
    }

    public String toJSONString() {
        return JsonUtils.getGson(false).toJson((Object) this);
    }
}
