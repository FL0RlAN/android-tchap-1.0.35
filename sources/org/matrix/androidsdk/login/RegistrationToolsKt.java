package org.matrix.androidsdk.login;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.rest.client.LoginRestClient;
import org.matrix.androidsdk.rest.model.login.LocalizedFlowDataLoginTerms;
import org.matrix.androidsdk.rest.model.login.RegistrationFlowResponse;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0014\u0010\u0000\u001a\u0004\u0018\u00010\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\u0002\u001a\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u001a*\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\u0005¨\u0006\r"}, d2 = {"extractUrlAndName", "Lorg/matrix/androidsdk/login/UrlAndName;", "policyData", "", "getCaptchaPublicKey", "", "registrationFlowResponse", "Lorg/matrix/androidsdk/rest/model/login/RegistrationFlowResponse;", "getLocalizedLoginTerms", "", "Lorg/matrix/androidsdk/rest/model/login/LocalizedFlowDataLoginTerms;", "userLanguage", "defaultLanguage", "matrix-sdk_release"}, k = 2, mv = {1, 1, 13})
/* compiled from: RegistrationTools.kt */
public final class RegistrationToolsKt {
    public static final String getCaptchaPublicKey(RegistrationFlowResponse registrationFlowResponse) {
        String str = null;
        if (registrationFlowResponse == null) {
            return str;
        }
        Map<String, Object> map = registrationFlowResponse.params;
        if (map == null) {
            return str;
        }
        Object obj = map.get(LoginRestClient.LOGIN_FLOW_TYPE_RECAPTCHA);
        if (!(obj instanceof Map)) {
            return str;
        }
        Object obj2 = ((Map) obj).get("public_key");
        if (obj2 != null) {
            return (String) obj2;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
    }

    public static /* synthetic */ List getLocalizedLoginTerms$default(RegistrationFlowResponse registrationFlowResponse, String str, String str2, int i, Object obj) {
        String str3 = "en";
        if ((i & 2) != 0) {
            str = str3;
        }
        if ((i & 4) != 0) {
            str2 = str3;
        }
        return getLocalizedLoginTerms(registrationFlowResponse, str, str2);
    }

    public static final List<LocalizedFlowDataLoginTerms> getLocalizedLoginTerms(RegistrationFlowResponse registrationFlowResponse, String str, String str2) {
        String str3 = TermsResponse.VERSION;
        Intrinsics.checkParameterIsNotNull(str, "userLanguage");
        Intrinsics.checkParameterIsNotNull(str2, "defaultLanguage");
        ArrayList arrayList = new ArrayList();
        if (registrationFlowResponse != null) {
            try {
                Map<String, Object> map = registrationFlowResponse.params;
                if (map != null) {
                    Object obj = map.get(LoginRestClient.LOGIN_FLOW_TYPE_TERMS);
                    if (obj instanceof Map) {
                        Object obj2 = ((Map) obj).get("policies");
                        if (obj2 instanceof Map) {
                            for (Object next : ((Map) obj2).keySet()) {
                                LocalizedFlowDataLoginTerms localizedFlowDataLoginTerms = new LocalizedFlowDataLoginTerms(null, null, null, null, 15, null);
                                if (next != null) {
                                    localizedFlowDataLoginTerms.setPolicyName((String) next);
                                    Object obj3 = ((Map) obj2).get(next);
                                    if (obj3 instanceof Map) {
                                        localizedFlowDataLoginTerms.setVersion((String) ((Map) obj3).get(str3));
                                        UrlAndName urlAndName = null;
                                        UrlAndName urlAndName2 = null;
                                        UrlAndName urlAndName3 = null;
                                        for (Object next2 : ((Map) obj3).keySet()) {
                                            if (!Intrinsics.areEqual(next2, (Object) str3)) {
                                                if (Intrinsics.areEqual(next2, (Object) str)) {
                                                    urlAndName = extractUrlAndName(((Map) obj3).get(next2));
                                                } else if (Intrinsics.areEqual(next2, (Object) str2)) {
                                                    urlAndName2 = extractUrlAndName(((Map) obj3).get(next2));
                                                } else if (urlAndName3 == null) {
                                                    urlAndName3 = extractUrlAndName(((Map) obj3).get(next2));
                                                }
                                            }
                                        }
                                        if (urlAndName != null) {
                                            if (urlAndName == null) {
                                                Intrinsics.throwNpe();
                                            }
                                            localizedFlowDataLoginTerms.setLocalizedUrl(urlAndName.getUrl());
                                            if (urlAndName == null) {
                                                Intrinsics.throwNpe();
                                            }
                                            localizedFlowDataLoginTerms.setLocalizedName(urlAndName.getName());
                                        } else if (urlAndName2 != null) {
                                            if (urlAndName2 == null) {
                                                Intrinsics.throwNpe();
                                            }
                                            localizedFlowDataLoginTerms.setLocalizedUrl(urlAndName2.getUrl());
                                            if (urlAndName2 == null) {
                                                Intrinsics.throwNpe();
                                            }
                                            localizedFlowDataLoginTerms.setLocalizedName(urlAndName2.getName());
                                        } else if (urlAndName3 != null) {
                                            if (urlAndName3 == null) {
                                                Intrinsics.throwNpe();
                                            }
                                            localizedFlowDataLoginTerms.setLocalizedUrl(urlAndName3.getUrl());
                                            if (urlAndName3 == null) {
                                                Intrinsics.throwNpe();
                                            }
                                            localizedFlowDataLoginTerms.setLocalizedName(urlAndName3.getName());
                                        }
                                    }
                                    arrayList.add(localizedFlowDataLoginTerms);
                                } else {
                                    throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
                                }
                            }
                        }
                    }
                }
            } catch (Exception unused) {
            }
        }
        return arrayList;
    }

    private static final UrlAndName extractUrlAndName(Object obj) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            String str = (String) map.get("url");
            String str2 = (String) map.get(TermsResponse.NAME);
            if (!(str == null || str2 == null)) {
                return new UrlAndName(str, str2);
            }
        }
        return null;
    }
}
