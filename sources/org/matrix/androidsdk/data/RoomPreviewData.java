package org.matrix.androidsdk.data;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import java.util.Map;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.publicroom.PublicRoom;
import org.matrix.androidsdk.rest.model.sync.RoomResponse;

public class RoomPreviewData {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = RoomPreviewData.class.getSimpleName();
    private String mEventId;
    private PublicRoom mPublicRoom;
    private String mRoomAlias;
    /* access modifiers changed from: private */
    public String mRoomAvatarUrl;
    private RoomEmailInvitation mRoomEmailInvitation;
    /* access modifiers changed from: private */
    public String mRoomId;
    /* access modifiers changed from: private */
    public String mRoomName;
    /* access modifiers changed from: private */
    public RoomResponse mRoomResponse;
    /* access modifiers changed from: private */
    public RoomState mRoomState;
    private MXSession mSession;

    public RoomPreviewData(MXSession mXSession, String str, String str2, String str3, Map<String, String> map) {
        this.mSession = mXSession;
        this.mRoomId = str;
        this.mRoomAlias = str3;
        this.mEventId = str2;
        if (map != null) {
            this.mRoomEmailInvitation = new RoomEmailInvitation(map);
            this.mRoomName = this.mRoomEmailInvitation.roomName;
            this.mRoomAvatarUrl = this.mRoomEmailInvitation.roomAvatarUrl;
        }
    }

    public RoomState getRoomState() {
        return this.mRoomState;
    }

    public PublicRoom getPublicRoom() {
        return this.mPublicRoom;
    }

    public void setRoomState(RoomState roomState) {
        this.mRoomState = roomState;
    }

    public String getRoomName() {
        String str = this.mRoomName;
        return TextUtils.isEmpty(str) ? getRoomIdOrAlias() : str;
    }

    public void setRoomName(String str) {
        this.mRoomName = str;
    }

    public String getRoomAvatarUrl() {
        return this.mRoomAvatarUrl;
    }

    public String getRoomId() {
        return this.mRoomId;
    }

    public String getRoomIdOrAlias() {
        if (!TextUtils.isEmpty(this.mRoomAlias)) {
            return this.mRoomAlias;
        }
        return this.mRoomId;
    }

    public String getEventId() {
        return this.mEventId;
    }

    public MXSession getSession() {
        return this.mSession;
    }

    public RoomResponse getRoomResponse() {
        return this.mRoomResponse;
    }

    public RoomEmailInvitation getRoomEmailInvitation() {
        return this.mRoomEmailInvitation;
    }

    public void fetchPreviewData(final ApiCallback<Void> apiCallback) {
        this.mSession.getRoomsApiClient().initialSync(this.mRoomId, new ApiCallback<RoomResponse>() {
            public void onSuccess(final RoomResponse roomResponse) {
                AnonymousClass1 r0 = new AsyncTask<Void, Void, Void>() {
                    /* access modifiers changed from: protected */
                    public Void doInBackground(Void... voidArr) {
                        RoomPreviewData.this.mRoomResponse = roomResponse;
                        RoomPreviewData.this.mRoomState = new RoomState();
                        RoomPreviewData.this.mRoomState.roomId = RoomPreviewData.this.mRoomId;
                        for (Event applyState : roomResponse.state) {
                            RoomPreviewData.this.mRoomState.applyState(applyState, true, null);
                        }
                        RoomPreviewData.this.mRoomName = RoomPreviewData.this.mRoomState.name;
                        RoomPreviewData.this.mRoomAvatarUrl = RoomPreviewData.this.mRoomState.getAvatarUrl();
                        return null;
                    }

                    /* access modifiers changed from: protected */
                    public void onPostExecute(Void voidR) {
                        apiCallback.onSuccess(null);
                    }
                };
                try {
                    r0.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                } catch (Exception e) {
                    String access$500 = RoomPreviewData.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## fetchPreviewData() failed ");
                    sb.append(e.getMessage());
                    Log.e(access$500, sb.toString(), e);
                    r0.cancel(true);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            if (apiCallback != null) {
                                apiCallback.onUnexpectedError(e);
                            }
                        }
                    });
                }
            }

            public void onNetworkError(Exception exc) {
                RoomPreviewData.this.mRoomState = new RoomState();
                RoomPreviewData.this.mRoomState.roomId = RoomPreviewData.this.mRoomId;
                apiCallback.onNetworkError(exc);
            }

            public void onMatrixError(MatrixError matrixError) {
                RoomPreviewData.this.mRoomState = new RoomState();
                RoomPreviewData.this.mRoomState.roomId = RoomPreviewData.this.mRoomId;
                apiCallback.onMatrixError(matrixError);
            }

            public void onUnexpectedError(Exception exc) {
                RoomPreviewData.this.mRoomState = new RoomState();
                RoomPreviewData.this.mRoomState.roomId = RoomPreviewData.this.mRoomId;
                apiCallback.onUnexpectedError(exc);
            }
        });
    }

    public void setPublicRoom(PublicRoom publicRoom) {
        this.mPublicRoom = publicRoom;
    }
}
