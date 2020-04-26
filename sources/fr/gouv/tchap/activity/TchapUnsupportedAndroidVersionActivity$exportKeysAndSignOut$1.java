package fr.gouv.tchap.activity;

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
import org.matrix.androidsdk.core.callback.SimpleApiCallback;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: TchapUnsupportedAndroidVersionActivity.kt */
final class TchapUnsupportedAndroidVersionActivity$exportKeysAndSignOut$1 implements OnClickListener {
    final /* synthetic */ AlertDialog $exportDialog;
    final /* synthetic */ TextInputEditText $passPhrase1EditText;
    final /* synthetic */ TchapUnsupportedAndroidVersionActivity this$0;

    TchapUnsupportedAndroidVersionActivity$exportKeysAndSignOut$1(TchapUnsupportedAndroidVersionActivity tchapUnsupportedAndroidVersionActivity, TextInputEditText textInputEditText, AlertDialog alertDialog) {
        this.this$0 = tchapUnsupportedAndroidVersionActivity;
        this.$passPhrase1EditText = textInputEditText;
        this.$exportDialog = alertDialog;
    }

    public final void onClick(View view) {
        this.this$0.showWaitingView();
        MXSession access$getSession$p = this.this$0.session;
        TextInputEditText textInputEditText = this.$passPhrase1EditText;
        Intrinsics.checkExpressionValueIsNotNull(textInputEditText, "passPhrase1EditText");
        CommonActivityUtils.exportKeys(access$getSession$p, String.valueOf(textInputEditText.getText()), new SimpleApiCallback<String>(this, this.this$0) {
            final /* synthetic */ TchapUnsupportedAndroidVersionActivity$exportKeysAndSignOut$1 this$0;

            {
                this.this$0 = r1;
            }

            public void onSuccess(String str) {
                Intrinsics.checkParameterIsNotNull(str, "filename");
                this.this$0.this$0.hideWaitingView();
                new Builder(this.this$0.this$0).setMessage((CharSequence) this.this$0.this$0.getString(R.string.encryption_export_saved_as, new Object[]{str})).setPositiveButton((int) R.string.action_sign_out, (DialogInterface.OnClickListener) new TchapUnsupportedAndroidVersionActivity$exportKeysAndSignOut$1$1$onSuccess$1(this)).show();
            }
        });
        this.$exportDialog.dismiss();
    }
}
