package im.vector.fragments;

import android.view.View;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class GroupDetailsPeopleFragment_ViewBinding implements Unbinder {
    private GroupDetailsPeopleFragment target;

    public GroupDetailsPeopleFragment_ViewBinding(GroupDetailsPeopleFragment groupDetailsPeopleFragment, View view) {
        this.target = groupDetailsPeopleFragment;
        groupDetailsPeopleFragment.mRecycler = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.recyclerview, "field 'mRecycler'", RecyclerView.class);
        groupDetailsPeopleFragment.mSearchView = (SearchView) Utils.findRequiredViewAsType(view, R.id.search_view, "field 'mSearchView'", SearchView.class);
    }

    public void unbind() {
        GroupDetailsPeopleFragment groupDetailsPeopleFragment = this.target;
        if (groupDetailsPeopleFragment != null) {
            this.target = null;
            groupDetailsPeopleFragment.mRecycler = null;
            groupDetailsPeopleFragment.mSearchView = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
