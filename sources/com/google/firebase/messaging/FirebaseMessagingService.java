package com.google.firebase.messaging;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.iid.zzab;
import com.google.firebase.iid.zzav;
import com.google.firebase.iid.zzb;
import im.vector.activity.VectorUniversalLinkActivity;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FirebaseMessagingService extends zzb {
    private static final Queue<String> zzdt = new ArrayDeque(10);

    public void onDeletedMessages() {
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
    }

    public void onMessageSent(String str) {
    }

    public void onNewToken(String str) {
    }

    public void onSendError(String str, Exception exc) {
    }

    /* access modifiers changed from: protected */
    public final Intent zzb(Intent intent) {
        return zzav.zzai().zzaj();
    }

    public final boolean zzc(Intent intent) {
        if (!"com.google.firebase.messaging.NOTIFICATION_OPEN".equals(intent.getAction())) {
            return false;
        }
        PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra("pending_intent");
        if (pendingIntent != null) {
            try {
                pendingIntent.send();
            } catch (CanceledException unused) {
                Log.e("FirebaseMessaging", "Notification pending intent canceled");
            }
        }
        if (MessagingAnalytics.shouldUploadMetrics(intent)) {
            MessagingAnalytics.logNotificationOpen(intent);
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00f3, code lost:
        if (r1.equals(r5) != false) goto L_0x0101;
     */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00c7  */
    public final void zzd(Intent intent) {
        Task task;
        boolean z;
        String action = intent.getAction();
        String str = "FirebaseMessaging";
        if ("com.google.android.c2dm.intent.RECEIVE".equals(action) || "com.google.firebase.messaging.RECEIVE_DIRECT_BOOT".equals(action)) {
            String str2 = "google.message_id";
            String stringExtra = intent.getStringExtra(str2);
            if (TextUtils.isEmpty(stringExtra)) {
                task = Tasks.forResult(null);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(str2, stringExtra);
                task = zzab.zzc(this).zza(2, bundle);
            }
            char c = 0;
            if (!TextUtils.isEmpty(stringExtra)) {
                if (zzdt.contains(stringExtra)) {
                    if (Log.isLoggable(str, 3)) {
                        String str3 = "Received duplicate message: ";
                        String valueOf = String.valueOf(stringExtra);
                        Log.d(str, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
                    }
                    z = true;
                    if (!z) {
                        String stringExtra2 = intent.getStringExtra("message_type");
                        String str4 = "gcm";
                        if (stringExtra2 == null) {
                            stringExtra2 = str4;
                        }
                        switch (stringExtra2.hashCode()) {
                            case -2062414158:
                                if (stringExtra2.equals("deleted_messages")) {
                                    c = 1;
                                    break;
                                }
                            case 102161:
                                break;
                            case 814694033:
                                if (stringExtra2.equals("send_error")) {
                                    c = 3;
                                    break;
                                }
                            case 814800675:
                                if (stringExtra2.equals("send_event")) {
                                    c = 2;
                                    break;
                                }
                            default:
                                c = 65535;
                                break;
                        }
                        if (c == 0) {
                            if (MessagingAnalytics.shouldUploadMetrics(intent)) {
                                MessagingAnalytics.logNotificationReceived(intent);
                            }
                            Bundle extras = intent.getExtras();
                            if (extras == null) {
                                extras = new Bundle();
                            }
                            extras.remove("androidx.contentpager.content.wakelockid");
                            if (zza.zzf(extras)) {
                                if (!new zza(this).zzh(extras)) {
                                    if (MessagingAnalytics.shouldUploadMetrics(intent)) {
                                        MessagingAnalytics.logNotificationForeground(intent);
                                    }
                                }
                            }
                            onMessageReceived(new RemoteMessage(extras));
                        } else if (c == 1) {
                            onDeletedMessages();
                        } else if (c == 2) {
                            onMessageSent(intent.getStringExtra(str2));
                        } else if (c != 3) {
                            String str5 = "Received message with unknown type: ";
                            String valueOf2 = String.valueOf(stringExtra2);
                            Log.w(str, valueOf2.length() != 0 ? str5.concat(valueOf2) : new String(str5));
                        } else {
                            String stringExtra3 = intent.getStringExtra(str2);
                            if (stringExtra3 == null) {
                                stringExtra3 = intent.getStringExtra("message_id");
                            }
                            onSendError(stringExtra3, new SendException(intent.getStringExtra("error")));
                        }
                    }
                    Tasks.await(task, 1, TimeUnit.SECONDS);
                }
                if (zzdt.size() >= 10) {
                    zzdt.remove();
                }
                zzdt.add(stringExtra);
            }
            z = false;
            if (!z) {
            }
            try {
                Tasks.await(task, 1, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                String valueOf3 = String.valueOf(e);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf3).length() + 20);
                sb.append("Message ack failed: ");
                sb.append(valueOf3);
                Log.w(str, sb.toString());
            }
        } else {
            if ("com.google.firebase.messaging.NOTIFICATION_DISMISS".equals(action)) {
                if (MessagingAnalytics.shouldUploadMetrics(intent)) {
                    MessagingAnalytics.logNotificationDismiss(intent);
                }
            } else if ("com.google.firebase.messaging.NEW_TOKEN".equals(action)) {
                onNewToken(intent.getStringExtra(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_TOKEN));
            } else {
                String str6 = "Unknown intent action: ";
                String valueOf4 = String.valueOf(intent.getAction());
                Log.d(str, valueOf4.length() != 0 ? str6.concat(valueOf4) : new String(str6));
            }
        }
    }

    static void zzj(Bundle bundle) {
        Iterator it = bundle.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (str != null && str.startsWith("google.c.")) {
                it.remove();
            }
        }
    }
}
