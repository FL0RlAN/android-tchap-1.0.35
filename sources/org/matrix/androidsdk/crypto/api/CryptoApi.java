package org.matrix.androidsdk.crypto.api;

import java.util.Map;
import org.matrix.androidsdk.crypto.model.crypto.KeyChangesResponse;
import org.matrix.androidsdk.crypto.model.crypto.KeysClaimResponse;
import org.matrix.androidsdk.crypto.model.crypto.KeysQueryResponse;
import org.matrix.androidsdk.crypto.model.crypto.KeysUploadResponse;
import org.matrix.androidsdk.crypto.model.rest.DeleteDeviceParams;
import org.matrix.androidsdk.crypto.model.rest.DevicesListResponse;
import org.matrix.androidsdk.crypto.rest.model.crypto.SendToDeviceBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CryptoApi {
    @POST("keys/claim")
    Call<KeysClaimResponse> claimOneTimeKeysForUsersDevices(@Body Map<String, Object> map);

    @HTTP(hasBody = true, method = "DELETE", path = "devices/{device_id}")
    Call<Void> deleteDevice(@Path("device_id") String str, @Body DeleteDeviceParams deleteDeviceParams);

    @POST("keys/query")
    Call<KeysQueryResponse> downloadKeysForUsers(@Body Map<String, Object> map);

    @GET("devices")
    Call<DevicesListResponse> getDevices();

    @GET("keys/changes")
    Call<KeyChangesResponse> getKeyChanges(@Query("from") String str, @Query("to") String str2);

    @PUT("sendToDevice/{eventType}/{txnId}")
    Call<Void> sendToDevice(@Path("eventType") String str, @Path("txnId") String str2, @Body SendToDeviceBody sendToDeviceBody);

    @PUT("devices/{device_id}")
    Call<Void> updateDeviceInfo(@Path("device_id") String str, @Body Map<String, String> map);

    @POST("keys/upload/{deviceId}")
    Call<KeysUploadResponse> uploadKeys(@Path("deviceId") String str, @Body Map<String, Object> map);

    @POST("keys/upload")
    Call<KeysUploadResponse> uploadKeys(@Body Map<String, Object> map);
}
