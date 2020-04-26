package im.vector.fragments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/content/DialogInterface;", "kotlin.jvm.PlatformType", "onCancel"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1$dialog$3 implements OnCancelListener {
    final /* synthetic */ View $view;
    final /* synthetic */ VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1 this$0;

    VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1$dialog$3(VectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1 vectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1, View view) {
        this.this$0 = vectorSettingsPreferencesFragment$doShowPasswordChangeDialog$1;
        this.$view = view;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        if (this.this$0.this$0.getActivity() != null) {
            Object systemService = this.this$0.this$0.getActivity().getSystemService("input_method");
            if (systemService != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) systemService;
                View view = this.$view;
                Intrinsics.checkExpressionValueIsNotNull(view, "view");
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type android.view.inputmethod.InputMethodManager");
        }
    }
}
