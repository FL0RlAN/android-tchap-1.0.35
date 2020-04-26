package im.vector.view;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fr.gouv.tchap.a.R;
import im.vector.widgets.Widget;
import im.vector.widgets.WidgetsManager;
import im.vector.widgets.WidgetsManager.onWidgetUpdateListener;
import java.util.List;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.call.IMXCallsManagerListener;
import org.matrix.androidsdk.call.MXCallsManager;
import org.matrix.androidsdk.call.MXCallsManagerListener;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.Room;

public class VectorOngoingConferenceCallView extends RelativeLayout {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorOngoingConferenceCallView.class.getSimpleName();
    /* access modifiers changed from: private */
    public Widget mActiveWidget;
    /* access modifiers changed from: private */
    public ICallClickListener mCallClickListener;
    private final IMXCallsManagerListener mCallsListener = new MXCallsManagerListener() {
        public void onVoipConferenceStarted(String str) {
            if (VectorOngoingConferenceCallView.this.mRoom != null && TextUtils.equals(str, VectorOngoingConferenceCallView.this.mRoom.getRoomId())) {
                VectorOngoingConferenceCallView.this.refresh();
            }
        }

        public void onVoipConferenceFinished(String str) {
            if (VectorOngoingConferenceCallView.this.mRoom != null && TextUtils.equals(str, VectorOngoingConferenceCallView.this.mRoom.getRoomId())) {
                VectorOngoingConferenceCallView.this.refresh();
            }
        }
    };
    private View mCloseWidgetIcon;
    /* access modifiers changed from: private */
    public Room mRoom;
    private MXSession mSession;
    private final onWidgetUpdateListener mWidgetListener = new onWidgetUpdateListener() {
        public void onWidgetUpdate(Widget widget) {
            VectorOngoingConferenceCallView.this.refresh();
        }
    };

    public interface ICallClickListener {
        void onActiveWidgetUpdate();

        void onCloseWidgetClick(Widget widget);

        void onVideoCallClick(Widget widget);

        void onVoiceCallClick(Widget widget);
    }

    public VectorOngoingConferenceCallView(Context context) {
        super(context);
        initView();
    }

    public VectorOngoingConferenceCallView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public VectorOngoingConferenceCallView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.vector_ongoing_conference_call, this);
        TextView textView = (TextView) findViewById(R.id.ongoing_conference_call_text_view);
        AnonymousClass3 r1 = new ClickableSpan() {
            public void onClick(View view) {
                if (VectorOngoingConferenceCallView.this.mCallClickListener != null) {
                    try {
                        VectorOngoingConferenceCallView.this.mCallClickListener.onVoiceCallClick(VectorOngoingConferenceCallView.this.mActiveWidget);
                    } catch (Exception e) {
                        String access$300 = VectorOngoingConferenceCallView.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## initView() : onVoiceCallClick failed ");
                        sb.append(e.getMessage());
                        Log.e(access$300, sb.toString(), e);
                    }
                }
            }
        };
        AnonymousClass4 r2 = new ClickableSpan() {
            public void onClick(View view) {
                if (VectorOngoingConferenceCallView.this.mCallClickListener != null) {
                    try {
                        VectorOngoingConferenceCallView.this.mCallClickListener.onVideoCallClick(VectorOngoingConferenceCallView.this.mActiveWidget);
                    } catch (Exception e) {
                        String access$300 = VectorOngoingConferenceCallView.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## initView() : onVideoCallClick failed ");
                        sb.append(e.getMessage());
                        Log.e(access$300, sb.toString(), e);
                    }
                }
            }
        };
        SpannableString spannableString = new SpannableString(textView.getText());
        String string = getContext().getString(R.string.ongoing_conference_call_voice);
        int indexOf = spannableString.toString().indexOf(string);
        String str = " in ";
        String str2 = "## initView() : cannot find ";
        if (indexOf >= 0) {
            spannableString.setSpan(r1, indexOf, string.length() + indexOf, 33);
            spannableString.setSpan(new UnderlineSpan(), indexOf, string.length() + indexOf, 0);
        } else {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(string);
            sb.append(str);
            sb.append(spannableString.toString());
            Log.e(str3, sb.toString());
        }
        String string2 = getContext().getString(R.string.ongoing_conference_call_video);
        int indexOf2 = spannableString.toString().indexOf(string2);
        if (indexOf2 >= 0) {
            spannableString.setSpan(r2, indexOf2, string2.length() + indexOf2, 33);
            spannableString.setSpan(new UnderlineSpan(), indexOf2, string2.length() + indexOf2, 0);
        } else {
            String str4 = LOG_TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(string2);
            sb2.append(str);
            sb2.append(spannableString.toString());
            Log.e(str4, sb2.toString());
        }
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        this.mCloseWidgetIcon = findViewById(R.id.close_widget_icon_container);
        this.mCloseWidgetIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VectorOngoingConferenceCallView.this.mCallClickListener != null) {
                    try {
                        VectorOngoingConferenceCallView.this.mCallClickListener.onCloseWidgetClick(VectorOngoingConferenceCallView.this.mActiveWidget);
                    } catch (Exception e) {
                        String access$300 = VectorOngoingConferenceCallView.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## initView() : onCloseWidgetClick failed ");
                        sb.append(e.getMessage());
                        Log.e(access$300, sb.toString(), e);
                    }
                }
            }
        });
    }

    public void initRoomInfo(MXSession mXSession, Room room) {
        this.mSession = mXSession;
        this.mRoom = room;
    }

    public void setCallClickListener(ICallClickListener iCallClickListener) {
        this.mCallClickListener = iCallClickListener;
    }

    public void refresh() {
        if (this.mRoom != null && this.mSession != null) {
            List activeJitsiWidgets = WidgetsManager.getSharedInstance().getActiveJitsiWidgets(this.mSession, this.mRoom);
            Widget widget = activeJitsiWidgets.isEmpty() ? null : (Widget) activeJitsiWidgets.get(0);
            if (this.mActiveWidget != widget) {
                this.mActiveWidget = widget;
                ICallClickListener iCallClickListener = this.mCallClickListener;
                if (iCallClickListener != null) {
                    try {
                        iCallClickListener.onActiveWidgetUpdate();
                    } catch (Exception e) {
                        String str = LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## refresh() : onActiveWidgetUpdate failed ");
                        sb.append(e.getMessage());
                        Log.e(str, sb.toString(), e);
                    }
                }
            }
            int i = 8;
            setVisibility(((MXCallsManager.isCallInProgress(this.mSession.mCallsManager.getCallWithRoomId(this.mRoom.getRoomId())) || !this.mRoom.isOngoingConferenceCall()) && this.mActiveWidget == null) ? 8 : 0);
            View view = this.mCloseWidgetIcon;
            if (this.mActiveWidget != null && WidgetsManager.getSharedInstance().checkWidgetPermission(this.mSession, this.mRoom) == null) {
                i = 0;
            }
            view.setVisibility(i);
        }
    }

    public void onActivityResume() {
        refresh();
        MXSession mXSession = this.mSession;
        if (mXSession != null) {
            mXSession.mCallsManager.addListener(this.mCallsListener);
        }
        WidgetsManager.addListener(this.mWidgetListener);
    }

    public void onActivityPause() {
        MXSession mXSession = this.mSession;
        if (mXSession != null) {
            mXSession.mCallsManager.removeListener(this.mCallsListener);
        }
        WidgetsManager.removeListener(this.mWidgetListener);
    }

    public Widget getActiveWidget() {
        return this.mActiveWidget;
    }
}
