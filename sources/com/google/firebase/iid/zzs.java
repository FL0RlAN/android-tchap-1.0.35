package com.google.firebase.iid;

import android.os.Bundle;
import com.google.android.gms.tasks.TaskCompletionSource;

final /* synthetic */ class zzs implements Runnable {
    private final zzr zzbl;
    private final Bundle zzbm;
    private final TaskCompletionSource zzbn;

    zzs(zzr zzr, Bundle bundle, TaskCompletionSource taskCompletionSource) {
        this.zzbl = zzr;
        this.zzbm = bundle;
        this.zzbn = taskCompletionSource;
    }

    public final void run() {
        this.zzbl.zza(this.zzbm, this.zzbn);
    }
}
