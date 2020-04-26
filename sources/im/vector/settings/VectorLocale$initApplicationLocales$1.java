package im.vector.settings;

import java.util.Comparator;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0006"}, d2 = {"<anonymous>", "", "lhs", "Ljava/util/Locale;", "kotlin.jvm.PlatformType", "rhs", "compare"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorLocale.kt */
final class VectorLocale$initApplicationLocales$1<T> implements Comparator<Locale> {
    public static final VectorLocale$initApplicationLocales$1 INSTANCE = new VectorLocale$initApplicationLocales$1();

    VectorLocale$initApplicationLocales$1() {
    }

    public final int compare(Locale locale, Locale locale2) {
        VectorLocale vectorLocale = VectorLocale.INSTANCE;
        Intrinsics.checkExpressionValueIsNotNull(locale, "lhs");
        String localeToLocalisedString = vectorLocale.localeToLocalisedString(locale);
        VectorLocale vectorLocale2 = VectorLocale.INSTANCE;
        Intrinsics.checkExpressionValueIsNotNull(locale2, "rhs");
        return localeToLocalisedString.compareTo(vectorLocale2.localeToLocalisedString(locale2));
    }
}
