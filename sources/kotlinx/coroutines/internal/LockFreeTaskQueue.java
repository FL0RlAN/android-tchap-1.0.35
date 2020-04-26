package kotlinx.coroutines.internal;

import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore.Companion;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore.Placeholder;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0010\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0013\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00028\u0000¢\u0006\u0002\u0010\u0012J\u0006\u0010\u0013\u001a\u00020\u0014J\u0006\u0010\u0015\u001a\u00020\u0004J&\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u00180\u0017\"\u0004\b\u0001\u0010\u00182\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u0002H\u00180\u001aJ\r\u0010\u001b\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010\u001cJ$\u0010\u001d\u001a\u0004\u0018\u00018\u00002\u0012\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u00040\u001aH\b¢\u0006\u0002\u0010\u001fR$\u0010\u0006\u001a\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00028\u00000\bj\b\u0012\u0004\u0012\u00028\u0000`\t0\u0007X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\n\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\r8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000f¨\u0006 "}, d2 = {"Lkotlinx/coroutines/internal/LockFreeTaskQueue;", "E", "", "singleConsumer", "", "(Z)V", "_cur", "Lkotlinx/atomicfu/AtomicRef;", "Lkotlinx/coroutines/internal/LockFreeTaskQueueCore;", "Lkotlinx/coroutines/internal/Core;", "isEmpty", "()Z", "size", "", "getSize", "()I", "addLast", "element", "(Ljava/lang/Object;)Z", "close", "", "isClosed", "map", "", "R", "transform", "Lkotlin/Function1;", "removeFirstOrNull", "()Ljava/lang/Object;", "removeFirstOrNullIf", "predicate", "(Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* compiled from: LockFreeTaskQueue.kt */
public class LockFreeTaskQueue<E> {
    public static final /* synthetic */ AtomicReferenceFieldUpdater _cur$FU$internal = AtomicReferenceFieldUpdater.newUpdater(LockFreeTaskQueue.class, Object.class, "_cur$internal");
    public volatile /* synthetic */ Object _cur$internal;

    public LockFreeTaskQueue(boolean z) {
        this._cur$internal = new LockFreeTaskQueueCore(8, z);
    }

    public final boolean isEmpty() {
        return ((LockFreeTaskQueueCore) this._cur$internal).isEmpty();
    }

    public final int getSize() {
        return ((LockFreeTaskQueueCore) this._cur$internal).getSize();
    }

    public final <R> List<R> map(Function1<? super E, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        return ((LockFreeTaskQueueCore) this._cur$internal).map(function1);
    }

    public final boolean isClosed() {
        return ((LockFreeTaskQueueCore) this._cur$internal).isClosed();
    }

    public final void close() {
        while (true) {
            LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) this._cur$internal;
            if (!lockFreeTaskQueueCore.close()) {
                _cur$FU$internal.compareAndSet(this, lockFreeTaskQueueCore, lockFreeTaskQueueCore.next());
            } else {
                return;
            }
        }
    }

    public final boolean addLast(E e) {
        Intrinsics.checkParameterIsNotNull(e, "element");
        while (true) {
            LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) this._cur$internal;
            int addLast = lockFreeTaskQueueCore.addLast(e);
            if (addLast == 0) {
                return true;
            }
            if (addLast == 1) {
                _cur$FU$internal.compareAndSet(this, lockFreeTaskQueueCore, lockFreeTaskQueueCore.next());
            } else if (addLast == 2) {
                return false;
            }
        }
    }

    public final E removeFirstOrNull() {
        E e;
        E e2;
        while (true) {
            LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) this._cur$internal;
            while (true) {
                long j = lockFreeTaskQueueCore._state$internal;
                e = null;
                if ((LockFreeTaskQueueCore.FROZEN_MASK & j) == 0) {
                    Companion companion = LockFreeTaskQueueCore.Companion;
                    int i = (int) ((LockFreeTaskQueueCore.HEAD_MASK & j) >> 0);
                    if ((lockFreeTaskQueueCore.mask & ((int) ((LockFreeTaskQueueCore.TAIL_MASK & j) >> 30))) != (lockFreeTaskQueueCore.mask & i)) {
                        e2 = lockFreeTaskQueueCore.array.get(lockFreeTaskQueueCore.mask & i);
                        if (e2 != null) {
                            if (!(e2 instanceof Placeholder)) {
                                int i2 = (i + 1) & LockFreeTaskQueueCore.MAX_CAPACITY_MASK;
                                if (!LockFreeTaskQueueCore._state$FU$internal.compareAndSet(lockFreeTaskQueueCore, j, LockFreeTaskQueueCore.Companion.updateHead(j, i2))) {
                                    if (lockFreeTaskQueueCore.singleConsumer) {
                                        LockFreeTaskQueueCore lockFreeTaskQueueCore2 = lockFreeTaskQueueCore;
                                        do {
                                            lockFreeTaskQueueCore2 = lockFreeTaskQueueCore2.removeSlowPath(i, i2);
                                        } while (lockFreeTaskQueueCore2 != null);
                                        break;
                                    }
                                } else {
                                    lockFreeTaskQueueCore.array.set(lockFreeTaskQueueCore.mask & i, null);
                                    break;
                                }
                            } else {
                                break;
                            }
                        } else if (lockFreeTaskQueueCore.singleConsumer) {
                            break;
                        }
                    } else {
                        break;
                    }
                } else {
                    e = LockFreeTaskQueueCore.REMOVE_FROZEN;
                    break;
                }
            }
            e = e2;
            if (e != LockFreeTaskQueueCore.REMOVE_FROZEN) {
                return e;
            }
            _cur$FU$internal.compareAndSet(this, lockFreeTaskQueueCore, lockFreeTaskQueueCore.next());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0094, code lost:
        r7 = r9;
     */
    public final E removeFirstOrNullIf(Function1<? super E, Boolean> function1) {
        E e;
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        while (true) {
            LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) this._cur$internal;
            while (true) {
                long j = lockFreeTaskQueueCore._state$internal;
                e = null;
                if ((LockFreeTaskQueueCore.FROZEN_MASK & j) == 0) {
                    Companion companion = LockFreeTaskQueueCore.Companion;
                    int i = (int) ((LockFreeTaskQueueCore.HEAD_MASK & j) >> 0);
                    if ((lockFreeTaskQueueCore.mask & ((int) ((LockFreeTaskQueueCore.TAIL_MASK & j) >> 30))) != (lockFreeTaskQueueCore.mask & i)) {
                        E e2 = lockFreeTaskQueueCore.array.get(lockFreeTaskQueueCore.mask & i);
                        if (e2 != null) {
                            if ((e2 instanceof Placeholder) || !((Boolean) function1.invoke(e2)).booleanValue()) {
                                break;
                            }
                            int i2 = (i + 1) & LockFreeTaskQueueCore.MAX_CAPACITY_MASK;
                            if (!LockFreeTaskQueueCore._state$FU$internal.compareAndSet(lockFreeTaskQueueCore, j, LockFreeTaskQueueCore.Companion.updateHead(j, i2))) {
                                if (lockFreeTaskQueueCore.singleConsumer) {
                                    LockFreeTaskQueueCore lockFreeTaskQueueCore2 = lockFreeTaskQueueCore;
                                    do {
                                        lockFreeTaskQueueCore2 = lockFreeTaskQueueCore2.removeSlowPath(i, i2);
                                    } while (lockFreeTaskQueueCore2 != null);
                                    break;
                                }
                            } else {
                                lockFreeTaskQueueCore.array.set(lockFreeTaskQueueCore.mask & i, null);
                                break;
                            }
                        } else if (lockFreeTaskQueueCore.singleConsumer) {
                            break;
                        }
                    } else {
                        break;
                    }
                } else {
                    e = LockFreeTaskQueueCore.REMOVE_FROZEN;
                    break;
                }
            }
            if (e != LockFreeTaskQueueCore.REMOVE_FROZEN) {
                return e;
            }
            _cur$FU$internal.compareAndSet(this, lockFreeTaskQueueCore, lockFreeTaskQueueCore.next());
        }
    }
}
