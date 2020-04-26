package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
final class KeysBackup$trustKeysBackupVersionWithRecoveryKey$1 implements Runnable {
    final /* synthetic */ ApiCallback $callback;
    final /* synthetic */ KeysVersionResult $keysBackupVersion;
    final /* synthetic */ String $recoveryKey;
    final /* synthetic */ KeysBackup this$0;

    KeysBackup$trustKeysBackupVersionWithRecoveryKey$1(KeysBackup keysBackup, String str, KeysVersionResult keysVersionResult, ApiCallback apiCallback) {
        this.this$0 = keysBackup;
        this.$recoveryKey = str;
        this.$keysBackupVersion = keysVersionResult;
        this.$callback = apiCallback;
    }

    public final void run() {
        if (!this.this$0.isValidRecoveryKeyForKeysBackupVersion(this.$recoveryKey, this.$keysBackupVersion)) {
            Log.w(KeysBackup.LOG_TAG, "trustKeyBackupVersionWithRecoveryKey: Invalid recovery key.");
            this.this$0.mCrypto.getUIHandler().post(new Runnable(this) {
                final /* synthetic */ KeysBackup$trustKeysBackupVersionWithRecoveryKey$1 this$0;

                {
                    this.this$0 = r1;
                }

                public final void run() {
                    this.this$0.$callback.onUnexpectedError(new IllegalArgumentException("Invalid recovery key or password"));
                }
            });
            return;
        }
        this.this$0.trustKeysBackupVersion(this.$keysBackupVersion, true, this.$callback);
    }
}
