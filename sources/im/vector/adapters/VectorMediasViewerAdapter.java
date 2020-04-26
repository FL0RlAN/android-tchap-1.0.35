package im.vector.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.JsonElement;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.media.MediaScanManager;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.VectorMediasViewerActivity;
import im.vector.util.SlidableMediaInfo;
import im.vector.view.PieFractionView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.ResourceUtils;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.listeners.IMXMediaDownloadListener.DownloadStats;
import org.matrix.androidsdk.listeners.MXMediaDownloadListener;
import org.matrix.androidsdk.rest.model.message.Message;

public class VectorMediasViewerAdapter extends PagerAdapter {
    private static final String LOG_TAG = VectorMediasViewerAdapter.class.getSimpleName();
    /* access modifiers changed from: private */
    public int mAutoPlayItemAt = -1;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final List<Integer> mHighResMediaIndex = new ArrayList();
    private int mLatestPrimaryItemPosition = -1;
    private View mLatestPrimaryView = null;
    private final LayoutInflater mLayoutInflater;
    private final int mMaxImageHeight;
    private final int mMaxImageWidth;
    protected MediaScanManager mMediaScanManager;
    /* access modifiers changed from: private */
    public final MXMediaCache mMediasCache;
    /* access modifiers changed from: private */
    public List<SlidableMediaInfo> mMediasMessagesList;
    /* access modifiers changed from: private */
    public VideoView mPlayingVideoView = null;
    private final MXSession mSession;

    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public VectorMediasViewerAdapter(Context context, MXSession mXSession, MXMediaCache mXMediaCache, List<SlidableMediaInfo> list, int i, int i2) {
        this.mContext = context;
        this.mSession = mXSession;
        this.mMediasCache = mXMediaCache;
        this.mMediasMessagesList = list;
        this.mMaxImageWidth = i;
        this.mMaxImageHeight = i2;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return this.mMediasMessagesList.size();
    }

    public void setPrimaryItem(ViewGroup viewGroup, final int i, Object obj) {
        if (this.mLatestPrimaryItemPosition != i) {
            this.mLatestPrimaryItemPosition = i;
            final View view = (View) obj;
            this.mLatestPrimaryView = view;
            view.findViewById(R.id.media_download_failed).setVisibility(8);
            view.post(new Runnable() {
                public void run() {
                    VectorMediasViewerAdapter.this.stopPlayingVideo();
                }
            });
            view.post(new Runnable() {
                public void run() {
                    if (VectorMediasViewerAdapter.this.mHighResMediaIndex.indexOf(Integer.valueOf(i)) < 0) {
                        VectorMediasViewerAdapter.this.downloadHighResMedia(view, i);
                    } else if (i == VectorMediasViewerAdapter.this.mAutoPlayItemAt) {
                        final SlidableMediaInfo slidableMediaInfo = (SlidableMediaInfo) VectorMediasViewerAdapter.this.mMediasMessagesList.get(i);
                        if (slidableMediaInfo.mMessageType.equals(Message.MSGTYPE_VIDEO)) {
                            final VideoView videoView = (VideoView) view.findViewById(R.id.media_slider_video_view);
                            if (VectorMediasViewerAdapter.this.mMediasCache.isMediaCached(slidableMediaInfo.mMediaUrl, slidableMediaInfo.mMimeType)) {
                                VectorMediasViewerAdapter.this.mMediasCache.createTmpDecryptedMediaFile(slidableMediaInfo.mMediaUrl, slidableMediaInfo.mMimeType, slidableMediaInfo.mEncryptedFileInfo, new SimpleApiCallback<File>() {
                                    public void onSuccess(File file) {
                                        if (file != null) {
                                            VectorMediasViewerAdapter.this.playVideo(view, videoView, file, slidableMediaInfo.mMimeType);
                                        }
                                    }
                                });
                            }
                        }
                        VectorMediasViewerAdapter.this.mAutoPlayItemAt = -1;
                    }
                }
            });
        }
    }

    public void setMediaScanManager(MediaScanManager mediaScanManager) {
        this.mMediaScanManager = mediaScanManager;
    }

    public void autoPlayItemAt(int i) {
        this.mAutoPlayItemAt = i;
    }

    /* access modifiers changed from: private */
    public void downloadHighResMedia(View view, int i) {
        SlidableMediaInfo slidableMediaInfo = (SlidableMediaInfo) this.mMediasMessagesList.get(i);
        if (slidableMediaInfo != null) {
            MediaScanManager mediaScanManager = this.mMediaScanManager;
            if (mediaScanManager != null && mediaScanManager.isTrustedSlidableMediaInfo(slidableMediaInfo)) {
                if (slidableMediaInfo.mMessageType.equals(Message.MSGTYPE_IMAGE)) {
                    if (TextUtils.isEmpty(slidableMediaInfo.mMimeType)) {
                        slidableMediaInfo.mMimeType = ResourceUtils.MIME_TYPE_JPEG;
                    }
                    downloadHighResImage(view, i);
                } else {
                    downloadVideo(view, i);
                }
                return;
            }
        }
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## downloadHighResMedia : the media is unchecked or untrusted ");
        sb.append(slidableMediaInfo.mMediaUrl);
        Log.e(str, sb.toString());
    }

    /* access modifiers changed from: private */
    public void downloadVideo(View view, int i) {
        downloadVideo(view, i, false);
    }

    private void downloadVideo(View view, int i, boolean z) {
        View view2 = view;
        int i2 = i;
        final VideoView videoView = (VideoView) view2.findViewById(R.id.media_slider_video_view);
        final ImageView imageView = (ImageView) view2.findViewById(R.id.media_slider_video_thumbnail);
        final PieFractionView pieFractionView = (PieFractionView) view2.findViewById(R.id.media_slider_pie_view);
        final ImageView imageView2 = (ImageView) view2.findViewById(R.id.media_slider_video_play);
        final View findViewById = view2.findViewById(R.id.media_download_failed);
        final SlidableMediaInfo slidableMediaInfo = (SlidableMediaInfo) this.mMediasMessagesList.get(i2);
        String str = slidableMediaInfo.mMediaUrl;
        final String str2 = slidableMediaInfo.mThumbnailUrl;
        if (this.mMediasCache.isMediaCached(str, slidableMediaInfo.mMimeType)) {
            MXMediaCache mXMediaCache = this.mMediasCache;
            String str3 = slidableMediaInfo.mMimeType;
            EncryptedFileInfo encryptedFileInfo = slidableMediaInfo.mEncryptedFileInfo;
            final int i3 = i;
            final View view3 = view;
            final VideoView videoView2 = videoView;
            AnonymousClass3 r0 = new SimpleApiCallback<File>() {
                public void onSuccess(File file) {
                    if (file != null) {
                        VectorMediasViewerAdapter.this.mHighResMediaIndex.add(Integer.valueOf(i3));
                        VectorMediasViewerAdapter.this.loadVideo(i3, view3, str2, Uri.fromFile(file).toString(), slidableMediaInfo.mMimeType, slidableMediaInfo.mEncryptedThumbnailFileInfo, slidableMediaInfo.mEncryptedFileInfo);
                        if (i3 == VectorMediasViewerAdapter.this.mAutoPlayItemAt) {
                            VectorMediasViewerAdapter.this.playVideo(view3, videoView2, file, slidableMediaInfo.mMimeType);
                        }
                        VectorMediasViewerAdapter.this.mAutoPlayItemAt = -1;
                    }
                }
            };
            mXMediaCache.createTmpDecryptedMediaFile(str, str3, encryptedFileInfo, r0);
        } else if (z || this.mAutoPlayItemAt == i2) {
            String downloadMedia = this.mMediasCache.downloadMedia(this.mContext, this.mSession.getHomeServerConfig(), str, slidableMediaInfo.mMimeType, slidableMediaInfo.mEncryptedFileInfo);
            if (downloadMedia != null) {
                pieFractionView.setVisibility(0);
                imageView2.setVisibility(8);
                pieFractionView.setFraction(this.mMediasCache.getProgressValueForDownloadId(downloadMedia));
                pieFractionView.setTag(downloadMedia);
                MXMediaCache mXMediaCache2 = this.mMediasCache;
                String str4 = str2;
                final String str5 = str;
                final int i4 = i;
                final View view4 = view;
                final String str6 = str4;
                AnonymousClass4 r02 = new MXMediaDownloadListener() {
                    public void onDownloadError(String str, JsonElement jsonElement) {
                        pieFractionView.setVisibility(8);
                        findViewById.setVisibility(0);
                        MatrixError matrixError = JsonUtils.toMatrixError(jsonElement);
                        if (matrixError != null && matrixError.isSupportedErrorCode()) {
                            Toast.makeText(VectorMediasViewerAdapter.this.mContext, matrixError.getLocalizedMessage(), 1).show();
                        }
                    }

                    public void onDownloadProgress(String str, DownloadStats downloadStats) {
                        if (str.equals(pieFractionView.getTag())) {
                            pieFractionView.setFraction(downloadStats.mProgress);
                        }
                    }

                    public void onDownloadComplete(String str) {
                        if (str.equals(pieFractionView.getTag())) {
                            pieFractionView.setVisibility(8);
                            if (VectorMediasViewerAdapter.this.mMediasCache.isMediaCached(str5, slidableMediaInfo.mMimeType)) {
                                imageView2.setVisibility(0);
                                VectorMediasViewerAdapter.this.mMediasCache.createTmpDecryptedMediaFile(str5, slidableMediaInfo.mMimeType, slidableMediaInfo.mEncryptedFileInfo, new SimpleApiCallback<File>() {
                                    public void onSuccess(final File file) {
                                        if (file != null) {
                                            VectorMediasViewerAdapter.this.mHighResMediaIndex.add(Integer.valueOf(i4));
                                            final String uri = Uri.fromFile(file).toString();
                                            imageView.post(new Runnable() {
                                                public void run() {
                                                    VectorMediasViewerAdapter.this.loadVideo(i4, view4, str6, uri, slidableMediaInfo.mMimeType, slidableMediaInfo.mEncryptedThumbnailFileInfo, slidableMediaInfo.mEncryptedFileInfo);
                                                    if (i4 == VectorMediasViewerAdapter.this.mAutoPlayItemAt) {
                                                        VectorMediasViewerAdapter.this.playVideo(view4, videoView, file, slidableMediaInfo.mMimeType);
                                                        VectorMediasViewerAdapter.this.mAutoPlayItemAt = -1;
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                                return;
                            }
                            findViewById.setVisibility(0);
                        }
                    }
                };
                mXMediaCache2.addDownloadListener(downloadMedia, r02);
            }
        }
    }

    private void downloadHighResImage(View view, int i) {
        View view2 = view;
        final PhotoView photoView = (PhotoView) view2.findViewById(R.id.media_slider_image_view);
        final PieFractionView pieFractionView = (PieFractionView) view2.findViewById(R.id.media_slider_pie_view);
        final View findViewById = view2.findViewById(R.id.media_download_failed);
        final SlidableMediaInfo slidableMediaInfo = (SlidableMediaInfo) this.mMediasMessagesList.get(i);
        final String str = slidableMediaInfo.mMediaUrl;
        String loadBitmap = this.mMediasCache.loadBitmap(this.mContext, this.mSession.getHomeServerConfig(), str, slidableMediaInfo.mRotationAngle, slidableMediaInfo.mOrientation, slidableMediaInfo.mMimeType, slidableMediaInfo.mEncryptedFileInfo);
        if (loadBitmap != null) {
            pieFractionView.setVisibility(0);
            pieFractionView.setFraction(this.mMediasCache.getProgressValueForDownloadId(loadBitmap));
            MXMediaCache mXMediaCache = this.mMediasCache;
            final String str2 = loadBitmap;
            final int i2 = i;
            AnonymousClass5 r0 = new MXMediaDownloadListener() {
                public void onDownloadError(String str, JsonElement jsonElement) {
                    if (str.equals(str2)) {
                        pieFractionView.setVisibility(8);
                        findViewById.setVisibility(0);
                        MatrixError matrixError = JsonUtils.toMatrixError(jsonElement);
                        if (matrixError != null) {
                            Toast.makeText(VectorMediasViewerAdapter.this.mContext, matrixError.getLocalizedMessage(), 1).show();
                        }
                    }
                }

                public void onDownloadProgress(String str, DownloadStats downloadStats) {
                    if (str.equals(str2)) {
                        pieFractionView.setFraction(downloadStats.mProgress);
                    }
                }

                public void onDownloadComplete(String str) {
                    if (str.equals(str2)) {
                        pieFractionView.setVisibility(8);
                        if (VectorMediasViewerAdapter.this.mMediasCache.isMediaCached(str, slidableMediaInfo.mMimeType)) {
                            VectorMediasViewerAdapter.this.mMediasCache.createTmpDecryptedMediaFile(str, slidableMediaInfo.mMimeType, slidableMediaInfo.mEncryptedFileInfo, new SimpleApiCallback<File>() {
                                public void onSuccess(File file) {
                                    if (file != null) {
                                        VectorMediasViewerAdapter.this.mHighResMediaIndex.add(Integer.valueOf(i2));
                                        final String uri = Uri.fromFile(file).toString();
                                        photoView.post(new Runnable() {
                                            public void run() {
                                                Glide.with((View) photoView).load(Uri.parse(uri)).into((ImageView) photoView);
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            findViewById.setVisibility(0);
                        }
                    }
                }
            };
            mXMediaCache.addDownloadListener(loadBitmap, r0);
        }
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        String str;
        int i2;
        final ViewGroup viewGroup2 = viewGroup;
        View inflate = this.mLayoutInflater.inflate(R.layout.adapter_vector_media_viewer, null, false);
        final PieFractionView pieFractionView = (PieFractionView) inflate.findViewById(R.id.media_slider_pie_view);
        pieFractionView.setVisibility(8);
        inflate.findViewById(R.id.media_download_failed).setVisibility(8);
        final PhotoView photoView = (PhotoView) inflate.findViewById(R.id.media_slider_image_view);
        View findViewById = inflate.findViewById(R.id.media_slider_video_layout);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.media_slider_video_thumbnail);
        photoView.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                VectorMediasViewerAdapter.this.onLongClickOnMedia();
                return true;
            }
        });
        imageView.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View view) {
                VectorMediasViewerAdapter.this.onLongClickOnMedia();
                return true;
            }
        });
        SlidableMediaInfo slidableMediaInfo = (SlidableMediaInfo) this.mMediasMessagesList.get(i);
        String str2 = slidableMediaInfo.mMediaUrl;
        if (slidableMediaInfo != null) {
            MediaScanManager mediaScanManager = this.mMediaScanManager;
            if (mediaScanManager != null && mediaScanManager.isTrustedSlidableMediaInfo(slidableMediaInfo)) {
                if (slidableMediaInfo.mMessageType.equals(Message.MSGTYPE_IMAGE)) {
                    photoView.setVisibility(0);
                    findViewById.setVisibility(8);
                    if (TextUtils.isEmpty(slidableMediaInfo.mMimeType)) {
                        slidableMediaInfo.mMimeType = ResourceUtils.MIME_TYPE_JPEG;
                    }
                    String str3 = slidableMediaInfo.mMimeType;
                    int i3 = -1;
                    if (this.mMediasCache.isMediaCached(str2, str3)) {
                        if (this.mHighResMediaIndex.indexOf(Integer.valueOf(i)) < 0) {
                            this.mHighResMediaIndex.add(Integer.valueOf(i));
                        }
                        i2 = -1;
                    } else {
                        i2 = this.mMaxImageWidth;
                        i3 = this.mMaxImageHeight;
                    }
                    if (this.mMediasCache.isMediaCached(str2, i2, i3, str3)) {
                        this.mMediasCache.createTmpDecryptedMediaFile(str2, i2, i3, str3, slidableMediaInfo.mEncryptedFileInfo, new SimpleApiCallback<File>() {
                            public void onSuccess(File file) {
                                if (file != null) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("file://");
                                    sb.append(file.getPath());
                                    Glide.with((View) viewGroup2).load(sb.toString()).into((ImageView) photoView);
                                }
                            }
                        });
                    }
                    viewGroup2.addView(inflate, 0);
                    str = str2;
                } else {
                    str = str2;
                    loadVideo(i, inflate, slidableMediaInfo.mThumbnailUrl, str2, slidableMediaInfo.mMimeType, slidableMediaInfo.mEncryptedThumbnailFileInfo, slidableMediaInfo.mEncryptedFileInfo);
                    viewGroup2.addView(inflate, 0);
                }
                String str4 = str;
                String downloadMedia = this.mMediasCache.downloadMedia(this.mContext, this.mSession.getHomeServerConfig(), str4, slidableMediaInfo.mMimeType, slidableMediaInfo.mEncryptedFileInfo);
                if (downloadMedia != null) {
                    pieFractionView.setVisibility(0);
                    pieFractionView.setFraction(this.mMediasCache.getProgressValueForDownloadId(downloadMedia));
                    pieFractionView.setTag(downloadMedia);
                    this.mMediasCache.addDownloadListener(downloadMedia, new MXMediaDownloadListener() {
                        public void onDownloadError(String str, JsonElement jsonElement) {
                            pieFractionView.setVisibility(8);
                            MatrixError matrixError = JsonUtils.toMatrixError(jsonElement);
                            if (matrixError != null && matrixError.isSupportedErrorCode()) {
                                Toast.makeText(VectorMediasViewerAdapter.this.mContext, matrixError.getLocalizedMessage(), 1).show();
                            }
                        }

                        public void onDownloadProgress(String str, DownloadStats downloadStats) {
                            if (str.equals(pieFractionView.getTag())) {
                                pieFractionView.setFraction(downloadStats.mProgress);
                            }
                        }

                        public void onDownloadComplete(String str) {
                            if (str.equals(pieFractionView.getTag())) {
                                pieFractionView.setVisibility(8);
                            }
                        }
                    });
                }
                return inflate;
            }
        }
        String str5 = str2;
        String str6 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## instantiateItem : the media is unchecked or untrusted ");
        sb.append(str5);
        Log.e(str6, sb.toString());
        return inflate;
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }

    /* access modifiers changed from: private */
    public void displayVideoThumbnail(View view, boolean z) {
        VideoView videoView = (VideoView) view.findViewById(R.id.media_slider_video_view);
        ImageView imageView = (ImageView) view.findViewById(R.id.media_slider_video_thumbnail);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.media_slider_video_play);
        int i = 8;
        videoView.setVisibility(z ? 8 : 0);
        imageView.setVisibility(z ? 0 : 8);
        if (z) {
            i = 0;
        }
        imageView2.setVisibility(i);
    }

    public void stopPlayingVideo() {
        VideoView videoView = this.mPlayingVideoView;
        if (videoView != null) {
            videoView.stopPlayback();
            displayVideoThumbnail((View) this.mPlayingVideoView.getParent(), true);
            this.mPlayingVideoView = null;
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00c8 A[SYNTHETIC, Splitter:B:47:0x00c8] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00d0 A[Catch:{ Exception -> 0x00cc }] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00ef A[Catch:{ Exception -> 0x0127 }] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0102 A[SYNTHETIC, Splitter:B:61:0x0102] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x010a A[Catch:{ Exception -> 0x0106 }] */
    public void playVideo(View view, VideoView videoView, File file, String str) {
        FileOutputStream fileOutputStream;
        FileInputStream fileInputStream;
        String str2 = "## playVideo() : failed ";
        if (file != null && file.exists()) {
            try {
                stopPlayingVideo();
                String extensionFromMimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(str);
                if (extensionFromMimeType != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(extensionFromMimeType);
                    sb.append(".");
                    sb.append(extensionFromMimeType);
                    extensionFromMimeType = sb.toString();
                }
                File cacheDir = this.mContext.getCacheDir();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("sliderMedia");
                sb2.append(extensionFromMimeType);
                File file2 = new File(cacheDir, sb2.toString());
                if (file2.exists()) {
                    file2.delete();
                }
                FileInputStream fileInputStream2 = null;
                try {
                    if (!file2.exists()) {
                        file2.createNewFile();
                        fileInputStream = new FileInputStream(file);
                        try {
                            fileOutputStream = new FileOutputStream(file2);
                            try {
                                byte[] bArr = new byte[10240];
                                while (true) {
                                    int read = fileInputStream.read(bArr);
                                    if (read == -1) {
                                        break;
                                    }
                                    fileOutputStream.write(bArr, 0, read);
                                }
                                fileInputStream2 = fileInputStream;
                            } catch (Exception e) {
                                e = e;
                                try {
                                    String str3 = LOG_TAG;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append(str2);
                                    sb3.append(e.getMessage());
                                    Log.e(str3, sb3.toString(), e);
                                    if (fileInputStream != null) {
                                    }
                                    if (fileOutputStream != null) {
                                    }
                                    file2 = null;
                                    if (file2 != null) {
                                    }
                                    displayVideoThumbnail(view, false);
                                    this.mPlayingVideoView = videoView;
                                    videoView.start();
                                } catch (Throwable th) {
                                    th = th;
                                    if (fileInputStream != null) {
                                    }
                                    if (fileOutputStream != null) {
                                    }
                                    throw th;
                                }
                            }
                        } catch (Exception e2) {
                            e = e2;
                            fileOutputStream = null;
                            String str32 = LOG_TAG;
                            StringBuilder sb32 = new StringBuilder();
                            sb32.append(str2);
                            sb32.append(e.getMessage());
                            Log.e(str32, sb32.toString(), e);
                            if (fileInputStream != null) {
                            }
                            if (fileOutputStream != null) {
                            }
                            file2 = null;
                            if (file2 != null) {
                            }
                            displayVideoThumbnail(view, false);
                            this.mPlayingVideoView = videoView;
                            videoView.start();
                        } catch (Throwable th2) {
                            th = th2;
                            fileOutputStream = null;
                            if (fileInputStream != null) {
                            }
                            if (fileOutputStream != null) {
                            }
                            throw th;
                        }
                    } else {
                        fileOutputStream = null;
                    }
                    if (fileInputStream2 != null) {
                        try {
                            fileInputStream2.close();
                        } catch (Exception e3) {
                            String str4 = LOG_TAG;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(str2);
                            sb4.append(e3.getMessage());
                            Log.e(str4, sb4.toString(), e3);
                        }
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (Exception e4) {
                    e = e4;
                    fileOutputStream = null;
                    fileInputStream = null;
                    String str322 = LOG_TAG;
                    StringBuilder sb322 = new StringBuilder();
                    sb322.append(str2);
                    sb322.append(e.getMessage());
                    Log.e(str322, sb322.toString(), e);
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e5) {
                            String str5 = LOG_TAG;
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append(str2);
                            sb5.append(e5.getMessage());
                            Log.e(str5, sb5.toString(), e5);
                            file2 = null;
                            if (file2 != null) {
                            }
                            displayVideoThumbnail(view, false);
                            this.mPlayingVideoView = videoView;
                            videoView.start();
                        }
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    file2 = null;
                    if (file2 != null) {
                    }
                    displayVideoThumbnail(view, false);
                    this.mPlayingVideoView = videoView;
                    videoView.start();
                } catch (Throwable th3) {
                    th = th3;
                    fileOutputStream = null;
                    fileInputStream = null;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Exception e6) {
                            String str6 = LOG_TAG;
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append(str2);
                            sb6.append(e6.getMessage());
                            Log.e(str6, sb6.toString(), e6);
                            throw th;
                        }
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    throw th;
                }
                if (file2 != null) {
                    videoView.setVideoPath(file2.getAbsolutePath());
                }
                displayVideoThumbnail(view, false);
                this.mPlayingVideoView = videoView;
                videoView.start();
            } catch (Exception e7) {
                String str7 = LOG_TAG;
                StringBuilder sb7 = new StringBuilder();
                sb7.append("## playVideo() : videoView.start(); failed ");
                sb7.append(e7.getMessage());
                Log.e(str7, sb7.toString(), e7);
            }
        }
    }

    public void downloadMediaAndExportToDownloads() {
        if (((VectorMediasViewerActivity) this.mContext).checkWritePermission(600)) {
            final SlidableMediaInfo slidableMediaInfo = (SlidableMediaInfo) this.mMediasMessagesList.get(this.mLatestPrimaryItemPosition);
            if (slidableMediaInfo != null) {
                MediaScanManager mediaScanManager = this.mMediaScanManager;
                if (mediaScanManager != null && mediaScanManager.isTrustedSlidableMediaInfo(slidableMediaInfo)) {
                    if (this.mMediasCache.isMediaCached(slidableMediaInfo.mMediaUrl, slidableMediaInfo.mMimeType)) {
                        this.mMediasCache.createTmpDecryptedMediaFile(slidableMediaInfo.mMediaUrl, slidableMediaInfo.mMimeType, slidableMediaInfo.mEncryptedFileInfo, new SimpleApiCallback<File>() {
                            public void onSuccess(File file) {
                                if (file != null) {
                                    CommonActivityUtils.saveMediaIntoDownloads(VectorMediasViewerAdapter.this.mContext, file, null, slidableMediaInfo.mMimeType, new SimpleApiCallback<String>() {
                                        public void onSuccess(String str) {
                                            Toast.makeText(VectorMediasViewerAdapter.this.mContext, VectorMediasViewerAdapter.this.mContext.getText(R.string.media_slider_saved), 1).show();
                                        }
                                    });
                                }
                            }
                        });
                        return;
                    }
                    downloadVideo(this.mLatestPrimaryView, this.mLatestPrimaryItemPosition, true);
                    final String downloadMedia = this.mMediasCache.downloadMedia(this.mContext, this.mSession.getHomeServerConfig(), slidableMediaInfo.mMediaUrl, slidableMediaInfo.mMimeType, slidableMediaInfo.mEncryptedFileInfo);
                    if (downloadMedia != null) {
                        this.mMediasCache.addDownloadListener(downloadMedia, new MXMediaDownloadListener() {
                            public void onDownloadError(String str, JsonElement jsonElement) {
                                MatrixError matrixError = JsonUtils.toMatrixError(jsonElement);
                                if (matrixError != null && matrixError.isSupportedErrorCode()) {
                                    Toast.makeText(VectorMediasViewerAdapter.this.mContext, matrixError.getLocalizedMessage(), 1).show();
                                }
                            }

                            public void onDownloadComplete(String str) {
                                if (str.equals(downloadMedia) && VectorMediasViewerAdapter.this.mMediasCache.isMediaCached(slidableMediaInfo.mMediaUrl, slidableMediaInfo.mMimeType)) {
                                    VectorMediasViewerAdapter.this.mMediasCache.createTmpDecryptedMediaFile(slidableMediaInfo.mMediaUrl, slidableMediaInfo.mMimeType, slidableMediaInfo.mEncryptedFileInfo, new SimpleApiCallback<File>() {
                                        public void onSuccess(File file) {
                                            if (file != null) {
                                                CommonActivityUtils.saveMediaIntoDownloads(VectorMediasViewerAdapter.this.mContext, file, null, slidableMediaInfo.mMimeType, new SimpleApiCallback<String>() {
                                                    public void onSuccess(String str) {
                                                        Toast.makeText(VectorMediasViewerAdapter.this.mContext, R.string.media_slider_saved, 1).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        return;
                    }
                    return;
                }
            }
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onAction : the media is unchecked or untrusted ");
            sb.append(slidableMediaInfo.mMediaUrl);
            Log.e(str, sb.toString());
        }
    }

    /* access modifiers changed from: private */
    public void onLongClickOnMedia() {
        new Builder(this.mContext).setMessage((int) R.string.media_slider_saved_message).setPositiveButton((int) R.string.yes, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                VectorMediasViewerAdapter.this.downloadMediaAndExportToDownloads();
            }
        }).setNegativeButton((int) R.string.no, (OnClickListener) null).show();
    }

    /* access modifiers changed from: private */
    public void loadVideo(int i, View view, String str, String str2, String str3, EncryptedFileInfo encryptedFileInfo, EncryptedFileInfo encryptedFileInfo2) {
        final View view2 = view;
        final VideoView videoView = (VideoView) view2.findViewById(R.id.media_slider_video_view);
        ImageView imageView = (ImageView) view2.findViewById(R.id.media_slider_video_thumbnail);
        ImageView imageView2 = (ImageView) view2.findViewById(R.id.media_slider_video_play);
        displayVideoThumbnail(view2, !videoView.isPlaying());
        videoView.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                VectorMediasViewerAdapter.this.mPlayingVideoView = null;
                VectorMediasViewerAdapter.this.displayVideoThumbnail(view2, true);
            }
        });
        ((View) videoView.getParent()).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                VectorMediasViewerAdapter.this.stopPlayingVideo();
                VectorMediasViewerAdapter.this.displayVideoThumbnail(view2, true);
            }
        });
        videoView.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                VectorMediasViewerAdapter.this.mPlayingVideoView = null;
                VectorMediasViewerAdapter.this.displayVideoThumbnail(view2, true);
                return false;
            }
        });
        this.mMediasCache.loadBitmap(this.mSession.getHomeServerConfig(), imageView, str, 0, 0, (String) null, encryptedFileInfo);
        final String str4 = str2;
        final String str5 = str3;
        final EncryptedFileInfo encryptedFileInfo3 = encryptedFileInfo2;
        final View view3 = view;
        ImageView imageView3 = imageView2;
        final int i2 = i;
        AnonymousClass16 r0 = new View.OnClickListener() {
            public void onClick(View view) {
                if (VectorMediasViewerAdapter.this.mMediasCache.isMediaCached(str4, str5)) {
                    VectorMediasViewerAdapter.this.mMediasCache.createTmpDecryptedMediaFile(str4, str5, encryptedFileInfo3, new SimpleApiCallback<File>() {
                        public void onSuccess(File file) {
                            if (file != null) {
                                VectorMediasViewerAdapter.this.playVideo(view3, videoView, file, str5);
                            }
                        }
                    });
                    return;
                }
                VectorMediasViewerAdapter.this.mAutoPlayItemAt = i2;
                VectorMediasViewerAdapter.this.downloadVideo(view3, i2);
            }
        };
        imageView3.setOnClickListener(r0);
    }
}
