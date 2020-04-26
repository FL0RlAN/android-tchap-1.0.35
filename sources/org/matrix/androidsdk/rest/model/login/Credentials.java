package org.matrix.androidsdk.rest.model.login;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import org.json.JSONException;
import org.json.JSONObject;
import org.matrix.androidsdk.rest.model.WellKnown;

public class Credentials {
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("device_id")
    public String deviceId;
    @SerializedName("home_server")
    @Deprecated
    public String homeServer;
    @SerializedName("refresh_token")
    public String refreshToken;
    @SerializedName("user_id")
    public String userId;
    @SerializedName("well_known")
    public WellKnown wellKnown;

    public JSONObject toJson() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("user_id", this.userId);
        jSONObject.put("home_server", this.homeServer);
        jSONObject.put("access_token", this.accessToken);
        jSONObject.put("refresh_token", TextUtils.isEmpty(this.refreshToken) ? JSONObject.NULL : this.refreshToken);
        jSONObject.put("device_id", this.deviceId);
        return jSONObject;
    }

    public static Credentials fromJson(JSONObject jSONObject) throws JSONException {
        Credentials credentials = new Credentials();
        credentials.userId = jSONObject.getString("user_id");
        credentials.homeServer = jSONObject.getString("home_server");
        credentials.accessToken = jSONObject.getString("access_token");
        String str = "device_id";
        if (jSONObject.has(str)) {
            credentials.deviceId = jSONObject.getString(str);
        }
        String str2 = "refresh_token";
        if (jSONObject.has(str2)) {
            try {
                credentials.refreshToken = jSONObject.getString(str2);
            } catch (Exception unused) {
                credentials.refreshToken = null;
            }
            return credentials;
        }
        throw new RuntimeException("refresh_token is required.");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Credentials{userId='");
        sb.append(this.userId);
        sb.append('\'');
        sb.append(", homeServer='");
        sb.append(this.homeServer);
        sb.append('\'');
        sb.append(", refreshToken.length='");
        String str = this.refreshToken;
        Object obj = "null";
        sb.append(str != null ? Integer.valueOf(str.length()) : obj);
        sb.append('\'');
        sb.append(", accessToken.length='");
        String str2 = this.accessToken;
        if (str2 != null) {
            obj = Integer.valueOf(str2.length());
        }
        sb.append(obj);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getUserId() {
        return this.userId;
    }

    public String getHomeServer() {
        return this.homeServer;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getDeviceId() {
        return this.deviceId;
    }
}
