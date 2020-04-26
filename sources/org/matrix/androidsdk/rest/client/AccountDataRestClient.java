package org.matrix.androidsdk.rest.client;

import java.util.HashMap;
import java.util.Map;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.UnsentEventsManager;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.rest.api.AccountDataApi;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback.RequestRetryCallBack;
import retrofit2.Call;

public class AccountDataRestClient extends RestClient<AccountDataApi> {
    public AccountDataRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        super(homeServerConnectionConfig, AccountDataApi.class, RestClient.URI_API_PREFIX_PATH_R0, JsonUtils.getGson(false));
    }

    public void setAccountData(String str, String str2, Object obj, ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("setAccountData userId : ");
        sb.append(str);
        sb.append(" type ");
        sb.append(str2);
        String sb2 = sb.toString();
        Call accountData = ((AccountDataApi) this.mApi).setAccountData(str, str2, obj);
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final String str3 = str;
        final String str4 = str2;
        final Object obj2 = obj;
        final ApiCallback<Void> apiCallback2 = apiCallback;
        AnonymousClass1 r4 = new RequestRetryCallBack() {
            public void onRetry() {
                AccountDataRestClient.this.setAccountData(str3, str4, obj2, apiCallback2);
            }
        };
        accountData.enqueue(new RestAdapterCallback(sb2, unsentEventsManager, apiCallback, r4));
    }

    public void openIdToken(final String str, final ApiCallback<Map<Object, Object>> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("openIdToken userId : ");
        sb.append(str);
        ((AccountDataApi) this.mApi).openIdToken(str, new HashMap()).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                AccountDataRestClient.this.openIdToken(str, apiCallback);
            }
        }));
    }
}
