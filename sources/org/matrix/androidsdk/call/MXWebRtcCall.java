package org.matrix.androidsdk.call;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import androidx.core.content.ContextCompat;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oney.WebRTCModule.EglUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.annotation.Nullable;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.rest.model.Event;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;
import org.matrix.androidsdk.rest.model.terms.TermsResponse;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.DataChannel;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaConstraints.KeyValuePair;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnection.IceConnectionState;
import org.webrtc.PeerConnection.IceGatheringState;
import org.webrtc.PeerConnection.IceServer;
import org.webrtc.PeerConnection.Observer;
import org.webrtc.PeerConnection.SignalingState;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.PeerConnectionFactory.InitializationOptions;
import org.webrtc.PeerConnectionFactory.Options;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.SessionDescription.Type;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

public class MXWebRtcCall extends MXCall {
    private static final String AUDIO_TRACK_ID = "ARDAMSa0";
    private static final int CAMERA_TYPE_FRONT = 1;
    private static final int CAMERA_TYPE_REAR = 2;
    private static final int CAMERA_TYPE_UNDEFINED = -1;
    private static final int DEFAULT_FPS = 30;
    private static final int DEFAULT_HEIGHT = 360;
    private static final int DEFAULT_WIDTH = 640;
    /* access modifiers changed from: private */
    public static final String LOG_TAG = MXWebRtcCall.class.getSimpleName();
    private static final String VIDEO_TRACK_ID = "ARDAMSv0";
    private static String mBackCameraName = null;
    private static CameraVideoCapturer mCameraVideoCapturer = null;
    private static String mFrontCameraName = null;
    private static boolean mIsInitialized = false;
    private static Boolean mIsSupported;
    /* access modifiers changed from: private */
    public static PeerConnectionFactory mPeerConnectionFactory = null;
    private AudioSource mAudioSource = null;
    private JsonObject mCallInviteParams = null;
    private String mCallState = IMXCall.CALL_STATE_CREATED;
    /* access modifiers changed from: private */
    public RelativeLayout mCallView = null;
    private int mCameraInUse = -1;
    /* access modifiers changed from: private */
    public MXWebRtcView mFullScreenRTCView = null;
    private boolean mIsAnswered = false;
    private boolean mIsCameraSwitched;
    private boolean mIsCameraUnplugged = false;
    /* access modifiers changed from: private */
    public boolean mIsIncomingPrepared = false;
    private AudioTrack mLocalAudioTrack = null;
    /* access modifiers changed from: private */
    public MediaStream mLocalMediaStream = null;
    /* access modifiers changed from: private */
    public VideoTrack mLocalVideoTrack = null;
    /* access modifiers changed from: private */
    public PeerConnection mPeerConnection = null;
    private JsonArray mPendingCandidates = new JsonArray();
    /* access modifiers changed from: private */
    public MXWebRtcView mPipRTCView = null;
    /* access modifiers changed from: private */
    public VideoTrack mRemoteVideoTrack = null;
    /* access modifiers changed from: private */
    public boolean mUsingLargeLocalRenderer = true;
    private VideoSource mVideoSource = null;

    public static boolean isSupported(Context context) {
        if (mIsSupported == null) {
            initializeAndroidGlobals(context.getApplicationContext());
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("isSupported ");
            sb.append(mIsSupported);
            Log.d(str, sb.toString());
        }
        return mIsSupported.booleanValue();
    }

    private static boolean useCamera2(Context context) {
        return Camera2Enumerator.isSupported(context);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0025, code lost:
        r6 = r3;
     */
    private static boolean isCameraInUse(Context context, boolean z) {
        boolean z2 = false;
        if (useCamera2(context)) {
            return false;
        }
        int i = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        int i2 = 0;
        while (true) {
            if (i2 >= numberOfCameras) {
                break;
            }
            CameraInfo cameraInfo = new CameraInfo();
            Camera.getCameraInfo(i2, cameraInfo);
            if ((cameraInfo.facing != 1 || !z) && (cameraInfo.facing != 0 || z)) {
                i2++;
            }
        }
        if (i < 0) {
            return false;
        }
        try {
            Camera open = Camera.open(i);
            if (open == null) {
                z2 = true;
            }
            if (open == null) {
                return z2;
            }
            open.release();
            return z2;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## isCameraInUse() : failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
            return true;
        }
    }

    private static CameraEnumerator getCameraEnumerator(Context context) {
        if (useCamera2(context)) {
            return new Camera2Enumerator(context);
        }
        return new Camera1Enumerator(false);
    }

    public MXWebRtcCall(MXSession mXSession, Context context, JsonElement jsonElement) {
        if (!isSupported(context)) {
            throw new AssertionError("MXWebRtcCall : not supported with the current android version");
        } else if (mXSession == null) {
            throw new AssertionError("MXWebRtcCall : session cannot be null");
        } else if (context != null) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("MXWebRtcCall constructor ");
            sb.append(jsonElement);
            Log.d(str, sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("c");
            sb2.append(System.currentTimeMillis());
            this.mCallId = sb2.toString();
            this.mSession = mXSession;
            this.mContext = context;
            this.mTurnServer = jsonElement;
        } else {
            throw new AssertionError("MXWebRtcCall : context cannot be null");
        }
    }

    private static void initializeAndroidGlobals(Context context) {
        if (!mIsInitialized) {
            try {
                PeerConnectionFactory.initialize(InitializationOptions.builder(context).createInitializationOptions());
                mIsInitialized = true;
                PeerConnectionFactory.initializeFieldTrials(null);
                mIsSupported = Boolean.valueOf(true);
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## initializeAndroidGlobals(): mIsInitialized=");
                sb.append(mIsInitialized);
                Log.d(str, sb.toString());
            } catch (Throwable th) {
                String str2 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## initializeAndroidGlobals(): Exception Msg=");
                sb2.append(th.getMessage());
                Log.e(str2, sb2.toString(), th);
                mIsInitialized = true;
                mIsSupported = Boolean.valueOf(false);
            }
        }
    }

    public void createCallView() {
        super.createCallView();
        Boolean bool = mIsSupported;
        if (bool != null && bool.booleanValue()) {
            Log.d(LOG_TAG, "++ createCallView()");
            dispatchOnStateDidChange(IMXCall.CALL_STATE_CREATING_CALL_VIEW);
            this.mUIThreadHandler.postDelayed(new Runnable() {
                public void run() {
                    MXWebRtcCall mXWebRtcCall = MXWebRtcCall.this;
                    mXWebRtcCall.mCallView = new RelativeLayout(mXWebRtcCall.mContext);
                    MXWebRtcCall.this.mCallView.setLayoutParams(new LayoutParams(-1, -1));
                    MXWebRtcCall.this.mCallView.setBackgroundColor(ContextCompat.getColor(MXWebRtcCall.this.mContext, 17170444));
                    MXWebRtcCall.this.mCallView.setVisibility(8);
                    MXWebRtcCall mXWebRtcCall2 = MXWebRtcCall.this;
                    mXWebRtcCall2.dispatchOnCallViewCreated(mXWebRtcCall2.mCallView);
                    MXWebRtcCall.this.mUIThreadHandler.post(new Runnable() {
                        public void run() {
                            MXWebRtcCall.this.dispatchOnStateDidChange(IMXCall.CALL_STATE_READY);
                            MXWebRtcCall.this.dispatchOnReady();
                        }
                    });
                }
            }, 10);
        }
    }

    /* access modifiers changed from: private */
    public void terminate(final int i) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## terminate(): reason= ");
        sb.append(i);
        Log.d(str, sb.toString());
        if (!isCallEnded()) {
            dispatchOnStateDidChange(IMXCall.CALL_STATE_ENDED);
            boolean z = false;
            PeerConnection peerConnection = this.mPeerConnection;
            if (peerConnection != null) {
                peerConnection.dispose();
                this.mPeerConnection = null;
                z = true;
            }
            CameraVideoCapturer cameraVideoCapturer = mCameraVideoCapturer;
            if (cameraVideoCapturer != null) {
                cameraVideoCapturer.dispose();
                mCameraVideoCapturer = null;
            }
            VideoSource videoSource = this.mVideoSource;
            if (videoSource != null) {
                videoSource.dispose();
                this.mVideoSource = null;
            }
            AudioSource audioSource = this.mAudioSource;
            if (audioSource != null) {
                audioSource.dispose();
                this.mAudioSource = null;
            }
            if (z) {
                PeerConnectionFactory peerConnectionFactory = mPeerConnectionFactory;
                if (peerConnectionFactory != null) {
                    peerConnectionFactory.dispose();
                    mPeerConnectionFactory = null;
                }
            }
            final RelativeLayout relativeLayout = this.mCallView;
            if (relativeLayout != null) {
                relativeLayout.post(new Runnable() {
                    public void run() {
                        relativeLayout.setVisibility(8);
                    }
                });
                this.mCallView = null;
            }
            this.mUIThreadHandler.post(new Runnable() {
                public void run() {
                    MXWebRtcCall.this.dispatchOnCallEnd(i);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void sendInvite(SessionDescription sessionDescription) {
        if (isCallEnded()) {
            Log.d(LOG_TAG, "## sendInvite(): isCallEnded");
            return;
        }
        Log.d(LOG_TAG, "## sendInvite()");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(TermsResponse.VERSION, (Number) Integer.valueOf(0));
        jsonObject.addProperty("call_id", this.mCallId);
        jsonObject.addProperty("lifetime", (Number) Integer.valueOf(MXCall.CALL_TIMEOUT_MS));
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("sdp", sessionDescription.description);
        jsonObject2.addProperty(PasswordLoginParams.IDENTIFIER_KEY_TYPE, sessionDescription.type.canonicalForm());
        jsonObject.add("offer", jsonObject2);
        this.mPendingEvents.add(new Event(Event.EVENT_TYPE_CALL_INVITE, jsonObject, this.mSession.getCredentials().userId, this.mCallSignalingRoom.getRoomId()));
        try {
            this.mCallTimeoutTimer = new Timer();
            this.mCallTimeoutTimer.schedule(new TimerTask() {
                public void run() {
                    try {
                        if (MXWebRtcCall.this.getCallState().equals(IMXCall.CALL_STATE_RINGING) || MXWebRtcCall.this.getCallState().equals(IMXCall.CALL_STATE_INVITE_SENT)) {
                            Log.d(MXWebRtcCall.LOG_TAG, "sendInvite : CALL_ERROR_USER_NOT_RESPONDING");
                            MXWebRtcCall.this.dispatchOnCallError(IMXCall.CALL_ERROR_USER_NOT_RESPONDING);
                            MXWebRtcCall.this.hangup(null);
                        }
                        MXWebRtcCall.this.mCallTimeoutTimer.cancel();
                        MXWebRtcCall.this.mCallTimeoutTimer = null;
                    } catch (Exception e) {
                        String access$100 = MXWebRtcCall.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## sendInvite(): Exception Msg= ");
                        sb.append(e.getMessage());
                        Log.e(access$100, sb.toString(), e);
                    }
                }
            }, 120000);
        } catch (Throwable th) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## sendInvite(): failed ");
            sb.append(th.getMessage());
            Log.e(str, sb.toString(), th);
            if (this.mCallTimeoutTimer != null) {
                this.mCallTimeoutTimer.cancel();
                this.mCallTimeoutTimer = null;
            }
        }
        sendNextEvent();
    }

    /* access modifiers changed from: private */
    public void sendAnswer(SessionDescription sessionDescription) {
        if (isCallEnded()) {
            Log.d(LOG_TAG, "sendAnswer isCallEnded");
            return;
        }
        Log.d(LOG_TAG, "sendAnswer");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(TermsResponse.VERSION, (Number) Integer.valueOf(0));
        jsonObject.addProperty("call_id", this.mCallId);
        jsonObject.addProperty("lifetime", (Number) Integer.valueOf(MXCall.CALL_TIMEOUT_MS));
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("sdp", sessionDescription.description);
        jsonObject2.addProperty(PasswordLoginParams.IDENTIFIER_KEY_TYPE, sessionDescription.type.canonicalForm());
        jsonObject.add("answer", jsonObject2);
        this.mPendingEvents.add(new Event(Event.EVENT_TYPE_CALL_ANSWER, jsonObject, this.mSession.getCredentials().userId, this.mCallSignalingRoom.getRoomId()));
        sendNextEvent();
        this.mIsAnswered = true;
    }

    public void updateLocalVideoRendererPosition(VideoLayoutConfiguration videoLayoutConfiguration) {
        super.updateLocalVideoRendererPosition(videoLayoutConfiguration);
        try {
            updateWebRtcViewLayout(this.mPipRTCView, videoLayoutConfiguration);
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## updateLocalVideoRendererPosition(): Exception Msg=");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    public boolean isSwitchCameraSupported() {
        String[] deviceNames = getCameraEnumerator(this.mContext).getDeviceNames();
        return (deviceNames == null || deviceNames.length == 0) ? false : true;
    }

    public boolean switchRearFrontCamera() {
        if (mCameraVideoCapturer == null || !isSwitchCameraSupported()) {
            Log.w(LOG_TAG, "## switchRearFrontCamera(): failure - invalid values");
        } else {
            try {
                mCameraVideoCapturer.switchCamera(null);
                if (1 == this.mCameraInUse) {
                    this.mCameraInUse = 2;
                } else {
                    this.mCameraInUse = 1;
                }
                this.mIsCameraSwitched = !this.mIsCameraSwitched;
                return true;
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## switchRearFrontCamera(): failed ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
        }
        return false;
    }

    public void muteVideoRecording(boolean z) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## muteVideoRecording(): muteValue=");
        sb.append(z);
        Log.d(str, sb.toString());
        if (!isCallEnded()) {
            VideoTrack videoTrack = this.mLocalVideoTrack;
            if (videoTrack != null) {
                videoTrack.setEnabled(!z);
            } else {
                Log.d(LOG_TAG, "## muteVideoRecording(): failure - invalid value");
            }
        } else {
            Log.d(LOG_TAG, "## muteVideoRecording(): the call is ended");
        }
    }

    public boolean isVideoRecordingMuted() {
        boolean z = false;
        if (!isCallEnded()) {
            VideoTrack videoTrack = this.mLocalVideoTrack;
            if (videoTrack != null) {
                z = !videoTrack.enabled();
            } else {
                Log.w(LOG_TAG, "## isVideoRecordingMuted(): failure - invalid value");
            }
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## isVideoRecordingMuted() = ");
            sb.append(z);
            Log.d(str, sb.toString());
        } else {
            Log.d(LOG_TAG, "## isVideoRecordingMuted() : the call is ended");
        }
        return z;
    }

    public boolean isCameraSwitched() {
        return this.mIsCameraSwitched;
    }

    /* access modifiers changed from: private */
    public void createLocalStream() {
        String str = "password";
        String str2 = "username";
        Log.d(LOG_TAG, "## createLocalStream(): IN");
        if (this.mLocalVideoTrack == null && this.mLocalAudioTrack == null) {
            Log.d(LOG_TAG, "## createLocalStream(): CALL_ERROR_CALL_INIT_FAILED");
            dispatchOnCallError(IMXCall.CALL_ERROR_CALL_INIT_FAILED);
            hangup("no_stream");
            terminate(-1);
            return;
        }
        this.mLocalMediaStream = mPeerConnectionFactory.createLocalMediaStream("ARDAMS");
        VideoTrack videoTrack = this.mLocalVideoTrack;
        if (videoTrack != null) {
            this.mLocalMediaStream.addTrack(videoTrack);
        }
        AudioTrack audioTrack = this.mLocalAudioTrack;
        if (audioTrack != null) {
            this.mLocalMediaStream.addTrack(audioTrack);
        }
        MXWebRtcView mXWebRtcView = this.mFullScreenRTCView;
        if (mXWebRtcView != null) {
            mXWebRtcView.setStream(this.mLocalMediaStream);
            this.mFullScreenRTCView.setVisibility(0);
        }
        ArrayList arrayList = new ArrayList();
        if (this.mTurnServer != null) {
            try {
                JsonObject asJsonObject = this.mTurnServer.getAsJsonObject();
                String str3 = null;
                String asString = asJsonObject.has(str2) ? asJsonObject.get(str2).getAsString() : null;
                if (asJsonObject.has(str)) {
                    str3 = asJsonObject.get(str).getAsString();
                }
                JsonArray asJsonArray = asJsonObject.get("uris").getAsJsonArray();
                for (int i = 0; i < asJsonArray.size(); i++) {
                    String asString2 = asJsonArray.get(i).getAsString();
                    if (asString == null || str3 == null) {
                        arrayList.add(new IceServer(asString2));
                    } else {
                        arrayList.add(new IceServer(asString2, asString, str3));
                    }
                }
            } catch (Exception e) {
                String str4 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## createLocalStream(): Exception in ICE servers list Msg=");
                sb.append(e.getMessage());
                Log.e(str4, sb.toString(), e);
            }
        }
        String str5 = LOG_TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("## createLocalStream(): ");
        sb2.append(arrayList.size());
        sb2.append(" known ice servers");
        Log.d(str5, sb2.toString());
        if (arrayList.isEmpty()) {
            Log.d(LOG_TAG, "## createLocalStream(): No iceServers found ");
        }
        MediaConstraints mediaConstraints = new MediaConstraints();
        String str6 = "true";
        mediaConstraints.optional.add(new KeyValuePair("RtpDataChannels", str6));
        this.mPeerConnection = mPeerConnectionFactory.createPeerConnection(arrayList, mediaConstraints, new Observer() {
            public void onSignalingChange(SignalingState signalingState) {
                String access$100 = MXWebRtcCall.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## mPeerConnection creation: onSignalingChange state=");
                sb.append(signalingState);
                Log.d(access$100, sb.toString());
            }

            public void onIceConnectionChange(final IceConnectionState iceConnectionState) {
                String access$100 = MXWebRtcCall.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## mPeerConnection creation: onIceConnectionChange ");
                sb.append(iceConnectionState);
                Log.d(access$100, sb.toString());
                MXWebRtcCall.this.mUIThreadHandler.post(new Runnable() {
                    public void run() {
                        if (iceConnectionState == IceConnectionState.CONNECTED) {
                            if (MXWebRtcCall.this.mLocalVideoTrack != null && MXWebRtcCall.this.mUsingLargeLocalRenderer && MXWebRtcCall.this.isVideo()) {
                                MXWebRtcCall.this.mLocalVideoTrack.setEnabled(false);
                                if (!MXWebRtcCall.this.isConference()) {
                                    MXWebRtcCall.this.mPipRTCView.setStream(MXWebRtcCall.this.mLocalMediaStream);
                                    MXWebRtcCall.this.mPipRTCView.setVisibility(0);
                                    MXWebRtcCall.this.mPipRTCView.setZOrder(1);
                                }
                                MXWebRtcCall.this.mLocalVideoTrack.setEnabled(true);
                                MXWebRtcCall.this.mUsingLargeLocalRenderer = false;
                                MXWebRtcCall.this.mCallView.post(new Runnable() {
                                    public void run() {
                                        if (MXWebRtcCall.this.mCallView != null) {
                                            MXWebRtcCall.this.mCallView.invalidate();
                                        }
                                    }
                                });
                            }
                            MXWebRtcCall.this.dispatchOnStateDidChange(IMXCall.CALL_STATE_CONNECTED);
                        } else if (iceConnectionState == IceConnectionState.FAILED) {
                            MXWebRtcCall.this.dispatchOnCallError(IMXCall.CALL_ERROR_ICE_FAILED);
                            MXWebRtcCall.this.hangup("ice_failed");
                        }
                    }
                });
            }

            public void onIceConnectionReceivingChange(boolean z) {
                String access$100 = MXWebRtcCall.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## mPeerConnection creation: onIceConnectionReceivingChange ");
                sb.append(z);
                Log.d(access$100, sb.toString());
            }

            public void onIceCandidatesRemoved(IceCandidate[] iceCandidateArr) {
                String access$100 = MXWebRtcCall.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## mPeerConnection creation: onIceCandidatesRemoved ");
                sb.append(iceCandidateArr);
                Log.d(access$100, sb.toString());
            }

            public void onIceGatheringChange(IceGatheringState iceGatheringState) {
                String access$100 = MXWebRtcCall.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## mPeerConnection creation: onIceGatheringChange ");
                sb.append(iceGatheringState);
                Log.d(access$100, sb.toString());
            }

            public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreamArr) {
                String access$100 = MXWebRtcCall.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## mPeerConnection creation: onAddTrack ");
                sb.append(rtpReceiver);
                sb.append(" -- ");
                sb.append(mediaStreamArr);
                Log.d(access$100, sb.toString());
            }

            public void onIceCandidate(final IceCandidate iceCandidate) {
                String access$100 = MXWebRtcCall.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## mPeerConnection creation: onIceCandidate ");
                sb.append(iceCandidate);
                Log.d(access$100, sb.toString());
                MXWebRtcCall.this.mUIThreadHandler.post(new Runnable() {
                    public void run() {
                        if (!MXWebRtcCall.this.isCallEnded()) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty(TermsResponse.VERSION, (Number) Integer.valueOf(0));
                            jsonObject.addProperty("call_id", MXWebRtcCall.this.mCallId);
                            JsonArray jsonArray = new JsonArray();
                            JsonObject jsonObject2 = new JsonObject();
                            jsonObject2.addProperty("sdpMLineIndex", (Number) Integer.valueOf(iceCandidate.sdpMLineIndex));
                            jsonObject2.addProperty("sdpMid", iceCandidate.sdpMid);
                            jsonObject2.addProperty("candidate", iceCandidate.sdp);
                            jsonArray.add((JsonElement) jsonObject2);
                            String str = "candidates";
                            jsonObject.add(str, jsonArray);
                            int size = MXWebRtcCall.this.mPendingEvents.size();
                            String str2 = Event.EVENT_TYPE_CALL_CANDIDATES;
                            boolean z = true;
                            if (size > 0) {
                                try {
                                    Event event = (Event) MXWebRtcCall.this.mPendingEvents.get(MXWebRtcCall.this.mPendingEvents.size() - 1);
                                    if (TextUtils.equals(event.getType(), str2)) {
                                        JsonObject contentAsJsonObject = event.getContentAsJsonObject();
                                        JsonArray asJsonArray = contentAsJsonObject.get(str).getAsJsonArray();
                                        JsonArray asJsonArray2 = jsonObject.get(str).getAsJsonArray();
                                        String access$100 = MXWebRtcCall.LOG_TAG;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("Merge candidates from ");
                                        sb.append(asJsonArray.size());
                                        sb.append(" to ");
                                        sb.append(asJsonArray.size() + asJsonArray2.size());
                                        sb.append(" items.");
                                        Log.d(access$100, sb.toString());
                                        asJsonArray.addAll(asJsonArray2);
                                        contentAsJsonObject.remove(str);
                                        contentAsJsonObject.add(str, asJsonArray);
                                        z = false;
                                    }
                                } catch (Exception e) {
                                    String access$1002 = MXWebRtcCall.LOG_TAG;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("## createLocalStream(): createPeerConnection - onIceCandidate() Exception Msg=");
                                    sb2.append(e.getMessage());
                                    Log.e(access$1002, sb2.toString(), e);
                                }
                            }
                            if (z) {
                                MXWebRtcCall.this.mPendingEvents.add(new Event(str2, jsonObject, MXWebRtcCall.this.mSession.getCredentials().userId, MXWebRtcCall.this.mCallSignalingRoom.getRoomId()));
                                MXWebRtcCall.this.sendNextEvent();
                            }
                        }
                    }
                });
            }

            public void onAddStream(final MediaStream mediaStream) {
                String access$100 = MXWebRtcCall.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## mPeerConnection creation: onAddStream ");
                sb.append(mediaStream);
                Log.d(access$100, sb.toString());
                MXWebRtcCall.this.mUIThreadHandler.post(new Runnable() {
                    public void run() {
                        if (mediaStream.videoTracks.size() == 1 && !MXWebRtcCall.this.isCallEnded()) {
                            MXWebRtcCall.this.mRemoteVideoTrack = (VideoTrack) mediaStream.videoTracks.get(0);
                            MXWebRtcCall.this.mRemoteVideoTrack.setEnabled(true);
                            MXWebRtcCall.this.mFullScreenRTCView.setStream(mediaStream);
                            MXWebRtcCall.this.mFullScreenRTCView.setVisibility(0);
                        }
                    }
                });
            }

            public void onRemoveStream(final MediaStream mediaStream) {
                String access$100 = MXWebRtcCall.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## mPeerConnection creation: onRemoveStream ");
                sb.append(mediaStream);
                Log.d(access$100, sb.toString());
                MXWebRtcCall.this.mUIThreadHandler.post(new Runnable() {
                    public void run() {
                        if (MXWebRtcCall.this.mRemoteVideoTrack != null) {
                            MXWebRtcCall.this.mRemoteVideoTrack.dispose();
                            MXWebRtcCall.this.mRemoteVideoTrack = null;
                            ((VideoTrack) mediaStream.videoTracks.get(0)).dispose();
                        }
                    }
                });
            }

            public void onDataChannel(DataChannel dataChannel) {
                String access$100 = MXWebRtcCall.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## mPeerConnection creation: onDataChannel ");
                sb.append(dataChannel);
                Log.d(access$100, sb.toString());
            }

            public void onRenegotiationNeeded() {
                Log.d(MXWebRtcCall.LOG_TAG, "## mPeerConnection creation: onRenegotiationNeeded");
            }
        });
        PeerConnection peerConnection = this.mPeerConnection;
        if (peerConnection == null) {
            dispatchOnCallError(IMXCall.CALL_ERROR_ICE_FAILED);
            hangup("cannot create peer connection");
            return;
        }
        peerConnection.addStream(this.mLocalMediaStream);
        MediaConstraints mediaConstraints2 = new MediaConstraints();
        mediaConstraints2.mandatory.add(new KeyValuePair("OfferToReceiveAudio", str6));
        List list = mediaConstraints2.mandatory;
        if (!isVideo()) {
            str6 = "false";
        }
        list.add(new KeyValuePair("OfferToReceiveVideo", str6));
        if (!isIncoming()) {
            Log.d(LOG_TAG, "## createLocalStream(): !isIncoming() -> createOffer");
            this.mPeerConnection.createOffer(new SdpObserver() {
                public void onCreateSuccess(SessionDescription sessionDescription) {
                    Log.d(MXWebRtcCall.LOG_TAG, "createOffer onCreateSuccess");
                    final SessionDescription sessionDescription2 = new SessionDescription(sessionDescription.type, sessionDescription.description);
                    MXWebRtcCall.this.mUIThreadHandler.post(new Runnable() {
                        public void run() {
                            if (MXWebRtcCall.this.mPeerConnection != null) {
                                MXWebRtcCall.this.mPeerConnection.setLocalDescription(new SdpObserver() {
                                    public void onCreateSuccess(SessionDescription sessionDescription) {
                                        Log.d(MXWebRtcCall.LOG_TAG, "setLocalDescription onCreateSuccess");
                                    }

                                    public void onSetSuccess() {
                                        Log.d(MXWebRtcCall.LOG_TAG, "setLocalDescription onSetSuccess");
                                        MXWebRtcCall.this.sendInvite(sessionDescription2);
                                        MXWebRtcCall.this.dispatchOnStateDidChange(IMXCall.CALL_STATE_INVITE_SENT);
                                    }

                                    public void onCreateFailure(String str) {
                                        String access$100 = MXWebRtcCall.LOG_TAG;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("setLocalDescription onCreateFailure ");
                                        sb.append(str);
                                        Log.e(access$100, sb.toString());
                                        MXWebRtcCall.this.dispatchOnCallError(IMXCall.CALL_ERROR_CAMERA_INIT_FAILED);
                                        MXWebRtcCall.this.hangup(null);
                                    }

                                    public void onSetFailure(String str) {
                                        String access$100 = MXWebRtcCall.LOG_TAG;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("setLocalDescription onSetFailure ");
                                        sb.append(str);
                                        Log.e(access$100, sb.toString());
                                        MXWebRtcCall.this.dispatchOnCallError(IMXCall.CALL_ERROR_CAMERA_INIT_FAILED);
                                        MXWebRtcCall.this.hangup(null);
                                    }
                                }, sessionDescription2);
                            }
                        }
                    });
                }

                public void onSetSuccess() {
                    Log.d(MXWebRtcCall.LOG_TAG, "createOffer onSetSuccess");
                }

                public void onCreateFailure(String str) {
                    String access$100 = MXWebRtcCall.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("createOffer onCreateFailure ");
                    sb.append(str);
                    Log.d(access$100, sb.toString());
                    MXWebRtcCall.this.dispatchOnCallError(IMXCall.CALL_ERROR_CAMERA_INIT_FAILED);
                }

                public void onSetFailure(String str) {
                    String access$100 = MXWebRtcCall.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("createOffer onSetFailure ");
                    sb.append(str);
                    Log.d(access$100, sb.toString());
                    MXWebRtcCall.this.dispatchOnCallError(IMXCall.CALL_ERROR_CAMERA_INIT_FAILED);
                }
            }, mediaConstraints2);
            dispatchOnStateDidChange(IMXCall.CALL_STATE_WAIT_CREATE_OFFER);
        }
    }

    private boolean hasCameraDevice() {
        int i;
        CameraEnumerator cameraEnumerator = getCameraEnumerator(this.mContext);
        String[] deviceNames = cameraEnumerator.getDeviceNames();
        mFrontCameraName = null;
        mBackCameraName = null;
        if (deviceNames != null) {
            for (String str : deviceNames) {
                if (cameraEnumerator.isFrontFacing(str) && !isCameraInUse(this.mContext, true)) {
                    mFrontCameraName = str;
                } else if (cameraEnumerator.isBackFacing(str) && !isCameraInUse(this.mContext, false)) {
                    mBackCameraName = str;
                }
            }
            i = deviceNames.length;
        } else {
            i = 0;
        }
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("hasCameraDevice():  camera number= ");
        sb.append(i);
        Log.d(str2, sb.toString());
        String str3 = LOG_TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("hasCameraDevice():  frontCameraName=");
        sb2.append(mFrontCameraName);
        sb2.append(" backCameraName=");
        sb2.append(mBackCameraName);
        Log.d(str3, sb2.toString());
        if (mFrontCameraName == null && mBackCameraName == null) {
            return false;
        }
        return true;
    }

    private CameraVideoCapturer createVideoCapturer(String str) {
        CameraEnumerator cameraEnumerator = getCameraEnumerator(this.mContext);
        String[] deviceNames = cameraEnumerator.getDeviceNames();
        if (deviceNames == null || deviceNames.length <= 0) {
            return null;
        }
        CameraVideoCapturer cameraVideoCapturer = null;
        for (String str2 : deviceNames) {
            if (str2.equals(str)) {
                cameraVideoCapturer = cameraEnumerator.createCapturer(str2, null);
                if (cameraVideoCapturer != null) {
                    break;
                }
            }
        }
        return cameraVideoCapturer == null ? cameraEnumerator.createCapturer(deviceNames[0], null) : cameraVideoCapturer;
    }

    /* access modifiers changed from: private */
    public void createVideoTrack() {
        Log.d(LOG_TAG, "createVideoTrack");
        if (hasCameraDevice()) {
            try {
                if (mCameraVideoCapturer != null) {
                    mCameraVideoCapturer.dispose();
                    mCameraVideoCapturer = null;
                }
                if (mFrontCameraName != null) {
                    mCameraVideoCapturer = createVideoCapturer(mFrontCameraName);
                    if (mCameraVideoCapturer == null) {
                        Log.e(LOG_TAG, "Cannot create Video Capturer from front camera");
                    } else {
                        this.mCameraInUse = 1;
                    }
                }
                if (mCameraVideoCapturer == null && mBackCameraName != null) {
                    mCameraVideoCapturer = createVideoCapturer(mBackCameraName);
                    if (mCameraVideoCapturer == null) {
                        Log.e(LOG_TAG, "Cannot create Video Capturer from back camera");
                    } else {
                        this.mCameraInUse = 2;
                    }
                }
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("createVideoTrack(): Exception Msg=");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
            if (mCameraVideoCapturer != null) {
                Log.d(LOG_TAG, "createVideoTrack find a video capturer");
                try {
                    SurfaceTextureHelper create = SurfaceTextureHelper.create("CaptureThread", EglUtils.getRootEglBase().getEglBaseContext());
                    this.mVideoSource = mPeerConnectionFactory.createVideoSource(mCameraVideoCapturer.isScreencast());
                    mCameraVideoCapturer.initialize(create, this.mContext, this.mVideoSource.getCapturerObserver());
                    mCameraVideoCapturer.startCapture(640, DEFAULT_HEIGHT, 30);
                    this.mLocalVideoTrack = mPeerConnectionFactory.createVideoTrack(VIDEO_TRACK_ID, this.mVideoSource);
                    this.mLocalVideoTrack.setEnabled(true);
                } catch (Exception e2) {
                    String str2 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("createVideoSource fails with exception ");
                    sb2.append(e2.getMessage());
                    Log.e(str2, sb2.toString(), e2);
                    this.mLocalVideoTrack = null;
                    VideoSource videoSource = this.mVideoSource;
                    if (videoSource != null) {
                        videoSource.dispose();
                        this.mVideoSource = null;
                    }
                }
            } else {
                Log.e(LOG_TAG, "## createVideoTrack(): Cannot create Video Capturer - no camera available");
            }
        }
    }

    /* access modifiers changed from: private */
    public void createAudioTrack() {
        Log.d(LOG_TAG, "createAudioTrack");
        MediaConstraints mediaConstraints = new MediaConstraints();
        String str = "true";
        mediaConstraints.mandatory.add(new KeyValuePair("googEchoCancellation", str));
        mediaConstraints.mandatory.add(new KeyValuePair("googEchoCancellation2", str));
        mediaConstraints.mandatory.add(new KeyValuePair("googDAEchoCancellation", str));
        mediaConstraints.mandatory.add(new KeyValuePair("googTypingNoiseDetection", str));
        mediaConstraints.mandatory.add(new KeyValuePair("googAutoGainControl", str));
        mediaConstraints.mandatory.add(new KeyValuePair("googAutoGainControl2", str));
        mediaConstraints.mandatory.add(new KeyValuePair("googNoiseSuppression", str));
        mediaConstraints.mandatory.add(new KeyValuePair("googNoiseSuppression2", str));
        mediaConstraints.mandatory.add(new KeyValuePair("googAudioMirroring", "false"));
        mediaConstraints.mandatory.add(new KeyValuePair("googHighpassFilter", str));
        this.mAudioSource = mPeerConnectionFactory.createAudioSource(mediaConstraints);
        this.mLocalAudioTrack = mPeerConnectionFactory.createAudioTrack(AUDIO_TRACK_ID, this.mAudioSource);
    }

    private void updateWebRtcViewLayout(MXWebRtcView mXWebRtcView, VideoLayoutConfiguration videoLayoutConfiguration) {
        if (mXWebRtcView != null) {
            DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
            int i = videoLayoutConfiguration.mDisplayWidth > 0 ? videoLayoutConfiguration.mDisplayWidth : displayMetrics.widthPixels;
            int i2 = videoLayoutConfiguration.mDisplayHeight > 0 ? videoLayoutConfiguration.mDisplayHeight : displayMetrics.heightPixels;
            int i3 = (videoLayoutConfiguration.mX * i) / 100;
            int i4 = (videoLayoutConfiguration.mY * i2) / 100;
            LayoutParams layoutParams = new LayoutParams((i * videoLayoutConfiguration.mWidth) / 100, (i2 * videoLayoutConfiguration.mHeight) / 100);
            layoutParams.leftMargin = i3;
            layoutParams.topMargin = i4;
            mXWebRtcView.setLayoutParams(layoutParams);
        }
    }

    /* access modifiers changed from: private */
    public void initCallUI(final JsonObject jsonObject, VideoLayoutConfiguration videoLayoutConfiguration) {
        Log.d(LOG_TAG, "## initCallUI(): IN");
        if (isCallEnded()) {
            Log.w(LOG_TAG, "## initCallUI(): skipped due to call is ended");
            return;
        }
        if (isVideo()) {
            Log.d(LOG_TAG, "## initCallUI(): building UI video call");
            try {
                this.mUIThreadHandler.post(new Runnable() {
                    public void run() {
                        if (MXWebRtcCall.mPeerConnectionFactory == null) {
                            Log.d(MXWebRtcCall.LOG_TAG, "## initCallUI(): video call and no mPeerConnectionFactory");
                            Options options = new Options();
                            DefaultVideoEncoderFactory defaultVideoEncoderFactory = new DefaultVideoEncoderFactory(EglUtils.getRootEglBase().getEglBaseContext(), true, true);
                            MXWebRtcCall.mPeerConnectionFactory = PeerConnectionFactory.builder().setOptions(options).setVideoEncoderFactory(defaultVideoEncoderFactory).setVideoDecoderFactory(new DefaultVideoDecoderFactory(EglUtils.getRootEglBase().getEglBaseContext())).createPeerConnectionFactory();
                            MXWebRtcCall.this.createVideoTrack();
                            MXWebRtcCall.this.createAudioTrack();
                            MXWebRtcCall.this.createLocalStream();
                            if (jsonObject != null) {
                                MXWebRtcCall.this.dispatchOnStateDidChange(IMXCall.CALL_STATE_RINGING);
                                MXWebRtcCall.this.setRemoteDescription(jsonObject);
                            }
                        }
                    }
                });
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## initCallUI(): VideoRendererGui.setView : Exception Msg =");
                sb.append(e.getMessage());
                Log.e(str, sb.toString(), e);
            }
            try {
                Log.d(LOG_TAG, "## initCallUI() building UI");
                this.mFullScreenRTCView = new MXWebRtcView(this.mContext);
                this.mFullScreenRTCView.setBackgroundColor(ContextCompat.getColor(this.mContext, 17170444));
                this.mCallView.addView(this.mFullScreenRTCView, new LayoutParams(-1, -1));
                this.mFullScreenRTCView.setVisibility(8);
                this.mPipRTCView = new MXWebRtcView(this.mContext);
                this.mCallView.addView(this.mPipRTCView, new LayoutParams(-1, -1));
                this.mPipRTCView.setBackgroundColor(ContextCompat.getColor(this.mContext, 17170445));
                this.mPipRTCView.setVisibility(8);
                if (videoLayoutConfiguration != null) {
                    updateWebRtcViewLayout(this.mPipRTCView, videoLayoutConfiguration);
                    String str2 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## initCallUI(): ");
                    sb2.append(videoLayoutConfiguration);
                    Log.d(str2, sb2.toString());
                } else {
                    updateWebRtcViewLayout(this.mPipRTCView, new VideoLayoutConfiguration(5, 5, 25, 25));
                }
            } catch (Exception e2) {
                String str3 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("## initCallUI(): Exception Msg =");
                sb3.append(e2.getMessage());
                Log.e(str3, sb3.toString(), e2);
            }
            RelativeLayout relativeLayout = this.mCallView;
            if (relativeLayout != null) {
                relativeLayout.setVisibility(0);
            }
        } else {
            Log.d(LOG_TAG, "## initCallUI(): build audio call");
            this.mUIThreadHandler.post(new Runnable() {
                public void run() {
                    if (MXWebRtcCall.mPeerConnectionFactory == null) {
                        MXWebRtcCall.mPeerConnectionFactory = PeerConnectionFactory.builder().createPeerConnectionFactory();
                        MXWebRtcCall.this.createAudioTrack();
                        MXWebRtcCall.this.createLocalStream();
                        if (jsonObject != null) {
                            MXWebRtcCall.this.dispatchOnStateDidChange(IMXCall.CALL_STATE_RINGING);
                            MXWebRtcCall.this.setRemoteDescription(jsonObject);
                        }
                    }
                }
            });
        }
    }

    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
        try {
            if (!isCallEnded()) {
                Log.d(LOG_TAG, "onPause with active call");
                if (!isVideoRecordingMuted()) {
                    muteVideoRecording(true);
                    this.mIsCameraUnplugged = true;
                }
            }
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onPause failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
        try {
            if (!isCallEnded()) {
                Log.d(LOG_TAG, "onResume with active call");
                if (this.mIsCameraUnplugged) {
                    muteVideoRecording(false);
                    this.mIsCameraUnplugged = false;
                }
            }
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onResume failed ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString(), e);
        }
    }

    public void placeCall(VideoLayoutConfiguration videoLayoutConfiguration) {
        Log.d(LOG_TAG, "placeCall");
        super.placeCall(videoLayoutConfiguration);
        dispatchOnStateDidChange(IMXCall.CALL_STATE_WAIT_LOCAL_MEDIA);
        initCallUI(null, videoLayoutConfiguration);
    }

    /* access modifiers changed from: private */
    public void setRemoteDescription(JsonObject jsonObject) {
        String str = "offer";
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setRemoteDescription ");
        sb.append(jsonObject);
        Log.d(str2, sb.toString());
        SessionDescription sessionDescription = null;
        try {
            if (jsonObject.has(str)) {
                JsonObject asJsonObject = jsonObject.getAsJsonObject(str);
                String asString = asJsonObject.get(PasswordLoginParams.IDENTIFIER_KEY_TYPE).getAsString();
                String asString2 = asJsonObject.get("sdp").getAsString();
                if (!TextUtils.isEmpty(asString) && !TextUtils.isEmpty(asString2)) {
                    sessionDescription = new SessionDescription(Type.OFFER, asString2);
                }
            }
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("## setRemoteDescription(): Exception Msg=");
            sb2.append(e.getMessage());
            Log.e(str3, sb2.toString(), e);
        }
        this.mPeerConnection.setRemoteDescription(new SdpObserver() {
            public void onCreateSuccess(SessionDescription sessionDescription) {
                Log.d(MXWebRtcCall.LOG_TAG, "setRemoteDescription onCreateSuccess");
            }

            public void onSetSuccess() {
                Log.d(MXWebRtcCall.LOG_TAG, "setRemoteDescription onSetSuccess");
                MXWebRtcCall.this.mIsIncomingPrepared = true;
                MXWebRtcCall.this.mUIThreadHandler.post(new Runnable() {
                    public void run() {
                        MXWebRtcCall.this.checkPendingCandidates();
                    }
                });
            }

            public void onCreateFailure(String str) {
                String access$100 = MXWebRtcCall.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("setRemoteDescription onCreateFailure ");
                sb.append(str);
                Log.e(access$100, sb.toString());
                MXWebRtcCall.this.dispatchOnCallError(IMXCall.CALL_ERROR_CAMERA_INIT_FAILED);
            }

            public void onSetFailure(String str) {
                String access$100 = MXWebRtcCall.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("setRemoteDescription onSetFailure ");
                sb.append(str);
                Log.e(access$100, sb.toString());
                MXWebRtcCall.this.dispatchOnCallError(IMXCall.CALL_ERROR_CAMERA_INIT_FAILED);
            }
        }, sessionDescription);
    }

    public void prepareIncomingCall(final JsonObject jsonObject, String str, final VideoLayoutConfiguration videoLayoutConfiguration) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## prepareIncomingCall : call state ");
        sb.append(getCallState());
        Log.d(str2, sb.toString());
        super.prepareIncomingCall(jsonObject, str, videoLayoutConfiguration);
        this.mCallId = str;
        if (IMXCall.CALL_STATE_READY.equals(getCallState())) {
            this.mIsIncoming = true;
            dispatchOnStateDidChange(IMXCall.CALL_STATE_WAIT_LOCAL_MEDIA);
            this.mUIThreadHandler.post(new Runnable() {
                public void run() {
                    MXWebRtcCall.this.initCallUI(jsonObject, videoLayoutConfiguration);
                }
            });
            return;
        }
        if (IMXCall.CALL_STATE_CREATED.equals(getCallState())) {
            this.mCallInviteParams = jsonObject;
            try {
                setIsVideo(this.mCallInviteParams.get("offer").getAsJsonObject().get("sdp").getAsString().contains("m=video"));
            } catch (Exception e) {
                String str3 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## prepareIncomingCall(): Exception Msg=");
                sb2.append(e.getMessage());
                Log.e(str3, sb2.toString(), e);
            }
        }
    }

    public void launchIncomingCall(VideoLayoutConfiguration videoLayoutConfiguration) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("launchIncomingCall : call state ");
        sb.append(getCallState());
        Log.d(str, sb.toString());
        super.launchIncomingCall(videoLayoutConfiguration);
        if (IMXCall.CALL_STATE_READY.equals(getCallState())) {
            prepareIncomingCall(this.mCallInviteParams, this.mCallId, videoLayoutConfiguration);
        }
    }

    private void onCallAnswer(final Event event) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onCallAnswer : call state ");
        sb.append(getCallState());
        Log.d(str, sb.toString());
        if (!IMXCall.CALL_STATE_CREATED.equals(getCallState()) && this.mPeerConnection != null) {
            this.mUIThreadHandler.post(new Runnable() {
                public void run() {
                    String str = "answer";
                    MXWebRtcCall.this.dispatchOnStateDidChange(IMXCall.CALL_STATE_CONNECTING);
                    SessionDescription sessionDescription = null;
                    try {
                        JsonObject contentAsJsonObject = event.getContentAsJsonObject();
                        if (contentAsJsonObject.has(str)) {
                            JsonObject asJsonObject = contentAsJsonObject.getAsJsonObject(str);
                            String asString = asJsonObject.get(PasswordLoginParams.IDENTIFIER_KEY_TYPE).getAsString();
                            String asString2 = asJsonObject.get("sdp").getAsString();
                            if (!TextUtils.isEmpty(asString) && !TextUtils.isEmpty(asString2) && asString.equals(str)) {
                                sessionDescription = new SessionDescription(Type.ANSWER, asString2);
                            }
                        }
                    } catch (Exception e) {
                        String access$100 = MXWebRtcCall.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onCallAnswer : ");
                        sb.append(e.getMessage());
                        Log.e(access$100, sb.toString(), e);
                    }
                    MXWebRtcCall.this.mPeerConnection.setRemoteDescription(new SdpObserver() {
                        public void onCreateSuccess(SessionDescription sessionDescription) {
                            Log.d(MXWebRtcCall.LOG_TAG, "setRemoteDescription onCreateSuccess");
                        }

                        public void onSetSuccess() {
                            Log.d(MXWebRtcCall.LOG_TAG, "setRemoteDescription onSetSuccess");
                        }

                        public void onCreateFailure(String str) {
                            String access$100 = MXWebRtcCall.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("setRemoteDescription onCreateFailure ");
                            sb.append(str);
                            Log.e(access$100, sb.toString());
                            MXWebRtcCall.this.dispatchOnCallError(IMXCall.CALL_ERROR_CAMERA_INIT_FAILED);
                        }

                        public void onSetFailure(String str) {
                            String access$100 = MXWebRtcCall.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("setRemoteDescription onSetFailure ");
                            sb.append(str);
                            Log.e(access$100, sb.toString());
                            MXWebRtcCall.this.dispatchOnCallError(IMXCall.CALL_ERROR_CAMERA_INIT_FAILED);
                        }
                    }, sessionDescription);
                }
            });
        }
    }

    private void onCallHangup(final int i) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onCallHangup(): call state=");
        sb.append(getCallState());
        Log.d(str, sb.toString());
        String callState = getCallState();
        if (!IMXCall.CALL_STATE_CREATED.equals(callState) && this.mPeerConnection != null) {
            this.mUIThreadHandler.post(new Runnable() {
                public void run() {
                    MXWebRtcCall.this.terminate(i);
                }
            });
        } else if (IMXCall.CALL_STATE_WAIT_LOCAL_MEDIA.equals(callState) && isVideo()) {
            this.mUIThreadHandler.post(new Runnable() {
                public void run() {
                    MXWebRtcCall.this.terminate(i);
                }
            });
        }
    }

    private void onNewCandidates(JsonArray jsonArray) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onNewCandidates(): call state ");
        sb.append(getCallState());
        sb.append(" with candidates ");
        sb.append(jsonArray);
        Log.d(str, sb.toString());
        if (!IMXCall.CALL_STATE_CREATED.equals(getCallState()) && this.mPeerConnection != null) {
            ArrayList<IceCandidate> arrayList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject asJsonObject = jsonArray.get(i).getAsJsonObject();
                try {
                    arrayList.add(new IceCandidate(asJsonObject.get("sdpMid").getAsString(), asJsonObject.get("sdpMLineIndex").getAsInt(), asJsonObject.get("candidate").getAsString()));
                } catch (Exception e) {
                    String str2 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## onNewCandidates(): Exception Msg=");
                    sb2.append(e.getMessage());
                    Log.e(str2, sb2.toString(), e);
                }
            }
            for (IceCandidate iceCandidate : arrayList) {
                String str3 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("## onNewCandidates(): addIceCandidate ");
                sb3.append(iceCandidate);
                Log.d(str3, sb3.toString());
                this.mPeerConnection.addIceCandidate(iceCandidate);
            }
        }
    }

    private void addCandidates(JsonArray jsonArray) {
        if (this.mIsIncomingPrepared || !isIncoming()) {
            Log.d(LOG_TAG, "addCandidates : ready");
            onNewCandidates(jsonArray);
            return;
        }
        synchronized (LOG_TAG) {
            Log.d(LOG_TAG, "addCandidates : pending");
            this.mPendingCandidates.addAll(jsonArray);
        }
    }

    /* access modifiers changed from: private */
    public void checkPendingCandidates() {
        Log.d(LOG_TAG, "checkPendingCandidates");
        synchronized (LOG_TAG) {
            onNewCandidates(this.mPendingCandidates);
            this.mPendingCandidates = new JsonArray();
        }
    }

    public void handleCallEvent(Event event) {
        super.handleCallEvent(event);
        if (event.isCallEvent()) {
            String type = event.getType();
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("handleCallEvent ");
            sb.append(type);
            Log.d(str, sb.toString());
            boolean equals = TextUtils.equals(event.getSender(), this.mSession.getMyUserId());
            String str2 = Event.EVENT_TYPE_CALL_HANGUP;
            String str3 = Event.EVENT_TYPE_CALL_ANSWER;
            if (equals) {
                char c = 65535;
                int hashCode = type.hashCode();
                if (hashCode != -1593761459) {
                    if (hashCode != -1405527012) {
                        if (hashCode == -1364651880 && type.equals(Event.EVENT_TYPE_CALL_INVITE)) {
                            c = 0;
                        }
                    } else if (type.equals(str2)) {
                        c = 2;
                    }
                } else if (type.equals(str3)) {
                    c = 1;
                }
                if (c == 0) {
                    this.mUIThreadHandler.post(new Runnable() {
                        public void run() {
                            MXWebRtcCall.this.dispatchOnStateDidChange(IMXCall.CALL_STATE_RINGING);
                        }
                    });
                } else if (c == 1) {
                    this.mUIThreadHandler.post(new Runnable() {
                        public void run() {
                            MXWebRtcCall.this.onAnsweredElsewhere();
                        }
                    });
                } else if (c == 2) {
                    onCallHangup(1);
                }
            } else if (str3.equals(type) && !this.mIsIncoming) {
                onCallAnswer(event);
            } else if (Event.EVENT_TYPE_CALL_CANDIDATES.equals(type)) {
                addCandidates(event.getContentAsJsonObject().getAsJsonArray("candidates"));
            } else if (str2.equals(type)) {
                onCallHangup(0);
            }
        }
    }

    public void answer() {
        super.answer();
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("answer ");
        sb.append(getCallState());
        Log.d(str, sb.toString());
        if (!IMXCall.CALL_STATE_CREATED.equals(getCallState()) && this.mPeerConnection != null) {
            this.mUIThreadHandler.post(new Runnable() {
                public void run() {
                    if (MXWebRtcCall.this.mPeerConnection == null) {
                        Log.d(MXWebRtcCall.LOG_TAG, "answer the connection has been closed");
                        return;
                    }
                    MXWebRtcCall.this.dispatchOnStateDidChange(IMXCall.CALL_STATE_CREATE_ANSWER);
                    MediaConstraints mediaConstraints = new MediaConstraints();
                    String str = "true";
                    mediaConstraints.mandatory.add(new KeyValuePair("OfferToReceiveAudio", str));
                    List list = mediaConstraints.mandatory;
                    if (!MXWebRtcCall.this.isVideo()) {
                        str = "false";
                    }
                    list.add(new KeyValuePair("OfferToReceiveVideo", str));
                    MXWebRtcCall.this.mPeerConnection.createAnswer(new SdpObserver() {
                        public void onCreateSuccess(SessionDescription sessionDescription) {
                            Log.d(MXWebRtcCall.LOG_TAG, "createAnswer onCreateSuccess");
                            final SessionDescription sessionDescription2 = new SessionDescription(sessionDescription.type, sessionDescription.description);
                            MXWebRtcCall.this.mUIThreadHandler.post(new Runnable() {
                                public void run() {
                                    if (MXWebRtcCall.this.mPeerConnection != null) {
                                        MXWebRtcCall.this.mPeerConnection.setLocalDescription(new SdpObserver() {
                                            public void onCreateSuccess(SessionDescription sessionDescription) {
                                                Log.d(MXWebRtcCall.LOG_TAG, "setLocalDescription onCreateSuccess");
                                            }

                                            public void onSetSuccess() {
                                                Log.d(MXWebRtcCall.LOG_TAG, "setLocalDescription onSetSuccess");
                                                MXWebRtcCall.this.sendAnswer(sessionDescription2);
                                                MXWebRtcCall.this.dispatchOnStateDidChange(IMXCall.CALL_STATE_CONNECTING);
                                            }

                                            public void onCreateFailure(String str) {
                                                String access$100 = MXWebRtcCall.LOG_TAG;
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("setLocalDescription onCreateFailure ");
                                                sb.append(str);
                                                Log.e(access$100, sb.toString());
                                                MXWebRtcCall.this.dispatchOnCallError(IMXCall.CALL_ERROR_CAMERA_INIT_FAILED);
                                                MXWebRtcCall.this.hangup(null);
                                            }

                                            public void onSetFailure(String str) {
                                                String access$100 = MXWebRtcCall.LOG_TAG;
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("setLocalDescription onSetFailure ");
                                                sb.append(str);
                                                Log.e(access$100, sb.toString());
                                                MXWebRtcCall.this.dispatchOnCallError(IMXCall.CALL_ERROR_CAMERA_INIT_FAILED);
                                                MXWebRtcCall.this.hangup(null);
                                            }
                                        }, sessionDescription2);
                                    }
                                }
                            });
                        }

                        public void onSetSuccess() {
                            Log.d(MXWebRtcCall.LOG_TAG, "createAnswer onSetSuccess");
                        }

                        public void onCreateFailure(String str) {
                            String access$100 = MXWebRtcCall.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("createAnswer onCreateFailure ");
                            sb.append(str);
                            Log.e(access$100, sb.toString());
                            MXWebRtcCall.this.dispatchOnCallError(IMXCall.CALL_ERROR_CAMERA_INIT_FAILED);
                            MXWebRtcCall.this.hangup(null);
                        }

                        public void onSetFailure(String str) {
                            String access$100 = MXWebRtcCall.LOG_TAG;
                            StringBuilder sb = new StringBuilder();
                            sb.append("createAnswer onSetFailure ");
                            sb.append(str);
                            Log.e(access$100, sb.toString());
                            MXWebRtcCall.this.dispatchOnCallError(IMXCall.CALL_ERROR_CAMERA_INIT_FAILED);
                            MXWebRtcCall.this.hangup(null);
                        }
                    }, mediaConstraints);
                }
            });
        }
    }

    public void hangup(@Nullable String str) {
        super.hangup(str);
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## hangup(): reason=");
        sb.append(str);
        Log.d(str2, sb.toString());
        if (!isCallEnded()) {
            sendHangup(str);
            terminate(-1);
        }
    }

    public String getCallState() {
        return this.mCallState;
    }

    public View getCallView() {
        return this.mCallView;
    }

    public int getVisibility() {
        RelativeLayout relativeLayout = this.mCallView;
        if (relativeLayout != null) {
            return relativeLayout.getVisibility();
        }
        return 8;
    }

    public boolean setVisibility(int i) {
        RelativeLayout relativeLayout = this.mCallView;
        if (relativeLayout == null) {
            return false;
        }
        relativeLayout.setVisibility(i);
        return true;
    }

    public void onAnsweredElsewhere() {
        super.onAnsweredElsewhere();
        String callState = getCallState();
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onAnsweredElsewhere in state ");
        sb.append(callState);
        Log.d(str, sb.toString());
        if (!isCallEnded() && !this.mIsAnswered) {
            dispatchAnsweredElsewhere();
            terminate(-1);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x002a, code lost:
        if (org.matrix.androidsdk.call.IMXCall.CALL_STATE_CONNECTED.equals(r3.mCallState) != false) goto L_0x002c;
     */
    public void dispatchOnStateDidChange(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("dispatchOnStateDidChange ");
        sb.append(str);
        Log.d(str2, sb.toString());
        this.mCallState = str;
        if (!IMXCall.CALL_STATE_CONNECTING.equals(this.mCallState)) {
        }
        if (this.mCallTimeoutTimer != null) {
            this.mCallTimeoutTimer.cancel();
            this.mCallTimeoutTimer = null;
        }
        super.dispatchOnStateDidChange(str);
    }
}
