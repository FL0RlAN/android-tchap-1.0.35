package com.bumptech.glide.load.model;

import android.util.Log;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamEncoder implements Encoder<InputStream> {
    private static final String TAG = "StreamEncoder";
    private final ArrayPool byteArrayPool;

    public StreamEncoder(ArrayPool arrayPool) {
        this.byteArrayPool = arrayPool;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0038 A[Catch:{ all -> 0x002e }] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x003f A[SYNTHETIC, Splitter:B:23:0x003f] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x004a A[SYNTHETIC, Splitter:B:29:0x004a] */
    public boolean encode(InputStream inputStream, File file, Options options) {
        String str = TAG;
        byte[] bArr = (byte[]) this.byteArrayPool.get(65536, byte[].class);
        boolean z = false;
        OutputStream outputStream = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            while (true) {
                try {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                } catch (IOException e) {
                    e = e;
                    outputStream = fileOutputStream;
                    try {
                        if (Log.isLoggable(str, 3)) {
                        }
                        if (outputStream != null) {
                        }
                        this.byteArrayPool.put(bArr);
                        return z;
                    } catch (Throwable th) {
                        th = th;
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException unused) {
                            }
                        }
                        this.byteArrayPool.put(bArr);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    outputStream = fileOutputStream;
                    if (outputStream != null) {
                    }
                    this.byteArrayPool.put(bArr);
                    throw th;
                }
            }
            fileOutputStream.close();
            z = true;
            try {
                fileOutputStream.close();
            } catch (IOException unused2) {
            }
        } catch (IOException e2) {
            e = e2;
            if (Log.isLoggable(str, 3)) {
                Log.d(str, "Failed to encode data onto the OutputStream", e);
            }
            if (outputStream != null) {
                outputStream.close();
            }
            this.byteArrayPool.put(bArr);
            return z;
        }
        this.byteArrayPool.put(bArr);
        return z;
    }
}
