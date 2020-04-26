package org.matrix.androidsdk.rest.client;

import com.google.gson.JsonObject;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.rest.DefaultRetrofit2CallbackWrapper;
import org.matrix.androidsdk.rest.api.CallRulesApi;

public class CallRestClient extends RestClient<CallRulesApi> {
    public CallRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        super(homeServerConnectionConfig, CallRulesApi.class, RestClient.URI_API_PREFIX_PATH_R0, JsonUtils.getGson(false));
    }

    public void getTurnServer(ApiCallback<JsonObject> apiCallback) {
        ((CallRulesApi) this.mApi).getTurnServer().enqueue(new DefaultRetrofit2CallbackWrapper(apiCallback));
    }
}
