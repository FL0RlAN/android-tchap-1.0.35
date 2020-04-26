package im.vector.activity;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, d2 = {"im/vector/activity/VectorWebViewActivity$initUiAndData$2", "Landroid/webkit/WebChromeClient;", "onReceivedTitle", "", "view", "Landroid/webkit/WebView;", "title", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorWebViewActivity.kt */
public final class VectorWebViewActivity$initUiAndData$2 extends WebChromeClient {
    final /* synthetic */ int $titleRes;
    final /* synthetic */ VectorWebViewActivity this$0;

    VectorWebViewActivity$initUiAndData$2(VectorWebViewActivity vectorWebViewActivity, int i) {
        this.this$0 = vectorWebViewActivity;
        this.$titleRes = i;
    }

    public void onReceivedTitle(WebView webView, String str) {
        Intrinsics.checkParameterIsNotNull(webView, "view");
        Intrinsics.checkParameterIsNotNull(str, "title");
        if (this.$titleRes == -1) {
            this.this$0.setTitle(str);
        }
    }
}
