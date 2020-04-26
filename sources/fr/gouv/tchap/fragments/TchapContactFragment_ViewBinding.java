package fr.gouv.tchap.fragments;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class TchapContactFragment_ViewBinding implements Unbinder {
    private TchapContactFragment target;

    public TchapContactFragment_ViewBinding(TchapContactFragment tchapContactFragment, View view) {
        this.target = tchapContactFragment;
        tchapContactFragment.mRecycler = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.recyclerview, "field 'mRecycler'", RecyclerView.class);
        tchapContactFragment.waitingView = Utils.findRequiredView(view, R.id.listView_spinner_views, "field 'waitingView'");
        tchapContactFragment.mInviteContactLayout = Utils.findRequiredView(view, R.id.ly_invite_contacts_to_tchap, "field 'mInviteContactLayout'");
    }

    public void unbind() {
        TchapContactFragment tchapContactFragment = this.target;
        if (tchapContactFragment != null) {
            this.target = null;
            tchapContactFragment.mRecycler = null;
            tchapContactFragment.waitingView = null;
            tchapContactFragment.mInviteContactLayout = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
