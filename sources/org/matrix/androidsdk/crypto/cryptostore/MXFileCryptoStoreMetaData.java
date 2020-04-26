package org.matrix.androidsdk.crypto.cryptostore;

import java.io.Serializable;

public class MXFileCryptoStoreMetaData implements Serializable {
    public boolean mDeviceAnnounced;
    public String mDeviceId;
    public String mUserId;
    public int mVersion;
}
