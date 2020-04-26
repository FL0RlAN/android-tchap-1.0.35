package im.vector.fragments;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import com.google.android.material.textfield.TextInputEditText;
import fr.gouv.tchap.a.R;
import im.vector.activity.CommonActivityUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$exportKeys$1 implements OnClickListener {
    final /* synthetic */ AlertDialog $exportDialog;
    final /* synthetic */ TextInputEditText $passPhrase1EditText;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$exportKeys$1(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, TextInputEditText textInputEditText, AlertDialog alertDialog) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$passPhrase1EditText = textInputEditText;
        this.$exportDialog = alertDialog;
    }

    public final void onClick(View view) {
        this.this$0.displayLoadingView();
        MXSession access$getMSession$p = VectorSettingsPreferencesFragment.access$getMSession$p(this.this$0);
        TextInputEditText textInputEditText = this.$passPhrase1EditText;
        Intrinsics.checkExpressionValueIsNotNull(textInputEditText, "passPhrase1EditText");
        CommonActivityUtils.exportKeys(access$getMSession$p, String.valueOf(textInputEditText.getText()), new ApiCallback<String>(this) {
            final /* synthetic */ VectorSettingsPreferencesFragment$exportKeys$1 this$0;

            {
                this.this$0 = r1;
            }

            public void onSuccess(String str) {
                Intrinsics.checkParameterIsNotNull(str, "filename");
                this.this$0.this$0.hideLoadingView();
                new Builder(this.this$0.this$0.getActivity()).setMessage((CharSequence) this.this$0.this$0.getString(R.string.encryption_export_saved_as, new Object[]{str})).setPositiveButton((int) R.string.ok, (DialogInterface.OnClickListener) null).show();
            }

            public void onNetworkError(Exception exc) {
                Intrinsics.checkParameterIsNotNull(exc, "e");
                this.this$0.this$0.hideLoadingView();
            }

            public void onMatrixError(MatrixError matrixError) {
                Intrinsics.checkParameterIsNotNull(matrixError, "e");
                this.this$0.this$0.hideLoadingView();
            }

            public void onUnexpectedError(Exception exc) {
                Intrinsics.checkParameterIsNotNull(exc, "e");
                this.this$0.this$0.hideLoadingView();
            }
        });
        this.$exportDialog.dismiss();
    }
}
