package im.vector.fragments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.model.pid.ThirdPartyIdentifier;
import org.matrix.androidsdk.rest.model.pid.ThreePid;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\n¢\u0006\u0002\b\u0007"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/content/DialogInterface;", "kotlin.jvm.PlatformType", "<anonymous parameter 1>", "", "onClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$displayDelete3PIDConfirmationDialog$1 implements OnClickListener {
    final /* synthetic */ ThirdPartyIdentifier $pid;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$displayDelete3PIDConfirmationDialog$1(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, ThirdPartyIdentifier thirdPartyIdentifier) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$pid = thirdPartyIdentifier;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.this$0.displayLoadingView();
        VectorSettingsPreferencesFragment.access$getMSession$p(this.this$0).getMyUser().delete3Pid(this.$pid, new ApiCallback<Void>(this) {
            final /* synthetic */ VectorSettingsPreferencesFragment$displayDelete3PIDConfirmationDialog$1 this$0;

            {
                this.this$0 = r1;
            }

            public void onSuccess(Void voidR) {
                String str = this.this$0.$pid.medium;
                if (str != null) {
                    int hashCode = str.hashCode();
                    if (hashCode != -1064943142) {
                        if (hashCode == 96619420 && str.equals("email")) {
                            this.this$0.this$0.refreshEmailsList();
                        }
                    } else if (str.equals(ThreePid.MEDIUM_MSISDN)) {
                        this.this$0.this$0.refreshPhoneNumbersList();
                    }
                }
                this.this$0.this$0.onCommonDone(null);
            }

            public void onNetworkError(Exception exc) {
                Intrinsics.checkParameterIsNotNull(exc, "e");
                this.this$0.this$0.onCommonDone(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                Intrinsics.checkParameterIsNotNull(matrixError, "e");
                this.this$0.this$0.onCommonDone(matrixError.getLocalizedMessage());
            }

            public void onUnexpectedError(Exception exc) {
                Intrinsics.checkParameterIsNotNull(exc, "e");
                this.this$0.this$0.onCommonDone(exc.getLocalizedMessage());
            }
        });
    }
}
