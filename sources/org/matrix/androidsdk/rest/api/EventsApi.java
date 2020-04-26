package org.matrix.androidsdk.rest.api;

import java.util.Map;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.pid.ThirdPartyProtocol;
import org.matrix.androidsdk.rest.model.publicroom.PublicRoomsParams;
import org.matrix.androidsdk.rest.model.publicroom.PublicRoomsResponse;
import org.matrix.androidsdk.rest.model.search.SearchParams;
import org.matrix.androidsdk.rest.model.search.SearchResponse;
import org.matrix.androidsdk.rest.model.search.SearchUsersParams;
import org.matrix.androidsdk.rest.model.search.SearchUsersRequestResponse;
import org.matrix.androidsdk.rest.model.sync.SyncResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface EventsApi {
    @GET("_matrix/client/r0/events/{eventId}")
    Call<Event> getEvent(@Path("eventId") String str);

    @GET("_matrix/media/r0/preview_url")
    Call<Map<String, Object>> getURLPreview(@Query("url") String str, @Query("ts") long j);

    @POST("_matrix/client/r0/publicRooms")
    Call<PublicRoomsResponse> publicRooms(@Query("server") String str, @Body PublicRoomsParams publicRoomsParams);

    @POST("_matrix/client/r0/search")
    Call<SearchResponse> searchEvents(@Body SearchParams searchParams, @Query("next_batch") String str);

    @POST("_matrix/client/r0/user_directory/search")
    Call<SearchUsersRequestResponse> searchUsers(@Body SearchUsersParams searchUsersParams);

    @GET("_matrix/client/r0/sync")
    Call<SyncResponse> sync(@QueryMap Map<String, Object> map);

    @GET("_matrix/client/unstable/thirdparty/protocols")
    Call<Map<String, ThirdPartyProtocol>> thirdPartyProtocols();
}
