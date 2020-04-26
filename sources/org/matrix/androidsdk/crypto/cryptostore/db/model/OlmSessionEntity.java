package org.matrix.androidsdk.crypto.cryptostore.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.internal.RealmObjectProxy;
import io.realm.org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxyInterface;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.crypto.cryptostore.db.HelperKt;
import org.matrix.olm.OlmSession;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0016\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB=\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019J\u0010\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0019R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000b\"\u0004\b\u0013\u0010\rR\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u000b\"\u0004\b\u0015\u0010\rR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u000b\"\u0004\b\u0017\u0010\r¨\u0006\u001e"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/model/OlmSessionEntity;", "Lio/realm/RealmObject;", "primaryKey", "", "sessionId", "deviceKey", "olmSessionData", "lastReceivedMessageTs", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)V", "getDeviceKey", "()Ljava/lang/String;", "setDeviceKey", "(Ljava/lang/String;)V", "getLastReceivedMessageTs", "()J", "setLastReceivedMessageTs", "(J)V", "getOlmSessionData", "setOlmSessionData", "getPrimaryKey", "setPrimaryKey", "getSessionId", "setSessionId", "getOlmSession", "Lorg/matrix/olm/OlmSession;", "putOlmSession", "", "olmSession", "Companion", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: OlmSessionEntity.kt */
public class OlmSessionEntity extends RealmObject implements org_matrix_androidsdk_crypto_cryptostore_db_model_OlmSessionEntityRealmProxyInterface {
    public static final Companion Companion = new Companion(null);
    private String deviceKey;
    private long lastReceivedMessageTs;
    private String olmSessionData;
    @PrimaryKey
    private String primaryKey;
    private String sessionId;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lorg/matrix/androidsdk/crypto/cryptostore/db/model/OlmSessionEntity$Companion;", "", "()V", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: OlmSessionEntity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public OlmSessionEntity() {
        this(null, null, null, null, 0, 31, null);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public String realmGet$deviceKey() {
        return this.deviceKey;
    }

    public long realmGet$lastReceivedMessageTs() {
        return this.lastReceivedMessageTs;
    }

    public String realmGet$olmSessionData() {
        return this.olmSessionData;
    }

    public String realmGet$primaryKey() {
        return this.primaryKey;
    }

    public String realmGet$sessionId() {
        return this.sessionId;
    }

    public void realmSet$deviceKey(String str) {
        this.deviceKey = str;
    }

    public void realmSet$lastReceivedMessageTs(long j) {
        this.lastReceivedMessageTs = j;
    }

    public void realmSet$olmSessionData(String str) {
        this.olmSessionData = str;
    }

    public void realmSet$primaryKey(String str) {
        this.primaryKey = str;
    }

    public void realmSet$sessionId(String str) {
        this.sessionId = str;
    }

    public final String getPrimaryKey() {
        return realmGet$primaryKey();
    }

    public final void setPrimaryKey(String str) {
        Intrinsics.checkParameterIsNotNull(str, "<set-?>");
        realmSet$primaryKey(str);
    }

    public /* synthetic */ OlmSessionEntity(String str, String str2, String str3, String str4, long j, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            str = "";
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
        String str7 = str4;
        if ((i & 16) != 0) {
            j = 0;
        }
        this(str, str5, str6, str7, j);
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public final String getSessionId() {
        return realmGet$sessionId();
    }

    public final void setSessionId(String str) {
        realmSet$sessionId(str);
    }

    public final String getDeviceKey() {
        return realmGet$deviceKey();
    }

    public final void setDeviceKey(String str) {
        realmSet$deviceKey(str);
    }

    public final String getOlmSessionData() {
        return realmGet$olmSessionData();
    }

    public final void setOlmSessionData(String str) {
        realmSet$olmSessionData(str);
    }

    public final long getLastReceivedMessageTs() {
        return realmGet$lastReceivedMessageTs();
    }

    public final void setLastReceivedMessageTs(long j) {
        realmSet$lastReceivedMessageTs(j);
    }

    public OlmSessionEntity(String str, String str2, String str3, String str4, long j) {
        Intrinsics.checkParameterIsNotNull(str, "primaryKey");
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
        realmSet$primaryKey(str);
        realmSet$sessionId(str2);
        realmSet$deviceKey(str3);
        realmSet$olmSessionData(str4);
        realmSet$lastReceivedMessageTs(j);
    }

    public final OlmSession getOlmSession() {
        return (OlmSession) HelperKt.deserializeFromRealm(realmGet$olmSessionData());
    }

    public final void putOlmSession(OlmSession olmSession) {
        realmSet$olmSessionData(HelperKt.serializeForRealm(olmSession));
    }
}
