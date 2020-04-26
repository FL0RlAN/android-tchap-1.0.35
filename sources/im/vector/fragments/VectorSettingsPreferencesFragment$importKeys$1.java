package im.vector.fragments;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J(\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\tH\u0016J(\u0010\f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0016¨\u0006\u000e"}, d2 = {"im/vector/fragments/VectorSettingsPreferencesFragment$importKeys$1", "Landroid/text/TextWatcher;", "afterTextChanged", "", "s", "Landroid/text/Editable;", "beforeTextChanged", "", "start", "", "count", "after", "onTextChanged", "before", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
public final class VectorSettingsPreferencesFragment$importKeys$1 implements TextWatcher {
    final /* synthetic */ Button $importButton;
    final /* synthetic */ TextInputEditText $passPhraseEditText;

    public void afterTextChanged(Editable editable) {
        Intrinsics.checkParameterIsNotNull(editable, "s");
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(charSequence, "s");
    }

    VectorSettingsPreferencesFragment$importKeys$1(Button button, TextInputEditText textInputEditText) {
        this.$importButton = button;
        this.$passPhraseEditText = textInputEditText;
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(charSequence, "s");
        Button button = this.$importButton;
        Intrinsics.checkExpressionValueIsNotNull(button, "importButton");
        TextInputEditText textInputEditText = this.$passPhraseEditText;
        Intrinsics.checkExpressionValueIsNotNull(textInputEditText, "passPhraseEditText");
        button.setEnabled(!TextUtils.isEmpty(textInputEditText.getText()));
    }
}
