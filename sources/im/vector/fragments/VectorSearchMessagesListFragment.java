package im.vector.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import fr.gouv.tchap.a.R;
import im.vector.activity.VectorRoomActivity;
import im.vector.adapters.VectorMessagesAdapter;
import im.vector.adapters.VectorSearchMessagesListAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.matrix.androidsdk.adapters.MessageRow;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.timeline.EventTimeline.Direction;
import org.matrix.androidsdk.fragments.MatrixMessageListFragment.OnSearchResultListener;
import org.matrix.androidsdk.rest.model.Event;

public class VectorSearchMessagesListFragment extends VectorMessageListFragment {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorSearchMessagesListFragment.class.getSimpleName();
    private View mProgressView = null;
    final ArrayList<OnSearchResultListener> mSearchListeners = new ArrayList<>();
    /* access modifiers changed from: private */
    public String mSearchingPattern;

    public boolean isDisplayAllEvents() {
        return true;
    }

    public boolean onContentLongClick(int i) {
        return false;
    }

    public void onEvent(Event event, Direction direction, RoomState roomState) {
    }

    public void onListTouch(MotionEvent motionEvent) {
    }

    public void onLiveEventsChunkProcessed() {
    }

    public void onReceiptEvent(List<String> list) {
    }

    public static VectorSearchMessagesListFragment newInstance(String str, String str2, int i) {
        VectorSearchMessagesListFragment vectorSearchMessagesListFragment = new VectorSearchMessagesListFragment();
        vectorSearchMessagesListFragment.setArguments(getArguments(str, str2, i));
        return vectorSearchMessagesListFragment;
    }

    public VectorMessagesAdapter createMessagesAdapter() {
        VectorSearchMessagesListAdapter vectorSearchMessagesListAdapter = new VectorSearchMessagesListAdapter(this.mSession, getActivity(), this.mRoomId == null, getMXMediaCache());
        if (this.mMediaScanManager != null) {
            vectorSearchMessagesListAdapter.setMediaScanManager(this.mMediaScanManager);
        }
        return vectorSearchMessagesListAdapter;
    }

    public void onPause() {
        super.onPause();
        if (this.mSession.isAlive()) {
            cancelSearch();
            if (this.mIsMediaSearch) {
                this.mSession.cancelSearchMediaByText();
            } else {
                this.mSession.cancelSearchMessagesByText();
            }
            this.mSearchingPattern = null;
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mProgressView = getActivity().findViewById(R.id.search_load_oldest_progress);
    }

    public void showLoadingBackProgress() {
        View view = this.mProgressView;
        if (view != null) {
            view.setVisibility(0);
        }
    }

    public void hideLoadingBackProgress() {
        View view = this.mProgressView;
        if (view != null) {
            view.setVisibility(8);
        }
    }

    public void scrollToBottom() {
        if (((VectorMessagesAdapter) this.mAdapter).getCount() != 0) {
            this.mMessageListView.setSelection(((VectorMessagesAdapter) this.mAdapter).getCount() - 1);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean allowSearch(String str) {
        return !TextUtils.isEmpty(str);
    }

    public void onInitialMessagesLoaded() {
        if (!allowSearch(this.mPattern)) {
            Log.e(LOG_TAG, "## onInitialMessagesLoaded() : history filling is cancelled");
        } else {
            super.onInitialMessagesLoaded();
        }
    }

    public void searchPattern(final String str, OnSearchResultListener onSearchResultListener) {
        if (onSearchResultListener != null) {
            this.mSearchListeners.add(onSearchResultListener);
        }
        if (this.mMessageListView != null) {
            if (TextUtils.equals(this.mSearchingPattern, str)) {
                this.mSearchListeners.add(onSearchResultListener);
                return;
            }
            if (!allowSearch(str)) {
                this.mPattern = null;
                this.mMessageListView.setVisibility(8);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Iterator it = VectorSearchMessagesListFragment.this.mSearchListeners.iterator();
                        while (it.hasNext()) {
                            try {
                                ((OnSearchResultListener) it.next()).onSearchSucceed(0);
                            } catch (Exception e) {
                                String access$000 = VectorSearchMessagesListFragment.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## searchPattern() : failed ");
                                sb.append(e.getMessage());
                                Log.e(access$000, sb.toString(), e);
                            }
                        }
                        VectorSearchMessagesListFragment.this.mSearchListeners.clear();
                        VectorSearchMessagesListFragment.this.mSearchingPattern = null;
                    }
                });
            } else if (TextUtils.equals(this.mPattern, str)) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        Iterator it = VectorSearchMessagesListFragment.this.mSearchListeners.iterator();
                        while (it.hasNext()) {
                            try {
                                ((OnSearchResultListener) it.next()).onSearchSucceed(((VectorMessagesAdapter) VectorSearchMessagesListFragment.this.mAdapter).getCount());
                            } catch (Exception e) {
                                String access$000 = VectorSearchMessagesListFragment.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## searchPattern() : failed ");
                                sb.append(e.getMessage());
                                Log.e(access$000, sb.toString(), e);
                            }
                        }
                        VectorSearchMessagesListFragment.this.mSearchListeners.clear();
                    }
                });
            } else {
                ((VectorMessagesAdapter) this.mAdapter).clear();
                this.mSearchingPattern = str;
                if (this.mAdapter instanceof VectorSearchMessagesListAdapter) {
                    ((VectorSearchMessagesListAdapter) this.mAdapter).setTextToHighlight(str);
                }
                super.searchPattern(str, this.mIsMediaSearch, new OnSearchResultListener() {
                    public void onSearchSucceed(int i) {
                        if (!TextUtils.equals(str, VectorSearchMessagesListFragment.this.mSearchingPattern)) {
                            ((VectorMessagesAdapter) VectorSearchMessagesListFragment.this.mAdapter).clear();
                            VectorSearchMessagesListFragment.this.mMessageListView.setVisibility(8);
                            return;
                        }
                        VectorSearchMessagesListFragment.this.mIsInitialSyncing = false;
                        VectorSearchMessagesListFragment.this.mMessageListView.setOnScrollListener(VectorSearchMessagesListFragment.this.mScrollListener);
                        VectorSearchMessagesListFragment.this.mMessageListView.setAdapter(VectorSearchMessagesListFragment.this.mAdapter);
                        VectorSearchMessagesListFragment.this.mMessageListView.setVisibility(0);
                        VectorSearchMessagesListFragment.this.scrollToBottom();
                        Iterator it = VectorSearchMessagesListFragment.this.mSearchListeners.iterator();
                        while (it.hasNext()) {
                            try {
                                ((OnSearchResultListener) it.next()).onSearchSucceed(i);
                            } catch (Exception e) {
                                String access$000 = VectorSearchMessagesListFragment.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## searchPattern() : failed ");
                                sb.append(e.getMessage());
                                Log.e(access$000, sb.toString(), e);
                            }
                        }
                        VectorSearchMessagesListFragment.this.mSearchListeners.clear();
                        VectorSearchMessagesListFragment.this.mSearchingPattern = null;
                        VectorSearchMessagesListFragment.this.backPaginate(true);
                    }

                    public void onSearchFailed() {
                        VectorSearchMessagesListFragment.this.mMessageListView.setVisibility(8);
                        ((VectorMessagesAdapter) VectorSearchMessagesListFragment.this.mAdapter).clear();
                        Iterator it = VectorSearchMessagesListFragment.this.mSearchListeners.iterator();
                        while (it.hasNext()) {
                            try {
                                ((OnSearchResultListener) it.next()).onSearchFailed();
                            } catch (Exception e) {
                                String access$000 = VectorSearchMessagesListFragment.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## searchPattern() : onSearchFailed failed ");
                                sb.append(e.getMessage());
                                Log.e(access$000, sb.toString(), e);
                            }
                        }
                        VectorSearchMessagesListFragment.this.mSearchListeners.clear();
                        VectorSearchMessagesListFragment.this.mSearchingPattern = null;
                    }
                });
            }
        }
    }

    public boolean onRowLongClick(int i) {
        onContentClick(i);
        return true;
    }

    public void onContentClick(int i) {
        Event event = ((MessageRow) ((VectorMessagesAdapter) this.mAdapter).getItem(i)).getEvent();
        Intent intent = new Intent(getActivity(), VectorRoomActivity.class);
        intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getMyUserId());
        intent.putExtra("EXTRA_ROOM_ID", event.roomId);
        intent.putExtra(VectorRoomActivity.EXTRA_EVENT_ID, event.eventId);
        getActivity().startActivity(intent);
    }
}
