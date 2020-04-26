package org.matrix.androidsdk.crypto.data;

import android.text.TextUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.CryptoConstantsKt;
import org.matrix.androidsdk.crypto.MegolmSessionData;
import org.matrix.olm.OlmAccount;
import org.matrix.olm.OlmInboundGroupSession;

public class MXOlmInboundGroupSession2 implements Serializable {
    private static final String LOG_TAG = MXOlmInboundGroupSession2.class.getSimpleName();
    private static final long serialVersionUID = 201702011617L;
    public List<String> mForwardingCurve25519KeyChain = new ArrayList();
    public Map<String, String> mKeysClaimed;
    public String mRoomId;
    public String mSenderKey;
    public OlmInboundGroupSession mSession;

    public MXOlmInboundGroupSession2(MXOlmInboundGroupSession mXOlmInboundGroupSession) {
        this.mSession = mXOlmInboundGroupSession.mSession;
        this.mRoomId = mXOlmInboundGroupSession.mRoomId;
        this.mSenderKey = mXOlmInboundGroupSession.mSenderKey;
        this.mKeysClaimed = mXOlmInboundGroupSession.mKeysClaimed;
    }

    public MXOlmInboundGroupSession2(String str, boolean z) {
        if (!z) {
            try {
                this.mSession = new OlmInboundGroupSession(str);
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot create : ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        } else {
            this.mSession = OlmInboundGroupSession.importSession(str);
        }
    }

    public MXOlmInboundGroupSession2(MegolmSessionData megolmSessionData) throws Exception {
        try {
            this.mSession = OlmInboundGroupSession.importSession(megolmSessionData.sessionKey);
            if (TextUtils.equals(this.mSession.sessionIdentifier(), megolmSessionData.sessionId)) {
                this.mSenderKey = megolmSessionData.senderKey;
                this.mKeysClaimed = megolmSessionData.senderClaimedKeys;
                this.mRoomId = megolmSessionData.roomId;
                return;
            }
            throw new Exception("Mismatched group session Id");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public MegolmSessionData exportKeys() {
        MegolmSessionData megolmSessionData = new MegolmSessionData();
        try {
            if (this.mForwardingCurve25519KeyChain == null) {
                this.mForwardingCurve25519KeyChain = new ArrayList();
            }
            megolmSessionData.senderClaimedEd25519Key = (String) this.mKeysClaimed.get(OlmAccount.JSON_KEY_FINGER_PRINT_KEY);
            megolmSessionData.forwardingCurve25519KeyChain = new ArrayList(this.mForwardingCurve25519KeyChain);
            megolmSessionData.senderKey = this.mSenderKey;
            megolmSessionData.senderClaimedKeys = this.mKeysClaimed;
            megolmSessionData.roomId = this.mRoomId;
            megolmSessionData.sessionId = this.mSession.sessionIdentifier();
            megolmSessionData.sessionKey = this.mSession.export(this.mSession.getFirstKnownIndex());
            megolmSessionData.algorithm = CryptoConstantsKt.MXCRYPTO_ALGORITHM_MEGOLM;
            return megolmSessionData;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## export() : senderKey ");
            sb.append(this.mSenderKey);
            sb.append(" failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            return null;
        }
    }

    public Long getFirstKnownIndex() {
        OlmInboundGroupSession olmInboundGroupSession = this.mSession;
        if (olmInboundGroupSession != null) {
            try {
                return Long.valueOf(olmInboundGroupSession.getFirstKnownIndex());
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getFirstKnownIndex() : getFirstKnownIndex failed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
        return null;
    }

    public String exportSession(long j) {
        OlmInboundGroupSession olmInboundGroupSession = this.mSession;
        if (olmInboundGroupSession != null) {
            try {
                return olmInboundGroupSession.export(j);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## exportSession() : export failed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
        return null;
    }
}
