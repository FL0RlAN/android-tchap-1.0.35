package org.piwik.sdk.tools;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import java.lang.reflect.Method;
import java.util.Locale;
import timber.log.Timber;

public class DeviceHelper {
    private static final String LOGGER_TAG = "PIWIK:DeviceHelper";
    private final BuildInfo mBuildInfo;
    private final Context mContext;
    private final PropertySource mPropertySource;

    public DeviceHelper(Context context, PropertySource propertySource, BuildInfo buildInfo) {
        this.mContext = context;
        this.mPropertySource = propertySource;
        this.mBuildInfo = buildInfo;
    }

    public String getUserLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public String getUserAgent() {
        String httpAgent = this.mPropertySource.getHttpAgent();
        if (httpAgent != null && !httpAgent.startsWith("Apache-HttpClient/UNAVAILABLE (java")) {
            return httpAgent;
        }
        String jVMVersion = this.mPropertySource.getJVMVersion();
        if (jVMVersion == null) {
            jVMVersion = "0.0.0";
        }
        String release = this.mBuildInfo.getRelease();
        String model = this.mBuildInfo.getModel();
        String buildId = this.mBuildInfo.getBuildId();
        return String.format(Locale.US, "Dalvik/%s (Linux; U; Android %s; %s Build/%s)", new Object[]{jVMVersion, release, model, buildId});
    }

    public int[] getResolution() {
        int i;
        int i2;
        String str = LOGGER_TAG;
        try {
            Display defaultDisplay = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay();
            if (VERSION.SDK_INT >= 17) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                defaultDisplay.getRealMetrics(displayMetrics);
                i = displayMetrics.widthPixels;
                i2 = displayMetrics.heightPixels;
            } else if (VERSION.SDK_INT >= 14) {
                try {
                    Method method = Display.class.getMethod("getRawWidth", new Class[0]);
                    Method method2 = Display.class.getMethod("getRawHeight", new Class[0]);
                    i = ((Integer) method.invoke(defaultDisplay, new Object[0])).intValue();
                    try {
                        i2 = ((Integer) method2.invoke(defaultDisplay, new Object[0])).intValue();
                    } catch (Exception e) {
                        e = e;
                        Timber.tag(str).w(e, "Reflection of getRawWidth/getRawHeight failed on API14-16 unexpectedly.", new Object[0]);
                        i2 = -1;
                        DisplayMetrics displayMetrics2 = new DisplayMetrics();
                        defaultDisplay.getMetrics(displayMetrics2);
                        i = displayMetrics2.widthPixels;
                        i2 = displayMetrics2.heightPixels;
                        return new int[]{i, i2};
                    }
                } catch (Exception e2) {
                    e = e2;
                    i = -1;
                    Timber.tag(str).w(e, "Reflection of getRawWidth/getRawHeight failed on API14-16 unexpectedly.", new Object[0]);
                    i2 = -1;
                    DisplayMetrics displayMetrics22 = new DisplayMetrics();
                    defaultDisplay.getMetrics(displayMetrics22);
                    i = displayMetrics22.widthPixels;
                    i2 = displayMetrics22.heightPixels;
                    return new int[]{i, i2};
                }
            } else {
                i2 = -1;
                i = -1;
            }
            if (i == -1 || i2 == -1) {
                DisplayMetrics displayMetrics222 = new DisplayMetrics();
                defaultDisplay.getMetrics(displayMetrics222);
                i = displayMetrics222.widthPixels;
                i2 = displayMetrics222.heightPixels;
            }
            return new int[]{i, i2};
        } catch (NullPointerException e3) {
            Timber.tag(str).e(e3, "Window service was not available from this context", new Object[0]);
            return null;
        }
    }
}
