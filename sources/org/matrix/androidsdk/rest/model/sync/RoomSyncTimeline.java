package org.matrix.androidsdk.rest.model.sync;

import java.util.List;
import org.matrix.androidsdk.rest.model.Event;

public class RoomSyncTimeline {
    public List<Event> events;
    public boolean limited;
    public String prevBatch;
}
