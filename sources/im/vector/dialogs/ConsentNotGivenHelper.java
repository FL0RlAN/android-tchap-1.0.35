package im.vector.dialogs;

import android.app.Activity;
import android.os.Bundle;
import fr.gouv.tchap.a.R;
import im.vector.activity.VectorWebViewActivity;
import im.vector.activity.interfaces.Restorable;
import im.vector.webview.WebViewMode;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0019\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ\u0010\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\u0011\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u0005H\u0001R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lim/vector/dialogs/ConsentNotGivenHelper;", "Lim/vector/activity/interfaces/Restorable;", "activity", "Landroid/app/Activity;", "savedInstanceState", "Landroid/os/Bundle;", "(Landroid/app/Activity;Landroid/os/Bundle;)V", "dialogLocker", "Lim/vector/dialogs/DialogLocker;", "(Landroid/app/Activity;Lim/vector/dialogs/DialogLocker;)V", "displayDialog", "", "matrixError", "Lorg/matrix/androidsdk/core/model/MatrixError;", "openWebViewActivity", "consentUri", "", "saveState", "outState", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: ConsentNotGivenHelper.kt */
public final class ConsentNotGivenHelper implements Restorable {
    /* access modifiers changed from: private */
    public final Activity activity;
    private final DialogLocker dialogLocker;

    public void saveState(Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(bundle, "outState");
        this.dialogLocker.saveState(bundle);
    }

    private ConsentNotGivenHelper(Activity activity2, DialogLocker dialogLocker2) {
        this.activity = activity2;
        this.dialogLocker = dialogLocker2;
    }

    public ConsentNotGivenHelper(Activity activity2, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(activity2, "activity");
        this(activity2, new DialogLocker(bundle));
    }

    public final void displayDialog(MatrixError matrixError) {
        Intrinsics.checkParameterIsNotNull(matrixError, "matrixError");
        if (matrixError.consentUri == null) {
            Log.e("ConsentNotGivenHelper", "Missing required parameter 'consent_uri'");
        } else {
            this.dialogLocker.displayDialog(new ConsentNotGivenHelper$displayDialog$1(this, matrixError));
        }
    }

    /* access modifiers changed from: private */
    public final void openWebViewActivity(String str) {
        this.activity.startActivity(VectorWebViewActivity.Companion.getIntent(this.activity, str, R.string.settings_app_term_conditions, WebViewMode.CONSENT));
    }
}
