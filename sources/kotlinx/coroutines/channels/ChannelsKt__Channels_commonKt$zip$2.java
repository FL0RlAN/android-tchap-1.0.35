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

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u0005H@ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0007"}, d2 = {"<anonymous>", "", "E", "R", "V", "Lkotlinx/coroutines/channels/ProducerScope;", "invoke", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 13})
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$zip$2", f = "Channels.common.kt", i = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4}, l = {1900, 1900, 1888, 1889, 1890}, m = "invokeSuspend", n = {"otherIterator", "$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "otherIterator", "$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "otherIterator", "$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "e$iv", "element1", "otherIterator", "$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "e$iv", "element1", "otherIterator", "$receiver$iv", "$receiver$iv$iv", "cause$iv$iv", "$receiver$iv", "e$iv", "element1", "element2"}, s = {"L$1", "L$2", "L$4", "L$5", "L$6", "L$1", "L$2", "L$4", "L$5", "L$6", "L$1", "L$2", "L$4", "L$5", "L$6", "L$8", "L$9", "L$1", "L$2", "L$4", "L$5", "L$6", "L$8", "L$9", "L$1", "L$2", "L$4", "L$5", "L$6", "L$8", "L$9", "L$10"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$zip$2 extends SuspendLambda implements Function2<ProducerScope<? super V>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ReceiveChannel $other;
    final /* synthetic */ ReceiveChannel $this_zip;
    final /* synthetic */ Function2 $transform;
    Object L$0;
    Object L$1;
    Object L$10;
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

    ChannelsKt__Channels_commonKt$zip$2(ReceiveChannel receiveChannel, ReceiveChannel receiveChannel2, Function2 function2, Continuation continuation) {
        this.$this_zip = receiveChannel;
        this.$other = receiveChannel2;
        this.$transform = function2;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$2 = new ChannelsKt__Channels_commonKt$zip$2(this.$this_zip, this.$other, this.$transform, continuation);
        channelsKt__Channels_commonKt$zip$2.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$zip$2;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$zip$2) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:59:0x017f A[Catch:{ all -> 0x0137 }] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x01be A[Catch:{ all -> 0x0137 }] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x01f0 A[Catch:{ all -> 0x0137 }] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x021d A[Catch:{ all -> 0x0137 }] */
    public final Object invokeSuspend(Object obj) {
        ReceiveChannel receiveChannel;
        Throwable th;
        ProducerScope producerScope;
        ChannelIterator channelIterator;
        ReceiveChannel receiveChannel2;
        Object obj2;
        ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$2;
        Throwable th2;
        ReceiveChannel receiveChannel3;
        ChannelIterator channelIterator2;
        ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$22;
        ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$23;
        ProducerScope producerScope2;
        ChannelIterator channelIterator3;
        ReceiveChannel receiveChannel4;
        ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$24;
        Throwable th3;
        ReceiveChannel receiveChannel5;
        ChannelIterator channelIterator4;
        Object obj3;
        Object obj4;
        Object invoke;
        Object obj5;
        Object obj6;
        Object obj7;
        Object next;
        Object obj8;
        Object obj9 = obj;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        int i2 = 2;
        int i3 = 1;
        if (i != 0) {
            if (i == 1) {
                channelIterator2 = (ChannelIterator) this.L$7;
                receiveChannel3 = (ReceiveChannel) this.L$6;
                th2 = (Throwable) this.L$5;
                receiveChannel = (ReceiveChannel) this.L$4;
                channelsKt__Channels_commonKt$zip$2 = (ChannelsKt__Channels_commonKt$zip$2) this.L$3;
                receiveChannel2 = (ReceiveChannel) this.L$2;
                channelIterator = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj9 instanceof Failure)) {
                    obj8 = coroutine_suspended;
                    channelsKt__Channels_commonKt$zip$22 = this;
                    if (!((Boolean) obj9).booleanValue()) {
                        channelsKt__Channels_commonKt$zip$22.L$0 = producerScope;
                        channelsKt__Channels_commonKt$zip$22.L$1 = channelIterator;
                        channelsKt__Channels_commonKt$zip$22.L$2 = receiveChannel2;
                        channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$2;
                        channelsKt__Channels_commonKt$zip$22.L$4 = receiveChannel;
                        channelsKt__Channels_commonKt$zip$22.L$5 = th2;
                        channelsKt__Channels_commonKt$zip$22.L$6 = receiveChannel3;
                        channelsKt__Channels_commonKt$zip$22.L$7 = channelIterator2;
                        channelsKt__Channels_commonKt$zip$22.label = i2;
                        obj9 = channelIterator2.next(channelsKt__Channels_commonKt$zip$2);
                        if (obj9 == obj8) {
                            return obj8;
                        }
                        producerScope2 = producerScope;
                        receiveChannel4 = receiveChannel2;
                        th3 = th2;
                        channelIterator4 = channelIterator2;
                        obj7 = obj8;
                        channelIterator3 = channelIterator;
                        channelsKt__Channels_commonKt$zip$24 = channelsKt__Channels_commonKt$zip$2;
                        receiveChannel5 = receiveChannel3;
                        channelsKt__Channels_commonKt$zip$22.L$0 = producerScope2;
                        channelsKt__Channels_commonKt$zip$22.L$1 = channelIterator3;
                        channelsKt__Channels_commonKt$zip$22.L$2 = receiveChannel4;
                        channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$24;
                        channelsKt__Channels_commonKt$zip$22.L$4 = receiveChannel;
                        channelsKt__Channels_commonKt$zip$22.L$5 = th3;
                        channelsKt__Channels_commonKt$zip$22.L$6 = receiveChannel5;
                        channelsKt__Channels_commonKt$zip$22.L$7 = channelIterator4;
                        channelsKt__Channels_commonKt$zip$22.L$8 = obj9;
                        channelsKt__Channels_commonKt$zip$22.L$9 = obj9;
                        channelsKt__Channels_commonKt$zip$22.label = 3;
                        obj5 = channelIterator3.hasNext(channelsKt__Channels_commonKt$zip$22);
                        if (obj5 != obj7) {
                        }
                        return obj7;
                        return obj8;
                    }
                    Unit unit = Unit.INSTANCE;
                    receiveChannel.cancel(th2);
                    return Unit.INSTANCE;
                }
                throw ((Failure) obj9).exception;
            } else if (i == 2) {
                ChannelIterator channelIterator5 = (ChannelIterator) this.L$7;
                ReceiveChannel receiveChannel6 = (ReceiveChannel) this.L$6;
                Throwable th4 = (Throwable) this.L$5;
                receiveChannel = (ReceiveChannel) this.L$4;
                ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$25 = (ChannelsKt__Channels_commonKt$zip$2) this.L$3;
                ReceiveChannel receiveChannel7 = (ReceiveChannel) this.L$2;
                ChannelIterator channelIterator6 = (ChannelIterator) this.L$1;
                ProducerScope producerScope3 = (ProducerScope) this.L$0;
                if (!(obj9 instanceof Failure)) {
                    channelIterator3 = channelIterator6;
                    producerScope2 = producerScope3;
                    channelsKt__Channels_commonKt$zip$24 = channelsKt__Channels_commonKt$zip$25;
                    receiveChannel4 = receiveChannel7;
                    receiveChannel5 = receiveChannel6;
                    th3 = th4;
                    channelIterator4 = channelIterator5;
                    obj7 = coroutine_suspended;
                    channelsKt__Channels_commonKt$zip$22 = this;
                    channelsKt__Channels_commonKt$zip$22.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$zip$22.L$1 = channelIterator3;
                    channelsKt__Channels_commonKt$zip$22.L$2 = receiveChannel4;
                    channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$24;
                    channelsKt__Channels_commonKt$zip$22.L$4 = receiveChannel;
                    channelsKt__Channels_commonKt$zip$22.L$5 = th3;
                    channelsKt__Channels_commonKt$zip$22.L$6 = receiveChannel5;
                    channelsKt__Channels_commonKt$zip$22.L$7 = channelIterator4;
                    channelsKt__Channels_commonKt$zip$22.L$8 = obj9;
                    channelsKt__Channels_commonKt$zip$22.L$9 = obj9;
                    channelsKt__Channels_commonKt$zip$22.label = 3;
                    obj5 = channelIterator3.hasNext(channelsKt__Channels_commonKt$zip$22);
                    if (obj5 != obj7) {
                        return obj7;
                    }
                    obj6 = obj9;
                    if (((Boolean) obj5).booleanValue()) {
                        channelsKt__Channels_commonKt$zip$22.L$0 = producerScope2;
                        channelsKt__Channels_commonKt$zip$22.L$1 = channelIterator3;
                        channelsKt__Channels_commonKt$zip$22.L$2 = receiveChannel4;
                        channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$24;
                        channelsKt__Channels_commonKt$zip$22.L$4 = receiveChannel;
                        channelsKt__Channels_commonKt$zip$22.L$5 = th3;
                        channelsKt__Channels_commonKt$zip$22.L$6 = receiveChannel5;
                        channelsKt__Channels_commonKt$zip$22.L$7 = channelIterator4;
                        channelsKt__Channels_commonKt$zip$22.L$8 = obj9;
                        channelsKt__Channels_commonKt$zip$22.L$9 = obj6;
                        channelsKt__Channels_commonKt$zip$22.label = 4;
                        next = channelIterator3.next(channelsKt__Channels_commonKt$zip$22);
                        if (next != obj7) {
                        }
                        return obj7;
                    }
                    obj2 = obj7;
                    channelIterator2 = channelIterator4;
                    receiveChannel3 = receiveChannel5;
                    th2 = th3;
                    channelsKt__Channels_commonKt$zip$2 = channelsKt__Channels_commonKt$zip$24;
                    receiveChannel2 = receiveChannel4;
                    channelIterator = channelIterator3;
                    producerScope = producerScope2;
                    channelsKt__Channels_commonKt$zip$23 = channelsKt__Channels_commonKt$zip$22;
                    coroutine_suspended = obj2;
                    i2 = 2;
                    i3 = 1;
                    return obj7;
                }
                throw ((Failure) obj9).exception;
            } else if (i == 3) {
                Object obj10 = this.L$9;
                Object obj11 = this.L$8;
                channelIterator4 = (ChannelIterator) this.L$7;
                receiveChannel5 = (ReceiveChannel) this.L$6;
                th3 = (Throwable) this.L$5;
                receiveChannel = (ReceiveChannel) this.L$4;
                channelsKt__Channels_commonKt$zip$24 = (ChannelsKt__Channels_commonKt$zip$2) this.L$3;
                receiveChannel4 = (ReceiveChannel) this.L$2;
                channelIterator3 = (ChannelIterator) this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                if (!(obj9 instanceof Failure)) {
                    obj5 = obj9;
                    obj9 = obj11;
                    obj6 = obj10;
                    obj7 = coroutine_suspended;
                    channelsKt__Channels_commonKt$zip$22 = this;
                    if (((Boolean) obj5).booleanValue()) {
                        obj2 = obj7;
                        channelIterator2 = channelIterator4;
                        receiveChannel3 = receiveChannel5;
                        th2 = th3;
                        channelsKt__Channels_commonKt$zip$2 = channelsKt__Channels_commonKt$zip$24;
                        receiveChannel2 = receiveChannel4;
                        channelIterator = channelIterator3;
                        producerScope = producerScope2;
                        channelsKt__Channels_commonKt$zip$23 = channelsKt__Channels_commonKt$zip$22;
                        coroutine_suspended = obj2;
                        i2 = 2;
                        i3 = 1;
                    }
                    channelsKt__Channels_commonKt$zip$22.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$zip$22.L$1 = channelIterator3;
                    channelsKt__Channels_commonKt$zip$22.L$2 = receiveChannel4;
                    channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$24;
                    channelsKt__Channels_commonKt$zip$22.L$4 = receiveChannel;
                    channelsKt__Channels_commonKt$zip$22.L$5 = th3;
                    channelsKt__Channels_commonKt$zip$22.L$6 = receiveChannel5;
                    channelsKt__Channels_commonKt$zip$22.L$7 = channelIterator4;
                    channelsKt__Channels_commonKt$zip$22.L$8 = obj9;
                    channelsKt__Channels_commonKt$zip$22.L$9 = obj6;
                    channelsKt__Channels_commonKt$zip$22.label = 4;
                    next = channelIterator3.next(channelsKt__Channels_commonKt$zip$22);
                    if (next != obj7) {
                        return obj7;
                    }
                    Object obj12 = obj6;
                    obj3 = obj9;
                    obj9 = next;
                    obj2 = obj7;
                    obj4 = obj12;
                    invoke = channelsKt__Channels_commonKt$zip$22.$transform.invoke(obj4, obj9);
                    channelsKt__Channels_commonKt$zip$22.L$0 = producerScope2;
                    channelsKt__Channels_commonKt$zip$22.L$1 = channelIterator3;
                    channelsKt__Channels_commonKt$zip$22.L$2 = receiveChannel4;
                    channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$24;
                    channelsKt__Channels_commonKt$zip$22.L$4 = receiveChannel;
                    channelsKt__Channels_commonKt$zip$22.L$5 = th3;
                    channelsKt__Channels_commonKt$zip$22.L$6 = receiveChannel5;
                    channelsKt__Channels_commonKt$zip$22.L$7 = channelIterator4;
                    channelsKt__Channels_commonKt$zip$22.L$8 = obj3;
                    channelsKt__Channels_commonKt$zip$22.L$9 = obj4;
                    channelsKt__Channels_commonKt$zip$22.L$10 = obj9;
                    channelsKt__Channels_commonKt$zip$22.label = 5;
                    if (producerScope2.send(invoke, channelsKt__Channels_commonKt$zip$22) != obj2) {
                    }
                    return obj2;
                    return obj7;
                }
                throw ((Failure) obj9).exception;
            } else if (i == 4) {
                obj4 = this.L$9;
                obj3 = this.L$8;
                ChannelIterator channelIterator7 = (ChannelIterator) this.L$7;
                ReceiveChannel receiveChannel8 = (ReceiveChannel) this.L$6;
                Throwable th5 = (Throwable) this.L$5;
                ReceiveChannel receiveChannel9 = (ReceiveChannel) this.L$4;
                ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$26 = (ChannelsKt__Channels_commonKt$zip$2) this.L$3;
                ReceiveChannel receiveChannel10 = (ReceiveChannel) this.L$2;
                ChannelIterator channelIterator8 = (ChannelIterator) this.L$1;
                ProducerScope producerScope4 = (ProducerScope) this.L$0;
                try {
                    if (!(obj9 instanceof Failure)) {
                        obj2 = coroutine_suspended;
                        receiveChannel5 = receiveChannel8;
                        channelsKt__Channels_commonKt$zip$24 = channelsKt__Channels_commonKt$zip$26;
                        channelIterator3 = channelIterator8;
                        channelsKt__Channels_commonKt$zip$22 = this;
                        ReceiveChannel receiveChannel11 = receiveChannel10;
                        producerScope2 = producerScope4;
                        channelIterator4 = channelIterator7;
                        th3 = th5;
                        receiveChannel = receiveChannel9;
                        receiveChannel4 = receiveChannel11;
                        invoke = channelsKt__Channels_commonKt$zip$22.$transform.invoke(obj4, obj9);
                        channelsKt__Channels_commonKt$zip$22.L$0 = producerScope2;
                        channelsKt__Channels_commonKt$zip$22.L$1 = channelIterator3;
                        channelsKt__Channels_commonKt$zip$22.L$2 = receiveChannel4;
                        channelsKt__Channels_commonKt$zip$22.L$3 = channelsKt__Channels_commonKt$zip$24;
                        channelsKt__Channels_commonKt$zip$22.L$4 = receiveChannel;
                        channelsKt__Channels_commonKt$zip$22.L$5 = th3;
                        channelsKt__Channels_commonKt$zip$22.L$6 = receiveChannel5;
                        channelsKt__Channels_commonKt$zip$22.L$7 = channelIterator4;
                        channelsKt__Channels_commonKt$zip$22.L$8 = obj3;
                        channelsKt__Channels_commonKt$zip$22.L$9 = obj4;
                        channelsKt__Channels_commonKt$zip$22.L$10 = obj9;
                        channelsKt__Channels_commonKt$zip$22.label = 5;
                        if (producerScope2.send(invoke, channelsKt__Channels_commonKt$zip$22) != obj2) {
                            return obj2;
                        }
                        channelIterator2 = channelIterator4;
                        receiveChannel3 = receiveChannel5;
                        th2 = th3;
                        channelsKt__Channels_commonKt$zip$2 = channelsKt__Channels_commonKt$zip$24;
                        receiveChannel2 = receiveChannel4;
                        channelIterator = channelIterator3;
                        producerScope = producerScope2;
                        channelsKt__Channels_commonKt$zip$23 = channelsKt__Channels_commonKt$zip$22;
                        coroutine_suspended = obj2;
                        i2 = 2;
                        i3 = 1;
                        return obj2;
                    }
                    throw ((Failure) obj9).exception;
                } catch (Throwable th6) {
                    th = th6;
                    receiveChannel = receiveChannel9;
                    throw th;
                }
            } else if (i == 5) {
                Object obj13 = this.L$10;
                Object obj14 = this.L$9;
                Object obj15 = this.L$8;
                channelIterator2 = (ChannelIterator) this.L$7;
                ReceiveChannel receiveChannel12 = (ReceiveChannel) this.L$6;
                Throwable th7 = (Throwable) this.L$5;
                receiveChannel = (ReceiveChannel) this.L$4;
                ChannelsKt__Channels_commonKt$zip$2 channelsKt__Channels_commonKt$zip$27 = (ChannelsKt__Channels_commonKt$zip$2) this.L$3;
                ReceiveChannel receiveChannel13 = (ReceiveChannel) this.L$2;
                ChannelIterator channelIterator9 = (ChannelIterator) this.L$1;
                ProducerScope producerScope5 = (ProducerScope) this.L$0;
                try {
                    if (!(obj9 instanceof Failure)) {
                        obj2 = coroutine_suspended;
                        receiveChannel5 = receiveChannel12;
                        th3 = th7;
                        channelsKt__Channels_commonKt$zip$24 = channelsKt__Channels_commonKt$zip$27;
                        receiveChannel4 = receiveChannel13;
                        channelIterator3 = channelIterator9;
                        producerScope2 = producerScope5;
                        channelsKt__Channels_commonKt$zip$22 = this;
                        receiveChannel3 = receiveChannel5;
                        th2 = th3;
                        channelsKt__Channels_commonKt$zip$2 = channelsKt__Channels_commonKt$zip$24;
                        receiveChannel2 = receiveChannel4;
                        channelIterator = channelIterator3;
                        producerScope = producerScope2;
                        channelsKt__Channels_commonKt$zip$23 = channelsKt__Channels_commonKt$zip$22;
                        coroutine_suspended = obj2;
                        i2 = 2;
                        i3 = 1;
                    } else {
                        throw ((Failure) obj9).exception;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    try {
                        throw th;
                    } catch (Throwable th9) {
                        Throwable th10 = th9;
                        receiveChannel.cancel(th);
                        throw th10;
                    }
                }
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } else if (!(obj9 instanceof Failure)) {
            ProducerScope producerScope6 = this.p$;
            ChannelIterator it = this.$other.iterator();
            receiveChannel = this.$this_zip;
            Throwable th11 = null;
            producerScope = producerScope6;
            channelsKt__Channels_commonKt$zip$23 = this;
            channelsKt__Channels_commonKt$zip$2 = channelsKt__Channels_commonKt$zip$23;
            channelIterator = it;
            channelIterator2 = receiveChannel.iterator();
            receiveChannel2 = receiveChannel;
            th2 = th11;
            receiveChannel3 = receiveChannel2;
        } else {
            throw ((Failure) obj9).exception;
        }
        channelsKt__Channels_commonKt$zip$23.L$0 = producerScope;
        channelsKt__Channels_commonKt$zip$23.L$1 = channelIterator;
        channelsKt__Channels_commonKt$zip$23.L$2 = receiveChannel2;
        channelsKt__Channels_commonKt$zip$23.L$3 = channelsKt__Channels_commonKt$zip$2;
        channelsKt__Channels_commonKt$zip$23.L$4 = receiveChannel;
        channelsKt__Channels_commonKt$zip$23.L$5 = th2;
        channelsKt__Channels_commonKt$zip$23.L$6 = receiveChannel3;
        channelsKt__Channels_commonKt$zip$23.L$7 = channelIterator2;
        channelsKt__Channels_commonKt$zip$23.label = i3;
        Object hasNext = channelIterator2.hasNext(channelsKt__Channels_commonKt$zip$2);
        if (hasNext == coroutine_suspended) {
            return coroutine_suspended;
        }
        Object obj16 = coroutine_suspended;
        channelsKt__Channels_commonKt$zip$22 = channelsKt__Channels_commonKt$zip$23;
        obj9 = hasNext;
        obj8 = obj16;
        if (!((Boolean) obj9).booleanValue()) {
            Unit unit2 = Unit.INSTANCE;
        }
        Unit unit22 = Unit.INSTANCE;
        receiveChannel.cancel(th2);
        return Unit.INSTANCE;
        return coroutine_suspended;
    }
}
