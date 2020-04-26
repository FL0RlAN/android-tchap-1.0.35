package org.matrix.androidsdk.rest.client;

import android.net.Uri;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.HomeServerConnectionConfig.Builder;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.rest.DefaultRetrofit2CallbackWrapper;
import org.matrix.androidsdk.rest.api.TermsApi;
import org.matrix.androidsdk.rest.model.terms.AcceptTermsBody;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J*\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u000bJ\u001c\u0010\f\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\r0\u000b¨\u0006\u000e"}, d2 = {"Lorg/matrix/androidsdk/rest/client/TermsRestClient;", "Lorg/matrix/androidsdk/RestClient;", "Lorg/matrix/androidsdk/rest/api/TermsApi;", "()V", "agreeToTerms", "", "prefix", "", "agreedUrls", "", "callback", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "get", "Lorg/matrix/androidsdk/rest/model/terms/TermsResponse;", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: TermsRestClient.kt */
public final class TermsRestClient extends RestClient<TermsApi> {
    public TermsRestClient() {
        super(new Builder().withHomeServerUri(Uri.parse("https://foo.bar")).build(), TermsApi.class, "");
    }

    public final void get(String str, ApiCallback<TermsResponse> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, "prefix");
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        TermsApi termsApi = (TermsApi) this.mApi;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("/terms");
        termsApi.getTerms(sb.toString()).enqueue(new DefaultRetrofit2CallbackWrapper(apiCallback));
    }

    public final void agreeToTerms(String str, List<String> list, ApiCallback<Unit> apiCallback) {
        Intrinsics.checkParameterIsNotNull(str, "prefix");
        Intrinsics.checkParameterIsNotNull(list, "agreedUrls");
        Intrinsics.checkParameterIsNotNull(apiCallback, "callback");
        TermsApi termsApi = (TermsApi) this.mApi;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("/terms");
        termsApi.agreeToTerms(sb.toString(), new AcceptTermsBody(list)).enqueue(new DefaultRetrofit2CallbackWrapper(apiCallback));
    }
}
