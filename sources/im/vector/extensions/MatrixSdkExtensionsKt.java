package im.vector.extensions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.vector.activity.VectorUniversalLinkActivity;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OutgoingRoomKeyRequestEntityFields;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.PowerLevels;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\f\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u0002\u001a\f\u0010\u0003\u001a\u00020\u0004*\u0004\u0018\u00010\u0005\u001a\f\u0010\u0006\u001a\u0004\u0018\u00010\u0001*\u00020\u0007\u001a\u0014\u0010\b\u001a\u00020\t*\u00020\u00052\b\u0010\n\u001a\u0004\u0018\u00010\u000b¨\u0006\f"}, d2 = {"getFingerprintHumanReadable", "", "Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "getRoomMaxPowerLevel", "", "Lorg/matrix/androidsdk/data/Room;", "getSessionId", "Lorg/matrix/androidsdk/rest/model/Event;", "isPowerLevelEnoughForAvatarUpdate", "", "aSession", "Lorg/matrix/androidsdk/MXSession;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 2, mv = {1, 1, 13})
/* compiled from: MatrixSdkExtensions.kt */
public final class MatrixSdkExtensionsKt {
    public static final String getFingerprintHumanReadable(MXDeviceInfo mXDeviceInfo) {
        Intrinsics.checkParameterIsNotNull(mXDeviceInfo, "receiver$0");
        String fingerprint = mXDeviceInfo.fingerprint();
        if (fingerprint != null) {
            List chunked = StringsKt.chunked(fingerprint, 4);
            if (chunked != null) {
                return CollectionsKt.joinToString$default(chunked, " ", null, null, 0, null, null, 62, null);
            }
        }
        return null;
    }

    public static final int getRoomMaxPowerLevel(Room room) {
        int i = 0;
        if (room == null) {
            return 0;
        }
        RoomState state = room.getState();
        if (state != null) {
            PowerLevels powerLevels = state.getPowerLevels();
            if (powerLevels != null) {
                int i2 = powerLevels.users_default;
                Map<String, Integer> map = powerLevels.users;
                if (map != null) {
                    Collection values = map.values();
                    if (values != null) {
                        Integer num = (Integer) CollectionsKt.max((Iterable) values);
                        if (num != null) {
                            i = num.intValue();
                        }
                    }
                }
                i = Math.max(i2, i);
            }
        }
        return i;
    }

    public static final boolean isPowerLevelEnoughForAvatarUpdate(Room room, MXSession mXSession) {
        Intrinsics.checkParameterIsNotNull(room, "receiver$0");
        if (mXSession == null) {
            return false;
        }
        RoomState state = room.getState();
        Intrinsics.checkExpressionValueIsNotNull(state, OutgoingRoomKeyRequestEntityFields.STATE);
        PowerLevels powerLevels = state.getPowerLevels();
        if (powerLevels == null || powerLevels.getUserPowerLevel(mXSession.getMyUserId()) < powerLevels.minimumPowerLevelForSendingEventAsStateEvent(Event.EVENT_TYPE_STATE_ROOM_AVATAR)) {
            return false;
        }
        return true;
    }

    public static final String getSessionId(Event event) {
        Intrinsics.checkParameterIsNotNull(event, "receiver$0");
        JsonElement wireContent = event.getWireContent();
        if (wireContent == null) {
            return null;
        }
        Intrinsics.checkExpressionValueIsNotNull(wireContent, "it");
        if (!wireContent.isJsonObject()) {
            wireContent = null;
        }
        if (wireContent == null) {
            return null;
        }
        JsonObject asJsonObject = wireContent.getAsJsonObject();
        if (asJsonObject == null) {
            return null;
        }
        JsonElement jsonElement = asJsonObject.get(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_SESSION_ID);
        if (jsonElement != null) {
            return jsonElement.getAsString();
        }
        return null;
    }
}
