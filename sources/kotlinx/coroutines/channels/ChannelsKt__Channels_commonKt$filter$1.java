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
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$filter$1", f = "Channels.common.kt", i = {2, 3}, l = {639, 639, 640, 640}, m = "invokeSuspend", n = {"e", "e"}, s = {"L$1", "L$1"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$filter$1 extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2 $predicate;
    final /* synthetic */ ReceiveChannel $this_filter;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    ChannelsKt__Channels_commonKt$filter$1(ReceiveChannel receiveChannel, Function2 function2, Continuation continuation) {
        this.$this_filter = receiveChannel;
        this.$predicate = function2;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$filter$1 channelsKt__Channels_commonKt$filter$1 = new ChannelsKt__Channels_commonKt$filter$1(this.$this_filter, this.$predicate, continuation);
        channelsKt__Channels_commonKt$filter$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$filter$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$filter$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0093 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0094  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a1  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00cc  */
    public final Object invokeSuspend(Object obj) {
        Object obj2;
        ProducerScope producerScope;
        Object obj3;
        ChannelIterator channelIterator;
        ChannelsKt__Channels_commonKt$filter$1 channelsKt__Channels_commonKt$filter$1;
        ChannelsKt__Channels_commonKt$filter$1 channelsKt__Channels_commonKt$filter$12;
        ProducerScope producerScope2;
        Object obj4;
        ProducerScope producerScope3;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i != 0) {
            if (i == 1) {
                channelIterator = (ChannelIterator) this.L$1;
                producerScope3 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj4 = coroutine_suspended;
                    channelsKt__Channels_commonKt$filter$1 = this;
                    if (!((Boolean) obj).booleanValue()) {
                    }
                    return Unit.INSTANCE;
                }
                throw ((Failure) obj).exception;
            } else if (i == 2) {
                channelIterator = (ChannelIterator) this.L$1;
                producerScope3 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj4 = coroutine_suspended;
                    channelsKt__Channels_commonKt$filter$1 = this;
                } else {
                    throw ((Failure) obj).exception;
                }
            } else if (i == 3) {
                channelIterator = (ChannelIterator) this.L$2;
                Object obj5 = this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = obj5;
                    obj3 = coroutine_suspended;
                    channelsKt__Channels_commonKt$filter$1 = this;
                    if (((Boolean) obj).booleanValue()) {
                        channelsKt__Channels_commonKt$filter$1.L$0 = producerScope;
                        channelsKt__Channels_commonKt$filter$1.L$1 = obj2;
                        channelsKt__Channels_commonKt$filter$1.L$2 = channelIterator;
                        channelsKt__Channels_commonKt$filter$1.label = 4;
                        if (producerScope.send(obj2, channelsKt__Channels_commonKt$filter$1) == obj3) {
                            return obj3;
                        }
                    }
                    channelsKt__Channels_commonKt$filter$12 = channelsKt__Channels_commonKt$filter$1;
                    producerScope2 = producerScope;
                    channelsKt__Channels_commonKt$filter$12.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$filter$12.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$filter$12.label = 1;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filter$12);
                    if (hasNext != obj3) {
                    }
                    return obj3;
                }
                throw ((Failure) obj).exception;
            } else if (i == 4) {
                channelIterator = (ChannelIterator) this.L$2;
                Object obj6 = this.L$1;
                ProducerScope producerScope4 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    channelsKt__Channels_commonKt$filter$12 = this;
                    ProducerScope producerScope5 = producerScope4;
                    obj3 = coroutine_suspended;
                    producerScope2 = producerScope5;
                    channelsKt__Channels_commonKt$filter$12.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$filter$12.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$filter$12.label = 1;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filter$12);
                    if (hasNext != obj3) {
                        return obj3;
                    }
                    ProducerScope producerScope6 = producerScope2;
                    channelsKt__Channels_commonKt$filter$1 = channelsKt__Channels_commonKt$filter$12;
                    obj = hasNext;
                    obj4 = obj3;
                    producerScope3 = producerScope6;
                    if (!((Boolean) obj).booleanValue()) {
                        channelsKt__Channels_commonKt$filter$1.L$0 = producerScope3;
                        channelsKt__Channels_commonKt$filter$1.L$1 = channelIterator;
                        channelsKt__Channels_commonKt$filter$1.label = 2;
                        obj = channelIterator.next(channelsKt__Channels_commonKt$filter$1);
                        if (obj == obj4) {
                            return obj4;
                        }
                    }
                    return Unit.INSTANCE;
                    return obj3;
                }
                throw ((Failure) obj).exception;
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(obj instanceof Failure)) {
            ProducerScope producerScope7 = this.p$;
            channelIterator = this.$this_filter.iterator();
            obj3 = coroutine_suspended;
            producerScope2 = producerScope7;
            channelsKt__Channels_commonKt$filter$12 = this;
            channelsKt__Channels_commonKt$filter$12.L$0 = producerScope2;
            channelsKt__Channels_commonKt$filter$12.L$1 = channelIterator;
            channelsKt__Channels_commonKt$filter$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filter$12);
            if (hasNext != obj3) {
            }
            return obj3;
        } else {
            throw ((Failure) obj).exception;
        }
        ProducerScope producerScope8 = producerScope3;
        Object obj7 = obj;
        ProducerScope producerScope9 = producerScope8;
        Function2 function2 = channelsKt__Channels_commonKt$filter$1.$predicate;
        channelsKt__Channels_commonKt$filter$1.L$0 = producerScope9;
        channelsKt__Channels_commonKt$filter$1.L$1 = obj7;
        channelsKt__Channels_commonKt$filter$1.L$2 = channelIterator;
        channelsKt__Channels_commonKt$filter$1.label = 3;
        Object invoke = function2.invoke(obj7, channelsKt__Channels_commonKt$filter$1);
        if (invoke == obj4) {
            return obj4;
        }
        Object obj8 = obj4;
        producerScope = producerScope9;
        obj = invoke;
        obj2 = obj7;
        obj3 = obj8;
        if (((Boolean) obj).booleanValue()) {
        }
        channelsKt__Channels_commonKt$filter$12 = channelsKt__Channels_commonKt$filter$1;
        producerScope2 = producerScope;
        channelsKt__Channels_commonKt$filter$12.L$0 = producerScope2;
        channelsKt__Channels_commonKt$filter$12.L$1 = channelIterator;
        channelsKt__Channels_commonKt$filter$12.label = 1;
        hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filter$12);
        if (hasNext != obj3) {
        }
        return obj3;
        return obj4;
    }
}
