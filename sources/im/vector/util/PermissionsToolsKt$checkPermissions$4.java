package im.vector.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import im.vector.contacts.ContactsManager;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\n¢\u0006\u0002\b\u0007"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/content/DialogInterface;", "kotlin.jvm.PlatformType", "<anonymous parameter 1>", "", "onClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: PermissionsTools.kt */
final class PermissionsToolsKt$checkPermissions$4 implements OnClickListener {
    final /* synthetic */ Activity $activity;
    final /* synthetic */ Fragment $fragment;
    final /* synthetic */ String[] $permissionsArrayToBeGranted;
    final /* synthetic */ int $requestCode;

    PermissionsToolsKt$checkPermissions$4(Fragment fragment, String[] strArr, int i, Activity activity) {
        this.$fragment = fragment;
        this.$permissionsArrayToBeGranted = strArr;
        this.$requestCode = i;
        this.$activity = activity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        ContactsManager.getInstance().setIsContactBookAccessAllowed(true);
        Fragment fragment = this.$fragment;
        if (fragment != null) {
            fragment.requestPermissions(this.$permissionsArrayToBeGranted, this.$requestCode);
        } else {
            ActivityCompat.requestPermissions(this.$activity, this.$permissionsArrayToBeGranted, this.$requestCode);
        }
    }
}
