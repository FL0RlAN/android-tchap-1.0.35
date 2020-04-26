package org.matrix.androidsdk.rest.model.login;

import android.os.Parcel;
import android.os.Parcelable;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0014\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001B5\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0007J\u000b\u0010\u0012\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0013\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0014\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0015\u001a\u0004\u0018\u00010\u0003HÆ\u0003J9\u0010\u0016\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001J\u0013\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cHÖ\u0003J\t\u0010\u001d\u001a\u00020\u0018HÖ\u0001J\t\u0010\u001e\u001a\u00020\u0003HÖ\u0001J\u0019\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u0018HÖ\u0001R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\t\"\u0004\b\r\u0010\u000bR\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\t\"\u0004\b\u000f\u0010\u000bR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\t\"\u0004\b\u0011\u0010\u000b¨\u0006$"}, d2 = {"Lorg/matrix/androidsdk/rest/model/login/LocalizedFlowDataLoginTerms;", "Landroid/os/Parcelable;", "policyName", "", "version", "localizedUrl", "localizedName", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getLocalizedName", "()Ljava/lang/String;", "setLocalizedName", "(Ljava/lang/String;)V", "getLocalizedUrl", "setLocalizedUrl", "getPolicyName", "setPolicyName", "getVersion", "setVersion", "component1", "component2", "component3", "component4", "copy", "describeContents", "", "equals", "", "other", "", "hashCode", "toString", "writeToParcel", "", "parcel", "Landroid/os/Parcel;", "flags", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: LocalizedFlowDataLoginTerms.kt */
public final class LocalizedFlowDataLoginTerms implements Parcelable {
    public static final android.os.Parcelable.Creator CREATOR = new Creator();
    private String localizedName;
    private String localizedUrl;
    private String policyName;
    private String version;

    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 13})
    public static class Creator implements android.os.Parcelable.Creator {
        public final Object createFromParcel(Parcel parcel) {
            Intrinsics.checkParameterIsNotNull(parcel, "in");
            return new LocalizedFlowDataLoginTerms(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString());
        }

        public final Object[] newArray(int i) {
            return new LocalizedFlowDataLoginTerms[i];
        }
    }

    public LocalizedFlowDataLoginTerms() {
        this(null, null, null, null, 15, null);
    }

    public static /* synthetic */ LocalizedFlowDataLoginTerms copy$default(LocalizedFlowDataLoginTerms localizedFlowDataLoginTerms, String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 1) != 0) {
            str = localizedFlowDataLoginTerms.policyName;
        }
        if ((i & 2) != 0) {
            str2 = localizedFlowDataLoginTerms.version;
        }
        if ((i & 4) != 0) {
            str3 = localizedFlowDataLoginTerms.localizedUrl;
        }
        if ((i & 8) != 0) {
            str4 = localizedFlowDataLoginTerms.localizedName;
        }
        return localizedFlowDataLoginTerms.copy(str, str2, str3, str4);
    }

    public final String component1() {
        return this.policyName;
    }

    public final String component2() {
        return this.version;
    }

    public final String component3() {
        return this.localizedUrl;
    }

    public final String component4() {
        return this.localizedName;
    }

    public final LocalizedFlowDataLoginTerms copy(String str, String str2, String str3, String str4) {
        return new LocalizedFlowDataLoginTerms(str, str2, str3, str4);
    }

    public int describeContents() {
        return 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002e, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2.localizedName, (java.lang.Object) r3.localizedName) != false) goto L_0x0033;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof LocalizedFlowDataLoginTerms) {
                LocalizedFlowDataLoginTerms localizedFlowDataLoginTerms = (LocalizedFlowDataLoginTerms) obj;
                if (Intrinsics.areEqual((Object) this.policyName, (Object) localizedFlowDataLoginTerms.policyName)) {
                    if (Intrinsics.areEqual((Object) this.version, (Object) localizedFlowDataLoginTerms.version)) {
                        if (Intrinsics.areEqual((Object) this.localizedUrl, (Object) localizedFlowDataLoginTerms.localizedUrl)) {
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        String str = this.policyName;
        int i = 0;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.version;
        int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.localizedUrl;
        int hashCode3 = (hashCode2 + (str3 != null ? str3.hashCode() : 0)) * 31;
        String str4 = this.localizedName;
        if (str4 != null) {
            i = str4.hashCode();
        }
        return hashCode3 + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LocalizedFlowDataLoginTerms(policyName=");
        sb.append(this.policyName);
        sb.append(", version=");
        sb.append(this.version);
        sb.append(", localizedUrl=");
        sb.append(this.localizedUrl);
        sb.append(", localizedName=");
        sb.append(this.localizedName);
        sb.append(")");
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        Intrinsics.checkParameterIsNotNull(parcel, "parcel");
        parcel.writeString(this.policyName);
        parcel.writeString(this.version);
        parcel.writeString(this.localizedUrl);
        parcel.writeString(this.localizedName);
    }

    public LocalizedFlowDataLoginTerms(String str, String str2, String str3, String str4) {
        this.policyName = str;
        this.version = str2;
        this.localizedUrl = str3;
        this.localizedName = str4;
    }

    public /* synthetic */ LocalizedFlowDataLoginTerms(String str, String str2, String str3, String str4, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            str = null;
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        if ((i & 4) != 0) {
            str3 = null;
        }
        if ((i & 8) != 0) {
            str4 = null;
        }
        this(str, str2, str3, str4);
    }

    public final String getPolicyName() {
        return this.policyName;
    }

    public final void setPolicyName(String str) {
        this.policyName = str;
    }

    public final String getVersion() {
        return this.version;
    }

    public final void setVersion(String str) {
        this.version = str;
    }

    public final String getLocalizedUrl() {
        return this.localizedUrl;
    }

    public final void setLocalizedUrl(String str) {
        this.localizedUrl = str;
    }

    public final String getLocalizedName() {
        return this.localizedName;
    }

    public final void setLocalizedName(String str) {
        this.localizedName = str;
    }
}
