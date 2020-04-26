package fr.gouv.tchap.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Filter.FilterListener;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.adapters.TchapContactAdapter;
import fr.gouv.tchap.adapters.TchapContactAdapter.OnSelectItemListener;
import fr.gouv.tchap.util.DinsicUtils;
import fr.gouv.tchap.util.DinumUtilsKt;
import im.vector.activity.VectorAppCompatActivity;
import im.vector.activity.VectorRoomInviteMembersActivity.ActionMode;
import im.vector.activity.VectorRoomInviteMembersActivity.ContactsFilter;
import im.vector.adapters.ParticipantAdapterItem;
import im.vector.contacts.Contact;
import im.vector.contacts.Contact.MXID;
import im.vector.contacts.ContactsManager;
import im.vector.contacts.ContactsManager.ContactsManagerListener;
import im.vector.contacts.PIDsRetriever;
import im.vector.fragments.AbsHomeFragment;
import im.vector.fragments.AbsHomeFragment.OnFilterListener;
import im.vector.fragments.AbsHomeFragment.OnRoomChangedListener;
import im.vector.util.HomeRoomsViewModel.Result;
import im.vector.util.PermissionsToolsKt;
import im.vector.util.VectorUtils;
import im.vector.view.EmptyViewItemDecoration;
import im.vector.view.SimpleDividerItemDecoration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.search.SearchUsersResponse;

public class TchapContactFragment extends AbsHomeFragment implements ContactsManagerListener, OnRoomChangedListener {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = TchapContactFragment.class.getSimpleName();
    private static final int MAX_KNOWN_CONTACTS_FILTER_COUNT = 50;
    /* access modifiers changed from: private */
    public TchapContactAdapter mAdapter;
    private int mContactsSnapshotSession = -1;
    private final List<Room> mDirectChats = new ArrayList();
    private MXEventListener mEventsListener;
    @BindView(2131296710)
    View mInviteContactLayout;
    /* access modifiers changed from: private */
    public final List<ParticipantAdapterItem> mKnownContacts = new ArrayList();
    private final List<ParticipantAdapterItem> mLocalContacts = new ArrayList();
    @BindView(2131296894)
    RecyclerView mRecycler;
    @BindView(2131296687)
    View waitingView;

    public void onContactPresenceUpdate(Contact contact, String str) {
    }

    public static TchapContactFragment newInstance() {
        return new TchapContactFragment();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_people_and_invite, viewGroup, false);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mEventsListener = new MXEventListener() {
            public void onPresenceUpdate(Event event, User user) {
                TchapContactFragment.this.mAdapter.updateKnownContact(user);
            }
        };
        this.mPrimaryColor = ContextCompat.getColor(getActivity(), R.color.tab_people);
        this.mSecondaryColor = ContextCompat.getColor(getActivity(), R.color.tab_people_secondary);
        initViews();
        this.mOnRoomChangedListener = this;
        this.mCurrentFilter = this.mActivity.getSearchQuery();
        this.mAdapter.onFilterDone(this.mCurrentFilter);
        if (!TextUtils.isEmpty(this.mCurrentFilter) && !DinsicUtils.isExternalTchapSession(this.mSession)) {
            startRemoteKnownContactsSearch(true);
        }
        if (DinumUtilsKt.isSecure() || DinsicUtils.isExternalTchapSession(this.mSession)) {
            this.mInviteContactLayout.setVisibility(8);
        } else {
            this.mInviteContactLayout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    TchapContactFragment.this.mActivity.createNewChat(ActionMode.SEND_INVITE, ContactsFilter.ALL_WITHOUT_TCHAP_USERS);
                }
            });
        }
        if (!ContactsManager.getInstance().isContactBookAccessRequested()) {
            PermissionsToolsKt.checkPermissions(8, (Fragment) this, (int) PermissionsToolsKt.PERMISSION_REQUEST_CODE);
        }
        initKnownContacts();
    }

    public void onResume() {
        super.onResume();
        this.mSession.getDataHandler().addListener(this.mEventsListener);
        ContactsManager.getInstance().addListener(this);
        initContactsData();
        initContactsViews();
        this.mRecycler.addOnScrollListener(this.mScrollListener);
    }

    public void onPause() {
        super.onPause();
        if (this.mSession.isAlive()) {
            this.mSession.getDataHandler().removeListener(this.mEventsListener);
        }
        ContactsManager.getInstance().removeListener(this);
        this.mRecycler.removeOnScrollListener(this.mScrollListener);
        this.mSession.cancelUsersSearch();
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 567) {
            if (iArr.length <= 0 || iArr[0] != 0) {
                initContactsData();
            } else {
                ContactsManager.getInstance().refreshLocalContactsSnapshot();
            }
            initContactsViews();
        }
    }

    /* access modifiers changed from: protected */
    public List<Room> getRooms() {
        return new ArrayList(this.mDirectChats);
    }

    /* access modifiers changed from: protected */
    public void onFilter(final String str, final OnFilterListener onFilterListener) {
        this.mAdapter.getFilter().filter(str, new FilterListener() {
            public void onFilterComplete(int i) {
                boolean z = TextUtils.isEmpty(TchapContactFragment.this.mCurrentFilter) && !TextUtils.isEmpty(str);
                String access$300 = TchapContactFragment.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onFilterComplete ");
                sb.append(i);
                Log.i(access$300, sb.toString());
                OnFilterListener onFilterListener = onFilterListener;
                if (onFilterListener != null) {
                    onFilterListener.onFilterDone(i);
                }
                if (!DinsicUtils.isExternalTchapSession(TchapContactFragment.this.mSession)) {
                    TchapContactFragment.this.startRemoteKnownContactsSearch(z);
                } else {
                    TchapContactFragment.this.mAdapter.filterAccountKnownContacts(TchapContactFragment.this.mCurrentFilter);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResetFilter() {
        this.mAdapter.getFilter().filter("", new FilterListener() {
            public void onFilterComplete(int i) {
                String access$300 = TchapContactFragment.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onResetFilter ");
                sb.append(i);
                Log.i(access$300, sb.toString());
            }
        });
    }

    private void initViews() {
        int dimension = (int) getResources().getDimension(R.dimen.item_decoration_left_margin);
        this.mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), 1, false));
        this.mRecycler.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), 1, dimension));
        RecyclerView recyclerView = this.mRecycler;
        EmptyViewItemDecoration emptyViewItemDecoration = new EmptyViewItemDecoration(getActivity(), 1, 40, 16, 14);
        recyclerView.addItemDecoration(emptyViewItemDecoration);
        this.mAdapter = new TchapContactAdapter(getActivity(), new OnSelectItemListener() {
            public void onSelectItem(Room room, int i) {
                TchapContactFragment.this.openRoom(room);
            }

            public void onSelectItem(ParticipantAdapterItem participantAdapterItem, int i) {
                DinsicUtils.startDirectChat((VectorAppCompatActivity) TchapContactFragment.this.getActivity(), TchapContactFragment.this.mSession, participantAdapterItem);
            }
        }, this, this);
        this.mRecycler.setAdapter(this.mAdapter);
    }

    private void initContactsData() {
        ContactsManager.getInstance().retrievePids();
        if (!ContactsManager.getInstance().didPopulateLocalContacts()) {
            Log.d(LOG_TAG, "## initContactsData() : The local contacts are not yet populated");
            this.mLocalContacts.clear();
        } else {
            int i = this.mContactsSnapshotSession;
            if (i == -1 || i != ContactsManager.getInstance().getLocalContactsSnapshotSession()) {
                this.mLocalContacts.clear();
                this.mLocalContacts.addAll(getOnlyTchapUserContacts());
            } else {
                ArrayList<ParticipantAdapterItem> arrayList = new ArrayList<>();
                for (ParticipantAdapterItem participantAdapterItem : this.mLocalContacts) {
                    if (participantAdapterItem.mContact.getContactId() == "null") {
                        arrayList.add(participantAdapterItem);
                    }
                }
                for (ParticipantAdapterItem remove : arrayList) {
                    this.mLocalContacts.remove(remove);
                }
            }
        }
        for (ParticipantAdapterItem participantAdapterItem2 : DinsicUtils.getContactsFromDirectChats(this.mSession)) {
            DinsicUtils.removeParticipantIfExist(this.mLocalContacts, participantAdapterItem2);
            this.mLocalContacts.add(participantAdapterItem2);
        }
    }

    /* access modifiers changed from: private */
    public void initKnownContacts() {
        AnonymousClass6 r0 = new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                TchapContactFragment.this.mKnownContacts.clear();
                TchapContactFragment.this.mKnownContacts.addAll(new ArrayList(VectorUtils.listKnownParticipants(TchapContactFragment.this.mSession).values()));
                return null;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Void voidR) {
                TchapContactFragment.this.mAdapter.setKnownContacts(TchapContactFragment.this.mKnownContacts);
            }
        };
        try {
            r0.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## initKnownContacts() failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString());
            r0.cancel(true);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                public void run() {
                    TchapContactFragment.this.initKnownContacts();
                }
            }, 1000);
        }
    }

    private void showKnownContactLoadingView() {
        TchapContactAdapter tchapContactAdapter = this.mAdapter;
        tchapContactAdapter.getSectionViewForSectionIndex(tchapContactAdapter.getSectionsCount() - 1).showLoadingView();
    }

    /* access modifiers changed from: private */
    public void hideKnownContactLoadingView() {
        TchapContactAdapter tchapContactAdapter = this.mAdapter;
        tchapContactAdapter.getSectionViewForSectionIndex(tchapContactAdapter.getSectionsCount() - 1).hideLoadingView();
    }

    /* access modifiers changed from: private */
    public void startRemoteKnownContactsSearch(boolean z) {
        if (!TextUtils.isEmpty(this.mCurrentFilter)) {
            if (z) {
                this.mAdapter.setFilteredKnownContacts(new ArrayList(), this.mCurrentFilter);
                showKnownContactLoadingView();
            }
            final String str = this.mCurrentFilter;
            this.mSession.searchUsers(this.mCurrentFilter, Integer.valueOf(50), new HashSet(Arrays.asList(new String[]{this.mSession.getMyUserId()})), new ApiCallback<SearchUsersResponse>() {
                public void onSuccess(SearchUsersResponse searchUsersResponse) {
                    if (TextUtils.equals(str, TchapContactFragment.this.mCurrentFilter)) {
                        TchapContactFragment.this.hideKnownContactLoadingView();
                        ArrayList arrayList = new ArrayList();
                        if (searchUsersResponse.results != null) {
                            for (User participantAdapterItem : searchUsersResponse.results) {
                                arrayList.add(new ParticipantAdapterItem(participantAdapterItem));
                            }
                        }
                        TchapContactFragment.this.mAdapter.setKnownContactsExtraTitle(null);
                        TchapContactFragment.this.mAdapter.setKnownContactsLimited(searchUsersResponse.limited != null ? searchUsersResponse.limited.booleanValue() : false);
                        TchapContactFragment.this.mAdapter.setFilteredKnownContacts(arrayList, TchapContactFragment.this.mCurrentFilter);
                    }
                }

                private void onError(String str) {
                    String access$300 = TchapContactFragment.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## startRemoteKnownContactsSearch() : failed ");
                    sb.append(str);
                    Log.e(access$300, sb.toString());
                    if (TextUtils.equals(str, TchapContactFragment.this.mCurrentFilter)) {
                        TchapContactFragment.this.hideKnownContactLoadingView();
                        TchapContactFragment.this.mAdapter.setKnownContactsExtraTitle(TchapContactFragment.this.getContext().getString(R.string.offline));
                        TchapContactFragment.this.mAdapter.filterAccountKnownContacts(TchapContactFragment.this.mCurrentFilter);
                    }
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

    private List<ParticipantAdapterItem> getOnlyTchapUserContacts() {
        ArrayList arrayList = new ArrayList();
        Collection<Contact> localContactsSnapshot = ContactsManager.getInstance().getLocalContactsSnapshot();
        this.mContactsSnapshotSession = ContactsManager.getInstance().getLocalContactsSnapshotSession();
        if (localContactsSnapshot != null) {
            for (Contact contact : localContactsSnapshot) {
                for (String str : contact.getEmails()) {
                    if (!TextUtils.isEmpty(str) && !ParticipantAdapterItem.isBlackedListed(str)) {
                        MXID mxid = PIDsRetriever.getInstance().getMXID(str);
                        if (mxid != null && !mxid.mMatrixId.equals(this.mSession.getMyUserId())) {
                            Contact contact2 = new Contact(contact.getContactId());
                            if (DinsicUtils.isExternalTchapUser(mxid.mMatrixId)) {
                                contact2.setDisplayName(str);
                            } else {
                                contact2.setDisplayName(contact.getDisplayName());
                            }
                            contact2.addEmailAdress(str);
                            contact2.setThumbnailUri(contact.getThumbnailUri());
                            ParticipantAdapterItem participantAdapterItem = new ParticipantAdapterItem(contact2);
                            participantAdapterItem.mUserId = mxid.mMatrixId;
                            arrayList.add(participantAdapterItem);
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    private void initContactsViews() {
        this.mAdapter.setLocalContacts(this.mLocalContacts);
    }

    public void onRoomResultUpdated(Result result) {
        if (isResumed()) {
            initContactsData();
        }
    }

    public void onRefresh() {
        initContactsData();
        initContactsViews();
    }

    public void onPIDsUpdate() {
        List onlyTchapUserContacts = getOnlyTchapUserContacts();
        for (ParticipantAdapterItem participantAdapterItem : DinsicUtils.getContactsFromDirectChats(this.mSession)) {
            DinsicUtils.removeParticipantIfExist(onlyTchapUserContacts, participantAdapterItem);
            onlyTchapUserContacts.add(participantAdapterItem);
        }
        if (!this.mLocalContacts.containsAll(onlyTchapUserContacts)) {
            this.mLocalContacts.clear();
            this.mLocalContacts.addAll(onlyTchapUserContacts);
            initContactsViews();
        }
    }

    public void onToggleDirectChat(String str, boolean z) {
        if (!z) {
            this.mAdapter.removeDirectChat(str);
        }
    }

    public void onRoomLeft(String str) {
        this.mAdapter.removeDirectChat(str);
    }

    public void onRoomForgot(String str) {
        this.mAdapter.removeDirectChat(str);
    }
}
