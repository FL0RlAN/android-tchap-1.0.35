package org.matrix.androidsdk.rest.client;

import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.rest.api.PresenceApi;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback.RequestRetryCallBack;
import org.matrix.androidsdk.rest.model.User;

public class PresenceRestClient extends RestClient<PresenceApi> {
    public PresenceRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        super(homeServerConnectionConfig, PresenceApi.class, RestClient.URI_API_PREFIX_PATH_R0, JsonUtils.getGson(false));
    }

    public void getPresence(final String str, final ApiCallback<User> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("getPresence userId : ");
        sb.append(str);
        ((PresenceApi) this.mApi).presenceStatus(str).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                PresenceRestClient.this.getPresence(str, apiCallback);
            }
        }));
    }
}
