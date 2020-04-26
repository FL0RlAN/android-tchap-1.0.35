package org.matrix.androidsdk.rest.callback;

import androidx.core.os.EnvironmentCompat;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.UnsentEventsManager;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.json.GsonProvider;
import org.matrix.androidsdk.core.model.HttpError;
import org.matrix.androidsdk.core.model.HttpException;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.core.rest.DefaultRetrofit2ResponseHandler;
import org.matrix.androidsdk.core.rest.DefaultRetrofit2ResponseHandler.Listener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestAdapterCallback<T> implements Callback<T> {
    private static final String LOG_TAG = "RestAdapterCallback";
    private final ApiCallback mApiCallback;
    private final String mEventDescription;
    private final boolean mIgnoreEventTimeLifeInOffline;
    private final RequestRetryCallBack mRequestRetryCallBack;
    private final UnsentEventsManager mUnsentEventsManager;

    public interface RequestRetryCallBack {
        void onRetry();
    }

    public RestAdapterCallback(String str, UnsentEventsManager unsentEventsManager, ApiCallback apiCallback, RequestRetryCallBack requestRetryCallBack) {
        this(str, unsentEventsManager, false, apiCallback, requestRetryCallBack);
    }

    public RestAdapterCallback(String str, UnsentEventsManager unsentEventsManager, boolean z, ApiCallback apiCallback, RequestRetryCallBack requestRetryCallBack) {
        if (str != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Trigger the event [");
            sb.append(str);
            sb.append("]");
            Log.d(LOG_TAG, sb.toString());
        }
        this.mEventDescription = str;
        this.mIgnoreEventTimeLifeInOffline = z;
        this.mApiCallback = apiCallback;
        this.mRequestRetryCallBack = requestRetryCallBack;
        this.mUnsentEventsManager = unsentEventsManager;
    }

    /* access modifiers changed from: protected */
    public void onEventSent() {
        String str = LOG_TAG;
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        if (unsentEventsManager != null) {
            try {
                if (!unsentEventsManager.getNetworkConnectivityReceiver().isConnected()) {
                    Log.d(str, "## onEventSent(): request succeed, while network seen as disconnected => ask ConnectivityReceiver to dispatch info");
                    this.mUnsentEventsManager.getNetworkConnectivityReceiver().checkNetworkConnection(this.mUnsentEventsManager.getContext());
                }
                this.mUnsentEventsManager.onEventSent(this.mApiCallback);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("## onEventSent(): Exception ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
    }

    public void onResponse(Call<T> call, Response<T> response) {
        try {
            handleResponse(response);
        } catch (IOException e) {
            onFailure(call, e);
        }
    }

    private void handleResponse(final Response<T> response) throws IOException {
        DefaultRetrofit2ResponseHandler.handleResponse(response, new Listener<T>() {
            public void onSuccess(Response<T> response) {
                RestAdapterCallback.this.success(response.body(), response);
            }

            public void onHttpError(HttpError httpError) {
                RestAdapterCallback.this.failure(response, new HttpException(httpError));
            }
        });
    }

    public void onFailure(Call<T> call, Throwable th) {
        failure(null, (Exception) th);
    }

    public void success(T t, Response<T> response) {
        String str = this.mEventDescription;
        String str2 = LOG_TAG;
        if (str != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("## Succeed() : [");
            sb.append(this.mEventDescription);
            sb.append("]");
            Log.d(str2, sb.toString());
        }
        try {
            onEventSent();
            if (this.mApiCallback != null) {
                try {
                    this.mApiCallback.onSuccess(t);
                } catch (Exception e) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## succeed() : onSuccess failed ");
                    sb2.append(e.getMessage());
                    Log.e(str2, sb2.toString(), e);
                    this.mApiCallback.onUnexpectedError(e);
                }
            }
        } catch (Exception e2) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("## succeed(): Exception ");
            sb3.append(e2.getMessage());
            Log.e(str2, sb3.toString(), e2);
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:34|35|36|37|78) */
    /* JADX WARNING: Code restructure failed: missing block: B:77:?, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:36:0x0091 */
    public void failure(Response<T> response, Exception exc) {
        MatrixError matrixError;
        String str = this.mEventDescription;
        String str2 = LOG_TAG;
        if (str != null) {
            String str3 = exc != null ? exc.getMessage() : response != null ? response.message() : EnvironmentCompat.MEDIA_UNKNOWN;
            StringBuilder sb = new StringBuilder();
            sb.append("## failure(): [");
            sb.append(this.mEventDescription);
            sb.append("] with error ");
            sb.append(str3);
            Log.d(str2, sb.toString());
        }
        boolean z = false;
        boolean z2 = response == null || response.code() < 400 || response.code() > 500;
        if (exc.getCause() == null || (!(exc.getCause() instanceof MalformedJsonException) && !(exc.getCause() instanceof JsonSyntaxException))) {
            z = true;
        }
        if ((z && z2) && this.mUnsentEventsManager != null) {
            Log.d(str2, "Add it to the UnsentEventsManager");
            this.mUnsentEventsManager.onEventSendingFailed(this.mEventDescription, this.mIgnoreEventTimeLifeInOffline, response, exc, this.mApiCallback, this.mRequestRetryCallBack);
        } else if (exc == null || !(exc instanceof IOException)) {
            try {
                HttpError httpError = ((HttpException) exc).getHttpError();
                response.errorBody();
                String errorBody = httpError.getErrorBody();
                matrixError = (MatrixError) GsonProvider.provideGson().fromJson(errorBody, MatrixError.class);
                matrixError.mStatus = Integer.valueOf(response.code());
                matrixError.mReason = response.message();
                matrixError.mErrorBodyAsString = errorBody;
            } catch (Exception unused) {
                matrixError = null;
            }
            if (matrixError != null) {
                if (MatrixError.LIMIT_EXCEEDED.equals(matrixError.errcode)) {
                    UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
                    if (unsentEventsManager != null) {
                        unsentEventsManager.onEventSendingFailed(this.mEventDescription, this.mIgnoreEventTimeLifeInOffline, response, exc, this.mApiCallback, this.mRequestRetryCallBack);
                        return;
                    }
                }
                if (MatrixError.isConfigurationErrorCode(matrixError.errcode)) {
                    UnsentEventsManager unsentEventsManager2 = this.mUnsentEventsManager;
                    if (unsentEventsManager2 != null) {
                        unsentEventsManager2.onConfigurationErrorCode(matrixError.errcode, this.mEventDescription);
                        return;
                    }
                }
                try {
                    if (this.mApiCallback != null) {
                        this.mApiCallback.onMatrixError(matrixError);
                    }
                } catch (Exception e) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## failure():  MatrixError ");
                    sb2.append(e.getMessage());
                    Log.e(str2, sb2.toString(), e);
                }
            } else {
                try {
                    if (this.mApiCallback != null) {
                        this.mApiCallback.onUnexpectedError(exc);
                    }
                } catch (Exception e2) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("## failure():  UnexpectedError ");
                    sb3.append(e2.getMessage());
                    Log.e(str2, sb3.toString(), e2);
                }
            }
        } else {
            try {
                if (this.mApiCallback != null) {
                    this.mApiCallback.onNetworkError(exc);
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("## failure(): onNetworkError ");
                    sb4.append(exc.getLocalizedMessage());
                    Log.e(str2, sb4.toString(), exc);
                }
            } catch (Exception e3) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append("## failure():  NetworkError ");
                sb5.append(e3.getMessage());
                Log.e(str2, sb5.toString(), e3);
            }
        }
    }
}
