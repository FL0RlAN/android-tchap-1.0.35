package org.matrix.androidsdk.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;
import org.matrix.androidsdk.rest.model.message.ImageInfo;

public class ContentUtils {
    private static final String LOG_TAG = FileContentUtils.class.getSimpleName();

    public static ImageInfo getImageInfoFromFile(String str) {
        ImageInfo imageInfo = new ImageInfo();
        try {
            Bitmap decodeFile = BitmapFactory.decodeFile(str);
            imageInfo.w = Integer.valueOf(decodeFile.getWidth());
            imageInfo.h = Integer.valueOf(decodeFile.getHeight());
            imageInfo.size = Long.valueOf(new File(str).length());
            imageInfo.mimetype = FileContentUtils.getMimeType(str);
        } catch (OutOfMemoryError e) {
            Log.e(LOG_TAG, "## getImageInfoFromFile() : oom", e);
        }
        return imageInfo;
    }
}
