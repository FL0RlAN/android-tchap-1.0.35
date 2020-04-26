package org.matrix.androidsdk.crypto.rest.model.crypto;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class EncryptedMessage implements SendToDeviceObject {
    public String algorithm;
    @SerializedName("ciphertext")
    public Map<String, Object> cipherText;
    @SerializedName("sender_key")
    public String senderKey;
}
