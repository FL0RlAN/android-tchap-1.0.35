package fr.gouv.tchap.sdk.rest.model;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0011\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004J\u000b\u0010\u0005\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0015\u0010\u0006\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\n\u001a\u00020\u000bHÖ\u0001J\t\u0010\f\u001a\u00020\rHÖ\u0001R\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lfr/gouv/tchap/sdk/rest/model/TchapClientConfig;", "", "minimumClientVersion", "Lfr/gouv/tchap/sdk/rest/model/MinClientVersion;", "(Lfr/gouv/tchap/sdk/rest/model/MinClientVersion;)V", "component1", "copy", "equals", "", "other", "hashCode", "", "toString", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: TchapClientConfig.kt */
public final class TchapClientConfig {
    @SerializedName("minimumClientVersion")
    public final MinClientVersion minimumClientVersion;

    public TchapClientConfig() {
        this(null, 1, null);
    }

    public static /* synthetic */ TchapClientConfig copy$default(TchapClientConfig tchapClientConfig, MinClientVersion minClientVersion, int i, Object obj) {
        if ((i & 1) != 0) {
            minClientVersion = tchapClientConfig.minimumClientVersion;
        }
        return tchapClientConfig.copy(minClientVersion);
    }

    public final MinClientVersion component1() {
        return this.minimumClientVersion;
    }

    public final TchapClientConfig copy(MinClientVersion minClientVersion) {
        return new TchapClientConfig(minClientVersion);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0010, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1.minimumClientVersion, (java.lang.Object) ((fr.gouv.tchap.sdk.rest.model.TchapClientConfig) r2).minimumClientVersion) != false) goto L_0x0015;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof TchapClientConfig) {
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        MinClientVersion minClientVersion = this.minimumClientVersion;
        if (minClientVersion != null) {
            return minClientVersion.hashCode();
        }
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TchapClientConfig(minimumClientVersion=");
        sb.append(this.minimumClientVersion);
        sb.append(")");
        return sb.toString();
    }

    public TchapClientConfig(MinClientVersion minClientVersion) {
        this.minimumClientVersion = minClientVersion;
    }

    public /* synthetic */ TchapClientConfig(MinClientVersion minClientVersion, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            minClientVersion = null;
        }
        this(minClientVersion);
    }
}
