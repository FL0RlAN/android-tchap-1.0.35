package im.vector.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import com.google.gson.reflect.TypeToken;
import fr.gouv.tchap.a.R;
import java.net.URLDecoder;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;

public class AccountCreationCaptchaActivity extends VectorAppCompatActivity {
    public static final String EXTRA_HOME_SERVER_URL = "AccountCreationCaptchaActivity.EXTRA_HOME_SERVER_URL";
    public static final String EXTRA_SITE_KEY = "AccountCreationCaptchaActivity.EXTRA_SITE_KEY";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = AccountCreationCaptchaActivity.class.getSimpleName();
    private static final String mRecaptchaHTMLString = "<html>  <head>  <script type=\"text/javascript\">  var verifyCallback = function(response) {  var iframe = document.createElement('iframe');  iframe.setAttribute('src', 'js:' + JSON.stringify({'action': 'verifyCallback', 'response': response}));  document.documentElement.appendChild(iframe);  iframe.parentNode.removeChild(iframe);  iframe = null;  };  var onloadCallback = function() {  grecaptcha.render('recaptcha_widget', {  'sitekey' : '%s',  'callback': verifyCallback  });  };  </script>  </head>  <body>  <div id=\"recaptcha_widget\"></div>  <script src=\"https://www.google.com/recaptcha/api.js?onload=onloadCallback&render=explicit\" async defer>  </script>  </body>  </html> ";

    public int getLayoutRes() {
        return R.layout.activity_vector_registration_captcha;
    }

    public int getTitleRes() {
        return R.string.create_account;
    }

    public void initUiAndData() {
        WebView webView = (WebView) findViewById(R.id.account_creation_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(false);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setAllowContentAccess(false);
        final View findViewById = findViewById(R.id.account_creation_webview_loading);
        Intent intent = getIntent();
        String str = EXTRA_HOME_SERVER_URL;
        String stringExtra = intent.hasExtra(str) ? intent.getStringExtra(str) : "https://matrix.org/";
        String str2 = "/";
        if (!stringExtra.endsWith(str2)) {
            StringBuilder sb = new StringBuilder();
            sb.append(stringExtra);
            sb.append(str2);
            stringExtra = sb.toString();
        }
        webView.loadDataWithBaseURL(stringExtra, new Formatter().format(mRecaptchaHTMLString, new Object[]{intent.getStringExtra(EXTRA_SITE_KEY)}).toString(), "text/html", "utf-8", null);
        webView.requestLayout();
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                findViewById.setVisibility(8);
            }

            public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
                String access$000 = AccountCreationCaptchaActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## onReceivedSslError() : ");
                sb.append(sslError.getCertificate());
                Log.e(access$000, sb.toString());
                new Builder(AccountCreationCaptchaActivity.this).setMessage((int) R.string.ssl_could_not_verify).setPositiveButton((int) R.string.ssl_trust, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(AccountCreationCaptchaActivity.LOG_TAG, "## onReceivedSslError() : the user trusted");
                        sslErrorHandler.proceed();
                    }
                }).setNegativeButton((int) R.string.ssl_do_not_trust, (OnClickListener) new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(AccountCreationCaptchaActivity.LOG_TAG, "## onReceivedSslError() : the user did not trust");
                        sslErrorHandler.cancel();
                    }
                }).setOnKeyListener(new OnKeyListener() {
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        if (keyEvent.getAction() != 1 || i != 4) {
                            return false;
                        }
                        sslErrorHandler.cancel();
                        Log.d(AccountCreationCaptchaActivity.LOG_TAG, "## onReceivedSslError() : the user dismisses the trust dialog.");
                        dialogInterface.dismiss();
                        return true;
                    }
                }).show();
            }

            private void onError(String str) {
                Log.e(AccountCreationCaptchaActivity.LOG_TAG, "## onError() : errorMessage");
                Toast.makeText(AccountCreationCaptchaActivity.this, str, 1).show();
                AccountCreationCaptchaActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        AccountCreationCaptchaActivity.this.finish();
                    }
                });
            }

            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
                if (VERSION.SDK_INT >= 23) {
                    onError(webResourceResponse.getReasonPhrase());
                } else {
                    onError(webResourceResponse.toString());
                }
            }

            public void onReceivedError(WebView webView, int i, String str, String str2) {
                super.onReceivedError(webView, i, str, str2);
                onError(str);
            }

            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                Map map;
                if (str != null && str.startsWith("js:")) {
                    try {
                        map = (Map) JsonUtils.getBasicGson().fromJson(URLDecoder.decode(str.substring(3), "UTF-8"), new TypeToken<HashMap<String, String>>() {
                        }.getType());
                    } catch (Exception e) {
                        String access$000 = AccountCreationCaptchaActivity.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## shouldOverrideUrlLoading() : fromJson failed ");
                        sb.append(e.getMessage());
                        Log.e(access$000, sb.toString(), e);
                        map = null;
                    }
                    if (map != null) {
                        String str2 = "action";
                        if (map.containsKey(str2)) {
                            String str3 = "response";
                            if (map.containsKey(str3) && TextUtils.equals((String) map.get(str2), "verifyCallback")) {
                                Intent intent = new Intent();
                                intent.putExtra(str3, (String) map.get(str3));
                                AccountCreationCaptchaActivity.this.setResult(-1, intent);
                                AccountCreationCaptchaActivity.this.finish();
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

    public void onLowMemory() {
        super.onLowMemory();
        CommonActivityUtils.onLowMemory(this);
    }

    public void onTrimMemory(int i) {
        super.onTrimMemory(i);
        CommonActivityUtils.onTrimMemory(this, i);
    }
}
