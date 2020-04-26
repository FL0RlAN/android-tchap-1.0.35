package org.matrix.androidsdk.rest.model;

import com.google.gson.annotations.SerializedName;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedBodyFileInfo;

public class EncryptedMediaScanEncryptedBody {
    @SerializedName("encrypted_body")
    public EncryptedBodyFileInfo encryptedBodyFileInfo;
}
