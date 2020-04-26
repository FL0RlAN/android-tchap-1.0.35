package org.matrix.androidsdk.rest.client;

import android.text.TextUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.vector.util.UrlUtilKt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.UnsentEventsManager;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.timeline.EventTimeline.Direction;
import org.matrix.androidsdk.rest.api.RoomsApi;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback.RequestRetryCallBack;
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
import org.matrix.androidsdk.rest.model.filter.RoomEventFilter;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;
import org.matrix.androidsdk.rest.model.message.Message;
import org.matrix.androidsdk.rest.model.sync.AccountDataElement;
import org.matrix.androidsdk.rest.model.sync.RoomResponse;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;
import retrofit2.Call;

public class RoomsRestClient extends RestClient<RoomsApi> {
    public static final int DEFAULT_MESSAGES_PAGINATION_LIMIT = 30;
    private static final String LOG_TAG = RoomsRestClient.class.getSimpleName();
    private static final String READ_MARKER_FULLY_READ = "m.fully_read";
    private static final String READ_MARKER_READ = "m.read";

    public RoomsRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        super(homeServerConnectionConfig, RoomsApi.class, RestClient.URI_API_PREFIX_PATH_R0, JsonUtils.getGson(false));
    }

    public void sendMessage(String str, String str2, Message message, ApiCallback<CreatedEvent> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("SendMessage : roomId ");
        sb.append(str2);
        String sb2 = sb.toString();
        Call sendMessage = ((RoomsApi) this.mApi).sendMessage(str, str2, message);
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final String str3 = str;
        final String str4 = str2;
        final Message message2 = message;
        final ApiCallback<CreatedEvent> apiCallback2 = apiCallback;
        AnonymousClass1 r4 = new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.sendMessage(str3, str4, message2, apiCallback2);
            }
        };
        sendMessage.enqueue(new RestAdapterCallback(sb2, unsentEventsManager, apiCallback, r4));
    }

    public void sendEventToRoom(String str, String str2, String str3, JsonObject jsonObject, ApiCallback<CreatedEvent> apiCallback) {
        String str4 = str;
        String str5 = str2;
        String str6 = str3;
        JsonObject jsonObject2 = jsonObject;
        ApiCallback<CreatedEvent> apiCallback2 = apiCallback;
        StringBuilder sb = new StringBuilder();
        sb.append("sendEvent : roomId ");
        sb.append(str5);
        sb.append(" - eventType ");
        sb.append(str6);
        String sb2 = sb.toString();
        if (!TextUtils.equals(str6, Event.EVENT_TYPE_CALL_INVITE)) {
            Call send = ((RoomsApi) this.mApi).send(str, str5, str6, jsonObject2);
            UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
            final String str7 = str;
            final String str8 = str2;
            final String str9 = str3;
            final JsonObject jsonObject3 = jsonObject;
            final ApiCallback<CreatedEvent> apiCallback3 = apiCallback;
            AnonymousClass2 r0 = new RequestRetryCallBack() {
                public void onRetry() {
                    RoomsRestClient.this.sendEventToRoom(str7, str8, str9, jsonObject3, apiCallback3);
                }
            };
            send.enqueue(new RestAdapterCallback(sb2, unsentEventsManager, apiCallback2, r0));
            return;
        }
        ((RoomsApi) this.mApi).send(str, str5, str6, jsonObject2).enqueue(new RestAdapterCallback(sb2, this.mUnsentEventsManager, apiCallback2, null));
    }

    public void getRoomMessagesFrom(String str, String str2, Direction direction, int i, RoomEventFilter roomEventFilter, ApiCallback<TokensChunkEvents> apiCallback) {
        Direction direction2 = direction;
        StringBuilder sb = new StringBuilder();
        sb.append("messagesFrom : roomId ");
        sb.append(str);
        sb.append(" fromToken ");
        sb.append(str2);
        sb.append("with direction ");
        sb.append(direction2);
        sb.append(" with limit ");
        sb.append(i);
        String sb2 = sb.toString();
        Call roomMessagesFrom = ((RoomsApi) this.mApi).getRoomMessagesFrom(str, str2, direction2 == Direction.BACKWARDS ? "b" : "f", i, toJson(roomEventFilter));
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final String str3 = str;
        final String str4 = str2;
        final Direction direction3 = direction;
        final int i2 = i;
        final RoomEventFilter roomEventFilter2 = roomEventFilter;
        final ApiCallback<TokensChunkEvents> apiCallback2 = apiCallback;
        AnonymousClass3 r0 = new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.getRoomMessagesFrom(str3, str4, direction3, i2, roomEventFilter2, apiCallback2);
            }
        };
        roomMessagesFrom.enqueue(new RestAdapterCallback(sb2, unsentEventsManager, apiCallback, r0));
    }

    public void inviteUserToRoom(final String str, final String str2, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("inviteToRoom : roomId ");
        sb.append(str);
        sb.append(" userId ");
        sb.append(str2);
        String sb2 = sb.toString();
        User user = new User();
        user.user_id = str2;
        ((RoomsApi) this.mApi).invite(str, user).enqueue(new RestAdapterCallback(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.inviteUserToRoom(str, str2, apiCallback);
            }
        }));
    }

    public void inviteByEmailToRoom(String str, String str2, ApiCallback<Void> apiCallback) {
        inviteThreePidToRoom("email", str2, str, apiCallback);
    }

    /* access modifiers changed from: private */
    public void inviteThreePidToRoom(String str, String str2, String str3, ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("inviteThreePidToRoom : medium ");
        sb.append(str);
        sb.append(" roomId ");
        sb.append(str3);
        String sb2 = sb.toString();
        String uri = this.mHsConfig.getIdentityServerUri().toString();
        if (uri.startsWith("http://")) {
            uri = uri.substring(7);
        } else if (uri.startsWith(UrlUtilKt.HTTPS_SCHEME)) {
            uri = uri.substring(8);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("id_server", uri);
        hashMap.put(PasswordLoginParams.IDENTIFIER_KEY_MEDIUM, str);
        hashMap.put(PasswordLoginParams.IDENTIFIER_KEY_ADDRESS, str2);
        Call invite = ((RoomsApi) this.mApi).invite(str3, (Map<String, String>) hashMap);
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final String str4 = str;
        final String str5 = str2;
        final String str6 = str3;
        final ApiCallback<Void> apiCallback2 = apiCallback;
        AnonymousClass5 r4 = new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.inviteThreePidToRoom(str4, str5, str6, apiCallback2);
            }
        };
        invite.enqueue(new RestAdapterCallback(sb2, unsentEventsManager, apiCallback, r4));
    }

    public void joinRoom(String str, List<String> list, Map<String, Object> map, ApiCallback<RoomResponse> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("joinRoom : roomId ");
        sb.append(str);
        String sb2 = sb.toString();
        Call joinRoomByAliasOrId = ((RoomsApi) this.mApi).joinRoomByAliasOrId(str, list == null ? new ArrayList<>() : list, map == null ? new HashMap<>() : map);
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final String str2 = str;
        final List<String> list2 = list;
        final Map<String, Object> map2 = map;
        final ApiCallback<RoomResponse> apiCallback2 = apiCallback;
        AnonymousClass6 r4 = new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.joinRoom(str2, list2, map2, apiCallback2);
            }
        };
        joinRoomByAliasOrId.enqueue(new RestAdapterCallback(sb2, unsentEventsManager, apiCallback, r4));
    }

    public void leaveRoom(final String str, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("leaveRoom : roomId ");
        sb.append(str);
        ((RoomsApi) this.mApi).leave(str, new JsonObject()).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.leaveRoom(str, apiCallback);
            }
        }));
    }

    public void forgetRoom(final String str, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("forgetRoom : roomId ");
        sb.append(str);
        ((RoomsApi) this.mApi).forget(str, new JsonObject()).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.forgetRoom(str, apiCallback);
            }
        }));
    }

    public void kickFromRoom(final String str, final UserIdAndReason userIdAndReason, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("kickFromRoom : roomId ");
        sb.append(str);
        sb.append(" userId ");
        sb.append(userIdAndReason.userId);
        ((RoomsApi) this.mApi).kick(str, userIdAndReason).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.kickFromRoom(str, userIdAndReason, apiCallback);
            }
        }));
    }

    public void banFromRoom(final String str, final UserIdAndReason userIdAndReason, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("banFromRoom : roomId ");
        sb.append(str);
        sb.append(" userId ");
        sb.append(userIdAndReason.userId);
        ((RoomsApi) this.mApi).ban(str, userIdAndReason).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.banFromRoom(str, userIdAndReason, apiCallback);
            }
        }));
    }

    public void unbanFromRoom(final String str, final UserIdAndReason userIdAndReason, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("Unban : roomId ");
        sb.append(str);
        sb.append(" userId ");
        sb.append(userIdAndReason.userId);
        ((RoomsApi) this.mApi).unban(str, userIdAndReason).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.unbanFromRoom(str, userIdAndReason, apiCallback);
            }
        }));
    }

    public void createRoom(final CreateRoomParams createRoomParams, final ApiCallback<CreateRoomResponse> apiCallback) {
        ((RoomsApi) this.mApi).createRoom(createRoomParams).enqueue(new RestAdapterCallback("createRoom", this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.createRoom(createRoomParams, apiCallback);
            }
        }));
    }

    public void initialSync(final String str, final ApiCallback<RoomResponse> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("initialSync : roomId ");
        sb.append(str);
        ((RoomsApi) this.mApi).initialSync(str, 30).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.initialSync(str, apiCallback);
            }
        }));
    }

    public void getEvent(String str, String str2, ApiCallback<Event> apiCallback) {
        final ApiCallback<Event> apiCallback2 = apiCallback;
        final String str3 = str;
        final String str4 = str2;
        AnonymousClass14 r0 = new SimpleApiCallback<Event>(apiCallback) {
            public void onSuccess(Event event) {
                apiCallback2.onSuccess(event);
            }

            public void onMatrixError(MatrixError matrixError) {
                if (TextUtils.equals(matrixError.errcode, MatrixError.UNRECOGNIZED)) {
                    RoomsRestClient.this.getContextOfEvent(str3, str4, 1, null, new SimpleApiCallback<EventContext>(apiCallback2) {
                        public void onSuccess(EventContext eventContext) {
                            apiCallback2.onSuccess(eventContext.event);
                        }
                    });
                } else {
                    apiCallback2.onMatrixError(matrixError);
                }
            }
        };
        getEventFromRoomIdEventId(str, str2, r0);
    }

    /* access modifiers changed from: private */
    public void getEventFromRoomIdEventId(final String str, final String str2, final ApiCallback<Event> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("getEventFromRoomIdEventId : roomId ");
        sb.append(str);
        sb.append(" eventId ");
        sb.append(str2);
        ((RoomsApi) this.mApi).getEvent(str, str2).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.getEventFromRoomIdEventId(str, str2, apiCallback);
            }
        }));
    }

    public void getContextOfEvent(String str, String str2, int i, RoomEventFilter roomEventFilter, ApiCallback<EventContext> apiCallback) {
        final String str3 = str;
        final String str4 = str2;
        final int i2 = i;
        StringBuilder sb = new StringBuilder();
        sb.append("getContextOfEvent : roomId ");
        sb.append(str);
        sb.append(" eventId ");
        sb.append(str2);
        sb.append(" limit ");
        sb.append(i2);
        String sb2 = sb.toString();
        final RoomEventFilter roomEventFilter2 = roomEventFilter;
        Call contextOfEvent = ((RoomsApi) this.mApi).getContextOfEvent(str, str2, i2, toJson(roomEventFilter2));
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final ApiCallback<EventContext> apiCallback2 = apiCallback;
        AnonymousClass16 r0 = new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.getContextOfEvent(str3, str4, i2, roomEventFilter2, apiCallback2);
            }
        };
        contextOfEvent.enqueue(new RestAdapterCallback(sb2, unsentEventsManager, apiCallback, r0));
    }

    public void updateRoomName(final String str, final String str2, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateName : roomId ");
        sb.append(str);
        sb.append(" name ");
        sb.append(str2);
        String sb2 = sb.toString();
        HashMap hashMap = new HashMap();
        hashMap.put(TermsResponse.NAME, str2);
        ((RoomsApi) this.mApi).sendStateEvent(str, Event.EVENT_TYPE_STATE_ROOM_NAME, hashMap).enqueue(new RestAdapterCallback(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.updateRoomName(str, str2, apiCallback);
            }
        }));
    }

    public void updateCanonicalAlias(final String str, final String str2, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateCanonicalAlias : roomId ");
        sb.append(str);
        sb.append(" canonicalAlias ");
        sb.append(str2);
        String sb2 = sb.toString();
        HashMap hashMap = new HashMap();
        hashMap.put("alias", str2);
        ((RoomsApi) this.mApi).sendStateEvent(str, Event.EVENT_TYPE_STATE_CANONICAL_ALIAS, hashMap).enqueue(new RestAdapterCallback(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.updateCanonicalAlias(str, str2, apiCallback);
            }
        }));
    }

    public void updateHistoryVisibility(final String str, final String str2, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateHistoryVisibility : roomId ");
        sb.append(str);
        sb.append(" visibility ");
        sb.append(str2);
        String sb2 = sb.toString();
        HashMap hashMap = new HashMap();
        hashMap.put("history_visibility", str2);
        ((RoomsApi) this.mApi).sendStateEvent(str, Event.EVENT_TYPE_STATE_HISTORY_VISIBILITY, hashMap).enqueue(new RestAdapterCallback(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.updateHistoryVisibility(str, str2, apiCallback);
            }
        }));
    }

    public void updateDirectoryVisibility(final String str, final String str2, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateRoomDirectoryVisibility : roomId=");
        sb.append(str);
        sb.append(" visibility=");
        sb.append(str2);
        String sb2 = sb.toString();
        RoomDirectoryVisibility roomDirectoryVisibility = new RoomDirectoryVisibility();
        roomDirectoryVisibility.visibility = str2;
        ((RoomsApi) this.mApi).setRoomDirectoryVisibility(str, roomDirectoryVisibility).enqueue(new RestAdapterCallback(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.updateDirectoryVisibility(str, str2, apiCallback);
            }
        }));
    }

    public void getDirectoryVisibility(final String str, final ApiCallback<RoomDirectoryVisibility> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("getDirectoryVisibility roomId=");
        sb.append(str);
        ((RoomsApi) this.mApi).getRoomDirectoryVisibility(str).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.getDirectoryVisibility(str, apiCallback);
            }
        }));
    }

    public void getRoomMembers(String str, String str2, String str3, String str4, ApiCallback<ChunkEvents> apiCallback) {
        final String str5 = str;
        StringBuilder sb = new StringBuilder();
        sb.append("getRoomMembers roomId=");
        sb.append(str);
        String sb2 = sb.toString();
        final String str6 = str2;
        final String str7 = str3;
        final String str8 = str4;
        Call members = ((RoomsApi) this.mApi).getMembers(str, str2, str7, str8);
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final ApiCallback<ChunkEvents> apiCallback2 = apiCallback;
        AnonymousClass22 r0 = new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.getRoomMembers(str5, str6, str7, str8, apiCallback2);
            }
        };
        members.enqueue(new RestAdapterCallback(sb2, unsentEventsManager, apiCallback, r0));
    }

    public void updateTopic(final String str, final String str2, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateTopic : roomId ");
        sb.append(str);
        sb.append(" topic ");
        sb.append(str2);
        String sb2 = sb.toString();
        HashMap hashMap = new HashMap();
        hashMap.put("topic", str2);
        ((RoomsApi) this.mApi).sendStateEvent(str, Event.EVENT_TYPE_STATE_ROOM_TOPIC, hashMap).enqueue(new RestAdapterCallback(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.updateTopic(str, str2, apiCallback);
            }
        }));
    }

    public void redactEvent(final String str, final String str2, final ApiCallback<Event> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("redactEvent : roomId ");
        sb.append(str);
        sb.append(" eventId ");
        sb.append(str2);
        ((RoomsApi) this.mApi).redactEvent(str, str2, new JsonObject()).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.redactEvent(str, str2, apiCallback);
            }
        }));
    }

    public void reportEvent(String str, String str2, int i, String str3, ApiCallback<Void> apiCallback) {
        final String str4 = str;
        final String str5 = str2;
        StringBuilder sb = new StringBuilder();
        sb.append("report : roomId ");
        sb.append(str);
        sb.append(" eventId ");
        sb.append(str2);
        String sb2 = sb.toString();
        ReportContentParams reportContentParams = new ReportContentParams();
        final int i2 = i;
        reportContentParams.score = i2;
        final String str6 = str3;
        reportContentParams.reason = str6;
        Call reportEvent = ((RoomsApi) this.mApi).reportEvent(str, str2, reportContentParams);
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final ApiCallback<Void> apiCallback2 = apiCallback;
        AnonymousClass25 r0 = new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.reportEvent(str4, str5, i2, str6, apiCallback2);
            }
        };
        reportEvent.enqueue(new RestAdapterCallback(sb2, unsentEventsManager, apiCallback, r0));
    }

    public void updatePowerLevels(final String str, final PowerLevels powerLevels, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("updatePowerLevels : roomId ");
        sb.append(str);
        sb.append(" powerLevels ");
        sb.append(powerLevels);
        ((RoomsApi) this.mApi).setPowerLevels(str, powerLevels).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.updatePowerLevels(str, powerLevels, apiCallback);
            }
        }));
    }

    public void sendStateEvent(String str, String str2, String str3, Map<String, Object> map, ApiCallback<Void> apiCallback) {
        String str4 = str;
        String str5 = str2;
        String str6 = str3;
        Map<String, Object> map2 = map;
        ApiCallback<Void> apiCallback2 = apiCallback;
        StringBuilder sb = new StringBuilder();
        sb.append("sendStateEvent : roomId ");
        sb.append(str);
        sb.append(" - eventType ");
        sb.append(str5);
        String sb2 = sb.toString();
        if (str6 != null) {
            Call sendStateEvent = ((RoomsApi) this.mApi).sendStateEvent(str, str5, str6, map2);
            UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
            final String str7 = str;
            final String str8 = str2;
            final String str9 = str3;
            final Map<String, Object> map3 = map;
            final ApiCallback<Void> apiCallback3 = apiCallback;
            AnonymousClass27 r0 = new RequestRetryCallBack() {
                public void onRetry() {
                    RoomsRestClient.this.sendStateEvent(str7, str8, str9, map3, apiCallback3);
                }
            };
            sendStateEvent.enqueue(new RestAdapterCallback(sb2, unsentEventsManager, apiCallback2, r0));
            return;
        }
        Call sendStateEvent2 = ((RoomsApi) this.mApi).sendStateEvent(str, str5, map2);
        UnsentEventsManager unsentEventsManager2 = this.mUnsentEventsManager;
        final String str10 = str;
        final String str11 = str2;
        final Map<String, Object> map4 = map;
        final ApiCallback<Void> apiCallback4 = apiCallback;
        AnonymousClass28 r02 = new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.sendStateEvent(str10, str11, null, map4, apiCallback4);
            }
        };
        sendStateEvent2.enqueue(new RestAdapterCallback(sb2, unsentEventsManager2, apiCallback2, r02));
    }

    public void getStateEvent(final String str, final String str2, final ApiCallback<JsonElement> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("getStateEvent : roomId ");
        sb.append(str);
        sb.append(" eventId ");
        sb.append(str2);
        ((RoomsApi) this.mApi).getStateEvent(str, str2).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.getStateEvent(str, str2, apiCallback);
            }
        }));
    }

    public void getStateEvent(String str, String str2, String str3, ApiCallback<JsonElement> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("getStateEvent : roomId ");
        sb.append(str);
        sb.append(" eventId ");
        sb.append(str2);
        sb.append(" stateKey ");
        sb.append(str3);
        String sb2 = sb.toString();
        Call stateEvent = ((RoomsApi) this.mApi).getStateEvent(str, str2, str3);
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final String str4 = str;
        final String str5 = str2;
        final String str6 = str3;
        final ApiCallback<JsonElement> apiCallback2 = apiCallback;
        AnonymousClass30 r4 = new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.getStateEvent(str4, str5, str6, apiCallback2);
            }
        };
        stateEvent.enqueue(new RestAdapterCallback(sb2, unsentEventsManager, apiCallback, r4));
    }

    public void sendTypingNotification(String str, String str2, boolean z, int i, ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("sendTypingNotification : roomId ");
        sb.append(str);
        sb.append(" isTyping ");
        sb.append(z);
        String sb2 = sb.toString();
        Typing typing = new Typing();
        typing.typing = z;
        if (-1 != i) {
            typing.timeout = i;
        }
        ((RoomsApi) this.mApi).setTypingNotification(str, str2, typing).enqueue(new RestAdapterCallback(sb2, null, apiCallback, null));
    }

    public void updateAvatarUrl(final String str, final String str2, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateAvatarUrl : roomId ");
        sb.append(str);
        sb.append(" avatarUrl ");
        sb.append(str2);
        String sb2 = sb.toString();
        HashMap hashMap = new HashMap();
        hashMap.put("url", str2);
        ((RoomsApi) this.mApi).sendStateEvent(str, Event.EVENT_TYPE_STATE_ROOM_AVATAR, hashMap).enqueue(new RestAdapterCallback(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.updateAvatarUrl(str, str2, apiCallback);
            }
        }));
    }

    public void sendReadMarker(String str, String str2, String str3, ApiCallback<Void> apiCallback) {
        String str4 = str;
        String str5 = str2;
        String str6 = str3;
        StringBuilder sb = new StringBuilder();
        sb.append("sendReadMarker : roomId ");
        sb.append(str4);
        sb.append(" - rmEventId ");
        sb.append(str5);
        sb.append(" -- rrEventId ");
        sb.append(str6);
        String sb2 = sb.toString();
        HashMap hashMap = new HashMap();
        if (!TextUtils.isEmpty(str2)) {
            hashMap.put("m.fully_read", str5);
        }
        if (!TextUtils.isEmpty(str3)) {
            hashMap.put(READ_MARKER_READ, str6);
        }
        Call sendReadMarker = ((RoomsApi) this.mApi).sendReadMarker(str4, hashMap);
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final String str7 = str;
        final String str8 = str2;
        final String str9 = str3;
        final ApiCallback<Void> apiCallback2 = apiCallback;
        AnonymousClass32 r0 = new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.sendReadMarker(str7, str8, str9, apiCallback2);
            }
        };
        RestAdapterCallback restAdapterCallback = new RestAdapterCallback(sb2, unsentEventsManager, true, apiCallback, r0);
        sendReadMarker.enqueue(restAdapterCallback);
    }

    public void addTag(String str, String str2, String str3, Double d, ApiCallback<Void> apiCallback) {
        final String str4 = str2;
        final String str5 = str3;
        final Double d2 = d;
        StringBuilder sb = new StringBuilder();
        sb.append("addTag : roomId ");
        sb.append(str2);
        sb.append(" - tag ");
        sb.append(str5);
        sb.append(" - order ");
        sb.append(d2);
        String sb2 = sb.toString();
        HashMap hashMap = new HashMap();
        hashMap.put("order", d2);
        final String str6 = str;
        Call addTag = ((RoomsApi) this.mApi).addTag(str, str2, str5, hashMap);
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final ApiCallback<Void> apiCallback2 = apiCallback;
        AnonymousClass33 r0 = new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.addTag(str6, str4, str5, d2, apiCallback2);
            }
        };
        addTag.enqueue(new RestAdapterCallback(sb2, unsentEventsManager, apiCallback, r0));
    }

    public void removeTag(String str, String str2, String str3, ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("removeTag : roomId ");
        sb.append(str2);
        sb.append(" - tag ");
        sb.append(str3);
        String sb2 = sb.toString();
        Call removeTag = ((RoomsApi) this.mApi).removeTag(str, str2, str3);
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final String str4 = str;
        final String str5 = str2;
        final String str6 = str3;
        final ApiCallback<Void> apiCallback2 = apiCallback;
        AnonymousClass34 r4 = new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.removeTag(str4, str5, str6, apiCallback2);
            }
        };
        removeTag.enqueue(new RestAdapterCallback(sb2, unsentEventsManager, apiCallback, r4));
    }

    public void updateURLPreviewStatus(String str, String str2, boolean z, ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateURLPreviewStatus : roomId ");
        sb.append(str2);
        sb.append(" - status ");
        sb.append(z);
        String sb2 = sb.toString();
        HashMap hashMap = new HashMap();
        hashMap.put(AccountDataElement.ACCOUNT_DATA_KEY_URL_PREVIEW_DISABLE, Boolean.valueOf(!z));
        Call updateAccountData = ((RoomsApi) this.mApi).updateAccountData(str, str2, Event.EVENT_TYPE_URL_PREVIEW, hashMap);
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final String str3 = str;
        final String str4 = str2;
        final boolean z2 = z;
        final ApiCallback<Void> apiCallback2 = apiCallback;
        AnonymousClass35 r4 = new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.updateURLPreviewStatus(str3, str4, z2, apiCallback2);
            }
        };
        updateAccountData.enqueue(new RestAdapterCallback(sb2, unsentEventsManager, apiCallback, r4));
    }

    public void getRoomIdByAlias(final String str, final ApiCallback<RoomAliasDescription> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("getRoomIdByAlias : ");
        sb.append(str);
        ((RoomsApi) this.mApi).getRoomIdByAlias(str).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.getRoomIdByAlias(str, apiCallback);
            }
        }));
    }

    public void setRoomIdByAlias(final String str, final String str2, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("setRoomIdByAlias : roomAlias ");
        sb.append(str2);
        sb.append(" - roomId : ");
        sb.append(str);
        String sb2 = sb.toString();
        RoomAliasDescription roomAliasDescription = new RoomAliasDescription();
        roomAliasDescription.room_id = str;
        ((RoomsApi) this.mApi).setRoomIdByAlias(str2, roomAliasDescription).enqueue(new RestAdapterCallback(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.setRoomIdByAlias(str, str2, apiCallback);
            }
        }));
    }

    public void removeRoomAlias(final String str, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("removeRoomAlias : ");
        sb.append(str);
        ((RoomsApi) this.mApi).removeRoomAlias(str).enqueue(new RestAdapterCallback(sb.toString(), this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.removeRoomAlias(str, apiCallback);
            }
        }));
    }

    public void updateJoinRules(final String str, final String str2, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateJoinRules : roomId=");
        sb.append(str);
        sb.append(" rule=");
        sb.append(str2);
        String sb2 = sb.toString();
        HashMap hashMap = new HashMap();
        hashMap.put("join_rule", str2);
        ((RoomsApi) this.mApi).sendStateEvent(str, Event.EVENT_TYPE_STATE_ROOM_JOIN_RULES, hashMap).enqueue(new RestAdapterCallback(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.updateJoinRules(str, str2, apiCallback);
            }
        }));
    }

    public void updateGuestAccess(final String str, final String str2, final ApiCallback<Void> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("updateGuestAccess : roomId=");
        sb.append(str);
        sb.append(" rule=");
        sb.append(str2);
        String sb2 = sb.toString();
        HashMap hashMap = new HashMap();
        hashMap.put("guest_access", str2);
        ((RoomsApi) this.mApi).sendStateEvent(str, Event.EVENT_TYPE_STATE_ROOM_GUEST_ACCESS, hashMap).enqueue(new RestAdapterCallback(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                RoomsRestClient.this.updateGuestAccess(str, str2, apiCallback);
            }
        }));
    }

    private String toJson(RoomEventFilter roomEventFilter) {
        if (roomEventFilter == null) {
            return null;
        }
        return roomEventFilter.toJSONString();
    }
}
