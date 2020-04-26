package im.vector.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayout.Tab;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.sdk.session.room.model.RoomAccessRulesKt;
import fr.gouv.tchap.util.DinsicUtils;
import fr.gouv.tchap.util.HexagonMaskView;
import fr.gouv.tchap.util.LiveSecurityChecks;
import im.vector.Matrix;
import im.vector.contacts.ContactsManager;
import im.vector.fragments.VectorRoomDetailsMembersFragment;
import im.vector.fragments.VectorRoomSettingsFragment;
import im.vector.fragments.VectorSearchRoomFilesListFragment;
import im.vector.util.PermissionsToolsKt;
import im.vector.util.VectorUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.fragments.MatrixMessageListFragment.OnSearchResultListener;
import org.matrix.androidsdk.listeners.MXEventListener;

public class VectorRoomDetailsActivity extends MXCActionBarActivity {
    public static final String EXTRA_ROOM_ID = "VectorRoomDetailsActivity.EXTRA_ROOM_ID";
    public static final String EXTRA_SELECTED_TAB_ID = "VectorRoomDetailsActivity.EXTRA_SELECTED_TAB_ID";
    public static final int FILE_TAB_INDEX = 1;
    private static final String KEY_FRAGMENT_TAG = "KEY_FRAGMENT_TAG";
    private static final String KEY_STATE_CURRENT_TAB_INDEX = "CURRENT_SELECTED_TAB";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorRoomDetailsActivity.class.getSimpleName();
    public static final int PEOPLE_TAB_INDEX = 0;
    public static final int SETTINGS_TAB_INDEX = 2;
    private static final String TAG_FRAGMENT_FILES_DETAILS = "im.vector.activity.TAG_FRAGMENT_FILES_DETAILS";
    private static final String TAG_FRAGMENT_PEOPLE_ROOM_DETAILS = "im.vector.activity.TAG_FRAGMENT_PEOPLE_ROOM_DETAILS";
    private static final String TAG_FRAGMENT_SETTINGS_ROOM_DETAIL = "im.vector.activity.TAG_FRAGMENT_SETTINGS_ROOM_DETAIL";
    private ActionBar mActionBar;
    private TextView mActionBarCustomTitle;
    private TextView mActionBarCustomTopic;
    private HexagonMaskView mAvatar;
    private int mCurrentTabIndex = -1;
    private final MXEventListener mEventListener = new MXEventListener() {
        public void onLeaveRoom(String str) {
            VectorRoomDetailsActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Intent intent = new Intent(VectorRoomDetailsActivity.this, VectorHomeActivity.class);
                    intent.setFlags(603979776);
                    VectorRoomDetailsActivity.this.startActivity(intent);
                }
            });
        }
    };
    private boolean mIsContactsPermissionChecked;
    private View mLoadOldestContentView;
    /* access modifiers changed from: private */
    public String mMatrixId;
    /* access modifiers changed from: private */
    public VectorRoomDetailsMembersFragment mRoomDetailsMembersFragment;
    /* access modifiers changed from: private */
    public String mRoomId;
    /* access modifiers changed from: private */
    public VectorRoomSettingsFragment mRoomSettingsFragment;
    /* access modifiers changed from: private */
    public VectorSearchRoomFilesListFragment mSearchFilesFragment;
    @BindView(2131297079)
    TabLayout mTabNavigationView;
    private Toolbar mToolbar;
    private LiveSecurityChecks securityChecks = new LiveSecurityChecks(this);

    public int getLayoutRes() {
        return R.layout.activity_vector_room_details;
    }

    public void initUiAndData() {
        if (CommonActivityUtils.shouldRestartApp(this)) {
            Log.e(LOG_TAG, "Restart the application.");
            CommonActivityUtils.restartApp(this);
        } else if (CommonActivityUtils.isGoingToSplash(this)) {
            Log.d(LOG_TAG, "onCreate : Going to splash screen");
        } else {
            Intent intent = getIntent();
            String str = EXTRA_ROOM_ID;
            if (!intent.hasExtra(str)) {
                Log.e(LOG_TAG, "No room ID extra.");
                finish();
                return;
            }
            String str2 = "MXCActionBarActivity.EXTRA_MATRIX_ID";
            if (intent.hasExtra(str2)) {
                this.mMatrixId = intent.getStringExtra(str2);
            }
            this.mSession = Matrix.getInstance(getApplicationContext()).getSession(this.mMatrixId);
            if (this.mSession == null || !this.mSession.isAlive()) {
                finish();
                return;
            }
            this.mRoomId = intent.getStringExtra(str);
            this.mRoom = this.mSession.getDataHandler().getRoom(this.mRoomId);
            intent.getIntExtra(EXTRA_SELECTED_TAB_ID, -1);
            setWaitingView(findViewById(R.id.settings_loading_layout));
            this.mLoadOldestContentView = findViewById(R.id.search_load_oldest_progress);
            this.mToolbar = (Toolbar) findViewById(R.id.room_toolbar);
            setSupportActionBar(this.mToolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            this.mActionBar = getSupportActionBar();
            this.mActionBarCustomTitle = (TextView) findViewById(R.id.room_action_bar_title);
            this.mActionBarCustomTopic = (TextView) findViewById(R.id.room_action_bar_topic);
            this.mAvatar = (HexagonMaskView) findViewById(R.id.avatar_img);
            setRoomTitle();
            setTopic();
            setAvatar();
            this.mTabNavigationView.addOnTabSelectedListener(new OnTabSelectedListener() {
                public void onTabReselected(Tab tab) {
                }

                public void onTabSelected(Tab tab) {
                    VectorRoomDetailsActivity vectorRoomDetailsActivity = VectorRoomDetailsActivity.this;
                    vectorRoomDetailsActivity.updateSelectedFragment(vectorRoomDetailsActivity.mTabNavigationView.getSelectedTabPosition());
                }

                public void onTabUnselected(Tab tab) {
                    int position = tab.getPosition();
                    FragmentTransaction beginTransaction = VectorRoomDetailsActivity.this.getSupportFragmentManager().beginTransaction();
                    if (position == 0) {
                        if (VectorRoomDetailsActivity.this.mRoomDetailsMembersFragment != null) {
                            beginTransaction.detach(VectorRoomDetailsActivity.this.mRoomDetailsMembersFragment).commit();
                        }
                    } else if (position == 2) {
                        VectorRoomDetailsActivity.this.onTabUnselectedSettingsFragment();
                    } else if (position != 1) {
                        Log.w(VectorRoomDetailsActivity.LOG_TAG, "## onTabUnselected() unknown tab selected!!");
                    } else if (VectorRoomDetailsActivity.this.mSearchFilesFragment != null) {
                        VectorRoomDetailsActivity.this.mSearchFilesFragment.cancelCatchingRequests();
                        beginTransaction.detach(VectorRoomDetailsActivity.this.mSearchFilesFragment).commit();
                    }
                }
            });
            int i = 0;
            if (!isFirstCreation()) {
                Bundle savedInstanceState = getSavedInstanceState();
                String str3 = KEY_STATE_CURRENT_TAB_INDEX;
                if (savedInstanceState.getInt(str3, 0) != 0) {
                    i = getSavedInstanceState().getInt(str3, 0);
                }
            }
            updateSelectedFragment(i);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        for (Fragment onActivityResult : getSupportFragmentManager().getFragments()) {
            onActivityResult.onActivityResult(i, i2, intent);
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Log.d(LOG_TAG, "## onSaveInstanceState(): ");
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            bundle.putInt(KEY_STATE_CURRENT_TAB_INDEX, actionBar.getSelectedNavigationIndex());
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (strArr.length == 0) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onRequestPermissionsResult(): cancelled ");
            sb.append(i);
            Log.d(str, sb.toString());
        } else if (i == 567) {
            if ("android.permission.READ_CONTACTS".equals(strArr[0])) {
                if (iArr[0] == 0) {
                    Log.d(LOG_TAG, "## onRequestPermissionsResult(): READ_CONTACTS permission granted");
                } else {
                    Log.w(LOG_TAG, "## onRequestPermissionsResult(): READ_CONTACTS permission not granted");
                    Toast.makeText(this, R.string.missing_permissions_warning, 0).show();
                }
                ContactsManager.getInstance().refreshLocalContactsSnapshot();
            }
        }
    }

    public void onBackPressed() {
        if (!(this.mCurrentTabIndex == 0 ? this.mRoomDetailsMembersFragment.onBackPressed() : false)) {
            super.onBackPressed();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mRoom.removeEventListener(this.mEventListener);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.securityChecks.checkOnActivityStart();
        applyScreenshotSecurity();
        if (this.mSession.isAlive()) {
            if ((this.mRoom.isJoined() || this.mRoom.isInvited()) && this.mSession.getDataHandler().doesRoomExist(this.mRoom.getRoomId())) {
                this.mRoom.addEventListener(this.mEventListener);
                startFileSearch();
            } else {
                Intent intent = new Intent(this, VectorHomeActivity.class);
                intent.setFlags(603979776);
                startActivity(intent);
            }
        }
    }

    private void saveUiTabContext(ActionBar.Tab tab) {
        tab.setTag((Bundle) tab.getTag());
    }

    private void resetUi() {
        hideWaitingView();
        View view = this.mLoadOldestContentView;
        if (view != null) {
            view.setVisibility(8);
        }
    }

    public void updateSelectedFragment(int i) {
        this.mTabNavigationView.getTabAt(i);
        Log.d(LOG_TAG, "## onTabSelected()");
        resetUi();
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        if (i == 0) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            String str = TAG_FRAGMENT_PEOPLE_ROOM_DETAILS;
            this.mRoomDetailsMembersFragment = (VectorRoomDetailsMembersFragment) supportFragmentManager.findFragmentByTag(str);
            VectorRoomDetailsMembersFragment vectorRoomDetailsMembersFragment = this.mRoomDetailsMembersFragment;
            if (vectorRoomDetailsMembersFragment == null) {
                this.mRoomDetailsMembersFragment = VectorRoomDetailsMembersFragment.newInstance();
                beginTransaction.replace(R.id.room_details_fragment_container, this.mRoomDetailsMembersFragment, str).commit();
                Log.d(LOG_TAG, "## onTabSelected() people frag replace");
            } else {
                beginTransaction.attach(vectorRoomDetailsMembersFragment).commit();
                Log.d(LOG_TAG, "## onTabSelected() people frag attach");
            }
            this.mCurrentTabIndex = 0;
            if (!this.mIsContactsPermissionChecked) {
                this.mIsContactsPermissionChecked = true;
                PermissionsToolsKt.checkPermissions(8, (Activity) this, (int) PermissionsToolsKt.PERMISSION_REQUEST_CODE);
            }
        } else if (i == 2) {
            onTabSelectSettingsFragment();
            this.mCurrentTabIndex = 2;
        } else if (i == 1) {
            FragmentManager supportFragmentManager2 = getSupportFragmentManager();
            String str2 = TAG_FRAGMENT_FILES_DETAILS;
            this.mSearchFilesFragment = (VectorSearchRoomFilesListFragment) supportFragmentManager2.findFragmentByTag(str2);
            VectorSearchRoomFilesListFragment vectorSearchRoomFilesListFragment = this.mSearchFilesFragment;
            if (vectorSearchRoomFilesListFragment == null) {
                this.mSearchFilesFragment = VectorSearchRoomFilesListFragment.newInstance(this.mSession.getCredentials().userId, this.mRoomId, R.layout.fragment_matrix_message_list_fragment);
                beginTransaction.replace(R.id.room_details_fragment_container, this.mSearchFilesFragment, str2).commit();
                Log.d(LOG_TAG, "## onTabSelected() file frag replace");
            } else {
                beginTransaction.attach(vectorSearchRoomFilesListFragment).commit();
                Log.d(LOG_TAG, "## onTabSelected() file frag attach");
            }
            this.mCurrentTabIndex = 1;
            startFileSearch();
        } else {
            Toast.makeText(this, "Not yet implemented", 0).show();
            this.mCurrentTabIndex = 2;
            Log.w(LOG_TAG, "## onTabSelected() unknown tab selected!!");
        }
    }

    private void startFileSearch() {
        if (this.mCurrentTabIndex == 1) {
            showWaitingView();
            this.mSearchFilesFragment.startFilesSearch(new OnSearchResultListener() {
                public void onSearchSucceed(int i) {
                    VectorRoomDetailsActivity.this.onSearchEnd(1, i);
                }

                public void onSearchFailed() {
                    VectorRoomDetailsActivity.this.onSearchEnd(1, 0);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void onSearchEnd(int i, int i2) {
        if (this.mCurrentTabIndex == i) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onSearchEnd() nbrMsg=");
            sb.append(i2);
            Log.d(str, sb.toString());
            hideWaitingView();
        }
    }

    private void onTabSelectSettingsFragment() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (VectorRoomDetailsActivity.this.mRoomSettingsFragment == null) {
                    VectorRoomDetailsActivity vectorRoomDetailsActivity = VectorRoomDetailsActivity.this;
                    vectorRoomDetailsActivity.mRoomSettingsFragment = VectorRoomSettingsFragment.newInstance(vectorRoomDetailsActivity.mMatrixId, VectorRoomDetailsActivity.this.mRoomId);
                    VectorRoomDetailsActivity.this.getFragmentManager().beginTransaction().replace(R.id.room_details_fragment_container, VectorRoomDetailsActivity.this.mRoomSettingsFragment, VectorRoomDetailsActivity.TAG_FRAGMENT_SETTINGS_ROOM_DETAIL).commit();
                    Log.d(VectorRoomDetailsActivity.LOG_TAG, "## onTabSelectSettingsFragment() settings frag replace");
                    return;
                }
                VectorRoomDetailsActivity.this.getFragmentManager().beginTransaction().attach(VectorRoomDetailsActivity.this.mRoomSettingsFragment).commit();
                Log.d(VectorRoomDetailsActivity.LOG_TAG, "## onTabSelectSettingsFragment() settings frag attach");
            }
        });
    }

    /* access modifiers changed from: private */
    public void onTabUnselectedSettingsFragment() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (VectorRoomDetailsActivity.this.mRoomSettingsFragment != null) {
                    VectorRoomDetailsActivity.this.getFragmentManager().beginTransaction().detach(VectorRoomDetailsActivity.this.mRoomSettingsFragment).commit();
                }
            }
        });
    }

    private void setRoomTitle() {
        setRoomTitle((this.mSession == null || this.mRoom == null) ? "" : DinsicUtils.getNameFromDisplayName(DinsicUtils.getRoomDisplayName(this, this.mRoom)));
    }

    public void setRoomTitle(String str) {
        this.mActionBarCustomTitle.setText(str);
    }

    private void setTopic() {
        if (this.mRoom != null) {
            setTopic(this.mRoom.getTopic());
        }
    }

    public void setTopic(String str) {
        this.mActionBarCustomTopic.setText(str);
    }

    private void setAvatar() {
        if (this.mRoom != null) {
            VectorUtils.loadRoomAvatar((Context) this, this.mSession, (ImageView) this.mAvatar, this.mRoom);
            if (TextUtils.equals(DinsicUtils.getRoomAccessRule(this.mRoom), RoomAccessRulesKt.RESTRICTED)) {
                this.mAvatar.setBorderSettings(ContextCompat.getColor(this, R.color.restricted_room_avatar_border_color), 3);
            } else {
                this.mAvatar.setBorderSettings(ContextCompat.getColor(this, R.color.unrestricted_room_avatar_border_color), 10);
            }
        }
    }

    public void refreshAvatar() {
        setAvatar();
    }
}
