package org.matrix.androidsdk.crypto.cryptostore.db;

import android.content.Context;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmConfiguration.Builder;
import io.realm.RealmList;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref.ObjectRef;
import kotlin.ranges.RangesKt;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.IncomingRoomKeyRequest;
import org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest;
import org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest.RequestState;
import org.matrix.androidsdk.crypto.cryptostore.IMXCryptoStore;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoMetadataEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;
import org.matrix.androidsdk.crypto.cryptostore.db.model.DeviceInfoEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.IncomingRoomKeyRequestEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.KeysBackupDataEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmInboundGroupSessionEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmInboundGroupSessionEntityKt;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmSessionEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmSessionEntityKt;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OutgoingRoomKeyRequestEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.UserEntity;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2;
import org.matrix.androidsdk.crypto.data.MXOlmSession;
import org.matrix.androidsdk.crypto.model.crypto.RoomKeyRequestBody;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.matrix.olm.OlmAccount;
import org.matrix.olm.OlmException;
import org.matrix.olm.OlmInboundGroupSession;
import org.matrix.olm.OlmSession;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000¢\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010#\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b \u0018\u0000 l2\u00020\u0001:\u0001lB\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\u0012\u0010\u0014\u001a\u00020\u00132\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016J\u0012\u0010\u0017\u001a\u00020\u00132\b\u0010\u0018\u001a\u0004\u0018\u00010\tH\u0016J\b\u0010\u0019\u001a\u00020\u0013H\u0016J\u0014\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\tH\u0016J\n\u0010\u001d\u001a\u0004\u0018\u00010\rH\u0016J\b\u0010\u001e\u001a\u00020\tH\u0016J\u001e\u0010\u001f\u001a\u0004\u0018\u00010\u000f2\b\u0010 \u001a\u0004\u0018\u00010\t2\b\u0010!\u001a\u0004\u0018\u00010\tH\u0016J\u0018\u0010\"\u001a\b\u0012\u0004\u0012\u00020\t0#2\b\u0010!\u001a\u0004\u0018\u00010\tH\u0016J\u001a\u0010$\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010\t2\u0006\u0010'\u001a\u00020%H\u0016J\u0014\u0010(\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020%0)H\u0016J\b\u0010*\u001a\u00020\u0003H\u0016J\u001e\u0010+\u001a\u0004\u0018\u00010\n2\b\u0010 \u001a\u0004\u0018\u00010\t2\b\u0010,\u001a\u0004\u0018\u00010\tH\u0016J\u000e\u0010-\u001a\b\u0012\u0004\u0012\u00020\n0.H\u0016J(\u0010/\u001a\u0004\u0018\u00010\u00162\b\u0010&\u001a\u0004\u0018\u00010\t2\b\u00100\u001a\u0004\u0018\u00010\t2\b\u00101\u001a\u0004\u0018\u00010\tH\u0016J\n\u00102\u001a\u0004\u0018\u00010\tH\u0016J\n\u00103\u001a\u0004\u0018\u000104H\u0016J\u0014\u00105\u001a\u0004\u0018\u00010\t2\b\u0010!\u001a\u0004\u0018\u00010\tH\u0016J\u0014\u00106\u001a\u0004\u0018\u0001072\b\u00108\u001a\u0004\u0018\u000107H\u0016J\u0014\u00109\u001a\u0004\u0018\u0001072\b\u0010:\u001a\u0004\u0018\u00010;H\u0016J\u001a\u0010<\u001a\u0004\u0018\u0001072\u000e\u0010=\u001a\n\u0012\u0004\u0012\u00020>\u0018\u00010#H\u0016J\u000e\u0010?\u001a\b\u0012\u0004\u0012\u00020\u00160.H\u0016J\u0012\u0010@\u001a\u0004\u0018\u00010\t2\u0006\u0010A\u001a\u00020\tH\u0016J\u000e\u0010B\u001a\b\u0012\u0004\u0012\u00020\t0.H\u0016J\u001e\u0010C\u001a\u0004\u0018\u00010\u001b2\b\u00100\u001a\u0004\u0018\u00010\t2\b\u0010&\u001a\u0004\u0018\u00010\tH\u0016J \u0010D\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u001b\u0018\u00010)2\b\u0010&\u001a\u0004\u0018\u00010\tH\u0016J\b\u0010E\u001a\u00020\u0003H\u0016J\u0010\u0010F\u001a\u00020%2\u0006\u0010G\u001a\u00020\u0003H\u0016J\u0016\u0010H\u001a\b\u0012\u0004\u0012\u00020\n0I2\u0006\u0010J\u001a\u00020%H\u0016J\u0018\u0010K\u001a\u00020\u00132\u0006\u0010L\u001a\u00020M2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010N\u001a\u00020\u0003H\u0016J\u0016\u0010O\u001a\u00020\u00132\f\u0010P\u001a\b\u0012\u0004\u0012\u00020\n0.H\u0016J\b\u0010Q\u001a\u00020\u0013H\u0016J\u001c\u0010R\u001a\u00020\u00132\b\u0010 \u001a\u0004\u0018\u00010\t2\b\u0010,\u001a\u0004\u0018\u00010\tH\u0016J\b\u0010S\u001a\u00020\u0013H\u0016J\u001e\u0010T\u001a\u00020\u00132\u0014\u0010U\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020%\u0018\u00010)H\u0016J\u0010\u0010V\u001a\u00020\u00132\u0006\u0010W\u001a\u00020\u0003H\u0016J\u0012\u0010X\u001a\u00020\u00132\b\u0010Y\u001a\u0004\u0018\u00010\tH\u0016J\u0012\u0010Z\u001a\u00020\u00132\b\u0010[\u001a\u0004\u0018\u000104H\u0016J\u0016\u0010\\\u001a\u00020\u00132\f\u0010]\u001a\b\u0012\u0004\u0012\u00020\t0.H\u0016J\u0010\u0010^\u001a\u00020\u00132\u0006\u0010_\u001a\u00020\rH\u0016J\u0012\u0010`\u001a\u00020\u00132\b\u00100\u001a\u0004\u0018\u00010\tH\u0016J\u0016\u0010a\u001a\u00020\u00132\f\u0010P\u001a\b\u0012\u0004\u0012\u00020\n0.H\u0016J\u0012\u0010b\u001a\u00020\u00132\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016J\u0018\u0010c\u001a\u00020\u00132\u0006\u0010A\u001a\u00020\t2\u0006\u0010d\u001a\u00020\tH\u0016J\u001c\u0010e\u001a\u00020\u00132\b\u0010f\u001a\u0004\u0018\u00010\u000f2\b\u0010!\u001a\u0004\u0018\u00010\tH\u0016J\u001c\u0010g\u001a\u00020\u00132\b\u0010&\u001a\u0004\u0018\u00010\t2\b\u0010h\u001a\u0004\u0018\u00010\u001bH\u0016J(\u0010i\u001a\u00020\u00132\b\u0010&\u001a\u0004\u0018\u00010\t2\u0014\u0010j\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u001b\u0018\u00010)H\u0016J\u0012\u0010k\u001a\u00020\u00132\b\u00108\u001a\u0004\u0018\u000107H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R*\u0010\u0007\u001a\u001e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bj\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n`\u000bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u000e¢\u0006\u0002\n\u0000R*\u0010\u000e\u001a\u001e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000f0\bj\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u000f`\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X.¢\u0006\u0002\n\u0000¨\u0006m"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/RealmCryptoStore;", "Lorg/matrix/androidsdk/crypto/cryptostore/IMXCryptoStore;", "enableFileEncryption", "", "(Z)V", "credentials", "Lorg/matrix/androidsdk/rest/model/login/Credentials;", "inboundGroupSessionToRelease", "Ljava/util/HashMap;", "", "Lorg/matrix/androidsdk/crypto/data/MXOlmInboundGroupSession2;", "Lkotlin/collections/HashMap;", "olmAccount", "Lorg/matrix/olm/OlmAccount;", "olmSessionsToRelease", "Lorg/matrix/androidsdk/crypto/data/MXOlmSession;", "realmConfiguration", "Lio/realm/RealmConfiguration;", "close", "", "deleteIncomingRoomKeyRequest", "incomingRoomKeyRequest", "Lorg/matrix/androidsdk/crypto/IncomingRoomKeyRequest;", "deleteOutgoingRoomKeyRequest", "transactionId", "deleteStore", "deviceWithIdentityKey", "Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "identityKey", "getAccount", "getDeviceId", "getDeviceSession", "sessionId", "deviceKey", "getDeviceSessionIds", "", "getDeviceTrackingStatus", "", "userId", "defaultValue", "getDeviceTrackingStatuses", "", "getGlobalBlacklistUnverifiedDevices", "getInboundGroupSession", "senderKey", "getInboundGroupSessions", "", "getIncomingRoomKeyRequest", "deviceId", "requestId", "getKeyBackupVersion", "getKeysBackupData", "Lorg/matrix/androidsdk/crypto/cryptostore/db/model/KeysBackupDataEntity;", "getLastUsedSessionId", "getOrAddOutgoingRoomKeyRequest", "Lorg/matrix/androidsdk/crypto/OutgoingRoomKeyRequest;", "request", "getOutgoingRoomKeyRequest", "requestBody", "Lorg/matrix/androidsdk/crypto/model/crypto/RoomKeyRequestBody;", "getOutgoingRoomKeyRequestByState", "states", "Lorg/matrix/androidsdk/crypto/OutgoingRoomKeyRequest$RequestState;", "getPendingIncomingRoomKeyRequests", "getRoomAlgorithm", "roomId", "getRoomsListBlacklistUnverifiedDevices", "getUserDevice", "getUserDevices", "hasData", "inboundGroupSessionsCount", "onlyBackedUp", "inboundGroupSessionsToBackup", "", "limit", "initWithCredentials", "context", "Landroid/content/Context;", "isCorrupted", "markBackupDoneForInboundGroupSessions", "sessions", "open", "removeInboundGroupSession", "resetBackupMarkers", "saveDeviceTrackingStatuses", "deviceTrackingStatuses", "setGlobalBlacklistUnverifiedDevices", "block", "setKeyBackupVersion", "keyBackupVersion", "setKeysBackupData", "keysBackupData", "setRoomsListBlacklistUnverifiedDevices", "roomIds", "storeAccount", "account", "storeDeviceId", "storeInboundGroupSessions", "storeIncomingRoomKeyRequest", "storeRoomAlgorithm", "algorithm", "storeSession", "session", "storeUserDevice", "deviceInfo", "storeUserDevices", "devices", "updateOutgoingRoomKeyRequest", "Companion", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: RealmCryptoStore.kt */
public final class RealmCryptoStore implements IMXCryptoStore {
    public static final Companion Companion = new Companion(null);
    private static final String LOG_TAG = "RealmCryptoStore";
    /* access modifiers changed from: private */
    public Credentials credentials;
    private final boolean enableFileEncryption;
    /* access modifiers changed from: private */
    public final HashMap<String, MXOlmInboundGroupSession2> inboundGroupSessionToRelease;
    private OlmAccount olmAccount;
    private final HashMap<String, MXOlmSession> olmSessionsToRelease;
    private RealmConfiguration realmConfiguration;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/RealmCryptoStore$Companion;", "", "()V", "LOG_TAG", "", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: RealmCryptoStore.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public RealmCryptoStore() {
        this(false, 1, null);
    }

    public boolean isCorrupted() {
        return false;
    }

    public RealmCryptoStore(boolean z) {
        this.enableFileEncryption = z;
        this.olmSessionsToRelease = new HashMap<>();
        this.inboundGroupSessionToRelease = new HashMap<>();
    }

    public /* synthetic */ RealmCryptoStore(boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            z = false;
        }
        this(z);
    }

    public static final /* synthetic */ Credentials access$getCredentials$p(RealmCryptoStore realmCryptoStore) {
        Credentials credentials2 = realmCryptoStore.credentials;
        if (credentials2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("credentials");
        }
        return credentials2;
    }

    public void initWithCredentials(Context context, Credentials credentials2) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(credentials2, "credentials");
        this.credentials = credentials2;
        Realm.init(context.getApplicationContext());
        Builder builder = new Builder();
        File filesDir = context.getFilesDir();
        String userId = credentials2.getUserId();
        if (userId == null) {
            userId = "defaultUserId";
        }
        Intrinsics.checkExpressionValueIsNotNull(userId, "(credentials.getUserId() ?: \"defaultUserId\")");
        RealmConfiguration build = builder.directory(new File(filesDir, HelperKt.hash(userId))).name("crypto_store.realm").modules(new RealmCryptoStoreModule(), new Object[0]).schemaVersion(2).migration(RealmCryptoStoreMigration.INSTANCE).initialData(new CryptoFileStoreImporter(this.enableFileEncryption, context, credentials2)).build();
        Intrinsics.checkExpressionValueIsNotNull(build, "RealmConfiguration.Build…\n                .build()");
        this.realmConfiguration = build;
    }

    public boolean hasData() {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        return ((Boolean) HelperKt.doWithRealm(realmConfiguration2, RealmCryptoStore$hasData$1.INSTANCE)).booleanValue();
    }

    public void deleteStore() {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        HelperKt.doRealmTransaction(realmConfiguration2, RealmCryptoStore$deleteStore$1.INSTANCE);
    }

    public void open() {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        HelperKt.doWithRealm(realmConfiguration2, new RealmCryptoStore$open$1(this));
    }

    public void close() {
        for (Entry value : this.olmSessionsToRelease.entrySet()) {
            ((MXOlmSession) value.getValue()).getOlmSession().releaseSession();
        }
        this.olmSessionsToRelease.clear();
        for (Entry value2 : this.inboundGroupSessionToRelease.entrySet()) {
            ((MXOlmInboundGroupSession2) value2.getValue()).mSession.releaseSession();
        }
        this.inboundGroupSessionToRelease.clear();
        OlmAccount olmAccount2 = this.olmAccount;
        if (olmAccount2 != null) {
            olmAccount2.releaseAccount();
        }
    }

    public void storeDeviceId(String str) {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$storeDeviceId$1(str));
    }

    public String getDeviceId() {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        CryptoMetadataEntity cryptoMetadataEntity = (CryptoMetadataEntity) HelperKt.doRealmQueryAndCopy(realmConfiguration2, RealmCryptoStore$getDeviceId$1.INSTANCE);
        if (cryptoMetadataEntity != null) {
            String deviceId = cryptoMetadataEntity.getDeviceId();
            if (deviceId != null) {
                return deviceId;
            }
        }
        return "";
    }

    public void storeAccount(OlmAccount olmAccount2) {
        Intrinsics.checkParameterIsNotNull(olmAccount2, "account");
        this.olmAccount = olmAccount2;
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$storeAccount$1(olmAccount2));
    }

    public OlmAccount getAccount() {
        if (this.olmAccount == null) {
            RealmConfiguration realmConfiguration2 = this.realmConfiguration;
            if (realmConfiguration2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
            }
            CryptoMetadataEntity cryptoMetadataEntity = (CryptoMetadataEntity) HelperKt.doRealmQueryAndCopy(realmConfiguration2, RealmCryptoStore$getAccount$1.INSTANCE);
            this.olmAccount = cryptoMetadataEntity != null ? cryptoMetadataEntity.getOlmAccount() : null;
        }
        return this.olmAccount;
    }

    public void storeUserDevice(String str, MXDeviceInfo mXDeviceInfo) {
        if (str != null && mXDeviceInfo != null) {
            RealmConfiguration realmConfiguration2 = this.realmConfiguration;
            if (realmConfiguration2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
            }
            HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$storeUserDevice$1(str, mXDeviceInfo));
        }
    }

    public MXDeviceInfo getUserDevice(String str, String str2) {
        if (str == null || str2 == null) {
            return null;
        }
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        DeviceInfoEntity deviceInfoEntity = (DeviceInfoEntity) HelperKt.doRealmQueryAndCopy(realmConfiguration2, new RealmCryptoStore$getUserDevice$1(str2, str));
        if (deviceInfoEntity != null) {
            return deviceInfoEntity.getDeviceInfo();
        }
        return null;
    }

    public MXDeviceInfo deviceWithIdentityKey(String str) {
        MXDeviceInfo mXDeviceInfo = null;
        if (str == null) {
            return null;
        }
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        DeviceInfoEntity deviceInfoEntity = (DeviceInfoEntity) HelperKt.doRealmQueryAndCopy(realmConfiguration2, new RealmCryptoStore$deviceWithIdentityKey$1(str));
        if (deviceInfoEntity != null) {
            mXDeviceInfo = deviceInfoEntity.getDeviceInfo();
        }
        return mXDeviceInfo;
    }

    public void storeUserDevices(String str, Map<String, MXDeviceInfo> map) {
        if (str != null) {
            RealmConfiguration realmConfiguration2 = this.realmConfiguration;
            if (realmConfiguration2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
            }
            HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$storeUserDevices$1(map, str));
        }
    }

    public Map<String, MXDeviceInfo> getUserDevices(String str) {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        UserEntity userEntity = (UserEntity) HelperKt.doRealmQueryAndCopy(realmConfiguration2, new RealmCryptoStore$getUserDevices$1(str));
        if (userEntity != null) {
            RealmList devices = userEntity.getDevices();
            if (devices != null) {
                Iterable<DeviceInfoEntity> iterable = devices;
                Collection arrayList = new ArrayList();
                for (DeviceInfoEntity deviceInfo : iterable) {
                    MXDeviceInfo deviceInfo2 = deviceInfo.getDeviceInfo();
                    if (deviceInfo2 != null) {
                        arrayList.add(deviceInfo2);
                    }
                }
                Iterable iterable2 = (List) arrayList;
                Map linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(iterable2, 10)), 16));
                for (Object next : iterable2) {
                    linkedHashMap.put(((MXDeviceInfo) next).deviceId, next);
                }
                return MapsKt.toMutableMap(linkedHashMap);
            }
        }
        return null;
    }

    public void storeRoomAlgorithm(String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str, CryptoRoomEntityFields.ROOM_ID);
        Intrinsics.checkParameterIsNotNull(str2, CryptoRoomEntityFields.ALGORITHM);
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$storeRoomAlgorithm$1(str2, str));
    }

    public String getRoomAlgorithm(String str) {
        Intrinsics.checkParameterIsNotNull(str, CryptoRoomEntityFields.ROOM_ID);
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        CryptoRoomEntity cryptoRoomEntity = (CryptoRoomEntity) HelperKt.doRealmQueryAndCopy(realmConfiguration2, new RealmCryptoStore$getRoomAlgorithm$1(str));
        if (cryptoRoomEntity != null) {
            return cryptoRoomEntity.getAlgorithm();
        }
        return null;
    }

    public void storeSession(MXOlmSession mXOlmSession, String str) {
        if (mXOlmSession != null && str != null) {
            ObjectRef objectRef = new ObjectRef();
            OlmSession olmSession = null;
            objectRef.element = (String) null;
            try {
                objectRef.element = mXOlmSession.getOlmSession().sessionIdentifier();
            } catch (OlmException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("## storeSession() : sessionIdentifier failed ");
                sb.append(e.getMessage());
                Log.e(LOG_TAG, sb.toString(), e);
            }
            if (((String) objectRef.element) != null) {
                String createPrimaryKey = OlmSessionEntityKt.createPrimaryKey(OlmSessionEntity.Companion, (String) objectRef.element, str);
                MXOlmSession mXOlmSession2 = (MXOlmSession) this.olmSessionsToRelease.get(createPrimaryKey);
                if (mXOlmSession2 != null) {
                    olmSession = mXOlmSession2.getOlmSession();
                }
                if (!Intrinsics.areEqual((Object) olmSession, (Object) mXOlmSession.getOlmSession())) {
                    MXOlmSession mXOlmSession3 = (MXOlmSession) this.olmSessionsToRelease.get(createPrimaryKey);
                    if (mXOlmSession3 != null) {
                        OlmSession olmSession2 = mXOlmSession3.getOlmSession();
                        if (olmSession2 != null) {
                            olmSession2.releaseSession();
                        }
                    }
                }
                this.olmSessionsToRelease.put(createPrimaryKey, mXOlmSession);
                RealmConfiguration realmConfiguration2 = this.realmConfiguration;
                if (realmConfiguration2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
                }
                HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$storeSession$1(createPrimaryKey, objectRef, str, mXOlmSession));
            }
        }
    }

    public MXOlmSession getDeviceSession(String str, String str2) {
        if (str == null || str2 == null) {
            return null;
        }
        String createPrimaryKey = OlmSessionEntityKt.createPrimaryKey(OlmSessionEntity.Companion, str, str2);
        if (this.olmSessionsToRelease.get(createPrimaryKey) == null) {
            RealmConfiguration realmConfiguration2 = this.realmConfiguration;
            if (realmConfiguration2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
            }
            OlmSessionEntity olmSessionEntity = (OlmSessionEntity) HelperKt.doRealmQueryAndCopy(realmConfiguration2, new RealmCryptoStore$getDeviceSession$1(createPrimaryKey));
            if (olmSessionEntity != null) {
                OlmSession olmSession = olmSessionEntity.getOlmSession();
                if (!(olmSession == null || olmSessionEntity.getSessionId() == null)) {
                    this.olmSessionsToRelease.put(createPrimaryKey, new MXOlmSession(olmSession, olmSessionEntity.getLastReceivedMessageTs()));
                }
            }
        }
        return (MXOlmSession) this.olmSessionsToRelease.get(createPrimaryKey);
    }

    public String getLastUsedSessionId(String str) {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        OlmSessionEntity olmSessionEntity = (OlmSessionEntity) HelperKt.doRealmQueryAndCopy(realmConfiguration2, new RealmCryptoStore$getLastUsedSessionId$1(str));
        if (olmSessionEntity != null) {
            return olmSessionEntity.getSessionId();
        }
        return null;
    }

    public Set<String> getDeviceSessionIds(String str) {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        Iterable<OlmSessionEntity> doRealmQueryAndCopyList = HelperKt.doRealmQueryAndCopyList(realmConfiguration2, new RealmCryptoStore$getDeviceSessionIds$1(str));
        Collection arrayList = new ArrayList();
        for (OlmSessionEntity sessionId : doRealmQueryAndCopyList) {
            String sessionId2 = sessionId.getSessionId();
            if (sessionId2 != null) {
                arrayList.add(sessionId2);
            }
        }
        return CollectionsKt.toMutableSet((List) arrayList);
    }

    public void storeInboundGroupSessions(List<MXOlmInboundGroupSession2> list) {
        Intrinsics.checkParameterIsNotNull(list, "sessions");
        if (!list.isEmpty()) {
            RealmConfiguration realmConfiguration2 = this.realmConfiguration;
            if (realmConfiguration2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
            }
            HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$storeInboundGroupSessions$1(this, list));
        }
    }

    public MXOlmInboundGroupSession2 getInboundGroupSession(String str, String str2) {
        String createPrimaryKey = OlmInboundGroupSessionEntityKt.createPrimaryKey(OlmInboundGroupSessionEntity.Companion, str, str2);
        if (this.inboundGroupSessionToRelease.get(createPrimaryKey) == null) {
            RealmConfiguration realmConfiguration2 = this.realmConfiguration;
            if (realmConfiguration2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
            }
            OlmInboundGroupSessionEntity olmInboundGroupSessionEntity = (OlmInboundGroupSessionEntity) HelperKt.doRealmQueryAndCopy(realmConfiguration2, new RealmCryptoStore$getInboundGroupSession$1(createPrimaryKey));
            if (olmInboundGroupSessionEntity != null) {
                MXOlmInboundGroupSession2 inboundGroupSession = olmInboundGroupSessionEntity.getInboundGroupSession();
                if (inboundGroupSession != null) {
                    this.inboundGroupSessionToRelease.put(createPrimaryKey, inboundGroupSession);
                }
            }
        }
        return (MXOlmInboundGroupSession2) this.inboundGroupSessionToRelease.get(createPrimaryKey);
    }

    public List<MXOlmInboundGroupSession2> getInboundGroupSessions() {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        Iterable<OlmInboundGroupSessionEntity> doRealmQueryAndCopyList = HelperKt.doRealmQueryAndCopyList(realmConfiguration2, RealmCryptoStore$getInboundGroupSessions$1.INSTANCE);
        Collection arrayList = new ArrayList();
        for (OlmInboundGroupSessionEntity inboundGroupSession : doRealmQueryAndCopyList) {
            MXOlmInboundGroupSession2 inboundGroupSession2 = inboundGroupSession.getInboundGroupSession();
            if (inboundGroupSession2 != null) {
                arrayList.add(inboundGroupSession2);
            }
        }
        return CollectionsKt.toMutableList((Collection<? extends T>) (List) arrayList);
    }

    public void removeInboundGroupSession(String str, String str2) {
        String createPrimaryKey = OlmInboundGroupSessionEntityKt.createPrimaryKey(OlmInboundGroupSessionEntity.Companion, str, str2);
        MXOlmInboundGroupSession2 mXOlmInboundGroupSession2 = (MXOlmInboundGroupSession2) this.inboundGroupSessionToRelease.get(createPrimaryKey);
        if (mXOlmInboundGroupSession2 != null) {
            OlmInboundGroupSession olmInboundGroupSession = mXOlmInboundGroupSession2.mSession;
            if (olmInboundGroupSession != null) {
                olmInboundGroupSession.releaseSession();
            }
        }
        this.inboundGroupSessionToRelease.remove(createPrimaryKey);
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$removeInboundGroupSession$1(createPrimaryKey));
    }

    public String getKeyBackupVersion() {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        CryptoMetadataEntity cryptoMetadataEntity = (CryptoMetadataEntity) HelperKt.doRealmQueryAndCopy(realmConfiguration2, RealmCryptoStore$getKeyBackupVersion$1.INSTANCE);
        if (cryptoMetadataEntity != null) {
            return cryptoMetadataEntity.getBackupVersion();
        }
        return null;
    }

    public void setKeyBackupVersion(String str) {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$setKeyBackupVersion$1(str));
    }

    public KeysBackupDataEntity getKeysBackupData() {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        return (KeysBackupDataEntity) HelperKt.doRealmQueryAndCopy(realmConfiguration2, RealmCryptoStore$getKeysBackupData$1.INSTANCE);
    }

    public void setKeysBackupData(KeysBackupDataEntity keysBackupDataEntity) {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$setKeysBackupData$1(keysBackupDataEntity));
    }

    public void resetBackupMarkers() {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        HelperKt.doRealmTransaction(realmConfiguration2, RealmCryptoStore$resetBackupMarkers$1.INSTANCE);
    }

    public void markBackupDoneForInboundGroupSessions(List<MXOlmInboundGroupSession2> list) {
        Intrinsics.checkParameterIsNotNull(list, "sessions");
        if (!list.isEmpty()) {
            RealmConfiguration realmConfiguration2 = this.realmConfiguration;
            if (realmConfiguration2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
            }
            HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$markBackupDoneForInboundGroupSessions$1(list));
        }
    }

    public List<MXOlmInboundGroupSession2> inboundGroupSessionsToBackup(int i) {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        Iterable<OlmInboundGroupSessionEntity> doRealmQueryAndCopyList = HelperKt.doRealmQueryAndCopyList(realmConfiguration2, new RealmCryptoStore$inboundGroupSessionsToBackup$1(i));
        Collection arrayList = new ArrayList();
        for (OlmInboundGroupSessionEntity inboundGroupSession : doRealmQueryAndCopyList) {
            MXOlmInboundGroupSession2 inboundGroupSession2 = inboundGroupSession.getInboundGroupSession();
            if (inboundGroupSession2 != null) {
                arrayList.add(inboundGroupSession2);
            }
        }
        return (List) arrayList;
    }

    public int inboundGroupSessionsCount(boolean z) {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        return ((Number) HelperKt.doWithRealm(realmConfiguration2, new RealmCryptoStore$inboundGroupSessionsCount$1(z))).intValue();
    }

    public void setGlobalBlacklistUnverifiedDevices(boolean z) {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$setGlobalBlacklistUnverifiedDevices$1(z));
    }

    public boolean getGlobalBlacklistUnverifiedDevices() {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        CryptoMetadataEntity cryptoMetadataEntity = (CryptoMetadataEntity) HelperKt.doRealmQueryAndCopy(realmConfiguration2, RealmCryptoStore$getGlobalBlacklistUnverifiedDevices$1.INSTANCE);
        if (cryptoMetadataEntity != null) {
            return cryptoMetadataEntity.getGlobalBlacklistUnverifiedDevices();
        }
        return false;
    }

    public void setRoomsListBlacklistUnverifiedDevices(List<String> list) {
        Intrinsics.checkParameterIsNotNull(list, "roomIds");
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$setRoomsListBlacklistUnverifiedDevices$1(list));
    }

    public List<String> getRoomsListBlacklistUnverifiedDevices() {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        Iterable<CryptoRoomEntity> doRealmQueryAndCopyList = HelperKt.doRealmQueryAndCopyList(realmConfiguration2, RealmCryptoStore$getRoomsListBlacklistUnverifiedDevices$1.INSTANCE);
        Collection arrayList = new ArrayList();
        for (CryptoRoomEntity roomId : doRealmQueryAndCopyList) {
            String roomId2 = roomId.getRoomId();
            if (roomId2 != null) {
                arrayList.add(roomId2);
            }
        }
        return CollectionsKt.toMutableList((Collection<? extends T>) (List) arrayList);
    }

    public Map<String, Integer> getDeviceTrackingStatuses() {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        Iterable doRealmQueryAndCopyList = HelperKt.doRealmQueryAndCopyList(realmConfiguration2, RealmCryptoStore$getDeviceTrackingStatuses$1.INSTANCE);
        Map linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(doRealmQueryAndCopyList, 10)), 16));
        for (Object next : doRealmQueryAndCopyList) {
            String userId = ((UserEntity) next).getUserId();
            if (userId == null) {
                Intrinsics.throwNpe();
            }
            linkedHashMap.put(userId, next);
        }
        Map linkedHashMap2 = new LinkedHashMap(MapsKt.mapCapacity(linkedHashMap.size()));
        for (Entry entry : linkedHashMap.entrySet()) {
            linkedHashMap2.put(entry.getKey(), Integer.valueOf(((UserEntity) entry.getValue()).getDeviceTrackingStatus()));
        }
        return MapsKt.toMutableMap(linkedHashMap2);
    }

    public void saveDeviceTrackingStatuses(Map<String, Integer> map) {
        if (map != null) {
            RealmConfiguration realmConfiguration2 = this.realmConfiguration;
            if (realmConfiguration2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
            }
            HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$saveDeviceTrackingStatuses$1(map));
        }
    }

    public int getDeviceTrackingStatus(String str, int i) {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        UserEntity userEntity = (UserEntity) HelperKt.doRealmQueryAndCopy(realmConfiguration2, new RealmCryptoStore$getDeviceTrackingStatus$1(str));
        return userEntity != null ? userEntity.getDeviceTrackingStatus() : i;
    }

    public OutgoingRoomKeyRequest getOutgoingRoomKeyRequest(RoomKeyRequestBody roomKeyRequestBody) {
        OutgoingRoomKeyRequest outgoingRoomKeyRequest = null;
        if (roomKeyRequestBody == null) {
            return null;
        }
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity = (OutgoingRoomKeyRequestEntity) HelperKt.doRealmQueryAndCopy(realmConfiguration2, new RealmCryptoStore$getOutgoingRoomKeyRequest$1(roomKeyRequestBody));
        if (outgoingRoomKeyRequestEntity != null) {
            outgoingRoomKeyRequest = outgoingRoomKeyRequestEntity.toOutgoingRoomKeyRequest();
        }
        return outgoingRoomKeyRequest;
    }

    public OutgoingRoomKeyRequest getOrAddOutgoingRoomKeyRequest(OutgoingRoomKeyRequest outgoingRoomKeyRequest) {
        if ((outgoingRoomKeyRequest != null ? outgoingRoomKeyRequest.mRequestBody : null) == null) {
            return null;
        }
        OutgoingRoomKeyRequest outgoingRoomKeyRequest2 = getOutgoingRoomKeyRequest(outgoingRoomKeyRequest.mRequestBody);
        if (outgoingRoomKeyRequest2 != null) {
            return outgoingRoomKeyRequest2;
        }
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$getOrAddOutgoingRoomKeyRequest$1(outgoingRoomKeyRequest));
        return outgoingRoomKeyRequest;
    }

    public OutgoingRoomKeyRequest getOutgoingRoomKeyRequestByState(Set<RequestState> set) {
        OutgoingRoomKeyRequest outgoingRoomKeyRequest = null;
        if (set == null) {
            return null;
        }
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        OutgoingRoomKeyRequestEntity outgoingRoomKeyRequestEntity = (OutgoingRoomKeyRequestEntity) HelperKt.doRealmQueryAndCopy(realmConfiguration2, new RealmCryptoStore$getOutgoingRoomKeyRequestByState$1(set));
        if (outgoingRoomKeyRequestEntity != null) {
            outgoingRoomKeyRequest = outgoingRoomKeyRequestEntity.toOutgoingRoomKeyRequest();
        }
        return outgoingRoomKeyRequest;
    }

    public void updateOutgoingRoomKeyRequest(OutgoingRoomKeyRequest outgoingRoomKeyRequest) {
        if (outgoingRoomKeyRequest != null) {
            RealmConfiguration realmConfiguration2 = this.realmConfiguration;
            if (realmConfiguration2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
            }
            HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$updateOutgoingRoomKeyRequest$1(outgoingRoomKeyRequest));
        }
    }

    public void deleteOutgoingRoomKeyRequest(String str) {
        if (str != null) {
            RealmConfiguration realmConfiguration2 = this.realmConfiguration;
            if (realmConfiguration2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
            }
            HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$deleteOutgoingRoomKeyRequest$1(str));
        }
    }

    public void storeIncomingRoomKeyRequest(IncomingRoomKeyRequest incomingRoomKeyRequest) {
        if (incomingRoomKeyRequest != null) {
            RealmConfiguration realmConfiguration2 = this.realmConfiguration;
            if (realmConfiguration2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
            }
            HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$storeIncomingRoomKeyRequest$1(incomingRoomKeyRequest));
        }
    }

    public void deleteIncomingRoomKeyRequest(IncomingRoomKeyRequest incomingRoomKeyRequest) {
        if (incomingRoomKeyRequest != null) {
            RealmConfiguration realmConfiguration2 = this.realmConfiguration;
            if (realmConfiguration2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
            }
            HelperKt.doRealmTransaction(realmConfiguration2, new RealmCryptoStore$deleteIncomingRoomKeyRequest$1(incomingRoomKeyRequest));
        }
    }

    public IncomingRoomKeyRequest getIncomingRoomKeyRequest(String str, String str2, String str3) {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        IncomingRoomKeyRequestEntity incomingRoomKeyRequestEntity = (IncomingRoomKeyRequestEntity) HelperKt.doRealmQueryAndCopy(realmConfiguration2, new RealmCryptoStore$getIncomingRoomKeyRequest$1(str, str2, str3));
        if (incomingRoomKeyRequestEntity != null) {
            return incomingRoomKeyRequestEntity.toIncomingRoomKeyRequest();
        }
        return null;
    }

    public List<IncomingRoomKeyRequest> getPendingIncomingRoomKeyRequests() {
        RealmConfiguration realmConfiguration2 = this.realmConfiguration;
        if (realmConfiguration2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("realmConfiguration");
        }
        Iterable<IncomingRoomKeyRequestEntity> doRealmQueryAndCopyList = HelperKt.doRealmQueryAndCopyList(realmConfiguration2, RealmCryptoStore$getPendingIncomingRoomKeyRequests$1.INSTANCE);
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(doRealmQueryAndCopyList, 10));
        for (IncomingRoomKeyRequestEntity incomingRoomKeyRequest : doRealmQueryAndCopyList) {
            arrayList.add(incomingRoomKeyRequest.toIncomingRoomKeyRequest());
        }
        return CollectionsKt.toMutableList((Collection<? extends T>) (List) arrayList);
    }
}
