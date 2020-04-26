package org.matrix.androidsdk.crypto.cryptostore.db.model;

public final class UserEntityFields {
    public static final String DEVICE_TRACKING_STATUS = "deviceTrackingStatus";
    public static final String USER_ID = "userId";

    public static final class DEVICES {
        public static final String $ = "devices";
        public static final String DEVICE_ID = "devices.deviceId";
        public static final String DEVICE_INFO_DATA = "devices.deviceInfoData";
        public static final String IDENTITY_KEY = "devices.identityKey";
        public static final String PRIMARY_KEY = "devices.primaryKey";
        public static final String USERS = "devices.users";
    }
}
