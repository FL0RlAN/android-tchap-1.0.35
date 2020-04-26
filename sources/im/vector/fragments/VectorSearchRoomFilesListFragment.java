package im.vector.fragments;

import android.text.TextUtils;
import android.widget.Toast;
import im.vector.adapters.VectorMessagesAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.matrix.androidsdk.adapters.MessageRow;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.fragments.MatrixMessageListFragment.OnSearchResultListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.TokensChunkEvents;
import org.matrix.androidsdk.rest.model.message.Message;

public class VectorSearchRoomFilesListFragment extends VectorSearchRoomsFilesListFragment {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorSearchRoomFilesListFragment.class.getSimpleName();
    private static final int MESSAGES_PAGINATION_LIMIT = 50;
    /* access modifiers changed from: private */
    public boolean mCanPaginateBack = true;
    /* access modifiers changed from: private */
    public final String mTimeLineId;

    /* access modifiers changed from: protected */
    public boolean allowSearch(String str) {
        return true;
    }

    public VectorSearchRoomFilesListFragment() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        sb.append("");
        this.mTimeLineId = sb.toString();
    }

    public static VectorSearchRoomFilesListFragment newInstance(String str, String str2, int i) {
        VectorSearchRoomFilesListFragment vectorSearchRoomFilesListFragment = new VectorSearchRoomFilesListFragment();
        vectorSearchRoomFilesListFragment.setArguments(getArguments(str, str2, i));
        return vectorSearchRoomFilesListFragment;
    }

    public void cancelCatchingRequests() {
        super.cancelCatchingRequests();
        this.mIsBackPaginating = false;
        this.mCanPaginateBack = true;
        if (this.mRoom != null) {
            this.mRoom.cancelRemoteHistoryRequest();
            this.mNextBatch = this.mRoom.getState().getToken();
        }
        if (this.mSession != null) {
            this.mSession.getDataHandler().resetReplayAttackCheckInTimeline(this.mTimeLineId);
        }
    }

    public void onPause() {
        super.onPause();
        cancelCatchingRequests();
    }

    public void startFilesSearch(OnSearchResultListener onSearchResultListener) {
        if (!this.mIsBackPaginating) {
            if (onSearchResultListener != null) {
                this.mSearchListeners.add(onSearchResultListener);
            }
            if (this.mMessageListView != null) {
                this.mIsBackPaginating = true;
                this.mMessageListView.setVisibility(8);
                remoteRoomHistoryRequest(new ArrayList(), new ApiCallback<ArrayList<Event>>() {
                    public void onSuccess(ArrayList<Event> arrayList) {
                        ArrayList arrayList2 = new ArrayList(arrayList.size());
                        RoomState state = VectorSearchRoomFilesListFragment.this.mRoom.getState();
                        Iterator it = arrayList.iterator();
                        while (it.hasNext()) {
                            arrayList2.add(new MessageRow((Event) it.next(), state));
                        }
                        Collections.reverse(arrayList2);
                        ((VectorMessagesAdapter) VectorSearchRoomFilesListFragment.this.mAdapter).clear();
                        ((VectorMessagesAdapter) VectorSearchRoomFilesListFragment.this.mAdapter).addAll(arrayList2);
                        VectorSearchRoomFilesListFragment.this.mMessageListView.setAdapter(VectorSearchRoomFilesListFragment.this.mAdapter);
                        VectorSearchRoomFilesListFragment.this.mMessageListView.setOnScrollListener(VectorSearchRoomFilesListFragment.this.mScrollListener);
                        VectorSearchRoomFilesListFragment.this.scrollToBottom();
                        VectorSearchRoomFilesListFragment.this.mMessageListView.setVisibility(0);
                        Iterator it2 = VectorSearchRoomFilesListFragment.this.mSearchListeners.iterator();
                        while (it2.hasNext()) {
                            try {
                                ((OnSearchResultListener) it2.next()).onSearchSucceed(arrayList2.size());
                            } catch (Exception e) {
                                String access$500 = VectorSearchRoomFilesListFragment.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## remoteRoomHistoryRequest() : onSearchSucceed failed ");
                                sb.append(e.getMessage());
                                Log.e(access$500, sb.toString(), e);
                            }
                        }
                        VectorSearchRoomFilesListFragment.this.mIsBackPaginating = false;
                        VectorSearchRoomFilesListFragment.this.mSearchListeners.clear();
                    }

                    private void onError() {
                        VectorSearchRoomFilesListFragment.this.mMessageListView.setVisibility(8);
                        ((VectorMessagesAdapter) VectorSearchRoomFilesListFragment.this.mAdapter).clear();
                        Iterator it = VectorSearchRoomFilesListFragment.this.mSearchListeners.iterator();
                        while (it.hasNext()) {
                            try {
                                ((OnSearchResultListener) it.next()).onSearchFailed();
                            } catch (Exception e) {
                                String access$500 = VectorSearchRoomFilesListFragment.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## remoteRoomHistoryRequest() : onSearchFailed failed ");
                                sb.append(e.getMessage());
                                Log.e(access$500, sb.toString(), e);
                            }
                        }
                        VectorSearchRoomFilesListFragment.this.mIsBackPaginating = false;
                        VectorSearchRoomFilesListFragment.this.mSearchListeners.clear();
                    }

                    public void onNetworkError(Exception exc) {
                        Toast.makeText(VectorSearchRoomFilesListFragment.this.getActivity(), exc.getLocalizedMessage(), 1).show();
                        onError();
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        Toast.makeText(VectorSearchRoomFilesListFragment.this.getActivity(), matrixError.getLocalizedMessage(), 1).show();
                        onError();
                    }

                    public void onUnexpectedError(Exception exc) {
                        Toast.makeText(VectorSearchRoomFilesListFragment.this.getActivity(), exc.getLocalizedMessage(), 1).show();
                        onError();
                    }
                });
            }
        }
    }

    public void backPaginate(boolean z) {
        if (!this.mIsBackPaginating && this.mCanPaginateBack) {
            this.mIsBackPaginating = true;
            final int count = ((VectorMessagesAdapter) this.mAdapter).getCount();
            if (((VectorMessagesAdapter) this.mAdapter).getCount() != 0) {
                showLoadingBackProgress();
            }
            remoteRoomHistoryRequest(new ArrayList(), new ApiCallback<ArrayList<Event>>() {
                public void onSuccess(final ArrayList<Event> arrayList) {
                    VectorSearchRoomFilesListFragment.this.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            if (arrayList.size() != 0) {
                                ((VectorMessagesAdapter) VectorSearchRoomFilesListFragment.this.mAdapter).setNotifyOnChange(false);
                                Iterator it = arrayList.iterator();
                                while (it.hasNext()) {
                                    ((VectorMessagesAdapter) VectorSearchRoomFilesListFragment.this.mAdapter).insert(new MessageRow((Event) it.next(), VectorSearchRoomFilesListFragment.this.mRoom.getState()), 0);
                                }
                                VectorSearchRoomFilesListFragment.this.mUiHandler.post(new Runnable() {
                                    public void run() {
                                        ((VectorMessagesAdapter) VectorSearchRoomFilesListFragment.this.mAdapter).notifyDataSetChanged();
                                        VectorSearchRoomFilesListFragment.this.mMessageListView.setSelection(VectorSearchRoomFilesListFragment.this.mMessageListView.getFirstVisiblePosition() + (((VectorMessagesAdapter) VectorSearchRoomFilesListFragment.this.mAdapter).getCount() - count));
                                        VectorSearchRoomFilesListFragment.this.mIsBackPaginating = false;
                                        VectorSearchRoomFilesListFragment.this.setMessageListViewScrollListener();
                                        Iterator it = VectorSearchRoomFilesListFragment.this.mSearchListeners.iterator();
                                        while (it.hasNext()) {
                                            try {
                                                ((OnSearchResultListener) it.next()).onSearchSucceed(arrayList.size());
                                            } catch (Exception e) {
                                                String access$500 = VectorSearchRoomFilesListFragment.LOG_TAG;
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("## backPaginate() : onSearchSucceed failed ");
                                                sb.append(e.getMessage());
                                                Log.e(access$500, sb.toString(), e);
                                            }
                                        }
                                        VectorSearchRoomFilesListFragment.this.mSearchListeners.clear();
                                        VectorSearchRoomFilesListFragment.this.mMessageListView.post(new Runnable() {
                                            public void run() {
                                                if (VectorSearchRoomFilesListFragment.this.mMessageListView.getFirstVisiblePosition() < 2) {
                                                    VectorSearchRoomFilesListFragment.this.backPaginate(true);
                                                }
                                            }
                                        });
                                    }
                                });
                            } else {
                                VectorSearchRoomFilesListFragment.this.mIsBackPaginating = false;
                                VectorSearchRoomFilesListFragment.this.mUiHandler.post(new Runnable() {
                                    public void run() {
                                        Iterator it = VectorSearchRoomFilesListFragment.this.mSearchListeners.iterator();
                                        while (it.hasNext()) {
                                            try {
                                                ((OnSearchResultListener) it.next()).onSearchSucceed(0);
                                            } catch (Exception e) {
                                                String access$500 = VectorSearchRoomFilesListFragment.LOG_TAG;
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("## backPaginate() : onSearchSucceed failed ");
                                                sb.append(e.getMessage());
                                                Log.e(access$500, sb.toString(), e);
                                            }
                                        }
                                    }
                                });
                            }
                            VectorSearchRoomFilesListFragment.this.hideLoadingBackProgress();
                        }
                    });
                }

                private void onError() {
                    VectorSearchRoomFilesListFragment.this.mIsBackPaginating = false;
                    VectorSearchRoomFilesListFragment.this.hideLoadingBackProgress();
                }

                public void onNetworkError(Exception exc) {
                    Toast.makeText(VectorSearchRoomFilesListFragment.this.getActivity(), exc.getLocalizedMessage(), 1).show();
                    onError();
                }

                public void onMatrixError(MatrixError matrixError) {
                    Toast.makeText(VectorSearchRoomFilesListFragment.this.getActivity(), matrixError.getLocalizedMessage(), 1).show();
                    onError();
                }

                public void onUnexpectedError(Exception exc) {
                    Toast.makeText(VectorSearchRoomFilesListFragment.this.getActivity(), exc.getLocalizedMessage(), 1).show();
                    onError();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void appendEvents(ArrayList<Event> arrayList, List<Event> list) {
        ArrayList arrayList2 = new ArrayList(list.size());
        for (Event event : list) {
            if (Event.EVENT_TYPE_MESSAGE.equals(event.getType())) {
                Message message = JsonUtils.toMessage(event.getContent());
                if (!Message.MSGTYPE_FILE.equals(message.msgtype)) {
                    if (!Message.MSGTYPE_IMAGE.equals(message.msgtype)) {
                        if (!Message.MSGTYPE_VIDEO.equals(message.msgtype)) {
                            if (!Message.MSGTYPE_AUDIO.equals(message.msgtype)) {
                            }
                        }
                    }
                }
                arrayList2.add(event);
            }
        }
        arrayList.addAll(arrayList2);
    }

    /* access modifiers changed from: private */
    public void remoteRoomHistoryRequest(final ArrayList<Event> arrayList, final ApiCallback<ArrayList<Event>> apiCallback) {
        this.mRoom.requestServerRoomHistory(this.mNextBatch, 50, new ApiCallback<TokensChunkEvents>() {
            public void onSuccess(TokensChunkEvents tokensChunkEvents) {
                if (VectorSearchRoomFilesListFragment.this.mNextBatch != null && !TextUtils.equals(tokensChunkEvents.start, VectorSearchRoomFilesListFragment.this.mNextBatch)) {
                    return;
                }
                if (TextUtils.equals(tokensChunkEvents.start, tokensChunkEvents.end)) {
                    VectorSearchRoomFilesListFragment.this.mCanPaginateBack = false;
                    apiCallback.onSuccess(arrayList);
                    return;
                }
                if (VectorSearchRoomFilesListFragment.this.mRoom.isEncrypted()) {
                    for (Event decryptEvent : tokensChunkEvents.chunk) {
                        VectorSearchRoomFilesListFragment.this.mSession.getDataHandler().decryptEvent(decryptEvent, VectorSearchRoomFilesListFragment.this.mTimeLineId);
                    }
                }
                VectorSearchRoomFilesListFragment.this.appendEvents(arrayList, tokensChunkEvents.chunk);
                VectorSearchRoomFilesListFragment.this.mNextBatch = tokensChunkEvents.end;
                if (arrayList.size() >= 10) {
                    apiCallback.onSuccess(arrayList);
                } else {
                    VectorSearchRoomFilesListFragment.this.remoteRoomHistoryRequest(arrayList, apiCallback);
                }
            }

            private void onError() {
                apiCallback.onSuccess(arrayList);
            }

            public void onNetworkError(Exception exc) {
                Toast.makeText(VectorSearchRoomFilesListFragment.this.getActivity(), exc.getLocalizedMessage(), 1).show();
                onError();
            }

            public void onMatrixError(MatrixError matrixError) {
                Toast.makeText(VectorSearchRoomFilesListFragment.this.getActivity(), matrixError.getLocalizedMessage(), 1).show();
                onError();
            }

            public void onUnexpectedError(Exception exc) {
                Toast.makeText(VectorSearchRoomFilesListFragment.this.getActivity(), exc.getLocalizedMessage(), 1).show();
                onError();
            }
        });
    }
}
