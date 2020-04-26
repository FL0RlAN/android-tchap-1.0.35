package org.matrix.androidsdk.login;

import kotlin.Metadata;
import org.json.JSONObject;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.login.AutoDiscovery.Action;
import org.matrix.androidsdk.login.AutoDiscovery.DiscoveredClientConfig;
import org.matrix.androidsdk.rest.model.WellKnown;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0016J\u0018\u0010\u0007\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0018\u00010\bj\u0004\u0018\u0001`\tH\u0016J\u0012\u0010\n\u001a\u00020\u00042\b\u0010\u000b\u001a\u0004\u0018\u00010\u0002H\u0016J\u0018\u0010\f\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0018\u00010\bj\u0004\u0018\u0001`\tH\u0016¨\u0006\r"}, d2 = {"org/matrix/androidsdk/login/AutoDiscovery$validateIdentityServerAndFinish$1", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Lorg/json/JSONObject;", "onMatrixError", "", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "info", "onUnexpectedError", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: AutoDiscovery.kt */
public final class AutoDiscovery$validateIdentityServerAndFinish$1 implements ApiCallback<JSONObject> {
    final /* synthetic */ ApiCallback $callback;
    final /* synthetic */ WellKnown $wellKnown;

    AutoDiscovery$validateIdentityServerAndFinish$1(ApiCallback apiCallback, WellKnown wellKnown) {
        this.$callback = apiCallback;
        this.$wellKnown = wellKnown;
    }

    public void onSuccess(JSONObject jSONObject) {
        this.$callback.onSuccess(new DiscoveredClientConfig(Action.PROMPT, this.$wellKnown));
    }

    public void onUnexpectedError(Exception exc) {
        this.$callback.onSuccess(new DiscoveredClientConfig(Action.FAIL_ERROR, null, 2, null));
    }

    public void onNetworkError(Exception exc) {
        this.$callback.onSuccess(new DiscoveredClientConfig(Action.FAIL_ERROR, null, 2, null));
    }

    public void onMatrixError(MatrixError matrixError) {
        this.$callback.onSuccess(new DiscoveredClientConfig(Action.FAIL_ERROR, null, 2, null));
    }
}
