package im.vector.widgets;

import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface WidgetsApi {
    @POST("register")
    Call<Map<String, String>> register(@Body Map<Object, Object> map);
}
