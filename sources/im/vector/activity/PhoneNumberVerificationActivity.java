package im.vector.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import fr.gouv.tchap.a.R;
import im.vector.Matrix;
import im.vector.ui.themes.ActivityOtherThemes;
import im.vector.ui.themes.ActivityOtherThemes.NoActionBar;
import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.core.Log;
import org.matrix.androidsdk.core.callback.ApiCallback;
import org.matrix.androidsdk.core.model.MatrixError;
import org.matrix.androidsdk.rest.model.pid.ThreePid;

public class PhoneNumberVerificationActivity extends VectorAppCompatActivity implements OnEditorActionListener, TextWatcher {
    private static final String EXTRA_MATRIX_ID = "EXTRA_MATRIX_ID";
    private static final String EXTRA_PID = "EXTRA_PID";
    /* access modifiers changed from: private */
    public static final String LOG_TAG = PhoneNumberVerificationActivity.class.getSimpleName();
    private boolean mIsSubmittingToken;
    private TextInputEditText mPhoneNumberCode;
    private TextInputLayout mPhoneNumberCodeLayout;
    private MXSession mSession;
    /* access modifiers changed from: private */
    public ThreePid mThreePid;

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public int getLayoutRes() {
        return R.layout.activity_phone_number_verification;
    }

    public int getMenuRes() {
        return R.menu.menu_phone_number_verification;
    }

    public int getTitleRes() {
        return R.string.settings_phone_number_verification;
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public static Intent getIntent(Context context, String str, ThreePid threePid) {
        Intent intent = new Intent(context, PhoneNumberVerificationActivity.class);
        intent.putExtra("EXTRA_MATRIX_ID", str);
        intent.putExtra(EXTRA_PID, threePid);
        return intent;
    }

    public ActivityOtherThemes getOtherThemes() {
        return NoActionBar.INSTANCE;
    }

    public void initUiAndData() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.mPhoneNumberCode = (TextInputEditText) findViewById(R.id.phone_number_code_value);
        this.mPhoneNumberCodeLayout = (TextInputLayout) findViewById(R.id.phone_number_code);
        setWaitingView(findViewById(R.id.loading_view));
        Intent intent = getIntent();
        this.mSession = Matrix.getInstance(this).getSession(intent.getStringExtra("EXTRA_MATRIX_ID"));
        MXSession mXSession = this.mSession;
        if (mXSession == null || !mXSession.isAlive()) {
            finish();
            return;
        }
        this.mThreePid = (ThreePid) intent.getSerializableExtra(EXTRA_PID);
        this.mPhoneNumberCode.addTextChangedListener(this);
        this.mPhoneNumberCode.setOnEditorActionListener(this);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mIsSubmittingToken = false;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.action_verify_phone_number) {
            return super.onOptionsItemSelected(menuItem);
        }
        submitCode();
        return true;
    }

    private void submitCode() {
        if (!this.mIsSubmittingToken) {
            this.mIsSubmittingToken = true;
            if (TextUtils.isEmpty(this.mPhoneNumberCode.getText())) {
                this.mPhoneNumberCodeLayout.setErrorEnabled(true);
                this.mPhoneNumberCodeLayout.setError(getString(R.string.settings_phone_number_verification_error_empty_code));
                return;
            }
            showWaitingView();
            this.mSession.getThirdPidRestClient().submitValidationToken(this.mThreePid.medium, this.mPhoneNumberCode.getText().toString(), this.mThreePid.clientSecret, this.mThreePid.sid, new ApiCallback<Boolean>() {
                public void onSuccess(Boolean bool) {
                    if (bool.booleanValue()) {
                        Log.e(PhoneNumberVerificationActivity.LOG_TAG, "## submitPhoneNumberValidationToken(): onSuccess() - registerAfterEmailValidations() started");
                        PhoneNumberVerificationActivity phoneNumberVerificationActivity = PhoneNumberVerificationActivity.this;
                        phoneNumberVerificationActivity.registerAfterPhoneNumberValidation(phoneNumberVerificationActivity.mThreePid);
                        return;
                    }
                    Log.e(PhoneNumberVerificationActivity.LOG_TAG, "## submitPhoneNumberValidationToken(): onSuccess() - failed (success=false)");
                    PhoneNumberVerificationActivity phoneNumberVerificationActivity2 = PhoneNumberVerificationActivity.this;
                    phoneNumberVerificationActivity2.onSubmitCodeError(phoneNumberVerificationActivity2.getString(R.string.settings_phone_number_verification_error));
                }

                public void onNetworkError(Exception exc) {
                    PhoneNumberVerificationActivity.this.onSubmitCodeError(exc.getLocalizedMessage());
                }

                public void onMatrixError(MatrixError matrixError) {
                    PhoneNumberVerificationActivity.this.onSubmitCodeError(matrixError.getLocalizedMessage());
                }

                public void onUnexpectedError(Exception exc) {
                    PhoneNumberVerificationActivity.this.onSubmitCodeError(exc.getLocalizedMessage());
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void registerAfterPhoneNumberValidation(ThreePid threePid) {
        this.mSession.getMyUser().add3Pid(threePid, true, new ApiCallback<Void>() {
            public void onSuccess(Void voidR) {
                PhoneNumberVerificationActivity.this.setResult(-1, new Intent());
                PhoneNumberVerificationActivity.this.finish();
            }

            public void onNetworkError(Exception exc) {
                PhoneNumberVerificationActivity.this.onSubmitCodeError(exc.getLocalizedMessage());
            }

            public void onMatrixError(MatrixError matrixError) {
                PhoneNumberVerificationActivity.this.onSubmitCodeError(matrixError.getLocalizedMessage());
            }

            public void onUnexpectedError(Exception exc) {
                PhoneNumberVerificationActivity.this.onSubmitCodeError(exc.getLocalizedMessage());
            }
        });
    }

    /* access modifiers changed from: private */
    public void onSubmitCodeError(String str) {
        this.mIsSubmittingToken = false;
        hideWaitingView();
        Toast.makeText(this, str, 0).show();
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 6 || isFinishing()) {
            return false;
        }
        submitCode();
        return true;
    }

    public void afterTextChanged(Editable editable) {
        if (this.mPhoneNumberCodeLayout.getError() != null) {
            this.mPhoneNumberCodeLayout.setError(null);
            this.mPhoneNumberCodeLayout.setErrorEnabled(false);
        }
    }
}
