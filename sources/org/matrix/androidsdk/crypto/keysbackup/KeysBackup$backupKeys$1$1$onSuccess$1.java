package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackup$backupKeys$1.AnonymousClass1;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState;
import org.matrix.androidsdk.crypto.model.rest.keys.BackupKeysResult;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
final class KeysBackup$backupKeys$1$1$onSuccess$1 implements Runnable {
    final /* synthetic */ BackupKeysResult $info;
    final /* synthetic */ AnonymousClass1 this$0;

    KeysBackup$backupKeys$1$1$onSuccess$1(AnonymousClass1 r1, BackupKeysResult backupKeysResult) {
        this.this$0 = r1;
        this.$info = backupKeysResult;
    }

    public final void run() {
        Log.d(KeysBackup.LOG_TAG, "backupKeys: 5a - Request complete");
        this.this$0.this$0.this$0.mCrypto.getCryptoStore().markBackupDoneForInboundGroupSessions(this.this$0.this$0.$sessions);
        if (this.this$0.this$0.$sessions.size() < 100) {
            Log.d(KeysBackup.LOG_TAG, "backupKeys: All keys have been backed up");
            this.this$0.this$0.this$0.onServerDataRetrieved(this.$info.getCount(), this.$info.getHash());
            this.this$0.this$0.this$0.mKeysBackupStateManager.setState(KeysBackupState.ReadyToBackUp);
            return;
        }
        Log.d(KeysBackup.LOG_TAG, "backupKeys: Continue to back up keys");
        this.this$0.this$0.this$0.mKeysBackupStateManager.setState(KeysBackupState.WillBackUp);
        this.this$0.this$0.this$0.backupKeys();
    }
}
