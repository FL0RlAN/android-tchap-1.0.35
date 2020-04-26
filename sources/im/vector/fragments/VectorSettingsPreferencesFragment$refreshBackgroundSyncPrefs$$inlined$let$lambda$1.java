package im.vector.fragments;

import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import im.vector.push.PushManager;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u00010\u00060\u0006H\n¢\u0006\u0002\b\u0007¨\u0006\b"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/preference/Preference;", "kotlin.jvm.PlatformType", "newValue", "", "onPreferenceChange", "im/vector/fragments/VectorSettingsPreferencesFragment$refreshBackgroundSyncPrefs$2$1"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$refreshBackgroundSyncPrefs$$inlined$let$lambda$1 implements OnPreferenceChangeListener {
    final /* synthetic */ int $delay$inlined;
    final /* synthetic */ PushManager $pushManager$inlined;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$refreshBackgroundSyncPrefs$$inlined$let$lambda$1(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, int i, PushManager pushManager) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$delay$inlined = i;
        this.$pushManager$inlined = pushManager;
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        int i = this.$delay$inlined;
        if (obj != null) {
            try {
                i = Integer.parseInt((String) obj);
            } catch (Exception e) {
                String access$getLOG_TAG$cp = VectorSettingsPreferencesFragment.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## refreshBackgroundSyncPrefs : parseInt failed ");
                sb.append(e.getMessage());
                Log.e(access$getLOG_TAG$cp, sb.toString(), e);
            }
            if (i != this.$delay$inlined) {
                PushManager pushManager = this.$pushManager$inlined;
                Intrinsics.checkExpressionValueIsNotNull(pushManager, "pushManager");
                pushManager.setBackgroundSyncDelay(i * 1000);
                this.this$0.getActivity().runOnUiThread(new Runnable(this) {
                    final /* synthetic */ VectorSettingsPreferencesFragment$refreshBackgroundSyncPrefs$$inlined$let$lambda$1 this$0;

                    {
                        this.this$0 = r1;
                    }

                    public final void run() {
                        this.this$0.this$0.refreshBackgroundSyncPrefs();
                    }
                });
            }
            return false;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
    }
}
