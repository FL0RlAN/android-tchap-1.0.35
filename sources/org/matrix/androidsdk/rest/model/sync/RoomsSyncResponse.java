package org.matrix.androidsdk.rest.model.sync;

import java.util.Map;

public class RoomsSyncResponse {
    public Map<String, InvitedRoomSync> invite;
    public Map<String, RoomSync> join;
    public Map<String, RoomSync> leave;
}
