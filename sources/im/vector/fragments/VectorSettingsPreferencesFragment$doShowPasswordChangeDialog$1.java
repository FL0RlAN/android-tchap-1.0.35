package im.vector.fragments;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog.Builder;
import fr.gouv.tchap.a.R;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1 implements Runnable {
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment) {
        this.this$0 = vectorSettingsPreferencesFragment;
    }

    public final void run() {
        Activity activity = this.this$0.getActivity();
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity");
        View inflate = activity.getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        final EditText editText = (EditText) inflate.findViewById(R.id.change_password_old_pwd_text);
        final EditText editText2 = (EditText) inflate.findViewById(R.id.change_password_new_pwd_text);
        final EditText editText3 = (EditText) inflate.findViewById(R.id.change_password_confirm_new_pwd_text);
        final Button button = new Builder(this.this$0.getActivity()).setTitle((int) R.string.settings_change_password).setMessage((int) R.string.tchap_change_password_help).setView(inflate).setPositiveButton((int) R.string.save, (OnClickListener) new VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1$dialog$1(this, inflate, editText, editText2)).setNegativeButton((int) R.string.cancel, (OnClickListener) new VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1$dialog$2(this, inflate)).setOnCancelListener(new VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1$dialog$3(this, inflate)).show().getButton(-1);
        Intrinsics.checkExpressionValueIsNotNull(button, "saveButton");
        button.setEnabled(false);
        editText3.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                Intrinsics.checkParameterIsNotNull(editable, "s");
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Intrinsics.checkParameterIsNotNull(charSequence, "s");
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Intrinsics.checkParameterIsNotNull(charSequence, "s");
                EditText editText = editText;
                Intrinsics.checkExpressionValueIsNotNull(editText, "oldPasswordText");
                String obj = editText.getText().toString();
                String str = "null cannot be cast to non-null type kotlin.CharSequence";
                if (obj != null) {
                    String obj2 = StringsKt.trim((CharSequence) obj).toString();
                    EditText editText2 = editText2;
                    Intrinsics.checkExpressionValueIsNotNull(editText2, "newPasswordText");
                    String obj3 = editText2.getText().toString();
                    if (obj3 != null) {
                        String obj4 = StringsKt.trim((CharSequence) obj3).toString();
                        EditText editText3 = editText3;
                        Intrinsics.checkExpressionValueIsNotNull(editText3, "confirmNewPasswordText");
                        String obj5 = editText3.getText().toString();
                        if (obj5 != null) {
                            String obj6 = StringsKt.trim((CharSequence) obj5).toString();
                            Button button = button;
                            Intrinsics.checkExpressionValueIsNotNull(button, "saveButton");
                            button.setEnabled(obj2.length() > 0 && obj4.length() > 0 && TextUtils.equals(obj4, obj6));
                            return;
                        }
                        throw new TypeCastException(str);
                    }
                    throw new TypeCastException(str);
                }
                throw new TypeCastException(str);
            }
        });
    }
}
