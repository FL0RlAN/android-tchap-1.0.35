package fr.gouv.tchap.fragments;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class TchapRoomsFragment_ViewBinding implements Unbinder {
    private TchapRoomsFragment target;

    public TchapRoomsFragment_ViewBinding(TchapRoomsFragment tchapRoomsFragment, View view) {
        this.target = tchapRoomsFragment;
        tchapRoomsFragment.mRecycler = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.recyclerview, "field 'mRecycler'", RecyclerView.class);
    }

    public void unbind() {
        TchapRoomsFragment tchapRoomsFragment = this.target;
        if (tchapRoomsFragment != null) {
            this.target = null;
            tchapRoomsFragment.mRecycler = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
