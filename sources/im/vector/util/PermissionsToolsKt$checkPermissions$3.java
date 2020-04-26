package im.vector.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.TypeCastException;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\n¢\u0006\u0002\b\u0007"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/content/DialogInterface;", "kotlin.jvm.PlatformType", "<anonymous parameter 1>", "", "onClick"}, k = 3, mv = {1, 1, 13})
/* compiled from: PermissionsTools.kt */
final class PermissionsToolsKt$checkPermissions$3 implements OnClickListener {
    final /* synthetic */ Activity $activity;
    final /* synthetic */ Fragment $fragment;
    final /* synthetic */ ArrayList $permissionsListToBeGranted;
    final /* synthetic */ int $requestCode;

    PermissionsToolsKt$checkPermissions$3(ArrayList arrayList, Fragment fragment, int i, Activity activity) {
        this.$permissionsListToBeGranted = arrayList;
        this.$fragment = fragment;
        this.$requestCode = i;
        this.$activity = activity;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        if (!this.$permissionsListToBeGranted.isEmpty()) {
            Fragment fragment = this.$fragment;
            String str = "null cannot be cast to non-null type kotlin.Array<T>";
            String str2 = "null cannot be cast to non-null type java.util.Collection<T>";
            if (fragment != null) {
                Collection collection = this.$permissionsListToBeGranted;
                if (collection != null) {
                    Object[] array = collection.toArray(new String[0]);
                    if (array != null) {
                        fragment.requestPermissions((String[]) array, this.$requestCode);
                        return;
                    }
                    throw new TypeCastException(str);
                }
                throw new TypeCastException(str2);
            }
            Activity activity = this.$activity;
            Collection collection2 = this.$permissionsListToBeGranted;
            if (collection2 != null) {
                Object[] array2 = collection2.toArray(new String[0]);
                if (array2 != null) {
                    ActivityCompat.requestPermissions(activity, (String[]) array2, this.$requestCode);
                    return;
                }
                throw new TypeCastException(str);
            }
            throw new TypeCastException(str2);
        }
    }
}
