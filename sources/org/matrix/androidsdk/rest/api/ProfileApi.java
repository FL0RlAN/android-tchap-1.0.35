package org.matrix.androidsdk.rest.api;

import org.matrix.androidsdk.rest.model.ChangePasswordParams;
import org.matrix.androidsdk.rest.model.DeactivateAccountParams;
import org.matrix.androidsdk.rest.model.ForgetPasswordParams;
import org.matrix.androidsdk.rest.model.ForgetPasswordResponse;
import org.matrix.androidsdk.rest.model.RequestEmailValidationParams;
import org.matrix.androidsdk.rest.model.RequestEmailValidationResponse;
import org.matrix.androidsdk.rest.model.RequestPhoneNumberValidationParams;
import org.matrix.androidsdk.rest.model.RequestPhoneNumberValidationResponse;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.login.TokenRefreshParams;
import org.matrix.androidsdk.rest.model.login.TokenRefreshResponse;
import org.matrix.androidsdk.rest.model.pid.AccountThreePidsResponse;
import org.matrix.androidsdk.rest.model.pid.AddThreePidsParams;
import org.matrix.androidsdk.rest.model.pid.DeleteThreePidParams;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProfileApi {
    @POST("_matrix/client/r0/account/3pid")
    Call<Void> add3PID(@Body AddThreePidsParams addThreePidsParams);

    @GET("_matrix/client/r0/profile/{userId}/avatar_url")
    Call<User> avatarUrl(@Path("userId") String str);

    @PUT("_matrix/client/r0/profile/{userId}/avatar_url")
    Call<Void> avatarUrl(@Path("userId") String str, @Body User user);

    @POST("_matrix/client/r0/account/deactivate")
    Call<Void> deactivate(@Body DeactivateAccountParams deactivateAccountParams);

    @POST("_matrix/client/unstable/account/3pid/delete")
    Call<Void> delete3PID(@Body DeleteThreePidParams deleteThreePidParams);

    @GET("_matrix/client/r0/profile/{userId}/displayname")
    Call<User> displayname(@Path("userId") String str);

    @PUT("_matrix/client/r0/profile/{userId}/displayname")
    Call<Void> displayname(@Path("userId") String str, @Body User user);

    @POST("_matrix/client/r0/account/password/email/requestToken")
    Call<ForgetPasswordResponse> forgetPassword(@Body ForgetPasswordParams forgetPasswordParams);

    @POST("_matrix/client/r0/account/3pid/email/requestToken")
    Call<RequestEmailValidationResponse> requestEmailValidation(@Body RequestEmailValidationParams requestEmailValidationParams);

    @POST("_matrix/client/r0/register/email/requestToken")
    Call<RequestEmailValidationResponse> requestEmailValidationForRegistration(@Body RequestEmailValidationParams requestEmailValidationParams);

    @POST("_matrix/client/r0/account/3pid/msisdn/requestToken")
    Call<RequestPhoneNumberValidationResponse> requestPhoneNumberValidation(@Body RequestPhoneNumberValidationParams requestPhoneNumberValidationParams);

    @POST("_matrix/client/r0/register/msisdn/requestToken")
    Call<RequestPhoneNumberValidationResponse> requestPhoneNumberValidationForRegistration(@Body RequestPhoneNumberValidationParams requestPhoneNumberValidationParams);

    @GET("_matrix/client/r0/account/3pid")
    Call<AccountThreePidsResponse> threePIDs();

    @POST("_matrix/client/r0/tokenrefresh")
    Call<TokenRefreshResponse> tokenRefresh(@Body TokenRefreshParams tokenRefreshParams);

    @POST("_matrix/client/r0/account/password")
    Call<Void> updatePassword(@Body ChangePasswordParams changePasswordParams);
}
