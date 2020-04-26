package fr.gouv.tchap.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.sdk.session.room.model.RoomAccessRulesKt;
import fr.gouv.tchap.sdk.session.room.model.RoomRetentionKt;
import fr.gouv.tchap.util.DinsicUtils;
import fr.gouv.tchap.util.DinumUtilsKt;
import fr.gouv.tchap.util.HexagonMaskView;
import fr.gouv.tchap.util.RoomRetentionPeriodPickerDialogFragment;
import im.vector.Matrix;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.MXCActionBarActivity;
import im.vector.activity.SelectPictureActivity;
import im.vector.activity.VectorRoomActivity;
import im.vector.activity.VectorRoomInviteMembersActivity;
import im.vector.activity.VectorRoomInviteMembersActivity.ActionMode;
import im.vector.activity.VectorRoomInviteMembersActivity.ContactsFilter;
import im.vector.util.VectorUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.ResourceUtils;
import org.matrix.androidsdk.core.ResourceUtils.Resource;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.listeners.MXMediaUploadListener;
import org.matrix.androidsdk.rest.model.CreateRoomParams;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.RoomDirectoryVisibility;

public class TchapRoomCreationActivity extends MXCActionBarActivity {
    private static final String ERROR_CODE_ROOM_ALIAS_ALREADY_TAKEN = "Room alias already taken";
    private static final String ERROR_CODE_ROOM_ALIAS_INVALID_CHARACTERS = "Invalid characters in room alias";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = TchapRoomCreationActivity.class.getSimpleName();
    private static final int REQ_CODE_ADD_PARTICIPANTS = 17;
    private static final int REQ_CODE_UPDATE_ROOM_AVATAR = 16;
    @BindView(2131297126)
    TextView addAvatarText;
    @BindView(2131297066)
    Switch disableFederationSwitch;
    @BindView(2131296480)
    TextInputEditText etRoomName;
    @BindView(2131297067)
    Switch externalAccessRoomSwitch;
    @BindView(2131296900)
    View hexagonAvatar;
    @BindView(2131296581)
    HexagonMaskView hexagonMaskView;
    private List<String> mParticipantsIds = new ArrayList();
    /* access modifiers changed from: private */
    public int mRetentionPeriod = RoomRetentionKt.DEFAULT_RETENTION_VALUE_IN_DAYS;
    /* access modifiers changed from: private */
    public CreateRoomParams mRoomParams = new CreateRoomParams();
    private MXSession mSession;
    /* access modifiers changed from: private */
    public Uri mThumbnailUri = null;
    @BindView(2131297068)
    Switch publicPrivateRoomSwitch;
    @BindView(2131297142)
    TextView roomMessageRetentionText;
    @BindView(2131297141)
    TextView tvPublicPrivateRoomDescription;

    public int getLayoutRes() {
        return R.layout.activity_tchap_room_creation;
    }

    public int getMenuRes() {
        return R.menu.tchap_menu_next;
    }

    public void initUiAndData() {
        setWaitingView(findViewById(R.id.room_creation_spinner_views));
        this.mSession = Matrix.getInstance(this).getDefaultSession();
        setTitle(R.string.tchap_room_creation_title);
        setRoomRetentionPeriod(RoomRetentionKt.DEFAULT_RETENTION_VALUE_IN_DAYS);
        this.roomMessageRetentionText.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new RoomRetentionPeriodPickerDialogFragment(TchapRoomCreationActivity.this).create(TchapRoomCreationActivity.this.mRetentionPeriod, new Function1() {
                    public final Object invoke(Object obj) {
                        return AnonymousClass1.this.lambda$onClick$0$TchapRoomCreationActivity$1((Integer) obj);
                    }
                }).show();
            }

            public /* synthetic */ Unit lambda$onClick$0$TchapRoomCreationActivity$1(Integer num) {
                TchapRoomCreationActivity.this.setRoomRetentionPeriod(num.intValue());
                return null;
            }
        });
        this.roomMessageRetentionText.setVisibility(8);
        this.externalAccessRoomSwitch.setChecked(false);
        if (DinumUtilsKt.isSecure()) {
            this.externalAccessRoomSwitch.setVisibility(8);
        }
        setRoomAccessRule(RoomAccessRulesKt.RESTRICTED);
        this.publicPrivateRoomSwitch.setChecked(false);
        CreateRoomParams createRoomParams = this.mRoomParams;
        createRoomParams.visibility = RoomDirectoryVisibility.DIRECTORY_VISIBILITY_PRIVATE;
        createRoomParams.preset = CreateRoomParams.PRESET_PRIVATE_CHAT;
        createRoomParams.setHistoryVisibility(RoomState.HISTORY_VISIBILITY_INVITED);
        this.mRoomParams.powerLevelContentOverride = new HashMap<String, Object>() {
            {
                put("invite", Float.valueOf(50.0f));
            }
        };
        String homeServerDisplayNameFromMXIdentifier = DinsicUtils.getHomeServerDisplayNameFromMXIdentifier(this.mSession.getMyUserId());
        this.disableFederationSwitch.setText(getString(R.string.tchap_room_creation_disable_federation, new Object[]{homeServerDisplayNameFromMXIdentifier}));
        this.hexagonMaskView.setBorderSettings(ContextCompat.getColor(this, R.color.restricted_room_avatar_border_color), 3);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            finish();
            return true;
        } else if (itemId != R.id.action_next) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            if (this.mRoomParams.preset.equals(CreateRoomParams.PRESET_PUBLIC_CHAT)) {
                CreateRoomParams createRoomParams = this.mRoomParams;
                String str = "";
                createRoomParams.roomAliasName = createRoomParams.name.trim().replace(" ", str);
                String str2 = ":";
                if (this.mRoomParams.roomAliasName.contains(str2)) {
                    CreateRoomParams createRoomParams2 = this.mRoomParams;
                    createRoomParams2.roomAliasName = createRoomParams2.roomAliasName.replace(str2, str);
                }
                if (this.mRoomParams.roomAliasName.isEmpty()) {
                    this.mRoomParams.roomAliasName = getRandomString();
                } else {
                    CreateRoomParams createRoomParams3 = this.mRoomParams;
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.mRoomParams.roomAliasName);
                    sb.append(getRandomString());
                    createRoomParams3.roomAliasName = sb.toString();
                }
            }
            inviteMembers(17);
            hideKeyboard();
            return true;
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_next).setEnabled(this.mRoomParams.name != null && !isWaitingViewVisible());
        return super.onPrepareOptionsMenu(menu);
    }

    private void hideKeyboard() {
        ((InputMethodManager) getSystemService("input_method")).toggleSoftInput(2, 0);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296900})
    public void addRoomAvatar() {
        startActivityForResult(new Intent(this, SelectPictureActivity.class), 16);
    }

    /* access modifiers changed from: 0000 */
    @OnCheckedChanged({2131297067})
    public void setRoomExternalAccess() {
        if (this.externalAccessRoomSwitch.isChecked()) {
            Log.d(LOG_TAG, "## unrestricted");
            setRoomAccessRule(RoomAccessRulesKt.UNRESTRICTED);
            this.hexagonMaskView.setBorderSettings(ContextCompat.getColor(this, R.color.unrestricted_room_avatar_border_color), 10);
            return;
        }
        Log.d(LOG_TAG, "## restricted");
        setRoomAccessRule(RoomAccessRulesKt.RESTRICTED);
        this.hexagonMaskView.setBorderSettings(ContextCompat.getColor(this, R.color.restricted_room_avatar_border_color), 3);
        if (!this.mParticipantsIds.isEmpty()) {
            int i = 0;
            while (i < this.mParticipantsIds.size()) {
                String str = (String) this.mParticipantsIds.get(i);
                if (DinsicUtils.isExternalTchapUser(str)) {
                    this.mParticipantsIds.remove(str);
                } else {
                    i++;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @OnCheckedChanged({2131297068})
    public void setRoomPrivacy() {
        if (this.publicPrivateRoomSwitch.isChecked()) {
            this.tvPublicPrivateRoomDescription.setTextColor(ContextCompat.getColor(this, R.color.vector_fuchsia_color));
            CreateRoomParams createRoomParams = this.mRoomParams;
            createRoomParams.visibility = "public";
            createRoomParams.preset = CreateRoomParams.PRESET_PUBLIC_CHAT;
            createRoomParams.setHistoryVisibility(RoomState.HISTORY_VISIBILITY_WORLD_READABLE);
            Log.d(LOG_TAG, "## public");
            this.disableFederationSwitch.setVisibility(0);
            updateRoomExternalAccessOption(false);
            return;
        }
        this.tvPublicPrivateRoomDescription.setTextColor(ContextCompat.getColor(this, R.color.vector_tchap_text_color_light_grey));
        CreateRoomParams createRoomParams2 = this.mRoomParams;
        createRoomParams2.visibility = RoomDirectoryVisibility.DIRECTORY_VISIBILITY_PRIVATE;
        createRoomParams2.preset = CreateRoomParams.PRESET_PRIVATE_CHAT;
        createRoomParams2.setHistoryVisibility(RoomState.HISTORY_VISIBILITY_INVITED);
        Log.d(LOG_TAG, "## private");
        this.disableFederationSwitch.setChecked(false);
        this.disableFederationSwitch.setVisibility(8);
        this.mRoomParams.creation_content = null;
        updateRoomExternalAccessOption(true);
    }

    /* access modifiers changed from: 0000 */
    @OnCheckedChanged({2131297066})
    public void setRoomFederation() {
        if (this.disableFederationSwitch.isChecked()) {
            HashMap hashMap = new HashMap();
            int i = 0;
            hashMap.put("m.federate", Boolean.valueOf(false));
            this.mRoomParams.creation_content = hashMap;
            Log.d(LOG_TAG, "## not federated");
            if (!this.mParticipantsIds.isEmpty()) {
                String homeServerNameFromMXIdentifier = DinsicUtils.getHomeServerNameFromMXIdentifier(this.mSession.getMyUserId());
                while (i < this.mParticipantsIds.size()) {
                    String str = (String) this.mParticipantsIds.get(i);
                    if (!DinsicUtils.getHomeServerNameFromMXIdentifier(str).equals(homeServerNameFromMXIdentifier)) {
                        this.mParticipantsIds.remove(str);
                    } else {
                        i++;
                    }
                }
                return;
            }
            return;
        }
        this.mRoomParams.creation_content = null;
        Log.d(LOG_TAG, "## federated");
    }

    /* access modifiers changed from: protected */
    @OnTextChanged({2131296480})
    public void onTextChanged(CharSequence charSequence) {
        String trim = charSequence.toString().trim();
        if (!trim.isEmpty()) {
            this.mRoomParams.name = trim;
        } else {
            this.mRoomParams.name = null;
        }
        invalidateOptionsMenu();
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("room name:");
        sb.append(this.mRoomParams.name);
        Log.i(str, sb.toString());
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 16) {
            onActivityResultRoomAvatarUpdate(i2, intent);
        } else if (i == 17) {
            this.mParticipantsIds = intent.getStringArrayListExtra(VectorRoomInviteMembersActivity.EXTRA_OUT_SELECTED_USER_IDS);
            if (i2 == -1) {
                showWaitingView();
                invalidateOptionsMenu();
                this.mRoomParams.invitedUserIds = this.mParticipantsIds;
                createNewRoom();
            } else if (i2 == 0) {
                invalidateOptionsMenu();
            }
        }
    }

    /* access modifiers changed from: private */
    public void setRoomRetentionPeriod(int i) {
        List<Event> list = this.mRoomParams.initialStates;
        String str = RoomRetentionKt.EVENT_TYPE_STATE_ROOM_RETENTION;
        if (list != null && !this.mRoomParams.initialStates.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            for (Event event : this.mRoomParams.initialStates) {
                if (!event.type.equals(str)) {
                    arrayList.add(event);
                }
            }
            this.mRoomParams.initialStates = arrayList;
        }
        Event event2 = new Event();
        event2.type = str;
        HashMap hashMap = new HashMap();
        hashMap.put(RoomRetentionKt.STATE_EVENT_CONTENT_MAX_LIFETIME, Long.valueOf(DinumUtilsKt.convertDaysToMs(i)));
        hashMap.put(RoomRetentionKt.STATE_EVENT_CONTENT_EXPIRE_ON_CLIENTS, Boolean.valueOf(true));
        event2.updateContent(JsonUtils.getGson(false).toJsonTree(hashMap));
        event2.stateKey = "";
        if (this.mRoomParams.initialStates == null) {
            this.mRoomParams.initialStates = Arrays.asList(new Event[]{event2});
        } else {
            this.mRoomParams.initialStates.add(event2);
        }
        SpannableString spannableString = new SpannableString(getResources().getQuantityString(R.plurals.tchap_room_creation_retention, i, new Object[]{Integer.valueOf(i)}));
        int indexOf = spannableString.toString().indexOf(String.valueOf(i));
        if (indexOf >= 0) {
            spannableString.setSpan(new UnderlineSpan(), indexOf, spannableString.length(), 0);
        }
        this.roomMessageRetentionText.setText(spannableString);
        this.mRetentionPeriod = i;
    }

    private void setRoomAccessRule(String str) {
        List<Event> list = this.mRoomParams.initialStates;
        String str2 = RoomAccessRulesKt.EVENT_TYPE_STATE_ROOM_ACCESS_RULES;
        if (list != null && !this.mRoomParams.initialStates.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            for (Event event : this.mRoomParams.initialStates) {
                if (!event.type.equals(str2)) {
                    arrayList.add(event);
                }
            }
            this.mRoomParams.initialStates = arrayList;
        }
        if (!TextUtils.isEmpty(str)) {
            Event event2 = new Event();
            event2.type = str2;
            HashMap hashMap = new HashMap();
            hashMap.put(RoomAccessRulesKt.STATE_EVENT_CONTENT_KEY_RULE, str);
            event2.updateContent(JsonUtils.getGson(false).toJsonTree(hashMap));
            event2.stateKey = "";
            if (this.mRoomParams.initialStates == null) {
                this.mRoomParams.initialStates = Arrays.asList(new Event[]{event2});
                return;
            }
            this.mRoomParams.initialStates.add(event2);
        }
    }

    private void updateRoomExternalAccessOption(boolean z) {
        this.externalAccessRoomSwitch.setEnabled(z);
        if (z) {
            this.externalAccessRoomSwitch.setTextColor(ContextCompat.getColor(this, R.color.vector_tchap_text_color_dark));
            return;
        }
        this.externalAccessRoomSwitch.setTextColor(ContextCompat.getColor(this, R.color.vector_tchap_text_color_light_grey));
        this.externalAccessRoomSwitch.setChecked(false);
    }

    private void onActivityResultRoomAvatarUpdate(int i, Intent intent) {
        MXSession mXSession = this.mSession;
        if (mXSession != null && i == -1) {
            this.mThumbnailUri = VectorUtils.getThumbnailUriFromIntent(this, intent, mXSession.getMediaCache());
            if (this.mThumbnailUri != null) {
                this.addAvatarText.setVisibility(8);
                this.hexagonMaskView.setBackgroundColor(-1);
                Glide.with((FragmentActivity) this).load(this.mThumbnailUri).apply(new RequestOptions().override(this.hexagonMaskView.getWidth(), this.hexagonMaskView.getHeight()).centerCrop()).into((ImageView) this.hexagonMaskView);
            }
        }
    }

    /* access modifiers changed from: private */
    public void createNewRoom() {
        this.mSession.createRoom(this.mRoomParams, new SimpleApiCallback<String>(this) {
            public void onSuccess(String str) {
                if (TchapRoomCreationActivity.this.mThumbnailUri != null) {
                    TchapRoomCreationActivity.this.hideWaitingView();
                    TchapRoomCreationActivity tchapRoomCreationActivity = TchapRoomCreationActivity.this;
                    tchapRoomCreationActivity.uploadRoomAvatar(str, tchapRoomCreationActivity.mThumbnailUri);
                    return;
                }
                TchapRoomCreationActivity.this.hideWaitingView();
                TchapRoomCreationActivity.this.openRoom(str);
            }

            private void onError(final String str) {
                TchapRoomCreationActivity.this.getWaitingView().post(new Runnable() {
                    public void run() {
                        if (str != null) {
                            Log.e(TchapRoomCreationActivity.LOG_TAG, "Fail to create the room");
                            Toast.makeText(TchapRoomCreationActivity.this, str, 1).show();
                        }
                        TchapRoomCreationActivity.this.hideWaitingView();
                        TchapRoomCreationActivity.this.invalidateOptionsMenu();
                    }
                });
            }

            public void onNetworkError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                    TchapRoomCreationActivity.this.hideWaitingView();
                    TchapRoomCreationActivity.this.getConsentNotGivenHelper().displayDialog(matrixError);
                    return;
                }
                String str = matrixError.error;
                char c = 65535;
                int hashCode = str.hashCode();
                if (hashCode != -654871926) {
                    if (hashCode == 737628057 && str.equals(TchapRoomCreationActivity.ERROR_CODE_ROOM_ALIAS_INVALID_CHARACTERS)) {
                        c = 0;
                    }
                } else if (str.equals(TchapRoomCreationActivity.ERROR_CODE_ROOM_ALIAS_ALREADY_TAKEN)) {
                    c = 1;
                }
                if (c == 0) {
                    TchapRoomCreationActivity.this.hideWaitingView();
                    TchapRoomCreationActivity.this.mRoomParams.roomAliasName = TchapRoomCreationActivity.this.getRandomString();
                    TchapRoomCreationActivity.this.createNewRoom();
                } else if (c != 1) {
                    onError(matrixError.getLocalizedMessage());
                } else {
                    TchapRoomCreationActivity.this.hideWaitingView();
                    TchapRoomCreationActivity.this.mRoomParams.roomAliasName = TchapRoomCreationActivity.this.getRandomString();
                    TchapRoomCreationActivity.this.createNewRoom();
                }
            }

            public void onUnexpectedError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }
        });
    }

    /* access modifiers changed from: private */
    public void uploadRoomAvatar(final String str, final Uri uri) {
        showWaitingView();
        Resource openResource = ResourceUtils.openResource(this, this.mThumbnailUri, null);
        if (openResource != null) {
            this.mSession.getMediaCache().uploadContent(openResource.mContentStream, null, openResource.mMimeType, null, new MXMediaUploadListener() {
                public void onUploadError(String str, int i, String str2) {
                    TchapRoomCreationActivity.this.hideWaitingView();
                    Log.e(TchapRoomCreationActivity.LOG_TAG, "Fail to upload the avatar");
                    TchapRoomCreationActivity.this.promptRoomAvatarError(new ValueCallback<Boolean>() {
                        public void onReceiveValue(Boolean bool) {
                            if (bool.booleanValue()) {
                                TchapRoomCreationActivity.this.uploadRoomAvatar(str, uri);
                            } else {
                                TchapRoomCreationActivity.this.openRoom(str);
                            }
                        }
                    });
                }

                public void onUploadComplete(String str, String str2) {
                    TchapRoomCreationActivity.this.hideWaitingView();
                    TchapRoomCreationActivity.this.updateRoomAvatar(str, str2);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void updateRoomAvatar(final String str, final String str2) {
        showWaitingView();
        Log.d(LOG_TAG, "The avatar has been uploaded, update the room avatar");
        this.mSession.getDataHandler().getRoom(str).updateAvatarUrl(str2, new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                TchapRoomCreationActivity.this.hideWaitingView();
                TchapRoomCreationActivity.this.openRoom(str);
            }

            private void onError(String str) {
                TchapRoomCreationActivity.this.hideWaitingView();
                String access$500 = TchapRoomCreationActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## updateAvatarUrl() failed ");
                sb.append(str);
                Log.e(access$500, sb.toString());
                TchapRoomCreationActivity.this.promptRoomAvatarError(new ValueCallback<Boolean>() {
                    public void onReceiveValue(Boolean bool) {
                        if (bool.booleanValue()) {
                            TchapRoomCreationActivity.this.updateRoomAvatar(str, str2);
                        } else {
                            TchapRoomCreationActivity.this.openRoom(str);
                        }
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
        });
    }

    /* access modifiers changed from: private */
    public void openRoom(String str) {
        Log.d(LOG_TAG, "## openRoom(): start VectorHomeActivity..");
        HashMap hashMap = new HashMap();
        hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getMyUserId());
        hashMap.put("EXTRA_ROOM_ID", str);
        hashMap.put(VectorRoomActivity.EXTRA_EXPAND_ROOM_HEADER, Boolean.valueOf(true));
        CommonActivityUtils.goToRoomPage(this, this.mSession, hashMap);
    }

    /* access modifiers changed from: private */
    public void promptRoomAvatarError(final ValueCallback<Boolean> valueCallback) {
        hideWaitingView();
        new Builder(this).setMessage((int) R.string.tchap_room_creation_save_avatar_failed).setPositiveButton((int) R.string.resend, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                valueCallback.onReceiveValue(Boolean.valueOf(true));
                dialogInterface.dismiss();
            }
        }).setNegativeButton((int) R.string.auth_skip, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                valueCallback.onReceiveValue(Boolean.valueOf(false));
                dialogInterface.dismiss();
            }
        }).show();
    }

    private void inviteMembers(int i) {
        Intent intent = new Intent(this, VectorRoomInviteMembersActivity.class);
        intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getMyUserId());
        intent.putExtra(VectorRoomInviteMembersActivity.EXTRA_ACTION_ACTIVITY_MODE, ActionMode.RETURN_SELECTED_USER_IDS);
        Object obj = this.mRoomParams.creation_content;
        String str = VectorRoomInviteMembersActivity.EXTRA_CONTACTS_FILTER;
        if (obj != null) {
            intent.putExtra(str, ContactsFilter.TCHAP_USERS_ONLY_WITHOUT_FEDERATION);
        } else if (this.externalAccessRoomSwitch.isChecked()) {
            intent.putExtra(str, ContactsFilter.TCHAP_USERS_ONLY);
        } else {
            intent.putExtra(str, ContactsFilter.TCHAP_USERS_ONLY_WITHOUT_EXTERNALS);
        }
        if (!this.mParticipantsIds.isEmpty()) {
            intent.putExtra(VectorRoomInviteMembersActivity.EXTRA_IN_SELECTED_USER_IDS, (Serializable) this.mParticipantsIds);
        }
        startActivityForResult(intent, i);
    }

    /* access modifiers changed from: protected */
    public String getRandomString() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        while (sb.length() < 7) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".charAt((int) (random.nextFloat() * ((float) 36))));
        }
        return sb.toString();
    }
}
