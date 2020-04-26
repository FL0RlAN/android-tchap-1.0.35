package im.vector.fragments;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import fr.gouv.tchap.a.R;
import im.vector.ui.themes.ThemeUtils;
import im.vector.util.VectorImageGetter;
import im.vector.util.VectorImageGetter.OnImageDownloadListener;
import im.vector.util.VectorUtils;
import org.matrix.androidsdk.rest.model.group.Group;

public class GroupDetailsHomeFragment extends GroupDetailsBaseFragment {
    private static final String LOG_TAG = GroupDetailsHomeFragment.class.getSimpleName();
    @BindView(2131296548)
    ImageView mGroupAvatar;
    @BindView(2131296590)
    TextView mGroupHtmlTextView;
    @BindView(2131296556)
    ImageView mGroupMembersIconView;
    @BindView(2131296558)
    TextView mGroupMembersTextView;
    @BindView(2131296563)
    TextView mGroupNameTextView;
    @BindView(2131296564)
    ImageView mGroupRoomsIconView;
    @BindView(2131296565)
    TextView mGroupRoomsTextView;
    @BindView(2131296569)
    TextView mGroupTopicTextView;
    private VectorImageGetter mImageGetter;
    @BindView(2131296831)
    TextView noLongDescriptionTextView;

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (this.mImageGetter == null) {
            this.mImageGetter = new VectorImageGetter(this.mSession);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_group_details_home, viewGroup, false);
    }

    public void onPause() {
        super.onPause();
        this.mImageGetter.setListener(null);
    }

    public void onResume() {
        super.onResume();
        refreshViews();
        this.mImageGetter.setListener(new OnImageDownloadListener() {
            public void onImageDownloaded(String str) {
                GroupDetailsHomeFragment.this.refreshLongDescription();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void initViews() {
        this.mGroupMembersIconView.setImageDrawable(ThemeUtils.INSTANCE.tintDrawableWithColor(ContextCompat.getDrawable(this.mActivity, R.drawable.riot_tab_groups), this.mGroupMembersTextView.getCurrentTextColor()));
        this.mGroupRoomsIconView.setImageDrawable(ThemeUtils.INSTANCE.tintDrawableWithColor(ContextCompat.getDrawable(this.mActivity, R.drawable.riot_tab_rooms), this.mGroupMembersTextView.getCurrentTextColor()));
    }

    public void refreshViews() {
        if (isAdded()) {
            Group group = this.mActivity.getGroup();
            VectorUtils.loadGroupAvatar(this.mActivity, this.mSession, this.mGroupAvatar, group);
            this.mGroupNameTextView.setText(group.getDisplayName());
            this.mGroupTopicTextView.setText(group.getShortDescription());
            TextView textView = this.mGroupTopicTextView;
            textView.setVisibility(TextUtils.isEmpty(textView.getText()) ? 8 : 0);
            int estimatedRoomCount = group.getGroupRooms() != null ? group.getGroupRooms().getEstimatedRoomCount() : 0;
            int estimatedUsersCount = group.getGroupUsers() != null ? group.getGroupUsers().getEstimatedUsersCount() : 1;
            this.mGroupRoomsTextView.setText(getResources().getQuantityString(R.plurals.group_rooms, estimatedRoomCount, new Object[]{Integer.valueOf(estimatedRoomCount)}));
            this.mGroupMembersTextView.setText(getResources().getQuantityString(R.plurals.group_members, estimatedUsersCount, new Object[]{Integer.valueOf(estimatedUsersCount)}));
            if (!TextUtils.isEmpty(group.getLongDescription())) {
                this.mGroupHtmlTextView.setVisibility(0);
                refreshLongDescription();
                this.noLongDescriptionTextView.setVisibility(8);
            } else {
                this.noLongDescriptionTextView.setVisibility(0);
                this.mGroupHtmlTextView.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: private */
    public void refreshLongDescription() {
        if (this.mGroupHtmlTextView != null) {
            Group group = this.mActivity.getGroup();
            if (VERSION.SDK_INT >= 24) {
                this.mGroupHtmlTextView.setText(Html.fromHtml(group.getLongDescription(), 0, this.mImageGetter, null));
            } else {
                this.mGroupHtmlTextView.setText(Html.fromHtml(group.getLongDescription(), this.mImageGetter, null));
            }
        }
    }
}
