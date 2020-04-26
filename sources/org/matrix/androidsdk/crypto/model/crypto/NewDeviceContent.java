package org.matrix.androidsdk.crypto.model.crypto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NewDeviceContent {
    private static final String LOG_TAG = "NewDeviceContent";
    @SerializedName("device_id")
    public String deviceId;
    public List<String> rooms;
}
