package org.matrix.androidsdk.crypto.rest.model.crypto;

import org.matrix.androidsdk.crypto.model.crypto.RoomKeyRequestBody;

public class RoomKeyShareRequest extends RoomKeyShare {
    public RoomKeyRequestBody body;

    public RoomKeyShareRequest() {
        this.action = RoomKeyShare.ACTION_SHARE_REQUEST;
    }
}
