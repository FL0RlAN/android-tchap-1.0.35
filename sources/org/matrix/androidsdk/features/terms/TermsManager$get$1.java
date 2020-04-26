package org.matrix.androidsdk.features.terms;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.ApiFailureCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0002H\u0016¨\u0006\u0006"}, d2 = {"org/matrix/androidsdk/features/terms/TermsManager$get$1", "Lorg/matrix/androidsdk/core/callback/SimpleApiCallback;", "Lorg/matrix/androidsdk/rest/model/terms/TermsResponse;", "onSuccess", "", "info", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: TermsManager.kt */
public final class TermsManager$get$1 extends SimpleApiCallback<TermsResponse> {
    final /* synthetic */ ApiCallback $callback;
    final /* synthetic */ TermsManager this$0;

    TermsManager$get$1(TermsManager termsManager, ApiCallback apiCallback, ApiFailureCallback apiFailureCallback) {
        this.this$0 = termsManager;
        this.$callback = apiCallback;
        super(apiFailureCallback);
    }

    public void onSuccess(TermsResponse termsResponse) {
        Intrinsics.checkParameterIsNotNull(termsResponse, "info");
        this.$callback.onSuccess(new GetTermsResponse(termsResponse, this.this$0.getAlreadyAcceptedTermUrlsFromAccountData()));
    }
}
