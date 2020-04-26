package im.vector.fragments;

import android.app.Activity;
import android.content.Context;
import android.preference.Preference;
import im.vector.preference.VectorCustomActionEditTextPreference.OnPreferenceLongClickListener;
import im.vector.util.SystemUtilsKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.crypto.model.rest.DeviceInfo;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/preference/Preference;", "kotlin.jvm.PlatformType", "onPreferenceLongClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$refreshCryptographyPreference$2 implements OnPreferenceLongClickListener {
    final /* synthetic */ DeviceInfo $aMyDeviceInfo;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$refreshCryptographyPreference$2(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, DeviceInfo deviceInfo) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$aMyDeviceInfo = deviceInfo;
    }

    public final boolean onPreferenceLongClick(Preference preference) {
        Activity activity = this.this$0.getActivity();
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
        Context context = activity;
        String str = this.$aMyDeviceInfo.display_name;
        Intrinsics.checkExpressionValueIsNotNull(str, "aMyDeviceInfo.display_name");
        SystemUtilsKt.copyToClipboard(context, str);
        return true;
    }
}
