package org.matrix.androidsdk.rest.model;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Comparator;
import org.matrix.androidsdk.core.ContentManager;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.interfaces.DatedObject;
import org.matrix.androidsdk.crypto.interfaces.CryptoRoomMember;

public class RoomMember implements Externalizable, DatedObject, CryptoRoomMember {
    private static final String LOG_TAG = RoomMember.class.getSimpleName();
    public static final String MEMBERSHIP_BAN = "ban";
    public static final String MEMBERSHIP_INVITE = "invite";
    public static final String MEMBERSHIP_JOIN = "join";
    public static final String MEMBERSHIP_KICK = "kick";
    public static final String MEMBERSHIP_LEAVE = "leave";
    public static Comparator<RoomMember> alphaComparator = new Comparator<RoomMember>() {
        public int compare(RoomMember roomMember, RoomMember roomMember2) {
            String name = roomMember.getName();
            String name2 = roomMember2.getName();
            if (name == null) {
                return -1;
            }
            if (name2 == null) {
                return 1;
            }
            String str = "@";
            if (name.startsWith(str)) {
                name = name.substring(1);
            }
            if (name2.startsWith(str)) {
                name2 = name2.substring(1);
            }
            return String.CASE_INSENSITIVE_ORDER.compare(name, name2);
        }
    };
    @SerializedName("avatar_url")
    public String avatarUrl;
    @SerializedName("displayname")
    public String displayname;
    @SerializedName("is_direct")
    public Boolean isDirect;
    private long mOriginServerTs = -1;
    private String mOriginalEventId = null;
    public String mSender;
    public String membership;
    public String reason;
    public Invite thirdPartyInvite;
    private String userId = null;

    public long getDate() {
        return this.mOriginServerTs;
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        if (objectInput.readBoolean()) {
            this.displayname = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.avatarUrl = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.membership = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.thirdPartyInvite = (Invite) objectInput.readObject();
        }
        if (objectInput.readBoolean()) {
            this.isDirect = Boolean.valueOf(objectInput.readBoolean());
        }
        if (objectInput.readBoolean()) {
            this.userId = objectInput.readUTF();
        }
        this.mOriginServerTs = objectInput.readLong();
        if (objectInput.readBoolean()) {
            this.mOriginalEventId = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.reason = objectInput.readUTF();
        }
        if (objectInput.readBoolean()) {
            this.mSender = objectInput.readUTF();
        }
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        boolean z = true;
        objectOutput.writeBoolean(this.displayname != null);
        String str = this.displayname;
        if (str != null) {
            objectOutput.writeUTF(str);
        }
        objectOutput.writeBoolean(this.avatarUrl != null);
        String str2 = this.avatarUrl;
        if (str2 != null) {
            objectOutput.writeUTF(str2);
        }
        objectOutput.writeBoolean(this.membership != null);
        String str3 = this.membership;
        if (str3 != null) {
            objectOutput.writeUTF(str3);
        }
        objectOutput.writeBoolean(this.thirdPartyInvite != null);
        Invite invite = this.thirdPartyInvite;
        if (invite != null) {
            objectOutput.writeObject(invite);
        }
        objectOutput.writeBoolean(this.isDirect != null);
        Boolean bool = this.isDirect;
        if (bool != null) {
            objectOutput.writeBoolean(bool.booleanValue());
        }
        objectOutput.writeBoolean(this.userId != null);
        String str4 = this.userId;
        if (str4 != null) {
            objectOutput.writeUTF(str4);
        }
        objectOutput.writeLong(this.mOriginServerTs);
        objectOutput.writeBoolean(this.mOriginalEventId != null);
        String str5 = this.mOriginalEventId;
        if (str5 != null) {
            objectOutput.writeUTF(str5);
        }
        objectOutput.writeBoolean(this.reason != null);
        String str6 = this.reason;
        if (str6 != null) {
            objectOutput.writeUTF(str6);
        }
        if (this.mSender == null) {
            z = false;
        }
        objectOutput.writeBoolean(z);
        String str7 = this.mSender;
        if (str7 != null) {
            objectOutput.writeUTF(str7);
        }
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String str) {
        this.userId = str;
    }

    public void setOriginServerTs(long j) {
        this.mOriginServerTs = j;
    }

    public long getOriginServerTs() {
        return this.mOriginServerTs;
    }

    public void setOriginalEventId(String str) {
        this.mOriginalEventId = str;
    }

    public String getOriginalEventId() {
        return this.mOriginalEventId;
    }

    public String getAvatarUrl() {
        String str = this.avatarUrl;
        if (str == null || str.toLowerCase().startsWith(ContentManager.MATRIX_CONTENT_URI_SCHEME)) {
            return this.avatarUrl;
        }
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## getAvatarUrl() : the member ");
        sb.append(this.userId);
        sb.append(" has an invalid avatar url ");
        sb.append(this.avatarUrl);
        Log.e(str2, sb.toString());
        return null;
    }

    public void setAvatarUrl(String str) {
        this.avatarUrl = str;
    }

    public String getThirdPartyInviteToken() {
        Invite invite = this.thirdPartyInvite;
        if (invite == null || invite.signed == null) {
            return null;
        }
        return this.thirdPartyInvite.signed.token;
    }

    public boolean matchWithPattern(String str) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str.trim())) {
            return false;
        }
        boolean z = !TextUtils.isEmpty(this.displayname) && this.displayname.toLowerCase().indexOf(str) >= 0;
        if (!z && !TextUtils.isEmpty(this.userId)) {
            z = this.userId.toLowerCase().indexOf(str) >= 0;
        }
        return z;
    }

    public boolean matchWithRegEx(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (!TextUtils.isEmpty(this.displayname)) {
            z = this.displayname.matches(str);
        }
        if (!z && !TextUtils.isEmpty(this.userId)) {
            z = this.userId.matches(str);
        }
        return z;
    }

    public boolean equals(RoomMember roomMember) {
        if (roomMember == null) {
            return false;
        }
        boolean equals = TextUtils.equals(this.displayname, roomMember.displayname);
        if (equals) {
            equals = TextUtils.equals(this.avatarUrl, roomMember.avatarUrl);
        }
        if (equals) {
            equals = TextUtils.equals(this.membership, roomMember.membership);
        }
        if (equals) {
            equals = TextUtils.equals(this.userId, roomMember.userId);
        }
        return equals;
    }

    public String getName() {
        String str = this.displayname;
        if (str != null) {
            return str;
        }
        String str2 = this.userId;
        if (str2 != null) {
            return str2;
        }
        return null;
    }

    public void prune() {
        this.displayname = null;
        this.avatarUrl = null;
        this.reason = null;
    }

    public RoomMember deepCopy() {
        RoomMember roomMember = new RoomMember();
        roomMember.displayname = this.displayname;
        roomMember.avatarUrl = this.avatarUrl;
        roomMember.membership = this.membership;
        roomMember.userId = this.userId;
        roomMember.mOriginalEventId = this.mOriginalEventId;
        roomMember.mSender = this.mSender;
        roomMember.reason = this.reason;
        return roomMember;
    }

    public boolean kickedOrBanned() {
        return TextUtils.equals(this.membership, MEMBERSHIP_KICK) || TextUtils.equals(this.membership, "ban");
    }

    public String getMembership() {
        return this.membership;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.displayname);
        sb.append(" (");
        sb.append(this.userId);
        sb.append(") ");
        sb.append(super.toString());
        return sb.toString();
    }
}
