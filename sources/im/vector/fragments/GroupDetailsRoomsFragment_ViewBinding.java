package im.vector.fragments;

import android.view.View;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class GroupDetailsRoomsFragment_ViewBinding implements Unbinder {
    private GroupDetailsRoomsFragment target;

    public GroupDetailsRoomsFragment_ViewBinding(GroupDetailsRoomsFragment groupDetailsRoomsFragment, View view) {
        this.target = groupDetailsRoomsFragment;
        groupDetailsRoomsFragment.mRecycler = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.recyclerview, "field 'mRecycler'", RecyclerView.class);
        groupDetailsRoomsFragment.mSearchView = (SearchView) Utils.findRequiredViewAsType(view, R.id.search_view, "field 'mSearchView'", SearchView.class);
    }

    public void unbind() {
        GroupDetailsRoomsFragment groupDetailsRoomsFragment = this.target;
        if (groupDetailsRoomsFragment != null) {
            this.target = null;
            groupDetailsRoomsFragment.mRecycler = null;
            groupDetailsRoomsFragment.mSearchView = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
