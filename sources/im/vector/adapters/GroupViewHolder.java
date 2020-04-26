package im.vector.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import fr.gouv.tchap.a.R;
import im.vector.adapters.AbsAdapter.GroupInvitationListener;
import im.vector.adapters.AbsAdapter.MoreGroupActionListener;
import im.vector.util.VectorUtils;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.rest.model.group.Group;

public class GroupViewHolder extends ViewHolder {
    private static final String LOG_TAG = GroupViewHolder.class.getSimpleName();
    @BindView(2131296923)
    ImageView vGroupAvatar;
    @BindView(2131296555)
    TextView vGroupMembersCount;
    @BindView(2131296559)
    View vGroupMoreActionAnchor;
    @BindView(2131296560)
    View vGroupMoreActionClickArea;
    @BindView(2131296562)
    TextView vGroupName;
    @BindView(2131296568)
    TextView vGroupTopic;

    public GroupViewHolder(View view) {
        super(view);
        ButterKnife.bind((Object) this, view);
    }

    public void populateViews(Context context, MXSession mXSession, final Group group, GroupInvitationListener groupInvitationListener, boolean z, final MoreGroupActionListener moreGroupActionListener) {
        if (group == null) {
            Log.e(LOG_TAG, "## populateViews() : null group");
            return;
        }
        if (z) {
            this.vGroupMembersCount.setText("!");
            this.vGroupMembersCount.setTypeface(null, 1);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(0);
            gradientDrawable.setCornerRadius(100.0f);
            gradientDrawable.setColor(ContextCompat.getColor(context, R.color.vector_fuchsia_color));
            this.vGroupMembersCount.setBackground(gradientDrawable);
            this.vGroupMembersCount.setVisibility(0);
        } else {
            this.vGroupMembersCount.setVisibility(8);
        }
        this.vGroupName.setText(group.getDisplayName());
        this.vGroupName.setTypeface(null, 0);
        VectorUtils.loadGroupAvatar(context, mXSession, this.vGroupAvatar, group);
        this.vGroupTopic.setText(group.getShortDescription());
        View view = this.vGroupMoreActionClickArea;
        if (!(view == null || this.vGroupMoreActionAnchor == null)) {
            view.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    MoreGroupActionListener moreGroupActionListener = moreGroupActionListener;
                    if (moreGroupActionListener != null) {
                        moreGroupActionListener.onMoreActionClick(GroupViewHolder.this.vGroupMoreActionAnchor, group);
                    }
                }
            });
        }
    }
}
