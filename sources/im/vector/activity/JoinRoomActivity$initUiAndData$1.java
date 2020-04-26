package im.vector.activity;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0014\u0010\u0007\u001a\u00020\u00042\n\u0010\u0005\u001a\u00060\bj\u0002`\tH\u0016J\u0012\u0010\n\u001a\u00020\u00042\b\u0010\u000b\u001a\u0004\u0018\u00010\u0002H\u0016J\u0014\u0010\f\u001a\u00020\u00042\n\u0010\u0005\u001a\u00060\bj\u0002`\tH\u0016¨\u0006\r"}, d2 = {"im/vector/activity/JoinRoomActivity$initUiAndData$1", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Ljava/lang/Void;", "onMatrixError", "", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "v", "onUnexpectedError", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: JoinRoomActivity.kt */
public final class JoinRoomActivity$initUiAndData$1 implements ApiCallback<Void> {
    JoinRoomActivity$initUiAndData$1() {
    }

    public void onSuccess(Void voidR) {
        Log.d(JoinRoomActivity.LOG_TAG, "## onCreate() : join succeeds");
    }

    public void onNetworkError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        String access$getLOG_TAG$cp = JoinRoomActivity.LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onCreate() : join fails ");
        sb.append(exc.getMessage());
        Log.e(access$getLOG_TAG$cp, sb.toString(), exc);
    }

    public void onMatrixError(MatrixError matrixError) {
        Intrinsics.checkParameterIsNotNull(matrixError, "e");
        String access$getLOG_TAG$cp = JoinRoomActivity.LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onCreate() : join fails ");
        sb.append(matrixError.getLocalizedMessage());
        Log.e(access$getLOG_TAG$cp, sb.toString());
    }

    public void onUnexpectedError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        String access$getLOG_TAG$cp = JoinRoomActivity.LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onCreate() : join fails ");
        sb.append(exc.getMessage());
        Log.e(access$getLOG_TAG$cp, sb.toString(), exc);
    }
}