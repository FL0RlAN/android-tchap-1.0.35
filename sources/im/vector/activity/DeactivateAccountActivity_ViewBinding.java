package im.vector.activity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public final class DeactivateAccountActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private DeactivateAccountActivity target;
    private View view7f0900a2;
    private View view7f0900a3;

    public DeactivateAccountActivity_ViewBinding(DeactivateAccountActivity deactivateAccountActivity) {
        this(deactivateAccountActivity, deactivateAccountActivity.getWindow().getDecorView());
    }

    public DeactivateAccountActivity_ViewBinding(final DeactivateAccountActivity deactivateAccountActivity, View view) {
        super(deactivateAccountActivity, view);
        this.target = deactivateAccountActivity;
        deactivateAccountActivity.eraseCheckBox = (CheckBox) Utils.findRequiredViewAsType(view, R.id.deactivate_account_erase_checkbox, "field 'eraseCheckBox'", CheckBox.class);
        deactivateAccountActivity.passwordEditText = (EditText) Utils.findRequiredViewAsType(view, R.id.deactivate_account_password, "field 'passwordEditText'", EditText.class);
        View findRequiredView = Utils.findRequiredView(view, R.id.deactivate_account_button_submit, "method 'onSubmit$vector_appAgentWithoutvoipWithpinningMatrixorg'");
        this.view7f0900a3 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                deactivateAccountActivity.onSubmit$vector_appAgentWithoutvoipWithpinningMatrixorg();
            }
        });
        View findRequiredView2 = Utils.findRequiredView(view, R.id.deactivate_account_button_cancel, "method 'onCancel$vector_appAgentWithoutvoipWithpinningMatrixorg'");
        this.view7f0900a2 = findRequiredView2;
        findRequiredView2.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                deactivateAccountActivity.onCancel$vector_appAgentWithoutvoipWithpinningMatrixorg();
            }
        });
    }

    public void unbind() {
        DeactivateAccountActivity deactivateAccountActivity = this.target;
        if (deactivateAccountActivity != null) {
            this.target = null;
            deactivateAccountActivity.eraseCheckBox = null;
            deactivateAccountActivity.passwordEditText = null;
            this.view7f0900a3.setOnClickListener(null);
            this.view7f0900a3 = null;
            this.view7f0900a2.setOnClickListener(null);
            this.view7f0900a2 = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
