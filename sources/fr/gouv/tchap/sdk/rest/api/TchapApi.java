package fr.gouv.tchap.sdk.rest.api;

import fr.gouv.tchap.sdk.rest.model.Platform;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TchapApi {
    @GET("info")
    Call<Platform> info(@Query("address") String str, @Query("medium") String str2);
}
