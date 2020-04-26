package fr.gouv.tchap.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import im.vector.util.VectorUtils;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.data.Room;

public class TchapRoomAvatarPreference extends HexagonAvatarPreference {
    private Room mRoom;

    public TchapRoomAvatarPreference(Context context) {
        super(context);
    }

    public TchapRoomAvatarPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public TchapRoomAvatarPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void refreshAvatar() {
        if (this.mAvatarView != null && this.mRoom != null) {
            VectorUtils.loadRoomAvatar(getContext(), this.mSession, (ImageView) this.mAvatarView, this.mRoom);
        }
    }

    public void setConfiguration(MXSession mXSession, Room room) {
        this.mSession = mXSession;
        this.mRoom = room;
        refreshAvatar();
    }
}
