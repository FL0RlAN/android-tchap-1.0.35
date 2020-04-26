package fr.gouv.tchap.sdk.session.room.model;

import androidx.core.app.NotificationCompat;
import com.google.gson.JsonElement;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.rest.model.Event;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u001a\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t\u001a\u0015\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\f\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"DEFAULT_RETENTION_VALUE_IN_DAYS", "", "EVENT_TYPE_STATE_ROOM_RETENTION", "", "STATE_EVENT_CONTENT_EXPIRE_ON_CLIENTS", "STATE_EVENT_CONTENT_MAX_LIFETIME", "getExpireOnClients", "", "event", "Lorg/matrix/androidsdk/rest/model/Event;", "getMaxLifetime", "", "(Lorg/matrix/androidsdk/rest/model/Event;)Ljava/lang/Long;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 2, mv = {1, 1, 13})
/* compiled from: RoomRetention.kt */
public final class RoomRetentionKt {
    public static final int DEFAULT_RETENTION_VALUE_IN_DAYS = 365;
    public static final String EVENT_TYPE_STATE_ROOM_RETENTION = "m.room.retention";
    public static final String STATE_EVENT_CONTENT_EXPIRE_ON_CLIENTS = "expire_on_clients";
    public static final String STATE_EVENT_CONTENT_MAX_LIFETIME = "max_lifetime";

    public static final Long getMaxLifetime(Event event) {
        Intrinsics.checkParameterIsNotNull(event, NotificationCompat.CATEGORY_EVENT);
        RoomRetentionContent roomRetentionContent = (RoomRetentionContent) JsonUtils.toClass((JsonElement) event.getContentAsJsonObject(), RoomRetentionContent.class);
        if (roomRetentionContent != null) {
            return roomRetentionContent.maxLifetime;
        }
        return null;
    }

    public static final boolean getExpireOnClients(Event event) {
        Intrinsics.checkParameterIsNotNull(event, NotificationCompat.CATEGORY_EVENT);
        RoomRetentionContent roomRetentionContent = (RoomRetentionContent) JsonUtils.toClass((JsonElement) event.getContentAsJsonObject(), RoomRetentionContent.class);
        if (roomRetentionContent != null) {
            Boolean bool = roomRetentionContent.expireOnClients;
            if (bool != null) {
                return bool.booleanValue();
            }
        }
        return false;
    }
}
