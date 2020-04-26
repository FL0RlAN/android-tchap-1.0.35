package org.matrix.androidsdk.login;

import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.ApiFailureCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.HttpError;
import org.matrix.androidsdk.core.model.HttpException;
import org.matrix.androidsdk.login.AutoDiscovery.Action;
import org.matrix.androidsdk.login.AutoDiscovery.DiscoveredClientConfig;
import org.matrix.androidsdk.rest.model.WellKnown;
import org.matrix.androidsdk.rest.model.WellKnownBaseConfig;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0014\u0010\u0003\u001a\u00020\u00042\n\u0010\u0005\u001a\u00060\u0006j\u0002`\u0007H\u0016J\u0010\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0002H\u0016¨\u0006\n"}, d2 = {"org/matrix/androidsdk/login/AutoDiscovery$findClientConfig$1", "Lorg/matrix/androidsdk/core/callback/SimpleApiCallback;", "Lorg/matrix/androidsdk/rest/model/WellKnown;", "onNetworkError", "", "e", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "wellKnown", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: AutoDiscovery.kt */
public final class AutoDiscovery$findClientConfig$1 extends SimpleApiCallback<WellKnown> {
    final /* synthetic */ ApiCallback $callback;
    final /* synthetic */ AutoDiscovery this$0;

    AutoDiscovery$findClientConfig$1(AutoDiscovery autoDiscovery, ApiCallback apiCallback, ApiFailureCallback apiFailureCallback) {
        this.this$0 = autoDiscovery;
        this.$callback = apiCallback;
        super(apiFailureCallback);
    }

    public void onSuccess(WellKnown wellKnown) {
        Intrinsics.checkParameterIsNotNull(wellKnown, "wellKnown");
        WellKnownBaseConfig wellKnownBaseConfig = wellKnown.homeServer;
        CharSequence charSequence = wellKnownBaseConfig != null ? wellKnownBaseConfig.baseURL : null;
        if (charSequence == null || StringsKt.isBlank(charSequence)) {
            this.$callback.onSuccess(new DiscoveredClientConfig(Action.FAIL_PROMPT, null, 2, null));
            return;
        }
        WellKnownBaseConfig wellKnownBaseConfig2 = wellKnown.homeServer;
        if (wellKnownBaseConfig2 == null) {
            Intrinsics.throwNpe();
        }
        String str = wellKnownBaseConfig2.baseURL;
        if (str == null) {
            Intrinsics.throwNpe();
        }
        if (this.this$0.isValidURL(str)) {
            this.this$0.validateHomeServerAndProceed(wellKnown, this.$callback);
        } else {
            this.$callback.onSuccess(new DiscoveredClientConfig(Action.FAIL_ERROR, null, 2, null));
        }
    }

    public void onNetworkError(Exception exc) {
        Intrinsics.checkParameterIsNotNull(exc, "e");
        if (exc instanceof HttpException) {
            HttpError httpError = ((HttpException) exc).getHttpError();
            Intrinsics.checkExpressionValueIsNotNull(httpError, "e.httpError");
            if (httpError.getHttpCode() != 404) {
                this.$callback.onSuccess(new DiscoveredClientConfig(Action.FAIL_PROMPT, null, 2, null));
            } else {
                this.$callback.onSuccess(new DiscoveredClientConfig(Action.IGNORE, null, 2, null));
            }
        } else if ((exc instanceof MalformedJsonException) || (exc instanceof EOFException)) {
            this.$callback.onSuccess(new DiscoveredClientConfig(Action.FAIL_PROMPT, null, 2, null));
        } else {
            super.onNetworkError(exc);
        }
    }
}
