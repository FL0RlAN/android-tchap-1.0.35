package org.matrix.androidsdk.rest.model.login;

import android.os.Build;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;
import org.matrix.androidsdk.rest.client.LoginRestClient;

public class PasswordLoginParams extends LoginParams {
    public static final String IDENTIFIER_KEY_ADDRESS = "address";
    public static final String IDENTIFIER_KEY_COUNTRY = "country";
    public static final String IDENTIFIER_KEY_MEDIUM = "medium";
    public static final String IDENTIFIER_KEY_NUMBER = "number";
    public static final String IDENTIFIER_KEY_TYPE = "type";
    public static final String IDENTIFIER_KEY_TYPE_PHONE = "m.id.phone";
    public static final String IDENTIFIER_KEY_TYPE_THIRD_PARTY = "m.id.thirdparty";
    public static final String IDENTIFIER_KEY_TYPE_USER = "m.id.user";
    public static final String IDENTIFIER_KEY_USER = "user";
    public String address;
    public String device_id;
    public Map<String, Object> identifier;
    public String initial_device_display_name;
    public String medium;
    public String password;
    public String user;

    public void setUserIdentifier(String str, String str2) {
        this.identifier = new HashMap();
        this.identifier.put(IDENTIFIER_KEY_TYPE, IDENTIFIER_KEY_TYPE_USER);
        this.identifier.put(IDENTIFIER_KEY_USER, str);
        this.user = str;
        setOtherData(str2);
    }

    public void setThirdPartyIdentifier(String str, String str2, String str3) {
        this.identifier = new HashMap();
        this.identifier.put(IDENTIFIER_KEY_TYPE, IDENTIFIER_KEY_TYPE_THIRD_PARTY);
        this.identifier.put(IDENTIFIER_KEY_MEDIUM, str);
        this.identifier.put(IDENTIFIER_KEY_ADDRESS, str2);
        this.medium = str;
        this.address = str2;
        setOtherData(str3);
    }

    public void setPhoneIdentifier(String str, String str2, String str3) {
        this.identifier = new HashMap();
        this.identifier.put(IDENTIFIER_KEY_TYPE, IDENTIFIER_KEY_TYPE_PHONE);
        this.identifier.put(IDENTIFIER_KEY_NUMBER, str);
        this.identifier.put(IDENTIFIER_KEY_COUNTRY, str2);
        setOtherData(str3);
    }

    private void setOtherData(String str) {
        this.password = str;
        this.type = LoginRestClient.LOGIN_FLOW_TYPE_PASSWORD;
        this.initial_device_display_name = Build.MODEL.trim();
    }

    public void setDeviceName(String str) {
        if (str == null || TextUtils.isEmpty(str.trim())) {
            this.initial_device_display_name = Build.MODEL.trim();
        } else {
            this.initial_device_display_name = str.trim();
        }
    }

    public void setDeviceId(String str) {
        this.device_id = str;
    }
}
