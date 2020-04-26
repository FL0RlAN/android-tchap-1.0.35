package im.vector.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.MediaStore.Images.Media;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import fr.gouv.tchap.a.R;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.ResourceUtils;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a \u0010\u0002\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0007\u001a(\u0010\b\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u00042\b\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\u0007\u001a\u001e\u0010\u000e\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0001\u001a\u0016\u0010\u0011\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u0001\u001a\u0016\u0010\u0013\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007\u001a\u0016\u0010\u0014\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u0001\u001a\u0018\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0017\u001a\u00020\u00182\b\u0010\u0015\u001a\u0004\u0018\u00010\u0019\u001a\u0018\u0010\u0016\u001a\u00020\t2\u0006\u0010\u0017\u001a\u00020\u00182\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001\u001a\u0016\u0010\u001b\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007\u001a.\u0010\u001c\u001a\u00020\t2\u0006\u0010\u001d\u001a\u00020\u00012\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010\u001f\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0003\u001a\u00020\u0004\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006 "}, d2 = {"LOG_TAG", "", "openCamera", "activity", "Landroid/app/Activity;", "titlePrefix", "requestCode", "", "openFileSelection", "", "fragment", "Landroid/app/Fragment;", "allowMultipleSelection", "", "openMedia", "savedMediaPath", "mimeType", "openPlayStore", "appId", "openSoundRecorder", "openUri", "uri", "openUrlInExternalBrowser", "context", "Landroid/content/Context;", "Landroid/net/Uri;", "url", "openVideoRecorder", "sendMailTo", "address", "subject", "message", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 2, mv = {1, 1, 13})
/* compiled from: ExternalApplicationsUtil.kt */
public final class ExternalApplicationsUtilKt {
    private static final String LOG_TAG = "ExternalApplicationsUtil";

    public static final void openUrlInExternalBrowser(Context context, String str) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        if (str != null) {
            openUrlInExternalBrowser(context, Uri.parse(str));
        }
    }

    public static final void openUrlInExternalBrowser(Context context, Uri uri) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        if (uri != null) {
            Intent intent = new Intent("android.intent.action.VIEW", uri);
            intent.putExtra("com.android.browser.application_id", context.getPackageName());
            try {
                context.startActivity(intent);
                Unit unit = Unit.INSTANCE;
            } catch (ActivityNotFoundException unused) {
                Toast makeText = Toast.makeText(context, R.string.error_no_external_application_found, 0);
                makeText.show();
                Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
            }
        }
    }

    public static final void openSoundRecorder(Activity activity, int i) {
        Intrinsics.checkParameterIsNotNull(activity, "activity");
        try {
            activity.startActivityForResult(Intent.createChooser(new Intent("android.provider.MediaStore.RECORD_SOUND"), activity.getString(R.string.go_on_with)), i);
        } catch (ActivityNotFoundException unused) {
            Toast makeText = Toast.makeText(activity, R.string.error_no_external_application_found, 0);
            makeText.show();
            Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        }
    }

    public static final void openFileSelection(Activity activity, Fragment fragment, boolean z, int i) {
        Intrinsics.checkParameterIsNotNull(activity, "activity");
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        if (VERSION.SDK_INT >= 18) {
            intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", z);
        }
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType(ResourceUtils.MIME_TYPE_ALL_CONTENT);
        if (fragment != null) {
            try {
                fragment.startActivityForResult(intent, i);
            } catch (ActivityNotFoundException unused) {
                Toast makeText = Toast.makeText(activity, R.string.error_no_external_application_found, 0);
                makeText.show();
                Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
            }
        } else {
            activity.startActivityForResult(intent, i);
        }
    }

    public static final void openVideoRecorder(Activity activity, int i) {
        Intrinsics.checkParameterIsNotNull(activity, "activity");
        Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
        intent.putExtra("android.intent.extra.videoQuality", 0);
        try {
            activity.startActivityForResult(intent, i);
        } catch (ActivityNotFoundException unused) {
            Toast makeText = Toast.makeText(activity, R.string.error_no_external_application_found, 0);
            makeText.show();
            Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        }
    }

    public static final String openCamera(Activity activity, String str, int i) {
        String str2;
        String str3 = LOG_TAG;
        Intrinsics.checkParameterIsNotNull(activity, "activity");
        Intrinsics.checkParameterIsNotNull(str, "titlePrefix");
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        ContentValues contentValues = new ContentValues();
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(simpleDateFormat.format(date));
        contentValues.put("title", sb.toString());
        contentValues.put("mime_type", ResourceUtils.MIME_TYPE_JPEG);
        Uri uri = null;
        try {
            uri = activity.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
            if (uri == null) {
                Log.e(str3, "Cannot use the external storage media to save image");
            }
        } catch (UnsupportedOperationException e) {
            Log.e(str3, "Unable to insert camera URI into MediaStore.Images.Media.EXTERNAL_CONTENT_URI - no SD card? Attempting to insert into device storage.", e);
        } catch (Exception e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to insert camera URI into MediaStore.Images.Media.EXTERNAL_CONTENT_URI. ");
            sb2.append(e2);
            Log.e(str3, sb2.toString(), e2);
        }
        if (uri == null) {
            try {
                uri = activity.getContentResolver().insert(Media.INTERNAL_CONTENT_URI, contentValues);
                if (uri == null) {
                    Log.e(str3, "Cannot use the internal storage to save media to save image");
                }
            } catch (Exception e3) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Unable to insert camera URI into internal storage. Giving up. ");
                sb3.append(e3);
                Log.e(str3, sb3.toString(), e3);
            }
        }
        if (uri != null) {
            intent.putExtra("output", uri);
            StringBuilder sb4 = new StringBuilder();
            sb4.append("trying to take a photo on ");
            sb4.append(uri.toString());
            Log.d(str3, sb4.toString());
        } else {
            Log.d(str3, "trying to take a photo with no predefined uri");
        }
        if (uri == null) {
            str2 = null;
        } else {
            str2 = uri.toString();
        }
        try {
            activity.startActivityForResult(intent, i);
            return str2;
        } catch (ActivityNotFoundException unused) {
            Toast makeText = Toast.makeText(activity, R.string.error_no_external_application_found, 0);
            makeText.show();
            Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
            return null;
        }
    }

    public static /* synthetic */ void sendMailTo$default(String str, String str2, String str3, Activity activity, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = null;
        }
        if ((i & 4) != 0) {
            str3 = null;
        }
        sendMailTo(str, str2, str3, activity);
    }

    public static final void sendMailTo(String str, String str2, String str3, Activity activity) {
        Intrinsics.checkParameterIsNotNull(str, PasswordLoginParams.IDENTIFIER_KEY_ADDRESS);
        Intrinsics.checkParameterIsNotNull(activity, "activity");
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", str, null));
        intent.putExtra("android.intent.extra.SUBJECT", str2);
        intent.putExtra("android.intent.extra.TEXT", str3);
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            Toast makeText = Toast.makeText(activity, R.string.error_no_external_application_found, 0);
            makeText.show();
            Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        }
    }

    public static final void openUri(Activity activity, String str) {
        Intrinsics.checkParameterIsNotNull(activity, "activity");
        Intrinsics.checkParameterIsNotNull(str, "uri");
        try {
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
        } catch (ActivityNotFoundException unused) {
            Toast makeText = Toast.makeText(activity, R.string.error_no_external_application_found, 0);
            makeText.show();
            Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        }
    }

    public static final void openMedia(Activity activity, String str, String str2) {
        Intrinsics.checkParameterIsNotNull(activity, "activity");
        Intrinsics.checkParameterIsNotNull(str, "savedMediaPath");
        Intrinsics.checkParameterIsNotNull(str2, "mimeType");
        File file = new File(str);
        Context context = activity;
        Uri uriForFile = FileProvider.getUriForFile(context, "fr.gouv.tchap.a.fileProvider", file);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uriForFile, str2);
        intent.addFlags(1);
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            Toast makeText = Toast.makeText(context, R.string.error_no_external_application_found, 0);
            makeText.show();
            Intrinsics.checkExpressionValueIsNotNull(makeText, "Toast\n        .makeText(…         show()\n        }");
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|9) */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0048, code lost:
        r4 = android.widget.Toast.makeText(r4, fr.gouv.tchap.a.R.string.error_no_external_application_found, 0);
        r4.show();
        kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r4, "Toast\n        .makeText(…         show()\n        }");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x002a */
    public static final void openPlayStore(Activity activity, String str) {
        String str2 = "android.intent.action.VIEW";
        Intrinsics.checkParameterIsNotNull(activity, "activity");
        Intrinsics.checkParameterIsNotNull(str, "appId");
        StringBuilder sb = new StringBuilder();
        sb.append("market://details?id=");
        sb.append(str);
        activity.startActivity(new Intent(str2, Uri.parse(sb.toString())));
        StringBuilder sb2 = new StringBuilder();
        sb2.append("https://play.google.com/store/apps/details?id=");
        sb2.append(str);
        activity.startActivity(new Intent(str2, Uri.parse(sb2.toString())));
    }
}
