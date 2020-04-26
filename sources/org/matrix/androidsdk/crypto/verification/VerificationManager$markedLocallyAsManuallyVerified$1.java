package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0016J\u0018\u0010\u0007\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0018\u00010\bj\u0004\u0018\u0001`\tH\u0016J\u0012\u0010\n\u001a\u00020\u00042\b\u0010\u000b\u001a\u0004\u0018\u00010\u0002H\u0016J\u0018\u0010\f\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0018\u00010\bj\u0004\u0018\u0001`\tH\u0016¨\u0006\r"}, d2 = {"org/matrix/androidsdk/crypto/verification/VerificationManager$markedLocallyAsManuallyVerified$1", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Ljava/lang/Void;", "onMatrixError", "", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "info", "onUnexpectedError", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: VerificationManager.kt */
public final class VerificationManager$markedLocallyAsManuallyVerified$1 implements ApiCallback<Void> {
    final /* synthetic */ String $deviceID;
    final /* synthetic */ String $userId;
    final /* synthetic */ VerificationManager this$0;

    VerificationManager$markedLocallyAsManuallyVerified$1(VerificationManager verificationManager, String str, String str2) {
        this.this$0 = verificationManager;
        this.$userId = str;
        this.$deviceID = str2;
    }

    public void onSuccess(Void voidR) {
        this.this$0.uiHandler.post(new VerificationManager$markedLocallyAsManuallyVerified$1$onSuccess$1(this));
    }

    public void onUnexpectedError(Exception exc) {
        Log.e(SASVerificationTransaction.Companion.getLOG_TAG(), "## Manual verification failed in state", exc);
    }

    public void onNetworkError(Exception exc) {
        Log.e(SASVerificationTransaction.Companion.getLOG_TAG(), "## Manual verification failed in state", exc);
    }

    public void onMatrixError(MatrixError matrixError) {
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## Manual verification failed in state ");
        sb.append(matrixError != null ? matrixError.mReason : null);
        Log.e(log_tag, sb.toString());
    }
}
