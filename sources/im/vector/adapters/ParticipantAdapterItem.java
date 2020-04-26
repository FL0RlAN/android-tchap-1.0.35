package im.vector.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageView;
import im.vector.VectorApp;
import im.vector.contacts.Contact;
import im.vector.contacts.Contact.MXID;
import im.vector.contacts.PIDsRetriever;
import im.vector.settings.VectorLocale;
import im.vector.util.VectorUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.MXPatterns;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.rest.model.RoomMember;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.pid.RoomThirdPartyInvite;

public class ParticipantAdapterItem implements Serializable {
    private static final Pattern FACEBOOK_EMAIL_ADDRESS = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@facebook.com");
    public static final Comparator<ParticipantAdapterItem> alphaComparator = new Comparator<ParticipantAdapterItem>() {
        public int compare(ParticipantAdapterItem participantAdapterItem, ParticipantAdapterItem participantAdapterItem2) {
            String comparisonDisplayName = participantAdapterItem.getComparisonDisplayName();
            String comparisonDisplayName2 = participantAdapterItem2.getComparisonDisplayName();
            if (comparisonDisplayName == null) {
                return -1;
            }
            if (comparisonDisplayName2 == null) {
                return 1;
            }
            return String.CASE_INSENSITIVE_ORDER.compare(comparisonDisplayName, comparisonDisplayName2);
        }
    };
    private static final List<Pattern> mBlackedListEmails = Collections.singletonList(FACEBOOK_EMAIL_ADDRESS);
    private static final String mTrimRegEx = "[_!~`@#$%^&*\\-+();:=\\{\\}\\[\\],.<>?]";
    public static final Comparator<ParticipantAdapterItem> tchapComparator = new Comparator<ParticipantAdapterItem>() {
        public int compare(ParticipantAdapterItem participantAdapterItem, ParticipantAdapterItem participantAdapterItem2) {
            String comparisonDisplayName = participantAdapterItem.getComparisonDisplayName();
            String comparisonDisplayName2 = participantAdapterItem2.getComparisonDisplayName();
            if (comparisonDisplayName == null) {
                return -1;
            }
            if (comparisonDisplayName2 == null) {
                return 1;
            }
            if (participantAdapterItem.mContact == null && participantAdapterItem2.mContact != null) {
                return -1;
            }
            if (participantAdapterItem.mContact == null || participantAdapterItem2.mContact != null) {
                return String.CASE_INSENSITIVE_ORDER.compare(comparisonDisplayName, comparisonDisplayName2);
            }
            return 1;
        }
    };
    public String mAvatarUrl;
    private String mComparisonDisplayName;
    public Contact mContact;
    public String mDisplayName;
    private List<String> mDisplayNameComponents;
    public boolean mIsValid = true;
    private String mLowerCaseDisplayName;
    private String mLowerCaseMatrixId;
    public RoomMember mRoomMember;
    public RoomThirdPartyInvite mRoomThirdPartyInvite;
    public String mUserId;

    public ParticipantAdapterItem(RoomMember roomMember) {
        this.mDisplayName = roomMember.getName();
        this.mAvatarUrl = roomMember.getAvatarUrl();
        this.mUserId = roomMember.getUserId();
        this.mRoomMember = roomMember;
        this.mContact = null;
        initSearchByPatternFields();
    }

    public ParticipantAdapterItem(User user) {
        this.mDisplayName = TextUtils.isEmpty(user.displayname) ? user.user_id : user.displayname;
        this.mUserId = user.user_id;
        this.mAvatarUrl = user.getAvatarUrl();
        initSearchByPatternFields();
    }

    public ParticipantAdapterItem(Contact contact) {
        this.mDisplayName = contact.getDisplayName();
        if (TextUtils.isEmpty(this.mDisplayName)) {
            this.mDisplayName = contact.getContactId();
        }
        this.mUserId = null;
        this.mAvatarUrl = contact.getThumbnailUri();
        this.mRoomMember = null;
        this.mContact = contact;
        initSearchByPatternFields();
    }

    public ParticipantAdapterItem(RoomThirdPartyInvite roomThirdPartyInvite) {
        this.mDisplayName = roomThirdPartyInvite.display_name;
        this.mUserId = null;
        this.mAvatarUrl = null;
        this.mIsValid = false;
        this.mRoomThirdPartyInvite = roomThirdPartyInvite;
        initSearchByPatternFields();
    }

    public ParticipantAdapterItem(String str, String str2, String str3, boolean z) {
        this.mDisplayName = str;
        this.mAvatarUrl = str2;
        this.mUserId = str3;
        this.mIsValid = z;
        initSearchByPatternFields();
    }

    private void initSearchByPatternFields() {
        if (!TextUtils.isEmpty(this.mDisplayName)) {
            this.mLowerCaseDisplayName = this.mDisplayName.toLowerCase(VectorLocale.INSTANCE.getApplicationLocale());
        }
        if (!TextUtils.isEmpty(this.mUserId)) {
            this.mLowerCaseMatrixId = this.mUserId.toLowerCase(VectorLocale.INSTANCE.getApplicationLocale());
        }
    }

    public String getComparisonDisplayName() {
        if (this.mComparisonDisplayName == null) {
            if (!TextUtils.isEmpty(this.mDisplayName)) {
                this.mComparisonDisplayName = this.mDisplayName;
            } else {
                this.mComparisonDisplayName = this.mUserId;
            }
            String str = this.mComparisonDisplayName;
            String str2 = "";
            if (str == null) {
                this.mComparisonDisplayName = str2;
            } else {
                this.mComparisonDisplayName = str.replaceAll(mTrimRegEx, str2);
            }
        }
        return this.mComparisonDisplayName;
    }

    public static Comparator<ParticipantAdapterItem> getComparator(MXSession mXSession) {
        final IMXStore store = mXSession.getDataHandler().getStore();
        return new Comparator<ParticipantAdapterItem>() {
            final Set<String> mUnknownUsers = new HashSet();
            final Map<String, User> mUsersMap = new HashMap();

            private int alphaComparator(String str, String str2) {
                if (str == null) {
                    return -1;
                }
                if (str2 == null) {
                    return 1;
                }
                return String.CASE_INSENSITIVE_ORDER.compare(str, str2);
            }

            private User getUser(String str) {
                if (this.mUsersMap.containsKey(str)) {
                    return (User) this.mUsersMap.get(str);
                }
                if (this.mUnknownUsers.contains(str)) {
                    return null;
                }
                User user = store.getUser(str);
                if (user == null) {
                    this.mUnknownUsers.add(str);
                } else {
                    this.mUsersMap.put(str, user);
                }
                return user;
            }

            public int compare(ParticipantAdapterItem participantAdapterItem, ParticipantAdapterItem participantAdapterItem2) {
                User user = null;
                User user2 = participantAdapterItem.mUserId != null ? getUser(participantAdapterItem.mUserId) : null;
                if (participantAdapterItem2.mUserId != null) {
                    user = getUser(participantAdapterItem2.mUserId);
                }
                String comparisonDisplayName = participantAdapterItem.getComparisonDisplayName();
                String comparisonDisplayName2 = participantAdapterItem2.getComparisonDisplayName();
                boolean z = false;
                boolean booleanValue = (user2 == null || user2.currently_active == null) ? false : user2.currently_active.booleanValue();
                if (!(user == null || user.currently_active == null)) {
                    z = user.currently_active.booleanValue();
                }
                if (user2 == null && user == null) {
                    return alphaComparator(comparisonDisplayName, comparisonDisplayName2);
                }
                int i = 1;
                if (user2 != null && user == null) {
                    return 1;
                }
                if (user2 == null && user != null) {
                    return -1;
                }
                if (booleanValue && z) {
                    return alphaComparator(comparisonDisplayName, comparisonDisplayName2);
                }
                if (booleanValue && !z) {
                    return -1;
                }
                if (!booleanValue && z) {
                    return 1;
                }
                long absoluteLastActiveAgo = user2 != null ? user2.getAbsoluteLastActiveAgo() : 0;
                long absoluteLastActiveAgo2 = user != null ? user.getAbsoluteLastActiveAgo() : 0;
                long j = absoluteLastActiveAgo - absoluteLastActiveAgo2;
                if (j == 0) {
                    return alphaComparator(comparisonDisplayName, comparisonDisplayName2);
                }
                if (0 == absoluteLastActiveAgo) {
                    return 1;
                }
                if (0 == absoluteLastActiveAgo2) {
                    return -1;
                }
                if (j <= 0) {
                    i = -1;
                }
                return i;
            }
        };
    }

    public boolean contains(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (!TextUtils.isEmpty(this.mLowerCaseDisplayName)) {
            z = this.mLowerCaseDisplayName.contains(str);
        }
        if (!z && !TextUtils.isEmpty(this.mLowerCaseMatrixId)) {
            z = this.mLowerCaseMatrixId.contains(str);
        }
        if (!z) {
            Contact contact = this.mContact;
            if (contact != null) {
                z = contact.contains(str);
            }
        }
        return z;
    }

    public boolean startsWith(String str) {
        boolean z = false;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (!TextUtils.isEmpty(this.mDisplayName)) {
            String str2 = this.mLowerCaseDisplayName;
            if (str2 != null && str2.startsWith(str)) {
                return true;
            }
            if (this.mDisplayNameComponents == null) {
                String[] split = this.mDisplayName.split(" ");
                this.mDisplayNameComponents = new ArrayList();
                if (split.length > 0) {
                    for (String trim : split) {
                        this.mDisplayNameComponents.add(trim.trim().toLowerCase(VectorLocale.INSTANCE.getApplicationLocale()));
                    }
                }
            }
            for (String startsWith : this.mDisplayNameComponents) {
                if (startsWith.startsWith(str)) {
                    return true;
                }
            }
        }
        if (!TextUtils.isEmpty(this.mLowerCaseMatrixId)) {
            String str3 = this.mLowerCaseMatrixId;
            StringBuilder sb = new StringBuilder();
            String str4 = "@";
            if (str.startsWith(str4)) {
                str4 = "";
            }
            sb.append(str4);
            sb.append(str);
            if (str3.startsWith(sb.toString())) {
                return true;
            }
        }
        Contact contact = this.mContact;
        if (contact != null && contact.startsWith(str)) {
            z = true;
        }
        return z;
    }

    public Bitmap getAvatarBitmap() {
        Contact contact = this.mContact;
        if (contact != null) {
            return contact.getThumbnail(VectorApp.getInstance());
        }
        return null;
    }

    public void displayAvatar(MXSession mXSession, ImageView imageView) {
        if (imageView != null) {
            if (getAvatarBitmap() != null) {
                imageView.setImageBitmap(getAvatarBitmap());
            } else if (TextUtils.isEmpty(this.mUserId)) {
                Context context = imageView.getContext();
                String str = this.mAvatarUrl;
                String str2 = this.mDisplayName;
                VectorUtils.loadUserAvatar(context, mXSession, imageView, str, str2, str2);
            } else {
                if (TextUtils.equals(this.mUserId, this.mDisplayName) || TextUtils.isEmpty(this.mAvatarUrl)) {
                    IMXStore store = mXSession.getDataHandler().getStore();
                    if (store != null) {
                        User user = store.getUser(this.mUserId);
                        if (user != null) {
                            if (TextUtils.equals(this.mUserId, this.mDisplayName) && !TextUtils.isEmpty(user.displayname)) {
                                this.mDisplayName = user.displayname;
                            }
                            if (this.mAvatarUrl == null) {
                                this.mAvatarUrl = user.avatar_url;
                            }
                        }
                    }
                }
                VectorUtils.loadUserAvatar(imageView.getContext(), mXSession, imageView, this.mAvatarUrl, this.mUserId, this.mDisplayName);
            }
        }
    }

    public String getUniqueDisplayName(List<String> list) {
        String str = this.mDisplayName;
        String lowerCase = str.toLowerCase(VectorLocale.INSTANCE.getApplicationLocale());
        int i = -1;
        if (list != null) {
            int indexOf = list.indexOf(lowerCase);
            if (indexOf < 0 || indexOf != list.lastIndexOf(lowerCase)) {
                i = indexOf;
            }
        }
        if (i < 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" (");
        sb.append(this.mUserId);
        sb.append(")");
        return sb.toString();
    }

    public boolean retrievePids() {
        if (this.mUserId != null && Patterns.EMAIL_ADDRESS.matcher(this.mUserId).matches()) {
            Contact contact = this.mContact;
            if (contact != null) {
                contact.refreshMatridIds();
            }
            MXID mxid = PIDsRetriever.getInstance().getMXID(this.mUserId);
            if (mxid != null) {
                this.mUserId = mxid.mMatrixId;
                return true;
            }
        }
        return false;
    }

    public static boolean isBlackedListed(String str) {
        for (int i = 0; i < mBlackedListEmails.size(); i++) {
            if (((Pattern) mBlackedListEmails.get(i)).matcher(str).matches()) {
                return true;
            }
        }
        return !Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }

    public boolean isMatrixUser() {
        if (this.mUserId != null) {
            return MXPatterns.PATTERN_CONTAIN_MATRIX_USER_IDENTIFIER.matcher(this.mUserId).matches();
        }
        return false;
    }
}
