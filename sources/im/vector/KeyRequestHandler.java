package im.vector;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import fr.gouv.tchap.a.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.IncomingRoomKeyRequest;
import org.matrix.androidsdk.crypto.IncomingRoomKeyRequestCancellation;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;

public class KeyRequestHandler {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = KeyRequestHandler.class.getSimpleName();
    private static KeyRequestHandler mInstance = null;
    private AlertDialog mAlertDialog;
    /* access modifiers changed from: private */
    public String mCurrentDevice;
    /* access modifiers changed from: private */
    public String mCurrentUser;
    private final Map<String, Map<String, List<IncomingRoomKeyRequest>>> mPendingKeyRequests = new HashMap();

    public static KeyRequestHandler getSharedInstance() {
        if (mInstance == null) {
            mInstance = new KeyRequestHandler();
        }
        return mInstance;
    }

    private KeyRequestHandler() {
    }

    public void handleKeyRequest(IncomingRoomKeyRequest incomingRoomKeyRequest) {
        String str = incomingRoomKeyRequest.mUserId;
        String str2 = incomingRoomKeyRequest.mDeviceId;
        String str3 = incomingRoomKeyRequest.mRequestId;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            Log.e(LOG_TAG, "## handleKeyRequest() : invalid parameters");
            return;
        }
        if (!this.mPendingKeyRequests.containsKey(str)) {
            this.mPendingKeyRequests.put(str, new HashMap());
        }
        if (!((Map) this.mPendingKeyRequests.get(str)).containsKey(str2)) {
            ((Map) this.mPendingKeyRequests.get(str)).put(str2, new ArrayList());
        }
        List list = (List) ((Map) this.mPendingKeyRequests.get(str)).get(str2);
        if (list.contains(incomingRoomKeyRequest)) {
            Log.d(LOG_TAG, "## handleKeyRequest() : Already have this key request, ignoring");
            return;
        }
        list.add(incomingRoomKeyRequest);
        if (this.mAlertDialog != null) {
            Log.d(LOG_TAG, "## handleKeyRequest() : Key request, but we already have a dialog open");
        } else {
            processNextRequest();
        }
    }

    public void handleKeyRequestCancellation(IncomingRoomKeyRequestCancellation incomingRoomKeyRequestCancellation) {
        String str = incomingRoomKeyRequestCancellation.mUserId;
        String str2 = incomingRoomKeyRequestCancellation.mDeviceId;
        String str3 = incomingRoomKeyRequestCancellation.mRequestId;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            Log.e(LOG_TAG, "## handleKeyRequestCancellation() : invalid parameters");
        } else if (TextUtils.equals(str, this.mCurrentUser) && TextUtils.equals(str2, this.mCurrentDevice)) {
            Log.d(LOG_TAG, "## handleKeyRequestCancellation() : room key request cancellation for the user we currently have a dialog open for ");
            AlertDialog alertDialog = this.mAlertDialog;
            if (alertDialog != null) {
                alertDialog.cancel();
            }
        } else if (this.mPendingKeyRequests.containsKey(str)) {
            List list = (List) ((Map) this.mPendingKeyRequests.get(str)).get(str2);
            if (list != null && list.contains(incomingRoomKeyRequestCancellation)) {
                Log.d(LOG_TAG, "## handleKeyRequestCancellation() : Forgetting room key request");
                list.remove(incomingRoomKeyRequestCancellation);
                if (list.isEmpty()) {
                    ((Map) this.mPendingKeyRequests.get(str)).remove(str2);
                }
                if (((Map) this.mPendingKeyRequests.get(str)).isEmpty()) {
                    this.mPendingKeyRequests.remove(str);
                }
            }
        }
    }

    public void processNextRequest() {
        if (this.mCurrentUser != null || this.mCurrentDevice != null) {
            Log.d(LOG_TAG, "## processNextRequest() : nothing to do");
        } else if (!this.mPendingKeyRequests.isEmpty()) {
            String str = (String) this.mPendingKeyRequests.keySet().iterator().next();
            if (!((Map) this.mPendingKeyRequests.get(str)).isEmpty()) {
                String str2 = (String) ((Map) this.mPendingKeyRequests.get(str)).keySet().iterator().next();
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## processNextRequest() : Starting KeyShareDialog for ");
                sb.append(str);
                sb.append(":");
                sb.append(str2);
                Log.d(str3, sb.toString());
                this.mCurrentUser = str;
                this.mCurrentDevice = str2;
                initKeyShareDialog();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onDisplayKeyShareDialogClose(boolean z, boolean z2) {
        if (this.mPendingKeyRequests.containsKey(this.mCurrentUser)) {
            List<IncomingRoomKeyRequest> list = (List) ((Map) this.mPendingKeyRequests.get(this.mCurrentUser)).get(this.mCurrentDevice);
            if (z) {
                for (IncomingRoomKeyRequest incomingRoomKeyRequest : list) {
                    if (incomingRoomKeyRequest.mShare != null) {
                        try {
                            incomingRoomKeyRequest.mShare.run();
                        } catch (Exception e) {
                            String str = LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## onDisplayKeyShareDialogClose() : req.mShare failed ");
                            sb.append(e.getMessage());
                            Log.e(str, sb.toString(), e);
                        }
                    }
                }
            } else if (z2) {
                for (IncomingRoomKeyRequest incomingRoomKeyRequest2 : list) {
                    if (incomingRoomKeyRequest2.mIgnore != null) {
                        try {
                            incomingRoomKeyRequest2.mIgnore.run();
                        } catch (Exception e2) {
                            String str2 = LOG_TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("## onDisplayKeyShareDialogClose() : req.mIgnore failed ");
                            sb2.append(e2.getMessage());
                            Log.e(str2, sb2.toString(), e2);
                        }
                    }
                }
            }
            ((Map) this.mPendingKeyRequests.get(this.mCurrentUser)).remove(this.mCurrentDevice);
            if (((Map) this.mPendingKeyRequests.get(this.mCurrentUser)).isEmpty()) {
                this.mPendingKeyRequests.remove(this.mCurrentUser);
            }
        }
        this.mCurrentUser = null;
        this.mCurrentDevice = null;
        this.mAlertDialog = null;
        processNextRequest();
    }

    private void initKeyShareDialog() {
        if (VectorApp.getCurrentActivity() == null) {
            this.mCurrentUser = null;
            this.mCurrentDevice = null;
            return;
        }
        final MXSession defaultSession = Matrix.getInstance(VectorApp.getInstance()).getDefaultSession();
        defaultSession.getCrypto().getDeviceList().downloadKeys(Arrays.asList(new String[]{this.mCurrentUser}), false, new ApiCallback<MXUsersDevicesMap<MXDeviceInfo>>() {
            public void onSuccess(MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
                final MXDeviceInfo mXDeviceInfo = (MXDeviceInfo) mXUsersDevicesMap.getObject(KeyRequestHandler.this.mCurrentDevice, KeyRequestHandler.this.mCurrentUser);
                if (mXDeviceInfo == null) {
                    String access$200 = KeyRequestHandler.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## displayKeyShareDialog() : No details found for device ");
                    sb.append(KeyRequestHandler.this.mCurrentUser);
                    sb.append(":");
                    sb.append(KeyRequestHandler.this.mCurrentDevice);
                    Log.e(access$200, sb.toString());
                    KeyRequestHandler.this.onDisplayKeyShareDialogClose(false, false);
                    return;
                }
                if (mXDeviceInfo.isUnknown()) {
                    defaultSession.getCrypto().setDeviceVerification(0, KeyRequestHandler.this.mCurrentDevice, KeyRequestHandler.this.mCurrentUser, new SimpleApiCallback<Void>() {
                        public void onSuccess(Void voidR) {
                            KeyRequestHandler.this.displayKeyShareDialog(defaultSession, mXDeviceInfo, true);
                        }
                    });
                } else {
                    KeyRequestHandler.this.displayKeyShareDialog(defaultSession, mXDeviceInfo, false);
                }
            }

            private void onError(String str) {
                String access$200 = KeyRequestHandler.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## displayKeyShareDialog : downloadKeys failed ");
                sb.append(str);
                Log.e(access$200, sb.toString());
                KeyRequestHandler.this.onDisplayKeyShareDialogClose(false, false);
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

    /* access modifiers changed from: private */
    public void displayKeyShareDialog(MXSession mXSession, MXDeviceInfo mXDeviceInfo, boolean z) {
        String str;
        if (VectorApp.getCurrentActivity() == null) {
            this.mCurrentUser = null;
            this.mCurrentDevice = null;
            return;
        }
        Activity currentActivity = VectorApp.getCurrentActivity();
        String displayName = TextUtils.isEmpty(mXDeviceInfo.displayName()) ? mXDeviceInfo.deviceId : mXDeviceInfo.displayName();
        if (z) {
            str = currentActivity.getString(R.string.you_added_a_new_device, new Object[]{displayName});
        } else {
            str = currentActivity.getString(R.string.your_unverified_device_requesting, new Object[]{displayName});
        }
        this.mAlertDialog = new Builder(currentActivity).setMessage((CharSequence) str).setCancelable(false).setNegativeButton((int) R.string.ignore_request, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                KeyRequestHandler.this.onDisplayKeyShareDialogClose(false, true);
            }
        }).setNeutralButton((int) R.string.share_without_verifying, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                KeyRequestHandler.this.onDisplayKeyShareDialogClose(true, false);
            }
        }).setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                KeyRequestHandler.this.onDisplayKeyShareDialogClose(false, true);
            }
        }).show();
    }
}
