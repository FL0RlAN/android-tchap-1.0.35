package fr.gouv.tchap.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter.FilterListener;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.adapters.TchapRoomAdapter;
import fr.gouv.tchap.adapters.TchapRoomAdapter.OnSelectItemListener;
import im.vector.fragments.AbsHomeFragment;
import im.vector.fragments.AbsHomeFragment.OnFilterListener;
import im.vector.fragments.AbsHomeFragment.OnRoomChangedListener;
import im.vector.util.HomeRoomsViewModel.Result;
import im.vector.view.EmptyViewItemDecoration;
import im.vector.view.SimpleDividerItemDecoration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.rest.model.RoomMember;

public class TchapRoomsFragment extends AbsHomeFragment implements OnRoomChangedListener {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = TchapRoomsFragment.class.getSimpleName();
    private TchapRoomAdapter mAdapter;
    @BindView(2131296894)
    RecyclerView mRecycler;
    private final List<Room> mRooms = new ArrayList();

    public void onRoomForgot(String str) {
    }

    public void onRoomLeft(String str) {
    }

    public void onToggleDirectChat(String str, boolean z) {
    }

    public static TchapRoomsFragment newInstance() {
        return new TchapRoomsFragment();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_rooms, viewGroup, false);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mPrimaryColor = ContextCompat.getColor(getActivity(), R.color.tab_rooms);
        this.mSecondaryColor = ContextCompat.getColor(getActivity(), R.color.tab_rooms_secondary);
        initViews();
        this.mOnRoomChangedListener = this;
        this.mCurrentFilter = this.mActivity.getSearchQuery();
        this.mAdapter.onFilterDone(this.mCurrentFilter);
    }

    public void onResume() {
        super.onResume();
        if (this.mActivity != null) {
            this.mAdapter.setInvitation(this.mActivity.getRoomInvitations());
        }
        this.mRecycler.addOnScrollListener(this.mScrollListener);
    }

    public void onPause() {
        super.onPause();
        this.mRecycler.removeOnScrollListener(this.mScrollListener);
    }

    /* access modifiers changed from: protected */
    public List<Room> getRooms() {
        return new ArrayList(this.mRooms);
    }

    /* access modifiers changed from: protected */
    public void onFilter(String str, final OnFilterListener onFilterListener) {
        this.mAdapter.getFilter().filter(str, new FilterListener() {
            public void onFilterComplete(int i) {
                String access$000 = TchapRoomsFragment.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onFilterComplete ");
                sb.append(i);
                Log.i(access$000, sb.toString());
                OnFilterListener onFilterListener = onFilterListener;
                if (onFilterListener != null) {
                    onFilterListener.onFilterDone(i);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResetFilter() {
        this.mAdapter.getFilter().filter("", new FilterListener() {
            public void onFilterComplete(int i) {
                String access$000 = TchapRoomsFragment.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onResetFilter ");
                sb.append(i);
                Log.i(access$000, sb.toString());
            }
        });
    }

    public void onRoomResultUpdated(Result result) {
        if (isResumed()) {
            refreshRooms(result.getJoinedRooms());
            this.mAdapter.setInvitation(this.mActivity.getRoomInvitations());
        }
    }

    private void initViews() {
        int dimension = (int) getResources().getDimension(R.dimen.item_decoration_left_margin);
        this.mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), 1, false));
        this.mRecycler.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), 1, dimension));
        RecyclerView recyclerView = this.mRecycler;
        EmptyViewItemDecoration emptyViewItemDecoration = new EmptyViewItemDecoration(getActivity(), 1, 40, 16, 14);
        recyclerView.addItemDecoration(emptyViewItemDecoration);
        this.mAdapter = new TchapRoomAdapter(getActivity(), new OnSelectItemListener() {
            public void onSelectItem(Room room, int i) {
                TchapRoomsFragment.this.openRoom(room);
            }
        }, this, this);
        this.mRecycler.setAdapter(this.mAdapter);
    }

    private void refreshRooms(List<Room> list) {
        this.mRooms.clear();
        for (Room room : list) {
            if (room.isDirect() && room.getState().thirdPartyInvites().size() != 0) {
                Iterator it = room.getState().getDisplayableLoadedMembers().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    RoomMember roomMember = (RoomMember) it.next();
                    if (!roomMember.getUserId().equals(this.mSession.getMyUserId()) && roomMember.getThirdPartyInviteToken() == null) {
                        this.mRooms.add(room);
                        break;
                    }
                }
            } else {
                this.mRooms.add(room);
            }
        }
        this.mAdapter.setRooms(this.mRooms);
    }
}
