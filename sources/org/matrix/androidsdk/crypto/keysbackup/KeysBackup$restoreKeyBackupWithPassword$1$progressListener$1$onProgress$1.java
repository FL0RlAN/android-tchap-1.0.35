package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import org.matrix.androidsdk.core.listeners.StepProgressListener.Step.ComputingKey;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
final class KeysBackup$restoreKeyBackupWithPassword$1$progressListener$1$onProgress$1 implements Runnable {
    final /* synthetic */ int $progress;
    final /* synthetic */ int $total;
    final /* synthetic */ KeysBackup$restoreKeyBackupWithPassword$1$progressListener$1 this$0;

    KeysBackup$restoreKeyBackupWithPassword$1$progressListener$1$onProgress$1(KeysBackup$restoreKeyBackupWithPassword$1$progressListener$1 keysBackup$restoreKeyBackupWithPassword$1$progressListener$1, int i, int i2) {
        this.this$0 = keysBackup$restoreKeyBackupWithPassword$1$progressListener$1;
        this.$progress = i;
        this.$total = i2;
    }

    public final void run() {
        this.this$0.this$0.$stepProgressListener.onStepProgress(new ComputingKey(this.$progress, this.$total));
    }
}
