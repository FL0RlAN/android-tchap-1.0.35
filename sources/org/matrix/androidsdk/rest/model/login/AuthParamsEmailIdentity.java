package org.matrix.androidsdk.rest.model.login;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import org.matrix.androidsdk.rest.client.LoginRestClient;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lorg/matrix/androidsdk/rest/model/login/AuthParamsEmailIdentity;", "Lorg/matrix/androidsdk/rest/model/login/AuthParams;", "()V", "threePidCredentials", "Lorg/matrix/androidsdk/rest/model/login/ThreePidCredentials;", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: AuthParamsEmailIdentity.kt */
public final class AuthParamsEmailIdentity extends AuthParams {
    @SerializedName("threepid_creds")
    public ThreePidCredentials threePidCredentials;

    public AuthParamsEmailIdentity() {
        super(LoginRestClient.LOGIN_FLOW_TYPE_EMAIL_IDENTITY);
    }
}
