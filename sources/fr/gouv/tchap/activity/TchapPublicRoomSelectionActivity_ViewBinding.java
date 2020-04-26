package fr.gouv.tchap.activity;

import android.view.View;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;
import im.vector.activity.VectorAppCompatActivity_ViewBinding;

public class TchapPublicRoomSelectionActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private TchapPublicRoomSelectionActivity target;

    public TchapPublicRoomSelectionActivity_ViewBinding(TchapPublicRoomSelectionActivity tchapPublicRoomSelectionActivity) {
        this(tchapPublicRoomSelectionActivity, tchapPublicRoomSelectionActivity.getWindow().getDecorView());
    }

    public TchapPublicRoomSelectionActivity_ViewBinding(TchapPublicRoomSelectionActivity tchapPublicRoomSelectionActivity, View view) {
        super(tchapPublicRoomSelectionActivity, view);
        this.target = tchapPublicRoomSelectionActivity;
        tchapPublicRoomSelectionActivity.waitingView = Utils.findRequiredView(view, R.id.listView_spinner_views, "field 'waitingView'");
        tchapPublicRoomSelectionActivity.mDrawerLayout = (DrawerLayout) Utils.findRequiredViewAsType(view, R.id.drawer_layout_public_room, "field 'mDrawerLayout'", DrawerLayout.class);
        tchapPublicRoomSelectionActivity.mSearchView = (SearchView) Utils.findRequiredViewAsType(view, R.id.search_view, "field 'mSearchView'", SearchView.class);
    }

    public void unbind() {
        TchapPublicRoomSelectionActivity tchapPublicRoomSelectionActivity = this.target;
        if (tchapPublicRoomSelectionActivity != null) {
            this.target = null;
            tchapPublicRoomSelectionActivity.waitingView = null;
            tchapPublicRoomSelectionActivity.mDrawerLayout = null;
            tchapPublicRoomSelectionActivity.mSearchView = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
