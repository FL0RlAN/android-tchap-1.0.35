package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import org.matrix.androidsdk.core.listeners.ProgressListener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
final class KeysBackup$getBackupProgress$1 implements Runnable {
    final /* synthetic */ ProgressListener $progressListener;
    final /* synthetic */ KeysBackup this$0;

    KeysBackup$getBackupProgress$1(KeysBackup keysBackup, ProgressListener progressListener) {
        this.this$0 = keysBackup;
        this.$progressListener = progressListener;
    }

    public final void run() {
        final int inboundGroupSessionsCount = this.this$0.mCrypto.getCryptoStore().inboundGroupSessionsCount(true);
        final int inboundGroupSessionsCount2 = this.this$0.mCrypto.getCryptoStore().inboundGroupSessionsCount(false);
        this.this$0.mCrypto.getUIHandler().post(new Runnable(this) {
            final /* synthetic */ KeysBackup$getBackupProgress$1 this$0;

            {
                this.this$0 = r1;
            }

            public final void run() {
                this.this$0.$progressListener.onProgress(inboundGroupSessionsCount, inboundGroupSessionsCount2);
            }
        });
    }
}
