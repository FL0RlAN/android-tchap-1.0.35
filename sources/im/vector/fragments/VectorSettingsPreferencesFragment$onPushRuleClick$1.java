package im.vector.fragments;

import android.app.Activity;
import android.widget.Toast;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.BingRulesManager.onBingRuleUpdateListener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0003H\u0016J\b\u0010\u0007\u001a\u00020\u0003H\u0002¨\u0006\b"}, d2 = {"im/vector/fragments/VectorSettingsPreferencesFragment$onPushRuleClick$1", "Lorg/matrix/androidsdk/core/BingRulesManager$onBingRuleUpdateListener;", "onBingRuleUpdateFailure", "", "errorMessage", "", "onBingRuleUpdateSuccess", "onDone", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
public final class VectorSettingsPreferencesFragment$onPushRuleClick$1 implements onBingRuleUpdateListener {
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$onPushRuleClick$1(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment) {
        this.this$0 = vectorSettingsPreferencesFragment;
    }

    private final void onDone() {
        this.this$0.refreshDisplay();
        this.this$0.hideLoadingView();
    }

    public void onBingRuleUpdateSuccess() {
        onDone();
    }

    public void onBingRuleUpdateFailure(String str) {
        Intrinsics.checkParameterIsNotNull(str, "errorMessage");
        Activity activity = this.this$0.getActivity();
        if (activity != null) {
            Toast makeText = Toast.makeText(activity, str, 0);
            makeText.show();
            Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        }
        onDone();
    }
}
