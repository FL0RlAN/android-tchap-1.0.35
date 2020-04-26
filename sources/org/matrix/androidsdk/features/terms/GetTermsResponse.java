package org.matrix.androidsdk.features.terms;

import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\u000f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005HÆ\u0003J#\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0006HÖ\u0001R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0015"}, d2 = {"Lorg/matrix/androidsdk/features/terms/GetTermsResponse;", "", "serverResponse", "Lorg/matrix/androidsdk/rest/model/terms/TermsResponse;", "alreadyAcceptedTermUrls", "", "", "(Lorg/matrix/androidsdk/rest/model/terms/TermsResponse;Ljava/util/Set;)V", "getAlreadyAcceptedTermUrls", "()Ljava/util/Set;", "getServerResponse", "()Lorg/matrix/androidsdk/rest/model/terms/TermsResponse;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: GetTermsResponse.kt */
public final class GetTermsResponse {
    private final Set<String> alreadyAcceptedTermUrls;
    private final TermsResponse serverResponse;

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Set, code=java.util.Set<java.lang.String>, for r2v0, types: [java.util.Set] */
    public static /* synthetic */ GetTermsResponse copy$default(GetTermsResponse getTermsResponse, TermsResponse termsResponse, Set<String> set, int i, Object obj) {
        if ((i & 1) != 0) {
            termsResponse = getTermsResponse.serverResponse;
        }
        if ((i & 2) != 0) {
            set = getTermsResponse.alreadyAcceptedTermUrls;
        }
        return getTermsResponse.copy(termsResponse, set);
    }

    public final TermsResponse component1() {
        return this.serverResponse;
    }

    public final Set<String> component2() {
        return this.alreadyAcceptedTermUrls;
    }

    public final GetTermsResponse copy(TermsResponse termsResponse, Set<String> set) {
        Intrinsics.checkParameterIsNotNull(termsResponse, "serverResponse");
        Intrinsics.checkParameterIsNotNull(set, "alreadyAcceptedTermUrls");
        return new GetTermsResponse(termsResponse, set);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x001a, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2.alreadyAcceptedTermUrls, (java.lang.Object) r3.alreadyAcceptedTermUrls) != false) goto L_0x001f;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof GetTermsResponse) {
                GetTermsResponse getTermsResponse = (GetTermsResponse) obj;
                if (Intrinsics.areEqual((Object) this.serverResponse, (Object) getTermsResponse.serverResponse)) {
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        TermsResponse termsResponse = this.serverResponse;
        int i = 0;
        int hashCode = (termsResponse != null ? termsResponse.hashCode() : 0) * 31;
        Set<String> set = this.alreadyAcceptedTermUrls;
        if (set != null) {
            i = set.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GetTermsResponse(serverResponse=");
        sb.append(this.serverResponse);
        sb.append(", alreadyAcceptedTermUrls=");
        sb.append(this.alreadyAcceptedTermUrls);
        sb.append(")");
        return sb.toString();
    }

    public GetTermsResponse(TermsResponse termsResponse, Set<String> set) {
        Intrinsics.checkParameterIsNotNull(termsResponse, "serverResponse");
        Intrinsics.checkParameterIsNotNull(set, "alreadyAcceptedTermUrls");
        this.serverResponse = termsResponse;
        this.alreadyAcceptedTermUrls = set;
    }

    public final TermsResponse getServerResponse() {
        return this.serverResponse;
    }

    public final Set<String> getAlreadyAcceptedTermUrls() {
        return this.alreadyAcceptedTermUrls;
    }
}
