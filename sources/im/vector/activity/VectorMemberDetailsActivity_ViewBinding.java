package im.vector.activity;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class VectorMemberDetailsActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private VectorMemberDetailsActivity target;

    public VectorMemberDetailsActivity_ViewBinding(VectorMemberDetailsActivity vectorMemberDetailsActivity) {
        this(vectorMemberDetailsActivity, vectorMemberDetailsActivity.getWindow().getDecorView());
    }

    public VectorMemberDetailsActivity_ViewBinding(VectorMemberDetailsActivity vectorMemberDetailsActivity, View view) {
        super(vectorMemberDetailsActivity, view);
        this.target = vectorMemberDetailsActivity;
        vectorMemberDetailsActivity.mMemberAvatarImageView = (ImageView) Utils.findRequiredViewAsType(view, R.id.avatar_img, "field 'mMemberAvatarImageView'", ImageView.class);
        vectorMemberDetailsActivity.mPresenceTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.member_details_presence, "field 'mPresenceTextView'", TextView.class);
        vectorMemberDetailsActivity.mFullMemberAvatarLayout = Utils.findRequiredView(view, R.id.member_details_fullscreen_avatar_layout, "field 'mFullMemberAvatarLayout'");
        vectorMemberDetailsActivity.mFullMemberAvatarImageView = (ImageView) Utils.findRequiredViewAsType(view, R.id.member_details_fullscreen_avatar_image_view, "field 'mFullMemberAvatarImageView'", ImageView.class);
        vectorMemberDetailsActivity.mExpandableListView = (ExpandableListView) Utils.findRequiredViewAsType(view, R.id.member_details_actions_list_view, "field 'mExpandableListView'", ExpandableListView.class);
        vectorMemberDetailsActivity.mDevicesListView = (ListView) Utils.findRequiredViewAsType(view, R.id.member_details_devices_list_view, "field 'mDevicesListView'", ListView.class);
        vectorMemberDetailsActivity.mDevicesListHeaderView = Utils.findRequiredView(view, R.id.devices_header_view, "field 'mDevicesListHeaderView'");
    }

    public void unbind() {
        VectorMemberDetailsActivity vectorMemberDetailsActivity = this.target;
        if (vectorMemberDetailsActivity != null) {
            this.target = null;
            vectorMemberDetailsActivity.mMemberAvatarImageView = null;
            vectorMemberDetailsActivity.mPresenceTextView = null;
            vectorMemberDetailsActivity.mFullMemberAvatarLayout = null;
            vectorMemberDetailsActivity.mFullMemberAvatarImageView = null;
            vectorMemberDetailsActivity.mExpandableListView = null;
            vectorMemberDetailsActivity.mDevicesListView = null;
            vectorMemberDetailsActivity.mDevicesListHeaderView = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
