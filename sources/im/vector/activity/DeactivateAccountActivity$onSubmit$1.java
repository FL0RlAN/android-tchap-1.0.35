package im.vector.activity;

import android.app.Activity;
import fr.gouv.tchap.a.R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0014\u0010\u0007\u001a\u00020\u00042\n\u0010\u0005\u001a\u00060\bj\u0002`\tH\u0016J\u0012\u0010\n\u001a\u00020\u00042\b\u0010\u000b\u001a\u0004\u0018\u00010\u0002H\u0016J\u0014\u0010\f\u001a\u00020\u00042\n\u0010\u0005\u001a\u00060\bj\u0002`\tH\u0016¨\u0006\r"}, d2 = {"im/vector/activity/DeactivateAccountActivity$onSubmit$1", "Lorg/matrix/androidsdk/core/callback/SimpleApiCallback;", "Ljava/lang/Void;", "onMatrixError", "", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "info", "onUnexpectedError", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: DeactivateAccountActivity.kt */
public final class DeactivateAccountActivity$onSubmit$1 extends SimpleApiCallback<Void> {
    final /* synthetic */ DeactivateAccountActivity this$0;

    DeactivateAccountActivity$onSubmit$1(DeactivateAccountActivity deactivateAccountActivity, Activity activity) {
        this.this$0 = deactivateAccountActivity;
        super(activity);
    }

    public void onSuccess(Void voidR) {
        this.this$0.hideWaitingView();
        CommonActivityUtils.startLoginActivityNewTask(this.this$0);
    }

    public void onMatrixError(MatrixError matrixError) {
        Intrinsics.checkParameterIsNotNull(matrixError, "e");
        this.this$0.hideWaitingView();
        if (Intrinsics.areEqual((Object) matrixError.errcode, (Object) MatrixError.FORBIDDEN)) {
            this.this$0.getPasswordEditText().setError(this.this$0.getString(R.string.auth_invalid_login_param));
        } else {
            super.onMatrixError(matrixError);
        }
    }

    public void onNetworkError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        this.this$0.hideWaitingView();
        super.onNetworkError(exc);
    }

    public void onUnexpectedError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        this.this$0.hideWaitingView();
        super.onUnexpectedError(exc);
    }
}
