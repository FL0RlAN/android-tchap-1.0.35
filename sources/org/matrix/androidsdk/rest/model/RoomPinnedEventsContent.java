package org.matrix.androidsdk.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoomPinnedEventsContent implements Serializable {
    public List<String> pinned;

    public RoomPinnedEventsContent deepCopy() {
        RoomPinnedEventsContent roomPinnedEventsContent = new RoomPinnedEventsContent();
        List<String> list = this.pinned;
        if (list == null) {
            roomPinnedEventsContent.pinned = null;
        } else {
            roomPinnedEventsContent.pinned = new ArrayList(list);
        }
        return roomPinnedEventsContent;
    }
}
