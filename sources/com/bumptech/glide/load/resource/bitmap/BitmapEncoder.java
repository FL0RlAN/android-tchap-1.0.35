package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.BufferedOutputStream;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.GlideTrace;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapEncoder implements ResourceEncoder<Bitmap> {
    public static final Option<CompressFormat> COMPRESSION_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionFormat");
    public static final Option<Integer> COMPRESSION_QUALITY = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionQuality", Integer.valueOf(90));
    private static final String TAG = "BitmapEncoder";
    private final ArrayPool arrayPool;

    public BitmapEncoder(ArrayPool arrayPool2) {
        this.arrayPool = arrayPool2;
    }

    @Deprecated
    public BitmapEncoder() {
        this.arrayPool = null;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:21|(2:38|39)|40|41) */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0066, code lost:
        if (r6 == null) goto L_0x0069;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:40:0x00bf */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0061 A[Catch:{ all -> 0x0057 }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00bc A[SYNTHETIC, Splitter:B:38:0x00bc] */
    public boolean encode(Resource<Bitmap> resource, File file, Options options) {
        String str = TAG;
        Bitmap bitmap = (Bitmap) resource.get();
        CompressFormat format = getFormat(bitmap, options);
        GlideTrace.beginSectionFormat("encode: [%dx%d] %s", Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()), format);
        try {
            long logTime = LogTime.getLogTime();
            int intValue = ((Integer) options.get(COMPRESSION_QUALITY)).intValue();
            boolean z = false;
            OutputStream outputStream = null;
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                try {
                    outputStream = this.arrayPool != null ? new BufferedOutputStream(fileOutputStream, this.arrayPool) : fileOutputStream;
                    bitmap.compress(format, intValue, outputStream);
                    outputStream.close();
                    z = true;
                } catch (IOException e) {
                    e = e;
                    outputStream = fileOutputStream;
                    try {
                        if (Log.isLoggable(str, 3)) {
                        }
                    } catch (Throwable th) {
                        th = th;
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    outputStream = fileOutputStream;
                    if (outputStream != null) {
                    }
                    throw th;
                }
            } catch (IOException e2) {
                e = e2;
                if (Log.isLoggable(str, 3)) {
                    Log.d(str, "Failed to encode Bitmap", e);
                }
            }
            try {
                outputStream.close();
            } catch (IOException unused) {
            }
            if (Log.isLoggable(str, 2)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Compressed with type: ");
                sb.append(format);
                sb.append(" of size ");
                sb.append(Util.getBitmapByteSize(bitmap));
                sb.append(" in ");
                sb.append(LogTime.getElapsedMillis(logTime));
                sb.append(", options format: ");
                sb.append(options.get(COMPRESSION_FORMAT));
                sb.append(", hasAlpha: ");
                sb.append(bitmap.hasAlpha());
                Log.v(str, sb.toString());
            }
            return z;
        } finally {
            GlideTrace.endSection();
        }
    }

    private CompressFormat getFormat(Bitmap bitmap, Options options) {
        CompressFormat compressFormat = (CompressFormat) options.get(COMPRESSION_FORMAT);
        if (compressFormat != null) {
            return compressFormat;
        }
        if (bitmap.hasAlpha()) {
            return CompressFormat.PNG;
        }
        return CompressFormat.JPEG;
    }

    public EncodeStrategy getEncodeStrategy(Options options) {
        return EncodeStrategy.TRANSFORMED;
    }
}
