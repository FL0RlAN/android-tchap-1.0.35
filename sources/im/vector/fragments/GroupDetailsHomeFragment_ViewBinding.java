package im.vector.fragments;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class GroupDetailsHomeFragment_ViewBinding implements Unbinder {
    private GroupDetailsHomeFragment target;

    public GroupDetailsHomeFragment_ViewBinding(GroupDetailsHomeFragment groupDetailsHomeFragment, View view) {
        this.target = groupDetailsHomeFragment;
        groupDetailsHomeFragment.mGroupAvatar = (ImageView) Utils.findRequiredViewAsType(view, R.id.group_avatar, "field 'mGroupAvatar'", ImageView.class);
        groupDetailsHomeFragment.mGroupNameTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.group_name_text_view, "field 'mGroupNameTextView'", TextView.class);
        groupDetailsHomeFragment.mGroupTopicTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.group_topic_text_view, "field 'mGroupTopicTextView'", TextView.class);
        groupDetailsHomeFragment.mGroupMembersIconView = (ImageView) Utils.findRequiredViewAsType(view, R.id.group_members_icon_view, "field 'mGroupMembersIconView'", ImageView.class);
        groupDetailsHomeFragment.mGroupMembersTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.group_members_text_view, "field 'mGroupMembersTextView'", TextView.class);
        groupDetailsHomeFragment.mGroupRoomsIconView = (ImageView) Utils.findRequiredViewAsType(view, R.id.group_rooms_icon_view, "field 'mGroupRoomsIconView'", ImageView.class);
        groupDetailsHomeFragment.mGroupRoomsTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.group_rooms_text_view, "field 'mGroupRoomsTextView'", TextView.class);
        groupDetailsHomeFragment.mGroupHtmlTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.html_text_view, "field 'mGroupHtmlTextView'", TextView.class);
        groupDetailsHomeFragment.noLongDescriptionTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.no_html_text_view, "field 'noLongDescriptionTextView'", TextView.class);
    }

    public void unbind() {
        GroupDetailsHomeFragment groupDetailsHomeFragment = this.target;
        if (groupDetailsHomeFragment != null) {
            this.target = null;
            groupDetailsHomeFragment.mGroupAvatar = null;
            groupDetailsHomeFragment.mGroupNameTextView = null;
            groupDetailsHomeFragment.mGroupTopicTextView = null;
            groupDetailsHomeFragment.mGroupMembersIconView = null;
            groupDetailsHomeFragment.mGroupMembersTextView = null;
            groupDetailsHomeFragment.mGroupRoomsIconView = null;
            groupDetailsHomeFragment.mGroupRoomsTextView = null;
            groupDetailsHomeFragment.mGroupHtmlTextView = null;
            groupDetailsHomeFragment.noLongDescriptionTextView = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
