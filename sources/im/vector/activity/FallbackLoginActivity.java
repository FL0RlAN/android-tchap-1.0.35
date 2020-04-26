package im.vector.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AlertDialog.Builder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import fr.gouv.tchap.a.R;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;

public class FallbackLoginActivity extends VectorAppCompatActivity {
    public static final String EXTRA_HOME_SERVER_ID = "FallbackLoginActivity.EXTRA_HOME_SERVER_ID";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = FallbackLoginActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public String mHomeServerUrl = null;
    private WebView mWebView = null;

    public int getLayoutRes() {
        return R.layout.activity_login_fallback;
    }

    public int getTitleRes() {
        return R.string.login;
    }

    public void initUiAndData() {
        this.mWebView = (WebView) findViewById(R.id.account_creation_webview);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.getSettings().setAllowFileAccessFromFileURLs(false);
        this.mWebView.getSettings().setAllowUniversalAccessFromFileURLs(false);
        this.mWebView.getSettings().setAllowFileAccess(false);
        this.mWebView.getSettings().setAllowContentAccess(false);
        Intent intent = getIntent();
        this.mHomeServerUrl = "https://matrix.org/";
        String str = EXTRA_HOME_SERVER_ID;
        if (intent.hasExtra(str)) {
            this.mHomeServerUrl = intent.getStringExtra(str);
        }
        String str2 = "/";
        if (!this.mHomeServerUrl.endsWith(str2)) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mHomeServerUrl);
            sb.append(str2);
            this.mHomeServerUrl = sb.toString();
        }
        CookieManager instance = CookieManager.getInstance();
        if (instance == null || instance.hasCookies()) {
            String str3 = " cookieManager.removeAllCookie() fails ";
            if (VERSION.SDK_INT < 21) {
                try {
                    instance.removeAllCookie();
                } catch (Exception e) {
                    String str4 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str3);
                    sb2.append(e.getLocalizedMessage());
                    Log.e(str4, sb2.toString(), e);
                }
                launchWebView();
                return;
            }
            try {
                instance.removeAllCookies(new ValueCallback<Boolean>() {
                    public void onReceiveValue(Boolean bool) {
                        FallbackLoginActivity.this.launchWebView();
                    }
                });
            } catch (Exception e2) {
                String str5 = LOG_TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append(e2.getLocalizedMessage());
                Log.e(str5, sb3.toString(), e2);
                launchWebView();
            }
        } else {
            launchWebView();
        }
    }

    /* access modifiers changed from: private */
    public void launchWebView() {
        WebView webView = this.mWebView;
        StringBuilder sb = new StringBuilder();
        sb.append(this.mHomeServerUrl);
        sb.append("_matrix/static/client/login/");
        webView.loadUrl(sb.toString());
        this.mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
                new Builder(FallbackLoginActivity.this).setMessage((int) R.string.ssl_could_not_verify).setPositiveButton((int) R.string.ssl_trust, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.proceed();
                    }
                }).setNegativeButton((int) R.string.ssl_do_not_trust, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.cancel();
                    }
                }).setOnKeyListener(new OnKeyListener() {
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        if (keyEvent.getAction() != 1 || i != 4) {
                            return false;
                        }
                        sslErrorHandler.cancel();
                        dialogInterface.dismiss();
                        return true;
                    }
                }).show();
            }

            public void onReceivedError(WebView webView, int i, String str, String str2) {
                super.onReceivedError(webView, i, str, str2);
                FallbackLoginActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        FallbackLoginActivity.this.finish();
                    }
                });
            }

            public void onPageFinished(WebView webView, String str) {
                if (str.startsWith("http")) {
                    webView.loadUrl("javascript:window.matrixLogin.sendObjectMessage = function(parameters) { var iframe = document.createElement('iframe');  iframe.setAttribute('src', 'js:' + JSON.stringify(parameters));  document.documentElement.appendChild(iframe); iframe.parentNode.removeChild(iframe); iframe = null; };");
                    webView.loadUrl("javascript:window.matrixLogin.onLogin = function(homeserverUrl, userId, accessToken) { matrixLogin.sendObjectMessage({ 'action': 'onLogin', 'homeServer': homeserverUrl,'userId': userId,  'accessToken': accessToken  }); };");
                }
            }

            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                Map map;
                if (str == null || !str.startsWith("js:")) {
                    return false;
                }
                try {
                    map = (Map) JsonUtils.getBasicGson().fromJson(URLDecoder.decode(str.substring(3), "UTF-8"), new TypeToken<HashMap<String, Object>>() {
                    }.getType());
                } catch (Exception e) {
                    String access$100 = FallbackLoginActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## shouldOverrideUrlLoading() : fromJson failed ");
                    sb.append(e.getMessage());
                    Log.e(access$100, sb.toString(), e);
                    map = null;
                }
                if (map != null) {
                    try {
                        String str2 = (String) map.get("action");
                        LinkedTreeMap linkedTreeMap = (LinkedTreeMap) map.get("homeServer");
                        if (TextUtils.equals("onLogin", str2) && linkedTreeMap != null) {
                            final String str3 = (String) linkedTreeMap.get("user_id");
                            final String str4 = (String) linkedTreeMap.get("access_token");
                            final String str5 = (String) linkedTreeMap.get("home_server");
                            if (FallbackLoginActivity.this.mHomeServerUrl.endsWith("/")) {
                                FallbackLoginActivity.this.mHomeServerUrl = FallbackLoginActivity.this.mHomeServerUrl.substring(0, FallbackLoginActivity.this.mHomeServerUrl.length() - 1);
                            }
                            if (!(str5 == null || str3 == null || str4 == null)) {
                                FallbackLoginActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent();
                                        intent.putExtra("homeServerUrl", FallbackLoginActivity.this.mHomeServerUrl);
                                        intent.putExtra("homeServer", str5);
                                        intent.putExtra("userId", str3);
                                        intent.putExtra("accessToken", str4);
                                        FallbackLoginActivity.this.setResult(-1, intent);
                                        FallbackLoginActivity.this.finish();
                                    }
                                });
                            }
                        }
                    } catch (Exception e2) {
                        String access$1002 = FallbackLoginActivity.LOG_TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("## shouldOverrideUrlLoading() : failed ");
                        sb2.append(e2.getMessage());
                        Log.e(access$1002, sb2.toString(), e2);
                    }
                }
                return true;
            }
        });
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 82) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    public void onLowMemory() {
        super.onLowMemory();
        CommonActivityUtils.onLowMemory(this);
    }

    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
        CommonActivityUtils.onTrimMemory(this, i);
    }
}
