package org.matrix.androidsdk.core.rest;

import java.io.IOException;
import org.matrix.androidsdk.core.model.HttpError;
import retrofit2.Response;

public class DefaultRetrofit2ResponseHandler {

    public interface Listener<T> {
        void onHttpError(HttpError httpError);

        void onSuccess(Response<T> response);
    }

    public static <T> void handleResponse(Response<T> response, Listener<T> listener) throws IOException {
        if (response.isSuccessful()) {
            listener.onSuccess(response);
        } else {
            listener.onHttpError(new HttpError(response.errorBody().string(), response.code()));
        }
    }
}
