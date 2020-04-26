package im.vector.fragments;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import im.vector.VectorApp;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$onCommonDone$1 implements Runnable {
    final /* synthetic */ String $errorMessage;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$onCommonDone$1(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, String str) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$errorMessage = str;
    }

    public final void run() {
        if (!TextUtils.isEmpty(this.$errorMessage)) {
            VectorApp instance = VectorApp.getInstance();
            Intrinsics.checkExpressionValueIsNotNull(instance, "VectorApp.getInstance()");
            Context context = instance;
            String str = this.$errorMessage;
            if (str == null) {
                Intrinsics.throwNpe();
            }
            Toast makeText = Toast.makeText(context, str, 0);
            makeText.show();
            Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        }
        this.this$0.hideLoadingView();
    }
}
