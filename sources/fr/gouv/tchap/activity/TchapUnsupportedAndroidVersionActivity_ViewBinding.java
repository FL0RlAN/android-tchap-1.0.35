package fr.gouv.tchap.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;
import im.vector.activity.VectorAppCompatActivity_ViewBinding;

public final class TchapUnsupportedAndroidVersionActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private TchapUnsupportedAndroidVersionActivity target;
    private View view7f0900e5;
    private View view7f090305;

    public TchapUnsupportedAndroidVersionActivity_ViewBinding(TchapUnsupportedAndroidVersionActivity tchapUnsupportedAndroidVersionActivity) {
        this(tchapUnsupportedAndroidVersionActivity, tchapUnsupportedAndroidVersionActivity.getWindow().getDecorView());
    }

    public TchapUnsupportedAndroidVersionActivity_ViewBinding(final TchapUnsupportedAndroidVersionActivity tchapUnsupportedAndroidVersionActivity, View view) {
        super(tchapUnsupportedAndroidVersionActivity, view);
        this.target = tchapUnsupportedAndroidVersionActivity;
        tchapUnsupportedAndroidVersionActivity.message = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_message, "field 'message'", TextView.class);
        View findRequiredView = Utils.findRequiredView(view, R.id.sign_out_button, "field 'signOutButton' and method 'signOut$vector_appAgentWithoutvoipWithpinningMatrixorg'");
        tchapUnsupportedAndroidVersionActivity.signOutButton = (Button) Utils.castView(findRequiredView, R.id.sign_out_button, "field 'signOutButton'", Button.class);
        this.view7f090305 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                tchapUnsupportedAndroidVersionActivity.signOut$vector_appAgentWithoutvoipWithpinningMatrixorg();
            }
        });
        View findRequiredView2 = Utils.findRequiredView(view, R.id.export_button, "field 'exportButton' and method 'exportKeysAndSignOut$vector_appAgentWithoutvoipWithpinningMatrixorg'");
        tchapUnsupportedAndroidVersionActivity.exportButton = (Button) Utils.castView(findRequiredView2, R.id.export_button, "field 'exportButton'", Button.class);
        this.view7f0900e5 = findRequiredView2;
        findRequiredView2.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                tchapUnsupportedAndroidVersionActivity.exportKeysAndSignOut$vector_appAgentWithoutvoipWithpinningMatrixorg();
            }
        });
    }

    public void unbind() {
        TchapUnsupportedAndroidVersionActivity tchapUnsupportedAndroidVersionActivity = this.target;
        if (tchapUnsupportedAndroidVersionActivity != null) {
            this.target = null;
            tchapUnsupportedAndroidVersionActivity.message = null;
            tchapUnsupportedAndroidVersionActivity.signOutButton = null;
            tchapUnsupportedAndroidVersionActivity.exportButton = null;
            this.view7f090305.setOnClickListener(null);
            this.view7f090305 = null;
            this.view7f0900e5.setOnClickListener(null);
            this.view7f0900e5 = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
