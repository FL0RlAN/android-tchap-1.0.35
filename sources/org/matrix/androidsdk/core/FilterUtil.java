package org.matrix.androidsdk.core;

import java.util.ArrayList;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.filter.Filter;
import org.matrix.androidsdk.rest.model.filter.FilterBody;
import org.matrix.androidsdk.rest.model.filter.RoomEventFilter;
import org.matrix.androidsdk.rest.model.filter.RoomFilter;

public class FilterUtil {
    public static void enableDataSaveMode(FilterBody filterBody, boolean z) {
        String str = Event.EVENT_TYPE_TYPING;
        String str2 = "*";
        if (z) {
            if (filterBody.room == null) {
                filterBody.room = new RoomFilter();
            }
            if (filterBody.room.ephemeral == null) {
                filterBody.room.ephemeral = new RoomEventFilter();
            }
            if (filterBody.room.ephemeral.notTypes == null) {
                filterBody.room.ephemeral.notTypes = new ArrayList();
            }
            if (!filterBody.room.ephemeral.notTypes.contains(str)) {
                filterBody.room.ephemeral.notTypes.add(str);
            }
            if (filterBody.presence == null) {
                filterBody.presence = new Filter();
            }
            if (filterBody.presence.notTypes == null) {
                filterBody.presence.notTypes = new ArrayList();
            }
            if (!filterBody.presence.notTypes.contains(str2)) {
                filterBody.presence.notTypes.add(str2);
                return;
            }
            return;
        }
        if (!(filterBody.room == null || filterBody.room.ephemeral == null || filterBody.room.ephemeral.notTypes == null)) {
            filterBody.room.ephemeral.notTypes.remove(str);
            if (filterBody.room.ephemeral.notTypes.isEmpty()) {
                filterBody.room.ephemeral.notTypes = null;
            }
            if (!filterBody.room.ephemeral.hasData()) {
                filterBody.room.ephemeral = null;
            }
            if (!filterBody.room.hasData()) {
                filterBody.room = null;
            }
        }
        if (filterBody.presence != null && filterBody.presence.notTypes != null) {
            filterBody.presence.notTypes.remove(str2);
            if (filterBody.presence.notTypes.isEmpty()) {
                filterBody.presence.notTypes = null;
            }
            if (!filterBody.presence.hasData()) {
                filterBody.presence = null;
            }
        }
    }

    public static void enableLazyLoading(FilterBody filterBody, boolean z) {
        if (z) {
            if (filterBody.room == null) {
                filterBody.room = new RoomFilter();
            }
            if (filterBody.room.state == null) {
                filterBody.room.state = new RoomEventFilter();
            }
            filterBody.room.state.lazyLoadMembers = Boolean.valueOf(true);
        } else if (filterBody.room != null && filterBody.room.state != null) {
            filterBody.room.state.lazyLoadMembers = null;
            if (!filterBody.room.state.hasData()) {
                filterBody.room.state = null;
            }
            if (!filterBody.room.hasData()) {
                filterBody.room = null;
            }
        }
    }

    public static void enableLazyLoading(RoomEventFilter roomEventFilter, boolean z) {
        roomEventFilter.lazyLoadMembers = Boolean.valueOf(z);
    }

    public static RoomEventFilter createRoomEventFilter(boolean z) {
        if (!z) {
            return null;
        }
        RoomEventFilter roomEventFilter = new RoomEventFilter();
        roomEventFilter.lazyLoadMembers = Boolean.valueOf(true);
        return roomEventFilter;
    }
}
