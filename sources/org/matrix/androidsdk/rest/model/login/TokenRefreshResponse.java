package org.matrix.androidsdk.rest.model.login;

import com.google.gson.annotations.SerializedName;

public class TokenRefreshResponse {
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("refresh_token")
    public String refreshToken;
}
