package fr.gouv.tchap.sdk.rest.client;

import fr.gouv.tchap.sdk.rest.api.TchapThirdPidApi;
import fr.gouv.tchap.sdk.rest.model.TchapBulkLookupParams;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.HttpError;
import org.matrix.androidsdk.core.model.HttpException;
import org.matrix.androidsdk.core.rest.DefaultRetrofit2ResponseHandler;
import org.matrix.androidsdk.core.rest.DefaultRetrofit2ResponseHandler.Listener;
import org.matrix.androidsdk.rest.model.BulkLookupResponse;
import org.matrix.androidsdk.rest.model.pid.PidResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TchapThirdPidRestClient extends RestClient<TchapThirdPidApi> {
    public TchapThirdPidRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        HomeServerConnectionConfig homeServerConnectionConfig2 = homeServerConnectionConfig;
        super(homeServerConnectionConfig2, TchapThirdPidApi.class, RestClient.URI_API_PREFIX_PATH_UNSTABLE, JsonUtils.getGson(false), false);
    }

    public void lookup(String str, String str2, final ApiCallback<String> apiCallback) {
        ((TchapThirdPidApi) this.mApi).lookup(str, str2, this.mHsConfig.getIdentityServerUri().getHost()).enqueue(new Callback<PidResponse>() {
            public void onResponse(Call<PidResponse> call, Response<PidResponse> response) {
                try {
                    TchapThirdPidRestClient.this.handleLookupResponse(response, apiCallback);
                } catch (IOException e) {
                    onFailure(call, e);
                }
            }

            public void onFailure(Call<PidResponse> call, Throwable th) {
                apiCallback.onUnexpectedError((Exception) th);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleLookupResponse(Response<PidResponse> response, final ApiCallback<String> apiCallback) throws IOException {
        DefaultRetrofit2ResponseHandler.handleResponse(response, new Listener<PidResponse>() {
            public void onSuccess(Response<PidResponse> response) {
                PidResponse pidResponse = (PidResponse) response.body();
                apiCallback.onSuccess(pidResponse.mxid == null ? "" : pidResponse.mxid);
            }

            public void onHttpError(HttpError httpError) {
                apiCallback.onNetworkError(new HttpException(httpError));
            }
        });
    }

    public void bulkLookup(final List<String> list, List<String> list2, final ApiCallback<List<String>> apiCallback) {
        if (list == null || list2 == null || list.size() != list2.size()) {
            apiCallback.onUnexpectedError(new Exception("invalid params"));
        } else if (list2.size() == 0) {
            apiCallback.onSuccess(new ArrayList());
        } else {
            TchapBulkLookupParams tchapBulkLookupParams = new TchapBulkLookupParams();
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                arrayList.add(Arrays.asList(new String[]{(String) list2.get(i), (String) list.get(i)}));
            }
            tchapBulkLookupParams.threepids = arrayList;
            tchapBulkLookupParams.idServer = this.mHsConfig.getIdentityServerUri().getHost();
            ((TchapThirdPidApi) this.mApi).bulkLookup(tchapBulkLookupParams).enqueue(new Callback<BulkLookupResponse>() {
                public void onResponse(Call<BulkLookupResponse> call, Response<BulkLookupResponse> response) {
                    try {
                        TchapThirdPidRestClient.this.handleBulkLookupResponse(response, list, apiCallback);
                    } catch (IOException e) {
                        apiCallback.onUnexpectedError(e);
                    }
                }

                public void onFailure(Call<BulkLookupResponse> call, Throwable th) {
                    apiCallback.onUnexpectedError((Exception) th);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handleBulkLookupResponse(Response<BulkLookupResponse> response, final List<String> list, final ApiCallback<List<String>> apiCallback) throws IOException {
        DefaultRetrofit2ResponseHandler.handleResponse(response, new Listener<BulkLookupResponse>() {
            public void onSuccess(Response<BulkLookupResponse> response) {
                TchapThirdPidRestClient.this.handleBulkLookupSuccess(response, list, apiCallback);
            }

            public void onHttpError(HttpError httpError) {
                apiCallback.onNetworkError(new HttpException(httpError));
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleBulkLookupSuccess(Response<BulkLookupResponse> response, List<String> list, ApiCallback<List<String>> apiCallback) {
        BulkLookupResponse bulkLookupResponse = (BulkLookupResponse) response.body();
        HashMap hashMap = new HashMap();
        if (bulkLookupResponse.threepids != null) {
            for (int i = 0; i < bulkLookupResponse.threepids.size(); i++) {
                List list2 = (List) bulkLookupResponse.threepids.get(i);
                hashMap.put(list2.get(1), list2.get(2));
            }
        }
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            if (hashMap.containsKey(str)) {
                arrayList.add(hashMap.get(str));
            } else {
                arrayList.add("");
            }
        }
        apiCallback.onSuccess(arrayList);
    }
}
