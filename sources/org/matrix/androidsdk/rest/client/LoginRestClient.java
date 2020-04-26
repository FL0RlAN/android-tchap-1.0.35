package org.matrix.androidsdk.rest.client;

import android.os.Build;
import android.text.TextUtils;
import java.util.List;
import java.util.UUID;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.RestClient;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.UnsentEventsManager;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.rest.api.LoginApi;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback;
import org.matrix.androidsdk.rest.callback.RestAdapterCallback.RequestRetryCallBack;
import org.matrix.androidsdk.rest.model.Versions;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.matrix.androidsdk.rest.model.login.LoginFlow;
import org.matrix.androidsdk.rest.model.login.LoginFlowResponse;
import org.matrix.androidsdk.rest.model.login.LoginParams;
import org.matrix.androidsdk.rest.model.login.PasswordLoginParams;
import org.matrix.androidsdk.rest.model.login.RegistrationParams;
import org.matrix.androidsdk.rest.model.login.TokenLoginParams;
import retrofit2.Call;
import retrofit2.Response;

public class LoginRestClient extends RestClient<LoginApi> {
    public static final String LOGIN_FLOW_TYPE_CAS = "m.login.cas";
    public static final String LOGIN_FLOW_TYPE_DUMMY = "m.login.dummy";
    public static final String LOGIN_FLOW_TYPE_EMAIL_CODE = "m.login.email.code";
    public static final String LOGIN_FLOW_TYPE_EMAIL_IDENTITY = "m.login.email.identity";
    public static final String LOGIN_FLOW_TYPE_EMAIL_URL = "m.login.email.url";
    public static final String LOGIN_FLOW_TYPE_MSISDN = "m.login.msisdn";
    public static final String LOGIN_FLOW_TYPE_OAUTH2 = "m.login.oauth2";
    public static final String LOGIN_FLOW_TYPE_PASSWORD = "m.login.password";
    public static final String LOGIN_FLOW_TYPE_RECAPTCHA = "m.login.recaptcha";
    public static final String LOGIN_FLOW_TYPE_SSO = "m.login.sso";
    public static final String LOGIN_FLOW_TYPE_TERMS = "m.login.terms";

    public LoginRestClient(HomeServerConnectionConfig homeServerConnectionConfig) {
        super(homeServerConnectionConfig, LoginApi.class, "", JsonUtils.getGson(false));
    }

    public void getVersions(ApiCallback<Versions> apiCallback) {
        ((LoginApi) this.mApi).versions().enqueue(new RestAdapterCallback("getVersions", this.mUnsentEventsManager, apiCallback, null));
    }

    public void getSupportedLoginFlows(final ApiCallback<List<LoginFlow>> apiCallback) {
        Call login = ((LoginApi) this.mApi).login();
        final ApiCallback<List<LoginFlow>> apiCallback2 = apiCallback;
        AnonymousClass2 r1 = new RestAdapterCallback<LoginFlowResponse>("geLoginSupportedFlows", this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                LoginRestClient.this.getSupportedLoginFlows(apiCallback);
            }
        }) {
            public void success(LoginFlowResponse loginFlowResponse, Response response) {
                onEventSent();
                apiCallback2.onSuccess(loginFlowResponse.flows);
            }
        };
        login.enqueue(r1);
    }

    public void register(final RegistrationParams registrationParams, final ApiCallback<Credentials> apiCallback) {
        boolean isEmpty = TextUtils.isEmpty(registrationParams.password);
        Boolean valueOf = Boolean.valueOf(true);
        if (!isEmpty && TextUtils.isEmpty(registrationParams.initial_device_display_name)) {
            registrationParams.initial_device_display_name = Build.MODEL.trim();
            registrationParams.x_show_msisdn = valueOf;
        } else if (registrationParams.password == null && registrationParams.username == null && registrationParams.auth == null) {
            registrationParams.x_show_msisdn = valueOf;
        }
        ((LoginApi) this.mApi).register(registrationParams).enqueue(new RestAdapterCallback("register", this.mUnsentEventsManager, new SimpleApiCallback<Credentials>(apiCallback) {
            public void onSuccess(Credentials credentials) {
                LoginRestClient.this.setAccessToken(credentials.accessToken);
                apiCallback.onSuccess(credentials);
            }
        }, new RequestRetryCallBack() {
            public void onRetry() {
                LoginRestClient.this.register(registrationParams, apiCallback);
            }
        }));
    }

    public void loginWithUser(String str, String str2, ApiCallback<Credentials> apiCallback) {
        loginWithUser(str, str2, null, null, apiCallback);
    }

    public void loginWithUser(String str, String str2, String str3, String str4, ApiCallback<Credentials> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("loginWithUser : ");
        sb.append(str);
        String sb2 = sb.toString();
        PasswordLoginParams passwordLoginParams = new PasswordLoginParams();
        passwordLoginParams.setUserIdentifier(str, str2);
        passwordLoginParams.setDeviceName(str3);
        passwordLoginParams.setDeviceId(str4);
        login(passwordLoginParams, apiCallback, sb2);
    }

    public void loginWith3Pid(String str, String str2, String str3, ApiCallback<Credentials> apiCallback) {
        loginWith3Pid(str, str2, str3, null, null, apiCallback);
    }

    public void loginWith3Pid(String str, String str2, String str3, String str4, String str5, ApiCallback<Credentials> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("loginWith3pid : ");
        sb.append(str2);
        String sb2 = sb.toString();
        PasswordLoginParams passwordLoginParams = new PasswordLoginParams();
        passwordLoginParams.setThirdPartyIdentifier(str, str2, str3);
        passwordLoginParams.setDeviceName(str4);
        passwordLoginParams.setDeviceId(str5);
        login(passwordLoginParams, apiCallback, sb2);
    }

    public void loginWithPhoneNumber(String str, String str2, String str3, ApiCallback<Credentials> apiCallback) {
        loginWithPhoneNumber(str, str2, str3, null, null, apiCallback);
    }

    public void loginWithPhoneNumber(String str, String str2, String str3, String str4, String str5, ApiCallback<Credentials> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("loginWithPhoneNumber : ");
        sb.append(str);
        String sb2 = sb.toString();
        PasswordLoginParams passwordLoginParams = new PasswordLoginParams();
        passwordLoginParams.setPhoneIdentifier(str, str2, str3);
        passwordLoginParams.setDeviceName(str4);
        passwordLoginParams.setDeviceId(str5);
        login(passwordLoginParams, apiCallback, sb2);
    }

    public void login(LoginParams loginParams, ApiCallback<Credentials> apiCallback) {
        StringBuilder sb = new StringBuilder();
        sb.append("login with a ");
        sb.append(loginParams.getClass().getSimpleName());
        sb.append(" object");
        login(loginParams, apiCallback, sb.toString());
    }

    /* access modifiers changed from: private */
    public void login(final LoginParams loginParams, final ApiCallback<Credentials> apiCallback, final String str) {
        ((LoginApi) this.mApi).login(loginParams).enqueue(new RestAdapterCallback(str, this.mUnsentEventsManager, new SimpleApiCallback<Credentials>(apiCallback) {
            public void onSuccess(Credentials credentials) {
                LoginRestClient.this.setAccessToken(credentials.accessToken);
                apiCallback.onSuccess(credentials);
            }
        }, new RequestRetryCallBack() {
            public void onRetry() {
                LoginRestClient.this.login(loginParams, apiCallback, str);
            }
        }));
    }

    public void loginWithToken(String str, String str2, String str3, ApiCallback<Credentials> apiCallback) {
        loginWithToken(str, str2, UUID.randomUUID().toString(), str3, apiCallback);
    }

    public void loginWithToken(String str, String str2, String str3, String str4, final ApiCallback<Credentials> apiCallback) {
        TokenLoginParams tokenLoginParams = new TokenLoginParams();
        tokenLoginParams.user = str;
        tokenLoginParams.token = str2;
        tokenLoginParams.txn_id = str3;
        if (str4 == null || TextUtils.isEmpty(str4.trim())) {
            tokenLoginParams.initial_device_display_name = Build.MODEL.trim();
        } else {
            tokenLoginParams.initial_device_display_name = str4.trim();
        }
        Call login = ((LoginApi) this.mApi).login(tokenLoginParams);
        UnsentEventsManager unsentEventsManager = this.mUnsentEventsManager;
        AnonymousClass7 r2 = new SimpleApiCallback<Credentials>(apiCallback) {
            public void onSuccess(Credentials credentials) {
                LoginRestClient.this.setAccessToken(credentials.accessToken);
                apiCallback.onSuccess(credentials);
            }
        };
        final String str5 = str;
        final String str6 = str2;
        final String str7 = str3;
        final ApiCallback<Credentials> apiCallback2 = apiCallback;
        AnonymousClass8 r3 = new RequestRetryCallBack() {
            public void onRetry() {
                LoginRestClient.this.loginWithToken(str5, str6, str7, apiCallback2);
            }
        };
        login.enqueue(new RestAdapterCallback("loginWithPassword user", unsentEventsManager, r2, r3));
    }

    public void logout(final ApiCallback<Void> apiCallback) {
        ((LoginApi) this.mApi).logout().enqueue(new RestAdapterCallback("logout user", this.mUnsentEventsManager, apiCallback, new RequestRetryCallBack() {
            public void onRetry() {
                LoginRestClient.this.logout(apiCallback);
            }
        }));
    }
}
