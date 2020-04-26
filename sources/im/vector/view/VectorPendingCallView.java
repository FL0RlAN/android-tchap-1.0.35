package im.vector.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fr.gouv.tchap.a.R;
import im.vector.util.CallUtilities;
import im.vector.util.CallsManager;
import im.vector.util.VectorUtils;
import org.matrix.androidsdk.call.IMXCall;
import org.matrix.androidsdk.call.IMXCallListener;
import org.matrix.androidsdk.call.MXCallListener;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.data.Room;

public class VectorPendingCallView extends RelativeLayout {
    /* access modifiers changed from: private */
    public IMXCall mCall;
    private TextView mCallDescriptionTextView;
    private final IMXCallListener mCallListener = new MXCallListener() {
        public void onPreviewSizeChanged(int i, int i2) {
        }

        public void onStateDidChange(String str) {
            VectorPendingCallView.this.refresh();
        }

        public void onCallError(String str) {
            VectorPendingCallView.this.refresh();
        }

        public void onCallViewCreated(View view) {
            VectorPendingCallView.this.refresh();
        }

        public void onCallAnsweredElsewhere() {
            VectorPendingCallView.this.onCallTerminated();
        }

        public void onCallEnd(int i) {
            VectorPendingCallView.this.onCallTerminated();
        }
    };
    private TextView mCallStatusTextView;
    private boolean mIsCallStatusHidden;
    private View mMainView;
    /* access modifiers changed from: private */
    public Handler mUIHandler;

    public VectorPendingCallView(Context context) {
        super(context);
        initView();
    }

    public VectorPendingCallView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public VectorPendingCallView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.vector_pending_call_view, this);
        this.mMainView = findViewById(R.id.main_view);
        this.mCallDescriptionTextView = (TextView) findViewById(R.id.pending_call_room_name_textview);
        this.mCallDescriptionTextView.setVisibility(8);
        this.mCallStatusTextView = (TextView) findViewById(R.id.pending_call_status_textview);
        this.mCallStatusTextView.setVisibility(8);
        this.mUIHandler = new Handler(Looper.getMainLooper());
    }

    public void checkPendingCall() {
        IMXCall activeCall = CallsManager.getSharedInstance().getActiveCall();
        if (activeCall == null) {
            IMXCall iMXCall = this.mCall;
            if (iMXCall != null) {
                iMXCall.removeListener(this.mCallListener);
            }
            this.mCall = null;
            setVisibility(8);
            return;
        }
        IMXCall iMXCall2 = this.mCall;
        if (iMXCall2 != activeCall) {
            if (iMXCall2 != null) {
                iMXCall2.removeListener(this.mCallListener);
            }
            this.mCall = activeCall;
            activeCall.addListener(this.mCallListener);
            setVisibility(0);
        }
        refresh();
    }

    /* access modifiers changed from: private */
    public void refresh() {
        this.mUIHandler.post(new Runnable() {
            public void run() {
                if (VectorPendingCallView.this.mCall != null) {
                    VectorPendingCallView.this.refreshCallDescription();
                    VectorPendingCallView.this.refreshCallStatus();
                    VectorPendingCallView.this.mUIHandler.postDelayed(new Runnable() {
                        public void run() {
                            VectorPendingCallView.this.refresh();
                        }
                    }, 1000);
                }
            }
        });
    }

    public void onCallTerminated() {
        checkPendingCall();
    }

    /* access modifiers changed from: private */
    public void refreshCallDescription() {
        if (this.mCall != null) {
            this.mCallDescriptionTextView.setVisibility(0);
            updateDescription(this.mCall.getCallId());
            Room room = this.mCall.getRoom();
            if (room != null) {
                VectorUtils.getCallingRoomDisplayName(getContext(), this.mCall.getSession(), room, new ApiCallback<String>() {
                    public void onMatrixError(MatrixError matrixError) {
                    }

                    public void onNetworkError(Exception exc) {
                    }

                    public void onUnexpectedError(Exception exc) {
                    }

                    public void onSuccess(String str) {
                        VectorPendingCallView.this.updateDescription(str);
                    }
                });
                return;
            }
            return;
        }
        this.mCallDescriptionTextView.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void updateDescription(String str) {
        if (TextUtils.equals(this.mCall.getCallState(), IMXCall.CALL_STATE_CONNECTED) && !this.mIsCallStatusHidden) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(" - ");
            sb.append(getResources().getString(R.string.active_call));
            str = sb.toString();
        }
        this.mCallDescriptionTextView.setText(str);
    }

    /* access modifiers changed from: private */
    public void refreshCallStatus() {
        String callStatus = CallUtilities.getCallStatus(getContext(), this.mCall);
        this.mCallStatusTextView.setText(callStatus);
        this.mCallStatusTextView.setVisibility(TextUtils.isEmpty(callStatus) ? 8 : 0);
    }

    public void enableCallStatusDisplay(boolean z) {
        this.mIsCallStatusHidden = !z;
    }

    public void updateBackgroundColor(int i) {
        this.mMainView.setBackgroundColor(i);
    }
}
