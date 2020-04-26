package im.vector.extensions;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0000\n\u0002\u0010\u000e\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a2\u0010\u0003\u001a\u0002H\u0004\"\b\b\u0000\u0010\u0004*\u00020\u0005*\u0002H\u00042\u0017\u0010\u0006\u001a\u0013\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007¢\u0006\u0002\b\n¢\u0006\u0002\u0010\u000b¨\u0006\f"}, d2 = {"toOnOff", "", "", "withArgs", "T", "Landroidx/fragment/app/Fragment;", "block", "Lkotlin/Function1;", "Landroid/os/Bundle;", "", "Lkotlin/ExtensionFunctionType;", "(Landroidx/fragment/app/Fragment;Lkotlin/jvm/functions/Function1;)Landroidx/fragment/app/Fragment;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 2, mv = {1, 1, 13})
/* compiled from: BasicExtensions.kt */
public final class BasicExtensionsKt {
    public static final String toOnOff(boolean z) {
        return z ? "ON" : "OFF";
    }

    public static final <T extends Fragment> T withArgs(T t, Function1<? super Bundle, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(t, "receiver$0");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        Bundle bundle = new Bundle();
        function1.invoke(bundle);
        t.setArguments(bundle);
        return t;
    }
}
