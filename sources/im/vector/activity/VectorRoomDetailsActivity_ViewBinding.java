package im.vector.activity;

import android.view.View;
import butterknife.internal.Utils;
import com.google.android.material.tabs.TabLayout;
import fr.gouv.tchap.a.R;

public class VectorRoomDetailsActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private VectorRoomDetailsActivity target;

    public VectorRoomDetailsActivity_ViewBinding(VectorRoomDetailsActivity vectorRoomDetailsActivity) {
        this(vectorRoomDetailsActivity, vectorRoomDetailsActivity.getWindow().getDecorView());
    }

    public VectorRoomDetailsActivity_ViewBinding(VectorRoomDetailsActivity vectorRoomDetailsActivity, View view) {
        super(vectorRoomDetailsActivity, view);
        this.target = vectorRoomDetailsActivity;
        vectorRoomDetailsActivity.mTabNavigationView = (TabLayout) Utils.findRequiredViewAsType(view, R.id.tab_layout, "field 'mTabNavigationView'", TabLayout.class);
    }

    public void unbind() {
        VectorRoomDetailsActivity vectorRoomDetailsActivity = this.target;
        if (vectorRoomDetailsActivity != null) {
            this.target = null;
            vectorRoomDetailsActivity.mTabNavigationView = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
