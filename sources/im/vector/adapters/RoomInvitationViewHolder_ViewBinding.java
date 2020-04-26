package im.vector.adapters;

import android.view.View;
import android.widget.Button;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class RoomInvitationViewHolder_ViewBinding extends RoomViewHolder_ViewBinding {
    private RoomInvitationViewHolder target;

    public RoomInvitationViewHolder_ViewBinding(RoomInvitationViewHolder roomInvitationViewHolder, View view) {
        super(roomInvitationViewHolder, view);
        this.target = roomInvitationViewHolder;
        roomInvitationViewHolder.vRejectButton = (Button) Utils.findRequiredViewAsType(view, R.id.room_invite_reject_button, "field 'vRejectButton'", Button.class);
        roomInvitationViewHolder.vJoinButton = (Button) Utils.findRequiredViewAsType(view, R.id.room_invite_join_button, "field 'vJoinButton'", Button.class);
    }

    public void unbind() {
        RoomInvitationViewHolder roomInvitationViewHolder = this.target;
        if (roomInvitationViewHolder != null) {
            this.target = null;
            roomInvitationViewHolder.vRejectButton = null;
            roomInvitationViewHolder.vJoinButton = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
