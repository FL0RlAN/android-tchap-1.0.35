package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupStateListener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, d2 = {"org/matrix/androidsdk/crypto/keysbackup/KeysBackup$backupAllGroupSessions$1$onProgress$1", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupStateListener;", "onStateChange", "", "newState", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupState;", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
public final class KeysBackup$backupAllGroupSessions$1$onProgress$1 implements KeysBackupStateListener {
    final /* synthetic */ KeysBackup$backupAllGroupSessions$1 this$0;

    KeysBackup$backupAllGroupSessions$1$onProgress$1(KeysBackup$backupAllGroupSessions$1 keysBackup$backupAllGroupSessions$1) {
        this.this$0 = keysBackup$backupAllGroupSessions$1;
    }

    public void onStateChange(KeysBackupState keysBackupState) {
        Intrinsics.checkParameterIsNotNull(keysBackupState, "newState");
        this.this$0.this$0.getBackupProgress(new KeysBackup$backupAllGroupSessions$1$onProgress$1$onStateChange$1(this));
    }
}
