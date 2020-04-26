package org.matrix.androidsdk.crypto.keysbackup;

import android.os.Handler;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs.CastExtraArgs;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.TypeCastException;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.core.JsonUtility;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SuccessCallback;
import org.matrix.androidsdk.core.callback.SuccessErrorCallback;
import org.matrix.androidsdk.core.listeners.ProgressListener;
import org.matrix.androidsdk.core.listeners.StepProgressListener;
import org.matrix.androidsdk.crypto.CryptoConstantsKt;
import org.matrix.androidsdk.crypto.MXOlmDevice;
import org.matrix.androidsdk.crypto.MegolmSessionData;
import org.matrix.androidsdk.crypto.cryptostore.IMXCryptoStore;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;
import org.matrix.androidsdk.crypto.cryptostore.db.model.KeysBackupDataEntity;
import org.matrix.androidsdk.crypto.data.ImportRoomKeysResult;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2;
import org.matrix.androidsdk.crypto.internal.MXCryptoImpl;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupState;
import org.matrix.androidsdk.crypto.keysbackup.KeysBackupStateManager.KeysBackupStateListener;
import org.matrix.androidsdk.crypto.model.keys.CreateKeysBackupVersionBody;
import org.matrix.androidsdk.crypto.model.keys.KeyBackupData;
import org.matrix.androidsdk.crypto.model.keys.KeysBackupData;
import org.matrix.androidsdk.crypto.model.keys.KeysVersion;
import org.matrix.androidsdk.crypto.model.keys.KeysVersionResult;
import org.matrix.androidsdk.crypto.rest.RoomKeysRestClient;
import org.matrix.androidsdk.crypto.util.RecoveryKeyKt;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;
import org.matrix.olm.OlmException;
import org.matrix.olm.OlmInboundGroupSession;
import org.matrix.olm.OlmPkDecryption;
import org.matrix.olm.OlmPkEncryption;
import org.matrix.olm.OlmPkMessage;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000Ð\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 p2\u00020\u0001:\u0001pB\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\u0015J\"\u0010(\u001a\u00020&2\b\u0010)\u001a\u0004\u0018\u00010*2\u0010\u0010+\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\t\u0018\u00010\bJ\b\u0010,\u001a\u00020&H\u0003J\u0006\u0010-\u001a\u00020\u000fJ\u0006\u0010.\u001a\u00020&J\u0012\u0010/\u001a\u00020&2\b\u00100\u001a\u0004\u0018\u00010\u0019H\u0002J\u001c\u00101\u001a\u00020&2\u0006\u00102\u001a\u0002032\f\u0010+\u001a\b\u0012\u0004\u0012\u0002040\bJ*\u00105\u001a\u0004\u0018\u0001062\u0006\u00107\u001a\u0002082\u0006\u00109\u001a\u00020\u000b2\u0006\u0010:\u001a\u00020\u000b2\u0006\u0010;\u001a\u00020<H\u0007J\u001e\u0010=\u001a\u00020&2\u0006\u0010>\u001a\u00020\u000b2\u000e\u0010+\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\bJ\u0010\u0010?\u001a\u00020&2\u0006\u0010@\u001a\u00020\u0019H\u0002J\u0010\u0010A\u001a\u0002082\u0006\u0010B\u001a\u00020CH\u0007J\u0014\u0010D\u001a\u00020&2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\u000f0\bJ\u000e\u0010E\u001a\u00020&2\u0006\u0010)\u001a\u00020*J\u0016\u0010F\u001a\u00020&2\u000e\u0010+\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00190\bJ2\u0010G\u001a\u00020&2\b\u00109\u001a\u0004\u0018\u00010\u000b2\b\u0010:\u001a\u0004\u0018\u00010\u000b2\u0006\u0010>\u001a\u00020\u000b2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020H0\bH\u0002J\u001c\u0010I\u001a\u00020&2\u0006\u0010J\u001a\u00020\u00192\f\u0010+\u001a\b\u0012\u0004\u0012\u00020L0KJ\u0010\u0010M\u001a\u00020L2\u0006\u0010J\u001a\u00020\u0019H\u0003J\u0012\u0010N\u001a\u0004\u0018\u00010O2\u0006\u0010P\u001a\u00020\u0019H\u0002J\u0006\u0010Q\u001a\u00020 J\u0006\u0010R\u001a\u00020SJ\u0006\u0010T\u001a\u00020SJ\u001e\u0010U\u001a\u00020&2\u0006\u0010>\u001a\u00020\u000b2\u000e\u0010+\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00190\bJ\u0018\u0010V\u001a\u00020\u000f2\u0006\u0010W\u001a\u00020\u000b2\u0006\u0010P\u001a\u00020\u0019H\u0003J\u0006\u0010X\u001a\u00020&J!\u0010Y\u001a\u00020&2\b\u0010Z\u001a\u0004\u0018\u00010S2\b\u0010[\u001a\u0004\u0018\u00010\u000bH\u0002¢\u0006\u0002\u0010\\J\u0012\u0010]\u001a\u0004\u0018\u00010<2\u0006\u0010W\u001a\u00020\u000bH\u0007J\u0012\u0010^\u001a\u0004\u0018\u00010\u000b2\u0006\u0010W\u001a\u00020\u000bH\u0003J(\u0010_\u001a\u00020&2\b\u0010`\u001a\u0004\u0018\u00010\u000b2\b\u0010)\u001a\u0004\u0018\u00010*2\f\u0010+\u001a\b\u0012\u0004\u0012\u0002030aJ$\u0010b\u001a\u0004\u0018\u00010\u000b2\u0006\u0010`\u001a\u00020\u000b2\u0006\u0010P\u001a\u00020\u00192\b\u0010)\u001a\u0004\u0018\u00010*H\u0003J\u000e\u0010c\u001a\u00020&2\u0006\u0010'\u001a\u00020\u0015J\b\u0010d\u001a\u00020&H\u0002J\b\u0010e\u001a\u00020&H\u0002JB\u0010f\u001a\u00020&2\u0006\u0010J\u001a\u00020\u00192\u0006\u0010`\u001a\u00020\u000b2\b\u0010:\u001a\u0004\u0018\u00010\u000b2\b\u00109\u001a\u0004\u0018\u00010\u000b2\b\u0010g\u001a\u0004\u0018\u00010h2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020i0\bJB\u0010j\u001a\u00020&2\u0006\u0010@\u001a\u00020\u00192\u0006\u0010W\u001a\u00020\u000b2\b\u0010:\u001a\u0004\u0018\u00010\u000b2\b\u00109\u001a\u0004\u0018\u00010\u000b2\b\u0010g\u001a\u0004\u0018\u00010h2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020i0\bJ\b\u0010k\u001a\u00020\u000bH\u0016J$\u0010l\u001a\u00020&2\u0006\u0010J\u001a\u00020\u00192\u0006\u0010m\u001a\u00020\u000f2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\t0\bJ$\u0010n\u001a\u00020&2\u0006\u0010J\u001a\u00020\u00192\u0006\u0010`\u001a\u00020\u000b2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\t0\bJ$\u0010o\u001a\u00020&2\u0006\u0010J\u001a\u00020\u00192\u0006\u0010W\u001a\u00020\u000b2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\t0\bR\u0018\u0010\u0007\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\t\u0018\u00010\bX\u000e¢\u0006\u0002\n\u0000R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b8F¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u000f8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\u000f8F¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0010R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\"\u0010\u001a\u001a\u0004\u0018\u00010\u00192\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u000e\u0010\u001d\u001a\u00020\u001eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010!\u001a\u00020\"8F¢\u0006\u0006\u001a\u0004\b#\u0010$¨\u0006q"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackup;", "", "mCrypto", "Lorg/matrix/androidsdk/crypto/internal/MXCryptoImpl;", "homeServerConnectionConfig", "Lorg/matrix/androidsdk/HomeServerConnectionConfig;", "(Lorg/matrix/androidsdk/crypto/internal/MXCryptoImpl;Lorg/matrix/androidsdk/HomeServerConnectionConfig;)V", "backupAllGroupSessionsCallback", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Ljava/lang/Void;", "currentBackupVersion", "", "getCurrentBackupVersion", "()Ljava/lang/String;", "isEnabled", "", "()Z", "isStucked", "mBackupKey", "Lorg/matrix/olm/OlmPkEncryption;", "mKeysBackupStateListener", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupStateListener;", "mKeysBackupStateManager", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager;", "<set-?>", "Lorg/matrix/androidsdk/crypto/model/keys/KeysVersionResult;", "mKeysBackupVersion", "getMKeysBackupVersion", "()Lorg/matrix/androidsdk/crypto/model/keys/KeysVersionResult;", "mRandom", "Ljava/util/Random;", "mRoomKeysRestClient", "Lorg/matrix/androidsdk/crypto/rest/RoomKeysRestClient;", "state", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupState;", "getState", "()Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupStateManager$KeysBackupState;", "addListener", "", "listener", "backupAllGroupSessions", "progressListener", "Lorg/matrix/androidsdk/core/listeners/ProgressListener;", "callback", "backupKeys", "canRestoreKeys", "checkAndStartKeysBackup", "checkAndStartWithKeysBackupVersion", "keyBackupVersion", "createKeysBackupVersion", "keysBackupCreationInfo", "Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupCreationInfo;", "Lorg/matrix/androidsdk/crypto/model/keys/KeysVersion;", "decryptKeyBackupData", "Lorg/matrix/androidsdk/crypto/MegolmSessionData;", "keyBackupData", "Lorg/matrix/androidsdk/crypto/model/keys/KeyBackupData;", "sessionId", "roomId", "decryption", "Lorg/matrix/olm/OlmPkDecryption;", "deleteBackup", "version", "enableKeysBackup", "keysVersionResult", "encryptGroupSession", "session", "Lorg/matrix/androidsdk/crypto/data/MXOlmInboundGroupSession2;", "forceUsingLastVersion", "getBackupProgress", "getCurrentVersion", "getKeys", "Lorg/matrix/androidsdk/crypto/model/keys/KeysBackupData;", "getKeysBackupTrust", "keysBackupVersion", "Lorg/matrix/androidsdk/core/callback/SuccessCallback;", "Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackupVersionTrust;", "getKeysBackupTrustBg", "getMegolmBackupAuthData", "Lorg/matrix/androidsdk/crypto/keysbackup/MegolmBackupAuthData;", "keysBackupData", "getRoomKeysRestClient", "getTotalNumbersOfBackedUpKeys", "", "getTotalNumbersOfKeys", "getVersion", "isValidRecoveryKeyForKeysBackupVersion", "recoveryKey", "maybeBackupKeys", "onServerDataRetrieved", "count", "hash", "(Ljava/lang/Integer;Ljava/lang/String;)V", "pkDecryptionFromRecoveryKey", "pkPublicKeyFromRecoveryKey", "prepareKeysBackupVersion", "password", "Lorg/matrix/androidsdk/core/callback/SuccessErrorCallback;", "recoveryKeyFromPassword", "removeListener", "resetBackupAllGroupSessionsListeners", "resetKeysBackupData", "restoreKeyBackupWithPassword", "stepProgressListener", "Lorg/matrix/androidsdk/core/listeners/StepProgressListener;", "Lorg/matrix/androidsdk/crypto/data/ImportRoomKeysResult;", "restoreKeysWithRecoveryKey", "toString", "trustKeysBackupVersion", "trust", "trustKeysBackupVersionWithPassphrase", "trustKeysBackupVersionWithRecoveryKey", "Companion", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackup.kt */
public final class KeysBackup {
    public static final Companion Companion = new Companion(null);
    private static final int KEY_BACKUP_SEND_KEYS_MAX_COUNT = 100;
    private static final int KEY_BACKUP_WAITING_TIME_TO_SEND_KEY_BACKUP_MILLIS = 10000;
    /* access modifiers changed from: private */
    public static final String LOG_TAG = KeysBackup.class.getSimpleName();
    /* access modifiers changed from: private */
    public ApiCallback<Void> backupAllGroupSessionsCallback;
    private OlmPkEncryption mBackupKey;
    /* access modifiers changed from: private */
    public final MXCryptoImpl mCrypto;
    /* access modifiers changed from: private */
    public KeysBackupStateListener mKeysBackupStateListener;
    /* access modifiers changed from: private */
    public final KeysBackupStateManager mKeysBackupStateManager = new KeysBackupStateManager(this.mCrypto);
    /* access modifiers changed from: private */
    public KeysVersionResult mKeysBackupVersion;
    private final Random mRandom = new Random();
    /* access modifiers changed from: private */
    public final RoomKeysRestClient mRoomKeysRestClient;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \b*\u0004\u0018\u00010\u00070\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lorg/matrix/androidsdk/crypto/keysbackup/KeysBackup$Companion;", "", "()V", "KEY_BACKUP_SEND_KEYS_MAX_COUNT", "", "KEY_BACKUP_WAITING_TIME_TO_SEND_KEY_BACKUP_MILLIS", "LOG_TAG", "", "kotlin.jvm.PlatformType", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: KeysBackup.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public KeysBackup(MXCryptoImpl mXCryptoImpl, HomeServerConnectionConfig homeServerConnectionConfig) {
        Intrinsics.checkParameterIsNotNull(mXCryptoImpl, "mCrypto");
        Intrinsics.checkParameterIsNotNull(homeServerConnectionConfig, "homeServerConnectionConfig");
        this.mCrypto = mXCryptoImpl;
        this.mRoomKeysRestClient = new RoomKeysRestClient(homeServerConnectionConfig);
    }

    public final KeysVersionResult getMKeysBackupVersion() {
        return this.mKeysBackupVersion;
    }

    public final boolean isEnabled() {
        return this.mKeysBackupStateManager.isEnabled();
    }

    public final boolean isStucked() {
        return this.mKeysBackupStateManager.isStucked();
    }

    public final KeysBackupState getState() {
        return this.mKeysBackupStateManager.getState();
    }

    public final String getCurrentBackupVersion() {
        KeysVersionResult keysVersionResult = this.mKeysBackupVersion;
        if (keysVersionResult != null) {
            return keysVersionResult.getVersion();
        }
        return null;
    }

    public final void addListener(KeysBackupStateListener keysBackupStateListener) {
        Intrinsics.checkParameterIsNotNull(keysBackupStateListener, CastExtraArgs.LISTENER);
        this.mKeysBackupStateManager.addListener(keysBackupStateListener);
    }

    public final void removeListener(KeysBackupStateListener keysBackupStateListener) {
        Intrinsics.checkParameterIsNotNull(keysBackupStateListener, CastExtraArgs.LISTENER);
        this.mKeysBackupStateManager.removeListener(keysBackupStateListener);
    }

    public final void prepareKeysBackupVersion(String str, ProgressListener progressListener, SuccessErrorCallback<MegolmBackupCreationInfo> successErrorCallback) {
        Intrinsics.checkParameterIsNotNull(successErrorCallback, "callback");
        this.mCrypto.getDecryptingThreadHandler().post(new KeysBackup$prepareKeysBackupVersion$1(this, str, progressListener, successErrorCallback));
    }

    public final void createKeysBackupVersion(MegolmBackupCreationInfo megolmBackupCreationInfo, ApiCallback<KeysVersion> apiCallback) {
        Intrinsics.checkParameterIsNotNull(megolmBackupCreationInfo, "keysBackupCreationInfo");
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        CreateKeysBackupVersionBody createKeysBackupVersionBody = new CreateKeysBackupVersionBody();
        createKeysBackupVersionBody.setAlgorithm(megolmBackupCreationInfo.getAlgorithm());
        createKeysBackupVersionBody.setAuthData(JsonUtility.getBasicGson().toJsonTree(megolmBackupCreationInfo.getAuthData()));
        this.mKeysBackupStateManager.setState(KeysBackupState.Enabling);
        this.mRoomKeysRestClient.createKeysBackupVersion(createKeysBackupVersionBody, new KeysBackup$createKeysBackupVersion$1(this, createKeysBackupVersionBody, apiCallback));
    }

    public final void deleteBackup(String str, ApiCallback<Void> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, TermsResponse.VERSION);
        this.mCrypto.getDecryptingThreadHandler().post(new KeysBackup$deleteBackup$1(this, str, apiCallback));
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0029  */
    /* JADX WARNING: Removed duplicated region for block: B:12:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0023  */
    public final boolean canRestoreKeys() {
        int i;
        int totalNumbersOfKeys = getTotalNumbersOfKeys();
        IMXCryptoStore cryptoStore = this.mCrypto.getCryptoStore();
        Intrinsics.checkExpressionValueIsNotNull(cryptoStore, "mCrypto.cryptoStore");
        KeysBackupDataEntity keysBackupData = cryptoStore.getKeysBackupData();
        if (keysBackupData != null) {
            Integer backupLastServerNumberOfKeys = keysBackupData.getBackupLastServerNumberOfKeys();
            if (backupLastServerNumberOfKeys != null) {
                i = backupLastServerNumberOfKeys.intValue();
                if (keysBackupData != null) {
                    keysBackupData.getBackupLastServerHash();
                }
                return totalNumbersOfKeys >= i;
            }
        }
        i = -1;
        if (keysBackupData != null) {
        }
        if (totalNumbersOfKeys >= i) {
        }
    }

    public final int getTotalNumbersOfKeys() {
        return this.mCrypto.getCryptoStore().inboundGroupSessionsCount(false);
    }

    public final int getTotalNumbersOfBackedUpKeys() {
        return this.mCrypto.getCryptoStore().inboundGroupSessionsCount(true);
    }

    public final void backupAllGroupSessions(ProgressListener progressListener, ApiCallback<Void> apiCallback) {
        getBackupProgress(new KeysBackup$backupAllGroupSessions$1(this, progressListener, apiCallback));
    }

    public final void getKeysBackupTrust(KeysVersionResult keysVersionResult, SuccessCallback<KeysBackupVersionTrust> successCallback) {
        Intrinsics.checkParameterIsNotNull(keysVersionResult, "keysBackupVersion");
        Intrinsics.checkParameterIsNotNull(successCallback, "callback");
        this.mCrypto.getDecryptingThreadHandler().post(new KeysBackup$getKeysBackupTrust$1(this, keysVersionResult, successCallback));
    }

    /* access modifiers changed from: private */
    public final KeysBackupVersionTrust getKeysBackupTrustBg(KeysVersionResult keysVersionResult) {
        boolean z;
        String str = this.mCrypto.getMyDevice().userId;
        KeysBackupVersionTrust keysBackupVersionTrust = new KeysBackupVersionTrust();
        MegolmBackupAuthData authDataAsMegolmBackupAuthData = keysVersionResult.getAuthDataAsMegolmBackupAuthData();
        if (!(keysVersionResult.getAlgorithm() == null || authDataAsMegolmBackupAuthData == null)) {
            if (!(authDataAsMegolmBackupAuthData.getPublicKey().length() == 0)) {
                Map signatures = authDataAsMegolmBackupAuthData.getSignatures();
                if (!(signatures == null || signatures.isEmpty())) {
                    Map signatures2 = authDataAsMegolmBackupAuthData.getSignatures();
                    if (signatures2 == null) {
                        Intrinsics.throwNpe();
                    }
                    Object obj = signatures2.get(str);
                    if (obj != null) {
                        Map map = (Map) obj;
                        if (map.isEmpty()) {
                            Log.d(LOG_TAG, "getKeysBackupTrust: Ignoring key backup because it lacks any signatures from this user");
                            return keysBackupVersionTrust;
                        }
                        for (String str2 : map.keySet()) {
                            String str3 = null;
                            List split$default = StringsKt.split$default((CharSequence) str2, new String[]{":"}, false, 0, 6, (Object) null);
                            if (split$default.size() == 2) {
                                str3 = (String) split$default.get(1);
                            }
                            MXDeviceInfo mXDeviceInfo = null;
                            if (str3 != null) {
                                MXDeviceInfo userDevice = this.mCrypto.getCryptoStore().getUserDevice(str3, str);
                                if (userDevice == null) {
                                    String str4 = LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("getKeysBackupTrust: Signature from unknown device ");
                                    sb.append(str3);
                                    Log.d(str4, sb.toString());
                                    z = false;
                                } else {
                                    MXOlmDevice olmDevice = this.mCrypto.getOlmDevice();
                                    if (olmDevice != null) {
                                        try {
                                            String fingerprint = userDevice.fingerprint();
                                            Map signalableJSONDictionary = authDataAsMegolmBackupAuthData.signalableJSONDictionary();
                                            Object obj2 = map.get(str2);
                                            if (obj2 != null) {
                                                olmDevice.verifySignature(fingerprint, signalableJSONDictionary, (String) obj2);
                                                z = true;
                                                if (z && userDevice.isVerified()) {
                                                    keysBackupVersionTrust.setUsable(true);
                                                }
                                            } else {
                                                throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
                                            }
                                        } catch (OlmException e) {
                                            String str5 = LOG_TAG;
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append("getKeysBackupTrust: Bad signature from device ");
                                            sb2.append(userDevice.deviceId);
                                            sb2.append(" ");
                                            sb2.append(e.getLocalizedMessage());
                                            Log.d(str5, sb2.toString());
                                        }
                                    }
                                    z = false;
                                    keysBackupVersionTrust.setUsable(true);
                                }
                                KeysBackupVersionTrustSignature keysBackupVersionTrustSignature = new KeysBackupVersionTrustSignature();
                                keysBackupVersionTrustSignature.setDevice(userDevice);
                                keysBackupVersionTrustSignature.setValid(z);
                                keysBackupVersionTrustSignature.setDeviceId(str3);
                                keysBackupVersionTrust.getSignatures().add(keysBackupVersionTrustSignature);
                            }
                        }
                        return keysBackupVersionTrust;
                    }
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.Map<kotlin.String, *>");
                }
            }
        }
        Log.d(LOG_TAG, "getKeysBackupTrust: Key backup is absent or missing required data");
        return keysBackupVersionTrust;
    }

    public final void trustKeysBackupVersion(KeysVersionResult keysVersionResult, boolean z, ApiCallback<Void> apiCallback) {
        Intrinsics.checkParameterIsNotNull(keysVersionResult, "keysBackupVersion");
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("trustKeyBackupVersion: ");
        sb.append(z);
        sb.append(", version ");
        sb.append(keysVersionResult.getVersion());
        Log.d(str, sb.toString());
        this.mCrypto.getDecryptingThreadHandler().post(new KeysBackup$trustKeysBackupVersion$1(this, keysVersionResult, apiCallback, z));
    }

    public final void trustKeysBackupVersionWithRecoveryKey(KeysVersionResult keysVersionResult, String str, ApiCallback<Void> apiCallback) {
        Intrinsics.checkParameterIsNotNull(keysVersionResult, "keysBackupVersion");
        Intrinsics.checkParameterIsNotNull(str, "recoveryKey");
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("trustKeysBackupVersionWithRecoveryKey: version ");
        sb.append(keysVersionResult.getVersion());
        Log.d(str2, sb.toString());
        this.mCrypto.getDecryptingThreadHandler().post(new KeysBackup$trustKeysBackupVersionWithRecoveryKey$1(this, str, keysVersionResult, apiCallback));
    }

    public final void trustKeysBackupVersionWithPassphrase(KeysVersionResult keysVersionResult, String str, ApiCallback<Void> apiCallback) {
        Intrinsics.checkParameterIsNotNull(keysVersionResult, "keysBackupVersion");
        Intrinsics.checkParameterIsNotNull(str, "password");
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("trustKeysBackupVersionWithPassphrase: version ");
        sb.append(keysVersionResult.getVersion());
        Log.d(str2, sb.toString());
        this.mCrypto.getDecryptingThreadHandler().post(new KeysBackup$trustKeysBackupVersionWithPassphrase$1(this, str, keysVersionResult, apiCallback));
    }

    private final String pkPublicKeyFromRecoveryKey(String str) {
        byte[] extractCurveKeyFromRecoveryKey = RecoveryKeyKt.extractCurveKeyFromRecoveryKey(str);
        if (extractCurveKeyFromRecoveryKey == null) {
            Log.w(LOG_TAG, "pkPublicKeyFromRecoveryKey: private key is null");
            return null;
        }
        try {
            String privateKey = new OlmPkDecryption().setPrivateKey(extractCurveKeyFromRecoveryKey);
            Intrinsics.checkExpressionValueIsNotNull(privateKey, "decryption.setPrivateKey(privateKey)");
            return privateKey;
        } catch (OlmException unused) {
            return null;
        }
    }

    /* access modifiers changed from: private */
    public final void resetBackupAllGroupSessionsListeners() {
        this.backupAllGroupSessionsCallback = null;
        KeysBackupStateListener keysBackupStateListener = this.mKeysBackupStateListener;
        if (keysBackupStateListener != null) {
            this.mKeysBackupStateManager.removeListener(keysBackupStateListener);
        }
        this.mKeysBackupStateListener = null;
    }

    public final void getBackupProgress(ProgressListener progressListener) {
        Intrinsics.checkParameterIsNotNull(progressListener, "progressListener");
        this.mCrypto.getDecryptingThreadHandler().post(new KeysBackup$getBackupProgress$1(this, progressListener));
    }

    public final void restoreKeysWithRecoveryKey(KeysVersionResult keysVersionResult, String str, String str2, String str3, StepProgressListener stepProgressListener, ApiCallback<ImportRoomKeysResult> apiCallback) {
        KeysVersionResult keysVersionResult2 = keysVersionResult;
        Intrinsics.checkParameterIsNotNull(keysVersionResult, "keysVersionResult");
        String str4 = str;
        Intrinsics.checkParameterIsNotNull(str, "recoveryKey");
        ApiCallback<ImportRoomKeysResult> apiCallback2 = apiCallback;
        Intrinsics.checkParameterIsNotNull(apiCallback2, "callback");
        String str5 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("restoreKeysWithRecoveryKey: From backup version: ");
        sb.append(keysVersionResult.getVersion());
        Log.d(str5, sb.toString());
        Handler decryptingThreadHandler = this.mCrypto.getDecryptingThreadHandler();
        KeysBackup$restoreKeysWithRecoveryKey$1 keysBackup$restoreKeysWithRecoveryKey$1 = new KeysBackup$restoreKeysWithRecoveryKey$1(this, str4, keysVersionResult2, apiCallback2, stepProgressListener, str3, str2);
        decryptingThreadHandler.post(keysBackup$restoreKeysWithRecoveryKey$1);
    }

    public final void restoreKeyBackupWithPassword(KeysVersionResult keysVersionResult, String str, String str2, String str3, StepProgressListener stepProgressListener, ApiCallback<ImportRoomKeysResult> apiCallback) {
        KeysVersionResult keysVersionResult2 = keysVersionResult;
        Intrinsics.checkParameterIsNotNull(keysVersionResult, "keysBackupVersion");
        String str4 = str;
        Intrinsics.checkParameterIsNotNull(str, "password");
        ApiCallback<ImportRoomKeysResult> apiCallback2 = apiCallback;
        Intrinsics.checkParameterIsNotNull(apiCallback2, "callback");
        String str5 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("[MXKeyBackup] restoreKeyBackup with password: From backup version: ");
        sb.append(keysVersionResult.getVersion());
        Log.d(str5, sb.toString());
        Handler decryptingThreadHandler = this.mCrypto.getDecryptingThreadHandler();
        KeysBackup$restoreKeyBackupWithPassword$1 keysBackup$restoreKeyBackupWithPassword$1 = new KeysBackup$restoreKeyBackupWithPassword$1(this, stepProgressListener, str4, keysVersionResult2, apiCallback2, str2, str3);
        decryptingThreadHandler.post(keysBackup$restoreKeyBackupWithPassword$1);
    }

    /* access modifiers changed from: private */
    public final void getKeys(String str, String str2, String str3, ApiCallback<KeysBackupData> apiCallback) {
        if (str2 != null && str != null) {
            this.mRoomKeysRestClient.getRoomKey(str2, str, str3, new KeysBackup$getKeys$1(str, str2, apiCallback, apiCallback));
        } else if (str2 != null) {
            this.mRoomKeysRestClient.getRoomKeys(str2, str3, new KeysBackup$getKeys$2(str2, apiCallback, apiCallback));
        } else {
            this.mRoomKeysRestClient.getKeys(str3, apiCallback);
        }
    }

    public final OlmPkDecryption pkDecryptionFromRecoveryKey(String str) {
        Intrinsics.checkParameterIsNotNull(str, "recoveryKey");
        byte[] extractCurveKeyFromRecoveryKey = RecoveryKeyKt.extractCurveKeyFromRecoveryKey(str);
        OlmPkDecryption olmPkDecryption = null;
        if (extractCurveKeyFromRecoveryKey == null) {
            return olmPkDecryption;
        }
        try {
            OlmPkDecryption olmPkDecryption2 = new OlmPkDecryption();
            try {
                olmPkDecryption2.setPrivateKey(extractCurveKeyFromRecoveryKey);
                return olmPkDecryption2;
            } catch (OlmException e) {
                e = e;
                olmPkDecryption = olmPkDecryption2;
                Log.e(LOG_TAG, "OlmException", e);
                return olmPkDecryption;
            }
        } catch (OlmException e2) {
            e = e2;
            Log.e(LOG_TAG, "OlmException", e);
            return olmPkDecryption;
        }
    }

    public final void maybeBackupKeys() {
        if (isStucked()) {
            checkAndStartKeysBackup();
        } else if (getState() == KeysBackupState.ReadyToBackUp) {
            this.mKeysBackupStateManager.setState(KeysBackupState.WillBackUp);
            this.mCrypto.getUIHandler().postDelayed(new KeysBackup$maybeBackupKeys$1(this), (long) this.mRandom.nextInt(KEY_BACKUP_WAITING_TIME_TO_SEND_KEY_BACKUP_MILLIS));
        } else {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("maybeBackupKeys: Skip it because state: ");
            sb.append(getState());
            Log.d(str, sb.toString());
        }
    }

    public final void getVersion(String str, ApiCallback<KeysVersionResult> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, TermsResponse.VERSION);
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        this.mRoomKeysRestClient.getKeysBackupVersion(str, new KeysBackup$getVersion$1(apiCallback, apiCallback));
    }

    public final void getCurrentVersion(ApiCallback<KeysVersionResult> apiCallback) {
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        this.mRoomKeysRestClient.getKeysBackupLastVersion(new KeysBackup$getCurrentVersion$1(apiCallback, apiCallback));
    }

    public final void forceUsingLastVersion(ApiCallback<Boolean> apiCallback) {
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        getCurrentVersion(new KeysBackup$forceUsingLastVersion$1(this, apiCallback, apiCallback));
    }

    public final void checkAndStartKeysBackup() {
        if (!isStucked()) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("checkAndStartKeysBackup: invalid state: ");
            sb.append(getState());
            Log.w(str, sb.toString());
            return;
        }
        this.mKeysBackupVersion = null;
        this.mKeysBackupStateManager.setState(KeysBackupState.CheckingBackUpOnHomeserver);
        getCurrentVersion(new KeysBackup$checkAndStartKeysBackup$1(this));
    }

    /* access modifiers changed from: private */
    public final void checkAndStartWithKeysBackupVersion(KeysVersionResult keysVersionResult) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("checkAndStartWithKeyBackupVersion: ");
        sb.append(keysVersionResult != null ? keysVersionResult.getVersion() : null);
        Log.d(str, sb.toString());
        this.mKeysBackupVersion = keysVersionResult;
        if (keysVersionResult == null) {
            Log.d(LOG_TAG, "checkAndStartWithKeysBackupVersion: Found no key backup version on the homeserver");
            resetKeysBackupData();
            this.mKeysBackupStateManager.setState(KeysBackupState.Disabled);
            return;
        }
        getKeysBackupTrust(keysVersionResult, new KeysBackup$checkAndStartWithKeysBackupVersion$1(this, keysVersionResult));
    }

    /* access modifiers changed from: private */
    public final MegolmBackupAuthData getMegolmBackupAuthData(KeysVersionResult keysVersionResult) {
        CharSequence version = keysVersionResult.getVersion();
        if (!(version == null || StringsKt.isBlank(version)) && !(!Intrinsics.areEqual((Object) keysVersionResult.getAlgorithm(), (Object) CryptoConstantsKt.MXCRYPTO_ALGORITHM_MEGOLM_BACKUP)) && keysVersionResult.getAuthData() != null) {
            MegolmBackupAuthData authDataAsMegolmBackupAuthData = keysVersionResult.getAuthDataAsMegolmBackupAuthData();
            if (authDataAsMegolmBackupAuthData.getSignatures() == null || StringsKt.isBlank(authDataAsMegolmBackupAuthData.getPublicKey())) {
                return null;
            }
            return authDataAsMegolmBackupAuthData;
        }
        return null;
    }

    /* access modifiers changed from: private */
    public final String recoveryKeyFromPassword(String str, KeysVersionResult keysVersionResult, ProgressListener progressListener) {
        MegolmBackupAuthData megolmBackupAuthData = getMegolmBackupAuthData(keysVersionResult);
        if (megolmBackupAuthData == null) {
            Log.w(LOG_TAG, "recoveryKeyFromPassword: invalid parameter");
            return null;
        }
        CharSequence privateKeySalt = megolmBackupAuthData.getPrivateKeySalt();
        if ((privateKeySalt == null || StringsKt.isBlank(privateKeySalt)) || megolmBackupAuthData.getPrivateKeyIterations() == null) {
            Log.w(LOG_TAG, "recoveryKeyFromPassword: Salt and/or iterations not found in key backup auth data");
            return null;
        }
        String privateKeySalt2 = megolmBackupAuthData.getPrivateKeySalt();
        if (privateKeySalt2 == null) {
            Intrinsics.throwNpe();
        }
        Integer privateKeyIterations = megolmBackupAuthData.getPrivateKeyIterations();
        if (privateKeyIterations == null) {
            Intrinsics.throwNpe();
        }
        return RecoveryKeyKt.computeRecoveryKey(KeysBackupPasswordKt.retrievePrivateKeyWithPassword(str, privateKeySalt2, privateKeyIterations.intValue(), progressListener));
    }

    /* access modifiers changed from: private */
    public final boolean isValidRecoveryKeyForKeysBackupVersion(String str, KeysVersionResult keysVersionResult) {
        String pkPublicKeyFromRecoveryKey = pkPublicKeyFromRecoveryKey(str);
        if (pkPublicKeyFromRecoveryKey == null) {
            Log.w(LOG_TAG, "isValidRecoveryKeyForKeysBackupVersion: public key is null");
            return false;
        }
        MegolmBackupAuthData megolmBackupAuthData = getMegolmBackupAuthData(keysVersionResult);
        if (megolmBackupAuthData == null) {
            Log.w(LOG_TAG, "isValidRecoveryKeyForKeysBackupVersion: Key backup is missing required data");
            return false;
        } else if (!(!Intrinsics.areEqual((Object) pkPublicKeyFromRecoveryKey, (Object) megolmBackupAuthData.getPublicKey()))) {
            return true;
        } else {
            Log.w(LOG_TAG, "isValidRecoveryKeyForKeysBackupVersion: Public keys mismatch");
            return false;
        }
    }

    /* access modifiers changed from: private */
    public final void enableKeysBackup(KeysVersionResult keysVersionResult) {
        String str = "Invalid authentication data";
        if (keysVersionResult.getAuthData() != null) {
            MegolmBackupAuthData authDataAsMegolmBackupAuthData = keysVersionResult.getAuthDataAsMegolmBackupAuthData();
            if (authDataAsMegolmBackupAuthData != null) {
                this.mKeysBackupVersion = keysVersionResult;
                IMXCryptoStore cryptoStore = this.mCrypto.getCryptoStore();
                Intrinsics.checkExpressionValueIsNotNull(cryptoStore, "mCrypto.cryptoStore");
                cryptoStore.setKeyBackupVersion(keysVersionResult.getVersion());
                onServerDataRetrieved(keysVersionResult.getCount(), keysVersionResult.getHash());
                try {
                    OlmPkEncryption olmPkEncryption = new OlmPkEncryption();
                    olmPkEncryption.setRecipientKey(authDataAsMegolmBackupAuthData.getPublicKey());
                    this.mBackupKey = olmPkEncryption;
                    this.mKeysBackupStateManager.setState(KeysBackupState.ReadyToBackUp);
                    maybeBackupKeys();
                } catch (OlmException e) {
                    Log.e(LOG_TAG, "OlmException", e);
                    this.mKeysBackupStateManager.setState(KeysBackupState.Disabled);
                }
            } else {
                Log.e(LOG_TAG, str);
                this.mKeysBackupStateManager.setState(KeysBackupState.Disabled);
            }
        } else {
            Log.e(LOG_TAG, str);
            this.mKeysBackupStateManager.setState(KeysBackupState.Disabled);
        }
    }

    /* access modifiers changed from: private */
    public final void onServerDataRetrieved(Integer num, String str) {
        IMXCryptoStore cryptoStore = this.mCrypto.getCryptoStore();
        Intrinsics.checkExpressionValueIsNotNull(cryptoStore, "mCrypto.cryptoStore");
        KeysBackupDataEntity keysBackupDataEntity = new KeysBackupDataEntity(0, null, null, 7, null);
        keysBackupDataEntity.setBackupLastServerNumberOfKeys(num);
        keysBackupDataEntity.setBackupLastServerHash(str);
        cryptoStore.setKeysBackupData(keysBackupDataEntity);
    }

    /* access modifiers changed from: private */
    public final void resetKeysBackupData() {
        resetBackupAllGroupSessionsListeners();
        IMXCryptoStore cryptoStore = this.mCrypto.getCryptoStore();
        String str = "mCrypto.cryptoStore";
        Intrinsics.checkExpressionValueIsNotNull(cryptoStore, str);
        cryptoStore.setKeyBackupVersion(null);
        IMXCryptoStore cryptoStore2 = this.mCrypto.getCryptoStore();
        Intrinsics.checkExpressionValueIsNotNull(cryptoStore2, str);
        cryptoStore2.setKeysBackupData(null);
        this.mBackupKey = null;
        this.mCrypto.getCryptoStore().resetBackupMarkers();
    }

    /* access modifiers changed from: private */
    public final void backupKeys() {
        Log.d(LOG_TAG, "backupKeys");
        if (!isEnabled() || this.mBackupKey == null || this.mKeysBackupVersion == null) {
            Log.d(LOG_TAG, "backupKeys: Invalid configuration");
            ApiCallback<Void> apiCallback = this.backupAllGroupSessionsCallback;
            if (apiCallback != null) {
                apiCallback.onUnexpectedError(new IllegalStateException("Invalid configuration"));
            }
            resetBackupAllGroupSessionsListeners();
        } else if (getState() == KeysBackupState.BackingUp) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("backupKeys: Invalid state: ");
            sb.append(getState());
            Log.d(str, sb.toString());
        } else {
            List inboundGroupSessionsToBackup = this.mCrypto.getCryptoStore().inboundGroupSessionsToBackup(100);
            String str2 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("backupKeys: 1 - ");
            sb2.append(inboundGroupSessionsToBackup.size());
            sb2.append(" sessions to back up");
            Log.d(str2, sb2.toString());
            if (inboundGroupSessionsToBackup.isEmpty()) {
                this.mKeysBackupStateManager.setState(KeysBackupState.ReadyToBackUp);
                ApiCallback<Void> apiCallback2 = this.backupAllGroupSessionsCallback;
                if (apiCallback2 != null) {
                    apiCallback2.onSuccess(null);
                }
                resetBackupAllGroupSessionsListeners();
                return;
            }
            this.mKeysBackupStateManager.setState(KeysBackupState.BackingUp);
            this.mCrypto.getEncryptingThreadHandler().post(new KeysBackup$backupKeys$1(this, inboundGroupSessionsToBackup));
        }
    }

    public final KeyBackupData encryptGroupSession(MXOlmInboundGroupSession2 mXOlmInboundGroupSession2) {
        String str = "OlmException";
        Intrinsics.checkParameterIsNotNull(mXOlmInboundGroupSession2, "session");
        MXDeviceInfo deviceWithIdentityKey = this.mCrypto.deviceWithIdentityKey(mXOlmInboundGroupSession2.mSenderKey, CryptoConstantsKt.MXCRYPTO_ALGORITHM_MEGOLM);
        MegolmSessionData exportKeys = mXOlmInboundGroupSession2.exportKeys();
        Pair[] pairArr = new Pair[5];
        if (exportKeys == null) {
            Intrinsics.throwNpe();
        }
        pairArr[0] = TuplesKt.to(CryptoRoomEntityFields.ALGORITHM, exportKeys.algorithm);
        pairArr[1] = TuplesKt.to("sender_key", exportKeys.senderKey);
        pairArr[2] = TuplesKt.to("sender_claimed_keys", exportKeys.senderClaimedKeys);
        List<String> list = exportKeys.forwardingCurve25519KeyChain;
        if (list == null) {
            list = new ArrayList<>();
        }
        pairArr[3] = TuplesKt.to("forwarding_curve25519_key_chain", list);
        pairArr[4] = TuplesKt.to("session_key", exportKeys.sessionKey);
        Map mapOf = MapsKt.mapOf(pairArr);
        OlmPkMessage olmPkMessage = null;
        OlmPkMessage olmPkMessage2 = null;
        try {
            OlmPkEncryption olmPkEncryption = this.mBackupKey;
            if (olmPkEncryption != null) {
                olmPkMessage = olmPkEncryption.encrypt(JsonUtility.getGson(false).toJson((Object) mapOf));
            }
            olmPkMessage2 = olmPkMessage;
        } catch (OlmException e) {
            Log.e(LOG_TAG, str, e);
        }
        KeyBackupData keyBackupData = new KeyBackupData();
        try {
            OlmInboundGroupSession olmInboundGroupSession = mXOlmInboundGroupSession2.mSession;
            Intrinsics.checkExpressionValueIsNotNull(olmInboundGroupSession, "session.mSession");
            keyBackupData.setFirstMessageIndex(olmInboundGroupSession.getFirstKnownIndex());
        } catch (OlmException e2) {
            Log.e(LOG_TAG, str, e2);
        }
        keyBackupData.setForwardedCount(mXOlmInboundGroupSession2.mForwardingCurve25519KeyChain.size());
        keyBackupData.setVerified(deviceWithIdentityKey != null && deviceWithIdentityKey.isVerified());
        Pair[] pairArr2 = new Pair[3];
        if (olmPkMessage2 == null) {
            Intrinsics.throwNpe();
        }
        pairArr2[0] = TuplesKt.to("ciphertext", olmPkMessage2.mCipherText);
        pairArr2[1] = TuplesKt.to("mac", olmPkMessage2.mMac);
        pairArr2[2] = TuplesKt.to("ephemeral", olmPkMessage2.mEphemeralKey);
        keyBackupData.setSessionData(JsonUtility.getGson(false).toJsonTree(MapsKt.mapOf(pairArr2)));
        return keyBackupData;
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x007d  */
    public final MegolmSessionData decryptKeyBackupData(KeyBackupData keyBackupData, String str, String str2, OlmPkDecryption olmPkDecryption) {
        String str3;
        String str4;
        Intrinsics.checkParameterIsNotNull(keyBackupData, "keyBackupData");
        Intrinsics.checkParameterIsNotNull(str, "sessionId");
        Intrinsics.checkParameterIsNotNull(str2, CryptoRoomEntityFields.ROOM_ID);
        Intrinsics.checkParameterIsNotNull(olmPkDecryption, "decryption");
        String str5 = null;
        MegolmSessionData megolmSessionData = null;
        JsonElement sessionData = keyBackupData.getSessionData();
        JsonObject asJsonObject = sessionData != null ? sessionData.getAsJsonObject() : null;
        if (asJsonObject != null) {
            JsonElement jsonElement = asJsonObject.get("ciphertext");
            if (jsonElement != null) {
                str3 = jsonElement.getAsString();
                if (asJsonObject != null) {
                    JsonElement jsonElement2 = asJsonObject.get("mac");
                    if (jsonElement2 != null) {
                        str4 = jsonElement2.getAsString();
                        if (asJsonObject != null) {
                            JsonElement jsonElement3 = asJsonObject.get("ephemeral");
                            if (jsonElement3 != null) {
                                str5 = jsonElement3.getAsString();
                            }
                        }
                        if (!(str3 == null || str4 == null || str5 == null)) {
                            OlmPkMessage olmPkMessage = new OlmPkMessage();
                            olmPkMessage.mCipherText = str3;
                            olmPkMessage.mMac = str4;
                            olmPkMessage.mEphemeralKey = str5;
                            megolmSessionData = (MegolmSessionData) JsonUtility.toClass(olmPkDecryption.decrypt(olmPkMessage), MegolmSessionData.class);
                            if (megolmSessionData != null) {
                                megolmSessionData.sessionId = str;
                                megolmSessionData.roomId = str2;
                            }
                        }
                        return megolmSessionData;
                    }
                }
                str4 = null;
                if (asJsonObject != null) {
                }
                OlmPkMessage olmPkMessage2 = new OlmPkMessage();
                olmPkMessage2.mCipherText = str3;
                olmPkMessage2.mMac = str4;
                olmPkMessage2.mEphemeralKey = str5;
                megolmSessionData = (MegolmSessionData) JsonUtility.toClass(olmPkDecryption.decrypt(olmPkMessage2), MegolmSessionData.class);
                if (megolmSessionData != null) {
                }
                return megolmSessionData;
            }
        }
        str3 = null;
        if (asJsonObject != null) {
        }
        str4 = null;
        if (asJsonObject != null) {
        }
        OlmPkMessage olmPkMessage22 = new OlmPkMessage();
        olmPkMessage22.mCipherText = str3;
        olmPkMessage22.mMac = str4;
        olmPkMessage22.mEphemeralKey = str5;
        try {
            megolmSessionData = (MegolmSessionData) JsonUtility.toClass(olmPkDecryption.decrypt(olmPkMessage22), MegolmSessionData.class);
        } catch (OlmException e) {
            Log.e(LOG_TAG, "OlmException", e);
        }
        if (megolmSessionData != null) {
        }
        return megolmSessionData;
    }

    public final RoomKeysRestClient getRoomKeysRestClient() {
        return this.mRoomKeysRestClient;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("KeysBackup for ");
        sb.append(this.mCrypto);
        return sb.toString();
    }
}
