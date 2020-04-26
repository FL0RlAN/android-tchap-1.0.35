package im.vector.contacts;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import im.vector.Matrix;
import im.vector.VectorApp;
import im.vector.contacts.Contact.MXID;
import im.vector.contacts.PIDsRetriever.PIDsRetrieverListener;
import im.vector.util.PhoneNumberUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.listeners.IMXNetworkEventListener;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.model.User;

public class ContactsManager implements OnSharedPreferenceChangeListener {
    public static final String CONTACTS_BOOK_ACCESS_KEY = "CONTACT_BOOK_ACCESS_KEY";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = ContactsManager.class.getSimpleName();
    private static ContactsManager mInstance = null;
    /* access modifiers changed from: private */
    public boolean mArePidsRetrieved = false;
    /* access modifiers changed from: private */
    public List<Contact> mContactsList = null;
    /* access modifiers changed from: private */
    public final Context mContext = VectorApp.getInstance().getApplicationContext();
    /* access modifiers changed from: private */
    public boolean mIsPopulating = false;
    /* access modifiers changed from: private */
    public boolean mIsRetrievingPids = false;
    /* access modifiers changed from: private */
    public final List<ContactsManagerListener> mListeners = new ArrayList();
    /* access modifiers changed from: private */
    public final IMXNetworkEventListener mNetworkConnectivityReceiver = new IMXNetworkEventListener() {
        public void onNetworkConnectionUpdate(boolean z) {
            if (z && ContactsManager.this.mRetryPIDsRetrievalOnConnect) {
                ContactsManager.this.retrievePids();
            }
        }
    };
    /* access modifiers changed from: private */
    public final PIDsRetrieverListener mPIDsRetrieverListener = new PIDsRetrieverListener() {
        /* access modifiers changed from: private */
        public void onContactPresenceUpdate(Contact contact, MXID mxid) {
            for (ContactsManagerListener onContactPresenceUpdate : ContactsManager.this.mListeners) {
                try {
                    onContactPresenceUpdate.onContactPresenceUpdate(contact, mxid.mMatrixId);
                } catch (Exception e) {
                    String access$200 = ContactsManager.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onContactPresenceUpdate failed ");
                    sb.append(e.getMessage());
                    Log.e(access$200, sb.toString(), e);
                }
            }
        }

        private void onPIDsUpdate() {
            for (ContactsManagerListener onPIDsUpdate : ContactsManager.this.mListeners) {
                try {
                    onPIDsUpdate.onPIDsUpdate();
                } catch (Exception e) {
                    String access$200 = ContactsManager.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onPIDsUpdate failed ");
                    sb.append(e.getMessage());
                    Log.e(access$200, sb.toString(), e);
                }
            }
        }

        public void onFailure(String str) {
            if (!ContactsManager.this.mIsRetrievingPids) {
                Log.d(ContactsManager.LOG_TAG, "## Retrieve a PIDS failed whereas it is not expected");
                return;
            }
            ContactsManager.this.mIsRetrievingPids = false;
            ContactsManager.this.mArePidsRetrieved = false;
            ContactsManager.this.mRetryPIDsRetrievalOnConnect = true;
            Log.d(ContactsManager.LOG_TAG, "## fail to retrieve the PIDs");
            onPIDsUpdate();
        }

        public void onSuccess(String str) {
            if (!ContactsManager.this.mIsRetrievingPids) {
                Log.d(ContactsManager.LOG_TAG, "## Retrieve a PIDS succeeded whereas it is not expected");
                return;
            }
            Log.d(ContactsManager.LOG_TAG, "## Retrieve IPDs successfully");
            ContactsManager.this.mRetryPIDsRetrievalOnConnect = false;
            ContactsManager.this.mIsRetrievingPids = false;
            ContactsManager.this.mArePidsRetrieved = true;
            onPIDsUpdate();
            MXSession session = Matrix.getInstance(VectorApp.getInstance().getApplicationContext()).getSession(str);
            if (!(session == null || !session.isAlive() || ContactsManager.this.mContactsList == null)) {
                for (final Contact contact : ContactsManager.this.mContactsList) {
                    for (String mxid : contact.getMatrixIdMediums()) {
                        final MXID mxid2 = contact.getMXID(mxid);
                        mxid2.mUser = session.getDataHandler().getUser(mxid2.mMatrixId);
                        if (mxid2.mUser == null) {
                            session.getPresenceApiClient().getPresence(mxid2.mMatrixId, new ApiCallback<User>() {
                                public void onSuccess(User user) {
                                    String access$200 = ContactsManager.LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("retrieve the presence of ");
                                    sb.append(mxid2.mMatrixId);
                                    sb.append(" :");
                                    sb.append(user);
                                    Log.d(access$200, sb.toString());
                                    MXID mxid = mxid2;
                                    mxid.mUser = user;
                                    AnonymousClass2.this.onContactPresenceUpdate(contact, mxid);
                                }

                                private void onError(String str) {
                                    String access$200 = ContactsManager.LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("cannot retrieve the presence of ");
                                    sb.append(mxid2.mMatrixId);
                                    sb.append(" :");
                                    sb.append(str);
                                    Log.e(access$200, sb.toString());
                                }

                                public void onNetworkError(Exception exc) {
                                    onError(exc.getMessage());
                                }

                                public void onMatrixError(MatrixError matrixError) {
                                    onError(matrixError.getMessage());
                                }

                                public void onUnexpectedError(Exception exc) {
                                    onError(exc.getMessage());
                                }
                            });
                        }
                    }
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mRetryPIDsRetrievalOnConnect = false;

    public interface ContactsManagerListener {
        void onContactPresenceUpdate(Contact contact, String str);

        void onPIDsUpdate();

        void onRefresh();
    }

    public static ContactsManager getInstance() {
        if (mInstance == null) {
            mInstance = new ContactsManager();
        }
        return mInstance;
    }

    private ContactsManager() {
        PreferenceManager.getDefaultSharedPreferences(this.mContext).registerOnSharedPreferenceChangeListener(this);
    }

    public int getLocalContactsSnapshotSession() {
        List<Contact> list = this.mContactsList;
        if (list != null) {
            return list.hashCode();
        }
        return 0;
    }

    public Collection<Contact> getLocalContactsSnapshot() {
        return this.mContactsList;
    }

    public boolean didPopulateLocalContacts() {
        boolean z;
        boolean z2;
        synchronized (LOG_TAG) {
            z = this.mContactsList != null;
            z2 = this.mIsPopulating;
        }
        if (!z && !z2) {
            refreshLocalContactsSnapshot();
        }
        return z;
    }

    public void reset() {
        this.mListeners.clear();
        clearSnapshot();
    }

    public void clearSnapshot() {
        synchronized (LOG_TAG) {
            this.mContactsList = null;
        }
        MXSession defaultSession = Matrix.getInstance(VectorApp.getInstance()).getDefaultSession();
        if (defaultSession != null) {
            defaultSession.getNetworkConnectivityReceiver().removeEventListener(this.mNetworkConnectivityReceiver);
        }
    }

    /* access modifiers changed from: private */
    public void onCountryCodeUpdate() {
        synchronized (LOG_TAG) {
            if (this.mContactsList != null) {
                for (Contact onCountryCodeUpdate : this.mContactsList) {
                    onCountryCodeUpdate.onCountryCodeUpdate();
                }
            }
        }
        this.mIsRetrievingPids = false;
        this.mArePidsRetrieved = false;
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        if (TextUtils.equals(str, PhoneNumberUtils.COUNTRY_CODE_PREF_KEY)) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    ContactsManager.this.onCountryCodeUpdate();
                }
            });
        }
    }

    public void addListener(ContactsManagerListener contactsManagerListener) {
        if (contactsManagerListener != null) {
            this.mListeners.add(contactsManagerListener);
        }
    }

    public void removeListener(ContactsManagerListener contactsManagerListener) {
        if (contactsManagerListener != null) {
            this.mListeners.remove(contactsManagerListener);
        }
    }

    public boolean arePIDsRetrieved() {
        return this.mArePidsRetrieved;
    }

    public void retrievePids() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                if (ContactsManager.this.mIsRetrievingPids) {
                    Log.d(ContactsManager.LOG_TAG, "## retrievePids() : already in progress");
                } else if (ContactsManager.this.mArePidsRetrieved) {
                    Log.d(ContactsManager.LOG_TAG, "## retrievePids() : already done");
                } else {
                    Log.d(ContactsManager.LOG_TAG, "## retrievePids() : Start search");
                    ContactsManager.this.mIsRetrievingPids = true;
                    PIDsRetriever.getInstance().retrieveMatrixIds(VectorApp.getInstance(), ContactsManager.this.mContactsList, false);
                }
            }
        });
    }

    public void refreshLocalContactsSnapshot() {
        boolean z;
        synchronized (LOG_TAG) {
            z = this.mIsPopulating;
        }
        if (!z) {
            synchronized (LOG_TAG) {
                this.mIsPopulating = true;
            }
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    Cursor cursor;
                    Cursor cursor2;
                    long currentTimeMillis = System.currentTimeMillis();
                    ContentResolver contentResolver = ContactsManager.this.mContext.getContentResolver();
                    HashMap hashMap = new HashMap();
                    if (ContactsManager.this.isContactBookAccessAllowed()) {
                        Log.d(ContactsManager.LOG_TAG, "## refreshLocalContactsSnapshot() starts");
                        Cursor cursor3 = null;
                        try {
                            ContentResolver contentResolver2 = contentResolver;
                            cursor = contentResolver2.query(Data.CONTENT_URI, new String[]{"display_name", "contact_id", "photo_thumb_uri"}, "mimetype = ?", new String[]{"vnd.android.cursor.item/name"}, null);
                        } catch (Exception e) {
                            String access$200 = ContactsManager.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## refreshLocalContactsSnapshot(): Exception - Contact names query Msg=");
                            sb.append(e.getMessage());
                            Log.e(access$200, sb.toString(), e);
                            cursor = null;
                        }
                        if (cursor != null) {
                            while (cursor.moveToNext()) {
                                try {
                                    String string = cursor.getString(cursor.getColumnIndex("display_name"));
                                    String string2 = cursor.getString(cursor.getColumnIndex("contact_id"));
                                    String string3 = cursor.getString(cursor.getColumnIndex("photo_thumb_uri"));
                                    if (string2 != null) {
                                        Contact contact = (Contact) hashMap.get(string2);
                                        if (contact == null) {
                                            contact = new Contact(string2);
                                            hashMap.put(string2, contact);
                                        }
                                        if (string != null) {
                                            contact.setDisplayName(string);
                                        }
                                        if (string3 != null) {
                                            contact.setThumbnailUri(string3);
                                        }
                                    }
                                } catch (Exception e2) {
                                    String access$2002 = ContactsManager.LOG_TAG;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("## refreshLocalContactsSnapshot(): Exception - Contact names query2 Msg=");
                                    sb2.append(e2.getMessage());
                                    Log.e(access$2002, sb2.toString(), e2);
                                }
                            }
                            cursor.close();
                        }
                        try {
                            cursor2 = contentResolver.query(Phone.CONTENT_URI, new String[]{"data1", "data4", "contact_id"}, null, null, null);
                        } catch (Exception e3) {
                            String access$2003 = ContactsManager.LOG_TAG;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("## refreshLocalContactsSnapshot(): Exception - Phone numbers query Msg=");
                            sb3.append(e3.getMessage());
                            Log.e(access$2003, sb3.toString(), e3);
                            cursor2 = null;
                        }
                        if (cursor2 != null) {
                            while (cursor2.moveToNext()) {
                                try {
                                    String string4 = cursor2.getString(cursor2.getColumnIndex("data1"));
                                    String string5 = cursor2.getString(cursor2.getColumnIndex("data4"));
                                    if (!TextUtils.isEmpty(string4)) {
                                        String string6 = cursor2.getString(cursor2.getColumnIndex("contact_id"));
                                        if (string6 != null) {
                                            Contact contact2 = (Contact) hashMap.get(string6);
                                            if (contact2 == null) {
                                                contact2 = new Contact(string6);
                                                hashMap.put(string6, contact2);
                                            }
                                            contact2.addPhoneNumber(string4, string5);
                                        }
                                    }
                                } catch (Exception e4) {
                                    String access$2004 = ContactsManager.LOG_TAG;
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("## refreshLocalContactsSnapshot(): Exception - Phone numbers query2 Msg=");
                                    sb4.append(e4.getMessage());
                                    Log.e(access$2004, sb4.toString(), e4);
                                }
                            }
                            cursor2.close();
                        }
                        try {
                            cursor3 = contentResolver.query(Email.CONTENT_URI, new String[]{"data1", "contact_id"}, null, null, null);
                        } catch (Exception e5) {
                            String access$2005 = ContactsManager.LOG_TAG;
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("## refreshLocalContactsSnapshot(): Exception - Emails query Msg=");
                            sb5.append(e5.getMessage());
                            Log.e(access$2005, sb5.toString(), e5);
                        }
                        if (cursor3 != null) {
                            while (cursor3.moveToNext()) {
                                try {
                                    String string7 = cursor3.getString(cursor3.getColumnIndex("data1"));
                                    if (!TextUtils.isEmpty(string7)) {
                                        String string8 = cursor3.getString(cursor3.getColumnIndex("contact_id"));
                                        if (string8 != null) {
                                            Contact contact3 = (Contact) hashMap.get(string8);
                                            if (contact3 == null) {
                                                contact3 = new Contact(string8);
                                                hashMap.put(string8, contact3);
                                            }
                                            contact3.addEmailAdress(string7);
                                        }
                                    }
                                } catch (Exception e6) {
                                    String access$2006 = ContactsManager.LOG_TAG;
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append("## refreshLocalContactsSnapshot(): Exception - Emails query2 Msg=");
                                    sb6.append(e6.getMessage());
                                    Log.e(access$2006, sb6.toString(), e6);
                                }
                            }
                            cursor3.close();
                        }
                    } else {
                        Log.d(ContactsManager.LOG_TAG, "## refreshLocalContactsSnapshot() : permission to read contacts is not granted");
                    }
                    synchronized (ContactsManager.LOG_TAG) {
                        ContactsManager.this.mContactsList = new ArrayList(hashMap.values());
                        ContactsManager.this.mIsPopulating = false;
                    }
                    if (ContactsManager.this.mContactsList == null) {
                        Log.d(ContactsManager.LOG_TAG, "## ## refreshLocalContactsSnapshot() : the contacts list has been cleared while processing it");
                        ContactsManager.this.mIsRetrievingPids = false;
                        ContactsManager.this.mArePidsRetrieved = false;
                        return;
                    }
                    long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                    String access$2007 = ContactsManager.LOG_TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("## refreshLocalContactsSnapshot(): retrieve ");
                    sb7.append(ContactsManager.this.mContactsList.size());
                    sb7.append(" contacts in ");
                    sb7.append(currentTimeMillis2);
                    sb7.append(" ms");
                    Log.d(access$2007, sb7.toString());
                    PIDsRetriever.getInstance().setPIDsRetrieverListener(ContactsManager.this.mPIDsRetrieverListener);
                    MXSession defaultSession = Matrix.getInstance(VectorApp.getInstance()).getDefaultSession();
                    if (defaultSession != null) {
                        defaultSession.getNetworkConnectivityReceiver().addEventListener(ContactsManager.this.mNetworkConnectivityReceiver);
                        ContactsManager.this.mIsRetrievingPids = false;
                        ContactsManager.this.mArePidsRetrieved = false;
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            for (ContactsManagerListener onRefresh : ContactsManager.this.mListeners) {
                                try {
                                    onRefresh.onRefresh();
                                } catch (Exception e) {
                                    String access$200 = ContactsManager.LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("refreshLocalContactsSnapshot : onRefresh failed");
                                    sb.append(e.getMessage());
                                    Log.e(access$200, sb.toString(), e);
                                }
                            }
                        }
                    });
                }
            });
            thread.setPriority(1);
            thread.start();
        }
    }

    public boolean isContactBookAccessRequested() {
        if (VERSION.SDK_INT < 23) {
            return PreferenceManager.getDefaultSharedPreferences(this.mContext).contains(CONTACTS_BOOK_ACCESS_KEY);
        }
        return ContextCompat.checkSelfPermission(this.mContext, "android.permission.READ_CONTACTS") == 0;
    }

    public void setIsContactBookAccessAllowed(boolean z) {
        if (VERSION.SDK_INT < 23) {
            PreferenceManager.getDefaultSharedPreferences(this.mContext).edit().putBoolean(CONTACTS_BOOK_ACCESS_KEY, z).apply();
        }
        this.mIsRetrievingPids = false;
        this.mArePidsRetrieved = false;
    }

    public boolean isContactBookAccessAllowed() {
        boolean z = false;
        if (VERSION.SDK_INT < 23) {
            return PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean(CONTACTS_BOOK_ACCESS_KEY, false);
        }
        if (ContextCompat.checkSelfPermission(this.mContext, "android.permission.READ_CONTACTS") == 0) {
            z = true;
        }
        return z;
    }
}
