package org.matrix.androidsdk.call;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.jvm.internal.LongCompanionObject;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.MXPatterns;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.MXCryptoError;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomState;
import org.matrix.androidsdk.data.store.IMXStore;
import org.matrix.androidsdk.listeners.MXEventListener;
import org.matrix.androidsdk.rest.client.CallRestClient;
import org.matrix.androidsdk.rest.model.CreateRoomParams;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.EventContent;
import org.matrix.androidsdk.rest.model.RoomMember;

public class MXCallsManager {
    private static final String DOMAIN = "matrix.org";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = MXCallsManager.class.getSimpleName();
    private static final String USER_PREFIX = "fs_";
    private static final Map<String, String> mConferenceUserIdByRoomId = new HashMap();
    /* access modifiers changed from: private */
    public CallRestClient mCallResClient = null;
    /* access modifiers changed from: private */
    public final Map<String, IMXCall> mCallsByCallId = new HashMap();
    private Context mContext = null;
    private final Set<IMXCallsManagerListener> mListeners = new HashSet();
    private CallClass mPreferredCallClass = CallClass.WEBRTC_CLASS;
    /* access modifiers changed from: private */
    public MXSession mSession = null;
    private boolean mSuspendTurnServerRefresh = false;
    /* access modifiers changed from: private */
    public JsonElement mTurnServer = null;
    /* access modifiers changed from: private */
    public Timer mTurnServerTimer = null;
    /* access modifiers changed from: private */
    public final Handler mUIThreadHandler;
    /* access modifiers changed from: private */
    public final Set<String> mxPendingIncomingCallId = new HashSet();

    public enum CallClass {
        WEBRTC_CLASS,
        DEFAULT_CLASS
    }

    public MXCallsManager(MXSession mXSession, Context context) {
        this.mSession = mXSession;
        this.mContext = context;
        this.mUIThreadHandler = new Handler(Looper.getMainLooper());
        this.mCallResClient = this.mSession.getCallRestClient();
        this.mSession.getDataHandler().addListener(new MXEventListener() {
            public void onLiveEvent(Event event, RoomState roomState) {
                if (TextUtils.equals(event.getType(), "m.room.member") && TextUtils.equals(event.sender, MXCallsManager.getConferenceUserId(event.roomId))) {
                    EventContent eventContent = JsonUtils.toEventContent(event.getContentAsJsonObject());
                    if (TextUtils.equals(eventContent.membership, "leave")) {
                        MXCallsManager.this.dispatchOnVoipConferenceFinished(event.roomId);
                    }
                    if (TextUtils.equals(eventContent.membership, "join")) {
                        MXCallsManager.this.dispatchOnVoipConferenceStarted(event.roomId);
                    }
                }
            }
        });
        refreshTurnServer();
    }

    public boolean isSupported() {
        return MXWebRtcCall.isSupported(this.mContext);
    }

    public Collection<CallClass> supportedClass() {
        ArrayList arrayList = new ArrayList();
        if (MXWebRtcCall.isSupported(this.mContext)) {
            arrayList.add(CallClass.WEBRTC_CLASS);
        }
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("supportedClass ");
        sb.append(arrayList);
        Log.d(str, sb.toString());
        return arrayList;
    }

    public void setDefaultCallClass(CallClass callClass) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setDefaultCallClass ");
        sb.append(callClass);
        Log.d(str, sb.toString());
        if (callClass == CallClass.WEBRTC_CLASS ? MXWebRtcCall.isSupported(this.mContext) : false) {
            this.mPreferredCallClass = callClass;
        }
    }

    private IMXCall createCall(String str) {
        MXWebRtcCall mXWebRtcCall;
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        String str3 = "createCall ";
        sb.append(str3);
        sb.append(str);
        Log.d(str2, sb.toString());
        try {
            mXWebRtcCall = new MXWebRtcCall(this.mSession, this.mContext, getTurnServer());
        } catch (Exception e) {
            String str4 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str3);
            sb2.append(e.getMessage());
            Log.e(str4, sb2.toString(), e);
            mXWebRtcCall = null;
        }
        if (str != null) {
            mXWebRtcCall.setCallId(str);
        }
        return mXWebRtcCall;
    }

    public IMXCall getCallWithRoomId(String str) {
        ArrayList<IMXCall> arrayList;
        synchronized (this) {
            arrayList = new ArrayList<>(this.mCallsByCallId.values());
        }
        for (IMXCall iMXCall : arrayList) {
            if (TextUtils.equals(str, iMXCall.getRoom().getRoomId())) {
                if (!TextUtils.equals(iMXCall.getCallState(), IMXCall.CALL_STATE_ENDED)) {
                    return iMXCall;
                }
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## getCallWithRoomId() : the call ");
                sb.append(iMXCall.getCallId());
                sb.append(" has been stopped");
                Log.d(str2, sb.toString());
                synchronized (this) {
                    this.mCallsByCallId.remove(iMXCall.getCallId());
                }
            }
        }
        return null;
    }

    public IMXCall getCallWithCallId(String str) {
        return getCallWithCallId(str, false);
    }

    /* access modifiers changed from: private */
    public IMXCall getCallWithCallId(String str, boolean z) {
        IMXCall iMXCall;
        IMXCall iMXCall2 = null;
        if (str != null) {
            synchronized (this) {
                iMXCall = (IMXCall) this.mCallsByCallId.get(str);
            }
        } else {
            iMXCall = null;
        }
        if (iMXCall == null || !TextUtils.equals(iMXCall.getCallState(), IMXCall.CALL_STATE_ENDED)) {
            iMXCall2 = iMXCall;
        } else {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## getCallWithCallId() : the call ");
            sb.append(str);
            sb.append(" has been stopped");
            Log.d(str2, sb.toString());
            synchronized (this) {
                this.mCallsByCallId.remove(iMXCall.getCallId());
            }
        }
        if (iMXCall2 == null && z) {
            iMXCall2 = createCall(str);
            synchronized (this) {
                this.mCallsByCallId.put(iMXCall2.getCallId(), iMXCall2);
            }
        }
        String str3 = LOG_TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getCallWithCallId ");
        sb2.append(str);
        sb2.append(" ");
        sb2.append(iMXCall2);
        Log.d(str3, sb2.toString());
        return iMXCall2;
    }

    public static boolean isCallInProgress(IMXCall iMXCall) {
        if (iMXCall == null) {
            return false;
        }
        String callState = iMXCall.getCallState();
        if (TextUtils.equals(callState, IMXCall.CALL_STATE_CREATED) || TextUtils.equals(callState, IMXCall.CALL_STATE_CREATING_CALL_VIEW) || TextUtils.equals(callState, IMXCall.CALL_STATE_READY) || TextUtils.equals(callState, IMXCall.CALL_STATE_WAIT_LOCAL_MEDIA) || TextUtils.equals(callState, IMXCall.CALL_STATE_WAIT_CREATE_OFFER) || TextUtils.equals(callState, IMXCall.CALL_STATE_INVITE_SENT) || TextUtils.equals(callState, IMXCall.CALL_STATE_RINGING) || TextUtils.equals(callState, IMXCall.CALL_STATE_CREATE_ANSWER) || TextUtils.equals(callState, IMXCall.CALL_STATE_CONNECTING) || TextUtils.equals(callState, IMXCall.CALL_STATE_CONNECTED)) {
            return true;
        }
        return false;
    }

    public boolean hasActiveCalls() {
        synchronized (this) {
            ArrayList<String> arrayList = new ArrayList<>();
            for (String str : this.mCallsByCallId.keySet()) {
                if (TextUtils.equals(((IMXCall) this.mCallsByCallId.get(str)).getCallState(), IMXCall.CALL_STATE_ENDED)) {
                    String str2 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("# hasActiveCalls() : the call ");
                    sb.append(str);
                    sb.append(" is not anymore valid");
                    Log.d(str2, sb.toString());
                    arrayList.add(str);
                } else {
                    String str3 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("# hasActiveCalls() : the call ");
                    sb2.append(str);
                    sb2.append(" is active");
                    Log.d(str3, sb2.toString());
                    return true;
                }
            }
            for (String remove : arrayList) {
                this.mCallsByCallId.remove(remove);
            }
            Log.d(LOG_TAG, "# hasActiveCalls() : no active call");
            return false;
        }
    }

    public void handleCallEvent(final IMXStore iMXStore, final Event event) {
        if (event.isCallEvent() && isSupported()) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("handleCallEvent ");
            sb.append(event.getType());
            Log.d(str, sb.toString());
            this.mUIThreadHandler.post(new Runnable() {
                /* JADX WARNING: Removed duplicated region for block: B:11:0x005e A[ADDED_TO_REGION] */
                /* JADX WARNING: Removed duplicated region for block: B:77:? A[ADDED_TO_REGION, ORIG_RETURN, RETURN, SYNTHETIC] */
                public void run() {
                    String str;
                    JsonObject jsonObject;
                    boolean equals = TextUtils.equals(event.getSender(), MXCallsManager.this.mSession.getMyUserId());
                    Room room = MXCallsManager.this.mSession.getDataHandler().getRoom(iMXStore, event.roomId, true);
                    try {
                        jsonObject = event.getContentAsJsonObject();
                        try {
                            str = jsonObject.getAsJsonPrimitive("call_id").getAsString();
                        } catch (Exception e) {
                            e = e;
                            String access$300 = MXCallsManager.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("handleCallEvent : fail to retrieve call_id ");
                            sb.append(e.getMessage());
                            Log.e(access$300, sb.toString(), e);
                            str = null;
                            if (str == null) {
                            }
                        }
                    } catch (Exception e2) {
                        e = e2;
                        jsonObject = null;
                        String access$3002 = MXCallsManager.LOG_TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("handleCallEvent : fail to retrieve call_id ");
                        sb2.append(e.getMessage());
                        Log.e(access$3002, sb2.toString(), e);
                        str = null;
                        if (str == null) {
                            return;
                        }
                    }
                    if (str == null && room != null) {
                        if (Event.EVENT_TYPE_CALL_INVITE.equals(event.getType())) {
                            long age = event.getAge();
                            if (LongCompanionObject.MAX_VALUE == age) {
                                age = System.currentTimeMillis() - event.getOriginServerTs();
                            }
                            if (age < 120000) {
                                IMXCall access$400 = MXCallsManager.this.getCallWithCallId(str, !equals);
                                if (access$400 != null) {
                                    if (access$400.getRoom() == null) {
                                        access$400.setRooms(room, room);
                                    }
                                    if (!equals) {
                                        access$400.prepareIncomingCall(jsonObject, str, null);
                                        MXCallsManager.this.mxPendingIncomingCallId.add(str);
                                        return;
                                    }
                                    access$400.handleCallEvent(event);
                                    return;
                                }
                                return;
                            }
                            Log.d(MXCallsManager.LOG_TAG, "## handleCallEvent() : m.call.invite is ignored because it is too old");
                            return;
                        }
                        if (!Event.EVENT_TYPE_CALL_CANDIDATES.equals(event.getType())) {
                            if (Event.EVENT_TYPE_CALL_ANSWER.equals(event.getType())) {
                                IMXCall callWithCallId = MXCallsManager.this.getCallWithCallId(str);
                                if (callWithCallId != null) {
                                    if (IMXCall.CALL_STATE_CREATED.equals(callWithCallId.getCallState())) {
                                        callWithCallId.onAnsweredElsewhere();
                                        synchronized (this) {
                                            MXCallsManager.this.mCallsByCallId.remove(str);
                                        }
                                        return;
                                    }
                                    if (callWithCallId.getRoom() == null) {
                                        callWithCallId.setRooms(room, room);
                                    }
                                    callWithCallId.handleCallEvent(event);
                                    return;
                                }
                                return;
                            }
                            if (Event.EVENT_TYPE_CALL_HANGUP.equals(event.getType())) {
                                final IMXCall callWithCallId2 = MXCallsManager.this.getCallWithCallId(str);
                                if (callWithCallId2 != null) {
                                    boolean z = !IMXCall.CALL_STATE_CREATED.equals(callWithCallId2.getCallState());
                                    if (callWithCallId2.getRoom() == null) {
                                        callWithCallId2.setRooms(room, room);
                                    }
                                    if (z) {
                                        callWithCallId2.handleCallEvent(event);
                                    }
                                    synchronized (this) {
                                        MXCallsManager.this.mCallsByCallId.remove(str);
                                    }
                                    MXCallsManager.this.mUIThreadHandler.post(new Runnable() {
                                        public void run() {
                                            MXCallsManager.this.dispatchOnCallHangUp(callWithCallId2);
                                        }
                                    });
                                }
                            }
                        } else if (!equals) {
                            IMXCall callWithCallId3 = MXCallsManager.this.getCallWithCallId(str);
                            if (callWithCallId3 != null) {
                                if (callWithCallId3.getRoom() == null) {
                                    callWithCallId3.setRooms(room, room);
                                }
                                callWithCallId3.handleCallEvent(event);
                            }
                        }
                    }
                }
            });
        }
    }

    public void checkPendingIncomingCalls() {
        this.mUIThreadHandler.post(new Runnable() {
            public void run() {
                if (MXCallsManager.this.mxPendingIncomingCallId.size() > 0) {
                    for (String callWithCallId : MXCallsManager.this.mxPendingIncomingCallId) {
                        final IMXCall callWithCallId2 = MXCallsManager.this.getCallWithCallId(callWithCallId);
                        if (callWithCallId2 != null) {
                            final Room room = callWithCallId2.getRoom();
                            if (room == null || !room.isEncrypted() || !MXCallsManager.this.mSession.getCrypto().warnOnUnknownDevices() || room.getNumberOfJoinedMembers() != 2) {
                                MXCallsManager.this.dispatchOnIncomingCall(callWithCallId2, null);
                            } else {
                                MXCallsManager.this.mSession.getCrypto().getGlobalBlacklistUnverifiedDevices(new SimpleApiCallback<Boolean>() {
                                    public void onSuccess(Boolean bool) {
                                        if (bool.booleanValue()) {
                                            MXCallsManager.this.dispatchOnIncomingCall(callWithCallId2, null);
                                        } else {
                                            MXCallsManager.this.mSession.getCrypto().isRoomBlacklistUnverifiedDevices(room.getRoomId(), new SimpleApiCallback<Boolean>() {
                                                public void onSuccess(Boolean bool) {
                                                    if (bool.booleanValue()) {
                                                        MXCallsManager.this.dispatchOnIncomingCall(callWithCallId2, null);
                                                    } else {
                                                        room.getJoinedMembersAsync(new ApiCallback<List<RoomMember>>() {
                                                            public void onNetworkError(Exception exc) {
                                                                MXCallsManager.this.dispatchOnIncomingCall(callWithCallId2, null);
                                                            }

                                                            public void onMatrixError(MatrixError matrixError) {
                                                                MXCallsManager.this.dispatchOnIncomingCall(callWithCallId2, null);
                                                            }

                                                            public void onUnexpectedError(Exception exc) {
                                                                MXCallsManager.this.dispatchOnIncomingCall(callWithCallId2, null);
                                                            }

                                                            public void onSuccess(List<RoomMember> list) {
                                                                String userId = ((RoomMember) list.get(0)).getUserId();
                                                                String userId2 = ((RoomMember) list.get(1)).getUserId();
                                                                Log.d(MXCallsManager.LOG_TAG, "## checkPendingIncomingCalls() : check the unknown devices");
                                                                MXCallsManager.this.mSession.getCrypto().checkUnknownDevices(Arrays.asList(new String[]{userId, userId2}), new ApiCallback<Void>() {
                                                                    public void onSuccess(Void voidR) {
                                                                        Log.d(MXCallsManager.LOG_TAG, "## checkPendingIncomingCalls() : no unknown device");
                                                                        MXCallsManager.this.dispatchOnIncomingCall(callWithCallId2, null);
                                                                    }

                                                                    public void onNetworkError(Exception exc) {
                                                                        String access$300 = MXCallsManager.LOG_TAG;
                                                                        StringBuilder sb = new StringBuilder();
                                                                        sb.append("## checkPendingIncomingCalls() : checkUnknownDevices failed ");
                                                                        sb.append(exc.getMessage());
                                                                        Log.e(access$300, sb.toString(), exc);
                                                                        MXCallsManager.this.dispatchOnIncomingCall(callWithCallId2, null);
                                                                    }

                                                                    /* JADX WARNING: Removed duplicated region for block: B:7:0x0019  */
                                                                    /* JADX WARNING: Removed duplicated region for block: B:8:0x0023  */
                                                                    public void onMatrixError(MatrixError matrixError) {
                                                                        MXUsersDevicesMap mXUsersDevicesMap;
                                                                        if (matrixError instanceof MXCryptoError) {
                                                                            MXCryptoError mXCryptoError = (MXCryptoError) matrixError;
                                                                            if (MXCryptoError.UNKNOWN_DEVICES_CODE.equals(mXCryptoError.errcode)) {
                                                                                mXUsersDevicesMap = (MXUsersDevicesMap) mXCryptoError.mExceptionData;
                                                                                if (mXUsersDevicesMap == null) {
                                                                                    Log.d(MXCallsManager.LOG_TAG, "## checkPendingIncomingCalls() : checkUnknownDevices found some unknown devices");
                                                                                } else {
                                                                                    String access$300 = MXCallsManager.LOG_TAG;
                                                                                    StringBuilder sb = new StringBuilder();
                                                                                    sb.append("## checkPendingIncomingCalls() : checkUnknownDevices failed ");
                                                                                    sb.append(matrixError.getMessage());
                                                                                    Log.e(access$300, sb.toString());
                                                                                }
                                                                                MXCallsManager.this.dispatchOnIncomingCall(callWithCallId2, mXUsersDevicesMap);
                                                                            }
                                                                        }
                                                                        mXUsersDevicesMap = null;
                                                                        if (mXUsersDevicesMap == null) {
                                                                        }
                                                                        MXCallsManager.this.dispatchOnIncomingCall(callWithCallId2, mXUsersDevicesMap);
                                                                    }

                                                                    public void onUnexpectedError(Exception exc) {
                                                                        String access$300 = MXCallsManager.LOG_TAG;
                                                                        StringBuilder sb = new StringBuilder();
                                                                        sb.append("## checkPendingIncomingCalls() : checkUnknownDevices failed ");
                                                                        sb.append(exc.getMessage());
                                                                        Log.e(access$300, sb.toString(), exc);
                                                                        MXCallsManager.this.dispatchOnIncomingCall(callWithCallId2, null);
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
                MXCallsManager.this.mxPendingIncomingCallId.clear();
            }
        });
    }

    public void createCallInRoom(String str, final boolean z, final ApiCallback<IMXCall> apiCallback) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("createCallInRoom in ");
        sb.append(str);
        Log.d(str2, sb.toString());
        final Room room = this.mSession.getDataHandler().getRoom(str);
        if (room != null) {
            boolean isSupported = isSupported();
            String str3 = MatrixError.NOT_SUPPORTED;
            if (isSupported) {
                int numberOfJoinedMembers = room.getNumberOfJoinedMembers();
                String str4 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("createCallInRoom : the room has ");
                sb2.append(numberOfJoinedMembers);
                sb2.append(" joined members");
                Log.d(str4, sb2.toString());
                if (numberOfJoinedMembers > 1) {
                    if (numberOfJoinedMembers != 2) {
                        Log.d(LOG_TAG, "createCallInRoom : inviteConferenceUser");
                        inviteConferenceUser(room, new ApiCallback<Void>() {
                            public void onSuccess(Void voidR) {
                                Log.d(MXCallsManager.LOG_TAG, "createCallInRoom : inviteConferenceUser succeeds");
                                MXCallsManager.this.getConferenceUserRoom(room.getRoomId(), new ApiCallback<Room>() {
                                    public void onSuccess(Room room) {
                                        Log.d(MXCallsManager.LOG_TAG, "createCallInRoom : getConferenceUserRoom succeeds");
                                        final IMXCall access$400 = MXCallsManager.this.getCallWithCallId(null, true);
                                        access$400.setRooms(room, room);
                                        access$400.setIsConference(true);
                                        access$400.setIsVideo(z);
                                        MXCallsManager.this.dispatchOnOutgoingCall(access$400);
                                        if (apiCallback != null) {
                                            MXCallsManager.this.mUIThreadHandler.post(new Runnable() {
                                                public void run() {
                                                    apiCallback.onSuccess(access$400);
                                                }
                                            });
                                        }
                                    }

                                    public void onNetworkError(Exception exc) {
                                        String access$300 = MXCallsManager.LOG_TAG;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("createCallInRoom : getConferenceUserRoom failed ");
                                        sb.append(exc.getMessage());
                                        Log.e(access$300, sb.toString(), exc);
                                        if (apiCallback != null) {
                                            apiCallback.onNetworkError(exc);
                                        }
                                    }

                                    public void onMatrixError(MatrixError matrixError) {
                                        String access$300 = MXCallsManager.LOG_TAG;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("createCallInRoom : getConferenceUserRoom failed ");
                                        sb.append(matrixError.getMessage());
                                        Log.e(access$300, sb.toString());
                                        if (apiCallback != null) {
                                            apiCallback.onMatrixError(matrixError);
                                        }
                                    }

                                    public void onUnexpectedError(Exception exc) {
                                        String access$300 = MXCallsManager.LOG_TAG;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("createCallInRoom : getConferenceUserRoom failed ");
                                        sb.append(exc.getMessage());
                                        Log.e(access$300, sb.toString(), exc);
                                        if (apiCallback != null) {
                                            apiCallback.onUnexpectedError(exc);
                                        }
                                    }
                                });
                            }

                            public void onNetworkError(Exception exc) {
                                String access$300 = MXCallsManager.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("createCallInRoom : inviteConferenceUser fails ");
                                sb.append(exc.getMessage());
                                Log.e(access$300, sb.toString(), exc);
                                ApiCallback apiCallback = apiCallback;
                                if (apiCallback != null) {
                                    apiCallback.onNetworkError(exc);
                                }
                            }

                            public void onMatrixError(MatrixError matrixError) {
                                String access$300 = MXCallsManager.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("createCallInRoom : inviteConferenceUser fails ");
                                sb.append(matrixError.getMessage());
                                Log.e(access$300, sb.toString());
                                ApiCallback apiCallback = apiCallback;
                                if (apiCallback != null) {
                                    apiCallback.onMatrixError(matrixError);
                                }
                            }

                            public void onUnexpectedError(Exception exc) {
                                String access$300 = MXCallsManager.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("createCallInRoom : inviteConferenceUser fails ");
                                sb.append(exc.getMessage());
                                Log.e(access$300, sb.toString(), exc);
                                ApiCallback apiCallback = apiCallback;
                                if (apiCallback != null) {
                                    apiCallback.onUnexpectedError(exc);
                                }
                            }
                        });
                    } else if (!room.isEncrypted() || !this.mSession.getCrypto().warnOnUnknownDevices()) {
                        final IMXCall callWithCallId = getCallWithCallId(null, true);
                        callWithCallId.setIsVideo(z);
                        dispatchOnOutgoingCall(callWithCallId);
                        callWithCallId.setRooms(room, room);
                        if (apiCallback != null) {
                            this.mUIThreadHandler.post(new Runnable() {
                                public void run() {
                                    apiCallback.onSuccess(callWithCallId);
                                }
                            });
                        }
                    } else {
                        final ApiCallback<IMXCall> apiCallback2 = apiCallback;
                        final Room room2 = room;
                        final boolean z2 = z;
                        AnonymousClass4 r1 = new SimpleApiCallback<List<RoomMember>>(apiCallback) {
                            public void onSuccess(List<RoomMember> list) {
                                if (list.size() != 2) {
                                    apiCallback2.onUnexpectedError(new Exception("Wrong number of members"));
                                    return;
                                }
                                String userId = ((RoomMember) list.get(0)).getUserId();
                                String userId2 = ((RoomMember) list.get(1)).getUserId();
                                MXCallsManager.this.mSession.getCrypto().checkUnknownDevices(Arrays.asList(new String[]{userId, userId2}), new SimpleApiCallback<Void>(apiCallback2) {
                                    public void onSuccess(Void voidR) {
                                        final IMXCall access$400 = MXCallsManager.this.getCallWithCallId(null, true);
                                        access$400.setRooms(room2, room2);
                                        access$400.setIsVideo(z2);
                                        MXCallsManager.this.dispatchOnOutgoingCall(access$400);
                                        if (apiCallback2 != null) {
                                            MXCallsManager.this.mUIThreadHandler.post(new Runnable() {
                                                public void run() {
                                                    apiCallback2.onSuccess(access$400);
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        };
                        room.getJoinedMembersAsync(r1);
                    }
                } else if (apiCallback != null) {
                    apiCallback.onMatrixError(new MatrixError(str3, "too few users"));
                }
            } else if (apiCallback != null) {
                apiCallback.onMatrixError(new MatrixError(str3, "VOIP is not supported"));
            }
        } else if (apiCallback != null) {
            apiCallback.onMatrixError(new MatrixError(MatrixError.NOT_FOUND, "room not found"));
        }
    }

    public void pauseTurnServerRefresh() {
        this.mSuspendTurnServerRefresh = true;
    }

    public void unpauseTurnServerRefresh() {
        Log.d(LOG_TAG, "unpauseTurnServerRefresh");
        this.mSuspendTurnServerRefresh = false;
        Timer timer = this.mTurnServerTimer;
        if (timer != null) {
            timer.cancel();
            this.mTurnServerTimer = null;
        }
        refreshTurnServer();
    }

    public void stopTurnServerRefresh() {
        Log.d(LOG_TAG, "stopTurnServerRefresh");
        this.mSuspendTurnServerRefresh = true;
        Timer timer = this.mTurnServerTimer;
        if (timer != null) {
            timer.cancel();
            this.mTurnServerTimer = null;
        }
    }

    private JsonElement getTurnServer() {
        JsonElement jsonElement;
        synchronized (LOG_TAG) {
            jsonElement = this.mTurnServer;
        }
        Log.d(LOG_TAG, "getTurnServer ");
        return jsonElement;
    }

    /* access modifiers changed from: private */
    public void refreshTurnServer() {
        if (!this.mSuspendTurnServerRefresh) {
            Log.d(LOG_TAG, "## refreshTurnServer () starts");
            this.mUIThreadHandler.post(new Runnable() {
                public void run() {
                    MXCallsManager.this.mCallResClient.getTurnServer(new ApiCallback<JsonObject>() {
                        private void restartAfter(int i) {
                            if (i <= 0) {
                                String access$300 = MXCallsManager.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## refreshTurnServer() : invalid delay ");
                                sb.append(i);
                                Log.e(access$300, sb.toString());
                                return;
                            }
                            if (MXCallsManager.this.mTurnServerTimer != null) {
                                MXCallsManager.this.mTurnServerTimer.cancel();
                            }
                            try {
                                MXCallsManager.this.mTurnServerTimer = new Timer();
                                MXCallsManager.this.mTurnServerTimer.schedule(new TimerTask() {
                                    public void run() {
                                        if (MXCallsManager.this.mTurnServerTimer != null) {
                                            MXCallsManager.this.mTurnServerTimer.cancel();
                                            MXCallsManager.this.mTurnServerTimer = null;
                                        }
                                        MXCallsManager.this.refreshTurnServer();
                                    }
                                }, (long) i);
                            } catch (Throwable th) {
                                Log.e(MXCallsManager.LOG_TAG, "## refreshTurnServer() failed to start the timer", th);
                                if (MXCallsManager.this.mTurnServerTimer != null) {
                                    MXCallsManager.this.mTurnServerTimer.cancel();
                                    MXCallsManager.this.mTurnServerTimer = null;
                                }
                                MXCallsManager.this.refreshTurnServer();
                            }
                        }

                        public void onSuccess(JsonObject jsonObject) {
                            int i;
                            Log.d(MXCallsManager.LOG_TAG, "## refreshTurnServer () : onSuccess");
                            if (jsonObject != null) {
                                if (jsonObject.has("uris")) {
                                    synchronized (MXCallsManager.LOG_TAG) {
                                        MXCallsManager.this.mTurnServer = jsonObject;
                                    }
                                }
                                if (jsonObject.has("ttl")) {
                                    int i2 = 60000;
                                    try {
                                        i2 = jsonObject.get("ttl").getAsInt();
                                        i = (i2 * 9) / 10;
                                    } catch (Exception e) {
                                        String access$300 = MXCallsManager.LOG_TAG;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("Fail to retrieve ttl ");
                                        sb.append(e.getMessage());
                                        Log.e(access$300, sb.toString(), e);
                                        i = i2;
                                    }
                                    String access$3002 = MXCallsManager.LOG_TAG;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("## refreshTurnServer () : onSuccess : retry after ");
                                    sb2.append(i);
                                    sb2.append(" seconds");
                                    Log.d(access$3002, sb2.toString());
                                    restartAfter(i * 1000);
                                }
                            }
                        }

                        public void onNetworkError(Exception exc) {
                            Log.e(MXCallsManager.LOG_TAG, "## refreshTurnServer () : onNetworkError", exc);
                            restartAfter(60000);
                        }

                        public void onMatrixError(MatrixError matrixError) {
                            String access$300 = MXCallsManager.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("## refreshTurnServer () : onMatrixError() : ");
                            sb.append(matrixError.errcode);
                            Log.e(access$300, sb.toString());
                            if (TextUtils.equals(matrixError.errcode, MatrixError.LIMIT_EXCEEDED) && matrixError.retry_after_ms != null) {
                                String access$3002 = MXCallsManager.LOG_TAG;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("## refreshTurnServer () : onMatrixError() : retry after ");
                                sb2.append(matrixError.retry_after_ms);
                                sb2.append(" ms");
                                Log.e(access$3002, sb2.toString());
                                restartAfter(matrixError.retry_after_ms.intValue());
                            }
                        }

                        public void onUnexpectedError(Exception exc) {
                            Log.e(MXCallsManager.LOG_TAG, "## refreshTurnServer () : onUnexpectedError()", exc);
                        }
                    });
                }
            });
        }
    }

    public static String getConferenceUserId(String str) {
        byte[] bArr;
        if (str == null) {
            return null;
        }
        String str2 = (String) mConferenceUserIdByRoomId.get(str);
        if (str2 == null) {
            try {
                bArr = str.getBytes("UTF-8");
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("conferenceUserIdForRoom failed ");
                sb.append(e.getMessage());
                Log.e(str3, sb.toString(), e);
                bArr = null;
            }
            if (bArr == null) {
                return null;
            }
            String replace = Base64.encodeToString(bArr, 10).replace("=", "");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("@fs_");
            sb2.append(replace);
            sb2.append(":");
            sb2.append(DOMAIN);
            str2 = sb2.toString();
            mConferenceUserIdByRoomId.put(str, str2);
        }
        return str2;
    }

    public static boolean isConferenceUserId(String str) {
        if (mConferenceUserIdByRoomId.values().contains(str)) {
            return true;
        }
        boolean z = false;
        if (!TextUtils.isEmpty(str) && str.startsWith("@fs_") && str.endsWith(":matrix.org")) {
            try {
                z = MXPatterns.isRoomId(new String(Base64.decode(str.substring(4, str.length() - 11), 10), "UTF-8"));
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("isConferenceUserId : failed ");
                sb.append(e.getMessage());
                Log.e(str2, sb.toString(), e);
            }
        }
        return z;
    }

    private void inviteConferenceUser(Room room, final ApiCallback<Void> apiCallback) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("inviteConferenceUser ");
        sb.append(room.getRoomId());
        Log.d(str, sb.toString());
        String conferenceUserId = getConferenceUserId(room.getRoomId());
        RoomMember member = room.getMember(conferenceUserId);
        if (member == null || !TextUtils.equals(member.membership, "join")) {
            room.invite(conferenceUserId, apiCallback);
        } else {
            this.mUIThreadHandler.post(new Runnable() {
                public void run() {
                    apiCallback.onSuccess(null);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void getConferenceUserRoom(String str, final ApiCallback<Room> apiCallback) {
        final Room room;
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getConferenceUserRoom with room id ");
        sb.append(str);
        Log.d(str2, sb.toString());
        String conferenceUserId = getConferenceUserId(str);
        Iterator it = this.mSession.getDataHandler().getStore().getRooms().iterator();
        while (true) {
            if (!it.hasNext()) {
                room = null;
                break;
            }
            room = (Room) it.next();
            if (room.isConferenceUserRoom() && room.getNumberOfMembers() == 2 && room.getMember(conferenceUserId) != null) {
                break;
            }
        }
        if (room != null) {
            Log.d(LOG_TAG, "getConferenceUserRoom : the room already exists");
            this.mSession.getDataHandler().getStore().commit();
            this.mUIThreadHandler.post(new Runnable() {
                public void run() {
                    apiCallback.onSuccess(room);
                }
            });
            return;
        }
        Log.d(LOG_TAG, "getConferenceUserRoom : create the room");
        CreateRoomParams createRoomParams = new CreateRoomParams();
        createRoomParams.preset = CreateRoomParams.PRESET_PRIVATE_CHAT;
        createRoomParams.invitedUserIds = Arrays.asList(new String[]{conferenceUserId});
        this.mSession.createRoom(createRoomParams, new ApiCallback<String>() {
            public void onSuccess(String str) {
                Log.d(MXCallsManager.LOG_TAG, "getConferenceUserRoom : the room creation succeeds");
                Room room = MXCallsManager.this.mSession.getDataHandler().getRoom(str);
                if (room != null) {
                    room.setIsConferenceUserRoom(true);
                    MXCallsManager.this.mSession.getDataHandler().getStore().commit();
                    apiCallback.onSuccess(room);
                }
            }

            public void onNetworkError(Exception exc) {
                String access$300 = MXCallsManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("getConferenceUserRoom : failed ");
                sb.append(exc.getMessage());
                Log.e(access$300, sb.toString(), exc);
                apiCallback.onNetworkError(exc);
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$300 = MXCallsManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("getConferenceUserRoom : failed ");
                sb.append(matrixError.getMessage());
                Log.e(access$300, sb.toString());
                apiCallback.onMatrixError(matrixError);
            }

            public void onUnexpectedError(Exception exc) {
                String access$300 = MXCallsManager.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("getConferenceUserRoom : failed ");
                sb.append(exc.getMessage());
                Log.e(access$300, sb.toString(), exc);
                apiCallback.onUnexpectedError(exc);
            }
        });
    }

    public void addListener(IMXCallsManagerListener iMXCallsManagerListener) {
        if (iMXCallsManagerListener != null) {
            synchronized (this) {
                this.mListeners.add(iMXCallsManagerListener);
            }
        }
    }

    public void removeListener(IMXCallsManagerListener iMXCallsManagerListener) {
        if (iMXCallsManagerListener != null) {
            synchronized (this) {
                this.mListeners.remove(iMXCallsManagerListener);
            }
        }
    }

    private Collection<IMXCallsManagerListener> getListeners() {
        HashSet hashSet;
        synchronized (this) {
            hashSet = new HashSet(this.mListeners);
        }
        return hashSet;
    }

    /* access modifiers changed from: private */
    public void dispatchOnIncomingCall(IMXCall iMXCall, MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        String str2 = "dispatchOnIncomingCall ";
        sb.append(str2);
        sb.append(iMXCall.getCallId());
        Log.d(str, sb.toString());
        for (IMXCallsManagerListener onIncomingCall : getListeners()) {
            try {
                onIncomingCall.onIncomingCall(iMXCall, mXUsersDevicesMap);
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str2);
                sb2.append(e.getMessage());
                Log.e(str3, sb2.toString(), e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void dispatchOnOutgoingCall(IMXCall iMXCall) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        String str2 = "dispatchOnOutgoingCall ";
        sb.append(str2);
        sb.append(iMXCall.getCallId());
        Log.d(str, sb.toString());
        for (IMXCallsManagerListener onOutgoingCall : getListeners()) {
            try {
                onOutgoingCall.onOutgoingCall(iMXCall);
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str2);
                sb2.append(e.getMessage());
                Log.e(str3, sb2.toString(), e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void dispatchOnCallHangUp(IMXCall iMXCall) {
        Log.d(LOG_TAG, "dispatchOnCallHangUp");
        for (IMXCallsManagerListener onCallHangUp : getListeners()) {
            try {
                onCallHangUp.onCallHangUp(iMXCall);
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("dispatchOnCallHangUp ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void dispatchOnVoipConferenceStarted(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("dispatchOnVoipConferenceStarted : ");
        sb.append(str);
        Log.d(str2, sb.toString());
        for (IMXCallsManagerListener onVoipConferenceStarted : getListeners()) {
            try {
                onVoipConferenceStarted.onVoipConferenceStarted(str);
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("dispatchOnVoipConferenceStarted ");
                sb2.append(e.getMessage());
                Log.e(str3, sb2.toString(), e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void dispatchOnVoipConferenceFinished(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onVoipConferenceFinished : ");
        sb.append(str);
        Log.d(str2, sb.toString());
        for (IMXCallsManagerListener onVoipConferenceFinished : getListeners()) {
            try {
                onVoipConferenceFinished.onVoipConferenceFinished(str);
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("dispatchOnVoipConferenceFinished ");
                sb2.append(e.getMessage());
                Log.e(str3, sb2.toString(), e);
            }
        }
    }
}
