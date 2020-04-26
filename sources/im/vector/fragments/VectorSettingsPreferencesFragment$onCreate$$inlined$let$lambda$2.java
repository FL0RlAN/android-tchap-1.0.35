package im.vector.fragments;

import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import im.vector.Matrix;
import im.vector.push.PushManager;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u00010\u00060\u0006H\n¢\u0006\u0002\b\u0007¨\u0006\b"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/preference/Preference;", "kotlin.jvm.PlatformType", "aNewValue", "", "onPreferenceChange", "im/vector/fragments/VectorSettingsPreferencesFragment$onCreate$9$1"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$2 implements OnPreferenceChangeListener {
    final /* synthetic */ PushManager $pushManager$inlined;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$2(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, PushManager pushManager) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$pushManager$inlined = pushManager;
    }

    public final boolean onPreferenceChange(Preference preference, Object obj) {
        if (obj != null) {
            boolean booleanValue = ((Boolean) obj).booleanValue();
            PushManager pushManager = this.$pushManager$inlined;
            String str = "pushManager";
            Intrinsics.checkExpressionValueIsNotNull(pushManager, str);
            if (booleanValue != pushManager.isBackgroundSyncAllowed()) {
                PushManager pushManager2 = this.$pushManager$inlined;
                Intrinsics.checkExpressionValueIsNotNull(pushManager2, str);
                pushManager2.setBackgroundSyncAllowed(booleanValue);
            }
            this.this$0.displayLoadingView();
            Matrix instance = Matrix.getInstance(this.this$0.getActivity());
            if (instance == null) {
                Intrinsics.throwNpe();
            }
            instance.getPushManager().forceSessionsRegistration(new ApiCallback<Void>(this) {
                final /* synthetic */ VectorSettingsPreferencesFragment$onCreate$$inlined$let$lambda$2 this$0;

                {
                    this.this$0 = r1;
                }

                public void onSuccess(Void voidR) {
                    this.this$0.this$0.hideLoadingView();
                }

                public void onMatrixError(MatrixError matrixError) {
                    this.this$0.this$0.hideLoadingView();
                }

                public void onNetworkError(Exception exc) {
                    this.this$0.this$0.hideLoadingView();
                }

                public void onUnexpectedError(Exception exc) {
                    this.this$0.this$0.hideLoadingView();
                }
            });
            return true;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Boolean");
    }
}
