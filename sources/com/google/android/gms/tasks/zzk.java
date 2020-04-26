package com.google.android.gms.tasks;

import java.util.concurrent.Executor;

final class zzk<TResult> implements zzq<TResult> {
    /* access modifiers changed from: private */
    public final Object mLock = new Object();
    private final Executor zzd;
    /* access modifiers changed from: private */
    public OnFailureListener zzn;

    public zzk(Executor executor, OnFailureListener onFailureListener) {
        this.zzd = executor;
        this.zzn = onFailureListener;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0016, code lost:
        r2.zzd.execute(new com.google.android.gms.tasks.zzl(r2, r3));
     */
    public final void onComplete(Task<TResult> task) {
        if (!task.isSuccessful() && !task.isCanceled()) {
            synchronized (this.mLock) {
                if (this.zzn == null) {
                }
            }
        }
    }

    public final void cancel() {
        synchronized (this.mLock) {
            this.zzn = null;
        }
    }
}
