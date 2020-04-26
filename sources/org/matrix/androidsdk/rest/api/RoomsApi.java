package org.matrix.androidsdk.rest.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.rest.model.ChunkEvents;
import org.matrix.androidsdk.rest.model.CreateRoomParams;
import org.matrix.androidsdk.rest.model.CreateRoomResponse;
import org.matrix.androidsdk.rest.model.CreatedEvent;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.EventContext;
import org.matrix.androidsdk.rest.model.PowerLevels;
import org.matrix.androidsdk.rest.model.ReportContentParams;
import org.matrix.androidsdk.rest.model.RoomAliasDescription;
import org.matrix.androidsdk.rest.model.RoomDirectoryVisibility;
import org.matrix.androidsdk.rest.model.TokensChunkEvents;
import org.matrix.androidsdk.rest.model.Typing;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.UserIdAndReason;
import org.matrix.androidsdk.rest.model.message.Message;
import org.matrix.androidsdk.rest.model.sync.RoomResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RoomsApi {
    @PUT("user/{userId}/rooms/{roomId}/tags/{tag}")
    Call<Void> addTag(@Path("userId") String str, @Path("roomId") String str2, @Path("tag") String str3, @Body Map<String, Object> map);

    @POST("rooms/{roomId}/ban")
    Call<Void> ban(@Path("roomId") String str, @Body UserIdAndReason userIdAndReason);

    @POST("createRoom")
    Call<CreateRoomResponse> createRoom(@Body CreateRoomParams createRoomParams);

    @POST("rooms/{roomId}/forget")
    Call<Void> forget(@Path("roomId") String str, @Body JsonObject jsonObject);

    @GET("rooms/{roomId}/context/{eventId}")
    Call<EventContext> getContextOfEvent(@Path("roomId") String str, @Path("eventId") String str2, @Query("limit") int i, @Query("filter") String str3);

    @GET("rooms/{roomId}/event/{eventId}")
    Call<Event> getEvent(@Path("roomId") String str, @Path("eventId") String str2);

    @GET("rooms/{roomId}/members")
    Call<ChunkEvents> getMembers(@Path("roomId") String str, @Query("at") String str2, @Query("membership") String str3, @Query("not_membership") String str4);

    @GET("directory/list/room/{roomId}")
    Call<RoomDirectoryVisibility> getRoomDirectoryVisibility(@Path("roomId") String str);

    @GET("directory/room/{roomAlias}")
    Call<RoomAliasDescription> getRoomIdByAlias(@Path("roomAlias") String str);

    @GET("rooms/{roomId}/messages")
    Call<TokensChunkEvents> getRoomMessagesFrom(@Path("roomId") String str, @Query("from") String str2, @Query("dir") String str3, @Query("limit") int i, @Query("filter") String str4);

    @GET("rooms/{roomId}/state/{eventType}")
    Call<JsonElement> getStateEvent(@Path("roomId") String str, @Path("eventType") String str2);

    @GET("rooms/{roomId}/state/{eventType}/{stateKey}")
    Call<JsonElement> getStateEvent(@Path("roomId") String str, @Path("eventType") String str2, @Path("stateKey") String str3);

    @GET("rooms/{roomId}/initialSync")
    Call<RoomResponse> initialSync(@Path("roomId") String str, @Query("limit") int i);

    @POST("rooms/{roomId}/invite")
    Call<Void> invite(@Path("roomId") String str, @Body Map<String, String> map);

    @POST("rooms/{roomId}/invite")
    Call<Void> invite(@Path("roomId") String str, @Body User user);

    @POST("join/{roomAliasOrId}")
    Call<RoomResponse> joinRoomByAliasOrId(@Path("roomAliasOrId") String str, @Query("server_name") List<String> list, @Body Map<String, Object> map);

    @POST("rooms/{roomId}/kick")
    Call<Void> kick(@Path("roomId") String str, @Body UserIdAndReason userIdAndReason);

    @POST("rooms/{roomId}/leave")
    Call<Void> leave(@Path("roomId") String str, @Body JsonObject jsonObject);

    @POST("rooms/{roomId}/redact/{eventId}")
    Call<Event> redactEvent(@Path("roomId") String str, @Path("eventId") String str2, @Body JsonObject jsonObject);

    @DELETE("directory/room/{roomAlias}")
    Call<Void> removeRoomAlias(@Path("roomAlias") String str);

    @DELETE("user/{userId}/rooms/{roomId}/tags/{tag}")
    Call<Void> removeTag(@Path("userId") String str, @Path("roomId") String str2, @Path("tag") String str3);

    @POST("rooms/{roomId}/report/{eventId}")
    Call<Void> reportEvent(@Path("roomId") String str, @Path("eventId") String str2, @Body ReportContentParams reportContentParams);

    @PUT("rooms/{roomId}/send/{eventType}/{txId}")
    Call<CreatedEvent> send(@Path("txId") String str, @Path("roomId") String str2, @Path("eventType") String str3, @Body JsonObject jsonObject);

    @PUT("rooms/{roomId}/send/m.room.message/{txId}")
    Call<CreatedEvent> sendMessage(@Path("txId") String str, @Path("roomId") String str2, @Body Message message);

    @POST("rooms/{roomId}/read_markers")
    Call<Void> sendReadMarker(@Path("roomId") String str, @Body Map<String, String> map);

    @POST("rooms/{roomId}/receipt/m.read/{eventId}")
    Call<Void> sendReadReceipt(@Path("roomId") String str, @Path("eventId") String str2, @Body JsonObject jsonObject);

    @PUT("rooms/{roomId}/state/{state_event_type}/{stateKey}")
    Call<Void> sendStateEvent(@Path("roomId") String str, @Path("state_event_type") String str2, @Path("stateKey") String str3, @Body Map<String, Object> map);

    @PUT("rooms/{roomId}/state/{state_event_type}")
    Call<Void> sendStateEvent(@Path("roomId") String str, @Path("state_event_type") String str2, @Body Map<String, Object> map);

    @PUT("rooms/{roomId}/state/m.room.power_levels")
    Call<Void> setPowerLevels(@Path("roomId") String str, @Body PowerLevels powerLevels);

    @PUT("directory/list/room/{roomId}")
    Call<Void> setRoomDirectoryVisibility(@Path("roomId") String str, @Body RoomDirectoryVisibility roomDirectoryVisibility);

    @PUT("directory/room/{roomAlias}")
    Call<Void> setRoomIdByAlias(@Path("roomAlias") String str, @Body RoomAliasDescription roomAliasDescription);

    @PUT("rooms/{roomId}/typing/{userId}")
    Call<Void> setTypingNotification(@Path("roomId") String str, @Path("userId") String str2, @Body Typing typing);

    @POST("rooms/{roomId}/unban")
    Call<Void> unban(@Path("roomId") String str, @Body UserIdAndReason userIdAndReason);

    @PUT("user/{userId}/rooms/{roomId}/account_data/{tag}")
    Call<Void> updateAccountData(@Path("userId") String str, @Path("roomId") String str2, @Path("tag") String str3, @Body Map<String, Object> map);
}
