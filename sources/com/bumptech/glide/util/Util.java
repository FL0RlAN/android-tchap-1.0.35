package com.bumptech.glide.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import android.os.Looper;
import com.bumptech.glide.load.model.Model;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import kotlin.UByte;

public final class Util {
    private static final int HASH_ACCUMULATOR = 17;
    private static final int HASH_MULTIPLIER = 31;
    private static final char[] HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
    private static final char[] SHA_256_CHARS = new char[64];

    /* renamed from: com.bumptech.glide.util.Util$1 reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Config.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        static {
            $SwitchMap$android$graphics$Bitmap$Config[Config.ALPHA_8.ordinal()] = 1;
            $SwitchMap$android$graphics$Bitmap$Config[Config.RGB_565.ordinal()] = 2;
            $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_4444.ordinal()] = 3;
            $SwitchMap$android$graphics$Bitmap$Config[Config.RGBA_F16.ordinal()] = 4;
            $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_8888.ordinal()] = 5;
        }
    }

    public static int hashCode(int i, int i2) {
        return (i2 * 31) + i;
    }

    private static boolean isValidDimension(int i) {
        return i > 0 || i == Integer.MIN_VALUE;
    }

    private Util() {
    }

    public static String sha256BytesToHex(byte[] bArr) {
        String bytesToHex;
        synchronized (SHA_256_CHARS) {
            bytesToHex = bytesToHex(bArr, SHA_256_CHARS);
        }
        return bytesToHex;
    }

    private static String bytesToHex(byte[] bArr, char[] cArr) {
        for (int i = 0; i < bArr.length; i++) {
            byte b = bArr[i] & UByte.MAX_VALUE;
            int i2 = i * 2;
            char[] cArr2 = HEX_CHAR_ARRAY;
            cArr[i2] = cArr2[b >>> 4];
            cArr[i2 + 1] = cArr2[b & 15];
        }
        return new String(cArr);
    }

    @Deprecated
    public static int getSize(Bitmap bitmap) {
        return getBitmapByteSize(bitmap);
    }

    public static int getBitmapByteSize(Bitmap bitmap) {
        if (!bitmap.isRecycled()) {
            if (VERSION.SDK_INT >= 19) {
                try {
                    return bitmap.getAllocationByteCount();
                } catch (NullPointerException unused) {
                }
            }
            return bitmap.getHeight() * bitmap.getRowBytes();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Cannot obtain size for recycled Bitmap: ");
        sb.append(bitmap);
        sb.append("[");
        sb.append(bitmap.getWidth());
        sb.append("x");
        sb.append(bitmap.getHeight());
        sb.append("] ");
        sb.append(bitmap.getConfig());
        throw new IllegalStateException(sb.toString());
    }

    public static int getBitmapByteSize(int i, int i2, Config config) {
        return i * i2 * getBytesPerPixel(config);
    }

    private static int getBytesPerPixel(Config config) {
        if (config == null) {
            config = Config.ARGB_8888;
        }
        int i = AnonymousClass1.$SwitchMap$android$graphics$Bitmap$Config[config.ordinal()];
        if (i == 1) {
            return 1;
        }
        if (i == 2 || i == 3) {
            return 2;
        }
        return i != 4 ? 4 : 8;
    }

    public static boolean isValidDimensions(int i, int i2) {
        return isValidDimension(i) && isValidDimension(i2);
    }

    public static void assertMainThread() {
        if (!isOnMainThread()) {
            throw new IllegalArgumentException("You must call this method on the main thread");
        }
    }

    public static void assertBackgroundThread() {
        if (!isOnBackgroundThread()) {
            throw new IllegalArgumentException("You must call this method on a background thread");
        }
    }

    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static boolean isOnBackgroundThread() {
        return !isOnMainThread();
    }

    public static <T> Queue<T> createQueue(int i) {
        return new ArrayDeque(i);
    }

    public static <T> List<T> getSnapshot(Collection<T> collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        for (Object next : collection) {
            if (next != null) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public static boolean bothNullOrEqual(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        return obj.equals(obj2);
    }

    public static boolean bothModelsNullEquivalentOrEquals(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        } else if (obj instanceof Model) {
            return ((Model) obj).isEquivalentTo(obj2);
        } else {
            return obj.equals(obj2);
        }
    }

    public static int hashCode(int i) {
        return hashCode(i, 17);
    }

    public static int hashCode(float f) {
        return hashCode(f, 17);
    }

    public static int hashCode(float f, int i) {
        return hashCode(Float.floatToIntBits(f), i);
    }

    public static int hashCode(Object obj, int i) {
        return hashCode(obj == null ? 0 : obj.hashCode(), i);
    }

    public static int hashCode(boolean z, int i) {
        return hashCode(z ? 1 : 0, i);
    }

    public static int hashCode(boolean z) {
        return hashCode(z, 17);
    }
}
