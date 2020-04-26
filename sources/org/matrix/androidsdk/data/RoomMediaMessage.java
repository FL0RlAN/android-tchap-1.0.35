package org.matrix.androidsdk.data;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.TextUtils;
import android.util.Pair;
import android.util.Patterns;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.matrix.androidsdk.core.FileUtilsKt;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.ResourceUtils;
import org.matrix.androidsdk.core.ResourceUtils.Resource;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.listeners.IMXMediaUploadListener;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.message.Message;

public class RoomMediaMessage implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public RoomMediaMessage createFromParcel(Parcel parcel) {
            return new RoomMediaMessage(parcel);
        }

        public RoomMediaMessage[] newArray(int i) {
            return new RoomMediaMessage[i];
        }
    };
    private static final String LOG_TAG = RoomMediaMessage.class.getSimpleName();
    private static final Uri mDummyUri = Uri.parse("http://www.matrixdummy.org");
    private Item mClipDataItem;
    private Event mEvent;
    private transient EventCreationListener mEventCreationListener;
    private transient ApiCallback<Void> mEventSendingCallback;
    private String mFileName;
    private transient IMXMediaUploadListener mMediaUploadListener;
    private String mMessageType;
    private String mMimeType;
    private Event mReplyToEvent;
    private Pair<Integer, Integer> mThumbnailSize;
    private Uri mUri;

    public interface EventCreationListener {
        void onEncryptionFailed(RoomMediaMessage roomMediaMessage);

        void onEventCreated(RoomMediaMessage roomMediaMessage);

        void onEventCreationFailed(RoomMediaMessage roomMediaMessage, String str);
    }

    public int describeContents() {
        return 0;
    }

    public RoomMediaMessage(Item item, String str) {
        Integer valueOf = Integer.valueOf(100);
        this.mThumbnailSize = new Pair<>(valueOf, valueOf);
        this.mClipDataItem = item;
        this.mMimeType = str;
    }

    public RoomMediaMessage(CharSequence charSequence, String str, String str2) {
        Integer valueOf = Integer.valueOf(100);
        this.mThumbnailSize = new Pair<>(valueOf, valueOf);
        this.mClipDataItem = new Item(charSequence, str);
        if (str == null) {
            str2 = "text/plain";
        }
        this.mMimeType = str2;
    }

    public RoomMediaMessage(Uri uri) {
        this(uri, (String) null);
    }

    public RoomMediaMessage(Uri uri, String str) {
        Integer valueOf = Integer.valueOf(100);
        this.mThumbnailSize = new Pair<>(valueOf, valueOf);
        this.mUri = uri;
        this.mFileName = str;
    }

    public RoomMediaMessage(Event event) {
        Integer valueOf = Integer.valueOf(100);
        this.mThumbnailSize = new Pair<>(valueOf, valueOf);
        setEvent(event);
        Message message = JsonUtils.toMessage(event.getContent());
        if (message != null) {
            setMessageType(message.msgtype);
        }
    }

    private RoomMediaMessage(Parcel parcel) {
        Integer valueOf = Integer.valueOf(100);
        this.mThumbnailSize = new Pair<>(valueOf, valueOf);
        this.mUri = unformatNullUri((Uri) parcel.readParcelable(Uri.class.getClassLoader()));
        this.mMimeType = unformatNullString(parcel.readString());
        String unformatNullString = unformatNullString(parcel.readString());
        String unformatNullString2 = unformatNullString(parcel.readString());
        Uri unformatNullUri = unformatNullUri((Uri) parcel.readParcelable(Uri.class.getClassLoader()));
        if (!TextUtils.isEmpty(unformatNullString) || !TextUtils.isEmpty(unformatNullString2) || unformatNullUri != null) {
            this.mClipDataItem = new Item(unformatNullString, unformatNullString2, null, unformatNullUri);
        }
        this.mFileName = unformatNullString(parcel.readString());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append("mUri ");
        sb.append(this.mUri);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append(" -- mMimeType ");
        sb3.append(this.mMimeType);
        String sb4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(sb4);
        sb5.append(" -- mEvent ");
        sb5.append(this.mEvent);
        String sb6 = sb5.toString();
        StringBuilder sb7 = new StringBuilder();
        sb7.append(sb6);
        sb7.append(" -- mClipDataItem ");
        sb7.append(this.mClipDataItem);
        String sb8 = sb7.toString();
        StringBuilder sb9 = new StringBuilder();
        sb9.append(sb8);
        sb9.append(" -- mFileName ");
        sb9.append(this.mFileName);
        String sb10 = sb9.toString();
        StringBuilder sb11 = new StringBuilder();
        sb11.append(sb10);
        sb11.append(" -- mMessageType ");
        sb11.append(this.mMessageType);
        String sb12 = sb11.toString();
        StringBuilder sb13 = new StringBuilder();
        sb13.append(sb12);
        sb13.append(" -- mThumbnailSize ");
        sb13.append(this.mThumbnailSize);
        return sb13.toString();
    }

    private static String unformatNullString(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return str;
    }

    private static Uri unformatNullUri(Uri uri) {
        if (uri == null || mDummyUri.equals(uri)) {
            return null;
        }
        return uri;
    }

    private static String formatNullString(String str) {
        return TextUtils.isEmpty(str) ? "" : str;
    }

    private static String formatNullString(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            return "";
        }
        return charSequence.toString();
    }

    private static Uri formatNullUri(Uri uri) {
        return uri == null ? mDummyUri : uri;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(formatNullUri(this.mUri), 0);
        parcel.writeString(formatNullString(this.mMimeType));
        Item item = this.mClipDataItem;
        if (item == null) {
            String str = "";
            parcel.writeString(str);
            parcel.writeString(str);
            parcel.writeParcelable(formatNullUri(null), 0);
        } else {
            parcel.writeString(formatNullString(item.getText()));
            parcel.writeString(formatNullString(this.mClipDataItem.getHtmlText()));
            parcel.writeParcelable(formatNullUri(this.mClipDataItem.getUri()), 0);
        }
        parcel.writeString(formatNullString(this.mFileName));
    }

    public void setMessageType(String str) {
        this.mMessageType = str;
    }

    public String getMessageType() {
        return this.mMessageType;
    }

    public void setReplyToEvent(Event event) {
        this.mReplyToEvent = event;
    }

    public Event getReplyToEvent() {
        return this.mReplyToEvent;
    }

    public void setEvent(Event event) {
        this.mEvent = event;
    }

    public Event getEvent() {
        return this.mEvent;
    }

    public void setThumbnailSize(Pair<Integer, Integer> pair) {
        this.mThumbnailSize = pair;
    }

    public Pair<Integer, Integer> getThumbnailSize() {
        return this.mThumbnailSize;
    }

    public void setMediaUploadListener(IMXMediaUploadListener iMXMediaUploadListener) {
        this.mMediaUploadListener = iMXMediaUploadListener;
    }

    public IMXMediaUploadListener getMediaUploadListener() {
        return this.mMediaUploadListener;
    }

    public void setEventSendingCallback(ApiCallback<Void> apiCallback) {
        this.mEventSendingCallback = apiCallback;
    }

    public ApiCallback<Void> getSendingCallback() {
        return this.mEventSendingCallback;
    }

    public void setEventCreationListener(EventCreationListener eventCreationListener) {
        this.mEventCreationListener = eventCreationListener;
    }

    public EventCreationListener getEventCreationListener() {
        return this.mEventCreationListener;
    }

    public CharSequence getText() {
        Item item = this.mClipDataItem;
        if (item != null) {
            return item.getText();
        }
        return null;
    }

    public String getHtmlText() {
        Item item = this.mClipDataItem;
        if (item != null) {
            return item.getHtmlText();
        }
        return null;
    }

    public Intent getIntent() {
        Item item = this.mClipDataItem;
        if (item != null) {
            return item.getIntent();
        }
        return null;
    }

    public Uri getUri() {
        Uri uri = this.mUri;
        if (uri != null) {
            return uri;
        }
        Item item = this.mClipDataItem;
        if (item != null) {
            return item.getUri();
        }
        return null;
    }

    public String getMimeType(Context context) {
        if (this.mMimeType == null && getUri() != null) {
            try {
                Uri uri = getUri();
                this.mMimeType = context.getContentResolver().getType(uri);
                if (this.mMimeType == null) {
                    String fileExtension = FileUtilsKt.getFileExtension(uri.toString());
                    if (fileExtension != null) {
                        this.mMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                    }
                }
                if (this.mMimeType != null) {
                    this.mMimeType = this.mMimeType.toLowerCase();
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Failed to open resource input stream", e);
            }
        }
        return this.mMimeType;
    }

    public Bitmap getMiniKindImageThumbnail(Context context) {
        return getImageThumbnail(context, 1);
    }

    public Bitmap getFullScreenImageKindThumbnail(Context context) {
        return getImageThumbnail(context, 2);
    }

    private Bitmap getImageThumbnail(Context context, int i) {
        Long l;
        if (getMimeType(context) == null || !getMimeType(context).startsWith("image/")) {
            return null;
        }
        try {
            ContentResolver contentResolver = context.getContentResolver();
            List pathSegments = getUri().getPathSegments();
            String str = (String) pathSegments.get(pathSegments.size() - 1);
            if (str.startsWith("image:")) {
                str = str.substring(6);
            }
            try {
                l = Long.valueOf(Long.parseLong(str));
            } catch (Exception unused) {
                l = null;
            }
            if (l != null) {
                return Thumbnails.getThumbnail(contentResolver, l.longValue(), i, null);
            }
            return null;
        } catch (Exception e) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("MediaStore.Images.Thumbnails.getThumbnail ");
            sb.append(e.getMessage());
            Log.e(str2, sb.toString(), e);
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0041, code lost:
        if (r9 != null) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        r9.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0066, code lost:
        if (r9 != null) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x006f, code lost:
        if (android.text.TextUtils.isEmpty(r8.mFileName) == false) goto L_0x00a0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0071, code lost:
        r9 = r0.getPathSegments();
        r8.mFileName = (java.lang.String) r9.get(r9.size() - 1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x009e, code lost:
        r8.mFileName = null;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0087 A[Catch:{ Exception -> 0x009e }] */
    public String getFileName(Context context) {
        Cursor cursor;
        if (this.mFileName == null && getUri() != null) {
            Uri uri = getUri();
            if (uri != null) {
                if (uri.toString().startsWith("content://")) {
                    try {
                        cursor = context.getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null) {
                            try {
                                if (cursor.moveToFirst()) {
                                    this.mFileName = cursor.getString(cursor.getColumnIndex("_display_name"));
                                }
                            } catch (Exception e) {
                                e = e;
                                try {
                                    String str = LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("cursor.getString ");
                                    sb.append(e.getMessage());
                                    Log.e(str, sb.toString(), e);
                                } catch (Throwable th) {
                                    th = th;
                                    if (cursor != null) {
                                        cursor.close();
                                    }
                                    throw th;
                                }
                            }
                        }
                    } catch (Exception e2) {
                        e = e2;
                        cursor = null;
                        String str2 = LOG_TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("cursor.getString ");
                        sb2.append(e.getMessage());
                        Log.e(str2, sb2.toString(), e);
                    } catch (Throwable th2) {
                        th = th2;
                        cursor = null;
                        if (cursor != null) {
                        }
                        throw th;
                    }
                } else if (uri.toString().startsWith(Message.FILE_SCHEME)) {
                    this.mFileName = uri.getLastPathSegment();
                }
            }
        }
        return this.mFileName;
    }

    public void saveMedia(Context context, File file) {
        this.mFileName = null;
        Uri uri = getUri();
        if (uri != null) {
            try {
                Resource openResource = ResourceUtils.openResource(context, uri, getMimeType(context));
                if (openResource == null) {
                    String str = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## saveMedia : Fail to retrieve the resource ");
                    sb.append(uri);
                    Log.e(str, sb.toString());
                    return;
                }
                this.mUri = saveFile(file, openResource.mContentStream, getFileName(context), openResource.mMimeType);
                openResource.mContentStream.close();
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## saveMedia : failed ");
                sb2.append(e.getMessage());
                Log.e(str2, sb2.toString(), e);
            }
        }
    }

    private static Uri saveFile(File file, InputStream inputStream, String str, String str2) {
        String str3 = "## saveFile failed ";
        if (str == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("file");
            sb.append(System.currentTimeMillis());
            str = sb.toString();
            if (str2 != null) {
                String extensionFromMimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(str2);
                if (extensionFromMimeType != null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(".");
                    sb2.append(extensionFromMimeType);
                    str = sb2.toString();
                }
            }
        }
        try {
            File file2 = new File(file, str);
            if (file2.exists()) {
                file2.delete();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file2.getPath());
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
                String str4 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append(e.getMessage());
                Log.e(str4, sb3.toString(), e);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
            return Uri.fromFile(file2);
        } catch (Exception e2) {
            String str5 = LOG_TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str3);
            sb4.append(e2.getMessage());
            Log.e(str5, sb4.toString(), e2);
            return null;
        }
    }

    /* access modifiers changed from: 0000 */
    public void onEventCreated() {
        if (getEventCreationListener() != null) {
            try {
                getEventCreationListener().onEventCreated(this);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## onEventCreated() failed : ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
        this.mEventCreationListener = null;
    }

    /* access modifiers changed from: 0000 */
    public void onEventCreationFailed(String str) {
        if (getEventCreationListener() != null) {
            try {
                getEventCreationListener().onEventCreationFailed(this, str);
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## onEventCreationFailed() failed : ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
        this.mMediaUploadListener = null;
        this.mEventSendingCallback = null;
        this.mEventCreationListener = null;
    }

    /* access modifiers changed from: 0000 */
    public void onEncryptionFailed() {
        if (getEventCreationListener() != null) {
            try {
                getEventCreationListener().onEncryptionFailed(this);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## onEncryptionFailed() failed : ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
        this.mMediaUploadListener = null;
        this.mEventSendingCallback = null;
        this.mEventCreationListener = null;
    }

    public static List<RoomMediaMessage> listRoomMediaMessages(Intent intent) {
        return listRoomMediaMessages(intent, null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00bd, code lost:
        if (((java.lang.String) r8.get(0)).endsWith("/*") != false) goto L_0x00bf;
     */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00c7  */
    public static List<RoomMediaMessage> listRoomMediaMessages(Intent intent, ClassLoader classLoader) {
        List list;
        int itemCount;
        int i;
        String str;
        ArrayList arrayList = new ArrayList();
        if (intent != null) {
            if (TextUtils.equals(intent.getType(), "text/plain")) {
                String str2 = "android.intent.extra.TEXT";
                String stringExtra = intent.getStringExtra(str2);
                if (stringExtra == null) {
                    CharSequence charSequenceExtra = intent.getCharSequenceExtra(str2);
                    if (charSequenceExtra != null) {
                        stringExtra = charSequenceExtra.toString();
                    }
                }
                String stringExtra2 = intent.getStringExtra("android.intent.extra.SUBJECT");
                if (!TextUtils.isEmpty(stringExtra2)) {
                    if (TextUtils.isEmpty(stringExtra)) {
                        stringExtra = stringExtra2;
                    } else if (Patterns.WEB_URL.matcher(stringExtra).matches()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(stringExtra2);
                        sb.append("\n");
                        sb.append(stringExtra);
                        stringExtra = sb.toString();
                    }
                }
                if (!TextUtils.isEmpty(stringExtra)) {
                    arrayList.add(new RoomMediaMessage(stringExtra, null, intent.getType()));
                    return arrayList;
                }
            }
            ClipData clipData = VERSION.SDK_INT >= 18 ? intent.getClipData() : null;
            if (clipData != null) {
                if (!(clipData.getDescription() == null || clipData.getDescription().getMimeTypeCount() == 0)) {
                    list = new ArrayList();
                    for (int i2 = 0; i2 < clipData.getDescription().getMimeTypeCount(); i2++) {
                        list.add(clipData.getDescription().getMimeType(i2));
                    }
                    if (1 == list.size()) {
                    }
                    itemCount = clipData.getItemCount();
                    for (i = 0; i < itemCount; i++) {
                        Item itemAt = clipData.getItemAt(i);
                        if (list != null) {
                            if (i < list.size()) {
                                str = (String) list.get(i);
                            } else {
                                str = (String) list.get(0);
                            }
                            if (!TextUtils.equals(str, "text/uri-list")) {
                                arrayList.add(new RoomMediaMessage(itemAt, str));
                            }
                        }
                        str = null;
                        arrayList.add(new RoomMediaMessage(itemAt, str));
                    }
                }
                list = null;
                itemCount = clipData.getItemCount();
                while (i < itemCount) {
                }
            } else if (intent.getData() != null) {
                arrayList.add(new RoomMediaMessage(intent.getData()));
            } else {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    extras.setClassLoader(RoomMediaMessage.class.getClassLoader());
                    String str3 = "android.intent.extra.STREAM";
                    if (extras.containsKey(str3)) {
                        try {
                            Object obj = extras.get(str3);
                            if (obj instanceof Uri) {
                                arrayList.add(new RoomMediaMessage((Uri) obj));
                            } else if (obj instanceof List) {
                                for (Object next : (List) obj) {
                                    if (next instanceof Uri) {
                                        arrayList.add(new RoomMediaMessage((Uri) next));
                                    } else if (next instanceof RoomMediaMessage) {
                                        arrayList.add((RoomMediaMessage) next);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "fail to extract the extra stream", e);
                        }
                    }
                }
            }
        }
        return arrayList;
    }
}
