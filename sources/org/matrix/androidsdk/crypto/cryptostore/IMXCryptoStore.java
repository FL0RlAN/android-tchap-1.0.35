package org.matrix.androidsdk.crypto.cryptostore;

import android.content.Context;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.matrix.androidsdk.crypto.IncomingRoomKeyRequest;
import org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest;
import org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest.RequestState;
import org.matrix.androidsdk.crypto.cryptostore.db.model.KeysBackupDataEntity;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2;
import org.matrix.androidsdk.crypto.data.MXOlmSession;
import org.matrix.androidsdk.crypto.model.crypto.RoomKeyRequestBody;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.matrix.olm.OlmAccount;

public interface IMXCryptoStore {
    void close();

    void deleteIncomingRoomKeyRequest(IncomingRoomKeyRequest incomingRoomKeyRequest);

    void deleteOutgoingRoomKeyRequest(String str);

    void deleteStore();

    MXDeviceInfo deviceWithIdentityKey(String str);

    OlmAccount getAccount();

    String getDeviceId();

    MXOlmSession getDeviceSession(String str, String str2);

    Set<String> getDeviceSessionIds(String str);

    int getDeviceTrackingStatus(String str, int i);

    Map<String, Integer> getDeviceTrackingStatuses();

    boolean getGlobalBlacklistUnverifiedDevices();

    MXOlmInboundGroupSession2 getInboundGroupSession(String str, String str2);

    List<MXOlmInboundGroupSession2> getInboundGroupSessions();

    IncomingRoomKeyRequest getIncomingRoomKeyRequest(String str, String str2, String str3);

    String getKeyBackupVersion();

    KeysBackupDataEntity getKeysBackupData();

    String getLastUsedSessionId(String str);

    OutgoingRoomKeyRequest getOrAddOutgoingRoomKeyRequest(OutgoingRoomKeyRequest outgoingRoomKeyRequest);

    OutgoingRoomKeyRequest getOutgoingRoomKeyRequest(RoomKeyRequestBody roomKeyRequestBody);

    OutgoingRoomKeyRequest getOutgoingRoomKeyRequestByState(Set<RequestState> set);

    List<IncomingRoomKeyRequest> getPendingIncomingRoomKeyRequests();

    String getRoomAlgorithm(String str);

    List<String> getRoomsListBlacklistUnverifiedDevices();

    MXDeviceInfo getUserDevice(String str, String str2);

    Map<String, MXDeviceInfo> getUserDevices(String str);

    boolean hasData();

    int inboundGroupSessionsCount(boolean z);

    List<MXOlmInboundGroupSession2> inboundGroupSessionsToBackup(int i);

    void initWithCredentials(Context context, Credentials credentials);

    boolean isCorrupted();

    void markBackupDoneForInboundGroupSessions(List<MXOlmInboundGroupSession2> list);

    void open();

    void removeInboundGroupSession(String str, String str2);

    void resetBackupMarkers();

    void saveDeviceTrackingStatuses(Map<String, Integer> map);

    void setGlobalBlacklistUnverifiedDevices(boolean z);

    void setKeyBackupVersion(String str);

    void setKeysBackupData(KeysBackupDataEntity keysBackupDataEntity);

    void setRoomsListBlacklistUnverifiedDevices(List<String> list);

    void storeAccount(OlmAccount olmAccount);

    void storeDeviceId(String str);

    void storeInboundGroupSessions(List<MXOlmInboundGroupSession2> list);

    void storeIncomingRoomKeyRequest(IncomingRoomKeyRequest incomingRoomKeyRequest);

    void storeRoomAlgorithm(String str, String str2);

    void storeSession(MXOlmSession mXOlmSession, String str);

    void storeUserDevice(String str, MXDeviceInfo mXDeviceInfo);

    void storeUserDevices(String str, Map<String, MXDeviceInfo> map);

    void updateOutgoingRoomKeyRequest(OutgoingRoomKeyRequest outgoingRoomKeyRequest);
}
