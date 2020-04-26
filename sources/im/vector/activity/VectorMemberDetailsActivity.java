package im.vector.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.activity.TchapContactActionBarActivity;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.Matrix;
import im.vector.adapters.VectorMemberDetailsAdapter;
import im.vector.adapters.VectorMemberDetailsAdapter.AdapterMemberActionItems;
import im.vector.adapters.VectorMemberDetailsAdapter.IEnablingActions;
import im.vector.adapters.VectorMemberDetailsDevicesAdapter;
import im.vector.adapters.VectorMemberDetailsDevicesAdapter.IDevicesAdapterListener;
import im.vector.extensions.MatrixSdkExtensionsKt;
import im.vector.fragments.VectorUnknownDevicesFragment.IUnknownDevicesSendAnywayListener;
import im.vector.ui.themes.ActivityOtherThemes;
import im.vector.ui.themes.ActivityOtherThemes.NoActionBar;
import im.vector.util.CallsManager;
import im.vector.util.PermissionsToolsKt;
import im.vector.util.RoomUtils;
import im.vector.util.VectorUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.call.IMXCall;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.MXCryptoError;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.PowerLevels;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.User;

public class VectorMemberDetailsActivity extends TchapContactActionBarActivity implements IEnablingActions, IDevicesAdapterListener {
    private static final String AVATAR_FULLSCREEN_MODE = "AVATAR_FULLSCREEN_MODE";
    public static final String EXTRA_MEMBER_AVATAR_URL = "EXTRA_MEMBER_AVATAR_URL";
    public static final String EXTRA_MEMBER_DISPLAY_NAME = "EXTRA_MEMBER_DISPLAY_NAME";
    public static final String EXTRA_MEMBER_ID = "EXTRA_MEMBER_ID";
    public static final String EXTRA_ROOM_ID = "EXTRA_ROOM_ID";
    public static final String EXTRA_STORE_ID = "EXTRA_STORE_ID";
    public static final int ITEM_ACTION_BAN = 3;
    private static final int ITEM_ACTION_DEVICES = 15;
    private static final int ITEM_ACTION_DIALOG = 16;
    private static final int ITEM_ACTION_IGNORE = 5;
    private static final int ITEM_ACTION_INVITE = 0;
    public static final int ITEM_ACTION_KICK = 2;
    private static final int ITEM_ACTION_LEAVE = 1;
    private static final int ITEM_ACTION_MENTION = 14;
    private static final int ITEM_ACTION_SET_ADMIN = 9;
    private static final int ITEM_ACTION_SET_DEFAULT_POWER_LEVEL = 7;
    private static final int ITEM_ACTION_SET_MODERATOR = 8;
    private static final int ITEM_ACTION_START_CHAT = 11;
    private static final int ITEM_ACTION_START_VIDEO_CALL = 13;
    private static final int ITEM_ACTION_START_VOICE_CALL = 12;
    private static final int ITEM_ACTION_UNBAN = 4;
    private static final int ITEM_ACTION_UNIGNORE = 6;
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorMemberDetailsActivity.class.getSimpleName();
    public static final String RESULT_MENTION_ID = "RESULT_MENTION_ID";
    private static final int VECTOR_ROOM_ADMIN_LEVEL = 100;
    private static final int VECTOR_ROOM_MODERATOR_LEVEL = 50;
    /* access modifiers changed from: private */
    public Room mCallableRoom;
    private final ApiCallback<String> mCreateDirectMessageCallBack = new ApiCallback<String>() {
        public void onSuccess(String str) {
            HashMap hashMap = new HashMap();
            hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", VectorMemberDetailsActivity.this.mSession.getMyUserId());
            hashMap.put("EXTRA_ROOM_ID", str);
            hashMap.put(VectorRoomActivity.EXTRA_EXPAND_ROOM_HEADER, Boolean.valueOf(true));
            Log.d(VectorMemberDetailsActivity.LOG_TAG, "## mCreateDirectMessageCallBack: onSuccess - start goToRoomPage");
            VectorMemberDetailsActivity vectorMemberDetailsActivity = VectorMemberDetailsActivity.this;
            CommonActivityUtils.goToRoomPage(vectorMemberDetailsActivity, vectorMemberDetailsActivity.mSession, hashMap);
        }

        public void onMatrixError(MatrixError matrixError) {
            String access$100 = VectorMemberDetailsActivity.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## mCreateDirectMessageCallBack: onMatrixError Msg=");
            sb.append(matrixError.getLocalizedMessage());
            Log.d(access$100, sb.toString());
            VectorMemberDetailsActivity.this.mRoomActionsListener.onMatrixError(matrixError);
        }

        public void onNetworkError(Exception exc) {
            String access$100 = VectorMemberDetailsActivity.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## mCreateDirectMessageCallBack: onNetworkError Msg=");
            sb.append(exc.getLocalizedMessage());
            Log.d(access$100, sb.toString());
            VectorMemberDetailsActivity.this.mRoomActionsListener.onNetworkError(exc);
        }

        public void onUnexpectedError(Exception exc) {
            String access$100 = VectorMemberDetailsActivity.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## mCreateDirectMessageCallBack: onUnexpectedError Msg=");
            sb.append(exc.getLocalizedMessage());
            Log.d(access$100, sb.toString());
            VectorMemberDetailsActivity.this.mRoomActionsListener.onUnexpectedError(exc);
        }
    };
    @BindView(2131296441)
    View mDevicesListHeaderView;
    @BindView(2131296754)
    ListView mDevicesListView;
    /* access modifiers changed from: private */
    public VectorMemberDetailsDevicesAdapter mDevicesListViewAdapter;
    private final ApiCallback<Void> mDevicesVerificationCallback = new ApiCallback<Void>() {
        public void onSuccess(Void voidR) {
            VectorMemberDetailsActivity.this.mDevicesListViewAdapter.notifyDataSetChanged();
        }

        public void onNetworkError(Exception exc) {
            VectorMemberDetailsActivity.this.mDevicesListViewAdapter.notifyDataSetChanged();
        }

        public void onMatrixError(MatrixError matrixError) {
            VectorMemberDetailsActivity.this.mDevicesListViewAdapter.notifyDataSetChanged();
        }

        public void onUnexpectedError(Exception exc) {
            VectorMemberDetailsActivity.this.mDevicesListViewAdapter.notifyDataSetChanged();
        }
    };
    @BindView(2131296752)
    ExpandableListView mExpandableListView;
    @BindView(2131296755)
    ImageView mFullMemberAvatarImageView;
    @BindView(2131296756)
    View mFullMemberAvatarLayout;
    /* access modifiers changed from: private */
    public VectorMemberDetailsAdapter mListViewAdapter;
    private final MXEventListener mLiveEventsListener = new MXEventListener() {
        public void onLiveEvent(Event event, RoomState roomState) {
            String type = event.getType();
            if ("m.room.member".equals(type) || Event.EVENT_TYPE_STATE_ROOM_POWER_LEVELS.equals(type)) {
                VectorMemberDetailsActivity.this.checkRoomMemberStatus(new SimpleApiCallback<Boolean>() {
                    public void onSuccess(final Boolean bool) {
                        VectorMemberDetailsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                if (bool.booleanValue()) {
                                    VectorMemberDetailsActivity.this.updateUi();
                                } else if (VectorMemberDetailsActivity.this.mRoom != null) {
                                    VectorMemberDetailsActivity.this.finish();
                                }
                            }
                        });
                    }
                });
            }
        }

        public void onLeaveRoom(String str) {
            VectorMemberDetailsActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Intent intent = new Intent(VectorMemberDetailsActivity.this, VectorHomeActivity.class);
                    intent.setFlags(603979776);
                    VectorMemberDetailsActivity.this.startActivity(intent);
                }
            });
        }
    };
    @BindView(2131296322)
    ImageView mMemberAvatarImageView;
    /* access modifiers changed from: private */
    public String mMemberId;
    private final MXEventListener mPresenceEventsListener = new MXEventListener() {
        public void onPresenceUpdate(Event event, User user) {
            if (VectorMemberDetailsActivity.this.mMemberId.equals(user.user_id)) {
                VectorMemberDetailsActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        VectorMemberDetailsActivity.this.updateMemberAvatarUi();
                        VectorMemberDetailsActivity.this.updatePresenceInfoUi();
                    }
                });
            }
        }
    };
    @BindView(2131296759)
    TextView mPresenceTextView;
    /* access modifiers changed from: private */
    public Room mRoom;
    /* access modifiers changed from: private */
    public final ApiCallback<Void> mRoomActionsListener = new SimpleApiCallback<Void>(this) {
        public void onMatrixError(MatrixError matrixError) {
            if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                VectorMemberDetailsActivity.this.getConsentNotGivenHelper().displayDialog(matrixError);
            } else {
                Toast.makeText(VectorMemberDetailsActivity.this, matrixError.getLocalizedMessage(), 1).show();
            }
            VectorMemberDetailsActivity.this.updateUi();
        }

        public void onSuccess(Void voidR) {
            VectorMemberDetailsActivity.this.updateUi();
        }

        public void onNetworkError(Exception exc) {
            Toast.makeText(VectorMemberDetailsActivity.this, exc.getLocalizedMessage(), 1).show();
            VectorMemberDetailsActivity.this.updateUi();
        }

        public void onUnexpectedError(Exception exc) {
            Toast.makeText(VectorMemberDetailsActivity.this, exc.getLocalizedMessage(), 1).show();
            VectorMemberDetailsActivity.this.updateUi();
        }
    };
    /* access modifiers changed from: private */
    public RoomMember mRoomMember;
    /* access modifiers changed from: private */
    public MXSession mSession;
    private User mUser;

    public int getLayoutRes() {
        return R.layout.activity_tchap_member_details;
    }

    /* access modifiers changed from: private */
    public void startCall(final boolean z) {
        if (!this.mSession.isAlive() || this.mCallableRoom == null) {
            Log.e(LOG_TAG, "startCall : the session is not anymore valid, or no callable room found");
        } else {
            this.mSession.mCallsManager.createCallInRoom(this.mCallableRoom.getRoomId(), z, new ApiCallback<IMXCall>() {
                public void onSuccess(final IMXCall iMXCall) {
                    VectorMemberDetailsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            final Intent intent = new Intent(VectorMemberDetailsActivity.this, VectorCallViewActivity.class);
                            intent.putExtra(VectorCallViewActivity.EXTRA_MATRIX_ID, VectorMemberDetailsActivity.this.mSession.getCredentials().userId);
                            intent.putExtra(VectorCallViewActivity.EXTRA_CALL_ID, iMXCall.getCallId());
                            VectorMemberDetailsActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    VectorMemberDetailsActivity.this.startActivity(intent);
                                }
                            });
                        }
                    });
                }

                public void onNetworkError(Exception exc) {
                    Toast.makeText(VectorMemberDetailsActivity.this, exc.getLocalizedMessage(), 0).show();
                    String access$100 = VectorMemberDetailsActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## startCall() failed ");
                    sb.append(exc.getMessage());
                    Log.e(access$100, sb.toString(), exc);
                }

                public void onMatrixError(MatrixError matrixError) {
                    if (matrixError instanceof MXCryptoError) {
                        MXCryptoError mXCryptoError = (MXCryptoError) matrixError;
                        if (MXCryptoError.UNKNOWN_DEVICES_CODE.equals(mXCryptoError.errcode)) {
                            CommonActivityUtils.displayUnknownDevicesDialog(VectorMemberDetailsActivity.this.mSession, VectorMemberDetailsActivity.this, (MXUsersDevicesMap) mXCryptoError.mExceptionData, true, new IUnknownDevicesSendAnywayListener() {
                                public void onSendAnyway() {
                                    VectorMemberDetailsActivity.this.startCall(z);
                                }
                            });
                            return;
                        }
                    }
                    Toast.makeText(VectorMemberDetailsActivity.this, matrixError.getLocalizedMessage(), 0).show();
                    String access$100 = VectorMemberDetailsActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## startCall() failed ");
                    sb.append(matrixError.getMessage());
                    Log.e(access$100, sb.toString());
                }

                public void onUnexpectedError(Exception exc) {
                    Toast.makeText(VectorMemberDetailsActivity.this, exc.getLocalizedMessage(), 0).show();
                    String access$100 = VectorMemberDetailsActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## startCall() failed ");
                    sb.append(exc.getMessage());
                    Log.e(access$100, sb.toString(), exc);
                }
            });
        }
    }

    private void startCheckCallPermissions(boolean z) {
        int i = z ? PermissionsToolsKt.PERMISSION_REQUEST_CODE_VIDEO_CALL : PermissionsToolsKt.PERMISSION_REQUEST_CODE_AUDIO_CALL;
        if (PermissionsToolsKt.checkPermissions(i, (Activity) this, i)) {
            startCall(z);
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (strArr.length == 0) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onRequestPermissionsResult(): cancelled ");
            sb.append(i);
            Log.d(str, sb.toString());
        } else if (i == 571) {
            if (PermissionsToolsKt.onPermissionResultAudioIpCall(this, iArr)) {
                startCall(false);
            }
        } else if (i == 572 && PermissionsToolsKt.onPermissionResultVideoIpCall(this, iArr)) {
            startCall(true);
        }
    }

    public void selectRoom(final Room room) {
        runOnUiThread(new Runnable() {
            public void run() {
                HashMap hashMap = new HashMap();
                hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", VectorMemberDetailsActivity.this.mSession.getMyUserId());
                hashMap.put("EXTRA_ROOM_ID", room.getRoomId());
                String access$100 = VectorMemberDetailsActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## selectRoom(): open the room ");
                sb.append(room.getRoomId());
                Log.d(access$100, sb.toString());
                VectorMemberDetailsActivity vectorMemberDetailsActivity = VectorMemberDetailsActivity.this;
                CommonActivityUtils.goToRoomPage(vectorMemberDetailsActivity, vectorMemberDetailsActivity.mSession, hashMap);
            }
        });
    }

    public void performItemAction(int i) {
        if (!this.mSession.isAlive()) {
            Log.e(LOG_TAG, "performItemAction : the session is not anymore valid");
            return;
        }
        RoomMember roomMember = this.mRoomMember;
        String str = roomMember == null ? this.mMemberId : TextUtils.isEmpty(roomMember.displayname) ? this.mRoomMember.getUserId() : this.mRoomMember.displayname;
        boolean z = true;
        int i2 = 0;
        switch (i) {
            case 0:
                Log.d(LOG_TAG, "## performItemAction(): Invite");
                if (this.mRoom != null) {
                    enableProgressBarView(true);
                    this.mRoom.invite(this.mRoomMember.getUserId(), this.mRoomActionsListener);
                    break;
                }
                break;
            case 1:
                Log.d(LOG_TAG, "## performItemAction(): Leave the room");
                Room room = this.mRoom;
                if (room != null) {
                    PowerLevels powerLevels = room.getState().getPowerLevels();
                    if (powerLevels != null && ((float) powerLevels.getUserPowerLevel(this.mSession.getMyUserId())) >= 100.0f) {
                        i2 = 1;
                    }
                    if (i2 == 0) {
                        enableProgressBarView(true);
                        this.mRoom.leave(this.mRoomActionsListener);
                        break;
                    } else {
                        RoomUtils.showLeaveRoomDialog(this, this.mSession, this.mRoom, new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                VectorMemberDetailsActivity.this.enableProgressBarView(true);
                                VectorMemberDetailsActivity.this.mRoom.leave(VectorMemberDetailsActivity.this.mRoomActionsListener);
                            }
                        });
                        break;
                    }
                }
                break;
            case 2:
                if (this.mRoom != null) {
                    View inflate = getLayoutInflater().inflate(R.layout.dialog_base_edit_text, null);
                    final TextView textView = (TextView) inflate.findViewById(R.id.edit_text);
                    textView.setHint(R.string.reason_hint);
                    new Builder(this).setTitle((CharSequence) getResources().getQuantityString(R.plurals.room_participants_kick_prompt_msg, 1)).setView(inflate).setPositiveButton((int) R.string.room_participants_action_kick, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            VectorMemberDetailsActivity.this.enableProgressBarView(true);
                            VectorMemberDetailsActivity.this.mRoom.kick(VectorMemberDetailsActivity.this.mRoomMember.getUserId(), textView.getText().toString(), VectorMemberDetailsActivity.this.mRoomActionsListener);
                            Log.d(VectorMemberDetailsActivity.LOG_TAG, "## performItemAction(): Kick");
                        }
                    }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
                    break;
                }
                break;
            case 3:
                if (this.mRoom != null) {
                    View inflate2 = getLayoutInflater().inflate(R.layout.dialog_base_edit_text, null);
                    final TextView textView2 = (TextView) inflate2.findViewById(R.id.edit_text);
                    textView2.setHint(R.string.reason_hint);
                    new Builder(this).setTitle((int) R.string.room_participants_ban_prompt_msg).setView(inflate2).setPositiveButton((int) R.string.room_participants_action_ban, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            VectorMemberDetailsActivity.this.enableProgressBarView(true);
                            VectorMemberDetailsActivity.this.mRoom.ban(VectorMemberDetailsActivity.this.mRoomMember.getUserId(), textView2.getText().toString(), VectorMemberDetailsActivity.this.mRoomActionsListener);
                            Log.d(VectorMemberDetailsActivity.LOG_TAG, "## performItemAction(): Block (Ban)");
                        }
                    }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
                    break;
                }
                break;
            case 4:
                if (this.mRoom != null) {
                    enableProgressBarView(true);
                    this.mRoom.unban(this.mRoomMember.getUserId(), this.mRoomActionsListener);
                    Log.d(LOG_TAG, "## performItemAction(): Block (unban)");
                    break;
                }
                break;
            case 5:
                new Builder(this).setMessage((int) R.string.room_event_action_report_prompt_ignore_user).setCancelable(false).setPositiveButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        VectorMemberDetailsActivity.this.enableProgressBarView(true);
                        ArrayList arrayList = new ArrayList();
                        if (VectorMemberDetailsActivity.this.mRoomMember != null) {
                            arrayList.add(VectorMemberDetailsActivity.this.mRoomMember.getUserId());
                        } else if (VectorMemberDetailsActivity.this.mMemberId != null) {
                            arrayList.add(VectorMemberDetailsActivity.this.mMemberId);
                        }
                        if (arrayList.size() != 0) {
                            VectorMemberDetailsActivity.this.enableProgressBarView(true);
                            VectorMemberDetailsActivity.this.mSession.ignoreUsers(arrayList, new ApiCallback<Void>() {
                                public void onSuccess(Void voidR) {
                                }

                                public void onNetworkError(Exception exc) {
                                    VectorMemberDetailsActivity.this.mRoomActionsListener.onNetworkError(exc);
                                }

                                public void onMatrixError(MatrixError matrixError) {
                                    VectorMemberDetailsActivity.this.mRoomActionsListener.onMatrixError(matrixError);
                                }

                                public void onUnexpectedError(Exception exc) {
                                    VectorMemberDetailsActivity.this.mRoomActionsListener.onUnexpectedError(exc);
                                }
                            });
                            Log.d(VectorMemberDetailsActivity.LOG_TAG, "## performItemAction(): ignoreUsers");
                        }
                    }
                }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
                break;
            case 6:
                new Builder(this).setMessage((int) R.string.room_participants_action_unignore_prompt).setCancelable(false).setPositiveButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList arrayList = new ArrayList();
                        if (VectorMemberDetailsActivity.this.mRoomMember != null) {
                            arrayList.add(VectorMemberDetailsActivity.this.mRoomMember.getUserId());
                        } else if (VectorMemberDetailsActivity.this.mMemberId != null) {
                            arrayList.add(VectorMemberDetailsActivity.this.mMemberId);
                        }
                        if (arrayList.size() != 0) {
                            VectorMemberDetailsActivity.this.enableProgressBarView(true);
                            VectorMemberDetailsActivity.this.mSession.unIgnoreUsers(arrayList, new ApiCallback<Void>() {
                                public void onSuccess(Void voidR) {
                                }

                                public void onNetworkError(Exception exc) {
                                    VectorMemberDetailsActivity.this.mRoomActionsListener.onNetworkError(exc);
                                }

                                public void onMatrixError(MatrixError matrixError) {
                                    VectorMemberDetailsActivity.this.mRoomActionsListener.onMatrixError(matrixError);
                                }

                                public void onUnexpectedError(Exception exc) {
                                    VectorMemberDetailsActivity.this.mRoomActionsListener.onUnexpectedError(exc);
                                }
                            });
                            Log.d(VectorMemberDetailsActivity.LOG_TAG, "## performItemAction(): unIgnoreUsers");
                        }
                    }
                }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
                break;
            case 7:
                Room room2 = this.mRoom;
                if (room2 != null) {
                    PowerLevels powerLevels2 = room2.getState().getPowerLevels();
                    if (powerLevels2 != null) {
                        i2 = powerLevels2.users_default;
                    }
                    updateUserPowerLevels(this.mMemberId, i2, this.mRoomActionsListener);
                    Log.d(LOG_TAG, "## performItemAction(): default power level");
                    break;
                }
                break;
            case 8:
                if (this.mRoom != null) {
                    updateUserPowerLevels(this.mMemberId, 50, this.mRoomActionsListener);
                    Log.d(LOG_TAG, "## performItemAction(): Make moderator");
                    break;
                }
                break;
            case 9:
                if (this.mRoom != null) {
                    updateUserPowerLevels(this.mMemberId, 100, this.mRoomActionsListener);
                    Log.d(LOG_TAG, "## performItemAction(): Make Admin");
                    break;
                }
                break;
            case 11:
                User user = this.mSession.getDataHandler().getUser(this.mMemberId);
                if (user == null) {
                    user = new User();
                    user.user_id = this.mMemberId;
                }
                DinsicUtils.startDirectChat((VectorAppCompatActivity) this, this.mSession, user);
                break;
            case 12:
            case 13:
                Log.d(LOG_TAG, "## performItemAction(): Start call");
                if (13 != i) {
                    z = false;
                }
                startCheckCallPermissions(z);
                break;
            case 14:
                Intent intent = new Intent();
                intent.putExtra(RESULT_MENTION_ID, str);
                setResult(-1, intent);
                finish();
                break;
            case 15:
                refreshDevicesListView();
                break;
            default:
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## performItemAction(): unknown action type = ");
                sb.append(i);
                Log.w(str2, sb.toString());
                break;
        }
    }

    /* access modifiers changed from: private */
    public void setScreenDevicesListVisibility(int i) {
        this.mDevicesListHeaderView.setVisibility(i);
        this.mDevicesListView.setVisibility(i);
        if (i == 0) {
            this.mExpandableListView.setVisibility(8);
        } else {
            this.mExpandableListView.setVisibility(0);
        }
    }

    private void refreshDevicesListView() {
        MXSession mXSession = this.mSession;
        if (mXSession != null && mXSession.getCrypto() != null) {
            enableProgressBarView(true);
            this.mSession.getCrypto().getDeviceList().downloadKeys(Collections.singletonList(this.mMemberId), true, new ApiCallback<MXUsersDevicesMap<MXDeviceInfo>>() {
                private void onError(String str) {
                    Toast.makeText(VectorMemberDetailsActivity.this, str, 1).show();
                    VectorMemberDetailsActivity.this.updateUi();
                }

                public void onSuccess(MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
                    final boolean access$1200 = VectorMemberDetailsActivity.this.populateDevicesListAdapter(mXUsersDevicesMap);
                    VectorMemberDetailsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            VectorMemberDetailsActivity.this.enableProgressBarView(false);
                            if (access$1200) {
                                VectorMemberDetailsActivity.this.setScreenDevicesListVisibility(0);
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
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (16908332 != menuItem.getItemId() || this.mDevicesListView.getVisibility() != 0) {
            return super.onOptionsItemSelected(menuItem);
        }
        setScreenDevicesListVisibility(8);
        return true;
    }

    /* access modifiers changed from: private */
    public boolean populateDevicesListAdapter(MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
        if (mXUsersDevicesMap != null) {
            VectorMemberDetailsDevicesAdapter vectorMemberDetailsDevicesAdapter = this.mDevicesListViewAdapter;
            if (vectorMemberDetailsDevicesAdapter != null) {
                vectorMemberDetailsDevicesAdapter.clear();
                if (mXUsersDevicesMap.getMap().containsKey(this.mMemberId)) {
                    this.mDevicesListViewAdapter.addAll(new ArrayList(new HashMap((Map) mXUsersDevicesMap.getMap().get(this.mMemberId)).values()));
                } else {
                    String str = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## populateDevicesListAdapter(): invalid response - entry for ");
                    sb.append(this.mMemberId);
                    sb.append(" is missing");
                    Log.w(str, sb.toString());
                }
            }
        }
        VectorMemberDetailsDevicesAdapter vectorMemberDetailsDevicesAdapter2 = this.mDevicesListViewAdapter;
        return (vectorMemberDetailsDevicesAdapter2 == null || vectorMemberDetailsDevicesAdapter2.getCount() == 0) ? false : true;
    }

    private void updateUserPowerLevels(final String str, final int i, final ApiCallback<Void> apiCallback) {
        PowerLevels powerLevels = this.mRoom.getState().getPowerLevels();
        if ((powerLevels != null ? powerLevels.getUserPowerLevel(this.mSession.getMyUserId()) : 0) == i) {
            new Builder(this).setMessage((int) R.string.room_participants_power_level_prompt).setPositiveButton((int) R.string.yes, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    VectorMemberDetailsActivity.this.mRoom.updateUserPowerLevels(str, i, apiCallback);
                }
            }).setNegativeButton((int) R.string.no, (OnClickListener) null).show();
        } else {
            this.mRoom.updateUserPowerLevels(str, i, apiCallback);
        }
    }

    private void searchCallableRoom(ApiCallback<Room> apiCallback) {
        if (!this.mSession.isAlive()) {
            Log.e(LOG_TAG, "searchCallableRoom : the session is not anymore valid");
            apiCallback.onSuccess(null);
            return;
        }
        searchCallableRoomRecursive(new ArrayList(this.mSession.getDataHandler().getStore().getRooms()), 0, apiCallback);
    }

    /* access modifiers changed from: private */
    public void searchCallableRoomRecursive(List<Room> list, int i, ApiCallback<Room> apiCallback) {
        if (i >= list.size()) {
            apiCallback.onSuccess(null);
            return;
        }
        Room room = (Room) list.get(i);
        if (room.getNumberOfMembers() != 2 || !room.canPerformCall()) {
            searchCallableRoomRecursive(list, i + 1, apiCallback);
            return;
        }
        final ApiCallback<Room> apiCallback2 = apiCallback;
        final Room room2 = room;
        final List<Room> list2 = list;
        final int i2 = i;
        AnonymousClass14 r1 = new SimpleApiCallback<List<RoomMember>>() {
            public void onSuccess(List<RoomMember> list) {
                for (RoomMember userId : list) {
                    if (userId.getUserId().equals(VectorMemberDetailsActivity.this.mMemberId)) {
                        apiCallback2.onSuccess(room2);
                        return;
                    }
                }
                VectorMemberDetailsActivity.this.searchCallableRoomRecursive(list2, i2 + 1, apiCallback2);
            }
        };
        room.getMembersAsync(r1);
    }

    private List<Integer> supportedActionsList() {
        int i;
        int i2;
        int i3;
        ArrayList arrayList = new ArrayList();
        if (!this.mSession.isAlive()) {
            Log.e(LOG_TAG, "supportedActionsList : the session is not anymore valid");
            return arrayList;
        }
        String myUserId = this.mSession.getMyUserId();
        PowerLevels powerLevels = null;
        Room room = this.mRoom;
        if (room != null) {
            powerLevels = room.getState().getPowerLevels();
        }
        if (powerLevels != null) {
            i3 = powerLevels.getUserPowerLevel(this.mMemberId);
            i2 = powerLevels.getUserPowerLevel(myUserId);
            i = 0;
            for (Integer num : powerLevels.users.values()) {
                if (num != null && ((float) num.intValue()) >= 100.0f) {
                    i++;
                }
            }
        } else {
            i3 = 50;
            i2 = 50;
            i = 0;
        }
        if (!TextUtils.equals(this.mMemberId, myUserId) && this.mRoom != null && (!DinsicUtils.isExternalTchapSession(this.mSession) || !DinsicUtils.isExternalTchapUser(this.mMemberId))) {
            arrayList.add(Integer.valueOf(16));
        }
        boolean equals = TextUtils.equals(this.mMemberId, myUserId);
        String str = Event.EVENT_TYPE_STATE_ROOM_POWER_LEVELS;
        if (equals) {
            if (this.mRoom != null) {
                arrayList.add(Integer.valueOf(1));
            }
            if (i > 1 && powerLevels != null && i2 >= powerLevels.minimumPowerLevelForSendingEventAsStateEvent(str)) {
                if (i2 >= 100) {
                    arrayList.add(Integer.valueOf(8));
                }
                if (i2 >= 50) {
                    arrayList.add(Integer.valueOf(7));
                }
            }
        } else if (this.mRoomMember != null) {
            if (this.mCallableRoom != null && this.mSession.isVoipCallSupported() && CallsManager.getSharedInstance().getActiveCall() == null) {
                arrayList.add(Integer.valueOf(12));
                arrayList.add(Integer.valueOf(13));
            }
            String str2 = this.mRoomMember.membership;
            if (powerLevels != null) {
                String str3 = "join";
                if (TextUtils.equals(str2, "invite") || TextUtils.equals(str2, str3)) {
                    if (i2 >= powerLevels.minimumPowerLevelForSendingEventAsStateEvent(str) && i2 > i3) {
                        if (i2 >= 100) {
                            arrayList.add(Integer.valueOf(9));
                        }
                        if (i2 >= 50 && i3 < 50) {
                            arrayList.add(Integer.valueOf(8));
                        }
                        if (i3 >= 8) {
                            arrayList.add(Integer.valueOf(7));
                        }
                    }
                    if (i2 >= powerLevels.kick && i2 > i3) {
                        arrayList.add(Integer.valueOf(2));
                    }
                    if (i2 >= powerLevels.ban && i2 > i3) {
                        arrayList.add(Integer.valueOf(3));
                    }
                    if (TextUtils.equals(str2, str3)) {
                        int roomMaxPowerLevel = MatrixSdkExtensionsKt.getRoomMaxPowerLevel(this.mRoom);
                        if (i2 == roomMaxPowerLevel && i3 != roomMaxPowerLevel) {
                            arrayList.add(Integer.valueOf(9));
                        }
                        if (!this.mSession.isUserIgnored(this.mRoomMember.getUserId())) {
                            arrayList.add(Integer.valueOf(5));
                        } else {
                            arrayList.add(Integer.valueOf(6));
                        }
                    }
                } else if (TextUtils.equals(str2, "leave")) {
                    if (i2 >= powerLevels.invite) {
                        arrayList.add(Integer.valueOf(0));
                    }
                    if (i2 >= powerLevels.ban && i2 > i3) {
                        arrayList.add(Integer.valueOf(3));
                    }
                } else if (TextUtils.equals(str2, "ban") && i2 >= powerLevels.ban && i2 > i3) {
                    arrayList.add(Integer.valueOf(4));
                }
            }
            if (!TextUtils.equals(this.mRoomMember.membership, this.mSession.getMyUserId())) {
                arrayList.add(Integer.valueOf(14));
            }
        } else if (this.mUser != null) {
            if (!this.mSession.isUserIgnored(this.mMemberId)) {
                arrayList.add(Integer.valueOf(5));
            } else {
                arrayList.add(Integer.valueOf(6));
            }
        }
        return arrayList;
    }

    private void updateAdapterListViewTchapItems() {
        if (this.mListViewAdapter == null) {
            Log.w(LOG_TAG, "## updateListViewItemsContent(): list view adapter not initialized");
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        new ArrayList();
        List supportedActionsList = supportedActionsList();
        if (supportedActionsList.indexOf(Integer.valueOf(16)) >= 0) {
            arrayList.add(new AdapterMemberActionItems(R.drawable.ic_comment_black, getResources().getString(R.string.start_new_chat_from_contact), 11));
        }
        if (supportedActionsList.indexOf(Integer.valueOf(5)) >= 0) {
            arrayList.add(new AdapterMemberActionItems(R.drawable.ic_person_outline_black, getResources().getString(R.string.room_participants_action_ignore), 5));
        }
        if (supportedActionsList.indexOf(Integer.valueOf(6)) >= 0) {
            arrayList.add(new AdapterMemberActionItems(R.drawable.ic_person_black, getResources().getString(R.string.room_participants_action_unignore), 6));
        }
        if (supportedActionsList.indexOf(Integer.valueOf(0)) >= 0) {
            arrayList.add(new AdapterMemberActionItems(R.drawable.ic_person_add_black, getResources().getString(R.string.room_participants_action_invite), 0));
        }
        if (supportedActionsList.indexOf(Integer.valueOf(1)) >= 0) {
            arrayList.add(new AdapterMemberActionItems(R.drawable.vector_leave_room_black, getResources().getString(R.string.room_participants_action_leave), 1));
        }
        if (supportedActionsList.indexOf(Integer.valueOf(9)) >= 0) {
            arrayList.add(new AdapterMemberActionItems(R.drawable.ic_verified_user_black, getResources().getString(R.string.room_participants_action_set_admin), 9));
        }
        if (supportedActionsList.indexOf(Integer.valueOf(8)) >= 0) {
            arrayList.add(new AdapterMemberActionItems(R.drawable.ic_verified_user_black, getResources().getString(R.string.room_participants_action_set_moderator), 8));
        }
        if (supportedActionsList.indexOf(Integer.valueOf(7)) >= 0) {
            arrayList.add(new AdapterMemberActionItems(R.drawable.ic_verified_user_black, getResources().getString(R.string.room_participants_action_set_default_power_level), 7));
        }
        if (supportedActionsList.indexOf(Integer.valueOf(2)) >= 0) {
            arrayList.add(new AdapterMemberActionItems(R.drawable.ic_remove_circle_outline_red, getResources().getString(R.string.room_participants_action_remove), 2));
        }
        if (supportedActionsList.indexOf(Integer.valueOf(3)) >= 0) {
            arrayList.add(new AdapterMemberActionItems(R.drawable.ic_block_black, getResources().getString(R.string.room_participants_action_ban), 3));
        }
        if (supportedActionsList.indexOf(Integer.valueOf(4)) >= 0) {
            arrayList.add(new AdapterMemberActionItems(R.drawable.ic_block_black, getResources().getString(R.string.room_participants_action_unban), 4));
        }
        this.mListViewAdapter.setUncategorizedActionsList(arrayList);
        this.mListViewAdapter.setAdminActionsList(arrayList2);
        this.mListViewAdapter.setCallActionsList(arrayList3);
        this.mListViewAdapter.notifyDataSetChanged();
        this.mExpandableListView.post(new Runnable() {
            public void run() {
                int groupCount = VectorMemberDetailsActivity.this.mListViewAdapter.getGroupCount();
                for (int i = 0; i < groupCount; i++) {
                    VectorMemberDetailsActivity.this.mExpandableListView.expandGroup(i);
                }
            }
        });
    }

    public ActivityOtherThemes getOtherThemes() {
        return NoActionBar.INSTANCE;
    }

    public void initUiAndData() {
        super.initUiAndData();
        if (CommonActivityUtils.shouldRestartApp(this)) {
            Log.e(LOG_TAG, "Restart the application");
            CommonActivityUtils.restartApp(this);
        } else if (CommonActivityUtils.isGoingToSplash(this)) {
            Log.d(LOG_TAG, "onCreate : Going to splash screen");
        } else {
            if (!initContextStateValues()) {
                Log.e(LOG_TAG, "## onCreate(): Parameters init failure");
                finish();
            } else {
                setSupportActionBar((Toolbar) findViewById(R.id.room_toolbar));
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
                setWaitingView(findViewById(R.id.member_details_list_view_progress_bar));
                this.mDevicesListViewAdapter = new VectorMemberDetailsDevicesAdapter(this, R.layout.adapter_item_member_details_devices, this.mSession);
                this.mDevicesListViewAdapter.setDevicesAdapterListener(this);
                this.mDevicesListView.setAdapter(this.mDevicesListViewAdapter);
                TextView textView = (TextView) this.mDevicesListHeaderView.findViewById(R.id.heading);
                if (textView != null) {
                    textView.setText(R.string.room_participants_header_devices);
                }
                this.mListViewAdapter = new VectorMemberDetailsAdapter(this, this.mSession, R.layout.vector_adapter_member_details_items, R.layout.adapter_item_vector_recent_header);
                this.mListViewAdapter.setActionListener(this);
                this.mExpandableListView.setGroupIndicator(null);
                this.mExpandableListView.setAdapter(this.mListViewAdapter);
                this.mExpandableListView.setOnGroupClickListener(new OnGroupClickListener() {
                    public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long j) {
                        return true;
                    }
                });
                this.mMemberAvatarImageView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        VectorMemberDetailsActivity.this.displayFullScreenAvatar();
                    }
                });
                this.mFullMemberAvatarImageView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        VectorMemberDetailsActivity.this.hideFullScreenAvatar();
                    }
                });
                this.mFullMemberAvatarLayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        VectorMemberDetailsActivity.this.hideFullScreenAvatar();
                    }
                });
                updateUi();
                checkRoomMemberStatus(new SimpleApiCallback<Boolean>(this) {
                    public void onSuccess(Boolean bool) {
                        VectorMemberDetailsActivity.this.updateUi();
                        if (!VectorMemberDetailsActivity.this.isFirstCreation() && VectorMemberDetailsActivity.this.getSavedInstanceState().getBoolean(VectorMemberDetailsActivity.AVATAR_FULLSCREEN_MODE, false)) {
                            VectorMemberDetailsActivity.this.displayFullScreenAvatar();
                        }
                    }
                });
                searchCallableRoom(new SimpleApiCallback<Room>() {
                    public void onSuccess(Room room) {
                        VectorMemberDetailsActivity.this.mCallableRoom = room;
                        VectorMemberDetailsActivity.this.updateUi();
                    }
                });
            }
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (4 == i) {
            if (this.mFullMemberAvatarLayout.getVisibility() == 0) {
                hideFullScreenAvatar();
                return true;
            } else if (this.mDevicesListView.getVisibility() == 0) {
                setScreenDevicesListVisibility(8);
                return true;
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean(AVATAR_FULLSCREEN_MODE, this.mFullMemberAvatarLayout.getVisibility() == 0);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        if (bundle.getBoolean(AVATAR_FULLSCREEN_MODE, false)) {
            displayFullScreenAvatar();
        }
    }

    /* access modifiers changed from: private */
    public void hideFullScreenAvatar() {
        this.mFullMemberAvatarLayout.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void displayFullScreenAvatar() {
        String str;
        String str2 = this.mMemberId;
        RoomMember roomMember = this.mRoomMember;
        if (roomMember != null) {
            str = roomMember.getAvatarUrl();
            if (TextUtils.isEmpty(str)) {
                str2 = this.mRoomMember.getUserId();
            }
        } else {
            str = null;
        }
        if (TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            User user = this.mUser;
            if (user != null) {
                str = user.getAvatarUrl();
            }
        }
        String str3 = str;
        if (!TextUtils.isEmpty(str3)) {
            this.mFullMemberAvatarLayout.setVisibility(0);
            this.mSession.getMediaCache().loadBitmap(this.mSession.getHomeServerConfig(), this.mFullMemberAvatarImageView, str3, 0, 0, (String) null, (EncryptedFileInfo) null);
        }
    }

    private boolean initContextStateValues() {
        IMXStore iMXStore;
        Intent intent = getIntent();
        boolean z = false;
        if (intent != null) {
            String stringExtra = intent.getStringExtra(EXTRA_MEMBER_ID);
            this.mMemberId = stringExtra;
            if (stringExtra == null) {
                Log.e(LOG_TAG, "member ID missing in extra");
                return false;
            }
            MXSession session = getSession(intent);
            this.mSession = session;
            if (session == null) {
                Log.e(LOG_TAG, "Invalid session");
                return false;
            }
            int intExtra = intent.getIntExtra(EXTRA_STORE_ID, -1);
            String str = "EXTRA_ROOM_ID";
            if (intExtra >= 0) {
                iMXStore = Matrix.getInstance(this).getTmpStore(intExtra);
            } else {
                iMXStore = this.mSession.getDataHandler().getStore();
                if (refreshUser()) {
                    intent.removeExtra(str);
                }
            }
            String stringExtra2 = intent.getStringExtra(str);
            if (stringExtra2 != null) {
                Room room = iMXStore.getRoom(stringExtra2);
                this.mRoom = room;
                if (room == null) {
                    Log.e(LOG_TAG, "The room is not found");
                }
            }
            Log.d(LOG_TAG, "Parameters init succeed");
            z = true;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public void checkRoomMemberStatus(final ApiCallback<Boolean> apiCallback) {
        this.mRoomMember = null;
        Room room = this.mRoom;
        if (room != null) {
            room.getMembersAsync(new SimpleApiCallback<List<RoomMember>>(apiCallback) {
                public void onSuccess(List<RoomMember> list) {
                    Iterator it = list.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        RoomMember roomMember = (RoomMember) it.next();
                        if (roomMember.getUserId().equals(VectorMemberDetailsActivity.this.mMemberId)) {
                            VectorMemberDetailsActivity.this.mRoomMember = roomMember;
                            break;
                        }
                    }
                    apiCallback.onSuccess(Boolean.valueOf(VectorMemberDetailsActivity.this.mRoomMember != null));
                }
            });
        } else {
            apiCallback.onSuccess(Boolean.valueOf(false));
        }
    }

    private boolean refreshUser() {
        this.mUser = this.mSession.getDataHandler().getStore().getUser(this.mMemberId);
        if (this.mUser != null) {
            return false;
        }
        this.mUser = new User();
        User user = this.mUser;
        user.user_id = this.mMemberId;
        user.displayname = getIntent().getStringExtra(EXTRA_MEMBER_DISPLAY_NAME);
        if (TextUtils.isEmpty(this.mUser.displayname)) {
            this.mUser.displayname = DinsicUtils.computeDisplayNameFromUserId(this.mMemberId);
        }
        this.mUser.avatar_url = getIntent().getStringExtra(EXTRA_MEMBER_AVATAR_URL);
        return true;
    }

    /* access modifiers changed from: private */
    public void updateUi() {
        if (!this.mSession.isAlive()) {
            Log.e(LOG_TAG, "updateUi : the session is not anymore valid");
            return;
        }
        RoomMember roomMember = this.mRoomMember;
        if (roomMember == null || TextUtils.isEmpty(roomMember.displayname)) {
            refreshUser();
            setTitle(DinsicUtils.getNameFromDisplayName(this.mUser.displayname));
            setTopic(DinsicUtils.getDomainFromDisplayName(this.mUser.displayname));
        } else {
            setTitle(DinsicUtils.getNameFromDisplayName(this.mRoomMember.displayname));
            setTopic(DinsicUtils.getDomainFromDisplayName(this.mRoomMember.displayname));
        }
        enableProgressBarView(false);
        updateMemberAvatarUi();
        updatePresenceInfoUi();
        updateAdapterListViewTchapItems();
        VectorMemberDetailsAdapter vectorMemberDetailsAdapter = this.mListViewAdapter;
        if (vectorMemberDetailsAdapter != null) {
            vectorMemberDetailsAdapter.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    public void updatePresenceInfoUi() {
        TextView textView = this.mPresenceTextView;
        if (textView != null) {
            textView.setText(VectorUtils.getUserOnlineStatus(this, this.mSession, this.mMemberId, new SimpleApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    TextView textView = VectorMemberDetailsActivity.this.mPresenceTextView;
                    VectorMemberDetailsActivity vectorMemberDetailsActivity = VectorMemberDetailsActivity.this;
                    textView.setText(VectorUtils.getUserOnlineStatus(vectorMemberDetailsActivity, vectorMemberDetailsActivity.mSession, VectorMemberDetailsActivity.this.mMemberId, null));
                }
            }));
        }
    }

    /* access modifiers changed from: private */
    public void updateMemberAvatarUi() {
        ImageView imageView = this.mMemberAvatarImageView;
        if (imageView != null) {
            RoomMember roomMember = this.mRoomMember;
            if (roomMember != null) {
                String str = roomMember.displayname;
                String avatarUrl = this.mRoomMember.getAvatarUrl();
                if ((TextUtils.isEmpty(avatarUrl) || TextUtils.isEmpty(str)) && this.mUser != null) {
                    if (TextUtils.isEmpty(avatarUrl)) {
                        avatarUrl = this.mUser.avatar_url;
                    }
                    if (TextUtils.isEmpty(str)) {
                        str = this.mUser.displayname;
                    }
                }
                String str2 = avatarUrl;
                VectorUtils.loadUserAvatar(this, this.mSession, this.mMemberAvatarImageView, str2, this.mRoomMember.getUserId(), str);
                return;
            }
            User user = this.mUser;
            if (user != null) {
                VectorUtils.loadUserAvatar(this, this.mSession, imageView, user);
                return;
            }
            MXSession mXSession = this.mSession;
            String str3 = this.mMemberId;
            VectorUtils.loadUserAvatar(this, mXSession, imageView, null, str3, str3);
        }
    }

    /* access modifiers changed from: private */
    public void enableProgressBarView(boolean z) {
        if (z) {
            showWaitingView();
        } else {
            hideWaitingView();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (this.mSession != null) {
            Room room = this.mRoom;
            if (room != null) {
                room.removeEventListener(this.mLiveEventsListener);
            }
            this.mSession.getDataHandler().removeListener(this.mPresenceEventsListener);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mSession != null) {
            Room room = this.mRoom;
            if (room != null) {
                room.addEventListener(this.mLiveEventsListener);
            }
            this.mSession.getDataHandler().addListener(this.mPresenceEventsListener);
            updateAdapterListViewTchapItems();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        Room room = this.mRoom;
        if (room != null) {
            room.removeEventListener(this.mLiveEventsListener);
        }
        MXSession mXSession = this.mSession;
        if (mXSession != null) {
            mXSession.getDataHandler().removeListener(this.mPresenceEventsListener);
        }
    }

    public void OnVerifyDeviceClick(MXDeviceInfo mXDeviceInfo) {
        if (mXDeviceInfo.mVerified != 1) {
            CommonActivityUtils.displayDeviceVerificationDialog(mXDeviceInfo, this.mMemberId, this.mSession, this, this.mDevicesVerificationCallback);
        } else {
            this.mSession.getCrypto().setDeviceVerification(0, mXDeviceInfo.deviceId, this.mMemberId, this.mDevicesVerificationCallback);
        }
    }

    public void OnBlockDeviceClick(MXDeviceInfo mXDeviceInfo) {
        if (mXDeviceInfo.mVerified == 2) {
            this.mSession.getCrypto().setDeviceVerification(0, mXDeviceInfo.deviceId, mXDeviceInfo.userId, this.mDevicesVerificationCallback);
        } else {
            this.mSession.getCrypto().setDeviceVerification(2, mXDeviceInfo.deviceId, mXDeviceInfo.userId, this.mDevicesVerificationCallback);
        }
        this.mDevicesListViewAdapter.notifyDataSetChanged();
    }
}
