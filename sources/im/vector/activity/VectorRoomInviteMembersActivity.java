package im.vector.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.activity.TchapLoginActivity;
import fr.gouv.tchap.sdk.rest.client.TchapThirdPidRestClient;
import fr.gouv.tchap.sdk.rest.model.Platform;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.Matrix;
import im.vector.adapters.ParticipantAdapterItem;
import im.vector.adapters.VectorParticipantsAdapter;
import im.vector.adapters.VectorParticipantsAdapter.OnParticipantsSearchListener;
import im.vector.adapters.VectorParticipantsAdapter.VectorParticipantsAdapterEditListener;
import im.vector.contacts.Contact;
import im.vector.contacts.ContactsManager;
import im.vector.contacts.ContactsManager.ContactsManagerListener;
import im.vector.settings.VectorLocale;
import im.vector.util.PermissionsToolsKt;
import im.vector.util.VectorUtils;
import im.vector.view.VectorAutoCompleteTextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.pid.RoomThirdPartyInvite;

public class VectorRoomInviteMembersActivity extends MXCActionBarActivity implements OnQueryTextListener {
    public static final String EXTRA_ACTION_ACTIVITY_MODE = "EXTRA_ACTION_ACTIVITY_MODE";
    public static final String EXTRA_ADD_CONFIRMATION_DIALOG = "VectorInviteMembersActivity.EXTRA_ADD_CONFIRMATION_DIALOG";
    public static final String EXTRA_CONTACTS_FILTER = "EXTRA_CONTACTS_FILTER";
    public static final String EXTRA_HIDDEN_PARTICIPANT_ITEMS = "VectorInviteMembersActivity.EXTRA_HIDDEN_PARTICIPANT_ITEMS";
    public static final String EXTRA_IN_SELECTED_USER_IDS = "VectorInviteMembersActivity.EXTRA_IN_SELECTED_USER_IDS";
    public static final String EXTRA_OUT_SELECTED_PARTICIPANT_ITEMS = "VectorInviteMembersActivity.EXTRA_OUT_SELECTED_PARTICIPANT_ITEMS";
    public static final String EXTRA_OUT_SELECTED_USER_IDS = "VectorInviteMembersActivity.EXTRA_OUT_SELECTED_USER_IDS";
    public static final String EXTRA_ROOM_ID = "VectorInviteMembersActivity.EXTRA_ROOM_ID";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorRoomInviteMembersActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public ActionMode mActionMode = ActionMode.RETURN_SELECTED_USER_IDS;
    /* access modifiers changed from: private */
    public VectorParticipantsAdapter mAdapter;
    private boolean mAddConfirmationDialog;
    /* access modifiers changed from: private */
    public ContactsFilter mContactsFilter = ContactsFilter.ALL;
    private final ContactsManagerListener mContactsListener = new ContactsManagerListener() {
        public void onContactPresenceUpdate(Contact contact, String str) {
        }

        public void onRefresh() {
            VectorRoomInviteMembersActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    VectorRoomInviteMembersActivity.this.onPatternUpdate(false);
                }
            });
        }

        public void onPIDsUpdate() {
            VectorRoomInviteMembersActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    VectorRoomInviteMembersActivity.this.mAdapter.onPIdsUpdate();
                }
            });
        }
    };
    /* access modifiers changed from: private */
    public int mCount;
    private final MXEventListener mEventsListener = new MXEventListener() {
        public void onPresenceUpdate(Event event, final User user) {
            VectorRoomInviteMembersActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Map visibleChildViews = VectorUtils.getVisibleChildViews(VectorRoomInviteMembersActivity.this.mListView, VectorRoomInviteMembersActivity.this.mAdapter);
                    for (Integer num : visibleChildViews.keySet()) {
                        Iterator it = ((List) visibleChildViews.get(num)).iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            Object child = VectorRoomInviteMembersActivity.this.mAdapter.getChild(num.intValue(), ((Integer) it.next()).intValue());
                            if (child instanceof ParticipantAdapterItem) {
                                if (TextUtils.equals(user.user_id, ((ParticipantAdapterItem) child).mUserId)) {
                                    VectorRoomInviteMembersActivity.this.mAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                }
            });
        }
    };
    private List<ParticipantAdapterItem> mHiddenParticipantItems = new ArrayList();
    @BindView(2131296940)
    ExpandableListView mListView;
    private String mMatrixId;
    /* access modifiers changed from: private */
    public View mParentLayout;
    /* access modifiers changed from: private */
    public SearchView mSearchView;
    /* access modifiers changed from: private */
    public int mSuccessCount;
    ArrayList<String> mUserIdsToInvite = new ArrayList<>();

    /* renamed from: im.vector.activity.VectorRoomInviteMembersActivity$16 reason: invalid class name */
    static /* synthetic */ class AnonymousClass16 {
        static final /* synthetic */ int[] $SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ActionMode = new int[ActionMode.values().length];
        static final /* synthetic */ int[] $SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ContactsFilter = new int[ContactsFilter.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(18:0|1|2|3|5|6|7|9|10|(2:11|12)|13|15|16|17|18|19|20|22) */
        /* JADX WARNING: Can't wrap try/catch for region: R(19:0|1|2|3|5|6|7|9|10|11|12|13|15|16|17|18|19|20|22) */
        /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0048 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0052 */
        static {
            try {
                $SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ContactsFilter[ContactsFilter.ALL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ContactsFilter[ContactsFilter.ALL_WITHOUT_EXTERNALS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            $SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ContactsFilter[ContactsFilter.ALL_WITHOUT_FEDERATION.ordinal()] = 3;
            try {
                $SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ContactsFilter[ContactsFilter.ALL_WITHOUT_TCHAP_USERS.ordinal()] = 4;
            } catch (NoSuchFieldError unused3) {
            }
            $SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ActionMode[ActionMode.START_DIRECT_CHAT.ordinal()] = 1;
            $SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ActionMode[ActionMode.RETURN_SELECTED_USER_IDS.ordinal()] = 2;
            $SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ActionMode[ActionMode.SEND_INVITE.ordinal()] = 3;
        }
    }

    public enum ActionMode {
        START_DIRECT_CHAT,
        SEND_INVITE,
        RETURN_SELECTED_USER_IDS
    }

    public enum ContactsFilter {
        ALL,
        ALL_WITHOUT_EXTERNALS,
        ALL_WITHOUT_FEDERATION,
        TCHAP_USERS_ONLY,
        TCHAP_USERS_ONLY_WITHOUT_EXTERNALS,
        TCHAP_USERS_ONLY_WITHOUT_FEDERATION,
        ALL_WITHOUT_TCHAP_USERS
    }

    public int getLayoutRes() {
        return R.layout.activity_vector_invite_members;
    }

    public boolean onQueryTextSubmit(String str) {
        return false;
    }

    static /* synthetic */ int access$1106(VectorRoomInviteMembersActivity vectorRoomInviteMembersActivity) {
        int i = vectorRoomInviteMembersActivity.mCount - 1;
        vectorRoomInviteMembersActivity.mCount = i;
        return i;
    }

    public boolean onQueryTextChange(String str) {
        onPatternUpdate(true);
        return false;
    }

    public void initUiAndData() {
        super.initUiAndData();
        if (CommonActivityUtils.shouldRestartApp(this)) {
            Log.e(LOG_TAG, "Restart the application.");
            CommonActivityUtils.restartApp(this);
        } else if (CommonActivityUtils.isGoingToSplash(this)) {
            Log.d(LOG_TAG, "onCreate : Going to splash screen");
        } else {
            setWaitingView(findViewById(R.id.search_progress_view));
            this.mParentLayout = findViewById(R.id.vector_invite_members_layout);
            this.mSearchView = (SearchView) findViewById(R.id.external_search_view);
            initSearchView();
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.mSearchView.getApplicationWindowToken(), 0);
            Intent intent = getIntent();
            String str = "MXCActionBarActivity.EXTRA_MATRIX_ID";
            if (intent.hasExtra(str)) {
                this.mMatrixId = intent.getStringExtra(str);
            }
            String str2 = EXTRA_HIDDEN_PARTICIPANT_ITEMS;
            if (intent.hasExtra(str2)) {
                this.mHiddenParticipantItems = (List) intent.getSerializableExtra(str2);
            }
            Intent intent2 = getIntent();
            String str3 = EXTRA_ACTION_ACTIVITY_MODE;
            if (intent2.hasExtra(str3)) {
                this.mActionMode = (ActionMode) intent.getSerializableExtra(str3);
            }
            Intent intent3 = getIntent();
            String str4 = EXTRA_CONTACTS_FILTER;
            if (intent3.hasExtra(str4)) {
                this.mContactsFilter = (ContactsFilter) intent.getSerializableExtra(str4);
            }
            Intent intent4 = getIntent();
            String str5 = EXTRA_IN_SELECTED_USER_IDS;
            if (intent4.hasExtra(str5)) {
                this.mUserIdsToInvite = (ArrayList) intent.getSerializableExtra(str5);
            }
            int i = AnonymousClass16.$SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ActionMode[this.mActionMode.ordinal()];
            if (i == 1) {
                setTitle(R.string.tchap_room_invite_member_direct_chat);
            } else if (i == 2) {
                setTitle(R.string.tchap_room_invite_member_title);
            } else if (i == 3) {
                setTitle(R.string.room_creation_invite_members);
            }
            this.mSession = Matrix.getInstance(getApplicationContext()).getSession(this.mMatrixId);
            if (this.mSession == null || !this.mSession.isAlive()) {
                finish();
                return;
            }
            String stringExtra = intent.getStringExtra(EXTRA_ROOM_ID);
            if (stringExtra != null) {
                this.mRoom = this.mSession.getDataHandler().getStore().getRoom(stringExtra);
            }
            this.mAddConfirmationDialog = intent.getBooleanExtra(EXTRA_ADD_CONFIRMATION_DIALOG, false);
            this.mListView.setGroupIndicator(null);
            VectorParticipantsAdapter vectorParticipantsAdapter = new VectorParticipantsAdapter(this, R.layout.adapter_item_vector_add_participants, R.layout.adapter_item_vector_people_header, this.mSession, stringExtra, this.mContactsFilter);
            this.mAdapter = vectorParticipantsAdapter;
            this.mAdapter.setSelectedUserIds(this.mUserIdsToInvite);
            if (this.mContactsFilter.equals(ContactsFilter.ALL_WITHOUT_TCHAP_USERS)) {
                this.mAdapter.setEditParticipantListener(new VectorParticipantsAdapterEditListener() {
                    public void editContactForm(ParticipantAdapterItem participantAdapterItem) {
                        if (participantAdapterItem.mContact != null) {
                            VectorRoomInviteMembersActivity vectorRoomInviteMembersActivity = VectorRoomInviteMembersActivity.this;
                            DinsicUtils.editContactForm(vectorRoomInviteMembersActivity, vectorRoomInviteMembersActivity, vectorRoomInviteMembersActivity.getString(R.string.people_edit_contact_warning_msg), participantAdapterItem.mContact);
                        }
                    }
                });
            }
            ParticipantAdapterItem participantAdapterItem = new ParticipantAdapterItem((User) this.mSession.getMyUser());
            if (!DinsicUtils.participantAlreadyAdded(this.mHiddenParticipantItems, participantAdapterItem)) {
                this.mHiddenParticipantItems.add(participantAdapterItem);
            }
            this.mAdapter.setHiddenParticipantItems(this.mHiddenParticipantItems);
            this.mListView.setAdapter(this.mAdapter);
            this.mListView.setOnChildClickListener(new OnChildClickListener() {
                public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long j) {
                    Object child = VectorRoomInviteMembersActivity.this.mAdapter.getChild(i, i2);
                    if (!(child instanceof ParticipantAdapterItem)) {
                        return false;
                    }
                    ParticipantAdapterItem participantAdapterItem = (ParticipantAdapterItem) child;
                    if (VectorRoomInviteMembersActivity.this.mActionMode == ActionMode.START_DIRECT_CHAT) {
                        VectorRoomInviteMembersActivity vectorRoomInviteMembersActivity = VectorRoomInviteMembersActivity.this;
                        DinsicUtils.startDirectChat((VectorAppCompatActivity) vectorRoomInviteMembersActivity, vectorRoomInviteMembersActivity.mSession, participantAdapterItem);
                        return true;
                    }
                    VectorRoomInviteMembersActivity.this.onSelectedParticipant(participantAdapterItem);
                    return true;
                }
            });
            View findViewById = findViewById(R.id.ly_invite_contacts_by_email);
            findViewById.setVisibility(8);
            if (!DinsicUtils.isExternalTchapSession(this.mSession)) {
                int i2 = AnonymousClass16.$SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ContactsFilter[this.mContactsFilter.ordinal()];
                if (i2 == 1 || i2 == 2 || i2 == 3 || i2 == 4) {
                    findViewById.setVisibility(0);
                    findViewById.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            VectorRoomInviteMembersActivity.this.mSearchView.setQuery("", false);
                            VectorRoomInviteMembersActivity.this.mParentLayout.requestFocus();
                            VectorRoomInviteMembersActivity.this.displayDialogToInviteByEmail();
                        }
                    });
                }
            }
            PermissionsToolsKt.checkPermissions(8, (Activity) this, (int) PermissionsToolsKt.PERMISSION_REQUEST_CODE);
        }
    }

    private void initSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService("search");
        LinearLayout linearLayout = (LinearLayout) this.mSearchView.findViewById(R.id.search_edit_frame);
        if (linearLayout != null) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) linearLayout.getLayoutParams();
            marginLayoutParams.leftMargin = -30;
            marginLayoutParams.rightMargin = -15;
            linearLayout.setLayoutParams(marginLayoutParams);
        }
        ((ImageView) this.mSearchView.findViewById(R.id.search_mag_icon)).setColorFilter(ContextCompat.getColor(this, R.color.tchap_search_bar_text));
        ((ImageView) this.mSearchView.findViewById(R.id.search_close_btn)).setColorFilter(ContextCompat.getColor(this, R.color.tchap_search_bar_text));
        this.mSearchView.setMaxWidth(Integer.MAX_VALUE);
        this.mSearchView.setSubmitButtonEnabled(false);
        this.mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        this.mSearchView.setIconifiedByDefault(false);
        this.mSearchView.setOnQueryTextListener(this);
        this.mSearchView.setQueryHint(getString(R.string.search_hint));
        this.mSearchView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (view != null) {
                    VectorRoomInviteMembersActivity.this.mSearchView.setIconified(false);
                }
            }
        });
    }

    public int getMenuRes() {
        hideKeyboard();
        return R.menu.tchap_room_invite_member_menu;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        String str = EXTRA_OUT_SELECTED_USER_IDS;
        if (itemId == 16908332) {
            if (this.mActionMode == ActionMode.RETURN_SELECTED_USER_IDS) {
                Intent intent = new Intent();
                intent.putExtra(str, this.mUserIdsToInvite);
                setResult(0, intent);
            }
            finish();
            return true;
        } else if (itemId != R.id.action_invite_members) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            if (this.mActionMode == ActionMode.RETURN_SELECTED_USER_IDS) {
                Intent intent2 = new Intent();
                intent2.putExtra(str, this.mUserIdsToInvite);
                setResult(-1, intent2);
                finish();
            } else {
                inviteNoTchapContactsByEmail(this.mUserIdsToInvite, true);
            }
            return true;
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem findItem = menu.findItem(R.id.action_invite_members);
        int i = AnonymousClass16.$SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ActionMode[this.mActionMode.ordinal()];
        if (i == 1) {
            findItem.setTitle("");
            findItem.setEnabled(!this.mUserIdsToInvite.isEmpty());
        } else if (i == 2) {
            findItem.setTitle(R.string.tchap_room_invite_member_action);
            findItem.setEnabled(true);
        } else if (i != 3) {
            findItem.setEnabled(true);
        } else {
            findItem.setTitle(R.string.invite);
            findItem.setEnabled(!this.mUserIdsToInvite.isEmpty());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void onBackPressed() {
        if (AnonymousClass16.$SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ActionMode[this.mActionMode.ordinal()] != 2) {
            super.onBackPressed();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_OUT_SELECTED_USER_IDS, this.mUserIdsToInvite);
        setResult(0, intent);
        finish();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mSession.getDataHandler().addListener(this.mEventsListener);
        ContactsManager.getInstance().addListener(this.mContactsListener);
        runOnUiThread(new Runnable() {
            public void run() {
                VectorRoomInviteMembersActivity.this.onPatternUpdate(false);
            }
        });
        this.mSearchView.setQuery("", false);
        this.mParentLayout.requestFocus();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mSession.getDataHandler().removeListener(this.mEventsListener);
        ContactsManager.getInstance().removeListener(this.mContactsListener);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (strArr.length == 0) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onRequestPermissionsResult(): cancelled ");
            sb.append(i);
            Log.d(str, sb.toString());
        } else if (i != 567) {
        } else {
            if (iArr[0] == 0) {
                Log.d(LOG_TAG, "## onRequestPermissionsResult(): READ_CONTACTS permission granted");
                ContactsManager.getInstance().refreshLocalContactsSnapshot();
                onPatternUpdate(false);
                return;
            }
            Log.d(LOG_TAG, "## onRequestPermissionsResult(): READ_CONTACTS permission not granted");
            Toast.makeText(this, R.string.missing_permissions_warning, 0).show();
        }
    }

    /* access modifiers changed from: protected */
    public void onPatternUpdate(boolean z) {
        String charSequence = this.mSearchView.getQuery().toString();
        if (!this.mAdapter.isKnownMembersInitialized()) {
            showWaitingView();
        }
        if (!ContactsManager.getInstance().didPopulateLocalContacts()) {
            Log.d(LOG_TAG, "## onPatternUpdate() : The local contacts are not yet populated");
            this.mAdapter.reset();
            showWaitingView();
            return;
        }
        this.mAdapter.setSearchedPattern(charSequence, null, new OnParticipantsSearchListener() {
            public void onSearchEnd(int i) {
                if (VectorRoomInviteMembersActivity.this.mListView != null) {
                    VectorRoomInviteMembersActivity.this.mListView.post(new Runnable() {
                        public void run() {
                            VectorRoomInviteMembersActivity.this.hideWaitingView();
                        }
                    });
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void onSelectedParticipant(ParticipantAdapterItem participantAdapterItem) {
        if (participantAdapterItem.mIsValid) {
            String str = participantAdapterItem.mUserId;
            if (this.mUserIdsToInvite.contains(str)) {
                this.mUserIdsToInvite.remove(str);
                this.mAdapter.setSelectedUserIds(this.mUserIdsToInvite);
                this.mAdapter.notifyDataSetChanged();
                invalidateOptionsMenu();
            } else if (Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
                onSelectedEmails(Arrays.asList(new String[]{str}));
            } else {
                this.mUserIdsToInvite.add(str);
                this.mAdapter.setSelectedUserIds(this.mUserIdsToInvite);
                this.mAdapter.notifyDataSetChanged();
                invalidateOptionsMenu();
            }
        } else {
            DinsicUtils.editContact(this, getApplicationContext(), participantAdapterItem);
        }
    }

    /* access modifiers changed from: private */
    public void displayDialogToInviteByEmail() {
        View inflate = getLayoutInflater().inflate(R.layout.dialog_invite_by_id, null);
        Builder view = new Builder(this).setTitle((int) R.string.people_search_invite_by_id_dialog_title).setView(inflate);
        final VectorAutoCompleteTextView vectorAutoCompleteTextView = (VectorAutoCompleteTextView) inflate.findViewById(R.id.invite_by_id_edit_text);
        final AlertDialog show = view.setPositiveButton(this.mActionMode == ActionMode.SEND_INVITE ? R.string.invite : R.string.add, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) null).show();
        final Button button = show.getButton(-1);
        if (button != null) {
            button.setEnabled(false);
            button.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    String trim = vectorAutoCompleteTextView.getText().toString().toLowerCase(VectorLocale.INSTANCE.getApplicationLocale()).trim();
                    ArrayList arrayList = new ArrayList();
                    Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(trim);
                    while (matcher.find()) {
                        try {
                            arrayList.add(trim.substring(matcher.start(0), matcher.end(0)));
                        } catch (Exception e) {
                            String access$600 = VectorRoomInviteMembersActivity.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## displayDialogToInviteByEmail() ");
                            sb.append(e.getMessage());
                            Log.e(access$600, sb.toString());
                        }
                    }
                    if (VectorRoomInviteMembersActivity.this.mActionMode == ActionMode.SEND_INVITE) {
                        VectorRoomInviteMembersActivity.this.handleIndividualInviteByEmail(arrayList);
                    } else if (VectorRoomInviteMembersActivity.this.mActionMode == ActionMode.RETURN_SELECTED_USER_IDS) {
                        VectorRoomInviteMembersActivity.this.onSelectedEmails(arrayList);
                    } else {
                        Log.e(VectorRoomInviteMembersActivity.LOG_TAG, "## displayDialogToInviteByEmail() unsupported case");
                    }
                    show.dismiss();
                }
            });
        }
        vectorAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (button != null) {
                    button.setEnabled(Patterns.EMAIL_ADDRESS.matcher(vectorAutoCompleteTextView.getText().toString()).find());
                }
            }
        });
    }

    private void hideKeyboard() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    /* access modifiers changed from: private */
    public void handleIndividualInviteByEmail(final List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            arrayList.add("email");
        }
        showWaitingView();
        new TchapThirdPidRestClient(this.mSession.getHomeServerConfig()).bulkLookup(list, arrayList, new ApiCallback<List<String>>() {
            public void onSuccess(List<String> list) {
                String access$600 = VectorRoomInviteMembersActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("bulkLookup: success ");
                sb.append(list.size());
                Log.e(access$600, sb.toString());
                int i = 0;
                while (i < list.size()) {
                    String str = (String) list.get(i);
                    String str2 = (String) list.get(i);
                    if (TextUtils.isEmpty(str2)) {
                        i++;
                    } else if (DinsicUtils.isDirectChatRoomAlreadyExist(str2, VectorRoomInviteMembersActivity.this.mSession, true) != null) {
                        list.remove(i);
                        list.remove(i);
                        Toast.makeText(VectorRoomInviteMembersActivity.this, VectorRoomInviteMembersActivity.this.getString(R.string.tchap_discussion_already_exist, new Object[]{str}), 1).show();
                    } else {
                        list.remove(i);
                        list.remove(i);
                        VectorRoomInviteMembersActivity.this.inviteDiscoveredTchapUser(str2, str);
                    }
                }
                VectorRoomInviteMembersActivity.this.hideWaitingView();
                if (!list.isEmpty()) {
                    VectorRoomInviteMembersActivity vectorRoomInviteMembersActivity = VectorRoomInviteMembersActivity.this;
                    vectorRoomInviteMembersActivity.inviteNoTchapContactsByEmail(list, vectorRoomInviteMembersActivity.mUserIdsToInvite.isEmpty());
                }
            }

            private void onError(String str) {
                String access$600 = VectorRoomInviteMembersActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## bulkLookup: failed ");
                sb.append(str);
                Log.e(access$600, sb.toString());
                VectorRoomInviteMembersActivity.this.hideWaitingView();
                if (TextUtils.isEmpty(str)) {
                    str = VectorRoomInviteMembersActivity.this.getString(R.string.tchap_error_message_default);
                }
                Toast.makeText(VectorRoomInviteMembersActivity.this, str, 1).show();
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

    /* access modifiers changed from: private */
    public void inviteNoTchapContactsByEmail(List<String> list, final boolean z) {
        if (this.mCount != 0) {
            Log.e(LOG_TAG, "##inviteNoTchapContactsByEmail : invitations are being sent");
            return;
        }
        showWaitingView();
        this.mCount = list.size();
        this.mSuccessCount = 0;
        for (final String str : list) {
            TchapLoginActivity.discoverTchapPlatform((Activity) this, str, (ApiCallback<Platform>) new ApiCallback<Platform>() {
                private void onError(String str) {
                    Toast.makeText(VectorRoomInviteMembersActivity.this, str, 1).show();
                    if (VectorRoomInviteMembersActivity.access$1106(VectorRoomInviteMembersActivity.this) == 0) {
                        VectorRoomInviteMembersActivity.this.onNoTchapInviteDone(z);
                    }
                }

                public void onSuccess(Platform platform) {
                    if (platform.hs == null || platform.hs.isEmpty()) {
                        onError(VectorRoomInviteMembersActivity.this.getString(R.string.tchap_invite_unreachable_message, new Object[]{str}));
                        return;
                    }
                    final AnonymousClass1 r0 = new ApiCallback<String>() {
                        public void onSuccess(String str) {
                            VectorRoomInviteMembersActivity.this.mSuccessCount = VectorRoomInviteMembersActivity.this.mSuccessCount + 1;
                            if (VectorRoomInviteMembersActivity.access$1106(VectorRoomInviteMembersActivity.this) == 0) {
                                VectorRoomInviteMembersActivity.this.onNoTchapInviteDone(z);
                            }
                        }

                        private void onError(String str) {
                            String access$600 = VectorRoomInviteMembersActivity.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("##inviteNoTchapUserByEmail failed : ");
                            sb.append(str);
                            Log.e(access$600, sb.toString());
                            new Builder(VectorRoomInviteMembersActivity.this).setMessage((CharSequence) VectorRoomInviteMembersActivity.this.getString(R.string.tchap_send_invite_failed, new Object[]{str})).setPositiveButton((int) R.string.ok, (DialogInterface.OnClickListener) null).setOnDismissListener(new OnDismissListener() {
                                public void onDismiss(DialogInterface dialogInterface) {
                                    if (VectorRoomInviteMembersActivity.access$1106(VectorRoomInviteMembersActivity.this) == 0) {
                                        VectorRoomInviteMembersActivity.this.onNoTchapInviteDone(z);
                                    }
                                }
                            }).show();
                        }

                        public void onNetworkError(Exception exc) {
                            onError(exc.getLocalizedMessage());
                        }

                        public void onMatrixError(MatrixError matrixError) {
                            if (!MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                                onError(matrixError.getLocalizedMessage());
                            } else if (VectorRoomInviteMembersActivity.this.mCount != 0) {
                                VectorRoomInviteMembersActivity.this.mCount = 0;
                                VectorRoomInviteMembersActivity.this.getConsentNotGivenHelper().displayDialog(matrixError);
                            }
                        }

                        public void onUnexpectedError(Exception exc) {
                            onError(exc.getLocalizedMessage());
                        }
                    };
                    final Room isDirectChatRoomAlreadyExist = DinsicUtils.isDirectChatRoomAlreadyExist(str, VectorRoomInviteMembersActivity.this.mSession, false);
                    if (isDirectChatRoomAlreadyExist == null) {
                        DinsicUtils.createDirectChat(VectorRoomInviteMembersActivity.this.mSession, str, r0);
                    } else if (DinsicUtils.isExternalTchapServer(platform.hs)) {
                        final AnonymousClass2 r11 = new ApiCallback<Void>() {
                            public void onSuccess(Void voidR) {
                                Log.d(VectorRoomInviteMembersActivity.LOG_TAG, "an empty discussion has been left (to renew the invite)");
                                DinsicUtils.createDirectChat(VectorRoomInviteMembersActivity.this.mSession, str, r0);
                            }

                            private void onError(String str) {
                                String access$600 = VectorRoomInviteMembersActivity.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("failed to leave discussion");
                                sb.append(str);
                                Log.e(access$600, sb.toString());
                                Toast.makeText(VectorRoomInviteMembersActivity.this, VectorRoomInviteMembersActivity.this.getString(R.string.tchap_send_invite_failed, new Object[]{str}), 1).show();
                                if (VectorRoomInviteMembersActivity.access$1106(VectorRoomInviteMembersActivity.this) == 0) {
                                    VectorRoomInviteMembersActivity.this.onNoTchapInviteDone(z);
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
                        };
                        String str = null;
                        ArrayList arrayList = new ArrayList(isDirectChatRoomAlreadyExist.getState().thirdPartyInvites());
                        if (arrayList.size() != 0) {
                            str = ((RoomThirdPartyInvite) arrayList.get(0)).token;
                        }
                        String str2 = str;
                        if (str2 != null) {
                            VectorRoomInviteMembersActivity.this.mSession.getRoomsApiClient().sendStateEvent(isDirectChatRoomAlreadyExist.getRoomId(), Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE, str2, new HashMap(), new ApiCallback<Void>() {
                                public void onSuccess(Void voidR) {
                                    isDirectChatRoomAlreadyExist.leave(r11);
                                }

                                private void onError(String str) {
                                    String access$600 = VectorRoomInviteMembersActivity.LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("failed to revoke 3pid invite");
                                    sb.append(str);
                                    Log.e(access$600, sb.toString());
                                    Toast.makeText(VectorRoomInviteMembersActivity.this, VectorRoomInviteMembersActivity.this.getString(R.string.tchap_invite_already_send_message, new Object[]{str}), 1).show();
                                    if (VectorRoomInviteMembersActivity.access$1106(VectorRoomInviteMembersActivity.this) == 0) {
                                        VectorRoomInviteMembersActivity.this.onNoTchapInviteDone(z);
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
                        } else {
                            Log.d(VectorRoomInviteMembersActivity.LOG_TAG, "There is no invite to revoke");
                            isDirectChatRoomAlreadyExist.leave(r11);
                        }
                    } else {
                        Toast.makeText(VectorRoomInviteMembersActivity.this, VectorRoomInviteMembersActivity.this.getString(R.string.tchap_invite_already_send_message, new Object[]{str}), 1).show();
                        if (VectorRoomInviteMembersActivity.access$1106(VectorRoomInviteMembersActivity.this) == 0) {
                            VectorRoomInviteMembersActivity.this.onNoTchapInviteDone(z);
                        }
                    }
                }

                public void onNetworkError(Exception exc) {
                    onError(VectorRoomInviteMembersActivity.this.getString(R.string.tchap_send_invite_network_error));
                }

                public void onMatrixError(MatrixError matrixError) {
                    onError(VectorRoomInviteMembersActivity.this.getString(R.string.tchap_invite_unreachable_message, new Object[]{str}));
                }

                public void onUnexpectedError(Exception exc) {
                    onError(VectorRoomInviteMembersActivity.this.getString(R.string.tchap_invite_unreachable_message, new Object[]{str}));
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void inviteDiscoveredTchapUser(String str, final String str2) {
        DinsicUtils.createDirectChat(this.mSession, str, new ApiCallback<String>() {
            public void onSuccess(String str) {
                Toast.makeText(VectorRoomInviteMembersActivity.this, VectorRoomInviteMembersActivity.this.getString(R.string.tchap_start_discussion_with_discovered_user, new Object[]{str2}), 1).show();
            }

            private void onError(String str) {
                String access$600 = VectorRoomInviteMembersActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("##inviteDiscoveredTchapUser failed : ");
                sb.append(str);
                Log.e(access$600, sb.toString());
                new Builder(VectorRoomInviteMembersActivity.this).setMessage((CharSequence) VectorRoomInviteMembersActivity.this.getString(R.string.tchap_send_invite_failed, new Object[]{str2})).setPositiveButton((int) R.string.ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
            }

            public void onNetworkError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                    VectorRoomInviteMembersActivity.this.getConsentNotGivenHelper().displayDialog(matrixError);
                } else {
                    onError(matrixError.getLocalizedMessage());
                }
            }

            public void onUnexpectedError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }
        });
    }

    /* access modifiers changed from: private */
    public void onNoTchapInviteDone(boolean z) {
        hideWaitingView();
        if (this.mSuccessCount > 0) {
            displayLocalNotification();
        }
        if (z) {
            finish();
        }
    }

    private void displayLocalNotification() {
        Log.e(LOG_TAG, "##inviteNoTchapUserByEmail : display notification");
        Resources resources = getResources();
        int i = this.mSuccessCount;
        SpannableString spannableString = new SpannableString(resources.getQuantityString(R.plurals.tchap_succes_invite_notification, i, new Object[]{Integer.valueOf(i)}));
        StringBuilder sb = new StringBuilder();
        sb.append(spannableString);
        sb.append(" \n");
        sb.append(getString(R.string.tchap_send_invite_confirmation));
        Toast.makeText(this, sb.toString(), 1).show();
    }

    /* access modifiers changed from: private */
    public void onSelectedEmails(List<String> list) {
        if (this.mCount != 0) {
            Log.e(LOG_TAG, "##onSelectedEmails: a process is already in progress");
            return;
        }
        showWaitingView();
        this.mCount = list.size();
        final ArrayList arrayList = new ArrayList();
        final String homeServerNameFromMXIdentifier = DinsicUtils.getHomeServerNameFromMXIdentifier(this.mSession.getMyUserId());
        for (final String str : list) {
            TchapLoginActivity.discoverTchapPlatform((Activity) this, str, (ApiCallback<Platform>) new ApiCallback<Platform>() {
                private void onError(String str, String str2) {
                    String access$600 = VectorRoomInviteMembersActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("##onSelectedEmails: ");
                    sb.append(str2);
                    Log.e(access$600, sb.toString());
                    new Builder(VectorRoomInviteMembersActivity.this).setTitle((CharSequence) str).setMessage((CharSequence) str2).setPositiveButton((int) R.string.ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (VectorRoomInviteMembersActivity.access$1106(VectorRoomInviteMembersActivity.this) == 0) {
                                VectorRoomInviteMembersActivity.this.hideWaitingView();
                                if (!arrayList.isEmpty()) {
                                    VectorRoomInviteMembersActivity.this.addEmailsToInvite(arrayList);
                                }
                            }
                        }
                    }).show();
                }

                /* JADX WARNING: Removed duplicated region for block: B:26:0x00a9  */
                public void onSuccess(Platform platform) {
                    if (platform.hs == null || platform.hs.isEmpty()) {
                        onError(null, VectorRoomInviteMembersActivity.this.getString(R.string.tchap_invite_unreachable_message, new Object[]{str}));
                        return;
                    }
                    int i = AnonymousClass16.$SwitchMap$im$vector$activity$VectorRoomInviteMembersActivity$ContactsFilter[VectorRoomInviteMembersActivity.this.mContactsFilter.ordinal()];
                    if (i != 1) {
                        if (i != 2) {
                            if (i != 3) {
                                if (i != 4) {
                                    Log.e(VectorRoomInviteMembersActivity.LOG_TAG, "## onSelectedEmails() unsupported case");
                                }
                            } else if (TextUtils.equals(platform.hs, homeServerNameFromMXIdentifier)) {
                                arrayList.add(str);
                            } else {
                                String homeServerDisplayNameFromMXIdentifier = DinsicUtils.getHomeServerDisplayNameFromMXIdentifier(VectorRoomInviteMembersActivity.this.mSession.getMyUserId());
                                onError(VectorRoomInviteMembersActivity.this.getString(R.string.tchap_invite_unauthorized_title_unfederated_room, new Object[]{homeServerDisplayNameFromMXIdentifier}), VectorRoomInviteMembersActivity.this.getString(R.string.tchap_invite_unauthorized_message, new Object[]{str}));
                                return;
                            }
                        } else if (!DinsicUtils.isExternalTchapServer(platform.hs)) {
                            arrayList.add(str);
                        } else {
                            onError(VectorRoomInviteMembersActivity.this.getString(R.string.tchap_invite_unauthorized_title_restricted_room), VectorRoomInviteMembersActivity.this.getString(R.string.tchap_invite_unauthorized_message, new Object[]{str}));
                            return;
                        }
                        if (VectorRoomInviteMembersActivity.access$1106(VectorRoomInviteMembersActivity.this) == 0) {
                            VectorRoomInviteMembersActivity.this.hideWaitingView();
                            if (!arrayList.isEmpty()) {
                                VectorRoomInviteMembersActivity.this.addEmailsToInvite(arrayList);
                            }
                        }
                    }
                    arrayList.add(str);
                    if (VectorRoomInviteMembersActivity.access$1106(VectorRoomInviteMembersActivity.this) == 0) {
                    }
                }

                public void onNetworkError(Exception exc) {
                    onError(null, VectorRoomInviteMembersActivity.this.getString(R.string.tchap_send_invite_network_error));
                }

                public void onMatrixError(MatrixError matrixError) {
                    onError(null, VectorRoomInviteMembersActivity.this.getString(R.string.tchap_invite_unreachable_message, new Object[]{str}));
                }

                public void onUnexpectedError(Exception exc) {
                    onError(null, VectorRoomInviteMembersActivity.this.getString(R.string.tchap_invite_unreachable_message, new Object[]{str}));
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void addEmailsToInvite(List<String> list) {
        for (String str : list) {
            if (!this.mUserIdsToInvite.contains(str)) {
                this.mUserIdsToInvite.add(str);
            }
        }
        this.mAdapter.setSelectedUserIds(this.mUserIdsToInvite);
        this.mAdapter.notifyDataSetChanged();
        this.mAdapter.refresh(null, null);
        invalidateOptionsMenu();
    }
}
