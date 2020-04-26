package im.vector.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fr.gouv.tchap.a.R;
import im.vector.widgets.Widget;
import im.vector.widgets.WidgetsManager;
import im.vector.widgets.WidgetsManager.onWidgetUpdateListener;
import java.util.ArrayList;
import java.util.List;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.Room;

public class ActiveWidgetsBanner extends RelativeLayout {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = ActiveWidgetsBanner.class.getSimpleName();
    /* access modifiers changed from: private */
    public List<Widget> mActiveWidgets = new ArrayList();
    private View mCloseWidgetIcon;
    private Context mContext;
    private Room mRoom;
    private MXSession mSession;
    /* access modifiers changed from: private */
    public onUpdateListener mUpdateListener;
    private final onWidgetUpdateListener mWidgetListener = new onWidgetUpdateListener() {
        public void onWidgetUpdate(Widget widget) {
            ActiveWidgetsBanner.this.refresh();
        }
    };
    private TextView mWidgetTypeTextView;

    public interface onUpdateListener {
        void onActiveWidgetsListUpdate();

        void onClick(List<Widget> list);

        void onCloseWidgetClick(Widget widget);
    }

    public ActiveWidgetsBanner(Context context) {
        super(context);
        initView(context);
    }

    public ActiveWidgetsBanner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public ActiveWidgetsBanner(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        View.inflate(getContext(), R.layout.active_widget_banner, this);
        this.mWidgetTypeTextView = (TextView) findViewById(R.id.widget_type_text_view);
        this.mCloseWidgetIcon = findViewById(R.id.close_widget_icon_container);
        this.mCloseWidgetIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ActiveWidgetsBanner.this.mUpdateListener != null) {
                    try {
                        ActiveWidgetsBanner.this.mUpdateListener.onCloseWidgetClick((Widget) ActiveWidgetsBanner.this.mActiveWidgets.get(0));
                    } catch (Exception e) {
                        String access$300 = ActiveWidgetsBanner.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## initView() : onCloseWidgetClick failed ");
                        sb.append(e.getMessage());
                        Log.e(access$300, sb.toString(), e);
                    }
                }
            }
        });
        setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ActiveWidgetsBanner.this.mUpdateListener != null) {
                    try {
                        ActiveWidgetsBanner.this.mUpdateListener.onClick(ActiveWidgetsBanner.this.mActiveWidgets);
                    } catch (Exception e) {
                        String access$300 = ActiveWidgetsBanner.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## initView() : onClick failed ");
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

    public void setOnUpdateListener(onUpdateListener onupdatelistener) {
        this.mUpdateListener = onupdatelistener;
    }

    /* access modifiers changed from: private */
    public void refresh() {
        if (this.mRoom != null && this.mSession != null) {
            List<Widget> activeWebviewWidgets = WidgetsManager.getSharedInstance().getActiveWebviewWidgets(this.mSession, this.mRoom);
            Widget widget = null;
            if (activeWebviewWidgets.size() != this.mActiveWidgets.size() || !this.mActiveWidgets.containsAll(activeWebviewWidgets)) {
                this.mActiveWidgets = activeWebviewWidgets;
                if (1 == this.mActiveWidgets.size()) {
                    widget = (Widget) this.mActiveWidgets.get(0);
                    this.mWidgetTypeTextView.setText(widget.getHumanName());
                } else if (this.mActiveWidgets.size() > 1) {
                    this.mWidgetTypeTextView.setText(this.mContext.getResources().getQuantityString(R.plurals.active_widgets, this.mActiveWidgets.size(), new Object[]{Integer.valueOf(this.mActiveWidgets.size())}));
                }
                onUpdateListener onupdatelistener = this.mUpdateListener;
                if (onupdatelistener != null) {
                    try {
                        onupdatelistener.onActiveWidgetsListUpdate();
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
            setVisibility(this.mActiveWidgets.size() > 0 ? 0 : 8);
            View view = this.mCloseWidgetIcon;
            if (widget != null && WidgetsManager.getSharedInstance().checkWidgetPermission(this.mSession, this.mRoom) == null) {
                i = 0;
            }
            view.setVisibility(i);
        }
    }

    public void onActivityResume() {
        refresh();
        WidgetsManager.getSharedInstance();
        WidgetsManager.addListener(this.mWidgetListener);
    }

    public void onActivityPause() {
        WidgetsManager.getSharedInstance();
        WidgetsManager.removeListener(this.mWidgetListener);
    }
}
