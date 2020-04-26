package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState;
import org.matrix.androidsdk.crypto.model.keys.CreateKeysBackupVersionBody;
import org.matrix.androidsdk.crypto.model.keys.KeysVersion;
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0016J\u0018\u0010\u0007\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0018\u00010\bj\u0004\u0018\u0001`\tH\u0016J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0002H\u0016J\u0018\u0010\f\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0018\u00010\bj\u0004\u0018\u0001`\tH\u0016¨\u0006\r"}, d2 = {"org/matrix/androidsdk/crypto/keysbackup/KeysBackup$createKeysBackupVersion$1", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Lorg/matrix/androidsdk/crypto/model/keys/KeysVersion;", "onMatrixError", "", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "info", "onUnexpectedError", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
public final class KeysBackup$createKeysBackupVersion$1 implements ApiCallback<KeysVersion> {
    final /* synthetic */ ApiCallback $callback;
    final /* synthetic */ CreateKeysBackupVersionBody $createKeysBackupVersionBody;
    final /* synthetic */ KeysBackup this$0;

    KeysBackup$createKeysBackupVersion$1(KeysBackup keysBackup, CreateKeysBackupVersionBody createKeysBackupVersionBody, ApiCallback apiCallback) {
        this.this$0 = keysBackup;
        this.$createKeysBackupVersionBody = createKeysBackupVersionBody;
        this.$callback = apiCallback;
    }

    public void onSuccess(KeysVersion keysVersion) {
        Intrinsics.checkParameterIsNotNull(keysVersion, "info");
        this.this$0.mCrypto.getCryptoStore().resetBackupMarkers();
        KeysVersionResult keysVersionResult = new KeysVersionResult();
        keysVersionResult.setAlgorithm(this.$createKeysBackupVersionBody.getAlgorithm());
        keysVersionResult.setAuthData(this.$createKeysBackupVersionBody.getAuthData());
        keysVersionResult.setVersion(keysVersion.getVersion());
        keysVersionResult.setCount(Integer.valueOf(0));
        keysVersionResult.setHash(null);
        this.this$0.enableKeysBackup(keysVersionResult);
        this.$callback.onSuccess(keysVersion);
    }

    public void onUnexpectedError(Exception exc) {
        this.this$0.mKeysBackupStateManager.setState(KeysBackupState.Disabled);
        this.$callback.onUnexpectedError(exc);
    }

    public void onNetworkError(Exception exc) {
        this.this$0.mKeysBackupStateManager.setState(KeysBackupState.Disabled);
        this.$callback.onNetworkError(exc);
    }

    public void onMatrixError(MatrixError matrixError) {
        this.this$0.mKeysBackupStateManager.setState(KeysBackupState.Disabled);
        this.$callback.onMatrixError(matrixError);
    }
}
