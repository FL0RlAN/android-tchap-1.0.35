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

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "E", "R", "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$mapIndexed$1", f = "Channels.common.kt", i = {0, 1, 2, 2, 3, 3}, l = {1220, 1220, 1221, 1221}, m = "invokeSuspend", n = {"index", "index", "index", "e", "index", "e"}, s = {"I$0", "I$0", "I$0", "L$1", "I$0", "L$1"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$mapIndexed$1 extends SuspendLambda implements Function2<ProducerScope<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ReceiveChannel $this_mapIndexed;
    final /* synthetic */ Function3 $transform;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;
    private ProducerScope p$;

    ChannelsKt__Channels_commonKt$mapIndexed$1(ReceiveChannel receiveChannel, Function3 function3, Continuation continuation) {
        this.$this_mapIndexed = receiveChannel;
        this.$transform = function3;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$mapIndexed$1 channelsKt__Channels_commonKt$mapIndexed$1 = new ChannelsKt__Channels_commonKt$mapIndexed$1(this.$this_mapIndexed, this.$transform, continuation);
        channelsKt__Channels_commonKt$mapIndexed$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$mapIndexed$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$mapIndexed$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a6 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00b6  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00f6  */
    public final Object invokeSuspend(Object obj) {
        Object obj2;
        ChannelIterator channelIterator;
        Object obj3;
        ProducerScope producerScope;
        int i;
        ProducerScope producerScope2;
        ChannelsKt__Channels_commonKt$mapIndexed$1 channelsKt__Channels_commonKt$mapIndexed$1;
        ChannelsKt__Channels_commonKt$mapIndexed$1 channelsKt__Channels_commonKt$mapIndexed$12;
        int i2;
        ChannelIterator channelIterator2;
        Object obj4;
        int i3;
        ChannelIterator channelIterator3;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i4 = this.label;
        if (i4 != 0) {
            if (i4 == 1) {
                channelIterator3 = (ChannelIterator) this.L$1;
                i3 = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj4 = coroutine_suspended;
                    channelsKt__Channels_commonKt$mapIndexed$1 = this;
                    if (!((Boolean) obj).booleanValue()) {
                    }
                    return Unit.INSTANCE;
                }
                throw ((Failure) obj).exception;
            } else if (i4 == 2) {
                channelIterator3 = (ChannelIterator) this.L$1;
                i3 = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj4 = coroutine_suspended;
                    channelsKt__Channels_commonKt$mapIndexed$1 = this;
                } else {
                    throw ((Failure) obj).exception;
                }
            } else if (i4 == 3) {
                producerScope2 = (ProducerScope) this.L$3;
                ChannelIterator channelIterator4 = (ChannelIterator) this.L$2;
                Object obj5 = this.L$1;
                int i5 = this.I$0;
                ProducerScope producerScope3 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$mapIndexed$1 = this;
                    ProducerScope producerScope4 = producerScope3;
                    channelIterator = channelIterator4;
                    i = i5;
                    obj3 = obj5;
                    producerScope = producerScope4;
                    channelsKt__Channels_commonKt$mapIndexed$1.L$0 = producerScope;
                    channelsKt__Channels_commonKt$mapIndexed$1.I$0 = i;
                    channelsKt__Channels_commonKt$mapIndexed$1.L$1 = obj3;
                    channelsKt__Channels_commonKt$mapIndexed$1.L$2 = channelIterator;
                    channelsKt__Channels_commonKt$mapIndexed$1.label = 4;
                    if (producerScope2.send(obj, channelsKt__Channels_commonKt$mapIndexed$1) != obj2) {
                        return obj2;
                    }
                    channelsKt__Channels_commonKt$mapIndexed$12 = channelsKt__Channels_commonKt$mapIndexed$1;
                    i2 = i;
                    channelIterator2 = channelIterator;
                    coroutine_suspended = obj2;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$mapIndexed$12.I$0 = i2;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$1 = channelIterator2;
                    channelsKt__Channels_commonKt$mapIndexed$12.label = 1;
                    hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$mapIndexed$12);
                    if (hasNext == coroutine_suspended) {
                    }
                    return coroutine_suspended;
                    return obj2;
                }
                throw ((Failure) obj).exception;
            } else if (i4 == 4) {
                ChannelIterator channelIterator5 = (ChannelIterator) this.L$2;
                Object obj6 = this.L$1;
                int i6 = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    channelsKt__Channels_commonKt$mapIndexed$12 = this;
                    int i7 = i6;
                    channelIterator2 = channelIterator5;
                    i2 = i7;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$mapIndexed$12.I$0 = i2;
                    channelsKt__Channels_commonKt$mapIndexed$12.L$1 = channelIterator2;
                    channelsKt__Channels_commonKt$mapIndexed$12.label = 1;
                    hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$mapIndexed$12);
                    if (hasNext == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    Object obj7 = coroutine_suspended;
                    channelsKt__Channels_commonKt$mapIndexed$1 = channelsKt__Channels_commonKt$mapIndexed$12;
                    obj = hasNext;
                    obj4 = obj7;
                    ChannelIterator channelIterator6 = channelIterator2;
                    i3 = i2;
                    channelIterator3 = channelIterator6;
                    if (!((Boolean) obj).booleanValue()) {
                        channelsKt__Channels_commonKt$mapIndexed$1.L$0 = producerScope;
                        channelsKt__Channels_commonKt$mapIndexed$1.I$0 = i3;
                        channelsKt__Channels_commonKt$mapIndexed$1.L$1 = channelIterator3;
                        channelsKt__Channels_commonKt$mapIndexed$1.label = 2;
                        obj = channelIterator3.next(channelsKt__Channels_commonKt$mapIndexed$1);
                        if (obj == obj4) {
                            return obj4;
                        }
                    }
                    return Unit.INSTANCE;
                    return coroutine_suspended;
                }
                throw ((Failure) obj).exception;
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(obj instanceof Failure)) {
            ProducerScope producerScope5 = this.p$;
            i2 = 0;
            channelIterator2 = this.$this_mapIndexed.iterator();
            producerScope = producerScope5;
            channelsKt__Channels_commonKt$mapIndexed$12 = this;
            channelsKt__Channels_commonKt$mapIndexed$12.L$0 = producerScope;
            channelsKt__Channels_commonKt$mapIndexed$12.I$0 = i2;
            channelsKt__Channels_commonKt$mapIndexed$12.L$1 = channelIterator2;
            channelsKt__Channels_commonKt$mapIndexed$12.label = 1;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$mapIndexed$12);
            if (hasNext == coroutine_suspended) {
            }
            return coroutine_suspended;
        } else {
            throw ((Failure) obj).exception;
        }
        ProducerScope producerScope6 = producerScope;
        Object obj8 = obj;
        ChannelIterator channelIterator7 = channelIterator3;
        producerScope2 = producerScope6;
        Function3 function3 = channelsKt__Channels_commonKt$mapIndexed$1.$transform;
        Integer boxInt = Boxing.boxInt(i3);
        i = i3 + 1;
        channelsKt__Channels_commonKt$mapIndexed$1.L$0 = producerScope2;
        channelsKt__Channels_commonKt$mapIndexed$1.I$0 = i;
        channelsKt__Channels_commonKt$mapIndexed$1.L$1 = obj8;
        channelsKt__Channels_commonKt$mapIndexed$1.L$2 = channelIterator7;
        channelsKt__Channels_commonKt$mapIndexed$1.L$3 = producerScope2;
        channelsKt__Channels_commonKt$mapIndexed$1.label = 3;
        Object invoke = function3.invoke(boxInt, obj8, channelsKt__Channels_commonKt$mapIndexed$1);
        if (invoke == obj4) {
            return obj4;
        }
        obj2 = obj4;
        obj3 = obj8;
        producerScope = producerScope2;
        Object obj9 = invoke;
        channelIterator = channelIterator7;
        obj = obj9;
        channelsKt__Channels_commonKt$mapIndexed$1.L$0 = producerScope;
        channelsKt__Channels_commonKt$mapIndexed$1.I$0 = i;
        channelsKt__Channels_commonKt$mapIndexed$1.L$1 = obj3;
        channelsKt__Channels_commonKt$mapIndexed$1.L$2 = channelIterator;
        channelsKt__Channels_commonKt$mapIndexed$1.label = 4;
        if (producerScope2.send(obj, channelsKt__Channels_commonKt$mapIndexed$1) != obj2) {
        }
        return obj2;
        return obj4;
    }
}
