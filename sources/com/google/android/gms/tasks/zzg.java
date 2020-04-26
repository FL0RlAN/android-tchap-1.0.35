package com.google.android.gms.tasks;

import java.util.concurrent.Executor;

final class zzg<TResult> implements zzq<TResult> {
    /* access modifiers changed from: private */
    public final Object mLock = new Object();
    private final Executor zzd;
    /* access modifiers changed from: private */
    public OnCanceledListener zzj;

    public zzg(Executor executor, OnCanceledListener onCanceledListener) {
        this.zzd = executor;
        this.zzj = onCanceledListener;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0010, code lost:
        r1.zzd.execute(new com.google.android.gms.tasks.zzh(r1));
     */
    public final void onComplete(Task task) {
        if (task.isCanceled()) {
            synchronized (this.mLock) {
                if (this.zzj == null) {
                }
            }
        }
    }

    public final void cancel() {
        synchronized (this.mLock) {
            this.zzj = null;
        }
    }
}
