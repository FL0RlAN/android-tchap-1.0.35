package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.listeners.ProgressListener;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupStateListener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, d2 = {"org/matrix/androidsdk/crypto/keysbackup/KeysBackup$backupAllGroupSessions$1", "Lorg/matrix/androidsdk/core/listeners/ProgressListener;", "onProgress", "", "progress", "", "total", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
public final class KeysBackup$backupAllGroupSessions$1 implements ProgressListener {
    final /* synthetic */ ApiCallback $callback;
    final /* synthetic */ ProgressListener $progressListener;
    final /* synthetic */ KeysBackup this$0;

    KeysBackup$backupAllGroupSessions$1(KeysBackup keysBackup, ProgressListener progressListener, ApiCallback apiCallback) {
        this.this$0 = keysBackup;
        this.$progressListener = progressListener;
        this.$callback = apiCallback;
    }

    public void onProgress(int i, int i2) {
        this.this$0.resetBackupAllGroupSessionsListeners();
        String access$getLOG_TAG$cp = KeysBackup.LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("backupAllGroupSessions: backupProgress: ");
        sb.append(i);
        sb.append('/');
        sb.append(i2);
        Log.d(access$getLOG_TAG$cp, sb.toString());
        try {
            ProgressListener progressListener = this.$progressListener;
            if (progressListener != null) {
                progressListener.onProgress(i, i2);
            }
        } catch (Exception e) {
            Log.e(KeysBackup.LOG_TAG, "backupAllGroupSessions: onProgress failure", e);
        }
        if (i == i2) {
            Log.d(KeysBackup.LOG_TAG, "backupAllGroupSessions: complete");
            ApiCallback apiCallback = this.$callback;
            if (apiCallback != null) {
                apiCallback.onSuccess(null);
            }
            return;
        }
        this.this$0.backupAllGroupSessionsCallback = this.$callback;
        this.this$0.mKeysBackupStateListener = new KeysBackup$backupAllGroupSessions$1$onProgress$1(this);
        KeysBackupStateManager access$getMKeysBackupStateManager$p = this.this$0.mKeysBackupStateManager;
        KeysBackupStateListener access$getMKeysBackupStateListener$p = this.this$0.mKeysBackupStateListener;
        if (access$getMKeysBackupStateListener$p == null) {
            Intrinsics.throwNpe();
        }
        access$getMKeysBackupStateManager$p.addListener(access$getMKeysBackupStateListener$p);
        this.this$0.backupKeys();
    }
}
