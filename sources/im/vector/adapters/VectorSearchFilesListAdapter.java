package im.vector.adapters;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.media.AntiVirusScanStatus;
import fr.gouv.tchap.model.MediaScan;
import fr.gouv.tchap.util.DinsicUtils;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.adapters.MessageRow;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.message.FileMessage;
import org.matrix.androidsdk.rest.model.message.ImageMessage;
import org.matrix.androidsdk.rest.model.message.Message;
import org.matrix.androidsdk.rest.model.message.VideoMessage;

public class VectorSearchFilesListAdapter extends VectorMessagesAdapter {
    private final boolean mDisplayRoomName;

    /* renamed from: im.vector.adapters.VectorSearchFilesListAdapter$1 reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$fr$gouv$tchap$media$AntiVirusScanStatus = new int[AntiVirusScanStatus.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            $SwitchMap$fr$gouv$tchap$media$AntiVirusScanStatus[AntiVirusScanStatus.IN_PROGRESS.ordinal()] = 1;
            $SwitchMap$fr$gouv$tchap$media$AntiVirusScanStatus[AntiVirusScanStatus.TRUSTED.ordinal()] = 2;
            try {
                $SwitchMap$fr$gouv$tchap$media$AntiVirusScanStatus[AntiVirusScanStatus.INFECTED.ordinal()] = 3;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean mergeView(Event event, int i, boolean z) {
        return false;
    }

    public VectorSearchFilesListAdapter(MXSession mXSession, Context context, boolean z, MXMediaCache mXMediaCache) {
        super(mXSession, context, mXMediaCache);
        this.mDisplayRoomName = z;
        setNotifyOnChange(true);
    }

    /* JADX WARNING: type inference failed for: r7v0 */
    /* JADX WARNING: type inference failed for: r15v0, types: [org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo] */
    /* JADX WARNING: type inference failed for: r7v1, types: [org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo] */
    /* JADX WARNING: type inference failed for: r5v2, types: [java.lang.Long] */
    /* JADX WARNING: type inference failed for: r10v2, types: [org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo] */
    /* JADX WARNING: type inference failed for: r5v9 */
    /* JADX WARNING: type inference failed for: r15v1 */
    /* JADX WARNING: type inference failed for: r7v9 */
    /* JADX WARNING: type inference failed for: r5v10 */
    /* JADX WARNING: type inference failed for: r5v12, types: [java.lang.Long] */
    /* JADX WARNING: type inference failed for: r5v15 */
    /* JADX WARNING: type inference failed for: r15v2 */
    /* JADX WARNING: type inference failed for: r9v9 */
    /* JADX WARNING: type inference failed for: r11v5, types: [org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo] */
    /* JADX WARNING: type inference failed for: r7v10 */
    /* JADX WARNING: type inference failed for: r15v3 */
    /* JADX WARNING: type inference failed for: r5v18 */
    /* JADX WARNING: type inference failed for: r7v11 */
    /* JADX WARNING: type inference failed for: r7v12, types: [org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo] */
    /* JADX WARNING: type inference failed for: r9v10 */
    /* JADX WARNING: type inference failed for: r9v12, types: [java.lang.Long] */
    /* JADX WARNING: type inference failed for: r8v20, types: [org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo] */
    /* JADX WARNING: type inference failed for: r10v6 */
    /* JADX WARNING: type inference failed for: r7v13 */
    /* JADX WARNING: type inference failed for: r15v4 */
    /* JADX WARNING: type inference failed for: r5v25 */
    /* JADX WARNING: type inference failed for: r7v14 */
    /* JADX WARNING: type inference failed for: r7v16, types: [java.lang.Long] */
    /* JADX WARNING: type inference failed for: r10v9 */
    /* JADX WARNING: type inference failed for: r10v11, types: [org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo] */
    /* JADX WARNING: type inference failed for: r10v12 */
    /* JADX WARNING: type inference failed for: r7v17 */
    /* JADX WARNING: type inference failed for: r5v28 */
    /* JADX WARNING: type inference failed for: r5v29 */
    /* JADX WARNING: type inference failed for: r7v18 */
    /* JADX WARNING: type inference failed for: r9v16 */
    /* JADX WARNING: type inference failed for: r7v19 */
    /* JADX WARNING: type inference failed for: r10v13 */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x0137, code lost:
        if (r6 != 3) goto L_0x0139;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x0160, code lost:
        if (r6 != 3) goto L_0x0139;
     */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r7v0
  assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], java.lang.Long, ?[OBJECT, ARRAY], org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo]
  uses: [?[OBJECT, ARRAY], ?[int, boolean, OBJECT, ARRAY, byte, short, char], org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo]
  mth insns count: 214
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0169  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x01a4  */
    /* JADX WARNING: Unknown variable types count: 15 */
    public View getView(int i, View view, ViewGroup viewGroup) {
        ? r15;
        String str;
        int i2;
        ? r7;
        String str2;
        ? r5;
        String str3;
        TextView textView;
        MediaScan mediaScan;
        MediaScan mediaScan2;
        ? r10;
        View inflate = view == null ? this.mLayoutInflater.inflate(R.layout.adapter_item_vector_search_file_by_name, viewGroup, false) : view;
        if (!this.mSession.isAlive()) {
            return inflate;
        }
        Event event = ((MessageRow) getItem(i)).getEvent();
        Message message = JsonUtils.toMessage(event.getContent());
        boolean equals = Message.MSGTYPE_IMAGE.equals(message.msgtype);
        int i3 = R.drawable.filetype_attachment;
        ? r72 = 0;
        if (equals) {
            ImageMessage imageMessage = JsonUtils.toImageMessage(event.getContent());
            str2 = imageMessage.getUrl();
            ? r8 = imageMessage.file;
            String thumbnailUrl = imageMessage.getThumbnailUrl();
            if (thumbnailUrl == null) {
                thumbnailUrl = str2;
                r10 = r8;
            } else {
                r10 = imageMessage.info != null ? imageMessage.info.thumbnail_file : 0;
            }
            if (imageMessage.info != null) {
                r72 = imageMessage.info.size;
            }
            str = thumbnailUrl;
            r15 = r10;
            i2 = "image/gif".equals(imageMessage.getMimeType()) ? R.drawable.filetype_gif : R.drawable.filetype_image;
            r5 = r72;
            r7 = r8;
        } else {
            if (Message.MSGTYPE_VIDEO.equals(message.msgtype)) {
                VideoMessage videoMessage = JsonUtils.toVideoMessage(event.getContent());
                str2 = videoMessage.getUrl();
                String thumbnailUrl2 = videoMessage.getThumbnailUrl();
                ? r9 = videoMessage.info != null ? videoMessage.info.size : 0;
                i2 = R.drawable.filetype_video;
                ? r11 = videoMessage.file;
                if (videoMessage.info != null) {
                    r72 = videoMessage.info.thumbnail_file;
                }
                r15 = r72;
                r5 = r9;
                r7 = r11;
                str = thumbnailUrl2;
            } else {
                boolean equals2 = Message.MSGTYPE_FILE.equals(message.msgtype);
                String str4 = Message.MSGTYPE_AUDIO;
                if (equals2 || str4.equals(message.msgtype)) {
                    FileMessage fileMessage = JsonUtils.toFileMessage(event.getContent());
                    String url = fileMessage.getUrl();
                    ? r102 = fileMessage.file;
                    ? r52 = fileMessage.info != null ? fileMessage.info.size : 0;
                    if (str4.equals(message.msgtype)) {
                        i3 = R.drawable.filetype_audio;
                    }
                    str = null;
                    r15 = 0;
                    r7 = r102;
                    i2 = i3;
                    str2 = url;
                    r5 = r52;
                } else {
                    r5 = 0;
                    str2 = null;
                    str = null;
                    r15 = 0;
                    i2 = R.drawable.filetype_attachment;
                    r7 = r72;
                }
            }
        }
        ImageView imageView = (ImageView) inflate.findViewById(R.id.file_search_thumbnail);
        int i4 = R.drawable.ic_notification_privacy_warning;
        imageView.setImageResource(R.drawable.ic_notification_privacy_warning);
        if (str2 != null) {
            AntiVirusScanStatus antiVirusScanStatus = AntiVirusScanStatus.UNKNOWN;
            if (this.mMediaScanManager != null) {
                if (r7 != 0) {
                    mediaScan2 = this.mMediaScanManager.scanEncryptedMedia(r7);
                } else {
                    mediaScan2 = this.mMediaScanManager.scanUnencryptedMedia(str2);
                }
                antiVirusScanStatus = mediaScan2.getAntiVirusScanStatus();
            }
            int i5 = AnonymousClass1.$SwitchMap$fr$gouv$tchap$media$AntiVirusScanStatus[antiVirusScanStatus.ordinal()];
            boolean z = true;
            if (i5 != 1) {
                if (i5 == 2) {
                    if (str != null) {
                        if (r15 != 0) {
                            mediaScan = this.mMediaScanManager.scanEncryptedMedia(r15);
                        } else {
                            mediaScan = this.mMediaScanManager.scanUnencryptedMedia(str);
                        }
                        int i6 = AnonymousClass1.$SwitchMap$fr$gouv$tchap$media$AntiVirusScanStatus[mediaScan.getAntiVirusScanStatus().ordinal()];
                        if (i6 != 1) {
                            if (i6 != 2) {
                            }
                        }
                    }
                    if (z) {
                        imageView.setImageResource(i2);
                        if (str != null) {
                            if (r15 == 0) {
                                this.mSession.getMediaCache().loadAvatarThumbnail(this.mSession.getHomeServerConfig(), imageView, str, getContext().getResources().getDimensionPixelSize(R.dimen.member_list_avatar_size));
                            } else {
                                this.mSession.getMediaCache().loadBitmap(this.mSession.getHomeServerConfig(), imageView, str, 0, 0, (String) null, (EncryptedFileInfo) r15);
                            }
                        }
                    } else {
                        imageView.setImageResource(i4);
                    }
                }
                i4 = R.drawable.tchap_danger;
                z = false;
                if (z) {
                }
            }
            i4 = R.drawable.tchap_scanning;
            z = false;
            if (z) {
            }
        }
        ((TextView) inflate.findViewById(R.id.file_search_filename)).setText(message.body);
        TextView textView2 = (TextView) inflate.findViewById(R.id.file_search_room_name);
        String str5 = "";
        if (this.mDisplayRoomName) {
            Room room = this.mSession.getDataHandler().getStore().getRoom(event.roomId);
            if (room != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(str5);
                sb.append(DinsicUtils.getRoomDisplayName(this.mContext, room));
                String sb2 = sb.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append(sb2);
                sb3.append(" - ");
                str3 = sb3.toString();
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str3);
                sb4.append(AdapterUtils.tsToString(this.mContext, event.getOriginServerTs(), false));
                textView2.setText(sb4.toString());
                textView = (TextView) inflate.findViewById(R.id.search_file_size);
                if (r5 != 0 || r5.longValue() <= 1) {
                    textView.setText(str5);
                } else {
                    textView.setText(Formatter.formatFileSize(this.mContext, r5.longValue()));
                }
                return inflate;
            }
        }
        str3 = str5;
        StringBuilder sb42 = new StringBuilder();
        sb42.append(str3);
        sb42.append(AdapterUtils.tsToString(this.mContext, event.getOriginServerTs(), false));
        textView2.setText(sb42.toString());
        textView = (TextView) inflate.findViewById(R.id.search_file_size);
        if (r5 != 0) {
        }
        textView.setText(str5);
        return inflate;
    }
}
