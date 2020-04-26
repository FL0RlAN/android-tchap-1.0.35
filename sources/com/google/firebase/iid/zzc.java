package com.google.firebase.iid;

import android.content.Intent;

final class zzc implements Runnable {
    private final /* synthetic */ Intent zzo;
    private final /* synthetic */ Intent zzp;
    private final /* synthetic */ zzb zzq;

    zzc(zzb zzb, Intent intent, Intent intent2) {
        this.zzq = zzb;
        this.zzo = intent;
        this.zzp = intent2;
    }

    public final void run() {
        this.zzq.zzd(this.zzo);
        this.zzq.zza(this.zzp);
    }
}
