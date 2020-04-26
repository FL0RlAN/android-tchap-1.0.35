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
@DebugMetadata(c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$dropWhile$1", f = "Channels.common.kt", i = {2, 3, 6}, l = {615, 615, 616, 617, 621, 621, 622}, m = "invokeSuspend", n = {"e", "e", "e"}, s = {"L$1", "L$1", "L$1"})
/* compiled from: Channels.common.kt */
final class ChannelsKt__Channels_commonKt$dropWhile$1 extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function2 $predicate;
    final /* synthetic */ ReceiveChannel $this_dropWhile;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    private ProducerScope p$;

    ChannelsKt__Channels_commonKt$dropWhile$1(ReceiveChannel receiveChannel, Function2 function2, Continuation continuation) {
        this.$this_dropWhile = receiveChannel;
        this.$predicate = function2;
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        Intrinsics.checkParameterIsNotNull(continuation, "completion");
        ChannelsKt__Channels_commonKt$dropWhile$1 channelsKt__Channels_commonKt$dropWhile$1 = new ChannelsKt__Channels_commonKt$dropWhile$1(this.$this_dropWhile, this.$predicate, continuation);
        channelsKt__Channels_commonKt$dropWhile$1.p$ = (ProducerScope) obj;
        return channelsKt__Channels_commonKt$dropWhile$1;
    }

    public final Object invoke(Object obj, Object obj2) {
        return ((ChannelsKt__Channels_commonKt$dropWhile$1) create(obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:32:0x008c, code lost:
        r6 = r2;
        r2 = r8;
        r8 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00b8, code lost:
        r8.L$0 = r0;
        r8.L$1 = r1;
        r8.label = 1;
        r3 = r1.hasNext(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00c3, code lost:
        if (r3 != r2) goto L_0x00c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00c5, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00c6, code lost:
        r6 = r0;
        r0 = r8;
        r8 = r3;
        r3 = r2;
        r2 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00d1, code lost:
        if (((java.lang.Boolean) r8).booleanValue() == false) goto L_0x0115;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00d3, code lost:
        r0.L$0 = r2;
        r0.L$1 = r1;
        r0.label = 2;
        r8 = r1.next(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00de, code lost:
        if (r8 != r3) goto L_0x008c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00e0, code lost:
        return r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00e1, code lost:
        r4 = r0.$predicate;
        r0.L$0 = r8;
        r0.L$1 = r2;
        r0.L$2 = r1;
        r0.label = 3;
        r4 = r4.invoke(r2, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00f0, code lost:
        if (r4 != r3) goto L_0x00f3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00f2, code lost:
        return r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00f3, code lost:
        r6 = r3;
        r3 = r8;
        r8 = r4;
        r4 = r2;
        r2 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00fe, code lost:
        if (((java.lang.Boolean) r8).booleanValue() != false) goto L_0x0112;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0100, code lost:
        r0.L$0 = r3;
        r0.L$1 = r4;
        r0.label = 4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x010b, code lost:
        if (r3.send(r4, r0) != r2) goto L_0x010e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x010d, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x010e, code lost:
        r6 = r3;
        r3 = r2;
        r2 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0112, code lost:
        r8 = r0;
        r0 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0115, code lost:
        r1 = r0.$this_dropWhile.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x011c, code lost:
        r0.L$0 = r2;
        r0.L$1 = r1;
        r0.label = 5;
        r8 = r1.hasNext(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0127, code lost:
        if (r8 != r3) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0129, code lost:
        return r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0130, code lost:
        if (((java.lang.Boolean) r8).booleanValue() == false) goto L_0x0150;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x0132, code lost:
        r0.L$0 = r2;
        r0.L$1 = r1;
        r0.label = 6;
        r8 = r1.next(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x013d, code lost:
        if (r8 != r3) goto L_0x0140;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x013f, code lost:
        return r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0140, code lost:
        r0.L$0 = r2;
        r0.L$1 = r8;
        r0.L$2 = r1;
        r0.label = 7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x014d, code lost:
        if (r2.send(r8, r0) != r3) goto L_0x011c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x014f, code lost:
        return r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0152, code lost:
        return kotlin.Unit.INSTANCE;
     */
    public final Object invokeSuspend(Object obj) {
        Object obj2;
        ProducerScope producerScope;
        ChannelIterator channelIterator;
        ChannelsKt__Channels_commonKt$dropWhile$1 channelsKt__Channels_commonKt$dropWhile$1;
        Object obj3;
        ChannelIterator channelIterator2;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                if (!(obj instanceof Failure)) {
                    ProducerScope producerScope2 = this.p$;
                    channelIterator2 = this.$this_dropWhile.iterator();
                    obj3 = coroutine_suspended;
                    ProducerScope producerScope3 = producerScope2;
                    ChannelsKt__Channels_commonKt$dropWhile$1 channelsKt__Channels_commonKt$dropWhile$12 = this;
                    break;
                } else {
                    throw ((Failure) obj).exception;
                }
            case 1:
                channelIterator2 = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$dropWhile$1 = this;
                    break;
                } else {
                    throw ((Failure) obj).exception;
                }
            case 2:
                channelIterator2 = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$dropWhile$1 = this;
                    break;
                } else {
                    throw ((Failure) obj).exception;
                }
            case 3:
                channelIterator2 = (ChannelIterator) this.L$2;
                Object obj4 = this.L$1;
                ProducerScope producerScope4 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    Object obj5 = obj4;
                    obj3 = coroutine_suspended;
                    channelsKt__Channels_commonKt$dropWhile$1 = this;
                    break;
                } else {
                    throw ((Failure) obj).exception;
                }
            case 4:
                Object obj6 = this.L$1;
                ProducerScope producerScope5 = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    producerScope = producerScope5;
                    channelsKt__Channels_commonKt$dropWhile$1 = this;
                    break;
                } else {
                    throw ((Failure) obj).exception;
                }
            case 5:
                channelIterator = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$dropWhile$1 = this;
                    break;
                } else {
                    throw ((Failure) obj).exception;
                }
            case 6:
                channelIterator = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$dropWhile$1 = this;
                    break;
                } else {
                    throw ((Failure) obj).exception;
                }
            case 7:
                channelIterator = (ChannelIterator) this.L$2;
                Object obj7 = this.L$1;
                producerScope = (ProducerScope) this.L$0;
                if (!(obj instanceof Failure)) {
                    obj2 = coroutine_suspended;
                    channelsKt__Channels_commonKt$dropWhile$1 = this;
                    break;
                } else {
                    throw ((Failure) obj).exception;
                }
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
