package org.piwik.sdk.extra;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.Collections;
import java.util.List;
import org.piwik.sdk.Piwik;
import timber.log.Timber;

public class InstallReferrerReceiver extends BroadcastReceiver {
    static final String ARG_KEY_GPLAY_REFERRER = "referrer";
    private static final String LOGGER_TAG = "PIWIK:InstallReferrerReceiver";
    static final String PREF_KEY_INSTALL_REFERRER_EXTRAS = "referrer.extras";
    static final String REFERRER_SOURCE_GPLAY = "com.android.vending.INSTALL_REFERRER";
    static final List<String> RESPONSIBILITIES = Collections.singletonList(REFERRER_SOURCE_GPLAY);

    public void onReceive(Context context, Intent intent) {
        String str = LOGGER_TAG;
        Timber.tag(str).d(intent.toString(), new Object[0]);
        if (intent.getAction() == null || !RESPONSIBILITIES.contains(intent.getAction())) {
            Timber.tag(str).w("Got called outside our responsibilities: %s", intent.getAction());
            return;
        }
        String str2 = "forwarded";
        if (intent.getBooleanExtra(str2, false)) {
            Timber.tag(str).d("Dropping forwarded intent", new Object[0]);
            return;
        }
        SharedPreferences piwikPreferences = Piwik.getInstance(context.getApplicationContext()).getPiwikPreferences();
        if (intent.getAction().equals(REFERRER_SOURCE_GPLAY)) {
            String stringExtra = intent.getStringExtra(ARG_KEY_GPLAY_REFERRER);
            if (stringExtra != null) {
                piwikPreferences.edit().putString(PREF_KEY_INSTALL_REFERRER_EXTRAS, stringExtra).apply();
                Timber.tag(str).d("Stored Google Play referrer extras: %s", stringExtra);
            }
        }
        intent.setComponent(null);
        intent.setPackage(context.getPackageName());
        intent.putExtra(str2, true);
        context.sendBroadcast(intent);
    }
}
