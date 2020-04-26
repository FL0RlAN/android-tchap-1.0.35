package org.piwik.sdk.tools;

import android.os.Build;
import android.os.Build.VERSION;

public class BuildInfo {
    public String getRelease() {
        return VERSION.RELEASE;
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getBuildId() {
        return Build.ID;
    }
}
