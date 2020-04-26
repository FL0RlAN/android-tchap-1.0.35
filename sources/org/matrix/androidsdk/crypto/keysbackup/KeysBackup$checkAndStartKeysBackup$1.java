package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState;
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0016J\u0018\u0010\u0007\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0018\u00010\bj\u0004\u0018\u0001`\tH\u0016J\u0012\u0010\n\u001a\u00020\u00042\b\u0010\u000b\u001a\u0004\u0018\u00010\u0002H\u0016J\u0018\u0010\f\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0018\u00010\bj\u0004\u0018\u0001`\tH\u0016¨\u0006\r"}, d2 = {"org/matrix/androidsdk/crypto/keysbackup/KeysBackup$checkAndStartKeysBackup$1", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Lorg/matrix/androidsdk/crypto/model/keys/KeysVersionResult;", "onMatrixError", "", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "keyBackupVersion", "onUnexpectedError", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
public final class KeysBackup$checkAndStartKeysBackup$1 implements ApiCallback<KeysVersionResult> {
    final /* synthetic */ KeysBackup this$0;

    KeysBackup$checkAndStartKeysBackup$1(KeysBackup keysBackup) {
        this.this$0 = keysBackup;
    }

    public void onSuccess(KeysVersionResult keysVersionResult) {
        this.this$0.checkAndStartWithKeysBackupVersion(keysVersionResult);
    }

    public void onUnexpectedError(Exception exc) {
        Log.e(KeysBackup.LOG_TAG, "checkAndStartKeysBackup: Failed to get current version", exc);
        this.this$0.mKeysBackupStateManager.setState(KeysBackupState.Unknown);
    }

    public void onNetworkError(Exception exc) {
        Log.e(KeysBackup.LOG_TAG, "checkAndStartKeysBackup: Failed to get current version", exc);
        this.this$0.mKeysBackupStateManager.setState(KeysBackupState.Unknown);
    }

    public void onMatrixError(MatrixError matrixError) {
        String access$getLOG_TAG$cp = KeysBackup.LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("checkAndStartKeysBackup: Failed to get current version ");
        sb.append(matrixError != null ? matrixError.getLocalizedMessage() : null);
        Log.e(access$getLOG_TAG$cp, sb.toString());
        this.this$0.mKeysBackupStateManager.setState(KeysBackupState.Unknown);
    }
}
