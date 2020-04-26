package im.vector.fragments;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import fr.gouv.tchap.a.R;
import im.vector.fragments.VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1$dialog$1.AnonymousClass1;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1$dialog$1$1$onDone$1 implements Runnable {
    final /* synthetic */ int $textId;
    final /* synthetic */ AnonymousClass1 this$0;

    VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1$dialog$1$1$onDone$1(AnonymousClass1 r1, int i) {
        this.this$0 = r1;
        this.$textId = i;
    }

    public final void run() {
        this.this$0.this$0.this$0.this$0.hideLoadingView();
        int i = this.$textId;
        Integer num = null;
        if (i == R.string.settings_password_updated) {
            i = R.string.settings_change_pwd_success_title;
            num = Integer.valueOf(R.string.settings_change_pwd_success_msg);
        } else if (i == R.string.tchap_password_weak_pwd_error || i == R.string.tchap_password_pwd_in_dict_error) {
            i = R.string.settings_fail_to_update_password;
            num = Integer.valueOf(this.$textId);
        }
        if (num != null) {
            num.intValue();
            if (new Builder(this.this$0.this$0.this$0.this$0.getActivity()).setTitle(i).setMessage(num.intValue()).setPositiveButton((int) R.string.ok, (OnClickListener) null).show() != null) {
                return;
            }
        }
        Activity activity = this.this$0.this$0.this$0.this$0.getActivity();
        if (activity != null) {
            Toast makeText = Toast.makeText(activity, this.$textId, 1);
            makeText.show();
            Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        }
    }
}
