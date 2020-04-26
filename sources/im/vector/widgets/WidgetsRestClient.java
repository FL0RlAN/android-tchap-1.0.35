package im.vector.widgets;

import android.content.Context;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.HomeServerConnectionConfigFactoryKt;
import java.util.Map;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback.RequestRetryCallBack;

class WidgetsRestClient extends RestClient<WidgetsApi> {
    public WidgetsRestClient(Context context) {
        super(HomeServerConnectionConfigFactoryKt.createHomeServerConnectionConfig(context.getString(R.string.integrations_rest_url), null), WidgetsApi.class, "api/", JsonUtils.getGson(false));
    }

    public void register(final Map<Object, Object> map, final ApiCallback<Map<String, String>> apiCallback) {
        ((WidgetsApi) this.mApi).register(map).enqueue(new RestAdapterCallback("Register", this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                WidgetsRestClient.this.register(map, apiCallback);
            }
        }));
    }
}
