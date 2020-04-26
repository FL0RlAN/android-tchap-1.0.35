package im.vector.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import fr.gouv.tchap.a.R;
import im.vector.fragments.GroupDetailsBaseFragment;
import im.vector.fragments.GroupDetailsHomeFragment;
import im.vector.fragments.GroupDetailsPeopleFragment;
import im.vector.fragments.GroupDetailsRoomsFragment;

public class GroupDetailsFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final int FRAGMENTS_COUNT = 3;
    private static final int HOME_FRAGMENT_INDEX = 0;
    private static final String LOG_TAG = GroupDetailsFragmentPagerAdapter.class.getSimpleName();
    private static final int PEOPLE_FRAGMENT_INDEX = 1;
    private static final int ROOMS_FRAGMENT_INDEX = 2;
    private final Context mContext;
    private GroupDetailsHomeFragment mHomeFragment;
    private GroupDetailsPeopleFragment mPeopleFragment;
    private GroupDetailsRoomsFragment mRoomsFragment;

    public int getCount() {
        return 3;
    }

    public GroupDetailsFragmentPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.mContext = context;
    }

    public Fragment getItem(int i) {
        if (i == 0) {
            GroupDetailsHomeFragment groupDetailsHomeFragment = this.mHomeFragment;
            if (groupDetailsHomeFragment != null) {
                return groupDetailsHomeFragment;
            }
            GroupDetailsHomeFragment groupDetailsHomeFragment2 = new GroupDetailsHomeFragment();
            this.mHomeFragment = groupDetailsHomeFragment2;
            return groupDetailsHomeFragment2;
        } else if (i == 1) {
            GroupDetailsPeopleFragment groupDetailsPeopleFragment = this.mPeopleFragment;
            if (groupDetailsPeopleFragment != null) {
                return groupDetailsPeopleFragment;
            }
            GroupDetailsPeopleFragment groupDetailsPeopleFragment2 = new GroupDetailsPeopleFragment();
            this.mPeopleFragment = groupDetailsPeopleFragment2;
            return groupDetailsPeopleFragment2;
        } else if (i != 2) {
            return null;
        } else {
            GroupDetailsRoomsFragment groupDetailsRoomsFragment = this.mRoomsFragment;
            if (groupDetailsRoomsFragment != null) {
                return groupDetailsRoomsFragment;
            }
            GroupDetailsRoomsFragment groupDetailsRoomsFragment2 = new GroupDetailsRoomsFragment();
            this.mRoomsFragment = groupDetailsRoomsFragment2;
            return groupDetailsRoomsFragment2;
        }
    }

    public CharSequence getPageTitle(int i) {
        if (i == 0) {
            return this.mContext.getString(R.string.group_details_home);
        }
        if (i == 1) {
            return this.mContext.getString(R.string.group_details_people);
        }
        if (i != 2) {
            return super.getPageTitle(i);
        }
        return this.mContext.getString(R.string.group_details_rooms);
    }

    public GroupDetailsBaseFragment getHomeFragment() {
        return this.mHomeFragment;
    }

    public GroupDetailsBaseFragment getPeopleFragment() {
        return this.mPeopleFragment;
    }

    public GroupDetailsBaseFragment getRoomsFragment() {
        return this.mRoomsFragment;
    }
}
