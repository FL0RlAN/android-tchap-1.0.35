package fr.gouv.tchap.util;

import fr.gouv.tchap.sdk.session.room.model.RoomRetentionKt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.ClosedRange;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007\u001a\u0018\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0006\u001a\u00020\u0007H\u0002\u001a\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\u0005\u001a\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010\u001a\u000e\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u000e\u001a\u000e\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0006\u001a\u00020\u0007\u001a\u0006\u0010\u0014\u001a\u00020\u0003\u001a,\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0016\u001a\u00020\u00102\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00190\u0018\"\u000e\u0010\u0000\u001a\u00020\u0001XD¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"LOG_TAG", "", "clearExpiredRoomContents", "", "session", "Lorg/matrix/androidsdk/MXSession;", "room", "Lorg/matrix/androidsdk/data/Room;", "clearExpiredRoomContentsFromStore", "store", "Lorg/matrix/androidsdk/data/store/IMXStore;", "clearSessionExpiredContents", "", "convertDaysToMs", "", "daysNb", "", "convertMsToDays", "durationMs", "getRoomRetention", "isSecure", "setRoomRetention", "periodInDays", "callback", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Ljava/lang/Void;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 2, mv = {1, 1, 13})
/* compiled from: DinumUtils.kt */
public final class DinumUtilsKt {
    private static final String LOG_TAG = "DinumUtils";

    public static final boolean isSecure() {
        return Intrinsics.areEqual((Object) "agent", (Object) "protecteed");
    }

    public static final int getRoomRetention(Room room) {
        Intrinsics.checkParameterIsNotNull(room, BingRule.KIND_ROOM);
        List stateEvents = room.getState().getStateEvents(new HashSet(Arrays.asList(new String[]{RoomRetentionKt.EVENT_TYPE_STATE_ROOM_RETENTION})));
        if (stateEvents.size() > 1) {
            CollectionsKt.sortWith(stateEvents, new DinumUtilsKt$$special$$inlined$sortBy$1());
        }
        Intrinsics.checkExpressionValueIsNotNull(stateEvents, "room.state.getStateEvent…nServerTs }\n            }");
        Event event = (Event) CollectionsKt.lastOrNull(stateEvents);
        if (event == null) {
            return RoomRetentionKt.DEFAULT_RETENTION_VALUE_IN_DAYS;
        }
        Long maxLifetime = RoomRetentionKt.getMaxLifetime(event);
        if (maxLifetime == null) {
            return RoomRetentionKt.DEFAULT_RETENTION_VALUE_IN_DAYS;
        }
        long longValue = maxLifetime.longValue();
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## getRoomRetention(): the period ");
        sb.append(longValue);
        sb.append("ms is defined");
        Log.d(str, sb.toString());
        return RangesKt.coerceIn(convertMsToDays(longValue), (ClosedRange) new IntRange(1, RoomRetentionKt.DEFAULT_RETENTION_VALUE_IN_DAYS));
    }

    public static final void setRoomRetention(MXSession mXSession, Room room, int i, ApiCallback<Void> apiCallback) {
        Intrinsics.checkParameterIsNotNull(mXSession, "session");
        Intrinsics.checkParameterIsNotNull(room, BingRule.KIND_ROOM);
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        Map hashMap = new HashMap();
        hashMap.put(RoomRetentionKt.STATE_EVENT_CONTENT_MAX_LIFETIME, Long.valueOf(convertDaysToMs(i)));
        hashMap.put(RoomRetentionKt.STATE_EVENT_CONTENT_EXPIRE_ON_CLIENTS, Boolean.valueOf(true));
        mXSession.getRoomsApiClient().sendStateEvent(room.getRoomId(), RoomRetentionKt.EVENT_TYPE_STATE_ROOM_RETENTION, "", hashMap, apiCallback);
    }

    public static final void clearSessionExpiredContents(MXSession mXSession) {
        Intrinsics.checkParameterIsNotNull(mXSession, "session");
        IMXStore store = mXSession.getDataHandler().getStore();
        String str = "it";
        Intrinsics.checkExpressionValueIsNotNull(store, str);
        if (!store.isReady()) {
            store = null;
        }
        if (store != null) {
            Intrinsics.checkExpressionValueIsNotNull(store, "store");
            Collection rooms = store.getRooms();
            Intrinsics.checkExpressionValueIsNotNull(rooms, "store.rooms");
            Iterable iterable = rooms;
            Collection arrayList = new ArrayList();
            for (Object next : iterable) {
                Room room = (Room) next;
                Intrinsics.checkExpressionValueIsNotNull(room, str);
                if (true ^ room.isInvited()) {
                    arrayList.add(next);
                }
            }
            Iterable<Room> iterable2 = (List) arrayList;
            Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
            for (Room room2 : iterable2) {
                Intrinsics.checkExpressionValueIsNotNull(room2, BingRule.KIND_ROOM);
                arrayList2.add(Boolean.valueOf(clearExpiredRoomContentsFromStore(store, room2)));
            }
            Iterable iterable3 = (List) arrayList2;
            boolean z = false;
            if (!(iterable3 instanceof Collection) || !((Collection) iterable3).isEmpty()) {
                Iterator it = iterable3.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (((Boolean) it.next()).booleanValue()) {
                            z = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            if (z) {
                store.commit();
            }
        }
    }

    public static final boolean clearExpiredRoomContents(MXSession mXSession, Room room) {
        Intrinsics.checkParameterIsNotNull(mXSession, "session");
        Intrinsics.checkParameterIsNotNull(room, BingRule.KIND_ROOM);
        IMXStore store = mXSession.getDataHandler().getStore();
        Intrinsics.checkExpressionValueIsNotNull(store, "it");
        if (!store.isReady()) {
            store = null;
        }
        if (store == null) {
            return false;
        }
        Intrinsics.checkExpressionValueIsNotNull(store, "store");
        boolean clearExpiredRoomContentsFromStore = clearExpiredRoomContentsFromStore(store, room);
        if (!clearExpiredRoomContentsFromStore) {
            return clearExpiredRoomContentsFromStore;
        }
        store.commit();
        return clearExpiredRoomContentsFromStore;
    }

    private static final boolean clearExpiredRoomContentsFromStore(IMXStore iMXStore, Room room) {
        long millis = TimeUnit.DAYS.toMillis((long) getRoomRetention(room));
        Collection<Event> roomMessages = iMXStore.getRoomMessages(room.getRoomId());
        boolean z = false;
        if (roomMessages != null) {
            for (Event event : roomMessages) {
                if (event.stateKey == null) {
                    if (System.currentTimeMillis() - event.getOriginServerTs() <= millis) {
                        break;
                    }
                    iMXStore.deleteEvent(event);
                    z = true;
                }
            }
        }
        return z;
    }

    public static final long convertDaysToMs(int i) {
        return TimeUnit.DAYS.toMillis((long) i);
    }

    public static final int convertMsToDays(long j) {
        return (int) TimeUnit.MILLISECONDS.toDays(j);
    }
}
