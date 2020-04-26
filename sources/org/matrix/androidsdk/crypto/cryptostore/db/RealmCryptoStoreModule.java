package org.matrix.androidsdk.crypto.cryptostore.db;

import io.realm.annotations.RealmModule;
import kotlin.Metadata;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoMetadataEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.DeviceInfoEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.IncomingRoomKeyRequestEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.KeysBackupDataEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmInboundGroupSessionEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OlmSessionEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OutgoingRoomKeyRequestEntity;
import org.matrix.androidsdk.crypto.cryptostore.db.model.UserEntity;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0001\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/RealmCryptoStoreModule;", "", "()V", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
@RealmModule(classes = {CryptoMetadataEntity.class, CryptoRoomEntity.class, DeviceInfoEntity.class, IncomingRoomKeyRequestEntity.class, KeysBackupDataEntity.class, OlmInboundGroupSessionEntity.class, OlmSessionEntity.class, OutgoingRoomKeyRequestEntity.class, UserEntity.class}, library = true)
/* compiled from: RealmCryptoStoreModule.kt */
public final class RealmCryptoStoreModule {
}
