package fr.gouv.tchap.sdk.rest.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TchapValidityApi {
    @GET("account_validity/renew")
    Call<Void> renewAccountValidity(@Query("token") String str);

    @POST("account_validity/send_mail")
    Call<Void> requestRenewalEmail();
}
