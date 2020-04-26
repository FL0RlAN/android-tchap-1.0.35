package org.matrix.androidsdk.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import org.matrix.androidsdk.core.json.BooleanDeserializer;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedEventContent;
import org.matrix.androidsdk.crypto.model.crypto.ForwardedRoomKeyContent;
import org.matrix.androidsdk.crypto.model.crypto.OlmEventContent;
import org.matrix.androidsdk.crypto.model.crypto.RoomKeyContent;
import org.matrix.androidsdk.crypto.rest.model.crypto.RoomKeyShare;
import org.matrix.androidsdk.crypto.rest.model.crypto.RoomKeyShareRequest;
import org.matrix.androidsdk.rest.json.ConditionDeserializer;
import org.matrix.androidsdk.rest.json.MatrixFieldNamingStrategy;
import org.matrix.androidsdk.rest.model.ContentResponse;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.EventContent;
import org.matrix.androidsdk.rest.model.PowerLevels;
import org.matrix.androidsdk.rest.model.RoomCreateContent;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.RoomPinnedEventsContent;
import org.matrix.androidsdk.rest.model.RoomTags;
import org.matrix.androidsdk.rest.model.RoomTombstoneContent;
import org.matrix.androidsdk.rest.model.StateEvent;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.bingrules.Condition;
import org.matrix.androidsdk.rest.model.login.RegistrationFlowResponse;
import org.matrix.androidsdk.rest.model.message.AudioMessage;
import org.matrix.androidsdk.rest.model.message.FileMessage;
import org.matrix.androidsdk.rest.model.message.ImageMessage;
import org.matrix.androidsdk.rest.model.message.LocationMessage;
import org.matrix.androidsdk.rest.model.message.Message;
import org.matrix.androidsdk.rest.model.message.StickerJsonMessage;
import org.matrix.androidsdk.rest.model.message.StickerMessage;
import org.matrix.androidsdk.rest.model.message.VideoMessage;
import org.matrix.androidsdk.rest.model.pid.RoomThirdPartyInvite;

public class JsonUtils {
    private static final String LOG_TAG = JsonUtils.class.getSimpleName();
    private static final Gson basicGson = new Gson();
    private static final Gson gson = new GsonBuilder().setFieldNamingStrategy(new MatrixFieldNamingStrategy()).excludeFieldsWithModifiers(2, 8).registerTypeAdapter(Condition.class, new ConditionDeserializer()).registerTypeAdapter(Boolean.TYPE, new BooleanDeserializer(false)).registerTypeAdapter(Boolean.class, new BooleanDeserializer(true)).create();
    private static final Gson gsonWithNullSerialization = new GsonBuilder().setFieldNamingStrategy(new MatrixFieldNamingStrategy()).excludeFieldsWithModifiers(2, 8).serializeNulls().registerTypeAdapter(Condition.class, new ConditionDeserializer()).registerTypeAdapter(Boolean.TYPE, new BooleanDeserializer(false)).registerTypeAdapter(Boolean.class, new BooleanDeserializer(true)).create();
    private static final Gson kotlinGson = new GsonBuilder().registerTypeAdapter(Boolean.TYPE, new BooleanDeserializer(false)).registerTypeAdapter(Boolean.class, new BooleanDeserializer(true)).create();

    public static Gson getBasicGson() {
        return basicGson;
    }

    public static Gson getKotlinGson() {
        return kotlinGson;
    }

    public static Gson getGson(boolean z) {
        return z ? gsonWithNullSerialization : gson;
    }

    public static StateEvent toStateEvent(JsonElement jsonElement) {
        return (StateEvent) toClass(jsonElement, StateEvent.class);
    }

    public static User toUser(JsonElement jsonElement) {
        return (User) toClass(jsonElement, User.class);
    }

    public static RoomMember toRoomMember(JsonElement jsonElement) {
        return (RoomMember) toClass(jsonElement, RoomMember.class);
    }

    public static RoomTags toRoomTags(JsonElement jsonElement) {
        return (RoomTags) toClass(jsonElement, RoomTags.class);
    }

    public static MatrixError toMatrixError(JsonElement jsonElement) {
        return (MatrixError) toClass(jsonElement, MatrixError.class);
    }

    public static String getMessageMsgType(JsonElement jsonElement) {
        try {
            return ((Message) gson.fromJson(jsonElement, Message.class)).msgtype;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getMessageMsgType failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            return null;
        }
    }

    public static Message toMessage(JsonElement jsonElement) {
        try {
            Message message = (Message) gson.fromJson(jsonElement, Message.class);
            if (Message.MSGTYPE_IMAGE.equals(message.msgtype)) {
                return toImageMessage(jsonElement);
            }
            if (Message.MSGTYPE_VIDEO.equals(message.msgtype)) {
                return toVideoMessage(jsonElement);
            }
            if (Message.MSGTYPE_LOCATION.equals(message.msgtype)) {
                return toLocationMessage(jsonElement);
            }
            if (Message.MSGTYPE_FILE.equals(message.msgtype)) {
                return toFileMessage(jsonElement);
            }
            return Message.MSGTYPE_AUDIO.equals(message.msgtype) ? toAudioMessage(jsonElement) : message;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## toMessage failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            return new Message();
        }
    }

    public static Event toEvent(JsonElement jsonElement) {
        return (Event) toClass(jsonElement, Event.class);
    }

    public static EncryptedEventContent toEncryptedEventContent(JsonElement jsonElement) {
        return (EncryptedEventContent) toClass(jsonElement, EncryptedEventContent.class);
    }

    public static OlmEventContent toOlmEventContent(JsonElement jsonElement) {
        return (OlmEventContent) toClass(jsonElement, OlmEventContent.class);
    }

    public static EventContent toEventContent(JsonElement jsonElement) {
        return (EventContent) toClass(jsonElement, EventContent.class);
    }

    public static RoomKeyContent toRoomKeyContent(JsonElement jsonElement) {
        return (RoomKeyContent) toClass(jsonElement, RoomKeyContent.class);
    }

    public static RoomKeyShare toRoomKeyShare(JsonElement jsonElement) {
        return (RoomKeyShare) toClass(jsonElement, RoomKeyShare.class);
    }

    public static RoomKeyShareRequest toRoomKeyShareRequest(JsonElement jsonElement) {
        return (RoomKeyShareRequest) toClass(jsonElement, RoomKeyShareRequest.class);
    }

    public static ForwardedRoomKeyContent toForwardedRoomKeyContent(JsonElement jsonElement) {
        return (ForwardedRoomKeyContent) toClass(jsonElement, ForwardedRoomKeyContent.class);
    }

    public static ImageMessage toImageMessage(JsonElement jsonElement) {
        return (ImageMessage) toClass(jsonElement, ImageMessage.class);
    }

    public static StickerMessage toStickerMessage(JsonElement jsonElement) {
        return new StickerMessage((StickerJsonMessage) toClass(jsonElement, StickerJsonMessage.class));
    }

    public static FileMessage toFileMessage(JsonElement jsonElement) {
        return (FileMessage) toClass(jsonElement, FileMessage.class);
    }

    public static AudioMessage toAudioMessage(JsonElement jsonElement) {
        return (AudioMessage) toClass(jsonElement, AudioMessage.class);
    }

    public static VideoMessage toVideoMessage(JsonElement jsonElement) {
        return (VideoMessage) toClass(jsonElement, VideoMessage.class);
    }

    public static LocationMessage toLocationMessage(JsonElement jsonElement) {
        return (LocationMessage) toClass(jsonElement, LocationMessage.class);
    }

    public static ContentResponse toContentResponse(String str) {
        return (ContentResponse) toClass(str, ContentResponse.class);
    }

    public static PowerLevels toPowerLevels(JsonElement jsonElement) {
        return (PowerLevels) toClass(jsonElement, PowerLevels.class);
    }

    public static RoomThirdPartyInvite toRoomThirdPartyInvite(JsonElement jsonElement) {
        return (RoomThirdPartyInvite) toClass(jsonElement, RoomThirdPartyInvite.class);
    }

    public static RegistrationFlowResponse toRegistrationFlowResponse(String str) {
        return (RegistrationFlowResponse) toClass(str, RegistrationFlowResponse.class);
    }

    public static RoomTombstoneContent toRoomTombstoneContent(JsonElement jsonElement) {
        return (RoomTombstoneContent) toClass(jsonElement, RoomTombstoneContent.class);
    }

    public static RoomCreateContent toRoomCreateContent(JsonElement jsonElement) {
        return (RoomCreateContent) toClass(jsonElement, RoomCreateContent.class);
    }

    public static RoomPinnedEventsContent toRoomPinnedEventsContent(JsonElement jsonElement) {
        return (RoomPinnedEventsContent) toClass(jsonElement, RoomPinnedEventsContent.class);
    }

    public static <T> T toClass(JsonElement jsonElement, Class<T> cls) {
        T t;
        String str = "## toClass failed ";
        try {
            t = gson.fromJson(jsonElement, cls);
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(e.getMessage());
            Log.e(str2, sb.toString(), e);
            t = null;
        }
        if (t != null) {
            return t;
        }
        try {
            return cls.getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Throwable th) {
            String str3 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(th.getMessage());
            Log.e(str3, sb2.toString(), th);
            return t;
        }
    }

    public static <T> T toClass(String str, Class<T> cls) {
        T t;
        String str2 = "## toClass failed ";
        try {
            t = gson.fromJson(str, cls);
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(e.getMessage());
            Log.e(str3, sb.toString(), e);
            t = null;
        }
        if (t != null) {
            return t;
        }
        try {
            return cls.getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Throwable th) {
            String str4 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(th.getMessage());
            Log.e(str4, sb2.toString(), th);
            return t;
        }
    }

    public static JsonObject toJson(Event event) {
        try {
            return (JsonObject) gson.toJsonTree(event);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## toJson failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            return new JsonObject();
        }
    }

    public static JsonObject toJson(Message message) {
        try {
            return (JsonObject) gson.toJsonTree(message);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## toJson failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            return null;
        }
    }

    public static String getAsString(Map<String, Object> map, String str) {
        if (!map.containsKey(str) || !(map.get(str) instanceof String)) {
            return null;
        }
        return (String) map.get(str);
    }
}
