package org.matrix.androidsdk.crypto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.crypto.model.crypto.RoomKeyRequestBody;

public class OutgoingRoomKeyRequest implements Serializable {
    public String mCancellationTxnId;
    public List<Map<String, String>> mRecipients;
    public RoomKeyRequestBody mRequestBody;
    public String mRequestId;
    public RequestState mState;

    public enum RequestState {
        UNSENT,
        SENT,
        CANCELLATION_PENDING,
        CANCELLATION_PENDING_AND_WILL_RESEND,
        FAILED;

        public static RequestState from(int i) {
            if (i == 0) {
                return UNSENT;
            }
            if (i == 1) {
                return SENT;
            }
            if (i == 2) {
                return CANCELLATION_PENDING;
            }
            if (i == 3) {
                return CANCELLATION_PENDING_AND_WILL_RESEND;
            }
            if (i != 4) {
                return null;
            }
            return FAILED;
        }
    }

    public OutgoingRoomKeyRequest(RoomKeyRequestBody roomKeyRequestBody, List<Map<String, String>> list, String str, RequestState requestState) {
        this.mRequestBody = roomKeyRequestBody;
        this.mRecipients = list;
        this.mRequestId = str;
        this.mState = requestState;
    }

    public String getRoomId() {
        RoomKeyRequestBody roomKeyRequestBody = this.mRequestBody;
        if (roomKeyRequestBody != null) {
            return roomKeyRequestBody.roomId;
        }
        return null;
    }

    public String getSessionId() {
        RoomKeyRequestBody roomKeyRequestBody = this.mRequestBody;
        if (roomKeyRequestBody != null) {
            return roomKeyRequestBody.sessionId;
        }
        return null;
    }
}
