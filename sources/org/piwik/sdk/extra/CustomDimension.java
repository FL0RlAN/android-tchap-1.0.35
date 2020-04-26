package org.piwik.sdk.extra;

import org.piwik.sdk.TrackMe;
import timber.log.Timber;

public class CustomDimension {
    private static final String LOGGER_TAG = "PIWIK:CustomDimension";

    public static boolean setDimension(TrackMe trackMe, int i, String str) {
        String str2 = LOGGER_TAG;
        if (i < 1) {
            Timber.tag(str2).e("dimensionId should be great than 0 (arg: %d)", Integer.valueOf(i));
            return false;
        }
        if (str != null && str.length() > 255) {
            str = str.substring(0, 255);
            Timber.tag(str2).w("dimensionValue was truncated to 255 chars.", new Object[0]);
        }
        if (str != null && str.length() == 0) {
            str = null;
        }
        trackMe.set(formatDimensionId(i), str);
        return true;
    }

    public static String getDimension(TrackMe trackMe, int i) {
        return trackMe.get(formatDimensionId(i));
    }

    private static String formatDimensionId(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("dimension");
        sb.append(i);
        return sb.toString();
    }
}
