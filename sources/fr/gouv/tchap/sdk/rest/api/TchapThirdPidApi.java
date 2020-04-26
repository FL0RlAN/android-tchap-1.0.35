package fr.gouv.tchap.sdk.rest.api;

import fr.gouv.tchap.sdk.rest.model.TchapBulkLookupParams;
import org.matrix.androidsdk.rest.model.BulkLookupResponse;
import org.matrix.androidsdk.rest.model.pid.PidResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TchapThirdPidApi {
    @POST("account/3pid/bulk_lookup")
    Call<BulkLookupResponse> bulkLookup(@Body TchapBulkLookupParams tchapBulkLookupParams);

    @GET("account/3pid/lookup")
    Call<PidResponse> lookup(@Query("address") String str, @Query("medium") String str2, @Query("id_server") String str3);
}
