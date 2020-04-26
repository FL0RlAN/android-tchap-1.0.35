package org.matrix.androidsdk.crypto;

import org.matrix.androidsdk.crypto.interfaces.CryptoEvent;

public class IncomingRoomKeyRequestCancellation extends IncomingRoomKeyRequest {
    public IncomingRoomKeyRequestCancellation(CryptoEvent cryptoEvent) {
        super(cryptoEvent);
        this.mRequestBody = null;
    }
}
