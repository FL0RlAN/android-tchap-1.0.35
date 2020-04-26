package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import kotlin.Unit;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupStateListener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackupStateManager.kt */
final class KeysBackupStateManager$state$1 implements Runnable {
    final /* synthetic */ KeysBackupState $newState;
    final /* synthetic */ KeysBackupStateManager this$0;

    KeysBackupStateManager$state$1(KeysBackupStateManager keysBackupStateManager, KeysBackupState keysBackupState) {
        this.this$0 = keysBackupStateManager;
        this.$newState = keysBackupState;
    }

    public final void run() {
        synchronized (this.this$0.mListeners) {
            for (KeysBackupStateListener onStateChange : this.this$0.mListeners) {
                onStateChange.onStateChange(this.$newState);
            }
            Unit unit = Unit.INSTANCE;
        }
    }
}
