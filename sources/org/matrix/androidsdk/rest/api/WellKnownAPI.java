package org.matrix.androidsdk.rest.api;

import kotlin.Metadata;
import org.matrix.androidsdk.rest.model.WellKnown;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H'¨\u0006\u0007"}, d2 = {"Lorg/matrix/androidsdk/rest/api/WellKnownAPI;", "", "getWellKnown", "Lretrofit2/Call;", "Lorg/matrix/androidsdk/rest/model/WellKnown;", "domain", "", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: WellKnownAPI.kt */
public interface WellKnownAPI {
    @GET("https://{domain}/.well-known/matrix/client")
    Call<WellKnown> getWellKnown(@Path("domain") String str);
}
