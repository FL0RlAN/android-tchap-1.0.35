package im.vector.adapters;

import android.view.View;
import android.widget.Button;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class GroupInvitationViewHolder_ViewBinding extends GroupViewHolder_ViewBinding {
    private GroupInvitationViewHolder target;

    public GroupInvitationViewHolder_ViewBinding(GroupInvitationViewHolder groupInvitationViewHolder, View view) {
        super(groupInvitationViewHolder, view);
        this.target = groupInvitationViewHolder;
        groupInvitationViewHolder.vRejectButton = (Button) Utils.findRequiredViewAsType(view, R.id.group_invite_reject_button, "field 'vRejectButton'", Button.class);
        groupInvitationViewHolder.vJoinButton = (Button) Utils.findRequiredViewAsType(view, R.id.group_invite_join_button, "field 'vJoinButton'", Button.class);
    }

    public void unbind() {
        GroupInvitationViewHolder groupInvitationViewHolder = this.target;
        if (groupInvitationViewHolder != null) {
            this.target = null;
            groupInvitationViewHolder.vRejectButton = null;
            groupInvitationViewHolder.vJoinButton = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
