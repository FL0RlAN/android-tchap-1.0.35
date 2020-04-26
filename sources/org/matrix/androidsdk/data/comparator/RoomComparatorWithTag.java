package org.matrix.androidsdk.data.comparator;

import java.util.Comparator;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomTag;

public class RoomComparatorWithTag implements Comparator<Room> {
    private final String mTag;

    public RoomComparatorWithTag(String str) {
        this.mTag = str;
    }

    public int compare(Room room, Room room2) {
        RoomTag roomTag = room.getAccountData().roomTag(this.mTag);
        RoomTag roomTag2 = room2.getAccountData().roomTag(this.mTag);
        if (roomTag == null || roomTag.mOrder == null || roomTag2 == null || roomTag2.mOrder == null) {
            return (roomTag == null || roomTag.mOrder == null) ? -1 : 1;
        }
        return Double.compare(roomTag.mOrder.doubleValue(), roomTag2.mOrder.doubleValue());
    }
}
