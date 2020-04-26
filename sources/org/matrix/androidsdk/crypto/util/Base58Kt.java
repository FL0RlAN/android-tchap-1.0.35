package org.matrix.androidsdk.crypto.util;

import java.math.BigInteger;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0004\u001a\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0001\u001a\u000e\u0010\b\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0006\u001a\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0001H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u0016\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"ALPHABET", "", "BASE", "Ljava/math/BigInteger;", "kotlin.jvm.PlatformType", "base58decode", "", "input", "base58encode", "decodeToBigInteger", "matrix-sdk-crypto_release"}, k = 2, mv = {1, 1, 13})
/* compiled from: Base58.kt */
public final class Base58Kt {
    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static final BigInteger BASE = BigInteger.valueOf(58);

    public static final String base58encode(byte[] bArr) {
        String str;
        Intrinsics.checkParameterIsNotNull(bArr, "input");
        BigInteger bigInteger = new BigInteger(1, bArr);
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            int compareTo = bigInteger.compareTo(BASE);
            str = ALPHABET;
            if (compareTo < 0) {
                break;
            }
            BigInteger mod = bigInteger.mod(BASE);
            stringBuffer.insert(0, str.charAt(mod.intValue()));
            bigInteger = bigInteger.subtract(mod).divide(BASE);
            Intrinsics.checkExpressionValueIsNotNull(bigInteger, "bi.subtract(mod).divide(BASE)");
        }
        stringBuffer.insert(0, str.charAt(bigInteger.intValue()));
        int length = bArr.length;
        int i = 0;
        while (i < length && bArr[i] == 0) {
            stringBuffer.insert(0, str.charAt(0));
            i++;
        }
        String stringBuffer2 = stringBuffer.toString();
        Intrinsics.checkExpressionValueIsNotNull(stringBuffer2, "s.toString()");
        return stringBuffer2;
    }

    public static final byte[] base58decode(String str) {
        Intrinsics.checkParameterIsNotNull(str, "input");
        byte[] byteArray = decodeToBigInteger(str).toByteArray();
        String str2 = "result";
        if (byteArray[0] == ((byte) 0)) {
            Intrinsics.checkExpressionValueIsNotNull(byteArray, str2);
            byteArray = ArraysKt.copyOfRange(byteArray, 1, byteArray.length);
        }
        Intrinsics.checkExpressionValueIsNotNull(byteArray, str2);
        return byteArray;
    }

    private static final BigInteger decodeToBigInteger(String str) {
        BigInteger valueOf = BigInteger.valueOf(0);
        for (int length = str.length() - 1; length >= 0; length--) {
            valueOf = valueOf.add(BigInteger.valueOf((long) StringsKt.indexOf$default((CharSequence) ALPHABET, str.charAt(length), 0, false, 6, (Object) null)).multiply(BASE.pow((str.length() - 1) - length)));
        }
        Intrinsics.checkExpressionValueIsNotNull(valueOf, "bi");
        return valueOf;
    }
}
