package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.Result.Failure;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.DelayKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.EventLoopKt;
import kotlinx.coroutines.GlobalScope;
import kotlinx.coroutines.TimeSourceKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a/\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H@ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a/\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H@ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a4\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0007\u0002\u0004\n\u0002\b\u0019¨\u0006\u000f"}, d2 = {"fixedDelayTicker", "", "delayMillis", "", "initialDelayMillis", "channel", "Lkotlinx/coroutines/channels/SendChannel;", "(JJLkotlinx/coroutines/channels/SendChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fixedPeriodTicker", "ticker", "Lkotlinx/coroutines/channels/ReceiveChannel;", "context", "Lkotlin/coroutines/CoroutineContext;", "mode", "Lkotlinx/coroutines/channels/TickerMode;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 13})
/* compiled from: TickerChannels.kt */
public final class TickerChannelsKt {

    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 13})
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0 = new int[TickerMode.values().length];

        static {
            $EnumSwitchMapping$0[TickerMode.FIXED_PERIOD.ordinal()] = 1;
            $EnumSwitchMapping$0[TickerMode.FIXED_DELAY.ordinal()] = 2;
        }
    }

    public static /* synthetic */ ReceiveChannel ticker$default(long j, long j2, CoroutineContext coroutineContext, TickerMode tickerMode, int i, Object obj) {
        if ((i & 2) != 0) {
            j2 = j;
        }
        if ((i & 4) != 0) {
            coroutineContext = EmptyCoroutineContext.INSTANCE;
        }
        if ((i & 8) != 0) {
            tickerMode = TickerMode.FIXED_PERIOD;
        }
        return ticker(j, j2, coroutineContext, tickerMode);
    }

    public static final ReceiveChannel<Unit> ticker(long j, long j2, CoroutineContext coroutineContext, TickerMode tickerMode) {
        long j3 = j;
        long j4 = j2;
        CoroutineContext coroutineContext2 = coroutineContext;
        Intrinsics.checkParameterIsNotNull(coroutineContext2, "context");
        Intrinsics.checkParameterIsNotNull(tickerMode, "mode");
        boolean z = true;
        String str = " ms";
        if (j3 >= 0) {
            if (j4 < 0) {
                z = false;
            }
            if (z) {
                CoroutineScope coroutineScope = GlobalScope.INSTANCE;
                CoroutineContext plus = Dispatchers.getUnconfined().plus(coroutineContext2);
                TickerChannelsKt$ticker$3 tickerChannelsKt$ticker$3 = new TickerChannelsKt$ticker$3(tickerMode, j, j2, null);
                return ProduceKt.produce(coroutineScope, plus, 0, tickerChannelsKt$ticker$3);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Expected non-negative initial delay, but has ");
            sb.append(j2);
            sb.append(str);
            throw new IllegalArgumentException(sb.toString().toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Expected non-negative delay, but has ");
        sb2.append(j);
        sb2.append(str);
        throw new IllegalArgumentException(sb2.toString().toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00ac  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00ed A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00ee  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0177 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0178  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x002b  */
    static final /* synthetic */ Object fixedPeriodTicker(long j, long j2, SendChannel<? super Unit> sendChannel, Continuation<? super Unit> continuation) {
        TickerChannelsKt$fixedPeriodTicker$1 tickerChannelsKt$fixedPeriodTicker$1;
        Object coroutine_suspended;
        int i;
        long j3;
        long j4;
        long j5;
        SendChannel<? super Unit> sendChannel2;
        Object obj;
        long j6;
        long j7;
        long j8;
        SendChannel<? super Unit> sendChannel3;
        long j9;
        long j10;
        long j11;
        SendChannel<? super Unit> sendChannel4;
        long j12;
        long j13;
        long j14;
        long coerceAtLeast;
        long delayNanosToMillis;
        Unit unit;
        long j15 = j2;
        Continuation<? super Unit> continuation2 = continuation;
        if (continuation2 instanceof TickerChannelsKt$fixedPeriodTicker$1) {
            tickerChannelsKt$fixedPeriodTicker$1 = (TickerChannelsKt$fixedPeriodTicker$1) continuation2;
            if ((tickerChannelsKt$fixedPeriodTicker$1.label & Integer.MIN_VALUE) != 0) {
                tickerChannelsKt$fixedPeriodTicker$1.label -= Integer.MIN_VALUE;
                Object obj2 = tickerChannelsKt$fixedPeriodTicker$1.result;
                coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = tickerChannelsKt$fixedPeriodTicker$1.label;
                int i2 = 2;
                if (i == 0) {
                    if (i == 1) {
                        long j16 = tickerChannelsKt$fixedPeriodTicker$1.J$2;
                        sendChannel2 = (SendChannel) tickerChannelsKt$fixedPeriodTicker$1.L$0;
                        long j17 = tickerChannelsKt$fixedPeriodTicker$1.J$1;
                        j4 = tickerChannelsKt$fixedPeriodTicker$1.J$0;
                        if (!(obj2 instanceof Failure)) {
                            long j18 = j16;
                            j15 = j17;
                            j5 = j18;
                        } else {
                            throw ((Failure) obj2).exception;
                        }
                    } else if (i == 2) {
                        j9 = tickerChannelsKt$fixedPeriodTicker$1.J$3;
                        j14 = tickerChannelsKt$fixedPeriodTicker$1.J$2;
                        sendChannel2 = (SendChannel) tickerChannelsKt$fixedPeriodTicker$1.L$0;
                        j11 = tickerChannelsKt$fixedPeriodTicker$1.J$1;
                        j7 = tickerChannelsKt$fixedPeriodTicker$1.J$0;
                        if (obj2 instanceof Failure) {
                            throw ((Failure) obj2).exception;
                        }
                        long nanoTime = TimeSourceKt.getTimeSource().nanoTime();
                        obj = coroutine_suspended;
                        SendChannel<? super Unit> sendChannel5 = sendChannel2;
                        long j19 = j14;
                        coerceAtLeast = RangesKt.coerceAtLeast(j14 - nanoTime, 0);
                        if (coerceAtLeast == 0) {
                        }
                        SendChannel<? super Unit> sendChannel6 = sendChannel5;
                        long j20 = coerceAtLeast;
                        long j21 = nanoTime;
                        long j22 = j20;
                        delayNanosToMillis = EventLoopKt.delayNanosToMillis(j22);
                        tickerChannelsKt$fixedPeriodTicker$1.J$0 = j7;
                        tickerChannelsKt$fixedPeriodTicker$1.J$1 = j11;
                        tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel6;
                        SendChannel<? super Unit> sendChannel7 = sendChannel6;
                        long j23 = j11;
                        long j24 = j19;
                        tickerChannelsKt$fixedPeriodTicker$1.J$2 = j24;
                        tickerChannelsKt$fixedPeriodTicker$1.J$3 = j9;
                        tickerChannelsKt$fixedPeriodTicker$1.J$4 = j21;
                        tickerChannelsKt$fixedPeriodTicker$1.J$5 = j22;
                        tickerChannelsKt$fixedPeriodTicker$1.label = 4;
                        if (DelayKt.delay(delayNanosToMillis, tickerChannelsKt$fixedPeriodTicker$1) == obj) {
                        }
                        return obj;
                    } else if (i == 3) {
                        long j25 = tickerChannelsKt$fixedPeriodTicker$1.J$6;
                        long j26 = tickerChannelsKt$fixedPeriodTicker$1.J$5;
                        long j27 = tickerChannelsKt$fixedPeriodTicker$1.J$4;
                        long j28 = tickerChannelsKt$fixedPeriodTicker$1.J$3;
                        long j29 = tickerChannelsKt$fixedPeriodTicker$1.J$2;
                        SendChannel<? super Unit> sendChannel8 = (SendChannel) tickerChannelsKt$fixedPeriodTicker$1.L$0;
                        j11 = tickerChannelsKt$fixedPeriodTicker$1.J$1;
                        j7 = tickerChannelsKt$fixedPeriodTicker$1.J$0;
                        if (!(obj2 instanceof Failure)) {
                            obj = coroutine_suspended;
                            sendChannel4 = sendChannel8;
                            j12 = j28;
                            j13 = j29;
                            sendChannel3 = sendChannel4;
                            j8 = j13;
                            j6 = j11;
                            j10 = j7;
                            j3 = j12;
                            coroutine_suspended = obj;
                            i2 = 2;
                            j14 = j5 + j3;
                            unit = Unit.INSTANCE;
                            tickerChannelsKt$fixedPeriodTicker$1.J$0 = j4;
                            tickerChannelsKt$fixedPeriodTicker$1.J$1 = j6;
                            tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel2;
                            tickerChannelsKt$fixedPeriodTicker$1.J$2 = j14;
                            tickerChannelsKt$fixedPeriodTicker$1.J$3 = j3;
                            tickerChannelsKt$fixedPeriodTicker$1.label = i2;
                            if (sendChannel2.send(unit, tickerChannelsKt$fixedPeriodTicker$1) != coroutine_suspended) {
                            }
                            return coroutine_suspended;
                        }
                        throw ((Failure) obj2).exception;
                    } else if (i == 4) {
                        long j30 = tickerChannelsKt$fixedPeriodTicker$1.J$5;
                        long j31 = tickerChannelsKt$fixedPeriodTicker$1.J$4;
                        j9 = tickerChannelsKt$fixedPeriodTicker$1.J$3;
                        j8 = tickerChannelsKt$fixedPeriodTicker$1.J$2;
                        sendChannel3 = (SendChannel) tickerChannelsKt$fixedPeriodTicker$1.L$0;
                        long j32 = tickerChannelsKt$fixedPeriodTicker$1.J$1;
                        j7 = tickerChannelsKt$fixedPeriodTicker$1.J$0;
                        if (!(obj2 instanceof Failure)) {
                            obj = coroutine_suspended;
                            long j33 = j32;
                            j10 = j7;
                            j3 = j9;
                            j6 = j33;
                            coroutine_suspended = obj;
                            i2 = 2;
                            j14 = j5 + j3;
                            unit = Unit.INSTANCE;
                            tickerChannelsKt$fixedPeriodTicker$1.J$0 = j4;
                            tickerChannelsKt$fixedPeriodTicker$1.J$1 = j6;
                            tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel2;
                            tickerChannelsKt$fixedPeriodTicker$1.J$2 = j14;
                            tickerChannelsKt$fixedPeriodTicker$1.J$3 = j3;
                            tickerChannelsKt$fixedPeriodTicker$1.label = i2;
                            if (sendChannel2.send(unit, tickerChannelsKt$fixedPeriodTicker$1) != coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            long j34 = j6;
                            j9 = j3;
                            j7 = j4;
                            j11 = j34;
                            long nanoTime2 = TimeSourceKt.getTimeSource().nanoTime();
                            obj = coroutine_suspended;
                            SendChannel<? super Unit> sendChannel52 = sendChannel2;
                            long j192 = j14;
                            coerceAtLeast = RangesKt.coerceAtLeast(j14 - nanoTime2, 0);
                            if (coerceAtLeast == 0 || j9 == 0) {
                                SendChannel<? super Unit> sendChannel62 = sendChannel52;
                                long j202 = coerceAtLeast;
                                long j212 = nanoTime2;
                                long j222 = j202;
                                delayNanosToMillis = EventLoopKt.delayNanosToMillis(j222);
                                tickerChannelsKt$fixedPeriodTicker$1.J$0 = j7;
                                tickerChannelsKt$fixedPeriodTicker$1.J$1 = j11;
                                tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel62;
                                SendChannel<? super Unit> sendChannel72 = sendChannel62;
                                long j232 = j11;
                                long j242 = j192;
                                tickerChannelsKt$fixedPeriodTicker$1.J$2 = j242;
                                tickerChannelsKt$fixedPeriodTicker$1.J$3 = j9;
                                tickerChannelsKt$fixedPeriodTicker$1.J$4 = j212;
                                tickerChannelsKt$fixedPeriodTicker$1.J$5 = j222;
                                tickerChannelsKt$fixedPeriodTicker$1.label = 4;
                                if (DelayKt.delay(delayNanosToMillis, tickerChannelsKt$fixedPeriodTicker$1) == obj) {
                                    return obj;
                                }
                                sendChannel3 = sendChannel72;
                                j8 = j242;
                                j32 = j232;
                                long j332 = j32;
                                j10 = j7;
                                j3 = j9;
                                j6 = j332;
                                coroutine_suspended = obj;
                                i2 = 2;
                                j14 = j5 + j3;
                                unit = Unit.INSTANCE;
                                tickerChannelsKt$fixedPeriodTicker$1.J$0 = j4;
                                tickerChannelsKt$fixedPeriodTicker$1.J$1 = j6;
                                tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel2;
                                tickerChannelsKt$fixedPeriodTicker$1.J$2 = j14;
                                tickerChannelsKt$fixedPeriodTicker$1.J$3 = j3;
                                tickerChannelsKt$fixedPeriodTicker$1.label = i2;
                                if (sendChannel2.send(unit, tickerChannelsKt$fixedPeriodTicker$1) != coroutine_suspended) {
                                }
                                return obj;
                            }
                            long j35 = j9 - ((nanoTime2 - j192) % j9);
                            long j36 = coerceAtLeast;
                            long j37 = nanoTime2 + j35;
                            long j38 = nanoTime2;
                            long delayNanosToMillis2 = EventLoopKt.delayNanosToMillis(j35);
                            tickerChannelsKt$fixedPeriodTicker$1.J$0 = j7;
                            tickerChannelsKt$fixedPeriodTicker$1.J$1 = j11;
                            sendChannel4 = sendChannel52;
                            tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel4;
                            tickerChannelsKt$fixedPeriodTicker$1.J$2 = j37;
                            tickerChannelsKt$fixedPeriodTicker$1.J$3 = j9;
                            long j39 = j37;
                            tickerChannelsKt$fixedPeriodTicker$1.J$4 = j38;
                            tickerChannelsKt$fixedPeriodTicker$1.J$5 = j36;
                            tickerChannelsKt$fixedPeriodTicker$1.J$6 = j35;
                            tickerChannelsKt$fixedPeriodTicker$1.label = 3;
                            if (DelayKt.delay(delayNanosToMillis2, tickerChannelsKt$fixedPeriodTicker$1) == obj) {
                                return obj;
                            }
                            j12 = j9;
                            j13 = j39;
                            sendChannel3 = sendChannel4;
                            j8 = j13;
                            j6 = j11;
                            j10 = j7;
                            j3 = j12;
                            coroutine_suspended = obj;
                            i2 = 2;
                            j14 = j5 + j3;
                            unit = Unit.INSTANCE;
                            tickerChannelsKt$fixedPeriodTicker$1.J$0 = j4;
                            tickerChannelsKt$fixedPeriodTicker$1.J$1 = j6;
                            tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel2;
                            tickerChannelsKt$fixedPeriodTicker$1.J$2 = j14;
                            tickerChannelsKt$fixedPeriodTicker$1.J$3 = j3;
                            tickerChannelsKt$fixedPeriodTicker$1.label = i2;
                            if (sendChannel2.send(unit, tickerChannelsKt$fixedPeriodTicker$1) != coroutine_suspended) {
                            }
                            return obj;
                            SendChannel<? super Unit> sendChannel622 = sendChannel52;
                            long j2022 = coerceAtLeast;
                            long j2122 = nanoTime2;
                            long j2222 = j2022;
                            delayNanosToMillis = EventLoopKt.delayNanosToMillis(j2222);
                            tickerChannelsKt$fixedPeriodTicker$1.J$0 = j7;
                            tickerChannelsKt$fixedPeriodTicker$1.J$1 = j11;
                            tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel622;
                            SendChannel<? super Unit> sendChannel722 = sendChannel622;
                            long j2322 = j11;
                            long j2422 = j192;
                            tickerChannelsKt$fixedPeriodTicker$1.J$2 = j2422;
                            tickerChannelsKt$fixedPeriodTicker$1.J$3 = j9;
                            tickerChannelsKt$fixedPeriodTicker$1.J$4 = j2122;
                            tickerChannelsKt$fixedPeriodTicker$1.J$5 = j2222;
                            tickerChannelsKt$fixedPeriodTicker$1.label = 4;
                            if (DelayKt.delay(delayNanosToMillis, tickerChannelsKt$fixedPeriodTicker$1) == obj) {
                            }
                            return obj;
                            return coroutine_suspended;
                        }
                        throw ((Failure) obj2).exception;
                    } else {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                } else if (!(obj2 instanceof Failure)) {
                    long nanoTime3 = TimeSourceKt.getTimeSource().nanoTime() + EventLoopKt.delayToNanos(j2);
                    long j40 = j;
                    tickerChannelsKt$fixedPeriodTicker$1.J$0 = j40;
                    tickerChannelsKt$fixedPeriodTicker$1.J$1 = j15;
                    sendChannel2 = sendChannel;
                    tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel2;
                    tickerChannelsKt$fixedPeriodTicker$1.J$2 = nanoTime3;
                    tickerChannelsKt$fixedPeriodTicker$1.label = 1;
                    if (DelayKt.delay(j15, tickerChannelsKt$fixedPeriodTicker$1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    j5 = nanoTime3;
                    j4 = j40;
                } else {
                    throw ((Failure) obj2).exception;
                }
                j3 = EventLoopKt.delayToNanos(j4);
                j14 = j5 + j3;
                unit = Unit.INSTANCE;
                tickerChannelsKt$fixedPeriodTicker$1.J$0 = j4;
                tickerChannelsKt$fixedPeriodTicker$1.J$1 = j6;
                tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel2;
                tickerChannelsKt$fixedPeriodTicker$1.J$2 = j14;
                tickerChannelsKt$fixedPeriodTicker$1.J$3 = j3;
                tickerChannelsKt$fixedPeriodTicker$1.label = i2;
                if (sendChannel2.send(unit, tickerChannelsKt$fixedPeriodTicker$1) != coroutine_suspended) {
                }
                return coroutine_suspended;
            }
        }
        tickerChannelsKt$fixedPeriodTicker$1 = new TickerChannelsKt$fixedPeriodTicker$1(continuation2);
        Object obj22 = tickerChannelsKt$fixedPeriodTicker$1.result;
        coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = tickerChannelsKt$fixedPeriodTicker$1.label;
        int i22 = 2;
        if (i == 0) {
        }
        j3 = EventLoopKt.delayToNanos(j4);
        j14 = j5 + j3;
        unit = Unit.INSTANCE;
        tickerChannelsKt$fixedPeriodTicker$1.J$0 = j4;
        tickerChannelsKt$fixedPeriodTicker$1.J$1 = j6;
        tickerChannelsKt$fixedPeriodTicker$1.L$0 = sendChannel2;
        tickerChannelsKt$fixedPeriodTicker$1.J$2 = j14;
        tickerChannelsKt$fixedPeriodTicker$1.J$3 = j3;
        tickerChannelsKt$fixedPeriodTicker$1.label = i22;
        if (sendChannel2.send(unit, tickerChannelsKt$fixedPeriodTicker$1) != coroutine_suspended) {
        }
        return coroutine_suspended;
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x006f  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0092 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0093  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00a5 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026  */
    static final /* synthetic */ Object fixedDelayTicker(long j, long j2, SendChannel<? super Unit> sendChannel, Continuation<? super Unit> continuation) {
        TickerChannelsKt$fixedDelayTicker$1 tickerChannelsKt$fixedDelayTicker$1;
        Object coroutine_suspended;
        int i;
        long j3;
        long j4;
        SendChannel<? super Unit> sendChannel2;
        Unit unit;
        if (continuation instanceof TickerChannelsKt$fixedDelayTicker$1) {
            tickerChannelsKt$fixedDelayTicker$1 = (TickerChannelsKt$fixedDelayTicker$1) continuation;
            if ((tickerChannelsKt$fixedDelayTicker$1.label & Integer.MIN_VALUE) != 0) {
                tickerChannelsKt$fixedDelayTicker$1.label -= Integer.MIN_VALUE;
                Object obj = tickerChannelsKt$fixedDelayTicker$1.result;
                coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                i = tickerChannelsKt$fixedDelayTicker$1.label;
                if (i == 0) {
                    if (i == 1) {
                        sendChannel = (SendChannel) tickerChannelsKt$fixedDelayTicker$1.L$0;
                        j2 = tickerChannelsKt$fixedDelayTicker$1.J$1;
                        j = tickerChannelsKt$fixedDelayTicker$1.J$0;
                        if (obj instanceof Failure) {
                            throw ((Failure) obj).exception;
                        }
                        unit = Unit.INSTANCE;
                        tickerChannelsKt$fixedDelayTicker$1.J$0 = j;
                        tickerChannelsKt$fixedDelayTicker$1.J$1 = j2;
                        tickerChannelsKt$fixedDelayTicker$1.L$0 = sendChannel;
                        tickerChannelsKt$fixedDelayTicker$1.label = 2;
                        if (sendChannel.send(unit, tickerChannelsKt$fixedDelayTicker$1) != coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        long j5 = j;
                        sendChannel2 = sendChannel;
                        j4 = j2;
                        j3 = j5;
                        tickerChannelsKt$fixedDelayTicker$1.J$0 = j3;
                        tickerChannelsKt$fixedDelayTicker$1.J$1 = j4;
                        tickerChannelsKt$fixedDelayTicker$1.L$0 = sendChannel2;
                        tickerChannelsKt$fixedDelayTicker$1.label = 3;
                        if (DelayKt.delay(j3, tickerChannelsKt$fixedDelayTicker$1) == coroutine_suspended) {
                        }
                        return coroutine_suspended;
                    } else if (i == 2) {
                        sendChannel2 = (SendChannel) tickerChannelsKt$fixedDelayTicker$1.L$0;
                        j4 = tickerChannelsKt$fixedDelayTicker$1.J$1;
                        j3 = tickerChannelsKt$fixedDelayTicker$1.J$0;
                        if (obj instanceof Failure) {
                            throw ((Failure) obj).exception;
                        }
                        tickerChannelsKt$fixedDelayTicker$1.J$0 = j3;
                        tickerChannelsKt$fixedDelayTicker$1.J$1 = j4;
                        tickerChannelsKt$fixedDelayTicker$1.L$0 = sendChannel2;
                        tickerChannelsKt$fixedDelayTicker$1.label = 3;
                        if (DelayKt.delay(j3, tickerChannelsKt$fixedDelayTicker$1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } else if (i == 3) {
                        sendChannel2 = (SendChannel) tickerChannelsKt$fixedDelayTicker$1.L$0;
                        j4 = tickerChannelsKt$fixedDelayTicker$1.J$1;
                        j3 = tickerChannelsKt$fixedDelayTicker$1.J$0;
                        if (obj instanceof Failure) {
                            throw ((Failure) obj).exception;
                        }
                    } else {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                } else if (!(obj instanceof Failure)) {
                    tickerChannelsKt$fixedDelayTicker$1.J$0 = j;
                    tickerChannelsKt$fixedDelayTicker$1.J$1 = j2;
                    tickerChannelsKt$fixedDelayTicker$1.L$0 = sendChannel;
                    tickerChannelsKt$fixedDelayTicker$1.label = 1;
                    if (DelayKt.delay(j2, tickerChannelsKt$fixedDelayTicker$1) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    unit = Unit.INSTANCE;
                    tickerChannelsKt$fixedDelayTicker$1.J$0 = j;
                    tickerChannelsKt$fixedDelayTicker$1.J$1 = j2;
                    tickerChannelsKt$fixedDelayTicker$1.L$0 = sendChannel;
                    tickerChannelsKt$fixedDelayTicker$1.label = 2;
                    if (sendChannel.send(unit, tickerChannelsKt$fixedDelayTicker$1) != coroutine_suspended) {
                    }
                    return coroutine_suspended;
                } else {
                    throw ((Failure) obj).exception;
                }
                sendChannel = sendChannel2;
                j2 = j4;
                j = j3;
                unit = Unit.INSTANCE;
                tickerChannelsKt$fixedDelayTicker$1.J$0 = j;
                tickerChannelsKt$fixedDelayTicker$1.J$1 = j2;
                tickerChannelsKt$fixedDelayTicker$1.L$0 = sendChannel;
                tickerChannelsKt$fixedDelayTicker$1.label = 2;
                if (sendChannel.send(unit, tickerChannelsKt$fixedDelayTicker$1) != coroutine_suspended) {
                }
                return coroutine_suspended;
            }
        }
        tickerChannelsKt$fixedDelayTicker$1 = new TickerChannelsKt$fixedDelayTicker$1(continuation);
        Object obj2 = tickerChannelsKt$fixedDelayTicker$1.result;
        coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        i = tickerChannelsKt$fixedDelayTicker$1.label;
        if (i == 0) {
        }
        sendChannel = sendChannel2;
        j2 = j4;
        j = j3;
        unit = Unit.INSTANCE;
        tickerChannelsKt$fixedDelayTicker$1.J$0 = j;
        tickerChannelsKt$fixedDelayTicker$1.J$1 = j2;
        tickerChannelsKt$fixedDelayTicker$1.L$0 = sendChannel;
        tickerChannelsKt$fixedDelayTicker$1.label = 2;
        if (sendChannel.send(unit, tickerChannelsKt$fixedDelayTicker$1) != coroutine_suspended) {
        }
        return coroutine_suspended;
    }
}
