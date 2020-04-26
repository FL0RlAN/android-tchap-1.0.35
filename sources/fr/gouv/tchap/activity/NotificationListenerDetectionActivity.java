package fr.gouv.tchap.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.provider.Settings.Secure;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationListenerDetectionActivity extends AppCompatActivity {
    public static String getAppName(String str, Context context) {
        ApplicationInfo applicationInfo;
        PackageManager packageManager = context.getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(str, 0);
        } catch (NameNotFoundException unused) {
            applicationInfo = null;
        }
        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
    }

    public static String[] getListeningServiceAppName(Context context, ContentResolver contentResolver) {
        String string = Secure.getString(contentResolver, "enabled_notification_listeners");
        if (string == null || string.equals("")) {
            return new String[0];
        }
        String[] split = string.split(":");
        String[] strArr = new String[split.length];
        for (int i = 0; i < split.length; i++) {
            strArr[i] = getAppName(split[i].split("/")[0], context);
        }
        return strArr;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        finish(true);
    }

    private void finish(boolean z) {
        finish();
        if (z) {
            startActivity(new Intent(this, AccessibilityServiceDetectionActivity.class));
        }
    }
}
