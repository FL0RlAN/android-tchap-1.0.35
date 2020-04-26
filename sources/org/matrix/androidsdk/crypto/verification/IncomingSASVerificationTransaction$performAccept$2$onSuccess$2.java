package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationAccept;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: IncomingSASVerificationTransaction.kt */
final class IncomingSASVerificationTransaction$performAccept$2$onSuccess$2 implements Runnable {
    final /* synthetic */ KeyVerificationAccept $accept;
    final /* synthetic */ IncomingSASVerificationTransaction$performAccept$2 this$0;

    IncomingSASVerificationTransaction$performAccept$2$onSuccess$2(IncomingSASVerificationTransaction$performAccept$2 incomingSASVerificationTransaction$performAccept$2, KeyVerificationAccept keyVerificationAccept) {
        this.this$0 = incomingSASVerificationTransaction$performAccept$2;
        this.$accept = keyVerificationAccept;
    }

    public final void run() {
        this.this$0.this$0.doAccept(this.this$0.$session, this.$accept);
    }
}
