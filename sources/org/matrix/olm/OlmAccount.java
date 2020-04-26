package org.matrix.olm;

import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import org.json.JSONObject;

public class OlmAccount extends CommonSerializeUtils implements Serializable {
    public static final String JSON_KEY_FINGER_PRINT_KEY = "ed25519";
    public static final String JSON_KEY_IDENTITY_KEY = "curve25519";
    public static final String JSON_KEY_ONE_TIME_KEY = "curve25519";
    private static final String LOG_TAG = "OlmAccount";
    private static final long serialVersionUID = 3497486121598434824L;
    private transient long mNativeId;

    private native long createNewAccountJni();

    private native long deserializeJni(byte[] bArr, byte[] bArr2);

    private native void generateOneTimeKeysJni(int i);

    private native byte[] identityKeysJni();

    private native void markOneTimeKeysAsPublishedJni();

    private native long maxOneTimeKeysJni();

    private native byte[] oneTimeKeysJni();

    private native void releaseAccountJni();

    private native void removeOneTimeKeysJni(long j);

    private native byte[] serializeJni(byte[] bArr);

    private native byte[] signMessageJni(byte[] bArr);

    public OlmAccount() throws OlmException {
        try {
            this.mNativeId = createNewAccountJni();
        } catch (Exception e) {
            throw new OlmException(10, e.getMessage());
        }
    }

    /* access modifiers changed from: 0000 */
    public long getOlmAccountId() {
        return this.mNativeId;
    }

    public void releaseAccount() {
        if (0 != this.mNativeId) {
            releaseAccountJni();
        }
        this.mNativeId = 0;
    }

    public boolean isReleased() {
        return 0 == this.mNativeId;
    }

    public Map<String, String> identityKeys() throws OlmException {
        JSONObject jSONObject;
        String str = LOG_TAG;
        try {
            byte[] identityKeysJni = identityKeysJni();
            if (identityKeysJni != null) {
                try {
                    jSONObject = new JSONObject(new String(identityKeysJni, "UTF-8"));
                } catch (Exception e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("## identityKeys(): Exception - Msg=");
                    sb.append(e.getMessage());
                    Log.e(str, sb.toString());
                }
            } else {
                Log.e(str, "## identityKeys(): Failure - identityKeysJni()=null");
                jSONObject = null;
            }
            return OlmUtility.toStringMap(jSONObject);
        } catch (Exception e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## identityKeys(): Failure - ");
            sb2.append(e2.getMessage());
            Log.e(str, sb2.toString());
            throw new OlmException(102, e2.getMessage());
        }
    }

    public long maxOneTimeKeys() {
        return maxOneTimeKeysJni();
    }

    public void generateOneTimeKeys(int i) throws OlmException {
        try {
            generateOneTimeKeysJni(i);
        } catch (Exception e) {
            throw new OlmException(103, e.getMessage());
        }
    }

    public Map<String, Map<String, String>> oneTimeKeys() throws OlmException {
        JSONObject jSONObject;
        try {
            byte[] oneTimeKeysJni = oneTimeKeysJni();
            String str = LOG_TAG;
            if (oneTimeKeysJni != null) {
                try {
                    jSONObject = new JSONObject(new String(oneTimeKeysJni, "UTF-8"));
                } catch (Exception e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("## oneTimeKeys(): Exception - Msg=");
                    sb.append(e.getMessage());
                    Log.e(str, sb.toString());
                }
            } else {
                Log.e(str, "## oneTimeKeys(): Failure - identityKeysJni()=null");
                jSONObject = null;
            }
            return OlmUtility.toStringMapMap(jSONObject);
        } catch (Exception e2) {
            throw new OlmException(104, e2.getMessage());
        }
    }

    public void removeOneTimeKeys(OlmSession olmSession) throws OlmException {
        if (olmSession != null) {
            try {
                removeOneTimeKeysJni(olmSession.getOlmSessionId());
            } catch (Exception e) {
                throw new OlmException(105, e.getMessage());
            }
        }
    }

    public void markOneTimeKeysAsPublished() throws OlmException {
        try {
            markOneTimeKeysAsPublishedJni();
        } catch (Exception e) {
            throw new OlmException(106, e.getMessage());
        }
    }

    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r1v1, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r1v2, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r1v3 */
    /* JADX WARNING: type inference failed for: r5v2, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r1v5 */
    /* JADX WARNING: type inference failed for: r1v6 */
    /* JADX WARNING: type inference failed for: r1v7, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r1v8 */
    /* JADX WARNING: type inference failed for: r1v9 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v0
  assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], ?[OBJECT, ARRAY], java.lang.String]
  uses: [java.lang.String, ?[int, boolean, OBJECT, ARRAY, byte, short, char], byte[]]
  mth insns count: 26
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0035  */
    /* JADX WARNING: Unknown variable types count: 4 */
    public String signMessage(String str) throws OlmException {
        ? r1;
        String str2 = "UTF-8";
        ? r12 = 0;
        if (str != null) {
            try {
                ? bytes = str.getBytes(str2);
                if (bytes != 0) {
                    try {
                        byte[] signMessageJni = signMessageJni(bytes);
                        if (signMessageJni != null) {
                            r12 = new String(signMessageJni, str2);
                        }
                    } catch (Exception e) {
                        e = e;
                        r12 = bytes;
                        try {
                            throw new OlmException(107, e.getMessage());
                        } catch (Throwable th) {
                            th = th;
                            r1 = r12;
                            if (r1 != 0) {
                                Arrays.fill(r1, 0);
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        r1 = bytes;
                        if (r1 != 0) {
                        }
                        throw th;
                    }
                }
                if (bytes != 0) {
                    Arrays.fill(bytes, 0);
                }
            } catch (Exception e2) {
                e = e2;
                throw new OlmException(107, e.getMessage());
            }
        }
        return r12;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        serialize(objectOutputStream);
    }

    private void readObject(ObjectInputStream objectInputStream) throws Exception {
        deserialize(objectInputStream);
    }

    /* access modifiers changed from: protected */
    public byte[] serialize(byte[] bArr, StringBuffer stringBuffer) {
        String str = LOG_TAG;
        if (stringBuffer == null) {
            Log.e(str, "## serialize(): invalid parameter - aErrorMsg=null");
        } else if (bArr == null) {
            stringBuffer.append("Invalid input parameters in serializeDataWithKey()");
        } else {
            stringBuffer.setLength(0);
            try {
                return serializeJni(bArr);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("## serialize() failed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString());
                stringBuffer.append(e.getMessage());
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void deserialize(byte[] bArr, byte[] bArr2) throws Exception {
        String str;
        String str2 = LOG_TAG;
        if (bArr == null || bArr2 == null) {
            Log.e(str2, "## deserialize(): invalid input parameters");
            str = "invalid input parameters";
        } else {
            try {
                this.mNativeId = deserializeJni(bArr, bArr2);
                str = null;
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("## deserialize() failed ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString());
                str = e.getMessage();
            }
        }
        if (!TextUtils.isEmpty(str)) {
            releaseAccount();
            throw new OlmException(101, str);
        }
    }
}
