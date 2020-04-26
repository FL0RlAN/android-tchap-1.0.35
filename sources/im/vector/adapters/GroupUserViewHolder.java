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
import org.matrix.androidsdk.rest.model.group.GroupUser;

public class GroupUserViewHolder extends ViewHolder {
    private static final String LOG_TAG = GroupUserViewHolder.class.getSimpleName();
    @BindView(2131296396)
    ImageView vContactAvatar;
    @BindView(2131296401)
    TextView vContactName;

    public GroupUserViewHolder(View view) {
        super(view);
        ButterKnife.bind((Object) this, view);
    }

    public void populateViews(Context context, MXSession mXSession, GroupUser groupUser) {
        if (groupUser == null) {
            Log.e(LOG_TAG, "## populateViews() : null groupUser");
        } else if (mXSession == null) {
            Log.e(LOG_TAG, "## populateViews() : null session");
        } else if (mXSession.getDataHandler() == null) {
            Log.e(LOG_TAG, "## populateViews() : null dataHandler");
        } else {
            this.vContactName.setText(groupUser.getDisplayname());
            VectorUtils.loadUserAvatar(context, mXSession, this.vContactAvatar, groupUser.avatarUrl, groupUser.userId, groupUser.getDisplayname());
        }
    }
}
