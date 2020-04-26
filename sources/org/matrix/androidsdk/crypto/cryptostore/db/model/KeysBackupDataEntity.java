package org.matrix.androidsdk.crypto.cryptostore.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.internal.RealmObjectProxy;
import io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0010\b\u0016\u0018\u00002\u00020\u0001B'\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0007R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001e\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u0010\n\u0002\u0010\u0010\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014¨\u0006\u0015"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/model/KeysBackupDataEntity;", "Lio/realm/RealmObject;", "primaryKey", "", "backupLastServerHash", "", "backupLastServerNumberOfKeys", "(ILjava/lang/String;Ljava/lang/Integer;)V", "getBackupLastServerHash", "()Ljava/lang/String;", "setBackupLastServerHash", "(Ljava/lang/String;)V", "getBackupLastServerNumberOfKeys", "()Ljava/lang/Integer;", "setBackupLastServerNumberOfKeys", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "getPrimaryKey", "()I", "setPrimaryKey", "(I)V", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: KeysBackupDataEntity.kt */
public class KeysBackupDataEntity extends RealmObject implements org_matrix_androidsdk_crypto_cryptostore_db_model_KeysBackupDataEntityRealmProxyInterface {
    private String backupLastServerHash;
    private Integer backupLastServerNumberOfKeys;
    @PrimaryKey
    private int primaryKey;

    public KeysBackupDataEntity() {
        this(0, null, null, 7, null);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public String realmGet$backupLastServerHash() {
        return this.backupLastServerHash;
    }

    public Integer realmGet$backupLastServerNumberOfKeys() {
        return this.backupLastServerNumberOfKeys;
    }

    public int realmGet$primaryKey() {
        return this.primaryKey;
    }

    public void realmSet$backupLastServerHash(String str) {
        this.backupLastServerHash = str;
    }

    public void realmSet$backupLastServerNumberOfKeys(Integer num) {
        this.backupLastServerNumberOfKeys = num;
    }

    public void realmSet$primaryKey(int i) {
        this.primaryKey = i;
    }

    public final int getPrimaryKey() {
        return realmGet$primaryKey();
    }

    public final void setPrimaryKey(int i) {
        realmSet$primaryKey(i);
    }

    public /* synthetic */ KeysBackupDataEntity(int i, String str, Integer num, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        if ((i2 & 2) != 0) {
            str = null;
        }
        if ((i2 & 4) != 0) {
            num = null;
        }
        this(i, str, num);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public final String getBackupLastServerHash() {
        return realmGet$backupLastServerHash();
    }

    public final void setBackupLastServerHash(String str) {
        realmSet$backupLastServerHash(str);
    }

    public final Integer getBackupLastServerNumberOfKeys() {
        return realmGet$backupLastServerNumberOfKeys();
    }

    public final void setBackupLastServerNumberOfKeys(Integer num) {
        realmSet$backupLastServerNumberOfKeys(num);
    }

    public KeysBackupDataEntity(int i, String str, Integer num) {
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
        realmSet$primaryKey(i);
        realmSet$backupLastServerHash(str);
        realmSet$backupLastServerNumberOfKeys(num);
    }
}
