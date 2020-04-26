package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import org.matrix.androidsdk.crypto.verification.VerificationManager.VerificationManagerListener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: VerificationManager.kt */
final class VerificationManager$removeListener$1 implements Runnable {
    final /* synthetic */ VerificationManagerListener $listener;
    final /* synthetic */ VerificationManager this$0;

    VerificationManager$removeListener$1(VerificationManager verificationManager, VerificationManagerListener verificationManagerListener) {
        this.this$0 = verificationManager;
        this.$listener = verificationManagerListener;
    }

    public final void run() {
        this.this$0.listeners.remove(this.$listener);
    }
}
