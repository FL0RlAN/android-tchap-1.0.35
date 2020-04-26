package io.realm.internal.android;

import android.os.Looper;
import io.realm.internal.Capabilities;
import javax.annotation.Nullable;

public class AndroidCapabilities implements Capabilities {
    public static boolean EMULATE_MAIN_THREAD = false;
    private final boolean isIntentServiceThread = isIntentServiceThread();
    private final Looper looper = Looper.myLooper();

    public boolean canDeliverNotification() {
        return hasLooper() && !this.isIntentServiceThread;
    }

    public void checkCanDeliverNotification(@Nullable String str) {
        String str2 = "";
        String str3 = " ";
        if (!hasLooper()) {
            if (str != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(str3);
                sb.append("Realm cannot be automatically updated on a thread without a looper.");
                str2 = sb.toString();
            }
            throw new IllegalStateException(str2);
        } else if (this.isIntentServiceThread) {
            if (str != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(str3);
                sb2.append("Realm cannot be automatically updated on an IntentService thread.");
                str2 = sb2.toString();
            }
            throw new IllegalStateException(str2);
        }
    }

    public boolean isMainThread() {
        Looper looper2 = this.looper;
        return looper2 != null && (EMULATE_MAIN_THREAD || looper2 == Looper.getMainLooper());
    }

    private boolean hasLooper() {
        return this.looper != null;
    }

    private static boolean isIntentServiceThread() {
        String name = Thread.currentThread().getName();
        return name != null && name.startsWith("IntentService[");
    }
}
