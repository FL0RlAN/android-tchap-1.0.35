package org.matrix.androidsdk.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.RoomTags;

public class RoomTag implements Serializable {
    private static final String LOG_TAG = RoomTag.class.getSimpleName();
    public static final String ROOM_TAG_FAVOURITE = "m.favourite";
    public static final String ROOM_TAG_LOW_PRIORITY = "m.lowpriority";
    public static final String ROOM_TAG_NO_TAG = "m.recent";
    public static final String ROOM_TAG_SERVER_NOTICE = "m.server_notice";
    private static final long serialVersionUID = 5172602958896551204L;
    public String mName;
    public Double mOrder;

    public RoomTag(String str, Double d) {
        this.mName = str;
        this.mOrder = d;
    }

    public static Map<String, RoomTag> roomTagsWithTagEvent(Event event) {
        HashMap hashMap = new HashMap();
        try {
            RoomTags roomTags = JsonUtils.toRoomTags(event.getContent());
            if (!(roomTags.tags == null || roomTags.tags.size() == 0)) {
                for (String str : roomTags.tags.keySet()) {
                    Map map = (Map) roomTags.tags.get(str);
                    if (map != null) {
                        hashMap.put(str, new RoomTag(str, (Double) map.get("order")));
                    } else {
                        hashMap.put(str, new RoomTag(str, null));
                    }
                }
            }
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("roomTagsWithTagEvent fails ");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString(), e);
        }
        return hashMap;
    }
}
