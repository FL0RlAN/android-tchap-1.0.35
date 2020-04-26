package org.matrix.androidsdk.rest.api;

import org.matrix.androidsdk.rest.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PresenceApi {
    @GET("presence/{userId}/status")
    Call<User> presenceStatus(@Path("userId") String str);
}
