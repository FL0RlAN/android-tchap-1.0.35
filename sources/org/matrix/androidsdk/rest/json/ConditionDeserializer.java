package org.matrix.androidsdk.rest.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.rest.model.bingrules.Condition;
import org.matrix.androidsdk.rest.model.bingrules.ContainsDisplayNameCondition;
import org.matrix.androidsdk.rest.model.bingrules.DeviceCondition;
import org.matrix.androidsdk.rest.model.bingrules.EventMatchCondition;
import org.matrix.androidsdk.rest.model.bingrules.RoomMemberCountCondition;
import org.matrix.androidsdk.rest.model.bingrules.SenderNotificationPermissionCondition;
import org.matrix.androidsdk.rest.model.bingrules.UnknownCondition;

public class ConditionDeserializer implements JsonDeserializer<Condition> {
    private static final String LOG_TAG = ConditionDeserializer.class.getSimpleName();

    public Condition deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonElement jsonElement2 = jsonElement.getAsJsonObject().get("kind");
        if (jsonElement2 != null) {
            String asString = jsonElement2.getAsString();
            if (asString != null) {
                char c = 65535;
                switch (asString.hashCode()) {
                    case -1335157162:
                        if (asString.equals(Condition.KIND_DEVICE)) {
                            c = 1;
                            break;
                        }
                        break;
                    case -895393272:
                        if (asString.equals(Condition.KIND_CONTAINS_DISPLAY_NAME)) {
                            c = 2;
                            break;
                        }
                        break;
                    case -224562791:
                        if (asString.equals(Condition.KIND_SENDER_NOTIFICATION_PERMISSION)) {
                            c = 4;
                            break;
                        }
                        break;
                    case 443732960:
                        if (asString.equals(Condition.KIND_EVENT_MATCH)) {
                            c = 0;
                            break;
                        }
                        break;
                    case 1519110158:
                        if (asString.equals(Condition.KIND_ROOM_MEMBER_COUNT)) {
                            c = 3;
                            break;
                        }
                        break;
                }
                if (c == 0) {
                    return (Condition) jsonDeserializationContext.deserialize(jsonElement, EventMatchCondition.class);
                }
                if (c == 1) {
                    return (Condition) jsonDeserializationContext.deserialize(jsonElement, DeviceCondition.class);
                }
                if (c == 2) {
                    return (Condition) jsonDeserializationContext.deserialize(jsonElement, ContainsDisplayNameCondition.class);
                }
                if (c == 3) {
                    return (Condition) jsonDeserializationContext.deserialize(jsonElement, RoomMemberCountCondition.class);
                }
                if (c == 4) {
                    return (Condition) jsonDeserializationContext.deserialize(jsonElement, SenderNotificationPermissionCondition.class);
                }
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## deserialize() : unsupported kind ");
                sb.append(asString);
                sb.append(" with value ");
                sb.append(jsonElement);
                Log.e(str, sb.toString());
                return (Condition) jsonDeserializationContext.deserialize(jsonElement, UnknownCondition.class);
            }
        }
        return null;
    }
}
