package org.matrix.androidsdk.crypto.model.crypto;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;

public class KeysQueryResponse {
    @SerializedName("device_keys")
    public Map<String, Map<String, MXDeviceInfo>> deviceKeys;
    public Map<String, Map<String, Object>> failures;
}
