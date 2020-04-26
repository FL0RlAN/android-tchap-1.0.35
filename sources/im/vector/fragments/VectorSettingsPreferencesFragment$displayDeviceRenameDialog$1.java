package im.vector.fragments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.preference.Preference;
import android.text.TextUtils;
import android.widget.EditText;
import im.vector.preference.VectorCustomActionEditTextPreference;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.model.rest.DeviceInfo;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\n¢\u0006\u0002\b\u0007"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/content/DialogInterface;", "kotlin.jvm.PlatformType", "<anonymous parameter 1>", "", "onClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$displayDeviceRenameDialog$1 implements OnClickListener {
    final /* synthetic */ DeviceInfo $aDeviceInfoToRename;
    final /* synthetic */ EditText $input;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$displayDeviceRenameDialog$1(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, EditText editText, DeviceInfo deviceInfo) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$input = editText;
        this.$aDeviceInfoToRename = deviceInfo;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.this$0.displayLoadingView();
        EditText editText = this.$input;
        Intrinsics.checkExpressionValueIsNotNull(editText, "input");
        final String obj = editText.getText().toString();
        VectorSettingsPreferencesFragment.access$getMSession$p(this.this$0).setDeviceName(this.$aDeviceInfoToRename.device_id, obj, new ApiCallback<Void>(this) {
            final /* synthetic */ VectorSettingsPreferencesFragment$displayDeviceRenameDialog$1 this$0;

            {
                this.this$0 = r1;
            }

            public void onSuccess(Void voidR) {
                this.this$0.this$0.hideLoadingView();
                int preferenceCount = this.this$0.this$0.getMDevicesListSettingsCategory().getPreferenceCount();
                int i = 0;
                while (i < preferenceCount) {
                    Preference preference = this.this$0.this$0.getMDevicesListSettingsCategory().getPreference(i);
                    if (preference != null) {
                        VectorCustomActionEditTextPreference vectorCustomActionEditTextPreference = (VectorCustomActionEditTextPreference) preference;
                        if (TextUtils.equals(this.this$0.$aDeviceInfoToRename.device_id, vectorCustomActionEditTextPreference.getTitle())) {
                            vectorCustomActionEditTextPreference.setSummary(obj);
                        }
                        i++;
                    } else {
                        throw new TypeCastException("null cannot be cast to non-null type im.vector.preference.VectorCustomActionEditTextPreference");
                    }
                }
                if (TextUtils.equals(this.this$0.this$0.getCryptoInfoDeviceIdPreference().getSummary(), this.this$0.$aDeviceInfoToRename.device_id)) {
                    this.this$0.this$0.getCryptoInfoDeviceNamePreference().setSummary(obj);
                }
                this.this$0.$aDeviceInfoToRename.display_name = obj;
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
