package com.google.firebase.iid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import javax.annotation.Nullable;

final class zzaz extends BroadcastReceiver {
    @Nullable
    private zzay zzdm;

    public zzaz(zzay zzay) {
        this.zzdm = zzay;
    }

    public final void zzao() {
        if (FirebaseInstanceId.zzm()) {
            Log.d("FirebaseInstanceId", "Connectivity change received registered");
        }
        this.zzdm.getContext().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public final void onReceive(Context context, Intent intent) {
        zzay zzay = this.zzdm;
        if (zzay != null && zzay.zzan()) {
            if (FirebaseInstanceId.zzm()) {
                Log.d("FirebaseInstanceId", "Connectivity changed. Starting background sync.");
            }
            FirebaseInstanceId.zza((Runnable) this.zzdm, 0);
            this.zzdm.getContext().unregisterReceiver(this);
            this.zzdm = null;
        }
    }
}
