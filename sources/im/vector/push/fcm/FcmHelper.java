package im.vector.push.fcm;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import fr.gouv.tchap.a.R;
import org.matrix.androidsdk.core.Log;

public class FcmHelper {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = FcmHelper.class.getSimpleName();
    private static final String PREFS_KEY_FCM_TOKEN = "FCM_TOKEN";

    public static String getFcmToken(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREFS_KEY_FCM_TOKEN, null);
    }

    public static void storeFcmToken(Context context, String str) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREFS_KEY_FCM_TOKEN, str).apply();
    }

    public static void ensureFcmTokenIsRetrieved(final Activity activity) {
        if (!TextUtils.isEmpty(getFcmToken(activity))) {
            return;
        }
        if (checkPlayServices(activity)) {
            try {
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(activity, (OnSuccessListener<? super TResult>) new OnSuccessListener<InstanceIdResult>() {
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        FcmHelper.storeFcmToken(activity, instanceIdResult.getToken());
                    }
                }).addOnFailureListener(activity, (OnFailureListener) new OnFailureListener() {
                    public void onFailure(Exception exc) {
                        String access$000 = FcmHelper.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## ensureFcmTokenIsRetrieved() : failed ");
                        sb.append(exc.getMessage());
                        Log.e(access$000, sb.toString(), exc);
                    }
                });
            } catch (Throwable th) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## ensureFcmTokenIsRetrieved() : failed ");
                sb.append(th.getMessage());
                Log.e(str, sb.toString(), th);
            }
        } else {
            Toast.makeText(activity, R.string.no_valid_google_play_services_apk, 0).show();
            Log.e(LOG_TAG, "No valid Google Play Services found. Cannot use FCM.");
        }
    }

    private static boolean checkPlayServices(Activity activity) {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity) == 0;
    }
}
