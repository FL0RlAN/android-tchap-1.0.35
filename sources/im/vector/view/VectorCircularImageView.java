package im.vector.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Pair;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.collection.LruCache;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import im.vector.util.BitmapUtilKt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.HelpFormatter;
import org.matrix.androidsdk.core.Log;

public class VectorCircularImageView extends AppCompatImageView {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorCircularImageView.class.getSimpleName();
    /* access modifiers changed from: private */
    public static final LruCache<String, RoundedBitmapDrawable> mCache = new LruCache<String, RoundedBitmapDrawable>(4194304) {
        /* access modifiers changed from: protected */
        public int sizeOf(String str, RoundedBitmapDrawable roundedBitmapDrawable) {
            return roundedBitmapDrawable.getBitmap().getRowBytes() * roundedBitmapDrawable.getBitmap().getHeight();
        }
    };
    private static HandlerThread mConversionImagesThread = null;
    private static Handler mConversionImagesThreadHandler = null;
    /* access modifiers changed from: private */
    public static Map<String, List<Pair<Object, VectorCircularImageView>>> mPendingConversion = new HashMap();
    /* access modifiers changed from: private */
    public static Handler mUIHandler = null;

    public VectorCircularImageView(Context context) {
        super(context);
    }

    public VectorCircularImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public VectorCircularImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (drawable != null && (drawable instanceof BitmapDrawable)) {
            final Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                post(new Runnable() {
                    public void run() {
                        VectorCircularImageView.this.setImageBitmap(bitmap);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setCircularImageDrawable(RoundedBitmapDrawable roundedBitmapDrawable) {
        super.setImageDrawable(roundedBitmapDrawable);
    }

    public void setImageBitmap(final Bitmap bitmap) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            final int height = bitmap.getHeight();
            StringBuilder sb = new StringBuilder();
            sb.append(bitmap.toString());
            sb.append(width);
            sb.append(HelpFormatter.DEFAULT_OPT_PREFIX);
            sb.append(height);
            final String sb2 = sb.toString();
            RoundedBitmapDrawable roundedBitmapDrawable = (RoundedBitmapDrawable) mCache.get(sb2);
            if (roundedBitmapDrawable != null) {
                setCircularImageDrawable(roundedBitmapDrawable);
                return;
            }
            if (mConversionImagesThread == null) {
                mConversionImagesThread = new HandlerThread("VectorCircularImageViewThread", 1);
                mConversionImagesThread.start();
                mConversionImagesThreadHandler = new Handler(mConversionImagesThread.getLooper());
                mUIHandler = new Handler(Looper.getMainLooper());
            }
            if (mPendingConversion.containsKey(sb2)) {
                ((List) mPendingConversion.get(sb2)).add(new Pair(getTag(), this));
                return;
            }
            mPendingConversion.put(sb2, new ArrayList(Arrays.asList(new Pair[]{new Pair(getTag(), this)})));
            mConversionImagesThreadHandler.post(new Runnable() {
                public void run() {
                    try {
                        final RoundedBitmapDrawable create = RoundedBitmapDrawableFactory.create(VectorCircularImageView.this.getResources(), BitmapUtilKt.createSquareBitmap(bitmap));
                        create.setAntiAlias(true);
                        create.setCornerRadius(((float) height) / 2.0f);
                        VectorCircularImageView.mUIHandler.post(new Runnable() {
                            public void run() {
                                VectorCircularImageView.mCache.put(sb2, create);
                                List<Pair> list = (List) VectorCircularImageView.mPendingConversion.get(sb2);
                                VectorCircularImageView.mPendingConversion.remove(sb2);
                                for (Pair pair : list) {
                                    if (((VectorCircularImageView) pair.second).getTag() == pair.first) {
                                        ((VectorCircularImageView) pair.second).setCircularImageDrawable(create);
                                    }
                                }
                            }
                        });
                    } catch (Exception e) {
                        String access$300 = VectorCircularImageView.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## setImageBitmap - RoundedBitmapDrawableFactory.create ");
                        sb.append(e.getMessage());
                        Log.e(access$300, sb.toString(), e);
                        VectorCircularImageView.mUIHandler.post(new Runnable() {
                            public void run() {
                                VectorCircularImageView.this.setImageBitmap(null);
                            }
                        });
                    }
                }
            });
        } else {
            super.setImageBitmap(null);
        }
    }
}
