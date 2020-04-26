package org.matrix.androidsdk.core.rest;

import java.io.IOException;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.HttpError;
import org.matrix.androidsdk.core.model.HttpException;
import org.matrix.androidsdk.core.rest.DefaultRetrofit2ResponseHandler.Listener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DefaultRetrofit2CallbackWrapper<T> implements Callback<T>, Listener<T> {
    private final ApiCallback<T> apiCallback;

    public DefaultRetrofit2CallbackWrapper(ApiCallback<T> apiCallback2) {
        this.apiCallback = apiCallback2;
    }

    public ApiCallback<T> getApiCallback() {
        return this.apiCallback;
    }

    public void onResponse(Call<T> call, Response<T> response) {
        try {
            handleResponse(response);
        } catch (IOException e) {
            this.apiCallback.onUnexpectedError(e);
        }
    }

    private void handleResponse(Response<T> response) throws IOException {
        DefaultRetrofit2ResponseHandler.handleResponse(response, this);
    }

    public void onFailure(Call<T> call, Throwable th) {
        this.apiCallback.onNetworkError((Exception) th);
    }

    public void onSuccess(Response<T> response) {
        this.apiCallback.onSuccess(response.body());
    }

    public void onHttpError(HttpError httpError) {
        this.apiCallback.onNetworkError(new HttpException(httpError));
    }
}
