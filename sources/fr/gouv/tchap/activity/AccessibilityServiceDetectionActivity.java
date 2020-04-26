package fr.gouv.tchap.activity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.version.TchapVersionCheckActivity;
import im.vector.util.PreferencesManager;
import org.matrix.androidsdk.core.Log;

public class AccessibilityServiceDetectionActivity extends AppCompatActivity {
    public static boolean isAccessibilityServiceActive(ContentResolver contentResolver) {
        int i;
        try {
            i = Secure.getInt(contentResolver, "accessibility_enabled");
        } catch (SettingNotFoundException unused) {
            Log.d("AccessibilityServiceDetectionActivity", "*** Security : accessibility settings not found ****");
            i = 0;
        }
        if (i == 0) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!PreferencesManager.detectAccessibilityService(this)) {
            finish(true);
        } else if (!isAccessibilityServiceActive(getContentResolver())) {
            finish(true);
        } else {
            new Builder(this).setMessage((int) R.string.accessibility_security_dialog_message).setIcon((int) R.drawable.logo_transparent).setTitle((int) R.string.security_dialog_title).setPositiveButton((int) R.string.security_dialog_start, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    AccessibilityServiceDetectionActivity.this.finish(true);
                }
            }).setNeutralButton((int) R.string.security_dialog_start_an_stop_detecting, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    PreferencesManager.putDetectAccessibilityService(AccessibilityServiceDetectionActivity.this, false);
                    AccessibilityServiceDetectionActivity.this.finish(true);
                }
            }).setNegativeButton((int) R.string.security_dialog_stop, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    AccessibilityServiceDetectionActivity.this.finish(false);
                }
            }).setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialogInterface) {
                    AccessibilityServiceDetectionActivity.this.finish(false);
                }
            }).show();
        }
    }

    /* access modifiers changed from: private */
    public void finish(boolean z) {
        finish();
        if (z) {
            startActivity(new Intent(this, TchapVersionCheckActivity.class));
        }
    }
}
