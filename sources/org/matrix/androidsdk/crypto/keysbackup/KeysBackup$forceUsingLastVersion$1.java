package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.ApiFailureCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState;
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0002H\u0016¨\u0006\u0006"}, d2 = {"org/matrix/androidsdk/crypto/keysbackup/KeysBackup$forceUsingLastVersion$1", "Lorg/matrix/androidsdk/core/callback/SimpleApiCallback;", "Lorg/matrix/androidsdk/crypto/model/keys/KeysVersionResult;", "onSuccess", "", "info", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
public final class KeysBackup$forceUsingLastVersion$1 extends SimpleApiCallback<KeysVersionResult> {
    final /* synthetic */ ApiCallback $callback;
    final /* synthetic */ KeysBackup this$0;

    KeysBackup$forceUsingLastVersion$1(KeysBackup keysBackup, ApiCallback apiCallback, ApiFailureCallback apiFailureCallback) {
        this.this$0 = keysBackup;
        this.$callback = apiCallback;
        super(apiFailureCallback);
    }

    public void onSuccess(KeysVersionResult keysVersionResult) {
        KeysVersionResult mKeysBackupVersion = this.this$0.getMKeysBackupVersion();
        String version = mKeysBackupVersion != null ? mKeysBackupVersion.getVersion() : null;
        Object version2 = keysVersionResult != null ? keysVersionResult.getVersion() : null;
        if (version2 == null) {
            if (version == null) {
                this.$callback.onSuccess(Boolean.valueOf(true));
                return;
            }
            this.$callback.onSuccess(Boolean.valueOf(false));
            this.this$0.resetKeysBackupData();
            this.this$0.mKeysBackupVersion = null;
            this.this$0.mKeysBackupStateManager.setState(KeysBackupState.Disabled);
        } else if (version == null) {
            this.$callback.onSuccess(Boolean.valueOf(false));
            this.this$0.checkAndStartWithKeysBackupVersion(keysVersionResult);
        } else if (Intrinsics.areEqual((Object) version, version2)) {
            this.$callback.onSuccess(Boolean.valueOf(true));
        } else {
            this.$callback.onSuccess(Boolean.valueOf(false));
            this.this$0.deleteBackup(version, null);
        }
    }
}
