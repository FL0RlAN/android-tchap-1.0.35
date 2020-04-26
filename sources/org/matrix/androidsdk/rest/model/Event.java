package org.matrix.androidsdk.rest.model;

import android.text.TextUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import kotlin.jvm.internal.LongCompanionObject;
import org.apache.commons.cli.HelpFormatter;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.MXCryptoError;
import org.matrix.androidsdk.crypto.MXEventDecryptionResult;
import org.matrix.androidsdk.crypto.cryptostore.db.model.DeviceInfoEntityFields;
import org.matrix.androidsdk.crypto.interfaces.CryptoEvent;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedEventContent;
import org.matrix.androidsdk.crypto.model.crypto.EncryptedFileInfo;
import org.matrix.androidsdk.crypto.model.crypto.ForwardedRoomKeyContent;
import org.matrix.androidsdk.crypto.model.crypto.OlmEventContent;
import org.matrix.androidsdk.crypto.model.crypto.RoomKeyContent;
import org.matrix.androidsdk.crypto.rest.model.crypto.RoomKeyShare;
import org.matrix.androidsdk.crypto.rest.model.crypto.RoomKeyShareRequest;
import org.matrix.androidsdk.db.MXMediaCache;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;
import org.matrix.androidsdk.rest.model.message.FileMessage;
import org.matrix.androidsdk.rest.model.message.ImageMessage;
import org.matrix.androidsdk.rest.model.message.LocationMessage;
import org.matrix.androidsdk.rest.model.message.Message;
import org.matrix.androidsdk.rest.model.message.StickerMessage;
import org.matrix.androidsdk.rest.model.message.VideoMessage;
import org.matrix.olm.OlmAccount;

public class Event implements Externalizable, CryptoEvent {
    public static final long DUMMY_EVENT_AGE = 9223372036854775806L;
    public static final String EVENT_TYPE_CALL_ANSWER = "m.call.answer";
    public static final String EVENT_TYPE_CALL_CANDIDATES = "m.call.candidates";
    public static final String EVENT_TYPE_CALL_HANGUP = "m.call.hangup";
    public static final String EVENT_TYPE_CALL_INVITE = "m.call.invite";
    public static final String EVENT_TYPE_FEEDBACK = "m.room.message.feedback";
    public static final String EVENT_TYPE_MESSAGE = "m.room.message";
    public static final String EVENT_TYPE_MESSAGE_ENCRYPTED = "m.room.encrypted";
    public static final String EVENT_TYPE_MESSAGE_ENCRYPTION = "m.room.encryption";
    public static final String EVENT_TYPE_PRESENCE = "m.presence";
    public static final String EVENT_TYPE_READ_MARKER = "m.fully_read";
    public static final String EVENT_TYPE_RECEIPT = "m.receipt";
    public static final String EVENT_TYPE_REDACTION = "m.room.redaction";
    public static final String EVENT_TYPE_ROOM_BOT_OPTIONS = "m.room.bot.options";
    public static final String EVENT_TYPE_ROOM_PLUMBING = "m.room.plumbing";
    public static final String EVENT_TYPE_STATE_CANONICAL_ALIAS = "m.room.canonical_alias";
    public static final String EVENT_TYPE_STATE_HISTORY_VISIBILITY = "m.room.history_visibility";
    public static final String EVENT_TYPE_STATE_PINNED_EVENT = "m.room.pinned_events";
    public static final String EVENT_TYPE_STATE_RELATED_GROUPS = "m.room.related_groups";
    public static final String EVENT_TYPE_STATE_ROOM_ALIASES = "m.room.aliases";
    public static final String EVENT_TYPE_STATE_ROOM_AVATAR = "m.room.avatar";
    public static final String EVENT_TYPE_STATE_ROOM_CREATE = "m.room.create";
    public static final String EVENT_TYPE_STATE_ROOM_GUEST_ACCESS = "m.room.guest_access";
    public static final String EVENT_TYPE_STATE_ROOM_JOIN_RULES = "m.room.join_rules";
    public static final String EVENT_TYPE_STATE_ROOM_MEMBER = "m.room.member";
    public static final String EVENT_TYPE_STATE_ROOM_NAME = "m.room.name";
    public static final String EVENT_TYPE_STATE_ROOM_POWER_LEVELS = "m.room.power_levels";
    public static final String EVENT_TYPE_STATE_ROOM_THIRD_PARTY_INVITE = "m.room.third_party_invite";
    public static final String EVENT_TYPE_STATE_ROOM_TOMBSTONE = "m.room.tombstone";
    public static final String EVENT_TYPE_STATE_ROOM_TOPIC = "m.room.topic";
    public static final String EVENT_TYPE_STICKER = "m.sticker";
    public static final String EVENT_TYPE_TAGS = "m.tag";
    public static final String EVENT_TYPE_TYPING = "m.typing";
    public static final String EVENT_TYPE_URL_PREVIEW = "org.matrix.room.preview_urls";
    private static final String LOG_TAG = Event.class.getSimpleName();
    static final long MAX_ORIGIN_SERVER_TS = 1125899906842624L;
    public static final String PAGINATE_BACK_TOKEN_END = "PAGINATE_BACK_TOKEN_END";
    static DateFormat mDateFormat = null;
    static long mFormatterRawOffset = 1234;
    private static final long serialVersionUID = -1431845331022808337L;
    public Long age;
    private String contentAsString;
    @SerializedName("content")
    public transient JsonElement contentJson;
    public String eventId;
    private transient String mClaimedEd25519Key;
    private transient Event mClearEvent;
    private MXCryptoError mCryptoError;
    private transient List<String> mForwardingCurve25519KeyChain;
    public boolean mIsInternalPaginationToken;
    private String mMatrixId;
    private transient String mSenderCurve25519Key;
    public SentState mSentState;
    private long mTimeZoneRawOffset;
    public String mToken;
    public long originServerTs;
    public transient JsonElement prev_content;
    private String prev_content_as_string;
    public String redacts;
    public String roomId;
    public String sender;
    @SerializedName("state_key")
    public String stateKey;
    public String type;
    public Exception unsentException;
    public MatrixError unsentMatrixError;
    public UnsignedData unsigned;
    public String userId;

    public enum SentState {
        UNSENT,
        ENCRYPTING,
        SENDING,
        WAITING_RETRY,
        SENT,
        UNDELIVERED,
        FAILED_UNKNOWN_DEVICES
    }

    private long getTimeZoneOffset() {
        return (long) TimeZone.getDefault().getRawOffset();
    }

    public Event() {
        this.contentJson = null;
        this.contentAsString = null;
        this.prev_content = null;
        this.prev_content_as_string = null;
        this.unsentException = null;
        this.unsentMatrixError = null;
        this.mTimeZoneRawOffset = 0;
        this.mForwardingCurve25519KeyChain = new ArrayList();
        this.type = null;
        this.contentJson = null;
        this.prev_content = null;
        this.mIsInternalPaginationToken = false;
        this.eventId = null;
        this.roomId = null;
        this.userId = null;
        this.originServerTs = 0;
        this.age = null;
        this.mTimeZoneRawOffset = getTimeZoneOffset();
        this.stateKey = null;
        this.redacts = null;
        this.unsentMatrixError = null;
        this.unsentException = null;
        this.mMatrixId = null;
        this.mSentState = SentState.SENT;
    }

    public String getSender() {
        String str = this.sender;
        return str == null ? this.userId : str;
    }

    public void setSender(String str) {
        this.userId = str;
        this.sender = str;
    }

    public void setMatrixId(String str) {
        this.mMatrixId = str;
    }

    public String getMatrixId() {
        return this.mMatrixId;
    }

    public boolean isValidOriginServerTs() {
        return this.originServerTs < MAX_ORIGIN_SERVER_TS;
    }

    public long getOriginServerTs() {
        return this.originServerTs;
    }

    public void updateContent(JsonElement jsonElement) {
        this.contentJson = jsonElement;
        this.contentAsString = null;
    }

    public boolean hasContentFields() {
        if (getContentAsJsonObject() == null) {
            return false;
        }
        Set entrySet = getContentAsJsonObject().entrySet();
        if (entrySet == null || entrySet.size() == 0) {
            return false;
        }
        return true;
    }

    public boolean isRedacted() {
        UnsignedData unsignedData = this.unsigned;
        return (unsignedData == null || unsignedData.redacted_because == null) ? false : true;
    }

    public String formattedOriginServerTs() {
        if (!isValidOriginServerTs()) {
            return " ";
        }
        if (mDateFormat == null || mFormatterRawOffset != getTimeZoneOffset()) {
            mDateFormat = new SimpleDateFormat("MMM d HH:mm", Locale.getDefault());
            mFormatterRawOffset = getTimeZoneOffset();
        }
        return mDateFormat.format(new Date(getOriginServerTs()));
    }

    public void setOriginServerTs(long j) {
        this.originServerTs = j;
    }

    public String getType() {
        Event event = this.mClearEvent;
        if (event != null) {
            return event.type;
        }
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getWireType() {
        return this.type;
    }

    public JsonElement getContent() {
        Event event = this.mClearEvent;
        if (event != null) {
            return event.getWireContent();
        }
        return getWireContent();
    }

    public JsonElement getWireContent() {
        finalizeDeserialization();
        return this.contentJson;
    }

    public JsonObject toJsonObject() {
        finalizeDeserialization();
        return JsonUtils.toJson(this);
    }

    public JsonObject getContentAsJsonObject() {
        JsonElement content = getContent();
        if (content == null || !content.isJsonObject()) {
            return null;
        }
        return content.getAsJsonObject();
    }

    public JsonObject getPrevContentAsJsonObject() {
        finalizeDeserialization();
        UnsignedData unsignedData = this.unsigned;
        if (!(unsignedData == null || unsignedData.prev_content == null)) {
            if (this.prev_content == null) {
                this.prev_content = this.unsigned.prev_content;
            }
            this.unsigned.prev_content = null;
        }
        JsonElement jsonElement = this.prev_content;
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            return null;
        }
        return this.prev_content.getAsJsonObject();
    }

    public EventContent getEventContent() {
        if (getContent() != null) {
            return JsonUtils.toEventContent(getContent());
        }
        return null;
    }

    public EventContent getWireEventContent() {
        if (getWireContent() != null) {
            return JsonUtils.toEventContent(getWireContent());
        }
        return null;
    }

    public EventContent getPrevContent() {
        if (getPrevContentAsJsonObject() != null) {
            return JsonUtils.toEventContent(getPrevContentAsJsonObject());
        }
        return null;
    }

    public long getAge() {
        Long l = this.age;
        if (l != null) {
            return l.longValue();
        }
        UnsignedData unsignedData = this.unsigned;
        if (unsignedData == null || unsignedData.age == null) {
            return LongCompanionObject.MAX_VALUE;
        }
        this.age = this.unsigned.age;
        return this.age.longValue();
    }

    public String getRedactedEventId() {
        String str = this.redacts;
        if (str != null) {
            return str;
        }
        if (!isRedacted()) {
            return null;
        }
        this.redacts = this.unsigned.redacted_because.redacts;
        return this.redacts;
    }

    public Event(Message message, String str, String str2) {
        this.contentJson = null;
        this.contentAsString = null;
        this.prev_content = null;
        this.prev_content_as_string = null;
        this.unsentException = null;
        this.unsentMatrixError = null;
        this.mTimeZoneRawOffset = 0;
        this.mForwardingCurve25519KeyChain = new ArrayList();
        this.type = EVENT_TYPE_MESSAGE;
        this.contentJson = JsonUtils.toJson(message);
        this.originServerTs = System.currentTimeMillis();
        this.userId = str;
        this.sender = str;
        this.roomId = str2;
        this.mSentState = SentState.UNSENT;
        createDummyEventId();
    }

    public Event(String str, JsonObject jsonObject, String str2, String str3) {
        this.contentJson = null;
        this.contentAsString = null;
        this.prev_content = null;
        this.prev_content_as_string = null;
        this.unsentException = null;
        this.unsentMatrixError = null;
        this.mTimeZoneRawOffset = 0;
        this.mForwardingCurve25519KeyChain = new ArrayList();
        this.type = str;
        this.contentJson = jsonObject;
        this.originServerTs = System.currentTimeMillis();
        this.userId = str2;
        this.sender = str2;
        this.roomId = str3;
        this.mSentState = SentState.UNSENT;
        createDummyEventId();
    }

    public void createDummyEventId() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.roomId);
        sb.append(HelpFormatter.DEFAULT_OPT_PREFIX);
        sb.append(this.originServerTs);
        this.eventId = sb.toString();
        this.age = Long.valueOf(DUMMY_EVENT_AGE);
    }

    public boolean isDummyEvent() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.roomId);
        sb.append(HelpFormatter.DEFAULT_OPT_PREFIX);
        sb.append(this.originServerTs);
        return sb.toString().equals(this.eventId);
    }

    public void setInternalPaginationToken(String str) {
        this.mToken = str;
        this.mIsInternalPaginationToken = true;
    }

    public boolean isInternalPaginationToken() {
        return this.mIsInternalPaginationToken;
    }

    public boolean hasToken() {
        return this.mToken != null && !this.mIsInternalPaginationToken;
    }

    public boolean isCallEvent() {
        if (!EVENT_TYPE_CALL_INVITE.equals(getType())) {
            if (!EVENT_TYPE_CALL_CANDIDATES.equals(getType())) {
                if (!EVENT_TYPE_CALL_ANSWER.equals(getType())) {
                    if (!EVENT_TYPE_CALL_HANGUP.equals(getType())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Event deepCopy() {
        finalizeDeserialization();
        Event event = new Event();
        event.type = this.type;
        event.contentJson = this.contentJson;
        event.contentAsString = this.contentAsString;
        event.eventId = this.eventId;
        event.roomId = this.roomId;
        event.userId = this.userId;
        event.sender = this.sender;
        event.originServerTs = this.originServerTs;
        event.mTimeZoneRawOffset = this.mTimeZoneRawOffset;
        event.age = this.age;
        event.stateKey = this.stateKey;
        event.prev_content = this.prev_content;
        event.prev_content_as_string = this.prev_content_as_string;
        event.unsigned = this.unsigned;
        event.redacts = this.redacts;
        event.mSentState = this.mSentState;
        event.unsentException = this.unsentException;
        event.unsentMatrixError = this.unsentMatrixError;
        event.mMatrixId = this.mMatrixId;
        event.mToken = this.mToken;
        event.mIsInternalPaginationToken = this.mIsInternalPaginationToken;
        return event;
    }

    public boolean canBeResent() {
        return this.mSentState == SentState.WAITING_RETRY || this.mSentState == SentState.UNDELIVERED || this.mSentState == SentState.FAILED_UNKNOWN_DEVICES;
    }

    public boolean isEncrypting() {
        return this.mSentState == SentState.ENCRYPTING;
    }

    public boolean isUnsent() {
        return this.mSentState == SentState.UNSENT;
    }

    public boolean isSending() {
        return this.mSentState == SentState.SENDING || this.mSentState == SentState.WAITING_RETRY;
    }

    public boolean isUndelivered() {
        return this.mSentState == SentState.UNDELIVERED;
    }

    public boolean isUnknownDevice() {
        return this.mSentState == SentState.FAILED_UNKNOWN_DEVICES;
    }

    public boolean isSent() {
        return this.mSentState == SentState.SENT;
    }

    public List<String> getMediaUrls() {
        ArrayList arrayList = new ArrayList();
        if (EVENT_TYPE_MESSAGE.equals(getType())) {
            String messageMsgType = JsonUtils.getMessageMsgType(getContent());
            if (Message.MSGTYPE_IMAGE.equals(messageMsgType)) {
                ImageMessage imageMessage = JsonUtils.toImageMessage(getContent());
                if (imageMessage.getUrl() != null) {
                    arrayList.add(imageMessage.getUrl());
                }
                if (imageMessage.getThumbnailUrl() != null) {
                    arrayList.add(imageMessage.getThumbnailUrl());
                }
            } else if (Message.MSGTYPE_FILE.equals(messageMsgType) || Message.MSGTYPE_AUDIO.equals(messageMsgType)) {
                FileMessage fileMessage = JsonUtils.toFileMessage(getContent());
                if (fileMessage.getUrl() != null) {
                    arrayList.add(fileMessage.getUrl());
                }
            } else if (Message.MSGTYPE_VIDEO.equals(messageMsgType)) {
                VideoMessage videoMessage = JsonUtils.toVideoMessage(getContent());
                if (videoMessage.getUrl() != null) {
                    arrayList.add(videoMessage.getUrl());
                }
                if (videoMessage.getThumbnailUrl() != null) {
                    arrayList.add(videoMessage.getThumbnailUrl());
                }
            } else if (Message.MSGTYPE_LOCATION.equals(messageMsgType)) {
                LocationMessage locationMessage = JsonUtils.toLocationMessage(getContent());
                if (locationMessage.thumbnail_url != null) {
                    arrayList.add(locationMessage.thumbnail_url);
                }
            }
        } else {
            if (EVENT_TYPE_STICKER.equals(getType())) {
                StickerMessage stickerMessage = JsonUtils.toStickerMessage(getContent());
                if (stickerMessage.getUrl() != null) {
                    arrayList.add(stickerMessage.getUrl());
                }
                if (stickerMessage.getThumbnailUrl() != null) {
                    arrayList.add(stickerMessage.getThumbnailUrl());
                }
            }
        }
        return arrayList;
    }

    public List<EncryptedFileInfo> getEncryptedFileInfos() {
        ArrayList arrayList = new ArrayList();
        if (!isEncrypted()) {
            return arrayList;
        }
        if (EVENT_TYPE_MESSAGE.equals(getType())) {
            String messageMsgType = JsonUtils.getMessageMsgType(getContent());
            if (Message.MSGTYPE_IMAGE.equals(messageMsgType)) {
                ImageMessage imageMessage = JsonUtils.toImageMessage(getContent());
                if (imageMessage.file != null) {
                    arrayList.add(imageMessage.file);
                }
                if (!(imageMessage.info == null || imageMessage.info.thumbnail_file == null)) {
                    arrayList.add(imageMessage.info.thumbnail_file);
                }
            } else if (Message.MSGTYPE_FILE.equals(messageMsgType) || Message.MSGTYPE_AUDIO.equals(messageMsgType)) {
                FileMessage fileMessage = JsonUtils.toFileMessage(getContent());
                if (fileMessage.file != null) {
                    arrayList.add(fileMessage.file);
                }
            } else if (Message.MSGTYPE_VIDEO.equals(messageMsgType)) {
                VideoMessage videoMessage = JsonUtils.toVideoMessage(getContent());
                if (videoMessage.file != null) {
                    arrayList.add(videoMessage.file);
                }
                if (!(videoMessage.info == null || videoMessage.info.thumbnail_file == null)) {
                    arrayList.add(videoMessage.info.thumbnail_file);
                }
            }
        } else {
            if (EVENT_TYPE_STICKER.equals(getType())) {
                StickerMessage stickerMessage = JsonUtils.toStickerMessage(getContent());
                if (stickerMessage.file != null) {
                    arrayList.add(stickerMessage.file);
                }
                if (!(stickerMessage.info == null || stickerMessage.info.thumbnail_file == null)) {
                    arrayList.add(stickerMessage.info.thumbnail_file);
                }
            }
        }
        return arrayList;
    }

    public boolean isUploadingMedia(MXMediaCache mXMediaCache) {
        for (String progressValueForUploadId : getMediaUrls()) {
            if (mXMediaCache.getProgressValueForUploadId(progressValueForUploadId) >= 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isDownloadingMedia(MXMediaCache mXMediaCache) {
        for (String downloadIdFromUrl : getMediaUrls()) {
            if (mXMediaCache.getProgressValueForDownloadId(mXMediaCache.downloadIdFromUrl(downloadIdFromUrl)) >= 0) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"age\" : ");
        sb.append(this.age);
        String str = ",\n";
        sb.append(str);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        sb3.append("  \"content\": {\n");
        String sb4 = sb3.toString();
        if (getWireContent() != null) {
            if (getWireContent().isJsonArray()) {
                Iterator it = getWireContent().getAsJsonArray().iterator();
                while (it.hasNext()) {
                    JsonElement jsonElement = (JsonElement) it.next();
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(sb4);
                    sb5.append("    ");
                    sb5.append(jsonElement.toString());
                    sb5.append(str);
                    sb4 = sb5.toString();
                }
            } else if (getWireContent().isJsonObject()) {
                for (Entry entry : getWireContent().getAsJsonObject().entrySet()) {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(sb4);
                    sb6.append("    \"");
                    sb6.append((String) entry.getKey());
                    sb6.append("\": ");
                    sb6.append(((JsonElement) entry.getValue()).toString());
                    sb6.append(str);
                    sb4 = sb6.toString();
                }
            } else {
                StringBuilder sb7 = new StringBuilder();
                sb7.append(sb4);
                sb7.append(getWireContent().toString());
                sb4 = sb7.toString();
            }
        }
        StringBuilder sb8 = new StringBuilder();
        sb8.append(sb4);
        sb8.append("  },\n");
        String sb9 = sb8.toString();
        StringBuilder sb10 = new StringBuilder();
        sb10.append(sb9);
        sb10.append("  \"eventId\": \"");
        sb10.append(this.eventId);
        String str2 = "\",\n";
        sb10.append(str2);
        String sb11 = sb10.toString();
        StringBuilder sb12 = new StringBuilder();
        sb12.append(sb11);
        sb12.append("  \"originServerTs\": ");
        sb12.append(this.originServerTs);
        sb12.append(str);
        String sb13 = sb12.toString();
        StringBuilder sb14 = new StringBuilder();
        sb14.append(sb13);
        sb14.append("  \"roomId\": \"");
        sb14.append(this.roomId);
        sb14.append(str2);
        String sb15 = sb14.toString();
        StringBuilder sb16 = new StringBuilder();
        sb16.append(sb15);
        sb16.append("  \"type\": \"");
        sb16.append(this.type);
        sb16.append(str2);
        String sb17 = sb16.toString();
        StringBuilder sb18 = new StringBuilder();
        sb18.append(sb17);
        sb18.append("  \"userId\": \"");
        sb18.append(this.userId);
        sb18.append(str2);
        String sb19 = sb18.toString();
        StringBuilder sb20 = new StringBuilder();
        sb20.append(sb19);
        sb20.append("  \"sender\": \"");
        sb20.append(this.sender);
        sb20.append(str2);
        String sb21 = sb20.toString();
        StringBuilder sb22 = new StringBuilder();
        sb22.append(sb21);
        sb22.append("}");
        String sb23 = sb22.toString();
        StringBuilder sb24 = new StringBuilder();
        sb24.append(sb23);
        sb24.append("\n\n Sent state : ");
        String sb25 = sb24.toString();
        if (this.mSentState == SentState.UNSENT) {
            StringBuilder sb26 = new StringBuilder();
            sb26.append(sb25);
            sb26.append("UNSENT");
            sb25 = sb26.toString();
        } else if (this.mSentState == SentState.SENDING) {
            StringBuilder sb27 = new StringBuilder();
            sb27.append(sb25);
            sb27.append("SENDING");
            sb25 = sb27.toString();
        } else if (this.mSentState == SentState.WAITING_RETRY) {
            StringBuilder sb28 = new StringBuilder();
            sb28.append(sb25);
            sb28.append("WAITING_RETRY");
            sb25 = sb28.toString();
        } else if (this.mSentState == SentState.SENT) {
            StringBuilder sb29 = new StringBuilder();
            sb29.append(sb25);
            sb29.append("SENT");
            sb25 = sb29.toString();
        } else if (this.mSentState == SentState.UNDELIVERED) {
            StringBuilder sb30 = new StringBuilder();
            sb30.append(sb25);
            sb30.append("UNDELIVERED");
            sb25 = sb30.toString();
        } else if (this.mSentState == SentState.FAILED_UNKNOWN_DEVICES) {
            StringBuilder sb31 = new StringBuilder();
            sb31.append(sb25);
            sb31.append("FAILED UNKNOWN DEVICES");
            sb25 = sb31.toString();
        }
        String str3 = "\n";
        if (this.unsentException != null) {
            StringBuilder sb32 = new StringBuilder();
            sb32.append(sb25);
            sb32.append("\n\n Exception reason: ");
            sb32.append(this.unsentException.getMessage());
            sb32.append(str3);
            sb25 = sb32.toString();
        }
        if (this.unsentMatrixError == null) {
            return sb25;
        }
        StringBuilder sb33 = new StringBuilder();
        sb33.append(sb25);
        sb33.append("\n\n Matrix reason: ");
        sb33.append(this.unsentMatrixError.getLocalizedMessage());
        sb33.append(str3);
        return sb33.toString();
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        if (objectInput.readBoolean()) {
            this.type = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.contentAsString = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.prev_content_as_string = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.eventId = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.roomId = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.userId = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.sender = objectInput.readUTF();
        }
        this.originServerTs = objectInput.readLong();
        if (objectInput.readBoolean()) {
            this.age = Long.valueOf(objectInput.readLong());
        }
        if (objectInput.readBoolean()) {
            this.stateKey = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.unsigned = (UnsignedData) objectInput.readObject();
        }
        if (objectInput.readBoolean()) {
            this.redacts = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.unsentException = (Exception) objectInput.readObject();
        }
        if (objectInput.readBoolean()) {
            this.unsentMatrixError = (MatrixError) objectInput.readObject();
        }
        this.mSentState = (SentState) objectInput.readObject();
        if (objectInput.readBoolean()) {
            this.mToken = objectInput.readUTF();
        }
        this.mIsInternalPaginationToken = objectInput.readBoolean();
        if (objectInput.readBoolean()) {
            this.mMatrixId = objectInput.readUTF();
        }
        this.mTimeZoneRawOffset = objectInput.readLong();
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        prepareSerialization();
        boolean z = true;
        objectOutput.writeBoolean(this.type != null);
        String str = this.type;
        if (str != null) {
            objectOutput.writeUTF(str);
        }
        objectOutput.writeBoolean(this.contentAsString != null);
        String str2 = this.contentAsString;
        if (str2 != null) {
            objectOutput.writeUTF(str2);
        }
        objectOutput.writeBoolean(this.prev_content_as_string != null);
        String str3 = this.prev_content_as_string;
        if (str3 != null) {
            objectOutput.writeUTF(str3);
        }
        objectOutput.writeBoolean(this.eventId != null);
        String str4 = this.eventId;
        if (str4 != null) {
            objectOutput.writeUTF(str4);
        }
        objectOutput.writeBoolean(this.roomId != null);
        String str5 = this.roomId;
        if (str5 != null) {
            objectOutput.writeUTF(str5);
        }
        objectOutput.writeBoolean(this.userId != null);
        String str6 = this.userId;
        if (str6 != null) {
            objectOutput.writeUTF(str6);
        }
        objectOutput.writeBoolean(this.sender != null);
        String str7 = this.sender;
        if (str7 != null) {
            objectOutput.writeUTF(str7);
        }
        objectOutput.writeLong(this.originServerTs);
        objectOutput.writeBoolean(this.age != null);
        Long l = this.age;
        if (l != null) {
            objectOutput.writeLong(l.longValue());
        }
        objectOutput.writeBoolean(this.stateKey != null);
        String str8 = this.stateKey;
        if (str8 != null) {
            objectOutput.writeUTF(str8);
        }
        objectOutput.writeBoolean(this.unsigned != null);
        UnsignedData unsignedData = this.unsigned;
        if (unsignedData != null) {
            objectOutput.writeObject(unsignedData);
        }
        objectOutput.writeBoolean(this.redacts != null);
        String str9 = this.redacts;
        if (str9 != null) {
            objectOutput.writeUTF(str9);
        }
        objectOutput.writeBoolean(this.unsentException != null);
        Exception exc = this.unsentException;
        if (exc != null) {
            objectOutput.writeObject(exc);
        }
        objectOutput.writeBoolean(this.unsentMatrixError != null);
        MatrixError matrixError = this.unsentMatrixError;
        if (matrixError != null) {
            objectOutput.writeObject(matrixError);
        }
        objectOutput.writeObject(this.mSentState);
        objectOutput.writeBoolean(this.mToken != null);
        String str10 = this.mToken;
        if (str10 != null) {
            objectOutput.writeUTF(str10);
        }
        objectOutput.writeBoolean(this.mIsInternalPaginationToken);
        if (this.mMatrixId == null) {
            z = false;
        }
        objectOutput.writeBoolean(z);
        String str11 = this.mMatrixId;
        if (str11 != null) {
            objectOutput.writeUTF(str11);
        }
        objectOutput.writeLong(this.mTimeZoneRawOffset);
    }

    private void prepareSerialization() {
        JsonElement jsonElement = this.contentJson;
        if (jsonElement != null && this.contentAsString == null) {
            this.contentAsString = jsonElement.toString();
        }
        if (getPrevContentAsJsonObject() != null && this.prev_content_as_string == null) {
            this.prev_content_as_string = getPrevContentAsJsonObject().toString();
        }
        UnsignedData unsignedData = this.unsigned;
        if (unsignedData != null && unsignedData.prev_content != null) {
            this.unsigned.prev_content = null;
        }
    }

    private void finalizeDeserialization() {
        if (this.contentAsString != null && this.contentJson == null) {
            try {
                this.contentJson = new JsonParser().parse(this.contentAsString).getAsJsonObject();
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("finalizeDeserialization : contentAsString deserialization ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
                this.contentAsString = null;
            }
        }
        if (this.prev_content_as_string != null && this.prev_content == null) {
            try {
                this.prev_content = new JsonParser().parse(this.prev_content_as_string).getAsJsonObject();
            } catch (Exception e2) {
                String str2 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("finalizeDeserialization : prev_content_as_string deserialization ");
                sb2.append(e2.getMessage());
                Log.e(str2, sb2.toString(), e2);
                this.prev_content_as_string = null;
            }
        }
    }

    private static JsonObject filterInContentWithKeys(JsonObject jsonObject, List<String> list) {
        if (jsonObject == null) {
            return null;
        }
        JsonObject jsonObject2 = new JsonObject();
        if (list == null || list.size() == 0) {
            return new JsonObject();
        }
        Set<Entry> entrySet = jsonObject.entrySet();
        if (entrySet != null) {
            for (Entry entry : entrySet) {
                if (list.indexOf(entry.getKey()) >= 0) {
                    jsonObject2.add((String) entry.getKey(), (JsonElement) entry.getValue());
                }
            }
        }
        return jsonObject2;
    }

    public void prune(Event event) {
        List list;
        if (TextUtils.equals("m.room.member", this.type)) {
            list = new ArrayList(Arrays.asList(new String[]{"membership"}));
        } else {
            if (TextUtils.equals(EVENT_TYPE_STATE_ROOM_CREATE, this.type)) {
                list = new ArrayList(Arrays.asList(new String[]{"creator"}));
            } else {
                if (TextUtils.equals(EVENT_TYPE_STATE_ROOM_JOIN_RULES, this.type)) {
                    list = new ArrayList(Arrays.asList(new String[]{"join_rule"}));
                } else {
                    if (TextUtils.equals(EVENT_TYPE_STATE_ROOM_POWER_LEVELS, this.type)) {
                        list = new ArrayList(Arrays.asList(new String[]{DeviceInfoEntityFields.USERS, "users_default", "events", "events_default", "state_default", "ban", RoomMember.MEMBERSHIP_KICK, "redact", "invite"}));
                    } else {
                        if (TextUtils.equals(EVENT_TYPE_STATE_ROOM_ALIASES, this.type)) {
                            list = new ArrayList(Arrays.asList(new String[]{"aliases"}));
                        } else {
                            if (TextUtils.equals(EVENT_TYPE_STATE_CANONICAL_ALIAS, this.type)) {
                                list = new ArrayList(Arrays.asList(new String[]{"alias"}));
                            } else {
                                if (TextUtils.equals(EVENT_TYPE_FEEDBACK, this.type)) {
                                    list = new ArrayList(Arrays.asList(new String[]{PasswordLoginParams.IDENTIFIER_KEY_TYPE, "target_event_id"}));
                                } else {
                                    if (TextUtils.equals("m.room.encrypted", this.type)) {
                                        this.mClearEvent = null;
                                    }
                                    list = null;
                                }
                            }
                        }
                    }
                }
            }
        }
        this.contentJson = filterInContentWithKeys(getContentAsJsonObject(), list);
        this.prev_content = filterInContentWithKeys(getPrevContentAsJsonObject(), list);
        this.prev_content_as_string = null;
        this.contentAsString = null;
        if (event != null) {
            if (this.unsigned == null) {
                this.unsigned = new UnsignedData();
            }
            this.unsigned.redacted_because = new RedactedBecause();
            this.unsigned.redacted_because.type = event.type;
            this.unsigned.redacted_because.origin_server_ts = event.originServerTs;
            this.unsigned.redacted_because.sender = event.sender;
            this.unsigned.redacted_because.event_id = event.eventId;
            this.unsigned.redacted_because.unsigned = event.unsigned;
            this.unsigned.redacted_because.redacts = event.redacts;
            this.unsigned.redacted_because.content = new RedactedContent();
            JsonObject contentAsJsonObject = getContentAsJsonObject();
            if (contentAsJsonObject != null) {
                String str = "reason";
                if (contentAsJsonObject.has(str)) {
                    try {
                        this.unsigned.redacted_because.content.reason = contentAsJsonObject.get(str).getAsString();
                    } catch (Exception e) {
                        String str2 = LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("unsigned.redacted_because.content.reason failed ");
                        sb.append(e.getMessage());
                        Log.e(str2, sb.toString(), e);
                    }
                }
            }
        }
    }

    public boolean isEncrypted() {
        return TextUtils.equals(getWireType(), "m.room.encrypted");
    }

    public void setClearData(MXEventDecryptionResult mXEventDecryptionResult) {
        String str = "m.relates_to";
        this.mClearEvent = null;
        if (mXEventDecryptionResult != null) {
            if (mXEventDecryptionResult.mClearEvent != null) {
                this.mClearEvent = JsonUtils.toEvent(mXEventDecryptionResult.mClearEvent);
            }
            Event event = this.mClearEvent;
            if (event != null) {
                event.mSenderCurve25519Key = mXEventDecryptionResult.mSenderCurve25519Key;
                this.mClearEvent.mClaimedEd25519Key = mXEventDecryptionResult.mClaimedEd25519Key;
                if (mXEventDecryptionResult.mForwardingCurve25519KeyChain != null) {
                    this.mClearEvent.mForwardingCurve25519KeyChain = mXEventDecryptionResult.mForwardingCurve25519KeyChain;
                } else {
                    this.mClearEvent.mForwardingCurve25519KeyChain = new ArrayList();
                }
                try {
                    if (getWireContent().getAsJsonObject().has(str)) {
                        this.mClearEvent.getContentAsJsonObject().add(str, getWireContent().getAsJsonObject().get(str));
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Unable to restore 'm.relates_to' the clear event", e);
                }
            }
            this.mCryptoError = null;
        }
    }

    public String getSenderKey() {
        Event event = this.mClearEvent;
        if (event != null) {
            return event.mSenderCurve25519Key;
        }
        return this.mSenderCurve25519Key;
    }

    public Map<String, String> getKeysClaimed() {
        HashMap hashMap = new HashMap();
        String str = getClearEvent() != null ? getClearEvent().mClaimedEd25519Key : this.mClaimedEd25519Key;
        if (str != null) {
            hashMap.put(OlmAccount.JSON_KEY_FINGER_PRINT_KEY, str);
        }
        return hashMap;
    }

    public MXCryptoError getCryptoError() {
        return this.mCryptoError;
    }

    public void setCryptoError(MXCryptoError mXCryptoError) {
        this.mCryptoError = mXCryptoError;
        if (mXCryptoError != null) {
            this.mClearEvent = null;
        }
    }

    public Event getClearEvent() {
        return this.mClearEvent;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public String getStateKey() {
        return this.stateKey;
    }

    public RoomKeyShare toRoomKeyShare() {
        return JsonUtils.toRoomKeyShare(getContentAsJsonObject());
    }

    public RoomKeyShareRequest toRoomKeyShareRequest() {
        return JsonUtils.toRoomKeyShareRequest(getContentAsJsonObject());
    }

    public String getEventId() {
        return this.eventId;
    }

    public RoomKeyContent toRoomKeyContent() {
        return JsonUtils.toRoomKeyContent(getContentAsJsonObject());
    }

    public OlmEventContent toOlmEventContent() {
        return JsonUtils.toOlmEventContent(getWireContent().getAsJsonObject());
    }

    public EncryptedEventContent toEncryptedEventContent() {
        return JsonUtils.toEncryptedEventContent(getWireContent().getAsJsonObject());
    }

    public ForwardedRoomKeyContent toForwardedRoomKeyContent() {
        return JsonUtils.toForwardedRoomKeyContent(getContentAsJsonObject());
    }
}
