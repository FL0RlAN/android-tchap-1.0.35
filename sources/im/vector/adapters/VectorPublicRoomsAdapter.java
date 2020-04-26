package im.vector.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.gouv.tchap.a.R;
import im.vector.util.VectorUtils;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom;

public class VectorPublicRoomsAdapter extends ArrayAdapter<PublicRoom> {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater = LayoutInflater.from(this.mContext);
    private final int mLayoutResourceId;
    private final MXSession mSession;

    public VectorPublicRoomsAdapter(Context context, int i, MXSession mXSession) {
        super(context, i);
        this.mContext = context;
        this.mLayoutResourceId = i;
        this.mSession = mXSession;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = this.mLayoutInflater.inflate(this.mLayoutResourceId, viewGroup, false);
        }
        PublicRoom publicRoom = (PublicRoom) getItem(i);
        String publicRoomDisplayName = !TextUtils.isEmpty(publicRoom.name) ? publicRoom.name : VectorUtils.getPublicRoomDisplayName(publicRoom);
        ImageView imageView = (ImageView) view.findViewById(R.id.room_avatar);
        TextView textView = (TextView) view.findViewById(R.id.roomSummaryAdapter_roomName);
        TextView textView2 = (TextView) view.findViewById(R.id.roomSummaryAdapter_roomMessage);
        TextView textView3 = (TextView) view.findViewById(R.id.roomSummaryAdapter_ts);
        View findViewById = view.findViewById(R.id.recents_separator);
        VectorUtils.loadUserAvatar(this.mContext, this.mSession, imageView, publicRoom.avatarUrl, publicRoom.roomId, publicRoomDisplayName);
        textView2.setText(publicRoom.topic);
        textView.setText(publicRoomDisplayName);
        textView3.setText(this.mContext.getResources().getQuantityString(R.plurals.public_room_nb_users, publicRoom.numJoinedMembers, new Object[]{Integer.valueOf(publicRoom.numJoinedMembers)}));
        findViewById.setVisibility(0);
        view.findViewById(R.id.bing_indicator_unread_message).setVisibility(4);
        view.findViewById(R.id.recents_groups_separator_line).setVisibility(8);
        view.findViewById(R.id.roomSummaryAdapter_action).setVisibility(8);
        view.findViewById(R.id.roomSummaryAdapter_action_image).setVisibility(8);
        view.findViewById(R.id.recents_groups_invitation_group).setVisibility(8);
        return view;
    }
}
