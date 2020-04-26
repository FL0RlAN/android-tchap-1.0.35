package com.google.firebase.iid;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import androidx.collection.SimpleArrayMap;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.iid.zzl.zza;
import im.vector.BuildConfig;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class zzat {
    private static int zzcg;
    private static PendingIntent zzcs;
    private final zzan zzap;
    private final SimpleArrayMap<String, TaskCompletionSource<Bundle>> zzct = new SimpleArrayMap<>();
    private Messenger zzcu;
    private Messenger zzcv;
    private zzl zzcw;
    private final Context zzz;

    public zzat(Context context, zzan zzan) {
        this.zzz = context;
        this.zzap = zzan;
        this.zzcu = new Messenger(new zzau(this, Looper.getMainLooper()));
    }

    /* access modifiers changed from: private */
    public final void zzb(Message message) {
        if (message == null || !(message.obj instanceof Intent)) {
            Log.w("FirebaseInstanceId", "Dropping invalid message");
        } else {
            Intent intent = (Intent) message.obj;
            intent.setExtrasClassLoader(new zza());
            if (intent.hasExtra("google.messenger")) {
                Parcelable parcelableExtra = intent.getParcelableExtra("google.messenger");
                if (parcelableExtra instanceof zzl) {
                    this.zzcw = (zzl) parcelableExtra;
                }
                if (parcelableExtra instanceof Messenger) {
                    this.zzcv = (Messenger) parcelableExtra;
                }
            }
            Intent intent2 = (Intent) message.obj;
            String action = intent2.getAction();
            if (!"com.google.android.c2dm.intent.REGISTRATION".equals(action)) {
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    String str = "Unexpected response action: ";
                    String valueOf = String.valueOf(action);
                    Log.d("FirebaseInstanceId", valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
                }
                return;
            }
            String stringExtra = intent2.getStringExtra("registration_id");
            if (stringExtra == null) {
                stringExtra = intent2.getStringExtra("unregistered");
            }
            if (stringExtra == null) {
                String stringExtra2 = intent2.getStringExtra("error");
                if (stringExtra2 == null) {
                    String valueOf2 = String.valueOf(intent2.getExtras());
                    StringBuilder sb = new StringBuilder(String.valueOf(valueOf2).length() + 49);
                    sb.append("Unexpected response, no error or registration id ");
                    sb.append(valueOf2);
                    Log.w("FirebaseInstanceId", sb.toString());
                } else {
                    if (Log.isLoggable("FirebaseInstanceId", 3)) {
                        String str2 = "Received InstanceID error ";
                        String valueOf3 = String.valueOf(stringExtra2);
                        Log.d("FirebaseInstanceId", valueOf3.length() != 0 ? str2.concat(valueOf3) : new String(str2));
                    }
                    if (stringExtra2.startsWith("|")) {
                        String[] split = stringExtra2.split("\\|");
                        if (split.length > 2) {
                            if ("ID".equals(split[1])) {
                                String str3 = split[2];
                                String str4 = split[3];
                                if (str4.startsWith(":")) {
                                    str4 = str4.substring(1);
                                }
                                zza(str3, intent2.putExtra("error", str4).getExtras());
                            }
                        }
                        String str5 = "Unexpected structured response ";
                        String valueOf4 = String.valueOf(stringExtra2);
                        Log.w("FirebaseInstanceId", valueOf4.length() != 0 ? str5.concat(valueOf4) : new String(str5));
                    } else {
                        synchronized (this.zzct) {
                            for (int i = 0; i < this.zzct.size(); i++) {
                                zza((String) this.zzct.keyAt(i), intent2.getExtras());
                            }
                        }
                    }
                }
            } else {
                Matcher matcher = Pattern.compile("\\|ID\\|([^|]+)\\|:?+(.*)").matcher(stringExtra);
                if (!matcher.matches()) {
                    if (Log.isLoggable("FirebaseInstanceId", 3)) {
                        String str6 = "Unexpected response string: ";
                        String valueOf5 = String.valueOf(stringExtra);
                        Log.d("FirebaseInstanceId", valueOf5.length() != 0 ? str6.concat(valueOf5) : new String(str6));
                    }
                    return;
                }
                String group = matcher.group(1);
                String group2 = matcher.group(2);
                Bundle extras = intent2.getExtras();
                extras.putString("registration_id", group2);
                zza(group, extras);
            }
        }
    }

    private static synchronized void zza(Context context, Intent intent) {
        synchronized (zzat.class) {
            if (zzcs == null) {
                Intent intent2 = new Intent();
                intent2.setPackage("com.google.example.invalidpackage");
                zzcs = PendingIntent.getBroadcast(context, 0, intent2, 0);
            }
            intent.putExtra(BuildConfig.FLAVOR_base, zzcs);
        }
    }

    private final void zza(String str, Bundle bundle) {
        synchronized (this.zzct) {
            TaskCompletionSource taskCompletionSource = (TaskCompletionSource) this.zzct.remove(str);
            if (taskCompletionSource == null) {
                String str2 = "FirebaseInstanceId";
                String str3 = "Missing callback for ";
                String valueOf = String.valueOf(str);
                Log.w(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
                return;
            }
            taskCompletionSource.setResult(bundle);
        }
    }

    /* access modifiers changed from: 0000 */
    public final Bundle zzc(Bundle bundle) throws IOException {
        if (this.zzap.zzaf() < 12000000) {
            return zzd(bundle);
        }
        try {
            return (Bundle) Tasks.await(zzab.zzc(this.zzz).zzb(1, bundle));
        } catch (InterruptedException | ExecutionException e) {
            String str = "FirebaseInstanceId";
            if (Log.isLoggable(str, 3)) {
                String valueOf = String.valueOf(e);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 22);
                sb.append("Error making request: ");
                sb.append(valueOf);
                Log.d(str, sb.toString());
            }
            if (!(e.getCause() instanceof zzal) || ((zzal) e.getCause()).getErrorCode() != 4) {
                return null;
            }
            return zzd(bundle);
        }
    }

    private final Bundle zzd(Bundle bundle) throws IOException {
        Bundle zze = zze(bundle);
        if (zze == null) {
            return zze;
        }
        String str = "google.messenger";
        if (!zze.containsKey(str)) {
            return zze;
        }
        Bundle zze2 = zze(bundle);
        if (zze2 == null || !zze2.containsKey(str)) {
            return zze2;
        }
        return null;
    }

    private static synchronized String zzah() {
        String num;
        synchronized (zzat.class) {
            int i = zzcg;
            zzcg = i + 1;
            num = Integer.toString(i);
        }
        return num;
    }

    /* JADX WARNING: Removed duplicated region for block: B:36:0x00f1 A[SYNTHETIC] */
    private final Bundle zze(Bundle bundle) throws IOException {
        String zzah = zzah();
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        synchronized (this.zzct) {
            this.zzct.put(zzah, taskCompletionSource);
        }
        if (this.zzap.zzac() != 0) {
            Intent intent = new Intent();
            intent.setPackage("com.google.android.gms");
            if (this.zzap.zzac() == 2) {
                intent.setAction("com.google.iid.TOKEN_REQUEST");
            } else {
                intent.setAction("com.google.android.c2dm.intent.REGISTER");
            }
            intent.putExtras(bundle);
            zza(this.zzz, intent);
            StringBuilder sb = new StringBuilder(String.valueOf(zzah).length() + 5);
            sb.append("|ID|");
            sb.append(zzah);
            sb.append("|");
            intent.putExtra("kid", sb.toString());
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String valueOf = String.valueOf(intent.getExtras());
                StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf).length() + 8);
                sb2.append("Sending ");
                sb2.append(valueOf);
                Log.d("FirebaseInstanceId", sb2.toString());
            }
            intent.putExtra("google.messenger", this.zzcu);
            if (!(this.zzcv == null && this.zzcw == null)) {
                Message obtain = Message.obtain();
                obtain.obj = intent;
                try {
                    if (this.zzcv != null) {
                        this.zzcv.send(obtain);
                    } else {
                        this.zzcw.send(obtain);
                    }
                } catch (RemoteException unused) {
                    if (Log.isLoggable("FirebaseInstanceId", 3)) {
                        Log.d("FirebaseInstanceId", "Messenger failed, fallback to startService");
                    }
                }
                Bundle bundle2 = (Bundle) Tasks.await(taskCompletionSource.getTask(), 30000, TimeUnit.MILLISECONDS);
                synchronized (this.zzct) {
                    this.zzct.remove(zzah);
                }
                return bundle2;
            }
            if (this.zzap.zzac() == 2) {
                this.zzz.sendBroadcast(intent);
            } else {
                this.zzz.startService(intent);
            }
            try {
                Bundle bundle22 = (Bundle) Tasks.await(taskCompletionSource.getTask(), 30000, TimeUnit.MILLISECONDS);
                synchronized (this.zzct) {
                }
                return bundle22;
            } catch (InterruptedException | TimeoutException unused2) {
                Log.w("FirebaseInstanceId", "No response");
                throw new IOException("TIMEOUT");
            } catch (ExecutionException e) {
                throw new IOException(e);
            } catch (Throwable th) {
                synchronized (this.zzct) {
                    this.zzct.remove(zzah);
                    throw th;
                }
            }
        } else {
            throw new IOException("MISSING_INSTANCEID_SERVICE");
        }
    }
}
