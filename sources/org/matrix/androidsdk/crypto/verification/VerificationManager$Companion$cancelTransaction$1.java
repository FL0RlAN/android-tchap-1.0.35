package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0016J\u0018\u0010\u0007\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0018\u00010\bj\u0004\u0018\u0001`\tH\u0016J\u0012\u0010\n\u001a\u00020\u00042\b\u0010\u000b\u001a\u0004\u0018\u00010\u0002H\u0016J\u0018\u0010\f\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0018\u00010\bj\u0004\u0018\u0001`\tH\u0016¨\u0006\r"}, d2 = {"org/matrix/androidsdk/crypto/verification/VerificationManager$Companion$cancelTransaction$1", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Ljava/lang/Void;", "onMatrixError", "", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "info", "onUnexpectedError", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: VerificationManager.kt */
public final class VerificationManager$Companion$cancelTransaction$1 implements ApiCallback<Void> {
    final /* synthetic */ CancelCode $code;
    final /* synthetic */ String $transactionId;

    VerificationManager$Companion$cancelTransaction$1(String str, CancelCode cancelCode) {
        this.$transactionId = str;
        this.$code = cancelCode;
    }

    public void onSuccess(Void voidR) {
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## SAS verification [");
        sb.append(this.$transactionId);
        sb.append("] canceled for reason ");
        sb.append(this.$code.getValue());
        Log.d(log_tag, sb.toString());
    }

    public void onUnexpectedError(Exception exc) {
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## SAS verification [");
        sb.append(this.$transactionId);
        sb.append("] failed to cancel.");
        Log.e(log_tag, sb.toString(), exc);
    }

    public void onNetworkError(Exception exc) {
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## SAS verification [");
        sb.append(this.$transactionId);
        sb.append("] failed to cancel.");
        Log.e(log_tag, sb.toString(), exc);
    }

    public void onMatrixError(MatrixError matrixError) {
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## SAS verification [");
        sb.append(this.$transactionId);
        sb.append("] failed to cancel. ");
        sb.append(matrixError != null ? matrixError.getLocalizedMessage() : null);
        Log.e(log_tag, sb.toString());
    }
}
