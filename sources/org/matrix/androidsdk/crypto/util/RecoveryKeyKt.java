package org.matrix.androidsdk.crypto.util;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u001a\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b\u001a\u0012\u0010\t\u001a\u0004\u0018\u00010\b2\b\u0010\n\u001a\u0004\u0018\u00010\u0006\u001a\u0010\u0010\u000b\u001a\u00020\f2\b\u0010\n\u001a\u0004\u0018\u00010\u0006\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"CHAR_0", "", "CHAR_1", "RECOVERY_KEY_LENGTH", "", "computeRecoveryKey", "", "curve25519Key", "", "extractCurveKeyFromRecoveryKey", "recoveryKey", "isValidRecoveryKey", "", "matrix-sdk-crypto_release"}, k = 2, mv = {1, 1, 13})
/* compiled from: RecoveryKey.kt */
public final class RecoveryKeyKt {
    private static final byte CHAR_0 = -117;
    private static final byte CHAR_1 = 1;
    private static final int RECOVERY_KEY_LENGTH = 35;

    public static final boolean isValidRecoveryKey(String str) {
        return extractCurveKeyFromRecoveryKey(str) != null;
    }

    public static final String computeRecoveryKey(byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(bArr, "curve25519Key");
        byte[] bArr2 = new byte[(bArr.length + 3)];
        bArr2[0] = CHAR_0;
        bArr2[1] = 1;
        byte b = (byte) -118;
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            bArr2[i + 2] = bArr[i];
            b = (byte) (b ^ bArr[i]);
        }
        bArr2[bArr.length + 2] = b;
        return Base58Kt.base58encode(bArr2);
    }

    public static final byte[] extractCurveKeyFromRecoveryKey(String str) {
        if (str == null) {
            return null;
        }
        byte[] base58decode = Base58Kt.base58decode(new Regex("\\s").replace((CharSequence) str, ""));
        if (base58decode.length != 35 || base58decode[0] != -117 || base58decode[1] != 1) {
            return null;
        }
        byte b = 0;
        for (int i = 0; i < 35; i++) {
            b = (byte) (b ^ base58decode[i]);
        }
        if (b != ((byte) 0)) {
            return null;
        }
        byte[] bArr = new byte[(base58decode.length - 3)];
        int length = base58decode.length - 1;
        for (int i2 = 2; i2 < length; i2++) {
            bArr[i2 - 2] = base58decode[i2];
        }
        return bArr;
    }
}
