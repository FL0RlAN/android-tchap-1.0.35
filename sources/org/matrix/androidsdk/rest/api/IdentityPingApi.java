package org.matrix.androidsdk.rest.api;

import kotlin.Metadata;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.http.GET;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H'¨\u0006\u0005"}, d2 = {"Lorg/matrix/androidsdk/rest/api/IdentityPingApi;", "", "ping", "Lretrofit2/Call;", "Lorg/json/JSONObject;", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: IdentityPingApi.kt */
public interface IdentityPingApi {
    @GET("_matrix/identity/api/v1")
    Call<JSONObject> ping();
}
