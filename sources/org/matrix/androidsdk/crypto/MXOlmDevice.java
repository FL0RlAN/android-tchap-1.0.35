package org.matrix.androidsdk.crypto;

import android.text.TextUtils;
import com.google.gson.JsonParser;
import im.vector.activity.VectorUniversalLinkActivity;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.matrix.androidsdk.core.JsonUtility;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.StringUtilsKt;
import org.matrix.androidsdk.crypto.algorithms.MXDecryptionResult;
import org.matrix.androidsdk.crypto.cryptostore.IMXCryptoStore;
import org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2;
import org.matrix.androidsdk.crypto.data.MXOlmSession;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;
import org.matrix.olm.OlmAccount;
import org.matrix.olm.OlmInboundGroupSession.DecryptMessageResult;
import org.matrix.olm.OlmMessage;
import org.matrix.olm.OlmOutboundGroupSession;
import org.matrix.olm.OlmSession;
import org.matrix.olm.OlmUtility;

public class MXOlmDevice {
    private static final String LOG_TAG = MXOlmDevice.class.getSimpleName();
    private String mDeviceCurve25519Key;
    private String mDeviceEd25519Key;
    private final Map<String, Map<String, Boolean>> mInboundGroupSessionMessageIndexes;
    private MXCryptoError mInboundGroupSessionWithIdError = null;
    private OlmAccount mOlmAccount;
    private OlmUtility mOlmUtility;
    private final Map<String, OlmOutboundGroupSession> mOutboundGroupSessionStore;
    private final IMXCryptoStore mStore;

    public MXOlmDevice(IMXCryptoStore iMXCryptoStore) {
        this.mStore = iMXCryptoStore;
        this.mOlmAccount = this.mStore.getAccount();
        if (this.mOlmAccount == null) {
            Log.d(LOG_TAG, "MXOlmDevice : create a new olm account");
            try {
                this.mOlmAccount = new OlmAccount();
                this.mStore.storeAccount(this.mOlmAccount);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("MXOlmDevice : cannot initialize mOlmAccount ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        } else {
            Log.d(LOG_TAG, "MXOlmDevice : use an existing account");
        }
        try {
            this.mOlmUtility = new OlmUtility();
        } catch (Exception e2) {
            String str2 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## MXOlmDevice : OlmUtility failed with error ");
            sb2.append(e2.getMessage());
            Log.e(str2, sb2.toString(), e2);
            this.mOlmUtility = null;
        }
        this.mOutboundGroupSessionStore = new HashMap();
        try {
            this.mDeviceCurve25519Key = (String) this.mOlmAccount.identityKeys().get("curve25519");
        } catch (Exception e3) {
            String str3 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("## MXOlmDevice : cannot find curve25519 with error ");
            sb3.append(e3.getMessage());
            Log.e(str3, sb3.toString(), e3);
        }
        try {
            this.mDeviceEd25519Key = (String) this.mOlmAccount.identityKeys().get(OlmAccount.JSON_KEY_FINGER_PRINT_KEY);
        } catch (Exception e4) {
            String str4 = LOG_TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("## MXOlmDevice : cannot find ed25519 with error ");
            sb4.append(e4.getMessage());
            Log.e(str4, sb4.toString(), e4);
        }
        this.mInboundGroupSessionMessageIndexes = new HashMap();
    }

    public void release() {
        OlmAccount olmAccount = this.mOlmAccount;
        if (olmAccount != null) {
            olmAccount.releaseAccount();
        }
    }

    public String getDeviceCurve25519Key() {
        return this.mDeviceCurve25519Key;
    }

    public String getDeviceEd25519Key() {
        return this.mDeviceEd25519Key;
    }

    public String signMessage(String str) {
        try {
            return this.mOlmAccount.signMessage(str);
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## signMessage() : failed ");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString(), e);
            return null;
        }
    }

    public Map<String, Map<String, String>> getOneTimeKeys() {
        try {
            return this.mOlmAccount.oneTimeKeys();
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getOneTimeKeys() : failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            return null;
        }
    }

    public long getMaxNumberOfOneTimeKeys() {
        OlmAccount olmAccount = this.mOlmAccount;
        if (olmAccount != null) {
            return olmAccount.maxOneTimeKeys();
        }
        return -1;
    }

    public void markKeysAsPublished() {
        try {
            this.mOlmAccount.markOneTimeKeysAsPublished();
            this.mStore.storeAccount(this.mOlmAccount);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## markKeysAsPublished() : failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    public void generateOneTimeKeys(int i) {
        try {
            this.mOlmAccount.generateOneTimeKeys(i);
            this.mStore.storeAccount(this.mOlmAccount);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## generateOneTimeKeys() : failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0073  */
    public String createOutboundSession(String str, String str2) {
        OlmSession olmSession;
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## createOutboundSession() ; theirIdentityKey ");
        sb.append(str);
        sb.append(" theirOneTimeKey ");
        sb.append(str2);
        Log.d(str3, sb.toString());
        try {
            olmSession = new OlmSession();
            try {
                olmSession.initOutboundSession(this.mOlmAccount, str, str2);
                MXOlmSession mXOlmSession = new MXOlmSession(olmSession, 0);
                mXOlmSession.onMessageReceived();
                this.mStore.storeSession(mXOlmSession, str);
                String sessionIdentifier = olmSession.sessionIdentifier();
                String str4 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## createOutboundSession() ;  olmSession.sessionIdentifier: ");
                sb2.append(sessionIdentifier);
                Log.d(str4, sb2.toString());
                return sessionIdentifier;
            } catch (Exception e) {
                e = e;
                String str5 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("## createOutboundSession() failed ; ");
                sb3.append(e.getMessage());
                Log.e(str5, sb3.toString(), e);
                if (olmSession != null) {
                }
                return null;
            }
        } catch (Exception e2) {
            e = e2;
            olmSession = null;
            String str52 = LOG_TAG;
            StringBuilder sb32 = new StringBuilder();
            sb32.append("## createOutboundSession() failed ; ");
            sb32.append(e.getMessage());
            Log.e(str52, sb32.toString(), e);
            if (olmSession != null) {
                olmSession.releaseSession();
            }
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x00eb A[Catch:{ Exception -> 0x011f }] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00fa A[Catch:{ Exception -> 0x011f }] */
    public Map<String, String> createInboundSession(String str, int i, String str2) {
        CharSequence charSequence;
        String sessionIdentifier;
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## createInboundSession() : theirIdentityKey: ");
        sb.append(str);
        String sb2 = sb.toString();
        Log.d(str3, sb2);
        try {
            OlmSession olmSession = sb2;
            OlmSession olmSession2 = new OlmSession();
            try {
                olmSession = olmSession2;
                olmSession2.initInboundSessionFrom(this.mOlmAccount, str, str2);
            } catch (Exception e) {
                e = e;
                String str4 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("## createInboundSession() : the session creation failed ");
                sb3.append(e.getMessage());
                Log.e(str4, sb3.toString(), e);
                return null;
            }
            try {
                olmSession = olmSession2;
                String str5 = LOG_TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("## createInboundSession() : sessionId: ");
                sb4.append(olmSession2.sessionIdentifier());
                Log.d(str5, sb4.toString());
                olmSession = olmSession2;
                try {
                    this.mOlmAccount.removeOneTimeKeys(olmSession2);
                    this.mStore.storeAccount(this.mOlmAccount);
                } catch (Exception e2) {
                    olmSession = olmSession2;
                    String str6 = LOG_TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("## createInboundSession() : removeOneTimeKeys failed ");
                    sb5.append(e2.getMessage());
                    Log.e(str6, sb5.toString(), e2);
                }
                String str7 = LOG_TAG;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("## createInboundSession() : ciphertext: ");
                sb6.append(str2);
                Log.d(str7, sb6.toString());
                try {
                    String str8 = LOG_TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("## createInboundSession() :ciphertext: SHA256:");
                    sb7.append(this.mOlmUtility.sha256(URLEncoder.encode(str2, "utf-8")));
                    Log.d(str8, sb7.toString());
                } catch (Exception e3) {
                    olmSession = olmSession2;
                    Log.e(LOG_TAG, "## createInboundSession() :ciphertext: cannot encode ciphertext", e3);
                }
                OlmMessage olmMessage = new OlmMessage();
                olmMessage.mCipherText = str2;
                olmMessage.mType = (long) i;
                try {
                    charSequence = olmSession2.decryptMessage(olmMessage);
                    try {
                        MXOlmSession mXOlmSession = new MXOlmSession(olmSession2, 0);
                        mXOlmSession.onMessageReceived();
                        this.mStore.storeSession(mXOlmSession, str);
                    } catch (Exception e4) {
                        e = e4;
                    }
                } catch (Exception e5) {
                    e = e5;
                    charSequence = null;
                    olmSession = olmSession2;
                    String str9 = LOG_TAG;
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("## createInboundSession() : decryptMessage failed ");
                    sb8.append(e.getMessage());
                    Log.e(str9, sb8.toString(), e);
                    HashMap hashMap = new HashMap();
                    if (!TextUtils.isEmpty(charSequence)) {
                    }
                    sessionIdentifier = olmSession2.sessionIdentifier();
                    if (!TextUtils.isEmpty(sessionIdentifier)) {
                    }
                    return hashMap;
                }
                HashMap hashMap2 = new HashMap();
                if (!TextUtils.isEmpty(charSequence)) {
                    hashMap2.put("payload", charSequence);
                }
                sessionIdentifier = olmSession2.sessionIdentifier();
                if (!TextUtils.isEmpty(sessionIdentifier)) {
                    hashMap2.put(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_SESSION_ID, sessionIdentifier);
                }
                return hashMap2;
            } catch (Exception e6) {
                String str10 = LOG_TAG;
                StringBuilder sb9 = new StringBuilder();
                sb9.append("## createInboundSession() : OlmSession creation failed ");
                sb9.append(e6.getMessage());
                Log.e(str10, sb9.toString(), e6);
                if (olmSession != 0) {
                    olmSession.releaseSession();
                }
                return null;
            }
        } catch (Exception e7) {
            e = e7;
            String str42 = LOG_TAG;
            StringBuilder sb32 = new StringBuilder();
            sb32.append("## createInboundSession() : the session creation failed ");
            sb32.append(e.getMessage());
            Log.e(str42, sb32.toString(), e);
            return null;
        }
    }

    public Set<String> getSessionIds(String str) {
        return this.mStore.getDeviceSessionIds(str);
    }

    public String getSessionId(String str) {
        return this.mStore.getLastUsedSessionId(str);
    }

    public Map<String, Object> encryptMessage(String str, String str2, String str3) {
        MXOlmSession sessionForDevice = getSessionForDevice(str, str2);
        Map<String, Object> map = null;
        if (sessionForDevice == null) {
            return null;
        }
        try {
            String str4 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## encryptMessage() : olmSession.sessionIdentifier: ");
            sb.append(str2);
            Log.d(str4, sb.toString());
            OlmMessage encryptMessage = sessionForDevice.getOlmSession().encryptMessage(str3);
            this.mStore.storeSession(sessionForDevice, str);
            HashMap hashMap = new HashMap();
            try {
                hashMap.put("body", encryptMessage.mCipherText);
                hashMap.put(PasswordLoginParams.IDENTIFIER_KEY_TYPE, Long.valueOf(encryptMessage.mType));
                return hashMap;
            } catch (Exception e) {
                e = e;
                map = hashMap;
                String str5 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## encryptMessage() : failed ");
                sb2.append(e.getMessage());
                Log.e(str5, sb2.toString(), e);
                return map;
            }
        } catch (Exception e2) {
            e = e2;
            String str52 = LOG_TAG;
            StringBuilder sb22 = new StringBuilder();
            sb22.append("## encryptMessage() : failed ");
            sb22.append(e.getMessage());
            Log.e(str52, sb22.toString(), e);
            return map;
        }
    }

    public String decryptMessage(String str, int i, String str2, String str3) {
        MXOlmSession sessionForDevice = getSessionForDevice(str3, str2);
        String str4 = null;
        if (sessionForDevice == null) {
            return null;
        }
        OlmMessage olmMessage = new OlmMessage();
        olmMessage.mCipherText = str;
        olmMessage.mType = (long) i;
        try {
            str4 = sessionForDevice.getOlmSession().decryptMessage(olmMessage);
            sessionForDevice.onMessageReceived();
            this.mStore.storeSession(sessionForDevice, str3);
            return str4;
        } catch (Exception e) {
            String str5 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## decryptMessage() : decryptMessage failed ");
            sb.append(e.getMessage());
            Log.e(str5, sb.toString(), e);
            return str4;
        }
    }

    public boolean matchesSession(String str, String str2, int i, String str3) {
        boolean z = false;
        if (i != 0) {
            return false;
        }
        MXOlmSession sessionForDevice = getSessionForDevice(str, str2);
        if (sessionForDevice != null && sessionForDevice.getOlmSession().matchesInboundSession(str3)) {
            z = true;
        }
        return z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0034  */
    public String createOutboundGroupSession() {
        OlmOutboundGroupSession olmOutboundGroupSession;
        try {
            olmOutboundGroupSession = new OlmOutboundGroupSession();
            try {
                this.mOutboundGroupSessionStore.put(olmOutboundGroupSession.sessionIdentifier(), olmOutboundGroupSession);
                return olmOutboundGroupSession.sessionIdentifier();
            } catch (Exception e) {
                e = e;
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("createOutboundGroupSession ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
                if (olmOutboundGroupSession != null) {
                }
                return null;
            }
        } catch (Exception e2) {
            e = e2;
            olmOutboundGroupSession = null;
            String str2 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("createOutboundGroupSession ");
            sb2.append(e.getMessage());
            Log.e(str2, sb2.toString(), e);
            if (olmOutboundGroupSession != null) {
                olmOutboundGroupSession.releaseSession();
            }
            return null;
        }
    }

    public String getSessionKey(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                return ((OlmOutboundGroupSession) this.mOutboundGroupSessionStore.get(str)).sessionKey();
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getSessionKey() : failed ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
        return null;
    }

    public int getMessageIndex(String str) {
        if (!TextUtils.isEmpty(str)) {
            return ((OlmOutboundGroupSession) this.mOutboundGroupSessionStore.get(str)).messageIndex();
        }
        return 0;
    }

    public String encryptGroupMessage(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            try {
                return ((OlmOutboundGroupSession) this.mOutboundGroupSessionStore.get(str)).encryptMessage(str2);
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## encryptGroupMessage() : failed ");
                sb.append(e.getMessage());
                Log.e(str3, sb.toString(), e);
            }
        }
        return null;
    }

    public boolean addInboundGroupSession(String str, String str2, String str3, String str4, List<String> list, Map<String, String> map, boolean z) {
        MXOlmInboundGroupSession2 inboundGroupSession = getInboundGroupSession(str, str4, str3);
        MXOlmInboundGroupSession2 mXOlmInboundGroupSession2 = new MXOlmInboundGroupSession2(str2, z);
        if (inboundGroupSession != null) {
            String str5 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## addInboundGroupSession() : Update for megolm session ");
            sb.append(str4);
            sb.append("/");
            sb.append(str);
            Log.e(str5, sb.toString());
            Long firstKnownIndex = inboundGroupSession.getFirstKnownIndex();
            Long firstKnownIndex2 = mXOlmInboundGroupSession2.getFirstKnownIndex();
            if (firstKnownIndex2 != null && firstKnownIndex.longValue() <= firstKnownIndex2.longValue()) {
                if (mXOlmInboundGroupSession2.mSession != null) {
                    mXOlmInboundGroupSession2.mSession.releaseSession();
                }
                return false;
            }
        }
        if (mXOlmInboundGroupSession2.mSession == null) {
            Log.e(LOG_TAG, "## addInboundGroupSession : invalid session");
            return false;
        }
        try {
            if (!TextUtils.equals(mXOlmInboundGroupSession2.mSession.sessionIdentifier(), str)) {
                String str6 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## addInboundGroupSession : ERROR: Mismatched group session ID from senderKey: ");
                sb2.append(str4);
                Log.e(str6, sb2.toString());
                mXOlmInboundGroupSession2.mSession.releaseSession();
                return false;
            }
            mXOlmInboundGroupSession2.mSenderKey = str4;
            mXOlmInboundGroupSession2.mRoomId = str3;
            mXOlmInboundGroupSession2.mKeysClaimed = map;
            mXOlmInboundGroupSession2.mForwardingCurve25519KeyChain = list;
            this.mStore.storeInboundGroupSessions(Collections.singletonList(mXOlmInboundGroupSession2));
            return true;
        } catch (Exception e) {
            mXOlmInboundGroupSession2.mSession.releaseSession();
            String str7 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("## addInboundGroupSession : sessionIdentifier() failed ");
            sb3.append(e.getMessage());
            Log.e(str7, sb3.toString(), e);
            return false;
        }
    }

    public List<MXOlmInboundGroupSession2> importInboundGroupSessions(List<MegolmSessionData> list) {
        String str = "/";
        String str2 = "## importInboundGroupSession() : Update for megolm session ";
        ArrayList arrayList = new ArrayList(list.size());
        for (MegolmSessionData megolmSessionData : list) {
            String str3 = megolmSessionData.sessionId;
            String str4 = megolmSessionData.senderKey;
            String str5 = megolmSessionData.roomId;
            MXOlmInboundGroupSession2 mXOlmInboundGroupSession2 = null;
            try {
                mXOlmInboundGroupSession2 = new MXOlmInboundGroupSession2(megolmSessionData);
            } catch (Exception e) {
                String str6 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(str4);
                sb.append(str);
                sb.append(str3);
                Log.e(str6, sb.toString(), e);
            }
            if (mXOlmInboundGroupSession2 == null || mXOlmInboundGroupSession2.mSession == null) {
                Log.e(LOG_TAG, "## importInboundGroupSession : invalid session");
            } else {
                try {
                    if (!TextUtils.equals(mXOlmInboundGroupSession2.mSession.sessionIdentifier(), str3)) {
                        String str7 = LOG_TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("## importInboundGroupSession : ERROR: Mismatched group session ID from senderKey: ");
                        sb2.append(str4);
                        Log.e(str7, sb2.toString());
                        if (mXOlmInboundGroupSession2.mSession != null) {
                            mXOlmInboundGroupSession2.mSession.releaseSession();
                        }
                    } else {
                        MXOlmInboundGroupSession2 inboundGroupSession = getInboundGroupSession(str3, str4, str5);
                        if (inboundGroupSession != null) {
                            String str8 = LOG_TAG;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(str2);
                            sb3.append(str4);
                            sb3.append(str);
                            sb3.append(str3);
                            Log.e(str8, sb3.toString());
                            if (inboundGroupSession.getFirstKnownIndex().longValue() <= mXOlmInboundGroupSession2.getFirstKnownIndex().longValue()) {
                                mXOlmInboundGroupSession2.mSession.releaseSession();
                            }
                        }
                        arrayList.add(mXOlmInboundGroupSession2);
                    }
                } catch (Exception e2) {
                    String str9 = LOG_TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("## importInboundGroupSession : sessionIdentifier() failed ");
                    sb4.append(e2.getMessage());
                    Log.e(str9, sb4.toString(), e2);
                    mXOlmInboundGroupSession2.mSession.releaseSession();
                }
            }
        }
        this.mStore.storeInboundGroupSessions(arrayList);
        return arrayList;
    }

    public void removeInboundGroupSession(String str, String str2) {
        if (str != null && str2 != null) {
            this.mStore.removeInboundGroupSession(str, str2);
        }
    }

    public MXDecryptionResult decryptGroupMessage(String str, String str2, String str3, String str4, String str5) throws MXDecryptionException {
        DecryptMessageResult decryptMessageResult;
        String str6;
        MXDecryptionResult mXDecryptionResult = new MXDecryptionResult();
        MXOlmInboundGroupSession2 inboundGroupSession = getInboundGroupSession(str4, str5, str2);
        if (inboundGroupSession != null) {
            boolean equals = TextUtils.equals(str2, inboundGroupSession.mRoomId);
            String str7 = MXCryptoError.UNABLE_TO_DECRYPT;
            String str8 = "## decryptGroupMessage() : ";
            if (equals) {
                try {
                    decryptMessageResult = inboundGroupSession.mSession.decryptMessage(str);
                    str6 = "";
                } catch (Exception e) {
                    String str9 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## decryptGroupMessage () : decryptMessage failed ");
                    sb.append(e.getMessage());
                    Log.e(str9, sb.toString(), e);
                    str6 = e.getMessage();
                    decryptMessageResult = null;
                }
                if (decryptMessageResult != null) {
                    if (str3 != null) {
                        if (!this.mInboundGroupSessionMessageIndexes.containsKey(str3)) {
                            this.mInboundGroupSessionMessageIndexes.put(str3, new HashMap());
                        }
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str5);
                        String str10 = "|";
                        sb2.append(str10);
                        sb2.append(str4);
                        sb2.append(str10);
                        sb2.append(decryptMessageResult.mIndex);
                        String sb3 = sb2.toString();
                        if (((Map) this.mInboundGroupSessionMessageIndexes.get(str3)).get(sb3) == null) {
                            ((Map) this.mInboundGroupSessionMessageIndexes.get(str3)).put(sb3, Boolean.valueOf(true));
                        } else {
                            String format = String.format(MXCryptoError.DUPLICATE_MESSAGE_INDEX_REASON, new Object[]{Long.valueOf(decryptMessageResult.mIndex)});
                            String str11 = LOG_TAG;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(str8);
                            sb4.append(format);
                            Log.e(str11, sb4.toString());
                            throw new MXDecryptionException(new MXCryptoError(MXCryptoError.DUPLICATED_MESSAGE_INDEX_ERROR_CODE, str7, format));
                        }
                    }
                    this.mStore.storeInboundGroupSessions(Collections.singletonList(inboundGroupSession));
                    try {
                        mXDecryptionResult.mPayload = new JsonParser().parse(StringUtilsKt.convertFromUTF8(decryptMessageResult.mDecryptedMessage));
                        if (mXDecryptionResult.mPayload == null) {
                            Log.e(LOG_TAG, "## decryptGroupMessage() : fails to parse the payload");
                            return null;
                        }
                        mXDecryptionResult.mKeysClaimed = inboundGroupSession.mKeysClaimed;
                        mXDecryptionResult.mSenderKey = str5;
                        mXDecryptionResult.mForwardingCurve25519KeyChain = inboundGroupSession.mForwardingCurve25519KeyChain;
                        return mXDecryptionResult;
                    } catch (Exception e2) {
                        String str12 = LOG_TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("## decryptGroupMessage() : RLEncoder.encode failed ");
                        sb5.append(e2.getMessage());
                        Log.e(str12, sb5.toString(), e2);
                        return null;
                    }
                } else {
                    Log.e(LOG_TAG, "## decryptGroupMessage() : failed to decode the message");
                    throw new MXDecryptionException(new MXCryptoError(MXCryptoError.OLM_ERROR_CODE, str6, null));
                }
            } else {
                String format2 = String.format(MXCryptoError.INBOUND_SESSION_MISMATCH_ROOM_ID_REASON, new Object[]{str2, inboundGroupSession.mRoomId});
                String str13 = LOG_TAG;
                StringBuilder sb6 = new StringBuilder();
                sb6.append(str8);
                sb6.append(format2);
                Log.e(str13, sb6.toString());
                throw new MXDecryptionException(new MXCryptoError(MXCryptoError.INBOUND_SESSION_MISMATCH_ROOM_ID_ERROR_CODE, str7, format2));
            }
        } else {
            String str14 = LOG_TAG;
            StringBuilder sb7 = new StringBuilder();
            sb7.append("## decryptGroupMessage() : Cannot retrieve inbound group session ");
            sb7.append(str4);
            Log.e(str14, sb7.toString());
            throw new MXDecryptionException(this.mInboundGroupSessionWithIdError);
        }
    }

    public void resetReplayAttackCheckInTimeline(String str) {
        if (str != null) {
            this.mInboundGroupSessionMessageIndexes.remove(str);
        }
    }

    public void verifySignature(String str, Map<String, Object> map, String str2) throws Exception {
        this.mOlmUtility.verifyEd25519Signature(str2, str, JsonUtility.getCanonicalizedJsonString(map));
    }

    public String sha256(String str) {
        return this.mOlmUtility.sha256(StringUtilsKt.convertToUTF8(str));
    }

    private MXOlmSession getSessionForDevice(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        return this.mStore.getDeviceSession(str2, str);
    }

    public MXOlmInboundGroupSession2 getInboundGroupSession(String str, String str2, String str3) {
        this.mInboundGroupSessionWithIdError = null;
        MXOlmInboundGroupSession2 inboundGroupSession = this.mStore.getInboundGroupSession(str, str2);
        if (inboundGroupSession == null) {
            String str4 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getInboundGroupSession() : Cannot retrieve inbound group session ");
            sb.append(str);
            Log.e(str4, sb.toString());
            this.mInboundGroupSessionWithIdError = new MXCryptoError(MXCryptoError.UNKNOWN_INBOUND_SESSION_ID_ERROR_CODE, MXCryptoError.UNKNOWN_INBOUND_SESSION_ID_REASON, null);
        } else if (!TextUtils.equals(str3, inboundGroupSession.mRoomId)) {
            String format = String.format(MXCryptoError.INBOUND_SESSION_MISMATCH_ROOM_ID_REASON, new Object[]{str3, inboundGroupSession.mRoomId});
            String str5 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## getInboundGroupSession() : ");
            sb2.append(format);
            Log.e(str5, sb2.toString());
            this.mInboundGroupSessionWithIdError = new MXCryptoError(MXCryptoError.INBOUND_SESSION_MISMATCH_ROOM_ID_ERROR_CODE, MXCryptoError.UNABLE_TO_DECRYPT, format);
        }
        return inboundGroupSession;
    }

    public boolean hasInboundSessionKeys(String str, String str2, String str3) {
        return getInboundGroupSession(str3, str2, str) != null;
    }
}
