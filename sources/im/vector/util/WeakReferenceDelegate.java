package im.vector.util;

import java.lang.ref.WeakReference;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KProperty;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00028\u0000¢\u0006\u0002\u0010\u0004J$\u0010\u0007\u001a\u0004\u0018\u00018\u00002\u0006\u0010\b\u001a\u00020\u00022\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\nH\u0002¢\u0006\u0002\u0010\u000bJ*\u0010\f\u001a\u00020\r2\u0006\u0010\b\u001a\u00020\u00022\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\n2\u0006\u0010\u0003\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\u000eR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lim/vector/util/WeakReferenceDelegate;", "T", "", "value", "(Ljava/lang/Object;)V", "weakReference", "Ljava/lang/ref/WeakReference;", "getValue", "thisRef", "property", "Lkotlin/reflect/KProperty;", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;)Ljava/lang/Object;", "setValue", "", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;Ljava/lang/Object;)V", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: WeakReferenceDelegate.kt */
public final class WeakReferenceDelegate<T> {
    private WeakReference<T> weakReference;

    public WeakReferenceDelegate(T t) {
        this.weakReference = new WeakReference<>(t);
    }

    public final T getValue(Object obj, KProperty<?> kProperty) {
        Intrinsics.checkParameterIsNotNull(obj, "thisRef");
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        return this.weakReference.get();
    }

    public final void setValue(Object obj, KProperty<?> kProperty, T t) {
        Intrinsics.checkParameterIsNotNull(obj, "thisRef");
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        this.weakReference = new WeakReference<>(t);
    }
}
