package im.vector.fragments;

import android.content.Context;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import im.vector.Matrix;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005¨\u0006\u0006"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/preference/Preference;", "kotlin.jvm.PlatformType", "onPreferenceClick", "im/vector/fragments/VectorSettingsPreferencesFragment$onCreate$14$2"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$8 implements OnPreferenceClickListener {
    final /* synthetic */ Context $appContext$inlined;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$8(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, Context context) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$appContext$inlined = context;
    }

    public final boolean onPreferenceClick(Preference preference) {
        this.this$0.displayLoadingView();
        Matrix.getInstance(this.$appContext$inlined).reloadSessions(this.$appContext$inlined);
        return false;
    }
}
