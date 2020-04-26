package org.matrix.androidsdk.rest.api;

import org.matrix.androidsdk.rest.model.filter.FilterBody;
import org.matrix.androidsdk.rest.model.filter.FilterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FilterApi {
    @GET("user/{userId}/filter/{filterId}")
    Call<FilterBody> getFilterById(@Path("userId") String str, @Path("filterId") String str2);

    @POST("user/{userId}/filter")
    Call<FilterResponse> uploadFilter(@Path("userId") String str, @Body FilterBody filterBody);
}
