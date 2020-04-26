package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\u0010\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003¨\u0006\u0004"}, d2 = {"safeValueOf", "Lorg/matrix/androidsdk/crypto/verification/CancelCode;", "code", "", "matrix-sdk-crypto_release"}, k = 2, mv = {1, 1, 13})
/* compiled from: CancelCode.kt */
public final class CancelCodeKt {
    public static final CancelCode safeValueOf(String str) {
        CancelCode cancelCode;
        CancelCode[] values = CancelCode.values();
        int length = values.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                cancelCode = null;
                break;
            }
            cancelCode = values[i];
            if (Intrinsics.areEqual((Object) str, (Object) cancelCode.getValue())) {
                break;
            }
            i++;
        }
        return cancelCode != null ? cancelCode : CancelCode.User;
    }
}
