package org.matrix.androidsdk.crypto.rest.model.crypto;

import com.google.gson.annotations.SerializedName;

public class RoomKeyShare implements SendToDeviceObject {
    public static final String ACTION_SHARE_CANCELLATION = "request_cancellation";
    public static final String ACTION_SHARE_REQUEST = "request";
    public String action;
    @SerializedName("request_id")
    public String requestId;
    @SerializedName("requesting_device_id")
    public String requestingDeviceId;
}
