package fr.gouv.tchap.sdk.rest.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0012\b\b\u0018\u00002\u00020\u0001BM\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0016\b\u0002\u0010\u0006\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\u000bJ\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u000fJ\u000b\u0010\u0010\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u0017\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0007HÆ\u0003J\u0010\u0010\u0012\u001a\u0004\u0018\u00010\tHÆ\u0003¢\u0006\u0002\u0010\u0013J\u0010\u0010\u0014\u001a\u0004\u0018\u00010\tHÆ\u0003¢\u0006\u0002\u0010\u0013JV\u0010\u0015\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0016\b\u0002\u0010\u0006\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\tHÆ\u0001¢\u0006\u0002\u0010\u0016J\u0013\u0010\u0017\u001a\u00020\t2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0019\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001a\u001a\u00020\u0005HÖ\u0001R\u0014\u0010\n\u001a\u0004\u0018\u00010\t8\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\fR\u0014\u0010\b\u001a\u0004\u0018\u00010\t8\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\fR\u001e\u0010\u0006\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u00078\u0006X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\rR\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"Lfr/gouv/tchap/sdk/rest/model/MinVersion;", "", "minVersionCode", "", "minVersionName", "", "message", "", "displayOnlyOnce", "", "allowOpeningApp", "(Ljava/lang/Integer;Ljava/lang/String;Ljava/util/Map;Ljava/lang/Boolean;Ljava/lang/Boolean;)V", "Ljava/lang/Boolean;", "Ljava/lang/Integer;", "component1", "()Ljava/lang/Integer;", "component2", "component3", "component4", "()Ljava/lang/Boolean;", "component5", "copy", "(Ljava/lang/Integer;Ljava/lang/String;Ljava/util/Map;Ljava/lang/Boolean;Ljava/lang/Boolean;)Lfr/gouv/tchap/sdk/rest/model/MinVersion;", "equals", "other", "hashCode", "toString", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: MinVersion.kt */
public final class MinVersion {
    @SerializedName("allowOpeningApp")
    public final Boolean allowOpeningApp;
    @SerializedName("displayOnlyOnce")
    public final Boolean displayOnlyOnce;
    @SerializedName("message")
    public final Map<String, String> message;
    @SerializedName("minVersionCode")
    public final Integer minVersionCode;
    @SerializedName("minVersionName")
    public final String minVersionName;

    public MinVersion() {
        this(null, null, null, null, null, 31, null);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Map, code=java.util.Map<java.lang.String, java.lang.String>, for r6v0, types: [java.util.Map] */
    public static /* synthetic */ MinVersion copy$default(MinVersion minVersion, Integer num, String str, Map<String, String> map, Boolean bool, Boolean bool2, int i, Object obj) {
        if ((i & 1) != 0) {
            num = minVersion.minVersionCode;
        }
        if ((i & 2) != 0) {
            str = minVersion.minVersionName;
        }
        String str2 = str;
        if ((i & 4) != 0) {
            map = minVersion.message;
        }
        Map map2 = map;
        if ((i & 8) != 0) {
            bool = minVersion.displayOnlyOnce;
        }
        Boolean bool3 = bool;
        if ((i & 16) != 0) {
            bool2 = minVersion.allowOpeningApp;
        }
        return minVersion.copy(num, str2, map2, bool3, bool2);
    }

    public final Integer component1() {
        return this.minVersionCode;
    }

    public final String component2() {
        return this.minVersionName;
    }

    public final Map<String, String> component3() {
        return this.message;
    }

    public final Boolean component4() {
        return this.displayOnlyOnce;
    }

    public final Boolean component5() {
        return this.allowOpeningApp;
    }

    public final MinVersion copy(Integer num, String str, Map<String, String> map, Boolean bool, Boolean bool2) {
        MinVersion minVersion = new MinVersion(num, str, map, bool, bool2);
        return minVersion;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0038, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2.allowOpeningApp, (java.lang.Object) r3.allowOpeningApp) != false) goto L_0x003d;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof MinVersion) {
                MinVersion minVersion = (MinVersion) obj;
                if (Intrinsics.areEqual((Object) this.minVersionCode, (Object) minVersion.minVersionCode)) {
                    if (Intrinsics.areEqual((Object) this.minVersionName, (Object) minVersion.minVersionName)) {
                        if (Intrinsics.areEqual((Object) this.message, (Object) minVersion.message)) {
                            if (Intrinsics.areEqual((Object) this.displayOnlyOnce, (Object) minVersion.displayOnlyOnce)) {
                            }
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        Integer num = this.minVersionCode;
        int i = 0;
        int hashCode = (num != null ? num.hashCode() : 0) * 31;
        String str = this.minVersionName;
        int hashCode2 = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
        Map<String, String> map = this.message;
        int hashCode3 = (hashCode2 + (map != null ? map.hashCode() : 0)) * 31;
        Boolean bool = this.displayOnlyOnce;
        int hashCode4 = (hashCode3 + (bool != null ? bool.hashCode() : 0)) * 31;
        Boolean bool2 = this.allowOpeningApp;
        if (bool2 != null) {
            i = bool2.hashCode();
        }
        return hashCode4 + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MinVersion(minVersionCode=");
        sb.append(this.minVersionCode);
        sb.append(", minVersionName=");
        sb.append(this.minVersionName);
        sb.append(", message=");
        sb.append(this.message);
        sb.append(", displayOnlyOnce=");
        sb.append(this.displayOnlyOnce);
        sb.append(", allowOpeningApp=");
        sb.append(this.allowOpeningApp);
        sb.append(")");
        return sb.toString();
    }

    public MinVersion(Integer num, String str, Map<String, String> map, Boolean bool, Boolean bool2) {
        this.minVersionCode = num;
        this.minVersionName = str;
        this.message = map;
        this.displayOnlyOnce = bool;
        this.allowOpeningApp = bool2;
    }

    public /* synthetic */ MinVersion(Integer num, String str, Map map, Boolean bool, Boolean bool2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            num = null;
        }
        if ((i & 2) != 0) {
            str = null;
        }
        String str2 = str;
        if ((i & 4) != 0) {
            map = null;
        }
        Map map2 = map;
        if ((i & 8) != 0) {
            bool = null;
        }
        Boolean bool3 = bool;
        if ((i & 16) != 0) {
            bool2 = null;
        }
        this(num, str2, map2, bool3, bool2);
    }
}
