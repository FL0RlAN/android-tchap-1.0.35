package org.matrix.androidsdk.crypto.rest;

import androidx.core.os.EnvironmentCompat;
import com.google.gson.Gson;
import java.io.IOException;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.HttpError;
import org.matrix.androidsdk.core.model.HttpException;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.core.rest.DefaultRetrofit2ResponseHandler;
import org.matrix.androidsdk.core.rest.DefaultRetrofit2ResponseHandler.Listener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CryptoRestAdapterCallback<T> implements Callback<T> {
    private static final String LOG_TAG = "RestAdapterCallback";
    private final ApiCallback mApiCallback;
    private final String mEventDescription;
    private final RequestRetryCallBack mRequestRetryCallBack;

    public interface RequestRetryCallBack {
        void onRetry();
    }

    public CryptoRestAdapterCallback(String str, ApiCallback apiCallback, RequestRetryCallBack requestRetryCallBack) {
        if (str != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Trigger the event [");
            sb.append(str);
            sb.append("]");
            Log.d(LOG_TAG, sb.toString());
        }
        this.mEventDescription = str;
        this.mApiCallback = apiCallback;
        this.mRequestRetryCallBack = requestRetryCallBack;
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
                CryptoRestAdapterCallback.this.success(response.body(), response);
            }

            public void onHttpError(HttpError httpError) {
                CryptoRestAdapterCallback.this.failure(response, new HttpException(httpError));
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

    /* JADX WARNING: Can't wrap try/catch for region: R(5:14|15|16|17|45) */
    /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x0045 */
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
        if (exc == null || !(exc instanceof IOException)) {
            try {
                HttpError httpError = ((HttpException) exc).getHttpError();
                response.errorBody();
                String errorBody = httpError.getErrorBody();
                matrixError = (MatrixError) new Gson().fromJson(errorBody, MatrixError.class);
                matrixError.mStatus = Integer.valueOf(response.code());
                matrixError.mReason = response.message();
                matrixError.mErrorBodyAsString = errorBody;
            } catch (Exception unused) {
                matrixError = null;
            }
            if (matrixError != null) {
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
