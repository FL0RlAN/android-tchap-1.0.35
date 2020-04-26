package org.matrix.androidsdk.rest.model.sync;

import org.matrix.androidsdk.crypto.interfaces.CryptoDeviceOneTimeKeysCountSyncResponse;

public class DeviceOneTimeKeysCountSyncResponse implements CryptoDeviceOneTimeKeysCountSyncResponse {
    public Integer signed_curve25519;

    public Integer getSignedCurve25519() {
        return this.signed_curve25519;
    }
}
