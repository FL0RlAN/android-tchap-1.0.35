package org.matrix.androidsdk.crypto.model.crypto;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class RoomKeyRequestBody implements Serializable {
    public String algorithm;
    @SerializedName("room_id")
    public String roomId;
    @SerializedName("sender_key")
    public String senderKey;
    @SerializedName("session_id")
    public String sessionId;
}
