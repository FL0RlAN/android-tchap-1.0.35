package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackup$restoreKeysWithRecoveryKey$1.AnonymousClass4;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
final class KeysBackup$restoreKeysWithRecoveryKey$1$4$onMatrixError$1 implements Runnable {
    final /* synthetic */ MatrixError $e;
    final /* synthetic */ AnonymousClass4 this$0;

    KeysBackup$restoreKeysWithRecoveryKey$1$4$onMatrixError$1(AnonymousClass4 r1, MatrixError matrixError) {
        this.this$0 = r1;
        this.$e = matrixError;
    }

    public final void run() {
        this.this$0.this$0.$callback.onMatrixError(this.$e);
    }
}
