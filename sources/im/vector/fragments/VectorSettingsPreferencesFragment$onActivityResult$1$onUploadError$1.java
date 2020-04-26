package im.vector.fragments;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$onActivityResult$1$onUploadError$1 implements Runnable {
    final /* synthetic */ String $serverErrorMessage;
    final /* synthetic */ int $serverResponseCode;
    final /* synthetic */ VectorSettingsPreferencesFragment$onActivityResult$1 this$0;

    VectorSettingsPreferencesFragment$onActivityResult$1$onUploadError$1(VectorSettingsPreferencesFragment$onActivityResult$1 vectorSettingsPreferencesFragment$onActivityResult$1, int i, String str) {
        this.this$0 = vectorSettingsPreferencesFragment$onActivityResult$1;
        this.$serverResponseCode = i;
        this.$serverErrorMessage = str;
    }

    public final void run() {
        VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment = this.this$0.this$0;
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(this.$serverResponseCode));
        sb.append(" : ");
        sb.append(this.$serverErrorMessage);
        vectorSettingsPreferencesFragment.onCommonDone(sb.toString());
    }
}
