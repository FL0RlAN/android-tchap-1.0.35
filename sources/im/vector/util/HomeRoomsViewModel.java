package im.vector.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomAccountData;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.RoomTag;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.rest.model.RoomTombstoneContent;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\u000fB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u0002J\u0006\u0010\u000e\u001a\u00020\u0006R\u001a\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lim/vector/util/HomeRoomsViewModel;", "", "session", "Lorg/matrix/androidsdk/MXSession;", "(Lorg/matrix/androidsdk/MXSession;)V", "result", "Lim/vector/util/HomeRoomsViewModel$Result;", "getResult", "()Lim/vector/util/HomeRoomsViewModel$Result;", "setResult", "(Lim/vector/util/HomeRoomsViewModel$Result;)V", "getJoinedRooms", "", "Lorg/matrix/androidsdk/data/Room;", "update", "Result", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: HomeRoomsViewModel.kt */
public final class HomeRoomsViewModel {
    private Result result;
    private final MXSession session;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001BU\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0002\u0010\tJ\u000f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\u000f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\u000f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\u000f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\u000f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003JY\u0010\u0015\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003J\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003J\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003J\t\u0010\u001c\u001a\u00020\u001dHÖ\u0001J\t\u0010\u001e\u001a\u00020\u001fHÖ\u0001R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000bR\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000b¨\u0006 "}, d2 = {"Lim/vector/util/HomeRoomsViewModel$Result;", "", "favourites", "", "Lorg/matrix/androidsdk/data/Room;", "directChats", "otherRooms", "lowPriorities", "serverNotices", "(Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;)V", "getDirectChats", "()Ljava/util/List;", "getFavourites", "getLowPriorities", "getOtherRooms", "getServerNotices", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "getDirectChatsWithFavorites", "getJoinedRooms", "getOtherRoomsWithFavorites", "hashCode", "", "toString", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: HomeRoomsViewModel.kt */
    public static final class Result {
        private final List<Room> directChats;
        private final List<Room> favourites;
        private final List<Room> lowPriorities;
        private final List<Room> otherRooms;
        private final List<Room> serverNotices;

        public Result() {
            this(null, null, null, null, null, 31, null);
        }

        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.matrix.androidsdk.data.Room>, for r4v0, types: [java.util.List] */
        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.matrix.androidsdk.data.Room>, for r5v0, types: [java.util.List] */
        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.matrix.androidsdk.data.Room>, for r6v0, types: [java.util.List] */
        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.matrix.androidsdk.data.Room>, for r7v0, types: [java.util.List] */
        /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<org.matrix.androidsdk.data.Room>, for r8v0, types: [java.util.List] */
        public static /* synthetic */ Result copy$default(Result result, List<Room> list, List<Room> list2, List<Room> list3, List<Room> list4, List<Room> list5, int i, Object obj) {
            if ((i & 1) != 0) {
                list = result.favourites;
            }
            if ((i & 2) != 0) {
                list2 = result.directChats;
            }
            List list6 = list2;
            if ((i & 4) != 0) {
                list3 = result.otherRooms;
            }
            List list7 = list3;
            if ((i & 8) != 0) {
                list4 = result.lowPriorities;
            }
            List list8 = list4;
            if ((i & 16) != 0) {
                list5 = result.serverNotices;
            }
            return result.copy(list, list6, list7, list8, list5);
        }

        public final List<Room> component1() {
            return this.favourites;
        }

        public final List<Room> component2() {
            return this.directChats;
        }

        public final List<Room> component3() {
            return this.otherRooms;
        }

        public final List<Room> component4() {
            return this.lowPriorities;
        }

        public final List<Room> component5() {
            return this.serverNotices;
        }

        public final Result copy(List<? extends Room> list, List<? extends Room> list2, List<? extends Room> list3, List<? extends Room> list4, List<? extends Room> list5) {
            Intrinsics.checkParameterIsNotNull(list, "favourites");
            Intrinsics.checkParameterIsNotNull(list2, "directChats");
            Intrinsics.checkParameterIsNotNull(list3, "otherRooms");
            Intrinsics.checkParameterIsNotNull(list4, "lowPriorities");
            Intrinsics.checkParameterIsNotNull(list5, "serverNotices");
            Result result = new Result(list, list2, list3, list4, list5);
            return result;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0038, code lost:
            if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2.serverNotices, (java.lang.Object) r3.serverNotices) != false) goto L_0x003d;
         */
        public boolean equals(Object obj) {
            if (this != obj) {
                if (obj instanceof Result) {
                    Result result = (Result) obj;
                    if (Intrinsics.areEqual((Object) this.favourites, (Object) result.favourites)) {
                        if (Intrinsics.areEqual((Object) this.directChats, (Object) result.directChats)) {
                            if (Intrinsics.areEqual((Object) this.otherRooms, (Object) result.otherRooms)) {
                                if (Intrinsics.areEqual((Object) this.lowPriorities, (Object) result.lowPriorities)) {
                                }
                            }
                        }
                    }
                }
                return false;
            }
            return true;
        }

        public int hashCode() {
            List<Room> list = this.favourites;
            int i = 0;
            int hashCode = (list != null ? list.hashCode() : 0) * 31;
            List<Room> list2 = this.directChats;
            int hashCode2 = (hashCode + (list2 != null ? list2.hashCode() : 0)) * 31;
            List<Room> list3 = this.otherRooms;
            int hashCode3 = (hashCode2 + (list3 != null ? list3.hashCode() : 0)) * 31;
            List<Room> list4 = this.lowPriorities;
            int hashCode4 = (hashCode3 + (list4 != null ? list4.hashCode() : 0)) * 31;
            List<Room> list5 = this.serverNotices;
            if (list5 != null) {
                i = list5.hashCode();
            }
            return hashCode4 + i;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Result(favourites=");
            sb.append(this.favourites);
            sb.append(", directChats=");
            sb.append(this.directChats);
            sb.append(", otherRooms=");
            sb.append(this.otherRooms);
            sb.append(", lowPriorities=");
            sb.append(this.lowPriorities);
            sb.append(", serverNotices=");
            sb.append(this.serverNotices);
            sb.append(")");
            return sb.toString();
        }

        public Result(List<? extends Room> list, List<? extends Room> list2, List<? extends Room> list3, List<? extends Room> list4, List<? extends Room> list5) {
            Intrinsics.checkParameterIsNotNull(list, "favourites");
            Intrinsics.checkParameterIsNotNull(list2, "directChats");
            Intrinsics.checkParameterIsNotNull(list3, "otherRooms");
            Intrinsics.checkParameterIsNotNull(list4, "lowPriorities");
            Intrinsics.checkParameterIsNotNull(list5, "serverNotices");
            this.favourites = list;
            this.directChats = list2;
            this.otherRooms = list3;
            this.lowPriorities = list4;
            this.serverNotices = list5;
        }

        public /* synthetic */ Result(List list, List list2, List list3, List list4, List list5, int i, DefaultConstructorMarker defaultConstructorMarker) {
            if ((i & 1) != 0) {
                list = CollectionsKt.emptyList();
            }
            if ((i & 2) != 0) {
                list2 = CollectionsKt.emptyList();
            }
            List list6 = list2;
            if ((i & 4) != 0) {
                list3 = CollectionsKt.emptyList();
            }
            List list7 = list3;
            if ((i & 8) != 0) {
                list4 = CollectionsKt.emptyList();
            }
            List list8 = list4;
            if ((i & 16) != 0) {
                list5 = CollectionsKt.emptyList();
            }
            this(list, list6, list7, list8, list5);
        }

        public final List<Room> getFavourites() {
            return this.favourites;
        }

        public final List<Room> getDirectChats() {
            return this.directChats;
        }

        public final List<Room> getOtherRooms() {
            return this.otherRooms;
        }

        public final List<Room> getLowPriorities() {
            return this.lowPriorities;
        }

        public final List<Room> getServerNotices() {
            return this.serverNotices;
        }

        public final List<Room> getDirectChatsWithFavorites() {
            Collection collection = this.directChats;
            Iterable iterable = this.favourites;
            Collection arrayList = new ArrayList();
            for (Object next : iterable) {
                if (((Room) next).isDirect()) {
                    arrayList.add(next);
                }
            }
            return CollectionsKt.plus(collection, (Iterable) (List) arrayList);
        }

        public final List<Room> getOtherRoomsWithFavorites() {
            Collection collection = this.otherRooms;
            Iterable iterable = this.favourites;
            Collection arrayList = new ArrayList();
            for (Object next : iterable) {
                if (!((Room) next).isDirect()) {
                    arrayList.add(next);
                }
            }
            return CollectionsKt.plus(collection, (Iterable) (List) arrayList);
        }

        public final List<Room> getJoinedRooms() {
            return CollectionsKt.plus((Collection) CollectionsKt.plus((Collection) CollectionsKt.plus((Collection) this.favourites, (Iterable) this.directChats), (Iterable) this.lowPriorities), (Iterable) this.otherRooms);
        }
    }

    public HomeRoomsViewModel(MXSession mXSession) {
        Intrinsics.checkParameterIsNotNull(mXSession, "session");
        this.session = mXSession;
        Result result2 = new Result(null, null, null, null, null, 31, null);
        this.result = result2;
    }

    public final Result getResult() {
        return this.result;
    }

    public final void setResult(Result result2) {
        Intrinsics.checkParameterIsNotNull(result2, "<set-?>");
        this.result = result2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0037, code lost:
        if (r7 != null) goto L_0x003e;
     */
    public final Result update() {
        Set set;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        for (Room room : getJoinedRooms()) {
            RoomAccountData accountData = room.getAccountData();
            if (accountData != null) {
                set = accountData.getKeys();
            }
            set = SetsKt.emptySet();
            Intrinsics.checkExpressionValueIsNotNull(set, "room.accountData?.keys ?: emptySet()");
            if (set.contains(RoomTag.ROOM_TAG_SERVER_NOTICE)) {
                arrayList5.add(room);
            } else if (set.contains(RoomTag.ROOM_TAG_FAVOURITE)) {
                arrayList.add(room);
            } else if (set.contains(RoomTag.ROOM_TAG_LOW_PRIORITY)) {
                arrayList4.add(room);
            } else if (RoomUtils.isDirectChat(this.session, room.getRoomId())) {
                arrayList2.add(room);
            } else {
                arrayList3.add(room);
            }
        }
        Result result2 = new Result(arrayList, arrayList2, arrayList3, arrayList4, arrayList5);
        this.result = result2;
        Log.d("HomeRoomsViewModel", this.result.toString());
        return this.result;
    }

    private final List<Room> getJoinedRooms() {
        IMXStore store = this.session.getDataHandler().getStore();
        Intrinsics.checkExpressionValueIsNotNull(store, "session.dataHandler.store");
        Collection rooms = store.getRooms();
        Intrinsics.checkExpressionValueIsNotNull(rooms, "session.dataHandler.store.rooms");
        Iterable iterable = rooms;
        Collection arrayList = new ArrayList();
        for (Object next : iterable) {
            Room room = (Room) next;
            Intrinsics.checkExpressionValueIsNotNull(room, "it");
            boolean isJoined = room.isJoined();
            RoomState state = room.getState();
            Intrinsics.checkExpressionValueIsNotNull(state, "it.state");
            RoomTombstoneContent roomTombstoneContent = state.getRoomTombstoneContent();
            Room room2 = null;
            if ((roomTombstoneContent != null ? roomTombstoneContent.replacementRoom : null) != null) {
                room2 = this.session.getDataHandler().getRoom(roomTombstoneContent.replacementRoom);
            }
            boolean z = false;
            boolean isJoined2 = room2 != null ? room2.isJoined() : false;
            if (isJoined && !isJoined2 && !room.isConferenceUserRoom()) {
                z = true;
            }
            if (z) {
                arrayList.add(next);
            }
        }
        return (List) arrayList;
    }
}
