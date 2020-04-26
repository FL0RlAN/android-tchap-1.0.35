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
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$take$1", f = "Channels.common.kt", i = {0, 1, 2, 2}, l = {848, 848, 849}, m = "invokeSuspend", n = {"remaining", "remaining", "remaining", "e"}, s = {"I$0", "I$0", "I$0", "L$1"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$take$1 extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
    final /* synthetic */ int $n;
    final /* synthetic */ ReceiveChannel $this_take;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    ChannelsKt__Channels_commonKt$take$1(ReceiveChannel receiveChannel, int i, Continuation continuation) {
        this.$this_take = receiveChannel;
        this.$n = i;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$take$1 channelsKt__Channels_commonKt$take$1 = new ChannelsKt__Channels_commonKt$take$1(this.$this_take, this.$n, continuation);
        channelsKt__Channels_commonKt$take$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$take$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$take$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ba  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00c0  */
    public final Object invokeSuspend(Object obj) {
        ChannelsKt__Channels_commonKt$take$1 channelsKt__Channels_commonKt$take$1;
        ProducerScope producerScope;
        int i;
        ChannelIterator channelIterator;
        Object obj2;
        ChannelsKt__Channels_commonKt$take$1 channelsKt__Channels_commonKt$take$12;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = this.label;
        if (i2 != 0) {
            if (i2 == 1) {
                channelIterator = (ChannelIterator) this.L$1;
                i = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$take$12 = this;
                    if (!((Boolean) obj).booleanValue()) {
                        channelsKt__Channels_commonKt$take$12.L$0 = producerScope;
                        channelsKt__Channels_commonKt$take$12.I$0 = i;
                        channelsKt__Channels_commonKt$take$12.L$1 = channelIterator;
                        channelsKt__Channels_commonKt$take$12.label = 2;
                        obj = channelIterator.next(channelsKt__Channels_commonKt$take$12);
                        if (obj == obj2) {
                            return obj2;
                        }
                        channelsKt__Channels_commonKt$take$12.L$0 = producerScope;
                        channelsKt__Channels_commonKt$take$12.I$0 = i;
                        channelsKt__Channels_commonKt$take$12.L$1 = obj;
                        channelsKt__Channels_commonKt$take$12.L$2 = channelIterator;
                        channelsKt__Channels_commonKt$take$12.label = 3;
                        if (producerScope.send(obj, channelsKt__Channels_commonKt$take$12) != obj2) {
                        }
                        return obj2;
                    }
                    return Unit.INSTANCE;
                }
                throw ((Failure) obj).exception;
            } else if (i2 == 2) {
                channelIterator = (ChannelIterator) this.L$1;
                i = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$take$12 = this;
                    channelsKt__Channels_commonKt$take$12.L$0 = producerScope;
                    channelsKt__Channels_commonKt$take$12.I$0 = i;
                    channelsKt__Channels_commonKt$take$12.L$1 = obj;
                    channelsKt__Channels_commonKt$take$12.L$2 = channelIterator;
                    channelsKt__Channels_commonKt$take$12.label = 3;
                    if (producerScope.send(obj, channelsKt__Channels_commonKt$take$12) != obj2) {
                        return obj2;
                    }
                    channelsKt__Channels_commonKt$take$1 = channelsKt__Channels_commonKt$take$12;
                    coroutine_suspended = obj2;
                    i--;
                    if (i == 0) {
                    }
                    return obj2;
                }
                throw ((Failure) obj).exception;
            } else if (i2 == 3) {
                channelIterator = (ChannelIterator) this.L$2;
                Object obj3 = this.L$1;
                i = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    channelsKt__Channels_commonKt$take$1 = this;
                    i--;
                    if (i == 0) {
                        return Unit.INSTANCE;
                    }
                } else {
                    throw ((Failure) obj).exception;
                }
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(obj instanceof Failure)) {
            ProducerScope producerScope2 = this.p$;
            int i3 = this.$n;
            if (i3 == 0) {
                return Unit.INSTANCE;
            }
            if (i3 >= 0) {
                producerScope = producerScope2;
                channelsKt__Channels_commonKt$take$1 = this;
                i = this.$n;
                channelIterator = this.$this_take.iterator();
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
        channelsKt__Channels_commonKt$take$1.L$0 = producerScope;
        channelsKt__Channels_commonKt$take$1.I$0 = i;
        channelsKt__Channels_commonKt$take$1.L$1 = channelIterator;
        channelsKt__Channels_commonKt$take$1.label = 1;
        Object hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$take$1);
        if (hasNext == coroutine_suspended) {
            return coroutine_suspended;
        }
        Object obj4 = coroutine_suspended;
        channelsKt__Channels_commonKt$take$12 = channelsKt__Channels_commonKt$take$1;
        obj = hasNext;
        obj2 = obj4;
        if (!((Boolean) obj).booleanValue()) {
        }
        return Unit.INSTANCE;
        return coroutine_suspended;
    }
}
