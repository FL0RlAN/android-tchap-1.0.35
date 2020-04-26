package org.matrix.androidsdk.rest.api;

import org.matrix.androidsdk.rest.model.Versions;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.matrix.androidsdk.rest.model.login.LoginFlowResponse;
import org.matrix.androidsdk.rest.model.login.LoginParams;
import org.matrix.androidsdk.rest.model.login.RegistrationParams;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginApi {
    @GET("_matrix/client/r0/login")
    Call<LoginFlowResponse> login();

    @POST("_matrix/client/r0/login")
    Call<Credentials> login(@Body LoginParams loginParams);

    @POST("_matrix/client/r0/logout")
    Call<Void> logout();

    @POST("_matrix/client/r0/register")
    Call<Credentials> register(@Body RegistrationParams registrationParams);

    @GET("_matrix/client/versions")
    Call<Versions> versions();
}
