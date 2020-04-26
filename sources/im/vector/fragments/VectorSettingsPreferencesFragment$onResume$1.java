package im.vector.fragments;

import kotlin.Metadata;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0002H\u0016¨\u0006\u0006"}, d2 = {"im/vector/fragments/VectorSettingsPreferencesFragment$onResume$1", "Lorg/matrix/androidsdk/core/callback/SimpleApiCallback;", "Ljava/lang/Void;", "onSuccess", "", "info", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
public final class VectorSettingsPreferencesFragment$onResume$1 extends SimpleApiCallback<Void> {
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$onResume$1(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment) {
        this.this$0 = vectorSettingsPreferencesFragment;
    }

    public void onSuccess(Void voidR) {
        if (this.this$0.getActivity() != null) {
            this.this$0.getActivity().runOnUiThread(new VectorSettingsPreferencesFragment$onResume$1$onSuccess$1(this));
        }
    }
}
