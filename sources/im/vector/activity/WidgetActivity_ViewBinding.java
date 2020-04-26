package im.vector.activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public final class WidgetActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private WidgetActivity target;
    private View view7f090393;
    private View view7f090394;

    public WidgetActivity_ViewBinding(WidgetActivity widgetActivity) {
        this(widgetActivity, widgetActivity.getWindow().getDecorView());
    }

    public WidgetActivity_ViewBinding(final WidgetActivity widgetActivity, View view) {
        super(widgetActivity, view);
        this.target = widgetActivity;
        View findRequiredView = Utils.findRequiredView(view, R.id.widget_close_icon, "field 'mCloseWidgetIcon' and method 'onCloseClick$vector_appAgentWithoutvoipWithpinningMatrixorg'");
        widgetActivity.mCloseWidgetIcon = findRequiredView;
        this.view7f090394 = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                widgetActivity.onCloseClick$vector_appAgentWithoutvoipWithpinningMatrixorg();
            }
        });
        widgetActivity.mWidgetWebView = (WebView) Utils.findRequiredViewAsType(view, R.id.widget_web_view, "field 'mWidgetWebView'", WebView.class);
        widgetActivity.mWidgetTypeTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.widget_title, "field 'mWidgetTypeTextView'", TextView.class);
        View findRequiredView2 = Utils.findRequiredView(view, R.id.widget_back_icon, "method 'onBackClicked$vector_appAgentWithoutvoipWithpinningMatrixorg'");
        this.view7f090393 = findRequiredView2;
        findRequiredView2.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                widgetActivity.onBackClicked$vector_appAgentWithoutvoipWithpinningMatrixorg();
            }
        });
    }

    public void unbind() {
        WidgetActivity widgetActivity = this.target;
        if (widgetActivity != null) {
            this.target = null;
            widgetActivity.mCloseWidgetIcon = null;
            widgetActivity.mWidgetWebView = null;
            widgetActivity.mWidgetTypeTextView = null;
            this.view7f090394.setOnClickListener(null);
            this.view7f090394 = null;
            this.view7f090393.setOnClickListener(null);
            this.view7f090393 = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
