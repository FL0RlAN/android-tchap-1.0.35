package org.matrix.androidsdk.crypto.data;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MXDeviceInfo implements Serializable {
    public static final int DEVICE_VERIFICATION_BLOCKED = 2;
    public static final int DEVICE_VERIFICATION_UNKNOWN = -1;
    public static final int DEVICE_VERIFICATION_UNVERIFIED = 0;
    public static final int DEVICE_VERIFICATION_VERIFIED = 1;
    private static final long serialVersionUID = 20129670646382964L;
    public List<String> algorithms;
    @SerializedName("device_id")
    public String deviceId;
    public Map<String, String> keys;
    public int mVerified = -1;
    public Map<String, Map<String, String>> signatures;
    public Map<String, Object> unsigned;
    @SerializedName("user_id")
    public String userId;

    public MXDeviceInfo() {
    }

    public MXDeviceInfo(String str) {
        this.deviceId = str;
    }

    public boolean isUnknown() {
        return this.mVerified == -1;
    }

    public boolean isVerified() {
        return this.mVerified == 1;
    }

    public boolean isUnverified() {
        return this.mVerified == 0;
    }

    public boolean isBlocked() {
        return this.mVerified == 2;
    }

    public String fingerprint() {
        if (this.keys == null || TextUtils.isEmpty(this.deviceId)) {
            return null;
        }
        Map<String, String> map = this.keys;
        StringBuilder sb = new StringBuilder();
        sb.append("ed25519:");
        sb.append(this.deviceId);
        return (String) map.get(sb.toString());
    }

    public String identityKey() {
        if (this.keys == null || TextUtils.isEmpty(this.deviceId)) {
            return null;
        }
        Map<String, String> map = this.keys;
        StringBuilder sb = new StringBuilder();
        sb.append("curve25519:");
        sb.append(this.deviceId);
        return (String) map.get(sb.toString());
    }

    public String displayName() {
        Map<String, Object> map = this.unsigned;
        if (map != null) {
            return (String) map.get("device_display_name");
        }
        return null;
    }

    public Map<String, Object> signalableJSONDictionary() {
        HashMap hashMap = new HashMap();
        hashMap.put("device_id", this.deviceId);
        String str = this.userId;
        if (str != null) {
            hashMap.put("user_id", str);
        }
        List<String> list = this.algorithms;
        if (list != null) {
            hashMap.put("algorithms", list);
        }
        Map<String, String> map = this.keys;
        if (map != null) {
            hashMap.put("keys", map);
        }
        return hashMap;
    }

    public Map<String, Object> JSONDictionary() {
        HashMap hashMap = new HashMap();
        hashMap.put("device_id", this.deviceId);
        String str = this.userId;
        if (str != null) {
            hashMap.put("user_id", str);
        }
        List<String> list = this.algorithms;
        if (list != null) {
            hashMap.put("algorithms", list);
        }
        Map<String, String> map = this.keys;
        if (map != null) {
            hashMap.put("keys", map);
        }
        Map<String, Map<String, String>> map2 = this.signatures;
        if (map2 != null) {
            hashMap.put("signatures", map2);
        }
        Map<String, Object> map3 = this.unsigned;
        if (map3 != null) {
            hashMap.put("unsigned", map3);
        }
        return hashMap;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MXDeviceInfo ");
        sb.append(this.userId);
        sb.append(":");
        sb.append(this.deviceId);
        return sb.toString();
    }
}
