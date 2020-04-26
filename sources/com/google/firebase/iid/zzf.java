package com.google.firebase.iid;

import android.os.Binder;
import android.os.Process;
import android.util.Log;

public final class zzf extends Binder {
    /* access modifiers changed from: private */
    public final zzb zzw;

    zzf(zzb zzb) {
        this.zzw = zzb;
    }

    public final void zza(zzd zzd) {
        if (Binder.getCallingUid() == Process.myUid()) {
            String str = "EnhancedIntentService";
            if (Log.isLoggable(str, 3)) {
                Log.d(str, "service received new intent via bind strategy");
            }
            if (this.zzw.zzc(zzd.intent)) {
                zzd.finish();
                return;
            }
            if (Log.isLoggable(str, 3)) {
                Log.d(str, "intent being queued for bg execution");
            }
            this.zzw.zzk.execute(new zzg(this, zzd));
            return;
        }
        throw new SecurityException("Binding only allowed within app");
    }
}
