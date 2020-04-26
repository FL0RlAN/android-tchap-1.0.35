package org.matrix.androidsdk.crypto.algorithms.megolm;

import android.text.TextUtils;
import com.google.gson.JsonElement;
import im.vector.activity.VectorUniversalLinkActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.core.JsonUtility;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.StringUtilsKt;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.CryptoConstantsKt;
import org.matrix.androidsdk.crypto.MXCryptoError;
import org.matrix.androidsdk.crypto.MXOlmDevice;
import org.matrix.androidsdk.crypto.algorithms.IMXEncrypting;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXOlmSessionResult;
import org.matrix.androidsdk.crypto.data.MXQueuedEncryption;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.internal.MXCryptoImpl;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;
import org.matrix.olm.OlmAccount;

public class MXMegolmEncryption implements IMXEncrypting {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = MXMegolmEncryption.class.getSimpleName();
    /* access modifiers changed from: private */
    public MXCryptoImpl mCrypto;
    private String mDeviceId;
    private MXOutboundSessionInfo mOutboundSession;
    /* access modifiers changed from: private */
    public final List<MXQueuedEncryption> mPendingEncryptions = new ArrayList();
    /* access modifiers changed from: private */
    public String mRoomId;
    private int mSessionRotationPeriodMs;
    private int mSessionRotationPeriodMsgs;
    /* access modifiers changed from: private */
    public boolean mShareOperationIsProgress;

    public void initWithMatrixSession(CryptoSession cryptoSession, MXCryptoImpl mXCryptoImpl, String str) {
        this.mCrypto = mXCryptoImpl;
        this.mRoomId = str;
        this.mDeviceId = cryptoSession.getDeviceId();
        this.mSessionRotationPeriodMsgs = 100;
        this.mSessionRotationPeriodMs = 604800000;
    }

    /* access modifiers changed from: private */
    public List<MXQueuedEncryption> getPendingEncryptions() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mPendingEncryptions) {
            arrayList.addAll(this.mPendingEncryptions);
        }
        return arrayList;
    }

    public void encryptEventContent(JsonElement jsonElement, String str, List<String> list, ApiCallback<JsonElement> apiCallback) {
        MXQueuedEncryption mXQueuedEncryption = new MXQueuedEncryption();
        mXQueuedEncryption.mEventContent = jsonElement;
        mXQueuedEncryption.mEventType = str;
        mXQueuedEncryption.mApiCallback = apiCallback;
        synchronized (this.mPendingEncryptions) {
            this.mPendingEncryptions.add(mXQueuedEncryption);
        }
        final long currentTimeMillis = System.currentTimeMillis();
        Log.d(LOG_TAG, "## encryptEventContent () starts");
        getDevicesInRoom(list, new ApiCallback<MXUsersDevicesMap<MXDeviceInfo>>() {
            /* access modifiers changed from: private */
            public void dispatchNetworkError(Exception exc) {
                String access$000 = MXMegolmEncryption.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## encryptEventContent() : onNetworkError ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                List<MXQueuedEncryption> access$100 = MXMegolmEncryption.this.getPendingEncryptions();
                for (MXQueuedEncryption mXQueuedEncryption : access$100) {
                    mXQueuedEncryption.mApiCallback.onNetworkError(exc);
                }
                synchronized (MXMegolmEncryption.this.mPendingEncryptions) {
                    MXMegolmEncryption.this.mPendingEncryptions.removeAll(access$100);
                }
            }

            /* access modifiers changed from: private */
            public void dispatchMatrixError(MatrixError matrixError) {
                String access$000 = MXMegolmEncryption.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## encryptEventContent() : onMatrixError ");
                sb.append(matrixError.getMessage());
                Log.e(access$000, sb.toString());
                List<MXQueuedEncryption> access$100 = MXMegolmEncryption.this.getPendingEncryptions();
                for (MXQueuedEncryption mXQueuedEncryption : access$100) {
                    mXQueuedEncryption.mApiCallback.onMatrixError(matrixError);
                }
                synchronized (MXMegolmEncryption.this.mPendingEncryptions) {
                    MXMegolmEncryption.this.mPendingEncryptions.removeAll(access$100);
                }
            }

            /* access modifiers changed from: private */
            public void dispatchUnexpectedError(Exception exc) {
                String access$000 = MXMegolmEncryption.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## onUnexpectedError() : onMatrixError ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                List<MXQueuedEncryption> access$100 = MXMegolmEncryption.this.getPendingEncryptions();
                for (MXQueuedEncryption mXQueuedEncryption : access$100) {
                    mXQueuedEncryption.mApiCallback.onUnexpectedError(exc);
                }
                synchronized (MXMegolmEncryption.this.mPendingEncryptions) {
                    MXMegolmEncryption.this.mPendingEncryptions.removeAll(access$100);
                }
            }

            public void onSuccess(MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
                MXMegolmEncryption.this.ensureOutboundSession(mXUsersDevicesMap, new ApiCallback<MXOutboundSessionInfo>() {
                    public void onSuccess(final MXOutboundSessionInfo mXOutboundSessionInfo) {
                        MXMegolmEncryption.this.mCrypto.getEncryptingThreadHandler().post(new Runnable() {
                            public void run() {
                                String access$000 = MXMegolmEncryption.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## encryptEventContent () processPendingEncryptions after ");
                                sb.append(System.currentTimeMillis() - currentTimeMillis);
                                sb.append("ms");
                                Log.d(access$000, sb.toString());
                                MXMegolmEncryption.this.processPendingEncryptions(mXOutboundSessionInfo);
                            }
                        });
                    }

                    public void onNetworkError(Exception exc) {
                        AnonymousClass1.this.dispatchNetworkError(exc);
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        AnonymousClass1.this.dispatchMatrixError(matrixError);
                    }

                    public void onUnexpectedError(Exception exc) {
                        AnonymousClass1.this.dispatchUnexpectedError(exc);
                    }
                });
            }

            public void onNetworkError(Exception exc) {
                dispatchNetworkError(exc);
            }

            public void onMatrixError(MatrixError matrixError) {
                dispatchMatrixError(matrixError);
            }

            public void onUnexpectedError(Exception exc) {
                dispatchUnexpectedError(exc);
            }
        });
    }

    private MXOutboundSessionInfo prepareNewSessionInRoom() {
        MXOlmDevice olmDevice = this.mCrypto.getOlmDevice();
        String createOutboundGroupSession = olmDevice.createOutboundGroupSession();
        HashMap hashMap = new HashMap();
        hashMap.put(OlmAccount.JSON_KEY_FINGER_PRINT_KEY, olmDevice.getDeviceEd25519Key());
        olmDevice.addInboundGroupSession(createOutboundGroupSession, olmDevice.getSessionKey(createOutboundGroupSession), this.mRoomId, olmDevice.getDeviceCurve25519Key(), new ArrayList(), hashMap, false);
        this.mCrypto.getKeysBackup().maybeBackupKeys();
        return new MXOutboundSessionInfo(createOutboundGroupSession);
    }

    /* access modifiers changed from: private */
    public void ensureOutboundSession(MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap, final ApiCallback<MXOutboundSessionInfo> apiCallback) {
        final MXOutboundSessionInfo mXOutboundSessionInfo = this.mOutboundSession;
        if (mXOutboundSessionInfo == null || mXOutboundSessionInfo.needsRotation(this.mSessionRotationPeriodMsgs, this.mSessionRotationPeriodMs) || mXOutboundSessionInfo.sharedWithTooManyDevices(mXUsersDevicesMap)) {
            mXOutboundSessionInfo = prepareNewSessionInRoom();
            this.mOutboundSession = mXOutboundSessionInfo;
        }
        if (this.mShareOperationIsProgress) {
            Log.d(LOG_TAG, "## ensureOutboundSessionInRoom() : already in progress");
            return;
        }
        HashMap hashMap = new HashMap();
        for (String str : mXUsersDevicesMap.getUserIds()) {
            for (String str2 : mXUsersDevicesMap.getUserDeviceIds(str)) {
                MXDeviceInfo mXDeviceInfo = (MXDeviceInfo) mXUsersDevicesMap.getObject(str2, str);
                if (mXOutboundSessionInfo.mSharedWithDevices.getObject(str2, str) == null) {
                    if (!hashMap.containsKey(str)) {
                        hashMap.put(str, new ArrayList());
                    }
                    ((List) hashMap.get(str)).add(mXDeviceInfo);
                }
            }
        }
        shareKey(mXOutboundSessionInfo, hashMap, new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                MXMegolmEncryption.this.mShareOperationIsProgress = false;
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onSuccess(mXOutboundSessionInfo);
                }
            }

            public void onNetworkError(Exception exc) {
                String access$000 = MXMegolmEncryption.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## ensureOutboundSessionInRoom() : shareKey onNetworkError ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onNetworkError(exc);
                }
                MXMegolmEncryption.this.mShareOperationIsProgress = false;
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$000 = MXMegolmEncryption.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## ensureOutboundSessionInRoom() : shareKey onMatrixError ");
                sb.append(matrixError.getMessage());
                Log.e(access$000, sb.toString());
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onMatrixError(matrixError);
                }
                MXMegolmEncryption.this.mShareOperationIsProgress = false;
            }

            public void onUnexpectedError(Exception exc) {
                String access$000 = MXMegolmEncryption.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## ensureOutboundSessionInRoom() : shareKey onUnexpectedError ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onUnexpectedError(exc);
                }
                MXMegolmEncryption.this.mShareOperationIsProgress = false;
            }
        });
    }

    /* access modifiers changed from: private */
    public void shareKey(MXOutboundSessionInfo mXOutboundSessionInfo, Map<String, List<MXDeviceInfo>> map, final ApiCallback<Void> apiCallback) {
        if (map.size() == 0) {
            Log.d(LOG_TAG, "## shareKey() : nothing more to do");
            if (apiCallback != null) {
                this.mCrypto.getUIHandler().post(new Runnable() {
                    public void run() {
                        apiCallback.onSuccess(null);
                    }
                });
            }
            return;
        }
        HashMap hashMap = new HashMap();
        final ArrayList arrayList = new ArrayList();
        int i = 0;
        for (String str : map.keySet()) {
            List list = (List) map.get(str);
            arrayList.add(str);
            hashMap.put(str, list);
            i += list.size();
            if (i > 100) {
                break;
            }
        }
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## shareKey() ; userId ");
        sb.append(arrayList);
        Log.d(str2, sb.toString());
        final Map<String, List<MXDeviceInfo>> map2 = map;
        final MXOutboundSessionInfo mXOutboundSessionInfo2 = mXOutboundSessionInfo;
        final ApiCallback<Void> apiCallback2 = apiCallback;
        AnonymousClass4 r1 = new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                MXMegolmEncryption.this.mCrypto.getEncryptingThreadHandler().post(new Runnable() {
                    public void run() {
                        for (String remove : arrayList) {
                            map2.remove(remove);
                        }
                        MXMegolmEncryption.this.shareKey(mXOutboundSessionInfo2, map2, apiCallback2);
                    }
                });
            }

            public void onNetworkError(Exception exc) {
                String access$000 = MXMegolmEncryption.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## shareKey() ; userIds ");
                sb.append(arrayList);
                sb.append(" failed ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                ApiCallback apiCallback = apiCallback2;
                if (apiCallback != null) {
                    apiCallback.onNetworkError(exc);
                }
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$000 = MXMegolmEncryption.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## shareKey() ; userIds ");
                sb.append(arrayList);
                sb.append(" failed ");
                sb.append(matrixError.getMessage());
                Log.e(access$000, sb.toString());
                ApiCallback apiCallback = apiCallback2;
                if (apiCallback != null) {
                    apiCallback.onMatrixError(matrixError);
                }
            }

            public void onUnexpectedError(Exception exc) {
                String access$000 = MXMegolmEncryption.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## shareKey() ; userIds ");
                sb.append(arrayList);
                sb.append(" failed ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                ApiCallback apiCallback = apiCallback2;
                if (apiCallback != null) {
                    apiCallback.onUnexpectedError(exc);
                }
            }
        };
        shareUserDevicesKey(mXOutboundSessionInfo, hashMap, r1);
    }

    private void shareUserDevicesKey(MXOutboundSessionInfo mXOutboundSessionInfo, Map<String, List<MXDeviceInfo>> map, ApiCallback<Void> apiCallback) {
        String sessionKey = this.mCrypto.getOlmDevice().getSessionKey(mXOutboundSessionInfo.mSessionId);
        final int messageIndex = this.mCrypto.getOlmDevice().getMessageIndex(mXOutboundSessionInfo.mSessionId);
        HashMap hashMap = new HashMap();
        hashMap.put(CryptoRoomEntityFields.ALGORITHM, CryptoConstantsKt.MXCRYPTO_ALGORITHM_MEGOLM);
        hashMap.put("room_id", this.mRoomId);
        hashMap.put(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_SESSION_ID, mXOutboundSessionInfo.mSessionId);
        hashMap.put("session_key", sessionKey);
        hashMap.put("chain_index", Integer.valueOf(messageIndex));
        final HashMap hashMap2 = new HashMap();
        hashMap2.put(PasswordLoginParams.IDENTIFIER_KEY_TYPE, "m.room_key");
        hashMap2.put(BingRule.KIND_CONTENT, hashMap);
        final long currentTimeMillis = System.currentTimeMillis();
        Log.d(LOG_TAG, "## shareUserDevicesKey() : starts");
        MXCryptoImpl mXCryptoImpl = this.mCrypto;
        final Map<String, List<MXDeviceInfo>> map2 = map;
        final MXOutboundSessionInfo mXOutboundSessionInfo2 = mXOutboundSessionInfo;
        final ApiCallback<Void> apiCallback2 = apiCallback;
        AnonymousClass5 r3 = new ApiCallback<MXUsersDevicesMap<MXOlmSessionResult>>() {
            public void onSuccess(final MXUsersDevicesMap<MXOlmSessionResult> mXUsersDevicesMap) {
                MXMegolmEncryption.this.mCrypto.getEncryptingThreadHandler().post(new Runnable() {
                    public void run() {
                        String access$000 = MXMegolmEncryption.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## shareUserDevicesKey() : ensureOlmSessionsForDevices succeeds after ");
                        sb.append(System.currentTimeMillis() - currentTimeMillis);
                        sb.append(" ms");
                        Log.d(access$000, sb.toString());
                        MXUsersDevicesMap mXUsersDevicesMap = new MXUsersDevicesMap();
                        boolean z = false;
                        for (String str : mXUsersDevicesMap.getUserIds()) {
                            for (MXDeviceInfo mXDeviceInfo : (List) map2.get(str)) {
                                String str2 = mXDeviceInfo.deviceId;
                                MXOlmSessionResult mXOlmSessionResult = (MXOlmSessionResult) mXUsersDevicesMap.getObject(str2, str);
                                if (!(mXOlmSessionResult == null || mXOlmSessionResult.mSessionId == null)) {
                                    String access$0002 = MXMegolmEncryption.LOG_TAG;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("## shareUserDevicesKey() : Sharing keys with device ");
                                    sb2.append(str);
                                    sb2.append(":");
                                    sb2.append(str2);
                                    Log.d(access$0002, sb2.toString());
                                    mXUsersDevicesMap.setObject(MXMegolmEncryption.this.mCrypto.encryptMessage(hashMap2, Arrays.asList(new MXDeviceInfo[]{mXOlmSessionResult.mDevice})), str, str2);
                                    z = true;
                                }
                            }
                        }
                        if (!z || MXMegolmEncryption.this.mCrypto.hasBeenReleased()) {
                            Log.d(MXMegolmEncryption.LOG_TAG, "## shareUserDevicesKey() : no need to sharekey");
                            if (apiCallback2 != null) {
                                MXMegolmEncryption.this.mCrypto.getUIHandler().post(new Runnable() {
                                    public void run() {
                                        apiCallback2.onSuccess(null);
                                    }
                                });
                                return;
                            }
                            return;
                        }
                        final long currentTimeMillis = System.currentTimeMillis();
                        Log.d(MXMegolmEncryption.LOG_TAG, "## shareUserDevicesKey() : has target");
                        MXMegolmEncryption.this.mCrypto.getCryptoRestClient().sendToDevice("m.room.encrypted", mXUsersDevicesMap, new ApiCallback<Void>() {
                            public void onSuccess(Void voidR) {
                                MXMegolmEncryption.this.mCrypto.getEncryptingThreadHandler().post(new Runnable() {
                                    public void run() {
                                        String access$000 = MXMegolmEncryption.LOG_TAG;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("## shareUserDevicesKey() : sendToDevice succeeds after ");
                                        sb.append(System.currentTimeMillis() - currentTimeMillis);
                                        sb.append(" ms");
                                        Log.d(access$000, sb.toString());
                                        for (String str : map2.keySet()) {
                                            for (MXDeviceInfo mXDeviceInfo : (List) map2.get(str)) {
                                                mXOutboundSessionInfo2.mSharedWithDevices.setObject(Integer.valueOf(messageIndex), str, mXDeviceInfo.deviceId);
                                            }
                                        }
                                        MXMegolmEncryption.this.mCrypto.getUIHandler().post(new Runnable() {
                                            public void run() {
                                                if (apiCallback2 != null) {
                                                    apiCallback2.onSuccess(null);
                                                }
                                            }
                                        });
                                    }
                                });
                            }

                            public void onNetworkError(Exception exc) {
                                String access$000 = MXMegolmEncryption.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## shareUserDevicesKey() : sendToDevice onNetworkError ");
                                sb.append(exc.getMessage());
                                Log.e(access$000, sb.toString(), exc);
                                if (apiCallback2 != null) {
                                    apiCallback2.onNetworkError(exc);
                                }
                            }

                            public void onMatrixError(MatrixError matrixError) {
                                String access$000 = MXMegolmEncryption.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## shareUserDevicesKey() : sendToDevice onMatrixError ");
                                sb.append(matrixError.getMessage());
                                Log.e(access$000, sb.toString());
                                if (apiCallback2 != null) {
                                    apiCallback2.onMatrixError(matrixError);
                                }
                            }

                            public void onUnexpectedError(Exception exc) {
                                String access$000 = MXMegolmEncryption.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## shareUserDevicesKey() : sendToDevice onUnexpectedError ");
                                sb.append(exc.getMessage());
                                Log.e(access$000, sb.toString(), exc);
                                if (apiCallback2 != null) {
                                    apiCallback2.onUnexpectedError(exc);
                                }
                            }
                        });
                    }
                });
            }

            public void onNetworkError(Exception exc) {
                String access$000 = MXMegolmEncryption.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## shareUserDevicesKey() : ensureOlmSessionsForDevices failed ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                ApiCallback apiCallback = apiCallback2;
                if (apiCallback != null) {
                    apiCallback.onNetworkError(exc);
                }
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$000 = MXMegolmEncryption.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## shareUserDevicesKey() : ensureOlmSessionsForDevices failed ");
                sb.append(matrixError.getMessage());
                Log.e(access$000, sb.toString());
                ApiCallback apiCallback = apiCallback2;
                if (apiCallback != null) {
                    apiCallback.onMatrixError(matrixError);
                }
            }

            public void onUnexpectedError(Exception exc) {
                String access$000 = MXMegolmEncryption.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## shareUserDevicesKey() : ensureOlmSessionsForDevices failed ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                ApiCallback apiCallback = apiCallback2;
                if (apiCallback != null) {
                    apiCallback.onUnexpectedError(exc);
                }
            }
        };
        mXCryptoImpl.ensureOlmSessionsForDevices(map, r3);
    }

    /* access modifiers changed from: private */
    public void processPendingEncryptions(MXOutboundSessionInfo mXOutboundSessionInfo) {
        if (mXOutboundSessionInfo != null) {
            List<MXQueuedEncryption> pendingEncryptions = getPendingEncryptions();
            for (final MXQueuedEncryption mXQueuedEncryption : pendingEncryptions) {
                HashMap hashMap = new HashMap();
                hashMap.put("room_id", this.mRoomId);
                hashMap.put(PasswordLoginParams.IDENTIFIER_KEY_TYPE, mXQueuedEncryption.mEventType);
                hashMap.put(BingRule.KIND_CONTENT, mXQueuedEncryption.mEventContent);
                String encryptGroupMessage = this.mCrypto.getOlmDevice().encryptGroupMessage(mXOutboundSessionInfo.mSessionId, StringUtilsKt.convertToUTF8(JsonUtility.canonicalize(JsonUtility.getGson(false).toJsonTree(hashMap)).toString()));
                final HashMap hashMap2 = new HashMap();
                hashMap2.put(CryptoRoomEntityFields.ALGORITHM, CryptoConstantsKt.MXCRYPTO_ALGORITHM_MEGOLM);
                hashMap2.put("sender_key", this.mCrypto.getOlmDevice().getDeviceCurve25519Key());
                hashMap2.put("ciphertext", encryptGroupMessage);
                hashMap2.put(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_SESSION_ID, mXOutboundSessionInfo.mSessionId);
                hashMap2.put("device_id", this.mDeviceId);
                this.mCrypto.getUIHandler().post(new Runnable() {
                    public void run() {
                        mXQueuedEncryption.mApiCallback.onSuccess(JsonUtility.getGson(false).toJsonTree(hashMap2));
                    }
                });
                mXOutboundSessionInfo.mUseCount++;
            }
            synchronized (this.mPendingEncryptions) {
                this.mPendingEncryptions.removeAll(pendingEncryptions);
            }
        }
    }

    private void getDevicesInRoom(List<String> list, final ApiCallback<MXUsersDevicesMap<MXDeviceInfo>> apiCallback) {
        this.mCrypto.getDeviceList().downloadKeys(list, false, new SimpleApiCallback<MXUsersDevicesMap<MXDeviceInfo>>(apiCallback) {
            public void onSuccess(final MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
                MXMegolmEncryption.this.mCrypto.getEncryptingThreadHandler().post(new Runnable() {
                    public void run() {
                        boolean z = MXMegolmEncryption.this.mCrypto.getGlobalBlacklistUnverifiedDevices() || MXMegolmEncryption.this.mCrypto.isRoomBlacklistUnverifiedDevices(MXMegolmEncryption.this.mRoomId);
                        final MXUsersDevicesMap mXUsersDevicesMap = new MXUsersDevicesMap();
                        final MXUsersDevicesMap mXUsersDevicesMap2 = new MXUsersDevicesMap();
                        for (String str : mXUsersDevicesMap.getUserIds()) {
                            for (String str2 : mXUsersDevicesMap.getUserDeviceIds(str)) {
                                MXDeviceInfo mXDeviceInfo = (MXDeviceInfo) mXUsersDevicesMap.getObject(str2, str);
                                if (MXMegolmEncryption.this.mCrypto.warnOnUnknownDevices() && mXDeviceInfo.isUnknown()) {
                                    mXUsersDevicesMap2.setObject(mXDeviceInfo, str, str2);
                                } else if (!mXDeviceInfo.isBlocked() && ((mXDeviceInfo.isVerified() || !z) && !TextUtils.equals(mXDeviceInfo.identityKey(), MXMegolmEncryption.this.mCrypto.getOlmDevice().getDeviceCurve25519Key()))) {
                                    mXUsersDevicesMap.setObject(mXDeviceInfo, str, str2);
                                }
                            }
                        }
                        MXMegolmEncryption.this.mCrypto.getUIHandler().post(new Runnable() {
                            public void run() {
                                if (mXUsersDevicesMap2.getMap().size() != 0) {
                                    apiCallback.onMatrixError(new MXCryptoError(MXCryptoError.UNKNOWN_DEVICES_CODE, MXCryptoError.UNABLE_TO_ENCRYPT, MXCryptoError.UNKNOWN_DEVICES_REASON, mXUsersDevicesMap2));
                                    return;
                                }
                                apiCallback.onSuccess(mXUsersDevicesMap);
                            }
                        });
                    }
                });
            }
        });
    }
}
