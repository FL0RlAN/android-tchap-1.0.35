package org.matrix.olm;

import android.util.Log;
import java.util.Arrays;

public class OlmPkDecryption {
    private static final String LOG_TAG = "OlmPkDecryption";
    private transient long mNativeId;

    private native long createNewPkDecryptionJni();

    private native byte[] decryptJni(OlmPkMessage olmPkMessage);

    private native byte[] generateKeyJni();

    private native byte[] privateKeyJni();

    public static native int privateKeyLength();

    private native void releasePkDecryptionJni();

    private native byte[] setPrivateKeyJni(byte[] bArr);

    public OlmPkDecryption() throws OlmException {
        try {
            this.mNativeId = createNewPkDecryptionJni();
        } catch (Exception e) {
            throw new OlmException(OlmException.EXCEPTION_CODE_PK_DECRYPTION_CREATION, e.getMessage());
        }
    }

    public void releaseDecryption() {
        if (0 != this.mNativeId) {
            releasePkDecryptionJni();
        }
        this.mNativeId = 0;
    }

    public boolean isReleased() {
        return 0 == this.mNativeId;
    }

    public String setPrivateKey(byte[] bArr) throws OlmException {
        try {
            return new String(setPrivateKeyJni(bArr), "UTF-8");
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## setPrivateKey(): failed ");
            sb.append(e.getMessage());
            Log.e(LOG_TAG, sb.toString());
            throw new OlmException(OlmException.EXCEPTION_CODE_PK_DECRYPTION_SET_PRIVATE_KEY, e.getMessage());
        }
    }

    public String generateKey() throws OlmException {
        try {
            return new String(generateKeyJni(), "UTF-8");
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## setRecipientKey(): failed ");
            sb.append(e.getMessage());
            Log.e(LOG_TAG, sb.toString());
            throw new OlmException(OlmException.EXCEPTION_CODE_PK_DECRYPTION_GENERATE_KEY, e.getMessage());
        }
    }

    public byte[] privateKey() throws OlmException {
        try {
            return privateKeyJni();
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## privateKey(): failed ");
            sb.append(e.getMessage());
            Log.e(LOG_TAG, sb.toString());
            throw new OlmException(OlmException.EXCEPTION_CODE_PK_DECRYPTION_PRIVATE_KEY, e.getMessage());
        }
    }

    public String decrypt(OlmPkMessage olmPkMessage) throws OlmException {
        if (olmPkMessage == null) {
            return null;
        }
        byte[] decryptJni = decryptJni(olmPkMessage);
        try {
            String str = new String(decryptJni, "UTF-8");
            Arrays.fill(decryptJni, 0);
            return str;
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## pkDecrypt(): failed ");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString());
            throw new OlmException(OlmException.EXCEPTION_CODE_PK_DECRYPTION_DECRYPT, e.getMessage());
        } catch (Throwable th) {
            Arrays.fill(decryptJni, 0);
            throw th;
        }
    }
}
