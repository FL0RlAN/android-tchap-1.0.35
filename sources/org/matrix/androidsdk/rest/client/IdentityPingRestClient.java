package org.matrix.androidsdk.rest.client;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONObject;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.rest.DefaultRetrofit2CallbackWrapper;
import org.matrix.androidsdk.rest.api.IdentityPingApi;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0014\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t¨\u0006\u000b"}, d2 = {"Lorg/matrix/androidsdk/rest/client/IdentityPingRestClient;", "Lorg/matrix/androidsdk/RestClient;", "Lorg/matrix/androidsdk/rest/api/IdentityPingApi;", "hsConfig", "Lorg/matrix/androidsdk/HomeServerConnectionConfig;", "(Lorg/matrix/androidsdk/HomeServerConnectionConfig;)V", "ping", "", "callback", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Lorg/json/JSONObject;", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: IdentityPingRestClient.kt */
public final class IdentityPingRestClient extends RestClient<IdentityPingApi> {
    public IdentityPingRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        Intrinsics.checkParameterIsNotNull(homeServerConnectionConfig, "hsConfig");
        HomeServerConnectionConfig homeServerConnectionConfig2 = homeServerConnectionConfig;
        super(homeServerConnectionConfig2, IdentityPingApi.class, "", JsonUtils.getGson(false), true);
    }

    public final void ping(ApiCallback<JSONObject> apiCallback) {
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((IdentityPingApi) this.mApi).ping().enqueue(new DefaultRetrofit2CallbackWrapper(apiCallback));
    }
}
