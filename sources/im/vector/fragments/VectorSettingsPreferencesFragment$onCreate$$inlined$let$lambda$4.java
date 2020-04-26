package im.vector.fragments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import androidx.appcompat.app.AlertDialog.Builder;
import fr.gouv.tchap.a.R;
import im.vector.util.PreferencesManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005¨\u0006\u0006"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/preference/Preference;", "kotlin.jvm.PlatformType", "onPreferenceClick", "im/vector/fragments/VectorSettingsPreferencesFragment$onCreate$12$1"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$4 implements OnPreferenceClickListener {
    final /* synthetic */ Preference $it;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$4(Preference preference, VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment) {
        this.$it = preference;
        this.this$0 = vectorSettingsPreferencesFragment;
    }

    public final boolean onPreferenceClick(Preference preference) {
        new Builder(this.this$0.getActivity()).setSingleChoiceItems((int) R.array.media_saving_choice, PreferencesManager.getSelectedMediasSavingPeriod(this.this$0.getActivity()), (OnClickListener) new OnClickListener(this) {
            final /* synthetic */ VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$4 this$0;

            {
                this.this$0 = r1;
            }

            public final void onClick(DialogInterface dialogInterface, int i) {
                PreferencesManager.setSelectedMediasSavingPeriod(this.this$0.this$0.getActivity(), i);
                dialogInterface.cancel();
                Preference preference = this.this$0.$it;
                Intrinsics.checkExpressionValueIsNotNull(preference, "it");
                preference.setSummary(PreferencesManager.getSelectedMediasSavingPeriodString(this.this$0.this$0.getActivity()));
            }
        }).show();
        return false;
    }
}
