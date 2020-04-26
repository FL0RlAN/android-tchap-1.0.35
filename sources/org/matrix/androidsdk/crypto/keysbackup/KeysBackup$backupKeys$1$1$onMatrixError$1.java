package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackup$backupKeys$1.AnonymousClass1;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
final class KeysBackup$backupKeys$1$1$onMatrixError$1 implements Runnable {
    final /* synthetic */ MatrixError $e;
    final /* synthetic */ AnonymousClass1 this$0;

    KeysBackup$backupKeys$1$1$onMatrixError$1(AnonymousClass1 r1, MatrixError matrixError) {
        this.this$0 = r1;
        this.$e = matrixError;
    }

    public final void run() {
        String access$getLOG_TAG$cp = KeysBackup.LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("backupKeys: backupKeys failed. Error: ");
        sb.append(this.$e.getLocalizedMessage());
        Log.e(access$getLOG_TAG$cp, sb.toString());
        String str = this.$e.errcode;
        if (str != null) {
            int hashCode = str.hashCode();
            if (hashCode == -1391927964 ? str.equals(MatrixError.NOT_FOUND) : !(hashCode != -706555347 || !str.equals(MatrixError.WRONG_ROOM_KEYS_VERSION))) {
                this.this$0.this$0.this$0.mKeysBackupStateManager.setState(KeysBackupState.WrongBackUpVersion);
                ApiCallback access$getBackupAllGroupSessionsCallback$p = this.this$0.this$0.this$0.backupAllGroupSessionsCallback;
                if (access$getBackupAllGroupSessionsCallback$p != null) {
                    access$getBackupAllGroupSessionsCallback$p.onMatrixError(this.$e);
                }
                this.this$0.this$0.this$0.resetBackupAllGroupSessionsListeners();
                this.this$0.this$0.this$0.resetKeysBackupData();
                this.this$0.this$0.this$0.mKeysBackupVersion = null;
                this.this$0.this$0.this$0.checkAndStartKeysBackup();
                return;
            }
        }
        this.this$0.this$0.this$0.mKeysBackupStateManager.setState(KeysBackupState.ReadyToBackUp);
    }
}
