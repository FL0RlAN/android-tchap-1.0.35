package org.matrix.androidsdk.crypto.cryptostore.db;

import android.content.Context;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmList;
import io.realm.RealmQuery;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.IncomingRoomKeyRequest;
import org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest;
import org.matrix.androidsdk.crypto.cryptostore.MXFileCryptoStore;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoMetadataEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;
import org.matrix.androidsdk.crypto.cryptostore.db.model.DeviceInfoEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.IncomingRoomKeyRequestEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmInboundGroupSessionEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmInboundGroupSessionEntityKt;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmSessionEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmSessionEntityKt;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OutgoingRoomKeyRequestEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.UserEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.query.CryptoRoomEntityQueriesKt;
import org.matrix.androidsdk.crypto.cryptostore.db.query.DeviceInfoEntityQueriesKt;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.matrix.olm.OlmSession;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u0000\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0013\u0010\t\u001a\u00020\u00032\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0002J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u0018\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0018\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0018\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0018\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0018\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0018\u0010\u0019\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0018\u0010\u001a\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000e\u001a\u00020\u000fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001c"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/CryptoFileStoreImporter;", "Lio/realm/Realm$Transaction;", "enableFileEncryption", "", "context", "Landroid/content/Context;", "credentials", "Lorg/matrix/androidsdk/rest/model/login/Credentials;", "(ZLandroid/content/Context;Lorg/matrix/androidsdk/rest/model/login/Credentials;)V", "equals", "other", "", "execute", "", "realm", "Lio/realm/Realm;", "hashCode", "", "importInboundGroupSessions", "fileCryptoStore", "Lorg/matrix/androidsdk/crypto/cryptostore/MXFileCryptoStore;", "importIncomingRoomKeyRequests", "importMetaData", "importOlmSessions", "importOutgoingRoomKeyRequests", "importRooms", "importUsers", "Companion", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: CryptoFileStoreImporter.kt */
public final class CryptoFileStoreImporter implements Transaction {
    public static final Companion Companion = new Companion(null);
    private static final String LOG_TAG = "CryptoFileStoreImporter";
    private final Context context;
    private final Credentials credentials;
    private final boolean enableFileEncryption;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/CryptoFileStoreImporter$Companion;", "", "()V", "LOG_TAG", "", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: CryptoFileStoreImporter.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public int hashCode() {
        return 99;
    }

    public CryptoFileStoreImporter(boolean z, Context context2, Credentials credentials2) {
        Intrinsics.checkParameterIsNotNull(context2, "context");
        Intrinsics.checkParameterIsNotNull(credentials2, "credentials");
        this.enableFileEncryption = z;
        this.context = context2;
        this.credentials = credentials2;
    }

    public void execute(Realm realm) {
        Intrinsics.checkParameterIsNotNull(realm, "realm");
        MXFileCryptoStore mXFileCryptoStore = new MXFileCryptoStore(this.enableFileEncryption);
        mXFileCryptoStore.initWithCredentials(this.context, this.credentials);
        boolean hasData = mXFileCryptoStore.hasData();
        String str = LOG_TAG;
        if (hasData) {
            Log.d(str, "Importing data...");
            long currentTimeMillis = System.currentTimeMillis();
            mXFileCryptoStore.open();
            importMetaData(mXFileCryptoStore, realm);
            importRooms(mXFileCryptoStore, realm);
            importUsers(mXFileCryptoStore, realm);
            importOutgoingRoomKeyRequests(mXFileCryptoStore, realm);
            importIncomingRoomKeyRequests(mXFileCryptoStore, realm);
            importOlmSessions(mXFileCryptoStore, realm);
            importInboundGroupSessions(mXFileCryptoStore, realm);
            mXFileCryptoStore.close();
            StringBuilder sb = new StringBuilder();
            sb.append("Importing data done in ");
            sb.append(System.currentTimeMillis() - currentTimeMillis);
            sb.append("ms");
            Log.d(str, sb.toString());
        } else {
            Log.d(str, "No data to import");
        }
        mXFileCryptoStore.deleteStore();
    }

    private final void importMetaData(MXFileCryptoStore mXFileCryptoStore, Realm realm) {
        Log.d(LOG_TAG, "Importing metadata");
        CryptoMetadataEntity cryptoMetadataEntity = (CryptoMetadataEntity) realm.createObject(CryptoMetadataEntity.class, this.credentials.getUserId());
        cryptoMetadataEntity.setDeviceId(mXFileCryptoStore.getDeviceId());
        cryptoMetadataEntity.setBackupVersion(null);
        cryptoMetadataEntity.putOlmAccount(mXFileCryptoStore.getAccount());
        cryptoMetadataEntity.setGlobalBlacklistUnverifiedDevices(mXFileCryptoStore.getGlobalBlacklistUnverifiedDevices());
    }

    private final void importRooms(MXFileCryptoStore mXFileCryptoStore, Realm realm) {
        Map roomsAlgorithms = mXFileCryptoStore.getRoomsAlgorithms();
        String str = LOG_TAG;
        if (roomsAlgorithms != null) {
            Set<Entry> entrySet = roomsAlgorithms.entrySet();
            if (entrySet != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Importing ");
                sb.append(entrySet.size());
                sb.append(" rooms");
                Log.d(str, sb.toString());
                if (entrySet != null) {
                    for (Entry entry : entrySet) {
                        org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntity.Companion companion = CryptoRoomEntity.Companion;
                        Object key = entry.getKey();
                        Intrinsics.checkExpressionValueIsNotNull(key, "entry.key");
                        CryptoRoomEntityQueriesKt.getOrCreate(companion, realm, (String) key).setAlgorithm((String) entry.getValue());
                    }
                }
            }
        }
        List<String> roomsListBlacklistUnverifiedDevices = mXFileCryptoStore.getRoomsListBlacklistUnverifiedDevices();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Setting ");
        sb2.append(roomsListBlacklistUnverifiedDevices.size());
        sb2.append(" room blacklistUnverifiedDevices flags to true");
        Log.d(str, sb2.toString());
        Intrinsics.checkExpressionValueIsNotNull(roomsListBlacklistUnverifiedDevices, "fileCryptoStore.roomsLis… true\")\n                }");
        for (String str2 : roomsListBlacklistUnverifiedDevices) {
            RealmQuery where = realm.where(CryptoRoomEntity.class);
            Intrinsics.checkExpressionValueIsNotNull(where, "this.where(T::class.java)");
            CryptoRoomEntity cryptoRoomEntity = (CryptoRoomEntity) where.equalTo(CryptoRoomEntityFields.ROOM_ID, str2).findFirst();
            if (cryptoRoomEntity != null) {
                cryptoRoomEntity.setBlacklistUnverifiedDevices(true);
            }
        }
    }

    private final void importUsers(MXFileCryptoStore mXFileCryptoStore, Realm realm) {
        MXUsersDevicesMap allUsersDevices = mXFileCryptoStore.getAllUsersDevices();
        String str = "Importing ";
        String str2 = LOG_TAG;
        if (allUsersDevices != null) {
            Map map = allUsersDevices.getMap();
            if (map != null) {
                Set<Entry> entrySet = map.entrySet();
                if (entrySet != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(entrySet.size());
                    sb.append(" users");
                    Log.d(str2, sb.toString());
                    if (entrySet != null) {
                        for (Entry entry : entrySet) {
                            UserEntity userEntity = (UserEntity) realm.createObject(UserEntity.class, entry.getKey());
                            userEntity.setDevices(new RealmList());
                            for (Entry entry2 : ((Map) entry.getValue()).entrySet()) {
                                RealmList devices = userEntity.getDevices();
                                org.matrix.androidsdk.crypto.cryptostore.db.model.DeviceInfoEntity.Companion companion = DeviceInfoEntity.Companion;
                                Object key = entry.getKey();
                                Intrinsics.checkExpressionValueIsNotNull(key, "userIdToDevices.key");
                                String str3 = (String) key;
                                String str4 = ((MXDeviceInfo) entry2.getValue()).deviceId;
                                Intrinsics.checkExpressionValueIsNotNull(str4, "deviceIdToDevice.value.deviceId");
                                DeviceInfoEntity orCreate = DeviceInfoEntityQueriesKt.getOrCreate(companion, realm, str3, str4);
                                orCreate.setDeviceId((String) entry2.getKey());
                                orCreate.setIdentityKey(((MXDeviceInfo) entry2.getValue()).identityKey());
                                orCreate.putDeviceInfo((MXDeviceInfo) entry2.getValue());
                                devices.add(orCreate);
                            }
                        }
                    }
                }
            }
        }
        Map deviceTrackingStatuses = mXFileCryptoStore.getDeviceTrackingStatuses();
        if (deviceTrackingStatuses != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(deviceTrackingStatuses.size());
            sb2.append(" device tracking status");
            Log.d(str2, sb2.toString());
            if (deviceTrackingStatuses != null) {
                for (Entry entry3 : deviceTrackingStatuses.entrySet()) {
                    RealmQuery where = realm.where(UserEntity.class);
                    Intrinsics.checkExpressionValueIsNotNull(where, "this.where(T::class.java)");
                    UserEntity userEntity2 = (UserEntity) where.equalTo("userId", (String) entry3.getKey()).findFirst();
                    if (userEntity2 != null) {
                        Object value = entry3.getValue();
                        Intrinsics.checkExpressionValueIsNotNull(value, "entry.value");
                        userEntity2.setDeviceTrackingStatus(((Number) value).intValue());
                    }
                }
            }
        }
    }

    private final void importOutgoingRoomKeyRequests(MXFileCryptoStore mXFileCryptoStore, Realm realm) {
        Map outgoingRoomKeyRequests = mXFileCryptoStore.getOutgoingRoomKeyRequests();
        if (outgoingRoomKeyRequests != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Importing ");
            sb.append(outgoingRoomKeyRequests.size());
            sb.append(" OutgoingRoomKeyRequests");
            Log.d(LOG_TAG, sb.toString());
            if (outgoingRoomKeyRequests != null) {
                for (Entry entry : outgoingRoomKeyRequests.entrySet()) {
                    OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity = (OutgoingRoomKeyRequestEntity) realm.createObject(OutgoingRoomKeyRequestEntity.class, ((OutgoingRoomKeyRequest) entry.getValue()).mRequestId);
                    outgoingRoomKeyRequestEntity.putRecipients(((OutgoingRoomKeyRequest) entry.getValue()).mRecipients);
                    outgoingRoomKeyRequestEntity.putRequestBody(((OutgoingRoomKeyRequest) entry.getValue()).mRequestBody);
                    outgoingRoomKeyRequestEntity.setState(((OutgoingRoomKeyRequest) entry.getValue()).mState.ordinal());
                    outgoingRoomKeyRequestEntity.setCancellationTxnId(((OutgoingRoomKeyRequest) entry.getValue()).mCancellationTxnId);
                }
            }
        }
    }

    private final void importIncomingRoomKeyRequests(MXFileCryptoStore mXFileCryptoStore, Realm realm) {
        List<IncomingRoomKeyRequest> pendingIncomingRoomKeyRequests = mXFileCryptoStore.getPendingIncomingRoomKeyRequests();
        if (pendingIncomingRoomKeyRequests != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Importing ");
            sb.append(pendingIncomingRoomKeyRequests.size());
            sb.append(" IncomingRoomKeyRequests");
            Log.d(LOG_TAG, sb.toString());
            if (pendingIncomingRoomKeyRequests != null) {
                for (IncomingRoomKeyRequest incomingRoomKeyRequest : pendingIncomingRoomKeyRequests) {
                    IncomingRoomKeyRequestEntity incomingRoomKeyRequestEntity = (IncomingRoomKeyRequestEntity) realm.createObject(IncomingRoomKeyRequestEntity.class);
                    incomingRoomKeyRequestEntity.setRequestId(incomingRoomKeyRequest.mRequestId);
                    incomingRoomKeyRequestEntity.setUserId(incomingRoomKeyRequest.mUserId);
                    incomingRoomKeyRequestEntity.setDeviceId(incomingRoomKeyRequest.mDeviceId);
                    incomingRoomKeyRequestEntity.putRequestBody(incomingRoomKeyRequest.mRequestBody);
                }
            }
        }
    }

    private final void importOlmSessions(MXFileCryptoStore mXFileCryptoStore, Realm realm) {
        Map olmSessions = mXFileCryptoStore.getOlmSessions();
        if (olmSessions != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Importing ");
            sb.append(olmSessions.size());
            sb.append(" olmSessions");
            Log.d(LOG_TAG, sb.toString());
            if (olmSessions != null) {
                for (Entry entry : olmSessions.entrySet()) {
                    Object value = entry.getValue();
                    Intrinsics.checkExpressionValueIsNotNull(value, "deviceKeyToMap.value");
                    for (Entry entry2 : ((Map) value).entrySet()) {
                        org.matrix.androidsdk.crypto.cryptostore.db.model.OlmSessionEntity.Companion companion = OlmSessionEntity.Companion;
                        Object key = entry2.getKey();
                        Intrinsics.checkExpressionValueIsNotNull(key, "olmSessionIdToOlmSession.key");
                        String str = (String) key;
                        Object key2 = entry.getKey();
                        Intrinsics.checkExpressionValueIsNotNull(key2, "deviceKeyToMap.key");
                        OlmSessionEntity olmSessionEntity = (OlmSessionEntity) realm.createObject(OlmSessionEntity.class, OlmSessionEntityKt.createPrimaryKey(companion, str, (String) key2));
                        olmSessionEntity.setDeviceKey((String) entry.getKey());
                        olmSessionEntity.setSessionId((String) entry2.getKey());
                        olmSessionEntity.putOlmSession((OlmSession) entry2.getValue());
                        olmSessionEntity.setLastReceivedMessageTs(0);
                    }
                }
            }
        }
    }

    private final void importInboundGroupSessions(MXFileCryptoStore mXFileCryptoStore, Realm realm) {
        List<MXOlmInboundGroupSession2> inboundGroupSessions = mXFileCryptoStore.getInboundGroupSessions();
        StringBuilder sb = new StringBuilder();
        sb.append("Importing ");
        sb.append(inboundGroupSessions.size());
        sb.append(" InboundGroupSessions");
        Log.d(LOG_TAG, sb.toString());
        Intrinsics.checkExpressionValueIsNotNull(inboundGroupSessions, "fileCryptoStore.inboundG…sions\")\n                }");
        for (MXOlmInboundGroupSession2 mXOlmInboundGroupSession2 : inboundGroupSessions) {
            OlmInboundGroupSessionEntity olmInboundGroupSessionEntity = (OlmInboundGroupSessionEntity) realm.createObject(OlmInboundGroupSessionEntity.class, OlmInboundGroupSessionEntityKt.createPrimaryKey(OlmInboundGroupSessionEntity.Companion, mXOlmInboundGroupSession2.mSession.sessionIdentifier(), mXOlmInboundGroupSession2.mSenderKey));
            olmInboundGroupSessionEntity.setSessionId(mXOlmInboundGroupSession2.mSession.sessionIdentifier());
            olmInboundGroupSessionEntity.setSenderKey(mXOlmInboundGroupSession2.mSenderKey);
            olmInboundGroupSessionEntity.putInboundGroupSession(mXOlmInboundGroupSession2);
        }
    }

    public boolean equals(Object obj) {
        return obj instanceof CryptoFileStoreImporter;
    }
}
