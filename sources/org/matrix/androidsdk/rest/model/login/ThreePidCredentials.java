package org.matrix.androidsdk.rest.model.login;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001B)\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0006J\u000b\u0010\u0007\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\b\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\t\u001a\u0004\u0018\u00010\u0003HÆ\u0003J-\u0010\n\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u000e\u001a\u00020\u000fHÖ\u0001J\t\u0010\u0010\u001a\u00020\u0003HÖ\u0001R\u0014\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lorg/matrix/androidsdk/rest/model/login/ThreePidCredentials;", "", "clientSecret", "", "idServer", "sid", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: AuthParamsThreePid.kt */
public final class ThreePidCredentials {
    @SerializedName("client_secret")
    public String clientSecret;
    @SerializedName("id_server")
    public String idServer;
    public String sid;

    public ThreePidCredentials() {
        this(null, null, null, 7, null);
    }

    public static /* synthetic */ ThreePidCredentials copy$default(ThreePidCredentials threePidCredentials, String str, String str2, String str3, int i, Object obj) {
        if ((i & 1) != 0) {
            str = threePidCredentials.clientSecret;
        }
        if ((i & 2) != 0) {
            str2 = threePidCredentials.idServer;
        }
        if ((i & 4) != 0) {
            str3 = threePidCredentials.sid;
        }
        return threePidCredentials.copy(str, str2, str3);
    }

    public final String component1() {
        return this.clientSecret;
    }

    public final String component2() {
        return this.idServer;
    }

    public final String component3() {
        return this.sid;
    }

    public final ThreePidCredentials copy(String str, String str2, String str3) {
        return new ThreePidCredentials(str, str2, str3);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0024, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2.sid, (java.lang.Object) r3.sid) != false) goto L_0x0029;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof ThreePidCredentials) {
                ThreePidCredentials threePidCredentials = (ThreePidCredentials) obj;
                if (Intrinsics.areEqual((Object) this.clientSecret, (Object) threePidCredentials.clientSecret)) {
                    if (Intrinsics.areEqual((Object) this.idServer, (Object) threePidCredentials.idServer)) {
                    }
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        String str = this.clientSecret;
        int i = 0;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.idServer;
        int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.sid;
        if (str3 != null) {
            i = str3.hashCode();
        }
        return hashCode2 + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ThreePidCredentials(clientSecret=");
        sb.append(this.clientSecret);
        sb.append(", idServer=");
        sb.append(this.idServer);
        sb.append(", sid=");
        sb.append(this.sid);
        sb.append(")");
        return sb.toString();
    }

    public ThreePidCredentials(String str, String str2, String str3) {
        this.clientSecret = str;
        this.idServer = str2;
        this.sid = str3;
    }

    public /* synthetic */ ThreePidCredentials(String str, String str2, String str3, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            str = null;
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        if ((i & 4) != 0) {
            str3 = null;
        }
        this(str, str2, str3);
    }
}
