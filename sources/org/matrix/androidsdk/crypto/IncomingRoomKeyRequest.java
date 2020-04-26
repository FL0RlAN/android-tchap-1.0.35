package org.matrix.androidsdk.crypto;

import java.io.Serializable;
import org.matrix.androidsdk.crypto.interfaces.CryptoEvent;
import org.matrix.androidsdk.crypto.model.crypto.RoomKeyRequestBody;
import org.matrix.androidsdk.crypto.rest.model.crypto.RoomKeyShareRequest;

public class IncomingRoomKeyRequest implements Serializable {
    public String mDeviceId;
    public transient Runnable mIgnore;
    public RoomKeyRequestBody mRequestBody;
    public String mRequestId;
    public transient Runnable mShare;
    public String mUserId;

    public IncomingRoomKeyRequest(CryptoEvent cryptoEvent) {
        this.mUserId = cryptoEvent.getSender();
        RoomKeyShareRequest roomKeyShareRequest = cryptoEvent.toRoomKeyShareRequest();
        this.mDeviceId = roomKeyShareRequest.requestingDeviceId;
        this.mRequestId = roomKeyShareRequest.requestId;
        this.mRequestBody = roomKeyShareRequest.body != null ? roomKeyShareRequest.body : new RoomKeyRequestBody();
    }

    public IncomingRoomKeyRequest() {
    }
}
