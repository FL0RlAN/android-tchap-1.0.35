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
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$map$1", f = "Channels.common.kt", i = {0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3}, l = {1900, 1900, 1199, 1199}, m = "invokeSuspend", n = {"$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "e$iv", "it", "$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "e$iv", "it"}, s = {"L$1", "L$3", "L$4", "L$5", "L$1", "L$3", "L$4", "L$5", "L$1", "L$3", "L$4", "L$5", "L$7", "L$8", "L$1", "L$3", "L$4", "L$5", "L$7", "L$8"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$map$1 extends SuspendLambda implements Function2<ProducerScope<? super R>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ReceiveChannel $this_map;
    final /* synthetic */ Function2 $transform;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    Object L$5;
    Object L$6;
    Object L$7;
    Object L$8;
    Object L$9;
    int label;
    private ProducerScope p$;

    ChannelsKt__Channels_commonKt$map$1(ReceiveChannel receiveChannel, Function2 function2, Continuation continuation) {
        this.$this_map = receiveChannel;
        this.$transform = function2;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$1 = new ChannelsKt__Channels_commonKt$map$1(this.$this_map, this.$transform, continuation);
        channelsKt__Channels_commonKt$map$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$map$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$map$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x011b A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0132 A[Catch:{ all -> 0x00ec }] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0192 A[Catch:{ all -> 0x00ec }] */
    public final Object invokeSuspend(Object obj) {
        ReceiveChannel receiveChannel;
        Throwable th;
        Throwable th2;
        ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$1;
        Object obj2;
        ReceiveChannel receiveChannel2;
        ProducerScope producerScope;
        ChannelIterator channelIterator;
        ReceiveChannel receiveChannel3;
        ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$12;
        ProducerScope producerScope2;
        ChannelIterator channelIterator2;
        Throwable th3;
        Object obj3;
        Object obj4;
        ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$13;
        ReceiveChannel receiveChannel4;
        Object obj5;
        ReceiveChannel receiveChannel5;
        ChannelIterator channelIterator3;
        ProducerScope producerScope3;
        ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$14;
        Object obj6;
        ReceiveChannel receiveChannel6;
        Throwable th4;
        ReceiveChannel receiveChannel7;
        ChannelIterator channelIterator4;
        Object obj7;
        Object hasNext;
        Object obj8 = obj;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        int i2 = 2;
        if (i != 0) {
            if (i == 1) {
                channelIterator4 = (ChannelIterator) this.L$6;
                receiveChannel7 = (ReceiveChannel) this.L$5;
                th4 = (Throwable) this.L$4;
                receiveChannel = (ReceiveChannel) this.L$3;
                channelsKt__Channels_commonKt$map$1 = (ChannelsKt__Channels_commonKt$map$1) this.L$2;
                receiveChannel6 = (ReceiveChannel) this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                if (!(obj8 instanceof Failure)) {
                    obj6 = coroutine_suspended;
                    channelsKt__Channels_commonKt$map$14 = this;
                    if (!((Boolean) obj8).booleanValue()) {
                        Unit unit = Unit.INSTANCE;
                    }
                    Unit unit2 = Unit.INSTANCE;
                    receiveChannel.cancel(th4);
                    return Unit.INSTANCE;
                }
                throw ((Failure) obj8).exception;
            } else if (i == 2) {
                channelIterator4 = (ChannelIterator) this.L$6;
                receiveChannel7 = (ReceiveChannel) this.L$5;
                th4 = (Throwable) this.L$4;
                receiveChannel = (ReceiveChannel) this.L$3;
                ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$15 = (ChannelsKt__Channels_commonKt$map$1) this.L$2;
                receiveChannel6 = (ReceiveChannel) this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                if (!(obj8 instanceof Failure)) {
                    obj6 = coroutine_suspended;
                    channelsKt__Channels_commonKt$map$13 = channelsKt__Channels_commonKt$map$15;
                    obj7 = obj8;
                    channelsKt__Channels_commonKt$map$14 = this;
                } else {
                    throw ((Failure) obj8).exception;
                }
            } else if (i == 3) {
                producerScope3 = (ProducerScope) this.L$9;
                Object obj9 = this.L$8;
                Object obj10 = this.L$7;
                ChannelIterator channelIterator5 = (ChannelIterator) this.L$6;
                ReceiveChannel receiveChannel8 = (ReceiveChannel) this.L$5;
                Throwable th5 = (Throwable) this.L$4;
                ReceiveChannel receiveChannel9 = (ReceiveChannel) this.L$3;
                channelsKt__Channels_commonKt$map$13 = (ChannelsKt__Channels_commonKt$map$1) this.L$2;
                ReceiveChannel receiveChannel10 = (ReceiveChannel) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                try {
                    if (!(obj8 instanceof Failure)) {
                        obj2 = coroutine_suspended;
                        channelIterator3 = channelIterator5;
                        receiveChannel = receiveChannel9;
                        channelsKt__Channels_commonKt$map$14 = this;
                        ReceiveChannel receiveChannel11 = receiveChannel10;
                        obj4 = obj9;
                        receiveChannel5 = receiveChannel8;
                        obj5 = obj10;
                        th2 = th5;
                        receiveChannel4 = receiveChannel11;
                        channelsKt__Channels_commonKt$map$14.L$0 = producerScope;
                        channelsKt__Channels_commonKt$map$14.L$1 = receiveChannel4;
                        channelsKt__Channels_commonKt$map$14.L$2 = channelsKt__Channels_commonKt$map$13;
                        channelsKt__Channels_commonKt$map$14.L$3 = receiveChannel;
                        channelsKt__Channels_commonKt$map$14.L$4 = th2;
                        channelsKt__Channels_commonKt$map$14.L$5 = receiveChannel5;
                        channelsKt__Channels_commonKt$map$14.L$6 = channelIterator3;
                        channelsKt__Channels_commonKt$map$14.L$7 = obj5;
                        channelsKt__Channels_commonKt$map$14.L$8 = obj4;
                        channelsKt__Channels_commonKt$map$14.label = 4;
                        if (producerScope3.send(obj8, channelsKt__Channels_commonKt$map$14) != obj2) {
                            return obj2;
                        }
                        channelsKt__Channels_commonKt$map$12 = channelsKt__Channels_commonKt$map$14;
                        channelIterator = channelIterator3;
                        receiveChannel2 = receiveChannel5;
                        receiveChannel3 = receiveChannel;
                        receiveChannel = receiveChannel4;
                        channelsKt__Channels_commonKt$map$1 = channelsKt__Channels_commonKt$map$13;
                        producerScope2 = producerScope;
                        channelIterator2 = channelIterator;
                        th3 = th2;
                        obj3 = obj2;
                        i2 = 2;
                        channelsKt__Channels_commonKt$map$12.L$0 = producerScope2;
                        channelsKt__Channels_commonKt$map$12.L$1 = receiveChannel;
                        channelsKt__Channels_commonKt$map$12.L$2 = channelsKt__Channels_commonKt$map$1;
                        channelsKt__Channels_commonKt$map$12.L$3 = receiveChannel3;
                        channelsKt__Channels_commonKt$map$12.L$4 = th3;
                        channelsKt__Channels_commonKt$map$12.L$5 = receiveChannel2;
                        channelsKt__Channels_commonKt$map$12.L$6 = channelIterator2;
                        channelsKt__Channels_commonKt$map$12.label = 1;
                        hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$map$1);
                        if (hasNext == obj3) {
                        }
                        return obj3;
                        return obj2;
                    }
                    throw ((Failure) obj8).exception;
                } catch (Throwable th6) {
                    th = th6;
                    receiveChannel = receiveChannel9;
                    throw th;
                }
            } else if (i == 4) {
                Object obj11 = this.L$8;
                Object obj12 = this.L$7;
                channelIterator = (ChannelIterator) this.L$6;
                ReceiveChannel receiveChannel12 = (ReceiveChannel) this.L$5;
                th2 = (Throwable) this.L$4;
                receiveChannel = (ReceiveChannel) this.L$3;
                ChannelsKt__Channels_commonKt$map$1 channelsKt__Channels_commonKt$map$16 = (ChannelsKt__Channels_commonKt$map$1) this.L$2;
                ReceiveChannel receiveChannel13 = (ReceiveChannel) this.L$1;
                ProducerScope producerScope4 = (ProducerScope) this.L$0;
                try {
                    if (!(obj8 instanceof Failure)) {
                        channelsKt__Channels_commonKt$map$12 = this;
                        obj2 = coroutine_suspended;
                        receiveChannel2 = receiveChannel12;
                        receiveChannel3 = receiveChannel;
                        channelsKt__Channels_commonKt$map$1 = channelsKt__Channels_commonKt$map$16;
                        receiveChannel = receiveChannel13;
                        producerScope = producerScope4;
                        producerScope2 = producerScope;
                        channelIterator2 = channelIterator;
                        th3 = th2;
                        obj3 = obj2;
                        i2 = 2;
                        try {
                            channelsKt__Channels_commonKt$map$12.L$0 = producerScope2;
                            channelsKt__Channels_commonKt$map$12.L$1 = receiveChannel;
                            channelsKt__Channels_commonKt$map$12.L$2 = channelsKt__Channels_commonKt$map$1;
                            channelsKt__Channels_commonKt$map$12.L$3 = receiveChannel3;
                            channelsKt__Channels_commonKt$map$12.L$4 = th3;
                            channelsKt__Channels_commonKt$map$12.L$5 = receiveChannel2;
                            channelsKt__Channels_commonKt$map$12.L$6 = channelIterator2;
                            channelsKt__Channels_commonKt$map$12.label = 1;
                            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$map$1);
                            if (hasNext == obj3) {
                                return obj3;
                            }
                            ReceiveChannel receiveChannel14 = receiveChannel3;
                            channelsKt__Channels_commonKt$map$14 = channelsKt__Channels_commonKt$map$12;
                            obj8 = hasNext;
                            obj6 = obj3;
                            receiveChannel6 = receiveChannel;
                            receiveChannel = receiveChannel14;
                            ReceiveChannel receiveChannel15 = receiveChannel2;
                            th4 = th3;
                            channelIterator4 = channelIterator2;
                            receiveChannel7 = receiveChannel15;
                            if (!((Boolean) obj8).booleanValue()) {
                                channelsKt__Channels_commonKt$map$14.L$0 = producerScope2;
                                channelsKt__Channels_commonKt$map$14.L$1 = receiveChannel6;
                                channelsKt__Channels_commonKt$map$14.L$2 = channelsKt__Channels_commonKt$map$1;
                                channelsKt__Channels_commonKt$map$14.L$3 = receiveChannel;
                                channelsKt__Channels_commonKt$map$14.L$4 = th4;
                                channelsKt__Channels_commonKt$map$14.L$5 = receiveChannel7;
                                channelsKt__Channels_commonKt$map$14.L$6 = channelIterator4;
                                channelsKt__Channels_commonKt$map$14.label = i2;
                                Object next = channelIterator4.next(channelsKt__Channels_commonKt$map$1);
                                if (next == obj6) {
                                    return obj6;
                                }
                                channelsKt__Channels_commonKt$map$13 = channelsKt__Channels_commonKt$map$1;
                                obj7 = next;
                                return obj6;
                            }
                            Unit unit22 = Unit.INSTANCE;
                            receiveChannel.cancel(th4);
                            return Unit.INSTANCE;
                            return obj3;
                        } catch (Throwable th7) {
                            th = th7;
                            receiveChannel = receiveChannel3;
                            th = th;
                            try {
                                throw th;
                            } catch (Throwable th8) {
                                Throwable th9 = th8;
                                receiveChannel.cancel(th);
                                throw th9;
                            }
                        }
                    } else {
                        throw ((Failure) obj8).exception;
                    }
                } catch (Throwable th10) {
                    th = th10;
                    th = th;
                    throw th;
                }
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(obj8 instanceof Failure)) {
            ProducerScope producerScope5 = this.p$;
            receiveChannel = this.$this_map;
            th3 = null;
            channelIterator2 = receiveChannel.iterator();
            producerScope2 = producerScope5;
            channelsKt__Channels_commonKt$map$12 = this;
            channelsKt__Channels_commonKt$map$1 = channelsKt__Channels_commonKt$map$12;
            obj3 = coroutine_suspended;
            receiveChannel3 = receiveChannel;
            receiveChannel2 = receiveChannel3;
            channelsKt__Channels_commonKt$map$12.L$0 = producerScope2;
            channelsKt__Channels_commonKt$map$12.L$1 = receiveChannel;
            channelsKt__Channels_commonKt$map$12.L$2 = channelsKt__Channels_commonKt$map$1;
            channelsKt__Channels_commonKt$map$12.L$3 = receiveChannel3;
            channelsKt__Channels_commonKt$map$12.L$4 = th3;
            channelsKt__Channels_commonKt$map$12.L$5 = receiveChannel2;
            channelsKt__Channels_commonKt$map$12.L$6 = channelIterator2;
            channelsKt__Channels_commonKt$map$12.label = 1;
            hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$map$1);
            if (hasNext == obj3) {
            }
            return obj3;
        } else {
            throw ((Failure) obj8).exception;
        }
        ChannelIterator channelIterator6 = channelIterator4;
        producerScope3 = producerScope2;
        Function2 function2 = channelsKt__Channels_commonKt$map$14.$transform;
        channelsKt__Channels_commonKt$map$14.L$0 = producerScope3;
        channelsKt__Channels_commonKt$map$14.L$1 = receiveChannel6;
        channelsKt__Channels_commonKt$map$14.L$2 = channelsKt__Channels_commonKt$map$13;
        channelsKt__Channels_commonKt$map$14.L$3 = receiveChannel;
        channelsKt__Channels_commonKt$map$14.L$4 = th4;
        channelsKt__Channels_commonKt$map$14.L$5 = receiveChannel7;
        channelsKt__Channels_commonKt$map$14.L$6 = channelIterator6;
        channelsKt__Channels_commonKt$map$14.L$7 = obj7;
        channelsKt__Channels_commonKt$map$14.L$8 = obj7;
        channelsKt__Channels_commonKt$map$14.L$9 = producerScope3;
        channelsKt__Channels_commonKt$map$14.label = 3;
        Object invoke = function2.invoke(obj7, channelsKt__Channels_commonKt$map$14);
        if (invoke == obj6) {
            return obj6;
        }
        obj4 = obj7;
        obj2 = obj6;
        receiveChannel5 = receiveChannel7;
        receiveChannel4 = receiveChannel6;
        producerScope = producerScope3;
        th2 = th4;
        channelIterator3 = channelIterator6;
        obj8 = invoke;
        obj5 = obj4;
        channelsKt__Channels_commonKt$map$14.L$0 = producerScope;
        channelsKt__Channels_commonKt$map$14.L$1 = receiveChannel4;
        channelsKt__Channels_commonKt$map$14.L$2 = channelsKt__Channels_commonKt$map$13;
        channelsKt__Channels_commonKt$map$14.L$3 = receiveChannel;
        channelsKt__Channels_commonKt$map$14.L$4 = th2;
        channelsKt__Channels_commonKt$map$14.L$5 = receiveChannel5;
        channelsKt__Channels_commonKt$map$14.L$6 = channelIterator3;
        channelsKt__Channels_commonKt$map$14.L$7 = obj5;
        channelsKt__Channels_commonKt$map$14.L$8 = obj4;
        channelsKt__Channels_commonKt$map$14.label = 4;
        if (producerScope3.send(obj8, channelsKt__Channels_commonKt$map$14) != obj2) {
        }
        return obj2;
        return obj6;
    }
}
