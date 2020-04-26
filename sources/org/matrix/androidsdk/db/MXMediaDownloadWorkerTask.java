package org.matrix.androidsdk.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import androidx.collection.LruCache;
import com.facebook.stetho.server.http.HttpHeaders;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import kotlin.UByte;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.ImageUtils;
import org.matrix.androidsdk.core.JsonUtility;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXOsHandler;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.MXEncryptedAttachments;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedBodyFileInfo;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;
import org.matrix.androidsdk.listeners.IMXMediaDownloadListener;
import org.matrix.androidsdk.listeners.IMXMediaDownloadListener.DownloadStats;
import org.matrix.androidsdk.network.NetworkConnectivityReceiver;
import org.matrix.androidsdk.rest.client.MediaScanRestClient;
import org.matrix.androidsdk.rest.model.EncryptedMediaScanBody;
import org.matrix.androidsdk.rest.model.EncryptedMediaScanEncryptedBody;
import org.matrix.androidsdk.ssl.CertUtil;
import org.matrix.olm.OlmPkEncryption;
import org.matrix.olm.OlmPkMessage;

class MXMediaDownloadWorkerTask extends AsyncTask<Void, Void, JsonElement> {
    private static final int DOWNLOAD_BUFFER_READ_SIZE = 32768;
    private static final int DOWNLOAD_TIME_OUT = 10000;
    /* access modifiers changed from: private */
    public static final String LOG_TAG = MXMediaDownloadWorkerTask.class.getSimpleName();
    /* access modifiers changed from: private */
    public static LruCache<String, Bitmap> sBitmapByDownloadIdCache = null;
    private static final Map<String, MXMediaDownloadWorkerTask> sPendingDownloadById = new HashMap();
    /* access modifiers changed from: private */
    public static final Object sSyncObject = new Object();
    private static final List<String> sUnreachableUrls = new ArrayList();
    private Context mApplicationContext;
    /* access modifiers changed from: private */
    public Bitmap mDefaultBitmap;
    private File mDirectoryFile;
    private String mDownloadId;
    private final List<IMXMediaDownloadListener> mDownloadListeners = new ArrayList();
    private DownloadStats mDownloadStats;
    private final EncryptedFileInfo mEncryptedFileInfo;
    private final HomeServerConnectionConfig mHsConfig;
    private final List<WeakReference<ImageView>> mImageViewReferences;
    private boolean mIsAvScannerEnabled;
    /* access modifiers changed from: private */
    public boolean mIsDone;
    private boolean mIsDownloadCancelled;
    private MediaScanRestClient mMediaScanRestClient;
    private String mMimeType;
    private final NetworkConnectivityReceiver mNetworkConnectivityReceiver;
    private int mRotation;
    private String mUrl;

    public static void clearBitmapsCache() {
        LruCache<String, Bitmap> lruCache = sBitmapByDownloadIdCache;
        if (lruCache != null) {
            lruCache.evictAll();
        }
        synchronized (sUnreachableUrls) {
            sUnreachableUrls.clear();
        }
    }

    public static MXMediaDownloadWorkerTask getMediaDownloadWorkerTask(String str) {
        MXMediaDownloadWorkerTask mXMediaDownloadWorkerTask;
        if (!sPendingDownloadById.containsKey(str)) {
            return null;
        }
        synchronized (sPendingDownloadById) {
            mXMediaDownloadWorkerTask = (MXMediaDownloadWorkerTask) sPendingDownloadById.get(str);
        }
        return mXMediaDownloadWorkerTask;
    }

    private static String uniqueId(String str) {
        String str2;
        try {
            byte[] digest = MessageDigest.getInstance("SHA1").digest(str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : digest) {
                stringBuffer.append(Integer.toString((b & UByte.MAX_VALUE) + 256, 16).substring(1));
            }
            str2 = stringBuffer.toString();
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("uniqueId failed ");
            sb.append(e.getMessage());
            Log.e(str3, sb.toString(), e);
            str2 = null;
        }
        if (str2 != null) {
            return str2;
        }
        StringBuilder sb2 = new StringBuilder();
        String str4 = "";
        sb2.append(str4);
        int hashCode = str.hashCode();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(System.currentTimeMillis());
        sb3.append(str4);
        sb2.append(Math.abs(hashCode + sb3.toString().hashCode()));
        return sb2.toString();
    }

    static String buildFileName(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append("file_");
        sb.append(uniqueId(str));
        String sb2 = sb.toString();
        if (TextUtils.isEmpty(str2)) {
            return sb2;
        }
        String extensionFromMimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(str2);
        if ("jpeg".equals(extensionFromMimeType)) {
            extensionFromMimeType = "jpg";
        }
        if (extensionFromMimeType == null) {
            return sb2;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append(".");
        sb3.append(extensionFromMimeType);
        return sb3.toString();
    }

    public static boolean isMediaCached(String str) {
        boolean z = false;
        if (sBitmapByDownloadIdCache != null) {
            synchronized (sSyncObject) {
                if (sBitmapByDownloadIdCache.get(str) != null) {
                    z = true;
                }
            }
        }
        return z;
    }

    public static boolean isMediaUrlUnreachable(String str) {
        boolean contains;
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        synchronized (sUnreachableUrls) {
            contains = sUnreachableUrls.contains(str);
        }
        return contains;
    }

    static boolean bitmapForURL(Context context, File file, String str, String str2, int i, String str3, EncryptedFileInfo encryptedFileInfo, ApiCallback<Bitmap> apiCallback) {
        final Bitmap bitmap;
        File file2 = file;
        String str4 = str2;
        if (TextUtils.isEmpty(str)) {
            Log.d(LOG_TAG, "bitmapForURL : null url");
            return false;
        }
        if (sBitmapByDownloadIdCache == null) {
            int min = Math.min(20971520, ((int) Runtime.getRuntime().maxMemory()) / 8);
            String str5 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("bitmapForURL  lruSize : ");
            sb.append(min);
            Log.d(str5, sb.toString());
            sBitmapByDownloadIdCache = new LruCache<String, Bitmap>(min) {
                /* access modifiers changed from: protected */
                public int sizeOf(String str, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }
        if (getMediaDownloadWorkerTask(str2) != null || isMediaUrlUnreachable(str)) {
            return false;
        }
        synchronized (sSyncObject) {
            bitmap = (Bitmap) sBitmapByDownloadIdCache.get(str2);
        }
        if (bitmap != null) {
            final ApiCallback<Bitmap> apiCallback2 = apiCallback;
            MXMediaCache.mUIHandler.post(new Runnable() {
                public void run() {
                    apiCallback2.onSuccess(bitmap);
                }
            });
            return true;
        }
        ApiCallback<Bitmap> apiCallback3 = apiCallback;
        if (file2 == null) {
            return false;
        }
        String str6 = null;
        String str7 = str;
        if (str.startsWith("file:")) {
            try {
                str6 = Uri.parse(str).getPath();
            } catch (Exception e) {
                String str8 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("bitmapForURL #1 : ");
                sb2.append(e.getMessage());
                Log.e(str8, sb2.toString(), e);
            }
            if (str6 == null) {
                return false;
            }
        }
        final String buildFileName = str6 == null ? buildFileName(str2, str3) : str6;
        final File file3 = buildFileName.startsWith(File.separator) ? new File(buildFileName) : new File(file, buildFileName);
        if (!file3.exists()) {
            return false;
        }
        MXOsHandler mXOsHandler = MXMediaCache.mDecryptingHandler;
        final int i2 = i;
        final EncryptedFileInfo encryptedFileInfo2 = encryptedFileInfo;
        final Context context2 = context;
        final String str9 = str2;
        final ApiCallback<Bitmap> apiCallback4 = apiCallback;
        AnonymousClass3 r1 = new Runnable() {
            /* JADX WARNING: Code restructure failed: missing block: B:48:0x00de, code lost:
                r1 = r0;
             */
            /* JADX WARNING: Removed duplicated region for block: B:28:0x0080 A[SYNTHETIC, Splitter:B:28:0x0080] */
            public void run() {
                Bitmap bitmap;
                Throwable th;
                Bitmap bitmap2;
                int i = i2;
                final Bitmap bitmap3 = null;
                try {
                    InputStream fileInputStream = new FileInputStream(file3);
                    if (encryptedFileInfo2 != null) {
                        InputStream decryptAttachment = MXEncryptedAttachments.decryptAttachment(fileInputStream, encryptedFileInfo2);
                        fileInputStream.close();
                        fileInputStream = decryptAttachment;
                    }
                    if (Integer.MAX_VALUE == i) {
                        i = ImageUtils.getRotationAngleForBitmap(context2, Uri.fromFile(file3));
                    }
                    if (fileInputStream != null) {
                        Options options = new Options();
                        options.inPreferredConfig = Config.ARGB_8888;
                        try {
                            bitmap = BitmapFactory.decodeStream(fileInputStream, null, options);
                        } catch (OutOfMemoryError e) {
                            System.gc();
                            String access$000 = MXMediaDownloadWorkerTask.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("bitmapForURL() : Out of memory 1 ");
                            sb.append(e);
                            Log.e(access$000, sb.toString(), e);
                            bitmap = null;
                        }
                        if (bitmap == null) {
                            try {
                                bitmap3 = BitmapFactory.decodeStream(fileInputStream, null, options);
                            } catch (OutOfMemoryError e2) {
                                try {
                                    String access$0002 = MXMediaDownloadWorkerTask.LOG_TAG;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("bitmapForURL() Out of memory 2 ");
                                    sb2.append(e2);
                                    Log.e(access$0002, sb2.toString(), e2);
                                } catch (FileNotFoundException unused) {
                                    bitmap3 = bitmap;
                                    String access$0003 = MXMediaDownloadWorkerTask.LOG_TAG;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append("bitmapForURL() : ");
                                    sb3.append(buildFileName);
                                    sb3.append(" does not exist");
                                    Log.d(access$0003, sb3.toString());
                                    MXMediaCache.mUIHandler.post(new Runnable() {
                                        public void run() {
                                            apiCallback4.onSuccess(bitmap3);
                                        }
                                    });
                                } catch (Exception e3) {
                                    e = e3;
                                    bitmap3 = bitmap;
                                    String access$0004 = MXMediaDownloadWorkerTask.LOG_TAG;
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("bitmapForURL() ");
                                    sb4.append(e);
                                    Log.e(access$0004, sb4.toString(), e);
                                    MXMediaCache.mUIHandler.post(new Runnable() {
                                        public void run() {
                                            apiCallback4.onSuccess(bitmap3);
                                        }
                                    });
                                }
                            }
                            if (bitmap3 != null) {
                                synchronized (MXMediaDownloadWorkerTask.sSyncObject) {
                                    if (i != 0) {
                                        try {
                                            Matrix matrix = new Matrix();
                                            matrix.postRotate((float) i);
                                            bitmap2 = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix, false);
                                            if (bitmap2 != bitmap3) {
                                                bitmap3.recycle();
                                            }
                                        } catch (OutOfMemoryError e4) {
                                            String access$0005 = MXMediaDownloadWorkerTask.LOG_TAG;
                                            StringBuilder sb5 = new StringBuilder();
                                            sb5.append("bitmapForURL rotation error : ");
                                            sb5.append(e4.getMessage());
                                            Log.e(access$0005, sb5.toString(), e4);
                                        } catch (Throwable th2) {
                                            th = th2;
                                            throw th;
                                        }
                                        if (bitmap2.getWidth() < 1000 && bitmap2.getHeight() < 1000) {
                                            MXMediaDownloadWorkerTask.sBitmapByDownloadIdCache.put(str9, bitmap2);
                                        }
                                    }
                                    bitmap2 = bitmap3;
                                    try {
                                        MXMediaDownloadWorkerTask.sBitmapByDownloadIdCache.put(str9, bitmap2);
                                    } catch (Throwable th3) {
                                        Throwable th4 = th3;
                                        bitmap3 = bitmap2;
                                        th = th4;
                                        throw th;
                                    }
                                }
                            }
                            fileInputStream.close();
                        }
                        bitmap3 = bitmap;
                        if (bitmap3 != null) {
                        }
                        fileInputStream.close();
                    }
                } catch (FileNotFoundException unused2) {
                    String access$00032 = MXMediaDownloadWorkerTask.LOG_TAG;
                    StringBuilder sb32 = new StringBuilder();
                    sb32.append("bitmapForURL() : ");
                    sb32.append(buildFileName);
                    sb32.append(" does not exist");
                    Log.d(access$00032, sb32.toString());
                    MXMediaCache.mUIHandler.post(new Runnable() {
                        public void run() {
                            apiCallback4.onSuccess(bitmap3);
                        }
                    });
                } catch (Exception e5) {
                    e = e5;
                    String access$00042 = MXMediaDownloadWorkerTask.LOG_TAG;
                    StringBuilder sb42 = new StringBuilder();
                    sb42.append("bitmapForURL() ");
                    sb42.append(e);
                    Log.e(access$00042, sb42.toString(), e);
                    MXMediaCache.mUIHandler.post(new Runnable() {
                        public void run() {
                            apiCallback4.onSuccess(bitmap3);
                        }
                    });
                }
                MXMediaCache.mUIHandler.post(new Runnable() {
                    public void run() {
                        apiCallback4.onSuccess(bitmap3);
                    }
                });
            }
        };
        mXOsHandler.post(r1);
        return true;
    }

    public MXMediaDownloadWorkerTask(Context context, HomeServerConnectionConfig homeServerConnectionConfig, NetworkConnectivityReceiver networkConnectivityReceiver, File file, String str, String str2, int i, String str3, EncryptedFileInfo encryptedFileInfo, MediaScanRestClient mediaScanRestClient, boolean z) {
        this.mApplicationContext = context;
        this.mHsConfig = homeServerConnectionConfig;
        this.mNetworkConnectivityReceiver = networkConnectivityReceiver;
        this.mDirectoryFile = file;
        this.mUrl = str;
        this.mDownloadId = str2;
        this.mRotation = i;
        this.mMimeType = str3;
        this.mEncryptedFileInfo = encryptedFileInfo;
        this.mMediaScanRestClient = mediaScanRestClient;
        this.mIsAvScannerEnabled = z;
        this.mImageViewReferences = new ArrayList();
        synchronized (sPendingDownloadById) {
            sPendingDownloadById.put(str2, this);
        }
    }

    public MXMediaDownloadWorkerTask(MXMediaDownloadWorkerTask mXMediaDownloadWorkerTask) {
        this.mApplicationContext = mXMediaDownloadWorkerTask.mApplicationContext;
        this.mHsConfig = mXMediaDownloadWorkerTask.mHsConfig;
        this.mNetworkConnectivityReceiver = mXMediaDownloadWorkerTask.mNetworkConnectivityReceiver;
        this.mDirectoryFile = mXMediaDownloadWorkerTask.mDirectoryFile;
        this.mUrl = mXMediaDownloadWorkerTask.mUrl;
        this.mDownloadId = mXMediaDownloadWorkerTask.mDownloadId;
        this.mRotation = mXMediaDownloadWorkerTask.mRotation;
        this.mMimeType = mXMediaDownloadWorkerTask.mMimeType;
        this.mEncryptedFileInfo = mXMediaDownloadWorkerTask.mEncryptedFileInfo;
        this.mIsAvScannerEnabled = mXMediaDownloadWorkerTask.mIsAvScannerEnabled;
        this.mMediaScanRestClient = mXMediaDownloadWorkerTask.mMediaScanRestClient;
        this.mImageViewReferences = mXMediaDownloadWorkerTask.mImageViewReferences;
        synchronized (sPendingDownloadById) {
            sPendingDownloadById.put(this.mDownloadId, this);
        }
    }

    public synchronized void cancelDownload() {
        this.mIsDownloadCancelled = true;
    }

    public synchronized boolean isDownloadCancelled() {
        return this.mIsDownloadCancelled;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void addImageView(ImageView imageView) {
        this.mImageViewReferences.add(new WeakReference(imageView));
    }

    public void setDefaultBitmap(Bitmap bitmap) {
        this.mDefaultBitmap = bitmap;
    }

    public void addDownloadListener(IMXMediaDownloadListener iMXMediaDownloadListener) {
        if (iMXMediaDownloadListener != null) {
            this.mDownloadListeners.add(iMXMediaDownloadListener);
        }
    }

    public int getProgress() {
        DownloadStats downloadStats = this.mDownloadStats;
        if (downloadStats != null) {
            return downloadStats.mProgress;
        }
        return -1;
    }

    public DownloadStats getDownloadStats() {
        return this.mDownloadStats;
    }

    private boolean isBitmapDownloadTask() {
        String str = this.mMimeType;
        return str != null && str.startsWith("image/");
    }

    /* access modifiers changed from: private */
    public void updateAndPublishProgress(long j) {
        this.mDownloadStats.mElapsedTime = (int) ((System.currentTimeMillis() - j) / 1000);
        if (this.mDownloadStats.mFileSize <= 0) {
            this.mDownloadStats.mProgress = -1;
        } else if (this.mDownloadStats.mDownloadedSize >= this.mDownloadStats.mFileSize) {
            this.mDownloadStats.mProgress = 99;
        } else {
            DownloadStats downloadStats = this.mDownloadStats;
            downloadStats.mProgress = (int) ((((long) downloadStats.mDownloadedSize) * 100) / ((long) this.mDownloadStats.mFileSize));
        }
        if (System.currentTimeMillis() != j) {
            DownloadStats downloadStats2 = this.mDownloadStats;
            downloadStats2.mBitRate = (int) (((((long) downloadStats2.mDownloadedSize) * 1000) / (System.currentTimeMillis() - j)) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
        } else {
            this.mDownloadStats.mBitRate = -1;
        }
        if (this.mDownloadStats.mBitRate == 0 || this.mDownloadStats.mFileSize <= 0 || this.mDownloadStats.mFileSize <= this.mDownloadStats.mDownloadedSize) {
            this.mDownloadStats.mEstimatedRemainingTime = -1;
        } else {
            DownloadStats downloadStats3 = this.mDownloadStats;
            downloadStats3.mEstimatedRemainingTime = ((downloadStats3.mFileSize - this.mDownloadStats.mDownloadedSize) / 1024) / this.mDownloadStats.mBitRate;
        }
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateAndPublishProgress ");
        sb.append(this);
        sb.append(" : ");
        sb.append(this.mDownloadStats.mProgress);
        Log.d(str, sb.toString());
        publishProgress(new Void[0]);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x0283 A[Catch:{ OutOfMemoryError -> 0x02c3, Exception -> 0x02a1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x029b A[Catch:{ OutOfMemoryError -> 0x02c3, Exception -> 0x02a1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x02ef A[SYNTHETIC, Splitter:B:132:0x02ef] */
    /* JADX WARNING: Removed duplicated region for block: B:142:0x033b A[Catch:{ Exception -> 0x0398 }] */
    /* JADX WARNING: Removed duplicated region for block: B:143:0x0357 A[Catch:{ Exception -> 0x0398 }] */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x03c3  */
    /* JADX WARNING: Removed duplicated region for block: B:160:0x03cf A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0179 A[SYNTHETIC, Splitter:B:65:0x0179] */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x01d1 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x0207 A[SYNTHETIC] */
    public JsonElement doInBackground(Void... voidArr) {
        JsonElement jsonElement;
        int i;
        HttpURLConnection httpURLConnection;
        InputStream errorStream;
        OutputStream outputStream;
        MatrixError matrixError = new MatrixError();
        matrixError.errcode = MatrixError.UNKNOWN;
        InputStream inputStream = null;
        try {
            URL url = new URL(this.mUrl);
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("MXMediaDownloadWorkerTask ");
            sb.append(this);
            sb.append(" starts");
            Log.d(str, sb.toString());
            this.mDownloadStats = new DownloadStats();
            this.mDownloadStats.mEstimatedRemainingTime = -1;
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                try {
                    if (RestClient.getUserAgent() != null) {
                        httpURLConnection.setRequestProperty("User-Agent", RestClient.getUserAgent());
                    }
                    if (this.mHsConfig != null && (httpURLConnection instanceof HttpsURLConnection)) {
                        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
                        try {
                            httpsURLConnection.setSSLSocketFactory((SSLSocketFactory) CertUtil.newPinnedSSLSocketFactory(this.mHsConfig).first);
                            httpsURLConnection.setHostnameVerifier(CertUtil.newHostnameVerifier(this.mHsConfig));
                        } catch (Exception e) {
                            String str2 = LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("doInBackground SSL exception ");
                            sb2.append(e.getMessage());
                            Log.e(str2, sb2.toString(), e);
                        }
                    }
                    httpURLConnection.setReadTimeout((int) ((this.mNetworkConnectivityReceiver != null ? this.mNetworkConnectivityReceiver.getTimeoutScale() : 1.0f) * 10000.0f));
                    if (this.mIsAvScannerEnabled && this.mEncryptedFileInfo != null) {
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setUseCaches(false);
                        EncryptedMediaScanBody encryptedMediaScanBody = new EncryptedMediaScanBody();
                        encryptedMediaScanBody.encryptedFileInfo = this.mEncryptedFileInfo;
                        String canonicalizedJsonString = JsonUtility.getCanonicalizedJsonString(encryptedMediaScanBody);
                        String antivirusServerPublicKey = getAntivirusServerPublicKey();
                        if (antivirusServerPublicKey != null) {
                            if (!TextUtils.isEmpty(antivirusServerPublicKey)) {
                                OlmPkEncryption olmPkEncryption = new OlmPkEncryption();
                                olmPkEncryption.setRecipientKey(antivirusServerPublicKey);
                                OlmPkMessage encrypt = olmPkEncryption.encrypt(canonicalizedJsonString);
                                EncryptedMediaScanEncryptedBody encryptedMediaScanEncryptedBody = new EncryptedMediaScanEncryptedBody();
                                encryptedMediaScanEncryptedBody.encryptedBodyFileInfo = new EncryptedBodyFileInfo(encrypt);
                                canonicalizedJsonString = JsonUtility.getCanonicalizedJsonString(encryptedMediaScanEncryptedBody);
                            }
                            outputStream = httpURLConnection.getOutputStream();
                            try {
                                outputStream.write(canonicalizedJsonString.getBytes("UTF-8"));
                            } catch (Exception e2) {
                                String str3 = LOG_TAG;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("doInBackground Failed to serialize encryption info ");
                                sb3.append(e2.getMessage());
                                Log.e(str3, sb3.toString(), e2);
                            }
                            outputStream.close();
                        } else {
                            throw new Exception("Unable to get public key");
                        }
                    }
                    i = httpURLConnection.getContentLength();
                    try {
                        jsonElement = null;
                        inputStream = httpURLConnection.getInputStream();
                    } catch (Exception e3) {
                        e = e3;
                        String str4 = LOG_TAG;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("bitmapForURL : fail to open the connection ");
                        sb4.append(e.getMessage());
                        Log.e(str4, sb4.toString(), e);
                        matrixError.error = e.getLocalizedMessage();
                        this.mMediaScanRestClient.resetServerPublicKey();
                        errorStream = httpURLConnection.getErrorStream();
                        if (errorStream != null) {
                        }
                        jsonElement = null;
                        try {
                            Log.d(LOG_TAG, "MediaWorkerTask an url does not exist");
                            synchronized (sUnreachableUrls) {
                            }
                            dispatchDownloadStart();
                            JsonParser jsonParser = new JsonParser();
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("Cannot open ");
                            sb5.append(this.mUrl);
                            jsonElement = jsonParser.parse(sb5.toString());
                            synchronized (sUnreachableUrls) {
                            }
                            final long currentTimeMillis = System.currentTimeMillis();
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append(buildFileName(this.mDownloadId, this.mMimeType));
                            sb6.append(".tmp");
                            String sb7 = sb6.toString();
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(this.mDirectoryFile, sb7));
                            this.mDownloadStats.mDownloadId = this.mDownloadId;
                            this.mDownloadStats.mProgress = 0;
                            this.mDownloadStats.mDownloadedSize = 0;
                            this.mDownloadStats.mFileSize = i;
                            this.mDownloadStats.mElapsedTime = 0;
                            this.mDownloadStats.mEstimatedRemainingTime = -1;
                            this.mDownloadStats.mBitRate = 0;
                            Timer timer = new Timer();
                            timer.scheduleAtFixedRate(new TimerTask() {
                                public void run() {
                                    if (!MXMediaDownloadWorkerTask.this.mIsDone) {
                                        MXMediaDownloadWorkerTask.this.updateAndPublishProgress(currentTimeMillis);
                                    }
                                }
                            }, new Date(), 100);
                            try {
                                byte[] bArr = new byte[32768];
                                while (!isDownloadCancelled()) {
                                }
                                if (!isDownloadCancelled()) {
                                }
                            } catch (OutOfMemoryError e4) {
                                Log.e(LOG_TAG, "doInBackground: out of memory", e4);
                                matrixError.error = e4.getLocalizedMessage();
                            } catch (Exception e5) {
                                String str5 = LOG_TAG;
                                StringBuilder sb8 = new StringBuilder();
                                sb8.append("doInBackground fail to read image ");
                                sb8.append(e5.getMessage());
                                Log.e(str5, sb8.toString(), e5);
                                matrixError.error = e5.getLocalizedMessage();
                            }
                            this.mIsDone = true;
                            close(inputStream);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            timer.cancel();
                            httpURLConnection.disconnect();
                            if (this.mDownloadStats.mProgress == 100) {
                            }
                            if (this.mDownloadStats.mProgress == 100) {
                            }
                        } catch (Exception e6) {
                            e = e6;
                        }
                        if (!TextUtils.isEmpty(matrixError.error)) {
                        }
                        synchronized (sPendingDownloadById) {
                        }
                        return jsonElement;
                    }
                } catch (Exception e7) {
                    e = e7;
                } catch (Throwable th) {
                    outputStream.close();
                    throw th;
                }
            } catch (Exception e8) {
                e = e8;
                httpURLConnection = null;
                i = -1;
                String str42 = LOG_TAG;
                StringBuilder sb42 = new StringBuilder();
                sb42.append("bitmapForURL : fail to open the connection ");
                sb42.append(e.getMessage());
                Log.e(str42, sb42.toString(), e);
                matrixError.error = e.getLocalizedMessage();
                if (httpURLConnection.getResponseCode() == 403 && this.mMediaScanRestClient != null) {
                    this.mMediaScanRestClient.resetServerPublicKey();
                }
                errorStream = httpURLConnection.getErrorStream();
                if (errorStream != null) {
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(errorStream, "UTF-8"));
                        StringBuilder sb9 = new StringBuilder();
                        while (true) {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            }
                            sb9.append(readLine);
                        }
                        jsonElement = new JsonParser().parse(sb9.toString());
                    } catch (Exception e9) {
                        String str6 = LOG_TAG;
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append("bitmapForURL : Error parsing error ");
                        sb10.append(e9.getMessage());
                        Log.e(str6, sb10.toString(), e9);
                        jsonElement = null;
                        Log.d(LOG_TAG, "MediaWorkerTask an url does not exist");
                        synchronized (sUnreachableUrls) {
                        }
                        dispatchDownloadStart();
                        JsonParser jsonParser2 = new JsonParser();
                        StringBuilder sb52 = new StringBuilder();
                        sb52.append("Cannot open ");
                        sb52.append(this.mUrl);
                        jsonElement = jsonParser2.parse(sb52.toString());
                        synchronized (sUnreachableUrls) {
                        }
                        final long currentTimeMillis2 = System.currentTimeMillis();
                        StringBuilder sb62 = new StringBuilder();
                        sb62.append(buildFileName(this.mDownloadId, this.mMimeType));
                        sb62.append(".tmp");
                        String sb72 = sb62.toString();
                        FileOutputStream fileOutputStream2 = new FileOutputStream(new File(this.mDirectoryFile, sb72));
                        this.mDownloadStats.mDownloadId = this.mDownloadId;
                        this.mDownloadStats.mProgress = 0;
                        this.mDownloadStats.mDownloadedSize = 0;
                        this.mDownloadStats.mFileSize = i;
                        this.mDownloadStats.mElapsedTime = 0;
                        this.mDownloadStats.mEstimatedRemainingTime = -1;
                        this.mDownloadStats.mBitRate = 0;
                        Timer timer2 = new Timer();
                        timer2.scheduleAtFixedRate(new TimerTask() {
                            public void run() {
                                if (!MXMediaDownloadWorkerTask.this.mIsDone) {
                                    MXMediaDownloadWorkerTask.this.updateAndPublishProgress(currentTimeMillis2);
                                }
                            }
                        }, new Date(), 100);
                        byte[] bArr2 = new byte[32768];
                        while (!isDownloadCancelled()) {
                        }
                        if (!isDownloadCancelled()) {
                        }
                        this.mIsDone = true;
                        close(inputStream);
                        fileOutputStream2.flush();
                        fileOutputStream2.close();
                        timer2.cancel();
                        httpURLConnection.disconnect();
                        if (this.mDownloadStats.mProgress == 100) {
                        }
                        if (this.mDownloadStats.mProgress == 100) {
                        }
                        if (!TextUtils.isEmpty(matrixError.error)) {
                        }
                        synchronized (sPendingDownloadById) {
                        }
                        return jsonElement;
                    }
                    Log.d(LOG_TAG, "MediaWorkerTask an url does not exist");
                    if (!this.mIsAvScannerEnabled || this.mEncryptedFileInfo == null) {
                        synchronized (sUnreachableUrls) {
                            sUnreachableUrls.add(this.mUrl);
                        }
                    }
                    dispatchDownloadStart();
                    JsonParser jsonParser22 = new JsonParser();
                    StringBuilder sb522 = new StringBuilder();
                    sb522.append("Cannot open ");
                    sb522.append(this.mUrl);
                    jsonElement = jsonParser22.parse(sb522.toString());
                    synchronized (sUnreachableUrls) {
                    }
                    final long currentTimeMillis22 = System.currentTimeMillis();
                    StringBuilder sb622 = new StringBuilder();
                    sb622.append(buildFileName(this.mDownloadId, this.mMimeType));
                    sb622.append(".tmp");
                    String sb722 = sb622.toString();
                    FileOutputStream fileOutputStream22 = new FileOutputStream(new File(this.mDirectoryFile, sb722));
                    this.mDownloadStats.mDownloadId = this.mDownloadId;
                    this.mDownloadStats.mProgress = 0;
                    this.mDownloadStats.mDownloadedSize = 0;
                    this.mDownloadStats.mFileSize = i;
                    this.mDownloadStats.mElapsedTime = 0;
                    this.mDownloadStats.mEstimatedRemainingTime = -1;
                    this.mDownloadStats.mBitRate = 0;
                    Timer timer22 = new Timer();
                    timer22.scheduleAtFixedRate(new TimerTask() {
                        public void run() {
                            if (!MXMediaDownloadWorkerTask.this.mIsDone) {
                                MXMediaDownloadWorkerTask.this.updateAndPublishProgress(currentTimeMillis22);
                            }
                        }
                    }, new Date(), 100);
                    byte[] bArr22 = new byte[32768];
                    while (!isDownloadCancelled()) {
                    }
                    if (!isDownloadCancelled()) {
                    }
                    this.mIsDone = true;
                    close(inputStream);
                    fileOutputStream22.flush();
                    fileOutputStream22.close();
                    timer22.cancel();
                    httpURLConnection.disconnect();
                    if (this.mDownloadStats.mProgress == 100) {
                    }
                    if (this.mDownloadStats.mProgress == 100) {
                    }
                    if (!TextUtils.isEmpty(matrixError.error)) {
                    }
                    synchronized (sPendingDownloadById) {
                    }
                    return jsonElement;
                }
                jsonElement = null;
                Log.d(LOG_TAG, "MediaWorkerTask an url does not exist");
                synchronized (sUnreachableUrls) {
                }
                dispatchDownloadStart();
                JsonParser jsonParser222 = new JsonParser();
                StringBuilder sb5222 = new StringBuilder();
                sb5222.append("Cannot open ");
                sb5222.append(this.mUrl);
                jsonElement = jsonParser222.parse(sb5222.toString());
                synchronized (sUnreachableUrls) {
                }
                final long currentTimeMillis222 = System.currentTimeMillis();
                StringBuilder sb6222 = new StringBuilder();
                sb6222.append(buildFileName(this.mDownloadId, this.mMimeType));
                sb6222.append(".tmp");
                String sb7222 = sb6222.toString();
                FileOutputStream fileOutputStream222 = new FileOutputStream(new File(this.mDirectoryFile, sb7222));
                this.mDownloadStats.mDownloadId = this.mDownloadId;
                this.mDownloadStats.mProgress = 0;
                this.mDownloadStats.mDownloadedSize = 0;
                this.mDownloadStats.mFileSize = i;
                this.mDownloadStats.mElapsedTime = 0;
                this.mDownloadStats.mEstimatedRemainingTime = -1;
                this.mDownloadStats.mBitRate = 0;
                Timer timer222 = new Timer();
                timer222.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        if (!MXMediaDownloadWorkerTask.this.mIsDone) {
                            MXMediaDownloadWorkerTask.this.updateAndPublishProgress(currentTimeMillis222);
                        }
                    }
                }, new Date(), 100);
                byte[] bArr222 = new byte[32768];
                while (!isDownloadCancelled()) {
                }
                if (!isDownloadCancelled()) {
                }
                this.mIsDone = true;
                close(inputStream);
                fileOutputStream222.flush();
                fileOutputStream222.close();
                timer222.cancel();
                httpURLConnection.disconnect();
                if (this.mDownloadStats.mProgress == 100) {
                }
                if (this.mDownloadStats.mProgress == 100) {
                }
                if (!TextUtils.isEmpty(matrixError.error)) {
                }
                synchronized (sPendingDownloadById) {
                }
                return jsonElement;
            }
            dispatchDownloadStart();
            if (inputStream == null && jsonElement == null) {
                JsonParser jsonParser2222 = new JsonParser();
                StringBuilder sb52222 = new StringBuilder();
                sb52222.append("Cannot open ");
                sb52222.append(this.mUrl);
                jsonElement = jsonParser2222.parse(sb52222.toString());
                if (!this.mIsAvScannerEnabled || this.mEncryptedFileInfo == null) {
                    synchronized (sUnreachableUrls) {
                        sUnreachableUrls.add(this.mUrl);
                    }
                }
            }
            if (!isDownloadCancelled() && jsonElement == null) {
                final long currentTimeMillis2222 = System.currentTimeMillis();
                StringBuilder sb62222 = new StringBuilder();
                sb62222.append(buildFileName(this.mDownloadId, this.mMimeType));
                sb62222.append(".tmp");
                String sb72222 = sb62222.toString();
                FileOutputStream fileOutputStream2222 = new FileOutputStream(new File(this.mDirectoryFile, sb72222));
                this.mDownloadStats.mDownloadId = this.mDownloadId;
                this.mDownloadStats.mProgress = 0;
                this.mDownloadStats.mDownloadedSize = 0;
                this.mDownloadStats.mFileSize = i;
                this.mDownloadStats.mElapsedTime = 0;
                this.mDownloadStats.mEstimatedRemainingTime = -1;
                this.mDownloadStats.mBitRate = 0;
                Timer timer2222 = new Timer();
                timer2222.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        if (!MXMediaDownloadWorkerTask.this.mIsDone) {
                            MXMediaDownloadWorkerTask.this.updateAndPublishProgress(currentTimeMillis2222);
                        }
                    }
                }, new Date(), 100);
                byte[] bArr2222 = new byte[32768];
                while (!isDownloadCancelled()) {
                    int read = inputStream.read(bArr2222);
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream2222.write(bArr2222, 0, read);
                    this.mDownloadStats.mDownloadedSize += read;
                }
                if (!isDownloadCancelled()) {
                    this.mDownloadStats.mProgress = 100;
                }
                this.mIsDone = true;
                close(inputStream);
                fileOutputStream2222.flush();
                fileOutputStream2222.close();
                timer2222.cancel();
                if (httpURLConnection != null && (httpURLConnection instanceof HttpsURLConnection)) {
                    httpURLConnection.disconnect();
                }
                if (this.mDownloadStats.mProgress == 100) {
                    try {
                        File file = new File(this.mDirectoryFile, sb72222);
                        String buildFileName = buildFileName(this.mDownloadId, this.mMimeType);
                        File file2 = new File(this.mDirectoryFile, buildFileName);
                        if (file2.exists()) {
                            this.mApplicationContext.deleteFile(buildFileName);
                        }
                        file.renameTo(file2);
                    } catch (Exception e10) {
                        String str7 = LOG_TAG;
                        StringBuilder sb11 = new StringBuilder();
                        sb11.append("doInBackground : renaming error ");
                        sb11.append(e10.getMessage());
                        Log.e(str7, sb11.toString(), e10);
                        matrixError.error = e10.getLocalizedMessage();
                    }
                }
            }
            if (this.mDownloadStats.mProgress == 100) {
                String str8 = LOG_TAG;
                StringBuilder sb12 = new StringBuilder();
                sb12.append("The download ");
                sb12.append(this);
                sb12.append(" is done.");
                Log.d(str8, sb12.toString());
            } else if (jsonElement != null) {
                String str9 = LOG_TAG;
                StringBuilder sb13 = new StringBuilder();
                sb13.append("The download ");
                sb13.append(this);
                sb13.append(" failed : mErrorAsJsonElement ");
                sb13.append(jsonElement.toString());
                Log.d(str9, sb13.toString());
            } else {
                String str10 = LOG_TAG;
                StringBuilder sb14 = new StringBuilder();
                sb14.append("The download ");
                sb14.append(this);
                sb14.append(" failed.");
                Log.d(str10, sb14.toString());
            }
        } catch (Exception e11) {
            e = e11;
            jsonElement = null;
            String str11 = LOG_TAG;
            StringBuilder sb15 = new StringBuilder();
            sb15.append("Unable to download media ");
            sb15.append(this);
            Log.e(str11, sb15.toString(), e);
            matrixError.error = e.getMessage();
            if (!TextUtils.isEmpty(matrixError.error)) {
            }
            synchronized (sPendingDownloadById) {
            }
            return jsonElement;
        }
        if (!TextUtils.isEmpty(matrixError.error)) {
            jsonElement = JsonUtils.getGson(false).toJsonTree(matrixError);
        }
        synchronized (sPendingDownloadById) {
            sPendingDownloadById.remove(this.mDownloadId);
        }
        return jsonElement;
    }

    private String getAntivirusServerPublicKey() {
        if (this.mMediaScanRestClient == null) {
            Log.e(LOG_TAG, "Mandatory mMediaScanRestClient is null");
            return null;
        }
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final String[] strArr = new String[1];
        this.mMediaScanRestClient.getServerPublicKey(new ApiCallback<String>() {
            public void onSuccess(String str) {
                strArr[0] = str;
                countDownLatch.countDown();
            }

            public void onNetworkError(Exception exc) {
                countDownLatch.countDown();
            }

            public void onMatrixError(MatrixError matrixError) {
                countDownLatch.countDown();
            }

            public void onUnexpectedError(Exception exc) {
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException unused) {
        }
        return strArr[0];
    }

    private void close(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("close error ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(Void... voidArr) {
        super.onProgressUpdate(new Void[0]);
        dispatchOnDownloadProgress(this.mDownloadStats);
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(JsonElement jsonElement) {
        if (jsonElement != null) {
            dispatchOnDownloadError(jsonElement);
        } else if (isDownloadCancelled()) {
            dispatchDownloadCancel();
        } else {
            dispatchOnDownloadComplete();
            if (isBitmapDownloadTask() && !bitmapForURL(this.mApplicationContext, this.mDirectoryFile, this.mUrl, this.mDownloadId, this.mRotation, this.mMimeType, this.mEncryptedFileInfo, new SimpleApiCallback<Bitmap>() {
                public void onSuccess(Bitmap bitmap) {
                    MXMediaDownloadWorkerTask mXMediaDownloadWorkerTask = MXMediaDownloadWorkerTask.this;
                    if (bitmap == null) {
                        bitmap = mXMediaDownloadWorkerTask.mDefaultBitmap;
                    }
                    mXMediaDownloadWorkerTask.setBitmap(bitmap);
                }
            })) {
                setBitmap(this.mDefaultBitmap);
            }
        }
    }

    /* access modifiers changed from: private */
    public void setBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            for (WeakReference weakReference : this.mImageViewReferences) {
                ImageView imageView = (ImageView) weakReference.get();
                if (imageView != null && TextUtils.equals(this.mDownloadId, (String) imageView.getTag())) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    private void dispatchDownloadStart() {
        for (IMXMediaDownloadListener onDownloadStart : this.mDownloadListeners) {
            try {
                onDownloadStart.onDownloadStart(this.mDownloadId);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("dispatchDownloadStart error ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
    }

    private void dispatchOnDownloadProgress(DownloadStats downloadStats) {
        for (IMXMediaDownloadListener onDownloadProgress : this.mDownloadListeners) {
            try {
                onDownloadProgress.onDownloadProgress(this.mDownloadId, downloadStats);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("dispatchOnDownloadProgress error ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
    }

    private void dispatchOnDownloadError(JsonElement jsonElement) {
        for (IMXMediaDownloadListener onDownloadError : this.mDownloadListeners) {
            try {
                onDownloadError.onDownloadError(this.mDownloadId, jsonElement);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("dispatchOnDownloadError error ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
    }

    private void dispatchOnDownloadComplete() {
        for (IMXMediaDownloadListener onDownloadComplete : this.mDownloadListeners) {
            try {
                onDownloadComplete.onDownloadComplete(this.mDownloadId);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("dispatchOnDownloadComplete error ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
    }

    private void dispatchDownloadCancel() {
        for (IMXMediaDownloadListener onDownloadCancel : this.mDownloadListeners) {
            try {
                onDownloadCancel.onDownloadCancel(this.mDownloadId);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("dispatchDownloadCancel error ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
    }
}
