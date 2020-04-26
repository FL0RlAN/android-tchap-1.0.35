package im.vector.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import fr.gouv.tchap.a.R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.crypto.model.rest.DeviceInfo;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\n¢\u0006\u0002\b\u0007"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/content/DialogInterface;", "kotlin.jvm.PlatformType", "<anonymous parameter 1>", "", "onClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$displayDeviceDeletionDialog$1 implements OnClickListener {
    final /* synthetic */ DeviceInfo $aDeviceInfoToDelete;
    final /* synthetic */ EditText $passwordEditText;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$displayDeviceDeletionDialog$1(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, EditText editText, DeviceInfo deviceInfo) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$passwordEditText = editText;
        this.$aDeviceInfoToDelete = deviceInfo;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        if (TextUtils.isEmpty(this.$passwordEditText.toString())) {
            Activity activity = this.this$0.getActivity();
            Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
            Context applicationContext = activity.getApplicationContext();
            Intrinsics.checkExpressionValueIsNotNull(applicationContext, "activity.applicationContext");
            Toast makeText = Toast.makeText(applicationContext, R.string.error_empty_field_your_password, 0);
            makeText.show();
            Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
            return;
        }
        VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment = this.this$0;
        EditText editText = this.$passwordEditText;
        Intrinsics.checkExpressionValueIsNotNull(editText, "passwordEditText");
        vectorSettingsPreferencesFragment.mAccountPassword = editText.getText().toString();
        VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment2 = this.this$0;
        String str = this.$aDeviceInfoToDelete.device_id;
        Intrinsics.checkExpressionValueIsNotNull(str, "aDeviceInfoToDelete.device_id");
        vectorSettingsPreferencesFragment2.deleteDevice(str);
    }
}
