package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.verification.SASVerificationTransaction.SASVerificationTxState;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002¨\u0006\u0003"}, d2 = {"<anonymous>", "", "invoke", "org/matrix/androidsdk/crypto/verification/SASVerificationTransaction$verifyMacs$2$1"}, k = 3, mv = {1, 1, 13})
/* compiled from: SASVerificationTransaction.kt */
final class SASVerificationTransaction$verifyMacs$$inlined$forEach$lambda$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ CryptoSession $session$inlined;
    final /* synthetic */ SASVerificationTransaction this$0;

    SASVerificationTransaction$verifyMacs$$inlined$forEach$lambda$1(SASVerificationTransaction sASVerificationTransaction, CryptoSession cryptoSession) {
        this.this$0 = sASVerificationTransaction;
        this.$session$inlined = cryptoSession;
        super(0);
    }

    public final void invoke() {
        this.this$0.setState(SASVerificationTxState.Verified);
    }
}
