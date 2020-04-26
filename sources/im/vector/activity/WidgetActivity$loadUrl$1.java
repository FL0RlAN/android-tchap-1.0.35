package im.vector.activity;

import android.widget.Toast;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0002H\u0002J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0014\u0010\t\u001a\u00020\u00042\n\u0010\u0007\u001a\u00060\nj\u0002`\u000bH\u0016J\u0010\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u0002H\u0016J\u0014\u0010\u000e\u001a\u00020\u00042\n\u0010\u0007\u001a\u00060\nj\u0002`\u000bH\u0016¨\u0006\u000f"}, d2 = {"im/vector/activity/WidgetActivity$loadUrl$1", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "", "onError", "", "errorMessage", "onMatrixError", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "url", "onUnexpectedError", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: WidgetActivity.kt */
public final class WidgetActivity$loadUrl$1 implements ApiCallback<String> {
    final /* synthetic */ WidgetActivity this$0;

    WidgetActivity$loadUrl$1(WidgetActivity widgetActivity) {
        this.this$0 = widgetActivity;
    }

    public void onSuccess(String str) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        this.this$0.hideWaitingView();
        this.this$0.getMWidgetWebView().loadUrl(str);
    }

    private final void onError(String str) {
        this.this$0.hideWaitingView();
        Toast makeText = Toast.makeText(this.this$0, str, 0);
        makeText.show();
        Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        this.this$0.finish();
    }

    public void onNetworkError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        String localizedMessage = exc.getLocalizedMessage();
        Intrinsics.checkExpressionValueIsNotNull(localizedMessage, "e.localizedMessage");
        onError(localizedMessage);
    }

    public void onMatrixError(MatrixError matrixError) {
        Intrinsics.checkParameterIsNotNull(matrixError, "e");
        String localizedMessage = matrixError.getLocalizedMessage();
        Intrinsics.checkExpressionValueIsNotNull(localizedMessage, "e.localizedMessage");
        onError(localizedMessage);
    }

    public void onUnexpectedError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        String localizedMessage = exc.getLocalizedMessage();
        Intrinsics.checkExpressionValueIsNotNull(localizedMessage, "e.localizedMessage");
        onError(localizedMessage);
    }
}
