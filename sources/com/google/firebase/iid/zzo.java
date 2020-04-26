package com.google.firebase.iid;

import com.google.android.gms.tasks.Task;

final /* synthetic */ class zzo implements zzas {
    private final FirebaseInstanceId zzav;
    private final String zzaw;
    private final String zzax;
    private final String zzay;
    private final String zzaz;

    zzo(FirebaseInstanceId firebaseInstanceId, String str, String str2, String str3, String str4) {
        this.zzav = firebaseInstanceId;
        this.zzaw = str;
        this.zzax = str2;
        this.zzay = str3;
        this.zzaz = str4;
    }

    public final Task zzs() {
        return this.zzav.zza(this.zzaw, this.zzax, this.zzay, this.zzaz);
    }
}
