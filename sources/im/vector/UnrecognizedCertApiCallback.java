package im.vector;

import im.vector.UnrecognizedCertHandler.Callback;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.ApiFailureCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;

public abstract class UnrecognizedCertApiCallback<T> extends SimpleApiCallback<T> {
    private ApiCallback mCallback;
    private HomeServerConnectionConfig mHsConfig;

    public abstract void onAcceptedCert();

    public UnrecognizedCertApiCallback(HomeServerConnectionConfig homeServerConnectionConfig, ApiCallback apiCallback) {
        super((ApiFailureCallback) apiCallback);
        this.mHsConfig = homeServerConnectionConfig;
        this.mCallback = apiCallback;
    }

    public UnrecognizedCertApiCallback(HomeServerConnectionConfig homeServerConnectionConfig) {
        this.mHsConfig = homeServerConnectionConfig;
    }

    public void onTLSOrNetworkError(Exception exc) {
        super.onNetworkError(exc);
    }

    public void onNetworkError(final Exception exc) {
        if (!UnrecognizedCertHandler.handle(this.mHsConfig, exc, new Callback() {
            public void onAccept() {
                UnrecognizedCertApiCallback.this.onAcceptedCert();
            }

            public void onIgnore() {
                UnrecognizedCertApiCallback.this.onTLSOrNetworkError(exc);
            }

            public void onReject() {
                UnrecognizedCertApiCallback.this.onTLSOrNetworkError(exc);
            }
        })) {
            onTLSOrNetworkError(exc);
        }
    }
}
