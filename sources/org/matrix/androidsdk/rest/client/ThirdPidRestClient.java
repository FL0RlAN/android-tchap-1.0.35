package org.matrix.androidsdk.rest.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.HttpError;
import org.matrix.androidsdk.core.model.HttpException;
import org.matrix.androidsdk.core.rest.DefaultRetrofit2ResponseHandler;
import org.matrix.androidsdk.core.rest.DefaultRetrofit2ResponseHandler.Listener;
import org.matrix.androidsdk.rest.api.ThirdPidApi;
import org.matrix.androidsdk.rest.model.BulkLookupParams;
import org.matrix.androidsdk.rest.model.BulkLookupResponse;
import org.matrix.androidsdk.rest.model.pid.PidResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThirdPidRestClient extends RestClient<ThirdPidApi> {
    private static final String KEY_SUBMIT_TOKEN_SUCCESS = "success";

    public ThirdPidRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        HomeServerConnectionConfig homeServerConnectionConfig2 = homeServerConnectionConfig;
        super(homeServerConnectionConfig2, ThirdPidApi.class, RestClient.URI_API_PREFIX_IDENTITY, JsonUtils.getGson(false), true);
    }

    public void lookup3Pid(String str, String str2, final ApiCallback<String> apiCallback) {
        ((ThirdPidApi) this.mApi).lookup3Pid(str, str2).enqueue(new Callback<PidResponse>() {
            public void onResponse(Call<PidResponse> call, Response<PidResponse> response) {
                try {
                    ThirdPidRestClient.this.handleLookup3PidResponse(response, apiCallback);
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
    public void handleLookup3PidResponse(Response<PidResponse> response, final ApiCallback<String> apiCallback) throws IOException {
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

    public void submitValidationToken(String str, String str2, String str3, String str4, final ApiCallback<Boolean> apiCallback) {
        ((ThirdPidApi) this.mApi).requestOwnershipValidation(str, str2, str3, str4).enqueue(new Callback<Map<String, Object>>() {
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                try {
                    ThirdPidRestClient.this.handleSubmitValidationTokenResponse(response, apiCallback);
                } catch (IOException e) {
                    apiCallback.onUnexpectedError(e);
                }
            }

            public void onFailure(Call<Map<String, Object>> call, Throwable th) {
                apiCallback.onUnexpectedError((Exception) th);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleSubmitValidationTokenResponse(Response<Map<String, Object>> response, final ApiCallback<Boolean> apiCallback) throws IOException {
        DefaultRetrofit2ResponseHandler.handleResponse(response, new Listener<Map<String, Object>>() {
            public void onSuccess(Response<Map<String, Object>> response) {
                Map map = (Map) response.body();
                String str = ThirdPidRestClient.KEY_SUBMIT_TOKEN_SUCCESS;
                if (map.containsKey(str)) {
                    apiCallback.onSuccess((Boolean) map.get(str));
                } else {
                    apiCallback.onSuccess(Boolean.valueOf(false));
                }
            }

            public void onHttpError(HttpError httpError) {
                apiCallback.onNetworkError(new HttpException(httpError));
            }
        });
    }

    public void lookup3Pids(final List<String> list, List<String> list2, final ApiCallback<List<String>> apiCallback) {
        if (list == null || list2 == null || list.size() != list2.size()) {
            apiCallback.onUnexpectedError(new Exception("invalid params"));
        } else if (list2.size() == 0) {
            apiCallback.onSuccess(new ArrayList());
        } else {
            BulkLookupParams bulkLookupParams = new BulkLookupParams();
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                arrayList.add(Arrays.asList(new String[]{(String) list2.get(i), (String) list.get(i)}));
            }
            bulkLookupParams.threepids = arrayList;
            ((ThirdPidApi) this.mApi).bulkLookup(bulkLookupParams).enqueue(new Callback<BulkLookupResponse>() {
                public void onResponse(Call<BulkLookupResponse> call, Response<BulkLookupResponse> response) {
                    try {
                        ThirdPidRestClient.this.handleBulkLookupResponse(response, list, apiCallback);
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
                ThirdPidRestClient.this.handleBulkLookupSuccess(response, list, apiCallback);
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
