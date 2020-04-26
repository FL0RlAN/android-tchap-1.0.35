package org.matrix.androidsdk.login;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003HÆ\u0003J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001J\t\u0010\u0011\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u0006\u0012"}, d2 = {"Lorg/matrix/androidsdk/login/UrlAndName;", "", "url", "", "name", "(Ljava/lang/String;Ljava/lang/String;)V", "getName", "()Ljava/lang/String;", "getUrl", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: RegistrationTools.kt */
final class UrlAndName {
    private final String name;
    private final String url;

    public static /* synthetic */ UrlAndName copy$default(UrlAndName urlAndName, String str, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            str = urlAndName.url;
        }
        if ((i & 2) != 0) {
            str2 = urlAndName.name;
        }
        return urlAndName.copy(str, str2);
    }

    public final String component1() {
        return this.url;
    }

    public final String component2() {
        return this.name;
    }

    public final UrlAndName copy(String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        Intrinsics.checkParameterIsNotNull(str2, TermsResponse.NAME);
        return new UrlAndName(str, str2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x001a, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2.name, (java.lang.Object) r3.name) != false) goto L_0x001f;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof UrlAndName) {
                UrlAndName urlAndName = (UrlAndName) obj;
                if (Intrinsics.areEqual((Object) this.url, (Object) urlAndName.url)) {
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        String str = this.url;
        int i = 0;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.name;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UrlAndName(url=");
        sb.append(this.url);
        sb.append(", name=");
        sb.append(this.name);
        sb.append(")");
        return sb.toString();
    }

    public UrlAndName(String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        Intrinsics.checkParameterIsNotNull(str2, TermsResponse.NAME);
        this.url = str;
        this.name = str2;
    }

    public final String getName() {
        return this.name;
    }

    public final String getUrl() {
        return this.url;
    }
}
