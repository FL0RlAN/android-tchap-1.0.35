package com.google.firebase.iid;

import android.text.TextUtils;
import android.util.Log;
import im.vector.activity.VectorUniversalLinkActivity;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

final class zzax {
    private static final long zzdh = TimeUnit.DAYS.toMillis(7);
    private final long timestamp;
    final String zzbr;
    private final String zzdi;

    private zzax(String str, String str2, long j) {
        this.zzbr = str;
        this.zzdi = str2;
        this.timestamp = j;
    }

    static zzax zzi(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (!str.startsWith("{")) {
            return new zzax(str, null, 0);
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            return new zzax(jSONObject.getString(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_TOKEN), jSONObject.getString("appVersion"), jSONObject.getLong("timestamp"));
        } catch (JSONException e) {
            String valueOf = String.valueOf(e);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 23);
            sb.append("Failed to parse token: ");
            sb.append(valueOf);
            Log.w("FirebaseInstanceId", sb.toString());
            return null;
        }
    }

    static String zza(String str, String str2, long j) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_TOKEN, str);
            jSONObject.put("appVersion", str2);
            jSONObject.put("timestamp", j);
            return jSONObject.toString();
        } catch (JSONException e) {
            String valueOf = String.valueOf(e);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 24);
            sb.append("Failed to encode token: ");
            sb.append(valueOf);
            Log.w("FirebaseInstanceId", sb.toString());
            return null;
        }
    }

    static String zzb(zzax zzax) {
        if (zzax == null) {
            return null;
        }
        return zzax.zzbr;
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzj(String str) {
        return System.currentTimeMillis() > this.timestamp + zzdh || !str.equals(this.zzdi);
    }
}
