package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.verification.VerificationManager.VerificationManagerListener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: VerificationManager.kt */
final class VerificationManager$dispatchTxAdded$1 implements Runnable {
    final /* synthetic */ VerificationTransaction $tx;
    final /* synthetic */ VerificationManager this$0;

    VerificationManager$dispatchTxAdded$1(VerificationManager verificationManager, VerificationTransaction verificationTransaction) {
        this.this$0 = verificationManager;
        this.$tx = verificationTransaction;
    }

    public final void run() {
        for (VerificationManagerListener transactionCreated : this.this$0.listeners) {
            try {
                transactionCreated.transactionCreated(this.$tx);
            } catch (Throwable th) {
                Log.e(VerificationManager.Companion.getLOG_TAG(), "## Error while notifying listeners", th);
            }
        }
    }
}
