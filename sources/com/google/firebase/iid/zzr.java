package com.google.firebase.iid;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import java.io.IOException;
import java.util.concurrent.Executor;
import org.matrix.androidsdk.rest.model.bingrules.BingRule;

final class zzr implements MessagingChannel {
    private final FirebaseApp zzao;
    private final zzan zzap;
    private final zzat zzbj;
    private final Executor zzbk;

    zzr(FirebaseApp firebaseApp, zzan zzan, Executor executor) {
        this(firebaseApp, zzan, executor, new zzat(firebaseApp.getApplicationContext(), zzan));
    }

    public final Task<Void> ackMessage(String str) {
        return null;
    }

    public final boolean isChannelBuilt() {
        return true;
    }

    public final boolean needsRefresh() {
        return false;
    }

    private zzr(FirebaseApp firebaseApp, zzan zzan, Executor executor, zzat zzat) {
        this.zzao = firebaseApp;
        this.zzap = zzan;
        this.zzbj = zzat;
        this.zzbk = executor;
    }

    public final boolean isAvailable() {
        return this.zzap.zzac() != 0;
    }

    public final Task<Void> buildChannel(String str, String str2) {
        return Tasks.forResult(null);
    }

    public final Task<String> getToken(String str, String str2, String str3, String str4) {
        return zzc(zza(str, str3, str4, new Bundle()));
    }

    public final Task<Void> deleteToken(String str, String str2, String str3, String str4) {
        Bundle bundle = new Bundle();
        bundle.putString("delete", "1");
        return zzb(zzc(zza(str, str3, str4, bundle)));
    }

    public final Task<Void> deleteInstanceId(String str) {
        Bundle bundle = new Bundle();
        String str2 = "delete";
        bundle.putString("iid-operation", str2);
        bundle.putString(str2, "1");
        String str3 = "*";
        return zzb(zzc(zza(str, str3, str3, bundle)));
    }

    public final Task<Void> subscribeToTopic(String str, String str2, String str3) {
        Bundle bundle = new Bundle();
        String valueOf = String.valueOf(str3);
        String str4 = "/topics/";
        bundle.putString("gcm.topic", valueOf.length() != 0 ? str4.concat(valueOf) : new String(str4));
        String valueOf2 = String.valueOf(str3);
        return zzb(zzc(zza(str, str2, valueOf2.length() != 0 ? str4.concat(valueOf2) : new String(str4), bundle)));
    }

    public final Task<Void> unsubscribeFromTopic(String str, String str2, String str3) {
        Bundle bundle = new Bundle();
        String valueOf = String.valueOf(str3);
        String str4 = "/topics/";
        bundle.putString("gcm.topic", valueOf.length() != 0 ? str4.concat(valueOf) : new String(str4));
        bundle.putString("delete", "1");
        String valueOf2 = String.valueOf(str3);
        return zzb(zzc(zza(str, str2, valueOf2.length() != 0 ? str4.concat(valueOf2) : new String(str4), bundle)));
    }

    private final Task<Bundle> zza(String str, String str2, String str3, Bundle bundle) {
        bundle.putString("scope", str3);
        bundle.putString(BingRule.KIND_SENDER, str2);
        bundle.putString("subtype", str2);
        bundle.putString("appid", str);
        bundle.putString("gmp_app_id", this.zzao.getOptions().getApplicationId());
        bundle.putString("gmsv", Integer.toString(this.zzap.zzaf()));
        bundle.putString("osv", Integer.toString(VERSION.SDK_INT));
        bundle.putString("app_ver", this.zzap.zzad());
        bundle.putString("app_ver_name", this.zzap.zzae());
        bundle.putString("cliv", "fiid-12451000");
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.zzbk.execute(new zzs(this, bundle, taskCompletionSource));
        return taskCompletionSource.getTask();
    }

    /* access modifiers changed from: private */
    public static String zza(Bundle bundle) throws IOException {
        String str = "SERVICE_NOT_AVAILABLE";
        if (bundle != null) {
            String string = bundle.getString("registration_id");
            if (string != null) {
                return string;
            }
            String string2 = bundle.getString("unregistered");
            if (string2 != null) {
                return string2;
            }
            String string3 = bundle.getString("error");
            if ("RST".equals(string3)) {
                throw new IOException("INSTANCE_ID_RESET");
            } else if (string3 != null) {
                throw new IOException(string3);
            } else {
                String valueOf = String.valueOf(bundle);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 21);
                sb.append("Unexpected response: ");
                sb.append(valueOf);
                Log.w("FirebaseInstanceId", sb.toString(), new Throwable());
                throw new IOException(str);
            }
        } else {
            throw new IOException(str);
        }
    }

    private final <T> Task<Void> zzb(Task<T> task) {
        return task.continueWith(zzi.zzf(), new zzt(this));
    }

    private final Task<String> zzc(Task<Bundle> task) {
        return task.continueWith(this.zzbk, new zzu(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void zza(Bundle bundle, TaskCompletionSource taskCompletionSource) {
        try {
            taskCompletionSource.setResult(this.zzbj.zzc(bundle));
        } catch (IOException e) {
            taskCompletionSource.setException(e);
        }
    }
}
