package org.matrix.androidsdk.crypto.model.crypto;

import java.io.Serializable;
import java.util.List;

public class EncryptedFileKey implements Serializable {
    public String alg;
    public Boolean ext;
    public String k;
    public List<String> key_ops;
    public String kty;

    public EncryptedFileKey deepCopy() {
        EncryptedFileKey encryptedFileKey = new EncryptedFileKey();
        encryptedFileKey.alg = this.alg;
        encryptedFileKey.ext = this.ext;
        encryptedFileKey.key_ops = this.key_ops;
        encryptedFileKey.kty = this.kty;
        encryptedFileKey.k = this.k;
        return encryptedFileKey;
    }
}
