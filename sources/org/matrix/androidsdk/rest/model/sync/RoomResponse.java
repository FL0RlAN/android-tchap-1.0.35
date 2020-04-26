package org.matrix.androidsdk.rest.model.sync;

import java.util.List;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.TokensChunkEvents;

public class RoomResponse {
    public List<Event> accountData;
    public Event invite;
    public String inviter;
    public String membership;
    public TokensChunkEvents messages;
    public List<Event> presence;
    public List<Event> receipts;
    public String roomId;
    public List<Event> state;
    public String visibility;
}
