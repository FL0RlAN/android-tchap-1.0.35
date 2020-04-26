package im.vector.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.PowerManager;
import android.widget.Toast;
import fr.gouv.tchap.a.R;
import im.vector.settings.VectorLocale;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\u001a\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007\u001a\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u0005\u001a\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u0005\u001a\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0007\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"LOG_TAG", "", "copyToClipboard", "", "context", "Landroid/content/Context;", "text", "", "getDeviceLocale", "Ljava/util/Locale;", "isIgnoringBatteryOptimizations", "", "requestDisablingBatteryOptimization", "activity", "Landroid/app/Activity;", "requestCode", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 2, mv = {1, 1, 13})
/* compiled from: SystemUtils.kt */
public final class SystemUtilsKt {
    private static final String LOG_TAG = "SystemUtils";

    public static final boolean isIgnoringBatteryOptimizations(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        if (VERSION.SDK_INT < 23) {
            return true;
        }
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        return powerManager != null && powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
    }

    public static final void requestDisablingBatteryOptimization(Activity activity, int i) {
        Intrinsics.checkParameterIsNotNull(activity, "activity");
        Intent intent = new Intent();
        intent.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
        StringBuilder sb = new StringBuilder();
        sb.append("package:");
        sb.append(activity.getPackageName());
        intent.setData(Uri.parse(sb.toString()));
        activity.startActivityForResult(intent, i);
    }

    public static final void copyToClipboard(Context context, CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(charSequence, "text");
        Object systemService = context.getSystemService("clipboard");
        if (systemService != null) {
            ((ClipboardManager) systemService).setPrimaryClip(ClipData.newPlainText("", charSequence));
            Toast makeText = Toast.makeText(context, R.string.copied_to_clipboard, 0);
            makeText.show();
            Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.content.ClipboardManager");
    }

    public static final Locale getDeviceLocale(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        try {
            Resources resourcesForApplication = context.getPackageManager().getResourcesForApplication("android");
            Intrinsics.checkExpressionValueIsNotNull(resourcesForApplication, "resources");
            Locale locale = resourcesForApplication.getConfiguration().locale;
            Intrinsics.checkExpressionValueIsNotNull(locale, "resources.configuration.locale");
            return locale;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## getDeviceLocale() failed ");
            sb.append(e.getMessage());
            Log.e(LOG_TAG, sb.toString(), e);
            return VectorLocale.INSTANCE.getApplicationLocale();
        }
    }
}
