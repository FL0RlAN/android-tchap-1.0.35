package im.vector.contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import im.vector.VectorApp;
import im.vector.settings.VectorLocale;
import im.vector.util.PhoneNumberUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.rest.model.User;

public class Contact implements Serializable {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = Contact.class.getSimpleName();
    private String mContactId;
    private String mDisplayName;
    private final List<String> mEmails = new ArrayList();
    private final Map<String, MXID> mMXIDsByElement = new HashMap();
    private final List<PhoneNumber> mPhoneNumbers = new ArrayList();
    private transient Bitmap mThumbnail;
    private String mThumbnailUri;

    public static class MXID implements Serializable {
        public final String mAccountId;
        public final String mMatrixId;
        public User mUser;

        public MXID(String str, String str2) {
            if (str == null) {
                str = "";
            }
            this.mMatrixId = str;
            this.mAccountId = str2;
            this.mUser = null;
        }
    }

    public static class PhoneNumber implements Serializable {
        public final String mCleanedPhoneNumber;
        public final String mE164PhoneNumber;
        public String mMsisdnPhoneNumber;
        public final String mRawPhoneNumber;

        public PhoneNumber(String str, String str2) {
            this.mRawPhoneNumber = str;
            this.mCleanedPhoneNumber = str.replaceAll("[\\D]", "");
            if (!TextUtils.isEmpty(str2)) {
                if (str2.startsWith("+")) {
                    str2 = str2.substring(1);
                }
                this.mE164PhoneNumber = str2;
                this.mMsisdnPhoneNumber = str2;
                return;
            }
            this.mE164PhoneNumber = null;
            refreshE164PhoneNumber();
        }

        public void refreshE164PhoneNumber() {
            if (TextUtils.isEmpty(this.mE164PhoneNumber)) {
                this.mMsisdnPhoneNumber = PhoneNumberUtils.getE164format((Context) VectorApp.getInstance(), this.mRawPhoneNumber);
                if (TextUtils.isEmpty(this.mMsisdnPhoneNumber)) {
                    this.mMsisdnPhoneNumber = this.mCleanedPhoneNumber;
                }
            }
            String access$000 = Contact.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## refreshE164PhoneNumber ");
            sb.append(this.mMsisdnPhoneNumber);
            Log.d(access$000, sb.toString());
        }

        public boolean startsWith(String str) {
            if (!this.mRawPhoneNumber.startsWith(str)) {
                String str2 = this.mE164PhoneNumber;
                if ((str2 == null || !str2.startsWith(str)) && !this.mMsisdnPhoneNumber.startsWith(str) && !this.mCleanedPhoneNumber.startsWith(str)) {
                    return false;
                }
            }
            return true;
        }
    }

    public Contact(String str) {
        String str2 = "";
        this.mContactId = str2;
        this.mDisplayName = str2;
        if (str != null) {
            this.mContactId = str;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(System.currentTimeMillis());
        this.mContactId = sb.toString();
    }

    public List<String> getEmails() {
        return this.mEmails;
    }

    public void addEmailAdress(String str) {
        String lowerCase = str.toLowerCase(Locale.ROOT);
        if (this.mEmails.indexOf(lowerCase) < 0) {
            this.mEmails.add(lowerCase);
            MXID mxid = PIDsRetriever.getInstance().getMXID(lowerCase);
            if (mxid != null) {
                this.mMXIDsByElement.put(lowerCase, mxid);
            }
        }
    }

    public List<PhoneNumber> getPhonenumbers() {
        return this.mPhoneNumbers;
    }

    public void addPhoneNumber(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            PhoneNumber phoneNumber = new PhoneNumber(str, str2);
            this.mPhoneNumbers.add(phoneNumber);
            MXID mxid = PIDsRetriever.getInstance().getMXID(phoneNumber.mMsisdnPhoneNumber);
            if (mxid != null) {
                this.mMXIDsByElement.put(phoneNumber.mMsisdnPhoneNumber, mxid);
            }
        }
    }

    public void onCountryCodeUpdate() {
        List<PhoneNumber> list = this.mPhoneNumbers;
        if (list != null) {
            for (PhoneNumber refreshE164PhoneNumber : list) {
                refreshE164PhoneNumber.refreshE164PhoneNumber();
            }
        }
    }

    public String getThumbnailUri() {
        return this.mThumbnailUri;
    }

    public void setThumbnailUri(String str) {
        this.mThumbnailUri = str;
    }

    public void refreshMatridIds() {
        this.mMXIDsByElement.clear();
        PIDsRetriever instance = PIDsRetriever.getInstance();
        for (String str : getEmails()) {
            MXID mxid = instance.getMXID(str);
            if (mxid != null) {
                put(str, mxid);
            }
        }
        for (PhoneNumber phoneNumber : getPhonenumbers()) {
            MXID mxid2 = instance.getMXID(phoneNumber.mMsisdnPhoneNumber);
            if (mxid2 != null) {
                put(phoneNumber.mMsisdnPhoneNumber, mxid2);
            }
        }
    }

    public void put(String str, MXID mxid) {
        if (str != null && mxid != null && !TextUtils.isEmpty(mxid.mMatrixId)) {
            this.mMXIDsByElement.put(str, mxid);
        }
    }

    public boolean contains(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        boolean contains = !TextUtils.isEmpty(this.mDisplayName) ? this.mDisplayName.toLowerCase(VectorLocale.INSTANCE.getApplicationLocale()).contains(str) : false;
        if (!contains) {
            for (String contains2 : this.mEmails) {
                contains |= contains2.contains(str);
            }
        }
        if (!contains) {
            for (PhoneNumber phoneNumber : this.mPhoneNumbers) {
                contains |= phoneNumber.mMsisdnPhoneNumber.toLowerCase(VectorLocale.INSTANCE.getApplicationLocale()).contains(str) || phoneNumber.mRawPhoneNumber.toLowerCase(VectorLocale.INSTANCE.getApplicationLocale()).contains(str) || (phoneNumber.mE164PhoneNumber != null && phoneNumber.mE164PhoneNumber.toLowerCase(VectorLocale.INSTANCE.getApplicationLocale()).contains(str));
            }
        }
        return contains;
    }

    public boolean startsWith(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        ArrayList<MXID> arrayList = new ArrayList<>();
        for (String str2 : this.mEmails) {
            if (str2.startsWith(str)) {
                return true;
            }
            Map<String, MXID> map = this.mMXIDsByElement;
            if (map != null && map.containsKey(str2)) {
                arrayList.add(this.mMXIDsByElement.get(str2));
            }
        }
        String replaceAll = str.replaceAll("\\s", "");
        if (replaceAll.startsWith("+")) {
            replaceAll = replaceAll.substring(1);
        }
        for (PhoneNumber phoneNumber : this.mPhoneNumbers) {
            if (phoneNumber.startsWith(replaceAll)) {
                return true;
            }
            Map<String, MXID> map2 = this.mMXIDsByElement;
            if (map2 != null && map2.containsKey(phoneNumber.mMsisdnPhoneNumber)) {
                arrayList.add(this.mMXIDsByElement.get(phoneNumber.mMsisdnPhoneNumber));
            }
        }
        for (MXID mxid : arrayList) {
            if (mxid.mMatrixId != null) {
                String str3 = mxid.mMatrixId;
                StringBuilder sb = new StringBuilder();
                sb.append("@");
                sb.append(str);
                if (str3.startsWith(sb.toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<String> getMatrixIdMediums() {
        Map<String, MXID> map = this.mMXIDsByElement;
        return map != null ? map.keySet() : Collections.emptySet();
    }

    public MXID getMXID(String str) {
        if (!TextUtils.isEmpty(str)) {
            return (MXID) this.mMXIDsByElement.get(str);
        }
        return null;
    }

    public void setDisplayName(String str) {
        this.mDisplayName = str;
    }

    public String getDisplayName() {
        String str = this.mDisplayName;
        if (TextUtils.isEmpty(str)) {
            for (String str2 : this.mEmails) {
                if (!TextUtils.isEmpty(str2)) {
                    return str2;
                }
            }
        }
        if (TextUtils.isEmpty(str)) {
            Iterator it = this.mPhoneNumbers.iterator();
            if (it.hasNext()) {
                str = ((PhoneNumber) it.next()).mRawPhoneNumber;
            }
        }
        return str;
    }

    public String getContactId() {
        return this.mContactId;
    }

    public Bitmap getThumbnail(Context context) {
        if (this.mThumbnail == null && this.mThumbnailUri != null) {
            try {
                this.mThumbnail = Media.getBitmap(context.getContentResolver(), Uri.parse(this.mThumbnailUri));
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("getThumbnail ");
                sb.append(e.getLocalizedMessage());
                Log.e(str, sb.toString(), e);
            }
        }
        return this.mThumbnail;
    }
}
