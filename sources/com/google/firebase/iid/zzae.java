package com.google.firebase.iid;

import android.os.Handler.Callback;
import android.os.Message;

final /* synthetic */ class zzae implements Callback {
    private final zzad zzcd;

    zzae(zzad zzad) {
        this.zzcd = zzad;
    }

    public final boolean handleMessage(Message message) {
        return this.zzcd.zza(message);
    }
}
