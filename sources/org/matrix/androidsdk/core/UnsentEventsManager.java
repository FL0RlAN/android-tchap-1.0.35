package org.matrix.androidsdk.core;

import android.content.Context;
import android.text.TextUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.json.GsonProvider;
import org.matrix.androidsdk.core.listeners.IMXNetworkEventListener;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.network.NetworkConnectivityReceiver;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback.RequestRetryCallBack;
import org.matrix.androidsdk.ssl.CertUtil;
import org.matrix.androidsdk.ssl.UnrecognizedCertificateException;
import retrofit2.Response;

public class UnsentEventsManager {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = UnsentEventsManager.class.getSimpleName();
    private static final int MAX_MESSAGE_LIFETIME_MS = 180000;
    private static final int MAX_RETRIES = 4;
    private static final int RETRY_JITTER_MS = 3000;
    /* access modifiers changed from: private */
    public final DataHandlerInterface mDataHandler;
    private final NetworkConnectivityReceiver mNetworkConnectivityReceiver;
    /* access modifiers changed from: private */
    public final List<UnsentEventSnapshot> mUnsentEvents = new ArrayList();
    /* access modifiers changed from: private */
    public final Map<Object, UnsentEventSnapshot> mUnsentEventsMap = new HashMap();
    /* access modifiers changed from: private */
    public boolean mbIsConnected = false;

    private class UnsentEventSnapshot {
        /* access modifiers changed from: private */
        public long mAge;
        private Timer mAutoResendTimer;
        public String mEventDescription;
        public boolean mIsResending;
        public Timer mLifeTimeTimer;
        /* access modifiers changed from: private */
        public RequestRetryCallBack mRequestRetryCallBack;
        /* access modifiers changed from: private */
        public int mRetryCount;

        private UnsentEventSnapshot() {
            this.mAutoResendTimer = null;
            this.mLifeTimeTimer = null;
            this.mIsResending = false;
            this.mEventDescription = null;
        }

        public boolean waitToBeResent() {
            return this.mAutoResendTimer != null;
        }

        public boolean resendEventAfter(int i) {
            stopTimer();
            try {
                if (this.mEventDescription != null) {
                    String access$000 = UnsentEventsManager.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Resend after ");
                    sb.append(i);
                    sb.append(" [");
                    sb.append(this.mEventDescription);
                    sb.append("]");
                    Log.d(access$000, sb.toString());
                }
                this.mAutoResendTimer = new Timer();
                this.mAutoResendTimer.schedule(new TimerTask() {
                    public void run() {
                        try {
                            UnsentEventSnapshot.this.mIsResending = true;
                            if (UnsentEventSnapshot.this.mEventDescription != null) {
                                String access$000 = UnsentEventsManager.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("Resend [");
                                sb.append(UnsentEventSnapshot.this.mEventDescription);
                                sb.append("]");
                                Log.d(access$000, sb.toString());
                            }
                            UnsentEventSnapshot.this.mRequestRetryCallBack.onRetry();
                        } catch (Throwable th) {
                            UnsentEventSnapshot.this.mIsResending = false;
                            String access$0002 = UnsentEventsManager.LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("## resendEventAfter() : ");
                            sb2.append(UnsentEventSnapshot.this.mEventDescription);
                            sb2.append(" + onRetry failed ");
                            sb2.append(th.getMessage());
                            Log.e(access$0002, sb2.toString(), th);
                        }
                    }
                }, (long) i);
                return true;
            } catch (Throwable th) {
                String access$0002 = UnsentEventsManager.LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## resendEventAfter failed ");
                sb2.append(th.getMessage());
                Log.e(access$0002, sb2.toString(), th);
                return false;
            }
        }

        public void stopTimer() {
            Timer timer = this.mAutoResendTimer;
            if (timer != null) {
                timer.cancel();
                this.mAutoResendTimer = null;
            }
        }

        public void stopTimers() {
            Timer timer = this.mAutoResendTimer;
            if (timer != null) {
                timer.cancel();
                this.mAutoResendTimer = null;
            }
            Timer timer2 = this.mLifeTimeTimer;
            if (timer2 != null) {
                timer2.cancel();
                this.mLifeTimeTimer = null;
            }
        }
    }

    public UnsentEventsManager(NetworkConnectivityReceiver networkConnectivityReceiver, DataHandlerInterface dataHandlerInterface) {
        this.mNetworkConnectivityReceiver = networkConnectivityReceiver;
        this.mNetworkConnectivityReceiver.addEventListener(new IMXNetworkEventListener() {
            public void onNetworkConnectionUpdate(boolean z) {
                UnsentEventsManager.this.mbIsConnected = z;
                if (z) {
                    UnsentEventsManager.this.resentUnsents();
                }
            }
        });
        this.mbIsConnected = this.mNetworkConnectivityReceiver.isConnected();
        this.mDataHandler = dataHandlerInterface;
    }

    public void onEventSent(ApiCallback apiCallback) {
        if (apiCallback != null) {
            UnsentEventSnapshot unsentEventSnapshot = null;
            synchronized (this.mUnsentEventsMap) {
                if (this.mUnsentEventsMap.containsKey(apiCallback)) {
                    unsentEventSnapshot = (UnsentEventSnapshot) this.mUnsentEventsMap.get(apiCallback);
                }
            }
            if (unsentEventSnapshot != null) {
                if (unsentEventSnapshot.mEventDescription != null) {
                    String str = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Resend Succeeded [");
                    sb.append(unsentEventSnapshot.mEventDescription);
                    sb.append("]");
                    Log.d(str, sb.toString());
                }
                unsentEventSnapshot.stopTimers();
                synchronized (this.mUnsentEventsMap) {
                    this.mUnsentEventsMap.remove(apiCallback);
                    this.mUnsentEvents.remove(unsentEventSnapshot);
                }
                resentUnsents();
            }
        }
    }

    public void clear() {
        synchronized (this.mUnsentEventsMap) {
            for (UnsentEventSnapshot stopTimers : this.mUnsentEvents) {
                stopTimers.stopTimers();
            }
            this.mUnsentEvents.clear();
            this.mUnsentEventsMap.clear();
        }
    }

    public NetworkConnectivityReceiver getNetworkConnectivityReceiver() {
        return this.mNetworkConnectivityReceiver;
    }

    public Context getContext() {
        return this.mDataHandler.getContext();
    }

    /* access modifiers changed from: private */
    public static void triggerErrorCallback(DataHandlerInterface dataHandlerInterface, String str, Response response, Exception exc, ApiCallback apiCallback) {
        MatrixError matrixError;
        if (exc != null && !TextUtils.isEmpty(exc.getMessage())) {
            Log.e(LOG_TAG, exc.getLocalizedMessage(), exc);
        }
        String str2 = "Unexpected Error ";
        String str3 = "Exception UnexpectedError ";
        if (exc == null) {
            if (str != null) {
                try {
                    String str4 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append(str2);
                    sb.append(str);
                    Log.e(str4, sb.toString());
                } catch (Exception e) {
                    String str5 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str3);
                    sb2.append(e.getMessage());
                    Log.e(str5, sb2.toString(), e);
                    return;
                }
            }
            if (apiCallback != null) {
                apiCallback.onUnexpectedError(null);
            }
        } else if (exc instanceof IOException) {
            if (str != null) {
                try {
                    String str6 = LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Network Error ");
                    sb3.append(str);
                    Log.e(str6, sb3.toString());
                } catch (Exception e2) {
                    String str7 = LOG_TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Exception NetworkError ");
                    sb4.append(e2.getMessage());
                    Log.e(str7, sb4.toString(), e2);
                    return;
                }
            }
            if (apiCallback != null) {
                apiCallback.onNetworkError(exc);
            }
        } else {
            try {
                matrixError = (MatrixError) GsonProvider.provideGson().fromJson(response.errorBody().string(), MatrixError.class);
            } catch (Exception unused) {
                matrixError = null;
            }
            if (matrixError != null) {
                if (str != null) {
                    try {
                        String str8 = LOG_TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("Matrix Error ");
                        sb5.append(matrixError);
                        sb5.append(" ");
                        sb5.append(str);
                        Log.e(str8, sb5.toString());
                    } catch (Exception e3) {
                        String str9 = LOG_TAG;
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("Exception MatrixError ");
                        sb6.append(e3.getLocalizedMessage());
                        Log.e(str9, sb6.toString(), e3);
                        return;
                    }
                }
                if (MatrixError.isConfigurationErrorCode(matrixError.errcode)) {
                    dataHandlerInterface.onConfigurationError(matrixError.errcode);
                } else if (apiCallback != null) {
                    apiCallback.onMatrixError(matrixError);
                }
            } else {
                if (str != null) {
                    try {
                        String str10 = LOG_TAG;
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append(str2);
                        sb7.append(str);
                        Log.e(str10, sb7.toString());
                    } catch (Exception e4) {
                        String str11 = LOG_TAG;
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append(str3);
                        sb8.append(e4.getLocalizedMessage());
                        Log.e(str11, sb8.toString(), e4);
                        return;
                    }
                }
                if (apiCallback != null) {
                    apiCallback.onUnexpectedError(exc);
                }
            }
        }
    }

    public void onConfigurationErrorCode(String str, String str2) {
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(" failed because of an unknown matrix token");
        Log.e(str3, sb.toString());
        this.mDataHandler.onConfigurationError(str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:111:0x0266  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x008a  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00bd  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00e8  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x01f2  */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x022f  */
    public void onEventSendingFailed(String str, boolean z, Response response, Exception exc, ApiCallback apiCallback, RequestRetryCallBack requestRetryCallBack) {
        Response response2;
        String str2;
        MatrixError matrixError;
        int i;
        UnsentEventSnapshot unsentEventSnapshot;
        long j;
        String str3 = str;
        Response response3 = response;
        Exception exc2 = exc;
        ApiCallback apiCallback2 = apiCallback;
        RequestRetryCallBack requestRetryCallBack2 = requestRetryCallBack;
        if (str3 != null) {
            String str4 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Fail to send [");
            sb.append(str3);
            sb.append("]");
            Log.d(str4, sb.toString());
        }
        boolean z2 = false;
        if (requestRetryCallBack2 == null || apiCallback2 == null) {
            str2 = str3;
            response2 = response3;
        } else {
            synchronized (this.mUnsentEventsMap) {
                if (response3 != null) {
                    try {
                        matrixError = (MatrixError) GsonProvider.provideGson().fromJson(response.errorBody().string(), MatrixError.class);
                    } catch (Exception unused) {
                    }
                    if (!(str3 == null || matrixError == null)) {
                        String str5 = LOG_TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Matrix error ");
                        sb2.append(matrixError.errcode);
                        sb2.append(" ");
                        sb2.append(matrixError.getMessage());
                        sb2.append(" [");
                        sb2.append(str3);
                        sb2.append("]");
                        Log.d(str5, sb2.toString());
                        if (MatrixError.isConfigurationErrorCode(matrixError.errcode)) {
                            Log.e(LOG_TAG, "## onEventSendingFailed() : invalid token detected");
                            this.mDataHandler.onConfigurationError(matrixError.errcode);
                            triggerErrorCallback(this.mDataHandler, str3, response3, exc2, apiCallback2);
                            return;
                        }
                    }
                    int intValue = (matrixError != null || !MatrixError.LIMIT_EXCEEDED.equals(matrixError.errcode) || matrixError.retry_after_ms == null) ? -1 : matrixError.retry_after_ms.intValue() + 200;
                    if (exc2 != null) {
                        UnrecognizedCertificateException certificateException = CertUtil.getCertificateException(exc);
                        if (certificateException != null) {
                            Log.e(LOG_TAG, "## onEventSendingFailed() : SSL issue detected");
                            this.mDataHandler.onSSLCertificateError(certificateException);
                            triggerErrorCallback(this.mDataHandler, str3, response3, exc2, apiCallback2);
                            return;
                        }
                    }
                    if (matrixError != null && matrixError.isSupportedErrorCode()) {
                        if (MatrixError.LIMIT_EXCEEDED.equals(matrixError.errcode)) {
                            str2 = str3;
                            response2 = response3;
                        }
                    }
                    boolean z3 = true;
                    if (this.mUnsentEventsMap.containsKey(apiCallback2)) {
                        UnsentEventSnapshot unsentEventSnapshot2 = (UnsentEventSnapshot) this.mUnsentEventsMap.get(apiCallback2);
                        unsentEventSnapshot2.mIsResending = false;
                        unsentEventSnapshot2.stopTimer();
                        if (intValue < 0) {
                            unsentEventSnapshot2.mRetryCount = unsentEventSnapshot2.mRetryCount + 1;
                        }
                        long j2 = 0;
                        if (unsentEventSnapshot2.mAge > 0) {
                            j2 = System.currentTimeMillis() - unsentEventSnapshot2.mAge;
                        }
                        if (j2 > 180000 || unsentEventSnapshot2.mRetryCount > 4) {
                            unsentEventSnapshot2.stopTimers();
                            this.mUnsentEventsMap.remove(apiCallback2);
                            this.mUnsentEvents.remove(unsentEventSnapshot2);
                            if (str3 != null) {
                                String str6 = LOG_TAG;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("Cancel [");
                                sb3.append(str3);
                                sb3.append("]");
                                Log.d(str6, sb3.toString());
                            }
                            z3 = false;
                        }
                        str2 = str3;
                        response2 = response3;
                        i = intValue;
                        unsentEventSnapshot = unsentEventSnapshot2;
                    } else {
                        UnsentEventSnapshot unsentEventSnapshot3 = new UnsentEventSnapshot();
                        if (z) {
                            j = -1;
                        } else {
                            try {
                                j = System.currentTimeMillis();
                            } catch (Throwable th) {
                                th = th;
                                i = intValue;
                                unsentEventSnapshot = unsentEventSnapshot3;
                                String str7 = LOG_TAG;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("## snapshot creation failed ");
                                sb4.append(th.getMessage());
                                Log.e(str7, sb4.toString(), th);
                                if (unsentEventSnapshot.mLifeTimeTimer != null) {
                                    unsentEventSnapshot.mLifeTimeTimer.cancel();
                                }
                                this.mUnsentEventsMap.remove(apiCallback2);
                                this.mUnsentEvents.remove(unsentEventSnapshot);
                                try {
                                    str2 = str;
                                    response2 = response;
                                    try {
                                        triggerErrorCallback(this.mDataHandler, str2, response2, exc2, apiCallback2);
                                    } catch (Exception e) {
                                        e = e;
                                    }
                                } catch (Exception e2) {
                                    e = e2;
                                    str2 = str;
                                    response2 = response;
                                    String str8 = LOG_TAG;
                                    StringBuilder sb5 = new StringBuilder();
                                    sb5.append("## onEventSendingFailed() : failure Msg=");
                                    sb5.append(e.getMessage());
                                    Log.e(str8, sb5.toString(), e);
                                    z3 = true;
                                    if (z3) {
                                    }
                                    z2 = z3;
                                    if (!z2) {
                                    }
                                }
                                z3 = true;
                                if (z3) {
                                }
                                z2 = z3;
                                if (!z2) {
                                }
                            }
                        }
                        unsentEventSnapshot3.mAge = j;
                        unsentEventSnapshot3.mRequestRetryCallBack = requestRetryCallBack2;
                        unsentEventSnapshot3.mRetryCount = 1;
                        unsentEventSnapshot3.mEventDescription = str3;
                        this.mUnsentEventsMap.put(apiCallback2, unsentEventSnapshot3);
                        this.mUnsentEvents.add(unsentEventSnapshot3);
                        if (!this.mbIsConnected) {
                            if (z) {
                                if (z) {
                                    String str9 = LOG_TAG;
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append("The request ");
                                    sb6.append(str3);
                                    sb6.append(" will be sent when a network will be available");
                                    Log.d(str9, sb6.toString());
                                }
                                i = intValue;
                                unsentEventSnapshot = unsentEventSnapshot3;
                                str2 = str;
                                response2 = response;
                                z3 = true;
                            }
                        }
                        unsentEventSnapshot3.mLifeTimeTimer = new Timer();
                        Timer timer = unsentEventSnapshot3.mLifeTimeTimer;
                        r1 = r1;
                        final String str10 = str;
                        AnonymousClass2 r15 = r1;
                        final UnsentEventSnapshot unsentEventSnapshot4 = unsentEventSnapshot3;
                        i = intValue;
                        unsentEventSnapshot = unsentEventSnapshot3;
                        final ApiCallback apiCallback3 = apiCallback;
                        final Response response4 = response;
                        final Exception exc3 = exc;
                        try {
                            AnonymousClass2 r1 = new TimerTask() {
                                public void run() {
                                    try {
                                        if (str10 != null) {
                                            String access$000 = UnsentEventsManager.LOG_TAG;
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("Cancel to send [");
                                            sb.append(str10);
                                            sb.append("]");
                                            Log.d(access$000, sb.toString());
                                        }
                                        unsentEventSnapshot4.stopTimers();
                                        synchronized (UnsentEventsManager.this.mUnsentEventsMap) {
                                            UnsentEventsManager.this.mUnsentEventsMap.remove(apiCallback3);
                                            UnsentEventsManager.this.mUnsentEvents.remove(unsentEventSnapshot4);
                                        }
                                        UnsentEventsManager.triggerErrorCallback(UnsentEventsManager.this.mDataHandler, str10, response4, exc3, apiCallback3);
                                    } catch (Exception e) {
                                        String access$0002 = UnsentEventsManager.LOG_TAG;
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append("## onEventSendingFailed() : failure Msg=");
                                        sb2.append(e.getMessage());
                                        Log.e(access$0002, sb2.toString(), e);
                                    }
                                }
                            };
                            timer.schedule(r15, 180000);
                            str2 = str;
                            response2 = response;
                        } catch (Throwable th2) {
                            th = th2;
                            String str72 = LOG_TAG;
                            StringBuilder sb42 = new StringBuilder();
                            sb42.append("## snapshot creation failed ");
                            sb42.append(th.getMessage());
                            Log.e(str72, sb42.toString(), th);
                            if (unsentEventSnapshot.mLifeTimeTimer != null) {
                            }
                            this.mUnsentEventsMap.remove(apiCallback2);
                            this.mUnsentEvents.remove(unsentEventSnapshot);
                            str2 = str;
                            response2 = response;
                            triggerErrorCallback(this.mDataHandler, str2, response2, exc2, apiCallback2);
                            z3 = true;
                            if (z3) {
                            }
                            z2 = z3;
                            if (!z2) {
                            }
                        }
                        z3 = true;
                    }
                    if (z3 || !this.mbIsConnected) {
                        z2 = z3;
                    } else {
                        int pow = ((int) Math.pow(2.0d, (double) unsentEventSnapshot.mRetryCount)) + (Math.abs(new Random(System.currentTimeMillis()).nextInt()) % 3000);
                        if (i > 0) {
                            pow = i;
                        }
                        z2 = unsentEventSnapshot.resendEventAfter(pow);
                    }
                }
                matrixError = null;
                String str52 = LOG_TAG;
                StringBuilder sb22 = new StringBuilder();
                sb22.append("Matrix error ");
                sb22.append(matrixError.errcode);
                sb22.append(" ");
                sb22.append(matrixError.getMessage());
                sb22.append(" [");
                sb22.append(str3);
                sb22.append("]");
                Log.d(str52, sb22.toString());
                if (MatrixError.isConfigurationErrorCode(matrixError.errcode)) {
                }
                if (matrixError != null) {
                }
                if (exc2 != null) {
                }
                if (MatrixError.LIMIT_EXCEEDED.equals(matrixError.errcode)) {
                }
            }
        }
        if (!z2) {
            Log.d(LOG_TAG, "Cannot resend it");
            triggerErrorCallback(this.mDataHandler, str2, response2, exc2, apiCallback2);
        }
    }

    /* access modifiers changed from: private */
    public void resentUnsents() {
        Log.d(LOG_TAG, "resentUnsents");
        synchronized (this.mUnsentEventsMap) {
            if (this.mUnsentEvents.size() > 0) {
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < this.mUnsentEvents.size(); i++) {
                    UnsentEventSnapshot unsentEventSnapshot = (UnsentEventSnapshot) this.mUnsentEvents.get(i);
                    if (!unsentEventSnapshot.waitToBeResent()) {
                        if (!unsentEventSnapshot.mIsResending) {
                            if (unsentEventSnapshot.mEventDescription != null) {
                                String str = LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("Automatically resend ");
                                sb.append(unsentEventSnapshot.mEventDescription);
                                Log.d(str, sb.toString());
                            }
                            try {
                                unsentEventSnapshot.mIsResending = true;
                                unsentEventSnapshot.mRequestRetryCallBack.onRetry();
                                break;
                            } catch (Exception e) {
                                unsentEventSnapshot.mIsResending = false;
                                arrayList.add(unsentEventSnapshot);
                                String str2 = LOG_TAG;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("## resentUnsents() : ");
                                sb2.append(unsentEventSnapshot.mEventDescription);
                                sb2.append(" onRetry() failed ");
                                sb2.append(e.getMessage());
                                Log.e(str2, sb2.toString(), e);
                            }
                        }
                    }
                }
                this.mUnsentEvents.removeAll(arrayList);
            }
        }
    }
}
