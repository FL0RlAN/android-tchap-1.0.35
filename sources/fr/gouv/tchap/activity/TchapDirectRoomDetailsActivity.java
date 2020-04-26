package fr.gouv.tchap.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.ActionBar.Tab;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.Matrix;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.VectorHomeActivity;
import im.vector.fragments.VectorSearchRoomFilesListFragment;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.fragments.MatrixMessageListFragment.OnSearchResultListener;
import org.matrix.androidsdk.listeners.MXEventListener;

public class TchapDirectRoomDetailsActivity extends TchapContactActionBarActivity {
    public static final String EXTRA_ROOM_ID = "TchapRoomDetailsActivity.EXTRA_ROOM_ID";
    private static final String LOG_TAG = TchapDirectRoomDetailsActivity.class.getSimpleName();
    private static final String TAG_FRAGMENT_FILES_DETAILS = "im.vector.activity.TAG_FRAGMENT_FILES_DETAILS";
    private final MXEventListener mEventListener = new MXEventListener() {
        public void onLeaveRoom(String str) {
            TchapDirectRoomDetailsActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Intent intent = new Intent(TchapDirectRoomDetailsActivity.this, VectorHomeActivity.class);
                    intent.setFlags(603979776);
                    TchapDirectRoomDetailsActivity.this.startActivity(intent);
                }
            });
        }
    };
    private FragmentManager mFragmentManager;
    private View mLoadOldestContentView;
    private String mMatrixId;
    private String mRoomId;
    private VectorSearchRoomFilesListFragment mSearchFilesFragment;
    private Toolbar mToolbar;

    public int getLayoutRes() {
        return R.layout.activity_tchap_direct_room_details;
    }

    public void initUiAndData() {
        super.initUiAndData();
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
            setWaitingView(findViewById(R.id.settings_loading_layout));
            this.mLoadOldestContentView = findViewById(R.id.search_load_oldest_progress);
            this.mToolbar = (Toolbar) findViewById(R.id.room_toolbar);
            setSupportActionBar(this.mToolbar);
            this.mFragmentManager = getSupportFragmentManager();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            setTitle();
            setTopic();
            setAvatar();
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            String str3 = TAG_FRAGMENT_FILES_DETAILS;
            this.mSearchFilesFragment = (VectorSearchRoomFilesListFragment) supportFragmentManager.findFragmentByTag(str3);
            FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
            VectorSearchRoomFilesListFragment vectorSearchRoomFilesListFragment = this.mSearchFilesFragment;
            if (vectorSearchRoomFilesListFragment == null) {
                this.mSearchFilesFragment = VectorSearchRoomFilesListFragment.newInstance(this.mSession.getCredentials().userId, this.mRoomId, R.layout.fragment_matrix_message_list_fragment);
                beginTransaction.replace(R.id.room_details_fragment_container, this.mSearchFilesFragment, str3).commit();
                Log.d(LOG_TAG, "## TchapDirectRoomDetailsActivity init file frag replace");
            } else {
                beginTransaction.attach(vectorSearchRoomFilesListFragment).commit();
                Log.d(LOG_TAG, "## TchapDirectRoomDetailsActivity init file frag attach");
            }
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
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        finish();
        return true;
    }

    /* access modifiers changed from: protected */
    public void setTitle() {
        super.setTitle((this.mSession == null || this.mRoom == null) ? "" : DinsicUtils.getNameFromDisplayName(DinsicUtils.getRoomDisplayName(this, this.mRoom)));
    }

    /* access modifiers changed from: protected */
    public void setTopic() {
        if (this.mRoom != null) {
            super.setTopic(DinsicUtils.getDomainFromDisplayName(DinsicUtils.getRoomDisplayName(this, this.mRoom)));
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
        if (!this.mSession.isAlive()) {
            return;
        }
        if (this.mRoom.getMember(this.mSession.getMyUserId()) == null || !this.mSession.getDataHandler().doesRoomExist(this.mRoom.getRoomId())) {
            Intent intent = new Intent(this, VectorHomeActivity.class);
            intent.setFlags(603979776);
            startActivity(intent);
            return;
        }
        this.mRoom.addEventListener(this.mEventListener);
        startFileSearch();
    }

    private void saveUiTabContext(Tab tab) {
        tab.setTag((Bundle) tab.getTag());
    }

    private void resetUi() {
        hideWaitingView();
        View view = this.mLoadOldestContentView;
        if (view != null) {
            view.setVisibility(8);
        }
    }

    private void startFileSearch() {
        showWaitingView();
        this.mSearchFilesFragment.startFilesSearch(new OnSearchResultListener() {
            public void onSearchSucceed(int i) {
                TchapDirectRoomDetailsActivity.this.onSearchEnd(i);
            }

            public void onSearchFailed() {
                TchapDirectRoomDetailsActivity.this.onSearchEnd(0);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onSearchEnd(int i) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onSearchEnd() nbrMsg=");
        sb.append(i);
        Log.d(str, sb.toString());
        hideWaitingView();
    }
}
