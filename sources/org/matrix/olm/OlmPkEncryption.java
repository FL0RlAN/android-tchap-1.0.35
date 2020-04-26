package org.matrix.olm;

import android.util.Log;
import java.util.Arrays;

public class OlmPkEncryption {
    private static final String LOG_TAG = "OlmPkEncryption";
    private transient long mNativeId;

    private native long createNewPkEncryptionJni();

    private native byte[] encryptJni(byte[] bArr, OlmPkMessage olmPkMessage);

    private native void releasePkEncryptionJni();

    private native void setRecipientKeyJni(byte[] bArr);

    public OlmPkEncryption() throws OlmException {
        try {
            this.mNativeId = createNewPkEncryptionJni();
        } catch (Exception e) {
            throw new OlmException(600, e.getMessage());
        }
    }

    public void releaseEncryption() {
        if (0 != this.mNativeId) {
            releasePkEncryptionJni();
        }
        this.mNativeId = 0;
    }

    public boolean isReleased() {
        return 0 == this.mNativeId;
    }

    public void setRecipientKey(String str) throws OlmException {
        if (str != null) {
            try {
                setRecipientKeyJni(str.getBytes("UTF-8"));
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("## setRecipientKey(): failed ");
                sb.append(e.getMessage());
                Log.e(LOG_TAG, sb.toString());
                throw new OlmException(OlmException.EXCEPTION_CODE_PK_ENCRYPTION_SET_RECIPIENT_KEY, e.getMessage());
            }
        }
    }

    public OlmPkMessage encrypt(String str) throws OlmException {
        String str2 = "UTF-8";
        if (str == null) {
            return null;
        }
        OlmPkMessage olmPkMessage = new OlmPkMessage();
        try {
            byte[] bytes = str.getBytes(str2);
            byte[] encryptJni = encryptJni(bytes, olmPkMessage);
            if (encryptJni != null) {
                olmPkMessage.mCipherText = new String(encryptJni, str2);
            }
            if (bytes != null) {
                Arrays.fill(bytes, 0);
            }
            return olmPkMessage;
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## pkEncrypt(): failed ");
            sb.append(e.getMessage());
            Log.e(str3, sb.toString());
            throw new OlmException(OlmException.EXCEPTION_CODE_PK_ENCRYPTION_ENCRYPT, e.getMessage());
        } catch (Throwable th) {
            if (0 != 0) {
                Arrays.fill(null, 0);
            }
            throw th;
        }
    }
}
