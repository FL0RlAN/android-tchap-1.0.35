package im.vector.fragments;

import android.app.Activity;
import im.vector.activity.VectorAppCompatActivity;
import im.vector.fragments.VectorSettingsPreferencesFragment$onActivityResult$1$onUploadComplete$1.AnonymousClass1;
import kotlin.Metadata;
import kotlin.TypeCastException;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$onActivityResult$1$onUploadComplete$1$1$onMatrixError$1 implements Runnable {
    final /* synthetic */ MatrixError $e;
    final /* synthetic */ AnonymousClass1 this$0;

    VectorSettingsPreferencesFragment$onActivityResult$1$onUploadComplete$1$1$onMatrixError$1(AnonymousClass1 r1, MatrixError matrixError) {
        this.this$0 = r1;
        this.$e = matrixError;
    }

    public final void run() {
        this.this$0.this$0.this$0.this$0.hideLoadingView();
        Activity activity = this.this$0.this$0.this$0.this$0.getActivity();
        if (activity != null) {
            ((VectorAppCompatActivity) activity).getConsentNotGivenHelper().displayDialog(this.$e);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type im.vector.activity.VectorAppCompatActivity");
    }
}
