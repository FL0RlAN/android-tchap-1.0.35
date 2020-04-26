package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import org.matrix.androidsdk.core.listeners.ProgressListener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, d2 = {"org/matrix/androidsdk/crypto/keysbackup/KeysBackup$prepareKeysBackupVersion$1$backgroundProgressListener$1", "Lorg/matrix/androidsdk/core/listeners/ProgressListener;", "onProgress", "", "progress", "", "total", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
public final class KeysBackup$prepareKeysBackupVersion$1$backgroundProgressListener$1 implements ProgressListener {
    final /* synthetic */ KeysBackup$prepareKeysBackupVersion$1 this$0;

    KeysBackup$prepareKeysBackupVersion$1$backgroundProgressListener$1(KeysBackup$prepareKeysBackupVersion$1 keysBackup$prepareKeysBackupVersion$1) {
        this.this$0 = keysBackup$prepareKeysBackupVersion$1;
    }

    public void onProgress(int i, int i2) {
        this.this$0.this$0.mCrypto.getUIHandler().post(new KeysBackup$prepareKeysBackupVersion$1$backgroundProgressListener$1$onProgress$1(this, i, i2));
    }
}
