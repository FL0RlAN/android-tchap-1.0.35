package org.matrix.androidsdk.crypto;

import android.os.Handler;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.OutgoingRoomKeyRequest.RequestState;
import org.matrix.androidsdk.crypto.cryptostore.IMXCryptoStore;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.crypto.internal.MXCryptoImpl;
import org.matrix.androidsdk.crypto.model.crypto.RoomKeyRequestBody;
import org.matrix.androidsdk.crypto.rest.model.crypto.RoomKeyShareCancellation;
import org.matrix.androidsdk.crypto.rest.model.crypto.RoomKeyShareRequest;

public class MXOutgoingRoomKeyRequestManager {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = MXOutgoingRoomKeyRequestManager.class.getSimpleName();
    private static final int SEND_KEY_REQUESTS_DELAY_MS = 500;
    public boolean mClientRunning;
    private final MXCryptoImpl mCrypto;
    /* access modifiers changed from: private */
    public IMXCryptoStore mCryptoStore;
    /* access modifiers changed from: private */
    public boolean mSendOutgoingRoomKeyRequestsRunning;
    private int mTxnCtr;
    /* access modifiers changed from: private */
    public Handler mWorkingHandler;

    public MXOutgoingRoomKeyRequestManager(MXCryptoImpl mXCryptoImpl) {
        this.mCrypto = mXCryptoImpl;
        this.mWorkingHandler = mXCryptoImpl.getEncryptingThreadHandler();
        this.mCryptoStore = mXCryptoImpl.getCryptoStore();
    }

    public void start() {
        this.mClientRunning = true;
        startTimer();
    }

    public void stop() {
        this.mClientRunning = false;
    }

    /* access modifiers changed from: private */
    public String makeTxnId() {
        StringBuilder sb = new StringBuilder();
        sb.append("m");
        sb.append(System.currentTimeMillis());
        sb.append(".");
        int i = this.mTxnCtr;
        this.mTxnCtr = i + 1;
        sb.append(i);
        return sb.toString();
    }

    public void sendRoomKeyRequest(final RoomKeyRequestBody roomKeyRequestBody, final List<Map<String, String>> list) {
        this.mWorkingHandler.post(new Runnable() {
            public void run() {
                if (MXOutgoingRoomKeyRequestManager.this.mCryptoStore.getOrAddOutgoingRoomKeyRequest(new OutgoingRoomKeyRequest(roomKeyRequestBody, list, MXOutgoingRoomKeyRequestManager.this.makeTxnId(), RequestState.UNSENT)).mState == RequestState.UNSENT) {
                    MXOutgoingRoomKeyRequestManager.this.startTimer();
                }
            }
        });
    }

    public void cancelRoomKeyRequest(RoomKeyRequestBody roomKeyRequestBody) {
        cancelRoomKeyRequest(roomKeyRequestBody, false);
    }

    public void resendRoomKeyRequest(RoomKeyRequestBody roomKeyRequestBody) {
        cancelRoomKeyRequest(roomKeyRequestBody, true);
    }

    private void cancelRoomKeyRequest(RoomKeyRequestBody roomKeyRequestBody, boolean z) {
        OutgoingRoomKeyRequest outgoingRoomKeyRequest = this.mCryptoStore.getOutgoingRoomKeyRequest(roomKeyRequestBody);
        if (outgoingRoomKeyRequest != null) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("cancelRoomKeyRequest: requestId: ");
            sb.append(outgoingRoomKeyRequest.mRequestId);
            sb.append(" state: ");
            sb.append(outgoingRoomKeyRequest.mState);
            sb.append(" andResend: ");
            sb.append(z);
            Log.d(str, sb.toString());
            if (!(outgoingRoomKeyRequest.mState == RequestState.CANCELLATION_PENDING || outgoingRoomKeyRequest.mState == RequestState.CANCELLATION_PENDING_AND_WILL_RESEND)) {
                if (outgoingRoomKeyRequest.mState == RequestState.UNSENT || outgoingRoomKeyRequest.mState == RequestState.FAILED) {
                    String str2 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## cancelRoomKeyRequest() : deleting unnecessary room key request for ");
                    sb2.append(roomKeyRequestBody);
                    Log.d(str2, sb2.toString());
                    this.mCryptoStore.deleteOutgoingRoomKeyRequest(outgoingRoomKeyRequest.mRequestId);
                } else if (outgoingRoomKeyRequest.mState == RequestState.SENT) {
                    if (z) {
                        outgoingRoomKeyRequest.mState = RequestState.CANCELLATION_PENDING_AND_WILL_RESEND;
                    } else {
                        outgoingRoomKeyRequest.mState = RequestState.CANCELLATION_PENDING;
                    }
                    outgoingRoomKeyRequest.mCancellationTxnId = makeTxnId();
                    this.mCryptoStore.updateOutgoingRoomKeyRequest(outgoingRoomKeyRequest);
                    sendOutgoingRoomKeyRequestCancellation(outgoingRoomKeyRequest);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void startTimer() {
        this.mWorkingHandler.post(new Runnable() {
            public void run() {
                if (!MXOutgoingRoomKeyRequestManager.this.mSendOutgoingRoomKeyRequestsRunning) {
                    MXOutgoingRoomKeyRequestManager.this.mWorkingHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (MXOutgoingRoomKeyRequestManager.this.mSendOutgoingRoomKeyRequestsRunning) {
                                Log.d(MXOutgoingRoomKeyRequestManager.LOG_TAG, "## startTimer() : RoomKeyRequestSend already in progress!");
                                return;
                            }
                            MXOutgoingRoomKeyRequestManager.this.mSendOutgoingRoomKeyRequestsRunning = true;
                            MXOutgoingRoomKeyRequestManager.this.sendOutgoingRoomKeyRequests();
                        }
                    }, 500);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void sendOutgoingRoomKeyRequests() {
        if (!this.mClientRunning) {
            this.mSendOutgoingRoomKeyRequestsRunning = false;
            return;
        }
        Log.d(LOG_TAG, "## sendOutgoingRoomKeyRequests() :  Looking for queued outgoing room key requests");
        OutgoingRoomKeyRequest outgoingRoomKeyRequestByState = this.mCryptoStore.getOutgoingRoomKeyRequestByState(new HashSet(Arrays.asList(new RequestState[]{RequestState.UNSENT, RequestState.CANCELLATION_PENDING, RequestState.CANCELLATION_PENDING_AND_WILL_RESEND})));
        if (outgoingRoomKeyRequestByState == null) {
            Log.e(LOG_TAG, "## sendOutgoingRoomKeyRequests() : No more outgoing room key requests");
            this.mSendOutgoingRoomKeyRequestsRunning = false;
            return;
        }
        if (RequestState.UNSENT == outgoingRoomKeyRequestByState.mState) {
            sendOutgoingRoomKeyRequest(outgoingRoomKeyRequestByState);
        } else {
            sendOutgoingRoomKeyRequestCancellation(outgoingRoomKeyRequestByState);
        }
    }

    private void sendOutgoingRoomKeyRequest(final OutgoingRoomKeyRequest outgoingRoomKeyRequest) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## sendOutgoingRoomKeyRequest() : Requesting keys ");
        sb.append(outgoingRoomKeyRequest.mRequestBody);
        sb.append(" from ");
        sb.append(outgoingRoomKeyRequest.mRecipients);
        sb.append(" id ");
        sb.append(outgoingRoomKeyRequest.mRequestId);
        Log.d(str, sb.toString());
        RoomKeyShareRequest roomKeyShareRequest = new RoomKeyShareRequest();
        roomKeyShareRequest.requestingDeviceId = this.mCryptoStore.getDeviceId();
        roomKeyShareRequest.requestId = outgoingRoomKeyRequest.mRequestId;
        roomKeyShareRequest.body = outgoingRoomKeyRequest.mRequestBody;
        sendMessageToDevices(roomKeyShareRequest, outgoingRoomKeyRequest.mRecipients, outgoingRoomKeyRequest.mRequestId, new ApiCallback<Void>() {
            private void onDone(final RequestState requestState) {
                MXOutgoingRoomKeyRequestManager.this.mWorkingHandler.post(new Runnable() {
                    public void run() {
                        if (outgoingRoomKeyRequest.mState != RequestState.UNSENT) {
                            String access$400 = MXOutgoingRoomKeyRequestManager.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## sendOutgoingRoomKeyRequest() : Cannot update room key request from UNSENT as it was already updated to ");
                            sb.append(outgoingRoomKeyRequest.mState);
                            Log.d(access$400, sb.toString());
                        } else {
                            outgoingRoomKeyRequest.mState = requestState;
                            MXOutgoingRoomKeyRequestManager.this.mCryptoStore.updateOutgoingRoomKeyRequest(outgoingRoomKeyRequest);
                        }
                        MXOutgoingRoomKeyRequestManager.this.mSendOutgoingRoomKeyRequestsRunning = false;
                        MXOutgoingRoomKeyRequestManager.this.startTimer();
                    }
                });
            }

            public void onSuccess(Void voidR) {
                Log.d(MXOutgoingRoomKeyRequestManager.LOG_TAG, "## sendOutgoingRoomKeyRequest succeed");
                onDone(RequestState.SENT);
            }

            public void onNetworkError(Exception exc) {
                String access$400 = MXOutgoingRoomKeyRequestManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## sendOutgoingRoomKeyRequest failed ");
                sb.append(exc.getMessage());
                Log.e(access$400, sb.toString(), exc);
                onDone(RequestState.FAILED);
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$400 = MXOutgoingRoomKeyRequestManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## sendOutgoingRoomKeyRequest failed ");
                sb.append(matrixError.getMessage());
                Log.e(access$400, sb.toString());
                onDone(RequestState.FAILED);
            }

            public void onUnexpectedError(Exception exc) {
                String access$400 = MXOutgoingRoomKeyRequestManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## sendOutgoingRoomKeyRequest failed ");
                sb.append(exc.getMessage());
                Log.e(access$400, sb.toString(), exc);
                onDone(RequestState.FAILED);
            }
        });
    }

    private void sendOutgoingRoomKeyRequestCancellation(final OutgoingRoomKeyRequest outgoingRoomKeyRequest) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## sendOutgoingRoomKeyRequestCancellation() : Sending cancellation for key request for ");
        sb.append(outgoingRoomKeyRequest.mRequestBody);
        sb.append(" to ");
        sb.append(outgoingRoomKeyRequest.mRecipients);
        sb.append(" cancellation id  ");
        sb.append(outgoingRoomKeyRequest.mCancellationTxnId);
        Log.d(str, sb.toString());
        RoomKeyShareCancellation roomKeyShareCancellation = new RoomKeyShareCancellation();
        roomKeyShareCancellation.requestingDeviceId = this.mCryptoStore.getDeviceId();
        roomKeyShareCancellation.requestId = outgoingRoomKeyRequest.mCancellationTxnId;
        sendMessageToDevices(roomKeyShareCancellation, outgoingRoomKeyRequest.mRecipients, outgoingRoomKeyRequest.mCancellationTxnId, new ApiCallback<Void>() {
            private void onDone() {
                MXOutgoingRoomKeyRequestManager.this.mWorkingHandler.post(new Runnable() {
                    public void run() {
                        MXOutgoingRoomKeyRequestManager.this.mCryptoStore.deleteOutgoingRoomKeyRequest(outgoingRoomKeyRequest.mRequestId);
                        MXOutgoingRoomKeyRequestManager.this.mSendOutgoingRoomKeyRequestsRunning = false;
                        MXOutgoingRoomKeyRequestManager.this.startTimer();
                    }
                });
            }

            public void onSuccess(Void voidR) {
                Log.d(MXOutgoingRoomKeyRequestManager.LOG_TAG, "## sendOutgoingRoomKeyRequestCancellation() : done");
                boolean z = outgoingRoomKeyRequest.mState == RequestState.CANCELLATION_PENDING_AND_WILL_RESEND;
                onDone();
                if (z) {
                    MXOutgoingRoomKeyRequestManager.this.sendRoomKeyRequest(outgoingRoomKeyRequest.mRequestBody, outgoingRoomKeyRequest.mRecipients);
                }
            }

            public void onNetworkError(Exception exc) {
                String access$400 = MXOutgoingRoomKeyRequestManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## sendOutgoingRoomKeyRequestCancellation failed ");
                sb.append(exc.getMessage());
                Log.e(access$400, sb.toString(), exc);
                onDone();
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$400 = MXOutgoingRoomKeyRequestManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## sendOutgoingRoomKeyRequestCancellation failed ");
                sb.append(matrixError.getMessage());
                Log.e(access$400, sb.toString());
                onDone();
            }

            public void onUnexpectedError(Exception exc) {
                String access$400 = MXOutgoingRoomKeyRequestManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## sendOutgoingRoomKeyRequestCancellation failed ");
                sb.append(exc.getMessage());
                Log.e(access$400, sb.toString(), exc);
                onDone();
            }
        });
    }

    private void sendMessageToDevices(Object obj, List<Map<String, String>> list, String str, ApiCallback<Void> apiCallback) {
        MXUsersDevicesMap mXUsersDevicesMap = new MXUsersDevicesMap();
        for (Map map : list) {
            mXUsersDevicesMap.setObject(obj, (String) map.get("userId"), (String) map.get("deviceId"));
        }
        this.mCrypto.getCryptoRestClient().sendToDevice("m.room_key_request", mXUsersDevicesMap, str, apiCallback);
    }
}
