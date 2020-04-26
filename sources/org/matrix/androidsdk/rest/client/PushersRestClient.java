package org.matrix.androidsdk.rest.client;

import java.util.HashMap;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.UnsentEventsManager;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.data.Pusher;
import org.matrix.androidsdk.rest.api.PushersApi;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback.RequestRetryCallBack;
import org.matrix.androidsdk.rest.model.PushersResponse;
import retrofit2.Call;

public class PushersRestClient extends RestClient<PushersApi> {
    private static final String DATA_KEY_HTTP_URL = "url";
    private static final String LOG_TAG = PushersRestClient.class.getSimpleName();
    private static final String PUSHER_KIND_HTTP = "http";

    public PushersRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        super(homeServerConnectionConfig, PushersApi.class, RestClient.URI_API_PREFIX_PATH_R0, JsonUtils.getGson(true));
    }

    public void addHttpPusher(String str, String str2, String str3, String str4, String str5, String str6, String str7, boolean z, boolean z2, ApiCallback<Void> apiCallback) {
        manageHttpPusher(str, str2, str3, str4, str5, str6, str7, z, z2, true, apiCallback);
    }

    public void removeHttpPusher(String str, String str2, String str3, String str4, String str5, String str6, String str7, ApiCallback<Void> apiCallback) {
        manageHttpPusher(str, str2, str3, str4, str5, str6, str7, false, false, false, apiCallback);
    }

    /* access modifiers changed from: private */
    public void manageHttpPusher(String str, String str2, String str3, String str4, String str5, String str6, String str7, boolean z, boolean z2, boolean z3, ApiCallback<Void> apiCallback) {
        Pusher pusher = new Pusher();
        pusher.pushkey = str;
        pusher.appId = str2;
        pusher.profileTag = str3;
        pusher.lang = str4;
        pusher.kind = z3 ? PUSHER_KIND_HTTP : null;
        pusher.appDisplayName = str5;
        pusher.deviceDisplayName = str6;
        pusher.data = new HashMap();
        pusher.data.put("url", str7);
        if (z3) {
            pusher.append = Boolean.valueOf(z);
        }
        if (z2) {
            pusher.data.put("format", "event_id_only");
        }
        Call call = ((PushersApi) this.mApi).set(pusher);
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final String str8 = str;
        final String str9 = str2;
        final String str10 = str3;
        final String str11 = str4;
        final String str12 = str5;
        final String str13 = str6;
        final String str14 = str7;
        final boolean z4 = z;
        final boolean z5 = z2;
        AnonymousClass1 r13 = r0;
        final boolean z6 = z3;
        Call call2 = call;
        UnsentEventsManager unsentEventsManager2 = unsentEventsManager;
        final ApiCallback<Void> apiCallback2 = apiCallback;
        AnonymousClass1 r0 = new RequestRetryCallBack() {
            public void onRetry() {
                PushersRestClient.this.manageHttpPusher(str8, str9, str10, str11, str12, str13, str14, z4, z5, z6, apiCallback2);
            }
        };
        call2.enqueue(new RestAdapterCallback("manageHttpPusher", unsentEventsManager2, apiCallback, r13));
    }

    public void getPushers(final ApiCallback<PushersResponse> apiCallback) {
        ((PushersApi) this.mApi).get().enqueue(new RestAdapterCallback("getPushers", this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                PushersRestClient.this.getPushers(apiCallback);
            }
        }));
    }
}
