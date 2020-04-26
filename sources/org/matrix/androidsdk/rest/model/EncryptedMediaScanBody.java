package org.matrix.androidsdk.rest.model;

import com.google.gson.annotations.SerializedName;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;

public class EncryptedMediaScanBody {
    @SerializedName("file")
    public EncryptedFileInfo encryptedFileInfo;
}
