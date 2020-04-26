package org.matrix.androidsdk.core;

import android.text.TextUtils;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\u0010\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0001¨\u0006\u0003"}, d2 = {"getFileExtension", "", "fileUri", "matrix-sdk_release"}, k = 2, mv = {1, 1, 13})
/* compiled from: FileUtils.kt */
public final class FileUtilsKt {
    public static final String getFileExtension(String str) {
        Intrinsics.checkParameterIsNotNull(str, "fileUri");
        CharSequence charSequence = str;
        if (!TextUtils.isEmpty(charSequence)) {
            int lastIndexOf$default = StringsKt.lastIndexOf$default(charSequence, '#', 0, false, 6, (Object) null);
            String str2 = "(this as java.lang.Strin…ing(startIndex, endIndex)";
            boolean z = false;
            if (lastIndexOf$default > 0) {
                str = str.substring(0, lastIndexOf$default);
                Intrinsics.checkExpressionValueIsNotNull(str, str2);
            }
            int lastIndexOf$default2 = StringsKt.lastIndexOf$default((CharSequence) str, '?', 0, false, 6, (Object) null);
            String str3 = "null cannot be cast to non-null type java.lang.String";
            if (lastIndexOf$default2 > 0) {
                if (str != null) {
                    str = str.substring(0, lastIndexOf$default2);
                    Intrinsics.checkExpressionValueIsNotNull(str, str2);
                } else {
                    throw new TypeCastException(str3);
                }
            }
            int lastIndexOf$default3 = StringsKt.lastIndexOf$default((CharSequence) str, '/', 0, false, 6, (Object) null);
            String str4 = "(this as java.lang.String).substring(startIndex)";
            if (lastIndexOf$default3 >= 0) {
                int i = lastIndexOf$default3 + 1;
                if (str != null) {
                    str = str.substring(i);
                    Intrinsics.checkExpressionValueIsNotNull(str, str4);
                } else {
                    throw new TypeCastException(str3);
                }
            }
            CharSequence charSequence2 = str;
            if (charSequence2.length() == 0) {
                z = true;
            }
            if (!z) {
                int lastIndexOf$default4 = StringsKt.lastIndexOf$default(charSequence2, '.', 0, false, 6, (Object) null);
                if (lastIndexOf$default4 >= 0) {
                    int i2 = lastIndexOf$default4 + 1;
                    if (str != null) {
                        String substring = str.substring(i2);
                        Intrinsics.checkExpressionValueIsNotNull(substring, str4);
                        if (!StringsKt.isBlank(substring)) {
                            if (substring != null) {
                                String lowerCase = substring.toLowerCase();
                                Intrinsics.checkExpressionValueIsNotNull(lowerCase, "(this as java.lang.String).toLowerCase()");
                                return lowerCase;
                            }
                            throw new TypeCastException(str3);
                        }
                    } else {
                        throw new TypeCastException(str3);
                    }
                }
            }
        }
        return null;
    }
}
