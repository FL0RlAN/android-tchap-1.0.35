package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackup$backupKeys$1.AnonymousClass1;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
final class KeysBackup$backupKeys$1$1$onNetworkError$1 implements Runnable {
    final /* synthetic */ Exception $e;
    final /* synthetic */ AnonymousClass1 this$0;

    KeysBackup$backupKeys$1$1$onNetworkError$1(AnonymousClass1 r1, Exception exc) {
        this.this$0 = r1;
        this.$e = exc;
    }

    public final void run() {
        ApiCallback access$getBackupAllGroupSessionsCallback$p = this.this$0.this$0.this$0.backupAllGroupSessionsCallback;
        if (access$getBackupAllGroupSessionsCallback$p != null) {
            access$getBackupAllGroupSessionsCallback$p.onNetworkError(this.$e);
        }
        this.this$0.this$0.this$0.resetBackupAllGroupSessionsListeners();
        this.this$0.onError();
    }
}
