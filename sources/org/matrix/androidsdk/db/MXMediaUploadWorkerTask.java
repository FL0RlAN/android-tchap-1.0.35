package org.matrix.androidsdk.db;

import android.os.AsyncTask;
import android.support.v4.media.session.PlaybackStateCompat;
import com.facebook.stetho.server.http.HttpHeaders;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.ContentManager;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.listeners.IMXMediaUploadListener;
import org.matrix.androidsdk.listeners.IMXMediaUploadListener.UploadStats;
import org.matrix.androidsdk.rest.model.ContentResponse;
import org.matrix.androidsdk.ssl.CertUtil;

public class MXMediaUploadWorkerTask extends AsyncTask<Void, Void, String> {
    private static final String LOG_TAG = MXMediaUploadWorkerTask.class.getSimpleName();
    private static final int UPLOAD_BUFFER_READ_SIZE = 32768;
    private static final Map<String, MXMediaUploadWorkerTask> mPendingUploadByUploadId = new HashMap();
    private final ApiCallback mApiCallback = new ApiCallback() {
        public void onMatrixError(MatrixError matrixError) {
        }

        public void onNetworkError(Exception exc) {
        }

        public void onSuccess(Object obj) {
        }

        public void onUnexpectedError(Exception exc) {
            MXMediaUploadWorkerTask mXMediaUploadWorkerTask = MXMediaUploadWorkerTask.this;
            mXMediaUploadWorkerTask.dispatchResult(mXMediaUploadWorkerTask.mResponseFromServer);
        }
    };
    private final ContentManager mContentManager;
    private final InputStream mContentStream;
    private String mFilename;
    private boolean mIsCancelled;
    /* access modifiers changed from: private */
    public boolean mIsDone;
    private final String mMimeType;
    private int mResponseCode = -1;
    /* access modifiers changed from: private */
    public String mResponseFromServer;
    private final String mUploadId;
    private final List<IMXMediaUploadListener> mUploadListeners = new ArrayList();
    private UploadStats mUploadStats;

    public static MXMediaUploadWorkerTask getMediaUploadWorkerTask(String str) {
        MXMediaUploadWorkerTask mXMediaUploadWorkerTask = null;
        if (str == null) {
            return null;
        }
        synchronized (mPendingUploadByUploadId) {
            if (mPendingUploadByUploadId.containsKey(str)) {
                mXMediaUploadWorkerTask = (MXMediaUploadWorkerTask) mPendingUploadByUploadId.get(str);
            }
        }
        return mXMediaUploadWorkerTask;
    }

    public static void cancelPendingUploads() {
        for (MXMediaUploadWorkerTask mXMediaUploadWorkerTask : mPendingUploadByUploadId.values()) {
            try {
                mXMediaUploadWorkerTask.cancelUpload();
                mXMediaUploadWorkerTask.cancel(true);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("cancelPendingUploads ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
        mPendingUploadByUploadId.clear();
    }

    public MXMediaUploadWorkerTask(ContentManager contentManager, InputStream inputStream, String str, String str2, String str3, IMXMediaUploadListener iMXMediaUploadListener) {
        if (inputStream.markSupported()) {
            try {
                inputStream.reset();
            } catch (Exception e) {
                String str4 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("MXMediaUploadWorkerTask ");
                sb.append(e.getMessage());
                Log.e(str4, sb.toString(), e);
            }
        } else {
            Log.w(LOG_TAG, "Warning, reset() is not supported for this stream");
        }
        this.mContentManager = contentManager;
        this.mContentStream = inputStream;
        this.mMimeType = str;
        this.mUploadId = str2;
        this.mFilename = str3;
        addListener(iMXMediaUploadListener);
        if (str2 != null) {
            mPendingUploadByUploadId.put(str2, this);
        }
    }

    public void addListener(IMXMediaUploadListener iMXMediaUploadListener) {
        if (iMXMediaUploadListener != null && this.mUploadListeners.indexOf(iMXMediaUploadListener) < 0) {
            this.mUploadListeners.add(iMXMediaUploadListener);
        }
    }

    public int getProgress() {
        UploadStats uploadStats = this.mUploadStats;
        if (uploadStats != null) {
            return uploadStats.mProgress;
        }
        return -1;
    }

    public UploadStats getStats() {
        return this.mUploadStats;
    }

    private synchronized boolean isUploadCancelled() {
        return this.mIsCancelled;
    }

    public synchronized void cancelUpload() {
        this.mIsCancelled = true;
    }

    /* access modifiers changed from: private */
    public void publishProgress(long j) {
        this.mUploadStats.mElapsedTime = (int) ((System.currentTimeMillis() - j) / 1000);
        if (this.mUploadStats.mFileSize != 0) {
            UploadStats uploadStats = this.mUploadStats;
            uploadStats.mProgress = (int) ((((long) uploadStats.mUploadedSize) * 96) / ((long) this.mUploadStats.mFileSize));
        }
        if (System.currentTimeMillis() != j) {
            UploadStats uploadStats2 = this.mUploadStats;
            uploadStats2.mBitRate = (int) (((((long) uploadStats2.mUploadedSize) * 1000) / (System.currentTimeMillis() - j)) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
        } else {
            this.mUploadStats.mBitRate = 0;
        }
        if (this.mUploadStats.mBitRate != 0) {
            UploadStats uploadStats3 = this.mUploadStats;
            uploadStats3.mEstimatedRemainingTime = ((uploadStats3.mFileSize - this.mUploadStats.mUploadedSize) / 1024) / this.mUploadStats.mBitRate;
        } else {
            this.mUploadStats.mEstimatedRemainingTime = -1;
        }
        publishProgress(new Void[0]);
    }

    /* access modifiers changed from: protected */
    public String doInBackground(Void... voidArr) {
        String str;
        InputStream inputStream;
        this.mResponseCode = -1;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mContentManager.getHsConfig().getHomeserverUri().toString());
        sb.append(ContentManager.URI_PREFIX_CONTENT_API);
        sb.append("upload");
        String sb2 = sb.toString();
        String str2 = this.mFilename;
        if (str2 != null) {
            try {
                String encode = URLEncoder.encode(str2, "utf-8");
                StringBuilder sb3 = new StringBuilder();
                sb3.append(sb2);
                sb3.append("?filename=");
                sb3.append(encode);
                sb2 = sb3.toString();
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("doInBackground ");
                sb4.append(e.getMessage());
                Log.e(str3, sb4.toString(), e);
            }
        }
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(sb2).openConnection();
            if (RestClient.getUserAgent() != null) {
                httpURLConnection.setRequestProperty("User-Agent", RestClient.getUserAgent());
            }
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Bearer ");
            sb5.append(this.mContentManager.getHsConfig().getCredentials().accessToken);
            httpURLConnection.setRequestProperty("Authorization", sb5.toString());
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            if (httpURLConnection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
                try {
                    httpsURLConnection.setSSLSocketFactory((SSLSocketFactory) CertUtil.newPinnedSSLSocketFactory(this.mContentManager.getHsConfig()).first);
                    httpsURLConnection.setHostnameVerifier(CertUtil.newHostnameVerifier(this.mContentManager.getHsConfig()));
                } catch (Exception e2) {
                    String str4 = LOG_TAG;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("sslConn ");
                    sb6.append(e2.getMessage());
                    Log.e(str4, sb6.toString(), e2);
                }
            }
            httpURLConnection.setRequestProperty(HttpHeaders.CONTENT_TYPE, this.mMimeType);
            httpURLConnection.setRequestProperty(HttpHeaders.CONTENT_LENGTH, Integer.toString(this.mContentStream.available()));
            httpURLConnection.setFixedLengthStreamingMode(this.mContentStream.available());
            httpURLConnection.connect();
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            int available = this.mContentStream.available();
            int min = Math.min(available, 32768);
            byte[] bArr = new byte[min];
            this.mUploadStats = new UploadStats();
            this.mUploadStats.mUploadId = this.mUploadId;
            this.mUploadStats.mProgress = 0;
            this.mUploadStats.mUploadedSize = 0;
            this.mUploadStats.mFileSize = available;
            this.mUploadStats.mElapsedTime = 0;
            this.mUploadStats.mEstimatedRemainingTime = -1;
            this.mUploadStats.mBitRate = 0;
            final long currentTimeMillis = System.currentTimeMillis();
            String str5 = LOG_TAG;
            StringBuilder sb7 = new StringBuilder();
            sb7.append("doInBackground : start Upload (");
            sb7.append(available);
            sb7.append(" bytes)");
            Log.d(str5, sb7.toString());
            int read = this.mContentStream.read(bArr, 0, min);
            dispatchOnUploadStart();
            Timer timer = new Timer();
            HttpURLConnection httpURLConnection2 = httpURLConnection;
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    if (!MXMediaUploadWorkerTask.this.mIsDone) {
                        MXMediaUploadWorkerTask.this.publishProgress(currentTimeMillis);
                    }
                }
            }, new Date(), 100);
            int i = 0;
            while (read > 0 && !isUploadCancelled()) {
                dataOutputStream.write(bArr, 0, read);
                i += read;
                int min2 = Math.min(this.mContentStream.available(), 32768);
                String str6 = LOG_TAG;
                StringBuilder sb8 = new StringBuilder();
                sb8.append("doInBackground : totalWritten ");
                sb8.append(i);
                sb8.append(" / totalSize ");
                sb8.append(available);
                Log.d(str6, sb8.toString());
                this.mUploadStats.mUploadedSize = i;
                read = this.mContentStream.read(bArr, 0, min2);
            }
            this.mIsDone = true;
            timer.cancel();
            if (!isUploadCancelled()) {
                this.mUploadStats.mProgress = 96;
                publishProgress(currentTimeMillis);
                dataOutputStream.flush();
                this.mUploadStats.mProgress = 97;
                publishProgress(currentTimeMillis);
                dataOutputStream.close();
                this.mUploadStats.mProgress = 98;
                publishProgress(currentTimeMillis);
                try {
                    this.mResponseCode = httpURLConnection2.getResponseCode();
                } catch (EOFException unused) {
                    this.mResponseCode = 500;
                }
                this.mUploadStats.mProgress = 99;
                publishProgress(currentTimeMillis);
                String str7 = LOG_TAG;
                StringBuilder sb9 = new StringBuilder();
                sb9.append("doInBackground : Upload is done with response code ");
                sb9.append(this.mResponseCode);
                Log.d(str7, sb9.toString());
                if (this.mResponseCode == 200) {
                    inputStream = httpURLConnection2.getInputStream();
                } else {
                    inputStream = httpURLConnection2.getErrorStream();
                }
                StringBuffer stringBuffer = new StringBuffer();
                while (true) {
                    int read2 = inputStream.read();
                    if (read2 == -1) {
                        break;
                    }
                    stringBuffer.append((char) read2);
                }
                str = stringBuffer.toString();
                inputStream.close();
                if (this.mResponseCode != 200) {
                    try {
                        str = new JSONObject(str).getString("error");
                    } catch (JSONException e3) {
                        String str8 = LOG_TAG;
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append("doInBackground : Error parsing ");
                        sb10.append(e3.getMessage());
                        Log.e(str8, sb10.toString(), e3);
                    }
                }
            } else {
                dataOutputStream.flush();
                dataOutputStream.close();
                str = null;
            }
            if (httpURLConnection2 != null) {
                httpURLConnection2.disconnect();
            }
        } catch (Exception e4) {
            str = e4.getLocalizedMessage();
            String str9 = LOG_TAG;
            StringBuilder sb11 = new StringBuilder();
            sb11.append("doInBackground ; failed with error ");
            sb11.append(e4.getClass());
            sb11.append(" - ");
            sb11.append(e4.getMessage());
            Log.e(str9, sb11.toString(), e4);
        }
        this.mResponseFromServer = str;
        return str;
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(Void... voidArr) {
        super.onProgressUpdate(new Void[0]);
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Upload ");
        sb.append(this);
        sb.append(" : ");
        sb.append(this.mUploadStats.mProgress);
        Log.d(str, sb.toString());
        dispatchOnUploadProgress(this.mUploadStats);
    }

    /* access modifiers changed from: private */
    public void dispatchResult(String str) {
        String str2 = this.mUploadId;
        if (str2 != null) {
            mPendingUploadByUploadId.remove(str2);
        }
        this.mContentManager.getUnsentEventsManager().onEventSent(this.mApiCallback);
        try {
            this.mContentStream.close();
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("dispatchResult ");
            sb.append(e.getMessage());
            Log.e(str3, sb.toString(), e);
        }
        if (isUploadCancelled()) {
            dispatchOnUploadCancel();
            return;
        }
        ContentResponse contentResponse = (this.mResponseCode != 200 || str == null) ? null : JsonUtils.toContentResponse(str);
        if (contentResponse == null || contentResponse.contentUri == null) {
            dispatchOnUploadError(this.mResponseCode, str);
        } else {
            dispatchOnUploadComplete(contentResponse.contentUri);
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(String str) {
        if (!isCancelled()) {
            dispatchResult(str);
        }
    }

    private void dispatchOnUploadStart() {
        for (IMXMediaUploadListener onUploadStart : this.mUploadListeners) {
            try {
                onUploadStart.onUploadStart(this.mUploadId);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("dispatchOnUploadStart failed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
    }

    private void dispatchOnUploadProgress(UploadStats uploadStats) {
        for (IMXMediaUploadListener onUploadProgress : this.mUploadListeners) {
            try {
                onUploadProgress.onUploadProgress(this.mUploadId, uploadStats);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("dispatchOnUploadProgress failed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
    }

    private void dispatchOnUploadCancel() {
        for (IMXMediaUploadListener onUploadCancel : this.mUploadListeners) {
            try {
                onUploadCancel.onUploadCancel(this.mUploadId);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("listener failed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
    }

    private void dispatchOnUploadError(int i, String str) {
        for (IMXMediaUploadListener onUploadError : this.mUploadListeners) {
            try {
                onUploadError.onUploadError(this.mUploadId, i, str);
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("dispatchOnUploadError failed ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
    }

    private void dispatchOnUploadComplete(String str) {
        for (IMXMediaUploadListener onUploadComplete : this.mUploadListeners) {
            try {
                onUploadComplete.onUploadComplete(this.mUploadId, str);
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("dispatchOnUploadComplete failed ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
    }
}
