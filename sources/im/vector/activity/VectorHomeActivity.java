package im.vector.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import butterknife.BindView;
import butterknife.OnClick;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionsMenu.OnFloatingActionsMenuUpdateListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayout.Tab;
import com.google.android.material.textfield.TextInputEditText;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.activity.TchapPublicRoomSelectionActivity;
import fr.gouv.tchap.activity.TchapRoomCreationActivity;
import fr.gouv.tchap.fragments.TchapContactFragment;
import fr.gouv.tchap.fragments.TchapRoomsFragment;
import fr.gouv.tchap.util.DinsicUtils;
import fr.gouv.tchap.util.LiveSecurityChecks;
import fr.gouv.tchap.version.TchapVersionCheckActivity;
import fr.gouv.tchap.version.VersionCheckResult;
import fr.gouv.tchap.version.VersionCheckResult.ShowUpgradeScreen;
import fr.gouv.tchap.version.VersionChecker;
import im.vector.Matrix;
import im.vector.MyPresenceManager;
import im.vector.VectorApp;
import im.vector.activity.VectorRoomInviteMembersActivity.ActionMode;
import im.vector.activity.VectorRoomInviteMembersActivity.ContactsFilter;
import im.vector.activity.util.RequestCodesKt;
import im.vector.fragments.AbsHomeFragment;
import im.vector.push.PushManager;
import im.vector.push.PushManager.NotificationPrivacy;
import im.vector.receiver.VectorUniversalLinkReceiver;
import im.vector.services.EventStreamService;
import im.vector.ui.themes.ActivityOtherThemes;
import im.vector.ui.themes.ActivityOtherThemes.Home;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.BugReporter;
import im.vector.util.CallsManager;
import im.vector.util.HomeRoomsViewModel;
import im.vector.util.HomeRoomsViewModel.Result;
import im.vector.util.PreferencesManager;
import im.vector.util.RoomUtils;
import im.vector.util.SystemUtilsKt;
import im.vector.util.VectorUtils;
import im.vector.view.UnreadCounterBadgeView;
import im.vector.view.VectorPendingCallView;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.HelpFormatter;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.call.IMXCall;
import org.matrix.androidsdk.core.BingRulesManager;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXPatterns;
import org.matrix.androidsdk.core.PermalinkUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.callback.SuccessCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.data.MyUser;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomPreviewData;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.RoomSummary;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.pid.ThirdPartyIdentifier;

public class VectorHomeActivity extends VectorAppCompatActivity implements OnQueryTextListener {
    public static final String BROADCAST_ACTION_STOP_WAITING_VIEW = "im.vector.activity.ACTION_STOP_WAITING_VIEW";
    private static final String CURRENT_MENU_ID = "CURRENT_MENU_ID";
    public static final String EXTRA_CALL_ID = "VectorHomeActivity.EXTRA_CALL_ID";
    public static final String EXTRA_CALL_SESSION_ID = "VectorHomeActivity.EXTRA_CALL_SESSION_ID";
    public static final String EXTRA_CALL_UNKNOWN_DEVICES = "VectorHomeActivity.EXTRA_CALL_UNKNOWN_DEVICES";
    public static final String EXTRA_GROUP_ID = "VectorHomeActivity.EXTRA_GROUP_ID";
    public static final String EXTRA_JUMP_TO_ROOM_PARAMS = "VectorHomeActivity.EXTRA_JUMP_TO_ROOM_PARAMS";
    public static final String EXTRA_JUMP_TO_UNIVERSAL_LINK = "VectorHomeActivity.EXTRA_JUMP_TO_UNIVERSAL_LINK";
    public static final String EXTRA_MEMBER_ID = "VectorHomeActivity.EXTRA_MEMBER_ID";
    public static final String EXTRA_SHARED_INTENT_PARAMS = "VectorHomeActivity.EXTRA_SHARED_INTENT_PARAMS";
    public static final String EXTRA_WAITING_VIEW_STATUS = "VectorHomeActivity.EXTRA_WAITING_VIEW_STATUS";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorHomeActivity.class.getSimpleName();
    private static final String NO_DEVICE_ID_WARNING_KEY = "NO_DEVICE_ID_WARNING_KEY";
    private static final int TAB_POSITION_CONTACT = 1;
    private static final int TAB_POSITION_CONVERSATION = 0;
    private static final String TAG_FRAGMENT_FAVOURITES = "TAG_FRAGMENT_FAVOURITES";
    private static final String TAG_FRAGMENT_GROUPS = "TAG_FRAGMENT_GROUPS";
    private static final String TAG_FRAGMENT_HOME = "TAG_FRAGMENT_HOME";
    private static final String TAG_FRAGMENT_PEOPLE = "TAG_FRAGMENT_PEOPLE";
    private static final String TAG_FRAGMENT_ROOMS = "TAG_FRAGMENT_ROOMS";
    public static final boolean WAITING_VIEW_START = true;
    private static final boolean WAITING_VIEW_STOP = false;
    private static VectorHomeActivity sharedInstance = null;
    private Map<String, Object> mAutomaticallyOpenedRoomParams = null;
    private final MXEventListener mBadgeEventsListener = new MXEventListener() {
        private boolean mRefreshBadgeOnChunkEnd = false;

        public void onLiveEventsChunkProcessed(String str, String str2) {
            if (this.mRefreshBadgeOnChunkEnd) {
                VectorHomeActivity.this.refreshUnreadBadges();
                this.mRefreshBadgeOnChunkEnd = false;
            }
        }

        public void onLiveEvent(Event event, RoomState roomState) {
            String type = event.getType();
            this.mRefreshBadgeOnChunkEnd = ((event.roomId != null && RoomSummary.isSupportedEvent(event)) || "m.room.member".equals(type) || Event.EVENT_TYPE_REDACTION.equals(type) || Event.EVENT_TYPE_TAGS.equals(type) || Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE.equals(type)) | this.mRefreshBadgeOnChunkEnd;
        }

        public void onReceiptEvent(String str, List<String> list) {
            this.mRefreshBadgeOnChunkEnd |= list.indexOf(VectorHomeActivity.this.mSession.getCredentials().userId) >= 0;
        }

        public void onLeaveRoom(String str) {
            this.mRefreshBadgeOnChunkEnd = true;
        }

        public void onNewRoom(String str) {
            this.mRefreshBadgeOnChunkEnd = true;
        }

        public void onJoinRoom(String str) {
            this.mRefreshBadgeOnChunkEnd = true;
        }

        public void onDirectMessageChatRoomsListUpdate() {
            this.mRefreshBadgeOnChunkEnd = true;
        }

        public void onRoomTagEvent(String str) {
            this.mRefreshBadgeOnChunkEnd = true;
        }
    };
    private final Map<Integer, UnreadCounterBadgeView> mBadgeViewByIndex = new HashMap();
    private final BroadcastReceiver mBrdRcvStopWaitingView = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            VectorHomeActivity.this.hideWaitingView();
        }
    };
    private String mCurrentFragmentTag;
    /* access modifiers changed from: private */
    public int mCurrentMenuId = -1;
    @BindView(2131296453)
    DrawerLayout mDrawerLayout;
    private MXEventListener mEventsListener;
    @BindView(2131296348)
    FloatingActionButton mFabCreateRoom;
    @BindView(2131296350)
    FloatingActionButton mFabJoinRoom;
    @BindView(2131296487)
    AddFloatingActionButton mFabMain;
    @BindView(2131296351)
    FloatingActionButton mFabStartChat;
    @BindView(2131296512)
    FloatingActionsMenu mFloatingActionsMenu;
    private FragmentManager mFragmentManager;
    private String mGroupIdToOpen = null;
    private String mMemberIdToOpen = null;
    private HomeRoomsViewModel mRoomsViewModel;
    @BindView(2131297008)
    SearchView mSearchView;
    /* access modifiers changed from: private */
    public MXSession mSession;
    /* access modifiers changed from: private */
    public Intent mSharedFilesIntent = null;
    /* access modifiers changed from: private */
    public Runnable mShowFloatingActionButtonRunnable;
    /* access modifiers changed from: private */
    public int mSlidingMenuIndex = -1;
    @BindView(2131296588)
    ProgressBar mSyncInProgressView;
    @BindView(2131296589)
    Toolbar mToolbar;
    @BindView(2131297079)
    TabLayout mTopNavigationView;
    private final VectorUniversalLinkReceiver mUniversalLinkReceiver = new VectorUniversalLinkReceiver();
    /* access modifiers changed from: private */
    public Uri mUniversalLinkToOpen = null;
    @BindView(2131296685)
    VectorPendingCallView mVectorPendingCallView;
    @BindView(2131296828)
    NavigationView navigationView;
    private LiveSecurityChecks securityChecks = new LiveSecurityChecks(this);
    @BindView(2131296513)
    View touchGuard;

    public int getLayoutRes() {
        return R.layout.activity_home;
    }

    public int getMenuRes() {
        return -1;
    }

    public boolean onQueryTextSubmit(String str) {
        return true;
    }

    public static VectorHomeActivity getInstance() {
        return sharedInstance;
    }

    public ActivityOtherThemes getOtherThemes() {
        return Home.INSTANCE;
    }

    public void initUiAndData() {
        this.mFragmentManager = getSupportFragmentManager();
        if (CommonActivityUtils.shouldRestartApp(this)) {
            Log.e(LOG_TAG, "Restart the application.");
            CommonActivityUtils.restartApp(this);
        } else if (CommonActivityUtils.isGoingToSplash(this)) {
            Log.d(LOG_TAG, "onCreate : Going to splash screen");
        } else {
            setWaitingView(findViewById(R.id.listView_spinner_views));
            sharedInstance = this;
            setupNavigation();
            initSlidingMenu();
            this.mSession = Matrix.getInstance(this).getDefaultSession();
            this.mRoomsViewModel = new HomeRoomsViewModel(this.mSession);
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String str = PreferencesManager.VERSION_BUILD;
            int i = defaultSharedPreferences.getInt(str, 0);
            if (i != 58) {
                defaultSharedPreferences.edit().putInt(str, 58).apply();
                if (i != 0) {
                    String str2 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("The application has been updated from version ");
                    sb.append(i);
                    sb.append(" to version ");
                    sb.append(58);
                    Log.d(str2, sb.toString());
                    if (i < 54) {
                        Matrix.getInstance(this).reloadSessions(this);
                        return;
                    }
                }
            }
            Intent intent = getIntent();
            boolean isFirstCreation = isFirstCreation();
            String str3 = EXTRA_GROUP_ID;
            String str4 = EXTRA_MEMBER_ID;
            String str5 = EXTRA_JUMP_TO_ROOM_PARAMS;
            String str6 = EXTRA_JUMP_TO_UNIVERSAL_LINK;
            String str7 = EXTRA_WAITING_VIEW_STATUS;
            String str8 = EXTRA_CALL_UNKNOWN_DEVICES;
            String str9 = EXTRA_CALL_ID;
            String str10 = EXTRA_SHARED_INTENT_PARAMS;
            String str11 = EXTRA_CALL_SESSION_ID;
            String str12 = VectorUniversalLinkReceiver.EXTRA_UNIVERSAL_LINK_URI;
            if (!isFirstCreation) {
                intent.removeExtra(str10);
                intent.removeExtra(str11);
                intent.removeExtra(str9);
                intent.removeExtra(str8);
                intent.removeExtra(str7);
                intent.removeExtra(str6);
                intent.removeExtra(str5);
                intent.removeExtra(str4);
                intent.removeExtra(str3);
                intent.removeExtra(str12);
            } else {
                if (intent.hasExtra(str11) && intent.hasExtra(str9)) {
                    startCall(intent.getStringExtra(str11), intent.getStringExtra(str9), (MXUsersDevicesMap) intent.getSerializableExtra(str8));
                    intent.removeExtra(str11);
                    intent.removeExtra(str9);
                    intent.removeExtra(str8);
                }
                if (intent.getBooleanExtra(str7, false)) {
                    showWaitingView();
                } else {
                    hideWaitingView();
                }
                intent.removeExtra(str7);
                this.mAutomaticallyOpenedRoomParams = (Map) intent.getSerializableExtra(str5);
                intent.removeExtra(str5);
                this.mUniversalLinkToOpen = (Uri) intent.getParcelableExtra(str6);
                intent.removeExtra(str6);
                this.mMemberIdToOpen = intent.getStringExtra(str4);
                intent.removeExtra(str4);
                this.mGroupIdToOpen = intent.getStringExtra(str3);
                intent.removeExtra(str3);
                if (intent.hasExtra(str12)) {
                    Log.d(LOG_TAG, "Has an universal link");
                    final Uri uri = (Uri) intent.getParcelableExtra(str12);
                    intent.removeExtra(str12);
                    Map parseUniversalLink = VectorUniversalLinkReceiver.parseUniversalLink(uri);
                    if (parseUniversalLink != null) {
                        String str13 = PermalinkUtils.ULINK_ROOM_ID_OR_ALIAS_KEY;
                        if (parseUniversalLink.containsKey(str13)) {
                            Log.d(LOG_TAG, "Has a valid universal link");
                            String str14 = (String) parseUniversalLink.get(str13);
                            if (MXPatterns.isRoomId(str14)) {
                                String str15 = LOG_TAG;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("Has a valid universal link to the room ID ");
                                sb2.append(str14);
                                Log.d(str15, sb2.toString());
                                if (this.mSession.getDataHandler().getRoom(str14, false) != null) {
                                    Log.d(LOG_TAG, "Has a valid universal link to a known room");
                                    this.mUniversalLinkToOpen = uri;
                                } else {
                                    Log.d(LOG_TAG, "Has a valid universal link but the room is not yet known");
                                    intent.putExtra(str12, uri);
                                }
                            } else if (MXPatterns.isRoomAlias(str14)) {
                                String str16 = LOG_TAG;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("Has a valid universal link of the room Alias ");
                                sb3.append(str14);
                                Log.d(str16, sb3.toString());
                                showWaitingView();
                                this.mSession.getDataHandler().roomIdByAlias(str14, new SimpleApiCallback<String>() {
                                    public void onSuccess(String str) {
                                        String access$000 = VectorHomeActivity.LOG_TAG;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("Retrieve the room ID ");
                                        sb.append(str);
                                        Log.d(access$000, sb.toString());
                                        VectorHomeActivity.this.getIntent().putExtra(VectorUniversalLinkReceiver.EXTRA_UNIVERSAL_LINK_URI, uri);
                                        if (VectorHomeActivity.this.mSession.getDataHandler().getRoom(str, false) != null) {
                                            Log.d(VectorHomeActivity.LOG_TAG, "Find the room from room ID : process it");
                                            VectorHomeActivity.this.processIntentUniversalLink();
                                            return;
                                        }
                                        Log.d(VectorHomeActivity.LOG_TAG, "Don't know the room");
                                    }
                                });
                            }
                        }
                    }
                } else {
                    Log.d(LOG_TAG, "create with no universal link");
                }
                if (intent.hasExtra(str10)) {
                    final Intent intent2 = (Intent) intent.getParcelableExtra(str10);
                    Log.d(LOG_TAG, "Has shared intent");
                    if (this.mSession.getDataHandler().getStore().isReady()) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Log.d(VectorHomeActivity.LOG_TAG, "shared intent : The store is ready -> display sendFilesTo");
                                CommonActivityUtils.sendFilesTo(VectorHomeActivity.this, intent2);
                            }
                        });
                    } else {
                        Log.d(LOG_TAG, "shared intent : Wait that the store is ready");
                        this.mSharedFilesIntent = intent2;
                    }
                    intent.removeExtra(str10);
                }
            }
            if (Matrix.getMXSessions(this).size() == 0) {
                Log.e(LOG_TAG, "Weird : onCreate : no session");
                if (Matrix.getInstance(this).getDefaultSession() != null) {
                    Log.e(LOG_TAG, "No loaded session : reload them");
                    startActivity(new Intent(this, SplashActivity.class));
                    finish();
                    return;
                }
            }
            Tab tabAt = this.mTopNavigationView.getTabAt((isFirstCreation() || getSavedInstanceState().getInt(CURRENT_MENU_ID, 0) == 0) ? 0 : 1);
            if (tabAt != null) {
                updateSelectedFragment(tabAt, false);
            }
            initViews();
        }
    }

    /* access modifiers changed from: private */
    public void showFloatingActionMenuIfRequired() {
        if (DinsicUtils.isExternalTchapSession(this.mSession)) {
            concealFloatingActionMenu();
        } else {
            revealFloatingActionMenu();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mUniversalLinkReceiver, new IntentFilter(VectorUniversalLinkReceiver.BROADCAST_ACTION_UNIVERSAL_LINK_RESUME));
        this.securityChecks.checkOnActivityStart();
        applyScreenshotSecurity();
        VersionChecker.INSTANCE.checkVersion(this, new SuccessCallback<VersionCheckResult>() {
            public void onSuccess(VersionCheckResult versionCheckResult) {
                if (versionCheckResult instanceof ShowUpgradeScreen) {
                    VectorHomeActivity vectorHomeActivity = VectorHomeActivity.this;
                    vectorHomeActivity.startActivity(new Intent(vectorHomeActivity, TchapVersionCheckActivity.class));
                    VectorHomeActivity.this.finish();
                }
            }
        });
        MyPresenceManager.createPresenceManager(this, Matrix.getInstance(this).getSessions());
        MyPresenceManager.advertiseAllOnline();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mBrdRcvStopWaitingView, new IntentFilter(BROADCAST_ACTION_STOP_WAITING_VIEW));
        Intent intent = getIntent();
        Map<String, Object> map = this.mAutomaticallyOpenedRoomParams;
        if (map != null) {
            CommonActivityUtils.goToRoomPage(this, this.mSession, map);
            this.mAutomaticallyOpenedRoomParams = null;
        }
        Uri uri = this.mUniversalLinkToOpen;
        if (uri != null) {
            intent.putExtra(VectorUniversalLinkReceiver.EXTRA_UNIVERSAL_LINK_URI, uri);
            new Handler(getMainLooper()).postDelayed(new Runnable() {
                public void run() {
                    VectorHomeActivity.this.processIntentUniversalLink();
                    VectorHomeActivity.this.mUniversalLinkToOpen = null;
                }
            }, 100);
        }
        if (this.mSession.isAlive()) {
            addEventsListener();
        }
        showFloatingActionMenuIfRequired();
        refreshSlidingMenu();
        this.mVectorPendingCallView.checkPendingCall();
        if (VectorApp.getInstance() != null && VectorApp.getInstance().didAppCrash()) {
            try {
                new Builder(this).setMessage((int) R.string.send_bug_report_app_crashed).setPositiveButton((int) R.string.yes, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BugReporter.sendBugReport();
                    }
                }).setNegativeButton((int) R.string.no, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BugReporter.deleteCrashFile(VectorHomeActivity.this);
                    }
                }).show();
                VectorApp.getInstance().clearAppCrashStatus();
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## onResume() : appCrashedAlert failed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
        String str2 = "MXCActionBarActivity.EXTRA_MATRIX_ID";
        if (this.mMemberIdToOpen != null) {
            Intent intent2 = new Intent(this, VectorMemberDetailsActivity.class);
            intent2.putExtra(VectorMemberDetailsActivity.EXTRA_MEMBER_ID, this.mMemberIdToOpen);
            intent2.putExtra(str2, this.mSession.getCredentials().userId);
            startActivity(intent2);
            this.mMemberIdToOpen = null;
        }
        if (this.mGroupIdToOpen != null) {
            Intent intent3 = new Intent(this, VectorGroupDetailsActivity.class);
            intent3.putExtra(VectorGroupDetailsActivity.EXTRA_GROUP_ID, this.mGroupIdToOpen);
            intent3.putExtra(str2, this.mSession.getCredentials().userId);
            startActivity(intent3);
            this.mGroupIdToOpen = null;
        }
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.vctr_riot_primary_background_color, typedValue, true);
        this.mToolbar.setBackgroundResource(typedValue.resourceId);
        checkDeviceId();
        this.mSyncInProgressView.setVisibility(VectorApp.isSessionSyncing(this.mSession) ? 0 : 8);
        displayCryptoCorruption();
        addBadgeEventsListener();
        checkNotificationPrivacySetting();
        setSelectedTabStyle();
        TabLayout tabLayout = this.mTopNavigationView;
        updateSelectedFragment(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()), false);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 14000) {
            Matrix.getInstance(this).getPushManager().setNotificationPrivacy(NotificationPrivacy.NORMAL, null);
        }
    }

    /* access modifiers changed from: private */
    public void setSelectedTabStyle() {
        for (int i = 0; i < this.mTopNavigationView.getTabCount(); i++) {
            TextView textView = (TextView) ((LinearLayout) this.mTopNavigationView.getTabAt(i).getCustomView()).getChildAt(0);
            if (textView != null) {
                if (i == this.mTopNavigationView.getSelectedTabPosition()) {
                    textView.setTypeface(null, 1);
                } else {
                    textView.setTypeface(null, 0);
                }
            }
        }
    }

    private void checkNotificationPrivacySetting() {
        if (VERSION.SDK_INT >= 23) {
            PushManager pushManager = Matrix.getInstance(this).getPushManager();
            if (pushManager.useFcm() && !PreferencesManager.didAskUserToIgnoreBatteryOptimizations(this)) {
                PreferencesManager.setDidAskUserToIgnoreBatteryOptimizations(this);
                if (SystemUtilsKt.isIgnoringBatteryOptimizations(this)) {
                    pushManager.setNotificationPrivacy(NotificationPrivacy.NORMAL, null);
                } else {
                    pushManager.setNotificationPrivacy(NotificationPrivacy.LOW_DETAIL, null);
                    new Builder(this).setCancelable(false).setTitle((int) R.string.startup_notification_privacy_title).setMessage((int) R.string.startup_notification_privacy_message).setPositiveButton((int) R.string.startup_notification_privacy_button_grant, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d(VectorHomeActivity.LOG_TAG, "checkNotificationPrivacySetting: user wants to grant the IgnoreBatteryOptimizations permission");
                            SystemUtilsKt.requestDisablingBatteryOptimization(VectorHomeActivity.this, RequestCodesKt.BATTERY_OPTIMIZATION_REQUEST_CODE);
                        }
                    }).setNegativeButton((int) R.string.startup_notification_privacy_button_other, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d(VectorHomeActivity.LOG_TAG, "checkNotificationPrivacySetting: user opens notification policy setting screen");
                            VectorHomeActivity vectorHomeActivity = VectorHomeActivity.this;
                            vectorHomeActivity.startActivity(NotificationPrivacyActivity.getIntent(vectorHomeActivity));
                        }
                    }).show();
                }
            }
        }
    }

    private void promptForAnalyticsTracking() {
        new Builder(this).setMessage((int) R.string.settings_opt_in_of_analytics_prompt).setPositiveButton((int) R.string.settings_opt_in_of_analytics_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                VectorHomeActivity.this.setAnalyticsAuthorization(true);
            }
        }).setNegativeButton((int) R.string.no, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                VectorHomeActivity.this.setAnalyticsAuthorization(false);
            }
        }).show();
    }

    /* access modifiers changed from: private */
    public void setAnalyticsAuthorization(boolean z) {
        PreferencesManager.setUseAnalytics(this, z);
        PreferencesManager.setDidAskToUseAnalytics(this);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return !CommonActivityUtils.shouldRestartApp(this);
    }

    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerVisible((int) GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer((int) GravityCompat.START);
        } else if (!TextUtils.isEmpty(this.mSearchView.getQuery().toString())) {
            this.mSearchView.setQuery("", true);
        } else if (this.mFloatingActionsMenu.isExpanded()) {
            this.mFloatingActionsMenu.collapse();
        } else {
            this.mFragmentManager.popBackStack((String) null, 1);
            super.onBackPressed();
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(CURRENT_MENU_ID, this.mCurrentMenuId);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mUniversalLinkReceiver);
        this.securityChecks.activityStopped();
        hideWaitingView();
        resetFilter();
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mBrdRcvStopWaitingView);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onPause() : unregisterReceiver fails ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
        if (this.mSession.isAlive()) {
            removeEventsListener();
        }
        Runnable runnable = this.mShowFloatingActionButtonRunnable;
        if (runnable != null) {
            FloatingActionsMenu floatingActionsMenu = this.mFloatingActionsMenu;
            if (floatingActionsMenu != null) {
                floatingActionsMenu.removeCallbacks(runnable);
                this.mShowFloatingActionButtonRunnable = null;
            }
        }
        removeBadgeEventsListener();
    }

    public void onDestroy() {
        super.onDestroy();
        this.securityChecks.activityStopped();
        if (sharedInstance == this) {
            sharedInstance = null;
        }
        resetFilter();
    }

    public void onLowMemory() {
        super.onLowMemory();
        CommonActivityUtils.onLowMemory(this);
    }

    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
        CommonActivityUtils.onTrimMemory(this, i);
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String str = EXTRA_JUMP_TO_ROOM_PARAMS;
        this.mAutomaticallyOpenedRoomParams = (Map) intent.getSerializableExtra(str);
        intent.removeExtra(str);
        String str2 = EXTRA_JUMP_TO_UNIVERSAL_LINK;
        this.mUniversalLinkToOpen = (Uri) intent.getParcelableExtra(str2);
        intent.removeExtra(str2);
        String str3 = EXTRA_MEMBER_ID;
        this.mMemberIdToOpen = intent.getStringExtra(str3);
        intent.removeExtra(str3);
        String str4 = EXTRA_GROUP_ID;
        this.mGroupIdToOpen = intent.getStringExtra(str4);
        intent.removeExtra(str4);
        String str5 = EXTRA_WAITING_VIEW_STATUS;
        if (intent.getBooleanExtra(str5, false)) {
            showWaitingView();
        } else {
            hideWaitingView();
        }
        intent.removeExtra(str5);
    }

    public HomeRoomsViewModel getRoomsViewModel() {
        return this.mRoomsViewModel;
    }

    private void setupNavigation() {
        setSupportActionBar(this.mToolbar);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_icon, null);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout.findViewById(R.id.tab_icon_conversations);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout.findViewById(R.id.tab_icon_contacts);
        this.mTopNavigationView.getTabAt(0).setCustomView((View) linearLayout2);
        this.mTopNavigationView.getTabAt(1).setCustomView((View) linearLayout3);
        this.mTopNavigationView.addOnTabSelectedListener(new OnTabSelectedListener() {
            public void onTabReselected(Tab tab) {
            }

            public void onTabUnselected(Tab tab) {
            }

            public void onTabSelected(Tab tab) {
                VectorHomeActivity.this.setSelectedTabStyle();
                VectorHomeActivity.this.updateSelectedFragment(tab, true);
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateSelectedFragment(Tab tab, boolean z) {
        Fragment fragment;
        int position = tab.getPosition();
        if (this.mCurrentMenuId != position) {
            if (position == 0) {
                Log.d(LOG_TAG, "onNavigationItemSelected ROOMS");
                FragmentManager fragmentManager = this.mFragmentManager;
                String str = TAG_FRAGMENT_ROOMS;
                Fragment findFragmentByTag = fragmentManager.findFragmentByTag(str);
                if (findFragmentByTag == null) {
                    findFragmentByTag = TchapRoomsFragment.newInstance();
                }
                this.mCurrentFragmentTag = str;
                this.mSearchView.setQueryHint(getString(R.string.home_filter_placeholder_rooms));
            } else if (position != 1) {
                fragment = null;
            } else {
                Log.d(LOG_TAG, "onNavigationItemSelected PEOPLE");
                FragmentManager fragmentManager2 = this.mFragmentManager;
                String str2 = TAG_FRAGMENT_PEOPLE;
                fragment = fragmentManager2.findFragmentByTag(str2);
                if (fragment == null) {
                    fragment = TchapContactFragment.newInstance();
                }
                this.mCurrentFragmentTag = str2;
                this.mSearchView.setQueryHint(getString(R.string.home_filter_placeholder_people));
            }
            Runnable runnable = this.mShowFloatingActionButtonRunnable;
            if (runnable != null) {
                FloatingActionsMenu floatingActionsMenu = this.mFloatingActionsMenu;
                if (floatingActionsMenu != null) {
                    floatingActionsMenu.removeCallbacks(runnable);
                    this.mShowFloatingActionButtonRunnable = null;
                }
            }
            hideWaitingView();
            this.mCurrentMenuId = position;
            showFloatingActionMenuIfRequired();
            if (fragment != null) {
                int i = R.anim.tchap_anim_slide_in_right;
                int i2 = R.anim.tchap_anim_slide_out_right;
                if (position == 1) {
                    i2 = R.anim.tchap_anim_slide_out_left;
                    i = R.anim.tchap_anim_slide_in_left;
                }
                try {
                    FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
                    if (z) {
                        beginTransaction.setCustomAnimations(i, i2);
                    }
                    beginTransaction.replace(R.id.fragment_container, fragment, this.mCurrentFragmentTag).addToBackStack(this.mCurrentFragmentTag).commit();
                    getSupportFragmentManager().executePendingTransactions();
                    if (this.mSearchView.getQuery().toString().length() == 0) {
                        resetFilter();
                    }
                } catch (Exception e) {
                    String str3 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## updateSelectedFragment() failed : ");
                    sb.append(e.getMessage());
                    Log.e(str3, sb.toString(), e);
                }
            }
        }
    }

    public String getSearchQuery() {
        return this.mSearchView.getQuery().toString();
    }

    public void updateTabStyle(int i, int i2) {
        this.mToolbar.setBackgroundColor(i);
        Class<FloatingActionsMenu> cls = FloatingActionsMenu.class;
        try {
            Field declaredField = cls.getDeclaredField("mAddButtonColorNormal");
            declaredField.setAccessible(true);
            Field declaredField2 = cls.getDeclaredField("mAddButtonColorPressed");
            declaredField2.setAccessible(true);
            declaredField.set(this.mFloatingActionsMenu, Integer.valueOf(i));
            declaredField2.set(this.mFloatingActionsMenu, Integer.valueOf(i2));
            this.mFabMain.setColorNormal(i);
            this.mFabMain.setColorPressed(i2);
        } catch (Exception unused) {
        }
        this.mFabJoinRoom.setColorNormal(i);
        this.mFabJoinRoom.setColorPressed(i2);
        this.mFabCreateRoom.setColorNormal(i);
        this.mFabCreateRoom.setColorPressed(i2);
        this.mFabStartChat.setColorNormal(i);
        this.mFabStartChat.setColorPressed(i2);
        this.mVectorPendingCallView.updateBackgroundColor(i);
        this.mSyncInProgressView.setBackgroundColor(i);
        if (VERSION.SDK_INT >= 21) {
            this.mSyncInProgressView.setIndeterminateTintList(ColorStateList.valueOf(i2));
        } else {
            this.mSyncInProgressView.getIndeterminateDrawable().setColorFilter(i2, Mode.SRC_IN);
        }
        EditText editText = (EditText) this.mSearchView.findViewById(R.id.search_src_text);
        editText.setTextColor(ThemeUtils.INSTANCE.getColor(this, R.attr.vctr_primary_text_color));
        editText.setHintTextColor(ThemeUtils.INSTANCE.getColor(this, R.attr.vctr_primary_hint_text_color));
        editText.setTextSize(15.0f);
    }

    private void initViews() {
        this.mVectorPendingCallView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                IMXCall activeCall = CallsManager.getSharedInstance().getActiveCall();
                if (activeCall != null) {
                    final Intent intent = new Intent(VectorHomeActivity.this, VectorCallViewActivity.class);
                    intent.putExtra(VectorCallViewActivity.EXTRA_MATRIX_ID, activeCall.getSession().getCredentials().userId);
                    intent.putExtra(VectorCallViewActivity.EXTRA_CALL_ID, activeCall.getCallId());
                    VectorHomeActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            VectorHomeActivity.this.startActivity(intent);
                        }
                    });
                }
            }
        });
        addUnreadBadges();
        SearchManager searchManager = (SearchManager) getSystemService("search");
        LinearLayout linearLayout = (LinearLayout) this.mSearchView.findViewById(R.id.search_edit_frame);
        if (linearLayout != null) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) linearLayout.getLayoutParams();
            marginLayoutParams.leftMargin = 0;
            linearLayout.setLayoutParams(marginLayoutParams);
        }
        ImageView imageView = (ImageView) this.mSearchView.findViewById(R.id.search_mag_icon);
        if (imageView != null) {
            MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) imageView.getLayoutParams();
            marginLayoutParams2.leftMargin = 0;
            imageView.setLayoutParams(marginLayoutParams2);
        }
        this.mToolbar.setContentInsetStartWithNavigation(0);
        this.mSearchView.setMaxWidth(Integer.MAX_VALUE);
        this.mSearchView.setSubmitButtonEnabled(false);
        this.mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        this.mSearchView.setIconifiedByDefault(false);
        this.mSearchView.setOnQueryTextListener(this);
        this.mSearchView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (view != null) {
                    VectorHomeActivity.this.mSearchView.setIconified(false);
                }
            }
        });
        Class<FloatingActionsMenu> cls = FloatingActionsMenu.class;
        try {
            Field declaredField = cls.getDeclaredField("mLabelsStyle");
            declaredField.setAccessible(true);
            declaredField.set(this.mFloatingActionsMenu, Integer.valueOf(ThemeUtils.INSTANCE.getResourceId(this, R.style.Floating_Actions_Menu)));
            Method declaredMethod = cls.getDeclaredMethod("createLabels", new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(this.mFloatingActionsMenu, new Object[0]);
        } catch (Exception unused) {
        }
        this.mFabStartChat.setIconDrawable(ThemeUtils.INSTANCE.tintDrawableWithColor(ContextCompat.getDrawable(this, R.drawable.tchap_ic_new_discussion), ContextCompat.getColor(this, 17170443)));
        this.mFabCreateRoom.setIconDrawable(ThemeUtils.INSTANCE.tintDrawableWithColor(ContextCompat.getDrawable(this, R.drawable.tchap_ic_new_room), ContextCompat.getColor(this, 17170443)));
        this.mFabJoinRoom.setIconDrawable(ThemeUtils.INSTANCE.tintDrawableWithColor(ContextCompat.getDrawable(this, R.drawable.tchap_ic_join_public), ContextCompat.getColor(this, 17170443)));
        this.mFloatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new OnFloatingActionsMenuUpdateListener() {
            public void onMenuExpanded() {
                if (!VectorHomeActivity.this.isWaitingViewVisible()) {
                    VectorHomeActivity.this.touchGuard.animate().alpha(0.6f);
                    VectorHomeActivity.this.touchGuard.setClickable(true);
                }
            }

            public void onMenuCollapsed() {
                VectorHomeActivity.this.touchGuard.animate().alpha(0.0f);
                VectorHomeActivity.this.touchGuard.setClickable(false);
            }
        });
        this.touchGuard.setClickable(false);
    }

    private void resetFilter() {
        SearchView searchView = this.mSearchView;
        if (searchView != null) {
            searchView.setQuery("", false);
            this.mSearchView.clearFocus();
        }
        hideKeyboard();
    }

    private void hideKeyboard() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    public boolean onQueryTextChange(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(HelpFormatter.DEFAULT_OPT_PREFIX);
        sb.append(this.mCurrentMenuId);
        final String sb2 = sb.toString();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public void run() {
                String charSequence = VectorHomeActivity.this.mSearchView.getQuery().toString();
                StringBuilder sb = new StringBuilder();
                sb.append(charSequence);
                sb.append(HelpFormatter.DEFAULT_OPT_PREFIX);
                sb.append(VectorHomeActivity.this.mCurrentMenuId);
                if (TextUtils.equals(sb.toString(), sb2)) {
                    VectorHomeActivity.this.applyFilter(charSequence);
                }
            }
        }, 500);
        return true;
    }

    private Fragment getSelectedFragment() {
        int i = this.mCurrentMenuId;
        if (i == 0) {
            return this.mFragmentManager.findFragmentByTag(TAG_FRAGMENT_ROOMS);
        }
        if (i != 1) {
            return null;
        }
        return this.mFragmentManager.findFragmentByTag(TAG_FRAGMENT_PEOPLE);
    }

    /* access modifiers changed from: private */
    public void applyFilter(String str) {
        Fragment selectedFragment = getSelectedFragment();
        if (selectedFragment instanceof AbsHomeFragment) {
            ((AbsHomeFragment) selectedFragment).applyFilter(str.trim());
        }
    }

    private void displayCryptoCorruption() {
        MXSession mXSession = this.mSession;
        if (mXSession != null && mXSession.getCrypto() != null && this.mSession.getCrypto().isCorrupted()) {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String str = "isFirstCryptoAlertKey";
            if (defaultSharedPreferences.getBoolean(str, true)) {
                defaultSharedPreferences.edit().putBoolean(str, false).apply();
                new Builder(this).setMessage((int) R.string.e2e_need_log_in_again).setNegativeButton((int) R.string.cancel, (OnClickListener) null).setPositiveButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CommonActivityUtils.logout(VectorApp.getCurrentActivity());
                    }
                }).show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void processIntentUniversalLink() {
        Intent intent = getIntent();
        if (intent != null) {
            String str = VectorUniversalLinkReceiver.EXTRA_UNIVERSAL_LINK_URI;
            if (intent.hasExtra(str)) {
                Log.d(LOG_TAG, "## processIntentUniversalLink(): EXTRA_UNIVERSAL_LINK_URI present1");
                if (((Uri) intent.getParcelableExtra(str)) != null) {
                    Intent intent2 = new Intent(VectorApp.getInstance(), VectorUniversalLinkReceiver.class);
                    intent2.setAction(VectorUniversalLinkReceiver.BROADCAST_ACTION_UNIVERSAL_LINK_RESUME);
                    intent2.putExtras(getIntent().getExtras());
                    intent2.putExtra(VectorUniversalLinkReceiver.EXTRA_UNIVERSAL_LINK_SENDER_ID, VectorUniversalLinkReceiver.HOME_SENDER_ID);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
                    showWaitingView();
                    intent.removeExtra(str);
                    Log.d(LOG_TAG, "## processIntentUniversalLink(): Broadcast BROADCAST_ACTION_UNIVERSAL_LINK_RESUME sent");
                }
            }
        }
    }

    private void revealFloatingActionMenu() {
        FloatingActionsMenu floatingActionsMenu = this.mFloatingActionsMenu;
        if (floatingActionsMenu != null) {
            floatingActionsMenu.collapse();
            this.mFloatingActionsMenu.setVisibility(0);
            this.mFabMain.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (VectorHomeActivity.this.mFloatingActionsMenu != null) {
                        VectorHomeActivity.this.mFloatingActionsMenu.setVisibility(0);
                    }
                }
            }).start();
        }
    }

    private void concealFloatingActionMenu() {
        FloatingActionsMenu floatingActionsMenu = this.mFloatingActionsMenu;
        if (floatingActionsMenu != null) {
            floatingActionsMenu.collapse();
            this.mFabMain.animate().scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (VectorHomeActivity.this.mFloatingActionsMenu != null) {
                        VectorHomeActivity.this.mFloatingActionsMenu.setVisibility(8);
                    }
                }
            }).start();
        }
    }

    public void hideFloatingActionButton(String str) {
        synchronized (this) {
            if (TextUtils.equals(this.mCurrentFragmentTag, str) && this.mFloatingActionsMenu != null) {
                if (this.mShowFloatingActionButtonRunnable == null) {
                    concealFloatingActionMenu();
                    this.mShowFloatingActionButtonRunnable = new Runnable() {
                        public void run() {
                            VectorHomeActivity.this.mShowFloatingActionButtonRunnable = null;
                            VectorHomeActivity.this.showFloatingActionMenuIfRequired();
                        }
                    };
                } else {
                    this.mFloatingActionsMenu.removeCallbacks(this.mShowFloatingActionButtonRunnable);
                }
                try {
                    this.mFloatingActionsMenu.postDelayed(this.mShowFloatingActionButtonRunnable, 1000);
                } catch (Throwable th) {
                    String str2 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("failed to postDelayed ");
                    sb.append(th.getMessage());
                    Log.e(str2, sb.toString(), th);
                    if (!(this.mShowFloatingActionButtonRunnable == null || this.mFloatingActionsMenu == null)) {
                        this.mFloatingActionsMenu.removeCallbacks(this.mShowFloatingActionButtonRunnable);
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            VectorHomeActivity.this.showFloatingActionMenuIfRequired();
                        }
                    });
                }
            }
        }
    }

    public View getFloatingActionButton() {
        return this.mFabMain;
    }

    public void createNewChat(ActionMode actionMode, ContactsFilter contactsFilter) {
        Intent intent = new Intent(this, VectorRoomInviteMembersActivity.class);
        intent.putExtra("MXCActionBarActivity.EXTRA_MATRIX_ID", this.mSession.getMyUserId());
        intent.putExtra(VectorRoomInviteMembersActivity.EXTRA_ACTION_ACTIVITY_MODE, actionMode);
        intent.putExtra(VectorRoomInviteMembersActivity.EXTRA_CONTACTS_FILTER, contactsFilter);
        startActivity(intent);
    }

    public List<Room> getRoomInvitations() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (this.mSession.getDataHandler().getStore() == null) {
            Log.e(LOG_TAG, "## getRoomInvitations() : null store");
            return new ArrayList();
        }
        for (RoomSummary roomSummary : this.mSession.getDataHandler().getStore().getSummaries()) {
            if (roomSummary != null) {
                Room room = this.mSession.getDataHandler().getStore().getRoom(roomSummary.getRoomId());
                if (room != null && !room.isConferenceUserRoom() && room.isInvited()) {
                    if (room.isDirectChatInvitation() || room.isDirect()) {
                        arrayList.add(room);
                    } else {
                        arrayList2.add(room);
                    }
                }
            }
        }
        Comparator roomsDateComparator = RoomUtils.getRoomsDateComparator(this.mSession, true);
        Collections.sort(arrayList, roomsDateComparator);
        Collections.sort(arrayList2, roomsDateComparator);
        ArrayList arrayList3 = new ArrayList();
        if (this.mCurrentMenuId == 0) {
            arrayList3.addAll(arrayList);
            arrayList3.addAll(arrayList2);
        }
        return arrayList3;
    }

    public void onJoinRoom(MXSession mXSession, String str) {
        Room room = mXSession.getDataHandler().getRoom(str);
        final RoomPreviewData roomPreviewData = new RoomPreviewData(this.mSession, str, null, (room == null || room.getState() == null) ? null : room.getState().getCanonicalAlias(), null);
        showWaitingView();
        DinsicUtils.joinRoom(roomPreviewData, new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                VectorHomeActivity.this.hideWaitingView();
                DinsicUtils.onNewJoinedRoom(VectorHomeActivity.this, roomPreviewData);
            }

            private void onError(String str) {
                VectorHomeActivity.this.hideWaitingView();
                Toast.makeText(VectorHomeActivity.this, str, 0).show();
            }

            public void onNetworkError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                    VectorHomeActivity.this.hideWaitingView();
                    VectorHomeActivity.this.getConsentNotGivenHelper().displayDialog(matrixError);
                    return;
                }
                onError(matrixError.getLocalizedMessage());
            }

            public void onUnexpectedError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }
        });
    }

    private ApiCallback<Void> createForgetLeaveCallback(final String str, final ApiCallback<Void> apiCallback) {
        return new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                EventStreamService.cancelNotificationsForRoomId(VectorHomeActivity.this.mSession.getMyUserId(), str);
                VectorHomeActivity.this.hideWaitingView();
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onSuccess(null);
                }
            }

            private void onError(String str) {
                VectorHomeActivity.this.hideWaitingView();
                Toast.makeText(VectorHomeActivity.this, str, 1).show();
            }

            public void onNetworkError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                if (MatrixError.M_CONSENT_NOT_GIVEN.equals(matrixError.errcode)) {
                    VectorHomeActivity.this.hideWaitingView();
                    VectorHomeActivity.this.getConsentNotGivenHelper().displayDialog(matrixError);
                    return;
                }
                onError(matrixError.getLocalizedMessage());
            }

            public void onUnexpectedError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }
        };
    }

    public void onForgetRoom(String str, ApiCallback<Void> apiCallback) {
        Room room = this.mSession.getDataHandler().getRoom(str);
        if (room != null) {
            showWaitingView();
            room.forget(createForgetLeaveCallback(str, apiCallback));
        }
    }

    public void onRejectInvitation(String str, ApiCallback<Void> apiCallback) {
        Room room = this.mSession.getDataHandler().getRoom(str);
        if (room != null) {
            showWaitingView();
            room.leave(createForgetLeaveCallback(str, apiCallback));
        }
    }

    /* access modifiers changed from: private */
    public void exportKeysAndSignOut() {
        View inflate = getLayoutInflater().inflate(R.layout.dialog_export_e2e_keys, null);
        Builder view = new Builder(this).setTitle((int) R.string.encryption_export_room_keys).setView(inflate);
        final TextInputEditText textInputEditText = (TextInputEditText) inflate.findViewById(R.id.dialog_e2e_keys_passphrase_edit_text);
        final TextInputEditText textInputEditText2 = (TextInputEditText) inflate.findViewById(R.id.dialog_e2e_keys_confirm_passphrase_edit_text);
        final Button button = (Button) inflate.findViewById(R.id.dialog_e2e_keys_export_button);
        AnonymousClass24 r4 = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                button.setEnabled(!TextUtils.isEmpty(textInputEditText.getText()) && TextUtils.equals(textInputEditText.getText(), textInputEditText2.getText()));
            }
        };
        textInputEditText.addTextChangedListener(r4);
        textInputEditText2.addTextChangedListener(r4);
        button.setEnabled(false);
        final AlertDialog show = view.show();
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                VectorHomeActivity.this.showWaitingView();
                CommonActivityUtils.exportKeys(VectorHomeActivity.this.mSession, textInputEditText.getText().toString(), new SimpleApiCallback<String>(VectorHomeActivity.this) {
                    public void onSuccess(String str) {
                        VectorHomeActivity.this.hideWaitingView();
                        new Builder(VectorHomeActivity.this).setMessage((CharSequence) VectorHomeActivity.this.getString(R.string.encryption_export_saved_as, new Object[]{str})).setCancelable(false).setPositiveButton((int) R.string.action_sign_out, (OnClickListener) new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                VectorHomeActivity.this.showWaitingView();
                                CommonActivityUtils.logout(VectorHomeActivity.this);
                            }
                        }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
                    }
                });
                show.dismiss();
            }
        });
    }

    private void initSlidingMenu() {
        AnonymousClass26 r0 = new ActionBarDrawerToggle(this, this.mDrawerLayout, this.mToolbar, R.string.action_open, R.string.action_close) {
            public void onDrawerOpened(View view) {
            }

            public void onDrawerClosed(View view) {
                switch (VectorHomeActivity.this.mSlidingMenuIndex) {
                    case R.id.sliding_menu_app_tac /*2131297032*/:
                        VectorUtils.displayAppTac();
                        break;
                    case R.id.sliding_menu_contacts /*2131297034*/:
                        VectorHomeActivity.this.mTopNavigationView.getTabAt(1).select();
                        break;
                    case R.id.sliding_menu_public_rooms /*2131297036*/:
                        VectorHomeActivity.this.startActivity(new Intent(VectorHomeActivity.this, TchapPublicRoomSelectionActivity.class));
                        break;
                    case R.id.sliding_menu_send_bug_report /*2131297037*/:
                        BugReporter.sendBugReport();
                        break;
                    case R.id.sliding_menu_settings /*2131297038*/:
                        VectorHomeActivity vectorHomeActivity = VectorHomeActivity.this;
                        vectorHomeActivity.startActivity(VectorSettingsActivity.getIntent(vectorHomeActivity, vectorHomeActivity.mSession.getMyUserId()));
                        break;
                    case R.id.sliding_menu_sign_out /*2131297039*/:
                        new Builder(VectorHomeActivity.this).setMessage((int) R.string.action_sign_out_confirmation).setCancelable(false).setPositiveButton((int) R.string.action_sign_out, (OnClickListener) new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                VectorHomeActivity.this.showWaitingView();
                                CommonActivityUtils.logout(VectorHomeActivity.this);
                            }
                        }).setNeutralButton((int) R.string.encryption_export_export, (OnClickListener) new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                VectorHomeActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        VectorHomeActivity.this.exportKeysAndSignOut();
                                    }
                                });
                            }
                        }).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
                        break;
                }
                VectorHomeActivity.this.mSlidingMenuIndex = -1;
            }
        };
        this.navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                VectorHomeActivity.this.mDrawerLayout.closeDrawers();
                VectorHomeActivity.this.mSlidingMenuIndex = menuItem.getItemId();
                return true;
            }
        });
        this.mDrawerLayout.setDrawerListener(r0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_material_menu_white));
        }
    }

    /* access modifiers changed from: private */
    public void refreshSlidingMenu() {
        NavigationView navigationView2 = this.navigationView;
        if (navigationView2 != null) {
            Menu menu = navigationView2.getMenu();
            MenuItem findItem = menu.findItem(R.id.sliding_menu_sign_out);
            if (findItem != null) {
                setTextColorForMenuItem(findItem, R.color.vector_fuchsia_color);
            }
            if (DinsicUtils.isExternalTchapSession(this.mSession)) {
                MenuItem findItem2 = menu.findItem(R.id.sliding_menu_public_rooms);
                if (findItem2 != null) {
                    findItem2.setVisible(false);
                }
            }
            TextView textView = (TextView) findViewById(R.id.sliding_menu_app_version);
            if (textView != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.room_sliding_menu_version));
                sb.append(" ");
                sb.append(VectorUtils.getApplicationVersion(this));
                textView.setText(sb.toString());
            }
            TextView textView2 = (TextView) findViewById(R.id.sliding_menu_infos);
            if (textView2 != null) {
                textView2.setText(getString(R.string.tchap_burger_menu_info));
            }
            TextView textView3 = (TextView) this.navigationView.findViewById(R.id.home_menu_main_displayname);
            if (textView3 != null) {
                textView3.setText(DinsicUtils.getNameFromDisplayName(this.mSession.getMyUser().displayname));
            }
            TextView textView4 = (TextView) this.navigationView.findViewById(R.id.home_menu_main_matrix_id);
            if (textView4 != null) {
                MXSession mXSession = this.mSession;
                if (mXSession != null) {
                    List list = mXSession.getMyUser().getlinkedEmails();
                    if (!(list == null || list.size() == 0)) {
                        textView4.setText(((ThirdPartyIdentifier) list.get(0)).address);
                    }
                }
            }
            ImageView imageView = (ImageView) this.navigationView.findViewById(R.id.home_menu_main_avatar);
            if (imageView != null) {
                MXSession mXSession2 = this.mSession;
                VectorUtils.loadUserAvatar(this, mXSession2, imageView, mXSession2.getMyUser());
                imageView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        VectorHomeActivity.this.mSlidingMenuIndex = R.id.sliding_menu_settings;
                        VectorHomeActivity.this.mDrawerLayout.closeDrawers();
                    }
                });
            } else {
                this.navigationView.post(new Runnable() {
                    public void run() {
                        VectorHomeActivity.this.refreshSlidingMenu();
                    }
                });
            }
        }
    }

    private void setTextColorForMenuItem(MenuItem menuItem, int i) {
        SpannableString spannableString = new SpannableString(menuItem.getTitle().toString());
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, i)), 0, spannableString.length(), 0);
        menuItem.setTitle(spannableString);
    }

    public void startCall(String str, String str2, MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
        if (str != null && str2 != null) {
            final Intent intent = new Intent(this, VectorCallViewActivity.class);
            intent.putExtra(VectorCallViewActivity.EXTRA_MATRIX_ID, str);
            intent.putExtra(VectorCallViewActivity.EXTRA_CALL_ID, str2);
            if (mXUsersDevicesMap != null) {
                intent.putExtra(VectorCallViewActivity.EXTRA_UNKNOWN_DEVICES, mXUsersDevicesMap);
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    VectorHomeActivity.this.startActivity(intent);
                }
            });
        }
    }

    private void addBadgeEventsListener() {
        this.mSession.getDataHandler().addListener(this.mBadgeEventsListener);
        refreshUnreadBadges();
    }

    private void removeBadgeEventsListener() {
        this.mSession.getDataHandler().removeListener(this.mBadgeEventsListener);
    }

    private void addUnreadBadges() {
        int i = (int) ((getResources().getDisplayMetrics().density * 7.0f) + 0.5f);
        for (int i2 = 0; i2 < this.mTopNavigationView.getTabCount(); i2++) {
            try {
                LinearLayout linearLayout = (LinearLayout) this.mTopNavigationView.getTabAt(i2).getCustomView();
                UnreadCounterBadgeView unreadCounterBadgeView = new UnreadCounterBadgeView(linearLayout.getContext());
                LayoutParams layoutParams = new LayoutParams(-2, -2);
                layoutParams.setMargins(0, i, 0, 0);
                linearLayout.addView(unreadCounterBadgeView, layoutParams);
                this.mBadgeViewByIndex.put(Integer.valueOf(i2), unreadCounterBadgeView);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## addUnreadBadges failed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
        refreshUnreadBadges();
    }

    public void refreshUnreadBadges() {
        MXDataHandler dataHandler = this.mSession.getDataHandler();
        if (dataHandler != null) {
            IMXStore store = dataHandler.getStore();
            if (store != null) {
                BingRulesManager bingRulesManager = dataHandler.getBingRulesManager();
                Collection<RoomSummary> summaries = store.getSummaries();
                HashMap hashMap = new HashMap();
                HashSet hashSet = new HashSet();
                for (RoomSummary roomSummary : summaries) {
                    Room room = store.getRoom(roomSummary.getRoomId());
                    if (room != null) {
                        hashMap.put(room, roomSummary);
                        if (!room.isConferenceUserRoom() && room.isInvited() && room.isDirectChatInvitation()) {
                            hashSet.add(room.getRoomId());
                        }
                    }
                }
                for (Integer num : new HashSet(this.mBadgeViewByIndex.keySet())) {
                    if (num.intValue() == 0) {
                        HashSet hashSet2 = new HashSet();
                        for (Room room2 : hashMap.keySet()) {
                            if (!room2.isConferenceUserRoom()) {
                                hashSet2.add(room2.getRoomId());
                            }
                        }
                        Iterator it = hashSet2.iterator();
                        int i = 0;
                        while (it.hasNext()) {
                            String str = (String) it.next();
                            Room room3 = store.getRoom(str);
                            if (room3 != null) {
                                if (!room3.isInvited()) {
                                    int notificationCount = room3.getNotificationCount();
                                    if (bingRulesManager.isRoomMentionOnly(str)) {
                                        notificationCount = room3.getHighlightCount();
                                    }
                                    if (notificationCount <= 0) {
                                    }
                                }
                                i++;
                            }
                        }
                        ((UnreadCounterBadgeView) this.mBadgeViewByIndex.get(num)).updateCounter(i, 0);
                    }
                }
            }
        }
    }

    private void checkDeviceId() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String str = NO_DEVICE_ID_WARNING_KEY;
        if (defaultSharedPreferences.getBoolean(str, true)) {
            defaultSharedPreferences.edit().putBoolean(str, false).apply();
            if (TextUtils.isEmpty(this.mSession.getCredentials().deviceId)) {
                new Builder(this).setMessage((int) R.string.e2e_enabling_on_app_update).setPositiveButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CommonActivityUtils.logout(VectorHomeActivity.this);
                    }
                }).setNegativeButton((int) R.string.later, (OnClickListener) null).show();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296513})
    public void touchGuardClicked() {
        this.mFloatingActionsMenu.collapse();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296351})
    public void fabMenuStartChat() {
        this.mFloatingActionsMenu.collapse();
        createNewChat(ActionMode.START_DIRECT_CHAT, ContactsFilter.TCHAP_USERS_ONLY);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296348})
    public void fabMenuCreateRoom() {
        this.mFloatingActionsMenu.collapse();
        startActivity(new Intent(this, TchapRoomCreationActivity.class));
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296350})
    public void fabMenuJoinRoom() {
        this.mFloatingActionsMenu.collapse();
        startActivity(new Intent(this, TchapPublicRoomSelectionActivity.class));
    }

    public void onRoomDataUpdated() {
        Result update = this.mRoomsViewModel.update();
        Fragment selectedFragment = getSelectedFragment();
        if (selectedFragment != null && (selectedFragment instanceof AbsHomeFragment)) {
            ((AbsHomeFragment) selectedFragment).onRoomResultUpdated(update);
        }
    }

    private void addEventsListener() {
        this.mEventsListener = new MXEventListener() {
            private boolean mRefreshOnChunkEnd = false;

            private void onForceRefresh() {
                if (VectorHomeActivity.this.mSyncInProgressView.getVisibility() != 0) {
                    VectorHomeActivity.this.onRoomDataUpdated();
                }
            }

            public void onAccountInfoUpdate(MyUser myUser) {
                VectorHomeActivity.this.refreshSlidingMenu();
            }

            public void onInitialSyncComplete(String str) {
                Log.d(VectorHomeActivity.LOG_TAG, "## onInitialSyncComplete()");
                VectorHomeActivity.this.onRoomDataUpdated();
            }

            public void onLiveEventsChunkProcessed(String str, String str2) {
                Activity currentActivity = VectorApp.getCurrentActivity();
                VectorHomeActivity vectorHomeActivity = VectorHomeActivity.this;
                if (currentActivity == vectorHomeActivity && this.mRefreshOnChunkEnd) {
                    vectorHomeActivity.onRoomDataUpdated();
                }
                this.mRefreshOnChunkEnd = false;
                VectorHomeActivity.this.mSyncInProgressView.setVisibility(8);
                VectorHomeActivity.this.processIntentUniversalLink();
            }

            public void onLiveEvent(Event event, RoomState roomState) {
                String type = event.getType();
                this.mRefreshOnChunkEnd = ((event.roomId != null && RoomSummary.isSupportedEvent(event)) || "m.room.member".equals(type) || Event.EVENT_TYPE_TAGS.equals(type) || Event.EVENT_TYPE_REDACTION.equals(type) || Event.EVENT_TYPE_RECEIPT.equals(type) || Event.EVENT_TYPE_STATE_ROOM_AVATAR.equals(type) || Event.EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE.equals(type)) | this.mRefreshOnChunkEnd;
            }

            public void onReceiptEvent(String str, List<String> list) {
                this.mRefreshOnChunkEnd |= list.indexOf(VectorHomeActivity.this.mSession.getCredentials().userId) >= 0;
            }

            public void onRoomTagEvent(String str) {
                this.mRefreshOnChunkEnd = true;
            }

            public void onStoreReady() {
                onForceRefresh();
                if (VectorHomeActivity.this.mSharedFilesIntent != null) {
                    Log.d(VectorHomeActivity.LOG_TAG, "shared intent : the store is now ready, display sendFilesTo");
                    VectorHomeActivity vectorHomeActivity = VectorHomeActivity.this;
                    CommonActivityUtils.sendFilesTo(vectorHomeActivity, vectorHomeActivity.mSharedFilesIntent);
                    VectorHomeActivity.this.mSharedFilesIntent = null;
                }
            }

            public void onLeaveRoom(String str) {
                EventStreamService.cancelNotificationsForRoomId(VectorHomeActivity.this.mSession.getMyUserId(), str);
                onForceRefresh();
            }

            public void onNewRoom(String str) {
                onForceRefresh();
            }

            public void onJoinRoom(String str) {
                onForceRefresh();
            }

            public void onDirectMessageChatRoomsListUpdate() {
                this.mRefreshOnChunkEnd = true;
            }

            public void onEventDecrypted(String str, String str2) {
                RoomSummary summary = VectorHomeActivity.this.mSession.getDataHandler().getStore().getSummary(str);
                if (summary != null) {
                    Event latestReceivedEvent = summary.getLatestReceivedEvent();
                    if (latestReceivedEvent != null && TextUtils.equals(latestReceivedEvent.eventId, str2)) {
                        VectorHomeActivity.this.onRoomDataUpdated();
                    }
                }
            }
        };
        this.mSession.getDataHandler().addListener(this.mEventsListener);
    }

    private void removeEventsListener() {
        if (this.mSession.isAlive()) {
            this.mSession.getDataHandler().removeListener(this.mEventsListener);
        }
    }
}
