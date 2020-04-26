package org.matrix.androidsdk.crypto.algorithms.olm;

import android.text.TextUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import im.vector.activity.VectorUniversalLinkActivity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.matrix.androidsdk.core.JsonUtility;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.StringUtilsKt;
import org.matrix.androidsdk.crypto.IncomingRoomKeyRequest;
import org.matrix.androidsdk.crypto.MXCryptoError;
import org.matrix.androidsdk.crypto.MXDecryptionException;
import org.matrix.androidsdk.crypto.MXEventDecryptionResult;
import org.matrix.androidsdk.crypto.MXOlmDevice;
import org.matrix.androidsdk.crypto.algorithms.IMXDecrypting;
import org.matrix.androidsdk.crypto.interfaces.CryptoEvent;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.internal.MXCryptoImpl;
import org.matrix.androidsdk.crypto.model.crypto.OlmEventContent;
import org.matrix.androidsdk.crypto.model.crypto.OlmPayloadContent;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;
import org.matrix.olm.OlmAccount;

public class MXOlmDecryption implements IMXDecrypting {
    private static final String LOG_TAG = "MXOlmDecryption";
    private MXOlmDevice mOlmDevice;
    private CryptoSession mSession;

    public boolean hasKeysForKeyRequest(IncomingRoomKeyRequest incomingRoomKeyRequest) {
        return false;
    }

    public void onNewSession(String str, String str2) {
    }

    public void onRoomKeyEvent(CryptoEvent cryptoEvent) {
    }

    public void shareKeysWithDevice(IncomingRoomKeyRequest incomingRoomKeyRequest) {
    }

    public void initWithMatrixSession(CryptoSession cryptoSession, MXCryptoImpl mXCryptoImpl) {
        this.mSession = cryptoSession;
        this.mOlmDevice = mXCryptoImpl.getOlmDevice();
    }

    public MXEventDecryptionResult decryptEvent(CryptoEvent cryptoEvent, String str) throws MXDecryptionException {
        String str2 = LOG_TAG;
        if (cryptoEvent == null) {
            Log.e(str2, "## decryptEvent() : null event");
            return null;
        }
        OlmEventContent olmEventContent = cryptoEvent.toOlmEventContent();
        String str3 = olmEventContent.sender_key;
        Map<String, Object> map = olmEventContent.ciphertext;
        String str4 = MXCryptoError.MISSING_CIPHER_TEXT_REASON;
        String str5 = MXCryptoError.UNABLE_TO_DECRYPT;
        if (map == null) {
            Log.e(str2, "## decryptEvent() : missing cipher text");
            throw new MXDecryptionException(new MXCryptoError(MXCryptoError.MISSING_CIPHER_TEXT_ERROR_CODE, str5, str4));
        } else if (map.containsKey(this.mOlmDevice.getDeviceCurve25519Key())) {
            String decryptMessage = decryptMessage((Map) map.get(this.mOlmDevice.getDeviceCurve25519Key()), str3);
            if (decryptMessage != null) {
                JsonElement parse = new JsonParser().parse(StringUtilsKt.convertFromUTF8(decryptMessage));
                String str6 = MXCryptoError.UNABLE_TO_DECRYPT_ERROR_CODE;
                if (parse != null) {
                    OlmPayloadContent olmPayloadContent = (OlmPayloadContent) JsonUtility.toClass(parse, OlmPayloadContent.class);
                    boolean isEmpty = TextUtils.isEmpty(olmPayloadContent.recipient);
                    String str7 = MXCryptoError.MISSING_PROPERTY_ERROR_CODE;
                    String str8 = MXCryptoError.ERROR_MISSING_PROPERTY_REASON;
                    if (!isEmpty) {
                        String str9 = "## decryptEvent() : Event ";
                        if (TextUtils.equals(olmPayloadContent.recipient, this.mSession.getMyUserId())) {
                            String str10 = "## decryptEvent() : Olm event (id=";
                            if (olmPayloadContent.recipient_keys != null) {
                                Map<String, String> map2 = olmPayloadContent.recipient_keys;
                                String str11 = OlmAccount.JSON_KEY_FINGER_PRINT_KEY;
                                String str12 = (String) map2.get(str11);
                                if (!TextUtils.equals(str12, this.mOlmDevice.getDeviceEd25519Key())) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(str9);
                                    sb.append(cryptoEvent.getEventId());
                                    sb.append(": Intended recipient ed25519 key ");
                                    sb.append(str12);
                                    sb.append(" did not match ours");
                                    Log.e(str2, sb.toString());
                                    throw new MXDecryptionException(new MXCryptoError(MXCryptoError.BAD_RECIPIENT_KEY_ERROR_CODE, str5, MXCryptoError.BAD_RECIPIENT_KEY_REASON));
                                } else if (TextUtils.isEmpty(olmPayloadContent.sender)) {
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append(str10);
                                    sb2.append(cryptoEvent.getEventId());
                                    sb2.append(") contains no 'sender' property; cannot prevent unknown-key attack");
                                    Log.e(str2, sb2.toString());
                                    throw new MXDecryptionException(new MXCryptoError(str7, str5, String.format(str8, new Object[]{BingRule.KIND_SENDER})));
                                } else if (!TextUtils.equals(olmPayloadContent.sender, cryptoEvent.getSender())) {
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("Event ");
                                    sb3.append(cryptoEvent.getEventId());
                                    sb3.append(": original sender ");
                                    sb3.append(olmPayloadContent.sender);
                                    sb3.append(" does not match reported sender ");
                                    sb3.append(cryptoEvent.getSender());
                                    Log.e(str2, sb3.toString());
                                    throw new MXDecryptionException(new MXCryptoError(MXCryptoError.FORWARDED_MESSAGE_ERROR_CODE, str5, String.format(MXCryptoError.FORWARDED_MESSAGE_REASON, new Object[]{olmPayloadContent.sender})));
                                } else if (!TextUtils.equals(olmPayloadContent.room_id, cryptoEvent.getRoomId())) {
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append(str9);
                                    sb4.append(cryptoEvent.getEventId());
                                    sb4.append(": original room ");
                                    sb4.append(olmPayloadContent.room_id);
                                    sb4.append(" does not match reported room ");
                                    sb4.append(cryptoEvent.getRoomId());
                                    Log.e(str2, sb4.toString());
                                    throw new MXDecryptionException(new MXCryptoError(MXCryptoError.BAD_ROOM_ERROR_CODE, str5, String.format(MXCryptoError.BAD_ROOM_REASON, new Object[]{olmPayloadContent.room_id})));
                                } else if (olmPayloadContent.keys != null) {
                                    MXEventDecryptionResult mXEventDecryptionResult = new MXEventDecryptionResult();
                                    mXEventDecryptionResult.mClearEvent = parse;
                                    mXEventDecryptionResult.mSenderCurve25519Key = str3;
                                    mXEventDecryptionResult.mClaimedEd25519Key = (String) olmPayloadContent.keys.get(str11);
                                    return mXEventDecryptionResult;
                                } else {
                                    Log.e(str2, "## decryptEvent failed : null keys");
                                    throw new MXDecryptionException(new MXCryptoError(str6, str5, str4));
                                }
                            } else {
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append(str10);
                                sb5.append(cryptoEvent.getEventId());
                                sb5.append(") contains no 'recipient_keys' property; cannot prevent unknown-key attack");
                                Log.e(str2, sb5.toString());
                                throw new MXDecryptionException(new MXCryptoError(str7, str5, String.format(str8, new Object[]{"recipient_keys"})));
                            }
                        } else {
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append(str9);
                            sb6.append(cryptoEvent.getEventId());
                            sb6.append(": Intended recipient ");
                            sb6.append(olmPayloadContent.recipient);
                            sb6.append(" does not match our id ");
                            sb6.append(this.mSession.getMyUserId());
                            Log.e(str2, sb6.toString());
                            throw new MXDecryptionException(new MXCryptoError(MXCryptoError.BAD_RECIPIENT_ERROR_CODE, str5, String.format(MXCryptoError.BAD_RECIPIENT_REASON, new Object[]{olmPayloadContent.recipient})));
                        }
                    } else {
                        String format = String.format(str8, new Object[]{"recipient"});
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("## decryptEvent() : ");
                        sb7.append(format);
                        Log.e(str2, sb7.toString());
                        throw new MXDecryptionException(new MXCryptoError(str7, str5, format));
                    }
                } else {
                    Log.e(str2, "## decryptEvent failed : null payload");
                    throw new MXDecryptionException(new MXCryptoError(str6, str5, str4));
                }
            } else {
                StringBuilder sb8 = new StringBuilder();
                sb8.append("## decryptEvent() Failed to decrypt Olm event (id= ");
                sb8.append(cryptoEvent.getEventId());
                sb8.append(" ) from ");
                sb8.append(str3);
                Log.e(str2, sb8.toString());
                throw new MXDecryptionException(new MXCryptoError(MXCryptoError.BAD_ENCRYPTED_MESSAGE_ERROR_CODE, str5, MXCryptoError.BAD_ENCRYPTED_MESSAGE_REASON));
            }
        } else {
            StringBuilder sb9 = new StringBuilder();
            sb9.append("## decryptEvent() : our device ");
            sb9.append(this.mOlmDevice.getDeviceCurve25519Key());
            sb9.append(" is not included in recipients. Event ");
            sb9.append(cryptoEvent.getContentAsJsonObject());
            Log.e(str2, sb9.toString());
            throw new MXDecryptionException(new MXCryptoError(MXCryptoError.NOT_INCLUDE_IN_RECIPIENTS_ERROR_CODE, str5, MXCryptoError.NOT_INCLUDED_IN_RECIPIENT_REASON));
        }
    }

    private String decryptMessage(Map<String, Object> map, String str) {
        ArrayList arrayList;
        Integer num;
        String str2;
        String str3;
        Set sessionIds = this.mOlmDevice.getSessionIds(str);
        if (sessionIds == null) {
            arrayList = new ArrayList();
        } else {
            arrayList = new ArrayList(sessionIds);
        }
        String str4 = (String) map.get("body");
        Object obj = map.get(PasswordLoginParams.IDENTIFIER_KEY_TYPE);
        if (obj != null) {
            if (obj instanceof Double) {
                num = Integer.valueOf(((Double) obj).intValue());
            } else if (obj instanceof Integer) {
                num = (Integer) obj;
            } else if (obj instanceof Long) {
                num = Integer.valueOf(((Long) obj).intValue());
            }
            if (str4 != null || num == null) {
                return null;
            }
            Iterator it = arrayList.iterator();
            do {
                boolean hasNext = it.hasNext();
                str2 = LOG_TAG;
                if (hasNext) {
                    str3 = (String) it.next();
                    String decryptMessage = this.mOlmDevice.decryptMessage(str4, num.intValue(), str3, str);
                    if (decryptMessage != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("## decryptMessage() : Decrypted Olm message from ");
                        sb.append(str);
                        sb.append(" with session ");
                        sb.append(str3);
                        Log.d(str2, sb.toString());
                        return decryptMessage;
                    }
                } else if (num.intValue() != 0) {
                    if (arrayList.size() == 0) {
                        Log.e(str2, "## decryptMessage() : No existing sessions");
                    } else {
                        Log.e(str2, "## decryptMessage() : Error decrypting non-prekey message with existing sessions");
                    }
                    return null;
                } else {
                    Map createInboundSession = this.mOlmDevice.createInboundSession(str, num.intValue(), str4);
                    if (createInboundSession == null) {
                        Log.e(str2, "## decryptMessage() :  Error decrypting non-prekey message with existing sessions");
                        return null;
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## decryptMessage() :  Created new inbound Olm session get id ");
                    sb2.append((String) createInboundSession.get(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_SESSION_ID));
                    sb2.append(" with ");
                    sb2.append(str);
                    Log.d(str2, sb2.toString());
                    return (String) createInboundSession.get("payload");
                }
            } while (!this.mOlmDevice.matchesSession(str, str3, num.intValue(), str4));
            StringBuilder sb3 = new StringBuilder();
            sb3.append("## decryptMessage() : Error decrypting prekey message with existing session id ");
            sb3.append(str3);
            sb3.append(":TODO");
            Log.e(str2, sb3.toString());
            return null;
        }
        num = null;
        if (str4 != null) {
        }
        return null;
    }
}
