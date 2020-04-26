package im.vector.adapters;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import butterknife.BindView;
import im.vector.adapters.AbsAdapter.GroupInvitationListener;
import im.vector.adapters.AbsAdapter.MoreGroupActionListener;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.rest.model.group.Group;

public class GroupInvitationViewHolder extends GroupViewHolder {
    @BindView(2131296552)
    Button vJoinButton;
    @BindView(2131296553)
    Button vRejectButton;

    GroupInvitationViewHolder(View view) {
        super(view);
    }

    public void populateViews(Context context, final MXSession mXSession, final Group group, final GroupInvitationListener groupInvitationListener, boolean z, MoreGroupActionListener moreGroupActionListener) {
        super.populateViews(context, mXSession, group, groupInvitationListener, true, moreGroupActionListener);
        this.vJoinButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                GroupInvitationListener groupInvitationListener = groupInvitationListener;
                if (groupInvitationListener != null) {
                    groupInvitationListener.onJoinGroup(mXSession, group.getGroupId());
                }
            }
        });
        this.vRejectButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                GroupInvitationListener groupInvitationListener = groupInvitationListener;
                if (groupInvitationListener != null) {
                    groupInvitationListener.onRejectInvitation(mXSession, group.getGroupId());
                }
            }
        });
    }
}
