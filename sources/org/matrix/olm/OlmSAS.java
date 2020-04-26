package org.matrix.olm;

import android.util.Log;
import java.io.UnsupportedEncodingException;

public class OlmSAS {
    private static final String LOG_TAG = OlmSAS.class.getName();
    private transient long mNativeId;
    private String theirPublicKey = null;

    private native byte[] calculateMacJni(byte[] bArr, byte[] bArr2);

    private native byte[] calculateMacLongKdfJni(byte[] bArr, byte[] bArr2);

    private native long createNewSASJni();

    private native byte[] generateShortCodeJni(byte[] bArr, int i);

    private native byte[] getPubKeyJni();

    private native void releaseSASJni();

    private native void setTheirPubKey(byte[] bArr);

    public OlmSAS() throws OlmException {
        try {
            this.mNativeId = createNewSASJni();
        } catch (Exception e) {
            throw new OlmException(OlmException.EXCEPTION_CODE_SAS_CREATION, e.getMessage());
        }
    }

    public String getPublicKey() throws OlmException {
        try {
            byte[] pubKeyJni = getPubKeyJni();
            if (pubKeyJni != null) {
                return new String(pubKeyJni, "UTF-8");
            }
            return null;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## sessionIdentifier(): ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString());
            throw new OlmException(OlmException.EXCEPTION_CODE_SAS_ERROR, e.getMessage());
        }
    }

    public void setTheirPublicKey(String str) throws OlmException {
        try {
            setTheirPubKey(str.getBytes("UTF-8"));
            this.theirPublicKey = str;
        } catch (UnsupportedEncodingException e) {
            throw new OlmException(OlmException.EXCEPTION_CODE_SAS_ERROR, e.getMessage());
        }
    }

    public byte[] generateShortCode(String str, int i) throws OlmException {
        String str2 = this.theirPublicKey;
        if (str2 == null || str2.isEmpty()) {
            throw new OlmException(OlmException.EXCEPTION_CODE_SAS_MISSING_THEIR_PKEY, "call setTheirPublicKey first");
        }
        try {
            return generateShortCodeJni(str.getBytes("UTF-8"), i);
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## sessionIdentifier(): ");
            sb.append(e.getMessage());
            Log.e(str3, sb.toString());
            throw new OlmException(OlmException.EXCEPTION_CODE_SAS_GENERATE_SHORT_CODE, e.getMessage());
        }
    }

    public String calculateMac(String str, String str2) throws OlmException {
        String str3 = "UTF-8";
        try {
            byte[] calculateMacJni = calculateMacJni(str.getBytes(str3), str2.getBytes(str3));
            if (calculateMacJni != null) {
                return new String(calculateMacJni, str3);
            }
            return null;
        } catch (UnsupportedEncodingException e) {
            throw new OlmException(OlmException.EXCEPTION_CODE_SAS_ERROR, e.getMessage());
        }
    }

    public String calculateMacLongKdf(String str, String str2) throws OlmException {
        String str3 = "UTF-8";
        try {
            byte[] calculateMacLongKdfJni = calculateMacLongKdfJni(str.getBytes(str3), str2.getBytes(str3));
            if (calculateMacLongKdfJni != null) {
                return new String(calculateMacLongKdfJni, str3);
            }
            return null;
        } catch (UnsupportedEncodingException e) {
            throw new OlmException(OlmException.EXCEPTION_CODE_SAS_ERROR, e.getMessage());
        }
    }

    public void releaseSas() {
        if (0 != this.mNativeId) {
            releaseSASJni();
        }
        this.mNativeId = 0;
    }
}
