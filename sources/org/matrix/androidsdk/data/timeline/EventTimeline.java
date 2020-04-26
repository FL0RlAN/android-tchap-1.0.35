package org.matrix.androidsdk.data.timeline;

import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.sync.InvitedRoomSync;
import org.matrix.androidsdk.rest.model.sync.RoomSync;

public interface EventTimeline {

    public enum Direction {
        FORWARDS,
        BACKWARDS
    }

    public interface Listener {
        void onEvent(Event event, Direction direction, RoomState roomState);
    }

    void addEventTimelineListener(Listener listener);

    boolean backPaginate(int i, ApiCallback<Integer> apiCallback);

    boolean backPaginate(int i, boolean z, ApiCallback<Integer> apiCallback);

    boolean backPaginate(ApiCallback<Integer> apiCallback);

    boolean canBackPaginate();

    void cancelPaginationRequests();

    boolean forwardPaginate(ApiCallback<Integer> apiCallback);

    String getInitialEventId();

    Room getRoom();

    RoomState getState();

    IMXStore getStore();

    String getTimelineId();

    void handleInvitedRoomSync(InvitedRoomSync invitedRoomSync);

    void handleJoinedRoomSync(RoomSync roomSync, boolean z);

    boolean hasReachedHomeServerForwardsPaginationEnd();

    void initHistory();

    boolean isHistorical();

    boolean isLiveTimeline();

    boolean paginate(Direction direction, ApiCallback<Integer> apiCallback);

    void removeEventTimelineListener(Listener listener);

    void resetPaginationAroundInitialEvent(int i, ApiCallback<Void> apiCallback);

    void setIsHistorical(boolean z);

    void setState(RoomState roomState);

    void storeOutgoingEvent(Event event);
}
