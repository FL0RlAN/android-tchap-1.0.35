package kotlinx.coroutines.channels;

import im.vector.activity.VectorUniversalLinkActivity;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.Result.Companion;
import kotlin.ResultKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.ContinuationKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CancellableContinuationKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.internal.LockFreeLinkedListHead;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.LockFreeLinkedListNode.AddLastDesc;
import kotlinx.coroutines.internal.LockFreeLinkedListNode.CondAddOp;
import kotlinx.coroutines.internal.LockFreeLinkedListNode.RemoveFirstDesc;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.intrinsics.UndispatchedKt;
import kotlinx.coroutines.selects.SelectClause2;
import kotlinx.coroutines.selects.SelectInstance;
import kotlinx.coroutines.selects.SelectKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000¤\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\b \u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0006VWXYZ[B\u0005¢\u0006\u0002\u0010\u0003J\u0012\u0010!\u001a\u00020\u000f2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\u0010\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020'H\u0004J\b\u0010(\u001a\u00020)H\u0002J!\u0010*\u001a\u000e\u0012\u0002\b\u00030+j\u0006\u0012\u0002\b\u0003`,2\u0006\u0010-\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010.J!\u0010/\u001a\u000e\u0012\u0002\b\u00030+j\u0006\u0012\u0002\b\u0003`,2\u0006\u0010-\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010.J\u001b\u00100\u001a\b\u0012\u0004\u0012\u00028\u0000012\u0006\u0010-\u001a\u00028\u0000H\u0004¢\u0006\u0002\u00102J\u0012\u00103\u001a\u0004\u0018\u00010\u00162\u0006\u00104\u001a\u000205H\u0002J\u0014\u00106\u001a\u00020%2\n\u00107\u001a\u0006\u0012\u0002\b\u00030\tH\u0002J\"\u00108\u001a\u00020%2\u0018\u00109\u001a\u0014\u0012\u0006\u0012\u0004\u0018\u00010#\u0012\u0004\u0012\u00020%0:j\u0002`;H\u0016J\u0012\u0010<\u001a\u00020%2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0002J\u0013\u0010=\u001a\u00020\u000f2\u0006\u0010-\u001a\u00028\u0000¢\u0006\u0002\u0010>J\u0015\u0010?\u001a\u00020\u00162\u0006\u0010-\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010@J!\u0010A\u001a\u00020\u00162\u0006\u0010-\u001a\u00028\u00002\n\u0010B\u001a\u0006\u0012\u0002\b\u00030CH\u0014¢\u0006\u0002\u0010DJ\u0010\u0010E\u001a\u00020%2\u0006\u00107\u001a\u00020'H\u0014JV\u0010F\u001a\u00020%\"\u0004\b\u0001\u0010G2\f\u0010B\u001a\b\u0012\u0004\u0012\u0002HG0C2\u0006\u0010-\u001a\u00028\u00002(\u0010H\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\n\u0012\b\u0012\u0004\u0012\u0002HG0J\u0012\u0006\u0012\u0004\u0018\u00010\u00160IH\u0002ø\u0001\u0000¢\u0006\u0002\u0010KJ\u0019\u00104\u001a\u00020%2\u0006\u0010-\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0002\u0010LJ\u001b\u0010M\u001a\b\u0012\u0002\b\u0003\u0018\u00010N2\u0006\u0010-\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010OJ\u001b\u0010P\u001a\b\u0012\u0002\b\u0003\u0018\u00010N2\u0006\u0010-\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010OJ\u0019\u0010Q\u001a\u00020%2\u0006\u0010-\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0002\u0010LJ\u0010\u0010R\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010NH\u0014J\n\u0010S\u001a\u0004\u0018\u00010TH\u0004J\b\u0010U\u001a\u00020\u0005H\u0016R\u0014\u0010\u0004\u001a\u00020\u00058TX\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\b\u0012\u0002\b\u0003\u0018\u00010\t8DX\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\f\u001a\b\u0012\u0002\b\u0003\u0018\u00010\t8DX\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000bR\u0012\u0010\u000e\u001a\u00020\u000fX¤\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u0010R\u0012\u0010\u0011\u001a\u00020\u000fX¤\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\u0012\u001a\u00020\u000f8F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0010R\u0011\u0010\u0013\u001a\u00020\u000f8F¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0010R\u0016\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00160\u0015X\u0004¢\u0006\u0002\n\u0000R#\u0010\u0017\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00020\u00188F¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001aR\u0014\u0010\u001b\u001a\u00020\u001cX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0014\u0010\u001f\u001a\u00020\u00058BX\u0004¢\u0006\u0006\u001a\u0004\b \u0010\u0007\u0002\u0004\n\u0002\b\u0019¨\u0006\\"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel;", "E", "Lkotlinx/coroutines/channels/SendChannel;", "()V", "bufferDebugString", "", "getBufferDebugString", "()Ljava/lang/String;", "closedForReceive", "Lkotlinx/coroutines/channels/Closed;", "getClosedForReceive", "()Lkotlinx/coroutines/channels/Closed;", "closedForSend", "getClosedForSend", "isBufferAlwaysFull", "", "()Z", "isBufferFull", "isClosedForSend", "isFull", "onCloseHandler", "Lkotlinx/atomicfu/AtomicRef;", "", "onSend", "Lkotlinx/coroutines/selects/SelectClause2;", "getOnSend", "()Lkotlinx/coroutines/selects/SelectClause2;", "queue", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "getQueue", "()Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "queueDebugStateString", "getQueueDebugStateString", "close", "cause", "", "conflatePreviousSendBuffered", "", "node", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "countQueueSize", "", "describeSendBuffered", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/internal/AddLastDesc;", "element", "(Ljava/lang/Object;)Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AddLastDesc;", "describeSendConflated", "describeTryOffer", "Lkotlinx/coroutines/channels/AbstractSendChannel$TryOfferDesc;", "(Ljava/lang/Object;)Lkotlinx/coroutines/channels/AbstractSendChannel$TryOfferDesc;", "enqueueSend", "send", "Lkotlinx/coroutines/channels/SendElement;", "helpClose", "closed", "invokeOnClose", "handler", "Lkotlin/Function1;", "Lkotlinx/coroutines/channels/Handler;", "invokeOnCloseHandler", "offer", "(Ljava/lang/Object;)Z", "offerInternal", "(Ljava/lang/Object;)Ljava/lang/Object;", "offerSelectInternal", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "(Ljava/lang/Object;Lkotlinx/coroutines/selects/SelectInstance;)Ljava/lang/Object;", "onClosedIdempotent", "registerSelectSend", "R", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "(Lkotlinx/coroutines/selects/SelectInstance;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendBuffered", "Lkotlinx/coroutines/channels/ReceiveOrClosed;", "(Ljava/lang/Object;)Lkotlinx/coroutines/channels/ReceiveOrClosed;", "sendConflated", "sendSuspend", "takeFirstReceiveOrPeekClosed", "takeFirstSendOrPeekClosed", "Lkotlinx/coroutines/channels/Send;", "toString", "SendBuffered", "SendBufferedDesc", "SendConflatedDesc", "SendSelect", "TryEnqueueSendDesc", "TryOfferDesc", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* compiled from: AbstractChannel.kt */
public abstract class AbstractSendChannel<E> implements SendChannel<E> {
    private static final AtomicReferenceFieldUpdater onCloseHandler$FU = AtomicReferenceFieldUpdater.newUpdater(AbstractSendChannel.class, Object.class, "onCloseHandler");
    private volatile Object onCloseHandler = null;
    private final LockFreeLinkedListHead queue = new LockFreeLinkedListHead();

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0002\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\u00020\u00022\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00028\u0001¢\u0006\u0002\u0010\u0005J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0016J\u0014\u0010\u000e\u001a\u00020\f2\n\u0010\u000f\u001a\u0006\u0012\u0002\b\u00030\u0010H\u0016J\u0014\u0010\u0011\u001a\u0004\u0018\u00010\b2\b\u0010\u0012\u001a\u0004\u0018\u00010\bH\u0016R\u0012\u0010\u0004\u001a\u00028\u00018\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\u0006R\u0016\u0010\u0007\u001a\u0004\u0018\u00010\b8VX\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\n\u0002\u0004\n\u0002\b\u0019¨\u0006\u0013"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel$SendBuffered;", "E", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/channels/Send;", "element", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", "pollResult", "", "getPollResult", "()Ljava/lang/Object;", "completeResumeSend", "", "token", "resumeSendClosed", "closed", "Lkotlinx/coroutines/channels/Closed;", "tryResumeSend", "idempotent", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* compiled from: AbstractChannel.kt */
    private static final class SendBuffered<E> extends LockFreeLinkedListNode implements Send {
        public final E element;

        public void resumeSendClosed(Closed<?> closed) {
            Intrinsics.checkParameterIsNotNull(closed, "closed");
        }

        public SendBuffered(E e) {
            this.element = e;
        }

        public Object getPollResult() {
            return this.element;
        }

        public Object tryResumeSend(Object obj) {
            return AbstractChannelKt.SEND_RESUMED;
        }

        public void completeResumeSend(Object obj) {
            Intrinsics.checkParameterIsNotNull(obj, VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_TOKEN);
            if (!(obj == AbstractChannelKt.SEND_RESUMED)) {
                throw new IllegalStateException("Check failed.".toString());
            }
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0012\u0018\u0000*\u0004\b\u0001\u0010\u00012\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00030\u0002j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0003`\u0004B\u0015\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00028\u0001¢\u0006\u0002\u0010\bJ\u001a\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\nH\u0014¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel$SendBufferedDesc;", "E", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/channels/AbstractSendChannel$SendBuffered;", "Lkotlinx/coroutines/internal/AddLastDesc;", "queue", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "element", "(Lkotlinx/coroutines/internal/LockFreeLinkedListHead;Ljava/lang/Object;)V", "failure", "", "affected", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "next", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* compiled from: AbstractChannel.kt */
    private static class SendBufferedDesc<E> extends AddLastDesc<SendBuffered<? extends E>> {
        public SendBufferedDesc(LockFreeLinkedListHead lockFreeLinkedListHead, E e) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListHead, "queue");
            super(lockFreeLinkedListHead, new SendBuffered(e));
        }

        /* access modifiers changed from: protected */
        public Object failure(LockFreeLinkedListNode lockFreeLinkedListNode, Object obj) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(obj, "next");
            if (lockFreeLinkedListNode instanceof ReceiveOrClosed) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            return null;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00028\u0001¢\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0014¨\u0006\f"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel$SendConflatedDesc;", "E", "Lkotlinx/coroutines/channels/AbstractSendChannel$SendBufferedDesc;", "queue", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "element", "(Lkotlinx/coroutines/internal/LockFreeLinkedListHead;Ljava/lang/Object;)V", "finishOnSuccess", "", "affected", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "next", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* compiled from: AbstractChannel.kt */
    private static final class SendConflatedDesc<E> extends SendBufferedDesc<E> {
        public SendConflatedDesc(LockFreeLinkedListHead lockFreeLinkedListHead, E e) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListHead, "queue");
            super(lockFreeLinkedListHead, e);
        }

        /* access modifiers changed from: protected */
        public void finishOnSuccess(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
            super.finishOnSuccess(lockFreeLinkedListNode, lockFreeLinkedListNode2);
            if (!(lockFreeLinkedListNode instanceof SendBuffered)) {
                lockFreeLinkedListNode = null;
            }
            SendBuffered sendBuffered = (SendBuffered) lockFreeLinkedListNode;
            if (sendBuffered != null) {
                sendBuffered.remove();
            }
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u0001*\u0004\b\u0002\u0010\u00022\u00020\u00032\u00020\u00042\u00020\u0005BX\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00010\t\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00020\u000b\u0012(\u0010\f\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\t\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00020\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00070\rø\u0001\u0000¢\u0006\u0002\u0010\u000fJ\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0007H\u0016J\b\u0010\u0016\u001a\u00020\u0014H\u0016J\u0006\u0010\u0017\u001a\u00020\u0014J\u0014\u0010\u0018\u001a\u00020\u00142\n\u0010\u0019\u001a\u0006\u0012\u0002\b\u00030\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\u0014\u0010\u001d\u001a\u0004\u0018\u00010\u00072\b\u0010\u001e\u001a\u0004\u0018\u00010\u0007H\u0016R7\u0010\f\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\t\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00020\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00070\r8\u0006X\u0004ø\u0001\u0000¢\u0006\u0004\n\u0002\u0010\u0010R\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00010\t8\u0006X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0016\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00020\u000b8\u0006X\u0004¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006\u001f"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel$SendSelect;", "E", "R", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/channels/Send;", "Lkotlinx/coroutines/DisposableHandle;", "pollResult", "", "channel", "Lkotlinx/coroutines/channels/SendChannel;", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "(Ljava/lang/Object;Lkotlinx/coroutines/channels/SendChannel;Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "Lkotlin/jvm/functions/Function2;", "getPollResult", "()Ljava/lang/Object;", "completeResumeSend", "", "token", "dispose", "disposeOnSelect", "resumeSendClosed", "closed", "Lkotlinx/coroutines/channels/Closed;", "toString", "", "tryResumeSend", "idempotent", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* compiled from: AbstractChannel.kt */
    private static final class SendSelect<E, R> extends LockFreeLinkedListNode implements Send, DisposableHandle {
        public final Function2<SendChannel<? super E>, Continuation<? super R>, Object> block;
        public final SendChannel<E> channel;
        private final Object pollResult;
        public final SelectInstance<R> select;

        public Object getPollResult() {
            return this.pollResult;
        }

        public SendSelect(Object obj, SendChannel<? super E> sendChannel, SelectInstance<? super R> selectInstance, Function2<? super SendChannel<? super E>, ? super Continuation<? super R>, ? extends Object> function2) {
            Intrinsics.checkParameterIsNotNull(sendChannel, "channel");
            Intrinsics.checkParameterIsNotNull(selectInstance, "select");
            Intrinsics.checkParameterIsNotNull(function2, "block");
            this.pollResult = obj;
            this.channel = sendChannel;
            this.select = selectInstance;
            this.block = function2;
        }

        public Object tryResumeSend(Object obj) {
            if (this.select.trySelect(obj)) {
                return AbstractChannelKt.SELECT_STARTED;
            }
            return null;
        }

        public void completeResumeSend(Object obj) {
            Intrinsics.checkParameterIsNotNull(obj, VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_TOKEN);
            if (obj == AbstractChannelKt.SELECT_STARTED) {
                ContinuationKt.startCoroutine(this.block, this.channel, this.select.getCompletion());
                return;
            }
            throw new IllegalStateException("Check failed.".toString());
        }

        public final void disposeOnSelect() {
            this.select.disposeOnSelect(this);
        }

        public void dispose() {
            remove();
        }

        public void resumeSendClosed(Closed<?> closed) {
            Intrinsics.checkParameterIsNotNull(closed, "closed");
            if (this.select.trySelect(null)) {
                this.select.resumeSelectCancellableWithException(closed.getSendException());
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("SendSelect(");
            sb.append(getPollResult());
            sb.append(")[");
            sb.append(this.channel);
            sb.append(", ");
            sb.append(this.select);
            sb.append(']');
            return sb.toString();
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0004\u0018\u0000*\u0004\b\u0001\u0010\u00012*\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u0002H\u00010\u00030\u0002j\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u0002H\u00010\u0003`\u0004BH\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00010\u0007\u0012(\u0010\b\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\n\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00010\u000b\u0012\u0006\u0012\u0004\u0018\u00010\f0\tø\u0001\u0000¢\u0006\u0002\u0010\rJ\u001a\u0010\u000e\u001a\u0004\u0018\u00010\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\fH\u0014J\u0018\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0014J\u001a\u0010\u0014\u001a\u0004\u0018\u00010\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0014\u0002\u0004\n\u0002\b\u0019¨\u0006\u0015"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel$TryEnqueueSendDesc;", "R", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$AddLastDesc;", "Lkotlinx/coroutines/channels/AbstractSendChannel$SendSelect;", "Lkotlinx/coroutines/internal/AddLastDesc;", "element", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/channels/SendChannel;", "Lkotlin/coroutines/Continuation;", "", "(Lkotlinx/coroutines/channels/AbstractSendChannel;Ljava/lang/Object;Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "failure", "affected", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "next", "finishOnSuccess", "", "onPrepare", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* compiled from: AbstractChannel.kt */
    private final class TryEnqueueSendDesc<R> extends AddLastDesc<SendSelect<E, R>> {
        final /* synthetic */ AbstractSendChannel this$0;

        public TryEnqueueSendDesc(AbstractSendChannel abstractSendChannel, E e, SelectInstance<? super R> selectInstance, Function2<? super SendChannel<? super E>, ? super Continuation<? super R>, ? extends Object> function2) {
            Intrinsics.checkParameterIsNotNull(selectInstance, "select");
            Intrinsics.checkParameterIsNotNull(function2, "block");
            this.this$0 = abstractSendChannel;
            super(abstractSendChannel.getQueue(), new SendSelect(e, abstractSendChannel, selectInstance, function2));
        }

        /* access modifiers changed from: protected */
        public Object failure(LockFreeLinkedListNode lockFreeLinkedListNode, Object obj) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(obj, "next");
            if (!(lockFreeLinkedListNode instanceof ReceiveOrClosed)) {
                return null;
            }
            if (!(lockFreeLinkedListNode instanceof Closed)) {
                lockFreeLinkedListNode = null;
            }
            Object obj2 = (Closed) lockFreeLinkedListNode;
            if (obj2 == null) {
                obj2 = AbstractChannelKt.ENQUEUE_FAILED;
            }
            return obj2;
        }

        /* access modifiers changed from: protected */
        public Object onPrepare(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
            if (!this.this$0.isBufferFull()) {
                return AbstractChannelKt.ENQUEUE_FAILED;
            }
            return super.onPrepare(lockFreeLinkedListNode, lockFreeLinkedListNode2);
        }

        /* access modifiers changed from: protected */
        public void finishOnSuccess(LockFreeLinkedListNode lockFreeLinkedListNode, LockFreeLinkedListNode lockFreeLinkedListNode2) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode2, "next");
            super.finishOnSuccess(lockFreeLinkedListNode, lockFreeLinkedListNode2);
            ((SendSelect) this.node).disposeOnSelect();
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0004\u0018\u0000*\u0004\b\u0001\u0010\u00012\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u00030\u0002j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0003`\u0004B\u0015\u0012\u0006\u0010\u0005\u001a\u00028\u0001\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u001a\u0010\f\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000bH\u0014J\u0016\u0010\u0010\u001a\u00020\u00112\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00028\u00010\u0003H\u0014R\u0012\u0010\u0005\u001a\u00028\u00018\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\tR\u0014\u0010\n\u001a\u0004\u0018\u00010\u000b8\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lkotlinx/coroutines/channels/AbstractSendChannel$TryOfferDesc;", "E", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$RemoveFirstDesc;", "Lkotlinx/coroutines/channels/ReceiveOrClosed;", "Lkotlinx/coroutines/internal/RemoveFirstDesc;", "element", "queue", "Lkotlinx/coroutines/internal/LockFreeLinkedListHead;", "(Ljava/lang/Object;Lkotlinx/coroutines/internal/LockFreeLinkedListHead;)V", "Ljava/lang/Object;", "resumeToken", "", "failure", "affected", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "next", "validatePrepared", "", "node", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* compiled from: AbstractChannel.kt */
    protected static final class TryOfferDesc<E> extends RemoveFirstDesc<ReceiveOrClosed<? super E>> {
        public final E element;
        public Object resumeToken;

        public TryOfferDesc(E e, LockFreeLinkedListHead lockFreeLinkedListHead) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListHead, "queue");
            super(lockFreeLinkedListHead);
            this.element = e;
        }

        /* access modifiers changed from: protected */
        public Object failure(LockFreeLinkedListNode lockFreeLinkedListNode, Object obj) {
            Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "affected");
            Intrinsics.checkParameterIsNotNull(obj, "next");
            if (!(lockFreeLinkedListNode instanceof ReceiveOrClosed)) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            if (lockFreeLinkedListNode instanceof Closed) {
                return lockFreeLinkedListNode;
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public boolean validatePrepared(ReceiveOrClosed<? super E> receiveOrClosed) {
            Intrinsics.checkParameterIsNotNull(receiveOrClosed, "node");
            Object tryResumeReceive = receiveOrClosed.tryResumeReceive(this.element, this);
            if (tryResumeReceive == null) {
                return false;
            }
            this.resumeToken = tryResumeReceive;
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public String getBufferDebugString() {
        return "";
    }

    /* access modifiers changed from: protected */
    public abstract boolean isBufferAlwaysFull();

    /* access modifiers changed from: protected */
    public abstract boolean isBufferFull();

    /* access modifiers changed from: protected */
    public void onClosedIdempotent(LockFreeLinkedListNode lockFreeLinkedListNode) {
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "closed");
    }

    /* access modifiers changed from: protected */
    public final LockFreeLinkedListHead getQueue() {
        return this.queue;
    }

    /* access modifiers changed from: protected */
    public Object offerInternal(E e) {
        ReceiveOrClosed takeFirstReceiveOrPeekClosed;
        Object tryResumeReceive;
        do {
            takeFirstReceiveOrPeekClosed = takeFirstReceiveOrPeekClosed();
            if (takeFirstReceiveOrPeekClosed == null) {
                return AbstractChannelKt.OFFER_FAILED;
            }
            tryResumeReceive = takeFirstReceiveOrPeekClosed.tryResumeReceive(e, null);
        } while (tryResumeReceive == null);
        takeFirstReceiveOrPeekClosed.completeResumeReceive(tryResumeReceive);
        return takeFirstReceiveOrPeekClosed.getOfferResult();
    }

    /* access modifiers changed from: protected */
    public Object offerSelectInternal(E e, SelectInstance<?> selectInstance) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        TryOfferDesc describeTryOffer = describeTryOffer(e);
        Object performAtomicTrySelect = selectInstance.performAtomicTrySelect(describeTryOffer);
        if (performAtomicTrySelect != null) {
            return performAtomicTrySelect;
        }
        ReceiveOrClosed receiveOrClosed = (ReceiveOrClosed) describeTryOffer.getResult();
        Object obj = describeTryOffer.resumeToken;
        if (obj == null) {
            Intrinsics.throwNpe();
        }
        receiveOrClosed.completeResumeReceive(obj);
        return receiveOrClosed.getOfferResult();
    }

    /* access modifiers changed from: protected */
    public final Closed<?> getClosedForSend() {
        LockFreeLinkedListNode prevNode = this.queue.getPrevNode();
        if (!(prevNode instanceof Closed)) {
            prevNode = null;
        }
        Closed<?> closed = (Closed) prevNode;
        if (closed == null) {
            return null;
        }
        helpClose(closed);
        return closed;
    }

    /* access modifiers changed from: protected */
    public final Closed<?> getClosedForReceive() {
        LockFreeLinkedListNode nextNode = this.queue.getNextNode();
        if (!(nextNode instanceof Closed)) {
            nextNode = null;
        }
        Closed<?> closed = (Closed) nextNode;
        if (closed == null) {
            return null;
        }
        helpClose(closed);
        return closed;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0024, code lost:
        r3 = r1;
     */
    public final Send takeFirstSendOrPeekClosed() {
        Send send;
        LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
        while (true) {
            Object next = lockFreeLinkedListHead.getNext();
            if (next != null) {
                LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) next;
                send = null;
                if (lockFreeLinkedListNode == lockFreeLinkedListHead || !(lockFreeLinkedListNode instanceof Send)) {
                    break;
                } else if (!(((Send) lockFreeLinkedListNode) instanceof Closed) && !lockFreeLinkedListNode.remove()) {
                    lockFreeLinkedListNode.helpDelete();
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        }
        return send;
    }

    /* access modifiers changed from: protected */
    public final ReceiveOrClosed<?> sendBuffered(E e) {
        LockFreeLinkedListNode lockFreeLinkedListNode;
        LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
        LockFreeLinkedListNode sendBuffered = new SendBuffered(e);
        do {
            Object prev = lockFreeLinkedListHead.getPrev();
            if (prev != null) {
                lockFreeLinkedListNode = (LockFreeLinkedListNode) prev;
                if (lockFreeLinkedListNode instanceof ReceiveOrClosed) {
                    return (ReceiveOrClosed) lockFreeLinkedListNode;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        } while (!lockFreeLinkedListNode.addNext(sendBuffered, lockFreeLinkedListHead));
        return null;
    }

    /* access modifiers changed from: protected */
    public final ReceiveOrClosed<?> sendConflated(E e) {
        LockFreeLinkedListNode lockFreeLinkedListNode;
        SendBuffered sendBuffered = new SendBuffered(e);
        LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
        do {
            Object prev = lockFreeLinkedListHead.getPrev();
            if (prev != null) {
                lockFreeLinkedListNode = (LockFreeLinkedListNode) prev;
                if (lockFreeLinkedListNode instanceof ReceiveOrClosed) {
                    return (ReceiveOrClosed) lockFreeLinkedListNode;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        } while (!lockFreeLinkedListNode.addNext(sendBuffered, lockFreeLinkedListHead));
        conflatePreviousSendBuffered(sendBuffered);
        return null;
    }

    /* access modifiers changed from: protected */
    public final void conflatePreviousSendBuffered(LockFreeLinkedListNode lockFreeLinkedListNode) {
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "node");
        for (LockFreeLinkedListNode prevNode = lockFreeLinkedListNode.getPrevNode(); prevNode instanceof SendBuffered; prevNode = prevNode.getPrevNode()) {
            if (!prevNode.remove()) {
                prevNode.helpRemove();
            }
        }
    }

    /* access modifiers changed from: protected */
    public final AddLastDesc<?> describeSendBuffered(E e) {
        return new SendBufferedDesc<>(this.queue, e);
    }

    /* access modifiers changed from: protected */
    public final AddLastDesc<?> describeSendConflated(E e) {
        return new SendConflatedDesc<>(this.queue, e);
    }

    public final boolean isClosedForSend() {
        return getClosedForSend() != null;
    }

    public final boolean isFull() {
        return !(this.queue.getNextNode() instanceof ReceiveOrClosed) && isBufferFull();
    }

    public final Object send(E e, Continuation<? super Unit> continuation) {
        if (offer(e)) {
            return Unit.INSTANCE;
        }
        return sendSuspend(e, continuation);
    }

    public final boolean offer(E e) {
        Object offerInternal = offerInternal(e);
        if (offerInternal == AbstractChannelKt.OFFER_SUCCESS) {
            return true;
        }
        if (offerInternal == AbstractChannelKt.OFFER_FAILED) {
            Closed closedForSend = getClosedForSend();
            if (closedForSend != null) {
                Throwable sendException = closedForSend.getSendException();
                if (sendException != null) {
                    Throwable recoverStackTrace = StackTraceRecoveryKt.recoverStackTrace(sendException);
                    if (recoverStackTrace != null) {
                        throw recoverStackTrace;
                    }
                }
            }
            return false;
        } else if (offerInternal instanceof Closed) {
            throw StackTraceRecoveryKt.recoverStackTrace(((Closed) offerInternal).getSendException());
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("offerInternal returned ");
            sb.append(offerInternal);
            throw new IllegalStateException(sb.toString().toString());
        }
    }

    /* access modifiers changed from: private */
    public final Object enqueueSend(SendElement sendElement) {
        boolean z;
        LockFreeLinkedListNode lockFreeLinkedListNode;
        String str = "null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */";
        if (isBufferAlwaysFull()) {
            LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
            do {
                Object prev = lockFreeLinkedListHead.getPrev();
                if (prev != null) {
                    lockFreeLinkedListNode = (LockFreeLinkedListNode) prev;
                    if (lockFreeLinkedListNode instanceof ReceiveOrClosed) {
                        return lockFreeLinkedListNode;
                    }
                } else {
                    throw new TypeCastException(str);
                }
            } while (!lockFreeLinkedListNode.addNext(sendElement, lockFreeLinkedListHead));
        } else {
            LockFreeLinkedListHead lockFreeLinkedListHead2 = this.queue;
            LockFreeLinkedListNode lockFreeLinkedListNode2 = sendElement;
            CondAddOp abstractSendChannel$enqueueSend$$inlined$addLastIfPrevAndIf$1 = new AbstractSendChannel$enqueueSend$$inlined$addLastIfPrevAndIf$1(lockFreeLinkedListNode2, lockFreeLinkedListNode2, this);
            while (true) {
                Object prev2 = lockFreeLinkedListHead2.getPrev();
                if (prev2 != null) {
                    LockFreeLinkedListNode lockFreeLinkedListNode3 = (LockFreeLinkedListNode) prev2;
                    if (!(lockFreeLinkedListNode3 instanceof ReceiveOrClosed)) {
                        int tryCondAddNext = lockFreeLinkedListNode3.tryCondAddNext(lockFreeLinkedListNode2, lockFreeLinkedListHead2, abstractSendChannel$enqueueSend$$inlined$addLastIfPrevAndIf$1);
                        z = true;
                        if (tryCondAddNext != 1) {
                            if (tryCondAddNext == 2) {
                                z = false;
                                break;
                            }
                        } else {
                            break;
                        }
                    } else {
                        return lockFreeLinkedListNode3;
                    }
                } else {
                    throw new TypeCastException(str);
                }
            }
            if (!z) {
                return AbstractChannelKt.ENQUEUE_FAILED;
            }
        }
        return null;
    }

    public boolean close(Throwable th) {
        boolean z;
        Closed closed = new Closed(th);
        LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
        while (true) {
            Object prev = lockFreeLinkedListHead.getPrev();
            if (prev != null) {
                LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) prev;
                if (!(lockFreeLinkedListNode instanceof Closed)) {
                    if (lockFreeLinkedListNode.addNext(closed, lockFreeLinkedListHead)) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        }
        if (!z) {
            LockFreeLinkedListNode prevNode = this.queue.getPrevNode();
            if (prevNode != null) {
                helpClose((Closed) prevNode);
                return false;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.channels.Closed<*>");
        }
        helpClose(closed);
        invokeOnCloseHandler(th);
        return true;
    }

    private final void invokeOnCloseHandler(Throwable th) {
        Object obj = this.onCloseHandler;
        if (obj != null && obj != AbstractChannelKt.HANDLER_INVOKED && onCloseHandler$FU.compareAndSet(this, obj, AbstractChannelKt.HANDLER_INVOKED)) {
            ((Function1) TypeIntrinsics.beforeCheckcastToFunctionOfArity(obj, 1)).invoke(th);
        }
    }

    public void invokeOnClose(Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        if (!onCloseHandler$FU.compareAndSet(this, null, function1)) {
            Object obj = this.onCloseHandler;
            if (obj == AbstractChannelKt.HANDLER_INVOKED) {
                throw new IllegalStateException("Another handler was already registered and successfully invoked");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Another handler was already registered: ");
            sb.append(obj);
            throw new IllegalStateException(sb.toString());
        }
        Closed closedForSend = getClosedForSend();
        if (closedForSend != null && onCloseHandler$FU.compareAndSet(this, function1, AbstractChannelKt.HANDLER_INVOKED)) {
            function1.invoke(closedForSend.closeCause);
        }
    }

    /* access modifiers changed from: private */
    public final void helpClose(Closed<?> closed) {
        while (true) {
            LockFreeLinkedListNode prevNode = closed.getPrevNode();
            if ((prevNode instanceof LockFreeLinkedListHead) || !(prevNode instanceof Receive)) {
                onClosedIdempotent(closed);
            } else if (!prevNode.remove()) {
                prevNode.helpRemove();
            } else {
                ((Receive) prevNode).resumeReceiveClosed(closed);
            }
        }
        onClosedIdempotent(closed);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0024, code lost:
        r3 = r1;
     */
    public ReceiveOrClosed<E> takeFirstReceiveOrPeekClosed() {
        ReceiveOrClosed<E> receiveOrClosed;
        LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
        while (true) {
            Object next = lockFreeLinkedListHead.getNext();
            if (next != null) {
                LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) next;
                receiveOrClosed = null;
                if (lockFreeLinkedListNode == lockFreeLinkedListHead || !(lockFreeLinkedListNode instanceof ReceiveOrClosed)) {
                    break;
                } else if (!(((ReceiveOrClosed) lockFreeLinkedListNode) instanceof Closed) && !lockFreeLinkedListNode.remove()) {
                    lockFreeLinkedListNode.helpDelete();
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        }
        return receiveOrClosed;
    }

    /* access modifiers changed from: protected */
    public final TryOfferDesc<E> describeTryOffer(E e) {
        return new TryOfferDesc<>(e, this.queue);
    }

    public final SelectClause2<E, SendChannel<E>> getOnSend() {
        return new AbstractSendChannel$onSend$1<>(this);
    }

    /* access modifiers changed from: private */
    public final <R> void registerSelectSend(SelectInstance<? super R> selectInstance, E e, Function2<? super SendChannel<? super E>, ? super Continuation<? super R>, ? extends Object> function2) {
        while (!selectInstance.isSelected()) {
            if (isFull()) {
                Object performAtomicIfNotSelected = selectInstance.performAtomicIfNotSelected(new TryEnqueueSendDesc(this, e, selectInstance, function2));
                if (performAtomicIfNotSelected != null && performAtomicIfNotSelected != SelectKt.getALREADY_SELECTED()) {
                    if (performAtomicIfNotSelected != AbstractChannelKt.ENQUEUE_FAILED) {
                        if (performAtomicIfNotSelected instanceof Closed) {
                            throw StackTraceRecoveryKt.recoverStackTrace(((Closed) performAtomicIfNotSelected).getSendException());
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("performAtomicIfNotSelected(TryEnqueueSendDesc) returned ");
                        sb.append(performAtomicIfNotSelected);
                        throw new IllegalStateException(sb.toString().toString());
                    }
                } else {
                    return;
                }
            } else {
                Object offerSelectInternal = offerSelectInternal(e, selectInstance);
                if (offerSelectInternal != SelectKt.getALREADY_SELECTED()) {
                    if (offerSelectInternal != AbstractChannelKt.OFFER_FAILED) {
                        if (offerSelectInternal == AbstractChannelKt.OFFER_SUCCESS) {
                            UndispatchedKt.startCoroutineUnintercepted(function2, this, selectInstance.getCompletion());
                            return;
                        } else if (offerSelectInternal instanceof Closed) {
                            throw StackTraceRecoveryKt.recoverStackTrace(((Closed) offerSelectInternal).getSendException());
                        } else {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("offerSelectInternal returned ");
                            sb2.append(offerSelectInternal);
                            throw new IllegalStateException(sb2.toString().toString());
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(DebugKt.getClassSimpleName(this));
        sb.append('@');
        sb.append(DebugKt.getHexAddress(this));
        sb.append('{');
        sb.append(getQueueDebugStateString());
        sb.append('}');
        sb.append(getBufferDebugString());
        return sb.toString();
    }

    private final String getQueueDebugStateString() {
        String str;
        LockFreeLinkedListNode nextNode = this.queue.getNextNode();
        if (nextNode == this.queue) {
            return "EmptyQueue";
        }
        if (nextNode instanceof Closed) {
            str = nextNode.toString();
        } else if (nextNode instanceof Receive) {
            str = "ReceiveQueued";
        } else if (nextNode instanceof Send) {
            str = "SendQueued";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("UNEXPECTED:");
            sb.append(nextNode);
            str = sb.toString();
        }
        LockFreeLinkedListNode prevNode = this.queue.getPrevNode();
        if (prevNode != nextNode) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(",queueSize=");
            sb2.append(countQueueSize());
            str = sb2.toString();
            if (prevNode instanceof Closed) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str);
                sb3.append(",closedForSend=");
                sb3.append(prevNode);
                str = sb3.toString();
            }
        }
        return str;
    }

    private final int countQueueSize() {
        LockFreeLinkedListHead lockFreeLinkedListHead = this.queue;
        Object next = lockFreeLinkedListHead.getNext();
        if (next != null) {
            int i = 0;
            for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) next; !Intrinsics.areEqual((Object) lockFreeLinkedListNode, (Object) lockFreeLinkedListHead); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
                if (lockFreeLinkedListNode instanceof LockFreeLinkedListNode) {
                    i++;
                }
            }
            return i;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ Object sendSuspend(E e, Continuation<? super Unit> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 0);
        CancellableContinuation cancellableContinuation = cancellableContinuationImpl;
        SendElement sendElement = new SendElement(e, cancellableContinuation);
        while (true) {
            Object access$enqueueSend = enqueueSend(sendElement);
            if (access$enqueueSend == null) {
                CancellableContinuationKt.removeOnCancellation(cancellableContinuation, sendElement);
                break;
            } else if (access$enqueueSend instanceof Closed) {
                Closed closed = (Closed) access$enqueueSend;
                helpClose(closed);
                Continuation continuation2 = cancellableContinuation;
                Throwable sendException = closed.getSendException();
                Companion companion = Result.Companion;
                continuation2.resumeWith(Result.m3constructorimpl(ResultKt.createFailure(sendException)));
                break;
            } else {
                Object offerInternal = offerInternal(e);
                if (offerInternal == AbstractChannelKt.OFFER_SUCCESS) {
                    Continuation continuation3 = cancellableContinuation;
                    Unit unit = Unit.INSTANCE;
                    Companion companion2 = Result.Companion;
                    continuation3.resumeWith(Result.m3constructorimpl(unit));
                    break;
                } else if (offerInternal != AbstractChannelKt.OFFER_FAILED) {
                    if (offerInternal instanceof Closed) {
                        Closed closed2 = (Closed) offerInternal;
                        helpClose(closed2);
                        Continuation continuation4 = cancellableContinuation;
                        Throwable sendException2 = closed2.getSendException();
                        Companion companion3 = Result.Companion;
                        continuation4.resumeWith(Result.m3constructorimpl(ResultKt.createFailure(sendException2)));
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("offerInternal returned ");
                        sb.append(offerInternal);
                        throw new IllegalStateException(sb.toString().toString());
                    }
                }
            }
        }
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }
}
