package im.vector.dialogs;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import im.vector.activity.interfaces.Restorable;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0007\u001a\u0004\u0018\u00010\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nJ\b\u0010\f\u001a\u00020\rH\u0002J\u0010\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0003H\u0016J\b\u0010\u0010\u001a\u00020\rH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lim/vector/dialogs/DialogLocker;", "Lim/vector/activity/interfaces/Restorable;", "savedInstanceState", "Landroid/os/Bundle;", "(Landroid/os/Bundle;)V", "isDialogDisplayed", "", "displayDialog", "Landroidx/appcompat/app/AlertDialog;", "builder", "Lkotlin/Function0;", "Landroidx/appcompat/app/AlertDialog$Builder;", "lock", "", "saveState", "outState", "unlock", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: DialogLocker.kt */
public final class DialogLocker implements Restorable {
    private boolean isDialogDisplayed;

    public DialogLocker(Bundle bundle) {
        boolean z = true;
        if (bundle == null || !bundle.getBoolean("DialogLocker.KEY_DIALOG_IS_DISPLAYED", false)) {
            z = false;
        }
        this.isDialogDisplayed = z;
    }

    /* access modifiers changed from: private */
    public final void unlock() {
        this.isDialogDisplayed = false;
    }

    /* access modifiers changed from: private */
    public final void lock() {
        this.isDialogDisplayed = true;
    }

    public final AlertDialog displayDialog(Function0<? extends Builder> function0) {
        Intrinsics.checkParameterIsNotNull(function0, "builder");
        if (this.isDialogDisplayed) {
            Log.w("DialogLocker", "Filtered dialog request");
            return null;
        }
        AlertDialog create = ((Builder) function0.invoke()).create();
        create.setOnShowListener(new DialogLocker$displayDialog$$inlined$apply$lambda$1(this));
        create.setOnCancelListener(new DialogLocker$displayDialog$$inlined$apply$lambda$2(this));
        create.setOnDismissListener(new DialogLocker$displayDialog$$inlined$apply$lambda$3(this));
        create.show();
        return create;
    }

    public void saveState(Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(bundle, "outState");
        bundle.putBoolean("DialogLocker.KEY_DIALOG_IS_DISPLAYED", this.isDialogDisplayed);
    }
}
