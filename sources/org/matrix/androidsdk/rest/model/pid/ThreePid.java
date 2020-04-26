package org.matrix.androidsdk.rest.model.pid;

import android.content.Context;
import android.text.TextUtils;
import java.io.Serializable;
import java.util.UUID;
import org.matrix.androidsdk.R;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.client.ProfileRestClient;
import org.matrix.androidsdk.rest.client.ThirdPidRestClient;
import org.matrix.androidsdk.rest.model.RequestEmailValidationResponse;
import org.matrix.androidsdk.rest.model.RequestPhoneNumberValidationResponse;

public class ThreePid implements Serializable {
    public static final int AUTH_STATE_TOKEN_AUTHENTIFICATED = 4;
    public static final int AUTH_STATE_TOKEN_RECEIVED = 2;
    public static final int AUTH_STATE_TOKEN_REQUESTED = 1;
    public static final int AUTH_STATE_TOKEN_SUBMITTED = 3;
    public static final int AUTH_STATE_TOKEN_UNKNOWN = 0;
    public static final String MEDIUM_EMAIL = "email";
    public static final String MEDIUM_MSISDN = "msisdn";
    public String clientSecret;
    public String country;
    public String emailAddress;
    /* access modifiers changed from: private */
    public int mValidationState;
    public String medium;
    public String phoneNumber;
    public int sendAttempt;
    public String sid;

    public ThreePid(String str, String str2) {
        this.medium = str2;
        this.emailAddress = str;
        if (TextUtils.equals("email", str2) && !TextUtils.isEmpty(str)) {
            this.emailAddress = str.toLowerCase();
        }
        this.clientSecret = UUID.randomUUID().toString();
    }

    public ThreePid(String str, String str2, String str3) {
        String str4;
        this.medium = str3;
        this.phoneNumber = str;
        if (str2 == null) {
            str4 = "";
        } else {
            str4 = str2.toUpperCase();
        }
        this.country = str4;
        this.clientSecret = UUID.randomUUID().toString();
    }

    private void resetValidationParameters() {
        this.mValidationState = 0;
        this.clientSecret = UUID.randomUUID().toString();
        this.sendAttempt = 1;
        this.sid = null;
    }

    public void requestEmailValidationToken(ProfileRestClient profileRestClient, String str, boolean z, final ApiCallback<Void> apiCallback) {
        if (profileRestClient != null) {
            int i = this.mValidationState;
            if (i != 1) {
                if (i != 0) {
                    resetValidationParameters();
                }
                this.mValidationState = 1;
                profileRestClient.requestEmailValidationToken(this.emailAddress, this.clientSecret, this.sendAttempt, str, z, new ApiCallback<RequestEmailValidationResponse>() {
                    public void onSuccess(RequestEmailValidationResponse requestEmailValidationResponse) {
                        if (TextUtils.equals(requestEmailValidationResponse.clientSecret, ThreePid.this.clientSecret)) {
                            ThreePid.this.mValidationState = 2;
                            ThreePid.this.sid = requestEmailValidationResponse.sid;
                            apiCallback.onSuccess(null);
                        }
                    }

                    private void commonError() {
                        ThreePid.this.sendAttempt++;
                        ThreePid.this.mValidationState = 0;
                    }

                    public void onNetworkError(Exception exc) {
                        commonError();
                        apiCallback.onNetworkError(exc);
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        commonError();
                        apiCallback.onMatrixError(matrixError);
                    }

                    public void onUnexpectedError(Exception exc) {
                        commonError();
                        apiCallback.onUnexpectedError(exc);
                    }
                });
            }
        }
    }

    public void requestPhoneNumberValidationToken(ProfileRestClient profileRestClient, boolean z, final ApiCallback<Void> apiCallback) {
        if (profileRestClient != null) {
            int i = this.mValidationState;
            if (i != 1) {
                if (i != 0) {
                    resetValidationParameters();
                }
                this.mValidationState = 1;
                profileRestClient.requestPhoneNumberValidationToken(this.phoneNumber, this.country, this.clientSecret, this.sendAttempt, z, new ApiCallback<RequestPhoneNumberValidationResponse>() {
                    public void onSuccess(RequestPhoneNumberValidationResponse requestPhoneNumberValidationResponse) {
                        if (TextUtils.equals(requestPhoneNumberValidationResponse.clientSecret, ThreePid.this.clientSecret)) {
                            ThreePid.this.mValidationState = 2;
                            ThreePid.this.sid = requestPhoneNumberValidationResponse.sid;
                            apiCallback.onSuccess(null);
                        }
                    }

                    private void commonError() {
                        ThreePid.this.sendAttempt++;
                        ThreePid.this.mValidationState = 0;
                    }

                    public void onNetworkError(Exception exc) {
                        commonError();
                        apiCallback.onNetworkError(exc);
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        commonError();
                        apiCallback.onMatrixError(matrixError);
                    }

                    public void onUnexpectedError(Exception exc) {
                        commonError();
                        apiCallback.onUnexpectedError(exc);
                    }
                });
            }
        }
    }

    public void submitValidationToken(ThirdPidRestClient thirdPidRestClient, String str, String str2, String str3, ApiCallback<Boolean> apiCallback) {
        if (thirdPidRestClient != null) {
            thirdPidRestClient.submitValidationToken(this.medium, str, str2, str3, apiCallback);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0033  */
    public static String getMediumFriendlyName(String str, Context context) {
        char c;
        int hashCode = str.hashCode();
        if (hashCode != -1064943142) {
            if (hashCode == 96619420 && str.equals("email")) {
                c = 0;
                if (c == 0) {
                    return context.getString(R.string.medium_email);
                }
                if (c != 1) {
                    return "";
                }
                return context.getString(R.string.medium_phone_number);
            }
        } else if (str.equals(MEDIUM_MSISDN)) {
            c = 1;
            if (c == 0) {
            }
        }
        c = 65535;
        if (c == 0) {
        }
    }
}
