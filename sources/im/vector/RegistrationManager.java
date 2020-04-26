package im.vector;

import android.content.Context;
import android.text.TextUtils;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.UnrecognizedCertHandler.Callback;
import im.vector.util.UrlUtilKt;
import java.util.Map;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.client.LoginRestClient;
import org.matrix.androidsdk.rest.client.ProfileRestClient;
import org.matrix.androidsdk.rest.client.ThirdPidRestClient;
import org.matrix.androidsdk.rest.model.login.AuthParams;
import org.matrix.androidsdk.rest.model.login.AuthParamsCaptcha;
import org.matrix.androidsdk.rest.model.login.AuthParamsThreePid;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.matrix.androidsdk.rest.model.login.RegistrationFlowResponse;
import org.matrix.androidsdk.rest.model.login.RegistrationParams;
import org.matrix.androidsdk.rest.model.pid.ThreePid;
import org.matrix.androidsdk.ssl.CertUtil;
import org.matrix.androidsdk.ssl.UnrecognizedCertificateException;

public class RegistrationManager {
    private static final String ERROR_MISSING_STAGE = "ERROR_MISSING_STAGE";
    private static final String JSON_KEY_CAPTCHA_RESPONSE = "response";
    private static final String JSON_KEY_CLIENT_SECRET = "client_secret";
    private static final String JSON_KEY_ID_SERVER = "id_server";
    private static final String JSON_KEY_PUBLIC_KEY = "public_key";
    private static final String JSON_KEY_SESSION = "session";
    private static final String JSON_KEY_SID = "sid";
    private static final String JSON_KEY_THREEPID_CREDS = "threepid_creds";
    private static final String JSON_KEY_TYPE = "type";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = RegistrationManager.class.getSimpleName();
    private static volatile RegistrationManager sInstance;
    private String mCaptchaResponse;
    /* access modifiers changed from: private */
    public ThreePid mEmail;
    /* access modifiers changed from: private */
    public HomeServerConnectionConfig mHsConfig;
    private LoginRestClient mLoginRestClient;
    private String mPassword;
    /* access modifiers changed from: private */
    public ThreePid mPhoneNumber;
    private ProfileRestClient mProfileRestClient;
    private RegistrationFlowResponse mRegistrationResponse;
    /* access modifiers changed from: private */
    public boolean mShowThreePidWarning;
    private ThirdPidRestClient mThirdPidRestClient;
    private String mUsername;

    private interface InternalRegistrationListener {
        void onRegistrationFailed(String str);

        void onRegistrationSuccess();

        void onResourceLimitExceeded(MatrixError matrixError);

        void onWaitingEmailValidation();
    }

    public interface RegistrationListener {
        void onRegistrationFailed(String str);

        void onRegistrationSuccess(String str);

        void onResourceLimitExceeded(MatrixError matrixError);

        void onThreePidRequestFailed(String str);

        void onWaitingCaptcha();

        void onWaitingEmailValidation();
    }

    public interface ThreePidRequestListener {
        void onThreePidRequestFailed(int i);

        void onThreePidRequested(ThreePid threePid);
    }

    public interface ThreePidValidationListener {
        void onThreePidValidated(boolean z);
    }

    private boolean isCaptchaRequired() {
        return false;
    }

    public static RegistrationManager getInstance() {
        if (sInstance == null) {
            sInstance = new RegistrationManager();
        }
        return sInstance;
    }

    private RegistrationManager() {
    }

    public void resetSingleton() {
        this.mHsConfig = null;
        this.mLoginRestClient = null;
        this.mThirdPidRestClient = null;
        this.mProfileRestClient = null;
        this.mRegistrationResponse = null;
        this.mUsername = null;
        this.mPassword = null;
        this.mEmail = null;
        this.mPhoneNumber = null;
        this.mCaptchaResponse = null;
        this.mShowThreePidWarning = false;
    }

    public void setHsConfig(HomeServerConnectionConfig homeServerConnectionConfig) {
        this.mHsConfig = homeServerConnectionConfig;
        this.mLoginRestClient = null;
        this.mThirdPidRestClient = null;
        this.mProfileRestClient = null;
    }

    public void setAccountData(String str, String str2) {
        this.mUsername = str;
        this.mPassword = str2;
    }

    public void setCaptchaResponse(String str) {
        this.mCaptchaResponse = str;
    }

    public void setSupportedRegistrationFlows(RegistrationFlowResponse registrationFlowResponse) {
        if (registrationFlowResponse != null) {
            this.mRegistrationResponse = registrationFlowResponse;
        }
    }

    public void attemptRegistration(final Context context, final RegistrationListener registrationListener) {
        AuthParams authParams;
        RegistrationFlowResponse registrationFlowResponse = this.mRegistrationResponse;
        String str = "";
        if (registrationFlowResponse == null || TextUtils.isEmpty(registrationFlowResponse.session)) {
            Log.e(LOG_TAG, "## attemptRegistration(): mRegistrationResponse is null or session is null");
            registrationListener.onRegistrationFailed(str);
        } else {
            ThreePid threePid = this.mPhoneNumber;
            String str2 = LoginRestClient.LOGIN_FLOW_TYPE_RECAPTCHA;
            String str3 = LoginRestClient.LOGIN_FLOW_TYPE_EMAIL_IDENTITY;
            String str4 = LoginRestClient.LOGIN_FLOW_TYPE_MSISDN;
            if (threePid != null && !isCompleted(str4) && !TextUtils.isEmpty(this.mPhoneNumber.sid)) {
                authParams = getThreePidAuthParams(this.mPhoneNumber.clientSecret, this.mHsConfig.getIdentityServerUri().getHost(), this.mPhoneNumber.sid, str4);
                str = str4;
            } else if (this.mEmail == null || isCompleted(str3)) {
                if (TextUtils.isEmpty(this.mCaptchaResponse) || isCompleted(str2)) {
                    authParams = null;
                } else {
                    authParams = getCaptchaAuthParams(this.mCaptchaResponse);
                    str = str2;
                }
            } else if (TextUtils.isEmpty(this.mEmail.sid)) {
                Log.d(LOG_TAG, "attemptRegistration: request email validation");
                requestValidationToken(this.mEmail, new ThreePidRequestListener() {
                    public void onThreePidRequested(ThreePid threePid) {
                        if (!TextUtils.isEmpty(threePid.sid)) {
                            RegistrationManager.this.attemptRegistration(context, registrationListener);
                        } else {
                            registrationListener.onThreePidRequestFailed(context.getString(R.string.tchap_error_message_default));
                        }
                    }

                    public void onThreePidRequestFailed(int i) {
                        registrationListener.onThreePidRequestFailed(context.getString(i));
                    }
                });
                return;
            } else {
                authParams = getThreePidAuthParams(this.mEmail.clientSecret, this.mHsConfig.getIdentityServerUri().getHost(), this.mEmail.sid, str3);
                str = str3;
            }
            if (TextUtils.equals(str, str4) && this.mEmail != null && !isCaptchaRequired()) {
                this.mShowThreePidWarning = true;
                this.mEmail = null;
            }
            RegistrationParams registrationParams = new RegistrationParams();
            if (!str.equals(str2)) {
                String str5 = this.mUsername;
                if (str5 != null) {
                    registrationParams.username = str5;
                }
                String str6 = this.mPassword;
                if (str6 != null) {
                    registrationParams.password = str6;
                }
                boolean z = false;
                registrationParams.bind_email = Boolean.valueOf(this.mEmail != null);
                if (this.mPhoneNumber != null) {
                    z = true;
                }
                registrationParams.bind_msisdn = Boolean.valueOf(z);
            }
            if (authParams != null) {
                authParams.session = this.mRegistrationResponse.session;
                registrationParams.auth = authParams;
            }
            register(context, registrationParams, new InternalRegistrationListener() {
                public void onRegistrationSuccess() {
                    if (RegistrationManager.this.mShowThreePidWarning) {
                        registrationListener.onRegistrationSuccess(context.getString(R.string.auth_threepid_warning_message));
                    } else {
                        registrationListener.onRegistrationSuccess(null);
                    }
                }

                public void onWaitingEmailValidation() {
                    registrationListener.onWaitingEmailValidation();
                }

                public void onRegistrationFailed(String str) {
                    if (!TextUtils.equals(RegistrationManager.ERROR_MISSING_STAGE, str)) {
                        registrationListener.onRegistrationFailed(str);
                    } else if (RegistrationManager.this.mPhoneNumber != null && !RegistrationManager.this.isCompleted(LoginRestClient.LOGIN_FLOW_TYPE_MSISDN)) {
                        registrationListener.onRegistrationFailed("");
                    } else if (RegistrationManager.this.mEmail == null || RegistrationManager.this.isCompleted(LoginRestClient.LOGIN_FLOW_TYPE_EMAIL_IDENTITY)) {
                        registrationListener.onWaitingCaptcha();
                    } else {
                        RegistrationManager.this.attemptRegistration(context, registrationListener);
                    }
                }

                public void onResourceLimitExceeded(MatrixError matrixError) {
                    registrationListener.onResourceLimitExceeded(matrixError);
                }
            });
        }
    }

    public void registerAfterEmailValidation(Context context, String str, String str2, String str3, String str4, final RegistrationListener registrationListener) {
        Log.d(LOG_TAG, "registerAfterEmailValidation");
        RegistrationFlowResponse registrationFlowResponse = this.mRegistrationResponse;
        if (registrationFlowResponse != null) {
            registrationFlowResponse.session = str4;
        }
        RegistrationParams registrationParams = new RegistrationParams();
        registrationParams.auth = getThreePidAuthParams(str, UrlUtilKt.removeUrlScheme(str3), str2, LoginRestClient.LOGIN_FLOW_TYPE_EMAIL_IDENTITY);
        registrationParams.auth.session = str4;
        this.mUsername = null;
        this.mPassword = null;
        clearThreePid();
        register(context, registrationParams, new InternalRegistrationListener() {
            public void onWaitingEmailValidation() {
            }

            public void onRegistrationSuccess() {
                registrationListener.onRegistrationSuccess(null);
            }

            public void onRegistrationFailed(String str) {
                if (TextUtils.equals(RegistrationManager.ERROR_MISSING_STAGE, str)) {
                    registrationListener.onWaitingCaptcha();
                } else {
                    registrationListener.onRegistrationFailed(str);
                }
            }

            public void onResourceLimitExceeded(MatrixError matrixError) {
                registrationListener.onResourceLimitExceeded(matrixError);
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean isCompleted(String str) {
        RegistrationFlowResponse registrationFlowResponse = this.mRegistrationResponse;
        return (registrationFlowResponse == null || registrationFlowResponse.completed == null || !this.mRegistrationResponse.completed.contains(str)) ? false : true;
    }

    public void submitValidationToken(String str, ThreePid threePid, final ThreePidValidationListener threePidValidationListener) {
        if (getThirdPidRestClient() != null) {
            threePid.submitValidationToken(getThirdPidRestClient(), str, threePid.clientSecret, threePid.sid, new ApiCallback<Boolean>() {
                public void onSuccess(Boolean bool) {
                    threePidValidationListener.onThreePidValidated(bool.booleanValue());
                }

                public void onNetworkError(Exception exc) {
                    threePidValidationListener.onThreePidValidated(false);
                }

                public void onMatrixError(MatrixError matrixError) {
                    threePidValidationListener.onThreePidValidated(false);
                }

                public void onUnexpectedError(Exception exc) {
                    threePidValidationListener.onThreePidValidated(false);
                }
            });
        }
    }

    public String getCaptchaPublicKey() {
        RegistrationFlowResponse registrationFlowResponse = this.mRegistrationResponse;
        if (!(registrationFlowResponse == null || registrationFlowResponse.params == null)) {
            Object obj = this.mRegistrationResponse.params.get(LoginRestClient.LOGIN_FLOW_TYPE_RECAPTCHA);
            if (obj != null) {
                try {
                    return (String) ((Map) obj).get(JSON_KEY_PUBLIC_KEY);
                } catch (Exception e) {
                    String str = LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("getCaptchaPublicKey: ");
                    sb.append(e.getLocalizedMessage());
                    Log.e(str, sb.toString(), e);
                }
            }
        }
        return null;
    }

    public void addEmailThreePid(ThreePid threePid) {
        this.mEmail = threePid;
    }

    public ThreePid getEmailThreePid() {
        return this.mEmail;
    }

    public void addPhoneNumberThreePid(String str, String str2, ThreePidRequestListener threePidRequestListener) {
        requestValidationToken(new ThreePid(str, str2, ThreePid.MEDIUM_MSISDN), threePidRequestListener);
    }

    public void clearThreePid() {
        this.mEmail = null;
        this.mPhoneNumber = null;
        this.mShowThreePidWarning = false;
    }

    private LoginRestClient getLoginRestClient() {
        if (this.mLoginRestClient == null) {
            HomeServerConnectionConfig homeServerConnectionConfig = this.mHsConfig;
            if (homeServerConnectionConfig != null) {
                this.mLoginRestClient = new LoginRestClient(homeServerConnectionConfig);
            }
        }
        return this.mLoginRestClient;
    }

    private ThirdPidRestClient getThirdPidRestClient() {
        if (this.mThirdPidRestClient == null) {
            HomeServerConnectionConfig homeServerConnectionConfig = this.mHsConfig;
            if (homeServerConnectionConfig != null) {
                this.mThirdPidRestClient = new ThirdPidRestClient(homeServerConnectionConfig);
            }
        }
        return this.mThirdPidRestClient;
    }

    private ProfileRestClient getProfileRestClient() {
        if (this.mProfileRestClient == null) {
            HomeServerConnectionConfig homeServerConnectionConfig = this.mHsConfig;
            if (homeServerConnectionConfig != null) {
                this.mProfileRestClient = new ProfileRestClient(homeServerConnectionConfig);
            }
        }
        return this.mProfileRestClient;
    }

    /* access modifiers changed from: private */
    public void setRegistrationFlowResponse(RegistrationFlowResponse registrationFlowResponse) {
        if (registrationFlowResponse != null) {
            this.mRegistrationResponse = registrationFlowResponse;
        }
    }

    private AuthParams getThreePidAuthParams(String str, String str2, String str3, String str4) {
        AuthParamsThreePid authParamsThreePid = new AuthParamsThreePid(str4);
        authParamsThreePid.threePidCredentials.clientSecret = str;
        authParamsThreePid.threePidCredentials.idServer = str2;
        authParamsThreePid.threePidCredentials.sid = str3;
        return authParamsThreePid;
    }

    private AuthParams getCaptchaAuthParams(String str) {
        AuthParamsCaptcha authParamsCaptcha = new AuthParamsCaptcha();
        authParamsCaptcha.response = str;
        return authParamsCaptcha;
    }

    /* access modifiers changed from: private */
    public void requestValidationToken(final ThreePid threePid, final ThreePidRequestListener threePidRequestListener) {
        if (getThirdPidRestClient() != null) {
            String str = threePid.medium;
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != -1064943142) {
                if (hashCode == 96619420 && str.equals("email")) {
                    c = 0;
                }
            } else if (str.equals(ThreePid.MEDIUM_MSISDN)) {
                c = 1;
            }
            if (c == 0) {
                String uri = this.mHsConfig.getHomeserverUri().toString();
                StringBuilder sb = new StringBuilder();
                sb.append(uri);
                sb.append("/#/register?client_secret=");
                sb.append(threePid.clientSecret);
                String sb2 = sb.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append(sb2);
                sb3.append("&hs_url=");
                sb3.append(this.mHsConfig.getHomeserverUri().toString());
                String sb4 = sb3.toString();
                StringBuilder sb5 = new StringBuilder();
                sb5.append(sb4);
                sb5.append("&is_url=");
                sb5.append(this.mHsConfig.getIdentityServerUri().toString());
                String sb6 = sb5.toString();
                StringBuilder sb7 = new StringBuilder();
                sb7.append(sb6);
                sb7.append("&session_id=");
                sb7.append(this.mRegistrationResponse.session);
                threePid.requestEmailValidationToken(getProfileRestClient(), sb7.toString(), true, new ApiCallback<Void>() {
                    public void onSuccess(Void voidR) {
                        threePidRequestListener.onThreePidRequested(threePid);
                    }

                    public void onNetworkError(Exception exc) {
                        RegistrationManager.this.warnAfterCertificateError(exc, threePid, threePidRequestListener);
                    }

                    public void onUnexpectedError(Exception exc) {
                        threePidRequestListener.onThreePidRequested(threePid);
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        if (TextUtils.equals(MatrixError.THREEPID_IN_USE, matrixError.errcode)) {
                            threePidRequestListener.onThreePidRequestFailed(R.string.account_email_already_used_error);
                            return;
                        }
                        if (TextUtils.equals(MatrixError.THREEPID_DENIED, matrixError.errcode)) {
                            threePidRequestListener.onThreePidRequestFailed(R.string.tchap_register_unauthorized_email);
                        } else {
                            threePidRequestListener.onThreePidRequested(threePid);
                        }
                    }
                });
            } else if (c == 1) {
                threePid.requestPhoneNumberValidationToken(getProfileRestClient(), true, new ApiCallback<Void>() {
                    public void onSuccess(Void voidR) {
                        RegistrationManager.this.mPhoneNumber = threePid;
                        threePidRequestListener.onThreePidRequested(threePid);
                    }

                    public void onNetworkError(Exception exc) {
                        RegistrationManager.this.warnAfterCertificateError(exc, threePid, threePidRequestListener);
                    }

                    public void onUnexpectedError(Exception exc) {
                        threePidRequestListener.onThreePidRequested(threePid);
                    }

                    public void onMatrixError(MatrixError matrixError) {
                        if (TextUtils.equals(MatrixError.THREEPID_IN_USE, matrixError.errcode)) {
                            threePidRequestListener.onThreePidRequestFailed(R.string.account_phone_number_already_used_error);
                        } else {
                            threePidRequestListener.onThreePidRequested(threePid);
                        }
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public void warnAfterCertificateError(Exception exc, final ThreePid threePid, final ThreePidRequestListener threePidRequestListener) {
        UnrecognizedCertificateException certificateException = CertUtil.getCertificateException(exc);
        if (certificateException != null) {
            UnrecognizedCertHandler.show(this.mHsConfig, certificateException.getFingerprint(), false, new Callback() {
                public void onAccept() {
                    RegistrationManager.this.requestValidationToken(threePid, threePidRequestListener);
                }

                public void onIgnore() {
                    threePidRequestListener.onThreePidRequested(threePid);
                }

                public void onReject() {
                    threePidRequestListener.onThreePidRequested(threePid);
                }
            });
            return;
        }
        threePidRequestListener.onThreePidRequested(threePid);
    }

    /* access modifiers changed from: private */
    public void register(Context context, RegistrationParams registrationParams, InternalRegistrationListener internalRegistrationListener) {
        if (getLoginRestClient() != null) {
            registrationParams.initial_device_display_name = DinsicUtils.getDeviceName();
            LoginRestClient loginRestClient = this.mLoginRestClient;
            final InternalRegistrationListener internalRegistrationListener2 = internalRegistrationListener;
            final Context context2 = context;
            final RegistrationParams registrationParams2 = registrationParams;
            AnonymousClass8 r1 = new UnrecognizedCertApiCallback<Credentials>(this.mHsConfig) {
                public void onSuccess(Credentials credentials) {
                    String str = "";
                    if (TextUtils.isEmpty(credentials.userId)) {
                        Log.e(RegistrationManager.LOG_TAG, "## register(): ERROR_EMPTY_USER_ID");
                        internalRegistrationListener2.onRegistrationFailed(str);
                        return;
                    }
                    boolean z = false;
                    for (MXSession credentials2 : Matrix.getMXSessions(context2)) {
                        Credentials credentials3 = credentials2.getCredentials();
                        z |= TextUtils.equals(credentials.userId, credentials3.userId) && TextUtils.equals(credentials.homeServer, credentials3.homeServer);
                    }
                    if (RegistrationManager.this.mHsConfig == null) {
                        Log.e(RegistrationManager.LOG_TAG, "## register(): null mHsConfig");
                        internalRegistrationListener2.onRegistrationFailed(str);
                        return;
                    }
                    if (!z) {
                        RegistrationManager.this.mHsConfig.setCredentials(credentials);
                        Matrix.getInstance(context2).addSession(Matrix.getInstance(context2).createSession(RegistrationManager.this.mHsConfig));
                    }
                    internalRegistrationListener2.onRegistrationSuccess();
                }

                public void onAcceptedCert() {
                    RegistrationManager.this.register(context2, registrationParams2, internalRegistrationListener2);
                }

                public void onTLSOrNetworkError(Exception exc) {
                    internalRegistrationListener2.onRegistrationFailed(exc.getLocalizedMessage());
                }

                public void onMatrixError(MatrixError matrixError) {
                    if (TextUtils.equals(matrixError.errcode, MatrixError.USER_IN_USE)) {
                        Log.d(RegistrationManager.LOG_TAG, "User name is used");
                        internalRegistrationListener2.onRegistrationFailed(context2.getString(R.string.login_error_user_in_use));
                    } else if (TextUtils.equals(matrixError.errcode, MatrixError.UNAUTHORIZED)) {
                        internalRegistrationListener2.onWaitingEmailValidation();
                    } else {
                        if (!TextUtils.equals(MatrixError.PASSWORD_TOO_SHORT, matrixError.errcode)) {
                            if (!TextUtils.equals(MatrixError.PASSWORD_NO_DIGIT, matrixError.errcode)) {
                                if (!TextUtils.equals(MatrixError.PASSWORD_NO_UPPERCASE, matrixError.errcode)) {
                                    if (!TextUtils.equals(MatrixError.PASSWORD_NO_LOWERCASE, matrixError.errcode)) {
                                        if (!TextUtils.equals(MatrixError.PASSWORD_NO_SYMBOL, matrixError.errcode)) {
                                            if (!TextUtils.equals(MatrixError.WEAK_PASSWORD, matrixError.errcode)) {
                                                if (TextUtils.equals(MatrixError.PASSWORD_IN_DICTIONARY, matrixError.errcode)) {
                                                    internalRegistrationListener2.onRegistrationFailed(context2.getString(R.string.tchap_password_pwd_in_dict_error));
                                                    return;
                                                } else if (matrixError.mStatus != null && matrixError.mStatus.intValue() == 401) {
                                                    try {
                                                        RegistrationManager.this.setRegistrationFlowResponse(JsonUtils.toRegistrationFlowResponse(matrixError.mErrorBodyAsString));
                                                    } catch (Exception e) {
                                                        String access$600 = RegistrationManager.LOG_TAG;
                                                        StringBuilder sb = new StringBuilder();
                                                        sb.append("JsonUtils.toRegistrationFlowResponse ");
                                                        sb.append(e.getLocalizedMessage());
                                                        Log.e(access$600, sb.toString(), e);
                                                    }
                                                    internalRegistrationListener2.onRegistrationFailed(RegistrationManager.ERROR_MISSING_STAGE);
                                                    return;
                                                } else if (TextUtils.equals(matrixError.errcode, MatrixError.RESOURCE_LIMIT_EXCEEDED)) {
                                                    internalRegistrationListener2.onResourceLimitExceeded(matrixError);
                                                    return;
                                                } else {
                                                    internalRegistrationListener2.onRegistrationFailed("");
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        internalRegistrationListener2.onRegistrationFailed(context2.getString(R.string.tchap_password_weak_pwd_error));
                    }
                }
            };
            loginRestClient.register(registrationParams, r1);
        }
    }
}
