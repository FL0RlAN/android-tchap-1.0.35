package com.google.firebase.iid;

import android.util.Pair;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

final /* synthetic */ class zzar implements Continuation {
    private final zzaq zzcq;
    private final Pair zzcr;

    zzar(zzaq zzaq, Pair pair) {
        this.zzcq = zzaq;
        this.zzcr = pair;
    }

    public final Object then(Task task) {
        return this.zzcq.zza(this.zzcr, task);
    }
}
