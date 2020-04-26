package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import org.matrix.androidsdk.core.callback.SuccessCallback;
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
final class KeysBackup$getKeysBackupTrust$1 implements Runnable {
    final /* synthetic */ SuccessCallback $callback;
    final /* synthetic */ KeysVersionResult $keysBackupVersion;
    final /* synthetic */ KeysBackup this$0;

    KeysBackup$getKeysBackupTrust$1(KeysBackup keysBackup, KeysVersionResult keysVersionResult, SuccessCallback successCallback) {
        this.this$0 = keysBackup;
        this.$keysBackupVersion = keysVersionResult;
        this.$callback = successCallback;
    }

    public final void run() {
        final KeysBackupVersionTrust access$getKeysBackupTrustBg = this.this$0.getKeysBackupTrustBg(this.$keysBackupVersion);
        this.this$0.mCrypto.getUIHandler().post(new Runnable(this) {
            final /* synthetic */ KeysBackup$getKeysBackupTrust$1 this$0;

            {
                this.this$0 = r1;
            }

            public final void run() {
                this.this$0.$callback.onSuccess(access$getKeysBackupTrustBg);
            }
        });
    }
}
