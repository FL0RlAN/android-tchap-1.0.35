package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.Result;
import kotlin.Result.Companion;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.SystemPropsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u000f\u001a\u00020\u0003*\u0006\u0012\u0002\b\u00030\u0010H\u0000\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u0010\u0010\u0007\u001a\u00020\u00018\u0000X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u0018\u0010\t\u001a\u00020\u0003*\u00020\n8@X\u0004¢\u0006\u0006\u001a\u0004\b\u000b\u0010\f\"\u0018\u0010\r\u001a\u00020\u0003*\u00020\n8@X\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\f¨\u0006\u0011"}, d2 = {"DEBUG", "", "DEBUG_PROPERTY_NAME", "", "DEBUG_PROPERTY_VALUE_AUTO", "DEBUG_PROPERTY_VALUE_OFF", "DEBUG_PROPERTY_VALUE_ON", "RECOVER_STACKTRACES", "STACKTRACE_RECOVERY_PROPERTY_NAME", "classSimpleName", "", "getClassSimpleName", "(Ljava/lang/Object;)Ljava/lang/String;", "hexAddress", "getHexAddress", "toDebugString", "Lkotlin/coroutines/Continuation;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 13})
/* compiled from: Debug.kt */
public final class DebugKt {
    public static final boolean DEBUG;
    public static final String DEBUG_PROPERTY_NAME = "kotlinx.coroutines.debug";
    public static final String DEBUG_PROPERTY_VALUE_AUTO = "auto";
    public static final String DEBUG_PROPERTY_VALUE_OFF = "off";
    public static final String DEBUG_PROPERTY_VALUE_ON = "on";
    public static final boolean RECOVER_STACKTRACES = SystemPropsKt.systemProp(STACKTRACE_RECOVERY_PROPERTY_NAME, true);
    public static final String STACKTRACE_RECOVERY_PROPERTY_NAME = "kotlinx.coroutines.stacktrace.recovery";

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0024, code lost:
        if (r0.equals(DEBUG_PROPERTY_VALUE_AUTO) != false) goto L_0x0026;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003d, code lost:
        if (r0.equals(DEBUG_PROPERTY_VALUE_ON) != false) goto L_0x0048;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0046, code lost:
        if (r0.equals("") != false) goto L_0x0048;
     */
    static {
        boolean z;
        String systemProp = SystemPropsKt.systemProp(DEBUG_PROPERTY_NAME);
        if (systemProp != null) {
            int hashCode = systemProp.hashCode();
            if (hashCode != 0) {
                if (hashCode != 3551) {
                    if (hashCode != 109935) {
                        if (hashCode == 3005871) {
                        }
                    } else if (systemProp.equals(DEBUG_PROPERTY_VALUE_OFF)) {
                        z = false;
                        DEBUG = z;
                    }
                }
                StringBuilder sb = new StringBuilder();
                sb.append("System property 'kotlinx.coroutines.debug' has unrecognized value '");
                sb.append(systemProp);
                sb.append('\'');
                throw new IllegalStateException(sb.toString().toString());
            }
            z = true;
            DEBUG = z;
        }
        z = CoroutineId.class.desiredAssertionStatus();
        DEBUG = z;
    }

    public static final String getHexAddress(Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "receiver$0");
        String hexString = Integer.toHexString(System.identityHashCode(obj));
        Intrinsics.checkExpressionValueIsNotNull(hexString, "Integer.toHexString(System.identityHashCode(this))");
        return hexString;
    }

    public static final String toDebugString(Continuation<?> continuation) {
        Object obj;
        Intrinsics.checkParameterIsNotNull(continuation, "receiver$0");
        if (continuation instanceof DispatchedContinuation) {
            return continuation.toString();
        }
        try {
            Companion companion = Result.Companion;
            StringBuilder sb = new StringBuilder();
            sb.append(continuation);
            sb.append('@');
            sb.append(getHexAddress(continuation));
            obj = Result.m3constructorimpl(sb.toString());
        } catch (Throwable th) {
            Companion companion2 = Result.Companion;
            obj = Result.m3constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m6exceptionOrNullimpl(obj) != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(continuation.getClass().getName());
            sb2.append('@');
            sb2.append(getHexAddress(continuation));
            obj = sb2.toString();
        }
        return (String) obj;
    }

    public static final String getClassSimpleName(Object obj) {
        Intrinsics.checkParameterIsNotNull(obj, "receiver$0");
        String simpleName = obj.getClass().getSimpleName();
        Intrinsics.checkExpressionValueIsNotNull(simpleName, "this::class.java.simpleName");
        return simpleName;
    }
}
