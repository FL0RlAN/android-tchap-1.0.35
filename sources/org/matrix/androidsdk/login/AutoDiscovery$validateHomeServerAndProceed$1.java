package org.matrix.androidsdk.login;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.login.AutoDiscovery.Action;
import org.matrix.androidsdk.login.AutoDiscovery.DiscoveredClientConfig;
import org.matrix.androidsdk.rest.model.Versions;
import org.matrix.androidsdk.rest.model.WellKnown;
import org.matrix.androidsdk.rest.model.WellKnownBaseConfig;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0016J\u0018\u0010\u0007\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0018\u00010\bj\u0004\u0018\u0001`\tH\u0016J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u0002H\u0016J\u0018\u0010\f\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0018\u00010\bj\u0004\u0018\u0001`\tH\u0016¨\u0006\r"}, d2 = {"org/matrix/androidsdk/login/AutoDiscovery$validateHomeServerAndProceed$1", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "Lorg/matrix/androidsdk/rest/model/Versions;", "onMatrixError", "", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "versions", "onUnexpectedError", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: AutoDiscovery.kt */
public final class AutoDiscovery$validateHomeServerAndProceed$1 implements ApiCallback<Versions> {
    final /* synthetic */ ApiCallback $callback;
    final /* synthetic */ WellKnown $wellKnown;
    final /* synthetic */ AutoDiscovery this$0;

    AutoDiscovery$validateHomeServerAndProceed$1(AutoDiscovery autoDiscovery, WellKnown wellKnown, ApiCallback apiCallback) {
        this.this$0 = autoDiscovery;
        this.$wellKnown = wellKnown;
        this.$callback = apiCallback;
    }

    public void onSuccess(Versions versions) {
        Intrinsics.checkParameterIsNotNull(versions, "versions");
        if (this.$wellKnown.identityServer == null) {
            this.$callback.onSuccess(new DiscoveredClientConfig(Action.PROMPT, this.$wellKnown));
            return;
        }
        WellKnownBaseConfig wellKnownBaseConfig = this.$wellKnown.identityServer;
        if (wellKnownBaseConfig == null) {
            Intrinsics.throwNpe();
        }
        CharSequence charSequence = wellKnownBaseConfig.baseURL;
        if (charSequence == null || StringsKt.isBlank(charSequence)) {
            this.$callback.onSuccess(new DiscoveredClientConfig(Action.FAIL_ERROR, null, 2, null));
            return;
        }
        WellKnownBaseConfig wellKnownBaseConfig2 = this.$wellKnown.identityServer;
        if (wellKnownBaseConfig2 == null) {
            Intrinsics.throwNpe();
        }
        String str = wellKnownBaseConfig2.baseURL;
        if (str == null) {
            Intrinsics.throwNpe();
        }
        if (this.this$0.isValidURL(str)) {
            this.this$0.validateIdentityServerAndFinish(this.$wellKnown, this.$callback);
        } else {
            this.$callback.onSuccess(new DiscoveredClientConfig(Action.FAIL_ERROR, null, 2, null));
        }
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
