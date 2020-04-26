package fr.gouv.tchap.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J(\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\tH\u0016J(\u0010\f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0016¨\u0006\u000e"}, d2 = {"fr/gouv/tchap/activity/TchapUnsupportedAndroidVersionActivity$exportKeysAndSignOut$textWatcher$1", "Landroid/text/TextWatcher;", "afterTextChanged", "", "s", "Landroid/text/Editable;", "beforeTextChanged", "", "start", "", "count", "after", "onTextChanged", "before", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: TchapUnsupportedAndroidVersionActivity.kt */
public final class TchapUnsupportedAndroidVersionActivity$exportKeysAndSignOut$textWatcher$1 implements TextWatcher {
    final /* synthetic */ Button $exportButton;
    final /* synthetic */ TextInputEditText $passPhrase1EditText;
    final /* synthetic */ TextInputEditText $passPhrase2EditText;

    public void afterTextChanged(Editable editable) {
        Intrinsics.checkParameterIsNotNull(editable, "s");
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(charSequence, "s");
    }

    TchapUnsupportedAndroidVersionActivity$exportKeysAndSignOut$textWatcher$1(Button button, TextInputEditText textInputEditText, TextInputEditText textInputEditText2) {
        this.$exportButton = button;
        this.$passPhrase1EditText = textInputEditText;
        this.$passPhrase2EditText = textInputEditText2;
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        boolean z;
        Intrinsics.checkParameterIsNotNull(charSequence, "s");
        Button button = this.$exportButton;
        Intrinsics.checkExpressionValueIsNotNull(button, "exportButton");
        TextInputEditText textInputEditText = this.$passPhrase1EditText;
        String str = "passPhrase1EditText";
        Intrinsics.checkExpressionValueIsNotNull(textInputEditText, str);
        if (!TextUtils.isEmpty(textInputEditText.getText())) {
            TextInputEditText textInputEditText2 = this.$passPhrase1EditText;
            Intrinsics.checkExpressionValueIsNotNull(textInputEditText2, str);
            CharSequence text = textInputEditText2.getText();
            TextInputEditText textInputEditText3 = this.$passPhrase2EditText;
            Intrinsics.checkExpressionValueIsNotNull(textInputEditText3, "passPhrase2EditText");
            if (TextUtils.equals(text, textInputEditText3.getText())) {
                z = true;
                button.setEnabled(z);
            }
        }
        z = false;
        button.setEnabled(z);
    }
}
