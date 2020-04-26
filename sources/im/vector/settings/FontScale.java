package im.vector.settings;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import fr.gouv.tchap.a.R;
import im.vector.VectorApp;
import java.util.Map;
import java.util.Map.Entry;
import kotlin.Metadata;
import kotlin.TuplesKt;
import kotlin.TypeCastException;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref.ObjectRef;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010$\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0011\u001a\u00020\u000eJ\u0006\u0010\u0012\u001a\u00020\u0004J\u0006\u0010\u0013\u001a\u00020\u0004J\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0004J\u000e\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00040\rX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00100\rX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0019"}, d2 = {"Lim/vector/settings/FontScale;", "", "()V", "APPLICATION_FONT_SCALE_KEY", "", "FONT_SCALE_HUGE", "FONT_SCALE_LARGE", "FONT_SCALE_LARGER", "FONT_SCALE_LARGEST", "FONT_SCALE_NORMAL", "FONT_SCALE_SMALL", "FONT_SCALE_TINY", "fontScaleToPrefValue", "", "", "prefValueToNameResId", "", "getFontScale", "getFontScaleDescription", "getFontScalePrefValue", "saveFontScale", "", "scaleValue", "updateFontScale", "fontScaleDescription", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: FontScale.kt */
public final class FontScale {
    private static final String APPLICATION_FONT_SCALE_KEY = "APPLICATION_FONT_SCALE_KEY";
    private static final String FONT_SCALE_HUGE = "FONT_SCALE_HUGE";
    private static final String FONT_SCALE_LARGE = "FONT_SCALE_LARGE";
    private static final String FONT_SCALE_LARGER = "FONT_SCALE_LARGER";
    private static final String FONT_SCALE_LARGEST = "FONT_SCALE_LARGEST";
    private static final String FONT_SCALE_NORMAL = "FONT_SCALE_NORMAL";
    private static final String FONT_SCALE_SMALL = "FONT_SCALE_SMALL";
    private static final String FONT_SCALE_TINY = "FONT_SCALE_TINY";
    public static final FontScale INSTANCE = new FontScale();
    private static final Map<Float, String> fontScaleToPrefValue;
    private static final Map<String, Integer> prefValueToNameResId;

    static {
        Float valueOf = Float.valueOf(0.7f);
        String str = FONT_SCALE_TINY;
        Float valueOf2 = Float.valueOf(0.85f);
        String str2 = FONT_SCALE_SMALL;
        Float valueOf3 = Float.valueOf(1.0f);
        String str3 = FONT_SCALE_NORMAL;
        Float valueOf4 = Float.valueOf(1.15f);
        String str4 = FONT_SCALE_LARGE;
        Float valueOf5 = Float.valueOf(1.3f);
        String str5 = FONT_SCALE_LARGER;
        Float valueOf6 = Float.valueOf(1.45f);
        String str6 = FONT_SCALE_LARGEST;
        Float valueOf7 = Float.valueOf(1.6f);
        String str7 = FONT_SCALE_HUGE;
        fontScaleToPrefValue = MapsKt.mapOf(TuplesKt.to(valueOf, str), TuplesKt.to(valueOf2, str2), TuplesKt.to(valueOf3, str3), TuplesKt.to(valueOf4, str4), TuplesKt.to(valueOf5, str5), TuplesKt.to(valueOf6, str6), TuplesKt.to(valueOf7, str7));
        prefValueToNameResId = MapsKt.mapOf(TuplesKt.to(str, Integer.valueOf(R.string.tiny)), TuplesKt.to(str2, Integer.valueOf(R.string.small)), TuplesKt.to(str3, Integer.valueOf(R.string.normal)), TuplesKt.to(str4, Integer.valueOf(R.string.large)), TuplesKt.to(str5, Integer.valueOf(R.string.larger)), TuplesKt.to(str6, Integer.valueOf(R.string.largest)), TuplesKt.to(str7, Integer.valueOf(R.string.huge)));
    }

    private FontScale() {
    }

    public final String getFontScalePrefValue() {
        VectorApp instance = VectorApp.getInstance();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(instance);
        ObjectRef objectRef = new ObjectRef();
        String str = APPLICATION_FONT_SCALE_KEY;
        boolean contains = defaultSharedPreferences.contains(str);
        T t = FONT_SCALE_NORMAL;
        if (!contains) {
            Intrinsics.checkExpressionValueIsNotNull(instance, "context");
            Resources resources = instance.getResources();
            Intrinsics.checkExpressionValueIsNotNull(resources, "context.resources");
            float f = resources.getConfiguration().fontScale;
            objectRef.element = t;
            if (fontScaleToPrefValue.containsKey(Float.valueOf(f))) {
                T t2 = fontScaleToPrefValue.get(Float.valueOf(f));
                if (t2 != null) {
                    objectRef.element = (String) t2;
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
                }
            }
            Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "preferences");
            Editor edit = defaultSharedPreferences.edit();
            Intrinsics.checkExpressionValueIsNotNull(edit, "editor");
            edit.putString(str, (String) objectRef.element);
            edit.apply();
        } else {
            T string = defaultSharedPreferences.getString(str, t);
            Intrinsics.checkExpressionValueIsNotNull(string, "preferences.getString(AP…E_KEY, FONT_SCALE_NORMAL)");
            objectRef.element = string;
        }
        return (String) objectRef.element;
    }

    public final float getFontScale() {
        String fontScalePrefValue = getFontScalePrefValue();
        if (fontScaleToPrefValue.containsValue(fontScalePrefValue)) {
            for (Entry entry : fontScaleToPrefValue.entrySet()) {
                if (TextUtils.equals((CharSequence) entry.getValue(), fontScalePrefValue)) {
                    return ((Number) entry.getKey()).floatValue();
                }
            }
        }
        return 1.0f;
    }

    public final String getFontScaleDescription() {
        VectorApp instance = VectorApp.getInstance();
        String fontScalePrefValue = getFontScalePrefValue();
        if (prefValueToNameResId.containsKey(fontScalePrefValue)) {
            Object obj = prefValueToNameResId.get(fontScalePrefValue);
            if (obj != null) {
                String string = instance.getString(((Integer) obj).intValue());
                Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(prefVa…eResId[fontScale] as Int)");
                return string;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Int");
        }
        String string2 = instance.getString(R.string.normal);
        Intrinsics.checkExpressionValueIsNotNull(string2, "context.getString(R.string.normal)");
        return string2;
    }

    public final void updateFontScale(String str) {
        Intrinsics.checkParameterIsNotNull(str, "fontScaleDescription");
        VectorApp instance = VectorApp.getInstance();
        for (Entry entry : prefValueToNameResId.entrySet()) {
            if (TextUtils.equals(instance.getString(((Number) entry.getValue()).intValue()), str)) {
                saveFontScale((String) entry.getKey());
            }
        }
        Intrinsics.checkExpressionValueIsNotNull(instance, "context");
        Resources resources = instance.getResources();
        String str2 = "context.resources";
        Intrinsics.checkExpressionValueIsNotNull(resources, str2);
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.fontScale = getFontScale();
        Resources resources2 = instance.getResources();
        Resources resources3 = instance.getResources();
        Intrinsics.checkExpressionValueIsNotNull(resources3, str2);
        resources2.updateConfiguration(configuration, resources3.getDisplayMetrics());
    }

    public final void saveFontScale(String str) {
        Intrinsics.checkParameterIsNotNull(str, "scaleValue");
        VectorApp instance = VectorApp.getInstance();
        if (!TextUtils.isEmpty(str)) {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(instance);
            Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef…haredPreferences(context)");
            Editor edit = defaultSharedPreferences.edit();
            Intrinsics.checkExpressionValueIsNotNull(edit, "editor");
            edit.putString(APPLICATION_FONT_SCALE_KEY, str);
            edit.apply();
        }
    }
}
