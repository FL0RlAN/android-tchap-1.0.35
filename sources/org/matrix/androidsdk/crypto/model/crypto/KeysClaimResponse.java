package org.matrix.androidsdk.crypto.model.crypto;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class KeysClaimResponse {
    @SerializedName("one_time_keys")
    public Map<String, Map<String, Map<String, Map<String, Object>>>> oneTimeKeys;
}
