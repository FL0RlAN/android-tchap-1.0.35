package im.vector.activity;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog.Builder;
import butterknife.BindView;
import butterknife.OnClick;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.ui.themes.ActivityOtherThemes.NoActionBar;
import im.vector.widgets.Widget;
import im.vector.widgets.WidgetsManager;
import im.vector.widgets.WidgetsManager.onWidgetUpdateListener;
import java.io.Serializable;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.MXDataHandler;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.data.Room;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u0000 -2\u00020\u0001:\u0001-B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u001d\u001a\u00020\u001eH\u0003J\b\u0010\u001f\u001a\u00020 H\u0016J\b\u0010!\u001a\u00020\"H\u0016J\b\u0010#\u001a\u00020\u001eH\u0017J\b\u0010$\u001a\u00020\u001eH\u0002J\r\u0010%\u001a\u00020\u001eH\u0001¢\u0006\u0002\b&J\r\u0010'\u001a\u00020\u001eH\u0001¢\u0006\u0002\b(J\b\u0010)\u001a\u00020\u001eH\u0014J\b\u0010*\u001a\u00020\u001eH\u0014J\b\u0010+\u001a\u00020\u001eH\u0014J\b\u0010,\u001a\u00020\u001eH\u0002R\u001e\u0010\u0003\u001a\u00020\u00048\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u0011\u001a\u00020\u00128\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001e\u0010\u0017\u001a\u00020\u00188\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001c¨\u0006."}, d2 = {"Lim/vector/activity/WidgetActivity;", "Lim/vector/activity/VectorAppCompatActivity;", "()V", "mCloseWidgetIcon", "Landroid/view/View;", "getMCloseWidgetIcon", "()Landroid/view/View;", "setMCloseWidgetIcon", "(Landroid/view/View;)V", "mRoom", "Lorg/matrix/androidsdk/data/Room;", "mSession", "Lorg/matrix/androidsdk/MXSession;", "mWidget", "Lim/vector/widgets/Widget;", "mWidgetListener", "Lim/vector/widgets/WidgetsManager$onWidgetUpdateListener;", "mWidgetTypeTextView", "Landroid/widget/TextView;", "getMWidgetTypeTextView", "()Landroid/widget/TextView;", "setMWidgetTypeTextView", "(Landroid/widget/TextView;)V", "mWidgetWebView", "Landroid/webkit/WebView;", "getMWidgetWebView", "()Landroid/webkit/WebView;", "setMWidgetWebView", "(Landroid/webkit/WebView;)V", "configureWebView", "", "getLayoutRes", "", "getOtherThemes", "Lim/vector/ui/themes/ActivityOtherThemes$NoActionBar;", "initUiAndData", "loadUrl", "onBackClicked", "onBackClicked$vector_appAgentWithoutvoipWithpinningMatrixorg", "onCloseClick", "onCloseClick$vector_appAgentWithoutvoipWithpinningMatrixorg", "onDestroy", "onPause", "onResume", "refreshStatusBar", "Companion", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: WidgetActivity.kt */
public final class WidgetActivity extends VectorAppCompatActivity {
    public static final Companion Companion = new Companion(null);
    private static final String EXTRA_WIDGET_ID = "EXTRA_WIDGET_ID";
    private static final String LOG_TAG = WidgetActivity.class.getSimpleName();
    private HashMap _$_findViewCache;
    @BindView(2131297172)
    public View mCloseWidgetIcon;
    /* access modifiers changed from: private */
    public Room mRoom;
    /* access modifiers changed from: private */
    public MXSession mSession;
    /* access modifiers changed from: private */
    public Widget mWidget;
    private final onWidgetUpdateListener mWidgetListener = new WidgetActivity$mWidgetListener$1(this);
    @BindView(2131297175)
    public TextView mWidgetTypeTextView;
    @BindView(2131297177)
    public WebView mWidgetWebView;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n \u0006*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lim/vector/activity/WidgetActivity$Companion;", "", "()V", "EXTRA_WIDGET_ID", "", "LOG_TAG", "kotlin.jvm.PlatformType", "getIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "widget", "Lim/vector/widgets/Widget;", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: WidgetActivity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Intent getIntent(Context context, Widget widget) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(widget, "widget");
            Intent intent = new Intent(context, WidgetActivity.class);
            intent.putExtra("EXTRA_WIDGET_ID", widget);
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
        return R.layout.activity_widget;
    }

    public final View getMCloseWidgetIcon() {
        View view = this.mCloseWidgetIcon;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mCloseWidgetIcon");
        }
        return view;
    }

    public final void setMCloseWidgetIcon(View view) {
        Intrinsics.checkParameterIsNotNull(view, "<set-?>");
        this.mCloseWidgetIcon = view;
    }

    public final WebView getMWidgetWebView() {
        WebView webView = this.mWidgetWebView;
        if (webView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mWidgetWebView");
        }
        return webView;
    }

    public final void setMWidgetWebView(WebView webView) {
        Intrinsics.checkParameterIsNotNull(webView, "<set-?>");
        this.mWidgetWebView = webView;
    }

    public final TextView getMWidgetTypeTextView() {
        TextView textView = this.mWidgetTypeTextView;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mWidgetTypeTextView");
        }
        return textView;
    }

    public final void setMWidgetTypeTextView(TextView textView) {
        Intrinsics.checkParameterIsNotNull(textView, "<set-?>");
        this.mWidgetTypeTextView = textView;
    }

    public NoActionBar getOtherThemes() {
        return NoActionBar.INSTANCE;
    }

    public void initUiAndData() {
        setWaitingView(findViewById(R.id.widget_progress_layout));
        Serializable serializableExtra = getIntent().getSerializableExtra("EXTRA_WIDGET_ID");
        if (serializableExtra != null) {
            this.mWidget = (Widget) serializableExtra;
            Widget widget = this.mWidget;
            if (widget != null) {
                if (widget == null) {
                    Intrinsics.throwNpe();
                }
                if (widget.getUrl() != null) {
                    Context context = this;
                    Widget widget2 = this.mWidget;
                    if (widget2 == null) {
                        Intrinsics.throwNpe();
                    }
                    this.mSession = Matrix.getMXSession(context, widget2.getSessionId());
                    MXSession mXSession = this.mSession;
                    if (mXSession == null) {
                        Log.e(LOG_TAG, "## onCreate() : invalid session");
                        finish();
                        return;
                    }
                    if (mXSession == null) {
                        Intrinsics.throwNpe();
                    }
                    MXDataHandler dataHandler = mXSession.getDataHandler();
                    Widget widget3 = this.mWidget;
                    if (widget3 == null) {
                        Intrinsics.throwNpe();
                    }
                    this.mRoom = dataHandler.getRoom(widget3.getRoomId());
                    if (this.mRoom == null) {
                        Log.e(LOG_TAG, "## onCreate() : invalid room");
                        finish();
                        return;
                    }
                    TextView textView = this.mWidgetTypeTextView;
                    if (textView == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("mWidgetTypeTextView");
                    }
                    Widget widget4 = this.mWidget;
                    if (widget4 == null) {
                        Intrinsics.throwNpe();
                    }
                    textView.setText(widget4.getHumanName());
                    configureWebView();
                    loadUrl();
                    return;
                }
            }
            Log.e(LOG_TAG, "## onCreate() : invalid widget");
            finish();
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type im.vector.widgets.Widget");
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        WebView webView = this.mWidgetWebView;
        if (webView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mWidgetWebView");
        }
        ViewParent parent = webView.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(webView);
            webView.removeAllViews();
            webView.destroy();
            super.onDestroy();
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        WidgetsManager.addListener(this.mWidgetListener);
        WebView webView = this.mWidgetWebView;
        if (webView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mWidgetWebView");
        }
        webView.resumeTimers();
        webView.onResume();
        refreshStatusBar();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        WebView webView = this.mWidgetWebView;
        if (webView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mWidgetWebView");
        }
        webView.pauseTimers();
        webView.onPause();
        WidgetsManager.removeListener(this.mWidgetListener);
    }

    @OnClick({2131297172})
    public final void onCloseClick$vector_appAgentWithoutvoipWithpinningMatrixorg() {
        new Builder(this).setMessage((int) R.string.widget_delete_message_confirmation).setPositiveButton((int) R.string.remove, (OnClickListener) new WidgetActivity$onCloseClick$1(this)).setNegativeButton((int) R.string.cancel, (OnClickListener) null).show();
    }

    @OnClick({2131297171})
    public final void onBackClicked$vector_appAgentWithoutvoipWithpinningMatrixorg() {
        WebView webView = this.mWidgetWebView;
        String str = "mWidgetWebView";
        if (webView == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        if (webView.canGoBack()) {
            WebView webView2 = this.mWidgetWebView;
            if (webView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            webView2.goBack();
            return;
        }
        finish();
    }

    private final void refreshStatusBar() {
        int i = 0;
        boolean z = WidgetsManager.getSharedInstance().checkWidgetPermission(this.mSession, this.mRoom) == null;
        View view = this.mCloseWidgetIcon;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mCloseWidgetIcon");
        }
        if (!z) {
            i = 4;
        }
        view.setVisibility(i);
    }

    private final void configureWebView() {
        WebView webView = this.mWidgetWebView;
        String str = "mWidgetWebView";
        if (webView == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        webView.setBackgroundColor(0);
        webView.clearHistory();
        webView.clearFormData();
        webView.clearCache(true);
        WebSettings settings = webView.getSettings();
        Intrinsics.checkExpressionValueIsNotNull(settings, "it");
        settings.setCacheMode(2);
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
        webView.setWebChromeClient(new WidgetActivity$configureWebView$1$2());
        webView.setWebViewClient(new WidgetActivity$configureWebView$$inlined$let$lambda$1(this));
        if (VERSION.SDK_INT >= 21) {
            CookieManager instance = CookieManager.getInstance();
            WebView webView2 = this.mWidgetWebView;
            if (webView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            instance.setAcceptThirdPartyCookies(webView2, true);
        }
    }

    private final void loadUrl() {
        showWaitingView();
        Context context = this;
        Widget widget = this.mWidget;
        if (widget == null) {
            Intrinsics.throwNpe();
        }
        WidgetsManager.getFormattedWidgetUrl(context, widget, new WidgetActivity$loadUrl$1(this));
    }
}
