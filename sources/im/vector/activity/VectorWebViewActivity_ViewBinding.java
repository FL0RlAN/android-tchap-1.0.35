package im.vector.activity;

import android.view.View;
import android.webkit.WebView;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;

public final class VectorWebViewActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private VectorWebViewActivity target;

    public VectorWebViewActivity_ViewBinding(VectorWebViewActivity vectorWebViewActivity) {
        this(vectorWebViewActivity, vectorWebViewActivity.getWindow().getDecorView());
    }

    public VectorWebViewActivity_ViewBinding(VectorWebViewActivity vectorWebViewActivity, View view) {
        super(vectorWebViewActivity, view);
        this.target = vectorWebViewActivity;
        vectorWebViewActivity.webView = (WebView) Utils.findRequiredViewAsType(view, R.id.simple_webview, "field 'webView'", WebView.class);
    }

    public void unbind() {
        VectorWebViewActivity vectorWebViewActivity = this.target;
        if (vectorWebViewActivity != null) {
            this.target = null;
            vectorWebViewActivity.webView = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
