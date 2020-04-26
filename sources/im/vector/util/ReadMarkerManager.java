package im.vector.util;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import fr.gouv.tchap.a.R;
import im.vector.activity.VectorRoomActivity;
import im.vector.adapters.VectorMessagesAdapter;
import im.vector.adapters.VectorMessagesAdapter.ReadMarkerListener;
import im.vector.fragments.VectorMessageListFragment;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.adapters.MessageRow;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXPatterns;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.rest.model.Event;

public class ReadMarkerManager implements ReadMarkerListener {
    public static final int LIVE_MODE = 0;
    private static final String LOG_TAG = ReadMarkerManager.class.getSimpleName();
    public static final int PREVIEW_MODE = 1;
    private static final int UNREAD_BACK_PAGINATE_EVENT_COUNT = 100;
    /* access modifiers changed from: private */
    public VectorRoomActivity mActivity;
    private View mCloseJumpToUnreadView;
    private Event mFirstVisibleEvent;
    private boolean mHasJumpedToBottom;
    /* access modifiers changed from: private */
    public boolean mHasJumpedToFirstUnread;
    private View mJumpToUnreadView;
    private View mJumpToUnreadViewSpinner;
    private Event mLastVisibleEvent;
    /* access modifiers changed from: private */
    public String mReadMarkerEventId;
    /* access modifiers changed from: private */
    public Room mRoom;
    private RoomSummary mRoomSummary;
    private int mScrollState = -1;
    private MXSession mSession;
    private int mUpdateMode = -1;
    /* access modifiers changed from: private */
    public VectorMessageListFragment mVectorMessageListFragment;

    @Retention(RetentionPolicy.SOURCE)
    @interface UpdateMode {
    }

    public ReadMarkerManager(final VectorRoomActivity vectorRoomActivity, VectorMessageListFragment vectorMessageListFragment, MXSession mXSession, Room room, int i, View view) {
        if (room != null) {
            this.mActivity = vectorRoomActivity;
            this.mVectorMessageListFragment = vectorMessageListFragment;
            this.mSession = mXSession;
            this.mRoom = room;
            this.mRoomSummary = this.mRoom.getDataHandler().getStore().getSummary(this.mRoom.getRoomId());
            this.mReadMarkerEventId = this.mRoomSummary.getReadMarkerEventId();
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Create ReadMarkerManager instance id:");
            sb.append(this.mReadMarkerEventId);
            sb.append(" for room:");
            sb.append(this.mRoom.getRoomId());
            Log.d(str, sb.toString());
            this.mUpdateMode = i;
            if (view != null) {
                this.mJumpToUnreadView = view;
                TextView textView = (TextView) view.findViewById(R.id.jump_to_first_unread_label);
                textView.setPaintFlags(textView.getPaintFlags() | 8);
                this.mCloseJumpToUnreadView = view.findViewById(R.id.close_jump_to_first_unread);
                this.mJumpToUnreadViewSpinner = view.findViewById(R.id.jump_to_read_spinner);
                if (isLiveMode()) {
                    textView.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            vectorRoomActivity.dismissKeyboard();
                            ReadMarkerManager.this.updateReadMarkerValue();
                            if (!TextUtils.isEmpty(ReadMarkerManager.this.mReadMarkerEventId)) {
                                Event event = ReadMarkerManager.this.mRoom.getDataHandler().getStore().getEvent(ReadMarkerManager.this.mReadMarkerEventId, ReadMarkerManager.this.mRoom.getRoomId());
                                if (event == null) {
                                    ReadMarkerManager readMarkerManager = ReadMarkerManager.this;
                                    readMarkerManager.openPreviewToGivenEvent(readMarkerManager.mReadMarkerEventId);
                                    return;
                                }
                                ReadMarkerManager.this.scrollUpToGivenEvent(event);
                            }
                        }
                    });
                    this.mCloseJumpToUnreadView.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            ReadMarkerManager.this.forgetReadMarker();
                        }
                    });
                }
            }
        }
    }

    public void onResume() {
        ((VectorMessagesAdapter) this.mVectorMessageListFragment.getMessageAdapter()).setReadMarkerListener(this);
        updateJumpToBanner();
    }

    public void onPause() {
        if (!isLiveMode() || this.mHasJumpedToFirstUnread) {
            setReadMarkerToLastVisibleRow();
        }
    }

    public void onScroll(int i, int i2, int i3, Event event, Event event2) {
        this.mFirstVisibleEvent = event;
        this.mLastVisibleEvent = event2;
        if (isLiveMode()) {
            updateJumpToBanner();
        } else if (this.mVectorMessageListFragment.getEventTimeLine().hasReachedHomeServerForwardsPaginationEnd()) {
            ListView messageListView = this.mVectorMessageListFragment.getMessageListView();
            if (messageListView != null && i + i2 == i3 && messageListView.getChildAt(messageListView.getChildCount() - 1).getBottom() == messageListView.getBottom()) {
                this.mActivity.setResult(-1);
                this.mActivity.finish();
            }
        }
    }

    public void onScrollStateChanged(int i) {
        if (i == 0) {
            int i2 = this.mScrollState;
            if (i2 == 2 || i2 == 1) {
                checkUnreadMessage();
            }
        }
        this.mScrollState = i;
    }

    public void onReadMarkerChanged(String str) {
        if (TextUtils.equals(this.mRoom.getRoomId(), str)) {
            String readMarkerEventId = this.mRoomSummary.getReadMarkerEventId();
            if (!TextUtils.equals(readMarkerEventId, this.mReadMarkerEventId)) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onReadMarkerChanged");
                sb.append(readMarkerEventId);
                Log.d(str2, sb.toString());
                refresh();
            }
        }
    }

    public void handleJumpToBottom() {
        this.mHasJumpedToBottom = true;
        if (isLiveMode() && this.mHasJumpedToFirstUnread) {
            setReadMarkerToLastVisibleRow();
            this.mHasJumpedToFirstUnread = false;
        }
        this.mVectorMessageListFragment.getMessageAdapter().updateReadMarker(this.mReadMarkerEventId, this.mRoomSummary.getReadReceiptEventId());
        this.mVectorMessageListFragment.scrollToBottom(0);
    }

    private void checkUnreadMessage() {
        Log.d(LOG_TAG, "checkUnreadMessage");
        if (this.mJumpToUnreadView.getVisibility() != 0) {
            String readReceiptEventId = this.mRoomSummary.getReadReceiptEventId();
            String str = this.mReadMarkerEventId;
            if (str != null && !str.equals(readReceiptEventId)) {
                if (!isLiveMode() || this.mHasJumpedToFirstUnread) {
                    Event event = this.mLastVisibleEvent;
                    if (event == null) {
                        return;
                    }
                    if (event.eventId.equals(this.mRoomSummary.getLatestReceivedEvent().eventId)) {
                        Log.d(LOG_TAG, "checkUnreadMessage: last received event has been reached by scrolling down");
                        markAllAsRead();
                    } else if (!isLiveMode()) {
                        Log.d(LOG_TAG, "checkUnreadMessage: preview mode, set read marker to last visible row");
                        setReadMarkerToLastVisibleRow();
                    }
                } else {
                    MessageRow messageRow = this.mVectorMessageListFragment.getMessageAdapter().getMessageRow(this.mReadMarkerEventId);
                    if (messageRow != null && messageRow.getEvent() != null && this.mFirstVisibleEvent != null && messageRow.getEvent().getOriginServerTs() >= this.mFirstVisibleEvent.getOriginServerTs()) {
                        Log.d(LOG_TAG, "checkUnreadMessage: first unread has been reached by scrolling up");
                        forgetReadMarker();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateReadMarkerValue() {
        this.mReadMarkerEventId = this.mRoomSummary.getReadMarkerEventId();
        this.mVectorMessageListFragment.getMessageAdapter().updateReadMarker(this.mReadMarkerEventId, this.mRoomSummary.getReadReceiptEventId());
    }

    private void refresh() {
        Log.d(LOG_TAG, "refresh");
        updateReadMarkerValue();
        updateJumpToBanner();
        checkUnreadMessage();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00da  */
    public synchronized void updateJumpToBanner() {
        boolean z;
        this.mReadMarkerEventId = this.mRoomSummary.getReadMarkerEventId();
        if (this.mRoomSummary == null || this.mReadMarkerEventId == null || this.mHasJumpedToFirstUnread) {
            z = false;
        } else {
            String readReceiptEventId = this.mRoomSummary.getReadReceiptEventId();
            z = true;
            if (!this.mReadMarkerEventId.equals(readReceiptEventId)) {
                if (!MXPatterns.isEventId(this.mReadMarkerEventId)) {
                    Log.e(LOG_TAG, "updateJumpToBanner: Read marker event id is invalid, ignore it as it should not occur");
                } else {
                    Event event = getEvent(this.mReadMarkerEventId);
                    if (event != null) {
                        Collection roomMessages = this.mRoom.getDataHandler().getStore().getRoomMessages(this.mRoom.getRoomId());
                        if (roomMessages == null) {
                            String str = LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("updateJumpToBanner getRoomMessages returned null instead of collection with event ");
                            sb.append(event.eventId);
                            Log.e(str, sb.toString());
                        } else {
                            ArrayList arrayList = new ArrayList(roomMessages);
                            int indexOf = arrayList.indexOf(event);
                            int i = indexOf != -1 ? indexOf + 1 : -1;
                            if (i != -1 && i < arrayList.size()) {
                                Event event2 = (Event) arrayList.get(i);
                                if (!(this.mFirstVisibleEvent == null || event2 == null)) {
                                    if (event2.getOriginServerTs() <= this.mFirstVisibleEvent.getOriginServerTs()) {
                                        if (event2.getOriginServerTs() == this.mFirstVisibleEvent.getOriginServerTs()) {
                                            ListView messageListView = this.mVectorMessageListFragment.getMessageListView();
                                            View childAt = messageListView != null ? messageListView.getChildAt(0) : null;
                                            if (childAt == null || childAt.getTop() >= 0) {
                                                z = false;
                                            }
                                            if (this.mHasJumpedToFirstUnread && !z) {
                                                forgetReadMarker();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (this.mVectorMessageListFragment.getMessageAdapter() != null) {
                        this.mVectorMessageListFragment.getMessageAdapter().updateReadMarker(this.mReadMarkerEventId, readReceiptEventId);
                    }
                }
            }
            z = false;
            if (this.mVectorMessageListFragment.getMessageAdapter() != null) {
            }
        }
        if (!isLiveMode() || !z) {
            this.mJumpToUnreadView.setVisibility(8);
        } else {
            this.mJumpToUnreadViewSpinner.setVisibility(8);
            this.mCloseJumpToUnreadView.setVisibility(0);
            this.mJumpToUnreadView.setVisibility(0);
        }
        if (this.mHasJumpedToBottom) {
            this.mHasJumpedToBottom = false;
            checkUnreadMessage();
        }
    }

    private Event getEvent(String str) {
        MessageRow messageRow = this.mVectorMessageListFragment.getMessageAdapter().getMessageRow(str);
        Event event = messageRow != null ? messageRow.getEvent() : null;
        return event == null ? this.mVectorMessageListFragment.getEventTimeLine().getStore().getEvent(this.mReadMarkerEventId, this.mRoom.getRoomId()) : event;
    }

    private boolean isLiveMode() {
        return this.mUpdateMode == 0;
    }

    /* access modifiers changed from: private */
    public void openPreviewToGivenEvent(String str) {
        if (!TextUtils.isEmpty(str)) {
            Intent intent = new Intent(this.mActivity, VectorRoomActivity.class);
            intent.putExtra("EXTRA_ROOM_ID", this.mRoom.getRoomId());
            intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getMyUserId());
            intent.putExtra(VectorRoomActivity.EXTRA_EVENT_ID, str);
            intent.putExtra(VectorRoomActivity.EXTRA_IS_UNREAD_PREVIEW_MODE, true);
            this.mActivity.startActivityForResult(intent, 5);
        }
    }

    /* access modifiers changed from: private */
    public void scrollUpToGivenEvent(final Event event) {
        if (event != null) {
            this.mCloseJumpToUnreadView.setVisibility(8);
            this.mJumpToUnreadViewSpinner.setVisibility(0);
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("scrollUpToGivenEvent ");
            sb.append(event.eventId);
            Log.d(str, sb.toString());
            if (!scrollToAdapterEvent(event)) {
                this.mRoom.getTimeline().backPaginate(100, true, new ApiCallback<Integer>() {
                    public void onSuccess(Integer num) {
                        if (!ReadMarkerManager.this.mActivity.isFinishing()) {
                            ReadMarkerManager.this.mVectorMessageListFragment.getMessageAdapter().notifyDataSetChanged();
                            if (!ReadMarkerManager.this.scrollToAdapterEvent(event)) {
                                ReadMarkerManager.this.openPreviewToGivenEvent(event.eventId);
                            }
                        }
                    }

                    public void onNetworkError(Exception exc) {
                        ReadMarkerManager.this.openPreviewToGivenEvent(event.eventId);
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        ReadMarkerManager.this.openPreviewToGivenEvent(event.eventId);
                    }

                    public void onUnexpectedError(Exception exc) {
                        ReadMarkerManager.this.openPreviewToGivenEvent(event.eventId);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean scrollToAdapterEvent(Event event) {
        Event event2 = null;
        MessageRow messageRow = this.mVectorMessageListFragment.getMessageAdapter() != null ? this.mVectorMessageListFragment.getMessageAdapter().getMessageRow(event.eventId) : null;
        if (messageRow != null) {
            scrollToRow(messageRow, true);
            return true;
        }
        Log.d(LOG_TAG, "scrollToAdapterEvent: need to load more events in adapter or eventId is not displayed");
        if (this.mVectorMessageListFragment.getMessageAdapter().getCount() > 0) {
            MessageRow messageRow2 = (MessageRow) this.mVectorMessageListFragment.getMessageAdapter().getItem(0);
            Event event3 = messageRow2 != null ? messageRow2.getEvent() : null;
            MessageRow messageRow3 = (MessageRow) this.mVectorMessageListFragment.getMessageAdapter().getItem(this.mVectorMessageListFragment.getMessageAdapter().getCount() - 1);
            if (messageRow3 != null) {
                event2 = messageRow3.getEvent();
            }
            if (event3 != null && event2 != null && event.getOriginServerTs() > event3.getOriginServerTs() && event.getOriginServerTs() < event2.getOriginServerTs()) {
                MessageRow closestRow = this.mVectorMessageListFragment.getMessageAdapter().getClosestRow(event);
                if (closestRow != null) {
                    scrollToRow(closestRow, closestRow.getEvent().eventId.equals(event.eventId));
                    return true;
                }
            }
        }
        return false;
    }

    private void scrollToRow(final MessageRow messageRow, final boolean z) {
        this.mVectorMessageListFragment.getMessageListView().post(new Runnable() {
            public void run() {
                ReadMarkerManager.this.mVectorMessageListFragment.scrollToRow(messageRow, z);
                ReadMarkerManager.this.mHasJumpedToFirstUnread = true;
            }
        });
    }

    private void setReadMarkerToLastVisibleRow() {
        Event event;
        Log.d(LOG_TAG, "setReadMarkerToLastVisibleRow");
        ListView messageListView = this.mVectorMessageListFragment.getMessageListView();
        if (messageListView != null && messageListView.getChildCount() != 0 && this.mVectorMessageListFragment.getMessageAdapter() != null) {
            int lastVisiblePosition = messageListView.getLastVisiblePosition();
            if (messageListView.getChildAt(messageListView.getChildCount() - 1).getBottom() <= messageListView.getBottom()) {
                event = this.mVectorMessageListFragment.getEvent(lastVisiblePosition);
            } else {
                event = this.mVectorMessageListFragment.getEvent(lastVisiblePosition - 1);
            }
            Event event2 = getEvent(this.mReadMarkerEventId);
            if (event2 != null) {
                long originServerTs = event2.getOriginServerTs();
                MessageRow closestRow = this.mVectorMessageListFragment.getMessageAdapter().getClosestRow(event);
                if (closestRow != null) {
                    Event event3 = closestRow.getEvent();
                    long originServerTs2 = event3.getOriginServerTs();
                    String str = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("setReadMarkerToLastVisibleRow currentReadMarkerEvent:");
                    sb.append(event2.eventId);
                    String str2 = " TS:";
                    sb.append(str2);
                    sb.append(originServerTs);
                    sb.append(" closestEvent:");
                    sb.append(event3.eventId);
                    sb.append(str2);
                    sb.append(event3.getOriginServerTs());
                    Log.v(str, sb.toString());
                    if (originServerTs2 > originServerTs) {
                        String str3 = LOG_TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("setReadMarkerToLastVisibleRow update read marker to:");
                        sb2.append(event.eventId);
                        sb2.append(" isEventId:");
                        sb2.append(MXPatterns.isEventId(event.eventId));
                        Log.d(str3, sb2.toString());
                        this.mRoom.setReadMakerEventId(event.eventId);
                        onReadMarkerChanged(this.mRoom.getRoomId());
                    }
                }
            }
        }
    }

    private void markAllAsRead() {
        Log.d(LOG_TAG, "markAllAsRead");
        this.mRoom.markAllAsRead(null);
    }

    /* access modifiers changed from: private */
    public void forgetReadMarker() {
        Log.d(LOG_TAG, "forgetReadMarker");
        this.mRoom.forgetReadMarker(new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                ReadMarkerManager.this.updateJumpToBanner();
            }

            public void onNetworkError(Exception exc) {
                ReadMarkerManager.this.updateJumpToBanner();
            }

            public void onMatrixError(MatrixError matrixError) {
                ReadMarkerManager.this.updateJumpToBanner();
            }

            public void onUnexpectedError(Exception exc) {
                ReadMarkerManager.this.updateJumpToBanner();
            }
        });
    }

    public void onReadMarkerDisplayed(Event event, View view) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onReadMarkerDisplayed for ");
        sb.append(event.eventId);
        Log.d(str, sb.toString());
        if (!this.mActivity.isFinishing()) {
            if (this.mLastVisibleEvent == null) {
                try {
                    this.mLastVisibleEvent = this.mVectorMessageListFragment.getEvent(this.mVectorMessageListFragment.getMessageListView().getLastVisiblePosition());
                } catch (Exception e) {
                    String str2 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## onReadMarkerDisplayed() : crash while retrieving mLastVisibleEvent ");
                    sb2.append(e.getMessage());
                    Log.e(str2, sb2.toString(), e);
                }
            }
            if (this.mFirstVisibleEvent == null) {
                try {
                    this.mFirstVisibleEvent = this.mVectorMessageListFragment.getEvent(this.mVectorMessageListFragment.getMessageListView().getFirstVisiblePosition());
                } catch (Exception e2) {
                    String str3 = LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("## onReadMarkerDisplayed() : crash while retrieving mFirstVisibleEvent ");
                    sb3.append(e2.getMessage());
                    Log.e(str3, sb3.toString(), e2);
                }
            }
            checkUnreadMessage();
        }
    }
}
