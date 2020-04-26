package com.google.firebase.iid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.util.concurrent.NamedThreadFactory;
import com.google.android.gms.internal.firebase_messaging.zzb;
import com.google.android.gms.tasks.Task;
import java.util.concurrent.ScheduledExecutorService;

public final class zzab {
    private static zzab zzbu;
    /* access modifiers changed from: private */
    public final ScheduledExecutorService zzbv;
    private zzad zzbw = new zzad(this);
    private int zzbx = 1;
    /* access modifiers changed from: private */
    public final Context zzz;

    public static synchronized zzab zzc(Context context) {
        zzab zzab;
        synchronized (zzab.class) {
            if (zzbu == null) {
                zzbu = new zzab(context, zzb.zza().zza(1, new NamedThreadFactory("MessengerIpcClient"), 9));
            }
            zzab = zzbu;
        }
        return zzab;
    }

    private zzab(Context context, ScheduledExecutorService scheduledExecutorService) {
        this.zzbv = scheduledExecutorService;
        this.zzz = context.getApplicationContext();
    }

    public final Task<Void> zza(int i, Bundle bundle) {
        return zza((zzak<T>) new zzaj<T>(zzx(), 2, bundle));
    }

    public final Task<Bundle> zzb(int i, Bundle bundle) {
        return zza((zzak<T>) new zzam<T>(zzx(), 1, bundle));
    }

    private final synchronized <T> Task<T> zza(zzak<T> zzak) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(zzak);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 9);
            sb.append("Queueing ");
            sb.append(valueOf);
            Log.d("MessengerIpcClient", sb.toString());
        }
        if (!this.zzbw.zzb(zzak)) {
            this.zzbw = new zzad(this);
            this.zzbw.zzb(zzak);
        }
        return zzak.zzch.getTask();
    }

    private final synchronized int zzx() {
        int i;
        i = this.zzbx;
        this.zzbx = i + 1;
        return i;
    }
}
