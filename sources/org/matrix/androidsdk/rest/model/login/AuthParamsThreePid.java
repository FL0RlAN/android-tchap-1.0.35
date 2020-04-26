package org.matrix.androidsdk.rest.model.login;

import com.google.gson.annotations.SerializedName;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lorg/matrix/androidsdk/rest/model/login/AuthParamsThreePid;", "Lorg/matrix/androidsdk/rest/model/login/AuthParams;", "type", "", "(Ljava/lang/String;)V", "threePidCredentials", "Lorg/matrix/androidsdk/rest/model/login/ThreePidCredentials;", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: AuthParamsThreePid.kt */
public final class AuthParamsThreePid extends AuthParams {
    @SerializedName("threepid_creds")
    public final ThreePidCredentials threePidCredentials;

    public AuthParamsThreePid(String str) {
        Intrinsics.checkParameterIsNotNull(str, PasswordLoginParams.IDENTIFIER_KEY_TYPE);
        super(str);
        ThreePidCredentials threePidCredentials2 = new ThreePidCredentials(null, null, null, 7, null);
        this.threePidCredentials = threePidCredentials2;
    }
}
