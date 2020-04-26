package im.vector.adapters;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import butterknife.BindView;
import im.vector.adapters.AbsAdapter.MoreRoomActionListener;
import im.vector.adapters.AbsAdapter.RoomInvitationListener;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.data.Room;

public class RoomInvitationViewHolder extends RoomViewHolder {
    @BindView(2131296947)
    Button vJoinButton;
    @BindView(2131296948)
    Button vRejectButton;

    RoomInvitationViewHolder(View view) {
        super(view);
    }

    /* access modifiers changed from: 0000 */
    public void populateViews(Context context, final MXSession mXSession, final Room room, final RoomInvitationListener roomInvitationListener, MoreRoomActionListener moreRoomActionListener) {
        super.populateViews(context, mXSession, room, room.isDirect(), true, moreRoomActionListener);
        this.vJoinButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                RoomInvitationListener roomInvitationListener = roomInvitationListener;
                if (roomInvitationListener != null) {
                    roomInvitationListener.onJoinRoom(mXSession, room.getRoomId());
                }
            }
        });
        this.vRejectButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                RoomInvitationListener roomInvitationListener = roomInvitationListener;
                if (roomInvitationListener != null) {
                    roomInvitationListener.onRejectInvitation(mXSession, room.getRoomId());
                }
            }
        });
    }
}
