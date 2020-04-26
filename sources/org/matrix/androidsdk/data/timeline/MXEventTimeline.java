package org.matrix.androidsdk.data.timeline;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.core.FilterUtil;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.data.timeline.EventTimeline.Direction;
import org.matrix.androidsdk.data.timeline.EventTimeline.Listener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.EventContext;
import org.matrix.androidsdk.rest.model.TokensChunkEvents;
import org.matrix.androidsdk.rest.model.sync.InvitedRoomSync;
import org.matrix.androidsdk.rest.model.sync.RoomSync;

class MXEventTimeline implements EventTimeline {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = MXEventTimeline.class.getSimpleName();
    private static final int MAX_EVENT_COUNT_PER_PAGINATION = 30;
    /* access modifiers changed from: private */
    public String mBackwardTopToken = "not yet found";
    public boolean mCanBackPaginate = true;
    /* access modifiers changed from: private */
    public MXDataHandler mDataHandler;
    /* access modifiers changed from: private */
    public final TimelineEventListeners mEventListeners;
    /* access modifiers changed from: private */
    public String mForwardsPaginationToken;
    /* access modifiers changed from: private */
    public boolean mHasReachedHomeServerForwardsPaginationEnd;
    private String mInitialEventId;
    /* access modifiers changed from: private */
    public boolean mIsBackPaginating = false;
    /* access modifiers changed from: private */
    public boolean mIsForwardPaginating = false;
    private boolean mIsHistorical;
    /* access modifiers changed from: private */
    public boolean mIsLastBackChunk;
    private boolean mIsLiveTimeline;
    private final TimelineLiveEventHandler mLiveEventHandler;
    private final Room mRoom;
    private String mRoomId;
    /* access modifiers changed from: private */
    public final List<SnapshotEvent> mSnapshotEvents;
    private final TimelineStateHolder mStateHolder;
    private IMXStore mStore;
    private final TimelineEventSaver mTimelineEventSaver;
    private final String mTimelineId;

    public class SnapshotEvent {
        public final Event mEvent;
        public final RoomState mState;

        public SnapshotEvent(Event event, RoomState roomState) {
            this.mEvent = event;
            this.mState = roomState;
        }
    }

    MXEventTimeline(IMXStore iMXStore, MXDataHandler mXDataHandler, Room room, String str, String str2, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        sb.append("");
        this.mTimelineId = sb.toString();
        this.mSnapshotEvents = new ArrayList();
        this.mIsLiveTimeline = z;
        this.mInitialEventId = str2;
        this.mDataHandler = mXDataHandler;
        this.mRoom = room;
        this.mRoomId = str;
        this.mStore = iMXStore;
        this.mEventListeners = new TimelineEventListeners();
        this.mStateHolder = new TimelineStateHolder(this.mDataHandler, this.mStore, str);
        StateEventRedactionChecker stateEventRedactionChecker = new StateEventRedactionChecker(this, this.mStateHolder);
        this.mTimelineEventSaver = new TimelineEventSaver(this.mStore, this.mRoom, this.mStateHolder);
        TimelineLiveEventHandler timelineLiveEventHandler = new TimelineLiveEventHandler(this, this.mTimelineEventSaver, stateEventRedactionChecker, new TimelinePushWorker(this.mDataHandler), this.mStateHolder, this.mEventListeners);
        this.mLiveEventHandler = timelineLiveEventHandler;
    }

    public void setIsHistorical(boolean z) {
        this.mIsHistorical = z;
    }

    public boolean isHistorical() {
        return this.mIsHistorical;
    }

    public String getTimelineId() {
        return this.mTimelineId;
    }

    public Room getRoom() {
        return this.mRoom;
    }

    public IMXStore getStore() {
        return this.mStore;
    }

    public String getInitialEventId() {
        return this.mInitialEventId;
    }

    public boolean isLiveTimeline() {
        return this.mIsLiveTimeline;
    }

    public boolean hasReachedHomeServerForwardsPaginationEnd() {
        return this.mHasReachedHomeServerForwardsPaginationEnd;
    }

    public void initHistory() {
        setBackState(getState().deepCopy());
        this.mCanBackPaginate = true;
        this.mIsBackPaginating = false;
        this.mIsForwardPaginating = false;
        MXDataHandler mXDataHandler = this.mDataHandler;
        if (mXDataHandler != null && mXDataHandler.getDataRetriever() != null) {
            this.mDataHandler.resetReplayAttackCheckInTimeline(getTimelineId());
            this.mDataHandler.getDataRetriever().cancelHistoryRequests(this.mRoomId);
        }
    }

    public RoomState getState() {
        return this.mStateHolder.getState();
    }

    public void setState(RoomState roomState) {
        this.mStateHolder.setState(roomState);
    }

    private void setBackState(RoomState roomState) {
        this.mStateHolder.setBackState(roomState);
    }

    /* access modifiers changed from: private */
    public RoomState getBackState() {
        return this.mStateHolder.getBackState();
    }

    /* access modifiers changed from: protected */
    public void setCanBackPaginate(boolean z) {
        this.mCanBackPaginate = z;
    }

    private void deepCopyState(Direction direction) {
        this.mStateHolder.deepCopyState(direction);
    }

    /* access modifiers changed from: private */
    public boolean processStateEvent(Event event, Direction direction, boolean z) {
        return this.mStateHolder.processStateEvent(event, direction, z);
    }

    public void handleInvitedRoomSync(InvitedRoomSync invitedRoomSync) {
        new TimelineInvitedRoomSyncHandler(this.mRoom, this.mLiveEventHandler, invitedRoomSync).handle();
    }

    public void handleJoinedRoomSync(RoomSync roomSync, boolean z) {
        TimelineJoinRoomSyncHandler timelineJoinRoomSyncHandler = new TimelineJoinRoomSyncHandler(this, roomSync, this.mStateHolder, this.mLiveEventHandler, z);
        timelineJoinRoomSyncHandler.handle();
    }

    public void storeOutgoingEvent(Event event) {
        if (this.mIsLiveTimeline) {
            storeEvent(event);
        }
    }

    private void storeEvent(Event event) {
        this.mTimelineEventSaver.storeEvent(event);
    }

    /* access modifiers changed from: private */
    public void manageBackEvents(int i, ApiCallback<Integer> apiCallback) {
        if (!this.mDataHandler.isAlive()) {
            Log.d(LOG_TAG, "manageEvents : mDataHandler is not anymore active.");
            return;
        }
        int min = Math.min(this.mSnapshotEvents.size(), i);
        Event event = null;
        for (int i2 = 0; i2 < min; i2++) {
            SnapshotEvent snapshotEvent = (SnapshotEvent) this.mSnapshotEvents.get(0);
            if (event == null && RoomSummary.isSupportedEvent(snapshotEvent.mEvent)) {
                event = snapshotEvent.mEvent;
            }
            this.mSnapshotEvents.remove(0);
            this.mEventListeners.onEvent(snapshotEvent.mEvent, Direction.BACKWARDS, snapshotEvent.mState);
        }
        if (event != null) {
            RoomSummary summary = this.mStore.getSummary(this.mRoomId);
            if (summary == null) {
                this.mStore.storeSummary(new RoomSummary(null, event, getState(), this.mDataHandler.getUserId()));
            } else if (!RoomSummary.isSupportedEvent(summary.getLatestReceivedEvent())) {
                summary.setLatestReceivedEvent(event, getState());
            }
        }
        Log.d(LOG_TAG, "manageEvents : commit");
        this.mStore.commit();
        if (this.mSnapshotEvents.size() < 30 && this.mIsLastBackChunk) {
            this.mCanBackPaginate = false;
        }
        this.mIsBackPaginating = false;
        if (apiCallback != null) {
            try {
                apiCallback.onSuccess(Integer.valueOf(min));
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("requestHistory exception ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void addPaginationEvents(List<Event> list, List<Event> list2, Direction direction) {
        boolean z;
        RoomSummary summary = this.mStore.getSummary(this.mRoomId);
        if (list2 != null) {
            for (Event event : list2) {
                if (direction == Direction.BACKWARDS) {
                    if ("m.room.member".equals(event.type) && this.mStateHolder.getState().getMember(event.stateKey) == null) {
                        processStateEvent(event, Direction.FORWARDS, true);
                    }
                }
                processStateEvent(event, direction, true);
            }
        }
        boolean z2 = false;
        for (Event event2 : list) {
            if (event2.stateKey != null) {
                boolean z3 = direction == Direction.FORWARDS;
                deepCopyState(direction);
                z = processStateEvent(event2, direction, z3);
            } else {
                z = true;
            }
            this.mDataHandler.decryptEvent(event2, getTimelineId());
            if (z && direction == Direction.BACKWARDS) {
                if (this.mIsLiveTimeline && summary != null && (summary.getLatestReceivedEvent() == null || (event2.isValidOriginServerTs() && summary.getLatestReceivedEvent().originServerTs < event2.originServerTs && RoomSummary.isSupportedEvent(event2)))) {
                    summary.setLatestReceivedEvent(event2, getState());
                    this.mStore.storeSummary(summary);
                    z2 = true;
                }
                this.mSnapshotEvents.add(new SnapshotEvent(event2, getBackState()));
            }
        }
        if (z2) {
            this.mStore.commit();
        }
    }

    /* access modifiers changed from: private */
    public void addPaginationEvents(List<Event> list, List<Event> list2, Direction direction, final ApiCallback<Integer> apiCallback) {
        final List<Event> list3 = list;
        final List<Event> list4 = list2;
        final Direction direction2 = direction;
        final ApiCallback<Integer> apiCallback2 = apiCallback;
        AnonymousClass1 r0 = new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                MXEventTimeline.this.addPaginationEvents(list3, list4, direction2);
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                if (direction2 == Direction.BACKWARDS) {
                    MXEventTimeline.this.manageBackEvents(30, apiCallback2);
                    return;
                }
                for (Event onEvent : list3) {
                    MXEventTimeline.this.mEventListeners.onEvent(onEvent, Direction.FORWARDS, MXEventTimeline.this.getState());
                }
                ApiCallback apiCallback = apiCallback2;
                if (apiCallback != null) {
                    apiCallback.onSuccess(Integer.valueOf(list3.size()));
                }
            }
        };
        try {
            r0.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## addPaginationEvents() failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            r0.cancel(true);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onUnexpectedError(e);
                    }
                }
            });
        }
    }

    public boolean canBackPaginate() {
        return !this.mIsBackPaginating && getState().canBackPaginate(this.mRoom.isJoined(), this.mRoom.isInvited()) && this.mCanBackPaginate && this.mRoom.isReady();
    }

    public boolean backPaginate(ApiCallback<Integer> apiCallback) {
        return backPaginate(30, apiCallback);
    }

    public boolean backPaginate(int i, ApiCallback<Integer> apiCallback) {
        return backPaginate(i, false, apiCallback);
    }

    public boolean backPaginate(final int i, boolean z, final ApiCallback<Integer> apiCallback) {
        boolean z2 = false;
        if (!canBackPaginate()) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("cannot requestHistory ");
            sb.append(this.mIsBackPaginating);
            String str2 = " ";
            sb.append(str2);
            sb.append(!getState().canBackPaginate(this.mRoom.isJoined(), this.mRoom.isInvited()));
            sb.append(str2);
            sb.append(!this.mCanBackPaginate);
            sb.append(str2);
            sb.append(!this.mRoom.isReady());
            Log.d(str, sb.toString());
            return false;
        }
        Log.d(LOG_TAG, "backPaginate starts");
        if (getBackState().getToken() == null) {
            this.mSnapshotEvents.clear();
        }
        final String token = getBackState().getToken();
        this.mIsBackPaginating = true;
        String str3 = Event.PAGINATE_BACK_TOKEN_END;
        if (z || this.mSnapshotEvents.size() >= i || TextUtils.equals(token, this.mBackwardTopToken) || TextUtils.equals(token, str3)) {
            if (TextUtils.equals(token, this.mBackwardTopToken) || TextUtils.equals(token, str3)) {
                z2 = true;
            }
            this.mIsLastBackChunk = z2;
            final Handler handler = new Handler(Looper.getMainLooper());
            if (z) {
                String str4 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("backPaginate : load ");
                sb2.append(this.mSnapshotEvents.size());
                sb2.append("cached events list");
                Log.d(str4, sb2.toString());
                i = Math.min(this.mSnapshotEvents.size(), i);
            } else if (this.mSnapshotEvents.size() >= i) {
                Log.d(LOG_TAG, "backPaginate : the events are already loaded.");
            } else {
                Log.d(LOG_TAG, "backPaginate : reach the history top");
            }
            new Thread(new Runnable() {
                public void run() {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            MXEventTimeline.this.manageBackEvents(i, apiCallback);
                        }
                    }, 0);
                }
            }).start();
            return true;
        }
        this.mDataHandler.getDataRetriever().backPaginate(this.mStore, this.mRoomId, getBackState().getToken(), i, this.mDataHandler.getPaginationFilter(), new SimpleApiCallback<TokensChunkEvents>(apiCallback) {
            public void onSuccess(TokensChunkEvents tokensChunkEvents) {
                if (MXEventTimeline.this.mDataHandler.isAlive()) {
                    if (tokensChunkEvents.chunk != null) {
                        String access$400 = MXEventTimeline.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("backPaginate : ");
                        sb.append(tokensChunkEvents.chunk.size());
                        sb.append(" events are retrieved.");
                        Log.d(access$400, sb.toString());
                    } else {
                        Log.d(MXEventTimeline.LOG_TAG, "backPaginate : there is no event");
                    }
                    MXEventTimeline.this.mIsLastBackChunk = (tokensChunkEvents.chunk != null && tokensChunkEvents.chunk.size() == 0 && TextUtils.equals(tokensChunkEvents.end, tokensChunkEvents.start)) || tokensChunkEvents.end == null;
                    if (MXEventTimeline.this.mIsLastBackChunk && tokensChunkEvents.end != null) {
                        MXEventTimeline.this.mBackwardTopToken = token;
                    } else if (tokensChunkEvents.end == null) {
                        MXEventTimeline.this.getBackState().setToken(Event.PAGINATE_BACK_TOKEN_END);
                    } else {
                        MXEventTimeline.this.getBackState().setToken(tokensChunkEvents.end);
                    }
                    MXEventTimeline.this.addPaginationEvents(tokensChunkEvents.chunk == null ? new ArrayList() : tokensChunkEvents.chunk, tokensChunkEvents.stateEvents, Direction.BACKWARDS, apiCallback);
                    return;
                }
                Log.d(MXEventTimeline.LOG_TAG, "mDataHandler is not active.");
            }

            public void onMatrixError(MatrixError matrixError) {
                Log.d(MXEventTimeline.LOG_TAG, "backPaginate onMatrixError");
                if (MatrixError.UNKNOWN.equals(matrixError.errcode)) {
                    MXEventTimeline.this.mCanBackPaginate = false;
                }
                MXEventTimeline.this.mIsBackPaginating = false;
                super.onMatrixError(matrixError);
            }

            public void onNetworkError(Exception exc) {
                Log.d(MXEventTimeline.LOG_TAG, "backPaginate onNetworkError");
                MXEventTimeline.this.mIsBackPaginating = false;
                super.onNetworkError(exc);
            }

            public void onUnexpectedError(Exception exc) {
                Log.d(MXEventTimeline.LOG_TAG, "backPaginate onUnexpectedError");
                MXEventTimeline.this.mIsBackPaginating = false;
                super.onUnexpectedError(exc);
            }
        });
        return true;
    }

    public boolean forwardPaginate(final ApiCallback<Integer> apiCallback) {
        if (this.mIsLiveTimeline) {
            Log.d(LOG_TAG, "Cannot forward paginate on Live timeline");
            return false;
        } else if (this.mIsForwardPaginating || this.mHasReachedHomeServerForwardsPaginationEnd) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("forwardPaginate ");
            sb.append(this.mIsForwardPaginating);
            sb.append(" mHasReachedHomeServerForwardsPaginationEnd ");
            sb.append(this.mHasReachedHomeServerForwardsPaginationEnd);
            Log.d(str, sb.toString());
            return false;
        } else {
            this.mIsForwardPaginating = true;
            this.mDataHandler.getDataRetriever().paginate(this.mStore, this.mRoomId, this.mForwardsPaginationToken, Direction.FORWARDS, this.mDataHandler.getPaginationFilter(), new SimpleApiCallback<TokensChunkEvents>(apiCallback) {
                public void onSuccess(TokensChunkEvents tokensChunkEvents) {
                    if (MXEventTimeline.this.mDataHandler.isAlive()) {
                        String access$400 = MXEventTimeline.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("forwardPaginate : ");
                        sb.append(tokensChunkEvents.chunk.size());
                        sb.append(" are retrieved.");
                        Log.d(access$400, sb.toString());
                        MXEventTimeline.this.mHasReachedHomeServerForwardsPaginationEnd = tokensChunkEvents.chunk.size() == 0 && TextUtils.equals(tokensChunkEvents.end, tokensChunkEvents.start);
                        MXEventTimeline.this.mForwardsPaginationToken = tokensChunkEvents.end;
                        MXEventTimeline.this.addPaginationEvents(tokensChunkEvents.chunk, tokensChunkEvents.stateEvents, Direction.FORWARDS, apiCallback);
                        MXEventTimeline.this.mIsForwardPaginating = false;
                        return;
                    }
                    Log.d(MXEventTimeline.LOG_TAG, "mDataHandler is not active.");
                }

                public void onMatrixError(MatrixError matrixError) {
                    MXEventTimeline.this.mIsForwardPaginating = false;
                    super.onMatrixError(matrixError);
                }

                public void onNetworkError(Exception exc) {
                    MXEventTimeline.this.mIsForwardPaginating = false;
                    super.onNetworkError(exc);
                }

                public void onUnexpectedError(Exception exc) {
                    MXEventTimeline.this.mIsForwardPaginating = false;
                    super.onUnexpectedError(exc);
                }
            });
            return true;
        }
    }

    public boolean paginate(Direction direction, ApiCallback<Integer> apiCallback) {
        if (Direction.BACKWARDS == direction) {
            return backPaginate(apiCallback);
        }
        return forwardPaginate(apiCallback);
    }

    public void cancelPaginationRequests() {
        this.mDataHandler.getDataRetriever().cancelHistoryRequests(this.mRoomId);
        this.mIsBackPaginating = false;
        this.mIsForwardPaginating = false;
    }

    public void resetPaginationAroundInitialEvent(int i, final ApiCallback<Void> apiCallback) {
        this.mStore.deleteRoomData(this.mRoomId);
        this.mDataHandler.resetReplayAttackCheckInTimeline(getTimelineId());
        this.mForwardsPaginationToken = null;
        this.mHasReachedHomeServerForwardsPaginationEnd = false;
        this.mDataHandler.getDataRetriever().getRoomsRestClient().getContextOfEvent(this.mRoomId, this.mInitialEventId, i, FilterUtil.createRoomEventFilter(this.mDataHandler.isLazyLoadingEnabled()), new SimpleApiCallback<EventContext>(apiCallback) {
            public void onSuccess(final EventContext eventContext) {
                AnonymousClass1 r0 = new AsyncTask<Void, Void, Void>() {
                    /* access modifiers changed from: protected */
                    public Void doInBackground(Void... voidArr) {
                        for (Event access$1300 : eventContext.state) {
                            MXEventTimeline.this.processStateEvent(access$1300, Direction.FORWARDS, true);
                        }
                        MXEventTimeline.this.initHistory();
                        ArrayList arrayList = new ArrayList();
                        Collections.reverse(eventContext.eventsAfter);
                        arrayList.addAll(eventContext.eventsAfter);
                        arrayList.add(eventContext.event);
                        arrayList.addAll(eventContext.eventsBefore);
                        MXEventTimeline.this.addPaginationEvents(arrayList, null, Direction.BACKWARDS);
                        return null;
                    }

                    /* access modifiers changed from: protected */
                    public void onPostExecute(Void voidR) {
                        ArrayList<SnapshotEvent> arrayList = new ArrayList<>(MXEventTimeline.this.mSnapshotEvents.subList(0, (MXEventTimeline.this.mSnapshotEvents.size() + 1) / 2));
                        Collections.reverse(arrayList);
                        for (SnapshotEvent snapshotEvent : arrayList) {
                            MXEventTimeline.this.mSnapshotEvents.remove(snapshotEvent);
                            MXEventTimeline.this.mEventListeners.onEvent(snapshotEvent.mEvent, Direction.FORWARDS, snapshotEvent.mState);
                        }
                        MXEventTimeline.this.getBackState().setToken(eventContext.start);
                        MXEventTimeline.this.mForwardsPaginationToken = eventContext.end;
                        MXEventTimeline.this.manageBackEvents(30, new ApiCallback<Integer>() {
                            public void onSuccess(Integer num) {
                                Log.d(MXEventTimeline.LOG_TAG, "addPaginationEvents succeeds");
                            }

                            public void onNetworkError(Exception exc) {
                                String access$400 = MXEventTimeline.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("addPaginationEvents failed ");
                                sb.append(exc.getMessage());
                                Log.e(access$400, sb.toString(), exc);
                            }

                            public void onMatrixError(MatrixError matrixError) {
                                String access$400 = MXEventTimeline.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("addPaginationEvents failed ");
                                sb.append(matrixError.getMessage());
                                Log.e(access$400, sb.toString());
                            }

                            public void onUnexpectedError(Exception exc) {
                                String access$400 = MXEventTimeline.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("addPaginationEvents failed ");
                                sb.append(exc.getMessage());
                                Log.e(access$400, sb.toString(), exc);
                            }
                        });
                        apiCallback.onSuccess(null);
                    }
                };
                try {
                    r0.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                } catch (Exception e) {
                    String access$400 = MXEventTimeline.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## resetPaginationAroundInitialEvent() failed ");
                    sb.append(e.getMessage());
                    Log.e(access$400, sb.toString(), e);
                    r0.cancel(true);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            if (apiCallback != null) {
                                apiCallback.onUnexpectedError(e);
                            }
                        }
                    });
                }
            }
        });
    }

    public void addEventTimelineListener(Listener listener) {
        this.mEventListeners.add(listener);
    }

    public void removeEventTimelineListener(Listener listener) {
        this.mEventListeners.remove(listener);
    }
}
