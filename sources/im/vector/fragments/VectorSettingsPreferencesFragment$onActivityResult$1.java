package im.vector.fragments;

import kotlin.Metadata;
import org.matrix.androidsdk.listeners.MXMediaUploadListener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u0016J$\u0010\u0007\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u000b"}, d2 = {"im/vector/fragments/VectorSettingsPreferencesFragment$onActivityResult$1", "Lorg/matrix/androidsdk/listeners/MXMediaUploadListener;", "onUploadComplete", "", "uploadId", "", "contentUri", "onUploadError", "serverResponseCode", "", "serverErrorMessage", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
public final class VectorSettingsPreferencesFragment$onActivityResult$1 extends MXMediaUploadListener {
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$onActivityResult$1(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment) {
        this.this$0 = vectorSettingsPreferencesFragment;
    }

    public void onUploadError(String str, int i, String str2) {
        this.this$0.getActivity().runOnUiThread(new VectorSettingsPreferencesFragment$onActivityResult$1$onUploadError$1(this, i, str2));
    }

    public void onUploadComplete(String str, String str2) {
        this.this$0.getActivity().runOnUiThread(new VectorSettingsPreferencesFragment$onActivityResult$1$onUploadComplete$1(this, str2));
    }
}
