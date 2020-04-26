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
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$takeWhile$1", f = "Channels.common.kt", i = {2, 3}, l = {869, 869, 870, 871}, m = "invokeSuspend", n = {"e", "e"}, s = {"L$1", "L$1"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$takeWhile$1 extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2 $predicate;
    final /* synthetic */ ReceiveChannel $this_takeWhile;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    ChannelsKt__Channels_commonKt$takeWhile$1(ReceiveChannel receiveChannel, Function2 function2, Continuation continuation) {
        this.$this_takeWhile = receiveChannel;
        this.$predicate = function2;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$takeWhile$1 channelsKt__Channels_commonKt$takeWhile$1 = new ChannelsKt__Channels_commonKt$takeWhile$1(this.$this_takeWhile, this.$predicate, continuation);
        channelsKt__Channels_commonKt$takeWhile$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$takeWhile$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$takeWhile$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0095 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0096  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00d3  */
    public final Object invokeSuspend(Object obj) {
        Object obj2;
        ChannelIterator channelIterator;
        ProducerScope producerScope;
        ChannelsKt__Channels_commonKt$takeWhile$1 channelsKt__Channels_commonKt$takeWhile$1;
        Object obj3;
        ChannelsKt__Channels_commonKt$takeWhile$1 channelsKt__Channels_commonKt$takeWhile$12;
        ProducerScope producerScope2;
        ChannelIterator channelIterator2;
        Object obj4;
        Object obj5;
        ProducerScope producerScope3;
        ChannelsKt__Channels_commonKt$takeWhile$1 channelsKt__Channels_commonKt$takeWhile$13;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i != 0) {
            if (i == 1) {
                channelIterator2 = (ChannelIterator) this.L$1;
                producerScope3 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj5 = coroutine_suspended;
                    channelsKt__Channels_commonKt$takeWhile$13 = this;
                    if (!((Boolean) obj).booleanValue()) {
                    }
                    return Unit.INSTANCE;
                }
                throw ((Failure) obj).exception;
            } else if (i == 2) {
                channelIterator2 = (ChannelIterator) this.L$1;
                producerScope3 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj5 = coroutine_suspended;
                    channelsKt__Channels_commonKt$takeWhile$13 = this;
                } else {
                    throw ((Failure) obj).exception;
                }
            } else if (i == 3) {
                ChannelIterator channelIterator3 = (ChannelIterator) this.L$2;
                Object obj6 = this.L$1;
                ProducerScope producerScope4 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    obj3 = obj6;
                    producerScope = producerScope4;
                    channelIterator = channelIterator3;
                    channelsKt__Channels_commonKt$takeWhile$1 = this;
                    if (!((Boolean) obj).booleanValue()) {
                        channelsKt__Channels_commonKt$takeWhile$1.L$0 = producerScope;
                        channelsKt__Channels_commonKt$takeWhile$1.L$1 = obj3;
                        channelsKt__Channels_commonKt$takeWhile$1.L$2 = channelIterator;
                        channelsKt__Channels_commonKt$takeWhile$1.label = 4;
                        if (producerScope.send(obj3, channelsKt__Channels_commonKt$takeWhile$1) == obj2) {
                            return obj2;
                        }
                        channelsKt__Channels_commonKt$takeWhile$12 = channelsKt__Channels_commonKt$takeWhile$1;
                        producerScope2 = producerScope;
                        channelIterator2 = channelIterator;
                        obj4 = obj2;
                        channelsKt__Channels_commonKt$takeWhile$12.L$0 = producerScope2;
                        channelsKt__Channels_commonKt$takeWhile$12.L$1 = channelIterator2;
                        channelsKt__Channels_commonKt$takeWhile$12.label = 1;
                        hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$takeWhile$12);
                        if (hasNext == obj4) {
                        }
                        return obj4;
                        return obj2;
                    }
                    return Unit.INSTANCE;
                }
                throw ((Failure) obj).exception;
            } else if (i == 4) {
                channelIterator2 = (ChannelIterator) this.L$2;
                Object obj7 = this.L$1;
                ProducerScope producerScope5 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    channelsKt__Channels_commonKt$takeWhile$12 = this;
                    ProducerScope producerScope6 = producerScope5;
                    obj4 = coroutine_suspended;
                    producerScope2 = producerScope6;
                    channelsKt__Channels_commonKt$takeWhile$12.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$takeWhile$12.L$1 = channelIterator2;
                    channelsKt__Channels_commonKt$takeWhile$12.label = 1;
                    hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$takeWhile$12);
                    if (hasNext == obj4) {
                        return obj4;
                    }
                    ProducerScope producerScope7 = producerScope2;
                    channelsKt__Channels_commonKt$takeWhile$13 = channelsKt__Channels_commonKt$takeWhile$12;
                    obj = hasNext;
                    obj5 = obj4;
                    producerScope3 = producerScope7;
                    if (!((Boolean) obj).booleanValue()) {
                        channelsKt__Channels_commonKt$takeWhile$13.L$0 = producerScope3;
                        channelsKt__Channels_commonKt$takeWhile$13.L$1 = channelIterator2;
                        channelsKt__Channels_commonKt$takeWhile$13.label = 2;
                        obj = channelIterator2.next(channelsKt__Channels_commonKt$takeWhile$13);
                        if (obj == obj5) {
                            return obj5;
                        }
                    }
                    return Unit.INSTANCE;
                    return obj4;
                }
                throw ((Failure) obj).exception;
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(obj instanceof Failure)) {
            ProducerScope producerScope8 = this.p$;
            channelIterator2 = this.$this_takeWhile.iterator();
            obj4 = coroutine_suspended;
            producerScope2 = producerScope8;
            channelsKt__Channels_commonKt$takeWhile$12 = this;
            channelsKt__Channels_commonKt$takeWhile$12.L$0 = producerScope2;
            channelsKt__Channels_commonKt$takeWhile$12.L$1 = channelIterator2;
            channelsKt__Channels_commonKt$takeWhile$12.label = 1;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$takeWhile$12);
            if (hasNext == obj4) {
            }
            return obj4;
        } else {
            throw ((Failure) obj).exception;
        }
        ProducerScope producerScope9 = producerScope3;
        Object obj8 = obj;
        ProducerScope producerScope10 = producerScope9;
        Function2 function2 = channelsKt__Channels_commonKt$takeWhile$13.$predicate;
        channelsKt__Channels_commonKt$takeWhile$13.L$0 = producerScope10;
        channelsKt__Channels_commonKt$takeWhile$13.L$1 = obj8;
        channelsKt__Channels_commonKt$takeWhile$13.L$2 = channelIterator2;
        channelsKt__Channels_commonKt$takeWhile$13.label = 3;
        Object invoke = function2.invoke(obj8, channelsKt__Channels_commonKt$takeWhile$13);
        if (invoke == obj5) {
            return obj5;
        }
        Object obj9 = obj8;
        producerScope = producerScope10;
        obj = invoke;
        obj2 = obj5;
        channelIterator = channelIterator2;
        channelsKt__Channels_commonKt$takeWhile$1 = channelsKt__Channels_commonKt$takeWhile$13;
        obj3 = obj9;
        if (!((Boolean) obj).booleanValue()) {
        }
        return Unit.INSTANCE;
        return obj5;
    }
}
