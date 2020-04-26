package fr.gouv.tchap.sdk.rest.client;

import fr.gouv.tchap.sdk.rest.api.TchapPasswordPolicyApi;
import fr.gouv.tchap.sdk.rest.model.PasswordPolicy;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback;

public class TchapPasswordPolicyRestClient extends RestClient<TchapPasswordPolicyApi> {
    public TchapPasswordPolicyRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        HomeServerConnectionConfig homeServerConnectionConfig2 = homeServerConnectionConfig;
        super(homeServerConnectionConfig2, TchapPasswordPolicyApi.class, RestClient.URI_API_PREFIX_PATH_UNSTABLE, JsonUtils.getGson(false), false);
    }

    public void getPasswordPolicy(ApiCallback<PasswordPolicy> apiCallback) {
        ((TchapPasswordPolicyApi) this.mApi).passwordPolicy().enqueue(new RestAdapterCallback("getPasswordPolicy", null, apiCallback, null));
    }
}
