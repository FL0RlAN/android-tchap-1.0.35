package org.matrix.androidsdk.crypto.cryptostore;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import im.vector.activity.VectorUniversalLinkActivity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import kotlin.UByte;
import org.matrix.androidsdk.core.CompatUtil;
import org.matrix.androidsdk.core.FileContentUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.IncomingRoomKeyRequest;
import org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest;
import org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest.RequestState;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;
import org.matrix.androidsdk.crypto.cryptostore.db.model.KeysBackupDataEntity;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession;
import org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2;
import org.matrix.androidsdk.crypto.data.MXOlmSession;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.crypto.model.crypto.RoomKeyRequestBody;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.matrix.olm.OlmAccount;
import org.matrix.olm.OlmSession;

@Deprecated
public class MXFileCryptoStore implements IMXCryptoStore {
    private static final String LOG_TAG = MXFileCryptoStore.class.getSimpleName();
    private static final String MXFILE_CRYPTO_STORE_ACCOUNT_FILE = "account";
    private static final String MXFILE_CRYPTO_STORE_ACCOUNT_FILE_TMP = "account.tmp";
    private static final String MXFILE_CRYPTO_STORE_ALGORITHMS_FILE = "roomsAlgorithms";
    private static final String MXFILE_CRYPTO_STORE_ALGORITHMS_FILE_TMP = "roomsAlgorithms.tmp";
    private static final String MXFILE_CRYPTO_STORE_DEVICES_FILE = "devices";
    private static final String MXFILE_CRYPTO_STORE_DEVICES_FILE_TMP = "devices.tmp";
    private static final String MXFILE_CRYPTO_STORE_DEVICES_FOLDER = "devicesFolder";
    private static final String MXFILE_CRYPTO_STORE_FOLDER = "MXFileCryptoStore";
    private static final String MXFILE_CRYPTO_STORE_INBOUND_GROUP_SESSIONS_FILE = "inboundGroupSessions";
    private static final String MXFILE_CRYPTO_STORE_INBOUND_GROUP_SESSIONS_FILE_TMP = "inboundGroupSessions.tmp";
    private static final String MXFILE_CRYPTO_STORE_INBOUND_GROUP_SESSIONS_FOLDER = "inboundGroupSessionsFolder";
    private static final String MXFILE_CRYPTO_STORE_INCOMING_ROOM_KEY_REQUESTS_FILE = "incomingRoomKeyRequests";
    private static final String MXFILE_CRYPTO_STORE_INCOMING_ROOM_KEY_REQUESTS_FILE_TMP = "incomingRoomKeyRequests.tmp";
    private static final String MXFILE_CRYPTO_STORE_METADATA_FILE = "MXFileCryptoStore";
    private static final String MXFILE_CRYPTO_STORE_METADATA_FILE_TMP = "MXFileCryptoStore.tmp";
    private static final String MXFILE_CRYPTO_STORE_OLM_SESSIONS_FILE = "sessions";
    private static final String MXFILE_CRYPTO_STORE_OLM_SESSIONS_FILE_TMP = "sessions.tmp";
    private static final String MXFILE_CRYPTO_STORE_OLM_SESSIONS_FOLDER = "olmSessionsFolder";
    private static final String MXFILE_CRYPTO_STORE_OUTGOING_ROOM_KEY_REQUEST_FILE = "outgoingRoomKeyRequests";
    private static final String MXFILE_CRYPTO_STORE_OUTGOING_ROOM_KEY_REQUEST_FILE_TMP = "outgoingRoomKeyRequests.tmp";
    private static final String MXFILE_CRYPTO_STORE_TRACKING_STATUSES_FILE = "trackingStatuses";
    private static final String MXFILE_CRYPTO_STORE_TRACKING_STATUSES_FILE_TMP = "trackingStatuses.tmp";
    private static final int MXFILE_CRYPTO_VERSION = 1;
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static final Object mOlmSessionsLock = new Object();
    private File mAccountFile;
    private File mAccountFileTmp;
    private File mAlgorithmsFile;
    private File mAlgorithmsFileTmp;
    private Context mContext;
    private Credentials mCredentials;
    private File mDevicesFile;
    private File mDevicesFileTmp;
    private File mDevicesFolder;
    private final boolean mEnableFileEncryption;
    private Map<String, Map<String, MXOlmInboundGroupSession2>> mInboundGroupSessions;
    private File mInboundGroupSessionsFile;
    private File mInboundGroupSessionsFileTmp;
    private File mInboundGroupSessionsFolder;
    private final Object mInboundGroupSessionsLock = new Object();
    private File mIncomingRoomKeyRequestsFile;
    private File mIncomingRoomKeyRequestsFileTmp;
    private boolean mIsCorrupted = false;
    private boolean mIsReady = false;
    private MXFileCryptoStoreMetaData2 mMetaData;
    private File mMetaDataFile;
    private File mMetaDataFileTmp;
    private OlmAccount mOlmAccount;
    private Map<String, Map<String, OlmSession>> mOlmSessions;
    private File mOlmSessionsFile;
    private File mOlmSessionsFileTmp;
    private File mOlmSessionsFolder;
    private final Map<Map<String, String>, OutgoingRoomKeyRequest> mOutgoingRoomKeyRequests = new HashMap();
    private File mOutgoingRoomKeyRequestsFile;
    private File mOutgoingRoomKeyRequestsFileTmp;
    private Map<String, Map<String, List<IncomingRoomKeyRequest>>> mPendingIncomingRoomKeyRequests;
    private Map<String, String> mRoomsAlgorithms;
    private File mStoreFile;
    private Map<String, Integer> mTrackingStatuses;
    private File mTrackingStatusesFile;
    private File mTrackingStatusesFileTmp;
    private MXUsersDevicesMap<MXDeviceInfo> mUsersDevicesInfoMap;
    private final Object mUsersDevicesInfoMapLock = new Object();

    public MXDeviceInfo deviceWithIdentityKey(String str) {
        return null;
    }

    public MXOlmSession getDeviceSession(String str, String str2) {
        return null;
    }

    public String getKeyBackupVersion() {
        return null;
    }

    public KeysBackupDataEntity getKeysBackupData() {
        return null;
    }

    public String getLastUsedSessionId(String str) {
        return null;
    }

    public OutgoingRoomKeyRequest getOutgoingRoomKeyRequest(RoomKeyRequestBody roomKeyRequestBody) {
        return null;
    }

    public int inboundGroupSessionsCount(boolean z) {
        return 0;
    }

    public void markBackupDoneForInboundGroupSessions(List<MXOlmInboundGroupSession2> list) {
    }

    public void resetBackupMarkers() {
    }

    public void setKeyBackupVersion(String str) {
    }

    public void setKeysBackupData(KeysBackupDataEntity keysBackupDataEntity) {
    }

    public void storeInboundGroupSessions(List<MXOlmInboundGroupSession2> list) {
    }

    public MXFileCryptoStore(boolean z) {
        this.mEnableFileEncryption = z;
    }

    public void initWithCredentials(Context context, Credentials credentials) {
        this.mCredentials = credentials;
        String str = "MXFileCryptoStore";
        this.mStoreFile = new File(new File(context.getApplicationContext().getFilesDir(), str), this.mCredentials.getUserId());
        this.mMetaDataFile = new File(this.mStoreFile, str);
        this.mMetaDataFileTmp = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_METADATA_FILE_TMP);
        this.mAccountFile = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_ACCOUNT_FILE);
        this.mAccountFileTmp = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_ACCOUNT_FILE_TMP);
        this.mDevicesFolder = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_DEVICES_FOLDER);
        this.mDevicesFile = new File(this.mStoreFile, "devices");
        this.mDevicesFileTmp = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_DEVICES_FILE_TMP);
        this.mAlgorithmsFile = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_ALGORITHMS_FILE);
        this.mAlgorithmsFileTmp = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_ALGORITHMS_FILE_TMP);
        this.mTrackingStatusesFile = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_TRACKING_STATUSES_FILE);
        this.mTrackingStatusesFileTmp = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_TRACKING_STATUSES_FILE_TMP);
        this.mOlmSessionsFile = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_OLM_SESSIONS_FILE);
        this.mOlmSessionsFileTmp = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_OLM_SESSIONS_FILE_TMP);
        this.mOlmSessionsFolder = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_OLM_SESSIONS_FOLDER);
        this.mInboundGroupSessionsFile = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_INBOUND_GROUP_SESSIONS_FILE);
        this.mInboundGroupSessionsFileTmp = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_INBOUND_GROUP_SESSIONS_FILE_TMP);
        this.mInboundGroupSessionsFolder = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_INBOUND_GROUP_SESSIONS_FOLDER);
        this.mOutgoingRoomKeyRequestsFile = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_OUTGOING_ROOM_KEY_REQUEST_FILE);
        this.mOutgoingRoomKeyRequestsFileTmp = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_OUTGOING_ROOM_KEY_REQUEST_FILE_TMP);
        this.mIncomingRoomKeyRequestsFile = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_INCOMING_ROOM_KEY_REQUESTS_FILE);
        this.mIncomingRoomKeyRequestsFileTmp = new File(this.mStoreFile, MXFILE_CRYPTO_STORE_INCOMING_ROOM_KEY_REQUESTS_FILE_TMP);
        if (!(this.mMetaData != null || credentials.getHomeServer() == null || credentials.getUserId() == null || credentials.getAccessToken() == null)) {
            this.mMetaData = new MXFileCryptoStoreMetaData2(this.mCredentials.getUserId(), this.mCredentials.getDeviceId(), 1);
        }
        this.mUsersDevicesInfoMap = new MXUsersDevicesMap<>();
        this.mRoomsAlgorithms = new HashMap();
        this.mTrackingStatuses = new HashMap();
        this.mOlmSessions = new HashMap();
        this.mInboundGroupSessions = new HashMap();
        this.mContext = context;
    }

    public boolean hasData() {
        boolean exists = this.mStoreFile.exists();
        if (!exists) {
            return exists;
        }
        loadMetaData();
        MXFileCryptoStoreMetaData2 mXFileCryptoStoreMetaData2 = this.mMetaData;
        if (mXFileCryptoStoreMetaData2 != null) {
            return TextUtils.isEmpty(mXFileCryptoStoreMetaData2.mDeviceId) || TextUtils.equals(this.mCredentials.getDeviceId(), this.mMetaData.mDeviceId);
        }
        return exists;
    }

    public boolean isCorrupted() {
        return this.mIsCorrupted;
    }

    public void deleteStore() {
        try {
            FileContentUtils.deleteDirectory(this.mStoreFile);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("deleteStore failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    public void open() {
        if (this.mIsReady) {
            Log.e(LOG_TAG, "## open() : the store is already opened");
            return;
        }
        this.mMetaData = null;
        loadMetaData();
        MXFileCryptoStoreMetaData2 mXFileCryptoStoreMetaData2 = this.mMetaData;
        if (mXFileCryptoStoreMetaData2 == null) {
            resetData();
        } else if (1 != mXFileCryptoStoreMetaData2.mVersion) {
            Log.e(LOG_TAG, "## open() : New MXFileCryptoStore version detected");
            resetData();
        } else if (!TextUtils.equals(this.mMetaData.mUserId, this.mCredentials.getUserId()) || (this.mCredentials.getDeviceId() != null && !TextUtils.equals(this.mCredentials.getDeviceId(), this.mMetaData.mDeviceId))) {
            Log.e(LOG_TAG, "## open() : Credentials do not match");
            resetData();
        }
        if (this.mMetaData != null) {
            preloadCryptoData();
        }
        if (this.mMetaData != null || this.mCredentials.getHomeServer() == null || this.mCredentials.getUserId() == null || this.mCredentials.getAccessToken() == null) {
            this.mIsReady = true;
            return;
        }
        this.mMetaData = new MXFileCryptoStoreMetaData2(this.mCredentials.getUserId(), this.mCredentials.getDeviceId(), 1);
        this.mIsReady = true;
        saveMetaData();
    }

    public void storeDeviceId(String str) {
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## storeDeviceId() : the store is not ready");
            return;
        }
        this.mMetaData.mDeviceId = str;
        saveMetaData();
    }

    public String getDeviceId() {
        if (this.mIsReady) {
            return this.mMetaData.mDeviceId;
        }
        Log.e(LOG_TAG, "## getDeviceId() : the store is not ready");
        return null;
    }

    private boolean storeObject(Object obj, File file, String str, String str2) {
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## storeObject() : the store is not ready");
            return false;
        } else if (obj == null || file == null || str == null) {
            Log.e(LOG_TAG, "## storeObject() : invalid parameters");
            return false;
        } else {
            if (!file.exists() && !file.mkdirs()) {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot create the folder ");
                sb.append(file);
                Log.e(str3, sb.toString());
            }
            return storeObject(obj, new File(file, str), str2);
        }
    }

    private boolean storeObject(Object obj, File file, String str) {
        boolean z = false;
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## storeObject() : the store is not ready");
            return false;
        }
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## storeObject() : should not be called in the UI thread ");
            sb.append(str);
            Log.e(str2, sb.toString());
        }
        synchronized (LOG_TAG) {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                if (file.exists()) {
                    file.delete();
                }
                OutputStream fileOutputStream = new FileOutputStream(file);
                if (this.mEnableFileEncryption) {
                    fileOutputStream = CompatUtil.createCipherOutputStream(fileOutputStream, this.mContext);
                }
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(CompatUtil.createGzipOutputStream(fileOutputStream));
                objectOutputStream.writeObject(obj);
                objectOutputStream.flush();
                objectOutputStream.close();
                z = true;
                String str3 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## storeObject () : ");
                sb2.append(str);
                sb2.append(" done in ");
                sb2.append(System.currentTimeMillis() - currentTimeMillis);
                sb2.append(" ms");
                Log.d(str3, sb2.toString());
            } catch (OutOfMemoryError e) {
                String str4 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("storeObject failed : ");
                sb3.append(str);
                sb3.append(" -- ");
                sb3.append(e.getMessage());
                Log.e(str4, sb3.toString(), e);
            } catch (Exception e2) {
                String str5 = LOG_TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("storeObject failed : ");
                sb4.append(str);
                sb4.append(" -- ");
                sb4.append(e2.getMessage());
                Log.e(str5, sb4.toString(), e2);
            }
        }
        return z;
    }

    private void saveMetaData() {
        if (this.mMetaDataFileTmp.exists()) {
            this.mMetaDataFileTmp.delete();
        }
        if (this.mMetaDataFile.exists()) {
            this.mMetaDataFile.renameTo(this.mMetaDataFileTmp);
        }
        if (storeObject(this.mMetaData, this.mMetaDataFile, "saveMetaData")) {
            if (this.mMetaDataFileTmp.exists()) {
                this.mMetaDataFileTmp.delete();
            }
        } else if (this.mMetaDataFileTmp.exists()) {
            this.mMetaDataFileTmp.renameTo(this.mMetaDataFile);
        }
    }

    public void storeAccount(OlmAccount olmAccount) {
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## storeAccount() : the store is not ready");
            return;
        }
        this.mOlmAccount = olmAccount;
        if (this.mAccountFileTmp.exists()) {
            this.mAccountFileTmp.delete();
        }
        if (this.mAccountFile.exists()) {
            this.mAccountFile.renameTo(this.mAccountFileTmp);
        }
        if (storeObject(this.mOlmAccount, this.mAccountFile, "storeAccount")) {
            if (this.mAccountFileTmp.exists()) {
                this.mAccountFileTmp.delete();
            }
        } else if (this.mAccountFileTmp.exists()) {
            this.mAccountFileTmp.renameTo(this.mAccountFile);
        }
    }

    public OlmAccount getAccount() {
        if (this.mIsReady) {
            return this.mOlmAccount;
        }
        Log.e(LOG_TAG, "## getAccount() : the store is not ready");
        return null;
    }

    private void loadUserDevices(String str) {
        boolean containsKey;
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.mUsersDevicesInfoMapLock) {
                containsKey = this.mUsersDevicesInfoMap.getMap().containsKey(str);
            }
            if (!containsKey) {
                File file = new File(this.mDevicesFolder, str);
                if (file.exists()) {
                    long currentTimeMillis = System.currentTimeMillis();
                    this.mIsCorrupted = false;
                    StringBuilder sb = new StringBuilder();
                    sb.append("load devices of ");
                    sb.append(str);
                    Object loadObject = loadObject(file, sb.toString());
                    if (loadObject != null) {
                        try {
                            synchronized (this.mUsersDevicesInfoMapLock) {
                                this.mUsersDevicesInfoMap.setObjects((Map) loadObject, str);
                            }
                        } catch (Exception e) {
                            String str2 = LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("## loadUserDevices : mUsersDevicesInfoMap.setObjects failed ");
                            sb2.append(e.getMessage());
                            Log.e(str2, sb2.toString(), e);
                            this.mIsCorrupted = true;
                        }
                    }
                    if (this.mIsCorrupted) {
                        String str3 = LOG_TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("## loadUserDevices : failed to load the device of ");
                        sb3.append(str);
                        Log.e(str3, sb3.toString());
                        file.delete();
                        this.mIsCorrupted = false;
                        return;
                    }
                    String str4 = LOG_TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("## loadUserDevices : Load the devices of ");
                    sb4.append(str);
                    sb4.append(" in ");
                    sb4.append(System.currentTimeMillis() - currentTimeMillis);
                    sb4.append("ms");
                    Log.d(str4, sb4.toString());
                }
            }
        }
    }

    public void storeUserDevice(String str, MXDeviceInfo mXDeviceInfo) {
        HashMap hashMap;
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## storeUserDevice() : the store is not ready");
            return;
        }
        loadUserDevices(str);
        synchronized (this.mUsersDevicesInfoMapLock) {
            this.mUsersDevicesInfoMap.setObject(mXDeviceInfo, str, mXDeviceInfo.deviceId);
            hashMap = new HashMap((Map) this.mUsersDevicesInfoMap.getMap().get(str));
        }
        File file = this.mDevicesFolder;
        StringBuilder sb = new StringBuilder();
        sb.append("storeUserDevice ");
        sb.append(str);
        sb.append(" with ");
        sb.append(hashMap.size());
        sb.append(" devices");
        storeObject(hashMap, file, str, sb.toString());
    }

    public MXDeviceInfo getUserDevice(String str, String str2) {
        MXDeviceInfo mXDeviceInfo;
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## getUserDevice() : the store is not ready");
            return null;
        }
        loadUserDevices(str2);
        synchronized (this.mUsersDevicesInfoMapLock) {
            mXDeviceInfo = (MXDeviceInfo) this.mUsersDevicesInfoMap.getObject(str, str2);
        }
        return mXDeviceInfo;
    }

    public void storeUserDevices(String str, Map<String, MXDeviceInfo> map) {
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## storeUserDevices() : the store is not ready");
            return;
        }
        synchronized (this.mUsersDevicesInfoMapLock) {
            this.mUsersDevicesInfoMap.setObjects(map, str);
        }
        File file = this.mDevicesFolder;
        StringBuilder sb = new StringBuilder();
        sb.append("storeUserDevice ");
        sb.append(str);
        storeObject(map, file, str, sb.toString());
    }

    public Map<String, MXDeviceInfo> getUserDevices(String str) {
        Map<String, MXDeviceInfo> map;
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## getUserDevices() : the store is not ready");
            return null;
        } else if (str == null) {
            return null;
        } else {
            loadUserDevices(str);
            synchronized (this.mUsersDevicesInfoMapLock) {
                map = (Map) this.mUsersDevicesInfoMap.getMap().get(str);
            }
            return map;
        }
    }

    public void storeRoomAlgorithm(String str, String str2) {
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## storeRoomAlgorithm() : the store is not ready");
            return;
        }
        if (!(str == null || str2 == null)) {
            this.mRoomsAlgorithms.put(str, str2);
            if (this.mAlgorithmsFileTmp.exists()) {
                this.mAlgorithmsFileTmp.delete();
            }
            if (this.mAlgorithmsFile.exists()) {
                this.mAlgorithmsFile.renameTo(this.mAlgorithmsFileTmp);
            }
            if (storeObject(this.mRoomsAlgorithms, this.mAlgorithmsFile, "storeAlgorithmForRoom - in background")) {
                if (this.mAlgorithmsFileTmp.exists()) {
                    this.mAlgorithmsFileTmp.delete();
                }
            } else if (this.mAlgorithmsFileTmp.exists()) {
                this.mAlgorithmsFileTmp.renameTo(this.mAlgorithmsFile);
            }
        }
    }

    public String getRoomAlgorithm(String str) {
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## getRoomAlgorithm() : the store is not ready");
            return null;
        } else if (str != null) {
            return (String) this.mRoomsAlgorithms.get(str);
        } else {
            return null;
        }
    }

    public int getDeviceTrackingStatus(String str, int i) {
        if (this.mIsReady) {
            return (str == null || !this.mTrackingStatuses.containsKey(str)) ? i : ((Integer) this.mTrackingStatuses.get(str)).intValue();
        }
        Log.e(LOG_TAG, "## getDeviceTrackingStatus() : the store is not ready");
        return i;
    }

    public Map<String, Integer> getDeviceTrackingStatuses() {
        if (this.mIsReady) {
            return new HashMap(this.mTrackingStatuses);
        }
        Log.e(LOG_TAG, "## getDeviceTrackingStatuses() : the store is not ready");
        return null;
    }

    private void saveDeviceTrackingStatuses() {
        if (this.mTrackingStatusesFileTmp.exists()) {
            this.mTrackingStatusesFileTmp.delete();
        }
        if (this.mTrackingStatusesFile.exists()) {
            this.mTrackingStatusesFile.renameTo(this.mTrackingStatusesFileTmp);
        }
        if (storeObject(this.mTrackingStatuses, this.mTrackingStatusesFile, "saveDeviceTrackingStatus - in background")) {
            if (this.mTrackingStatusesFileTmp.exists()) {
                this.mTrackingStatusesFileTmp.delete();
            }
        } else if (this.mTrackingStatusesFileTmp.exists()) {
            this.mTrackingStatusesFileTmp.renameTo(this.mTrackingStatusesFile);
        }
    }

    public void saveDeviceTrackingStatuses(Map<String, Integer> map) {
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## saveDeviceTrackingStatuses() : the store is not ready");
            return;
        }
        this.mTrackingStatuses.clear();
        this.mTrackingStatuses.putAll(map);
        saveDeviceTrackingStatuses();
    }

    public void storeSession(MXOlmSession mXOlmSession, String str) {
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## storeSession() : the store is not ready");
            return;
        }
        String str2 = null;
        if (mXOlmSession != null) {
            try {
                str2 = mXOlmSession.getOlmSession().sessionIdentifier();
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## storeSession : session.sessionIdentifier() failed ");
                sb.append(e.getMessage());
                Log.e(str3, sb.toString(), e);
            }
        }
        if (!(str == null || str2 == null)) {
            synchronized (mOlmSessionsLock) {
                if (!this.mOlmSessions.containsKey(str)) {
                    this.mOlmSessions.put(str, new HashMap());
                }
                OlmSession olmSession = (OlmSession) ((Map) this.mOlmSessions.get(str)).get(str2);
                if (mXOlmSession.getOlmSession() != olmSession) {
                    if (olmSession != null) {
                        olmSession.releaseSession();
                    }
                    ((Map) this.mOlmSessions.get(str)).put(str2, mXOlmSession.getOlmSession());
                }
            }
            File file = new File(this.mOlmSessionsFolder, encodeFilename(str));
            if (!file.exists()) {
                file.mkdir();
            }
            OlmSession olmSession2 = mXOlmSession.getOlmSession();
            String encodeFilename = encodeFilename(str2);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Store olm session ");
            sb2.append(str);
            sb2.append(" ");
            sb2.append(str2);
            storeObject(olmSession2, file, encodeFilename, sb2.toString());
        }
    }

    public Set<String> getDeviceSessionIds(String str) {
        Map map;
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## storeSession() : the store is not ready");
            return null;
        }
        if (str != null) {
            synchronized (mOlmSessionsLock) {
                map = (Map) this.mOlmSessions.get(str);
            }
            if (map != null) {
                return map.keySet();
            }
        }
        return null;
    }

    public void removeInboundGroupSession(String str, String str2) {
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## removeInboundGroupSession() : the store is not ready");
            return;
        }
        if (!(str == null || str2 == null)) {
            synchronized (this.mInboundGroupSessionsLock) {
                if (this.mInboundGroupSessions.containsKey(str2)) {
                    MXOlmInboundGroupSession2 mXOlmInboundGroupSession2 = (MXOlmInboundGroupSession2) ((Map) this.mInboundGroupSessions.get(str2)).get(str);
                    if (mXOlmInboundGroupSession2 != null) {
                        ((Map) this.mInboundGroupSessions.get(str2)).remove(str);
                        File file = new File(this.mInboundGroupSessionsFolder, encodeFilename(mXOlmInboundGroupSession2.mSenderKey));
                        if (file.exists() && !new File(file, encodeFilename(str)).delete()) {
                            String str3 = LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## removeInboundGroupSession() : fail to remove the sessionid ");
                            sb.append(str);
                            Log.e(str3, sb.toString());
                        }
                        mXOlmInboundGroupSession2.mSession.releaseSession();
                    }
                }
            }
        }
    }

    public MXOlmInboundGroupSession2 getInboundGroupSession(String str, String str2) {
        MXOlmInboundGroupSession2 mXOlmInboundGroupSession2;
        MXOlmInboundGroupSession2 mXOlmInboundGroupSession22 = null;
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## getInboundGroupSession() : the store is not ready");
            return null;
        } else if (str == null || str2 == null || !this.mInboundGroupSessions.containsKey(str2)) {
            return null;
        } else {
            try {
                synchronized (this.mInboundGroupSessionsLock) {
                    try {
                        mXOlmInboundGroupSession2 = (MXOlmInboundGroupSession2) ((Map) this.mInboundGroupSessions.get(str2)).get(str);
                        try {
                        } catch (Throwable th) {
                            th = th;
                            mXOlmInboundGroupSession22 = mXOlmInboundGroupSession2;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getInboundGroupSession() failed ");
                sb.append(e.getMessage());
                Log.e(str3, sb.toString(), e);
                mXOlmInboundGroupSession2 = mXOlmInboundGroupSession22;
            }
        }
        return mXOlmInboundGroupSession2;
    }

    public List<MXOlmInboundGroupSession2> getInboundGroupSessions() {
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## getInboundGroupSessions() : the store is not ready");
            return null;
        }
        ArrayList arrayList = new ArrayList();
        synchronized (this.mInboundGroupSessionsLock) {
            for (String str : this.mInboundGroupSessions.keySet()) {
                arrayList.addAll(((Map) this.mInboundGroupSessions.get(str)).values());
            }
        }
        return arrayList;
    }

    public List<MXOlmInboundGroupSession2> inboundGroupSessionsToBackup(int i) {
        return new ArrayList();
    }

    public void close() {
        ArrayList<OlmSession> arrayList = new ArrayList<>();
        for (Map values : this.mOlmSessions.values()) {
            arrayList.addAll(values.values());
        }
        for (OlmSession releaseSession : arrayList) {
            releaseSession.releaseSession();
        }
        this.mOlmSessions.clear();
        ArrayList<MXOlmInboundGroupSession2> arrayList2 = new ArrayList<>();
        for (Map values2 : this.mInboundGroupSessions.values()) {
            arrayList2.addAll(values2.values());
        }
        for (MXOlmInboundGroupSession2 mXOlmInboundGroupSession2 : arrayList2) {
            if (mXOlmInboundGroupSession2.mSession != null) {
                mXOlmInboundGroupSession2.mSession.releaseSession();
            }
        }
        this.mInboundGroupSessions.clear();
    }

    public void setGlobalBlacklistUnverifiedDevices(boolean z) {
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## setGlobalBlacklistUnverifiedDevices() : the store is not ready");
            return;
        }
        this.mMetaData.mGlobalBlacklistUnverifiedDevices = z;
        saveMetaData();
    }

    public boolean getGlobalBlacklistUnverifiedDevices() {
        if (this.mIsReady) {
            return this.mMetaData.mGlobalBlacklistUnverifiedDevices;
        }
        Log.e(LOG_TAG, "## getGlobalBlacklistUnverifiedDevices() : the store is not ready");
        return false;
    }

    public void setRoomsListBlacklistUnverifiedDevices(List<String> list) {
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## setRoomsListBlacklistUnverifiedDevices() : the store is not ready");
            return;
        }
        this.mMetaData.mBlacklistUnverifiedDevicesRoomIdsList = list;
        saveMetaData();
    }

    public List<String> getRoomsListBlacklistUnverifiedDevices() {
        if (!this.mIsReady) {
            Log.e(LOG_TAG, "## getRoomsListBlacklistUnverifiedDevices() : the store is not ready");
            return null;
        } else if (this.mMetaData.mBlacklistUnverifiedDevicesRoomIdsList == null) {
            return new ArrayList();
        } else {
            return new ArrayList(this.mMetaData.mBlacklistUnverifiedDevicesRoomIdsList);
        }
    }

    private void saveOutgoingRoomKeyRequests() {
        if (this.mOutgoingRoomKeyRequestsFileTmp.exists()) {
            this.mOutgoingRoomKeyRequestsFileTmp.delete();
        }
        if (this.mOutgoingRoomKeyRequestsFile.exists()) {
            this.mOutgoingRoomKeyRequestsFile.renameTo(this.mOutgoingRoomKeyRequestsFileTmp);
        }
        if (storeObject(this.mOutgoingRoomKeyRequests, this.mOutgoingRoomKeyRequestsFile, "saveOutgoingRoomKeyRequests")) {
            if (this.mOutgoingRoomKeyRequestsFileTmp.exists()) {
                this.mOutgoingRoomKeyRequestsFileTmp.delete();
            }
        } else if (this.mOutgoingRoomKeyRequestsFileTmp.exists()) {
            this.mOutgoingRoomKeyRequestsFileTmp.renameTo(this.mOutgoingRoomKeyRequestsFile);
        }
    }

    public OutgoingRoomKeyRequest getOrAddOutgoingRoomKeyRequest(OutgoingRoomKeyRequest outgoingRoomKeyRequest) {
        if (outgoingRoomKeyRequest == null || outgoingRoomKeyRequest.mRequestBody == null) {
            return null;
        }
        if (this.mOutgoingRoomKeyRequests.containsKey(outgoingRoomKeyRequest.mRequestBody)) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getOrAddOutgoingRoomKeyRequest() : `already have key request outstanding for ");
            sb.append(outgoingRoomKeyRequest.getRoomId());
            sb.append(" / ");
            sb.append(outgoingRoomKeyRequest.getSessionId());
            sb.append(" not sending another");
            Log.d(str, sb.toString());
            return (OutgoingRoomKeyRequest) this.mOutgoingRoomKeyRequests.get(outgoingRoomKeyRequest.mRequestBody);
        }
        HashMap hashMap = new HashMap();
        hashMap.put(CryptoRoomEntityFields.ALGORITHM, outgoingRoomKeyRequest.mRequestBody.algorithm);
        hashMap.put("room_id", outgoingRoomKeyRequest.mRequestBody.roomId);
        hashMap.put("sender_key", outgoingRoomKeyRequest.mRequestBody.senderKey);
        hashMap.put(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_SESSION_ID, outgoingRoomKeyRequest.mRequestBody.sessionId);
        this.mOutgoingRoomKeyRequests.put(hashMap, outgoingRoomKeyRequest);
        saveOutgoingRoomKeyRequests();
        return outgoingRoomKeyRequest;
    }

    private OutgoingRoomKeyRequest getOutgoingRoomKeyRequestByTxId(String str) {
        if (str != null) {
            for (OutgoingRoomKeyRequest outgoingRoomKeyRequest : this.mOutgoingRoomKeyRequests.values()) {
                if (TextUtils.equals(outgoingRoomKeyRequest.mRequestId, str)) {
                    return outgoingRoomKeyRequest;
                }
            }
        }
        return null;
    }

    public OutgoingRoomKeyRequest getOutgoingRoomKeyRequestByState(Set<RequestState> set) {
        for (OutgoingRoomKeyRequest outgoingRoomKeyRequest : this.mOutgoingRoomKeyRequests.values()) {
            if (set.contains(outgoingRoomKeyRequest.mState)) {
                return outgoingRoomKeyRequest;
            }
        }
        return null;
    }

    public void updateOutgoingRoomKeyRequest(OutgoingRoomKeyRequest outgoingRoomKeyRequest) {
        if (outgoingRoomKeyRequest != null) {
            saveOutgoingRoomKeyRequests();
        }
    }

    public void deleteOutgoingRoomKeyRequest(String str) {
        OutgoingRoomKeyRequest outgoingRoomKeyRequestByTxId = getOutgoingRoomKeyRequestByTxId(str);
        if (outgoingRoomKeyRequestByTxId != null) {
            this.mOutgoingRoomKeyRequests.remove(outgoingRoomKeyRequestByTxId.mRequestBody);
            saveOutgoingRoomKeyRequests();
        }
    }

    private void resetData() {
        close();
        synchronized (LOG_TAG) {
            deleteStore();
        }
        if (!this.mStoreFile.exists()) {
            this.mStoreFile.mkdirs();
        }
        if (!this.mDevicesFolder.exists()) {
            this.mDevicesFolder.mkdirs();
        }
        if (!this.mOlmSessionsFolder.exists()) {
            this.mOlmSessionsFolder.mkdir();
        }
        if (!this.mInboundGroupSessionsFolder.exists()) {
            this.mInboundGroupSessionsFolder.mkdirs();
        }
        this.mMetaData = null;
    }

    /* JADX WARNING: type inference failed for: r0v6, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r2v5, types: [java.io.InputStream] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    private Object loadObject(File file, String str) {
        Object obj = null;
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            if (this.mEnableFileEncryption) {
                ? createCipherInputStream = CompatUtil.createCipherInputStream(fileInputStream, this.mContext);
                if (createCipherInputStream == 0) {
                    Log.i(LOG_TAG, "## loadObject() : failed to read encrypted, fallback to unencrypted read");
                    fileInputStream.close();
                    fileInputStream = new FileInputStream(file);
                } else {
                    fileInputStream = createCipherInputStream;
                }
            }
            ObjectInputStream objectInputStream = new ObjectInputStream(new GZIPInputStream(fileInputStream));
            Object readObject = objectInputStream.readObject();
            objectInputStream.close();
            return readObject;
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            String str3 = "failed : ";
            sb.append(str3);
            sb.append(e.getMessage());
            sb.append(" step 1");
            Log.e(str2, sb.toString(), e);
            try {
                ObjectInputStream objectInputStream2 = new ObjectInputStream(new FileInputStream(file));
                obj = objectInputStream2.readObject();
                objectInputStream2.close();
                return obj;
            } catch (Exception e2) {
                this.mIsCorrupted = true;
                String str4 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(str3);
                sb2.append(e2.getMessage());
                sb2.append(" step 2");
                Log.e(str4, sb2.toString(), e2);
                return obj;
            }
        }
    }

    private void loadMetaData() {
        Object obj;
        String str = "loadMetadata";
        if (this.mMetaDataFileTmp.exists()) {
            obj = loadObject(this.mMetaDataFileTmp, str);
        } else {
            obj = loadObject(this.mMetaDataFile, str);
        }
        if (obj != null) {
            try {
                if (obj instanceof MXFileCryptoStoreMetaData2) {
                    this.mMetaData = (MXFileCryptoStoreMetaData2) obj;
                } else {
                    this.mMetaData = new MXFileCryptoStoreMetaData2((MXFileCryptoStoreMetaData) obj);
                }
            } catch (Exception e) {
                this.mIsCorrupted = true;
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## loadMetadata() : metadata has been corrupted ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:107:0x03b6  */
    /* JADX WARNING: Removed duplicated region for block: B:151:0x04b6  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0186  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x018f  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0199 A[SYNTHETIC, Splitter:B:45:0x0199] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x01c6  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x01c9  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x01d1  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x020a  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x02a7  */
    private void preloadCryptoData() {
        Object obj;
        Object obj2;
        int i;
        Object obj3;
        File file;
        Object obj4;
        String str;
        String str2;
        File file2;
        MXOlmInboundGroupSession2 mXOlmInboundGroupSession2;
        Object obj5;
        Object obj6;
        Log.d(LOG_TAG, "## preloadCryptoData() starts");
        long currentTimeMillis = System.currentTimeMillis();
        if (this.mAccountFileTmp.exists()) {
            obj = loadObject(this.mAccountFileTmp, "preloadCryptoData - mAccountFile - tmp");
        } else {
            obj = loadObject(this.mAccountFile, "preloadCryptoData - mAccountFile");
        }
        if (obj != null) {
            try {
                this.mOlmAccount = (OlmAccount) obj;
            } catch (Exception e) {
                this.mIsCorrupted = true;
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## preloadCryptoData() - invalid mAccountFile ");
                sb.append(e.getMessage());
                Log.e(str3, sb.toString(), e);
            }
        }
        String str4 = LOG_TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("## preloadCryptoData() : load mOlmAccount in ");
        sb2.append(System.currentTimeMillis() - currentTimeMillis);
        String str5 = " ms";
        sb2.append(str5);
        Log.d(str4, sb2.toString());
        if (!this.mDevicesFolder.exists()) {
            if (this.mDevicesFileTmp.exists()) {
                obj6 = loadObject(this.mDevicesFileTmp, "preloadCryptoData - mUsersDevicesInfoMap - tmp");
            } else {
                obj6 = loadObject(this.mDevicesFile, "preloadCryptoData - mUsersDevicesInfoMap");
            }
            if (obj6 != null) {
                try {
                    this.mUsersDevicesInfoMap = new MXUsersDevicesMap<>(((MXUsersDevicesMap) obj6).getMap());
                } catch (Exception e2) {
                    this.mIsCorrupted = true;
                    String str6 = LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("## preloadCryptoData() - invalid mUsersDevicesInfoMap ");
                    sb3.append(e2.getMessage());
                    Log.e(str6, sb3.toString(), e2);
                }
            } else {
                this.mIsCorrupted = false;
            }
            this.mDevicesFolder.mkdirs();
            MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap = this.mUsersDevicesInfoMap;
            if (mXUsersDevicesMap != null) {
                Map map = mXUsersDevicesMap.getMap();
                for (String str7 : map.keySet()) {
                    Object obj7 = map.get(str7);
                    File file3 = this.mDevicesFolder;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("convert devices map of ");
                    sb4.append(str7);
                    storeObject(obj7, file3, str7, sb4.toString());
                }
                this.mDevicesFileTmp.delete();
                this.mDevicesFile.delete();
            }
        } else {
            this.mUsersDevicesInfoMap = new MXUsersDevicesMap<>();
        }
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mAlgorithmsFileTmp.exists()) {
            obj2 = loadObject(this.mAlgorithmsFileTmp, "preloadCryptoData - mRoomsAlgorithms - tmp");
        } else {
            obj2 = loadObject(this.mAlgorithmsFile, "preloadCryptoData - mRoomsAlgorithms");
        }
        if (obj2 != null) {
            try {
                this.mRoomsAlgorithms = new HashMap((Map) obj2);
                i = this.mRoomsAlgorithms.size();
            } catch (Exception e3) {
                this.mIsCorrupted = true;
                String str8 = LOG_TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("## preloadCryptoData() - invalid mAlgorithmsFile ");
                sb5.append(e3.getMessage());
                Log.e(str8, sb5.toString(), e3);
            }
            String str9 = LOG_TAG;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("## preloadCryptoData() : load mRoomsAlgorithms (");
            sb6.append(i);
            sb6.append(" algos) in ");
            sb6.append(System.currentTimeMillis() - currentTimeMillis2);
            sb6.append(str5);
            Log.d(str9, sb6.toString());
            if (!this.mTrackingStatusesFileTmp.exists()) {
                obj3 = loadObject(this.mTrackingStatusesFileTmp, "preloadCryptoData - mTrackingStatuses - tmp");
            } else {
                obj3 = loadObject(this.mTrackingStatusesFile, "preloadCryptoData - mTrackingStatuses");
            }
            if (obj3 != null) {
                try {
                    this.mTrackingStatuses = new HashMap((Map) obj3);
                } catch (Exception e4) {
                    String str10 = LOG_TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("## preloadCryptoData() - invalid mTrackingStatuses ");
                    sb7.append(e4.getMessage());
                    Log.e(str10, sb7.toString(), e4);
                }
            }
            if (!this.mOutgoingRoomKeyRequestsFileTmp.exists()) {
                file = this.mOutgoingRoomKeyRequestsFileTmp;
            } else {
                file = this.mOutgoingRoomKeyRequestsFile;
            }
            if (file.exists()) {
                Object loadObject = loadObject(file, "get outgoing key request");
                if (loadObject != null) {
                    try {
                        this.mOutgoingRoomKeyRequests.putAll((Map) loadObject);
                    } catch (Exception e5) {
                        String str11 = LOG_TAG;
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("## preloadCryptoData() : mOutgoingRoomKeyRequests init failed ");
                        sb8.append(e5.getMessage());
                        Log.e(str11, sb8.toString(), e5);
                    }
                }
            }
            String str12 = "## preloadCryptoData() : load ";
            String str13 = " ";
            String str14 = "Cannot create the folder ";
            if (!this.mOlmSessionsFolder.exists()) {
                long currentTimeMillis3 = System.currentTimeMillis();
                this.mOlmSessions = new HashMap();
                String[] list = this.mOlmSessionsFolder.list();
                if (list != null) {
                    for (String str15 : list) {
                        HashMap hashMap = new HashMap();
                        File file4 = new File(this.mOlmSessionsFolder, str15);
                        String[] list2 = file4.list();
                        if (list2 != null) {
                            int i2 = 0;
                            while (i2 < list2.length) {
                                String str16 = list2[i2];
                                File file5 = new File(file4, str16);
                                File file6 = file4;
                                StringBuilder sb9 = new StringBuilder();
                                String[] strArr = list2;
                                sb9.append("load the olmSession ");
                                sb9.append(str15);
                                sb9.append(str13);
                                sb9.append(str16);
                                OlmSession olmSession = (OlmSession) loadObject(file5, sb9.toString());
                                if (olmSession != null) {
                                    hashMap.put(decodeFilename(str16), olmSession);
                                }
                                i2++;
                                file4 = file6;
                                list2 = strArr;
                            }
                        }
                        this.mOlmSessions.put(decodeFilename(str15), hashMap);
                    }
                    String str17 = LOG_TAG;
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append(str12);
                    sb10.append(list.length);
                    sb10.append(" olmsessions in ");
                    sb10.append(System.currentTimeMillis() - currentTimeMillis3);
                    sb10.append(str5);
                    Log.d(str17, sb10.toString());
                }
            } else {
                if (this.mOlmSessionsFileTmp.exists()) {
                    obj5 = loadObject(this.mOlmSessionsFileTmp, "preloadCryptoData - mOlmSessions - tmp");
                } else {
                    obj5 = loadObject(this.mOlmSessionsFile, "preloadCryptoData - mOlmSessions");
                }
                if (obj5 != null) {
                    try {
                        Map map2 = (Map) obj5;
                        this.mOlmSessions = new HashMap();
                        for (String str18 : map2.keySet()) {
                            this.mOlmSessions.put(str18, new HashMap((Map) map2.get(str18)));
                        }
                        if (!this.mOlmSessionsFolder.mkdir()) {
                            String str19 = LOG_TAG;
                            StringBuilder sb11 = new StringBuilder();
                            sb11.append(str14);
                            sb11.append(this.mOlmSessionsFolder);
                            Log.e(str19, sb11.toString());
                        }
                        for (String str20 : map2.keySet()) {
                            Map map3 = (Map) map2.get(str20);
                            File file7 = new File(this.mOlmSessionsFolder, encodeFilename(str20));
                            if (!file7.mkdir()) {
                                String str21 = LOG_TAG;
                                StringBuilder sb12 = new StringBuilder();
                                sb12.append(str14);
                                sb12.append(file7);
                                Log.e(str21, sb12.toString());
                            }
                            for (String str22 : map3.keySet()) {
                                Object obj8 = map3.get(str22);
                                String encodeFilename = encodeFilename(str22);
                                StringBuilder sb13 = new StringBuilder();
                                sb13.append("Convert olmSession ");
                                sb13.append(str20);
                                sb13.append(str13);
                                sb13.append(str22);
                                storeObject(obj8, file7, encodeFilename, sb13.toString());
                            }
                        }
                    } catch (Exception e6) {
                        this.mIsCorrupted = true;
                        String str23 = LOG_TAG;
                        StringBuilder sb14 = new StringBuilder();
                        sb14.append("## preloadCryptoData() - invalid mSessionsFile ");
                        sb14.append(e6.getMessage());
                        Log.e(str23, sb14.toString(), e6);
                    }
                    this.mOlmSessionsFileTmp.delete();
                    this.mOlmSessionsFile.delete();
                }
            }
            String str24 = "## preloadCryptoData() - invalid mInboundGroupSessions ";
            if (!this.mInboundGroupSessionsFolder.exists()) {
                long currentTimeMillis4 = System.currentTimeMillis();
                this.mInboundGroupSessions = new HashMap();
                String[] list3 = this.mInboundGroupSessionsFolder.list();
                int i3 = 0;
                if (list3 != null) {
                    int i4 = 0;
                    while (i4 < list3.length) {
                        File file8 = new File(this.mInboundGroupSessionsFolder, list3[i4]);
                        HashMap hashMap2 = new HashMap();
                        String[] list4 = file8.list();
                        if (list4 != null) {
                            int i5 = i3;
                            int i6 = 0;
                            while (i6 < list4.length) {
                                File file9 = new File(file8, list4[i6]);
                                try {
                                    StringBuilder sb15 = new StringBuilder();
                                    file2 = file8;
                                    try {
                                        sb15.append("load inboundsession ");
                                        sb15.append(list4[i6]);
                                        sb15.append(str13);
                                        Object loadObject2 = loadObject(file9, sb15.toString());
                                        if (loadObject2 == null || !(loadObject2 instanceof MXOlmInboundGroupSession)) {
                                            mXOlmInboundGroupSession2 = (MXOlmInboundGroupSession2) loadObject2;
                                        } else {
                                            mXOlmInboundGroupSession2 = new MXOlmInboundGroupSession2((MXOlmInboundGroupSession) loadObject2);
                                        }
                                        if (mXOlmInboundGroupSession2 != null) {
                                            hashMap2.put(decodeFilename(list4[i6]), mXOlmInboundGroupSession2);
                                            str2 = str13;
                                        } else {
                                            String str25 = LOG_TAG;
                                            StringBuilder sb16 = new StringBuilder();
                                            str2 = str13;
                                            try {
                                                sb16.append("## preloadCryptoData() : delete ");
                                                sb16.append(file9);
                                                Log.e(str25, sb16.toString());
                                                file9.delete();
                                                try {
                                                    this.mIsCorrupted = false;
                                                } catch (Exception e7) {
                                                    e = e7;
                                                    String str26 = LOG_TAG;
                                                    StringBuilder sb17 = new StringBuilder();
                                                    sb17.append(str24);
                                                    sb17.append(e.getMessage());
                                                    Log.e(str26, sb17.toString(), e);
                                                    i6++;
                                                    file8 = file2;
                                                    str13 = str2;
                                                }
                                            } catch (Exception e8) {
                                                e = e8;
                                                String str262 = LOG_TAG;
                                                StringBuilder sb172 = new StringBuilder();
                                                sb172.append(str24);
                                                sb172.append(e.getMessage());
                                                Log.e(str262, sb172.toString(), e);
                                                i6++;
                                                file8 = file2;
                                                str13 = str2;
                                            }
                                        }
                                        i5++;
                                    } catch (Exception e9) {
                                        e = e9;
                                        str2 = str13;
                                        String str2622 = LOG_TAG;
                                        StringBuilder sb1722 = new StringBuilder();
                                        sb1722.append(str24);
                                        sb1722.append(e.getMessage());
                                        Log.e(str2622, sb1722.toString(), e);
                                        i6++;
                                        file8 = file2;
                                        str13 = str2;
                                    }
                                } catch (Exception e10) {
                                    e = e10;
                                    str2 = str13;
                                    file2 = file8;
                                    String str26222 = LOG_TAG;
                                    StringBuilder sb17222 = new StringBuilder();
                                    sb17222.append(str24);
                                    sb17222.append(e.getMessage());
                                    Log.e(str26222, sb17222.toString(), e);
                                    i6++;
                                    file8 = file2;
                                    str13 = str2;
                                }
                                i6++;
                                file8 = file2;
                                str13 = str2;
                            }
                            str = str13;
                            i3 = i5;
                        } else {
                            str = str13;
                        }
                        this.mInboundGroupSessions.put(decodeFilename(list3[i4]), hashMap2);
                        i4++;
                        str13 = str;
                    }
                }
                String str27 = LOG_TAG;
                StringBuilder sb18 = new StringBuilder();
                sb18.append(str12);
                sb18.append(i3);
                sb18.append(" inboundGroupSessions in ");
                sb18.append(System.currentTimeMillis() - currentTimeMillis4);
                sb18.append(str5);
                Log.d(str27, sb18.toString());
            } else {
                if (this.mInboundGroupSessionsFileTmp.exists()) {
                    obj4 = loadObject(this.mInboundGroupSessionsFileTmp, "preloadCryptoData - mInboundGroupSessions - tmp");
                } else {
                    obj4 = loadObject(this.mInboundGroupSessionsFile, "preloadCryptoData - mInboundGroupSessions");
                }
                if (obj4 != null) {
                    try {
                        Map map4 = (Map) obj4;
                        this.mInboundGroupSessions = new HashMap();
                        for (String str28 : map4.keySet()) {
                            this.mInboundGroupSessions.put(str28, new HashMap((Map) map4.get(str28)));
                        }
                    } catch (Exception e11) {
                        this.mIsCorrupted = true;
                        String str29 = LOG_TAG;
                        StringBuilder sb19 = new StringBuilder();
                        sb19.append(str24);
                        sb19.append(e11.getMessage());
                        Log.e(str29, sb19.toString(), e11);
                    }
                    if (!this.mInboundGroupSessionsFolder.mkdirs()) {
                        String str30 = LOG_TAG;
                        StringBuilder sb20 = new StringBuilder();
                        sb20.append(str14);
                        sb20.append(this.mInboundGroupSessionsFolder);
                        Log.e(str30, sb20.toString());
                    }
                    for (String str31 : this.mInboundGroupSessions.keySet()) {
                        File file10 = new File(this.mInboundGroupSessionsFolder, encodeFilename(str31));
                        if (!file10.mkdirs()) {
                            String str32 = LOG_TAG;
                            StringBuilder sb21 = new StringBuilder();
                            sb21.append(str14);
                            sb21.append(file10);
                            Log.e(str32, sb21.toString());
                        }
                        Map map5 = (Map) this.mInboundGroupSessions.get(str31);
                        for (String str33 : map5.keySet()) {
                            storeObject(map5.get(str33), file10, encodeFilename(str33), "Convert inboundsession");
                        }
                    }
                }
                this.mInboundGroupSessionsFileTmp.delete();
                this.mInboundGroupSessionsFile.delete();
            }
            if (this.mOlmAccount == null && this.mUsersDevicesInfoMap.getMap().size() > 0) {
                this.mIsCorrupted = true;
                Log.e(LOG_TAG, "## preloadCryptoData() - there is no account but some devices are defined");
                return;
            }
        }
        i = 0;
        String str92 = LOG_TAG;
        StringBuilder sb62 = new StringBuilder();
        sb62.append("## preloadCryptoData() : load mRoomsAlgorithms (");
        sb62.append(i);
        sb62.append(" algos) in ");
        sb62.append(System.currentTimeMillis() - currentTimeMillis2);
        sb62.append(str5);
        Log.d(str92, sb62.toString());
        if (!this.mTrackingStatusesFileTmp.exists()) {
        }
        if (obj3 != null) {
        }
        if (!this.mOutgoingRoomKeyRequestsFileTmp.exists()) {
        }
        if (file.exists()) {
        }
        String str122 = "## preloadCryptoData() : load ";
        String str132 = " ";
        String str142 = "Cannot create the folder ";
        if (!this.mOlmSessionsFolder.exists()) {
        }
        String str242 = "## preloadCryptoData() - invalid mInboundGroupSessions ";
        if (!this.mInboundGroupSessionsFolder.exists()) {
        }
        if (this.mOlmAccount == null) {
        }
    }

    private static String encodeFilename(String str) {
        if (str == null) {
            return null;
        }
        try {
            byte[] bytes = str.getBytes("UTF-8");
            char[] cArr = new char[(bytes.length * 2)];
            for (int i = 0; i < bytes.length; i++) {
                byte b = bytes[i] & UByte.MAX_VALUE;
                int i2 = i * 2;
                cArr[i2] = hexArray[b >>> 4];
                cArr[i2 + 1] = hexArray[b & 15];
            }
            return new String(cArr);
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## encodeFilename() - failed ");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString(), e);
            return str;
        }
    }

    private static String decodeFilename(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        try {
            return new String(bArr, "UTF-8");
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## decodeFilename() - failed ");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString(), e);
            return str;
        }
    }

    private boolean isValidIncomingRoomKeyRequest(IncomingRoomKeyRequest incomingRoomKeyRequest) {
        return incomingRoomKeyRequest != null && !TextUtils.isEmpty(incomingRoomKeyRequest.mUserId) && !TextUtils.isEmpty(incomingRoomKeyRequest.mDeviceId) && !TextUtils.isEmpty(incomingRoomKeyRequest.mRequestId);
    }

    public IncomingRoomKeyRequest getIncomingRoomKeyRequest(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3) || !this.mPendingIncomingRoomKeyRequests.containsKey(str) || !((Map) this.mPendingIncomingRoomKeyRequests.get(str)).containsKey(str2)) {
            return null;
        }
        for (IncomingRoomKeyRequest incomingRoomKeyRequest : (List) ((Map) this.mPendingIncomingRoomKeyRequests.get(str)).get(str2)) {
            if (TextUtils.equals(str3, incomingRoomKeyRequest.mRequestId)) {
                return incomingRoomKeyRequest;
            }
        }
        return null;
    }

    public List<IncomingRoomKeyRequest> getPendingIncomingRoomKeyRequests() {
        loadIncomingRoomKeyRequests();
        ArrayList arrayList = new ArrayList();
        for (String str : this.mPendingIncomingRoomKeyRequests.keySet()) {
            for (String str2 : ((Map) this.mPendingIncomingRoomKeyRequests.get(str)).keySet()) {
                arrayList.addAll((Collection) ((Map) this.mPendingIncomingRoomKeyRequests.get(str)).get(str2));
            }
        }
        return arrayList;
    }

    private void addIncomingRoomKeyRequest(IncomingRoomKeyRequest incomingRoomKeyRequest) {
        String str = incomingRoomKeyRequest.mUserId;
        String str2 = incomingRoomKeyRequest.mDeviceId;
        if (!this.mPendingIncomingRoomKeyRequests.containsKey(str)) {
            this.mPendingIncomingRoomKeyRequests.put(str, new HashMap());
        }
        if (!((Map) this.mPendingIncomingRoomKeyRequests.get(str)).containsKey(str2)) {
            ((Map) this.mPendingIncomingRoomKeyRequests.get(str)).put(str2, new ArrayList());
        }
        ((List) ((Map) this.mPendingIncomingRoomKeyRequests.get(str)).get(str2)).add(incomingRoomKeyRequest);
    }

    public void storeIncomingRoomKeyRequest(IncomingRoomKeyRequest incomingRoomKeyRequest) {
        loadIncomingRoomKeyRequests();
        if (isValidIncomingRoomKeyRequest(incomingRoomKeyRequest) && getIncomingRoomKeyRequest(incomingRoomKeyRequest.mUserId, incomingRoomKeyRequest.mDeviceId, incomingRoomKeyRequest.mRequestId) == null) {
            addIncomingRoomKeyRequest(incomingRoomKeyRequest);
            saveIncomingRoomKeyRequests();
        }
    }

    public void deleteIncomingRoomKeyRequest(IncomingRoomKeyRequest incomingRoomKeyRequest) {
        loadIncomingRoomKeyRequests();
        if (isValidIncomingRoomKeyRequest(incomingRoomKeyRequest)) {
            IncomingRoomKeyRequest incomingRoomKeyRequest2 = getIncomingRoomKeyRequest(incomingRoomKeyRequest.mUserId, incomingRoomKeyRequest.mDeviceId, incomingRoomKeyRequest.mRequestId);
            if (incomingRoomKeyRequest2 != null) {
                String str = incomingRoomKeyRequest.mUserId;
                String str2 = incomingRoomKeyRequest.mDeviceId;
                ((List) ((Map) this.mPendingIncomingRoomKeyRequests.get(str)).get(str2)).remove(incomingRoomKeyRequest2);
                if (((List) ((Map) this.mPendingIncomingRoomKeyRequests.get(str)).get(str2)).isEmpty()) {
                    ((Map) this.mPendingIncomingRoomKeyRequests.get(str)).remove(str2);
                }
                if (((Map) this.mPendingIncomingRoomKeyRequests.get(str)).isEmpty()) {
                    this.mPendingIncomingRoomKeyRequests.remove(str);
                }
                saveIncomingRoomKeyRequests();
            }
        }
    }

    private void saveIncomingRoomKeyRequests() {
        if (this.mIncomingRoomKeyRequestsFileTmp.exists()) {
            this.mIncomingRoomKeyRequestsFileTmp.delete();
        }
        if (this.mIncomingRoomKeyRequestsFile.exists()) {
            this.mIncomingRoomKeyRequestsFile.renameTo(this.mIncomingRoomKeyRequestsFileTmp);
        }
        if (storeObject(getPendingIncomingRoomKeyRequests(), this.mIncomingRoomKeyRequestsFile, "savedIncomingRoomKeyRequests - in background")) {
            if (this.mIncomingRoomKeyRequestsFileTmp.exists()) {
                this.mIncomingRoomKeyRequestsFileTmp.delete();
            }
        } else if (this.mIncomingRoomKeyRequestsFileTmp.exists()) {
            this.mIncomingRoomKeyRequestsFileTmp.renameTo(this.mIncomingRoomKeyRequestsFile);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0043 A[LOOP:0: B:14:0x003d->B:16:0x0043, LOOP_END] */
    private void loadIncomingRoomKeyRequests() {
        Object obj;
        List<IncomingRoomKeyRequest> list;
        if (this.mPendingIncomingRoomKeyRequests == null) {
            if (this.mIncomingRoomKeyRequestsFileTmp.exists()) {
                obj = loadObject(this.mIncomingRoomKeyRequestsFileTmp, "loadIncomingRoomKeyRequests - tmp");
            } else {
                obj = loadObject(this.mIncomingRoomKeyRequestsFile, "loadIncomingRoomKeyRequests");
            }
            List arrayList = new ArrayList();
            if (obj != null) {
                try {
                    list = (List) obj;
                } catch (Exception unused) {
                    this.mIncomingRoomKeyRequestsFileTmp.delete();
                    this.mIncomingRoomKeyRequestsFile.delete();
                }
                this.mPendingIncomingRoomKeyRequests = new HashMap();
                for (IncomingRoomKeyRequest addIncomingRoomKeyRequest : list) {
                    addIncomingRoomKeyRequest(addIncomingRoomKeyRequest);
                }
            }
            list = arrayList;
            this.mPendingIncomingRoomKeyRequests = new HashMap();
            while (r0.hasNext()) {
            }
        }
    }

    public Map<String, String> getRoomsAlgorithms() {
        return this.mRoomsAlgorithms;
    }

    public MXUsersDevicesMap<MXDeviceInfo> getAllUsersDevices() {
        String[] list = this.mDevicesFolder.list();
        if (list != null) {
            for (String loadUserDevices : list) {
                loadUserDevices(loadUserDevices);
            }
        }
        return this.mUsersDevicesInfoMap;
    }

    public Map<Map<String, String>, OutgoingRoomKeyRequest> getOutgoingRoomKeyRequests() {
        return this.mOutgoingRoomKeyRequests;
    }

    public Map<String, Map<String, OlmSession>> getOlmSessions() {
        return this.mOlmSessions;
    }
}
