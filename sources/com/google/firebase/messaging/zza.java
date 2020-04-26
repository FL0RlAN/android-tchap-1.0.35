package com.google.firebase.messaging;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Process;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.NotificationCompat.BigTextStyle;
import androidx.core.app.NotificationCompat.Builder;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.firebase.iid.zzav;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONException;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

final class zza {
    private static final AtomicInteger zzdp = new AtomicInteger((int) SystemClock.elapsedRealtime());
    private Bundle zzdq;
    private final Context zzz;

    public zza(Context context) {
        this.zzz = context.getApplicationContext();
    }

    static boolean zzf(Bundle bundle) {
        return "1".equals(zza(bundle, "gcm.n.e")) || zza(bundle, "gcm.n.icon") != null;
    }

    static String zza(Bundle bundle, String str) {
        String string = bundle.getString(str);
        return string == null ? bundle.getString(str.replace("gcm.n.", "gcm.notification.")) : string;
    }

    static String zzb(Bundle bundle, String str) {
        String valueOf = String.valueOf(str);
        String str2 = "_loc_key";
        return zza(bundle, str2.length() != 0 ? valueOf.concat(str2) : new String(valueOf));
    }

    static Object[] zzc(Bundle bundle, String str) {
        String valueOf = String.valueOf(str);
        String str2 = "_loc_args";
        String zza = zza(bundle, str2.length() != 0 ? valueOf.concat(str2) : new String(valueOf));
        if (TextUtils.isEmpty(zza)) {
            return null;
        }
        try {
            JSONArray jSONArray = new JSONArray(zza);
            Object[] objArr = new String[jSONArray.length()];
            for (int i = 0; i < objArr.length; i++) {
                objArr[i] = jSONArray.opt(i);
            }
            return objArr;
        } catch (JSONException unused) {
            String valueOf2 = String.valueOf(str);
            String substring = (str2.length() != 0 ? valueOf2.concat(str2) : new String(valueOf2)).substring(6);
            StringBuilder sb = new StringBuilder(String.valueOf(substring).length() + 41 + String.valueOf(zza).length());
            sb.append("Malformed ");
            sb.append(substring);
            sb.append(": ");
            sb.append(zza);
            sb.append("  Default value will be used.");
            Log.w("FirebaseMessaging", sb.toString());
            return null;
        }
    }

    static Uri zzg(Bundle bundle) {
        String zza = zza(bundle, "gcm.n.link_android");
        if (TextUtils.isEmpty(zza)) {
            zza = zza(bundle, "gcm.n.link");
        }
        if (!TextUtils.isEmpty(zza)) {
            return Uri.parse(zza);
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x0307  */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x0310  */
    /* JADX WARNING: Removed duplicated region for block: B:109:0x0321  */
    /* JADX WARNING: Removed duplicated region for block: B:111:0x032a  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x032f  */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x0334  */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x0339  */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x034c  */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x0361  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0126  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0128  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x018c  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x01a0  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x01d5  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x01d7  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x021d  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x021f  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x022b  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x025d  */
    public final boolean zzh(Bundle bundle) {
        boolean z;
        int i;
        Integer zzl;
        String zzi;
        Uri uri;
        String zza;
        Intent intent;
        PendingIntent pendingIntent;
        boolean z2;
        PendingIntent pendingIntent2;
        String str;
        String zza2;
        Bundle bundle2 = bundle;
        String str2 = "1";
        if (str2.equals(zza(bundle2, "gcm.n.noui"))) {
            return true;
        }
        if (!((KeyguardManager) this.zzz.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
            if (!PlatformVersion.isAtLeastLollipop()) {
                SystemClock.sleep(10);
            }
            int myPid = Process.myPid();
            List runningAppProcesses = ((ActivityManager) this.zzz.getSystemService("activity")).getRunningAppProcesses();
            if (runningAppProcesses != null) {
                Iterator it = runningAppProcesses.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    RunningAppProcessInfo runningAppProcessInfo = (RunningAppProcessInfo) it.next();
                    if (runningAppProcessInfo.pid == myPid) {
                        if (runningAppProcessInfo.importance == 100) {
                            z = true;
                        }
                    }
                }
            }
        }
        z = false;
        if (z) {
            return false;
        }
        CharSequence zzd = zzd(bundle2, "gcm.n.title");
        if (TextUtils.isEmpty(zzd)) {
            zzd = this.zzz.getApplicationInfo().loadLabel(this.zzz.getPackageManager());
        }
        String zzd2 = zzd(bundle2, "gcm.n.body");
        String zza3 = zza(bundle2, "gcm.n.icon");
        String str3 = "FirebaseMessaging";
        if (!TextUtils.isEmpty(zza3)) {
            Resources resources = this.zzz.getResources();
            i = resources.getIdentifier(zza3, "drawable", this.zzz.getPackageName());
            if (i == 0 || !zzb(i)) {
                i = resources.getIdentifier(zza3, "mipmap", this.zzz.getPackageName());
                if (i == 0 || !zzb(i)) {
                    StringBuilder sb = new StringBuilder(String.valueOf(zza3).length() + 61);
                    sb.append("Icon resource ");
                    sb.append(zza3);
                    sb.append(" not found. Notification will use default icon.");
                    Log.w(str3, sb.toString());
                }
            }
            zzl = zzl(zza(bundle2, "gcm.n.color"));
            zzi = zzi(bundle);
            if (!TextUtils.isEmpty(zzi)) {
                uri = null;
            } else {
                if (!BingRule.ACTION_VALUE_DEFAULT.equals(zzi)) {
                    if (this.zzz.getResources().getIdentifier(zzi, "raw", this.zzz.getPackageName()) != 0) {
                        String packageName = this.zzz.getPackageName();
                        StringBuilder sb2 = new StringBuilder(String.valueOf(packageName).length() + 24 + String.valueOf(zzi).length());
                        sb2.append("android.resource://");
                        sb2.append(packageName);
                        sb2.append("/raw/");
                        sb2.append(zzi);
                        uri = Uri.parse(sb2.toString());
                    }
                }
                uri = RingtoneManager.getDefaultUri(2);
            }
            zza = zza(bundle2, "gcm.n.click_action");
            if (TextUtils.isEmpty(zza)) {
                intent = new Intent(zza);
                intent.setPackage(this.zzz.getPackageName());
                intent.setFlags(268435456);
            } else {
                Uri zzg = zzg(bundle);
                if (zzg != null) {
                    intent = new Intent("android.intent.action.VIEW");
                    intent.setPackage(this.zzz.getPackageName());
                    intent.setData(zzg);
                } else {
                    intent = this.zzz.getPackageManager().getLaunchIntentForPackage(this.zzz.getPackageName());
                    if (intent == null) {
                        Log.w(str3, "No activity found to launch app");
                    }
                }
            }
            if (intent != null) {
                pendingIntent = null;
            } else {
                intent.addFlags(67108864);
                Bundle bundle3 = new Bundle(bundle2);
                FirebaseMessagingService.zzj(bundle3);
                intent.putExtras(bundle3);
                for (String str4 : bundle3.keySet()) {
                    if (str4.startsWith("gcm.n.") || str4.startsWith("gcm.notification.")) {
                        intent.removeExtra(str4);
                    }
                }
                pendingIntent = PendingIntent.getActivity(this.zzz, zzdp.incrementAndGet(), intent, 1073741824);
            }
            if (bundle2 != null) {
                z2 = false;
            } else {
                z2 = str2.equals(bundle2.getString("google.c.a.e"));
            }
            if (!z2) {
                Intent intent2 = new Intent("com.google.firebase.messaging.NOTIFICATION_OPEN");
                zza(intent2, bundle2);
                intent2.putExtra("pending_intent", pendingIntent);
                pendingIntent = zzav.zza(this.zzz, zzdp.incrementAndGet(), intent2, 1073741824);
                Intent intent3 = new Intent("com.google.firebase.messaging.NOTIFICATION_DISMISS");
                zza(intent3, bundle2);
                pendingIntent2 = zzav.zza(this.zzz, zzdp.incrementAndGet(), intent3, 1073741824);
            } else {
                pendingIntent2 = null;
            }
            String zza4 = zza(bundle2, "gcm.n.android_channel_id");
            str = "fcm_fallback_notification_channel";
            if (PlatformVersion.isAtLeastO() || this.zzz.getApplicationInfo().targetSdkVersion < 26) {
                str = null;
            } else {
                NotificationManager notificationManager = (NotificationManager) this.zzz.getSystemService(NotificationManager.class);
                if (!TextUtils.isEmpty(zza4)) {
                    if (notificationManager.getNotificationChannel(zza4) != null) {
                        str = zza4;
                    } else {
                        StringBuilder sb3 = new StringBuilder(String.valueOf(zza4).length() + 122);
                        sb3.append("Notification Channel requested (");
                        sb3.append(zza4);
                        sb3.append(") has not been created by the app. Manifest configuration, or default, value will be used.");
                        Log.w(str3, sb3.toString());
                    }
                }
                String string = zzar().getString("com.google.firebase.messaging.default_notification_channel_id");
                if (TextUtils.isEmpty(string)) {
                    Log.w(str3, "Missing Default Notification Channel metadata in AndroidManifest. Default value will be used.");
                } else if (notificationManager.getNotificationChannel(string) != null) {
                    str = string;
                } else {
                    Log.w(str3, "Notification Channel set in AndroidManifest.xml has not been created by the app. Default value will be used.");
                }
                if (notificationManager.getNotificationChannel(str) == null) {
                    notificationManager.createNotificationChannel(new NotificationChannel(str, this.zzz.getString(R.string.fcm_fallback_notification_channel_label), 3));
                }
            }
            Builder smallIcon = new Builder(this.zzz).setAutoCancel(true).setSmallIcon(i);
            if (!TextUtils.isEmpty(zzd)) {
                smallIcon.setContentTitle(zzd);
            }
            if (!TextUtils.isEmpty(zzd2)) {
                smallIcon.setContentText(zzd2);
                smallIcon.setStyle(new BigTextStyle().bigText(zzd2));
            }
            if (zzl != null) {
                smallIcon.setColor(zzl.intValue());
            }
            if (uri != null) {
                smallIcon.setSound(uri);
            }
            if (pendingIntent != null) {
                smallIcon.setContentIntent(pendingIntent);
            }
            if (pendingIntent2 != null) {
                smallIcon.setDeleteIntent(pendingIntent2);
            }
            if (str != null) {
                smallIcon.setChannelId(str);
            }
            Notification build = smallIcon.build();
            zza2 = zza(bundle2, "gcm.n.tag");
            if (Log.isLoggable(str3, 3)) {
                Log.d(str3, "Showing notification");
            }
            NotificationManager notificationManager2 = (NotificationManager) this.zzz.getSystemService("notification");
            if (TextUtils.isEmpty(zza2)) {
                long uptimeMillis = SystemClock.uptimeMillis();
                StringBuilder sb4 = new StringBuilder(37);
                sb4.append("FCM-Notification:");
                sb4.append(uptimeMillis);
                zza2 = sb4.toString();
            }
            notificationManager2.notify(zza2, 0, build);
            return true;
        }
        int i2 = zzar().getInt("com.google.firebase.messaging.default_notification_icon", 0);
        if (i2 == 0 || !zzb(i2)) {
            i2 = this.zzz.getApplicationInfo().icon;
        }
        i = (i2 == 0 || !zzb(i2)) ? 17301651 : i2;
        zzl = zzl(zza(bundle2, "gcm.n.color"));
        zzi = zzi(bundle);
        if (!TextUtils.isEmpty(zzi)) {
        }
        zza = zza(bundle2, "gcm.n.click_action");
        if (TextUtils.isEmpty(zza)) {
        }
        if (intent != null) {
        }
        if (bundle2 != null) {
        }
        if (!z2) {
        }
        String zza42 = zza(bundle2, "gcm.n.android_channel_id");
        str = "fcm_fallback_notification_channel";
        if (PlatformVersion.isAtLeastO()) {
        }
        str = null;
        Builder smallIcon2 = new Builder(this.zzz).setAutoCancel(true).setSmallIcon(i);
        if (!TextUtils.isEmpty(zzd)) {
        }
        if (!TextUtils.isEmpty(zzd2)) {
        }
        if (zzl != null) {
        }
        if (uri != null) {
        }
        if (pendingIntent != null) {
        }
        if (pendingIntent2 != null) {
        }
        if (str != null) {
        }
        Notification build2 = smallIcon2.build();
        zza2 = zza(bundle2, "gcm.n.tag");
        if (Log.isLoggable(str3, 3)) {
        }
        NotificationManager notificationManager22 = (NotificationManager) this.zzz.getSystemService("notification");
        if (TextUtils.isEmpty(zza2)) {
        }
        notificationManager22.notify(zza2, 0, build2);
        return true;
    }

    private final String zzd(Bundle bundle, String str) {
        String zza = zza(bundle, str);
        if (!TextUtils.isEmpty(zza)) {
            return zza;
        }
        String zzb = zzb(bundle, str);
        if (TextUtils.isEmpty(zzb)) {
            return null;
        }
        Resources resources = this.zzz.getResources();
        int identifier = resources.getIdentifier(zzb, "string", this.zzz.getPackageName());
        String str2 = " Default value will be used.";
        String str3 = "FirebaseMessaging";
        if (identifier == 0) {
            String valueOf = String.valueOf(str);
            String str4 = "_loc_key";
            String substring = (str4.length() != 0 ? valueOf.concat(str4) : new String(valueOf)).substring(6);
            StringBuilder sb = new StringBuilder(String.valueOf(substring).length() + 49 + String.valueOf(zzb).length());
            sb.append(substring);
            sb.append(" resource not found: ");
            sb.append(zzb);
            sb.append(str2);
            Log.w(str3, sb.toString());
            return null;
        }
        Object[] zzc = zzc(bundle, str);
        if (zzc == null) {
            return resources.getString(identifier);
        }
        try {
            return resources.getString(identifier, zzc);
        } catch (MissingFormatArgumentException e) {
            String arrays = Arrays.toString(zzc);
            StringBuilder sb2 = new StringBuilder(String.valueOf(zzb).length() + 58 + String.valueOf(arrays).length());
            sb2.append("Missing format argument for ");
            sb2.append(zzb);
            sb2.append(": ");
            sb2.append(arrays);
            sb2.append(str2);
            Log.w(str3, sb2.toString(), e);
            return null;
        }
    }

    private final boolean zzb(int i) {
        if (VERSION.SDK_INT != 26) {
            return true;
        }
        try {
            if (!(this.zzz.getResources().getDrawable(i, null) instanceof AdaptiveIconDrawable)) {
                return true;
            }
            StringBuilder sb = new StringBuilder(77);
            sb.append("Adaptive icons cannot be used in notifications. Ignoring icon id: ");
            sb.append(i);
            Log.e("FirebaseMessaging", sb.toString());
            return false;
        } catch (NotFoundException unused) {
            return false;
        }
    }

    private final Integer zzl(String str) {
        if (VERSION.SDK_INT < 21) {
            return null;
        }
        String str2 = "FirebaseMessaging";
        if (!TextUtils.isEmpty(str)) {
            try {
                return Integer.valueOf(Color.parseColor(str));
            } catch (IllegalArgumentException unused) {
                StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 54);
                sb.append("Color ");
                sb.append(str);
                sb.append(" not valid. Notification will use default color.");
                Log.w(str2, sb.toString());
            }
        }
        int i = zzar().getInt("com.google.firebase.messaging.default_notification_color", 0);
        if (i != 0) {
            try {
                return Integer.valueOf(ContextCompat.getColor(this.zzz, i));
            } catch (NotFoundException unused2) {
                Log.w(str2, "Cannot find the color resource referenced in AndroidManifest.");
            }
        }
        return null;
    }

    static String zzi(Bundle bundle) {
        String zza = zza(bundle, "gcm.n.sound2");
        return TextUtils.isEmpty(zza) ? zza(bundle, "gcm.n.sound") : zza;
    }

    private static void zza(Intent intent, Bundle bundle) {
        for (String str : bundle.keySet()) {
            if (str.startsWith("google.c.a.") || str.equals("from")) {
                intent.putExtra(str, bundle.getString(str));
            }
        }
    }

    private final Bundle zzar() {
        Bundle bundle = this.zzdq;
        if (bundle != null) {
            return bundle;
        }
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = this.zzz.getPackageManager().getApplicationInfo(this.zzz.getPackageName(), 128);
        } catch (NameNotFoundException unused) {
        }
        if (applicationInfo == null || applicationInfo.metaData == null) {
            return Bundle.EMPTY;
        }
        this.zzdq = applicationInfo.metaData;
        return this.zzdq;
    }
}
