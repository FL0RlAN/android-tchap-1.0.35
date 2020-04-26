package org.matrix.androidsdk.extensions;

import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0001¨\u0006\u0002"}, d2 = {"split4", "", "matrix-sdk_release"}, k = 2, mv = {1, 1, 13})
/* compiled from: StringExtensions.kt */
public final class StringExtensionsKt {
    public static final String split4(String str) {
        Intrinsics.checkParameterIsNotNull(str, "receiver$0");
        return CollectionsKt.joinToString$default(StringsKt.chunked(str, 4), " ", null, null, 0, null, null, 62, null);
    }
}
