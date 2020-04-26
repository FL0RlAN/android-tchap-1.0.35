package org.matrix.androidsdk.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.Pair;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.R;
import org.matrix.androidsdk.core.ImageUtils;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.PermalinkUtils;
import org.matrix.androidsdk.core.ResourceUtils;
import org.matrix.androidsdk.core.ResourceUtils.Resource;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.MXEncryptedAttachments;
import org.matrix.androidsdk.crypto.MXEncryptedAttachments.EncryptionResult;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.listeners.MXMediaUploadListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.Event.SentState;
import org.matrix.androidsdk.rest.model.message.AudioMessage;
import org.matrix.androidsdk.rest.model.message.FileMessage;
import org.matrix.androidsdk.rest.model.message.ImageMessage;
import org.matrix.androidsdk.rest.model.message.MediaMessage;
import org.matrix.androidsdk.rest.model.message.Message;
import org.matrix.androidsdk.rest.model.message.RelatesTo;
import org.matrix.androidsdk.rest.model.message.VideoMessage;

class RoomMediaMessagesSender {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = RoomMediaMessagesSender.class.getSimpleName();
    private static Handler mEncodingHandler = null;
    private static Handler mEventHandler = null;
    /* access modifiers changed from: private */
    public static Handler mUiHandler = null;
    private static Pattern sPreviousReplyPattern = Pattern.compile("^<mx-reply>.*</mx-reply>", 32);
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final MXDataHandler mDataHandler;
    /* access modifiers changed from: private */
    public final List<RoomMediaMessage> mPendingRoomMediaMessages = new ArrayList();
    /* access modifiers changed from: private */
    public final Room mRoom;
    /* access modifiers changed from: private */
    public RoomMediaMessage mSendingRoomMediaMessage;

    RoomMediaMessagesSender(Context context, MXDataHandler mXDataHandler, Room room) {
        this.mRoom = room;
        this.mContext = context.getApplicationContext();
        this.mDataHandler = mXDataHandler;
        if (mUiHandler == null) {
            mUiHandler = new Handler(Looper.getMainLooper());
            HandlerThread handlerThread = new HandlerThread("RoomDataItemsSender_event", 1);
            handlerThread.start();
            mEventHandler = new Handler(handlerThread.getLooper());
            HandlerThread handlerThread2 = new HandlerThread("RoomDataItemsSender_encoding", 1);
            handlerThread2.start();
            mEncodingHandler = new Handler(handlerThread2.getLooper());
        }
    }

    /* access modifiers changed from: 0000 */
    public void send(final RoomMediaMessage roomMediaMessage) {
        mEventHandler.post(new Runnable() {
            public void run() {
                Message message;
                if (roomMediaMessage.getEvent() == null) {
                    String mimeType = roomMediaMessage.getMimeType(RoomMediaMessagesSender.this.mContext);
                    if (mimeType == null) {
                        mimeType = "";
                    }
                    if (roomMediaMessage.getUri() == null) {
                        message = RoomMediaMessagesSender.this.buildTextMessage(roomMediaMessage);
                    } else if (mimeType.startsWith("image/")) {
                        message = RoomMediaMessagesSender.this.buildImageMessage(roomMediaMessage);
                    } else if (mimeType.startsWith("video/")) {
                        message = RoomMediaMessagesSender.this.buildVideoMessage(roomMediaMessage);
                    } else {
                        message = RoomMediaMessagesSender.this.buildFileMessage(roomMediaMessage);
                    }
                    if (message == null) {
                        String access$500 = RoomMediaMessagesSender.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## send ");
                        sb.append(roomMediaMessage);
                        sb.append(" not supported");
                        Log.e(access$500, sb.toString());
                        RoomMediaMessagesSender.mUiHandler.post(new Runnable() {
                            public void run() {
                                RoomMediaMessage roomMediaMessage = roomMediaMessage;
                                StringBuilder sb = new StringBuilder();
                                sb.append("not supported ");
                                sb.append(roomMediaMessage);
                                roomMediaMessage.onEventCreationFailed(sb.toString());
                            }
                        });
                        return;
                    }
                    roomMediaMessage.setMessageType(message.msgtype);
                    if (roomMediaMessage.getReplyToEvent() != null) {
                        message.relatesTo = new RelatesTo();
                        message.relatesTo.dict = new HashMap();
                        message.relatesTo.dict.put("event_id", roomMediaMessage.getReplyToEvent().eventId);
                    }
                    roomMediaMessage.setEvent(new Event(message, RoomMediaMessagesSender.this.mDataHandler.getUserId(), RoomMediaMessagesSender.this.mRoom.getRoomId()));
                }
                RoomMediaMessagesSender.this.mDataHandler.updateEventState(roomMediaMessage.getEvent(), SentState.UNSENT);
                RoomMediaMessagesSender.this.mRoom.storeOutgoingEvent(roomMediaMessage.getEvent());
                RoomMediaMessagesSender.this.mDataHandler.getStore().commit();
                RoomMediaMessagesSender.mUiHandler.post(new Runnable() {
                    public void run() {
                        roomMediaMessage.onEventCreated();
                    }
                });
                synchronized (RoomMediaMessagesSender.LOG_TAG) {
                    if (!RoomMediaMessagesSender.this.mPendingRoomMediaMessages.contains(roomMediaMessage)) {
                        RoomMediaMessagesSender.this.mPendingRoomMediaMessages.add(roomMediaMessage);
                    }
                }
                RoomMediaMessagesSender.mUiHandler.post(new Runnable() {
                    public void run() {
                        RoomMediaMessagesSender.this.sendNext();
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public void skip() {
        synchronized (LOG_TAG) {
            this.mSendingRoomMediaMessage = null;
        }
        sendNext();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0028, code lost:
        if (uploadMedia(r1) == false) goto L_0x002b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002a, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002b, code lost:
        sendEvent(r1.getEvent());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0032, code lost:
        return;
     */
    public void sendNext() {
        synchronized (LOG_TAG) {
            if (this.mSendingRoomMediaMessage == null) {
                if (!this.mPendingRoomMediaMessages.isEmpty()) {
                    this.mSendingRoomMediaMessage = (RoomMediaMessage) this.mPendingRoomMediaMessages.get(0);
                    this.mPendingRoomMediaMessages.remove(0);
                    RoomMediaMessage roomMediaMessage = this.mSendingRoomMediaMessage;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void sendEvent(final Event event) {
        mUiHandler.post(new Runnable() {
            public void run() {
                RoomMediaMessagesSender.this.mRoom.sendEvent(event, new ApiCallback<Void>() {
                    private ApiCallback<Void> getCallback() {
                        ApiCallback<Void> sendingCallback;
                        synchronized (RoomMediaMessagesSender.LOG_TAG) {
                            sendingCallback = RoomMediaMessagesSender.this.mSendingRoomMediaMessage.getSendingCallback();
                            RoomMediaMessagesSender.this.mSendingRoomMediaMessage.setEventSendingCallback(null);
                            RoomMediaMessagesSender.this.mSendingRoomMediaMessage = null;
                        }
                        return sendingCallback;
                    }

                    public void onSuccess(Void voidR) {
                        ApiCallback callback = getCallback();
                        if (callback != null) {
                            try {
                                callback.onSuccess(null);
                            } catch (Exception e) {
                                String access$500 = RoomMediaMessagesSender.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## sendNext() failed ");
                                sb.append(e.getMessage());
                                Log.e(access$500, sb.toString(), e);
                            }
                        }
                        RoomMediaMessagesSender.this.sendNext();
                    }

                    public void onNetworkError(Exception exc) {
                        ApiCallback callback = getCallback();
                        if (callback != null) {
                            try {
                                callback.onNetworkError(exc);
                            } catch (Exception e) {
                                String access$500 = RoomMediaMessagesSender.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## sendNext() failed ");
                                sb.append(e.getMessage());
                                Log.e(access$500, sb.toString(), e);
                            }
                        }
                        RoomMediaMessagesSender.this.sendNext();
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        ApiCallback callback = getCallback();
                        if (callback != null) {
                            try {
                                callback.onMatrixError(matrixError);
                            } catch (Exception e) {
                                String access$500 = RoomMediaMessagesSender.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## sendNext() failed ");
                                sb.append(e.getMessage());
                                Log.e(access$500, sb.toString(), e);
                            }
                        }
                        RoomMediaMessagesSender.this.sendNext();
                    }

                    public void onUnexpectedError(Exception exc) {
                        ApiCallback callback = getCallback();
                        if (callback != null) {
                            try {
                                callback.onUnexpectedError(exc);
                            } catch (Exception e) {
                                String access$500 = RoomMediaMessagesSender.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## sendNext() failed ");
                                sb.append(e.getMessage());
                                Log.e(access$500, sb.toString(), e);
                            }
                        }
                        RoomMediaMessagesSender.this.sendNext();
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00d0, code lost:
        r13 = r9;
        r6 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00f6, code lost:
        r9 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0130, code lost:
        if (r9 == null) goto L_0x015d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x0138, code lost:
        if (android.text.TextUtils.isEmpty(r3.formatted_body) == false) goto L_0x013d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x013a, code lost:
        r15 = r3.body;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x013d, code lost:
        r15 = r3.formatted_body;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x013f, code lost:
        r7 = r14;
        r8 = r0;
        r10 = r6;
        r3.body = includeReplyToToBody(r8, r9, r10, r3.body, r1.equals(r4));
        r3.formatted_body = includeReplyToToFormattedBody(r8, r13, r10, r15, r1.equals(r4));
        r3.format = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x015d, code lost:
        r0 = LOG_TAG;
        r4 = new java.lang.StringBuilder();
        r4.append("Unsupported 'msgtype': ");
        r4.append(r1);
        r4.append(". Consider calling Room.canReplyTo(Event)");
        org.matrix.androidsdk.core.Log.e(r0, r4.toString());
        r15.setReplyToEvent(null);
     */
    public Message buildTextMessage(RoomMediaMessage roomMediaMessage) {
        String str;
        String str2;
        String str3;
        CharSequence text = roomMediaMessage.getText();
        String htmlText = roomMediaMessage.getHtmlText();
        String str4 = text == null ? htmlText != null ? Html.fromHtml(htmlText).toString() : null : text.toString();
        boolean isEmpty = TextUtils.isEmpty(str4);
        String str5 = Message.MSGTYPE_EMOTE;
        if (isEmpty && !TextUtils.equals(roomMediaMessage.getMessageType(), str5)) {
            return null;
        }
        Message message = new Message();
        String messageType = roomMediaMessage.getMessageType();
        String str6 = Message.MSGTYPE_TEXT;
        message.msgtype = messageType == null ? str6 : roomMediaMessage.getMessageType();
        message.body = str4;
        if (message.body == null) {
            message.body = "";
        }
        boolean isEmpty2 = TextUtils.isEmpty(htmlText);
        String str7 = Message.FORMAT_MATRIX_HTML;
        if (!isEmpty2) {
            message.formatted_body = htmlText;
            message.format = str7;
        }
        Event replyToEvent = roomMediaMessage.getReplyToEvent();
        if (replyToEvent != null) {
            String messageMsgType = JsonUtils.getMessageMsgType(replyToEvent.getContentAsJsonObject());
            if (messageMsgType != null) {
                char c = 65535;
                boolean z = true;
                switch (messageMsgType.hashCode()) {
                    case -1128764835:
                        if (messageMsgType.equals(Message.MSGTYPE_FILE)) {
                            c = 6;
                            break;
                        }
                        break;
                    case -1128351218:
                        if (messageMsgType.equals(str6)) {
                            c = 0;
                            break;
                        }
                        break;
                    case -636239083:
                        if (messageMsgType.equals(Message.MSGTYPE_AUDIO)) {
                            c = 5;
                            break;
                        }
                        break;
                    case -632772425:
                        if (messageMsgType.equals(str5)) {
                            c = 2;
                            break;
                        }
                        break;
                    case -629092198:
                        if (messageMsgType.equals(Message.MSGTYPE_IMAGE)) {
                            c = 3;
                            break;
                        }
                        break;
                    case -617202758:
                        if (messageMsgType.equals(Message.MSGTYPE_VIDEO)) {
                            c = 4;
                            break;
                        }
                        break;
                    case 2118539129:
                        if (messageMsgType.equals(Message.MSGTYPE_NOTICE)) {
                            c = 1;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case 1:
                    case 2:
                        Message message2 = JsonUtils.toMessage(replyToEvent.getContentAsJsonObject());
                        String str8 = message2.body;
                        if (TextUtils.isEmpty(message2.formatted_body)) {
                            str2 = message2.body;
                        } else {
                            str2 = message2.formatted_body;
                        }
                        if (message2.relatesTo == null || message2.relatesTo.dict == null || TextUtils.isEmpty((CharSequence) message2.relatesTo.dict.get("event_id"))) {
                            z = false;
                        }
                        String str9 = str2;
                        boolean z2 = z;
                        str = str8;
                        break;
                    case 3:
                        str3 = this.mContext.getString(R.string.reply_to_an_image);
                        break;
                    case 4:
                        str3 = this.mContext.getString(R.string.reply_to_a_video);
                        break;
                    case 5:
                        str3 = this.mContext.getString(R.string.reply_to_an_audio_file);
                        break;
                    case 6:
                        str3 = this.mContext.getString(R.string.reply_to_a_file);
                        break;
                    default:
                        String str10 = LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Reply to: unsupported msgtype: ");
                        sb.append(messageMsgType);
                        Log.w(str10, sb.toString());
                        str = null;
                        break;
                }
            } else {
                Log.e(LOG_TAG, "Null 'msgtype'. Consider calling Room.canReplyTo(Event)");
                roomMediaMessage.setReplyToEvent(null);
            }
        }
        return message;
    }

    private String includeReplyToToBody(Event event, String str, boolean z, String str2, boolean z2) {
        String str3 = "\n";
        String[] split = str.split(str3);
        String str4 = "> ";
        int i = 0;
        if (z) {
            while (i < split.length && split[i].startsWith(str4)) {
                i++;
            }
            if (i < split.length && split[i].isEmpty()) {
                i++;
            }
        }
        StringBuilder sb = new StringBuilder();
        if (i < split.length) {
            if (z2) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("* <");
                sb2.append(event.sender);
                sb2.append(str4);
                sb2.append(split[i]);
                split[i] = sb2.toString();
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("<");
                sb3.append(event.sender);
                sb3.append(str4);
                sb3.append(split[i]);
                split[i] = sb3.toString();
            }
            while (i < split.length) {
                sb.append(str4);
                sb.append(split[i]);
                sb.append(str3);
                i++;
            }
        }
        sb.append(str3);
        sb.append(str2);
        return sb.toString();
    }

    private String includeReplyToToFormattedBody(Event event, String str, boolean z, String str2, boolean z2) {
        if (z) {
            str = sPreviousReplyPattern.matcher(str).replaceAll("");
        }
        StringBuilder sb = new StringBuilder("<mx-reply><blockquote><a href=\"");
        sb.append(PermalinkUtils.createPermalink(event));
        String str3 = "\">";
        sb.append(str3);
        sb.append(this.mContext.getString(R.string.message_reply_to_prefix));
        sb.append("</a> ");
        if (z2) {
            sb.append("* ");
        }
        sb.append("<a href=\"");
        sb.append(PermalinkUtils.createPermalink(event.sender));
        sb.append(str3);
        sb.append(event.sender);
        sb.append("</a><br>");
        sb.append(str);
        sb.append("</blockquote></mx-reply>");
        sb.append(str2);
        return sb.toString();
    }

    private static String getThumbnailPath(String str) {
        if (!TextUtils.isEmpty(str)) {
            String str2 = ".jpg";
            if (str.endsWith(str2)) {
                return str.replace(str2, "_thumb.jpg");
            }
        }
        return null;
    }

    private Bitmap getMediaPickerThumbnail(RoomMediaMessage roomMediaMessage) {
        try {
            String thumbnailPath = getThumbnailPath(roomMediaMessage.getUri().getPath());
            if (thumbnailPath == null || !new File(thumbnailPath).exists()) {
                return null;
            }
            Options options = new Options();
            options.inPreferredConfig = Config.ARGB_8888;
            return BitmapFactory.decodeFile(thumbnailPath, options);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("cannot restore the media picker thumbnail ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            return null;
        } catch (OutOfMemoryError e2) {
            Log.e(LOG_TAG, "cannot restore the media picker thumbnail oom", e2);
            return null;
        }
    }

    private String getMediaUrl(RoomMediaMessage roomMediaMessage) {
        String uri = roomMediaMessage.getUri().toString();
        if (uri.startsWith("file:")) {
            return uri;
        }
        String mimeType = roomMediaMessage.getMimeType(this.mContext);
        Resource openResource = ResourceUtils.openResource(this.mContext, roomMediaMessage.getUri(), mimeType);
        String saveMedia = this.mDataHandler.getMediaCache().saveMedia(openResource.mContentStream, null, mimeType);
        openResource.close();
        return saveMedia;
    }

    /* access modifiers changed from: private */
    public Message buildImageMessage(RoomMediaMessage roomMediaMessage) {
        try {
            String mimeType = roomMediaMessage.getMimeType(this.mContext);
            MXMediaCache mediaCache = this.mDataHandler.getMediaCache();
            String mediaUrl = getMediaUrl(roomMediaMessage);
            Bitmap fullScreenImageKindThumbnail = roomMediaMessage.getFullScreenImageKindThumbnail(this.mContext);
            if (fullScreenImageKindThumbnail == null) {
                fullScreenImageKindThumbnail = getMediaPickerThumbnail(roomMediaMessage);
            }
            if (fullScreenImageKindThumbnail == null) {
                Pair thumbnailSize = roomMediaMessage.getThumbnailSize();
                fullScreenImageKindThumbnail = ResourceUtils.createThumbnailBitmap(this.mContext, roomMediaMessage.getUri(), ((Integer) thumbnailSize.first).intValue(), ((Integer) thumbnailSize.second).intValue());
            }
            if (fullScreenImageKindThumbnail == null) {
                fullScreenImageKindThumbnail = roomMediaMessage.getMiniKindImageThumbnail(this.mContext);
            }
            String saveBitmap = fullScreenImageKindThumbnail != null ? mediaCache.saveBitmap(fullScreenImageKindThumbnail, null) : null;
            int rotationAngleForBitmap = ImageUtils.getRotationAngleForBitmap(this.mContext, Uri.parse(mediaUrl));
            if (rotationAngleForBitmap != 0) {
                ImageUtils.rotateImage(this.mContext, saveBitmap, rotationAngleForBitmap, mediaCache);
            }
            ImageMessage imageMessage = new ImageMessage();
            imageMessage.url = mediaUrl;
            imageMessage.body = roomMediaMessage.getFileName(this.mContext);
            if (TextUtils.isEmpty(imageMessage.body)) {
                imageMessage.body = "Image";
            }
            Uri parse = Uri.parse(mediaUrl);
            if (imageMessage.info == null) {
                Room.fillImageInfo(this.mContext, imageMessage, parse, mimeType);
            }
            if (!(saveBitmap == null || imageMessage.info == null || imageMessage.info.thumbnailInfo != null)) {
                Room.fillThumbnailInfo(this.mContext, imageMessage, Uri.parse(saveBitmap), ResourceUtils.MIME_TYPE_JPEG);
                imageMessage.info.thumbnailUrl = saveBitmap;
            }
            return imageMessage;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## buildImageMessage() failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            return null;
        }
    }

    public String getVideoThumbnailUrl(String str) {
        try {
            return this.mDataHandler.getMediaCache().saveBitmap(ThumbnailUtils.createVideoThumbnail(Uri.parse(str).getPath(), 1), null);
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getVideoThumbnailUrl() failed with ");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString(), e);
            return null;
        }
    }

    /* access modifiers changed from: private */
    public Message buildVideoMessage(RoomMediaMessage roomMediaMessage) {
        try {
            String mediaUrl = getMediaUrl(roomMediaMessage);
            String videoThumbnailUrl = getVideoThumbnailUrl(mediaUrl);
            if (videoThumbnailUrl == null) {
                return buildFileMessage(roomMediaMessage);
            }
            VideoMessage videoMessage = new VideoMessage();
            videoMessage.url = mediaUrl;
            videoMessage.body = roomMediaMessage.getFileName(this.mContext);
            Uri parse = Uri.parse(mediaUrl);
            Room.fillVideoInfo(this.mContext, videoMessage, parse, roomMediaMessage.getMimeType(this.mContext), videoThumbnailUrl != null ? Uri.parse(videoThumbnailUrl) : null, ResourceUtils.MIME_TYPE_JPEG);
            if (videoMessage.body == null) {
                videoMessage.body = parse.getLastPathSegment();
            }
            return videoMessage;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## buildVideoMessage() failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            return null;
        }
    }

    /* access modifiers changed from: private */
    public Message buildFileMessage(RoomMediaMessage roomMediaMessage) {
        FileMessage fileMessage;
        try {
            String mimeType = roomMediaMessage.getMimeType(this.mContext);
            String mediaUrl = getMediaUrl(roomMediaMessage);
            if (mimeType.startsWith("audio/")) {
                fileMessage = new AudioMessage();
            } else {
                fileMessage = new FileMessage();
            }
            fileMessage.url = mediaUrl;
            fileMessage.body = roomMediaMessage.getFileName(this.mContext);
            Uri parse = Uri.parse(mediaUrl);
            Room.fillFileInfo(this.mContext, fileMessage, parse, mimeType);
            if (fileMessage.body == null) {
                fileMessage.body = parse.getLastPathSegment();
            }
            return fileMessage;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## buildFileMessage() failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            return null;
        }
    }

    /* access modifiers changed from: private */
    public boolean uploadMedia(RoomMediaMessage roomMediaMessage) {
        String url;
        String mimeType;
        final Event event = roomMediaMessage.getEvent();
        final Message message = JsonUtils.toMessage(event.getContent());
        if (!(message instanceof MediaMessage)) {
            return false;
        }
        final MediaMessage mediaMessage = (MediaMessage) message;
        if (mediaMessage.isThumbnailLocalContent()) {
            url = mediaMessage.getThumbnailUrl();
            mimeType = ResourceUtils.MIME_TYPE_JPEG;
        } else if (!mediaMessage.isLocalContent()) {
            return false;
        } else {
            url = mediaMessage.getUrl();
            mimeType = mediaMessage.getMimeType();
        }
        final String str = url;
        final String str2 = mimeType;
        Handler handler = mEncodingHandler;
        final RoomMediaMessage roomMediaMessage2 = roomMediaMessage;
        AnonymousClass3 r0 = new Runnable() {
            public void run() {
                final Uri uri;
                String str;
                String str2;
                FileInputStream fileInputStream;
                final EncryptionResult encryptionResult;
                String str3;
                final MXMediaCache mediaCache = RoomMediaMessagesSender.this.mDataHandler.getMediaCache();
                Uri parse = Uri.parse(str);
                String str4 = str2;
                try {
                    FileInputStream fileInputStream2 = new FileInputStream(new File(parse.getPath()));
                    if (!RoomMediaMessagesSender.this.mRoom.isEncrypted() || !RoomMediaMessagesSender.this.mDataHandler.isCryptoEnabled()) {
                        if (mediaMessage.isThumbnailLocalContent()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("thumb");
                            sb.append(message.body);
                            str3 = sb.toString();
                        } else {
                            str3 = message.body;
                        }
                        uri = null;
                        str = str4;
                        fileInputStream = fileInputStream2;
                        str2 = str3;
                        encryptionResult = null;
                    } else {
                        encryptionResult = MXEncryptedAttachments.encryptAttachment(fileInputStream2, str4);
                        fileInputStream2.close();
                        if (encryptionResult != null) {
                            Uri parse2 = Uri.parse(mediaCache.saveMedia(encryptionResult.mEncryptedStream, null, str2));
                            FileInputStream fileInputStream3 = new FileInputStream(new File(parse2.getPath()));
                            uri = parse2;
                            str2 = null;
                            str = "application/octet-stream";
                            fileInputStream = fileInputStream3;
                        } else {
                            RoomMediaMessagesSender.this.skip();
                            RoomMediaMessagesSender.mUiHandler.post(new Runnable() {
                                public void run() {
                                    RoomMediaMessagesSender.this.mDataHandler.updateEventState(roomMediaMessage2.getEvent(), SentState.UNDELIVERED);
                                    RoomMediaMessagesSender.this.mRoom.storeOutgoingEvent(roomMediaMessage2.getEvent());
                                    RoomMediaMessagesSender.this.mDataHandler.getStore().commit();
                                    roomMediaMessage2.onEncryptionFailed();
                                }
                            });
                            return;
                        }
                    }
                    RoomMediaMessagesSender.this.mDataHandler.updateEventState(roomMediaMessage2.getEvent(), SentState.SENDING);
                    mediaCache.uploadContent(fileInputStream, str2, str, str, new MXMediaUploadListener() {
                        public void onUploadStart(final String str) {
                            RoomMediaMessagesSender.mUiHandler.post(new Runnable() {
                                public void run() {
                                    if (roomMediaMessage2.getMediaUploadListener() != null) {
                                        roomMediaMessage2.getMediaUploadListener().onUploadStart(str);
                                    }
                                }
                            });
                        }

                        public void onUploadCancel(final String str) {
                            RoomMediaMessagesSender.mUiHandler.post(new Runnable() {
                                public void run() {
                                    RoomMediaMessagesSender.this.mDataHandler.updateEventState(roomMediaMessage2.getEvent(), SentState.UNDELIVERED);
                                    if (roomMediaMessage2.getMediaUploadListener() != null) {
                                        roomMediaMessage2.getMediaUploadListener().onUploadCancel(str);
                                        roomMediaMessage2.setMediaUploadListener(null);
                                        roomMediaMessage2.setEventSendingCallback(null);
                                    }
                                    RoomMediaMessagesSender.this.skip();
                                }
                            });
                        }

                        public void onUploadError(final String str, final int i, final String str2) {
                            RoomMediaMessagesSender.mUiHandler.post(new Runnable() {
                                public void run() {
                                    RoomMediaMessagesSender.this.mDataHandler.updateEventState(roomMediaMessage2.getEvent(), SentState.UNDELIVERED);
                                    if (roomMediaMessage2.getMediaUploadListener() != null) {
                                        roomMediaMessage2.getMediaUploadListener().onUploadError(str, i, str2);
                                        roomMediaMessage2.setMediaUploadListener(null);
                                        roomMediaMessage2.setEventSendingCallback(null);
                                    }
                                    RoomMediaMessagesSender.this.skip();
                                }
                            });
                        }

                        public void onUploadComplete(final String str, final String str2) {
                            RoomMediaMessagesSender.mUiHandler.post(new Runnable() {
                                public void run() {
                                    boolean isThumbnailLocalContent = mediaMessage.isThumbnailLocalContent();
                                    String str = "## cannot delete the uncompress media";
                                    if (isThumbnailLocalContent) {
                                        mediaMessage.setThumbnailUrl(encryptionResult, str2);
                                        if (encryptionResult != null) {
                                            mediaCache.saveFileMediaForUrl(str2, uri.toString(), -1, -1, ResourceUtils.MIME_TYPE_JPEG);
                                            try {
                                                new File(Uri.parse(str).getPath()).delete();
                                            } catch (Exception e) {
                                                Log.e(RoomMediaMessagesSender.LOG_TAG, str, e);
                                            }
                                        } else {
                                            Pair thumbnailSize = roomMediaMessage2.getThumbnailSize();
                                            mediaCache.saveFileMediaForUrl(str2, str, ((Integer) thumbnailSize.first).intValue(), ((Integer) thumbnailSize.second).intValue(), ResourceUtils.MIME_TYPE_JPEG);
                                        }
                                        event.updateContent(JsonUtils.toJson(message));
                                        RoomMediaMessagesSender.this.mDataHandler.getStore().flushRoomEvents(RoomMediaMessagesSender.this.mRoom.getRoomId());
                                        RoomMediaMessagesSender.this.uploadMedia(roomMediaMessage2);
                                    } else {
                                        if (uri != null) {
                                            mediaCache.saveFileMediaForUrl(str2, uri.toString(), mediaMessage.getMimeType());
                                            try {
                                                new File(Uri.parse(str).getPath()).delete();
                                            } catch (Exception e2) {
                                                Log.e(RoomMediaMessagesSender.LOG_TAG, str, e2);
                                            }
                                        } else {
                                            mediaCache.saveFileMediaForUrl(str2, str, mediaMessage.getMimeType());
                                        }
                                        mediaMessage.setUrl(encryptionResult, str2);
                                        event.updateContent(JsonUtils.toJson(message));
                                        RoomMediaMessagesSender.this.mDataHandler.getStore().flushRoomEvents(RoomMediaMessagesSender.this.mRoom.getRoomId());
                                        String access$500 = RoomMediaMessagesSender.LOG_TAG;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("Uploaded to ");
                                        sb.append(str2);
                                        Log.d(access$500, sb.toString());
                                        RoomMediaMessagesSender.this.sendEvent(event);
                                    }
                                    if (roomMediaMessage2.getMediaUploadListener() != null) {
                                        roomMediaMessage2.getMediaUploadListener().onUploadComplete(str, str2);
                                        if (!isThumbnailLocalContent) {
                                            roomMediaMessage2.setMediaUploadListener(null);
                                        }
                                    }
                                }
                            });
                        }
                    });
                } catch (Exception unused) {
                    RoomMediaMessagesSender.this.skip();
                }
            }
        };
        handler.post(r0);
        return true;
    }
}
