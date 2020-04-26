package im.vector.fragments;

import im.vector.push.PushManager;
import kotlin.Metadata;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\b\u0010\u0003\u001a\u00020\u0004H\u0002J\u0012\u0010\u0005\u001a\u00020\u00042\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0016J\u0012\u0010\b\u001a\u00020\u00042\b\u0010\u0006\u001a\u0004\u0018\u00010\tH\u0016J\u0012\u0010\n\u001a\u00020\u00042\b\u0010\u000b\u001a\u0004\u0018\u00010\u0002H\u0016J\u0012\u0010\f\u001a\u00020\u00042\b\u0010\u0006\u001a\u0004\u0018\u00010\tH\u0016¨\u0006\r"}, d2 = {"im/vector/fragments/VectorSettingsPreferencesFragment$onPushRuleClick$listener$1", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Ljava/lang/Void;", "onDone", "", "onMatrixError", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "onSuccess", "info", "onUnexpectedError", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
public final class VectorSettingsPreferencesFragment$onPushRuleClick$listener$1 implements ApiCallback<Void> {
    final /* synthetic */ boolean $isAllowed;
    final /* synthetic */ PushManager $pushManager;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$onPushRuleClick$listener$1(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, PushManager pushManager, boolean z) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$pushManager = pushManager;
        this.$isAllowed = z;
    }

    private final void onDone() {
        if (this.this$0.getActivity() != null) {
            this.this$0.getActivity().runOnUiThread(new VectorSettingsPreferencesFragment$onPushRuleClick$listener$1$onDone$1(this));
        }
    }

    public void onSuccess(Void voidR) {
        onDone();
    }

    public void onMatrixError(MatrixError matrixError) {
        this.$pushManager.setDeviceNotificationsAllowed(this.$isAllowed);
        onDone();
    }

    public void onNetworkError(Exception exc) {
        this.$pushManager.setDeviceNotificationsAllowed(this.$isAllowed);
        onDone();
    }

    public void onUnexpectedError(Exception exc) {
        this.$pushManager.setDeviceNotificationsAllowed(this.$isAllowed);
        onDone();
    }
}
