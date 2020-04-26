package im.vector.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AlertDialog.Builder;
import com.google.gson.reflect.TypeToken;
import fr.gouv.tchap.a.R;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;

public class AccountCreationActivity extends VectorAppCompatActivity {
    public static final String EXTRA_HOME_SERVER_ID = "AccountCreationActivity.EXTRA_HOME_SERVER_ID";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = AccountCreationActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public String mHomeServerUrl;

    public int getLayoutRes() {
        return R.layout.activity_account_creation;
    }

    public int getTitleRes() {
        return R.string.create_account;
    }

    public void onLowMemory() {
        super.onLowMemory();
        CommonActivityUtils.onLowMemory(this);
    }

    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
        CommonActivityUtils.onTrimMemory(this, i);
    }

    public void initUiAndData() {
        WebView webView = (WebView) findViewById(R.id.account_creation_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(false);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(false);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setAllowContentAccess(false);
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
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mHomeServerUrl);
        sb2.append("_matrix/static/client/register/");
        webView.loadUrl(sb2.toString());
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
                new Builder(AccountCreationActivity.this).setMessage((int) R.string.ssl_could_not_verify).setPositiveButton((int) R.string.ssl_trust, (OnClickListener) new OnClickListener() {
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
                AccountCreationActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        AccountCreationActivity.this.finish();
                    }
                });
            }

            public void onPageFinished(WebView webView, String str) {
                if (str.startsWith("http")) {
                    webView.loadUrl("javascript:window.matrixRegistration.sendObjectMessage = function(parameters) { var iframe = document.createElement('iframe');  iframe.setAttribute('src', 'js:' + JSON.stringify(parameters));  document.documentElement.appendChild(iframe); iframe.parentNode.removeChild(iframe); iframe = null; };");
                    webView.loadUrl("javascript:window.matrixRegistration.onRegistered = function(homeserverUrl, userId, accessToken) { matrixRegistration.sendObjectMessage({ 'action': 'onRegistered', 'homeServer': homeserverUrl,'userId': userId,  'accessToken': accessToken  }); };");
                }
            }

            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                Map map;
                if (str != null && str.startsWith("js:")) {
                    try {
                        map = (Map) JsonUtils.getBasicGson().fromJson(URLDecoder.decode(str.substring(3), "UTF-8"), new TypeToken<HashMap<String, String>>() {
                        }.getType());
                    } catch (Exception e) {
                        String access$000 = AccountCreationActivity.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## shouldOverrideUrlLoading() : fromJson failed ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                        map = null;
                    }
                    if (map != null) {
                        String str2 = "homeServer";
                        if (map.containsKey(str2)) {
                            String str3 = "userId";
                            if (map.containsKey(str3)) {
                                String str4 = "accessToken";
                                if (map.containsKey(str4)) {
                                    String str5 = "action";
                                    if (map.containsKey(str5)) {
                                        final String str6 = (String) map.get(str3);
                                        final String str7 = (String) map.get(str4);
                                        final String str8 = (String) map.get(str2);
                                        String str9 = (String) map.get(str5);
                                        if (AccountCreationActivity.this.mHomeServerUrl.endsWith("/")) {
                                            AccountCreationActivity accountCreationActivity = AccountCreationActivity.this;
                                            accountCreationActivity.mHomeServerUrl = accountCreationActivity.mHomeServerUrl.substring(0, AccountCreationActivity.this.mHomeServerUrl.length() - 1);
                                        }
                                        if (str9.equals("onRegistered")) {
                                            AccountCreationActivity.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Intent intent = new Intent();
                                                    intent.putExtra("homeServerUrl", AccountCreationActivity.this.mHomeServerUrl);
                                                    intent.putExtra("homeServer", str8);
                                                    intent.putExtra("userId", str6);
                                                    intent.putExtra("accessToken", str7);
                                                    AccountCreationActivity.this.setResult(-1, intent);
                                                    AccountCreationActivity.this.finish();
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        }
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
}
