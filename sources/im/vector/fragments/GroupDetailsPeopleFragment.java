package im.vector.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter.FilterListener;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import fr.gouv.tchap.a.R;
import im.vector.adapters.GroupDetailsPeopleAdapter;
import im.vector.adapters.GroupDetailsPeopleAdapter.OnSelectUserListener;
import im.vector.util.GroupUtils;
import im.vector.view.EmptyViewItemDecoration;
import im.vector.view.SimpleDividerItemDecoration;
import org.matrix.androidsdk.rest.model.group.GroupUser;

public class GroupDetailsPeopleFragment extends GroupDetailsBaseFragment {
    /* access modifiers changed from: private */
    public GroupDetailsPeopleAdapter mAdapter;
    /* access modifiers changed from: private */
    public String mCurrentFilter;
    @BindView(2131296894)
    RecyclerView mRecycler;
    @BindView(2131297008)
    SearchView mSearchView;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_group_details_people, viewGroup, false);
    }

    public void onResume() {
        super.onResume();
        refreshViews();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mCurrentFilter = this.mSearchView.getQuery().toString();
        this.mAdapter.onFilterDone(this.mCurrentFilter);
    }

    /* access modifiers changed from: protected */
    public void initViews() {
        int dimension = (int) getResources().getDimension(R.dimen.item_decoration_left_margin);
        this.mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), 1, false));
        this.mRecycler.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), 1, dimension));
        RecyclerView recyclerView = this.mRecycler;
        EmptyViewItemDecoration emptyViewItemDecoration = new EmptyViewItemDecoration(getActivity(), 1, 40, 16, 14);
        recyclerView.addItemDecoration(emptyViewItemDecoration);
        this.mAdapter = new GroupDetailsPeopleAdapter(getActivity(), new OnSelectUserListener() {
            public void onSelectItem(GroupUser groupUser, int i) {
                GroupUtils.openGroupUserPage(GroupDetailsPeopleFragment.this.mActivity, GroupDetailsPeopleFragment.this.mSession, groupUser);
            }
        });
        this.mRecycler.setAdapter(this.mAdapter);
        this.mSearchView.setOnQueryTextListener(new OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return true;
            }

            public boolean onQueryTextChange(final String str) {
                if (!TextUtils.equals(GroupDetailsPeopleFragment.this.mCurrentFilter, str)) {
                    GroupDetailsPeopleFragment.this.mAdapter.getFilter().filter(str, new FilterListener() {
                        public void onFilterComplete(int i) {
                            GroupDetailsPeopleFragment.this.mCurrentFilter = str;
                        }
                    });
                }
                return true;
            }
        });
        this.mSearchView.setMaxWidth(Integer.MAX_VALUE);
        this.mSearchView.setQueryHint(getString(R.string.filter_group_members));
        this.mSearchView.setFocusable(false);
        this.mSearchView.setIconifiedByDefault(false);
        this.mSearchView.clearFocus();
    }

    public void refreshViews() {
        if (isAdded()) {
            this.mAdapter.setJoinedGroupUsers(this.mActivity.getGroup().getGroupUsers().getUsers());
            this.mAdapter.setInvitedGroupUsers(this.mActivity.getGroup().getInvitedGroupUsers().getUsers());
        }
    }
}
