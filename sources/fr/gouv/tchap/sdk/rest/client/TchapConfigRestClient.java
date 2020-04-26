package fr.gouv.tchap.sdk.rest.client;

import android.net.Uri;
import fr.gouv.tchap.sdk.rest.api.TchapConfigApi;
import fr.gouv.tchap.sdk.rest.model.TchapClientConfig;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.HomeServerConnectionConfig.Builder;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u0014\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007¨\u0006\t"}, d2 = {"Lfr/gouv/tchap/sdk/rest/client/TchapConfigRestClient;", "Lorg/matrix/androidsdk/RestClient;", "Lfr/gouv/tchap/sdk/rest/api/TchapConfigApi;", "()V", "getClientConfig", "", "callback", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Lfr/gouv/tchap/sdk/rest/model/TchapClientConfig;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: TchapConfigRestClient.kt */
public final class TchapConfigRestClient extends RestClient<TchapConfigApi> {
    public TchapConfigRestClient() {
        super(new Builder().withHomeServerUri(Uri.parse("https://www.tchap.gouv.fr")).build(), TchapConfigApi.class, "", JsonUtils.getKotlinGson(), false);
    }

    public final void getClientConfig(ApiCallback<TchapClientConfig> apiCallback) {
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        ((TchapConfigApi) this.mApi).getTchapClientConfig("agent").enqueue(new RestAdapterCallback("getClientConfig", null, apiCallback, null));
    }
}
