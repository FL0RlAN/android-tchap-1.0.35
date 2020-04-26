package com.google.firebase.iid;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import androidx.legacy.content.WakefulBroadcastReceiver;
import com.google.android.gms.common.util.concurrent.NamedThreadFactory;
import com.google.android.gms.internal.firebase_messaging.zza;
import java.util.concurrent.ExecutorService;

public abstract class zzb extends Service {
    private final Object lock;
    final ExecutorService zzk;
    private Binder zzl;
    private int zzm;
    private int zzn;

    public zzb() {
        zza zza = com.google.android.gms.internal.firebase_messaging.zzb.zza();
        String valueOf = String.valueOf(getClass().getSimpleName());
        String str = "Firebase-";
        this.zzk = zza.zza(new NamedThreadFactory(valueOf.length() != 0 ? str.concat(valueOf) : new String(str)), 9);
        this.lock = new Object();
        this.zzn = 0;
    }

    /* access modifiers changed from: protected */
    public Intent zzb(Intent intent) {
        return intent;
    }

    public boolean zzc(Intent intent) {
        return false;
    }

    public abstract void zzd(Intent intent);

    public final synchronized IBinder onBind(Intent intent) {
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "Service received bind request");
        }
        if (this.zzl == null) {
            this.zzl = new zzf(this);
        }
        return this.zzl;
    }

    public final int onStartCommand(Intent intent, int i, int i2) {
        synchronized (this.lock) {
            this.zzm = i2;
            this.zzn++;
        }
        Intent zzb = zzb(intent);
        if (zzb == null) {
            zza(intent);
            return 2;
        } else if (zzc(zzb)) {
            zza(intent);
            return 2;
        } else {
            this.zzk.execute(new zzc(this, zzb, intent));
            return 3;
        }
    }

    /* access modifiers changed from: private */
    public final void zza(Intent intent) {
        if (intent != null) {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
        synchronized (this.lock) {
            this.zzn--;
            if (this.zzn == 0) {
                stopSelfResult(this.zzm);
            }
        }
    }
}
