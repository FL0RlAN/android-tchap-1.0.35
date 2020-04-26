package im.vector.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.HexagonMaskView;

public class RoomViewHolder_ViewBinding implements Unbinder {
    private RoomViewHolder target;

    public RoomViewHolder_ViewBinding(RoomViewHolder roomViewHolder, View view) {
        this.target = roomViewHolder;
        roomViewHolder.vRoomPinFavorite = view.findViewById(R.id.room_pin_ic);
        roomViewHolder.vRoomAvatar = (ImageView) Utils.findOptionalViewAsType(view, R.id.room_avatar, "field 'vRoomAvatar'", ImageView.class);
        roomViewHolder.vRoomAvatarHexagon = (HexagonMaskView) Utils.findOptionalViewAsType(view, R.id.room_avatar_hexagon, "field 'vRoomAvatarHexagon'", HexagonMaskView.class);
        roomViewHolder.vRoomName = (TextView) Utils.findRequiredViewAsType(view, R.id.room_name, "field 'vRoomName'", TextView.class);
        roomViewHolder.vRoomNotificationMute = (ImageView) Utils.findOptionalViewAsType(view, R.id.notification_mute_bell, "field 'vRoomNotificationMute'", ImageView.class);
        roomViewHolder.vRoomDomain = (TextView) Utils.findOptionalViewAsType(view, R.id.room_member_domain, "field 'vRoomDomain'", TextView.class);
        roomViewHolder.vSenderDisplayName = (TextView) Utils.findOptionalViewAsType(view, R.id.sender_name, "field 'vSenderDisplayName'", TextView.class);
        roomViewHolder.vRoomNameServer = (TextView) Utils.findOptionalViewAsType(view, R.id.room_name_server, "field 'vRoomNameServer'", TextView.class);
        roomViewHolder.vRoomLastMessage = (TextView) Utils.findOptionalViewAsType(view, R.id.room_message, "field 'vRoomLastMessage'", TextView.class);
        roomViewHolder.vRoomTimestamp = (TextView) Utils.findOptionalViewAsType(view, R.id.room_update_date, "field 'vRoomTimestamp'", TextView.class);
        roomViewHolder.vRoomUnreadCount = (TextView) Utils.findRequiredViewAsType(view, R.id.room_unread_count, "field 'vRoomUnreadCount'", TextView.class);
        roomViewHolder.vRoomEncryptedIcon = Utils.findRequiredView(view, R.id.room_avatar_encrypted_icon, "field 'vRoomEncryptedIcon'");
        roomViewHolder.vRoomMoreActionClickArea = view.findViewById(R.id.room_more_action_click_area);
        roomViewHolder.vRoomMoreActionAnchor = view.findViewById(R.id.room_more_action_anchor);
    }

    public void unbind() {
        RoomViewHolder roomViewHolder = this.target;
        if (roomViewHolder != null) {
            this.target = null;
            roomViewHolder.vRoomPinFavorite = null;
            roomViewHolder.vRoomAvatar = null;
            roomViewHolder.vRoomAvatarHexagon = null;
            roomViewHolder.vRoomName = null;
            roomViewHolder.vRoomNotificationMute = null;
            roomViewHolder.vRoomDomain = null;
            roomViewHolder.vSenderDisplayName = null;
            roomViewHolder.vRoomNameServer = null;
            roomViewHolder.vRoomLastMessage = null;
            roomViewHolder.vRoomTimestamp = null;
            roomViewHolder.vRoomUnreadCount = null;
            roomViewHolder.vRoomEncryptedIcon = null;
            roomViewHolder.vRoomMoreActionClickArea = null;
            roomViewHolder.vRoomMoreActionAnchor = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
