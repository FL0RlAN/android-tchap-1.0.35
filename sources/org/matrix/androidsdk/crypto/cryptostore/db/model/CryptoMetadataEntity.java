package org.matrix.androidsdk.crypto.cryptostore.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.internal.RealmObjectProxy;
import io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface;
import kotlin.Metadata;
import org.matrix.androidsdk.crypto.cryptostore.db.HelperKt;
import org.matrix.olm.OlmAccount;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0010\u0018\u00002\u00020\u0001BK\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\nJ\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cJ\u0010\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010\u001cR\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\f\"\u0004\b\u0010\u0010\u000eR\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\f\"\u0004\b\u0012\u0010\u000eR\u001a\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\f\"\u0004\b\u0018\u0010\u000eR \u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\f\"\u0004\b\u001a\u0010\u000e¨\u0006 "}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/model/CryptoMetadataEntity;", "Lio/realm/RealmObject;", "userId", "", "deviceId", "olmAccountData", "deviceSyncToken", "globalBlacklistUnverifiedDevices", "", "backupVersion", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;)V", "getBackupVersion", "()Ljava/lang/String;", "setBackupVersion", "(Ljava/lang/String;)V", "getDeviceId", "setDeviceId", "getDeviceSyncToken", "setDeviceSyncToken", "getGlobalBlacklistUnverifiedDevices", "()Z", "setGlobalBlacklistUnverifiedDevices", "(Z)V", "getOlmAccountData", "setOlmAccountData", "getUserId", "setUserId", "getOlmAccount", "Lorg/matrix/olm/OlmAccount;", "putOlmAccount", "", "olmAccount", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: CryptoMetadataEntity.kt */
public class CryptoMetadataEntity extends RealmObject implements org_matrix_androidsdk_crypto_cryptostore_db_model_CryptoMetadataEntityRealmProxyInterface {
    private String backupVersion;
    private String deviceId;
    private String deviceSyncToken;
    private boolean globalBlacklistUnverifiedDevices;
    private String olmAccountData;
    @PrimaryKey
    private String userId;

    public CryptoMetadataEntity() {
        this(null, null, null, null, false, null, 63, null);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public String realmGet$backupVersion() {
        return this.backupVersion;
    }

    public String realmGet$deviceId() {
        return this.deviceId;
    }

    public String realmGet$deviceSyncToken() {
        return this.deviceSyncToken;
    }

    public boolean realmGet$globalBlacklistUnverifiedDevices() {
        return this.globalBlacklistUnverifiedDevices;
    }

    public String realmGet$olmAccountData() {
        return this.olmAccountData;
    }

    public String realmGet$userId() {
        return this.userId;
    }

    public void realmSet$backupVersion(String str) {
        this.backupVersion = str;
    }

    public void realmSet$deviceId(String str) {
        this.deviceId = str;
    }

    public void realmSet$deviceSyncToken(String str) {
        this.deviceSyncToken = str;
    }

    public void realmSet$globalBlacklistUnverifiedDevices(boolean z) {
        this.globalBlacklistUnverifiedDevices = z;
    }

    public void realmSet$olmAccountData(String str) {
        this.olmAccountData = str;
    }

    public void realmSet$userId(String str) {
        this.userId = str;
    }

    public /* synthetic */ CryptoMetadataEntity(String str, String str2, String str3, String str4, boolean z, String str5, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            str = null;
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        String str6 = str2;
        if ((i & 4) != 0) {
            str3 = null;
        }
        String str7 = str3;
        if ((i & 8) != 0) {
            str4 = null;
        }
        String str8 = str4;
        boolean z2 = (i & 16) != 0 ? false : z;
        if ((i & 32) != 0) {
            str5 = null;
        }
        this(str, str6, str7, str8, z2, str5);
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

    public final String getDeviceId() {
        return realmGet$deviceId();
    }

    public final void setDeviceId(String str) {
        realmSet$deviceId(str);
    }

    public final String getOlmAccountData() {
        return realmGet$olmAccountData();
    }

    public final void setOlmAccountData(String str) {
        realmSet$olmAccountData(str);
    }

    public final String getDeviceSyncToken() {
        return realmGet$deviceSyncToken();
    }

    public final void setDeviceSyncToken(String str) {
        realmSet$deviceSyncToken(str);
    }

    public final boolean getGlobalBlacklistUnverifiedDevices() {
        return realmGet$globalBlacklistUnverifiedDevices();
    }

    public final void setGlobalBlacklistUnverifiedDevices(boolean z) {
        realmSet$globalBlacklistUnverifiedDevices(z);
    }

    public final String getBackupVersion() {
        return realmGet$backupVersion();
    }

    public final void setBackupVersion(String str) {
        realmSet$backupVersion(str);
    }

    public CryptoMetadataEntity(String str, String str2, String str3, String str4, boolean z, String str5) {
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
        realmSet$userId(str);
        realmSet$deviceId(str2);
        realmSet$olmAccountData(str3);
        realmSet$deviceSyncToken(str4);
        realmSet$globalBlacklistUnverifiedDevices(z);
        realmSet$backupVersion(str5);
    }

    public final OlmAccount getOlmAccount() {
        return (OlmAccount) HelperKt.deserializeFromRealm(realmGet$olmAccountData());
    }

    public final void putOlmAccount(OlmAccount olmAccount) {
        realmSet$olmAccountData(HelperKt.serializeForRealm(olmAccount));
    }
}
