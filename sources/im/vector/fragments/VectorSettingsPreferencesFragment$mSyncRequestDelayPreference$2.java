package im.vector.fragments;

import android.preference.EditTextPreference;
import im.vector.util.PreferencesManager;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u0004\u0018\u00010\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "Landroid/preference/EditTextPreference;", "invoke"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$mSyncRequestDelayPreference$2 extends Lambda implements Function0<EditTextPreference> {
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$mSyncRequestDelayPreference$2(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment) {
        this.this$0 = vectorSettingsPreferencesFragment;
        super(0);
    }

    public final EditTextPreference invoke() {
        return (EditTextPreference) this.this$0.findPreference(PreferencesManager.SETTINGS_SET_SYNC_DELAY_PREFERENCE_KEY);
    }
}
