package im.vector.activity;

import android.view.View;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class VectorAppCompatActivity_ViewBinding implements Unbinder {
    private VectorAppCompatActivity target;

    public VectorAppCompatActivity_ViewBinding(VectorAppCompatActivity vectorAppCompatActivity) {
        this(vectorAppCompatActivity, vectorAppCompatActivity.getWindow().getDecorView());
    }

    public VectorAppCompatActivity_ViewBinding(VectorAppCompatActivity vectorAppCompatActivity, View view) {
        this.target = vectorAppCompatActivity;
        vectorAppCompatActivity.toolbar = (Toolbar) Utils.findOptionalViewAsType(view, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    }

    public void unbind() {
        VectorAppCompatActivity vectorAppCompatActivity = this.target;
        if (vectorAppCompatActivity != null) {
            this.target = null;
            vectorAppCompatActivity.toolbar = null;
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
