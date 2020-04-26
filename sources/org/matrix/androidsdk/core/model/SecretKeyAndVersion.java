package org.matrix.androidsdk.core.model;

import javax.crypto.SecretKey;

public class SecretKeyAndVersion {
    private final int androidVersionWhenTheKeyHasBeenGenerated;
    private final SecretKey secretKey;

    public SecretKeyAndVersion(SecretKey secretKey2, int i) {
        this.secretKey = secretKey2;
        this.androidVersionWhenTheKeyHasBeenGenerated = i;
    }

    public SecretKey getSecretKey() {
        return this.secretKey;
    }

    public int getAndroidVersionWhenTheKeyHasBeenGenerated() {
        return this.androidVersionWhenTheKeyHasBeenGenerated;
    }
}
