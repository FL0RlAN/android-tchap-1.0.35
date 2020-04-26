package io.realm.internal.android;

import android.os.Handler;
import android.os.Looper;
import io.realm.internal.Capabilities;
import io.realm.internal.OsSharedRealm;
import io.realm.internal.RealmNotifier;
import javax.annotation.Nullable;

public class AndroidRealmNotifier extends RealmNotifier {
    private Handler handler;

    public AndroidRealmNotifier(@Nullable OsSharedRealm osSharedRealm, Capabilities capabilities) {
        super(osSharedRealm);
        if (capabilities.canDeliverNotification()) {
            this.handler = new Handler(Looper.myLooper());
        } else {
            this.handler = null;
        }
    }

    public boolean post(Runnable runnable) {
        Handler handler2 = this.handler;
        return handler2 != null && handler2.post(runnable);
    }
}
