package fr.gouv.tchap.activity;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.fragments.TchapPublicRoomsFragment;
import im.vector.Matrix;
import im.vector.MyPresenceManager;
import im.vector.PublicRoomsManager;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.VectorAppCompatActivity;
import org.apache.commons.cli.HelpFormatter;
import org.matrix.androidsdk.MXSession;

public class TchapPublicRoomSelectionActivity extends VectorAppCompatActivity implements OnQueryTextListener {
    private static final String LOG_TAG = TchapPublicRoomSelectionActivity.class.getSimpleName();
    private static final String TAG_FRAGMENT_ROOMS = "TAG_FRAGMENT_ROOMS";
    private static TchapPublicRoomSelectionActivity sharedInstance = null;
    private final BroadcastReceiver mBrdRcvStopWaitingView = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            TchapPublicRoomSelectionActivity.this.hideWaitingView();
        }
    };
    @BindView(2131296454)
    DrawerLayout mDrawerLayout;
    private FragmentManager mFragmentManager;
    @BindView(2131297008)
    SearchView mSearchView;
    private MXSession mSession;
    @BindView(2131296687)
    View waitingView;

    public int getLayoutRes() {
        return R.layout.activity_tchap_public_room;
    }

    public boolean onQueryTextSubmit(String str) {
        return true;
    }

    public static TchapPublicRoomSelectionActivity getInstance() {
        return sharedInstance;
    }

    public void initUiAndData() {
        this.mFragmentManager = getSupportFragmentManager();
        if (isFirstCreation()) {
            this.mFragmentManager.beginTransaction().add(R.id.fragment_container, TchapPublicRoomsFragment.newInstance(), TAG_FRAGMENT_ROOMS).commit();
        }
        setTitle(R.string.room_join_public_room_alt_title);
        setWaitingView(this.waitingView);
        sharedInstance = this;
        this.mSession = Matrix.getInstance(this).getDefaultSession();
        PublicRoomsManager.getInstance().setSession(this.mSession);
        initViews();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        MyPresenceManager.createPresenceManager(this, Matrix.getInstance(this).getSessions());
        MyPresenceManager.advertiseAllOnline();
        getIntent();
    }

    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerVisible((int) GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer((int) GravityCompat.START);
        } else if (!TextUtils.isEmpty(this.mSearchView.getQuery().toString())) {
            this.mSearchView.setQuery("", true);
        } else {
            this.mFragmentManager.popBackStack((String) null, 1);
            super.onBackPressed();
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        CommonActivityUtils.onLowMemory(this);
    }

    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
        CommonActivityUtils.onTrimMemory(this, i);
    }

    private void initViews() {
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
                    TchapPublicRoomSelectionActivity.this.mSearchView.setIconified(false);
                }
            }
        });
    }

    private void resetFilter() {
        this.mSearchView.setQuery("", false);
        this.mSearchView.clearFocus();
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
        final String sb2 = sb.toString();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public void run() {
                String charSequence = TchapPublicRoomSelectionActivity.this.mSearchView.getQuery().toString();
                StringBuilder sb = new StringBuilder();
                sb.append(charSequence);
                sb.append(HelpFormatter.DEFAULT_OPT_PREFIX);
                if (TextUtils.equals(sb.toString(), sb2)) {
                    TchapPublicRoomSelectionActivity.this.applyFilter(charSequence);
                }
            }
        }, 500);
        return true;
    }

    /* access modifiers changed from: private */
    public void applyFilter(String str) {
        Fragment findFragmentByTag = this.mFragmentManager.findFragmentByTag(TAG_FRAGMENT_ROOMS);
        if (findFragmentByTag instanceof TchapPublicRoomsFragment) {
            ((TchapPublicRoomsFragment) findFragmentByTag).applyFilter(str.trim());
        }
    }
}
