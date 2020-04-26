package im.vector.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.gson.JsonParser;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.activity.TchapDirectRoomDetailsActivity;
import fr.gouv.tchap.sdk.session.room.model.RoomAccessRulesKt;
import fr.gouv.tchap.util.DinsicUtils;
import fr.gouv.tchap.util.HexagonMaskView;
import fr.gouv.tchap.util.LiveSecurityChecks;
import im.vector.Matrix;
import im.vector.VectorApp;
import im.vector.ViewedRoomTracker;
import im.vector.activity.util.RequestCodesKt;
import im.vector.dialogs.DialogCallAdapter;
import im.vector.dialogs.DialogListItem;
import im.vector.dialogs.DialogListItem.SendFile;
import im.vector.dialogs.DialogListItem.SendSticker;
import im.vector.dialogs.DialogListItem.SendVoice;
import im.vector.dialogs.DialogListItem.TakePhoto;
import im.vector.dialogs.DialogListItem.TakeVideo;
import im.vector.dialogs.DialogSendItemAdapter;
import im.vector.features.hhs.LimitResourceState;
import im.vector.features.hhs.ResourceLimitEventListener;
import im.vector.features.hhs.ResourceLimitEventListener.Callback;
import im.vector.fragments.VectorMessageListFragment;
import im.vector.fragments.VectorMessageListFragment.VectorMessageListFragmentListener;
import im.vector.fragments.VectorReadReceiptsDialogFragment.VectorReadReceiptsDialogFragmentListener;
import im.vector.fragments.VectorUnknownDevicesFragment.IUnknownDevicesSendAnywayListener;
import im.vector.listeners.IMessagesAdapterActionsListener;
import im.vector.notifications.NotificationUtils;
import im.vector.services.EventStreamService;
import im.vector.ui.themes.ActivityOtherThemes;
import im.vector.ui.themes.ActivityOtherThemes.NoActionBar;
import im.vector.util.CallsManager;
import im.vector.util.ExternalApplicationsUtilKt;
import im.vector.util.PermissionsToolsKt;
import im.vector.util.PreferencesManager;
import im.vector.util.ReadMarkerManager;
import im.vector.util.SlashCommandsParser;
import im.vector.util.SlashCommandsParser.SlashCommand;
import im.vector.util.VectorMarkdownParser.IVectorMarkdownParserListener;
import im.vector.util.VectorRoomMediasSender;
import im.vector.util.VectorUtils;
import im.vector.view.ActiveWidgetsBanner;
import im.vector.view.ActiveWidgetsBanner.onUpdateListener;
import im.vector.view.NotificationAreaView;
import im.vector.view.NotificationAreaView.Delegate;
import im.vector.view.NotificationAreaView.State;
import im.vector.view.NotificationAreaView.State.ConnectionError;
import im.vector.view.NotificationAreaView.State.Default;
import im.vector.view.NotificationAreaView.State.Hidden;
import im.vector.view.NotificationAreaView.State.ResourceLimitExceededError;
import im.vector.view.NotificationAreaView.State.ScrollToBottom;
import im.vector.view.NotificationAreaView.State.Tombstone;
import im.vector.view.NotificationAreaView.State.Typing;
import im.vector.view.NotificationAreaView.State.UnreadPreview;
import im.vector.view.NotificationAreaView.State.UnsentEvents;
import im.vector.view.VectorAutoCompleteTextView;
import im.vector.view.VectorOngoingConferenceCallView;
import im.vector.view.VectorOngoingConferenceCallView.ICallClickListener;
import im.vector.view.VectorPendingCallView;
import im.vector.widgets.Widget;
import im.vector.widgets.WidgetsManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.call.IMXCall;
import org.matrix.androidsdk.call.IMXCallListener;
import org.matrix.androidsdk.call.MXCallListener;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.ResourceUtils;
import org.matrix.androidsdk.core.ResourceUtils.Resource;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.listeners.IMXNetworkEventListener;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.MXCryptoError;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomEmailInvitation;
import org.matrix.androidsdk.data.RoomMediaMessage;
import org.matrix.androidsdk.data.RoomPreviewData;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.db.MXLatestChatMessageCache;
import org.matrix.androidsdk.fragments.MatrixMessageListFragment;
import org.matrix.androidsdk.fragments.MatrixMessageListFragment.IEventSendingListener;
import org.matrix.androidsdk.fragments.MatrixMessageListFragment.IOnScrollListener;
import org.matrix.androidsdk.fragments.MatrixMessageListFragment.IRoomPreviewDataListener;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.listeners.MXMediaUploadListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.PowerLevels;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;
import org.matrix.androidsdk.rest.model.message.Message;
import org.piwik.sdk.BuildConfig;

public class VectorRoomActivity extends MXCActionBarActivity implements IRoomPreviewDataListener, IEventSendingListener, IOnScrollListener, VectorMessageListFragmentListener, VectorReadReceiptsDialogFragmentListener {
    private static final String CAMERA_VALUE_TITLE = "attachment";
    public static final int CONFIRM_MEDIA_REQUEST_CODE = 7;
    private static final String E2E_WARNINGS_PREFERENCES = "E2E_WARNINGS_PREFERENCES";
    public static final String EXTRA_DEFAULT_NAME = "EXTRA_DEFAULT_NAME";
    public static final String EXTRA_DEFAULT_TOPIC = "EXTRA_DEFAULT_TOPIC";
    public static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";
    public static final String EXTRA_EXPAND_ROOM_HEADER = "EXTRA_EXPAND_ROOM_HEADER";
    public static final String EXTRA_IS_UNREAD_PREVIEW_MODE = "EXTRA_IS_UNREAD_PREVIEW_MODE";
    public static final String EXTRA_MATRIX_ID = "MXCActionBarActivity.EXTRA_MATRIX_ID";
    public static final String EXTRA_ROOM_ID = "EXTRA_ROOM_ID";
    public static final String EXTRA_ROOM_INTENT = "EXTRA_ROOM_INTENT";
    public static final String EXTRA_ROOM_PREVIEW_ID = "EXTRA_ROOM_PREVIEW_ID";
    public static final String EXTRA_ROOM_PREVIEW_ROOM_ALIAS = "EXTRA_ROOM_PREVIEW_ROOM_ALIAS";
    public static final String EXTRA_START_CALL_ID = "EXTRA_START_CALL_ID";
    public static final String EXTRA_TCHAP_USER = "EXTRA_TCHAP_USER";
    public static final String EXTRA_TEXT_MESSAGE = "EXTRA_TEXT_MESSAGE";
    private static final String FIRST_VISIBLE_ROW = "FIRST_VISIBLE_ROW";
    public static final int GET_MENTION_REQUEST_CODE = 2;
    private static final boolean HIDE_ACTION_BAR_HEADER = false;
    private static final int INVITE_USER_REQUEST_CODE = 4;
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorRoomActivity.class.getSimpleName();
    private static final int RECORD_AUDIO_REQUEST_CODE = 6;
    private static final int REQUEST_FILES_REQUEST_CODE = 0;
    private static final int REQUEST_ROOM_AVATAR_CODE = 3;
    private static final boolean SHOW_ACTION_BAR_HEADER = true;
    private static final String TAG_FRAGMENT_MATRIX_MESSAGE_LIST = "TAG_FRAGMENT_MATRIX_MESSAGE_LIST";
    private static final int TAKE_IMAGE_REQUEST_CODE = 1;
    private static final int TYPING_TIMEOUT_MS = 10000;
    public static final int UNREAD_PREVIEW_REQUEST_CODE = 5;
    public static RoomPreviewData sRoomPreviewData = null;
    @BindView(2131296387)
    View closeReply;
    @BindView(2131296965)
    TextView invitationTextView;
    @BindView(2131296920)
    TextView mActionBarCustomTitle;
    @BindView(2131296921)
    TextView mActionBarCustomTopic;
    private HexagonMaskView mActionBarHeaderHexagonRoomAvatar;
    private ImageView mActionBarHeaderRoomAvatar;
    @BindView(2131296919)
    TextView mActionBarRoomInfo;
    @BindView(2131296962)
    ActiveWidgetsBanner mActiveWidgetsBanner;
    @BindView(2131296691)
    View mBackProgressView;
    @BindView(2131296929)
    View mBottomLayout;
    @BindView(2131296335)
    View mBottomSeparator;
    private String mCallId = null;
    private final IMXCallListener mCallListener = new MXCallListener() {
        public void onPreviewSizeChanged(int i, int i2) {
        }

        public void onCallError(String str) {
            VectorRoomActivity.this.refreshCallButtons(true);
        }

        public void onCallAnsweredElsewhere() {
            VectorRoomActivity.this.refreshCallButtons(true);
        }

        public void onCallEnd(int i) {
            VectorRoomActivity.this.refreshCallButtons(true);
        }
    };
    private int mCameraPermissionAction;
    @BindView(2131296931)
    View mCanNotPostTextView;
    private String mDefaultRoomName;
    private String mDefaultTopic;
    @BindView(2131296945)
    ImageView mE2eImageView;
    @BindView(2131296456)
    VectorAutoCompleteTextView mEditText;
    private String mEventId;
    @BindView(2131296692)
    View mForwardProgressView;
    private final MXEventListener mGlobalEventListener = new MXEventListener() {
        public void onSyncError(MatrixError matrixError) {
            VectorRoomActivity.this.mSyncInProgressView.setVisibility(8);
            VectorRoomActivity.this.checkSendEventStatus();
            VectorRoomActivity.this.refreshNotificationsArea();
        }

        public void onLeaveRoom(String str) {
            if (VectorRoomActivity.sRoomPreviewData != null && TextUtils.equals(VectorRoomActivity.sRoomPreviewData.getRoomId(), str)) {
                Log.d(VectorRoomActivity.LOG_TAG, "The room invitation has been declined from another client");
                VectorRoomActivity.this.onDeclined();
            }
        }

        public void onJoinRoom(String str) {
            if (VectorRoomActivity.sRoomPreviewData != null && TextUtils.equals(VectorRoomActivity.sRoomPreviewData.getRoomId(), str)) {
                VectorRoomActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(VectorRoomActivity.LOG_TAG, "The room invitation has been accepted from another client");
                        VectorRoomActivity.this.onJoined();
                    }
                });
            }
        }

        public void onLiveEventsChunkProcessed(String str, String str2) {
            VectorRoomActivity.this.mSyncInProgressView.setVisibility(8);
            VectorRoomActivity.this.checkSendEventStatus();
            VectorRoomActivity.this.refreshNotificationsArea();
        }
    };
    private boolean mHasUnsentEvents;
    /* access modifiers changed from: private */
    public boolean mIgnoreTextUpdate = false;
    private boolean mIsHeaderViewDisplayed = false;
    /* access modifiers changed from: private */
    public boolean mIsMarkDowning;
    private Boolean mIsScrolledToTheBottom;
    private boolean mIsUnreadPreviewMode;
    /* access modifiers changed from: private */
    public long mLastTypingDate = 0;
    /* access modifiers changed from: private */
    public MXLatestChatMessageCache mLatestChatMessageCache;
    private Event mLatestDisplayedEvent;
    private String mLatestTakePictureCameraUri = null;
    private String mLatestTypingMessage;
    @BindView(2131296714)
    View mMainProgressView;
    private String mMyUserId;
    private final IMXNetworkEventListener mNetworkEventListener = new IMXNetworkEventListener() {
        public void onNetworkConnectionUpdate(boolean z) {
            VectorRoomActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    VectorRoomActivity.this.refreshNotificationsArea();
                    VectorRoomActivity.this.refreshCallButtons(true);
                }
            });
        }
    };
    @BindView(2131296959)
    NotificationAreaView mNotificationsArea;
    /* access modifiers changed from: private */
    public ReadMarkerManager mReadMarkerManager;
    @BindView(2131296968)
    TextView mReplyMessage;
    @BindView(2131296896)
    TextView mReplySenderName;
    private ResourceLimitEventListener mResourceLimitEventListener;
    /* access modifiers changed from: private */
    public Room mRoom;
    private final MXEventListener mRoomEventListener = new MXEventListener() {
        public void onRoomFlush(String str) {
            VectorRoomActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    VectorRoomActivity.this.updateActionBarTitleAndTopic();
                    VectorRoomActivity.this.updateRoomHeaderAvatar();
                }
            });
        }

        public void onLeaveRoom(String str) {
            VectorRoomActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    VectorRoomActivity.this.finish();
                }
            });
        }

        public void onRoomKick(String str) {
            HashMap hashMap = new HashMap();
            hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", VectorRoomActivity.this.mSession.getMyUserId());
            hashMap.put("EXTRA_ROOM_ID", VectorRoomActivity.this.mRoom.getRoomId());
            Intent intent = new Intent(VectorRoomActivity.this, VectorHomeActivity.class);
            intent.setFlags(872415232);
            intent.putExtra(VectorHomeActivity.EXTRA_JUMP_TO_ROOM_PARAMS, hashMap);
            VectorRoomActivity.this.startActivity(intent);
        }

        public void onLiveEvent(final Event event, RoomState roomState) {
            VectorRoomActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    char c;
                    String type = event.getType();
                    String access$300 = VectorRoomActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Received event type: ");
                    sb.append(type);
                    Log.d(access$300, sb.toString());
                    int hashCode = type.hashCode();
                    boolean z = false;
                    String str = Event.EVENT_TYPE_TYPING;
                    switch (hashCode) {
                        case -2077370164:
                            if (type.equals(Event.EVENT_TYPE_STATE_ROOM_ALIASES)) {
                                c = 1;
                                break;
                            }
                        case -1995536872:
                            if (type.equals(str)) {
                                c = 5;
                                break;
                            }
                        case -612186677:
                            if (type.equals(Event.EVENT_TYPE_STATE_ROOM_AVATAR)) {
                                c = 6;
                                break;
                            }
                        case -338982155:
                            if (type.equals("m.room.encryption")) {
                                c = 7;
                                break;
                            }
                        case -283996404:
                            if (type.equals("m.room.member")) {
                                c = 2;
                                break;
                            }
                        case -2395523:
                            if (type.equals(Event.EVENT_TYPE_STATE_ROOM_TOPIC)) {
                                c = 3;
                                break;
                            }
                        case 138277757:
                            if (type.equals(Event.EVENT_TYPE_STATE_ROOM_NAME)) {
                                c = 0;
                                break;
                            }
                        case 915435739:
                            if (type.equals(Event.EVENT_TYPE_STATE_ROOM_POWER_LEVELS)) {
                                c = 4;
                                break;
                            }
                        case 1042755427:
                            if (type.equals(Event.EVENT_TYPE_STATE_ROOM_TOMBSTONE)) {
                                c = 8;
                                break;
                            }
                        default:
                            c = 65535;
                            break;
                    }
                    switch (c) {
                        case 0:
                        case 1:
                        case 2:
                            VectorRoomActivity.this.setTitle();
                            VectorRoomActivity.this.updateRoomHeaderAvatar();
                            break;
                        case 3:
                            VectorRoomActivity.this.setTopic();
                            break;
                        case 4:
                            VectorRoomActivity.this.checkSendEventStatus();
                            break;
                        case 5:
                            VectorRoomActivity.this.onRoomTyping();
                            break;
                        case 6:
                            VectorRoomActivity.this.updateRoomHeaderAvatar();
                            break;
                        case 7:
                            if (VectorRoomActivity.this.mRoom.isEncrypted() && VectorRoomActivity.this.mSession.isCryptoEnabled()) {
                                z = true;
                            }
                            VectorRoomActivity.this.mE2eImageView.setImageResource(z ? R.drawable.e2e_verified : R.drawable.e2e_unencrypted);
                            VectorRoomActivity.this.mVectorMessageListFragment.setIsRoomEncrypted(VectorRoomActivity.this.mRoom.isEncrypted());
                            break;
                        case 8:
                            VectorRoomActivity.this.checkSendEventStatus();
                            break;
                        default:
                            String access$3002 = VectorRoomActivity.LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Ignored event type: ");
                            sb2.append(type);
                            Log.d(access$3002, sb2.toString());
                            break;
                    }
                    if (!VectorApp.isAppInBackground() && !str.equals(type) && VectorRoomActivity.this.mRoom != null) {
                        VectorRoomActivity.this.refreshNotificationsArea();
                    }
                }
            });
        }

        public void onBingRulesUpdate() {
            VectorRoomActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    VectorRoomActivity.this.updateActionBarTitleAndTopic();
                    VectorRoomActivity.this.mVectorMessageListFragment.onBingRulesUpdate();
                }
            });
        }

        public void onEventSentStateUpdated(Event event) {
            VectorRoomActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    VectorRoomActivity.this.refreshNotificationsArea();
                }
            });
        }

        public void onEventSent(Event event, String str) {
            VectorRoomActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    VectorRoomActivity.this.refreshNotificationsArea();
                }
            });
        }

        public void onReceiptEvent(String str, List<String> list) {
            VectorRoomActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    VectorRoomActivity.this.refreshNotificationsArea();
                }
            });
        }

        public void onReadMarkerEvent(String str) {
            if (VectorRoomActivity.this.mReadMarkerManager != null) {
                VectorRoomActivity.this.mReadMarkerManager.onReadMarkerChanged(str);
            }
        }
    };
    @BindView(2131296964)
    View mRoomPreviewLayout;
    @BindView(2131296967)
    View mRoomReplyArea;
    private int mScrollToIndex = -1;
    @BindView(2131296922)
    ImageView mSendAttachedFileView;
    @BindView(2131296970)
    ImageView mSendMessageView;
    @BindView(2131296971)
    View mSendingMessagesLayout;
    /* access modifiers changed from: private */
    public MXSession mSession;
    @BindView(2131296972)
    View mStartCallLayout;
    @BindView(2131296946)
    View mStopCallLayout;
    @BindView(2131296973)
    View mSyncInProgressView;
    /* access modifiers changed from: private */
    public User mTchapUser;
    /* access modifiers changed from: private */
    public Timer mTypingTimer = null;
    /* access modifiers changed from: private */
    public TimerTask mTypingTimerTask;
    /* access modifiers changed from: private */
    public VectorMessageListFragment mVectorMessageListFragment;
    @BindView(2131296960)
    VectorOngoingConferenceCallView mVectorOngoingConferenceCallView;
    @BindView(2131296961)
    VectorPendingCallView mVectorPendingCallView;
    private VectorRoomMediasSender mVectorRoomMediasSender;
    private LiveSecurityChecks securityChecks = new LiveSecurityChecks(this);
    @BindView(2131296966)
    TextView subInvitationTextView;

    public int getLayoutRes() {
        return R.layout.activity_vector_room;
    }

    public ActivityOtherThemes getOtherThemes() {
        return NoActionBar.INSTANCE;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:84:0x02d5, code lost:
        if (android.text.TextUtils.isEmpty(r9.mEventId) != false) goto L_0x02d7;
     */
    public void initUiAndData() {
        setWaitingView(findViewById(R.id.main_progress_layout));
        if (CommonActivityUtils.shouldRestartApp(this)) {
            Log.e(LOG_TAG, "onCreate : Restart the application.");
            CommonActivityUtils.restartApp(this);
            return;
        }
        final Intent intent = getIntent();
        String str = "EXTRA_ROOM_ID";
        boolean hasExtra = intent.hasExtra(str);
        String str2 = EXTRA_TCHAP_USER;
        if (hasExtra || intent.hasExtra(str2)) {
            this.mSession = getSession(intent);
            MXSession mXSession = this.mSession;
            if (mXSession == null || !mXSession.isAlive()) {
                Log.e(LOG_TAG, "No MXSession.");
                finish();
                return;
            }
            this.mResourceLimitEventListener = new ResourceLimitEventListener(this.mSession.getDataHandler(), new Callback() {
                public void onResourceLimitStateChanged() {
                    VectorRoomActivity.this.refreshNotificationsArea();
                }
            });
            String stringExtra = intent.getStringExtra(str);
            this.mTchapUser = (User) intent.getSerializableExtra(str2);
            String str3 = null;
            if (!intent.hasExtra(EXTRA_ROOM_PREVIEW_ID)) {
                sRoomPreviewData = null;
                Matrix.getInstance(this).clearTmpStoresList();
            }
            if (CommonActivityUtils.isGoingToSplash(this, this.mSession.getMyUserId(), stringExtra)) {
                Log.d(LOG_TAG, "onCreate : Going to splash screen");
                return;
            }
            this.mBottomLayout.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    VectorRoomActivity.this.enableActionBarHeader(false);
                    return false;
                }
            });
            configureToolbar();
            this.mActionBarHeaderRoomAvatar = (ImageView) this.toolbar.findViewById(R.id.avatar_img);
            this.mActionBarHeaderHexagonRoomAvatar = (HexagonMaskView) this.toolbar.findViewById(R.id.avatar_h_img);
            this.mCallId = intent.getStringExtra(EXTRA_START_CALL_ID);
            this.mEventId = intent.getStringExtra(EXTRA_EVENT_ID);
            this.mDefaultRoomName = intent.getStringExtra(EXTRA_DEFAULT_NAME);
            this.mDefaultTopic = intent.getStringExtra(EXTRA_DEFAULT_TOPIC);
            this.mIsUnreadPreviewMode = intent.getBooleanExtra(EXTRA_IS_UNREAD_PREVIEW_MODE, false);
            if (intent.getAction() != null && intent.getAction().startsWith(NotificationUtils.TAP_TO_VIEW_ACTION)) {
                NotificationUtils.INSTANCE.cancelAllNotifications(this);
            }
            String str4 = "Displaying ";
            if (this.mIsUnreadPreviewMode) {
                String str5 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append(str4);
                sb.append(stringExtra);
                sb.append(" in unread preview mode");
                Log.d(str5, sb.toString());
            } else if (!TextUtils.isEmpty(this.mEventId) || sRoomPreviewData != null) {
                String str6 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str4);
                sb2.append(stringExtra);
                sb2.append(" in preview mode");
                Log.d(str6, sb2.toString());
            } else if (this.mTchapUser != null) {
                String str7 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Displaying new direct chat for");
                sb3.append(this.mTchapUser.user_id);
                Log.d(str7, sb3.toString());
            } else {
                String str8 = LOG_TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str4);
                sb4.append(stringExtra);
                Log.d(str8, sb4.toString());
            }
            this.mEditText.setOnEditorActionListener(new OnEditorActionListener() {
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (6 == (i & 255)) {
                        VectorRoomActivity.this.tchapSendTextMessage();
                        return true;
                    } else if (keyEvent == null || keyEvent.isShiftPressed() || keyEvent.getKeyCode() != 66 || VectorRoomActivity.this.getResources().getConfiguration().keyboard == 1 || keyEvent.getAction() != 0) {
                        return false;
                    } else {
                        VectorRoomActivity.this.tchapSendTextMessage();
                        return true;
                    }
                }
            });
            if (stringExtra != null) {
                this.mRoom = this.mSession.getDataHandler().getRoom(stringExtra, false);
            }
            this.mEditText.setAddColonOnFirstItem(true);
            this.mEditText.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void afterTextChanged(Editable editable) {
                    if (VectorRoomActivity.this.mRoom != null) {
                        MXLatestChatMessageCache access$1700 = VectorRoomActivity.this.mLatestChatMessageCache;
                        VectorRoomActivity vectorRoomActivity = VectorRoomActivity.this;
                        String latestText = access$1700.getLatestText(vectorRoomActivity, vectorRoomActivity.mRoom.getRoomId());
                        if (!VectorRoomActivity.this.mIgnoreTextUpdate && !latestText.equals(VectorRoomActivity.this.mEditText.getText().toString())) {
                            VectorRoomActivity vectorRoomActivity2 = VectorRoomActivity.this;
                            access$1700.updateLatestMessage(vectorRoomActivity2, vectorRoomActivity2.mRoom.getRoomId(), VectorRoomActivity.this.mEditText.getText().toString());
                            VectorRoomActivity vectorRoomActivity3 = VectorRoomActivity.this;
                            vectorRoomActivity3.handleTypingNotification(vectorRoomActivity3.mEditText.getText().length() != 0);
                        }
                        VectorRoomActivity.this.refreshCallButtons(true);
                    }
                    if (VectorRoomActivity.this.mRoom != null || VectorRoomActivity.this.mTchapUser != null) {
                        VectorRoomActivity.this.manageSendMoreButtons();
                    }
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    VectorRoomActivity.this.mEditText.updateAutoCompletionMode(false);
                }
            });
            this.mMyUserId = this.mSession.getCredentials().userId;
            CommonActivityUtils.resumeEventStream(this);
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            String str9 = TAG_FRAGMENT_MATRIX_MESSAGE_LIST;
            this.mVectorMessageListFragment = (VectorMessageListFragment) supportFragmentManager.findFragmentByTag(str9);
            if (this.mVectorMessageListFragment != null || stringExtra == null) {
                Log.d(LOG_TAG, "Reuse VectorMessageListFragment");
            } else {
                Log.d(LOG_TAG, "Create VectorMessageListFragment");
                if (sRoomPreviewData != null) {
                    str3 = MatrixMessageListFragment.PREVIEW_MODE_READ_ONLY;
                } else if (this.mIsUnreadPreviewMode) {
                    str3 = MatrixMessageListFragment.PREVIEW_MODE_UNREAD_MESSAGE;
                }
                this.mVectorMessageListFragment = VectorMessageListFragment.newInstance(this.mMyUserId, stringExtra, this.mEventId, str3, R.layout.fragment_matrix_message_list_fragment);
                supportFragmentManager.beginTransaction().add(R.id.anchor_fragment_messages, this.mVectorMessageListFragment, str9).commit();
            }
            VectorMessageListFragment vectorMessageListFragment = this.mVectorMessageListFragment;
            if (vectorMessageListFragment != null) {
                vectorMessageListFragment.setListener(this);
                this.mVectorRoomMediasSender = new VectorRoomMediasSender(this, this.mVectorMessageListFragment, Matrix.getInstance(this).getMediaCache());
            }
            manageRoomPreview();
            Room room = this.mRoom;
            if (room != null) {
                room.getMembersAsync(new SimpleApiCallback<List<RoomMember>>() {
                    public void onSuccess(List<RoomMember> list) {
                        VectorRoomActivity.this.refreshNotificationsArea();
                        VectorRoomActivity.this.checkIfUserHasBeenKicked();
                    }
                });
            }
            checkIfUserHasBeenKicked();
            this.mLatestChatMessageCache = Matrix.getInstance(this).getDefaultLatestChatMessageCache();
            String str10 = EXTRA_ROOM_INTENT;
            if (!intent.hasExtra(str10)) {
                String str11 = EXTRA_TEXT_MESSAGE;
                if (intent.hasExtra(str11)) {
                    if (isFirstCreation()) {
                        final String stringExtra2 = intent.getStringExtra(str11);
                        this.mEditText.postDelayed(new Runnable() {
                            public void run() {
                                intent.removeExtra(VectorRoomActivity.EXTRA_TEXT_MESSAGE);
                                VectorRoomActivity.this.mEditText.setText(stringExtra2);
                                VectorRoomActivity.this.sendTextMessage();
                            }
                        }, 500);
                    } else {
                        intent.removeExtra(str11);
                        Log.e(LOG_TAG, "## onCreate() : ignore EXTRA_TEXT_MESSAGE because savedInstanceState != null");
                    }
                }
            } else if (isFirstCreation()) {
                final Intent intent2 = (Intent) intent.getParcelableExtra(str10);
                if (intent2 != null) {
                    this.mEditText.postDelayed(new Runnable() {
                        public void run() {
                            intent.removeExtra(VectorRoomActivity.EXTRA_ROOM_INTENT);
                            VectorRoomActivity.this.sendMediasIntent(intent2);
                        }
                    }, 500);
                }
            } else {
                intent.removeExtra(str10);
                Log.e(LOG_TAG, "## onCreate() : ignore EXTRA_ROOM_INTENT because savedInstanceState != null");
            }
            this.mActiveWidgetsBanner.initRoomInfo(this.mSession, this.mRoom);
            this.mActiveWidgetsBanner.setOnUpdateListener(new onUpdateListener() {
                public void onActiveWidgetsListUpdate() {
                }

                public void onCloseWidgetClick(final Widget widget) {
                    new Builder(VectorRoomActivity.this).setMessage((int) R.string.widget_delete_message_confirmation).setPositiveButton((int) R.string.remove, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            VectorRoomActivity.this.showWaitingView();
                            WidgetsManager.getSharedInstance().closeWidget(VectorRoomActivity.this.mSession, VectorRoomActivity.this.mRoom, widget.getWidgetId(), new ApiCallback<Void>() {
                                public void onSuccess(Void voidR) {
                                    VectorRoomActivity.this.hideWaitingView();
                                }

                                private void onError(String str) {
                                    VectorRoomActivity.this.hideWaitingView();
                                    Toast.makeText(VectorRoomActivity.this, str, 0).show();
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
                    }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
                }

                /* access modifiers changed from: private */
                public void displayWidget(Widget widget) {
                    VectorRoomActivity.this.startActivity(WidgetActivity.Companion.getIntent(VectorRoomActivity.this, widget));
                }

                public void onClick(final List<Widget> list) {
                    if (list.size() == 1) {
                        displayWidget((Widget) list.get(0));
                    } else if (list.size() > 1) {
                        ArrayList arrayList = new ArrayList();
                        CharSequence[] charSequenceArr = new CharSequence[arrayList.size()];
                        for (Widget humanName : list) {
                            arrayList.add(humanName.getHumanName());
                        }
                        new Builder(VectorRoomActivity.this).setSingleChoiceItems((CharSequence[]) arrayList.toArray(charSequenceArr), 0, (OnClickListener) new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                AnonymousClass12.this.displayWidget((Widget) list.get(i));
                            }
                        }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
                    }
                }
            });
            this.mVectorOngoingConferenceCallView.initRoomInfo(this.mSession, this.mRoom);
            this.mVectorOngoingConferenceCallView.setCallClickListener(new ICallClickListener() {
                private void startCall(boolean z) {
                    if (PermissionsToolsKt.checkPermissions(z ? 5 : 4, (Activity) VectorRoomActivity.this, z ? PermissionsToolsKt.PERMISSION_REQUEST_CODE_VIDEO_CALL : PermissionsToolsKt.PERMISSION_REQUEST_CODE_AUDIO_CALL)) {
                        VectorRoomActivity.this.startIpCall(false, z);
                    }
                }

                private void onCallClick(Widget widget, boolean z) {
                    if (widget != null) {
                        VectorRoomActivity.this.launchJitsiActivity(widget, z);
                    } else {
                        startCall(z);
                    }
                }

                public void onVoiceCallClick(Widget widget) {
                    onCallClick(widget, false);
                }

                public void onVideoCallClick(Widget widget) {
                    onCallClick(widget, true);
                }

                public void onCloseWidgetClick(Widget widget) {
                    VectorRoomActivity.this.showWaitingView();
                    WidgetsManager.getSharedInstance().closeWidget(VectorRoomActivity.this.mSession, VectorRoomActivity.this.mRoom, widget.getWidgetId(), new ApiCallback<Void>() {
                        public void onSuccess(Void voidR) {
                            VectorRoomActivity.this.hideWaitingView();
                        }

                        private void onError(String str) {
                            VectorRoomActivity.this.hideWaitingView();
                            Toast.makeText(VectorRoomActivity.this, str, 0).show();
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

                public void onActiveWidgetUpdate() {
                    VectorRoomActivity.this.refreshCallButtons(false);
                }
            });
            VectorRoomMediasSender vectorRoomMediasSender = this.mVectorRoomMediasSender;
            if (vectorRoomMediasSender != null) {
                vectorRoomMediasSender.resumeResizeMediaAndSend();
            }
            String str12 = EXTRA_EXPAND_ROOM_HEADER;
            enableActionBarHeader(intent.getBooleanExtra(str12, false));
            intent.removeExtra(str12);
            if (!this.mIsUnreadPreviewMode) {
                Room room2 = this.mRoom;
                if (room2 != null) {
                    if (room2.getTimeline() != null) {
                        if (this.mRoom.getTimeline().isLiveTimeline()) {
                        }
                    }
                }
                this.mNotificationsArea.setDelegate(new Delegate() {
                    public IMessagesAdapterActionsListener providesMessagesActionListener() {
                        return VectorRoomActivity.this.mVectorMessageListFragment;
                    }

                    public void resendUnsentEvents() {
                        VectorRoomActivity.this.mVectorMessageListFragment.resendUnsentMessages();
                    }

                    public void deleteUnsentEvents() {
                        VectorRoomActivity.this.mVectorMessageListFragment.deleteUnsentEvents();
                    }

                    public void closeScreen() {
                        VectorRoomActivity.this.setResult(-1);
                        VectorRoomActivity.this.finish();
                    }

                    public void jumpToBottom() {
                        if (VectorRoomActivity.this.mReadMarkerManager != null) {
                            VectorRoomActivity.this.mReadMarkerManager.handleJumpToBottom();
                        } else {
                            VectorRoomActivity.this.mVectorMessageListFragment.scrollToBottom(0);
                        }
                    }
                });
                this.closeReply.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        VectorRoomActivity.this.mVectorMessageListFragment.onContentClick(0);
                    }
                });
                Log.d(LOG_TAG, "End of create");
                return;
            }
            if (this.mRoom == null) {
                Log.e(LOG_TAG, "## onCreate() : null room");
            } else if (this.mSession.getDataHandler().getStore().getSummary(this.mRoom.getRoomId()) == null) {
                Log.e(LOG_TAG, "## onCreate() : there is no summary for this room");
            } else {
                VectorMessageListFragment vectorMessageListFragment2 = this.mVectorMessageListFragment;
                MXSession mXSession2 = this.mSession;
                Room room3 = this.mRoom;
                boolean z = this.mIsUnreadPreviewMode;
                ReadMarkerManager readMarkerManager = new ReadMarkerManager(this, vectorMessageListFragment2, mXSession2, room3, z ? 1 : 0, findViewById(R.id.jump_to_first_unread));
                this.mReadMarkerManager = readMarkerManager;
            }
            this.mNotificationsArea.setDelegate(new Delegate() {
                public IMessagesAdapterActionsListener providesMessagesActionListener() {
                    return VectorRoomActivity.this.mVectorMessageListFragment;
                }

                public void resendUnsentEvents() {
                    VectorRoomActivity.this.mVectorMessageListFragment.resendUnsentMessages();
                }

                public void deleteUnsentEvents() {
                    VectorRoomActivity.this.mVectorMessageListFragment.deleteUnsentEvents();
                }

                public void closeScreen() {
                    VectorRoomActivity.this.setResult(-1);
                    VectorRoomActivity.this.finish();
                }

                public void jumpToBottom() {
                    if (VectorRoomActivity.this.mReadMarkerManager != null) {
                        VectorRoomActivity.this.mReadMarkerManager.handleJumpToBottom();
                    } else {
                        VectorRoomActivity.this.mVectorMessageListFragment.scrollToBottom(0);
                    }
                }
            });
            this.closeReply.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    VectorRoomActivity.this.mVectorMessageListFragment.onContentClick(0);
                }
            });
            Log.d(LOG_TAG, "End of create");
            return;
        }
        Log.e(LOG_TAG, "No room ID or User ID extra.");
        finish();
    }

    /* access modifiers changed from: private */
    public void checkIfUserHasBeenKicked() {
        Room room = this.mRoom;
        RoomMember member = room != null ? room.getMember(this.mMyUserId) : null;
        boolean z = member != null && member.kickedOrBanned();
        if (!TextUtils.isEmpty(this.mEventId) || sRoomPreviewData != null || z) {
            if (!this.mIsUnreadPreviewMode || z) {
                this.mNotificationsArea.setVisibility(8);
                this.mBottomSeparator.setVisibility(8);
                findViewById(R.id.room_notification_separator).setVisibility(8);
            }
            this.mBottomLayout.getLayoutParams().height = 0;
        }
        if (sRoomPreviewData == null && z) {
            manageBannedHeader(member);
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        VectorMessageListFragment vectorMessageListFragment = this.mVectorMessageListFragment;
        if (vectorMessageListFragment != null) {
            bundle.putInt(FIRST_VISIBLE_ROW, vectorMessageListFragment.mMessageListView.getFirstVisiblePosition());
        }
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        this.mScrollToIndex = bundle.getInt(FIRST_VISIBLE_ROW, -1);
    }

    public void onDestroy() {
        this.securityChecks.activityStopped();
        VectorMessageListFragment vectorMessageListFragment = this.mVectorMessageListFragment;
        if (vectorMessageListFragment != null) {
            vectorMessageListFragment.onDestroy();
        }
        VectorOngoingConferenceCallView vectorOngoingConferenceCallView = this.mVectorOngoingConferenceCallView;
        if (vectorOngoingConferenceCallView != null) {
            vectorOngoingConferenceCallView.setCallClickListener(null);
        }
        ActiveWidgetsBanner activeWidgetsBanner = this.mActiveWidgetsBanner;
        if (activeWidgetsBanner != null) {
            activeWidgetsBanner.setOnUpdateListener(null);
        }
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.securityChecks.activityStopped();
        ReadMarkerManager readMarkerManager = this.mReadMarkerManager;
        if (readMarkerManager != null) {
            readMarkerManager.onPause();
        }
        cancelTypingNotification();
        Room room = this.mRoom;
        if (room != null) {
            room.removeEventListener(this.mRoomEventListener);
        }
        Matrix.getInstance(this).removeNetworkEventListener(this.mNetworkEventListener);
        if (this.mSession.isAlive() && this.mSession.getDataHandler() != null) {
            this.mSession.getDataHandler().removeListener(this.mGlobalEventListener);
            this.mSession.getDataHandler().removeListener(this.mResourceLimitEventListener);
        }
        this.mVectorOngoingConferenceCallView.onActivityPause();
        this.mActiveWidgetsBanner.onActivityPause();
        ViewedRoomTracker.getInstance().setViewedRoomId(null);
        ViewedRoomTracker.getInstance().setMatrixId(null);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        Log.d(LOG_TAG, "++ Resume the activity");
        super.onResume();
        this.securityChecks.checkOnActivityStart();
        applyScreenshotSecurity();
        ViewedRoomTracker.getInstance().setMatrixId(this.mSession.getCredentials().userId);
        Room room = this.mRoom;
        int i = R.drawable.e2e_verified;
        int i2 = 8;
        if (room != null) {
            if (room.isReady()) {
                String str = "## onResume() : the user is not anymore a member of the room.";
                if (!this.mRoom.isMember()) {
                    Log.e(LOG_TAG, str);
                    finish();
                    return;
                } else if (!this.mSession.getDataHandler().doesRoomExist(this.mRoom.getRoomId())) {
                    Log.e(LOG_TAG, str);
                    finish();
                    return;
                } else if (this.mRoom.isLeaving()) {
                    Log.e(LOG_TAG, "## onResume() : the user is leaving the room.");
                    finish();
                    return;
                }
            }
            ViewedRoomTracker.getInstance().setViewedRoomId(this.mRoom.getRoomId());
            this.mRoom.addEventListener(this.mRoomEventListener);
            setEditTextHint(this.mVectorMessageListFragment.getCurrentSelectedEvent());
            View view = this.mSyncInProgressView;
            if (VectorApp.isSessionSyncing(this.mSession)) {
                i2 = 0;
            }
            view.setVisibility(i2);
        } else {
            this.mSyncInProgressView.setVisibility(8);
            if (this.mTchapUser != null) {
                this.mEditText.setHint(this.mSession.isCryptoEnabled() ? R.string.room_message_placeholder_encrypted : R.string.room_message_placeholder_not_encrypted);
                this.mE2eImageView.setImageResource(this.mSession.isCryptoEnabled() ? R.drawable.e2e_verified : R.drawable.e2e_unencrypted);
            }
        }
        this.mSession.getDataHandler().addListener(this.mGlobalEventListener);
        this.mSession.getDataHandler().addListener(this.mResourceLimitEventListener);
        Matrix.getInstance(this).addNetworkEventListener(this.mNetworkEventListener);
        if (this.mRoom != null) {
            EventStreamService.cancelNotificationsForRoomId(this.mSession.getCredentials().userId, this.mRoom.getRoomId());
        }
        if (!(this.mRoom == null || Matrix.getInstance(this).getDefaultLatestChatMessageCache() == null)) {
            String latestText = Matrix.getInstance(this).getDefaultLatestChatMessageCache().getLatestText(this, this.mRoom.getRoomId());
            if (!latestText.equals(this.mEditText.getText().toString())) {
                this.mIgnoreTextUpdate = true;
                this.mEditText.setText("");
                this.mEditText.append(latestText);
                this.mIgnoreTextUpdate = false;
            }
            boolean z = this.mRoom.isEncrypted() && this.mSession.isCryptoEnabled();
            ImageView imageView = this.mE2eImageView;
            if (!z) {
                i = R.drawable.e2e_unencrypted;
            }
            imageView.setImageResource(i);
            this.mVectorMessageListFragment.setIsRoomEncrypted(this.mRoom.isEncrypted());
        }
        manageSendMoreButtons();
        updateActionBarTitleAndTopic();
        sendReadReceipt();
        refreshCallButtons(true);
        checkSendEventStatus();
        enableActionBarHeader(this.mIsHeaderViewDisplayed);
        VectorMessageListFragment vectorMessageListFragment = this.mVectorMessageListFragment;
        if (vectorMessageListFragment != null) {
            vectorMessageListFragment.refresh();
            if (this.mVectorMessageListFragment.mMessageListView != null) {
                this.mVectorMessageListFragment.mMessageListView.lockSelectionOnResize();
            }
            int i3 = this.mScrollToIndex;
            if (i3 > 0) {
                this.mVectorMessageListFragment.scrollToIndexWhenLoaded(i3);
                this.mScrollToIndex = -1;
            }
        }
        if (this.mCallId != null) {
            IMXCall activeCall = CallsManager.getSharedInstance().getActiveCall();
            if (activeCall == null || activeCall.getCallId().equals(this.mCallId)) {
                final Intent intent = new Intent(this, VectorCallViewActivity.class);
                intent.putExtra(VectorCallViewActivity.EXTRA_MATRIX_ID, this.mSession.getCredentials().userId);
                intent.putExtra(VectorCallViewActivity.EXTRA_CALL_ID, this.mCallId);
                enableActionBarHeader(false);
                runOnUiThread(new Runnable() {
                    public void run() {
                        VectorRoomActivity.this.startActivity(intent);
                    }
                });
            }
            this.mCallId = null;
        }
        if (sRoomPreviewData == null && this.mEventId == null) {
            this.mVectorPendingCallView.checkPendingCall();
            this.mVectorOngoingConferenceCallView.onActivityResume();
            this.mActiveWidgetsBanner.onActivityResume();
        }
        this.mEditText.initAutoCompletions(this.mSession, this.mRoom);
        ReadMarkerManager readMarkerManager = this.mReadMarkerManager;
        if (readMarkerManager != null) {
            readMarkerManager.onResume();
        }
        Log.d(LOG_TAG, "-- Resume the activity");
    }

    private void setEditTextHint(Event event) {
        Room room = this.mRoom;
        if (room != null) {
            if (room.canReplyTo(event)) {
                this.mEditText.setHint((!this.mRoom.isEncrypted() || !this.mSession.isCryptoEnabled()) ? R.string.room_message_placeholder_reply_to_not_encrypted : R.string.room_message_placeholder_reply_to_encrypted);
                this.mRoomReplyArea.setVisibility(0);
                ((InputMethodManager) getSystemService("input_method")).showSoftInput(this.mEditText, 1);
                RoomState state = this.mRoom.getState();
                String str = "";
                this.mReplySenderName.setText((state == null || event.getSender() == null) ? str : state.getMemberName(event.getSender()));
                if (event.isEncrypted()) {
                    event = event.getClearEvent();
                }
                Message message = JsonUtils.toMessage(event.getContent());
                if (message != null) {
                    str = message.body;
                }
                this.mReplyMessage.setText(str);
            } else {
                this.mEditText.setHint((!this.mRoom.isEncrypted() || !this.mSession.isCryptoEnabled()) ? R.string.room_message_placeholder_not_encrypted : R.string.room_message_placeholder_encrypted);
                this.mRoomReplyArea.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != -1) {
            return;
        }
        if (i != 12000) {
            switch (i) {
                case 0:
                case 1:
                case 6:
                    if (!sendMessageByInvitingLeftMemberInDirectChat(intent) && !sendMessageByCreatingNewDirectChat(intent)) {
                        sendMediasIntent(intent);
                        return;
                    }
                    return;
                case 2:
                    insertUserDisplayNameInTextEditor(intent.getStringExtra(VectorMemberDetailsActivity.RESULT_MENTION_ID));
                    return;
                case 3:
                    onActivityResultRoomAvatarUpdate(intent);
                    return;
                case 4:
                    onActivityResultRoomInvite(intent);
                    return;
                case 5:
                    this.mVectorMessageListFragment.scrollToBottom(0);
                    return;
                case 7:
                    ArrayList arrayList = new ArrayList(RoomMediaMessage.listRoomMediaMessages(intent));
                    if (arrayList.size() == 0) {
                        arrayList.add(new RoomMediaMessage(Uri.parse(intent.getStringExtra(MediaPreviewerActivity.EXTRA_CAMERA_PICTURE_URI))));
                    }
                    this.mVectorRoomMediasSender.sendMedias(arrayList);
                    return;
                default:
                    return;
            }
        } else {
            sendSticker(intent);
        }
    }

    public void onMessageSendingSucceeded(Event event) {
        refreshNotificationsArea();
    }

    public void onMessageSendingFailed(Event event) {
        refreshNotificationsArea();
    }

    public void onMessageRedacted(Event event) {
        refreshNotificationsArea();
    }

    public void onUnknownDevices(Event event, MXCryptoError mXCryptoError) {
        refreshNotificationsArea();
        CommonActivityUtils.displayUnknownDevicesDialog(this.mSession, this, (MXUsersDevicesMap) mXCryptoError.mExceptionData, false, new IUnknownDevicesSendAnywayListener() {
            public void onSendAnyway() {
                VectorRoomActivity.this.mVectorMessageListFragment.resendUnsentMessages();
                VectorRoomActivity.this.refreshNotificationsArea();
            }
        });
    }

    public void onConsentNotGiven(Event event, MatrixError matrixError) {
        refreshNotificationsArea();
        getConsentNotGivenHelper().displayDialog(matrixError);
    }

    private void sendReadReceipt() {
        Room room = this.mRoom;
        if (room != null && sRoomPreviewData == null) {
            final Event event = this.mLatestDisplayedEvent;
            room.sendReadReceipt(event, new ApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    try {
                        if (!VectorRoomActivity.this.isFinishing() && event != null && VectorRoomActivity.this.mVectorMessageListFragment.getMessageAdapter() != null) {
                            VectorRoomActivity.this.mVectorMessageListFragment.getMessageAdapter().updateReadMarker(VectorRoomActivity.this.mRoom.getReadMarkerEventId(), event.eventId);
                        }
                    } catch (Exception e) {
                        String access$300 = VectorRoomActivity.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## sendReadReceipt() : failed ");
                        sb.append(e.getMessage());
                        Log.e(access$300, sb.toString(), e);
                    }
                }

                public void onNetworkError(Exception exc) {
                    String access$300 = VectorRoomActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## sendReadReceipt() : failed ");
                    sb.append(exc.getMessage());
                    Log.e(access$300, sb.toString(), exc);
                }

                public void onMatrixError(MatrixError matrixError) {
                    String access$300 = VectorRoomActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## sendReadReceipt() : failed ");
                    sb.append(matrixError.getMessage());
                    Log.e(access$300, sb.toString());
                }

                public void onUnexpectedError(Exception exc) {
                    String access$300 = VectorRoomActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## sendReadReceipt() : failed ");
                    sb.append(exc.getMessage());
                    Log.e(access$300, sb.toString(), exc);
                }
            });
            refreshNotificationsArea();
        }
    }

    public void onScroll(int i, int i2, int i3) {
        Event event = this.mVectorMessageListFragment.getEvent((i + i2) - 1);
        Event event2 = this.mVectorMessageListFragment.getEvent(i);
        if (event != null && (this.mLatestDisplayedEvent == null || !TextUtils.equals(event.eventId, this.mLatestDisplayedEvent.eventId))) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onScroll firstVisibleItem ");
            sb.append(i);
            sb.append(" visibleItemCount ");
            sb.append(i2);
            sb.append(" totalItemCount ");
            sb.append(i3);
            Log.d(str, sb.toString());
            this.mLatestDisplayedEvent = event;
            if (!VectorApp.isAppInBackground()) {
                sendReadReceipt();
            } else {
                Log.d(LOG_TAG, "## onScroll : the app is in background");
            }
        }
        ReadMarkerManager readMarkerManager = this.mReadMarkerManager;
        if (readMarkerManager != null) {
            readMarkerManager.onScroll(i, i2, i3, event2, event);
        }
    }

    public void onScrollStateChanged(int i) {
        ReadMarkerManager readMarkerManager = this.mReadMarkerManager;
        if (readMarkerManager != null) {
            readMarkerManager.onScrollStateChanged(i);
        }
    }

    public void onLatestEventDisplay(boolean z) {
        Boolean bool = this.mIsScrolledToTheBottom;
        if (bool == null || z != bool.booleanValue()) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onLatestEventDisplay : isDisplayed ");
            sb.append(z);
            Log.d(str, sb.toString());
            if (z) {
                Room room = this.mRoom;
                if (room != null) {
                    this.mLatestDisplayedEvent = room.getDataHandler().getStore().getLatestEvent(this.mRoom.getRoomId());
                    this.mRoom.sendReadReceipt();
                }
            }
            this.mIsScrolledToTheBottom = Boolean.valueOf(z);
            refreshNotificationsArea();
        }
    }

    /* access modifiers changed from: private */
    public void openIntegrationManagerActivity(String str) {
        if (this.mRoom != null) {
            startActivity(IntegrationManagerActivity.Companion.getIntent(this, this.mMyUserId, this.mRoom.getRoomId(), null, str));
        }
    }

    private boolean isUserAllowedToStartConfCall() {
        Room room = this.mRoom;
        boolean z = true;
        if (room == null || !room.isOngoingConferenceCall()) {
            Room room2 = this.mRoom;
            if (room2 != null && room2.getNumberOfMembers() > 2) {
                PowerLevels powerLevels = this.mRoom.getState().getPowerLevels();
                if (powerLevels == null || powerLevels.getUserPowerLevel(this.mSession.getMyUserId()) < powerLevels.invite) {
                    z = false;
                }
            }
        } else {
            Log.d(LOG_TAG, "## isUserAllowedToStartConfCall(): conference in progress");
        }
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## isUserAllowedToStartConfCall(): isAllowed=");
        sb.append(z);
        Log.d(str, sb.toString());
        return z;
    }

    private void displayConfCallNotAllowed() {
        new Builder(this).setTitle((int) R.string.missing_permissions_title_to_start_conf_call).setMessage((int) R.string.missing_permissions_to_start_conf_call).setIcon(17301543).setPositiveButton((int) R.string.ok, (OnClickListener) null).show();
    }

    /* access modifiers changed from: private */
    public void displayVideoCallIpDialog() {
        enableActionBarHeader(false);
        new Builder(this).setAdapter(new DialogCallAdapter(this), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                VectorRoomActivity.this.onCallItemClicked(i);
            }
        }).show();
    }

    /* access modifiers changed from: private */
    public void onCallItemClicked(int i) {
        final boolean z;
        final int i2;
        final int i3;
        if (i == 0) {
            z = false;
            i3 = 4;
            i2 = PermissionsToolsKt.PERMISSION_REQUEST_CODE_AUDIO_CALL;
        } else {
            z = true;
            i3 = 5;
            i2 = PermissionsToolsKt.PERMISSION_REQUEST_CODE_VIDEO_CALL;
        }
        Builder title = new Builder(this).setTitle((int) R.string.dialog_title_confirmation);
        if (z) {
            title.setMessage((CharSequence) getString(R.string.start_video_call_prompt_msg));
        } else {
            title.setMessage((CharSequence) getString(R.string.start_voice_call_prompt_msg));
        }
        title.setPositiveButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (PermissionsToolsKt.checkPermissions(i3, (Activity) VectorRoomActivity.this, i2)) {
                    VectorRoomActivity vectorRoomActivity = VectorRoomActivity.this;
                    vectorRoomActivity.startIpCall(PreferencesManager.useJitsiConfCall(vectorRoomActivity), z);
                }
            }
        }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
    }

    /* access modifiers changed from: private */
    public void launchJitsiActivity(Widget widget, boolean z) {
        Intent intent = new Intent(this, JitsiCallActivity.class);
        intent.putExtra("EXTRA_WIDGET_ID", widget);
        intent.putExtra(JitsiCallActivity.EXTRA_ENABLE_VIDEO, z);
        startActivity(intent);
    }

    /* access modifiers changed from: private */
    public void startJitsiCall(boolean z) {
        enableActionBarHeader(false);
        showWaitingView();
        WidgetsManager.getSharedInstance().createJitsiWidget(this.mSession, this.mRoom, z, new ApiCallback<Widget>() {
            public void onSuccess(Widget widget) {
                VectorRoomActivity.this.hideWaitingView();
                Intent intent = new Intent(VectorRoomActivity.this, JitsiCallActivity.class);
                intent.putExtra("EXTRA_WIDGET_ID", widget);
                VectorRoomActivity.this.startActivity(intent);
            }

            private void onError(String str) {
                VectorRoomActivity.this.hideWaitingView();
                Toast.makeText(VectorRoomActivity.this, str, 0).show();
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
    public void startIpCall(final boolean z, final boolean z2) {
        Room room = this.mRoom;
        if (room != null) {
            if (room.getNumberOfMembers() <= 2 || !z) {
                enableActionBarHeader(false);
                showWaitingView();
                this.mSession.mCallsManager.createCallInRoom(this.mRoom.getRoomId(), z2, new ApiCallback<IMXCall>() {
                    public void onSuccess(final IMXCall iMXCall) {
                        Log.d(VectorRoomActivity.LOG_TAG, "## startIpCall(): onSuccess");
                        VectorRoomActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                VectorRoomActivity.this.hideWaitingView();
                                Intent intent = new Intent(VectorRoomActivity.this, VectorCallViewActivity.class);
                                intent.putExtra(VectorCallViewActivity.EXTRA_MATRIX_ID, VectorRoomActivity.this.mSession.getCredentials().userId);
                                intent.putExtra(VectorCallViewActivity.EXTRA_CALL_ID, iMXCall.getCallId());
                                VectorRoomActivity.this.startActivity(intent);
                            }
                        });
                    }

                    private void onError(final String str) {
                        VectorRoomActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                VectorRoomActivity.this.hideWaitingView();
                                VectorRoomActivity vectorRoomActivity = VectorRoomActivity.this;
                                StringBuilder sb = new StringBuilder();
                                sb.append(VectorRoomActivity.this.getString(R.string.cannot_start_call));
                                sb.append(" (");
                                sb.append(str);
                                sb.append(")");
                                Toast.makeText(vectorRoomActivity, sb.toString(), 0).show();
                            }
                        });
                    }

                    public void onNetworkError(Exception exc) {
                        String access$300 = VectorRoomActivity.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## startIpCall(): onNetworkError Msg=");
                        sb.append(exc.getMessage());
                        Log.e(access$300, sb.toString(), exc);
                        onError(exc.getLocalizedMessage());
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        String access$300 = VectorRoomActivity.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## startIpCall(): onMatrixError Msg=");
                        sb.append(matrixError.getLocalizedMessage());
                        Log.e(access$300, sb.toString());
                        if (matrixError instanceof MXCryptoError) {
                            MXCryptoError mXCryptoError = (MXCryptoError) matrixError;
                            if (MXCryptoError.UNKNOWN_DEVICES_CODE.equals(mXCryptoError.errcode)) {
                                VectorRoomActivity.this.hideWaitingView();
                                CommonActivityUtils.displayUnknownDevicesDialog(VectorRoomActivity.this.mSession, VectorRoomActivity.this, (MXUsersDevicesMap) mXCryptoError.mExceptionData, true, new IUnknownDevicesSendAnywayListener() {
                                    public void onSendAnyway() {
                                        VectorRoomActivity.this.startIpCall(z, z2);
                                    }
                                });
                                return;
                            }
                        }
                        onError(matrixError.getLocalizedMessage());
                    }

                    public void onUnexpectedError(Exception exc) {
                        String access$300 = VectorRoomActivity.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## startIpCall(): onUnexpectedError Msg=");
                        sb.append(exc.getLocalizedMessage());
                        Log.e(access$300, sb.toString(), exc);
                        onError(exc.getLocalizedMessage());
                    }
                });
                return;
            }
            startJitsiCall(z2);
        }
    }

    public void cancelSelectionMode() {
        this.mVectorMessageListFragment.cancelSelectionMode();
    }

    /* access modifiers changed from: private */
    public void tchapSendTextMessage() {
        if (TextUtils.isEmpty(this.mEditText.getText())) {
            refreshCallButtons(true);
        } else if (!sendMessageByInvitingLeftMemberInDirectChat(null) && !sendMessageByCreatingNewDirectChat(null)) {
            sendTextMessage();
        }
    }

    /* access modifiers changed from: private */
    public void sendTextMessage() {
        if (!this.mIsMarkDowning) {
            this.mSendMessageView.setEnabled(false);
            final boolean z = true;
            this.mIsMarkDowning = true;
            String trim = this.mEditText.getText().toString().trim();
            if (trim.startsWith("\\/")) {
                trim = trim.substring(1);
                z = false;
            }
            VectorApp.markdownToHtml(trim, new IVectorMarkdownParserListener() {
                public void onMarkdownParsed(final String str, final String str2) {
                    VectorRoomActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            VectorRoomActivity.this.mSendMessageView.setEnabled(true);
                            VectorRoomActivity.this.mIsMarkDowning = false;
                            VectorRoomActivity.this.enableActionBarHeader(false);
                            VectorRoomActivity vectorRoomActivity = VectorRoomActivity.this;
                            String str = str;
                            vectorRoomActivity.sendMessage(str, TextUtils.equals(str, str2) ? null : str2, Message.FORMAT_MATRIX_HTML, z);
                            VectorRoomActivity.this.mEditText.setText("");
                        }
                    });
                }
            });
        }
    }

    public void sendMessage(String str, String str2, String str3, boolean z) {
        if (!TextUtils.isEmpty(str)) {
            if (z) {
                if (SlashCommandsParser.manageSplashCommand(this, this.mSession, this.mRoom, str, str2, str3)) {
                    return;
                }
            }
            Event currentSelectedEvent = this.mVectorMessageListFragment.getCurrentSelectedEvent();
            cancelSelectionMode();
            this.mVectorMessageListFragment.sendTextMessage(str, str2, currentSelectedEvent, str3);
        }
    }

    public void sendEmote(String str, String str2, String str3) {
        VectorMessageListFragment vectorMessageListFragment = this.mVectorMessageListFragment;
        if (vectorMessageListFragment != null) {
            vectorMessageListFragment.sendEmote(str, str2, str3);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005d, code lost:
        if ("text/plain".equals(((org.matrix.androidsdk.data.RoomMediaMessage) r0.get(0)).getMimeType(r6)) != false) goto L_0x0061;
     */
    public void sendMediasIntent(Intent intent) {
        if (intent != null || this.mLatestTakePictureCameraUri != null) {
            ArrayList arrayList = new ArrayList();
            if (intent != null) {
                arrayList = new ArrayList(RoomMediaMessage.listRoomMediaMessages(intent));
            }
            if (arrayList.size() == 0 && intent != null) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    String str = "android.intent.extra.TEXT";
                    if (extras.containsKey(str)) {
                        this.mEditText.append(extras.getString(str));
                        this.mEditText.post(new Runnable() {
                            public void run() {
                                VectorRoomActivity.this.mEditText.setSelection(VectorRoomActivity.this.mEditText.getText().length());
                            }
                        });
                    }
                }
            }
            boolean z = true;
            boolean z2 = !arrayList.isEmpty();
            if (arrayList.size() == 1) {
            }
            z = false;
            boolean previewMediaWhenSending = PreferencesManager.previewMediaWhenSending(this);
            if (!z2 || z || !previewMediaWhenSending) {
                if (this.mLatestTakePictureCameraUri != null && arrayList.size() == 0) {
                    arrayList.add(new RoomMediaMessage(Uri.parse(this.mLatestTakePictureCameraUri)));
                }
                this.mVectorRoomMediasSender.sendMedias(arrayList);
            } else {
                if (intent != null) {
                    intent.setClass(this, MediaPreviewerActivity.class);
                } else {
                    intent = new Intent(this, MediaPreviewerActivity.class);
                }
                intent.setExtrasClassLoader(RoomMediaMessage.class.getClassLoader());
                Room room = this.mRoom;
                if (room != null) {
                    intent.putExtra(MediaPreviewerActivity.EXTRA_ROOM_TITLE, DinsicUtils.getRoomDisplayName(this, room));
                }
                String str2 = this.mLatestTakePictureCameraUri;
                if (str2 != null) {
                    intent.putExtra(MediaPreviewerActivity.EXTRA_CAMERA_PICTURE_URI, str2);
                }
                startActivityForResult(intent, 7);
            }
            this.mLatestTakePictureCameraUri = null;
        }
    }

    private void sendSticker(Intent intent) {
        if (this.mRoom != null) {
            this.mVectorMessageListFragment.sendStickerMessage(new Event(Event.EVENT_TYPE_STICKER, new JsonParser().parse(StickerPickerActivity.Companion.getResultContent(intent)).getAsJsonObject(), this.mSession.getCredentials().userId, this.mRoom.getRoomId()));
        }
    }

    private String leftMemberInDirectChat() {
        Room room = this.mRoom;
        if (room != null && room.isDirect()) {
            for (RoomMember roomMember : this.mRoom.getState().getDisplayableLoadedMembers()) {
                if (TextUtils.equals(roomMember.membership, "leave")) {
                    return roomMember.getUserId();
                }
            }
        }
        return null;
    }

    private boolean sendMessageByInvitingLeftMemberInDirectChat(final Intent intent) {
        final String leftMemberInDirectChat = leftMemberInDirectChat();
        if (leftMemberInDirectChat == null) {
            return false;
        }
        this.mSession.getProfileApiClient().displayname(leftMemberInDirectChat, new ApiCallback<String>() {
            public void onSuccess(String str) {
                if (str == null) {
                    Log.d(VectorRoomActivity.LOG_TAG, "sendMessageByInvitingLeftMemberInDirectChat: the left member has deactivated his account");
                    Toast.makeText(VectorRoomActivity.this.getApplicationContext(), VectorRoomActivity.this.getString(R.string.tchap_cannot_invite_deactivated_account_user), 1).show();
                    return;
                }
                String access$300 = VectorRoomActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("sendMessageByInvitingLeftMemberInDirectChat: invite again ");
                sb.append(leftMemberInDirectChat);
                Log.d(access$300, sb.toString());
                VectorRoomActivity.this.mRoom.invite(leftMemberInDirectChat, (ApiCallback<Void>) new ApiCallback<Void>() {
                    public void onSuccess(Void voidR) {
                        if (intent != null) {
                            Log.d(VectorRoomActivity.LOG_TAG, "sendMessageByInvitingLeftMemberInDirectChat: sendMediasIntent");
                            VectorRoomActivity.this.sendMediasIntent(intent);
                            return;
                        }
                        Log.d(VectorRoomActivity.LOG_TAG, "sendMessageByInvitingLeftMemberInDirectChat: sendTextMessage");
                        VectorRoomActivity.this.sendTextMessage();
                    }

                    public void onNetworkError(Exception exc) {
                        String access$300 = VectorRoomActivity.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("sendMessageByInvitingLeftMemberInDirectChat invite failed ");
                        sb.append(exc.getMessage());
                        Log.e(access$300, sb.toString());
                        Toast.makeText(VectorRoomActivity.this.getApplicationContext(), VectorRoomActivity.this.getString(R.string.tchap_error_message_default), 0).show();
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        String access$300 = VectorRoomActivity.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("sendMessageByInvitingLeftMemberInDirectChat invite failed ");
                        sb.append(matrixError.getMessage());
                        Log.e(access$300, sb.toString());
                        Toast.makeText(VectorRoomActivity.this.getApplicationContext(), VectorRoomActivity.this.getString(R.string.tchap_error_message_default), 0).show();
                    }

                    public void onUnexpectedError(Exception exc) {
                        String access$300 = VectorRoomActivity.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("sendMessageByInvitingLeftMemberInDirectChat invite failed ");
                        sb.append(exc.getMessage());
                        Log.e(access$300, sb.toString());
                        Toast.makeText(VectorRoomActivity.this.getApplicationContext(), VectorRoomActivity.this.getString(R.string.tchap_error_message_default), 0).show();
                    }
                });
            }

            public void onNetworkError(Exception exc) {
                String access$300 = VectorRoomActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("sendMessageByInvitingLeftMemberInDirectChat get displayname failed ");
                sb.append(exc.getMessage());
                Log.e(access$300, sb.toString());
                Toast.makeText(VectorRoomActivity.this.getApplicationContext(), VectorRoomActivity.this.getString(R.string.tchap_error_message_default), 0).show();
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$300 = VectorRoomActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("sendMessageByInvitingLeftMemberInDirectChat get displayname failed ");
                sb.append(matrixError.getMessage());
                Log.e(access$300, sb.toString());
                Toast.makeText(VectorRoomActivity.this.getApplicationContext(), VectorRoomActivity.this.getString(R.string.tchap_error_message_default), 0).show();
            }

            public void onUnexpectedError(Exception exc) {
                String access$300 = VectorRoomActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("sendMessageByInvitingLeftMemberInDirectChat get displayname failed ");
                sb.append(exc.getMessage());
                Log.e(access$300, sb.toString());
                Toast.makeText(VectorRoomActivity.this.getApplicationContext(), VectorRoomActivity.this.getString(R.string.tchap_error_message_default), 0).show();
            }
        });
        return true;
    }

    private boolean sendMessageByCreatingNewDirectChat(final Intent intent) {
        if (this.mTchapUser == null) {
            return false;
        }
        if (!DinsicUtils.isExternalTchapSession(this.mSession) || !DinsicUtils.isExternalTchapUser(this.mTchapUser.user_id)) {
            this.mSendMessageView.setEnabled(false);
            this.mSendAttachedFileView.setEnabled(false);
            showWaitingView();
            DinsicUtils.createDirectChat(this.mSession, this.mTchapUser.user_id, new ApiCallback<String>() {
                public void onSuccess(String str) {
                    VectorRoomActivity.this.hideWaitingView();
                    HashMap hashMap = new HashMap();
                    hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", VectorRoomActivity.this.mSession.getMyUserId());
                    hashMap.put("EXTRA_ROOM_ID", str);
                    Intent intent = intent;
                    if (intent != null) {
                        hashMap.put(VectorRoomActivity.EXTRA_ROOM_INTENT, intent);
                    } else {
                        hashMap.put(VectorRoomActivity.EXTRA_TEXT_MESSAGE, VectorRoomActivity.this.mEditText.getText().toString());
                    }
                    Log.d(VectorRoomActivity.LOG_TAG, "## sendMessageByCreatingNewDirectChat: onSuccess - start goToRoomPage");
                    VectorRoomActivity vectorRoomActivity = VectorRoomActivity.this;
                    CommonActivityUtils.goToRoomPage(vectorRoomActivity, vectorRoomActivity.mSession, hashMap);
                }

                private void onError(final String str) {
                    VectorRoomActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            if (str != null) {
                                Toast.makeText(VectorRoomActivity.this, str, 1).show();
                            }
                            VectorRoomActivity.this.mSendMessageView.setEnabled(true);
                            VectorRoomActivity.this.mSendAttachedFileView.setEnabled(true);
                            VectorRoomActivity.this.hideWaitingView();
                        }
                    });
                }

                public void onNetworkError(Exception exc) {
                    onError(exc.getLocalizedMessage());
                }

                public void onMatrixError(MatrixError matrixError) {
                    if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                        VectorRoomActivity.this.hideWaitingView();
                        VectorRoomActivity.this.onConsentNotGiven(null, matrixError);
                        return;
                    }
                    onError(matrixError.getLocalizedMessage());
                }

                public void onUnexpectedError(Exception exc) {
                    onError(exc.getLocalizedMessage());
                }
            });
        } else {
            DinsicUtils.alertSimpleMsg(this, getString(R.string.room_creation_forbidden));
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void handleTypingNotification(boolean z) {
        if (!PreferencesManager.sendTypingNotifs(this)) {
            Log.d(LOG_TAG, "##handleTypingNotification() : the typing notifications are disabled");
        } else if (this.mRoom != null) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("##handleTypingNotification() : isTyping ");
            sb.append(z);
            Log.d(str, sb.toString());
            int i = -1;
            if (!z) {
                TimerTask timerTask = this.mTypingTimerTask;
                if (timerTask != null) {
                    timerTask.cancel();
                    this.mTypingTimerTask = null;
                }
                Timer timer = this.mTypingTimer;
                if (timer != null) {
                    timer.cancel();
                    this.mTypingTimer = null;
                }
                this.mLastTypingDate = 0;
            } else if (this.mTypingTimer != null) {
                System.currentTimeMillis();
                this.mLastTypingDate = System.currentTimeMillis();
                return;
            } else {
                int i2 = TYPING_TIMEOUT_MS;
                if (0 != this.mLastTypingDate) {
                    long currentTimeMillis = System.currentTimeMillis() - this.mLastTypingDate;
                    long j = (long) TYPING_TIMEOUT_MS;
                    i2 = currentTimeMillis < j ? (int) (j - currentTimeMillis) : 0;
                } else {
                    this.mLastTypingDate = System.currentTimeMillis();
                }
                if (i2 > 0) {
                    try {
                        this.mTypingTimerTask = new TimerTask() {
                            public void run() {
                                synchronized (VectorRoomActivity.LOG_TAG) {
                                    if (VectorRoomActivity.this.mTypingTimerTask != null) {
                                        VectorRoomActivity.this.mTypingTimerTask.cancel();
                                        VectorRoomActivity.this.mTypingTimerTask = null;
                                    }
                                    if (VectorRoomActivity.this.mTypingTimer != null) {
                                        VectorRoomActivity.this.mTypingTimer.cancel();
                                        VectorRoomActivity.this.mTypingTimer = null;
                                    }
                                    Log.d(VectorRoomActivity.LOG_TAG, "##handleTypingNotification() : send end of typing");
                                    VectorRoomActivity.this.handleTypingNotification(0 != VectorRoomActivity.this.mLastTypingDate);
                                }
                            }
                        };
                        try {
                            synchronized (LOG_TAG) {
                                this.mTypingTimer = new Timer();
                                this.mTypingTimer.schedule(this.mTypingTimerTask, 10000);
                            }
                        } catch (Throwable th) {
                            String str2 = LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("fails to launch typing timer ");
                            sb2.append(th.getMessage());
                            Log.e(str2, sb2.toString());
                            this.mTypingTimer = null;
                            this.mTypingTimerTask = null;
                        }
                        i = BuildConfig.VERSION_CODE;
                    } catch (Throwable th2) {
                        String str3 = LOG_TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("## mTypingTimerTask creation failed ");
                        sb3.append(th2.getMessage());
                        Log.e(str3, sb3.toString());
                        return;
                    }
                } else {
                    z = false;
                }
            }
            this.mRoom.sendTypingNotification(z, i, new SimpleApiCallback<Void>(this) {
                public void onSuccess(Void voidR) {
                    VectorRoomActivity.this.mLastTypingDate = 0;
                }

                public void onNetworkError(Exception exc) {
                    if (VectorRoomActivity.this.mTypingTimerTask != null) {
                        VectorRoomActivity.this.mTypingTimerTask.cancel();
                        VectorRoomActivity.this.mTypingTimerTask = null;
                    }
                    if (VectorRoomActivity.this.mTypingTimer != null) {
                        VectorRoomActivity.this.mTypingTimer.cancel();
                        VectorRoomActivity.this.mTypingTimer = null;
                    }
                }
            });
        }
    }

    private void cancelTypingNotification() {
        if (!(this.mRoom == null || 0 == this.mLastTypingDate)) {
            TimerTask timerTask = this.mTypingTimerTask;
            if (timerTask != null) {
                timerTask.cancel();
                this.mTypingTimerTask = null;
            }
            Timer timer = this.mTypingTimer;
            if (timer != null) {
                timer.cancel();
                this.mTypingTimer = null;
            }
            this.mLastTypingDate = 0;
            this.mRoom.sendTypingNotification(false, -1, new SimpleApiCallback<Void>(this) {
                public void onSuccess(Void voidR) {
                }
            });
        }
    }

    private void launchRoomDetails(int i) {
        if (this.mSession != null && this.mRoom != null) {
            enableActionBarHeader(false);
            Intent intent = new Intent(this, VectorRoomDetailsActivity.class);
            intent.putExtra(VectorRoomDetailsActivity.EXTRA_ROOM_ID, this.mRoom.getRoomId());
            intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getCredentials().userId);
            intent.putExtra(VectorRoomDetailsActivity.EXTRA_SELECTED_TAB_ID, i);
            startActivityForResult(intent, 2);
        }
    }

    private void launchDirectRoomDetails() {
        MXSession mXSession = this.mSession;
        if (mXSession != null) {
            Room room = this.mRoom;
            if (room != null && room.getMember(mXSession.getMyUserId()) != null) {
                Intent intent = new Intent(this, TchapDirectRoomDetailsActivity.class);
                intent.putExtra(TchapDirectRoomDetailsActivity.EXTRA_ROOM_ID, this.mRoom.getRoomId());
                intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getCredentials().userId);
                startActivityForResult(intent, 2);
            }
        }
    }

    private void launchAudioRecorderIntent() {
        enableActionBarHeader(false);
        ExternalApplicationsUtilKt.openSoundRecorder(this, 6);
    }

    private void launchFileSelectionIntent() {
        enableActionBarHeader(false);
        ExternalApplicationsUtilKt.openFileSelection(this, null, true, 0);
    }

    private void startStickerPickerActivity() {
        String str;
        String str2;
        Iterator it = this.mSession.getUserWidgets().values().iterator();
        while (true) {
            if (!it.hasNext()) {
                str = null;
                str2 = null;
                break;
            }
            Object next = it.next();
            if (next instanceof Map) {
                Map map = (Map) next;
                Object obj = map.get(BingRule.KIND_CONTENT);
                if (obj != null && (obj instanceof Map)) {
                    Map map2 = (Map) obj;
                    Object obj2 = map2.get(PasswordLoginParams.IDENTIFIER_KEY_TYPE);
                    if (obj2 != null && (obj2 instanceof String) && obj2.equals(StickerPickerActivity.WIDGET_NAME)) {
                        str = (String) map2.get("url");
                        str2 = (String) map.get("id");
                        break;
                    }
                }
            }
        }
        if (TextUtils.isEmpty(str)) {
            Builder builder = new Builder(this);
            builder.setView(LayoutInflater.from(builder.getContext()).inflate(R.layout.dialog_no_sticker_pack, null)).setPositiveButton((int) R.string.yes, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    VectorRoomActivity.this.openIntegrationManagerActivity("type_m.stickerpicker");
                }
            }).setNegativeButton((int) R.string.no, (OnClickListener) null).show();
        } else if (this.mRoom != null) {
            startActivityForResult(StickerPickerActivity.Companion.getIntent(this, this.mMyUserId, this.mRoom.getRoomId(), str, str2), RequestCodesKt.STICKER_PICKER_ACTIVITY_REQUEST_CODE);
        }
    }

    private void launchNativeVideoRecorder() {
        enableActionBarHeader(false);
        ExternalApplicationsUtilKt.openVideoRecorder(this, 1);
    }

    private void launchNativeCamera() {
        enableActionBarHeader(false);
        this.mLatestTakePictureCameraUri = ExternalApplicationsUtilKt.openCamera(this, CAMERA_VALUE_TITLE, 1);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (strArr.length == 0) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onRequestPermissionsResult(): cancelled ");
            sb.append(i);
            Log.e(str, sb.toString());
        } else if (i == 569 || i == 570) {
            String str2 = "android.permission.CAMERA";
            boolean z = !PermissionsToolsKt.hasToAskForPermission(this, str2);
            boolean z2 = false;
            for (int i2 = 0; i2 < strArr.length; i2++) {
                String str3 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## onRequestPermissionsResult(): ");
                sb2.append(strArr[i2]);
                sb2.append("=");
                sb2.append(iArr[i2]);
                Log.d(str3, sb2.toString());
                if (str2.equals(strArr[i2])) {
                    if (iArr[i2] == 0) {
                        Log.d(LOG_TAG, "## onRequestPermissionsResult(): CAMERA permission granted");
                        z = true;
                    } else {
                        Log.d(LOG_TAG, "## onRequestPermissionsResult(): CAMERA permission not granted");
                    }
                }
                if ("android.permission.WRITE_EXTERNAL_STORAGE".equals(strArr[i2])) {
                    if (iArr[i2] == 0) {
                        Log.d(LOG_TAG, "## onRequestPermissionsResult(): WRITE_EXTERNAL_STORAGE permission granted");
                        z2 = true;
                    } else {
                        Log.d(LOG_TAG, "## onRequestPermissionsResult(): WRITE_EXTERNAL_STORAGE permission not granted");
                    }
                }
            }
            if (!z) {
                Toast.makeText(this, getString(R.string.missing_permissions_warning), 0).show();
            } else if (i == 569) {
                if (z2) {
                    launchNativeCamera();
                } else {
                    Toast.makeText(this, getString(R.string.missing_permissions_error), 0).show();
                }
            } else if (i != 570) {
            } else {
                if (z2) {
                    launchNativeVideoRecorder();
                } else {
                    Toast.makeText(this, getString(R.string.missing_permissions_error), 0).show();
                }
            }
        } else if (i == 571) {
            if (PermissionsToolsKt.onPermissionResultAudioIpCall(this, iArr)) {
                startIpCall(PreferencesManager.useJitsiConfCall(this), false);
            }
        } else if (i != 572) {
            super.onRequestPermissionsResult(i, strArr, iArr);
        } else if (PermissionsToolsKt.onPermissionResultVideoIpCall(this, iArr)) {
            startIpCall(PreferencesManager.useJitsiConfCall(this), true);
        }
    }

    /* access modifiers changed from: private */
    public void manageSendMoreButtons() {
        if (this.mEditText.getText().length() > 0) {
            this.mSendMessageView.setVisibility(0);
        } else {
            this.mSendMessageView.setVisibility(8);
        }
    }

    public static String sanitizeDisplayname(String str) {
        return (TextUtils.isEmpty(str) || !str.endsWith(" (IRC)")) ? str : str.substring(0, str.length() - 6);
    }

    public void insertTextInTextEditor(String str) {
        if (TextUtils.isEmpty(this.mEditText.getText())) {
            this.mEditText.append(str);
            return;
        }
        Editable text = this.mEditText.getText();
        int selectionStart = this.mEditText.getSelectionStart();
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" ");
        text.insert(selectionStart, sb.toString());
    }

    public void insertUserDisplayNameInTextEditor(String str) {
        if (str != null) {
            boolean z = true;
            String str2 = " ";
            if (TextUtils.equals(this.mSession.getMyUser().displayname, str)) {
                if (TextUtils.isEmpty(this.mEditText.getText())) {
                    VectorAutoCompleteTextView vectorAutoCompleteTextView = this.mEditText;
                    StringBuilder sb = new StringBuilder();
                    sb.append(SlashCommand.EMOTE.getCommand());
                    sb.append(str2);
                    vectorAutoCompleteTextView.append(sb.toString());
                    VectorAutoCompleteTextView vectorAutoCompleteTextView2 = this.mEditText;
                    vectorAutoCompleteTextView2.setSelection(vectorAutoCompleteTextView2.getText().length());
                } else {
                    z = false;
                }
            } else if (TextUtils.isEmpty(this.mEditText.getText())) {
                if (str.startsWith("/")) {
                    this.mEditText.append("\\");
                }
                VectorAutoCompleteTextView vectorAutoCompleteTextView3 = this.mEditText;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(sanitizeDisplayname(str));
                sb2.append(": ");
                vectorAutoCompleteTextView3.append(sb2.toString());
            } else {
                Editable text = this.mEditText.getText();
                int selectionStart = this.mEditText.getSelectionStart();
                StringBuilder sb3 = new StringBuilder();
                sb3.append(sanitizeDisplayname(str));
                sb3.append(str2);
                text.insert(selectionStart, sb3.toString());
            }
            if (z && PreferencesManager.vibrateWhenMentioning(this)) {
                Vibrator vibrator = (Vibrator) getSystemService("vibrator");
                if (vibrator != null && vibrator.hasVibrator()) {
                    vibrator.vibrate(100);
                }
            }
        }
    }

    public void insertQuoteInTextEditor(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (TextUtils.isEmpty(this.mEditText.getText())) {
            this.mEditText.setText("");
            this.mEditText.append(str);
            return;
        }
        Editable text = this.mEditText.getText();
        int selectionStart = this.mEditText.getSelectionStart();
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(str);
        text.insert(selectionStart, sb.toString());
    }

    public void showPreviousEventsLoadingWheel() {
        this.mBackProgressView.setVisibility(0);
    }

    public void hidePreviousEventsLoadingWheel() {
        this.mBackProgressView.setVisibility(8);
    }

    public void showNextEventsLoadingWheel() {
        this.mForwardProgressView.setVisibility(0);
    }

    public void hideNextEventsLoadingWheel() {
        this.mForwardProgressView.setVisibility(8);
    }

    public void showMainLoadingWheel() {
        this.mMainProgressView.setVisibility(0);
    }

    public void hideMainLoadingWheel() {
        this.mMainProgressView.setVisibility(8);
    }

    public void onSelectedEventChange(Event event) {
        setEditTextHint(event);
    }

    /* access modifiers changed from: private */
    public void refreshNotificationsArea() {
        if (this.mSession.getDataHandler() != null && this.mRoom != null && sRoomPreviewData == null) {
            LimitResourceState limitResourceState = this.mResourceLimitEventListener.getLimitResourceState();
            MatrixError resourceLimitExceededError = this.mSession.getDataHandler().getResourceLimitExceededError();
            MatrixError softErrorOrNull = limitResourceState.softErrorOrNull();
            State state = Default.INSTANCE;
            int i = 0;
            this.mHasUnsentEvents = false;
            if (!this.mIsUnreadPreviewMode && !TextUtils.isEmpty(this.mEventId)) {
                state = Hidden.INSTANCE;
            } else if (resourceLimitExceededError != null) {
                state = new ResourceLimitExceededError(false, resourceLimitExceededError);
            } else if (softErrorOrNull != null) {
                state = new ResourceLimitExceededError(true, softErrorOrNull);
            } else if (!Matrix.getInstance(this).isConnected()) {
                state = ConnectionError.INSTANCE;
            } else if (this.mIsUnreadPreviewMode) {
                state = UnreadPreview.INSTANCE;
            } else {
                List undeliveredEvents = this.mSession.getDataHandler().getStore().getUndeliveredEvents(this.mRoom.getRoomId());
                List unknownDeviceEvents = this.mSession.getDataHandler().getStore().getUnknownDeviceEvents(this.mRoom.getRoomId());
                boolean z = undeliveredEvents != null && undeliveredEvents.size() > 0;
                boolean z2 = unknownDeviceEvents != null && unknownDeviceEvents.size() > 0;
                if (z) {
                    this.mHasUnsentEvents = true;
                    state = new UnsentEvents(z, z2);
                } else {
                    Boolean bool = this.mIsScrolledToTheBottom;
                    if (bool != null && !bool.booleanValue()) {
                        RoomSummary summary = this.mRoom.getDataHandler().getStore().getSummary(this.mRoom.getRoomId());
                        if (summary != null) {
                            i = this.mRoom.getDataHandler().getStore().eventsCountAfter(this.mRoom.getRoomId(), summary.getReadReceiptEventId());
                        }
                        state = new ScrollToBottom(i, this.mLatestTypingMessage);
                    } else if (!TextUtils.isEmpty(this.mLatestTypingMessage)) {
                        state = new Typing(this.mLatestTypingMessage);
                    } else if (this.mRoom.getState().isVersioned()) {
                        state = new Tombstone(this.mRoom.getState().getRoomTombstoneContent());
                    }
                }
            }
            this.mNotificationsArea.render(state);
        }
    }

    /* access modifiers changed from: private */
    public void refreshCallButtons(boolean z) {
        if (sRoomPreviewData == null && this.mEventId == null && this.mTchapUser == null && canSendMessages()) {
            int i = 0;
            boolean z2 = this.mRoom.canPerformCall() && this.mSession.isVoipCallSupported();
            IMXCall activeCall = CallsManager.getSharedInstance().getActiveCall();
            Widget activeWidget = this.mVectorOngoingConferenceCallView.getActiveWidget();
            if (activeCall == null && activeWidget == null) {
                View view = this.mStartCallLayout;
                if (!z2 || this.mEditText.getText().length() != 0) {
                    i = 8;
                }
                view.setVisibility(i);
                this.mStopCallLayout.setVisibility(8);
            } else if (activeWidget != null) {
                this.mStartCallLayout.setVisibility(8);
                this.mStopCallLayout.setVisibility(8);
            } else {
                IMXCall callWithRoomId = this.mSession.mCallsManager.getCallWithRoomId(this.mRoom.getRoomId());
                activeCall.removeListener(this.mCallListener);
                activeCall.addListener(this.mCallListener);
                this.mStartCallLayout.setVisibility(8);
                View view2 = this.mStopCallLayout;
                if (activeCall != callWithRoomId) {
                    i = 8;
                }
                view2.setVisibility(i);
            }
            if (z) {
                this.mVectorOngoingConferenceCallView.refresh();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onRoomTyping() {
        this.mLatestTypingMessage = null;
        Room room = this.mRoom;
        if (room != null) {
            List typingUsers = room.getTypingUsers();
            if (!typingUsers.isEmpty()) {
                String myUserId = this.mSession.getMyUserId();
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < typingUsers.size(); i++) {
                    RoomMember member = this.mRoom.getMember((String) typingUsers.get(i));
                    if (!(member == null || TextUtils.equals(myUserId, member.getUserId()) || member.displayname == null)) {
                        arrayList.add(member.displayname);
                    }
                }
                if (arrayList.isEmpty()) {
                    this.mLatestTypingMessage = null;
                } else if (arrayList.size() == 1) {
                    this.mLatestTypingMessage = getString(R.string.room_one_user_is_typing, new Object[]{arrayList.get(0)});
                } else if (arrayList.size() == 2) {
                    this.mLatestTypingMessage = getString(R.string.room_two_users_are_typing, new Object[]{arrayList.get(0), arrayList.get(1)});
                } else {
                    this.mLatestTypingMessage = getString(R.string.room_many_users_are_typing, new Object[]{arrayList.get(0), arrayList.get(1)});
                }
            }
            refreshNotificationsArea();
        }
    }

    /* access modifiers changed from: private */
    public void updateActionBarTitleAndTopic() {
        setTitle();
        setTopic();
    }

    /* access modifiers changed from: private */
    public void setTopic() {
        String str;
        Room room = this.mRoom;
        if (room != null) {
            str = room.isDirect() ? DinsicUtils.getDomainFromDisplayName(DinsicUtils.getRoomDisplayName(this, this.mRoom)) : getResources().getQuantityString(R.plurals.room_title_members, this.mRoom.getNumberOfJoinedMembers(), new Object[]{Integer.valueOf(this.mRoom.getNumberOfJoinedMembers())});
        } else {
            RoomPreviewData roomPreviewData = sRoomPreviewData;
            if (roomPreviewData == null || roomPreviewData.getRoomState() == null) {
                RoomPreviewData roomPreviewData2 = sRoomPreviewData;
                str = (roomPreviewData2 == null || roomPreviewData2.getPublicRoom() == null) ? null : sRoomPreviewData.getPublicRoom().topic;
            } else {
                str = sRoomPreviewData.getRoomState().topic;
            }
        }
        setTopic(str);
    }

    private void setTopic(String str) {
        if (!TextUtils.isEmpty(this.mEventId)) {
            this.mActionBarCustomTopic.setVisibility(8);
            return;
        }
        this.mActionBarCustomTopic.setText(str);
        if (TextUtils.isEmpty(str)) {
            this.mActionBarCustomTopic.setVisibility(8);
        } else {
            this.mActionBarCustomTopic.setVisibility(0);
        }
    }

    /* access modifiers changed from: private */
    public void updateRoomHeaderAvatar() {
        Room room = this.mRoom;
        if (room != null) {
            if (room.isDirect()) {
                this.mActionBarHeaderHexagonRoomAvatar.setVisibility(4);
                this.mActionBarHeaderRoomAvatar.setVisibility(0);
                VectorUtils.loadRoomAvatar((Context) this, this.mSession, this.mActionBarHeaderRoomAvatar, this.mRoom);
                return;
            }
            this.mActionBarHeaderHexagonRoomAvatar.setVisibility(0);
            this.mActionBarHeaderRoomAvatar.setVisibility(4);
            VectorUtils.loadRoomAvatar((Context) this, this.mSession, (ImageView) this.mActionBarHeaderHexagonRoomAvatar, this.mRoom);
            if (TextUtils.equals(DinsicUtils.getRoomAccessRule(this.mRoom), RoomAccessRulesKt.RESTRICTED)) {
                this.mActionBarHeaderHexagonRoomAvatar.setBorderSettings(ContextCompat.getColor(this, R.color.restricted_room_avatar_border_color), 3);
            } else {
                this.mActionBarHeaderHexagonRoomAvatar.setBorderSettings(ContextCompat.getColor(this, R.color.unrestricted_room_avatar_border_color), 10);
            }
        } else if (sRoomPreviewData != null) {
            this.mActionBarHeaderHexagonRoomAvatar.setVisibility(0);
            this.mActionBarHeaderRoomAvatar.setVisibility(4);
            String roomName = sRoomPreviewData.getRoomName();
            if (TextUtils.isEmpty(roomName)) {
                roomName = " ";
            }
            VectorUtils.loadUserAvatar(this, sRoomPreviewData.getSession(), this.mActionBarHeaderHexagonRoomAvatar, sRoomPreviewData.getRoomAvatarUrl(), sRoomPreviewData.getRoomId(), roomName);
            this.mActionBarHeaderHexagonRoomAvatar.setBorderSettings(ContextCompat.getColor(this, R.color.restricted_room_avatar_border_color), 3);
        } else if (this.mTchapUser != null) {
            this.mActionBarHeaderHexagonRoomAvatar.setVisibility(4);
            this.mActionBarHeaderRoomAvatar.setVisibility(0);
            VectorUtils.loadUserAvatar(this, this.mSession, this.mActionBarHeaderRoomAvatar, this.mTchapUser);
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296576})
    public void onTextsContainerClick() {
        Room room = this.mRoom;
        if (room == null) {
            return;
        }
        if (room.isDirect()) {
            launchDirectRoomDetails();
        } else {
            launchRoomDetails(0);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0095  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0099  */
    public void setTitle() {
        TextView textView;
        String str = this.mDefaultRoomName;
        if (this.mSession != null) {
            Room room = this.mRoom;
            if (room != null) {
                str = DinsicUtils.getRoomDisplayName(this, room);
                if (this.mRoom.isDirect()) {
                    str = DinsicUtils.getNameFromDisplayName(str);
                }
                if (TextUtils.isEmpty(str)) {
                    str = this.mDefaultRoomName;
                }
                if (!TextUtils.isEmpty(this.mEventId) && !this.mIsUnreadPreviewMode) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(getText(R.string.search));
                    sb.append(" : ");
                    sb.append(str);
                    str = sb.toString();
                }
                textView = this.mActionBarCustomTitle;
                if (textView == null) {
                    textView.setText(str);
                    return;
                } else {
                    setTitle(str);
                    return;
                }
            }
        }
        RoomPreviewData roomPreviewData = sRoomPreviewData;
        if (roomPreviewData == null) {
            User user = this.mTchapUser;
            if (user != null) {
                if (user.displayname != null) {
                    str = DinsicUtils.getNameFromDisplayName(this.mTchapUser.displayname);
                } else {
                    str = this.mTchapUser.user_id;
                }
            }
        } else if (roomPreviewData.getRoomState() == null || sRoomPreviewData.getRoomState().name == null || sRoomPreviewData.getRoomState().name.length() <= 0) {
            str = sRoomPreviewData.getRoomName();
        } else {
            str = sRoomPreviewData.getRoomState().name;
        }
        textView = this.mActionBarCustomTitle;
        if (textView == null) {
        }
    }

    private void updateRoomAccessInfo() {
        this.mActionBarRoomInfo.setVisibility(8);
        Room room = this.mRoom;
        if (room != null && TextUtils.equals(DinsicUtils.getRoomAccessRule(room), RoomAccessRulesKt.UNRESTRICTED)) {
            this.mActionBarRoomInfo.setText(getString(R.string.tchap_room_access_unrestricted));
            this.mActionBarRoomInfo.setVisibility(0);
        }
    }

    private void updateActionBarHeaderView() {
        updateRoomHeaderAvatar();
        setTitle();
        updateRoomAccessInfo();
    }

    private boolean canSendMessages() {
        Room room = this.mRoom;
        if (room != null) {
            return canSendMessages(room.getState());
        }
        return this.mTchapUser != null;
    }

    private boolean canSendMessages(RoomState roomState) {
        boolean z = !roomState.isVersioned();
        if (z) {
            PowerLevels powerLevels = roomState.getPowerLevels();
            z = powerLevels != null && powerLevels.maySendMessage(this.mMyUserId);
        }
        if (z) {
            return this.mSession.getDataHandler().getResourceLimitExceededError() == null;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public void checkSendEventStatus() {
        Room room = this.mRoom;
        if ((room != null && room.getState() != null) || this.mTchapUser != null) {
            if (canSendMessages()) {
                this.mBottomLayout.getLayoutParams().height = -2;
                this.mBottomSeparator.setVisibility(0);
                this.mSendingMessagesLayout.setVisibility(0);
                this.mCanNotPostTextView.setVisibility(8);
            } else if (this.mRoom.getState().isVersioned() || this.mSession.getDataHandler().getResourceLimitExceededError() != null) {
                this.mBottomSeparator.setVisibility(8);
                this.mBottomLayout.getLayoutParams().height = 0;
            } else {
                this.mBottomSeparator.setVisibility(8);
                this.mSendingMessagesLayout.setVisibility(8);
                this.mCanNotPostTextView.setVisibility(0);
            }
        }
    }

    public void dismissKeyboard() {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.mEditText.getWindowToken(), 0);
    }

    /* access modifiers changed from: private */
    public void enableActionBarHeader(boolean z) {
        updateActionBarHeaderView();
    }

    private void manageBannedHeader(RoomMember roomMember) {
        this.mRoomPreviewLayout.setVisibility(0);
        String str = "ban";
        if (TextUtils.equals(roomMember.membership, str)) {
            this.invitationTextView.setText(getString(R.string.has_been_banned, new Object[]{DinsicUtils.getRoomDisplayName(this, this.mRoom), this.mRoom.getState().getMemberName(roomMember.mSender)}));
        } else {
            this.invitationTextView.setText(getString(R.string.has_been_kicked, new Object[]{DinsicUtils.getRoomDisplayName(this, this.mRoom), this.mRoom.getState().getMemberName(roomMember.mSender)}));
        }
        if (!TextUtils.isEmpty(roomMember.reason)) {
            this.subInvitationTextView.setText(getString(R.string.reason_colon, new Object[]{roomMember.reason}));
        } else {
            this.subInvitationTextView.setText(null);
        }
        Button button = (Button) findViewById(R.id.button_join_room);
        if (TextUtils.equals(roomMember.membership, str)) {
            button.setVisibility(4);
        } else {
            button.setText(getString(R.string.rejoin));
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    VectorRoomActivity.this.showWaitingView();
                    VectorRoomActivity.this.mSession.joinRoom(VectorRoomActivity.this.mRoom.getRoomId(), new ApiCallback<String>() {
                        public void onSuccess(String str) {
                            VectorRoomActivity.this.hideWaitingView();
                            HashMap hashMap = new HashMap();
                            hashMap.put("MXCActionBarActivity.EXTRA_MATRIX_ID", VectorRoomActivity.this.mSession.getMyUserId());
                            hashMap.put("EXTRA_ROOM_ID", VectorRoomActivity.this.mRoom.getRoomId());
                            Intent intent = new Intent(VectorRoomActivity.this, VectorHomeActivity.class);
                            intent.setFlags(872415232);
                            intent.putExtra(VectorHomeActivity.EXTRA_JUMP_TO_ROOM_PARAMS, hashMap);
                            VectorRoomActivity.this.startActivity(intent);
                        }

                        private void onError(String str) {
                            String access$300 = VectorRoomActivity.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("re join failed ");
                            sb.append(str);
                            Log.d(access$300, sb.toString());
                            Toast.makeText(VectorRoomActivity.this, str, 0).show();
                            VectorRoomActivity.this.hideWaitingView();
                        }

                        public void onNetworkError(Exception exc) {
                            onError(exc.getLocalizedMessage());
                        }

                        public void onMatrixError(MatrixError matrixError) {
                            if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                                VectorRoomActivity.this.hideWaitingView();
                                VectorRoomActivity.this.getConsentNotGivenHelper().displayDialog(matrixError);
                                return;
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
        Button button2 = (Button) findViewById(R.id.button_decline);
        button2.setText(getString(R.string.forget_room));
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                VectorRoomActivity.this.mRoom.forget(new ApiCallback<Void>() {
                    public void onSuccess(Void voidR) {
                        VectorRoomActivity.this.finish();
                    }

                    private void onError(String str) {
                        String access$300 = VectorRoomActivity.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("forget failed ");
                        sb.append(str);
                        Log.d(access$300, sb.toString());
                        Toast.makeText(VectorRoomActivity.this, str, 0).show();
                        VectorRoomActivity.this.hideWaitingView();
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
        enableActionBarHeader(true);
    }

    public RoomPreviewData getRoomPreviewData() {
        return sRoomPreviewData;
    }

    private void manageRoomPreview() {
        if (sRoomPreviewData != null) {
            this.mRoomPreviewLayout.setVisibility(0);
            Button button = (Button) findViewById(R.id.button_join_room);
            Button button2 = (Button) findViewById(R.id.button_decline);
            RoomEmailInvitation roomEmailInvitation = sRoomPreviewData.getRoomEmailInvitation();
            boolean isEmpty = TextUtils.isEmpty(sRoomPreviewData.getRoomName());
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Preview the room ");
            sb.append(sRoomPreviewData.getRoomId());
            Log.d(str, sb.toString());
            if (this.mRoom != null) {
                Log.d(LOG_TAG, "manageRoomPreview : The room is known");
                String str2 = roomEmailInvitation != null ? roomEmailInvitation.inviterName : "";
                if (TextUtils.isEmpty(str2)) {
                    this.mRoom.getActiveMembersAsync(new SimpleApiCallback<List<RoomMember>>(this) {
                        public void onSuccess(List<RoomMember> list) {
                            String str = "";
                            for (RoomMember roomMember : list) {
                                if (TextUtils.equals(roomMember.membership, "join")) {
                                    str = TextUtils.isEmpty(roomMember.displayname) ? roomMember.getUserId() : roomMember.displayname;
                                }
                            }
                            VectorRoomActivity.this.invitationTextView.setText(VectorRoomActivity.this.getString(R.string.room_preview_invitation_format, new Object[]{str}));
                        }
                    });
                } else {
                    this.invitationTextView.setText(getString(R.string.room_preview_invitation_format, new Object[]{str2}));
                }
                button2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Log.d(VectorRoomActivity.LOG_TAG, "The user clicked on decline.");
                        VectorRoomActivity.this.showWaitingView();
                        VectorRoomActivity.this.mRoom.leave(new ApiCallback<Void>() {
                            public void onSuccess(Void voidR) {
                                Log.d(VectorRoomActivity.LOG_TAG, "The invitation is rejected");
                                VectorRoomActivity.this.onDeclined();
                            }

                            private void onError(String str) {
                                String access$300 = VectorRoomActivity.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("The invitation rejection failed ");
                                sb.append(str);
                                Log.d(access$300, sb.toString());
                                Toast.makeText(VectorRoomActivity.this, str, 0).show();
                                VectorRoomActivity.this.hideWaitingView();
                            }

                            public void onNetworkError(Exception exc) {
                                onError(exc.getLocalizedMessage());
                            }

                            public void onMatrixError(MatrixError matrixError) {
                                if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                                    VectorRoomActivity.this.hideWaitingView();
                                    VectorRoomActivity.this.getConsentNotGivenHelper().displayDialog(matrixError);
                                    return;
                                }
                                onError(matrixError.getLocalizedMessage());
                            }

                            public void onUnexpectedError(Exception exc) {
                                onError(exc.getLocalizedMessage());
                            }
                        });
                    }
                });
            } else {
                if (roomEmailInvitation == null || TextUtils.isEmpty(roomEmailInvitation.email)) {
                    String roomName = sRoomPreviewData.getRoomName();
                    if (!(sRoomPreviewData.getRoomState() == null || sRoomPreviewData.getRoomState().name == null || sRoomPreviewData.getRoomState().name.length() <= 0)) {
                        roomName = sRoomPreviewData.getRoomState().name;
                    }
                    TextView textView = this.invitationTextView;
                    Object[] objArr = new Object[1];
                    if (TextUtils.isEmpty(roomName)) {
                        roomName = getResources().getString(R.string.room_preview_try_join_an_unknown_room_default);
                    }
                    objArr[0] = roomName;
                    textView.setText(getString(R.string.room_preview_try_join_an_unknown_room, objArr));
                    if (!(sRoomPreviewData.getRoomResponse() == null || sRoomPreviewData.getRoomResponse().messages == null)) {
                        this.subInvitationTextView.setText(getString(R.string.room_preview_room_interactions_disabled));
                    }
                } else {
                    this.invitationTextView.setText(getString(R.string.room_preview_invitation_format, new Object[]{roomEmailInvitation.inviterName}));
                    this.subInvitationTextView.setText(getString(R.string.room_preview_unlinked_email_warning, new Object[]{roomEmailInvitation.email}));
                }
                button2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Log.d(VectorRoomActivity.LOG_TAG, "The invitation is declined (unknown room)");
                        VectorRoomActivity.sRoomPreviewData = null;
                        VectorRoomActivity.this.finish();
                    }
                });
            }
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Log.d(VectorRoomActivity.LOG_TAG, "The user clicked on Join.");
                    if (VectorRoomActivity.sRoomPreviewData != null) {
                        VectorRoomActivity.this.showWaitingView();
                        DinsicUtils.joinRoom(VectorRoomActivity.sRoomPreviewData, new ApiCallback<Void>() {
                            public void onSuccess(Void voidR) {
                                VectorRoomActivity.this.hideWaitingView();
                                VectorRoomActivity.this.onJoined();
                            }

                            private void onError(String str) {
                                Toast.makeText(VectorRoomActivity.this, str, 0).show();
                                VectorRoomActivity.this.hideWaitingView();
                            }

                            public void onNetworkError(Exception exc) {
                                onError(exc.getLocalizedMessage());
                            }

                            public void onMatrixError(MatrixError matrixError) {
                                if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                                    VectorRoomActivity.this.hideWaitingView();
                                    VectorRoomActivity.this.getConsentNotGivenHelper().displayDialog(matrixError);
                                    return;
                                }
                                onError(matrixError.getLocalizedMessage());
                            }

                            public void onUnexpectedError(Exception exc) {
                                onError(exc.getLocalizedMessage());
                            }
                        });
                        return;
                    }
                    VectorRoomActivity.this.finish();
                }
            });
            enableActionBarHeader(true);
            return;
        }
        this.mRoomPreviewLayout.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void onDeclined() {
        if (sRoomPreviewData != null) {
            finish();
            sRoomPreviewData = null;
        }
    }

    /* access modifiers changed from: private */
    public void onJoined() {
        RoomPreviewData roomPreviewData = sRoomPreviewData;
        if (roomPreviewData != null) {
            DinsicUtils.onNewJoinedRoom(this, roomPreviewData);
            sRoomPreviewData = null;
        }
    }

    private void onActivityResultRoomInvite(Intent intent) {
        List list = (List) intent.getSerializableExtra(VectorRoomInviteMembersActivity.EXTRA_OUT_SELECTED_USER_IDS);
        if (this.mRoom != null && list != null && list.size() > 0) {
            showWaitingView();
            this.mRoom.invite(list, (ApiCallback<Void>) new ApiCallback<Void>() {
                private void onDone(String str) {
                    if (!TextUtils.isEmpty(str)) {
                        Toast.makeText(VectorRoomActivity.this, str, 0).show();
                    }
                    VectorRoomActivity.this.hideWaitingView();
                }

                public void onSuccess(Void voidR) {
                    onDone(null);
                }

                public void onNetworkError(Exception exc) {
                    onDone(exc.getMessage());
                }

                public void onMatrixError(MatrixError matrixError) {
                    onDone(matrixError.getMessage());
                }

                public void onUnexpectedError(Exception exc) {
                    onDone(exc.getMessage());
                }
            });
        }
    }

    private void onActivityResultRoomAvatarUpdate(Intent intent) {
        MXSession mXSession = this.mSession;
        if (mXSession != null && this.mRoom != null) {
            Uri thumbnailUriFromIntent = VectorUtils.getThumbnailUriFromIntent(this, intent, mXSession.getMediaCache());
            if (thumbnailUriFromIntent != null) {
                showWaitingView();
                Resource openResource = ResourceUtils.openResource(this, thumbnailUriFromIntent, null);
                if (openResource != null) {
                    this.mSession.getMediaCache().uploadContent(openResource.mContentStream, null, openResource.mMimeType, null, new MXMediaUploadListener() {
                        public void onUploadError(String str, int i, String str2) {
                            Log.e(VectorRoomActivity.LOG_TAG, "Fail to upload the avatar");
                        }

                        public void onUploadComplete(String str, final String str2) {
                            VectorRoomActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Log.d(VectorRoomActivity.LOG_TAG, "The avatar has been uploaded, update the room avatar");
                                    VectorRoomActivity.this.mRoom.updateAvatarUrl(str2, new ApiCallback<Void>() {
                                        private void onDone(String str) {
                                            if (!TextUtils.isEmpty(str)) {
                                                Toast.makeText(VectorRoomActivity.this, str, 0).show();
                                            }
                                            VectorRoomActivity.this.hideWaitingView();
                                            VectorRoomActivity.this.updateRoomHeaderAvatar();
                                        }

                                        public void onSuccess(Void voidR) {
                                            onDone(null);
                                        }

                                        public void onNetworkError(Exception exc) {
                                            onDone(exc.getLocalizedMessage());
                                        }

                                        public void onMatrixError(MatrixError matrixError) {
                                            onDone(matrixError.getLocalizedMessage());
                                        }

                                        public void onUnexpectedError(Exception exc) {
                                            onDone(exc.getLocalizedMessage());
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        }
    }

    private void onRoomTitleClick() {
        if (this.mRoom != null) {
            View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_base_edit_text, null);
            Builder builder = new Builder(this);
            builder.setTitle((int) R.string.room_info_room_name).setView(inflate);
            final EditText editText = (EditText) inflate.findViewById(R.id.edit_text);
            editText.setText(this.mRoom.getState().name);
            builder.setCancelable(false).setPositiveButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    VectorRoomActivity.this.showWaitingView();
                    VectorRoomActivity.this.mRoom.updateName(editText.getText().toString(), new ApiCallback<Void>() {
                        private void onDone(String str) {
                            if (!TextUtils.isEmpty(str)) {
                                Toast.makeText(VectorRoomActivity.this, str, 0).show();
                            }
                            VectorRoomActivity.this.hideWaitingView();
                            VectorRoomActivity.this.updateActionBarTitleAndTopic();
                        }

                        public void onSuccess(Void voidR) {
                            onDone(null);
                        }

                        public void onNetworkError(Exception exc) {
                            onDone(exc.getLocalizedMessage());
                        }

                        public void onMatrixError(MatrixError matrixError) {
                            onDone(matrixError.getLocalizedMessage());
                        }

                        public void onUnexpectedError(Exception exc) {
                            onDone(exc.getLocalizedMessage());
                        }
                    });
                }
            }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
        }
    }

    private void onRoomTopicClick() {
        if (this.mRoom != null) {
            View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_base_edit_text, null);
            Builder builder = new Builder(this);
            builder.setTitle((int) R.string.room_info_room_topic).setView(inflate);
            final EditText editText = (EditText) inflate.findViewById(R.id.edit_text);
            editText.setText(this.mRoom.getState().topic);
            builder.setCancelable(false).setPositiveButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    VectorRoomActivity.this.showWaitingView();
                    VectorRoomActivity.this.mRoom.updateTopic(editText.getText().toString(), new ApiCallback<Void>() {
                        private void onDone(String str) {
                            if (!TextUtils.isEmpty(str)) {
                                Toast.makeText(VectorRoomActivity.this, str, 0).show();
                            }
                            VectorRoomActivity.this.hideWaitingView();
                            VectorRoomActivity.this.updateActionBarTitleAndTopic();
                        }

                        public void onSuccess(Void voidR) {
                            onDone(null);
                        }

                        public void onNetworkError(Exception exc) {
                            onDone(exc.getLocalizedMessage());
                        }

                        public void onMatrixError(MatrixError matrixError) {
                            onDone(matrixError.getLocalizedMessage());
                        }

                        public void onUnexpectedError(Exception exc) {
                            onDone(exc.getLocalizedMessage());
                        }
                    });
                }
            }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
        }
    }

    private void displayE2eRoomAlert() {
        if (!isFinishing()) {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String str = E2E_WARNINGS_PREFERENCES;
            if (!defaultSharedPreferences.contains(str)) {
                Room room = this.mRoom;
                if (room != null && room.isEncrypted()) {
                    defaultSharedPreferences.edit().putBoolean(str, false).apply();
                    new Builder(this).setTitle((int) R.string.room_e2e_alert_title).setMessage((int) R.string.room_e2e_alert_message).setPositiveButton((int) R.string.ok, (OnClickListener) null).show();
                }
            }
        }
    }

    public void onMemberClicked(String str) {
        if (this.mRoom != null) {
            Intent intent = new Intent(this, VectorMemberDetailsActivity.class);
            intent.putExtra("EXTRA_ROOM_ID", this.mRoom.getRoomId());
            intent.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_ID, str);
            intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getCredentials().userId);
            startActivityForResult(intent, 2);
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296456})
    public void onEditTextClick() {
        enableActionBarHeader(false);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296970})
    public void onSendClick() {
        tchapSendTextMessage();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296922})
    public void onSendFileClick() {
        enableActionBarHeader(false);
        final ArrayList arrayList = new ArrayList();
        arrayList.add(SendFile.INSTANCE);
        if (PreferencesManager.isSendVoiceFeatureEnabled(this)) {
            arrayList.add(SendVoice.INSTANCE);
        }
        arrayList.add(TakePhoto.INSTANCE);
        arrayList.add(TakeVideo.INSTANCE);
        new Builder(this).setAdapter(new DialogSendItemAdapter(this, arrayList), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                VectorRoomActivity.this.onSendChoiceClicked((DialogListItem) arrayList.get(i));
            }
        }).show();
    }

    /* access modifiers changed from: private */
    public void onSendChoiceClicked(DialogListItem dialogListItem) {
        if (dialogListItem instanceof SendFile) {
            launchFileSelectionIntent();
        } else if (dialogListItem instanceof SendVoice) {
            launchAudioRecorderIntent();
        } else if (dialogListItem instanceof SendSticker) {
            startStickerPickerActivity();
        } else if (dialogListItem instanceof TakePhoto) {
            if (PermissionsToolsKt.checkPermissions(3, (Activity) this, (int) PermissionsToolsKt.PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_CAMERA)) {
                launchNativeCamera();
            }
        } else if ((dialogListItem instanceof TakeVideo) && PermissionsToolsKt.checkPermissions(3, (Activity) this, (int) PermissionsToolsKt.PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_VIDEO_CAMERA)) {
            launchNativeVideoRecorder();
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296961})
    public void onPendingCallClick() {
        IMXCall activeCall = CallsManager.getSharedInstance().getActiveCall();
        if (activeCall != null) {
            Intent intent = new Intent(this, VectorCallViewActivity.class);
            intent.putExtra(VectorCallViewActivity.EXTRA_MATRIX_ID, activeCall.getSession().getCredentials().userId);
            intent.putExtra(VectorCallViewActivity.EXTRA_CALL_ID, activeCall.getCallId());
            startActivity(intent);
            return;
        }
        this.mVectorPendingCallView.onCallTerminated();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296971})
    public void onSendingMessageLayoutClick() {
        if (this.mEditText.requestFocus()) {
            ((InputMethodManager) getSystemService("input_method")).showSoftInput(this.mEditText, 1);
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296972})
    public void onStartCallClick() {
        Room room = this.mRoom;
        if (room != null && room.isEncrypted() && this.mRoom.getNumberOfMembers() > 2) {
            new Builder(this).setMessage((int) R.string.room_no_conference_call_in_encrypted_rooms).setIcon(17301543).setPositiveButton((int) R.string.ok, (OnClickListener) null).show();
        } else if (!isUserAllowedToStartConfCall()) {
            displayConfCallNotAllowed();
        } else if (this.mRoom.getNumberOfMembers() > 2) {
            new Builder(this).setTitle((int) R.string.conference_call_warning_title).setMessage((int) R.string.conference_call_warning_message).setIcon(17301543).setPositiveButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (PreferencesManager.useJitsiConfCall(VectorRoomActivity.this)) {
                        VectorRoomActivity.this.startJitsiCall(true);
                    } else {
                        VectorRoomActivity.this.displayVideoCallIpDialog();
                    }
                }
            }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
        } else {
            displayVideoCallIpDialog();
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296946})
    public void onStopCallClick() {
        CallsManager.getSharedInstance().onHangUp(null);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296930})
    public void onMarginRightClick() {
        if (this.mStopCallLayout.getVisibility() == 0) {
            this.mStopCallLayout.performClick();
        } else if (this.mStartCallLayout.getVisibility() == 0) {
            this.mStartCallLayout.performClick();
        } else if (this.mSendMessageView.getVisibility() == 0) {
            this.mSendMessageView.performClick();
        }
    }
}
