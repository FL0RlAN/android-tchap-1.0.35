package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000\u001a\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0006\u001a\u001b\u0010\u0004\u001a\u0004\u0018\u00010\u0005*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\t\u001a\u0013\u0010\n\u001a\u0004\u0018\u00010\b*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u000b\u001a\u001b\u0010\n\u001a\u0004\u0018\u00010\b*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\f\u001a\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u000f\u001a\u001b\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\u0010\u001a\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0012*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0013\u001a\u001b\u0010\u0011\u001a\u0004\u0018\u00010\u0012*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\u0014¨\u0006\u0015"}, d2 = {"numberFormatError", "", "input", "", "toByteOrNull", "", "(Ljava/lang/String;)Ljava/lang/Byte;", "radix", "", "(Ljava/lang/String;I)Ljava/lang/Byte;", "toIntOrNull", "(Ljava/lang/String;)Ljava/lang/Integer;", "(Ljava/lang/String;I)Ljava/lang/Integer;", "toLongOrNull", "", "(Ljava/lang/String;)Ljava/lang/Long;", "(Ljava/lang/String;I)Ljava/lang/Long;", "toShortOrNull", "", "(Ljava/lang/String;)Ljava/lang/Short;", "(Ljava/lang/String;I)Ljava/lang/Short;", "kotlin-stdlib"}, k = 5, mv = {1, 1, 15}, xi = 1, xs = "kotlin/text/StringsKt")
/* compiled from: StringNumberConversions.kt */
class StringsKt__StringNumberConversionsKt extends StringsKt__StringNumberConversionsJVMKt {
    public static final Byte toByteOrNull(String str) {
        Intrinsics.checkParameterIsNotNull(str, "$this$toByteOrNull");
        return StringsKt.toByteOrNull(str, 10);
    }

    public static final Byte toByteOrNull(String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "$this$toByteOrNull");
        Integer intOrNull = StringsKt.toIntOrNull(str, i);
        if (intOrNull != null) {
            int intValue = intOrNull.intValue();
            if (intValue >= -128 && intValue <= 127) {
                return Byte.valueOf((byte) intValue);
            }
        }
        return null;
    }

    public static final Short toShortOrNull(String str) {
        Intrinsics.checkParameterIsNotNull(str, "$this$toShortOrNull");
        return StringsKt.toShortOrNull(str, 10);
    }

    public static final Short toShortOrNull(String str, int i) {
        Intrinsics.checkParameterIsNotNull(str, "$this$toShortOrNull");
        Integer intOrNull = StringsKt.toIntOrNull(str, i);
        if (intOrNull != null) {
            int intValue = intOrNull.intValue();
            if (intValue >= -32768 && intValue <= 32767) {
                return Short.valueOf((short) intValue);
            }
        }
        return null;
    }

    public static final Integer toIntOrNull(String str) {
        Intrinsics.checkParameterIsNotNull(str, "$this$toIntOrNull");
        return StringsKt.toIntOrNull(str, 10);
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0037 A[LOOP:0: B:18:0x0037->B:28:0x004f, LOOP_START, PHI: r2 r3 
  PHI: (r2v2 int) = (r2v0 int), (r2v4 int) binds: [B:17:0x0035, B:28:0x004f] A[DONT_GENERATE, DONT_INLINE]
  PHI: (r3v2 int) = (r3v1 int), (r3v3 int) binds: [B:17:0x0035, B:28:0x004f] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0054  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0059  */
    public static final Integer toIntOrNull(String str, int i) {
        boolean z;
        int i2;
        int i3;
        Intrinsics.checkParameterIsNotNull(str, "$this$toIntOrNull");
        CharsKt.checkRadix(i);
        int length = str.length();
        if (length == 0) {
            return null;
        }
        int i4 = 0;
        char charAt = str.charAt(0);
        int i5 = -2147483647;
        if (charAt >= '0') {
            i2 = 0;
        } else if (length == 1) {
            return null;
        } else {
            if (charAt == '-') {
                i5 = Integer.MIN_VALUE;
                i2 = 1;
                z = true;
                int i6 = i5 / i;
                i3 = length - 1;
                if (i2 <= i3) {
                    while (true) {
                        int digitOf = CharsKt.digitOf(str.charAt(i2), i);
                        if (digitOf >= 0 && i4 >= i6) {
                            int i7 = i4 * i;
                            if (i7 >= i5 + digitOf) {
                                i4 = i7 - digitOf;
                                if (i2 == i3) {
                                    break;
                                }
                                i2++;
                            } else {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                }
                return !z ? Integer.valueOf(i4) : Integer.valueOf(-i4);
            } else if (charAt != '+') {
                return null;
            } else {
                i2 = 1;
            }
        }
        z = false;
        int i62 = i5 / i;
        i3 = length - 1;
        if (i2 <= i3) {
        }
        return !z ? Integer.valueOf(i4) : Integer.valueOf(-i4);
    }

    public static final Long toLongOrNull(String str) {
        Intrinsics.checkParameterIsNotNull(str, "$this$toLongOrNull");
        return StringsKt.toLongOrNull(str, 10);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003f A[LOOP:0: B:17:0x003f->B:29:0x0062, LOOP_START, PHI: r3 r4 r14 
  PHI: (r3v1 java.lang.Long) = (r3v0 java.lang.Long), (r3v3 java.lang.Long) binds: [B:16:0x003d, B:29:0x0062] A[DONT_GENERATE, DONT_INLINE]
  PHI: (r4v2 int) = (r4v1 int), (r4v4 int) binds: [B:16:0x003d, B:29:0x0062] A[DONT_GENERATE, DONT_INLINE]
  PHI: (r14v2 long) = (r14v0 long), (r14v4 long) binds: [B:16:0x003d, B:29:0x0062] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x006d  */
    public static final Long toLongOrNull(String str, int i) {
        boolean z;
        int i2;
        String str2 = str;
        int i3 = i;
        Intrinsics.checkParameterIsNotNull(str2, "$this$toLongOrNull");
        CharsKt.checkRadix(i);
        int length = str.length();
        Long l = null;
        if (length == 0) {
            return null;
        }
        int i4 = 0;
        char charAt = str2.charAt(0);
        long j = -9223372036854775807L;
        if (charAt < '0') {
            if (length == 1) {
                return null;
            }
            if (charAt == '-') {
                j = Long.MIN_VALUE;
                i4 = 1;
                z = true;
                long j2 = (long) i3;
                long j3 = j / j2;
                long j4 = 0;
                i2 = length - 1;
                if (i4 <= i2) {
                    while (true) {
                        int digitOf = CharsKt.digitOf(str2.charAt(i4), i3);
                        if (digitOf >= 0 && j4 >= j3) {
                            long j5 = j4 * j2;
                            int i5 = i4;
                            long j6 = (long) digitOf;
                            if (j5 >= j + j6) {
                                j4 = j5 - j6;
                                int i6 = i5;
                                if (i6 == i2) {
                                    break;
                                }
                                i4 = i6 + 1;
                                l = null;
                            } else {
                                return null;
                            }
                        } else {
                            return l;
                        }
                    }
                }
                return !z ? Long.valueOf(j4) : Long.valueOf(-j4);
            } else if (charAt != '+') {
                return null;
            } else {
                i4 = 1;
            }
        }
        z = false;
        long j22 = (long) i3;
        long j32 = j / j22;
        long j42 = 0;
        i2 = length - 1;
        if (i4 <= i2) {
        }
        return !z ? Long.valueOf(j42) : Long.valueOf(-j42);
    }

    public static final Void numberFormatError(String str) {
        Intrinsics.checkParameterIsNotNull(str, "input");
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid number format: '");
        sb.append(str);
        sb.append('\'');
        throw new NumberFormatException(sb.toString());
    }
}
