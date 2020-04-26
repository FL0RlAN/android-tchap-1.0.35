package org.matrix.androidsdk.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.InputStream;

public class ResourceUtils {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = ResourceUtils.class.getSimpleName();
    public static final String MIME_TYPE_ALL_CONTENT = "*/*";
    public static final String MIME_TYPE_IMAGE_ALL = "image/*";
    public static final String MIME_TYPE_JPEG = "image/jpeg";
    public static final String MIME_TYPE_JPG = "image/jpg";

    public static class Resource {
        public InputStream mContentStream;
        public String mMimeType;

        public Resource(InputStream inputStream, String str) {
            this.mContentStream = inputStream;
            this.mMimeType = str;
        }

        public void close() {
            try {
                this.mMimeType = null;
                if (this.mContentStream != null) {
                    this.mContentStream.close();
                    this.mContentStream = null;
                }
            } catch (Exception e) {
                String access$000 = ResourceUtils.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Resource.close failed ");
                sb.append(e.getLocalizedMessage());
                Log.e(access$000, sb.toString(), e);
            }
        }

        public boolean isJpegResource() {
            if (!ResourceUtils.MIME_TYPE_JPEG.equals(this.mMimeType)) {
                if (!ResourceUtils.MIME_TYPE_JPG.equals(this.mMimeType)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static Resource openResource(Context context, Uri uri, String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                str = context.getContentResolver().getType(uri);
                if (str == null) {
                    String fileExtension = FileUtilsKt.getFileExtension(uri.toString());
                    if (fileExtension != null) {
                        str = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                    }
                }
            }
            return new Resource(context.getContentResolver().openInputStream(uri), str);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to open resource input stream", e);
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x003c A[Catch:{ Exception -> 0x00aa }] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00a4 A[Catch:{ Exception -> 0x00aa }] */
    public static Bitmap createThumbnailBitmap(Context context, Uri uri, int i, int i2) {
        Bitmap bitmap;
        Bitmap createScaledBitmap;
        Bitmap bitmap2 = null;
        if (openResource(context, uri, null) == null) {
            return null;
        }
        try {
            Options options = new Options();
            options.inPreferredConfig = Config.ARGB_8888;
            Resource openResource = openResource(context, uri, null);
            if (openResource != null) {
                try {
                    bitmap = BitmapFactory.decodeStream(openResource.mContentStream, null, options);
                } catch (Exception e) {
                    String str = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("BitmapFactory.decodeStream fails ");
                    sb.append(e.getLocalizedMessage());
                    Log.e(str, sb.toString(), e);
                }
                if (bitmap != null) {
                    if (bitmap.getHeight() >= i2 || bitmap.getWidth() >= i) {
                        double d = (double) i;
                        double d2 = (double) i2;
                        double width = (double) bitmap.getWidth();
                        double height = (double) bitmap.getHeight();
                        if (width > height) {
                            Double.isNaN(d);
                            Double.isNaN(height);
                            double d3 = height * d;
                            Double.isNaN(width);
                            d2 = d3 / width;
                        } else {
                            Double.isNaN(d2);
                            Double.isNaN(width);
                            double d4 = width * d2;
                            Double.isNaN(height);
                            d = d4 / height;
                        }
                        if (bitmap == null) {
                            bitmap = null;
                        }
                        try {
                            createScaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) d, (int) d2, false);
                        } catch (OutOfMemoryError e2) {
                            String str2 = LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("createThumbnailBitmap ");
                            sb2.append(e2.getMessage());
                            Log.e(str2, sb2.toString(), e2);
                        }
                    } else {
                        createScaledBitmap = bitmap.copy(Config.ARGB_8888, true);
                    }
                    bitmap2 = createScaledBitmap;
                }
                if (openResource != null) {
                    openResource.mContentStream.close();
                }
                return bitmap2;
            }
            bitmap = null;
            if (bitmap != null) {
            }
            if (openResource != null) {
            }
        } catch (Exception e3) {
            String str3 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("createThumbnailBitmap fails ");
            sb3.append(e3.getLocalizedMessage());
            Log.e(str3, sb3.toString());
        }
        return bitmap2;
    }
}
