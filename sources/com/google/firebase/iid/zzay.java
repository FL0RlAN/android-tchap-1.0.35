package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import im.vector.activity.VectorUniversalLinkActivity;
import java.io.IOException;

final class zzay implements Runnable {
    private final zzba zzas;
    private final long zzdj;
    private final WakeLock zzdk = ((PowerManager) getContext().getSystemService("power")).newWakeLock(1, "fiid-sync");
    private final FirebaseInstanceId zzdl;

    zzay(FirebaseInstanceId firebaseInstanceId, zzan zzan, zzba zzba, long j) {
        this.zzdl = firebaseInstanceId;
        this.zzas = zzba;
        this.zzdj = j;
        this.zzdk.setReferenceCounted(false);
    }

    public final void run() {
        try {
            if (zzav.zzai().zzd(getContext())) {
                this.zzdk.acquire();
            }
            this.zzdl.zza(true);
            if (!this.zzdl.zzo()) {
                this.zzdl.zza(false);
            } else if (!zzav.zzai().zze(getContext()) || zzan()) {
                if (!zzam() || !this.zzas.zzc(this.zzdl)) {
                    this.zzdl.zza(this.zzdj);
                } else {
                    this.zzdl.zza(false);
                }
                if (zzav.zzai().zzd(getContext())) {
                    this.zzdk.release();
                }
            } else {
                new zzaz(this).zzao();
                if (zzav.zzai().zzd(getContext())) {
                    this.zzdk.release();
                }
            }
        } finally {
            if (zzav.zzai().zzd(getContext())) {
                this.zzdk.release();
            }
        }
    }

    private final boolean zzam() {
        String str = "FirebaseInstanceId";
        zzax zzk = this.zzdl.zzk();
        if (!this.zzdl.zzr() && !this.zzdl.zza(zzk)) {
            return true;
        }
        try {
            String zzl = this.zzdl.zzl();
            if (zzl == null) {
                Log.e(str, "Token retrieval failed: null");
                return false;
            }
            if (Log.isLoggable(str, 3)) {
                Log.d(str, "Token successfully retrieved");
            }
            if (zzk == null || (zzk != null && !zzl.equals(zzk.zzbr))) {
                Context context = getContext();
                Intent intent = new Intent("com.google.firebase.messaging.NEW_TOKEN");
                intent.putExtra(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_TOKEN, zzl);
                zzav.zzc(context, intent);
                zzav.zzb(context, new Intent("com.google.firebase.iid.TOKEN_REFRESH"));
            }
            return true;
        } catch (IOException | SecurityException e) {
            String str2 = "Token retrieval failed: ";
            String valueOf = String.valueOf(e.getMessage());
            Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    public final Context getContext() {
        return this.zzdl.zzi().getApplicationContext();
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzan() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
