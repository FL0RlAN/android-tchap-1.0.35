package org.matrix.androidsdk.rest.api;

import java.util.Map;
import org.matrix.androidsdk.rest.model.BulkLookupParams;
import org.matrix.androidsdk.rest.model.BulkLookupResponse;
import org.matrix.androidsdk.rest.model.pid.PidResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ThirdPidApi {
    @POST("bulk_lookup")
    Call<BulkLookupResponse> bulkLookup(@Body BulkLookupParams bulkLookupParams);

    @GET("lookup")
    Call<PidResponse> lookup3Pid(@Query("address") String str, @Query("medium") String str2);

    @POST("validate/{medium}/submitToken")
    Call<Map<String, Object>> requestOwnershipValidation(@Path("medium") String str, @Query("token") String str2, @Query("client_secret") String str3, @Query("sid") String str4);
}
