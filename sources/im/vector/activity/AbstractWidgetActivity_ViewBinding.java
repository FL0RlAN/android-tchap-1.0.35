package im.vector.activity;

import android.view.View;
import android.webkit.WebView;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public class AbstractWidgetActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private AbstractWidgetActivity target;

    public AbstractWidgetActivity_ViewBinding(AbstractWidgetActivity abstractWidgetActivity) {
        this(abstractWidgetActivity, abstractWidgetActivity.getWindow().getDecorView());
    }

    public AbstractWidgetActivity_ViewBinding(AbstractWidgetActivity abstractWidgetActivity, View view) {
        super(abstractWidgetActivity, view);
        this.target = abstractWidgetActivity;
        abstractWidgetActivity.mWebView = (WebView) Utils.findRequiredViewAsType(view, R.id.widget_webview, "field 'mWebView'", WebView.class);
    }

    public void unbind() {
        AbstractWidgetActivity abstractWidgetActivity = this.target;
        if (abstractWidgetActivity != null) {
            this.target = null;
            abstractWidgetActivity.mWebView = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
