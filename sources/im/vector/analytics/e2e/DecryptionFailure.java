package im.vector.analytics.e2e;

import java.util.Date;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u0018"}, d2 = {"Lim/vector/analytics/e2e/DecryptionFailure;", "", "reason", "Lim/vector/analytics/e2e/DecryptionFailureReason;", "failedEventId", "", "(Lim/vector/analytics/e2e/DecryptionFailureReason;Ljava/lang/String;)V", "getFailedEventId", "()Ljava/lang/String;", "getReason", "()Lim/vector/analytics/e2e/DecryptionFailureReason;", "timestamp", "", "getTimestamp", "()J", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: DecryptionFailure.kt */
public final class DecryptionFailure {
    private final String failedEventId;
    private final DecryptionFailureReason reason;
    private final long timestamp = new Date().getTime();

    public static /* synthetic */ DecryptionFailure copy$default(DecryptionFailure decryptionFailure, DecryptionFailureReason decryptionFailureReason, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            decryptionFailureReason = decryptionFailure.reason;
        }
        if ((i & 2) != 0) {
            str = decryptionFailure.failedEventId;
        }
        return decryptionFailure.copy(decryptionFailureReason, str);
    }

    public final DecryptionFailureReason component1() {
        return this.reason;
    }

    public final String component2() {
        return this.failedEventId;
    }

    public final DecryptionFailure copy(DecryptionFailureReason decryptionFailureReason, String str) {
        Intrinsics.checkParameterIsNotNull(decryptionFailureReason, "reason");
        Intrinsics.checkParameterIsNotNull(str, "failedEventId");
        return new DecryptionFailure(decryptionFailureReason, str);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x001a, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2.failedEventId, (java.lang.Object) r3.failedEventId) != false) goto L_0x001f;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof DecryptionFailure) {
                DecryptionFailure decryptionFailure = (DecryptionFailure) obj;
                if (Intrinsics.areEqual((Object) this.reason, (Object) decryptionFailure.reason)) {
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        DecryptionFailureReason decryptionFailureReason = this.reason;
        int i = 0;
        int hashCode = (decryptionFailureReason != null ? decryptionFailureReason.hashCode() : 0) * 31;
        String str = this.failedEventId;
        if (str != null) {
            i = str.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DecryptionFailure(reason=");
        sb.append(this.reason);
        sb.append(", failedEventId=");
        sb.append(this.failedEventId);
        sb.append(")");
        return sb.toString();
    }

    public DecryptionFailure(DecryptionFailureReason decryptionFailureReason, String str) {
        Intrinsics.checkParameterIsNotNull(decryptionFailureReason, "reason");
        Intrinsics.checkParameterIsNotNull(str, "failedEventId");
        this.reason = decryptionFailureReason;
        this.failedEventId = str;
    }

    public final DecryptionFailureReason getReason() {
        return this.reason;
    }

    public final String getFailedEventId() {
        return this.failedEventId;
    }

    public final long getTimestamp() {
        return this.timestamp;
    }
}
