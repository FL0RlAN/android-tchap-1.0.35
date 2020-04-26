package org.matrix.androidsdk.crypto;

import android.os.Handler;
import com.google.gson.JsonElement;
import java.util.List;
import kotlin.Metadata;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.listeners.ProgressListener;
import org.matrix.androidsdk.crypto.cryptostore.IMXCryptoStore;
import org.matrix.androidsdk.crypto.data.ImportRoomKeysResult;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXEncryptEventContentResult;
import org.matrix.androidsdk.crypto.data.MXOlmSessionResult;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.crypto.interfaces.CryptoEvent;
import org.matrix.androidsdk.crypto.interfaces.CryptoRoom;
import org.matrix.androidsdk.crypto.interfaces.CryptoSyncResponse;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackup;
import org.matrix.androidsdk.crypto.model.crypto.RoomKeyRequestBody;
import org.matrix.androidsdk.crypto.rest.CryptoRestClient;
import org.matrix.androidsdk.crypto.verification.VerificationManager;
import org.matrix.androidsdk.network.NetworkConnectivityReceiver;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000Ø\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\t\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H&J\u0010\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u001aH&J$\u0010\u001b\u001a\u00020\u00152\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001d2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020!0 H&J\b\u0010\"\u001a\u00020\u0015H&J\u001a\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\b\u0010'\u001a\u0004\u0018\u00010\u001eH&J\u001a\u0010(\u001a\u0004\u0018\u00010\r2\u0006\u0010)\u001a\u00020\u001e2\u0006\u0010*\u001a\u00020\u001eH&J.\u0010+\u001a\u00020\u00152\u0006\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020\u001e2\u0006\u0010/\u001a\u0002002\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u0002010 H&J*\u00102\u001a\u00020\u00152\f\u00103\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001d2\u0012\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u000205040 H'J\u001e\u00106\u001a\u00020\u00152\u0006\u00107\u001a\u00020\u001e2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u0002080 H&J\b\u00109\u001a\u00020:H&J\b\u0010;\u001a\u00020<H&J(\u0010=\u001a\u00020\u00152\u0006\u0010>\u001a\u00020\u001e2\b\u0010?\u001a\u0004\u0018\u00010\u001e2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\r0 H&J\b\u0010@\u001a\u00020AH&J\u0010\u0010B\u001a\u00020C2\u0006\u0010>\u001a\u00020\u001eH&J\u0016\u0010D\u001a\u00020\u00152\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020E0 H&J\n\u0010F\u001a\u0004\u0018\u00010GH&J\b\u0010H\u001a\u00020<H&J\u0016\u0010I\u001a\b\u0012\u0004\u0012\u00020\r0J2\u0006\u0010>\u001a\u00020\u001eH&J0\u0010K\u001a\u00020\u00152\u0006\u0010L\u001a\u0002082\u0006\u00107\u001a\u00020\u001e2\b\u0010M\u001a\u0004\u0018\u00010N2\f\u0010O\u001a\b\u0012\u0004\u0012\u00020P0 H&J\b\u0010Q\u001a\u00020EH&J\u001e\u0010R\u001a\u00020\u00152\u0006\u0010S\u001a\u00020\u001e2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020E0 H&J\b\u0010T\u001a\u00020EH&J\b\u0010U\u001a\u00020EH&J \u0010V\u001a\u00020\u00152\u0006\u0010W\u001a\u00020X2\u0006\u0010Y\u001a\u00020\u001e2\u0006\u0010Z\u001a\u00020EH&J\u0010\u0010[\u001a\u00020\u00152\u0006\u0010%\u001a\u00020&H&J\u0010\u0010\\\u001a\u00020\u00152\u0006\u0010'\u001a\u00020\u001eH&J.\u0010]\u001a\u00020\u00152\u0006\u0010^\u001a\u00020C2\u0006\u0010?\u001a\u00020\u001e2\u0006\u0010>\u001a\u00020\u001e2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020!0 H&J&\u0010_\u001a\u00020\u00152\f\u0010`\u001a\b\u0012\u0004\u0012\u00020\r0\u001d2\u000e\u0010\u001f\u001a\n\u0012\u0004\u0012\u00020!\u0018\u00010 H&J\u001e\u0010a\u001a\u00020\u00152\u0006\u0010b\u001a\u00020E2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020!0 H&J\u0010\u0010c\u001a\u00020\u00152\u0006\u0010d\u001a\u00020eH&J\u001e\u0010f\u001a\u00020\u00152\u0006\u0010S\u001a\u00020\u001e2\f\u0010O\u001a\b\u0012\u0004\u0012\u00020!0 H&J\u001e\u0010g\u001a\u00020\u00152\u0006\u0010S\u001a\u00020\u001e2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020!0 H&J\u0010\u0010h\u001a\u00020\u00152\u0006\u0010i\u001a\u00020EH&J\u001e\u0010j\u001a\u00020\u00152\u0006\u0010k\u001a\u00020E2\f\u0010l\u001a\b\u0012\u0004\u0012\u00020!0 H&J\b\u0010m\u001a\u00020EH&R\u001a\u0010\u0002\u001a\u00020\u00038&X§\u0004¢\u0006\f\u0012\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0006\u0010\u0007R\u0012\u0010\b\u001a\u00020\tX¦\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0012\u0010\f\u001a\u00020\rX¦\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0012\u0010\u0010\u001a\u00020\u0011X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013¨\u0006n"}, d2 = {"Lorg/matrix/androidsdk/crypto/MXCrypto;", "", "cryptoStore", "Lorg/matrix/androidsdk/crypto/cryptostore/IMXCryptoStore;", "cryptoStore$annotations", "()V", "getCryptoStore", "()Lorg/matrix/androidsdk/crypto/cryptostore/IMXCryptoStore;", "keysBackup", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackup;", "getKeysBackup", "()Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackup;", "myDevice", "Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "getMyDevice", "()Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "shortCodeVerificationManager", "Lorg/matrix/androidsdk/crypto/verification/VerificationManager;", "getShortCodeVerificationManager", "()Lorg/matrix/androidsdk/crypto/verification/VerificationManager;", "addRoomKeysRequestListener", "", "listener", "Lorg/matrix/androidsdk/crypto/RoomKeysRequestListener;", "cancelRoomKeyRequest", "requestBody", "Lorg/matrix/androidsdk/crypto/model/crypto/RoomKeyRequestBody;", "checkUnknownDevices", "userIds", "", "", "callback", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Ljava/lang/Void;", "close", "decryptEvent", "Lorg/matrix/androidsdk/crypto/MXEventDecryptionResult;", "event", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoEvent;", "timelineId", "deviceWithIdentityKey", "senderKey", "algorithm", "encryptEventContent", "eventContent", "Lcom/google/gson/JsonElement;", "eventType", "room", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoRoom;", "Lorg/matrix/androidsdk/crypto/data/MXEncryptEventContentResult;", "ensureOlmSessionsForUsers", "users", "Lorg/matrix/androidsdk/crypto/data/MXUsersDevicesMap;", "Lorg/matrix/androidsdk/crypto/data/MXOlmSessionResult;", "exportRoomKeys", "password", "", "getCryptoRestClient", "Lorg/matrix/androidsdk/crypto/rest/CryptoRestClient;", "getDecryptingThreadHandler", "Landroid/os/Handler;", "getDeviceInfo", "userId", "deviceId", "getDeviceList", "Lorg/matrix/androidsdk/crypto/MXDeviceList;", "getDeviceTrackingStatus", "", "getGlobalBlacklistUnverifiedDevices", "", "getOlmDevice", "Lorg/matrix/androidsdk/crypto/MXOlmDevice;", "getUIHandler", "getUserDevices", "", "importRoomKeys", "roomKeysAsArray", "progressListener", "Lorg/matrix/androidsdk/core/listeners/ProgressListener;", "apiCallback", "Lorg/matrix/androidsdk/crypto/data/ImportRoomKeysResult;", "isCorrupted", "isRoomBlacklistUnverifiedDevices", "roomId", "isStarted", "isStarting", "onSyncCompleted", "syncResponse", "Lorg/matrix/androidsdk/crypto/interfaces/CryptoSyncResponse;", "fromToken", "isCatchingUp", "reRequestRoomKeyForEvent", "resetReplayAttackCheckInTimeline", "setDeviceVerification", "verificationStatus", "setDevicesKnown", "devices", "setGlobalBlacklistUnverifiedDevices", "block", "setNetworkConnectivityReceiver", "networkConnectivityReceiver", "Lorg/matrix/androidsdk/network/NetworkConnectivityReceiver;", "setRoomBlacklistUnverifiedDevices", "setRoomUnBlacklistUnverifiedDevices", "setWarnOnUnknownDevices", "warn", "start", "isInitialSync", "aCallback", "warnOnUnknownDevices", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: MXCrypto.kt */
public interface MXCrypto {

    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 13})
    /* compiled from: MXCrypto.kt */
    public static final class DefaultImpls {
        public static /* synthetic */ void cryptoStore$annotations() {
        }
    }

    void addRoomKeysRequestListener(RoomKeysRequestListener roomKeysRequestListener);

    void cancelRoomKeyRequest(RoomKeyRequestBody roomKeyRequestBody);

    void checkUnknownDevices(List<String> list, ApiCallback<Void> apiCallback);

    void close();

    MXEventDecryptionResult decryptEvent(CryptoEvent cryptoEvent, String str) throws MXDecryptionException;

    MXDeviceInfo deviceWithIdentityKey(String str, String str2);

    void encryptEventContent(JsonElement jsonElement, String str, CryptoRoom cryptoRoom, ApiCallback<MXEncryptEventContentResult> apiCallback);

    void ensureOlmSessionsForUsers(List<String> list, ApiCallback<MXUsersDevicesMap<MXOlmSessionResult>> apiCallback);

    void exportRoomKeys(String str, ApiCallback<byte[]> apiCallback);

    CryptoRestClient getCryptoRestClient();

    IMXCryptoStore getCryptoStore();

    Handler getDecryptingThreadHandler();

    void getDeviceInfo(String str, String str2, ApiCallback<MXDeviceInfo> apiCallback);

    MXDeviceList getDeviceList();

    int getDeviceTrackingStatus(String str);

    void getGlobalBlacklistUnverifiedDevices(ApiCallback<Boolean> apiCallback);

    KeysBackup getKeysBackup();

    MXDeviceInfo getMyDevice();

    MXOlmDevice getOlmDevice();

    VerificationManager getShortCodeVerificationManager();

    Handler getUIHandler();

    List<MXDeviceInfo> getUserDevices(String str);

    void importRoomKeys(byte[] bArr, String str, ProgressListener progressListener, ApiCallback<ImportRoomKeysResult> apiCallback);

    boolean isCorrupted();

    void isRoomBlacklistUnverifiedDevices(String str, ApiCallback<Boolean> apiCallback);

    boolean isStarted();

    boolean isStarting();

    void onSyncCompleted(CryptoSyncResponse cryptoSyncResponse, String str, boolean z);

    void reRequestRoomKeyForEvent(CryptoEvent cryptoEvent);

    void resetReplayAttackCheckInTimeline(String str);

    void setDeviceVerification(int i, String str, String str2, ApiCallback<Void> apiCallback);

    void setDevicesKnown(List<? extends MXDeviceInfo> list, ApiCallback<Void> apiCallback);

    void setGlobalBlacklistUnverifiedDevices(boolean z, ApiCallback<Void> apiCallback);

    void setNetworkConnectivityReceiver(NetworkConnectivityReceiver networkConnectivityReceiver);

    void setRoomBlacklistUnverifiedDevices(String str, ApiCallback<Void> apiCallback);

    void setRoomUnBlacklistUnverifiedDevices(String str, ApiCallback<Void> apiCallback);

    void setWarnOnUnknownDevices(boolean z);

    void start(boolean z, ApiCallback<Void> apiCallback);

    boolean warnOnUnknownDevices();
}
