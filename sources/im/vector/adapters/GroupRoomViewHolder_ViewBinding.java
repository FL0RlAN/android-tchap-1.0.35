package im.vector.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class GroupRoomViewHolder_ViewBinding implements Unbinder {
    private GroupRoomViewHolder target;

    public GroupRoomViewHolder_ViewBinding(GroupRoomViewHolder groupRoomViewHolder, View view) {
        this.target = groupRoomViewHolder;
        groupRoomViewHolder.vContactAvatar = (ImageView) Utils.findRequiredViewAsType(view, R.id.contact_avatar, "field 'vContactAvatar'", ImageView.class);
        groupRoomViewHolder.vContactName = (TextView) Utils.findRequiredViewAsType(view, R.id.contact_name, "field 'vContactName'", TextView.class);
        groupRoomViewHolder.vContactDesc = (TextView) Utils.findOptionalViewAsType(view, R.id.contact_desc, "field 'vContactDesc'", TextView.class);
    }

    public void unbind() {
        GroupRoomViewHolder groupRoomViewHolder = this.target;
        if (groupRoomViewHolder != null) {
            this.target = null;
            groupRoomViewHolder.vContactAvatar = null;
            groupRoomViewHolder.vContactName = null;
            groupRoomViewHolder.vContactDesc = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
