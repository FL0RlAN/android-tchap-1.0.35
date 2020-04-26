package fr.gouv.tchap.sdk.rest.model;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B)\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0006J\u000b\u0010\u0007\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\b\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\t\u001a\u0004\u0018\u00010\u0003HÆ\u0003J-\u0010\n\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u000e\u001a\u00020\u000fHÖ\u0001J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001R\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00038\u0006X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lfr/gouv/tchap/sdk/rest/model/MinClientVersion;", "", "criticalMinVersion", "Lfr/gouv/tchap/sdk/rest/model/MinVersion;", "mandatoryMinVersion", "infoMinVersion", "(Lfr/gouv/tchap/sdk/rest/model/MinVersion;Lfr/gouv/tchap/sdk/rest/model/MinVersion;Lfr/gouv/tchap/sdk/rest/model/MinVersion;)V", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: MinClientVersion.kt */
public final class MinClientVersion {
    @SerializedName("critical")
    public final MinVersion criticalMinVersion;
    @SerializedName("info")
    public final MinVersion infoMinVersion;
    @SerializedName("mandatory")
    public final MinVersion mandatoryMinVersion;

    public MinClientVersion() {
        this(null, null, null, 7, null);
    }

    public static /* synthetic */ MinClientVersion copy$default(MinClientVersion minClientVersion, MinVersion minVersion, MinVersion minVersion2, MinVersion minVersion3, int i, Object obj) {
        if ((i & 1) != 0) {
            minVersion = minClientVersion.criticalMinVersion;
        }
        if ((i & 2) != 0) {
            minVersion2 = minClientVersion.mandatoryMinVersion;
        }
        if ((i & 4) != 0) {
            minVersion3 = minClientVersion.infoMinVersion;
        }
        return minClientVersion.copy(minVersion, minVersion2, minVersion3);
    }

    public final MinVersion component1() {
        return this.criticalMinVersion;
    }

    public final MinVersion component2() {
        return this.mandatoryMinVersion;
    }

    public final MinVersion component3() {
        return this.infoMinVersion;
    }

    public final MinClientVersion copy(MinVersion minVersion, MinVersion minVersion2, MinVersion minVersion3) {
        return new MinClientVersion(minVersion, minVersion2, minVersion3);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0024, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2.infoMinVersion, (java.lang.Object) r3.infoMinVersion) != false) goto L_0x0029;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof MinClientVersion) {
                MinClientVersion minClientVersion = (MinClientVersion) obj;
                if (Intrinsics.areEqual((Object) this.criticalMinVersion, (Object) minClientVersion.criticalMinVersion)) {
                    if (Intrinsics.areEqual((Object) this.mandatoryMinVersion, (Object) minClientVersion.mandatoryMinVersion)) {
                    }
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        MinVersion minVersion = this.criticalMinVersion;
        int i = 0;
        int hashCode = (minVersion != null ? minVersion.hashCode() : 0) * 31;
        MinVersion minVersion2 = this.mandatoryMinVersion;
        int hashCode2 = (hashCode + (minVersion2 != null ? minVersion2.hashCode() : 0)) * 31;
        MinVersion minVersion3 = this.infoMinVersion;
        if (minVersion3 != null) {
            i = minVersion3.hashCode();
        }
        return hashCode2 + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MinClientVersion(criticalMinVersion=");
        sb.append(this.criticalMinVersion);
        sb.append(", mandatoryMinVersion=");
        sb.append(this.mandatoryMinVersion);
        sb.append(", infoMinVersion=");
        sb.append(this.infoMinVersion);
        sb.append(")");
        return sb.toString();
    }

    public MinClientVersion(MinVersion minVersion, MinVersion minVersion2, MinVersion minVersion3) {
        this.criticalMinVersion = minVersion;
        this.mandatoryMinVersion = minVersion2;
        this.infoMinVersion = minVersion3;
    }

    public /* synthetic */ MinClientVersion(MinVersion minVersion, MinVersion minVersion2, MinVersion minVersion3, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            minVersion = null;
        }
        if ((i & 2) != 0) {
            minVersion2 = null;
        }
        if ((i & 4) != 0) {
            minVersion3 = null;
        }
        this(minVersion, minVersion2, minVersion3);
    }
}
