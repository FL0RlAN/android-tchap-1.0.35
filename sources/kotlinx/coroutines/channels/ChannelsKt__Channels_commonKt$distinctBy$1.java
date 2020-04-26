package kotlinx.coroutines.channels;

import java.util.HashSet;
import kotlin.Metadata;
import kotlin.Result.Failure;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u0004H@ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006"}, d2 = {"<anonymous>", "", "E", "K", "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$distinctBy$1", f = "Channels.common.kt", i = {0, 1, 2, 2, 3, 3, 3}, l = {1461, 1461, 1462, 1464}, m = "invokeSuspend", n = {"keys", "keys", "keys", "e", "keys", "e", "k"}, s = {"L$1", "L$1", "L$1", "L$2", "L$1", "L$2", "L$4"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$distinctBy$1 extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2 $selector;
    final /* synthetic */ ReceiveChannel $this_distinctBy;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    int label;
    private ProducerScope p$;

    ChannelsKt__Channels_commonKt$distinctBy$1(ReceiveChannel receiveChannel, Function2 function2, Continuation continuation) {
        this.$this_distinctBy = receiveChannel;
        this.$selector = function2;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$distinctBy$1 channelsKt__Channels_commonKt$distinctBy$1 = new ChannelsKt__Channels_commonKt$distinctBy$1(this.$this_distinctBy, this.$selector, continuation);
        channelsKt__Channels_commonKt$distinctBy$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$distinctBy$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$distinctBy$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x00ad A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00ae  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00be  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00ee  */
    public final Object invokeSuspend(Object obj) {
        Object obj2;
        ProducerScope producerScope;
        HashSet hashSet;
        ChannelIterator channelIterator;
        ChannelsKt__Channels_commonKt$distinctBy$1 channelsKt__Channels_commonKt$distinctBy$1;
        HashSet hashSet2;
        Object obj3;
        ChannelsKt__Channels_commonKt$distinctBy$1 channelsKt__Channels_commonKt$distinctBy$12;
        ProducerScope producerScope2;
        Object obj4;
        Object obj5;
        Object obj6;
        ProducerScope producerScope3;
        HashSet hashSet3;
        ChannelIterator channelIterator2;
        Object hasNext;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i != 0) {
            if (i == 1) {
                channelIterator2 = (ChannelIterator) this.L$2;
                hashSet3 = (HashSet) this.L$1;
                producerScope3 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj6 = coroutine_suspended;
                    channelsKt__Channels_commonKt$distinctBy$1 = this;
                    if (!((Boolean) obj).booleanValue()) {
                    }
                    return Unit.INSTANCE;
                }
                throw ((Failure) obj).exception;
            } else if (i == 2) {
                channelIterator2 = (ChannelIterator) this.L$2;
                hashSet3 = (HashSet) this.L$1;
                producerScope3 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj6 = coroutine_suspended;
                    channelsKt__Channels_commonKt$distinctBy$1 = this;
                } else {
                    throw ((Failure) obj).exception;
                }
            } else if (i == 3) {
                ChannelIterator channelIterator3 = (ChannelIterator) this.L$3;
                Object obj7 = this.L$2;
                hashSet = (HashSet) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj5 = obj7;
                    channelIterator = channelIterator3;
                    obj4 = obj;
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$distinctBy$1 = this;
                    if (!hashSet.contains(obj4)) {
                        channelsKt__Channels_commonKt$distinctBy$1.L$0 = producerScope;
                        channelsKt__Channels_commonKt$distinctBy$1.L$1 = hashSet;
                        channelsKt__Channels_commonKt$distinctBy$1.L$2 = obj5;
                        channelsKt__Channels_commonKt$distinctBy$1.L$3 = channelIterator;
                        channelsKt__Channels_commonKt$distinctBy$1.L$4 = obj4;
                        channelsKt__Channels_commonKt$distinctBy$1.label = 4;
                        if (producerScope.send(obj5, channelsKt__Channels_commonKt$distinctBy$1) == obj2) {
                            return obj2;
                        }
                        hashSet.add(obj4);
                    }
                    hashSet2 = hashSet;
                    obj3 = obj2;
                    channelsKt__Channels_commonKt$distinctBy$12 = channelsKt__Channels_commonKt$distinctBy$1;
                    producerScope2 = producerScope;
                    channelsKt__Channels_commonKt$distinctBy$12.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$distinctBy$12.L$1 = hashSet2;
                    channelsKt__Channels_commonKt$distinctBy$12.L$2 = channelIterator;
                    channelsKt__Channels_commonKt$distinctBy$12.label = 1;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$distinctBy$12);
                    if (hasNext != obj3) {
                    }
                    return obj3;
                }
                throw ((Failure) obj).exception;
            } else if (i == 4) {
                obj4 = this.L$4;
                channelIterator = (ChannelIterator) this.L$3;
                Object obj8 = this.L$2;
                hashSet = (HashSet) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$distinctBy$1 = this;
                    hashSet.add(obj4);
                    hashSet2 = hashSet;
                    obj3 = obj2;
                    channelsKt__Channels_commonKt$distinctBy$12 = channelsKt__Channels_commonKt$distinctBy$1;
                    producerScope2 = producerScope;
                    channelsKt__Channels_commonKt$distinctBy$12.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$distinctBy$12.L$1 = hashSet2;
                    channelsKt__Channels_commonKt$distinctBy$12.L$2 = channelIterator;
                    channelsKt__Channels_commonKt$distinctBy$12.label = 1;
                    hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$distinctBy$12);
                    if (hasNext != obj3) {
                        return obj3;
                    }
                    ProducerScope producerScope4 = producerScope2;
                    channelsKt__Channels_commonKt$distinctBy$1 = channelsKt__Channels_commonKt$distinctBy$12;
                    obj = hasNext;
                    obj6 = obj3;
                    producerScope3 = producerScope4;
                    ChannelIterator channelIterator4 = channelIterator;
                    hashSet3 = hashSet2;
                    channelIterator2 = channelIterator4;
                    if (!((Boolean) obj).booleanValue()) {
                        channelsKt__Channels_commonKt$distinctBy$1.L$0 = producerScope3;
                        channelsKt__Channels_commonKt$distinctBy$1.L$1 = hashSet3;
                        channelsKt__Channels_commonKt$distinctBy$1.L$2 = channelIterator2;
                        channelsKt__Channels_commonKt$distinctBy$1.label = 2;
                        obj = channelIterator2.next(channelsKt__Channels_commonKt$distinctBy$1);
                        if (obj == obj6) {
                            return obj6;
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
            ProducerScope producerScope5 = this.p$;
            hashSet2 = new HashSet();
            channelIterator = this.$this_distinctBy.iterator();
            obj3 = coroutine_suspended;
            producerScope2 = producerScope5;
            channelsKt__Channels_commonKt$distinctBy$12 = this;
            channelsKt__Channels_commonKt$distinctBy$12.L$0 = producerScope2;
            channelsKt__Channels_commonKt$distinctBy$12.L$1 = hashSet2;
            channelsKt__Channels_commonKt$distinctBy$12.L$2 = channelIterator;
            channelsKt__Channels_commonKt$distinctBy$12.label = 1;
            hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$distinctBy$12);
            if (hasNext != obj3) {
            }
            return obj3;
        } else {
            throw ((Failure) obj).exception;
        }
        HashSet hashSet4 = hashSet3;
        Object obj9 = obj;
        HashSet hashSet5 = hashSet4;
        Function2 function2 = channelsKt__Channels_commonKt$distinctBy$1.$selector;
        channelsKt__Channels_commonKt$distinctBy$1.L$0 = producerScope3;
        channelsKt__Channels_commonKt$distinctBy$1.L$1 = hashSet5;
        channelsKt__Channels_commonKt$distinctBy$1.L$2 = obj9;
        channelsKt__Channels_commonKt$distinctBy$1.L$3 = channelIterator2;
        channelsKt__Channels_commonKt$distinctBy$1.label = 3;
        Object invoke = function2.invoke(obj9, channelsKt__Channels_commonKt$distinctBy$1);
        if (invoke == obj6) {
            return obj6;
        }
        ProducerScope producerScope6 = producerScope3;
        hashSet = hashSet5;
        obj2 = obj6;
        producerScope = producerScope6;
        Object obj10 = obj9;
        channelIterator = channelIterator2;
        obj4 = invoke;
        obj5 = obj10;
        if (!hashSet.contains(obj4)) {
        }
        hashSet2 = hashSet;
        obj3 = obj2;
        channelsKt__Channels_commonKt$distinctBy$12 = channelsKt__Channels_commonKt$distinctBy$1;
        producerScope2 = producerScope;
        channelsKt__Channels_commonKt$distinctBy$12.L$0 = producerScope2;
        channelsKt__Channels_commonKt$distinctBy$12.L$1 = hashSet2;
        channelsKt__Channels_commonKt$distinctBy$12.L$2 = channelIterator;
        channelsKt__Channels_commonKt$distinctBy$12.label = 1;
        hasNext = channelIterator.hasNext(channelsKt__Channels_commonKt$distinctBy$12);
        if (hasNext != obj3) {
        }
        return obj3;
        return obj6;
    }
}
