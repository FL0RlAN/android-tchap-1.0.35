package org.matrix.androidsdk.rest.model.login;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0016\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0014\u0010\u0005\u001a\u0004\u0018\u00010\u00038\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lorg/matrix/androidsdk/rest/model/login/AuthParams;", "", "type", "", "(Ljava/lang/String;)V", "session", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: AuthParams.kt */
public class AuthParams {
    public String session;
    public final String type;

    public AuthParams(String str) {
        Intrinsics.checkParameterIsNotNull(str, PasswordLoginParams.IDENTIFIER_KEY_TYPE);
        this.type = str;
    }
}
