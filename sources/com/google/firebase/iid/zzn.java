package com.google.firebase.iid;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

final /* synthetic */ class zzn implements Continuation {
    private final FirebaseInstanceId zzav;
    private final String zzaw;
    private final String zzax;

    zzn(FirebaseInstanceId firebaseInstanceId, String str, String str2) {
        this.zzav = firebaseInstanceId;
        this.zzaw = str;
        this.zzax = str2;
    }

    public final Object then(Task task) {
        return this.zzav.zza(this.zzaw, this.zzax, task);
    }
}
