package com.google.firebase.iid;

import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.TaskCompletionSource;

abstract class zzak<T> {
    final int what;
    final int zzcg;
    final TaskCompletionSource<T> zzch = new TaskCompletionSource<>();
    final Bundle zzci;

    zzak(int i, int i2, Bundle bundle) {
        this.zzcg = i;
        this.what = i2;
        this.zzci = bundle;
    }

    /* access modifiers changed from: 0000 */
    public abstract boolean zzab();

    /* access modifiers changed from: 0000 */
    public abstract void zzb(Bundle bundle);

    /* access modifiers changed from: 0000 */
    public final void finish(T t) {
        String str = "MessengerIpcClient";
        if (Log.isLoggable(str, 3)) {
            String valueOf = String.valueOf(this);
            String valueOf2 = String.valueOf(t);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 16 + String.valueOf(valueOf2).length());
            sb.append("Finishing ");
            sb.append(valueOf);
            sb.append(" with ");
            sb.append(valueOf2);
            Log.d(str, sb.toString());
        }
        this.zzch.setResult(t);
    }

    /* access modifiers changed from: 0000 */
    public final void zza(zzal zzal) {
        String str = "MessengerIpcClient";
        if (Log.isLoggable(str, 3)) {
            String valueOf = String.valueOf(this);
            String valueOf2 = String.valueOf(zzal);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 14 + String.valueOf(valueOf2).length());
            sb.append("Failing ");
            sb.append(valueOf);
            sb.append(" with ");
            sb.append(valueOf2);
            Log.d(str, sb.toString());
        }
        this.zzch.setException(zzal);
    }

    public String toString() {
        int i = this.what;
        int i2 = this.zzcg;
        boolean zzab = zzab();
        StringBuilder sb = new StringBuilder(55);
        sb.append("Request { what=");
        sb.append(i);
        sb.append(" id=");
        sb.append(i2);
        sb.append(" oneWay=");
        sb.append(zzab);
        sb.append("}");
        return sb.toString();
    }
}
