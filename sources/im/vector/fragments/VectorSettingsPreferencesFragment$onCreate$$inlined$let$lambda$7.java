package im.vector.fragments;

import android.content.Context;
import android.preference.Preference;
import android.text.format.Formatter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0002H\u0016¨\u0006\u0006¸\u0006\u0000"}, d2 = {"im/vector/fragments/VectorSettingsPreferencesFragment$onCreate$14$1", "Lorg/matrix/androidsdk/core/callback/SimpleApiCallback;", "", "onSuccess", "", "size", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
public final class VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$7 extends SimpleApiCallback<Long> {
    final /* synthetic */ Context $appContext$inlined;
    final /* synthetic */ Preference $it;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$7(Preference preference, VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, Context context) {
        this.$it = preference;
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$appContext$inlined = context;
    }

    public /* bridge */ /* synthetic */ void onSuccess(Object obj) {
        onSuccess(((Number) obj).longValue());
    }

    public void onSuccess(long j) {
        if (this.this$0.getActivity() != null) {
            Preference preference = this.$it;
            Intrinsics.checkExpressionValueIsNotNull(preference, "it");
            preference.setSummary(Formatter.formatFileSize(this.this$0.getActivity(), j));
        }
    }
}
