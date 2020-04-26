package im.vector.activity;

import android.view.View;
import android.widget.ExpandableListView;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class VectorRoomInviteMembersActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private VectorRoomInviteMembersActivity target;

    public VectorRoomInviteMembersActivity_ViewBinding(VectorRoomInviteMembersActivity vectorRoomInviteMembersActivity) {
        this(vectorRoomInviteMembersActivity, vectorRoomInviteMembersActivity.getWindow().getDecorView());
    }

    public VectorRoomInviteMembersActivity_ViewBinding(VectorRoomInviteMembersActivity vectorRoomInviteMembersActivity, View view) {
        super(vectorRoomInviteMembersActivity, view);
        this.target = vectorRoomInviteMembersActivity;
        vectorRoomInviteMembersActivity.mListView = (ExpandableListView) Utils.findRequiredViewAsType(view, R.id.room_details_members_list, "field 'mListView'", ExpandableListView.class);
    }

    public void unbind() {
        VectorRoomInviteMembersActivity vectorRoomInviteMembersActivity = this.target;
        if (vectorRoomInviteMembersActivity != null) {
            this.target = null;
            vectorRoomInviteMembersActivity.mListView = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
