package im.vector.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import butterknife.BindView;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.activity.util.RequestCodesKt;
import im.vector.util.JsonUtilKt;
import im.vector.widgets.WidgetsManager;
import java.util.HashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.crypto.cryptostore.db.model.CryptoRoomEntityFields;
import org.matrix.androidsdk.data.Room;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010$\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\n\b&\u0018\u0000 52\u00020\u0001:\u0003567B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0015\u001a\u0004\u0018\u00010\u00162\u0006\u0010\u0017\u001a\u00020\u0016H&J&\u0010\u0018\u001a\u00020\u00192\u001c\u0010\u001a\u001a\u0018\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u001c0\u001bj\b\u0012\u0004\u0012\u00020\u001c`\u001dH\u0017J\b\u0010\u001e\u001a\u00020\u001fH\u0017J\b\u0010 \u001a\u00020\u001fH\u0003J\u0010\u0010!\u001a\u00020\u001f2\u0006\u0010\u0017\u001a\u00020\u0016H\u0002J\b\u0010\"\u001a\u00020\u001fH\u0016J0\u0010#\u001a\u00020\u001f2&\u0010$\u001a\"\u0012\u0004\u0012\u00020\u0016\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u001c0\u001b\u0018\u00010\u001bj\u0004\u0018\u0001`%H\u0002J\u001c\u0010&\u001a\u00020\u001f2\b\u0010'\u001a\u0004\u0018\u00010\u00162\b\u0010(\u001a\u0004\u0018\u00010\u0016H\u0004J.\u0010)\u001a\u00020\u001f2\u0006\u0010*\u001a\u00020\u00192\u001c\u0010\u001a\u001a\u0018\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u001c0\u001bj\b\u0012\u0004\u0012\u00020\u001c`\u001dH\u0004J.\u0010+\u001a\u00020\u001f2\u0006\u0010,\u001a\u00020\u00162\u001c\u0010\u001a\u001a\u0018\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u001c0\u001bj\b\u0012\u0004\u0012\u00020\u001c`\u001dH\u0004J.\u0010-\u001a\u00020\u001f2\u0006\u0010*\u001a\u00020.2\u001c\u0010\u001a\u001a\u0018\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u001c0\u001bj\b\u0012\u0004\u0012\u00020\u001c`\u001dH\u0004J.\u0010/\u001a\u00020\u001f2\u0006\u00100\u001a\u00020\u001c2\u001c\u0010\u001a\u001a\u0018\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u001c0\u001bj\b\u0012\u0004\u0012\u00020\u001c`\u001dH\u0004J0\u00101\u001a\u00020\u001f2\b\u0010*\u001a\u0004\u0018\u00010\u001c2\u001c\u0010\u001a\u001a\u0018\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u001c0\u001bj\b\u0012\u0004\u0012\u00020\u001c`\u001dH\u0004J.\u00102\u001a\u00020\u001f2\u0006\u00103\u001a\u00020\u00162\u001c\u0010\u001a\u001a\u0018\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u001c0\u001bj\b\u0012\u0004\u0012\u00020\u001c`\u001dH\u0002J&\u00104\u001a\u00020\u001f2\u001c\u0010\u001a\u001a\u0018\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u001c0\u001bj\b\u0012\u0004\u0012\u00020\u001c`\u001dH\u0004R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001e\u0010\u000f\u001a\u00020\u00108\u0006@\u0006X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014¨\u00068"}, d2 = {"Lim/vector/activity/AbstractWidgetActivity;", "Lim/vector/activity/VectorAppCompatActivity;", "()V", "mRoom", "Lorg/matrix/androidsdk/data/Room;", "getMRoom", "()Lorg/matrix/androidsdk/data/Room;", "setMRoom", "(Lorg/matrix/androidsdk/data/Room;)V", "mSession", "Lorg/matrix/androidsdk/MXSession;", "getMSession", "()Lorg/matrix/androidsdk/MXSession;", "setMSession", "(Lorg/matrix/androidsdk/MXSession;)V", "mWebView", "Landroid/webkit/WebView;", "getMWebView", "()Landroid/webkit/WebView;", "setMWebView", "(Landroid/webkit/WebView;)V", "buildInterfaceUrl", "", "scalarToken", "dealsWithWidgetRequest", "", "eventData", "", "", "Lim/vector/types/JsonDict;", "initUiAndData", "", "initWebView", "launchUrl", "onBackPressed", "onWidgetMessage", "JSData", "Lim/vector/types/WidgetEventData;", "openIntegrationManager", "widgetId", "screenId", "sendBoolResponse", "response", "sendError", "message", "sendIntegerResponse", "", "sendObjectAsJsonMap", "any", "sendObjectResponse", "sendResponse", "jsString", "sendSuccess", "Companion", "WidgetApiCallback", "WidgetWebAppInterface", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
/* compiled from: AbstractWidgetActivity.kt */
public abstract class AbstractWidgetActivity extends VectorAppCompatActivity {
    public static final Companion Companion = new Companion(null);
    public static final String EXTRA_MATRIX_ID = "EXTRA_MATRIX_ID";
    public static final String EXTRA_ROOM_ID = "EXTRA_ROOM_ID";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = AbstractWidgetActivity.class.getSimpleName();
    private HashMap _$_findViewCache;
    private Room mRoom;
    private MXSession mSession;
    @BindView(2131297178)
    public WebView mWebView;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0007*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lim/vector/activity/AbstractWidgetActivity$Companion;", "", "()V", "EXTRA_MATRIX_ID", "", "EXTRA_ROOM_ID", "LOG_TAG", "kotlin.jvm.PlatformType", "getIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "matrixId", "roomId", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: AbstractWidgetActivity.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Intent getIntent(Context context, String str, String str2) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(str, "matrixId");
            Intrinsics.checkParameterIsNotNull(str2, CryptoRoomEntityFields.ROOM_ID);
            Intent intent = new Intent(context, AbstractWidgetActivity.class);
            intent.putExtra("EXTRA_MATRIX_ID", str);
            intent.putExtra("EXTRA_ROOM_ID", str2);
            return intent;
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0004\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B+\u0012\u001c\u0010\u0003\u001a\u0018\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004j\b\u0012\u0004\u0012\u00020\u0006`\u0007\u0012\u0006\u0010\b\u001a\u00020\u0005¢\u0006\u0002\u0010\tJ\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0005H\u0002J\u0010\u0010\r\u001a\u00020\u000b2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0014\u0010\u0010\u001a\u00020\u000b2\n\u0010\u000e\u001a\u00060\u0011j\u0002`\u0012H\u0016J\u0017\u0010\u0013\u001a\u00020\u000b2\b\u0010\u0014\u001a\u0004\u0018\u00018\u0000H\u0016¢\u0006\u0002\u0010\u0015J\u0014\u0010\u0016\u001a\u00020\u000b2\n\u0010\u000e\u001a\u00060\u0011j\u0002`\u0012H\u0016R\u000e\u0010\b\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R$\u0010\u0003\u001a\u0018\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004j\b\u0012\u0004\u0012\u00020\u0006`\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lim/vector/activity/AbstractWidgetActivity$WidgetApiCallback;", "T", "Lorg/matrix/androidsdk/core/callback/ApiCallback;", "mEventData", "", "", "", "Lim/vector/types/JsonDict;", "mDescription", "(Lim/vector/activity/AbstractWidgetActivity;Ljava/util/Map;Ljava/lang/String;)V", "onError", "", "error", "onMatrixError", "e", "Lorg/matrix/androidsdk/core/model/MatrixError;", "onNetworkError", "Ljava/lang/Exception;", "Lkotlin/Exception;", "onSuccess", "info", "(Ljava/lang/Object;)V", "onUnexpectedError", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: AbstractWidgetActivity.kt */
    protected final class WidgetApiCallback<T> implements ApiCallback<T> {
        private final String mDescription;
        private final Map<String, Object> mEventData;
        final /* synthetic */ AbstractWidgetActivity this$0;

        public WidgetApiCallback(AbstractWidgetActivity abstractWidgetActivity, Map<String, ? extends Object> map, String str) {
            Intrinsics.checkParameterIsNotNull(map, "mEventData");
            Intrinsics.checkParameterIsNotNull(str, "mDescription");
            this.this$0 = abstractWidgetActivity;
            this.mEventData = map;
            this.mDescription = str;
        }

        public void onSuccess(T t) {
            String access$getLOG_TAG$cp = AbstractWidgetActivity.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(this.mDescription);
            sb.append(" succeeds");
            Log.d(access$getLOG_TAG$cp, sb.toString());
            this.this$0.sendSuccess(this.mEventData);
        }

        private final void onError(String str) {
            String access$getLOG_TAG$cp = AbstractWidgetActivity.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(this.mDescription);
            sb.append(" failed with error ");
            sb.append(str);
            Log.e(access$getLOG_TAG$cp, sb.toString());
            AbstractWidgetActivity abstractWidgetActivity = this.this$0;
            String string = abstractWidgetActivity.getString(R.string.widget_integration_failed_to_send_request);
            Intrinsics.checkExpressionValueIsNotNull(string, "getString(R.string.widge…n_failed_to_send_request)");
            abstractWidgetActivity.sendError(string, this.mEventData);
        }

        public void onNetworkError(Exception exc) {
            Intrinsics.checkParameterIsNotNull(exc, "e");
            String message = exc.getMessage();
            if (message == null) {
                Intrinsics.throwNpe();
            }
            onError(message);
        }

        public void onMatrixError(MatrixError matrixError) {
            Intrinsics.checkParameterIsNotNull(matrixError, "e");
            String message = matrixError.getMessage();
            Intrinsics.checkExpressionValueIsNotNull(message, "e.message");
            onError(message);
        }

        public void onUnexpectedError(Exception exc) {
            Intrinsics.checkParameterIsNotNull(exc, "e");
            String message = exc.getMessage();
            if (message == null) {
                Intrinsics.throwNpe();
            }
            onError(message);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0007\b\u0000¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007¨\u0006\u0007"}, d2 = {"Lim/vector/activity/AbstractWidgetActivity$WidgetWebAppInterface;", "", "(Lim/vector/activity/AbstractWidgetActivity;)V", "onWidgetEvent", "", "eventData", "", "vector_appAgentWithoutvoipWithpinningMatrixorg"}, k = 1, mv = {1, 1, 13})
    /* compiled from: AbstractWidgetActivity.kt */
    private final class WidgetWebAppInterface {
        public WidgetWebAppInterface() {
        }

        @JavascriptInterface
        public final void onWidgetEvent(String str) {
            Intrinsics.checkParameterIsNotNull(str, "eventData");
            String access$getLOG_TAG$cp = AbstractWidgetActivity.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("BRIDGE onWidgetEvent : ");
            sb.append(str);
            Log.d(access$getLOG_TAG$cp, sb.toString());
            try {
                AbstractWidgetActivity.this.runOnUiThread(new AbstractWidgetActivity$WidgetWebAppInterface$onWidgetEvent$1(this, (Map) JsonUtils.getGson(false).fromJson(str, new AbstractWidgetActivity$WidgetWebAppInterface$onWidgetEvent$objectAsMap$1().getType())));
            } catch (Exception e) {
                String access$getLOG_TAG$cp2 = AbstractWidgetActivity.LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## onWidgetEvent() failed ");
                sb2.append(e.getMessage());
                Log.e(access$getLOG_TAG$cp2, sb2.toString(), e);
            }
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

    public abstract String buildInterfaceUrl(String str);

    public final WebView getMWebView() {
        WebView webView = this.mWebView;
        if (webView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mWebView");
        }
        return webView;
    }

    public final void setMWebView(WebView webView) {
        Intrinsics.checkParameterIsNotNull(webView, "<set-?>");
        this.mWebView = webView;
    }

    /* access modifiers changed from: protected */
    public final MXSession getMSession() {
        return this.mSession;
    }

    /* access modifiers changed from: protected */
    public final void setMSession(MXSession mXSession) {
        this.mSession = mXSession;
    }

    /* access modifiers changed from: protected */
    public final Room getMRoom() {
        return this.mRoom;
    }

    /* access modifiers changed from: protected */
    public final void setMRoom(Room room) {
        this.mRoom = room;
    }

    public void initUiAndData() {
        Context context = this;
        this.mSession = Matrix.getInstance(context).getSession(getIntent().getStringExtra("EXTRA_MATRIX_ID"));
        MXSession mXSession = this.mSession;
        if (mXSession != null) {
            if (mXSession == null) {
                Intrinsics.throwNpe();
            }
            if (mXSession.isAlive()) {
                initWebView();
                MXSession mXSession2 = this.mSession;
                if (mXSession2 == null) {
                    Intrinsics.throwNpe();
                }
                this.mRoom = mXSession2.getDataHandler().getRoom(getIntent().getStringExtra("EXTRA_ROOM_ID"));
                MXSession mXSession3 = this.mSession;
                if (mXSession3 == null) {
                    Intrinsics.throwNpe();
                }
                WidgetsManager.getScalarToken(context, mXSession3, new AbstractWidgetActivity$initUiAndData$1(this));
                return;
            }
        }
        Log.e(LOG_TAG, "## onCreate() : invalid session");
        finish();
    }

    public void onBackPressed() {
        WebView webView = this.mWebView;
        String str = "mWebView";
        if (webView == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        if (webView.canGoBack()) {
            WebView webView2 = this.mWebView;
            if (webView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            webView2.goBack();
            return;
        }
        super.onBackPressed();
    }

    private final void initWebView() {
        WebView webView = this.mWebView;
        String str = "mWebView";
        if (webView == null) {
            Intrinsics.throwUninitializedPropertyAccessException(str);
        }
        webView.addJavascriptInterface(new WidgetWebAppInterface(), "Android");
        webView.setWebChromeClient(new AbstractWidgetActivity$initWebView$$inlined$let$lambda$1(this));
        WebSettings settings = webView.getSettings();
        Intrinsics.checkExpressionValueIsNotNull(settings, "it");
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
        webView.setWebViewClient(new AbstractWidgetActivity$initWebView$$inlined$let$lambda$2(this));
        if (VERSION.SDK_INT >= 21) {
            CookieManager instance = CookieManager.getInstance();
            WebView webView2 = this.mWebView;
            if (webView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str);
            }
            instance.setAcceptThirdPartyCookies(webView2, true);
        }
    }

    /* access modifiers changed from: private */
    public final void launchUrl(String str) {
        String buildInterfaceUrl = buildInterfaceUrl(str);
        if (buildInterfaceUrl == null) {
            finish();
            return;
        }
        WebView webView = this.mWebView;
        if (webView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mWebView");
        }
        webView.loadUrl(buildInterfaceUrl);
    }

    /* access modifiers changed from: private */
    public final void onWidgetMessage(Map<String, ? extends Map<String, ? extends Object>> map) {
        String str = "getString(R.string.widge…n_failed_to_send_request)";
        String str2 = "## onWidgetMessage() : invalid JSData";
        if (map == null) {
            Log.e(LOG_TAG, str2);
            return;
        }
        Map map2 = (Map) map.get("event.data");
        if (map2 == null) {
            Log.e(LOG_TAG, str2);
            return;
        }
        try {
            if (!dealsWithWidgetRequest(map2)) {
                String string = getString(R.string.widget_integration_failed_to_send_request);
                Intrinsics.checkExpressionValueIsNotNull(string, str);
                sendError(string, map2);
            }
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onWidgetMessage() : failed ");
            sb.append(e.getMessage());
            Log.e(str3, sb.toString(), e);
            String string2 = getString(R.string.widget_integration_failed_to_send_request);
            Intrinsics.checkExpressionValueIsNotNull(string2, str);
            sendError(string2, map2);
        }
    }

    public boolean dealsWithWidgetRequest(Map<String, ? extends Object> map) {
        String str;
        String str2;
        Intrinsics.checkParameterIsNotNull(map, "eventData");
        String str3 = (String) map.get("action");
        if (str3 == null || str3.hashCode() != 1328652327 || !str3.equals("integration_manager_open")) {
            return false;
        }
        String str4 = null;
        Object obj = map.get("data");
        if ((obj instanceof Map ? obj : null) == null) {
            str = str4;
        } else if (obj != null) {
            Map map2 = (Map) obj;
            Object obj2 = map2.get("integType");
            if (!(obj2 instanceof String)) {
                obj2 = null;
            }
            String str5 = "null cannot be cast to non-null type kotlin.String";
            if (obj2 == null) {
                str2 = str4;
            } else if (obj2 != null) {
                str2 = (String) obj2;
            } else {
                throw new TypeCastException(str5);
            }
            Object obj3 = map2.get("integId");
            if (!(obj3 instanceof String)) {
                obj3 = null;
            }
            if (obj3 != null) {
                if (obj3 != null) {
                    str4 = (String) obj3;
                } else {
                    throw new TypeCastException(str5);
                }
            }
            if (str2 != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("type_");
                sb.append(str2);
                str = sb.toString();
            } else {
                str = str2;
            }
        } else {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.Map<*, *>");
        }
        openIntegrationManager(str4, str);
        return true;
    }

    private final void sendResponse(String str, Map<String, ? extends Object> map) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("sendResponseFromRiotAndroid('");
            sb.append(map.get("_id"));
            sb.append("' , ");
            sb.append(str);
            sb.append(");");
            String sb2 = sb.toString();
            String str2 = LOG_TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("BRIDGE sendResponse: ");
            sb3.append(sb2);
            Log.v(str2, sb3.toString());
            String str3 = "mWebView";
            if (VERSION.SDK_INT < 19) {
                WebView webView = this.mWebView;
                if (webView == null) {
                    Intrinsics.throwUninitializedPropertyAccessException(str3);
                }
                StringBuilder sb4 = new StringBuilder();
                sb4.append("javascript:");
                sb4.append(sb2);
                webView.loadUrl(sb4.toString());
                return;
            }
            WebView webView2 = this.mWebView;
            if (webView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException(str3);
            }
            webView2.evaluateJavascript(sb2, null);
        } catch (Exception e) {
            String str4 = LOG_TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("## sendResponse() failed ");
            sb5.append(e.getMessage());
            Log.e(str4, sb5.toString(), e);
        }
    }

    /* access modifiers changed from: protected */
    public final void sendBoolResponse(boolean z, Map<String, ? extends Object> map) {
        Intrinsics.checkParameterIsNotNull(map, "eventData");
        sendResponse(z ? "true" : "false", map);
    }

    /* access modifiers changed from: protected */
    public final void sendIntegerResponse(int i, Map<String, ? extends Object> map) {
        Intrinsics.checkParameterIsNotNull(map, "eventData");
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(i));
        sb.append("");
        sendResponse(sb.toString(), map);
    }

    /* access modifiers changed from: protected */
    public final void sendObjectResponse(Object obj, Map<String, ? extends Object> map) {
        Intrinsics.checkParameterIsNotNull(map, "eventData");
        String str = null;
        if (obj != null) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("JSON.parse('");
                sb.append(JsonUtils.getGson(false).toJson(obj));
                sb.append("')");
                str = sb.toString();
            } catch (Exception e) {
                String str2 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## sendObjectResponse() : toJson failed ");
                sb2.append(e.getMessage());
                Log.e(str2, sb2.toString(), e);
            }
        }
        if (str == null) {
            str = "null";
        }
        sendResponse(str, map);
    }

    /* access modifiers changed from: protected */
    public final void sendSuccess(Map<String, ? extends Object> map) {
        Intrinsics.checkParameterIsNotNull(map, "eventData");
        HashMap hashMap = new HashMap();
        hashMap.put("success", Boolean.valueOf(true));
        sendObjectResponse(hashMap, map);
    }

    /* access modifiers changed from: protected */
    public final void sendError(String str, Map<String, ? extends Object> map) {
        String str2 = "message";
        Intrinsics.checkParameterIsNotNull(str, str2);
        Intrinsics.checkParameterIsNotNull(map, "eventData");
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## sendError() : eventData ");
        sb.append(map);
        sb.append(" failed ");
        sb.append(str);
        Log.e(str3, sb.toString());
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        hashMap2.put(str2, str);
        hashMap.put("error", hashMap2);
        sendObjectResponse(hashMap, map);
    }

    /* access modifiers changed from: protected */
    public final void sendObjectAsJsonMap(Object obj, Map<String, ? extends Object> map) {
        Intrinsics.checkParameterIsNotNull(obj, "any");
        Intrinsics.checkParameterIsNotNull(map, "eventData");
        sendObjectResponse(JsonUtilKt.toJsonMap(obj), map);
    }

    /* access modifiers changed from: protected */
    public final void openIntegrationManager(String str, String str2) {
        im.vector.activity.IntegrationManagerActivity.Companion companion = IntegrationManagerActivity.Companion;
        Context context = this;
        MXSession mXSession = this.mSession;
        if (mXSession == null) {
            Intrinsics.throwNpe();
        }
        String myUserId = mXSession.getMyUserId();
        Intrinsics.checkExpressionValueIsNotNull(myUserId, "mSession!!.myUserId");
        Room room = this.mRoom;
        if (room == null) {
            Intrinsics.throwNpe();
        }
        String roomId = room.getRoomId();
        Intrinsics.checkExpressionValueIsNotNull(roomId, "mRoom!!.roomId");
        startActivityForResult(companion.getIntent(context, myUserId, roomId, str, str2), RequestCodesKt.INTEGRATION_MANAGER_ACTIVITY_REQUEST_CODE);
    }
}
