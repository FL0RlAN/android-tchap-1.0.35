package org.matrix.androidsdk.rest.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TokensChunkEvents extends TokensChunkResponse<Event> {
    @SerializedName("state")
    public List<Event> stateEvents;
}
