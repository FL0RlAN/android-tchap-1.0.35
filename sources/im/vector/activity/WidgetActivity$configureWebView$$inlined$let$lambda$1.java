package im.vector.activity;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H\u0016J&\u0010\b\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016¨\u0006\u000b¸\u0006\u0000"}, d2 = {"im/vector/activity/WidgetActivity$configureWebView$1$3", "Landroid/webkit/WebViewClient;", "onPageFinished", "", "view", "Landroid/webkit/WebView;", "url", "", "onPageStarted", "favicon", "Landroid/graphics/Bitmap;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: WidgetActivity.kt */
public final class WidgetActivity$configureWebView$$inlined$let$lambda$1 extends WebViewClient {
    final /* synthetic */ WidgetActivity this$0;

    WidgetActivity$configureWebView$$inlined$let$lambda$1(WidgetActivity widgetActivity) {
        this.this$0 = widgetActivity;
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        this.this$0.showWaitingView();
    }

    public void onPageFinished(WebView webView, String str) {
        this.this$0.hideWaitingView();
    }
}
