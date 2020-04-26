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

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "E", "R", "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$flatMap$1", f = "Channels.common.kt", i = {2, 3}, l = {1105, 1105, 1106, 1106}, m = "invokeSuspend", n = {"e", "e"}, s = {"L$1", "L$1"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$flatMap$1 extends SuspendLambda implements Function2<ProducerScope<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ReceiveChannel $this_flatMap;
    final /* synthetic */ Function2 $transform;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    ChannelsKt__Channels_commonKt$flatMap$1(ReceiveChannel receiveChannel, Function2 function2, Continuation continuation) {
        this.$this_flatMap = receiveChannel;
        this.$transform = function2;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$flatMap$1 channelsKt__Channels_commonKt$flatMap$1 = new ChannelsKt__Channels_commonKt$flatMap$1(this.$this_flatMap, this.$transform, continuation);
        channelsKt__Channels_commonKt$flatMap$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$flatMap$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$flatMap$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0096 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a4  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00dc  */
    public final Object invokeSuspend(Object obj) {
        Object obj2;
        ChannelIterator channelIterator;
        Object obj3;
        Object obj4;
        ChannelsKt__Channels_commonKt$flatMap$1 channelsKt__Channels_commonKt$flatMap$1;
        ReceiveChannel receiveChannel;
        SendChannel sendChannel;
        ChannelsKt__Channels_commonKt$flatMap$1 channelsKt__Channels_commonKt$flatMap$12;
        Object obj5;
        ChannelIterator channelIterator2;
        Object obj6;
        Object obj7;
        Object obj8;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i != 0) {
            if (i == 1) {
                channelIterator2 = (ChannelIterator) this.L$1;
                obj8 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj7 = coroutine_suspended;
                    channelsKt__Channels_commonKt$flatMap$1 = this;
                    if (!((Boolean) obj).booleanValue()) {
                    }
                    return Unit.INSTANCE;
                }
                throw ((Failure) obj).exception;
            } else if (i == 2) {
                channelIterator2 = (ChannelIterator) this.L$1;
                obj8 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj7 = coroutine_suspended;
                    channelsKt__Channels_commonKt$flatMap$1 = this;
                } else {
                    throw ((Failure) obj).exception;
                }
            } else if (i == 3) {
                ChannelIterator channelIterator3 = (ChannelIterator) this.L$2;
                Object obj9 = this.L$1;
                ProducerScope producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$flatMap$1 = this;
                    Object obj10 = producerScope;
                    channelIterator = channelIterator3;
                    obj4 = obj9;
                    obj3 = obj10;
                    receiveChannel = (ReceiveChannel) obj;
                    sendChannel = (SendChannel) obj3;
                    channelsKt__Channels_commonKt$flatMap$1.L$0 = obj3;
                    channelsKt__Channels_commonKt$flatMap$1.L$1 = obj4;
                    channelsKt__Channels_commonKt$flatMap$1.L$2 = channelIterator;
                    channelsKt__Channels_commonKt$flatMap$1.label = 4;
                    if (ChannelsKt.toChannel(receiveChannel, sendChannel, channelsKt__Channels_commonKt$flatMap$1) != obj2) {
                        return obj2;
                    }
                    channelsKt__Channels_commonKt$flatMap$12 = channelsKt__Channels_commonKt$flatMap$1;
                    obj5 = obj3;
                    channelIterator2 = channelIterator;
                    obj6 = obj2;
                    channelsKt__Channels_commonKt$flatMap$12.L$0 = obj5;
                    channelsKt__Channels_commonKt$flatMap$12.L$1 = channelIterator2;
                    channelsKt__Channels_commonKt$flatMap$12.label = 1;
                    hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$flatMap$12);
                    if (hasNext == obj6) {
                    }
                    return obj6;
                    return obj2;
                }
                throw ((Failure) obj).exception;
            } else if (i == 4) {
                channelIterator2 = (ChannelIterator) this.L$2;
                Object obj11 = this.L$1;
                ProducerScope producerScope2 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    channelsKt__Channels_commonKt$flatMap$12 = this;
                    Object obj12 = producerScope2;
                    obj6 = coroutine_suspended;
                    obj5 = obj12;
                    channelsKt__Channels_commonKt$flatMap$12.L$0 = obj5;
                    channelsKt__Channels_commonKt$flatMap$12.L$1 = channelIterator2;
                    channelsKt__Channels_commonKt$flatMap$12.label = 1;
                    hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$flatMap$12);
                    if (hasNext == obj6) {
                        return obj6;
                    }
                    Object obj13 = obj5;
                    channelsKt__Channels_commonKt$flatMap$1 = channelsKt__Channels_commonKt$flatMap$12;
                    obj = hasNext;
                    obj7 = obj6;
                    obj8 = obj13;
                    if (!((Boolean) obj).booleanValue()) {
                        channelsKt__Channels_commonKt$flatMap$1.L$0 = obj8;
                        channelsKt__Channels_commonKt$flatMap$1.L$1 = channelIterator2;
                        channelsKt__Channels_commonKt$flatMap$1.label = 2;
                        obj = channelIterator2.next(channelsKt__Channels_commonKt$flatMap$1);
                        if (obj == obj7) {
                            return obj7;
                        }
                    }
                    return Unit.INSTANCE;
                    return obj6;
                }
                throw ((Failure) obj).exception;
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(obj instanceof Failure)) {
            ProducerScope producerScope3 = this.p$;
            channelIterator2 = this.$this_flatMap.iterator();
            obj6 = coroutine_suspended;
            obj5 = producerScope3;
            channelsKt__Channels_commonKt$flatMap$12 = this;
            channelsKt__Channels_commonKt$flatMap$12.L$0 = obj5;
            channelsKt__Channels_commonKt$flatMap$12.L$1 = channelIterator2;
            channelsKt__Channels_commonKt$flatMap$12.label = 1;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$flatMap$12);
            if (hasNext == obj6) {
            }
            return obj6;
        } else {
            throw ((Failure) obj).exception;
        }
        Object obj14 = obj8;
        Object obj15 = obj;
        Object obj16 = obj14;
        Function2 function2 = channelsKt__Channels_commonKt$flatMap$1.$transform;
        channelsKt__Channels_commonKt$flatMap$1.L$0 = obj16;
        channelsKt__Channels_commonKt$flatMap$1.L$1 = obj15;
        channelsKt__Channels_commonKt$flatMap$1.L$2 = channelIterator2;
        channelsKt__Channels_commonKt$flatMap$1.label = 3;
        Object invoke = function2.invoke(obj15, channelsKt__Channels_commonKt$flatMap$1);
        if (invoke == obj7) {
            return obj7;
        }
        Object obj17 = obj15;
        obj3 = obj16;
        obj = invoke;
        obj2 = obj7;
        channelIterator = channelIterator2;
        obj4 = obj17;
        receiveChannel = (ReceiveChannel) obj;
        sendChannel = (SendChannel) obj3;
        channelsKt__Channels_commonKt$flatMap$1.L$0 = obj3;
        channelsKt__Channels_commonKt$flatMap$1.L$1 = obj4;
        channelsKt__Channels_commonKt$flatMap$1.L$2 = channelIterator;
        channelsKt__Channels_commonKt$flatMap$1.label = 4;
        if (ChannelsKt.toChannel(receiveChannel, sendChannel, channelsKt__Channels_commonKt$flatMap$1) != obj2) {
        }
        return obj2;
        return obj7;
    }
}
