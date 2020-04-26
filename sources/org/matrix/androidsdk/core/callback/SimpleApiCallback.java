package org.matrix.androidsdk.core.callback;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.model.MatrixError;

public abstract class SimpleApiCallback<T> implements ApiCallback<T> {
    private static final String LOG_TAG = "SimpleApiCallback";
    private ApiFailureCallback failureCallback = null;
    /* access modifiers changed from: private */
    public Activity mActivity;
    /* access modifiers changed from: private */
    public Context mContext = null;
    private View mPostView = null;

    public SimpleApiCallback() {
    }

    public SimpleApiCallback(Activity activity) {
        this.mActivity = activity;
    }

    public SimpleApiCallback(Context context, View view) {
        this.mContext = context;
        this.mPostView = view;
    }

    public SimpleApiCallback(ApiFailureCallback apiFailureCallback) {
        this.failureCallback = apiFailureCallback;
    }

    private void displayToast(final String str) {
        Activity activity = this.mActivity;
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(SimpleApiCallback.this.mActivity, str, 0).show();
                }
            });
        } else if (this.mContext != null) {
            View view = this.mPostView;
            if (view != null) {
                view.post(new Runnable() {
                    public void run() {
                        Toast.makeText(SimpleApiCallback.this.mContext, str, 0).show();
                    }
                });
            }
        }
    }

    public void onNetworkError(Exception exc) {
        ApiFailureCallback apiFailureCallback = this.failureCallback;
        if (apiFailureCallback != null) {
            try {
                apiFailureCallback.onNetworkError(exc);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("## onNetworkError() failed");
                sb.append(e.getMessage());
                Log.e(LOG_TAG, sb.toString(), e);
            }
        } else {
            displayToast("Network Error");
        }
    }

    public void onMatrixError(MatrixError matrixError) {
        ApiFailureCallback apiFailureCallback = this.failureCallback;
        if (apiFailureCallback != null) {
            try {
                apiFailureCallback.onMatrixError(matrixError);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("## onMatrixError() failed");
                sb.append(e.getMessage());
                Log.e(LOG_TAG, sb.toString(), e);
            }
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Matrix Error : ");
            sb2.append(matrixError.getLocalizedMessage());
            displayToast(sb2.toString());
        }
    }

    public void onUnexpectedError(Exception exc) {
        ApiFailureCallback apiFailureCallback = this.failureCallback;
        if (apiFailureCallback != null) {
            try {
                apiFailureCallback.onUnexpectedError(exc);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("## onUnexpectedError() failed");
                sb.append(e.getMessage());
                Log.e(LOG_TAG, sb.toString(), e);
            }
        } else {
            displayToast(exc.getLocalizedMessage());
        }
    }
}
