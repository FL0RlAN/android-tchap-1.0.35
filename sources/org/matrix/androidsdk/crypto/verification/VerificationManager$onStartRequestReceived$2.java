package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationStart;
import org.matrix.androidsdk.crypto.verification.VerificationManager.Companion;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: VerificationManager.kt */
final class VerificationManager$onStartRequestReceived$2 extends Lambda implements Function0<Unit> {
    final /* synthetic */ String $otherUserId;
    final /* synthetic */ KeyVerificationStart $startReq;
    final /* synthetic */ VerificationManager this$0;

    VerificationManager$onStartRequestReceived$2(VerificationManager verificationManager, KeyVerificationStart keyVerificationStart, String str) {
        this.this$0 = verificationManager;
        this.$startReq = keyVerificationStart;
        this.$otherUserId = str;
        super(0);
    }

    public final void invoke() {
        Companion companion = VerificationManager.Companion;
        CryptoSession session = this.this$0.getSession();
        String str = this.$startReq.transactionID;
        if (str == null) {
            Intrinsics.throwNpe();
        }
        String str2 = this.$otherUserId;
        String str3 = this.$startReq.fromDevice;
        if (str3 == null) {
            Intrinsics.throwNpe();
        }
        companion.cancelTransaction(session, str, str2, str3, CancelCode.UnexpectedMessage);
    }
}
