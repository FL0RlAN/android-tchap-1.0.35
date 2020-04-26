package im.vector.activity;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import im.vector.util.AssetReader;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J&\u0010\b\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016¨\u0006\u000b¸\u0006\u0000"}, d2 = {"im/vector/activity/AbstractWidgetActivity$initWebView$1$3", "Landroid/webkit/WebViewClient;", "onPageFinished", "", "view", "Landroid/webkit/WebView;", "url", "", "onPageStarted", "favicon", "Landroid/graphics/Bitmap;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: AbstractWidgetActivity.kt */
public final class AbstractWidgetActivity$initWebView$$inlined$let$lambda$2 extends WebViewClient {
    final /* synthetic */ AbstractWidgetActivity this$0;

    AbstractWidgetActivity$initWebView$$inlined$let$lambda$2(AbstractWidgetActivity abstractWidgetActivity) {
        this.this$0 = abstractWidgetActivity;
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        String access$getLOG_TAG$cp = AbstractWidgetActivity.LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onPageStarted - Url: ");
        sb.append(str);
        Log.d(access$getLOG_TAG$cp, sb.toString());
        this.this$0.showWaitingView();
    }

    public void onPageFinished(WebView webView, String str) {
        Intrinsics.checkParameterIsNotNull(webView, "view");
        Intrinsics.checkParameterIsNotNull(str, "url");
        if (!this.this$0.isDestroyed()) {
            this.this$0.hideWaitingView();
            final String readAssetFile = AssetReader.INSTANCE.readAssetFile(this.this$0, "postMessageAPI.js");
            if (readAssetFile != null) {
                this.this$0.runOnUiThread(new Runnable(this) {
                    final /* synthetic */ AbstractWidgetActivity$initWebView$$inlined$let$lambda$2 this$0;

                    {
                        this.this$0 = r1;
                    }

                    public final void run() {
                        WebView mWebView = this.this$0.this$0.getMWebView();
                        StringBuilder sb = new StringBuilder();
                        sb.append("javascript:");
                        sb.append(readAssetFile);
                        mWebView.loadUrl(sb.toString());
                    }
                });
            }
        }
    }
}
