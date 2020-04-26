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
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$drop$1", f = "Channels.common.kt", i = {0, 1, 2, 3, 4, 4}, l = {592, 592, 597, 597, 598}, m = "invokeSuspend", n = {"remaining", "remaining", "remaining", "remaining", "remaining", "e"}, s = {"I$0", "I$0", "I$0", "I$0", "I$0", "L$1"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$drop$1 extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
    final /* synthetic */ int $n;
    final /* synthetic */ ReceiveChannel $this_drop;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    ChannelsKt__Channels_commonKt$drop$1(ReceiveChannel receiveChannel, int i, Continuation continuation) {
        this.$this_drop = receiveChannel;
        this.$n = i;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$drop$1 channelsKt__Channels_commonKt$drop$1 = new ChannelsKt__Channels_commonKt$drop$1(this.$this_drop, this.$n, continuation);
        channelsKt__Channels_commonKt$drop$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$drop$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$drop$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x00cf  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00e6  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0109 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x010a  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0116  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0136  */
    public final Object invokeSuspend(Object obj) {
        Object obj2;
        ProducerScope producerScope;
        int i;
        ChannelIterator channelIterator;
        ChannelsKt__Channels_commonKt$drop$1 channelsKt__Channels_commonKt$drop$1;
        ChannelsKt__Channels_commonKt$drop$1 channelsKt__Channels_commonKt$drop$12;
        Object hasNext;
        Object obj3;
        int i2;
        ProducerScope producerScope2;
        ChannelIterator channelIterator2;
        ChannelIterator channelIterator3;
        int i3;
        Object obj4;
        ProducerScope producerScope3;
        ChannelsKt__Channels_commonKt$drop$1 channelsKt__Channels_commonKt$drop$13;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i4 = this.label;
        if (i4 != 0) {
            if (i4 == 1) {
                channelIterator2 = (ChannelIterator) this.L$1;
                i2 = this.I$0;
                producerScope3 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj4 = coroutine_suspended;
                    channelsKt__Channels_commonKt$drop$13 = this;
                    if (!((Boolean) obj).booleanValue()) {
                        channelsKt__Channels_commonKt$drop$13.L$0 = producerScope3;
                        channelsKt__Channels_commonKt$drop$13.I$0 = i2;
                        channelsKt__Channels_commonKt$drop$13.L$1 = channelIterator2;
                        channelsKt__Channels_commonKt$drop$13.label = 2;
                        if (channelIterator2.next(channelsKt__Channels_commonKt$drop$13) == obj4) {
                            return obj4;
                        }
                        channelsKt__Channels_commonKt$drop$12 = channelsKt__Channels_commonKt$drop$13;
                        producerScope2 = producerScope3;
                        obj3 = obj4;
                        i2--;
                        if (i2 != 0) {
                        }
                        channelIterator = channelsKt__Channels_commonKt$drop$12.$this_drop.iterator();
                        producerScope = producerScope2;
                        i = i2;
                        coroutine_suspended = obj3;
                        channelsKt__Channels_commonKt$drop$12.L$0 = producerScope;
                        channelsKt__Channels_commonKt$drop$12.I$0 = i;
                        channelsKt__Channels_commonKt$drop$12.L$1 = channelIterator;
                        channelsKt__Channels_commonKt$drop$12.label = 3;
                        hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$drop$12);
                        if (hasNext == coroutine_suspended) {
                        }
                        return coroutine_suspended;
                        return obj4;
                    }
                    channelsKt__Channels_commonKt$drop$12 = channelsKt__Channels_commonKt$drop$13;
                    producerScope2 = producerScope3;
                    obj3 = obj4;
                    channelIterator = channelsKt__Channels_commonKt$drop$12.$this_drop.iterator();
                    producerScope = producerScope2;
                    i = i2;
                    coroutine_suspended = obj3;
                    channelsKt__Channels_commonKt$drop$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$drop$12.I$0 = i;
                    channelsKt__Channels_commonKt$drop$12.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$drop$12.label = 3;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$drop$12);
                    if (hasNext == coroutine_suspended) {
                    }
                    return coroutine_suspended;
                }
                throw ((Failure) obj).exception;
            } else if (i4 == 2) {
                channelIterator2 = (ChannelIterator) this.L$1;
                i2 = this.I$0;
                ProducerScope producerScope4 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    channelsKt__Channels_commonKt$drop$12 = this;
                    ProducerScope producerScope5 = producerScope4;
                    obj3 = coroutine_suspended;
                    producerScope2 = producerScope5;
                    i2--;
                    if (i2 != 0) {
                        int i5 = i2;
                        channelIterator3 = channelIterator2;
                        i3 = i5;
                    }
                    channelIterator = channelsKt__Channels_commonKt$drop$12.$this_drop.iterator();
                    producerScope = producerScope2;
                    i = i2;
                    coroutine_suspended = obj3;
                    channelsKt__Channels_commonKt$drop$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$drop$12.I$0 = i;
                    channelsKt__Channels_commonKt$drop$12.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$drop$12.label = 3;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$drop$12);
                    if (hasNext == coroutine_suspended) {
                    }
                    return coroutine_suspended;
                }
                throw ((Failure) obj).exception;
            } else if (i4 == 3) {
                channelIterator = (ChannelIterator) this.L$1;
                i = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$drop$1 = this;
                    if (!((Boolean) obj).booleanValue()) {
                    }
                    return Unit.INSTANCE;
                }
                throw ((Failure) obj).exception;
            } else if (i4 == 4) {
                channelIterator = (ChannelIterator) this.L$1;
                i = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$drop$1 = this;
                    channelsKt__Channels_commonKt$drop$1.L$0 = producerScope;
                    channelsKt__Channels_commonKt$drop$1.I$0 = i;
                    channelsKt__Channels_commonKt$drop$1.L$1 = obj;
                    channelsKt__Channels_commonKt$drop$1.L$2 = channelIterator;
                    channelsKt__Channels_commonKt$drop$1.label = 5;
                    if (producerScope.send(obj, channelsKt__Channels_commonKt$drop$1) != obj2) {
                    }
                    return obj2;
                }
                throw ((Failure) obj).exception;
            } else if (i4 == 5) {
                channelIterator = (ChannelIterator) this.L$2;
                Object obj5 = this.L$1;
                i = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    channelsKt__Channels_commonKt$drop$12 = this;
                    channelsKt__Channels_commonKt$drop$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$drop$12.I$0 = i;
                    channelsKt__Channels_commonKt$drop$12.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$drop$12.label = 3;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$drop$12);
                    if (hasNext == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    Object obj6 = coroutine_suspended;
                    channelsKt__Channels_commonKt$drop$1 = channelsKt__Channels_commonKt$drop$12;
                    obj = hasNext;
                    obj2 = obj6;
                    if (!((Boolean) obj).booleanValue()) {
                        channelsKt__Channels_commonKt$drop$1.L$0 = producerScope;
                        channelsKt__Channels_commonKt$drop$1.I$0 = i;
                        channelsKt__Channels_commonKt$drop$1.L$1 = channelIterator;
                        channelsKt__Channels_commonKt$drop$1.label = 4;
                        obj = channelIterator.next(channelsKt__Channels_commonKt$drop$1);
                        if (obj == obj2) {
                            return obj2;
                        }
                        channelsKt__Channels_commonKt$drop$1.L$0 = producerScope;
                        channelsKt__Channels_commonKt$drop$1.I$0 = i;
                        channelsKt__Channels_commonKt$drop$1.L$1 = obj;
                        channelsKt__Channels_commonKt$drop$1.L$2 = channelIterator;
                        channelsKt__Channels_commonKt$drop$1.label = 5;
                        if (producerScope.send(obj, channelsKt__Channels_commonKt$drop$1) != obj2) {
                            return obj2;
                        }
                        channelsKt__Channels_commonKt$drop$12 = channelsKt__Channels_commonKt$drop$1;
                        coroutine_suspended = obj2;
                        channelsKt__Channels_commonKt$drop$12.L$0 = producerScope;
                        channelsKt__Channels_commonKt$drop$12.I$0 = i;
                        channelsKt__Channels_commonKt$drop$12.L$1 = channelIterator;
                        channelsKt__Channels_commonKt$drop$12.label = 3;
                        hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$drop$12);
                        if (hasNext == coroutine_suspended) {
                        }
                        return obj2;
                    }
                    return Unit.INSTANCE;
                    return coroutine_suspended;
                }
                throw ((Failure) obj).exception;
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(obj instanceof Failure)) {
            ProducerScope producerScope6 = this.p$;
            if (this.$n >= 0) {
                i3 = this.$n;
                if (i3 > 0) {
                    channelIterator3 = this.$this_drop.iterator();
                    obj3 = coroutine_suspended;
                    producerScope2 = producerScope6;
                    channelsKt__Channels_commonKt$drop$12 = this;
                } else {
                    obj3 = coroutine_suspended;
                    i2 = i3;
                    producerScope2 = producerScope6;
                    channelsKt__Channels_commonKt$drop$12 = this;
                    channelIterator = channelsKt__Channels_commonKt$drop$12.$this_drop.iterator();
                    producerScope = producerScope2;
                    i = i2;
                    coroutine_suspended = obj3;
                    channelsKt__Channels_commonKt$drop$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$drop$12.I$0 = i;
                    channelsKt__Channels_commonKt$drop$12.L$1 = channelIterator;
                    channelsKt__Channels_commonKt$drop$12.label = 3;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$drop$12);
                    if (hasNext == coroutine_suspended) {
                    }
                    return coroutine_suspended;
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Requested element count ");
                sb.append(this.$n);
                sb.append(" is less than zero.");
                throw new IllegalArgumentException(sb.toString().toString());
            }
        } else {
            throw ((Failure) obj).exception;
        }
        channelsKt__Channels_commonKt$drop$12.L$0 = producerScope2;
        channelsKt__Channels_commonKt$drop$12.I$0 = i3;
        channelsKt__Channels_commonKt$drop$12.L$1 = channelIterator3;
        channelsKt__Channels_commonKt$drop$12.label = 1;
        Object hasNext2 = channelIterator3.hasNext(channelsKt__Channels_commonKt$drop$12);
        if (hasNext2 == obj3) {
            return obj3;
        }
        ProducerScope producerScope7 = producerScope2;
        channelsKt__Channels_commonKt$drop$13 = channelsKt__Channels_commonKt$drop$12;
        obj = hasNext2;
        obj4 = obj3;
        producerScope3 = producerScope7;
        ChannelIterator channelIterator4 = channelIterator3;
        i2 = i3;
        channelIterator2 = channelIterator4;
        if (!((Boolean) obj).booleanValue()) {
            channelsKt__Channels_commonKt$drop$12 = channelsKt__Channels_commonKt$drop$13;
            producerScope2 = producerScope3;
            obj3 = obj4;
        }
        channelsKt__Channels_commonKt$drop$12 = channelsKt__Channels_commonKt$drop$13;
        producerScope2 = producerScope3;
        obj3 = obj4;
        channelIterator = channelsKt__Channels_commonKt$drop$12.$this_drop.iterator();
        producerScope = producerScope2;
        i = i2;
        coroutine_suspended = obj3;
        channelsKt__Channels_commonKt$drop$12.L$0 = producerScope;
        channelsKt__Channels_commonKt$drop$12.I$0 = i;
        channelsKt__Channels_commonKt$drop$12.L$1 = channelIterator;
        channelsKt__Channels_commonKt$drop$12.label = 3;
        hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$drop$12);
        if (hasNext == coroutine_suspended) {
        }
        return coroutine_suspended;
        return obj3;
    }
}
