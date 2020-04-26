package im.vector.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.activity.VectorRoomInviteMembersActivity.ContactsFilter;
import im.vector.contacts.Contact;
import im.vector.contacts.Contact.MXID;
import im.vector.contacts.Contact.PhoneNumber;
import im.vector.contacts.ContactsManager;
import im.vector.contacts.PIDsRetriever;
import im.vector.settings.VectorLocale;
import im.vector.util.VectorUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.cli.HelpFormatter;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXPatterns;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.search.SearchUsersResponse;

public class VectorParticipantsAdapter extends BaseExpandableListAdapter {
    private static final String KEY_EXPAND_STATE_SEARCH_LOCAL_CONTACTS_GROUP = "KEY_EXPAND_STATE_SEARCH_LOCAL_CONTACTS_GROUP";
    private static final String KEY_EXPAND_STATE_SEARCH_MATRIX_CONTACTS_GROUP = "KEY_EXPAND_STATE_SEARCH_MATRIX_CONTACTS_GROUP";
    private static final String KEY_FILTER_MATRIX_USERS_ONLY = "KEY_FILTER_MATRIX_USERS_ONLY";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorParticipantsAdapter.class.getSimpleName();
    private static final int MAX_USERS_SEARCH_COUNT = 100;
    private final int mCellLayoutResourceId;
    /* access modifiers changed from: private */
    public ContactsFilter mContactsFilter;
    /* access modifiers changed from: private */
    public List<ParticipantAdapterItem> mContactsParticipants = null;
    private final Context mContext;
    private List<String> mCurrentSelectedUsers = null;
    /* access modifiers changed from: private */
    public List<String> mDisplayNamesList = null;
    /* access modifiers changed from: private */
    public VectorParticipantsAdapterEditListener mEditParticipantListener;
    private ParticipantAdapterItem mFirstEntry;
    private int mFirstEntryPosition = -1;
    private final int mHeaderLayoutResourceId;
    /* access modifiers changed from: private */
    public boolean mIsOfflineContactsSearch;
    private List<ParticipantAdapterItem> mItemsToHide = new ArrayList();
    /* access modifiers changed from: private */
    public boolean mKnownContactsLimited;
    private int mKnownContactsSectionPosition = -1;
    private final LayoutInflater mLayoutInflater;
    private int mLocalContactsSectionPosition = -1;
    private int mLocalContactsSnapshotSession = -1;
    private final List<List<ParticipantAdapterItem>> mParticipantsListsList = new ArrayList();
    /* access modifiers changed from: private */
    public String mPattern = "";
    private final String mRoomId;
    /* access modifiers changed from: private */
    public final MXSession mSession;
    private boolean mShowMatrixUserOnly = false;
    private final Comparator<ParticipantAdapterItem> mSortMethod;
    /* access modifiers changed from: private */
    public List<ParticipantAdapterItem> mUnusedParticipants = null;
    /* access modifiers changed from: private */
    public Set<String> mUsedMemberUserIds = null;

    /* renamed from: im.vector.adapters.VectorParticipantsAdapter$8 reason: invalid class name */
    static /* synthetic */ class AnonymousClass8 {
        static final /* synthetic */ int[] $SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ContactsFilter = new int[ContactsFilter.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|(3:11|12|14)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            $SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ContactsFilter[ContactsFilter.TCHAP_USERS_ONLY.ordinal()] = 1;
            $SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ContactsFilter[ContactsFilter.TCHAP_USERS_ONLY_WITHOUT_EXTERNALS.ordinal()] = 2;
            $SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ContactsFilter[ContactsFilter.TCHAP_USERS_ONLY_WITHOUT_FEDERATION.ordinal()] = 3;
            $SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ContactsFilter[ContactsFilter.ALL_WITHOUT_TCHAP_USERS.ordinal()] = 4;
            $SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ContactsFilter[ContactsFilter.ALL_WITHOUT_EXTERNALS.ordinal()] = 5;
            try {
                $SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ContactsFilter[ContactsFilter.ALL_WITHOUT_FEDERATION.ordinal()] = 6;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public interface OnParticipantsSearchListener {
        void onSearchEnd(int i);
    }

    public interface VectorParticipantsAdapterEditListener {
        void editContactForm(ParticipantAdapterItem participantAdapterItem);
    }

    public boolean hasStableIds() {
        return false;
    }

    public VectorParticipantsAdapter(Context context, int i, int i2, MXSession mXSession, String str, ContactsFilter contactsFilter) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mCellLayoutResourceId = i;
        this.mHeaderLayoutResourceId = i2;
        this.mSession = mXSession;
        this.mRoomId = str;
        this.mContactsFilter = contactsFilter;
        this.mSortMethod = ParticipantAdapterItem.getComparator(mXSession);
    }

    public void reset() {
        this.mParticipantsListsList.clear();
        this.mFirstEntryPosition = -1;
        this.mLocalContactsSectionPosition = -1;
        this.mKnownContactsSectionPosition = -1;
        this.mPattern = null;
        notifyDataSetChanged();
    }

    public void setEditParticipantListener(VectorParticipantsAdapterEditListener vectorParticipantsAdapterEditListener) {
        this.mEditParticipantListener = vectorParticipantsAdapterEditListener;
    }

    public void setSearchedPattern(String str, ParticipantAdapterItem participantAdapterItem, OnParticipantsSearchListener onParticipantsSearchListener) {
        String lowerCase = str == null ? "" : str.toLowerCase().trim().toLowerCase(VectorLocale.INSTANCE.getApplicationLocale());
        int i = 0;
        if (!lowerCase.equals(this.mPattern) || TextUtils.isEmpty(this.mPattern)) {
            this.mPattern = lowerCase;
            if (TextUtils.isEmpty(this.mPattern)) {
                this.mShowMatrixUserOnly = false;
                Editor edit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
                edit.putBoolean(KEY_FILTER_MATRIX_USERS_ONLY, this.mShowMatrixUserOnly);
                edit.apply();
            }
            refresh(participantAdapterItem, onParticipantsSearchListener);
        } else if (onParticipantsSearchListener != null) {
            for (List size : this.mParticipantsListsList) {
                i += size.size();
            }
            onParticipantsSearchListener.onSearchEnd(i);
        }
    }

    /* access modifiers changed from: private */
    public void addLocalContacts(List<ParticipantAdapterItem> list) {
        Collection<Contact> localContactsSnapshot = ContactsManager.getInstance().getLocalContactsSnapshot();
        if (localContactsSnapshot != null) {
            for (Contact contact : localContactsSnapshot) {
                if (!contact.getEmails().isEmpty() || this.mContactsFilter != ContactsFilter.ALL_WITHOUT_TCHAP_USERS) {
                    for (String str : contact.getEmails()) {
                        if (!TextUtils.isEmpty(str) && !ParticipantAdapterItem.isBlackedListed(str)) {
                            MXID mxid = PIDsRetriever.getInstance().getMXID(str);
                            int i = AnonymousClass8.$SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ContactsFilter[this.mContactsFilter.ordinal()];
                            if (i == 1 || i == 2 || i == 3) {
                                if (mxid == null) {
                                }
                            } else if (i == 4 && mxid != null) {
                            }
                            Contact contact2 = new Contact(contact.getContactId());
                            if (mxid == null || !DinsicUtils.isExternalTchapUser(mxid.mMatrixId)) {
                                contact2.setDisplayName(contact.getDisplayName());
                            } else {
                                contact2.setDisplayName(str);
                            }
                            contact2.addEmailAdress(str);
                            contact2.setThumbnailUri(contact.getThumbnailUri());
                            ParticipantAdapterItem participantAdapterItem = new ParticipantAdapterItem(contact2);
                            if (mxid != null) {
                                participantAdapterItem.mUserId = mxid.mMatrixId;
                            } else {
                                participantAdapterItem.mUserId = str;
                            }
                            Set<String> set = this.mUsedMemberUserIds;
                            if ((set == null || !set.contains(participantAdapterItem.mUserId)) && !DinsicUtils.participantAlreadyAdded(list, participantAdapterItem)) {
                                list.add(participantAdapterItem);
                            }
                        }
                    }
                } else {
                    Contact contact3 = new Contact(contact.getContactId());
                    contact3.setDisplayName(contact.getDisplayName());
                    contact3.addEmailAdress(this.mContext.getString(R.string.no_email));
                    contact3.setThumbnailUri(contact.getThumbnailUri());
                    ParticipantAdapterItem participantAdapterItem2 = new ParticipantAdapterItem(contact3);
                    participantAdapterItem2.mIsValid = false;
                    list.add(participantAdapterItem2);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void fillUsedMembersList(final ApiCallback<Void> apiCallback) {
        IMXStore store = this.mSession.getDataHandler().getStore();
        this.mUsedMemberUserIds = new HashSet();
        String str = this.mRoomId;
        if (str == null || store == null) {
            fillUsedMembersListStep2(apiCallback);
            return;
        }
        Room room = store.getRoom(str);
        if (room != null) {
            room.getDisplayableMembersAsync(new SimpleApiCallback<List<RoomMember>>(apiCallback) {
                public void onSuccess(List<RoomMember> list) {
                    for (RoomMember roomMember : list) {
                        if (TextUtils.equals(roomMember.membership, "join") || TextUtils.equals(roomMember.membership, "invite")) {
                            VectorParticipantsAdapter.this.mUsedMemberUserIds.add(roomMember.getUserId());
                        }
                    }
                    VectorParticipantsAdapter.this.fillUsedMembersListStep2(apiCallback);
                }
            });
        } else {
            fillUsedMembersListStep2(apiCallback);
        }
    }

    /* access modifiers changed from: private */
    public void fillUsedMembersListStep2(ApiCallback<Void> apiCallback) {
        for (ParticipantAdapterItem participantAdapterItem : this.mItemsToHide) {
            this.mUsedMemberUserIds.add(participantAdapterItem.mUserId);
        }
        apiCallback.onSuccess(null);
    }

    /* access modifiers changed from: private */
    public void listOtherMembers() {
        fillUsedMembersList(new SimpleApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                ArrayList arrayList = new ArrayList();
                if (VectorParticipantsAdapter.this.mContactsFilter != ContactsFilter.ALL_WITHOUT_TCHAP_USERS) {
                    arrayList.addAll(VectorUtils.listKnownParticipants(VectorParticipantsAdapter.this.mSession).values());
                    for (ParticipantAdapterItem participantAdapterItem : DinsicUtils.getContactsFromDirectChats(VectorParticipantsAdapter.this.mSession)) {
                        DinsicUtils.removeParticipantIfExist(arrayList, participantAdapterItem);
                        arrayList.add(participantAdapterItem);
                    }
                }
                VectorParticipantsAdapter.this.addLocalContacts(arrayList);
                ArrayList arrayList2 = new ArrayList();
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    ParticipantAdapterItem participantAdapterItem2 = (ParticipantAdapterItem) it.next();
                    if (!VectorParticipantsAdapter.this.mUsedMemberUserIds.isEmpty() && VectorParticipantsAdapter.this.mUsedMemberUserIds.contains(participantAdapterItem2.mUserId)) {
                        it.remove();
                    } else if (!TextUtils.isEmpty(participantAdapterItem2.mDisplayName)) {
                        arrayList2.add(participantAdapterItem2.mDisplayName.toLowerCase(VectorLocale.INSTANCE.getApplicationLocale()));
                    }
                }
                synchronized (VectorParticipantsAdapter.LOG_TAG) {
                    VectorParticipantsAdapter.this.mDisplayNamesList = arrayList2;
                    VectorParticipantsAdapter.this.mUnusedParticipants = arrayList;
                }
            }
        });
    }

    public boolean isKnownMembersInitialized() {
        boolean z;
        synchronized (LOG_TAG) {
            z = this.mDisplayNamesList != null;
        }
        return z;
    }

    private static boolean match(ParticipantAdapterItem participantAdapterItem, String str) {
        return participantAdapterItem.startsWith(str);
    }

    public void setSelectedUserIds(List<String> list) {
        this.mCurrentSelectedUsers = new ArrayList(list);
    }

    public void onPIdsUpdate() {
        boolean z;
        List list;
        ArrayList<ParticipantAdapterItem> arrayList = new ArrayList<>();
        ArrayList<ParticipantAdapterItem> arrayList2 = new ArrayList<>();
        synchronized (LOG_TAG) {
            if (this.mUnusedParticipants != null) {
                arrayList = new ArrayList<>(this.mUnusedParticipants);
            }
            z = false;
            if (this.mContactsParticipants != null) {
                if (this.mContactsFilter != ContactsFilter.ALL_WITHOUT_TCHAP_USERS) {
                    list = DinsicUtils.getContactsFromDirectChats(this.mSession);
                } else {
                    list = new ArrayList();
                }
                addLocalContacts(list);
                if (!this.mContactsParticipants.containsAll(list)) {
                    z = true;
                    this.mContactsParticipants = null;
                } else {
                    arrayList2 = new ArrayList<>(this.mContactsParticipants);
                }
            }
        }
        for (ParticipantAdapterItem retrievePids : arrayList) {
            z |= retrievePids.retrievePids();
        }
        for (ParticipantAdapterItem retrievePids2 : arrayList2) {
            z |= retrievePids2.retrievePids();
        }
        if (z) {
            refresh(this.mFirstEntry, null);
        }
    }

    public void setHiddenParticipantItems(List<ParticipantAdapterItem> list) {
        if (list != null) {
            this.mItemsToHide = list;
        }
    }

    public void refresh(final ParticipantAdapterItem participantAdapterItem, final OnParticipantsSearchListener onParticipantsSearchListener) {
        if (!this.mSession.isAlive()) {
            Log.e(LOG_TAG, "refresh : the session is not anymore active");
            return;
        }
        if (this.mLocalContactsSnapshotSession != ContactsManager.getInstance().getLocalContactsSnapshotSession()) {
            synchronized (LOG_TAG) {
                this.mUnusedParticipants = null;
                this.mContactsParticipants = null;
                this.mUsedMemberUserIds = null;
                this.mDisplayNamesList = null;
            }
            this.mLocalContactsSnapshotSession = ContactsManager.getInstance().getLocalContactsSnapshotSession();
        }
        if (DinsicUtils.isExternalTchapSession(this.mSession) || this.mContactsFilter == ContactsFilter.ALL_WITHOUT_TCHAP_USERS) {
            searchAccountKnownContacts(participantAdapterItem, new ArrayList(), true, onParticipantsSearchListener);
            return;
        }
        if (!TextUtils.isEmpty(this.mPattern)) {
            fillUsedMembersList(new SimpleApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    final String access$800 = VectorParticipantsAdapter.this.mPattern;
                    VectorParticipantsAdapter.this.mSession.searchUsers(VectorParticipantsAdapter.this.mPattern, Integer.valueOf(100), VectorParticipantsAdapter.this.mUsedMemberUserIds, new ApiCallback<SearchUsersResponse>() {
                        public void onSuccess(SearchUsersResponse searchUsersResponse) {
                            if (TextUtils.equals(access$800, VectorParticipantsAdapter.this.mPattern)) {
                                ArrayList arrayList = new ArrayList();
                                if (searchUsersResponse.results != null) {
                                    for (User participantAdapterItem : searchUsersResponse.results) {
                                        arrayList.add(new ParticipantAdapterItem(participantAdapterItem));
                                    }
                                }
                                VectorParticipantsAdapter.this.mIsOfflineContactsSearch = false;
                                VectorParticipantsAdapter.this.mKnownContactsLimited = searchUsersResponse.limited != null ? searchUsersResponse.limited.booleanValue() : false;
                                VectorParticipantsAdapter.this.searchAccountKnownContacts(participantAdapterItem, arrayList, false, onParticipantsSearchListener);
                            }
                        }

                        private void onError() {
                            if (TextUtils.equals(access$800, VectorParticipantsAdapter.this.mPattern)) {
                                VectorParticipantsAdapter.this.mIsOfflineContactsSearch = true;
                                VectorParticipantsAdapter.this.searchAccountKnownContacts(participantAdapterItem, new ArrayList(), true, onParticipantsSearchListener);
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
            });
        } else {
            searchAccountKnownContacts(participantAdapterItem, new ArrayList(), true, onParticipantsSearchListener);
        }
    }

    /* access modifiers changed from: private */
    public void searchAccountKnownContacts(final ParticipantAdapterItem participantAdapterItem, List<ParticipantAdapterItem> list, boolean z, final OnParticipantsSearchListener onParticipantsSearchListener) {
        this.mKnownContactsLimited = false;
        if (TextUtils.isEmpty(this.mPattern)) {
            resetGroupExpansionPreferences();
            if (this.mContactsParticipants == null) {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        VectorParticipantsAdapter.this.fillUsedMembersList(new SimpleApiCallback<Void>() {
                            public void onSuccess(Void voidR) {
                                List list;
                                if (VectorParticipantsAdapter.this.mContactsFilter != ContactsFilter.ALL_WITHOUT_TCHAP_USERS) {
                                    list = DinsicUtils.getContactsFromDirectChats(VectorParticipantsAdapter.this.mSession);
                                } else {
                                    list = new ArrayList();
                                }
                                VectorParticipantsAdapter.this.addLocalContacts(list);
                                synchronized (VectorParticipantsAdapter.LOG_TAG) {
                                    VectorParticipantsAdapter.this.mContactsParticipants = list;
                                }
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    public void run() {
                                        VectorParticipantsAdapter.this.refresh(participantAdapterItem, onParticipantsSearchListener);
                                    }
                                });
                            }
                        });
                    }
                });
                thread.setPriority(1);
                thread.start();
                return;
            }
            ArrayList arrayList = new ArrayList();
            synchronized (LOG_TAG) {
                if (this.mContactsParticipants != null) {
                    arrayList = new ArrayList(this.mContactsParticipants);
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ParticipantAdapterItem participantAdapterItem2 = (ParticipantAdapterItem) it.next();
                if (!this.mUsedMemberUserIds.isEmpty() && this.mUsedMemberUserIds.contains(participantAdapterItem2.mUserId)) {
                    it.remove();
                }
            }
            synchronized (LOG_TAG) {
                this.mContactsParticipants = arrayList;
            }
            synchronized (LOG_TAG) {
                if (this.mContactsParticipants != null) {
                    list.addAll(this.mContactsParticipants);
                }
            }
        } else if (this.mUnusedParticipants == null) {
            final ParticipantAdapterItem participantAdapterItem3 = participantAdapterItem;
            final List<ParticipantAdapterItem> list2 = list;
            final boolean z2 = z;
            final OnParticipantsSearchListener onParticipantsSearchListener2 = onParticipantsSearchListener;
            AnonymousClass4 r2 = new Runnable() {
                public void run() {
                    VectorParticipantsAdapter.this.listOtherMembers();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            VectorParticipantsAdapter.this.searchAccountKnownContacts(participantAdapterItem3, list2, z2, onParticipantsSearchListener2);
                        }
                    });
                }
            };
            Thread thread2 = new Thread(r2);
            thread2.setPriority(1);
            thread2.start();
            return;
        } else {
            ArrayList<ParticipantAdapterItem> arrayList2 = new ArrayList<>();
            synchronized (LOG_TAG) {
                if (this.mUnusedParticipants != null) {
                    arrayList2 = new ArrayList<>(this.mUnusedParticipants);
                }
            }
            for (ParticipantAdapterItem participantAdapterItem4 : arrayList2) {
                if (match(participantAdapterItem4, this.mPattern)) {
                    DinsicUtils.removeParticipantIfExist(list, participantAdapterItem4);
                    list.add(participantAdapterItem4);
                }
            }
        }
        onKnownContactsSearchEnd(list, participantAdapterItem, z, onParticipantsSearchListener);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0055, code lost:
        if (r10 != 6) goto L_0x00aa;
     */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00c3  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00f1  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x0161  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0178  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x01a8  */
    private void onKnownContactsSearchEnd(List<ParticipantAdapterItem> list, ParticipantAdapterItem participantAdapterItem, boolean z, OnParticipantsSearchListener onParticipantsSearchListener) {
        ArrayList arrayList;
        ArrayList arrayList2;
        ArrayList arrayList3;
        ContactsManager.getInstance().retrievePids();
        Set<String> set = this.mUsedMemberUserIds;
        if (!(set == null || participantAdapterItem == null || !set.contains(participantAdapterItem.mUserId))) {
            participantAdapterItem = null;
        }
        int i = 0;
        if (participantAdapterItem != null) {
            list.add(0, participantAdapterItem);
            int i2 = 1;
            while (true) {
                if (i2 >= list.size()) {
                    break;
                } else if (TextUtils.equals(((ParticipantAdapterItem) list.get(i2)).mUserId, participantAdapterItem.mUserId)) {
                    list.remove(i2);
                    break;
                } else {
                    i2++;
                }
            }
            this.mFirstEntry = participantAdapterItem;
        } else {
            this.mFirstEntry = null;
        }
        int i3 = AnonymousClass8.$SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ContactsFilter[this.mContactsFilter.ordinal()];
        if (i3 != 2) {
            if (i3 != 3) {
                if (i3 != 5) {
                }
            }
            String homeServerNameFromMXIdentifier = DinsicUtils.getHomeServerNameFromMXIdentifier(this.mSession.getMyUserId());
            Iterator it = list.iterator();
            while (it.hasNext()) {
                ParticipantAdapterItem participantAdapterItem2 = (ParticipantAdapterItem) it.next();
                if (participantAdapterItem2.isMatrixUser() && !DinsicUtils.getHomeServerNameFromMXIdentifier(participantAdapterItem2.mUserId).equals(homeServerNameFromMXIdentifier)) {
                    it.remove();
                }
            }
            arrayList = new ArrayList();
            arrayList2 = new ArrayList();
            arrayList3 = new ArrayList();
            for (ParticipantAdapterItem participantAdapterItem3 : list) {
                ParticipantAdapterItem participantAdapterItem4 = this.mFirstEntry;
                if (participantAdapterItem3 == participantAdapterItem4) {
                    arrayList.add(participantAdapterItem4);
                } else if (participantAdapterItem3.mContact != null) {
                    if (!DinsicUtils.participantAlreadyAdded(arrayList2, participantAdapterItem3)) {
                        arrayList2.add(participantAdapterItem3);
                    }
                } else if (!DinsicUtils.participantAlreadyAdded(arrayList3, participantAdapterItem3)) {
                    arrayList3.add(participantAdapterItem3);
                }
            }
            if (TextUtils.isEmpty(this.mPattern)) {
                for (String str : this.mCurrentSelectedUsers) {
                    if (MXPatterns.PATTERN_CONTAIN_MATRIX_USER_IDENTIFIER.matcher(str).matches()) {
                        User user = this.mSession.getDataHandler().getUser(str);
                        String str2 = user != null ? user.displayname : null;
                        if (TextUtils.isEmpty(str2)) {
                            str2 = DinsicUtils.computeDisplayNameFromUserId(str);
                        }
                        ParticipantAdapterItem participantAdapterItem5 = new ParticipantAdapterItem(str2, null, str, true);
                        if (!DinsicUtils.participantAlreadyAdded(arrayList2, participantAdapterItem5)) {
                            arrayList2.add(participantAdapterItem5);
                        }
                    } else if (Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
                        ParticipantAdapterItem participantAdapterItem6 = new ParticipantAdapterItem(str, null, str, true);
                        if (!DinsicUtils.participantAlreadyAdded(arrayList2, participantAdapterItem6)) {
                            arrayList2.add(participantAdapterItem6);
                        }
                    }
                }
            }
            this.mFirstEntryPosition = -1;
            this.mParticipantsListsList.clear();
            if (arrayList.size() > 0) {
                this.mParticipantsListsList.add(arrayList);
                this.mFirstEntryPosition = 0;
            }
            this.mLocalContactsSectionPosition = this.mFirstEntryPosition + 1;
            this.mKnownContactsSectionPosition = this.mLocalContactsSectionPosition + 1;
            if (arrayList2.size() > 0) {
                Collections.sort(arrayList2, ParticipantAdapterItem.tchapComparator);
            }
            this.mParticipantsListsList.add(arrayList2);
            if (!this.mContactsFilter.equals(ContactsFilter.ALL_WITHOUT_TCHAP_USERS) && !TextUtils.isEmpty(this.mPattern)) {
                if (arrayList3.size() > 0 && z) {
                    Collections.sort(arrayList3, this.mSortMethod);
                }
                this.mParticipantsListsList.add(arrayList3);
            }
            if (onParticipantsSearchListener != null) {
                for (List size : this.mParticipantsListsList) {
                    i += size.size();
                }
                onParticipantsSearchListener.onSearchEnd(i);
            }
            notifyDataSetChanged();
        }
        Iterator it2 = list.iterator();
        while (it2.hasNext()) {
            ParticipantAdapterItem participantAdapterItem7 = (ParticipantAdapterItem) it2.next();
            if (participantAdapterItem7.isMatrixUser() && DinsicUtils.isExternalTchapUser(participantAdapterItem7.mUserId)) {
                it2.remove();
            }
        }
        arrayList = new ArrayList();
        arrayList2 = new ArrayList();
        arrayList3 = new ArrayList();
        for (ParticipantAdapterItem participantAdapterItem32 : list) {
        }
        if (TextUtils.isEmpty(this.mPattern)) {
        }
        this.mFirstEntryPosition = -1;
        this.mParticipantsListsList.clear();
        if (arrayList.size() > 0) {
        }
        this.mLocalContactsSectionPosition = this.mFirstEntryPosition + 1;
        this.mKnownContactsSectionPosition = this.mLocalContactsSectionPosition + 1;
        if (arrayList2.size() > 0) {
        }
        this.mParticipantsListsList.add(arrayList2);
        Collections.sort(arrayList3, this.mSortMethod);
        this.mParticipantsListsList.add(arrayList3);
        if (onParticipantsSearchListener != null) {
        }
        notifyDataSetChanged();
    }

    public void onGroupCollapsed(int i) {
        super.onGroupCollapsed(i);
        setGroupExpandedStatus(i, false);
    }

    public void onGroupExpanded(int i) {
        super.onGroupExpanded(i);
        setGroupExpandedStatus(i, true);
    }

    public boolean isChildSelectable(int i, int i2) {
        return (i == 0 && ((ParticipantAdapterItem) getChild(i, i2)) == null) ? false : true;
    }

    public int getGroupCount() {
        return this.mParticipantsListsList.size();
    }

    private boolean couldHaveUnusedParticipants() {
        List<ParticipantAdapterItem> list = this.mUnusedParticipants;
        boolean z = false;
        if (list != null) {
            if (list.size() != 0) {
                z = true;
            }
            return z;
        }
        for (Room numberOfMembers : this.mSession.getDataHandler().getStore().getRooms()) {
            if (numberOfMembers.getNumberOfMembers() > 1) {
                return true;
            }
        }
        return false;
    }

    private String getGroupTitle(int i) {
        int i2;
        String str;
        if (i < this.mParticipantsListsList.size()) {
            i2 = ((List) this.mParticipantsListsList.get(i)).size();
        } else {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getGroupTitle position ");
            sb.append(i);
            sb.append(" is invalid, mParticipantsListsList.size()=");
            sb.append(this.mParticipantsListsList.size());
            Log.e(str2, sb.toString());
            i2 = 0;
        }
        if (i == this.mLocalContactsSectionPosition) {
            return this.mContext.getString(R.string.people_search_local_contacts, new Object[]{Integer.valueOf(i2)});
        } else if (i != this.mKnownContactsSectionPosition) {
            return "??";
        } else {
            if (TextUtils.isEmpty(this.mPattern) && couldHaveUnusedParticipants()) {
                str = HelpFormatter.DEFAULT_OPT_PREFIX;
            } else if (this.mIsOfflineContactsSearch) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(this.mContext.getString(R.string.offline));
                sb2.append(", ");
                sb2.append(String.valueOf(i2));
                str = sb2.toString();
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(this.mKnownContactsLimited ? ">" : "");
                sb3.append(String.valueOf(i2));
                str = sb3.toString();
            }
            return this.mContext.getString(R.string.people_search_user_directory, new Object[]{str});
        }
    }

    public Object getGroup(int i) {
        return getGroupTitle(i);
    }

    public long getGroupId(int i) {
        return (long) getGroupTitle(i).hashCode();
    }

    public int getChildrenCount(int i) {
        if (i >= this.mParticipantsListsList.size()) {
            return 0;
        }
        return ((List) this.mParticipantsListsList.get(i)).size();
    }

    public Object getChild(int i, int i2) {
        if (i < this.mParticipantsListsList.size() && i >= 0) {
            List list = (List) this.mParticipantsListsList.get(i);
            if (i2 < list.size() && i2 >= 0) {
                return list.get(i2);
            }
        }
        return null;
    }

    public long getChildId(int i, int i2) {
        Object child = getChild(i, i2);
        if (child != null) {
            return (long) child.hashCode();
        }
        return 0;
    }

    public View getGroupView(final int i, boolean z, View view, final ViewGroup viewGroup) {
        if (view == null) {
            view = this.mLayoutInflater.inflate(this.mHeaderLayoutResourceId, null);
        }
        TextView textView = (TextView) view.findViewById(R.id.people_header_text_view);
        if (textView != null) {
            textView.setText(getGroupTitle(i));
        }
        View findViewById = view.findViewById(R.id.people_header_sub_layout);
        if (findViewById == null) {
            Log.e(LOG_TAG, "## getGroupView() : null subLayout");
            return view;
        }
        int i2 = 8;
        findViewById.setVisibility(i == this.mFirstEntryPosition ? 8 : 0);
        View findViewById2 = findViewById.findViewById(R.id.heading_loading_view);
        if (findViewById2 == null) {
            Log.e(LOG_TAG, "## getGroupView() : null loadingView");
            return view;
        }
        if (i == this.mLocalContactsSectionPosition && !ContactsManager.getInstance().arePIDsRetrieved()) {
            i2 = 0;
        }
        findViewById2.setVisibility(i2);
        ImageView imageView = (ImageView) view.findViewById(R.id.heading_image);
        if (imageView == null) {
            Log.e(LOG_TAG, "## getGroupView() : null UI items");
            return view;
        }
        if (i != this.mKnownContactsSectionPosition || ((List) this.mParticipantsListsList.get(i)).size() > 0) {
            if (z) {
                imageView.setImageResource(R.drawable.ic_material_expand_less_black);
            } else {
                imageView.setImageResource(R.drawable.ic_material_expand_more_black);
            }
            boolean isGroupExpanded = isGroupExpanded(i);
            if (viewGroup instanceof ExpandableListView) {
                ExpandableListView expandableListView = (ExpandableListView) viewGroup;
                if (expandableListView.isGroupExpanded(i) != isGroupExpanded) {
                    if (isGroupExpanded) {
                        imageView.setImageResource(R.drawable.ic_material_expand_less_black);
                        expandableListView.expandGroup(i);
                    } else {
                        imageView.setImageResource(R.drawable.ic_material_expand_more_black);
                        expandableListView.collapseGroup(i);
                    }
                }
            }
            findViewById.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ViewGroup viewGroup = viewGroup;
                    if (!(viewGroup instanceof ExpandableListView)) {
                        return;
                    }
                    if (((ExpandableListView) viewGroup).isGroupExpanded(i)) {
                        ((ExpandableListView) viewGroup).collapseGroup(i);
                    } else {
                        ((ExpandableListView) viewGroup).expandGroup(i);
                    }
                }
            });
        } else {
            imageView.setImageDrawable(null);
        }
        return view;
    }

    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        int i3 = 0;
        if (view == null) {
            view = this.mLayoutInflater.inflate(this.mCellLayoutResourceId, viewGroup, false);
        }
        if (i >= this.mParticipantsListsList.size()) {
            Log.e(LOG_TAG, "## getChildView() : invalid group position");
            return view;
        }
        List list = (List) this.mParticipantsListsList.get(i);
        if (i2 >= list.size()) {
            Log.e(LOG_TAG, "## getChildView() : invalid child position");
            return view;
        }
        final ParticipantAdapterItem participantAdapterItem = (ParticipantAdapterItem) list.get(i2);
        ImageView imageView = (ImageView) view.findViewById(R.id.filtered_list_avatar);
        TextView textView = (TextView) view.findViewById(R.id.filtered_list_name);
        TextView textView2 = (TextView) view.findViewById(R.id.filtered_list_domain);
        TextView textView3 = (TextView) view.findViewById(R.id.filtered_list_email);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.filtered_list_online_status);
        if (imageView == null || textView == null || textView3 == null) {
            Log.e(LOG_TAG, "## getChildView() : some ui items are null");
            return view;
        }
        if (participantAdapterItem.isMatrixUser()) {
            imageView2.setVisibility(VectorUtils.isUserOnline(this.mContext, this.mSession, participantAdapterItem.mUserId, null) ? 0 : 8);
            textView2.setText(DinsicUtils.getDomainFromDisplayName(participantAdapterItem.mDisplayName));
            textView2.setVisibility(0);
            textView2.setPadding(0, 33, 0, 0);
            textView.setText(DinsicUtils.getNameFromDisplayName(participantAdapterItem.mDisplayName));
            textView.setPadding(0, 21, 0, 0);
            textView.setTypeface(null, 1);
            textView3.setVisibility(8);
        } else {
            textView2.setVisibility(8);
            textView2.setPadding(0, 0, 0, 0);
            textView.setText(participantAdapterItem.mDisplayName);
            textView.setPadding(0, 0, 0, 0);
            textView.setTypeface(null, 2);
            if (participantAdapterItem.mContact != null) {
                textView3.setVisibility(0);
                if (participantAdapterItem.mContact.getEmails().size() > 0) {
                    textView3.setText((CharSequence) participantAdapterItem.mContact.getEmails().get(0));
                } else if (participantAdapterItem.mContact.getPhonenumbers().size() > 0) {
                    textView3.setText(((PhoneNumber) participantAdapterItem.mContact.getPhonenumbers().get(0)).mRawPhoneNumber);
                }
            } else {
                textView3.setVisibility(8);
            }
        }
        participantAdapterItem.displayAvatar(this.mSession, imageView);
        View findViewById = view.findViewById(R.id.filtered_list_actions_list);
        if (this.mEditParticipantListener != null) {
            findViewById.setVisibility(0);
            findViewById.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (VectorParticipantsAdapter.this.mEditParticipantListener != null) {
                        VectorParticipantsAdapter.this.mEditParticipantListener.editContactForm(participantAdapterItem);
                    }
                }
            });
        } else {
            findViewById.setVisibility(8);
        }
        ImageView imageView3 = (ImageView) view.findViewById(R.id.icon_check_invite_member);
        if (participantAdapterItem.mIsValid) {
            if (!this.mCurrentSelectedUsers.contains(participantAdapterItem.mUserId)) {
                i3 = 8;
            }
            imageView3.setVisibility(i3);
        } else {
            imageView3.setVisibility(8);
        }
        return view;
    }

    private void resetGroupExpansionPreferences() {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        edit.remove(KEY_EXPAND_STATE_SEARCH_LOCAL_CONTACTS_GROUP);
        edit.remove(KEY_EXPAND_STATE_SEARCH_MATRIX_CONTACTS_GROUP);
        edit.remove(KEY_FILTER_MATRIX_USERS_ONLY);
        edit.apply();
    }

    private boolean isGroupExpanded(int i) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        if (i == this.mLocalContactsSectionPosition) {
            return defaultSharedPreferences.getBoolean(KEY_EXPAND_STATE_SEARCH_LOCAL_CONTACTS_GROUP, true);
        }
        if (i == this.mKnownContactsSectionPosition) {
            return defaultSharedPreferences.getBoolean(KEY_EXPAND_STATE_SEARCH_MATRIX_CONTACTS_GROUP, true);
        }
        return true;
    }

    private void setGroupExpandedStatus(int i, boolean z) {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        if (i == this.mLocalContactsSectionPosition) {
            edit.putBoolean(KEY_EXPAND_STATE_SEARCH_LOCAL_CONTACTS_GROUP, z);
        } else if (i == this.mKnownContactsSectionPosition) {
            edit.putBoolean(KEY_EXPAND_STATE_SEARCH_MATRIX_CONTACTS_GROUP, z);
        }
        edit.apply();
    }
}
