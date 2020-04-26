package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.listeners.ProgressListener;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, d2 = {"org/matrix/androidsdk/crypto/keysbackup/KeysBackup$backupAllGroupSessions$1$onProgress$1$onStateChange$1", "Lorg/matrix/androidsdk/core/listeners/ProgressListener;", "onProgress", "", "progress", "", "total", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
public final class KeysBackup$backupAllGroupSessions$1$onProgress$1$onStateChange$1 implements ProgressListener {
    final /* synthetic */ KeysBackup$backupAllGroupSessions$1$onProgress$1 this$0;

    KeysBackup$backupAllGroupSessions$1$onProgress$1$onStateChange$1(KeysBackup$backupAllGroupSessions$1$onProgress$1 keysBackup$backupAllGroupSessions$1$onProgress$1) {
        this.this$0 = keysBackup$backupAllGroupSessions$1$onProgress$1;
    }

    public void onProgress(int i, int i2) {
        try {
            ProgressListener progressListener = this.this$0.this$0.$progressListener;
            if (progressListener != null) {
                progressListener.onProgress(i, i2);
            }
        } catch (Exception e) {
            Log.e(KeysBackup.LOG_TAG, "backupAllGroupSessions: onProgress failure 2", e);
        }
        if (this.this$0.this$0.this$0.getState() == KeysBackupState.ReadyToBackUp) {
            ApiCallback access$getBackupAllGroupSessionsCallback$p = this.this$0.this$0.this$0.backupAllGroupSessionsCallback;
            if (access$getBackupAllGroupSessionsCallback$p != null) {
                access$getBackupAllGroupSessionsCallback$p.onSuccess(null);
            }
            this.this$0.this$0.this$0.resetBackupAllGroupSessionsListeners();
        }
    }
}
