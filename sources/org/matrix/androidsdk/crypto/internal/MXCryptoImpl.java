package org.matrix.androidsdk.crypto.internal;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import im.vector.activity.VectorUniversalLinkActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.core.JsonUtility;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.StringUtilsKt;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.listeners.IMXNetworkEventListener;
import org.matrix.androidsdk.core.listeners.ProgressListener;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.CryptoConstantsKt;
import org.matrix.androidsdk.crypto.IncomingRoomKeyRequest;
import org.matrix.androidsdk.crypto.IncomingRoomKeyRequestCancellation;
import org.matrix.androidsdk.crypto.MXCrypto;
import org.matrix.androidsdk.crypto.MXCryptoAlgorithms;
import org.matrix.androidsdk.crypto.MXCryptoConfig;
import org.matrix.androidsdk.crypto.MXCryptoError;
import org.matrix.androidsdk.crypto.MXDecryptionException;
import org.matrix.androidsdk.crypto.MXDeviceList;
import org.matrix.androidsdk.crypto.MXEventDecryptionResult;
import org.matrix.androidsdk.crypto.MXMegolmExportEncryption;
import org.matrix.androidsdk.crypto.MXOlmDevice;
import org.matrix.androidsdk.crypto.MXOutgoingRoomKeyRequestManager;
import org.matrix.androidsdk.crypto.MegolmSessionData;
import org.matrix.androidsdk.crypto.RoomKeysRequestListener;
import org.matrix.androidsdk.crypto.algorithms.IMXDecrypting;
import org.matrix.androidsdk.crypto.algorithms.IMXEncrypting;
import org.matrix.androidsdk.crypto.cryptostore.IMXCryptoStore;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;
import org.matrix.androidsdk.crypto.data.ImportRoomKeysResult;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXEncryptEventContentResult;
import org.matrix.androidsdk.crypto.data.MXKey;
import org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2;
import org.matrix.androidsdk.crypto.data.MXOlmSessionResult;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.crypto.interfaces.CryptoEvent;
import org.matrix.androidsdk.crypto.interfaces.CryptoEventContent;
import org.matrix.androidsdk.crypto.interfaces.CryptoEventListener;
import org.matrix.androidsdk.crypto.interfaces.CryptoRoom;
import org.matrix.androidsdk.crypto.interfaces.CryptoRoomMember;
import org.matrix.androidsdk.crypto.interfaces.CryptoRoomState;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.interfaces.CryptoSyncResponse;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackup;
import org.matrix.androidsdk.crypto.model.crypto.KeysUploadResponse;
import org.matrix.androidsdk.crypto.model.crypto.RoomKeyContent;
import org.matrix.androidsdk.crypto.model.crypto.RoomKeyRequestBody;
import org.matrix.androidsdk.crypto.rest.CryptoRestClient;
import org.matrix.androidsdk.crypto.rest.model.crypto.EncryptedMessage;
import org.matrix.androidsdk.crypto.rest.model.crypto.RoomKeyShare;
import org.matrix.androidsdk.crypto.verification.VerificationManager;
import org.matrix.androidsdk.network.NetworkConnectivityReceiver;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;
import org.matrix.olm.OlmAccount;

public class MXCryptoImpl implements MXCrypto {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = MXCryptoImpl.class.getSimpleName();
    private static final int ONE_TIME_KEY_GENERATION_MAX_NUMBER = 5;
    private static final long ONE_TIME_KEY_UPLOAD_PERIOD = 60000;
    /* access modifiers changed from: private */
    public MXCryptoConfig mCryptoConfig;
    private final CryptoRestClient mCryptoRestClient;
    public IMXCryptoStore mCryptoStore;
    private Handler mDecryptingHandler = null;
    /* access modifiers changed from: private */
    public HandlerThread mDecryptingHandlerThread;
    /* access modifiers changed from: private */
    public final MXDeviceList mDevicesList;
    private Handler mEncryptingHandler = null;
    /* access modifiers changed from: private */
    public HandlerThread mEncryptingHandlerThread;
    private final CryptoEventListener mEventListener = new CryptoEventListener() {
        public void onToDeviceEvent(CryptoEvent cryptoEvent) {
            MXCryptoImpl.this.onToDeviceEvent(cryptoEvent);
        }

        public void onLiveEvent(CryptoEvent cryptoEvent, CryptoRoomState cryptoRoomState) {
            if (TextUtils.equals(cryptoEvent.getType(), "m.room.encryption")) {
                MXCryptoImpl.this.onCryptoEvent(cryptoEvent);
            } else if (TextUtils.equals(cryptoEvent.getType(), "m.room.member")) {
                MXCryptoImpl.this.onRoomMembership(cryptoEvent);
            }
        }
    };
    /* access modifiers changed from: private */
    public final List<ApiCallback<Void>> mInitializationCallbacks = new ArrayList();
    /* access modifiers changed from: private */
    public boolean mIsStarted;
    /* access modifiers changed from: private */
    public boolean mIsStarting;
    /* access modifiers changed from: private */
    public final KeysBackup mKeysBackup;
    private long mLastOneTimeKeyCheck = 0;
    /* access modifiers changed from: private */
    public Map<String, Map<String, String>> mLastPublishedOneTimeKeys;
    private MXDeviceInfo mMyDevice;
    /* access modifiers changed from: private */
    public NetworkConnectivityReceiver mNetworkConnectivityReceiver;
    /* access modifiers changed from: private */
    public final IMXNetworkEventListener mNetworkListener = new IMXNetworkEventListener() {
        public void onNetworkConnectionUpdate(boolean z) {
            if (z && !MXCryptoImpl.this.isStarted()) {
                Log.d(MXCryptoImpl.LOG_TAG, "Start MXCrypto because a network connection has been retrieved ");
                MXCryptoImpl.this.start(false, null);
            }
        }
    };
    /* access modifiers changed from: private */
    public MXOlmDevice mOlmDevice;
    /* access modifiers changed from: private */
    public boolean mOneTimeKeyCheckInProgress = false;
    /* access modifiers changed from: private */
    public Integer mOneTimeKeyCount;
    /* access modifiers changed from: private */
    public final MXOutgoingRoomKeyRequestManager mOutgoingRoomKeyRequestManager;
    private final List<IncomingRoomKeyRequest> mReceivedRoomKeyRequestCancellations = new ArrayList();
    private final List<IncomingRoomKeyRequest> mReceivedRoomKeyRequests = new ArrayList();
    /* access modifiers changed from: private */
    public final Map<String, Map<String, IMXDecrypting>> mRoomDecryptors;
    /* access modifiers changed from: private */
    public final Map<String, IMXEncrypting> mRoomEncryptors;
    public final Set<RoomKeysRequestListener> mRoomKeysRequestListeners = new HashSet();
    /* access modifiers changed from: private */
    public final CryptoSession mSession;
    private final VerificationManager mShortCodeVerificationManager;
    private Handler mUIHandler;
    private boolean mWarnOnUnknownDevices = true;

    public MXCryptoImpl(CryptoSession cryptoSession, IMXCryptoStore iMXCryptoStore, MXCryptoConfig mXCryptoConfig, HomeServerConnectionConfig homeServerConnectionConfig) {
        HashMap hashMap;
        this.mSession = cryptoSession;
        this.mCryptoStore = iMXCryptoStore;
        this.mCryptoRestClient = new CryptoRestClient(homeServerConnectionConfig);
        if (mXCryptoConfig != null) {
            this.mCryptoConfig = mXCryptoConfig;
        } else {
            this.mCryptoConfig = new MXCryptoConfig();
        }
        this.mOlmDevice = new MXOlmDevice(this.mCryptoStore);
        this.mRoomEncryptors = new HashMap();
        this.mRoomDecryptors = new HashMap();
        String deviceId = this.mSession.getDeviceId();
        boolean z = !TextUtils.isEmpty(deviceId);
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = this.mCryptoStore.getDeviceId();
            this.mSession.setDeviceId(deviceId);
        }
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = UUID.randomUUID().toString();
            this.mSession.setDeviceId(deviceId);
            Log.d(LOG_TAG, "Warning: No device id in MXCredentials. An id was created. Think of storing it");
            this.mCryptoStore.storeDeviceId(deviceId);
        }
        this.mMyDevice = new MXDeviceInfo(deviceId);
        this.mMyDevice.userId = this.mSession.getMyUserId();
        this.mDevicesList = new MXDeviceList(cryptoSession, this);
        HashMap hashMap2 = new HashMap();
        if (!TextUtils.isEmpty(this.mOlmDevice.getDeviceEd25519Key())) {
            StringBuilder sb = new StringBuilder();
            sb.append("ed25519:");
            sb.append(this.mSession.getDeviceId());
            hashMap2.put(sb.toString(), this.mOlmDevice.getDeviceEd25519Key());
        }
        if (!TextUtils.isEmpty(this.mOlmDevice.getDeviceCurve25519Key())) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("curve25519:");
            sb2.append(this.mSession.getDeviceId());
            hashMap2.put(sb2.toString(), this.mOlmDevice.getDeviceCurve25519Key());
        }
        MXDeviceInfo mXDeviceInfo = this.mMyDevice;
        mXDeviceInfo.keys = hashMap2;
        mXDeviceInfo.algorithms = MXCryptoAlgorithms.sharedAlgorithms().supportedAlgorithms();
        this.mMyDevice.mVerified = 1;
        Map userDevices = this.mCryptoStore.getUserDevices(this.mSession.getMyUserId());
        if (userDevices != null) {
            hashMap = new HashMap(userDevices);
        } else {
            hashMap = new HashMap();
        }
        hashMap.put(this.mMyDevice.deviceId, this.mMyDevice);
        this.mCryptoStore.storeUserDevices(this.mSession.getMyUserId(), hashMap);
        this.mSession.getDataHandler().setCryptoEventsListener(this.mEventListener);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("MXCrypto_encrypting_");
        sb3.append(this.mSession.getMyUserId());
        this.mEncryptingHandlerThread = new HandlerThread(sb3.toString(), 1);
        this.mEncryptingHandlerThread.start();
        StringBuilder sb4 = new StringBuilder();
        sb4.append("MXCrypto_decrypting_");
        sb4.append(this.mSession.getMyUserId());
        this.mDecryptingHandlerThread = new HandlerThread(sb4.toString(), 1);
        this.mDecryptingHandlerThread.start();
        this.mUIHandler = new Handler(Looper.getMainLooper());
        if (z) {
            this.mDevicesList.handleDeviceListsChanges(Arrays.asList(new String[]{this.mSession.getMyUserId()}), null);
        }
        this.mOutgoingRoomKeyRequestManager = new MXOutgoingRoomKeyRequestManager(this);
        this.mReceivedRoomKeyRequests.addAll(this.mCryptoStore.getPendingIncomingRoomKeyRequests());
        this.mKeysBackup = new KeysBackup(this, homeServerConnectionConfig);
        this.mShortCodeVerificationManager = new VerificationManager(this.mSession);
    }

    public Handler getEncryptingThreadHandler() {
        if (this.mEncryptingHandler == null) {
            this.mEncryptingHandler = new Handler(this.mEncryptingHandlerThread.getLooper());
        }
        Handler handler = this.mEncryptingHandler;
        return handler == null ? this.mUIHandler : handler;
    }

    public Handler getDecryptingThreadHandler() {
        if (this.mDecryptingHandler == null) {
            this.mDecryptingHandler = new Handler(this.mDecryptingHandlerThread.getLooper());
        }
        Handler handler = this.mDecryptingHandler;
        return handler == null ? this.mUIHandler : handler;
    }

    public Handler getUIHandler() {
        return this.mUIHandler;
    }

    public void setNetworkConnectivityReceiver(NetworkConnectivityReceiver networkConnectivityReceiver) {
        this.mNetworkConnectivityReceiver = networkConnectivityReceiver;
    }

    public boolean isCorrupted() {
        IMXCryptoStore iMXCryptoStore = this.mCryptoStore;
        return iMXCryptoStore != null && iMXCryptoStore.isCorrupted();
    }

    public boolean hasBeenReleased() {
        return this.mOlmDevice == null;
    }

    public MXDeviceInfo getMyDevice() {
        return this.mMyDevice;
    }

    public IMXCryptoStore getCryptoStore() {
        return this.mCryptoStore;
    }

    public MXDeviceList getDeviceList() {
        return this.mDevicesList;
    }

    public int getDeviceTrackingStatus(String str) {
        return this.mCryptoStore.getDeviceTrackingStatus(str, -1);
    }

    public boolean isStarted() {
        return this.mIsStarted;
    }

    public boolean isStarting() {
        return this.mIsStarting;
    }

    public void start(final boolean z, ApiCallback<Void> apiCallback) {
        synchronized (this.mInitializationCallbacks) {
            if (apiCallback != null) {
                if (this.mInitializationCallbacks.indexOf(apiCallback) < 0) {
                    this.mInitializationCallbacks.add(apiCallback);
                }
            }
        }
        if (!this.mIsStarting) {
            NetworkConnectivityReceiver networkConnectivityReceiver = this.mNetworkConnectivityReceiver;
            if (networkConnectivityReceiver == null || networkConnectivityReceiver.isConnected()) {
                this.mIsStarting = true;
                getEncryptingThreadHandler().post(new Runnable() {
                    public void run() {
                        MXCryptoImpl.this.uploadDeviceKeys(new ApiCallback<KeysUploadResponse>() {
                            /* access modifiers changed from: private */
                            public void onError() {
                                MXCryptoImpl.this.getUIHandler().postDelayed(new Runnable() {
                                    public void run() {
                                        if (!MXCryptoImpl.this.isStarted()) {
                                            MXCryptoImpl.this.mIsStarting = false;
                                            MXCryptoImpl.this.start(z, null);
                                        }
                                    }
                                }, 1000);
                            }

                            public void onSuccess(KeysUploadResponse keysUploadResponse) {
                                MXCryptoImpl.this.getEncryptingThreadHandler().post(new Runnable() {
                                    public void run() {
                                        if (!MXCryptoImpl.this.hasBeenReleased()) {
                                            Log.d(MXCryptoImpl.LOG_TAG, "###########################################################");
                                            String access$000 = MXCryptoImpl.LOG_TAG;
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("uploadDeviceKeys done for ");
                                            sb.append(MXCryptoImpl.this.mSession.getMyUserId());
                                            Log.d(access$000, sb.toString());
                                            String access$0002 = MXCryptoImpl.LOG_TAG;
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append("  - device id  : ");
                                            sb2.append(MXCryptoImpl.this.mSession.getDeviceId());
                                            Log.d(access$0002, sb2.toString());
                                            String access$0003 = MXCryptoImpl.LOG_TAG;
                                            StringBuilder sb3 = new StringBuilder();
                                            sb3.append("  - ed25519    : ");
                                            sb3.append(MXCryptoImpl.this.mOlmDevice.getDeviceEd25519Key());
                                            Log.d(access$0003, sb3.toString());
                                            String access$0004 = MXCryptoImpl.LOG_TAG;
                                            StringBuilder sb4 = new StringBuilder();
                                            sb4.append("  - curve25519 : ");
                                            sb4.append(MXCryptoImpl.this.mOlmDevice.getDeviceCurve25519Key());
                                            Log.d(access$0004, sb4.toString());
                                            String access$0005 = MXCryptoImpl.LOG_TAG;
                                            StringBuilder sb5 = new StringBuilder();
                                            sb5.append("  - oneTimeKeys: ");
                                            sb5.append(MXCryptoImpl.this.mLastPublishedOneTimeKeys);
                                            Log.d(access$0005, sb5.toString());
                                            Log.d(MXCryptoImpl.LOG_TAG, "");
                                            MXCryptoImpl.this.getEncryptingThreadHandler().post(new Runnable() {
                                                public void run() {
                                                    MXCryptoImpl.this.maybeUploadOneTimeKeys(new ApiCallback<Void>() {
                                                        public void onSuccess(Void voidR) {
                                                            MXCryptoImpl.this.getEncryptingThreadHandler().post(new Runnable() {
                                                                public void run() {
                                                                    if (MXCryptoImpl.this.mNetworkConnectivityReceiver != null) {
                                                                        MXCryptoImpl.this.mNetworkConnectivityReceiver.removeEventListener(MXCryptoImpl.this.mNetworkListener);
                                                                    }
                                                                    MXCryptoImpl.this.mIsStarting = false;
                                                                    MXCryptoImpl.this.mIsStarted = true;
                                                                    MXCryptoImpl.this.mOutgoingRoomKeyRequestManager.start();
                                                                    MXCryptoImpl.this.mKeysBackup.checkAndStartKeysBackup();
                                                                    synchronized (MXCryptoImpl.this.mInitializationCallbacks) {
                                                                        for (final ApiCallback apiCallback : MXCryptoImpl.this.mInitializationCallbacks) {
                                                                            MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                                                                                public void run() {
                                                                                    apiCallback.onSuccess(null);
                                                                                }
                                                                            });
                                                                        }
                                                                        MXCryptoImpl.this.mInitializationCallbacks.clear();
                                                                    }
                                                                    if (z) {
                                                                        MXCryptoImpl.this.getEncryptingThreadHandler().post(new Runnable() {
                                                                            public void run() {
                                                                                MXCryptoImpl.this.getDeviceList().invalidateAllDeviceLists();
                                                                                MXCryptoImpl.this.mDevicesList.refreshOutdatedDeviceLists();
                                                                            }
                                                                        });
                                                                    } else {
                                                                        MXCryptoImpl.this.getEncryptingThreadHandler().post(new Runnable() {
                                                                            public void run() {
                                                                                MXCryptoImpl.this.processReceivedRoomKeyRequests();
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });
                                                        }

                                                        public void onNetworkError(Exception exc) {
                                                            String access$000 = MXCryptoImpl.LOG_TAG;
                                                            StringBuilder sb = new StringBuilder();
                                                            sb.append("## start failed : ");
                                                            sb.append(exc.getMessage());
                                                            Log.e(access$000, sb.toString(), exc);
                                                            AnonymousClass1.this.onError();
                                                        }

                                                        public void onMatrixError(MatrixError matrixError) {
                                                            String access$000 = MXCryptoImpl.LOG_TAG;
                                                            StringBuilder sb = new StringBuilder();
                                                            sb.append("## start failed : ");
                                                            sb.append(matrixError.getMessage());
                                                            Log.e(access$000, sb.toString());
                                                            AnonymousClass1.this.onError();
                                                        }

                                                        public void onUnexpectedError(Exception exc) {
                                                            String access$000 = MXCryptoImpl.LOG_TAG;
                                                            StringBuilder sb = new StringBuilder();
                                                            sb.append("## start failed : ");
                                                            sb.append(exc.getMessage());
                                                            Log.e(access$000, sb.toString(), exc);
                                                            AnonymousClass1.this.onError();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                            }

                            public void onNetworkError(Exception exc) {
                                String access$000 = MXCryptoImpl.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## start failed : ");
                                sb.append(exc.getMessage());
                                Log.e(access$000, sb.toString(), exc);
                                onError();
                            }

                            public void onMatrixError(MatrixError matrixError) {
                                String access$000 = MXCryptoImpl.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## start failed : ");
                                sb.append(matrixError.getMessage());
                                Log.e(access$000, sb.toString());
                                onError();
                            }

                            public void onUnexpectedError(Exception exc) {
                                String access$000 = MXCryptoImpl.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## start failed : ");
                                sb.append(exc.getMessage());
                                Log.e(access$000, sb.toString(), exc);
                                onError();
                            }
                        });
                    }
                });
                return;
            }
            this.mNetworkConnectivityReceiver.removeEventListener(this.mNetworkListener);
            this.mNetworkConnectivityReceiver.addEventListener(this.mNetworkListener);
        }
    }

    public void close() {
        if (this.mEncryptingHandlerThread != null) {
            this.mSession.getDataHandler().setCryptoEventsListener(null);
            getEncryptingThreadHandler().post(new Runnable() {
                public void run() {
                    if (MXCryptoImpl.this.mOlmDevice != null) {
                        MXCryptoImpl.this.mOlmDevice.release();
                        MXCryptoImpl.this.mOlmDevice = null;
                    }
                    MXCryptoImpl.this.mCryptoStore.close();
                    if (MXCryptoImpl.this.mEncryptingHandlerThread != null) {
                        MXCryptoImpl.this.mEncryptingHandlerThread.quit();
                        MXCryptoImpl.this.mEncryptingHandlerThread = null;
                    }
                    MXCryptoImpl.this.mOutgoingRoomKeyRequestManager.stop();
                }
            });
            getDecryptingThreadHandler().post(new Runnable() {
                public void run() {
                    if (MXCryptoImpl.this.mDecryptingHandlerThread != null) {
                        MXCryptoImpl.this.mDecryptingHandlerThread.quit();
                        MXCryptoImpl.this.mDecryptingHandlerThread = null;
                    }
                }
            });
        }
    }

    public MXOlmDevice getOlmDevice() {
        return this.mOlmDevice;
    }

    public KeysBackup getKeysBackup() {
        return this.mKeysBackup;
    }

    public VerificationManager getShortCodeVerificationManager() {
        return this.mShortCodeVerificationManager;
    }

    public void onSyncCompleted(final CryptoSyncResponse cryptoSyncResponse, String str, final boolean z) {
        getEncryptingThreadHandler().post(new Runnable() {
            public void run() {
                if (cryptoSyncResponse.getDeviceLists() != null) {
                    MXCryptoImpl.this.getDeviceList().handleDeviceListsChanges(cryptoSyncResponse.getDeviceLists().getChanged(), cryptoSyncResponse.getDeviceLists().getLeft());
                }
                if (cryptoSyncResponse.getDeviceOneTimeKeysCount() != null) {
                    MXCryptoImpl.this.updateOneTimeKeyCount(cryptoSyncResponse.getDeviceOneTimeKeysCount().getSignedCurve25519() != null ? cryptoSyncResponse.getDeviceOneTimeKeysCount().getSignedCurve25519().intValue() : 0);
                }
                if (MXCryptoImpl.this.isStarted()) {
                    MXCryptoImpl.this.mDevicesList.refreshOutdatedDeviceLists();
                }
                if (!z && MXCryptoImpl.this.isStarted()) {
                    MXCryptoImpl.this.maybeUploadOneTimeKeys();
                    MXCryptoImpl.this.processReceivedRoomKeyRequests();
                }
            }
        });
    }

    public void getUserDevices(final String str, final ApiCallback<List<MXDeviceInfo>> apiCallback) {
        getEncryptingThreadHandler().post(new Runnable() {
            public void run() {
                final List userDevices = MXCryptoImpl.this.getUserDevices(str);
                if (apiCallback != null) {
                    MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                        public void run() {
                            apiCallback.onSuccess(userDevices);
                        }
                    });
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void updateOneTimeKeyCount(int i) {
        this.mOneTimeKeyCount = Integer.valueOf(i);
    }

    public MXDeviceInfo deviceWithIdentityKey(String str, String str2) {
        if (hasBeenReleased()) {
            return null;
        }
        if (TextUtils.equals(str2, CryptoConstantsKt.MXCRYPTO_ALGORITHM_MEGOLM) || TextUtils.equals(str2, CryptoConstantsKt.MXCRYPTO_ALGORITHM_OLM)) {
            return getCryptoStore().deviceWithIdentityKey(str);
        }
        return null;
    }

    public void getDeviceInfo(final String str, final String str2, final ApiCallback<MXDeviceInfo> apiCallback) {
        getDecryptingThreadHandler().post(new Runnable() {
            public void run() {
                final MXDeviceInfo userDevice = (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) ? null : MXCryptoImpl.this.mCryptoStore.getUserDevice(str2, str);
                if (apiCallback != null) {
                    MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                        public void run() {
                            apiCallback.onSuccess(userDevice);
                        }
                    });
                }
            }
        });
    }

    public void setDevicesKnown(final List<? extends MXDeviceInfo> list, final ApiCallback<Void> apiCallback) {
        if (!hasBeenReleased()) {
            getEncryptingThreadHandler().post(new Runnable() {
                public void run() {
                    HashMap hashMap = new HashMap();
                    for (MXDeviceInfo mXDeviceInfo : list) {
                        List list = (List) hashMap.get(mXDeviceInfo.userId);
                        if (list == null) {
                            list = new ArrayList();
                            hashMap.put(mXDeviceInfo.userId, list);
                        }
                        list.add(mXDeviceInfo.deviceId);
                    }
                    for (String str : hashMap.keySet()) {
                        Map userDevices = MXCryptoImpl.this.mCryptoStore.getUserDevices(str);
                        if (userDevices != null) {
                            boolean z = false;
                            for (String str2 : (List) hashMap.get(str)) {
                                MXDeviceInfo mXDeviceInfo2 = (MXDeviceInfo) userDevices.get(str2);
                                if (mXDeviceInfo2 != null && mXDeviceInfo2.isUnknown()) {
                                    mXDeviceInfo2.mVerified = 0;
                                    z = true;
                                }
                            }
                            if (z) {
                                MXCryptoImpl.this.mCryptoStore.storeUserDevices(str, userDevices);
                            }
                        }
                    }
                    if (apiCallback != null) {
                        MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                            public void run() {
                                apiCallback.onSuccess(null);
                            }
                        });
                    }
                }
            });
        }
    }

    public void setDeviceVerification(int i, String str, String str2, ApiCallback<Void> apiCallback) {
        if (!hasBeenReleased()) {
            Handler encryptingThreadHandler = getEncryptingThreadHandler();
            final String str3 = str;
            final String str4 = str2;
            final ApiCallback<Void> apiCallback2 = apiCallback;
            final int i2 = i;
            AnonymousClass10 r1 = new Runnable() {
                public void run() {
                    MXDeviceInfo userDevice = MXCryptoImpl.this.mCryptoStore.getUserDevice(str3, str4);
                    if (userDevice == null) {
                        String access$000 = MXCryptoImpl.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## setDeviceVerification() : Unknown device ");
                        sb.append(str4);
                        sb.append(":");
                        sb.append(str3);
                        Log.e(access$000, sb.toString());
                        if (apiCallback2 != null) {
                            MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                                public void run() {
                                    apiCallback2.onSuccess(null);
                                }
                            });
                        }
                        return;
                    }
                    int i = userDevice.mVerified;
                    int i2 = i2;
                    if (i != i2) {
                        userDevice.mVerified = i2;
                        MXCryptoImpl.this.mCryptoStore.storeUserDevice(str4, userDevice);
                        if (str4.equals(MXCryptoImpl.this.mSession.getMyUserId())) {
                            MXCryptoImpl.this.mKeysBackup.checkAndStartKeysBackup();
                        }
                    }
                    if (apiCallback2 != null) {
                        MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                            public void run() {
                                apiCallback2.onSuccess(null);
                            }
                        });
                    }
                }
            };
            encryptingThreadHandler.post(r1);
        }
    }

    /* access modifiers changed from: private */
    public boolean setEncryptionInRoom(String str, String str2, boolean z, List<CryptoRoomMember> list) {
        if (hasBeenReleased()) {
            return false;
        }
        String roomAlgorithm = this.mCryptoStore.getRoomAlgorithm(str);
        if (TextUtils.isEmpty(roomAlgorithm) || TextUtils.equals(roomAlgorithm, str2)) {
            Class encryptorClassForAlgorithm = MXCryptoAlgorithms.sharedAlgorithms().encryptorClassForAlgorithm(str2);
            if (encryptorClassForAlgorithm == null) {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## setEncryptionInRoom() : Unable to encrypt with ");
                sb.append(str2);
                Log.e(str3, sb.toString());
                return false;
            }
            this.mCryptoStore.storeRoomAlgorithm(str, str2);
            try {
                IMXEncrypting iMXEncrypting = (IMXEncrypting) encryptorClassForAlgorithm.getConstructors()[0].newInstance(new Object[0]);
                iMXEncrypting.initWithMatrixSession(this.mSession, this, str);
                synchronized (this.mRoomEncryptors) {
                    this.mRoomEncryptors.put(str, iMXEncrypting);
                }
                if (roomAlgorithm == null) {
                    String str4 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Enabling encryption in ");
                    sb2.append(str);
                    sb2.append(" for the first time; invalidating device lists for all users therein");
                    Log.d(str4, sb2.toString());
                    ArrayList arrayList = new ArrayList();
                    for (CryptoRoomMember userId : list) {
                        arrayList.add(userId.getUserId());
                    }
                    getDeviceList().startTrackingDeviceList(arrayList);
                    if (!z) {
                        getDeviceList().refreshOutdatedDeviceLists();
                    }
                }
                return true;
            } catch (Exception e) {
                Log.e(LOG_TAG, "## setEncryptionInRoom() : fail to load the class", e);
                return false;
            }
        } else {
            String str5 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("## setEncryptionInRoom() : Ignoring m.room.encryption event which requests a change of config in ");
            sb3.append(str);
            Log.e(str5, sb3.toString());
            return false;
        }
    }

    public boolean isRoomEncrypted(String str) {
        boolean containsKey;
        if (str == null) {
            return false;
        }
        synchronized (this.mRoomEncryptors) {
            containsKey = this.mRoomEncryptors.containsKey(str);
            if (!containsKey) {
                CryptoRoom room = this.mSession.getDataHandler().getRoom(str);
                if (room != null) {
                    containsKey = room.getState().isEncrypted();
                }
            }
        }
        return containsKey;
    }

    public List<MXDeviceInfo> getUserDevices(String str) {
        ArrayList arrayList;
        Map userDevices = getCryptoStore().getUserDevices(str);
        if (userDevices != null) {
            arrayList = new ArrayList(userDevices.values());
        } else {
            arrayList = new ArrayList();
        }
        return arrayList;
    }

    public void ensureOlmSessionsForUsers(List<String> list, ApiCallback<MXUsersDevicesMap<MXOlmSessionResult>> apiCallback) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## ensureOlmSessionsForUsers() : ensureOlmSessionsForUsers ");
        sb.append(list);
        Log.d(str, sb.toString());
        HashMap hashMap = new HashMap();
        for (String str2 : list) {
            hashMap.put(str2, new ArrayList());
            for (MXDeviceInfo mXDeviceInfo : getUserDevices(str2)) {
                if (!TextUtils.equals(mXDeviceInfo.identityKey(), this.mOlmDevice.getDeviceCurve25519Key()) && !mXDeviceInfo.isVerified()) {
                    ((List) hashMap.get(str2)).add(mXDeviceInfo);
                }
            }
        }
        ensureOlmSessionsForDevices(hashMap, apiCallback);
    }

    public void ensureOlmSessionsForDevices(final Map<String, List<MXDeviceInfo>> map, final ApiCallback<MXUsersDevicesMap<MXOlmSessionResult>> apiCallback) {
        ArrayList<MXDeviceInfo> arrayList = new ArrayList<>();
        final MXUsersDevicesMap mXUsersDevicesMap = new MXUsersDevicesMap();
        for (String str : map.keySet()) {
            for (MXDeviceInfo mXDeviceInfo : (List) map.get(str)) {
                String str2 = mXDeviceInfo.deviceId;
                String sessionId = this.mOlmDevice.getSessionId(mXDeviceInfo.identityKey());
                if (TextUtils.isEmpty(sessionId)) {
                    arrayList.add(mXDeviceInfo);
                }
                mXUsersDevicesMap.setObject(new MXOlmSessionResult(mXDeviceInfo, sessionId), str, str2);
            }
        }
        if (arrayList.size() == 0) {
            if (apiCallback != null) {
                getUIHandler().post(new Runnable() {
                    public void run() {
                        apiCallback.onSuccess(mXUsersDevicesMap);
                    }
                });
            }
            return;
        }
        MXUsersDevicesMap mXUsersDevicesMap2 = new MXUsersDevicesMap();
        for (MXDeviceInfo mXDeviceInfo2 : arrayList) {
            mXUsersDevicesMap2.setObject(MXKey.KEY_SIGNED_CURVE_25519_TYPE, mXDeviceInfo2.userId, mXDeviceInfo2.deviceId);
        }
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## claimOneTimeKeysForUsersDevices() : ");
        sb.append(mXUsersDevicesMap2);
        Log.d(str3, sb.toString());
        this.mCryptoRestClient.claimOneTimeKeysForUsersDevices(mXUsersDevicesMap2, new ApiCallback<MXUsersDevicesMap<MXKey>>() {
            public void onSuccess(final MXUsersDevicesMap<MXKey> mXUsersDevicesMap) {
                MXCryptoImpl.this.getEncryptingThreadHandler().post(new Runnable() {
                    public void run() {
                        try {
                            String access$000 = MXCryptoImpl.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## claimOneTimeKeysForUsersDevices() : keysClaimResponse.oneTimeKeys: ");
                            sb.append(mXUsersDevicesMap);
                            Log.d(access$000, sb.toString());
                            for (String str : map.keySet()) {
                                for (MXDeviceInfo mXDeviceInfo : (List) map.get(str)) {
                                    MXKey mXKey = null;
                                    List<String> userDeviceIds = mXUsersDevicesMap.getUserDeviceIds(str);
                                    if (userDeviceIds != null) {
                                        for (String str2 : userDeviceIds) {
                                            MXOlmSessionResult mXOlmSessionResult = (MXOlmSessionResult) mXUsersDevicesMap.getObject(str2, str);
                                            if (mXOlmSessionResult.mSessionId == null) {
                                                MXKey mXKey2 = (MXKey) mXUsersDevicesMap.getObject(str2, str);
                                                if (TextUtils.equals(mXKey2.type, MXKey.KEY_SIGNED_CURVE_25519_TYPE)) {
                                                    mXKey = mXKey2;
                                                }
                                                if (mXKey == null) {
                                                    String access$0002 = MXCryptoImpl.LOG_TAG;
                                                    StringBuilder sb2 = new StringBuilder();
                                                    sb2.append("## ensureOlmSessionsForDevices() : No one-time keys signed_curve25519 for device ");
                                                    sb2.append(str);
                                                    sb2.append(" : ");
                                                    sb2.append(str2);
                                                    Log.d(access$0002, sb2.toString());
                                                } else {
                                                    mXOlmSessionResult.mSessionId = MXCryptoImpl.this.verifyKeyAndStartSession(mXKey, str, mXDeviceInfo);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            String access$0003 = MXCryptoImpl.LOG_TAG;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("## ensureOlmSessionsForDevices() ");
                            sb3.append(e.getMessage());
                            Log.e(access$0003, sb3.toString(), e);
                        }
                        if (!MXCryptoImpl.this.hasBeenReleased() && apiCallback != null) {
                            MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                                public void run() {
                                    apiCallback.onSuccess(mXUsersDevicesMap);
                                }
                            });
                        }
                    }
                });
            }

            public void onNetworkError(Exception exc) {
                String access$000 = MXCryptoImpl.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## ensureOlmSessionsForUsers(): claimOneTimeKeysForUsersDevices request failed");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onNetworkError(exc);
                }
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$000 = MXCryptoImpl.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## ensureOlmSessionsForUsers(): claimOneTimeKeysForUsersDevices request failed");
                sb.append(matrixError.getMessage());
                Log.e(access$000, sb.toString());
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onMatrixError(matrixError);
                }
            }

            public void onUnexpectedError(Exception exc) {
                String access$000 = MXCryptoImpl.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## ensureOlmSessionsForUsers(): claimOneTimeKeysForUsersDevices request failed");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onUnexpectedError(exc);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public String verifyKeyAndStartSession(MXKey mXKey, String str, MXDeviceInfo mXDeviceInfo) {
        String str2;
        String str3 = mXDeviceInfo.deviceId;
        StringBuilder sb = new StringBuilder();
        sb.append("ed25519:");
        sb.append(str3);
        String signatureForUserId = mXKey.signatureForUserId(str, sb.toString());
        String str4 = null;
        if (!TextUtils.isEmpty(signatureForUserId) && !TextUtils.isEmpty(mXDeviceInfo.fingerprint())) {
            boolean z = false;
            try {
                this.mOlmDevice.verifySignature(mXDeviceInfo.fingerprint(), mXKey.signalableJSONDictionary(), signatureForUserId);
                z = true;
                str2 = null;
            } catch (Exception e) {
                str2 = e.getMessage();
            }
            String str5 = ":";
            if (z) {
                str4 = getOlmDevice().createOutboundSession(mXDeviceInfo.identityKey(), mXKey.value);
                if (!TextUtils.isEmpty(str4)) {
                    String str6 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## verifyKeyAndStartSession() : Started new sessionid ");
                    sb2.append(str4);
                    sb2.append(" for device ");
                    sb2.append(mXDeviceInfo);
                    sb2.append("(theirOneTimeKey: ");
                    sb2.append(mXKey.value);
                    sb2.append(")");
                    Log.d(str6, sb2.toString());
                } else {
                    String str7 = LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("## verifyKeyAndStartSession() : Error starting session with device ");
                    sb3.append(str);
                    sb3.append(str5);
                    sb3.append(str3);
                    Log.e(str7, sb3.toString());
                }
            } else {
                String str8 = LOG_TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("## verifyKeyAndStartSession() : Unable to verify signature on one-time key for device ");
                sb4.append(str);
                sb4.append(str5);
                sb4.append(str3);
                sb4.append(" Error ");
                sb4.append(str2);
                Log.e(str8, sb4.toString());
            }
        }
        return str4;
    }

    public void encryptEventContent(JsonElement jsonElement, String str, CryptoRoom cryptoRoom, ApiCallback<MXEncryptEventContentResult> apiCallback) {
        boolean z = false;
        if (!isStarted()) {
            Log.d(LOG_TAG, "## encryptEventContent() : wait after e2e init");
            final JsonElement jsonElement2 = jsonElement;
            final String str2 = str;
            final CryptoRoom cryptoRoom2 = cryptoRoom;
            final ApiCallback<MXEncryptEventContentResult> apiCallback2 = apiCallback;
            AnonymousClass13 r3 = new ApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    MXCryptoImpl.this.encryptEventContent(jsonElement2, str2, cryptoRoom2, apiCallback2);
                }

                public void onNetworkError(Exception exc) {
                    String access$000 = MXCryptoImpl.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## encryptEventContent() : onNetworkError while waiting to start e2e : ");
                    sb.append(exc.getMessage());
                    Log.e(access$000, sb.toString(), exc);
                    ApiCallback apiCallback = apiCallback2;
                    if (apiCallback != null) {
                        apiCallback.onNetworkError(exc);
                    }
                }

                public void onMatrixError(MatrixError matrixError) {
                    String access$000 = MXCryptoImpl.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## encryptEventContent() : onMatrixError while waiting to start e2e : ");
                    sb.append(matrixError.getMessage());
                    Log.e(access$000, sb.toString());
                    ApiCallback apiCallback = apiCallback2;
                    if (apiCallback != null) {
                        apiCallback.onMatrixError(matrixError);
                    }
                }

                public void onUnexpectedError(Exception exc) {
                    String access$000 = MXCryptoImpl.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## encryptEventContent() : onUnexpectedError while waiting to start e2e : ");
                    sb.append(exc.getMessage());
                    Log.e(access$000, sb.toString(), exc);
                    ApiCallback apiCallback = apiCallback2;
                    if (apiCallback != null) {
                        apiCallback.onUnexpectedError(exc);
                    }
                }
            };
            start(false, r3);
            return;
        }
        final CryptoRoom cryptoRoom3 = cryptoRoom;
        final JsonElement jsonElement3 = jsonElement;
        final String str3 = str;
        final ApiCallback<MXEncryptEventContentResult> apiCallback3 = apiCallback;
        AnonymousClass14 r2 = new SimpleApiCallback<List<CryptoRoomMember>>(apiCallback) {
            public void onSuccess(final List<CryptoRoomMember> list) {
                final ArrayList arrayList = new ArrayList();
                for (CryptoRoomMember userId : list) {
                    arrayList.add(userId.getUserId());
                }
                MXCryptoImpl.this.getEncryptingThreadHandler().post(new Runnable() {
                    public void run() {
                        IMXEncrypting iMXEncrypting;
                        synchronized (MXCryptoImpl.this.mRoomEncryptors) {
                            iMXEncrypting = (IMXEncrypting) MXCryptoImpl.this.mRoomEncryptors.get(cryptoRoom3.getRoomId());
                        }
                        if (iMXEncrypting == null) {
                            String encryptionAlgorithm = cryptoRoom3.getState().encryptionAlgorithm();
                            if (encryptionAlgorithm != null && MXCryptoImpl.this.setEncryptionInRoom(cryptoRoom3.getRoomId(), encryptionAlgorithm, false, list)) {
                                synchronized (MXCryptoImpl.this.mRoomEncryptors) {
                                    iMXEncrypting = (IMXEncrypting) MXCryptoImpl.this.mRoomEncryptors.get(cryptoRoom3.getRoomId());
                                }
                            }
                        }
                        if (iMXEncrypting != null) {
                            final long currentTimeMillis = System.currentTimeMillis();
                            Log.d(MXCryptoImpl.LOG_TAG, "## encryptEventContent() starts");
                            iMXEncrypting.encryptEventContent(jsonElement3, str3, arrayList, new ApiCallback<JsonElement>() {
                                public void onSuccess(JsonElement jsonElement) {
                                    String access$000 = MXCryptoImpl.LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("## encryptEventContent() : succeeds after ");
                                    sb.append(System.currentTimeMillis() - currentTimeMillis);
                                    sb.append(" ms");
                                    Log.d(access$000, sb.toString());
                                    if (apiCallback3 != null) {
                                        apiCallback3.onSuccess(new MXEncryptEventContentResult(jsonElement, "m.room.encrypted"));
                                    }
                                }

                                public void onNetworkError(Exception exc) {
                                    String access$000 = MXCryptoImpl.LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("## encryptEventContent() : onNetworkError ");
                                    sb.append(exc.getMessage());
                                    Log.e(access$000, sb.toString(), exc);
                                    if (apiCallback3 != null) {
                                        apiCallback3.onNetworkError(exc);
                                    }
                                }

                                public void onMatrixError(MatrixError matrixError) {
                                    String access$000 = MXCryptoImpl.LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("## encryptEventContent() : onMatrixError ");
                                    sb.append(matrixError.getMessage());
                                    Log.e(access$000, sb.toString());
                                    if (apiCallback3 != null) {
                                        apiCallback3.onMatrixError(matrixError);
                                    }
                                }

                                public void onUnexpectedError(Exception exc) {
                                    String access$000 = MXCryptoImpl.LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("## encryptEventContent() : onUnexpectedError ");
                                    sb.append(exc.getMessage());
                                    Log.e(access$000, sb.toString(), exc);
                                    if (apiCallback3 != null) {
                                        apiCallback3.onUnexpectedError(exc);
                                    }
                                }
                            });
                            return;
                        }
                        String encryptionAlgorithm2 = cryptoRoom3.getState().encryptionAlgorithm();
                        Object[] objArr = new Object[1];
                        if (encryptionAlgorithm2 == null) {
                            encryptionAlgorithm2 = MXCryptoError.NO_MORE_ALGORITHM_REASON;
                        }
                        objArr[0] = encryptionAlgorithm2;
                        final String format = String.format(MXCryptoError.UNABLE_TO_ENCRYPT_REASON, objArr);
                        String access$000 = MXCryptoImpl.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## encryptEventContent() : ");
                        sb.append(format);
                        Log.e(access$000, sb.toString());
                        if (apiCallback3 != null) {
                            MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                                public void run() {
                                    apiCallback3.onMatrixError(new MXCryptoError(MXCryptoError.UNABLE_TO_ENCRYPT_ERROR_CODE, MXCryptoError.UNABLE_TO_ENCRYPT, format));
                                }
                            });
                        }
                    }
                });
            }
        };
        if (this.mCryptoConfig.mEnableEncryptionForInvitedMembers && cryptoRoom.shouldEncryptForInvitedMembers()) {
            z = true;
        }
        if (z) {
            cryptoRoom.getActiveMembersAsyncCrypto(r2);
        } else {
            cryptoRoom.getJoinedMembersAsyncCrypto(r2);
        }
    }

    public MXEventDecryptionResult decryptEvent(CryptoEvent cryptoEvent, String str) throws MXDecryptionException {
        if (cryptoEvent == null) {
            Log.e(LOG_TAG, "## decryptEvent : null event");
            return null;
        }
        final CryptoEventContent wireEventContent = cryptoEvent.getWireEventContent();
        if (wireEventContent == null) {
            Log.e(LOG_TAG, "## decryptEvent : empty event content");
            return null;
        }
        ArrayList arrayList = new ArrayList();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ArrayList arrayList2 = new ArrayList();
        Handler decryptingThreadHandler = getDecryptingThreadHandler();
        final CryptoEvent cryptoEvent2 = cryptoEvent;
        final ArrayList arrayList3 = arrayList2;
        final String str2 = str;
        final ArrayList arrayList4 = arrayList;
        final CountDownLatch countDownLatch2 = countDownLatch;
        AnonymousClass15 r2 = new Runnable() {
            public void run() {
                MXEventDecryptionResult mXEventDecryptionResult;
                IMXDecrypting access$2600 = MXCryptoImpl.this.getRoomDecryptor(cryptoEvent2.getRoomId(), wireEventContent.getAlgorithm());
                if (access$2600 == null) {
                    String format = String.format(MXCryptoError.UNABLE_TO_DECRYPT_REASON, new Object[]{cryptoEvent2.getEventId(), wireEventContent.getAlgorithm()});
                    String access$000 = MXCryptoImpl.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## decryptEvent() : ");
                    sb.append(format);
                    Log.e(access$000, sb.toString());
                    arrayList3.add(new MXDecryptionException(new MXCryptoError(MXCryptoError.UNABLE_TO_DECRYPT_ERROR_CODE, MXCryptoError.UNABLE_TO_DECRYPT, format)));
                } else {
                    try {
                        mXEventDecryptionResult = access$2600.decryptEvent(cryptoEvent2, str2);
                    } catch (MXDecryptionException e) {
                        arrayList3.add(e);
                        mXEventDecryptionResult = null;
                    }
                    if (mXEventDecryptionResult != null) {
                        arrayList4.add(mXEventDecryptionResult);
                    }
                }
                countDownLatch2.countDown();
            }
        };
        decryptingThreadHandler.post(r2);
        try {
            countDownLatch.await();
        } catch (Exception e) {
            Exception exc = e;
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## decryptEvent() : failed ");
            sb.append(exc.getMessage());
            Log.e(str3, sb.toString(), exc);
        }
        if (!arrayList2.isEmpty()) {
            throw ((MXDecryptionException) arrayList2.get(0));
        } else if (!arrayList.isEmpty()) {
            return (MXEventDecryptionResult) arrayList.get(0);
        } else {
            return null;
        }
    }

    public void resetReplayAttackCheckInTimeline(final String str) {
        if (str != null && getOlmDevice() != null) {
            getDecryptingThreadHandler().post(new Runnable() {
                public void run() {
                    MXCryptoImpl.this.getOlmDevice().resetReplayAttackCheckInTimeline(str);
                }
            });
        }
    }

    public EncryptedMessage encryptMessage(Map<String, Object> map, List<MXDeviceInfo> list) {
        if (hasBeenReleased()) {
            return new EncryptedMessage();
        }
        HashMap hashMap = new HashMap();
        ArrayList<String> arrayList = new ArrayList<>();
        for (MXDeviceInfo mXDeviceInfo : list) {
            arrayList.add(mXDeviceInfo.identityKey());
            hashMap.put(mXDeviceInfo.identityKey(), mXDeviceInfo);
        }
        HashMap hashMap2 = new HashMap(map);
        hashMap2.put(BingRule.KIND_SENDER, this.mSession.getMyUserId());
        hashMap2.put("sender_device", this.mSession.getDeviceId());
        HashMap hashMap3 = new HashMap();
        String deviceEd25519Key = this.mOlmDevice.getDeviceEd25519Key();
        String str = OlmAccount.JSON_KEY_FINGER_PRINT_KEY;
        hashMap3.put(str, deviceEd25519Key);
        hashMap2.put("keys", hashMap3);
        HashMap hashMap4 = new HashMap();
        for (String str2 : arrayList) {
            String sessionId = this.mOlmDevice.getSessionId(str2);
            if (!TextUtils.isEmpty(sessionId)) {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Using sessionid ");
                sb.append(sessionId);
                sb.append(" for device ");
                sb.append(str2);
                Log.d(str3, sb.toString());
                MXDeviceInfo mXDeviceInfo2 = (MXDeviceInfo) hashMap.get(str2);
                hashMap2.put("recipient", mXDeviceInfo2.userId);
                HashMap hashMap5 = new HashMap();
                hashMap5.put(str, mXDeviceInfo2.fingerprint());
                hashMap2.put("recipient_keys", hashMap5);
                hashMap4.put(str2, this.mOlmDevice.encryptMessage(str2, sessionId, StringUtilsKt.convertToUTF8(JsonUtility.canonicalize(JsonUtility.getGson(false).toJsonTree(hashMap2)).toString())));
            }
        }
        EncryptedMessage encryptedMessage = new EncryptedMessage();
        encryptedMessage.algorithm = CryptoConstantsKt.MXCRYPTO_ALGORITHM_OLM;
        encryptedMessage.senderKey = this.mOlmDevice.getDeviceCurve25519Key();
        encryptedMessage.cipherText = hashMap4;
        return encryptedMessage;
    }

    public Map<String, Map<String, String>> signObject(String str) {
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        StringBuilder sb = new StringBuilder();
        sb.append("ed25519:");
        sb.append(this.mMyDevice.deviceId);
        hashMap2.put(sb.toString(), this.mOlmDevice.signMessage(str));
        hashMap.put(this.mMyDevice.userId, hashMap2);
        return hashMap;
    }

    /* access modifiers changed from: private */
    public void onToDeviceEvent(final CryptoEvent cryptoEvent) {
        this.mShortCodeVerificationManager.onToDeviceEvent(cryptoEvent);
        if (TextUtils.equals(cryptoEvent.getType(), "m.room_key") || TextUtils.equals(cryptoEvent.getType(), "m.forwarded_room_key")) {
            getDecryptingThreadHandler().post(new Runnable() {
                public void run() {
                    MXCryptoImpl.this.onRoomKeyEvent(cryptoEvent);
                }
            });
        } else if (TextUtils.equals(cryptoEvent.getType(), "m.room_key_request")) {
            getEncryptingThreadHandler().post(new Runnable() {
                public void run() {
                    MXCryptoImpl.this.onRoomKeyRequestEvent(cryptoEvent);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void onRoomKeyEvent(CryptoEvent cryptoEvent) {
        if (cryptoEvent == null) {
            Log.e(LOG_TAG, "## onRoomKeyEvent() : null event");
            return;
        }
        RoomKeyContent roomKeyContent = cryptoEvent.toRoomKeyContent();
        String str = roomKeyContent.room_id;
        String str2 = roomKeyContent.algorithm;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            Log.e(LOG_TAG, "## onRoomKeyEvent() : missing fields");
            return;
        }
        IMXDecrypting roomDecryptor = getRoomDecryptor(str, str2);
        if (roomDecryptor == null) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onRoomKeyEvent() : Unable to handle keys for ");
            sb.append(str2);
            Log.e(str3, sb.toString());
            return;
        }
        roomDecryptor.onRoomKeyEvent(cryptoEvent);
    }

    /* access modifiers changed from: private */
    public void onRoomKeyRequestEvent(CryptoEvent cryptoEvent) {
        RoomKeyShare roomKeyShare = cryptoEvent.toRoomKeyShare();
        if (roomKeyShare.action != null) {
            String str = roomKeyShare.action;
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != -524427085) {
                if (hashCode == 1095692943 && str.equals(RoomKeyShare.ACTION_SHARE_REQUEST)) {
                    c = 0;
                }
            } else if (str.equals(RoomKeyShare.ACTION_SHARE_CANCELLATION)) {
                c = 1;
            }
            if (c == 0) {
                synchronized (this.mReceivedRoomKeyRequests) {
                    this.mReceivedRoomKeyRequests.add(new IncomingRoomKeyRequest(cryptoEvent));
                }
            } else if (c != 1) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## onRoomKeyRequestEvent() : unsupported action ");
                sb.append(roomKeyShare.action);
                Log.e(str2, sb.toString());
            } else {
                synchronized (this.mReceivedRoomKeyRequestCancellations) {
                    this.mReceivedRoomKeyRequestCancellations.add(new IncomingRoomKeyRequestCancellation(cryptoEvent));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void processReceivedRoomKeyRequests() {
        ArrayList<IncomingRoomKeyRequestCancellation> arrayList;
        List<IncomingRoomKeyRequest> list;
        synchronized (this.mReceivedRoomKeyRequests) {
            arrayList = null;
            if (!this.mReceivedRoomKeyRequests.isEmpty()) {
                list = new ArrayList<>(this.mReceivedRoomKeyRequests);
                this.mReceivedRoomKeyRequests.clear();
            } else {
                list = null;
            }
        }
        if (list != null) {
            for (final IncomingRoomKeyRequest incomingRoomKeyRequest : list) {
                String str = incomingRoomKeyRequest.mUserId;
                String str2 = incomingRoomKeyRequest.mDeviceId;
                RoomKeyRequestBody roomKeyRequestBody = incomingRoomKeyRequest.mRequestBody;
                String str3 = roomKeyRequestBody.roomId;
                String str4 = roomKeyRequestBody.algorithm;
                String str5 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("m.room_key_request from ");
                sb.append(str);
                sb.append(":");
                sb.append(str2);
                sb.append(" for ");
                sb.append(str3);
                sb.append(" / ");
                sb.append(roomKeyRequestBody.sessionId);
                sb.append(" id ");
                sb.append(incomingRoomKeyRequest.mRequestId);
                Log.d(str5, sb.toString());
                if (!TextUtils.equals(this.mSession.getMyUserId(), str)) {
                    Log.e(LOG_TAG, "## processReceivedRoomKeyRequests() : Ignoring room key request from other user for now");
                    return;
                }
                final IMXDecrypting roomDecryptor = getRoomDecryptor(str3, str4);
                if (roomDecryptor == null) {
                    String str6 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## processReceivedRoomKeyRequests() : room key request for unknown ");
                    sb2.append(str4);
                    sb2.append(" in room ");
                    sb2.append(str3);
                    Log.e(str6, sb2.toString());
                } else if (!roomDecryptor.hasKeysForKeyRequest(incomingRoomKeyRequest)) {
                    String str7 = LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("## processReceivedRoomKeyRequests() : room key request for unknown session ");
                    sb3.append(roomKeyRequestBody.sessionId);
                    Log.e(str7, sb3.toString());
                    this.mCryptoStore.deleteIncomingRoomKeyRequest(incomingRoomKeyRequest);
                } else if (!TextUtils.equals(str2, getMyDevice().deviceId) || !TextUtils.equals(this.mSession.getMyUserId(), str)) {
                    incomingRoomKeyRequest.mShare = new Runnable() {
                        public void run() {
                            MXCryptoImpl.this.getEncryptingThreadHandler().post(new Runnable() {
                                public void run() {
                                    roomDecryptor.shareKeysWithDevice(incomingRoomKeyRequest);
                                    MXCryptoImpl.this.mCryptoStore.deleteIncomingRoomKeyRequest(incomingRoomKeyRequest);
                                }
                            });
                        }
                    };
                    incomingRoomKeyRequest.mIgnore = new Runnable() {
                        public void run() {
                            MXCryptoImpl.this.getEncryptingThreadHandler().post(new Runnable() {
                                public void run() {
                                    MXCryptoImpl.this.mCryptoStore.deleteIncomingRoomKeyRequest(incomingRoomKeyRequest);
                                }
                            });
                        }
                    };
                    MXDeviceInfo userDevice = this.mCryptoStore.getUserDevice(str2, str);
                    if (userDevice != null) {
                        if (userDevice.isVerified()) {
                            Log.d(LOG_TAG, "## processReceivedRoomKeyRequests() : device is already verified: sharing keys");
                            this.mCryptoStore.deleteIncomingRoomKeyRequest(incomingRoomKeyRequest);
                            incomingRoomKeyRequest.mShare.run();
                        } else if (userDevice.isBlocked()) {
                            Log.d(LOG_TAG, "## processReceivedRoomKeyRequests() : device is blocked -> ignored");
                            this.mCryptoStore.deleteIncomingRoomKeyRequest(incomingRoomKeyRequest);
                        }
                    }
                    this.mCryptoStore.storeIncomingRoomKeyRequest(incomingRoomKeyRequest);
                    onRoomKeyRequest(incomingRoomKeyRequest);
                } else {
                    Log.d(LOG_TAG, "## processReceivedRoomKeyRequests() : oneself device - ignored");
                    this.mCryptoStore.deleteIncomingRoomKeyRequest(incomingRoomKeyRequest);
                }
            }
        }
        synchronized (this.mReceivedRoomKeyRequestCancellations) {
            if (!this.mReceivedRoomKeyRequestCancellations.isEmpty()) {
                arrayList = new ArrayList<>(this.mReceivedRoomKeyRequestCancellations);
                this.mReceivedRoomKeyRequestCancellations.clear();
            }
        }
        if (arrayList != null) {
            for (IncomingRoomKeyRequestCancellation incomingRoomKeyRequestCancellation : arrayList) {
                String str8 = LOG_TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("## ## processReceivedRoomKeyRequests() : m.room_key_request cancellation for ");
                sb4.append(incomingRoomKeyRequestCancellation.mUserId);
                sb4.append(":");
                sb4.append(incomingRoomKeyRequestCancellation.mDeviceId);
                sb4.append(" id ");
                sb4.append(incomingRoomKeyRequestCancellation.mRequestId);
                Log.d(str8, sb4.toString());
                onRoomKeyRequestCancellation(incomingRoomKeyRequestCancellation);
                this.mCryptoStore.deleteIncomingRoomKeyRequest(incomingRoomKeyRequestCancellation);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onCryptoEvent(final CryptoEvent cryptoEvent) {
        final CryptoEventContent wireEventContent = cryptoEvent.getWireEventContent();
        final CryptoRoom room = this.mSession.getDataHandler().getRoom(cryptoEvent.getRoomId());
        boolean z = this.mCryptoConfig.mEnableEncryptionForInvitedMembers && room.shouldEncryptForInvitedMembers();
        AnonymousClass21 r3 = new ApiCallback<List<CryptoRoomMember>>() {
            public void onSuccess(final List<CryptoRoomMember> list) {
                MXCryptoImpl.this.getEncryptingThreadHandler().post(new Runnable() {
                    public void run() {
                        MXCryptoImpl.this.setEncryptionInRoom(cryptoEvent.getRoomId(), wireEventContent.getAlgorithm(), true, list);
                    }
                });
            }

            private void onError() {
                MXCryptoImpl.this.getEncryptingThreadHandler().post(new Runnable() {
                    public void run() {
                        MXCryptoImpl.this.setEncryptionInRoom(cryptoEvent.getRoomId(), wireEventContent.getAlgorithm(), true, room.getState().getLoadedMembersCrypto());
                    }
                });
            }

            public void onNetworkError(Exception exc) {
                Log.w(MXCryptoImpl.LOG_TAG, "[MXCrypto] onCryptoEvent: Warning: Unable to get all members from the HS. Fallback by using lazy-loaded members", exc);
                onError();
            }

            public void onMatrixError(MatrixError matrixError) {
                Log.w(MXCryptoImpl.LOG_TAG, "[MXCrypto] onCryptoEvent: Warning: Unable to get all members from the HS. Fallback by using lazy-loaded members");
                onError();
            }

            public void onUnexpectedError(Exception exc) {
                Log.w(MXCryptoImpl.LOG_TAG, "[MXCrypto] onCryptoEvent: Warning: Unable to get all members from the HS. Fallback by using lazy-loaded members", exc);
                onError();
            }
        };
        if (z) {
            room.getActiveMembersAsyncCrypto(r3);
        } else {
            room.getJoinedMembersAsyncCrypto(r3);
        }
    }

    /* access modifiers changed from: private */
    public void onRoomMembership(CryptoEvent cryptoEvent) {
        IMXEncrypting iMXEncrypting;
        synchronized (this.mRoomEncryptors) {
            iMXEncrypting = (IMXEncrypting) this.mRoomEncryptors.get(cryptoEvent.getRoomId());
        }
        if (iMXEncrypting != null) {
            final String stateKey = cryptoEvent.getStateKey();
            final CryptoRoom room = this.mSession.getDataHandler().getRoom(cryptoEvent.getRoomId());
            CryptoRoomMember member = room.getState().getMember(stateKey);
            if (member != null) {
                final String membership = member.getMembership();
                getEncryptingThreadHandler().post(new Runnable() {
                    public void run() {
                        if (TextUtils.equals(membership, "join")) {
                            MXCryptoImpl.this.getDeviceList().startTrackingDeviceList(Arrays.asList(new String[]{stateKey}));
                        } else if (TextUtils.equals(membership, "invite") && room.shouldEncryptForInvitedMembers() && MXCryptoImpl.this.mCryptoConfig.mEnableEncryptionForInvitedMembers) {
                            MXCryptoImpl.this.getDeviceList().startTrackingDeviceList(Arrays.asList(new String[]{stateKey}));
                        }
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public void uploadDeviceKeys(ApiCallback<KeysUploadResponse> apiCallback) {
        MXDeviceInfo mXDeviceInfo = this.mMyDevice;
        mXDeviceInfo.signatures = signObject(JsonUtility.getCanonicalizedJsonString(mXDeviceInfo.signalableJSONDictionary()));
        this.mCryptoRestClient.uploadKeys(this.mMyDevice.JSONDictionary(), null, this.mMyDevice.deviceId, apiCallback);
    }

    /* access modifiers changed from: private */
    public void uploadLoop(int i, final int i2, final ApiCallback<Void> apiCallback) {
        if (i2 <= i) {
            if (apiCallback != null) {
                getUIHandler().post(new Runnable() {
                    public void run() {
                        apiCallback.onSuccess(null);
                    }
                });
            }
            return;
        }
        getOlmDevice().generateOneTimeKeys(Math.min(i2 - i, 5));
        uploadOneTimeKeys(new SimpleApiCallback<KeysUploadResponse>(apiCallback) {
            public void onSuccess(final KeysUploadResponse keysUploadResponse) {
                MXCryptoImpl.this.getEncryptingThreadHandler().post(new Runnable() {
                    public void run() {
                        KeysUploadResponse keysUploadResponse = keysUploadResponse;
                        String str = MXKey.KEY_SIGNED_CURVE_25519_TYPE;
                        if (keysUploadResponse.hasOneTimeKeyCountsForAlgorithm(str)) {
                            MXCryptoImpl.this.uploadLoop(keysUploadResponse.oneTimeKeyCountsForAlgorithm(str), i2, apiCallback);
                            return;
                        }
                        Log.e(MXCryptoImpl.LOG_TAG, "## uploadLoop() : response for uploading keys does not contain one_time_key_counts.signed_curve25519");
                        MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                            public void run() {
                                apiCallback.onUnexpectedError(new Exception("response for uploading keys does not contain one_time_key_counts.signed_curve25519"));
                            }
                        });
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public void maybeUploadOneTimeKeys() {
        maybeUploadOneTimeKeys(null);
    }

    /* access modifiers changed from: private */
    public void maybeUploadOneTimeKeys(final ApiCallback<Void> apiCallback) {
        if (this.mOneTimeKeyCheckInProgress) {
            getUIHandler().post(new Runnable() {
                public void run() {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onSuccess(null);
                    }
                }
            });
        } else if (System.currentTimeMillis() - this.mLastOneTimeKeyCheck < 60000) {
            getUIHandler().post(new Runnable() {
                public void run() {
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onSuccess(null);
                    }
                }
            });
        } else {
            this.mLastOneTimeKeyCheck = System.currentTimeMillis();
            this.mOneTimeKeyCheckInProgress = true;
            double maxNumberOfOneTimeKeys = (double) getOlmDevice().getMaxNumberOfOneTimeKeys();
            Double.isNaN(maxNumberOfOneTimeKeys);
            final int floor = (int) Math.floor(maxNumberOfOneTimeKeys / 2.0d);
            Integer num = this.mOneTimeKeyCount;
            if (num != null) {
                uploadOTK(num.intValue(), floor, apiCallback);
            } else {
                this.mCryptoRestClient.uploadKeys(null, null, this.mMyDevice.deviceId, new ApiCallback<KeysUploadResponse>() {
                    private void onFailed(String str) {
                        if (str != null) {
                            String access$000 = MXCryptoImpl.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## uploadKeys() : failed ");
                            sb.append(str);
                            Log.e(access$000, sb.toString());
                        }
                        MXCryptoImpl.this.mOneTimeKeyCount = null;
                        MXCryptoImpl.this.mOneTimeKeyCheckInProgress = false;
                    }

                    public void onSuccess(final KeysUploadResponse keysUploadResponse) {
                        MXCryptoImpl.this.getEncryptingThreadHandler().post(new Runnable() {
                            public void run() {
                                if (!MXCryptoImpl.this.hasBeenReleased()) {
                                    MXCryptoImpl.this.uploadOTK(keysUploadResponse.oneTimeKeyCountsForAlgorithm(MXKey.KEY_SIGNED_CURVE_25519_TYPE), floor, apiCallback);
                                }
                            }
                        });
                    }

                    public void onNetworkError(final Exception exc) {
                        onFailed(exc.getMessage());
                        MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                            public void run() {
                                if (apiCallback != null) {
                                    apiCallback.onNetworkError(exc);
                                }
                            }
                        });
                    }

                    public void onMatrixError(final MatrixError matrixError) {
                        onFailed(matrixError.getMessage());
                        MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                            public void run() {
                                if (apiCallback != null) {
                                    apiCallback.onMatrixError(matrixError);
                                }
                            }
                        });
                    }

                    public void onUnexpectedError(final Exception exc) {
                        onFailed(exc.getMessage());
                        MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                            public void run() {
                                if (apiCallback != null) {
                                    apiCallback.onUnexpectedError(exc);
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public void uploadOTK(int i, int i2, final ApiCallback<Void> apiCallback) {
        uploadLoop(i, i2, new ApiCallback<Void>() {
            private void uploadKeysDone(String str) {
                if (str != null) {
                    String access$000 = MXCryptoImpl.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## maybeUploadOneTimeKeys() : failed ");
                    sb.append(str);
                    Log.e(access$000, sb.toString());
                }
                MXCryptoImpl.this.mOneTimeKeyCount = null;
                MXCryptoImpl.this.mOneTimeKeyCheckInProgress = false;
            }

            public void onSuccess(Void voidR) {
                Log.d(MXCryptoImpl.LOG_TAG, "## maybeUploadOneTimeKeys() : succeeded");
                uploadKeysDone(null);
                MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                    public void run() {
                        if (apiCallback != null) {
                            apiCallback.onSuccess(null);
                        }
                    }
                });
            }

            public void onNetworkError(final Exception exc) {
                uploadKeysDone(exc.getMessage());
                MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                    public void run() {
                        if (apiCallback != null) {
                            apiCallback.onNetworkError(exc);
                        }
                    }
                });
            }

            public void onMatrixError(final MatrixError matrixError) {
                uploadKeysDone(matrixError.getMessage());
                MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                    public void run() {
                        if (apiCallback != null) {
                            apiCallback.onMatrixError(matrixError);
                        }
                    }
                });
            }

            public void onUnexpectedError(final Exception exc) {
                uploadKeysDone(exc.getMessage());
                MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                    public void run() {
                        if (apiCallback != null) {
                            apiCallback.onUnexpectedError(exc);
                        }
                    }
                });
            }
        });
    }

    private void uploadOneTimeKeys(final ApiCallback<KeysUploadResponse> apiCallback) {
        final Map oneTimeKeys = this.mOlmDevice.getOneTimeKeys();
        HashMap hashMap = new HashMap();
        Map map = (Map) oneTimeKeys.get("curve25519");
        if (map != null) {
            for (String str : map.keySet()) {
                HashMap hashMap2 = new HashMap();
                hashMap2.put("key", map.get(str));
                hashMap2.put("signatures", signObject(JsonUtility.getCanonicalizedJsonString(hashMap2)));
                StringBuilder sb = new StringBuilder();
                sb.append("signed_curve25519:");
                sb.append(str);
                hashMap.put(sb.toString(), hashMap2);
            }
        }
        this.mCryptoRestClient.uploadKeys(null, hashMap, this.mMyDevice.deviceId, new SimpleApiCallback<KeysUploadResponse>(apiCallback) {
            public void onSuccess(final KeysUploadResponse keysUploadResponse) {
                MXCryptoImpl.this.getEncryptingThreadHandler().post(new Runnable() {
                    public void run() {
                        if (!MXCryptoImpl.this.hasBeenReleased()) {
                            MXCryptoImpl.this.mLastPublishedOneTimeKeys = oneTimeKeys;
                            MXCryptoImpl.this.mOlmDevice.markKeysAsPublished();
                            if (apiCallback != null) {
                                MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                                    public void run() {
                                        apiCallback.onSuccess(keysUploadResponse);
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public IMXDecrypting getRoomDecryptor(String str, String str2) {
        IMXDecrypting iMXDecrypting;
        if (TextUtils.isEmpty(str2)) {
            Log.e(LOG_TAG, "## getRoomDecryptor() : null algorithm");
            return null;
        } else if (this.mRoomDecryptors == null) {
            Log.e(LOG_TAG, "## getRoomDecryptor() : null mRoomDecryptors");
            return null;
        } else {
            if (!TextUtils.isEmpty(str)) {
                synchronized (this.mRoomDecryptors) {
                    if (!this.mRoomDecryptors.containsKey(str)) {
                        this.mRoomDecryptors.put(str, new HashMap());
                    }
                    iMXDecrypting = (IMXDecrypting) ((Map) this.mRoomDecryptors.get(str)).get(str2);
                }
                if (iMXDecrypting != null) {
                    return iMXDecrypting;
                }
            } else {
                iMXDecrypting = null;
            }
            Class decryptorClassForAlgorithm = MXCryptoAlgorithms.sharedAlgorithms().decryptorClassForAlgorithm(str2);
            if (decryptorClassForAlgorithm != null) {
                try {
                    iMXDecrypting = (IMXDecrypting) decryptorClassForAlgorithm.getConstructors()[0].newInstance(new Object[0]);
                    if (iMXDecrypting != null) {
                        iMXDecrypting.initWithMatrixSession(this.mSession, this);
                        if (!TextUtils.isEmpty(str)) {
                            synchronized (this.mRoomDecryptors) {
                                ((Map) this.mRoomDecryptors.get(str)).put(str2, iMXDecrypting);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "## getRoomDecryptor() : fail to load the class", e);
                    return null;
                }
            }
            return iMXDecrypting;
        }
    }

    public void exportRoomKeys(String str, ApiCallback<byte[]> apiCallback) {
        exportRoomKeys(str, MXMegolmExportEncryption.DEFAULT_ITERATION_COUNT, apiCallback);
    }

    public void exportRoomKeys(final String str, int i, final ApiCallback<byte[]> apiCallback) {
        final int max = Math.max(0, i);
        getDecryptingThreadHandler().post(new Runnable() {
            public void run() {
                if (MXCryptoImpl.this.mCryptoStore == null) {
                    MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                        public void run() {
                            apiCallback.onSuccess(new byte[0]);
                        }
                    });
                    return;
                }
                ArrayList arrayList = new ArrayList();
                for (MXOlmInboundGroupSession2 exportKeys : MXCryptoImpl.this.mCryptoStore.getInboundGroupSessions()) {
                    MegolmSessionData exportKeys2 = exportKeys.exportKeys();
                    if (exportKeys2 != null) {
                        arrayList.add(exportKeys2);
                    }
                }
                try {
                    final byte[] encryptMegolmKeyFile = MXMegolmExportEncryption.encryptMegolmKeyFile(JsonUtility.getGson(false).toJsonTree(arrayList).toString(), str, max);
                    MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                        public void run() {
                            apiCallback.onSuccess(encryptMegolmKeyFile);
                        }
                    });
                } catch (Exception e) {
                    apiCallback.onUnexpectedError(e);
                }
            }
        });
    }

    public void importRoomKeys(byte[] bArr, String str, ProgressListener progressListener, ApiCallback<ImportRoomKeysResult> apiCallback) {
        Handler decryptingThreadHandler = getDecryptingThreadHandler();
        final byte[] bArr2 = bArr;
        final String str2 = str;
        final ApiCallback<ImportRoomKeysResult> apiCallback2 = apiCallback;
        final ProgressListener progressListener2 = progressListener;
        AnonymousClass31 r1 = new Runnable() {
            public void run() {
                Log.d(MXCryptoImpl.LOG_TAG, "## importRoomKeys starts");
                long currentTimeMillis = System.currentTimeMillis();
                try {
                    String decryptMegolmKeyFile = MXMegolmExportEncryption.decryptMegolmKeyFile(bArr2, str2);
                    long currentTimeMillis2 = System.currentTimeMillis();
                    String access$000 = MXCryptoImpl.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## importRoomKeys : decryptMegolmKeyFile done in ");
                    sb.append(currentTimeMillis2 - currentTimeMillis);
                    String str = " ms";
                    sb.append(str);
                    Log.d(access$000, sb.toString());
                    try {
                        List list = (List) JsonUtility.getGson(false).fromJson(decryptMegolmKeyFile, new TypeToken<List<MegolmSessionData>>() {
                        }.getType());
                        long currentTimeMillis3 = System.currentTimeMillis();
                        String access$0002 = MXCryptoImpl.LOG_TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("## importRoomKeys : JSON parsing ");
                        sb2.append(currentTimeMillis3 - currentTimeMillis2);
                        sb2.append(str);
                        Log.d(access$0002, sb2.toString());
                        MXCryptoImpl.this.importMegolmSessionsData(list, true, progressListener2, apiCallback2);
                    } catch (Exception e) {
                        String access$0003 = MXCryptoImpl.LOG_TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("## importRoomKeys failed ");
                        sb3.append(e.getMessage());
                        Log.e(access$0003, sb3.toString(), e);
                        MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                            public void run() {
                                apiCallback2.onUnexpectedError(e);
                            }
                        });
                    }
                } catch (Exception e2) {
                    MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                        public void run() {
                            apiCallback2.onUnexpectedError(e2);
                        }
                    });
                }
            }
        };
        decryptingThreadHandler.post(r1);
    }

    public void importMegolmSessionsData(List<MegolmSessionData> list, boolean z, ProgressListener progressListener, ApiCallback<ImportRoomKeysResult> apiCallback) {
        Handler decryptingThreadHandler = getDecryptingThreadHandler();
        final List<MegolmSessionData> list2 = list;
        final ProgressListener progressListener2 = progressListener;
        final boolean z2 = z;
        final ApiCallback<ImportRoomKeysResult> apiCallback2 = apiCallback;
        AnonymousClass32 r1 = new Runnable() {
            public void run() {
                long currentTimeMillis = System.currentTimeMillis();
                final int size = list2.size();
                if (progressListener2 != null) {
                    MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                        public void run() {
                            progressListener2.onProgress(0, 100);
                        }
                    });
                }
                List importInboundGroupSessions = MXCryptoImpl.this.mOlmDevice.importInboundGroupSessions(list2);
                int i = 0;
                final int i2 = 0;
                int i3 = 0;
                for (MegolmSessionData megolmSessionData : list2) {
                    i++;
                    if (megolmSessionData != null && MXCryptoImpl.this.mRoomDecryptors.containsKey(megolmSessionData.roomId)) {
                        IMXDecrypting iMXDecrypting = (IMXDecrypting) ((Map) MXCryptoImpl.this.mRoomDecryptors.get(megolmSessionData.roomId)).get(megolmSessionData.algorithm);
                        if (iMXDecrypting != null) {
                            try {
                                String str = megolmSessionData.sessionId;
                                String access$000 = MXCryptoImpl.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## importRoomKeys retrieve mSenderKey ");
                                sb.append(megolmSessionData.senderKey);
                                sb.append(" sessionId ");
                                sb.append(str);
                                Log.d(access$000, sb.toString());
                                i2++;
                                RoomKeyRequestBody roomKeyRequestBody = new RoomKeyRequestBody();
                                roomKeyRequestBody.algorithm = megolmSessionData.algorithm;
                                roomKeyRequestBody.roomId = megolmSessionData.roomId;
                                roomKeyRequestBody.senderKey = megolmSessionData.senderKey;
                                roomKeyRequestBody.sessionId = megolmSessionData.sessionId;
                                MXCryptoImpl.this.cancelRoomKeyRequest(roomKeyRequestBody);
                                iMXDecrypting.onNewSession(megolmSessionData.senderKey, str);
                            } catch (Exception e) {
                                String access$0002 = MXCryptoImpl.LOG_TAG;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("## importRoomKeys() : onNewSession failed ");
                                sb2.append(e.getMessage());
                                Log.e(access$0002, sb2.toString(), e);
                            }
                        }
                    }
                    if (progressListener2 != null) {
                        final int i4 = (i * 100) / size;
                        if (i3 != i4) {
                            MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                                public void run() {
                                    progressListener2.onProgress(i4, 100);
                                }
                            });
                            i3 = i4;
                        }
                    }
                }
                if (z2) {
                    MXCryptoImpl.this.mKeysBackup.maybeBackupKeys();
                } else {
                    MXCryptoImpl.this.mCryptoStore.markBackupDoneForInboundGroupSessions(importInboundGroupSessions);
                }
                long currentTimeMillis2 = System.currentTimeMillis();
                String access$0003 = MXCryptoImpl.LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("## importMegolmSessionsData : sessions import ");
                sb3.append(currentTimeMillis2 - currentTimeMillis);
                sb3.append(" ms (");
                sb3.append(list2.size());
                sb3.append(" sessions)");
                Log.d(access$0003, sb3.toString());
                MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                    public void run() {
                        apiCallback2.onSuccess(new ImportRoomKeysResult(size, i2));
                    }
                });
            }
        };
        decryptingThreadHandler.post(r1);
    }

    public boolean warnOnUnknownDevices() {
        return this.mWarnOnUnknownDevices;
    }

    public void setWarnOnUnknownDevices(boolean z) {
        this.mWarnOnUnknownDevices = z;
    }

    public static MXUsersDevicesMap<MXDeviceInfo> getUnknownDevices(MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
        MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap2 = new MXUsersDevicesMap<>();
        for (String str : mXUsersDevicesMap.getUserIds()) {
            for (String str2 : mXUsersDevicesMap.getUserDeviceIds(str)) {
                MXDeviceInfo mXDeviceInfo = (MXDeviceInfo) mXUsersDevicesMap.getObject(str2, str);
                if (mXDeviceInfo.isUnknown()) {
                    mXUsersDevicesMap2.setObject(mXDeviceInfo, str, str2);
                }
            }
        }
        return mXUsersDevicesMap2;
    }

    public void checkUnknownDevices(List<String> list, final ApiCallback<Void> apiCallback) {
        this.mDevicesList.downloadKeys(list, true, new SimpleApiCallback<MXUsersDevicesMap<MXDeviceInfo>>(apiCallback) {
            public void onSuccess(MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
                MXUsersDevicesMap unknownDevices = MXCryptoImpl.getUnknownDevices(mXUsersDevicesMap);
                if (unknownDevices.getMap().size() == 0) {
                    apiCallback.onSuccess(null);
                } else {
                    apiCallback.onMatrixError(new MXCryptoError(MXCryptoError.UNKNOWN_DEVICES_CODE, MXCryptoError.UNABLE_TO_ENCRYPT, MXCryptoError.UNKNOWN_DEVICES_REASON, unknownDevices));
                }
            }
        });
    }

    public void setGlobalBlacklistUnverifiedDevices(final boolean z, final ApiCallback<Void> apiCallback) {
        getEncryptingThreadHandler().post(new Runnable() {
            public void run() {
                MXCryptoImpl.this.mCryptoStore.setGlobalBlacklistUnverifiedDevices(z);
                MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                    public void run() {
                        if (apiCallback != null) {
                            apiCallback.onSuccess(null);
                        }
                    }
                });
            }
        });
    }

    public boolean getGlobalBlacklistUnverifiedDevices() {
        return this.mCryptoStore.getGlobalBlacklistUnverifiedDevices();
    }

    public void getGlobalBlacklistUnverifiedDevices(final ApiCallback<Boolean> apiCallback) {
        getEncryptingThreadHandler().post(new Runnable() {
            public void run() {
                if (apiCallback != null) {
                    final boolean globalBlacklistUnverifiedDevices = MXCryptoImpl.this.getGlobalBlacklistUnverifiedDevices();
                    MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                        public void run() {
                            apiCallback.onSuccess(Boolean.valueOf(globalBlacklistUnverifiedDevices));
                        }
                    });
                }
            }
        });
    }

    public boolean isRoomBlacklistUnverifiedDevices(String str) {
        if (str != null) {
            return this.mCryptoStore.getRoomsListBlacklistUnverifiedDevices().contains(str);
        }
        return false;
    }

    public void isRoomBlacklistUnverifiedDevices(final String str, final ApiCallback<Boolean> apiCallback) {
        getEncryptingThreadHandler().post(new Runnable() {
            public void run() {
                final boolean isRoomBlacklistUnverifiedDevices = MXCryptoImpl.this.isRoomBlacklistUnverifiedDevices(str);
                MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                    public void run() {
                        if (apiCallback != null) {
                            apiCallback.onSuccess(Boolean.valueOf(isRoomBlacklistUnverifiedDevices));
                        }
                    }
                });
            }
        });
    }

    private void setRoomBlacklistUnverifiedDevices(final String str, final boolean z, final ApiCallback<Void> apiCallback) {
        if (this.mSession.getDataHandler().getRoom(str) == null) {
            getUIHandler().post(new Runnable() {
                public void run() {
                    apiCallback.onSuccess(null);
                }
            });
        } else {
            getEncryptingThreadHandler().post(new Runnable() {
                public void run() {
                    List roomsListBlacklistUnverifiedDevices = MXCryptoImpl.this.mCryptoStore.getRoomsListBlacklistUnverifiedDevices();
                    if (!z) {
                        roomsListBlacklistUnverifiedDevices.remove(str);
                    } else if (!roomsListBlacklistUnverifiedDevices.contains(str)) {
                        roomsListBlacklistUnverifiedDevices.add(str);
                    }
                    MXCryptoImpl.this.mCryptoStore.setRoomsListBlacklistUnverifiedDevices(roomsListBlacklistUnverifiedDevices);
                    MXCryptoImpl.this.getUIHandler().post(new Runnable() {
                        public void run() {
                            if (apiCallback != null) {
                                apiCallback.onSuccess(null);
                            }
                        }
                    });
                }
            });
        }
    }

    public void setRoomBlacklistUnverifiedDevices(String str, ApiCallback<Void> apiCallback) {
        setRoomBlacklistUnverifiedDevices(str, true, apiCallback);
    }

    public void setRoomUnBlacklistUnverifiedDevices(String str, ApiCallback<Void> apiCallback) {
        setRoomBlacklistUnverifiedDevices(str, false, apiCallback);
    }

    public void requestRoomKey(final RoomKeyRequestBody roomKeyRequestBody, final List<Map<String, String>> list) {
        getEncryptingThreadHandler().post(new Runnable() {
            public void run() {
                MXCryptoImpl.this.mOutgoingRoomKeyRequestManager.sendRoomKeyRequest(roomKeyRequestBody, list);
            }
        });
    }

    public void cancelRoomKeyRequest(final RoomKeyRequestBody roomKeyRequestBody) {
        getEncryptingThreadHandler().post(new Runnable() {
            public void run() {
                MXCryptoImpl.this.mOutgoingRoomKeyRequestManager.cancelRoomKeyRequest(roomKeyRequestBody);
            }
        });
    }

    public void reRequestRoomKeyForEvent(CryptoEvent cryptoEvent) {
        if (cryptoEvent.getWireContent().isJsonObject()) {
            JsonObject asJsonObject = cryptoEvent.getWireContent().getAsJsonObject();
            final String asString = asJsonObject.get(CryptoRoomEntityFields.ALGORITHM).getAsString();
            final String asString2 = asJsonObject.get("sender_key").getAsString();
            final String asString3 = asJsonObject.get(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_SESSION_ID).getAsString();
            Handler encryptingThreadHandler = getEncryptingThreadHandler();
            final CryptoEvent cryptoEvent2 = cryptoEvent;
            AnonymousClass41 r2 = new Runnable() {
                public void run() {
                    RoomKeyRequestBody roomKeyRequestBody = new RoomKeyRequestBody();
                    roomKeyRequestBody.roomId = cryptoEvent2.getRoomId();
                    roomKeyRequestBody.algorithm = asString;
                    roomKeyRequestBody.senderKey = asString2;
                    roomKeyRequestBody.sessionId = asString3;
                    MXCryptoImpl.this.mOutgoingRoomKeyRequestManager.resendRoomKeyRequest(roomKeyRequestBody);
                }
            };
            encryptingThreadHandler.post(r2);
        }
    }

    public CryptoRestClient getCryptoRestClient() {
        return this.mCryptoRestClient;
    }

    public void addRoomKeysRequestListener(RoomKeysRequestListener roomKeysRequestListener) {
        synchronized (this.mRoomKeysRequestListeners) {
            this.mRoomKeysRequestListeners.add(roomKeysRequestListener);
        }
    }

    public void removeRoomKeysRequestListener(RoomKeysRequestListener roomKeysRequestListener) {
        synchronized (this.mRoomKeysRequestListeners) {
            this.mRoomKeysRequestListeners.remove(roomKeysRequestListener);
        }
    }

    private void onRoomKeyRequest(IncomingRoomKeyRequest incomingRoomKeyRequest) {
        synchronized (this.mRoomKeysRequestListeners) {
            for (RoomKeysRequestListener onRoomKeyRequest : this.mRoomKeysRequestListeners) {
                try {
                    onRoomKeyRequest.onRoomKeyRequest(incomingRoomKeyRequest);
                } catch (Exception e) {
                    String str = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## onRoomKeyRequest() failed ");
                    sb.append(e.getMessage());
                    Log.e(str, sb.toString(), e);
                }
            }
        }
    }

    private void onRoomKeyRequestCancellation(IncomingRoomKeyRequestCancellation incomingRoomKeyRequestCancellation) {
        synchronized (this.mRoomKeysRequestListeners) {
            for (RoomKeysRequestListener onRoomKeyRequestCancellation : this.mRoomKeysRequestListeners) {
                try {
                    onRoomKeyRequestCancellation.onRoomKeyRequestCancellation(incomingRoomKeyRequestCancellation);
                } catch (Exception e) {
                    String str = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## onRoomKeyRequestCancellation() failed ");
                    sb.append(e.getMessage());
                    Log.e(str, sb.toString(), e);
                }
            }
        }
    }

    public String toString() {
        if (this.mMyDevice == null) {
            return super.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.mMyDevice.userId);
        sb.append(" (");
        sb.append(this.mMyDevice.deviceId);
        sb.append(")");
        return sb.toString();
    }
}
