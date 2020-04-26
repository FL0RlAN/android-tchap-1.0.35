package im.vector.gcm;

import android.content.Context;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import im.vector.VectorApp;
import org.matrix.androidsdk.core.Log;

public class GCMHelper {
    private static final String LOG_TAG = GCMHelper.class.getSimpleName();

    static String getRegistrationToken() {
        String str = "StackTrace";
        if (VectorApp.getInstance() == null) {
            Log.e(LOG_TAG, "## getRegistrationToken() : No active application", new Exception(str));
        } else {
            try {
                if (FirebaseApp.initializeApp(VectorApp.getInstance()) == null) {
                    Log.e(LOG_TAG, "## getRegistrationToken() : cannot initialise FirebaseApp", new Exception(str));
                }
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getRegistrationToken() : init failed ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
        String str3 = null;
        try {
            str3 = FirebaseInstanceId.getInstance().getToken();
            String str4 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## getRegistrationToken(): ");
            sb2.append(str3);
            Log.d(str4, sb2.toString());
            return str3;
        } catch (Exception e2) {
            String str5 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("## getRegistrationToken() : failed ");
            sb3.append(e2.getMessage());
            Log.e(str5, sb3.toString(), e2);
            return str3;
        }
    }

    static void clearRegistrationToken() {
        try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("##clearRegistrationToken() failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    public static void checkLastVersion(Context context) {
        try {
            ProviderInstaller.installIfNeeded(context);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().showErrorNotification(context, e.getConnectionStatusCode());
        } catch (GooglePlayServicesNotAvailableException e2) {
            Log.e(LOG_TAG, "GooglePlayServicesNotAvailableException", e2);
        }
    }
}
