package com.google.firebase.iid;

import android.util.Log;
import android.util.Pair;
import androidx.collection.ArrayMap;
import com.google.android.gms.tasks.Task;
import java.util.Map;
import java.util.concurrent.Executor;

final class zzaq {
    private final Executor zzbk;
    private final Map<Pair<String, String>, Task<InstanceIdResult>> zzcp = new ArrayMap();

    zzaq(Executor executor) {
        this.zzbk = executor;
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003e, code lost:
        return r4;
     */
    public final synchronized Task<InstanceIdResult> zza(String str, String str2, zzas zzas) {
        Pair pair = new Pair(str, str2);
        Task<InstanceIdResult> task = (Task) this.zzcp.get(pair);
        if (task == null) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String valueOf = String.valueOf(pair);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 24);
                sb.append("Making new request for: ");
                sb.append(valueOf);
                Log.d("FirebaseInstanceId", sb.toString());
            }
            Task<InstanceIdResult> continueWithTask = zzas.zzs().continueWithTask(this.zzbk, new zzar(this, pair));
            this.zzcp.put(pair, continueWithTask);
            return continueWithTask;
        } else if (Log.isLoggable("FirebaseInstanceId", 3)) {
            String valueOf2 = String.valueOf(pair);
            StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 29);
            sb2.append("Joining ongoing request for: ");
            sb2.append(valueOf2);
            Log.d("FirebaseInstanceId", sb2.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ Task zza(Pair pair, Task task) throws Exception {
        synchronized (this) {
            this.zzcp.remove(pair);
        }
        return task;
    }
}
