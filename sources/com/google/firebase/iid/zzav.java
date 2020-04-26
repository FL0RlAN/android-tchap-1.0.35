package com.google.firebase.iid;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.util.Log;
import androidx.collection.SimpleArrayMap;
import androidx.legacy.content.WakefulBroadcastReceiver;
import java.util.ArrayDeque;
import java.util.Queue;
import org.matrix.olm.OlmException;

public final class zzav {
    private static zzav zzcy;
    private final SimpleArrayMap<String, String> zzcz = new SimpleArrayMap<>();
    private Boolean zzda = null;
    private Boolean zzdb = null;
    final Queue<Intent> zzdc = new ArrayDeque();
    private final Queue<Intent> zzdd = new ArrayDeque();

    public static synchronized zzav zzai() {
        zzav zzav;
        synchronized (zzav.class) {
            if (zzcy == null) {
                zzcy = new zzav();
            }
            zzav = zzcy;
        }
        return zzav;
    }

    private zzav() {
    }

    public static PendingIntent zza(Context context, int i, Intent intent, int i2) {
        return PendingIntent.getBroadcast(context, i, zza(context, "com.google.firebase.MESSAGING_EVENT", intent), 1073741824);
    }

    public static void zzb(Context context, Intent intent) {
        context.sendBroadcast(zza(context, "com.google.firebase.INSTANCE_ID_EVENT", intent));
    }

    public static void zzc(Context context, Intent intent) {
        context.sendBroadcast(zza(context, "com.google.firebase.MESSAGING_EVENT", intent));
    }

    private static Intent zza(Context context, String str, Intent intent) {
        Intent intent2 = new Intent(context, FirebaseInstanceIdReceiver.class);
        intent2.setAction(str);
        intent2.putExtra("wrapped_intent", intent);
        return intent2;
    }

    public final Intent zzaj() {
        return (Intent) this.zzdd.poll();
    }

    public final int zzb(Context context, String str, Intent intent) {
        String str2 = "FirebaseInstanceId";
        if (Log.isLoggable(str2, 3)) {
            String str3 = "Starting service: ";
            String valueOf = String.valueOf(str);
            Log.d(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
        }
        char c = 65535;
        int hashCode = str.hashCode();
        if (hashCode != -842411455) {
            if (hashCode == 41532704 && str.equals("com.google.firebase.MESSAGING_EVENT")) {
                c = 1;
            }
        } else if (str.equals("com.google.firebase.INSTANCE_ID_EVENT")) {
            c = 0;
        }
        if (c == 0) {
            this.zzdc.offer(intent);
        } else if (c != 1) {
            String str4 = "Unknown service action: ";
            String valueOf2 = String.valueOf(str);
            Log.w(str2, valueOf2.length() != 0 ? str4.concat(valueOf2) : new String(str4));
            return 500;
        } else {
            this.zzdd.offer(intent);
        }
        Intent intent2 = new Intent(str);
        intent2.setPackage(context.getPackageName());
        return zzd(context, intent2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x00de A[Catch:{ SecurityException -> 0x0124, IllegalStateException -> 0x00fc }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00e3 A[Catch:{ SecurityException -> 0x0124, IllegalStateException -> 0x00fc }] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00f0 A[Catch:{ SecurityException -> 0x0124, IllegalStateException -> 0x00fc }] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00fa  */
    private final int zzd(Context context, Intent intent) {
        String str;
        ComponentName componentName;
        synchronized (this.zzcz) {
            str = (String) this.zzcz.get(intent.getAction());
        }
        if (str == null) {
            ResolveInfo resolveService = context.getPackageManager().resolveService(intent, 0);
            if (resolveService == null || resolveService.serviceInfo == null) {
                Log.e("FirebaseInstanceId", "Failed to resolve target intent service, skipping classname enforcement");
                if (zzd(context)) {
                    componentName = WakefulBroadcastReceiver.startWakefulService(context, intent);
                } else {
                    componentName = context.startService(intent);
                    Log.d("FirebaseInstanceId", "Missing wake lock permission, service start may be delayed");
                }
                if (componentName != null) {
                    return -1;
                }
                Log.e("FirebaseInstanceId", "Error while delivering the message: ServiceIntent not found.");
                return 404;
            }
            ServiceInfo serviceInfo = resolveService.serviceInfo;
            if (!context.getPackageName().equals(serviceInfo.packageName) || serviceInfo.name == null) {
                String str2 = serviceInfo.packageName;
                String str3 = serviceInfo.name;
                StringBuilder sb = new StringBuilder(String.valueOf(str2).length() + 94 + String.valueOf(str3).length());
                sb.append("Error resolving target intent service, skipping classname enforcement. Resolved service was: ");
                sb.append(str2);
                sb.append("/");
                sb.append(str3);
                Log.e("FirebaseInstanceId", sb.toString());
                if (zzd(context)) {
                }
                if (componentName != null) {
                }
            } else {
                String str4 = serviceInfo.name;
                if (str4.startsWith(".")) {
                    String valueOf = String.valueOf(context.getPackageName());
                    String valueOf2 = String.valueOf(str4);
                    str4 = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
                }
                str = str4;
                synchronized (this.zzcz) {
                    this.zzcz.put(intent.getAction(), str);
                }
            }
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            String str5 = "Restricting intent to a specific service: ";
            String valueOf3 = String.valueOf(str);
            Log.d("FirebaseInstanceId", valueOf3.length() != 0 ? str5.concat(valueOf3) : new String(str5));
        }
        intent.setClassName(context.getPackageName(), str);
        try {
            if (zzd(context)) {
            }
            if (componentName != null) {
            }
        } catch (SecurityException e) {
            Log.e("FirebaseInstanceId", "Error while delivering the message to the serviceIntent", e);
            return OlmException.EXCEPTION_CODE_SESSION_INIT_OUTBOUND_SESSION;
        } catch (IllegalStateException e2) {
            String valueOf4 = String.valueOf(e2);
            StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf4).length() + 45);
            sb2.append("Failed to start service while in background: ");
            sb2.append(valueOf4);
            Log.e("FirebaseInstanceId", sb2.toString());
            return OlmException.EXCEPTION_CODE_SESSION_INIT_INBOUND_SESSION;
        }
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzd(Context context) {
        if (this.zzda == null) {
            this.zzda = Boolean.valueOf(context.checkCallingOrSelfPermission("android.permission.WAKE_LOCK") == 0);
        }
        if (!this.zzda.booleanValue()) {
            String str = "FirebaseInstanceId";
            if (Log.isLoggable(str, 3)) {
                Log.d(str, "Missing Permission: android.permission.WAKE_LOCK this should normally be included by the manifest merger, but may needed to be manually added to your manifest");
            }
        }
        return this.zzda.booleanValue();
    }

    /* access modifiers changed from: 0000 */
    public final boolean zze(Context context) {
        if (this.zzdb == null) {
            this.zzdb = Boolean.valueOf(context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == 0);
        }
        if (!this.zzda.booleanValue()) {
            String str = "FirebaseInstanceId";
            if (Log.isLoggable(str, 3)) {
                Log.d(str, "Missing Permission: android.permission.ACCESS_NETWORK_STATE this should normally be included by the manifest merger, but may needed to be manually added to your manifest");
            }
        }
        return this.zzdb.booleanValue();
    }
}
