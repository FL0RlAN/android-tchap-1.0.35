package org.matrix.androidsdk.features.terms;

import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.rest.client.TermsRestClient;
import org.matrix.androidsdk.rest.model.sync.AccountDataElement;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\b\u0003\u0018\u0000 \u00162\u00020\u0001:\u0002\u0016\u0017B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J<\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\b0\u0011J$\u0010\u0012\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00130\u0011J\u000e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\f0\u0015H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lorg/matrix/androidsdk/features/terms/TermsManager;", "", "mxSession", "Lorg/matrix/androidsdk/MXSession;", "(Lorg/matrix/androidsdk/MXSession;)V", "termsRestClient", "Lorg/matrix/androidsdk/rest/client/TermsRestClient;", "agreeToTerms", "", "serviceType", "Lorg/matrix/androidsdk/features/terms/TermsManager$ServiceType;", "baseUrl", "", "agreedUrls", "", "token", "callback", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "get", "Lorg/matrix/androidsdk/features/terms/GetTermsResponse;", "getAlreadyAcceptedTermUrlsFromAccountData", "", "Companion", "ServiceType", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: TermsManager.kt */
public final class TermsManager {
    public static final Companion Companion = new Companion(null);
    private static final String LOG_TAG = "TermsManager";
    /* access modifiers changed from: private */
    public final MXSession mxSession;
    private final TermsRestClient termsRestClient = new TermsRestClient();

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lorg/matrix/androidsdk/features/terms/TermsManager$Companion;", "", "()V", "LOG_TAG", "", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: TermsManager.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004¨\u0006\u0005"}, d2 = {"Lorg/matrix/androidsdk/features/terms/TermsManager$ServiceType;", "", "(Ljava/lang/String;I)V", "IntegrationManager", "IdentityService", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
    /* compiled from: TermsManager.kt */
    public enum ServiceType {
        IntegrationManager,
        IdentityService
    }

    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 13})
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0 = new int[ServiceType.values().length];
        public static final /* synthetic */ int[] $EnumSwitchMapping$1 = new int[ServiceType.values().length];

        static {
            $EnumSwitchMapping$0[ServiceType.IntegrationManager.ordinal()] = 1;
            $EnumSwitchMapping$0[ServiceType.IdentityService.ordinal()] = 2;
            $EnumSwitchMapping$1[ServiceType.IntegrationManager.ordinal()] = 1;
            $EnumSwitchMapping$1[ServiceType.IdentityService.ordinal()] = 2;
        }
    }

    public TermsManager(MXSession mXSession) {
        Intrinsics.checkParameterIsNotNull(mXSession, "mxSession");
        this.mxSession = mXSession;
    }

    public final void get(ServiceType serviceType, String str, ApiCallback<GetTermsResponse> apiCallback) {
        String str2;
        Intrinsics.checkParameterIsNotNull(serviceType, "serviceType");
        Intrinsics.checkParameterIsNotNull(str, "baseUrl");
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        int i = WhenMappings.$EnumSwitchMapping$0[serviceType.ordinal()];
        if (i == 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(RestClient.URI_INTEGRATION_MANAGER_PATH);
            str2 = sb.toString();
        } else if (i == 2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(RestClient.URI_IDENTITY_PATH_V2);
            str2 = sb2.toString();
        } else {
            throw new NoWhenBranchMatchedException();
        }
        this.termsRestClient.get(str2, new TermsManager$get$1(this, apiCallback, apiCallback));
    }

    public final void agreeToTerms(ServiceType serviceType, String str, List<String> list, String str2, ApiCallback<Unit> apiCallback) {
        String str3;
        Intrinsics.checkParameterIsNotNull(serviceType, "serviceType");
        Intrinsics.checkParameterIsNotNull(str, "baseUrl");
        Intrinsics.checkParameterIsNotNull(list, "agreedUrls");
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        this.termsRestClient.setAccessToken(str2);
        int i = WhenMappings.$EnumSwitchMapping$1[serviceType.ordinal()];
        if (i == 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(RestClient.URI_INTEGRATION_MANAGER_PATH);
            str3 = sb.toString();
        } else if (i == 2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(RestClient.URI_IDENTITY_PATH_V2);
            str3 = sb2.toString();
        } else {
            throw new NoWhenBranchMatchedException();
        }
        this.termsRestClient.agreeToTerms(str3, list, new TermsManager$agreeToTerms$1(this, list, apiCallback, apiCallback));
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0029  */
    /* JADX WARNING: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0023  */
    public final Set<String> getAlreadyAcceptedTermUrlsFromAccountData() {
        Object obj;
        AccountDataElement accountDataElement = this.mxSession.getDataHandler().getStore().getAccountDataElement(AccountDataElement.ACCOUNT_DATA_ACCEPTED_TERMS);
        if (accountDataElement != null) {
            Map<String, Object> map = accountDataElement.content;
            if (map != null) {
                obj = map.get(AccountDataElement.ACCOUNT_DATA_KEY_ACCEPTED_TERMS);
                if (!(obj instanceof Set)) {
                    obj = null;
                }
                Set<String> set = (Set) obj;
                return set == null ? set : SetsKt.emptySet();
            }
        }
        obj = null;
        if (!(obj instanceof Set)) {
        }
        Set<String> set2 = (Set) obj;
        if (set2 == null) {
        }
    }
}
