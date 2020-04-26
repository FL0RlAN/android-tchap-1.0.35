package im.vector.activity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.activity.TchapLoginActivity;
import fr.gouv.tchap.sdk.rest.client.TchapValidityRestClient;
import fr.gouv.tchap.util.HomeServerConnectionConfigFactoryKt;
import im.vector.LoginHandler;
import im.vector.Matrix;
import im.vector.receiver.VectorUniversalLinkReceiver;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.model.HttpException;
import org.matrix.androidsdk.core.model.MatrixError;

public class VectorUniversalLinkActivity extends VectorAppCompatActivity {
    private static final String ACCOUNT_VALIDITY_RENEW_PATH_SUFFIX = "/account_validity/renew";
    private static final String ACCOUNT_VALIDITY_RENEW_TOKEN = "token";
    private static final String EMAIL_VALIDATION_PATH_SUFFIX = "/validate/email/submitToken";
    public static final String EXTRA_EMAIL_VALIDATION_PARAMS = "EXTRA_EMAIL_VALIDATION_PARAMS";
    public static final String KEY_MAIL_VALIDATION_CLIENT_SECRET = "client_secret";
    public static final String KEY_MAIL_VALIDATION_HOME_SERVER_URL = "hs_url";
    public static final String KEY_MAIL_VALIDATION_IDENTITY_SERVER_SESSION_ID = "sid";
    public static final String KEY_MAIL_VALIDATION_IDENTITY_SERVER_URL = "is_url";
    public static final String KEY_MAIL_VALIDATION_NEXT_LINK = "nextLink";
    public static final String KEY_MAIL_VALIDATION_SESSION_ID = "session_id";
    public static final String KEY_MAIL_VALIDATION_TOKEN = "token";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = VectorUniversalLinkActivity.class.getSimpleName();
    private final VectorUniversalLinkReceiver mUniversalLinkReceiver = new VectorUniversalLinkReceiver();

    public int getLayoutRes() {
        return R.layout.activity_vector_universal_link_activity;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        String str = VectorUniversalLinkReceiver.BROADCAST_ACTION_UNIVERSAL_LINK;
        IntentFilter intentFilter = new IntentFilter(str);
        intentFilter.addDataScheme("http");
        intentFilter.addDataScheme("https");
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mUniversalLinkReceiver, intentFilter);
        String str2 = null;
        try {
            Uri data = getIntent().getData();
            String path = data.getPath();
            if (path.endsWith(EMAIL_VALIDATION_PATH_SUFFIX)) {
                final Map parseMailRegistrationLink = parseMailRegistrationLink(data);
                MXSession defaultSession = Matrix.getInstance(this).getDefaultSession();
                if (!parseMailRegistrationLink.containsKey(KEY_MAIL_VALIDATION_NEXT_LINK)) {
                    if (defaultSession != null) {
                        emailBinding(data, parseMailRegistrationLink);
                    }
                }
                if (defaultSession == null) {
                    Intent intent = new Intent(this, TchapLoginActivity.class);
                    intent.putExtra(EXTRA_EMAIL_VALIDATION_PARAMS, (HashMap) parseMailRegistrationLink);
                    intent.setFlags(872415232);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d(LOG_TAG, "## logout the current sessions, before finalizing an account creation based on an email validation");
                    CommonActivityUtils.logout((Context) this, Matrix.getMXSessions(this), true, (ApiCallback<Void>) new SimpleApiCallback<Void>() {
                        public void onSuccess(Void voidR) {
                            Log.d(VectorUniversalLinkActivity.LOG_TAG, "## logout succeeded");
                            Intent intent = new Intent(VectorUniversalLinkActivity.this, TchapLoginActivity.class);
                            intent.putExtra(VectorUniversalLinkActivity.EXTRA_EMAIL_VALIDATION_PARAMS, (HashMap) parseMailRegistrationLink);
                            intent.setFlags(872415232);
                            VectorUniversalLinkActivity.this.startActivity(intent);
                            VectorUniversalLinkActivity.this.finish();
                        }
                    });
                }
            } else if (path.endsWith(ACCOUNT_VALIDITY_RENEW_PATH_SUFFIX)) {
                renewAccountValidity(data);
            } else {
                str2 = str;
            }
        } catch (Exception e) {
            String str3 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## Exception - Msg=");
            sb.append(e.getMessage());
            Log.e(str3, sb.toString(), e);
        }
        if (str2 != null) {
            Intent intent2 = new Intent(this, VectorUniversalLinkReceiver.class);
            intent2.setAction(str2);
            intent2.setData(getIntent().getData());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mUniversalLinkReceiver);
    }

    private void emailBinding(Uri uri, Map<String, String> map) {
        Log.d(LOG_TAG, "## emailBinding()");
        StringBuilder sb = new StringBuilder();
        sb.append(uri.getScheme());
        sb.append("://");
        sb.append(uri.getHost());
        String sb2 = sb.toString();
        new LoginHandler().submitEmailTokenValidation(getApplicationContext(), HomeServerConnectionConfigFactoryKt.createHomeServerConnectionConfig(sb2, sb2), (String) map.get("token"), (String) map.get(KEY_MAIL_VALIDATION_CLIENT_SECRET), (String) map.get(KEY_MAIL_VALIDATION_IDENTITY_SERVER_SESSION_ID), new ApiCallback<Boolean>() {
            private void errorHandler(final String str) {
                VectorUniversalLinkActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(VectorUniversalLinkActivity.this.getApplicationContext(), str, 1).show();
                        VectorUniversalLinkActivity.this.bringAppToForeground();
                    }
                });
            }

            public void onSuccess(Boolean bool) {
                VectorUniversalLinkActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(VectorUniversalLinkActivity.LOG_TAG, "## emailBinding(): succeeds.");
                        VectorUniversalLinkActivity.this.bringAppToForeground();
                    }
                });
            }

            public void onNetworkError(Exception exc) {
                String access$000 = VectorUniversalLinkActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## emailBinding(): onNetworkError() Msg=");
                sb.append(exc.getLocalizedMessage());
                Log.d(access$000, sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append(VectorUniversalLinkActivity.this.getString(R.string.login_error_unable_register));
                sb2.append(" : ");
                sb2.append(exc.getLocalizedMessage());
                errorHandler(sb2.toString());
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$000 = VectorUniversalLinkActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## emailBinding(): onMatrixError() Msg=");
                sb.append(matrixError.getLocalizedMessage());
                Log.d(access$000, sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append(VectorUniversalLinkActivity.this.getString(R.string.login_error_unable_register));
                sb2.append(" : ");
                sb2.append(matrixError.getLocalizedMessage());
                errorHandler(sb2.toString());
            }

            public void onUnexpectedError(Exception exc) {
                String access$000 = VectorUniversalLinkActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## emailBinding(): onUnexpectedError() Msg=");
                sb.append(exc.getLocalizedMessage());
                Log.d(access$000, sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append(VectorUniversalLinkActivity.this.getString(R.string.login_error_unable_register));
                sb2.append(" : ");
                sb2.append(exc.getLocalizedMessage());
                errorHandler(sb2.toString());
            }
        });
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
        r5 = java.net.URLDecoder.decode(r5.getQueryParameter(r2), "UTF-8");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x004e, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x004f, code lost:
        r1 = LOG_TAG;
        r2 = new java.lang.StringBuilder();
        r2.append("## renewAccountValidity(): Exception - parse query params Msg=");
        r2.append(r5.getLocalizedMessage());
        org.matrix.androidsdk.core.Log.e(r1, r2.toString(), r5);
     */
    private void renewAccountValidity(Uri uri) {
        Log.i(LOG_TAG, "## renewAccountValidity()");
        StringBuilder sb = new StringBuilder();
        sb.append(uri.getScheme());
        sb.append("://");
        sb.append(uri.getHost());
        String sb2 = sb.toString();
        HomeServerConnectionConfig createHomeServerConnectionConfig = HomeServerConnectionConfigFactoryKt.createHomeServerConnectionConfig(sb2, sb2);
        Iterator it = uri.getQueryParameterNames().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String str = (String) it.next();
            if ("token".equals(str)) {
                break;
            }
        }
        String str2 = null;
        new TchapValidityRestClient(createHomeServerConnectionConfig).renewAccountValidity(str2, new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                Log.i(VectorUniversalLinkActivity.LOG_TAG, "## renewAccountValidity() succeeded");
                onResult(VectorUniversalLinkActivity.this.getString(R.string.tchap_renew_account_validity_success_msg), Boolean.valueOf(true));
            }

            public void onNetworkError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                onError(matrixError.getLocalizedMessage());
            }

            public void onUnexpectedError(Exception exc) {
                String localizedMessage = exc.getLocalizedMessage();
                if ((exc instanceof HttpException) && ((HttpException) exc).getHttpError().getHttpCode() == 404) {
                    localizedMessage = VectorUniversalLinkActivity.this.getString(R.string.tchap_renew_account_validity_invalid_token_msg);
                }
                onError(localizedMessage);
            }

            private void onError(String str) {
                String access$000 = VectorUniversalLinkActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## renewAccountValidity() failed: ");
                sb.append(str);
                Log.e(access$000, sb.toString());
                boolean isEmpty = TextUtils.isEmpty(str);
                Boolean valueOf = Boolean.valueOf(false);
                if (isEmpty) {
                    onResult(VectorUniversalLinkActivity.this.getString(R.string.tchap_error_message_default), valueOf);
                } else {
                    onResult(str, valueOf);
                }
            }

            private void onResult(String str, final Boolean bool) {
                new Builder(VectorUniversalLinkActivity.this).setMessage((CharSequence) str).setPositiveButton((int) R.string.ok, (OnClickListener) null).setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (bool.booleanValue()) {
                            Matrix.getInstance(VectorUniversalLinkActivity.this).onRenewAccountValidity();
                        }
                        VectorUniversalLinkActivity.this.bringAppToForeground();
                    }
                }).show();
            }
        });
    }

    /* access modifiers changed from: private */
    public void bringAppToForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService("activity");
        List runningTasks = activityManager.getRunningTasks(100);
        if (!runningTasks.isEmpty()) {
            int size = runningTasks.size();
            for (int i = 0; i < size; i++) {
                RunningTaskInfo runningTaskInfo = (RunningTaskInfo) runningTasks.get(i);
                if (runningTaskInfo.topActivity.getPackageName().equals(getApplicationContext().getPackageName())) {
                    Log.d(LOG_TAG, "Bring the app in foreground.");
                    activityManager.moveTaskToFront(runningTaskInfo.id, 0);
                }
            }
        }
        finish();
    }

    private Map<String, String> parseMailRegistrationLink(Uri uri) {
        String str = KEY_MAIL_VALIDATION_SESSION_ID;
        String str2 = KEY_MAIL_VALIDATION_IDENTITY_SERVER_URL;
        String str3 = KEY_MAIL_VALIDATION_HOME_SERVER_URL;
        HashMap hashMap = new HashMap();
        if (uri != null) {
            try {
                if (!TextUtils.isEmpty(uri.getPath())) {
                    String host = uri.getHost();
                    String str4 = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## parseMailRegistrationLink(): host=");
                    sb.append(host);
                    Log.i(str4, sb.toString());
                    String fragment = uri.getFragment();
                    String lastPathSegment = uri.getLastPathSegment();
                    String schemeSpecificPart = uri.getSchemeSpecificPart();
                    String str5 = LOG_TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("## parseMailRegistrationLink(): uriFragment=");
                    sb2.append(fragment);
                    Log.i(str5, sb2.toString());
                    String str6 = LOG_TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("## parseMailRegistrationLink(): getLastPathSegment()=");
                    sb3.append(lastPathSegment);
                    Log.i(str6, sb3.toString());
                    String str7 = LOG_TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("## parseMailRegistrationLink(): getSchemeSpecificPart()=");
                    sb4.append(schemeSpecificPart);
                    Log.i(str7, sb4.toString());
                    Uri uri2 = null;
                    for (String str8 : uri.getQueryParameterNames()) {
                        String queryParameter = uri.getQueryParameter(str8);
                        if (KEY_MAIL_VALIDATION_NEXT_LINK.equals(str8)) {
                            uri2 = Uri.parse(queryParameter.replace("#/", ""));
                        }
                        try {
                            queryParameter = URLDecoder.decode(queryParameter, "UTF-8");
                        } catch (Exception e) {
                            String str9 = LOG_TAG;
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("## parseMailRegistrationLink(): Exception - parse query params Msg=");
                            sb5.append(e.getLocalizedMessage());
                            Log.e(str9, sb5.toString(), e);
                        }
                        hashMap.put(str8, queryParameter);
                    }
                    if (uri2 != null) {
                        hashMap.put(str3, uri2.getQueryParameter(str3));
                        hashMap.put(str2, uri2.getQueryParameter(str2));
                        hashMap.put(str, uri2.getQueryParameter(str));
                    }
                    String str10 = LOG_TAG;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("## parseMailRegistrationLink(): map query=");
                    sb6.append(hashMap.toString());
                    Log.i(str10, sb6.toString());
                    return hashMap;
                }
            } catch (Exception e2) {
                String str11 = LOG_TAG;
                StringBuilder sb7 = new StringBuilder();
                sb7.append("## parseMailRegistrationLink(): Exception - Msg=");
                sb7.append(e2.getLocalizedMessage());
                Log.e(str11, sb7.toString(), e2);
                return null;
            }
        }
        Log.e(LOG_TAG, "## parseMailRegistrationLink : null");
        return hashMap;
    }
}
