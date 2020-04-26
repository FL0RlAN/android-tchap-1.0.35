package im.vector;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import fr.gouv.tchap.util.DinsicUtils;
import im.vector.settings.VectorLocale;
import java.util.List;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.client.LoginRestClient;
import org.matrix.androidsdk.rest.client.ThirdPidRestClient;
import org.matrix.androidsdk.rest.model.login.Credentials;
import org.matrix.androidsdk.rest.model.login.LoginFlow;
import org.matrix.androidsdk.rest.model.login.RegistrationParams;
import org.matrix.androidsdk.rest.model.pid.ThreePid;

public class LoginHandler {
    /* access modifiers changed from: private */
    public void onRegistrationDone(Context context, HomeServerConnectionConfig homeServerConnectionConfig, Credentials credentials, ApiCallback<Void> apiCallback) {
        if (TextUtils.isEmpty(credentials.userId)) {
            apiCallback.onMatrixError(new MatrixError(MatrixError.FORBIDDEN, "No user id"));
            return;
        }
        boolean z = false;
        for (MXSession credentials2 : Matrix.getMXSessions(context)) {
            Credentials credentials3 = credentials2.getCredentials();
            z |= TextUtils.equals(credentials.userId, credentials3.userId) && TextUtils.equals(credentials.homeServer, credentials3.homeServer);
        }
        if (!z) {
            homeServerConnectionConfig.setCredentials(credentials);
            Matrix.getInstance(context).addSession(Matrix.getInstance(context).createSession(homeServerConnectionConfig));
        }
        apiCallback.onSuccess(null);
    }

    public void login(Context context, HomeServerConnectionConfig homeServerConnectionConfig, String str, String str2, String str3, String str4, ApiCallback<Void> apiCallback) {
        final Context applicationContext = context.getApplicationContext();
        final HomeServerConnectionConfig homeServerConnectionConfig2 = homeServerConnectionConfig;
        final ApiCallback<Void> apiCallback2 = apiCallback;
        final String str5 = str;
        final String str6 = str2;
        final String str7 = str3;
        final String str8 = str4;
        AnonymousClass1 r0 = new UnrecognizedCertApiCallback<Credentials>(homeServerConnectionConfig, apiCallback) {
            public void onSuccess(Credentials credentials) {
                LoginHandler.this.onRegistrationDone(applicationContext, homeServerConnectionConfig2, credentials, apiCallback2);
            }

            public void onAcceptedCert() {
                LoginHandler.this.login(applicationContext, homeServerConnectionConfig2, str5, str6, str7, str8, apiCallback2);
            }
        };
        callLogin(context, homeServerConnectionConfig, str, str2, str3, str4, r0);
    }

    private void callLogin(Context context, HomeServerConnectionConfig homeServerConnectionConfig, String str, String str2, String str3, String str4, ApiCallback<Credentials> apiCallback) {
        LoginRestClient loginRestClient = new LoginRestClient(homeServerConnectionConfig);
        String deviceName = DinsicUtils.getDeviceName();
        if (!TextUtils.isEmpty(str)) {
            if (Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
                loginRestClient.loginWith3Pid("email", str.toLowerCase(VectorLocale.INSTANCE.getApplicationLocale()), str4, deviceName, null, apiCallback);
                return;
            }
            loginRestClient.loginWithUser(str, str4, deviceName, null, apiCallback);
        } else if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
            loginRestClient.loginWithPhoneNumber(str2, str3, str4, deviceName, null, apiCallback);
        }
    }

    public void getSupportedLoginFlows(Context context, HomeServerConnectionConfig homeServerConnectionConfig, ApiCallback<List<LoginFlow>> apiCallback) {
        final Context applicationContext = context.getApplicationContext();
        LoginRestClient loginRestClient = new LoginRestClient(homeServerConnectionConfig);
        final ApiCallback<List<LoginFlow>> apiCallback2 = apiCallback;
        final HomeServerConnectionConfig homeServerConnectionConfig2 = homeServerConnectionConfig;
        AnonymousClass2 r0 = new UnrecognizedCertApiCallback<List<LoginFlow>>(homeServerConnectionConfig, apiCallback) {
            public void onSuccess(List<LoginFlow> list) {
                apiCallback2.onSuccess(list);
            }

            public void onAcceptedCert() {
                LoginHandler.this.getSupportedLoginFlows(applicationContext, homeServerConnectionConfig2, apiCallback2);
            }
        };
        loginRestClient.getSupportedLoginFlows(r0);
    }

    public void getSupportedRegistrationFlows(Context context, HomeServerConnectionConfig homeServerConnectionConfig, ApiCallback<Void> apiCallback) {
        RegistrationParams registrationParams = new RegistrationParams();
        final Context applicationContext = context.getApplicationContext();
        LoginRestClient loginRestClient = new LoginRestClient(homeServerConnectionConfig);
        registrationParams.initial_device_display_name = DinsicUtils.getDeviceName();
        final HomeServerConnectionConfig homeServerConnectionConfig2 = homeServerConnectionConfig;
        final ApiCallback<Void> apiCallback2 = apiCallback;
        AnonymousClass3 r1 = new UnrecognizedCertApiCallback<Credentials>(homeServerConnectionConfig, apiCallback) {
            public void onSuccess(Credentials credentials) {
                LoginHandler.this.onRegistrationDone(applicationContext, homeServerConnectionConfig2, credentials, apiCallback2);
            }

            public void onAcceptedCert() {
                LoginHandler.this.getSupportedRegistrationFlows(applicationContext, homeServerConnectionConfig2, apiCallback2);
            }
        };
        loginRestClient.register(registrationParams, r1);
    }

    public void submitEmailTokenValidation(Context context, HomeServerConnectionConfig homeServerConnectionConfig, String str, String str2, String str3, ApiCallback<Boolean> apiCallback) {
        ThreePid threePid = new ThreePid(null, "email");
        final HomeServerConnectionConfig homeServerConnectionConfig2 = homeServerConnectionConfig;
        ThirdPidRestClient thirdPidRestClient = new ThirdPidRestClient(homeServerConnectionConfig);
        final ApiCallback<Boolean> apiCallback2 = apiCallback;
        final Context context2 = context;
        final String str4 = str;
        final String str5 = str2;
        final String str6 = str3;
        AnonymousClass4 r2 = new UnrecognizedCertApiCallback<Boolean>(homeServerConnectionConfig, apiCallback) {
            public void onSuccess(Boolean bool) {
                apiCallback2.onSuccess(bool);
            }

            public void onAcceptedCert() {
                LoginHandler.this.submitEmailTokenValidation(context2, homeServerConnectionConfig2, str4, str5, str6, apiCallback2);
            }
        };
        threePid.submitValidationToken(thirdPidRestClient, str, str2, str3, r2);
    }
}
