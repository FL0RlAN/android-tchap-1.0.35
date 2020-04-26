package im.vector.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import im.vector.util.VectorUtils;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.rest.model.group.GroupRoom;

public class GroupRoomViewHolder extends ViewHolder {
    private static final String LOG_TAG = GroupRoomViewHolder.class.getSimpleName();
    @BindView(2131296396)
    ImageView vContactAvatar;
    @BindView(2131296399)
    TextView vContactDesc;
    @BindView(2131296401)
    TextView vContactName;

    public GroupRoomViewHolder(View view) {
        super(view);
        ButterKnife.bind((Object) this, view);
    }

    public void populateViews(Context context, MXSession mXSession, GroupRoom groupRoom) {
        if (groupRoom == null) {
            Log.e(LOG_TAG, "## populateViews() : null groupRoom");
        } else if (mXSession == null) {
            Log.e(LOG_TAG, "## populateViews() : null session");
        } else if (mXSession.getDataHandler() == null) {
            Log.e(LOG_TAG, "## populateViews() : null dataHandler");
        } else {
            this.vContactName.setText(groupRoom.getDisplayName());
            VectorUtils.loadUserAvatar(context, mXSession, this.vContactAvatar, groupRoom.avatarUrl, groupRoom.roomId, groupRoom.getDisplayName());
            TextView textView = this.vContactDesc;
            if (textView != null) {
                textView.setText(groupRoom.topic);
            }
        }
    }
}
