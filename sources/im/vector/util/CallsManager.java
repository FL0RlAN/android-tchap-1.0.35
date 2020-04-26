package im.vector.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.VectorApp;
import im.vector.activity.VectorCallViewActivity;
import im.vector.activity.VectorHomeActivity;
import im.vector.notifications.NotificationUtils;
import im.vector.services.EventStreamService;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.call.CallSoundsManager;
import org.matrix.androidsdk.call.CallSoundsManager.OnMediaListener;
import org.matrix.androidsdk.call.HeadsetConnectionReceiver;
import org.matrix.androidsdk.call.HeadsetConnectionReceiver.OnHeadsetStatusUpdateListener;
import org.matrix.androidsdk.call.IMXCall;
import org.matrix.androidsdk.call.IMXCallListener;
import org.matrix.androidsdk.call.IMXCallsManagerListener;
import org.matrix.androidsdk.call.MXCallListener;
import org.matrix.androidsdk.call.MXCallsManagerListener;
import org.matrix.androidsdk.call.VideoLayoutConfiguration;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.data.MXDeviceInfo;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;

public class CallsManager {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = CallsManager.class.getSimpleName();
    private static final String RING_TONE_START_RINGING = "ring.ogg";
    private static CallsManager mSharedInstance = null;
    /* access modifiers changed from: private */
    public IMXCall mActiveCall;
    /* access modifiers changed from: private */
    public Activity mCallActivity;
    /* access modifiers changed from: private */
    public final IMXCallListener mCallListener = new MXCallListener() {
        public void onStateDidChange(final String str) {
            CallsManager.this.mUiHandler.post(new Runnable() {
                public void run() {
                    if (CallsManager.this.mActiveCall == null) {
                        Log.d(CallsManager.LOG_TAG, "## onStateDidChange() : no more active call");
                        return;
                    }
                    String access$300 = CallsManager.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("dispatchOnStateDidChange ");
                    sb.append(CallsManager.this.mActiveCall.getCallId());
                    sb.append(" : ");
                    sb.append(str);
                    Log.d(access$300, sb.toString());
                    String str = str;
                    char c = 65535;
                    int hashCode = str.hashCode();
                    String str2 = IMXCall.CALL_STATE_RINGING;
                    switch (hashCode) {
                        case -1444885671:
                            if (str.equals(IMXCall.CALL_STATE_WAIT_LOCAL_MEDIA)) {
                                c = 4;
                                break;
                            }
                            break;
                        case -215535408:
                            if (str.equals(IMXCall.CALL_STATE_WAIT_CREATE_OFFER)) {
                                c = 5;
                                break;
                            }
                            break;
                        case 183694318:
                            if (str.equals(IMXCall.CALL_STATE_CREATE_ANSWER)) {
                                c = 3;
                                break;
                            }
                            break;
                        case 946025035:
                            if (str.equals(IMXCall.CALL_STATE_CONNECTING)) {
                                c = 2;
                                break;
                            }
                            break;
                        case 1322015527:
                            if (str.equals(IMXCall.CALL_STATE_ENDED)) {
                                c = 8;
                                break;
                            }
                            break;
                        case 1700515443:
                            if (str.equals(IMXCall.CALL_STATE_CREATING_CALL_VIEW)) {
                                c = 1;
                                break;
                            }
                            break;
                        case 1781900309:
                            if (str.equals(IMXCall.CALL_STATE_CREATED)) {
                                c = 0;
                                break;
                            }
                            break;
                        case 1831632118:
                            if (str.equals(IMXCall.CALL_STATE_CONNECTED)) {
                                c = 6;
                                break;
                            }
                            break;
                        case 1960371423:
                            if (str.equals(str2)) {
                                c = 7;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                            if (CallsManager.this.mActiveCall.isIncoming()) {
                                if (EventStreamService.getInstance() != null) {
                                    EventStreamService.getInstance().displayIncomingCallNotification(CallsManager.this.mActiveCall.getSession(), CallsManager.this.mActiveCall.getRoom(), null, CallsManager.this.mActiveCall.getCallId(), null);
                                }
                                CallsManager.this.startRinging();
                                break;
                            }
                            break;
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            if (CallsManager.this.mActiveCall.isIncoming()) {
                                CallsManager.this.mCallSoundsManager.stopSounds();
                                break;
                            }
                            break;
                        case 6:
                            if (EventStreamService.getInstance() != null) {
                                EventStreamService.getInstance().displayCallInProgressNotification(CallsManager.this.mActiveCall.getSession(), CallsManager.this.mActiveCall.getRoom(), CallsManager.this.mActiveCall.getCallId());
                            }
                            CallsManager.this.mCallSoundsManager.stopSounds();
                            CallsManager.this.requestAudioFocus();
                            CallsManager.this.mUiHandler.post(new Runnable() {
                                public void run() {
                                    if (CallsManager.this.mActiveCall != null) {
                                        CallsManager.this.setCallSpeakerphoneOn(CallsManager.this.mActiveCall.isVideo() && !HeadsetConnectionReceiver.isHeadsetPlugged(CallsManager.this.mContext));
                                        CallsManager.this.mCallSoundsManager.setMicrophoneMute(false);
                                        return;
                                    }
                                    Log.e(CallsManager.LOG_TAG, "## onStateDidChange() : no more active call");
                                }
                            });
                            break;
                        case 7:
                            if (!CallsManager.this.mActiveCall.isIncoming()) {
                                CallsManager.this.startRingBackSound();
                                break;
                            }
                            break;
                        case 8:
                            if (!TextUtils.equals(str2, CallsManager.this.mPrevCallState) || CallsManager.this.mActiveCall.isIncoming()) {
                                if (!TextUtils.equals(IMXCall.CALL_STATE_INVITE_SENT, CallsManager.this.mPrevCallState)) {
                                    CallsManager.this.endCall(false);
                                    break;
                                }
                            }
                            if (!CallsManager.this.mIsStoppedByUser) {
                                CallsManager.this.showToast(CallsManager.this.mContext.getString(R.string.call_error_user_not_responding));
                            }
                            CallsManager.this.endCall(true);
                            break;
                    }
                    CallsManager.this.mPrevCallState = str;
                }
            });
        }

        public void onCallError(final String str) {
            CallsManager.this.mUiHandler.post(new Runnable() {
                public void run() {
                    if (CallsManager.this.mActiveCall == null) {
                        Log.d(CallsManager.LOG_TAG, "## onCallError() : no more active call");
                        return;
                    }
                    String access$300 = CallsManager.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## onCallError(): error=");
                    sb.append(str);
                    Log.d(access$300, sb.toString());
                    String str = str;
                    String str2 = IMXCall.CALL_ERROR_USER_NOT_RESPONDING;
                    if (str2.equals(str)) {
                        CallsManager.this.showToast(CallsManager.this.mContext.getString(R.string.call_error_user_not_responding));
                    } else {
                        if (IMXCall.CALL_ERROR_ICE_FAILED.equals(str)) {
                            CallsManager.this.showToast(CallsManager.this.mContext.getString(R.string.call_error_ice_failed));
                        } else {
                            if (IMXCall.CALL_ERROR_CAMERA_INIT_FAILED.equals(str)) {
                                CallsManager.this.showToast(CallsManager.this.mContext.getString(R.string.call_error_camera_init_failed));
                            } else {
                                CallsManager.this.showToast(str);
                            }
                        }
                    }
                    CallsManager.this.endCall(str2.equals(str));
                }
            });
        }

        public void onCallAnsweredElsewhere() {
            CallsManager.this.mUiHandler.post(new Runnable() {
                public void run() {
                    if (CallsManager.this.mActiveCall == null) {
                        Log.d(CallsManager.LOG_TAG, "## onCallError() : no more active call");
                        return;
                    }
                    String access$300 = CallsManager.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onCallAnsweredElsewhere ");
                    sb.append(CallsManager.this.mActiveCall.getCallId());
                    Log.d(access$300, sb.toString());
                    CallsManager.this.showToast(CallsManager.this.mContext.getString(R.string.call_error_answered_elsewhere));
                    CallsManager.this.releaseCall();
                }
            });
        }

        public void onCallEnd(int i) {
            CallsManager.this.mUiHandler.post(new Runnable() {
                public void run() {
                    if (CallsManager.this.mActiveCall == null) {
                        Log.d(CallsManager.LOG_TAG, "## onCallEnd() : no more active call");
                    } else {
                        CallsManager.this.endCall(false);
                    }
                }
            });
        }
    };
    /* access modifiers changed from: private */
    public CallSoundsManager mCallSoundsManager;
    private View mCallView = null;
    private final IMXCallsManagerListener mCallsManagerListener = new MXCallsManagerListener() {
        public void onIncomingCall(final IMXCall iMXCall, final MXUsersDevicesMap<MXDeviceInfo> mXUsersDevicesMap) {
            CallsManager.this.mUiHandler.post(new Runnable() {
                public void run() {
                    VectorApp instance = VectorApp.getInstance();
                    String access$300 = CallsManager.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## onIncomingCall () :");
                    sb.append(iMXCall.getCallId());
                    Log.d(access$300, sb.toString());
                    TelephonyManager telephonyManager = (TelephonyManager) instance.getSystemService("phone");
                    int callState = (telephonyManager == null || telephonyManager.getSimState() != 5) ? 0 : telephonyManager.getCallState();
                    String access$3002 = CallsManager.LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## onIncomingCall () : currentCallState(GSM) = ");
                    sb2.append(callState);
                    Log.d(access$3002, sb2.toString());
                    if (callState == 2 || callState == 1) {
                        Log.d(CallsManager.LOG_TAG, "## onIncomingCall () : rejected because GSM Call is in progress");
                        iMXCall.hangup(null);
                    } else if (CallsManager.this.mActiveCall != null) {
                        String access$3003 = CallsManager.LOG_TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("## onIncomingCall () : rejected because ");
                        sb3.append(CallsManager.this.mActiveCall);
                        sb3.append(" is in progress");
                        Log.d(access$3003, sb3.toString());
                        iMXCall.hangup(null);
                    } else {
                        CallsManager.this.mPrevCallState = null;
                        CallsManager.this.mIsStoppedByUser = false;
                        CallsManager.this.mActiveCall = iMXCall;
                        VectorHomeActivity instance2 = VectorHomeActivity.getInstance();
                        if (instance2 == null) {
                            Log.d(CallsManager.LOG_TAG, "onIncomingCall : the home activity does not exist -> launch it");
                            Intent intent = new Intent(instance, VectorHomeActivity.class);
                            intent.setFlags(872415232);
                            intent.putExtra(VectorHomeActivity.EXTRA_CALL_SESSION_ID, CallsManager.this.mActiveCall.getSession().getMyUserId());
                            intent.putExtra(VectorHomeActivity.EXTRA_CALL_ID, CallsManager.this.mActiveCall.getCallId());
                            MXUsersDevicesMap mXUsersDevicesMap = mXUsersDevicesMap;
                            if (mXUsersDevicesMap != null) {
                                intent.putExtra(VectorHomeActivity.EXTRA_CALL_UNKNOWN_DEVICES, mXUsersDevicesMap);
                            }
                            instance.startActivity(intent);
                        } else {
                            Log.d(CallsManager.LOG_TAG, "onIncomingCall : the home activity exists : but permissions have to be checked before");
                            instance2.startCall(CallsManager.this.mActiveCall.getSession().getMyUserId(), CallsManager.this.mActiveCall.getCallId(), mXUsersDevicesMap);
                        }
                        CallsManager.this.startRinging();
                        CallsManager.this.mActiveCall.addListener(CallsManager.this.mCallListener);
                    }
                }
            });
        }

        public void onOutgoingCall(final IMXCall iMXCall) {
            String access$300 = CallsManager.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onOutgoingCall () :");
            sb.append(iMXCall.getCallId());
            Log.d(access$300, sb.toString());
            CallsManager.this.mUiHandler.post(new Runnable() {
                public void run() {
                    CallsManager.this.mPrevCallState = null;
                    CallsManager.this.mIsStoppedByUser = false;
                    CallsManager.this.mActiveCall = iMXCall;
                    CallsManager.this.mActiveCall.addListener(CallsManager.this.mCallListener);
                    CallsManager.this.startRingBackSound();
                }
            });
        }

        public void onCallHangUp(IMXCall iMXCall) {
            String access$300 = CallsManager.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onCallHangUp ");
            sb.append(iMXCall.getCallId());
            Log.d(access$300, sb.toString());
            CallsManager.this.mUiHandler.post(new Runnable() {
                public void run() {
                    if (CallsManager.this.mActiveCall == null) {
                        Log.d(CallsManager.LOG_TAG, "## onCallEnd() : no more active call");
                    } else {
                        CallsManager.this.endCall(false);
                    }
                }
            });
        }
    };
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public boolean mIsStoppedByUser;
    private VideoLayoutConfiguration mLocalVideoLayoutConfig = null;
    private final OnHeadsetStatusUpdateListener mOnHeadsetStatusUpdateListener = new OnHeadsetStatusUpdateListener() {
        private void onHeadsetUpdate(boolean z) {
            if (CallsManager.this.mActiveCall != null) {
                boolean isHeadsetPlugged = HeadsetConnectionReceiver.isHeadsetPlugged(CallsManager.this.mContext);
                if (CallsManager.this.mCallSoundsManager.isSpeakerphoneOn() && isHeadsetPlugged) {
                    Log.d(CallsManager.LOG_TAG, "toggle the call speaker because the call was on loudspeaker.");
                    CallsManager.this.mCallSoundsManager.toggleSpeaker();
                } else if (!isHeadsetPlugged && CallsManager.this.mActiveCall.isVideo()) {
                    Log.d(CallsManager.LOG_TAG, "toggle the call speaker because the headset was unplugged during a video call.");
                    CallsManager.this.mCallSoundsManager.toggleSpeaker();
                } else if (z) {
                    AudioManager audioManager = (AudioManager) CallsManager.this.mContext.getSystemService("audio");
                    if (HeadsetConnectionReceiver.isBTHeadsetPlugged()) {
                        audioManager.startBluetoothSco();
                        audioManager.setBluetoothScoOn(true);
                    } else if (audioManager.isBluetoothScoOn()) {
                        audioManager.stopBluetoothSco();
                        audioManager.setBluetoothScoOn(false);
                    }
                }
                if (CallsManager.this.mCallActivity instanceof VectorCallViewActivity) {
                    ((VectorCallViewActivity) CallsManager.this.mCallActivity).refreshSpeakerButton();
                }
            }
        }

        public void onWiredHeadsetUpdate(boolean z) {
            onHeadsetUpdate(false);
        }

        public void onBluetoothHeadsetUpdate(boolean z) {
            onHeadsetUpdate(true);
        }
    };
    /* access modifiers changed from: private */
    public String mPrevCallState;
    /* access modifiers changed from: private */
    public final Handler mUiHandler = new Handler(Looper.getMainLooper());

    public CallsManager(Context context) {
        this.mContext = context.getApplicationContext();
        CallSoundsManager callSoundsManager = this.mCallSoundsManager;
        this.mCallSoundsManager = CallSoundsManager.getSharedInstance(this.mContext);
        HeadsetConnectionReceiver.getSharedInstance(this.mContext).addListener(this.mOnHeadsetStatusUpdateListener);
    }

    public static CallsManager getSharedInstance() {
        if (mSharedInstance == null) {
            mSharedInstance = new CallsManager(VectorApp.getInstance());
        }
        return mSharedInstance;
    }

    public IMXCall getActiveCall() {
        IMXCall iMXCall = this.mActiveCall;
        if (iMXCall == null || !TextUtils.equals(iMXCall.getCallState(), IMXCall.CALL_STATE_ENDED)) {
            return this.mActiveCall;
        }
        return null;
    }

    public void setCallView(View view) {
        this.mCallView = view;
    }

    public View getCallView() {
        return this.mCallView;
    }

    public void setVideoLayoutConfiguration(VideoLayoutConfiguration videoLayoutConfiguration) {
        this.mLocalVideoLayoutConfig = videoLayoutConfiguration;
    }

    public VideoLayoutConfiguration getVideoLayoutConfiguration() {
        return this.mLocalVideoLayoutConfig;
    }

    public void setCallActivity(Activity activity) {
        this.mCallActivity = activity;
    }

    /* access modifiers changed from: private */
    public void showToast(final String str) {
        this.mUiHandler.post(new Runnable() {
            public void run() {
                Toast.makeText(CallsManager.this.mContext, str, 1).show();
            }
        });
    }

    public void addSession(MXSession mXSession) {
        mXSession.getDataHandler().getCallsManager().addListener(this.mCallsManagerListener);
    }

    public void removeSession(MXSession mXSession) {
        mXSession.getDataHandler().getCallsManager().removeListener(this.mCallsManagerListener);
    }

    public void checkDeadCalls() {
        boolean z = false;
        for (MXSession dataHandler : Matrix.getMXSessions(this.mContext)) {
            z |= dataHandler.getDataHandler().getCallsManager().hasActiveCalls();
        }
        if (this.mActiveCall != null && !z) {
            Log.e(LOG_TAG, "## checkDeadCalls() : fix an infinite ringing");
            if (EventStreamService.getInstance() != null) {
                EventStreamService.getInstance().hideCallNotifications();
            }
            releaseCall();
        }
    }

    public void rejectCall() {
        IMXCall iMXCall = this.mActiveCall;
        if (iMXCall != null) {
            iMXCall.hangup(null);
            releaseCall();
        }
    }

    public void toggleSpeaker() {
        if (this.mActiveCall != null) {
            this.mCallSoundsManager.toggleSpeaker();
        } else {
            Log.w(LOG_TAG, "## toggleSpeaker(): no active call");
        }
    }

    /* access modifiers changed from: private */
    public void setCallSpeakerphoneOn(boolean z) {
        if (this.mActiveCall != null) {
            this.mCallSoundsManager.setCallSpeakerphoneOn(z);
        } else {
            Log.w(LOG_TAG, "## toggleSpeaker(): no active call");
        }
    }

    public void onHangUp(String str) {
        IMXCall iMXCall = this.mActiveCall;
        if (iMXCall != null) {
            this.mIsStoppedByUser = true;
            iMXCall.hangup(str);
            endCall(false);
        }
    }

    /* access modifiers changed from: private */
    public void startRinging() {
        if (NotificationUtils.INSTANCE.isDoNotDisturbModeOn(this.mContext)) {
            Log.w(LOG_TAG, "Do not ring because DO NOT DISTURB MODE is on");
            this.mCallSoundsManager.startRingingSilently();
            return;
        }
        requestAudioFocus();
        if (RingtoneUtilsKt.useRiotDefaultRingtone(this.mContext)) {
            this.mCallSoundsManager.startRinging(R.raw.ring, RING_TONE_START_RINGING);
        } else {
            this.mCallSoundsManager.startRinging(RingtoneUtilsKt.getCallRingtone(this.mContext));
        }
    }

    public boolean isRinging() {
        return this.mCallSoundsManager.isRinging();
    }

    public boolean isSpeakerphoneOn() {
        return this.mCallSoundsManager.isSpeakerphoneOn();
    }

    /* access modifiers changed from: private */
    public void requestAudioFocus() {
        this.mCallSoundsManager.requestAudioFocus();
    }

    /* access modifiers changed from: private */
    public void startRingBackSound() {
        this.mCallSoundsManager.startSound(R.raw.ringback, true, new OnMediaListener() {
            public void onMediaCompleted() {
            }

            public void onMediaPlay() {
            }

            public void onMediaReadyToPlay() {
                if (CallsManager.this.mActiveCall != null) {
                    CallsManager.this.requestAudioFocus();
                    CallsManager.this.mCallSoundsManager.setSpeakerphoneOn(true, CallsManager.this.mActiveCall.isVideo() && !HeadsetConnectionReceiver.isHeadsetPlugged(CallsManager.this.mContext));
                    return;
                }
                Log.e(CallsManager.LOG_TAG, "## startSound() : null mActiveCall");
            }
        });
    }

    /* access modifiers changed from: private */
    public void endCall(boolean z) {
        final IMXCall iMXCall = this.mActiveCall;
        if (iMXCall != null) {
            this.mActiveCall = null;
            if (this.mCallSoundsManager.isRinging()) {
                releaseCall(iMXCall);
            } else {
                this.mCallSoundsManager.startSound(z ? R.raw.busy : R.raw.callend, false, new OnMediaListener() {
                    public void onMediaPlay() {
                    }

                    public void onMediaReadyToPlay() {
                        if (CallsManager.this.mCallActivity != null) {
                            CallsManager.this.mCallActivity.finish();
                            CallsManager.this.mCallActivity = null;
                        }
                    }

                    public void onMediaCompleted() {
                        CallsManager.this.releaseCall(iMXCall);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public void releaseCall() {
        IMXCall iMXCall = this.mActiveCall;
        if (iMXCall != null) {
            releaseCall(iMXCall);
            this.mActiveCall = null;
        }
    }

    /* access modifiers changed from: private */
    public void releaseCall(IMXCall iMXCall) {
        if (iMXCall != null) {
            iMXCall.removeListener(this.mCallListener);
            this.mCallSoundsManager.stopSounds();
            this.mCallSoundsManager.releaseAudioFocus();
            Activity activity = this.mCallActivity;
            if (activity != null) {
                activity.finish();
                this.mCallActivity = null;
            }
            this.mCallView = null;
            this.mLocalVideoLayoutConfig = null;
            if (EventStreamService.getInstance() != null) {
                EventStreamService.getInstance().hideCallNotifications();
            }
        }
    }
}
