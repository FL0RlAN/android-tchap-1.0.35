package im.vector.webview;

import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.crypto.rest.model.crypto.RoomKeyShare;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\"\u0010\r\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016J \u0010\u0010\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0017J(\u0010\u0010\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\fH\u0016J\u0010\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0018\u0010\u001a\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\u0012H\u0017J\u0018\u0010\u001a\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"Lim/vector/webview/VectorWebViewClient;", "Landroid/webkit/WebViewClient;", "eventListener", "Lim/vector/webview/WebViewEventListener;", "(Lim/vector/webview/WebViewEventListener;)V", "mInError", "", "onPageFinished", "", "view", "Landroid/webkit/WebView;", "url", "", "onPageStarted", "favicon", "Landroid/graphics/Bitmap;", "onReceivedError", "request", "Landroid/webkit/WebResourceRequest;", "error", "Landroid/webkit/WebResourceError;", "errorCode", "", "description", "failingUrl", "shouldOverrideUrl", "shouldOverrideUrlLoading", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorWebViewClient.kt */
public final class VectorWebViewClient extends WebViewClient {
    private final WebViewEventListener eventListener;
    private boolean mInError;

    public VectorWebViewClient(WebViewEventListener webViewEventListener) {
        Intrinsics.checkParameterIsNotNull(webViewEventListener, "eventListener");
        this.eventListener = webViewEventListener;
    }

    public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
        Intrinsics.checkParameterIsNotNull(webView, "view");
        Intrinsics.checkParameterIsNotNull(webResourceRequest, RoomKeyShare.ACTION_SHARE_REQUEST);
        String uri = webResourceRequest.getUrl().toString();
        Intrinsics.checkExpressionValueIsNotNull(uri, "request.url.toString()");
        return shouldOverrideUrl(uri);
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        Intrinsics.checkParameterIsNotNull(webView, "view");
        Intrinsics.checkParameterIsNotNull(str, "url");
        return shouldOverrideUrl(str);
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        Intrinsics.checkParameterIsNotNull(webView, "view");
        Intrinsics.checkParameterIsNotNull(str, "url");
        super.onPageStarted(webView, str, bitmap);
        this.mInError = false;
        this.eventListener.onPageStarted(str);
    }

    public void onPageFinished(WebView webView, String str) {
        Intrinsics.checkParameterIsNotNull(webView, "view");
        Intrinsics.checkParameterIsNotNull(str, "url");
        super.onPageFinished(webView, str);
        if (!this.mInError) {
            this.eventListener.onPageFinished(str);
        }
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
        Intrinsics.checkParameterIsNotNull(webView, "view");
        Intrinsics.checkParameterIsNotNull(str, "description");
        Intrinsics.checkParameterIsNotNull(str2, "failingUrl");
        super.onReceivedError(webView, i, str, str2);
        if (!this.mInError) {
            this.mInError = true;
            this.eventListener.onPageError(str2, i, str);
        }
    }

    public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
        Intrinsics.checkParameterIsNotNull(webView, "view");
        Intrinsics.checkParameterIsNotNull(webResourceRequest, RoomKeyShare.ACTION_SHARE_REQUEST);
        Intrinsics.checkParameterIsNotNull(webResourceError, "error");
        super.onReceivedError(webView, webResourceRequest, webResourceError);
        if (!this.mInError) {
            this.mInError = true;
            WebViewEventListener webViewEventListener = this.eventListener;
            String uri = webResourceRequest.getUrl().toString();
            Intrinsics.checkExpressionValueIsNotNull(uri, "request.url.toString()");
            webViewEventListener.onPageError(uri, webResourceError.getErrorCode(), webResourceError.getDescription().toString());
        }
    }

    private final boolean shouldOverrideUrl(String str) {
        this.mInError = false;
        boolean shouldOverrideUrlLoading = this.eventListener.shouldOverrideUrlLoading(str);
        if (!shouldOverrideUrlLoading) {
            this.eventListener.pageWillStart(str);
        }
        return shouldOverrideUrlLoading;
    }
}
