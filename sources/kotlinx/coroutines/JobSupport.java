package kotlinx.coroutines;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.CoroutineContext.Element;
import kotlin.coroutines.CoroutineContext.Key;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlinx.coroutines.Job.DefaultImpls;
import kotlinx.coroutines.internal.ConcurrentKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.LockFreeLinkedListNode.CondAddOp;
import kotlinx.coroutines.internal.OpDescriptor;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.intrinsics.CancellableKt;
import kotlinx.coroutines.intrinsics.UndispatchedKt;
import kotlinx.coroutines.selects.SelectClause0;
import kotlinx.coroutines.selects.SelectInstance;
import org.matrix.androidsdk.crypto.cryptostore.db.model.OutgoingRoomKeyRequestEntityFields;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000è\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010 \n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0001\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0017\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004:\u0006¥\u0001¦\u0001§\u0001B\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J$\u0010-\u001a\u00020\u00062\u0006\u0010.\u001a\u00020\n2\u0006\u0010/\u001a\u0002002\n\u00101\u001a\u0006\u0012\u0002\b\u000302H\u0002J\u000e\u00103\u001a\u00020\"2\u0006\u00104\u001a\u00020\u0002J\u0015\u00105\u001a\u0004\u0018\u00010\nH@ø\u0001\u0000¢\u0006\u0004\b6\u00107J\u0013\u00108\u001a\u0004\u0018\u00010\nH@ø\u0001\u0000¢\u0006\u0002\u00107J\b\u00109\u001a\u00020:H\u0016J\u0012\u00109\u001a\u00020\u00062\b\u0010;\u001a\u0004\u0018\u00010'H\u0016J\u0012\u0010<\u001a\u00020\u00062\b\u0010;\u001a\u0004\u0018\u00010\nH\u0002J\u0012\u0010=\u001a\u00020\u00062\b\u0010;\u001a\u0004\u0018\u00010\nH\u0002J\u0010\u0010>\u001a\u00020\u00062\u0006\u0010;\u001a\u00020'H\u0002J\u0010\u0010?\u001a\u00020\u00062\u0006\u0010;\u001a\u00020'H\u0016J*\u0010@\u001a\u00020:2\u0006\u0010#\u001a\u00020+2\b\u0010A\u001a\u0004\u0018\u00010\n2\u0006\u0010B\u001a\u00020C2\u0006\u0010D\u001a\u00020\u0006H\u0002J\"\u0010E\u001a\u00020:2\u0006\u0010#\u001a\u00020F2\u0006\u0010G\u001a\u00020H2\b\u0010I\u001a\u0004\u0018\u00010\nH\u0002J\u0012\u0010J\u001a\u00020'2\b\u0010;\u001a\u0004\u0018\u00010\nH\u0002J\b\u0010K\u001a\u00020LH\u0002J\u0012\u0010M\u001a\u0004\u0018\u00010H2\u0006\u0010#\u001a\u00020+H\u0002J\n\u0010N\u001a\u00060Oj\u0002`PJ\b\u0010Q\u001a\u00020'H\u0016J\u000f\u0010R\u001a\u0004\u0018\u00010\nH\u0000¢\u0006\u0002\bSJ\n\u0010T\u001a\u0004\u0018\u00010'H\u0004J\b\u0010U\u001a\u0004\u0018\u00010'J \u0010V\u001a\u0004\u0018\u00010'2\u0006\u0010#\u001a\u00020F2\f\u0010W\u001a\b\u0012\u0004\u0012\u00020'0XH\u0002J\u0012\u0010Y\u001a\u0004\u0018\u0001002\u0006\u0010#\u001a\u00020+H\u0002J\u0010\u0010Z\u001a\u00020:2\u0006\u0010[\u001a\u00020'H\u0014J\u0015\u0010\\\u001a\u00020:2\u0006\u0010[\u001a\u00020'H\u0010¢\u0006\u0002\b]J\u0017\u0010^\u001a\u00020:2\b\u0010_\u001a\u0004\u0018\u00010\u0001H\u0000¢\u0006\u0002\b`J?\u0010a\u001a\u00020b2\u0006\u0010c\u001a\u00020\u00062\u0006\u0010d\u001a\u00020\u00062'\u0010e\u001a#\u0012\u0015\u0012\u0013\u0018\u00010'¢\u0006\f\bg\u0012\b\bh\u0012\u0004\b\b(;\u0012\u0004\u0012\u00020:0fj\u0002`iJ/\u0010a\u001a\u00020b2'\u0010e\u001a#\u0012\u0015\u0012\u0013\u0018\u00010'¢\u0006\f\bg\u0012\b\bh\u0012\u0004\b\b(;\u0012\u0004\u0012\u00020:0fj\u0002`iJ\u0011\u0010j\u001a\u00020:H@ø\u0001\u0000¢\u0006\u0002\u00107J\b\u0010k\u001a\u00020\u0006H\u0002J\u0011\u0010l\u001a\u00020:H@ø\u0001\u0000¢\u0006\u0002\u00107J\u001f\u0010m\u001a\u00020n2\u0014\u0010o\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\n\u0012\u0004\u0012\u00020:0fH\bJ\u0012\u0010p\u001a\u00020\u00062\b\u0010;\u001a\u0004\u0018\u00010\nH\u0002J\u0017\u0010q\u001a\u00020\u00062\b\u0010I\u001a\u0004\u0018\u00010\nH\u0000¢\u0006\u0002\brJ\u001f\u0010s\u001a\u00020\u00062\b\u0010I\u001a\u0004\u0018\u00010\n2\u0006\u0010B\u001a\u00020CH\u0000¢\u0006\u0002\btJ=\u0010u\u001a\u0006\u0012\u0002\b\u0003022'\u0010e\u001a#\u0012\u0015\u0012\u0013\u0018\u00010'¢\u0006\f\bg\u0012\b\bh\u0012\u0004\b\b(;\u0012\u0004\u0012\u00020:0fj\u0002`i2\u0006\u0010c\u001a\u00020\u0006H\u0002J\r\u0010v\u001a\u00020wH\u0010¢\u0006\u0002\bxJ\u0018\u0010y\u001a\u00020:2\u0006\u0010/\u001a\u0002002\u0006\u0010;\u001a\u00020'H\u0002J+\u0010z\u001a\u00020:\"\u000e\b\u0000\u0010{\u0018\u0001*\u0006\u0012\u0002\b\u0003022\u0006\u0010/\u001a\u0002002\b\u0010;\u001a\u0004\u0018\u00010'H\bJ\u0012\u0010|\u001a\u00020:2\b\u0010;\u001a\u0004\u0018\u00010'H\u0014J'\u0010}\u001a\u00020:2\b\u0010#\u001a\u0004\u0018\u00010\n2\u0006\u0010B\u001a\u00020C2\u0006\u0010D\u001a\u00020\u0006H\u0010¢\u0006\u0002\b~J\u000e\u0010\u001a\u00020:H\u0010¢\u0006\u0003\b\u0001J\u0010\u0010\u0001\u001a\u00020:2\u0007\u0010\u0001\u001a\u00020\u0003J\u0012\u0010\u0001\u001a\u00020:2\u0007\u0010#\u001a\u00030\u0001H\u0002J\u0015\u0010\u0001\u001a\u00020:2\n\u0010#\u001a\u0006\u0012\u0002\b\u000302H\u0002JH\u0010\u0001\u001a\u00020:\"\u0005\b\u0000\u0010\u00012\u000f\u0010\u0001\u001a\n\u0012\u0005\u0012\u0003H\u00010\u00012\u001e\u0010o\u001a\u001a\b\u0001\u0012\f\u0012\n\u0012\u0005\u0012\u0003H\u00010\u0001\u0012\u0006\u0012\u0004\u0018\u00010\n0fø\u0001\u0000¢\u0006\u0003\u0010\u0001JZ\u0010\u0001\u001a\u00020:\"\u0004\b\u0000\u0010{\"\u0005\b\u0001\u0010\u00012\u000f\u0010\u0001\u001a\n\u0012\u0005\u0012\u0003H\u00010\u00012%\u0010o\u001a!\b\u0001\u0012\u0004\u0012\u0002H{\u0012\f\u0012\n\u0012\u0005\u0012\u0003H\u00010\u0001\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0001H\u0000ø\u0001\u0000¢\u0006\u0006\b\u0001\u0010\u0001J\u001b\u0010\u0001\u001a\u00020:2\n\u00101\u001a\u0006\u0012\u0002\b\u000302H\u0000¢\u0006\u0003\b\u0001JZ\u0010\u0001\u001a\u00020:\"\u0004\b\u0000\u0010{\"\u0005\b\u0001\u0010\u00012\u000f\u0010\u0001\u001a\n\u0012\u0005\u0012\u0003H\u00010\u00012%\u0010o\u001a!\b\u0001\u0012\u0004\u0012\u0002H{\u0012\f\u0012\n\u0012\u0005\u0012\u0003H\u00010\u0001\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0001H\u0000ø\u0001\u0000¢\u0006\u0006\b\u0001\u0010\u0001J\u0007\u0010\u0001\u001a\u00020\u0006J\u0013\u0010\u0001\u001a\u00020C2\b\u0010#\u001a\u0004\u0018\u00010\nH\u0002J\u0013\u0010\u0001\u001a\u00020w2\b\u0010#\u001a\u0004\u0018\u00010\nH\u0002J \u0010\u0001\u001a\u00020\u00062\u0007\u0010\u0001\u001a\u00020'2\f\u0010W\u001a\b\u0012\u0004\u0012\u00020'0XH\u0002J\t\u0010\u0001\u001a\u00020wH\u0007J\t\u0010\u0001\u001a\u00020wH\u0016J#\u0010\u0001\u001a\u00020\u00062\u0006\u0010#\u001a\u00020F2\b\u0010I\u001a\u0004\u0018\u00010\n2\u0006\u0010B\u001a\u00020CH\u0002J#\u0010\u0001\u001a\u00020\u00062\u0006\u0010#\u001a\u00020+2\b\u0010A\u001a\u0004\u0018\u00010\n2\u0006\u0010B\u001a\u00020CH\u0002J\u001a\u0010\u0001\u001a\u00020\u00062\u0006\u0010#\u001a\u00020+2\u0007\u0010\u0001\u001a\u00020'H\u0002J%\u0010\u0001\u001a\u00020C2\b\u0010#\u001a\u0004\u0018\u00010\n2\b\u0010I\u001a\u0004\u0018\u00010\n2\u0006\u0010B\u001a\u00020CH\u0002J$\u0010\u0001\u001a\u00020\u00062\u0006\u0010#\u001a\u00020F2\u0006\u00104\u001a\u00020H2\b\u0010I\u001a\u0004\u0018\u00010\nH\u0010J\u0010\u0010 \u0001\u001a\u0004\u0018\u00010H*\u00030¡\u0001H\u0002J\u0017\u0010¢\u0001\u001a\u00020:*\u0002002\b\u0010;\u001a\u0004\u0018\u00010'H\u0002J\u001a\u0010£\u0001\u001a\u00060Oj\u0002`P*\u00020'2\u0007\u0010¤\u0001\u001a\u00020wH\u0002R\u0016\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00020\u00068TX\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f8F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\u00020\u00068TX\u0004¢\u0006\u0006\u001a\u0004\b\u0013\u0010\rR\u0014\u0010\u0014\u001a\u00020\u00068VX\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\rR\u0011\u0010\u0015\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0015\u0010\rR\u0011\u0010\u0016\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\rR\u0011\u0010\u0017\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0017\u0010\rR\u0015\u0010\u0018\u001a\u0006\u0012\u0002\b\u00030\u00198F¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u0014\u0010\u001c\u001a\u00020\u00068PX\u0004¢\u0006\u0006\u001a\u0004\b\u001d\u0010\rR\u0011\u0010\u001e\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u001f\u0010 R\u0010\u0010!\u001a\u0004\u0018\u00010\"X\u000e¢\u0006\u0002\n\u0000R\u0016\u0010#\u001a\u0004\u0018\u00010\n8@X\u0004¢\u0006\u0006\u001a\u0004\b$\u0010%R\u001c\u0010&\u001a\u0004\u0018\u00010'*\u0004\u0018\u00010\n8BX\u0004¢\u0006\u0006\u001a\u0004\b(\u0010)R\u0018\u0010*\u001a\u00020\u0006*\u00020+8BX\u0004¢\u0006\u0006\u001a\u0004\b*\u0010,\u0002\u0004\n\u0002\b\u0019¨\u0006¨\u0001"}, d2 = {"Lkotlinx/coroutines/JobSupport;", "Lkotlinx/coroutines/Job;", "Lkotlinx/coroutines/ChildJob;", "Lkotlinx/coroutines/ParentJob;", "Lkotlinx/coroutines/selects/SelectClause0;", "active", "", "(Z)V", "_state", "Lkotlinx/atomicfu/AtomicRef;", "", "cancelsParent", "getCancelsParent", "()Z", "children", "Lkotlin/sequences/Sequence;", "getChildren", "()Lkotlin/sequences/Sequence;", "handlesException", "getHandlesException", "isActive", "isCancelled", "isCompleted", "isCompletedExceptionally", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "getKey", "()Lkotlin/coroutines/CoroutineContext$Key;", "onCancelComplete", "getOnCancelComplete$kotlinx_coroutines_core", "onJoin", "getOnJoin", "()Lkotlinx/coroutines/selects/SelectClause0;", "parentHandle", "Lkotlinx/coroutines/ChildHandle;", "state", "getState$kotlinx_coroutines_core", "()Ljava/lang/Object;", "exceptionOrNull", "", "getExceptionOrNull", "(Ljava/lang/Object;)Ljava/lang/Throwable;", "isCancelling", "Lkotlinx/coroutines/Incomplete;", "(Lkotlinx/coroutines/Incomplete;)Z", "addLastAtomic", "expect", "list", "Lkotlinx/coroutines/NodeList;", "node", "Lkotlinx/coroutines/JobNode;", "attachChild", "child", "awaitInternal", "awaitInternal$kotlinx_coroutines_core", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "awaitSuspend", "cancel", "", "cause", "cancelImpl", "cancelMakeCompleting", "cancelParent", "childCancelled", "completeStateFinalization", "update", "mode", "", "suppressed", "continueCompleting", "Lkotlinx/coroutines/JobSupport$Finishing;", "lastChild", "Lkotlinx/coroutines/ChildHandleNode;", "proposedUpdate", "createCauseException", "createJobCancellationException", "Lkotlinx/coroutines/JobCancellationException;", "firstChild", "getCancellationException", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "getChildJobCancellationCause", "getCompletedInternal", "getCompletedInternal$kotlinx_coroutines_core", "getCompletionCause", "getCompletionExceptionOrNull", "getFinalRootCause", "exceptions", "", "getOrPromoteCancellingList", "handleJobException", "exception", "handleOnCompletionException", "handleOnCompletionException$kotlinx_coroutines_core", "initParentJobInternal", "parent", "initParentJobInternal$kotlinx_coroutines_core", "invokeOnCompletion", "Lkotlinx/coroutines/DisposableHandle;", "onCancelling", "invokeImmediately", "handler", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "Lkotlinx/coroutines/CompletionHandler;", "join", "joinInternal", "joinSuspend", "loopOnState", "", "block", "makeCancelling", "makeCompleting", "makeCompleting$kotlinx_coroutines_core", "makeCompletingOnce", "makeCompletingOnce$kotlinx_coroutines_core", "makeNode", "nameString", "", "nameString$kotlinx_coroutines_core", "notifyCancelling", "notifyHandlers", "T", "onCancellation", "onCompletionInternal", "onCompletionInternal$kotlinx_coroutines_core", "onStartInternal", "onStartInternal$kotlinx_coroutines_core", "parentCancelled", "parentJob", "promoteEmptyToNodeList", "Lkotlinx/coroutines/Empty;", "promoteSingleToNodeList", "registerSelectClause0", "R", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "Lkotlin/coroutines/Continuation;", "(Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function1;)V", "registerSelectClause1Internal", "Lkotlin/Function2;", "registerSelectClause1Internal$kotlinx_coroutines_core", "(Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "removeNode", "removeNode$kotlinx_coroutines_core", "selectAwaitCompletion", "selectAwaitCompletion$kotlinx_coroutines_core", "start", "startInternal", "stateString", "suppressExceptions", "rootCause", "toDebugString", "toString", "tryFinalizeFinishingState", "tryFinalizeSimpleState", "tryMakeCancelling", "tryMakeCompleting", "tryWaitForChild", "nextChild", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "notifyCompletion", "toCancellationException", "message", "AwaitContinuation", "ChildCompletion", "Finishing", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
@Deprecated(level = DeprecationLevel.ERROR, message = "This is internal API and may be removed in the future releases")
/* compiled from: JobSupport.kt */
public class JobSupport implements Job, ChildJob, ParentJob, SelectClause0 {
    private static final AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(JobSupport.class, Object.class, "_state");
    private volatile Object _state;
    private volatile ChildHandle parentHandle;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001b\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0014R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/JobSupport$AwaitContinuation;", "T", "Lkotlinx/coroutines/CancellableContinuationImpl;", "delegate", "Lkotlin/coroutines/Continuation;", "job", "Lkotlinx/coroutines/JobSupport;", "(Lkotlin/coroutines/Continuation;Lkotlinx/coroutines/JobSupport;)V", "getContinuationCancellationCause", "", "parent", "Lkotlinx/coroutines/Job;", "nameString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* compiled from: JobSupport.kt */
    private static final class AwaitContinuation<T> extends CancellableContinuationImpl<T> {
        private final JobSupport job;

        /* access modifiers changed from: protected */
        public String nameString() {
            return "AwaitContinuation";
        }

        public AwaitContinuation(Continuation<? super T> continuation, JobSupport jobSupport) {
            Intrinsics.checkParameterIsNotNull(continuation, "delegate");
            Intrinsics.checkParameterIsNotNull(jobSupport, "job");
            super(continuation, 1);
            this.job = jobSupport;
        }

        public Throwable getContinuationCancellationCause(Job job2) {
            Intrinsics.checkParameterIsNotNull(job2, "parent");
            Object state$kotlinx_coroutines_core = this.job.getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof Finishing) {
                Throwable th = ((Finishing) state$kotlinx_coroutines_core).rootCause;
                if (th != null) {
                    return th;
                }
            }
            if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
                return ((CompletedExceptionally) state$kotlinx_coroutines_core).cause;
            }
            return job2.getCancellationException();
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B'\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\u0002\u0010\u000bJ\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lkotlinx/coroutines/JobSupport$ChildCompletion;", "Lkotlinx/coroutines/JobNode;", "Lkotlinx/coroutines/Job;", "parent", "Lkotlinx/coroutines/JobSupport;", "state", "Lkotlinx/coroutines/JobSupport$Finishing;", "child", "Lkotlinx/coroutines/ChildHandleNode;", "proposedUpdate", "", "(Lkotlinx/coroutines/JobSupport;Lkotlinx/coroutines/JobSupport$Finishing;Lkotlinx/coroutines/ChildHandleNode;Ljava/lang/Object;)V", "invoke", "", "cause", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* compiled from: JobSupport.kt */
    private static final class ChildCompletion extends JobNode<Job> {
        private final ChildHandleNode child;
        private final JobSupport parent;
        private final Object proposedUpdate;
        private final Finishing state;

        public /* bridge */ /* synthetic */ Object invoke(Object obj) {
            invoke((Throwable) obj);
            return Unit.INSTANCE;
        }

        public ChildCompletion(JobSupport jobSupport, Finishing finishing, ChildHandleNode childHandleNode, Object obj) {
            Intrinsics.checkParameterIsNotNull(jobSupport, "parent");
            Intrinsics.checkParameterIsNotNull(finishing, OutgoingRoomKeyRequestEntityFields.STATE);
            Intrinsics.checkParameterIsNotNull(childHandleNode, "child");
            super(childHandleNode.childJob);
            this.parent = jobSupport;
            this.state = finishing;
            this.child = childHandleNode;
            this.proposedUpdate = obj;
        }

        public void invoke(Throwable th) {
            this.parent.continueCompleting(this.state, this.child, this.proposedUpdate);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ChildCompletion[");
            sb.append(this.child);
            sb.append(", ");
            sb.append(this.proposedUpdate);
            sb.append(']');
            return sb.toString();
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0003\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00060\u0001j\u0002`\u00022\u00020\u0003B\u001f\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\nJ\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\tJ\u0018\u0010\u0015\u001a\u0012\u0012\u0004\u0012\u00020\t0\u0016j\b\u0012\u0004\u0012\u00020\t`\u0017H\u0002J\u0016\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\t0\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\tJ\b\u0010\u001b\u001a\u00020\u001cH\u0016R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u0001X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\u00078VX\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\rR\u0012\u0010\u0006\u001a\u00020\u00078\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u000f\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\rR\u0014\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\b\u001a\u0004\u0018\u00010\t8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lkotlinx/coroutines/JobSupport$Finishing;", "", "Lkotlinx/coroutines/internal/SynchronizedObject;", "Lkotlinx/coroutines/Incomplete;", "list", "Lkotlinx/coroutines/NodeList;", "isCompleting", "", "rootCause", "", "(Lkotlinx/coroutines/NodeList;ZLjava/lang/Throwable;)V", "_exceptionsHolder", "isActive", "()Z", "isCancelling", "isSealed", "getList", "()Lkotlinx/coroutines/NodeList;", "addExceptionLocked", "", "exception", "allocateList", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "sealLocked", "", "proposedException", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* compiled from: JobSupport.kt */
    private static final class Finishing implements Incomplete {
        private volatile Object _exceptionsHolder;
        public volatile boolean isCompleting;
        private final NodeList list;
        public volatile Throwable rootCause;

        public NodeList getList() {
            return this.list;
        }

        public Finishing(NodeList nodeList, boolean z, Throwable th) {
            Intrinsics.checkParameterIsNotNull(nodeList, "list");
            this.list = nodeList;
            this.isCompleting = z;
            this.rootCause = th;
        }

        public final boolean isSealed() {
            return this._exceptionsHolder == JobSupportKt.SEALED;
        }

        public final boolean isCancelling() {
            return this.rootCause != null;
        }

        public boolean isActive() {
            return this.rootCause == null;
        }

        public final List<Throwable> sealLocked(Throwable th) {
            ArrayList arrayList;
            Object obj = this._exceptionsHolder;
            if (obj == null) {
                arrayList = allocateList();
            } else if (obj instanceof Throwable) {
                ArrayList allocateList = allocateList();
                allocateList.add(obj);
                arrayList = allocateList;
            } else if (!(obj instanceof ArrayList)) {
                StringBuilder sb = new StringBuilder();
                sb.append("State is ");
                sb.append(obj);
                throw new IllegalStateException(sb.toString().toString());
            } else if (obj != null) {
                arrayList = (ArrayList) obj;
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.ArrayList<kotlin.Throwable> /* = java.util.ArrayList<kotlin.Throwable> */");
            }
            Throwable th2 = this.rootCause;
            if (th2 != null) {
                arrayList.add(0, th2);
            }
            if (th != null && (!Intrinsics.areEqual((Object) th, (Object) th2))) {
                arrayList.add(th);
            }
            this._exceptionsHolder = JobSupportKt.SEALED;
            return arrayList;
        }

        public final void addExceptionLocked(Throwable th) {
            Intrinsics.checkParameterIsNotNull(th, "exception");
            Throwable th2 = this.rootCause;
            if (th2 == null) {
                this.rootCause = th;
            } else if (th != th2) {
                Object obj = this._exceptionsHolder;
                if (obj == null) {
                    this._exceptionsHolder = th;
                } else if (obj instanceof Throwable) {
                    if (th != obj) {
                        ArrayList allocateList = allocateList();
                        allocateList.add(obj);
                        allocateList.add(th);
                        this._exceptionsHolder = allocateList;
                    }
                } else if (!(obj instanceof ArrayList)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("State is ");
                    sb.append(obj);
                    throw new IllegalStateException(sb.toString().toString());
                } else if (obj != null) {
                    ((ArrayList) obj).add(th);
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.ArrayList<kotlin.Throwable> /* = java.util.ArrayList<kotlin.Throwable> */");
                }
            }
        }

        private final ArrayList<Throwable> allocateList() {
            return new ArrayList<>(4);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Finishing[cancelling=");
            sb.append(isCancelling());
            sb.append(", completing=");
            sb.append(this.isCompleting);
            sb.append(", rootCause=");
            sb.append(this.rootCause);
            sb.append(", exceptions=");
            sb.append(this._exceptionsHolder);
            sb.append(", list=");
            sb.append(getList());
            sb.append(']');
            return sb.toString();
        }
    }

    /* access modifiers changed from: protected */
    public boolean getCancelsParent() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean getHandlesException() {
        return true;
    }

    public boolean getOnCancelComplete$kotlinx_coroutines_core() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void handleJobException(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "exception");
    }

    /* access modifiers changed from: protected */
    public void onCancellation(Throwable th) {
    }

    public void onCompletionInternal$kotlinx_coroutines_core(Object obj, int i, boolean z) {
    }

    public void onStartInternal$kotlinx_coroutines_core() {
    }

    public JobSupport(boolean z) {
        this._state = z ? JobSupportKt.EMPTY_ACTIVE : JobSupportKt.EMPTY_NEW;
    }

    public <R> R fold(R r, Function2<? super R, ? super Element, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        return DefaultImpls.fold(this, r, function2);
    }

    public <E extends Element> E get(Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return DefaultImpls.get(this, key);
    }

    public CoroutineContext minusKey(Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return DefaultImpls.minusKey(this, key);
    }

    public CoroutineContext plus(CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return DefaultImpls.plus((Job) this, coroutineContext);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")
    public Job plus(Job job) {
        Intrinsics.checkParameterIsNotNull(job, "other");
        return DefaultImpls.plus((Job) this, job);
    }

    public final Key<?> getKey() {
        return Job.Key;
    }

    public final void initParentJobInternal$kotlinx_coroutines_core(Job job) {
        if (!(this.parentHandle == null)) {
            throw new IllegalStateException("Check failed.".toString());
        } else if (job == null) {
            this.parentHandle = NonDisposableHandle.INSTANCE;
        } else {
            job.start();
            ChildHandle attachChild = job.attachChild(this);
            this.parentHandle = attachChild;
            if (isCompleted()) {
                attachChild.dispose();
                this.parentHandle = NonDisposableHandle.INSTANCE;
            }
        }
    }

    private final Void loopOnState(Function1<Object, Unit> function1) {
        while (true) {
            function1.invoke(getState$kotlinx_coroutines_core());
        }
    }

    public boolean isActive() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        return (state$kotlinx_coroutines_core instanceof Incomplete) && ((Incomplete) state$kotlinx_coroutines_core).isActive();
    }

    public final boolean isCompleted() {
        return !(getState$kotlinx_coroutines_core() instanceof Incomplete);
    }

    public final boolean isCancelled() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        return (state$kotlinx_coroutines_core instanceof CompletedExceptionally) || ((state$kotlinx_coroutines_core instanceof Finishing) && ((Finishing) state$kotlinx_coroutines_core).isCancelling());
    }

    private final boolean tryFinalizeFinishingState(Finishing finishing, Object obj, int i) {
        Throwable finalRootCause;
        boolean z = false;
        if (!(getState$kotlinx_coroutines_core() == finishing)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        } else if (!(!finishing.isSealed())) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        } else if (finishing.isCompleting) {
            Throwable th = null;
            CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(obj instanceof CompletedExceptionally) ? null : obj);
            if (completedExceptionally != null) {
                th = completedExceptionally.cause;
            }
            synchronized (finishing) {
                List sealLocked = finishing.sealLocked(th);
                finalRootCause = getFinalRootCause(finishing, sealLocked);
                if (finalRootCause != null && (suppressExceptions(finalRootCause, sealLocked) || finalRootCause != finishing.rootCause)) {
                    z = true;
                }
            }
            if (!(finalRootCause == null || finalRootCause == th)) {
                obj = new CompletedExceptionally(finalRootCause);
            }
            if (finalRootCause != null && !cancelParent(finalRootCause)) {
                handleJobException(finalRootCause);
            }
            if (_state$FU.compareAndSet(this, finishing, JobSupportKt.boxIncomplete(obj))) {
                completeStateFinalization(finishing, obj, i, z);
                return true;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected state: ");
            sb.append(this._state);
            sb.append(", expected: ");
            sb.append(finishing);
            sb.append(", update: ");
            sb.append(obj);
            throw new IllegalArgumentException(sb.toString().toString());
        } else {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
    }

    private final Throwable getFinalRootCause(Finishing finishing, List<? extends Throwable> list) {
        Object obj;
        if (!list.isEmpty()) {
            Iterator it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    obj = null;
                    break;
                }
                obj = it.next();
                if (!(((Throwable) obj) instanceof CancellationException)) {
                    break;
                }
            }
            Throwable th = (Throwable) obj;
            if (th == null) {
                th = (Throwable) list.get(0);
            }
            return th;
        } else if (finishing.isCancelling()) {
            return createJobCancellationException();
        } else {
            return null;
        }
    }

    private final boolean suppressExceptions(Throwable th, List<? extends Throwable> list) {
        boolean z = false;
        if (list.size() <= 1) {
            return false;
        }
        Set identitySet = ConcurrentKt.identitySet(list.size());
        for (Throwable unwrap : list) {
            Throwable unwrap2 = StackTraceRecoveryKt.unwrap(unwrap);
            if (unwrap2 != th && !(unwrap2 instanceof CancellationException) && identitySet.add(unwrap2)) {
                ExceptionsKt.addSuppressed(th, unwrap2);
                z = true;
            }
        }
        return z;
    }

    private final boolean tryFinalizeSimpleState(Incomplete incomplete, Object obj, int i) {
        String str = "Check failed.";
        if (!((incomplete instanceof Empty) || (incomplete instanceof JobNode))) {
            throw new IllegalStateException(str.toString());
        } else if (!(!(obj instanceof CompletedExceptionally))) {
            throw new IllegalStateException(str.toString());
        } else if (!_state$FU.compareAndSet(this, incomplete, JobSupportKt.boxIncomplete(obj))) {
            return false;
        } else {
            completeStateFinalization(incomplete, obj, i, false);
            return true;
        }
    }

    private final void completeStateFinalization(Incomplete incomplete, Object obj, int i, boolean z) {
        ChildHandle childHandle = this.parentHandle;
        if (childHandle != null) {
            childHandle.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
        Throwable th = null;
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(obj instanceof CompletedExceptionally) ? null : obj);
        if (completedExceptionally != null) {
            th = completedExceptionally.cause;
        }
        if (!isCancelling(incomplete)) {
            onCancellation(th);
        }
        if (incomplete instanceof JobNode) {
            try {
                ((JobNode) incomplete).invoke(th);
            } catch (Throwable th2) {
                StringBuilder sb = new StringBuilder();
                sb.append("Exception in completion handler ");
                sb.append(incomplete);
                sb.append(" for ");
                sb.append(this);
                handleOnCompletionException$kotlinx_coroutines_core(new CompletionHandlerException(sb.toString(), th2));
            }
        } else {
            NodeList list = incomplete.getList();
            if (list != null) {
                notifyCompletion(list, th);
            }
        }
        onCompletionInternal$kotlinx_coroutines_core(obj, i, z);
    }

    private final void notifyCancelling(NodeList nodeList, Throwable th) {
        onCancellation(th);
        Throwable th2 = null;
        Object next = nodeList.getNext();
        if (next != null) {
            for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) next; !Intrinsics.areEqual((Object) lockFreeLinkedListNode, (Object) nodeList); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
                if (lockFreeLinkedListNode instanceof JobCancellingNode) {
                    JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                    try {
                        jobNode.invoke(th);
                    } catch (Throwable th3) {
                        if (th2 != null) {
                            ExceptionsKt.addSuppressed(th2, th3);
                            if (th2 != null) {
                            }
                        }
                        JobSupport jobSupport = this;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Exception in completion handler ");
                        sb.append(jobNode);
                        sb.append(" for ");
                        sb.append(jobSupport);
                        Throwable completionHandlerException = new CompletionHandlerException(sb.toString(), th3);
                        Unit unit = Unit.INSTANCE;
                        th2 = completionHandlerException;
                    }
                }
            }
            if (th2 != null) {
                handleOnCompletionException$kotlinx_coroutines_core(th2);
            }
            cancelParent(th);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
    }

    private final <T extends JobNode<?>> void notifyHandlers(NodeList nodeList, Throwable th) {
        Throwable th2 = null;
        Object next = nodeList.getNext();
        if (next != null) {
            for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) next; !Intrinsics.areEqual((Object) lockFreeLinkedListNode, (Object) nodeList); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
                Intrinsics.reifiedOperationMarker(3, "T");
                if (lockFreeLinkedListNode instanceof LockFreeLinkedListNode) {
                    JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                    try {
                        jobNode.invoke(th);
                    } catch (Throwable th3) {
                        if (th2 != null) {
                            ExceptionsKt.addSuppressed(th2, th3);
                            if (th2 != null) {
                            }
                        }
                        JobSupport jobSupport = this;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Exception in completion handler ");
                        sb.append(jobNode);
                        sb.append(" for ");
                        sb.append(jobSupport);
                        Throwable completionHandlerException = new CompletionHandlerException(sb.toString(), th3);
                        Unit unit = Unit.INSTANCE;
                        th2 = completionHandlerException;
                    }
                }
            }
            if (th2 != null) {
                handleOnCompletionException$kotlinx_coroutines_core(th2);
                return;
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
    }

    private final int startInternal(Object obj) {
        if (obj instanceof Empty) {
            if (((Empty) obj).isActive()) {
                return 0;
            }
            if (!_state$FU.compareAndSet(this, obj, JobSupportKt.EMPTY_ACTIVE)) {
                return -1;
            }
            onStartInternal$kotlinx_coroutines_core();
            return 1;
        } else if (!(obj instanceof InactiveNodeList)) {
            return 0;
        } else {
            if (!_state$FU.compareAndSet(this, obj, ((InactiveNodeList) obj).getList())) {
                return -1;
            }
            onStartInternal$kotlinx_coroutines_core();
            return 1;
        }
    }

    public final CancellationException getCancellationException() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        String str = "Job is still new or active: ";
        if (state$kotlinx_coroutines_core instanceof Finishing) {
            Throwable th = ((Finishing) state$kotlinx_coroutines_core).rootCause;
            if (th != null) {
                CancellationException cancellationException = toCancellationException(th, "Job is cancelling");
                if (cancellationException != null) {
                    return cancellationException;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(this);
            throw new IllegalStateException(sb.toString().toString());
        } else if (state$kotlinx_coroutines_core instanceof Incomplete) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(this);
            throw new IllegalStateException(sb2.toString().toString());
        } else if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            return toCancellationException(((CompletedExceptionally) state$kotlinx_coroutines_core).cause, "Job was cancelled");
        } else {
            return new JobCancellationException("Job has completed normally", null, this);
        }
    }

    private final CancellationException toCancellationException(Throwable th, String str) {
        CancellationException cancellationException = (CancellationException) (!(th instanceof CancellationException) ? null : th);
        return cancellationException != null ? cancellationException : new JobCancellationException(str, th, this);
    }

    public final DisposableHandle invokeOnCompletion(Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        return invokeOnCompletion(false, true, function1);
    }

    public final DisposableHandle invokeOnCompletion(boolean z, boolean z2, Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        Throwable th = null;
        JobNode jobNode = null;
        while (true) {
            Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof Empty) {
                Empty empty = (Empty) state$kotlinx_coroutines_core;
                if (empty.isActive()) {
                    if (jobNode == null) {
                        jobNode = makeNode(function1, z);
                    }
                    if (_state$FU.compareAndSet(this, state$kotlinx_coroutines_core, jobNode)) {
                        return jobNode;
                    }
                } else {
                    promoteEmptyToNodeList(empty);
                }
            } else if (state$kotlinx_coroutines_core instanceof Incomplete) {
                NodeList list = ((Incomplete) state$kotlinx_coroutines_core).getList();
                if (list != null) {
                    Throwable th2 = null;
                    DisposableHandle disposableHandle = NonDisposableHandle.INSTANCE;
                    if (z && (state$kotlinx_coroutines_core instanceof Finishing)) {
                        synchronized (state$kotlinx_coroutines_core) {
                            th2 = ((Finishing) state$kotlinx_coroutines_core).rootCause;
                            if (th2 == null || ((function1 instanceof ChildHandleNode) && !((Finishing) state$kotlinx_coroutines_core).isCompleting)) {
                                if (jobNode == null) {
                                    jobNode = makeNode(function1, z);
                                }
                                if (addLastAtomic(state$kotlinx_coroutines_core, list, jobNode)) {
                                    if (th2 == null) {
                                        DisposableHandle disposableHandle2 = jobNode;
                                        return disposableHandle2;
                                    }
                                    disposableHandle = jobNode;
                                }
                            }
                            Unit unit = Unit.INSTANCE;
                        }
                    }
                    if (th2 != null) {
                        if (z2) {
                            function1.invoke(th2);
                        }
                        return disposableHandle;
                    }
                    if (jobNode == null) {
                        jobNode = makeNode(function1, z);
                    }
                    if (addLastAtomic(state$kotlinx_coroutines_core, list, jobNode)) {
                        return jobNode;
                    }
                } else if (state$kotlinx_coroutines_core != null) {
                    promoteSingleToNodeList((JobNode) state$kotlinx_coroutines_core);
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.JobNode<*>");
                }
            } else {
                if (z2) {
                    if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
                        state$kotlinx_coroutines_core = null;
                    }
                    CompletedExceptionally completedExceptionally = (CompletedExceptionally) state$kotlinx_coroutines_core;
                    if (completedExceptionally != null) {
                        th = completedExceptionally.cause;
                    }
                    function1.invoke(th);
                }
                return NonDisposableHandle.INSTANCE;
            }
        }
    }

    private final JobNode<?> makeNode(Function1<? super Throwable, Unit> function1, boolean z) {
        String str = "Failed requirement.";
        boolean z2 = true;
        JobCancellingNode jobCancellingNode = null;
        if (z) {
            if (function1 instanceof JobCancellingNode) {
                jobCancellingNode = function1;
            }
            JobCancellingNode jobCancellingNode2 = jobCancellingNode;
            if (jobCancellingNode2 != null) {
                if (jobCancellingNode2.job != this) {
                    z2 = false;
                }
                if (!z2) {
                    throw new IllegalArgumentException(str.toString());
                } else if (jobCancellingNode2 != null) {
                    return jobCancellingNode2;
                }
            }
            return new InvokeOnCancelling<>(this, function1);
        }
        if (function1 instanceof JobNode) {
            jobCancellingNode = function1;
        }
        JobNode<?> jobNode = jobCancellingNode;
        if (jobNode != null) {
            if (jobNode.job != this || (jobNode instanceof JobCancellingNode)) {
                z2 = false;
            }
            if (!z2) {
                throw new IllegalArgumentException(str.toString());
            } else if (jobNode != null) {
                return jobNode;
            }
        }
        return new InvokeOnCompletion<>(this, function1);
    }

    private final void promoteEmptyToNodeList(Empty empty) {
        NodeList nodeList = new NodeList();
        _state$FU.compareAndSet(this, empty, empty.isActive() ? nodeList : new InactiveNodeList(nodeList));
    }

    private final void promoteSingleToNodeList(JobNode<?> jobNode) {
        jobNode.addOneIfEmpty(new NodeList());
        _state$FU.compareAndSet(this, jobNode, jobNode.getNextNode());
    }

    public final Object join(Continuation<? super Unit> continuation) {
        if (joinInternal()) {
            return joinSuspend(continuation);
        }
        YieldKt.checkCompletion(continuation.getContext());
        return Unit.INSTANCE;
    }

    public final SelectClause0 getOnJoin() {
        return this;
    }

    public void cancel() {
        cancel(null);
    }

    public boolean cancel(Throwable th) {
        return cancelImpl(th) && getHandlesException();
    }

    public final void parentCancelled(ParentJob parentJob) {
        Intrinsics.checkParameterIsNotNull(parentJob, "parentJob");
        cancelImpl(parentJob);
    }

    public boolean childCancelled(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "cause");
        return cancelImpl(th) && getHandlesException();
    }

    private final boolean cancelImpl(Object obj) {
        if (!getOnCancelComplete$kotlinx_coroutines_core() || !cancelMakeCompleting(obj)) {
            return makeCancelling(obj);
        }
        return true;
    }

    private final JobCancellationException createJobCancellationException() {
        return new JobCancellationException("Job was cancelled", null, this);
    }

    public Throwable getChildJobCancellationCause() {
        Throwable th;
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (state$kotlinx_coroutines_core instanceof Finishing) {
            th = ((Finishing) state$kotlinx_coroutines_core).rootCause;
        } else if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
            th = state$kotlinx_coroutines_core instanceof CompletedExceptionally ? ((CompletedExceptionally) state$kotlinx_coroutines_core).cause : null;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot be cancelling child in this state: ");
            sb.append(state$kotlinx_coroutines_core);
            throw new IllegalStateException(sb.toString().toString());
        }
        if (th != null && (!getHandlesException() || (th instanceof CancellationException))) {
            return th;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Parent job is ");
        sb2.append(stateString(state$kotlinx_coroutines_core));
        return new JobCancellationException(sb2.toString(), th, this);
    }

    private final Throwable createCauseException(Object obj) {
        if (obj != null ? obj instanceof Throwable : true) {
            if (obj == null) {
                obj = createJobCancellationException();
            }
            return (Throwable) obj;
        } else if (obj != null) {
            return ((ParentJob) obj).getChildJobCancellationCause();
        } else {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.ParentJob");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x003e, code lost:
        if (r8 == null) goto L_0x0049;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0040, code lost:
        notifyCancelling(((kotlinx.coroutines.JobSupport.Finishing) r2).getList(), r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0049, code lost:
        return true;
     */
    private final boolean makeCancelling(Object obj) {
        Throwable th = null;
        while (true) {
            Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof Finishing) {
                synchronized (state$kotlinx_coroutines_core) {
                    if (((Finishing) state$kotlinx_coroutines_core).isSealed()) {
                        return false;
                    }
                    boolean isCancelling = ((Finishing) state$kotlinx_coroutines_core).isCancelling();
                    if (obj != null || !isCancelling) {
                        if (th == null) {
                            th = createCauseException(obj);
                        }
                        ((Finishing) state$kotlinx_coroutines_core).addExceptionLocked(th);
                    }
                    Throwable th2 = ((Finishing) state$kotlinx_coroutines_core).rootCause;
                    if (!(!isCancelling)) {
                        th2 = null;
                    }
                }
            } else if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                return false;
            } else {
                if (th == null) {
                    th = createCauseException(obj);
                }
                Incomplete incomplete = (Incomplete) state$kotlinx_coroutines_core;
                if (!incomplete.isActive()) {
                    int tryMakeCompleting = tryMakeCompleting(state$kotlinx_coroutines_core, new CompletedExceptionally(th), 0);
                    if (tryMakeCompleting == 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Cannot happen in ");
                        sb.append(state$kotlinx_coroutines_core);
                        throw new IllegalStateException(sb.toString().toString());
                    } else if (tryMakeCompleting == 1 || tryMakeCompleting == 2) {
                        return true;
                    } else {
                        if (tryMakeCompleting != 3) {
                            throw new IllegalStateException("unexpected result".toString());
                        }
                    }
                } else if (tryMakeCancelling(incomplete, th)) {
                    return true;
                }
            }
        }
        return true;
    }

    private final NodeList getOrPromoteCancellingList(Incomplete incomplete) {
        NodeList list = incomplete.getList();
        if (list != null) {
            return list;
        }
        if (incomplete instanceof Empty) {
            return new NodeList();
        }
        if (incomplete instanceof JobNode) {
            promoteSingleToNodeList((JobNode) incomplete);
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("State should have list: ");
        sb.append(incomplete);
        throw new IllegalStateException(sb.toString().toString());
    }

    private final boolean tryMakeCancelling(Incomplete incomplete, Throwable th) {
        String str = "Check failed.";
        if (!(!(incomplete instanceof Finishing))) {
            throw new IllegalStateException(str.toString());
        } else if (incomplete.isActive()) {
            NodeList orPromoteCancellingList = getOrPromoteCancellingList(incomplete);
            if (orPromoteCancellingList == null) {
                return false;
            }
            if (!_state$FU.compareAndSet(this, incomplete, new Finishing(orPromoteCancellingList, false, th))) {
                return false;
            }
            notifyCancelling(orPromoteCancellingList, th);
            return true;
        } else {
            throw new IllegalStateException(str.toString());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:54:0x007a, code lost:
        if (r1 == null) goto L_0x007f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x007c, code lost:
        notifyCancelling(r4, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x007f, code lost:
        r9 = firstChild(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x0083, code lost:
        if (r9 == null) goto L_0x008d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0089, code lost:
        if (tryWaitForChild(r5, r9, r10) == false) goto L_0x008d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x008c, code lost:
        return 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0091, code lost:
        if (tryFinalizeFinishingState(r5, r10, r11) == false) goto L_0x0094;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0093, code lost:
        return 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0094, code lost:
        return 3;
     */
    private final int tryMakeCompleting(Object obj, Object obj2, int i) {
        if (!(obj instanceof Incomplete)) {
            return 0;
        }
        if (((obj instanceof Empty) || (obj instanceof JobNode)) && !(obj instanceof ChildHandleNode) && !(obj2 instanceof CompletedExceptionally)) {
            return !tryFinalizeSimpleState((Incomplete) obj, obj2, i) ? 3 : 1;
        }
        Incomplete incomplete = (Incomplete) obj;
        NodeList orPromoteCancellingList = getOrPromoteCancellingList(incomplete);
        if (orPromoteCancellingList == null) {
            return 3;
        }
        Finishing finishing = (Finishing) (!(obj instanceof Finishing) ? null : obj);
        if (finishing == null) {
            finishing = new Finishing(orPromoteCancellingList, false, null);
        }
        Throwable th = null;
        synchronized (finishing) {
            if (finishing.isCompleting) {
                return 0;
            }
            finishing.isCompleting = true;
            if (finishing != obj && !_state$FU.compareAndSet(this, obj, finishing)) {
                return 3;
            }
            if (!finishing.isSealed()) {
                boolean isCancelling = finishing.isCancelling();
                CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(obj2 instanceof CompletedExceptionally) ? null : obj2);
                if (completedExceptionally != null) {
                    finishing.addExceptionLocked(completedExceptionally.cause);
                }
                Throwable th2 = finishing.rootCause;
                if (!(!isCancelling)) {
                    th2 = null;
                }
                Unit unit = Unit.INSTANCE;
            } else {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
        }
    }

    private final Throwable getExceptionOrNull(Object obj) {
        if (!(obj instanceof CompletedExceptionally)) {
            obj = null;
        }
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) obj;
        if (completedExceptionally != null) {
            return completedExceptionally.cause;
        }
        return null;
    }

    private final ChildHandleNode firstChild(Incomplete incomplete) {
        ChildHandleNode childHandleNode = (ChildHandleNode) (!(incomplete instanceof ChildHandleNode) ? null : incomplete);
        if (childHandleNode != null) {
            return childHandleNode;
        }
        NodeList list = incomplete.getList();
        if (list != null) {
            return nextChild(list);
        }
        return null;
    }

    private final boolean tryWaitForChild(Finishing finishing, ChildHandleNode childHandleNode, Object obj) {
        while (DefaultImpls.invokeOnCompletion$default(childHandleNode.childJob, false, false, new ChildCompletion(this, finishing, childHandleNode, obj), 1, null) == NonDisposableHandle.INSTANCE) {
            childHandleNode = nextChild(childHandleNode);
            if (childHandleNode == null) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public final void continueCompleting(Finishing finishing, ChildHandleNode childHandleNode, Object obj) {
        if (getState$kotlinx_coroutines_core() == finishing) {
            ChildHandleNode nextChild = nextChild(childHandleNode);
            if ((nextChild == null || !tryWaitForChild(finishing, nextChild, obj)) && tryFinalizeFinishingState(finishing, obj, 0)) {
            }
            return;
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    private final ChildHandleNode nextChild(LockFreeLinkedListNode lockFreeLinkedListNode) {
        while (lockFreeLinkedListNode.isRemoved()) {
            lockFreeLinkedListNode = lockFreeLinkedListNode.getPrevNode();
        }
        while (true) {
            lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode();
            if (!lockFreeLinkedListNode.isRemoved()) {
                if (lockFreeLinkedListNode instanceof ChildHandleNode) {
                    return (ChildHandleNode) lockFreeLinkedListNode;
                }
                if (lockFreeLinkedListNode instanceof NodeList) {
                    return null;
                }
            }
        }
    }

    public final Sequence<Job> getChildren() {
        return SequencesKt.sequence(new JobSupport$children$1(this, null));
    }

    public final ChildHandle attachChild(ChildJob childJob) {
        Intrinsics.checkParameterIsNotNull(childJob, "child");
        DisposableHandle invokeOnCompletion$default = DefaultImpls.invokeOnCompletion$default(this, true, false, new ChildHandleNode(this, childJob), 2, null);
        if (invokeOnCompletion$default != null) {
            return (ChildHandle) invokeOnCompletion$default;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.ChildHandle");
    }

    public void handleOnCompletionException$kotlinx_coroutines_core(Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "exception");
        throw th;
    }

    private final boolean cancelParent(Throwable th) {
        boolean z = true;
        if (th instanceof CancellationException) {
            return true;
        }
        if (!getCancelsParent()) {
            return false;
        }
        ChildHandle childHandle = this.parentHandle;
        if (childHandle == null || !childHandle.childCancelled(th)) {
            z = false;
        }
        return z;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(toDebugString());
        sb.append('@');
        sb.append(DebugKt.getHexAddress(this));
        return sb.toString();
    }

    public final String toDebugString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nameString$kotlinx_coroutines_core());
        sb.append('{');
        sb.append(stateString(getState$kotlinx_coroutines_core()));
        sb.append('}');
        return sb.toString();
    }

    public String nameString$kotlinx_coroutines_core() {
        return DebugKt.getClassSimpleName(this);
    }

    private final String stateString(Object obj) {
        String str = "Active";
        if (obj instanceof Finishing) {
            Finishing finishing = (Finishing) obj;
            if (finishing.isCancelling()) {
                return "Cancelling";
            }
            if (finishing.isCompleting) {
                return "Completing";
            }
            return str;
        } else if (!(obj instanceof Incomplete)) {
            return obj instanceof CompletedExceptionally ? "Cancelled" : "Completed";
        } else {
            if (((Incomplete) obj).isActive()) {
                return str;
            }
            return "New";
        }
    }

    private final boolean isCancelling(Incomplete incomplete) {
        return (incomplete instanceof Finishing) && ((Finishing) incomplete).isCancelling();
    }

    public final boolean isCompletedExceptionally() {
        return getState$kotlinx_coroutines_core() instanceof CompletedExceptionally;
    }

    public final Throwable getCompletionExceptionOrNull() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
            return getExceptionOrNull(state$kotlinx_coroutines_core);
        }
        throw new IllegalStateException("This job has not completed yet".toString());
    }

    public final Object getCompletedInternal$kotlinx_coroutines_core() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (!(!(state$kotlinx_coroutines_core instanceof Incomplete))) {
            throw new IllegalStateException("This job has not completed yet".toString());
        } else if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
            return JobSupportKt.unboxState(state$kotlinx_coroutines_core);
        } else {
            throw ((CompletedExceptionally) state$kotlinx_coroutines_core).cause;
        }
    }

    public final Object awaitInternal$kotlinx_coroutines_core(Continuation<Object> continuation) {
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
                    return JobSupportKt.unboxState(state$kotlinx_coroutines_core);
                }
                Throwable th = ((CompletedExceptionally) state$kotlinx_coroutines_core).cause;
                if (!StackTraceRecoveryKt.recoveryDisabled(th)) {
                    InlineMarker.mark(0);
                    if (!(continuation instanceof CoroutineStackFrame)) {
                        throw th;
                    }
                    throw StackTraceRecoveryKt.recoverFromStackFrame(th, (CoroutineStackFrame) continuation);
                }
                throw th;
            }
        } while (startInternal(state$kotlinx_coroutines_core) < 0);
        return awaitSuspend(continuation);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ Object awaitSuspend(Continuation<Object> continuation) {
        AwaitContinuation awaitContinuation = new AwaitContinuation(IntrinsicsKt.intercepted(continuation), this);
        CancellableContinuationKt.disposeOnCancellation(awaitContinuation, invokeOnCompletion(new ResumeAwaitOnCompletion(this, awaitContinuation)));
        Object result = awaitContinuation.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    public final <T, R> void selectAwaitCompletion$kotlinx_coroutines_core(SelectInstance<? super R> selectInstance, Function2<? super T, ? super Continuation<? super R>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            selectInstance.resumeSelectCancellableWithException(((CompletedExceptionally) state$kotlinx_coroutines_core).cause);
        } else {
            CancellableKt.startCoroutineCancellable(function2, JobSupportKt.unboxState(state$kotlinx_coroutines_core), selectInstance.getCompletion());
        }
    }

    public final Object getState$kotlinx_coroutines_core() {
        while (true) {
            Object obj = this._state;
            if (!(obj instanceof OpDescriptor)) {
                return obj;
            }
            ((OpDescriptor) obj).perform(this);
        }
    }

    private final void notifyCompletion(NodeList nodeList, Throwable th) {
        Throwable th2 = null;
        Object next = nodeList.getNext();
        if (next != null) {
            for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) next; !Intrinsics.areEqual((Object) lockFreeLinkedListNode, (Object) nodeList); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
                if (lockFreeLinkedListNode instanceof JobNode) {
                    JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                    try {
                        jobNode.invoke(th);
                    } catch (Throwable th3) {
                        if (th2 != null) {
                            ExceptionsKt.addSuppressed(th2, th3);
                            if (th2 != null) {
                            }
                        }
                        JobSupport jobSupport = this;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Exception in completion handler ");
                        sb.append(jobNode);
                        sb.append(" for ");
                        sb.append(jobSupport);
                        Throwable completionHandlerException = new CompletionHandlerException(sb.toString(), th3);
                        Unit unit = Unit.INSTANCE;
                        th2 = completionHandlerException;
                    }
                }
            }
            if (th2 != null) {
                handleOnCompletionException$kotlinx_coroutines_core(th2);
                return;
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
    }

    public final boolean start() {
        int startInternal;
        do {
            startInternal = startInternal(getState$kotlinx_coroutines_core());
            if (startInternal == 0) {
                return false;
            }
        } while (startInternal != 1);
        return true;
    }

    /* access modifiers changed from: protected */
    public final Throwable getCompletionCause() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        String str = "Job is still new or active: ";
        if (state$kotlinx_coroutines_core instanceof Finishing) {
            Throwable th = ((Finishing) state$kotlinx_coroutines_core).rootCause;
            if (th != null) {
                return th;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(this);
            throw new IllegalStateException(sb.toString().toString());
        } else if (state$kotlinx_coroutines_core instanceof Incomplete) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(this);
            throw new IllegalStateException(sb2.toString().toString());
        } else if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            return ((CompletedExceptionally) state$kotlinx_coroutines_core).cause;
        } else {
            return null;
        }
    }

    private final boolean addLastAtomic(Object obj, NodeList nodeList, JobNode<?> jobNode) {
        int tryCondAddNext;
        LockFreeLinkedListNode lockFreeLinkedListNode = jobNode;
        CondAddOp jobSupport$addLastAtomic$$inlined$addLastIf$1 = new JobSupport$addLastAtomic$$inlined$addLastIf$1(lockFreeLinkedListNode, lockFreeLinkedListNode, this, obj);
        do {
            Object prev = nodeList.getPrev();
            if (prev != null) {
                tryCondAddNext = ((LockFreeLinkedListNode) prev).tryCondAddNext(lockFreeLinkedListNode, nodeList, jobSupport$addLastAtomic$$inlined$addLastIf$1);
                if (tryCondAddNext == 1) {
                    return true;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        } while (tryCondAddNext != 2);
        return false;
    }

    private final boolean joinInternal() {
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                return false;
            }
        } while (startInternal(state$kotlinx_coroutines_core) < 0);
        return true;
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ Object joinSuspend(Continuation<? super Unit> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 1);
        CancellableContinuation cancellableContinuation = cancellableContinuationImpl;
        CancellableContinuationKt.disposeOnCancellation(cancellableContinuation, invokeOnCompletion(new ResumeOnCompletion(this, cancellableContinuation)));
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    public final <R> void registerSelectClause0(SelectInstance<? super R> selectInstance, Function1<? super Continuation<? super R>, ? extends Object> function1) {
        Object state$kotlinx_coroutines_core;
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!selectInstance.isSelected()) {
                if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                    if (selectInstance.trySelect(null)) {
                        YieldKt.checkCompletion(selectInstance.getCompletion().getContext());
                        UndispatchedKt.startCoroutineUnintercepted(function1, selectInstance.getCompletion());
                    }
                    return;
                }
            } else {
                return;
            }
        } while (startInternal(state$kotlinx_coroutines_core) != 0);
        selectInstance.disposeOnSelect(invokeOnCompletion(new SelectJoinOnCompletion(this, selectInstance, function1)));
    }

    public final void removeNode$kotlinx_coroutines_core(JobNode<?> jobNode) {
        Object state$kotlinx_coroutines_core;
        Intrinsics.checkParameterIsNotNull(jobNode, "node");
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof JobNode)) {
                if ((state$kotlinx_coroutines_core instanceof Incomplete) && ((Incomplete) state$kotlinx_coroutines_core).getList() != null) {
                    jobNode.remove();
                }
                return;
            } else if (state$kotlinx_coroutines_core != jobNode) {
                return;
            }
        } while (!_state$FU.compareAndSet(this, state$kotlinx_coroutines_core, JobSupportKt.EMPTY_ACTIVE));
    }

    private final boolean cancelMakeCompleting(Object obj) {
        int tryMakeCompleting;
        do {
            Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if ((state$kotlinx_coroutines_core instanceof Incomplete) && (!(state$kotlinx_coroutines_core instanceof Finishing) || !((Finishing) state$kotlinx_coroutines_core).isCompleting)) {
                tryMakeCompleting = tryMakeCompleting(state$kotlinx_coroutines_core, new CompletedExceptionally(createCauseException(obj)), 0);
                if (tryMakeCompleting != 0) {
                    if (tryMakeCompleting == 1 || tryMakeCompleting == 2) {
                        return true;
                    }
                }
            }
            return false;
        } while (tryMakeCompleting == 3);
        throw new IllegalStateException("unexpected result".toString());
    }

    public final boolean makeCompleting$kotlinx_coroutines_core(Object obj) {
        int tryMakeCompleting;
        do {
            boolean z = false;
            tryMakeCompleting = tryMakeCompleting(getState$kotlinx_coroutines_core(), obj, 0);
            if (tryMakeCompleting != 0) {
                z = true;
                if (!(tryMakeCompleting == 1 || tryMakeCompleting == 2)) {
                }
            }
            return z;
        } while (tryMakeCompleting == 3);
        throw new IllegalStateException("unexpected result".toString());
    }

    public final boolean makeCompletingOnce$kotlinx_coroutines_core(Object obj, int i) {
        int tryMakeCompleting;
        do {
            tryMakeCompleting = tryMakeCompleting(getState$kotlinx_coroutines_core(), obj, i);
            if (tryMakeCompleting == 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("Job ");
                sb.append(this);
                sb.append(" is already complete or completing, ");
                sb.append("but is being completed with ");
                sb.append(obj);
                throw new IllegalStateException(sb.toString(), getExceptionOrNull(obj));
            } else if (tryMakeCompleting == 1) {
                return true;
            } else {
                if (tryMakeCompleting == 2) {
                    return false;
                }
            }
        } while (tryMakeCompleting == 3);
        throw new IllegalStateException("unexpected result".toString());
    }

    public final <T, R> void registerSelectClause1Internal$kotlinx_coroutines_core(SelectInstance<? super R> selectInstance, Function2<? super T, ? super Continuation<? super R>, ? extends Object> function2) {
        Object state$kotlinx_coroutines_core;
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!selectInstance.isSelected()) {
                if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                    if (selectInstance.trySelect(null)) {
                        if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
                            selectInstance.resumeSelectCancellableWithException(((CompletedExceptionally) state$kotlinx_coroutines_core).cause);
                        } else {
                            UndispatchedKt.startCoroutineUnintercepted(function2, JobSupportKt.unboxState(state$kotlinx_coroutines_core), selectInstance.getCompletion());
                        }
                    }
                    return;
                }
            } else {
                return;
            }
        } while (startInternal(state$kotlinx_coroutines_core) != 0);
        selectInstance.disposeOnSelect(invokeOnCompletion(new SelectAwaitOnCompletion(this, selectInstance, function2)));
    }
}
