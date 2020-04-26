package im.vector.features.hhs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.style.StyleSpan;
import androidx.appcompat.app.AlertDialog.Builder;
import com.binaryfork.spanny.Spanny;
import fr.gouv.tchap.a.R;
import im.vector.features.hhs.ResourceLimitErrorFormatter.Mode.Hard;
import im.vector.util.ExternalApplicationsUtilKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u0001H\n¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "Landroidx/appcompat/app/AlertDialog$Builder;", "kotlin.jvm.PlatformType", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: ResourceLimitDialogHelper.kt */
final class ResourceLimitDialogHelper$displayDialog$1 extends Lambda implements Function0<Builder> {
    final /* synthetic */ MatrixError $matrixError;
    final /* synthetic */ ResourceLimitDialogHelper this$0;

    ResourceLimitDialogHelper$displayDialog$1(ResourceLimitDialogHelper resourceLimitDialogHelper, MatrixError matrixError) {
        this.this$0 = resourceLimitDialogHelper;
        this.$matrixError = matrixError;
        super(0);
    }

    public final Builder invoke() {
        Spanny spanny = new Spanny((CharSequence) this.this$0.activity.getString(R.string.resource_limit_exceeded_title), (Object) new StyleSpan(1));
        Builder message = new Builder(this.this$0.activity, im.vector.R.style.AppTheme_Dialog_Light).setTitle((CharSequence) spanny).setMessage(ResourceLimitErrorFormatter.format$default(this.this$0.formatter, this.$matrixError, Hard.INSTANCE, "\n\n", false, 8, null));
        if (this.$matrixError.adminUri != null) {
            message.setPositiveButton((int) R.string.resource_limit_contact_action, (OnClickListener) new OnClickListener(this) {
                final /* synthetic */ ResourceLimitDialogHelper$displayDialog$1 this$0;

                {
                    this.this$0 = r1;
                }

                public final void onClick(DialogInterface dialogInterface, int i) {
                    Activity access$getActivity$p = this.this$0.this$0.activity;
                    String str = this.this$0.$matrixError.adminUri;
                    if (str == null) {
                        Intrinsics.throwNpe();
                    }
                    Intrinsics.checkExpressionValueIsNotNull(str, "matrixError.adminUri!!");
                    ExternalApplicationsUtilKt.openUri(access$getActivity$p, str);
                }
            }).setNegativeButton((int) R.string.cancel, (OnClickListener) null);
        } else {
            message.setPositiveButton((int) R.string.ok, (OnClickListener) null);
        }
        Intrinsics.checkExpressionValueIsNotNull(message, "builder");
        return message;
    }
}
