package com.google.firebase.iid;

import android.text.TextUtils;
import android.util.Log;
import androidx.collection.ArrayMap;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.io.IOException;
import java.util.Map;

final class zzba {
    private final zzaw zzal;
    private int zzdn = 0;
    private final Map<Integer, TaskCompletionSource<Void>> zzdo = new ArrayMap();

    zzba(zzaw zzaw) {
        this.zzal = zzaw;
    }

    /* access modifiers changed from: 0000 */
    public final synchronized Task<Void> zza(String str) {
        String zzak;
        TaskCompletionSource taskCompletionSource;
        int i;
        synchronized (this.zzal) {
            zzak = this.zzal.zzak();
            zzaw zzaw = this.zzal;
            StringBuilder sb = new StringBuilder(String.valueOf(zzak).length() + 1 + String.valueOf(str).length());
            sb.append(zzak);
            sb.append(",");
            sb.append(str);
            zzaw.zzf(sb.toString());
        }
        taskCompletionSource = new TaskCompletionSource();
        Map<Integer, TaskCompletionSource<Void>> map = this.zzdo;
        if (TextUtils.isEmpty(zzak)) {
            i = 0;
        } else {
            i = zzak.split(",").length - 1;
        }
        map.put(Integer.valueOf(this.zzdn + i), taskCompletionSource);
        return taskCompletionSource.getTask();
    }

    /* access modifiers changed from: 0000 */
    public final synchronized boolean zzap() {
        return zzaq() != null;
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001c, code lost:
        if (zza(r5, r0) != false) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001f, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0020, code lost:
        monitor-enter(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r2 = (com.google.android.gms.tasks.TaskCompletionSource) r4.zzdo.remove(java.lang.Integer.valueOf(r4.zzdn));
        zzk(r0);
        r4.zzdn++;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0037, code lost:
        monitor-exit(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0038, code lost:
        if (r2 == null) goto L_0x0000;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003a, code lost:
        r2.setResult(null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0016, code lost:
        return true;
     */
    public final boolean zzc(FirebaseInstanceId firebaseInstanceId) {
        while (true) {
            synchronized (this) {
                String zzaq = zzaq();
                if (zzaq == null) {
                    if (FirebaseInstanceId.zzm()) {
                        Log.d("FirebaseInstanceId", "topic sync succeeded");
                    }
                }
            }
        }
        while (true) {
        }
    }

    private final String zzaq() {
        String zzak;
        synchronized (this.zzal) {
            zzak = this.zzal.zzak();
        }
        if (!TextUtils.isEmpty(zzak)) {
            String[] split = zzak.split(",");
            if (split.length > 1 && !TextUtils.isEmpty(split[1])) {
                return split[1];
            }
        }
        return null;
    }

    private final synchronized boolean zzk(String str) {
        synchronized (this.zzal) {
            String zzak = this.zzal.zzak();
            String str2 = ",";
            String valueOf = String.valueOf(str);
            if (!zzak.startsWith(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2))) {
                return false;
            }
            String str3 = ",";
            String valueOf2 = String.valueOf(str);
            this.zzal.zzf(zzak.substring((valueOf2.length() != 0 ? str3.concat(valueOf2) : new String(str3)).length()));
            return true;
        }
    }

    private static boolean zza(FirebaseInstanceId firebaseInstanceId, String str) {
        String str2 = "FirebaseInstanceId";
        String[] split = str.split("!");
        if (split.length == 2) {
            String str3 = split[0];
            String str4 = split[1];
            char c = 65535;
            try {
                int hashCode = str3.hashCode();
                if (hashCode != 83) {
                    if (hashCode == 85) {
                        if (str3.equals("U")) {
                            c = 1;
                        }
                    }
                } else if (str3.equals("S")) {
                    c = 0;
                }
                if (c == 0) {
                    firebaseInstanceId.zzb(str4);
                    if (FirebaseInstanceId.zzm()) {
                        Log.d(str2, "subscribe operation succeeded");
                    }
                } else if (c == 1) {
                    firebaseInstanceId.zzc(str4);
                    if (FirebaseInstanceId.zzm()) {
                        Log.d(str2, "unsubscribe operation succeeded");
                    }
                }
            } catch (IOException e) {
                String str5 = "Topic sync failed: ";
                String valueOf = String.valueOf(e.getMessage());
                Log.e(str2, valueOf.length() != 0 ? str5.concat(valueOf) : new String(str5));
                return false;
            }
        }
        return true;
    }
}
