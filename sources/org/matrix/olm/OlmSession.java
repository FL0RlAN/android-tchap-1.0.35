package org.matrix.olm;

import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class OlmSession extends CommonSerializeUtils implements Serializable {
    private static final String LOG_TAG = "OlmSession";
    private static final long serialVersionUID = -8975488639186976419L;
    private transient long mNativeId;

    private native long createNewSessionJni();

    private native byte[] decryptMessageJni(OlmMessage olmMessage);

    private native long deserializeJni(byte[] bArr, byte[] bArr2);

    private native byte[] encryptMessageJni(byte[] bArr, OlmMessage olmMessage);

    private native byte[] getSessionIdentifierJni();

    private native void initInboundSessionFromIdKeyJni(long j, byte[] bArr, byte[] bArr2);

    private native void initInboundSessionJni(long j, byte[] bArr);

    private native void initOutboundSessionJni(long j, byte[] bArr, byte[] bArr2);

    private native boolean matchesInboundSessionFromIdKeyJni(byte[] bArr, byte[] bArr2);

    private native boolean matchesInboundSessionJni(byte[] bArr);

    private native void releaseSessionJni();

    private native byte[] serializeJni(byte[] bArr);

    public OlmSession() throws OlmException {
        try {
            this.mNativeId = createNewSessionJni();
        } catch (Exception e) {
            throw new OlmException(OlmException.EXCEPTION_CODE_INIT_SESSION_CREATION, e.getMessage());
        }
    }

    /* access modifiers changed from: 0000 */
    public long getOlmSessionId() {
        return this.mNativeId;
    }

    public void releaseSession() {
        if (0 != this.mNativeId) {
            releaseSessionJni();
        }
        this.mNativeId = 0;
    }

    public boolean isReleased() {
        return 0 == this.mNativeId;
    }

    public void initOutboundSession(OlmAccount olmAccount, String str, String str2) throws OlmException {
        String str3 = "UTF-8";
        String str4 = LOG_TAG;
        if (olmAccount == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            Log.e(str4, "## initOutboundSession(): invalid input parameters");
            throw new OlmException(OlmException.EXCEPTION_CODE_SESSION_INIT_OUTBOUND_SESSION, "invalid input parameters");
        }
        try {
            initOutboundSessionJni(olmAccount.getOlmAccountId(), str.getBytes(str3), str2.getBytes(str3));
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## initOutboundSession(): ");
            sb.append(e.getMessage());
            Log.e(str4, sb.toString());
            throw new OlmException(OlmException.EXCEPTION_CODE_SESSION_INIT_OUTBOUND_SESSION, e.getMessage());
        }
    }

    public void initInboundSession(OlmAccount olmAccount, String str) throws OlmException {
        String str2 = LOG_TAG;
        if (olmAccount == null || TextUtils.isEmpty(str)) {
            Log.e(str2, "## initInboundSession(): invalid input parameters");
            throw new OlmException(OlmException.EXCEPTION_CODE_SESSION_INIT_INBOUND_SESSION, "invalid input parameters");
        }
        try {
            initInboundSessionJni(olmAccount.getOlmAccountId(), str.getBytes("UTF-8"));
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## initInboundSession(): ");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString());
            throw new OlmException(OlmException.EXCEPTION_CODE_SESSION_INIT_INBOUND_SESSION, e.getMessage());
        }
    }

    public void initInboundSessionFrom(OlmAccount olmAccount, String str, String str2) throws OlmException {
        String str3 = "UTF-8";
        String str4 = LOG_TAG;
        if (olmAccount == null || TextUtils.isEmpty(str2)) {
            Log.e(str4, "## initInboundSessionFrom(): invalid input parameters");
            throw new OlmException(OlmException.EXCEPTION_CODE_SESSION_INIT_INBOUND_SESSION_FROM, "invalid input parameters");
        }
        try {
            initInboundSessionFromIdKeyJni(olmAccount.getOlmAccountId(), str.getBytes(str3), str2.getBytes(str3));
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## initInboundSessionFrom(): ");
            sb.append(e.getMessage());
            Log.e(str4, sb.toString());
            throw new OlmException(OlmException.EXCEPTION_CODE_SESSION_INIT_INBOUND_SESSION_FROM, e.getMessage());
        }
    }

    public String sessionIdentifier() throws OlmException {
        try {
            byte[] sessionIdentifierJni = getSessionIdentifierJni();
            if (sessionIdentifierJni != null) {
                return new String(sessionIdentifierJni, "UTF-8");
            }
            return null;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## sessionIdentifier(): ");
            sb.append(e.getMessage());
            Log.e(LOG_TAG, sb.toString());
            throw new OlmException(OlmException.EXCEPTION_CODE_SESSION_SESSION_IDENTIFIER, e.getMessage());
        }
    }

    public boolean matchesInboundSession(String str) {
        try {
            return matchesInboundSessionJni(str.getBytes("UTF-8"));
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## matchesInboundSession(): failed ");
            sb.append(e.getMessage());
            Log.e(LOG_TAG, sb.toString());
            return false;
        }
    }

    public boolean matchesInboundSessionFrom(String str, String str2) {
        String str3 = "UTF-8";
        try {
            return matchesInboundSessionFromIdKeyJni(str.getBytes(str3), str2.getBytes(str3));
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## matchesInboundSessionFrom(): failed ");
            sb.append(e.getMessage());
            Log.e(LOG_TAG, sb.toString());
            return false;
        }
    }

    public OlmMessage encryptMessage(String str) throws OlmException {
        String str2 = "UTF-8";
        if (str == null) {
            return null;
        }
        OlmMessage olmMessage = new OlmMessage();
        try {
            byte[] bytes = str.getBytes(str2);
            byte[] encryptMessageJni = encryptMessageJni(bytes, olmMessage);
            Arrays.fill(bytes, 0);
            if (encryptMessageJni != null) {
                olmMessage.mCipherText = new String(encryptMessageJni, str2);
            }
            return olmMessage;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## encryptMessage(): failed ");
            sb.append(e.getMessage());
            Log.e(LOG_TAG, sb.toString());
            throw new OlmException(404, e.getMessage());
        }
    }

    public String decryptMessage(OlmMessage olmMessage) throws OlmException {
        if (olmMessage == null) {
            return null;
        }
        try {
            byte[] decryptMessageJni = decryptMessageJni(olmMessage);
            String str = new String(decryptMessageJni, "UTF-8");
            Arrays.fill(decryptMessageJni, 0);
            return str;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("## decryptMessage(): failed ");
            sb.append(e.getMessage());
            Log.e(LOG_TAG, sb.toString());
            throw new OlmException(OlmException.EXCEPTION_CODE_SESSION_DECRYPT_MESSAGE, e.getMessage());
        }
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
            Log.e(str, "## serializeDataWithKey(): invalid parameter - aErrorMsg=null");
        } else if (bArr == null) {
            stringBuffer.append("Invalid input parameters in serializeDataWithKey()");
        } else {
            stringBuffer.setLength(0);
            try {
                return serializeJni(bArr);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("## serializeDataWithKey(): failed ");
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
            releaseSession();
            throw new OlmException(101, str);
        }
    }
}
