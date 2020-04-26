package org.matrix.androidsdk.data.room;

import android.content.Context;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import org.matrix.androidsdk.R;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.comparator.Comparators;
import org.matrix.androidsdk.rest.model.RoomMember;

public class RoomDisplayNameResolver {
    private static final String LOG_TAG = RoomDisplayNameResolver.class.getSimpleName();
    private final Room mRoom;

    public RoomDisplayNameResolver(Room room) {
        this.mRoom = room;
    }

    public String resolve(Context context) {
        return resolve(context, context.getString(R.string.room_displayname_empty_room));
    }

    public String resolve(Context context, String str) {
        try {
            RoomState state = this.mRoom.getState();
            if (!TextUtils.isEmpty(state.name)) {
                return state.name;
            }
            if (!TextUtils.isEmpty(state.getCanonicalAlias())) {
                return state.getCanonicalAlias();
            }
            if (state.aliases != null && !state.aliases.isEmpty()) {
                return (String) state.aliases.get(0);
            }
            ArrayList arrayList = new ArrayList();
            RoomMember roomMember = null;
            if (!this.mRoom.getDataHandler().isLazyLoadingEnabled() || !this.mRoom.isJoined() || this.mRoom.getRoomSummary() == null) {
                for (RoomMember roomMember2 : state.getDisplayableLoadedMembers()) {
                    if (!TextUtils.equals(roomMember2.membership, "leave")) {
                        if (TextUtils.equals(roomMember2.getUserId(), this.mRoom.getDataHandler().getUserId())) {
                            roomMember = roomMember2;
                        } else {
                            arrayList.add(roomMember2);
                        }
                    }
                }
                Collections.sort(arrayList, Comparators.ascComparator);
            } else {
                for (String member : this.mRoom.getRoomSummary().getHeroes()) {
                    RoomMember member2 = state.getMember(member);
                    if (member2 != null) {
                        arrayList.add(member2);
                    }
                }
            }
            int size = arrayList.size();
            if (this.mRoom.isInvited()) {
                if (roomMember == null || arrayList.isEmpty() || TextUtils.isEmpty(roomMember.mSender)) {
                    str = context.getString(R.string.room_displayname_room_invite);
                } else {
                    str = context.getString(R.string.room_displayname_invite_from, new Object[]{state.getMemberName(roomMember.mSender)});
                }
            } else if (size != 0) {
                if (size == 1) {
                    str = state.getMemberName(((RoomMember) arrayList.get(0)).getUserId());
                } else if (size == 2) {
                    RoomMember roomMember3 = (RoomMember) arrayList.get(0);
                    RoomMember roomMember4 = (RoomMember) arrayList.get(1);
                    str = context.getString(R.string.room_displayname_two_members, new Object[]{state.getMemberName(roomMember3.getUserId()), state.getMemberName(roomMember4.getUserId())});
                } else {
                    RoomMember roomMember5 = (RoomMember) arrayList.get(0);
                    str = context.getResources().getQuantityString(R.plurals.room_displayname_three_and_more_members, this.mRoom.getNumberOfJoinedMembers() - 1, new Object[]{state.getMemberName(roomMember5.getUserId()), Integer.valueOf(this.mRoom.getNumberOfJoinedMembers() - 1)});
                }
            }
            return str;
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## Computing room display name failed ");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString(), e);
            return this.mRoom.getRoomId();
        }
    }
}
