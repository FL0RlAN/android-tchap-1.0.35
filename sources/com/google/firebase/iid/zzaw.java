package com.google.firebase.iid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import androidx.collection.ArrayMap;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.IOException;
import java.util.Map;

final class zzaw {
    private final SharedPreferences zzde;
    private final zzy zzdf;
    private final Map<String, zzz> zzdg;
    private final Context zzz;

    public zzaw(Context context) {
        this(context, new zzy());
    }

    private zzaw(Context context, zzy zzy) {
        String str = "FirebaseInstanceId";
        this.zzdg = new ArrayMap();
        this.zzz = context;
        this.zzde = context.getSharedPreferences("com.google.android.gms.appid", 0);
        this.zzdf = zzy;
        File file = new File(ContextCompat.getNoBackupFilesDir(this.zzz), "com.google.android.gms.appid-no-backup");
        if (!file.exists()) {
            try {
                if (file.createNewFile() && !isEmpty()) {
                    Log.i(str, "App restored, clearing state");
                    zzal();
                    FirebaseInstanceId.getInstance().zzn();
                }
            } catch (IOException e) {
                if (Log.isLoggable(str, 3)) {
                    String str2 = "Error creating file in no backup dir: ";
                    String valueOf = String.valueOf(e.getMessage());
                    Log.d(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                }
            }
        }
    }

    public final synchronized String zzak() {
        return this.zzde.getString("topic_operaion_queue", "");
    }

    public final synchronized void zzf(String str) {
        this.zzde.edit().putString("topic_operaion_queue", str).apply();
    }

    private final synchronized boolean isEmpty() {
        return this.zzde.getAll().isEmpty();
    }

    private static String zza(String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 4 + String.valueOf(str2).length() + String.valueOf(str3).length());
        sb.append(str);
        sb.append("|T|");
        sb.append(str2);
        sb.append("|");
        sb.append(str3);
        return sb.toString();
    }

    static String zzd(String str, String str2) {
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 3 + String.valueOf(str2).length());
        sb.append(str);
        sb.append("|S|");
        sb.append(str2);
        return sb.toString();
    }

    public final synchronized void zzal() {
        this.zzdg.clear();
        zzy.zza(this.zzz);
        this.zzde.edit().clear().commit();
    }

    public final synchronized zzax zzb(String str, String str2, String str3) {
        return zzax.zzi(this.zzde.getString(zza(str, str2, str3), null));
    }

    public final synchronized void zza(String str, String str2, String str3, String str4, String str5) {
        String zza = zzax.zza(str4, str5, System.currentTimeMillis());
        if (zza != null) {
            Editor edit = this.zzde.edit();
            edit.putString(zza(str, str2, str3), zza);
            edit.commit();
        }
    }

    public final synchronized void zzc(String str, String str2, String str3) {
        String zza = zza(str, str2, str3);
        Editor edit = this.zzde.edit();
        edit.remove(zza);
        edit.commit();
    }

    public final synchronized zzz zzg(String str) {
        zzz zzz2;
        zzz zzz3 = (zzz) this.zzdg.get(str);
        if (zzz3 != null) {
            return zzz3;
        }
        try {
            zzz2 = this.zzdf.zzb(this.zzz, str);
        } catch (zzaa unused) {
            Log.w("FirebaseInstanceId", "Stored data is corrupt, generating new identity");
            FirebaseInstanceId.getInstance().zzn();
            zzz2 = this.zzdf.zzc(this.zzz, str);
        }
        this.zzdg.put(str, zzz2);
        return zzz2;
    }

    public final synchronized void zzh(String str) {
        String concat = String.valueOf(str).concat("|T|");
        Editor edit = this.zzde.edit();
        for (String str2 : this.zzde.getAll().keySet()) {
            if (str2.startsWith(concat)) {
                edit.remove(str2);
            }
        }
        edit.commit();
    }
}
