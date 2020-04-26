package kotlinx.coroutines;

import java.util.concurrent.locks.LockSupport;
import kotlin.Metadata;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.LongCompanionObject;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001f\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b¢\u0006\u0002\u0010\tJ\u000b\u0010\n\u001a\u00028\u0000¢\u0006\u0002\u0010\u000bJ'\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0010¢\u0006\u0002\b\u0014R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lkotlinx/coroutines/BlockingCoroutine;", "T", "Lkotlinx/coroutines/AbstractCoroutine;", "parentContext", "Lkotlin/coroutines/CoroutineContext;", "blockedThread", "Ljava/lang/Thread;", "eventLoop", "Lkotlinx/coroutines/EventLoop;", "(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Thread;Lkotlinx/coroutines/EventLoop;)V", "joinBlocking", "()Ljava/lang/Object;", "onCompletionInternal", "", "state", "", "mode", "", "suppressed", "", "onCompletionInternal$kotlinx_coroutines_core", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* compiled from: Builders.kt */
final class BlockingCoroutine<T> extends AbstractCoroutine<T> {
    private final Thread blockedThread;
    private final EventLoop eventLoop;

    public BlockingCoroutine(CoroutineContext coroutineContext, Thread thread, EventLoop eventLoop2) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "parentContext");
        Intrinsics.checkParameterIsNotNull(thread, "blockedThread");
        super(coroutineContext, true);
        this.blockedThread = thread;
        this.eventLoop = eventLoop2;
    }

    public void onCompletionInternal$kotlinx_coroutines_core(Object obj, int i, boolean z) {
        if (!Intrinsics.areEqual((Object) Thread.currentThread(), (Object) this.blockedThread)) {
            LockSupport.unpark(this.blockedThread);
        }
    }

    public final T joinBlocking() {
        TimeSourceKt.getTimeSource().registerTimeLoopThread();
        try {
            EventLoop eventLoop2 = this.eventLoop;
            T t = null;
            if (eventLoop2 != null) {
                EventLoop.incrementUseCount$default(eventLoop2, false, 1, null);
            }
            while (!Thread.interrupted()) {
                EventLoop eventLoop3 = this.eventLoop;
                long processNextEvent = eventLoop3 != null ? eventLoop3.processNextEvent() : LongCompanionObject.MAX_VALUE;
                if (isCompleted()) {
                    EventLoop eventLoop4 = this.eventLoop;
                    if (eventLoop4 != null) {
                        EventLoop.decrementUseCount$default(eventLoop4, false, 1, null);
                    }
                    TimeSourceKt.getTimeSource().unregisterTimeLoopThread();
                    T unboxState = JobSupportKt.unboxState(getState$kotlinx_coroutines_core());
                    if (unboxState instanceof CompletedExceptionally) {
                        t = unboxState;
                    }
                    CompletedExceptionally completedExceptionally = (CompletedExceptionally) t;
                    if (completedExceptionally == null) {
                        return unboxState;
                    }
                    throw completedExceptionally.cause;
                }
                TimeSourceKt.getTimeSource().parkNanos(this, processNextEvent);
            }
            InterruptedException interruptedException = new InterruptedException();
            cancel(interruptedException);
            throw interruptedException;
        } catch (Throwable th) {
            TimeSourceKt.getTimeSource().unregisterTimeLoopThread();
            throw th;
        }
    }
}
