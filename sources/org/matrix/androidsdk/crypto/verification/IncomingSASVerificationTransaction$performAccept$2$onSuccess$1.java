package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: IncomingSASVerificationTransaction.kt */
final class IncomingSASVerificationTransaction$performAccept$2$onSuccess$1 implements Runnable {
    final /* synthetic */ IncomingSASVerificationTransaction$performAccept$2 this$0;

    IncomingSASVerificationTransaction$performAccept$2$onSuccess$1(IncomingSASVerificationTransaction$performAccept$2 incomingSASVerificationTransaction$performAccept$2) {
        this.this$0 = incomingSASVerificationTransaction$performAccept$2;
    }

    public final void run() {
        this.this$0.this$0.cancel(this.this$0.$session, CancelCode.User);
    }
}
