package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.ApiFailureCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u0002H\u0016¨\u0006\t"}, d2 = {"org/matrix/androidsdk/crypto/keysbackup/KeysBackup$getVersion$1", "Lorg/matrix/androidsdk/core/callback/SimpleApiCallback;", "Lorg/matrix/androidsdk/crypto/model/keys/KeysVersionResult;", "onMatrixError", "", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onSuccess", "info", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
public final class KeysBackup$getVersion$1 extends SimpleApiCallback<KeysVersionResult> {
    final /* synthetic */ ApiCallback $callback;

    KeysBackup$getVersion$1(ApiCallback apiCallback, ApiFailureCallback apiFailureCallback) {
        this.$callback = apiCallback;
        super(apiFailureCallback);
    }

    public void onSuccess(KeysVersionResult keysVersionResult) {
        Intrinsics.checkParameterIsNotNull(keysVersionResult, "info");
        this.$callback.onSuccess(keysVersionResult);
    }

    public void onMatrixError(MatrixError matrixError) {
        Intrinsics.checkParameterIsNotNull(matrixError, "e");
        if (Intrinsics.areEqual((Object) matrixError.errcode, (Object) MatrixError.NOT_FOUND)) {
            this.$callback.onSuccess(null);
        } else {
            this.$callback.onMatrixError(matrixError);
        }
    }
}
