package im.vector;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import fr.gouv.tchap.a.R;
import im.vector.UnrecognizedCertHandler.Callback;
import im.vector.activity.CommonActivityUtils;
import java.util.Arrays;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiFailureCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.ssl.CertUtil;
import org.matrix.androidsdk.ssl.Fingerprint;
import org.matrix.androidsdk.ssl.UnrecognizedCertificateException;

public class ErrorListener implements ApiFailureCallback {
    private static final String LOG_TAG = ErrorListener.class.getSimpleName();
    /* access modifiers changed from: private */
    public final Activity mActivity;
    /* access modifiers changed from: private */
    public final MXSession mSession;

    public ErrorListener(MXSession mXSession, Activity activity) {
        this.mSession = mXSession;
        this.mActivity = activity;
    }

    public void onNetworkError(Exception exc) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Network error: ");
        sb.append(exc.getMessage());
        Log.e(str, sb.toString(), exc);
        if (!VectorApp.isAppInBackground()) {
            UnrecognizedCertificateException certificateException = CertUtil.getCertificateException(exc);
            if (certificateException == null) {
                handleNetworkError(exc);
            } else {
                handleCertError(certificateException, exc);
            }
        }
    }

    public void onMatrixError(MatrixError matrixError) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Matrix error: ");
        sb.append(matrixError.errcode);
        sb.append(" - ");
        sb.append(matrixError.error);
        Log.e(str, sb.toString());
        if (MatrixError.UNKNOWN_TOKEN.equals(matrixError.errcode)) {
            CommonActivityUtils.logout(this.mActivity);
        }
    }

    public void onUnexpectedError(Exception exc) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected error: ");
        sb.append(exc.getMessage());
        Log.e(str, sb.toString(), exc);
    }

    /* access modifiers changed from: private */
    public void handleNetworkError(Exception exc) {
        this.mActivity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(ErrorListener.this.mActivity, ErrorListener.this.mActivity.getString(R.string.network_error), 0).show();
            }
        });
    }

    private void handleCertError(UnrecognizedCertificateException unrecognizedCertificateException, final Exception exc) {
        final Fingerprint fingerprint = unrecognizedCertificateException.getFingerprint();
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Found fingerprint: SHA-256: ");
        sb.append(fingerprint.getBytesAsHexString());
        Log.d(str, sb.toString());
        this.mActivity.runOnUiThread(new Runnable() {
            public void run() {
                UnrecognizedCertHandler.show(ErrorListener.this.mSession.getHomeServerConfig(), fingerprint, true, new Callback() {
                    public void onAccept() {
                        Matrix.getInstance(ErrorListener.this.mActivity.getApplicationContext()).getLoginStorage().replaceCredentials(ErrorListener.this.mSession.getHomeServerConfig());
                    }

                    public void onIgnore() {
                        ErrorListener.this.handleNetworkError(exc);
                    }

                    public void onReject() {
                        CommonActivityUtils.logout((Context) ErrorListener.this.mActivity, Arrays.asList(new MXSession[]{ErrorListener.this.mSession}), true, null);
                    }
                });
            }
        });
    }
}
