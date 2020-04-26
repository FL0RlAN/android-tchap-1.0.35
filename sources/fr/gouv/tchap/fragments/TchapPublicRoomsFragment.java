package fr.gouv.tchap.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter.FilterListener;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import butterknife.BindView;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.adapters.TchapPublicRoomAdapter;
import fr.gouv.tchap.adapters.TchapPublicRoomAdapter.OnSelectItemListener;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.Matrix;
import im.vector.PublicRoomsManager;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.VectorAppCompatActivity;
import im.vector.activity.VectorRoomActivity;
import im.vector.adapters.AdapterSection;
import im.vector.fragments.VectorBaseFragment;
import im.vector.view.EmptyViewItemDecoration;
import im.vector.view.SectionView;
import im.vector.view.SimpleDividerItemDecoration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomPreviewData;
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom;

public class TchapPublicRoomsFragment extends VectorBaseFragment {
    private static final String CURRENT_FILTER = "CURRENT_FILTER";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = TchapPublicRoomsFragment.class.getSimpleName();
    protected VectorAppCompatActivity mActivity;
    /* access modifiers changed from: private */
    public TchapPublicRoomAdapter mAdapter;
    protected String mCurrentFilter;
    /* access modifiers changed from: private */
    public List<String> mCurrentHosts = null;
    /* access modifiers changed from: private */
    public boolean mMorePublicRooms = false;
    private final OnScrollListener mPublicRoomScrollListener = new OnScrollListener() {
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            super.onScrolled(recyclerView, i, i2);
            int findLastCompletelyVisibleItemPosition = ((LinearLayoutManager) TchapPublicRoomsFragment.this.mRecycler.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            SectionView sectionViewForSectionIndex = TchapPublicRoomsFragment.this.mAdapter.getSectionViewForSectionIndex(TchapPublicRoomsFragment.this.mAdapter.getSectionsCount() - 1);
            AdapterSection section = sectionViewForSectionIndex != null ? sectionViewForSectionIndex.getSection() : null;
            if (section != null) {
                for (int i3 = 0; i3 < TchapPublicRoomsFragment.this.mAdapter.getSectionsCount() - 1; i3++) {
                    SectionView sectionViewForSectionIndex2 = TchapPublicRoomsFragment.this.mAdapter.getSectionViewForSectionIndex(i3);
                    if (!(sectionViewForSectionIndex2 == null || sectionViewForSectionIndex2.getSection() == null)) {
                        findLastCompletelyVisibleItemPosition -= sectionViewForSectionIndex2.getSection().getNbItems();
                        if (findLastCompletelyVisibleItemPosition <= 0) {
                            return;
                        }
                    }
                }
                if (section.getNbItems() - findLastCompletelyVisibleItemPosition < 10) {
                    TchapPublicRoomsFragment.this.forwardPaginate();
                }
            }
        }
    };
    private List<PublicRoomsManager> mPublicRoomsManagers = null;
    @BindView(2131296894)
    RecyclerView mRecycler;
    private ArrayAdapter<CharSequence> mRoomDirectoryAdapter;
    private final List<Room> mRooms = new ArrayList();
    protected MXSession mSession;

    public static TchapPublicRoomsFragment newInstance() {
        return new TchapPublicRoomsFragment();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_rooms, viewGroup, false);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() instanceof VectorAppCompatActivity) {
            this.mActivity = (VectorAppCompatActivity) getActivity();
        }
        this.mSession = Matrix.getInstance(getActivity()).getDefaultSession();
        if (bundle != null) {
            String str = CURRENT_FILTER;
            if (bundle.containsKey(str)) {
                this.mCurrentFilter = bundle.getString(str);
            }
        }
        String homeServerNameFromMXIdentifier = DinsicUtils.getHomeServerNameFromMXIdentifier(this.mSession.getMyUserId());
        List asList = Arrays.asList(getResources().getStringArray(R.array.room_directory_servers));
        this.mCurrentHosts = new ArrayList();
        boolean z = false;
        for (int i = 0; i < asList.size(); i++) {
            if (((String) asList.get(i)).compareTo(homeServerNameFromMXIdentifier) == 0) {
                this.mCurrentHosts.add(null);
                z = true;
            } else {
                this.mCurrentHosts.add(asList.get(i));
            }
        }
        if (!z) {
            this.mCurrentHosts.add(null);
        }
        initViews();
        this.mAdapter.onFilterDone(this.mCurrentFilter);
        initPublicRooms(false);
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

    public void applyFilter(final String str) {
        if (TextUtils.isEmpty(str)) {
            if (this.mCurrentFilter != null) {
                this.mAdapter.getFilter().filter("", new FilterListener() {
                    public void onFilterComplete(int i) {
                        String access$000 = TchapPublicRoomsFragment.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onResetFilter ");
                        sb.append(i);
                        Log.i(access$000, sb.toString());
                        TchapPublicRoomsFragment.this.initPublicRooms(false);
                    }
                });
                this.mCurrentFilter = null;
            }
        } else if (!TextUtils.equals(this.mCurrentFilter, str)) {
            this.mAdapter.getFilter().filter(str, new FilterListener() {
                public void onFilterComplete(int i) {
                    String access$000 = TchapPublicRoomsFragment.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onFilterComplete ");
                    sb.append(i);
                    Log.i(access$000, sb.toString());
                    TchapPublicRoomsFragment tchapPublicRoomsFragment = TchapPublicRoomsFragment.this;
                    tchapPublicRoomsFragment.mCurrentFilter = str;
                    tchapPublicRoomsFragment.initPublicRooms(false);
                }
            });
        }
    }

    private void initViews() {
        int dimension = (int) getResources().getDimension(R.dimen.item_decoration_left_margin);
        this.mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), 1, false));
        this.mRecycler.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), 1, dimension));
        RecyclerView recyclerView = this.mRecycler;
        EmptyViewItemDecoration emptyViewItemDecoration = new EmptyViewItemDecoration(getActivity(), 1, 40, 16, 14);
        recyclerView.addItemDecoration(emptyViewItemDecoration);
        this.mAdapter = new TchapPublicRoomAdapter(getActivity(), new OnSelectItemListener() {
            public void onSelectItem(Room room, int i) {
            }

            public void onSelectItem(PublicRoom publicRoom) {
                TchapPublicRoomsFragment.this.onPublicRoomSelected(publicRoom);
            }
        });
        this.mRecycler.setAdapter(this.mAdapter);
    }

    /* access modifiers changed from: private */
    public void onPublicRoomSelected(final PublicRoom publicRoom) {
        if (publicRoom.roomId != null) {
            final RoomPreviewData roomPreviewData = new RoomPreviewData(this.mSession, publicRoom.roomId, null, publicRoom.canonicalAlias, null);
            Room room = this.mSession.getDataHandler().getRoom(publicRoom.roomId, false);
            if (room != null && room.isInvited()) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("manageRoom : the user is invited -> display the preview ");
                sb.append(getActivity());
                Log.d(str, sb.toString());
                CommonActivityUtils.previewRoom(getActivity(), roomPreviewData);
            } else if (room == null || !room.isJoined()) {
                Log.d(LOG_TAG, "manageRoom : display the preview");
                VectorAppCompatActivity vectorAppCompatActivity = this.mActivity;
                if (vectorAppCompatActivity != null) {
                    vectorAppCompatActivity.showWaitingView();
                }
                roomPreviewData.fetchPreviewData(new ApiCallback<Void>() {
                    private void onDone() {
                        if (TchapPublicRoomsFragment.this.mActivity != null) {
                            TchapPublicRoomsFragment.this.mActivity.hideWaitingView();
                        }
                        CommonActivityUtils.previewRoom(TchapPublicRoomsFragment.this.getActivity(), roomPreviewData);
                    }

                    public void onSuccess(Void voidR) {
                        onDone();
                    }

                    private void onError() {
                        roomPreviewData.setPublicRoom(publicRoom);
                        roomPreviewData.setRoomName(publicRoom.name);
                        onDone();
                    }

                    public void onNetworkError(Exception exc) {
                        onError();
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        if (!MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode) || !TchapPublicRoomsFragment.this.isAdded() || TchapPublicRoomsFragment.this.mActivity == null) {
                            onError();
                        } else {
                            TchapPublicRoomsFragment.this.mActivity.getConsentNotGivenHelper().displayDialog(matrixError);
                        }
                    }

                    public void onUnexpectedError(Exception exc) {
                        onError();
                    }
                });
            } else {
                Log.d(LOG_TAG, "manageRoom : the user joined the room -> open the room");
                HashMap hashMap = new HashMap();
                hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getMyUserId());
                hashMap.put("EXTRA_ROOM_ID", publicRoom.roomId);
                if (!TextUtils.isEmpty(publicRoom.name)) {
                    hashMap.put(VectorRoomActivity.EXTRA_DEFAULT_NAME, publicRoom.name);
                }
                if (!TextUtils.isEmpty(publicRoom.topic)) {
                    hashMap.put(VectorRoomActivity.EXTRA_DEFAULT_TOPIC, publicRoom.topic);
                }
                CommonActivityUtils.goToRoomPage(getActivity(), this.mSession, hashMap);
            }
        }
    }

    /* access modifiers changed from: private */
    public void initPublicRooms(boolean z) {
        this.mAdapter.setNoMorePublicRooms(false);
        this.mAdapter.setPublicRooms(null);
        this.mMorePublicRooms = false;
        VectorAppCompatActivity vectorAppCompatActivity = this.mActivity;
        if (vectorAppCompatActivity != null) {
            vectorAppCompatActivity.showWaitingView();
        }
        initPublicRoomsCascade(z, 0);
    }

    /* access modifiers changed from: private */
    public void initPublicRoomsCascade(final boolean z, final int i) {
        if (this.mPublicRoomsManagers == null) {
            this.mPublicRoomsManagers = new ArrayList();
            initPublicRoomsManagers();
        }
        ((PublicRoomsManager) this.mPublicRoomsManagers.get(i)).startPublicRoomsSearch((String) this.mCurrentHosts.get(i), null, false, this.mCurrentFilter, new ApiCallback<List<PublicRoom>>() {
            public void onSuccess(List<PublicRoom> list) {
                if (TchapPublicRoomsFragment.this.getActivity() != null) {
                    TchapPublicRoomsFragment tchapPublicRoomsFragment = TchapPublicRoomsFragment.this;
                    tchapPublicRoomsFragment.mMorePublicRooms = tchapPublicRoomsFragment.mMorePublicRooms || list.size() >= -1;
                    TchapPublicRoomsFragment.this.mAdapter.addPublicRooms(list);
                    if (i == TchapPublicRoomsFragment.this.mCurrentHosts.size() - 1) {
                        TchapPublicRoomsFragment.this.mAdapter.setNoMorePublicRooms(!TchapPublicRoomsFragment.this.mMorePublicRooms);
                        TchapPublicRoomsFragment.this.addPublicRoomsListener();
                        if (z) {
                            TchapPublicRoomsFragment.this.mRecycler.post(new Runnable() {
                                public void run() {
                                    SectionView sectionViewForSectionIndex = TchapPublicRoomsFragment.this.mAdapter.getSectionViewForSectionIndex(TchapPublicRoomsFragment.this.mAdapter.getSectionsCount() - 1);
                                    if (sectionViewForSectionIndex != null && !sectionViewForSectionIndex.isStickyHeader()) {
                                        sectionViewForSectionIndex.callOnClick();
                                    }
                                }
                            });
                        }
                        if (TchapPublicRoomsFragment.this.mActivity != null) {
                            TchapPublicRoomsFragment.this.mActivity.hideWaitingView();
                            return;
                        }
                        return;
                    }
                    TchapPublicRoomsFragment.this.initPublicRoomsCascade(z, i + 1);
                }
            }

            private void onError(String str) {
                if (TchapPublicRoomsFragment.this.getActivity() != null) {
                    String access$000 = TchapPublicRoomsFragment.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## startPublicRoomsSearch() failed ");
                    sb.append(str);
                    Log.e(access$000, sb.toString());
                    if (i == TchapPublicRoomsFragment.this.mCurrentHosts.size() - 1) {
                        TchapPublicRoomsFragment.this.mAdapter.setNoMorePublicRooms(!TchapPublicRoomsFragment.this.mMorePublicRooms);
                        TchapPublicRoomsFragment.this.addPublicRoomsListener();
                        if (TchapPublicRoomsFragment.this.mActivity != null) {
                            TchapPublicRoomsFragment.this.mActivity.hideWaitingView();
                            return;
                        }
                        return;
                    }
                    TchapPublicRoomsFragment.this.initPublicRoomsCascade(z, i + 1);
                }
            }

            public void onNetworkError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                if (!MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode) || !TchapPublicRoomsFragment.this.isAdded() || TchapPublicRoomsFragment.this.mActivity == null) {
                    onError(matrixError.getLocalizedMessage());
                    return;
                }
                TchapPublicRoomsFragment.this.mActivity.hideWaitingView();
                TchapPublicRoomsFragment.this.mActivity.getConsentNotGivenHelper().displayDialog(matrixError);
            }

            public void onUnexpectedError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }
        });
    }

    private void initPublicRoomsManagers() {
        for (int i = 0; i < this.mCurrentHosts.size(); i++) {
            PublicRoomsManager publicRoomsManager = new PublicRoomsManager();
            publicRoomsManager.setSession(this.mSession);
            this.mPublicRoomsManagers.add(publicRoomsManager);
        }
    }

    /* access modifiers changed from: private */
    public void forwardPaginate() {
        int i = 0;
        while (i < this.mPublicRoomsManagers.size()) {
            if (!((PublicRoomsManager) this.mPublicRoomsManagers.get(i)).isRequestInProgress()) {
                i++;
            } else {
                return;
            }
        }
        this.mMorePublicRooms = false;
        VectorAppCompatActivity vectorAppCompatActivity = this.mActivity;
        if (vectorAppCompatActivity != null) {
            vectorAppCompatActivity.showWaitingView();
        }
        cascadeForwardPaginate(0);
    }

    /* access modifiers changed from: private */
    public void cascadeForwardPaginate(final int i) {
        if (!((PublicRoomsManager) this.mPublicRoomsManagers.get(i)).forwardPaginate(new ApiCallback<List<PublicRoom>>() {
            public void onSuccess(List<PublicRoom> list) {
                if (TchapPublicRoomsFragment.this.getActivity() != null) {
                    if (PublicRoomsManager.getInstance().hasMoreResults()) {
                        TchapPublicRoomsFragment.this.mMorePublicRooms = true;
                    }
                    TchapPublicRoomsFragment.this.mAdapter.addPublicRooms(list);
                }
                if (i == TchapPublicRoomsFragment.this.mCurrentHosts.size() - 1) {
                    if (TchapPublicRoomsFragment.this.mActivity != null) {
                        TchapPublicRoomsFragment.this.mActivity.hideWaitingView();
                    }
                    if (!TchapPublicRoomsFragment.this.mMorePublicRooms) {
                        TchapPublicRoomsFragment.this.mAdapter.setNoMorePublicRooms(true);
                        TchapPublicRoomsFragment.this.removePublicRoomsListener();
                        return;
                    }
                    return;
                }
                TchapPublicRoomsFragment.this.cascadeForwardPaginate(i + 1);
            }

            private void onError(String str) {
                if (TchapPublicRoomsFragment.this.getActivity() != null) {
                    String access$000 = TchapPublicRoomsFragment.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## forwardPaginate() failed ");
                    sb.append(str);
                    Log.e(access$000, sb.toString());
                    Toast.makeText(TchapPublicRoomsFragment.this.getActivity(), str, 0).show();
                }
                if (i != TchapPublicRoomsFragment.this.mCurrentHosts.size()) {
                    TchapPublicRoomsFragment.this.cascadeForwardPaginate(i + 1);
                } else if (TchapPublicRoomsFragment.this.mActivity != null) {
                    TchapPublicRoomsFragment.this.mActivity.hideWaitingView();
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
            VectorAppCompatActivity vectorAppCompatActivity = this.mActivity;
            if (vectorAppCompatActivity != null) {
                vectorAppCompatActivity.hideWaitingView();
            }
        }
    }

    /* access modifiers changed from: private */
    public void addPublicRoomsListener() {
        this.mRecycler.addOnScrollListener(this.mPublicRoomScrollListener);
    }

    /* access modifiers changed from: private */
    public void removePublicRoomsListener() {
        this.mRecycler.removeOnScrollListener(null);
    }
}
