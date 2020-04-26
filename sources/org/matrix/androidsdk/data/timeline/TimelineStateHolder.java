package org.matrix.androidsdk.data.timeline;

import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.data.timeline.EventTimeline.Direction;
import org.matrix.androidsdk.rest.model.Event;

class TimelineStateHolder {
    private RoomState mBackState;
    private final MXDataHandler mDataHandler;
    private String mRoomId;
    private RoomState mState;
    private final IMXStore mStore;

    TimelineStateHolder(MXDataHandler mXDataHandler, IMXStore iMXStore, String str) {
        this.mDataHandler = mXDataHandler;
        this.mStore = iMXStore;
        this.mRoomId = str;
        initStates();
    }

    public void clear() {
        initStates();
    }

    public RoomState getState() {
        return this.mState;
    }

    public void setState(RoomState roomState) {
        this.mState = roomState;
    }

    public RoomState getBackState() {
        return this.mBackState;
    }

    public void setBackState(RoomState roomState) {
        this.mBackState = roomState;
    }

    public void deepCopyState(Direction direction) {
        if (direction == Direction.FORWARDS) {
            this.mState = this.mState.deepCopy();
        } else {
            this.mBackState = this.mBackState.deepCopy();
        }
    }

    public boolean processStateEvent(Event event, Direction direction, boolean z) {
        IMXStore iMXStore;
        RoomState roomState;
        if (direction == Direction.FORWARDS) {
            roomState = this.mState;
            iMXStore = this.mStore;
        } else {
            roomState = this.mBackState;
            iMXStore = null;
        }
        boolean applyState = roomState.applyState(event, z, iMXStore);
        if (applyState && direction == Direction.FORWARDS) {
            this.mStore.storeLiveStateForRoom(this.mRoomId);
        }
        return applyState;
    }

    public void setRoomId(String str) {
        this.mRoomId = str;
        this.mState.roomId = str;
        this.mBackState.roomId = str;
    }

    private void initStates() {
        this.mBackState = new RoomState();
        this.mBackState.setDataHandler(this.mDataHandler);
        this.mBackState.roomId = this.mRoomId;
        this.mState = new RoomState();
        this.mState.setDataHandler(this.mDataHandler);
        this.mState.roomId = this.mRoomId;
    }
}
