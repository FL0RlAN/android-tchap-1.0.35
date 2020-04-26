package org.matrix.androidsdk.rest.model.sync;

import java.util.List;
import org.matrix.androidsdk.crypto.interfaces.CryptoDeviceListResponse;

public class DeviceListResponse implements CryptoDeviceListResponse {
    public List<String> changed;
    public List<String> left;

    public List<String> getChanged() {
        return this.changed;
    }

    public List<String> getLeft() {
        return this.left;
    }
}
