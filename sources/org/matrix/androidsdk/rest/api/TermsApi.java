package org.matrix.androidsdk.rest.api;

import kotlin.Metadata;
import kotlin.Unit;
import org.matrix.androidsdk.rest.model.terms.AcceptTermsBody;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\"\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\bH'J\u0018\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H'¨\u0006\u000b"}, d2 = {"Lorg/matrix/androidsdk/rest/api/TermsApi;", "", "agreeToTerms", "Lretrofit2/Call;", "", "url", "", "params", "Lorg/matrix/androidsdk/rest/model/terms/AcceptTermsBody;", "getTerms", "Lorg/matrix/androidsdk/rest/model/terms/TermsResponse;", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: TermsApi.kt */
public interface TermsApi {
    @POST
    Call<Unit> agreeToTerms(@Url String str, @Body AcceptTermsBody acceptTermsBody);

    @GET
    Call<TermsResponse> getTerms(@Url String str);
}
