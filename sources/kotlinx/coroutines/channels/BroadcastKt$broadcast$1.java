package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.Result.Failure;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "kotlinx.coroutines.channels.BroadcastKt$broadcast$1", f = "Broadcast.kt", i = {2}, l = {29, 29, 30}, m = "invokeSuspend", n = {"e"}, s = {"L$1"})
/* compiled from: Broadcast.kt */
final class BroadcastKt$broadcast$1 extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ReceiveChannel $this_broadcast;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    BroadcastKt$broadcast$1(ReceiveChannel receiveChannel, Continuation continuation) {
        this.$this_broadcast = receiveChannel;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        BroadcastKt$broadcast$1 broadcastKt$broadcast$1 = new BroadcastKt$broadcast$1(this.$this_broadcast, continuation);
        broadcastKt$broadcast$1.p$ = (ProducerScope) obj;
        return broadcastKt$broadcast$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((BroadcastKt$broadcast$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x007c  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0098  */
    public final Object invokeSuspend(Object obj) {
        Object obj2;
        ProducerScope producerScope;
        ChannelIterator channelIterator;
        BroadcastKt$broadcast$1 broadcastKt$broadcast$1;
        BroadcastKt$broadcast$1 broadcastKt$broadcast$12;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i != 0) {
            if (i == 1) {
                channelIterator = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    broadcastKt$broadcast$1 = this;
                    if (!((Boolean) obj).booleanValue()) {
                    }
                    return Unit.INSTANCE;
                }
                throw ((Failure) obj).exception;
            } else if (i == 2) {
                channelIterator = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    broadcastKt$broadcast$1 = this;
                    broadcastKt$broadcast$1.L$0 = producerScope;
                    broadcastKt$broadcast$1.L$1 = obj;
                    broadcastKt$broadcast$1.L$2 = channelIterator;
                    broadcastKt$broadcast$1.label = 3;
                    if (producerScope.send(obj, broadcastKt$broadcast$1) != obj2) {
                        return obj2;
                    }
                    broadcastKt$broadcast$12 = broadcastKt$broadcast$1;
                    coroutine_suspended = obj2;
                    broadcastKt$broadcast$12.L$0 = producerScope;
                    broadcastKt$broadcast$12.L$1 = channelIterator;
                    broadcastKt$broadcast$12.label = 1;
                    hasNext = channelIterator.hasNext(broadcastKt$broadcast$12);
                    if (hasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    Object obj3 = coroutine_suspended;
                    broadcastKt$broadcast$1 = broadcastKt$broadcast$12;
                    obj = hasNext;
                    obj2 = obj3;
                    if (!((Boolean) obj).booleanValue()) {
                        broadcastKt$broadcast$1.L$0 = producerScope;
                        broadcastKt$broadcast$1.L$1 = channelIterator;
                        broadcastKt$broadcast$1.label = 2;
                        obj = channelIterator.next(broadcastKt$broadcast$1);
                        if (obj == obj2) {
                            return obj2;
                        }
                        broadcastKt$broadcast$1.L$0 = producerScope;
                        broadcastKt$broadcast$1.L$1 = obj;
                        broadcastKt$broadcast$1.L$2 = channelIterator;
                        broadcastKt$broadcast$1.label = 3;
                        if (producerScope.send(obj, broadcastKt$broadcast$1) != obj2) {
                        }
                    }
                    return Unit.INSTANCE;
                    return coroutine_suspended;
                    return obj2;
                }
                throw ((Failure) obj).exception;
            } else if (i == 3) {
                channelIterator = (ChannelIterator) this.L$2;
                Object obj4 = this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (obj instanceof Failure) {
                    throw ((Failure) obj).exception;
                }
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(obj instanceof Failure)) {
            ProducerScope producerScope2 = this.p$;
            channelIterator = this.$this_broadcast.iterator();
            producerScope = producerScope2;
        } else {
            throw ((Failure) obj).exception;
        }
        broadcastKt$broadcast$12 = this;
        broadcastKt$broadcast$12.L$0 = producerScope;
        broadcastKt$broadcast$12.L$1 = channelIterator;
        broadcastKt$broadcast$12.label = 1;
        hasNext = channelIterator.hasNext(broadcastKt$broadcast$12);
        if (hasNext != coroutine_suspended) {
        }
        return coroutine_suspended;
    }
}
