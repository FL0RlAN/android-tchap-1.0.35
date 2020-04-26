package im.vector.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class GroupUserViewHolder_ViewBinding implements Unbinder {
    private GroupUserViewHolder target;

    public GroupUserViewHolder_ViewBinding(GroupUserViewHolder groupUserViewHolder, View view) {
        this.target = groupUserViewHolder;
        groupUserViewHolder.vContactAvatar = (ImageView) Utils.findRequiredViewAsType(view, R.id.contact_avatar, "field 'vContactAvatar'", ImageView.class);
        groupUserViewHolder.vContactName = (TextView) Utils.findRequiredViewAsType(view, R.id.contact_name, "field 'vContactName'", TextView.class);
    }

    public void unbind() {
        GroupUserViewHolder groupUserViewHolder = this.target;
        if (groupUserViewHolder != null) {
            this.target = null;
            groupUserViewHolder.vContactAvatar = null;
            groupUserViewHolder.vContactName = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
