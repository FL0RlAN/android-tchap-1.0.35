package org.matrix.androidsdk.crypto.cryptostore.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.internal.RealmObjectProxy;
import io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface;
import kotlin.Metadata;
import org.matrix.androidsdk.crypto.cryptostore.db.HelperKt;
import org.matrix.androidsdk.crypto.data.MXOlmInboundGroupSession2;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0016\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB?\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019J\u0010\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0019R\u001a\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R \u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000f\"\u0004\b\u0013\u0010\u0011R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u000f\"\u0004\b\u0015\u0010\u0011R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u000f\"\u0004\b\u0017\u0010\u0011¨\u0006\u001e"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/model/OlmInboundGroupSessionEntity;", "Lio/realm/RealmObject;", "primaryKey", "", "sessionId", "senderKey", "olmInboundGroupSessionData", "backedUp", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V", "getBackedUp", "()Z", "setBackedUp", "(Z)V", "getOlmInboundGroupSessionData", "()Ljava/lang/String;", "setOlmInboundGroupSessionData", "(Ljava/lang/String;)V", "getPrimaryKey", "setPrimaryKey", "getSenderKey", "setSenderKey", "getSessionId", "setSessionId", "getInboundGroupSession", "Lorg/matrix/androidsdk/crypto/data/MXOlmInboundGroupSession2;", "putInboundGroupSession", "", "mxOlmInboundGroupSession2", "Companion", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: OlmInboundGroupSessionEntity.kt */
public class OlmInboundGroupSessionEntity extends RealmObject implements org_matrix_androidsdk_crypto_cryptostore_db_model_OlmInboundGroupSessionEntityRealmProxyInterface {
    public static final Companion Companion = new Companion(null);
    private boolean backedUp;
    private String olmInboundGroupSessionData;
    @PrimaryKey
    private String primaryKey;
    private String senderKey;
    private String sessionId;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/model/OlmInboundGroupSessionEntity$Companion;", "", "()V", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: OlmInboundGroupSessionEntity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public OlmInboundGroupSessionEntity() {
        this(null, null, null, null, false, 31, null);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public boolean realmGet$backedUp() {
        return this.backedUp;
    }

    public String realmGet$olmInboundGroupSessionData() {
        return this.olmInboundGroupSessionData;
    }

    public String realmGet$primaryKey() {
        return this.primaryKey;
    }

    public String realmGet$senderKey() {
        return this.senderKey;
    }

    public String realmGet$sessionId() {
        return this.sessionId;
    }

    public void realmSet$backedUp(boolean z) {
        this.backedUp = z;
    }

    public void realmSet$olmInboundGroupSessionData(String str) {
        this.olmInboundGroupSessionData = str;
    }

    public void realmSet$primaryKey(String str) {
        this.primaryKey = str;
    }

    public void realmSet$senderKey(String str) {
        this.senderKey = str;
    }

    public void realmSet$sessionId(String str) {
        this.sessionId = str;
    }

    public /* synthetic */ OlmInboundGroupSessionEntity(String str, String str2, String str3, String str4, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            str = null;
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        String str5 = str2;
        if ((i & 4) != 0) {
            str3 = null;
        }
        String str6 = str3;
        if ((i & 8) != 0) {
            str4 = null;
        }
        this(str, str5, str6, str4, (i & 16) != 0 ? false : z);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public final String getPrimaryKey() {
        return realmGet$primaryKey();
    }

    public final void setPrimaryKey(String str) {
        realmSet$primaryKey(str);
    }

    public final String getSessionId() {
        return realmGet$sessionId();
    }

    public final void setSessionId(String str) {
        realmSet$sessionId(str);
    }

    public final String getSenderKey() {
        return realmGet$senderKey();
    }

    public final void setSenderKey(String str) {
        realmSet$senderKey(str);
    }

    public final String getOlmInboundGroupSessionData() {
        return realmGet$olmInboundGroupSessionData();
    }

    public final void setOlmInboundGroupSessionData(String str) {
        realmSet$olmInboundGroupSessionData(str);
    }

    public final boolean getBackedUp() {
        return realmGet$backedUp();
    }

    public final void setBackedUp(boolean z) {
        realmSet$backedUp(z);
    }

    public OlmInboundGroupSessionEntity(String str, String str2, String str3, String str4, boolean z) {
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
        realmSet$primaryKey(str);
        realmSet$sessionId(str2);
        realmSet$senderKey(str3);
        realmSet$olmInboundGroupSessionData(str4);
        realmSet$backedUp(z);
    }

    public final MXOlmInboundGroupSession2 getInboundGroupSession() {
        return (MXOlmInboundGroupSession2) HelperKt.deserializeFromRealm(realmGet$olmInboundGroupSessionData());
    }

    public final void putInboundGroupSession(MXOlmInboundGroupSession2 mXOlmInboundGroupSession2) {
        realmSet$olmInboundGroupSessionData(HelperKt.serializeForRealm(mXOlmInboundGroupSession2));
    }
}
