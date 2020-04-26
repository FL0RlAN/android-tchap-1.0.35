package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.crypto.verification.SASVerificationTransaction.SASVerificationTxState;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: OutgoingSASVerificationRequest.kt */
final class OutgoingSASVerificationRequest$onVerificationAccept$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ OutgoingSASVerificationRequest this$0;

    OutgoingSASVerificationRequest$onVerificationAccept$1(OutgoingSASVerificationRequest outgoingSASVerificationRequest) {
        this.this$0 = outgoingSASVerificationRequest;
        super(0);
    }

    public final void invoke() {
        if (this.this$0.getState() == SASVerificationTxState.SendingKey) {
            this.this$0.setState(SASVerificationTxState.KeySent);
        }
    }
}
