package org.matrix.androidsdk.crypto.verification;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.properties.ObservableProperty;
import kotlin.reflect.KProperty;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.verification.SASVerificationTransaction.SASVerificationTxState;
import org.matrix.androidsdk.crypto.verification.VerificationTransaction.Listener;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0004\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J)\u0010\u0002\u001a\u00020\u00032\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u00052\u0006\u0010\u0006\u001a\u00028\u00002\u0006\u0010\u0007\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010\b¨\u0006\t¸\u0006\u0000"}, d2 = {"kotlin/properties/Delegates$observable$1", "Lkotlin/properties/ObservableProperty;", "afterChange", "", "property", "Lkotlin/reflect/KProperty;", "oldValue", "newValue", "(Lkotlin/reflect/KProperty;Ljava/lang/Object;Ljava/lang/Object;)V", "kotlin-stdlib"}, k = 1, mv = {1, 1, 13})
/* compiled from: Delegates.kt */
public final class SASVerificationTransaction$$special$$inlined$observable$1 extends ObservableProperty<SASVerificationTxState> {
    final /* synthetic */ Object $initialValue;
    final /* synthetic */ SASVerificationTransaction this$0;

    public SASVerificationTransaction$$special$$inlined$observable$1(Object obj, Object obj2, SASVerificationTransaction sASVerificationTransaction) {
        this.$initialValue = obj;
        this.this$0 = sASVerificationTransaction;
        super(obj2);
    }

    /* access modifiers changed from: protected */
    public void afterChange(KProperty<?> kProperty, SASVerificationTxState sASVerificationTxState, SASVerificationTxState sASVerificationTxState2) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        SASVerificationTxState sASVerificationTxState3 = sASVerificationTxState2;
        SASVerificationTxState sASVerificationTxState4 = sASVerificationTxState;
        for (Listener transactionUpdated : this.this$0.getListeners()) {
            try {
                transactionUpdated.transactionUpdated(this.this$0);
            } catch (Throwable th) {
                Log.e(SASVerificationTransaction.Companion.getLOG_TAG(), "## Error while notifying listeners", th);
            }
        }
        if (sASVerificationTxState3 == SASVerificationTxState.Cancelled || sASVerificationTxState3 == SASVerificationTxState.OnCancelled || sASVerificationTxState3 == SASVerificationTxState.Verified) {
            this.this$0.releaseSAS();
        }
    }
}
