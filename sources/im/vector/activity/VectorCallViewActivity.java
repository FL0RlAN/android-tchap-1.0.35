package im.vector.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.VectorApp;
import im.vector.settings.VectorLocale;
import im.vector.ui.themes.ActivityOtherThemes;
import im.vector.ui.themes.ActivityOtherThemes.Call;
import im.vector.util.CallsManager;
import im.vector.util.PermissionsToolsKt;
import im.vector.util.VectorUtils;
import im.vector.view.VectorPendingCallView;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.call.CallSoundsManager;
import org.matrix.androidsdk.call.CallSoundsManager.OnAudioConfigurationUpdateListener;
import org.matrix.androidsdk.call.IMXCall;
import org.matrix.androidsdk.call.IMXCallListener;
import org.matrix.androidsdk.call.MXCallListener;
import org.matrix.androidsdk.call.VideoLayoutConfiguration;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap;

public class VectorCallViewActivity extends VectorAppCompatActivity implements SensorEventListener {
    public static final String EXTRA_CALL_ID = "CallViewActivity.EXTRA_CALL_ID";
    private static final String EXTRA_LOCAL_FRAME_LAYOUT = "EXTRA_LOCAL_FRAME_LAYOUT";
    public static final String EXTRA_MATRIX_ID = "CallViewActivity.EXTRA_MATRIX_ID";
    public static final String EXTRA_UNKNOWN_DEVICES = "CallViewActivity.EXTRA_UNKNOWN_DEVICES";
    private static final int FADE_IN_DURATION = 250;
    private static final int FADE_OUT_DURATION = 2000;
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorCallViewActivity.class.getSimpleName();
    private static final int PERCENT_LOCAL_USER_VIDEO_SIZE = 25;
    private static final float PROXIMITY_THRESHOLD = 3.0f;
    private static final int VIDEO_FADING_TIMER = 5000;
    private static final float VIDEO_TO_BUTTONS_VERTICAL_SPACE = 0.03076923f;
    /* access modifiers changed from: private */
    public View mAcceptIncomingCallButton;
    private final OnAudioConfigurationUpdateListener mAudioConfigListener = new OnAudioConfigurationUpdateListener() {
        public void onAudioConfigurationUpdate() {
            VectorCallViewActivity.this.refreshSpeakerButton();
            VectorCallViewActivity.this.refreshMuteMicButton();
        }
    };
    private ImageView mAvatarView;
    /* access modifiers changed from: private */
    public View mButtonsContainerView;
    /* access modifiers changed from: private */
    public IMXCall mCall;
    /* access modifiers changed from: private */
    public View mCallView;
    /* access modifiers changed from: private */
    public CallsManager mCallsManager;
    private int mField = 32;
    private ImageView mHangUpImageView;
    private VectorPendingCallView mHeaderPendingCallView;
    private View mIncomingCallTabbar;
    /* access modifiers changed from: private */
    public boolean mIsCustomLocalVideoLayoutConfig;
    private boolean mIsScreenOff = false;
    private final IMXCallListener mListener = new MXCallListener() {
        public void onStateDidChange(final String str) {
            VectorCallViewActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    String access$000 = VectorCallViewActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## onStateDidChange(): new state=");
                    sb.append(str);
                    Log.d(access$000, sb.toString());
                    VectorCallViewActivity.this.manageSubViews();
                    if (VectorCallViewActivity.this.mCall != null && VectorCallViewActivity.this.mCall.isVideo() && VectorCallViewActivity.this.mCall.getCallState().equals(IMXCall.CALL_STATE_CONNECTED)) {
                        VectorCallViewActivity.this.mCall.updateLocalVideoRendererPosition(VectorCallViewActivity.this.mLocalVideoLayoutConfig);
                    }
                }
            });
        }

        public void onCallViewCreated(View view) {
            Log.d(VectorCallViewActivity.LOG_TAG, "## onViewLoading():");
            VectorCallViewActivity.this.mCallView = view;
            VectorCallViewActivity.this.insertCallView();
        }

        public void onReady() {
            VectorCallViewActivity.this.computeVideoUiLayout();
            if (!VectorCallViewActivity.this.mCall.isIncoming()) {
                Log.d(VectorCallViewActivity.LOG_TAG, "## onReady(): placeCall()");
                VectorCallViewActivity.this.mCall.placeCall(VectorCallViewActivity.this.mLocalVideoLayoutConfig);
                return;
            }
            Log.d(VectorCallViewActivity.LOG_TAG, "## onReady(): launchIncomingCall()");
            VectorCallViewActivity.this.mCall.launchIncomingCall(VectorCallViewActivity.this.mLocalVideoLayoutConfig);
        }

        public void onPreviewSizeChanged(int i, int i2) {
            String access$000 = VectorCallViewActivity.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onPreviewSizeChanged : ");
            sb.append(i);
            sb.append(" * ");
            sb.append(i2);
            Log.d(access$000, sb.toString());
            VectorCallViewActivity.this.mSourceVideoWidth = i;
            VectorCallViewActivity.this.mSourceVideoHeight = i2;
            if (VectorCallViewActivity.this.mCall != null && VectorCallViewActivity.this.mCall.isVideo() && VectorCallViewActivity.this.mCall.getCallState().equals(IMXCall.CALL_STATE_CONNECTED)) {
                VectorCallViewActivity.this.computeVideoUiLayout();
                VectorCallViewActivity.this.mCall.updateLocalVideoRendererPosition(VectorCallViewActivity.this.mLocalVideoLayoutConfig);
            }
        }
    };
    /* access modifiers changed from: private */
    public VideoLayoutConfiguration mLocalVideoLayoutConfig;
    private final OnTouchListener mMainViewTouchListener = new OnTouchListener() {
        private Rect mPreviewRect = null;
        private int mStartX = 0;
        private int mStartY = 0;

        private Rect computePreviewRect() {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            VectorCallViewActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int i = displayMetrics.heightPixels;
            int i2 = displayMetrics.widthPixels;
            return new Rect((VectorCallViewActivity.this.mLocalVideoLayoutConfig.mX * i2) / 100, (VectorCallViewActivity.this.mLocalVideoLayoutConfig.mY * i) / 100, ((VectorCallViewActivity.this.mLocalVideoLayoutConfig.mX + VectorCallViewActivity.this.mLocalVideoLayoutConfig.mWidth) * i2) / 100, ((VectorCallViewActivity.this.mLocalVideoLayoutConfig.mY + VectorCallViewActivity.this.mLocalVideoLayoutConfig.mHeight) * i) / 100);
        }

        private void updatePreviewFrame(int i, int i2) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            VectorCallViewActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int i3 = displayMetrics.heightPixels;
            int i4 = displayMetrics.widthPixels;
            int width = this.mPreviewRect.width();
            int height = this.mPreviewRect.height();
            Rect rect = this.mPreviewRect;
            rect.left = Math.max(0, rect.left + i);
            Rect rect2 = this.mPreviewRect;
            rect2.right = rect2.left + width;
            Rect rect3 = this.mPreviewRect;
            rect3.top = Math.max(0, rect3.top + i2);
            Rect rect4 = this.mPreviewRect;
            rect4.bottom = rect4.top + height;
            if (this.mPreviewRect.right > i4) {
                Rect rect5 = this.mPreviewRect;
                rect5.right = i4;
                rect5.left = rect5.right - width;
            }
            if (this.mPreviewRect.bottom > i3) {
                Rect rect6 = this.mPreviewRect;
                rect6.bottom = i3;
                rect6.top = i3 - height;
            }
            VectorCallViewActivity.this.mLocalVideoLayoutConfig.mX = (this.mPreviewRect.left * 100) / i4;
            VectorCallViewActivity.this.mLocalVideoLayoutConfig.mY = (this.mPreviewRect.top * 100) / i3;
            VectorCallViewActivity.this.mLocalVideoLayoutConfig.mDisplayWidth = i4;
            VectorCallViewActivity.this.mLocalVideoLayoutConfig.mDisplayHeight = i3;
            VectorCallViewActivity.this.mIsCustomLocalVideoLayoutConfig = true;
            VectorCallViewActivity.this.mCall.updateLocalVideoRendererPosition(VectorCallViewActivity.this.mLocalVideoLayoutConfig);
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (VectorCallViewActivity.this.mCall != null && VectorCallViewActivity.this.mCall.isVideo()) {
                if (TextUtils.equals(IMXCall.CALL_STATE_CONNECTED, VectorCallViewActivity.this.mCall.getCallState())) {
                    int action = motionEvent.getAction();
                    int x = (int) motionEvent.getX();
                    int y = (int) motionEvent.getY();
                    if (action == 0) {
                        Rect computePreviewRect = computePreviewRect();
                        if (computePreviewRect.contains(x, y)) {
                            this.mPreviewRect = computePreviewRect;
                            this.mStartX = x;
                            this.mStartY = y;
                            return true;
                        }
                    } else if (this.mPreviewRect == null || action != 2) {
                        this.mPreviewRect = null;
                    } else {
                        updatePreviewFrame(x - this.mStartX, y - this.mStartY);
                        this.mStartX = x;
                        this.mStartY = y;
                        return true;
                    }
                }
            }
            return false;
        }
    };
    private String mMatrixId = null;
    private ImageView mMuteLocalCameraView;
    private ImageView mMuteMicImageView;
    private Sensor mProximitySensor;
    private SensorManager mSensorMgr;
    /* access modifiers changed from: private */
    public MXSession mSession = null;
    /* access modifiers changed from: private */
    public int mSourceVideoHeight = 0;
    /* access modifiers changed from: private */
    public int mSourceVideoWidth = 0;
    private ImageView mSpeakerSelectionView;
    private ImageView mSwitchRearFrontCameraImageView;
    private Timer mVideoFadingEdgesTimer;
    private TimerTask mVideoFadingEdgesTimerTask;
    private WakeLock mWakeLock;

    public int getLayoutRes() {
        return R.layout.activity_callview;
    }

    /* access modifiers changed from: private */
    public void insertCallView() {
        if (this.mCallView != null) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.call_layout);
            LayoutParams layoutParams = new LayoutParams(-1, -1);
            layoutParams.addRule(13, -1);
            relativeLayout.removeView(this.mCallView);
            relativeLayout.setVisibility(0);
            if (this.mCall.isVideo()) {
                if (this.mCallView.getParent() != null) {
                    ((ViewGroup) this.mCallView.getParent()).removeView(this.mCallView);
                }
                relativeLayout.addView(this.mCallView, 1, layoutParams);
            }
            this.mCall.setVisibility(8);
        }
    }

    public ActivityOtherThemes getOtherThemes() {
        return Call.INSTANCE;
    }

    public void initUiAndData() {
        final Intent intent = getIntent();
        if (intent == null) {
            Log.e(LOG_TAG, "Need an intent to view.");
            finish();
            return;
        }
        String str = EXTRA_MATRIX_ID;
        if (!intent.hasExtra(str)) {
            Log.e(LOG_TAG, "No matrix ID extra.");
            finish();
            return;
        }
        String stringExtra = intent.getStringExtra(EXTRA_CALL_ID);
        this.mMatrixId = intent.getStringExtra(str);
        this.mSession = Matrix.getInstance(getApplicationContext()).getSession(this.mMatrixId);
        MXSession mXSession = this.mSession;
        if (mXSession == null || !mXSession.isAlive()) {
            Log.e(LOG_TAG, "invalid session");
            finish();
            return;
        }
        this.mCall = CallsManager.getSharedInstance().getActiveCall();
        IMXCall iMXCall = this.mCall;
        if (iMXCall == null || !TextUtils.equals(iMXCall.getCallId(), stringExtra)) {
            Log.e(LOG_TAG, "invalid call");
            finish();
            return;
        }
        this.mCallsManager = CallsManager.getSharedInstance();
        this.mHangUpImageView = (ImageView) findViewById(R.id.hang_up_button);
        this.mSpeakerSelectionView = (ImageView) findViewById(R.id.call_speaker_view);
        this.mAvatarView = (ImageView) findViewById(R.id.call_other_member);
        this.mMuteMicImageView = (ImageView) findViewById(R.id.mute_audio);
        this.mHeaderPendingCallView = (VectorPendingCallView) findViewById(R.id.header_pending_callview);
        this.mSwitchRearFrontCameraImageView = (ImageView) findViewById(R.id.call_switch_camera_view);
        this.mMuteLocalCameraView = (ImageView) findViewById(R.id.mute_local_camera);
        this.mButtonsContainerView = findViewById(R.id.call_menu_buttons_layout_container);
        this.mIncomingCallTabbar = findViewById(R.id.incoming_call_menu_buttons_layout_container);
        this.mAcceptIncomingCallButton = findViewById(R.id.accept_incoming_call);
        View findViewById = findViewById(R.id.reject_incoming_call);
        View findViewById2 = findViewById(R.id.call_layout);
        findViewById2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VectorCallViewActivity.this.fadeInVideoEdge();
                VectorCallViewActivity.this.startVideoFadingEdgesScreenTimer();
            }
        });
        findViewById2.setOnTouchListener(this.mMainViewTouchListener);
        ((ImageView) findViewById(R.id.room_chat_link)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VectorCallViewActivity.this.finish();
                VectorCallViewActivity.this.startRoomActivity();
            }
        });
        this.mSwitchRearFrontCameraImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VectorCallViewActivity.this.toggleRearFrontCamera();
                VectorCallViewActivity.this.refreshSwitchRearFrontCameraButton();
                VectorCallViewActivity.this.startVideoFadingEdgesScreenTimer();
            }
        });
        this.mMuteLocalCameraView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VectorCallViewActivity.this.toggleVideoMute();
                VectorCallViewActivity.this.refreshMuteVideoButton();
                VectorCallViewActivity.this.startVideoFadingEdgesScreenTimer();
            }
        });
        this.mMuteMicImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VectorCallViewActivity.this.toggleMicMute();
                VectorCallViewActivity.this.startVideoFadingEdgesScreenTimer();
            }
        });
        this.mHangUpImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VectorCallViewActivity.this.mCallsManager.onHangUp(null);
            }
        });
        this.mSpeakerSelectionView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VectorCallViewActivity.this.toggleSpeaker();
                VectorCallViewActivity.this.startVideoFadingEdgesScreenTimer();
            }
        });
        if (!isFirstCreation()) {
            this.mLocalVideoLayoutConfig = (VideoLayoutConfiguration) getSavedInstanceState().getSerializable(EXTRA_LOCAL_FRAME_LAYOUT);
            if (this.mLocalVideoLayoutConfig != null) {
                if (this.mLocalVideoLayoutConfig.mIsPortrait != (2 != getResources().getConfiguration().orientation)) {
                    this.mLocalVideoLayoutConfig = null;
                }
            }
            this.mIsCustomLocalVideoLayoutConfig = this.mLocalVideoLayoutConfig != null;
        }
        manageSubViews();
        if (this.mCallsManager.getCallView() == null || this.mCallsManager.getCallView().getParent() != null) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (VectorCallViewActivity.this.mCall.getCallView() != null) {
                        VectorCallViewActivity vectorCallViewActivity = VectorCallViewActivity.this;
                        vectorCallViewActivity.mCallView = vectorCallViewActivity.mCall.getCallView();
                        VectorCallViewActivity.this.insertCallView();
                        VectorCallViewActivity.this.computeVideoUiLayout();
                        VectorCallViewActivity.this.mCall.updateLocalVideoRendererPosition(VectorCallViewActivity.this.mLocalVideoLayoutConfig);
                        if (TextUtils.equals(VectorCallViewActivity.this.mCall.getCallState(), IMXCall.CALL_STATE_READY) && VectorCallViewActivity.this.mCall.isIncoming()) {
                            VectorCallViewActivity.this.mCall.launchIncomingCall(VectorCallViewActivity.this.mLocalVideoLayoutConfig);
                        }
                    } else if (!VectorCallViewActivity.this.mCall.isIncoming()) {
                        if (TextUtils.equals(IMXCall.CALL_STATE_CREATED, VectorCallViewActivity.this.mCall.getCallState())) {
                            VectorCallViewActivity.this.mCall.createCallView();
                        }
                    }
                }
            });
        } else {
            this.mCallView = this.mCallsManager.getCallView();
            insertCallView();
            if (this.mCallsManager.getVideoLayoutConfiguration() != null) {
                if (this.mCallsManager.getVideoLayoutConfiguration().mIsPortrait == (2 != getResources().getConfiguration().orientation)) {
                    this.mLocalVideoLayoutConfig = this.mCallsManager.getVideoLayoutConfiguration();
                    this.mIsCustomLocalVideoLayoutConfig = true;
                }
            }
        }
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int min = Math.min(point.x, point.y) / 2;
        LayoutParams layoutParams = (LayoutParams) this.mAvatarView.getLayoutParams();
        layoutParams.height = min;
        layoutParams.width = min;
        this.mAvatarView.setLayoutParams(layoutParams);
        VectorUtils.loadCallAvatar(this, this.mSession, this.mAvatarView, this.mCall.getRoom());
        int i = 8;
        this.mIncomingCallTabbar.setVisibility((!CallsManager.getSharedInstance().isRinging() || !this.mCall.isIncoming()) ? 8 : 0);
        int i2 = this.mCall.isVideo() ? 5 : 4;
        int i3 = this.mCall.isVideo() ? PermissionsToolsKt.PERMISSION_REQUEST_CODE_VIDEO_CALL : PermissionsToolsKt.PERMISSION_REQUEST_CODE_AUDIO_CALL;
        View view = this.mAcceptIncomingCallButton;
        if (PermissionsToolsKt.checkPermissions(i2, (Activity) this, i3)) {
            i = 0;
        }
        view.setVisibility(i);
        this.mAcceptIncomingCallButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.d(VectorCallViewActivity.LOG_TAG, "Accept the incoming call");
                VectorCallViewActivity.this.mAcceptIncomingCallButton.setVisibility(8);
                VectorCallViewActivity.this.mCall.createCallView();
            }
        });
        findViewById.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.d(VectorCallViewActivity.LOG_TAG, "Reject the incoming call");
                VectorCallViewActivity.this.mCallsManager.rejectCall();
            }
        });
        runOnUiThread(new Runnable() {
            public void run() {
                CommonActivityUtils.displayUnknownDevicesDialog(VectorCallViewActivity.this.mSession, VectorCallViewActivity.this, (MXUsersDevicesMap) intent.getSerializableExtra(VectorCallViewActivity.EXTRA_UNKNOWN_DEVICES), true, null);
            }
        });
        setupHeaderPendingCallView();
        Log.d(LOG_TAG, "## onCreate(): OUT");
    }

    private void setupHeaderPendingCallView() {
        VectorPendingCallView vectorPendingCallView = this.mHeaderPendingCallView;
        if (vectorPendingCallView != null) {
            vectorPendingCallView.findViewById(R.id.main_view).setBackgroundResource(R.drawable.call_header_transparent_bg);
            this.mHeaderPendingCallView.findViewById(R.id.call_icon_container).setVisibility(8);
            View findViewById = this.mHeaderPendingCallView.findViewById(R.id.back_icon);
            findViewById.setVisibility(0);
            findViewById.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    VectorCallViewActivity.this.onBackPressed();
                }
            });
            LinearLayout linearLayout = (LinearLayout) this.mHeaderPendingCallView.findViewById(R.id.call_info_container);
            linearLayout.setHorizontalGravity(1);
            linearLayout.setPadding(0, 0, 0, 0);
            this.mHeaderPendingCallView.enableCallStatusDisplay(false);
        }
    }

    private void initBackLightManagement() {
        IMXCall iMXCall = this.mCall;
        if (iMXCall == null) {
            return;
        }
        if (iMXCall.isVideo()) {
            Log.d(LOG_TAG, "## initBackLightManagement(): backlight is ON");
            getWindow().addFlags(128);
        } else if (this.mSensorMgr == null) {
            IMXCall iMXCall2 = this.mCall;
            if (iMXCall2 != null && TextUtils.equals(iMXCall2.getCallState(), IMXCall.CALL_STATE_CONNECTED)) {
                this.mSensorMgr = (SensorManager) getSystemService("sensor");
                Sensor defaultSensor = this.mSensorMgr.getDefaultSensor(8);
                this.mProximitySensor = defaultSensor;
                if (defaultSensor == null) {
                    Log.w(LOG_TAG, "## initBackLightManagement(): Warning - proximity sensor not supported");
                } else {
                    this.mSensorMgr.registerListener(this, this.mProximitySensor, 3);
                }
            }
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        CommonActivityUtils.onLowMemory(this);
    }

    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
        CommonActivityUtils.onTrimMemory(this, i);
    }

    public void finish() {
        super.finish();
        stopProximitySensor();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        if (!this.mIsScreenOff) {
            stopProximitySensor();
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        int i2 = 0;
        if (i == 572) {
            View view = this.mAcceptIncomingCallButton;
            if (!PermissionsToolsKt.onPermissionResultVideoIpCall(this, iArr)) {
                i2 = 8;
            }
            view.setVisibility(i2);
        } else if (i == 571) {
            View view2 = this.mAcceptIncomingCallButton;
            if (!PermissionsToolsKt.onPermissionResultAudioIpCall(this, iArr)) {
                i2 = 8;
            }
            view2.setVisibility(i2);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (!this.mIsScreenOff) {
            IMXCall iMXCall = this.mCall;
            if (iMXCall != null) {
                iMXCall.onPause();
            }
        }
        IMXCall iMXCall2 = this.mCall;
        if (iMXCall2 != null) {
            iMXCall2.removeListener(this.mListener);
        }
        saveCallView();
        CallsManager.getSharedInstance().setCallActivity(null);
        CallSoundsManager.getSharedInstance(this).removeAudioConfigurationListener(this.mAudioConfigListener);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mCallsManager.getActiveCall() == null) {
            Log.d(LOG_TAG, "## onResume() : the call does not exist anymore");
            finish();
            return;
        }
        this.mHeaderPendingCallView.checkPendingCall();
        computeVideoUiLayout();
        IMXCall iMXCall = this.mCall;
        if (iMXCall != null && iMXCall.isVideo() && this.mCall.getCallState().equals(IMXCall.CALL_STATE_CONNECTED)) {
            this.mCall.updateLocalVideoRendererPosition(this.mLocalVideoLayoutConfig);
        }
        IMXCall iMXCall2 = this.mCall;
        if (iMXCall2 != null) {
            iMXCall2.addListener(this.mListener);
            if (!this.mIsScreenOff) {
                this.mCall.onResume();
            }
            this.mIsScreenOff = false;
            String callState = this.mCall.getCallState();
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onResume(): call state=");
            sb.append(callState);
            Log.d(str, sb.toString());
            this.mCallView = this.mCallsManager.getCallView();
            insertCallView();
            manageSubViews();
            initBackLightManagement();
            CallsManager.getSharedInstance().setCallActivity(this);
            CallSoundsManager.getSharedInstance(this).addAudioConfigurationListener(this.mAudioConfigListener);
        } else {
            finish();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        computeVideoUiLayout();
        IMXCall iMXCall = this.mCall;
        if (iMXCall != null && iMXCall.isVideo() && this.mCall.getCallState().equals(IMXCall.CALL_STATE_CONNECTED)) {
            this.mCall.updateLocalVideoRendererPosition(this.mLocalVideoLayoutConfig);
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (this.mIsCustomLocalVideoLayoutConfig) {
            bundle.putSerializable(EXTRA_LOCAL_FRAME_LAYOUT, this.mLocalVideoLayoutConfig);
        }
    }

    /* access modifiers changed from: private */
    public void toggleMicMute() {
        CallSoundsManager sharedInstance = CallSoundsManager.getSharedInstance(this);
        sharedInstance.setMicrophoneMute(!sharedInstance.isMicrophoneMute());
    }

    /* access modifiers changed from: private */
    public void toggleVideoMute() {
        IMXCall iMXCall = this.mCall;
        if (iMXCall == null) {
            Log.w(LOG_TAG, "## toggleVideoMute(): Failed");
        } else if (iMXCall.isVideo()) {
            boolean isVideoRecordingMuted = this.mCall.isVideoRecordingMuted();
            this.mCall.muteVideoRecording(!isVideoRecordingMuted);
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## toggleVideoMute(): camera record turned to ");
            sb.append(!isVideoRecordingMuted);
            Log.w(str, sb.toString());
        }
    }

    /* access modifiers changed from: private */
    public void toggleSpeaker() {
        this.mCallsManager.toggleSpeaker();
    }

    /* access modifiers changed from: private */
    public void toggleRearFrontCamera() {
        boolean z;
        IMXCall iMXCall = this.mCall;
        if (iMXCall == null || !iMXCall.isVideo()) {
            Log.w(LOG_TAG, "## toggleRearFrontCamera(): Skipped");
            z = false;
        } else {
            z = this.mCall.switchRearFrontCamera();
        }
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## toggleRearFrontCamera(): done? ");
        sb.append(z);
        Log.w(str, sb.toString());
    }

    /* access modifiers changed from: private */
    public void startRoomActivity() {
        IMXCall iMXCall = this.mCall;
        if (iMXCall != null) {
            String roomId = iMXCall.getRoom().getRoomId();
            String str = "MXCActionBarActivity.EXTRA_MATRIX_ID";
            String str2 = "EXTRA_ROOM_ID";
            if (VectorApp.getCurrentActivity() != null) {
                HashMap hashMap = new HashMap();
                hashMap.put(str, this.mMatrixId);
                hashMap.put(str2, roomId);
                CommonActivityUtils.goToRoomPage(VectorApp.getCurrentActivity(), this.mSession, hashMap);
                return;
            }
            Intent intent = new Intent(getApplicationContext(), VectorRoomActivity.class);
            intent.putExtra(str2, roomId);
            intent.putExtra(str, this.mMatrixId);
            startActivity(intent);
        }
    }

    /* access modifiers changed from: private */
    public void stopVideoFadingEdgesScreenTimer() {
        Timer timer = this.mVideoFadingEdgesTimer;
        if (timer != null) {
            timer.cancel();
            this.mVideoFadingEdgesTimer = null;
            this.mVideoFadingEdgesTimerTask = null;
        }
    }

    /* access modifiers changed from: private */
    public void startVideoFadingEdgesScreenTimer() {
        IMXCall iMXCall = this.mCall;
        if (iMXCall != null && iMXCall.isVideo()) {
            stopVideoFadingEdgesScreenTimer();
            try {
                this.mVideoFadingEdgesTimer = new Timer();
                this.mVideoFadingEdgesTimerTask = new TimerTask() {
                    public void run() {
                        VectorCallViewActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                VectorCallViewActivity.this.stopVideoFadingEdgesScreenTimer();
                                VectorCallViewActivity.this.fadeOutVideoEdge();
                            }
                        });
                    }
                };
                this.mVideoFadingEdgesTimer.schedule(this.mVideoFadingEdgesTimerTask, 5000);
            } catch (Throwable th) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## startVideoFadingEdgesScreenTimer() ");
                sb.append(th.getMessage());
                Log.e(str, sb.toString());
                stopVideoFadingEdgesScreenTimer();
                fadeOutVideoEdge();
            }
        }
    }

    private void fadeVideoEdge(final float f, int i) {
        VectorPendingCallView vectorPendingCallView = this.mHeaderPendingCallView;
        if (!(vectorPendingCallView == null || f == vectorPendingCallView.getAlpha())) {
            this.mHeaderPendingCallView.animate().alpha(f).setDuration((long) i).setInterpolator(new AccelerateInterpolator());
        }
        View view = this.mButtonsContainerView;
        if (view != null && f != view.getAlpha()) {
            this.mButtonsContainerView.animate().alpha(f).setDuration((long) i).setInterpolator(new AccelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (0.0f == f) {
                        VectorCallViewActivity.this.mButtonsContainerView.setVisibility(8);
                    } else {
                        VectorCallViewActivity.this.mButtonsContainerView.setVisibility(0);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void fadeOutVideoEdge() {
        fadeVideoEdge(0.0f, FADE_OUT_DURATION);
    }

    /* access modifiers changed from: private */
    public void fadeInVideoEdge() {
        fadeVideoEdge(1.0f, 250);
    }

    /* access modifiers changed from: private */
    public void computeVideoUiLayout() {
        if (this.mLocalVideoLayoutConfig == null) {
            this.mLocalVideoLayoutConfig = new VideoLayoutConfiguration();
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.heightPixels;
        int i2 = displayMetrics.widthPixels;
        TypedValue typedValue = new TypedValue();
        if (getTheme().resolveAttribute(16843499, typedValue, true)) {
            i -= TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        }
        ViewGroup.LayoutParams layoutParams = findViewById(R.id.hang_up_button).getLayoutParams();
        if (this.mLocalVideoLayoutConfig.mWidth == 0) {
            this.mLocalVideoLayoutConfig.mWidth = 25;
        }
        if (this.mLocalVideoLayoutConfig.mHeight == 0) {
            this.mLocalVideoLayoutConfig.mHeight = 25;
        }
        if (this.mSourceVideoWidth == 0 || this.mSourceVideoHeight == 0) {
            VideoLayoutConfiguration videoLayoutConfiguration = this.mLocalVideoLayoutConfig;
            videoLayoutConfiguration.mWidth = 25;
            videoLayoutConfiguration.mHeight = 25;
        } else {
            int i3 = (this.mSourceVideoWidth * 100) / this.mSourceVideoHeight;
            if (i3 != (((this.mLocalVideoLayoutConfig.mWidth * i2) / 100) * 100) / ((this.mLocalVideoLayoutConfig.mHeight * i) / 100)) {
                int i4 = (i2 * 25) / 100;
                int i5 = ((i * 25) / 100) * i3;
                if (i5 / 100 > i4) {
                    VideoLayoutConfiguration videoLayoutConfiguration2 = this.mLocalVideoLayoutConfig;
                    videoLayoutConfiguration2.mHeight = (((i4 * 100) * 100) / i3) / i;
                    videoLayoutConfiguration2.mWidth = 25;
                } else {
                    VideoLayoutConfiguration videoLayoutConfiguration3 = this.mLocalVideoLayoutConfig;
                    videoLayoutConfiguration3.mWidth = i5 / i2;
                    videoLayoutConfiguration3.mHeight = 25;
                }
            }
        }
        boolean z = false;
        if (!this.mIsCustomLocalVideoLayoutConfig) {
            int i6 = this.mButtonsContainerView.getVisibility() == 0 ? (layoutParams.height * 100) / i : 0;
            float f = (float) i;
            int i7 = (int) (((VIDEO_TO_BUTTONS_VERTICAL_SPACE * f) * 100.0f) / f);
            VideoLayoutConfiguration videoLayoutConfiguration4 = this.mLocalVideoLayoutConfig;
            videoLayoutConfiguration4.mX = (i7 * i) / i2;
            videoLayoutConfiguration4.mY = ((100 - i7) - i6) - videoLayoutConfiguration4.mHeight;
        }
        VideoLayoutConfiguration videoLayoutConfiguration5 = this.mLocalVideoLayoutConfig;
        if (getResources().getConfiguration().orientation != 2) {
            z = true;
        }
        videoLayoutConfiguration5.mIsPortrait = z;
        VideoLayoutConfiguration videoLayoutConfiguration6 = this.mLocalVideoLayoutConfig;
        videoLayoutConfiguration6.mDisplayWidth = i2;
        videoLayoutConfiguration6.mDisplayHeight = i;
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## computeVideoUiLayout() : x ");
        sb.append(this.mLocalVideoLayoutConfig.mX);
        sb.append(" y ");
        sb.append(this.mLocalVideoLayoutConfig.mY);
        Log.d(str, sb.toString());
        String str2 = LOG_TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("## computeVideoUiLayout() : mWidth ");
        sb2.append(this.mLocalVideoLayoutConfig.mWidth);
        sb2.append(" mHeight ");
        sb2.append(this.mLocalVideoLayoutConfig.mHeight);
        Log.d(str2, sb2.toString());
    }

    /* access modifiers changed from: private */
    public void refreshMuteMicButton() {
        this.mMuteMicImageView.setImageResource(CallSoundsManager.getSharedInstance(this).isMicrophoneMute() ? R.drawable.ic_material_mic_off_pink_red : R.drawable.ic_material_mic_off_grey);
    }

    public void refreshSpeakerButton() {
        this.mSpeakerSelectionView.setImageResource(CallSoundsManager.getSharedInstance(this).isSpeakerphoneOn() ? R.drawable.ic_material_speaker_phone_pink_red : R.drawable.ic_material_speaker_phone_grey);
    }

    /* access modifiers changed from: private */
    public void refreshMuteVideoButton() {
        IMXCall iMXCall = this.mCall;
        if (iMXCall == null || !iMXCall.isVideo()) {
            Log.d(LOG_TAG, "## refreshMuteVideoButton(): View.INVISIBLE");
            this.mMuteLocalCameraView.setVisibility(4);
            return;
        }
        this.mMuteLocalCameraView.setVisibility(0);
        boolean isVideoRecordingMuted = this.mCall.isVideoRecordingMuted();
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## refreshMuteVideoButton(): isMuted=");
        sb.append(isVideoRecordingMuted);
        Log.d(str, sb.toString());
        this.mMuteLocalCameraView.setImageResource(isVideoRecordingMuted ? R.drawable.ic_material_videocam_off_pink_red : R.drawable.ic_material_videocam_off_grey);
    }

    /* access modifiers changed from: private */
    public void refreshSwitchRearFrontCameraButton() {
        IMXCall iMXCall = this.mCall;
        if (iMXCall == null || !iMXCall.isVideo() || !this.mCall.isSwitchCameraSupported()) {
            Log.d(LOG_TAG, "## refreshSwitchRearFrontCameraButton(): View.INVISIBLE");
            this.mSwitchRearFrontCameraImageView.setVisibility(4);
            return;
        }
        this.mSwitchRearFrontCameraImageView.setVisibility(0);
        boolean isCameraSwitched = this.mCall.isCameraSwitched();
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## refreshSwitchRearFrontCameraButton(): isSwitched=");
        sb.append(isCameraSwitched);
        Log.d(str, sb.toString());
        this.mSwitchRearFrontCameraImageView.setImageResource(isCameraSwitched ? R.drawable.ic_material_switch_video_pink_red : R.drawable.ic_material_switch_video_grey);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0093  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00cf  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00d7  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00f5  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00ff A[FALL_THROUGH] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0102 A[ADDED_TO_REGION] */
    public void manageSubViews() {
        char c;
        char c2;
        int i;
        IMXCall iMXCall = this.mCall;
        if (iMXCall == null) {
            Log.d(LOG_TAG, "## manageSubViews(): call instance = null, just return");
            return;
        }
        String callState = iMXCall.getCallState();
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## manageSubViews() IN callState : ");
        sb.append(callState);
        Log.d(str, sb.toString());
        ImageView imageView = this.mAvatarView;
        String str2 = IMXCall.CALL_STATE_CONNECTED;
        char c3 = 0;
        imageView.setVisibility((!callState.equals(str2) || !this.mCall.isVideo()) ? 0 : 8);
        refreshSpeakerButton();
        refreshMuteMicButton();
        refreshMuteVideoButton();
        refreshSwitchRearFrontCameraButton();
        int hashCode = callState.hashCode();
        String str3 = IMXCall.CALL_STATE_ENDED;
        if (hashCode != 1322015527) {
            if (hashCode == 1831632118 && callState.equals(str2)) {
                c = 1;
                if (c == 0) {
                    this.mHangUpImageView.setVisibility(4);
                } else if (c != 1) {
                    this.mHangUpImageView.setVisibility(0);
                } else {
                    initBackLightManagement();
                    this.mHangUpImageView.setVisibility(0);
                }
                if (this.mCall.isVideo()) {
                    if (((callState.hashCode() == 1831632118 && callState.equals(str2)) ? (char) 0 : 65535) != 0) {
                        stopVideoFadingEdgesScreenTimer();
                    } else {
                        startVideoFadingEdgesScreenTimer();
                    }
                }
                boolean isVideo = this.mCall.isVideo();
                String str4 = IMXCall.CALL_STATE_RINGING;
                if (isVideo && !callState.equals(str3)) {
                    switch (callState.hashCode()) {
                        case -215535408:
                            if (callState.equals(IMXCall.CALL_STATE_WAIT_CREATE_OFFER)) {
                                c2 = 0;
                                break;
                            }
                        case 183694318:
                            if (callState.equals(IMXCall.CALL_STATE_CREATE_ANSWER)) {
                                c2 = 3;
                                break;
                            }
                        case 358221275:
                            if (callState.equals(IMXCall.CALL_STATE_INVITE_SENT)) {
                                c2 = 1;
                                break;
                            }
                        case 946025035:
                            if (callState.equals(IMXCall.CALL_STATE_CONNECTING)) {
                                c2 = 4;
                                break;
                            }
                        case 1831632118:
                            if (callState.equals(str2)) {
                                c2 = 5;
                                break;
                            }
                        case 1960371423:
                            if (callState.equals(str4)) {
                                c2 = 2;
                                break;
                            }
                        default:
                            c2 = 65535;
                            break;
                    }
                    i = (c2 != 0 || c2 == 1 || c2 == 2 || c2 == 3 || c2 == 4 || c2 == 5) ? 0 : 8;
                    IMXCall iMXCall2 = this.mCall;
                    if (!(iMXCall2 == null || i == iMXCall2.getVisibility())) {
                        this.mCall.setVisibility(i);
                    }
                }
                if (callState.hashCode() != 1960371423 || !callState.equals(str4)) {
                    c3 = 65535;
                }
                if (c3 == 0 && this.mCall.isIncoming()) {
                    this.mCall.answer();
                    this.mIncomingCallTabbar.setVisibility(8);
                }
                Log.d(LOG_TAG, "## manageSubViews(): OUT");
            }
        } else if (callState.equals(str3)) {
            c = 0;
            if (c == 0) {
            }
            if (this.mCall.isVideo()) {
            }
            boolean isVideo2 = this.mCall.isVideo();
            String str42 = IMXCall.CALL_STATE_RINGING;
            switch (callState.hashCode()) {
                case -215535408:
                    break;
                case 183694318:
                    break;
                case 358221275:
                    break;
                case 946025035:
                    break;
                case 1831632118:
                    break;
                case 1960371423:
                    break;
            }
            if (c2 != 0) {
            }
            IMXCall iMXCall22 = this.mCall;
            this.mCall.setVisibility(i);
            c3 = 65535;
            this.mCall.answer();
            this.mIncomingCallTabbar.setVisibility(8);
            Log.d(LOG_TAG, "## manageSubViews(): OUT");
        }
        c = 65535;
        if (c == 0) {
        }
        if (this.mCall.isVideo()) {
        }
        boolean isVideo22 = this.mCall.isVideo();
        String str422 = IMXCall.CALL_STATE_RINGING;
        switch (callState.hashCode()) {
            case -215535408:
                break;
            case 183694318:
                break;
            case 358221275:
                break;
            case 946025035:
                break;
            case 1831632118:
                break;
            case 1960371423:
                break;
        }
        if (c2 != 0) {
        }
        IMXCall iMXCall222 = this.mCall;
        this.mCall.setVisibility(i);
        c3 = 65535;
        this.mCall.answer();
        this.mIncomingCallTabbar.setVisibility(8);
        Log.d(LOG_TAG, "## manageSubViews(): OUT");
    }

    private void saveCallView() {
        IMXCall iMXCall = this.mCall;
        if (iMXCall != null && !iMXCall.getCallState().equals(IMXCall.CALL_STATE_ENDED)) {
            View view = this.mCallView;
            if (view != null && view.getParent() != null) {
                this.mCall.onPause();
                ((ViewGroup) this.mCallView.getParent()).removeView(this.mCallView);
                this.mCallsManager.setCallView(this.mCallView);
                this.mCallsManager.setVideoLayoutConfiguration(this.mLocalVideoLayoutConfig);
                ((RelativeLayout) findViewById(R.id.call_layout)).setVisibility(8);
                this.mCallView = null;
            }
        }
    }

    private void initScreenManagement() {
        try {
            this.mField = PowerManager.class.getClass().getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
        } catch (Throwable th) {
            try {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## initScreenManagement ");
                sb.append(th.getMessage());
                Log.e(str, sb.toString(), th);
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## initScreenManagement() : failed ");
                sb2.append(e.getMessage());
                Log.e(str2, sb2.toString(), e);
                return;
            }
        }
        this.mWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(this.mField, getLocalClassName());
    }

    private void turnScreenOff() {
        if (this.mWakeLock == null) {
            initScreenManagement();
        }
        try {
            if (this.mWakeLock != null && !this.mWakeLock.isHeld()) {
                this.mWakeLock.acquire();
                this.mIsScreenOff = true;
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "## turnScreenOff() failed", e);
        }
        if (getWindow() != null && getWindow().getAttributes() != null) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.screenBrightness = 0.0f;
            getWindow().setAttributes(attributes);
        }
    }

    private void turnScreenOn() {
        try {
            if (this.mWakeLock != null) {
                this.mWakeLock.release();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "## turnScreenOn() failed", e);
        }
        this.mIsScreenOff = false;
        this.mWakeLock = null;
        if (getWindow() != null && getWindow().getAttributes() != null) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.screenBrightness = -1.0f;
            getWindow().setAttributes(attributes);
        }
    }

    private void stopProximitySensor() {
        if (this.mProximitySensor != null) {
            SensorManager sensorManager = this.mSensorMgr;
            if (sensorManager != null) {
                sensorManager.unregisterListener(this);
                this.mProximitySensor = null;
                this.mSensorMgr = null;
            }
        }
        turnScreenOn();
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent != null) {
            float f = sensorEvent.values[0];
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onSensorChanged(): ");
            sb.append(String.format(VectorLocale.INSTANCE.getApplicationLocale(), "distance=%.3f", new Object[]{Float.valueOf(f)}));
            Log.d(str, sb.toString());
            if (CallsManager.getSharedInstance().isSpeakerphoneOn()) {
                Log.d(LOG_TAG, "## onSensorChanged(): Skipped due speaker ON");
            } else if (f <= PROXIMITY_THRESHOLD) {
                turnScreenOff();
                Log.d(LOG_TAG, "## onSensorChanged(): force screen OFF");
            } else {
                turnScreenOn();
                Log.d(LOG_TAG, "## onSensorChanged(): force screen ON");
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onAccuracyChanged(): accuracy=");
        sb.append(i);
        Log.d(str, sb.toString());
    }
}
