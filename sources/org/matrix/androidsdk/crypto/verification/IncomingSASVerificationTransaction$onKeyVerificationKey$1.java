package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.crypto.verification.SASVerificationTransaction.SASVerificationTxState;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: IncomingSASVerificationTransaction.kt */
final class IncomingSASVerificationTransaction$onKeyVerificationKey$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ IncomingSASVerificationTransaction this$0;

    IncomingSASVerificationTransaction$onKeyVerificationKey$1(IncomingSASVerificationTransaction incomingSASVerificationTransaction) {
        this.this$0 = incomingSASVerificationTransaction;
        super(0);
    }

    public final void invoke() {
        if (this.this$0.getState() == SASVerificationTxState.SendingKey) {
            this.this$0.setState(SASVerificationTxState.KeySent);
        }
    }
}
