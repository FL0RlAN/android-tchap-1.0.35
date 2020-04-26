package im.vector.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import butterknife.BindView;
import fr.gouv.tchap.a.R;
import im.vector.ui.themes.ActivityOtherThemes.NoActionBar;
import im.vector.webview.VectorWebViewClient;
import im.vector.webview.WebViewEventListener;
import im.vector.webview.WebViewMode;
import java.io.Serializable;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\fH\u0016J\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u000eH\u0016R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\u0011"}, d2 = {"Lim/vector/activity/VectorWebViewActivity;", "Lim/vector/activity/VectorAppCompatActivity;", "()V", "webView", "Landroid/webkit/WebView;", "getWebView", "()Landroid/webkit/WebView;", "setWebView", "(Landroid/webkit/WebView;)V", "getLayoutRes", "", "getOtherThemes", "Lim/vector/ui/themes/ActivityOtherThemes$NoActionBar;", "initUiAndData", "", "onBackPressed", "Companion", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: VectorWebViewActivity.kt */
public final class VectorWebViewActivity extends VectorAppCompatActivity {
    public static final Companion Companion = new Companion(null);
    private static final String EXTRA_MODE = "EXTRA_MODE";
    private static final String EXTRA_TITLE_RES_ID = "EXTRA_TITLE_RES_ID";
    private static final String EXTRA_URL = "EXTRA_URL";
    private static final int INVALID_RES_ID = -1;
    private HashMap _$_findViewCache;
    @BindView(2131297030)
    public WebView webView;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J*\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00042\b\b\u0003\u0010\u000e\u001a\u00020\b2\b\b\u0002\u0010\u000f\u001a\u00020\u0010R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bXT¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lim/vector/activity/VectorWebViewActivity$Companion;", "", "()V", "EXTRA_MODE", "", "EXTRA_TITLE_RES_ID", "EXTRA_URL", "INVALID_RES_ID", "", "getIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "url", "titleRes", "mode", "Lim/vector/webview/WebViewMode;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: VectorWebViewActivity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ Intent getIntent$default(Companion companion, Context context, String str, int i, WebViewMode webViewMode, int i2, Object obj) {
            if ((i2 & 4) != 0) {
                i = -1;
            }
            if ((i2 & 8) != 0) {
                webViewMode = WebViewMode.DEFAULT;
            }
            return companion.getIntent(context, str, i, webViewMode);
        }

        public final Intent getIntent(Context context, String str, int i, WebViewMode webViewMode) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(str, "url");
            Intrinsics.checkParameterIsNotNull(webViewMode, "mode");
            Intent intent = new Intent(context, VectorWebViewActivity.class);
            intent.putExtra(VectorWebViewActivity.EXTRA_URL, str);
            intent.putExtra(VectorWebViewActivity.EXTRA_TITLE_RES_ID, i);
            intent.putExtra(VectorWebViewActivity.EXTRA_MODE, webViewMode);
            return intent;
        }
    }

    public void _$_clearFindViewByIdCache() {
        HashMap hashMap = this._$_findViewCache;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public int getLayoutRes() {
        return R.layout.activity_vector_web_view;
    }

    public final WebView getWebView() {
        WebView webView2 = this.webView;
        if (webView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("webView");
        }
        return webView2;
    }

    public final void setWebView(WebView webView2) {
        Intrinsics.checkParameterIsNotNull(webView2, "<set-?>");
        this.webView = webView2;
    }

    public NoActionBar getOtherThemes() {
        return NoActionBar.INSTANCE;
    }

    public void initUiAndData() {
        configureToolbar();
        setWaitingView(findViewById(R.id.simple_webview_loader));
        WebView webView2 = this.webView;
        String str = "webView";
        if (webView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        WebSettings settings = webView2.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setDisplayZoomControls(false);
        if (VERSION.SDK_INT >= 21) {
            CookieManager instance = CookieManager.getInstance();
            WebView webView3 = this.webView;
            if (webView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            instance.setAcceptThirdPartyCookies(webView3, true);
        }
        Intent intent = getIntent();
        String str2 = "intent";
        Intrinsics.checkExpressionValueIsNotNull(intent, str2);
        String string = intent.getExtras().getString(EXTRA_URL);
        Intent intent2 = getIntent();
        Intrinsics.checkExpressionValueIsNotNull(intent2, str2);
        int i = intent2.getExtras().getInt(EXTRA_TITLE_RES_ID, -1);
        if (i != -1) {
            setTitle(i);
        }
        Intent intent3 = getIntent();
        Intrinsics.checkExpressionValueIsNotNull(intent3, str2);
        Serializable serializable = intent3.getExtras().getSerializable(EXTRA_MODE);
        if (serializable != null) {
            WebViewEventListener eventListener = ((WebViewMode) serializable).eventListener(this);
            WebView webView4 = this.webView;
            if (webView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            webView4.setWebViewClient(new VectorWebViewClient(eventListener));
            WebView webView5 = this.webView;
            if (webView5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            webView5.setWebChromeClient(new VectorWebViewActivity$initUiAndData$2(this, i));
            WebView webView6 = this.webView;
            if (webView6 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            webView6.loadUrl(string);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type im.vector.webview.WebViewMode");
    }

    public void onBackPressed() {
        WebView webView2 = this.webView;
        String str = "webView";
        if (webView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        if (webView2.canGoBack()) {
            WebView webView3 = this.webView;
            if (webView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            webView3.goBack();
            return;
        }
        super.onBackPressed();
    }
}
