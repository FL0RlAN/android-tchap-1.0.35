package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import org.matrix.androidsdk.core.listeners.ProgressListener;
import org.matrix.androidsdk.core.listeners.StepProgressListener.Step.ImportingKey;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackup$restoreKeysWithRecoveryKey$1.AnonymousClass4;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, d2 = {"org/matrix/androidsdk/crypto/keysbackup/KeysBackup$restoreKeysWithRecoveryKey$1$4$onSuccess$progressListener$1", "Lorg/matrix/androidsdk/core/listeners/ProgressListener;", "onProgress", "", "progress", "", "total", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
public final class KeysBackup$restoreKeysWithRecoveryKey$1$4$onSuccess$progressListener$1 implements ProgressListener {
    final /* synthetic */ AnonymousClass4 this$0;

    KeysBackup$restoreKeysWithRecoveryKey$1$4$onSuccess$progressListener$1(AnonymousClass4 r1) {
        this.this$0 = r1;
    }

    public void onProgress(int i, int i2) {
        this.this$0.this$0.$stepProgressListener.onStepProgress(new ImportingKey(i, i2));
    }
}
