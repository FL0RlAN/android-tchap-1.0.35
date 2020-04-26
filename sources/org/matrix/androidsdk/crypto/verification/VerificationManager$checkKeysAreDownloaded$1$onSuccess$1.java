package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: VerificationManager.kt */
final class VerificationManager$checkKeysAreDownloaded$1$onSuccess$1 implements Runnable {
    final /* synthetic */ MXUsersDevicesMap $info;
    final /* synthetic */ VerificationManager$checkKeysAreDownloaded$1 this$0;

    VerificationManager$checkKeysAreDownloaded$1$onSuccess$1(VerificationManager$checkKeysAreDownloaded$1 verificationManager$checkKeysAreDownloaded$1, MXUsersDevicesMap mXUsersDevicesMap) {
        this.this$0 = verificationManager$checkKeysAreDownloaded$1;
        this.$info = mXUsersDevicesMap;
    }

    public final void run() {
        if (this.$info.getUserDeviceIds(this.this$0.$otherUserId).contains(this.this$0.$startReq.fromDevice)) {
            this.this$0.$success.invoke(this.$info);
        } else {
            this.this$0.$error.invoke();
        }
    }
}
