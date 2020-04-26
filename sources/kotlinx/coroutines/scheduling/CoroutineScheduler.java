package kotlinx.coroutines.scheduling;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin._Assertions;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.TimeSourceKt;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.SystemPropsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001c\b\u0000\u0018\u0000 E2\u00020\u00012\u00020\u0002:\u0003EFGB)\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0011\u0010\r\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020\u0007H\bJ\b\u0010#\u001a\u00020$H\u0016J\b\u0010%\u001a\u00020\u0004H\u0002J!\u0010&\u001a\u00020'2\n\u0010(\u001a\u00060)j\u0002`*2\u0006\u0010+\u001a\u00020,H\u0000¢\u0006\u0002\b-J\u0011\u0010\u0014\u001a\u00020\u00042\u0006\u0010\"\u001a\u00020\u0007H\bJ\t\u0010.\u001a\u00020$H\bJ\t\u0010/\u001a\u00020\u0004H\bJ&\u00100\u001a\u00020$2\n\u0010(\u001a\u00060)j\u0002`*2\b\b\u0002\u0010+\u001a\u00020,2\b\b\u0002\u00101\u001a\u00020\u0019J\u0014\u00102\u001a\u00020$2\n\u00103\u001a\u00060)j\u0002`*H\u0016J\t\u00104\u001a\u00020$H\bJ\t\u00105\u001a\u00020\u0004H\bJ\u0014\u00106\u001a\u00020\u00042\n\u00107\u001a\u00060 R\u00020\u0000H\u0002J\u000e\u00108\u001a\b\u0018\u00010 R\u00020\u0000H\u0002J\u0014\u00109\u001a\u00020$2\n\u00107\u001a\u00060 R\u00020\u0000H\u0002J$\u0010:\u001a\u00020$2\n\u00107\u001a\u00060 R\u00020\u00002\u0006\u0010;\u001a\u00020\u00042\u0006\u0010<\u001a\u00020\u0004H\u0002J\b\u0010=\u001a\u00020$H\u0002J\u0010\u0010>\u001a\u00020$2\u0006\u0010?\u001a\u00020'H\u0002J\u000e\u0010@\u001a\u00020$2\u0006\u0010A\u001a\u00020\u0007J\u0018\u0010B\u001a\u00020\u00042\u0006\u0010?\u001a\u00020'2\u0006\u00101\u001a\u00020\u0019H\u0002J\b\u0010C\u001a\u00020\tH\u0016J\b\u0010D\u001a\u00020\u0019H\u0002R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u0015\u0010\r\u001a\u00020\u00048Â\u0002X\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u0015\u0010\u0014\u001a\u00020\u00048Â\u0002X\u0004¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u000fR\u000e\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\u00020\u00198BX\u0004¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u001aR\u000e\u0010\u0005\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0018\u00010 R\u00020\u00000\u001fX\u0004¢\u0006\u0004\n\u0002\u0010!¨\u0006H"}, d2 = {"Lkotlinx/coroutines/scheduling/CoroutineScheduler;", "Ljava/util/concurrent/Executor;", "Ljava/io/Closeable;", "corePoolSize", "", "maxPoolSize", "idleWorkerKeepAliveNs", "", "schedulerName", "", "(IIJLjava/lang/String;)V", "_isTerminated", "Lkotlinx/atomicfu/AtomicInt;", "blockingWorkers", "getBlockingWorkers", "()I", "controlState", "Lkotlinx/atomicfu/AtomicLong;", "cpuPermits", "Ljava/util/concurrent/Semaphore;", "createdWorkers", "getCreatedWorkers", "globalQueue", "Lkotlinx/coroutines/scheduling/GlobalQueue;", "isTerminated", "", "()Z", "parkedWorkersStack", "random", "Ljava/util/Random;", "workers", "", "Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;", "[Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;", "state", "close", "", "createNewWorker", "createTask", "Lkotlinx/coroutines/scheduling/Task;", "block", "Ljava/lang/Runnable;", "Lkotlinx/coroutines/Runnable;", "taskContext", "Lkotlinx/coroutines/scheduling/TaskContext;", "createTask$kotlinx_coroutines_core", "decrementBlockingWorkers", "decrementCreatedWorkers", "dispatch", "fair", "execute", "command", "incrementBlockingWorkers", "incrementCreatedWorkers", "parkedWorkersStackNextIndex", "worker", "parkedWorkersStackPop", "parkedWorkersStackPush", "parkedWorkersStackTopUpdate", "oldIndex", "newIndex", "requestCpuWorker", "runSafely", "task", "shutdown", "timeout", "submitToLocalQueue", "toString", "tryUnpark", "Companion", "Worker", "WorkerState", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* compiled from: CoroutineScheduler.kt */
public final class CoroutineScheduler implements Executor, Closeable {
    private static final int ADDED = -1;
    private static final int ADDED_REQUIRES_HELP = 0;
    private static final int ALLOWED = 0;
    private static final long BLOCKING_MASK = 4398044413952L;
    private static final int BLOCKING_SHIFT = 21;
    private static final long CREATED_MASK = 2097151;
    public static final Companion Companion = new Companion(null);
    private static final int FORBIDDEN = -1;
    /* access modifiers changed from: private */
    public static final int MAX_PARK_TIME_NS = ((int) TimeUnit.SECONDS.toNanos(1));
    /* access modifiers changed from: private */
    public static final int MAX_SPINS = SystemPropsKt.systemProp$default("kotlinx.coroutines.scheduler.spins", 1000, 1, 0, 8, (Object) null);
    public static final int MAX_SUPPORTED_POOL_SIZE = 2097150;
    /* access modifiers changed from: private */
    public static final int MAX_YIELDS = (MAX_SPINS + SystemPropsKt.systemProp$default("kotlinx.coroutines.scheduler.yields", 0, 0, 0, 8, (Object) null));
    /* access modifiers changed from: private */
    public static final int MIN_PARK_TIME_NS = ((int) RangesKt.coerceAtMost(RangesKt.coerceAtLeast(TasksKt.WORK_STEALING_TIME_RESOLUTION_NS / ((long) 4), 10), (long) MAX_PARK_TIME_NS));
    public static final int MIN_SUPPORTED_POOL_SIZE = 1;
    private static final int NOT_ADDED = 1;
    /* access modifiers changed from: private */
    public static final Symbol NOT_IN_STACK = new Symbol("NOT_IN_STACK");
    private static final long PARKED_INDEX_MASK = 2097151;
    private static final long PARKED_VERSION_INC = 2097152;
    private static final long PARKED_VERSION_MASK = -2097152;
    private static final int TERMINATED = 1;
    private static final AtomicIntegerFieldUpdater _isTerminated$FU;
    static final AtomicLongFieldUpdater controlState$FU;
    private static final AtomicLongFieldUpdater parkedWorkersStack$FU;
    private volatile int _isTerminated;
    volatile long controlState;
    /* access modifiers changed from: private */
    public final int corePoolSize;
    /* access modifiers changed from: private */
    public final Semaphore cpuPermits;
    /* access modifiers changed from: private */
    public final GlobalQueue globalQueue;
    /* access modifiers changed from: private */
    public final long idleWorkerKeepAliveNs;
    private final int maxPoolSize;
    private volatile long parkedWorkersStack;
    /* access modifiers changed from: private */
    public final Random random;
    /* access modifiers changed from: private */
    public final String schedulerName;
    /* access modifiers changed from: private */
    public final Worker[] workers;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\u00020\u00048\u0002X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\r\u0010\u0002R\u000e\u0010\u000e\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0011\u001a\u00020\u00048\u0002X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0012\u0010\u0002R\u000e\u0010\u0013\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"Lkotlinx/coroutines/scheduling/CoroutineScheduler$Companion;", "", "()V", "ADDED", "", "ADDED_REQUIRES_HELP", "ALLOWED", "BLOCKING_MASK", "", "BLOCKING_SHIFT", "CREATED_MASK", "FORBIDDEN", "MAX_PARK_TIME_NS", "MAX_PARK_TIME_NS$annotations", "MAX_SPINS", "MAX_SUPPORTED_POOL_SIZE", "MAX_YIELDS", "MIN_PARK_TIME_NS", "MIN_PARK_TIME_NS$annotations", "MIN_SUPPORTED_POOL_SIZE", "NOT_ADDED", "NOT_IN_STACK", "Lkotlinx/coroutines/internal/Symbol;", "PARKED_INDEX_MASK", "PARKED_VERSION_INC", "PARKED_VERSION_MASK", "TERMINATED", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* compiled from: CoroutineScheduler.kt */
    public static final class Companion {
        @JvmStatic
        private static /* synthetic */ void MAX_PARK_TIME_NS$annotations() {
        }

        @JvmStatic
        private static /* synthetic */ void MIN_PARK_TIME_NS$annotations() {
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 13})
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0 = new int[WorkerState.values().length];

        static {
            $EnumSwitchMapping$0[WorkerState.PARKING.ordinal()] = 1;
            $EnumSwitchMapping$0[WorkerState.BLOCKING.ordinal()] = 2;
            $EnumSwitchMapping$0[WorkerState.CPU_ACQUIRED.ordinal()] = 3;
            $EnumSwitchMapping$0[WorkerState.RETIRING.ordinal()] = 4;
            $EnumSwitchMapping$0[WorkerState.TERMINATED.ordinal()] = 5;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0011\b\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004B\u0007\b\u0002¢\u0006\u0002\u0010\u0005J\u0010\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020/H\u0002J\u0018\u00100\u001a\u00020-2\u0006\u0010.\u001a\u00020/2\u0006\u00101\u001a\u00020\u0010H\u0002J\b\u00102\u001a\u00020\fH\u0002J\b\u00103\u001a\u00020-H\u0002J\b\u00104\u001a\u00020-H\u0002J\u0010\u00105\u001a\u00020\f2\u0006\u00106\u001a\u00020\u0010H\u0002J\u000f\u00107\u001a\u0004\u0018\u000108H\u0000¢\u0006\u0002\b9J\n\u0010:\u001a\u0004\u0018\u000108H\u0002J\u0010\u0010;\u001a\u00020-2\u0006\u0010<\u001a\u00020/H\u0002J\u0006\u0010=\u001a\u00020-J\u0015\u0010>\u001a\u00020\u00032\u0006\u0010?\u001a\u00020\u0003H\u0000¢\u0006\u0002\b@J\b\u0010A\u001a\u00020-H\u0016J\u0006\u0010B\u001a\u00020\fJ\u0006\u0010C\u001a\u00020\fJ\u0015\u0010D\u001a\u00020\f2\u0006\u0010E\u001a\u00020$H\u0000¢\u0006\u0002\bFJ\n\u0010G\u001a\u0004\u0018\u000108H\u0002J\b\u0010H\u001a\u00020-H\u0002R$\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\f8F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\rR\u0011\u0010\u000e\u001a\u00020\f8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\rR\u000e\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0012\u001a\u00020\u0013¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u001c\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001bR\u000e\u0010\u001c\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u001e\u001a\u00020\u001f8F¢\u0006\u0006\u001a\u0004\b \u0010!R\u000e\u0010\"\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010#\u001a\u00020$X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010&\"\u0004\b'\u0010(R\u000e\u0010)\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020+X\u0004¢\u0006\u0002\n\u0000¨\u0006I"}, d2 = {"Lkotlinx/coroutines/scheduling/CoroutineScheduler$Worker;", "Ljava/lang/Thread;", "index", "", "(Lkotlinx/coroutines/scheduling/CoroutineScheduler;I)V", "(Lkotlinx/coroutines/scheduling/CoroutineScheduler;)V", "indexInArray", "getIndexInArray", "()I", "setIndexInArray", "(I)V", "isBlocking", "", "()Z", "isParking", "lastExhaustionTime", "", "lastStealIndex", "localQueue", "Lkotlinx/coroutines/scheduling/WorkQueue;", "getLocalQueue", "()Lkotlinx/coroutines/scheduling/WorkQueue;", "nextParkedWorker", "", "getNextParkedWorker", "()Ljava/lang/Object;", "setNextParkedWorker", "(Ljava/lang/Object;)V", "parkTimeNs", "rngState", "scheduler", "Lkotlinx/coroutines/scheduling/CoroutineScheduler;", "getScheduler", "()Lkotlinx/coroutines/scheduling/CoroutineScheduler;", "spins", "state", "Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;", "getState", "()Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;", "setState", "(Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;)V", "terminationDeadline", "terminationState", "Lkotlinx/atomicfu/AtomicInt;", "afterTask", "", "taskMode", "Lkotlinx/coroutines/scheduling/TaskMode;", "beforeTask", "taskSubmissionTime", "blockingQuiescence", "blockingWorkerIdle", "cpuWorkerIdle", "doPark", "nanos", "findTask", "Lkotlinx/coroutines/scheduling/Task;", "findTask$kotlinx_coroutines_core", "findTaskWithCpuPermit", "idleReset", "mode", "idleResetBeforeUnpark", "nextInt", "upperBound", "nextInt$kotlinx_coroutines_core", "run", "tryAcquireCpuPermit", "tryForbidTermination", "tryReleaseCpu", "newState", "tryReleaseCpu$kotlinx_coroutines_core", "trySteal", "tryTerminateWorker", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* compiled from: CoroutineScheduler.kt */
    public final class Worker extends Thread {
        private static final AtomicIntegerFieldUpdater terminationState$FU = AtomicIntegerFieldUpdater.newUpdater(Worker.class, "terminationState");
        private volatile int indexInArray;
        private long lastExhaustionTime;
        private int lastStealIndex;
        private final WorkQueue localQueue;
        private volatile Object nextParkedWorker;
        private int parkTimeNs;
        private int rngState;
        private volatile int spins;
        private volatile WorkerState state;
        private long terminationDeadline;
        private volatile int terminationState;

        private Worker() {
            setDaemon(true);
            this.localQueue = new WorkQueue();
            this.state = WorkerState.RETIRING;
            this.terminationState = 0;
            this.nextParkedWorker = CoroutineScheduler.NOT_IN_STACK;
            this.parkTimeNs = CoroutineScheduler.MIN_PARK_TIME_NS;
            this.rngState = CoroutineScheduler.this.random.nextInt();
        }

        public final int getIndexInArray() {
            return this.indexInArray;
        }

        public final void setIndexInArray(int i) {
            StringBuilder sb = new StringBuilder();
            sb.append(CoroutineScheduler.this.schedulerName);
            sb.append("-worker-");
            sb.append(i == 0 ? "TERMINATED" : String.valueOf(i));
            setName(sb.toString());
            this.indexInArray = i;
        }

        public Worker(CoroutineScheduler coroutineScheduler, int i) {
            this();
            setIndexInArray(i);
        }

        public final CoroutineScheduler getScheduler() {
            return CoroutineScheduler.this;
        }

        public final WorkQueue getLocalQueue() {
            return this.localQueue;
        }

        public final WorkerState getState() {
            return this.state;
        }

        public final void setState(WorkerState workerState) {
            Intrinsics.checkParameterIsNotNull(workerState, "<set-?>");
            this.state = workerState;
        }

        public final boolean isParking() {
            return this.state == WorkerState.PARKING;
        }

        public final boolean isBlocking() {
            return this.state == WorkerState.BLOCKING;
        }

        public final Object getNextParkedWorker() {
            return this.nextParkedWorker;
        }

        public final void setNextParkedWorker(Object obj) {
            this.nextParkedWorker = obj;
        }

        public final boolean tryForbidTermination() {
            int i = this.terminationState;
            if (i == -1) {
                return false;
            }
            if (i == 0) {
                return terminationState$FU.compareAndSet(this, 0, -1);
            }
            if (i == 1) {
                return false;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid terminationState = ");
            sb.append(i);
            throw new IllegalStateException(sb.toString().toString());
        }

        public final boolean tryAcquireCpuPermit() {
            if (this.state == WorkerState.CPU_ACQUIRED) {
                return true;
            }
            if (!CoroutineScheduler.this.cpuPermits.tryAcquire()) {
                return false;
            }
            this.state = WorkerState.CPU_ACQUIRED;
            return true;
        }

        public final boolean tryReleaseCpu$kotlinx_coroutines_core(WorkerState workerState) {
            Intrinsics.checkParameterIsNotNull(workerState, "newState");
            WorkerState workerState2 = this.state;
            boolean z = workerState2 == WorkerState.CPU_ACQUIRED;
            if (z) {
                CoroutineScheduler.this.cpuPermits.release();
            }
            if (workerState2 != workerState) {
                this.state = workerState;
            }
            return z;
        }

        public void run() {
            boolean z = false;
            while (!CoroutineScheduler.this.isTerminated() && this.state != WorkerState.TERMINATED) {
                Task findTask$kotlinx_coroutines_core = findTask$kotlinx_coroutines_core();
                if (findTask$kotlinx_coroutines_core == null) {
                    if (this.state == WorkerState.CPU_ACQUIRED) {
                        cpuWorkerIdle();
                    } else {
                        blockingWorkerIdle();
                    }
                    z = true;
                } else {
                    TaskMode mode = findTask$kotlinx_coroutines_core.getMode();
                    if (z) {
                        idleReset(mode);
                        z = false;
                    }
                    beforeTask(mode, findTask$kotlinx_coroutines_core.submissionTime);
                    CoroutineScheduler.this.runSafely(findTask$kotlinx_coroutines_core);
                    afterTask(mode);
                }
            }
            tryReleaseCpu$kotlinx_coroutines_core(WorkerState.TERMINATED);
        }

        private final void beforeTask(TaskMode taskMode, long j) {
            if (taskMode != TaskMode.NON_BLOCKING) {
                CoroutineScheduler.controlState$FU.addAndGet(CoroutineScheduler.this, 2097152);
                if (tryReleaseCpu$kotlinx_coroutines_core(WorkerState.BLOCKING)) {
                    CoroutineScheduler.this.requestCpuWorker();
                }
            } else if (CoroutineScheduler.this.cpuPermits.availablePermits() != 0) {
                long nanoTime = TasksKt.schedulerTimeSource.nanoTime();
                if (nanoTime - j >= TasksKt.WORK_STEALING_TIME_RESOLUTION_NS && nanoTime - this.lastExhaustionTime >= TasksKt.WORK_STEALING_TIME_RESOLUTION_NS * ((long) 5)) {
                    this.lastExhaustionTime = nanoTime;
                    CoroutineScheduler.this.requestCpuWorker();
                }
            }
        }

        private final void afterTask(TaskMode taskMode) {
            if (taskMode != TaskMode.NON_BLOCKING) {
                CoroutineScheduler.controlState$FU.addAndGet(CoroutineScheduler.this, CoroutineScheduler.PARKED_VERSION_MASK);
                WorkerState workerState = this.state;
                if (workerState != WorkerState.TERMINATED) {
                    boolean z = workerState == WorkerState.BLOCKING;
                    if (!_Assertions.ENABLED || z) {
                        this.state = WorkerState.RETIRING;
                        return;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Expected BLOCKING state, but has ");
                    sb.append(workerState);
                    throw new AssertionError(sb.toString());
                }
            }
        }

        public final int nextInt$kotlinx_coroutines_core(int i) {
            int i2 = this.rngState;
            this.rngState = i2 ^ (i2 << 13);
            int i3 = this.rngState;
            this.rngState = i3 ^ (i3 >> 17);
            int i4 = this.rngState;
            this.rngState = i4 ^ (i4 << 5);
            int i5 = i - 1;
            if ((i5 & i) == 0) {
                return this.rngState & i5;
            }
            return (this.rngState & Integer.MAX_VALUE) % i;
        }

        private final void cpuWorkerIdle() {
            int i = this.spins;
            if (i <= CoroutineScheduler.MAX_YIELDS) {
                this.spins = i + 1;
                if (i >= CoroutineScheduler.MAX_SPINS) {
                    Thread.yield();
                    return;
                }
                return;
            }
            if (this.parkTimeNs < CoroutineScheduler.MAX_PARK_TIME_NS) {
                this.parkTimeNs = RangesKt.coerceAtMost((this.parkTimeNs * 3) >>> 1, CoroutineScheduler.MAX_PARK_TIME_NS);
            }
            tryReleaseCpu$kotlinx_coroutines_core(WorkerState.PARKING);
            doPark((long) this.parkTimeNs);
        }

        private final void blockingWorkerIdle() {
            tryReleaseCpu$kotlinx_coroutines_core(WorkerState.PARKING);
            if (blockingQuiescence()) {
                this.terminationState = 0;
                if (this.terminationDeadline == 0) {
                    this.terminationDeadline = System.nanoTime() + CoroutineScheduler.this.idleWorkerKeepAliveNs;
                }
                if (doPark(CoroutineScheduler.this.idleWorkerKeepAliveNs) && System.nanoTime() - this.terminationDeadline >= 0) {
                    this.terminationDeadline = 0;
                    tryTerminateWorker();
                }
            }
        }

        private final boolean doPark(long j) {
            CoroutineScheduler.this.parkedWorkersStackPush(this);
            if (!blockingQuiescence()) {
                return false;
            }
            LockSupport.parkNanos(j);
            return true;
        }

        private final void tryTerminateWorker() {
            synchronized (CoroutineScheduler.this.workers) {
                if (!CoroutineScheduler.this.isTerminated()) {
                    if (CoroutineScheduler.this.getCreatedWorkers() > CoroutineScheduler.this.corePoolSize) {
                        if (blockingQuiescence()) {
                            if (terminationState$FU.compareAndSet(this, 0, 1)) {
                                int i = this.indexInArray;
                                setIndexInArray(0);
                                CoroutineScheduler.this.parkedWorkersStackTopUpdate(this, i, 0);
                                int andDecrement = (int) (CoroutineScheduler.controlState$FU.getAndDecrement(CoroutineScheduler.this) & 2097151);
                                if (andDecrement != i) {
                                    Worker worker = CoroutineScheduler.this.workers[andDecrement];
                                    if (worker == null) {
                                        Intrinsics.throwNpe();
                                    }
                                    CoroutineScheduler.this.workers[i] = worker;
                                    worker.setIndexInArray(i);
                                    CoroutineScheduler.this.parkedWorkersStackTopUpdate(worker, andDecrement, i);
                                }
                                CoroutineScheduler.this.workers[andDecrement] = null;
                                Unit unit = Unit.INSTANCE;
                                this.state = WorkerState.TERMINATED;
                            }
                        }
                    }
                }
            }
        }

        private final boolean blockingQuiescence() {
            Task removeFirstWithModeOrNull = CoroutineScheduler.this.globalQueue.removeFirstWithModeOrNull(TaskMode.PROBABLY_BLOCKING);
            if (removeFirstWithModeOrNull == null) {
                return true;
            }
            this.localQueue.add(removeFirstWithModeOrNull, CoroutineScheduler.this.globalQueue);
            return false;
        }

        private final void idleReset(TaskMode taskMode) {
            this.terminationDeadline = 0;
            this.lastStealIndex = 0;
            if (this.state == WorkerState.PARKING) {
                boolean z = taskMode == TaskMode.PROBABLY_BLOCKING;
                if (!_Assertions.ENABLED || z) {
                    this.state = WorkerState.BLOCKING;
                    this.parkTimeNs = CoroutineScheduler.MIN_PARK_TIME_NS;
                } else {
                    throw new AssertionError("Assertion failed");
                }
            }
            this.spins = 0;
        }

        public final void idleResetBeforeUnpark() {
            this.parkTimeNs = CoroutineScheduler.MIN_PARK_TIME_NS;
            this.spins = 0;
        }

        public final Task findTask$kotlinx_coroutines_core() {
            if (tryAcquireCpuPermit()) {
                return findTaskWithCpuPermit();
            }
            Task poll = this.localQueue.poll();
            if (poll == null) {
                poll = CoroutineScheduler.this.globalQueue.removeFirstWithModeOrNull(TaskMode.PROBABLY_BLOCKING);
            }
            return poll;
        }

        private final Task findTaskWithCpuPermit() {
            boolean z = nextInt$kotlinx_coroutines_core(CoroutineScheduler.this.corePoolSize * 2) == 0;
            if (z) {
                Task removeFirstWithModeOrNull = CoroutineScheduler.this.globalQueue.removeFirstWithModeOrNull(TaskMode.NON_BLOCKING);
                if (removeFirstWithModeOrNull != null) {
                    return removeFirstWithModeOrNull;
                }
            }
            Task poll = this.localQueue.poll();
            if (poll != null) {
                return poll;
            }
            if (!z) {
                Task task = (Task) CoroutineScheduler.this.globalQueue.removeFirstOrNull();
                if (task != null) {
                    return task;
                }
            }
            return trySteal();
        }

        private final Task trySteal() {
            int access$getCreatedWorkers$p = CoroutineScheduler.this.getCreatedWorkers();
            if (access$getCreatedWorkers$p < 2) {
                return null;
            }
            int i = this.lastStealIndex;
            if (i == 0) {
                i = nextInt$kotlinx_coroutines_core(access$getCreatedWorkers$p);
            }
            int i2 = i + 1;
            if (i2 > access$getCreatedWorkers$p) {
                i2 = 1;
            }
            this.lastStealIndex = i2;
            Worker worker = CoroutineScheduler.this.workers[i2];
            if (worker == null || worker == this || !this.localQueue.trySteal(worker.localQueue, CoroutineScheduler.this.globalQueue)) {
                return null;
            }
            return this.localQueue.poll();
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/scheduling/CoroutineScheduler$WorkerState;", "", "(Ljava/lang/String;I)V", "CPU_ACQUIRED", "BLOCKING", "PARKING", "RETIRING", "TERMINATED", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* compiled from: CoroutineScheduler.kt */
    public enum WorkerState {
        CPU_ACQUIRED,
        BLOCKING,
        PARKING,
        RETIRING,
        TERMINATED
    }

    private final int blockingWorkers(long j) {
        return (int) ((j & BLOCKING_MASK) >> 21);
    }

    /* access modifiers changed from: private */
    public final int createdWorkers(long j) {
        return (int) (j & 2097151);
    }

    public CoroutineScheduler(int i, int i2, long j, String str) {
        Intrinsics.checkParameterIsNotNull(str, "schedulerName");
        this.corePoolSize = i;
        this.maxPoolSize = i2;
        this.idleWorkerKeepAliveNs = j;
        this.schedulerName = str;
        if (this.corePoolSize >= 1) {
            String str2 = "Max pool size ";
            if (this.maxPoolSize >= this.corePoolSize) {
                if (this.maxPoolSize <= 2097150) {
                    if (this.idleWorkerKeepAliveNs > 0) {
                        this.globalQueue = new GlobalQueue();
                        this.cpuPermits = new Semaphore(this.corePoolSize, false);
                        this.parkedWorkersStack = 0;
                        this.workers = new Worker[(this.maxPoolSize + 1)];
                        this.controlState = 0;
                        this.random = new Random();
                        this._isTerminated = 0;
                        return;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Idle worker keep alive time ");
                    sb.append(this.idleWorkerKeepAliveNs);
                    sb.append(" must be positive");
                    throw new IllegalArgumentException(sb.toString().toString());
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str2);
                sb2.append(this.maxPoolSize);
                sb2.append(" should not exceed maximal supported number of threads 2097150");
                throw new IllegalArgumentException(sb2.toString().toString());
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str2);
            sb3.append(this.maxPoolSize);
            sb3.append(" should be greater than or equals to core pool size ");
            sb3.append(this.corePoolSize);
            throw new IllegalArgumentException(sb3.toString().toString());
        }
        StringBuilder sb4 = new StringBuilder();
        sb4.append("Core pool size ");
        sb4.append(this.corePoolSize);
        sb4.append(" should be at least 1");
        throw new IllegalArgumentException(sb4.toString().toString());
    }

    public /* synthetic */ CoroutineScheduler(int i, int i2, long j, String str, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i3 & 4) != 0) {
            j = TasksKt.IDLE_WORKER_KEEP_ALIVE_NS;
        }
        long j2 = j;
        if ((i3 & 8) != 0) {
            str = TasksKt.DEFAULT_SCHEDULER_NAME;
        }
        this(i, i2, j2, str);
    }

    /* access modifiers changed from: private */
    public final void parkedWorkersStackPush(Worker worker) {
        long j;
        long j2;
        int indexInArray;
        if (worker.getNextParkedWorker() == NOT_IN_STACK) {
            do {
                j = this.parkedWorkersStack;
                int i = (int) (2097151 & j);
                j2 = (2097152 + j) & PARKED_VERSION_MASK;
                indexInArray = worker.getIndexInArray();
                boolean z = indexInArray != 0;
                if (!_Assertions.ENABLED || z) {
                    worker.setNextParkedWorker(this.workers[i]);
                } else {
                    throw new AssertionError("Assertion failed");
                }
            } while (!parkedWorkersStack$FU.compareAndSet(this, j, ((long) indexInArray) | j2));
        }
    }

    private final int parkedWorkersStackNextIndex(Worker worker) {
        Object nextParkedWorker = worker.getNextParkedWorker();
        while (nextParkedWorker != NOT_IN_STACK) {
            if (nextParkedWorker == null) {
                return 0;
            }
            Worker worker2 = (Worker) nextParkedWorker;
            int indexInArray = worker2.getIndexInArray();
            if (indexInArray != 0) {
                return indexInArray;
            }
            nextParkedWorker = worker2.getNextParkedWorker();
        }
        return -1;
    }

    /* access modifiers changed from: private */
    public final int getCreatedWorkers() {
        return (int) (this.controlState & 2097151);
    }

    private final int getBlockingWorkers() {
        return (int) ((this.controlState & BLOCKING_MASK) >> 21);
    }

    private final int incrementCreatedWorkers() {
        return (int) (controlState$FU.incrementAndGet(this) & 2097151);
    }

    /* access modifiers changed from: private */
    public final int decrementCreatedWorkers() {
        return (int) (controlState$FU.getAndDecrement(this) & 2097151);
    }

    /* access modifiers changed from: private */
    public final void incrementBlockingWorkers() {
        controlState$FU.addAndGet(this, 2097152);
    }

    /* access modifiers changed from: private */
    public final void decrementBlockingWorkers() {
        controlState$FU.addAndGet(this, PARKED_VERSION_MASK);
    }

    /* access modifiers changed from: private */
    public final boolean isTerminated() {
        return this._isTerminated != 0;
    }

    static {
        Class<CoroutineScheduler> cls = CoroutineScheduler.class;
        parkedWorkersStack$FU = AtomicLongFieldUpdater.newUpdater(cls, "parkedWorkersStack");
        controlState$FU = AtomicLongFieldUpdater.newUpdater(cls, "controlState");
        _isTerminated$FU = AtomicIntegerFieldUpdater.newUpdater(cls, "_isTerminated");
    }

    public void execute(Runnable runnable) {
        Intrinsics.checkParameterIsNotNull(runnable, "command");
        dispatch$default(this, runnable, null, false, 6, null);
    }

    public void close() {
        shutdown(10000);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0083, code lost:
        if (r9 != null) goto L_0x008e;
     */
    public final void shutdown(long j) {
        int i;
        Task task;
        boolean z = false;
        if (_isTerminated$FU.compareAndSet(this, 0, 1)) {
            Thread currentThread = Thread.currentThread();
            if (!(currentThread instanceof Worker)) {
                currentThread = null;
            }
            Worker worker = (Worker) currentThread;
            synchronized (this.workers) {
                i = (int) (this.controlState & 2097151);
            }
            if (1 <= i) {
                int i2 = 1;
                while (true) {
                    Worker worker2 = this.workers[i2];
                    if (worker2 == null) {
                        Intrinsics.throwNpe();
                    }
                    if (worker2 != worker) {
                        while (worker2.isAlive()) {
                            LockSupport.unpark(worker2);
                            worker2.join(j);
                        }
                        WorkerState state = worker2.getState();
                        if (state == WorkerState.TERMINATED) {
                            worker2.getLocalQueue().offloadAllWork$kotlinx_coroutines_core(this.globalQueue);
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Expected TERMINATED state, but found ");
                            sb.append(state);
                            throw new IllegalStateException(sb.toString().toString());
                        }
                    }
                    if (i2 == i) {
                        break;
                    }
                    i2++;
                }
            }
            this.globalQueue.close();
            while (true) {
                if (worker != null) {
                    task = worker.findTask$kotlinx_coroutines_core();
                }
                task = (Task) this.globalQueue.removeFirstOrNull();
                if (task == null) {
                    break;
                }
                runSafely(task);
            }
            if (worker != null) {
                worker.tryReleaseCpu$kotlinx_coroutines_core(WorkerState.TERMINATED);
            }
            if (this.cpuPermits.availablePermits() == this.corePoolSize) {
                z = true;
            }
            if (!_Assertions.ENABLED || z) {
                this.parkedWorkersStack = 0;
                this.controlState = 0;
                return;
            }
            throw new AssertionError("Assertion failed");
        }
    }

    public static /* synthetic */ void dispatch$default(CoroutineScheduler coroutineScheduler, Runnable runnable, TaskContext taskContext, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            taskContext = NonBlockingContext.INSTANCE;
        }
        if ((i & 4) != 0) {
            z = false;
        }
        coroutineScheduler.dispatch(runnable, taskContext, z);
    }

    public final void dispatch(Runnable runnable, TaskContext taskContext, boolean z) {
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        Intrinsics.checkParameterIsNotNull(taskContext, "taskContext");
        TimeSourceKt.getTimeSource().trackTask();
        Task createTask$kotlinx_coroutines_core = createTask$kotlinx_coroutines_core(runnable, taskContext);
        int submitToLocalQueue = submitToLocalQueue(createTask$kotlinx_coroutines_core, z);
        if (submitToLocalQueue != -1) {
            if (submitToLocalQueue != 1) {
                requestCpuWorker();
            } else if (this.globalQueue.addLast(createTask$kotlinx_coroutines_core)) {
                requestCpuWorker();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(this.schedulerName);
                sb.append(" was terminated");
                throw new RejectedExecutionException(sb.toString());
            }
        }
    }

    public final Task createTask$kotlinx_coroutines_core(Runnable runnable, TaskContext taskContext) {
        Intrinsics.checkParameterIsNotNull(runnable, "block");
        Intrinsics.checkParameterIsNotNull(taskContext, "taskContext");
        long nanoTime = TasksKt.schedulerTimeSource.nanoTime();
        if (!(runnable instanceof Task)) {
            return new TaskImpl(runnable, nanoTime, taskContext);
        }
        Task task = (Task) runnable;
        task.submissionTime = nanoTime;
        task.taskContext = taskContext;
        return task;
    }

    /* access modifiers changed from: private */
    public final void requestCpuWorker() {
        if (this.cpuPermits.availablePermits() == 0) {
            tryUnpark();
        } else if (!tryUnpark()) {
            long j = this.controlState;
            if (((int) (2097151 & j)) - ((int) ((j & BLOCKING_MASK) >> 21)) < this.corePoolSize) {
                int createNewWorker = createNewWorker();
                if (createNewWorker == 1 && this.corePoolSize > 1) {
                    createNewWorker();
                }
                if (createNewWorker > 0) {
                    return;
                }
            }
            tryUnpark();
        }
    }

    private final boolean tryUnpark() {
        while (true) {
            Worker parkedWorkersStackPop = parkedWorkersStackPop();
            if (parkedWorkersStackPop == null) {
                return false;
            }
            parkedWorkersStackPop.idleResetBeforeUnpark();
            boolean isParking = parkedWorkersStackPop.isParking();
            LockSupport.unpark(parkedWorkersStackPop);
            if (isParking && parkedWorkersStackPop.tryForbidTermination()) {
                return true;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0080, code lost:
        return 0;
     */
    private final int createNewWorker() {
        synchronized (this.workers) {
            if (isTerminated()) {
                return -1;
            }
            long j = this.controlState;
            int i = (int) (j & 2097151);
            int i2 = i - ((int) ((j & BLOCKING_MASK) >> 21));
            boolean z = false;
            if (i2 >= this.corePoolSize) {
                return 0;
            }
            if (i < this.maxPoolSize) {
                if (this.cpuPermits.availablePermits() != 0) {
                    int i3 = ((int) (this.controlState & 2097151)) + 1;
                    if (i3 > 0 && this.workers[i3] == null) {
                        Worker worker = new Worker(this, i3);
                        worker.start();
                        if (i3 == ((int) (2097151 & controlState$FU.incrementAndGet(this)))) {
                            z = true;
                        }
                        if (z) {
                            this.workers[i3] = worker;
                            int i4 = i2 + 1;
                            return i4;
                        }
                        throw new IllegalArgumentException("Failed requirement.".toString());
                    }
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
            }
        }
    }

    private final int submitToLocalQueue(Task task, boolean z) {
        boolean z2;
        Thread currentThread = Thread.currentThread();
        if (!(currentThread instanceof Worker)) {
            currentThread = null;
        }
        Worker worker = (Worker) currentThread;
        if (worker == null || worker.getScheduler() != this || worker.getState() == WorkerState.TERMINATED) {
            return 1;
        }
        int i = -1;
        if (task.getMode() == TaskMode.NON_BLOCKING) {
            if (worker.isBlocking()) {
                i = 0;
            } else if (!worker.tryAcquireCpuPermit()) {
                return 1;
            }
        }
        if (z) {
            z2 = worker.getLocalQueue().addLast(task, this.globalQueue);
        } else {
            z2 = worker.getLocalQueue().add(task, this.globalQueue);
        }
        if (!z2 || worker.getLocalQueue().getBufferSize$kotlinx_coroutines_core() > TasksKt.QUEUE_SIZE_OFFLOAD_THRESHOLD) {
            return 0;
        }
        return i;
    }

    public String toString() {
        Worker[] workerArr;
        ArrayList arrayList = new ArrayList();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        for (Worker worker : this.workers) {
            if (worker != null) {
                int size$kotlinx_coroutines_core = worker.getLocalQueue().size$kotlinx_coroutines_core();
                int i6 = WhenMappings.$EnumSwitchMapping$0[worker.getState().ordinal()];
                if (i6 == 1) {
                    i3++;
                } else if (i6 == 2) {
                    i2++;
                    Collection collection = arrayList;
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.valueOf(size$kotlinx_coroutines_core));
                    sb.append("b");
                    collection.add(sb.toString());
                } else if (i6 == 3) {
                    i++;
                    Collection collection2 = arrayList;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(String.valueOf(size$kotlinx_coroutines_core));
                    sb2.append("c");
                    collection2.add(sb2.toString());
                } else if (i6 == 4) {
                    i4++;
                    if (size$kotlinx_coroutines_core > 0) {
                        Collection collection3 = arrayList;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(String.valueOf(size$kotlinx_coroutines_core));
                        sb3.append("r");
                        collection3.add(sb3.toString());
                    }
                } else if (i6 == 5) {
                    i5++;
                }
            }
        }
        long j = this.controlState;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(this.schedulerName);
        sb4.append('@');
        sb4.append(DebugKt.getHexAddress(this));
        sb4.append('[');
        sb4.append("Pool Size {");
        sb4.append("core = ");
        sb4.append(this.corePoolSize);
        String str = ", ";
        sb4.append(str);
        sb4.append("max = ");
        sb4.append(this.maxPoolSize);
        String str2 = "}, ";
        sb4.append(str2);
        sb4.append("Worker States {");
        sb4.append("CPU = ");
        sb4.append(i);
        sb4.append(str);
        String str3 = "blocking = ";
        sb4.append(str3);
        sb4.append(i2);
        sb4.append(str);
        sb4.append("parked = ");
        sb4.append(i3);
        sb4.append(str);
        sb4.append("retired = ");
        sb4.append(i4);
        sb4.append(str);
        sb4.append("terminated = ");
        sb4.append(i5);
        sb4.append(str2);
        sb4.append("running workers queues = ");
        sb4.append(arrayList);
        sb4.append(str);
        sb4.append("global queue size = ");
        sb4.append(this.globalQueue.getSize());
        sb4.append(str);
        sb4.append("Control State Workers {");
        sb4.append("created = ");
        sb4.append((int) (2097151 & j));
        sb4.append(str);
        sb4.append(str3);
        sb4.append((int) ((j & BLOCKING_MASK) >> 21));
        sb4.append('}');
        sb4.append("]");
        return sb4.toString();
    }

    /* access modifiers changed from: private */
    public final void runSafely(Task task) {
        try {
            task.run();
        } catch (Throwable th) {
            TimeSourceKt.getTimeSource().unTrackTask();
            throw th;
        }
        TimeSourceKt.getTimeSource().unTrackTask();
    }

    /* access modifiers changed from: private */
    public final void parkedWorkersStackTopUpdate(Worker worker, int i, int i2) {
        while (true) {
            long j = this.parkedWorkersStack;
            int i3 = (int) (2097151 & j);
            long j2 = (2097152 + j) & PARKED_VERSION_MASK;
            int i4 = i3 == i ? i2 == 0 ? parkedWorkersStackNextIndex(worker) : i2 : i3;
            if (i4 >= 0) {
                if (parkedWorkersStack$FU.compareAndSet(this, j, j2 | ((long) i4))) {
                    return;
                }
            }
        }
    }

    private final Worker parkedWorkersStackPop() {
        while (true) {
            long j = this.parkedWorkersStack;
            Worker worker = this.workers[(int) (2097151 & j)];
            if (worker == null) {
                return null;
            }
            long j2 = (2097152 + j) & PARKED_VERSION_MASK;
            int parkedWorkersStackNextIndex = parkedWorkersStackNextIndex(worker);
            if (parkedWorkersStackNextIndex >= 0) {
                if (parkedWorkersStack$FU.compareAndSet(this, j, ((long) parkedWorkersStackNextIndex) | j2)) {
                    worker.setNextParkedWorker(NOT_IN_STACK);
                    return worker;
                }
            }
        }
    }
}
