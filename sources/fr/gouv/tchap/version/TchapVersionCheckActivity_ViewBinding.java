package fr.gouv.tchap.version;

import android.view.View;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;
import im.vector.activity.VectorAppCompatActivity_ViewBinding;

public final class TchapVersionCheckActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private TchapVersionCheckActivity target;
    private View view7f090077;
    private View view7f09007a;

    public TchapVersionCheckActivity_ViewBinding(TchapVersionCheckActivity tchapVersionCheckActivity) {
        this(tchapVersionCheckActivity, tchapVersionCheckActivity.getWindow().getDecorView());
    }

    public TchapVersionCheckActivity_ViewBinding(final TchapVersionCheckActivity tchapVersionCheckActivity, View view) {
        super(tchapVersionCheckActivity, view);
        this.target = tchapVersionCheckActivity;
        View findRequiredView = Utils.findRequiredView(view, R.id.checkVersionUpgrade, "method 'openStore'");
        this.view7f09007a = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                tchapVersionCheckActivity.openStore();
            }
        });
        View findRequiredView2 = Utils.findRequiredView(view, R.id.checkVersionOpen, "method 'openApp'");
        this.view7f090077 = findRequiredView2;
        findRequiredView2.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                tchapVersionCheckActivity.openApp();
            }
        });
    }

    public void unbind() {
        if (this.target != null) {
            this.target = null;
            this.view7f09007a.setOnClickListener(null);
            this.view7f09007a = null;
            this.view7f090077.setOnClickListener(null);
            this.view7f090077 = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
