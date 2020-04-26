package org.matrix.androidsdk.rest.client;

import android.text.TextUtils;
import im.vector.util.UrlUtilKt;
import java.util.List;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.UnsentEventsManager;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.rest.api.ProfileApi;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback.RequestRetryCallBack;
import org.matrix.androidsdk.rest.model.ChangePasswordParams;
import org.matrix.androidsdk.rest.model.DeactivateAccountParams;
import org.matrix.androidsdk.rest.model.ForgetPasswordParams;
import org.matrix.androidsdk.rest.model.ForgetPasswordResponse;
import org.matrix.androidsdk.rest.model.RequestEmailValidationParams;
import org.matrix.androidsdk.rest.model.RequestEmailValidationResponse;
import org.matrix.androidsdk.rest.model.RequestPhoneNumberValidationParams;
import org.matrix.androidsdk.rest.model.RequestPhoneNumberValidationResponse;
import org.matrix.androidsdk.rest.model.ThreePidCreds;
import org.matrix.androidsdk.rest.model.User;
import org.matrix.androidsdk.rest.model.login.AuthParamsEmailIdentity;
import org.matrix.androidsdk.rest.model.login.AuthParamsLoginPassword;
import org.matrix.androidsdk.rest.model.login.ThreePidCredentials;
import org.matrix.androidsdk.rest.model.login.TokenRefreshParams;
import org.matrix.androidsdk.rest.model.login.TokenRefreshResponse;
import org.matrix.androidsdk.rest.model.pid.AccountThreePidsResponse;
import org.matrix.androidsdk.rest.model.pid.AddThreePidsParams;
import org.matrix.androidsdk.rest.model.pid.DeleteThreePidParams;
import org.matrix.androidsdk.rest.model.pid.ThirdPartyIdentifier;
import org.matrix.androidsdk.rest.model.pid.ThreePid;
import retrofit2.Call;
import retrofit2.Response;

public class ProfileRestClient extends RestClient<ProfileApi> {
    private static final String LOG_TAG = ProfileRestClient.class.getSimpleName();

    public ProfileRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        super(homeServerConnectionConfig, ProfileApi.class, "", JsonUtils.getGson(false));
    }

    public void displayname(final String str, final ApiCallback<String> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("display name userId : ");
        sb.append(str);
        String sb2 = sb.toString();
        Call displayname = ((ProfileApi) this.mApi).displayname(str);
        final ApiCallback<String> apiCallback2 = apiCallback;
        AnonymousClass2 r2 = new RestAdapterCallback<User>(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                ProfileRestClient.this.displayname(str, apiCallback);
            }
        }) {
            public void success(User user, Response<User> response) {
                onEventSent();
                apiCallback2.onSuccess(user.displayname);
            }
        };
        displayname.enqueue(r2);
    }

    public void updateDisplayname(final String str, final String str2, final ApiCallback<Void> apiCallback) {
        User user = new User();
        user.displayname = str2;
        ((ProfileApi) this.mApi).displayname(str, user).enqueue(new RestAdapterCallback("update display name", this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                ProfileRestClient.this.updateDisplayname(str, str2, apiCallback);
            }
        }));
    }

    public void avatarUrl(final String str, final ApiCallback<String> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("avatarUrl userId : ");
        sb.append(str);
        String sb2 = sb.toString();
        Call avatarUrl = ((ProfileApi) this.mApi).avatarUrl(str);
        final ApiCallback<String> apiCallback2 = apiCallback;
        AnonymousClass5 r2 = new RestAdapterCallback<User>(sb2, this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                ProfileRestClient.this.avatarUrl(str, apiCallback);
            }
        }) {
            public void success(User user, Response response) {
                onEventSent();
                apiCallback2.onSuccess(user.getAvatarUrl());
            }
        };
        avatarUrl.enqueue(r2);
    }

    public void updateAvatarUrl(final String str, final String str2, final ApiCallback<Void> apiCallback) {
        User user = new User();
        user.setAvatarUrl(str2);
        ((ProfileApi) this.mApi).avatarUrl(str, user).enqueue(new RestAdapterCallback("updateAvatarUrl", this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                ProfileRestClient.this.updateAvatarUrl(str, str2, apiCallback);
            }
        }));
    }

    public void updatePassword(String str, String str2, String str3, ApiCallback<Void> apiCallback) {
        ChangePasswordParams changePasswordParams = new ChangePasswordParams();
        AuthParamsLoginPassword authParamsLoginPassword = new AuthParamsLoginPassword();
        authParamsLoginPassword.user = str;
        authParamsLoginPassword.password = str2;
        changePasswordParams.auth = authParamsLoginPassword;
        changePasswordParams.new_password = str3;
        Call updatePassword = ((ProfileApi) this.mApi).updatePassword(changePasswordParams);
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final String str4 = str;
        final String str5 = str2;
        final String str6 = str3;
        final ApiCallback<Void> apiCallback2 = apiCallback;
        AnonymousClass7 r3 = new RequestRetryCallBack() {
            public void onRetry() {
                ProfileRestClient.this.updatePassword(str4, str5, str6, apiCallback2);
            }
        };
        updatePassword.enqueue(new RestAdapterCallback("update password", unsentEventsManager, apiCallback, r3));
    }

    public void resetPassword(final String str, final ThreePidCredentials threePidCredentials, final ApiCallback<Void> apiCallback) {
        ChangePasswordParams changePasswordParams = new ChangePasswordParams();
        AuthParamsEmailIdentity authParamsEmailIdentity = new AuthParamsEmailIdentity();
        authParamsEmailIdentity.threePidCredentials = threePidCredentials;
        changePasswordParams.auth = authParamsEmailIdentity;
        changePasswordParams.new_password = str;
        ((ProfileApi) this.mApi).updatePassword(changePasswordParams).enqueue(new RestAdapterCallback("Reset password", this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                ProfileRestClient.this.resetPassword(str, threePidCredentials, apiCallback);
            }
        }));
    }

    public void forgetPassword(final String str, final ApiCallback<ThreePid> apiCallback) {
        if (!TextUtils.isEmpty(str)) {
            final ThreePid threePid = new ThreePid(str, "email");
            ForgetPasswordParams forgetPasswordParams = new ForgetPasswordParams();
            forgetPasswordParams.email = str;
            forgetPasswordParams.client_secret = threePid.clientSecret;
            forgetPasswordParams.send_attempt = Integer.valueOf(1);
            forgetPasswordParams.id_server = this.mHsConfig.getIdentityServerUri().getHost();
            Call forgetPassword = ((ProfileApi) this.mApi).forgetPassword(forgetPasswordParams);
            final ApiCallback<ThreePid> apiCallback2 = apiCallback;
            AnonymousClass10 r1 = new RestAdapterCallback<ForgetPasswordResponse>("forget password", this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
                public void onRetry() {
                    ProfileRestClient.this.forgetPassword(str, apiCallback);
                }
            }) {
                public void success(ForgetPasswordResponse forgetPasswordResponse, Response response) {
                    onEventSent();
                    threePid.sid = forgetPasswordResponse.sid;
                    apiCallback2.onSuccess(threePid);
                }
            };
            forgetPassword.enqueue(r1);
        }
    }

    public void deactivateAccount(String str, String str2, boolean z, ApiCallback<Void> apiCallback) {
        DeactivateAccountParams deactivateAccountParams = new DeactivateAccountParams();
        deactivateAccountParams.auth = new AuthParamsLoginPassword();
        deactivateAccountParams.auth.user = str;
        deactivateAccountParams.auth.password = str2;
        deactivateAccountParams.erase = z;
        Call deactivate = ((ProfileApi) this.mApi).deactivate(deactivateAccountParams);
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final String str3 = str;
        final String str4 = str2;
        final boolean z2 = z;
        final ApiCallback<Void> apiCallback2 = apiCallback;
        AnonymousClass11 r3 = new RequestRetryCallBack() {
            public void onRetry() {
                ProfileRestClient.this.deactivateAccount(str3, str4, z2, apiCallback2);
            }
        };
        deactivate.enqueue(new RestAdapterCallback("deactivate account", unsentEventsManager, apiCallback, r3));
    }

    public void refreshTokens(String str, final ApiCallback<TokenRefreshResponse> apiCallback) {
        TokenRefreshParams tokenRefreshParams = new TokenRefreshParams();
        tokenRefreshParams.refresh_token = str;
        Call call = ((ProfileApi) this.mApi).tokenRefresh(tokenRefreshParams);
        AnonymousClass13 r0 = new RestAdapterCallback<TokenRefreshResponse>("refreshTokens", this.mUnsentEventsManager, new SimpleApiCallback<TokenRefreshResponse>(apiCallback) {
            public void onSuccess(TokenRefreshResponse tokenRefreshResponse) {
                ProfileRestClient.this.setAccessToken(tokenRefreshResponse.accessToken);
                ApiCallback apiCallback = apiCallback;
                if (apiCallback != null) {
                    apiCallback.onSuccess(tokenRefreshResponse);
                }
            }
        }, null) {
        };
        call.enqueue(r0);
    }

    public void threePIDs(ApiCallback<List<ThirdPartyIdentifier>> apiCallback) {
        Call threePIDs = ((ProfileApi) this.mApi).threePIDs();
        final ApiCallback<List<ThirdPartyIdentifier>> apiCallback2 = apiCallback;
        AnonymousClass14 r1 = new RestAdapterCallback<AccountThreePidsResponse>("threePIDs", this.mUnsentEventsManager, apiCallback, null) {
            public void success(AccountThreePidsResponse accountThreePidsResponse, Response<AccountThreePidsResponse> response) {
                onEventSent();
                ApiCallback apiCallback = apiCallback2;
                if (apiCallback != null) {
                    apiCallback.onSuccess(accountThreePidsResponse.threepids);
                }
            }
        };
        threePIDs.enqueue(r1);
    }

    public void requestEmailValidationToken(String str, String str2, int i, String str3, boolean z, ApiCallback<RequestEmailValidationResponse> apiCallback) {
        RequestEmailValidationParams requestEmailValidationParams = new RequestEmailValidationParams();
        requestEmailValidationParams.email = str;
        requestEmailValidationParams.clientSecret = str2;
        requestEmailValidationParams.sendAttempt = Integer.valueOf(i);
        requestEmailValidationParams.id_server = this.mHsConfig.getIdentityServerUri().getHost();
        String str4 = str3;
        if (!TextUtils.isEmpty(str3)) {
            requestEmailValidationParams.next_link = str4;
        }
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final String str5 = str;
        final String str6 = str2;
        final int i2 = i;
        final String str7 = str3;
        final boolean z2 = z;
        final ApiCallback<RequestEmailValidationResponse> apiCallback2 = apiCallback;
        AnonymousClass15 r0 = new RequestRetryCallBack() {
            public void onRetry() {
                ProfileRestClient.this.requestEmailValidationToken(str5, str6, i2, str7, z2, apiCallback2);
            }
        };
        final String str8 = str;
        final String str9 = str2;
        final int i3 = i;
        final ApiCallback<RequestEmailValidationResponse> apiCallback3 = apiCallback;
        AnonymousClass16 r02 = new RestAdapterCallback<RequestEmailValidationResponse>("requestEmailValidationToken", unsentEventsManager, apiCallback, r0) {
            public void success(RequestEmailValidationResponse requestEmailValidationResponse, Response response) {
                onEventSent();
                requestEmailValidationResponse.email = str8;
                requestEmailValidationResponse.clientSecret = str9;
                requestEmailValidationResponse.sendAttempt = Integer.valueOf(i3);
                apiCallback3.onSuccess(requestEmailValidationResponse);
            }
        };
        if (z) {
            ((ProfileApi) this.mApi).requestEmailValidationForRegistration(requestEmailValidationParams).enqueue(r02);
        } else {
            ((ProfileApi) this.mApi).requestEmailValidation(requestEmailValidationParams).enqueue(r02);
        }
    }

    public void requestPhoneNumberValidationToken(String str, String str2, String str3, int i, boolean z, ApiCallback<RequestPhoneNumberValidationResponse> apiCallback) {
        RequestPhoneNumberValidationParams requestPhoneNumberValidationParams = new RequestPhoneNumberValidationParams();
        final String str4 = str;
        requestPhoneNumberValidationParams.phone_number = str4;
        final String str5 = str2;
        requestPhoneNumberValidationParams.country = str5;
        requestPhoneNumberValidationParams.clientSecret = str3;
        requestPhoneNumberValidationParams.sendAttempt = Integer.valueOf(i);
        requestPhoneNumberValidationParams.id_server = this.mHsConfig.getIdentityServerUri().getHost();
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        final String str6 = str3;
        final int i2 = i;
        final boolean z2 = z;
        final ApiCallback<RequestPhoneNumberValidationResponse> apiCallback2 = apiCallback;
        AnonymousClass17 r0 = new RequestRetryCallBack() {
            public void onRetry() {
                ProfileRestClient.this.requestPhoneNumberValidationToken(str4, str5, str6, i2, z2, apiCallback2);
            }
        };
        final String str7 = str3;
        final int i3 = i;
        final ApiCallback<RequestPhoneNumberValidationResponse> apiCallback3 = apiCallback;
        AnonymousClass18 r02 = new RestAdapterCallback<RequestPhoneNumberValidationResponse>("requestPhoneNumberValidationToken", unsentEventsManager, apiCallback, r0) {
            public void success(RequestPhoneNumberValidationResponse requestPhoneNumberValidationResponse, Response response) {
                onEventSent();
                requestPhoneNumberValidationResponse.clientSecret = str7;
                requestPhoneNumberValidationResponse.sendAttempt = Integer.valueOf(i3);
                apiCallback3.onSuccess(requestPhoneNumberValidationResponse);
            }
        };
        if (z) {
            ((ProfileApi) this.mApi).requestPhoneNumberValidationForRegistration(requestPhoneNumberValidationParams).enqueue(r02);
        } else {
            ((ProfileApi) this.mApi).requestPhoneNumberValidation(requestPhoneNumberValidationParams).enqueue(r02);
        }
    }

    public void add3PID(final ThreePid threePid, final boolean z, final ApiCallback<Void> apiCallback) {
        AddThreePidsParams addThreePidsParams = new AddThreePidsParams();
        addThreePidsParams.three_pid_creds = new ThreePidCreds();
        String uri = this.mHsConfig.getIdentityServerUri().toString();
        if (uri.startsWith("http://")) {
            uri = uri.substring(7);
        } else if (uri.startsWith(UrlUtilKt.HTTPS_SCHEME)) {
            uri = uri.substring(8);
        }
        addThreePidsParams.three_pid_creds.id_server = uri;
        addThreePidsParams.three_pid_creds.sid = threePid.sid;
        addThreePidsParams.three_pid_creds.client_secret = threePid.clientSecret;
        addThreePidsParams.bind = Boolean.valueOf(z);
        ((ProfileApi) this.mApi).add3PID(addThreePidsParams).enqueue(new RestAdapterCallback("add3PID", this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                ProfileRestClient.this.add3PID(threePid, z, apiCallback);
            }
        }));
    }

    public void delete3PID(final ThirdPartyIdentifier thirdPartyIdentifier, final ApiCallback<Void> apiCallback) {
        DeleteThreePidParams deleteThreePidParams = new DeleteThreePidParams();
        deleteThreePidParams.medium = thirdPartyIdentifier.medium;
        deleteThreePidParams.address = thirdPartyIdentifier.address;
        ((ProfileApi) this.mApi).delete3PID(deleteThreePidParams).enqueue(new RestAdapterCallback("delete3PID", this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                ProfileRestClient.this.delete3PID(thirdPartyIdentifier, apiCallback);
            }
        }));
    }
}
