package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.common.util.concurrent.NamedThreadFactory;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.DataCollectionDefaultChange;
import com.google.firebase.FirebaseApp;
import com.google.firebase.events.EventHandler;
import com.google.firebase.events.Subscriber;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FirebaseInstanceId {
    private static final long zzak = TimeUnit.HOURS.toSeconds(8);
    private static zzaw zzal;
    private static ScheduledThreadPoolExecutor zzam;
    private final Executor zzan;
    /* access modifiers changed from: private */
    public final FirebaseApp zzao;
    private final zzan zzap;
    private MessagingChannel zzaq;
    private final zzaq zzar;
    private final zzba zzas;
    private boolean zzat;
    private final zza zzau;

    private class zza {
        private final boolean zzba = zzu();
        private final Subscriber zzbb;
        private EventHandler<DataCollectionDefaultChange> zzbc;
        private Boolean zzbd = zzt();

        zza(Subscriber subscriber) {
            this.zzbb = subscriber;
            if (this.zzbd == null && this.zzba) {
                this.zzbc = new zzq(this);
                subscriber.subscribe(DataCollectionDefaultChange.class, this.zzbc);
            }
        }

        /* access modifiers changed from: 0000 */
        public final synchronized boolean isEnabled() {
            if (this.zzbd == null) {
                return this.zzba && FirebaseInstanceId.this.zzao.isDataCollectionDefaultEnabled();
            }
            return this.zzbd.booleanValue();
        }

        /* access modifiers changed from: 0000 */
        public final synchronized void setEnabled(boolean z) {
            if (this.zzbc != null) {
                this.zzbb.unsubscribe(DataCollectionDefaultChange.class, this.zzbc);
                this.zzbc = null;
            }
            Editor edit = FirebaseInstanceId.this.zzao.getApplicationContext().getSharedPreferences("com.google.firebase.messaging", 0).edit();
            edit.putBoolean("auto_init", z);
            edit.apply();
            if (z) {
                FirebaseInstanceId.this.zzh();
            }
            this.zzbd = Boolean.valueOf(z);
        }

        private final Boolean zzt() {
            String str = "firebase_messaging_auto_init_enabled";
            Context applicationContext = FirebaseInstanceId.this.zzao.getApplicationContext();
            SharedPreferences sharedPreferences = applicationContext.getSharedPreferences("com.google.firebase.messaging", 0);
            String str2 = "auto_init";
            if (sharedPreferences.contains(str2)) {
                return Boolean.valueOf(sharedPreferences.getBoolean(str2, false));
            }
            try {
                PackageManager packageManager = applicationContext.getPackageManager();
                if (packageManager != null) {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(applicationContext.getPackageName(), 128);
                    if (!(applicationInfo == null || applicationInfo.metaData == null || !applicationInfo.metaData.containsKey(str))) {
                        return Boolean.valueOf(applicationInfo.metaData.getBoolean(str));
                    }
                }
            } catch (NameNotFoundException unused) {
            }
            return null;
        }

        private final boolean zzu() {
            try {
                Class.forName("com.google.firebase.messaging.FirebaseMessaging");
                return true;
            } catch (ClassNotFoundException unused) {
                Context applicationContext = FirebaseInstanceId.this.zzao.getApplicationContext();
                Intent intent = new Intent("com.google.firebase.MESSAGING_EVENT");
                intent.setPackage(applicationContext.getPackageName());
                ResolveInfo resolveService = applicationContext.getPackageManager().resolveService(intent, 0);
                if (resolveService == null || resolveService.serviceInfo == null) {
                    return false;
                }
                return true;
            }
        }
    }

    public static FirebaseInstanceId getInstance() {
        return getInstance(FirebaseApp.getInstance());
    }

    public static FirebaseInstanceId getInstance(FirebaseApp firebaseApp) {
        return (FirebaseInstanceId) firebaseApp.get(FirebaseInstanceId.class);
    }

    FirebaseInstanceId(FirebaseApp firebaseApp, Subscriber subscriber) {
        this(firebaseApp, new zzan(firebaseApp.getApplicationContext()), zzi.zzg(), zzi.zzg(), subscriber);
    }

    private FirebaseInstanceId(FirebaseApp firebaseApp, zzan zzan2, Executor executor, Executor executor2, Subscriber subscriber) {
        this.zzat = false;
        if (zzan.zza(firebaseApp) != null) {
            synchronized (FirebaseInstanceId.class) {
                if (zzal == null) {
                    zzal = new zzaw(firebaseApp.getApplicationContext());
                }
            }
            this.zzao = firebaseApp;
            this.zzap = zzan2;
            if (this.zzaq == null) {
                MessagingChannel messagingChannel = (MessagingChannel) firebaseApp.get(MessagingChannel.class);
                if (messagingChannel == null || !messagingChannel.isAvailable()) {
                    this.zzaq = new zzr(firebaseApp, zzan2, executor);
                } else {
                    this.zzaq = messagingChannel;
                }
            }
            this.zzaq = this.zzaq;
            this.zzan = executor2;
            this.zzas = new zzba(zzal);
            this.zzau = new zza(subscriber);
            this.zzar = new zzaq(executor);
            if (this.zzau.isEnabled()) {
                zzh();
                return;
            }
            return;
        }
        throw new IllegalStateException("FirebaseInstanceId failed to initialize, FirebaseApp is missing project ID");
    }

    /* access modifiers changed from: private */
    public final void zzh() {
        zzax zzk = zzk();
        if (zzr() || zza(zzk) || this.zzas.zzap()) {
            startSync();
        }
    }

    /* access modifiers changed from: 0000 */
    public final FirebaseApp zzi() {
        return this.zzao;
    }

    /* access modifiers changed from: 0000 */
    public final synchronized void zza(boolean z) {
        this.zzat = z;
    }

    private final synchronized void startSync() {
        if (!this.zzat) {
            zza(0);
        }
    }

    /* access modifiers changed from: 0000 */
    public final synchronized void zza(long j) {
        zzay zzay = new zzay(this, this.zzap, this.zzas, Math.min(Math.max(30, j << 1), zzak));
        zza((Runnable) zzay, j);
        this.zzat = true;
    }

    static void zza(Runnable runnable, long j) {
        synchronized (FirebaseInstanceId.class) {
            if (zzam == null) {
                zzam = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("FirebaseInstanceId"));
            }
            zzam.schedule(runnable, j, TimeUnit.SECONDS);
        }
    }

    public String getId() {
        zzh();
        return zzj();
    }

    private static String zzj() {
        return zzan.zza(zzal.zzg("").getKeyPair());
    }

    public long getCreationTime() {
        return zzal.zzg("").getCreationTime();
    }

    public Task<InstanceIdResult> getInstanceId() {
        return zza(zzan.zza(this.zzao), "*");
    }

    private final Task<InstanceIdResult> zza(String str, String str2) {
        return Tasks.forResult(null).continueWithTask(this.zzan, new zzn(this, str, zzd(str2)));
    }

    public void deleteInstanceId() throws IOException {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            zza(this.zzaq.deleteInstanceId(zzj()));
            zzn();
            return;
        }
        throw new IOException("MAIN_THREAD");
    }

    @Deprecated
    public String getToken() {
        zzax zzk = zzk();
        if (this.zzaq.needsRefresh() || zza(zzk)) {
            startSync();
        }
        return zzax.zzb(zzk);
    }

    /* access modifiers changed from: 0000 */
    public final zzax zzk() {
        return zzb(zzan.zza(this.zzao), "*");
    }

    private static zzax zzb(String str, String str2) {
        return zzal.zzb("", str, str2);
    }

    /* access modifiers changed from: 0000 */
    public final String zzl() throws IOException {
        return getToken(zzan.zza(this.zzao), "*");
    }

    public String getToken(String str, String str2) throws IOException {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            return ((InstanceIdResult) zza(zza(str, str2))).getToken();
        }
        throw new IOException("MAIN_THREAD");
    }

    private final <T> T zza(Task<T> task) throws IOException {
        try {
            return Tasks.await(task, 30000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                if ("INSTANCE_ID_RESET".equals(cause.getMessage())) {
                    zzn();
                }
                throw ((IOException) cause);
            } else if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            } else {
                throw new IOException(e);
            }
        } catch (InterruptedException | TimeoutException unused) {
            throw new IOException("SERVICE_NOT_AVAILABLE");
        }
    }

    public void deleteToken(String str, String str2) throws IOException {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            String zzd = zzd(str2);
            zza(this.zzaq.deleteToken(zzj(), zzax.zzb(zzb(str, zzd)), str, zzd));
            zzal.zzc("", str, zzd);
            return;
        }
        throw new IOException("MAIN_THREAD");
    }

    public final synchronized Task<Void> zza(String str) {
        Task<Void> zza2;
        zza2 = this.zzas.zza(str);
        startSync();
        return zza2;
    }

    /* access modifiers changed from: 0000 */
    public final void zzb(String str) throws IOException {
        zzax zzk = zzk();
        if (!zza(zzk)) {
            zza(this.zzaq.subscribeToTopic(zzj(), zzk.zzbr, str));
            return;
        }
        throw new IOException("token not available");
    }

    /* access modifiers changed from: 0000 */
    public final void zzc(String str) throws IOException {
        zzax zzk = zzk();
        if (!zza(zzk)) {
            zza(this.zzaq.unsubscribeFromTopic(zzj(), zzk.zzbr, str));
            return;
        }
        throw new IOException("token not available");
    }

    static boolean zzm() {
        String str = "FirebaseInstanceId";
        return Log.isLoggable(str, 3) || (VERSION.SDK_INT == 23 && Log.isLoggable(str, 3));
    }

    /* access modifiers changed from: 0000 */
    public final synchronized void zzn() {
        zzal.zzal();
        if (this.zzau.isEnabled()) {
            startSync();
        }
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzo() {
        return this.zzaq.isAvailable();
    }

    /* access modifiers changed from: 0000 */
    public final void zzp() {
        zzal.zzh("");
        startSync();
    }

    public final boolean zzq() {
        return this.zzau.isEnabled();
    }

    public final void zzb(boolean z) {
        this.zzau.setEnabled(z);
    }

    private static String zzd(String str) {
        return (str.isEmpty() || str.equalsIgnoreCase("fcm") || str.equalsIgnoreCase("gcm")) ? "*" : str;
    }

    /* access modifiers changed from: 0000 */
    public final boolean zza(zzax zzax) {
        return zzax == null || zzax.zzj(this.zzap.zzad());
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzr() {
        return this.zzaq.needsRefresh();
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ Task zza(String str, String str2, Task task) throws Exception {
        String zzj = zzj();
        zzax zzb = zzb(str, str2);
        if (!this.zzaq.needsRefresh() && !zza(zzb)) {
            return Tasks.forResult(new zzx(zzj, zzb.zzbr));
        }
        String zzb2 = zzax.zzb(zzb);
        zzaq zzaq2 = this.zzar;
        zzo zzo = new zzo(this, zzj, zzb2, str, str2);
        return zzaq2.zza(str, str2, zzo);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ Task zza(String str, String str2, String str3, String str4) {
        return this.zzaq.getToken(str, str2, str3, str4).onSuccessTask(this.zzan, new zzp(this, str3, str4, str));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ Task zzb(String str, String str2, String str3, String str4) throws Exception {
        zzal.zza("", str, str2, str4, this.zzap.zzad());
        return Tasks.forResult(new zzx(str3, str4));
    }
}
