package im.vector.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import im.vector.activity.CommonActivityUtils;
import im.vector.util.PreferencesManager;
import org.matrix.androidsdk.core.Log;

public class VectorBootReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = VectorBootReceiver.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onReceive() : ");
        sb.append(intent.getAction());
        Log.d(str, sb.toString());
        if (!TextUtils.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED") && !TextUtils.equals(intent.getAction(), "android.intent.action.ACTION_BOOT_COMPLETED")) {
            return;
        }
        if (PreferencesManager.autoStartOnBoot(context)) {
            Log.d(LOG_TAG, "## onReceive() : starts the application");
            CommonActivityUtils.startEventStreamService(context);
            return;
        }
        Log.d(LOG_TAG, "## onReceive() : the autostart is disabled");
    }
}
