package fr.gouv.tchap.fragments;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class TchapPublicRoomsFragment_ViewBinding implements Unbinder {
    private TchapPublicRoomsFragment target;

    public TchapPublicRoomsFragment_ViewBinding(TchapPublicRoomsFragment tchapPublicRoomsFragment, View view) {
        this.target = tchapPublicRoomsFragment;
        tchapPublicRoomsFragment.mRecycler = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.recyclerview, "field 'mRecycler'", RecyclerView.class);
    }

    public void unbind() {
        TchapPublicRoomsFragment tchapPublicRoomsFragment = this.target;
        if (tchapPublicRoomsFragment != null) {
            this.target = null;
            tchapPublicRoomsFragment.mRecycler = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
