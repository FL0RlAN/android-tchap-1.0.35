package org.matrix.androidsdk.fragments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.R;
import org.matrix.androidsdk.adapters.AbstractMessagesAdapter;
import org.matrix.androidsdk.adapters.MessageRow;
import org.matrix.androidsdk.core.EventDisplay;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.MXCryptoError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomMediaMessage;
import org.matrix.androidsdk.data.RoomMediaMessage.EventCreationListener;
import org.matrix.androidsdk.data.RoomPreviewData;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.data.timeline.EventTimeline;
import org.matrix.androidsdk.data.timeline.EventTimeline.Direction;
import org.matrix.androidsdk.data.timeline.EventTimelineFactory;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.fragments.MatrixMessagesFragment.MatrixMessagesListener;
import org.matrix.androidsdk.listeners.IMXEventListener;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.listeners.MXMediaUploadListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.Event.SentState;
import org.matrix.androidsdk.rest.model.ReceiptData;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.message.MediaMessage;
import org.matrix.androidsdk.rest.model.message.Message;
import org.matrix.androidsdk.rest.model.search.SearchResponse;
import org.matrix.androidsdk.rest.model.search.SearchResult;
import org.matrix.androidsdk.view.AutoScrollDownListView;
import org.matrix.olm.OlmException;

public abstract class MatrixMessageListFragment<MessagesAdapter extends AbstractMessagesAdapter> extends Fragment implements MatrixMessagesListener {
    protected static final String ARG_EVENT_ID = "MatrixMessageListFragment.ARG_EVENT_ID";
    private static final String ARG_LAYOUT_ID = "MatrixMessageListFragment.ARG_LAYOUT_ID";
    private static final String ARG_MATRIX_ID = "MatrixMessageListFragment.ARG_MATRIX_ID";
    protected static final String ARG_PREVIEW_MODE_ID = "MatrixMessageListFragment.ARG_PREVIEW_MODE_ID";
    private static final String ARG_ROOM_ID = "MatrixMessageListFragment.ARG_ROOM_ID";
    private static final String LOG_TAG = "MatrixMsgsListFrag";
    public static final String PREVIEW_MODE_READ_ONLY = "PREVIEW_MODE_READ_ONLY";
    public static final String PREVIEW_MODE_UNREAD_MESSAGE = "PREVIEW_MODE_UNREAD_MESSAGE";
    private static final int UNDEFINED_VIEW_Y_POS = -12345678;
    protected IOnScrollListener mActivityOnScrollListener;
    /* access modifiers changed from: protected */
    public MessagesAdapter mAdapter;
    public boolean mCheckSlideToHide = false;
    private boolean mDisplayAllEvents = true;
    private final EventCreationListener mEventCreationListener = new EventCreationListener() {
        public void onEventCreated(RoomMediaMessage roomMediaMessage) {
            MatrixMessageListFragment.this.add(roomMediaMessage);
        }

        public void onEventCreationFailed(RoomMediaMessage roomMediaMessage, String str) {
            MatrixMessageListFragment.this.displayMessageSendingFailed(str);
        }

        public void onEncryptionFailed(RoomMediaMessage roomMediaMessage) {
            MatrixMessageListFragment.this.displayEncryptionAlert();
        }
    };
    protected String mEventId;
    protected long mEventOriginServerTs;
    /* access modifiers changed from: protected */
    public IEventSendingListener mEventSendingListener;
    protected EventTimeline mEventTimeLine;
    private final IMXEventListener mEventsListener = new MXEventListener() {
        /* access modifiers changed from: private */
        public boolean mRefreshAfterEventsDecryption;

        public void onEventSentStateUpdated(Event event) {
            MatrixMessageListFragment.this.getUiHandler().post(new Runnable() {
                public void run() {
                    MatrixMessageListFragment.this.mAdapter.notifyDataSetChanged();
                }
            });
        }

        public void onEventDecrypted(String str, final String str2) {
            MatrixMessageListFragment.this.getUiHandler().post(new Runnable() {
                public void run() {
                    boolean access$100 = AnonymousClass1.this.mRefreshAfterEventsDecryption;
                    String str = "## onEventDecrypted ";
                    String str2 = MatrixMessageListFragment.LOG_TAG;
                    if (access$100) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(str2);
                        sb.append(" : there is a pending refresh");
                        Log.d(str2, sb.toString());
                        return;
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(str2);
                    Log.d(str2, sb2.toString());
                    AnonymousClass1.this.mRefreshAfterEventsDecryption = true;
                    MatrixMessageListFragment.this.getUiHandler().postDelayed(new Runnable() {
                        public void run() {
                            Log.d(MatrixMessageListFragment.LOG_TAG, "## onEventDecrypted : refresh the list");
                            AnonymousClass1.this.mRefreshAfterEventsDecryption = false;
                            MatrixMessageListFragment.this.mAdapter.notifyDataSetChanged();
                        }
                    }, 500);
                }
            });
        }
    };
    private boolean mFillHistoryOnResume;
    private int mFirstVisibleRow = -1;
    /* access modifiers changed from: private */
    public int mFirstVisibleRowY = UNDEFINED_VIEW_Y_POS;
    private String mFutureReadMarkerEventId;
    /* access modifiers changed from: protected */
    public boolean mIsBackPaginating = false;
    protected boolean mIsFwdPaginating = false;
    /* access modifiers changed from: protected */
    public boolean mIsInitialSyncing = true;
    protected final boolean mIsLive = true;
    protected boolean mIsMediaSearch;
    private boolean mIsScrollListenerSet;
    /* access modifiers changed from: private */
    public boolean mLockBackPagination = false;
    /* access modifiers changed from: private */
    public boolean mLockFwdPagination = true;
    protected String mMatrixId;
    /* access modifiers changed from: private */
    public MatrixMessagesFragment mMatrixMessagesFragment;
    public AutoScrollDownListView mMessageListView;
    /* access modifiers changed from: protected */
    public String mNextBatch = null;
    protected String mPattern = null;
    /* access modifiers changed from: private */
    public final Map<String, Timer> mPendingRelaunchTimersByEventId = new HashMap();
    /* access modifiers changed from: protected */
    public Room mRoom;
    protected String mRoomId;
    protected IRoomPreviewDataListener mRoomPreviewDataListener;
    /* access modifiers changed from: protected */
    public final OnScrollListener mScrollListener = new OnScrollListener() {
        public void onScrollStateChanged(AbsListView absListView, int i) {
            MatrixMessageListFragment.this.mCheckSlideToHide = i == 1;
            String str = MatrixMessageListFragment.LOG_TAG;
            if (i == 1) {
                int firstVisiblePosition = MatrixMessageListFragment.this.mMessageListView.getFirstVisiblePosition();
                if (MatrixMessageListFragment.this.mMessageListView.getLastVisiblePosition() + 10 >= MatrixMessageListFragment.this.mMessageListView.getCount()) {
                    Log.d(str, "onScrollStateChanged - forwardPaginate");
                    MatrixMessageListFragment.this.forwardPaginate();
                } else if (firstVisiblePosition < 10) {
                    Log.d(str, "onScrollStateChanged - request history");
                    MatrixMessageListFragment.this.backPaginate(false);
                }
            }
            if (MatrixMessageListFragment.this.mActivityOnScrollListener != null) {
                try {
                    MatrixMessageListFragment.this.mActivityOnScrollListener.onScrollStateChanged(i);
                } catch (Exception e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("## manageScrollListener : onScrollStateChanged failed ");
                    sb.append(e.getMessage());
                    Log.e(str, sb.toString(), e);
                }
            }
        }

        private void manageScrollListener(AbsListView absListView, int i, int i2, int i3) {
            String str = MatrixMessageListFragment.LOG_TAG;
            if (MatrixMessageListFragment.this.mActivityOnScrollListener != null) {
                try {
                    MatrixMessageListFragment.this.mActivityOnScrollListener.onScroll(i, i2, i3);
                } catch (Exception e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("## manageScrollListener : onScroll failed ");
                    sb.append(e.getMessage());
                    Log.e(str, sb.toString(), e);
                }
                boolean z = false;
                if (i + i2 >= i3) {
                    View childAt = absListView.getChildAt(i2 - 1);
                    if (childAt != null && childAt.getTop() + childAt.getHeight() <= absListView.getHeight()) {
                        z = true;
                    }
                }
                try {
                    MatrixMessageListFragment.this.mActivityOnScrollListener.onLatestEventDisplay(z);
                } catch (Exception e2) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## manageScrollListener : onLatestEventDisplay failed ");
                    sb2.append(e2.getMessage());
                    Log.e(str, sb2.toString(), e2);
                }
            }
        }

        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            MatrixMessageListFragment.this.mFirstVisibleRowY = MatrixMessageListFragment.UNDEFINED_VIEW_Y_POS;
            View childAt = MatrixMessageListFragment.this.mMessageListView.getChildAt(i2 == MatrixMessageListFragment.this.mMessageListView.getChildCount() ? 0 : i);
            if (childAt != null) {
                MatrixMessageListFragment.this.mFirstVisibleRowY = childAt.getTop();
            }
            String str = " totalItemCount ";
            String str2 = " visibleItemCount ";
            String str3 = MatrixMessageListFragment.LOG_TAG;
            if (i < 10 && i2 != i3 && i2 != 0) {
                if (!MatrixMessageListFragment.this.mLockBackPagination) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("onScroll - backPaginate firstVisibleItem ");
                    sb.append(i);
                    sb.append(str2);
                    sb.append(i2);
                    sb.append(str);
                    sb.append(i3);
                    Log.d(str3, sb.toString());
                }
                MatrixMessageListFragment.this.backPaginate(false);
            } else if (i + i2 + 10 >= i3) {
                if (!MatrixMessageListFragment.this.mLockFwdPagination) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onScroll - forwardPaginate firstVisibleItem ");
                    sb2.append(i);
                    sb2.append(str2);
                    sb2.append(i2);
                    sb2.append(str);
                    sb2.append(i3);
                    Log.d(str3, sb2.toString());
                }
                MatrixMessageListFragment.this.forwardPaginate();
            }
            manageScrollListener(absListView, i, i2, i3);
        }
    };
    /* access modifiers changed from: private */
    public int mScrollToIndex = -1;
    /* access modifiers changed from: protected */
    public MXSession mSession;
    /* access modifiers changed from: protected */
    public Handler mUiHandler;

    public interface IEventSendingListener {
        void onConsentNotGiven(Event event, MatrixError matrixError);

        void onMessageRedacted(Event event);

        void onMessageSendingFailed(Event event);

        void onMessageSendingSucceeded(Event event);

        void onUnknownDevices(Event event, MXCryptoError mXCryptoError);
    }

    public interface IOnScrollListener {
        void onLatestEventDisplay(boolean z);

        void onScroll(int i, int i2, int i3);

        void onScrollStateChanged(int i);
    }

    public interface IRoomPreviewDataListener {
        RoomPreviewData getRoomPreviewData();
    }

    public interface OnSearchResultListener {
        void onSearchFailed();

        void onSearchSucceed(int i);
    }

    public abstract MessagesAdapter createMessagesAdapter();

    public abstract MXMediaCache getMXMediaCache();

    /* access modifiers changed from: protected */
    public String getMatrixMessagesFragmentTag() {
        return "org.matrix.androidsdk.RoomActivity.TAG_FRAGMENT_MATRIX_MESSAGES";
    }

    public abstract MXSession getSession(String str);

    public void hideInitLoading() {
    }

    public void hideLoadingBackProgress() {
    }

    public void hideLoadingForwardProgress() {
    }

    public boolean isDisplayAllEvents() {
        return true;
    }

    public void onListTouch(MotionEvent motionEvent) {
    }

    public void onLiveEventsChunkProcessed() {
    }

    public void showInitLoading() {
    }

    public void showLoadingBackProgress() {
    }

    public void showLoadingForwardProgress() {
    }

    public static Bundle getArguments(String str, String str2, int i) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_MATRIX_ID, str);
        bundle.putString(ARG_ROOM_ID, str2);
        bundle.putInt(ARG_LAYOUT_ID, i);
        return bundle;
    }

    public MXSession getSession() {
        return this.mSession;
    }

    /* access modifiers changed from: private */
    public Handler getUiHandler() {
        if (this.mUiHandler == null) {
            this.mUiHandler = new Handler(Looper.getMainLooper());
        }
        return this.mUiHandler;
    }

    public void onCreate(Bundle bundle) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        String str = LOG_TAG;
        Log.d(str, "onCreateView");
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        Bundle arguments = getArguments();
        this.mUiHandler = new Handler(Looper.getMainLooper());
        this.mMatrixId = arguments.getString(ARG_MATRIX_ID);
        this.mSession = getSession(this.mMatrixId);
        if (this.mSession == null) {
            String str2 = "Must have valid default MXSession.";
            if (getActivity() != null) {
                Log.e(str, str2);
                getActivity().finish();
                return onCreateView;
            }
            throw new RuntimeException(str2);
        } else if (getMXMediaCache() == null) {
            String str3 = "Must have valid default MediaCache.";
            if (getActivity() != null) {
                Log.e(str, str3);
                getActivity().finish();
                return onCreateView;
            }
            throw new RuntimeException(str3);
        } else {
            this.mRoomId = arguments.getString(ARG_ROOM_ID);
            View inflate = layoutInflater.inflate(arguments.getInt(ARG_LAYOUT_ID), viewGroup, false);
            this.mMessageListView = (AutoScrollDownListView) inflate.findViewById(R.id.listView_messages);
            this.mIsScrollListenerSet = false;
            if (this.mAdapter == null) {
                this.mAdapter = createMessagesAdapter();
                if (this.mAdapter == null) {
                    throw new RuntimeException("Must have valid default MessagesAdapter.");
                }
            } else if (bundle != null) {
                this.mFirstVisibleRow = bundle.getInt("FIRST_VISIBLE_ROW", -1);
            }
            this.mAdapter.setIsPreviewMode(false);
            if (this.mEventTimeLine == null) {
                this.mEventId = arguments.getString(ARG_EVENT_ID);
                String string = arguments.getString(ARG_PREVIEW_MODE_ID);
                if (!TextUtils.isEmpty(this.mEventId)) {
                    this.mEventTimeLine = EventTimelineFactory.pastTimeline(this.mSession.getDataHandler(), this.mRoomId, this.mEventId);
                    this.mRoom = this.mEventTimeLine.getRoom();
                    if (PREVIEW_MODE_UNREAD_MESSAGE.equals(string)) {
                        this.mAdapter.setIsUnreadViewMode(true);
                    }
                } else if (PREVIEW_MODE_READ_ONLY.equals(string)) {
                    this.mAdapter.setIsPreviewMode(true);
                    this.mEventTimeLine = EventTimelineFactory.inMemoryTimeline(this.mSession.getDataHandler(), this.mRoomId);
                    this.mRoom = this.mEventTimeLine.getRoom();
                } else if (!TextUtils.isEmpty(this.mRoomId)) {
                    this.mRoom = this.mSession.getDataHandler().getRoom(this.mRoomId);
                    this.mEventTimeLine = this.mRoom.getTimeline();
                }
            }
            this.mMessageListView.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    MatrixMessageListFragment.this.onListTouch(motionEvent);
                    return false;
                }
            });
            this.mDisplayAllEvents = isDisplayAllEvents();
            Room room = this.mRoom;
            if (room != null) {
                room.getMembersAsync(new SimpleApiCallback<List<RoomMember>>() {
                    public void onSuccess(List<RoomMember> list) {
                        if (MatrixMessageListFragment.this.isAdded()) {
                            MatrixMessageListFragment.this.mAdapter.setLiveRoomMembers(list);
                        }
                    }
                });
            }
            return inflate;
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        AutoScrollDownListView autoScrollDownListView = this.mMessageListView;
        if (autoScrollDownListView != null) {
            int firstVisiblePosition = autoScrollDownListView.getFirstVisiblePosition();
            if (firstVisiblePosition > 0) {
                firstVisiblePosition++;
            }
            bundle.putInt("FIRST_VISIBLE_ROW", firstVisiblePosition);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        MatrixMessagesFragment matrixMessagesFragment = this.mMatrixMessagesFragment;
        if (matrixMessagesFragment != null) {
            matrixMessagesFragment.setMatrixMessagesListener(null);
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        FragmentManager childFragmentManager = getChildFragmentManager();
        this.mMatrixMessagesFragment = (MatrixMessagesFragment) childFragmentManager.findFragmentByTag(getMatrixMessagesFragmentTag());
        MatrixMessagesFragment matrixMessagesFragment = this.mMatrixMessagesFragment;
        String str = LOG_TAG;
        if (matrixMessagesFragment == null) {
            Log.d(str, "onActivityCreated create");
            this.mMatrixMessagesFragment = createMessagesFragmentInstance(arguments.getString(ARG_ROOM_ID));
            childFragmentManager.beginTransaction().add((Fragment) this.mMatrixMessagesFragment, getMatrixMessagesFragmentTag()).commit();
        } else {
            Log.d(str, "onActivityCreated - reuse");
        }
        this.mMatrixMessagesFragment.setMatrixMessagesListener(this);
        this.mMatrixMessagesFragment.setMXSession(getSession());
        this.mMatrixMessagesFragment.mKeepRoomHistory = -1 != this.mFirstVisibleRow;
    }

    public void onPause() {
        super.onPause();
        this.mEventSendingListener = null;
        this.mActivityOnScrollListener = null;
        this.mEventSendingListener = null;
        this.mActivityOnScrollListener = null;
        Room room = this.mRoom;
        if (room != null) {
            room.removeEventListener(this.mEventsListener);
        }
        cancelCatchingRequests();
    }

    public void onResume() {
        super.onResume();
        FragmentActivity activity = getActivity();
        if (activity instanceof IEventSendingListener) {
            this.mEventSendingListener = (IEventSendingListener) activity;
        }
        if (activity instanceof IOnScrollListener) {
            this.mActivityOnScrollListener = (IOnScrollListener) activity;
        }
        if (this.mRoom != null && this.mEventTimeLine.isLiveTimeline()) {
            Room room = this.mSession.getDataHandler().getRoom(this.mRoom.getRoomId(), false);
            if (room != null) {
                room.addEventListener(this.mEventsListener);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("the room ");
                sb.append(this.mRoom.getRoomId());
                sb.append(" does not exist anymore");
                Log.e(LOG_TAG, sb.toString());
            }
        }
        if (this.mFillHistoryOnResume) {
            this.mFillHistoryOnResume = false;
            backPaginate(true);
        }
    }

    public MatrixMessagesFragment createMessagesFragmentInstance(String str) {
        return MatrixMessagesFragment.newInstance(str);
    }

    public void scrollToIndexWhenLoaded(int i) {
        this.mScrollToIndex = i;
    }

    public int getMaxThumbnailWidth() {
        return this.mAdapter.getMaxThumbnailWidth();
    }

    public int getMaxThumbnailHeight() {
        return this.mAdapter.getMaxThumbnailHeight();
    }

    public void onBingRulesUpdate() {
        this.mAdapter.onBingRulesUpdate();
    }

    public void scrollToBottom(int i) {
        this.mMessageListView.postDelayed(new Runnable() {
            public void run() {
                MatrixMessageListFragment.this.mMessageListView.setSelection(MatrixMessageListFragment.this.mAdapter.getCount() - 1);
                MatrixMessageListFragment.this.mMessageListView.smoothScrollBy(0, 0);
            }
        }, (long) Math.max(i, 0));
    }

    public void scrollToBottom() {
        scrollToBottom(OlmException.EXCEPTION_CODE_CREATE_OUTBOUND_GROUP_SESSION);
    }

    public Event getEvent(int i) {
        if (this.mAdapter.getCount() > i) {
            return ((MessageRow) this.mAdapter.getItem(i)).getEvent();
        }
        return null;
    }

    private boolean canUpdateReadMarker(MessageRow messageRow, MessageRow messageRow2) {
        if (messageRow2 == null || this.mAdapter.getPosition(messageRow) != this.mAdapter.getPosition(messageRow2) + 1 || messageRow.getEvent().getOriginServerTs() <= messageRow2.getEvent().originServerTs) {
            return false;
        }
        return true;
    }

    private MessageRow getReadMarkerMessageRow(MessageRow messageRow) {
        String readMarkerEventId = this.mRoom.getReadMarkerEventId();
        MessageRow messageRow2 = this.mAdapter.getMessageRow(readMarkerEventId);
        if (messageRow2 != null) {
            return messageRow2;
        }
        try {
            Event event = this.mSession.getDataHandler().getStore().getEvent(readMarkerEventId, this.mRoom.getRoomId());
            if (event == null || canAddEvent(event)) {
                return messageRow2;
            }
            MessageRow closestRowFromTs = this.mAdapter.getClosestRowFromTs(event.eventId, event.getOriginServerTs());
            if (closestRowFromTs != null && !canUpdateReadMarker(messageRow, closestRowFromTs)) {
                closestRowFromTs = null;
            }
            if (closestRowFromTs == null) {
                return this.mAdapter.getClosestRowBeforeTs(event.eventId, event.getOriginServerTs());
            }
            return closestRowFromTs;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## getReadMarkerMessageRow() failed : ");
            sb.append(e.getMessage());
            Log.e(LOG_TAG, sb.toString(), e);
            return messageRow2;
        }
    }

    private MessageRow addMessageRow(RoomMediaMessage roomMediaMessage) {
        if (this.mRoom == null) {
            return null;
        }
        Event event = roomMediaMessage.getEvent();
        MessageRow messageRow = new MessageRow(event, this.mRoom.getState());
        this.mAdapter.add(messageRow);
        if (canUpdateReadMarker(messageRow, getReadMarkerMessageRow(messageRow))) {
            AutoScrollDownListView autoScrollDownListView = this.mMessageListView;
            View childAt = autoScrollDownListView.getChildAt(autoScrollDownListView.getChildCount() - 1);
            if (childAt != null && childAt.getTop() >= 0) {
                this.mFutureReadMarkerEventId = event.eventId;
                this.mAdapter.resetReadMarker();
            }
        }
        scrollToBottom();
        getSession().getDataHandler().getStore().commit();
        return messageRow;
    }

    /* access modifiers changed from: protected */
    public void redactEvent(String str) {
        this.mMatrixMessagesFragment.redact(str, new ApiCallback<Event>() {
            public void onSuccess(final Event event) {
                if (event != null) {
                    MatrixMessageListFragment.this.getUiHandler().post(new Runnable() {
                        public void run() {
                            Event event = new Event();
                            event.roomId = event.roomId;
                            event.redacts = event.eventId;
                            event.setType(Event.EVENT_TYPE_REDACTION);
                            MatrixMessageListFragment.this.onEvent(event, Direction.FORWARDS, MatrixMessageListFragment.this.mRoom.getState());
                            if (MatrixMessageListFragment.this.mEventSendingListener != null) {
                                try {
                                    MatrixMessageListFragment.this.mEventSendingListener.onMessageRedacted(event);
                                } catch (Exception e) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("redactEvent fails : ");
                                    sb.append(e.getMessage());
                                    Log.e(MatrixMessageListFragment.LOG_TAG, sb.toString(), e);
                                }
                            }
                        }
                    });
                }
            }

            private void onError() {
                if (MatrixMessageListFragment.this.getActivity() != null) {
                    Toast.makeText(MatrixMessageListFragment.this.getActivity(), R.string.could_not_redact, 0).show();
                }
            }

            public void onNetworkError(Exception exc) {
                onError();
            }

            public void onMatrixError(MatrixError matrixError) {
                onError();
            }

            public void onUnexpectedError(Exception exc) {
                onError();
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean canAddEvent(Event event) {
        String type = event.getType();
        return this.mDisplayAllEvents || Event.EVENT_TYPE_MESSAGE.equals(type) || "m.room.encrypted".equals(type) || "m.room.encryption".equals(type) || Event.EVENT_TYPE_STATE_ROOM_NAME.equals(type) || Event.EVENT_TYPE_STATE_ROOM_TOPIC.equals(type) || "m.room.member".equals(type) || Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE.equals(type) || Event.EVENT_TYPE_STATE_HISTORY_VISIBILITY.equals(type) || Event.EVENT_TYPE_STICKER.equals(type) || Event.EVENT_TYPE_STATE_ROOM_CREATE.equals(type) || (event.isCallEvent() && !Event.EVENT_TYPE_CALL_CANDIDATES.equals(type));
    }

    /* access modifiers changed from: private */
    public void onMessageSendingFailed(Event event) {
        IEventSendingListener iEventSendingListener = this.mEventSendingListener;
        if (iEventSendingListener != null) {
            try {
                iEventSendingListener.onMessageSendingFailed(event);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("onMessageSendingFailed failed ");
                sb.append(e.getMessage());
                Log.e(LOG_TAG, sb.toString(), e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onMessageSendingSucceeded(Event event) {
        IEventSendingListener iEventSendingListener = this.mEventSendingListener;
        if (iEventSendingListener != null) {
            try {
                iEventSendingListener.onMessageSendingSucceeded(event);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("onMessageSendingSucceeded failed ");
                sb.append(e.getMessage());
                Log.e(LOG_TAG, sb.toString(), e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onUnknownDevices(Event event, MXCryptoError mXCryptoError) {
        IEventSendingListener iEventSendingListener = this.mEventSendingListener;
        if (iEventSendingListener != null) {
            try {
                iEventSendingListener.onUnknownDevices(event, mXCryptoError);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("onUnknownDevices failed ");
                sb.append(e.getMessage());
                Log.e(LOG_TAG, sb.toString(), e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onConsentNotGiven(Event event, MatrixError matrixError) {
        IEventSendingListener iEventSendingListener = this.mEventSendingListener;
        if (iEventSendingListener != null) {
            try {
                iEventSendingListener.onConsentNotGiven(event, matrixError);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("onConsentNotGiven failed ");
                sb.append(e.getMessage());
                Log.e(LOG_TAG, sb.toString(), e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void add(RoomMediaMessage roomMediaMessage) {
        MessageRow addMessageRow = addMessageRow(roomMediaMessage);
        if (addMessageRow != null) {
            final Event event = addMessageRow.getEvent();
            if (!event.isUndelivered()) {
                roomMediaMessage.setEventSendingCallback(new ApiCallback<Void>() {
                    public void onSuccess(Void voidR) {
                        MatrixMessageListFragment.this.getUiHandler().post(new Runnable() {
                            public void run() {
                                MatrixMessageListFragment.this.onMessageSendingSucceeded(event);
                            }
                        });
                    }

                    private void commonFailure(final Event event) {
                        MatrixMessageListFragment.this.getUiHandler().post(new Runnable() {
                            public void run() {
                                String str;
                                FragmentActivity activity = MatrixMessageListFragment.this.getActivity();
                                if (activity != null) {
                                    String str2 = " : ";
                                    if (event.unsentException == null || !event.isUndelivered()) {
                                        if (event.unsentMatrixError != null) {
                                            if (event.unsentMatrixError instanceof MXCryptoError) {
                                                str = ((MXCryptoError) event.unsentMatrixError).getDetailedErrorDescription();
                                            } else {
                                                str = event.unsentMatrixError.getLocalizedMessage();
                                            }
                                            StringBuilder sb = new StringBuilder();
                                            sb.append(activity.getString(R.string.unable_to_send_message));
                                            sb.append(str2);
                                            sb.append(str);
                                            Toast.makeText(activity, sb.toString(), 1).show();
                                        }
                                    } else if (event.unsentException instanceof IOException) {
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append(activity.getString(R.string.unable_to_send_message));
                                        sb2.append(str2);
                                        sb2.append(activity.getString(R.string.network_error));
                                        Toast.makeText(activity, sb2.toString(), 1).show();
                                    } else {
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append(activity.getString(R.string.unable_to_send_message));
                                        sb3.append(str2);
                                        sb3.append(event.unsentException.getLocalizedMessage());
                                        Toast.makeText(activity, sb3.toString(), 1).show();
                                    }
                                    MatrixMessageListFragment.this.mAdapter.notifyDataSetChanged();
                                    MatrixMessageListFragment.this.onMessageSendingFailed(event);
                                }
                            }
                        });
                    }

                    public void onNetworkError(Exception exc) {
                        commonFailure(event);
                    }

                    public void onMatrixError(final MatrixError matrixError) {
                        if (event.mSentState == SentState.FAILED_UNKNOWN_DEVICES) {
                            MatrixMessageListFragment.this.getUiHandler().post(new Runnable() {
                                public void run() {
                                    MatrixMessageListFragment.this.mAdapter.notifyDataSetChanged();
                                    MatrixMessageListFragment.this.onUnknownDevices(event, (MXCryptoError) matrixError);
                                }
                            });
                            return;
                        }
                        if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                            MatrixMessageListFragment.this.getUiHandler().post(new Runnable() {
                                public void run() {
                                    MatrixMessageListFragment.this.mAdapter.notifyDataSetChanged();
                                    MatrixMessageListFragment.this.onConsentNotGiven(event, matrixError);
                                }
                            });
                        } else {
                            commonFailure(event);
                        }
                    }

                    public void onUnexpectedError(Exception exc) {
                        commonFailure(event);
                    }
                });
            }
        }
    }

    public void sendTextMessage(String str) {
        sendTextMessage(Message.MSGTYPE_TEXT, str, null);
    }

    public void sendTextMessage(String str, String str2, String str3) {
        this.mRoom.sendTextMessage(str, str2, str3, this.mEventCreationListener);
    }

    public void sendTextMessage(String str, String str2, Event event, String str3) {
        this.mRoom.sendTextMessage(str, str2, str3, event, this.mEventCreationListener);
    }

    public void sendEmote(String str, String str2, String str3) {
        this.mRoom.sendEmoteMessage(str, str2, str3, this.mEventCreationListener);
    }

    public void sendStickerMessage(Event event) {
        this.mRoom.sendStickerMessage(event, this.mEventCreationListener);
    }

    /* access modifiers changed from: private */
    public void commonMediaUploadError(int i, String str, final MessageRow messageRow) {
        if (i == 500) {
            messageRow.getEvent().mSentState = SentState.WAITING_RETRY;
            try {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        if (MatrixMessageListFragment.this.mPendingRelaunchTimersByEventId.containsKey(messageRow.getEvent().eventId)) {
                            MatrixMessageListFragment.this.mPendingRelaunchTimersByEventId.remove(messageRow.getEvent().eventId);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                public void run() {
                                    MatrixMessageListFragment.this.resend(messageRow.getEvent());
                                }
                            });
                        }
                    }
                }, 1000);
                this.mPendingRelaunchTimersByEventId.put(messageRow.getEvent().eventId, timer);
            } catch (Throwable th) {
                StringBuilder sb = new StringBuilder();
                sb.append("relaunchTimer.schedule failed ");
                sb.append(th.getMessage());
                Log.e(LOG_TAG, sb.toString(), th);
            }
        } else {
            messageRow.getEvent().mSentState = SentState.UNDELIVERED;
            onMessageSendingFailed(messageRow.getEvent());
            this.mAdapter.notifyDataSetChanged();
            if (getActivity() != null) {
                FragmentActivity activity = getActivity();
                if (str == null) {
                    str = getString(R.string.message_failed_to_upload);
                }
                Toast.makeText(activity, str, 1).show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void displayEncryptionAlert() {
        if (getActivity() != null) {
            new Builder(getActivity()).setMessage((CharSequence) "Fail to encrypt?").setPositiveButton(17039370, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setIcon(17301543).show();
        }
    }

    /* access modifiers changed from: private */
    public void displayMessageSendingFailed(String str) {
        if (getActivity() != null) {
            new Builder(getActivity()).setMessage((CharSequence) str).setPositiveButton(17039370, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setIcon(17301543).show();
        }
    }

    public void sendMediaMessage(final RoomMediaMessage roomMediaMessage) {
        this.mRoom.sendMediaMessage(roomMediaMessage, getMaxThumbnailWidth(), getMaxThumbnailHeight(), this.mEventCreationListener);
        roomMediaMessage.setMediaUploadListener(new MXMediaUploadListener() {
            public void onUploadStart(String str) {
                MatrixMessageListFragment.this.onMessageSendingSucceeded(roomMediaMessage.getEvent());
                MatrixMessageListFragment.this.mAdapter.notifyDataSetChanged();
            }

            public void onUploadCancel(String str) {
                MatrixMessageListFragment.this.onMessageSendingFailed(roomMediaMessage.getEvent());
                MatrixMessageListFragment.this.mAdapter.notifyDataSetChanged();
            }

            public void onUploadError(String str, int i, String str2) {
                MatrixMessageListFragment matrixMessageListFragment = MatrixMessageListFragment.this;
                matrixMessageListFragment.commonMediaUploadError(i, str2, matrixMessageListFragment.mAdapter.getMessageRow(roomMediaMessage.getEvent().eventId));
            }

            public void onUploadComplete(String str, String str2) {
                StringBuilder sb = new StringBuilder();
                sb.append("Uploaded to ");
                sb.append(str2);
                Log.d(MatrixMessageListFragment.LOG_TAG, sb.toString());
            }
        });
    }

    public void deleteUnsentEvents() {
        List<Event> unsentEvents = this.mRoom.getUnsentEvents();
        this.mRoom.deleteEvents(unsentEvents);
        for (Event event : unsentEvents) {
            this.mAdapter.removeEventById(event.eventId);
        }
        this.mAdapter.notifyDataSetChanged();
    }

    public void resendUnsentMessages() {
        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    MatrixMessageListFragment.this.resendUnsentMessages();
                }
            });
            return;
        }
        for (Event resend : this.mRoom.getUnsentEvents()) {
            resend(resend);
        }
    }

    /* access modifiers changed from: protected */
    public void resend(final Event event) {
        if (event.eventId == null) {
            Log.e(LOG_TAG, "resend : got an event with a null eventId");
        } else if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    MatrixMessageListFragment.this.resend(event);
                }
            });
        } else {
            event.originServerTs = System.currentTimeMillis();
            getSession().getDataHandler().deleteRoomEvent(event);
            this.mAdapter.removeEventById(event.eventId);
            this.mPendingRelaunchTimersByEventId.remove(event.eventId);
            Message message = JsonUtils.toMessage(event.getContent());
            RoomMediaMessage roomMediaMessage = new RoomMediaMessage(new Event(message, this.mSession.getMyUserId(), this.mRoom.getRoomId()));
            roomMediaMessage.getEvent().eventId = event.eventId;
            if (message instanceof MediaMessage) {
                sendMediaMessage(roomMediaMessage);
            } else {
                this.mRoom.sendMediaMessage(roomMediaMessage, getMaxThumbnailWidth(), getMaxThumbnailHeight(), this.mEventCreationListener);
            }
        }
    }

    public void refresh() {
        this.mAdapter.notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    public void onPaginateRequestError(Object obj) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            boolean z = obj instanceof Exception;
            String str = LOG_TAG;
            if (z) {
                StringBuilder sb = new StringBuilder();
                sb.append("Network error: ");
                Exception exc = (Exception) obj;
                sb.append(exc.getMessage());
                Log.e(str, sb.toString(), exc);
                Toast.makeText(activity, activity.getString(R.string.network_error), 0).show();
            } else if (obj instanceof MatrixError) {
                MatrixError matrixError = (MatrixError) obj;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Matrix error : ");
                sb2.append(matrixError.errcode);
                sb2.append(" - ");
                sb2.append(matrixError.getMessage());
                Log.e(str, sb2.toString());
                StringBuilder sb3 = new StringBuilder();
                sb3.append(activity.getString(R.string.matrix_error));
                sb3.append(" : ");
                sb3.append(matrixError.getLocalizedMessage());
                Toast.makeText(activity, sb3.toString(), 0).show();
            }
            hideLoadingBackProgress();
            hideLoadingForwardProgress();
            StringBuilder sb4 = new StringBuilder();
            sb4.append("requestHistory failed ");
            sb4.append(obj);
            Log.d(str, sb4.toString());
            this.mIsBackPaginating = false;
        }
    }

    /* access modifiers changed from: private */
    public void forwardPaginate() {
        boolean z = this.mLockFwdPagination;
        String str = LOG_TAG;
        if (z) {
            Log.d(str, "The forward pagination is locked.");
            return;
        }
        EventTimeline eventTimeline = this.mEventTimeLine;
        if (eventTimeline != null && !eventTimeline.isLiveTimeline()) {
            if (this.mIsFwdPaginating) {
                Log.d(str, "A forward pagination is in progress, please wait.");
            } else if (!isResumed()) {
                Log.d(str, "ignore forward pagination because the fragment is not active");
            } else {
                showLoadingForwardProgress();
                final int count = this.mAdapter.getCount();
                this.mIsFwdPaginating = this.mEventTimeLine.forwardPaginate(new ApiCallback<Integer>() {
                    /* access modifiers changed from: private */
                    public void onEndOfPagination(String str) {
                        if (str != null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("forwardPaginate fails : ");
                            sb.append(str);
                            Log.e(MatrixMessageListFragment.LOG_TAG, sb.toString());
                        }
                        MatrixMessageListFragment matrixMessageListFragment = MatrixMessageListFragment.this;
                        matrixMessageListFragment.mIsFwdPaginating = false;
                        matrixMessageListFragment.hideLoadingForwardProgress();
                    }

                    public void onSuccess(Integer num) {
                        int firstVisiblePosition = MatrixMessageListFragment.this.mMessageListView.getFirstVisiblePosition();
                        MatrixMessageListFragment.this.mLockBackPagination = true;
                        if (num.intValue() != 0) {
                            MatrixMessageListFragment.this.mMessageListView.lockSelectionOnResize();
                            MatrixMessageListFragment.this.mAdapter.notifyDataSetChanged();
                            MatrixMessageListFragment.this.mMessageListView.setSelection(firstVisiblePosition);
                            MatrixMessageListFragment.this.mMessageListView.post(new Runnable() {
                                public void run() {
                                    int count = MatrixMessageListFragment.this.mAdapter.getCount() - count;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("forwardPaginate ends with ");
                                    sb.append(count);
                                    sb.append(" new items.");
                                    Log.d(MatrixMessageListFragment.LOG_TAG, sb.toString());
                                    AnonymousClass15.this.onEndOfPagination(null);
                                    MatrixMessageListFragment.this.mLockBackPagination = false;
                                }
                            });
                            return;
                        }
                        Log.d(MatrixMessageListFragment.LOG_TAG, "forwardPaginate ends : nothing to add");
                        onEndOfPagination(null);
                        MatrixMessageListFragment.this.mLockBackPagination = false;
                    }

                    public void onNetworkError(Exception exc) {
                        onEndOfPagination(exc.getLocalizedMessage());
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        onEndOfPagination(matrixError.getLocalizedMessage());
                    }

                    public void onUnexpectedError(Exception exc) {
                        onEndOfPagination(exc.getLocalizedMessage());
                    }
                });
                if (this.mIsFwdPaginating) {
                    Log.d(str, "forwardPaginate starts");
                    showLoadingForwardProgress();
                } else {
                    hideLoadingForwardProgress();
                    Log.d(str, "forwardPaginate nothing to do");
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setMessageListViewScrollListener() {
        if (!this.mIsScrollListenerSet) {
            this.mIsScrollListenerSet = true;
            this.mMessageListView.setOnScrollListener(this.mScrollListener);
        }
    }

    public void backPaginate(final boolean z) {
        boolean z2 = this.mIsBackPaginating;
        String str = LOG_TAG;
        if (z2) {
            Log.d(str, "backPaginate is in progress : please wait");
        } else if (this.mIsInitialSyncing) {
            Log.d(str, "backPaginate : an initial sync is in progress");
        } else if (this.mLockBackPagination) {
            Log.d(str, "backPaginate : The back pagination is locked.");
        } else if (!TextUtils.isEmpty(this.mPattern)) {
            StringBuilder sb = new StringBuilder();
            sb.append("backPaginate with pattern ");
            sb.append(this.mPattern);
            Log.d(str, sb.toString());
            requestSearchHistory();
        } else if (!this.mMatrixMessagesFragment.canBackPaginate()) {
            Log.d(str, "backPaginate : cannot back paginating again");
            setMessageListViewScrollListener();
        } else if (!isResumed()) {
            Log.d(str, "backPaginate : the fragment is not anymore active");
            this.mFillHistoryOnResume = true;
        } else {
            final int count = this.mAdapter.getCount();
            this.mIsBackPaginating = this.mMatrixMessagesFragment.backPaginate(new SimpleApiCallback<Integer>(getActivity()) {
                public void onSuccess(Integer num) {
                    MatrixMessageListFragment.this.mMessageListView.post(new Runnable() {
                        public void run() {
                            MatrixMessageListFragment.this.mLockFwdPagination = true;
                            final int count = MatrixMessageListFragment.this.mAdapter.getCount() - count;
                            int firstVisiblePosition = MatrixMessageListFragment.this.mMessageListView.getFirstVisiblePosition();
                            StringBuilder sb = new StringBuilder();
                            sb.append("backPaginate : ends with ");
                            sb.append(count);
                            sb.append(" new items (total : ");
                            sb.append(MatrixMessageListFragment.this.mAdapter.getCount());
                            sb.append(")");
                            String sb2 = sb.toString();
                            String str = MatrixMessageListFragment.LOG_TAG;
                            Log.d(str, sb2);
                            if (count != 0) {
                                MatrixMessageListFragment.this.mMessageListView.lockSelectionOnResize();
                                MatrixMessageListFragment.this.mAdapter.notifyDataSetChanged();
                                int count2 = z ? MatrixMessageListFragment.this.mAdapter.getCount() - 1 : firstVisiblePosition + count;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("backPaginate : expect to jump to ");
                                sb3.append(count2);
                                Log.d(str, sb3.toString());
                                if (z || MatrixMessageListFragment.UNDEFINED_VIEW_Y_POS == MatrixMessageListFragment.this.mFirstVisibleRowY) {
                                    MatrixMessageListFragment.this.mMessageListView.setSelection(count2);
                                } else {
                                    MatrixMessageListFragment.this.mMessageListView.setSelectionFromTop(count2, -MatrixMessageListFragment.this.mFirstVisibleRowY);
                                }
                                MatrixMessageListFragment.this.mMessageListView.post(new Runnable() {
                                    public void run() {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("backPaginate : jump to ");
                                        sb.append(MatrixMessageListFragment.this.mMessageListView.getFirstVisiblePosition());
                                        Log.d(MatrixMessageListFragment.LOG_TAG, sb.toString());
                                    }
                                });
                            }
                            if (MatrixMessageListFragment.this.mMatrixMessagesFragment.canBackPaginate()) {
                                Log.d(str, "backPaginate again");
                                MatrixMessageListFragment.this.mMessageListView.post(new Runnable() {
                                    public void run() {
                                        MatrixMessageListFragment.this.mLockFwdPagination = false;
                                        MatrixMessageListFragment.this.mIsBackPaginating = false;
                                        MatrixMessageListFragment.this.mMessageListView.post(new Runnable() {
                                            public void run() {
                                                int i = count;
                                                String str = MatrixMessageListFragment.LOG_TAG;
                                                if (i == 0) {
                                                    Log.d(str, "backPaginate again because there was nothing in the current chunk");
                                                    MatrixMessageListFragment.this.backPaginate(z);
                                                } else if (!z) {
                                                    MatrixMessageListFragment.this.hideLoadingBackProgress();
                                                } else if (MatrixMessageListFragment.this.mMessageListView.getVisibility() != 0 || MatrixMessageListFragment.this.mMessageListView.getFirstVisiblePosition() >= 10) {
                                                    Log.d(str, "backPaginate : history should be filled");
                                                    MatrixMessageListFragment.this.hideLoadingBackProgress();
                                                    MatrixMessageListFragment.this.mIsInitialSyncing = false;
                                                    MatrixMessageListFragment.this.setMessageListViewScrollListener();
                                                } else {
                                                    Log.d(str, "backPaginate : fill history");
                                                    MatrixMessageListFragment.this.backPaginate(z);
                                                }
                                            }
                                        });
                                    }
                                });
                                return;
                            }
                            Log.d(str, "no more backPaginate");
                            MatrixMessageListFragment.this.setMessageListViewScrollListener();
                            MatrixMessageListFragment.this.hideLoadingBackProgress();
                            MatrixMessageListFragment.this.mIsBackPaginating = false;
                            MatrixMessageListFragment.this.mLockFwdPagination = false;
                        }
                    });
                }

                public void onNetworkError(Exception exc) {
                    MatrixMessageListFragment.this.onPaginateRequestError(exc);
                }

                public void onMatrixError(MatrixError matrixError) {
                    MatrixMessageListFragment.this.onPaginateRequestError(matrixError);
                }

                public void onUnexpectedError(Exception exc) {
                    MatrixMessageListFragment.this.onPaginateRequestError(exc);
                }
            });
            if (!this.mIsBackPaginating || getActivity() == null) {
                Log.d(str, "requestHistory : nothing to do");
            } else {
                Log.d(str, "backPaginate : starts");
                showLoadingBackProgress();
            }
        }
    }

    public void cancelCatchingRequests() {
        this.mPattern = null;
        EventTimeline eventTimeline = this.mEventTimeLine;
        if (eventTimeline != null) {
            eventTimeline.cancelPaginationRequests();
        }
        this.mIsInitialSyncing = false;
        this.mIsBackPaginating = false;
        this.mIsFwdPaginating = false;
        this.mLockBackPagination = false;
        this.mLockFwdPagination = false;
        hideInitLoading();
        hideLoadingBackProgress();
        hideLoadingForwardProgress();
    }

    public void scrollToRow(MessageRow messageRow, boolean z) {
        int i = (int) (getResources().getDisplayMetrics().density * 100.0f);
        int position = this.mAdapter.getPosition(messageRow);
        if (z && position < this.mMessageListView.getCount() - 1) {
            position++;
        }
        this.mMessageListView.setSelectionFromTop(position, i);
    }

    public void onEvent(final Event event, Direction direction, RoomState roomState) {
        if (event == null) {
            Log.e(LOG_TAG, "## onEvent() : null event");
            return;
        }
        if (TextUtils.equals(event.eventId, this.mEventId)) {
            this.mEventOriginServerTs = event.getOriginServerTs();
        }
        if (direction == Direction.FORWARDS) {
            boolean z = false;
            if (Event.EVENT_TYPE_REDACTION.equals(event.getType())) {
                MessageRow messageRow = this.mAdapter.getMessageRow(event.getRedactedEventId());
                if (messageRow != null) {
                    Event event2 = this.mSession.getDataHandler().getStore().getEvent(event.getRedactedEventId(), event.roomId);
                    if (event2 == null) {
                        this.mAdapter.removeEventById(event.getRedactedEventId());
                    } else {
                        messageRow.updateEvent(event2);
                        JsonObject contentAsJsonObject = messageRow.getEvent().getContentAsJsonObject();
                        if (contentAsJsonObject == null || contentAsJsonObject.entrySet() == null || contentAsJsonObject.entrySet().size() == 0) {
                            z = true;
                        }
                        if (!z && getActivity() != null) {
                            z = TextUtils.isEmpty(new EventDisplay(getActivity()).getTextualDisplay(event2, roomState));
                        }
                        if (z) {
                            this.mAdapter.removeEventById(event2.eventId);
                        }
                    }
                    this.mAdapter.notifyDataSetChanged();
                }
            } else if (canAddEvent(event)) {
                MessageRow messageRow2 = new MessageRow(event, roomState);
                MessagesAdapter messagesadapter = this.mAdapter;
                EventTimeline eventTimeline = this.mEventTimeLine;
                if (eventTimeline == null || eventTimeline.isLiveTimeline()) {
                    z = true;
                }
                messagesadapter.add(messageRow2, z);
                if (isResumed()) {
                    EventTimeline eventTimeline2 = this.mEventTimeLine;
                    if (eventTimeline2 != null && eventTimeline2.isLiveTimeline() && canUpdateReadMarker(messageRow2, getReadMarkerMessageRow(messageRow2))) {
                        if (this.mMessageListView.getChildCount() == 0) {
                            this.mMessageListView.post(new Runnable() {
                                public void run() {
                                    View childAt = MatrixMessageListFragment.this.mMessageListView.getChildAt(MatrixMessageListFragment.this.mMessageListView.getChildCount() - 2);
                                    if (childAt != null && childAt.getTop() >= 0) {
                                        MatrixMessageListFragment.this.mRoom.setReadMakerEventId(event.eventId);
                                        MatrixMessageListFragment.this.mAdapter.resetReadMarker();
                                    }
                                }
                            });
                        } else {
                            AutoScrollDownListView autoScrollDownListView = this.mMessageListView;
                            View childAt = autoScrollDownListView.getChildAt(autoScrollDownListView.getChildCount() - 1);
                            if (childAt != null && childAt.getTop() >= 0) {
                                this.mRoom.setReadMakerEventId(event.eventId);
                                this.mAdapter.resetReadMarker();
                            }
                        }
                    }
                }
            }
        } else if (canAddEvent(event)) {
            this.mAdapter.addToFront(new MessageRow(event, roomState));
        }
    }

    public void onEventSent(Event event, String str) {
        if (this.mAdapter.getMessageRow(event.eventId) != null || !canAddEvent(event)) {
            MessageRow messageRow = this.mAdapter.getMessageRow(str);
            if (messageRow != null) {
                this.mAdapter.remove(messageRow);
                return;
            }
            return;
        }
        if (this.mAdapter.getMessageRow(str) != null) {
            this.mAdapter.updateEventById(event, str);
        } else {
            this.mAdapter.add(new MessageRow(event, this.mRoom.getState()), true);
        }
        String str2 = this.mFutureReadMarkerEventId;
        if (str2 != null && str.equals(str2)) {
            this.mFutureReadMarkerEventId = null;
            this.mRoom.setReadMakerEventId(event.eventId);
            RoomSummary summary = this.mRoom.getDataHandler().getStore().getSummary(this.mRoom.getRoomId());
            if (summary != null) {
                this.mAdapter.updateReadMarker(event.eventId, summary.getReadReceiptEventId());
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
    public void onReceiptEvent(List<String> list) {
        boolean z;
        try {
            IMXStore store = this.mSession.getDataHandler().getStore();
            int lastVisiblePosition = this.mMessageListView.getLastVisiblePosition();
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (int firstVisiblePosition = this.mMessageListView.getFirstVisiblePosition(); firstVisiblePosition <= lastVisiblePosition; firstVisiblePosition++) {
                Event event = ((MessageRow) this.mAdapter.getItem(firstVisiblePosition)).getEvent();
                if (!(event.getSender() == null || event.eventId == null)) {
                    arrayList.add(event.getSender());
                    arrayList2.add(event.eventId);
                }
            }
            z = false;
            try {
                for (String str : list) {
                    if (!TextUtils.equals(str, this.mSession.getMyUserId())) {
                        ReceiptData receipt = store.getReceipt(this.mRoom.getRoomId(), str);
                        if (receipt != null) {
                            int indexOf = arrayList2.indexOf(receipt.eventId);
                            if (indexOf >= 0) {
                                z = !TextUtils.equals((CharSequence) arrayList.get(indexOf), str);
                                if (z) {
                                    break;
                                }
                            } else {
                                continue;
                            }
                        } else {
                            continue;
                        }
                    }
                }
            } catch (Exception e) {
                e = e;
                StringBuilder sb = new StringBuilder();
                sb.append("onReceiptEvent failed with ");
                sb.append(e.getMessage());
                Log.e(LOG_TAG, sb.toString(), e);
                if (z) {
                }
            }
        } catch (Exception e2) {
            e = e2;
            z = true;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onReceiptEvent failed with ");
            sb2.append(e.getMessage());
            Log.e(LOG_TAG, sb2.toString(), e);
            if (z) {
            }
        }
        if (z) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void onInitialMessagesLoaded() {
        Log.d(LOG_TAG, "onInitialMessagesLoaded");
        getUiHandler().post(new Runnable() {
            public void run() {
                if (MatrixMessageListFragment.this.mMessageListView != null) {
                    MatrixMessageListFragment.this.hideLoadingBackProgress();
                    if (MatrixMessageListFragment.this.mMessageListView.getAdapter() == null) {
                        MatrixMessageListFragment.this.mMessageListView.setAdapter(MatrixMessageListFragment.this.mAdapter);
                    }
                    if (MatrixMessageListFragment.this.mEventTimeLine == null || MatrixMessageListFragment.this.mEventTimeLine.isLiveTimeline()) {
                        if (MatrixMessageListFragment.this.mAdapter.getCount() > 0) {
                            MatrixMessageListFragment.this.mAdapter.notifyDataSetChanged();
                            if (MatrixMessageListFragment.this.mScrollToIndex >= 0) {
                                MatrixMessageListFragment.this.mMessageListView.setSelection(MatrixMessageListFragment.this.mScrollToIndex);
                                MatrixMessageListFragment.this.mScrollToIndex = -1;
                            } else {
                                MatrixMessageListFragment.this.mMessageListView.setSelection(MatrixMessageListFragment.this.mAdapter.getCount() - 1);
                            }
                        }
                        MatrixMessageListFragment.this.mMessageListView.post(new Runnable() {
                            public void run() {
                                int visibility = MatrixMessageListFragment.this.mMessageListView.getVisibility();
                                String str = MatrixMessageListFragment.LOG_TAG;
                                if (visibility != 0 || MatrixMessageListFragment.this.mMessageListView.getFirstVisiblePosition() >= 10) {
                                    Log.d(str, "onInitialMessagesLoaded : history should be filled");
                                    MatrixMessageListFragment.this.mIsInitialSyncing = false;
                                    MatrixMessageListFragment.this.setMessageListViewScrollListener();
                                    return;
                                }
                                Log.d(str, "onInitialMessagesLoaded : fill history");
                                MatrixMessageListFragment.this.backPaginate(true);
                            }
                        });
                    } else {
                        Log.d(MatrixMessageListFragment.LOG_TAG, "onInitialMessagesLoaded : default behaviour");
                        if (MatrixMessageListFragment.this.mAdapter.getCount() == 0 || MatrixMessageListFragment.this.mScrollToIndex <= 0) {
                            MatrixMessageListFragment matrixMessageListFragment = MatrixMessageListFragment.this;
                            matrixMessageListFragment.mIsInitialSyncing = false;
                            matrixMessageListFragment.setMessageListViewScrollListener();
                        } else {
                            MatrixMessageListFragment.this.mAdapter.notifyDataSetChanged();
                            MatrixMessageListFragment.this.mMessageListView.setSelection(MatrixMessageListFragment.this.mScrollToIndex);
                            MatrixMessageListFragment.this.mScrollToIndex = -1;
                            MatrixMessageListFragment.this.mMessageListView.post(new Runnable() {
                                public void run() {
                                    MatrixMessageListFragment.this.mIsInitialSyncing = false;
                                    MatrixMessageListFragment.this.setMessageListViewScrollListener();
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    public EventTimeline getEventTimeLine() {
        return this.mEventTimeLine;
    }

    public void onTimelineInitialized() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            this.mMessageListView.post(new Runnable() {
                public void run() {
                    if (MatrixMessageListFragment.this.getActivity() == null) {
                        Log.e(MatrixMessageListFragment.LOG_TAG, "## onTimelineInitialized : the fragment is not anymore attached to an activity");
                        return;
                    }
                    int i = 0;
                    MatrixMessageListFragment.this.mLockFwdPagination = false;
                    MatrixMessageListFragment matrixMessageListFragment = MatrixMessageListFragment.this;
                    matrixMessageListFragment.mIsInitialSyncing = false;
                    if (!matrixMessageListFragment.mAdapter.isUnreadViewMode() || MatrixMessageListFragment.this.mAdapter.getMessageRow(MatrixMessageListFragment.this.mEventId) != null) {
                        while (i < MatrixMessageListFragment.this.mAdapter.getCount() && !TextUtils.equals(((MessageRow) MatrixMessageListFragment.this.mAdapter.getItem(i)).getEvent().eventId, MatrixMessageListFragment.this.mEventId)) {
                            i++;
                        }
                        MatrixMessageListFragment.this.mAdapter.notifyDataSetChanged();
                        MatrixMessageListFragment.this.mMessageListView.setAdapter(MatrixMessageListFragment.this.mAdapter);
                        if (MatrixMessageListFragment.this.mAdapter.isUnreadViewMode()) {
                            MatrixMessageListFragment matrixMessageListFragment2 = MatrixMessageListFragment.this;
                            matrixMessageListFragment2.scrollToRow(matrixMessageListFragment2.mAdapter.getMessageRow(MatrixMessageListFragment.this.mEventId), true);
                        } else {
                            MatrixMessageListFragment.this.mMessageListView.setSelectionFromTop(i, ((View) MatrixMessageListFragment.this.mMessageListView.getParent()).getHeight() / 2);
                        }
                    } else {
                        MessageRow closestRowFromTs = MatrixMessageListFragment.this.mAdapter.getClosestRowFromTs(MatrixMessageListFragment.this.mEventId, MatrixMessageListFragment.this.mEventOriginServerTs);
                        int position = MatrixMessageListFragment.this.mAdapter.getPosition(closestRowFromTs);
                        if (position > 0) {
                            closestRowFromTs = (MessageRow) MatrixMessageListFragment.this.mAdapter.getItem(position - 1);
                        }
                        if (closestRowFromTs != null) {
                            MatrixMessageListFragment.this.mAdapter.updateReadMarker(closestRowFromTs.getEvent().eventId, null);
                        }
                        MatrixMessageListFragment.this.mAdapter.notifyDataSetChanged();
                        MatrixMessageListFragment.this.mMessageListView.setAdapter(MatrixMessageListFragment.this.mAdapter);
                        if (closestRowFromTs != null) {
                            MatrixMessageListFragment.this.scrollToRow(closestRowFromTs, true);
                        }
                    }
                }
            });
        }
    }

    public RoomPreviewData getRoomPreviewData() {
        if (getActivity() != null) {
            if (this.mRoomPreviewDataListener == null) {
                try {
                    this.mRoomPreviewDataListener = (IRoomPreviewDataListener) getActivity();
                } catch (ClassCastException e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("getRoomPreviewData failed with ");
                    sb.append(e.getMessage());
                    Log.e(LOG_TAG, sb.toString(), e);
                }
            }
            IRoomPreviewDataListener iRoomPreviewDataListener = this.mRoomPreviewDataListener;
            if (iRoomPreviewDataListener != null) {
                return iRoomPreviewDataListener.getRoomPreviewData();
            }
        }
        return null;
    }

    public void onRoomFlush() {
        this.mAdapter.clear();
    }

    /* access modifiers changed from: protected */
    public void cancelSearch() {
        this.mPattern = null;
    }

    public void requestSearchHistory() {
        if (TextUtils.isEmpty(this.mNextBatch)) {
            this.mIsBackPaginating = false;
            return;
        }
        this.mIsBackPaginating = true;
        final int firstVisiblePosition = this.mMessageListView.getFirstVisiblePosition();
        final String str = this.mPattern;
        final int count = this.mAdapter.getCount();
        showLoadingBackProgress();
        List list = null;
        Room room = this.mRoom;
        if (room != null) {
            list = Arrays.asList(new String[]{room.getRoomId()});
        }
        AnonymousClass20 r0 = new ApiCallback<SearchResponse>() {
            public void onSuccess(SearchResponse searchResponse) {
                if (TextUtils.equals(MatrixMessageListFragment.this.mPattern, str)) {
                    List<SearchResult> list = searchResponse.searchCategories.roomEvents.results;
                    if (list.size() != 0) {
                        MatrixMessageListFragment.this.mAdapter.setNotifyOnChange(false);
                        for (SearchResult searchResult : list) {
                            MatrixMessageListFragment.this.mAdapter.insert(new MessageRow(searchResult.result, MatrixMessageListFragment.this.mRoom == null ? null : MatrixMessageListFragment.this.mRoom.getState()), 0);
                        }
                        MatrixMessageListFragment.this.mNextBatch = searchResponse.searchCategories.roomEvents.nextBatch;
                        MatrixMessageListFragment.this.getUiHandler().post(new Runnable() {
                            public void run() {
                                int count = firstVisiblePosition + (MatrixMessageListFragment.this.mAdapter.getCount() - count);
                                MatrixMessageListFragment.this.mMessageListView.lockSelectionOnResize();
                                MatrixMessageListFragment.this.mAdapter.notifyDataSetChanged();
                                MatrixMessageListFragment.this.mMessageListView.setSelection(count);
                                MatrixMessageListFragment.this.mMessageListView.post(new Runnable() {
                                    public void run() {
                                        MatrixMessageListFragment.this.mIsBackPaginating = false;
                                        if (MatrixMessageListFragment.this.mMessageListView.getFirstVisiblePosition() <= 2) {
                                            MatrixMessageListFragment.this.requestSearchHistory();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        MatrixMessageListFragment.this.mIsBackPaginating = false;
                    }
                    MatrixMessageListFragment.this.hideLoadingBackProgress();
                }
            }

            private void onError() {
                MatrixMessageListFragment matrixMessageListFragment = MatrixMessageListFragment.this;
                matrixMessageListFragment.mIsBackPaginating = false;
                matrixMessageListFragment.hideLoadingBackProgress();
            }

            public void onNetworkError(Exception exc) {
                StringBuilder sb = new StringBuilder();
                sb.append("Network error: ");
                sb.append(exc.getMessage());
                Log.e(MatrixMessageListFragment.LOG_TAG, sb.toString(), exc);
                onError();
            }

            public void onMatrixError(MatrixError matrixError) {
                StringBuilder sb = new StringBuilder();
                sb.append("Matrix error : ");
                sb.append(matrixError.errcode);
                sb.append(" - ");
                sb.append(matrixError.getMessage());
                Log.e(MatrixMessageListFragment.LOG_TAG, sb.toString());
                onError();
            }

            public void onUnexpectedError(Exception exc) {
                StringBuilder sb = new StringBuilder();
                sb.append("onUnexpectedError error");
                sb.append(exc.getMessage());
                Log.e(MatrixMessageListFragment.LOG_TAG, sb.toString(), exc);
                onError();
            }
        };
        if (this.mIsMediaSearch) {
            this.mSession.searchMediaByName(this.mPattern, list, this.mNextBatch, r0);
        } else {
            this.mSession.searchMessagesByText(this.mPattern, list, this.mNextBatch, r0);
        }
    }

    /* access modifiers changed from: protected */
    public void onSearchResponse(SearchResponse searchResponse, OnSearchResultListener onSearchResultListener) {
        List<SearchResult> list = searchResponse.searchCategories.roomEvents.results;
        ArrayList arrayList = new ArrayList(list.size());
        for (SearchResult searchResult : list) {
            RoomState roomState = null;
            Room room = this.mRoom;
            if (room != null) {
                roomState = room.getState();
            }
            if (roomState == null) {
                Room room2 = this.mSession.getDataHandler().getStore().getRoom(searchResult.result.roomId);
                if (room2 != null) {
                    roomState = room2.getState();
                }
            }
            boolean z = false;
            if (!(searchResult.result == null || searchResult.result.getContent() == null)) {
                JsonObject contentAsJsonObject = searchResult.result.getContentAsJsonObject();
                if (!(contentAsJsonObject == null || contentAsJsonObject.entrySet().size() == 0)) {
                    z = true;
                }
            }
            if (z) {
                arrayList.add(new MessageRow(searchResult.result, roomState));
            }
        }
        Collections.reverse(arrayList);
        this.mAdapter.clear();
        this.mAdapter.addAll(arrayList);
        this.mNextBatch = searchResponse.searchCategories.roomEvents.nextBatch;
        if (onSearchResultListener != null) {
            try {
                onSearchResultListener.onSearchSucceed(arrayList.size());
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("onSearchResponse failed with ");
                sb.append(e.getMessage());
                Log.e(LOG_TAG, sb.toString(), e);
            }
        }
    }

    public void searchPattern(String str, OnSearchResultListener onSearchResultListener) {
        searchPattern(str, false, onSearchResultListener);
    }

    public void searchPattern(final String str, boolean z, final OnSearchResultListener onSearchResultListener) {
        List list;
        if (!TextUtils.equals(this.mPattern, str)) {
            this.mPattern = str;
            this.mIsMediaSearch = z;
            this.mAdapter.setSearchPattern(this.mPattern);
            if (!TextUtils.isEmpty(this.mPattern)) {
                Room room = this.mRoom;
                if (room != null) {
                    list = Arrays.asList(new String[]{room.getRoomId()});
                } else {
                    list = null;
                }
                AnonymousClass21 r2 = new ApiCallback<SearchResponse>() {
                    public void onSuccess(final SearchResponse searchResponse) {
                        MatrixMessageListFragment.this.getUiHandler().post(new Runnable() {
                            public void run() {
                                if (TextUtils.equals(MatrixMessageListFragment.this.mPattern, str)) {
                                    MatrixMessageListFragment.this.onSearchResponse(searchResponse, onSearchResultListener);
                                }
                            }
                        });
                    }

                    private void onError() {
                        MatrixMessageListFragment.this.getUiHandler().post(new Runnable() {
                            public void run() {
                                if (onSearchResultListener != null) {
                                    try {
                                        onSearchResultListener.onSearchFailed();
                                    } catch (Exception e) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("onSearchResultListener failed with ");
                                        sb.append(e.getMessage());
                                        Log.e(MatrixMessageListFragment.LOG_TAG, sb.toString(), e);
                                    }
                                }
                            }
                        });
                    }

                    public void onNetworkError(Exception exc) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Network error: ");
                        sb.append(exc.getMessage());
                        Log.e(MatrixMessageListFragment.LOG_TAG, sb.toString(), exc);
                        onError();
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Matrix error : ");
                        sb.append(matrixError.errcode);
                        sb.append(" - ");
                        sb.append(matrixError.getMessage());
                        Log.e(MatrixMessageListFragment.LOG_TAG, sb.toString());
                        onError();
                    }

                    public void onUnexpectedError(Exception exc) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("onUnexpectedError error");
                        sb.append(exc.getMessage());
                        Log.e(MatrixMessageListFragment.LOG_TAG, sb.toString(), exc);
                        onError();
                    }
                };
                if (z) {
                    this.mSession.searchMediaByName(this.mPattern, list, null, r2);
                } else {
                    this.mSession.searchMessagesByText(this.mPattern, list, null, r2);
                }
            }
        }
    }
}
