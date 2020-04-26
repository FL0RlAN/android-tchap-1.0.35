package org.matrix.androidsdk.data.timeline;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.data.timeline.EventTimeline.Direction;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.sync.RoomSync;

class TimelineJoinRoomSyncHandler {
    private static final String LOG_TAG = TimelineJoinRoomSyncHandler.class.getSimpleName();
    private final MXEventTimeline mEventTimeline;
    private final boolean mIsGlobalInitialSync;
    private final RoomSync mRoomSync;
    private final TimelineLiveEventHandler mTimelineLiveEventHandler;
    private final TimelineStateHolder mTimelineStateHolder;

    TimelineJoinRoomSyncHandler(MXEventTimeline mXEventTimeline, RoomSync roomSync, TimelineStateHolder timelineStateHolder, TimelineLiveEventHandler timelineLiveEventHandler, boolean z) {
        this.mEventTimeline = mXEventTimeline;
        this.mRoomSync = roomSync;
        this.mTimelineStateHolder = timelineStateHolder;
        this.mTimelineLiveEventHandler = timelineLiveEventHandler;
        this.mIsGlobalInitialSync = z;
    }

    public void handle() {
        IMXStore store = this.mEventTimeline.getStore();
        Room room = this.mEventTimeline.getRoom();
        MXDataHandler dataHandler = room.getDataHandler();
        String roomId = room.getRoomId();
        String str = dataHandler.getMyUser().user_id;
        RoomMember member = this.mTimelineStateHolder.getState().getMember(str);
        RoomSummary summary = store.getSummary(roomId);
        String str2 = member != null ? member.membership : null;
        String str3 = "invite";
        boolean z = str2 == null || TextUtils.equals(str2, str3);
        if (str3.equals(str2)) {
            cleanInvitedRoom(store, roomId);
        }
        if (!(this.mRoomSync.state == null || this.mRoomSync.state.events == null || this.mRoomSync.state.events.size() <= 0)) {
            handleRoomSyncState(room, store, z);
        }
        if (this.mRoomSync.timeline != null) {
            handleRoomSyncTimeline(store, str, roomId, summary, z);
        }
        if (z) {
            room.setReadyState(true);
        } else if (this.mRoomSync.timeline != null && this.mRoomSync.timeline.limited) {
            dataHandler.onRoomFlush(roomId);
        }
        if (this.mEventTimeline.isLiveTimeline()) {
            handleLiveTimeline(dataHandler, store, roomId, str, summary);
        }
    }

    private void handleRoomSyncState(Room room, IMXStore iMXStore, boolean z) {
        if (z) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("##");
            sb.append(this.mRoomSync.state.events.size());
            sb.append(" events for room ");
            sb.append(room.getRoomId());
            sb.append("in store ");
            sb.append(iMXStore);
            Log.d(str, sb.toString());
        }
        if (room.getDataHandler().isAlive()) {
            for (Event event : this.mRoomSync.state.events) {
                boolean z2 = false;
                if (this.mRoomSync.timeline.events != null) {
                    Iterator it = this.mRoomSync.timeline.events.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (TextUtils.equals(((Event) it.next()).eventId, event.eventId)) {
                                z2 = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
                if (!z2) {
                    try {
                        this.mTimelineStateHolder.processStateEvent(event, Direction.FORWARDS, true);
                    } catch (Exception e) {
                        String str2 = LOG_TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("processStateEvent failed ");
                        sb2.append(e.getMessage());
                        Log.e(str2, sb2.toString(), e);
                    }
                }
            }
            room.setReadyState(true);
        } else {
            Log.e(LOG_TAG, "## mDataHandler.isAlive() is false");
        }
        if (z) {
            RoomState state = this.mTimelineStateHolder.getState();
            String str3 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("## handleJoinedRoomSync() : retrieve X ");
            sb3.append(state.getLoadedMembers().size());
            sb3.append(" members for room ");
            sb3.append(room.getRoomId());
            Log.d(str3, sb3.toString());
            this.mTimelineStateHolder.setBackState(state.deepCopy());
        }
    }

    private void cleanInvitedRoom(IMXStore iMXStore, String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("clean invited room from the store ");
        sb.append(str);
        Log.d(str2, sb.toString());
        iMXStore.deleteRoomData(str);
        this.mTimelineStateHolder.clear();
    }

    private void handleRoomSyncTimeline(IMXStore iMXStore, String str, String str2, RoomSummary roomSummary, boolean z) {
        if (this.mRoomSync.timeline.limited) {
            if (!z) {
                RoomState state = this.mTimelineStateHolder.getState();
                Event oldestEvent = iMXStore.getOldestEvent(str2);
                iMXStore.deleteAllRoomMessages(str2, true);
                if (oldestEvent != null && RoomSummary.isSupportedEvent(oldestEvent)) {
                    if (roomSummary != null) {
                        roomSummary.setLatestReceivedEvent(oldestEvent, state);
                        iMXStore.storeSummary(roomSummary);
                    } else {
                        iMXStore.storeSummary(new RoomSummary(null, oldestEvent, state, str));
                    }
                }
                state.forceMembersRequest();
            }
            if (this.mRoomSync.timeline.prevBatch == null) {
                this.mRoomSync.timeline.prevBatch = Event.PAGINATE_BACK_TOKEN_END;
            }
            iMXStore.storeBackToken(str2, this.mRoomSync.timeline.prevBatch);
            this.mTimelineStateHolder.getBackState().setToken(null);
            this.mEventTimeline.setCanBackPaginate(true);
        }
        if (this.mRoomSync.timeline.events != null && !this.mRoomSync.timeline.events.isEmpty()) {
            List<Event> list = this.mRoomSync.timeline.events;
            ((Event) list.get(0)).mToken = this.mRoomSync.timeline.prevBatch;
            for (Event event : list) {
                event.roomId = str2;
                try {
                    this.mTimelineLiveEventHandler.handleLiveEvent(event, !(this.mRoomSync.timeline != null && this.mRoomSync.timeline.limited) && !this.mIsGlobalInitialSync, !this.mIsGlobalInitialSync && !z);
                } catch (Exception e) {
                    String str3 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("timeline event failed ");
                    sb.append(e.getMessage());
                    Log.e(str3, sb.toString(), e);
                }
            }
        }
    }

    private void handleLiveTimeline(MXDataHandler mXDataHandler, IMXStore iMXStore, String str, String str2, RoomSummary roomSummary) {
        RoomState state = this.mTimelineStateHolder.getState();
        if (iMXStore.getRoom(str) != null && iMXStore.getSummary(str) == null) {
            Event oldestEvent = iMXStore.getOldestEvent(str);
            if (oldestEvent != null) {
                iMXStore.storeSummary(new RoomSummary(null, oldestEvent, state, str2));
                iMXStore.commit();
                if (!RoomSummary.isSupportedEvent(oldestEvent)) {
                    String str3 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("the room ");
                    sb.append(str);
                    sb.append(" has no valid summary, back paginate once to find a valid one");
                    Log.e(str3, sb.toString());
                }
            } else if (roomSummary != null) {
                roomSummary.setLatestReceivedEvent(roomSummary.getLatestReceivedEvent(), state);
                iMXStore.storeSummary(roomSummary);
                iMXStore.commit();
            } else if (this.mRoomSync.state != null && this.mRoomSync.state.events != null && this.mRoomSync.state.events.size() > 0) {
                ArrayList arrayList = new ArrayList(this.mRoomSync.state.events);
                Collections.reverse(arrayList);
                Iterator it = arrayList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Event event = (Event) it.next();
                    event.roomId = str;
                    if (RoomSummary.isSupportedEvent(event)) {
                        iMXStore.storeSummary(new RoomSummary(iMXStore.getSummary(str), event, state, str2));
                        iMXStore.commit();
                        break;
                    }
                }
            }
        }
        if (this.mRoomSync.unreadNotifications != null) {
            int i = 0;
            int intValue = this.mRoomSync.unreadNotifications.highlightCount != null ? this.mRoomSync.unreadNotifications.highlightCount.intValue() : 0;
            if (this.mRoomSync.unreadNotifications.notificationCount != null) {
                i = this.mRoomSync.unreadNotifications.notificationCount.intValue();
            }
            String str4 = " - notifCount ";
            String str5 = ": highlightCount ";
            if (!(i == state.getNotificationCount() && state.getHighlightCount() == intValue)) {
                String str6 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## handleJoinedRoomSync() : update room state notifs count for room id ");
                sb2.append(str);
                sb2.append(str5);
                sb2.append(intValue);
                sb2.append(str4);
                sb2.append(i);
                Log.d(str6, sb2.toString());
                state.setNotificationCount(i);
                state.setHighlightCount(intValue);
                iMXStore.storeLiveStateForRoom(str);
                mXDataHandler.onNotificationCountUpdate(str);
            }
            RoomSummary summary = iMXStore.getSummary(str);
            if (!(summary == null || (i == summary.getNotificationCount() && summary.getHighlightCount() == intValue))) {
                String str7 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("## handleJoinedRoomSync() : update room summary notifs count for room id ");
                sb3.append(str);
                sb3.append(str5);
                sb3.append(intValue);
                sb3.append(str4);
                sb3.append(i);
                Log.d(str7, sb3.toString());
                summary.setNotificationCount(i);
                summary.setHighlightCount(intValue);
                iMXStore.flushSummary(summary);
                mXDataHandler.onNotificationCountUpdate(str);
            }
        }
        if (this.mRoomSync.roomSyncSummary != null) {
            RoomSummary summary2 = iMXStore.getSummary(str);
            if (summary2 == null) {
                Log.e(LOG_TAG, "## RoomSummary is null");
                return;
            }
            summary2.setRoomSyncSummary(this.mRoomSync.roomSyncSummary);
            iMXStore.flushSummary(summary2);
        }
    }
}
