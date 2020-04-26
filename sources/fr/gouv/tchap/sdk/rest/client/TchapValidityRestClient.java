package fr.gouv.tchap.sdk.rest.client;

import fr.gouv.tchap.sdk.rest.api.TchapValidityApi;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback;

public class TchapValidityRestClient extends RestClient<TchapValidityApi> {
    public TchapValidityRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        HomeServerConnectionConfig homeServerConnectionConfig2 = homeServerConnectionConfig;
        super(homeServerConnectionConfig2, TchapValidityApi.class, RestClient.URI_API_PREFIX_PATH_UNSTABLE, JsonUtils.getGson(false), false);
    }

    public void requestRenewalEmail(ApiCallback<Void> apiCallback) {
        ((TchapValidityApi) this.mApi).requestRenewalEmail().enqueue(new RestAdapterCallback("requestRenewalEmail", null, apiCallback, null));
    }

    public void renewAccountValidity(String str, ApiCallback<Void> apiCallback) {
        ((TchapValidityApi) this.mApi).renewAccountValidity(str).enqueue(new RestAdapterCallback("renewAccountValidity", null, apiCallback, null));
    }
}
