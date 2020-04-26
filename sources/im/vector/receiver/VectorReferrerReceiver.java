package im.vector.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import java.net.URLDecoder;
import org.matrix.androidsdk.core.Log;

public class VectorReferrerReceiver extends BroadcastReceiver {
    private static final String INSTALL_REFERRER_ACTION = "com.android.vending.INSTALL_REFERRER";
    private static final String KEY_HS = "hs";
    private static final String KEY_IS = "is";
    private static final String KEY_REFERRER = "referrer";
    private static final String LOG_TAG = VectorReferrerReceiver.class.getSimpleName();
    private static final String UTM_CONTENT = "utm_content";
    private static final String UTM_SOURCE = "utm_source";

    public void onReceive(Context context, Intent intent) {
        String str;
        String str2 = "utf-8";
        String str3 = "https://dummy?";
        String str4 = "";
        if (intent == null) {
            Log.e(LOG_TAG, "No intent");
            return;
        }
        String str5 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onReceive() : ");
        sb.append(intent.getAction());
        Log.d(str5, sb.toString());
        if (TextUtils.equals(intent.getAction(), INSTALL_REFERRER_ACTION)) {
            Bundle extras = intent.getExtras();
            if (extras == null) {
                Log.e(LOG_TAG, "No extra");
                return;
            }
            try {
                String str6 = (String) extras.get(KEY_REFERRER);
                String str7 = LOG_TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("## onReceive() : referrer ");
                sb2.append(str6);
                Log.d(str7, sb2.toString());
                if (!TextUtils.isEmpty(str6)) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str3);
                    sb3.append(URLDecoder.decode(str6, str2));
                    Uri parse = Uri.parse(sb3.toString());
                    String queryParameter = parse.getQueryParameter(UTM_SOURCE);
                    String queryParameter2 = parse.getQueryParameter(UTM_CONTENT);
                    String str8 = LOG_TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("## onReceive() : utm_source ");
                    sb4.append(queryParameter);
                    sb4.append(" -- utm_content ");
                    sb4.append(queryParameter2);
                    Log.d(str8, sb4.toString());
                    if (queryParameter2 != null) {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(str3);
                        sb5.append(URLDecoder.decode(queryParameter2, str2));
                        Uri parse2 = Uri.parse(sb5.toString());
                        str = parse2.getQueryParameter(KEY_HS);
                        try {
                            str4 = parse2.getQueryParameter(KEY_IS);
                        } catch (Throwable th) {
                            th = th;
                            String str9 = LOG_TAG;
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("## onReceive() : failed ");
                            sb6.append(th.getMessage());
                            Log.e(str9, sb6.toString(), th);
                            String str10 = LOG_TAG;
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append("## onReceive() : HS ");
                            sb7.append(str);
                            Log.d(str10, sb7.toString());
                            String str11 = LOG_TAG;
                            StringBuilder sb8 = new StringBuilder();
                            sb8.append("## onReceive() : IS ");
                            sb8.append(str4);
                            Log.d(str11, sb8.toString());
                        }
                        String str102 = LOG_TAG;
                        StringBuilder sb72 = new StringBuilder();
                        sb72.append("## onReceive() : HS ");
                        sb72.append(str);
                        Log.d(str102, sb72.toString());
                        String str112 = LOG_TAG;
                        StringBuilder sb82 = new StringBuilder();
                        sb82.append("## onReceive() : IS ");
                        sb82.append(str4);
                        Log.d(str112, sb82.toString());
                    }
                }
                str = str4;
            } catch (Throwable th2) {
                th = th2;
                str = str4;
                String str92 = LOG_TAG;
                StringBuilder sb62 = new StringBuilder();
                sb62.append("## onReceive() : failed ");
                sb62.append(th.getMessage());
                Log.e(str92, sb62.toString(), th);
                String str1022 = LOG_TAG;
                StringBuilder sb722 = new StringBuilder();
                sb722.append("## onReceive() : HS ");
                sb722.append(str);
                Log.d(str1022, sb722.toString());
                String str1122 = LOG_TAG;
                StringBuilder sb822 = new StringBuilder();
                sb822.append("## onReceive() : IS ");
                sb822.append(str4);
                Log.d(str1122, sb822.toString());
            }
            String str10222 = LOG_TAG;
            StringBuilder sb7222 = new StringBuilder();
            sb7222.append("## onReceive() : HS ");
            sb7222.append(str);
            Log.d(str10222, sb7222.toString());
            String str11222 = LOG_TAG;
            StringBuilder sb8222 = new StringBuilder();
            sb8222.append("## onReceive() : IS ");
            sb8222.append(str4);
            Log.d(str11222, sb8222.toString());
        }
    }
}
