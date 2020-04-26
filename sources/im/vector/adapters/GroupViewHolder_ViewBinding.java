package im.vector.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class GroupViewHolder_ViewBinding implements Unbinder {
    private GroupViewHolder target;

    public GroupViewHolder_ViewBinding(GroupViewHolder groupViewHolder, View view) {
        this.target = groupViewHolder;
        groupViewHolder.vGroupAvatar = (ImageView) Utils.findRequiredViewAsType(view, R.id.room_avatar, "field 'vGroupAvatar'", ImageView.class);
        groupViewHolder.vGroupName = (TextView) Utils.findRequiredViewAsType(view, R.id.group_name, "field 'vGroupName'", TextView.class);
        groupViewHolder.vGroupTopic = (TextView) Utils.findOptionalViewAsType(view, R.id.group_topic, "field 'vGroupTopic'", TextView.class);
        groupViewHolder.vGroupMembersCount = (TextView) Utils.findRequiredViewAsType(view, R.id.group_members_count, "field 'vGroupMembersCount'", TextView.class);
        groupViewHolder.vGroupMoreActionClickArea = view.findViewById(R.id.group_more_action_click_area);
        groupViewHolder.vGroupMoreActionAnchor = view.findViewById(R.id.group_more_action_anchor);
    }

    public void unbind() {
        GroupViewHolder groupViewHolder = this.target;
        if (groupViewHolder != null) {
            this.target = null;
            groupViewHolder.vGroupAvatar = null;
            groupViewHolder.vGroupName = null;
            groupViewHolder.vGroupTopic = null;
            groupViewHolder.vGroupMembersCount = null;
            groupViewHolder.vGroupMoreActionClickArea = null;
            groupViewHolder.vGroupMoreActionAnchor = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
