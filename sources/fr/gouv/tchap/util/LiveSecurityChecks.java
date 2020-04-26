package fr.gouv.tchap.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import androidx.appcompat.app.AlertDialog.Builder;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.activity.AccessibilityServiceDetectionActivity;
import fr.gouv.tchap.activity.NotificationListenerDetectionActivity;
import im.vector.util.PreferencesManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.matrix.androidsdk.core.Log;

public class LiveSecurityChecks {
    private static boolean accessibilityServiceActive_at_stop;
    private static String[] notificationListenerState_at_stop;
    private static boolean stopped_before_starting;
    /* access modifiers changed from: private */
    public Activity attachedActivity;

    public LiveSecurityChecks(Activity activity) {
        stopped_before_starting = false;
        this.attachedActivity = activity;
    }

    public void activityStopped() {
        Log.d(LiveSecurityChecks.class.getSimpleName(), "**** activityStopped ****");
        stopped_before_starting = true;
        notificationListenerState_at_stop = NotificationListenerDetectionActivity.getListeningServiceAppName(this.attachedActivity.getApplicationContext(), this.attachedActivity.getContentResolver());
        accessibilityServiceActive_at_stop = AccessibilityServiceDetectionActivity.isAccessibilityServiceActive(this.attachedActivity.getContentResolver());
    }

    public void checkOnActivityStart() {
        if (stopped_before_starting) {
            stopped_before_starting = false;
            checkAccessibilityChange();
        }
    }

    /* access modifiers changed from: protected */
    public void checkAccessibilityChange() {
        if (PreferencesManager.detectAccessibilityService(this.attachedActivity)) {
            boolean isAccessibilityServiceActive = AccessibilityServiceDetectionActivity.isAccessibilityServiceActive(this.attachedActivity.getContentResolver());
            if (!accessibilityServiceActive_at_stop && isAccessibilityServiceActive) {
                new Builder(this.attachedActivity).setMessage((int) R.string.accessibility_change_security_dialog_message).setIcon((int) R.drawable.logo_transparent).setTitle((int) R.string.security_dialog_title).setPositiveButton((int) R.string.security_dialog_continue, (OnClickListener) null).setNeutralButton((int) R.string.security_dialog_start_an_stop_detecting, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PreferencesManager.putDetectAccessibilityService(LiveSecurityChecks.this.attachedActivity, false);
                    }
                }).setNegativeButton((int) R.string.security_dialog_stop, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LiveSecurityChecks.this.attachedActivity.finishAffinity();
                    }
                }).show();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void checkNotificationListenerChange() {
        if (PreferencesManager.detectNotificationListener(this.attachedActivity)) {
            String[] listeningServiceAppName = NotificationListenerDetectionActivity.getListeningServiceAppName(this.attachedActivity.getApplicationContext(), this.attachedActivity.getContentResolver());
            ArrayList arrayList = new ArrayList();
            List asList = Arrays.asList(notificationListenerState_at_stop);
            for (String str : listeningServiceAppName) {
                if (!asList.contains(str)) {
                    arrayList.add(str);
                }
            }
            if (arrayList.size() != 0) {
                StringBuilder sb = new StringBuilder();
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    String str2 = (String) it.next();
                    sb.append("\n");
                    sb.append("- ");
                    sb.append(str2);
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(this.attachedActivity.getResources().getString(R.string.notification_change_security_dialog_message));
                sb2.append(sb.toString());
                new Builder(this.attachedActivity).setMessage((CharSequence) sb2.toString()).setIcon((int) R.drawable.logo_transparent).setTitle((int) R.string.security_dialog_title).setPositiveButton((int) R.string.security_dialog_continue, (OnClickListener) null).setNeutralButton((int) R.string.security_dialog_start_an_stop_detecting, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PreferencesManager.putDetectNotificationListener(LiveSecurityChecks.this.attachedActivity, false);
                    }
                }).setNegativeButton((int) R.string.security_dialog_stop, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LiveSecurityChecks.this.attachedActivity.finishAffinity();
                    }
                }).show();
            }
        }
    }
}
