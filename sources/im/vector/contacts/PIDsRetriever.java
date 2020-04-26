package im.vector.contacts;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Patterns;
import fr.gouv.tchap.sdk.rest.client.TchapThirdPidRestClient;
import im.vector.Matrix;
import im.vector.contacts.Contact.MXID;
import im.vector.contacts.Contact.PhoneNumber;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.model.pid.ThreePid;

public class PIDsRetriever {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = PIDsRetriever.class.getSimpleName();
    private static PIDsRetriever mPIDsRetriever = null;
    /* access modifiers changed from: private */
    public PIDsRetrieverListener mListener = null;
    /* access modifiers changed from: private */
    public final Map<String, MXID> mMatrixIdsByMedium = new HashMap();

    public interface PIDsRetrieverListener {
        void onFailure(String str);

        void onSuccess(String str);
    }

    public static PIDsRetriever getInstance() {
        if (mPIDsRetriever == null) {
            mPIDsRetriever = new PIDsRetriever();
        }
        return mPIDsRetriever;
    }

    public void setPIDsRetrieverListener(PIDsRetrieverListener pIDsRetrieverListener) {
        this.mListener = pIDsRetrieverListener;
    }

    public void onAppBackgrounded() {
        this.mMatrixIdsByMedium.clear();
    }

    public void reset() {
        this.mMatrixIdsByMedium.clear();
        this.mListener = null;
    }

    public MXID getMXID(String str) {
        if (str == null || !this.mMatrixIdsByMedium.containsKey(str)) {
            return null;
        }
        MXID mxid = (MXID) this.mMatrixIdsByMedium.get(str);
        if (mxid.mMatrixId == null) {
            return null;
        }
        return mxid;
    }

    /* access modifiers changed from: private */
    public Set<String> retrieveMatrixIds(List<Contact> list) {
        HashSet hashSet = new HashSet();
        for (Contact contact : list) {
            for (String str : contact.getEmails()) {
                if (this.mMatrixIdsByMedium.containsKey(str)) {
                    MXID mxid = (MXID) this.mMatrixIdsByMedium.get(str);
                    if (mxid != null) {
                        contact.put(str, mxid);
                    }
                } else {
                    hashSet.add(str);
                }
            }
            for (PhoneNumber phoneNumber : contact.getPhonenumbers()) {
                if (this.mMatrixIdsByMedium.containsKey(phoneNumber.mMsisdnPhoneNumber)) {
                    MXID mxid2 = (MXID) this.mMatrixIdsByMedium.get(phoneNumber.mMsisdnPhoneNumber);
                    if (mxid2 != null) {
                        contact.put(phoneNumber.mMsisdnPhoneNumber, mxid2);
                    }
                } else {
                    hashSet.add(phoneNumber.mMsisdnPhoneNumber);
                }
            }
        }
        hashSet.remove(null);
        return hashSet;
    }

    public void retrieveMatrixIds(final Context context, final List<Contact> list, boolean z) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("retrieveMatrixIds starts for ");
        sb.append(list == null ? 0 : list.size());
        sb.append(" contacts");
        Log.d(str, sb.toString());
        if (list == null || list.size() == 0) {
            if (this.mListener != null) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        MXSession defaultSession = Matrix.getInstance(context.getApplicationContext()).getDefaultSession();
                        if (defaultSession != null) {
                            PIDsRetriever.this.mListener.onSuccess(defaultSession.getMyUserId());
                        }
                    }
                });
            }
            return;
        }
        Set<String> retrieveMatrixIds = retrieveMatrixIds(list);
        if (!z && !retrieveMatrixIds.isEmpty()) {
            HashMap hashMap = new HashMap();
            for (String str2 : retrieveMatrixIds) {
                if (str2 != null) {
                    if (Patterns.EMAIL_ADDRESS.matcher(str2).matches()) {
                        hashMap.put(str2, "email");
                    } else {
                        hashMap.put(str2, ThreePid.MEDIUM_MSISDN);
                    }
                }
            }
            final ArrayList arrayList = new ArrayList(hashMap.keySet());
            ArrayList arrayList2 = new ArrayList(hashMap.values());
            for (MXSession mXSession : Matrix.getInstance(context.getApplicationContext()).getSessions()) {
                final String str3 = mXSession.getCredentials().userId;
                new TchapThirdPidRestClient(mXSession.getHomeServerConfig()).bulkLookup(arrayList, arrayList2, new ApiCallback<List<String>>() {
                    public void onSuccess(List<String> list) {
                        String access$100 = PIDsRetriever.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("bulkLookup success ");
                        sb.append(list.size());
                        Log.d(access$100, sb.toString());
                        for (int i = 0; i < arrayList.size(); i++) {
                            String str = (String) arrayList.get(i);
                            String str2 = (String) list.get(i);
                            if (!TextUtils.isEmpty(str2)) {
                                PIDsRetriever.this.mMatrixIdsByMedium.put(str, new MXID(str2, str3));
                            }
                        }
                        PIDsRetriever.this.retrieveMatrixIds(list);
                        if (PIDsRetriever.this.mListener != null) {
                            PIDsRetriever.this.mListener.onSuccess(str3);
                        }
                    }

                    private void onError(String str) {
                        String access$100 = PIDsRetriever.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## bulkLookup() : failed ");
                        sb.append(str);
                        Log.e(access$100, sb.toString());
                        if (PIDsRetriever.this.mListener != null) {
                            PIDsRetriever.this.mListener.onFailure(str3);
                        }
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
