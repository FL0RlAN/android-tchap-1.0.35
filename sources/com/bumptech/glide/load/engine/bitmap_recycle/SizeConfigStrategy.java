package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import com.bumptech.glide.util.Util;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

public class SizeConfigStrategy implements LruPoolStrategy {
    private static final Config[] ALPHA_8_IN_CONFIGS = {Config.ALPHA_8};
    private static final Config[] ARGB_4444_IN_CONFIGS = {Config.ARGB_4444};
    private static final Config[] ARGB_8888_IN_CONFIGS;
    private static final int MAX_SIZE_MULTIPLE = 8;
    private static final Config[] RGBA_F16_IN_CONFIGS = ARGB_8888_IN_CONFIGS;
    private static final Config[] RGB_565_IN_CONFIGS = {Config.RGB_565};
    private final GroupedLinkedMap<Key, Bitmap> groupedMap = new GroupedLinkedMap<>();
    private final KeyPool keyPool = new KeyPool();
    private final Map<Config, NavigableMap<Integer, Integer>> sortedSizes = new HashMap();

    /* renamed from: com.bumptech.glide.load.engine.bitmap_recycle.SizeConfigStrategy$1 reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Config.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        static {
            $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_8888.ordinal()] = 1;
            $SwitchMap$android$graphics$Bitmap$Config[Config.RGB_565.ordinal()] = 2;
            $SwitchMap$android$graphics$Bitmap$Config[Config.ARGB_4444.ordinal()] = 3;
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Config.ALPHA_8.ordinal()] = 4;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    static final class Key implements Poolable {
        private Config config;
        private final KeyPool pool;
        int size;

        public Key(KeyPool keyPool) {
            this.pool = keyPool;
        }

        Key(KeyPool keyPool, int i, Config config2) {
            this(keyPool);
            init(i, config2);
        }

        public void init(int i, Config config2) {
            this.size = i;
            this.config = config2;
        }

        public void offer() {
            this.pool.offer(this);
        }

        public String toString() {
            return SizeConfigStrategy.getBitmapString(this.size, this.config);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }
            Key key = (Key) obj;
            if (this.size != key.size || !Util.bothNullOrEqual(this.config, key.config)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int i = this.size * 31;
            Config config2 = this.config;
            return i + (config2 != null ? config2.hashCode() : 0);
        }
    }

    static class KeyPool extends BaseKeyPool<Key> {
        KeyPool() {
        }

        public Key get(int i, Config config) {
            Key key = (Key) get();
            key.init(i, config);
            return key;
        }

        /* access modifiers changed from: protected */
        public Key create() {
            return new Key(this);
        }
    }

    static {
        Config[] configArr = {Config.ARGB_8888, null};
        if (VERSION.SDK_INT >= 26) {
            configArr = (Config[]) Arrays.copyOf(configArr, configArr.length + 1);
            configArr[configArr.length - 1] = Config.RGBA_F16;
        }
        ARGB_8888_IN_CONFIGS = configArr;
    }

    public void put(Bitmap bitmap) {
        Key key = this.keyPool.get(Util.getBitmapByteSize(bitmap), bitmap.getConfig());
        this.groupedMap.put(key, bitmap);
        NavigableMap sizesForConfig = getSizesForConfig(bitmap.getConfig());
        Integer num = (Integer) sizesForConfig.get(Integer.valueOf(key.size));
        Integer valueOf = Integer.valueOf(key.size);
        int i = 1;
        if (num != null) {
            i = 1 + num.intValue();
        }
        sizesForConfig.put(valueOf, Integer.valueOf(i));
    }

    public Bitmap get(int i, int i2, Config config) {
        Key findBestKey = findBestKey(Util.getBitmapByteSize(i, i2, config), config);
        Bitmap bitmap = (Bitmap) this.groupedMap.get(findBestKey);
        if (bitmap != null) {
            decrementBitmapOfSize(Integer.valueOf(findBestKey.size), bitmap);
            bitmap.reconfigure(i, i2, bitmap.getConfig() != null ? bitmap.getConfig() : Config.ARGB_8888);
        }
        return bitmap;
    }

    private Key findBestKey(int i, Config config) {
        Key key = this.keyPool.get(i, config);
        Config[] inConfigs = getInConfigs(config);
        int length = inConfigs.length;
        int i2 = 0;
        while (i2 < length) {
            Config config2 = inConfigs[i2];
            Integer num = (Integer) getSizesForConfig(config2).ceilingKey(Integer.valueOf(i));
            if (num == null || num.intValue() > i * 8) {
                i2++;
            } else {
                if (num.intValue() == i) {
                    if (config2 == null) {
                        if (config == null) {
                            return key;
                        }
                    } else if (config2.equals(config)) {
                        return key;
                    }
                }
                this.keyPool.offer(key);
                return this.keyPool.get(num.intValue(), config2);
            }
        }
        return key;
    }

    public Bitmap removeLast() {
        Bitmap bitmap = (Bitmap) this.groupedMap.removeLast();
        if (bitmap != null) {
            decrementBitmapOfSize(Integer.valueOf(Util.getBitmapByteSize(bitmap)), bitmap);
        }
        return bitmap;
    }

    private void decrementBitmapOfSize(Integer num, Bitmap bitmap) {
        NavigableMap sizesForConfig = getSizesForConfig(bitmap.getConfig());
        Integer num2 = (Integer) sizesForConfig.get(num);
        if (num2 == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Tried to decrement empty size, size: ");
            sb.append(num);
            sb.append(", removed: ");
            sb.append(logBitmap(bitmap));
            sb.append(", this: ");
            sb.append(this);
            throw new NullPointerException(sb.toString());
        } else if (num2.intValue() == 1) {
            sizesForConfig.remove(num);
        } else {
            sizesForConfig.put(num, Integer.valueOf(num2.intValue() - 1));
        }
    }

    private NavigableMap<Integer, Integer> getSizesForConfig(Config config) {
        NavigableMap<Integer, Integer> navigableMap = (NavigableMap) this.sortedSizes.get(config);
        if (navigableMap != null) {
            return navigableMap;
        }
        TreeMap treeMap = new TreeMap();
        this.sortedSizes.put(config, treeMap);
        return treeMap;
    }

    public String logBitmap(Bitmap bitmap) {
        return getBitmapString(Util.getBitmapByteSize(bitmap), bitmap.getConfig());
    }

    public String logBitmap(int i, int i2, Config config) {
        return getBitmapString(Util.getBitmapByteSize(i, i2, config), config);
    }

    public int getSize(Bitmap bitmap) {
        return Util.getBitmapByteSize(bitmap);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SizeConfigStrategy{groupedMap=");
        sb.append(this.groupedMap);
        sb.append(", sortedSizes=(");
        for (Entry entry : this.sortedSizes.entrySet()) {
            sb.append(entry.getKey());
            sb.append('[');
            sb.append(entry.getValue());
            sb.append("], ");
        }
        if (!this.sortedSizes.isEmpty()) {
            sb.replace(sb.length() - 2, sb.length(), "");
        }
        sb.append(")}");
        return sb.toString();
    }

    static String getBitmapString(int i, Config config) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(i);
        sb.append("](");
        sb.append(config);
        sb.append(")");
        return sb.toString();
    }

    private static Config[] getInConfigs(Config config) {
        if (VERSION.SDK_INT >= 26 && Config.RGBA_F16.equals(config)) {
            return RGBA_F16_IN_CONFIGS;
        }
        int i = AnonymousClass1.$SwitchMap$android$graphics$Bitmap$Config[config.ordinal()];
        if (i == 1) {
            return ARGB_8888_IN_CONFIGS;
        }
        if (i == 2) {
            return RGB_565_IN_CONFIGS;
        }
        if (i == 3) {
            return ARGB_4444_IN_CONFIGS;
        }
        if (i == 4) {
            return ALPHA_8_IN_CONFIGS;
        }
        return new Config[]{config};
    }
}
