package org.matrix.androidsdk;

import android.util.Pair;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.rest.client.MXRestExecutorService;
import org.matrix.androidsdk.ssl.CertUtil;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u0011\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004J\u001e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0003R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lorg/matrix/androidsdk/RestClientHttpClientFactory;", "", "testInterceptor", "Lokhttp3/Interceptor;", "(Lokhttp3/Interceptor;)V", "createHttpClient", "Lokhttp3/OkHttpClient;", "hsConfig", "Lorg/matrix/androidsdk/HomeServerConnectionConfig;", "endPoint", "", "authenticationInterceptor", "Companion", "matrix-sdk-core_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: RestClientHttpClientFactory.kt */
public final class RestClientHttpClientFactory {
    public static final Companion Companion = new Companion(null);
    public static final long READ_TIMEOUT_MS = 60000;
    public static final long WRITE_TIMEOUT_MS = 60000;
    private final Interceptor testInterceptor;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lorg/matrix/androidsdk/RestClientHttpClientFactory$Companion;", "", "()V", "READ_TIMEOUT_MS", "", "WRITE_TIMEOUT_MS", "matrix-sdk-core_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: RestClientHttpClientFactory.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public RestClientHttpClientFactory() {
        this(null, 1, null);
    }

    public RestClientHttpClientFactory(Interceptor interceptor) {
        this.testInterceptor = interceptor;
    }

    public /* synthetic */ RestClientHttpClientFactory(Interceptor interceptor, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            interceptor = null;
        }
        this(interceptor);
    }

    public final OkHttpClient createHttpClient(HomeServerConnectionConfig homeServerConnectionConfig, String str, Interceptor interceptor) {
        Intrinsics.checkParameterIsNotNull(homeServerConnectionConfig, "hsConfig");
        Intrinsics.checkParameterIsNotNull(str, "endPoint");
        Intrinsics.checkParameterIsNotNull(interceptor, "authenticationInterceptor");
        Builder addInterceptor = new OkHttpClient().newBuilder().connectTimeout((long) 30000, TimeUnit.MILLISECONDS).readTimeout(60000, TimeUnit.MILLISECONDS).writeTimeout(60000, TimeUnit.MILLISECONDS).addInterceptor(interceptor);
        if (RestClient.mUseMXExecutor) {
            addInterceptor.dispatcher(new Dispatcher(new MXRestExecutorService()));
        }
        try {
            Pair newPinnedSSLSocketFactory = CertUtil.newPinnedSSLSocketFactory(homeServerConnectionConfig);
            addInterceptor.sslSocketFactory((SSLSocketFactory) newPinnedSSLSocketFactory.first, (X509TrustManager) newPinnedSSLSocketFactory.second);
            addInterceptor.hostnameVerifier(CertUtil.newHostnameVerifier(homeServerConnectionConfig));
            addInterceptor.connectionSpecs(CertUtil.newConnectionSpecs(homeServerConnectionConfig, str));
        } catch (Exception e) {
            String name = RestClientHttpClientFactory.class.getName();
            StringBuilder sb = new StringBuilder();
            sb.append("## RestClient() setSslSocketFactory failed: ");
            sb.append(e.getMessage());
            Log.e(name, sb.toString(), e);
        }
        OkHttpClient build = addInterceptor.build();
        Intrinsics.checkExpressionValueIsNotNull(build, "okHttpClientBuilder.build()");
        return build;
    }
}
