package org.matrix.androidsdk.rest.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.rest.api.GroupsApi;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback.RequestRetryCallBack;
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
import retrofit2.Response;

public class GroupsRestClient extends RestClient<GroupsApi> {
    public GroupsRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        super(homeServerConnectionConfig, GroupsApi.class, RestClient.URI_API_PREFIX_PATH_R0, JsonUtils.getGson(false));
    }

    public void createGroup(final CreateGroupParams createGroupParams, final ApiCallback<String> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("createGroup ");
        sb.append(createGroupParams.localpart);
        String sb2 = sb.toString();
        Call createGroup = ((GroupsApi) this.mApi).createGroup(createGroupParams);
        final ApiCallback<String> apiCallback2 = apiCallback;
        AnonymousClass2 r2 = new RestAdapterCallback<CreateGroupResponse>(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                GroupsRestClient.this.createGroup(createGroupParams, apiCallback);
            }
        }) {
            public void success(CreateGroupResponse createGroupResponse, Response<CreateGroupResponse> response) {
                onEventSent();
                apiCallback2.onSuccess(createGroupResponse.group_id);
            }
        };
        createGroup.enqueue(r2);
    }

    public void inviteUserInGroup(final String str, final String str2, final ApiCallback<String> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("inviteUserInGroup ");
        sb.append(str);
        sb.append(" - ");
        sb.append(str2);
        String sb2 = sb.toString();
        Call inviteUser = ((GroupsApi) this.mApi).inviteUser(str, str2, new GroupInviteUserParams());
        final ApiCallback<String> apiCallback2 = apiCallback;
        AnonymousClass4 r2 = new RestAdapterCallback<GroupInviteUserResponse>(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                GroupsRestClient.this.inviteUserInGroup(str, str2, apiCallback);
            }
        }) {
            public void success(GroupInviteUserResponse groupInviteUserResponse, Response<GroupInviteUserResponse> response) {
                onEventSent();
                apiCallback2.onSuccess(groupInviteUserResponse.state);
            }
        };
        inviteUser.enqueue(r2);
    }

    public void KickUserFromGroup(final String str, final String str2, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("KickFromGroup ");
        sb.append(str);
        sb.append(" ");
        sb.append(str2);
        String sb2 = sb.toString();
        ((GroupsApi) this.mApi).kickUser(str, str2, new GroupKickUserParams()).enqueue(new RestAdapterCallback(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                GroupsRestClient.this.KickUserFromGroup(str, str2, apiCallback);
            }
        }));
    }

    public void addRoomInGroup(final String str, final String str2, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("addRoomInGroup ");
        sb.append(str);
        sb.append(" ");
        sb.append(str2);
        String sb2 = sb.toString();
        ((GroupsApi) this.mApi).addRoom(str, str2, new AddGroupParams()).enqueue(new RestAdapterCallback(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                GroupsRestClient.this.addRoomInGroup(str, str2, apiCallback);
            }
        }));
    }

    public void removeRoomFromGroup(final String str, final String str2, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("removeRoomFromGroup ");
        sb.append(str);
        sb.append(" ");
        sb.append(str2);
        ((GroupsApi) this.mApi).removeRoom(str, str2).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                GroupsRestClient.this.removeRoomFromGroup(str, str2, apiCallback);
            }
        }));
    }

    public void updateGroupProfile(final String str, final GroupProfile groupProfile, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateGroupProfile ");
        sb.append(str);
        ((GroupsApi) this.mApi).updateProfile(str, groupProfile).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                GroupsRestClient.this.updateGroupProfile(str, groupProfile, apiCallback);
            }
        }));
    }

    public void getGroupProfile(final String str, final ApiCallback<GroupProfile> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("getGroupProfile ");
        sb.append(str);
        ((GroupsApi) this.mApi).getProfile(str).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                GroupsRestClient.this.getGroupProfile(str, apiCallback);
            }
        }));
    }

    public void getGroupInvitedUsers(final String str, final ApiCallback<GroupUsers> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("getGroupInvitedUsers ");
        sb.append(str);
        ((GroupsApi) this.mApi).getInvitedUsers(str).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                GroupsRestClient.this.getGroupInvitedUsers(str, apiCallback);
            }
        }));
    }

    public void getGroupRooms(final String str, final ApiCallback<GroupRooms> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("getGroupRooms ");
        sb.append(str);
        ((GroupsApi) this.mApi).getRooms(str).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                GroupsRestClient.this.getGroupRooms(str, apiCallback);
            }
        }));
    }

    public void getGroupUsers(final String str, final ApiCallback<GroupUsers> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("getGroupUsers ");
        sb.append(str);
        ((GroupsApi) this.mApi).getUsers(str).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                GroupsRestClient.this.getGroupUsers(str, apiCallback);
            }
        }));
    }

    public void getGroupSummary(final String str, final ApiCallback<GroupSummary> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("getGroupSummary ");
        sb.append(str);
        ((GroupsApi) this.mApi).getSummary(str).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                GroupsRestClient.this.getGroupSummary(str, apiCallback);
            }
        }));
    }

    public void joinGroup(final String str, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("joinGroup ");
        sb.append(str);
        String sb2 = sb.toString();
        ((GroupsApi) this.mApi).acceptInvitation(str, new AcceptGroupInvitationParams()).enqueue(new RestAdapterCallback(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                GroupsRestClient.this.joinGroup(str, apiCallback);
            }
        }));
    }

    public void leaveGroup(final String str, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("leaveGroup ");
        sb.append(str);
        String sb2 = sb.toString();
        ((GroupsApi) this.mApi).leave(str, new LeaveGroupParams()).enqueue(new RestAdapterCallback(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                GroupsRestClient.this.leaveGroup(str, apiCallback);
            }
        }));
    }

    public void updateGroupPublicity(final String str, final boolean z, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateGroupPublicity ");
        sb.append(str);
        sb.append(" - ");
        sb.append(z);
        String sb2 = sb.toString();
        UpdatePubliciseParams updatePubliciseParams = new UpdatePubliciseParams();
        updatePubliciseParams.publicise = Boolean.valueOf(z);
        ((GroupsApi) this.mApi).updatePublicity(str, updatePubliciseParams).enqueue(new RestAdapterCallback(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                GroupsRestClient.this.updateGroupPublicity(str, z, apiCallback);
            }
        }));
    }

    public void getJoinedGroups(final ApiCallback<List<String>> apiCallback) {
        Call joinedGroupIds = ((GroupsApi) this.mApi).getJoinedGroupIds();
        final ApiCallback<List<String>> apiCallback2 = apiCallback;
        AnonymousClass18 r1 = new RestAdapterCallback<GetGroupsResponse>("getJoinedGroups", this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                GroupsRestClient.this.getJoinedGroups(apiCallback);
            }
        }) {
            public void success(GetGroupsResponse getGroupsResponse, Response<GetGroupsResponse> response) {
                onEventSent();
                apiCallback2.onSuccess(getGroupsResponse.groupIds);
            }
        };
        joinedGroupIds.enqueue(r1);
    }

    public void getUserPublicisedGroups(final String str, final ApiCallback<List<String>> apiCallback) {
        getPublicisedGroups(Arrays.asList(new String[]{str}), new SimpleApiCallback<Map<String, List<String>>>(apiCallback) {
            public void onSuccess(Map<String, List<String>> map) {
                apiCallback.onSuccess(map.get(str));
            }
        });
    }

    public void getPublicisedGroups(final List<String> list, final ApiCallback<Map<String, List<String>>> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("getPublicisedGroups ");
        sb.append(list);
        String sb2 = sb.toString();
        HashMap hashMap = new HashMap();
        hashMap.put("user_ids", list);
        Call publicisedGroups = ((GroupsApi) this.mApi).getPublicisedGroups(hashMap);
        final List<String> list2 = list;
        final ApiCallback<Map<String, List<String>>> apiCallback2 = apiCallback;
        AnonymousClass21 r2 = new RestAdapterCallback<GetPublicisedGroupsResponse>(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                GroupsRestClient.this.getPublicisedGroups(list, apiCallback);
            }
        }) {
            public void success(GetPublicisedGroupsResponse getPublicisedGroupsResponse, Response<GetPublicisedGroupsResponse> response) {
                onEventSent();
                HashMap hashMap = new HashMap();
                for (String str : list2) {
                    List list = null;
                    if (getPublicisedGroupsResponse.users != null && getPublicisedGroupsResponse.users.containsKey(str)) {
                        list = (List) getPublicisedGroupsResponse.users.get(str);
                    }
                    if (list == null) {
                        list = new ArrayList();
                    }
                    hashMap.put(str, list);
                }
                apiCallback2.onSuccess(hashMap);
            }
        };
        publicisedGroups.enqueue(r2);
    }
}
