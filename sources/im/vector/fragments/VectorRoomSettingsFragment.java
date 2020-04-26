package im.vector.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.preference.TchapRoomAvatarPreference;
import fr.gouv.tchap.sdk.session.room.model.RoomAccessRulesKt;
import fr.gouv.tchap.sdk.session.room.model.RoomRetentionKt;
import fr.gouv.tchap.util.DinsicUtils;
import fr.gouv.tchap.util.DinumUtilsKt;
import im.vector.Matrix;
import im.vector.activity.SelectPictureActivity;
import im.vector.activity.VectorMemberDetailsActivity;
import im.vector.activity.VectorRoomDetailsActivity;
import im.vector.preference.VectorCustomActionEditTextPreference;
import im.vector.preference.VectorCustomActionEditTextPreference.OnPreferenceLongClickListener;
import im.vector.preference.VectorListPreference;
import im.vector.preference.VectorListPreference.OnPreferenceWarningIconClickListener;
import im.vector.settings.VectorLocale;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.RoomUtils;
import im.vector.util.SystemUtilsKt;
import im.vector.util.VectorUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.BingRulesManager;
import org.matrix.androidsdk.core.BingRulesManager.RoomNotificationState;
import org.matrix.androidsdk.core.BingRulesManager.onBingRuleUpdateListener;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXPatterns;
import org.matrix.androidsdk.core.PermalinkUtils;
import org.matrix.androidsdk.core.ResourceUtils;
import org.matrix.androidsdk.core.ResourceUtils.Resource;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.listeners.IMXNetworkEventListener;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.CryptoConstantsKt;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomAccountData;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.RoomTag;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.listeners.MXMediaUploadListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.PowerLevels;
import org.matrix.androidsdk.rest.model.RoomDirectoryVisibility;
import org.matrix.androidsdk.rest.model.RoomMember;

public class VectorRoomSettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    private static final String ACCESS_RULES_ANYONE_WITH_LINK_APART_GUEST = "2";
    private static final String ACCESS_RULES_ANYONE_WITH_LINK_INCLUDING_GUEST = "3";
    private static final String ACCESS_RULES_ONLY_PEOPLE_INVITED = "1";
    private static final String ADDRESSES_PREFERENCE_KEY_BASE = "ADDRESSES_PREFERENCE_KEY_BASE";
    private static final String ADD_ADDRESSES_PREFERENCE_KEY = "ADD_ADDRESSES_PREFERENCE_KEY";
    private static final String BANNED_PREFERENCE_KEY_BASE = "BANNED_PREFERENCE_KEY_BASE";
    private static final boolean DO_NOT_UPDATE_UI = false;
    private static final String EXTRA_MATRIX_ID = "KEY_EXTRA_MATRIX_ID";
    private static final String EXTRA_ROOM_ID = "KEY_EXTRA_ROOM_ID";
    private static final String FLAIR_PREFERENCE_KEY_BASE = "FLAIR_PREFERENCE_KEY_BASE";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorRoomSettingsFragment.class.getSimpleName();
    private static final String NO_LOCAL_ADDRESS_PREFERENCE_KEY = "NO_LOCAL_ADDRESS_PREFERENCE_KEY";
    private static final String PREF_KEY_ADDRESSES = "addresses";
    private static final String PREF_KEY_ADVANCED = "advanced";
    private static final String PREF_KEY_BANNED = "banned";
    private static final String PREF_KEY_BANNED_DIVIDER = "banned_divider";
    private static final String PREF_KEY_ENCRYPTION = "encryptionKey";
    private static final String PREF_KEY_FLAIR = "flair";
    private static final String PREF_KEY_FLAIR_DIVIDER = "flair_divider";
    private static final String PREF_KEY_REMOVE_FROM_ROOMS_DIRECTORY = "removeFromRoomsDirectory";
    private static final String PREF_KEY_ROOM_ACCESS_RULE = "roomAccessRule";
    private static final String PREF_KEY_ROOM_ACCESS_RULES_LIST = "roomAccessRulesList";
    private static final String PREF_KEY_ROOM_DIRECTORY_VISIBILITY_SWITCH = "roomNameListedInDirectorySwitch";
    private static final String PREF_KEY_ROOM_HISTORY_READABILITY_LIST = "roomReadHistoryRulesList";
    private static final String PREF_KEY_ROOM_INTERNAL_ID = "roomInternalId";
    private static final String PREF_KEY_ROOM_LEAVE = "roomLeave";
    private static final String PREF_KEY_ROOM_NAME = "roomNameEditText";
    private static final String PREF_KEY_ROOM_NOTIFICATIONS_LIST = "roomNotificationPreference";
    private static final String PREF_KEY_ROOM_PHOTO_AVATAR = "roomPhotoAvatar";
    private static final String PREF_KEY_ROOM_RETENTION = "roomRetention";
    private static final String PREF_KEY_ROOM_TAG_LIST = "roomTagList";
    private static final String PREF_KEY_ROOM_TOPIC = "roomTopicEditText";
    private static final int REQ_CODE_UPDATE_ROOM_AVATAR = 16;
    private static final String UNKNOWN_VALUE = "UNKNOWN_VALUE";
    private static final boolean UPDATE_UI = true;
    private PreferenceCategory mAddressesSettingsCategory;
    private PreferenceCategory mAdvandceSettingsCategory;
    /* access modifiers changed from: private */
    public final ApiCallback<Void> mAliasUpdatesCallback = new ApiCallback<Void>() {
        public void onSuccess(Void voidR) {
            VectorRoomSettingsFragment.this.hideLoadingView(false);
            VectorRoomSettingsFragment.this.refreshAddresses();
        }

        private void onError(String str) {
            Toast.makeText(VectorRoomSettingsFragment.this.getActivity(), str, 0).show();
            VectorRoomSettingsFragment.this.hideLoadingView(false);
            VectorRoomSettingsFragment.this.refreshAddresses();
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
    /* access modifiers changed from: private */
    public PreferenceCategory mBannedMembersSettingsCategory;
    /* access modifiers changed from: private */
    public PreferenceCategory mBannedMembersSettingsCategoryDivider;
    private BingRulesManager mBingRulesManager;
    private final MXEventListener mEventListener = new MXEventListener() {
        public void onLiveEvent(final Event event, RoomState roomState) {
            VectorRoomSettingsFragment.this.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    String type = event.getType();
                    boolean equals = Event.EVENT_TYPE_STATE_ROOM_NAME.equals(type);
                    String str = Event.EVENT_TYPE_STATE_ROOM_POWER_LEVELS;
                    String str2 = "m.room.member";
                    String str3 = Event.EVENT_TYPE_STATE_ROOM_ALIASES;
                    String str4 = RoomAccessRulesKt.EVENT_TYPE_STATE_ROOM_ACCESS_RULES;
                    if (equals || str3.equals(type) || str2.equals(type) || Event.EVENT_TYPE_STATE_ROOM_AVATAR.equals(type) || Event.EVENT_TYPE_STATE_ROOM_TOPIC.equals(type) || str.equals(type) || Event.EVENT_TYPE_STATE_HISTORY_VISIBILITY.equals(type) || Event.EVENT_TYPE_STATE_ROOM_JOIN_RULES.equals(type) || Event.EVENT_TYPE_STATE_ROOM_GUEST_ACCESS.equals(type) || str4.equals(type) || RoomRetentionKt.EVENT_TYPE_STATE_ROOM_RETENTION.equals(type)) {
                        String access$200 = VectorRoomSettingsFragment.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## onLiveEvent() event = ");
                        sb.append(type);
                        Log.d(access$200, sb.toString());
                        VectorRoomSettingsFragment.this.updateUi();
                        if (str4.equals(type) && (VectorRoomSettingsFragment.this.getActivity() instanceof VectorRoomDetailsActivity)) {
                            ((VectorRoomDetailsActivity) VectorRoomSettingsFragment.this.getActivity()).refreshAvatar();
                        }
                    }
                    if ("m.room.encryption".equals(type)) {
                        VectorRoomSettingsFragment.this.refreshEndToEnd();
                    }
                    if (Event.EVENT_TYPE_STATE_CANONICAL_ALIAS.equals(type) || str3.equals(type) || str.equals(type)) {
                        Log.d(VectorRoomSettingsFragment.LOG_TAG, "## onLiveEvent() refresh the addresses list");
                        VectorRoomSettingsFragment.this.refreshAddresses();
                    }
                    if (str2.equals(type)) {
                        Log.d(VectorRoomSettingsFragment.LOG_TAG, "## onLiveEvent() refresh the banned members list");
                        VectorRoomSettingsFragment.this.refreshBannedMembersList();
                    }
                }
            });
        }

        public void onRoomFlush(String str) {
            VectorRoomSettingsFragment.this.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    VectorRoomSettingsFragment.this.updateUi();
                }
            });
        }

        public void onRoomTagEvent(String str) {
            Log.d(VectorRoomSettingsFragment.LOG_TAG, "## onRoomTagEvent()");
            VectorRoomSettingsFragment.this.updateUi();
        }

        public void onBingRulesUpdate() {
            VectorRoomSettingsFragment.this.updateUi();
        }
    };
    private PreferenceCategory mFlairSettingsCategory;
    /* access modifiers changed from: private */
    public final ApiCallback mFlairUpdatesCallback = new ApiCallback<Void>() {
        public void onSuccess(Void voidR) {
            VectorRoomSettingsFragment.this.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    VectorRoomSettingsFragment.this.hideLoadingView(false);
                    VectorRoomSettingsFragment.this.refreshFlair();
                }
            });
        }

        private void onError(final String str) {
            VectorRoomSettingsFragment.this.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(VectorRoomSettingsFragment.this.getActivity(), str, 0).show();
                    VectorRoomSettingsFragment.this.hideLoadingView(false);
                    VectorRoomSettingsFragment.this.refreshFlair();
                }
            });
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
    private boolean mIsUiUpdateSkipped;
    private final IMXNetworkEventListener mNetworkListener = new IMXNetworkEventListener() {
        public void onNetworkConnectionUpdate(boolean z) {
            VectorRoomSettingsFragment.this.updateUi();
        }
    };
    /* access modifiers changed from: private */
    public View mParentFragmentContainerView;
    /* access modifiers changed from: private */
    public View mParentLoadingView;
    /* access modifiers changed from: private */
    public Preference mRemoveFromDirectoryPreference;
    /* access modifiers changed from: private */
    public Room mRoom;
    private Preference mRoomAccessRulePreference;
    private VectorListPreference mRoomAccessRulesListPreference;
    /* access modifiers changed from: private */
    public CheckBoxPreference mRoomDirectoryVisibilitySwitch;
    private ListPreference mRoomHistoryReadabilityRulesListPreference;
    private EditTextPreference mRoomNameEditTxt;
    private ListPreference mRoomNotificationsPreference;
    /* access modifiers changed from: private */
    public TchapRoomAvatarPreference mRoomPhotoAvatar;
    private Preference mRoomRetentionPreference;
    private ListPreference mRoomTagListPreference;
    private EditTextPreference mRoomTopicEditTxt;
    /* access modifiers changed from: private */
    public MXSession mSession;
    /* access modifiers changed from: private */
    public final ApiCallback<Void> mUpdateAvatarCallback = new ApiCallback<Void>() {
        private void onDone(String str, final boolean z) {
            if (VectorRoomSettingsFragment.this.getActivity() != null) {
                if (!TextUtils.isEmpty(str)) {
                    Toast.makeText(VectorRoomSettingsFragment.this.getActivity(), str, 1).show();
                }
                VectorRoomSettingsFragment.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        VectorRoomSettingsFragment.this.hideLoadingView(z);
                    }
                });
            }
        }

        public void onSuccess(Void voidR) {
            Log.d(VectorRoomSettingsFragment.LOG_TAG, "##update succeed");
            onDone(null, true);
            if (VectorRoomSettingsFragment.this.getActivity() instanceof VectorRoomDetailsActivity) {
                ((VectorRoomDetailsActivity) VectorRoomSettingsFragment.this.getActivity()).refreshAvatar();
            }
        }

        public void onNetworkError(Exception exc) {
            String access$200 = VectorRoomSettingsFragment.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("##NetworkError ");
            sb.append(exc.getLocalizedMessage());
            Log.w(access$200, sb.toString());
            onDone(exc.getLocalizedMessage(), false);
        }

        public void onMatrixError(MatrixError matrixError) {
            String access$200 = VectorRoomSettingsFragment.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("##MatrixError ");
            sb.append(matrixError.getLocalizedMessage());
            Log.w(access$200, sb.toString());
            onDone(matrixError.getLocalizedMessage(), false);
        }

        public void onUnexpectedError(Exception exc) {
            String access$200 = VectorRoomSettingsFragment.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("##UnexpectedError ");
            sb.append(exc.getLocalizedMessage());
            Log.w(access$200, sb.toString());
            onDone(exc.getLocalizedMessage(), false);
        }
    };
    /* access modifiers changed from: private */
    public final ApiCallback<Void> mUpdateCallback = new ApiCallback<Void>() {
        private void onDone(String str, final boolean z) {
            if (VectorRoomSettingsFragment.this.getActivity() != null) {
                if (!TextUtils.isEmpty(str)) {
                    Toast.makeText(VectorRoomSettingsFragment.this.getActivity(), str, 1).show();
                }
                VectorRoomSettingsFragment.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        VectorRoomSettingsFragment.this.hideLoadingView(z);
                    }
                });
            }
        }

        public void onSuccess(Void voidR) {
            Log.d(VectorRoomSettingsFragment.LOG_TAG, "##update succeed");
            onDone(null, true);
        }

        public void onNetworkError(Exception exc) {
            String access$200 = VectorRoomSettingsFragment.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("##NetworkError ");
            sb.append(exc.getLocalizedMessage());
            Log.w(access$200, sb.toString());
            onDone(exc.getLocalizedMessage(), true);
        }

        public void onMatrixError(MatrixError matrixError) {
            String access$200 = VectorRoomSettingsFragment.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("##MatrixError ");
            sb.append(matrixError.getLocalizedMessage());
            Log.w(access$200, sb.toString());
            onDone(matrixError.getLocalizedMessage(), true);
        }

        public void onUnexpectedError(Exception exc) {
            String access$200 = VectorRoomSettingsFragment.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("##UnexpectedError ");
            sb.append(exc.getLocalizedMessage());
            Log.w(access$200, sb.toString());
            onDone(exc.getLocalizedMessage(), true);
        }
    };

    /* access modifiers changed from: private */
    public void refreshAddresses() {
    }

    /* access modifiers changed from: private */
    public void refreshEndToEnd() {
    }

    public static VectorRoomSettingsFragment newInstance(String str, String str2) {
        VectorRoomSettingsFragment vectorRoomSettingsFragment = new VectorRoomSettingsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_MATRIX_ID, str);
        bundle.putString(EXTRA_ROOM_ID, str2);
        vectorRoomSettingsFragment.setArguments(bundle);
        return vectorRoomSettingsFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(LOG_TAG, "## onCreate() IN");
        String string = getArguments().getString(EXTRA_MATRIX_ID);
        String string2 = getArguments().getString(EXTRA_ROOM_ID);
        if (TextUtils.isEmpty(string) || TextUtils.isEmpty(string2)) {
            Log.e(LOG_TAG, "## onCreate(): fragment extras (MatrixId or RoomId) are missing");
            getActivity().finish();
        } else {
            this.mSession = Matrix.getInstance(getActivity()).getSession(string);
            MXSession mXSession = this.mSession;
            if (mXSession != null && mXSession.isAlive()) {
                this.mRoom = this.mSession.getDataHandler().getRoom(string2);
                this.mBingRulesManager = this.mSession.getDataHandler().getBingRulesManager();
            }
            if (this.mRoom == null) {
                Log.e(LOG_TAG, "## onCreate(): unable to retrieve Room object");
                getActivity().finish();
            }
        }
        addPreferencesFromResource(R.xml.vector_room_settings_preferences);
        this.mRoomPhotoAvatar = (TchapRoomAvatarPreference) findPreference(PREF_KEY_ROOM_PHOTO_AVATAR);
        this.mRoomNameEditTxt = (EditTextPreference) findPreference(PREF_KEY_ROOM_NAME);
        this.mRoomTopicEditTxt = (EditTextPreference) findPreference(PREF_KEY_ROOM_TOPIC);
        this.mRoomDirectoryVisibilitySwitch = (CheckBoxPreference) findPreference(PREF_KEY_ROOM_DIRECTORY_VISIBILITY_SWITCH);
        this.mRoomTagListPreference = (ListPreference) findPreference(PREF_KEY_ROOM_TAG_LIST);
        this.mRoomAccessRulesListPreference = (VectorListPreference) findPreference(PREF_KEY_ROOM_ACCESS_RULES_LIST);
        this.mRoomHistoryReadabilityRulesListPreference = (ListPreference) findPreference(PREF_KEY_ROOM_HISTORY_READABILITY_LIST);
        this.mAddressesSettingsCategory = (PreferenceCategory) getPreferenceManager().findPreference(PREF_KEY_ADDRESSES);
        this.mAdvandceSettingsCategory = (PreferenceCategory) getPreferenceManager().findPreference(PREF_KEY_ADVANCED);
        this.mBannedMembersSettingsCategory = (PreferenceCategory) getPreferenceManager().findPreference(PREF_KEY_BANNED);
        this.mBannedMembersSettingsCategoryDivider = (PreferenceCategory) getPreferenceManager().findPreference(PREF_KEY_BANNED_DIVIDER);
        this.mFlairSettingsCategory = (PreferenceCategory) getPreferenceManager().findPreference(PREF_KEY_FLAIR);
        this.mRoomNotificationsPreference = (ListPreference) getPreferenceManager().findPreference(PREF_KEY_ROOM_NOTIFICATIONS_LIST);
        VectorListPreference vectorListPreference = this.mRoomAccessRulesListPreference;
        if (vectorListPreference != null) {
            vectorListPreference.setOnPreferenceWarningIconClickListener(new OnPreferenceWarningIconClickListener() {
                public void onWarningIconClick(Preference preference) {
                    VectorRoomSettingsFragment.this.displayAccessRoomWarning();
                }
            });
        }
        Preference findPreference = findPreference(PREF_KEY_ROOM_INTERNAL_ID);
        if (findPreference != null) {
            findPreference.setSummary(this.mRoom.getRoomId());
            findPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    SystemUtilsKt.copyToClipboard(VectorRoomSettingsFragment.this.getActivity(), VectorRoomSettingsFragment.this.mRoom.getRoomId());
                    return false;
                }
            });
        }
        Preference findPreference2 = findPreference(PREF_KEY_ROOM_LEAVE);
        if (findPreference2 != null) {
            findPreference2.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    RoomUtils.showLeaveRoomDialog(VectorRoomSettingsFragment.this.getActivity(), VectorRoomSettingsFragment.this.mSession, VectorRoomSettingsFragment.this.mRoom, new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            VectorRoomSettingsFragment.this.displayLoadingView();
                            VectorRoomSettingsFragment.this.mRoom.leave(new ApiCallback<Void>() {
                                public void onSuccess(Void voidR) {
                                    if (VectorRoomSettingsFragment.this.getActivity() != null) {
                                        VectorRoomSettingsFragment.this.getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                VectorRoomSettingsFragment.this.getActivity().finish();
                                            }
                                        });
                                    }
                                }

                                private void onError(final String str) {
                                    if (VectorRoomSettingsFragment.this.getActivity() != null) {
                                        VectorRoomSettingsFragment.this.getActivity().runOnUiThread(new Runnable() {
                                            public void run() {
                                                VectorRoomSettingsFragment.this.hideLoadingView(true);
                                                Toast.makeText(VectorRoomSettingsFragment.this.getActivity(), str, 0).show();
                                            }
                                        });
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
                    });
                    return true;
                }
            });
        }
        this.mRemoveFromDirectoryPreference = findPreference(PREF_KEY_REMOVE_FROM_ROOMS_DIRECTORY);
        Preference preference = this.mRemoveFromDirectoryPreference;
        if (preference != null) {
            preference.setEnabled(false);
            this.mRemoveFromDirectoryPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    new Builder(VectorRoomSettingsFragment.this.getActivity()).setTitle((int) R.string.dialog_title_warning).setMessage((int) R.string.tchap_room_settings_remove_from_directory_prompt_msg).setPositiveButton((int) R.string.yes, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            VectorRoomSettingsFragment.this.removeFromRoomsDirectory();
                        }
                    }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
                    return true;
                }
            });
        }
        this.mRoomRetentionPreference = findPreference(PREF_KEY_ROOM_RETENTION);
        this.mRoomAccessRulePreference = findPreference(PREF_KEY_ROOM_ACCESS_RULE);
        if (this.mRoomAccessRulePreference != null && DinumUtilsKt.isSecure()) {
            getPreferenceScreen().removePreference(this.mRoomAccessRulePreference);
            this.mRoomAccessRulePreference = null;
        }
        this.mRoomPhotoAvatar.setConfiguration(this.mSession, this.mRoom);
        this.mRoomPhotoAvatar.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                if (VectorRoomSettingsFragment.this.mRoomPhotoAvatar == null || !VectorRoomSettingsFragment.this.mRoomPhotoAvatar.isEnabled()) {
                    return false;
                }
                VectorRoomSettingsFragment.this.onRoomAvatarPreferenceChanged();
                return true;
            }
        });
        enableSharedPreferenceListener(true);
        setRetainInstance(true);
    }

    /* access modifiers changed from: private */
    public void removeFromRoomsDirectory() {
        displayLoadingView();
        this.mRoom.updateJoinRules("invite", new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                if (!VectorRoomSettingsFragment.this.mRoom.isEncrypted()) {
                    VectorRoomSettingsFragment.this.mRoom.enableEncryptionWithAlgorithm(CryptoConstantsKt.MXCRYPTO_ALGORITHM_MEGOLM, new ApiCallback<Void>() {
                        public void onSuccess(Void voidR) {
                            VectorRoomSettingsFragment.this.mRoom.updateDirectoryVisibility(RoomDirectoryVisibility.DIRECTORY_VISIBILITY_PRIVATE, VectorRoomSettingsFragment.this.mUpdateCallback);
                        }

                        public void onNetworkError(Exception exc) {
                            VectorRoomSettingsFragment.this.onRemoveFromDirectoryError(exc.getLocalizedMessage());
                        }

                        public void onMatrixError(MatrixError matrixError) {
                            VectorRoomSettingsFragment.this.onRemoveFromDirectoryError(matrixError.getLocalizedMessage());
                        }

                        public void onUnexpectedError(Exception exc) {
                            VectorRoomSettingsFragment.this.onRemoveFromDirectoryError(exc.getLocalizedMessage());
                        }
                    });
                    return;
                }
                VectorRoomSettingsFragment.this.mRoom.updateDirectoryVisibility(RoomDirectoryVisibility.DIRECTORY_VISIBILITY_PRIVATE, VectorRoomSettingsFragment.this.mUpdateCallback);
            }

            public void onNetworkError(Exception exc) {
                VectorRoomSettingsFragment.this.onRemoveFromDirectoryError(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                VectorRoomSettingsFragment.this.onRemoveFromDirectoryError(matrixError.getLocalizedMessage());
            }

            public void onUnexpectedError(Exception exc) {
                VectorRoomSettingsFragment.this.onRemoveFromDirectoryError(exc.getLocalizedMessage());
            }
        });
    }

    /* access modifiers changed from: private */
    public void onRemoveFromDirectoryError(String str) {
        hideLoadingView(true);
        Toast.makeText(getActivity(), str, 0).show();
    }

    /* access modifiers changed from: private */
    public void allowExternalsToJoin() {
        displayLoadingView();
        DinsicUtils.setRoomAccessRule(this.mSession, this.mRoom, RoomAccessRulesKt.UNRESTRICTED, this.mUpdateCallback);
    }

    private void setRetentionPeriod(int i) {
        displayLoadingView();
        DinumUtilsKt.setRoomRetention(this.mSession, this.mRoom, i, this.mUpdateCallback);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        View findViewById = onCreateView.findViewById(16908298);
        if (findViewById != null) {
            findViewById.setPadding(0, 0, 0, 0);
        }
        onCreateView.setBackgroundColor(ThemeUtils.INSTANCE.getColor(getActivity(), R.attr.vctr_riot_primary_background_color));
        return onCreateView;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (this.mParentLoadingView == null) {
            for (View view2 = getView(); view2 != null && this.mParentLoadingView == null; view2 = (View) view2.getParent()) {
                this.mParentLoadingView = view2.findViewById(R.id.settings_loading_layout);
            }
        }
        if (this.mParentFragmentContainerView == null) {
            for (View view3 = getView(); view3 != null && this.mParentFragmentContainerView == null; view3 = (View) view3.getParent()) {
                this.mParentFragmentContainerView = view3.findViewById(R.id.room_details_fragment_container);
            }
        }
    }

    public void onPause() {
        super.onPause();
        if (this.mRoom != null) {
            Matrix.getInstance(getActivity()).removeNetworkEventListener(this.mNetworkListener);
            this.mRoom.removeEventListener(this.mEventListener);
        }
        enableSharedPreferenceListener(false);
    }

    public void onResume() {
        super.onResume();
        if (this.mRoom != null) {
            Matrix.getInstance(getActivity()).addNetworkEventListener(this.mNetworkListener);
            this.mRoom.addEventListener(this.mEventListener);
            updateUi();
            refreshAddresses();
            refreshFlair();
            refreshBannedMembersList();
            refreshEndToEnd();
        }
    }

    /* access modifiers changed from: private */
    public void enableSharedPreferenceListener(boolean z) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## enableSharedPreferenceListener(): aIsListenerEnabled=");
        sb.append(z);
        Log.d(str, sb.toString());
        this.mIsUiUpdateSkipped = !z;
        try {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            if (z) {
                defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this);
            } else {
                defaultSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
            }
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## enableSharedPreferenceListener(): Exception Msg=");
            sb2.append(e.getMessage());
            Log.e(str2, sb2.toString(), e);
        }
    }

    /* access modifiers changed from: private */
    public void updateUi() {
        updatePreferenceAccessFromPowerLevel();
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                VectorRoomSettingsFragment.this.enableSharedPreferenceListener(false);
                VectorRoomSettingsFragment.this.updatePreferenceUiValues();
                VectorRoomSettingsFragment.this.enableSharedPreferenceListener(true);
                VectorRoomSettingsFragment.this.updateRoomDirectoryVisibilityAsync();
            }
        });
    }

    private void updateUiOnUiThread() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                VectorRoomSettingsFragment.this.updateUi();
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateRoomDirectoryVisibilityAsync() {
        if (this.mRoom == null) {
            Log.w(LOG_TAG, "## updateRoomDirectoryVisibilityUi(): not processed due to invalid parameters");
            return;
        }
        displayLoadingView();
        Room room = this.mRoom;
        room.getDirectoryVisibility(room.getRoomId(), new ApiCallback<String>() {
            private void handleResponseOnUiThread(String str) {
                if (VectorRoomSettingsFragment.this.isAdded()) {
                    VectorRoomSettingsFragment.this.hideLoadingView(false);
                    boolean equals = "public".equals(str);
                    if (VectorRoomSettingsFragment.this.mRoomDirectoryVisibilitySwitch != null) {
                        VectorRoomSettingsFragment.this.enableSharedPreferenceListener(false);
                        VectorRoomSettingsFragment.this.mRoomDirectoryVisibilitySwitch.setChecked(equals);
                        VectorRoomSettingsFragment.this.enableSharedPreferenceListener(true);
                    }
                    if (VectorRoomSettingsFragment.this.mRemoveFromDirectoryPreference != null) {
                        if (equals) {
                            VectorRoomSettingsFragment.this.mRemoveFromDirectoryPreference.setEnabled(true);
                        } else {
                            VectorRoomSettingsFragment.this.getPreferenceScreen().removePreference(VectorRoomSettingsFragment.this.mRemoveFromDirectoryPreference);
                            VectorRoomSettingsFragment.this.mRemoveFromDirectoryPreference = null;
                        }
                    }
                }
            }

            public void onSuccess(String str) {
                handleResponseOnUiThread(str);
            }

            public void onNetworkError(Exception exc) {
                String access$200 = VectorRoomSettingsFragment.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getDirectoryVisibility(): onNetworkError Msg=");
                sb.append(exc.getLocalizedMessage());
                Log.w(access$200, sb.toString());
                handleResponseOnUiThread(null);
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$200 = VectorRoomSettingsFragment.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getDirectoryVisibility(): onMatrixError Msg=");
                sb.append(matrixError.getLocalizedMessage());
                Log.w(access$200, sb.toString());
                handleResponseOnUiThread(null);
            }

            public void onUnexpectedError(Exception exc) {
                String access$200 = VectorRoomSettingsFragment.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getDirectoryVisibility(): onUnexpectedError Msg=");
                sb.append(exc.getLocalizedMessage());
                Log.w(access$200, sb.toString());
                handleResponseOnUiThread(null);
            }
        });
    }

    /* access modifiers changed from: private */
    public void displayAccessRoomWarning() {
        Toast.makeText(getActivity(), R.string.room_settings_room_access_warning, 0).show();
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x011b  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x012a  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0138  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x013f  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x016f A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x017c  */
    /* JADX WARNING: Removed duplicated region for block: B:98:? A[RETURN, SYNTHETIC] */
    private void updatePreferenceAccessFromPowerLevel() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        TchapRoomAvatarPreference tchapRoomAvatarPreference;
        EditTextPreference editTextPreference;
        EditTextPreference editTextPreference2;
        Preference preference;
        CheckBoxPreference checkBoxPreference;
        ListPreference listPreference;
        VectorListPreference vectorListPreference;
        ListPreference listPreference2;
        ListPreference listPreference3;
        boolean isConnected = Matrix.getInstance(getActivity()).isConnected();
        Room room = this.mRoom;
        boolean z5 = true;
        if (room == null || this.mSession == null) {
            Log.w(LOG_TAG, "## updatePreferenceAccessFromPowerLevel(): session or room may be missing");
        } else {
            PowerLevels powerLevels = room.getState().getPowerLevels();
            if (powerLevels != null) {
                int userPowerLevel = powerLevels.getUserPowerLevel(this.mSession.getMyUserId());
                z2 = userPowerLevel >= powerLevels.minimumPowerLevelForSendingEventAsStateEvent(Event.EVENT_TYPE_STATE_ROOM_AVATAR);
                z = userPowerLevel >= powerLevels.minimumPowerLevelForSendingEventAsStateEvent(Event.EVENT_TYPE_STATE_ROOM_NAME);
                z4 = userPowerLevel >= powerLevels.minimumPowerLevelForSendingEventAsStateEvent(Event.EVENT_TYPE_STATE_ROOM_TOPIC);
                z3 = ((float) userPowerLevel) >= 100.0f;
                tchapRoomAvatarPreference = this.mRoomPhotoAvatar;
                if (tchapRoomAvatarPreference != null) {
                    tchapRoomAvatarPreference.setEnabled(z2 && isConnected);
                }
                editTextPreference = this.mRoomNameEditTxt;
                if (editTextPreference != null) {
                    editTextPreference.setEnabled(z && isConnected);
                }
                editTextPreference2 = this.mRoomTopicEditTxt;
                if (editTextPreference2 != null) {
                    editTextPreference2.setEnabled(z4 && isConnected);
                }
                if (this.mRemoveFromDirectoryPreference != null && (!z3 || !isConnected)) {
                    getPreferenceScreen().removePreference(this.mRemoveFromDirectoryPreference);
                    this.mRemoveFromDirectoryPreference = null;
                }
                preference = this.mRoomAccessRulePreference;
                String str = "invite";
                if (preference != null) {
                    preference.setOnPreferenceClickListener(null);
                    this.mRoomAccessRulePreference.setEnabled(true);
                    if (TextUtils.equals(DinsicUtils.getRoomAccessRule(this.mRoom), RoomAccessRulesKt.RESTRICTED)) {
                        String str2 = this.mRoom.getState().join_rule;
                        if (!z3 || !isConnected || !str.equals(str2)) {
                            this.mRoomAccessRulePreference.setTitle(getString(R.string.tchap_room_settings_room_access_title));
                            this.mRoomAccessRulePreference.setSummary(getString(R.string.tchap_room_settings_room_access_restricted));
                        } else {
                            this.mRoomAccessRulePreference.setTitle(getString(R.string.tchap_room_settings_allow_external_users_to_join));
                            this.mRoomAccessRulePreference.setSummary(null);
                            this.mRoomAccessRulePreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                                public boolean onPreferenceClick(Preference preference) {
                                    new Builder(VectorRoomSettingsFragment.this.getActivity()).setTitle((int) R.string.dialog_title_warning).setMessage((int) R.string.tchap_room_settings_allow_external_users_to_join_prompt_msg).setPositiveButton((int) R.string.yes, (OnClickListener) new OnClickListener() {
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            VectorRoomSettingsFragment.this.allowExternalsToJoin();
                                        }
                                    }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
                                    return true;
                                }
                            });
                        }
                    } else {
                        this.mRoomAccessRulePreference.setTitle(getString(R.string.tchap_room_settings_room_access_title));
                        this.mRoomAccessRulePreference.setSummary(getString(R.string.tchap_room_settings_room_access_unrestricted));
                    }
                }
                if (this.mRoomRetentionPreference != null) {
                    getPreferenceScreen().removePreference(this.mRoomRetentionPreference);
                    this.mRoomRetentionPreference = null;
                }
                checkBoxPreference = this.mRoomDirectoryVisibilitySwitch;
                if (checkBoxPreference != null) {
                    checkBoxPreference.setEnabled(z3 && isConnected);
                }
                listPreference = this.mRoomTagListPreference;
                if (listPreference != null) {
                    listPreference.setEnabled(isConnected);
                }
                vectorListPreference = this.mRoomAccessRulesListPreference;
                if (vectorListPreference != null) {
                    vectorListPreference.setEnabled(z3 && isConnected);
                    this.mRoomAccessRulesListPreference.setWarningIconVisible(this.mRoom.getAliases().size() == 0 && !TextUtils.equals(str, this.mRoom.getState().join_rule));
                }
                listPreference2 = this.mRoomHistoryReadabilityRulesListPreference;
                if (listPreference2 != null) {
                    if (!z3 || !isConnected) {
                        z5 = false;
                    }
                    listPreference2.setEnabled(z5);
                }
                listPreference3 = this.mRoomNotificationsPreference;
                if (listPreference3 == null) {
                    listPreference3.setEnabled(isConnected);
                    return;
                }
                return;
            }
        }
        z4 = false;
        z3 = false;
        z2 = false;
        z = false;
        tchapRoomAvatarPreference = this.mRoomPhotoAvatar;
        if (tchapRoomAvatarPreference != null) {
        }
        editTextPreference = this.mRoomNameEditTxt;
        if (editTextPreference != null) {
        }
        editTextPreference2 = this.mRoomTopicEditTxt;
        if (editTextPreference2 != null) {
        }
        getPreferenceScreen().removePreference(this.mRemoveFromDirectoryPreference);
        this.mRemoveFromDirectoryPreference = null;
        preference = this.mRoomAccessRulePreference;
        String str3 = "invite";
        if (preference != null) {
        }
        if (this.mRoomRetentionPreference != null) {
        }
        checkBoxPreference = this.mRoomDirectoryVisibilitySwitch;
        if (checkBoxPreference != null) {
        }
        listPreference = this.mRoomTagListPreference;
        if (listPreference != null) {
        }
        vectorListPreference = this.mRoomAccessRulesListPreference;
        if (vectorListPreference != null) {
        }
        listPreference2 = this.mRoomHistoryReadabilityRulesListPreference;
        if (listPreference2 != null) {
        }
        listPreference3 = this.mRoomNotificationsPreference;
        if (listPreference3 == null) {
        }
    }

    /* access modifiers changed from: private */
    public void updatePreferenceUiValues() {
        String str;
        if (this.mSession == null || this.mRoom == null) {
            Log.w(LOG_TAG, "## updatePreferenceUiValues(): session or room may be missing");
            return;
        }
        TchapRoomAvatarPreference tchapRoomAvatarPreference = this.mRoomPhotoAvatar;
        if (tchapRoomAvatarPreference != null) {
            tchapRoomAvatarPreference.refreshAvatar();
        }
        if (this.mRoomNameEditTxt != null) {
            String str2 = this.mRoom.getState().name;
            this.mRoomNameEditTxt.setSummary(str2);
            this.mRoomNameEditTxt.setText(str2);
        }
        if (this.mRoomTopicEditTxt != null) {
            String topic = this.mRoom.getTopic();
            this.mRoomTopicEditTxt.setSummary(topic);
            this.mRoomTopicEditTxt.setText(topic);
        }
        if (!isAdded()) {
            Log.e(LOG_TAG, "## updatePreferenceUiValues(): fragment not added to Activity - isAdded()=false");
            return;
        }
        if (this.mRoomAccessRulesListPreference != null) {
            String str3 = this.mRoom.getState().join_rule;
            String guestAccess = this.mRoom.getState().getGuestAccess();
            if ("invite".equals(str3)) {
                str = ACCESS_RULES_ONLY_PEOPLE_INVITED;
            } else {
                String str4 = "public";
                if (str4.equals(str3) && RoomState.GUEST_ACCESS_FORBIDDEN.equals(guestAccess)) {
                    str = ACCESS_RULES_ANYONE_WITH_LINK_APART_GUEST;
                } else if (!str4.equals(str3) || !RoomState.GUEST_ACCESS_CAN_JOIN.equals(guestAccess)) {
                    String str5 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## updatePreferenceUiValues(): unknown room access configuration joinRule=");
                    sb.append(str3);
                    sb.append(" and guestAccessRule=");
                    sb.append(guestAccess);
                    Log.w(str5, sb.toString());
                    str = null;
                } else {
                    str = ACCESS_RULES_ANYONE_WITH_LINK_INCLUDING_GUEST;
                }
            }
            if (str != null) {
                this.mRoomAccessRulesListPreference.setValue(str);
            } else {
                this.mRoomAccessRulesListPreference.setValue(UNKNOWN_VALUE);
            }
        }
        if (this.mRoomNotificationsPreference != null) {
            RoomNotificationState roomNotificationState = this.mSession.getDataHandler().getBingRulesManager().getRoomNotificationState(this.mRoom.getRoomId());
            if (roomNotificationState != null) {
                this.mRoomNotificationsPreference.setValue(roomNotificationState.name());
            } else {
                this.mRoomNotificationsPreference.setValue(RoomNotificationState.MUTE.name());
            }
        }
        if (!(this.mRoomTagListPreference == null || this.mRoom.getAccountData() == null)) {
            RoomAccountData accountData = this.mRoom.getAccountData();
            String str6 = RoomTag.ROOM_TAG_FAVOURITE;
            RoomTag roomTag = accountData.roomTag(str6);
            String str7 = RoomTag.ROOM_TAG_LOW_PRIORITY;
            if (roomTag == null) {
                str6 = this.mRoom.getAccountData().roomTag(str7) != null ? str7 : RoomTag.ROOM_TAG_NO_TAG;
            }
            this.mRoomTagListPreference.setValue(str6);
        }
        if (this.mRoomHistoryReadabilityRulesListPreference != null) {
            String historyVisibility = this.mRoom.getState().getHistoryVisibility();
            if (historyVisibility != null) {
                this.mRoomHistoryReadabilityRulesListPreference.setValue(historyVisibility);
            }
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        if (this.mIsUiUpdateSkipped) {
            Log.d(LOG_TAG, "## onSharedPreferenceChanged(): Skipped");
        } else if (getActivity() == null) {
            Log.d(LOG_TAG, "## onSharedPreferenceChanged(): no attached to an activity");
        } else {
            if (str.equals(PREF_KEY_ROOM_PHOTO_AVATAR)) {
                onRoomAvatarPreferenceChanged();
            } else if (str.equals(PREF_KEY_ROOM_NAME)) {
                onRoomNamePreferenceChanged();
            } else if (str.equals(PREF_KEY_ROOM_TOPIC)) {
                onRoomTopicPreferenceChanged();
            } else if (str.equals(PREF_KEY_ROOM_NOTIFICATIONS_LIST)) {
                onRoomNotificationsPreferenceChanged();
            } else if (str.equals(PREF_KEY_ROOM_DIRECTORY_VISIBILITY_SWITCH)) {
                onRoomDirectoryVisibilityPreferenceChanged();
            } else if (str.equals(PREF_KEY_ROOM_TAG_LIST)) {
                onRoomTagPreferenceChanged();
            } else if (str.equals(PREF_KEY_ROOM_ACCESS_RULES_LIST)) {
                onRoomAccessPreferenceChanged();
            } else if (str.equals(PREF_KEY_ROOM_HISTORY_READABILITY_LIST)) {
                onRoomHistoryReadabilityPreferenceChanged();
            } else {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## onSharedPreferenceChanged(): unknown aKey = ");
                sb.append(str);
                Log.w(str2, sb.toString());
            }
        }
    }

    private void onRoomHistoryReadabilityPreferenceChanged() {
        Room room = this.mRoom;
        if (room == null || this.mRoomHistoryReadabilityRulesListPreference == null) {
            Log.w(LOG_TAG, "## onRoomHistoryReadabilityPreferenceChanged(): not processed due to invalid parameters");
            return;
        }
        String str = room.getState().history_visibility;
        String value = this.mRoomHistoryReadabilityRulesListPreference.getValue();
        if (!TextUtils.equals(value, str)) {
            displayLoadingView();
            this.mRoom.updateHistoryVisibility(value, this.mUpdateCallback);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
    private void onRoomTagPreferenceChanged() {
        if (this.mRoom != null) {
            ListPreference listPreference = this.mRoomTagListPreference;
            if (listPreference != null) {
                String value = listPreference.getValue();
                Double valueOf = Double.valueOf(0.0d);
                RoomAccountData accountData = this.mRoom.getAccountData();
                String str = (accountData == null || !accountData.hasTags()) ? null : (String) accountData.getKeys().iterator().next();
                boolean z = true;
                if (!value.equals(str)) {
                    if (!value.equals(RoomTag.ROOM_TAG_FAVOURITE) && !value.equals(RoomTag.ROOM_TAG_LOW_PRIORITY)) {
                        if (value.equals(RoomTag.ROOM_TAG_NO_TAG)) {
                            value = null;
                        } else {
                            String str2 = LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## onRoomTagPreferenceChanged() not supported tag = ");
                            sb.append(value);
                            Log.w(str2, sb.toString());
                        }
                    }
                    if (!z) {
                        displayLoadingView();
                        this.mRoom.replaceTag(str, value, valueOf, this.mUpdateCallback);
                        return;
                    }
                    return;
                }
                z = false;
                if (!z) {
                }
            }
        }
        Log.w(LOG_TAG, "## onRoomTagPreferenceChanged(): not processed due to invalid parameters");
    }

    private void onRoomAccessPreferenceChanged() {
        String str;
        Room room = this.mRoom;
        if (room == null || this.mRoomAccessRulesListPreference == null) {
            Log.w(LOG_TAG, "## onRoomAccessPreferenceChanged(): not processed due to invalid parameters");
            return;
        }
        String str2 = room.getState().join_rule;
        String guestAccess = this.mRoom.getState().getGuestAccess();
        String value = this.mRoomAccessRulesListPreference.getValue();
        boolean equals = ACCESS_RULES_ONLY_PEOPLE_INVITED.equals(value);
        String str3 = RoomState.GUEST_ACCESS_CAN_JOIN;
        String str4 = null;
        if (equals) {
            String str5 = "invite";
            if (str5.equals(str2)) {
                str5 = null;
            }
            if (!str3.equals(guestAccess)) {
                str4 = str3;
            }
            str = str5;
        } else {
            str = "public";
            if (ACCESS_RULES_ANYONE_WITH_LINK_APART_GUEST.equals(value)) {
                if (str.equals(str2)) {
                    str = null;
                }
                String str6 = RoomState.GUEST_ACCESS_FORBIDDEN;
                if (!str6.equals(guestAccess)) {
                    str4 = str6;
                }
                if (this.mRoom.getAliases().size() == 0) {
                    displayAccessRoomWarning();
                }
            } else if (ACCESS_RULES_ANYONE_WITH_LINK_INCLUDING_GUEST.equals(value)) {
                if (str.equals(str2)) {
                    str = null;
                }
                if (!str3.equals(guestAccess)) {
                    str4 = str3;
                }
                if (this.mRoom.getAliases().size() == 0) {
                    displayAccessRoomWarning();
                }
            } else {
                String str7 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## onRoomAccessPreferenceChanged(): unknown selected value = ");
                sb.append(value);
                Log.d(str7, sb.toString());
                str = null;
            }
        }
        if (str != null) {
            displayLoadingView();
            this.mRoom.updateJoinRules(str, this.mUpdateCallback);
        }
        if (str4 != null) {
            displayLoadingView();
            this.mRoom.updateGuestAccess(str4, this.mUpdateCallback);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x001f  */
    /* JADX WARNING: Removed duplicated region for block: B:12:? A[RETURN, SYNTHETIC] */
    private void onRoomDirectoryVisibilityPreferenceChanged() {
        String str;
        if (this.mRoom != null) {
            CheckBoxPreference checkBoxPreference = this.mRoomDirectoryVisibilitySwitch;
            if (checkBoxPreference != null) {
                str = checkBoxPreference.isChecked() ? "public" : RoomDirectoryVisibility.DIRECTORY_VISIBILITY_PRIVATE;
                if (str == null) {
                    String str2 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## onRoomDirectoryVisibilityPreferenceChanged(): directory visibility set to ");
                    sb.append(str);
                    Log.d(str2, sb.toString());
                    displayLoadingView();
                    this.mRoom.updateDirectoryVisibility(str, this.mUpdateCallback);
                    return;
                }
                return;
            }
        }
        Log.w(LOG_TAG, "## onRoomDirectoryVisibilityPreferenceChanged(): not processed due to invalid parameters");
        str = null;
        if (str == null) {
        }
    }

    private void onRoomNotificationsPreferenceChanged() {
        RoomNotificationState roomNotificationState;
        if (this.mRoom != null && this.mBingRulesManager != null) {
            String value = this.mRoomNotificationsPreference.getValue();
            if (TextUtils.equals(value, RoomNotificationState.ALL_MESSAGES_NOISY.name())) {
                roomNotificationState = RoomNotificationState.ALL_MESSAGES_NOISY;
            } else if (TextUtils.equals(value, RoomNotificationState.ALL_MESSAGES.name())) {
                roomNotificationState = RoomNotificationState.ALL_MESSAGES;
            } else if (TextUtils.equals(value, RoomNotificationState.MENTIONS_ONLY.name())) {
                roomNotificationState = RoomNotificationState.MENTIONS_ONLY;
            } else {
                roomNotificationState = RoomNotificationState.MUTE;
            }
            if (this.mBingRulesManager.getRoomNotificationState(this.mRoom.getRoomId()) != roomNotificationState) {
                displayLoadingView();
                this.mBingRulesManager.updateRoomNotificationState(this.mRoom.getRoomId(), roomNotificationState, new onBingRuleUpdateListener() {
                    public void onBingRuleUpdateSuccess() {
                        Log.d(VectorRoomSettingsFragment.LOG_TAG, "##onRoomNotificationsPreferenceChanged(): update succeed");
                        VectorRoomSettingsFragment.this.hideLoadingView(true);
                    }

                    public void onBingRuleUpdateFailure(String str) {
                        Log.w(VectorRoomSettingsFragment.LOG_TAG, "##onRoomNotificationsPreferenceChanged(): BingRuleUpdateFailure");
                        VectorRoomSettingsFragment.this.hideLoadingView(false);
                    }
                });
            }
        }
    }

    private void onRoomNamePreferenceChanged() {
        Room room = this.mRoom;
        if (room != null && this.mSession != null && this.mRoomNameEditTxt != null) {
            String str = room.getState().name;
            String text = this.mRoomNameEditTxt.getText();
            if (!TextUtils.equals(str, text)) {
                displayLoadingView();
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("##onRoomNamePreferenceChanged to ");
                sb.append(text);
                Log.d(str2, sb.toString());
                this.mRoom.updateName(text, this.mUpdateCallback);
                if (getActivity() instanceof VectorRoomDetailsActivity) {
                    ((VectorRoomDetailsActivity) getActivity()).setRoomTitle(text);
                }
            }
        }
    }

    private void onRoomTopicPreferenceChanged() {
        Room room = this.mRoom;
        if (room != null) {
            String topic = room.getTopic();
            String text = this.mRoomTopicEditTxt.getText();
            if (!TextUtils.equals(topic, text)) {
                displayLoadingView();
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## update topic to ");
                sb.append(text);
                Log.d(str, sb.toString());
                this.mRoom.updateTopic(text, this.mUpdateCallback);
                if (getActivity() instanceof VectorRoomDetailsActivity) {
                    ((VectorRoomDetailsActivity) getActivity()).setTopic(text);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onRoomAvatarPreferenceChanged() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                VectorRoomSettingsFragment.this.startActivityForResult(new Intent(VectorRoomSettingsFragment.this.getActivity(), SelectPictureActivity.class), 16);
            }
        });
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (16 == i) {
            onActivityResultRoomAvatarUpdate(i2, intent);
        }
    }

    private void onActivityResultRoomAvatarUpdate(int i, Intent intent) {
        if (this.mSession != null && i == -1) {
            Uri thumbnailUriFromIntent = VectorUtils.getThumbnailUriFromIntent(getActivity(), intent, this.mSession.getMediaCache());
            if (thumbnailUriFromIntent != null) {
                displayLoadingView();
                Resource openResource = ResourceUtils.openResource(getActivity(), thumbnailUriFromIntent, null);
                if (openResource != null) {
                    this.mSession.getMediaCache().uploadContent(openResource.mContentStream, null, openResource.mMimeType, null, new MXMediaUploadListener() {
                        public void onUploadError(String str, int i, String str2) {
                            VectorRoomSettingsFragment.this.getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Log.e(VectorRoomSettingsFragment.LOG_TAG, "Fail to upload the avatar");
                                    VectorRoomSettingsFragment.this.hideLoadingView(false);
                                }
                            });
                        }

                        public void onUploadComplete(String str, final String str2) {
                            VectorRoomSettingsFragment.this.getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Log.d(VectorRoomSettingsFragment.LOG_TAG, "The avatar has been uploaded, update the room avatar");
                                    VectorRoomSettingsFragment.this.mRoom.updateAvatarUrl(str2, VectorRoomSettingsFragment.this.mUpdateAvatarCallback);
                                }
                            });
                        }
                    });
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void displayLoadingView() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (VectorRoomSettingsFragment.this.mParentFragmentContainerView != null) {
                        VectorRoomSettingsFragment.this.mParentFragmentContainerView.setEnabled(false);
                    }
                    if (VectorRoomSettingsFragment.this.mParentLoadingView != null) {
                        VectorRoomSettingsFragment.this.mParentLoadingView.setVisibility(0);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void hideLoadingView(boolean z) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (VectorRoomSettingsFragment.this.mParentFragmentContainerView != null) {
                    VectorRoomSettingsFragment.this.mParentFragmentContainerView.setEnabled(true);
                }
                if (VectorRoomSettingsFragment.this.mParentLoadingView != null) {
                    VectorRoomSettingsFragment.this.mParentLoadingView.setVisibility(8);
                }
            }
        });
        if (z) {
            updateUiOnUiThread();
        }
    }

    /* access modifiers changed from: private */
    public void refreshBannedMembersList() {
        final PreferenceScreen preferenceScreen = getPreferenceScreen();
        preferenceScreen.removePreference(this.mBannedMembersSettingsCategoryDivider);
        preferenceScreen.removePreference(this.mBannedMembersSettingsCategory);
        this.mBannedMembersSettingsCategory.removeAll();
        this.mRoom.getMembersAsync(new SimpleApiCallback<List<RoomMember>>(getActivity()) {
            public void onSuccess(List<RoomMember> list) {
                ArrayList<RoomMember> arrayList = new ArrayList<>();
                if (list != null) {
                    for (RoomMember roomMember : list) {
                        if (TextUtils.equals(roomMember.membership, "ban")) {
                            arrayList.add(roomMember);
                        }
                    }
                }
                Collections.sort(arrayList, new Comparator<RoomMember>() {
                    public int compare(RoomMember roomMember, RoomMember roomMember2) {
                        return roomMember.getUserId().toLowerCase(VectorLocale.INSTANCE.getApplicationLocale()).compareTo(roomMember2.getUserId().toLowerCase(VectorLocale.INSTANCE.getApplicationLocale()));
                    }
                });
                if (arrayList.size() > 0) {
                    preferenceScreen.addPreference(VectorRoomSettingsFragment.this.mBannedMembersSettingsCategoryDivider);
                    preferenceScreen.addPreference(VectorRoomSettingsFragment.this.mBannedMembersSettingsCategory);
                    for (RoomMember roomMember2 : arrayList) {
                        VectorCustomActionEditTextPreference vectorCustomActionEditTextPreference = new VectorCustomActionEditTextPreference(VectorRoomSettingsFragment.this.getActivity());
                        final String userId = roomMember2.getUserId();
                        final String computeDisplayNameFromUserId = DinsicUtils.computeDisplayNameFromUserId(userId);
                        vectorCustomActionEditTextPreference.setTitle(computeDisplayNameFromUserId);
                        StringBuilder sb = new StringBuilder();
                        sb.append(VectorRoomSettingsFragment.BANNED_PREFERENCE_KEY_BASE);
                        sb.append(userId);
                        vectorCustomActionEditTextPreference.setKey(sb.toString());
                        vectorCustomActionEditTextPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                            public boolean onPreferenceClick(Preference preference) {
                                Intent intent = new Intent(VectorRoomSettingsFragment.this.getActivity(), VectorMemberDetailsActivity.class);
                                intent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_ID, userId);
                                intent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_DISPLAY_NAME, computeDisplayNameFromUserId);
                                intent.putExtra("EXTRA_ROOM_ID", VectorRoomSettingsFragment.this.mRoom.getRoomId());
                                intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", VectorRoomSettingsFragment.this.mSession.getCredentials().userId);
                                VectorRoomSettingsFragment.this.getActivity().startActivity(intent);
                                return false;
                            }
                        });
                        VectorRoomSettingsFragment.this.mBannedMembersSettingsCategory.addPreference(vectorCustomActionEditTextPreference);
                    }
                }
            }
        });
    }

    private boolean canUpdateFlair() {
        PowerLevels powerLevels = this.mRoom.getState().getPowerLevels();
        if (powerLevels == null || powerLevels.getUserPowerLevel(this.mSession.getMyUserId()) < powerLevels.minimumPowerLevelForSendingEventAsStateEvent(Event.EVENT_TYPE_STATE_RELATED_GROUPS)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void refreshFlair() {
        if (this.mFlairSettingsCategory != null) {
            final List<String> relatedGroups = this.mRoom.getState().getRelatedGroups();
            Collections.sort(relatedGroups, String.CASE_INSENSITIVE_ORDER);
            this.mFlairSettingsCategory.removeAll();
            if (!relatedGroups.isEmpty()) {
                for (final String str : relatedGroups) {
                    VectorCustomActionEditTextPreference vectorCustomActionEditTextPreference = new VectorCustomActionEditTextPreference(getActivity());
                    vectorCustomActionEditTextPreference.setTitle(str);
                    StringBuilder sb = new StringBuilder();
                    sb.append(FLAIR_PREFERENCE_KEY_BASE);
                    sb.append(str);
                    vectorCustomActionEditTextPreference.setKey(sb.toString());
                    vectorCustomActionEditTextPreference.setOnPreferenceLongClickListener(new OnPreferenceLongClickListener() {
                        public boolean onPreferenceLongClick(Preference preference) {
                            VectorRoomSettingsFragment.this.getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    VectorRoomSettingsFragment.this.displayLoadingView();
                                    VectorRoomSettingsFragment.this.mRoom.removeRelatedGroup(str, VectorRoomSettingsFragment.this.mFlairUpdatesCallback);
                                }
                            });
                            return true;
                        }
                    });
                    this.mFlairSettingsCategory.addPreference(vectorCustomActionEditTextPreference);
                }
            } else {
                VectorCustomActionEditTextPreference vectorCustomActionEditTextPreference2 = new VectorCustomActionEditTextPreference(getActivity());
                vectorCustomActionEditTextPreference2.setTitle(getString(R.string.room_settings_no_flair));
                vectorCustomActionEditTextPreference2.setKey("FLAIR_PREFERENCE_KEY_BASEno_flair");
                this.mFlairSettingsCategory.addPreference(vectorCustomActionEditTextPreference2);
            }
            if (canUpdateFlair()) {
                EditTextPreference editTextPreference = new EditTextPreference(getActivity());
                editTextPreference.setTitle(R.string.room_settings_add_new_group);
                editTextPreference.setDialogTitle(R.string.room_settings_add_new_group);
                editTextPreference.setKey("FLAIR_PREFERENCE_KEY_BASE__add");
                editTextPreference.setIcon(ThemeUtils.INSTANCE.tintDrawable(getActivity(), ContextCompat.getDrawable(getActivity(), R.drawable.ic_add_black), R.attr.vctr_settings_icon_tint_color));
                editTextPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object obj) {
                        final String trim = ((String) obj).trim();
                        if (!TextUtils.isEmpty(trim)) {
                            if (!MXPatterns.isGroupId(trim)) {
                                new Builder(VectorRoomSettingsFragment.this.getActivity()).setTitle((int) R.string.room_settings_invalid_group_format_dialog_title).setMessage((CharSequence) VectorRoomSettingsFragment.this.getString(R.string.room_settings_invalid_group_format_dialog_body, new Object[]{trim})).setPositiveButton((int) R.string.ok, (OnClickListener) null).show();
                            } else if (!relatedGroups.contains(trim)) {
                                VectorRoomSettingsFragment.this.getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        VectorRoomSettingsFragment.this.displayLoadingView();
                                        VectorRoomSettingsFragment.this.mRoom.addRelatedGroup(trim, VectorRoomSettingsFragment.this.mFlairUpdatesCallback);
                                    }
                                });
                            }
                        }
                        return false;
                    }
                });
                this.mFlairSettingsCategory.addPreference(editTextPreference);
            }
        }
    }

    private void onAddressLongClick(final String str, View view) {
        Activity activity = getActivity();
        PopupMenu popupMenu = VERSION.SDK_INT >= 19 ? new PopupMenu(activity, view, GravityCompat.END) : new PopupMenu(activity, view);
        popupMenu.getMenuInflater().inflate(R.menu.vector_room_settings_addresses, popupMenu.getMenu());
        boolean z = false;
        try {
            Field[] declaredFields = popupMenu.getClass().getDeclaredFields();
            int length = declaredFields.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Field field = declaredFields[i];
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object obj = field.get(popupMenu);
                    Class.forName(obj.getClass().getName()).getMethod("setForceShowIcon", new Class[]{Boolean.TYPE}).invoke(obj, new Object[]{Boolean.valueOf(true)});
                    break;
                }
                i++;
            }
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onMessageClick : force to display the icons failed ");
            sb.append(e.getLocalizedMessage());
            Log.e(str2, sb.toString(), e);
        }
        Menu menu = popupMenu.getMenu();
        ThemeUtils.INSTANCE.tintMenuIcons(menu, ThemeUtils.INSTANCE.getColor(activity, R.attr.vctr_icon_tint_on_light_action_bar_color));
        String canonicalAlias = this.mRoom.getState().getCanonicalAlias();
        final boolean canUpdateCanonicalAlias = canUpdateCanonicalAlias();
        menu.findItem(R.id.ic_action_vector_delete_alias).setVisible(true);
        menu.findItem(R.id.ic_action_vector_set_as_main_address).setVisible(canUpdateCanonicalAlias && !TextUtils.equals(str, canonicalAlias));
        MenuItem findItem = menu.findItem(R.id.ic_action_vector_unset_main_address);
        if (canUpdateCanonicalAlias && TextUtils.equals(str, canonicalAlias)) {
            z = true;
        }
        findItem.setVisible(z);
        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.ic_action_vector_unset_main_address) {
                    new Builder(VectorRoomSettingsFragment.this.getActivity()).setMessage((int) R.string.room_settings_addresses_disable_main_address_prompt_msg).setTitle((int) R.string.room_settings_addresses_disable_main_address_prompt_title).setPositiveButton((int) R.string.yes, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            VectorRoomSettingsFragment.this.displayLoadingView();
                            VectorRoomSettingsFragment.this.mRoom.updateCanonicalAlias(null, VectorRoomSettingsFragment.this.mAliasUpdatesCallback);
                        }
                    }).setNegativeButton((int) R.string.no, (OnClickListener) null).show();
                } else if (menuItem.getItemId() == R.id.ic_action_vector_set_as_main_address) {
                    VectorRoomSettingsFragment.this.displayLoadingView();
                    VectorRoomSettingsFragment.this.mRoom.updateCanonicalAlias(str, VectorRoomSettingsFragment.this.mAliasUpdatesCallback);
                } else if (menuItem.getItemId() == R.id.ic_action_vector_delete_alias) {
                    VectorRoomSettingsFragment.this.displayLoadingView();
                    VectorRoomSettingsFragment.this.mRoom.removeAlias(str, new SimpleApiCallback<Void>(VectorRoomSettingsFragment.this.mAliasUpdatesCallback) {
                        public void onSuccess(Void voidR) {
                            if (VectorRoomSettingsFragment.this.mRoom.getAliases().size() != 1 || !canUpdateCanonicalAlias) {
                                VectorRoomSettingsFragment.this.mAliasUpdatesCallback.onSuccess(voidR);
                            } else {
                                VectorRoomSettingsFragment.this.mRoom.updateCanonicalAlias((String) VectorRoomSettingsFragment.this.mRoom.getAliases().get(0), VectorRoomSettingsFragment.this.mAliasUpdatesCallback);
                            }
                        }
                    });
                } else if (menuItem.getItemId() == R.id.ic_action_vector_room_url) {
                    SystemUtilsKt.copyToClipboard(VectorRoomSettingsFragment.this.getActivity(), PermalinkUtils.createPermalink(str));
                } else {
                    SystemUtilsKt.copyToClipboard(VectorRoomSettingsFragment.this.getActivity(), str);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private boolean canUpdateCanonicalAlias() {
        PowerLevels powerLevels = this.mRoom.getState().getPowerLevels();
        if (powerLevels == null || powerLevels.getUserPowerLevel(this.mSession.getMyUserId()) < powerLevels.minimumPowerLevelForSendingEventAsStateEvent(Event.EVENT_TYPE_STATE_CANONICAL_ALIAS)) {
            return false;
        }
        return true;
    }
}
