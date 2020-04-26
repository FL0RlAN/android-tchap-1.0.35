package im.vector.activity;

import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import fr.gouv.tchap.a.R;
import im.vector.view.VectorPendingCallView;

public class VectorHomeActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private VectorHomeActivity target;
    private View view7f09005c;
    private View view7f09005e;
    private View view7f09005f;
    private View view7f090101;

    public VectorHomeActivity_ViewBinding(VectorHomeActivity vectorHomeActivity) {
        this(vectorHomeActivity, vectorHomeActivity.getWindow().getDecorView());
    }

    public VectorHomeActivity_ViewBinding(final VectorHomeActivity vectorHomeActivity, View view) {
        super(vectorHomeActivity, view);
        this.target = vectorHomeActivity;
        vectorHomeActivity.mFloatingActionsMenu = (FloatingActionsMenu) Utils.findRequiredViewAsType(view, R.id.floating_action_menu, "field 'mFloatingActionsMenu'", FloatingActionsMenu.class);
        vectorHomeActivity.mFabMain = (AddFloatingActionButton) Utils.findRequiredViewAsType(view, R.id.fab_expand_menu_button, "field 'mFabMain'", AddFloatingActionButton.class);
        View findRequiredView = Utils.findRequiredView(view, R.id.button_start_chat, "field 'mFabStartChat' and method 'fabMenuStartChat'");
        vectorHomeActivity.mFabStartChat = (FloatingActionButton) Utils.castView(findRequiredView, R.id.button_start_chat, "field 'mFabStartChat'", FloatingActionButton.class);
        this.view7f09005f = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                vectorHomeActivity.fabMenuStartChat();
            }
        });
        View findRequiredView2 = Utils.findRequiredView(view, R.id.button_create_room, "field 'mFabCreateRoom' and method 'fabMenuCreateRoom'");
        vectorHomeActivity.mFabCreateRoom = (FloatingActionButton) Utils.castView(findRequiredView2, R.id.button_create_room, "field 'mFabCreateRoom'", FloatingActionButton.class);
        this.view7f09005c = findRequiredView2;
        findRequiredView2.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                vectorHomeActivity.fabMenuCreateRoom();
            }
        });
        View findRequiredView3 = Utils.findRequiredView(view, R.id.button_join_room, "field 'mFabJoinRoom' and method 'fabMenuJoinRoom'");
        vectorHomeActivity.mFabJoinRoom = (FloatingActionButton) Utils.castView(findRequiredView3, R.id.button_join_room, "field 'mFabJoinRoom'", FloatingActionButton.class);
        this.view7f09005e = findRequiredView3;
        findRequiredView3.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                vectorHomeActivity.fabMenuJoinRoom();
            }
        });
        vectorHomeActivity.mToolbar = (Toolbar) Utils.findRequiredViewAsType(view, R.id.home_toolbar, "field 'mToolbar'", Toolbar.class);
        vectorHomeActivity.mDrawerLayout = (DrawerLayout) Utils.findRequiredViewAsType(view, R.id.drawer_layout, "field 'mDrawerLayout'", DrawerLayout.class);
        vectorHomeActivity.mTopNavigationView = (TabLayout) Utils.findRequiredViewAsType(view, R.id.tab_layout, "field 'mTopNavigationView'", TabLayout.class);
        vectorHomeActivity.navigationView = (NavigationView) Utils.findRequiredViewAsType(view, R.id.navigation_view, "field 'navigationView'", NavigationView.class);
        vectorHomeActivity.mVectorPendingCallView = (VectorPendingCallView) Utils.findRequiredViewAsType(view, R.id.listView_pending_callview, "field 'mVectorPendingCallView'", VectorPendingCallView.class);
        vectorHomeActivity.mSyncInProgressView = (ProgressBar) Utils.findRequiredViewAsType(view, R.id.home_recents_sync_in_progress, "field 'mSyncInProgressView'", ProgressBar.class);
        vectorHomeActivity.mSearchView = (SearchView) Utils.findRequiredViewAsType(view, R.id.search_view, "field 'mSearchView'", SearchView.class);
        View findRequiredView4 = Utils.findRequiredView(view, R.id.floating_action_menu_touch_guard, "field 'touchGuard' and method 'touchGuardClicked'");
        vectorHomeActivity.touchGuard = findRequiredView4;
        this.view7f090101 = findRequiredView4;
        findRequiredView4.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                vectorHomeActivity.touchGuardClicked();
            }
        });
    }

    public void unbind() {
        VectorHomeActivity vectorHomeActivity = this.target;
        if (vectorHomeActivity != null) {
            this.target = null;
            vectorHomeActivity.mFloatingActionsMenu = null;
            vectorHomeActivity.mFabMain = null;
            vectorHomeActivity.mFabStartChat = null;
            vectorHomeActivity.mFabCreateRoom = null;
            vectorHomeActivity.mFabJoinRoom = null;
            vectorHomeActivity.mToolbar = null;
            vectorHomeActivity.mDrawerLayout = null;
            vectorHomeActivity.mTopNavigationView = null;
            vectorHomeActivity.navigationView = null;
            vectorHomeActivity.mVectorPendingCallView = null;
            vectorHomeActivity.mSyncInProgressView = null;
            vectorHomeActivity.mSearchView = null;
            vectorHomeActivity.touchGuard = null;
            this.view7f09005f.setOnClickListener(null);
            this.view7f09005f = null;
            this.view7f09005c.setOnClickListener(null);
            this.view7f09005c = null;
            this.view7f09005e.setOnClickListener(null);
            this.view7f09005e = null;
            this.view7f090101.setOnClickListener(null);
            this.view7f090101 = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
