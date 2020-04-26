package fr.gouv.tchap.activity;

import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog.Builder;
import butterknife.BindView;
import butterknife.OnClick;
import com.google.android.material.textfield.TextInputEditText;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.VectorAppCompatActivity;
import im.vector.ui.themes.ActivityOtherThemes.NoActionBar;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.MXSession;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\r\u0010\u0014\u001a\u00020\u0015H\u0001¢\u0006\u0002\b\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u0015H\u0016J\r\u0010\u001c\u001a\u00020\u0015H\u0001¢\u0006\u0002\b\u001dR\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0011\u001a\u00020\u00048\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0006\"\u0004\b\u0013\u0010\b¨\u0006\u001e"}, d2 = {"Lfr/gouv/tchap/activity/TchapUnsupportedAndroidVersionActivity;", "Lim/vector/activity/VectorAppCompatActivity;", "()V", "exportButton", "Landroid/widget/Button;", "getExportButton", "()Landroid/widget/Button;", "setExportButton", "(Landroid/widget/Button;)V", "message", "Landroid/widget/TextView;", "getMessage", "()Landroid/widget/TextView;", "setMessage", "(Landroid/widget/TextView;)V", "session", "Lorg/matrix/androidsdk/MXSession;", "signOutButton", "getSignOutButton", "setSignOutButton", "exportKeysAndSignOut", "", "exportKeysAndSignOut$vector_appAgentWithoutvoipWithpinningMatrixorg", "getLayoutRes", "", "getOtherThemes", "Lim/vector/ui/themes/ActivityOtherThemes$NoActionBar;", "initUiAndData", "signOut", "signOut$vector_appAgentWithoutvoipWithpinningMatrixorg", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: TchapUnsupportedAndroidVersionActivity.kt */
public final class TchapUnsupportedAndroidVersionActivity extends VectorAppCompatActivity {
    private HashMap _$_findViewCache;
    @BindView(2131296485)
    public Button exportButton;
    @BindView(2131297132)
    public TextView message;
    /* access modifiers changed from: private */
    public MXSession session;
    @BindView(2131297029)
    public Button signOutButton;

    public void _$_clearFindViewByIdCache() {
        HashMap hashMap = this._$_findViewCache;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public int getLayoutRes() {
        return R.layout.activity_unsupported_android_version;
    }

    public final TextView getMessage() {
        TextView textView = this.message;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("message");
        }
        return textView;
    }

    public final void setMessage(TextView textView) {
        Intrinsics.checkParameterIsNotNull(textView, "<set-?>");
        this.message = textView;
    }

    public final Button getSignOutButton() {
        Button button = this.signOutButton;
        if (button == null) {
            Intrinsics.throwUninitializedPropertyAccessException("signOutButton");
        }
        return button;
    }

    public final void setSignOutButton(Button button) {
        Intrinsics.checkParameterIsNotNull(button, "<set-?>");
        this.signOutButton = button;
    }

    public final Button getExportButton() {
        Button button = this.exportButton;
        if (button == null) {
            Intrinsics.throwUninitializedPropertyAccessException("exportButton");
        }
        return button;
    }

    public final void setExportButton(Button button) {
        Intrinsics.checkParameterIsNotNull(button, "<set-?>");
        this.exportButton = button;
    }

    public NoActionBar getOtherThemes() {
        return NoActionBar.INSTANCE;
    }

    public void initUiAndData() {
        super.initUiAndData();
        setWaitingView(findViewById(R.id.waiting_view));
        Matrix instance = Matrix.getInstance(this);
        Intrinsics.checkExpressionValueIsNotNull(instance, "Matrix.getInstance(this)");
        this.session = instance.getDefaultSession();
        if (this.session == null) {
            TextView textView = this.message;
            if (textView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("message");
            }
            textView.setText(getString(R.string.tchap_unsupported_android_version_default_msg));
            Button button = this.signOutButton;
            if (button == null) {
                Intrinsics.throwUninitializedPropertyAccessException("signOutButton");
            }
            button.setVisibility(8);
            Button button2 = this.exportButton;
            if (button2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("exportButton");
            }
            button2.setVisibility(8);
        }
    }

    @OnClick({2131297029})
    public final void signOut$vector_appAgentWithoutvoipWithpinningMatrixorg() {
        showWaitingView();
        CommonActivityUtils.logout(this);
    }

    @OnClick({2131296485})
    public final void exportKeysAndSignOut$vector_appAgentWithoutvoipWithpinningMatrixorg() {
        View inflate = getLayoutInflater().inflate(R.layout.dialog_export_e2e_keys, null);
        Builder view = new Builder(this).setTitle((int) R.string.encryption_export_room_keys).setView(inflate);
        TextInputEditText textInputEditText = (TextInputEditText) inflate.findViewById(R.id.dialog_e2e_keys_passphrase_edit_text);
        TextInputEditText textInputEditText2 = (TextInputEditText) inflate.findViewById(R.id.dialog_e2e_keys_confirm_passphrase_edit_text);
        Button button = (Button) inflate.findViewById(R.id.dialog_e2e_keys_export_button);
        TextWatcher tchapUnsupportedAndroidVersionActivity$exportKeysAndSignOut$textWatcher$1 = new TchapUnsupportedAndroidVersionActivity$exportKeysAndSignOut$textWatcher$1(button, textInputEditText, textInputEditText2);
        textInputEditText.addTextChangedListener(tchapUnsupportedAndroidVersionActivity$exportKeysAndSignOut$textWatcher$1);
        textInputEditText2.addTextChangedListener(tchapUnsupportedAndroidVersionActivity$exportKeysAndSignOut$textWatcher$1);
        Intrinsics.checkExpressionValueIsNotNull(button, "exportButton");
        button.setEnabled(false);
        button.setOnClickListener(new TchapUnsupportedAndroidVersionActivity$exportKeysAndSignOut$1(this, textInputEditText, view.show()));
    }
}
