package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.crypto.interfaces.CryptoEvent;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: VerificationManager.kt */
final class VerificationManager$onToDeviceEvent$1 implements Runnable {
    final /* synthetic */ CryptoEvent $event;
    final /* synthetic */ VerificationManager this$0;

    VerificationManager$onToDeviceEvent$1(VerificationManager verificationManager, CryptoEvent cryptoEvent) {
        this.this$0 = verificationManager;
        this.$event = cryptoEvent;
    }

    public final void run() {
        CharSequence type = this.$event.getType();
        if (Intrinsics.areEqual((Object) type, (Object) "m.key.verification.start")) {
            this.this$0.onStartRequestReceived(this.$event);
        } else if (Intrinsics.areEqual((Object) type, (Object) "m.key.verification.cancel")) {
            this.this$0.onCancelReceived(this.$event);
        } else if (Intrinsics.areEqual((Object) type, (Object) "m.key.verification.accept")) {
            this.this$0.onAcceptReceived(this.$event);
        } else if (Intrinsics.areEqual((Object) type, (Object) "m.key.verification.key")) {
            this.this$0.onKeyReceived(this.$event);
        } else if (Intrinsics.areEqual((Object) type, (Object) "m.key.verification.mac")) {
            this.this$0.onMacReceived(this.$event);
        }
    }
}
