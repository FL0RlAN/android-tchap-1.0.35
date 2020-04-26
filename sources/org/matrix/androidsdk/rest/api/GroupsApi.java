package org.matrix.androidsdk.rest.api;

import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.rest.model.group.AcceptGroupInvitationParams;
import org.matrix.androidsdk.rest.model.group.AddGroupParams;
import org.matrix.androidsdk.rest.model.group.CreateGroupParams;
import org.matrix.androidsdk.rest.model.group.CreateGroupResponse;
import org.matrix.androidsdk.rest.model.group.GetGroupsResponse;
import org.matrix.androidsdk.rest.model.group.GetPublicisedGroupsResponse;
import org.matrix.androidsdk.rest.model.group.GroupInviteUserParams;
import org.matrix.androidsdk.rest.model.group.GroupInviteUserResponse;
import org.matrix.androidsdk.rest.model.group.GroupKickUserParams;
import org.matrix.androidsdk.rest.model.group.GroupProfile;
import org.matrix.androidsdk.rest.model.group.GroupRooms;
import org.matrix.androidsdk.rest.model.group.GroupSummary;
import org.matrix.androidsdk.rest.model.group.GroupUsers;
import org.matrix.androidsdk.rest.model.group.LeaveGroupParams;
import org.matrix.androidsdk.rest.model.group.UpdatePubliciseParams;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GroupsApi {
    @PUT("groups/{groupId}/self/accept_invite")
    Call<Void> acceptInvitation(@Path("groupId") String str, @Body AcceptGroupInvitationParams acceptGroupInvitationParams);

    @PUT("groups/{groupId}/admin/rooms/{roomId}")
    Call<Void> addRoom(@Path("groupId") String str, @Path("roomId") String str2, @Body AddGroupParams addGroupParams);

    @POST("create_group")
    Call<CreateGroupResponse> createGroup(@Body CreateGroupParams createGroupParams);

    @GET("groups/{groupId}/invited_users")
    Call<GroupUsers> getInvitedUsers(@Path("groupId") String str);

    @GET("joined_groups")
    Call<GetGroupsResponse> getJoinedGroupIds();

    @GET("groups/{groupId}/profile")
    Call<GroupProfile> getProfile(@Path("groupId") String str);

    @POST("publicised_groups")
    Call<GetPublicisedGroupsResponse> getPublicisedGroups(@Body Map<String, List<String>> map);

    @GET("groups/{groupId}/rooms")
    Call<GroupRooms> getRooms(@Path("groupId") String str);

    @GET("groups/{groupId}/summary")
    Call<GroupSummary> getSummary(@Path("groupId") String str);

    @GET("groups/{groupId}/users")
    Call<GroupUsers> getUsers(@Path("groupId") String str);

    @PUT("groups/{groupId}/admin/users/invite/{userId}")
    Call<GroupInviteUserResponse> inviteUser(@Path("groupId") String str, @Path("userId") String str2, @Body GroupInviteUserParams groupInviteUserParams);

    @PUT("groups/{groupId}/users/remove/{userId}")
    Call<Void> kickUser(@Path("groupId") String str, @Path("userId") String str2, @Body GroupKickUserParams groupKickUserParams);

    @PUT("groups/{groupId}/self/leave")
    Call<Void> leave(@Path("groupId") String str, @Body LeaveGroupParams leaveGroupParams);

    @DELETE("groups/{groupId}/admin/rooms/{roomId}")
    Call<Void> removeRoom(@Path("groupId") String str, @Path("roomId") String str2);

    @POST("groups/{groupId}/profile")
    Call<Void> updateProfile(@Path("groupId") String str, @Body GroupProfile groupProfile);

    @PUT("groups/{groupId}/self/update_publicity")
    Call<Void> updatePublicity(@Path("groupId") String str, @Body UpdatePubliciseParams updatePubliciseParams);
}
