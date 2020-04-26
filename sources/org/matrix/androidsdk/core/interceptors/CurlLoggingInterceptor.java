package org.matrix.androidsdk.core.interceptors;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.Response;

public class CurlLoggingInterceptor implements Interceptor {
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }
}
