package im.vector.fragments;

import android.app.Activity;
import kotlin.Metadata;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0002H\u0016¨\u0006\u0006"}, d2 = {"im/vector/fragments/VectorSettingsPreferencesFragment$onResume$2", "Lorg/matrix/androidsdk/core/callback/SimpleApiCallback;", "Ljava/lang/Void;", "onSuccess", "", "info", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
public final class VectorSettingsPreferencesFragment$onResume$2 extends SimpleApiCallback<Void> {
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$onResume$2(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, Activity activity) {
        this.this$0 = vectorSettingsPreferencesFragment;
        super(activity);
    }

    public void onSuccess(Void voidR) {
        this.this$0.refreshPushersList();
    }
}
