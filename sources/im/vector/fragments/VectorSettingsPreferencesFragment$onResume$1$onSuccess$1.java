package im.vector.fragments;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$onResume$1$onSuccess$1 implements Runnable {
    final /* synthetic */ VectorSettingsPreferencesFragment$onResume$1 this$0;

    VectorSettingsPreferencesFragment$onResume$1$onSuccess$1(VectorSettingsPreferencesFragment$onResume$1 vectorSettingsPreferencesFragment$onResume$1) {
        this.this$0 = vectorSettingsPreferencesFragment$onResume$1;
    }

    public final void run() {
        this.this$0.this$0.refreshEmailsList();
        this.this$0.this$0.refreshPhoneNumbersList();
    }
}
