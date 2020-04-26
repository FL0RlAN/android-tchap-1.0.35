package org.matrix.androidsdk.rest.client;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.UnsentEventsManager;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.api.EventsApi;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback.RequestRetryCallBack;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.URLPreview;
import org.matrix.androidsdk.rest.model.pid.ThirdPartyProtocol;
import org.matrix.androidsdk.rest.model.publicroom.PublicRoomsFilter;
import org.matrix.androidsdk.rest.model.publicroom.PublicRoomsParams;
import org.matrix.androidsdk.rest.model.publicroom.PublicRoomsResponse;
import org.matrix.androidsdk.rest.model.search.SearchParams;
import org.matrix.androidsdk.rest.model.search.SearchResponse;
import org.matrix.androidsdk.rest.model.search.SearchRoomEventCategoryParams;
import org.matrix.androidsdk.rest.model.search.SearchUsersParams;
import org.matrix.androidsdk.rest.model.search.SearchUsersRequestResponse;
import org.matrix.androidsdk.rest.model.search.SearchUsersRequestResponse.User;
import org.matrix.androidsdk.rest.model.search.SearchUsersResponse;
import org.matrix.androidsdk.rest.model.sync.SyncResponse;
import retrofit2.Call;

public class EventsRestClient extends RestClient<EventsApi> {
    private static final int EVENT_STREAM_TIMEOUT_MS = 30000;
    /* access modifiers changed from: private */
    public String mSearchEventsMediaNameIdentifier = null;
    /* access modifiers changed from: private */
    public String mSearchEventsPatternIdentifier = null;
    /* access modifiers changed from: private */
    public String mSearchUsersPatternIdentifier = null;

    public EventsRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        super(homeServerConnectionConfig, EventsApi.class, "", JsonUtils.getGson(false));
    }

    public void getThirdPartyServerProtocols(final ApiCallback<Map<String, ThirdPartyProtocol>> apiCallback) {
        ((EventsApi) this.mApi).thirdPartyProtocols().enqueue(new RestAdapterCallback("getThirdPartyServerProtocols", this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                EventsRestClient.this.getThirdPartyServerProtocols(apiCallback);
            }
        }));
    }

    public void getPublicRoomsCount(ApiCallback<Integer> apiCallback) {
        getPublicRoomsCount(null, null, false, apiCallback);
    }

    public void getPublicRoomsCount(String str, ApiCallback<Integer> apiCallback) {
        getPublicRoomsCount(str, null, false, apiCallback);
    }

    public void getPublicRoomsCount(String str, String str2, boolean z, final ApiCallback<Integer> apiCallback) {
        loadPublicRooms(str, str2, z, null, null, 0, new SimpleApiCallback<PublicRoomsResponse>(apiCallback) {
            public void onSuccess(PublicRoomsResponse publicRoomsResponse) {
                apiCallback.onSuccess(publicRoomsResponse.total_room_count_estimate);
            }
        });
    }

    public void loadPublicRooms(String str, String str2, boolean z, String str3, String str4, int i, ApiCallback<PublicRoomsResponse> apiCallback) {
        PublicRoomsParams publicRoomsParams = new PublicRoomsParams();
        publicRoomsParams.thirdPartyInstanceId = str2;
        publicRoomsParams.includeAllNetworks = z;
        publicRoomsParams.limit = Integer.valueOf(Math.max(0, i));
        publicRoomsParams.since = str4;
        if (!TextUtils.isEmpty(str3)) {
            publicRoomsParams.filter = new PublicRoomsFilter();
            publicRoomsParams.filter.generic_search_term = str3;
        } else {
            String str5 = str3;
        }
        final String str6 = str;
        Call publicRooms = ((EventsApi) this.mApi).publicRooms(str, publicRoomsParams);
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final String str7 = str2;
        final boolean z2 = z;
        final String str8 = str3;
        final String str9 = str4;
        final int i2 = i;
        final ApiCallback<PublicRoomsResponse> apiCallback2 = apiCallback;
        AnonymousClass3 r0 = new RequestRetryCallBack() {
            public void onRetry() {
                EventsRestClient.this.loadPublicRooms(str6, str7, z2, str8, str9, i2, apiCallback2);
            }
        };
        publicRooms.enqueue(new RestAdapterCallback("loadPublicRooms", unsentEventsManager, apiCallback, r0));
    }

    public void syncFromToken(String str, int i, int i2, String str2, String str3, ApiCallback<SyncResponse> apiCallback) {
        String str4 = str;
        HashMap hashMap = new HashMap();
        if (!TextUtils.isEmpty(str)) {
            hashMap.put("since", str);
        }
        int i3 = i;
        int i4 = -1 != i3 ? i3 : 30;
        if (!TextUtils.isEmpty(str2)) {
            hashMap.put("set_presence", str2);
        } else {
            String str5 = str2;
        }
        if (!TextUtils.isEmpty(str3)) {
            hashMap.put("filter", str3);
        } else {
            String str6 = str3;
        }
        hashMap.put("timeout", Integer.valueOf(i4));
        setConnectionTimeout((str4 == null ? 2 : 1) * EVENT_STREAM_TIMEOUT_MS);
        Call sync = ((EventsApi) this.mApi).sync(hashMap);
        final String str7 = str;
        final int i5 = i;
        final int i6 = i2;
        final String str8 = str2;
        final String str9 = str3;
        final ApiCallback<SyncResponse> apiCallback2 = apiCallback;
        AnonymousClass4 r0 = new RequestRetryCallBack() {
            public void onRetry() {
                EventsRestClient.this.syncFromToken(str7, i5, i6, str8, str9, apiCallback2);
            }
        };
        RestAdapterCallback restAdapterCallback = new RestAdapterCallback("syncFromToken", null, false, apiCallback, r0);
        sync.enqueue(restAdapterCallback);
    }

    public void getEventFromEventId(final String str, final ApiCallback<Event> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("getEventFromEventId : eventId ");
        sb.append(str);
        ((EventsApi) this.mApi).getEvent(str).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                EventsRestClient.this.getEventFromEventId(str, apiCallback);
            }
        }));
    }

    public void searchMessagesByText(final String str, List<String> list, int i, int i2, String str2, ApiCallback<SearchResponse> apiCallback) {
        String str3 = str;
        List<String> list2 = list;
        SearchParams searchParams = new SearchParams();
        SearchRoomEventCategoryParams searchRoomEventCategoryParams = new SearchRoomEventCategoryParams();
        searchRoomEventCategoryParams.search_term = str3;
        searchRoomEventCategoryParams.order_by = "recent";
        searchRoomEventCategoryParams.event_context = new HashMap();
        searchRoomEventCategoryParams.event_context.put("before_limit", Integer.valueOf(i));
        searchRoomEventCategoryParams.event_context.put("after_limit", Integer.valueOf(i2));
        searchRoomEventCategoryParams.event_context.put("include_profile", Boolean.valueOf(true));
        if (list2 != null) {
            searchRoomEventCategoryParams.filter = new HashMap();
            searchRoomEventCategoryParams.filter.put("rooms", list2);
        }
        searchParams.search_categories = new HashMap();
        searchParams.search_categories.put("room_events", searchRoomEventCategoryParams);
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        sb.append("");
        final String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append(str);
        this.mSearchEventsPatternIdentifier = sb3.toString();
        final String str4 = str2;
        Call searchEvents = ((EventsApi) this.mApi).searchEvents(searchParams, str4);
        final ApiCallback<SearchResponse> apiCallback2 = apiCallback;
        AnonymousClass6 r12 = new ApiCallback<SearchResponse>() {
            private boolean isActiveRequest() {
                String access$000 = EventsRestClient.this.mSearchEventsPatternIdentifier;
                StringBuilder sb = new StringBuilder();
                sb.append(sb2);
                sb.append(str);
                return TextUtils.equals(access$000, sb.toString());
            }

            public void onSuccess(SearchResponse searchResponse) {
                if (isActiveRequest()) {
                    ApiCallback apiCallback = apiCallback2;
                    if (apiCallback != null) {
                        apiCallback.onSuccess(searchResponse);
                    }
                    EventsRestClient.this.mSearchEventsPatternIdentifier = null;
                }
            }

            public void onNetworkError(Exception exc) {
                if (isActiveRequest()) {
                    ApiCallback apiCallback = apiCallback2;
                    if (apiCallback != null) {
                        apiCallback.onNetworkError(exc);
                    }
                    EventsRestClient.this.mSearchEventsPatternIdentifier = null;
                }
            }

            public void onMatrixError(MatrixError matrixError) {
                if (isActiveRequest()) {
                    ApiCallback apiCallback = apiCallback2;
                    if (apiCallback != null) {
                        apiCallback.onMatrixError(matrixError);
                    }
                    EventsRestClient.this.mSearchEventsPatternIdentifier = null;
                }
            }

            public void onUnexpectedError(Exception exc) {
                if (isActiveRequest()) {
                    ApiCallback apiCallback = apiCallback2;
                    if (apiCallback != null) {
                        apiCallback.onUnexpectedError(exc);
                    }
                    EventsRestClient.this.mSearchEventsPatternIdentifier = null;
                }
            }
        };
        final String str5 = str;
        final List<String> list3 = list;
        final int i3 = i;
        final int i4 = i2;
        AnonymousClass7 r0 = new RequestRetryCallBack() {
            public void onRetry() {
                EventsRestClient.this.searchMessagesByText(str5, list3, i3, i4, str4, apiCallback2);
            }
        };
        searchEvents.enqueue(new RestAdapterCallback("searchMessageText", null, r12, r0));
    }

    public void searchMediaByText(final String str, List<String> list, int i, int i2, String str2, ApiCallback<SearchResponse> apiCallback) {
        String str3 = str;
        List<String> list2 = list;
        SearchParams searchParams = new SearchParams();
        SearchRoomEventCategoryParams searchRoomEventCategoryParams = new SearchRoomEventCategoryParams();
        searchRoomEventCategoryParams.search_term = str3;
        searchRoomEventCategoryParams.order_by = "recent";
        searchRoomEventCategoryParams.event_context = new HashMap();
        searchRoomEventCategoryParams.event_context.put("before_limit", Integer.valueOf(i));
        searchRoomEventCategoryParams.event_context.put("after_limit", Integer.valueOf(i2));
        Map<String, Object> map = searchRoomEventCategoryParams.event_context;
        Boolean valueOf = Boolean.valueOf(true);
        map.put("include_profile", valueOf);
        searchRoomEventCategoryParams.filter = new HashMap();
        if (list2 != null) {
            searchRoomEventCategoryParams.filter.put("rooms", list2);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(Event.EVENT_TYPE_MESSAGE);
        searchRoomEventCategoryParams.filter.put("types", arrayList);
        searchRoomEventCategoryParams.filter.put("contains_url", valueOf);
        searchParams.search_categories = new HashMap();
        searchParams.search_categories.put("room_events", searchRoomEventCategoryParams);
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        sb.append("");
        final String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append(str);
        this.mSearchEventsMediaNameIdentifier = sb3.toString();
        final String str4 = str2;
        Call searchEvents = ((EventsApi) this.mApi).searchEvents(searchParams, str4);
        final ApiCallback<SearchResponse> apiCallback2 = apiCallback;
        AnonymousClass8 r12 = new ApiCallback<SearchResponse>() {
            private boolean isActiveRequest() {
                String access$100 = EventsRestClient.this.mSearchEventsMediaNameIdentifier;
                StringBuilder sb = new StringBuilder();
                sb.append(sb2);
                sb.append(str);
                return TextUtils.equals(access$100, sb.toString());
            }

            public void onSuccess(SearchResponse searchResponse) {
                if (isActiveRequest()) {
                    apiCallback2.onSuccess(searchResponse);
                    EventsRestClient.this.mSearchEventsMediaNameIdentifier = null;
                }
            }

            public void onNetworkError(Exception exc) {
                if (isActiveRequest()) {
                    apiCallback2.onNetworkError(exc);
                    EventsRestClient.this.mSearchEventsMediaNameIdentifier = null;
                }
            }

            public void onMatrixError(MatrixError matrixError) {
                if (isActiveRequest()) {
                    apiCallback2.onMatrixError(matrixError);
                    EventsRestClient.this.mSearchEventsMediaNameIdentifier = null;
                }
            }

            public void onUnexpectedError(Exception exc) {
                if (isActiveRequest()) {
                    apiCallback2.onUnexpectedError(exc);
                    EventsRestClient.this.mSearchEventsMediaNameIdentifier = null;
                }
            }
        };
        final String str5 = str;
        final List<String> list3 = list;
        final int i3 = i;
        final int i4 = i2;
        AnonymousClass9 r0 = new RequestRetryCallBack() {
            public void onRetry() {
                EventsRestClient.this.searchMediaByText(str5, list3, i3, i4, str4, apiCallback2);
            }
        };
        searchEvents.enqueue(new RestAdapterCallback("searchMediaByText", null, r12, r0));
    }

    public void searchUsers(String str, Integer num, final Set<String> set, final ApiCallback<SearchUsersResponse> apiCallback) {
        SearchUsersParams searchUsersParams = new SearchUsersParams();
        searchUsersParams.search_term = str;
        searchUsersParams.limit = Integer.valueOf(num.intValue() + (set != null ? set.size() : 0));
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        String str2 = " ";
        sb.append(str2);
        sb.append(str);
        sb.append(str2);
        sb.append(num);
        final String sb2 = sb.toString();
        this.mSearchUsersPatternIdentifier = sb2;
        Call searchUsers = ((EventsApi) this.mApi).searchUsers(searchUsersParams);
        AnonymousClass10 r4 = new ApiCallback<SearchUsersRequestResponse>() {
            private boolean isActiveRequest() {
                return TextUtils.equals(EventsRestClient.this.mSearchUsersPatternIdentifier, sb2);
            }

            public void onSuccess(SearchUsersRequestResponse searchUsersRequestResponse) {
                if (isActiveRequest()) {
                    SearchUsersResponse searchUsersResponse = new SearchUsersResponse();
                    searchUsersResponse.limited = searchUsersRequestResponse.limited;
                    searchUsersResponse.results = new ArrayList();
                    Set set = set;
                    if (set == null) {
                        set = new HashSet();
                    }
                    if (searchUsersRequestResponse.results != null) {
                        for (User user : searchUsersRequestResponse.results) {
                            if (user.user_id != null && !set.contains(user.user_id)) {
                                org.matrix.androidsdk.rest.model.User user2 = new org.matrix.androidsdk.rest.model.User();
                                user2.user_id = user.user_id;
                                user2.avatar_url = user.avatar_url;
                                user2.displayname = user.display_name;
                                searchUsersResponse.results.add(user2);
                            }
                        }
                    }
                    apiCallback.onSuccess(searchUsersResponse);
                    EventsRestClient.this.mSearchUsersPatternIdentifier = null;
                }
            }

            public void onNetworkError(Exception exc) {
                if (isActiveRequest()) {
                    apiCallback.onNetworkError(exc);
                    EventsRestClient.this.mSearchUsersPatternIdentifier = null;
                }
            }

            public void onMatrixError(MatrixError matrixError) {
                if (isActiveRequest()) {
                    apiCallback.onMatrixError(matrixError);
                    EventsRestClient.this.mSearchUsersPatternIdentifier = null;
                }
            }

            public void onUnexpectedError(Exception exc) {
                if (isActiveRequest()) {
                    apiCallback.onUnexpectedError(exc);
                    EventsRestClient.this.mSearchUsersPatternIdentifier = null;
                }
            }
        };
        final String str3 = str;
        final Integer num2 = num;
        final Set<String> set2 = set;
        final ApiCallback<SearchUsersResponse> apiCallback2 = apiCallback;
        AnonymousClass11 r5 = new RequestRetryCallBack() {
            public void onRetry() {
                EventsRestClient.this.searchUsers(str3, num2, set2, apiCallback2);
            }
        };
        searchUsers.enqueue(new RestAdapterCallback("searchUsers", null, r4, r5));
    }

    public void cancelSearchMediaByText() {
        this.mSearchEventsMediaNameIdentifier = null;
    }

    public void cancelSearchMessagesByText() {
        this.mSearchEventsPatternIdentifier = null;
    }

    public void cancelUsersSearch() {
        this.mSearchUsersPatternIdentifier = null;
    }

    public void getURLPreview(String str, long j, ApiCallback<URLPreview> apiCallback) {
        final String str2 = str;
        final long j2 = j;
        StringBuilder sb = new StringBuilder();
        sb.append("getURLPreview : URL ");
        sb.append(str2);
        sb.append(" with ts ");
        sb.append(j2);
        String sb2 = sb.toString();
        Call uRLPreview = ((EventsApi) this.mApi).getURLPreview(str2, j2);
        final ApiCallback<URLPreview> apiCallback2 = apiCallback;
        AnonymousClass12 r11 = new SimpleApiCallback<Map<String, Object>>(apiCallback2) {
            public void onSuccess(Map<String, Object> map) {
                ApiCallback apiCallback = apiCallback2;
                if (apiCallback != null) {
                    apiCallback.onSuccess(new URLPreview(map, str2));
                }
            }
        };
        AnonymousClass13 r0 = new RequestRetryCallBack() {
            public void onRetry() {
                EventsRestClient.this.getURLPreview(str2, j2, apiCallback2);
            }
        };
        RestAdapterCallback restAdapterCallback = new RestAdapterCallback(sb2, null, false, r11, r0);
        uRLPreview.enqueue(restAdapterCallback);
    }
}
