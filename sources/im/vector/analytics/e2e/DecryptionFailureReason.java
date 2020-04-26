package im.vector.analytics.e2e;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n¨\u0006\u000b"}, d2 = {"Lim/vector/analytics/e2e/DecryptionFailureReason;", "", "value", "", "(Ljava/lang/String;ILjava/lang/String;)V", "getValue", "()Ljava/lang/String;", "UNSPECIFIED", "OLM_KEYS_NOT_SENT", "OLM_INDEX_ERROR", "UNEXPECTED", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: DecryptionFailure.kt */
public enum DecryptionFailureReason {
    UNSPECIFIED("unspecified"),
    OLM_KEYS_NOT_SENT("olmKeysNotSent"),
    OLM_INDEX_ERROR("olmIndexError"),
    UNEXPECTED("unexpected");
    
    private final String value;

    private DecryptionFailureReason(String str) {
        this.value = str;
    }

    public final String getValue() {
        return this.value;
    }
}
