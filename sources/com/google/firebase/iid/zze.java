package com.google.firebase.iid;

import android.content.Intent;
import android.util.Log;

final /* synthetic */ class zze implements Runnable {
    private final zzd zzu;
    private final Intent zzv;

    zze(zzd zzd, Intent intent) {
        this.zzu = zzd;
        this.zzv = intent;
    }

    public final void run() {
        zzd zzd = this.zzu;
        String action = this.zzv.getAction();
        StringBuilder sb = new StringBuilder(String.valueOf(action).length() + 61);
        sb.append("Service took too long to process intent: ");
        sb.append(action);
        sb.append(" App may get closed.");
        Log.w("EnhancedIntentService", sb.toString());
        zzd.finish();
    }
}
