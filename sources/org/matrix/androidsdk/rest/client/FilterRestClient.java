package org.matrix.androidsdk.rest.client;

import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.rest.api.FilterApi;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback.RequestRetryCallBack;
import org.matrix.androidsdk.rest.model.filter.FilterBody;
import org.matrix.androidsdk.rest.model.filter.FilterResponse;

public class FilterRestClient extends RestClient<FilterApi> {
    public FilterRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        super(homeServerConnectionConfig, FilterApi.class, RestClient.URI_API_PREFIX_PATH_R0, JsonUtils.getGson(false));
    }

    public void uploadFilter(final String str, final FilterBody filterBody, final ApiCallback<FilterResponse> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("uploadFilter userId : ");
        sb.append(str);
        sb.append(" filter : ");
        sb.append(filterBody);
        ((FilterApi) this.mApi).uploadFilter(str, filterBody).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                FilterRestClient.this.uploadFilter(str, filterBody, apiCallback);
            }
        }));
    }

    public void getFilter(final String str, final String str2, final ApiCallback<FilterBody> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("getFilter userId : ");
        sb.append(str);
        sb.append(" filterId : ");
        sb.append(str2);
        ((FilterApi) this.mApi).getFilterById(str, str2).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                FilterRestClient.this.getFilter(str, str2, apiCallback);
            }
        }));
    }
}
