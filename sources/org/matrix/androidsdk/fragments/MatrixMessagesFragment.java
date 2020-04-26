package org.matrix.androidsdk.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.List;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomPreviewData;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.timeline.EventTimeline;
import org.matrix.androidsdk.data.timeline.EventTimeline.Direction;
import org.matrix.androidsdk.data.timeline.EventTimeline.Listener;
import org.matrix.androidsdk.listeners.IMXEventListener;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.sync.RoomResponse;
import org.matrix.androidsdk.rest.model.sync.RoomSync;
import org.matrix.androidsdk.rest.model.sync.RoomSyncState;
import org.matrix.androidsdk.rest.model.sync.RoomSyncTimeline;

public class MatrixMessagesFragment extends Fragment {
    private static final String ARG_ROOM_ID = "org.matrix.androidsdk.fragments.MatrixMessageFragment.ARG_ROOM_ID";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = MatrixMessagesFragment.class.getSimpleName();
    /* access modifiers changed from: private */
    public Context mContext;
    private final IMXEventListener mEventListener = new MXEventListener() {
        public void onLiveEventsChunkProcessed(String str, String str2) {
            if (MatrixMessagesFragment.this.mMatrixMessagesListener != null) {
                MatrixMessagesFragment.this.mMatrixMessagesListener.onLiveEventsChunkProcessed();
            }
        }

        public void onReceiptEvent(String str, List<String> list) {
            if (MatrixMessagesFragment.this.mMatrixMessagesListener != null) {
                MatrixMessagesFragment.this.mMatrixMessagesListener.onReceiptEvent(list);
            }
        }

        public void onRoomFlush(String str) {
            if (MatrixMessagesFragment.this.mMatrixMessagesListener != null && MatrixMessagesFragment.this.mEventTimeline.isLiveTimeline()) {
                MatrixMessagesFragment.this.mMatrixMessagesListener.onRoomFlush();
                MatrixMessagesFragment.this.mEventTimeline.initHistory();
                MatrixMessagesFragment.this.requestInitialHistory();
            }
        }

        public void onEventSent(Event event, String str) {
            if (MatrixMessagesFragment.this.mMatrixMessagesListener != null) {
                MatrixMessagesFragment.this.mMatrixMessagesListener.onEventSent(event, str);
            }
        }
    };
    /* access modifiers changed from: private */
    public EventTimeline mEventTimeline;
    private final Listener mEventTimelineListener = new Listener() {
        public void onEvent(Event event, Direction direction, RoomState roomState) {
            if (MatrixMessagesFragment.this.mMatrixMessagesListener != null) {
                MatrixMessagesFragment.this.mMatrixMessagesListener.onEvent(event, direction, roomState);
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mHasPendingInitialHistory;
    public boolean mKeepRoomHistory;
    /* access modifiers changed from: private */
    public MatrixMessagesListener mMatrixMessagesListener;
    /* access modifiers changed from: private */
    public Room mRoom;
    private MXSession mSession;

    public interface MatrixMessagesListener {
        EventTimeline getEventTimeLine();

        RoomPreviewData getRoomPreviewData();

        void hideInitLoading();

        void onEvent(Event event, Direction direction, RoomState roomState);

        void onEventSent(Event event, String str);

        void onInitialMessagesLoaded();

        void onLiveEventsChunkProcessed();

        void onReceiptEvent(List<String> list);

        void onRoomFlush();

        void onTimelineInitialized();

        void showInitLoading();
    }

    public static MatrixMessagesFragment newInstance(String str) {
        MatrixMessagesFragment matrixMessagesFragment = new MatrixMessagesFragment();
        matrixMessagesFragment.setArguments(getArgument(str));
        return matrixMessagesFragment;
    }

    public static Bundle getArgument(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ROOM_ID, str);
        return bundle;
    }

    public void onCreate(Bundle bundle) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Log.d(LOG_TAG, "onCreateView");
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        this.mContext = getActivity().getApplicationContext();
        String string = getArguments().getString(ARG_ROOM_ID);
        if (this.mSession != null) {
            EventTimeline eventTimeline = this.mEventTimeline;
            if (eventTimeline == null) {
                this.mEventTimeline = this.mMatrixMessagesListener.getEventTimeLine();
                EventTimeline eventTimeline2 = this.mEventTimeline;
                if (eventTimeline2 != null) {
                    eventTimeline2.addEventTimelineListener(this.mEventTimelineListener);
                    this.mRoom = this.mEventTimeline.getRoom();
                }
                if (this.mRoom == null) {
                    this.mRoom = this.mSession.getDataHandler().getRoom(string);
                }
                EventTimeline eventTimeline3 = this.mEventTimeline;
                if (eventTimeline3 == null || eventTimeline3.isLiveTimeline() || this.mEventTimeline.getInitialEventId() == null) {
                    boolean z = false;
                    if (this.mRoom != null) {
                        EventTimeline eventTimeline4 = this.mEventTimeline;
                        if (eventTimeline4 != null) {
                            eventTimeline4.initHistory();
                            if (this.mRoom.getState().getRoomCreateContent() != null) {
                                z = this.mRoom.isJoined();
                            }
                            this.mRoom.addEventListener(this.mEventListener);
                            if (!this.mEventTimeline.isLiveTimeline()) {
                                previewRoom();
                            } else if (!z) {
                                String str = LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("Joining room >> ");
                                sb.append(string);
                                Log.d(str, sb.toString());
                                joinRoom();
                            } else {
                                this.mHasPendingInitialHistory = true;
                            }
                        }
                    }
                    sendInitialMessagesLoaded();
                } else {
                    initializeTimeline();
                }
                return onCreateView;
            }
            eventTimeline.addEventTimelineListener(this.mEventTimelineListener);
            sendInitialMessagesLoaded();
            return onCreateView;
        }
        throw new RuntimeException("Must have valid default MXSession.");
    }

    public void onDestroy() {
        super.onDestroy();
        Room room = this.mRoom;
        if (room != null && this.mEventTimeline != null) {
            room.removeEventListener(this.mEventListener);
            this.mEventTimeline.removeEventTimelineListener(this.mEventTimelineListener);
        }
    }

    public void onResume() {
        super.onResume();
        if (this.mHasPendingInitialHistory) {
            requestInitialHistory();
        }
    }

    /* access modifiers changed from: private */
    public void sendInitialMessagesLoaded() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public void run() {
                if (MatrixMessagesFragment.this.mMatrixMessagesListener != null) {
                    MatrixMessagesFragment.this.mMatrixMessagesListener.onInitialMessagesLoaded();
                }
            }
        }, 100);
    }

    private void previewRoom() {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Make a room preview of ");
        sb.append(this.mRoom.getRoomId());
        Log.d(str, sb.toString());
        MatrixMessagesListener matrixMessagesListener = this.mMatrixMessagesListener;
        if (matrixMessagesListener != null) {
            RoomPreviewData roomPreviewData = matrixMessagesListener.getRoomPreviewData();
            if (roomPreviewData != null) {
                if (roomPreviewData.getRoomResponse() != null) {
                    Log.d(LOG_TAG, "A preview data is provided with sync response");
                    RoomResponse roomResponse = roomPreviewData.getRoomResponse();
                    RoomSync roomSync = new RoomSync();
                    roomSync.state = new RoomSyncState();
                    roomSync.state.events = roomResponse.state;
                    roomSync.timeline = new RoomSyncTimeline();
                    roomSync.timeline.events = roomResponse.messages.chunk;
                    roomSync.timeline.limited = true;
                    roomSync.timeline.prevBatch = roomResponse.messages.end;
                    this.mEventTimeline.handleJoinedRoomSync(roomSync, true);
                    Log.d(LOG_TAG, "The room preview is done -> fill the room history");
                    this.mHasPendingInitialHistory = true;
                } else {
                    Log.d(LOG_TAG, "A preview data is provided with no sync response : assume that it is not possible to get a room preview");
                    if (getActivity() != null) {
                        MatrixMessagesListener matrixMessagesListener2 = this.mMatrixMessagesListener;
                        if (matrixMessagesListener2 != null) {
                            matrixMessagesListener2.hideInitLoading();
                        }
                    }
                }
                return;
            }
        }
        this.mSession.getRoomsApiClient().initialSync(this.mRoom.getRoomId(), new ApiCallback<RoomResponse>() {
            public void onSuccess(RoomResponse roomResponse) {
                RoomSync roomSync = new RoomSync();
                roomSync.state = new RoomSyncState();
                roomSync.state.events = roomResponse.state;
                roomSync.timeline = new RoomSyncTimeline();
                roomSync.timeline.events = roomResponse.messages.chunk;
                MatrixMessagesFragment.this.mEventTimeline.handleJoinedRoomSync(roomSync, true);
                Log.d(MatrixMessagesFragment.LOG_TAG, "The room preview is done -> fill the room history");
                MatrixMessagesFragment.this.requestInitialHistory();
            }

            private void onError(String str) {
                String access$200 = MatrixMessagesFragment.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("The room preview of ");
                sb.append(MatrixMessagesFragment.this.mRoom.getRoomId());
                sb.append("failed ");
                sb.append(str);
                Log.e(access$200, sb.toString());
                if (MatrixMessagesFragment.this.getActivity() != null) {
                    MatrixMessagesFragment.this.getActivity().finish();
                }
            }

            public void onNetworkError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                onError(matrixError.getLocalizedMessage());
            }

            public void onUnexpectedError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }
        });
    }

    /* access modifiers changed from: protected */
    public void displayInitializeTimelineError(Object obj) {
        String str = obj instanceof MatrixError ? ((MatrixError) obj).getLocalizedMessage() : obj instanceof Exception ? ((Exception) obj).getLocalizedMessage() : "";
        if (!TextUtils.isEmpty(str)) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("displayInitializeTimelineError : ");
            sb.append(str);
            Log.d(str2, sb.toString());
            Toast.makeText(this.mContext, str, 0).show();
        }
    }

    private void initializeTimeline() {
        Log.d(LOG_TAG, "initializeTimeline");
        MatrixMessagesListener matrixMessagesListener = this.mMatrixMessagesListener;
        if (matrixMessagesListener != null) {
            matrixMessagesListener.showInitLoading();
        }
        this.mEventTimeline.resetPaginationAroundInitialEvent(60, new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                Log.d(MatrixMessagesFragment.LOG_TAG, "initializeTimeline is done");
                if (MatrixMessagesFragment.this.getActivity() != null && !MatrixMessagesFragment.this.getActivity().isFinishing()) {
                    if (MatrixMessagesFragment.this.mMatrixMessagesListener != null) {
                        MatrixMessagesFragment.this.mMatrixMessagesListener.hideInitLoading();
                        MatrixMessagesFragment.this.mMatrixMessagesListener.onTimelineInitialized();
                    }
                    MatrixMessagesFragment.this.sendInitialMessagesLoaded();
                }
            }

            private void onError() {
                Log.d(MatrixMessagesFragment.LOG_TAG, "initializeTimeline fails");
                if (MatrixMessagesFragment.this.getActivity() != null && !MatrixMessagesFragment.this.getActivity().isFinishing() && MatrixMessagesFragment.this.mMatrixMessagesListener != null) {
                    MatrixMessagesFragment.this.mMatrixMessagesListener.hideInitLoading();
                    MatrixMessagesFragment.this.mMatrixMessagesListener.onTimelineInitialized();
                }
            }

            public void onNetworkError(Exception exc) {
                MatrixMessagesFragment.this.displayInitializeTimelineError(exc);
                onError();
            }

            public void onMatrixError(MatrixError matrixError) {
                MatrixMessagesFragment.this.displayInitializeTimelineError(matrixError);
                onError();
            }

            public void onUnexpectedError(Exception exc) {
                MatrixMessagesFragment.this.displayInitializeTimelineError(exc);
                onError();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void requestInitialHistory() {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("requestInitialHistory ");
        sb.append(this.mRoom.getRoomId());
        Log.d(str, sb.toString());
        if (backPaginate(new SimpleApiCallback<Integer>(getActivity()) {
            public void onSuccess(Integer num) {
                Log.d(MatrixMessagesFragment.LOG_TAG, "requestInitialHistory onSuccess");
                MatrixMessagesFragment.this.mHasPendingInitialHistory = false;
                if (MatrixMessagesFragment.this.getActivity() != null && MatrixMessagesFragment.this.mMatrixMessagesListener != null) {
                    MatrixMessagesFragment.this.mMatrixMessagesListener.hideInitLoading();
                    MatrixMessagesFragment.this.mMatrixMessagesListener.onTimelineInitialized();
                    MatrixMessagesFragment.this.mMatrixMessagesListener.onInitialMessagesLoaded();
                }
            }

            private void onError(String str) {
                String access$200 = MatrixMessagesFragment.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("requestInitialHistory failed");
                sb.append(str);
                Log.e(access$200, sb.toString());
                MatrixMessagesFragment.this.mHasPendingInitialHistory = false;
                if (MatrixMessagesFragment.this.getActivity() != null) {
                    Toast.makeText(MatrixMessagesFragment.this.mContext, str, 1).show();
                    if (MatrixMessagesFragment.this.mMatrixMessagesListener != null) {
                        MatrixMessagesFragment.this.mMatrixMessagesListener.hideInitLoading();
                    }
                }
            }

            public void onNetworkError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                onError(matrixError.getLocalizedMessage());
            }

            public void onUnexpectedError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }
        })) {
            MatrixMessagesListener matrixMessagesListener = this.mMatrixMessagesListener;
            if (matrixMessagesListener != null) {
                matrixMessagesListener.showInitLoading();
            }
        }
    }

    public void setMatrixMessagesListener(MatrixMessagesListener matrixMessagesListener) {
        this.mMatrixMessagesListener = matrixMessagesListener;
    }

    public void setMXSession(MXSession mXSession) {
        this.mSession = mXSession;
    }

    public boolean canBackPaginate() {
        EventTimeline eventTimeline = this.mEventTimeline;
        if (eventTimeline != null) {
            return eventTimeline.canBackPaginate();
        }
        return false;
    }

    public boolean backPaginate(ApiCallback<Integer> apiCallback) {
        EventTimeline eventTimeline = this.mEventTimeline;
        if (eventTimeline != null) {
            return eventTimeline.backPaginate(apiCallback);
        }
        return false;
    }

    public boolean forwardPaginate(ApiCallback<Integer> apiCallback) {
        EventTimeline eventTimeline = this.mEventTimeline;
        if (eventTimeline == null || !eventTimeline.isLiveTimeline()) {
            return false;
        }
        return this.mEventTimeline.forwardPaginate(apiCallback);
    }

    public void redact(String str, ApiCallback<Event> apiCallback) {
        Room room = this.mRoom;
        if (room != null) {
            room.redact(str, apiCallback);
        }
    }

    private void joinRoom() {
        MatrixMessagesListener matrixMessagesListener = this.mMatrixMessagesListener;
        if (matrixMessagesListener != null) {
            matrixMessagesListener.showInitLoading();
        }
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("joinRoom ");
        sb.append(this.mRoom.getRoomId());
        Log.d(str, sb.toString());
        Room room = this.mRoom;
        room.join(room.getRoomId(), null, null, new SimpleApiCallback<Void>(getActivity()) {
            public void onSuccess(Void voidR) {
                Log.d(MatrixMessagesFragment.LOG_TAG, "joinRoom succeeds");
                MatrixMessagesFragment.this.requestInitialHistory();
            }

            private void onError(String str) {
                if (MatrixMessagesFragment.this.getActivity() != null) {
                    Toast.makeText(MatrixMessagesFragment.this.mContext, str, 0).show();
                    MatrixMessagesFragment.this.getActivity().finish();
                }
            }

            public void onNetworkError(Exception exc) {
                String access$200 = MatrixMessagesFragment.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("joinRoom Network error: ");
                sb.append(exc.getMessage());
                Log.e(access$200, sb.toString(), exc);
                onError(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$200 = MatrixMessagesFragment.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("joinRoom onMatrixError : ");
                sb.append(matrixError.getMessage());
                Log.e(access$200, sb.toString());
                onError(matrixError.getLocalizedMessage());
            }

            public void onUnexpectedError(Exception exc) {
                String access$200 = MatrixMessagesFragment.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("joinRoom Override : ");
                sb.append(exc.getMessage());
                Log.e(access$200, sb.toString(), exc);
                onError(exc.getLocalizedMessage());
            }
        });
    }
}
