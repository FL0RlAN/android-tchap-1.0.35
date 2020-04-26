package im.vector.fragments;

import im.vector.settings.VectorLocale;
import java.util.Comparator;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0006"}, d2 = {"<anonymous>", "", "u1", "", "kotlin.jvm.PlatformType", "u2", "compare"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$refreshIgnoredUsersList$1<T> implements Comparator<String> {
    public static final VectorSettingsPreferencesFragment$refreshIgnoredUsersList$1 INSTANCE = new VectorSettingsPreferencesFragment$refreshIgnoredUsersList$1();

    VectorSettingsPreferencesFragment$refreshIgnoredUsersList$1() {
    }

    public final int compare(String str, String str2) {
        Intrinsics.checkExpressionValueIsNotNull(str, "u1");
        Locale applicationLocale = VectorLocale.INSTANCE.getApplicationLocale();
        String str3 = "null cannot be cast to non-null type java.lang.String";
        if (str != null) {
            String lowerCase = str.toLowerCase(applicationLocale);
            String str4 = "(this as java.lang.String).toLowerCase(locale)";
            Intrinsics.checkExpressionValueIsNotNull(lowerCase, str4);
            Intrinsics.checkExpressionValueIsNotNull(str2, "u2");
            Locale applicationLocale2 = VectorLocale.INSTANCE.getApplicationLocale();
            if (str2 != null) {
                String lowerCase2 = str2.toLowerCase(applicationLocale2);
                Intrinsics.checkExpressionValueIsNotNull(lowerCase2, str4);
                return lowerCase.compareTo(lowerCase2);
            }
            throw new TypeCastException(str3);
        }
        throw new TypeCastException(str3);
    }
}
