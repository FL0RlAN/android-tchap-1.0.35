package org.matrix.androidsdk.rest.model.group;

import java.io.Serializable;
import java.util.List;

public class GroupSummaryRoomsSection implements Serializable {
    public List<String> rooms;
    public Integer totalRoomCountEstimate;
}
