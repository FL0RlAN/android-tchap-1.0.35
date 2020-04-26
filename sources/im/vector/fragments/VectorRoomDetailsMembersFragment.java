package im.vector.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.sdk.session.room.model.RoomAccessRulesKt;
import fr.gouv.tchap.util.DinsicUtils;
import fr.gouv.tchap.util.DinumUtilsKt;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.MXCActionBarActivity;
import im.vector.activity.VectorMemberDetailsActivity;
import im.vector.activity.VectorRoomInviteMembersActivity;
import im.vector.activity.VectorRoomInviteMembersActivity.ContactsFilter;
import im.vector.adapters.ParticipantAdapterItem;
import im.vector.adapters.VectorRoomDetailsMembersAdapter;
import im.vector.adapters.VectorRoomDetailsMembersAdapter.OnParticipantsListener;
import im.vector.adapters.VectorRoomDetailsMembersAdapter.OnRoomMembersSearchListener;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.RoomUtils;
import im.vector.util.VectorUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.jetbrains.anko.ToastsKt;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.PowerLevels;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.pid.RoomThirdPartyInvite;

public class VectorRoomDetailsMembersFragment extends VectorBaseFragment {
    private static final int GET_MENTION_REQUEST_CODE = 666;
    private static final int INVITE_USER_REQUEST_CODE = 777;
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorRoomDetailsMembersFragment.class.getSimpleName();
    private static final boolean REFRESH_FORCED = true;
    private static final boolean REFRESH_NOT_FORCED = false;
    /* access modifiers changed from: private */
    public VectorRoomDetailsMembersAdapter mAdapter;
    private View mAddMembersButton;
    /* access modifiers changed from: private */
    public ImageView mClearSearchImageView;
    /* access modifiers changed from: private */
    public final ApiCallback<Void> mDefaultCallBack = new ApiCallback<Void>() {
        public void onSuccess(Void voidR) {
            if (VectorRoomDetailsMembersFragment.this.getActivity() != null) {
                VectorRoomDetailsMembersFragment.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        VectorRoomDetailsMembersFragment.this.mProgressView.setVisibility(8);
                    }
                });
            }
        }

        public void onError(final String str) {
            if (VectorRoomDetailsMembersFragment.this.getActivity() != null) {
                VectorRoomDetailsMembersFragment.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        VectorRoomDetailsMembersFragment.this.mProgressView.setVisibility(8);
                        Toast.makeText(VectorRoomDetailsMembersFragment.this.getActivity(), str, 0).show();
                    }
                });
            }
        }

        public void onNetworkError(Exception exc) {
            onError(exc.getLocalizedMessage());
        }

        public void onMatrixError(final MatrixError matrixError) {
            if (VectorRoomDetailsMembersFragment.this.getVectorActivity() != null) {
                if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                    VectorRoomDetailsMembersFragment.this.getVectorActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            VectorRoomDetailsMembersFragment.this.mProgressView.setVisibility(8);
                            VectorRoomDetailsMembersFragment.this.getVectorActivity().getConsentNotGivenHelper().displayDialog(matrixError);
                        }
                    });
                    return;
                }
            }
            onError(matrixError.getLocalizedMessage());
        }

        public void onUnexpectedError(Exception exc) {
            onError(exc.getLocalizedMessage());
        }
    };
    private final MXEventListener mEventListener = new MXEventListener() {
        public void onLiveEvent(final Event event, RoomState roomState) {
            VectorRoomDetailsMembersFragment.this.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    String type = event.getType();
                    if ("m.room.member".equals(type) || Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE.equals(type) || Event.EVENT_TYPE_STATE_ROOM_POWER_LEVELS.equals(type)) {
                        VectorRoomDetailsMembersFragment.this.refreshRoomMembersList(VectorRoomDetailsMembersFragment.this.mPatternValue, true);
                    }
                }
            });
        }

        public void onRoomFlush(String str) {
            VectorRoomDetailsMembersFragment.this.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    VectorRoomDetailsMembersFragment.this.refreshRoomMembersList(VectorRoomDetailsMembersFragment.this.mPatternValue, true);
                }
            });
        }

        public void onPresenceUpdate(Event event, final User user) {
            if (VectorRoomDetailsMembersFragment.this.getActivity() != null) {
                VectorRoomDetailsMembersFragment.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (VectorRoomDetailsMembersFragment.this.mAdapter.getUserIdsList().indexOf(user.user_id) >= 0) {
                            VectorRoomDetailsMembersFragment.this.delayedUpdateRoomMembersDataModel();
                        }
                    }
                });
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mIsInvitingNewMembers;
    /* access modifiers changed from: private */
    public Map<Integer, Boolean> mIsListViewGroupExpandedMap;
    /* access modifiers changed from: private */
    public boolean mIsMultiSelectionMode;
    /* access modifiers changed from: private */
    public ExpandableListView mParticipantsListView;
    /* access modifiers changed from: private */
    public EditText mPatternToSearchEditText;
    /* access modifiers changed from: private */
    public String mPatternValue;
    /* access modifiers changed from: private */
    public View mProgressView;
    /* access modifiers changed from: private */
    public Timer mRefreshTimer;
    /* access modifiers changed from: private */
    public TimerTask mRefreshTimerTask;
    private MenuItem mRemoveMembersMenuItem;
    /* access modifiers changed from: private */
    public Room mRoom;
    private final OnRoomMembersSearchListener mSearchListener = new OnRoomMembersSearchListener() {
        public void onSearchEnd(final int i, boolean z, final String str) {
            VectorRoomDetailsMembersFragment.this.mParticipantsListView.post(new Runnable() {
                public void run() {
                    if (!VectorRoomDetailsMembersFragment.this.mIsInvitingNewMembers) {
                        VectorRoomDetailsMembersFragment.this.mProgressView.setVisibility(8);
                    }
                    if (TextUtils.isEmpty(str)) {
                        if (i == 0) {
                            VectorRoomDetailsMembersFragment.this.mSearchNoResultTextView.setVisibility(0);
                        } else {
                            VectorRoomDetailsMembersFragment.this.mSearchNoResultTextView.setVisibility(8);
                        }
                        if (TextUtils.isEmpty(VectorRoomDetailsMembersFragment.this.mPatternValue)) {
                            VectorRoomDetailsMembersFragment.this.updateListExpandingState();
                        } else {
                            VectorRoomDetailsMembersFragment.this.forceListInExpandingState();
                            VectorRoomDetailsMembersFragment.this.mClearSearchImageView.setVisibility(0);
                        }
                        VectorRoomDetailsMembersFragment.this.mParticipantsListView.post(new Runnable() {
                            public void run() {
                                VectorRoomDetailsMembersFragment.this.mParticipantsListView.setSelection(0);
                            }
                        });
                        return;
                    }
                    ToastsKt.toast((Context) VectorRoomDetailsMembersFragment.this.getActivity(), (CharSequence) str);
                }
            });
        }
    };
    /* access modifiers changed from: private */
    public TextView mSearchNoResultTextView;
    /* access modifiers changed from: private */
    public MXSession mSession;
    private MenuItem mSwitchDeletionMenuItem;
    private final TextWatcher mTextWatcherListener = new TextWatcher() {
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void afterTextChanged(Editable editable) {
            final String obj = VectorRoomDetailsMembersFragment.this.mPatternToSearchEditText.getText().toString();
            if (TextUtils.isEmpty(obj)) {
                VectorRoomDetailsMembersFragment.this.mClearSearchImageView.setVisibility(4);
                VectorRoomDetailsMembersFragment.this.mPatternValue = null;
                VectorRoomDetailsMembersFragment vectorRoomDetailsMembersFragment = VectorRoomDetailsMembersFragment.this;
                vectorRoomDetailsMembersFragment.refreshRoomMembersList(vectorRoomDetailsMembersFragment.mPatternValue, false);
                return;
            }
            new Timer().schedule(new TimerTask() {
                public void run() {
                    String str;
                    try {
                        str = VectorRoomDetailsMembersFragment.this.mPatternToSearchEditText.getText().toString();
                    } catch (Exception e) {
                        String access$1200 = VectorRoomDetailsMembersFragment.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## afterTextChanged() failed ");
                        sb.append(e.getMessage());
                        Log.e(access$1200, sb.toString(), e);
                        str = null;
                    }
                    if (TextUtils.equals(str, obj) && VectorRoomDetailsMembersFragment.this.getActivity() != null) {
                        VectorRoomDetailsMembersFragment.this.mPatternValue = str;
                        VectorRoomDetailsMembersFragment.this.getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                VectorRoomDetailsMembersFragment.this.refreshRoomMembersList(VectorRoomDetailsMembersFragment.this.mPatternValue, false);
                            }
                        });
                    }
                }
            }, 100);
            VectorRoomDetailsMembersFragment.this.mClearSearchImageView.setVisibility(0);
        }
    };
    /* access modifiers changed from: private */
    public Handler mUIHandler;
    private final List<String> mUpdatedPresenceUserIds = new ArrayList();
    private View mViewHierarchy;

    private void processEditionMode() {
    }

    /* access modifiers changed from: private */
    public void resetActivityTitle() {
    }

    public static VectorRoomDetailsMembersFragment newInstance() {
        return new VectorRoomDetailsMembersFragment();
    }

    public void onPause() {
        super.onPause();
        Log.d("RoomDetailsMembersFragment", "## onPause()");
        EditText editText = this.mPatternToSearchEditText;
        if (editText != null) {
            editText.removeTextChangedListener(this.mTextWatcherListener);
        }
        Room room = this.mRoom;
        if (room != null) {
            room.removeEventListener(this.mEventListener);
        }
        if (this.mIsMultiSelectionMode) {
            toggleMultiSelectionMode();
        }
        Timer timer = this.mRefreshTimer;
        if (timer != null) {
            timer.cancel();
            this.mRefreshTimer = null;
            this.mRefreshTimerTask = null;
        }
    }

    public void onStop() {
        if (getActivity() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService("input_method");
            if (inputMethodManager != null) {
                EditText editText = this.mPatternToSearchEditText;
                if (editText != null) {
                    inputMethodManager.hideSoftInputFromWindow(editText.getApplicationWindowToken(), 0);
                    this.mPatternToSearchEditText.clearFocus();
                }
            }
        }
        super.onStop();
    }

    public void onResume() {
        super.onResume();
        Log.d("RoomDetailsMembersFragment", "## onResume()");
        EditText editText = this.mPatternToSearchEditText;
        if (editText != null) {
            editText.addTextChangedListener(this.mTextWatcherListener);
        }
        Room room = this.mRoom;
        if (room != null) {
            room.addEventListener(this.mEventListener);
        }
        refreshRoomMembersList(this.mPatternValue, false);
        updateListExpandingState();
        refreshMenuEntries();
        refreshMemberPresences();
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable(CommonActivityUtils.KEY_GROUPS_EXPANDED_STATE, (HashMap) this.mIsListViewGroupExpandedMap);
        bundle.putString(CommonActivityUtils.KEY_SEARCH_PATTERN, this.mPatternValue);
        Log.d("RoomDetailsMembersFragment", "## onSaveInstanceState()");
    }

    /* access modifiers changed from: private */
    public void updateListExpandingState() {
        ExpandableListView expandableListView = this.mParticipantsListView;
        if (expandableListView != null) {
            expandableListView.post(new Runnable() {
                public void run() {
                    int groupCount = VectorRoomDetailsMembersFragment.this.mParticipantsListView.getExpandableListAdapter().getGroupCount();
                    Boolean valueOf = Boolean.valueOf(true);
                    for (int i = 0; i < groupCount; i++) {
                        if (VectorRoomDetailsMembersFragment.this.mIsListViewGroupExpandedMap != null) {
                            valueOf = (Boolean) VectorRoomDetailsMembersFragment.this.mIsListViewGroupExpandedMap.get(Integer.valueOf(i));
                        }
                        if (valueOf == null || true == valueOf.booleanValue()) {
                            VectorRoomDetailsMembersFragment.this.mParticipantsListView.expandGroup(i);
                        } else {
                            VectorRoomDetailsMembersFragment.this.mParticipantsListView.collapseGroup(i);
                        }
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void forceListInExpandingState() {
        ExpandableListView expandableListView = this.mParticipantsListView;
        if (expandableListView != null) {
            expandableListView.post(new Runnable() {
                public void run() {
                    int groupCount = VectorRoomDetailsMembersFragment.this.mParticipantsListView.getExpandableListAdapter().getGroupCount();
                    for (int i = 0; i < groupCount; i++) {
                        VectorRoomDetailsMembersFragment.this.mParticipantsListView.expandGroup(i);
                    }
                }
            });
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mViewHierarchy = layoutInflater.inflate(R.layout.fragment_vector_add_participants, viewGroup, false);
        FragmentActivity activity = getActivity();
        if (activity instanceof MXCActionBarActivity) {
            MXCActionBarActivity mXCActionBarActivity = (MXCActionBarActivity) activity;
            this.mRoom = mXCActionBarActivity.getRoom();
            this.mSession = mXCActionBarActivity.getSession();
            MXSession mXSession = this.mSession;
            if (mXSession == null || !mXSession.isAlive()) {
                Log.e(LOG_TAG, "## onCreateView : the session is null -> kill the activity");
                if (getActivity() != null) {
                    getActivity().finish();
                }
            } else {
                finalizeInit();
            }
        }
        if (bundle != null) {
            this.mIsListViewGroupExpandedMap = (HashMap) bundle.getSerializable(CommonActivityUtils.KEY_GROUPS_EXPANDED_STATE);
            this.mPatternValue = bundle.getString(CommonActivityUtils.KEY_SEARCH_PATTERN, null);
        } else if (this.mIsListViewGroupExpandedMap == null) {
            this.mIsListViewGroupExpandedMap = new HashMap();
        }
        setHasOptionsMenu(true);
        this.mUIHandler = new Handler(Looper.getMainLooper());
        return this.mViewHierarchy;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if (!CommonActivityUtils.shouldRestartApp(getActivity())) {
            getActivity().getMenuInflater().inflate(R.menu.vector_room_details_add_people, menu);
            ThemeUtils.INSTANCE.tintMenuIcons(menu, ThemeUtils.INSTANCE.getColor(getContext(), R.attr.vctr_icon_tint_on_dark_action_bar_color));
            this.mRemoveMembersMenuItem = menu.findItem(R.id.ic_action_room_details_delete);
            this.mSwitchDeletionMenuItem = menu.findItem(R.id.ic_action_room_details_edition_mode);
            processEditionMode();
            refreshMenuEntries();
        }
    }

    public boolean onBackPressed() {
        if (!this.mIsMultiSelectionMode) {
            return false;
        }
        toggleMultiSelectionMode();
        return true;
    }

    private boolean isUserAdmin() {
        Room room = this.mRoom;
        if (room == null || this.mSession == null) {
            return false;
        }
        PowerLevels powerLevels = room.getState().getPowerLevels();
        if (powerLevels == null) {
            return false;
        }
        String myUserId = this.mSession.getMyUserId();
        if (myUserId == null || ((float) powerLevels.getUserPowerLevel(myUserId)) < 100.0f) {
            return false;
        }
        return true;
    }

    private boolean isUserAllowedToInvite() {
        Room room = this.mRoom;
        if (room == null || this.mSession == null) {
            return false;
        }
        PowerLevels powerLevels = room.getState().getPowerLevels();
        if (powerLevels == null) {
            return false;
        }
        String myUserId = this.mSession.getMyUserId();
        if (myUserId == null || powerLevels.getUserPowerLevel(myUserId) < powerLevels.invite) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void delayedUpdateRoomMembersDataModel() {
        Timer timer = this.mRefreshTimer;
        if (timer != null) {
            timer.cancel();
            this.mRefreshTimer = null;
            this.mRefreshTimerTask = null;
        }
        try {
            this.mRefreshTimer = new Timer();
            this.mRefreshTimerTask = new TimerTask() {
                public void run() {
                    VectorRoomDetailsMembersFragment.this.mUIHandler.post(new Runnable() {
                        public void run() {
                            if (VectorRoomDetailsMembersFragment.this.mRefreshTimer != null) {
                                VectorRoomDetailsMembersFragment.this.mRefreshTimer.cancel();
                            }
                            VectorRoomDetailsMembersFragment.this.mRefreshTimer = null;
                            VectorRoomDetailsMembersFragment.this.mRefreshTimerTask = null;
                            VectorRoomDetailsMembersFragment.this.mAdapter.updateRoomMembersDataModel(null);
                        }
                    });
                }
            };
            this.mRefreshTimer.schedule(this.mRefreshTimerTask, 1000);
        } catch (Throwable th) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## delayedUpdateRoomMembersDataModel() failed ");
            sb.append(th.getMessage());
            Log.e(str, sb.toString(), th);
            Timer timer2 = this.mRefreshTimer;
            if (timer2 != null) {
                timer2.cancel();
                this.mRefreshTimer = null;
            }
            this.mRefreshTimerTask = null;
            this.mAdapter.updateRoomMembersDataModel(null);
        }
    }

    /* access modifiers changed from: private */
    public void refreshMemberPresences() {
        int firstVisiblePosition = this.mParticipantsListView.getFirstVisiblePosition();
        int lastVisiblePosition = this.mParticipantsListView.getLastVisiblePosition() + 20;
        int count = this.mParticipantsListView.getCount();
        while (firstVisiblePosition <= lastVisiblePosition && firstVisiblePosition < count) {
            Object itemAtPosition = this.mParticipantsListView.getItemAtPosition(firstVisiblePosition);
            if (itemAtPosition instanceof ParticipantAdapterItem) {
                ParticipantAdapterItem participantAdapterItem = (ParticipantAdapterItem) itemAtPosition;
                if (this.mUpdatedPresenceUserIds.indexOf(participantAdapterItem.mUserId) < 0) {
                    this.mUpdatedPresenceUserIds.add(participantAdapterItem.mUserId);
                    VectorUtils.getUserOnlineStatus(getActivity(), this.mSession, participantAdapterItem.mUserId, new SimpleApiCallback<Void>() {
                        public void onSuccess(Void voidR) {
                            VectorRoomDetailsMembersFragment.this.mUIHandler.post(new Runnable() {
                                public void run() {
                                    VectorRoomDetailsMembersFragment.this.delayedUpdateRoomMembersDataModel();
                                }
                            });
                        }
                    });
                }
            }
            firstVisiblePosition++;
        }
    }

    private void refreshMenuEntries() {
        MenuItem menuItem = this.mRemoveMembersMenuItem;
        if (menuItem != null) {
            menuItem.setVisible(this.mIsMultiSelectionMode);
            if (this.mAddMembersButton != null) {
                int i = 8;
                if (RoomUtils.isDirectChat(this.mSession, this.mRoom.getRoomId()) || !isUserAllowedToInvite()) {
                    this.mAddMembersButton.setVisibility(8);
                } else {
                    View view = this.mAddMembersButton;
                    if (!this.mIsMultiSelectionMode) {
                        i = 0;
                    }
                    view.setVisibility(i);
                }
            }
        }
        MenuItem menuItem2 = this.mSwitchDeletionMenuItem;
        if (menuItem2 != null && menuItem2.isEnabled()) {
            this.mSwitchDeletionMenuItem.setVisible(false);
        }
    }

    /* access modifiers changed from: private */
    public void setActivityTitle(String str) {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle((CharSequence) str);
        }
    }

    /* access modifiers changed from: private */
    public void toggleMultiSelectionMode() {
        resetActivityTitle();
        this.mIsMultiSelectionMode = !this.mIsMultiSelectionMode;
        this.mAdapter.setMultiSelectionMode(this.mIsMultiSelectionMode);
        refreshMenuEntries();
        this.mAdapter.notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    public void kickUsers(final List<String> list) {
        View inflate = getLayoutInflater().inflate(R.layout.dialog_base_edit_text, null);
        final TextView textView = (TextView) inflate.findViewById(R.id.edit_text);
        textView.setHint(R.string.reason_hint);
        new Builder(getActivity()).setTitle((CharSequence) getResources().getQuantityString(R.plurals.room_participants_kick_prompt_msg, list.size())).setView(inflate).setPositiveButton((int) R.string.room_participants_action_kick, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                VectorRoomDetailsMembersFragment.this.kickUsersRecursive(list, textView.getText().toString(), 0);
            }
        }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
    }

    /* access modifiers changed from: private */
    public void kickUsersRecursive(final List<String> list, final String str, final int i) {
        if (i >= list.size()) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        VectorRoomDetailsMembersFragment.this.mProgressView.setVisibility(8);
                        if (VectorRoomDetailsMembersFragment.this.mIsMultiSelectionMode) {
                            VectorRoomDetailsMembersFragment.this.toggleMultiSelectionMode();
                            VectorRoomDetailsMembersFragment.this.resetActivityTitle();
                        }
                        VectorRoomDetailsMembersFragment.this.mAdapter.notifyDataSetChanged();
                    }
                });
            }
            return;
        }
        this.mRemoveMembersMenuItem.setEnabled(false);
        this.mSwitchDeletionMenuItem.setEnabled(false);
        this.mProgressView.setVisibility(0);
        this.mRoom.kick((String) list.get(i), str, new ApiCallback<Void>() {
            private void kickNext() {
                VectorRoomDetailsMembersFragment.this.kickUsersRecursive(list, str, i + 1);
            }

            public void onSuccess(Void voidR) {
                kickNext();
            }

            public void onNetworkError(Exception exc) {
                kickNext();
            }

            public void onMatrixError(MatrixError matrixError) {
                if (VectorRoomDetailsMembersFragment.this.getVectorActivity() != null) {
                    if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                        VectorRoomDetailsMembersFragment.this.getVectorActivity().getConsentNotGivenHelper().displayDialog(matrixError);
                        return;
                    }
                    Toast.makeText(VectorRoomDetailsMembersFragment.this.getActivity(), matrixError.getLocalizedMessage(), 0).show();
                    kickNext();
                    return;
                }
                kickNext();
            }

            public void onUnexpectedError(Exception exc) {
                kickNext();
            }
        });
    }

    /* access modifiers changed from: private */
    public void revokeInvite(RoomThirdPartyInvite roomThirdPartyInvite) {
        this.mProgressView.setVisibility(0);
        this.mSession.getRoomsApiClient().sendStateEvent(this.mRoom.getRoomId(), Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE, roomThirdPartyInvite.token, new HashMap(), new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                if (VectorRoomDetailsMembersFragment.this.isAdded()) {
                    VectorRoomDetailsMembersFragment.this.mProgressView.setVisibility(8);
                    VectorRoomDetailsMembersFragment.this.mAdapter.notifyDataSetChanged();
                }
            }

            private void onError(String str) {
                if (VectorRoomDetailsMembersFragment.this.isAdded()) {
                    VectorRoomDetailsMembersFragment.this.mProgressView.setVisibility(8);
                    Toast.makeText(VectorRoomDetailsMembersFragment.this.getActivity(), str, 0).show();
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
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.ic_action_room_details_delete) {
            kickUsers(this.mAdapter.getSelectedUserIds());
            return true;
        } else if (itemId != R.id.ic_action_room_details_edition_mode) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            toggleMultiSelectionMode();
            return true;
        }
    }

    private void finalizeInit() {
        MXMediaCache mediaCache = this.mSession.getMediaCache();
        this.mAddMembersButton = this.mViewHierarchy.findViewById(R.id.ly_invite_contacts_to_room);
        this.mAddMembersButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(VectorRoomDetailsMembersFragment.this.getActivity(), VectorRoomInviteMembersActivity.class);
                intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", VectorRoomDetailsMembersFragment.this.mSession.getMyUserId());
                intent.putExtra(VectorRoomInviteMembersActivity.EXTRA_ROOM_ID, VectorRoomDetailsMembersFragment.this.mRoom.getRoomId());
                boolean isSecure = DinumUtilsKt.isSecure();
                String str = VectorRoomInviteMembersActivity.EXTRA_CONTACTS_FILTER;
                if (isSecure) {
                    if (DinsicUtils.isFederatedRoom(VectorRoomDetailsMembersFragment.this.mRoom)) {
                        intent.putExtra(str, ContactsFilter.TCHAP_USERS_ONLY);
                    } else {
                        intent.putExtra(str, ContactsFilter.TCHAP_USERS_ONLY_WITHOUT_FEDERATION);
                    }
                } else if (!DinsicUtils.isFederatedRoom(VectorRoomDetailsMembersFragment.this.mRoom)) {
                    intent.putExtra(str, ContactsFilter.ALL_WITHOUT_FEDERATION);
                } else if (TextUtils.equals(DinsicUtils.getRoomAccessRule(VectorRoomDetailsMembersFragment.this.mRoom), RoomAccessRulesKt.RESTRICTED)) {
                    intent.putExtra(str, ContactsFilter.ALL_WITHOUT_EXTERNALS);
                } else {
                    intent.putExtra(str, ContactsFilter.ALL);
                }
                VectorRoomDetailsMembersFragment.this.getActivity().startActivityForResult(intent, VectorRoomDetailsMembersFragment.INVITE_USER_REQUEST_CODE);
            }
        });
        this.mPatternToSearchEditText = (EditText) this.mViewHierarchy.findViewById(R.id.search_value_edit_text);
        this.mClearSearchImageView = (ImageView) this.mViewHierarchy.findViewById(R.id.clear_search_icon_image_view);
        this.mSearchNoResultTextView = (TextView) this.mViewHierarchy.findViewById(R.id.search_no_results_text_view);
        this.mPatternToSearchEditText.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 3 && i != 2 && i != 6) {
                    return false;
                }
                String access$000 = VectorRoomDetailsMembersFragment.this.mPatternValue;
                VectorRoomDetailsMembersFragment vectorRoomDetailsMembersFragment = VectorRoomDetailsMembersFragment.this;
                vectorRoomDetailsMembersFragment.mPatternValue = vectorRoomDetailsMembersFragment.mPatternToSearchEditText.getText().toString();
                if (TextUtils.isEmpty(VectorRoomDetailsMembersFragment.this.mPatternValue.trim())) {
                    VectorRoomDetailsMembersFragment.this.mPatternValue = access$000;
                } else {
                    VectorRoomDetailsMembersFragment vectorRoomDetailsMembersFragment2 = VectorRoomDetailsMembersFragment.this;
                    vectorRoomDetailsMembersFragment2.refreshRoomMembersList(vectorRoomDetailsMembersFragment2.mPatternValue, false);
                }
                return true;
            }
        });
        this.mClearSearchImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                VectorRoomDetailsMembersFragment.this.mPatternToSearchEditText.setText("");
                VectorRoomDetailsMembersFragment.this.mPatternValue = null;
                VectorRoomDetailsMembersFragment vectorRoomDetailsMembersFragment = VectorRoomDetailsMembersFragment.this;
                vectorRoomDetailsMembersFragment.refreshRoomMembersList(vectorRoomDetailsMembersFragment.mPatternValue, false);
                VectorRoomDetailsMembersFragment.this.forceListInExpandingState();
            }
        });
        this.mProgressView = this.mViewHierarchy.findViewById(R.id.add_participants_progress_view);
        this.mParticipantsListView = (ExpandableListView) this.mViewHierarchy.findViewById(R.id.room_details_members_exp_list_view);
        VectorRoomDetailsMembersAdapter vectorRoomDetailsMembersAdapter = new VectorRoomDetailsMembersAdapter(getActivity(), R.layout.adapter_item_vector_add_participants, R.layout.adapter_item_vector_recent_header, this.mSession, this.mRoom.getRoomId(), mediaCache);
        this.mAdapter = vectorRoomDetailsMembersAdapter;
        this.mParticipantsListView.setAdapter(this.mAdapter);
        this.mParticipantsListView.setGroupIndicator(null);
        this.mParticipantsListView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                VectorRoomDetailsMembersFragment.this.refreshMemberPresences();
            }
        });
        this.mAdapter.setOnParticipantsListener(new OnParticipantsListener() {
            public void onClick(ParticipantAdapterItem participantAdapterItem) {
                Intent intent = new Intent(VectorRoomDetailsMembersFragment.this.getActivity(), VectorMemberDetailsActivity.class);
                intent.putExtra("EXTRA_ROOM_ID", VectorRoomDetailsMembersFragment.this.mRoom.getRoomId());
                intent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_ID, participantAdapterItem.mUserId);
                intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", VectorRoomDetailsMembersFragment.this.mSession.getCredentials().userId);
                VectorRoomDetailsMembersFragment.this.getActivity().startActivityForResult(intent, VectorRoomDetailsMembersFragment.GET_MENTION_REQUEST_CODE);
            }

            public void onSelectUserId(String str) {
                List selectedUserIds = VectorRoomDetailsMembersFragment.this.mAdapter.getSelectedUserIds();
                if (selectedUserIds.size() != 0) {
                    VectorRoomDetailsMembersFragment vectorRoomDetailsMembersFragment = VectorRoomDetailsMembersFragment.this;
                    vectorRoomDetailsMembersFragment.setActivityTitle(vectorRoomDetailsMembersFragment.getResources().getQuantityString(R.plurals.room_details_selected, selectedUserIds.size(), new Object[]{Integer.valueOf(selectedUserIds.size())}));
                    return;
                }
                VectorRoomDetailsMembersFragment.this.resetActivityTitle();
            }

            public void onRemoveClick(final ParticipantAdapterItem participantAdapterItem) {
                if (participantAdapterItem.mRoomThirdPartyInvite != null) {
                    new Builder(VectorRoomDetailsMembersFragment.this.getActivity()).setTitle((int) R.string.dialog_title_confirmation).setMessage((CharSequence) VectorRoomDetailsMembersFragment.this.getString(R.string.room_participants_remove_3pid_invite_prompt_msg)).setPositiveButton((int) R.string.remove, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            VectorRoomDetailsMembersFragment.this.revokeInvite(participantAdapterItem.mRoomThirdPartyInvite);
                        }
                    }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
                    return;
                }
                new Builder(VectorRoomDetailsMembersFragment.this.getActivity()).setTitle((int) R.string.dialog_title_confirmation).setMessage((CharSequence) VectorRoomDetailsMembersFragment.this.getString(R.string.room_participants_remove_prompt_msg, participantAdapterItem.mDisplayName)).setPositiveButton((int) R.string.remove, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VectorRoomDetailsMembersFragment.this.kickUsers(Collections.singletonList(participantAdapterItem.mUserId));
                    }
                }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
            }

            public void onLeaveClick() {
                RoomUtils.showLeaveRoomDialog(VectorRoomDetailsMembersFragment.this.getActivity(), VectorRoomDetailsMembersFragment.this.mSession, VectorRoomDetailsMembersFragment.this.mRoom, new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VectorRoomDetailsMembersFragment.this.mProgressView.setVisibility(0);
                        VectorRoomDetailsMembersFragment.this.mRoom.leave(new ApiCallback<Void>() {
                            public void onSuccess(Void voidR) {
                                if (VectorRoomDetailsMembersFragment.this.getActivity() != null) {
                                    VectorRoomDetailsMembersFragment.this.getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            VectorRoomDetailsMembersFragment.this.getActivity().finish();
                                        }
                                    });
                                }
                            }

                            private void onError(final String str) {
                                if (VectorRoomDetailsMembersFragment.this.getActivity() != null) {
                                    VectorRoomDetailsMembersFragment.this.getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            VectorRoomDetailsMembersFragment.this.mProgressView.setVisibility(8);
                                            Toast.makeText(VectorRoomDetailsMembersFragment.this.getActivity(), str, 0).show();
                                        }
                                    });
                                }
                            }

                            public void onNetworkError(Exception exc) {
                                onError(exc.getLocalizedMessage());
                            }

                            public void onMatrixError(final MatrixError matrixError) {
                                if (VectorRoomDetailsMembersFragment.this.getVectorActivity() != null) {
                                    if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                                        VectorRoomDetailsMembersFragment.this.getVectorActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                VectorRoomDetailsMembersFragment.this.mProgressView.setVisibility(8);
                                                VectorRoomDetailsMembersFragment.this.getVectorActivity().getConsentNotGivenHelper().displayDialog(matrixError);
                                            }
                                        });
                                        return;
                                    }
                                }
                                onError(matrixError.getLocalizedMessage());
                            }

                            public void onUnexpectedError(Exception exc) {
                                onError(exc.getLocalizedMessage());
                            }
                        });
                    }
                });
            }

            public void onGroupCollapsedNotif(int i) {
                if (VectorRoomDetailsMembersFragment.this.mIsListViewGroupExpandedMap != null) {
                    VectorRoomDetailsMembersFragment.this.mIsListViewGroupExpandedMap.put(Integer.valueOf(i), Boolean.valueOf(false));
                }
            }

            public void onGroupExpandedNotif(int i) {
                if (VectorRoomDetailsMembersFragment.this.mIsListViewGroupExpandedMap != null) {
                    VectorRoomDetailsMembersFragment.this.mIsListViewGroupExpandedMap.put(Integer.valueOf(i), Boolean.valueOf(true));
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void refreshRoomMembersList(String str, boolean z) {
        if (this.mAdapter != null) {
            this.mProgressView.setVisibility(0);
            this.mAdapter.setSearchedPattern(str, this.mSearchListener, z);
        } else {
            Log.w(LOG_TAG, "## refreshRoomMembersList(): search failure - adapter not initialized");
        }
        processEditionMode();
    }

    private void inviteUserIds(List<String> list) {
        this.mRoom.invite(list, (ApiCallback<Void>) new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                VectorRoomDetailsMembersFragment.this.mIsInvitingNewMembers = false;
                VectorRoomDetailsMembersFragment.this.mDefaultCallBack.onSuccess(null);
            }

            public void onNetworkError(Exception exc) {
                VectorRoomDetailsMembersFragment.this.mIsInvitingNewMembers = false;
                VectorRoomDetailsMembersFragment.this.mDefaultCallBack.onNetworkError(exc);
            }

            public void onMatrixError(MatrixError matrixError) {
                VectorRoomDetailsMembersFragment.this.mIsInvitingNewMembers = false;
                VectorRoomDetailsMembersFragment.this.mDefaultCallBack.onMatrixError(matrixError);
            }

            public void onUnexpectedError(Exception exc) {
                VectorRoomDetailsMembersFragment.this.mIsInvitingNewMembers = false;
                VectorRoomDetailsMembersFragment.this.mDefaultCallBack.onUnexpectedError(exc);
            }
        });
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == INVITE_USER_REQUEST_CODE && i2 == -1) {
            List list = (List) intent.getSerializableExtra(VectorRoomInviteMembersActivity.EXTRA_OUT_SELECTED_USER_IDS);
            if (list != null && list.size() > 0) {
                inviteUserIds(list);
            }
        } else if (i == GET_MENTION_REQUEST_CODE && i2 == -1) {
            String str = VectorMemberDetailsActivity.RESULT_MENTION_ID;
            String stringExtra = intent.getStringExtra(str);
            if (!TextUtils.isEmpty(stringExtra) && getActivity() != null) {
                Intent intent2 = new Intent();
                intent2.putExtra(str, stringExtra);
                getActivity().setResult(-1, intent2);
                getActivity().finish();
            }
        }
    }
}
