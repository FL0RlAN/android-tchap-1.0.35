package com.google.firebase.iid;

import android.os.Looper;
import android.os.Message;
import com.google.android.gms.internal.firebase_messaging.zze;

final class zzau extends zze {
    private final /* synthetic */ zzat zzcx;

    zzau(zzat zzat, Looper looper) {
        this.zzcx = zzat;
        super(looper);
    }

    public final void handleMessage(Message message) {
        this.zzcx.zzb(message);
    }
}
