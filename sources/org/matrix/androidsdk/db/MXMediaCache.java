package org.matrix.androidsdk.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.core.ContentManager;
import org.matrix.androidsdk.core.FileContentUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXOsHandler;
import org.matrix.androidsdk.core.ResourceUtils;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.crypto.MXEncryptedAttachments;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;
import org.matrix.androidsdk.listeners.IMXMediaDownloadListener;
import org.matrix.androidsdk.listeners.IMXMediaDownloadListener.DownloadStats;
import org.matrix.androidsdk.listeners.IMXMediaUploadListener;
import org.matrix.androidsdk.listeners.IMXMediaUploadListener.UploadStats;
import org.matrix.androidsdk.listeners.MXMediaDownloadListener;
import org.matrix.androidsdk.network.NetworkConnectivityReceiver;
import org.matrix.androidsdk.rest.client.MediaScanRestClient;

public class MXMediaCache {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = MXMediaCache.class.getSimpleName();
    private static final String MXMEDIA_STORE_EXT_SHARE_FOLDER = "ext_share";
    private static final String MXMEDIA_STORE_FOLDER = "MXMediaStore3";
    private static final String MXMEDIA_STORE_IMAGES_FOLDER = "Images";
    private static final String MXMEDIA_STORE_MEMBER_THUMBNAILS_FOLDER = "MXMemberThumbnailsStore";
    private static final String MXMEDIA_STORE_OTHERS_FOLDER = "Others";
    private static final String MXMEDIA_STORE_SHARE_FOLDER = "share";
    private static final String MXMEDIA_STORE_TMP_FOLDER = "tmp";
    static MXOsHandler mDecryptingHandler = null;
    static HandlerThread mDecryptingHandlerThread = null;
    private static Bitmap mDefaultBitmap = null;
    static Handler mUIHandler = null;
    private static final List<String> sPreviousMediaCacheFolders = Arrays.asList(new String[]{"MXMediaStore", "MXMediaStore2"});
    private ContentManager mContentManager;
    private File mImagesFolderFile;
    private File mMediaFolderFile;
    private MediaScanRestClient mMediaScanRestClient;
    private final NetworkConnectivityReceiver mNetworkConnectivityReceiver;
    private File mOthersFolderFile;
    private File mShareFolderFile;
    private final List<MXMediaDownloadWorkerTask> mSuspendedTasks = new ArrayList();
    private File mThumbnailsFolderFile;
    /* access modifiers changed from: private */
    public File mTmpFolderFile;

    public MXMediaCache(ContentManager contentManager, NetworkConnectivityReceiver networkConnectivityReceiver, String str, Context context) {
        this.mContentManager = contentManager;
        this.mNetworkConnectivityReceiver = networkConnectivityReceiver;
        for (String file : sPreviousMediaCacheFolders) {
            File file2 = new File(context.getApplicationContext().getFilesDir(), file);
            if (file2.exists()) {
                FileContentUtils.deleteDirectory(file2);
            }
        }
        File file3 = new File(context.getApplicationContext().getFilesDir(), MXMEDIA_STORE_FOLDER);
        if (!file3.exists()) {
            file3.mkdirs();
        }
        this.mMediaFolderFile = new File(file3, str);
        this.mImagesFolderFile = new File(this.mMediaFolderFile, MXMEDIA_STORE_IMAGES_FOLDER);
        this.mOthersFolderFile = new File(this.mMediaFolderFile, MXMEDIA_STORE_OTHERS_FOLDER);
        this.mTmpFolderFile = new File(this.mMediaFolderFile, MXMEDIA_STORE_TMP_FOLDER);
        if (this.mTmpFolderFile.exists()) {
            FileContentUtils.deleteDirectory(this.mTmpFolderFile);
        }
        this.mTmpFolderFile.mkdirs();
        this.mShareFolderFile = new File(context.getApplicationContext().getFilesDir(), MXMEDIA_STORE_EXT_SHARE_FOLDER);
        if (this.mShareFolderFile.exists()) {
            FileContentUtils.deleteDirectory(this.mShareFolderFile);
        }
        this.mShareFolderFile.mkdirs();
        this.mThumbnailsFolderFile = new File(file3, MXMEDIA_STORE_MEMBER_THUMBNAILS_FOLDER);
        if (mDecryptingHandlerThread == null) {
            mDecryptingHandlerThread = new HandlerThread("MXMediaDecryptingBackgroundThread", 1);
            mDecryptingHandlerThread.start();
            mDecryptingHandler = new MXOsHandler(mDecryptingHandlerThread.getLooper());
            mUIHandler = new Handler(Looper.getMainLooper());
        }
    }

    private File getMediaFolderFile() {
        if (!this.mMediaFolderFile.exists()) {
            this.mMediaFolderFile.mkdirs();
        }
        return this.mMediaFolderFile;
    }

    private File getFolderFile(String str) {
        File file;
        if (str == null || str.startsWith("image/")) {
            file = this.mImagesFolderFile;
        } else {
            file = this.mOthersFolderFile;
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    private File getThumbnailsFolderFile() {
        if (!this.mThumbnailsFolderFile.exists()) {
            this.mThumbnailsFolderFile.mkdirs();
        }
        return this.mThumbnailsFolderFile;
    }

    public static void getCachesSize(final Context context, final ApiCallback<Long> apiCallback) {
        AnonymousClass1 r0 = new AsyncTask<Void, Void, Long>() {
            /* access modifiers changed from: protected */
            public Long doInBackground(Void... voidArr) {
                Context context = context;
                return Long.valueOf(FileContentUtils.getDirectorySize(context, new File(context.getApplicationContext().getFilesDir(), MXMediaCache.MXMEDIA_STORE_FOLDER), 1));
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Long l) {
                String access$000 = MXMediaCache.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getCachesSize() : ");
                sb.append(l);
                Log.d(access$000, sb.toString());
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onSuccess(l);
                }
            }
        };
        try {
            r0.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getCachesSize() : failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            r0.cancel(true);
        }
    }

    public long removeMediaBefore(long j, Set<String> set) {
        return removeMediaBefore(getMediaFolderFile(), j, set) + 0 + removeMediaBefore(getThumbnailsFolderFile(), j, set);
    }

    private long removeMediaBefore(File file, long j, Set<String> set) {
        File[] listFiles = file.listFiles();
        long j2 = 0;
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (file2.isDirectory()) {
                    j2 += removeMediaBefore(file2, j, set);
                } else if (!set.contains(file2.getPath()) && FileContentUtils.getLastAccessTime(file2) < j) {
                    j2 += file2.length();
                    file2.delete();
                }
            }
        }
        return j2;
    }

    public void clear() {
        FileContentUtils.deleteDirectory(getMediaFolderFile());
        FileContentUtils.deleteDirectory(this.mThumbnailsFolderFile);
        MXMediaDownloadWorkerTask.clearBitmapsCache();
        MXMediaUploadWorkerTask.cancelPendingUploads();
    }

    public static void clearThumbnailsCache(Context context) {
        FileContentUtils.deleteDirectory(new File(new File(context.getApplicationContext().getFilesDir(), MXMEDIA_STORE_FOLDER), MXMEDIA_STORE_MEMBER_THUMBNAILS_FOLDER));
    }

    public File thumbnailCacheFile(String str, int i) {
        String downloadTaskIdForMatrixMediaContent = this.mContentManager.downloadTaskIdForMatrixMediaContent(str);
        if (downloadTaskIdForMatrixMediaContent != null) {
            if (i > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(downloadTaskIdForMatrixMediaContent);
                sb.append("_w_");
                sb.append(i);
                sb.append("_h_");
                sb.append(i);
                downloadTaskIdForMatrixMediaContent = sb.toString();
            }
            try {
                File file = new File(getThumbnailsFolderFile(), MXMediaDownloadWorkerTask.buildFileName(downloadTaskIdForMatrixMediaContent, ResourceUtils.MIME_TYPE_JPEG));
                if (file.exists()) {
                    return file;
                }
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("thumbnailCacheFile failed ");
                sb2.append(e.getMessage());
                Log.e(str2, sb2.toString(), e);
            }
        }
        return null;
    }

    private File mediaCacheFile(String str, int i, int i2, String str2) {
        if (str == null) {
            return null;
        }
        String str3 = "file:";
        if (!str.startsWith(str3)) {
            String downloadTaskIdForMatrixMediaContent = this.mContentManager.downloadTaskIdForMatrixMediaContent(str);
            if (downloadTaskIdForMatrixMediaContent != null) {
                if (i > 0 && i2 > 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(downloadTaskIdForMatrixMediaContent);
                    sb.append("_w_");
                    sb.append(i);
                    sb.append("_h_");
                    sb.append(i2);
                    downloadTaskIdForMatrixMediaContent = sb.toString();
                }
                str = MXMediaDownloadWorkerTask.buildFileName(downloadTaskIdForMatrixMediaContent, str2);
            }
            return null;
        }
        try {
            if (str.startsWith(str3)) {
                str = Uri.parse(str).getLastPathSegment();
            }
            File file = new File(getFolderFile(str2), str);
            if (file.exists()) {
                return file;
            }
            return null;
        } catch (Exception e) {
            String str4 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mediaCacheFile failed ");
            sb2.append(e.getMessage());
            Log.e(str4, sb2.toString(), e);
        }
    }

    public boolean isMediaCached(String str, String str2) {
        return isMediaCached(str, -1, -1, str2);
    }

    public boolean isMediaCached(String str, int i, int i2, String str2) {
        return mediaCacheFile(str, i, i2, str2) != null;
    }

    public boolean createTmpDecryptedMediaFile(String str, String str2, EncryptedFileInfo encryptedFileInfo, ApiCallback<File> apiCallback) {
        return createTmpDecryptedMediaFile(str, -1, -1, str2, encryptedFileInfo, apiCallback);
    }

    public boolean createTmpDecryptedMediaFile(String str, int i, int i2, String str2, final EncryptedFileInfo encryptedFileInfo, final ApiCallback<File> apiCallback) {
        final File mediaCacheFile = mediaCacheFile(str, i, i2, str2);
        if (mediaCacheFile != null) {
            mDecryptingHandler.post(new Runnable() {
                public void run() {
                    final File file = new File(MXMediaCache.this.mTmpFolderFile, mediaCacheFile.getName());
                    if (!file.exists()) {
                        try {
                            InputStream fileInputStream = new FileInputStream(mediaCacheFile);
                            if (encryptedFileInfo != null) {
                                InputStream decryptAttachment = MXEncryptedAttachments.decryptAttachment(fileInputStream, encryptedFileInfo);
                                fileInputStream.close();
                                fileInputStream = decryptAttachment;
                            }
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            byte[] bArr = new byte[2048];
                            while (true) {
                                int read = fileInputStream.read(bArr);
                                if (read == -1) {
                                    break;
                                }
                                fileOutputStream.write(bArr, 0, read);
                            }
                            fileInputStream.close();
                            fileOutputStream.close();
                        } catch (Exception e) {
                            String access$000 = MXMediaCache.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## createTmpDecryptedMediaFile() failed ");
                            sb.append(e.getMessage());
                            Log.e(access$000, sb.toString(), e);
                        }
                    }
                    MXMediaCache.mUIHandler.post(new Runnable() {
                        public void run() {
                            apiCallback.onSuccess(file);
                        }
                    });
                }
            });
        }
        return mediaCacheFile != null;
    }

    public void clearTmpDecryptedMediaCache() {
        Log.d(LOG_TAG, "clearTmpDecryptedMediaCache()");
        if (this.mTmpFolderFile.exists()) {
            FileContentUtils.deleteDirectory(this.mTmpFolderFile);
        }
        if (!this.mTmpFolderFile.exists()) {
            this.mTmpFolderFile.mkdirs();
        }
    }

    public File moveToShareFolder(File file, String str) {
        File file2 = new File(this.mShareFolderFile, str);
        if (file2.exists() && !file2.delete()) {
            Log.w(LOG_TAG, "Unable to delete file");
        }
        if (file.renameTo(file2)) {
            return file2;
        }
        Log.w(LOG_TAG, "Unable to rename file");
        return file;
    }

    public void clearShareDecryptedMediaCache() {
        Log.d(LOG_TAG, "clearShareDecryptedMediaCache()");
        if (this.mShareFolderFile.exists()) {
            FileContentUtils.deleteDirectory(this.mShareFolderFile);
        }
        if (!this.mShareFolderFile.exists()) {
            this.mShareFolderFile.mkdirs();
        }
    }

    public String saveBitmap(Bitmap bitmap, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("file");
        sb.append(System.currentTimeMillis());
        sb.append(".jpg");
        String sb2 = sb.toString();
        if (str != null) {
            try {
                File file = new File(getFolderFile(null), str);
                file.delete();
                sb2 = Uri.fromFile(file).getLastPathSegment();
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("saveBitmap failed ");
                sb3.append(e.getMessage());
                Log.e(str2, sb3.toString(), e);
                return null;
            }
        }
        File file2 = new File(getFolderFile(null), sb2);
        FileOutputStream fileOutputStream = new FileOutputStream(file2.getPath());
        bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        return Uri.fromFile(file2).toString();
    }

    public String saveMedia(InputStream inputStream, String str, String str2) {
        String str3 = "saveMedia failed ";
        if (str == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("file");
            sb.append(System.currentTimeMillis());
            str = sb.toString();
            if (str2 != null) {
                String extensionFromMimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(str2);
                if (extensionFromMimeType == null) {
                    String str4 = "/";
                    if (str2.lastIndexOf(str4) >= 0) {
                        extensionFromMimeType = str2.substring(str2.lastIndexOf(str4) + 1);
                    }
                }
                if (!TextUtils.isEmpty(extensionFromMimeType)) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(".");
                    sb2.append(extensionFromMimeType);
                    str = sb2.toString();
                }
            }
        }
        try {
            File file = new File(getFolderFile(str2), str);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file.getPath());
            try {
                byte[] bArr = new byte[32768];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                }
            } catch (Exception e) {
                String str5 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append(e.getMessage());
                Log.e(str5, sb3.toString(), e);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
            return Uri.fromFile(file).toString();
        } catch (Exception e2) {
            String str6 = LOG_TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str3);
            sb4.append(e2.getMessage());
            Log.e(str6, sb4.toString(), e2);
            return null;
        }
    }

    public void saveFileMediaForUrl(String str, String str2, String str3) {
        saveFileMediaForUrl(str, str2, -1, -1, str3);
    }

    public void saveFileMediaForUrl(String str, String str2, int i, int i2, String str3) {
        saveFileMediaForUrl(str, str2, i, i2, str3, false);
    }

    public void saveFileMediaForUrl(String str, String str2, int i, int i2, String str3, boolean z) {
        String downloadTaskIdForMatrixMediaContent = this.mContentManager.downloadTaskIdForMatrixMediaContent(str);
        if (downloadTaskIdForMatrixMediaContent != null) {
            if (i > 0 && i2 > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(downloadTaskIdForMatrixMediaContent);
                sb.append("_w_");
                sb.append(i);
                sb.append("_h_");
                sb.append(i2);
                downloadTaskIdForMatrixMediaContent = sb.toString();
            }
            try {
                File file = new File(getFolderFile(str3), MXMediaDownloadWorkerTask.buildFileName(downloadTaskIdForMatrixMediaContent, str3));
                if (file.exists()) {
                    try {
                        file.delete();
                    } catch (Exception e) {
                        String str4 = LOG_TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("saveFileMediaForUrl delete failed ");
                        sb2.append(e.getMessage());
                        Log.e(str4, sb2.toString(), e);
                    }
                }
                File file2 = new File(Uri.parse(str2).getPath());
                if (z) {
                    FileInputStream fileInputStream = new FileInputStream(file2);
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read > 0) {
                            fileOutputStream.write(bArr, 0, read);
                        } else {
                            fileInputStream.close();
                            fileOutputStream.close();
                            return;
                        }
                    }
                } else {
                    file2.renameTo(file);
                }
            } catch (Exception e2) {
                String str5 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("saveFileMediaForUrl failed ");
                sb3.append(e2.getMessage());
                Log.e(str5, sb3.toString(), e2);
            }
        }
    }

    public String loadAvatarThumbnail(HomeServerConnectionConfig homeServerConnectionConfig, ImageView imageView, String str, int i) {
        return loadBitmap(imageView.getContext(), homeServerConnectionConfig, imageView, str, i, i, 0, 0, null, getThumbnailsFolderFile(), null);
    }

    public String loadAvatarThumbnail(HomeServerConnectionConfig homeServerConnectionConfig, ImageView imageView, String str, int i, Bitmap bitmap) {
        return loadBitmap(imageView.getContext(), homeServerConnectionConfig, imageView, str, i, i, 0, 0, null, getThumbnailsFolderFile(), bitmap, null);
    }

    public boolean isAvatarThumbnailCached(String str, int i) {
        String downloadTaskIdForMatrixMediaContent = this.mContentManager.downloadTaskIdForMatrixMediaContent(str);
        if (downloadTaskIdForMatrixMediaContent == null) {
            return false;
        }
        if (i > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(downloadTaskIdForMatrixMediaContent);
            sb.append("_w_");
            sb.append(i);
            sb.append("_h_");
            sb.append(i);
            downloadTaskIdForMatrixMediaContent = sb.toString();
        }
        boolean isMediaCached = MXMediaDownloadWorkerTask.isMediaCached(downloadTaskIdForMatrixMediaContent);
        if (isMediaCached) {
            return isMediaCached;
        }
        try {
            return new File(getThumbnailsFolderFile(), MXMediaDownloadWorkerTask.buildFileName(downloadTaskIdForMatrixMediaContent, ResourceUtils.MIME_TYPE_JPEG)).exists();
        } catch (Throwable th) {
            String str2 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## isAvatarThumbnailCached() : failed ");
            sb2.append(th.getMessage());
            Log.e(str2, sb2.toString(), th);
            return isMediaCached;
        }
    }

    public static boolean isMediaUrlUnreachable(String str) {
        return MXMediaDownloadWorkerTask.isMediaUrlUnreachable(str);
    }

    public String loadBitmap(HomeServerConnectionConfig homeServerConnectionConfig, ImageView imageView, String str, int i, int i2, String str2, EncryptedFileInfo encryptedFileInfo) {
        return loadBitmap(homeServerConnectionConfig, imageView, str, -1, -1, i, i2, str2, encryptedFileInfo);
    }

    public String loadBitmap(Context context, HomeServerConnectionConfig homeServerConnectionConfig, String str, int i, int i2, String str2, EncryptedFileInfo encryptedFileInfo) {
        String str3 = str2;
        return loadBitmap(context, homeServerConnectionConfig, null, str, -1, -1, i, i2, str3, getFolderFile(str3), encryptedFileInfo);
    }

    public String loadBitmap(HomeServerConnectionConfig homeServerConnectionConfig, ImageView imageView, String str, int i, int i2, int i3, int i4, String str2, EncryptedFileInfo encryptedFileInfo) {
        String str3 = str2;
        return loadBitmap(imageView.getContext(), homeServerConnectionConfig, imageView, str, i, i2, i3, i4, str3, getFolderFile(str3), encryptedFileInfo);
    }

    public String downloadIdFromUrl(String str) {
        String downloadTaskIdForMatrixMediaContent = this.mContentManager.downloadTaskIdForMatrixMediaContent(str);
        if (downloadTaskIdForMatrixMediaContent == null || MXMediaDownloadWorkerTask.getMediaDownloadWorkerTask(downloadTaskIdForMatrixMediaContent) == null) {
            return null;
        }
        return downloadTaskIdForMatrixMediaContent;
    }

    public String downloadMedia(Context context, HomeServerConnectionConfig homeServerConnectionConfig, String str, String str2, EncryptedFileInfo encryptedFileInfo) {
        return downloadMedia(context, homeServerConnectionConfig, str, str2, encryptedFileInfo, null);
    }

    public String downloadMedia(Context context, HomeServerConnectionConfig homeServerConnectionConfig, String str, String str2, EncryptedFileInfo encryptedFileInfo, IMXMediaDownloadListener iMXMediaDownloadListener) {
        String str3 = str;
        String str4 = str2;
        IMXMediaDownloadListener iMXMediaDownloadListener2 = iMXMediaDownloadListener;
        if (!(str4 == null || context == null)) {
            String downloadTaskIdForMatrixMediaContent = this.mContentManager.downloadTaskIdForMatrixMediaContent(str3);
            if (downloadTaskIdForMatrixMediaContent != null && !isMediaCached(str3, str4)) {
                MXMediaDownloadWorkerTask mediaDownloadWorkerTask = MXMediaDownloadWorkerTask.getMediaDownloadWorkerTask(downloadTaskIdForMatrixMediaContent);
                if (mediaDownloadWorkerTask != null) {
                    mediaDownloadWorkerTask.addDownloadListener(iMXMediaDownloadListener2);
                    return downloadTaskIdForMatrixMediaContent;
                }
                MXMediaDownloadWorkerTask mXMediaDownloadWorkerTask = r2;
                MXMediaDownloadWorkerTask mXMediaDownloadWorkerTask2 = new MXMediaDownloadWorkerTask(context, homeServerConnectionConfig, this.mNetworkConnectivityReceiver, getFolderFile(str4), this.mContentManager.getDownloadableUrl(str3, encryptedFileInfo != null), downloadTaskIdForMatrixMediaContent, 0, str2, encryptedFileInfo, this.mMediaScanRestClient, this.mContentManager.isAvScannerEnabled());
                MXMediaDownloadWorkerTask mXMediaDownloadWorkerTask3 = mXMediaDownloadWorkerTask;
                mXMediaDownloadWorkerTask3.addDownloadListener(iMXMediaDownloadListener2);
                try {
                    mXMediaDownloadWorkerTask3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                } catch (RejectedExecutionException e) {
                    synchronized (this.mSuspendedTasks) {
                        mXMediaDownloadWorkerTask3.cancel(true);
                        this.mSuspendedTasks.add(new MXMediaDownloadWorkerTask(mXMediaDownloadWorkerTask3));
                        Log.e(LOG_TAG, "Suspend the task ", e);
                    }
                } catch (Exception e2) {
                    String str5 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("downloadMedia failed ");
                    sb.append(e2.getMessage());
                    Log.e(str5, sb.toString(), e2);
                    synchronized (this.mSuspendedTasks) {
                        mXMediaDownloadWorkerTask3.cancel(true);
                    }
                }
                return downloadTaskIdForMatrixMediaContent;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void launchSuspendedTask() {
        synchronized (this.mSuspendedTasks) {
            if (!this.mSuspendedTasks.isEmpty()) {
                MXMediaDownloadWorkerTask mXMediaDownloadWorkerTask = (MXMediaDownloadWorkerTask) this.mSuspendedTasks.get(0);
                Log.d(LOG_TAG, "Restart a task ");
                try {
                    mXMediaDownloadWorkerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                    this.mSuspendedTasks.remove(mXMediaDownloadWorkerTask);
                } catch (RejectedExecutionException unused) {
                    mXMediaDownloadWorkerTask.cancel(true);
                    this.mSuspendedTasks.remove(mXMediaDownloadWorkerTask);
                    MXMediaDownloadWorkerTask mXMediaDownloadWorkerTask2 = new MXMediaDownloadWorkerTask(mXMediaDownloadWorkerTask);
                    this.mSuspendedTasks.add(mXMediaDownloadWorkerTask2);
                    String str = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Suspend again the task ");
                    sb.append(mXMediaDownloadWorkerTask2.getStatus());
                    Log.d(str, sb.toString());
                } catch (Exception e) {
                    String str2 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Try to Restart a task fails ");
                    sb2.append(e.getMessage());
                    Log.e(str2, sb2.toString(), e);
                }
            }
        }
    }

    public String loadBitmap(Context context, HomeServerConnectionConfig homeServerConnectionConfig, ImageView imageView, String str, int i, int i2, int i3, int i4, String str2, File file, EncryptedFileInfo encryptedFileInfo) {
        return loadBitmap(context, homeServerConnectionConfig, imageView, str, i, i2, i3, i4, str2, file, null, encryptedFileInfo);
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x00c5  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00ca  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00cf  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00ed  */
    public String loadBitmap(Context context, HomeServerConnectionConfig homeServerConnectionConfig, ImageView imageView, String str, int i, int i2, int i3, int i4, String str2, File file, Bitmap bitmap, EncryptedFileInfo encryptedFileInfo) {
        String str3;
        String str4;
        final String str5;
        String str6;
        String str7;
        String str8;
        final ImageView imageView2 = imageView;
        String str9 = str;
        int i5 = i;
        int i6 = i2;
        int i7 = i4;
        String str10 = null;
        if (i5 == 0 || i6 == 0) {
            return null;
        }
        if (mDefaultBitmap == null) {
            mDefaultBitmap = BitmapFactory.decodeResource(context.getResources(), 17301567);
        }
        final Bitmap bitmap2 = bitmap == null ? mDefaultBitmap : bitmap;
        String downloadTaskIdForMatrixMediaContent = this.mContentManager.downloadTaskIdForMatrixMediaContent(str9);
        if (downloadTaskIdForMatrixMediaContent == null) {
            if (imageView2 != null) {
                imageView2.setImageBitmap(bitmap2);
            }
            return null;
        }
        if (encryptedFileInfo != null || i5 <= 0 || i6 <= 0) {
            str3 = this.mContentManager.getDownloadableUrl(str9, encryptedFileInfo != null);
        } else {
            str3 = this.mContentManager.getDownloadableThumbnailUrl(str9, i5, i6, ContentManager.METHOD_SCALE);
            StringBuilder sb = new StringBuilder();
            sb.append(downloadTaskIdForMatrixMediaContent);
            sb.append("_w_");
            sb.append(i5);
            sb.append("_h_");
            sb.append(i6);
            downloadTaskIdForMatrixMediaContent = sb.toString();
        }
        if (encryptedFileInfo != null) {
            int i8 = i3;
        } else if (!(i3 != Integer.MAX_VALUE || i7 == 0 || i7 == 1)) {
            if (str3.contains("?")) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str3);
                sb2.append("&apply_orientation=true");
                str8 = sb2.toString();
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append("?apply_orientation=true");
                str8 = sb3.toString();
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append(downloadTaskIdForMatrixMediaContent);
            sb4.append("_apply_orientation");
            str4 = str8;
            str5 = sb4.toString();
            if (imageView2 != null) {
                imageView2.setTag(str5);
            }
            str6 = str2 != null ? ResourceUtils.MIME_TYPE_JPEG : str2;
            if (!MXMediaDownloadWorkerTask.bitmapForURL(context.getApplicationContext(), file, str4, str5, i3, str6, encryptedFileInfo, new SimpleApiCallback<Bitmap>() {
                public void onSuccess(Bitmap bitmap) {
                    ImageView imageView = imageView2;
                    if (imageView != null && TextUtils.equals(str5, (String) imageView.getTag())) {
                        ImageView imageView2 = imageView2;
                        if (bitmap == null) {
                            bitmap = bitmap2;
                        }
                        imageView2.setImageBitmap(bitmap);
                    }
                }
            })) {
                MXMediaDownloadWorkerTask mediaDownloadWorkerTask = MXMediaDownloadWorkerTask.getMediaDownloadWorkerTask(str5);
                if (mediaDownloadWorkerTask != null) {
                    if (imageView2 != null) {
                        mediaDownloadWorkerTask.addImageView(imageView2);
                    }
                    str7 = str5;
                } else {
                    MXMediaDownloadWorkerTask mXMediaDownloadWorkerTask = r2;
                    str7 = str5;
                    MXMediaDownloadWorkerTask mXMediaDownloadWorkerTask2 = new MXMediaDownloadWorkerTask(context, homeServerConnectionConfig, this.mNetworkConnectivityReceiver, file, str4, str5, i3, str6, encryptedFileInfo, this.mMediaScanRestClient, this.mContentManager.isAvScannerEnabled());
                    if (imageView2 != null) {
                        mXMediaDownloadWorkerTask.addImageView(imageView2);
                    }
                    mXMediaDownloadWorkerTask.setDefaultBitmap(bitmap2);
                    mXMediaDownloadWorkerTask.addDownloadListener(new MXMediaDownloadListener() {
                        public void onDownloadComplete(String str) {
                            MXMediaCache.this.launchSuspendedTask();
                        }
                    });
                    try {
                        mXMediaDownloadWorkerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                    } catch (RejectedExecutionException e) {
                        synchronized (this.mSuspendedTasks) {
                            mXMediaDownloadWorkerTask.cancel(true);
                            this.mSuspendedTasks.add(new MXMediaDownloadWorkerTask(mXMediaDownloadWorkerTask));
                            Log.e(LOG_TAG, "Suspend a task", e);
                        }
                    } catch (Exception e2) {
                        String str11 = LOG_TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("loadBitmap failed ");
                        sb5.append(e2.getMessage());
                        Log.e(str11, sb5.toString(), e2);
                    }
                }
                str10 = str7;
            }
            return str10;
        }
        str4 = str3;
        str5 = downloadTaskIdForMatrixMediaContent;
        if (imageView2 != null) {
        }
        if (str2 != null) {
        }
        if (!MXMediaDownloadWorkerTask.bitmapForURL(context.getApplicationContext(), file, str4, str5, i3, str6, encryptedFileInfo, new SimpleApiCallback<Bitmap>() {
            public void onSuccess(Bitmap bitmap) {
                ImageView imageView = imageView2;
                if (imageView != null && TextUtils.equals(str5, (String) imageView.getTag())) {
                    ImageView imageView2 = imageView2;
                    if (bitmap == null) {
                        bitmap = bitmap2;
                    }
                    imageView2.setImageBitmap(bitmap);
                }
            }
        })) {
        }
        return str10;
    }

    public int getProgressValueForDownloadId(String str) {
        MXMediaDownloadWorkerTask mediaDownloadWorkerTask = MXMediaDownloadWorkerTask.getMediaDownloadWorkerTask(str);
        if (mediaDownloadWorkerTask != null) {
            return mediaDownloadWorkerTask.getProgress();
        }
        return -1;
    }

    public DownloadStats getStatsForDownloadId(String str) {
        MXMediaDownloadWorkerTask mediaDownloadWorkerTask = MXMediaDownloadWorkerTask.getMediaDownloadWorkerTask(str);
        if (mediaDownloadWorkerTask != null) {
            return mediaDownloadWorkerTask.getDownloadStats();
        }
        return null;
    }

    public void addDownloadListener(String str, IMXMediaDownloadListener iMXMediaDownloadListener) {
        MXMediaDownloadWorkerTask mediaDownloadWorkerTask = MXMediaDownloadWorkerTask.getMediaDownloadWorkerTask(str);
        if (mediaDownloadWorkerTask != null) {
            mediaDownloadWorkerTask.addDownloadListener(iMXMediaDownloadListener);
        }
    }

    public void cancelDownload(String str) {
        MXMediaDownloadWorkerTask mediaDownloadWorkerTask = MXMediaDownloadWorkerTask.getMediaDownloadWorkerTask(str);
        if (mediaDownloadWorkerTask != null) {
            mediaDownloadWorkerTask.cancelDownload();
        }
    }

    public void uploadContent(InputStream inputStream, String str, String str2, String str3, IMXMediaUploadListener iMXMediaUploadListener) {
        try {
            MXMediaUploadWorkerTask mXMediaUploadWorkerTask = new MXMediaUploadWorkerTask(this.mContentManager, inputStream, str2, str3, str, iMXMediaUploadListener);
            mXMediaUploadWorkerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } catch (Exception unused) {
            if (iMXMediaUploadListener != null) {
                iMXMediaUploadListener.onUploadError(str3, -1, null);
            }
        }
    }

    public int getProgressValueForUploadId(String str) {
        MXMediaUploadWorkerTask mediaUploadWorkerTask = MXMediaUploadWorkerTask.getMediaUploadWorkerTask(str);
        if (mediaUploadWorkerTask != null) {
            return mediaUploadWorkerTask.getProgress();
        }
        return -1;
    }

    public UploadStats getStatsForUploadId(String str) {
        MXMediaUploadWorkerTask mediaUploadWorkerTask = MXMediaUploadWorkerTask.getMediaUploadWorkerTask(str);
        if (mediaUploadWorkerTask != null) {
            return mediaUploadWorkerTask.getStats();
        }
        return null;
    }

    public void addUploadListener(String str, IMXMediaUploadListener iMXMediaUploadListener) {
        MXMediaUploadWorkerTask mediaUploadWorkerTask = MXMediaUploadWorkerTask.getMediaUploadWorkerTask(str);
        if (mediaUploadWorkerTask != null) {
            mediaUploadWorkerTask.addListener(iMXMediaUploadListener);
        }
    }

    public void cancelUpload(String str) {
        MXMediaUploadWorkerTask mediaUploadWorkerTask = MXMediaUploadWorkerTask.getMediaUploadWorkerTask(str);
        if (mediaUploadWorkerTask != null) {
            mediaUploadWorkerTask.cancelUpload();
        }
    }

    public void setMediaScanRestClient(MediaScanRestClient mediaScanRestClient) {
        this.mMediaScanRestClient = mediaScanRestClient;
    }
}
