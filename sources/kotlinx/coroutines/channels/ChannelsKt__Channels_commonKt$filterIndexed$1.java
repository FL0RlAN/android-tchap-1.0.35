package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.Result.Failure;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H@ø\u0001\u0000¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$filterIndexed$1", f = "Channels.common.kt", i = {0, 1, 2, 2, 3, 3}, l = {660, 660, 661, 661}, m = "invokeSuspend", n = {"index", "index", "index", "e", "index", "e"}, s = {"I$0", "I$0", "I$0", "L$1", "I$0", "L$1"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$filterIndexed$1 extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function3 $predicate;
    final /* synthetic */ ReceiveChannel $this_filterIndexed;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    ChannelsKt__Channels_commonKt$filterIndexed$1(ReceiveChannel receiveChannel, Function3 function3, Continuation continuation) {
        this.$this_filterIndexed = receiveChannel;
        this.$predicate = function3;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$filterIndexed$1 channelsKt__Channels_commonKt$filterIndexed$1 = new ChannelsKt__Channels_commonKt$filterIndexed$1(this.$this_filterIndexed, this.$predicate, continuation);
        channelsKt__Channels_commonKt$filterIndexed$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$filterIndexed$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$filterIndexed$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a4 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00e9  */
    public final Object invokeSuspend(Object obj) {
        Object obj2;
        Object obj3;
        ProducerScope producerScope;
        ChannelIterator channelIterator;
        int i;
        ChannelsKt__Channels_commonKt$filterIndexed$1 channelsKt__Channels_commonKt$filterIndexed$1;
        ChannelsKt__Channels_commonKt$filterIndexed$1 channelsKt__Channels_commonKt$filterIndexed$12;
        ProducerScope producerScope2;
        Object obj4;
        int i2;
        ChannelIterator channelIterator2;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i3 = this.label;
        if (i3 != 0) {
            if (i3 == 1) {
                channelIterator2 = (ChannelIterator) this.L$1;
                i2 = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj3 = coroutine_suspended;
                    channelsKt__Channels_commonKt$filterIndexed$1 = this;
                    if (!((Boolean) obj).booleanValue()) {
                    }
                    return Unit.INSTANCE;
                }
                throw ((Failure) obj).exception;
            } else if (i3 == 2) {
                channelIterator2 = (ChannelIterator) this.L$1;
                i2 = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj3 = coroutine_suspended;
                    channelsKt__Channels_commonKt$filterIndexed$1 = this;
                } else {
                    throw ((Failure) obj).exception;
                }
            } else if (i3 == 3) {
                ChannelIterator channelIterator3 = (ChannelIterator) this.L$2;
                Object obj5 = this.L$1;
                int i4 = this.I$0;
                ProducerScope producerScope3 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = obj5;
                    channelIterator = channelIterator3;
                    i = i4;
                    producerScope = producerScope3;
                    obj3 = coroutine_suspended;
                    channelsKt__Channels_commonKt$filterIndexed$1 = this;
                    if (((Boolean) obj).booleanValue()) {
                        channelsKt__Channels_commonKt$filterIndexed$1.L$0 = producerScope;
                        channelsKt__Channels_commonKt$filterIndexed$1.I$0 = i;
                        channelsKt__Channels_commonKt$filterIndexed$1.L$1 = obj2;
                        channelsKt__Channels_commonKt$filterIndexed$1.L$2 = channelIterator;
                        channelsKt__Channels_commonKt$filterIndexed$1.label = 4;
                        if (producerScope.send(obj2, channelsKt__Channels_commonKt$filterIndexed$1) == obj3) {
                            return obj3;
                        }
                    }
                    channelsKt__Channels_commonKt$filterIndexed$12 = channelsKt__Channels_commonKt$filterIndexed$1;
                    producerScope2 = producerScope;
                    obj4 = obj3;
                    channelsKt__Channels_commonKt$filterIndexed$12.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$filterIndexed$12.I$0 = i;
                    channelsKt__Channels_commonKt$filterIndexed$12.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$filterIndexed$12.label = 1;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filterIndexed$12);
                    if (hasNext != obj4) {
                    }
                    return obj4;
                }
                throw ((Failure) obj).exception;
            } else if (i3 == 4) {
                ChannelIterator channelIterator4 = (ChannelIterator) this.L$2;
                Object obj6 = this.L$1;
                int i5 = this.I$0;
                ProducerScope producerScope4 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    channelsKt__Channels_commonKt$filterIndexed$12 = this;
                    ProducerScope producerScope5 = producerScope4;
                    obj4 = coroutine_suspended;
                    producerScope2 = producerScope5;
                    int i6 = i5;
                    channelIterator = channelIterator4;
                    i = i6;
                    channelsKt__Channels_commonKt$filterIndexed$12.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$filterIndexed$12.I$0 = i;
                    channelsKt__Channels_commonKt$filterIndexed$12.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$filterIndexed$12.label = 1;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filterIndexed$12);
                    if (hasNext != obj4) {
                        return obj4;
                    }
                    ProducerScope producerScope6 = producerScope2;
                    channelsKt__Channels_commonKt$filterIndexed$1 = channelsKt__Channels_commonKt$filterIndexed$12;
                    obj = hasNext;
                    obj3 = obj4;
                    producerScope = producerScope6;
                    ChannelIterator channelIterator5 = channelIterator;
                    i2 = i;
                    channelIterator2 = channelIterator5;
                    if (!((Boolean) obj).booleanValue()) {
                        channelsKt__Channels_commonKt$filterIndexed$1.L$0 = producerScope;
                        channelsKt__Channels_commonKt$filterIndexed$1.I$0 = i2;
                        channelsKt__Channels_commonKt$filterIndexed$1.L$1 = channelIterator2;
                        channelsKt__Channels_commonKt$filterIndexed$1.label = 2;
                        obj = channelIterator2.next(channelsKt__Channels_commonKt$filterIndexed$1);
                        if (obj == obj3) {
                            return obj3;
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
            ProducerScope producerScope7 = this.p$;
            i = 0;
            channelIterator = this.$this_filterIndexed.iterator();
            obj4 = coroutine_suspended;
            producerScope2 = producerScope7;
            channelsKt__Channels_commonKt$filterIndexed$12 = this;
            channelsKt__Channels_commonKt$filterIndexed$12.L$0 = producerScope2;
            channelsKt__Channels_commonKt$filterIndexed$12.I$0 = i;
            channelsKt__Channels_commonKt$filterIndexed$12.L$1 = channelIterator;
            channelsKt__Channels_commonKt$filterIndexed$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filterIndexed$12);
            if (hasNext != obj4) {
            }
            return obj4;
        } else {
            throw ((Failure) obj).exception;
        }
        int i7 = i2;
        Object obj7 = obj;
        int i8 = i7;
        Function3 function3 = channelsKt__Channels_commonKt$filterIndexed$1.$predicate;
        Integer boxInt = Boxing.boxInt(i8);
        int i9 = i8 + 1;
        channelsKt__Channels_commonKt$filterIndexed$1.L$0 = producerScope;
        channelsKt__Channels_commonKt$filterIndexed$1.I$0 = i9;
        channelsKt__Channels_commonKt$filterIndexed$1.L$1 = obj7;
        channelsKt__Channels_commonKt$filterIndexed$1.L$2 = channelIterator2;
        channelsKt__Channels_commonKt$filterIndexed$1.label = 3;
        Object invoke = function3.invoke(boxInt, obj7, channelsKt__Channels_commonKt$filterIndexed$1);
        if (invoke == obj3) {
            return obj3;
        }
        ChannelIterator channelIterator6 = channelIterator2;
        i = i9;
        obj = invoke;
        obj2 = obj7;
        channelIterator = channelIterator6;
        if (((Boolean) obj).booleanValue()) {
        }
        channelsKt__Channels_commonKt$filterIndexed$12 = channelsKt__Channels_commonKt$filterIndexed$1;
        producerScope2 = producerScope;
        obj4 = obj3;
        channelsKt__Channels_commonKt$filterIndexed$12.L$0 = producerScope2;
        channelsKt__Channels_commonKt$filterIndexed$12.I$0 = i;
        channelsKt__Channels_commonKt$filterIndexed$12.L$1 = channelIterator;
        channelsKt__Channels_commonKt$filterIndexed$12.label = 1;
        hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$filterIndexed$12);
        if (hasNext != obj4) {
        }
        return obj4;
        return obj3;
    }
}
