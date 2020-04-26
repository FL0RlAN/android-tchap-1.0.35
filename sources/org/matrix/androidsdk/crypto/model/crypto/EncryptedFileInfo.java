package org.matrix.androidsdk.crypto.model.crypto;

import java.io.Serializable;
import java.util.Map;

public class EncryptedFileInfo implements Serializable {
    public Map<String, String> hashes;
    public String iv;
    public EncryptedFileKey key;
    public String mimetype;
    public String url;
    public String v;

    public EncryptedFileInfo deepCopy() {
        EncryptedFileInfo encryptedFileInfo = new EncryptedFileInfo();
        encryptedFileInfo.url = this.url;
        encryptedFileInfo.mimetype = this.mimetype;
        EncryptedFileKey encryptedFileKey = this.key;
        if (encryptedFileKey != null) {
            encryptedFileInfo.key = encryptedFileKey.deepCopy();
        }
        encryptedFileInfo.iv = this.iv;
        encryptedFileInfo.hashes = this.hashes;
        return encryptedFileInfo;
    }
}
