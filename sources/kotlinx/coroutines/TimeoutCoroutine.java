package kotlinx.coroutines;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0012\u0018\u0000*\u0004\b\u0000\u0010\u0001*\n\b\u0001\u0010\u0002 \u0000*\u0002H\u00012\b\u0012\u0004\u0012\u0002H\u00020\u00032\u00060\u0004j\u0002`\u00052\b\u0012\u0004\u0012\u0002H\u00020\u00062\u00060\u0007j\u0002`\bB\u001b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006¢\u0006\u0002\u0010\fJ\u0010\u0010\u0014\u001a\n\u0018\u00010\u0015j\u0004\u0018\u0001`\u0016H\u0016J\r\u0010\u0017\u001a\u00020\u0018H\u0010¢\u0006\u0002\b\u0019J'\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020 H\u0010¢\u0006\u0002\b!J\b\u0010\"\u001a\u00020\u001bH\u0016R\u001c\u0010\r\u001a\n\u0018\u00010\u0007j\u0004\u0018\u0001`\b8VX\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\u00020\u00118PX\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0010\u0010\t\u001a\u00020\n8\u0006X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000b\u001a\b\u0012\u0004\u0012\u00028\u00000\u00068\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006#"}, d2 = {"Lkotlinx/coroutines/TimeoutCoroutine;", "U", "T", "Lkotlinx/coroutines/AbstractCoroutine;", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "Lkotlin/coroutines/Continuation;", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "Lkotlinx/coroutines/internal/CoroutineStackFrame;", "time", "", "uCont", "(JLkotlin/coroutines/Continuation;)V", "callerFrame", "getCallerFrame", "()Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "defaultResumeMode", "", "getDefaultResumeMode$kotlinx_coroutines_core", "()I", "getStackTraceElement", "Ljava/lang/StackTraceElement;", "Lkotlinx/coroutines/internal/StackTraceElement;", "nameString", "", "nameString$kotlinx_coroutines_core", "onCompletionInternal", "", "state", "", "mode", "suppressed", "", "onCompletionInternal$kotlinx_coroutines_core", "run", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* compiled from: Timeout.kt */
class TimeoutCoroutine<U, T extends U> extends AbstractCoroutine<T> implements Runnable, Continuation<T>, CoroutineStackFrame {
    public final long time;
    public final Continuation<U> uCont;

    public int getDefaultResumeMode$kotlinx_coroutines_core() {
        return 2;
    }

    public TimeoutCoroutine(long j, Continuation<? super U> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "uCont");
        super(continuation.getContext(), true);
        this.time = j;
        this.uCont = continuation;
    }

    public CoroutineStackFrame getCallerFrame() {
        Continuation<U> continuation = this.uCont;
        if (!(continuation instanceof CoroutineStackFrame)) {
            continuation = null;
        }
        CoroutineStackFrame coroutineStackFrame = (CoroutineStackFrame) continuation;
        if (coroutineStackFrame != null) {
            return coroutineStackFrame.getCallerFrame();
        }
        return null;
    }

    public StackTraceElement getStackTraceElement() {
        Continuation<U> continuation = this.uCont;
        if (!(continuation instanceof CoroutineStackFrame)) {
            continuation = null;
        }
        CoroutineStackFrame coroutineStackFrame = (CoroutineStackFrame) continuation;
        if (coroutineStackFrame != null) {
            return coroutineStackFrame.getStackTraceElement();
        }
        return null;
    }

    public void run() {
        cancel(TimeoutKt.TimeoutCancellationException(this.time, this));
    }

    public void onCompletionInternal$kotlinx_coroutines_core(Object obj, int i, boolean z) {
        if (obj instanceof CompletedExceptionally) {
            ResumeModeKt.resumeUninterceptedWithExceptionMode(this.uCont, ((CompletedExceptionally) obj).cause, i);
        } else {
            ResumeModeKt.resumeUninterceptedMode(this.uCont, obj, i);
        }
    }

    public String nameString$kotlinx_coroutines_core() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.nameString$kotlinx_coroutines_core());
        sb.append("(timeMillis=");
        sb.append(this.time);
        sb.append(')');
        return sb.toString();
    }
}
