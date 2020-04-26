package org.matrix.androidsdk.crypto.algorithms.megolm;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.IncomingRoomKeyRequest;
import org.matrix.androidsdk.crypto.MXCryptoError;
import org.matrix.androidsdk.crypto.MXDecryptionException;
import org.matrix.androidsdk.crypto.MXEventDecryptionResult;
import org.matrix.androidsdk.crypto.MXOlmDevice;
import org.matrix.androidsdk.crypto.algorithms.IMXDecrypting;
import org.matrix.androidsdk.crypto.algorithms.MXDecryptionResult;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2;
import org.matrix.androidsdk.crypto.data.MXOlmSessionResult;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.crypto.interfaces.CryptoEvent;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.internal.MXCryptoImpl;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedEventContent;
import org.matrix.androidsdk.crypto.model.crypto.ForwardedRoomKeyContent;
import org.matrix.androidsdk.crypto.model.crypto.RoomKeyContent;
import org.matrix.androidsdk.crypto.model.crypto.RoomKeyRequestBody;
import org.matrix.androidsdk.crypto.rest.model.crypto.EncryptedMessage;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;
import org.matrix.olm.OlmAccount;

public class MXMegolmDecryption implements IMXDecrypting {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = MXMegolmDecryption.class.getSimpleName();
    /* access modifiers changed from: private */
    public MXCryptoImpl mCrypto;
    private MXOlmDevice mOlmDevice;
    private Map<String, Map<String, List<CryptoEvent>>> mPendingEvents;
    /* access modifiers changed from: private */
    public CryptoSession mSession;

    public void initWithMatrixSession(CryptoSession cryptoSession, MXCryptoImpl mXCryptoImpl) {
        this.mSession = cryptoSession;
        this.mCrypto = mXCryptoImpl;
        this.mOlmDevice = mXCryptoImpl.getOlmDevice();
        this.mPendingEvents = new HashMap();
    }

    public MXEventDecryptionResult decryptEvent(CryptoEvent cryptoEvent, String str) throws MXDecryptionException {
        return decryptEvent(cryptoEvent, str, true);
    }

    private MXEventDecryptionResult decryptEvent(CryptoEvent cryptoEvent, String str, boolean z) throws MXDecryptionException {
        MXCryptoError mXCryptoError;
        MXDecryptionResult mXDecryptionResult;
        MXEventDecryptionResult mXEventDecryptionResult = null;
        if (cryptoEvent == null) {
            Log.e(LOG_TAG, "## decryptEvent() : null event");
            return null;
        }
        EncryptedEventContent encryptedEventContent = cryptoEvent.toEncryptedEventContent();
        String str2 = encryptedEventContent.sender_key;
        String str3 = encryptedEventContent.ciphertext;
        String str4 = encryptedEventContent.session_id;
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str4) || TextUtils.isEmpty(str3)) {
            throw new MXDecryptionException(new MXCryptoError(MXCryptoError.MISSING_FIELDS_ERROR_CODE, MXCryptoError.UNABLE_TO_DECRYPT, MXCryptoError.MISSING_FIELDS_REASON));
        }
        try {
            mXDecryptionResult = this.mOlmDevice.decryptGroupMessage(str3, cryptoEvent.getRoomId(), str, str4, str2);
            mXCryptoError = null;
        } catch (MXDecryptionException e) {
            mXCryptoError = e.getCryptoError();
            mXDecryptionResult = null;
        }
        if (mXDecryptionResult != null && mXDecryptionResult.mPayload != null && mXCryptoError == null) {
            mXEventDecryptionResult = new MXEventDecryptionResult();
            mXEventDecryptionResult.mClearEvent = mXDecryptionResult.mPayload;
            mXEventDecryptionResult.mSenderCurve25519Key = mXDecryptionResult.mSenderKey;
            if (mXDecryptionResult.mKeysClaimed != null) {
                mXEventDecryptionResult.mClaimedEd25519Key = (String) mXDecryptionResult.mKeysClaimed.get(OlmAccount.JSON_KEY_FINGER_PRINT_KEY);
            }
            mXEventDecryptionResult.mForwardingCurve25519KeyChain = mXDecryptionResult.mForwardingCurve25519KeyChain;
        } else if (mXCryptoError != null) {
            if (mXCryptoError.isOlmError()) {
                if (TextUtils.equals("UNKNOWN_MESSAGE_INDEX", mXCryptoError.error)) {
                    addEventToPendingList(cryptoEvent, str);
                    if (z) {
                        requestKeysForEvent(cryptoEvent);
                    }
                }
                throw new MXDecryptionException(new MXCryptoError(MXCryptoError.OLM_ERROR_CODE, String.format(MXCryptoError.OLM_REASON, new Object[]{mXCryptoError.error}), String.format(MXCryptoError.DETAILLED_OLM_REASON, new Object[]{str3, mXCryptoError.error})));
            }
            if (TextUtils.equals(mXCryptoError.errcode, MXCryptoError.UNKNOWN_INBOUND_SESSION_ID_ERROR_CODE)) {
                addEventToPendingList(cryptoEvent, str);
                if (z) {
                    requestKeysForEvent(cryptoEvent);
                }
            }
            throw new MXDecryptionException(mXCryptoError);
        }
        return mXEventDecryptionResult;
    }

    private void requestKeysForEvent(CryptoEvent cryptoEvent) {
        String sender = cryptoEvent.getSender();
        EncryptedEventContent encryptedEventContent = cryptoEvent.toEncryptedEventContent();
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        String str = "userId";
        hashMap.put(str, this.mSession.getMyUserId());
        String str2 = "deviceId";
        hashMap.put(str2, "*");
        arrayList.add(hashMap);
        if (!TextUtils.equals(sender, this.mSession.getMyUserId())) {
            HashMap hashMap2 = new HashMap();
            hashMap2.put(str, sender);
            hashMap2.put(str2, encryptedEventContent.device_id);
            arrayList.add(hashMap2);
        }
        RoomKeyRequestBody roomKeyRequestBody = new RoomKeyRequestBody();
        roomKeyRequestBody.roomId = cryptoEvent.getRoomId();
        roomKeyRequestBody.algorithm = encryptedEventContent.algorithm;
        roomKeyRequestBody.senderKey = encryptedEventContent.sender_key;
        roomKeyRequestBody.sessionId = encryptedEventContent.session_id;
        this.mCrypto.requestRoomKey(roomKeyRequestBody, arrayList);
    }

    private void addEventToPendingList(CryptoEvent cryptoEvent, String str) {
        EncryptedEventContent encryptedEventContent = cryptoEvent.toEncryptedEventContent();
        String str2 = encryptedEventContent.sender_key;
        String str3 = encryptedEventContent.session_id;
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append("|");
        sb.append(str3);
        String sb2 = sb.toString();
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        if (!this.mPendingEvents.containsKey(sb2)) {
            this.mPendingEvents.put(sb2, new HashMap());
        }
        if (!((Map) this.mPendingEvents.get(sb2)).containsKey(str)) {
            ((Map) this.mPendingEvents.get(sb2)).put(str, new ArrayList());
        }
        if (((List) ((Map) this.mPendingEvents.get(sb2)).get(str)).indexOf(cryptoEvent) < 0) {
            String str4 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("## addEventToPendingList() : add Event ");
            sb3.append(cryptoEvent.getEventId());
            sb3.append(" in room id ");
            sb3.append(cryptoEvent.getRoomId());
            Log.d(str4, sb3.toString());
            ((List) ((Map) this.mPendingEvents.get(sb2)).get(str)).add(cryptoEvent);
        }
    }

    public void onRoomKeyEvent(CryptoEvent cryptoEvent) {
        String str;
        boolean z;
        Map map;
        List list;
        ArrayList arrayList;
        RoomKeyContent roomKeyContent = cryptoEvent.toRoomKeyContent();
        String str2 = roomKeyContent.room_id;
        String str3 = roomKeyContent.session_id;
        String str4 = roomKeyContent.session_key;
        String senderKey = cryptoEvent.getSenderKey();
        Map hashMap = new HashMap();
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3) || TextUtils.isEmpty(str4)) {
            Log.e(LOG_TAG, "## onRoomKeyEvent() :  Key event is missing fields");
            return;
        }
        String str5 = " sessionKey ";
        String str6 = " sessionId ";
        if (TextUtils.equals(cryptoEvent.getType(), "m.forwarded_room_key")) {
            String str7 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onRoomKeyEvent(), forward adding key : roomId ");
            sb.append(str2);
            sb.append(str6);
            sb.append(str3);
            sb.append(str5);
            sb.append(str4);
            Log.d(str7, sb.toString());
            ForwardedRoomKeyContent forwardedRoomKeyContent = cryptoEvent.toForwardedRoomKeyContent();
            if (forwardedRoomKeyContent.forwarding_curve25519_key_chain == null) {
                arrayList = new ArrayList();
            } else {
                arrayList = new ArrayList(forwardedRoomKeyContent.forwarding_curve25519_key_chain);
            }
            arrayList.add(senderKey);
            String str8 = forwardedRoomKeyContent.sender_key;
            if (str8 == null) {
                Log.e(LOG_TAG, "## onRoomKeyEvent() : forwarded_room_key event is missing sender_key field");
                return;
            }
            String str9 = forwardedRoomKeyContent.sender_claimed_ed25519_key;
            if (str9 == null) {
                Log.e(LOG_TAG, "## forwarded_room_key_event is missing sender_claimed_ed25519_key field");
                return;
            }
            hashMap.put(OlmAccount.JSON_KEY_FINGER_PRINT_KEY, str9);
            map = hashMap;
            str = str8;
            z = true;
            list = arrayList;
        } else {
            String str10 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## onRoomKeyEvent(), Adding key : roomId ");
            sb2.append(str2);
            sb2.append(str6);
            sb2.append(str3);
            sb2.append(str5);
            sb2.append(str4);
            Log.d(str10, sb2.toString());
            if (senderKey == null) {
                Log.e(LOG_TAG, "## onRoomKeyEvent() : key event has no sender key (not encrypted?)");
                return;
            }
            map = cryptoEvent.getKeysClaimed();
            str = senderKey;
            list = null;
            z = false;
        }
        if (this.mOlmDevice.addInboundGroupSession(str3, str4, str2, str, list, map, z)) {
            this.mSession.requireCrypto().getKeysBackup().maybeBackupKeys();
            RoomKeyRequestBody roomKeyRequestBody = new RoomKeyRequestBody();
            roomKeyRequestBody.algorithm = roomKeyContent.algorithm;
            roomKeyRequestBody.roomId = roomKeyContent.room_id;
            roomKeyRequestBody.sessionId = roomKeyContent.session_id;
            roomKeyRequestBody.senderKey = str;
            this.mSession.requireCrypto().cancelRoomKeyRequest(roomKeyRequestBody);
            onNewSession(str, str3);
        }
    }

    public void onNewSession(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("|");
        sb.append(str2);
        String sb2 = sb.toString();
        Map map = (Map) this.mPendingEvents.get(sb2);
        if (map != null) {
            this.mPendingEvents.remove(sb2);
            for (String str3 : map.keySet()) {
                for (final CryptoEvent cryptoEvent : (List) map.get(str3)) {
                    final MXEventDecryptionResult mXEventDecryptionResult = null;
                    try {
                        mXEventDecryptionResult = decryptEvent(cryptoEvent, TextUtils.isEmpty(str3) ? null : str3);
                    } catch (MXDecryptionException e) {
                        String str4 = LOG_TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("## onNewSession() : Still can't decrypt ");
                        sb3.append(cryptoEvent.getEventId());
                        sb3.append(". Error ");
                        sb3.append(e.getMessage());
                        Log.e(str4, sb3.toString(), e);
                        cryptoEvent.setCryptoError(e.getCryptoError());
                    }
                    if (mXEventDecryptionResult != null) {
                        this.mCrypto.getUIHandler().post(new Runnable() {
                            public void run() {
                                cryptoEvent.setClearData(mXEventDecryptionResult);
                                MXMegolmDecryption.this.mSession.getDataHandler().onEventDecrypted(cryptoEvent);
                            }
                        });
                        String str5 = LOG_TAG;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("## onNewSession() : successful re-decryption of ");
                        sb4.append(cryptoEvent.getEventId());
                        Log.d(str5, sb4.toString());
                    }
                }
            }
        }
    }

    public boolean hasKeysForKeyRequest(IncomingRoomKeyRequest incomingRoomKeyRequest) {
        return (incomingRoomKeyRequest == null || incomingRoomKeyRequest.mRequestBody == null || !this.mOlmDevice.hasInboundSessionKeys(incomingRoomKeyRequest.mRequestBody.roomId, incomingRoomKeyRequest.mRequestBody.senderKey, incomingRoomKeyRequest.mRequestBody.sessionId)) ? false : true;
    }

    public void shareKeysWithDevice(final IncomingRoomKeyRequest incomingRoomKeyRequest) {
        if (incomingRoomKeyRequest != null && incomingRoomKeyRequest.mRequestBody != null) {
            final String str = incomingRoomKeyRequest.mUserId;
            this.mCrypto.getDeviceList().downloadKeys(Arrays.asList(new String[]{str}), false, new ApiCallback<MXUsersDevicesMap<MXDeviceInfo>>() {
                public void onSuccess(MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
                    final String str = incomingRoomKeyRequest.mDeviceId;
                    final MXDeviceInfo userDevice = MXMegolmDecryption.this.mCrypto.mCryptoStore.getUserDevice(str, str);
                    if (userDevice != null) {
                        final RoomKeyRequestBody roomKeyRequestBody = incomingRoomKeyRequest.mRequestBody;
                        HashMap hashMap = new HashMap();
                        hashMap.put(str, new ArrayList(Arrays.asList(new MXDeviceInfo[]{userDevice})));
                        MXMegolmDecryption.this.mCrypto.ensureOlmSessionsForDevices(hashMap, new ApiCallback<MXUsersDevicesMap<MXOlmSessionResult>>() {
                            public void onSuccess(MXUsersDevicesMap<MXOlmSessionResult> mXUsersDevicesMap) {
                                MXOlmSessionResult mXOlmSessionResult = (MXOlmSessionResult) mXUsersDevicesMap.getObject(str, str);
                                if (mXOlmSessionResult != null && mXOlmSessionResult.mSessionId != null) {
                                    String access$200 = MXMegolmDecryption.LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("## shareKeysWithDevice() : sharing keys for session ");
                                    sb.append(roomKeyRequestBody.senderKey);
                                    sb.append("|");
                                    sb.append(roomKeyRequestBody.sessionId);
                                    sb.append(" with device ");
                                    sb.append(str);
                                    String str = ":";
                                    sb.append(str);
                                    sb.append(str);
                                    Log.d(access$200, sb.toString());
                                    MXOlmInboundGroupSession2 inboundGroupSession = MXMegolmDecryption.this.mCrypto.getOlmDevice().getInboundGroupSession(roomKeyRequestBody.sessionId, roomKeyRequestBody.senderKey, roomKeyRequestBody.roomId);
                                    HashMap hashMap = new HashMap();
                                    hashMap.put(PasswordLoginParams.IDENTIFIER_KEY_TYPE, "m.forwarded_room_key");
                                    hashMap.put(BingRule.KIND_CONTENT, inboundGroupSession.exportKeys());
                                    EncryptedMessage encryptMessage = MXMegolmDecryption.this.mCrypto.encryptMessage(hashMap, Arrays.asList(new MXDeviceInfo[]{userDevice}));
                                    MXUsersDevicesMap mXUsersDevicesMap2 = new MXUsersDevicesMap();
                                    mXUsersDevicesMap2.setObject(encryptMessage, str, str);
                                    String access$2002 = MXMegolmDecryption.LOG_TAG;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("## shareKeysWithDevice() : sending to ");
                                    sb2.append(str);
                                    sb2.append(str);
                                    sb2.append(str);
                                    Log.d(access$2002, sb2.toString());
                                    MXMegolmDecryption.this.mCrypto.getCryptoRestClient().sendToDevice("m.room.encrypted", mXUsersDevicesMap2, new ApiCallback<Void>() {
                                        public void onSuccess(Void voidR) {
                                            String access$200 = MXMegolmDecryption.LOG_TAG;
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("## shareKeysWithDevice() : sent to ");
                                            sb.append(str);
                                            sb.append(":");
                                            sb.append(str);
                                            Log.d(access$200, sb.toString());
                                        }

                                        public void onNetworkError(Exception exc) {
                                            String access$200 = MXMegolmDecryption.LOG_TAG;
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("## shareKeysWithDevice() : sendToDevice ");
                                            sb.append(str);
                                            sb.append(":");
                                            sb.append(str);
                                            sb.append(" failed ");
                                            sb.append(exc.getMessage());
                                            Log.e(access$200, sb.toString(), exc);
                                        }

                                        public void onMatrixError(MatrixError matrixError) {
                                            String access$200 = MXMegolmDecryption.LOG_TAG;
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("## shareKeysWithDevice() : sendToDevice ");
                                            sb.append(str);
                                            sb.append(":");
                                            sb.append(str);
                                            sb.append(" failed ");
                                            sb.append(matrixError.getMessage());
                                            Log.e(access$200, sb.toString());
                                        }

                                        public void onUnexpectedError(Exception exc) {
                                            String access$200 = MXMegolmDecryption.LOG_TAG;
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("## shareKeysWithDevice() : sendToDevice ");
                                            sb.append(str);
                                            sb.append(":");
                                            sb.append(str);
                                            sb.append(" failed ");
                                            sb.append(exc.getMessage());
                                            Log.e(access$200, sb.toString(), exc);
                                        }
                                    });
                                }
                            }

                            public void onNetworkError(Exception exc) {
                                String access$200 = MXMegolmDecryption.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## shareKeysWithDevice() : ensureOlmSessionsForDevices ");
                                sb.append(str);
                                sb.append(":");
                                sb.append(str);
                                sb.append(" failed ");
                                sb.append(exc.getMessage());
                                Log.e(access$200, sb.toString(), exc);
                            }

                            public void onMatrixError(MatrixError matrixError) {
                                String access$200 = MXMegolmDecryption.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## shareKeysWithDevice() : ensureOlmSessionsForDevices ");
                                sb.append(str);
                                sb.append(":");
                                sb.append(str);
                                sb.append(" failed ");
                                sb.append(matrixError.getMessage());
                                Log.e(access$200, sb.toString());
                            }

                            public void onUnexpectedError(Exception exc) {
                                String access$200 = MXMegolmDecryption.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## shareKeysWithDevice() : ensureOlmSessionsForDevices ");
                                sb.append(str);
                                sb.append(":");
                                sb.append(str);
                                sb.append(" failed ");
                                sb.append(exc.getMessage());
                                Log.e(access$200, sb.toString(), exc);
                            }
                        });
                        return;
                    }
                    String access$200 = MXMegolmDecryption.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## shareKeysWithDevice() : ensureOlmSessionsForDevices ");
                    sb.append(str);
                    sb.append(":");
                    sb.append(str);
                    sb.append(" not found");
                    Log.e(access$200, sb.toString());
                }

                public void onNetworkError(Exception exc) {
                    String access$200 = MXMegolmDecryption.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## shareKeysWithDevice() : downloadKeys ");
                    sb.append(str);
                    sb.append(" failed ");
                    sb.append(exc.getMessage());
                    Log.e(access$200, sb.toString(), exc);
                }

                public void onMatrixError(MatrixError matrixError) {
                    String access$200 = MXMegolmDecryption.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## shareKeysWithDevice() : downloadKeys ");
                    sb.append(str);
                    sb.append(" failed ");
                    sb.append(matrixError.getMessage());
                    Log.e(access$200, sb.toString());
                }

                public void onUnexpectedError(Exception exc) {
                    String access$200 = MXMegolmDecryption.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## shareKeysWithDevice() : downloadKeys ");
                    sb.append(str);
                    sb.append(" failed ");
                    sb.append(exc.getMessage());
                    Log.e(access$200, sb.toString(), exc);
                }
            });
        }
    }
}
