package im.vector.fragments;

import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u00010\u00060\u0006H\n¢\u0006\u0002\b\u0007"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/preference/Preference;", "kotlin.jvm.PlatformType", "newValueAsVoid", "", "onPreferenceChange"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$onCreate$7 implements OnPreferenceChangeListener {
    final /* synthetic */ Preference $preference;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$onCreate$7(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, Preference preference) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$preference = preference;
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment = this.this$0;
        String key = ((CheckBoxPreference) this.$preference).getKey();
        Intrinsics.checkExpressionValueIsNotNull(key, "preference.key");
        if (obj != null) {
            vectorSettingsPreferencesFragment.onPushRuleClick(key, ((Boolean) obj).booleanValue());
            return true;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Boolean");
    }
}
