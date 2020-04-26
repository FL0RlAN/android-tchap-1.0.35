package im.vector.ui.themes;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import com.google.android.material.tabs.TabLayout;
import im.vector.R;
import im.vector.VectorApp;
import im.vector.activity.VectorGroupDetailsActivity;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000eJ\u001a\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u000e2\b\b\u0001\u0010\u0011\u001a\u00020\u000bH\u0007J\u0016\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u000bJ\u0016\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019J\u0016\u0010\u001a\u001a\u00020\u00152\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u001b\u001a\u00020\u0004J\u0016\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u001eJ \u0010\u001f\u001a\u00020 2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010!\u001a\u00020 2\b\b\u0001\u0010\"\u001a\u00020\u000bJ\u0018\u0010#\u001a\u00020 2\u0006\u0010!\u001a\u00020 2\b\b\u0001\u0010$\u001a\u00020\u000bJ\u0016\u0010%\u001a\u00020\u00152\u0006\u0010&\u001a\u00020'2\u0006\u0010$\u001a\u00020\u000bR\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0\nX\u0004¢\u0006\u0002\n\u0000¨\u0006("}, d2 = {"Lim/vector/ui/themes/ThemeUtils;", "", "()V", "APPLICATION_THEME_KEY", "", "THEME_BLACK_VALUE", "THEME_DARK_VALUE", "THEME_LIGHT_VALUE", "THEME_STATUS_VALUE", "mColorByAttr", "Ljava/util/HashMap;", "", "getApplicationTheme", "context", "Landroid/content/Context;", "getColor", "c", "colorAttribute", "getResourceId", "resourceId", "setActivityTheme", "", "activity", "Landroid/app/Activity;", "otherThemes", "Lim/vector/ui/themes/ActivityOtherThemes;", "setApplicationTheme", "aTheme", "setTabLayoutTheme", "layout", "Lcom/google/android/material/tabs/TabLayout;", "tintDrawable", "Landroid/graphics/drawable/Drawable;", "drawable", "attribute", "tintDrawableWithColor", "color", "tintMenuIcons", "menu", "Landroid/view/Menu;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: ThemeUtils.kt */
public final class ThemeUtils {
    public static final String APPLICATION_THEME_KEY = "APPLICATION_THEME_KEY";
    public static final ThemeUtils INSTANCE = new ThemeUtils();
    private static final String THEME_BLACK_VALUE = "black";
    private static final String THEME_DARK_VALUE = "dark";
    private static final String THEME_LIGHT_VALUE = "light";
    private static final String THEME_STATUS_VALUE = "status";
    private static final HashMap<Integer, Integer> mColorByAttr = new HashMap<>();

    private ThemeUtils() {
    }

    public final String getApplicationTheme(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        String string = PreferenceManager.getDefaultSharedPreferences(context).getString(APPLICATION_THEME_KEY, THEME_LIGHT_VALUE);
        Intrinsics.checkExpressionValueIsNotNull(string, "PreferenceManager.getDef…E_KEY, THEME_LIGHT_VALUE)");
        return string;
    }

    public final void setApplicationTheme(Context context, String str) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(str, "aTheme");
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(APPLICATION_THEME_KEY, str).apply();
        int hashCode = str.hashCode();
        if (hashCode != -892481550) {
            if (hashCode != 3075958) {
                if (hashCode == 93818879 && str.equals(THEME_BLACK_VALUE)) {
                    VectorApp.getInstance().setTheme(R.style.AppTheme_Black);
                    mColorByAttr.clear();
                }
            } else if (str.equals(THEME_DARK_VALUE)) {
                VectorApp.getInstance().setTheme(R.style.AppTheme_Dark);
                mColorByAttr.clear();
            }
        } else if (str.equals("status")) {
            VectorApp.getInstance().setTheme(R.style.AppTheme_Status);
            mColorByAttr.clear();
        }
        VectorApp.getInstance().setTheme(R.style.AppTheme_Light);
        mColorByAttr.clear();
    }

    public final void setActivityTheme(Activity activity, ActivityOtherThemes activityOtherThemes) {
        Intrinsics.checkParameterIsNotNull(activity, "activity");
        Intrinsics.checkParameterIsNotNull(activityOtherThemes, "otherThemes");
        String applicationTheme = getApplicationTheme(activity);
        int hashCode = applicationTheme.hashCode();
        if (hashCode != -892481550) {
            if (hashCode != 3075958) {
                if (hashCode == 93818879 && applicationTheme.equals(THEME_BLACK_VALUE)) {
                    activity.setTheme(activityOtherThemes.getBlack());
                }
            } else if (applicationTheme.equals(THEME_DARK_VALUE)) {
                activity.setTheme(activityOtherThemes.getDark());
            }
        } else if (applicationTheme.equals("status")) {
            activity.setTheme(activityOtherThemes.getStatus());
        }
        mColorByAttr.clear();
    }

    public final void setTabLayoutTheme(Activity activity, TabLayout tabLayout) {
        int i;
        int i2;
        Intrinsics.checkParameterIsNotNull(activity, "activity");
        Intrinsics.checkParameterIsNotNull(tabLayout, "layout");
        if (activity instanceof VectorGroupDetailsActivity) {
            Context context = activity;
            if (TextUtils.equals(getApplicationTheme(context), THEME_LIGHT_VALUE)) {
                i2 = ContextCompat.getColor(context, 17170443);
                i = ContextCompat.getColor(context, fr.gouv.tchap.a.R.color.tab_groups);
            } else if (TextUtils.equals(getApplicationTheme(context), "status")) {
                i2 = ContextCompat.getColor(context, 17170443);
                i = getColor(context, fr.gouv.tchap.a.R.attr.vctr_primary_color);
            } else {
                i2 = ContextCompat.getColor(context, fr.gouv.tchap.a.R.color.tab_groups);
                i = getColor(context, fr.gouv.tchap.a.R.attr.vctr_primary_color);
            }
            tabLayout.setTabTextColors(i2, i2);
            tabLayout.setSelectedTabIndicatorColor(i2);
            tabLayout.setBackgroundColor(i);
        }
    }

    public final int getColor(Context context, int i) {
        int i2;
        Intrinsics.checkParameterIsNotNull(context, "c");
        if (mColorByAttr.containsKey(Integer.valueOf(i))) {
            Object obj = mColorByAttr.get(Integer.valueOf(i));
            if (obj != null) {
                return ((Integer) obj).intValue();
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Int");
        }
        try {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(i, typedValue, true);
            i2 = typedValue.data;
        } catch (Exception unused) {
            i2 = ContextCompat.getColor(context, 17170455);
        }
        mColorByAttr.put(Integer.valueOf(i), Integer.valueOf(i2));
        return i2;
    }

    public final int getResourceId(Context context, int i) {
        Intrinsics.checkParameterIsNotNull(context, "c");
        if (!TextUtils.equals(getApplicationTheme(context), THEME_LIGHT_VALUE) && !TextUtils.equals(getApplicationTheme(context), "status")) {
            return i;
        }
        if (i == fr.gouv.tchap.a.R.drawable.line_divider_dark) {
            i = fr.gouv.tchap.a.R.drawable.line_divider_light;
        } else if (i == fr.gouv.tchap.a.R.style.Floating_Actions_Menu) {
            i = R.style.Floating_Actions_Menu_Light;
        }
        return i;
    }

    public final void tintMenuIcons(Menu menu, int i) {
        Intrinsics.checkParameterIsNotNull(menu, "menu");
        int size = menu.size();
        for (int i2 = 0; i2 < size; i2++) {
            MenuItem item = menu.getItem(i2);
            Intrinsics.checkExpressionValueIsNotNull(item, "item");
            Drawable icon = item.getIcon();
            if (icon != null) {
                Drawable wrap = DrawableCompat.wrap(icon);
                icon.mutate();
                DrawableCompat.setTint(wrap, i);
                item.setIcon(icon);
            }
        }
    }

    public final Drawable tintDrawable(Context context, Drawable drawable, int i) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(drawable, "drawable");
        return tintDrawableWithColor(drawable, getColor(context, i));
    }

    public final Drawable tintDrawableWithColor(Drawable drawable, int i) {
        Intrinsics.checkParameterIsNotNull(drawable, "drawable");
        Drawable wrap = DrawableCompat.wrap(drawable);
        drawable.mutate();
        DrawableCompat.setTint(wrap, i);
        Intrinsics.checkExpressionValueIsNotNull(wrap, "tinted");
        return wrap;
    }
}
