package im.vector.webview;

import im.vector.activity.VectorAppCompatActivity;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0002H\u0002J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0014\u0010\t\u001a\u00020\u00042\n\u0010\u0007\u001a\u00060\nj\u0002`\u000bH\u0016J\u0010\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u0002H\u0016J\u0014\u0010\u000e\u001a\u00020\u00042\n\u0010\u0007\u001a\u00060\nj\u0002`\u000bH\u0016¨\u0006\u000f"}, d2 = {"im/vector/webview/ConsentWebViewEventListener$createRiotBotRoomCallback$1", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "", "onError", "", "error", "onMatrixError", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "info", "onUnexpectedError", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: ConsentWebViewEventListener.kt */
public final class ConsentWebViewEventListener$createRiotBotRoomCallback$1 implements ApiCallback<String> {
    final /* synthetic */ ConsentWebViewEventListener this$0;

    ConsentWebViewEventListener$createRiotBotRoomCallback$1(ConsentWebViewEventListener consentWebViewEventListener) {
        this.this$0 = consentWebViewEventListener;
    }

    public void onSuccess(String str) {
        Intrinsics.checkParameterIsNotNull(str, "info");
        Log.d("ConsentWebViewEventListener", "## On success : succeed to invite riot-bot");
        VectorAppCompatActivity access$getSafeActivity$p = this.this$0.getSafeActivity();
        if (access$getSafeActivity$p != null) {
            access$getSafeActivity$p.finish();
        }
    }

    private final void onError(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("## On error : failed  to invite riot-bot ");
        sb.append(str);
        Log.e("ConsentWebViewEventListener", sb.toString());
        VectorAppCompatActivity access$getSafeActivity$p = this.this$0.getSafeActivity();
        if (access$getSafeActivity$p != null) {
            access$getSafeActivity$p.finish();
        }
    }

    public void onNetworkError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        onError(exc.getMessage());
    }

    public void onMatrixError(MatrixError matrixError) {
        Intrinsics.checkParameterIsNotNull(matrixError, "e");
        onError(matrixError.getMessage());
    }

    public void onUnexpectedError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        onError(exc.getMessage());
    }
}
