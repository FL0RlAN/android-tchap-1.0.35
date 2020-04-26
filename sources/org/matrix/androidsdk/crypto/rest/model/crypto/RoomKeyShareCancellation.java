package org.matrix.androidsdk.crypto.rest.model.crypto;

public class RoomKeyShareCancellation extends RoomKeyShare {
    public RoomKeyShareCancellation() {
        this.action = RoomKeyShare.ACTION_SHARE_CANCELLATION;
    }
}
