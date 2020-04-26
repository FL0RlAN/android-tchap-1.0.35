package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.verification.VerificationManager.VerificationManagerListener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: VerificationManager.kt */
final class VerificationManager$markedLocallyAsManuallyVerified$1$onSuccess$1 implements Runnable {
    final /* synthetic */ VerificationManager$markedLocallyAsManuallyVerified$1 this$0;

    VerificationManager$markedLocallyAsManuallyVerified$1$onSuccess$1(VerificationManager$markedLocallyAsManuallyVerified$1 verificationManager$markedLocallyAsManuallyVerified$1) {
        this.this$0 = verificationManager$markedLocallyAsManuallyVerified$1;
    }

    public final void run() {
        for (VerificationManagerListener markedAsManuallyVerified : this.this$0.this$0.listeners) {
            try {
                markedAsManuallyVerified.markedAsManuallyVerified(this.this$0.$userId, this.this$0.$deviceID);
            } catch (Throwable th) {
                Log.e(VerificationManager.Companion.getLOG_TAG(), "## Error while notifying listeners", th);
            }
        }
    }
}
