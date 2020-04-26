package org.matrix.androidsdk.crypto.cryptostore.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.internal.RealmObjectProxy;
import io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\r\b\u0010\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B'\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR \u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\t\"\u0004\b\u0011\u0010\u000b¨\u0006\u0013"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/model/CryptoRoomEntity;", "Lio/realm/RealmObject;", "roomId", "", "algorithm", "blacklistUnverifiedDevices", "", "(Ljava/lang/String;Ljava/lang/String;Z)V", "getAlgorithm", "()Ljava/lang/String;", "setAlgorithm", "(Ljava/lang/String;)V", "getBlacklistUnverifiedDevices", "()Z", "setBlacklistUnverifiedDevices", "(Z)V", "getRoomId", "setRoomId", "Companion", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: CryptoRoomEntity.kt */
public class CryptoRoomEntity extends RealmObject implements org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoRoomEntityRealmProxyInterface {
    public static final Companion Companion = new Companion(null);
    private String algorithm;
    private boolean blacklistUnverifiedDevices;
    @PrimaryKey
    private String roomId;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/model/CryptoRoomEntity$Companion;", "", "()V", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: CryptoRoomEntity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public CryptoRoomEntity() {
        this(null, null, false, 7, null);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public String realmGet$algorithm() {
        return this.algorithm;
    }

    public boolean realmGet$blacklistUnverifiedDevices() {
        return this.blacklistUnverifiedDevices;
    }

    public String realmGet$roomId() {
        return this.roomId;
    }

    public void realmSet$algorithm(String str) {
        this.algorithm = str;
    }

    public void realmSet$blacklistUnverifiedDevices(boolean z) {
        this.blacklistUnverifiedDevices = z;
    }

    public void realmSet$roomId(String str) {
        this.roomId = str;
    }

    public /* synthetic */ CryptoRoomEntity(String str, String str2, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            str = null;
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        if ((i & 4) != 0) {
            z = false;
        }
        this(str, str2, z);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public final String getRoomId() {
        return realmGet$roomId();
    }

    public final void setRoomId(String str) {
        realmSet$roomId(str);
    }

    public final String getAlgorithm() {
        return realmGet$algorithm();
    }

    public final void setAlgorithm(String str) {
        realmSet$algorithm(str);
    }

    public final boolean getBlacklistUnverifiedDevices() {
        return realmGet$blacklistUnverifiedDevices();
    }

    public final void setBlacklistUnverifiedDevices(boolean z) {
        realmSet$blacklistUnverifiedDevices(z);
    }

    public CryptoRoomEntity(String str, String str2, boolean z) {
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
        realmSet$roomId(str);
        realmSet$algorithm(str2);
        realmSet$blacklistUnverifiedDevices(z);
    }
}
