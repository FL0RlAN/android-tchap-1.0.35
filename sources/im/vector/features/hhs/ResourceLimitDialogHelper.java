package im.vector.features.hhs;

import android.app.Activity;
import android.os.Bundle;
import im.vector.activity.interfaces.Restorable;
import im.vector.dialogs.DialogLocker;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.model.MatrixError;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0019\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0011\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0005H\u0001R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lim/vector/features/hhs/ResourceLimitDialogHelper;", "Lim/vector/activity/interfaces/Restorable;", "activity", "Landroid/app/Activity;", "savedInstanceState", "Landroid/os/Bundle;", "(Landroid/app/Activity;Landroid/os/Bundle;)V", "dialogLocker", "Lim/vector/dialogs/DialogLocker;", "(Landroid/app/Activity;Lim/vector/dialogs/DialogLocker;)V", "formatter", "Lim/vector/features/hhs/ResourceLimitErrorFormatter;", "displayDialog", "", "matrixError", "Lorg/matrix/androidsdk/core/model/MatrixError;", "saveState", "outState", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: ResourceLimitDialogHelper.kt */
public final class ResourceLimitDialogHelper implements Restorable {
    /* access modifiers changed from: private */
    public final Activity activity;
    private final DialogLocker dialogLocker;
    /* access modifiers changed from: private */
    public final ResourceLimitErrorFormatter formatter;

    public void saveState(Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(bundle, "outState");
        this.dialogLocker.saveState(bundle);
    }

    private ResourceLimitDialogHelper(Activity activity2, DialogLocker dialogLocker2) {
        this.activity = activity2;
        this.dialogLocker = dialogLocker2;
        this.formatter = new ResourceLimitErrorFormatter(this.activity);
    }

    public ResourceLimitDialogHelper(Activity activity2, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(activity2, "activity");
        this(activity2, new DialogLocker(bundle));
    }

    public final void displayDialog(MatrixError matrixError) {
        Intrinsics.checkParameterIsNotNull(matrixError, "matrixError");
        this.dialogLocker.displayDialog(new ResourceLimitDialogHelper$displayDialog$1(this, matrixError));
    }
}
