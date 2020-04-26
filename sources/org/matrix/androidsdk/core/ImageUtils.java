package org.matrix.androidsdk.core;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.text.TextUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

public class ImageUtils {
    private static final String LOG_TAG = ImageUtils.class.getSimpleName();

    public static int getRotationAngleForBitmap(Context context, Uri uri) {
        int orientationForBitmap = getOrientationForBitmap(context, uri);
        if (6 == orientationForBitmap) {
            return 90;
        }
        if (3 == orientationForBitmap) {
            return 180;
        }
        return 8 == orientationForBitmap ? 270 : 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0064, code lost:
        if (r9 != null) goto L_0x0066;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0066, code lost:
        r9.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0087, code lost:
        if (r9 == null) goto L_0x00cd;
     */
    public static int getOrientationForBitmap(Context context, Uri uri) {
        int i = 0;
        if (uri == null) {
            return 0;
        }
        String str = "Orientation";
        if (TextUtils.equals(uri.getScheme(), BingRule.KIND_CONTENT)) {
            String str2 = "_data";
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, new String[]{str2}, null, null, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    String string = cursor.getString(cursor.getColumnIndexOrThrow(str2));
                    if (TextUtils.isEmpty(string)) {
                        String str3 = LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Cannot find path in media db for uri ");
                        sb.append(uri);
                        Log.w(str3, sb.toString());
                        if (cursor != null) {
                            cursor.close();
                        }
                        return 0;
                    }
                    i = new ExifInterface(string).getAttributeInt(str, 0);
                }
            } catch (Exception e) {
                String str4 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Cannot get orientation for bitmap: ");
                sb2.append(e.getMessage());
                Log.e(str4, sb2.toString(), e);
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } else if (TextUtils.equals(uri.getScheme(), "file")) {
            try {
                i = new ExifInterface(uri.getPath()).getAttributeInt(str, 0);
            } catch (Exception e2) {
                String str5 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Cannot get EXIF for file uri ");
                sb3.append(uri);
                sb3.append(" because ");
                sb3.append(e2.getMessage());
                Log.e(str5, sb3.toString(), e2);
            }
        }
        return i;
    }

    public static Options decodeBitmapDimensions(InputStream inputStream) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        if (options.outHeight != -1 && options.outWidth != -1) {
            return options;
        }
        Log.e(LOG_TAG, "Cannot resize input stream, failed to get w/h.");
        return null;
    }

    public static int getSampleSize(int i, int i2, int i3) {
        if (i2 > i) {
            i = i2;
        }
        int highestOneBit = Integer.highestOneBit((int) Math.floor(i > i3 ? (double) (i / i3) : 1.0d));
        if (highestOneBit == 0) {
            return 1;
        }
        return highestOneBit;
    }

    public static InputStream resizeImage(InputStream inputStream, int i, int i2, int i3) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[2048];
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                break;
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
        inputStream.close();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        byteArrayOutputStream.close();
        Options decodeBitmapDimensions = decodeBitmapDimensions(byteArrayInputStream);
        if (decodeBitmapDimensions == null) {
            return null;
        }
        int i4 = decodeBitmapDimensions.outWidth;
        int i5 = decodeBitmapDimensions.outHeight;
        byteArrayInputStream.reset();
        if (i != -1) {
            i2 = getSampleSize(i4, i5, i);
        }
        if (i2 == 1) {
            return byteArrayInputStream;
        }
        Options options = new Options();
        options.inSampleSize = i2;
        Bitmap decodeStream = BitmapFactory.decodeStream(byteArrayInputStream, null, options);
        if (decodeStream == null) {
            return null;
        }
        byteArrayInputStream.close();
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        decodeStream.compress(CompressFormat.JPEG, i3, byteArrayOutputStream2);
        decodeStream.recycle();
        return new ByteArrayInputStream(byteArrayOutputStream2.toByteArray());
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0083 A[Catch:{ OutOfMemoryError -> 0x00a7, Exception -> 0x008d }] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0088 A[Catch:{ OutOfMemoryError -> 0x00a7, Exception -> 0x008d }] */
    public static Bitmap rotateImage(Context context, String str, int i, MXMediaCache mXMediaCache) {
        Bitmap bitmap;
        Bitmap createBitmap;
        String str2 = "applyExifRotation ";
        try {
            Uri parse = Uri.parse(str);
            if (i == 0) {
                return null;
            }
            Options options = new Options();
            options.inPreferredConfig = Config.ARGB_8888;
            options.outWidth = -1;
            options.outHeight = -1;
            try {
                FileInputStream fileInputStream = new FileInputStream(new File(parse.getPath()));
                bitmap = BitmapFactory.decodeStream(fileInputStream, null, options);
                try {
                    fileInputStream.close();
                } catch (OutOfMemoryError e) {
                    e = e;
                } catch (Exception e2) {
                    e = e2;
                    String str3 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append(str2);
                    sb.append(e.getMessage());
                    Log.e(str3, sb.toString(), e);
                    Matrix matrix = new Matrix();
                    matrix.postRotate((float) i);
                    createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                    if (createBitmap != bitmap) {
                    }
                    if (mXMediaCache != null) {
                    }
                    return createBitmap;
                }
            } catch (OutOfMemoryError e3) {
                e = e3;
                bitmap = null;
                String str4 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("applyExifRotation BitmapFactory.decodeStream : ");
                sb2.append(e.getMessage());
                Log.e(str4, sb2.toString(), e);
                Matrix matrix2 = new Matrix();
                matrix2.postRotate((float) i);
                createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix2, false);
                if (createBitmap != bitmap) {
                }
                if (mXMediaCache != null) {
                }
                return createBitmap;
            } catch (Exception e4) {
                e = e4;
                bitmap = null;
                String str32 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str2);
                sb3.append(e.getMessage());
                Log.e(str32, sb3.toString(), e);
                Matrix matrix22 = new Matrix();
                matrix22.postRotate((float) i);
                createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix22, false);
                if (createBitmap != bitmap) {
                }
                if (mXMediaCache != null) {
                }
                return createBitmap;
            }
            Matrix matrix222 = new Matrix();
            matrix222.postRotate((float) i);
            createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix222, false);
            if (createBitmap != bitmap) {
                bitmap.recycle();
            }
            if (mXMediaCache != null) {
                mXMediaCache.saveBitmap(createBitmap, str);
            }
            return createBitmap;
        } catch (OutOfMemoryError e5) {
            String str5 = LOG_TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str2);
            sb4.append(e5.getMessage());
            Log.e(str5, sb4.toString(), e5);
            return null;
        } catch (Exception e6) {
            String str6 = LOG_TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str2);
            sb5.append(e6.getMessage());
            Log.e(str6, sb5.toString(), e6);
            return null;
        }
    }

    public static Bitmap applyExifRotation(Context context, String str, MXMediaCache mXMediaCache) {
        try {
            int rotationAngleForBitmap = getRotationAngleForBitmap(context, Uri.parse(str));
            if (rotationAngleForBitmap != 0) {
                return rotateImage(context, str, rotationAngleForBitmap, mXMediaCache);
            }
            return null;
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyExifRotation ");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString(), e);
            return null;
        }
    }

    public static String scaleAndRotateImage(Context context, InputStream inputStream, String str, int i, int i2, MXMediaCache mXMediaCache) {
        String str2 = null;
        if (context == null || inputStream == null || mXMediaCache == null) {
            return null;
        }
        try {
            str2 = mXMediaCache.saveMedia(resizeImage(inputStream, i, 0, 75), null, str);
            rotateImage(context, str2, i2, mXMediaCache);
            return str2;
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("rotateAndScale ");
            sb.append(e.getMessage());
            Log.e(str3, sb.toString(), e);
            return str2;
        }
    }
}
