package fr.gouv.tchap.sdk.session.room.model;

import androidx.core.app.NotificationCompat;
import com.google.gson.JsonElement;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.rest.model.Event;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0007\u001a\u00020\b\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"DIRECT", "", "EVENT_TYPE_STATE_ROOM_ACCESS_RULES", "RESTRICTED", "STATE_EVENT_CONTENT_KEY_RULE", "UNRESTRICTED", "getRule", "event", "Lorg/matrix/androidsdk/rest/model/Event;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 2, mv = {1, 1, 13})
/* compiled from: RoomAccessRules.kt */
public final class RoomAccessRulesKt {
    public static final String DIRECT = "direct";
    public static final String EVENT_TYPE_STATE_ROOM_ACCESS_RULES = "im.vector.room.access_rules";
    public static final String RESTRICTED = "restricted";
    public static final String STATE_EVENT_CONTENT_KEY_RULE = "rule";
    public static final String UNRESTRICTED = "unrestricted";

    public static final String getRule(Event event) {
        Intrinsics.checkParameterIsNotNull(event, NotificationCompat.CATEGORY_EVENT);
        RoomAccessRulesContent roomAccessRulesContent = (RoomAccessRulesContent) JsonUtils.toClass((JsonElement) event.getContentAsJsonObject(), RoomAccessRulesContent.class);
        if (roomAccessRulesContent != null) {
            return roomAccessRulesContent.rule;
        }
        return null;
    }
}
