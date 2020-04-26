package org.matrix.androidsdk.rest.model.login;

import kotlin.Metadata;
import org.matrix.androidsdk.rest.client.LoginRestClient;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lorg/matrix/androidsdk/rest/model/login/AuthParamsCaptcha;", "Lorg/matrix/androidsdk/rest/model/login/AuthParams;", "()V", "response", "", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: AuthParamsCaptcha.kt */
public final class AuthParamsCaptcha extends AuthParams {
    public String response;

    public AuthParamsCaptcha() {
        super(LoginRestClient.LOGIN_FLOW_TYPE_RECAPTCHA);
    }
}
