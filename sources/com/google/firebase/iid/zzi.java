package com.google.firebase.iid;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

final class zzi {
    private static final Executor zzaf = zzk.zzah;

    static Executor zzf() {
        return zzaf;
    }

    static Executor zzg() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, 30, TimeUnit.SECONDS, new LinkedBlockingQueue(), zzj.zzag);
        return threadPoolExecutor;
    }

    static final /* synthetic */ Thread zza(Runnable runnable) {
        return new Thread(runnable, "firebase-iid-executor");
    }
}
