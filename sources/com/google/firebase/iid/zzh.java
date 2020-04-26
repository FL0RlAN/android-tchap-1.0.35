package com.google.firebase.iid;

import android.content.BroadcastReceiver.PendingResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.android.gms.common.util.concurrent.NamedThreadFactory;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public final class zzh implements ServiceConnection {
    private final Intent zzaa;
    private final ScheduledExecutorService zzab;
    private final Queue<zzd> zzac;
    private zzf zzad;
    private boolean zzae;
    private final Context zzz;

    public zzh(Context context, String str) {
        this(context, str, new ScheduledThreadPoolExecutor(0, new NamedThreadFactory("Firebase-FirebaseInstanceIdServiceConnection")));
    }

    private zzh(Context context, String str, ScheduledExecutorService scheduledExecutorService) {
        this.zzac = new ArrayDeque();
        this.zzae = false;
        this.zzz = context.getApplicationContext();
        this.zzaa = new Intent(str).setPackage(this.zzz.getPackageName());
        this.zzab = scheduledExecutorService;
    }

    public final synchronized void zza(Intent intent, PendingResult pendingResult) {
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "new intent queued in the bind-strategy delivery");
        }
        this.zzac.add(new zzd(intent, pendingResult, this.zzab));
        zzd();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00a8, code lost:
        return;
     */
    private final synchronized void zzd() {
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "flush queue called");
        }
        while (!this.zzac.isEmpty()) {
            if (Log.isLoggable("EnhancedIntentService", 3)) {
                Log.d("EnhancedIntentService", "found intent to be delivered");
            }
            if (this.zzad == null || !this.zzad.isBinderAlive()) {
                if (Log.isLoggable("EnhancedIntentService", 3)) {
                    String str = "EnhancedIntentService";
                    boolean z = !this.zzae;
                    StringBuilder sb = new StringBuilder(39);
                    sb.append("binder is dead. start connection? ");
                    sb.append(z);
                    Log.d(str, sb.toString());
                }
                if (!this.zzae) {
                    this.zzae = true;
                    try {
                        if (!ConnectionTracker.getInstance().bindService(this.zzz, this.zzaa, this, 65)) {
                            Log.e("EnhancedIntentService", "binding to the service failed");
                            this.zzae = false;
                            zze();
                        } else {
                            return;
                        }
                    } catch (SecurityException e) {
                        Log.e("EnhancedIntentService", "Exception while binding the service", e);
                    }
                }
            } else {
                if (Log.isLoggable("EnhancedIntentService", 3)) {
                    Log.d("EnhancedIntentService", "binder is alive, sending the intent.");
                }
                this.zzad.zza((zzd) this.zzac.poll());
            }
        }
    }

    private final void zze() {
        while (!this.zzac.isEmpty()) {
            ((zzd) this.zzac.poll()).finish();
        }
    }

    public final synchronized void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            String valueOf = String.valueOf(componentName);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 20);
            sb.append("onServiceConnected: ");
            sb.append(valueOf);
            Log.d("EnhancedIntentService", sb.toString());
        }
        this.zzae = false;
        if (!(iBinder instanceof zzf)) {
            String valueOf2 = String.valueOf(iBinder);
            StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 28);
            sb2.append("Invalid service connection: ");
            sb2.append(valueOf2);
            Log.e("EnhancedIntentService", sb2.toString());
            zze();
            return;
        }
        this.zzad = (zzf) iBinder;
        zzd();
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        String str = "EnhancedIntentService";
        if (Log.isLoggable(str, 3)) {
            String valueOf = String.valueOf(componentName);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 23);
            sb.append("onServiceDisconnected: ");
            sb.append(valueOf);
            Log.d(str, sb.toString());
        }
        zzd();
    }
}
