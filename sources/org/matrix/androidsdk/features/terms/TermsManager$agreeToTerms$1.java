package org.matrix.androidsdk.features.terms;

import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.ApiFailureCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.rest.client.AccountDataRestClient;
import org.matrix.androidsdk.rest.model.sync.AccountDataElement;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001J\u0015\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0004\u001a\u00020\u0002H\u0016¢\u0006\u0002\u0010\u0005¨\u0006\u0006"}, d2 = {"org/matrix/androidsdk/features/terms/TermsManager$agreeToTerms$1", "Lorg/matrix/androidsdk/core/callback/SimpleApiCallback;", "", "onSuccess", "info", "(Lkotlin/Unit;)V", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: TermsManager.kt */
public final class TermsManager$agreeToTerms$1 extends SimpleApiCallback<Unit> {
    final /* synthetic */ List $agreedUrls;
    final /* synthetic */ ApiCallback $callback;
    final /* synthetic */ TermsManager this$0;

    TermsManager$agreeToTerms$1(TermsManager termsManager, List list, ApiCallback apiCallback, ApiFailureCallback apiFailureCallback) {
        this.this$0 = termsManager;
        this.$agreedUrls = list;
        this.$callback = apiCallback;
        super(apiFailureCallback);
    }

    public void onSuccess(Unit unit) {
        Intrinsics.checkParameterIsNotNull(unit, "info");
        Set mutableSet = CollectionsKt.toMutableSet(this.this$0.getAlreadyAcceptedTermUrlsFromAccountData());
        mutableSet.addAll(this.$agreedUrls);
        List list = CollectionsKt.toList(mutableSet);
        String myUserId = this.this$0.mxSession.getMyUserId();
        AccountDataRestClient accountDataRestClient = this.this$0.mxSession.getAccountDataRestClient();
        if (accountDataRestClient != null) {
            accountDataRestClient.setAccountData(myUserId, AccountDataElement.ACCOUNT_DATA_ACCEPTED_TERMS, MapsKt.mapOf(TuplesKt.to(AccountDataElement.ACCOUNT_DATA_KEY_ACCEPTED_TERMS, list)), new TermsManager$agreeToTerms$1$onSuccess$$inlined$let$lambda$1(this.$callback, this, list));
        }
    }
}
