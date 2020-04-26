package im.vector.webview;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.core.Log;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J \u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0006H\u0016J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u000f"}, d2 = {"Lim/vector/webview/DefaultWebViewEventListener;", "Lim/vector/webview/WebViewEventListener;", "()V", "onPageError", "", "url", "", "errorCode", "", "description", "onPageFinished", "onPageStarted", "pageWillStart", "shouldOverrideUrlLoading", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: DefaultWebViewEventListener.kt */
public final class DefaultWebViewEventListener implements WebViewEventListener {
    public void pageWillStart(String str) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        StringBuilder sb = new StringBuilder();
        sb.append("On page will start: ");
        sb.append(str);
        Log.v("DefaultWebViewEventListener", sb.toString());
    }

    public void onPageStarted(String str) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        StringBuilder sb = new StringBuilder();
        sb.append("On page started: ");
        sb.append(str);
        Log.d("DefaultWebViewEventListener", sb.toString());
    }

    public void onPageFinished(String str) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        StringBuilder sb = new StringBuilder();
        sb.append("On page finished: ");
        sb.append(str);
        Log.d("DefaultWebViewEventListener", sb.toString());
    }

    public void onPageError(String str, int i, String str2) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        Intrinsics.checkParameterIsNotNull(str2, "description");
        StringBuilder sb = new StringBuilder();
        sb.append("On received error: ");
        sb.append(str);
        sb.append(" - errorCode: ");
        sb.append(i);
        sb.append(" - message: ");
        sb.append(str2);
        Log.e("DefaultWebViewEventListener", sb.toString());
    }

    public boolean shouldOverrideUrlLoading(String str) {
        Intrinsics.checkParameterIsNotNull(str, "url");
        StringBuilder sb = new StringBuilder();
        sb.append("Should override url: ");
        sb.append(str);
        Log.v("DefaultWebViewEventListener", sb.toString());
        return false;
    }
}
