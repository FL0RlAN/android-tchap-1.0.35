package im.vector.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import fr.gouv.tchap.a.R;
import im.vector.activity.VectorRoomActivity;
import im.vector.fragments.ImageSizeSelectionDialogFragment;
import im.vector.fragments.VectorMessageListFragment;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.matrix.androidsdk.core.ImageUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.ResourceUtils;
import org.matrix.androidsdk.core.ResourceUtils.Resource;
import org.matrix.androidsdk.data.RoomMediaMessage;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.rest.model.message.Message;

public class VectorRoomMediasSender {
    private static final int LARGE_IMAGE_SIZE = 2048;
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorRoomMediasSender.class.getSimpleName();
    private static final int MEDIUM_IMAGE_SIZE = 1024;
    private static final int SMALL_IMAGE_SIZE = 512;
    private static final String TAG_FRAGMENT_IMAGE_SIZE_DIALOG = "TAG_FRAGMENT_IMAGE_SIZE_DIALOG";
    private static HandlerThread mHandlerThread = null;
    private static Handler mMediasSendingHandler = null;
    /* access modifiers changed from: private */
    public String mImageCompressionDescription;
    /* access modifiers changed from: private */
    public AlertDialog mImageSizesListDialog;
    private final MXMediaCache mMediasCache;
    /* access modifiers changed from: private */
    public List<RoomMediaMessage> mSharedDataItems;
    /* access modifiers changed from: private */
    public final VectorMessageListFragment mVectorMessageListFragment;
    /* access modifiers changed from: private */
    public final VectorRoomActivity mVectorRoomActivity;

    private class ImageCompressionSizes {
        public ImageSize mFullImageSize;
        public ImageSize mLargeImageSize;
        public ImageSize mMediumImageSize;
        public ImageSize mSmallImageSize;

        private ImageCompressionSizes() {
        }

        public List<ImageSize> getImageSizesList() {
            ArrayList arrayList = new ArrayList();
            ImageSize imageSize = this.mFullImageSize;
            if (imageSize != null) {
                arrayList.add(imageSize);
            }
            ImageSize imageSize2 = this.mLargeImageSize;
            if (imageSize2 != null) {
                arrayList.add(imageSize2);
            }
            ImageSize imageSize3 = this.mMediumImageSize;
            if (imageSize3 != null) {
                arrayList.add(imageSize3);
            }
            ImageSize imageSize4 = this.mSmallImageSize;
            if (imageSize4 != null) {
                arrayList.add(imageSize4);
            }
            return arrayList;
        }

        public List<String> getImageSizesDescription(Context context) {
            ArrayList arrayList = new ArrayList();
            if (this.mFullImageSize != null) {
                arrayList.add(context.getString(R.string.compression_opt_list_original));
            }
            if (this.mLargeImageSize != null) {
                arrayList.add(context.getString(R.string.compression_opt_list_large));
            }
            if (this.mMediumImageSize != null) {
                arrayList.add(context.getString(R.string.compression_opt_list_medium));
            }
            if (this.mSmallImageSize != null) {
                arrayList.add(context.getString(R.string.compression_opt_list_small));
            }
            return arrayList;
        }

        public ImageSize getImageSize(Context context, String str) {
            boolean equals = TextUtils.equals(context.getString(R.string.compression_opt_list_original), str);
            if (TextUtils.isEmpty(str) || equals) {
                return this.mFullImageSize;
            }
            boolean equals2 = TextUtils.equals(context.getString(R.string.compression_opt_list_small), str);
            boolean equals3 = TextUtils.equals(context.getString(R.string.compression_opt_list_medium), str);
            boolean equals4 = TextUtils.equals(context.getString(R.string.compression_opt_list_large), str);
            ImageSize imageSize = null;
            if (equals2) {
                imageSize = this.mSmallImageSize;
            }
            if (imageSize == null && (equals2 || equals3)) {
                imageSize = this.mMediumImageSize;
            }
            if (imageSize == null && (equals2 || equals3 || equals4)) {
                imageSize = this.mLargeImageSize;
            }
            if (imageSize == null) {
                imageSize = this.mFullImageSize;
            }
            return imageSize;
        }
    }

    private class ImageSize {
        public int mHeight;
        public int mWidth;

        public ImageSize(int i, int i2) {
            this.mWidth = i;
            this.mHeight = i2;
        }

        public ImageSize(ImageSize imageSize) {
            this.mWidth = imageSize.mWidth;
            this.mHeight = imageSize.mHeight;
        }

        /* access modifiers changed from: private */
        public ImageSize computeSizeToFit(float f) {
            if (0.0f == f) {
                return new ImageSize(0, 0);
            }
            ImageSize imageSize = new ImageSize(this);
            if (((float) this.mWidth) > f || ((float) this.mHeight) > f) {
                double highestOneBit = (double) Integer.highestOneBit((int) Math.floor(1.0d / Math.min((double) (f / ((float) this.mWidth)), (double) (f / ((float) this.mHeight)))));
                Double.isNaN(highestOneBit);
                double d = 1.0d / highestOneBit;
                double d2 = (double) imageSize.mWidth;
                Double.isNaN(d2);
                imageSize.mWidth = (int) (Math.floor((d2 * d) / 2.0d) * 2.0d);
                double d3 = (double) imageSize.mHeight;
                Double.isNaN(d3);
                imageSize.mHeight = (int) (Math.floor((d3 * d) / 2.0d) * 2.0d);
            }
            return imageSize;
        }
    }

    private interface OnImageUploadListener {
        void onCancel();

        void onDone();
    }

    public VectorRoomMediasSender(VectorRoomActivity vectorRoomActivity, VectorMessageListFragment vectorMessageListFragment, MXMediaCache mXMediaCache) {
        this.mVectorRoomActivity = vectorRoomActivity;
        this.mVectorMessageListFragment = vectorMessageListFragment;
        this.mMediasCache = mXMediaCache;
        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread("VectorRoomMediasSender", 1);
            mHandlerThread.start();
            mMediasSendingHandler = new Handler(mHandlerThread.getLooper());
        }
    }

    public void resumeResizeMediaAndSend() {
        if (this.mSharedDataItems != null) {
            this.mVectorRoomActivity.runOnUiThread(new Runnable() {
                public void run() {
                    VectorRoomMediasSender.this.sendMedias();
                }
            });
        }
    }

    public void sendMedias(List<RoomMediaMessage> list) {
        if (list != null) {
            this.mSharedDataItems = new ArrayList(list);
            sendMedias();
        }
    }

    /* access modifiers changed from: private */
    public void sendMedias() {
        if (this.mVectorRoomActivity == null || this.mVectorMessageListFragment == null || this.mMediasCache == null) {
            Log.d(LOG_TAG, "sendMedias : null parameters");
            return;
        }
        List<RoomMediaMessage> list = this.mSharedDataItems;
        if (list == null || list.size() == 0) {
            Log.d(LOG_TAG, "sendMedias : done");
            this.mImageCompressionDescription = null;
            this.mSharedDataItems = null;
            this.mVectorRoomActivity.runOnUiThread(new Runnable() {
                public void run() {
                    VectorRoomMediasSender.this.mVectorMessageListFragment.scrollToBottom();
                    VectorRoomMediasSender.this.mVectorRoomActivity.cancelSelectionMode();
                    VectorRoomMediasSender.this.mVectorRoomActivity.hideWaitingView();
                }
            });
            return;
        }
        this.mVectorRoomActivity.cancelSelectionMode();
        this.mVectorRoomActivity.showWaitingView();
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("sendMedias : ");
        sb.append(this.mSharedDataItems.size());
        sb.append(" items to send");
        Log.d(str, sb.toString());
        mMediasSendingHandler.post(new Runnable() {
            public void run() {
                final RoomMediaMessage roomMediaMessage = (RoomMediaMessage) VectorRoomMediasSender.this.mSharedDataItems.get(0);
                String mimeType = roomMediaMessage.getMimeType(VectorRoomMediasSender.this.mVectorRoomActivity);
                if (mimeType == null) {
                    mimeType = "";
                }
                if (TextUtils.equals("text/vnd.android.intent", mimeType)) {
                    Log.d(VectorRoomMediasSender.LOG_TAG, "sendMedias :  unsupported mime type");
                    if (VectorRoomMediasSender.this.mSharedDataItems.size() > 0) {
                        VectorRoomMediasSender.this.mSharedDataItems.remove(0);
                    }
                    VectorRoomMediasSender.this.sendMedias();
                } else if (roomMediaMessage.getUri() == null && (TextUtils.equals("text/plain", mimeType) || TextUtils.equals("text/html", mimeType))) {
                    VectorRoomMediasSender.this.sendTextMessage(roomMediaMessage);
                } else if (roomMediaMessage.getUri() == null) {
                    Log.e(VectorRoomMediasSender.LOG_TAG, "sendMedias : null uri");
                    if (VectorRoomMediasSender.this.mSharedDataItems.size() > 0) {
                        VectorRoomMediasSender.this.mSharedDataItems.remove(0);
                    }
                    VectorRoomMediasSender.this.sendMedias();
                } else {
                    String fileName = roomMediaMessage.getFileName(VectorRoomMediasSender.this.mVectorRoomActivity);
                    Resource openResource = ResourceUtils.openResource(VectorRoomMediasSender.this.mVectorRoomActivity, roomMediaMessage.getUri(), roomMediaMessage.getMimeType(VectorRoomMediasSender.this.mVectorRoomActivity));
                    if (openResource == null) {
                        String access$400 = VectorRoomMediasSender.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("sendMedias : ");
                        sb.append(fileName);
                        sb.append(" is not found");
                        Log.e(access$400, sb.toString());
                        VectorRoomMediasSender.this.mVectorRoomActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(VectorRoomMediasSender.this.mVectorRoomActivity, VectorRoomMediasSender.this.mVectorRoomActivity.getString(R.string.room_message_file_not_found), 1).show();
                            }
                        });
                        if (VectorRoomMediasSender.this.mSharedDataItems.size() > 0) {
                            VectorRoomMediasSender.this.mSharedDataItems.remove(0);
                        }
                        VectorRoomMediasSender.this.sendMedias();
                    } else if (!mimeType.startsWith("image/") || (!ResourceUtils.MIME_TYPE_JPEG.equals(mimeType) && !ResourceUtils.MIME_TYPE_JPG.equals(mimeType) && !ResourceUtils.MIME_TYPE_IMAGE_ALL.equals(mimeType))) {
                        openResource.close();
                        VectorRoomMediasSender.this.mVectorRoomActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                VectorRoomMediasSender.this.mVectorMessageListFragment.sendMediaMessage(roomMediaMessage);
                            }
                        });
                        if (VectorRoomMediasSender.this.mSharedDataItems.size() > 0) {
                            VectorRoomMediasSender.this.mSharedDataItems.remove(0);
                        }
                        VectorRoomMediasSender.this.sendMedias();
                    } else {
                        VectorRoomMediasSender.this.sendJpegImage(roomMediaMessage, openResource);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void sendTextMessage(RoomMediaMessage roomMediaMessage) {
        final CharSequence text = roomMediaMessage.getText();
        final String htmlText = roomMediaMessage.getHtmlText();
        if (TextUtils.isEmpty(text) || htmlText != null) {
            final String str = null;
            if (text != null) {
                str = text.toString();
            } else if (htmlText != null) {
                str = Html.fromHtml(htmlText).toString();
            }
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("sendTextMessage ");
            sb.append(str);
            Log.d(str2, sb.toString());
            this.mVectorRoomActivity.runOnUiThread(new Runnable() {
                public void run() {
                    VectorRoomMediasSender.this.mVectorRoomActivity.sendMessage(str, htmlText, Message.FORMAT_MATRIX_HTML, false);
                }
            });
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    VectorRoomMediasSender.this.mVectorRoomActivity.insertTextInTextEditor(text.toString());
                }
            });
        }
        if (this.mSharedDataItems.size() > 0) {
            this.mSharedDataItems.remove(0);
        }
        sendMedias();
    }

    /* access modifiers changed from: private */
    public void sendJpegImage(final RoomMediaMessage roomMediaMessage, Resource resource) {
        final String mimeType = roomMediaMessage.getMimeType(this.mVectorRoomActivity);
        final String saveMedia = this.mMediasCache.saveMedia(resource.mContentStream, null, mimeType);
        resource.close();
        this.mVectorRoomActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (VectorRoomMediasSender.this.mSharedDataItems != null && VectorRoomMediasSender.this.mSharedDataItems.size() > 0) {
                    VectorRoomMediasSender.this.sendJpegImage(roomMediaMessage, saveMedia, mimeType, new OnImageUploadListener() {
                        public void onDone() {
                            if (VectorRoomMediasSender.this.mSharedDataItems != null && VectorRoomMediasSender.this.mSharedDataItems.size() > 0) {
                                VectorRoomMediasSender.this.mSharedDataItems.remove(0);
                            }
                            VectorRoomMediasSender.this.sendMedias();
                        }

                        public void onCancel() {
                            if (VectorRoomMediasSender.this.mSharedDataItems != null) {
                                VectorRoomMediasSender.this.mSharedDataItems.clear();
                            }
                            VectorRoomMediasSender.this.sendMedias();
                        }
                    });
                }
            }
        });
    }

    private ImageCompressionSizes computeImageSizes(int i, int i2) {
        ImageCompressionSizes imageCompressionSizes = new ImageCompressionSizes();
        imageCompressionSizes.mFullImageSize = new ImageSize(i, i2);
        int i3 = i2 > i ? i2 : i;
        if (i3 > 512) {
            if (i3 > 2048) {
                imageCompressionSizes.mLargeImageSize = imageCompressionSizes.mFullImageSize.computeSizeToFit(2048.0f);
                if (imageCompressionSizes.mLargeImageSize.mWidth == i && imageCompressionSizes.mLargeImageSize.mHeight == i2) {
                    imageCompressionSizes.mLargeImageSize = null;
                }
            }
            if (i3 > 1024) {
                imageCompressionSizes.mMediumImageSize = imageCompressionSizes.mFullImageSize.computeSizeToFit(1024.0f);
                if (imageCompressionSizes.mMediumImageSize.mWidth == i && imageCompressionSizes.mMediumImageSize.mHeight == i2) {
                    imageCompressionSizes.mMediumImageSize = null;
                }
            }
            if (i3 > 512) {
                imageCompressionSizes.mSmallImageSize = imageCompressionSizes.mFullImageSize.computeSizeToFit(512.0f);
                if (imageCompressionSizes.mSmallImageSize.mWidth == i && imageCompressionSizes.mSmallImageSize.mHeight == i2) {
                    imageCompressionSizes.mSmallImageSize = null;
                }
            }
        }
        return imageCompressionSizes;
    }

    private static int estimateFileSize(ImageSize imageSize) {
        if (imageSize != null) {
            return ((((imageSize.mWidth * imageSize.mHeight) * 2) / 10) / 1024) * 1024;
        }
        return 0;
    }

    private static void addDialogEntry(Context context, List<String> list, String str, ImageSize imageSize, int i) {
        if (imageSize != null && list != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(": ");
            sb.append(Formatter.formatFileSize(context, (long) i));
            sb.append(" (");
            sb.append(imageSize.mWidth);
            sb.append("x");
            sb.append(imageSize.mHeight);
            sb.append(")");
            list.add(sb.toString());
        }
    }

    private static String[] getImagesCompressionTextsList(Context context, ImageCompressionSizes imageCompressionSizes, int i) {
        ArrayList arrayList = new ArrayList();
        addDialogEntry(context, arrayList, context.getString(R.string.compression_opt_list_original), imageCompressionSizes.mFullImageSize, i);
        addDialogEntry(context, arrayList, context.getString(R.string.compression_opt_list_large), imageCompressionSizes.mLargeImageSize, Math.min(estimateFileSize(imageCompressionSizes.mLargeImageSize), i));
        addDialogEntry(context, arrayList, context.getString(R.string.compression_opt_list_medium), imageCompressionSizes.mMediumImageSize, Math.min(estimateFileSize(imageCompressionSizes.mMediumImageSize), i));
        addDialogEntry(context, arrayList, context.getString(R.string.compression_opt_list_small), imageCompressionSizes.mSmallImageSize, Math.min(estimateFileSize(imageCompressionSizes.mSmallImageSize), i));
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0059 A[Catch:{ Exception -> 0x0068 }] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x006c A[Catch:{ Exception -> 0x0068 }] */
    public String resizeImage(String str, String str2, ImageSize imageSize, ImageSize imageSize2, int i) {
        InputStream inputStream;
        if (imageSize2 != null) {
            try {
                try {
                    inputStream = ImageUtils.resizeImage(new FileInputStream(new File(str2)), -1, ((imageSize.mWidth + imageSize2.mWidth) - 1) / imageSize2.mWidth, 75);
                } catch (OutOfMemoryError e) {
                    String str3 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("resizeImage out of memory : ");
                    sb.append(e.getMessage());
                    Log.e(str3, sb.toString(), e);
                    inputStream = null;
                    if (inputStream != null) {
                    }
                    if (i != 0) {
                    }
                    return str;
                } catch (Exception e2) {
                    String str4 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("resizeImage failed : ");
                    sb2.append(e2.getMessage());
                    Log.e(str4, sb2.toString(), e2);
                    inputStream = null;
                    if (inputStream != null) {
                    }
                    if (i != 0) {
                    }
                    return str;
                }
                if (inputStream != null) {
                    String saveMedia = this.mMediasCache.saveMedia(inputStream, null, ResourceUtils.MIME_TYPE_JPEG);
                    if (saveMedia != null) {
                        str = saveMedia;
                    }
                    inputStream.close();
                }
            } catch (Exception e3) {
                String str5 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("resizeImage ");
                sb3.append(e3.getMessage());
                Log.e(str5, sb3.toString(), e3);
            }
        }
        if (i != 0) {
            ImageUtils.rotateImage(this.mVectorRoomActivity, str, i, this.mMediasCache);
        }
        return str;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0117  */
    /* JADX WARNING: Removed duplicated region for block: B:40:? A[RETURN, SYNTHETIC] */
    public void sendJpegImage(RoomMediaMessage roomMediaMessage, String str, String str2, OnImageUploadListener onImageUploadListener) {
        final RoomMediaMessage roomMediaMessage2 = roomMediaMessage;
        String str3 = str2;
        final OnImageUploadListener onImageUploadListener2 = onImageUploadListener;
        if (str != null && onImageUploadListener2 != null) {
            boolean z = false;
            if (ResourceUtils.MIME_TYPE_JPEG.equals(str3) || ResourceUtils.MIME_TYPE_JPG.equals(str3) || ResourceUtils.MIME_TYPE_IMAGE_ALL.equals(str3)) {
                System.gc();
                try {
                    Uri parse = Uri.parse(str);
                    final String path = parse.getPath();
                    final int rotationAngleForBitmap = ImageUtils.getRotationAngleForBitmap(this.mVectorRoomActivity, parse);
                    FileInputStream fileInputStream = new FileInputStream(new File(path));
                    int available = fileInputStream.available();
                    Options options = new Options();
                    options.inJustDecodeBounds = true;
                    options.inPreferredConfig = Config.ARGB_8888;
                    options.outWidth = -1;
                    options.outHeight = -1;
                    try {
                        BitmapFactory.decodeStream(fileInputStream, null, options);
                    } catch (OutOfMemoryError e) {
                        OutOfMemoryError outOfMemoryError = e;
                        String str4 = LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("sendImageMessage out of memory error : ");
                        sb.append(outOfMemoryError.getMessage());
                        Log.e(str4, sb.toString(), outOfMemoryError);
                    }
                    ImageCompressionSizes computeImageSizes = computeImageSizes(options.outWidth, options.outHeight);
                    fileInputStream.close();
                    if (this.mImageCompressionDescription != null) {
                        try {
                            String str5 = str;
                            String str6 = path;
                            final String resizeImage = resizeImage(str5, str6, computeImageSizes.mFullImageSize, computeImageSizes.getImageSize(this.mVectorRoomActivity, this.mImageCompressionDescription), rotationAngleForBitmap);
                            this.mVectorRoomActivity.runOnUiThread(new Runnable() {
                                public void run() {
                                    VectorRoomMediasSender.this.mVectorMessageListFragment.sendMediaMessage(new RoomMediaMessage(Uri.parse(resizeImage), roomMediaMessage2.getFileName(VectorRoomMediasSender.this.mVectorRoomActivity)));
                                    onImageUploadListener2.onDone();
                                }
                            });
                        } catch (Exception e2) {
                            e = e2;
                            z = true;
                            String str7 = LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("sendImageMessage failed ");
                            sb2.append(e.getMessage());
                            Log.e(str7, sb2.toString(), e);
                            if (!z) {
                            }
                        }
                    } else if (computeImageSizes.mSmallImageSize != null) {
                        ImageSizeSelectionDialogFragment imageSizeSelectionDialogFragment = (ImageSizeSelectionDialogFragment) this.mVectorRoomActivity.getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_IMAGE_SIZE_DIALOG);
                        if (imageSizeSelectionDialogFragment != null) {
                            imageSizeSelectionDialogFragment.dismissAllowingStateLoss();
                        }
                        String[] imagesCompressionTextsList = getImagesCompressionTextsList(this.mVectorRoomActivity, computeImageSizes, available);
                        Builder title = new Builder(this.mVectorRoomActivity).setTitle((int) R.string.compression_options);
                        final ImageCompressionSizes imageCompressionSizes = computeImageSizes;
                        final String str8 = str;
                        final RoomMediaMessage roomMediaMessage3 = roomMediaMessage;
                        AnonymousClass9 r0 = r1;
                        final OnImageUploadListener onImageUploadListener3 = onImageUploadListener;
                        AnonymousClass9 r1 = new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, final int i) {
                                VectorRoomMediasSender.this.mImageSizesListDialog.dismiss();
                                VectorRoomMediasSender.this.mVectorRoomActivity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        VectorRoomMediasSender.this.mVectorRoomActivity.showWaitingView();
                                        Thread thread = new Thread(new Runnable() {
                                            public void run() {
                                                ImageSize imageSize = i != 0 ? (ImageSize) imageCompressionSizes.getImageSizesList().get(i) : null;
                                                VectorRoomMediasSender.this.mImageCompressionDescription = (String) imageCompressionSizes.getImageSizesDescription(VectorRoomMediasSender.this.mVectorRoomActivity).get(i);
                                                final String access$1200 = VectorRoomMediasSender.this.resizeImage(str8, path, imageCompressionSizes.mFullImageSize, imageSize, rotationAngleForBitmap);
                                                VectorRoomMediasSender.this.mVectorRoomActivity.runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        VectorRoomMediasSender.this.mVectorMessageListFragment.sendMediaMessage(new RoomMediaMessage(Uri.parse(access$1200), roomMediaMessage3.getFileName(VectorRoomMediasSender.this.mVectorRoomActivity)));
                                                        onImageUploadListener3.onDone();
                                                    }
                                                });
                                            }
                                        });
                                        thread.setPriority(1);
                                        thread.start();
                                    }
                                });
                            }
                        };
                        this.mImageSizesListDialog = title.setSingleChoiceItems((CharSequence[]) imagesCompressionTextsList, -1, (OnClickListener) r0).setOnCancelListener(new OnCancelListener() {
                            public void onCancel(DialogInterface dialogInterface) {
                                VectorRoomMediasSender.this.mImageSizesListDialog = null;
                                OnImageUploadListener onImageUploadListener = onImageUploadListener2;
                                if (onImageUploadListener != null) {
                                    onImageUploadListener.onCancel();
                                }
                            }
                        }).show();
                    }
                    z = true;
                } catch (Exception e3) {
                    e = e3;
                    String str72 = LOG_TAG;
                    StringBuilder sb22 = new StringBuilder();
                    sb22.append("sendImageMessage failed ");
                    sb22.append(e.getMessage());
                    Log.e(str72, sb22.toString(), e);
                    if (!z) {
                    }
                }
            }
            if (!z) {
                this.mVectorRoomActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        VectorRoomMediasSender.this.mVectorMessageListFragment.sendMediaMessage(roomMediaMessage2);
                        OnImageUploadListener onImageUploadListener = onImageUploadListener2;
                        if (onImageUploadListener != null) {
                            onImageUploadListener.onDone();
                        }
                    }
                });
            }
        }
    }
}
