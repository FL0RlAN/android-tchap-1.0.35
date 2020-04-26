package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.listeners.StepProgressListener;
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
final class KeysBackup$restoreKeyBackupWithPassword$1 implements Runnable {
    final /* synthetic */ ApiCallback $callback;
    final /* synthetic */ KeysVersionResult $keysBackupVersion;
    final /* synthetic */ String $password;
    final /* synthetic */ String $roomId;
    final /* synthetic */ String $sessionId;
    final /* synthetic */ StepProgressListener $stepProgressListener;
    final /* synthetic */ KeysBackup this$0;

    KeysBackup$restoreKeyBackupWithPassword$1(KeysBackup keysBackup, StepProgressListener stepProgressListener, String str, KeysVersionResult keysVersionResult, ApiCallback apiCallback, String str2, String str3) {
        this.this$0 = keysBackup;
        this.$stepProgressListener = stepProgressListener;
        this.$password = str;
        this.$keysBackupVersion = keysVersionResult;
        this.$callback = apiCallback;
        this.$roomId = str2;
        this.$sessionId = str3;
    }

    public final void run() {
        String access$recoveryKeyFromPassword = this.this$0.recoveryKeyFromPassword(this.$password, this.$keysBackupVersion, this.$stepProgressListener != null ? new KeysBackup$restoreKeyBackupWithPassword$1$progressListener$1(this) : null);
        if (access$recoveryKeyFromPassword == null) {
            this.this$0.mCrypto.getUIHandler().post(new Runnable(this) {
                final /* synthetic */ KeysBackup$restoreKeyBackupWithPassword$1 this$0;

                {
                    this.this$0 = r1;
                }

                public final void run() {
                    Log.d(KeysBackup.LOG_TAG, "backupKeys: Invalid configuration");
                    this.this$0.$callback.onUnexpectedError(new IllegalStateException("Invalid configuration"));
                }
            });
        } else {
            this.this$0.restoreKeysWithRecoveryKey(this.$keysBackupVersion, access$recoveryKeyFromPassword, this.$roomId, this.$sessionId, this.$stepProgressListener, this.$callback);
        }
    }
}
