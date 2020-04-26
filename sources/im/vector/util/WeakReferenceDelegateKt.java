package im.vector.util;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u001f\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, d2 = {"weak", "Lim/vector/util/WeakReferenceDelegate;", "T", "value", "(Ljava/lang/Object;)Lim/vector/util/WeakReferenceDelegate;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 2, mv = {1, 1, 13})
/* compiled from: WeakReferenceDelegate.kt */
public final class WeakReferenceDelegateKt {
    public static final <T> WeakReferenceDelegate<T> weak(T t) {
        return new WeakReferenceDelegate<>(t);
    }
}
