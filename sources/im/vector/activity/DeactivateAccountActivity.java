package im.vector.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.ui.themes.ActivityOtherThemes.NoActionBar;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.MXSession;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\u0018\u0000 \u001c2\u00020\u0001:\u0001\u001cB\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0012H\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\r\u0010\u0018\u001a\u00020\u0017H\u0001¢\u0006\u0002\b\u0019J\r\u0010\u001a\u001a\u00020\u0017H\u0001¢\u0006\u0002\b\u001bR\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X.¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lim/vector/activity/DeactivateAccountActivity;", "Lim/vector/activity/VectorAppCompatActivity;", "()V", "eraseCheckBox", "Landroid/widget/CheckBox;", "getEraseCheckBox", "()Landroid/widget/CheckBox;", "setEraseCheckBox", "(Landroid/widget/CheckBox;)V", "passwordEditText", "Landroid/widget/EditText;", "getPasswordEditText", "()Landroid/widget/EditText;", "setPasswordEditText", "(Landroid/widget/EditText;)V", "session", "Lorg/matrix/androidsdk/MXSession;", "getLayoutRes", "", "getOtherThemes", "Lim/vector/ui/themes/ActivityOtherThemes$NoActionBar;", "getTitleRes", "initUiAndData", "", "onCancel", "onCancel$vector_appAgentWithoutvoipWithpinningMatrixorg", "onSubmit", "onSubmit$vector_appAgentWithoutvoipWithpinningMatrixorg", "Companion", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: DeactivateAccountActivity.kt */
public final class DeactivateAccountActivity extends VectorAppCompatActivity {
    public static final Companion Companion = new Companion(null);
    private HashMap _$_findViewCache;
    @BindView(2131296421)
    public CheckBox eraseCheckBox;
    @BindView(2131296422)
    public EditText passwordEditText;
    private MXSession session;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006¨\u0006\u0007"}, d2 = {"Lim/vector/activity/DeactivateAccountActivity$Companion;", "", "()V", "getIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: DeactivateAccountActivity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Intent getIntent(Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            return new Intent(context, DeactivateAccountActivity.class);
        }
    }

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
        return R.layout.activity_deactivate_account;
    }

    public int getTitleRes() {
        return R.string.deactivate_account_title;
    }

    public final CheckBox getEraseCheckBox() {
        CheckBox checkBox = this.eraseCheckBox;
        if (checkBox == null) {
            Intrinsics.throwUninitializedPropertyAccessException("eraseCheckBox");
        }
        return checkBox;
    }

    public final void setEraseCheckBox(CheckBox checkBox) {
        Intrinsics.checkParameterIsNotNull(checkBox, "<set-?>");
        this.eraseCheckBox = checkBox;
    }

    public final EditText getPasswordEditText() {
        EditText editText = this.passwordEditText;
        if (editText == null) {
            Intrinsics.throwUninitializedPropertyAccessException("passwordEditText");
        }
        return editText;
    }

    public final void setPasswordEditText(EditText editText) {
        Intrinsics.checkParameterIsNotNull(editText, "<set-?>");
        this.passwordEditText = editText;
    }

    public NoActionBar getOtherThemes() {
        return NoActionBar.INSTANCE;
    }

    public void initUiAndData() {
        super.initUiAndData();
        configureToolbar();
        setWaitingView(findViewById(R.id.waiting_view));
        Matrix instance = Matrix.getInstance(this);
        Intrinsics.checkExpressionValueIsNotNull(instance, "Matrix.getInstance(this)");
        MXSession defaultSession = instance.getDefaultSession();
        Intrinsics.checkExpressionValueIsNotNull(defaultSession, "Matrix.getInstance(this).defaultSession");
        this.session = defaultSession;
    }

    @OnClick({2131296419})
    public final void onSubmit$vector_appAgentWithoutvoipWithpinningMatrixorg() {
        EditText editText = this.passwordEditText;
        String str = "passwordEditText";
        if (editText == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        String obj = editText.getText().toString();
        if (obj.length() == 0) {
            EditText editText2 = this.passwordEditText;
            if (editText2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            editText2.setError(getString(R.string.auth_missing_password));
            return;
        }
        showWaitingView();
        Context context = this;
        MXSession mXSession = this.session;
        if (mXSession == null) {
            Intrinsics.throwUninitializedPropertyAccessException("session");
        }
        CheckBox checkBox = this.eraseCheckBox;
        if (checkBox == null) {
            Intrinsics.throwUninitializedPropertyAccessException("eraseCheckBox");
        }
        CommonActivityUtils.deactivateAccount(context, mXSession, obj, checkBox.isChecked(), new DeactivateAccountActivity$onSubmit$1(this, this));
    }

    @OnClick({2131296418})
    public final void onCancel$vector_appAgentWithoutvoipWithpinningMatrixorg() {
        finish();
    }
}
