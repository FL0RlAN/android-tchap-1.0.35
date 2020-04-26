package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.Result.Failure;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref.ObjectRef;
import kotlinx.coroutines.intrinsics.UndispatchedKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0000\u001a_\u0010\u0006\u001a\u0004\u0018\u00010\u0007\"\u0004\b\u0000\u0010\b\"\b\b\u0001\u0010\t*\u0002H\b2\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u0002H\t0\n2'\u0010\u000b\u001a#\b\u0001\u0012\u0004\u0012\u00020\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f¢\u0006\u0002\b\u000fH\u0002ø\u0001\u0000¢\u0006\u0002\u0010\u0010\u001aH\u0010\u0011\u001a\u0002H\t\"\u0004\b\u0000\u0010\t2\u0006\u0010\u0012\u001a\u00020\u00032'\u0010\u000b\u001a#\b\u0001\u0012\u0004\u0012\u00020\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f¢\u0006\u0002\b\u000fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0013\u001aJ\u0010\u0014\u001a\u0004\u0018\u0001H\t\"\u0004\b\u0000\u0010\t2\u0006\u0010\u0012\u001a\u00020\u00032'\u0010\u000b\u001a#\b\u0001\u0012\u0004\u0012\u00020\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00070\f¢\u0006\u0002\b\u000fH@ø\u0001\u0000¢\u0006\u0002\u0010\u0013\u0002\u0004\n\u0002\b\u0019¨\u0006\u0015"}, d2 = {"TimeoutCancellationException", "Lkotlinx/coroutines/TimeoutCancellationException;", "time", "", "coroutine", "Lkotlinx/coroutines/Job;", "setupTimeout", "", "U", "T", "Lkotlinx/coroutines/TimeoutCoroutine;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/CoroutineScope;", "Lkotlin/coroutines/Continuation;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/TimeoutCoroutine;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "withTimeout", "timeMillis", "(JLkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "withTimeoutOrNull", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 13})
/* compiled from: Timeout.kt */
public final class TimeoutKt {
    public static final <T> Object withTimeout(long j, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2, Continuation<? super T> continuation) {
        if (j > 0) {
            Object access$setupTimeout = setupTimeout(new TimeoutCoroutine(j, continuation), function2);
            if (access$setupTimeout == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            return access$setupTimeout;
        }
        throw new TimeoutCancellationException("Timed out immediately");
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0047  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x008a A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x008b  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    public static final <T> Object withTimeoutOrNull(long j, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2, Continuation<? super T> continuation) {
        TimeoutKt$withTimeoutOrNull$1 timeoutKt$withTimeoutOrNull$1;
        int i;
        ObjectRef objectRef;
        Object obj;
        if (continuation instanceof TimeoutKt$withTimeoutOrNull$1) {
            timeoutKt$withTimeoutOrNull$1 = (TimeoutKt$withTimeoutOrNull$1) continuation;
            if ((timeoutKt$withTimeoutOrNull$1.label & Integer.MIN_VALUE) != 0) {
                timeoutKt$withTimeoutOrNull$1.label -= Integer.MIN_VALUE;
                Object obj2 = timeoutKt$withTimeoutOrNull$1.result;
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = timeoutKt$withTimeoutOrNull$1.label;
                if (i == 0) {
                    if (i == 1) {
                        ObjectRef objectRef2 = (ObjectRef) timeoutKt$withTimeoutOrNull$1.L$1;
                        Function2 function22 = (Function2) timeoutKt$withTimeoutOrNull$1.L$0;
                        long j2 = timeoutKt$withTimeoutOrNull$1.J$0;
                        try {
                            if (!(obj2 instanceof Failure)) {
                                obj = obj2;
                            } else {
                                throw ((Failure) obj2).exception;
                            }
                        } catch (TimeoutCancellationException e) {
                            e = e;
                            objectRef = objectRef2;
                            if (e.coroutine != ((TimeoutCoroutine) objectRef.element)) {
                            }
                        }
                    } else {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                } else if (obj2 instanceof Failure) {
                    throw ((Failure) obj2).exception;
                } else if (j <= 0) {
                    return null;
                } else {
                    objectRef = new ObjectRef();
                    objectRef.element = (TimeoutCoroutine) null;
                    try {
                        timeoutKt$withTimeoutOrNull$1.J$0 = j;
                        timeoutKt$withTimeoutOrNull$1.L$0 = function2;
                        timeoutKt$withTimeoutOrNull$1.L$1 = objectRef;
                        timeoutKt$withTimeoutOrNull$1.label = 1;
                        T timeoutCoroutine = new TimeoutCoroutine(j, timeoutKt$withTimeoutOrNull$1);
                        objectRef.element = timeoutCoroutine;
                        obj = setupTimeout(timeoutCoroutine, function2);
                        if (obj == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                            DebugProbesKt.probeCoroutineSuspended(timeoutKt$withTimeoutOrNull$1);
                        }
                        if (obj == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } catch (TimeoutCancellationException e2) {
                        e = e2;
                        if (e.coroutine != ((TimeoutCoroutine) objectRef.element)) {
                            return null;
                        }
                        throw e;
                    }
                }
                return obj;
            }
        }
        timeoutKt$withTimeoutOrNull$1 = new TimeoutKt$withTimeoutOrNull$1(continuation);
        Object obj22 = timeoutKt$withTimeoutOrNull$1.result;
        Object coroutine_suspended2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = timeoutKt$withTimeoutOrNull$1.label;
        if (i == 0) {
        }
        return obj;
    }

    /* access modifiers changed from: private */
    public static final <U, T extends U> Object setupTimeout(TimeoutCoroutine<U, ? super T> timeoutCoroutine, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> function2) {
        JobKt.disposeOnCompletion(timeoutCoroutine, DelayKt.getDelay(timeoutCoroutine.uCont.getContext()).invokeOnTimeout(timeoutCoroutine.time, timeoutCoroutine));
        return UndispatchedKt.startUndispatchedOrReturnIgnoreTimeout(timeoutCoroutine, timeoutCoroutine, function2);
    }

    public static final TimeoutCancellationException TimeoutCancellationException(long j, Job job) {
        Intrinsics.checkParameterIsNotNull(job, "coroutine");
        StringBuilder sb = new StringBuilder();
        sb.append("Timed out waiting for ");
        sb.append(j);
        sb.append(" ms");
        return new TimeoutCancellationException(sb.toString(), job);
    }
}