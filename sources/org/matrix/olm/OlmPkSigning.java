package org.matrix.olm;

import android.util.Log;
import java.util.Arrays;

public class OlmPkSigning {
    private static final String LOG_TAG = "OlmPkSigning";
    private transient long mNativeId;

    private native long createNewPkSigningJni();

    public static native byte[] generateSeedJni();

    private native byte[] pkSignJni(byte[] bArr);

    private native void releasePkSigningJni();

    public static native int seedLength();

    public native byte[] setKeyFromSeedJni(byte[] bArr);

    public OlmPkSigning() throws OlmException {
        try {
            this.mNativeId = createNewPkSigningJni();
        } catch (Exception e) {
            throw new OlmException(OlmException.EXCEPTION_CODE_PK_SIGNING_CREATION, e.getMessage());
        }
    }

    public void releaseSigning() {
        if (0 != this.mNativeId) {
            releasePkSigningJni();
        }
        this.mNativeId = 0;
    }

    public boolean isReleased() {
        return 0 == this.mNativeId;
    }

    public static byte[] generateSeed() throws OlmException {
        try {
            return generateSeedJni();
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## generateSeed(): failed ");
            sb.append(e.getMessage());
            Log.e(LOG_TAG, sb.toString());
            throw new OlmException(OlmException.EXCEPTION_CODE_PK_SIGNING_GENERATE_SEED, e.getMessage());
        }
    }

    public String initWithSeed(byte[] bArr) throws OlmException {
        try {
            return new String(setKeyFromSeedJni(bArr), "UTF-8");
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## initWithSeed(): failed ");
            sb.append(e.getMessage());
            Log.e(LOG_TAG, sb.toString());
            throw new OlmException(OlmException.EXCEPTION_CODE_PK_SIGNING_INIT_WITH_SEED, e.getMessage());
        }
    }

    public String sign(String str) throws OlmException {
        String str2 = "UTF-8";
        byte[] bArr = null;
        if (str == null) {
            return null;
        }
        try {
            bArr = str.getBytes(str2);
            String str3 = new String(pkSignJni(bArr), str2);
            if (bArr != null) {
                Arrays.fill(bArr, 0);
            }
            return str3;
        } catch (Exception e) {
            String str4 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## pkSign(): failed ");
            sb.append(e.getMessage());
            Log.e(str4, sb.toString());
            throw new OlmException(OlmException.EXCEPTION_CODE_PK_SIGNING_SIGN, e.getMessage());
        } catch (Throwable th) {
            if (bArr != null) {
                Arrays.fill(bArr, 0);
            }
            throw th;
        }
    }
}
