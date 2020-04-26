package org.matrix.androidsdk.crypto.cryptostore.db.model;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.internal.RealmObjectProxy;
import io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.crypto.cryptostore.db.HelperKt;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0016\u0018\u0000 \u001c2\u00020\u0001:\u0001\u001cB3\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0007J\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018J\u0010\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0018R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\t\"\u0004\b\r\u0010\u000bR\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\t\"\u0004\b\u000f\u0010\u000bR\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\t\"\u0004\b\u0011\u0010\u000bR\u001e\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u00138\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016¨\u0006\u001d"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/model/DeviceInfoEntity;", "Lio/realm/RealmObject;", "primaryKey", "", "deviceId", "identityKey", "deviceInfoData", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getDeviceId", "()Ljava/lang/String;", "setDeviceId", "(Ljava/lang/String;)V", "getDeviceInfoData", "setDeviceInfoData", "getIdentityKey", "setIdentityKey", "getPrimaryKey", "setPrimaryKey", "users", "Lio/realm/RealmResults;", "Lorg/matrix/androidsdk/crypto/cryptostore/db/model/UserEntity;", "getUsers", "()Lio/realm/RealmResults;", "getDeviceInfo", "Lorg/matrix/androidsdk/crypto/data/MXDeviceInfo;", "putDeviceInfo", "", "deviceInfo", "Companion", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: DeviceInfoEntity.kt */
public class DeviceInfoEntity extends RealmObject implements org_matrix_androidsdk_crypto_cryptostore_db_model_DeviceInfoEntityRealmProxyInterface {
    public static final Companion Companion = new Companion(null);
    private String deviceId;
    private String deviceInfoData;
    private String identityKey;
    @PrimaryKey
    private String primaryKey;
    @LinkingObjects("devices")
    private final RealmResults<UserEntity> users;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/model/DeviceInfoEntity$Companion;", "", "()V", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: DeviceInfoEntity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public DeviceInfoEntity() {
        this(null, null, null, null, 15, null);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public String realmGet$deviceId() {
        return this.deviceId;
    }

    public String realmGet$deviceInfoData() {
        return this.deviceInfoData;
    }

    public String realmGet$identityKey() {
        return this.identityKey;
    }

    public String realmGet$primaryKey() {
        return this.primaryKey;
    }

    public RealmResults realmGet$users() {
        return this.users;
    }

    public void realmSet$deviceId(String str) {
        this.deviceId = str;
    }

    public void realmSet$deviceInfoData(String str) {
        this.deviceInfoData = str;
    }

    public void realmSet$identityKey(String str) {
        this.identityKey = str;
    }

    public void realmSet$primaryKey(String str) {
        this.primaryKey = str;
    }

    public void realmSet$users(RealmResults realmResults) {
        this.users = realmResults;
    }

    public final String getPrimaryKey() {
        return realmGet$primaryKey();
    }

    public final void setPrimaryKey(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        realmSet$primaryKey(str);
    }

    public /* synthetic */ DeviceInfoEntity(String str, String str2, String str3, String str4, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            str = "";
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        if ((i & 4) != 0) {
            str3 = null;
        }
        if ((i & 8) != 0) {
            str4 = null;
        }
        this(str, str2, str3, str4);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public final String getDeviceId() {
        return realmGet$deviceId();
    }

    public final void setDeviceId(String str) {
        realmSet$deviceId(str);
    }

    public final String getIdentityKey() {
        return realmGet$identityKey();
    }

    public final void setIdentityKey(String str) {
        realmSet$identityKey(str);
    }

    public final String getDeviceInfoData() {
        return realmGet$deviceInfoData();
    }

    public final void setDeviceInfoData(String str) {
        realmSet$deviceInfoData(str);
    }

    public DeviceInfoEntity(String str, String str2, String str3, String str4) {
        Intrinsics.checkParameterIsNotNull(str, "primaryKey");
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
        realmSet$primaryKey(str);
        realmSet$deviceId(str2);
        realmSet$identityKey(str3);
        realmSet$deviceInfoData(str4);
    }

    public final MXDeviceInfo getDeviceInfo() {
        return (MXDeviceInfo) HelperKt.deserializeFromRealm(realmGet$deviceInfoData());
    }

    public final void putDeviceInfo(MXDeviceInfo mXDeviceInfo) {
        realmSet$deviceInfoData(HelperKt.serializeForRealm(mXDeviceInfo));
    }

    public final RealmResults<UserEntity> getUsers() {
        return realmGet$users();
    }
}
