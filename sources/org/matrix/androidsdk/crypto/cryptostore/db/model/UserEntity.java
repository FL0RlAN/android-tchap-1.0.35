package org.matrix.androidsdk.crypto.cryptostore.db.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.internal.RealmObjectProxy;
import io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxyInterface;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.crypto.cryptostore.db.model.UserEntityFields.DEVICES;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\b\u0016\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B+\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tR\u001a\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR \u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R \u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015¨\u0006\u0017"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/model/UserEntity;", "Lio/realm/RealmObject;", "userId", "", "devices", "Lio/realm/RealmList;", "Lorg/matrix/androidsdk/crypto/cryptostore/db/model/DeviceInfoEntity;", "deviceTrackingStatus", "", "(Ljava/lang/String;Lio/realm/RealmList;I)V", "getDeviceTrackingStatus", "()I", "setDeviceTrackingStatus", "(I)V", "getDevices", "()Lio/realm/RealmList;", "setDevices", "(Lio/realm/RealmList;)V", "getUserId", "()Ljava/lang/String;", "setUserId", "(Ljava/lang/String;)V", "Companion", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: UserEntity.kt */
public class UserEntity extends RealmObject implements org_matrix_androidsdk_crypto_cryptostore_db_model_UserEntityRealmProxyInterface {
    public static final Companion Companion = new Companion(null);
    private int deviceTrackingStatus;
    private RealmList<DeviceInfoEntity> devices;
    @PrimaryKey
    private String userId;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/model/UserEntity$Companion;", "", "()V", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: UserEntity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public UserEntity() {
        this(null, null, 0, 7, null);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public int realmGet$deviceTrackingStatus() {
        return this.deviceTrackingStatus;
    }

    public RealmList realmGet$devices() {
        return this.devices;
    }

    public String realmGet$userId() {
        return this.userId;
    }

    public void realmSet$deviceTrackingStatus(int i) {
        this.deviceTrackingStatus = i;
    }

    public void realmSet$devices(RealmList realmList) {
        this.devices = realmList;
    }

    public void realmSet$userId(String str) {
        this.userId = str;
    }

    public /* synthetic */ UserEntity(String str, RealmList realmList, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i2 & 1) != 0) {
            str = null;
        }
        if ((i2 & 2) != 0) {
            realmList = new RealmList();
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        this(str, realmList, i);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public final String getUserId() {
        return realmGet$userId();
    }

    public final void setUserId(String str) {
        realmSet$userId(str);
    }

    public final RealmList<DeviceInfoEntity> getDevices() {
        return realmGet$devices();
    }

    public final void setDevices(RealmList<DeviceInfoEntity> realmList) {
        Intrinsics.checkParameterIsNotNull(realmList, "<set-?>");
        realmSet$devices(realmList);
    }

    public final int getDeviceTrackingStatus() {
        return realmGet$deviceTrackingStatus();
    }

    public final void setDeviceTrackingStatus(int i) {
        realmSet$deviceTrackingStatus(i);
    }

    public UserEntity(String str, RealmList<DeviceInfoEntity> realmList, int i) {
        Intrinsics.checkParameterIsNotNull(realmList, DEVICES.$);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
        realmSet$userId(str);
        realmSet$devices(realmList);
        realmSet$deviceTrackingStatus(i);
    }
}
