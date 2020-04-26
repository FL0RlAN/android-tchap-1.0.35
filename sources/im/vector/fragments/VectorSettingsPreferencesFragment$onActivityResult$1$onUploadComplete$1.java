package im.vector.fragments;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$onActivityResult$1$onUploadComplete$1 implements Runnable {
    final /* synthetic */ String $contentUri;
    final /* synthetic */ VectorSettingsPreferencesFragment$onActivityResult$1 this$0;

    VectorSettingsPreferencesFragment$onActivityResult$1$onUploadComplete$1(VectorSettingsPreferencesFragment$onActivityResult$1 vectorSettingsPreferencesFragment$onActivityResult$1, String str) {
        this.this$0 = vectorSettingsPreferencesFragment$onActivityResult$1;
        this.$contentUri = str;
    }

    public final void run() {
        VectorSettingsPreferencesFragment.access$getMSession$p(this.this$0.this$0).getMyUser().updateAvatarUrl(this.$contentUri, new ApiCallback<Void>(this) {
            final /* synthetic */ VectorSettingsPreferencesFragment$onActivityResult$1$onUploadComplete$1 this$0;

            {
                this.this$0 = r1;
            }

            public void onSuccess(Void voidR) {
                this.this$0.this$0.this$0.onCommonDone(null);
                this.this$0.this$0.this$0.refreshDisplay();
            }

            public void onNetworkError(Exception exc) {
                Intrinsics.checkParameterIsNotNull(exc, "e");
                this.this$0.this$0.this$0.onCommonDone(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                Intrinsics.checkParameterIsNotNull(matrixError, "e");
                if (!Intrinsics.areEqual((Object) MatrixError.M_CONSENT_NOT_GIVEN, (Object) matrixError.errcode)) {
                    this.this$0.this$0.this$0.onCommonDone(matrixError.getLocalizedMessage());
                } else if (this.this$0.this$0.this$0.getActivity() != null) {
                    this.this$0.this$0.this$0.getActivity().runOnUiThread(new VectorSettingsPreferencesFragment$onActivityResult$1$onUploadComplete$1$1$onMatrixError$1(this, matrixError));
                }
            }

            public void onUnexpectedError(Exception exc) {
                Intrinsics.checkParameterIsNotNull(exc, "e");
                this.this$0.this$0.this$0.onCommonDone(exc.getLocalizedMessage());
            }
        });
    }
}
