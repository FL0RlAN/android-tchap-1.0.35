package org.matrix.androidsdk.rest.model.terms;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0002\u0010\u0005J\u000f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\u0019\u0010\u0007\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0001J\u0013\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u000b\u001a\u00020\fHÖ\u0001J\t\u0010\r\u001a\u00020\u0004HÖ\u0001R\u0016\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00038\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lorg/matrix/androidsdk/rest/model/terms/AcceptTermsBody;", "", "acceptedTermUrls", "", "", "(Ljava/util/List;)V", "component1", "copy", "equals", "", "other", "hashCode", "", "toString", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: AcceptTermsBody.kt */
public final class AcceptTermsBody {
    @SerializedName("user_accepts")
    public final List<String> acceptedTermUrls;

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.String>, for r1v0, types: [java.util.List] */
    public static /* synthetic */ AcceptTermsBody copy$default(AcceptTermsBody acceptTermsBody, List<String> list, int i, Object obj) {
        if ((i & 1) != 0) {
            list = acceptTermsBody.acceptedTermUrls;
        }
        return acceptTermsBody.copy(list);
    }

    public final List<String> component1() {
        return this.acceptedTermUrls;
    }

    public final AcceptTermsBody copy(List<String> list) {
        Intrinsics.checkParameterIsNotNull(list, "acceptedTermUrls");
        return new AcceptTermsBody(list);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0010, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1.acceptedTermUrls, (java.lang.Object) ((org.matrix.androidsdk.rest.model.terms.AcceptTermsBody) r2).acceptedTermUrls) != false) goto L_0x0015;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof AcceptTermsBody) {
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        List<String> list = this.acceptedTermUrls;
        if (list != null) {
            return list.hashCode();
        }
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AcceptTermsBody(acceptedTermUrls=");
        sb.append(this.acceptedTermUrls);
        sb.append(")");
        return sb.toString();
    }

    public AcceptTermsBody(List<String> list) {
        Intrinsics.checkParameterIsNotNull(list, "acceptedTermUrls");
        this.acceptedTermUrls = list;
    }
}
