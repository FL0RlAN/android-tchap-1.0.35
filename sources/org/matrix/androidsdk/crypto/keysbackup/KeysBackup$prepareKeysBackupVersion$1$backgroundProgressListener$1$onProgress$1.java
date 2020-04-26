package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import org.matrix.androidsdk.core.Log;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
final class KeysBackup$prepareKeysBackupVersion$1$backgroundProgressListener$1$onProgress$1 implements Runnable {
    final /* synthetic */ int $progress;
    final /* synthetic */ int $total;
    final /* synthetic */ KeysBackup$prepareKeysBackupVersion$1$backgroundProgressListener$1 this$0;

    KeysBackup$prepareKeysBackupVersion$1$backgroundProgressListener$1$onProgress$1(KeysBackup$prepareKeysBackupVersion$1$backgroundProgressListener$1 keysBackup$prepareKeysBackupVersion$1$backgroundProgressListener$1, int i, int i2) {
        this.this$0 = keysBackup$prepareKeysBackupVersion$1$backgroundProgressListener$1;
        this.$progress = i;
        this.$total = i2;
    }

    public final void run() {
        try {
            this.this$0.this$0.$progressListener.onProgress(this.$progress, this.$total);
        } catch (Exception e) {
            Log.e(KeysBackup.LOG_TAG, "prepareKeysBackupVersion: onProgress failure", e);
        }
    }
}
