package com.google.firebase.iid;

import android.content.BroadcastReceiver.PendingResult;
import android.content.Intent;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

final class zzd {
    final Intent intent;
    private final PendingResult zzr;
    private boolean zzs = false;
    private final ScheduledFuture<?> zzt;

    zzd(Intent intent2, PendingResult pendingResult, ScheduledExecutorService scheduledExecutorService) {
        this.intent = intent2;
        this.zzr = pendingResult;
        this.zzt = scheduledExecutorService.schedule(new zze(this, intent2), 9000, TimeUnit.MILLISECONDS);
    }

    /* access modifiers changed from: 0000 */
    public final synchronized void finish() {
        if (!this.zzs) {
            this.zzr.finish();
            this.zzt.cancel(false);
            this.zzs = true;
        }
    }
}
