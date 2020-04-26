package im.vector;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import fr.gouv.tchap.a.R;
import im.vector.EventEmitter.Listener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.ssl.CertUtil;
import org.matrix.androidsdk.ssl.Fingerprint;
import org.matrix.androidsdk.ssl.UnrecognizedCertificateException;

public class UnrecognizedCertHandler {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = UnrecognizedCertHandler.class.getSimpleName();
    private static final Map<String, Set<Fingerprint>> ignoredFingerprints = new HashMap();
    /* access modifiers changed from: private */
    public static final Set<String> openDialogIds = new HashSet();

    public interface Callback {
        void onAccept();

        void onIgnore();

        void onReject();
    }

    public static boolean handle(HomeServerConnectionConfig homeServerConnectionConfig, Exception exc, Callback callback) {
        UnrecognizedCertificateException certificateException = CertUtil.getCertificateException(exc);
        if (certificateException == null) {
            return false;
        }
        show(homeServerConnectionConfig, certificateException.getFingerprint(), false, callback);
        return true;
    }

    public static void show(HomeServerConnectionConfig homeServerConnectionConfig, Fingerprint fingerprint, boolean z, final Callback callback) {
        final String str;
        final Activity currentActivity = VectorApp.getCurrentActivity();
        if (currentActivity != null) {
            if (homeServerConnectionConfig.getCredentials() != null) {
                str = homeServerConnectionConfig.getCredentials().userId;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(homeServerConnectionConfig.getHomeserverUri().toString());
                sb.append(fingerprint.getBytesAsHexString());
                str = sb.toString();
            }
            if (openDialogIds.contains(str)) {
                String str2 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Not opening dialog ");
                sb2.append(str);
                sb2.append(" as one is already open.");
                Log.i(str2, sb2.toString());
                return;
            }
            if (homeServerConnectionConfig.getCredentials() != null) {
                Set set = (Set) ignoredFingerprints.get(homeServerConnectionConfig.getCredentials().userId);
                if (set != null && set.contains(fingerprint)) {
                    callback.onIgnore();
                    return;
                }
            }
            Builder builder = new Builder(currentActivity);
            View inflate = currentActivity.getLayoutInflater().inflate(R.layout.dialog_ssl_fingerprint, null);
            ((TextView) inflate.findViewById(R.id.ssl_fingerprint_title)).setText(currentActivity.getString(R.string.ssl_fingerprint_hash, new Object[]{fingerprint.getType().toString()}));
            ((TextView) inflate.findViewById(R.id.ssl_fingerprint)).setText(fingerprint.getBytesAsHexString());
            TextView textView = (TextView) inflate.findViewById(R.id.ssl_user_id);
            if (homeServerConnectionConfig.getCredentials() != null) {
                textView.setText(currentActivity.getString(R.string.generic_label_and_value, new Object[]{currentActivity.getString(R.string.username), homeServerConnectionConfig.getCredentials().userId}));
            } else {
                textView.setText(currentActivity.getString(R.string.generic_label_and_value, new Object[]{currentActivity.getString(R.string.hs_url), homeServerConnectionConfig.getHomeserverUri().toString()}));
            }
            TextView textView2 = (TextView) inflate.findViewById(R.id.ssl_explanation);
            if (!z) {
                textView2.setText(currentActivity.getString(R.string.ssl_cert_new_account_expl));
            } else if (homeServerConnectionConfig.getAllowedFingerprints().size() > 0) {
                textView2.setText(currentActivity.getString(R.string.ssl_expected_existing_expl));
            } else {
                textView2.setText(currentActivity.getString(R.string.ssl_unexpected_existing_expl));
            }
            builder.setView(inflate);
            builder.setTitle((int) R.string.ssl_could_not_verify);
            builder.setCancelable(false);
            builder.setPositiveButton((int) R.string.ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    callback.onIgnore();
                }
            });
            final AlertDialog create = builder.create();
            final AnonymousClass2 r11 = new Listener<Activity>() {
                public void onEventFired(EventEmitter<Activity> eventEmitter, Activity activity) {
                    if (currentActivity == activity) {
                        callback.onIgnore();
                        create.dismiss();
                    }
                }
            };
            final EventEmitter onActivityDestroyedListener = VectorApp.getInstance().getOnActivityDestroyedListener();
            onActivityDestroyedListener.register(r11);
            create.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    Log.d(UnrecognizedCertHandler.LOG_TAG, "Dismissed!");
                    UnrecognizedCertHandler.openDialogIds.remove(str);
                    onActivityDestroyedListener.unregister(r11);
                }
            });
            create.show();
            openDialogIds.add(str);
        }
    }
}
