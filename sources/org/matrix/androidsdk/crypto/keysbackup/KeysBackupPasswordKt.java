package org.matrix.androidsdk.crypto.keysbackup;

import java.nio.charset.Charset;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.listeners.ProgressListener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a*\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00012\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0003\u001a\u001a\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0007\u001a\b\u0010\r\u001a\u00020\u0006H\u0002\u001a,\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u00012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\nH\u0007\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"DEFAULT_ITERATION", "", "SALT_LENGTH", "deriveKey", "", "password", "", "salt", "iterations", "progressListener", "Lorg/matrix/androidsdk/core/listeners/ProgressListener;", "generatePrivateKeyWithPassword", "Lorg/matrix/androidsdk/crypto/keysbackup/GeneratePrivateKeyResult;", "generateSalt", "retrievePrivateKeyWithPassword", "matrix-sdk-crypto_release"}, k = 2, mv = {1, 1, 13})
/* compiled from: KeysBackupPassword.kt */
public final class KeysBackupPasswordKt {
    private static final int DEFAULT_ITERATION = 500000;
    private static final int SALT_LENGTH = 32;

    public static final GeneratePrivateKeyResult generatePrivateKeyWithPassword(String str, ProgressListener progressListener) {
        Intrinsics.checkParameterIsNotNull(str, "password");
        String generateSalt = generateSalt();
        return new GeneratePrivateKeyResult(deriveKey(str, generateSalt, 500000, progressListener), generateSalt, 500000);
    }

    public static /* synthetic */ byte[] retrievePrivateKeyWithPassword$default(String str, String str2, int i, ProgressListener progressListener, int i2, Object obj) {
        if ((i2 & 8) != 0) {
            progressListener = null;
        }
        return retrievePrivateKeyWithPassword(str, str2, i, progressListener);
    }

    public static final byte[] retrievePrivateKeyWithPassword(String str, String str2, int i, ProgressListener progressListener) {
        Intrinsics.checkParameterIsNotNull(str, "password");
        Intrinsics.checkParameterIsNotNull(str2, "salt");
        return deriveKey(str, str2, i, progressListener);
    }

    private static final byte[] deriveKey(String str, String str2, int i, ProgressListener progressListener) {
        long currentTimeMillis = System.currentTimeMillis();
        String str3 = "HmacSHA512";
        Mac instance = Mac.getInstance(str3);
        Charset charset = Charsets.UTF_8;
        String str4 = "null cannot be cast to non-null type java.lang.String";
        if (str != null) {
            byte[] bytes = str.getBytes(charset);
            String str5 = "(this as java.lang.String).getBytes(charset)";
            Intrinsics.checkExpressionValueIsNotNull(bytes, str5);
            instance.init(new SecretKeySpec(bytes, str3));
            byte[] bArr = new byte[32];
            byte[] bArr2 = new byte[64];
            Charset charset2 = Charsets.UTF_8;
            if (str2 != null) {
                byte[] bytes2 = str2.getBytes(charset2);
                Intrinsics.checkExpressionValueIsNotNull(bytes2, str5);
                instance.update(bytes2);
                instance.update(new byte[]{0, 0, 0, 1});
                instance.doFinal(bArr2, 0);
                System.arraycopy(bArr2, 0, bArr, 0, bArr.length);
                int i2 = -1;
                int i3 = 2;
                if (2 <= i) {
                    while (true) {
                        instance.update(bArr2);
                        instance.doFinal(bArr2, 0);
                        int length = bArr.length;
                        for (int i4 = 0; i4 < length; i4++) {
                            bArr[i4] = (byte) (bArr[i4] ^ bArr2[i4]);
                        }
                        int i5 = i3 + 1;
                        int i6 = (i5 * 100) / i;
                        if (i6 != i2) {
                            if (progressListener != null) {
                                progressListener.onProgress(i6, 100);
                            }
                            i2 = i6;
                        }
                        if (i3 == i) {
                            break;
                        }
                        i3 = i5;
                    }
                }
                StringBuilder sb = new StringBuilder();
                sb.append("## deriveKeys() : ");
                sb.append(i);
                sb.append(" in ");
                sb.append(System.currentTimeMillis() - currentTimeMillis);
                sb.append(" ms");
                Log.d("KeysBackupPassword", sb.toString());
                return bArr;
            }
            throw new TypeCastException(str4);
        }
        throw new TypeCastException(str4);
    }

    private static final String generateSalt() {
        String str = "";
        do {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(UUID.randomUUID().toString());
            str = sb.toString();
        } while (str.length() < 32);
        if (str != null) {
            String substring = str.substring(0, 32);
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            return substring;
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
    }
}
