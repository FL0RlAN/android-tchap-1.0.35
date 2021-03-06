package kotlinx.coroutines.internal;

import java.lang.reflect.Constructor;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.Result.Companion;
import kotlin.ResultKt;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0003\u0010\u0000\u001a\u0004\u0018\u0001H\u0001\"\b\b\u0000\u0010\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "E", "", "e", "invoke", "(Ljava/lang/Throwable;)Ljava/lang/Throwable;"}, k = 3, mv = {1, 1, 13})
/* compiled from: ExceptionsConstuctor.kt */
final class ExceptionsConstuctorKt$tryCopyException$3 extends Lambda implements Function1<Throwable, E> {
    final /* synthetic */ Constructor $constructor;

    ExceptionsConstuctorKt$tryCopyException$3(Constructor constructor) {
        this.$constructor = constructor;
        super(1);
    }

    public final E invoke(Throwable th) {
        E e;
        Intrinsics.checkParameterIsNotNull(th, "e");
        try {
            Companion companion = Result.Companion;
            Object newInstance = this.$constructor.newInstance(new Object[0]);
            if (newInstance != null) {
                e = Result.m3constructorimpl((Throwable) newInstance);
                if (Result.m9isFailureimpl(e)) {
                    e = null;
                }
                E e2 = (Throwable) e;
                if (e2 == null) {
                    return null;
                }
                e2.initCause(th);
                return e2;
            }
            throw new TypeCastException("null cannot be cast to non-null type E");
        } catch (Throwable th2) {
            Companion companion2 = Result.Companion;
            e = Result.m3constructorimpl(ResultKt.createFailure(th2));
        }
    }
}
