package org.matrix.androidsdk.core;

import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0001\u001a\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0001¨\u0006\u0004"}, d2 = {"convertFromUTF8", "", "s", "convertToUTF8", "matrix-sdk-core_release"}, k = 2, mv = {1, 1, 13})
/* compiled from: StringUtils.kt */
public final class StringUtilsKt {
    public static final String convertToUTF8(String str) {
        Intrinsics.checkParameterIsNotNull(str, "s");
        try {
            Charset forName = Charset.forName("UTF-8");
            Intrinsics.checkExpressionValueIsNotNull(forName, "Charset.forName(charsetName)");
            byte[] bytes = str.getBytes(forName);
            Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
            return new String(bytes, Charsets.UTF_8);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## convertToUTF8()  failed ");
            sb.append(e.getMessage());
            Log.e("Util", sb.toString(), e);
            return str;
        }
    }

    public static final String convertFromUTF8(String str) {
        Intrinsics.checkParameterIsNotNull(str, "s");
        try {
            byte[] bytes = str.getBytes(Charsets.UTF_8);
            Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
            Charset forName = Charset.forName("UTF-8");
            Intrinsics.checkExpressionValueIsNotNull(forName, "Charset.forName(charsetName)");
            return new String(bytes, forName);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## convertFromUTF8()  failed ");
            sb.append(e.getMessage());
            Log.e("Util", sb.toString(), e);
            return str;
        }
    }
}
