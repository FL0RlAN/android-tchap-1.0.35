package im.vector.fragments;

import android.content.Context;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import im.vector.util.PreferencesManager;
import kotlin.Metadata;
import kotlin.TypeCastException;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u00010\u00060\u0006H\n¢\u0006\u0002\b\u0007¨\u0006\b"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/preference/Preference;", "kotlin.jvm.PlatformType", "newValue", "", "onPreferenceChange", "im/vector/fragments/VectorSettingsPreferencesFragment$onCreate$11$1"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$3 implements OnPreferenceChangeListener {
    final /* synthetic */ Context $appContext$inlined;

    VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$3(Context context) {
        this.$appContext$inlined = context;
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        Context context = this.$appContext$inlined;
        if (obj != null) {
            PreferencesManager.setUseRageshake(context, ((Boolean) obj).booleanValue());
            return true;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Boolean");
    }
}
