package org.matrix.androidsdk.data.timeline;

import android.text.TextUtils;
import java.util.List;
import javax.annotation.Nonnull;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.data.timeline.EventTimeline.Direction;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.RoomMember;

class StateEventRedactionChecker {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = StateEventRedactionChecker.class.getSimpleName();
    /* access modifiers changed from: private */
    public final EventTimeline mEventTimeline;
    /* access modifiers changed from: private */
    public final TimelineStateHolder mTimelineStateHolder;

    StateEventRedactionChecker(EventTimeline eventTimeline, TimelineStateHolder timelineStateHolder) {
        this.mEventTimeline = eventTimeline;
        this.mTimelineStateHolder = timelineStateHolder;
    }

    public void checkStateEventRedaction(Event event) {
        IMXStore store = this.mEventTimeline.getStore();
        Room room = this.mEventTimeline.getRoom();
        final MXDataHandler dataHandler = room.getDataHandler();
        final String roomId = room.getRoomId();
        final String redactedEventId = event.getRedactedEventId();
        RoomState state = this.mTimelineStateHolder.getState();
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("checkStateEventRedaction of event ");
        sb.append(redactedEventId);
        Log.d(str, sb.toString());
        final Event event2 = event;
        final RoomState roomState = state;
        final IMXStore iMXStore = store;
        AnonymousClass1 r1 = new SimpleApiCallback<List<Event>>() {
            public void onSuccess(List<Event> list) {
                boolean z = false;
                int i = 0;
                while (true) {
                    if (i >= list.size()) {
                        break;
                    }
                    Event event = (Event) list.get(i);
                    if (TextUtils.equals(event.eventId, redactedEventId)) {
                        Log.d(StateEventRedactionChecker.LOG_TAG, "checkStateEventRedaction: the current room state has been modified by the event redaction");
                        event.prune(event2);
                        list.set(i, event);
                        StateEventRedactionChecker.this.mTimelineStateHolder.processStateEvent(event, Direction.FORWARDS, true);
                        z = true;
                        break;
                    }
                    i++;
                }
                if (!z) {
                    RoomMember memberByEventId = roomState.getMemberByEventId(redactedEventId);
                    if (memberByEventId != null) {
                        Log.d(StateEventRedactionChecker.LOG_TAG, "checkStateEventRedaction: the current room members list has been modified by the event redaction");
                        memberByEventId.prune();
                        z = true;
                    }
                }
                if (z) {
                    iMXStore.storeLiveStateForRoom(roomId);
                    StateEventRedactionChecker.this.mEventTimeline.initHistory();
                    dataHandler.onRoomFlush(roomId);
                    return;
                }
                Log.d(StateEventRedactionChecker.LOG_TAG, "checkStateEventRedaction: the redacted event is unknown. Fetch it from the homeserver");
                StateEventRedactionChecker.this.checkStateEventRedactionWithHomeserver(dataHandler, roomId, redactedEventId);
            }
        };
        state.getStateEvents(store, null, r1);
    }

    /* access modifiers changed from: private */
    public void checkStateEventRedactionWithHomeserver(@Nonnull MXDataHandler mXDataHandler, @Nonnull String str, @Nonnull String str2) {
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("checkStateEventRedactionWithHomeserver on event Id ");
        sb.append(str2);
        Log.d(str3, sb.toString());
        if (!TextUtils.isEmpty(str2)) {
            Log.d(LOG_TAG, "checkStateEventRedactionWithHomeserver : retrieving the event");
            mXDataHandler.getDataRetriever().getRoomsRestClient().getEvent(str, str2, new ApiCallback<Event>() {
                public void onSuccess(Event event) {
                    if (event == null || event.stateKey == null) {
                        Log.d(StateEventRedactionChecker.LOG_TAG, "checkStateEventRedactionWithHomeserver : the redacted event is a not state event -> job is done");
                    } else {
                        Log.d(StateEventRedactionChecker.LOG_TAG, "checkStateEventRedactionWithHomeserver : the redacted event is a state event in the past. TODO: prune prev_content of the new state event");
                    }
                }

                public void onNetworkError(Exception exc) {
                    String access$000 = StateEventRedactionChecker.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("checkStateEventRedactionWithHomeserver : failed to retrieved the redacted event: onNetworkError ");
                    sb.append(exc.getMessage());
                    Log.e(access$000, sb.toString(), exc);
                }

                public void onMatrixError(MatrixError matrixError) {
                    String access$000 = StateEventRedactionChecker.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("checkStateEventRedactionWithHomeserver : failed to retrieved the redacted event: onNetworkError ");
                    sb.append(matrixError.getMessage());
                    Log.e(access$000, sb.toString());
                }

                public void onUnexpectedError(Exception exc) {
                    String access$000 = StateEventRedactionChecker.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("checkStateEventRedactionWithHomeserver : failed to retrieved the redacted event: onNetworkError ");
                    sb.append(exc.getMessage());
                    Log.e(access$000, sb.toString(), exc);
                }
            });
        }
    }
}
