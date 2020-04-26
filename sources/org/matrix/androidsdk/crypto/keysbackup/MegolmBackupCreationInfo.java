package org.matrix.androidsdk.crypto.keysbackup;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\b¨\u0006\u0012"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupCreationInfo;", "", "()V", "algorithm", "", "getAlgorithm", "()Ljava/lang/String;", "setAlgorithm", "(Ljava/lang/String;)V", "authData", "Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupAuthData;", "getAuthData", "()Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupAuthData;", "setAuthData", "(Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupAuthData;)V", "recoveryKey", "getRecoveryKey", "setRecoveryKey", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: MegolmBackupCreationInfo.kt */
public final class MegolmBackupCreationInfo {
    private String algorithm;
    private MegolmBackupAuthData authData;
    private String recoveryKey;

    public MegolmBackupCreationInfo() {
        String str = "";
        this.algorithm = str;
        this.recoveryKey = str;
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public final void setAlgorithm(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.algorithm = str;
    }

    public final MegolmBackupAuthData getAuthData() {
        return this.authData;
    }

    public final void setAuthData(MegolmBackupAuthData megolmBackupAuthData) {
        this.authData = megolmBackupAuthData;
    }

    public final String getRecoveryKey() {
        return this.recoveryKey;
    }

    public final void setRecoveryKey(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        this.recoveryKey = str;
    }
}
