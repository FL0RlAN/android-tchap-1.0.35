package im.vector.notifications;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.widget.ImageView;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.Matrix;
import im.vector.VectorApp;
import im.vector.activity.LockScreenActivity;
import im.vector.util.RiotEventDisplay;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.User;

public class RoomsNotifications implements Parcelable {
    public static final Creator<RoomsNotifications> CREATOR = new Creator<RoomsNotifications>() {
        public RoomsNotifications createFromParcel(Parcel parcel) {
            RoomsNotifications roomsNotifications = new RoomsNotifications();
            roomsNotifications.init(parcel);
            return roomsNotifications;
        }

        public RoomsNotifications[] newArray(int i) {
            return new RoomsNotifications[i];
        }
    };
    private static final String LOG_TAG = RoomsNotifications.class.getSimpleName();
    static final int MAX_NUMBER_NOTIFICATION_LINES = 10;
    private static final String ROOMS_NOTIFICATIONS_FILE_NAME = "ROOMS_NOTIFICATIONS_FILE_NAME";
    String mContentText;
    String mContentTitle;
    long mContentTs;
    private Context mContext;
    private Event mEvent;
    private NotifiedEvent mEventToNotify;
    boolean mIsInvitationEvent = false;
    private Map<String, List<NotifiedEvent>> mNotifiedEventsByRoomId;
    String mQuickReplyBody;
    List<CharSequence> mReversedMessagesList;
    private Room mRoom;
    String mRoomAvatarPath;
    String mRoomId;
    List<RoomNotifications> mRoomNotifications;
    String mSenderName;
    private MXSession mSession;
    String mSessionId;
    String mSummaryText;
    String mWearableMessage;

    public int describeContents() {
        return 0;
    }

    public RoomsNotifications() {
        String str = "";
        this.mSessionId = str;
        this.mRoomId = str;
        this.mSummaryText = str;
        this.mQuickReplyBody = str;
        this.mWearableMessage = str;
        this.mRoomAvatarPath = str;
        this.mContentTs = -1;
        this.mContentTitle = str;
        this.mContentText = str;
        this.mSenderName = str;
        this.mRoomNotifications = new ArrayList();
        this.mReversedMessagesList = new ArrayList();
    }

    public RoomsNotifications(NotifiedEvent notifiedEvent, Map<String, List<NotifiedEvent>> map) {
        String str = "";
        this.mSessionId = str;
        this.mRoomId = str;
        this.mSummaryText = str;
        this.mQuickReplyBody = str;
        this.mWearableMessage = str;
        this.mRoomAvatarPath = str;
        this.mContentTs = -1;
        this.mContentTitle = str;
        this.mContentText = str;
        this.mSenderName = str;
        this.mRoomNotifications = new ArrayList();
        this.mReversedMessagesList = new ArrayList();
        this.mContext = VectorApp.getInstance();
        this.mSession = Matrix.getInstance(this.mContext).getDefaultSession();
        IMXStore store = this.mSession.getDataHandler().getStore();
        this.mEventToNotify = notifiedEvent;
        this.mNotifiedEventsByRoomId = map;
        this.mSessionId = this.mSession.getMyUserId();
        this.mRoomId = notifiedEvent.mRoomId;
        this.mRoom = store.getRoom(this.mEventToNotify.mRoomId);
        this.mEvent = store.getEvent(this.mEventToNotify.mEventId, this.mEventToNotify.mRoomId);
        if (this.mRoom == null || this.mEvent == null) {
            if (this.mRoom == null) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## RoomsNotifications() : null room ");
                sb.append(this.mEventToNotify.mRoomId);
                Log.e(str2, sb.toString());
            } else {
                String str3 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## RoomsNotifications() : null event ");
                sb2.append(this.mEventToNotify.mEventId);
                sb2.append(" ");
                sb2.append(this.mEventToNotify.mRoomId);
                Log.e(str3, sb2.toString());
            }
            return;
        }
        this.mIsInvitationEvent = false;
        RiotEventDisplay riotEventDisplay = new RiotEventDisplay(this.mContext);
        boolean z = true;
        riotEventDisplay.setPrependMessagesWithAuthor(true);
        CharSequence textualDisplay = riotEventDisplay.getTextualDisplay(this.mEvent, this.mRoom.getState());
        if (!TextUtils.isEmpty(textualDisplay)) {
            str = textualDisplay.toString();
        }
        if ("m.room.member".equals(this.mEvent.getType())) {
            try {
                this.mIsInvitationEvent = "invite".equals(this.mEvent.getContentAsJsonObject().getAsJsonPrimitive("membership").getAsString());
            } catch (Exception e) {
                Log.e(LOG_TAG, "RoomsNotifications : invitation parsing failed", e);
            }
        }
        if (!this.mIsInvitationEvent) {
            int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.profile_avatar_size);
            String roomAvatarUrl = DinsicUtils.getRoomAvatarUrl(this.mRoom);
            File thumbnailCacheFile = this.mSession.getMediaCache().thumbnailCacheFile(roomAvatarUrl, dimensionPixelSize);
            if (thumbnailCacheFile != null) {
                this.mRoomAvatarPath = thumbnailCacheFile.getPath();
            } else {
                this.mSession.getMediaCache().loadAvatarThumbnail(this.mSession.getHomeServerConfig(), new ImageView(this.mContext), roomAvatarUrl, dimensionPixelSize);
            }
        }
        String roomName = getRoomName(this.mContext, this.mSession, this.mRoom, this.mEvent);
        this.mContentTs = this.mEvent.getOriginServerTs();
        this.mContentTitle = roomName;
        this.mContentText = str;
        RoomMember member = this.mRoom.getMember(this.mEvent.getSender());
        this.mSenderName = member == null ? this.mEvent.getSender() : member.getName();
        if (this.mNotifiedEventsByRoomId.size() != 1) {
            z = false;
        }
        if (z) {
            initSingleRoom();
        } else {
            initMultiRooms();
        }
    }

    private void initSingleRoom() {
        RoomNotifications roomNotifications = new RoomNotifications();
        this.mRoomNotifications.add(roomNotifications);
        roomNotifications.mRoomId = this.mEvent.roomId;
        roomNotifications.mRoomName = this.mContentTitle;
        List<NotifiedEvent> list = (List) this.mNotifiedEventsByRoomId.get(roomNotifications.mRoomId);
        int size = list.size();
        Collections.reverse(list);
        if (list.size() > 10) {
            list = list.subList(0, 10);
        }
        IMXStore store = this.mSession.getDataHandler().getStore();
        for (NotifiedEvent notifiedEvent : list) {
            Event event = store.getEvent(notifiedEvent.mEventId, notifiedEvent.mRoomId);
            RiotEventDisplay riotEventDisplay = new RiotEventDisplay(this.mContext);
            riotEventDisplay.setPrependMessagesWithAuthor(true);
            CharSequence textualDisplay = riotEventDisplay.getTextualDisplay(event, this.mRoom.getState());
            if (!TextUtils.isEmpty(textualDisplay)) {
                this.mReversedMessagesList.add(textualDisplay);
            }
        }
        int size2 = list.size();
        if (size > 10) {
            this.mSummaryText = this.mContext.getResources().getQuantityString(R.plurals.notification_unread_notified_messages, size, new Object[]{Integer.valueOf(size)});
        }
        if (!LockScreenActivity.isDisplayingALockScreenActivity() && !this.mIsInvitationEvent) {
            Event event2 = store.getEvent(this.mEventToNotify.mEventId, this.mEventToNotify.mRoomId);
            RoomMember member = this.mRoom.getMember(event2.getSender());
            roomNotifications.mSenderName = member == null ? event2.getSender() : member.getName();
            RiotEventDisplay riotEventDisplay2 = new RiotEventDisplay(this.mContext);
            riotEventDisplay2.setPrependMessagesWithAuthor(false);
            CharSequence textualDisplay2 = riotEventDisplay2.getTextualDisplay(event2, this.mRoom.getState());
            this.mQuickReplyBody = !TextUtils.isEmpty(textualDisplay2) ? textualDisplay2.toString() : "";
        }
        initWearableMessage(this.mContext, this.mRoom, store.getEvent(((NotifiedEvent) list.get(list.size() - 1)).mEventId, roomNotifications.mRoomId), this.mIsInvitationEvent);
    }

    private void initMultiRooms() {
        String str;
        IMXStore store = this.mSession.getDataHandler().getStore();
        int i = 0;
        int i2 = 0;
        for (String str2 : this.mNotifiedEventsByRoomId.keySet()) {
            Room room = this.mSession.getDataHandler().getRoom(str2);
            String roomName = getRoomName(this.mContext, this.mSession, room, null);
            List list = (List) this.mNotifiedEventsByRoomId.get(str2);
            Event event = store.getEvent(((NotifiedEvent) list.get(list.size() - 1)).mEventId, str2);
            RiotEventDisplay riotEventDisplay = new RiotEventDisplay(this.mContext);
            riotEventDisplay.setPrependMessagesWithAuthor(false);
            String str3 = "";
            String str4 = ": ";
            if (room.isInvited()) {
                StringBuilder sb = new StringBuilder();
                sb.append(roomName);
                sb.append(str4);
                str = sb.toString();
                CharSequence textualDisplay = riotEventDisplay.getTextualDisplay(event, room.getState());
                if (!TextUtils.isEmpty(textualDisplay)) {
                    str3 = textualDisplay.toString();
                }
            } else if (1 == list.size()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(roomName);
                sb2.append(str4);
                sb2.append(room.getState().getMemberName(event.getSender()));
                sb2.append(" ");
                str = sb2.toString();
                CharSequence textualDisplay2 = riotEventDisplay.getTextualDisplay(event, room.getState());
                if (!TextUtils.isEmpty(textualDisplay2)) {
                    str3 = textualDisplay2.toString();
                }
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(roomName);
                sb3.append(str4);
                String sb4 = sb3.toString();
                str3 = this.mContext.getResources().getQuantityString(R.plurals.notification_unread_notified_messages, list.size(), new Object[]{Integer.valueOf(list.size())});
                str = sb4;
            }
            if (!TextUtils.isEmpty(str3)) {
                RoomNotifications roomNotifications = new RoomNotifications();
                this.mRoomNotifications.add(roomNotifications);
                roomNotifications.mRoomId = str2;
                roomNotifications.mLatestEventTs = event.getOriginServerTs();
                roomNotifications.mMessageHeader = str;
                StringBuilder sb5 = new StringBuilder();
                sb5.append(str);
                sb5.append(str3);
                roomNotifications.mMessagesSummary = sb5.toString();
                i += list.size();
                i2++;
            }
        }
        Collections.sort(this.mRoomNotifications, RoomNotifications.mRoomNotificationsComparator);
        if (this.mRoomNotifications.size() > 10) {
            this.mRoomNotifications = this.mRoomNotifications.subList(0, 10);
        }
        Context context = this.mContext;
        this.mSummaryText = context.getString(R.string.notification_unread_notified_messages_in_room, new Object[]{context.getResources().getQuantityString(R.plurals.notification_unread_notified_messages_in_room_msgs, i, new Object[]{Integer.valueOf(i)}), this.mContext.getResources().getQuantityString(R.plurals.notification_unread_notified_messages_in_room_rooms, i2, new Object[]{Integer.valueOf(i2)})});
    }

    private void initWearableMessage(Context context, Room room, Event event, boolean z) {
        if (!z && event != null && room != null) {
            String roomName = getRoomName(context, Matrix.getInstance(context).getDefaultSession(), room, null);
            RiotEventDisplay riotEventDisplay = new RiotEventDisplay(context);
            riotEventDisplay.setPrependMessagesWithAuthor(false);
            StringBuilder sb = new StringBuilder();
            sb.append(roomName);
            sb.append(": ");
            sb.append(room.getState().getMemberName(event.getSender()));
            sb.append(" ");
            this.mWearableMessage = sb.toString();
            CharSequence textualDisplay = riotEventDisplay.getTextualDisplay(event, room.getState());
            if (!TextUtils.isEmpty(textualDisplay)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(this.mWearableMessage);
                sb2.append(textualDisplay.toString());
                this.mWearableMessage = sb2.toString();
            }
        }
    }

    public int getTotalMessages() {
        Map<String, List<NotifiedEvent>> map = this.mNotifiedEventsByRoomId;
        int i = 0;
        if (map == null) {
            return 0;
        }
        for (List size : map.values()) {
            i += size.size();
        }
        return i;
    }

    public int getNumberOfRoomsWithMessages() {
        Map<String, List<NotifiedEvent>> map = this.mNotifiedEventsByRoomId;
        if (map == null) {
            return 0;
        }
        return map.size();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mSessionId);
        parcel.writeString(this.mRoomId);
        parcel.writeString(this.mSummaryText);
        parcel.writeString(this.mQuickReplyBody);
        parcel.writeString(this.mWearableMessage);
        parcel.writeInt(this.mIsInvitationEvent ? 1 : 0);
        parcel.writeString(this.mRoomAvatarPath);
        parcel.writeLong(this.mContentTs);
        parcel.writeString(this.mContentTitle);
        parcel.writeString(this.mContentText);
        parcel.writeString(this.mSenderName);
        RoomNotifications[] roomNotificationsArr = new RoomNotifications[this.mRoomNotifications.size()];
        this.mRoomNotifications.toArray(roomNotificationsArr);
        parcel.writeArray(roomNotificationsArr);
        parcel.writeInt(this.mReversedMessagesList.size());
        for (CharSequence writeToParcel : this.mReversedMessagesList) {
            TextUtils.writeToParcel(writeToParcel, parcel, 0);
        }
    }

    /* access modifiers changed from: private */
    public void init(Parcel parcel) {
        this.mSessionId = parcel.readString();
        this.mRoomId = parcel.readString();
        this.mSummaryText = parcel.readString();
        this.mQuickReplyBody = parcel.readString();
        this.mWearableMessage = parcel.readString();
        boolean z = true;
        if (1 != parcel.readInt()) {
            z = false;
        }
        this.mIsInvitationEvent = z;
        this.mRoomAvatarPath = parcel.readString();
        this.mContentTs = parcel.readLong();
        this.mContentTitle = parcel.readString();
        this.mContentText = parcel.readString();
        this.mSenderName = parcel.readString();
        for (Object obj : parcel.readArray(RoomNotifications.class.getClassLoader())) {
            this.mRoomNotifications.add((RoomNotifications) obj);
        }
        int readInt = parcel.readInt();
        this.mReversedMessagesList = new ArrayList();
        for (int i = 0; i < readInt; i++) {
            this.mReversedMessagesList.add(TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel));
        }
    }

    private byte[] marshall() {
        Parcel obtain = Parcel.obtain();
        writeToParcel(obtain, 0);
        byte[] marshall = obtain.marshall();
        obtain.recycle();
        return marshall;
    }

    private RoomsNotifications(byte[] bArr) {
        String str = "";
        this.mSessionId = str;
        this.mRoomId = str;
        this.mSummaryText = str;
        this.mQuickReplyBody = str;
        this.mWearableMessage = str;
        this.mRoomAvatarPath = str;
        this.mContentTs = -1;
        this.mContentTitle = str;
        this.mContentText = str;
        this.mSenderName = str;
        this.mRoomNotifications = new ArrayList();
        this.mReversedMessagesList = new ArrayList();
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(bArr, 0, bArr.length);
        obtain.setDataPosition(0);
        init(obtain);
        obtain.recycle();
    }

    public static void deleteCachedRoomNotifications(Context context) {
        File file = new File(context.getApplicationContext().getCacheDir(), ROOMS_NOTIFICATIONS_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x005d A[SYNTHETIC, Splitter:B:21:0x005d] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0065 A[Catch:{ Exception -> 0x0061 }] */
    public static void saveRoomNotifications(Context context, RoomsNotifications roomsNotifications) {
        FileOutputStream fileOutputStream;
        ByteArrayInputStream byteArrayInputStream;
        String str = "## saveRoomNotifications() failed ";
        deleteCachedRoomNotifications(context);
        if (!roomsNotifications.mRoomNotifications.isEmpty()) {
            try {
                byteArrayInputStream = new ByteArrayInputStream(roomsNotifications.marshall());
                try {
                    fileOutputStream = new FileOutputStream(new File(context.getApplicationContext().getCacheDir(), ROOMS_NOTIFICATIONS_FILE_NAME));
                    try {
                        byte[] bArr = new byte[1024];
                        while (true) {
                            int read = byteArrayInputStream.read(bArr, 0, 1024);
                            if (read <= 0) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, read);
                        }
                    } catch (Throwable th) {
                        th = th;
                        String str2 = LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(th.getMessage());
                        Log.e(str2, sb.toString(), th);
                        if (byteArrayInputStream != null) {
                        }
                        if (fileOutputStream != null) {
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileOutputStream = null;
                    String str22 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(th.getMessage());
                    Log.e(str22, sb2.toString(), th);
                    if (byteArrayInputStream != null) {
                    }
                    if (fileOutputStream != null) {
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                fileOutputStream = null;
                byteArrayInputStream = null;
                String str222 = LOG_TAG;
                StringBuilder sb22 = new StringBuilder();
                sb22.append(str);
                sb22.append(th.getMessage());
                Log.e(str222, sb22.toString(), th);
                if (byteArrayInputStream != null) {
                }
                if (fileOutputStream != null) {
                }
            }
            if (byteArrayInputStream != null) {
                try {
                    byteArrayInputStream.close();
                } catch (Exception e) {
                    String str3 = LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str);
                    sb3.append(e.getMessage());
                    Log.e(str3, sb3.toString(), e);
                }
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x005f A[SYNTHETIC, Splitter:B:22:0x005f] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0067 A[Catch:{ Exception -> 0x0063 }] */
    public static RoomsNotifications loadRoomsNotifications(Context context) {
        FileInputStream fileInputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        String str = "## loadRoomsNotifications() failed ";
        File file = new File(context.getApplicationContext().getCacheDir(), ROOMS_NOTIFICATIONS_FILE_NAME);
        RoomsNotifications roomsNotifications = null;
        if (!file.exists()) {
            return null;
        }
        try {
            fileInputStream = new FileInputStream(file);
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
            } catch (Throwable th) {
                th = th;
                byteArrayOutputStream = null;
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(th.getMessage());
                Log.e(str2, sb.toString(), th);
                if (fileInputStream != null) {
                }
                if (byteArrayOutputStream != null) {
                }
                return roomsNotifications;
            }
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = fileInputStream.read(bArr, 0, 1024);
                    if (read <= 0) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                roomsNotifications = new RoomsNotifications(byteArrayOutputStream.toByteArray());
            } catch (Throwable th2) {
                th = th2;
                String str22 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(th.getMessage());
                Log.e(str22, sb2.toString(), th);
                if (fileInputStream != null) {
                }
                if (byteArrayOutputStream != null) {
                }
                return roomsNotifications;
            }
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
            byteArrayOutputStream = null;
            String str222 = LOG_TAG;
            StringBuilder sb22 = new StringBuilder();
            sb22.append(str);
            sb22.append(th.getMessage());
            Log.e(str222, sb22.toString(), th);
            if (fileInputStream != null) {
            }
            if (byteArrayOutputStream != null) {
            }
            return roomsNotifications;
        }
        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str);
                sb3.append(e.getMessage());
                Log.e(str3, sb3.toString(), e);
            }
        }
        if (byteArrayOutputStream != null) {
            byteArrayOutputStream.close();
        }
        return roomsNotifications;
    }

    public static String getRoomName(Context context, MXSession mXSession, Room room, Event event) {
        String roomDisplayName = DinsicUtils.getRoomDisplayName(context, room);
        if (!TextUtils.equals(roomDisplayName, room.getRoomId()) || event == null) {
            return roomDisplayName;
        }
        User user = mXSession.getDataHandler().getStore().getUser(event.sender);
        if (user != null) {
            return user.displayname;
        }
        return event.sender;
    }
}
