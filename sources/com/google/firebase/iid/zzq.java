package com.google.firebase.iid;

import com.google.firebase.events.Event;
import com.google.firebase.events.EventHandler;

final /* synthetic */ class zzq implements EventHandler {
    private final zza zzbf;

    zzq(zza zza) {
        this.zzbf = zza;
    }

    public final void handle(Event event) {
        zza zza = this.zzbf;
        synchronized (zza) {
            if (zza.isEnabled()) {
                FirebaseInstanceId.this.zzh();
            }
        }
    }
}
