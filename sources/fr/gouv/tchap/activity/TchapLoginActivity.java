package fr.gouv.tchap.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import butterknife.BindView;
import butterknife.OnClick;
import fr.gouv.tchap.a.R;
import fr.gouv.tchap.sdk.rest.client.TchapPasswordPolicyRestClient;
import fr.gouv.tchap.sdk.rest.client.TchapRestClient;
import fr.gouv.tchap.sdk.rest.model.PasswordPolicy;
import fr.gouv.tchap.sdk.rest.model.Platform;
import fr.gouv.tchap.util.DinsicUtils;
import fr.gouv.tchap.util.HomeServerConnectionConfigFactoryKt;
import im.vector.LoginHandler;
import im.vector.Matrix;
import im.vector.RegistrationManager;
import im.vector.RegistrationManager.RegistrationListener;
import im.vector.UnrecognizedCertHandler;
import im.vector.UnrecognizedCertHandler.Callback;
import im.vector.activity.AccountCreationCaptchaActivity;
import im.vector.activity.CommonActivityUtils;
import im.vector.activity.MXCActionBarActivity;
import im.vector.activity.SplashActivity;
import im.vector.activity.VectorUniversalLinkActivity;
import im.vector.activity.util.RequestCodesKt;
import im.vector.features.hhs.ResourceLimitDialogHelper;
import im.vector.gcm.GCMHelper;
import im.vector.push.fcm.FcmHelper;
import im.vector.receiver.VectorUniversalLinkReceiver;
import im.vector.services.EventStreamService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;
import org.matrix.androidsdk.HomeServerConnectionConfig;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.JsonUtils;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.callback.SimpleApiCallback;
import org.matrix.androidsdk.core.callback.SuccessCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.client.ProfileRestClient;
import org.matrix.androidsdk.rest.model.login.RegistrationFlowResponse;
import org.matrix.androidsdk.rest.model.login.ThreePidCredentials;
import org.matrix.androidsdk.rest.model.pid.ThreePid;
import org.matrix.androidsdk.ssl.CertUtil;
import org.matrix.androidsdk.ssl.UnrecognizedCertificateException;

public class TchapLoginActivity extends MXCActionBarActivity implements RegistrationListener {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = TchapLoginActivity.class.getSimpleName();
    public static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MODE_ACCOUNT_CREATION = 2;
    private static final int MODE_ACCOUNT_CREATION_WAIT_FOR_EMAIL = 21;
    private static final int MODE_FORGOT_PASSWORD = 3;
    private static final int MODE_FORGOT_PASSWORD_WAITING_VALIDATION = 4;
    private static final int MODE_FORGOT_PASSWORD_WAITING_VALIDATION_2 = 7;
    private static final int MODE_LOGIN = 1;
    private static final int MODE_START = 6;
    private static final int MODE_UNKNOWN = 0;
    private static final String SAVED_CONFIG_EMAIL = "SAVED_CONFIG_EMAIL";
    private static final String SAVED_CREATION_EMAIL_NAME = "SAVED_CREATION_EMAIL_NAME";
    private static final String SAVED_CREATION_PASSWORD1 = "SAVED_CREATION_PASSWORD1";
    private static final String SAVED_CREATION_PASSWORD2 = "SAVED_CREATION_PASSWORD2";
    private static final String SAVED_CREATION_REGISTRATION_RESPONSE = "SAVED_CREATION_REGISTRATION_RESPONSE";
    private static final String SAVED_FORGOT_EMAIL_ADDRESS = "SAVED_FORGOT_EMAIL_ADDRESS";
    private static final String SAVED_FORGOT_PASSWORD1 = "SAVED_FORGOT_PASSWORD1";
    private static final String SAVED_FORGOT_PASSWORD2 = "SAVED_FORGOT_PASSWORD2";
    private static final String SAVED_LOGIN_EMAIL_ADDRESS = "SAVED_LOGIN_EMAIL_ADDRESS";
    private static final String SAVED_LOGIN_PASSWORD_ADDRESS = "SAVED_LOGIN_PASSWORD_ADDRESS";
    private static final String SAVED_MODE = "SAVED_MODE";
    private static final String SAVED_TCHAP_PLATFORM = "SAVED_TCHAP_PLATFORM";
    private EditText mCreationEmailAddressTextView;
    private EditText mCreationPassword1TextView;
    private EditText mCreationPassword2TextView;
    /* access modifiers changed from: private */
    public Dialog mCurrentDialog;
    /* access modifiers changed from: private */
    public String mCurrentEmail;
    /* access modifiers changed from: private */
    public HashMap<String, String> mEmailValidationExtraParams;
    @BindView(2131296521)
    TextView mForgotEmailTextView;
    @BindView(2131296523)
    EditText mForgotPassword1TextView;
    @BindView(2131296524)
    EditText mForgotPassword2TextView;
    /* access modifiers changed from: private */
    public ThreePidCredentials mForgotPid = null;
    private boolean mIsMailValidationPending;
    /* access modifiers changed from: private */
    public boolean mIsPasswordResetted;
    /* access modifiers changed from: private */
    public boolean mIsWaitingNetworkConnection = false;
    private EditText mLoginEmailTextView;
    private final LoginHandler mLoginHandler = new LoginHandler();
    private RelativeLayout mLoginMaskView;
    private EditText mLoginPasswordTextView;
    private View mMainLayout;
    /* access modifiers changed from: private */
    public int mMode = 6;
    private final BroadcastReceiver mNetworkReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            try {
                NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    if (TchapLoginActivity.this.mIsWaitingNetworkConnection) {
                        TchapLoginActivity.this.refreshDisplay();
                    } else {
                        TchapLoginActivity.this.removeNetworkStateNotificationListener();
                    }
                }
            } catch (Exception e) {
                String access$300 = TchapLoginActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## BroadcastReceiver onReceive failed ");
                sb.append(e.getMessage());
                Log.e(access$300, sb.toString());
            }
        }
    };
    private TextView mPasswordForgottenTxtView;
    private TextView mProgressTextView;
    private RegistrationFlowResponse mRegistrationResponse;
    private ResourceLimitDialogHelper mResourceLimitDialogHelper;
    private HomeServerConnectionConfig mServerConfig;
    /* access modifiers changed from: private */
    public Platform mTchapPlatform;
    private Parcelable mUniversalLinkUri;
    @BindView(2131296534)
    Button messageButton;
    @BindView(2131296533)
    TextView messageNotice;
    @BindView(2131296530)
    View screenForgottenPassword;
    @BindView(2131296531)
    View screenLogin;
    @BindView(2131296532)
    View screenMessageButton;
    @BindView(2131296535)
    View screenRegister;
    @BindView(2131296536)
    View screenRegisterWaitForEmail;
    @BindView(2131296542)
    TextView screenRegisterWaitForEmailEmailTextView;
    @BindView(2131296537)
    View screenWelcome;

    public int getLayoutRes() {
        return R.layout.activity_tchap_login;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Dialog dialog = this.mCurrentDialog;
        if (dialog != null) {
            dialog.dismiss();
            this.mCurrentDialog = null;
        }
        RegistrationManager.getInstance().resetSingleton();
        super.onDestroy();
        Log.i(LOG_TAG, "## onDestroy(): IN");
        this.mMode = 0;
        this.mEmailValidationExtraParams = null;
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        removeNetworkStateNotificationListener();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(LOG_TAG, "## onNewIntent(): IN ");
        if (intent == null) {
            Log.d(LOG_TAG, "## onNewIntent(): Unexpected value - aIntent=null ");
            return;
        }
        Bundle extras = intent.getExtras();
        if (extras == null) {
            Log.d(LOG_TAG, "## onNewIntent(): Unexpected value - extras are missing");
        } else if (extras.containsKey(VectorUniversalLinkActivity.EXTRA_EMAIL_VALIDATION_PARAMS)) {
            Log.d(LOG_TAG, "## onNewIntent() Login activity started by email verification for registration");
            if (processEmailValidationExtras(extras)) {
                checkIfMailValidationPending();
            }
        }
    }

    public void initUiAndData() {
        if (getIntent() == null) {
            Log.d(LOG_TAG, "## onCreate(): IN with no intent");
        } else {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## onCreate(): IN with flags ");
            sb.append(Integer.toHexString(getIntent().getFlags()));
            Log.d(str, sb.toString());
        }
        if (VERSION.SDK_INT <= 19) {
            startActivity(new Intent(this, TchapUnsupportedAndroidVersionActivity.class));
            finish();
            return;
        }
        WebView.setWebContentsDebuggingEnabled(false);
        CommonActivityUtils.onApplicationStarted(this);
        FcmHelper.ensureFcmTokenIsRetrieved(this);
        Intent intent = getIntent();
        if (hasCredentials()) {
            if (intent != null && (intent.getFlags() & 4194304) == 0) {
                Log.d(LOG_TAG, "## onCreate(): goToSplash because the credentials are already provided.");
                goToSplash();
            } else if (EventStreamService.getInstance() == null) {
                Log.d(LOG_TAG, "## onCreate(): goToSplash with credentials but there is no event stream service.");
                goToSplash();
            } else {
                Log.d(LOG_TAG, "## onCreate(): close the login screen because it is a temporary task");
            }
            finish();
            return;
        }
        configureToolbar();
        this.mLoginMaskView = (RelativeLayout) findViewById(R.id.flow_ui_mask_login);
        this.mLoginEmailTextView = (EditText) findViewById(R.id.tchap_first_login_email);
        this.mLoginPasswordTextView = (EditText) findViewById(R.id.tchap_first_login_password);
        this.mLoginPasswordTextView.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 6) {
                    return false;
                }
                TchapLoginActivity.this.onClick();
                TchapLoginActivity.this.onLoginClick();
                return true;
            }
        });
        this.mCreationEmailAddressTextView = (EditText) findViewById(R.id.tchap_first_register_email);
        this.mCreationPassword1TextView = (EditText) findViewById(R.id.tchap_first_register_password);
        this.mCreationPassword2TextView = (EditText) findViewById(R.id.tchap_first_register_password_confirm);
        this.mCreationPassword2TextView.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 6) {
                    return false;
                }
                TchapLoginActivity.this.onClick();
                TchapLoginActivity.this.onRegisterClick();
                return true;
            }
        });
        this.mPasswordForgottenTxtView = (TextView) findViewById(R.id.tchap_first_login_password_forgotten);
        this.mProgressTextView = (TextView) findViewById(R.id.flow_progress_message_textview);
        this.mMainLayout = findViewById(R.id.main_input_layout);
        this.mPasswordForgottenTxtView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TchapLoginActivity.this.mMode = 3;
                TchapLoginActivity.this.refreshDisplay();
            }
        });
        Bundle bundle = null;
        if (isFirstCreation()) {
            this.mResourceLimitDialogHelper = new ResourceLimitDialogHelper((Activity) this, (Bundle) null);
        } else {
            Bundle savedInstanceState = getSavedInstanceState();
            this.mResourceLimitDialogHelper = new ResourceLimitDialogHelper((Activity) this, savedInstanceState);
            restoreSavedData(savedInstanceState);
        }
        addToRestorables(this.mResourceLimitDialogHelper);
        refreshDisplay();
        CommonActivityUtils.updateBadgeCount((Context) this, 0);
        if (intent != null) {
            bundle = getIntent().getExtras();
        }
        if (bundle != null) {
            String str2 = VectorUniversalLinkReceiver.EXTRA_UNIVERSAL_LINK_URI;
            if (bundle.containsKey(str2)) {
                this.mUniversalLinkUri = bundle.getParcelable(str2);
                Log.d(LOG_TAG, "## onCreate() Login activity started by universal link");
            } else if (bundle.containsKey(VectorUniversalLinkActivity.EXTRA_EMAIL_VALIDATION_PARAMS)) {
                Log.d(LOG_TAG, "## onCreate() Login activity started by email verification for registration");
                if (processEmailValidationExtras(bundle)) {
                    checkIfMailValidationPending();
                }
            }
        }
    }

    public int getMenuRes() {
        int i = this.mMode;
        if (i == 1 || i == 2) {
            return R.menu.tchap_menu_next;
        }
        return -1;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != 16908332) {
            if (itemId == R.id.action_next) {
                int i = this.mMode;
                if (i == 1) {
                    onClick();
                    onLoginClick();
                    return true;
                } else if (i == 2) {
                    onClick();
                    onRegisterClick();
                    return true;
                }
            }
            return super.onOptionsItemSelected(menuItem);
        }
        int i2 = this.mMode;
        if (i2 == 1 || i2 == 2) {
            fallbackToStartMode();
            return true;
        } else if (i2 == 3) {
            fallbackToLoginMode();
            return true;
        } else if (i2 == 4) {
            this.mForgotPid = null;
            this.mMode = 3;
            refreshDisplay();
            return true;
        } else if (i2 != 7) {
            if (i2 == 21) {
                fallbackToRegistrationMode();
                return true;
            }
            return super.onOptionsItemSelected(menuItem);
        } else {
            fallbackToLoginMode();
            return true;
        }
    }

    public void onBackPressed() {
        int i = this.mMode;
        if (i == 1 || i == 2) {
            Log.d(LOG_TAG, "## fallback to initial screen");
            fallbackToStartMode();
        } else if (i == 3) {
            fallbackToLoginMode();
        } else if (i == 4) {
            this.mForgotPid = null;
            this.mMode = 3;
            refreshDisplay();
        } else if (i == 7) {
            fallbackToLoginMode();
        } else if (i != 21) {
            super.onBackPressed();
        } else {
            fallbackToRegistrationMode();
        }
    }

    /* access modifiers changed from: private */
    public void addNetworkStateNotificationListener() {
        if (Matrix.getInstance(getApplicationContext()) != null && !this.mIsWaitingNetworkConnection) {
            try {
                registerReceiver(this.mNetworkReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
                this.mIsWaitingNetworkConnection = true;
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## addNetworkStateNotificationListener : ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString());
            }
        }
    }

    /* access modifiers changed from: private */
    public void removeNetworkStateNotificationListener() {
        if (Matrix.getInstance(getApplicationContext()) != null && this.mIsWaitingNetworkConnection) {
            try {
                unregisterReceiver(this.mNetworkReceiver);
                this.mIsWaitingNetworkConnection = false;
            } catch (Exception e) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## removeNetworkStateNotificationListener : ");
                sb.append(e.getMessage());
                Log.e(str, sb.toString());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        GCMHelper.checkLastVersion(this);
    }

    /* access modifiers changed from: private */
    public void fallbackToLoginMode() {
        onClick();
        this.mMainLayout.setVisibility(0);
        this.mEmailValidationExtraParams = null;
        this.mRegistrationResponse = null;
        showMainLayout();
        enableLoadingScreen(false);
        this.mForgotPassword1TextView.setText(null);
        this.mForgotPassword2TextView.setText(null);
        this.mForgotPid = null;
        this.mMode = 1;
        refreshDisplay();
    }

    private void fallbackToStartMode() {
        onClick();
        this.mMainLayout.setVisibility(0);
        this.mEmailValidationExtraParams = null;
        this.mRegistrationResponse = null;
        showMainLayout();
        enableLoadingScreen(false);
        this.mMode = 6;
        refreshDisplay();
    }

    private void fallbackToRegistrationMode() {
        showMainLayout();
        enableLoadingScreen(false);
        this.mMode = 2;
        refreshDisplay();
    }

    private boolean hasCredentials() {
        boolean z = false;
        try {
            MXSession defaultSession = Matrix.getInstance(this).getDefaultSession();
            if (defaultSession != null && defaultSession.isAlive()) {
                z = true;
            }
            return z;
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("## Exception: ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString());
            Log.e(LOG_TAG, "## hasCredentials() : invalid credentials");
            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        CommonActivityUtils.logout(TchapLoginActivity.this);
                    } catch (Exception e) {
                        String access$300 = TchapLoginActivity.LOG_TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("## Exception: ");
                        sb.append(e.getMessage());
                        Log.w(access$300, sb.toString());
                    }
                }
            });
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void goToSplash() {
        Log.d(LOG_TAG, "## gotoSplash(): Go to splash.");
        Intent intent = new Intent(this, SplashActivity.class);
        Parcelable parcelable = this.mUniversalLinkUri;
        if (parcelable != null) {
            intent.putExtra(VectorUniversalLinkReceiver.EXTRA_UNIVERSAL_LINK_URI, parcelable);
        }
        startActivity(intent);
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296529})
    public void onForgotPasswordClick() {
        onClick();
        final String trim = this.mForgotEmailTextView.getText().toString().toLowerCase(Locale.ROOT).trim();
        final String trim2 = this.mForgotPassword1TextView.getText().toString().trim();
        String trim3 = this.mForgotPassword2TextView.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            Toast.makeText(getApplicationContext(), getString(R.string.auth_reset_password_missing_email), 0).show();
        } else if (TextUtils.isEmpty(trim2)) {
            Toast.makeText(getApplicationContext(), getString(R.string.auth_reset_password_missing_password), 0).show();
        } else if (!TextUtils.equals(trim2, trim3)) {
            Toast.makeText(getApplicationContext(), getString(R.string.auth_password_dont_match), 0).show();
        } else if (TextUtils.isEmpty(trim) || Patterns.EMAIL_ADDRESS.matcher(trim).matches()) {
            enableLoadingScreen(true);
            Log.d(LOG_TAG, "## onForgotPasswordClick()");
            discoverTchapPlatform((Activity) this, trim, (ApiCallback<Platform>) new ApiCallback<Platform>() {
                private void onError(String str) {
                    TchapLoginActivity tchapLoginActivity = TchapLoginActivity.this;
                    if (str == null) {
                        str = tchapLoginActivity.getString(R.string.auth_invalid_email);
                    }
                    Toast.makeText(tchapLoginActivity, str, 1).show();
                    TchapLoginActivity.this.enableLoadingScreen(false);
                }

                public void onSuccess(Platform platform) {
                    Log.d(TchapLoginActivity.LOG_TAG, "## onForgotPasswordClick(): discoverTchapPlatform succeeds");
                    if (platform.hs == null || platform.hs.isEmpty()) {
                        Log.e(TchapLoginActivity.LOG_TAG, "## onForgotPasswordClick(): invalid platform");
                        onError(TchapLoginActivity.this.getString(R.string.tchap_register_unauthorized_email));
                        return;
                    }
                    TchapLoginActivity.this.mTchapPlatform = platform;
                    TchapLoginActivity.this.mCurrentEmail = trim;
                    HomeServerConnectionConfig access$900 = TchapLoginActivity.this.getHsConfig();
                    TchapLoginActivity.this.checkPasswordValidity(trim2, access$900, new SuccessCallback(access$900, trim) {
                        private final /* synthetic */ HomeServerConnectionConfig f$1;
                        private final /* synthetic */ String f$2;

                        {
                            this.f$1 = r2;
                            this.f$2 = r3;
                        }

                        public final void onSuccess(Object obj) {
                            AnonymousClass6.this.lambda$onSuccess$0$TchapLoginActivity$6(this.f$1, this.f$2, (Boolean) obj);
                        }
                    });
                }

                public /* synthetic */ void lambda$onSuccess$0$TchapLoginActivity$6(final HomeServerConnectionConfig homeServerConnectionConfig, String str, Boolean bool) {
                    if (bool.booleanValue()) {
                        new ProfileRestClient(homeServerConnectionConfig).forgetPassword(str, new ApiCallback<ThreePid>() {
                            public void onSuccess(ThreePid threePid) {
                                if (TchapLoginActivity.this.mMode == 3) {
                                    Log.d(TchapLoginActivity.LOG_TAG, "## onForgotPasswordClick(): requestEmailValidationToken succeeds");
                                    TchapLoginActivity.this.enableLoadingScreen(false);
                                    TchapLoginActivity.this.mMode = 4;
                                    TchapLoginActivity.this.refreshDisplay();
                                    TchapLoginActivity.this.mForgotPid = new ThreePidCredentials();
                                    TchapLoginActivity.this.mForgotPid.clientSecret = threePid.clientSecret;
                                    TchapLoginActivity.this.mForgotPid.idServer = homeServerConnectionConfig.getIdentityServerUri().getHost();
                                    TchapLoginActivity.this.mForgotPid.sid = threePid.sid;
                                }
                            }

                            /* access modifiers changed from: private */
                            public void onError(String str) {
                                String access$300 = TchapLoginActivity.LOG_TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("## onForgotPasswordClick(): requestEmailValidationToken fails with error ");
                                sb.append(str);
                                Log.e(access$300, sb.toString());
                                if (TchapLoginActivity.this.mMode == 3) {
                                    TchapLoginActivity.this.enableLoadingScreen(false);
                                    Toast.makeText(TchapLoginActivity.this, str, 1).show();
                                }
                            }

                            public void onNetworkError(final Exception exc) {
                                if (TchapLoginActivity.this.mMode == 3) {
                                    UnrecognizedCertificateException certificateException = CertUtil.getCertificateException(exc);
                                    if (certificateException != null) {
                                        UnrecognizedCertHandler.show(homeServerConnectionConfig, certificateException.getFingerprint(), false, new Callback() {
                                            public void onAccept() {
                                                TchapLoginActivity.this.onForgotPasswordClick();
                                            }

                                            public void onIgnore() {
                                                AnonymousClass1.this.onError(exc.getLocalizedMessage());
                                            }

                                            public void onReject() {
                                                AnonymousClass1.this.onError(exc.getLocalizedMessage());
                                            }
                                        });
                                        return;
                                    }
                                    onError(exc.getLocalizedMessage());
                                }
                            }

                            public void onUnexpectedError(Exception exc) {
                                onError(exc.getLocalizedMessage());
                            }

                            public void onMatrixError(MatrixError matrixError) {
                                if (TextUtils.equals(MatrixError.THREEPID_NOT_FOUND, matrixError.errcode)) {
                                    onError(TchapLoginActivity.this.getString(R.string.account_email_not_found_error));
                                } else {
                                    onError(matrixError.getLocalizedMessage());
                                }
                            }
                        });
                        return;
                    }
                    TchapLoginActivity.this.enableLoadingScreen(false);
                    Toast.makeText(TchapLoginActivity.this.getApplicationContext(), TchapLoginActivity.this.getString(R.string.tchap_password_weak_pwd_error), 1).show();
                }

                public void onNetworkError(Exception exc) {
                    onError(exc.getLocalizedMessage());
                }

                public void onMatrixError(MatrixError matrixError) {
                    onError(matrixError.getLocalizedMessage());
                }

                public void onUnexpectedError(Exception exc) {
                    onError(exc.getLocalizedMessage());
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.auth_invalid_email), 0).show();
        }
    }

    /* access modifiers changed from: private */
    public void onForgotOnEmailValidated(HomeServerConnectionConfig homeServerConnectionConfig) {
        if (this.mIsPasswordResetted) {
            Log.d(LOG_TAG, "onForgotOnEmailValidated : go back to login screen");
            this.mIsPasswordResetted = false;
            fallbackToLoginMode();
            return;
        }
        ProfileRestClient profileRestClient = new ProfileRestClient(homeServerConnectionConfig);
        enableLoadingScreen(true);
        Log.d(LOG_TAG, "onForgotOnEmailValidated : try to reset the password");
        profileRestClient.resetPassword(this.mForgotPassword1TextView.getText().toString().trim(), this.mForgotPid, new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                if (TchapLoginActivity.this.mMode == 4) {
                    Log.d(TchapLoginActivity.LOG_TAG, "onForgotOnEmailValidated : the password has been updated");
                    TchapLoginActivity.this.enableLoadingScreen(false);
                    TchapLoginActivity.this.mMode = 7;
                    TchapLoginActivity.this.mIsPasswordResetted = true;
                    TchapLoginActivity.this.refreshDisplay();
                }
            }

            private void onError(String str, boolean z, boolean z2) {
                if (TchapLoginActivity.this.mMode == 4) {
                    String access$300 = TchapLoginActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onForgotOnEmailValidated : failed ");
                    sb.append(str);
                    Log.d(access$300, sb.toString());
                    Toast.makeText(TchapLoginActivity.this.getApplicationContext(), str, 1).show();
                    TchapLoginActivity.this.enableLoadingScreen(false);
                    if (z) {
                        TchapLoginActivity.this.fallbackToLoginMode();
                    } else if (z2) {
                        TchapLoginActivity.this.onBackPressed();
                    }
                }
            }

            public void onNetworkError(Exception exc) {
                onError(exc.getLocalizedMessage(), false, false);
            }

            public void onMatrixError(MatrixError matrixError) {
                if (TchapLoginActivity.this.mMode != 4) {
                    return;
                }
                if (TextUtils.equals(matrixError.errcode, MatrixError.UNAUTHORIZED)) {
                    Log.d(TchapLoginActivity.LOG_TAG, "onForgotOnEmailValidated : failed UNAUTHORIZED");
                    onError(TchapLoginActivity.this.getResources().getString(R.string.auth_reset_password_error_unauthorized), false, false);
                    return;
                }
                if (!TextUtils.equals(MatrixError.PASSWORD_TOO_SHORT, matrixError.errcode)) {
                    if (!TextUtils.equals(MatrixError.PASSWORD_NO_DIGIT, matrixError.errcode)) {
                        if (!TextUtils.equals(MatrixError.PASSWORD_NO_UPPERCASE, matrixError.errcode)) {
                            if (!TextUtils.equals(MatrixError.PASSWORD_NO_LOWERCASE, matrixError.errcode)) {
                                if (!TextUtils.equals(MatrixError.PASSWORD_NO_SYMBOL, matrixError.errcode)) {
                                    if (!TextUtils.equals(MatrixError.WEAK_PASSWORD, matrixError.errcode)) {
                                        if (TextUtils.equals(MatrixError.PASSWORD_IN_DICTIONARY, matrixError.errcode)) {
                                            onError(TchapLoginActivity.this.getResources().getString(R.string.tchap_password_pwd_in_dict_error), false, true);
                                            return;
                                        } else {
                                            onError(matrixError.getLocalizedMessage(), true, false);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                onError(TchapLoginActivity.this.getResources().getString(R.string.tchap_password_weak_pwd_error), false, true);
            }

            public void onUnexpectedError(Exception exc) {
                onError(exc.getLocalizedMessage(), true, false);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onFailureDuringAuthRequest(MatrixError matrixError) {
        String str;
        enableLoadingScreen(false);
        String str2 = matrixError.errcode;
        if (MatrixError.RESOURCE_LIMIT_EXCEEDED.equals(str2)) {
            Log.e(LOG_TAG, "## onFailureDuringAuthRequest(): RESOURCE_LIMIT_EXCEEDED");
            this.mResourceLimitDialogHelper.displayDialog(matrixError);
            return;
        }
        if (TextUtils.equals(str2, MatrixError.FORBIDDEN)) {
            str = getString(R.string.login_error_forbidden);
        } else if (TextUtils.equals(str2, MatrixError.UNKNOWN_TOKEN)) {
            str = getString(R.string.login_error_unknown_token);
        } else if (TextUtils.equals(str2, MatrixError.BAD_JSON)) {
            str = getString(R.string.login_error_bad_json);
        } else if (TextUtils.equals(str2, MatrixError.NOT_JSON)) {
            str = getString(R.string.login_error_not_json);
        } else if (TextUtils.equals(str2, MatrixError.LIMIT_EXCEEDED)) {
            str = getString(R.string.login_error_limit_exceeded);
        } else if (TextUtils.equals(str2, MatrixError.USER_IN_USE)) {
            str = getString(R.string.login_error_user_in_use);
        } else if (TextUtils.equals(str2, MatrixError.LOGIN_EMAIL_URL_NOT_YET)) {
            str = getString(R.string.login_error_login_email_not_yet);
        } else {
            str = matrixError.getLocalizedMessage();
        }
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onFailureDuringAuthRequest(): Msg= \"");
        sb.append(str);
        sb.append("\"");
        Log.e(str3, sb.toString());
        Toast.makeText(getApplicationContext(), str, 1).show();
    }

    private boolean processEmailValidationExtras(Bundle bundle) {
        Log.d(LOG_TAG, "## processEmailValidationExtras() IN");
        boolean z = true;
        if (bundle != null) {
            this.mEmailValidationExtraParams = (HashMap) bundle.getSerializable(VectorUniversalLinkActivity.EXTRA_EMAIL_VALIDATION_PARAMS);
            if (this.mEmailValidationExtraParams != null) {
                this.mIsMailValidationPending = true;
                this.mMode = 2;
                Matrix.getInstance(this).clearSessions(this, true, null);
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## processEmailValidationExtras() OUT - reCode=");
                sb.append(z);
                Log.d(str, sb.toString());
                return z;
            }
        } else {
            Log.e(LOG_TAG, "## processEmailValidationExtras(): Bundle is missing - aRegistrationBundle=null");
        }
        z = false;
        String str2 = LOG_TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("## processEmailValidationExtras() OUT - reCode=");
        sb2.append(z);
        Log.d(str2, sb2.toString());
        return z;
    }

    /* access modifiers changed from: private */
    public void startEmailOwnershipValidation(HashMap<String, String> hashMap) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## startEmailOwnershipValidation(): IN aMapParams=");
        sb.append(hashMap);
        Log.d(str, sb.toString());
        if (hashMap != null) {
            enableLoadingScreen(true);
            hideMainLayoutAndToast("");
            this.mMode = 2;
            String str2 = (String) hashMap.get(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_TOKEN);
            String str3 = (String) hashMap.get(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_CLIENT_SECRET);
            String str4 = (String) hashMap.get(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_IDENTITY_SERVER_SESSION_ID);
            String str5 = (String) hashMap.get(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_SESSION_ID);
            String str6 = (String) hashMap.get(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_HOME_SERVER_URL);
            String str7 = (String) hashMap.get(VectorUniversalLinkActivity.KEY_MAIL_VALIDATION_IDENTITY_SERVER_URL);
            if (str6 == null) {
                str6 = getHomeServerUrl();
            }
            String str8 = str6;
            if (str7 == null) {
                str7 = getIdentityServerUrl();
            }
            String str9 = str7;
            try {
                Uri.parse(str8);
                Uri.parse(str9);
                submitEmailToken(str2, str3, str4, str5, str8, str9);
            } catch (Exception unused) {
                Toast.makeText(this, getString(R.string.login_error_invalid_home_server), 0).show();
            }
        } else {
            Log.d(LOG_TAG, "## startEmailOwnershipValidation(): skipped");
        }
    }

    private void submitEmailToken(String str, String str2, String str3, String str4, String str5, String str6) {
        HomeServerConnectionConfig createHomeServerConnectionConfig = HomeServerConnectionConfigFactoryKt.createHomeServerConnectionConfig(str5, str6);
        this.mServerConfig = createHomeServerConnectionConfig;
        RegistrationManager.getInstance().setHsConfig(createHomeServerConnectionConfig);
        Log.d(LOG_TAG, "## submitEmailToken(): IN");
        if (this.mMode == 2) {
            Log.d(LOG_TAG, "## submitEmailToken(): calling submitEmailTokenValidation()..");
            LoginHandler loginHandler = this.mLoginHandler;
            Context applicationContext = getApplicationContext();
            final String str7 = str4;
            final String str8 = str2;
            final HomeServerConnectionConfig homeServerConnectionConfig = createHomeServerConnectionConfig;
            final String str9 = str3;
            final String str10 = str6;
            AnonymousClass8 r0 = new ApiCallback<Boolean>() {
                private void errorHandler(String str) {
                    Log.d(TchapLoginActivity.LOG_TAG, "## submitEmailToken(): errorHandler().");
                    TchapLoginActivity.this.enableLoadingScreen(false);
                    TchapLoginActivity.this.showMainLayout();
                    TchapLoginActivity.this.refreshDisplay();
                    Toast.makeText(TchapLoginActivity.this.getApplicationContext(), str, 1).show();
                }

                public void onSuccess(Boolean bool) {
                    if (!bool.booleanValue()) {
                        Log.d(TchapLoginActivity.LOG_TAG, "## submitEmailToken(): onSuccess() - failed (success=false)");
                        errorHandler(TchapLoginActivity.this.getString(R.string.login_error_unable_register_mail_ownership));
                    } else if (str7 == null) {
                        Log.d(TchapLoginActivity.LOG_TAG, "## submitEmailToken(): onSuccess() - the password update is in progress");
                        TchapLoginActivity.this.mMode = 4;
                        TchapLoginActivity.this.mForgotPid = new ThreePidCredentials();
                        TchapLoginActivity.this.mForgotPid.clientSecret = str8;
                        TchapLoginActivity.this.mForgotPid.idServer = homeServerConnectionConfig.getIdentityServerUri().getHost();
                        TchapLoginActivity.this.mForgotPid.sid = str9;
                        TchapLoginActivity.this.mIsPasswordResetted = false;
                        TchapLoginActivity.this.onForgotOnEmailValidated(homeServerConnectionConfig);
                    } else {
                        Log.d(TchapLoginActivity.LOG_TAG, "## submitEmailToken(): onSuccess() - registerAfterEmailValidations() started");
                        TchapLoginActivity.this.mMode = 2;
                        TchapLoginActivity.this.enableLoadingScreen(true);
                        RegistrationManager instance = RegistrationManager.getInstance();
                        TchapLoginActivity tchapLoginActivity = TchapLoginActivity.this;
                        instance.registerAfterEmailValidation(tchapLoginActivity, str8, str9, str10, str7, tchapLoginActivity);
                    }
                }

                public void onNetworkError(Exception exc) {
                    String access$300 = TchapLoginActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## submitEmailToken(): onNetworkError() Msg=");
                    sb.append(exc.getLocalizedMessage());
                    Log.d(access$300, sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(TchapLoginActivity.this.getString(R.string.login_error_unable_register));
                    sb2.append(" : ");
                    sb2.append(exc.getLocalizedMessage());
                    errorHandler(sb2.toString());
                }

                public void onMatrixError(MatrixError matrixError) {
                    String access$300 = TchapLoginActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## submitEmailToken(): onMatrixError() Msg=");
                    sb.append(matrixError.getLocalizedMessage());
                    Log.d(access$300, sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(TchapLoginActivity.this.getString(R.string.login_error_unable_register));
                    sb2.append(" : ");
                    sb2.append(matrixError.getLocalizedMessage());
                    errorHandler(sb2.toString());
                }

                public void onUnexpectedError(Exception exc) {
                    String access$300 = TchapLoginActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## submitEmailToken(): onUnexpectedError() Msg=");
                    sb.append(exc.getLocalizedMessage());
                    Log.d(access$300, sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(TchapLoginActivity.this.getString(R.string.login_error_unable_register));
                    sb2.append(" : ");
                    sb2.append(exc.getLocalizedMessage());
                    errorHandler(sb2.toString());
                }
            };
            loginHandler.submitEmailTokenValidation(applicationContext, createHomeServerConnectionConfig, str, str2, str9, r0);
        }
    }

    /* access modifiers changed from: private */
    public void onRegistrationFlow(RegistrationFlowResponse registrationFlowResponse) {
        enableLoadingScreen(false);
        this.mRegistrationResponse = registrationFlowResponse;
    }

    private void checkIfMailValidationPending() {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## checkIfMailValidationPending(): mIsMailValidationPending=");
        sb.append(this.mIsMailValidationPending);
        Log.d(str, sb.toString());
        if (this.mIsMailValidationPending) {
            this.mIsMailValidationPending = false;
            runOnUiThread(new Runnable() {
                public void run() {
                    if (TchapLoginActivity.this.mEmailValidationExtraParams != null) {
                        TchapLoginActivity tchapLoginActivity = TchapLoginActivity.this;
                        tchapLoginActivity.startEmailOwnershipValidation(tchapLoginActivity.mEmailValidationExtraParams);
                    }
                }
            });
            return;
        }
        Log.d(LOG_TAG, "## checkIfMailValidationPending(): pending mail validation not started");
    }

    private void checkRegistrationFlows(final SimpleApiCallback<Void> simpleApiCallback) {
        Log.d(LOG_TAG, "## checkRegistrationFlows(): IN");
        if (this.mMode == 2) {
            if (this.mRegistrationResponse == null) {
                try {
                    HomeServerConnectionConfig hsConfig = getHsConfig();
                    if (hsConfig != null) {
                        enableLoadingScreen(true);
                        this.mLoginHandler.getSupportedRegistrationFlows(this, hsConfig, new SimpleApiCallback<Void>() {
                            public void onSuccess(Void voidR) {
                            }

                            private void onError(String str) {
                                if (TchapLoginActivity.this.mMode == 2) {
                                    TchapLoginActivity.this.showMainLayout();
                                    TchapLoginActivity.this.enableLoadingScreen(false);
                                    Toast.makeText(TchapLoginActivity.this.getApplicationContext(), str, 1).show();
                                }
                            }

                            public void onNetworkError(Exception exc) {
                                TchapLoginActivity.this.addNetworkStateNotificationListener();
                                if (TchapLoginActivity.this.mMode == 2) {
                                    String access$300 = TchapLoginActivity.LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Network Error: ");
                                    sb.append(exc.getMessage());
                                    Log.e(access$300, sb.toString(), exc);
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append(TchapLoginActivity.this.getString(R.string.login_error_registration_network_error));
                                    sb2.append(" : ");
                                    sb2.append(exc.getLocalizedMessage());
                                    onError(sb2.toString());
                                }
                            }

                            public void onUnexpectedError(Exception exc) {
                                if (TchapLoginActivity.this.mMode == 2) {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(TchapLoginActivity.this.getString(R.string.login_error_unable_register));
                                    sb.append(" : ");
                                    sb.append(exc.getLocalizedMessage());
                                    onError(sb.toString());
                                }
                            }

                            /* JADX WARNING: Removed duplicated region for block: B:15:0x0070  */
                            /* JADX WARNING: Removed duplicated region for block: B:16:0x0082  */
                            public void onMatrixError(MatrixError matrixError) {
                                RegistrationFlowResponse registrationFlowResponse;
                                TchapLoginActivity.this.removeNetworkStateNotificationListener();
                                if (TchapLoginActivity.this.mMode == 2) {
                                    String access$300 = TchapLoginActivity.LOG_TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("## checkRegistrationFlows(): onMatrixError - Resp=");
                                    sb.append(matrixError.getLocalizedMessage());
                                    Log.d(access$300, sb.toString());
                                    if (matrixError.mStatus != null) {
                                        if (matrixError.mStatus.intValue() == 401) {
                                            try {
                                                registrationFlowResponse = JsonUtils.toRegistrationFlowResponse(matrixError.mErrorBodyAsString);
                                            } catch (Exception e) {
                                                String access$3002 = TchapLoginActivity.LOG_TAG;
                                                StringBuilder sb2 = new StringBuilder();
                                                sb2.append("JsonUtils.toRegistrationFlowResponse ");
                                                sb2.append(e.getLocalizedMessage());
                                                Log.e(access$3002, sb2.toString());
                                            }
                                            if (registrationFlowResponse == null) {
                                                RegistrationManager.getInstance().setSupportedRegistrationFlows(registrationFlowResponse);
                                                TchapLoginActivity.this.onRegistrationFlow(registrationFlowResponse);
                                                simpleApiCallback.onSuccess(null);
                                                return;
                                            }
                                            TchapLoginActivity.this.onFailureDuringAuthRequest(matrixError);
                                            return;
                                        } else if (matrixError.mStatus.intValue() == 403) {
                                            TchapLoginActivity.this.refreshDisplay();
                                        }
                                    }
                                    registrationFlowResponse = null;
                                    if (registrationFlowResponse == null) {
                                    }
                                }
                            }
                        });
                    }
                } catch (Exception unused) {
                    Toast.makeText(getApplicationContext(), getString(R.string.login_error_invalid_home_server), 0).show();
                    enableLoadingScreen(false);
                }
            } else {
                simpleApiCallback.onSuccess(null);
            }
        }
    }

    /* access modifiers changed from: private */
    public void checkPasswordValidity(final String str, HomeServerConnectionConfig homeServerConnectionConfig, final SuccessCallback<Boolean> successCallback) {
        Log.d(LOG_TAG, "## checkPasswordValidity(): IN");
        new TchapPasswordPolicyRestClient(homeServerConnectionConfig).getPasswordPolicy(new ApiCallback<PasswordPolicy>() {
            public void onSuccess(PasswordPolicy passwordPolicy) {
                Log.d(TchapLoginActivity.LOG_TAG, "## checkPasswordValidity(): getPasswordPolicy succeeds");
                Boolean valueOf = Boolean.valueOf(true);
                int length = str.length();
                int i = passwordPolicy.minLength;
                Boolean valueOf2 = Boolean.valueOf(false);
                if (length >= i && ((!passwordPolicy.isDigitRequired || Pattern.compile("[0-9]").matcher(str).find()) && ((!passwordPolicy.isUppercaseRequired || Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZ]").matcher(str).find()) && ((!passwordPolicy.isLowercaseRequired || Pattern.compile("[abcdefghijklmnopqrstuvwxyz]").matcher(str).find()) && (!passwordPolicy.isSymbolRequired || Pattern.compile("[^a-zA-Z0-9]").matcher(str).find()))))) {
                    valueOf2 = valueOf;
                }
                successCallback.onSuccess(valueOf2);
            }

            private void onError(String str) {
                String access$300 = TchapLoginActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## checkPasswordValidity(): getPasswordPolicy fails with error ");
                sb.append(str);
                Log.e(access$300, sb.toString());
                successCallback.onSuccess(Boolean.valueOf(str.length() >= 8));
            }

            public void onNetworkError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }

            public void onUnexpectedError(Exception exc) {
                onError(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                onError(matrixError.getLocalizedMessage());
            }
        });
    }

    private void hideMainLayoutAndToast(String str) {
        this.mMainLayout.setVisibility(8);
        this.mProgressTextView.setVisibility(0);
        this.mProgressTextView.setText(str);
    }

    /* access modifiers changed from: private */
    public void showMainLayout() {
        this.mMainLayout.setVisibility(0);
        this.mProgressTextView.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void onClick() {
        if (getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296538})
    public void onLoginClick() {
        onClick();
        if (this.mMode != 1) {
            showMainLayout();
            this.mMode = 1;
            refreshDisplay();
            return;
        }
        final String trim = this.mLoginEmailTextView.getText().toString().toLowerCase(Locale.ROOT).trim();
        final String trim2 = this.mLoginPasswordTextView.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(trim).matches()) {
            Toast.makeText(this, getString(R.string.auth_invalid_login_param), 0).show();
        } else if (TextUtils.isEmpty(trim2)) {
            Toast.makeText(this, getString(R.string.auth_invalid_login_param), 0).show();
        } else {
            enableLoadingScreen(true);
            Log.d(LOG_TAG, "## onLoginClick()");
            discoverTchapPlatform((Activity) this, trim, (ApiCallback<Platform>) new ApiCallback<Platform>() {
                private void onError(String str) {
                    TchapLoginActivity tchapLoginActivity = TchapLoginActivity.this;
                    if (str == null) {
                        str = tchapLoginActivity.getString(R.string.login_error_unable_login);
                    }
                    Toast.makeText(tchapLoginActivity, str, 1).show();
                    TchapLoginActivity.this.enableLoadingScreen(false);
                }

                public void onSuccess(Platform platform) {
                    if (platform.hs == null || platform.hs.isEmpty()) {
                        Log.e(TchapLoginActivity.LOG_TAG, "## onLoginClick(): invalid platform");
                        onError(TchapLoginActivity.this.getString(R.string.login_error_unable_login));
                        return;
                    }
                    TchapLoginActivity.this.mTchapPlatform = platform;
                    TchapLoginActivity.this.mCurrentEmail = trim;
                    TchapLoginActivity.this.login(TchapLoginActivity.this.getHsConfig(), trim, null, null, trim2);
                }

                public void onNetworkError(Exception exc) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(TchapLoginActivity.this.getString(R.string.login_error_unable_login));
                    sb.append(" : ");
                    sb.append(exc.getLocalizedMessage());
                    onError(sb.toString());
                }

                public void onMatrixError(MatrixError matrixError) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(TchapLoginActivity.this.getString(R.string.login_error_unable_login));
                    sb.append(" : ");
                    sb.append(matrixError.getLocalizedMessage());
                    onError(sb.toString());
                }

                public void onUnexpectedError(Exception exc) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(TchapLoginActivity.this.getString(R.string.login_error_unable_login));
                    sb.append(" : ");
                    sb.append(exc.getLocalizedMessage());
                    onError(sb.toString());
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void login(HomeServerConnectionConfig homeServerConnectionConfig, String str, String str2, String str3, String str4) {
        try {
            this.mLoginHandler.login(this, homeServerConnectionConfig, str, str2, str3, str4, new SimpleApiCallback<Void>(this) {
                public void onSuccess(Void voidR) {
                    TchapLoginActivity.this.enableLoadingScreen(false);
                    TchapLoginActivity.this.goToSplash();
                    TchapLoginActivity.this.finish();
                }

                public void onNetworkError(Exception exc) {
                    String access$300 = TchapLoginActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## login(): Network Error: ");
                    sb.append(exc.getMessage());
                    Log.e(access$300, sb.toString());
                    TchapLoginActivity.this.enableLoadingScreen(false);
                    Toast.makeText(TchapLoginActivity.this.getApplicationContext(), TchapLoginActivity.this.getString(R.string.login_error_network_error), 1).show();
                }

                public void onUnexpectedError(Exception exc) {
                    String access$300 = TchapLoginActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## login(): onUnexpectedError");
                    sb.append(exc.getMessage());
                    Log.e(access$300, sb.toString());
                    TchapLoginActivity.this.enableLoadingScreen(false);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(TchapLoginActivity.this.getString(R.string.login_error_unable_login));
                    sb2.append(" : ");
                    sb2.append(exc.getMessage());
                    Toast.makeText(TchapLoginActivity.this.getApplicationContext(), sb2.toString(), 1).show();
                }

                public void onMatrixError(MatrixError matrixError) {
                    String access$300 = TchapLoginActivity.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("## login(): onMatrixError ");
                    sb.append(matrixError.getLocalizedMessage());
                    Log.e(access$300, sb.toString());
                    TchapLoginActivity.this.enableLoadingScreen(false);
                    TchapLoginActivity.this.onFailureDuringAuthRequest(matrixError);
                }
            });
        } catch (Exception unused) {
            Toast.makeText(this, getString(R.string.login_error_invalid_home_server), 0).show();
            enableLoadingScreen(false);
        }
    }

    private void restoreSavedData(Bundle bundle) {
        Log.d(LOG_TAG, "## restoreSavedData(): IN");
        if (bundle != null) {
            this.mLoginEmailTextView.setText(bundle.getString(SAVED_LOGIN_EMAIL_ADDRESS));
            this.mLoginPasswordTextView.setText(bundle.getString(SAVED_LOGIN_PASSWORD_ADDRESS));
            this.mCreationEmailAddressTextView.setText(bundle.getString(SAVED_CREATION_EMAIL_NAME));
            this.mCreationPassword1TextView.setText(bundle.getString(SAVED_CREATION_PASSWORD1));
            this.mCreationPassword2TextView.setText(bundle.getString(SAVED_CREATION_PASSWORD2));
            this.mForgotEmailTextView.setText(bundle.getString(SAVED_FORGOT_EMAIL_ADDRESS));
            this.mForgotPassword1TextView.setText(bundle.getString(SAVED_FORGOT_PASSWORD1));
            this.mForgotPassword2TextView.setText(bundle.getString(SAVED_FORGOT_PASSWORD2));
            this.mRegistrationResponse = (RegistrationFlowResponse) bundle.getSerializable(SAVED_CREATION_REGISTRATION_RESPONSE);
            this.mMode = bundle.getInt(SAVED_MODE, 1);
            String str = VectorUniversalLinkReceiver.EXTRA_UNIVERSAL_LINK_URI;
            if (bundle.containsKey(str)) {
                this.mUniversalLinkUri = bundle.getParcelable(str);
            }
            this.mTchapPlatform = (Platform) bundle.getSerializable(SAVED_TCHAP_PLATFORM);
            this.mCurrentEmail = bundle.getString(SAVED_CONFIG_EMAIL);
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Log.d(LOG_TAG, "## onSaveInstanceState(): IN");
        if (!TextUtils.isEmpty(this.mLoginEmailTextView.getText().toString().trim())) {
            bundle.putString(SAVED_LOGIN_EMAIL_ADDRESS, this.mLoginEmailTextView.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(this.mLoginPasswordTextView.getText().toString().trim())) {
            bundle.putString(SAVED_LOGIN_PASSWORD_ADDRESS, this.mLoginPasswordTextView.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(this.mCreationEmailAddressTextView.getText().toString().trim())) {
            bundle.putString(SAVED_CREATION_EMAIL_NAME, this.mCreationEmailAddressTextView.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(this.mCreationPassword1TextView.getText().toString().trim())) {
            bundle.putString(SAVED_CREATION_PASSWORD1, this.mCreationPassword1TextView.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(this.mCreationPassword2TextView.getText().toString().trim())) {
            bundle.putString(SAVED_CREATION_PASSWORD2, this.mCreationPassword2TextView.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(this.mForgotEmailTextView.getText().toString().trim())) {
            bundle.putString(SAVED_FORGOT_EMAIL_ADDRESS, this.mForgotEmailTextView.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(this.mForgotPassword1TextView.getText().toString().trim())) {
            bundle.putString(SAVED_FORGOT_PASSWORD1, this.mForgotPassword1TextView.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(this.mForgotPassword2TextView.getText().toString().trim())) {
            bundle.putString(SAVED_FORGOT_PASSWORD2, this.mForgotPassword2TextView.getText().toString().trim());
        }
        Platform platform = this.mTchapPlatform;
        if (platform != null) {
            bundle.putSerializable(SAVED_TCHAP_PLATFORM, platform);
            String str = this.mCurrentEmail;
            if (str != null) {
                bundle.putString(SAVED_CONFIG_EMAIL, str);
            }
        }
        RegistrationFlowResponse registrationFlowResponse = this.mRegistrationResponse;
        if (registrationFlowResponse != null) {
            bundle.putSerializable(SAVED_CREATION_REGISTRATION_RESPONSE, registrationFlowResponse);
        }
        Parcelable parcelable = this.mUniversalLinkUri;
        if (parcelable != null) {
            bundle.putParcelable(VectorUniversalLinkReceiver.EXTRA_UNIVERSAL_LINK_URI, parcelable);
        }
        bundle.putInt(SAVED_MODE, this.mMode);
    }

    /* access modifiers changed from: private */
    public void refreshDisplay() {
        int i = this.mMode;
        if (i == 1) {
            this.toolbar.setVisibility(0);
            this.toolbar.setTitle((int) R.string.tchap_connection_title);
            this.screenWelcome.setVisibility(8);
            this.screenRegister.setVisibility(8);
            this.screenRegisterWaitForEmail.setVisibility(8);
            this.screenLogin.setVisibility(0);
            this.screenForgottenPassword.setVisibility(8);
            this.screenMessageButton.setVisibility(8);
        } else if (i == 2) {
            this.toolbar.setVisibility(0);
            this.toolbar.setTitle((int) R.string.tchap_register_title);
            this.screenWelcome.setVisibility(8);
            this.screenRegister.setVisibility(0);
            this.screenRegisterWaitForEmail.setVisibility(8);
            this.screenLogin.setVisibility(8);
            this.screenForgottenPassword.setVisibility(8);
            this.screenMessageButton.setVisibility(8);
        } else if (i != 3) {
            if (i != 4) {
                if (i == 6) {
                    this.toolbar.setVisibility(8);
                    this.screenWelcome.setVisibility(0);
                    this.screenRegister.setVisibility(8);
                    this.screenRegisterWaitForEmail.setVisibility(8);
                    this.screenLogin.setVisibility(8);
                    this.screenForgottenPassword.setVisibility(8);
                    this.screenMessageButton.setVisibility(8);
                } else if (i != 7) {
                    if (i != 21) {
                        this.toolbar.setTitle((CharSequence) "");
                    } else {
                        this.toolbar.setVisibility(0);
                        this.toolbar.setTitle((int) R.string.tchap_register_title);
                        this.screenWelcome.setVisibility(8);
                        this.screenRegister.setVisibility(8);
                        this.screenRegisterWaitForEmail.setVisibility(0);
                        this.screenLogin.setVisibility(8);
                        this.screenForgottenPassword.setVisibility(8);
                        this.screenMessageButton.setVisibility(8);
                    }
                }
            }
            this.toolbar.setVisibility(0);
            this.toolbar.setTitle((int) R.string.tchap_connection_title);
            this.screenWelcome.setVisibility(8);
            this.screenRegister.setVisibility(8);
            this.screenRegisterWaitForEmail.setVisibility(8);
            this.screenLogin.setVisibility(8);
            this.screenForgottenPassword.setVisibility(8);
            this.screenMessageButton.setVisibility(0);
        } else {
            this.toolbar.setVisibility(0);
            this.toolbar.setTitle((int) R.string.tchap_connection_title);
            this.screenWelcome.setVisibility(8);
            this.screenRegister.setVisibility(8);
            this.screenRegisterWaitForEmail.setVisibility(8);
            this.screenLogin.setVisibility(8);
            this.screenForgottenPassword.setVisibility(0);
            this.screenMessageButton.setVisibility(8);
        }
        int i2 = this.mMode;
        if (i2 != 4) {
            if (i2 == 7) {
                this.messageNotice.setText(R.string.auth_reset_password_success_message);
                this.messageButton.setText(R.string.auth_return_to_login);
            } else if (i2 == 21) {
                this.screenRegisterWaitForEmailEmailTextView.setText(this.mCurrentEmail);
            }
            supportInvalidateOptionsMenu();
        }
        this.messageNotice.setText(getString(R.string.auth_reset_password_email_validation_message, new Object[]{this.mCurrentEmail}));
        this.messageButton.setText(R.string.auth_reset_password_next_step_button);
        supportInvalidateOptionsMenu();
    }

    /* access modifiers changed from: private */
    public void enableLoadingScreen(boolean z) {
        supportInvalidateOptionsMenu();
        RelativeLayout relativeLayout = this.mLoginMaskView;
        if (relativeLayout != null) {
            relativeLayout.setVisibility(z ? 0 : 8);
        }
    }

    private static String sanitizeUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        return str.replaceAll("\\s", "");
    }

    /* access modifiers changed from: private */
    public HomeServerConnectionConfig getHsConfig() {
        try {
            this.mServerConfig = null;
            this.mServerConfig = HomeServerConnectionConfigFactoryKt.createHomeServerConnectionConfig(getHomeServerUrl(), getIdentityServerUrl());
        } catch (Exception e) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getHsConfig fails ");
            sb.append(e.getLocalizedMessage());
            Log.e(str, sb.toString());
        }
        return this.mServerConfig;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onActivityResult(): IN - requestCode=");
        sb.append(i);
        sb.append(" resultCode=");
        sb.append(i2);
        Log.d(str, sb.toString());
        if (316 != i) {
            return;
        }
        if (i2 == -1) {
            Log.d(LOG_TAG, "## onActivityResult(): CAPTCHA_CREATION_ACTIVITY_REQUEST_CODE => RESULT_OK");
            RegistrationManager.getInstance().setCaptchaResponse(intent.getStringExtra("response"));
            createAccount();
            return;
        }
        Log.d(LOG_TAG, "## onActivityResult(): CAPTCHA_CREATION_ACTIVITY_REQUEST_CODE => RESULT_KO");
        this.mRegistrationResponse = null;
        showMainLayout();
        enableLoadingScreen(false);
        refreshDisplay();
    }

    private void createAccount() {
        Dialog dialog = this.mCurrentDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
        enableLoadingScreen(true);
        hideMainLayoutAndToast("");
        RegistrationManager.getInstance().attemptRegistration(this, this);
    }

    public void onRegistrationSuccess(String str) {
        enableLoadingScreen(false);
        if (!TextUtils.isEmpty(str)) {
            Dialog dialog = this.mCurrentDialog;
            if (dialog != null) {
                dialog.dismiss();
            }
            this.mCurrentDialog = new Builder(this).setTitle((int) R.string.dialog_title_warning).setMessage((CharSequence) str).setPositiveButton((int) R.string.ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    TchapLoginActivity.this.goToSplash();
                    TchapLoginActivity.this.finish();
                }
            }).show();
            return;
        }
        goToSplash();
        finish();
    }

    public void onRegistrationFailed(String str) {
        this.mEmailValidationExtraParams = null;
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onRegistrationFailed(): ");
        sb.append(str);
        Log.e(str2, sb.toString());
        fallbackToRegistrationMode();
        if (str.length() == 0) {
            str = getString(R.string.login_error_unable_register);
        }
        Toast.makeText(this, str, 1).show();
    }

    public void onWaitingEmailValidation() {
        Log.d(LOG_TAG, "## onWaitingEmailValidation()");
        enableLoadingScreen(false);
        this.mMode = 21;
        refreshDisplay();
    }

    public void onWaitingCaptcha() {
        String captchaPublicKey = RegistrationManager.getInstance().getCaptchaPublicKey();
        if (!TextUtils.isEmpty(captchaPublicKey)) {
            Log.d(LOG_TAG, "## onWaitingCaptcha");
            Intent intent = new Intent(this, AccountCreationCaptchaActivity.class);
            intent.putExtra(AccountCreationCaptchaActivity.EXTRA_HOME_SERVER_URL, getHomeServerUrl());
            intent.putExtra(AccountCreationCaptchaActivity.EXTRA_SITE_KEY, captchaPublicKey);
            startActivityForResult(intent, RequestCodesKt.CAPTCHA_CREATION_ACTIVITY_REQUEST_CODE);
            return;
        }
        Log.d(LOG_TAG, "## onWaitingCaptcha(): captcha flow cannot be done");
        Toast.makeText(this, getString(R.string.login_error_unable_register), 0).show();
    }

    public void onThreePidRequestFailed(String str) {
        String str2 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## onThreePidRequestFailed():");
        sb.append(str);
        Log.d(str2, sb.toString());
        enableLoadingScreen(false);
        showMainLayout();
        refreshDisplay();
        Toast.makeText(this, str, 0).show();
    }

    public void onResourceLimitExceeded(MatrixError matrixError) {
        enableLoadingScreen(false);
        this.mResourceLimitDialogHelper.displayDialog(matrixError);
    }

    private String getHomeServerUrl() {
        Platform platform = this.mTchapPlatform;
        if (platform == null || platform.hs == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.server_url_prefix));
        sb.append(this.mTchapPlatform.hs);
        return sb.toString();
    }

    private String getIdentityServerUrl() {
        Platform platform = this.mTchapPlatform;
        if (platform == null || platform.hs == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.server_url_prefix));
        sb.append(this.mTchapPlatform.hs);
        return sb.toString();
    }

    public static void discoverTchapPlatform(Activity activity, String str, ApiCallback<Platform> apiCallback) {
        String str2;
        String str3 = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("## discoverTchapPlatform [");
        sb.append(str);
        sb.append("]");
        Log.d(str3, sb.toString());
        ArrayList arrayList = new ArrayList();
        MXSession defaultSession = Matrix.getInstance(activity).getDefaultSession();
        if (defaultSession != null) {
            str2 = defaultSession.getHomeServerConfig().getIdentityServerUri().toString();
            arrayList.add(str2);
        } else {
            str2 = null;
        }
        ArrayList arrayList2 = new ArrayList(Arrays.asList(activity.getResources().getStringArray(R.array.preferred_identity_server_names)));
        while (!arrayList2.isEmpty()) {
            String str4 = (String) arrayList2.remove(new Random().nextInt(arrayList2.size()));
            StringBuilder sb2 = new StringBuilder();
            sb2.append(activity.getString(R.string.server_url_prefix));
            sb2.append(str4);
            String sb3 = sb2.toString();
            if (str2 == null || !sb3.equals(str2)) {
                arrayList.add(sb3);
            }
        }
        ArrayList arrayList3 = new ArrayList(Arrays.asList(activity.getResources().getStringArray(R.array.identity_server_names)));
        while (!arrayList3.isEmpty()) {
            String str5 = (String) arrayList3.remove(new Random().nextInt(arrayList3.size()));
            StringBuilder sb4 = new StringBuilder();
            sb4.append(activity.getString(R.string.server_url_prefix));
            sb4.append(str5);
            String sb5 = sb4.toString();
            if (str2 == null || !sb5.equals(str2)) {
                arrayList.add(sb5);
            }
        }
        discoverTchapPlatform(str, (List<String>) arrayList, apiCallback);
    }

    /* access modifiers changed from: private */
    public static void discoverTchapPlatform(final String str, final List<String> list, final ApiCallback<Platform> apiCallback) {
        if (list.isEmpty()) {
            apiCallback.onMatrixError(new MatrixError(MatrixError.UNKNOWN, "No host"));
        }
        String str2 = (String) list.remove(0);
        new TchapRestClient(HomeServerConnectionConfigFactoryKt.createHomeServerConnectionConfig(str2, str2)).info(str, "email", new ApiCallback<Platform>() {
            public void onSuccess(Platform platform) {
                String access$300 = TchapLoginActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## discoverTchapPlatform succeeded (");
                sb.append(platform.hs);
                sb.append(")");
                Log.d(access$300, sb.toString());
                apiCallback.onSuccess(platform);
            }

            public void onNetworkError(Exception exc) {
                String access$300 = TchapLoginActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## discoverTchapPlatform failed ");
                sb.append(exc.getMessage());
                Log.e(access$300, sb.toString());
                if (list.isEmpty()) {
                    apiCallback.onNetworkError(exc);
                } else {
                    TchapLoginActivity.discoverTchapPlatform(str, list, apiCallback);
                }
            }

            public void onMatrixError(MatrixError matrixError) {
                String access$300 = TchapLoginActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## discoverTchapPlatform failed ");
                sb.append(matrixError.getMessage());
                Log.e(access$300, sb.toString());
                if (list.isEmpty()) {
                    apiCallback.onMatrixError(matrixError);
                } else {
                    TchapLoginActivity.discoverTchapPlatform(str, list, apiCallback);
                }
            }

            public void onUnexpectedError(Exception exc) {
                String access$300 = TchapLoginActivity.LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("## discoverTchapPlatform failed ");
                sb.append(exc.getMessage());
                Log.e(access$300, sb.toString());
                if (list.isEmpty()) {
                    apiCallback.onUnexpectedError(exc);
                } else {
                    TchapLoginActivity.discoverTchapPlatform(str, list, apiCallback);
                }
            }
        });
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296539})
    public void onRegisterClick() {
        Log.d(LOG_TAG, "## onRegisterClick(): IN");
        onClick();
        if (this.mMode != 2) {
            this.mMode = 2;
            refreshDisplay();
            return;
        }
        final String trim = this.mCreationEmailAddressTextView.getText().toString().toLowerCase(Locale.ROOT).trim();
        if (TextUtils.isEmpty(trim) || !Patterns.EMAIL_ADDRESS.matcher(trim).matches()) {
            Toast.makeText(this, R.string.auth_invalid_email, 0).show();
            return;
        }
        final String trim2 = this.mCreationPassword1TextView.getText().toString().trim();
        String trim3 = this.mCreationPassword2TextView.getText().toString().trim();
        if (TextUtils.isEmpty(trim2)) {
            Toast.makeText(getApplicationContext(), getString(R.string.auth_missing_password), 0).show();
        } else if (!TextUtils.equals(trim2, trim3)) {
            Toast.makeText(getApplicationContext(), getString(R.string.auth_password_dont_match), 0).show();
        } else {
            enableLoadingScreen(true);
            discoverTchapPlatform((Activity) this, trim, (ApiCallback<Platform>) new ApiCallback<Platform>() {
                private void onError(String str) {
                    TchapLoginActivity.this.enableLoadingScreen(false);
                    TchapLoginActivity tchapLoginActivity = TchapLoginActivity.this;
                    if (str == null) {
                        str = tchapLoginActivity.getString(R.string.auth_invalid_email);
                    }
                    Toast.makeText(tchapLoginActivity, str, 1).show();
                }

                public void onSuccess(Platform platform) {
                    if (platform.hs == null || platform.hs.isEmpty()) {
                        Log.e(TchapLoginActivity.LOG_TAG, "## onRegisterClick(): unauthorized email");
                        onError(TchapLoginActivity.this.getString(R.string.tchap_register_unauthorized_email));
                        return;
                    }
                    TchapLoginActivity.this.mTchapPlatform = platform;
                    TchapLoginActivity.this.mCurrentEmail = trim;
                    if (DinsicUtils.isExternalTchapServer(platform.hs)) {
                        if (TchapLoginActivity.this.mCurrentDialog != null) {
                            TchapLoginActivity.this.mCurrentDialog.dismiss();
                        }
                        TchapLoginActivity tchapLoginActivity = TchapLoginActivity.this;
                        tchapLoginActivity.mCurrentDialog = new Builder(tchapLoginActivity).setTitle((int) R.string.tchap_register_warning_for_external_title).setCancelable(false).setMessage((int) R.string.tchap_register_warning_for_external).setPositiveButton((int) R.string.tchap_register_warning_for_external_proceed, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TchapLoginActivity.this.checkPasswordValidityBeforeRegistration(trim2);
                            }
                        }).setNegativeButton((int) R.string.cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TchapLoginActivity.this.enableLoadingScreen(false);
                            }
                        }).show();
                    } else {
                        TchapLoginActivity.this.checkPasswordValidityBeforeRegistration(trim2);
                    }
                }

                public void onNetworkError(Exception exc) {
                    onError(exc.getLocalizedMessage());
                }

                public void onMatrixError(MatrixError matrixError) {
                    onError(matrixError.getLocalizedMessage());
                }

                public void onUnexpectedError(Exception exc) {
                    onError(exc.getLocalizedMessage());
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void checkPasswordValidityBeforeRegistration(String str) {
        HomeServerConnectionConfig hsConfig = getHsConfig();
        checkPasswordValidity(str, hsConfig, new SuccessCallback(hsConfig, str) {
            private final /* synthetic */ HomeServerConnectionConfig f$1;
            private final /* synthetic */ String f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            public final void onSuccess(Object obj) {
                TchapLoginActivity.this.lambda$checkPasswordValidityBeforeRegistration$0$TchapLoginActivity(this.f$1, this.f$2, (Boolean) obj);
            }
        });
    }

    public /* synthetic */ void lambda$checkPasswordValidityBeforeRegistration$0$TchapLoginActivity(HomeServerConnectionConfig homeServerConnectionConfig, String str, Boolean bool) {
        if (bool.booleanValue()) {
            RegistrationManager.getInstance().setHsConfig(homeServerConnectionConfig);
            RegistrationManager.getInstance().setAccountData(null, str);
            RegistrationManager.getInstance().clearThreePid();
            RegistrationManager.getInstance().addEmailThreePid(new ThreePid(this.mCurrentEmail, "email"));
            checkRegistrationFlows(new SimpleApiCallback<Void>() {
                public void onSuccess(Void voidR) {
                    RegistrationManager instance = RegistrationManager.getInstance();
                    TchapLoginActivity tchapLoginActivity = TchapLoginActivity.this;
                    instance.attemptRegistration(tchapLoginActivity, tchapLoginActivity);
                }
            });
            return;
        }
        enableLoadingScreen(false);
        Toast.makeText(getApplicationContext(), getString(R.string.tchap_password_weak_pwd_error), 1).show();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296543})
    public void goToLoginScreen() {
        fallbackToLoginMode();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296541})
    public void onEmailNotReceived() {
        fallbackToRegistrationMode();
    }

    /* access modifiers changed from: 0000 */
    @OnClick({2131296534})
    public void messageSubmit() {
        onForgotOnEmailValidated(getHsConfig());
    }
}
