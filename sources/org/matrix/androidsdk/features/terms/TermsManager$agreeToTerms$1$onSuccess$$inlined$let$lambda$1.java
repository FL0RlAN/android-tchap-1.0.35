package org.matrix.androidsdk.features.terms;

import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiFailureCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0002H\u0016¨\u0006\u0006¸\u0006\u0000"}, d2 = {"org/matrix/androidsdk/features/terms/TermsManager$agreeToTerms$1$onSuccess$1$1", "Lorg/matrix/androidsdk/core/callback/SimpleApiCallback;", "Ljava/lang/Void;", "onSuccess", "", "info", "matrix-sdk_release"}, k = 1, mv = {1, 1, 13})
/* compiled from: TermsManager.kt */
public final class TermsManager$agreeToTerms$1$onSuccess$$inlined$let$lambda$1 extends SimpleApiCallback<Void> {
    final /* synthetic */ List $newList$inlined;
    final /* synthetic */ TermsManager$agreeToTerms$1 this$0;

    TermsManager$agreeToTerms$1$onSuccess$$inlined$let$lambda$1(ApiFailureCallback apiFailureCallback, TermsManager$agreeToTerms$1 termsManager$agreeToTerms$1, List list) {
        this.this$0 = termsManager$agreeToTerms$1;
        this.$newList$inlined = list;
        super(apiFailureCallback);
    }

    public void onSuccess(Void voidR) {
        Log.d("TermsManager", "Account data accepted terms updated");
        this.this$0.$callback.onSuccess(Unit.INSTANCE);
    }
}
