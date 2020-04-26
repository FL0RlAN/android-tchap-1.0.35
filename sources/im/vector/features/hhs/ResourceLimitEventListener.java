package im.vector.features.hhs;

import androidx.core.app.NotificationCompat;
import im.vector.features.hhs.LimitResourceState.Exceeded;
import im.vector.features.hhs.LimitResourceState.Normal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomAccountData;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.RoomTag;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.RoomPinnedEventsContent;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001\"B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u000f\u001a\u00020\u0010H\u0002J\u000e\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u0002J\u0018\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u0010\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u001b\u001a\u00020\u000eH\u0002J\u0018\u0010\u001c\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\u0019H\u0002J&\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u00192\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00190\rH\u0002R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u000e¢\u0006\u0002\n\u0000¨\u0006#"}, d2 = {"Lim/vector/features/hhs/ResourceLimitEventListener;", "Lorg/matrix/androidsdk/listeners/MXEventListener;", "dataHandler", "Lorg/matrix/androidsdk/MXDataHandler;", "callback", "Lim/vector/features/hhs/ResourceLimitEventListener$Callback;", "(Lorg/matrix/androidsdk/MXDataHandler;Lim/vector/features/hhs/ResourceLimitEventListener$Callback;)V", "<set-?>", "Lim/vector/features/hhs/LimitResourceState;", "limitResourceState", "getLimitResourceState", "()Lim/vector/features/hhs/LimitResourceState;", "serverNoticesRooms", "", "Lorg/matrix/androidsdk/data/Room;", "loadAndProcessServerNoticeRooms", "", "loadServerNoticeRooms", "onLiveEvent", "event", "Lorg/matrix/androidsdk/rest/model/Event;", "roomState", "Lorg/matrix/androidsdk/data/RoomState;", "onRoomTagEvent", "roomId", "", "processPinnedEvents", "room", "retrieveResourceLimitExceededEvent", "eventId", "shouldStateBeBackToNormal", "", "state", "pinnedEvents", "Callback", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: ResourceLimitEventListener.kt */
public final class ResourceLimitEventListener extends MXEventListener {
    /* access modifiers changed from: private */
    public final Callback callback;
    private final MXDataHandler dataHandler;
    /* access modifiers changed from: private */
    public LimitResourceState limitResourceState = Normal.INSTANCE;
    private List<? extends Room> serverNoticesRooms = CollectionsKt.emptyList();

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&¨\u0006\u0004"}, d2 = {"Lim/vector/features/hhs/ResourceLimitEventListener$Callback;", "", "onResourceLimitStateChanged", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: ResourceLimitEventListener.kt */
    public interface Callback {
        void onResourceLimitStateChanged();
    }

    public ResourceLimitEventListener(MXDataHandler mXDataHandler, Callback callback2) {
        Intrinsics.checkParameterIsNotNull(mXDataHandler, "dataHandler");
        this.dataHandler = mXDataHandler;
        this.callback = callback2;
        loadAndProcessServerNoticeRooms();
    }

    public final LimitResourceState getLimitResourceState() {
        return this.limitResourceState;
    }

    public void onRoomTagEvent(String str) {
        Intrinsics.checkParameterIsNotNull(str, CryptoRoomEntityFields.ROOM_ID);
        loadAndProcessServerNoticeRooms();
    }

    public void onLiveEvent(Event event, RoomState roomState) {
        Intrinsics.checkParameterIsNotNull(event, NotificationCompat.CATEGORY_EVENT);
        Intrinsics.checkParameterIsNotNull(roomState, "roomState");
        if (Intrinsics.areEqual((Object) event.type, (Object) Event.EVENT_TYPE_STATE_PINNED_EVENT)) {
            Iterable<Room> iterable = this.serverNoticesRooms;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            for (Room roomId : iterable) {
                arrayList.add(roomId.getRoomId());
            }
            if (((List) arrayList).contains(event.roomId)) {
                Room room = this.dataHandler.getRoom(event.roomId);
                Intrinsics.checkExpressionValueIsNotNull(room, "dataHandler.getRoom(event.roomId)");
                processPinnedEvents(room);
            }
        }
    }

    private final void loadAndProcessServerNoticeRooms() {
        this.serverNoticesRooms = loadServerNoticeRooms();
        for (Room processPinnedEvents : this.serverNoticesRooms) {
            processPinnedEvents(processPinnedEvents);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0044, code lost:
        if (r3 != null) goto L_0x004b;
     */
    private final List<Room> loadServerNoticeRooms() {
        Set set;
        Log.v("ResourceLimitEventListener", "Load server notice rooms");
        IMXStore store = this.dataHandler.getStore();
        Intrinsics.checkExpressionValueIsNotNull(store, "dataHandler.store");
        Collection rooms = store.getRooms();
        Intrinsics.checkExpressionValueIsNotNull(rooms, "dataHandler.store.rooms");
        Iterable iterable = rooms;
        Collection arrayList = new ArrayList();
        for (Object next : iterable) {
            Room room = (Room) next;
            Intrinsics.checkExpressionValueIsNotNull(room, "it");
            RoomAccountData accountData = room.getAccountData();
            if (accountData != null) {
                set = accountData.getKeys();
            }
            set = SetsKt.emptySet();
            Intrinsics.checkExpressionValueIsNotNull(set, "it.accountData?.keys ?: emptySet()");
            if (set.contains(RoomTag.ROOM_TAG_SERVER_NOTICE)) {
                arrayList.add(next);
            }
        }
        return (List) arrayList;
    }

    private final void processPinnedEvents(Room room) {
        Log.v("ResourceLimitEventListener", "Process pinned events");
        RoomState state = room.getState();
        Intrinsics.checkExpressionValueIsNotNull(state, "room.state");
        RoomPinnedEventsContent roomPinnedEventsContent = state.getRoomPinnedEventsContent();
        if (roomPinnedEventsContent != null) {
            List<String> list = roomPinnedEventsContent.pinned;
            if (list != null) {
                LimitResourceState limitResourceState2 = this.limitResourceState;
                String roomId = room.getRoomId();
                String str = "room.roomId";
                Intrinsics.checkExpressionValueIsNotNull(roomId, str);
                if (shouldStateBeBackToNormal(limitResourceState2, roomId, list)) {
                    this.limitResourceState = Normal.INSTANCE;
                    Callback callback2 = this.callback;
                    if (callback2 != null) {
                        callback2.onResourceLimitStateChanged();
                        return;
                    }
                    return;
                }
                for (String str2 : CollectionsKt.take(list, 2)) {
                    String roomId2 = room.getRoomId();
                    Intrinsics.checkExpressionValueIsNotNull(roomId2, str);
                    Intrinsics.checkExpressionValueIsNotNull(str2, "it");
                    retrieveResourceLimitExceededEvent(roomId2, str2);
                }
            }
        }
    }

    private final boolean shouldStateBeBackToNormal(LimitResourceState limitResourceState2, String str, List<String> list) {
        if (limitResourceState2 instanceof Exceeded) {
            Exceeded exceeded = (Exceeded) limitResourceState2;
            return Intrinsics.areEqual((Object) str, (Object) exceeded.getRoomId()) && !list.contains(exceeded.getEventId());
        } else if (limitResourceState2 instanceof Normal) {
            return false;
        } else {
            throw new NoWhenBranchMatchedException();
        }
    }

    private final void retrieveResourceLimitExceededEvent(String str, String str2) {
        this.dataHandler.getDataRetriever().getEvent(this.dataHandler.getStore(), str, str2, new ResourceLimitEventListener$retrieveResourceLimitExceededEvent$1(this, str, str2));
    }
}
