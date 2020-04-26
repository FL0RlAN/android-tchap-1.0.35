package org.matrix.androidsdk.core.model;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MatrixError implements Serializable {
    public static final String BAD_JSON = "M_BAD_JSON";
    public static final String BAD_PAGINATION = "M_BAD_PAGINATION";
    public static final String FORBIDDEN = "M_FORBIDDEN";
    public static final String LIMIT_EXCEEDED = "M_LIMIT_EXCEEDED";
    public static final String LIMIT_TYPE_MAU = "monthly_active_user";
    public static final String LOGIN_EMAIL_URL_NOT_YET = "M_LOGIN_EMAIL_URL_NOT_YET";
    public static final String M_CONSENT_NOT_GIVEN = "M_CONSENT_NOT_GIVEN";
    public static final String NOT_FOUND = "M_NOT_FOUND";
    public static final String NOT_JSON = "M_NOT_JSON";
    public static final String NOT_SUPPORTED = "M_NOT_SUPPORTED";
    public static final String OLD_VERSION = "M_OLD_VERSION";
    public static final String PASSWORD_IN_DICTIONARY = "M_PASSWORD_IN_DICTIONARY";
    public static final String PASSWORD_NO_DIGIT = "M_PASSWORD_NO_DIGIT";
    public static final String PASSWORD_NO_LOWERCASE = "M_PASSWORD_NO_LOWERCASE";
    public static final String PASSWORD_NO_SYMBOL = "M_PASSWORD_NO_SYMBOL";
    public static final String PASSWORD_NO_UPPERCASE = "M_PASSWORD_NO_UPPERCASE";
    public static final String PASSWORD_TOO_SHORT = "M_PASSWORD_TOO_SHORT";
    public static final String RESOURCE_LIMIT_EXCEEDED = "M_RESOURCE_LIMIT_EXCEEDED";
    public static final String ROOM_IN_USE = "M_ROOM_IN_USE";
    public static final String SERVER_NOT_TRUSTED = "M_SERVER_NOT_TRUSTED";
    public static final String TERMS_NOT_SIGNED = "M_TERMS_NOT_SIGNED";
    public static final String THREEPID_AUTH_FAILED = "M_THREEPID_AUTH_FAILED";
    public static final String THREEPID_DENIED = "M_THREEPID_DENIED";
    public static final String THREEPID_IN_USE = "M_THREEPID_IN_USE";
    public static final String THREEPID_NOT_FOUND = "M_THREEPID_NOT_FOUND";
    public static final String TOO_LARGE = "M_TOO_LARGE";
    public static final String UNAUTHORIZED = "M_UNAUTHORIZED";
    public static final String UNKNOWN = "M_UNKNOWN";
    public static final String UNKNOWN_TOKEN = "M_UNKNOWN_TOKEN";
    public static final String UNRECOGNIZED = "M_UNRECOGNIZED";
    public static final String USER_IN_USE = "M_USER_IN_USE";
    public static final String WEAK_PASSWORD = "M_WEAK_PASSWORD";
    public static final String WRONG_ROOM_KEYS_VERSION = "M_WRONG_ROOM_KEYS_VERSION";
    public static final Set<String> mConfigurationErrorCodes = new HashSet(Arrays.asList(new String[]{UNKNOWN_TOKEN, OLD_VERSION}));
    @SerializedName("admin_contact")
    public String adminUri;
    @SerializedName("consent_uri")
    public String consentUri;
    public String errcode;
    public String error;
    @SerializedName("limit_type")
    public String limitType;
    public String mErrorBodyAsString;
    public String mReason;
    public Integer mStatus;
    public Integer retry_after_ms;

    public MatrixError() {
    }

    public MatrixError(String str, String str2) {
        this.errcode = str;
        this.error = str2;
    }

    public String getLocalizedMessage() {
        if (!TextUtils.isEmpty(this.error)) {
            return this.error;
        }
        if (!TextUtils.isEmpty(this.errcode)) {
            return this.errcode;
        }
        return !TextUtils.isEmpty(this.mErrorBodyAsString) ? this.mErrorBodyAsString : "";
    }

    public String getMessage() {
        return getLocalizedMessage();
    }

    public boolean isSupportedErrorCode() {
        if (!FORBIDDEN.equals(this.errcode)) {
            if (!UNKNOWN_TOKEN.equals(this.errcode)) {
                if (!BAD_JSON.equals(this.errcode)) {
                    if (!NOT_JSON.equals(this.errcode)) {
                        if (!NOT_FOUND.equals(this.errcode)) {
                            if (!LIMIT_EXCEEDED.equals(this.errcode)) {
                                if (!USER_IN_USE.equals(this.errcode)) {
                                    if (!ROOM_IN_USE.equals(this.errcode)) {
                                        if (!TOO_LARGE.equals(this.errcode)) {
                                            if (!BAD_PAGINATION.equals(this.errcode)) {
                                                if (!OLD_VERSION.equals(this.errcode)) {
                                                    if (!UNRECOGNIZED.equals(this.errcode)) {
                                                        if (!RESOURCE_LIMIT_EXCEEDED.equals(this.errcode)) {
                                                            return false;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public static boolean isConfigurationErrorCode(String str) {
        return str != null && mConfigurationErrorCodes.contains(str);
    }
}
