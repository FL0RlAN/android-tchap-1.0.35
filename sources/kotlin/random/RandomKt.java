package kotlin.random;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.LongCompanionObject;
import kotlin.ranges.IntRange;
import kotlin.ranges.LongRange;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0004H\u0007\u001a\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\fH\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0003H\u0000\u001a\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004H\u0000\u001a\u0014\u0010\r\u001a\u00020\u0003*\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000fH\u0007\u001a\u0014\u0010\u0010\u001a\u00020\u0004*\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u0011H\u0007\u001a\u0014\u0010\u0012\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u0003H\u0000¨\u0006\u0014"}, d2 = {"Random", "Lkotlin/random/Random;", "seed", "", "", "boundsErrorMessage", "", "from", "", "until", "checkRangeBounds", "", "", "nextInt", "range", "Lkotlin/ranges/IntRange;", "nextLong", "Lkotlin/ranges/LongRange;", "takeUpperBits", "bitCount", "kotlin-stdlib"}, k = 2, mv = {1, 1, 15})
/* compiled from: Random.kt */
public final class RandomKt {
    public static final int takeUpperBits(int i, int i2) {
        return (i >>> (32 - i2)) & ((-i2) >> 31);
    }

    public static final Random Random(int i) {
        return new XorWowRandom(i, i >> 31);
    }

    public static final Random Random(long j) {
        return new XorWowRandom((int) j, (int) (j >> 32));
    }

    public static final int nextInt(Random random, IntRange intRange) {
        Intrinsics.checkParameterIsNotNull(random, "$this$nextInt");
        Intrinsics.checkParameterIsNotNull(intRange, "range");
        if (intRange.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot get random in empty range: ");
            sb.append(intRange);
            throw new IllegalArgumentException(sb.toString());
        } else if (intRange.getLast() < Integer.MAX_VALUE) {
            return random.nextInt(intRange.getFirst(), intRange.getLast() + 1);
        } else {
            if (intRange.getFirst() > Integer.MIN_VALUE) {
                return random.nextInt(intRange.getFirst() - 1, intRange.getLast()) + 1;
            }
            return random.nextInt();
        }
    }

    public static final long nextLong(Random random, LongRange longRange) {
        Intrinsics.checkParameterIsNotNull(random, "$this$nextLong");
        Intrinsics.checkParameterIsNotNull(longRange, "range");
        if (longRange.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot get random in empty range: ");
            sb.append(longRange);
            throw new IllegalArgumentException(sb.toString());
        } else if (longRange.getLast() < LongCompanionObject.MAX_VALUE) {
            return random.nextLong(longRange.getStart().longValue(), longRange.getEndInclusive().longValue() + 1);
        } else {
            if (longRange.getStart().longValue() > Long.MIN_VALUE) {
                return random.nextLong(longRange.getStart().longValue() - 1, longRange.getEndInclusive().longValue()) + 1;
            }
            return random.nextLong();
        }
    }

    public static final void checkRangeBounds(int i, int i2) {
        if (!(i2 > i)) {
            throw new IllegalArgumentException(boundsErrorMessage(Integer.valueOf(i), Integer.valueOf(i2)).toString());
        }
    }

    public static final void checkRangeBounds(long j, long j2) {
        if (!(j2 > j)) {
            throw new IllegalArgumentException(boundsErrorMessage(Long.valueOf(j), Long.valueOf(j2)).toString());
        }
    }

    public static final void checkRangeBounds(double d, double d2) {
        if (!(d2 > d)) {
            throw new IllegalArgumentException(boundsErrorMessage(Double.valueOf(d), Double.valueOf(d2)).toString());
        }
    }

    public static final String boundsErrorMessage(Object obj, Object obj2) {
        Intrinsics.checkParameterIsNotNull(obj, "from");
        Intrinsics.checkParameterIsNotNull(obj2, "until");
        StringBuilder sb = new StringBuilder();
        sb.append("Random range is empty: [");
        sb.append(obj);
        sb.append(", ");
        sb.append(obj2);
        sb.append(").");
        return sb.toString();
    }
}