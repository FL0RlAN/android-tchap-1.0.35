package org.matrix.androidsdk.data.timeline;

import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.data.store.MXMemoryStore;

public class EventTimelineFactory {
    public static EventTimeline liveTimeline(MXDataHandler mXDataHandler, IMXStore iMXStore, Room room, String str) {
        MXEventTimeline mXEventTimeline = new MXEventTimeline(iMXStore, mXDataHandler, room, str, null, true);
        return mXEventTimeline;
    }

    public static EventTimeline inMemoryTimeline(MXDataHandler mXDataHandler, String str) {
        return inMemoryTimeline(mXDataHandler, str, null);
    }

    public static EventTimeline pastTimeline(MXDataHandler mXDataHandler, String str, String str2) {
        return inMemoryTimeline(mXDataHandler, str, str2);
    }

    private static EventTimeline inMemoryTimeline(MXDataHandler mXDataHandler, String str, String str2) {
        MXMemoryStore mXMemoryStore = new MXMemoryStore(mXDataHandler.getCredentials(), null);
        Room room = mXDataHandler.getRoom((IMXStore) mXMemoryStore, str, true);
        MXEventTimeline mXEventTimeline = new MXEventTimeline(mXMemoryStore, mXDataHandler, room, str, str2, false);
        room.setTimeline(mXEventTimeline);
        room.setReadyState(true);
        return mXEventTimeline;
    }
}
