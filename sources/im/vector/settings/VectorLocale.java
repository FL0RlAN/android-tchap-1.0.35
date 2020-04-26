package im.vector.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Pair;
import fr.gouv.tchap.a.R;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.GlobalScope;
import org.matrix.androidsdk.core.Log;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J \u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\u000e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0014\u001a\u00020\u0015J\u0010\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u000e\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\nJ\u0016\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\nR\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \b*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u000b\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\n@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R*\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\n0\u000f2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u000f@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006\u001e"}, d2 = {"Lim/vector/settings/VectorLocale;", "", "()V", "APPLICATION_LOCALE_COUNTRY_KEY", "", "APPLICATION_LOCALE_LANGUAGE_KEY", "APPLICATION_LOCALE_VARIANT_KEY", "LOG_TAG", "kotlin.jvm.PlatformType", "<set-?>", "Ljava/util/Locale;", "applicationLocale", "getApplicationLocale", "()Ljava/util/Locale;", "defaultLocale", "Ljava/util/ArrayList;", "supportedLocales", "getSupportedLocales", "()Ljava/util/ArrayList;", "getString", "context", "Landroid/content/Context;", "locale", "resourceId", "", "init", "", "initApplicationLocales", "localeToLocalisedString", "saveApplicationLocale", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorLocale.kt */
public final class VectorLocale {
    private static final String APPLICATION_LOCALE_COUNTRY_KEY = "APPLICATION_LOCALE_COUNTRY_KEY";
    private static final String APPLICATION_LOCALE_LANGUAGE_KEY = "APPLICATION_LOCALE_LANGUAGE_KEY";
    private static final String APPLICATION_LOCALE_VARIANT_KEY = "APPLICATION_LOCALE_VARIANT_KEY";
    public static final VectorLocale INSTANCE = new VectorLocale();
    private static final String LOG_TAG = INSTANCE.getClass().getSimpleName();
    private static Locale applicationLocale = defaultLocale;
    private static final Locale defaultLocale = new Locale("fr", "FR");
    private static ArrayList<Locale> supportedLocales = new ArrayList<>();

    private VectorLocale() {
    }

    public final ArrayList<Locale> getSupportedLocales() {
        return supportedLocales;
    }

    public final Locale getApplicationLocale() {
        return applicationLocale;
    }

    public final void init(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String str = APPLICATION_LOCALE_LANGUAGE_KEY;
        if (defaultSharedPreferences.contains(str)) {
            String str2 = "";
            applicationLocale = new Locale(defaultSharedPreferences.getString(str, str2), defaultSharedPreferences.getString(APPLICATION_LOCALE_COUNTRY_KEY, str2), defaultSharedPreferences.getString(APPLICATION_LOCALE_VARIANT_KEY, str2));
        } else {
            Locale.setDefault(defaultLocale);
            applicationLocale = defaultLocale;
            saveApplicationLocale(context, applicationLocale);
        }
        BuildersKt__Builders_commonKt.launch$default(GlobalScope.INSTANCE, null, null, new VectorLocale$init$1(context, null), 3, null);
    }

    public final void saveApplicationLocale(Context context, Locale locale) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(locale, "locale");
        applicationLocale = locale;
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef…haredPreferences(context)");
        Editor edit = defaultSharedPreferences.edit();
        Intrinsics.checkExpressionValueIsNotNull(edit, "editor");
        String language = locale.getLanguage();
        boolean isEmpty = TextUtils.isEmpty(language);
        String str = APPLICATION_LOCALE_LANGUAGE_KEY;
        if (isEmpty) {
            edit.remove(str);
        } else {
            edit.putString(str, language);
        }
        String country = locale.getCountry();
        boolean isEmpty2 = TextUtils.isEmpty(country);
        String str2 = APPLICATION_LOCALE_COUNTRY_KEY;
        if (isEmpty2) {
            edit.remove(str2);
        } else {
            edit.putString(str2, country);
        }
        String variant = locale.getVariant();
        boolean isEmpty3 = TextUtils.isEmpty(variant);
        String str3 = APPLICATION_LOCALE_VARIANT_KEY;
        if (isEmpty3) {
            edit.remove(str3);
        } else {
            edit.putString(str3, variant);
        }
        edit.apply();
    }

    private final String getString(Context context, Locale locale, int i) {
        if (VERSION.SDK_INT >= 17) {
            Resources resources = context.getResources();
            Intrinsics.checkExpressionValueIsNotNull(resources, "context.resources");
            Configuration configuration = new Configuration(resources.getConfiguration());
            configuration.setLocale(locale);
            try {
                return context.createConfigurationContext(configuration).getText(i).toString();
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getString() failed : ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
                String string = context.getString(i);
                Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(resourceId)");
                return string;
            }
        } else {
            Resources resources2 = context.getResources();
            Intrinsics.checkExpressionValueIsNotNull(resources2, "resources");
            Configuration configuration2 = resources2.getConfiguration();
            Locale locale2 = configuration2.locale;
            configuration2.locale = locale;
            resources2.updateConfiguration(configuration2, null);
            String string2 = resources2.getString(i);
            Intrinsics.checkExpressionValueIsNotNull(string2, "resources.getString(resourceId)");
            configuration2.locale = locale2;
            resources2.updateConfiguration(configuration2, null);
            return string2;
        }
    }

    /* access modifiers changed from: private */
    public final void initApplicationLocales(Context context) {
        Locale[] availableLocales;
        HashSet hashSet = new HashSet();
        try {
            for (Locale locale : Locale.getAvailableLocales()) {
                Intrinsics.checkExpressionValueIsNotNull(locale, "locale");
                hashSet.add(new Pair(getString(context, locale, R.string.resources_language), getString(context, locale, R.string.resources_country_code)));
            }
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getApplicationLocales() : failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            hashSet.add(new Pair(context.getString(R.string.resources_language), context.getString(R.string.resources_country_code)));
        }
        supportedLocales.clear();
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            supportedLocales.add(new Locale((String) pair.first, (String) pair.second));
        }
        CollectionsKt.sortWith(supportedLocales, VectorLocale$initApplicationLocales$1.INSTANCE);
    }

    public final String localeToLocalisedString(Locale locale) {
        Intrinsics.checkParameterIsNotNull(locale, "locale");
        String displayLanguage = locale.getDisplayLanguage(locale);
        if (!TextUtils.isEmpty(locale.getDisplayCountry(locale))) {
            StringBuilder sb = new StringBuilder();
            sb.append(displayLanguage);
            sb.append(" (");
            sb.append(locale.getDisplayCountry(locale));
            sb.append(")");
            displayLanguage = sb.toString();
        }
        Intrinsics.checkExpressionValueIsNotNull(displayLanguage, "res");
        return displayLanguage;
    }
}
