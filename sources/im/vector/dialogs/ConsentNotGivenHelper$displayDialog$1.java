package im.vector.dialogs;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import androidx.appcompat.app.AlertDialog.Builder;
import fr.gouv.tchap.a.R;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001H\n¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "Landroidx/appcompat/app/AlertDialog$Builder;", "kotlin.jvm.PlatformType", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: ConsentNotGivenHelper.kt */
final class ConsentNotGivenHelper$displayDialog$1 extends Lambda implements Function0<Builder> {
    final /* synthetic */ MatrixError $matrixError;
    final /* synthetic */ ConsentNotGivenHelper this$0;

    ConsentNotGivenHelper$displayDialog$1(ConsentNotGivenHelper consentNotGivenHelper, MatrixError matrixError) {
        this.this$0 = consentNotGivenHelper;
        this.$matrixError = matrixError;
        super(0);
    }

    public final Builder invoke() {
        Builder positiveButton = new Builder(this.this$0.activity).setTitle((int) R.string.settings_app_term_conditions).setMessage((CharSequence) this.this$0.activity.getString(R.string.dialog_user_consent_content, new Object[]{"Tchap"})).setPositiveButton((int) R.string.dialog_user_consent_submit, (OnClickListener) new OnClickListener(this) {
            final /* synthetic */ ConsentNotGivenHelper$displayDialog$1 this$0;

            {
                this.this$0 = r1;
            }

            public final void onClick(DialogInterface dialogInterface, int i) {
                ConsentNotGivenHelper consentNotGivenHelper = this.this$0.this$0;
                String str = this.this$0.$matrixError.consentUri;
                Intrinsics.checkExpressionValueIsNotNull(str, "matrixError.consentUri");
                consentNotGivenHelper.openWebViewActivity(str);
            }
        });
        Intrinsics.checkExpressionValueIsNotNull(positiveButton, "AlertDialog.Builder(acti…ri)\n                    }");
        return positiveButton;
    }
}
