package im.vector.settings;

import android.content.Context;
import kotlin.Metadata;
import kotlin.Result.Failure;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H@ø\u0001\u0000¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "im.vector.settings.VectorLocale$init$1", f = "VectorLocale.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
/* compiled from: VectorLocale.kt */
final class VectorLocale$init$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Context $context;
    int label;
    private CoroutineScope p$;

    VectorLocale$init$1(Context context, Continuation continuation) {
        this.$context = context;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        VectorLocale$init$1 vectorLocale$init$1 = new VectorLocale$init$1(this.$context, continuation);
        vectorLocale$init$1.p$ = (CoroutineScope) obj;
        return vectorLocale$init$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((VectorLocale$init$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label != 0) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else if (!(obj instanceof Failure)) {
            CoroutineScope coroutineScope = this.p$;
            VectorLocale.INSTANCE.initApplicationLocales(this.$context);
            return Unit.INSTANCE;
        } else {
            throw ((Failure) obj).exception;
        }
    }
}
