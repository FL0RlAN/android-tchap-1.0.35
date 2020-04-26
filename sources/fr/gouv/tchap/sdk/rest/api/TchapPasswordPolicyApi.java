package fr.gouv.tchap.sdk.rest.api;

import fr.gouv.tchap.sdk.rest.model.PasswordPolicy;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TchapPasswordPolicyApi {
    @GET("password_policy")
    Call<PasswordPolicy> passwordPolicy();
}
