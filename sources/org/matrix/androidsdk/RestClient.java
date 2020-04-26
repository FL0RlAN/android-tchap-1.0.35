package org.matrix.androidsdk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.google.gson.Gson;
import im.vector.util.UrlUtilKt;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.PolymorphicRequestBodyConverter;
import org.matrix.androidsdk.core.UnsentEventsManager;
import org.matrix.androidsdk.core.json.GsonProvider;
import org.matrix.androidsdk.core.listeners.IMXNetworkEventListener;
import org.matrix.androidsdk.network.NetworkConnectivityReceiver;
import org.matrix.androidsdk.rest.model.login.Credentials;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient<T> {
    protected static final int CONNECTION_TIMEOUT_MS = 30000;
    /* access modifiers changed from: private */
    public static final String LOG_TAG = RestClient.class.getSimpleName();
    public static final String URI_API_PREFIX_IDENTITY = "_matrix/identity/api/v1/";
    public static final String URI_API_PREFIX_PATH = "_matrix/client/";
    public static final String URI_API_PREFIX_PATH_MEDIA_PROXY_UNSTABLE = "_matrix/media_proxy/unstable/";
    public static final String URI_API_PREFIX_PATH_MEDIA_R0 = "_matrix/media/r0/";
    public static final String URI_API_PREFIX_PATH_R0 = "_matrix/client/r0/";
    public static final String URI_API_PREFIX_PATH_UNSTABLE = "_matrix/client/unstable/";
    public static final String URI_IDENTITY_PATH = "_matrix/identity/api/v1";
    public static final String URI_IDENTITY_PATH_V2 = "_matrix/identity/v2/";
    public static final String URI_INTEGRATION_MANAGER_PATH = "_matrix/integrations/v1";
    public static boolean mUseMXExecutor = false;
    /* access modifiers changed from: private */
    public static String sUserAgent = null;
    /* access modifiers changed from: private */
    public String mAccessToken;
    /* access modifiers changed from: protected */
    public T mApi;
    protected HomeServerConnectionConfig mHsConfig;
    private OkHttpClient mOkHttpClient;
    protected UnsentEventsManager mUnsentEventsManager;

    /* renamed from: org.matrix.androidsdk.RestClient$3 reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$org$matrix$androidsdk$RestClient$EndPointServer = new int[EndPointServer.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            $SwitchMap$org$matrix$androidsdk$RestClient$EndPointServer[EndPointServer.IDENTITY_SERVER.ordinal()] = 1;
            $SwitchMap$org$matrix$androidsdk$RestClient$EndPointServer[EndPointServer.ANTIVIRUS_SERVER.ordinal()] = 2;
            try {
                $SwitchMap$org$matrix$androidsdk$RestClient$EndPointServer[EndPointServer.HOME_SERVER.ordinal()] = 3;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public enum EndPointServer {
        HOME_SERVER,
        IDENTITY_SERVER,
        ANTIVIRUS_SERVER
    }

    public RestClient(HomeServerConnectionConfig homeServerConnectionConfig, Class<T> cls, String str) {
        this(homeServerConnectionConfig, cls, str, GsonProvider.provideGson(), EndPointServer.HOME_SERVER);
    }

    public RestClient(HomeServerConnectionConfig homeServerConnectionConfig, Class<T> cls, String str, Gson gson) {
        this(homeServerConnectionConfig, cls, str, gson, EndPointServer.HOME_SERVER);
    }

    public RestClient(HomeServerConnectionConfig homeServerConnectionConfig, Class<T> cls, String str, Gson gson, boolean z) {
        this(homeServerConnectionConfig, cls, str, gson, z ? EndPointServer.IDENTITY_SERVER : EndPointServer.HOME_SERVER);
    }

    public RestClient(HomeServerConnectionConfig homeServerConnectionConfig, Class<T> cls, String str, Gson gson, EndPointServer endPointServer) {
        this.mHsConfig = homeServerConnectionConfig;
        if (endPointServer == EndPointServer.HOME_SERVER) {
            Credentials credentials = homeServerConnectionConfig.getCredentials();
            if (credentials != null) {
                this.mAccessToken = credentials.accessToken;
            }
        }
        AnonymousClass1 r0 = new Interceptor() {
            public Response intercept(Chain chain) throws IOException {
                Builder newBuilder = chain.request().newBuilder();
                if (RestClient.sUserAgent != null) {
                    newBuilder.addHeader("User-Agent", RestClient.sUserAgent);
                }
                if (RestClient.this.mAccessToken != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Bearer ");
                    sb.append(RestClient.this.mAccessToken);
                    newBuilder.addHeader("Authorization", sb.toString());
                }
                return chain.proceed(newBuilder.build());
            }
        };
        String makeEndpoint = makeEndpoint(homeServerConnectionConfig, str, endPointServer);
        this.mOkHttpClient = RestHttpClientFactoryProvider.Companion.getDefaultProvider().createHttpClient(homeServerConnectionConfig, makeEndpoint, r0);
        this.mApi = new Retrofit.Builder().baseUrl(makeEndpoint).addConverterFactory(PolymorphicRequestBodyConverter.FACTORY).addConverterFactory(GsonConverterFactory.create(gson)).client(this.mOkHttpClient).build().create(cls);
    }

    private String makeEndpoint(HomeServerConnectionConfig homeServerConnectionConfig, String str, EndPointServer endPointServer) {
        String str2;
        int i = AnonymousClass3.$SwitchMap$org$matrix$androidsdk$RestClient$EndPointServer[endPointServer.ordinal()];
        if (i == 1) {
            str2 = homeServerConnectionConfig.getIdentityServerUri().toString();
        } else if (i != 2) {
            str2 = homeServerConnectionConfig.getHomeserverUri().toString();
        } else {
            str2 = homeServerConnectionConfig.getAntiVirusServerUri().toString();
        }
        String sanitizeBaseUrl = sanitizeBaseUrl(str2);
        String sanitizeDynamicPath = sanitizeDynamicPath(str);
        StringBuilder sb = new StringBuilder();
        sb.append(sanitizeBaseUrl);
        sb.append(sanitizeDynamicPath);
        return sb.toString();
    }

    private String sanitizeBaseUrl(String str) {
        String str2 = "/";
        if (str.endsWith(str2)) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        return sb.toString();
    }

    private String sanitizeDynamicPath(String str) {
        if (str.startsWith("http://")) {
            return str.substring(7);
        }
        return str.startsWith(UrlUtilKt.HTTPS_SCHEME) ? str.substring(8) : str;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0069  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00f7  */
    public static void initUserAgent(Context context, String str, String str2) {
        String str3;
        String str4 = "";
        if (context != null) {
            try {
                String packageName = context.getApplicationContext().getPackageName();
                PackageManager packageManager = context.getPackageManager();
                String charSequence = packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, 0)).toString();
                try {
                    String str5 = packageManager.getPackageInfo(context.getApplicationContext().getPackageName(), 0).versionName;
                    if (charSequence.matches("\\A\\p{ASCII}*\\z")) {
                        packageName = charSequence;
                    }
                    String str6 = packageName;
                    str3 = str5;
                    str4 = str6;
                } catch (Exception e) {
                    e = e;
                    str3 = str4;
                    str4 = charSequence;
                    String str7 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## initUserAgent() : failed ");
                    sb.append(e.getMessage());
                    Log.e(str7, sb.toString(), e);
                    sUserAgent = System.getProperty("http.agent");
                    if (!TextUtils.isEmpty(str4)) {
                    }
                    if (sUserAgent == null) {
                    }
                    return;
                }
            } catch (Exception e2) {
                e = e2;
                str3 = str4;
                String str72 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## initUserAgent() : failed ");
                sb2.append(e.getMessage());
                Log.e(str72, sb2.toString(), e);
                sUserAgent = System.getProperty("http.agent");
                if (!TextUtils.isEmpty(str4)) {
                }
                if (sUserAgent == null) {
                }
                return;
            }
        } else {
            str3 = str4;
        }
        sUserAgent = System.getProperty("http.agent");
        if (!TextUtils.isEmpty(str4) || TextUtils.isEmpty(str3)) {
            if (sUserAgent == null) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Java");
                sb3.append(System.getProperty("java.version"));
                sUserAgent = sb3.toString();
            }
            return;
        }
        String str8 = sUserAgent;
        String str9 = "; MatrixAndroidSDK ";
        String str10 = "/";
        String str11 = ")";
        if (!(str8 == null || str8.lastIndexOf(str11) == -1)) {
            String str12 = "(";
            if (sUserAgent.contains(str12)) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str4);
                sb4.append(str10);
                sb4.append(str3);
                sb4.append(" ");
                String str13 = sUserAgent;
                sb4.append(str13.substring(str13.indexOf(str12), sUserAgent.lastIndexOf(str11) - 1));
                sb4.append("; Flavour ");
                sb4.append(str2);
                sb4.append(str9);
                sb4.append(str);
                sb4.append(str11);
                sUserAgent = sb4.toString();
            }
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append(str4);
        sb5.append(str10);
        sb5.append(str3);
        sb5.append(" ( Flavour ");
        sb5.append(str2);
        sb5.append(str9);
        sb5.append(str);
        sb5.append(str11);
        sUserAgent = sb5.toString();
    }

    public static String getUserAgent() {
        return sUserAgent;
    }

    /* access modifiers changed from: private */
    public void refreshConnectionTimeout(NetworkConnectivityReceiver networkConnectivityReceiver) {
        OkHttpClient.Builder newBuilder = this.mOkHttpClient.newBuilder();
        if (networkConnectivityReceiver.isConnected()) {
            float timeoutScale = networkConnectivityReceiver.getTimeoutScale();
            float f = 30000.0f * timeoutScale;
            float f2 = timeoutScale * 60000.0f;
            long j = (long) ((int) f2);
            newBuilder.connectTimeout((long) ((int) f), TimeUnit.MILLISECONDS).readTimeout(j, TimeUnit.MILLISECONDS).writeTimeout(j, TimeUnit.MILLISECONDS);
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## refreshConnectionTimeout()  : update setConnectTimeout to ");
            sb.append(f);
            String str2 = " ms";
            sb.append(str2);
            Log.d(str, sb.toString());
            String str3 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## refreshConnectionTimeout()  : update setReadTimeout to ");
            sb2.append(f2);
            sb2.append(str2);
            Log.d(str3, sb2.toString());
            String str4 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("## refreshConnectionTimeout()  : update setWriteTimeout to ");
            sb3.append(f2);
            sb3.append(str2);
            Log.d(str4, sb3.toString());
        } else {
            newBuilder.connectTimeout(1, TimeUnit.MILLISECONDS);
            Log.d(LOG_TAG, "## refreshConnectionTimeout()  : update the requests timeout to 1 ms");
        }
        this.mOkHttpClient = newBuilder.build();
    }

    /* access modifiers changed from: protected */
    public void setConnectionTimeout(int i) {
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        if (unsentEventsManager != null) {
            NetworkConnectivityReceiver networkConnectivityReceiver = unsentEventsManager.getNetworkConnectivityReceiver();
            if (networkConnectivityReceiver != null) {
                i = networkConnectivityReceiver.isConnected() ? (int) (((float) i) * networkConnectivityReceiver.getTimeoutScale()) : 1000;
            }
        }
        if (i != this.mOkHttpClient.connectTimeoutMillis()) {
            this.mOkHttpClient = this.mOkHttpClient.newBuilder().connectTimeout((long) i, TimeUnit.MILLISECONDS).build();
        }
    }

    public void setUnsentEventsManager(UnsentEventsManager unsentEventsManager) {
        this.mUnsentEventsManager = unsentEventsManager;
        final NetworkConnectivityReceiver networkConnectivityReceiver = this.mUnsentEventsManager.getNetworkConnectivityReceiver();
        refreshConnectionTimeout(networkConnectivityReceiver);
        networkConnectivityReceiver.addEventListener(new IMXNetworkEventListener() {
            public void onNetworkConnectionUpdate(boolean z) {
                String access$200 = RestClient.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## setUnsentEventsManager()  : update the requests timeout to ");
                sb.append(z ? RestClient.CONNECTION_TIMEOUT_MS : 1);
                sb.append(" ms");
                Log.d(access$200, sb.toString());
                RestClient.this.refreshConnectionTimeout(networkConnectivityReceiver);
            }
        });
    }

    public void setAccessToken(String str) {
        this.mAccessToken = str;
    }
}
