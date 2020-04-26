package org.matrix.androidsdk.data.timeline;

import android.text.TextUtils;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.core.EventDisplay;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.MyUser;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.data.timeline.EventTimeline.Direction;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.EventContent;

class TimelineLiveEventHandler {
    private static final String LOG_TAG = TimelineLiveEventHandler.class.getSimpleName();
    private final TimelineEventListeners mEventListeners;
    private final MXEventTimeline mEventTimeline;
    private final StateEventRedactionChecker mStateEventRedactionChecker;
    private final TimelineEventSaver mTimelineEventSaver;
    private final TimelinePushWorker mTimelinePushWorker;
    private final TimelineStateHolder mTimelineStateHolder;

    TimelineLiveEventHandler(@Nonnull MXEventTimeline mXEventTimeline, @Nonnull TimelineEventSaver timelineEventSaver, @Nonnull StateEventRedactionChecker stateEventRedactionChecker, @Nonnull TimelinePushWorker timelinePushWorker, TimelineStateHolder timelineStateHolder, TimelineEventListeners timelineEventListeners) {
        this.mEventTimeline = mXEventTimeline;
        this.mTimelineEventSaver = timelineEventSaver;
        this.mStateEventRedactionChecker = stateEventRedactionChecker;
        this.mTimelinePushWorker = timelinePushWorker;
        this.mTimelineStateHolder = timelineStateHolder;
        this.mEventListeners = timelineEventListeners;
    }

    public void handleLiveEvent(Event event, boolean z, boolean z2) {
        IMXStore store = this.mEventTimeline.getStore();
        MXDataHandler dataHandler = this.mEventTimeline.getRoom().getDataHandler();
        String timelineId = this.mEventTimeline.getTimelineId();
        MyUser myUser = dataHandler.getMyUser();
        dataHandler.decryptEvent(event, timelineId);
        boolean z3 = false;
        if (event.isCallEvent()) {
            RoomState state = this.mTimelineStateHolder.getState();
            dataHandler.getCallsManager().handleCallEvent(store, event);
            storeLiveRoomEvent(dataHandler, store, event, false);
            if (!TextUtils.equals(event.getType(), Event.EVENT_TYPE_CALL_CANDIDATES)) {
                dataHandler.onLiveEvent(event, state);
                this.mEventListeners.onEvent(event, Direction.FORWARDS, state);
            }
            if (z2) {
                this.mTimelinePushWorker.triggerPush(state, event);
            }
        } else {
            Event event2 = store.getEvent(event.eventId, event.roomId);
            if (event2 != null) {
                String str = " in ";
                String str2 = "handleLiveEvent : the event ";
                if (event2.getAge() == Event.DUMMY_EVENT_AGE) {
                    store.deleteEvent(event2);
                    store.storeLiveRoomEvent(event);
                    store.commit();
                    String str3 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append(str2);
                    sb.append(event.eventId);
                    sb.append(str);
                    sb.append(event.roomId);
                    sb.append(" has been echoed");
                    Log.d(str3, sb.toString());
                } else {
                    String str4 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str2);
                    sb2.append(event.eventId);
                    sb2.append(str);
                    sb2.append(event.roomId);
                    sb2.append(" already exist.");
                    Log.d(str4, sb2.toString());
                    return;
                }
            }
            if (event.roomId != null) {
                if ("m.room.member".equals(event.getType()) && TextUtils.equals(event.getSender(), dataHandler.getUserId())) {
                    EventContent eventContent = JsonUtils.toEventContent(event.getContentAsJsonObject());
                    EventContent prevContent = event.getPrevContent();
                    String str5 = null;
                    if (prevContent != null) {
                        str5 = prevContent.membership;
                    }
                    if (!event.isRedacted() && TextUtils.equals(str5, eventContent.membership)) {
                        if (TextUtils.equals("join", eventContent.membership)) {
                            if (!TextUtils.equals(eventContent.displayname, myUser.displayname)) {
                                myUser.displayname = eventContent.displayname;
                                store.setDisplayName(myUser.displayname, event.getOriginServerTs());
                                z3 = true;
                            }
                            if (!TextUtils.equals(eventContent.avatar_url, myUser.getAvatarUrl())) {
                                myUser.setAvatarUrl(eventContent.avatar_url);
                                store.setAvatarURL(myUser.avatar_url, event.getOriginServerTs());
                                z3 = true;
                            }
                            if (z3) {
                                dataHandler.onAccountInfoUpdate(myUser);
                            }
                        }
                    }
                }
                RoomState state2 = this.mTimelineStateHolder.getState();
                if (event.stateKey != null) {
                    this.mTimelineStateHolder.deepCopyState(Direction.FORWARDS);
                    if (!this.mTimelineStateHolder.processStateEvent(event, Direction.FORWARDS, true)) {
                        return;
                    }
                }
                storeLiveRoomEvent(dataHandler, store, event, z);
                dataHandler.onLiveEvent(event, state2);
                this.mEventListeners.onEvent(event, Direction.FORWARDS, state2);
                if (z2) {
                    this.mTimelinePushWorker.triggerPush(this.mTimelineStateHolder.getState(), event);
                }
            } else {
                String str6 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Unknown live event type: ");
                sb3.append(event.getType());
                Log.e(str6, sb3.toString());
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00b3, code lost:
        if (org.matrix.androidsdk.rest.model.Event.EVENT_TYPE_CALL_CANDIDATES.equals(r12.getType()) == false) goto L_0x00b5;
     */
    private void storeLiveRoomEvent(MXDataHandler mXDataHandler, IMXStore iMXStore, Event event, boolean z) {
        String str = mXDataHandler.getCredentials().userId;
        String str2 = "membership";
        String str3 = "m.room.member";
        boolean z2 = false;
        if (!Event.EVENT_TYPE_REDACTION.equals(event.getType())) {
            if (event.isCallEvent()) {
            }
            z2 = true;
            if (str3.equals(event.getType()) && str.equals(event.stateKey)) {
                String asString = event.getContentAsJsonObject().getAsJsonPrimitive(str2).getAsString();
                if ("leave".equals(asString) || "ban".equals(asString)) {
                    z2 = this.mEventTimeline.isHistorical();
                }
            }
        } else if (event.getRedactedEventId() != null) {
            Event event2 = iMXStore.getEvent(event.getRedactedEventId(), event.roomId);
            if (event2 != null) {
                event2.prune(event);
                this.mTimelineEventSaver.storeEvent(event2);
                this.mTimelineEventSaver.storeEvent(event);
                if (z && event2.stateKey != null) {
                    this.mStateEventRedactionChecker.checkStateEventRedaction(event);
                }
                ArrayList arrayList = new ArrayList(iMXStore.getRoomMessages(event.roomId));
                int size = arrayList.size() - 1;
                while (true) {
                    if (size < 0) {
                        break;
                    }
                    Event event3 = (Event) arrayList.get(size);
                    if (RoomSummary.isSupportedEvent(event3)) {
                        if (TextUtils.equals(event3.getType(), "m.room.encrypted") && mXDataHandler.getCrypto() != null) {
                            mXDataHandler.decryptEvent(event3, this.mEventTimeline.getTimelineId());
                        }
                        if (!TextUtils.isEmpty(new EventDisplay(iMXStore.getContext()).getTextualDisplay(event3, this.mTimelineStateHolder.getState()))) {
                            event = event3;
                            break;
                        }
                    }
                    size--;
                }
                z2 = true;
            } else if (z) {
                this.mStateEventRedactionChecker.checkStateEventRedaction(event);
            }
        }
        if (z2) {
            this.mTimelineEventSaver.storeEvent(event);
        }
        if (Event.EVENT_TYPE_STATE_ROOM_CREATE.equals(event.getType())) {
            mXDataHandler.onNewRoom(event.roomId);
        }
        if (str3.equals(event.getType()) && str.equals(event.stateKey)) {
            String asString2 = event.getContentAsJsonObject().getAsJsonPrimitive(str2).getAsString();
            if ("join".equals(asString2)) {
                mXDataHandler.onJoinRoom(event.roomId);
            } else if ("invite".equals(asString2)) {
                mXDataHandler.onNewRoom(event.roomId);
            }
        }
    }
}
