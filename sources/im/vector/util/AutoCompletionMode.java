package im.vector.util;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0001\u0018\u0000 \u00052\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0005B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004¨\u0006\u0006"}, d2 = {"Lim/vector/util/AutoCompletionMode;", "", "(Ljava/lang/String;I)V", "USER_MODE", "COMMAND_MODE", "Companion", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: AutoCompletionMode.kt */
public enum AutoCompletionMode {
    USER_MODE,
    COMMAND_MODE;
    
    public static final Companion Companion = null;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006¨\u0006\u0007"}, d2 = {"Lim/vector/util/AutoCompletionMode$Companion;", "", "()V", "getWithText", "Lim/vector/util/AutoCompletionMode;", "text", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: AutoCompletionMode.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final AutoCompletionMode getWithText(String str) {
            Intrinsics.checkParameterIsNotNull(str, "text");
            if (StringsKt.startsWith$default(str, "@", false, 2, null) || StringsKt.contains$default((CharSequence) str, (CharSequence) " ", false, 2, (Object) null)) {
                return AutoCompletionMode.USER_MODE;
            }
            if (StringsKt.startsWith$default(str, "/", false, 2, null)) {
                return AutoCompletionMode.COMMAND_MODE;
            }
            return AutoCompletionMode.USER_MODE;
        }
    }

    static {
        Companion = new Companion(null);
    }
}
