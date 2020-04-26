package org.matrix.androidsdk.crypto.rest;

import android.text.TextUtils;
import im.vector.activity.VectorUniversalLinkActivity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.StringUtilsKt;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.crypto.api.CryptoApi;
import org.matrix.androidsdk.crypto.data.MXKey;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.crypto.model.crypto.KeyChangesResponse;
import org.matrix.androidsdk.crypto.model.crypto.KeysClaimResponse;
import org.matrix.androidsdk.crypto.model.crypto.KeysQueryResponse;
import org.matrix.androidsdk.crypto.model.crypto.KeysUploadResponse;
import org.matrix.androidsdk.crypto.model.rest.DeleteDeviceParams;
import org.matrix.androidsdk.crypto.model.rest.DevicesListResponse;
import org.matrix.androidsdk.crypto.rest.CryptoRestAdapterCallback.RequestRetryCallBack;
import org.matrix.androidsdk.crypto.rest.model.crypto.SendToDeviceBody;
import retrofit2.Call;
import retrofit2.Response;

public class CryptoRestClient extends RestClient<CryptoApi> {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = CryptoRestClient.class.getSimpleName();

    public CryptoRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        super(homeServerConnectionConfig, CryptoApi.class, RestClient.URI_API_PREFIX_PATH_UNSTABLE);
    }

    public void uploadKeys(Map<String, ?> map, Map<String, ?> map2, String str, ApiCallback<KeysUploadResponse> apiCallback) {
        String convertToUTF8 = StringUtilsKt.convertToUTF8(str);
        HashMap hashMap = new HashMap();
        if (map != null) {
            hashMap.put("device_keys", map);
        }
        if (map2 != null) {
            hashMap.put("one_time_keys", map2);
        }
        String str2 = "uploadKeys";
        if (!TextUtils.isEmpty(convertToUTF8)) {
            Call uploadKeys = ((CryptoApi) this.mApi).uploadKeys(convertToUTF8, hashMap);
            final Map<String, ?> map3 = map;
            final Map<String, ?> map4 = map2;
            final String str3 = str;
            final ApiCallback<KeysUploadResponse> apiCallback2 = apiCallback;
            AnonymousClass1 r4 = new RequestRetryCallBack() {
                public void onRetry() {
                    CryptoRestClient.this.uploadKeys(map3, map4, str3, apiCallback2);
                }
            };
            uploadKeys.enqueue(new CryptoRestAdapterCallback(str2, apiCallback, r4));
            return;
        }
        Call uploadKeys2 = ((CryptoApi) this.mApi).uploadKeys(hashMap);
        final Map<String, ?> map5 = map;
        final Map<String, ?> map6 = map2;
        final String str4 = str;
        final ApiCallback<KeysUploadResponse> apiCallback3 = apiCallback;
        AnonymousClass2 r42 = new RequestRetryCallBack() {
            public void onRetry() {
                CryptoRestClient.this.uploadKeys(map5, map6, str4, apiCallback3);
            }
        };
        uploadKeys2.enqueue(new CryptoRestAdapterCallback(str2, apiCallback, r42));
    }

    public void downloadKeysForUsers(final List<String> list, final String str, final ApiCallback<KeysQueryResponse> apiCallback) {
        HashMap hashMap = new HashMap();
        if (list != null) {
            for (String put : list) {
                hashMap.put(put, new HashMap());
            }
        }
        HashMap hashMap2 = new HashMap();
        hashMap2.put("device_keys", hashMap);
        if (!TextUtils.isEmpty(str)) {
            hashMap2.put(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_TOKEN, str);
        }
        ((CryptoApi) this.mApi).downloadKeysForUsers(hashMap2).enqueue(new CryptoRestAdapterCallback("downloadKeysForUsers", apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                CryptoRestClient.this.downloadKeysForUsers(list, str, apiCallback);
            }
        }));
    }

    public void claimOneTimeKeysForUsersDevices(final MXUsersDevicesMap<String> mXUsersDevicesMap, final ApiCallback<MXUsersDevicesMap<MXKey>> apiCallback) {
        HashMap hashMap = new HashMap();
        hashMap.put("one_time_keys", mXUsersDevicesMap.getMap());
        Call claimOneTimeKeysForUsersDevices = ((CryptoApi) this.mApi).claimOneTimeKeysForUsersDevices(hashMap);
        final ApiCallback<MXUsersDevicesMap<MXKey>> apiCallback2 = apiCallback;
        AnonymousClass5 r1 = new CryptoRestAdapterCallback<KeysClaimResponse>("claimOneTimeKeysForUsersDevices", apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                CryptoRestClient.this.claimOneTimeKeysForUsersDevices(mXUsersDevicesMap, apiCallback);
            }
        }) {
            public void success(KeysClaimResponse keysClaimResponse, Response response) {
                HashMap hashMap = new HashMap();
                if (keysClaimResponse.oneTimeKeys != null) {
                    for (String str : keysClaimResponse.oneTimeKeys.keySet()) {
                        Map map = (Map) keysClaimResponse.oneTimeKeys.get(str);
                        HashMap hashMap2 = new HashMap();
                        for (String str2 : map.keySet()) {
                            try {
                                hashMap2.put(str2, new MXKey((Map) map.get(str2)));
                            } catch (Exception e) {
                                String access$000 = CryptoRestClient.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## claimOneTimeKeysForUsersDevices : fail to create a MXKey ");
                                sb.append(e.getMessage());
                                Log.e(access$000, sb.toString(), e);
                            }
                        }
                        if (hashMap2.size() != 0) {
                            hashMap.put(str, hashMap2);
                        }
                    }
                }
                apiCallback2.onSuccess(new MXUsersDevicesMap(hashMap));
            }
        };
        claimOneTimeKeysForUsersDevices.enqueue(r1);
    }

    public void sendToDevice(String str, MXUsersDevicesMap<Object> mXUsersDevicesMap, ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append(new Random().nextInt(Integer.MAX_VALUE));
        sb.append("");
        sendToDevice(str, mXUsersDevicesMap, sb.toString(), apiCallback);
    }

    public void sendToDevice(final String str, final MXUsersDevicesMap<Object> mXUsersDevicesMap, String str2, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("sendToDevice ");
        sb.append(str);
        String sb2 = sb.toString();
        SendToDeviceBody sendToDeviceBody = new SendToDeviceBody();
        sendToDeviceBody.messages = mXUsersDevicesMap.getMap();
        ((CryptoApi) this.mApi).sendToDevice(str, str2, sendToDeviceBody).enqueue(new CryptoRestAdapterCallback(sb2, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                CryptoRestClient.this.sendToDevice(str, mXUsersDevicesMap, apiCallback);
            }
        }));
    }

    public void getDevices(final ApiCallback<DevicesListResponse> apiCallback) {
        ((CryptoApi) this.mApi).getDevices().enqueue(new CryptoRestAdapterCallback("getDevicesListInfo", apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                CryptoRestClient.this.getDevices(apiCallback);
            }
        }));
    }

    public void deleteDevice(final String str, final DeleteDeviceParams deleteDeviceParams, final ApiCallback<Void> apiCallback) {
        ((CryptoApi) this.mApi).deleteDevice(str, deleteDeviceParams).enqueue(new CryptoRestAdapterCallback("deleteDevice", apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                CryptoRestClient.this.deleteDevice(str, deleteDeviceParams, apiCallback);
            }
        }));
    }

    public void setDeviceName(final String str, final String str2, final ApiCallback<Void> apiCallback) {
        HashMap hashMap = new HashMap();
        hashMap.put("display_name", TextUtils.isEmpty(str2) ? "" : str2);
        ((CryptoApi) this.mApi).updateDeviceInfo(str, hashMap).enqueue(new CryptoRestAdapterCallback("setDeviceName", apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                CryptoRestClient.this.setDeviceName(str, str2, apiCallback);
            }
        }));
    }

    public void getKeyChanges(final String str, final String str2, final ApiCallback<KeyChangesResponse> apiCallback) {
        ((CryptoApi) this.mApi).getKeyChanges(str, str2).enqueue(new CryptoRestAdapterCallback("getKeyChanges", apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                CryptoRestClient.this.getKeyChanges(str, str2, apiCallback);
            }
        }));
    }
}
