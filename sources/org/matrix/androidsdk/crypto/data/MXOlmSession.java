package org.matrix.androidsdk.crypto.data;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.olm.OlmSession;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001J\u0006\u0010\u0015\u001a\u00020\u0016J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001R\u001a\u0010\u0004\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0019"}, d2 = {"Lorg/matrix/androidsdk/crypto/data/MXOlmSession;", "", "olmSession", "Lorg/matrix/olm/OlmSession;", "lastReceivedMessageTs", "", "(Lorg/matrix/olm/OlmSession;J)V", "getLastReceivedMessageTs", "()J", "setLastReceivedMessageTs", "(J)V", "getOlmSession", "()Lorg/matrix/olm/OlmSession;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "onMessageReceived", "", "toString", "", "matrix-sdk-crypto_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: MXOlmSession.kt */
public final class MXOlmSession {
    private long lastReceivedMessageTs;
    private final OlmSession olmSession;

    public static /* synthetic */ MXOlmSession copy$default(MXOlmSession mXOlmSession, OlmSession olmSession2, long j, int i, Object obj) {
        if ((i & 1) != 0) {
            olmSession2 = mXOlmSession.olmSession;
        }
        if ((i & 2) != 0) {
            j = mXOlmSession.lastReceivedMessageTs;
        }
        return mXOlmSession.copy(olmSession2, j);
    }

    public final OlmSession component1() {
        return this.olmSession;
    }

    public final long component2() {
        return this.lastReceivedMessageTs;
    }

    public final MXOlmSession copy(OlmSession olmSession2, long j) {
        Intrinsics.checkParameterIsNotNull(olmSession2, "olmSession");
        return new MXOlmSession(olmSession2, j);
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof MXOlmSession) {
                MXOlmSession mXOlmSession = (MXOlmSession) obj;
                if (Intrinsics.areEqual((Object) this.olmSession, (Object) mXOlmSession.olmSession)) {
                    if (this.lastReceivedMessageTs == mXOlmSession.lastReceivedMessageTs) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        OlmSession olmSession2 = this.olmSession;
        int hashCode = (olmSession2 != null ? olmSession2.hashCode() : 0) * 31;
        long j = this.lastReceivedMessageTs;
        return hashCode + ((int) (j ^ (j >>> 32)));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MXOlmSession(olmSession=");
        sb.append(this.olmSession);
        sb.append(", lastReceivedMessageTs=");
        sb.append(this.lastReceivedMessageTs);
        sb.append(")");
        return sb.toString();
    }

    public MXOlmSession(OlmSession olmSession2, long j) {
        Intrinsics.checkParameterIsNotNull(olmSession2, "olmSession");
        this.olmSession = olmSession2;
        this.lastReceivedMessageTs = j;
    }

    public final OlmSession getOlmSession() {
        return this.olmSession;
    }

    public /* synthetic */ MXOlmSession(OlmSession olmSession2, long j, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 2) != 0) {
            j = 0;
        }
        this(olmSession2, j);
    }

    public final long getLastReceivedMessageTs() {
        return this.lastReceivedMessageTs;
    }

    public final void setLastReceivedMessageTs(long j) {
        this.lastReceivedMessageTs = j;
    }

    public final void onMessageReceived() {
        this.lastReceivedMessageTs = System.currentTimeMillis();
    }
}
