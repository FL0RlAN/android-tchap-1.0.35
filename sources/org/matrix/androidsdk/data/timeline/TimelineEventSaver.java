package org.matrix.androidsdk.data.timeline;

import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.ReceiptData;

class TimelineEventSaver {
    private final Room mRoom;
    private final IMXStore mStore;
    private final TimelineStateHolder mTimelineStateHolder;

    TimelineEventSaver(IMXStore iMXStore, Room room, TimelineStateHolder timelineStateHolder) {
        this.mStore = iMXStore;
        this.mRoom = room;
        this.mTimelineStateHolder = timelineStateHolder;
    }

    public void storeEvent(Event event) {
        String str = this.mRoom.getDataHandler().getCredentials().userId;
        if (!(event.getSender() == null || event.eventId == null)) {
            this.mRoom.handleReceiptData(new ReceiptData(event.getSender(), event.eventId, event.originServerTs));
        }
        this.mStore.storeLiveRoomEvent(event);
        if (RoomSummary.isSupportedEvent(event)) {
            RoomState state = this.mTimelineStateHolder.getState();
            RoomSummary summary = this.mStore.getSummary(event.roomId);
            if (summary == null) {
                summary = new RoomSummary(summary, event, state, str);
            } else {
                summary.setLatestReceivedEvent(event, state);
            }
            this.mStore.storeSummary(summary);
        }
    }
}
