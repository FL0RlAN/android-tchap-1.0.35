package org.matrix.androidsdk.rest.api;

import org.matrix.androidsdk.data.Pusher;
import org.matrix.androidsdk.rest.model.PushersResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PushersApi {
    @GET("pushers")
    Call<PushersResponse> get();

    @POST("pushers/set")
    Call<Void> set(@Body Pusher pusher);
}
