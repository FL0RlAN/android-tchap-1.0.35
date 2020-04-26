package org.matrix.androidsdk.crypto.keysbackup;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0007HÆ\u0003J'\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0007HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u0018"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/GeneratePrivateKeyResult;", "", "privateKey", "", "salt", "", "iterations", "", "([BLjava/lang/String;I)V", "getIterations", "()I", "getPrivateKey", "()[B", "getSalt", "()Ljava/lang/String;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackupPassword.kt */
public final class GeneratePrivateKeyResult {
    private final int iterations;
    private final byte[] privateKey;
    private final String salt;

    public static /* synthetic */ GeneratePrivateKeyResult copy$default(GeneratePrivateKeyResult generatePrivateKeyResult, byte[] bArr, String str, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            bArr = generatePrivateKeyResult.privateKey;
        }
        if ((i2 & 2) != 0) {
            str = generatePrivateKeyResult.salt;
        }
        if ((i2 & 4) != 0) {
            i = generatePrivateKeyResult.iterations;
        }
        return generatePrivateKeyResult.copy(bArr, str, i);
    }

    public final byte[] component1() {
        return this.privateKey;
    }

    public final String component2() {
        return this.salt;
    }

    public final int component3() {
        return this.iterations;
    }

    public final GeneratePrivateKeyResult copy(byte[] bArr, String str, int i) {
        Intrinsics.checkParameterIsNotNull(bArr, "privateKey");
        Intrinsics.checkParameterIsNotNull(str, "salt");
        return new GeneratePrivateKeyResult(bArr, str, i);
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof GeneratePrivateKeyResult) {
                GeneratePrivateKeyResult generatePrivateKeyResult = (GeneratePrivateKeyResult) obj;
                if (Intrinsics.areEqual((Object) this.privateKey, (Object) generatePrivateKeyResult.privateKey) && Intrinsics.areEqual((Object) this.salt, (Object) generatePrivateKeyResult.salt)) {
                    if (this.iterations == generatePrivateKeyResult.iterations) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        byte[] bArr = this.privateKey;
        int i = 0;
        int hashCode = (bArr != null ? Arrays.hashCode(bArr) : 0) * 31;
        String str = this.salt;
        if (str != null) {
            i = str.hashCode();
        }
        return ((hashCode + i) * 31) + this.iterations;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GeneratePrivateKeyResult(privateKey=");
        sb.append(Arrays.toString(this.privateKey));
        sb.append(", salt=");
        sb.append(this.salt);
        sb.append(", iterations=");
        sb.append(this.iterations);
        sb.append(")");
        return sb.toString();
    }

    public GeneratePrivateKeyResult(byte[] bArr, String str, int i) {
        Intrinsics.checkParameterIsNotNull(bArr, "privateKey");
        Intrinsics.checkParameterIsNotNull(str, "salt");
        this.privateKey = bArr;
        this.salt = str;
        this.iterations = i;
    }

    public final byte[] getPrivateKey() {
        return this.privateKey;
    }

    public final String getSalt() {
        return this.salt;
    }

    public final int getIterations() {
        return this.iterations;
    }
}
