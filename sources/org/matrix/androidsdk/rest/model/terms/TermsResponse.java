package org.matrix.androidsdk.rest.model.terms;

import com.google.gson.annotations.SerializedName;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.rest.model.login.LocalizedFlowDataLoginTerms;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\b\b\u0018\u0000 \u00132\u00020\u0001:\u0001\u0013B\u001b\u0012\u0014\b\u0002\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0002\b\u0003\u0018\u00010\u0003¢\u0006\u0002\u0010\u0005J\u0015\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0002\b\u0003\u0018\u00010\u0003HÆ\u0003J\u001f\u0010\u0007\u001a\u00020\u00002\u0014\b\u0002\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0002\b\u0003\u0018\u00010\u0003HÆ\u0001J\u0013\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\u001a\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u00042\b\b\u0002\u0010\u000e\u001a\u00020\u0004J\u001a\u0010\u000f\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u00042\b\b\u0002\u0010\u000e\u001a\u00020\u0004J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0004HÖ\u0001R\u001c\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0002\b\u0003\u0018\u00010\u00038\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lorg/matrix/androidsdk/rest/model/terms/TermsResponse;", "", "policies", "", "", "(Ljava/util/Map;)V", "component1", "copy", "equals", "", "other", "getLocalizedPrivacyPolicies", "Lorg/matrix/androidsdk/rest/model/login/LocalizedFlowDataLoginTerms;", "userLanguage", "defaultLanguage", "getLocalizedTermOfServices", "hashCode", "", "toString", "Companion", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: TermsResponse.kt */
public final class TermsResponse {
    public static final Companion Companion = new Companion(null);
    public static final String NAME = "name";
    public static final String PRIVACY_POLICY = "privacy_policy";
    public static final String TERMS_OF_SERVICE = "terms_of_service";
    public static final String URL = "url";
    public static final String VERSION = "version";
    @SerializedName("policies")
    public final Map<String, ?> policies;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lorg/matrix/androidsdk/rest/model/terms/TermsResponse$Companion;", "", "()V", "NAME", "", "PRIVACY_POLICY", "TERMS_OF_SERVICE", "URL", "VERSION", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: TermsResponse.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    public TermsResponse() {
        this(null, 1, null);
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Map, code=java.util.Map<java.lang.String, ?>, for r1v0, types: [java.util.Map] */
    public static /* synthetic */ TermsResponse copy$default(TermsResponse termsResponse, Map<String, ?> map, int i, Object obj) {
        if ((i & 1) != 0) {
            map = termsResponse.policies;
        }
        return termsResponse.copy(map);
    }

    public final Map<String, ?> component1() {
        return this.policies;
    }

    public final TermsResponse copy(Map<String, ?> map) {
        return new TermsResponse(map);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0010, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1.policies, (java.lang.Object) ((org.matrix.androidsdk.rest.model.terms.TermsResponse) r2).policies) != false) goto L_0x0015;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof TermsResponse) {
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        Map<String, ?> map = this.policies;
        if (map != null) {
            return map.hashCode();
        }
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TermsResponse(policies=");
        sb.append(this.policies);
        sb.append(")");
        return sb.toString();
    }

    public TermsResponse(Map<String, ?> map) {
        this.policies = map;
    }

    public /* synthetic */ TermsResponse(Map map, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            map = null;
        }
        this(map);
    }

    public static /* synthetic */ LocalizedFlowDataLoginTerms getLocalizedTermOfServices$default(TermsResponse termsResponse, String str, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = "en";
        }
        return termsResponse.getLocalizedTermOfServices(str, str2);
    }

    public final LocalizedFlowDataLoginTerms getLocalizedTermOfServices(String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str, "userLanguage");
        Intrinsics.checkParameterIsNotNull(str2, "defaultLanguage");
        Map<String, ?> map = this.policies;
        String str3 = TERMS_OF_SERVICE;
        Object obj = map != null ? map.get(str3) : null;
        if (!(obj instanceof Map)) {
            obj = null;
        }
        Map map2 = (Map) obj;
        if (map2 == null) {
            return null;
        }
        Object obj2 = map2.get(str);
        if (obj2 == null) {
            obj2 = map2.get(str2);
        }
        if (!(obj2 instanceof Map)) {
            obj2 = null;
        }
        Map map3 = (Map) obj2;
        if (map3 == null) {
            return null;
        }
        Object obj3 = map3.get(NAME);
        if (!(obj3 instanceof String)) {
            obj3 = null;
        }
        String str4 = (String) obj3;
        Object obj4 = map3.get("url");
        if (!(obj4 instanceof String)) {
            obj4 = null;
        }
        String str5 = (String) obj4;
        Object obj5 = map2.get(VERSION);
        if (!(obj5 instanceof String)) {
            obj5 = null;
        }
        return new LocalizedFlowDataLoginTerms(str3, (String) obj5, str5, str4);
    }

    public static /* synthetic */ LocalizedFlowDataLoginTerms getLocalizedPrivacyPolicies$default(TermsResponse termsResponse, String str, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = "en";
        }
        return termsResponse.getLocalizedPrivacyPolicies(str, str2);
    }

    public final LocalizedFlowDataLoginTerms getLocalizedPrivacyPolicies(String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str, "userLanguage");
        Intrinsics.checkParameterIsNotNull(str2, "defaultLanguage");
        Map<String, ?> map = this.policies;
        Object obj = map != null ? map.get(PRIVACY_POLICY) : null;
        if (!(obj instanceof Map)) {
            obj = null;
        }
        Map map2 = (Map) obj;
        if (map2 == null) {
            return null;
        }
        Object obj2 = map2.get(str);
        if (obj2 == null) {
            obj2 = map2.get(str2);
        }
        if (!(obj2 instanceof Map)) {
            obj2 = null;
        }
        Map map3 = (Map) obj2;
        if (map3 == null) {
            return null;
        }
        Object obj3 = map3.get(NAME);
        if (!(obj3 instanceof String)) {
            obj3 = null;
        }
        String str3 = (String) obj3;
        Object obj4 = map3.get("url");
        if (!(obj4 instanceof String)) {
            obj4 = null;
        }
        String str4 = (String) obj4;
        Object obj5 = map2.get(VERSION);
        if (!(obj5 instanceof String)) {
            obj5 = null;
        }
        return new LocalizedFlowDataLoginTerms(TERMS_OF_SERVICE, (String) obj5, str4, str3);
    }
}
