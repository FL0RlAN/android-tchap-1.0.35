package im.vector.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import fr.gouv.tchap.a.R;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\n¢\u0006\u0002\b\u0007"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/content/DialogInterface;", "kotlin.jvm.PlatformType", "<anonymous parameter 1>", "", "onClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1$dialog$1 implements OnClickListener {
    final /* synthetic */ EditText $newPasswordText;
    final /* synthetic */ EditText $oldPasswordText;
    final /* synthetic */ View $view;
    final /* synthetic */ VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1 this$0;

    VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1$dialog$1(VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1 vectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1, View view, EditText editText, EditText editText2) {
        this.this$0 = vectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1;
        this.$view = view;
        this.$oldPasswordText = editText;
        this.$newPasswordText = editText2;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        if (this.this$0.this$0.getActivity() != null) {
            Object systemService = this.this$0.this$0.getActivity().getSystemService("input_method");
            if (systemService != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) systemService;
                View view = this.$view;
                Intrinsics.checkExpressionValueIsNotNull(view, "view");
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            } else {
                throw new TypeCastException("null cannot be cast to non-null type android.view.inputmethod.InputMethodManager");
            }
        }
        EditText editText = this.$oldPasswordText;
        Intrinsics.checkExpressionValueIsNotNull(editText, "oldPasswordText");
        String obj = editText.getText().toString();
        String str = "null cannot be cast to non-null type kotlin.CharSequence";
        if (obj != null) {
            String obj2 = StringsKt.trim((CharSequence) obj).toString();
            EditText editText2 = this.$newPasswordText;
            Intrinsics.checkExpressionValueIsNotNull(editText2, "newPasswordText");
            String obj3 = editText2.getText().toString();
            if (obj3 != null) {
                String obj4 = StringsKt.trim((CharSequence) obj3).toString();
                if (obj4.length() < 8) {
                    Activity activity = this.this$0.this$0.getActivity();
                    if (activity != null) {
                        Toast makeText = Toast.makeText(activity, R.string.auth_invalid_password, 0);
                        makeText.show();
                        Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
                    }
                    return;
                }
                this.this$0.this$0.displayLoadingView();
                VectorSettingsPreferencesFragment.access$getMSession$p(this.this$0.this$0).updatePassword(obj2, obj4, new ApiCallback<Void>(this) {
                    final /* synthetic */ VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1$dialog$1 this$0;

                    {
                        this.this$0 = r1;
                    }

                    private final void onDone(int i) {
                        if (this.this$0.this$0.this$0.getActivity() != null) {
                            this.this$0.this$0.this$0.getActivity().runOnUiThread(new VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1$dialog$1$1$onDone$1(this, i));
                        }
                    }

                    public void onSuccess(Void voidR) {
                        onDone(R.string.settings_password_updated);
                    }

                    public void onNetworkError(Exception exc) {
                        Intrinsics.checkParameterIsNotNull(exc, "e");
                        onDone(R.string.settings_fail_to_update_password);
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        Intrinsics.checkParameterIsNotNull(matrixError, "e");
                        if (TextUtils.equals(MatrixError.PASSWORD_TOO_SHORT, matrixError.errcode) || TextUtils.equals(MatrixError.PASSWORD_NO_DIGIT, matrixError.errcode) || TextUtils.equals(MatrixError.PASSWORD_NO_UPPERCASE, matrixError.errcode) || TextUtils.equals(MatrixError.PASSWORD_NO_LOWERCASE, matrixError.errcode) || TextUtils.equals(MatrixError.PASSWORD_NO_SYMBOL, matrixError.errcode) || TextUtils.equals(MatrixError.WEAK_PASSWORD, matrixError.errcode)) {
                            onDone(R.string.tchap_password_weak_pwd_error);
                        } else if (TextUtils.equals(MatrixError.PASSWORD_IN_DICTIONARY, matrixError.errcode)) {
                            onDone(R.string.tchap_password_pwd_in_dict_error);
                        } else {
                            onDone(R.string.settings_fail_to_update_password);
                        }
                    }

                    public void onUnexpectedError(Exception exc) {
                        Intrinsics.checkParameterIsNotNull(exc, "e");
                        onDone(R.string.settings_fail_to_update_password);
                    }
                });
                return;
            }
            throw new TypeCastException(str);
        }
        throw new TypeCastException(str);
    }
}
