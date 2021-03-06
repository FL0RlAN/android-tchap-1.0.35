package org.matrix.androidsdk.core;

import android.os.Handler;
import android.os.Looper;

public class MXOsHandler {
    public static final IPostListener mPostListener = null;
    private final Handler mHandler;

    public interface IPostListener {
        void onPost(Looper looper);
    }

    public MXOsHandler(Looper looper) {
        this.mHandler = new Handler(looper);
    }

    public boolean post(Runnable runnable) {
        boolean post = this.mHandler.post(runnable);
        if (post) {
            IPostListener iPostListener = mPostListener;
            if (iPostListener != null) {
                iPostListener.onPost(this.mHandler.getLooper());
            }
        }
        return post;
    }
}
