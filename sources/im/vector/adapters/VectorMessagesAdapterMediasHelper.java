package im.vector.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonElement;
import fr.gouv.tchap.a.R;
import im.vector.listeners.IMessagesAdapterActionsListener;
import java.util.HashMap;
import java.util.Map;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.ResourceUtils;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.listeners.IMXMediaDownloadListener.DownloadStats;
import org.matrix.androidsdk.listeners.IMXMediaUploadListener.UploadStats;
import org.matrix.androidsdk.listeners.MXMediaDownloadListener;
import org.matrix.androidsdk.listeners.MXMediaUploadListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.message.FileMessage;
import org.matrix.androidsdk.rest.model.message.ImageInfo;
import org.matrix.androidsdk.rest.model.message.ImageMessage;
import org.matrix.androidsdk.rest.model.message.Message;
import org.matrix.androidsdk.rest.model.message.StickerMessage;
import org.matrix.androidsdk.rest.model.message.VideoMessage;

class VectorMessagesAdapterMediasHelper {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorMessagesAdapterMediasHelper.class.getSimpleName();
    /* access modifiers changed from: private */
    public final Context mContext;
    private final int mDefaultMessageTextColor;
    private final int mMaxImageHeight;
    private final int mMaxImageWidth;
    private final MXMediaCache mMediasCache;
    private final int mNotSentMessageTextColor;
    private final MXSession mSession;
    private Map<String, String> mUrlByBitmapIndex = new HashMap();
    /* access modifiers changed from: private */
    public IMessagesAdapterActionsListener mVectorMessagesAdapterEventsListener;

    VectorMessagesAdapterMediasHelper(Context context, MXSession mXSession, int i, int i2, int i3, int i4) {
        this.mContext = context;
        this.mSession = mXSession;
        this.mMaxImageWidth = i;
        this.mMaxImageHeight = i2;
        this.mMediasCache = this.mSession.getMediaCache();
        this.mNotSentMessageTextColor = i3;
        this.mDefaultMessageTextColor = i4;
    }

    /* access modifiers changed from: 0000 */
    public void setVectorMessagesAdapterActionsListener(IMessagesAdapterActionsListener iMessagesAdapterActionsListener) {
        this.mVectorMessagesAdapterEventsListener = iMessagesAdapterActionsListener;
    }

    /* access modifiers changed from: 0000 */
    public void managePendingUpload(View view, Event event, int i, String str) {
        View view2 = view;
        int i2 = i;
        String str2 = str;
        View findViewById = view2.findViewById(R.id.content_upload_progress_layout);
        ProgressBar progressBar = (ProgressBar) view2.findViewById(R.id.upload_event_spinner);
        if (!(findViewById == null || progressBar == null)) {
            findViewById.setTag(str2);
            if (!this.mSession.getMyUserId().equals(event.getSender()) || !event.isSending()) {
                Event event2 = event;
                findViewById.setVisibility(8);
                progressBar.setVisibility(8);
                showUploadFailure(view2, i2, event.isUndelivered());
            } else {
                UploadStats statsForUploadId = this.mSession.getMediaCache().getStatsForUploadId(str2);
                if (statsForUploadId != null) {
                    MXMediaCache mediaCache = this.mSession.getMediaCache();
                    final View view3 = findViewById;
                    final Event event3 = event;
                    final View view4 = view;
                    final int i3 = i;
                    AnonymousClass1 r13 = r0;
                    final ProgressBar progressBar2 = progressBar;
                    AnonymousClass1 r0 = new MXMediaUploadListener() {
                        public void onUploadProgress(String str, UploadStats uploadStats) {
                            if (TextUtils.equals((String) view3.getTag(), str)) {
                                VectorMessagesAdapterMediasHelper.this.refreshUploadViews(event3, uploadStats, view3);
                            }
                        }

                        private void onUploadStop(String str) {
                            if (!TextUtils.isEmpty(str)) {
                                Toast.makeText(VectorMessagesAdapterMediasHelper.this.mContext, str, 1).show();
                            }
                            VectorMessagesAdapterMediasHelper.this.showUploadFailure(view4, i3, true);
                            view3.setVisibility(8);
                            progressBar2.setVisibility(8);
                        }

                        public void onUploadCancel(String str) {
                            if (TextUtils.equals((String) view3.getTag(), str)) {
                                onUploadStop(null);
                            }
                        }

                        public void onUploadError(String str, int i, String str2) {
                            if (TextUtils.equals((String) view3.getTag(), str)) {
                                onUploadStop(str2);
                            }
                        }

                        public void onUploadComplete(String str, String str2) {
                            if (TextUtils.equals((String) view3.getTag(), str)) {
                                progressBar2.setVisibility(8);
                            }
                        }
                    };
                    mediaCache.addUploadListener(str2, r13);
                }
                int i4 = 0;
                showUploadFailure(view2, i2, false);
                if (statsForUploadId != null) {
                    i4 = 8;
                }
                progressBar.setVisibility(i4);
                refreshUploadViews(event, statsForUploadId, findViewById);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0105, code lost:
        if (android.text.TextUtils.equals(r6.toString(), (java.lang.CharSequence) r0.mUrlByBitmapIndex.get(r7)) == false) goto L_0x0107;
     */
    public void managePendingImageVideoDownload(View view, Event event, Message message, int i) {
        int i2;
        int i3;
        EncryptedFileInfo encryptedFileInfo;
        int i4;
        int i5;
        String str;
        int i6;
        int i7;
        LayoutParams layoutParams;
        boolean z;
        String str2;
        String str3;
        ImageView imageView;
        int i8;
        int i9;
        EncryptedFileInfo encryptedFileInfo2;
        int i10;
        int i11;
        int i12;
        View view2 = view;
        Event event2 = event;
        Message message2 = message;
        int i13 = this.mMaxImageWidth;
        int i14 = this.mMaxImageHeight;
        boolean z2 = message2 instanceof ImageMessage;
        int i15 = -1;
        if (z2) {
            ImageMessage imageMessage = (ImageMessage) message2;
            imageMessage.checkMediaUrls();
            if (imageMessage.getThumbnailUrl() != null) {
                str = imageMessage.getThumbnailUrl();
                encryptedFileInfo2 = imageMessage.info != null ? imageMessage.info.thumbnail_file : null;
            } else if (imageMessage.getUrl() != null) {
                str = imageMessage.getUrl();
                encryptedFileInfo2 = imageMessage.file;
            } else {
                str = null;
                encryptedFileInfo2 = null;
            }
            int rotation = imageMessage.getRotation();
            ImageInfo imageInfo = imageMessage.info;
            if (imageInfo != null) {
                if (imageInfo.w == null || imageInfo.h == null) {
                    i11 = -1;
                    i10 = -1;
                } else {
                    i11 = imageInfo.w.intValue();
                    i10 = imageInfo.h.intValue();
                }
                i12 = imageInfo.orientation != null ? imageInfo.orientation.intValue() : 1;
            } else {
                i12 = 1;
                i11 = -1;
                i10 = -1;
            }
            encryptedFileInfo = encryptedFileInfo2;
            i3 = i11;
            i2 = i10;
            i5 = i12;
            i4 = rotation;
        } else if (message2 instanceof VideoMessage) {
            VideoMessage videoMessage = (VideoMessage) message2;
            videoMessage.checkMediaUrls();
            String thumbnailUrl = videoMessage.getThumbnailUrl();
            EncryptedFileInfo encryptedFileInfo3 = videoMessage.info != null ? videoMessage.info.thumbnail_file : null;
            if (videoMessage.info == null || videoMessage.info.thumbnail_info == null || videoMessage.info.thumbnail_info.w == null || videoMessage.info.thumbnail_info.h == null) {
                encryptedFileInfo = encryptedFileInfo3;
                i5 = 1;
                i4 = 0;
                i3 = -1;
                i2 = -1;
            } else {
                int intValue = videoMessage.info.thumbnail_info.w.intValue();
                i2 = videoMessage.info.thumbnail_info.h.intValue();
                encryptedFileInfo = encryptedFileInfo3;
                i3 = intValue;
                i5 = 1;
                i4 = 0;
            }
            str = thumbnailUrl;
        } else {
            str = null;
            encryptedFileInfo = null;
            i5 = 1;
            i4 = 0;
            i3 = -1;
            i2 = -1;
        }
        ImageView imageView2 = (ImageView) view2.findViewById(R.id.messagesAdapter_image);
        String str4 = "";
        if (str != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(imageView2.hashCode());
            sb.append(str4);
        }
        imageView2.setImageBitmap(null);
        if (str != null) {
            Map<String, String> map = this.mUrlByBitmapIndex;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(imageView2.hashCode());
            sb2.append(str4);
            map.put(str, sb2.toString());
        }
        LayoutParams layoutParams2 = (LayoutParams) ((RelativeLayout) view2.findViewById(R.id.messagesAdapter_image_layout)).getLayoutParams();
        String type = event.getType();
        String str5 = Event.EVENT_TYPE_STICKER;
        if (!type.equals(str5)) {
            str3 = str5;
            layoutParams = layoutParams2;
            imageView = imageView2;
            i7 = i5;
            i6 = i4;
            z = z2;
            str2 = this.mMediasCache.loadBitmap(this.mSession.getHomeServerConfig(), imageView2, str, i13, i14, i4, 0, ResourceUtils.MIME_TYPE_JPEG, encryptedFileInfo);
        } else {
            str3 = str5;
            layoutParams = layoutParams2;
            imageView = imageView2;
            i7 = i5;
            i6 = i4;
            z = z2;
            str2 = null;
        }
        if (str2 == null) {
            if (message2 instanceof VideoMessage) {
                str2 = this.mMediasCache.downloadIdFromUrl(((VideoMessage) message2).getUrl());
            } else if (z) {
                str2 = this.mMediasCache.downloadIdFromUrl(((ImageMessage) message2).getUrl());
            }
        }
        String str6 = str2;
        if (event.getType().equals(str3)) {
            i8 = 0;
            String downloadableUrl = this.mSession.getContentManager().getDownloadableUrl(((StickerMessage) message2).getUrl(), false);
            if (downloadableUrl != null) {
                Glide.with(this.mContext).load(downloadableUrl).apply(new RequestOptions().override(i13, i14).fitCenter().placeholder((int) R.drawable.sticker_placeholder)).into(imageView);
            } else {
                imageView.setImageResource(R.drawable.sticker_placeholder);
            }
        } else {
            i8 = 0;
        }
        ImageView imageView3 = imageView;
        final View findViewById = view.findViewById(R.id.content_download_progress_layout);
        if (findViewById != null) {
            findViewById.setTag(str6);
            if (i3 <= 0 || i2 <= 0) {
                i9 = -1;
            } else {
                int i16 = i6;
                if (!(i16 == 90 || i16 == 270)) {
                    int i17 = i7;
                    if (!(i17 == 6 || i17 == 8)) {
                        int i18 = i2;
                        i2 = i3;
                        i3 = i18;
                    }
                }
                i15 = Math.min((i13 * i3) / i2, i14);
                i9 = (i2 * i15) / i3;
            }
            int i19 = i15 < 0 ? this.mMaxImageHeight : i15;
            if (i9 < 0) {
                i9 = this.mMaxImageWidth;
            }
            int i20 = i9;
            LayoutParams layoutParams3 = layoutParams;
            layoutParams3.height = i19;
            layoutParams3.width = i20;
            if (str6 != null) {
                findViewById.setVisibility(i8);
                final Event event3 = event;
                final int i21 = i;
                this.mMediasCache.addDownloadListener(str6, new MXMediaDownloadListener() {
                    public void onDownloadCancel(String str) {
                        if (TextUtils.equals(str, (String) findViewById.getTag())) {
                            findViewById.setVisibility(8);
                        }
                    }

                    public void onDownloadError(String str, JsonElement jsonElement) {
                        if (TextUtils.equals(str, (String) findViewById.getTag())) {
                            MatrixError matrixError = null;
                            try {
                                matrixError = JsonUtils.toMatrixError(jsonElement);
                            } catch (Exception e) {
                                String access$300 = VectorMessagesAdapterMediasHelper.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("Cannot cast to Matrix error ");
                                sb.append(e.getLocalizedMessage());
                                Log.e(access$300, sb.toString(), e);
                            }
                            findViewById.setVisibility(8);
                            if (matrixError != null && matrixError.isSupportedErrorCode()) {
                                Toast.makeText(VectorMessagesAdapterMediasHelper.this.mContext, matrixError.getLocalizedMessage(), 1).show();
                            } else if (jsonElement != null) {
                                Toast.makeText(VectorMessagesAdapterMediasHelper.this.mContext, jsonElement.toString(), 1).show();
                            }
                        }
                    }

                    public void onDownloadProgress(String str, DownloadStats downloadStats) {
                        if (TextUtils.equals(str, (String) findViewById.getTag())) {
                            VectorMessagesAdapterMediasHelper.this.refreshDownloadViews(event3, downloadStats, findViewById);
                        }
                    }

                    public void onDownloadComplete(String str) {
                        if (TextUtils.equals(str, (String) findViewById.getTag())) {
                            findViewById.setVisibility(8);
                            if (VectorMessagesAdapterMediasHelper.this.mVectorMessagesAdapterEventsListener != null) {
                                VectorMessagesAdapterMediasHelper.this.mVectorMessagesAdapterEventsListener.onMediaDownloaded(i21);
                            }
                        }
                    }
                });
                refreshDownloadViews(event3, this.mMediasCache.getStatsForDownloadId(str6), findViewById);
            } else {
                findViewById.setVisibility(8);
            }
            imageView3.setBackgroundColor(i8);
            imageView3.setScaleType(ScaleType.CENTER_CROP);
        }
    }

    /* access modifiers changed from: 0000 */
    public void managePendingImageVideoUpload(View view, Event event, Message message) {
        String str;
        boolean z;
        boolean z2;
        int i;
        String str2;
        String str3;
        boolean z3;
        View view2 = view;
        Message message2 = message;
        View findViewById = view2.findViewById(R.id.content_upload_progress_layout);
        ProgressBar progressBar = (ProgressBar) view2.findViewById(R.id.upload_event_spinner);
        boolean z4 = message2 instanceof VideoMessage;
        if (!(findViewById == null || progressBar == null)) {
            findViewById.setTag(null);
            boolean z5 = (z4 ? ((VideoMessage) message2).info : ((ImageMessage) message2).info) != null;
            if (!this.mSession.getMyUserId().equals(event.getSender()) || event.isUndelivered() || !z5) {
                Event event2 = event;
                findViewById.setVisibility(8);
                progressBar.setVisibility(8);
                showUploadFailure(view2, z4 ? 5 : 1, event.isUndelivered());
            } else {
                if (z4) {
                    VideoMessage videoMessage = (VideoMessage) message2;
                    str = videoMessage.getThumbnailUrl();
                    z = videoMessage.isThumbnailLocalContent();
                } else {
                    ImageMessage imageMessage = (ImageMessage) message2;
                    str = imageMessage.getThumbnailUrl();
                    z = imageMessage.isThumbnailLocalContent();
                }
                boolean z6 = z;
                if (z6) {
                    i = this.mSession.getMediaCache().getProgressValueForUploadId(str);
                    str2 = str;
                    z2 = false;
                } else {
                    if (z4) {
                        VideoMessage videoMessage2 = (VideoMessage) message2;
                        str = videoMessage2.getUrl();
                        z3 = videoMessage2.isLocalContent();
                    } else if (message2 instanceof ImageMessage) {
                        ImageMessage imageMessage2 = (ImageMessage) message2;
                        str = imageMessage2.getUrl();
                        z3 = imageMessage2.isLocalContent();
                    } else {
                        z3 = false;
                    }
                    z2 = z3;
                    i = this.mSession.getMediaCache().getProgressValueForUploadId(str);
                    str2 = str;
                }
                if (i >= 0) {
                    findViewById.setTag(str2);
                    final View view3 = findViewById;
                    final Event event3 = event;
                    AnonymousClass3 r13 = r0;
                    final boolean z7 = z6;
                    MXMediaCache mediaCache = this.mSession.getMediaCache();
                    final View view4 = view;
                    str3 = str2;
                    final boolean z8 = z4;
                    final ProgressBar progressBar2 = progressBar;
                    AnonymousClass3 r0 = new MXMediaUploadListener() {
                        public void onUploadProgress(String str, UploadStats uploadStats) {
                            int i;
                            if (TextUtils.equals((String) view3.getTag(), str)) {
                                VectorMessagesAdapterMediasHelper.this.refreshUploadViews(event3, uploadStats, view3);
                                if (!z7) {
                                    i = ((uploadStats.mProgress * 90) / 100) + 10;
                                } else {
                                    i = (uploadStats.mProgress * 10) / 100;
                                }
                                VectorMessagesAdapterMediasHelper.updateUploadProgress(view3, i);
                            }
                        }

                        private void onUploadStop(String str) {
                            if (!TextUtils.isEmpty(str)) {
                                Toast.makeText(VectorMessagesAdapterMediasHelper.this.mContext, str, 1).show();
                            }
                            VectorMessagesAdapterMediasHelper.this.showUploadFailure(view4, z8 ? 5 : 1, true);
                            view3.setVisibility(8);
                            progressBar2.setVisibility(8);
                        }

                        public void onUploadCancel(String str) {
                            if (TextUtils.equals((String) view3.getTag(), str)) {
                                onUploadStop(null);
                            }
                        }

                        public void onUploadError(String str, int i, String str2) {
                            if (TextUtils.equals((String) view3.getTag(), str)) {
                                onUploadStop(str2);
                            }
                        }

                        public void onUploadComplete(String str, String str2) {
                            if (TextUtils.equals((String) view3.getTag(), str)) {
                                progressBar2.setVisibility(8);
                            }
                        }
                    };
                    mediaCache.addUploadListener(str3, r13);
                } else {
                    str3 = str2;
                }
                int i2 = 0;
                showUploadFailure(view2, z4 ? 5 : 1, false);
                progressBar.setVisibility((i >= 0 || !event.isSending()) ? 8 : 0);
                refreshUploadViews(event, this.mSession.getMediaCache().getStatsForUploadId(str3), findViewById);
                if (z2) {
                    i = ((i * 90) / 100) + 10;
                } else if (z6) {
                    i = (i * 10) / 100;
                }
                int i3 = i;
                updateUploadProgress(findViewById, i3);
                if (i3 < 0 || !event.isSending()) {
                    i2 = 8;
                }
                findViewById.setVisibility(i2);
            }
        }
    }

    /* access modifiers changed from: private */
    public static void updateUploadProgress(View view, int i) {
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.media_progress_view);
        if (progressBar != null) {
            progressBar.setProgress(i);
        }
    }

    /* access modifiers changed from: private */
    public void refreshUploadViews(final Event event, UploadStats uploadStats, View view) {
        if (uploadStats != null) {
            view.setVisibility(0);
            TextView textView = (TextView) view.findViewById(R.id.media_progress_text_view);
            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.media_progress_view);
            if (textView != null) {
                textView.setText(formatUploadStats(this.mContext, uploadStats));
            }
            if (progressBar != null) {
                progressBar.setProgress(uploadStats.mProgress);
            }
            final View findViewById = view.findViewById(R.id.media_progress_cancel);
            if (findViewById != null) {
                findViewById.setTag(event);
                findViewById.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (event == findViewById.getTag() && VectorMessagesAdapterMediasHelper.this.mVectorMessagesAdapterEventsListener != null) {
                            VectorMessagesAdapterMediasHelper.this.mVectorMessagesAdapterEventsListener.onEventAction(event, "", R.id.ic_action_vector_cancel_upload);
                        }
                    }
                });
                return;
            }
            return;
        }
        view.setVisibility(8);
    }

    /* access modifiers changed from: 0000 */
    public void managePendingFileDownload(View view, final Event event, FileMessage fileMessage, final int i) {
        String downloadIdFromUrl = this.mMediasCache.downloadIdFromUrl(fileMessage.getUrl());
        final View findViewById = view.findViewById(R.id.content_download_progress_layout);
        if (findViewById != null) {
            findViewById.setTag(downloadIdFromUrl);
            if (downloadIdFromUrl != null) {
                findViewById.setVisibility(0);
                this.mMediasCache.addDownloadListener(downloadIdFromUrl, new MXMediaDownloadListener() {
                    public void onDownloadCancel(String str) {
                        if (TextUtils.equals(str, (String) findViewById.getTag())) {
                            findViewById.setVisibility(8);
                        }
                    }

                    public void onDownloadError(String str, JsonElement jsonElement) {
                        if (TextUtils.equals(str, (String) findViewById.getTag())) {
                            MatrixError matrixError = null;
                            try {
                                matrixError = JsonUtils.toMatrixError(jsonElement);
                            } catch (Exception e) {
                                String access$300 = VectorMessagesAdapterMediasHelper.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("Cannot cast to Matrix error ");
                                sb.append(e.getLocalizedMessage());
                                Log.e(access$300, sb.toString(), e);
                            }
                            findViewById.setVisibility(8);
                            if (matrixError != null && matrixError.isSupportedErrorCode()) {
                                Toast.makeText(VectorMessagesAdapterMediasHelper.this.mContext, matrixError.getLocalizedMessage(), 1).show();
                            } else if (jsonElement != null) {
                                Toast.makeText(VectorMessagesAdapterMediasHelper.this.mContext, jsonElement.toString(), 1).show();
                            }
                        }
                    }

                    public void onDownloadProgress(String str, DownloadStats downloadStats) {
                        if (TextUtils.equals(str, (String) findViewById.getTag())) {
                            VectorMessagesAdapterMediasHelper.this.refreshDownloadViews(event, downloadStats, findViewById);
                        }
                    }

                    public void onDownloadComplete(String str) {
                        if (TextUtils.equals(str, (String) findViewById.getTag())) {
                            findViewById.setVisibility(8);
                            if (VectorMessagesAdapterMediasHelper.this.mVectorMessagesAdapterEventsListener != null) {
                                VectorMessagesAdapterMediasHelper.this.mVectorMessagesAdapterEventsListener.onMediaDownloaded(i);
                            }
                        }
                    }
                });
                refreshDownloadViews(event, this.mMediasCache.getStatsForDownloadId(downloadIdFromUrl), findViewById);
            } else {
                findViewById.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: private */
    public void showUploadFailure(View view, int i, boolean z) {
        if (4 == i) {
            TextView textView = (TextView) view.findViewById(R.id.messagesAdapter_filename);
            if (textView != null) {
                textView.setTextColor(z ? this.mNotSentMessageTextColor : this.mDefaultMessageTextColor);
            }
        } else if (1 == i || 5 == i) {
            View findViewById = view.findViewById(R.id.media_upload_failed);
            if (findViewById != null) {
                findViewById.setVisibility(z ? 0 : 8);
            }
        }
    }

    /* access modifiers changed from: private */
    public void refreshDownloadViews(final Event event, DownloadStats downloadStats, View view) {
        if (downloadStats == null || !isMediaDownloading(event)) {
            view.setVisibility(8);
            return;
        }
        view.setVisibility(0);
        TextView textView = (TextView) view.findViewById(R.id.media_progress_text_view);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.media_progress_view);
        if (textView != null) {
            textView.setText(formatDownloadStats(this.mContext, downloadStats));
        }
        if (progressBar != null) {
            progressBar.setProgress(downloadStats.mProgress);
        }
        final View findViewById = view.findViewById(R.id.media_progress_cancel);
        if (findViewById != null) {
            findViewById.setTag(event);
            findViewById.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (event == findViewById.getTag() && VectorMessagesAdapterMediasHelper.this.mVectorMessagesAdapterEventsListener != null) {
                        VectorMessagesAdapterMediasHelper.this.mVectorMessagesAdapterEventsListener.onEventAction(event, "", R.id.ic_action_vector_cancel_download);
                    }
                }
            });
        }
    }

    private boolean isMediaDownloading(Event event) {
        if (!TextUtils.equals(event.getType(), Event.EVENT_TYPE_MESSAGE)) {
            return false;
        }
        Message message = JsonUtils.toMessage(event.getContent());
        String str = null;
        if (TextUtils.equals(message.msgtype, Message.MSGTYPE_IMAGE)) {
            str = JsonUtils.toImageMessage(event.getContent()).getUrl();
        } else if (TextUtils.equals(message.msgtype, Message.MSGTYPE_VIDEO)) {
            str = JsonUtils.toVideoMessage(event.getContent()).getUrl();
        } else if (TextUtils.equals(message.msgtype, Message.MSGTYPE_FILE)) {
            str = JsonUtils.toFileMessage(event.getContent()).getUrl();
        }
        if (TextUtils.isEmpty(str) || this.mSession.getMediaCache().downloadIdFromUrl(str) == null) {
            return false;
        }
        return true;
    }

    private static String vectorRemainingTimeToString(Context context, int i) {
        if (i < 0) {
            return "";
        }
        if (i <= 1) {
            return "< 1s";
        }
        if (i < 60) {
            return context.getString(R.string.attachment_remaining_time_seconds, new Object[]{Integer.valueOf(i)});
        } else if (i >= 3600) {
            return DateUtils.formatElapsedTime((long) i);
        } else {
            return context.getString(R.string.attachment_remaining_time_minutes, new Object[]{Integer.valueOf(i / 60), Integer.valueOf(i % 60)});
        }
    }

    private static String formatStats(Context context, int i, int i2, int i3) {
        String str = "";
        if (i2 > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(Formatter.formatShortFileSize(context, (long) i));
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(sb2);
            sb3.append(" / ");
            sb3.append(Formatter.formatShortFileSize(context, (long) i2));
            str = sb3.toString();
        }
        if (i3 <= 0) {
            return str;
        }
        if (!TextUtils.isEmpty(str)) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str);
            sb4.append(" (");
            sb4.append(vectorRemainingTimeToString(context, i3));
            sb4.append(")");
            return sb4.toString();
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append(str);
        sb5.append(vectorRemainingTimeToString(context, i3));
        return sb5.toString();
    }

    private static String formatDownloadStats(Context context, DownloadStats downloadStats) {
        return formatStats(context, downloadStats.mDownloadedSize, downloadStats.mFileSize, downloadStats.mEstimatedRemainingTime);
    }

    private static String formatUploadStats(Context context, UploadStats uploadStats) {
        return formatStats(context, uploadStats.mUploadedSize, uploadStats.mFileSize, uploadStats.mEstimatedRemainingTime);
    }
}
