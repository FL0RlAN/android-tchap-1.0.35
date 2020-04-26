package im.vector.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.widget.Toast;
import fr.gouv.tchap.a.R;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/content/DialogInterface;", "kotlin.jvm.PlatformType", "onCancel"}, k = 3, mv = {1, 1, 13})
/* compiled from: PermissionsTools.kt */
final class PermissionsToolsKt$checkPermissions$2 implements OnCancelListener {
    final /* synthetic */ Activity $activity;

    PermissionsToolsKt$checkPermissions$2(Activity activity) {
        this.$activity = activity;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        Toast.makeText(this.$activity, R.string.missing_permissions_warning, 0).show();
    }
}
