package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.Result.Failure;
import kotlin.Unit;
import kotlin.collections.IndexedValue;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00040\u0003H@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;", "Lkotlin/collections/IndexedValue;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$withIndex$1", f = "Channels.common.kt", i = {0, 1, 2, 2}, l = {1424, 1424, 1425}, m = "invokeSuspend", n = {"index", "index", "index", "e"}, s = {"I$0", "I$0", "I$0", "L$1"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$withIndex$1 extends SuspendLambda implements Function2<ProducerScope<? super IndexedValue<? extends E>>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ReceiveChannel $this_withIndex;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    ChannelsKt__Channels_commonKt$withIndex$1(ReceiveChannel receiveChannel, Continuation continuation) {
        this.$this_withIndex = receiveChannel;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$withIndex$1 channelsKt__Channels_commonKt$withIndex$1 = new ChannelsKt__Channels_commonKt$withIndex$1(this.$this_withIndex, continuation);
        channelsKt__Channels_commonKt$withIndex$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$withIndex$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$withIndex$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00b4  */
    public final Object invokeSuspend(Object obj) {
        Object obj2;
        ProducerScope producerScope;
        int i;
        ChannelIterator channelIterator;
        ChannelsKt__Channels_commonKt$withIndex$1 channelsKt__Channels_commonKt$withIndex$1;
        IndexedValue indexedValue;
        ChannelsKt__Channels_commonKt$withIndex$1 channelsKt__Channels_commonKt$withIndex$12;
        Object obj3;
        int i2;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i3 = this.label;
        if (i3 != 0) {
            if (i3 == 1) {
                channelIterator = (ChannelIterator) this.L$1;
                i = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$withIndex$1 = this;
                    if (!((Boolean) obj).booleanValue()) {
                        channelsKt__Channels_commonKt$withIndex$1.L$0 = producerScope;
                        channelsKt__Channels_commonKt$withIndex$1.I$0 = i;
                        channelsKt__Channels_commonKt$withIndex$1.L$1 = channelIterator;
                        channelsKt__Channels_commonKt$withIndex$1.label = 2;
                        obj = channelIterator.next(channelsKt__Channels_commonKt$withIndex$1);
                        if (obj == obj2) {
                            return obj2;
                        }
                        int i4 = i + 1;
                        indexedValue = new IndexedValue(i, obj);
                        channelsKt__Channels_commonKt$withIndex$1.L$0 = producerScope;
                        channelsKt__Channels_commonKt$withIndex$1.I$0 = i4;
                        channelsKt__Channels_commonKt$withIndex$1.L$1 = obj;
                        channelsKt__Channels_commonKt$withIndex$1.L$2 = channelIterator;
                        channelsKt__Channels_commonKt$withIndex$1.label = 3;
                        if (producerScope.send(indexedValue, channelsKt__Channels_commonKt$withIndex$1) != obj2) {
                        }
                        return obj2;
                    }
                    return Unit.INSTANCE;
                }
                throw ((Failure) obj).exception;
            } else if (i3 == 2) {
                channelIterator = (ChannelIterator) this.L$1;
                i = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$withIndex$1 = this;
                    int i42 = i + 1;
                    indexedValue = new IndexedValue(i, obj);
                    channelsKt__Channels_commonKt$withIndex$1.L$0 = producerScope;
                    channelsKt__Channels_commonKt$withIndex$1.I$0 = i42;
                    channelsKt__Channels_commonKt$withIndex$1.L$1 = obj;
                    channelsKt__Channels_commonKt$withIndex$1.L$2 = channelIterator;
                    channelsKt__Channels_commonKt$withIndex$1.label = 3;
                    if (producerScope.send(indexedValue, channelsKt__Channels_commonKt$withIndex$1) != obj2) {
                        return obj2;
                    }
                    channelsKt__Channels_commonKt$withIndex$12 = channelsKt__Channels_commonKt$withIndex$1;
                    obj3 = obj2;
                    i2 = i42;
                    return obj2;
                }
                throw ((Failure) obj).exception;
            } else if (i3 == 3) {
                channelIterator = (ChannelIterator) this.L$2;
                Object obj4 = this.L$1;
                int i5 = this.I$0;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    channelsKt__Channels_commonKt$withIndex$12 = this;
                    int i6 = i5;
                    obj3 = coroutine_suspended;
                    i2 = i6;
                } else {
                    throw ((Failure) obj).exception;
                }
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(obj instanceof Failure)) {
            producerScope = this.p$;
            channelIterator = this.$this_withIndex.iterator();
            channelsKt__Channels_commonKt$withIndex$12 = this;
            obj3 = coroutine_suspended;
            i2 = 0;
        } else {
            throw ((Failure) obj).exception;
        }
        channelsKt__Channels_commonKt$withIndex$12.L$0 = producerScope;
        channelsKt__Channels_commonKt$withIndex$12.I$0 = i2;
        channelsKt__Channels_commonKt$withIndex$12.L$1 = channelIterator;
        channelsKt__Channels_commonKt$withIndex$12.label = 1;
        Object hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$withIndex$12);
        if (hasNext == obj3) {
            return obj3;
        }
        int i7 = i2;
        channelsKt__Channels_commonKt$withIndex$1 = channelsKt__Channels_commonKt$withIndex$12;
        obj = hasNext;
        obj2 = obj3;
        i = i7;
        if (!((Boolean) obj).booleanValue()) {
        }
        return Unit.INSTANCE;
        return obj3;
    }
}
