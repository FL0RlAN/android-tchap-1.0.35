package im.vector.fragments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import androidx.appcompat.app.AlertDialog.Builder;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005¨\u0006\u0006"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/preference/Preference;", "kotlin.jvm.PlatformType", "onPreferenceClick", "im/vector/fragments/VectorSettingsPreferencesFragment$onCreate$16$1"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$9 implements OnPreferenceClickListener {
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$9(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment) {
        this.this$0 = vectorSettingsPreferencesFragment;
    }

    public final boolean onPreferenceClick(Preference preference) {
        boolean isChecked = this.this$0.getHideFromUsersDirectoryPreference().isChecked();
        if (isChecked || !DinsicUtils.isExternalTchapSession(VectorSettingsPreferencesFragment.access$getMSession$p(this.this$0))) {
            this.this$0.hideUserFromUsersDirectory(isChecked);
        } else {
            new Builder(this.this$0.getActivity()).setMessage((int) R.string.settings_show_external_user_in_users_directory_prompt).setPositiveButton((int) R.string.accept, (OnClickListener) new OnClickListener(this) {
                final /* synthetic */ VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$9 this$0;

                {
                    this.this$0 = r1;
                }

                public final void onClick(DialogInterface dialogInterface, int i) {
                    this.this$0.this$0.hideUserFromUsersDirectory(false);
                }
            }).setNegativeButton((int) R.string.cancel, (OnClickListener) new OnClickListener(this) {
                final /* synthetic */ VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$9 this$0;

                {
                    this.this$0 = r1;
                }

                public final void onClick(DialogInterface dialogInterface, int i) {
                    this.this$0.this$0.getHideFromUsersDirectoryPreference().setChecked(true);
                }
            }).setOnCancelListener(new OnCancelListener(this) {
                final /* synthetic */ VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$9 this$0;

                {
                    this.this$0 = r1;
                }

                public final void onCancel(DialogInterface dialogInterface) {
                    this.this$0.this$0.getHideFromUsersDirectoryPreference().setChecked(true);
                }
            }).show();
        }
        return true;
    }
}
