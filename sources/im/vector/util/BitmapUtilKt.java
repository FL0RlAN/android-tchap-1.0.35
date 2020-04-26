package im.vector.util;

import android.graphics.Bitmap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\f\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u0001¨\u0006\u0002"}, d2 = {"createSquareBitmap", "Landroid/graphics/Bitmap;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 2, mv = {1, 1, 13})
/* compiled from: BitmapUtil.kt */
public final class BitmapUtilKt {
    public static final Bitmap createSquareBitmap(Bitmap bitmap) {
        Intrinsics.checkParameterIsNotNull(bitmap, "receiver$0");
        if (bitmap.getWidth() == bitmap.getHeight()) {
            return bitmap;
        }
        String str = "## createSquareBitmap ";
        String str2 = "BitmapUtil";
        if (bitmap.getWidth() > bitmap.getHeight()) {
            try {
                return Bitmap.createBitmap(bitmap, (bitmap.getWidth() - bitmap.getHeight()) / 2, 0, bitmap.getHeight(), bitmap.getHeight());
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
                return bitmap;
            }
        } else {
            try {
                return Bitmap.createBitmap(bitmap, 0, (bitmap.getHeight() - bitmap.getWidth()) / 2, bitmap.getWidth(), bitmap.getWidth());
            } catch (Exception e2) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(e2.getMessage());
                Log.e(str2, sb2.toString(), e2);
                return bitmap;
            }
        }
    }
}
