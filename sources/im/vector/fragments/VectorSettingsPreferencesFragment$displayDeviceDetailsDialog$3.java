package im.vector.fragments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import androidx.core.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u000e\u0010\u0007\u001a\n \u0004*\u0004\u0018\u00010\b0\bH\n¢\u0006\u0002\b\t"}, d2 = {"<anonymous>", "", "dialog", "Landroid/content/DialogInterface;", "kotlin.jvm.PlatformType", "keyCode", "", "event", "Landroid/view/KeyEvent;", "onKey"}, k = 3, mv = {1, 1, 13})
/* compiled from: VectorSettingsPreferencesFragment.kt */
final class VectorSettingsPreferencesFragment$displayDeviceDetailsDialog$3 implements OnKeyListener {
    public static final VectorSettingsPreferencesFragment$displayDeviceDetailsDialog$3 INSTANCE = new VectorSettingsPreferencesFragment$displayDeviceDetailsDialog$3();

    VectorSettingsPreferencesFragment$displayDeviceDetailsDialog$3() {
    }

    public final boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        Intrinsics.checkExpressionValueIsNotNull(keyEvent, NotificationCompat.CATEGORY_EVENT);
        if (keyEvent.getAction() != 1 || i != 4) {
            return false;
        }
        dialogInterface.cancel();
        return true;
    }
}
