package org.matrix.androidsdk.crypto;

import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXPatterns;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.cryptostore.IMXCryptoStore;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.crypto.interfaces.CryptoSession;
import org.matrix.androidsdk.crypto.internal.MXCryptoImpl;
import org.matrix.androidsdk.crypto.model.crypto.KeysQueryResponse;

public class MXDeviceList {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = MXDeviceList.class.getSimpleName();
    public static final int TRACKING_STATUS_DOWNLOAD_IN_PROGRESS = 2;
    public static final int TRACKING_STATUS_NOT_TRACKED = -1;
    public static final int TRACKING_STATUS_PENDING_DOWNLOAD = 1;
    public static final int TRACKING_STATUS_UNREACHABLE_SERVER = 4;
    public static final int TRACKING_STATUS_UP_TO_DATE = 3;
    private final IMXCryptoStore mCryptoStore;
    private final List<DownloadKeysPromise> mDownloadKeysQueues = new ArrayList();
    private boolean mIsDownloadingKeys;
    private final Set<String> mNotReadyToRetryHS = new HashSet();
    /* access modifiers changed from: private */
    public final Map<String, String> mPendingDownloadKeysRequestToken = new HashMap();
    private final Set<String> mUserKeyDownloadsInProgress = new HashSet();
    /* access modifiers changed from: private */
    public final MXCryptoImpl mxCrypto;
    private final CryptoSession mxSession;

    class DownloadKeysPromise {
        final ApiCallback<MXUsersDevicesMap<MXDeviceInfo>> mCallback;
        final List<String> mPendingUserIdsList;
        final List<String> mUserIdsList;

        DownloadKeysPromise(List<String> list, ApiCallback<MXUsersDevicesMap<MXDeviceInfo>> apiCallback) {
            this.mPendingUserIdsList = new ArrayList(list);
            this.mUserIdsList = new ArrayList(list);
            this.mCallback = apiCallback;
        }
    }

    public MXDeviceList(CryptoSession cryptoSession, MXCryptoImpl mXCryptoImpl) {
        boolean z = false;
        this.mIsDownloadingKeys = false;
        this.mxSession = cryptoSession;
        this.mxCrypto = mXCryptoImpl;
        this.mCryptoStore = mXCryptoImpl.getCryptoStore();
        Map deviceTrackingStatuses = this.mCryptoStore.getDeviceTrackingStatuses();
        for (String str : deviceTrackingStatuses.keySet()) {
            int intValue = ((Integer) deviceTrackingStatuses.get(str)).intValue();
            if (2 == intValue || 4 == intValue) {
                deviceTrackingStatuses.put(str, Integer.valueOf(1));
                z = true;
            }
        }
        if (z) {
            this.mCryptoStore.saveDeviceTrackingStatuses(deviceTrackingStatuses);
        }
    }

    private boolean canRetryKeysDownload(String str) {
        boolean z = false;
        if (!TextUtils.isEmpty(str) && str.contains(":")) {
            try {
                synchronized (this.mNotReadyToRetryHS) {
                    if (!this.mNotReadyToRetryHS.contains(str.substring(str.lastIndexOf(":") + 1))) {
                        z = true;
                    }
                }
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## canRetryKeysDownload() failed : ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
        return z;
    }

    private List<String> addDownloadKeysPromise(List<String> list, ApiCallback<MXUsersDevicesMap<MXDeviceInfo>> apiCallback) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (String str : list) {
            if (MXPatterns.isUserId(str)) {
                arrayList.add(str);
            } else {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## userId ");
                sb.append(str);
                sb.append("is not a valid user id");
                Log.e(str2, sb.toString());
                arrayList2.add(str);
            }
        }
        synchronized (this.mUserKeyDownloadsInProgress) {
            arrayList.removeAll(this.mUserKeyDownloadsInProgress);
            this.mUserKeyDownloadsInProgress.addAll(list);
            this.mUserKeyDownloadsInProgress.removeAll(arrayList2);
            list.removeAll(arrayList2);
        }
        this.mDownloadKeysQueues.add(new DownloadKeysPromise(list, apiCallback));
        return arrayList;
    }

    private void clearUnavailableServersList() {
        synchronized (this.mNotReadyToRetryHS) {
            this.mNotReadyToRetryHS.clear();
        }
    }

    public void startTrackingDeviceList(List<String> list) {
        if (list != null) {
            boolean z = false;
            Map deviceTrackingStatuses = this.mCryptoStore.getDeviceTrackingStatuses();
            for (String str : list) {
                if (!deviceTrackingStatuses.containsKey(str) || -1 == ((Integer) deviceTrackingStatuses.get(str)).intValue()) {
                    String str2 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## startTrackingDeviceList() : Now tracking device list for ");
                    sb.append(str);
                    Log.d(str2, sb.toString());
                    deviceTrackingStatuses.put(str, Integer.valueOf(1));
                    z = true;
                }
            }
            if (z) {
                this.mCryptoStore.saveDeviceTrackingStatuses(deviceTrackingStatuses);
            }
        }
    }

    public void handleDeviceListsChanges(List<String> list, List<String> list2) {
        Map deviceTrackingStatuses = this.mCryptoStore.getDeviceTrackingStatuses();
        boolean z = false;
        if (!(list == null || list.size() == 0)) {
            clearUnavailableServersList();
            for (String str : list) {
                if (deviceTrackingStatuses.containsKey(str)) {
                    String str2 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## invalidateUserDeviceList() : Marking device list outdated for ");
                    sb.append(str);
                    Log.d(str2, sb.toString());
                    deviceTrackingStatuses.put(str, Integer.valueOf(1));
                    z = true;
                }
            }
        }
        if (!(list2 == null || list2.size() == 0)) {
            clearUnavailableServersList();
            for (String str3 : list2) {
                if (deviceTrackingStatuses.containsKey(str3)) {
                    String str4 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## invalidateUserDeviceList() : No longer tracking device list for ");
                    sb2.append(str3);
                    Log.d(str4, sb2.toString());
                    deviceTrackingStatuses.put(str3, Integer.valueOf(-1));
                    z = true;
                }
            }
        }
        if (z) {
            this.mCryptoStore.saveDeviceTrackingStatuses(deviceTrackingStatuses);
        }
    }

    public void invalidateAllDeviceLists() {
        handleDeviceListsChanges(new ArrayList(this.mCryptoStore.getDeviceTrackingStatuses().keySet()), null);
    }

    /* access modifiers changed from: private */
    public void onKeysDownloadFailed(List<String> list) {
        if (list != null) {
            synchronized (this.mUserKeyDownloadsInProgress) {
                Map deviceTrackingStatuses = this.mCryptoStore.getDeviceTrackingStatuses();
                for (String str : list) {
                    this.mUserKeyDownloadsInProgress.remove(str);
                    deviceTrackingStatuses.put(str, Integer.valueOf(1));
                }
                this.mCryptoStore.saveDeviceTrackingStatuses(deviceTrackingStatuses);
            }
        }
        this.mIsDownloadingKeys = false;
    }

    /* access modifiers changed from: private */
    public void onKeysDownloadSucceed(List<String> list, Map<String, Map<String, Object>> map) {
        if (map != null) {
            for (String str : map.keySet()) {
                Map map2 = (Map) map.get(str);
                if (map2.containsKey(NotificationCompat.CATEGORY_STATUS)) {
                    Object obj = map2.get(NotificationCompat.CATEGORY_STATUS);
                    int i = obj instanceof Double ? ((Double) obj).intValue() : obj instanceof Integer ? ((Integer) obj).intValue() : 0;
                    if (i == 503) {
                        synchronized (this.mNotReadyToRetryHS) {
                            this.mNotReadyToRetryHS.add(str);
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        Map deviceTrackingStatuses = this.mCryptoStore.getDeviceTrackingStatuses();
        if (list != null) {
            if (this.mDownloadKeysQueues.size() > 0) {
                ArrayList arrayList = new ArrayList();
                for (DownloadKeysPromise downloadKeysPromise : this.mDownloadKeysQueues) {
                    downloadKeysPromise.mPendingUserIdsList.removeAll(list);
                    if (downloadKeysPromise.mPendingUserIdsList.size() == 0) {
                        final MXUsersDevicesMap mXUsersDevicesMap = new MXUsersDevicesMap();
                        for (String str2 : downloadKeysPromise.mUserIdsList) {
                            Map userDevices = this.mCryptoStore.getUserDevices(str2);
                            if (userDevices != null) {
                                if (deviceTrackingStatuses.containsKey(str2) && 2 == ((Integer) deviceTrackingStatuses.get(str2)).intValue()) {
                                    deviceTrackingStatuses.put(str2, Integer.valueOf(3));
                                    String str3 = LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Device list for ");
                                    sb.append(str2);
                                    sb.append(" now up to date");
                                    Log.d(str3, sb.toString());
                                }
                                mXUsersDevicesMap.setObjects(userDevices, str2);
                            } else if (canRetryKeysDownload(str2)) {
                                deviceTrackingStatuses.put(str2, Integer.valueOf(1));
                                String str4 = LOG_TAG;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("failed to retry the devices of ");
                                sb2.append(str2);
                                sb2.append(" : retry later");
                                Log.e(str4, sb2.toString());
                            } else if (deviceTrackingStatuses.containsKey(str2) && 2 == ((Integer) deviceTrackingStatuses.get(str2)).intValue()) {
                                deviceTrackingStatuses.put(str2, Integer.valueOf(4));
                                String str5 = LOG_TAG;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("failed to retry the devices of ");
                                sb3.append(str2);
                                sb3.append(" : the HS is not available");
                                Log.e(str5, sb3.toString());
                            }
                        }
                        if (!this.mxCrypto.hasBeenReleased()) {
                            final ApiCallback<MXUsersDevicesMap<MXDeviceInfo>> apiCallback = downloadKeysPromise.mCallback;
                            if (apiCallback != null) {
                                this.mxCrypto.getUIHandler().post(new Runnable() {
                                    public void run() {
                                        apiCallback.onSuccess(mXUsersDevicesMap);
                                    }
                                });
                            }
                        }
                        arrayList.add(downloadKeysPromise);
                    }
                }
                this.mDownloadKeysQueues.removeAll(arrayList);
            }
            for (String remove : list) {
                this.mUserKeyDownloadsInProgress.remove(remove);
            }
            this.mCryptoStore.saveDeviceTrackingStatuses(deviceTrackingStatuses);
        }
        this.mIsDownloadingKeys = false;
    }

    public void downloadKeys(List<String> list, boolean z, final ApiCallback<MXUsersDevicesMap<MXDeviceInfo>> apiCallback) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## downloadKeys() : forceDownload ");
        sb.append(z);
        sb.append(" : ");
        sb.append(list);
        Log.d(str, sb.toString());
        final MXUsersDevicesMap mXUsersDevicesMap = new MXUsersDevicesMap();
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            if (z) {
                arrayList.addAll(list);
            } else {
                for (String str2 : list) {
                    Integer valueOf = Integer.valueOf(this.mCryptoStore.getDeviceTrackingStatus(str2, -1));
                    if (this.mUserKeyDownloadsInProgress.contains(str2) || !(3 == valueOf.intValue() || 4 == valueOf.intValue())) {
                        arrayList.add(str2);
                    } else {
                        Map userDevices = this.mCryptoStore.getUserDevices(str2);
                        if (userDevices != null) {
                            mXUsersDevicesMap.setObjects(userDevices, str2);
                        } else {
                            arrayList.add(str2);
                        }
                    }
                }
            }
        }
        if (arrayList.size() == 0) {
            Log.d(LOG_TAG, "## downloadKeys() : no new user device");
            if (apiCallback != null) {
                this.mxCrypto.getUIHandler().post(new Runnable() {
                    public void run() {
                        apiCallback.onSuccess(mXUsersDevicesMap);
                    }
                });
                return;
            }
            return;
        }
        Log.d(LOG_TAG, "## downloadKeys() : starts");
        final long currentTimeMillis = System.currentTimeMillis();
        final ApiCallback<MXUsersDevicesMap<MXDeviceInfo>> apiCallback2 = apiCallback;
        AnonymousClass3 r2 = new ApiCallback<MXUsersDevicesMap<MXDeviceInfo>>() {
            public void onSuccess(MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
                String access$000 = MXDeviceList.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## downloadKeys() : doKeyDownloadForUsers succeeds after ");
                sb.append(System.currentTimeMillis() - currentTimeMillis);
                sb.append(" ms");
                Log.d(access$000, sb.toString());
                mXUsersDevicesMap.addEntriesFromMap(mXUsersDevicesMap);
                ApiCallback apiCallback = apiCallback2;
                if (apiCallback != null) {
                    apiCallback.onSuccess(mXUsersDevicesMap);
                }
            }

            public void onNetworkError(Exception exc) {
                String access$000 = MXDeviceList.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## downloadKeys() : doKeyDownloadForUsers onNetworkError ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                ApiCallback apiCallback = apiCallback2;
                if (apiCallback != null) {
                    apiCallback.onNetworkError(exc);
                }
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$000 = MXDeviceList.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## downloadKeys() : doKeyDownloadForUsers onMatrixError ");
                sb.append(matrixError.getMessage());
                Log.e(access$000, sb.toString());
                ApiCallback apiCallback = apiCallback2;
                if (apiCallback != null) {
                    apiCallback.onMatrixError(matrixError);
                }
            }

            public void onUnexpectedError(Exception exc) {
                String access$000 = MXDeviceList.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## downloadKeys() : doKeyDownloadForUsers onUnexpectedError ");
                sb.append(exc.getMessage());
                Log.e(access$000, sb.toString(), exc);
                ApiCallback apiCallback = apiCallback2;
                if (apiCallback != null) {
                    apiCallback.onUnexpectedError(exc);
                }
            }
        };
        doKeyDownloadForUsers(arrayList, r2);
    }

    private void doKeyDownloadForUsers(List<String> list, final ApiCallback<MXUsersDevicesMap<MXDeviceInfo>> apiCallback) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## doKeyDownloadForUsers() : doKeyDownloadForUsers ");
        sb.append(list);
        Log.d(str, sb.toString());
        final List<String> addDownloadKeysPromise = addDownloadKeysPromise(list, apiCallback);
        if (!(addDownloadKeysPromise.size() == 0 || this.mxSession.getDataHandler() == null || this.mxSession.getDataHandler().getStore() == null)) {
            this.mIsDownloadingKeys = true;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(addDownloadKeysPromise.hashCode());
            sb2.append(" ");
            sb2.append(System.currentTimeMillis());
            final String sb3 = sb2.toString();
            for (String put : addDownloadKeysPromise) {
                this.mPendingDownloadKeysRequestToken.put(put, sb3);
            }
            this.mxCrypto.getCryptoRestClient().downloadKeysForUsers(addDownloadKeysPromise, this.mxSession.getDataHandler().getStore().getEventStreamToken(), new ApiCallback<KeysQueryResponse>() {
                public void onSuccess(final KeysQueryResponse keysQueryResponse) {
                    MXDeviceList.this.mxCrypto.getEncryptingThreadHandler().post(new Runnable() {
                        public void run() {
                            String access$000 = MXDeviceList.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            String str = "## doKeyDownloadForUsers() : Got keys for ";
                            sb.append(str);
                            sb.append(addDownloadKeysPromise.size());
                            sb.append(" users");
                            Log.d(access$000, sb.toString());
                            MXDeviceInfo myDevice = MXDeviceList.this.mxCrypto.getMyDevice();
                            IMXCryptoStore cryptoStore = MXDeviceList.this.mxCrypto.getCryptoStore();
                            for (String str2 : new ArrayList(addDownloadKeysPromise)) {
                                if (!TextUtils.equals((CharSequence) MXDeviceList.this.mPendingDownloadKeysRequestToken.get(str2), sb3)) {
                                    String access$0002 = MXDeviceList.LOG_TAG;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("## doKeyDownloadForUsers() : Another update in the queue for ");
                                    sb2.append(str2);
                                    sb2.append(" not marking up-to-date");
                                    Log.e(access$0002, sb2.toString());
                                    addDownloadKeysPromise.remove(str2);
                                } else {
                                    Map map = (Map) keysQueryResponse.deviceKeys.get(str2);
                                    String access$0003 = MXDeviceList.LOG_TAG;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append(str);
                                    sb3.append(str2);
                                    sb3.append(" : ");
                                    sb3.append(map);
                                    Log.d(access$0003, sb3.toString());
                                    if (map != null) {
                                        HashMap hashMap = new HashMap(map);
                                        for (String str3 : new ArrayList(hashMap.keySet())) {
                                            if (cryptoStore == null) {
                                                break;
                                            }
                                            MXDeviceInfo userDevice = cryptoStore.getUserDevice(str3, str2);
                                            MXDeviceInfo mXDeviceInfo = (MXDeviceInfo) hashMap.get(str3);
                                            if (TextUtils.equals(mXDeviceInfo.deviceId, myDevice.deviceId) && TextUtils.equals(str2, myDevice.userId)) {
                                                mXDeviceInfo.mVerified = 1;
                                            }
                                            if (!MXDeviceList.this.validateDeviceKeys(mXDeviceInfo, str2, str3, userDevice)) {
                                                hashMap.remove(str3);
                                                if (userDevice != null) {
                                                    hashMap.put(str3, userDevice);
                                                }
                                            } else if (userDevice != null) {
                                                ((MXDeviceInfo) hashMap.get(str3)).mVerified = userDevice.mVerified;
                                            }
                                        }
                                        cryptoStore.storeUserDevices(str2, hashMap);
                                    }
                                    MXDeviceList.this.mPendingDownloadKeysRequestToken.remove(str2);
                                }
                            }
                            MXDeviceList.this.onKeysDownloadSucceed(addDownloadKeysPromise, keysQueryResponse.failures);
                        }
                    });
                }

                private void onFailed() {
                    MXDeviceList.this.mxCrypto.getEncryptingThreadHandler().post(new Runnable() {
                        public void run() {
                            for (String str : new ArrayList(addDownloadKeysPromise)) {
                                if (!TextUtils.equals((CharSequence) MXDeviceList.this.mPendingDownloadKeysRequestToken.get(str), sb3)) {
                                    String access$000 = MXDeviceList.LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("## doKeyDownloadForUsers() : Another update in the queue for ");
                                    sb.append(str);
                                    sb.append(" not marking up-to-date");
                                    Log.e(access$000, sb.toString());
                                    addDownloadKeysPromise.remove(str);
                                } else {
                                    MXDeviceList.this.mPendingDownloadKeysRequestToken.remove(str);
                                }
                            }
                            MXDeviceList.this.onKeysDownloadFailed(addDownloadKeysPromise);
                        }
                    });
                }

                public void onNetworkError(Exception exc) {
                    String access$000 = MXDeviceList.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("##doKeyDownloadForUsers() : onNetworkError ");
                    sb.append(exc.getMessage());
                    Log.e(access$000, sb.toString(), exc);
                    onFailed();
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onNetworkError(exc);
                    }
                }

                public void onMatrixError(MatrixError matrixError) {
                    String access$000 = MXDeviceList.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("##doKeyDownloadForUsers() : onMatrixError ");
                    sb.append(matrixError.getMessage());
                    Log.e(access$000, sb.toString());
                    onFailed();
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onMatrixError(matrixError);
                    }
                }

                public void onUnexpectedError(Exception exc) {
                    String access$000 = MXDeviceList.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("##doKeyDownloadForUsers() : onUnexpectedError ");
                    sb.append(exc.getMessage());
                    Log.e(access$000, sb.toString(), exc);
                    onFailed();
                    ApiCallback apiCallback = apiCallback;
                    if (apiCallback != null) {
                        apiCallback.onUnexpectedError(exc);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public boolean validateDeviceKeys(MXDeviceInfo mXDeviceInfo, String str, String str2, MXDeviceInfo mXDeviceInfo2) {
        boolean z;
        String str3 = ":";
        if (mXDeviceInfo == null) {
            String str4 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## validateDeviceKeys() : deviceKeys is null from ");
            sb.append(str);
            sb.append(str3);
            sb.append(str2);
            Log.e(str4, sb.toString());
            return false;
        } else if (mXDeviceInfo.keys == null) {
            String str5 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## validateDeviceKeys() : deviceKeys.keys is null from ");
            sb2.append(str);
            sb2.append(str3);
            sb2.append(str2);
            Log.e(str5, sb2.toString());
            return false;
        } else if (mXDeviceInfo.signatures == null) {
            String str6 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("## validateDeviceKeys() : deviceKeys.signatures is null from ");
            sb3.append(str);
            sb3.append(str3);
            sb3.append(str2);
            Log.e(str6, sb3.toString());
            return false;
        } else {
            String str7 = " from ";
            if (!TextUtils.equals(mXDeviceInfo.userId, str)) {
                String str8 = LOG_TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("## validateDeviceKeys() : Mismatched user_id ");
                sb4.append(mXDeviceInfo.userId);
                sb4.append(str7);
                sb4.append(str);
                sb4.append(str3);
                sb4.append(str2);
                Log.e(str8, sb4.toString());
                return false;
            } else if (!TextUtils.equals(mXDeviceInfo.deviceId, str2)) {
                String str9 = LOG_TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("## validateDeviceKeys() : Mismatched device_id ");
                sb5.append(mXDeviceInfo.deviceId);
                sb5.append(str7);
                sb5.append(str);
                sb5.append(str3);
                sb5.append(str2);
                Log.e(str9, sb5.toString());
                return false;
            } else {
                StringBuilder sb6 = new StringBuilder();
                sb6.append("ed25519:");
                sb6.append(mXDeviceInfo.deviceId);
                String sb7 = sb6.toString();
                String str10 = (String) mXDeviceInfo.keys.get(sb7);
                String str11 = "## validateDeviceKeys() : Device ";
                if (str10 == null) {
                    String str12 = LOG_TAG;
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(str11);
                    sb8.append(str);
                    sb8.append(str3);
                    sb8.append(mXDeviceInfo.deviceId);
                    sb8.append(" has no ed25519 key");
                    Log.e(str12, sb8.toString());
                    return false;
                }
                Map map = (Map) mXDeviceInfo.signatures.get(str);
                if (map == null) {
                    String str13 = LOG_TAG;
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(str11);
                    sb9.append(str);
                    sb9.append(str3);
                    sb9.append(mXDeviceInfo.deviceId);
                    sb9.append(" has no map for ");
                    sb9.append(str);
                    Log.e(str13, sb9.toString());
                    return false;
                }
                String str14 = (String) map.get(sb7);
                if (str14 == null) {
                    String str15 = LOG_TAG;
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append(str11);
                    sb10.append(str);
                    sb10.append(str3);
                    sb10.append(mXDeviceInfo.deviceId);
                    sb10.append(" is not signed");
                    Log.e(str15, sb10.toString());
                    return false;
                }
                String str16 = null;
                try {
                    this.mxCrypto.getOlmDevice().verifySignature(str10, mXDeviceInfo.signalableJSONDictionary(), str14);
                    z = true;
                } catch (Exception e) {
                    str16 = e.getMessage();
                    z = false;
                }
                if (!z) {
                    String str17 = LOG_TAG;
                    StringBuilder sb11 = new StringBuilder();
                    sb11.append("## validateDeviceKeys() : Unable to verify signature on device ");
                    sb11.append(str);
                    sb11.append(str3);
                    sb11.append(mXDeviceInfo.deviceId);
                    sb11.append(" with error ");
                    sb11.append(str16);
                    Log.e(str17, sb11.toString());
                    return false;
                } else if (mXDeviceInfo2 == null || TextUtils.equals(mXDeviceInfo2.fingerprint(), str10)) {
                    return true;
                } else {
                    String str18 = LOG_TAG;
                    StringBuilder sb12 = new StringBuilder();
                    sb12.append("## validateDeviceKeys() : WARNING:Ed25519 key for device ");
                    sb12.append(str);
                    sb12.append(str3);
                    sb12.append(mXDeviceInfo.deviceId);
                    sb12.append(" has changed : ");
                    sb12.append(mXDeviceInfo2.fingerprint());
                    String str19 = " -> ";
                    sb12.append(str19);
                    sb12.append(str10);
                    Log.e(str18, sb12.toString());
                    String str20 = LOG_TAG;
                    StringBuilder sb13 = new StringBuilder();
                    String str21 = "## validateDeviceKeys() : ";
                    sb13.append(str21);
                    sb13.append(mXDeviceInfo2);
                    sb13.append(str19);
                    sb13.append(mXDeviceInfo);
                    Log.e(str20, sb13.toString());
                    String str22 = LOG_TAG;
                    StringBuilder sb14 = new StringBuilder();
                    sb14.append(str21);
                    sb14.append(mXDeviceInfo2.keys);
                    sb14.append(str19);
                    sb14.append(mXDeviceInfo.keys);
                    Log.e(str22, sb14.toString());
                    return false;
                }
            }
        }
    }

    public void refreshOutdatedDeviceLists() {
        final ArrayList<String> arrayList = new ArrayList<>();
        Map deviceTrackingStatuses = this.mCryptoStore.getDeviceTrackingStatuses();
        for (String str : deviceTrackingStatuses.keySet()) {
            if (1 == ((Integer) deviceTrackingStatuses.get(str)).intValue()) {
                arrayList.add(str);
            }
        }
        if (arrayList.size() != 0 && !this.mIsDownloadingKeys) {
            for (String str2 : arrayList) {
                Integer num = (Integer) deviceTrackingStatuses.get(str2);
                if (num != null && 1 == num.intValue()) {
                    deviceTrackingStatuses.put(str2, Integer.valueOf(2));
                }
            }
            this.mCryptoStore.saveDeviceTrackingStatuses(deviceTrackingStatuses);
            doKeyDownloadForUsers(arrayList, new ApiCallback<MXUsersDevicesMap<MXDeviceInfo>>() {
                public void onSuccess(MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
                    MXDeviceList.this.mxCrypto.getEncryptingThreadHandler().post(new Runnable() {
                        public void run() {
                            Log.d(MXDeviceList.LOG_TAG, "## refreshOutdatedDeviceLists() : done");
                        }
                    });
                }

                private void onError(String str) {
                    String access$000 = MXDeviceList.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## refreshOutdatedDeviceLists() : ERROR updating device keys for users ");
                    sb.append(arrayList);
                    sb.append(" : ");
                    sb.append(str);
                    Log.e(access$000, sb.toString());
                }

                public void onNetworkError(Exception exc) {
                    onError(exc.getMessage());
                }

                public void onMatrixError(MatrixError matrixError) {
                    onError(matrixError.getMessage());
                }

                public void onUnexpectedError(Exception exc) {
                    onError(exc.getMessage());
                }
            });
        }
    }
}
