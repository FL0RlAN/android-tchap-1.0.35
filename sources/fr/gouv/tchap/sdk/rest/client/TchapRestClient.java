package fr.gouv.tchap.sdk.rest.client;

import fr.gouv.tchap.sdk.rest.api.TchapApi;
import fr.gouv.tchap.sdk.rest.model.Platform;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback;

public class TchapRestClient extends RestClient<TchapApi> {
    public TchapRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        HomeServerConnectionConfig homeServerConnectionConfig2 = homeServerConnectionConfig;
        super(homeServerConnectionConfig2, TchapApi.class, RestClient.URI_API_PREFIX_IDENTITY, JsonUtils.getGson(false), true);
    }

    public void info(String str, String str2, ApiCallback<Platform> apiCallback) {
        ((TchapApi) this.mApi).info(str, str2).enqueue(new RestAdapterCallback("platformInfo", null, apiCallback, null));
    }
}
