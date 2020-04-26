package im.vector.fragments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.VectorHomeActivity;
import im.vector.adapters.AbsAdapter.MoreRoomActionListener;
import im.vector.adapters.AbsAdapter.RoomInvitationListener;
import im.vector.util.HomeRoomsViewModel.Result;
import im.vector.util.RoomUtils;
import im.vector.util.RoomUtils.MoreActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.BingRulesManager.RoomNotificationState;
import org.matrix.androidsdk.core.BingRulesManager.onBingRuleUpdateListener;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomTag;

public abstract class AbsHomeFragment extends VectorBaseFragment implements RoomInvitationListener, MoreRoomActionListener, MoreActionListener {
    private static final String CURRENT_FILTER = "CURRENT_FILTER";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = AbsHomeFragment.class.getSimpleName();
    /* access modifiers changed from: protected */
    public VectorHomeActivity mActivity;
    /* access modifiers changed from: protected */
    public String mCurrentFilter;
    protected OnRoomChangedListener mOnRoomChangedListener;
    protected int mPrimaryColor = -1;
    protected final OnScrollListener mScrollListener = new OnScrollListener() {
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            if (AbsHomeFragment.this.mActivity != null && i2 != 0) {
                AbsHomeFragment.this.mActivity.hideFloatingActionButton(AbsHomeFragment.this.getTag());
            }
        }
    };
    protected int mSecondaryColor = -1;
    /* access modifiers changed from: protected */
    public MXSession mSession;

    public interface OnFilterListener {
        void onFilterDone(int i);
    }

    public interface OnRoomChangedListener {
        void onRoomForgot(String str);

        void onRoomLeft(String str);

        void onToggleDirectChat(String str, boolean z);
    }

    /* access modifiers changed from: protected */
    public abstract List<Room> getRooms();

    public boolean onFabClick() {
        return false;
    }

    /* access modifiers changed from: protected */
    public abstract void onFilter(String str, OnFilterListener onFilterListener);

    /* access modifiers changed from: protected */
    public abstract void onResetFilter();

    public void onRoomResultUpdated(Result result) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() instanceof VectorHomeActivity) {
            this.mActivity = (VectorHomeActivity) getActivity();
        }
        this.mSession = Matrix.getInstance(getActivity()).getDefaultSession();
        if (bundle != null) {
            String str = CURRENT_FILTER;
            if (bundle.containsKey(str)) {
                this.mCurrentFilter = bundle.getString(str);
            }
        }
    }

    public void onResume() {
        super.onResume();
        VectorHomeActivity vectorHomeActivity = this.mActivity;
        if (vectorHomeActivity != null) {
            int i = this.mPrimaryColor;
            if (i != -1) {
                int i2 = this.mSecondaryColor;
                if (i2 == -1) {
                    i2 = i;
                }
                vectorHomeActivity.updateTabStyle(i, i2);
            }
            onRoomResultUpdated(this.mActivity.getRoomsViewModel().getResult());
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.ic_action_mark_all_as_read) {
            return false;
        }
        Log.d(LOG_TAG, "onOptionsItemSelected mark all as read");
        onMarkAllAsRead();
        return true;
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(CURRENT_FILTER, this.mCurrentFilter);
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.mCurrentFilter = null;
    }

    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }

    public void onJoinRoom(MXSession mXSession, String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onJoinRoom ");
        sb.append(str);
        Log.i(str2, sb.toString());
        this.mActivity.onJoinRoom(mXSession, str);
    }

    public void onRejectInvitation(MXSession mXSession, String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onRejectInvitation ");
        sb.append(str);
        Log.i(str2, sb.toString());
        this.mActivity.onRejectInvitation(str, null);
    }

    public void onMoreActionClick(View view, Room room) {
        Set keys = room.getAccountData().getKeys();
        RoomUtils.displayPopupMenu(this.mActivity, this.mSession, room, view, keys != null && keys.contains(RoomTag.ROOM_TAG_FAVOURITE), keys != null && keys.contains(RoomTag.ROOM_TAG_LOW_PRIORITY), this);
    }

    public void onTchapMoreActionClick(View view, Room room, View view2) {
        Set keys = room.getAccountData().getKeys();
        RoomUtils.displayTchapPopupMenu(this.mActivity, this.mSession, room, view, view2, keys != null && keys.contains(RoomTag.ROOM_TAG_FAVOURITE), keys != null && keys.contains(RoomTag.ROOM_TAG_LOW_PRIORITY), this);
    }

    public void onUpdateRoomNotificationsState(MXSession mXSession, String str, RoomNotificationState roomNotificationState) {
        this.mActivity.showWaitingView();
        mXSession.getDataHandler().getBingRulesManager().updateRoomNotificationState(str, roomNotificationState, new onBingRuleUpdateListener() {
            public void onBingRuleUpdateSuccess() {
                AbsHomeFragment.this.onRequestDone(null);
            }

            public void onBingRuleUpdateFailure(String str) {
                AbsHomeFragment.this.onRequestDone(str);
            }
        });
    }

    public void onToggleDirectChat(MXSession mXSession, final String str) {
        this.mActivity.showWaitingView();
        RoomUtils.toggleDirectChat(mXSession, str, new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                AbsHomeFragment.this.onRequestDone(null);
                if (AbsHomeFragment.this.mOnRoomChangedListener != null) {
                    AbsHomeFragment.this.mOnRoomChangedListener.onToggleDirectChat(str, RoomUtils.isDirectChat(AbsHomeFragment.this.mSession, str));
                }
            }

            public void onNetworkError(Exception exc) {
                AbsHomeFragment.this.onRequestDone(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                AbsHomeFragment.this.onRequestDone(matrixError.getLocalizedMessage());
            }

            public void onUnexpectedError(Exception exc) {
                AbsHomeFragment.this.onRequestDone(exc.getLocalizedMessage());
            }
        });
    }

    public void moveToFavorites(MXSession mXSession, String str) {
        updateTag(str, null, RoomTag.ROOM_TAG_FAVOURITE);
    }

    public void moveToConversations(MXSession mXSession, String str) {
        updateTag(str, null, null);
    }

    public void moveToLowPriority(MXSession mXSession, String str) {
        updateTag(str, null, RoomTag.ROOM_TAG_LOW_PRIORITY);
    }

    public void onLeaveRoom(MXSession mXSession, final String str) {
        Room room = this.mSession.getDataHandler().getRoom(str);
        if (room != null) {
            RoomUtils.showLeaveRoomDialog(getActivity(), mXSession, room, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (AbsHomeFragment.this.mActivity != null && !AbsHomeFragment.this.mActivity.isFinishing()) {
                        AbsHomeFragment.this.mActivity.onRejectInvitation(str, new SimpleApiCallback<Void>() {
                            public void onSuccess(Void voidR) {
                                if (AbsHomeFragment.this.mOnRoomChangedListener != null) {
                                    AbsHomeFragment.this.mOnRoomChangedListener.onRoomLeft(str);
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    public void onForgetRoom(MXSession mXSession, final String str) {
        this.mActivity.onForgetRoom(str, new SimpleApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                if (AbsHomeFragment.this.mOnRoomChangedListener != null) {
                    AbsHomeFragment.this.mOnRoomChangedListener.onRoomForgot(str);
                }
            }
        });
    }

    public void addHomeScreenShortcut(MXSession mXSession, String str) {
        RoomUtils.addHomeScreenShortcut(getActivity(), mXSession, str);
    }

    public void applyFilter(final String str) {
        if (TextUtils.isEmpty(str)) {
            if (this.mCurrentFilter != null) {
                onResetFilter();
                this.mCurrentFilter = null;
            }
        } else if (!TextUtils.equals(this.mCurrentFilter, str)) {
            onFilter(str, new OnFilterListener() {
                public void onFilterDone(int i) {
                    AbsHomeFragment.this.mCurrentFilter = str;
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void openRoom(Room room) {
        if (this.mSession.getDataHandler() != null && this.mSession.getDataHandler().getStore() != null) {
            String roomId = (room == null || room.isLeaving()) ? null : room.getRoomId();
            if (roomId != null) {
                if (this.mSession.getDataHandler().getStore().getSummary(roomId) != null) {
                    room.sendReadReceipt();
                }
                CommonActivityUtils.specificUpdateBadgeUnreadCount(this.mSession, getContext());
                HashMap hashMap = new HashMap();
                hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getMyUserId());
                hashMap.put("EXTRA_ROOM_ID", roomId);
                CommonActivityUtils.goToRoomPage(getActivity(), this.mSession, hashMap);
            }
        }
    }

    private void updateTag(String str, Double d, String str2) {
        this.mActivity.showWaitingView();
        RoomUtils.updateRoomTag(this.mSession, str, d, str2, new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                AbsHomeFragment.this.onRequestDone(null);
            }

            public void onNetworkError(Exception exc) {
                AbsHomeFragment.this.onRequestDone(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                AbsHomeFragment.this.onRequestDone(matrixError.getLocalizedMessage());
            }

            public void onUnexpectedError(Exception exc) {
                AbsHomeFragment.this.onRequestDone(exc.getLocalizedMessage());
            }
        });
    }

    /* access modifiers changed from: private */
    public void onRequestDone(final String str) {
        VectorHomeActivity vectorHomeActivity = this.mActivity;
        if (vectorHomeActivity != null && !vectorHomeActivity.isFinishing()) {
            this.mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    AbsHomeFragment.this.mActivity.hideWaitingView();
                    if (!TextUtils.isEmpty(str)) {
                        Toast.makeText(AbsHomeFragment.this.mActivity, str, 0).show();
                    }
                }
            });
        }
    }

    private void onMarkAllAsRead() {
        this.mActivity.showWaitingView();
        this.mSession.markRoomsAsRead((Collection<Room>) getRooms(), (ApiCallback<Void>) new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                if (AbsHomeFragment.this.mActivity != null && !AbsHomeFragment.this.mActivity.isFinishing()) {
                    AbsHomeFragment.this.mActivity.hideWaitingView();
                    AbsHomeFragment.this.mActivity.refreshUnreadBadges();
                    AbsHomeFragment.this.mActivity.onRoomDataUpdated();
                }
            }

            private void onError(String str) {
                String access$100 = AbsHomeFragment.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## markAllMessagesAsRead() failed ");
                sb.append(str);
                Log.e(access$100, sb.toString());
                onSuccess((Void) null);
            }

            public void onNetworkError(Exception exc) {
                onError(exc.getMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                onError(matrixError.getMessage());
            }

            public void onUnexpectedError(Exception exc) {
                onError(exc.getMessage());
            }
        });
    }
}
