package im.vector.util;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\u001a\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0001\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"HTTPS_SCHEME", "", "HTTP_SCHEME", "removeUrlScheme", "aUrl", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 2, mv = {1, 1, 13})
/* compiled from: UrlUtil.kt */
public final class UrlUtilKt {
    public static final String HTTPS_SCHEME = "https://";
    private static final String HTTP_SCHEME = "http://";

    public static final String removeUrlScheme(String str) {
        if (str == null) {
            return str;
        }
        String str2 = "(this as java.lang.String).substring(startIndex)";
        if (StringsKt.startsWith$default(str, HTTP_SCHEME, false, 2, null)) {
            String substring = str.substring(7);
            Intrinsics.checkExpressionValueIsNotNull(substring, str2);
            return substring;
        } else if (!StringsKt.startsWith$default(str, HTTPS_SCHEME, false, 2, null)) {
            return str;
        } else {
            String substring2 = str.substring(8);
            Intrinsics.checkExpressionValueIsNotNull(substring2, str2);
            return substring2;
        }
    }
}
