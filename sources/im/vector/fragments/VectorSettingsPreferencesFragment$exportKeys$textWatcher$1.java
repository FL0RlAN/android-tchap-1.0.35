package im.vector.fragments;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
import fr.gouv.tchap.a.R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J(\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\tH\u0016J(\u0010\f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0016¨\u0006\u000e"}, d2 = {"im/vector/fragments/VectorSettingsPreferencesFragment$exportKeys$textWatcher$1", "Landroid/text/TextWatcher;", "afterTextChanged", "", "s", "Landroid/text/Editable;", "beforeTextChanged", "", "start", "", "count", "after", "onTextChanged", "before", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
public final class VectorSettingsPreferencesFragment$exportKeys$textWatcher$1 implements TextWatcher {
    final /* synthetic */ Button $exportButton;
    final /* synthetic */ TextInputEditText $passPhrase1EditText;
    final /* synthetic */ TextInputEditText $passPhrase2EditText;
    final /* synthetic */ VectorSettingsPreferencesFragment this$0;

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(charSequence, "s");
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(charSequence, "s");
    }

    VectorSettingsPreferencesFragment$exportKeys$textWatcher$1(VectorSettingsPreferencesFragment vectorSettingsPreferencesFragment, TextInputEditText textInputEditText, Button button, TextInputEditText textInputEditText2) {
        this.this$0 = vectorSettingsPreferencesFragment;
        this.$passPhrase1EditText = textInputEditText;
        this.$exportButton = button;
        this.$passPhrase2EditText = textInputEditText2;
    }

    public void afterTextChanged(Editable editable) {
        Intrinsics.checkParameterIsNotNull(editable, "s");
        TextInputEditText textInputEditText = this.$passPhrase1EditText;
        String str = "passPhrase1EditText";
        Intrinsics.checkExpressionValueIsNotNull(textInputEditText, str);
        String str2 = "exportButton";
        String str3 = "passPhrase2EditText";
        if (TextUtils.isEmpty(textInputEditText.getText())) {
            Button button = this.$exportButton;
            Intrinsics.checkExpressionValueIsNotNull(button, str2);
            button.setEnabled(false);
            TextInputEditText textInputEditText2 = this.$passPhrase2EditText;
            Intrinsics.checkExpressionValueIsNotNull(textInputEditText2, str3);
            textInputEditText2.setError(null);
            return;
        }
        TextInputEditText textInputEditText3 = this.$passPhrase1EditText;
        Intrinsics.checkExpressionValueIsNotNull(textInputEditText3, str);
        CharSequence text = textInputEditText3.getText();
        TextInputEditText textInputEditText4 = this.$passPhrase2EditText;
        Intrinsics.checkExpressionValueIsNotNull(textInputEditText4, str3);
        if (TextUtils.equals(text, textInputEditText4.getText())) {
            Button button2 = this.$exportButton;
            Intrinsics.checkExpressionValueIsNotNull(button2, str2);
            button2.setEnabled(true);
            TextInputEditText textInputEditText5 = this.$passPhrase2EditText;
            Intrinsics.checkExpressionValueIsNotNull(textInputEditText5, str3);
            textInputEditText5.setError(null);
            return;
        }
        Button button3 = this.$exportButton;
        Intrinsics.checkExpressionValueIsNotNull(button3, str2);
        button3.setEnabled(false);
        TextInputEditText textInputEditText6 = this.$passPhrase2EditText;
        Intrinsics.checkExpressionValueIsNotNull(textInputEditText6, str3);
        textInputEditText6.setError(this.this$0.getString(R.string.encryption_export_passphrase_dont_match));
    }
}
