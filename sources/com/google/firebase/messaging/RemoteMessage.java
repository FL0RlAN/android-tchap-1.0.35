package com.google.firebase.messaging;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import java.util.Map.Entry;

public final class RemoteMessage extends AbstractSafeParcelable {
    public static final Creator<RemoteMessage> CREATOR = new zzc();
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_NORMAL = 2;
    public static final int PRIORITY_UNKNOWN = 0;
    Bundle zzdu;
    private Map<String, String> zzdv;
    private Notification zzdw;

    public static class Builder {
        private final Bundle zzdu = new Bundle();
        private final Map<String, String> zzdv = new ArrayMap();

        public Builder(String str) {
            if (TextUtils.isEmpty(str)) {
                String str2 = "Invalid to: ";
                String valueOf = String.valueOf(str);
                throw new IllegalArgumentException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
            }
            this.zzdu.putString("google.to", str);
        }

        public RemoteMessage build() {
            Bundle bundle = new Bundle();
            for (Entry entry : this.zzdv.entrySet()) {
                bundle.putString((String) entry.getKey(), (String) entry.getValue());
            }
            bundle.putAll(this.zzdu);
            this.zzdu.remove("from");
            return new RemoteMessage(bundle);
        }

        public Builder addData(String str, String str2) {
            this.zzdv.put(str, str2);
            return this;
        }

        public Builder setData(Map<String, String> map) {
            this.zzdv.clear();
            this.zzdv.putAll(map);
            return this;
        }

        public Builder clearData() {
            this.zzdv.clear();
            return this;
        }

        public Builder setMessageId(String str) {
            this.zzdu.putString("google.message_id", str);
            return this;
        }

        public Builder setMessageType(String str) {
            this.zzdu.putString("message_type", str);
            return this;
        }

        public Builder setTtl(int i) {
            this.zzdu.putString("google.ttl", String.valueOf(i));
            return this;
        }

        public Builder setCollapseKey(String str) {
            this.zzdu.putString("collapse_key", str);
            return this;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MessagePriority {
    }

    public static class Notification {
        private final String tag;
        private final String zzdx;
        private final String zzdy;
        private final String[] zzdz;
        private final String zzea;
        private final String zzeb;
        private final String[] zzec;
        private final String zzed;
        private final String zzee;
        private final String zzef;
        private final String zzeg;
        private final String zzeh;
        private final Uri zzei;

        private Notification(Bundle bundle) {
            String str = "gcm.n.title";
            this.zzdx = zza.zza(bundle, str);
            this.zzdy = zza.zzb(bundle, str);
            this.zzdz = zze(bundle, str);
            String str2 = "gcm.n.body";
            this.zzea = zza.zza(bundle, str2);
            this.zzeb = zza.zzb(bundle, str2);
            this.zzec = zze(bundle, str2);
            this.zzed = zza.zza(bundle, "gcm.n.icon");
            this.zzee = zza.zzi(bundle);
            this.tag = zza.zza(bundle, "gcm.n.tag");
            this.zzef = zza.zza(bundle, "gcm.n.color");
            this.zzeg = zza.zza(bundle, "gcm.n.click_action");
            this.zzeh = zza.zza(bundle, "gcm.n.android_channel_id");
            this.zzei = zza.zzg(bundle);
        }

        private static String[] zze(Bundle bundle, String str) {
            Object[] zzc = zza.zzc(bundle, str);
            if (zzc == null) {
                return null;
            }
            String[] strArr = new String[zzc.length];
            for (int i = 0; i < zzc.length; i++) {
                strArr[i] = String.valueOf(zzc[i]);
            }
            return strArr;
        }

        public String getTitle() {
            return this.zzdx;
        }

        public String getTitleLocalizationKey() {
            return this.zzdy;
        }

        public String[] getTitleLocalizationArgs() {
            return this.zzdz;
        }

        public String getBody() {
            return this.zzea;
        }

        public String getBodyLocalizationKey() {
            return this.zzeb;
        }

        public String[] getBodyLocalizationArgs() {
            return this.zzec;
        }

        public String getIcon() {
            return this.zzed;
        }

        public String getSound() {
            return this.zzee;
        }

        public String getTag() {
            return this.tag;
        }

        public String getColor() {
            return this.zzef;
        }

        public String getClickAction() {
            return this.zzeg;
        }

        public String getChannelId() {
            return this.zzeh;
        }

        public Uri getLink() {
            return this.zzei;
        }
    }

    public RemoteMessage(Bundle bundle) {
        this.zzdu = bundle;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeBundle(parcel, 2, this.zzdu, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public final String getFrom() {
        return this.zzdu.getString("from");
    }

    public final String getTo() {
        return this.zzdu.getString("google.to");
    }

    public final Map<String, String> getData() {
        if (this.zzdv == null) {
            Bundle bundle = this.zzdu;
            ArrayMap arrayMap = new ArrayMap();
            for (String str : bundle.keySet()) {
                Object obj = bundle.get(str);
                if (obj instanceof String) {
                    String str2 = (String) obj;
                    if (!str.startsWith("google.") && !str.startsWith("gcm.") && !str.equals("from") && !str.equals("message_type") && !str.equals("collapse_key")) {
                        arrayMap.put(str, str2);
                    }
                }
            }
            this.zzdv = arrayMap;
        }
        return this.zzdv;
    }

    public final String getCollapseKey() {
        return this.zzdu.getString("collapse_key");
    }

    public final String getMessageId() {
        String string = this.zzdu.getString("google.message_id");
        return string == null ? this.zzdu.getString("message_id") : string;
    }

    public final String getMessageType() {
        return this.zzdu.getString("message_type");
    }

    public final long getSentTime() {
        Object obj = this.zzdu.get("google.sent_time");
        if (obj instanceof Long) {
            return ((Long) obj).longValue();
        }
        if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException unused) {
                String valueOf = String.valueOf(obj);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 19);
                sb.append("Invalid sent time: ");
                sb.append(valueOf);
                Log.w("FirebaseMessaging", sb.toString());
            }
        }
        return 0;
    }

    public final int getTtl() {
        Object obj = this.zzdu.get("google.ttl");
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException unused) {
                String valueOf = String.valueOf(obj);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 13);
                sb.append("Invalid TTL: ");
                sb.append(valueOf);
                Log.w("FirebaseMessaging", sb.toString());
            }
        }
        return 0;
    }

    public final int getOriginalPriority() {
        String string = this.zzdu.getString("google.original_priority");
        if (string == null) {
            string = this.zzdu.getString("google.priority");
        }
        return zzm(string);
    }

    public final int getPriority() {
        String string = this.zzdu.getString("google.delivered_priority");
        if (string == null) {
            if ("1".equals(this.zzdu.getString("google.priority_reduced"))) {
                return 2;
            }
            string = this.zzdu.getString("google.priority");
        }
        return zzm(string);
    }

    private static int zzm(String str) {
        if ("high".equals(str)) {
            return 1;
        }
        return "normal".equals(str) ? 2 : 0;
    }

    public final Notification getNotification() {
        if (this.zzdw == null && zza.zzf(this.zzdu)) {
            this.zzdw = new Notification(this.zzdu);
        }
        return this.zzdw;
    }

    public final Intent toIntent() {
        Intent intent = new Intent();
        intent.putExtras(this.zzdu);
        return intent;
    }
}
