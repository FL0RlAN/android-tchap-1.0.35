package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.rest.model.crypto.KeyVerificationStart;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000+\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u00020\u0001J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0014\u0010\b\u001a\u00020\u00052\n\u0010\u0006\u001a\u00060\tj\u0002`\nH\u0016J\u0016\u0010\u000b\u001a\u00020\u00052\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0016J\u0014\u0010\r\u001a\u00020\u00052\n\u0010\u0006\u001a\u00060\tj\u0002`\nH\u0016¨\u0006\u000e"}, d2 = {"org/matrix/androidsdk/crypto/verification/VerificationManager$checkKeysAreDownloaded$1", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Lorg/matrix/androidsdk/crypto/data/MXUsersDevicesMap;", "Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "onMatrixError", "", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "info", "onUnexpectedError", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: VerificationManager.kt */
public final class VerificationManager$checkKeysAreDownloaded$1 implements ApiCallback<MXUsersDevicesMap<MXDeviceInfo>> {
    final /* synthetic */ Function0 $error;
    final /* synthetic */ String $otherUserId;
    final /* synthetic */ CryptoSession $session;
    final /* synthetic */ KeyVerificationStart $startReq;
    final /* synthetic */ Function1 $success;

    VerificationManager$checkKeysAreDownloaded$1(CryptoSession cryptoSession, Function0 function0, String str, KeyVerificationStart keyVerificationStart, Function1 function1) {
        this.$session = cryptoSession;
        this.$error = function0;
        this.$otherUserId = str;
        this.$startReq = keyVerificationStart;
        this.$success = function1;
    }

    public void onUnexpectedError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        this.$session.requireCrypto().getDecryptingThreadHandler().post(new VerificationManager$checkKeysAreDownloaded$1$onUnexpectedError$1(this));
    }

    public void onNetworkError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        this.$session.requireCrypto().getDecryptingThreadHandler().post(new VerificationManager$checkKeysAreDownloaded$1$onNetworkError$1(this));
    }

    public void onMatrixError(MatrixError matrixError) {
        Intrinsics.checkParameterIsNotNull(matrixError, "e");
        this.$session.requireCrypto().getDecryptingThreadHandler().post(new VerificationManager$checkKeysAreDownloaded$1$onMatrixError$1(this));
    }

    public void onSuccess(MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
        Intrinsics.checkParameterIsNotNull(mXUsersDevicesMap, "info");
        this.$session.requireCrypto().getDecryptingThreadHandler().post(new VerificationManager$checkKeysAreDownloaded$1$onSuccess$1(this, mXUsersDevicesMap));
    }
}
