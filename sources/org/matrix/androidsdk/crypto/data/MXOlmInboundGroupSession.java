package org.matrix.androidsdk.crypto.data;

import java.io.Serializable;
import java.util.Map;
import org.matrix.androidsdk.core.Log;
import org.matrix.olm.OlmInboundGroupSession;

public class MXOlmInboundGroupSession implements Serializable {
    private static final String LOG_TAG = "OlmInboundGroupSession";
    public Map<String, String> mKeysClaimed;
    public String mRoomId;
    public String mSenderKey;
    public OlmInboundGroupSession mSession;

    public MXOlmInboundGroupSession(String str) {
        try {
            this.mSession = new OlmInboundGroupSession(str);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot create : ");
            sb.append(e.getMessage());
            Log.e(LOG_TAG, sb.toString(), e);
        }
    }
}
