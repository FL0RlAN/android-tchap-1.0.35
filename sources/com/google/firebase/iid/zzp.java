package com.google.firebase.iid;

import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;

final /* synthetic */ class zzp implements SuccessContinuation {
    private final FirebaseInstanceId zzav;
    private final String zzaw;
    private final String zzax;
    private final String zzay;

    zzp(FirebaseInstanceId firebaseInstanceId, String str, String str2, String str3) {
        this.zzav = firebaseInstanceId;
        this.zzaw = str;
        this.zzax = str2;
        this.zzay = str3;
    }

    public final Task then(Object obj) {
        return this.zzav.zzb(this.zzaw, this.zzax, this.zzay, (String) obj);
    }
}
