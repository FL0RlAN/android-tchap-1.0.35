package org.matrix.androidsdk.rest.model.sync;

import org.matrix.androidsdk.crypto.interfaces.CryptoDeviceListResponse;
import org.matrix.androidsdk.crypto.interfaces.CryptoDeviceOneTimeKeysCountSyncResponse;
import org.matrix.androidsdk.crypto.interfaces.CryptoSyncResponse;
import org.matrix.androidsdk.rest.model.group.GroupsSyncResponse;

public class SyncResponse implements CryptoSyncResponse {
    public AccountData accountData;
    public DeviceListResponse deviceLists;
    public DeviceOneTimeKeysCountSyncResponse deviceOneTimeKeysCount;
    public GroupsSyncResponse groups;
    public String nextBatch;
    public PresenceSyncResponse presence;
    public RoomsSyncResponse rooms;
    public ToDeviceSyncResponse toDevice;

    public CryptoDeviceListResponse getDeviceLists() {
        return this.deviceLists;
    }

    public CryptoDeviceOneTimeKeysCountSyncResponse getDeviceOneTimeKeysCount() {
        return this.deviceOneTimeKeysCount;
    }
}
