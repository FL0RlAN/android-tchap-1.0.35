package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.verification.SASVerificationTransaction.SASVerificationTxState;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0004H\u0002J\u0012\u0010\u0005\u001a\u00020\u00042\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0016J\u0018\u0010\b\u001a\u00020\u00042\u000e\u0010\u0006\u001a\n\u0018\u00010\tj\u0004\u0018\u0001`\nH\u0016J\u0012\u0010\u000b\u001a\u00020\u00042\b\u0010\f\u001a\u0004\u0018\u00010\u0002H\u0016J\u0018\u0010\r\u001a\u00020\u00042\u000e\u0010\u0006\u001a\n\u0018\u00010\tj\u0004\u0018\u0001`\nH\u0016¨\u0006\u000e"}, d2 = {"org/matrix/androidsdk/crypto/verification/SASVerificationTransaction$sendToOther$1", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Ljava/lang/Void;", "handleError", "", "onMatrixError", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "info", "onUnexpectedError", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: SASVerificationTransaction.kt */
public final class SASVerificationTransaction$sendToOther$1 implements ApiCallback<Void> {
    final /* synthetic */ SASVerificationTxState $nextState;
    final /* synthetic */ Function0 $onDone;
    final /* synthetic */ CancelCode $onErrorReason;
    final /* synthetic */ CryptoSession $session;
    final /* synthetic */ String $type;
    final /* synthetic */ SASVerificationTransaction this$0;

    SASVerificationTransaction$sendToOther$1(SASVerificationTransaction sASVerificationTransaction, String str, CryptoSession cryptoSession, Function0 function0, SASVerificationTxState sASVerificationTxState, CancelCode cancelCode) {
        this.this$0 = sASVerificationTransaction;
        this.$type = str;
        this.$session = cryptoSession;
        this.$onDone = function0;
        this.$nextState = sASVerificationTxState;
        this.$onErrorReason = cancelCode;
    }

    public void onSuccess(Void voidR) {
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## SAS verification [");
        sb.append(this.this$0.getTransactionId());
        sb.append("] toDevice type '");
        sb.append(this.$type);
        sb.append("' success.");
        Log.d(log_tag, sb.toString());
        this.$session.requireCrypto().getDecryptingThreadHandler().post(new SASVerificationTransaction$sendToOther$1$onSuccess$1(this));
    }

    private final void handleError() {
        this.$session.requireCrypto().getDecryptingThreadHandler().post(new SASVerificationTransaction$sendToOther$1$handleError$1(this));
    }

    public void onUnexpectedError(Exception exc) {
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## SAS verification [");
        sb.append(this.this$0.getTransactionId());
        sb.append("] failed to send toDevice in state : ");
        sb.append(this.this$0.getState());
        Log.e(log_tag, sb.toString());
        handleError();
    }

    public void onNetworkError(Exception exc) {
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## SAS verification [");
        sb.append(this.this$0.getTransactionId());
        sb.append("] failed to send toDevice in state : ");
        sb.append(this.this$0.getState());
        Log.e(log_tag, sb.toString());
        handleError();
    }

    public void onMatrixError(MatrixError matrixError) {
        String log_tag = SASVerificationTransaction.Companion.getLOG_TAG();
        StringBuilder sb = new StringBuilder();
        sb.append("## SAS verification [");
        sb.append(this.this$0.getTransactionId());
        sb.append("] failed to send toDevice in state : ");
        sb.append(this.this$0.getState());
        Log.e(log_tag, sb.toString());
        handleError();
    }
}
