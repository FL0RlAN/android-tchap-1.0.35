package com.facebook.stetho.common.android;

import android.os.Handler;
import android.os.Looper;
import com.facebook.stetho.common.UncheckedCallable;
import com.facebook.stetho.common.Util;

public final class HandlerUtil {

    private static abstract class WaitableRunnable<V> implements Runnable {
        private Exception mException;
        private boolean mIsDone;
        private V mValue;

        /* access modifiers changed from: protected */
        public abstract V onRun();

        protected WaitableRunnable() {
        }

        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        public final void run() {
            try {
                this.mValue = onRun();
                this.mException = null;
                synchronized (this) {
                    this.mIsDone = true;
                    notifyAll();
                }
            } catch (Exception e) {
                try {
                    this.mValue = null;
                    this.mException = e;
                    synchronized (this) {
                        this.mIsDone = true;
                        notifyAll();
                    }
                } catch (Throwable th) {
                    synchronized (this) {
                        this.mIsDone = true;
                        notifyAll();
                        throw th;
                    }
                }
            }
        }

        public V invoke(Handler handler) {
            if (handler.post(this)) {
                join();
                Exception exc = this.mException;
                if (exc == null) {
                    return this.mValue;
                }
                throw new RuntimeException(exc);
            }
            throw new RuntimeException("Handler.post() returned false");
        }

        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Missing exception handler attribute for start block: B:1:0x0001 */
        /* JADX WARNING: Removed duplicated region for block: B:1:0x0001 A[LOOP:0: B:1:0x0001->B:12:0x0001, LOOP_START, SYNTHETIC] */
        private void join() {
            synchronized (this) {
                while (!this.mIsDone) {
                    wait();
                }
            }
        }
    }

    private HandlerUtil() {
    }

    public static boolean checkThreadAccess(Handler handler) {
        return Looper.myLooper() == handler.getLooper();
    }

    public static void verifyThreadAccess(Handler handler) {
        Util.throwIfNot(checkThreadAccess(handler));
    }

    public static <V> V postAndWait(Handler handler, final UncheckedCallable<V> uncheckedCallable) {
        if (!checkThreadAccess(handler)) {
            return new WaitableRunnable<V>() {
                /* access modifiers changed from: protected */
                public V onRun() {
                    return uncheckedCallable.call();
                }
            }.invoke(handler);
        }
        try {
            return uncheckedCallable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void postAndWait(Handler handler, final Runnable runnable) {
        if (checkThreadAccess(handler)) {
            try {
                runnable.run();
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        } else {
            new WaitableRunnable<Void>() {
                /* access modifiers changed from: protected */
                public Void onRun() {
                    runnable.run();
                    return null;
                }
            }.invoke(handler);
        }
    }
}
