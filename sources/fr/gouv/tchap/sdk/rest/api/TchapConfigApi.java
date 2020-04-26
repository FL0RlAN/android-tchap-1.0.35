package fr.gouv.tchap.sdk.rest.api;

import fr.gouv.tchap.sdk.rest.model.TchapClientConfig;
import kotlin.Metadata;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H'¨\u0006\u0007"}, d2 = {"Lfr/gouv/tchap/sdk/rest/api/TchapConfigApi;", "", "getTchapClientConfig", "Lretrofit2/Call;", "Lfr/gouv/tchap/sdk/rest/model/TchapClientConfig;", "variant", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: TchapConfigApi.kt */
public interface TchapConfigApi {
    @GET("client/config/{variant}/android")
    Call<TchapClientConfig> getTchapClientConfig(@Path("variant") String str);
}
