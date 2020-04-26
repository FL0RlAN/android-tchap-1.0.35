package org.matrix.androidsdk.crypto;

public class MXDecryptionException extends Exception {
    private MXCryptoError mCryptoError;

    public MXDecryptionException(MXCryptoError mXCryptoError) {
        this.mCryptoError = mXCryptoError;
    }

    public MXCryptoError getCryptoError() {
        return this.mCryptoError;
    }

    public String getMessage() {
        MXCryptoError mXCryptoError = this.mCryptoError;
        if (mXCryptoError != null) {
            return mXCryptoError.getMessage();
        }
        return super.getMessage();
    }

    public String getLocalizedMessage() {
        MXCryptoError mXCryptoError = this.mCryptoError;
        if (mXCryptoError != null) {
            return mXCryptoError.getLocalizedMessage();
        }
        return super.getLocalizedMessage();
    }
}
