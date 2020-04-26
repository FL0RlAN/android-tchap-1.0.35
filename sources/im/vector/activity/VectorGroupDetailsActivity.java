package im.vector.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import com.google.android.material.tabs.TabLayout;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.adapters.GroupDetailsFragmentPagerAdapter;
import im.vector.ui.themes.ActivityOtherThemes;
import im.vector.ui.themes.ThemeUtils;
import im.vector.view.RiotViewPager;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXPatterns;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.groups.GroupsManager;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.rest.model.group.Group;

public class VectorGroupDetailsActivity extends MXCActionBarActivity {
    public static final String EXTRA_GROUP_ID = "EXTRA_GROUP_ID";
    public static final String EXTRA_TAB_INDEX = "VectorUnifiedSearchActivity.EXTRA_TAB_INDEX";
    private static final String LOG_TAG = VectorRoomDetailsActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public Group mGroup;
    private MXEventListener mGroupEventsListener = new MXEventListener() {
        private void refresh(String str) {
            if (VectorGroupDetailsActivity.this.mGroup != null && TextUtils.equals(VectorGroupDetailsActivity.this.mGroup.getGroupId(), str)) {
                VectorGroupDetailsActivity.this.refreshGroupInfo();
            }
        }

        public void onLeaveGroup(String str) {
            if (VectorGroupDetailsActivity.this.mRoom != null && TextUtils.equals(str, VectorGroupDetailsActivity.this.mGroup.getGroupId())) {
                VectorGroupDetailsActivity.this.finish();
            }
        }

        public void onNewGroupInvitation(String str) {
            refresh(str);
        }

        public void onJoinGroup(String str) {
            refresh(str);
        }

        public void onGroupProfileUpdate(String str) {
            if (VectorGroupDetailsActivity.this.mGroup != null && TextUtils.equals(VectorGroupDetailsActivity.this.mGroup.getGroupId(), str) && VectorGroupDetailsActivity.this.mPagerAdapter.getHomeFragment() != null) {
                VectorGroupDetailsActivity.this.mPagerAdapter.getHomeFragment().refreshViews();
            }
        }

        public void onGroupRoomsListUpdate(String str) {
            if (VectorGroupDetailsActivity.this.mGroup != null && TextUtils.equals(VectorGroupDetailsActivity.this.mGroup.getGroupId(), str)) {
                if (VectorGroupDetailsActivity.this.mPagerAdapter.getRoomsFragment() != null) {
                    VectorGroupDetailsActivity.this.mPagerAdapter.getRoomsFragment().refreshViews();
                }
                if (VectorGroupDetailsActivity.this.mPagerAdapter.getHomeFragment() != null) {
                    VectorGroupDetailsActivity.this.mPagerAdapter.getHomeFragment().refreshViews();
                }
            }
        }

        public void onGroupUsersListUpdate(String str) {
            if (VectorGroupDetailsActivity.this.mGroup != null && TextUtils.equals(VectorGroupDetailsActivity.this.mGroup.getGroupId(), str)) {
                if (VectorGroupDetailsActivity.this.mPagerAdapter.getPeopleFragment() != null) {
                    VectorGroupDetailsActivity.this.mPagerAdapter.getPeopleFragment().refreshViews();
                }
                if (VectorGroupDetailsActivity.this.mPagerAdapter.getHomeFragment() != null) {
                    VectorGroupDetailsActivity.this.mPagerAdapter.getHomeFragment().refreshViews();
                }
            }
        }

        public void onGroupInvitedUsersListUpdate(String str) {
            onGroupUsersListUpdate(str);
        }
    };
    /* access modifiers changed from: private */
    public ProgressBar mGroupSyncInProgress;
    private GroupsManager mGroupsManager;
    private RiotViewPager mPager;
    /* access modifiers changed from: private */
    public GroupDetailsFragmentPagerAdapter mPagerAdapter;
    private MXSession mSession;

    public int getLayoutRes() {
        return R.layout.activity_vector_group_details;
    }

    public ActivityOtherThemes getOtherThemes() {
        return ActivityOtherThemes.Group.INSTANCE;
    }

    public void initUiAndData() {
        if (CommonActivityUtils.shouldRestartApp(this)) {
            Log.e(LOG_TAG, "Restart the application.");
            CommonActivityUtils.restartApp(this);
        } else if (CommonActivityUtils.isGoingToSplash(this)) {
            Log.d(LOG_TAG, "onCreate : Going to splash screen");
        } else {
            Intent intent = getIntent();
            String str = EXTRA_GROUP_ID;
            if (!intent.hasExtra(str)) {
                Log.e(LOG_TAG, "No group id");
                finish();
                return;
            }
            this.mSession = Matrix.getInstance(getApplicationContext()).getSession(intent.getStringExtra("MXCActionBarActivity.EXTRA_MATRIX_ID"));
            MXSession mXSession = this.mSession;
            if (mXSession == null || !mXSession.isAlive()) {
                finish();
                return;
            }
            this.mGroupsManager = this.mSession.getGroupsManager();
            String stringExtra = intent.getStringExtra(str);
            if (!MXPatterns.isGroupId(stringExtra)) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("invalid group id ");
                sb.append(stringExtra);
                Log.e(str2, sb.toString());
                finish();
                return;
            }
            this.mGroup = this.mGroupsManager.getGroup(stringExtra);
            String str3 = "## onCreate() : displaying ";
            if (this.mGroup == null) {
                String str4 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str3);
                sb2.append(stringExtra);
                sb2.append(" in preview mode");
                Log.d(str4, sb2.toString());
                this.mGroup = new Group(stringExtra);
            } else {
                String str5 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append(stringExtra);
                Log.d(str5, sb3.toString());
            }
            setWaitingView(findViewById(R.id.group_loading_layout));
            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setDisplayShowHomeEnabled(true);
                supportActionBar.setDisplayHomeAsUpEnabled(true);
            }
            this.mGroupSyncInProgress = (ProgressBar) findViewById(R.id.group_sync_in_progress);
            this.mPager = (RiotViewPager) findViewById(R.id.groups_pager);
            this.mPagerAdapter = new GroupDetailsFragmentPagerAdapter(getSupportFragmentManager(), this);
            this.mPager.setAdapter(this.mPagerAdapter);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.group_tabs);
            ThemeUtils.INSTANCE.setTabLayoutTheme(this, tabLayout);
            String str6 = EXTRA_TAB_INDEX;
            int i = 0;
            if (intent.hasExtra(str6)) {
                this.mPager.setCurrentItem(getIntent().getIntExtra(str6, 0));
            } else {
                RiotViewPager riotViewPager = this.mPager;
                if (!isFirstCreation()) {
                    i = getSavedInstanceState().getInt(str6, 0);
                }
                riotViewPager.setCurrentItem(i);
            }
            tabLayout.setupWithViewPager(this.mPager);
            this.mPager.addOnPageChangeListener(new OnPageChangeListener() {
                public void onPageScrollStateChanged(int i) {
                }

                public void onPageSelected(int i) {
                }

                public void onPageScrolled(int i, float f, int i2) {
                    View currentFocus = VectorGroupDetailsActivity.this.getCurrentFocus();
                    if (currentFocus != null) {
                        ((InputMethodManager) VectorGroupDetailsActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                    }
                }
            });
        }
    }

    public Group getGroup() {
        return this.mGroup;
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
        bundle.putInt(EXTRA_TAB_INDEX, this.mPager.getCurrentItem());
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mSession.getDataHandler().removeListener(this.mGroupEventsListener);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        applyScreenshotSecurity();
        refreshGroupInfo();
        this.mSession.getDataHandler().addListener(this.mGroupEventsListener);
    }

    /* access modifiers changed from: private */
    public void refreshGroupInfo() {
        if (this.mGroup != null) {
            this.mGroupSyncInProgress.setVisibility(0);
            this.mGroupsManager.refreshGroupData(this.mGroup, new ApiCallback<Void>() {
                private void onDone() {
                    if (VectorGroupDetailsActivity.this.mGroupSyncInProgress != null) {
                        VectorGroupDetailsActivity.this.mGroupSyncInProgress.setVisibility(8);
                    }
                }

                public void onSuccess(Void voidR) {
                    onDone();
                }

                public void onNetworkError(Exception exc) {
                    onDone();
                }

                public void onMatrixError(MatrixError matrixError) {
                    onDone();
                }

                public void onUnexpectedError(Exception exc) {
                    onDone();
                }
            });
        }
    }
}
