package org.matrix.androidsdk.data;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import org.matrix.androidsdk.rest.model.Event;

public class RoomAccountData implements Serializable {
    private static final long serialVersionUID = -8406116277864521120L;
    private Map<String, RoomTag> tags = null;

    public void handleTagEvent(Event event) {
        if (event.getType().equals(Event.EVENT_TYPE_TAGS)) {
            this.tags = RoomTag.roomTagsWithTagEvent(event);
        }
    }

    public RoomTag roomTag(String str) {
        Map<String, RoomTag> map = this.tags;
        if (map == null || !map.containsKey(str)) {
            return null;
        }
        return (RoomTag) this.tags.get(str);
    }

    public boolean hasTags() {
        Map<String, RoomTag> map = this.tags;
        return map != null && map.size() > 0;
    }

    public Set<String> getKeys() {
        if (hasTags()) {
            return this.tags.keySet();
        }
        return null;
    }
}
