package org.matrix.olm;

import android.text.TextUtils;
import android.util.Log;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kotlin.jvm.internal.ByteCompanionObject;
import org.json.JSONObject;

public class OlmUtility {
    private static final String LOG_TAG = "OlmUtility";
    public static final int RANDOM_KEY_SIZE = 32;
    private long mNativeId;

    private native long createUtilityJni();

    private native void releaseUtilityJni();

    private native byte[] sha256Jni(byte[] bArr);

    private native String verifyEd25519SignatureJni(byte[] bArr, byte[] bArr2, byte[] bArr3);

    public OlmUtility() throws OlmException {
        initUtility();
    }

    private void initUtility() throws OlmException {
        try {
            this.mNativeId = createUtilityJni();
        } catch (Exception e) {
            throw new OlmException(500, e.getMessage());
        }
    }

    public void releaseUtility() {
        if (0 != this.mNativeId) {
            releaseUtilityJni();
        }
        this.mNativeId = 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0033  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0061 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0062  */
    public void verifyEd25519Signature(String str, String str2, String str3) throws OlmException {
        String str4;
        String str5 = LOG_TAG;
        String str6 = "UTF-8";
        byte[] bArr = null;
        try {
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                if (!TextUtils.isEmpty(str3)) {
                    bArr = str3.getBytes(str6);
                    str4 = verifyEd25519SignatureJni(str.getBytes(str6), str2.getBytes(str6), bArr);
                    if (bArr != null) {
                        Arrays.fill(bArr, 0);
                    }
                    if (TextUtils.isEmpty(str4)) {
                        throw new OlmException(501, str4);
                    }
                    return;
                }
            }
            Log.e(str5, "## verifyEd25519Signature(): invalid input parameters");
            str4 = "JAVA sanity check failure - invalid input parameters";
            if (bArr != null) {
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## verifyEd25519Signature(): failed ");
            sb.append(e.getMessage());
            Log.e(str5, sb.toString());
            str4 = e.getMessage();
            if (0 != 0) {
                Arrays.fill(null, 0);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                Arrays.fill(null, 0);
            }
            throw th;
        }
        if (TextUtils.isEmpty(str4)) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x003d  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0044  */
    /* JADX WARNING: Removed duplicated region for block: B:26:? A[RETURN, SYNTHETIC] */
    public String sha256(String str) {
        byte[] bArr;
        String str2 = "UTF-8";
        if (str == null) {
            return null;
        }
        try {
            bArr = str.getBytes(str2);
            try {
                String str3 = new String(sha256Jni(bArr), str2);
                if (bArr != null) {
                    Arrays.fill(bArr, 0);
                }
                return str3;
            } catch (Exception e) {
                e = e;
                String str4 = LOG_TAG;
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("## sha256(): failed ");
                    sb.append(e.getMessage());
                    Log.e(str4, sb.toString());
                    if (bArr != null) {
                    }
                } catch (Throwable th) {
                    th = th;
                    if (bArr != null) {
                        Arrays.fill(bArr, 0);
                    }
                    throw th;
                }
            }
        } catch (Exception e2) {
            e = e2;
            bArr = null;
            String str42 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## sha256(): failed ");
            sb2.append(e.getMessage());
            Log.e(str42, sb2.toString());
            if (bArr != null) {
                return null;
            }
            Arrays.fill(bArr, 0);
            return null;
        } catch (Throwable th2) {
            th = th2;
            bArr = null;
            if (bArr != null) {
            }
            throw th;
        }
    }

    public static byte[] getRandomKey() {
        byte[] bArr = new byte[32];
        new SecureRandom().nextBytes(bArr);
        for (int i = 0; i < 32; i++) {
            bArr[i] = (byte) (bArr[i] & ByteCompanionObject.MAX_VALUE);
        }
        return bArr;
    }

    public boolean isReleased() {
        return 0 == this.mNativeId;
    }

    public static Map<String, String> toStringMap(JSONObject jSONObject) {
        String str = LOG_TAG;
        if (jSONObject == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String str2 = (String) keys.next();
            try {
                Object obj = jSONObject.get(str2);
                if (obj instanceof String) {
                    hashMap.put(str2, (String) obj);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("## toStringMap(): unexpected type ");
                    sb.append(obj.getClass());
                    Log.e(str, sb.toString());
                }
            } catch (Exception e) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## toStringMap(): failed ");
                sb2.append(e.getMessage());
                Log.e(str, sb2.toString());
            }
        }
        return hashMap;
    }

    public static Map<String, Map<String, String>> toStringMapMap(JSONObject jSONObject) {
        String str = LOG_TAG;
        if (jSONObject == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String str2 = (String) keys.next();
            try {
                Object obj = jSONObject.get(str2);
                if (obj instanceof JSONObject) {
                    hashMap.put(str2, toStringMap((JSONObject) obj));
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("## toStringMapMap(): unexpected type ");
                    sb.append(obj.getClass());
                    Log.e(str, sb.toString());
                }
            } catch (Exception e) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## toStringMapMap(): failed ");
                sb2.append(e.getMessage());
                Log.e(str, sb2.toString());
            }
        }
        return hashMap;
    }
}
